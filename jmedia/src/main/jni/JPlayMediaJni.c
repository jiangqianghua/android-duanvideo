#include <jni.h>
#include <android/log.h>
#include <stdio.h>
#include <stdbool.h>
#include <pthread.h>
#include <unistd.h>
#include "libavformat/avformat.h"
#include "libavcodec/avcodec.h"
#include "libavcodec/version.h"
#include "libavutil/channel_layout.h"
#include "libavutil/common.h"
#include "libavutil/imgutils.h"
#include "libavutil/mathematics.h"
#include "libavutil/samplefmt.h"
#include "libavutil/avutil.h"
#include "libavformat/avformat.h"
#include "libavfilter/avfilter.h"
#include "libswscale/swscale.h"
#include "libswresample/swresample.h"
#include "sonic.h"

#define OPEN_LOG

#define LOG_TAG    "JPlayMediaJni"
#undef LOG
#ifdef  OPEN_LOG
#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#define LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define LOGW(...)  __android_log_print(ANDROID_LOG_WARN,LOG_TAG,__VA_ARGS__)
#define LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#define LOGF(...)  __android_log_print(ANDROID_LOG_FATAL,LOG_TAG,__VA_ARGS__)
#else
#define LOGD(...)
#define LOGI(...)
#define LOGW(...)
#define LOGE(...)
#define LOGF(...)
#endif

#define VIDEO_PICTURE_QUEUE_SIZE 1
#define AV_SYNC_THRESHOLD   0.01
#define AV_NOSYNC_THRESHOLD 10.0
#define RESUME_WAITINH_TIME 5*1000
// 播放状态
#define PLAY_STATUS_STOP 1
#define PLAY_STATUS_START 2
#define PLAY_STATUS_PAUSE 3
#define PLAY_STATUS_PREPARE 4

// 返回给java的key
#define MEDIA_PREPARED 1
#define MEDIA_PLAYBACK_COMPLETE 2
#define MEDIA_BUFFERING_UPDATE 3
#define MEDIA_SEEK_COMPLETE 4
#define MEDIA_ERROR 100
#define MEDIA_ERROR_OPENURL 101
#define MEDIA_ERROR_FINDSTREAM 102
#define MEDIA_ERROR_FIND_VIDEO_DECODE 103
#define MEDIA_ERROR_OPEN_VIDEO_DECODE 104
#define MEDIA_ERROR_FIND_AUDIO_DECODE 105
#define MEDIA_ERROR_OPEN_AUDIO_DECODE 106
#define MEDIA_INFO 200

/****************回调函数相关*****************/
static jobject jPlayMediaJniObj;

static jmethodID jni_perpare ;
static jmethodID jni_flush_video_data;
static jmethodID jni_flush_audio_short_data ;
static jmethodID jni_flush_audio_byte_data ;
static jmethodID jni_play_completion ;
static jmethodID jni_seek_complete ;
static jmethodID jni_error;
static jmethodID jni_info ;

static jmethodID jni_postEventFromNative;

static JavaVM *g_jvm = NULL;

/****************音视频队列相关*****************/
// 读取数据包的队列
typedef struct PacketQueue
{
    AVPacketList *first_ptk ; // 队头
    AVPacketList *last_pkt ;  //队尾
    int nb_packets ;          // 包的个数
    int size ;                // 占用空间的字节数
    pthread_mutex_t mutex ;
}PacketQueue ;

/****************一些结构体*****************/
typedef struct AVFFmpegCtx
{
    AVFormatContext *pFormatCtx ;
} AVFFmpegCtx;

typedef struct PlayStatus
{
    // 一些状态控制
    bool isPlay ;
    bool isReadStop ;
    bool isResum  ;
    int64_t seekpos  ;
    // 倍速控制
    float speed  ;
    // 音频相关
    double audio_clock ;

    // 视频相关
    double frame_last_pts;
    double frame_last_delay;
    double frame_timer;
    const char *url ;
    PacketQueue audioq;
    PacketQueue videoq;
    // 初始化部分：先创建一个流
    sonicStream tempoStream_;

    int video_index ;
    int audio_index ;

    int videoWdith ;
    int videoHeight ;
    int64_t duration ;

    int sample_rate ;
    int sample_rate_speed ;
    int isStereo ;
    int is16Bit;
    int desiredFrames ;
    int channels ;
    int play_status ;
    double last_show_time ;
    bool isVRunning;
    bool isARunning ;

} PlayStatus;


/****************结构体对象相关*****************/
PacketQueue *packetQueue ;
PlayStatus *playStatus ;
AVFFmpegCtx *avFFmpegCtx ;

/****************函数声明*****************/
void init_ffmpeg();
void init_params();
void init_alloc();
void myffmpeglog2(void* p, int v, const char* str, va_list lis);
int hasVideo();
int hasAudio();
void decode_video_stream();
void decode_audio_stream();
double get_audio_clock();
void call_back_videodata(const uint8_t *pData, int width, int height,JNIEnv *env );
void call_back_aduiodata(AVFrame *frame,JNIEnv *env );
double nowTime();

// 队列相关
void packet_queue_init(PacketQueue *q); // 初始化队列
int packet_queue_put(PacketQueue *q, AVPacket *pkt); // 加入队列
int packet_queue_get(PacketQueue *q,AVPacket *pkt, int block);  // 获取队列数据，block 是否堵塞
void packet_queue_destory(PacketQueue *q);
void packet_queue_clean(PacketQueue *q);
void postEventFromNative(jint what,jint arg1,jint arg2,jobject obj);

// 线程相关
void *prepareAsync(void *arg);
void *recv_thread(void *arg);
void *video_thread(void *arg);
void *audio_thread(void *arg);

/****************java调用的函数*****************/

// 初始化调用
void Java_com_jqh_jmedia_JMediaPlayer__1initNative(JNIEnv *env,jobject jobj)
{
    init_ffmpeg();
    init_alloc();
    init_params();

    playStatus->isARunning = false;
    playStatus->isVRunning = false;
//保存全局JVM以便在子线程中使用
    (*env)->GetJavaVM(env,&g_jvm);
    LOGD("init1 - %p %p" ,g_jvm,jobj);
    //com/jqh/jmedia/JMediaPlayer
    jclass cls = (*env)->FindClass(env,"com/jqh/jmedia/JMediaPlayer");
    if(cls == NULL)
    {
        LOGD("Can not find cls by  com/jqh/jmedia/JMediaPlayer" );
    }
    jPlayMediaJniObj = (jobject)((*env)->NewGlobalRef(env, jobj));

    // jni_perpare = (*env)->GetMethodID(env, cls,
    //                             "jni_perpare", "()V");
    jni_flush_video_data = (*env)->GetMethodID(env, cls,
                                "jni_flush_video_data", "([B)V");
    jni_flush_audio_byte_data = (*env)->GetMethodID(env, cls,
                                "jni_flush_audio_byte_data", "([B)V");
    jni_postEventFromNative = (*env)->GetMethodID(env,cls,
                                "postEventFromNative","(IIILjava/lang/Object;)V");

}


// 设置源数据
void Java_com_jqh_jmedia_JMediaPlayer__1setDataSource(JNIEnv *env, jobject jobject,jstring url,jobjectArray keys,jobjectArray values)
{
    playStatus->url = (*env)->GetStringUTFChars(env,url,NULL);

    (*env)->DeleteLocalRef(env,url);
}

// 准备播放，异步操作，准备完成回调给java上层
void Java_com_jqh_jmedia_JMediaPlayer_prepareAsync(JNIEnv *env, jobject jobject)
{

    // 开辟线程执行
    pthread_t tid ;
    // 创建一个接受线程
    int result = pthread_create(&tid,NULL,prepareAsync,NULL);
    if(result != 0)
    {
        LOGD("prepareAsync thread created error %d",result);
    }
    pthread_detach(tid);
}

void Java_com_jqh_jmedia_JMediaPlayer__1start(JNIEnv *env, jobject jobject)
{
    LOGD("media opetator -- begin start");
    if(playStatus->play_status == PLAY_STATUS_PREPARE)
    {
        LOGD("media opetator -- start");
        // 开启线程刷数据
        pthread_t tid ;
        int result = pthread_create(&tid,NULL,recv_thread,NULL);
        pthread_detach(tid);
        if(hasVideo())
        {
            pthread_t tid_v ;
            int result_v = pthread_create(&tid_v,NULL,video_thread,NULL);
            pthread_detach(tid_v);
        }

        if(hasAudio())
        {
            pthread_t tid_a ;
            int result_a = pthread_create(&tid_a,NULL,audio_thread,NULL);
            pthread_detach(tid_a);
        }
        playStatus->play_status = PLAY_STATUS_START ;
    }else if(playStatus->play_status == PLAY_STATUS_PAUSE){
        LOGD("media opetator -- go on");
        playStatus->isResum = true ;
        playStatus->play_status = PLAY_STATUS_START ;
    }
}

void Java_com_jqh_jmedia_JMediaPlayer__1stop(JNIEnv *env, jobject jobject)
{
    LOGD("media opetator -- stop");
    playStatus->play_status = PLAY_STATUS_STOP;
    if(playStatus)
        playStatus->isPlay = false;
}

void Java_com_jqh_jmedia_JMediaPlayer__1pause(JNIEnv *env, jobject jobject)
{
    LOGD("media opetator -- pause");
    playStatus->play_status = PLAY_STATUS_PAUSE;
    if(playStatus)
        playStatus->isResum = false ;
}

void Java_com_jqh_jmedia_JMediaPlayer_seekTo(JNIEnv *env, jobject jobject,jlong seek)
{
    if(playStatus)
        playStatus->seekpos = seek ;
}

void Java_com_jqh_jmedia_JMediaPlayer__1release(JNIEnv *env, jobject jobject)
{
    LOGD("media opetator -- release");
    playStatus->play_status = PLAY_STATUS_STOP;
    if(playStatus)
        playStatus->isPlay = false;
    // free 释放内存
}

void Java_com_jqh_jmedia_JMediaPlayer__1setPlaybackSpeed(JNIEnv *env, jobject jobject,jfloat jspeed)
{
    if(playStatus)
    {
        playStatus->speed = jspeed ;

        if(playStatus->play_status == PLAY_STATUS_START || playStatus->play_status == PLAY_STATUS_PAUSE){
            int audioSampleRate = (int)(playStatus->sample_rate * playStatus->speed) ;
            //LOGD("InitAudio --- %d %d %lld channels=%d ",acodeCtx->sample_rate, audioSampleRate,acodeCtx->channel_layout,acodeCtx->channels);
            // 参数为采样率和声道数
            playStatus->tempoStream_ = sonicCreateStream(playStatus->sample_rate,playStatus->channels);
            // 设置速率
            sonicSetSpeed(playStatus->tempoStream_, playStatus->speed);
            sonicSetPitch(playStatus->tempoStream_, 1.0);
            sonicSetRate(playStatus->tempoStream_, 1.0/playStatus->speed);
            playStatus->sample_rate_speed = (int)(playStatus->sample_rate * playStatus->speed) ;
        }
    }
}

jint Java_com_jqh_jmedia_JMediaPlayer_getVideoWidth(JNIEnv *env, jobject jobject)
{
    return playStatus->videoWdith;
}

jint Java_com_jqh_jmedia_JMediaPlayer_getVideoHeight(JNIEnv *env, jobject jobject)
{
    return playStatus->videoHeight;
}

jlong Java_com_jqh_jmedia_JMediaPlayer_getDuration(JNIEnv *env, jobject jobject)
{
    return playStatus->duration;
}

jint Java_com_jqh_jmedia_JMediaPlayer_getSampleRate(JNIEnv *env, jobject jobject)
{
    return playStatus->sample_rate_speed;
}

jint Java_com_jqh_jmedia_JMediaPlayer_getDesiredFrames(JNIEnv *env, jobject jobject)
{
    return playStatus->desiredFrames;
}

jboolean Java_com_jqh_jmedia_JMediaPlayer_is16Bit(JNIEnv *env, jobject jobject)
{
    return playStatus->is16Bit;
}

jboolean Java_com_jqh_jmedia_JMediaPlayer_isStereo(JNIEnv *env, jobject jobject)
{
    return playStatus->isStereo;
}

jboolean Java_com_jqh_jmedia_JMediaPlayer_isPlaying(JNIEnv *env, jobject jobject)
{
    return playStatus->play_status == PLAY_STATUS_START || playStatus->play_status == PLAY_STATUS_PREPARE;
}


jboolean Java_com_jqh_jmedia_JMediaPlayer_isPause(JNIEnv *env, jobject jobject)
{
    return playStatus->play_status == PLAY_STATUS_PAUSE;
}

/**
 * 初始化ffmpeg参数
 */
void init_ffmpeg()
{
   // av_log_set_callback(myffmpeglog2);
    av_log_set_level(AV_LOG_INFO);
    av_register_all();
    avformat_network_init();
}

void init_alloc()
{
    packetQueue = malloc(sizeof(PacketQueue));
    playStatus = malloc(sizeof(PlayStatus));
    avFFmpegCtx = malloc(sizeof(AVFFmpegCtx));
}
/**
 *  初始化音视频相关参数
 */
void init_params()
{

    packet_queue_init(&playStatus->videoq);
    packet_queue_init(&playStatus->audioq);

    playStatus->isPlay = false;
    playStatus->isReadStop = false;
    playStatus->isARunning = false;
    playStatus->isVRunning = false ;
    playStatus->isResum = false;
    playStatus->seekpos = -1 ;
    playStatus->speed = 1.0;
    playStatus->audio_clock = 0 ;
    playStatus->frame_last_delay = 0.0 ;
    playStatus->frame_last_pts = 0.0 ;
    playStatus->frame_timer = 0.0;

    playStatus->video_index = -1;
    playStatus->audio_index = -1;
    playStatus->videoWdith = 0 ;
    playStatus->videoHeight = 0 ;
    playStatus->duration = 0 ;

    playStatus->sample_rate = 0 ;
    playStatus->sample_rate_speed = 0 ;
    playStatus->isStereo = 0 ;
    playStatus->is16Bit = 0;
    playStatus->desiredFrames = 0;
    playStatus->channels = 1 ;
    playStatus->play_status = PLAY_STATUS_STOP;
    playStatus->last_show_time = 0.0 ;
    avFFmpegCtx->pFormatCtx = NULL;
}

/**
 * 自定义日志输出回调接口,可以获取到ffmpeg系统的一些日志
 */
void myffmpeglog2(void* p, int v, const char* str, va_list lis){

    va_list vl2;
    char line[1024];
    static int print_prefix = 1;


    va_copy(vl2, lis);
    av_log_format_line(p, v, str, vl2, line, sizeof(line), &print_prefix);
    va_end(vl2);
    LOGE("%s",line);
}


// 初始化队列
void packet_queue_init(PacketQueue *q)
{
    memset(q,0,sizeof(PacketQueue));
    q->first_ptk = NULL;
    q->last_pkt = NULL;
    packet_queue_clean(q);
    pthread_mutex_init(&q->mutex , NULL) ; // 初始化锁
}

// 加入队列
int packet_queue_put(PacketQueue *q, AVPacket *pkt)
{

    AVPacketList *pkt1 = (AVPacketList *) av_malloc(sizeof(AVPacketList));
    if(!pkt1)
    {
        return -1 ;
    }
    if(q == NULL)
        return -1 ;
    //对互斥锁上锁
    if(pthread_mutex_lock(&q->mutex) != 0)
    {
        printf("packet_queue_put Thread lock failed! \n") ;
        return -1 ;
    }
    pkt1->pkt = *pkt ;
    pkt1->next = NULL;

    if(q == NULL)
        return -1 ;
    if(!q->last_pkt)
    {
        q->first_ptk = pkt1 ;
    }
    else
    {
        q->last_pkt->next = pkt1 ;
    }

    q->last_pkt = pkt1 ;
    q->nb_packets++;
    q->size += pkt1->pkt.size ;
    //解锁
    pthread_mutex_unlock(&q->mutex) ;
    return 0;
}

// 获取队列数据，block 是否堵塞
int packet_queue_get(PacketQueue *q,AVPacket *pkt, int block)
{
    AVPacketList *pkt1 = NULL;
    int ret = 0 ;

    if(q == NULL)
        return -1 ;
    //对互斥锁上锁
    if(pthread_mutex_lock(&q->mutex) != 0)
    {
        printf("packet_queue_get Thread lock failed! \n") ;
        return -1 ;
    }

    if(q == NULL)
        return -1 ;
    for(;;)
    {
        pkt1 = q->first_ptk ;
        if(pkt1)
        {
            q->first_ptk = pkt1->next;
            if(!q->first_ptk)
            {
                q->last_pkt = NULL;
            }
            q->nb_packets-- ;
            q->size -= pkt1->pkt.size ;
            *pkt = pkt1->pkt;
            av_free(pkt1);
            ret = 1 ;
            LOGD("[packet_queue_get] video packgets = %d",q->nb_packets);
            break;
        }
        else if(!block)
        {
            ret = 0 ;
            LOGD("[packet_queue_get] !block");
            break;
        }
        else
        {
            ret = -1 ;
            break;
        }
    }

     //解锁
    pthread_mutex_unlock(&q->mutex);
    return ret ;
}

void packet_queue_destory(PacketQueue *q)
{
    if(q != NULL)
        pthread_mutex_destroy(&q->mutex) ;
}

void packet_queue_clean(PacketQueue *q)
{
    if(q == NULL)
        return ;
    //对互斥锁上锁
    if(pthread_mutex_lock(&q->mutex) != 0)
    {
        printf("packet_queue_clean Thread lock failed! \n") ;
        return  ;
    }

    //清空数据
    q->first_ptk = NULL;
    q->last_pkt = NULL;
    q->size = 0 ;
    q->nb_packets = 0 ;
    //解锁
    pthread_mutex_unlock(&q->mutex) ;

}

// 准备解码
void *prepareAsync(void *arg)
{
    // 判断当前是否在播放
    while(playStatus->isARunning || playStatus->isVRunning)
    {
        playStatus->isPlay = false ;
        playStatus->play_status = PLAY_STATUS_STOP;
        usleep(0.5*1000*1000);
    }
    init_params();
    playStatus->play_status = PLAY_STATUS_PREPARE ;
    AVCodecContext *codecCtx = NULL;
    AVCodec *codec = NULL;

    AVCodecContext *acodeCtx = NULL;
    AVCodec *acodec = NULL ;

    avFFmpegCtx->pFormatCtx = avformat_alloc_context();
    // 设置参数
    AVDictionary *pAvDic = NULL;
    // 设置http请求头部信息
    av_dict_set(&pAvDic,"user_agent", "android", 0);

    LOGD("media opetator -- avformat_open_input");
    if(avformat_open_input(&avFFmpegCtx->pFormatCtx,playStatus->url,NULL,&pAvDic))
    {
         LOGE("open error");
         postEventFromNative(MEDIA_ERROR,MEDIA_ERROR_OPENURL,0,NULL);
         return NULL;
    }

    LOGD("avformat_open_input ok");
    // 查找流
    if(avformat_find_stream_info(avFFmpegCtx->pFormatCtx,NULL)<0)
    {
        LOGE("find error");
        postEventFromNative(MEDIA_ERROR,MEDIA_ERROR_FINDSTREAM,0,NULL);
        return NULL;
    }

    LOGD("find stream ok");
    // 获取流的个数
    int stream_num = avFFmpegCtx->pFormatCtx->nb_streams;
    LOGD("stream num=%d",stream_num);
    // 标识出每个流信息
    int i = 0 ;
    for(i = 0 ; i < stream_num; i++ )
    {
        if(avFFmpegCtx->pFormatCtx->streams[i]->codec->codec_type == AVMEDIA_TYPE_VIDEO)
        {
            playStatus->video_index = i ;
        }
        else if(avFFmpegCtx->pFormatCtx->streams[i]->codec->codec_type == AVMEDIA_TYPE_AUDIO)
        {
            playStatus->audio_index = i ;
        }
    }

    LOGD("video stream index=%d",playStatus->video_index);
    LOGD("audio stream index=%d",playStatus->audio_index);

    if(hasVideo())
        decode_video_stream(); // 解码视频流
    if(hasAudio())
        decode_audio_stream(); // 解码音频流

    playStatus->duration = avFFmpegCtx->pFormatCtx->duration ;
    LOGD("duration -- %lld",playStatus->duration);
    //(*env)->CallVoidMethod(env,jPlayMediaJniObj, jni_perpare);
    postEventFromNative(MEDIA_PREPARED,0,0,NULL);
    return NULL;
}


void decode_video_stream()
{
    AVCodecContext *codecCtx = NULL;
    AVCodec *codec = NULL;
    codecCtx = avFFmpegCtx->pFormatCtx->streams[playStatus->video_index]->codec;
    codec = avcodec_find_decoder(codecCtx->codec_id);
    if(!codec)
    {
        LOGE("can not decode video,Unsupport codec id = %d",codecCtx->codec_id);
        postEventFromNative(MEDIA_ERROR,MEDIA_ERROR_FIND_VIDEO_DECODE,0,NULL);
        return ;
    }

    LOGD("the video codec id = %d",codecCtx->codec_id);
    if(avcodec_open2(codecCtx,codec,NULL) < 0)
    {
        LOGE("video avcodec_open2 error");
        postEventFromNative(MEDIA_ERROR,MEDIA_ERROR_OPEN_VIDEO_DECODE,0,NULL);
        return ;
    }

    playStatus->videoWdith = codecCtx->width ;
    playStatus->videoHeight = codecCtx->height ;

}

void decode_audio_stream()
{
    AVCodecContext *acodeCtx = NULL;
    AVCodec *acodec = NULL ;

    // 查找音频解码器
    acodeCtx = avFFmpegCtx->pFormatCtx->streams[playStatus->audio_index]->codec;
    acodec = avcodec_find_decoder(acodeCtx->codec_id);
    if(!acodec)
    {
        LOGE("can not decode audio,Unsupport codec id = %d",acodeCtx->codec_id);
        postEventFromNative(MEDIA_ERROR,MEDIA_ERROR_FIND_AUDIO_DECODE,0,NULL);
        return ;
    }
    // 解码音频
    if(avcodec_open2(acodeCtx,acodec,NULL)<0)
    {
        LOGE("audio avcodec_open2 error");
        postEventFromNative(MEDIA_ERROR,MEDIA_ERROR_OPEN_AUDIO_DECODE,0,NULL);
        return ;
    }

    playStatus->sample_rate_speed = (int)(acodeCtx->sample_rate * playStatus->speed) ;
    LOGD("InitAudio --- %d %d %f %lld channels=%d ",acodeCtx->sample_rate, playStatus->sample_rate_speed,playStatus->speed,acodeCtx->channel_layout,acodeCtx->channels);
    // 参数为采样率和声道数
    playStatus->tempoStream_ = sonicCreateStream(acodeCtx->sample_rate,acodeCtx->channels);
    // 设置速率
    sonicSetSpeed(playStatus->tempoStream_, playStatus->speed);
    sonicSetPitch(playStatus->tempoStream_, 1.0);
    sonicSetRate(playStatus->tempoStream_, 1.0/playStatus->speed);

    playStatus->sample_rate = acodeCtx->sample_rate;
    playStatus->is16Bit = 1;
    playStatus->isStereo = acodeCtx->channels > 1;
    playStatus->desiredFrames = 4096;
    playStatus->channels = acodeCtx->channels;

}

// 音视频接受数据线程
void *recv_thread(void *arg)
{
    playStatus->isPlay = true ;
    playStatus->isResum = true ;
    LOGD("recv_thread...");
    AVPacket pkt1, *packet = &pkt1;

    AVFrame* pFrame = NULL;
    pFrame = av_frame_alloc();
    int i = 0 ;

    AVFrame *pAFrame = av_frame_alloc();
    AVFrame *pAFrameCache = av_frame_alloc();
    SwrContext *pSwrCtx = NULL;

    if(playStatus->seekpos > 0)
        av_seek_frame(avFFmpegCtx->pFormatCtx, -1 , playStatus->seekpos * AV_TIME_BASE, AVSEEK_FLAG_ANY);

    packet_queue_init(&playStatus->videoq);
    packet_queue_init(&playStatus->audioq);

    playStatus->frame_timer = nowTime();
    playStatus->frame_last_delay = 40e-3;
    playStatus->frame_last_pts = 0 ;
    //------------------------------------------
    while(1){

        if(!playStatus->isPlay)
        {
            LOGD("recv_thread stop");
            playStatus->isReadStop = true ;
            break;
        }

        if(!playStatus->isResum)
        {
            LOGD("recv_thread usleep");
            usleep(RESUME_WAITINH_TIME);
            continue;
        }
        if(playStatus->seekpos > 0)
        {
            // 用户拖动进度条
            //AVSEEK_FLAG_BACKWARD：若你设置seek时间为1秒，但是只有0秒和2秒上才有I帧，则时间从0秒开始。
            //AVSEEK_FLAG_ANY：若你设置seek时间为1秒，但是只有0秒和2秒上才有I帧，则时间从2秒开始。
            //AVSEEK_FLAG_FRAME：若你设置seek时间为1秒，但是只有0秒和2秒上才有I帧，则时间从2秒开始。
            LOGD("av_seek_frame to -- %lld" , playStatus->seekpos);
            av_seek_frame(avFFmpegCtx->pFormatCtx, -1 , playStatus->seekpos * AV_TIME_BASE, AVSEEK_FLAG_BACKWARD);
            playStatus->seekpos = -1 ;
            packet_queue_clean(&playStatus->audioq);
            packet_queue_clean(&playStatus->videoq);
            // 音频相关
            playStatus->audio_clock = 0.0;

            // 视频相关
            playStatus->frame_last_pts = 0.0;
            playStatus->frame_last_delay = 0.0;
            playStatus->frame_timer = 0.0;
        }
        LOGD("recv_thread av_read_frame");
        int result = av_read_frame(avFFmpegCtx->pFormatCtx,packet);
        LOGD("recv_thread av_read_frame ok");
        if(result != 0)
        {
            if(result == -EAGAIN)
            {
                LOGD("recv_thread continue");
                continue;
            }
            else
            {
                LOGD("recv_thread --- break");
                usleep(RESUME_WAITINH_TIME);
                continue;
            }
        }

        if(packet->stream_index == playStatus->video_index)
        {
            LOGD("packet_queue set---video");
            packet_queue_put(&playStatus->videoq,packet);
        }
        else if(packet->stream_index == playStatus->audio_index)
        {
            LOGD("packet_queue set---audio");
            packet_queue_put(&playStatus->audioq,packet);
        }
        else
        {
            av_free_packet(packet);
        }
    }
    LOGD("recv_thread exit");
    playStatus->play_status = PLAY_STATUS_STOP;
    return NULL;
}

// 视频接受解码线程
void *video_thread(void *arg)
{
    AVPacket pkt1 , *packet = &pkt1 ;
    int frameFinished = 0;
    AVFrame *pFrame = NULL;
    AVStream *astream = avFFmpegCtx->pFormatCtx->streams[playStatus->video_index] ;
    double sync_threshold ;
    double diff = 0 ;
    double pts ;
    double delay ;
    double actual_delay ;

    pFrame = av_frame_alloc();
    AVCodecContext *codecCtx = avFFmpegCtx->pFormatCtx->streams[playStatus->video_index]->codec;
    JNIEnv *env = NULL;

    AVFrame* pFrameRGB = NULL;
    pFrameRGB = av_frame_alloc();

    // 使用的缓冲区的大小
    int numBytes = 0;
    uint8_t* buffer = NULL;

    numBytes = avpicture_get_size(AV_PIX_FMT_RGB565, codecCtx->width, codecCtx->height);
    buffer = (uint8_t*)av_malloc(numBytes * sizeof(uint8_t));
    avpicture_fill((AVPicture*)pFrameRGB, buffer, AV_PIX_FMT_RGB565, codecCtx->width, codecCtx->height);

    struct SwsContext *sws_ctx = NULL;
    sws_ctx = sws_getContext(codecCtx->width,codecCtx->height,codecCtx->pix_fmt,
        codecCtx->width,codecCtx->height,AV_PIX_FMT_RGB565, SWS_BILINEAR,NULL,NULL,NULL);
    if((*g_jvm)->AttachCurrentThread(g_jvm, &env, NULL) != JNI_OK)
    {
        LOGD("AttachCurrentThread--() failed");
    }
    while(1)
    {

        if(playStatus->isReadStop)
        {
            LOGD("video stop play");
            break;
        }
        if(!playStatus->isResum)
        {
            //LOGD("pause ing ...");
            usleep(RESUME_WAITINH_TIME);
            continue;
        }
        playStatus->isVRunning = true ;
        //LOGD("video_thread --- packet_queue_get 111");
        if(packet_queue_get(&playStatus->videoq,packet,1) < 0)
        {
            LOGD("video_thread --- no packet");
            //usleep(200000);// 50ms   1ms = 1000us
            usleep(RESUME_WAITINH_TIME);
            continue ;
        }
        double time1 = nowTime();
        LOGD("[video_thread] avcodec_decode_video2");
        avcodec_decode_video2(codecCtx,pFrame,&frameFinished,packet);
        double time2 = nowTime();
        //--------------以下是音视频同步相关
        pts = 0 ;
        if (packet->dts == AV_NOPTS_VALUE && pFrame->opaque
            && *(uint64_t*) pFrame->opaque != AV_NOPTS_VALUE)
        {
            pts = *(uint64_t *) pFrame->opaque;
        }
        else if (packet->dts != AV_NOPTS_VALUE)
        {
            pts = packet->dts;
        }
        else
        {
            pts = 0;
        }
        LOGD("show time video_clock pts:%lf",pts);
        pts *= av_q2d(astream->time_base);
        LOGD("show time video_clock pts--:%lf",pts);
        double ref_clock = get_audio_clock() ;
        if(pts >= 0.001 && ref_clock >= 0.01)
        {
            delay = pts -playStatus->frame_last_pts;
            // 保存
            playStatus->frame_last_delay = delay;
            playStatus->frame_last_pts = pts;
            pts = pts - playStatus->last_show_time;
            diff = pts - ref_clock;

            LOGE("show time -- pts audioclock %lf %lf", pts,ref_clock);

            sync_threshold = (delay > AV_SYNC_THRESHOLD) ? delay : AV_SYNC_THRESHOLD;
            if (fabs(diff) < AV_NOSYNC_THRESHOLD)
            {
                if (diff <= -sync_threshold)
                {
                    delay = 0;
                }
                else if (diff >= sync_threshold)
                {
                    delay = 2 * delay;
                }
            }

            double dNow = nowTime();
            if ((playStatus->frame_timer - dNow > 1000) || (dNow - playStatus->frame_timer > 0.5))
            {
                //LOGE("show time ft=%lf dNow=%lf", frame_timer,dNow);
                playStatus->frame_timer = dNow;
            }
            playStatus->frame_timer += delay;
            //actual_delay = frame_timer - dNow;
            actual_delay = diff;
            if (actual_delay <= 0.010)
                actual_delay = 0.010;
            if(diff >= 0.01) // 等待
            {
                LOGE("show time -- sleep frame =%lf", diff);
                usleep(diff*1000*1000);
            }
            else if(diff < - 0.01)
            {
                LOGE("show time -- drop frame =%lf", diff);
                continue ; // 丢帧````````
            }
            LOGE("show time -- diff=%lf -- %lf", diff,diff*1000*1000);
            //--------------end--------------
        }

        if(frameFinished)
        {
             double time3 = nowTime();
            sws_scale(sws_ctx, (uint8_t const * const *)pFrame->data, pFrame->linesize, 0,
            codecCtx->height, pFrameRGB->data, pFrameRGB->linesize);
            // 计算显示时间
            call_back_videodata(buffer, codecCtx->width, codecCtx->height,env);
            double time4 = nowTime();
            playStatus->last_show_time = (time4 - time3) + (time2 - time1) ;
            if(playStatus->last_show_time > 2)
                playStatus->last_show_time = 2;
            playStatus->last_show_time += 0.2;
            LOGD("time1 = %f time2 = %f diff = %f",time1,time2,playStatus->last_show_time);
        }

    }

    av_frame_free(&pFrameRGB);
    av_frame_free(&pFrame);

    packet_queue_destory(&playStatus->videoq);
    LOGD("video thread exit");
    (*g_jvm)->DetachCurrentThread(g_jvm);
    playStatus->isVRunning = false ;
    return NULL;
}

// 音频接受解码线程
void *audio_thread(void *arg)
{

    AVPacket pkt1 , *packet = &pkt1 ;
    AVCodecContext *acodeCtx = avFFmpegCtx->pFormatCtx->streams[playStatus->audio_index]->codec;
    AVStream *astream = avFFmpegCtx->pFormatCtx->streams[playStatus->audio_index] ;
    JNIEnv *env = NULL;
    AVFrame *pAFrame = av_frame_alloc();
    AVFrame *pAFrameCache = av_frame_alloc();
    SwrContext *pSwrCtx = NULL;
    int resampled_data_size ;

    if((*g_jvm)->AttachCurrentThread(g_jvm, &env, NULL) != JNI_OK)
    {
        LOGD("AttachCurrentThread--() failed");
    }

    while(1)
    {

        if(playStatus->isReadStop)
        {
            LOGD("audio stop play");
            break;
        }
        if(!playStatus->isResum)
        {
            //LOGD("pause ing ...");
            usleep(RESUME_WAITINH_TIME);
            continue;
        }
        playStatus->isARunning = true ;
        //LOGD("[audio_thread] packet_queue_get");
        if(packet_queue_get(&playStatus->audioq,packet,1) < 0)
        {
            LOGD("[audio_thread] no packet");
            //usleep(200000);// 50ms   1ms = 1000us
            usleep(RESUME_WAITINH_TIME);
            continue;
        }
        if(packet->pts != AV_NOPTS_VALUE)
        {
            playStatus->audio_clock = av_q2d(astream->time_base)*packet->pts;
        }
        int framefinished = 0 ; // 判断是否读取完
        //LOGD("recv_thread audio packet");
        avcodec_decode_audio4(acodeCtx,pAFrameCache,&framefinished,packet);
        if(framefinished)
        {
            // if is 16bit
            if(acodeCtx->sample_fmt == AV_SAMPLE_FMT_S16)
            {
                int nb_sample = pAFrameCache->nb_samples;
                int out_channels = acodeCtx->channels;
                int bytes_per_sample = av_get_bytes_per_sample(acodeCtx->sample_fmt);
                int dst_buf_size = nb_sample * bytes_per_sample * out_channels;
                int ret = sonicWriteShortToStream(playStatus->tempoStream_, (short *)pAFrameCache->data[0], dst_buf_size/(2*out_channels));
                // 计算处理后的点数
                LOGD("frame data[0] 11 %d",*(pAFrameCache->data[0]+1));
                int numSamples = dst_buf_size / (2*out_channels);
                if(ret) {
                // 从流中读取处理好的数据
                   int new_buffer_size = sonicReadShortFromStream(playStatus->tempoStream_, (short *)pAFrameCache->data[0], numSamples);
                }
                LOGD("frame data[0] 22 %d",*(pAFrameCache->data[0]+1));
                //LOGD("is 16 bit");
                // render to java
                LOGD("S16 --- fmt=%d size=%d ret1=%d",acodeCtx->sample_fmt,dst_buf_size,ret);

                call_back_aduiodata(pAFrameCache,env);
            }
            else
            {

                LOGD("NO S16 --- %d size=%d",acodeCtx->sample_fmt,pAFrameCache->linesize[0]);
                int nb_sample = pAFrameCache->nb_samples;
                int out_channels = acodeCtx->channels;
                int bytes_per_sample = av_get_bytes_per_sample(AV_SAMPLE_FMT_S16);
                int dst_buf_size = nb_sample * bytes_per_sample * out_channels;

                pAFrame->linesize[0] = pAFrameCache->linesize[0];
                pAFrame->data[0] = (uint8_t*) av_malloc(dst_buf_size);

                avcodec_fill_audio_frame(pAFrame, out_channels, AV_SAMPLE_FMT_S16, pAFrame->data[0], dst_buf_size, 0);

                if (!pSwrCtx)
                {
                    uint64_t in_channel_layout = av_get_default_channel_layout(acodeCtx->channels);
                    uint64_t out_channel_layout = av_get_default_channel_layout(out_channels);
                    pSwrCtx = swr_alloc_set_opts(NULL, out_channel_layout, AV_SAMPLE_FMT_S16, acodeCtx->sample_rate,
                    in_channel_layout, acodeCtx->sample_fmt, acodeCtx->sample_rate, 0, NULL);
                    swr_init(pSwrCtx);
                }

                if(pSwrCtx)
                {
                    int out_count = dst_buf_size / out_channels / av_get_bytes_per_sample(AV_SAMPLE_FMT_S16);
                    int ret = swr_convert(pSwrCtx, pAFrame->data, out_count, (const uint8_t**)(pAFrameCache->data), nb_sample);
                    if (ret < 0)
                    {
                        //av_free(out_channels->data[0]);
                    }
                    else
                    {
                        resampled_data_size = ret * out_channels * bytes_per_sample;
                        // 更新基准时间
                        playStatus->audio_clock += (double)resampled_data_size/(double)(2 * out_channels * acodeCtx->sample_rate);
                        //LOGD("show time audio_clock:%lf",audio_clock);
                        pAFrameCache->linesize[0] = pAFrame->linesize[0] = ret * av_get_bytes_per_sample(AV_SAMPLE_FMT_S16) * out_channels;
                    }
                    //LOGD("NO S16 +++ fmt=%d size=%d size=%d channels=%d",acodeCtx->sample_fmt,dst_buf_size,pAFrame->linesize[0],out_channels);
                    int ret1 = sonicWriteShortToStream(playStatus->tempoStream_, (short *)pAFrame->data[0], dst_buf_size/(2*out_channels));
                    // 计算处理后的点数
                    int numSamples = dst_buf_size / (2*out_channels);
                    if(ret1) {
                    // 从流中读取处理好的数据
                       int new_buffer_size = sonicReadShortFromStream(playStatus->tempoStream_, (short *)pAFrame->data[0], numSamples);
                    }
                    call_back_aduiodata(pAFrame,env);
                }
            }
        }
    }
    av_frame_free(&pAFrame);
    av_frame_free(&pAFrameCache);
    avformat_free_context(avFFmpegCtx->pFormatCtx);
    packet_queue_destory(&playStatus->audioq);
    LOGD("audio thread exit");
    (*g_jvm)->DetachCurrentThread(g_jvm);
    playStatus->isARunning = false ;
    return NULL;
}


// 回调返回视频数据
void call_back_videodata(const uint8_t *pData, int width, int height,JNIEnv *env )
{
    int len = 2*width*height;
    if(!env)
    {
        if((*g_jvm)->AttachCurrentThread(g_jvm, &env, NULL) != JNI_OK)
        {
            LOGE("AttachCurrentThread() failed");
        }
    }

    jbyteArray carr = (*env)->NewByteArray(env, len);
    (*env)->SetByteArrayRegion(env,carr,0,len,(const jbyte *)pData);
    (*env)->CallVoidMethod(env, jPlayMediaJniObj, jni_flush_video_data, carr);
    (*env)->DeleteLocalRef(env, carr);

}

// 回调返回音频数据
void call_back_aduiodata(AVFrame *frame,JNIEnv *env )
{
    if(!env)
    {
        if((*g_jvm)->AttachCurrentThread(g_jvm, &env, NULL) != JNI_OK)
        {
            LOGE("AttachCurrentThread() failed");
        }
    }
    jbyteArray carr = (*env)->NewByteArray(env, frame->linesize[0]);
    (*env)->SetByteArrayRegion(env,carr,0,frame->linesize[0],(const jbyte *)frame->data[0]);
    (*env)->CallVoidMethod(env,jPlayMediaJniObj,jni_flush_audio_byte_data,carr);
    (*env)->DeleteLocalRef(env, carr);
}

void postEventFromNative(jint what,jint arg1,jint arg2,jobject obj)
{
    JNIEnv *env = NULL;
    if((*g_jvm)->AttachCurrentThread(g_jvm, &env, NULL) != JNI_OK)
    {
        LOGE("AttachCurrentThread() failed");
        return ;
    }
    (*env)->CallVoidMethod(env,jPlayMediaJniObj,jni_postEventFromNative,what,arg1,arg2,obj);

    (*g_jvm)->DetachCurrentThread(g_jvm);
}

int hasVideo()
{
    if(playStatus->video_index >= 0)
    {
        return 1 ;
    }
    return 0 ;
}

int hasAudio()
{
    if(playStatus->audio_index >= 0)
    {
        return 1 ;
    }
    return 0 ;
}

//获取时间基准
double get_audio_clock()
{
    return playStatus->audio_clock ;
}

// 返回当前时间，double类型
double nowTime()
{
/**
    unsigned long long ullNow = av_gettime();
    double dNow = (double)ullNow / 1000000;
    return  dNow ;
    **/
    struct timeval tv;
    gettimeofday(&tv,NULL);

    unsigned long long ullNow = tv.tv_sec*1000000 + tv.tv_usec;//av_gettime();
    double dNow = (double)ullNow / 1000000;
    return  dNow ;
}
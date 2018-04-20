package com.jqh.jmedia;

/**
 * 和底层交互
 * Created by user on 2018/2/5.
 */
public class JMediaJni {


//    static {
//        System.loadLibrary("ffmpegjni");
//    }

    private static JMediaJni instance ;

    private JMediaJni(){

    }

    public synchronized static JMediaJni getInstance(){
        if(instance == null){
            synchronized (JMediaJni.class){
                if(instance == null)
                    instance = new JMediaJni();
            }
        }

        return instance ;
    }

    public interface OnJMediaPlayEventListener {
        void postEventFromNative(int what, int arg1, int arg2, Object obj);
        void jni_flush_video_data(byte[] data);
        void jni_flush_audio_byte_data(byte[] data);
    }

    private OnJMediaPlayEventListener listener ;

    public void setOnJMediaEventListener(OnJMediaPlayEventListener listener){
        this.listener = listener ;
    }

    public void clean(){
        listener = null ;
        instance = null ;
    }

    //  jni接口处理
    /**
     * 初始化底层Native
     */
    public native void _initNative();

    /**
     *
     * @param path play url
     * @param keys   http header key
     * @param values http header value
     */
    public native void _setDataSource(String path,String[] keys,String[] values);

    /**
     *  准备播放，同步操作
     */
    public native void prepare();

    /**
     * 准备播放，异步操作
     */
    public native void prepareAsync();

    /**
     * 开始播放
     */
    public native void _start();

    /**
     * 停止播放
     */
    public native void _stop();

    /**
     * 暂停播放
     */
    public native void _pause();

    /**
     * 是否正在播放
     * @return
     */
    public native boolean isPlaying();

    /**
     * 是否正在播放
     * @return
     */
    public native boolean isPause();


    /**
     * 毫秒单位，指定到某一时间播放
     * @param msec
     */
    public native void seekTo(long msec);

    /**
     * 倍速
     * @param speed
     */
    public native void _setPlaybackSpeed(float speed);

    /**
     * 从jni层获取当前视屏播放的一些参数
     * @return
     */
    public native long getDuration();

    /**
     * 获取视频宽度
     * @return
     */
    public native int getVideoWidth();

    /**
     * 获取视频高度
     * @return
     */
    public native int getVideoHeight();

    /**
     * 获取视频采样率
     * @return
     */
    public native int getSampleRate();

    /**
     * 音频是否是16位
     * @return
     */
    public native boolean is16Bit();

    /**
     * 声道
     * @return
     */
    public native boolean isStereo();

    /**
     * 缓存大小
     * @return
     */
    public native int getDesiredFrames();


    public native void _release();


    // jni回调接口
    public void postEventFromNative(int what,int arg1,int arg2,Object obj){
        // 回调
        if(listener != null)
        {
            listener.postEventFromNative(what,arg1,arg2,obj);
        }
    }

    public void jni_flush_video_data(byte[] data){
        // 回调
        if(listener != null)
        {
            listener.jni_flush_video_data(data);
        }
    }

    public void jni_flush_audio_byte_data(byte[] data){
        // 回调
        if(listener != null)
        {
            listener.jni_flush_audio_byte_data(data);
        }
    }


    //以下是录制相关接口回调

    /**
     * 初始化录制
     * @param url
     * @param w
     * @param h
     * @return
     */
    public static native int publicStreamInit(String url,int w,int h);

    /**
     * 推送数据
     * @param data
     * @param isVideo
     * @return
     */
    public static native  int flushStreamData(byte[] data,int isVideo);

    /**
     * 停止推流
     * @return
     */
    public static native  int stopStream();


    /**
     * 停止推流
     * @return
     */
    public static native  int h246ToMp4();


}

package com.jqh.duanvideo.view;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;

import com.jqh.duanvideo.R;
import com.jqh.jmedia.JMediaPlayer;

/**
 * Created by user on 2018/4/12.
 */
public class JVideoView extends RelativeLayout implements SurfaceHolder.Callback {


    private Context mContext ;
    private SurfaceView mSurfaceView ;
    private MediaPlayer mediaPlayer ;

    private long mCurSeek = 0;
    private float mCurSpeed = 1.0f ;
    public interface IVideoPlayerListener{
        void onSuccess();
        void onLoading();
        void onError(int code, String msg);
    }
    private IVideoPlayerListener mIVideoPlayerListener ;

    public void setIVideoPlayerListener(IVideoPlayerListener mIVideoPlayerListener) {
        this.mIVideoPlayerListener = mIVideoPlayerListener;
    }

    public JVideoView(Context context) {
        super(context);
        init(context);
    }

    public JVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public JVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context){
        this.mContext = context ;
        LayoutInflater.from(mContext).inflate(R.layout.view_jvideo,this);
        mediaPlayer = new MediaPlayer();
        mSurfaceView = (SurfaceView) findViewById(R.id.surface_view);
        mSurfaceView.getHolder().addCallback(this);
        initEvent();
    }

    private void initEvent(){
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();
                if(mIVideoPlayerListener != null)
                    mIVideoPlayerListener.onSuccess();
            }
        });

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                if(mIVideoPlayerListener != null)
                    mIVideoPlayerListener.onError(what,"error");
                return false;
            }
        });

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mediaPlayer.setDisplay(mSurfaceView.getHolder());
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    /**
     * 开始播放
     */
    public void start(String path) throws Exception{
        if(mediaPlayer == null)
            throw new Exception();
        mCurSpeed = 1.0f;
        start(path,0);
    }

    /**
     * 开始播放
     */
    public void start(String path,long seek) throws Exception{
        mCurSpeed = 1.0f;
        start(path,seek,1.0f);
    }

    /**
     * 开始播放
     */
    public void start(String path,long seek,float speed) throws Exception{

        mediaPlayer.setDisplay(mSurfaceView.getHolder());
        initEvent();
        mCurSeek = seek ;
      //  mediaPlayer.setPlaybackSpeed(speed);
        mCurSpeed = speed ;
        mediaPlayer.setDataSource(path);
        mediaPlayer.prepareAsync();
        if(mIVideoPlayerListener != null)
            mIVideoPlayerListener.onLoading();
    }

    /**
     * 停止播放
     */
    public void stop(){
        mediaPlayer.stop();
    }

    /**
     * 暂停播放
     */
    public void pause(){
        mediaPlayer.pause();
    }


    public void start(){
        mediaPlayer.start();
    }

    /**
     * 是否正在播放
     */
    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }

    public boolean isPause(){
        return !mediaPlayer.isPlaying();
    }

    /**
     * has video
     * @return
     */
    public boolean hasVideo(){
        if(mediaPlayer.getVideoWidth() == 0 || mediaPlayer.getVideoHeight() == 0)
            return false;
        return true ;
    }

    public void setSpeed(float speed){
        if(mediaPlayer != null) {
            mCurSpeed = speed ;
        }
    }
}


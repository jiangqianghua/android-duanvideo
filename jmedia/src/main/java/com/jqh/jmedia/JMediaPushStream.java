package com.jqh.jmedia;

import android.view.SurfaceHolder;

/**
 * 推流
 * Created by user on 2018/2/5.
 */
public class JMediaPushStream {

    private SurfaceDraw mSurfaceDraw ;

    private SurfaceHolder holder  ;

    public JMediaPushStream(){
        if(mSurfaceDraw == null)
            mSurfaceDraw = new SurfaceDraw();
    }

    public void setDisplay(SurfaceHolder sh){
        holder = sh ;
        mSurfaceDraw.setSurfaceHolder(holder);
    }

    public void setVideoSize(int videoW , int videoH){
        mSurfaceDraw.width = videoW;
        mSurfaceDraw.height = videoH;
    }

    /**
     * 初始化录制
     * @param url
     * @param w
     * @param h
     * @return
     */
    public  void publicStreamInit(String url,int w,int h){
        JMediaJni.getInstance().publicStreamInit(url,w,h);
    }

    /**
     * 推送数据
     * @param data
     * @param isVideo
     * @return
     */
    private  void flushStreamData(byte[] data,int isVideo){
        JMediaJni.getInstance().flushStreamData(data,isVideo);
    }

    /**
     * 停止推流
     * @return
     */
    public  void stopStream(){
        JMediaJni.getInstance().stopStream();
    }


    public void toMp4(){
        JMediaJni.getInstance().h246ToMp4();
    }


    public  synchronized void flushStreamDataToJni(final byte[] data,final int isVideo)
    {
//        if(mSurfaceDraw != null && isVideo == 1)
//            mSurfaceDraw.flushVideoData(data);
        flushStreamData(data,isVideo);
    }

}

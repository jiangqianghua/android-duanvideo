package com.jqh.jmedia;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by user on 2018/1/19.
 */
public class SurfaceDraw {

    /** 视频实际宽 */
    public Integer width = new Integer(0);
    /** 视频实际高 */
    public Integer height = new Integer(0);

    private Rect gRect;
    /******************** 绘图数据 ********************/
    //private boolean isDraw = false;
    private Bitmap bmpOriginal = null; // 原始视频位图
    private Bitmap bmpScale = null;// 放大后的视频位图
    private Matrix matrix = null; // 定义位图放大系数
    private ByteBuffer byteBuffer = null;
    private float fStartX = 0;
    private float fStartY = 0;

    private SurfaceHolder mSurfaceHolder;
    public SurfaceDraw(SurfaceHolder holder){
        mSurfaceHolder = holder ;
        matrix = null ;
    }

    public SurfaceDraw(){

    }

    public void setSurfaceHolder(SurfaceHolder mSurfaceHolder) {
        this.mSurfaceHolder = mSurfaceHolder;
        matrix = null ;
    }

    public void flushVideoData(byte[] bytes) {
        if (bytes == null)
            return;
        DrawImage(bytes);

    }
    public boolean isDraw = false ;
    public synchronized void  DrawImage(byte[] bytes) {
        Canvas canvas = null ;
        isDraw = true ;
        try {
            canvas = mSurfaceHolder.lockCanvas();
            if (canvas == null)
                return;

            byteBuffer = ByteBuffer.wrap(bytes);

            bmpOriginal = Bitmap.createBitmap(width, height,
                    Bitmap.Config.RGB_565);
            bmpOriginal.copyPixelsFromBuffer(byteBuffer);
            if (matrix == null) {
                gRect = new Rect(0,0,canvas.getWidth(),canvas.getHeight());
                matrix = new Matrix();
                float scaleWidth = (float) canvas.getWidth()
                        / (float) bmpOriginal.getWidth();
                float scaleHeight = (float) canvas.getHeight()
                        / (float) bmpOriginal.getHeight();
                if (scaleWidth < scaleHeight) {
                    matrix.postScale(scaleWidth, scaleWidth);
                } else {
                    matrix.postScale(scaleHeight, scaleHeight);
                }
                if(width > height)
                    matrix.setRotate(-90);
                bmpScale = Bitmap.createBitmap(bmpOriginal, 0, 0,
                        bmpOriginal.getWidth(), bmpOriginal.getHeight(), matrix,
                        true);
                fStartX = (float)(canvas.getWidth() - bmpScale.getWidth()) / 2;
                fStartY = (float)(canvas.getHeight() - bmpScale.getHeight()) / 2;
            }
            bmpScale = Bitmap.createBitmap(bmpOriginal, 0, 0,
                    bmpOriginal.getWidth(), bmpOriginal.getHeight(), matrix,
                    true);
            // 美颜处理
            //bmpScale = beautifyMultiThread.beautifyImg(bmpScale,20) ;
            if(bmpScale != null)
            {
                synchronized(bmpScale)
                {
                    //LogUtil.i("canvas->drawBitmap");
                    //canvas.drawBitmap(bmpScale, fStartX, fStartY, null);
                    canvas.drawBitmap(bmpScale, null,gRect, null);
                }
            }

            if(bmpOriginal.isRecycled() == false)
            {
                bmpOriginal.recycle();
            }
            // 释放
            if(bmpScale.isRecycled() == false)
            {
                bmpScale.recycle();
            }
            bmpOriginal = null;
            bmpScale = null;
        } catch (Exception e) {
            Log.e("SurfaceDraw","DrawImage exception msg "+ e.getMessage());
        } finally
        {
            if(mSurfaceHolder!= null && canvas != null)
            {
                try
                {
                    mSurfaceHolder.unlockCanvasAndPost(canvas);//结束锁定画图，并提交改变。
                }catch(Exception e)
                {

                }
                isDraw = false;
            }
        }
    }



}

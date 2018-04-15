package com.jqh.duanvideo.view;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.TextureView;

import com.jqh.duanvideo.inter.CameraInterface;
import com.jqh.duanvideo.utils.LogUtils;

/**
 * Created by jiangqianghua on 18/4/15.
 */

public class CameraTextureView extends TextureView implements TextureView.SurfaceTextureListener {
    private static final String TAG = "yanzi";
    Context mContext;
    SurfaceTexture mSurface;
    public CameraTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        mContext = context;
        this.setSurfaceTextureListener(this);
    }
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width,
                                          int height) {
        // TODO Auto-generated method stub
        LogUtils.d("onSurfaceTextureAvailable...");
        mSurface = surface;
//      CameraInterface.getInstance().doStartPreview(surface, 1.33f);
    }
    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        // TODO Auto-generated method stub
        LogUtils.d("onSurfaceTextureDestroyed...");
        CameraInterface.getInstance().doStopCamera();
        return true;
    }
    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width,
                                            int height) {
        // TODO Auto-generated method stub
        LogUtils.d("onSurfaceTextureSizeChanged...");
    }
    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        // TODO Auto-generated method stub
        LogUtils.d("onSurfaceTextureUpdated...");

    }

    /* 让Activity能得到TextureView的SurfaceTexture
     * @see android.view.TextureView#getSurfaceTexture()
     */
    public SurfaceTexture _getSurfaceTexture(){
        return mSurface;
    }
}
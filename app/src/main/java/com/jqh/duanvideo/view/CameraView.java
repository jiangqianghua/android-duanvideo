package com.jqh.duanvideo.view;

import android.content.Context;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.jqh.duanvideo.R;
import com.jqh.duanvideo.base.BaseLayout;
import com.jqh.duanvideo.utils.LogUtils;
import com.jqh.jmedia.SurfaceDraw;

import java.util.List;

/**
 * Created by jiangqianghua on 18/4/15.
 */

public class CameraView extends BaseLayout {

    private SurfaceView mSurfaceView ;

    private Camera mCamera ;

    private SurfaceTexture gSurfaceTexture ;

    private SurfaceDraw mSurfaceDraw ;
    private static final int MAGIC_TEXTURE_ID = 10;
    public static final int ALLOW_PIC_LEN = 800;       //最大允许的照片尺寸的长度
    public CameraView(Context context) {
        super(context);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CameraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_camera;
    }

    @Override
    protected void initView() {
        mSurfaceDraw = new SurfaceDraw();
       // final CameraTextureView cameraTextureView = bindViewId(R.id.cameraTexture_view);

        gSurfaceTexture =new SurfaceTexture(MAGIC_TEXTURE_ID);
        mSurfaceView = bindViewId(R.id.surface_view);
        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mSurfaceDraw.setSurfaceHolder(mSurfaceView.getHolder());

                mCamera = Camera.open(1);
                Camera.Parameters parameters = mCamera.getParameters();//获取各项参数
                Point point = getBestCameraResolution(parameters,getScreenMetrics(mContext));
                parameters.setPreviewSize(point.x, point.y);// 设置预览大小
                mSurfaceDraw.width = point.x ;
                mSurfaceDraw.height = point.y ;
                mCamera.setParameters(parameters);
                mCamera.setPreviewCallback(new Camera.PreviewCallback() {
                    @Override
                    public void onPreviewFrame(byte[] data, Camera camera) {
                        //LogUtils.d(data.length+"");
                        mSurfaceDraw.flushVideoData(data);
                    }
                });
                try {
                    mCamera.setPreviewTexture(gSurfaceTexture);
                }catch (Exception e){
                    e.printStackTrace();
                }
                mCamera.startPreview();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }

    /**
     * 获取最佳预览大小
     * @param parameters 相机参数
     * @param screenResolution 屏幕宽高
     * @return
     */
    private Point getBestCameraResolution(Camera.Parameters parameters, Point screenResolution) {
        float tmp = 0f;
        float mindiff = 100f;
        float x_d_y = (float) screenResolution.x / (float) screenResolution.y;
        Camera.Size best = null;
        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        for (Camera.Size s : supportedPreviewSizes) {
            tmp = Math.abs(((float) s.height / (float) s.width) - x_d_y);
            if (tmp < mindiff) {
                mindiff = tmp;
                best = s;
            }
        }
        return new Point(best.width, best.height);
    }

    /**
     * 获取屏幕宽度和高度，单位为px
     * @param context
     * @return
     */
    public static Point getScreenMetrics(Context context){
        DisplayMetrics dm =context.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        return new Point(w_screen, h_screen);

    }

    /**
     * 返回合适的照片尺寸参数
     *
     * @param cameraParameters
     * @param bl
     * @return
     */
    private Camera.Size findFitPicResolution(Camera.Parameters cameraParameters, float bl) throws Exception {
        List<Camera.Size> supportedPicResolutions = cameraParameters.getSupportedPictureSizes();

        Camera.Size resultSize = null;
        for (Camera.Size size : supportedPicResolutions) {
            if ((float) size.width / size.height == bl && size.width <= ALLOW_PIC_LEN && size.height <= ALLOW_PIC_LEN) {
                if (resultSize == null) {
                    resultSize = size;
                } else if (size.width > resultSize.width) {
                    resultSize = size;
                }
            }
        }
        if (resultSize == null) {
            return supportedPicResolutions.get(0);
        }
        return resultSize;
    }

}

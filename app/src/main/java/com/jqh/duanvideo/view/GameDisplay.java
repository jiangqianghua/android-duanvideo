package com.jqh.duanvideo.view;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.jqh.duanvideo.utils.BeautifyMultiThread;

public class GameDisplay extends SurfaceView implements SurfaceHolder.Callback,
        Camera.PreviewCallback{
    public final static String TAG="GameDisplay";

    private static final int MAGIC_TEXTURE_ID = 10;
    public static final int DEFAULT_WIDTH=800;
    public static final int DEFAULT_HEIGHT=480;
    public static final int BLUR = 0;
    public static final int CLEAR = BLUR + 1;
    //public static final int PAUSE = PLAY + 1;
    //public static final int EXIT = PAUSE + 1;
    public SurfaceHolder gHolder;
    public  SurfaceTexture gSurfaceTexture;
    public Camera gCamera;
    public byte gBuffer[];
    public int textureBuffer[];
    private int bufferSize;
    private Camera.Parameters parameters;
    public int previewWidth, previewHeight;
    public int screenWidth, screenHeight;
    public Bitmap gBitmap;
    private Rect gRect;
    // timer
    private Timer sampleTimer;
    private TimerTask sampleTask;
    private Context mContext ;

    private BeautifyMultiThread mBeautifyMultiThread = new BeautifyMultiThread();


    public GameDisplay(Context context,int screenWidth,int screenHeight) {
        super(context);
        mContext = context ;
        gHolder=this.getHolder();
        gHolder.addCallback(this);
        gHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        gSurfaceTexture=new SurfaceTexture(MAGIC_TEXTURE_ID);
        this.screenWidth=screenWidth;
        this.screenHeight=screenHeight;
        gRect=new Rect(0,0,screenWidth,screenHeight);
        Log.v(TAG, "GameDisplay initialization completed");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        Log.v(TAG, "GameDisplay surfaceChanged");
        parameters = gCamera.getParameters();
        List<Size> preSize = parameters.getSupportedPreviewSizes();
        previewWidth = preSize.get(4).width;
        previewHeight = preSize.get(4).height;
//        for (int i = 1; i < preSize.size(); i++) {
//            double similarity = Math
//                    .abs(((double) preSize.get(i).height / screenHeight)
//                            - ((double) preSize.get(i).width / screenWidth));
//            if (similarity < Math.abs(((double) previewHeight / screenHeight)
//                    - ((double) previewWidth / screenWidth))) {
//                previewWidth = preSize.get(i).width;
//                previewHeight = preSize.get(i).height;
//            }
//        }
        gBitmap= Bitmap.createBitmap(previewWidth, previewHeight, Bitmap.Config.ARGB_8888);
        parameters.setPreviewSize(previewWidth, previewHeight);
        gCamera.setParameters(parameters);
        bufferSize = previewWidth * previewHeight;
        textureBuffer=new int[bufferSize];
        bufferSize  = bufferSize * ImageFormat.getBitsPerPixel(parameters.getPreviewFormat()) / 8;
        gBuffer = new byte[bufferSize];
        gCamera.addCallbackBuffer(gBuffer);
        gCamera.setPreviewCallbackWithBuffer(this);
        gCamera.startPreview();
    }

    private int cameraid = Camera.CameraInfo.CAMERA_FACING_FRONT;
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.v(TAG, "GameDisplay surfaceCreated");
        if (gCamera == null) {
            gCamera = Camera.open(cameraid);
        }
        try {
            gCamera.setPreviewTexture(gSurfaceTexture);
        }catch (Exception e){

        }

        //sampleStart();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.v(TAG, "GameDisplay surfaceDestroyed");
        //gProcessThread.isRunning=false;
        //sampleTimer.cancel();
        //sampleTimer = null;
        //sampleTask.cancel();
        //sampleTask = null;
        gCamera.stopPreview();
        gCamera.release();
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        Log.v(TAG, "GameDisplay onPreviewFrame");
        //gProcessThread.raw_data=data;
        camera.addCallbackBuffer(gBuffer);
//        for(int i=0;i<textureBuffer.length;i++)
//            textureBuffer[i]=0xff000000|data[i];

//        for(int i=0;i<textureBuffer.length;i++)
//            textureBuffer[i]= data[i] > 100 ?255 : 0;

        gBitmap = rawByteArray2RGBABitmap2ForAndroid(data,previewWidth,previewHeight);

        int degrees = getDisplayRotation((Activity)mContext);
        int result;
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraid, info);
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//            result = (info.orientation + degrees) % 360;
//            result = (360 - result) % 360; // compensate the mirror
            result = (info.orientation - degrees + 360) % 360;
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        gBitmap = rotaingImageView(result,gBitmap);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        gBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);

        // 美颜
        gBitmap = mBeautifyMultiThread.beautifyImg(gBitmap,20);
        //ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //gBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);

        // slow
        //gBitmap.setPixels(textureBuffer, 0, previewWidth, 0, 0, previewWidth, previewHeight);
        synchronized (gHolder)
        {
            Canvas canvas = this.getHolder().lockCanvas();
            if(canvas == null)
                return ;
            canvas.drawBitmap(gBitmap, null,gRect, null);
            //canvas.drawBitmap(textureBuffer, 0, screenWidth, 0, 0, screenWidth, screenHeight, false, null);
            this.getHolder().unlockCanvasAndPost(canvas);
        }

    }

    public Bitmap rawByteArray2RGBABitmap2(byte[] data, int width, int height) {
        int frameSize = width * height;
        int[] rgba = new int[frameSize];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int y = (0xff & ((int) data[i * width + j]));
                int u = (0xff & ((int) data[frameSize + (i >> 1) * width + (j & ~1) + 0]));
                int v = (0xff & ((int) data[frameSize + (i >> 1) * width + (j & ~1) + 1]));
                y = y < 16 ? 16 : y;
                int r = Math.round(1.164f * (y - 16) + 1.596f * (v - 128));
                int g = Math.round(1.164f * (y - 16) - 0.813f * (v - 128) - 0.391f * (u - 128));
                int b = Math.round(1.164f * (y - 16) + 2.018f * (u - 128));
                r = r < 0 ? 0 : (r > 255 ? 255 : r);
                g = g < 0 ? 0 : (g > 255 ? 255 : g);
                b = b < 0 ? 0 : (b > 255 ? 255 : b);
                rgba[i * width + j] = 0xff000000 + (b << 16) + (g << 8) + r;
            }
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bmp.setPixels(rgba, 0 , width, 0, 0, width, height);
        return bmp;
    }


    public Bitmap rawByteArray2RGBABitmap2ForAndroid(byte[] data, int width, int height) {
        Bitmap bmp = null ;
        YuvImage image = new YuvImage(data, ImageFormat.NV21, width, height, null);
        if(image!=null){
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compressToJpeg(new Rect(0, 0, width, height), 80, stream);

            bmp = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());
            //TODO：此处可以对位图进行处理，如显示，保存等

            try {
                stream.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return bmp ;

    }


    public  int getDisplayRotation(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        switch (rotation) {
            case Surface.ROTATION_0: return 0;
            case Surface.ROTATION_90: return 90;
            case Surface.ROTATION_180: return 180;
            case Surface.ROTATION_270: return 270;
        }
        return 0;
    }


    public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();;
        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }
}
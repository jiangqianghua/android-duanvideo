package com.jqh.duanvideo.view;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.jqh.duanvideo.utils.BeautifyMultiThread;
import com.jqh.duanvideo.utils.FileUtils;
import com.jqh.jmedia.JMediaPushStream;

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
    private JMediaPushStream mJMediaPushStream ;
    private PcmRecordThread thread;
    private String filePath ;
    private boolean mIsStartPre = false ;

    //线程池维护线程的最少数量
    private static final int COREPOOLSIZE = 2;
    //线程池维护线程的最大数量
    private static final int MAXINUMPOOLSIZE = 5;
    //线程池维护线程所允许的空闲时间
    private static final long KEEPALIVETIME = 4;
    //线程池维护线程所允许的空闲时间的单位
    private static final TimeUnit UNIT = TimeUnit.SECONDS;
    //线程池所使用的缓冲队列,这里队列大小为3
    private static final BlockingQueue<Runnable> WORKQUEUE = new ArrayBlockingQueue<Runnable>(128);
    private static final ThreadPoolExecutor.AbortPolicy HANDLER = new ThreadPoolExecutor.AbortPolicy();
    // TODO 初始化线程池
    ThreadPoolExecutor threadPool = new ThreadPoolExecutor(COREPOOLSIZE, MAXINUMPOOLSIZE, KEEPALIVETIME, UNIT, WORKQUEUE, HANDLER);
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

        mJMediaPushStream = new JMediaPushStream();

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
    public void onPreviewFrame(final byte[] data, Camera camera) {
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

//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        gBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);

        if(mIsStartPre) {
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    //data = getYUVByBitmap(gBitmap);
                    mJMediaPushStream.flushStreamDataToJni(data, 1);
                }
            });
        }

        // 美颜
        //gBitmap = mBeautifyMultiThread.beautifyImg(gBitmap,10);
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

    public void startRecod(){
        if(!mIsStartPre){
            mIsStartPre = true ;
            filePath = FileUtils.getSDPath()+"/a/outVideo.h264";
            File file = new File(filePath);
            if (file.exists())
                file.delete();
            //filePath = "rtmp://livemediabspb.xdfkoo.com/mediaserver/live334422113";
            mJMediaPushStream.publicStreamInit(filePath,previewWidth,previewHeight);
            thread=new PcmRecordThread();
            thread.start();
        }
    }

    public void stopRecod(){
        if(mIsStartPre)
        {
            mIsStartPre = false ;
            mJMediaPushStream.stopStream();
            thread.stopRecord();
        }
    }

    public void toMp4(){
        mJMediaPushStream.toMp4();
    }
    private class PcmRecordThread extends Thread {
        private int sampleRate = 22050;
        private AudioRecord audioRecord;
        private int minBufferSize = 0;
        private boolean isRecording = false;

        public PcmRecordThread() {
            minBufferSize = AudioRecord.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT);
            minBufferSize = 4096;
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT, minBufferSize);

        }

        @Override
        public synchronized void start() {
            audioRecord.startRecording();
            isRecording = true;
            super.start();
        }

        @Override
        public void run() {
            while (isRecording == true) {
                byte[] bytes = new byte[minBufferSize];
                if (audioRecord == null)
                    return;
                int res = audioRecord.read(bytes, 0, minBufferSize);
                if (res > 0 && isRecording == true) {
                    mJMediaPushStream.flushStreamDataToJni(bytes,0);
                }
            }
        }

        public void stopRecord() {
            isRecording = false;
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }
    }


    /**
     * 把Bitmap转Byte
     */
    public static byte[] Bitmap2Bytes(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    /*
 * 获取位图的YUV数据
 */
    public static byte[] getYUVByBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int size = width * height;

        int pixels[] = new int[size];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        // byte[] data = convertColorToByte(pixels);
        byte[] data = encodeYUV420SP(pixels, width, height);

        return data;
    }

    public static byte[] rgb2YCbCr420(int[] pixels, int width, int height) {
        int len = width * height;
        // yuv格式数组大小，y亮度占len长度，u,v各占len/4长度。
        byte[] yuv = new byte[len * 3 / 2];
        int y, u, v;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // 屏蔽ARGB的透明度值
                int rgb = pixels[i * width + j] & 0x00FFFFFF;
                // 像素的颜色顺序为bgr，移位运算。
                int r = rgb & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb >> 16) & 0xFF;
                // 套用公式
                y = ((66 * r + 129 * g + 25 * b + 128) >> 8) + 16;
                u = ((-38 * r - 74 * g + 112 * b + 128) >> 8) + 128;
                v = ((112 * r - 94 * g - 18 * b + 128) >> 8) + 128;
                // rgb2yuv
                // y = (int) (0.299 * r + 0.587 * g + 0.114 * b);
                // u = (int) (-0.147 * r - 0.289 * g + 0.437 * b);
                // v = (int) (0.615 * r - 0.515 * g - 0.1 * b);
                // RGB转换YCbCr
                // y = (int) (0.299 * r + 0.587 * g + 0.114 * b);
                // u = (int) (-0.1687 * r - 0.3313 * g + 0.5 * b + 128);
                // if (u > 255)
                // u = 255;
                // v = (int) (0.5 * r - 0.4187 * g - 0.0813 * b + 128);
                // if (v > 255)
                // v = 255;
                // 调整
                y = y < 16 ? 16 : (y > 255 ? 255 : y);
                u = u < 0 ? 0 : (u > 255 ? 255 : u);
                v = v < 0 ? 0 : (v > 255 ? 255 : v);
                // 赋值
                yuv[i * width + j] = (byte) y;
                yuv[len + (i >> 1) * width + (j & ~1) + 0] = (byte) u;
                yuv[len + +(i >> 1) * width + (j & ~1) + 1] = (byte) v;
            }
        }
        return yuv;
    }


    /**
     * 将bitmap里得到的argb数据转成yuv420sp格式
     * 这个yuv420sp数据就可以直接传给MediaCodec,通过AvcEncoder间接进行编码
     * @param yuv420sp 用来存放yuv429sp数据
     * @param argb 传入argb数据
     * @param width   图片width
     * @param height 图片height
     */
    public static byte[] encodeYUV420SP( int[] argb, int width, int height) {
        final int frameSize = width * height;

        byte[] yuv420sp ;
        int yIndex = 0;
        int uvIndex = frameSize;
        yuv420sp = new byte[frameSize * 3 / 2];
        int a, R, G, B, Y, U, V;
        int index = 0;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {

                a = (argb[index] & 0xff000000) >> 24; // a is not used obviously
                R = (argb[index] & 0xff0000) >> 16;
                G = (argb[index] & 0xff00) >> 8;
                B = (argb[index] & 0xff) >> 0;

                // well known RGB to YUV algorithm
                Y = ( (  66 * R + 129 * G +  25 * B + 128) >> 8) +  16;
                U = ( ( -38 * R -  74 * G + 112 * B + 128) >> 8) + 128;
                V = ( ( 112 * R -  94 * G -  18 * B + 128) >> 8) + 128;

                // NV21 has a plane of Y and interleaved planes of VU each sampled by a factor of 2
                //    meaning for every 4 Y pixels there are 1 V and 1 U.  Note the sampling is every other
                //    pixel AND every other scanline.
                yuv420sp[yIndex++] = (byte) ((Y < 0) ? 0 : ((Y > 255) ? 255 : Y));
                if (j % 2 == 0 && index % 2 == 0) {
                    yuv420sp[uvIndex++] = (byte)((V<0) ? 0 : ((V > 255) ? 255 : V));
                    yuv420sp[uvIndex++] = (byte)((U<0) ? 0 : ((U > 255) ? 255 : U));
                }

                index ++;
            }
        }

        return yuv420sp;
    }
}
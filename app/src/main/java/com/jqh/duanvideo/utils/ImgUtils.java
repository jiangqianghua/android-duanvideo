package com.jqh.duanvideo.utils;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jqh.duanvideo.AppManager;
import com.jqh.duanvideo.R;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Administrator on 2017/4/3.
 */

public class ImgUtils {

    private static final float VER_POSTER_RATO = 0.73f ;
    private static final float HOR_POSTER_RATO = 1.5f ;

    public static void load(String url, ImageView targetView) {
        Glide.with(AppManager.getContext())
                .load(url)
                .into(targetView);
    }

    public static void load(int resId, ImageView targetView) {
        Glide.with(AppManager.getContext())
                .load(resId)
                .into(targetView);
    }

    public static void loadRound(String url, ImageView targetView) {
        Glide.with(AppManager.getContext())
                .load(url)
                .bitmapTransform(new CropCircleTransformation(AppManager.getContext()))
                .into(targetView);
    }

    public static void loadRound(int resId, ImageView targetView) {
        Glide.with(AppManager.getContext())
                .load(resId)
                .bitmapTransform(new CropCircleTransformation(AppManager.getContext()))
                .into(targetView);
    }

    /**
     * 毛玻璃效果
     * @param url
     * @param targetView
     * @param radius
     */
    public static void loadBlur(String url , ImageView targetView,int radius){
        Glide.with(AppManager.getContext())
                .load(url)
                .bitmapTransform(new BlurTransformation(AppManager.getContext(),radius))
                .into(targetView);
    }

    public static void disPlayImage( String url,ImageView view ){
        if(view != null && url != null){
            Glide.with(view.getContext()).load(url).into(view);
        }
    }

    public static void disPlayImage(String url,ImageView view,int w , int h){
        if(view != null && url != null && w > 0 && h > 0)
        {
            if(w > h)
            {
                Glide.with(view.getContext())
                        .load(url)
                        .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存
                        .error(R.mipmap.ic_launcher)
                        .fitCenter()  // 图片居中
                        .override(h,w)
                        .into(view)  //加到imageview
                ;
            }
            else
            {
                Glide.with(view.getContext())
                        .load(url)
                        .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存
                        .error(R.mipmap.ic_launcher)
                        .centerCrop()  // 图片居中
                        .override(w,h)
                        .into(view)  //加到imageview
                ;
            }
        }
    }

    /**
     * 让图片获取到最佳比例
     * @param context
     * @param columns
     * @return
     */
    public static Point getVerPostSize(Context context, int columns){
        int width = getScreenWidthPixel(context)/columns;
        width = width - (int)context.getResources().getDimension(R.dimen.dimen_1dp);
        int height = Math.round((float)width/VER_POSTER_RATO);
        Point point = new Point();
        point.x = width ;
        point.y = height ;
        return point ;
    }

    /**
     * 让图片获取到最佳比例
     * @param context
     * @param columns
     * @return
     */
    public static Point getHorPostSize(Context context, int columns){
        int width = getScreenWidthPixel(context)/columns;
        width = width - (int)context.getResources().getDimension(R.dimen.dimen_6dp);
        int height = Math.round((float)width/HOR_POSTER_RATO);
        Point point = new Point();
        point.x = width ;
        point.y = height ;
        return point ;
    }

    public static int getScreenWidthPixel(Context context){
        WindowManager wm = (WindowManager)context.getSystemService(context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x ;
        return width ;
    }


    public static int getScreenHeightPixel(Context context){
        WindowManager wm = (WindowManager)context.getSystemService(context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int height = point.y ;
        return height ;
    }

}


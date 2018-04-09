package com.jqh.duanvideo;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;

/**
 * 该类做一些初始化的工作
 * Created by jiangqianghua on 18/1/13.
 */

public class AppManager extends Application {

    private static Gson mGson;
    private static OkHttpClient mOkHttpClient;
    private static Context context ;

    @Override
    public void onCreate() {
        super.onCreate();
        mGson = new Gson();
        mOkHttpClient = new OkHttpClient();
        context = this ;

    }

    public static Gson getGson(){
        return mGson;
    }


    public static OkHttpClient getHttpClient()
    {
        return mOkHttpClient;
    }

    public static Context getContext()
    {
        return context ;
    }

    public static Resources getResource(){
        return context.getResources();
    }

    /**
     * 网络是否可用
     * @return
     */
    public static boolean isNetWorkAvailable(){

        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * 获取wifi连接状态
     * @return
     */
    public static boolean isNetWorkWifiAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(1) != null){
            NetworkInfo.State state = connectivityManager.getNetworkInfo(1).getState();
            if(state == NetworkInfo.State.CONNECTED ||
                    state == NetworkInfo.State.CONNECTING){
                return true ;
            }else{
                return false ;
            }
        }
        return false ;
    }
}



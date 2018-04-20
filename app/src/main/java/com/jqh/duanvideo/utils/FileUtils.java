package com.jqh.duanvideo.utils;

import android.os.Environment;

/**
 * Created by jiangqianghua on 18/4/20.
 */

public class FileUtils {

    public static String getSDPath() {
        String str = "";
        if (Environment.getExternalStorageState().equals("mounted"))
            str = Environment.getExternalStorageDirectory().toString();
        return str;
    }
}

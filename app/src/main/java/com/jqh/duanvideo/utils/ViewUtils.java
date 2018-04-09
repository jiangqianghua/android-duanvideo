package com.jqh.duanvideo.utils;

import android.view.View;

/**
 * Created by jiangqianghua on 18/4/9.
 */

public class ViewUtils {


    public static <T extends View> T bindViewId(View view, int resId)
    {
        return (T)view.findViewById(resId);
    }
}

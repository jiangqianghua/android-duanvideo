package com.jqh.duanvideo.view.pullloadview;

import android.content.Context;
import android.widget.LinearLayout;

import com.jqh.duanvideo.R;

/**
 * Created by jiangqianghua on 18/1/21.
 */

public class LoadingView extends LinearLayout {
    public LoadingView(Context context) {
        super(context);
        init();
    }

    private void init(){
        inflate(getContext(), R.layout.view_loading_layout,this);
    }
}

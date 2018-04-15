package com.jqh.duanvideo.widget;

import android.view.View;
import android.widget.Button;

import com.jqh.duanvideo.R;
import com.jqh.duanvideo.base.BaseActivity;

/**
 * Created by jiangqianghua on 18/4/11.
 */

public class RecodCameraActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recodcamera;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.bottom_exit);
    }
}

package com.jqh.duanvideo.widget;

import android.view.View;
import android.widget.Button;

import com.jqh.duanvideo.R;
import com.jqh.duanvideo.base.BaseActivity;

/**
 * Created by jiangqianghua on 18/4/11.
 */

public class RecodCameraActivity extends BaseActivity {

    private Button exitbtn ;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_recodcamera;
    }

    @Override
    protected void initView() {
        exitbtn = bindViewId(R.id.exitbtn);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        exitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecodCameraActivity.this.finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.bottom_exit);
    }
}

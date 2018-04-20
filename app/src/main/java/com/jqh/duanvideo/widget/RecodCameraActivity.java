package com.jqh.duanvideo.widget;

import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.jqh.duanvideo.R;
import com.jqh.duanvideo.base.BaseActivity;
import com.jqh.duanvideo.view.GameDisplay;
import com.jqh.duanvideo.view.RecodBtnView;
import com.jqh.jmedia.JMediaPushStream;

/**
 * Created by jiangqianghua on 18/4/11.
 */

public class RecodCameraActivity extends BaseActivity {

    private RelativeLayout camera_container_rl ;
    private GameDisplay gameDisplay;

    private Button startBtn ;
    private Button stopBtn ;

    private Button toMp4Btn ;

    private RecodBtnView recodBtnView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recodcamera;
    }

    @Override
    protected void initView() {
        camera_container_rl = bindViewId(R.id.camera_container_rl);
        //gameDisplay.setVisibility(SurfaceView.VISIBLE);
        DisplayMetrics dm = getResources().getDisplayMetrics();
//        int screenWidth = dm.widthPixels;
//        int screenHeight = dm.heightPixels;
        gameDisplay= new GameDisplay(this,dm.widthPixels,dm.heightPixels);
        camera_container_rl.addView(gameDisplay);
        gameDisplay.setVisibility(SurfaceView.VISIBLE);

        startBtn = bindViewId(R.id.startBtn);
        stopBtn = bindViewId(R.id.stopBtn);
        toMp4Btn = bindViewId(R.id.toMp4Btn);

        recodBtnView = bindViewId(R.id.recodBtnView);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameDisplay.startRecod();
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameDisplay.stopRecod();
            }
        });

        toMp4Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameDisplay.toMp4();
            }
        });

        recodBtnView.setOnHoldListener(new RecodBtnView.OnHoldListener() {
            @Override
            public void onHold(boolean hold) {
                
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.bottom_exit);
    }
}

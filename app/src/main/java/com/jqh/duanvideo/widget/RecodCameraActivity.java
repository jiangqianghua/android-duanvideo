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

        recodBtnView = bindViewId(R.id.recodBtnView);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        recodBtnView.setOnHoldListener(new RecodBtnView.OnHoldListener() {
            @Override
            public void onHold(boolean hold) {
                if(hold){
                    gameDisplay.startRecod();
                }else{
                    gameDisplay.stopRecod();
                }
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.bottom_exit);
    }
}

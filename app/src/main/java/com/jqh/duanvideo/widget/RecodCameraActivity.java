package com.jqh.duanvideo.widget;

import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jqh.duanvideo.R;
import com.jqh.duanvideo.base.BaseActivity;
import com.jqh.duanvideo.view.GameDisplay;
import com.jqh.duanvideo.view.RecodBtnView;
import com.jqh.jmedia.JMediaPushStream;

import java.util.LinkedList;

import me.lake.librestreaming.filter.hardvideofilter.BaseHardVideoFilter;
import me.lake.librestreaming.filter.hardvideofilter.HardVideoGroupFilter;
import me.lake.librestreaming.ws.StreamAVOption;
import me.lake.librestreaming.ws.StreamLiveCameraView;
import me.lake.librestreaming.ws.filter.hardfilter.GPUImageBeautyFilter;
import me.lake.librestreaming.ws.filter.hardfilter.extra.GPUImageCompatibleFilter;

/**
 * Created by jiangqianghua on 18/4/11.
 */

public class RecodCameraActivity extends BaseActivity {

    private RelativeLayout camera_container_rl ;
    private GameDisplay gameDisplay;

    private RecodBtnView recodBtnView;
    private StreamLiveCameraView mAVRootView;
    private ImageView mSwitchCamera ;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_recodcamera;
    }

    @Override
    protected void initView() {
        mAVRootView = bindViewId(R.id.live_view);

        recodBtnView = bindViewId(R.id.recodBtnView);
        mSwitchCamera = bindViewId(R.id.switch_camera);
        initCamera();
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
                    mAVRootView.startRecord();
                }else{
                    mAVRootView.stopRecord();
                }
            }
        });

        mSwitchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAVRootView.swapCamera();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.bottom_exit);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mAVRootView.destroy();
    }

    private StreamAVOption streamAVOption;
    private void initCamera(){
        //参数配置 start
        streamAVOption = new StreamAVOption();
        //参数配置 end

        mAVRootView.init(this, streamAVOption);
        LinkedList<BaseHardVideoFilter> files = new LinkedList<>();
        files.add(new GPUImageCompatibleFilter<>(new GPUImageBeautyFilter()));
        // files.add(new WatermarkFilter(BitmapFactory.decodeResource(getResources(),R.mipmap.live),new Rect(100,100,200,200)));
        mAVRootView.setHardVideoFilter(new HardVideoGroupFilter(files));

    }
}

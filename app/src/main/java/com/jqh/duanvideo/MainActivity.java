package com.jqh.duanvideo;

import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.jqh.duanvideo.fragment.FollowPageFragment;
import com.jqh.duanvideo.fragment.HomePageFragment;
import com.jqh.duanvideo.view.IconNumView;
import com.jqh.duanvideo.base.BaseActivity;

public class MainActivity extends BaseActivity {

    private ImageView headImageView ;
    private IconNumView mHeartView ;

    private Button homePageBtn ;
    private Button followPageBtn;

    private HomePageFragment mHomePageFragment ;
    private FollowPageFragment mFollowPageFragment ;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
//        headImageView = bindViewId(R.id.header_iv);
//        ImgUtils.loadRound("http://a-ssl.duitang.com/uploads/item/201404/15/20140415192752_JGUFz.jpeg",
//                headImageView);

//        mHeartView = bindViewId(R.id.heart_view);
//        mHeartView.loadIcon(R.mipmap.ic_launcher);
//        mHeartView.setNum(9999);

        homePageBtn = bindViewId(R.id.homepage_btn);
        followPageBtn = bindViewId(R.id.followpage_btn);

        // 默认显示第一个
        switchToHomePage();

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        homePageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToHomePage();
            }
        });

        followPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToFollowPage();
            }
        });
    }

    private void switchToHomePage(){
        if(mHomePageFragment == null)
            mHomePageFragment = HomePageFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_layout,mHomePageFragment);
        ft.commit();
        // 延迟构造
        getSupportFragmentManager().executePendingTransactions();

    }

    private void switchToFollowPage(){
        if(mFollowPageFragment == null)
            mFollowPageFragment = mFollowPageFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_layout,mFollowPageFragment);
        ft.commit();
        // 延迟构造
        getSupportFragmentManager().executePendingTransactions();
    }
}

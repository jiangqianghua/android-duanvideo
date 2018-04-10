package com.jqh.duanvideo;

import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.jqh.duanvideo.fragment.FollowPageFragment;
import com.jqh.duanvideo.fragment.HomePageFragment;
import com.jqh.duanvideo.fragment.MePageFragment;
import com.jqh.duanvideo.fragment.MessagePageFragment;
import com.jqh.duanvideo.view.BottomNavigationBarView;
import com.jqh.duanvideo.view.IconNumView;
import com.jqh.duanvideo.base.BaseActivity;

public class MainActivity extends BaseActivity {


    private BottomNavigationBarView mBottomNavigationBarView ;
    private HomePageFragment mHomePageFragment ;
    private FollowPageFragment mFollowPageFragment ;
    private MessagePageFragment mMessagePageFragment;
    private MePageFragment mMePageFragment ;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

        mBottomNavigationBarView = bindViewId(R.id.BottomNavigationBar_View);
        // 默认显示第一个
        switchToHomePage();

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

        mBottomNavigationBarView.setOnBottomNavigationBarListener(new BottomNavigationBarView.OnBottomNavigationBarListener() {
            @Override
            public void onHomePageItemClick() {
                switchToHomePage();
            }

            @Override
            public void onFollowPageItemClick() {
                switchToFollowPage();
            }

            @Override
            public void onMessagePageItemClick() {
                switchToMessagePage();
            }

            @Override
            public void onMePageItemClick() {
                switchToMePage();
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

    private void switchToMessagePage(){
        if(mMessagePageFragment == null)
            mMessagePageFragment = mMessagePageFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_layout,mMessagePageFragment);
        ft.commit();
        // 延迟构造
        getSupportFragmentManager().executePendingTransactions();
    }

    private void switchToMePage(){
        if(mMePageFragment == null)
            mMePageFragment = mMePageFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_layout,mMePageFragment);
        ft.commit();
        // 延迟构造
        getSupportFragmentManager().executePendingTransactions();
    }
}

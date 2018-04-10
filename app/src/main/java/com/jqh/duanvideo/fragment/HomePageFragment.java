package com.jqh.duanvideo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.jqh.duanvideo.R;
import com.jqh.duanvideo.base.BaseFragment;
import com.jqh.duanvideo.utils.LogUtils;
import com.jqh.duanvideo.utils.ViewUtils;
import com.jqh.duanvideo.view.HomePageTopView;

/**
 * Created by jiangqianghua on 18/4/9.
 */

public class HomePageFragment extends BaseFragment {

    private HomePageTopView mHomePageTopView ;

    private NearbyPageFragment mNearbyPageFragment ;
    private RecommendPageFragment mRecommendPageFragment;

    @Override
    protected void initView() {
        mHomePageTopView = bindViewId(R.id.homepageTop_view);
        initEvent();
        switchToRecommendPage();
    }

    private void initEvent(){
        mHomePageTopView.setOnHomePageTopClickListener(new HomePageTopView.OnHomePageTopClickListener() {
            @Override
            public void onRecommendClick() {
                switchToRecommendPage();
            }

            @Override
            public void onNearbyClick() {
                switchToNearbyPage();
            }
        });
    }
    public static HomePageFragment newInstance() {
        Bundle bundle = new Bundle();
        HomePageFragment fragment = new HomePageFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    private void switchToRecommendPage(){
        if(mRecommendPageFragment == null)
            mRecommendPageFragment = RecommendPageFragment.newInstance();
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.frame_layout,mRecommendPageFragment);
        ft.commit();
        // 延迟构造
        getChildFragmentManager().executePendingTransactions();
    }

    private void switchToNearbyPage(){
        if(mNearbyPageFragment == null)
            mNearbyPageFragment = NearbyPageFragment.newInstance();
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.frame_layout,mNearbyPageFragment);
        ft.commit();
        // 延迟构造
        getChildFragmentManager().executePendingTransactions();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_homepage;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d("onDestroy");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.d("onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.d("onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.d("onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d("onResume");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtils.d("onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtils.d("onDetach");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.d("onDestroyView");
    }
}

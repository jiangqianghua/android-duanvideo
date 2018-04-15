package com.jqh.duanvideo.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.jqh.duanvideo.R;
import com.jqh.duanvideo.base.BaseFragment;
import com.jqh.duanvideo.inter.ActivityInterface;
import com.jqh.duanvideo.utils.LogUtils;
import com.jqh.duanvideo.utils.ViewUtils;
import com.jqh.duanvideo.view.HomePageTopView;

import java.util.ArrayList;

/**
 * Created by jiangqianghua on 18/4/9.
 */

public class HomePageFragment extends BaseFragment {

    private HomePageTopView mHomePageTopView ;

    private NearbyPageFragment mNearbyPageFragment ;
    private RecommendPageFragment mRecommendPageFragment;
    private Fragment mCurrentFragmen = null; // 记录当前显示的Fragment

    private String[] mFragmentTagList = {"RecommendPageFragment", "NearbyPageFragment"};
    private FragmentManager mFm;
    private ActivityInterface mActivityInterface ;
    private ArrayList<Fragment> mFragmentList = new ArrayList<Fragment>();

    @Override
    protected void initView() {
        mHomePageTopView = bindViewId(R.id.homepageTop_view);
        initEvent();
    }

    private void initEvent(){
        mHomePageTopView.setOnHomePageTopClickListener(new HomePageTopView.OnHomePageTopClickListener() {
            @Override
            public void onRecommendClick() {
                switchFragment(mFragmentList.get(0),mFragmentTagList[0]);
                mActivityInterface.setBottomBarColor(R.color.bottomtoolbar_nono);
            }

            @Override
            public void onNearbyClick() {
                switchFragment(mFragmentList.get(1),mFragmentTagList[1]);
                mActivityInterface.setBottomBarColor(R.color.bottomtoolbar_black);
            }
        });
    }
    public static HomePageFragment newInstance() {
        Bundle bundle = new Bundle();
        HomePageFragment fragment = new HomePageFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_homepage;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivityInterface = (ActivityInterface)activity;
    }

    @Override
    protected void initData() {
        mRecommendPageFragment = RecommendPageFragment.newInstance();
        mNearbyPageFragment = NearbyPageFragment.newInstance();

        mFragmentList.add(0, mRecommendPageFragment);
        mFragmentList.add(1, mNearbyPageFragment);

        mCurrentFragmen = mFragmentList.get(0);

        mFm = getChildFragmentManager();
        FragmentTransaction transaction = mFm.beginTransaction();
        transaction.add(R.id.frame_layout, mCurrentFragmen, mFragmentTagList[0]);
        transaction.commitAllowingStateLoss();
    }

    // 转换Fragment
    void switchFragment(Fragment to, String tag){
        if(mCurrentFragmen != to){
            FragmentTransaction transaction = mFm.beginTransaction();
            if(!to.isAdded()){
                // 没有添加过:
                // 隐藏当前的，添加新的，显示新的
                transaction.hide(mCurrentFragmen).add(R.id.frame_layout, to, tag).show(to);
            }else{
                // 隐藏当前的，显示新的
                transaction.hide(mCurrentFragmen).show(to);
            }
            mCurrentFragmen = to;
            transaction.commitAllowingStateLoss();

        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtils.d("onHiddenChanged");
        if( !hidden ){
            if(mCurrentFragmen instanceof RecommendPageFragment){
                mActivityInterface.setBottomBarColor(R.color.bottomtoolbar_nono);
            }
            else
            {
                mActivityInterface.setBottomBarColor(R.color.bottomtoolbar_black);
            }
        }
    }


}

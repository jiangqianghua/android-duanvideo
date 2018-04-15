package com.jqh.duanvideo.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;

import com.jqh.duanvideo.R;
import com.jqh.duanvideo.base.BaseFragment;
import com.jqh.duanvideo.inter.ActivityInterface;
import com.jqh.duanvideo.utils.ImgUtils;
import com.jqh.duanvideo.view.IndircatiorBarView;

import java.util.ArrayList;

/**
 * Created by jiangqianghua on 18/4/11.
 */

public class MePageFragment extends BaseFragment {

    private ImageView mBackGroudImageView  ;
    private ImageView mAvaterImageView ;

    private IndircatiorBarView mIndircatiorBarView ;

    private WorksListFragment mWorksListFragment ;
    private WorksListFragment mLikeListFragment ;

    private ActivityInterface mActivityInterface ;

    private ArrayList<Fragment> mFragmentList = new ArrayList<Fragment>();
    private Fragment mCurrentFragmen = null; // 记录当前显示的Fragment

    private final static String[] mFragmentTagList = {"mWorksListFragment", "mLikeListFragment"};
    private FragmentManager mFm;

    private String avater ;
    private int userId ;

    public static MePageFragment newInstance() {
        
        Bundle args = new Bundle();
        
        MePageFragment fragment = new MePageFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected void initView() {
        mBackGroudImageView = bindViewId(R.id.backgourd_imageview);
        mAvaterImageView = bindViewId(R.id.avater_iv);
        mIndircatiorBarView = bindViewId(R.id.indircatiorbar_view);
        mFm = this.getChildFragmentManager();
        mActivityInterface.setBottomBarColor(R.color.bottomtoolbar_black);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_userinfo;
    }

    @Override
    protected void initData() {
        avater = "http://img3.duitang.com/uploads/item/201410/13/20141013082510_wCKhQ.jpeg";
        ImgUtils.loadBlur(avater,mBackGroudImageView,60);
        ImgUtils.loadRound(avater,mAvaterImageView);
        mWorksListFragment = WorksListFragment.newInstance();
        mLikeListFragment = WorksListFragment.newInstance();
        mFragmentList.add(mWorksListFragment);
        mFragmentList.add(mLikeListFragment);
        mCurrentFragmen = mFragmentList.get(0);

        FragmentTransaction transaction = mFm.beginTransaction();
        transaction.add(R.id.frame_layout, mCurrentFragmen, mFragmentTagList[0]);
        transaction.commitAllowingStateLoss();

        initEvent();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivityInterface = (ActivityInterface)activity;
    }

    private void initEvent(){
        mIndircatiorBarView.setOnIndircatiorBarClickListener(new IndircatiorBarView.OnIndircatiorBarClickListener() {
            @Override
            public void onWorksItemclick() {
                switchFragment(mFragmentList.get(0),mFragmentTagList[0]);
            }

            @Override
            public void onLikeItemClick() {
                switchFragment(mFragmentList.get(1),mFragmentTagList[1]);
            }
        });
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
        if( !hidden ){
            mActivityInterface.setBottomBarColor(R.color.bottomtoolbar_black);
        }
    }

}

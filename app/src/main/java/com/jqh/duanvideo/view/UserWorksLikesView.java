package com.jqh.duanvideo.view;

import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;

import com.jqh.duanvideo.R;
import com.jqh.duanvideo.base.BaseLayout;
import com.jqh.duanvideo.fragment.WorksListFragment;

import java.util.ArrayList;

/**
 * Created by jiangqianghua on 18/4/14.
 */

public class UserWorksLikesView extends BaseLayout {

    private IndircatiorBarView mIndircatiorBarView ;

    private WorksListFragment mWorksListFragment ;
    private WorksListFragment mLikeListFragment ;


    private ArrayList<Fragment> mFragmentList = new ArrayList<Fragment>();
    private Fragment mCurrentFragmen = null; // 记录当前显示的Fragment

    private final static String[] mFragmentTagList = {"mWorksListFragment", "mLikeListFragment"};
    private FragmentManager mFm;

    public UserWorksLikesView(Context context) {
        super(context);
    }

    public UserWorksLikesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public UserWorksLikesView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_userworkslikes;
    }

    @Override
    protected void initView() {
        mIndircatiorBarView = bindViewId(R.id.indircatiorbar_view);
        mFm = ((AppCompatActivity)mContext).getSupportFragmentManager();
        initData();
        initEvent();
    }

    private void initData(){
        if(mFragmentList == null)
            mFragmentList = new ArrayList<Fragment>();

        mWorksListFragment = WorksListFragment.newInstance();
        mLikeListFragment = WorksListFragment.newInstance();
        mFragmentList.add(mWorksListFragment);
        mFragmentList.add(mLikeListFragment);

        mCurrentFragmen = mFragmentList.get(0);

        FragmentTransaction transaction = mFm.beginTransaction();
        transaction.add(R.id.frame_layout, mCurrentFragmen, mFragmentTagList[0]);
        transaction.commitAllowingStateLoss();

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
}

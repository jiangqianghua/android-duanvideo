package com.jqh.duanvideo;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.jqh.duanvideo.dialog.CommentDialog;
import com.jqh.duanvideo.fragment.FollowPageFragment;
import com.jqh.duanvideo.fragment.HomePageFragment;
import com.jqh.duanvideo.fragment.MePageFragment;
import com.jqh.duanvideo.fragment.MessagePageFragment;
import com.jqh.duanvideo.view.BottomNavigationBarView;
import com.jqh.duanvideo.base.BaseActivity;
import com.jqh.duanvideo.widget.RecodCameraActivity;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {


    private BottomNavigationBarView mBottomNavigationBarView ;
    private HomePageFragment mHomePageFragment ;
    private FollowPageFragment mFollowPageFragment ;
    private MessagePageFragment mMessagePageFragment;
    private MePageFragment mMePageFragment ;

    private ArrayList<Fragment> mFragmentList = new ArrayList<Fragment>();
    private Fragment mCurrentFragmen = null; // 记录当前显示的Fragment

    private String[] mFragmentTagList = {"HomePageFragment", "FollowPageFragment", "MessagePageFragment","MePageFragment"};
    private FragmentManager mFm;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

        mBottomNavigationBarView = bindViewId(R.id.BottomNavigationBar_View);

    }

    @Override
    protected void initData() {
        mHomePageFragment = HomePageFragment.newInstance();
        mFollowPageFragment = FollowPageFragment.newInstance();
        mMessagePageFragment = MessagePageFragment.newInstance();
        mMePageFragment = MePageFragment.newInstance();

        mFragmentList.add(0, mHomePageFragment);
        mFragmentList.add(1, mFollowPageFragment);
        mFragmentList.add(2, mMessagePageFragment);
        mFragmentList.add(3, mMePageFragment);

        mCurrentFragmen = mFragmentList.get(0);


        mFm = getSupportFragmentManager();
        FragmentTransaction transaction = mFm.beginTransaction();
        transaction.add(R.id.frame_layout, mCurrentFragmen, mFragmentTagList[0]);
        transaction.commitAllowingStateLoss();
    }

    @Override
    protected void initEvent() {

        mBottomNavigationBarView.setOnBottomNavigationBarListener(new BottomNavigationBarView.OnBottomNavigationBarListener() {
            @Override
            public void onHomePageItemClick() {
                switchFragment(mFragmentList.get(0),mFragmentTagList[0]);
            }

            @Override
            public void onFollowPageItemClick() {
                switchFragment(mFragmentList.get(1),mFragmentTagList[1]);
            }

            @Override
            public void onMessagePageItemClick() {
                switchFragment(mFragmentList.get(2),mFragmentTagList[2]);
            }

            @Override
            public void onMePageItemClick() {
                switchFragment(mFragmentList.get(3),mFragmentTagList[3]);
            }

            @Override
            public void onRecodeCameraItemClick() {
                Intent intent = new Intent(MainActivity.this,RecodCameraActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.bottom_entry,0);
            }
        });
    }

    // 当activity非正常销毁时被调用
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        // 重置Fragment，防止当内存不足时导致Fragment重叠
        updateFragment(outState);
    }


    // 重置Fragment
    private void updateFragment(Bundle outState) {

        mFm = getSupportFragmentManager();
        if(outState == null){
            FragmentTransaction transaction = mFm.beginTransaction();
            mHomePageFragment = HomePageFragment.newInstance();
            mCurrentFragmen = mHomePageFragment;
            transaction.add(R.id.frame_layout, mHomePageFragment, mFragmentTagList[0]).commitAllowingStateLoss();
        }else{
            // 通过tag找到fragment并重置
            HomePageFragment oneFragment = (HomePageFragment) mFm.findFragmentByTag(mFragmentTagList[0]);
            FollowPageFragment twoFragment = (FollowPageFragment) mFm.findFragmentByTag(mFragmentTagList[1]);
            MessagePageFragment threeFragment = (MessagePageFragment) mFm.findFragmentByTag(mFragmentTagList[2]);
            MePageFragment fourFragment = (MePageFragment) mFm.findFragmentByTag(mFragmentTagList[3]);
            mFm.beginTransaction().show(oneFragment).hide(twoFragment).hide(threeFragment).hide(fourFragment);
        }
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

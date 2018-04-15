package com.jqh.duanvideo.widget;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.jqh.duanvideo.R;
import com.jqh.duanvideo.base.BaseActivity;
import com.jqh.duanvideo.fragment.WorksListFragment;
import com.jqh.duanvideo.utils.ImgUtils;
import com.jqh.duanvideo.view.IndircatiorBarView;

import java.util.ArrayList;

/**
 * Created by jiangqianghua on 18/4/11.
 */

public class UserInfoActivity extends BaseActivity {
    private ImageView mBackGroudImageView  ;
    private ImageView mAvaterImageView ;

    private IndircatiorBarView mIndircatiorBarView ;

    private WorksListFragment mWorksListFragment ;
    private WorksListFragment mLikeListFragment ;


    private ArrayList<Fragment> mFragmentList = new ArrayList<Fragment>();
    private Fragment mCurrentFragmen = null; // 记录当前显示的Fragment

    private final static String[] mFragmentTagList = {"mWorksListFragment", "mLikeListFragment"};
    private FragmentManager mFm;

    private String avater ;
    private int userId ;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_userinfo;
    }

    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        avater = bundle.getString("avater");

        //setSupportActionBar(R.id.userinfo_toolbar);
        //setTitle("用户信息");
        //setSupportArrowActionBar(true);
        mBackGroudImageView = bindViewId(R.id.backgourd_imageview);
        mAvaterImageView = bindViewId(R.id.avater_iv);
        mIndircatiorBarView = bindViewId(R.id.indircatiorbar_view);
        mFm = this.getSupportFragmentManager();
    }



    @Override
    protected void initData() {
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
    }

    @Override
    protected void initEvent() {
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.right_exit);
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

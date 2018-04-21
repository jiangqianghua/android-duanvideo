package com.jqh.duanvideo.widget;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.jqh.duanvideo.R;
import com.jqh.duanvideo.base.BaseActivity;
import com.jqh.duanvideo.fragment.PlayVideoPageItemFragment;
import com.jqh.duanvideo.model.VideoModule;
import com.jqh.duanvideo.viewpager.VerticalViewPager;

import java.util.ArrayList;

/**
 * Created by jiangqianghua on 18/4/21.
 */

public class PlayVideoListActivity extends BaseActivity {

    private VerticalViewPager mViewPager ;


    private Fragment fm1,fm2,fm3,fm4,fm5,fm6,fm7,fm8;
    private FragmentPagerAdapter mAdapter ;
    private ArrayList<Fragment> mDatas;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_playvideolist;
    }

    @Override
    protected void initView() {
        mViewPager = bindViewId(R.id.view_pager);
    }

    @Override
    protected void initData() {
        VideoModule recommendModule1 = new VideoModule();
        recommendModule1.setAvater("http://www.qqzhi.com/uploadpic/2014-09-23/000247589.jpg");
        recommendModule1.setmMediaUlr("/storage/emulated/0/a/outVideo.h264");//http://v.xdfkoo.com/126856/liveV421091964379749002_126856_0000.mp4
        recommendModule1.setLikeNum(100);
        recommendModule1.setSendNum(99);
        recommendModule1.setUserId(0);
        recommendModule1.setWorksId(100);
        recommendModule1.setComentNum(66);
        fm1 = PlayVideoPageItemFragment.newInstance(recommendModule1);
        recommendModule1 = new VideoModule();
        recommendModule1.setAvater("http://img1.2345.com/duoteimg/qqTxImg/2013/12/ns/29-020632_476.jpg");
        recommendModule1.setmMediaUlr("http://ksy.fffffive.com/mda-hifsrhtqjn8jxeha/mda-hifsrhtqjn8jxeha.mp4");
        recommendModule1.setLikeNum(100);
        recommendModule1.setSendNum(99);
        recommendModule1.setUserId(0);
        recommendModule1.setWorksId(100);
        recommendModule1.setComentNum(66);
        fm2 = PlayVideoPageItemFragment.newInstance(recommendModule1);
        recommendModule1 = new VideoModule();
        recommendModule1.setAvater("http://img3.duitang.com/uploads/item/201608/12/20160812005801_kKHTy.jpeg");
        recommendModule1.setmMediaUlr("http://ksy.fffffive.com/mda-hiw61ic7i4qkcvmx/mda-hiw61ic7i4qkcvmx.mp4");
        recommendModule1.setLikeNum(100);
        recommendModule1.setSendNum(99);
        recommendModule1.setUserId(0);
        recommendModule1.setWorksId(100);
        recommendModule1.setComentNum(66);
        fm3 = PlayVideoPageItemFragment.newInstance(recommendModule1);
        recommendModule1 = new VideoModule();
        recommendModule1.setAvater("http://img5q.duitang.com/uploads/item/201503/21/20150321114038_fJyMS.jpeg");
        recommendModule1.setmMediaUlr("http://ksy.fffffive.com/mda-hihvysind8etz7ga/mda-hihvysind8etz7ga.mp4");
        recommendModule1.setLikeNum(100);
        recommendModule1.setSendNum(99);
        recommendModule1.setUserId(0);
        recommendModule1.setWorksId(100);
        recommendModule1.setComentNum(66);
        fm4 = PlayVideoPageItemFragment.newInstance(recommendModule1);
        recommendModule1 = new VideoModule();
        recommendModule1.setAvater("http://www.feizl.com/upload2007/2015_01/1501031935730412.jpg");
        recommendModule1.setmMediaUlr("http://ksy.fffffive.com/mda-hidnzn5r61qwhxp4/mda-hidnzn5r61qwhxp4.mp4");
        recommendModule1.setLikeNum(100);
        recommendModule1.setSendNum(99);
        recommendModule1.setUserId(0);
        recommendModule1.setWorksId(100);
        recommendModule1.setComentNum(66);
        fm5 = PlayVideoPageItemFragment.newInstance(recommendModule1);
        recommendModule1 = new VideoModule();
        recommendModule1.setAvater("http://img0.imgtn.bdimg.com/it/u=2855421225,14851720&fm=214&gp=0.jpg");
        recommendModule1.setmMediaUlr("http://ksy.fffffive.com/mda-he1zy3rky0rwrf60/mda-he1zy3rky0rwrf60.mp4");
        recommendModule1.setLikeNum(100);
        recommendModule1.setSendNum(99);
        recommendModule1.setUserId(0);
        recommendModule1.setWorksId(100);
        recommendModule1.setComentNum(66);
        fm6 = PlayVideoPageItemFragment.newInstance(recommendModule1);
        recommendModule1 = new VideoModule();
        recommendModule1.setAvater("http://img1.touxiang.cn/uploads/20121212/12-055808_368.jpg");
        recommendModule1.setmMediaUlr("http://ksy.fffffive.com/mda-hh6cxd0dqjqcszcj/mda-hh6cxd0dqjqcszcj.mp4");
        recommendModule1.setLikeNum(100);
        recommendModule1.setSendNum(99);
        recommendModule1.setUserId(0);
        recommendModule1.setWorksId(100);
        recommendModule1.setComentNum(66);
        fm7 = PlayVideoPageItemFragment.newInstance(recommendModule1);
        recommendModule1 = new VideoModule();
        recommendModule1.setAvater("http://www.qqzhi.com/uploadpic/2014-09-23/000247589.jpg");
        recommendModule1.setmMediaUlr("http://ksy.fffffive.com/mda-hifsrhtqjn8jxeha/mda-hifsrhtqjn8jxeha.mp4");
        recommendModule1.setLikeNum(100);
        recommendModule1.setSendNum(99);
        recommendModule1.setUserId(0);
        recommendModule1.setWorksId(100);
        recommendModule1.setComentNum(66);
        fm8 = PlayVideoPageItemFragment.newInstance(recommendModule1);

        mDatas = new ArrayList<>();
        mDatas.add(fm1);
        mDatas.add(fm2);
        mDatas.add(fm3);
        mDatas.add(fm4);
        mDatas.add(fm5);
        mDatas.add(fm6);
        mDatas.add(fm7);
        mDatas.add(fm8);
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mDatas.get(position);
            }

            @Override
            public int getCount() {
                return mDatas.size();
            }
        };
        mViewPager.setAdapter(mAdapter);
    }

    @Override
    protected void initEvent() {
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //LogUtils.d("position="+position + " positionOffset="+positionOffset + " positionOffsetPixels="+positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                setSelect(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setSelect(int index) {
        mViewPager.setCurrentItem(index);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.right_exit);
    }
}

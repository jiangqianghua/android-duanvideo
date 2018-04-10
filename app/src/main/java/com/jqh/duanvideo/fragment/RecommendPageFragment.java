package com.jqh.duanvideo.fragment;

import android.os.Bundle;

import com.jqh.duanvideo.R;
import com.jqh.duanvideo.base.BaseFragment;

/**
 * Created by jiangqianghua on 18/4/10.
 */

public class RecommendPageFragment extends BaseFragment {

    public static RecommendPageFragment newInstance() {
        
        Bundle args = new Bundle();
        
        RecommendPageFragment fragment = new RecommendPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recommendpage;
    }

    @Override
    protected void initData() {

    }
}

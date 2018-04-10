package com.jqh.duanvideo.fragment;

import android.os.Bundle;

import com.jqh.duanvideo.R;
import com.jqh.duanvideo.base.BaseFragment;

/**
 * Created by jiangqianghua on 18/4/10.
 */

public class NearbyPageFragment extends BaseFragment {

    public static NearbyPageFragment newInstance() {
        
        Bundle args = new Bundle();
        
        NearbyPageFragment fragment = new NearbyPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_nearbypage;
    }

    @Override
    protected void initData() {

    }
}

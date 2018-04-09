package com.jqh.duanvideo.fragment;

import android.os.Bundle;

import com.jqh.duanvideo.R;
import com.jqh.duanvideo.base.BaseFragment;

/**
 * Created by jiangqianghua on 18/4/9.
 */

public class FollowPageFragment extends BaseFragment {

    public static FollowPageFragment newInstance() {
        
        Bundle bundle = new Bundle();
        
        FollowPageFragment fragment = new FollowPageFragment();
        fragment.setArguments(bundle);

        return fragment;
    }
    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_followpage;
    }

    @Override
    protected void initData() {

    }
}

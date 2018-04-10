package com.jqh.duanvideo.fragment;

import android.os.Bundle;

import com.jqh.duanvideo.R;
import com.jqh.duanvideo.base.BaseFragment;

/**
 * Created by jiangqianghua on 18/4/11.
 */

public class MePageFragment extends BaseFragment {

    public static MePageFragment newInstance() {
        
        Bundle args = new Bundle();
        
        MePageFragment fragment = new MePageFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mepage;
    }

    @Override
    protected void initData() {

    }
}

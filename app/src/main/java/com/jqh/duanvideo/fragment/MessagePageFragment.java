package com.jqh.duanvideo.fragment;

import android.content.Context;
import android.os.Bundle;

import com.jqh.duanvideo.R;
import com.jqh.duanvideo.base.BaseFragment;
import com.jqh.duanvideo.utils.LogUtils;

/**
 * Created by jiangqianghua on 18/4/11.
 */

public class MessagePageFragment extends BaseFragment {
    public static MessagePageFragment newInstance() {
        
        Bundle args = new Bundle();
        
        MessagePageFragment fragment = new MessagePageFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_messagepage;
    }

    @Override
    protected void initData() {

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtils.d("mess onHiddenChanged");
    }

}

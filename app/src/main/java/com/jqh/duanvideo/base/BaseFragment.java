package com.jqh.duanvideo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jiangqianghua on 17/9/20.
 */

public abstract class BaseFragment extends Fragment {

    private View mContentView ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mContentView = getActivity().getLayoutInflater().inflate(getLayoutId(),container,false);
        initView();
        initData();
        return mContentView;
    }

    protected abstract void initView();

    protected abstract int getLayoutId();

    protected abstract void initData();

    protected <T extends View> T bindViewId(int resId){
        return (T)mContentView.findViewById(resId);
    }
}

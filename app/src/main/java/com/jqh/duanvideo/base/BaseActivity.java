package com.jqh.duanvideo.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by jiangqianghua on 18/4/9.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar mToolBar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        initView();
        initData();
        initEvent();
    }


    protected abstract int getLayoutId();
    protected abstract void initView();
    protected abstract void initData();
    protected abstract void initEvent();

    protected <T extends View> T bindViewId(int resId)
    {
        return (T)findViewById(resId);
    }

    protected void setSupportActionBar()
    {
//        mToolBar = bindViewId(R.id.title_bar);
//        if(mToolBar != null)
//        {
//            setSupportActionBar(mToolBar);
//        }
    }

    /**
     * 是否支持返回箭头
     * @param isSupport
     */
    protected void setSupportArrowActionBar(boolean isSupport)
    {
        getSupportActionBar().setDisplayHomeAsUpEnabled(isSupport);
    }

    protected void setActionBarIcon(int resId)
    {
        if(mToolBar != null)
        {
            mToolBar.setNavigationIcon(resId);
        }
    }
}

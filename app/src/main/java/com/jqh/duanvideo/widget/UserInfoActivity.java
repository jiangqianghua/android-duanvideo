package com.jqh.duanvideo.widget;

import com.jqh.duanvideo.R;
import com.jqh.duanvideo.base.BaseActivity;

/**
 * Created by jiangqianghua on 18/4/11.
 */

public class UserInfoActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_userinfo;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.right_exit);
    }
}

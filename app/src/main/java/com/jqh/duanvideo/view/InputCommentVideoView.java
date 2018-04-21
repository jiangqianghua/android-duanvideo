package com.jqh.duanvideo.view;

import android.content.Context;
import android.util.AttributeSet;

import com.jqh.duanvideo.R;
import com.jqh.duanvideo.base.BaseLayout;

/**
 * Created by jiangqianghua on 18/4/21.
 */

public class InputCommentVideoView extends BaseLayout {

    public InputCommentVideoView(Context context) {
        super(context);
    }

    public InputCommentVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InputCommentVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_inputcommentvideo;
    }

    @Override
    protected void initView() {

    }
}

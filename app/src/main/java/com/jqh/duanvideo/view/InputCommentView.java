package com.jqh.duanvideo.view;

import android.content.Context;
import android.util.AttributeSet;

import com.jqh.duanvideo.R;
import com.jqh.duanvideo.base.BaseLayout;

/**
 * Created by jiangqianghua on 18/4/21.
 */

public class InputCommentView extends BaseLayout{

    public InputCommentView(Context context) {
        super(context);
    }

    public InputCommentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public InputCommentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_inputcomment;
    }

    @Override
    protected void initView() {

    }
}

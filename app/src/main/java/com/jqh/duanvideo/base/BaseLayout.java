package com.jqh.duanvideo.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by jiangqianghua on 18/4/14.
 */

public abstract class BaseLayout extends RelativeLayout {

    protected Context mContext ;

    public BaseLayout(Context context) {
        super(context);
        init(context);
    }

    public BaseLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BaseLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    protected abstract int getLayoutId();

    protected abstract void initView();
    private void init(Context context){
        this.mContext = context ;
        LayoutInflater.from(context).inflate(getLayoutId(),this);
        initView();
    }

    protected <T extends View> T bindViewId(int resId){
        return (T)this.findViewById(resId);
    }

}

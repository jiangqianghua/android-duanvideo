package com.jqh.duanvideo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jqh.duanvideo.R;
import com.jqh.duanvideo.utils.ImgUtils;
import com.jqh.duanvideo.utils.ViewUtils;

/**
 * Created by jiangqianghua on 18/4/9.
 */
public class IconNumView extends LinearLayout {

    private Context mContext ;
    private ImageView mIconImageView ;
    private TextView mNumTextView ;
    private int normal_icon ;
    private int selected_icon ;

    private boolean isNormal = true ;
    public IconNumView(Context context) {
        super(context);
        init(context);
    }

    public IconNumView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public IconNumView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray attributes = context.obtainStyledAttributes(attrs,R.styleable.IconNumView);
        normal_icon = attributes.getResourceId(R.styleable.IconNumView_normal_icon,0);
        selected_icon = attributes.getResourceId(R.styleable.IconNumView_selected_icon,0);
        init(context);
    }

    private void init(Context context){
        this.mContext = context ;
        LayoutInflater.from(context).inflate(R.layout.view_icon_num,this);
        mIconImageView = ViewUtils.bindViewId(this,R.id.icon_iv);
        loadIcon(normal_icon);
        mNumTextView = ViewUtils.bindViewId(this,R.id.num_tv);
    }

    public void loadIcon(int resId){
        ImgUtils.load(resId,mIconImageView);
    }

    public void setNum(int num){
        mNumTextView.setText(num+"");
    }

    public void setNormal(){
        isNormal = true ;
        loadIcon(normal_icon);
    }

    public void setSelected(){
        isNormal = false;
        loadIcon(selected_icon);
    }

    public boolean isSelected() {
        return !isNormal;
    }
}

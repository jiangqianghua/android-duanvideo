package com.jqh.duanvideo.view;

import android.content.Context;
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
        init(context);
    }

    private void init(Context context){
        this.mContext = context ;
        LayoutInflater.from(context).inflate(R.layout.view_icon_num,this);
        mIconImageView = ViewUtils.bindViewId(this,R.id.icon_iv);
        mNumTextView = ViewUtils.bindViewId(this,R.id.num_tv);
    }

    public void loadIcon(int resId){
        ImgUtils.loadRound(resId,mIconImageView);
    }

    public void setNum(int num){
        mNumTextView.setText(num+"");
    }


}

package com.jqh.duanvideo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jqh.duanvideo.R;
import com.jqh.duanvideo.utils.ViewUtils;

/**
 * Created by jiangqianghua on 18/4/10.
 */

public class BottomNavigationItemView extends RelativeLayout {

    private Context mContext ;
    private String name ;
    private TextView mItemNameTextView ;
    private View mLineView ;
    private int mSelectedColor ;
    private int mUnSelectedColor ;
    private boolean mSelected;

    public BottomNavigationItemView(Context context) {
        super(context);
        init(context);
    }

    public BottomNavigationItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public BottomNavigationItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray attributes = context.obtainStyledAttributes(attrs,R.styleable.BottomNavigationItemView);
        name = attributes.getString(R.styleable.BottomNavigationItemView_name);
        mSelectedColor = attributes.getColor(R.styleable.BottomNavigationItemView_selected_color,getResources().getColor(R.color.bottomitemselected));
        mUnSelectedColor = attributes.getColor(R.styleable.BottomNavigationItemView_unselected_color,getResources().getColor(R.color.bottomitemunselected));
        mSelected = attributes.getBoolean(R.styleable.BottomNavigationItemView_selected,false);
        init(context);
    }

    private void init(Context context){
        this.mContext = context ;
        LayoutInflater.from(context).inflate(R.layout.view_bottomnavigationitem,this);
        mItemNameTextView = ViewUtils.bindViewId(this,R.id.item_name_tv);
        mLineView = ViewUtils.bindViewId(this,R.id.line_view);
        mLineView.setBackgroundColor(mSelectedColor);
        mItemNameTextView.setText(name);
        setSelected(mSelected);
    }

    public void setSelected(boolean selected){
        this.mSelected = selected;
        if(selected){
            mItemNameTextView.setTextColor(mSelectedColor);
            mLineView.setVisibility(VISIBLE);
        }
        else{
            mItemNameTextView.setTextColor(mUnSelectedColor);
            mLineView.setVisibility(INVISIBLE);
        }
    }


}

package com.jqh.duanvideo.view;

import android.content.Context;
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

public class HomePageTopView extends RelativeLayout {
    private Context mContext ;

    private TextView mRecommendTextView ;
    private TextView mNearbyTextView ;
    private OnHomePageTopClickListener mOnHomePageTopClickListener ;

    public void setOnHomePageTopClickListener(OnHomePageTopClickListener mOnHomePageTopClickListener) {
        this.mOnHomePageTopClickListener = mOnHomePageTopClickListener;
    }

    public interface OnHomePageTopClickListener{
        void onRecommendClick();
        void onNearbyClick();
    }
    public HomePageTopView(Context context) {
        super(context);
        init(context);
    }

    public HomePageTopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HomePageTopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_homepage_top,this);
        mRecommendTextView = ViewUtils.bindViewId(this,R.id.recommend_tv);
        mNearbyTextView = ViewUtils.bindViewId(this,R.id.nearby_tv);
        initEvent();

        switchToRecommend();
    }

    private void initEvent(){
        mRecommendTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnHomePageTopClickListener != null) {
                    mOnHomePageTopClickListener.onRecommendClick();
                    switchToRecommend();
                }
            }
        });

        mNearbyTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnHomePageTopClickListener != null) {
                    mOnHomePageTopClickListener.onNearbyClick();
                    switchToNearby();
                }
            }
        });
    }

    private void switchToRecommend(){
        mRecommendTextView.setTextColor(mContext.getResources().getColor(R.color.white));
        mNearbyTextView.setTextColor(0xffcccccc);
        mRecommendTextView.setTextSize(25);
        mNearbyTextView.setTextSize(20);
    }

    private void switchToNearby(){
        mRecommendTextView.setTextColor(0xffcccccc);
        mNearbyTextView.setTextColor(mContext.getResources().getColor(R.color.white));
        mRecommendTextView.setTextSize(20);
        mNearbyTextView.setTextSize(25);
    }

}

package com.jqh.duanvideo.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jqh.duanvideo.R;
import com.jqh.duanvideo.base.BaseLayout;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jiangqianghua on 18/4/14.
 */

public class IndircatiorBarView extends BaseLayout {

    private String[] mSiteNames = new String[]{"作品","喜欢"};
    private List<String> mDataSet = Arrays.asList(mSiteNames);

    private RelativeLayout mWorksContainerLayout;
    private RelativeLayout mLikeContainerLayout ;
    private TextView mWorksTextView;
    private TextView mLikeTextView ;

    private OnIndircatiorBarClickListener mOnIndircatiorBarClickListener ;

    public void setOnIndircatiorBarClickListener(OnIndircatiorBarClickListener mOnIndircatiorBarClickListener) {
        this.mOnIndircatiorBarClickListener = mOnIndircatiorBarClickListener;
    }

    public interface OnIndircatiorBarClickListener{
        void onWorksItemclick();
        void onLikeItemClick();
    }
    public IndircatiorBarView(Context context) {
        super(context);
    }

    public IndircatiorBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IndircatiorBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_indircationbar;
    }

    @Override
    protected void initView() {
        mWorksContainerLayout = bindViewId(R.id.works_container_rl);
        mLikeContainerLayout = bindViewId(R.id.like_container_rl);
        mWorksTextView = bindViewId(R.id.works_num_tv);
        mLikeTextView = bindViewId(R.id.like_num_tv);
        switchToWorksContainer();
        initEvent();
    }

    private void initEvent(){
        mWorksContainerLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToWorksContainer();
                if(mOnIndircatiorBarClickListener != null)
                    mOnIndircatiorBarClickListener.onWorksItemclick();
            }
        });

        mLikeContainerLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToLikeContainer();
                if(mOnIndircatiorBarClickListener != null)
                    mOnIndircatiorBarClickListener.onLikeItemClick();
            }
        });
    }

    private void switchToWorksContainer(){
        mWorksContainerLayout.setBackgroundColor(mContext.getResources().getColor(R.color.indiraction_bg_select));
        mWorksTextView.setTextColor(mContext.getResources().getColor(R.color.indiraction_text_select));
        mLikeContainerLayout.setBackgroundColor(mContext.getResources().getColor(R.color.indiraction_bg_unselect));
        mLikeTextView.setTextColor(mContext.getResources().getColor(R.color.indiraction_text_unselect));
    }

    private void switchToLikeContainer(){
        mLikeContainerLayout.setBackgroundColor(mContext.getResources().getColor(R.color.indiraction_bg_select));
        mLikeTextView.setTextColor(mContext.getResources().getColor(R.color.indiraction_text_select));
        mWorksContainerLayout.setBackgroundColor(mContext.getResources().getColor(R.color.indiraction_bg_unselect));
        mWorksTextView.setTextColor(mContext.getResources().getColor(R.color.indiraction_text_unselect));
    }

    public void setWorksNum(int num){
        mLikeTextView.setText("作品 "+num);
    }

    public void setLikesNum(int num){
        mWorksTextView.setText("喜欢 "+num);
    }
}

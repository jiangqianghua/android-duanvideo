package com.jqh.duanvideo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jqh.duanvideo.R;
import com.jqh.duanvideo.utils.ViewUtils;

/**
 * 底部导航栏
 * Created by jiangqianghua on 18/4/10.
 */
public class BottomNavigationBarView extends RelativeLayout{

    private BottomNavigationItemView mHomePageItem ;
    private BottomNavigationItemView mFollowPageItem ;
    private BottomNavigationItemView mMessagePageItem ;
    private BottomNavigationItemView mMePageItem ;
    private ImageView mStartrecodeItem ;

    private Context mContext ;

    private OnBottomNavigationBarListener mOnBottomNavigationBarListener ;

    public void setOnBottomNavigationBarListener(OnBottomNavigationBarListener mOnBottomNavigationBarListener) {
        this.mOnBottomNavigationBarListener = mOnBottomNavigationBarListener;
    }

    public interface OnBottomNavigationBarListener{
        void onHomePageItemClick();
        void onFollowPageItemClick();
        void onMessagePageItemClick();
        void onMePageItemClick();
        void onRecodeCameraItemClick();
    }
    public BottomNavigationBarView(Context context) {
        super(context);
        init(context);
    }

    public BottomNavigationBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BottomNavigationBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        this.mContext = context ;
        LayoutInflater.from(context).inflate(R.layout.view_bottomnavigationbar,this);
        mHomePageItem = ViewUtils.bindViewId(this,R.id.homepage_view);
        mFollowPageItem = ViewUtils.bindViewId(this,R.id.followpage_view);
        mMessagePageItem = ViewUtils.bindViewId(this,R.id.messagepage_view);
        mMePageItem = ViewUtils.bindViewId(this,R.id.mepage_view);
        mStartrecodeItem = ViewUtils.bindViewId(this,R.id.startrecode_imageview);
        initEvent();
    }

    private void initEvent(){
        mHomePageItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAll();
                mHomePageItem.setSelected(true);
                if(mOnBottomNavigationBarListener != null)
                    mOnBottomNavigationBarListener.onHomePageItemClick();
            }
        });

        mFollowPageItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAll();
                mFollowPageItem.setSelected(true);
                if(mOnBottomNavigationBarListener != null)
                    mOnBottomNavigationBarListener.onFollowPageItemClick();
            }
        });

        mMessagePageItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAll();
                mMessagePageItem.setSelected(true);
                if(mOnBottomNavigationBarListener != null)
                    mOnBottomNavigationBarListener.onMessagePageItemClick();
            }
        });

        mMePageItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectAll();
                mMePageItem.setSelected(true);
                if(mOnBottomNavigationBarListener != null)
                    mOnBottomNavigationBarListener.onMePageItemClick();
            }
        });

        mStartrecodeItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnBottomNavigationBarListener != null)
                    mOnBottomNavigationBarListener.onRecodeCameraItemClick();
            }
        });
    }

    private void unSelectAll(){
        mHomePageItem.setSelected(false);
        mFollowPageItem.setSelected(false);
        mMessagePageItem.setSelected(false);
        mMePageItem.setSelected(false);
    }
}

package com.jqh.duanvideo.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.VideoView;

import com.jqh.duanvideo.R;
import com.jqh.duanvideo.base.BaseFragment;
import com.jqh.duanvideo.dialog.CommentDialog;
import com.jqh.duanvideo.utils.LogUtils;
import com.jqh.duanvideo.view.JVideoView;
import com.jqh.duanvideo.view.RightToolView;
import com.jqh.duanvideo.widget.UserInfoActivity;

/**
 * Created by user on 2018/4/12.
 */
public class RecommendPageItemFragment extends BaseFragment {


    private boolean isCreate = false ;
    private JVideoView mVideoView ;
    private String mUrl;
    private RightToolView mRightToolView ;
    private Activity mAttachActivity ;
    public static RecommendPageItemFragment newInstance(String url) {
        
        Bundle args = new Bundle();
        args.putString("url",url);
        RecommendPageItemFragment fragment = new RecommendPageItemFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected void initView() {

        mVideoView = bindViewId(R.id.video_view);
        mRightToolView = bindViewId(R.id.tools_view);
        Bundle bundle = getArguments();
        mUrl = bundle.getString("url");
        isCreate = true ;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recommendpageitem;
    }

    @Override
    protected void initData() {
        initEvent();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAttachActivity = (Activity)context;
    }

    private void initEvent(){
        mRightToolView.setOnRightToolItemClickListener(new RightToolView.OnRightToolItemClickListener() {
            @Override
            public void onAvatarClick() {
                Intent intent = new Intent(mAttachActivity,UserInfoActivity.class);
                mAttachActivity.startActivity(intent);
                mAttachActivity.overridePendingTransition(R.anim.right_entry,0);
            }

            @Override
            public void onLikeClick() {

            }

            @Override
            public void onCommentClick() {
                CommentDialog commentDialog = new CommentDialog(getContext());
                commentDialog.show();
            }

            @Override
            public void onSendClick() {

            }
        });
    }

    // 不起作用
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    // 在切换fragment会调用一次，但如果tag为null，意味着initView没有被调用，这个时候我们不需要做任何处理
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            LogUtils.d("show");
        }
        else{
            LogUtils.d("hidden");
        }

        if(isCreate && isVisibleToUser){
            try {
                mVideoView.start(mUrl);
            }catch (Exception e){
                e.printStackTrace();
            }

        } else if(isCreate && !isVisibleToUser){
            mVideoView.stop();
        }
    }



}

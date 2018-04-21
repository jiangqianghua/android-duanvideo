package com.jqh.duanvideo.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jqh.duanvideo.R;
import com.jqh.duanvideo.base.BaseFragment;
import com.jqh.duanvideo.dialog.CommentDialog;
import com.jqh.duanvideo.model.VideoModule;
import com.jqh.duanvideo.utils.LogUtils;
import com.jqh.duanvideo.utils.ShareUtils;
import com.jqh.duanvideo.view.JVideoView;
import com.jqh.duanvideo.view.RightToolView;
import com.jqh.duanvideo.widget.UserInfoActivity;

/**
 * Created by user on 2018/4/12.
 */
public class PlayVideoPageItemFragment extends BaseFragment {


    private boolean isCreate = false ;
    private JVideoView mVideoView ;
    private RightToolView mRightToolView ;
    private Activity mAttachActivity ;



    private int userId ;
    private int worksId;
    private String url ;
    private String avater ;
    private int commentsNum ;
    private int sendsNum;
    private int likesNum ;
    public static PlayVideoPageItemFragment newInstance(VideoModule recommendModule) {

        Bundle args = new Bundle();
        args.putInt("userId",recommendModule.getUserId());
        args.putInt("worksId",recommendModule.getWorksId());
        args.putString("url",recommendModule.getmMediaUlr());
        args.putString("avater",recommendModule.getAvater());
        args.putInt("comment",recommendModule.getComentNum());
        args.putInt("like",recommendModule.getLikeNum());
        args.putInt("send",recommendModule.getSendNum());
        PlayVideoPageItemFragment fragment = new PlayVideoPageItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        userId = bundle.getInt("userId");
        worksId = bundle.getInt("worksId");
        url = bundle.getString("url");
        avater = bundle.getString("avater");
        commentsNum = bundle.getInt("comment");
        sendsNum = bundle.getInt("send");
        likesNum = bundle.getInt("like");

    }

    @Override
    protected void initView() {

        mVideoView = bindViewId(R.id.video_view);
        mRightToolView = bindViewId(R.id.tools_view);
        isCreate = true ;

        mRightToolView.setCommentNum(commentsNum);
        mRightToolView.setmLikeNum(likesNum);
        mRightToolView.setSendNum(sendsNum);
        mRightToolView.loadAvater(avater);

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
            public void onAvatarClick(int userId,String avater) {
                Intent intent = new Intent(mAttachActivity,UserInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("userId",userId);
                bundle.putString("avater",avater);
                intent.putExtras(bundle);
                mAttachActivity.startActivity(intent);
                mAttachActivity.overridePendingTransition(R.anim.right_entry,0);
            }

            @Override
            public void onLikeClick(boolean islike) {

                if(islike) {
                    likesNum++;
                }
                else{
                    likesNum--;
                }
                mRightToolView.updateLike(likesNum, islike);
            }

            @Override
            public void onCommentClick() {
                CommentDialog commentDialog = new CommentDialog(getContext());
                commentDialog.show();
            }

            @Override
            public void onSendClick() {
                ShareUtils.showShare(mAttachActivity);
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
                mVideoView.start(url);
            }catch (Exception e){
                e.printStackTrace();
            }

        } else if(isCreate && !isVisibleToUser){
            mVideoView.stop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mVideoView.stop();
    }
}

package com.jqh.duanvideo.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jqh.duanvideo.MainActivity;
import com.jqh.duanvideo.R;
import com.jqh.duanvideo.base.BaseFragment;
import com.jqh.duanvideo.dialog.CommentDialog;
import com.jqh.duanvideo.utils.LogUtils;
import com.jqh.duanvideo.widget.UserInfoActivity;

/**
 * Created by jiangqianghua on 18/4/10.
 */

public class RecommendPageFragment extends BaseFragment {

    private Button popCommentBtn ;
    private Button popUserinfoBtn;
    private Activity mAttachActivity ;
    public static RecommendPageFragment newInstance() {
        
        Bundle args = new Bundle();
        
        RecommendPageFragment fragment = new RecommendPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        popCommentBtn = bindViewId(R.id.pop_comment);
        popUserinfoBtn = bindViewId(R.id.pop_userinfo);
        initEvent();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAttachActivity = (Activity)context;
    }

    private void initEvent(){
        popCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentDialog commentDialog = new CommentDialog(getContext());
                commentDialog.show();
            }
        });

        popUserinfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mAttachActivity,UserInfoActivity.class);
                mAttachActivity.startActivity(intent);
                mAttachActivity.overridePendingTransition(R.anim.right_entry,0);

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recommendpage;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtils.d("onHiddenChanged");
    }
}

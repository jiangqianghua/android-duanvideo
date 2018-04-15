package com.jqh.duanvideo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.jqh.duanvideo.R;
import com.jqh.duanvideo.adapter.CommonAdapter;
import com.jqh.duanvideo.adapter.ViewHolder;
import com.jqh.duanvideo.base.BaseDialog;
import com.jqh.duanvideo.model.CommentModule;
import com.jqh.duanvideo.utils.ImgUtils;
import com.jqh.duanvideo.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangqianghua on 18/4/11.
 */

public class CommentDialog extends BaseDialog {


    private List<CommentModule> mCommentModuleList;
    private ListView mCommentListView ;
    private CommentAdapter mCommentAdapter;
    public CommentDialog(Context context) {
        super(context);
    }

    @Override
    protected int getDialogStyleId() {
        return R.style.CommentDialogStyle;
    }

    @Override
    protected View getView() {

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_comment,null);
        mCommentListView = ViewUtils.bindViewId(view,R.id.comment_lv);

        return view;
    }

    @Override
    protected void initData() {
        mCommentModuleList = new ArrayList<>();

        //  test data
        CommentModule commentModule = new CommentModule();
        commentModule.setLikeNum(7);
        commentModule.setAvater("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523737110895&di=39db8b332312dbf5067532614ba0a602&imgtype=0&src=http%3A%2F%2Fimg3.duitang.com%2Fuploads%2Fitem%2F201512%2F10%2F20151210140032_dncWK.jpeg");
        commentModule.setCommentContent("非常棒，内容我喜欢...非常棒，内容我喜欢...非常棒，内容我喜欢...非常棒，内容我喜欢...非常棒，内容我喜欢...非常棒，内容我喜欢...非常棒，内容我喜欢...非常棒，内容我喜欢...非常棒，内容我喜欢...非常棒，内容我喜欢...非常棒，内容我喜欢...");
        commentModule.setDate("4月15号");
        commentModule.setUserName("快乐主义1");
        mCommentModuleList.add(commentModule);

        commentModule = new CommentModule();
        commentModule.setLikeNum(7);
        commentModule.setAvater("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523737111294&di=15d8dcfeb0592266b8e2b5229353d58e&imgtype=0&src=http%3A%2F%2Fimg4.duitang.com%2Fuploads%2Fitem%2F201512%2F09%2F20151209144050_5Tn3N.jpeg");
        commentModule.setCommentContent("非常棒，内容我喜欢...");
        commentModule.setDate("4月15号");
        commentModule.setUserName("快乐主义2");
        mCommentModuleList.add(commentModule);

        commentModule = new CommentModule();
        commentModule.setLikeNum(7);
        commentModule.setAvater("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523737111294&di=3408aee1da68409f0a63b6adc40388a7&imgtype=0&src=http%3A%2F%2Fimg3.duitang.com%2Fuploads%2Fitem%2F201512%2F09%2F20151209151540_wZNMx.jpeg");
        commentModule.setCommentContent("非常棒，内容我喜欢...非常棒，内容我喜欢...非常棒，内容我喜欢...非常棒，内容我喜欢...非常棒，内容我喜欢...非常棒，内容我喜欢...非常棒，内容我喜欢...非常棒，内容我喜欢...非常棒，内容我喜欢...");
        commentModule.setDate("4月15号");
        commentModule.setUserName("快乐主义3");
        mCommentModuleList.add(commentModule);

        commentModule = new CommentModule();
        commentModule.setLikeNum(7);
        commentModule.setAvater("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523737111294&di=45ceae3cf5067f389e1a512eb50f4c71&imgtype=0&src=http%3A%2F%2Fp3.wmpic.me%2Farticle%2F2015%2F03%2F19%2F1426744136_aAQzjXGj.jpeg");
        commentModule.setCommentContent("非常棒，内容我喜欢...");
        commentModule.setDate("4月15号");
        commentModule.setUserName("快乐主义4");
        mCommentModuleList.add(commentModule);
        commentModule = new CommentModule();
        commentModule.setLikeNum(7);
        commentModule.setAvater("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523737111294&di=3d723f56dbb8511ad068deee0b920972&imgtype=0&src=http%3A%2F%2Fcdnq.duitang.com%2Fuploads%2Fitem%2F201502%2F17%2F20150217161549_C4K8L.thumb.700_0.jpeg");
        commentModule.setCommentContent("非常棒，内容我喜欢...");
        commentModule.setDate("4月15号");
        commentModule.setUserName("快乐主义5");
        mCommentModuleList.add(commentModule);

        commentModule = new CommentModule();
        commentModule.setLikeNum(7);
        commentModule.setAvater("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523737111294&di=85fb0862f33e5fe5783de4aadaff2b8f&imgtype=0&src=http%3A%2F%2Fimg3.duitang.com%2Fuploads%2Fitem%2F201502%2F23%2F20150223104319_KhVsH.thumb.700_0.jpeg");
        commentModule.setCommentContent("非常棒，内容我喜欢...非常棒，内容我喜欢...非常棒，内容我喜欢...非常棒，内容我喜欢...非常棒，内容我喜欢...非常棒，内容我喜欢...非常棒，内容我喜欢...非常棒，内容我喜欢...非常棒，内容我喜欢...非常棒，内容我喜欢...非常棒，内容我喜欢...非常棒，内容我喜欢...非常棒，内容我喜欢...非常棒，内容我喜欢...非常棒，内容我喜欢...非常棒，内容我喜欢...非常棒，内容我喜欢...非常棒，内容我喜欢...非常棒，内容我喜欢...非常棒，内容我喜欢...非常棒，内容我喜欢...非常棒，内容我喜欢...非常棒，内容我喜欢...");
        commentModule.setDate("4月15号");
        commentModule.setUserName("快乐主义6");
        mCommentModuleList.add(commentModule);

        mCommentAdapter = new CommentAdapter(context,mCommentModuleList,R.layout.item_comment);
        mCommentListView.setAdapter(mCommentAdapter);
    }

    @Override
    protected void setWindowAttr() {
//隐藏系统输入盘
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0); // dialog 边距
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 0;//设置Dialog距离底部的距离
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = (int)context.getResources().getDimension(R.dimen.dimen_400dp);
        //lp.dimAmount = 0.2f;
        //    将属性设置给窗体
        dialogWindow.setAttributes(lp);
    }


    private class CommentAdapter extends CommonAdapter<CommentModule>{

        private Context mContext ;

        public CommentAdapter(Context context, List<CommentModule> datas, int layoutId) {
            super(context, datas, layoutId);
            this.mContext = context;
        }

        @Override
        public void convert(ViewHolder holder, CommentModule commentModule) {
            holder.setText(R.id.comment_name_tv,commentModule.getUserName());
            holder.setText(R.id.comment_date_tv,commentModule.getDate());
            holder.setText(R.id.comment_content_tv,commentModule.getCommentContent());
            holder.setRoundImage(R.id.comment_avater_iv,commentModule.getAvater());
            holder.setText(R.id.comment_likenum_tv,commentModule.getLikeNum()+"");


        }
    }
}

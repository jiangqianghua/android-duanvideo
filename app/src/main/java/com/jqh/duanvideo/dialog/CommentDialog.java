package com.jqh.duanvideo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jqh.duanvideo.R;
import com.jqh.duanvideo.base.BaseDialog;

/**
 * Created by jiangqianghua on 18/4/11.
 */

public class CommentDialog extends BaseDialog {


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

        return view;
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
}

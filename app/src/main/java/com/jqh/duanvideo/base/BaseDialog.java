package com.jqh.duanvideo.base;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.jqh.duanvideo.R;

/**
 * Created by jiangqianghua on 18/4/11.
 */

public abstract class BaseDialog {

    //这些属性，Context 是肯定要的，基本对话框要用它
    protected Context context;
    private Display display;//这个设置显示属性用的
    protected Dialog dialog;//自定义Dialog，Dialog还是要有一个的吧

    //对话框布局的样式ID (通过这个抽象方法，我们可以给不同的对话框设置不同样式主题)
    protected abstract int getDialogStyleId();
    //构建对话框的方法(都说了是不同的对话框，布局什么的肯定是不一样的)
    protected abstract View getView();

    protected abstract void setWindowAttr();

    protected abstract void initData();

    //构造方法 来实现 最基本的对话框
    public BaseDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        //在这里初始化 基础对话框s
        if (getDialogStyleId() == 0){
           // dialog = new Dialog(context, DIALOG_COMMON_STYLE );
        }else {
            dialog = new Dialog(context, getDialogStyleId());
        }
        // 调整dialog背景大小
       // getView().setLayoutParams(new FrameLayout.LayoutParams((int) (display.getWidth() * 1), LinearLayout.LayoutParams.WRAP_CONTENT));
        dialog.setContentView(getView());
        setWindowAttr();

        initData();
    }

    /** * Dialog 的基础方法，
     *凡是要用的就在这写出来，然后直接用对话框调本来的方法就好了，不够自己加~hhh */

    //像这类设置对话框属性的方法，就返回值写自己，这样就可以一条链式设置了
    public BaseDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public void show() {
        dialog.show();
    }

    public void dismiss(){
        dialog.dismiss();
    }

    public boolean isShowing(){
        return dialog.isShowing();
    }

    public BaseDialog setdismissListeren(DialogInterface.OnDismissListener dismissListener){
        dialog.setOnDismissListener(dismissListener);
        return this;
    }
}

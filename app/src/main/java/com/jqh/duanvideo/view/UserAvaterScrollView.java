package com.jqh.duanvideo.view;

import android.content.Context;
import android.util.AttributeSet;

import com.jqh.duanvideo.R;
import com.jqh.duanvideo.adapter.CommonAdapter;
import com.jqh.duanvideo.adapter.ViewHolder;
import com.jqh.duanvideo.base.BaseLayout;
import com.jqh.duanvideo.model.CommentModule;
import com.jqh.duanvideo.model.UserModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangqianghua on 18/4/15.
 */

public class UserAvaterScrollView extends BaseLayout {

    private List<UserModule> mUserModuleList;
    private UserAvaterAdapter mUserAvaterAdapter;
    private HorizontalListView mHorizontalListView ;
    public UserAvaterScrollView(Context context) {
        super(context);
    }

    public UserAvaterScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public UserAvaterScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_useravaterscroll;
    }

    @Override
    protected void initView() {
        mHorizontalListView = bindViewId(R.id.horizontallist_view);
        initData();
    }

    private void initData(){
        mUserModuleList = new ArrayList<>();
        UserModule userModule = null;

        userModule = new UserModule();
        userModule.setAvater("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=359604599,305372113&fm=27&gp=0.jpg");
        userModule.setName("江强华1");
        mUserModuleList.add(userModule);

        userModule = new UserModule();
        userModule.setAvater("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523770022492&di=1d1c2d3d6a101dd1cc93436876797400&imgtype=0&src=http%3A%2F%2Fimg5.duitang.com%2Fuploads%2Fitem%2F201512%2F17%2F20151217101815_AMEuB.jpeg");
        userModule.setName("江强华2");
        mUserModuleList.add(userModule);

        userModule = new UserModule();
        userModule.setAvater("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523770022491&di=76ae6b5cc177cf2de708cbef75ef8d85&imgtype=0&src=http%3A%2F%2Fimg5.duitang.com%2Fuploads%2Fitem%2F201502%2F13%2F20150213155836_Z2RGa.jpeg");
        userModule.setName("江强华3");
        mUserModuleList.add(userModule);

        userModule = new UserModule();
        userModule.setAvater("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523770022491&di=fe4dd8db06986270528c1bd9b1a575c5&imgtype=0&src=http%3A%2F%2Fimg5.duitang.com%2Fuploads%2Fitem%2F201410%2F20%2F20141020203619_VPFRr.jpeg");
        userModule.setName("江强华4");
        mUserModuleList.add(userModule);

        userModule = new UserModule();
        userModule.setAvater("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523770022491&di=646718289a62cc18c864b09fec8917e9&imgtype=0&src=http%3A%2F%2Fimg5.duitang.com%2Fuploads%2Fitem%2F201411%2F22%2F20141122001808_w4AVY.jpeg");
        userModule.setName("江强华5");
        mUserModuleList.add(userModule);

        userModule = new UserModule();
        userModule.setAvater("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523770022490&di=da6e22226c2e76f4235b70c0eb594d2f&imgtype=0&src=http%3A%2F%2Fimg4q.duitang.com%2Fuploads%2Fitem%2F201505%2F23%2F20150523165254_SwNn4.jpeg");
        userModule.setName("江强华6");
        mUserModuleList.add(userModule);

        userModule = new UserModule();
        userModule.setAvater("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523770022490&di=ad131635c40fac59282d22fb089b4a42&imgtype=0&src=http%3A%2F%2Fimg3.duitang.com%2Fuploads%2Fitem%2F201412%2F09%2F20141209231325_RtGtF.jpeg");
        userModule.setName("江强华7");
        mUserModuleList.add(userModule);

        userModule = new UserModule();
        userModule.setAvater("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523770022490&di=1012e957c76f97c8312381e7999fe2c0&imgtype=0&src=http%3A%2F%2Fimg4.duitang.com%2Fuploads%2Fitem%2F201507%2F02%2F20150702115925_AfKtT.jpeg");
        userModule.setName("江强华8");
        mUserModuleList.add(userModule);

        userModule = new UserModule();
        userModule.setAvater("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523770022490&di=4a2f2fccffa2b2d312de2a1a5fe3bc69&imgtype=0&src=http%3A%2F%2Fimg4.duitang.com%2Fuploads%2Fitem%2F201507%2F02%2F20150702192435_ekQAB.jpeg");
        userModule.setName("江强华");
        mUserModuleList.add(userModule);

        userModule = new UserModule();
        userModule.setAvater("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523770022489&di=58ea126df89124ba0e53ad05d4056910&imgtype=0&src=http%3A%2F%2Fimg5q.duitang.com%2Fuploads%2Fitem%2F201506%2F07%2F20150607230548_PXNWi.jpeg");
        userModule.setName("江强华9");
        mUserModuleList.add(userModule);

        mUserAvaterAdapter = new UserAvaterAdapter(mContext,mUserModuleList,R.layout.item_useravater);
        mHorizontalListView.setAdapter(mUserAvaterAdapter);

    }

    private class UserAvaterAdapter extends CommonAdapter<UserModule>{

        private Context mContext ;

        public UserAvaterAdapter(Context context, List<UserModule> datas, int layoutId) {
            super(context, datas, layoutId);
            this.mContext = context;
        }

        @Override
        public void convert(ViewHolder holder, UserModule userModule) {
            holder.setText(R.id.username_iv,userModule.getName());
            holder.setRoundImage(R.id.useravater_iv,userModule.getAvater());
        }
    }
}

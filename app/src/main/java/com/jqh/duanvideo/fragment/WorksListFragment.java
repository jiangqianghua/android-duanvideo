package com.jqh.duanvideo.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jqh.duanvideo.R;
import com.jqh.duanvideo.base.BaseFragment;
import com.jqh.duanvideo.model.WorksItemModule;
import com.jqh.duanvideo.utils.ImgUtils;
import com.jqh.duanvideo.utils.ViewUtils;
import com.jqh.duanvideo.view.pullloadview.PullLoadRecyclerView;
import com.jqh.duanvideo.view.pullloadview.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangqianghua on 18/4/14.
 */

public class WorksListFragment extends BaseFragment{


    private int mColumns = 3;
    private int pageNo = 0 ;
    private PullLoadRecyclerView mPullLoadRecyclerView ;
    private WorksListAdapter mWorksListAdapter ;
    private Handler mHandler = new Handler(Looper.getMainLooper());// 在主线程
    public static final int REFRESH_DURATION = 500;//刷新时长，毫秒
    public static final int LOADMORE_DURATION = 100;//刷新时长，毫秒

    public static WorksListFragment newInstance() {
        
        Bundle args = new Bundle();
        
        WorksListFragment fragment = new WorksListFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected void initView() {
        mPullLoadRecyclerView = bindViewId(R.id.pullLoadRecyclerView);
        mPullLoadRecyclerView.setOnPullLoadMoreListener(new PullLoadRecyclerListener());
        mPullLoadRecyclerView.setGridLayout(mColumns);
        mPullLoadRecyclerView.addItemDecoration(new SpaceItemDecoration(0));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_workslist;
    }

    @Override
    protected void initData() {
        reRreshData();
    }

    private void reRreshData(){
        // 请求接口刷新数据
        pageNo = 0 ;
        mWorksListAdapter = null ;
        mWorksListAdapter = new WorksListAdapter(getActivity());

        loadMoreData();
        mPullLoadRecyclerView.setAdapter(mWorksListAdapter);
    }

    private void loadMoreData(){
        pageNo++;
        // 加载更多数据
        List<String> imagelist = new ArrayList<>();
        imagelist.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523692525652&di=0449eb70cca169bda356047ec82f0c77&imgtype=0&src=http%3A%2F%2Fwww.zhlzw.com%2FUploadFiles%2FArticle_UploadFiles%2F201204%2F20120412123916285.jpg");
        imagelist.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523692525651&di=cecb024c87f168eaa2b57f692759912c&imgtype=0&src=http%3A%2F%2Fpic7.nipic.com%2F20100602%2F2177138_170759552925_2.jpg");
        imagelist.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523692525651&di=1cdd2f6782c01e67a7838136737239f4&imgtype=0&src=http%3A%2F%2Fpic10.photophoto.cn%2F20090224%2F0036036802407491_b.jpg");
        imagelist.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523692525650&di=4089be886ff7ef3bb2a0f2f7859289df&imgtype=0&src=http%3A%2F%2Fwww.zhlzw.com%2FUploadFiles%2FArticle_UploadFiles%2F201204%2F20120422013455474.JPG");
        imagelist.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523692525645&di=4ce4acbdcb389053955fcf950a932157&imgtype=0&src=http%3A%2F%2Fwww.zhlzw.com%2FUploadFiles%2FArticle_UploadFiles%2F201210%2F20121006154613648.jpg");
        imagelist.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1523697756117&di=cf5da61d7644025ff496b0120c8cfd36&imgtype=0&src=http%3A%2F%2Fimg.tupianzj.com%2Fuploads%2Fallimg%2F160812%2F9-160Q2215I3.jpg");
        //  test data
        for(int i = 0 ; i < 6 ; i++){
            WorksItemModule work = new WorksItemModule();
            work.setCover(imagelist.get(i));
            work.setAvater("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1524287334&di=febd03c2460eab35d74bb31a02a6054a&imgtype=jpg&er=1&src=http%3A%2F%2Fimg.cheshi-img.com%2Fproduct%2F1_1024%2F1536%2F4b7a38619b46e.jpg");
            work.setDistance("0.8km");
            work.setLikeNum(pageNo+i);
            work.setWorksDetail("美女哦，你要来吗,哈哈哈哈哈哈哈哈哈美女哦，你要来吗,哈哈哈哈哈哈哈哈哈");
            work.setWorksName("作品"+pageNo+"-"+i);
            mWorksListAdapter.setData(work);
        }

        mWorksListAdapter.notifyDataSetChanged();
    }

    public class PullLoadRecyclerListener implements PullLoadRecyclerView.OnPullLoadMoreListener
    {
        @Override
        public void reRresh() {
            //刷新数据
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //reRreshData();
                    mPullLoadRecyclerView.setRefreshCompleted();
                }
            },REFRESH_DURATION);

        }

        @Override
        public void loadMore() {
            // 加载数据
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadMoreData();
                    mPullLoadRecyclerView.setLoadMoreCompleted();
                }
            },LOADMORE_DURATION);
        }
    }

    class WorksListAdapter extends RecyclerView.Adapter{


        private List<WorksItemModule> mWorksItemModuleList = new ArrayList<>();
        private Context mContext;
        private int columns = 3;
        public WorksListAdapter(Context context) {
            this.mContext = context ;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = ((Activity)mContext).getLayoutInflater().inflate(R.layout.item_wotks,null);
            WorksListAdapter.ItemViewHolder itemViewHolder = new WorksListAdapter.ItemViewHolder(view);
            view.setTag(itemViewHolder);
            return itemViewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(mWorksItemModuleList != null && mWorksItemModuleList.size() == 0)
                return ;
            final WorksItemModule worksItemModule = getItem(position);
            if(holder instanceof WorksListAdapter.ItemViewHolder){
                WorksListAdapter.ItemViewHolder itemViewHolder = (WorksListAdapter.ItemViewHolder) holder;
                itemViewHolder.mNum.setText(worksItemModule.getLikeNum()+"");

                ImgUtils.loadRound(R.mipmap.dislike, itemViewHolder.mTypeIconImageView);
                ImgUtils.load(worksItemModule.getCover(),itemViewHolder.mCoverImageView);

                Point point = ImgUtils.getVerPostSize(mContext,columns);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(point.x,point.y);
                itemViewHolder.mCoverImageView.setLayoutParams(params);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
            super.onBindViewHolder(holder, position, payloads);
        }

        @Override
        public int getItemCount() {
            if(mWorksItemModuleList != null&& mWorksItemModuleList.size()>0)
                return mWorksItemModuleList.size();
            return 0;
        }

        public void setData(WorksItemModule worksItemModule){
            mWorksItemModuleList.add(worksItemModule);
        }

        private  WorksItemModule getItem(int position)
        {
            return mWorksItemModuleList.get(position);
        }

        public void setColumns(int columns) {
            this.columns = columns;
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder{
            private RelativeLayout container;
            private ImageView mCoverImageView;
            private ImageView mTypeIconImageView ;
            private TextView mNum ;
            public ItemViewHolder(View view)
            {
                super(view);
                container = ViewUtils.bindViewId(view,R.id.container);
                mCoverImageView = ViewUtils.bindViewId(view,R.id.cover_iv);
                mTypeIconImageView = ViewUtils.bindViewId(view,R.id.typeicon_iv);
                mNum = ViewUtils.bindViewId(view,R.id.typenum_tv);
            }
        }
    }
}

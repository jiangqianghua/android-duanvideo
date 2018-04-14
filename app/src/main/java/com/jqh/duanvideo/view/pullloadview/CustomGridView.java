package com.jqh.duanvideo.view.pullloadview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangqianghua on 18/1/21.
 */

public class CustomGridView extends GridView {

    public interface OnLoadMoreListener{
        void onLoadMoreItems();
    }

    public interface OnScrollListener{
        void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount);
    }

    private OnLoadMoreListener mOnLoadMoreListener ;
    private OnScrollListener mOnScrollListener;

    public OnLoadMoreListener getOnLoadMoreListener() {
        return mOnLoadMoreListener;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    public OnScrollListener getOnScrollListener() {
        return mOnScrollListener;
    }

    public void setOnScrollListener(OnScrollListener mOnScrollListener) {
        this.mOnScrollListener = mOnScrollListener;
    }

    private Context mContext ;

    private List<ViewHolder> mFooterViewList = new ArrayList<>();

    private boolean isLoading ;

    private boolean mHasMoreItem;

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public boolean isHasMoreItem() {
        return mHasMoreItem;
    }

    public void setHasMoreItem(boolean hasMoreItem) {
        this.mHasMoreItem = hasMoreItem;
    }

    public CustomGridView(Context context) {
        super(context);
        initView(context);
    }

    public CustomGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public CustomGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context){
        mContext = context ;
        isLoading = false ;
        LoadingView loadingView = new LoadingView(context);
        addFooterView(loadingView);
        this.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(mOnScrollListener != null){
                    mOnScrollListener.onScroll(view,firstVisibleItem,visibleItemCount,totalItemCount);
                }
                if(totalItemCount > 0){
                    // 可见范围条目
                    int lastViewVisible = firstVisibleItem + visibleItemCount ;
                    if(!isLoading && mHasMoreItem && lastViewVisible == totalItemCount){
                        if(mOnLoadMoreListener != null){
                            mOnLoadMoreListener.onLoadMoreItems();
                        }
                    }
                }
            }
        });
    }

    public void addFooterView(View view,Object data,boolean isSelect){
        ViewHolder holder = new ViewHolder();
        FrameLayout fl= new FullWidthViewLayout(mContext);
        fl.addView(view);
        holder.view = view ;
        holder.data = data ;
        holder.viewContainer = fl ;
        holder.isSelected = isSelect ;
        mFooterViewList.add(holder);
    }

    public void addFooterView(View view){
        addFooterView(view,null,true);
    }

    public void removeFooterView(View v){
        if(mFooterViewList.size() > 0){
            removeHolder(v,mFooterViewList);
        }
    }

    private void removeHolder(View view,List<ViewHolder> list){
        for(int i = 0 ; i < list.size() ; i++){
            ViewHolder holder = list.get(i);
            if(holder.view == view){
                list.remove(i);
                break;
            }
        }
    }

    public void notifyChanged(){
        this.requestLayout();
        this.invalidate();
    }

    class ViewHolder{
        public View view ;
        public ViewGroup viewContainer;
        public Object data ;
        public boolean isSelected ;
    }

    class FullWidthViewLayout extends FrameLayout{
        public FullWidthViewLayout(Context context) {
            super(context);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int targetWidth = this.getWidth() - this.getPaddingLeft() - this.getPaddingRight() ;
            MeasureSpec.makeMeasureSpec(targetWidth, MeasureSpec.getMode(widthMeasureSpec));

        }
    }
}

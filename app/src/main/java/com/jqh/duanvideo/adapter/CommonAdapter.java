package com.jqh.duanvideo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 通用的适配器
 * @author user
 *
 * @param <T>
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

	protected Context mContext ;
	protected List<T> mDatas ;
	protected LayoutInflater mInflater ;
	private int layoutId ; 
	public CommonAdapter(Context context , List<T> datas , int layoutId) {
		// TODO Auto-generated constructor stub
		this.mContext = context ;
		mDatas = datas ;
		mInflater = LayoutInflater.from(context);
		this.layoutId = layoutId ;
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(mDatas == null)
			return 0 ;
		return mDatas.size();
	}

	@Override
	public T getItem(int position) {
		// TODO Auto-generated method stub
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, layoutId, position); 
		convert(viewHolder , getItem(position));
		return viewHolder.getmConvertView();
	}
	
	public abstract void convert(ViewHolder holder , T t);

}

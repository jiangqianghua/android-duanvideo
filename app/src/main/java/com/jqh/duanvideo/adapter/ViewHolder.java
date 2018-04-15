package com.jqh.duanvideo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jqh.duanvideo.utils.ImgUtils;

/**
 * 通用的ViewHolder
 * @author user
 */
public class ViewHolder {

	private SparseArray<View> mViews ;
	private int mPosition ; 
	private View mConvertView ;
	public ViewHolder(Context context , ViewGroup parent , int layoutId , int position)
	{
		this.mPosition = position ;
		this.mViews = new SparseArray<View>() ;
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent , false);
		mConvertView.setTag(this);
	}
	
	public static ViewHolder get(Context context , View convertView, ViewGroup parent , int layoutId , int position)
	{
		if(convertView == null)
		{
			return new ViewHolder(context , parent,layoutId,position);
		}
		else
		{
			ViewHolder holder = (ViewHolder) convertView.getTag() ;
			holder.mPosition = position ;
			return holder ;
		}
	}

	public View getmConvertView() {
		return mConvertView;
	}

	public void setmConvertView(View mConvertView) {
		this.mConvertView = mConvertView;
	}
	/**
	 * 通过viewId获取控件
	 * @param viewId
	 * @return
	 */
	public<T extends View> T getView(int viewId)
	{
		View view = mViews.get(viewId);
		if(view == null)
		{
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		
		return (T)view ;
	}
	
	/**
	 * 设置textview值
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setText(int viewId , String text)
	{
		TextView tv = 	getView(viewId);
		tv.setText(text);
		return this ;
	}
	
	/**
	 * 设置imageView
	 * @param viewId
	 * @param resId
	 * @return
	 */
	public ViewHolder setImageResource(int viewId , int resId)
	{
		ImageView view = getView(viewId);
		view.setImageResource(resId);
	//	UIUtils.loadDrawableImage(resId,view);
		return this ;
	}
	
	/**
	 * 设置imageView
	 * @param viewId
	 * @return
	 */
	public ViewHolder setImageBitmap(int viewId , Bitmap bitmap)
	{
		ImageView view = getView(viewId);
		view.setImageBitmap(bitmap);
		return this ;
	}

	/**
	 * 设置imageView
	 * @param viewId
	 * @param resId
	 * @return
	 */
	public ViewHolder setImageUrl(int viewId , String Url)
	{
		ImageView view = getView(viewId);
		//view.setImage
		// Imageloader.getInstance().loadImage(view url);
		return this ;
	}

	public ViewHolder setRoundImage(int viewId,String url){
		ImageView view = getView(viewId);
		ImgUtils.loadRound(url,view);
		return this;
	}
	
	public ViewHolder setVisibility(int viewId,int visibility)
	{
		View view = getView(viewId);
		view.setVisibility(visibility);
		return this ;
	}
}

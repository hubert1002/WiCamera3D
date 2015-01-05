package com.wistron.StereoUI;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Utilities.CSStaticData;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.TextView;
import android.widget.Gallery.LayoutParams;

/**
 * Copyright(c)2011 Wistron SWPC ALL rights reserved. 
 * @date: 2011-12-22 09:15:07
 * @author :WH1107028
 * @purpose:立体控件のToast
 */
public class CSGalleryAdapter extends BaseAdapter{
	private static final String TAG = "CSGalleryAdapter";
	private HashMap<Integer, Size>
	                   mOrgViewSizeList    = null;
	private List<View> mViewList           = null;
	private Context    mContext            = null;
	private TextView   mNonItem            = null;
	private boolean    mDefaultItemEnable  = false;
	private boolean    mAutoAdjustEnable   = true; 
	private boolean    mDimension          = false;
	private Animation  mSelectedAnimation  = null;
	private Animation  mClickedAnimation   = null;
	private Animation  mLostFocusAnimation = null;
	private int        mCurrentChild       = 0;      //the position of child who called by getView
	private int        mScrWidth           = 0,
			           mScrHeight          = 0;
	private float      mScaleRateWidth     = 1f,
					   mScaleRateHeight    = 1f;
	

	public CSGalleryAdapter(Context context) {
		mContext         = context;
		mDimension       = false;
		mViewList        = new ArrayList<View>();
		mOrgViewSizeList = new HashMap<Integer,Size>();
		
		//预设控件自适应所使用的显示参数
		initDisplay();
		
		//初始化一个默认元素
		TextView nonItem = new TextView(mContext);
		nonItem.setText("还没有添加元素...");
		nonItem.setTextSize(22);
		nonItem.setGravity(Gravity.CENTER);
		nonItem.setTextColor(Color.parseColor("#ff0000"));
		nonItem.setBackgroundResource(android.R.drawable.toast_frame);
		nonItem.setLayoutParams(new Gallery.LayoutParams(200, 200));
		mNonItem = nonItem;
	}
	
	public int addView(View item){
		int                  currentPosition = 0;
		Size                 curSize         = null;
		Gallery.LayoutParams lParams         = null;
		
		if(mViewList == null){
			mViewList = new ArrayList<View>();
		}
		if(mOrgViewSizeList == null){
			mOrgViewSizeList = new HashMap<Integer,Size>();
		}
		
		//保存原始宽高信息
		lParams = (Gallery.LayoutParams) item.getLayoutParams();
		if(lParams != null){
			curSize = new Size(lParams.width, lParams.height);
		}else{
			curSize = new Size(LayoutParams.WRAP_CONTENT , LayoutParams.WRAP_CONTENT);
		}
		mOrgViewSizeList.put(mViewList.size() - 1, curSize);
		//添加进元素列表
		mViewList.add(item);
		if(lParams != null){
			//计算自适应
			if (CSStaticData.DEBUG)
				Log.w(TAG,"[addView]元素 " + mViewList.size() + " 开始自适应");
			if(mAutoAdjustEnable){
				if(mScaleRateWidth != 1f)
					curSize.setWidth((int)(curSize.getWidth()*mScaleRateWidth));
				if(mScaleRateHeight != 1f)
					curSize.setHeight((int)(curSize.getHeight()*mScaleRateHeight));
			}
			//宽度减半
			if(mDimension){
				if (CSStaticData.DEBUG)
					Log.w(TAG,"[addView]元素 " + mViewList.size() + " 开始3D模式调整");
				curSize.setWidth(curSize.getWidth()/2);
			}
			//设定新的宽高
			item.setLayoutParams(new Gallery.LayoutParams(curSize.getWidth(), curSize.getHeight()));
		}
		//准备函数返回值
		currentPosition = mViewList.indexOf(item); //替换成mViewList.size() - 1效率会更高，但这里要确保一个验证工作
		
		notifyDataSetChanged();
		return currentPosition;
	}
	
	public View remove(int position) {
		View removedItem = null;
		
		if(mViewList != null){
			if(position >= 0 || position < mViewList.size()){
				removedItem = mViewList.remove(position);
			}
			if(position >= 0 || position < mOrgViewSizeList.size()){
				mOrgViewSizeList.remove(position);
			}
		}

		notifyDataSetChanged();
		return removedItem;
	}
	
	public View removeLast() {
		View removedItem = null;
		
		if(mViewList != null && mViewList.size() > 0){
			removedItem = mViewList.remove(mViewList.size()-1);
		}
		if(mOrgViewSizeList != null && mOrgViewSizeList.size() > 0){
			mOrgViewSizeList.remove(mOrgViewSizeList.size()-1);
		}
		
		notifyDataSetChanged(); 
		return removedItem;
	}
	
	/**
	 * 设定是否开启自带的默认元素
	 * @param enable
	 */
	public void setDefaultItemEnable(boolean enable){
		mDefaultItemEnable = enable;
	}
	
	/**
	 * 设定是否打开元素的自适应
	 * 此函数要在addView前调用
	 * @param enable
	 */
	public void setAutoAdjustEnable(boolean enable){
		mAutoAdjustEnable = enable;
	}
	
	/**
	 * @deprecated
	 * 建议Activity层不要使用这个方法
	 * 这个方法是给CSGallery调用的
	 * @param is3D
	 */
	public void setDimension(boolean is3D){
		//仅把mOrgViewSizeList和mViewList中的宽高更改就行了
		//step 1：获取当前getView读取到的position ===> mCurrentChild
		//step 2：从这个位置开始依次对mCurrentChild - 1, mCurrentChild + 1,mCurrentChild - 2, mCurrentChild + 2 ...,0 , max做自适应修改
		boolean isPrevDone = false,            //向前扫描OK
				isNextDone = false;            //向后扫描OK
		int     viewCount  = mViewList.size(); //元素的个数 
		Size    curSize    = null;             //元素宽高（临时）
		
		mDimension = is3D;
		
		if(mDimension != is3D){
			int i = mCurrentChild,
				j = mCurrentChild + 1;
			
			if(viewCount == 0){
				return;
			}
			do{
				if(i >= 0){
					//TODO 计算自适应，可能会是耗时操作，要注意
					curSize   = new Size();
					curSize   = mOrgViewSizeList.get(i);
					if(curSize.getHeight() >= 0){
						if (CSStaticData.DEBUG)
							Log.w(TAG,"[setDimension]元素 " + i + " 开始自适应");
						//STEP 1:宽高自适应
						if(mAutoAdjustEnable){
							if(mScaleRateWidth != 1f)
								curSize.setWidth((int)(curSize.getWidth()*mScaleRateWidth));
							if(mScaleRateHeight != 1f)
								curSize.setHeight((int)(curSize.getHeight()*mScaleRateHeight));
						}
						//STEP 2:宽度减半
						if(mDimension){
							if (CSStaticData.DEBUG)
								Log.w(TAG,"[setDimension]元素 " + i + " 开始3D模式调整");
							curSize.setWidth(curSize.getWidth()/2);
						}else{
							if (CSStaticData.DEBUG)
								Log.w(TAG,"[setDimension]元素 " + i + " 取消3D模式调整");
							curSize.setWidth(curSize.getWidth());
						}
						//STEP 3:设定新的宽高
						mViewList.get(i).setLayoutParams(new Gallery.LayoutParams(curSize.getWidth(), curSize.getHeight()));
					}
					//STEP 4:设定循环参数
					i--;
					isPrevDone = false;
				}else{
					isPrevDone = true;
				}
				if(j < viewCount){//如果是耗时操作，这里的viewCount要换成mViewList.size()来实时扫描size，才安全
					//TODO 计算自适应，可能会是耗时操作，要注意
					//STEP 1:宽高自适应
					curSize   = new Size();
					curSize   = mOrgViewSizeList.get(j);
					if(curSize.getHeight() >= 0){
						if (CSStaticData.DEBUG)
							Log.w(TAG,"[setDimension]元素 " + j + " 开始自适应");
						//STEP 1:宽高自适应
						if(mAutoAdjustEnable){
							if(mScaleRateWidth != 1f)
								curSize.setWidth((int)(curSize.getWidth()*mScaleRateWidth));
							if(mScaleRateHeight != 1f)
								curSize.setHeight((int)(curSize.getHeight()*mScaleRateHeight));
						}
						//STEP 2:宽度减半
						if(mDimension){
							if (CSStaticData.DEBUG)
								Log.w(TAG,"[setDimension]元素 " + j + " 开始3D模式调整");
							curSize.setWidth(curSize.getWidth()/2);
						}else{
							if (CSStaticData.DEBUG)
								Log.w(TAG,"[setDimension]元素 " + i + " 取消3D模式调整");
							curSize.setWidth(curSize.getWidth());
						}
						//STEP 3:设定新的宽高
						mViewList.get(j).setLayoutParams(new Gallery.LayoutParams(curSize.getWidth(), curSize.getHeight()));
					}
					//STEP 4:设定循环参数
					j++;
					isNextDone = false;
				}else{
					isNextDone = true;
				}
			}while(!(isPrevDone && isNextDone));
		}
		
		notifyDataSetChanged();
	}
	
	/**
	 * 设定元素被点中后的动画
	 * @param anim
	 */
	@Deprecated
	public void setClickedAnimation(Animation anim){
		
	}
	
	/**
	 * 设定元素被选中后的动画
	 * @param anim
	 */
	@Deprecated
	public void setSelectedAnimation(Animation anim){
		
	}

	/**
	 * 设定元素失去点子焦点后的动画
	 * @param anim
	 */
	@Deprecated
	public void setLostFocusAnimation(Animation anim){
		
	}
	
	public CSGalleryAdapter getAdapter(){
		return this;
	}
	
	/**
	 * 检查是否开启了自带的默认元素
	 * @return
	 */
	public boolean getDefaultItemEnable(){
		return mDefaultItemEnable;
	}
	
	public boolean getDimension(){
		return mDimension;
	}
	
	public int getCount() {
		int count = 0;
		
		if(mViewList != null){
			count = mViewList.size();
		}
		
		if(count == 0){
			if(mDefaultItemEnable){
				mViewList.add(mNonItem);
				count = mViewList.size();
			}
		}else{
			if(mViewList.indexOf(mNonItem) != -1);
				mViewList.remove(mNonItem);
		}
		
		return count;
	}

	public Object getItem(int position) {
		View item = null;
		
		if(mViewList != null){
			item = mViewList.get(position);
		}
		
		return item;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (CSStaticData.DEBUG)
			Log.d(TAG, "[getView]获取元素 " + position + "的内容");
		View item      = null;
		int  direction = 0;    //获取元素移动的方向： = 0 未移动，  > 0 向右移动， < 0 向左移动
		
		//保存当前将要显示的Child position
		direction = position - mCurrentChild;
		mCurrentChild = position;
		
		//获取View
//		if(convertView == null){
			if(mViewList != null && mViewList.size() != 0){ 
				item = mViewList.get(position);
			}else{
				//当ChildView被删光时，才会调这里
				item = mNonItem;
			}
//		}else{
//			item = convertView;
//		}
		
		//添加动画
		if(direction < 0){

		}
		if(direction > 0){
			
		}
		
		return item;
	}
	
	private void initDisplay(){
		Class<?> cint  = null; //类的实例
		Object   obj   = null; //类的对象
		Field    field = null; //状态栏高度的变量
		int      objid = 0,    //资源标识符
				 sbarh = 0;    //状态栏高度
		
		//获取状态栏高度
		try {
			if(android.os.Build.VERSION.SDK_INT >= 11){
				//仅对有状态栏的版本使用此方法
				cint  = Class.forName("com.android.internal.R$dimen");
				obj   = cint.newInstance();
				field = cint.getField("status_bar_height");
				objid = Integer.parseInt(field.get(obj).toString());
				sbarh = mContext.getResources().getDimensionPixelSize(objid);
			}else{
				sbarh = 0;
			}
			
			if (CSStaticData.DEBUG)
				Log.e("CSView","[CSView]状态栏高度 = " + sbarh);
		} catch (Exception e1) {
			if (CSStaticData.DEBUG)
				Log.e("CSView","[CSView]获取状态栏高度失败");
			e1.printStackTrace();
		}  
		
		//获取屏幕宽度和高度
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		if(wm != null){
			mScrWidth  = wm.getDefaultDisplay().getWidth();
			mScrHeight = wm.getDefaultDisplay().getHeight();
		}
		
		//比较获取的屏幕高度是否已经除除了状态栏高度
		if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			if (CSStaticData.DEBUG)
				Log.i("CSView", "[CSView]显示模式：横屏");
			if(mScrHeight % 100 == 0){
				mScrHeight -= sbarh;
			}
		}
		else if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			if (CSStaticData.DEBUG)
				Log.i("CSView", "[CSView]显示模式：竖屏");
			if(mScrHeight % 10 == 0){
				mScrHeight -= sbarh;
			}
		}
		
		//保存压缩比率
		mScaleRateWidth  = (float)mScrWidth/1024f;
		mScaleRateHeight = (float)mScrHeight/600f;
	}
	
	
	/**
	 * 
	 * @author WH1107028
	 * @description 保存宽高的类
	 */
	public class Size{
		private int mWidth  = 0,
			        mHeight = 0;
		
		public Size() {
			
		}
		
		public Size(int width, int height) {
			mWidth  = width;
			mHeight = height;
		}
		
		public int getWidth(){
			return mWidth;
		}
		
		public int getHeight(){
			return mHeight;
		}
		
		public void setWidth(int width){
			mWidth = width;
		}
		
		public void setHeight(int height){
			mHeight = height;
		}
	}
}

package com.wistron.StereoUI;

import java.util.ArrayList;
import java.util.List;
import com.wistron.swpc.wicamera3dii.R;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * Copyright (c) 2011 Wistron SWPC All rights reserved. created: 2012/03/22
 * filename: ShareMenu.java
 * 
 * @author WangWei purpose:分享菜单
 */
public class ShareMenu extends View implements OnItemClickListener {

	private Context 												mContext = null;
	private ListView 												mListView = null;
	private ListAppAdapter                                          mListAppAdapter = null;
	private TextView 												mTitle = null;
	private List<ResolveInfo> 										mApps = null;
	private RelativeLayout 											mMainLayout = null;
	private ArrayList<String> 										mImageFilePath = null,
																	mVideoFilePath = null;
	private RelativeLayout.LayoutParams 							mMainLayoutParams = null;
	private ViewGroup                                               mParentLayout = null;                              
	private MsgBox                                                  msgBox = null;

	private int 													mShareMenuX = 0,
																	mShareMenuY = 0, 
																	mShareMenuWidth = 0,
																	mShareMenuHeight = 0,
																	mShareMenuAnchor = 0,
																	mTitleHeight = 45,
																	mListItemHeight = 60,
																	mListSelector = 0,
																	mListItemSelector = 0,
																	mListLayoutSelector = 0,
																	mIconWidth = 0,
																	mIconHeight = 0,
																	mTextSize = 18,
																	mTextColor = Color.WHITE, mListSize = 0;

	private boolean 												mAllowHide = true,
																	mIsShow = false,
																	mIsMenuTouched = false;

	private static final String 									SHARE_IMAGE = "Share image", 
																	SHARE_VIDEO = "Share video",
																	SHARE_VIDEO_AND_IMAGE = "Share video and image", 
																	TAG = "ShareMenu";

	public static final int 										ANCHOR_TOP_LEFT = 0x11, 
																	ANCHOR_BUTTOM_LEFT = 0x12,
																	ANCHOR_CENTER = 0x13,
																	ANCHOR_TOP_RIGHT = 0x14,
																	ANCHOR_BUTTOM_RIGHT = 0x15,
																	TYPE_ONE_BUTTON = 0x16, 
																	TYPE_TWO_BUTTON = 0x17,
																	TYPE_THREE_BUTTON = 0x18;

	/**
	 * 初始化ShareMenu
	 * 
	 * @param context
	 *            :getBaseContext
	 * @param location
	 *            :弹出的位置
	 * @param x
	 *            :x坐标
	 * @param y
	 *            :y坐标
	 */
	public ShareMenu(Context context, int x, int y) {
		super(context);
		mContext = context;
		mShareMenuX = x;
		mShareMenuY = y;

		mImageFilePath = new ArrayList<String>();
		mVideoFilePath = new ArrayList<String>();
		mApps = new ArrayList<ResolveInfo>();

		mMainLayout = new RelativeLayout(mContext);
		mListView = new ListView(mContext);
		mTitle = new TextView(mContext);
	
		
		mListAppAdapter = new ListAppAdapter();
		initData();
		
	

	}
	
	public void setLocation(int x,int y,int anchor){
	 mShareMenuX = x;
	 mShareMenuY = y;
	 setAnchor(anchor);
	 setMargin(mShareMenuX, mShareMenuY, 0, 0);
	 
 }
 
	public void setMargin(int left,int top,int right,int bottom){
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mShareMenuWidth, mShareMenuHeight);
		layoutParams.setMargins(left, top, right, bottom);
		mMainLayout.setLayoutParams(layoutParams);
		mMainLayout.invalidate();
		
	}
	
	public void setShareLayoutParams(RelativeLayout.LayoutParams layoutParams) {
		// TODO Auto-generated method stub
		mMainLayoutParams = layoutParams;
	}
	
	public void setLayoutParams(RelativeLayout.LayoutParams layoutParams) {
		// TODO Auto-generated method stub
		mMainLayout.setLayoutParams(layoutParams);
	}
	private void initData() {

		// 获取屏幕的宽高

		mMainLayout.setPadding(5, 5, 5, 5);
		mShareMenuWidth = 300;
		mShareMenuHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
		mMainLayoutParams = new RelativeLayout.LayoutParams(mShareMenuWidth, mShareMenuHeight);
		mMainLayoutParams.setMargins(mShareMenuX, mShareMenuY, 0, 0);
//		mMainLayout.setBackgroundResource(R.drawable.main_menu);

		
	 
		mListView.setFadingEdgeLength(0);
		mListView.setScrollingCacheEnabled(false);
		mListView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				mShareMenuHeight));
		mListView.setBackgroundResource(R.drawable.main_menu);
		mListView.setPadding(10, 45, 10, 15);
		
		mTitle.setText("Share");
		mTitle.setTextSize(mTextSize);
		mTitle.setTextColor(mTextColor);
		mTitle.setGravity(Gravity.CENTER);
		mTitle.setId(0x1110);
		
		mTitle.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mTitleHeight));

		mIconWidth = 55;
		mIconHeight = 55;
		
		mIsMenuTouched = false;
		mMainLayout.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				mIsMenuTouched = true;
				return mIsMenuTouched;
			}
		});

	}

	public void setBackground(int resid) {
		mMainLayout.setBackgroundResource(resid);
	}

	public ArrayList<String> getImagePath() {
		return mImageFilePath;
	}

	public void setImagePath(ArrayList<String> filepath_image) {
		this.mImageFilePath = filepath_image;
	}

	public ArrayList<String> getVideoPath() {
		return mVideoFilePath;
	}

	public void setVideoPath(ArrayList<String> filepath_video) {
		this.mVideoFilePath = filepath_video;
	}
	/**
	 * 将CSShareMenu添加到布局中
	 * 
	 * @param layout
	 *            :父布局
	 */
	public void addToLayout(ViewGroup layout) {

		mParentLayout = layout;
		// 若图片和视频路径都为空，弹出提示框
		if (mImageFilePath.size() == 0 && mVideoFilePath.size() == 0) {

			msgBox = new MsgBox(mContext);
			msgBox.setMessage("image or video not found,please make sure the image or video is exist!");
			msgBox.setPositiveButton("OK", new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					msgBox.hide();
				}
			});

			msgBox.addToLayout(layout);
			msgBox.show();
			mMainLayout.setVisibility(View.GONE);

		} else {
			loadApps();
			mListSize = mApps.size();
			Log.w(TAG, "mApps.size()" + mApps.size());

		}

//		Log.w(TAG, "mImageFilePath" + mImageFilePath.get(0) + "mImageFilePath.size()" + mImageFilePath.size());

		mListView.setAdapter(mListAppAdapter);
		mListView.setOnItemClickListener(this);
//		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//				ViewGroup.LayoutParams.WRAP_CONTENT);
//		layoutParams.addRule(RelativeLayout.BELOW, 0x1110);
		
		mMainLayout.addView(mListView);
		mMainLayout.addView(mTitle);

		layout.addView(mMainLayout, mMainLayoutParams);

	}

	/**
	 * 加载可接受图片或视频的应用
	 */
	private void loadApps() {

		Intent intent = new Intent();
		List<ResolveInfo> mApps_image = new ArrayList<ResolveInfo>();
		List<ResolveInfo> mApps_video = new ArrayList<ResolveInfo>();

		if (mImageFilePath.size() > 1 || mVideoFilePath.size() > 1
				|| (mImageFilePath.size() != 0 && mVideoFilePath.size() != 0)) {
			intent.setAction(Intent.ACTION_SEND_MULTIPLE);
		} else {
			intent.setAction(Intent.ACTION_SEND);
		}

		if (mImageFilePath.size() != 0 && mVideoFilePath.size() == 0) {

			intent.setType("image/*");
			mApps_image = mContext.getPackageManager().queryIntentActivities(intent, 0);
			mApps = mApps_image;

		} else if (mImageFilePath.size() == 0 && mVideoFilePath.size() != 0) {

			intent.setType("video/*");
			mApps_video = mContext.getPackageManager().queryIntentActivities(intent, 0);
			mApps = mApps_video;

		} else if (mImageFilePath.size() != 0 && mVideoFilePath.size() != 0) {

			intent.setType("image/*");
			mApps_image = mContext.getPackageManager().queryIntentActivities(intent, 0);

			intent.setType("video/*");
			mApps_video = mContext.getPackageManager().queryIntentActivities(intent, 0);

			if (mApps_image.size() != 0 && mApps_video.size() != 0) {
				int maxSize = mApps_image.size() > mApps_video.size() ? mApps_image.size() : mApps_video.size();
				for (int i = 0; i < maxSize; i++) {
					if (mApps_image.size() == maxSize) {
						if (mApps_video.contains(mApps_image.get(i))){
							mApps.add(mApps_image.get(i));
						}
						
					} else {
						if (mApps_image.contains(mApps_video.get(i))){
							mApps.add(mApps_video.get(i));
						}
						
					}
				}
			}

		}
	}

	/**
	 * ListAppAdapter继承BaseAdapter
	 * 
	 */
	public class ListAppAdapter extends BaseAdapter {

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {

				holder = new ViewHolder();
				convertView = holder.mListItemLayout;
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			ResolveInfo info = mApps.get(position);
			holder.mIcon.setImageDrawable(info.activityInfo.loadIcon(mContext.getPackageManager()));
			holder.mAppName.setText(info.activityInfo.loadLabel(mContext.getPackageManager()));
			return convertView;

		}

		public final int getCount() {
			return mApps.size();
		}

		public final Object getItem(int position) {
			return mApps.get(position);
		}

		public final long getItemId(int position) {
			return position;
		}
	}

	/**
	 * 管理listItem的类
	 * 
	 * @author WH1107017
	 * 
	 */
	class ViewHolder {
		private ImageView mIcon;
		private TextView mAppName;
		private RelativeLayout mListItemLayout;
		private RelativeLayout.LayoutParams mLayoutParams;

		public ViewHolder() {
			mListItemLayout = new RelativeLayout(mContext);
			mListItemLayout.setBackgroundResource(R.drawable.camera_menu_selecter);
			mAppName = new TextView(mContext);
			mAppName.setTextSize(mTextSize);
			mAppName.setGravity(Gravity.CENTER_VERTICAL);
			mAppName.setTextColor(mTextColor);

			mIcon = new ImageView(mContext);
			mIcon.setId(0x1220);
			RelativeLayout.LayoutParams mIconLayoutParams = new RelativeLayout.LayoutParams(
					mIconWidth, mIconHeight);
			mIconLayoutParams.setMargins(15, 0, 15, 0);
			mIconLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);

			mLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, mListItemHeight);
			mLayoutParams.addRule(RelativeLayout.RIGHT_OF, 0x1220);

			mListItemLayout.addView(mIcon, mIconLayoutParams);
			mListItemLayout.addView(mAppName, mLayoutParams);
			mListItemLayout.setGravity(Gravity.CENTER_VERTICAL);
		}
	}

	/**
	 * 应用列表的点击事件
	 * 
	 */
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		ResolveInfo info = mApps.get(position);
		Intent intent = new Intent();

		ArrayList<Uri> aFileList = new ArrayList<Uri>();
		if (mImageFilePath.size() > 1 || mVideoFilePath.size() > 1
				|| (mImageFilePath.size() != 0 && mVideoFilePath.size() != 0)) {
			intent.setAction(Intent.ACTION_SEND_MULTIPLE);
			if (mImageFilePath.size() >= 1 && mVideoFilePath.size() >= 1) {

				intent.setType("*/*");
				intent.putExtra(Intent.EXTRA_SUBJECT, SHARE_VIDEO_AND_IMAGE);
				for (int i = 0; i < mImageFilePath.size(); i++) {
					aFileList.add(Uri.parse(mImageFilePath.get(i)));
				}
				for (int j = 0; j < mVideoFilePath.size(); j++) {
					aFileList.add(Uri.parse(mVideoFilePath.get(j)));
				}
			} else if (mImageFilePath.size() > 1 && mVideoFilePath.size() == 0) {
				intent.setType("image/*");
				intent.putExtra(Intent.EXTRA_SUBJECT, SHARE_IMAGE);
				for (int i = 0; i < mImageFilePath.size(); i++) {
					aFileList.add(Uri.parse(mImageFilePath.get(i)));
				}
			} else if (mImageFilePath.size() == 0 && mVideoFilePath.size() > 1) {
				intent.setType("video/*");
				intent.putExtra(Intent.EXTRA_SUBJECT, SHARE_VIDEO);
				for (int i = 0; i < mVideoFilePath.size(); i++) {
					aFileList.add(Uri.parse(mVideoFilePath.get(i)));
				}
			}

			intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, aFileList);

		} else {
			intent.setAction(Intent.ACTION_SEND);
			if (mImageFilePath.size() == 1) {
				intent.setType("image/*");
				intent.putExtra(Intent.EXTRA_SUBJECT, SHARE_IMAGE);
				intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(mImageFilePath.get(0)));
			} else {
				intent.setType("video/*");
				intent.putExtra(Intent.EXTRA_SUBJECT, SHARE_VIDEO);
				intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(mVideoFilePath.get(0)));
			}
		}

		try {
			intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
			mContext.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		hide();
	}

	/**
	 * 分享菜单事件分发函数
	 * 
	 * @param event
	 * @return 如果down事件是在菜单上，则返回true，反之菜单隐藏并返回false
	 */
/*	public boolean dispatchTouchEvent(MotionEvent event) {

		mListView.dispatchTouchEvent(event);

		if (event.getRawX() > mShareMenuX && event.getRawX() < mShareMenuX + mShareMenuWidth
				&& event.getRawY() > mShareMenuY
				&& event.getRawY() < mShareMenuY + mListItemHeight * mListSize + mTitleHeight) {

			try {
				mIsMenuTouched |= mListView.dispatchTouchEvent(event);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (mMainLayout.getVisibility() == View.VISIBLE) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					mAllowHide = false;
				}
				return true;
			} else {
				mAllowHide = true;
				return false;
			}
		} else {

			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				mMainLayout.setVisibility(View.INVISIBLE);
				mIsMenuTouched = false;
			}
			if (mMainLayout.getVisibility() == View.VISIBLE && event.getAction() == MotionEvent.ACTION_UP && mAllowHide) {
				mMainLayout.setVisibility(View.INVISIBLE);
				mIsMenuTouched = false;
			}
		}

		return mIsMenuTouched;

	}*/

	/**
	 * 判断分享菜单是否被点击, 如果down事件是在菜单上，则返回true，反之菜单隐藏并返回false
	 * 
	 * @return
	 */
	public boolean getIsShareMenuTouch() {
		return mIsMenuTouched;
	}

	public void setIsShareMenuTouch(boolean b){
		mIsMenuTouched = b;
		
	}
	/**
	 * 判断分享菜单是否显示
	 * 
	 * @return
	 */
	public boolean isShown() {

		return mIsShow;
	}

	/**
	 * 显示分享菜单
	 * 
	 * @return
	 */
	public boolean show() {
		if (mMainLayout != null) {
			mMainLayout.setVisibility(View.VISIBLE);
			mListView.setAdapter(mListAppAdapter);
		}
		mIsShow = true;
		return mIsShow;
	}

	/**
	 * 隐藏分享菜单
	 * 
	 * @return
	 */
	public boolean hideAndRemove() {
		if (mMainLayout != null) {
			mMainLayout.setVisibility(View.GONE);
			mParentLayout.removeView(mMainLayout);
		}
		mIsShow = false;
		return mIsShow;
	}

	public boolean hide(){
		if (mMainLayout != null) {
			mMainLayout.setVisibility(View.GONE);
		}
		mIsShow = false;
		return mIsShow;
	}
	/**
	 * 获取分享菜单的可见度
	 * 
	 * @return
	 */
	public int getVisibility() {
		if (mMainLayout == null) {
			return View.INVISIBLE;
		}
		return mMainLayout.getVisibility();
	}

	/**
	 * 设置分享菜单的可见度
	 * 
	 * @param visibility
	 */
	public void setVisibility(int visibility) {
		if (mMainLayout == null) {
			return;
		}
		mMainLayout.setVisibility(visibility);

	}

	/**
	 * 设置菜单是背景
	 * 
	 * @param resid
	 */
	public void setListViewSelector(int resid) {

		mListSelector = resid;
	}

	/**
	 * 设置菜单整个布局的背景
	 * 
	 * @param resid
	 */
	public void setListLayoutSelector(int resid) {

		mListLayoutSelector = resid;
	}

	/**
	 * 设置定位点
	 * 
	 * @param anchor
	 *            TOP_LEFT | TOP_RIGHT | BUTTOM_LEFT | BUTTOM_RIGHT | CENTER
	 */
	public void setAnchor(int anchor) {
		mShareMenuAnchor = anchor;
		switch (anchor) {

		
		case MsgBox.ANCHOR_TOP_LEFT:

			break;
		case MsgBox.ANCHOR_BUTTOM_LEFT:
			mShareMenuY = mShareMenuY - mShareMenuHeight;
			break;
		case MsgBox.ANCHOR_CENTER:
			mShareMenuX = mShareMenuX - mShareMenuWidth / 2;
			mShareMenuY = mShareMenuY - mShareMenuHeight / 2;
			break;
		case MsgBox.ANCHOR_TOP_RIGHT:
			mShareMenuX = mShareMenuX - mShareMenuWidth;

			break;
		case MsgBox.ANCHOR_BUTTOM_RIGHT:
			mShareMenuX = mShareMenuX - mShareMenuWidth;
			mShareMenuY = mShareMenuY - mShareMenuHeight;
			break;
		}
	}

	
	@Override
	public void setRotation(float rotation) {
		// TODO Auto-generated method stub
		
		mMainLayout.setRotation(rotation);
	}
	
	public void setPivotX(float pivotX){
		mMainLayout.setPivotX(pivotX);
	}
	
	public void setPivotY(float pivotY){
		mMainLayout.setPivotY(pivotY);
	}
	
	public void setTranslationX(float x){
		mMainLayout.setTranslationX(x);
	}
//	@Override
//	public boolean dispatchTouchEvent(MotionEvent event) {
//		// TODO Auto-generated method stub
//		
//		Log.e(TAG, "width:"+mMainLayout.getWidth()+"height:"+mMainLayout.getHeight());
//		Log.e(TAG, "x:"+mMainLayout.getX()+"Y:"+mMainLayout.getY());
//		boolean state = mMainLayout.dispatchTouchEvent(event);
//		Log.e(TAG, ""+state);
//		return state;
//	}

}

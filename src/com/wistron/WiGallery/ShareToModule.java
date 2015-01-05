package com.wistron.WiGallery;

import java.util.ArrayList;
import java.util.List;

import com.wistron.swpc.wicamera3dii.R;

import Utilities.CSStaticData;
import Utilities.CSStaticData.MEDIA_META_TYPE;
import Utilities.CSStaticData.SHARE_MULTI_SELECT_TYPE;
import Utilities.FileTypeHelper;
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
import android.view.ViewGroup.LayoutParams;
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
public class ShareToModule extends View implements OnItemClickListener {

	private Context 												mContext = null;
	private ListView 												mListView = null;
	private ListAppAdapter                                          mListAppAdapter = null;
	protected List<ResolveInfo>                                     mApps_image = new ArrayList<ResolveInfo>();
	protected List<ResolveInfo>                                     mApps_video = new ArrayList<ResolveInfo>();
	private TextView 												mTitle = null;
	private List<ResolveInfo> 										mApps = null;
	private RelativeLayout 											mMainLayout = null;
	private ArrayList<String> 										mImageFilePath = null,
																	mVideoFilePath = null;
	private RelativeLayout.LayoutParams 							mMainLayoutParams = null;

	private int 													mShareMenuX = 0,
																	mShareMenuY = 0, 
																	mShareMenuWidth = 141,
																	mShareMenuHeight = 0,
																	mShareMenuAnchor = 0,
																	mTitleHeight = 55,
																	mListItemHeight = 60,
																	mListSelector = 0,
																	mListItemSelector = 0,
																	mListLayoutSelector = 0,
																	mIconWidth = 0,
																	mIconHeight = 0,
																	mTextSize = 18,
																	mTextColor = Color.WHITE, mListSize = 0;

	private float 													mScreenWidth = 0,
																	mScreenHeight = 0;
	private boolean 												mAllowHide = true,
																	mIsShow = false,
																	mIsMenuTouched = false;
	private int                                                     mClickPosition;
	private OnClickListener                                         mOnClickListener;
	private OnShareToListener                                       mOnShareToListener;
	private MEDIA_META_TYPE                                         mCurMetaType = MEDIA_META_TYPE.ALL_MEDIA_TYPE;
	private SHARE_MULTI_SELECT_TYPE                                 mCurMultiSelectionType = SHARE_MULTI_SELECT_TYPE.NONE_LIMITE;
	
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
	public ShareToModule(Context context, int x, int y) {
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
	
	public void setLayoutParams(RelativeLayout.LayoutParams layoutParams) {
		// TODO Auto-generated method stub
		mMainLayoutParams = layoutParams;
	}
	private void initData() {

		// 获取屏幕的宽高
		WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		mScreenWidth = windowManager.getDefaultDisplay().getWidth();
		mScreenHeight = windowManager.getDefaultDisplay().getHeight();

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
		mListView.setPadding(10, 55, 10, 20);
		
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


	/**
	 * 将CSShareMenu添加到布局中
	 * 
	 * @param layout
	 *            :父布局
	 */
	public void addToLayout(ViewGroup layout) {
	
		loadApps(MEDIA_META_TYPE.ALL_MEDIA_TYPE);
		mListSize = mApps.size();
		if(CSStaticData.DEBUG){
			Log.w(TAG, "[addToLayout]找到 " + mApps.size() + " 个应用");
		}

//		Log.w(TAG, "mImageFilePath" + mImageFilePath.get(0) + "mImageFilePath.size()" + mImageFilePath.size());

		mListView.setAdapter(mListAppAdapter);
		mListView.setOnItemClickListener(this);
//		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//				ViewGroup.LayoutParams.MATCH_PARENT);
//		layoutParams.addRule(RelativeLayout.BELOW, 0x1110);
		
		mMainLayout.addView(mListView);
		mMainLayout.addView(mTitle);

		layout.addView(mMainLayout, mMainLayoutParams);

	}

	/**
	 * 加载可接受图片或视频的应用
	 */
	private void loadApps(MEDIA_META_TYPE metaType) {

		Intent intent = new Intent();
		mApps_image.clear();
		mApps_video.clear();
		
		if(mCurMultiSelectionType == SHARE_MULTI_SELECT_TYPE.NONE_LIMITE){
			//Do Nothing
			intent.setAction(Intent.ACTION_SEND);
			intent.setType("image/*");
			mApps_image = mContext.getPackageManager().queryIntentActivities(intent, 0);

			intent.setType("video/*");
			mApps_video = mContext.getPackageManager().queryIntentActivities(intent, 0);
			ArrayList<ResolveInfo> list_image = new ArrayList<ResolveInfo>(mApps_image);
			ArrayList<ResolveInfo> list_video = new ArrayList<ResolveInfo>(mApps_video);
			List<ResolveInfo> list_aciton_send =  unionApps(metaType,list_image,list_video);
			
//			intent.setAction(Intent.ACTION_SEND_MULTIPLE);
//			intent.setType("image/*");
//			mApps_image = mContext.getPackageManager().queryIntentActivities(intent, 0);
//
//			intent.setType("video/*");
//			mApps_video = mContext.getPackageManager().queryIntentActivities(intent, 0);
//			List<ResolveInfo> list_action_multipl = unionApps(metaType,mApps_image,mApps_video);
			mApps.clear();
//			mApps = unionApps(metaType, list_action_multipl, list_aciton_send);
			mApps = list_aciton_send;
			return;
			
		}
		if(mCurMultiSelectionType == SHARE_MULTI_SELECT_TYPE.SINGLE_SELECTION){
			intent.setAction(Intent.ACTION_SEND);
		}
		if(mCurMultiSelectionType == SHARE_MULTI_SELECT_TYPE.MULTI_SELECTION){
			intent.setAction(Intent.ACTION_SEND_MULTIPLE);
		}

		intent.setType("image/*");
		mApps_image = mContext.getPackageManager().queryIntentActivities(intent, 0);

		intent.setType("video/*");
		mApps_video = mContext.getPackageManager().queryIntentActivities(intent, 0);
		ArrayList<ResolveInfo> list_image = new ArrayList<ResolveInfo>(mApps_image);
		ArrayList<ResolveInfo> list_video = new ArrayList<ResolveInfo>(mApps_video);
		unionApps(metaType,list_image,list_video);

	}

	
	private List<ResolveInfo>  unionApps(MEDIA_META_TYPE metaType,List<ResolveInfo> list_image,List<ResolveInfo> list_video){
		List<ResolveInfo> mAppsList = new ArrayList<ResolveInfo>();
		if (list_image.size() != 0 && list_video.size() != 0) {
			int maxSize = list_image.size() > list_video.size() ? list_image.size() : list_video.size();
			int list_image_size = list_image.size();
			int list_video_size = list_video.size();
			if(metaType == MEDIA_META_TYPE.ALL_MEDIA_TYPE){
				
				if (list_image_size == maxSize) {
					for(int i = 0;i<list_image_size;i++){
						if(isContainInList(list_video, list_image.get(i))){
							list_image.remove(i);
							list_image_size--;
							i--;
						}
					}
				}else {
					for(int i = 0;i<list_video_size;i++){
						if(isContainInList(list_image, list_video.get(i))){
							list_video.remove(i);
							list_video_size--;
							i--;
						}
					}
				}
				
				
				mAppsList.addAll(list_image);
				mAppsList.addAll(list_video);
			}
			if(metaType == MEDIA_META_TYPE.NO_FILTER){
				for (int i = 0; i < maxSize; i++) {
					if (mApps_image.size() == maxSize) {
						if(isContainInList(mApps_image, mApps_video.get(i))){
							mApps_video.remove(mApps_video.get(i));
						}
					} else {
						if(isContainInList(mApps_video, mApps_image.get(i))){
							mApps_image.remove(mApps_image.get(i));
						}
					}
				}	
				
				mAppsList.addAll(mApps_image);
				mAppsList.addAll(mApps_video);
			}
			if(metaType == MEDIA_META_TYPE.IMAGE_MEDIA_TYPE){
				mAppsList = mApps_image;
				
			}
			if(metaType == MEDIA_META_TYPE.VIDEO_MEDIA_TYPE){
				mAppsList = mApps_video;
			}
		}
		
		return mAppsList;
	}
	public void setOnClickListener(OnClickListener listener){
		mOnClickListener = listener;
	}

	public interface OnClickListener {
		abstract void OnClick();
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
			mIcon.setId(0x1220); //？
			RelativeLayout.LayoutParams mIconLayoutParams = new RelativeLayout.LayoutParams(
					55, 55);
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
		int dataTypeSize = 0;
		boolean isImageApp = false;
		boolean isVideoApp = false;
		mClickPosition = position;
		
	
//		isImageApp = mApps_image.contains(mApps.get(position));
//		isVideoApp = mApps_video.contains(mApps.get(position));
//		Log.v(TAG, " mApps_image.size()"+ mApps_image.size());
		
		if (isContainInList(mApps_image, mApps.get(position))) {
			isImageApp = true;
		}

		if (isContainInList(mApps_video, mApps.get(position))) {
			isVideoApp = true;
		}
//		if (mApps_image != null && mApps_image.size() != 0) {
//			isImageApp = true;
//			
//		}
//	
//		if (mApps_video != null && mApps_video.size() != 0) {
//			isVideoApp = true;
//			Log.v(TAG, " mApps_video.size()"+ mApps_video.size());
//		}
		if(isImageApp){
			mCurMetaType = MEDIA_META_TYPE.IMAGE_MEDIA_TYPE;
		}
		
		if(isVideoApp){
			mCurMetaType = MEDIA_META_TYPE.VIDEO_MEDIA_TYPE;
		}
		
		if(isImageApp && isVideoApp){
			mCurMetaType = MEDIA_META_TYPE.ALL_MEDIA_TYPE;
		}
		
		Log.v(TAG, "mCurMetaType"+mCurMetaType);
		hide();
		
		if(mOnClickListener != null){
			mOnClickListener.OnClick();
		}
	}
	
	public void sendShareList(List<String> fileList){
		int size = 0;
		ResolveInfo info = mApps.get(mClickPosition);
		Intent intent = new Intent();
		ArrayList<Uri> aFileList = new ArrayList<Uri>();
		
		if(fileList == null || fileList.size() == 0){
			return;
		}
		
		if(mImageFilePath == null){
			mImageFilePath = new ArrayList<String>();
		}
		if(mVideoFilePath == null){
			mVideoFilePath = new ArrayList<String>();
		}
		
		size = fileList.size();
		mImageFilePath.clear();
		mVideoFilePath.clear();
		for(int i = 0; i < size; i++){
			if(FileTypeHelper.isImageFile(fileList.get(i))){
				mImageFilePath.add(fileList.get(i));
			}
			if(FileTypeHelper.isVideoFile(fileList.get(i))){
				mVideoFilePath.add(fileList.get(i));
			}
		}
		
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
			if(mOnShareToListener != null){
				mOnShareToListener.onShared(intent, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(mOnShareToListener != null){
				mOnShareToListener.onShared(intent, false);
			}
		} finally {
			//Do Nothing
		}
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
	public void hide() {
		if (mMainLayout != null) {
			mMainLayout.setVisibility(View.GONE);
		}
		mIsShow = false;
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
		case ANCHOR_TOP_LEFT:

			break;
		case ANCHOR_BUTTOM_LEFT:
			mShareMenuY = mShareMenuY - mShareMenuHeight;
			break;
		case ANCHOR_CENTER:
			mShareMenuX = mShareMenuX - mShareMenuWidth / 2;
			mShareMenuY = mShareMenuY - mShareMenuHeight / 2;
			break;
		case ANCHOR_TOP_RIGHT:
			mShareMenuX = mShareMenuX - mShareMenuWidth;

			break;
		case ANCHOR_BUTTOM_RIGHT:
			mShareMenuX = mShareMenuX - mShareMenuWidth;
			mShareMenuY = mShareMenuY - mShareMenuHeight;
			break;
		}
	}

	
	@Override
	public void setRotation(float rotation) {
		mMainLayout.setRotation(rotation);
	}
	
	/**
	 * 设定分享菜单的应用媒体类型支持
	 * @param metaType
	 */
	public void setMetaType(MEDIA_META_TYPE metaType){
		if(mApps != null){
			mApps.clear();
		}else{
			mApps = new ArrayList<ResolveInfo>();
		}
		loadApps(metaType);
		mListSize = mApps.size();
		if(CSStaticData.DEBUG){
			Log.w(TAG, "[addToLayout]找到 " + mApps.size() + " 个应用");
		}

	}
	
	/**
	 * 获取分享菜单的应用媒体类型支持
	 * @return
	 */
	public MEDIA_META_TYPE getMetaType(){
		return mCurMetaType;
	}
	
	/**
	 * 判断当前选中的应用是否可接受多个内容
	 * @return
	 */
	public SHARE_MULTI_SELECT_TYPE isMultiApp(){
		return mCurMultiSelectionType;
	}
	
	public void setMultiSelectionMode(SHARE_MULTI_SELECT_TYPE multiMode){
		mCurMultiSelectionType = multiMode;
		setMetaType(mCurMetaType);
	}
	
	protected boolean isContainInList(List<ResolveInfo> list, ResolveInfo elem) {
		int listSize = 0;
		if(list == null){
			return false;
		}
		listSize = list.size();
		for(int i = 0; i < listSize; i++){
			if(resolveInfoEquals(list.get(i), elem)){
				return true;
			}
		}
		
		return false;
	}
	
	protected boolean resolveInfoEquals(ResolveInfo elemA, ResolveInfo elemB) {
		if(elemA.activityInfo.loadLabel(mContext.getPackageManager()).equals(elemB.activityInfo.loadLabel(mContext.getPackageManager()))){
			return true;
		}else{
			return false;
		}
	}
	
	public void setOnShareToListener(OnShareToListener listener){
		mOnShareToListener = listener;
	}
	
	public int saveStatus(){
		return mClickPosition;
	}
	
	public void restoreStatus(int position){
		mClickPosition = position;
	}
	
	public interface OnShareToListener{
		public void onShared(Intent intent, boolean isSuccess);
	}
}



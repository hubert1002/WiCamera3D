package com.wistron.WiGallery;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wistron.swpc.wicamera3dii.R;

public class GalleryListViewAdapter extends BaseAdapter {
	private MainMenuViewHolder mMainMenuViewHolder;
	private Context mContext;
	private int[][] mResId = null;
	private int mItemid = -1;
	private int mMenuType = 0;
	public static final int MENU_TYPE_MAIN_MENU    = 0x220;
	public static final int MENU_TYPE_MOVETO_MENU  = 0x221;
	public static final int MENU_TYPE_GROUP_MENU   = 0x222;
	public static final int MENU_TYPE_ORDER_MENU   = 0x223;
	public static final int MENU_TYPE_FILTER_MENU  = 0x224;

	public GalleryListViewAdapter(Context context,int[][] resid) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		mResId = resid;
	}

	@Override
	public int getCount() {
		return mResId.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getItemStartId(){
		return mItemid;
	}
	
	public void setItemStartId(int id){
		mItemid = id;
	}

	public void setResId(int[][] resid){
		mResId = resid;
	}
	
	public void setMenuType(int type){
		mMenuType = type;
	}
	
	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		Log.v("", "isEnabled");
	
		switch (mMenuType) {
		case MENU_TYPE_MAIN_MENU:
			if (position == WiGalleryActivity.mMoreMenuDisableClickPosition) {
				Log.v("", "此选项不可用");
				return false;
			}
			break;

		case MENU_TYPE_GROUP_MENU:
			if (position == WiGalleryActivity.mGroupMenuCurClickPosition) {
				Log.v("", "此选项不可用");
				return false;
			}
			break;
			
		case MENU_TYPE_ORDER_MENU:
			if (position == WiGalleryActivity.mOrderMenuCurClickPosition) {
				Log.v("", "此选项不可用");
				return false;
			}
			break;
			
		case MENU_TYPE_FILTER_MENU:
			if (position == WiGalleryActivity.mFilterMenuCurClickPosition) {
				Log.v("", "此选项不可用");
				return false;
			}
			break;
		}
		
		
		return super.isEnabled(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		mMainMenuViewHolder = null;
		if (convertView == null) {
			mMainMenuViewHolder = new MainMenuViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.gallery_menu_item, null);
			mMainMenuViewHolder.mImageView = (ImageView) convertView.findViewById(R.id.galleryMenuIcon);
			mMainMenuViewHolder.mTextView = (TextView) convertView.findViewById(R.id.galleryMenuText);
			convertView.setTag(mMainMenuViewHolder);
		} else {
			mMainMenuViewHolder = (MainMenuViewHolder) convertView.getTag();
		}

		mMainMenuViewHolder.mImageView.setImageResource(mResId[position][0]);
		mMainMenuViewHolder.mTextView.setText(mContext.getResources().getText(mResId[position][1]));

		convertView.setId(mItemid + position);
		
		switch (mMenuType) {
		case MENU_TYPE_MAIN_MENU:
			if (position == WiGalleryActivity.mMoreMenuCurClickPosition) {
				mMainMenuViewHolder.mTextView.setTextColor(WiGalleryActivity.COLORSTATELIST_GREEN);
			}else{
				mMainMenuViewHolder.mTextView.setTextColor(WiGalleryActivity.COLORSTATELIST_WHITE);
			}
			
			break;

		case MENU_TYPE_GROUP_MENU:
			if (position == WiGalleryActivity.mGroupMenuCurClickPosition) {
				mMainMenuViewHolder.mTextView.setTextColor(Color.GRAY);
			}else{
				mMainMenuViewHolder.mTextView.setTextColor(WiGalleryActivity.COLORSTATELIST_WHITE);
			}
			break;
			
		case MENU_TYPE_ORDER_MENU:
			if (position == WiGalleryActivity.mOrderMenuCurClickPosition) {
				mMainMenuViewHolder.mTextView.setTextColor(Color.GRAY);
			}else{
				mMainMenuViewHolder.mTextView.setTextColor(WiGalleryActivity.COLORSTATELIST_WHITE);
			}
			break;
		case MENU_TYPE_FILTER_MENU:
			if (position == WiGalleryActivity.mFilterMenuCurClickPosition) {
				mMainMenuViewHolder.mTextView.setTextColor(Color.GRAY);
			}else{
				mMainMenuViewHolder.mTextView.setTextColor(WiGalleryActivity.COLORSTATELIST_WHITE);
			}
			break;
		}
		
		if(mMenuType == MENU_TYPE_MAIN_MENU && position == WiGalleryActivity.mMoreMenuDisableClickPosition){
			mMainMenuViewHolder.mTextView.setTextColor(Color.GRAY);
		}

		return convertView;
	}
	
	class MainMenuViewHolder {
		ImageView mImageView;
		TextView mTextView;
	}
	
	public void notifyDataSetChanged(){
		super.notifyDataSetChanged();
	}
}

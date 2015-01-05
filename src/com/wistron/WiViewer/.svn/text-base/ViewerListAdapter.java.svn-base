package com.wistron.WiViewer;

import java.util.HashMap;
import com.wistron.swpc.wicamera3dii.R;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewerListAdapter extends BaseAdapter {
	private MainMenuViewHolder mMainMenuViewHolder;
	private Context mContext;
	private int[][] mResId = null;
	private int mItemid = -1;
	private int mMenuItemStartId = -1;
	private int mMenuType = 0;
	private HashMap<Integer, Integer> mMap;
	public static final int MENU_TYPE_MAIN_MENU_IMAGEVIEWER = 0x220;
	public static final int MENU_TYPE_SUB_MENU_IMAGEVIEWER = 0x221;
	public static final int MENU_TYPE_MAIN_MENU_VIDEOVIEWER = 0x222;
	public static final int MENU_TYPE_SUB_MENU_VIDEOVIEWER = 0x223;
	

	public ViewerListAdapter(Context context,int[][] resid) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		mResId = resid;
	}

	public void setMenuType(int type){
		mMenuType = type;
	}
	@Override
	public int getCount() {
		if (mResId == null) {
			return -1;
		}
		return mResId.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
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
		mMenuItemStartId = id;
	}
	
	public void setResId(int[][] resid){
		mResId = resid;
	}

	public void setMap(HashMap<Integer, Integer> map){
		mMap = map;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		mMainMenuViewHolder = null;
		if (convertView == null) {
			mMainMenuViewHolder = new MainMenuViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.viewer_menu_item, null);
			mMainMenuViewHolder.mImageView = (ImageView) convertView.findViewById(R.id.videoPlayerMenuIcon);
			mMainMenuViewHolder.mTextView = (TextView) convertView.findViewById(R.id.videoPlayerMenuText);
			convertView.setTag(mMainMenuViewHolder);
		} else {
			mMainMenuViewHolder = (MainMenuViewHolder) convertView.getTag();
		}

		mMainMenuViewHolder.mImageView.setImageResource(mResId[position][0]);
		mMainMenuViewHolder.mTextView.setText(mContext.getResources().getText(mResId[position][1]));

		convertView.setId(mItemid + position);

		switch (mMenuType) {
		
		
		case MENU_TYPE_MAIN_MENU_IMAGEVIEWER:
			if (position == WiImageViewerActivity.mListPosition) {
				mMainMenuViewHolder.mTextView.setTextColor(WiImageViewerActivity.COLORSTATELIST_GREEN);
			}else{
				
				mMainMenuViewHolder.mTextView.setTextColor(WiImageViewerActivity.COLORSTATELIST_WHITE);
			}
			
			for(int i = 0;i < WiImageViewerActivity.mDisableList.size();i++){
				if (position  == WiImageViewerActivity.mDisableList.get(i) )
				{
					mMainMenuViewHolder.mTextView.setTextColor(Color.GRAY);
				}
			}
			
			break;

		case MENU_TYPE_SUB_MENU_IMAGEVIEWER:
			try {
				if (mMap.size() != 0 && mMap.get(mMenuItemStartId) == position) {
					mMainMenuViewHolder.mTextView.setTextColor(Color.GRAY);
				} else {
					mMainMenuViewHolder.mTextView.setTextColor(WiImageViewerActivity.COLORSTATELIST_WHITE);
				}
			} catch (NullPointerException e) {
				// TODO: handle exception
			}
			break;
		case MENU_TYPE_MAIN_MENU_VIDEOVIEWER:
			if (position == WiVideoViewerActivity.mListPosition) {
				mMainMenuViewHolder.mTextView.setTextColor(WiVideoViewerActivity.COLORSTATELIST_GREEN);
			}else{
				mMainMenuViewHolder.mTextView.setTextColor(WiVideoViewerActivity.COLORSTATELIST_WHITE);
			}
			
			if (position  == WiVideoViewerActivity.mMainListDisablePosition )
			{
				mMainMenuViewHolder.mTextView.setTextColor(Color.GRAY);
			}
			break;

		case MENU_TYPE_SUB_MENU_VIDEOVIEWER:
			try {
				if (mMap.size() != 0 && mMap.get(mMenuItemStartId) == position) {
					mMainMenuViewHolder.mTextView.setTextColor(Color.GRAY);
				} else {
					mMainMenuViewHolder.mTextView.setTextColor(WiVideoViewerActivity.COLORSTATELIST_WHITE);
				}
			} catch (NullPointerException e) {
				// TODO: handle exception
			}
			
			break;
		}
		
		return convertView;
	}
	
	class MainMenuViewHolder {
		ImageView mImageView;
		TextView mTextView;
	}
	
	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
	
		switch (mMenuType) {
		case MENU_TYPE_MAIN_MENU_IMAGEVIEWER:
//			if (position == WiImageViewerActivity.mMainListDisablePosition)
//			{
//				return false;
//			}
			
			for(int i = 0;i < WiImageViewerActivity.mDisableList.size();i++){
				if (position  == WiImageViewerActivity.mDisableList.get(i) )
				{
					return false;
				}
			}
			break;

		case MENU_TYPE_SUB_MENU_IMAGEVIEWER:
			try {
				if (mMap.size() != 0 && mMap.get(mMenuItemStartId) == position) {
					return false;
				} 
			} catch (NullPointerException e) {
				// TODO: handle exception
			}
		case MENU_TYPE_MAIN_MENU_VIDEOVIEWER:
//			if (position == WiVideoViewerActivity.mListPosition) {
//				return false;
//			}
			
			if (position == WiVideoViewerActivity.mMainListDisablePosition)
			{
				return false;
			}
			break;

		case MENU_TYPE_SUB_MENU_VIDEOVIEWER:
			try {
				if (mMap.size() != 0 && mMap.get(mMenuItemStartId) == position) {
					return false;
				} 
			} catch (NullPointerException e) {
				// TODO: handle exception
			}
			
			break;
		}
		
		
		return super.isEnabled(position);
	}
}

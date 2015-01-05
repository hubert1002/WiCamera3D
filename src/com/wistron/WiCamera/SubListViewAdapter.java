package com.wistron.WiCamera;

import java.util.HashMap;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wistron.swpc.wicamera3dii.R;

public class SubListViewAdapter extends BaseAdapter {
	public int mMenuItemId = -1;
	private int[][] mResIdArray = null;
	private SubMenuViewHolder holder;
	private int mMenuItemStartId = -1;
	private Context context;
	private HashMap<Integer, Integer> mMap;

	public SubListViewAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mResIdArray.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public void setMenuItemStartId(int id) {
		this.mMenuItemId = id;
		mMenuItemStartId = id;
	}

	public int getMenuItemStartId() {
		return mMenuItemId;
	}

	public void setResIdArray(int[][] resId) {
		this.mResIdArray = resId;
	}
	
	public void setMap(HashMap<Integer, Integer> map){
		mMap = map;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		holder = null;
		if (convertView == null) {
			holder = new SubMenuViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.list_items_submenu, null);
			holder.mText = (TextView) convertView.findViewById(R.id.Content);
			holder.mImageView = (ImageView) convertView.findViewById(R.id.HeadImage);
			holder.mRadioButton = (RadioButton) convertView.findViewById(R.id.rbtn);
//			holder.mRadioButton.setScaleX(1f/WiCameraActivity.mDensity);
//			holder.mRadioButton.setScaleY(1f/WiCameraActivity.mDensity);
			convertView.setTag(holder);
		} else {
			holder = (SubMenuViewHolder) convertView.getTag();
		}

		holder.mImageView.setImageResource(mResIdArray[position][0]);
		if (mResIdArray[position][0] == 0) {
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.setMargins(20, 0, 0, 0);
			holder.mImageView.setLayoutParams(layoutParams);
		}
		holder.mText.setText(context.getResources().getText(mResIdArray[position][1]));

		try {
			if (mMap.size() != 0 && mMap.get(mMenuItemStartId) == position) {
				holder.mRadioButton.setChecked(true);
			} else {
				holder.mRadioButton.setChecked(false);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		

		convertView.setId(mMenuItemId + position);
		holder.mRadioButton.setId(position);

		return convertView;
	}
}

class SubMenuViewHolder {
	TextView mText;
	ImageView mImageView;
	RadioButton mRadioButton;

}
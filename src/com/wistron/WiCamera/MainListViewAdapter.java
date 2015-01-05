package com.wistron.WiCamera;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.wistron.swpc.wicamera3dii.R;

public class MainListViewAdapter extends BaseAdapter {
	private MainMenuViewHolder mMainMenuViewHolder;
	private Context mContext;
	private int[] mData;

	public MainListViewAdapter(Context context, int[] data) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		mData = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.length;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		mMainMenuViewHolder = null;
		if (convertView == null) {
			mMainMenuViewHolder = new MainMenuViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.list_items_settingmenu, null);
			mMainMenuViewHolder.mTextView = (TextView) convertView
					.findViewById(R.id.SettingContent);
			convertView.setTag(mMainMenuViewHolder);
		} else {
			mMainMenuViewHolder = (MainMenuViewHolder) convertView.getTag();
		}

		mMainMenuViewHolder.mTextView.setText(mContext.getResources().getText(
				mData[position]));

		if (WiCameraActivity.mIsCameraList
				&& position == WiCameraActivity.LISTPOSITION
				&& WiCameraActivity.mSubListView.getVisibility() == View.VISIBLE) {
			mMainMenuViewHolder.mTextView
					.setTextColor(WiCameraActivity.COLORSTATELIST_GREEN);
			// mMainMenuViewHolder.mTextView.setTextColor(mContext.getResources().getColor(R.color.green));
		} else if (WiCameraActivity.mIsRecoderList
				&& position == WiCameraActivity.LISTPOSITION
				&& WiCameraActivity.mSubListView.getVisibility() == View.VISIBLE) {
			mMainMenuViewHolder.mTextView
					.setTextColor(WiCameraActivity.COLORSTATELIST_GREEN);
			// mMainMenuViewHolder.mTextView.setTextColor(mContext.getResources().getColor(R.color.green));
		} else {
			mMainMenuViewHolder.mTextView
					.setTextColor(WiCameraActivity.COLORSTATELIST_WHITE);
			// mMainMenuViewHolder.mTextView.setTextColor(Color.WHITE);
		}

		return convertView;
	}

}

class MainMenuViewHolder {
	TextView mTextView;

}

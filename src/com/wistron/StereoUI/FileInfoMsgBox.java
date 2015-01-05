package com.wistron.StereoUI;


import com.wistron.swpc.wicamera3dii.R;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class FileInfoMsgBox {

	private ListView 					mListView = null;
	private TextView 					mTitleTV = null;
	private RelativeLayout.LayoutParams mMsgBoxLayoutParams = null,
										mTitleLayoutParams = null;
	private MBaseAdapter 				mBaseAdapter = null;
	private SpannableString[] 			mContentList = null;
	private Context 					mContext = null;
	private RelativeLayout 				mMsgBoxLayout = null,
										mMainLayout = null;
	private int 						mMsgBoxWidth = 400, 
										mTitleHeight = 48, 
										mTextSize = 19;
	private boolean 					mIsClickBlankHide = false,
										mMsgBoxIsShow = false,
										mIsInfoType = false,mIsmode = true;
	private ViewGroup 					mParentGroup = null;
	private CharSequence 				mMessage = null;
	
	

	public FileInfoMsgBox(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		mListView = new ListView(context);
	
		mTitleTV = new TextView(context);
		mMsgBoxLayout = new RelativeLayout(context);
		mMainLayout = new RelativeLayout(context);
		mMainLayout.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		//标题
		mTitleTV.setGravity(Gravity.CENTER);
		mTitleTV.setBackgroundColor(Color.BLACK);
		mTitleTV.setTextColor(Color.WHITE);
		mTitleTV.setTextSize(mTextSize+3);
		
		mListView.setPadding(8, mTitleHeight, 8, 15);
		mListView.setScrollingCacheEnabled(false);
		mListView.setBackgroundResource(R.drawable.main_menu);
		mListView.setClickable(false);
//		mListView.setDivider(new ColorDrawable(Color.GRAY));
		mListView.setDividerHeight(2);
		
		
		mMainLayout.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				//设定为模式对话框
				
				if (mIsClickBlankHide ) {
					hideAndRemove();
					return mIsmode;
				}
				return mIsmode;
			}
		});
		
		mMsgBoxLayout.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});

	}

	public void setMode(boolean ismode){
		mIsmode = ismode;
	}
	public void setMessage(SpannableString[] list){
		mContentList = list;
	}
	
	public void setMessage(CharSequence msg){
		mMessage = msg;
		mIsInfoType = true;
	}
	public void setTitle(CharSequence Title){
		mTitleTV.setText(Title);
	}
	
	public void setInfoType(boolean type){
		mIsInfoType = type;
	}
	
	public void setTitleLayoutParams(RelativeLayout.LayoutParams layoutParams){
		mTitleLayoutParams = layoutParams;
		mTitleLayoutParams.setMargins(8, 8, 8, 8);
	}
	public void setLayoutParams(RelativeLayout.LayoutParams layoutParams){
		mMsgBoxLayoutParams = layoutParams;
		mMsgBoxLayout.setLayoutParams(layoutParams);
	}
	
	public void addToLayout(ViewGroup viewGroup){
		
		mParentGroup = viewGroup;
	
	
		if (mMsgBoxLayoutParams == null) {
			mMsgBoxLayoutParams = new RelativeLayout.LayoutParams(mMsgBoxWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
			mMsgBoxLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		}
		
		if(mTitleLayoutParams == null){
			mTitleLayoutParams = new RelativeLayout.LayoutParams(mMsgBoxWidth,mTitleHeight);
			mTitleLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
			mTitleLayoutParams.setMargins(8, 8, 8, 8);
			
		}
		mTitleTV.setLayoutParams(mTitleLayoutParams);
		
		if (mIsInfoType) {
			mContentList = new SpannableString[7];
			mContentList[2] = new SpannableString(mMessage);
			mListView.setDivider(null);
		}else{
			
			
		}
		mBaseAdapter = new MBaseAdapter();
		mListView.setAdapter(mBaseAdapter);
		mMsgBoxLayout.addView(mListView);
		mMsgBoxLayout.addView(mTitleTV);
		mMsgBoxLayout.setLayoutParams(mMsgBoxLayoutParams);
		
		mMainLayout.addView(mMsgBoxLayout);
		mMainLayout.setBackgroundColor(Color.BLACK);
		mMainLayout.getBackground().setAlpha(50);
		viewGroup.addView(mMainLayout);
		mMsgBoxIsShow = true;
	}
	
	/**
	 *隐藏MessageBox
	 */
	public void hideAndRemove(){
		if(mMainLayout == null || !isShown()){
			return;
		}
		mMainLayout.setVisibility(View.GONE);
		mMsgBoxIsShow = false;
		if (mParentGroup != null) {
			mParentGroup.removeView(mMainLayout);
		}
		mTitleTV = null;
		mListView = null;
		
	}
	
	public void hide(){
		if(mMainLayout == null || !isShown()){
			return;
		}
		mMainLayout.setVisibility(View.GONE);
	}
	
	public void show(){
		if(mMainLayout == null ){
			return;
		}
		mMainLayout.setVisibility(View.VISIBLE);
	}
	
	public boolean isShown(){
		return mMsgBoxIsShow;
	}
	
	public void setRotation(float rotation) {
		// TODO Auto-generated method stub
		
		mMsgBoxLayout.setRotation(rotation);
	}
	
	public void setTranslationX(float x){
		mMsgBoxLayout.setTranslationX(x);
	}
	public boolean dispatchTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		
		mMainLayout.dispatchTouchEvent(event);
		return mMsgBoxIsShow;
	}
	private class MBaseAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mContentList.length;
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
			ViewHolder viewHolder = null;
			if (convertView == null) {
				viewHolder = new ViewHolder(mContext);
				convertView = viewHolder.mTextView;
				convertView.setTag(viewHolder);
			}else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
				viewHolder.mTextView.setText(mContentList[position]);
			
			return convertView;
		}
		
		
		@Override
		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
//			return super.isEnabled(position);
			return false;
		}

	}
	
	private class ViewHolder{
		private TextView mTextView = null;
		
		public ViewHolder(Context context) {
			// TODO Auto-generated constructor stub
			mTextView = new TextView(context);
			mTextView.setTextSize(mTextSize);
			mTextView.setPadding(10, 0, 0, 0);
			
			
			if (mIsInfoType) {
				mTextView.setGravity(Gravity.CENTER);
				mTextView.setTextColor(Color.WHITE);
			}
		}
	}

	/**
	 * 设置点击对话框之外的地方让对话框消失
	 * @param isHide
	 */
	public void setClickBlankHide(boolean isHide){
		mIsClickBlankHide = isHide;
	}
}

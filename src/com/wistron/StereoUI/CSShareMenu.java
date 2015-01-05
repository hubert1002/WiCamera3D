package com.wistron.StereoUI;

import java.util.ArrayList;
import java.util.List;

import com.wistron.swpc.wicamera3dii.R;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Copyright (c) 2011 Wistron SWPC
 * All rights reserved.
 * created: 2011/10/26
 * filename: CSShareMenu.java
 * @author WangWei
 * purpose:分享菜单
 */
@SuppressWarnings("deprecation")
public class CSShareMenu implements OnItemClickListener {

	private Context m_context;
	private ListView m_lv_left;
	private ListView m_lv_right;
	private List<ResolveInfo> mApps;
	private LinearLayout m_LinearLayout;
	private LinearLayout m_LinearLayout_left;
	private LinearLayout m_LinearLayout_right;
	private ViewGroup m_parent_layout = null;
	private ArrayList<String> m_filepath_image = null;
	private ArrayList<String> m_filepath_video = null;

	private int m_shareMenu_x = 0;                                //定义分享菜单的横纵坐标以及宽高
	private int m_shareMenu_y = 0;
	private int m_shareMenu_width = 141;
	private int m_shareMenu_height = 312;

	private int m_location = 0;
	private int m_list_size = 0;
	private int m_image_width = 22;
	private int m_image_height = 44;
	private int m_title_height = 41;
	private int m_listItem_height = 48;
	private int m_botom_height= 21;
	
	private int mListTitleBg = 0;
	private int mListBottonBg = 0;
	private int mListSelector = 0;
	private int mListLayoutSelector = 0;
	
	
	private float screenWidth = 0;
	private float screenHeight = 0;
	private boolean m_allow_hide_share = true;
	private boolean m_shareMenu_isShow = false;
	private boolean m_isShareMenu_touch = false;

	public static final int LOCATION_LEFT_TOP = 1;
	public static final int LOCATION_LEFT_BOTTOM = 2;
	public static final int LOCATION_RIGHT_TOP = 3;
	public static final int LOCATION_RIGHT_BOTTOM = 4;
	public static final int LOCATION_CENTER = 5;
	private static final String SHARE_IMAGE = "Share image";
	private static final String SHARE_VIDEO = "Share video";
	private static final String SHARE_VIDEO_AND_IMAGE = "Share video and image";

	/**
	 * 初始化ShareMenu
	 * @param context:getBaseContext
	 * @param location:弹出的位置
	 * @param x:x坐标
	 * @param y:y坐标
	 */
	public CSShareMenu(Context context,int location,int x,int y) {
		this.m_context = context;
		this.m_location = location;
		this.m_shareMenu_x = x;
		this.m_shareMenu_y = y;

		m_filepath_image=new ArrayList<String>();
		m_filepath_video=new ArrayList<String>();
		mApps = new ArrayList<ResolveInfo>();

		//获取屏幕的宽高
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		screenWidth = windowManager.getDefaultDisplay().getWidth();
		screenHeight = windowManager.getDefaultDisplay().getHeight();
	}

	public ArrayList<String> getImagePath() {
		return m_filepath_image;
	}

	public void setImagePath(ArrayList<String> filepath_image) {
		this.m_filepath_image = filepath_image;
	}

	public ArrayList<String> getVideoPath() {
		return m_filepath_video;
	}

	public void setVideoPath(ArrayList<String> filepath_video) {
		this.m_filepath_video = filepath_video;
	}

	/**
	 * 将CSShareMenu添加到布局中
	 * @param layout:父布局
	 */
	public void addToLayout(ViewGroup layout){
		m_parent_layout = layout;

		//若图片和视频路径都为空，弹出提示框
		if(m_filepath_image.size() == 0&& m_filepath_video.size() == 0){
//			TDStaticData.ShowMsgBox(m_context, layout, "Warning"
//					, "Image or video not found!", CSMessageBox.TYPE1_OK, 512, 300, new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					TDStaticData.HideMsgBox(m_parent_layout);
//
//				}
//			}, null, null);
		
		
		}else{
			loadApps();
			m_list_size = mApps.size();
			float mscreenHeight = screenHeight;

			//根据location的值计算分享菜单的位置
			switch (m_location) {
			case 1:
				break;
			case 2:
				if(m_list_size<6){
					m_shareMenu_y = (int) ((mscreenHeight/600) *m_shareMenu_y)- m_botom_height - m_list_size * m_listItem_height- m_title_height;
				}else{
					m_shareMenu_y =  (int)((mscreenHeight/600) *m_shareMenu_y) - m_botom_height - 6 * m_listItem_height- m_title_height ;
				}

				break;

			case 3:
				m_shareMenu_x = m_shareMenu_x - m_shareMenu_width;
				break;

			case 4:
				if(m_list_size<6){	
					m_shareMenu_y = (int) ((mscreenHeight/600) *m_shareMenu_y)- m_botom_height - m_list_size * m_listItem_height- m_title_height;	
				}else{
					m_shareMenu_y =  (int)((mscreenHeight/600) *m_shareMenu_y) - m_botom_height - 6 * m_listItem_height- m_title_height ;
				}
				m_shareMenu_x = m_shareMenu_x - m_shareMenu_width;
				break;

			case 5:
				if(m_list_size<6){	
					m_shareMenu_y -= (int) ((mscreenHeight/600 *m_shareMenu_y- m_botom_height - m_list_size * m_listItem_height- m_title_height)/2);	
				}else{
					m_shareMenu_y -= (int)((mscreenHeight/600 *m_shareMenu_y - m_botom_height - 6 * m_listItem_height- m_title_height)/2) ;
				}
				m_shareMenu_x -= m_shareMenu_width/2;
				break;

			}
			m_LinearLayout = new LinearLayout(m_context);
			m_LinearLayout_left = new LinearLayout(m_context);
			m_LinearLayout_right = new LinearLayout(m_context);
			m_LinearLayout_left.setPadding(0, 0, 0, 0);
			m_LinearLayout_right.setPadding(0, 0, 0, 0);
			m_LinearLayout_left.setOrientation(LinearLayout.VERTICAL);
			m_LinearLayout_right.setOrientation(LinearLayout.VERTICAL);
			m_LinearLayout.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

			m_lv_left = new ListView(m_context);
			m_lv_left.setCacheColorHint(0);
			m_lv_left.setFadingEdgeLength(0);
			m_lv_left.setDivider(null);

//			m_lv_left.setSelector(R.drawable.popup_list_share_selector_test);
			LinearLayout.LayoutParams layoutParams0;
			if(m_list_size<6){
				layoutParams0= new LinearLayout.LayoutParams(m_shareMenu_width,
						LayoutParams.WRAP_CONTENT);

			}else{
				layoutParams0= new LinearLayout.LayoutParams(m_shareMenu_width,
						m_shareMenu_height);
			}

			LinearLayout.LayoutParams lp_layout_left= new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			LinearLayout.LayoutParams lp_layout_right= new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);

			lp_layout_left.setMargins(m_shareMenu_x, m_shareMenu_y-m_title_height, 0, 0);
			lp_layout_right.setMargins((int) (screenWidth/2-m_shareMenu_width), m_shareMenu_y-m_title_height, 0, 0);
			m_lv_left.setAdapter(new ListAppAdapter());
			m_lv_left.setOnItemClickListener(this);

			//左边listview的title和尾部
			TextView textView_title_left = new TextView(m_context);
			textView_title_left.setText("Share");
			textView_title_left.setTextScaleX(0.5f);
			textView_title_left.setTextSize(18);
			textView_title_left.setTextColor(Color.GRAY);
			textView_title_left.setBackgroundResource(mListTitleBg);
			textView_title_left.setLayoutParams(new LinearLayout.LayoutParams(m_shareMenu_width, m_title_height));

			ImageView imageView_left = new ImageView(m_context);
			imageView_left.setBackgroundResource(mListBottonBg);
			imageView_left.setLayoutParams(new LinearLayout.LayoutParams(m_shareMenu_width, m_botom_height));

			m_LinearLayout_left.addView(textView_title_left);
			m_LinearLayout_left.addView(m_lv_left,layoutParams0);
			m_LinearLayout_left.addView(imageView_left);

			//右边的listview
			m_lv_right = new ListView(m_context);
			m_lv_right.setCacheColorHint(Color.TRANSPARENT);
			m_lv_right.setDivider(null);
			m_lv_right.setFadingEdgeLength(0);
			m_lv_right.setSelector(mListSelector);
			LinearLayout.LayoutParams layoutParams;
			if(m_list_size<6){
				layoutParams = new LinearLayout.LayoutParams(m_shareMenu_width,
						LayoutParams.WRAP_CONTENT);
			}else{
				layoutParams = new LinearLayout.LayoutParams(m_shareMenu_width,
						m_shareMenu_height);
			}

			m_lv_right.setAdapter(new ListAppAdapter());

			//右边listview的title
			TextView textView_title_right = new TextView(m_context);
			textView_title_right.setText("Share");
			textView_title_right.setTextScaleX(0.5f);
			textView_title_right.setTextSize(18);
			textView_title_right.setTextColor(Color.GRAY);
			textView_title_right.setBackgroundResource(mListTitleBg);
			textView_title_right.setLayoutParams(new LinearLayout.LayoutParams(m_shareMenu_width, m_title_height));

			ImageView imageView_right = new ImageView(m_context);
			imageView_right.setBackgroundResource(mListBottonBg);
			imageView_right.setLayoutParams(new LinearLayout.LayoutParams(m_shareMenu_width, m_botom_height));

			m_LinearLayout_right.addView(textView_title_right);
			m_LinearLayout_right.addView(m_lv_right,layoutParams);
			m_LinearLayout_right.addView(imageView_right);

			m_LinearLayout.addView(m_LinearLayout_left,lp_layout_left);
			m_LinearLayout.addView(m_LinearLayout_right,lp_layout_right);
			layout.addView(m_LinearLayout);
		}
	}



	/**
	 * 加载可接受图片或视频的应用
	 */
	private void loadApps() {

		//		 Intent mainintent = new Intent(Intent.ACTION_MAIN, null);
		//		 mainintent.addCategory(Intent.CATEGORY_LAUNCHER);
		//		 mApps = this.getPackageManager().queryIntentActivities(mainintent, 0);

		Intent intent = new Intent();
		List<ResolveInfo> mApps_image = new ArrayList<ResolveInfo>();
		List<ResolveInfo> mApps_video = new ArrayList<ResolveInfo>();

		if(m_filepath_image.size()>1 ||m_filepath_video.size()>1 || (m_filepath_image.size()!=0 && m_filepath_video.size()!=0)){
			intent.setAction(Intent.ACTION_SEND_MULTIPLE);
		}else{
			intent.setAction(Intent.ACTION_SEND);
		}

		if(m_filepath_image.size() != 0 &&m_filepath_video.size() == 0){

			intent.setType("image/*");
			mApps_image = m_context.getPackageManager().queryIntentActivities(intent, 0); 
			mApps = mApps_image;

		}else if(m_filepath_image.size() == 0 && m_filepath_video.size() != 0){

			intent.setType("video/*");
			mApps_video =m_context.getPackageManager().queryIntentActivities(intent, 0); 
			mApps = mApps_video;

		}else if(m_filepath_image.size() != 0 && m_filepath_video.size() != 0){

			intent.setType("image/*");
			mApps_image = m_context.getPackageManager().queryIntentActivities(intent, 0); 

			intent.setType("video/*");
			mApps_video =m_context.getPackageManager().queryIntentActivities(intent, 0); 

			if(mApps_image.size()!= 0&&mApps_video.size()!=0){
				int maxSize = mApps_image.size()>mApps_video.size()?mApps_image.size():mApps_video.size();
				for(int i = 0;i<maxSize;i++){
					if(mApps_image.size() == maxSize){
						if(mApps_video.contains(mApps_image.get(i)));
						mApps.add(mApps_image.get(i));
					}
					else {
						if(mApps_image.contains(mApps_video.get(i)));
						mApps.add(mApps_image.get(i));
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
				convertView = holder.m_listItem_layout;
				//				convertView = LayoutInflater.from(getBaseContext()).inflate(holder.m_listItem_layout, null);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			ResolveInfo info = mApps.get(position);
			holder.m_icon.setImageDrawable(setImageAutoScale(info.activityInfo.loadIcon(m_context.getPackageManager())));
			holder.m_appName.setText(info.activityInfo.loadLabel(m_context.getPackageManager()));
			setImageAutoScale(info.activityInfo.loadIcon(m_context.getPackageManager()));
			return convertView;

		}

		/**
		 * 把图片宽度压缩成当前的一半
		 * @param resid
		 * @return
		 */
		public Drawable setImageAutoScale(Drawable resid){
			Matrix scaleMatrix = new Matrix();
			Drawable resDrawableL = resid,
					desDrawableL = null;
			float scaleFactorWidth = 1f,
					scaleFactorHeight = 1f;

			if(screenWidth != 1024||screenHeight!=600){
				scaleFactorWidth = ((float)screenWidth/(float)1024)*0.5f;
				scaleFactorHeight = (float)screenHeight/(float)600;
			}else{
				scaleFactorWidth = 0.5f;
				scaleFactorHeight = 1f;
			}

			scaleMatrix.postScale(scaleFactorWidth, scaleFactorHeight);
			desDrawableL = new BitmapDrawable(Bitmap.createBitmap(
					drawableToBitmap(resDrawableL), 
					0, 0, 
					resDrawableL.getIntrinsicWidth(), 
					resDrawableL.getIntrinsicHeight(), 
					scaleMatrix, true));
			return desDrawableL;
		}

		private Bitmap drawableToBitmap(Drawable drawable)
		{
			int width            = 0,
					height           = 0;
			Bitmap.Config config = null;
			Bitmap bitmap        = null;
			Canvas canvas        = null;
			width  = drawable.getIntrinsicWidth();
			height = drawable.getIntrinsicHeight();
			config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888:Bitmap.Config.RGB_565;
			bitmap = Bitmap.createBitmap(width, height, config);
			canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, width, height);
			drawable.draw(canvas);

			return bitmap;
		}


		public final int getCount() {
			m_list_size = mApps.size();
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
	 * @author WH1107017
	 *
	 */
	class ViewHolder {
		private ImageView m_icon;
		private TextView m_appName;
		private AbsoluteLayout m_listItem_layout;
		private LinearLayout m_lLayout;

		public ViewHolder() {
			m_listItem_layout = new AbsoluteLayout(CSShareMenu.this.m_context);
			m_lLayout = new LinearLayout(CSShareMenu.this.m_context);
			m_appName = new TextView(CSShareMenu.this.m_context);
			m_icon = new ImageView(CSShareMenu.this.m_context);
			m_icon.setLayoutParams(new LinearLayout.LayoutParams(m_image_width,
					m_image_height));
			m_appName.setTextSize(22);
			m_appName.setTextScaleX(0.5f);
			m_lLayout.setGravity(Gravity.CENTER_VERTICAL);
			m_lLayout.addView(m_icon);
			m_lLayout.addView(m_appName);
			m_lLayout.setLayoutParams(new AbsoluteLayout.LayoutParams(m_shareMenu_width, LayoutParams.WRAP_CONTENT, 0, 0));
			m_lLayout.setBackgroundResource(mListLayoutSelector);
			m_listItem_layout.addView(m_lLayout);
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
		if(m_filepath_image.size()>1 ||m_filepath_video.size()>1 || (m_filepath_image.size()!=0 && m_filepath_video.size()!=0)){
			intent.setAction(Intent.ACTION_SEND_MULTIPLE);
			if(m_filepath_image.size()>=1 && m_filepath_video.size()>=1){

				intent.setType("*/*");
				intent.putExtra(Intent.EXTRA_SUBJECT, SHARE_VIDEO_AND_IMAGE);
				for(int i= 0;i<m_filepath_image.size();i++){
					aFileList.add(Uri.parse(m_filepath_image.get(i)));
				}
				for(int j = 0;j<m_filepath_video.size();j++){
					aFileList.add(Uri.parse(m_filepath_video.get(j)));
				}
			}else if(m_filepath_image.size()>1 && m_filepath_video.size() == 0){
				intent.setType("image/*");
				intent.putExtra(Intent.EXTRA_SUBJECT, SHARE_IMAGE);
				for(int i= 0;i<m_filepath_image.size();i++){
					aFileList.add(Uri.parse(m_filepath_image.get(i)));
				}
			}else if(m_filepath_image.size() == 0 && m_filepath_video.size() >1){
				intent.setType("video/*");
				intent.putExtra(Intent.EXTRA_SUBJECT, SHARE_VIDEO);
				for(int i= 0;i<m_filepath_video.size();i++){
					aFileList.add(Uri.parse(m_filepath_video.get(i)));
				}
			}

			intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, aFileList);

		}else{
			intent.setAction(Intent.ACTION_SEND);
			if(m_filepath_image.size() == 1){
				intent.setType("image/*");
				intent.putExtra(Intent.EXTRA_SUBJECT, SHARE_IMAGE);
				intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(m_filepath_image.get(0)));
			}else{
				intent.setType("video/*");
				intent.putExtra(Intent.EXTRA_SUBJECT,SHARE_VIDEO);
				intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(m_filepath_video.get(0)));
			}

		}

		try {
			intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
			m_context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 分享菜单事件分发函数
	 * @param event
	 * @return 如果down事件是在菜单上，则返回true，反之菜单隐藏并返回false
	 */
	public boolean dispatchTouchEvent(MotionEvent event) {

		event.setLocation((event.getX() - 2* m_shareMenu_x)/2, event.getY()-m_shareMenu_y);
		m_lv_left.dispatchTouchEvent(event);
		m_lv_right.dispatchTouchEvent(event);
		event.setLocation((event.getX()*2 + 2* m_shareMenu_x), event.getY()+m_shareMenu_y);
		if(m_list_size>6){
			m_list_size = 6;
		}

		if (	   event.getX() >  m_shareMenu_x * 2
				&& event.getX() < 2 * ( m_shareMenu_x +  m_shareMenu_width)
				&& event.getY() > m_shareMenu_y 
				&& event.getY() < m_shareMenu_y +  m_listItem_height *  m_list_size +m_botom_height
				) {

			event.setLocation((event.getX() - 2* m_shareMenu_x)/2, event.getY()-m_shareMenu_y);
			try {
				m_isShareMenu_touch |= m_lv_left.dispatchTouchEvent(event);
				m_lv_right.dispatchTouchEvent(event);
				//			 Log.e("CSShareMenu", "isShareMenuPressed:"+m_isShareMenu_touch);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(m_LinearLayout.getVisibility() == View.VISIBLE){
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					m_allow_hide_share = false;
				}
				return true;
			}
			else{
				m_allow_hide_share = true;
				return false;
			}
		}else{


			if(event.getAction() == MotionEvent.ACTION_DOWN){
				m_LinearLayout.setVisibility(View.GONE);
				m_isShareMenu_touch = false;
			}
			if(m_LinearLayout.getVisibility() == View.VISIBLE && event.getAction() == MotionEvent.ACTION_UP && m_allow_hide_share){
				m_LinearLayout.setVisibility(View.GONE);
				m_isShareMenu_touch = false;
			}
		}

		//如果分享菜单未被点击，则隐藏分享菜单
		//	if(!m_isShareMenu_touch){
		//		if(event.getAction() == MotionEvent.ACTION_DOWN){
		//			m_LinearLayout.setVisibility(View.GONE);
		//		}
		//		if(m_LinearLayout.getVisibility() == View.VISIBLE && event.getAction() == MotionEvent.ACTION_UP && m_allow_hide_share){
		//			m_LinearLayout.setVisibility(View.GONE);
		//		}
		//	}
		return m_isShareMenu_touch;

	}

	/**
	 * 判断分享菜单是否被点击, 如果down事件是在菜单上，则返回true，反之菜单隐藏并返回false
	 * @return
	 */
	public boolean getIsShareMenuTouch(){
		return m_isShareMenu_touch;
	}



	/**
	 * 判断分享菜单是否显示
	 * @return
	 */
	public boolean isShown(){

		return m_shareMenu_isShow;
	}

	/**
	 * 显示分享菜单
	 * @return
	 */
	public boolean show(){
		if(m_LinearLayout!= null){
			m_LinearLayout.setVisibility(View.VISIBLE);
		}
		m_shareMenu_isShow = true;
		return m_shareMenu_isShow;
	}

	/**
	 * 隐藏分享菜单
	 * @return
	 */
	public boolean hide(){
		if(m_LinearLayout!= null){
			m_LinearLayout.setVisibility(View.INVISIBLE);
		}
		m_shareMenu_isShow = false;
		return m_shareMenu_isShow;
	}

	/**
	 * 获取分享菜单的可见度
	 * @return
	 */
	public int getVisibility(){
		if(m_LinearLayout == null){
			return View.INVISIBLE;
		}
		return m_LinearLayout.getVisibility();
	}

	/**
	 * 设置分享菜单的可见度
	 * @param visibility
	 */
	public void setVisibility(int visibility){

		m_LinearLayout.setVisibility(visibility);

	}
	
	/**
	 * 设置title的背景
	 * @param resid
	 */
	public void setTitleBackground(int resid){
		
		mListTitleBg = resid;
	}
	
	/**
	 * 设置bottom的背景
	 * @param resid
	 */
	public void setBottomBackground(int resid){
		
		mListBottonBg = resid;
		
	}
	
	/**
	 * 设置菜单是背景
	 * @param resid
	 */
	public void setListSelecter(int resid){
		
		mListSelector = resid;
	}
	
	/**
	 * 设置菜单整个布局的背景
	 * @param resid
	 */
	public void setListLayoutSelector(int resid){
		
		mListLayoutSelector = resid;
	}
}

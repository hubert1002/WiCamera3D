package com.wistron.WiGallery;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.wistron.swpc.wicamera3dii.R;

import android.R.integer;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MsgBox extends View {

	private RelativeLayout 								mMsgBoxLayout 				= null,
														mButtonsLayout 				= null,
														mMainLayout                 = null;
	private TextView 									mInfoTV 					= null,
														mTitle                      = null;
	private Button 										mButtons[] 					= null;
	private ProgressBar                                 mProgressBar                = null;
	private Context 									mContext           			= null;
	private boolean             						mMsgBoxIsShow               = false,
														mIsHasTitle                 = false,
														mIsHasProgressBar           = false,
	                                                    mIsModel                    = true;  //是否为模态对话框
	private CharSequence        						mMessage                    = null;
	private ViewGroup                                   mParentViewGroup            = null;  //addToLayout的Layout
	private RelativeLayout.LayoutParams 				mLayoutParams       		= null,
														mMsgBoxLayoutParams         = null,
														mButtonsLayoutParams        = null,
														mTextLayoutParams           = null;
	private int 										mBtnBgResid					= 0,    
														mBtnWidth 					= 0, 
														mBtnHeigth 					= 0,
														mMsgBoxWidth 				= 0,
														mMsgBoxHeight 				= 0,
														mMsgBoxX 					= 0, 
														mMsgBoxY 					= 0,
														mScreenWidth 			    = 0,
														mScreenHeight 			    = 0,
														mPanding                    = 0,
														mMsgBoxType                 = 0,
														mMsgBoxAnchor               = 0,
														mTextSize                   = 0,
														mTextGravity                = 0,
														mButtonsTextSize            = 0,
														mBackgroundResid            = 0,
														mPandingBottom              = 0,
														mTextPadingLeft             = 0,
														mTextPadingTop              = 0,
														mTextPadingRight            = 0,
														mTextPadingBottom           = 0,
														mTitleHeight                = 0,
														mTextColor                  = 0,
														mProgressBarWidth           = 0,
														mProgressBarHeigth          = 0;
	private int                                         mMsgboxAutoHideTimeout      = 2000;  //对话框自动隐藏的延时
	private boolean                                     mIsMsgboxAutoHide           = false; //对话框是否自动隐藏
	public static final int 							ANCHOR_TOP_LEFT 			= 0x11, 
														ANCHOR_BUTTOM_LEFT 			= 0x12,
														ANCHOR_CENTER 				= 0x13,
														ANCHOR_TOP_RIGHT 			= 0x14, 
														ANCHOR_BUTTOM_RIGHT 		= 0x15,
														TYPE_ONE_BUTTON             = 0x16,
														TYPE_TWO_BUTTON      		= 0x17,
														TYPE_THREE_BUTTON    		= 0x18,
														HANDLE_AUTOHIDE             = 0x19;
	private  ColorStateList mColorStateListWhite  = null;
	private XmlPullParser mTextColorWhite = null;

	public MsgBox(Context context) {
		super(context);

		mContext = context;
		mMsgBoxLayout = new RelativeLayout(mContext);
		mButtonsLayout = new RelativeLayout(mContext);
		mMainLayout = new RelativeLayout(mContext);
		mInfoTV = new TextView(mContext);
		mTitle = new TextView(mContext);

		mButtons = new Button[3];
		for (int i = 0; i < mButtons.length; i++) {
			mButtons[i] = new Button(mContext);
		}
		//加载默认数据
		initData();
		
	}


	private void initData() {
		
		// 获取屏幕的宽高
		WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		mScreenWidth = windowManager.getDefaultDisplay().getWidth();
		mScreenHeight = windowManager.getDefaultDisplay().getHeight();
	
		//按钮的初始值
//		mBtnBgResid = android.R.drawable.btn_default;
//		mBtnWidth = LayoutParams.WRAP_CONTENT;
//		mBtnHeigth = LayoutParams.WRAP_CONTENT;
		mBtnBgResid = R.drawable.btn_selector;
		mBtnWidth = 184;
		mBtnHeigth = 61;
		
		//屏幕居中
		mMsgBoxAnchor = ANCHOR_CENTER;
		setAnchor(mMsgBoxAnchor);
		mMsgBoxX = mScreenWidth/2;
		mMsgBoxY = mScreenHeight/2;
		
		//MessageBox
//		mMsgBoxWidth = LayoutParams.WRAP_CONTENT;
//		mMsgBoxHeight = LayoutParams.WRAP_CONTENT;
		mMsgBoxWidth = 400;
		mMsgBoxHeight = 300;
		mMsgBoxLayoutParams = new RelativeLayout.LayoutParams(mMsgBoxWidth, mMsgBoxHeight);
		
		mPanding = 5;
		mPandingBottom = 5;
		mTextSize = 16;
		mTextColor = Color.WHITE;
		mTextPadingLeft = mTextPadingRight = 2 * mPanding;
		mTextPadingTop = mTextPadingBottom = mPanding;
		mTextGravity = Gravity.CENTER;
		mButtonsTextSize = 14;
		mMsgBoxType = 0;
		
		mBackgroundResid = R.drawable.main_menu_window_portrait;
		
		mTitleHeight = 50;
		
		mProgressBar = new ProgressBar(mContext,null,android.R.attr.progressBarStyleHorizontal);
		mProgressBar.setMinimumHeight(5);
		mProgressBar.setMax(100);
		mProgressBarWidth = 300;
		mProgressBarHeigth = 5;
		
		//初始化按钮文字的颜色
		 mTextColorWhite = getResources().getXml(R.drawable.textcolor_selector_white);
	
		try {
			mColorStateListWhite = ColorStateList.createFromXml(getResources(), mTextColorWhite);
		} catch (XmlPullParserException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}


	
	/**
	 * 设置MessageBox的大小，MessageBox会根据文字自适应大小，可以不设置
	 * @param width
	 * @param height
	 */
	public void setSize(int width, int height) {
		mMsgBoxWidth = width;
		mMsgBoxHeight = height;
		mMsgBoxLayoutParams = new RelativeLayout.LayoutParams(mMsgBoxWidth, mMsgBoxHeight);
	
	}

	public void setBackgound(int resid){
		mBackgroundResid = resid;
		
	}

	public void setTitle(CharSequence title){
		mIsHasTitle = true;
		mTitle.setText(title);
		mTitle.setTextSize(20);
		mTitle.setTextColor(mTextColor);
		mTitle.setGravity(Gravity.CENTER);
		mTitle.setBackgroundColor(Color.BLACK);
		RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mTitleHeight);
		titleParams.setMargins(mPanding, mPanding, mPanding, 0);
		mMsgBoxLayout.addView(mTitle,titleParams);
	}

	/**
	 * 设置MessageBox要显示的消息
	 * @param msg
	 */
	public void setMessage(CharSequence msg) {
		mMessage = msg;
		addView();
		this.hide();
	}

	/**
	 * 设置MessageBox要显示的消息
	 * @param resid
	 */
	public void setMessage(int resid){
		mMessage = mContext.getResources().getString(resid);
	}
	/**
	 * 设置message的字体大小
	 * @param size
	 */
	public void setMessageTextSize(int size){
		mTextSize = size;
	}
	

	/**
	 * 设置按钮的背景
	 * @param resid
	 */
	public void setButtonBackground(int resid) {
		mBtnBgResid = resid;
		mButtons[0].setBackgroundResource(resid);
		mButtons[1].setBackgroundResource(resid);
		mButtons[2].setBackgroundResource(resid);
		
		
	}

	
	/**
	 * 设置按钮的宽高
	 * @param width
	 * @param height
	 */
	public void setButtonSize(int width, int height) {
		mBtnWidth = width;
		mBtnHeigth = height;
		
	}

	/**
	 * 设置PositiveButton
	 * @param text：按钮文字
	 * @param listener：监听事件
	 */
	public void setPositiveButton(CharSequence text, OnClickListener listener) {
		
		if (mMsgBoxType != TYPE_THREE_BUTTON && mMsgBoxType != TYPE_TWO_BUTTON) {
			mMsgBoxType = TYPE_ONE_BUTTON;
		}
 
		mButtons[0].setText(text);
		mButtons[0].setPadding(0, 0, 0, 0);
		mButtons[0].setTextSize(mButtonsTextSize);
		mButtons[0].setBackgroundResource(mBtnBgResid);
		mButtons[0].setOnClickListener(listener);
		mButtons[0].setTextColor(mColorStateListWhite);
		addView();
		this.hide();
	}
	
	/**
	 * 设置PositiveButton
	 * @param text：按钮文字
	 * @param listener：监听事件
	 */
	public void setPositiveButton(int resid, OnClickListener listener) {

		if (mMsgBoxType != TYPE_THREE_BUTTON && mMsgBoxType != TYPE_TWO_BUTTON) {
			mMsgBoxType = TYPE_ONE_BUTTON;
		}

		mButtons[0].setText(resid);
		mButtons[0].setPadding(0, 0, 0, 0);
		mButtons[0].setTextSize(mButtonsTextSize);
		mButtons[0].setBackgroundResource(mBtnBgResid);
		mButtons[0].setOnClickListener(listener);
		mButtons[0].setTextColor(mColorStateListWhite);
		addView();
		this.hide();
	}

	public void setPositiveButtonLayoutParams(RelativeLayout.LayoutParams layoutParams){
		mButtonsLayoutParams = layoutParams;
		mMsgBoxType = TYPE_ONE_BUTTON;
	}
	
	/**
	 * 设置NegativeButton
	 * @param text：按钮文字
	 * @param listener：按钮的监听事件
	 */
	public void setNegativeButton(CharSequence text, OnClickListener listener) {
		if (mMsgBoxType != TYPE_THREE_BUTTON) {
			mMsgBoxType = TYPE_TWO_BUTTON;
			
		}
		
		mButtons[1].setText(text);
		mButtons[1].setPadding(0, 0, 0, 0);
		mButtons[1].setBackgroundResource(mBtnBgResid);
		mButtons[1].setOnClickListener(listener);
		mButtons[1].setTextColor(mColorStateListWhite);
		addView();
		this.hide();
	}

	/**
	 * 设置NegativeButton
	 * @param text：按钮文字
	 * @param listener：按钮的监听事件
	 */
	public void setNegativeButton(int resid, OnClickListener listener) {
		if (mMsgBoxType != TYPE_THREE_BUTTON) {
			mMsgBoxType = TYPE_TWO_BUTTON;
			
		}
		
		mButtons[1].setText(resid);
		mButtons[1].setPadding(0, 0, 0, 0);
		mButtons[1].setBackgroundResource(mBtnBgResid);
		mButtons[1].setOnClickListener(listener);
		mButtons[1].setTextColor(mColorStateListWhite);
		addView();
		this.hide();
	}
	
	/**
	 * 设置取消按钮
	 * @param text：按钮文字
	 * @param listener：按钮的监听事件
	 */
	public void setCancelButton(CharSequence text, OnClickListener listener) {

		mMsgBoxType = TYPE_THREE_BUTTON;
		mButtons[2].setText(text);
		mButtons[2].setPadding(0, 0, 0, 0);
		mButtons[2].setBackgroundResource(mBtnBgResid);
		mButtons[2].setOnClickListener(listener);
		mButtons[2].setTextColor(mColorStateListWhite);
		addView();
		this.hide();
	}
	
	/**
	 * 设置取消按钮
	 * @param text：按钮文字
	 * @param listener：按钮的监听事件
	 */
	public void setCancelButton(int resid, OnClickListener listener) {

		mMsgBoxType = TYPE_THREE_BUTTON;
		mButtons[2].setText(resid);
		mButtons[2].setPadding(0, 0, 0, 0);
		mButtons[2].setBackgroundResource(mBtnBgResid);
		mButtons[2].setOnClickListener(listener);
		mButtons[2].setTextColor(mColorStateListWhite);
		addView();
		this.hide();
	}

	/**
	 * 设置message文字的Gravity
	 * @param gravity
	 */
	public void setTextGravity(int gravity){
		
		mTextGravity = gravity;
	}
	
	/**
	 * 设置message文字的LayoutParams
	 * @param layoutParams
	 */
	public void setTextLayoutParams(RelativeLayout.LayoutParams layoutParams){
		mTextLayoutParams = layoutParams;
	}
	
	/**
	 * 设置message文字的大小
	 * @param size
	 */
	public void setTextSize(int size){
		mTextSize = size;
	}
	
	/**
	 * 设置message文字的Padding
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 */
	public void setTextPadding(int left,int top,int right,int bottom){
		mTextPadingLeft = left;
		mTextPadingTop = top;
		mTextPadingRight = right;
		mTextPadingBottom = bottom;
	}
	
	/**
	 * 设定窗口是否为模态的，默认为模态
	 * @param isModel
	 */
	public void setModelStatus(boolean isModel){
		mIsModel = isModel;
	}
	
	/**
	 * 设置MsgBox的LayoutParams
	 */
	public void setLayoutParams(RelativeLayout.LayoutParams params) {
		// TODO Auto-generated method stub
		mMsgBoxLayoutParams = params;
	}

	/**
	 * 设置MsgBox底下的全屏背景颜色
	 * @param color
	 */
	public void setLayoutBackgroundColor(int color){
		mMainLayout.setBackgroundColor(color);
	}
	
	/**
	 * 设置MsgBox底下全屏背景颜色的透明度
	 * @param alpha
	 */
	public void setLayoutBackgroundAlpha(int alpha){
		mMainLayout.getBackground().setAlpha(alpha);
	}
	
	/**
	 * 添加到布局
	 * @param layout
	 */
	public void addToLayout(ViewGroup layout) {
		if(layout == null){
			return;
		}
		mParentViewGroup = layout;
		addView();
		this.hide();
	}
	
	public void removeFromLayout(){
		if(mParentViewGroup != null && mMainLayout != null){
			mParentViewGroup.removeView(mMainLayout);
			mParentViewGroup.requestLayout();
		}
	}
	
	/**
	 * 按钮标题，文字，按钮，尾部的顺序添加控件到布局
	 */
	private void addView() {

		//初始化
		mMsgBoxLayout.removeAllViews();
		mButtonsLayout.removeAllViews();
		mMainLayout.removeAllViews();
		
		mLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
	
		if (mIsHasTitle) {
			//在有Title的情况下设定message在title下面
			mTitle.setId(0x1110);
			mLayoutParams.addRule(RelativeLayout.BELOW,0x1110);
			mLayoutParams.setMargins(20, 0, 20, 0);
		}else {
			mLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT); 
			
		}
		
		//如果用户未定义message的布局方式采用默认的居中方式，否则采用的用户的 layoutParams
		if (mTextLayoutParams != null) {
			mLayoutParams = mTextLayoutParams;
		}
		
		
		if (mIsHasProgressBar) {
			
			RelativeLayout.LayoutParams  layoutParams = new RelativeLayout.LayoutParams(mProgressBarWidth, mProgressBarHeigth);
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
			layoutParams.setMargins(10, 20, 10, 20);
			mMsgBoxLayout.addView(mProgressBar,layoutParams);
			mMsgBoxType = 0;
		}
		
		mInfoTV.setTextSize(mTextSize);
		mInfoTV.setText(mMessage);
		mInfoTV.setTextColor(mTextColor);
		mInfoTV.setGravity(mTextGravity);
		mInfoTV.setPadding(mTextPadingLeft, 0,mTextPadingRight, mTextPadingBottom);
		mInfoTV.setVerticalScrollBarEnabled(true);
		mInfoTV.setMovementMethod(ScrollingMovementMethod.getInstance());
		mInfoTV.setLayoutParams(mLayoutParams);
		mMsgBoxLayout.setBackgroundResource(mBackgroundResid);
		mMsgBoxLayout.setPadding(mPanding, mPanding, mPanding, mPanding);
		
		mMsgBoxLayout.addView(mInfoTV);

		
		switch (mMsgBoxType) {
		case MsgBox.TYPE_ONE_BUTTON:
			mButtons[0].setLayoutParams(new RelativeLayout.LayoutParams(mBtnWidth, mBtnHeigth));
			mButtonsLayout.addView(mButtons[0]);
			break;
		case MsgBox.TYPE_TWO_BUTTON:
			mButtons[0].setId(0x1110);
			mLayoutParams = new RelativeLayout.LayoutParams(mBtnWidth, mBtnHeigth);
			mButtons[0].setLayoutParams(new RelativeLayout.LayoutParams(mBtnWidth, mBtnHeigth));
			mLayoutParams.addRule(RelativeLayout.RIGHT_OF,0x1110); 
			mButtonsLayout.addView(mButtons[0]);
			mButtonsLayout.addView(mButtons[1],mLayoutParams);
			break;
		case MsgBox.TYPE_THREE_BUTTON:
			
			mButtons[0].setId(0x1110);
			mButtons[1].setId(0x1111);
			
			mLayoutParams = new RelativeLayout.LayoutParams(mBtnWidth, mBtnHeigth);
			mButtons[0].setLayoutParams(new RelativeLayout.LayoutParams(mBtnWidth, mBtnHeigth));
			mLayoutParams.addRule(RelativeLayout.RIGHT_OF,0x1110); 
			mButtonsLayout.addView(mButtons[0]);
			mButtonsLayout.addView(mButtons[1],mLayoutParams);
	
			mLayoutParams = new RelativeLayout.LayoutParams(mBtnWidth, mBtnHeigth);
			mLayoutParams.addRule(RelativeLayout.RIGHT_OF,0x1111); 
			mButtonsLayout.addView(mButtons[2],mLayoutParams);
			break;
		}

		
		mLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE); 
		mLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		mButtonsLayout.setPadding(mPanding, mPanding, mPanding, mPandingBottom);
		mMsgBoxLayout.setPadding(mPanding, mPanding, mPanding, mPanding);
		
		//设定Confirm按钮的布局参数
		if (mButtonsLayoutParams != null) {
			mLayoutParams = mButtonsLayoutParams;
		}
		mMsgBoxLayout.addView(mButtonsLayout,mLayoutParams);
	
		
		//MessageBox居中显示
		mMsgBoxLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		mMainLayout.addView(mMsgBoxLayout,mMsgBoxLayoutParams);
		
		mMainLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		mMainLayout.setGravity(Gravity.CENTER);
		
		mMainLayout.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				//设定为模式对话框
				return mIsModel;
			}
		});
	}

	/**
	 * 设置定位点
	 * 
	 * @param anchor
	 *            TOP_LEFT | TOP_RIGHT | BUTTOM_LEFT | BUTTOM_RIGHT | CENTER
	 */
	public void setAnchor(int anchor) {// WeiWang
		switch (anchor) {

		case MsgBox.ANCHOR_TOP_LEFT:

			break;
		case MsgBox.ANCHOR_BUTTOM_LEFT:
			mMsgBoxY = mMsgBoxY - mMsgBoxHeight;
			break;
		case MsgBox.ANCHOR_CENTER:
			mMsgBoxX = mMsgBoxX - mMsgBoxWidth / 2;
			mMsgBoxY = mMsgBoxY - mMsgBoxHeight / 2;
			break;
		case MsgBox.ANCHOR_TOP_RIGHT:
			mMsgBoxX = mMsgBoxX - mMsgBoxWidth;

			break;
		case MsgBox.ANCHOR_BUTTOM_RIGHT:
			mMsgBoxX = mMsgBoxX - mMsgBoxWidth;
			mMsgBoxY = mMsgBoxY - mMsgBoxHeight;
			break;
		}
	}

	
	/**
	 *隐藏MessageBox
	 */
	public void hide(){
		if(mMainLayout == null || !isShown()){
			return;
		}
		mMainLayout.setVisibility(View.GONE);
		mMsgBoxIsShow = false;
		removeFromLayout();
	}
	
	/**
	 * 显示MessageBox
	 */
	public void show(){
		if(mMainLayout == null || isShown()){
			return;
		}
		mMainLayout.setVisibility(View.VISIBLE);
		mMsgBoxIsShow = true;
		mParentViewGroup.addView(mMainLayout);
		mParentViewGroup.bringChildToFront(mMainLayout);
		if(mIsMsgboxAutoHide){
			Timer autoHideTimer = new Timer();
			autoHideTimer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					mUIHandler.sendEmptyMessage(HANDLE_AUTOHIDE);
				}
			}, mMsgboxAutoHideTimeout);
		}
	}
	
	public boolean isShown(){
		
		return mMsgBoxIsShow;
	}
	
	public int getProgress(){
		return mProgressBar.getProgress();
	}
	
	public void setRotation(float rotation) {
		// TODO Auto-generated method stub
		
		mMsgBoxLayout.setRotation(rotation);
//		mMainLayout.setRotation(rotation);

	}
	
	@Override
	public void setVisibility(int visibility) {
		// TODO Auto-generated method stub
		mMainLayout.setVisibility(visibility);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		
		mMainLayout.dispatchTouchEvent(event);
		return true;
	}
	
	/**
	 * 设置是否显示进度条控件
	 * @param isShow
	 */
	public void setProgressBarShow(boolean isShow){
		mIsHasProgressBar = isShow;
		
	}
	public void setProgress(int progress){
		if (mIsHasProgressBar) {
			mProgressBar.setProgress(progress);
		}
	}
	
	/**
	 * 更新MsgBox中的文字
	 * @param message
	 */
	public void updateMessage(CharSequence message){
		mMessage = message;
		TextView mTextView = new TextView(mContext);
		mTextView.setText(message);
		mTextView.setTextSize(mTextSize);
		mTextView.setGravity(Gravity.CENTER);
		mTextView.setLayoutParams(mInfoTV.getLayoutParams());
	
		mInfoTV.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.GONE);
		mMsgBoxLayout.addView(mTextView);
		
	}
	
	
	/**
	 * 设置MsgBox消失的动画
	 * @param time
	 */
	public void hideDelay(int time){
		
		AnimationSet animationSet = new AnimationSet(true);
		// 创建一个AlphaAnimation对象
					AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);// 1表示不透明，0表示透明
					// 设置动画执行的时间（单位：毫秒）
					alphaAnimation.setDuration(time);
					// 将AlphaAnimation对象添加到AnimationSet当中
					animationSet.addAnimation(alphaAnimation);
					// 使用ImageView的startAnimation方法开始执行动画
					mMainLayout.startAnimation(alphaAnimation);
					alphaAnimation.setFillAfter(true);
					
					mMsgBoxIsShow = false;
					removeFromLayout();
	}


	/**
	 * 设定对话框自动隐藏
	 * @param isAutoHide
	 * @param mMsgboxAutoHideTimeout
	 */
	public void setAutoHide(boolean isAutoHide, int autoHideTimeout) {
		mMsgboxAutoHideTimeout = autoHideTimeout;
		mIsMsgboxAutoHide      = isAutoHide;
	}

	private Handler mUIHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLE_AUTOHIDE:
				hideDelay(400);
				break;

			default:
				break;
			}
		}
		
	};
}

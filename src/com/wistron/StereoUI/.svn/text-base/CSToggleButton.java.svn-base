package com.wistron.StereoUI;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.BitmapFactory.Options;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;

/**
 * Copyright (c) 2012 Wistron SWPC
 * All rights reserved.
 * @author WH1107028	
 * @date 2012-02-21 12:00:00
 * @comment 
 * @purpose CSToggleButton
 * @detail 
 */
@SuppressWarnings("deprecation")
public class CSToggleButton extends CSView{
	
	protected ToggleButton[] mToggleButtons  = null;
	private boolean          m_dragged       = false;
	private boolean          m_allow_dragged = false;
	private int              mSlipWidth  =0,
			                 mSlipHeight = 0;
	private float            mRotation = 0,mScaleX = 1f,mScaleY = 1f,mSclipX = 0;
	
	private float            mParameter = 0;
	private boolean          mDrawSlip = false;


	public CSToggleButton(Context context) {
		super(context);
		m_context = context;	
		
		mToggleButtons = new ToggleButton[2];
		for (int i = 0; i < mToggleButtons.length; i++) {
			mToggleButtons[i] = new ToggleButton(context);
		}
		
	}
	
	@Override
	public void setVisibility(int visibility) {
		if(mToggleButtons[0] == null || mToggleButtons[1] == null){
			return;
		}

		mToggleButtons[0].setVisibility(visibility);
		mToggleButtons[1].setVisibility(visibility);
	}

	@Override
	public void setEnable(boolean enabled) {
		if(mToggleButtons[0] == null || mToggleButtons[1] == null){
			return;
		}

		mToggleButtons[0].setEnabled(enabled);
		mToggleButtons[1].setEnabled(enabled);
	}

	public void setBackground(int resid){
		if(mToggleButtons[0] == null || mToggleButtons[1] == null){
			return;
		}
		
		mToggleButtons[0].setBackground(resid);
		mToggleButtons[1].setBackground(resid);
	}
	
	public void setOffImage(int resid){
		if(mToggleButtons[0] == null || mToggleButtons[1] == null){
			return;
		}
		
		mToggleButtons[0].setOffImage(resid);
		mToggleButtons[1].setOffImage(resid);
	}
	
	public void setOnImage(int resid){
		if(mToggleButtons[0] == null || mToggleButtons[1] == null){
			return;
		}
		
		mToggleButtons[0].setOnImage(resid);
		mToggleButtons[1].setOnImage(resid);
		
	}
	
	public void setRotation(float rotation){
		if(mToggleButtons[0] == null || mToggleButtons[1] == null){
			return;
		}
		
		mRotation = rotation;
		mToggleButtons[0].setRotation(rotation);
		mToggleButtons[1].setRotation(rotation);
		
	}
	
	public void setSlipSize(int width,int height){
		if(mToggleButtons[0] == null || mToggleButtons[1] == null){
			return;
		}
		
		mSlipWidth = width;
		mSlipHeight = height;
		
		mToggleButtons[0].setSlipSize(width, height);
		mToggleButtons[1].setSlipSize(width, height);
		
	}

	/**
	 * 設置控件為3D模式
	 */
	@Override
	public void setDimension(boolean is3d) {
		super.setDimension(is3d);
		
		
		if(mToggleButtons[0] == null || mToggleButtons[1] == null){
			return;
		}
		
	
		if(mToggleButtons[0].getLayoutParams() != null && mToggleButtons[1].getLayoutParams() != null){
			if(m_cur_width == WRAP_CONTENT || m_cur_height == WRAP_CONTENT){
				mToggleButtons[0].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_lx, m_cur_ly));
				mToggleButtons[1].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_rx, m_cur_ry));
			}else{
				mToggleButtons[0].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_lx, m_cur_ly));
				mToggleButtons[1].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_rx, m_cur_ry));
			}
			mToggleButtons[0].requestLayout();
			mToggleButtons[1].requestLayout();
		}
	
		Log.e("CSToggleButton", "m_cur_width"+m_cur_width+"m_cur_height"+m_cur_height);
		
	
		
		if (is3d) {
			
			mToggleButtons[0].setButtonSize(m_cur_width, m_cur_height);
			mToggleButtons[1].setButtonSize(m_cur_width, m_cur_height);
//			mToggleButtons[0].mSlipOnWidth = mToggleButtons[0].mSlipOnWidth/2;
//			mToggleButtons[1].mSlipOnWidth = mToggleButtons[0].mSlipOnWidth/2;
//			mToggleButtons[0].setSlipSize(mToggleButtons[0].mSlipOnWidth/2, mToggleButtons[0].mSlipOnHeight);
//			mToggleButtons[1].setSlipSize(mToggleButtons[1].mSlipOnWidth/2, mToggleButtons[1].mSlipOnHeight);
			
			
			mDrawSlip = true;
			mToggleButtons[1].mSlipBtnX = 	mToggleButtons[0].mSlipBtnX ;
			
			mToggleButtons[0].setSlipScaleAndRotation(0.5f, 1f,-mRotation);
			mToggleButtons[1].setSlipScaleAndRotation(0.5f, 1f,-mRotation);
			
//			mToggleButtons[0].setButtonScale(0.5f, 1f);
//			mToggleButtons[1].setButtonScale(0.5f, 1f);
		
		}else {
			mToggleButtons[0].setButtonSize(m_cur_width, m_cur_height);
			mToggleButtons[1].setButtonSize(m_cur_width, m_cur_height);
			
			mToggleButtons[0].setSlipScaleAndRotation(1f, 1f,0);
			mToggleButtons[1].setSlipScaleAndRotation(1f, 1f,0);
			
//			mToggleButtons[0].setSlipSize(mToggleButtons[0].mSlipOnWidth*2, mToggleButtons[0].mSlipOnHeight);
//			mToggleButtons[1].setSlipSize(mToggleButtons[1].mSlipOnWidth*2, mToggleButtons[1].mSlipOnHeight);
			
//			mToggleButtons[0].setButtonSize(mToggleButtons[0].mBgOnOriginal.getWidth(),mToggleButtons[0].mBgOnOriginal.getHeight());
//			mToggleButtons[1].setButtonSize(mToggleButtons[1].mBgOnOriginal.getWidth(),mToggleButtons[1].mBgOnOriginal.getHeight());
//			mToggleButtons[0].setSlipSize(mToggleButtons[0].mSlipButtonOriginal.getWidth(), mToggleButtons[0].mSlipButtonOriginal.getHeight());
//			mToggleButtons[1].setSlipSize(mToggleButtons[1].mSlipButtonOriginal.getWidth(), mToggleButtons[1].mSlipButtonOriginal.getHeight());
		}
	}

	
	@Override
	public boolean touchEvent(MotionEvent event) {
		boolean state = false;
		boolean mAllowTouch = false;
		Rect  lRect = new Rect();
		Rect  rRect = new Rect();
		int [] location0 = new int[2];
		int [] location1 = new int[2];
		
		float extendWidth = 0;
		float extendHeight = 0;

		if(mToggleButtons[0] == null || mToggleButtons[1] == null){
			return state;
		}

		mToggleButtons[0].getGlobalVisibleRect(lRect);
		mToggleButtons[1].getGlobalVisibleRect(rRect);
		mToggleButtons[0].getLocationOnScreen(location0);
		mToggleButtons[1].getLocationOnScreen(location1);

		//若控件隐藏，则不响应点击事件
		if(getVisibility()==View.INVISIBLE || getVisibility()==View.GONE){
			return false;
		}
		
		
		if (mRotation == 0) {
			mParameter = 1f;
			
		}else {
			mParameter = 0.3f;
			extendWidth = mToggleButtons[0].getWidth() * mParameter;
			extendHeight = mToggleButtons[0].getWidth() * (1 - mParameter);
		}
	
		if(
				((float)location0[0] <= event.getRawX()/2 - (float)m_offset 
				&& location0[0] + mToggleButtons[0].getWidth() >= event.getRawX()/2 - m_offset
				&& (float)location0[1] - extendHeight/2 <= event.getRawY()
				&& location0[1] + mToggleButtons[0].getHeight() + extendHeight/2 >= event.getRawY())
				||
				((float)location1[0] <= event.getRawX()/2 +(float) m_offset 
				&& location1[0] + mToggleButtons[1].getWidth() >= event.getRawX()/2 + m_offset
				&& (float)location1[1] - extendHeight/2 <= event.getRawY() 
				&& location1[1] + mToggleButtons[1].getHeight() + extendHeight/2 >= event.getRawY())
				)
		{
		
			if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
				mAllowTouch =true;
			}
				event.setLocation( event.getRawX() - m_cur_lx , event.getRawY()  );
				state |= mToggleButtons[0].onTouchEvent(event);
				event.setLocation( event.getRawX() + m_cur_lx , event.getRawY() );				
				
				mDrawSlip = false;
				event.setLocation(event.getRawX() + m_cur_rx - 2*m_cur_lx , event.getRawY() );
				state |= mToggleButtons[1].onTouchEvent(event);
				event.setLocation(event.getRawX() - m_cur_rx + 2*m_cur_lx , event.getRawY() );

		}else{ 
			if(event.getAction() == MotionEvent.ACTION_UP){
				if(mToggleButtons[0].isPressed()){
					mToggleButtons[0].setPressed(false);
					mToggleButtons[1].setPressed(false);
					state |= true;
				}
			}
			

		}
		
		return state;
		
	}
		
//	@Override
//	public boolean touchEvent(MotionEvent event) {
//		boolean state = false;
//		boolean mAllowTouch = false;
//		Rect  lRect = new Rect();
//		Rect  rRect = new Rect();
//		int [] location0 = new int[2];
//		int [] location1 = new int[2];
//		
//		float extendWidth = 0;
//		float extendHeight = 0;
//
//		if(mToggleButtons[0] == null || mToggleButtons[1] == null){
//			return state;
//		}
//
//		mToggleButtons[0].getGlobalVisibleRect(lRect);
//		mToggleButtons[1].getGlobalVisibleRect(rRect);
//		mToggleButtons[0].getLocationOnScreen(location0);
//		mToggleButtons[1].getLocationOnScreen(location1);
//
//		//若控件隐藏，则不响应点击事件
//		if(getVisibility()==View.INVISIBLE || getVisibility()==View.GONE){
//			return false;
//		}
//		
//		
//		if (mRotation == 0) {
//			mParameter = 1f;
//			
//		}else {
//			mParameter = 0.3f;
//			extendWidth = mToggleButtons[0].getWidth() * mParameter;
//			extendHeight = mToggleButtons[0].getWidth() * (1 - mParameter);
//		}
//	
//		if(
//				((float)location0[0] <= event.getRawX()/2 - (float)m_offset 
//				&& location0[0] + mToggleButtons[0].getWidth() >= event.getRawX()/2 - m_offset
//				&& (float)location0[1] - extendHeight/2 <= event.getRawY()
//				&& location0[1] + mToggleButtons[0].getHeight() + extendHeight/2 >= event.getRawY())
//				||
//				((float)location1[0] <= event.getRawX()/2 +(float) m_offset 
//				&& location1[0] + mToggleButtons[1].getWidth() >= event.getRawX()/2 + m_offset
//				&& (float)location1[1] - extendHeight/2 <= event.getRawY() 
//				&& location1[1] + mToggleButtons[1].getHeight() + extendHeight/2 >= event.getRawY())
////		event.getX()<= mToggleButtons[0].getWidth() && event.getY() < mToggleButtons[1].getHeight() + extendHeight
//		
//			
//				)
//		{
//		if (event.getAction() ==  MotionEvent.ACTION_DOWN) {
//			mAllowTouch = true;
//		}
//			
////				event.setLocation( event.getRawX() - m_cur_lx , event.getRawY()  );
////				state |= mToggleButtons[0].onTouchEvent(event);
////				event.setLocation( event.getRawX() + m_cur_lx , event.getRawY() );				
////				
////				mDrawSlip = false;
////				event.setLocation(event.getRawX() + m_cur_rx - 2*m_cur_lx , event.getRawY() );
////				state |= mToggleButtons[1].onTouchEvent(event);
////				event.setLocation(event.getRawX() - m_cur_rx + 2*m_cur_lx , event.getRawY() );
//
//		}
//		
//		
//		if (event.getAction() == MotionEvent.ACTION_UP && mAllowTouch) {
//			
//			mAllowTouch = false;
//			event.setLocation( event.getRawX() - m_cur_lx , event.getRawY()  );
//			state |= mToggleButtons[0].onTouchEvent(event);
//			event.setLocation( event.getRawX() + m_cur_lx , event.getRawY() );				
//			
//			mDrawSlip = false;
//			event.setLocation(event.getRawX() + m_cur_rx - 2*m_cur_lx , event.getRawY() );
//			state |= mToggleButtons[1].onTouchEvent(event);
//			event.setLocation(event.getRawX() - m_cur_rx + 2*m_cur_lx , event.getRawY() );
//		}
//		
//		
//		if (mAllowTouch) {
//			mAllowTouch = false;
//			event.setLocation( event.getRawX() - m_cur_lx , event.getRawY()  );
//			state |= mToggleButtons[0].onTouchEvent(event);
//			event.setLocation( event.getRawX() + m_cur_lx , event.getRawY() );				
//			
//			mDrawSlip = false;
//			event.setLocation(event.getRawX() + m_cur_rx - 2*m_cur_lx , event.getRawY() );
//			state |= mToggleButtons[1].onTouchEvent(event);
//			event.setLocation(event.getRawX() - m_cur_rx + 2*m_cur_lx , event.getRawY() );
//		}
////		else{ 
////			if(event.getAction() == MotionEvent.ACTION_UP){
////				mToggleButtons[0].setPressed(false);
////				mToggleButtons[1].setPressed(false);
////				state |= true;
////			}
//			
//
////		}
//		
//		return state;
//		
//	}
	public void setOnCheckedChangeListener(OnChangedListener l){
		
		if(mToggleButtons[0] == null || mToggleButtons[1] == null){
			return;
		}
		
		mToggleButtons[0].setOnChangedListener(l);
	}
	
	public void setOnClickListener(OnClickListener l){
		if(mToggleButtons[0] == null || mToggleButtons[1] == null){
			return;
		}
		
		mToggleButtons[0].setOnClickListener(l);
		mToggleButtons[1].setOnClickListener(l);
		
	}
	@Override
	public void addToLayout(ViewGroup layout) {
		// TODO Auto-generated method stub
		if(mToggleButtons[0] == null || mToggleButtons[1] == null){
			return;
		}
		
		if(m_is_3D){

		if(m_cur_width == WRAP_CONTENT || m_cur_height == WRAP_CONTENT){
			mToggleButtons[0].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_lx, m_cur_ly));
			mToggleButtons[1].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_rx, m_cur_ry));
		}else{
			mToggleButtons[0].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_lx, m_cur_ly));
			mToggleButtons[1].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_rx, m_cur_ry));
		}
		}
		layout.addView(mToggleButtons[0]);
		layout.addView(mToggleButtons[1]);
		
	}
	
	/**
	 * 获取控件的可见性
	 * @return
	 */
	
	public int getVisibility(){
		int visile = -1;
		if(mToggleButtons[0] == null || mToggleButtons[1] == null){
			visile = -1;
		}

		visile = mToggleButtons[0].getVisibility();
		
		return visile;
	}
	/**
	 * 設置控件的位置
	 */
	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		if(mToggleButtons[0] == null || mToggleButtons[1] == null){
			return;
		}
		
		if(mToggleButtons[0].getLayoutParams() != null && mToggleButtons[1].getLayoutParams() != null){
			if(m_cur_width == WRAP_CONTENT || m_cur_height == WRAP_CONTENT){
				mToggleButtons[0].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_lx, m_cur_ly));
				mToggleButtons[1].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_rx, m_cur_ry));
			}else{
				mToggleButtons[0].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_lx, m_cur_ly));
				mToggleButtons[1].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_rx, m_cur_ry));
			}
			mToggleButtons[0].requestLayout();
			mToggleButtons[1].requestLayout();
		}
	}
	
	@Override
	public void setSize(int width, int height) {
		// TODO Auto-generated method stub
		super.setSize(width, height);
		m_cur_width = width;
		m_cur_height = height;
		if(mToggleButtons[0] == null || mToggleButtons[1] == null){
			return;
		}
		mToggleButtons[0].setButtonSize(width, height);
		mToggleButtons[1].setButtonSize(width, height);
		
	}
	
	
	/**
	 * 拨动按钮的普通版
	 * @author WH1107028
	 *
	 */
	public class ToggleButton extends View  {

		private boolean mNowChoose  = false;//记录当前按钮是否打开,true为打开,flase为关闭
		private boolean mOnSlip     = false;//记录用户是否在滑动的变量
		private float   mDownX      = 0,
				mNowX       = 0;    //按下时的x,当前的x,
		private Rect    mBtnOn     = null,
				mBtnOff    = null; //打开和关闭状态下,游标的Rect
		
		private float mSlipBtnX = 0;

		private boolean mIsChangeListenerOn = false;
		private OnChangedListener mOnChangeListener;

		private Bitmap mBgOn       = null,
				       mBgOff      = null,
				       mSlipOn     = null,
				       mSlipOff    = null,
				       mSlipButton = null,
				
				       mBgOnHalf = null,
				       mBgOffHalf= null,
				       mSlipButtonHalf = null,
				
				       mBgOnOriginal = null,
				       mBgOffOriginal = null,
				       mSlipOnOriginal = null,
				       mSlipOffOriginal = null,
				       mSlipButtonOriginal = null;

		private int    mIdBgOn     = android.R.drawable.toast_frame,
					   mIdBgOff    = android.R.drawable.toast_frame,
					   mIdBtnOn    = android.R.drawable.button_onoff_indicator_on,
					   mIdBtnOff   = android.R.drawable.button_onoff_indicator_off;
		
		private int mBgOnWidth = 0,
				    mBgOnHeight = 0,
				    mSlipOnWidth = 0,
				    mSlipOnHeight = 0;

		public ToggleButton(Context context) {
			super(context);
			init();
		}

		public ToggleButton(Context context, AttributeSet attrs) {
			super(context, attrs);
			init();
		}

		public ToggleButton(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			init();
		}

		private void init(){//初始化
			//载入图片资源
			Options opt = new Options();
			opt.inJustDecodeBounds = false;
//			mBgOn   = BitmapFactory.decodeResource(getResources(), mIdBgOn, opt);
//			mBgOff    = BitmapFactory.decodeResource(getResources(), mIdBgOff, opt);
//			mSlipOn  = BitmapFactory.decodeResource(getResources(), mIdBtnOn, opt);
//			mSlipOff = BitmapFactory.decodeResource(getResources(), mIdBtnOff, opt);
			
			mBgOnOriginal    = BitmapFactory.decodeResource(getResources(), mIdBgOn, opt);
			mBgOffOriginal   = BitmapFactory.decodeResource(getResources(), mIdBgOff, opt);
			mSlipOnOriginal  = BitmapFactory.decodeResource(getResources(), mIdBtnOn, opt);
			mSlipOffOriginal = BitmapFactory.decodeResource(getResources(), mIdBtnOff, opt);
			


			mBgOn = mBgOnOriginal;
			mBgOff = mBgOffOriginal;
			mSlipOn = mSlipOnOriginal;
			mSlipOff = mSlipOffOriginal;
			
			mSlipButton = mSlipOn;

			//获得需要的Rect数据
			mBtnOn  = new Rect(0,0,mSlipButton.getWidth(),mSlipButton.getHeight());
			mBtnOff = new Rect(
					mBgOff.getWidth() - mSlipButton.getWidth(),
					0,
					mBgOff.getWidth(),
					mSlipButton.getHeight());

			this.setPivotX(mBtnOn.width()/2);
			this.setPivotY(mBtnOn.height()/2);
			
			mSlipOnWidth = mSlipOnOriginal.getWidth();
			mSlipOnHeight = mSlipOnOriginal.getHeight();
			

			//设置监听器
//			setOnTouchListener(this);
		}

		public void setScaleThumbX(float scale, float degree){
			
		}
		
		public void setBackground(int resid){
			mIdBgOff  = mIdBgOn = resid;
//			mBgOff    = mBgOn   = BitmapFactory.decodeResource(getResources(), resid);
			mBgOffOriginal    = mBgOnOriginal   = BitmapFactory.decodeResource(getResources(), resid);
		}

		public void setOnImage(int resid){
			mIdBtnOn  = resid;
			mSlipOnOriginal   = BitmapFactory.decodeResource(getResources(), resid);
			init();
		}

		public void setOffImage(int resid){
			mIdBtnOff = resid;
			mSlipOffOriginal  = BitmapFactory.decodeResource(getResources(), resid);
			init();
		}

		public void setPivot(float pivotX, float pivotY){
			this.setPivotX(pivotX);
			this.setPivotY(pivotY);
		}

		@Override
		protected void onDraw(Canvas canvas) {//绘图函数
			mBtnOn  = new Rect(0,0,mSlipButton.getWidth(),mSlipButton.getHeight());
			mBtnOff = new Rect(
					mBgOff.getWidth() - mSlipButton.getWidth(),
					0,
					mBgOff.getWidth(),
					mSlipButton.getHeight());

			super.onDraw(canvas);

			Matrix matrix = new Matrix();
			Paint  paint  = new Paint();
			float  x      = 0f;

			paint.setAntiAlias(true);

			{
				//滑动到前半段与后半段的背景不同
				if(mNowX < (mBgOn.getWidth()/2)){
					canvas.drawBitmap(mBgOff, matrix, paint); //画出关闭时的背景
				}
				else{
					canvas.drawBitmap(mBgOn, matrix, paint);  //画出打开时的背景
				}

			
				
				//定位游标位置
				if(mOnSlip){//正在滑动
					if(mNowX >= mBgOn.getWidth() - mSlipButton.getWidth()/2){//是否划出指定范围
						x = mBgOn.getWidth() - mSlipButton.getWidth(); 
						Log.w("SlipButton_-2", "x = bg.w - sb.w = " + mBgOn.getWidth() + " - " + mSlipButton.getWidth() + " = " + x);
					}
					else{
						x = mNowX - mSlipButton.getWidth()/2;
						Log.w("SlipButton_-1", "x = now.x - sb.w/2 = " + mNowX + " - " + mSlipButton.getWidth()/2 + " = " + x);
					}
				}else{//未滑动
					if(mNowChoose)//根据现在的开关状态设置画游标的位置
						x = mBtnOff.left;
					else
						x = mBtnOn.left;
				}

				//对游标位置进行异常判断...
				if(x < 0){
					x = 0;
//					Log.w("SlipButton_0", "x = bg.w - sb.w = " + mBgOn.getWidth() + " - " + mSlipButton.getWidth() + " = " + x);
				}
				else if(x > mBgOn.getWidth() - mSlipButton.getWidth()){
					x = mBgOn.getWidth() - mSlipButton.getWidth();
					Log.w("SlipButton_1", "x = bg.w - sb.w = " + mBgOn.getWidth() + " - " + mSlipButton.getWidth() + " = " + x);
				}

//				Log.w("SlipButton_2", "x = bg.w - sb.w = " + mBgOn.getWidth() + " - " + mSlipButton.getWidth() + " = " + x);

				//更换游标图片
				if(x < mBgOn.getWidth()/2 - mSlipButton.getWidth()/2){
					mSlipButton = mSlipOn;
				}else{
					mSlipButton = mSlipOff;
				}

//				mSlipBtnX = x;
				
				if (m_is_3D) {
					
					matrix.postScale(mScaleX, mScaleY);
					matrix.postRotate(-mRotation);
					matrix.postTranslate(-4f, 0f);
					mSlipButton = Bitmap.createBitmap(mSlipButton, 0, 0, mSlipButton.getWidth(), mSlipButton.getHeight(), matrix, true);
					if (mDrawSlip) {
						canvas.drawBitmap(mSlipButton, mSlipBtnX, 0, paint);//画出游标.
					}else {
						canvas.drawBitmap(mSlipButton, x, 0, paint);//画出游标.
					}
				}else {
					canvas.drawBitmap(mSlipButton, x, 0, paint);//画出游标.
				}
			}
		}


		@Override
		public boolean onTouchEvent( MotionEvent event) {
			boolean state = false;

			int [] location = new int[2];

			getLocationOnScreen(location);
			if (m_is_3D) {
				event.setLocation(event.getX() - location[0], event.getY() - location[1]);
			}

			Log.e("event.getx()="+event.getX(), "mbnon.getwidth()/2="+mBgOn.getWidth()/2+"event.gety()="+event.getY()+"location[0]="+location[0]+"location[1]="+location[1]);
			switch(event.getAction()){
			case MotionEvent.ACTION_MOVE:
				mNowX = event.getX();
				state = true;
				break;
			case MotionEvent.ACTION_DOWN:
				if(event.getX() > mBgOn.getWidth() || event.getY() >mBgOn.getHeight()){
					return false;
				}else {
					mOnSlip = true;
					mDownX  = event.getX();
					mNowX   = mDownX;
					state = true;
				}
				
				break;
			case MotionEvent.ACTION_UP:
				mOnSlip = false;
				boolean LastChoose = mNowChoose;
				if(event.getX() >= (mBgOn.getWidth() / 2)){
					mNowChoose = true;
				}
				else{
					mNowChoose = false;
				}
				Log.e("up", "mIsChangeListenerOn:"+mIsChangeListenerOn+"LastChoose:"+LastChoose+"mNowChoose"+mNowChoose);
				
				if(mIsChangeListenerOn && (LastChoose != mNowChoose)){
					mOnChangeListener.OnChanged(mNowChoose);
					
					Log.e("", "mischanglistener");
				}
				state = true;
				break;
			default:
				break;
			}

			invalidate();//重画控件
			if (m_is_3D) {
				event.setLocation(event.getX() + location[0], event.getY() + location[1]);
			}

			return state;
		}

		public void setOnChangedListener(OnChangedListener listener){//设置监听器,当状态修改的时候
			mIsChangeListenerOn = true;
			mOnChangeListener   = listener;
		}
		
		public void setButtonSize(int width,int height){
			mBgOnWidth = width;
			mBgOnHeight = height;

					mBgOn = zoomBitmap(mBgOnOriginal, width, height);
					mBgOff = zoomBitmap(mBgOffOriginal, width, height);
					invalidate();
		}
		
		public void setSlipSize(int width,int height){
			
			mSlipOnWidth = width;
			mSlipOnHeight = height;

			mSlipOn = zoomBitmap(mSlipOnOriginal, width, height);
			mSlipOff = zoomBitmap(mSlipOffOriginal, width, height);
			invalidate();
		}
		
		
		public void setHalfScale(){
			//保存压缩后的图片
			Matrix matrix = new Matrix();
			matrix.setScale(0.5f, 1f);
			mSlipButtonHalf = Bitmap.createBitmap(mSlipButton, 0, 0, mSlipButton.getWidth(), mSlipButton.getHeight(), matrix, true);
		}
		
		public void setSlipScaleAndRotation(float sx,float sy,float rotation){
			
			mScaleX = sx;
			mScaleY = sy;
			invalidate();
		}
		
		public void setButtonScale(float sx,float sy){
			Matrix matrix = new Matrix();
			matrix.setScale(sx, sy);
			mBgOn = Bitmap.createBitmap(mBgOn, 0, 0, mBgOn.getWidth(), mBgOn.getHeight(), matrix, true);
			mBgOff = Bitmap.createBitmap(mBgOff, 0, 0, mBgOff.getWidth(), mBgOff.getHeight(), matrix, true);
			invalidate();
			
		}
		public int getSlipWidth(){
			if(mSlipOn != null && mSlipOff != null){
				return mSlipOn.getWidth();
			}else {
				return -1;
			}
			
			
		}
		public int getSlipHeight(){
			if(mSlipOn != null && mSlipOff != null){
				return mSlipOn.getHeight();
			}else {
				return -1;
			}
		}
		
	
		
		//放大缩小图片

		public  Bitmap zoomBitmap(Bitmap bitmap,int w,int h){
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			Matrix matrix = new Matrix();
			float scaleWidht = ((float)w / width);
			float scaleHeight = ((float)h / height);
			matrix.postScale(scaleWidht, scaleHeight);
			Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,
					true);
			return newbmp;
		}
	}

	//事件接口
	public interface OnChangedListener {
		abstract void OnChanged(boolean checkState);
	}

	@Override
	public Object save() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void restore(Object object) {
		// TODO Auto-generated method stub
		
	}

}

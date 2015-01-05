package com.wistron.StereoUI;
 
import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * Copyright(c)2011 Wistron SWPC ALL rights reserved. 
 * created: 2011-09-06  
 * filename:CSSeekBar.java
 * @author :WangWei
 * purpose:立体控件CSSeekBar的封装
 * 
 */
public class CSSeekBar extends CSView{

	protected SeekBar[] m_seekBars      = null;      
	private int       m_style         = android.R.attr.seekBarStyle;
	private boolean   m_dragged       = false;
	private int       m_ambiguous     = 10;
	private boolean   m_allow_dragged = false;
	private int       m_lthumbId      = 0, m_rthumbId = 0;

	public CSSeekBar(Context context) {
		super(context);
		m_context = context;	
		
		m_seekBars = new SeekBar[2];
		for (int i = 0; i < m_seekBars.length; i++) {
			m_seekBars[i] = new SeekBar(context,null,android.R.attr.seekBarStyle);
		}
	}
	/**
	 * 初始化CSSeekBar
	 * @param context
	 * @param style 進度條樣式 （如果設置為默認，請填寫0）
	 */
	public CSSeekBar(Context context,int style) {
		super(context);
		m_context = context;	
		
		if(style == 0){
			style = android.R.attr.seekBarStyle;
		}
		this.m_style = style;
		
		m_seekBars = new SeekBar[2];
		for (int i = 0; i < m_seekBars.length; i++) {
			m_seekBars[i] = new SeekBar(context,null,m_style);
		}
	}

	/**
	 * 設置控件的可見性
	 */
	@Override
	public void setVisibility(int visibility) {
		if(m_seekBars[0] == null || m_seekBars[1] == null){
			return;
		}

		m_seekBars[0].setVisibility(visibility);
		m_seekBars[1].setVisibility(visibility);
	}

	/**
	 * 設置控件的可用性
	 */
	@Override
	public void setEnable(boolean enabled) {
		if(m_seekBars[0] == null || m_seekBars[1] == null){
			return;
		}

		m_seekBars[0].setEnabled(enabled);
		m_seekBars[1].setEnabled(enabled);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void setDimension(boolean is3d) {
		super.setDimension(is3d);
		if(m_seekBars[0] == null || m_seekBars[1] == null){
			return;
		}
		
		if(m_lthumbId != 0){
			if(m_rthumbId == 0){
				m_rthumbId = m_lthumbId;
			}
			setThumbAutoScale(m_lthumbId, m_rthumbId);
		}
		
		if(m_seekBars[0].getLayoutParams() != null && m_seekBars[1].getLayoutParams() != null){
			if(m_cur_width == WRAP_CONTENT || m_cur_height == WRAP_CONTENT){
				m_seekBars[0].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_lx, m_cur_ly));
				m_seekBars[1].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_rx, m_cur_ry));
			}else{
				m_seekBars[0].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_lx, m_cur_ly));
				m_seekBars[1].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_rx, m_cur_ry));
			}
			m_seekBars[0].requestLayout();
			m_seekBars[1].requestLayout();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		if(m_seekBars[0] == null || m_seekBars[1] == null){
			return;
		}
		
		if(m_seekBars[0].getLayoutParams() != null && m_seekBars[1].getLayoutParams() != null){
			if(m_cur_width == WRAP_CONTENT || m_cur_height == WRAP_CONTENT){
				m_seekBars[0].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_lx, m_cur_ly));
				m_seekBars[1].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_rx, m_cur_ry));
			}else{
				m_seekBars[0].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_lx, m_cur_ly));
				m_seekBars[1].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_rx, m_cur_ry));
			}
			m_seekBars[0].requestLayout();
			m_seekBars[1].requestLayout();
		}
	}
	
	/**
	 * 获取控件的可见性
	 * @return
	 */
	public int getVisibility(){
		int visile = View.NO_ID;
		if(m_seekBars[0] == null || m_seekBars[1] == null){
			visile = View.NO_ID;
		}

		visile = m_seekBars[0].getVisibility();

		return visile;
	}

	/**
	 * 返回主进度
	 * @return
	 */
	public int getProgress(){
		if(m_seekBars[0] == null || m_seekBars[1] == null){
			return 0;
		}

		return m_seekBars[0].getProgress();
	}
	
	/**
	 * 返回副进度
	 * @return
	 */
	public int getSeconderyProgress(){
		if(m_seekBars[0] == null || m_seekBars[1] == null){
			return 0;
		}
		
		return m_seekBars[0].getSecondaryProgress();
	}

	/**
	 * 設置控件的觸摸事件
	 */
	@Override
	public boolean touchEvent(MotionEvent event) {
		boolean state = false;

		int [] location0 = new int[2];
		int [] location1 = new int[2];
		if(m_seekBars[0] == null || m_seekBars[1] == null){
			return state;
		}

		m_seekBars[0].getLocationOnScreen(location0);
		m_seekBars[1].getLocationOnScreen(location1);

		//若控件隐藏，则不响应点击事件
		if(getVisibility()==View.INVISIBLE || getVisibility()==View.GONE){
			return false;
		}

		if(m_is_3D){
			//Under 3D mode
			if(
					(location0[0] <= event.getRawX()/2 - m_offset - m_ambiguous
					&& location0[0] + m_seekBars[0].getWidth() >= event.getRawX()/2 - m_offset
					&& location0[1] <= event.getRawY()
					&& location0[1] + m_seekBars[0].getHeight() >= event.getRawY())
					||
					(location1[0] <= event.getRawX()/2 + m_offset + m_ambiguous
					&& location1[0] + m_seekBars[1].getWidth() >= event.getRawX()/2 + m_offset
					&& location1[1] <= event.getRawY()
					&& location1[1] + m_seekBars[1].getHeight() >= event.getRawY())
					)
			{
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					m_allow_dragged = true;
					state |= true;
					return state;
				}
			}
			if(event.getAction() == MotionEvent.ACTION_UP && m_allow_dragged)
			{
				m_allow_dragged = false;
				event.setLocation((event.getX()-m_cur_lx*2f)/2, event.getY());
				state |= m_seekBars[0].onTouchEvent(event);
				state |= m_seekBars[1].onTouchEvent(event);
				event.setLocation((event.getX() + m_cur_lx)/2f, event.getY());
				return state;
			}

			if(m_allow_dragged){
				event.setLocation((event.getX()-m_cur_lx*2f)/2, event.getY());
				state |= m_seekBars[0].onTouchEvent(event);
				state |= m_seekBars[1].onTouchEvent(event);
				event.setLocation((event.getX() + m_cur_lx)/2f, event.getY());
			}
		}else{
			//Under 2D mode
			if(
					location0[0] <= event.getRawX() - m_ambiguous
					&& location0[0] + m_seekBars[0].getWidth() >= event.getRawX()
					&& location0[1] <= event.getRawY()
					&& location0[1] + m_seekBars[0].getHeight() >= event.getRawY()
					)
			{
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					m_allow_dragged = true;
					state |= true;
					return state;
				}
			}
			if(event.getAction() == MotionEvent.ACTION_UP && m_allow_dragged)
			{
				m_allow_dragged = false;
				event.setLocation(event.getX() - m_cur_lx, event.getY());
				state |= m_seekBars[0].onTouchEvent(event);
				state |= m_seekBars[1].onTouchEvent(event);
				event.setLocation(event.getX() + m_cur_lx, event.getY());
				return state;
			}

			if(m_allow_dragged){
				event.setLocation(event.getX() - m_cur_lx, event.getY());
				state |= m_seekBars[0].onTouchEvent(event);
				state |= m_seekBars[1].onTouchEvent(event);
				event.setLocation(event.getX() + m_cur_lx, event.getY());
			}
		}

		return state;
	}

	/**
	 * 將控件添加到layout中去
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void addToLayout(ViewGroup alayout) {
		if(m_seekBars[0] == null || m_seekBars[1] == null){
			return;
		}

		if(alayout == null){
			return;
		}

		//設置控件在屏幕中顯示的位置
		if(m_cur_width == WRAP_CONTENT || m_cur_height == WRAP_CONTENT){
			m_seekBars[0].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_lx, m_cur_ly));
			m_seekBars[1].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_rx, m_cur_ry));
		}else{
			m_seekBars[0].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_lx, m_cur_ly));
			m_seekBars[1].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_rx, m_cur_ry));
		}

		Log.w("CSSeekBar","[addToLayout]w:h:x:y = " + m_cur_width + ", " + m_cur_height  + ", " + m_cur_lx + ", " + m_cur_ly);
		Log.w("CSSeekBar","[addToLayout]w:h:x:y = " + m_cur_width + ", " + m_cur_height  + ", " + m_cur_rx + ", " + m_cur_ry);
		
		alayout.addView(m_seekBars[0]);
		alayout.addView(m_seekBars[1]);
	}

	/**
	 * 設置進度條的的點擊事件
	 * @param l
	 */
	public void setOnClickListener(OnClickListener l){
		if(m_seekBars[0] == null ||m_seekBars[1] == null){
			return;
		}
		
		m_seekBars[0].setOnClickListener(l);
	}

	/**
	 * 設置進度條的進度改變時的監聽事件
	 * @param l
	 */
	public void setOnSeekBarChangeListener(OnSeekBarChangeListener l){
		if(m_seekBars[0] == null ||m_seekBars[1] == null){
			return;
		}
		
		m_seekBars[0].setOnSeekBarChangeListener(l);
	}

	/**
	 * 設置進度條的背景
	 * @param resid
	 */
	public void setBackground(int resid){
		if(m_seekBars[0] == null ||m_seekBars[1] == null){
			return;
		}
		
		m_seekBars[0].setBackgroundResource(resid);
		m_seekBars[1].setBackgroundResource(resid);
	}

	/**
	 * 設置進度條的背景
	 * @param resid
	 */
	public void setBackground(int residL, int residR){
		if(m_seekBars[0] == null ||m_seekBars[1] == null){
			return;
		}
		
		m_seekBars[0].setBackgroundResource(residL);
		m_seekBars[1].setBackgroundResource(residR);
	}

	/**
	 * 設置進度條的最大進度值
	 * @param max
	 */
	public void setMax(int max){
		if(m_seekBars[0] == null || m_seekBars[1] == null){
			return;
		}

		m_seekBars[0].setMax(max);
		m_seekBars[1].setMax(max);
	}

	/**
	 * 設置進度條的進度
	 * @param progress
	 */
	public void setProgress(int progress){
		if(m_seekBars[0] == null || m_seekBars[1] == null){
			return;
		}

		m_seekBars[0].setProgress(progress);
		m_seekBars[1].setProgress(progress);
	}

	/**
	 * 設置能量條的進度
	 * @param progress
	 */
	public void setSeconderyProgress(int seconderyProgress)
	{
		if(m_seekBars[0] == null || m_seekBars[1] == null){
			return;
		}

		m_seekBars[0].setSecondaryProgress(seconderyProgress);
		m_seekBars[1].setSecondaryProgress(seconderyProgress);
	}

	/**
	 * 設置進度條的最小寬度
	 * @param minWidth
	 */
	public void setMinimumWidth(int minWidth){
		if(m_seekBars[0] == null || m_seekBars[1] == null){
			return;
		}

		m_seekBars[0].setMinimumWidth(minWidth);
		m_seekBars[1].setMinimumWidth(minWidth);
	}

	/**
	 * 設置進度條的最小高度
	 * @param minHight
	 */
	public void setMinimumHeight(int minHight){
		if(m_seekBars[0] == null || m_seekBars[1] == null){
			return;
		}
		
		m_seekBars[0].setMinimumHeight(minHight);
		m_seekBars[1].setMinimumHeight(minHight);
	}

	/**
	 * 設置進度條滑塊偏移量
	 * @param thumbOffset
	 */
	public void setThumbOffset(int thumbOffset){
		if(m_seekBars[0] == null || m_seekBars[1] == null){
			return;
		}
		
		m_seekBars[0].setThumbOffset(thumbOffset);
		m_seekBars[1].setThumbOffset(thumbOffset);
	}

	/**
	 * 获取进度条最大值
	 * @return 最大值
	 */
	public int getMax(){
		int max = 0;
		
		if(m_seekBars[0] == null || m_seekBars[1] == null){
			return max;
		}
		max = m_seekBars[0].getMax() >= m_seekBars[1].getMax() ? m_seekBars[0].getMax() : m_seekBars[1].getMax();
		
		return max;
	}

	/**
	 * Sets the thumb that will be drawn at the end of the progress meter within the SeekBar. 
	 * If the thumb is a valid drawable (i.e. not null), half its width will be used as the new thumb offset (@see #setThumbOffset(int)).
	 * @param resid Drawable ID representing the thumb 
	 */
	public void setThumb(int resid){
		if(m_seekBars[0] == null || m_seekBars[1] == null){
			return;
		}

		m_seekBars[0].setThumb(m_context.getResources().getDrawable(resid));
		m_seekBars[1].setThumb(m_context.getResources().getDrawable(resid));
	}

	/**
	 * Sets the thumb that will be drawn at the end of the progress meter within the SeekBar. 
	 * If the thumb is a valid drawable (i.e. not null), half its width will be used as the new thumb offset (@see #setThumbOffset(int)).
	 * @param resid Drawable ID representing the thumb 
	 */
	public void setThumb(int residL, int residR){
		if(m_seekBars[0] == null || m_seekBars[1] == null){
			return;
		}

		m_seekBars[0].setThumb(m_context.getResources().getDrawable(residL));
		m_seekBars[1].setThumb(m_context.getResources().getDrawable(residR));
	}

	/**
	 * Sets the thumb that will be drawn at the end of the progress meter within the SeekBar. 
	 * If the thumb is a valid drawable (i.e. not null), half its width will be used as the new thumb offset (@see #setThumbOffset(int)).
	 * @param resid Drawable ID representing the thumb 
	 */
	public void setThumbAutoScale(int resid){
		Matrix   scaleMatrix       = new Matrix();
		Drawable resDrawable       = m_context.getResources().getDrawable(resid),
				 desDrawable       = null;
		float    scaleFactorWidth  = 1f,
				 scaleFactorHeight = 1f;

		if(m_seekBars[0] == null || m_seekBars[1] == null){
			return;
		}
		
		m_lthumbId = m_rthumbId = resid;

		if(m_is_3D){
			scaleFactorWidth  = ((float)m_cur_width/(float)m_std_width)*0.5f;
			scaleFactorHeight = (float)m_cur_height/(float)m_std_height;
		}else{
			scaleFactorWidth  = (float)m_cur_width/(float)m_std_width;
			scaleFactorHeight = (float)m_cur_height/(float)m_std_height;
		}

		scaleMatrix.setScale(scaleFactorWidth, scaleFactorHeight);
		desDrawable = new BitmapDrawable(Bitmap.createBitmap(
				drawableToBitmap(resDrawable), 
				0, 0, 
				resDrawable.getIntrinsicWidth(), 
				resDrawable.getIntrinsicHeight(), 
				scaleMatrix, true));

		m_seekBars[0].setThumb(desDrawable);
		m_seekBars[1].setThumb(desDrawable);
	}

	/**
	 * Sets the thumb that will be drawn at the end of the progress meter within the SeekBar. 
	 * If the thumb is a valid drawable (i.e. not null), half its width will be used as the new thumb offset (@see #setThumbOffset(int)).
	 * @param resid Drawable ID representing the thumb 
	 */
	public void setThumbAutoScale(int residL, int residR){
		Matrix   scaleMatrix       = new Matrix();
		Drawable resDrawableL      = m_context.getResources().getDrawable(residL),
				 resDrawableR      = m_context.getResources().getDrawable(residR),
				 desDrawableL      = null,
				 desDrawableR      = null;
		float    scaleFactorWidth  = 1f,
				 scaleFactorHeight = 1f;

		if(m_seekBars[0] == null || m_seekBars[1] == null){
			return;
		}
		
		m_lthumbId = residL;
		m_rthumbId = residR;

		if(m_is_3D){
			if(m_cur_width != m_std_width){
				scaleFactorWidth  = ((float)m_cur_width/(float)m_std_width);//*0.5f;因为横竖屏切换的时候，宽度的比率已经为原来的2倍了，因此不必再x0.5F
				scaleFactorHeight = (float)m_cur_height/(float)m_std_height;
			}else{
				scaleFactorWidth  = 0.5f;
				scaleFactorHeight = 1f;
			}
		}else{
			if(m_cur_width != m_std_width){
				scaleFactorWidth  = (float)m_cur_width/(float)m_std_width;
				scaleFactorHeight = (float)m_cur_height/(float)m_std_height;
			}else{
				scaleFactorWidth  = 1f;
				scaleFactorHeight = 1f;
			}
		}

		scaleMatrix.setScale(scaleFactorWidth, scaleFactorHeight);
		desDrawableL = new BitmapDrawable(Bitmap.createBitmap(
				drawableToBitmap(resDrawableL), 
				0, 0, 
				resDrawableL.getIntrinsicWidth(), 
				resDrawableL.getIntrinsicHeight(), 
				scaleMatrix, true));
		desDrawableR = new BitmapDrawable(Bitmap.createBitmap(
				drawableToBitmap(resDrawableR), 
				0, 0, 
				resDrawableR.getIntrinsicWidth(), 
				resDrawableR.getIntrinsicHeight(), 
				scaleMatrix, true));

		m_seekBars[0].setThumb(desDrawableL);
		m_seekBars[1].setThumb(desDrawableR);
	}

	/**
	 * Define the drawable used to draw the progress bar in progress mode.
	 * @param resid the Progress drawable ID 
	 */
	public void setProgressDrawable(int resid){
		if(m_seekBars[0] == null || m_seekBars[1] == null){
			return;
		}

		m_seekBars[0].setProgressDrawable(m_context.getResources().getDrawable(resid));
		m_seekBars[1].setProgressDrawable(m_context.getResources().getDrawable(resid));
	}

	/**
	 * Define the drawable used to draw the progress bar in progress mode.
	 * @param resid the Progress drawable ID 
	 */
	public void setProgressDrawable(int residL, int residR){
		if(m_seekBars[0] == null || m_seekBars[1] == null){
			return;
		}

		m_seekBars[0].setProgressDrawable(m_context.getResources().getDrawable(residL));
		m_seekBars[1].setProgressDrawable(m_context.getResources().getDrawable(residR));
	}

	/**
	 * 设置控件的ID
	 * @param id
	 */
	public void setId(int id) {
		if(m_seekBars[0] == null || m_seekBars[1] == null){
			return;
		}

		m_seekBars[0].setId(id);
		m_seekBars[1].setId(id);
	}
	
	public void setLayoutParams(LayoutParams lParams, LayoutParams rParams){
		if(m_seekBars[0] == null || m_seekBars[1] == null){
			return;
		}
		
		m_seekBars[0].setLayoutParams(lParams);
		m_seekBars[1].setLayoutParams(rParams);
	}

	private Bitmap drawableToBitmap(Drawable drawable)
	{
		int           width  = 0,
				      height = 0;
		Bitmap.Config config = null;
		Bitmap        bitmap = null;
		Canvas        canvas = null;
		
		width  = drawable.getIntrinsicWidth();
		height = drawable.getIntrinsicHeight();
		config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
		bitmap = Bitmap.createBitmap(width, height, config);
		canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);

		return bitmap;
	}

	/**
	 * 设置控件内部的内容与控件的边距
	 */
	@Override
	public void setPadding(int left, int top, int right, int bottom) {
		if(m_seekBars[0] == null || m_seekBars[1] == null){
			return;
		}

		m_seekBars[0].setPadding(left, top, right, bottom);
		m_seekBars[1].setPadding(left, top, right, bottom);
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
	
	public void setd(){
		
		if(m_seekBars[0] == null || m_seekBars[1] == null){
			return;
		}
		
	
		
	}
	
	/**
	 * 設置SeekBar旋轉的角度
	 * @param degree
	 */
	public void setRotation(float degree){
		if(m_seekBars[0] == null || m_seekBars[1] == null){
			return;
		}

		m_seekBars[0].setRotation(degree);
		m_seekBars[1].setRotation(degree);
	}
	
	public void setRotationX(float degree){
		if(m_seekBars[0] == null || m_seekBars[1] == null){
			return;
		}

		m_seekBars[0].setRotationX(degree);
		m_seekBars[1].setRotationX(degree);
	}
	
	public void setRotationY(float degree){
		if(m_seekBars[0] == null || m_seekBars[1] == null){
			return;
		}

		m_seekBars[0].setRotationY(degree);
		m_seekBars[1].setRotationY(degree);
	}
	
//	public void setPivotX(float pivotX){
//		float pivotX1 = 0f,
//			  pivotX2 = 0f;
//		if(m_seekBars[0] == null || m_seekBars[1] == null){
//			return;
//		}
//
//		{//坐标换算
//			pivotX1 = pivotX;
//			pivotX2 = pivotX;
//		}
//		
//		m_seekBars[0].setPivotX(pivotX1);
//		m_seekBars[1].setPivotX(pivotX2);
//	}
//	
//	public void setPivotY(float pivotY){
//		float pivotY1 = 0f,
//			  pivotY2 = 0f;
//		if(m_seekBars[0] == null || m_seekBars[1] == null){
//			return;
//		}
//
//		{//坐标换算
//			pivotY1 = pivotY;
//			pivotY2 = pivotY;
//		}
//		
//		m_seekBars[0].setPivotX(pivotY1);
//		m_seekBars[1].setPivotX(pivotY2);
//	}
	
	/**
	 * 獲取進度條的角度
	 * @return
	 */
	public float getRotation(){
		float degree = 0f;
		if(m_seekBars[0] == null || m_seekBars[1] == null){
			return degree;
		}
		
		degree = m_seekBars[0].getRotation();
		
		return degree;
	}
	
	public float getRotationX(){
		float degreeX = 0f;
		if(m_seekBars[0] == null || m_seekBars[1] == null){
			return degreeX;
		}
		
		degreeX = m_seekBars[0].getRotationX();
		
		return degreeX;
	}
	
	public float getRotationY(){
		float degreeY = 0f;
		if(m_seekBars[0] == null || m_seekBars[1] == null){
			return degreeY;
		}
		
		degreeY = m_seekBars[0].getRotationY();
		
		return degreeY;
	}

}

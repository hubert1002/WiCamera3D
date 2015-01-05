package com.wistron.StereoUI;


import android.content.Context;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AbsoluteLayout;
import android.widget.Button;

/**
 * Copyright(c)2011 Wistron SWPC ALL rights reserved. 
 * created: 2011-09-01  
 * filename:CSButton.java
 * @author :WH1107028
 * purpose:立体控件CSButton的封装
 * 
 */
@SuppressWarnings("deprecation")
public class CSButton extends CSView{
	protected Button[] m_buttons = null;

	public CSButton(Context context) {
		super(context);
		m_context = context;
		m_buttons = new Button[2];
		for (int i = 0; i < m_buttons.length; i++) {
			m_buttons[i] = new Button(context);
		}
	}

	/**
	 * 設置控件的可见性
	 * 
	 * @param visibility
	 */
	@Override
	public void setVisibility(int visibility) {
		if(m_buttons[0] == null || m_buttons[1] == null){
			return;
		}

		m_buttons[0].setVisibility(visibility);
		m_buttons[1].setVisibility(visibility);
	}

	/**
	 * 設置控件的可用性
	 * @param enabled
	 */
	@Override
	public void setEnable(boolean enabled) {
		if(m_buttons[0] == null || m_buttons[1] == null){
			return;
		}

		m_buttons[0].setEnabled(enabled);
		m_buttons[1].setEnabled(enabled);
	}

	/**
	 * 設置控件為3D模式
	 */
	@Override
	public void setDimension(boolean is3d) {
		super.setDimension(is3d);
		if(m_buttons[0] == null || m_buttons[1] == null){
			return;
		}
		
		if(m_buttons[0].getLayoutParams() != null && m_buttons[1].getLayoutParams() != null){
			if(m_cur_width == WRAP_CONTENT || m_cur_height == WRAP_CONTENT){
				m_buttons[0].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_lx, m_cur_ly));
				m_buttons[1].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_rx, m_cur_ry));
			}else{
				m_buttons[0].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_lx, m_cur_ly));
				m_buttons[1].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_rx, m_cur_ry));
			}
			m_buttons[0].requestLayout();
			m_buttons[1].requestLayout();
		}
	}

	/**
	 * 設置控件的位置
	 */
	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		if(m_buttons[0] == null || m_buttons[1] == null){
			return;
		}
		
		if(m_buttons[0].getLayoutParams() != null && m_buttons[1].getLayoutParams() != null){
			if(m_cur_width == WRAP_CONTENT || m_cur_height == WRAP_CONTENT){
				m_buttons[0].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_lx, m_cur_ly));
				m_buttons[1].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_rx, m_cur_ry));
			}else{
				m_buttons[0].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_lx, m_cur_ly));
				m_buttons[1].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_rx, m_cur_ry));
			}
			m_buttons[0].requestLayout();
			m_buttons[1].requestLayout();
		}
	}

	@Deprecated
	@Override
	public void setLocation(int x, int y, int screenWidth, int screenHeight) {
		super.setLocation(x, y, screenWidth, screenHeight);
		if(m_buttons[0] == null || m_buttons[1] == null){
			return;
		}
		if(m_buttons[0].getLayoutParams() != null && m_buttons[1].getLayoutParams() != null){
			m_buttons[0].requestLayout();
			m_buttons[1].requestLayout();
		}
	}

	/**
	 * 設置控件背景
	 * @param enabled
	 */
	public void setBackground(int resid){
		if(m_buttons[0] == null || m_buttons[1] == null){
			return;
		}

		m_buttons[0].setBackgroundResource(resid);
		m_buttons[1].setBackgroundResource(resid);
	}
	
	/**
	 * 設置控件背景
	 * @param enabled
	 */
	public void setBackground(int residL, int residR){
		if(m_buttons[0] == null || m_buttons[1] == null){
			return;
		}

		m_buttons[0].setBackgroundResource(residL);
		m_buttons[1].setBackgroundResource(residR);
	}

	/**
	 * 设置控件文本
	 * @param text CharSequence
	 */
	public void setText(CharSequence text) {
		if(m_buttons[0] == null || m_buttons[1] == null){
			return;
		}

		m_buttons[0].setText(text);
		m_buttons[1].setText(text);
	}
	
	/**
	 * 设置控件文本
	 * @param text int
	 */
	public void setText(int resid) {
		if(m_buttons[0] == null || m_buttons[1] == null){
			return;
		}

		m_buttons[0].setText(resid);
		m_buttons[1].setText(resid);
	}
	
	/**
	 * Set the default text size to the given value, interpreted as "scaled pixel" units. This size is adjusted based on the current density and user font size preference.
	 * @param size
	 */
	public void setTextSize(float size){
		if(m_buttons[0] == null || m_buttons[1] == null){
			return;
		}

		m_buttons[0].setTextSize(size);
		m_buttons[1].setTextSize(size);
	}
	
	/**
	 * Sets the text color for all the states (normal, selected, focused) to be this color.
	 * @param color
	 */
	public void setTextColor(int color) {
		if(m_buttons[0] == null || m_buttons[1] == null){
			return;
		}

		m_buttons[0].setTextColor(color);
		m_buttons[1].setTextColor(color);
	}
	
	/**
	 * Sets the extent by which text should be stretched horizontally.
	 * @param size
	 */
	public void setTextScaleX(float size){
		if(m_buttons[0] == null || m_buttons[1] == null){
			return;
		}

		m_buttons[0].setTextScaleX(size);
		m_buttons[1].setTextScaleX(size);
	}
	
	/**
	 * 設置onClick事件監聽
	 * @param l
	 * @return
	 */
	public void setOnClickListener(OnClickListener l) {
		if(m_buttons[0] == null || m_buttons[1] == null){
			return;
		}

		m_buttons[0].setOnClickListener(l);
	}
	
	/**
	 * 设置控件的ID
	 * @param id
	 */
	public void setId(int id) {
		if(m_buttons[0] == null || m_buttons[1] == null){
			return;
		}

		m_buttons[0].setId(id);
	}
	
	public void setGravity(int gravity){
		if(m_buttons[0] == null || m_buttons[1] == null){
			return;
		}
		
		m_buttons[0].setGravity(gravity);
		m_buttons[1].setGravity(gravity);
	}

	/**
	 * 设置按钮中的文字和按钮的边距
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 */
	@Override
	public void setPadding(int left,int top,int right,int bottom){
		if(m_buttons[0] == null || m_buttons[1] == null){
			return;
		}
		
		super.setPadding(left, top, right, bottom);
		
		m_buttons[0].setPadding(m_pad_lleft, m_pad_ltop, m_pad_lright, m_pad_lbottom);
		m_buttons[1].setPadding(m_pad_rleft, m_pad_rtop, m_pad_rright, m_pad_rbottom);
	}
	
	/**
	 * setLayoutParams
	 * @param lParams left
	 * @param rParams right
	 */
	public void setLayoutParams(LayoutParams lParams, LayoutParams rParams){
		if(m_buttons[0] == null || m_buttons[1] == null){
			return;
		}
		
		m_buttons[0].setLayoutParams(lParams);
		m_buttons[1].setLayoutParams(rParams);
	}
	
	/**
	 * 获取控件的可见性
	 * @return
	 */
	public int getVisibility(){
		int visile = -1;
		if(m_buttons[0] == null || m_buttons[1] == null){
			visile = -1;
		}

		visile = m_buttons[0].getVisibility();
		
		return visile;
	}
	
	/**
	 * 取得控件的id
	 * @return if values is -1, then cannot get id, otherwise success
	 */
	public int getId(){
		if(m_buttons[0] == null || m_buttons[1] == null){
			return View.NO_ID;
		}

		return m_buttons[0].getId();
	}
	
	/**
	 * 获取子元素
	 * @param which  0 = 获取左屏元素，1 = 获取右屏元素
	 * @return 当没有找到控件或错误时，返回null
	 */
	public Button getChild(int which){
		Button res = null;
		
		switch (which) {
		case 0:
			if(m_buttons[0] != null){
				res = m_buttons[0];
			}
			
			break;

		case 1:
			if(m_buttons[1] != null){
				res = m_buttons[1];
			}
			break;
		}
		
		return res;
	}
	
	/**
	 * 判断按钮是否被按下
	 * @return
	 */
	public boolean isPressed() {
		boolean state = false;
		
		if(m_buttons[0] == null || m_buttons[1] == null){
			return false;
		}
		state |= m_buttons[0].isPressed();
		state |= m_buttons[1].isPressed();

		return state;
	}
	
	/**
	 * Start the specified animation now.
	 * @param anim the animation to start now 
	 */
	public void startAnimation(Animation anim) {
		if(m_buttons[0] == null || m_buttons[1] == null){
			return;
		}
		
		m_buttons[0].startAnimation(anim);
		m_buttons[1].startAnimation(anim);
	}
	
	/**
	 * Start the specified animation now.
	 * @param anim the animation to start now 
	 */
	public void startAnimation(Animation animL, Animation animR) {
		if(m_buttons[0] == null || m_buttons[1] == null){
			return;
		}
		
		m_buttons[0].startAnimation(animL);
		m_buttons[1].startAnimation(animR);
	}
	
	/**
	 * Call this view's OnClickListener, if it is defined.
	 * @return True there was an assigned OnClickListener that was called, false otherwise is returned. 
	 */
	public boolean performClick(){
		boolean state = false;
		
		if(m_buttons[0] == null || m_buttons[1] == null){
			return false;
		}
		state |= m_buttons[0].performClick();
		
		return state;
	}
	
	protected void computeDimension(boolean is3D){
		super.setDimension(is3D);
	}
	
	public void requestLayout(){
		if(m_buttons[0] == null || m_buttons[1] == null){
			return;
		}
		
		m_buttons[0].requestLayout();
		m_buttons[1].requestLayout();
	}
	
	/**
	 * 觸摸事件
	 * @param event
	 * @return
	 */
	@Override
	public boolean touchEvent(final MotionEvent ev) {
		boolean state = false;
		MotionEvent event = MotionEvent.obtain(ev);
		Rect  lRect = new Rect();
		Rect  rRect = new Rect();
		int [] location0 = new int[2];
		int [] location1 = new int[2];

		if(m_buttons[0] == null || m_buttons[1] == null){
			return state;
		}

		m_buttons[0].getGlobalVisibleRect(lRect);
		m_buttons[1].getGlobalVisibleRect(rRect);
		m_buttons[0].getLocationOnScreen(location0);
		m_buttons[1].getLocationOnScreen(location1);

		//若控件隐藏，则不响应点击事件
		if(getVisibility() == View.INVISIBLE || getVisibility() == View.GONE){
			return false;
		}

		if(m_is_3D){
			//Under 3D mode
			if(
					(location0[0] <= event.getRawX()/2 - m_offset
					&& location0[0] + m_buttons[0].getWidth() >= event.getRawX()/2 - m_offset
					&& location0[1] <= event.getRawY()
					&& location0[1] + m_buttons[0].getHeight() >= event.getRawY())
					||
					(location1[0] <= event.getRawX()/2 + m_offset
					&& location1[0] + m_buttons[1].getWidth() >= event.getRawX()/2 + m_offset
					&& location1[1] <= event.getRawY()
					&& location1[1] + m_buttons[1].getHeight() >= event.getRawY())
					)
			{
				if(event.getAction() != MotionEvent.ACTION_MOVE)
				{
					state |= m_buttons[0].onTouchEvent(event);
					state |= m_buttons[1].onTouchEvent(event);
				}

			}else{ 
				if(event.getAction() == MotionEvent.ACTION_UP){
					//m_buttons[0].onTouchEvent(event);
					//m_buttons[1].onTouchEvent(event);
					if(m_buttons[0].isPressed()){
						m_buttons[0].setPressed(false);
						m_buttons[1].setPressed(false);
						state |= true;
					}
				}
			}
		}else{
			//Under 2D mode
			if(
					location0[0] <= event.getRawX()
					&& location0[0] + m_buttons[0].getWidth() >= event.getRawX()
					&& location0[1] <= event.getRawY()
					&& location0[1] + m_buttons[0].getHeight() >= event.getRawY())
			{
				if(event.getAction() != MotionEvent.ACTION_MOVE)
				{
					state |= m_buttons[0].onTouchEvent(event);
					state |= m_buttons[1].onTouchEvent(event);
				}

			}else{ 
				if(event.getAction() == MotionEvent.ACTION_UP){
					//m_buttons[0].onTouchEvent(event);
					//m_buttons[1].onTouchEvent(event);
					m_buttons[0].setPressed(false);
					m_buttons[1].setPressed(false);
					state |= true;
				}
			}
		}

		return state;
	}

	/**
	 * 把本Group添加至指定的ViewGroup
	 * @param layout
	 * @return
	 */
	public void addToLayout(ViewGroup alayout) {
		float scaleRate = 1f;
		if(m_buttons[0] == null || m_buttons[1] == null){
			return;
		}

		if(alayout == null){
			return;
		}
		
		scaleRate = m_scale_rate_w < m_scale_rate_h ? m_scale_rate_w : m_scale_rate_h;
		m_buttons[0].setTextSize(m_buttons[0].getTextSize() * scaleRate);
		m_buttons[1].setTextSize(m_buttons[1].getTextSize() * scaleRate);
		
		if(m_is_3D){
			m_buttons[0].setTextScaleX(0.5f);
			m_buttons[1].setTextScaleX(0.5f);
		}else{
			m_buttons[0].setTextScaleX(1f);
			m_buttons[1].setTextScaleX(1f);
		}

		if(m_cur_width == WRAP_CONTENT || m_cur_height == WRAP_CONTENT){
			m_buttons[0].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_lx, m_cur_ly));
			m_buttons[1].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_rx, m_cur_ry));
		}else{
			m_buttons[0].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_lx, m_cur_ly));
			m_buttons[1].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_rx, m_cur_ry));
		}
		
		alayout.addView(m_buttons[0]);
		alayout.addView(m_buttons[1]);
	}

	/**
	 * 保存立體控件的狀態
	 */
	@Override
	public Object save() {
		// TODO Auto-generated method stub
		return null;
	}



	/**
	 * 讀取立體控件的狀態
	 */
	@Override
	public void restore(Object object) {
		// TODO Auto-generated method stub
		
	}

}

package com.wistron.StereoUI;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsoluteLayout;
import android.widget.RadioButton;

/**
 * Copyright(c)2011 Wistron SWPC ALL rights reserved. 
 * created: 2011-09-10  
 * filename:CSRadioButton.java
 * @author :WangWei
 * purpose:立体控件CSRadioButton的封装
 * 
 */
@SuppressWarnings("deprecation")
public class CSRadioButton extends CSView {

	protected RadioButton[] m_radioButtons = null;

	public CSRadioButton(Context context){
		super(context);
		m_radioButtons = new RadioButton[2];	
		for (int i = 0; i < m_radioButtons.length; i++) {
			m_radioButtons[i] = new RadioButton(context);
		}
	}

	/**
	 * 設置控件的可见性
	 * 
	 * @param visibility
	 */
	@Override
	public void setVisibility(int visibility) {
		if(m_radioButtons[0] == null || m_radioButtons[1] == null){
			return;
		}

		m_radioButtons[0].setVisibility(visibility);
		m_radioButtons[1].setVisibility(visibility);
	}

	/**
	 * 設置控件的可用性
	 * @param enabled
	 */
	@Override
	public void setEnable(boolean enabled) {
		if(m_radioButtons[0] == null || m_radioButtons[1] == null){
			return;
		}

		m_radioButtons[0].setEnabled(enabled);
		m_radioButtons[1].setEnabled(enabled);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void setDimension(boolean is3d) {
		super.setDimension(is3d);
		if(m_radioButtons[0] == null || m_radioButtons[1] == null){
			return;
		}
		
		if(m_is_3D){
			m_radioButtons[0].setTextScaleX(0.5f);
			m_radioButtons[1].setTextScaleX(0.5f);
		}else{
			m_radioButtons[0].setTextScaleX(1f);
			m_radioButtons[1].setTextScaleX(1f);
		}
		
		if(m_radioButtons[0].getLayoutParams() != null && m_radioButtons[1].getLayoutParams() != null){
			if(m_cur_width == WRAP_CONTENT || m_cur_height == WRAP_CONTENT){
				m_radioButtons[0].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_lx, m_cur_ly));
				m_radioButtons[1].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_rx, m_cur_ry));
			}else{
				m_radioButtons[0].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_lx, m_cur_ly));
				m_radioButtons[1].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_rx, m_cur_ry));
			}
			m_radioButtons[0].requestLayout();
			m_radioButtons[1].requestLayout();
		
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		if(m_radioButtons[0] == null || m_radioButtons[1] == null){
			return;
		}
		if(m_radioButtons[0].getLayoutParams() != null && m_radioButtons[1].getLayoutParams() != null){
			if(m_cur_width == WRAP_CONTENT || m_cur_height == WRAP_CONTENT){
				m_radioButtons[0].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_lx, m_cur_ly));
				m_radioButtons[1].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_rx, m_cur_ry));
			}else{
				m_radioButtons[0].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_lx, m_cur_ly));
				m_radioButtons[1].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_rx, m_cur_ry));
			}
			m_radioButtons[0].requestLayout();
			m_radioButtons[1].requestLayout();
		}
	}

	/**
	 * 设置控件文本
	 * @param text CharSequence
	 */
	public void setText(CharSequence text) {
		if(m_radioButtons[0] == null || m_radioButtons[1] == null){
			return;
		}

		m_radioButtons[0].setText(text);
		m_radioButtons[1].setText(text);
	}

	/**
	 * 设置控件文本
	 * @param text
	 */
	public void setText(int resid) {
		if(m_radioButtons[0] == null || m_radioButtons[1] == null){
			return;
		}

		m_radioButtons[0].setText(resid);
		m_radioButtons[1].setText(resid);
	}

	/**
	 * 設置控件背景
	 * @param resid
	 */
	public void setBackground(int resid){
		if(m_radioButtons[0] == null || m_radioButtons[1] == null){
			return;
		}

		m_radioButtons[0].setBackgroundResource(resid);
		m_radioButtons[1].setBackgroundResource(resid);
	}

	/**
	 * Set the default text size to the given value, interpreted as "scaled pixel" units. This size is adjusted based on the current density and user font size preference.
	 * @param size
	 */
	public void setTextSize(float size){
		if(m_radioButtons[0] == null || m_radioButtons[1] == null){
			return;
		}

		m_radioButtons[0].setTextSize(size);
		m_radioButtons[1].setTextSize(size);
	}


	/**
	 * Sets the text color for all the states (normal, selected, focused) to be this color.
	 * @param color
	 */
	public void setTextColor(int color) {
		if(m_radioButtons[0] == null || m_radioButtons[1] == null){
			return;
		}

		m_radioButtons[0].setTextColor(color);
		m_radioButtons[1].setTextColor(color);
	}

	/**
	 * 获取控件的可见性
	 * @return
	 */
	public int getVisibility(){
		int visile = View.NO_ID;
		if(m_radioButtons[0] == null || m_radioButtons[1] == null){
			visile = View.NO_ID;
		}

		visile = m_radioButtons[0].getVisibility();

		return visile;
	}

	/**
	 * 觸摸事件
	 * @param event
	 * @return
	 */
	@Override
	public boolean touchEvent(MotionEvent event) {
		boolean state = false;
		int [] location0 = new int[2];
		int [] location1 = new int[2];
		if(m_radioButtons[0] == null || m_radioButtons[1] == null){
			return state;
		}

		m_radioButtons[0].getLocationOnScreen(location0);
		m_radioButtons[1].getLocationOnScreen(location1);

		//若控件隐藏，则不响应点击事件
		if(getVisibility()==View.INVISIBLE || getVisibility()==View.GONE){
			return false;
		}

		if(m_is_3D){
			//Under 3D mode
			if(
					(location0[0] <= event.getRawX()/2 - m_offset
					&& location0[0] + m_radioButtons[0].getWidth() >= event.getRawX()/2 - m_offset
					&& location0[1] <= event.getRawY()
					&& location0[1] + m_radioButtons[0].getHeight() >= event.getRawY())
					||
					(location1[0] <= event.getRawX()/2 + m_offset
					&& location1[0] + m_radioButtons[1].getWidth() >= event.getRawX()/2 + m_offset
					&& location1[1] <= event.getRawY()
					&& location1[1] + m_radioButtons[1].getHeight() >= event.getRawY()))
			{
				if(event.getAction() != MotionEvent.ACTION_MOVE)
				{
					state |= m_radioButtons[0].onTouchEvent(event);
					state |= m_radioButtons[1].onTouchEvent(event);
				}
			}else{
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					state |= m_radioButtons[0].onTouchEvent(event);
					state |= m_radioButtons[1].onTouchEvent(event);
				}
			}
		}else{
			//Under 2D mode
			if(
					location0[0] <= event.getRawX()
					&& location0[0] + m_radioButtons[0].getWidth() >= event.getRawX()
					&& location0[1] <= event.getRawY()
					&& location0[1] + m_radioButtons[0].getHeight() >= event.getRawY()
					)
			{
				if(event.getAction() != MotionEvent.ACTION_MOVE)
				{
					state |= m_radioButtons[0].onTouchEvent(event);
					state |= m_radioButtons[1].onTouchEvent(event);
				}
			}else{
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					state |= m_radioButtons[0].onTouchEvent(event);
					state |= m_radioButtons[1].onTouchEvent(event);
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
	@SuppressWarnings("deprecation")
	public void addToLayout(ViewGroup alayout) {
		float scaleRate = 1f;
		
		if(m_radioButtons[0] == null || m_radioButtons[1] == null){
			return;
		}

		if(alayout == null){
			return;
		}
		
		scaleRate = m_scale_rate_w < m_scale_rate_h ? m_scale_rate_w : m_scale_rate_h;
		m_radioButtons[0].setTextSize(m_radioButtons[0].getTextSize() * scaleRate);
		m_radioButtons[1].setTextSize(m_radioButtons[1].getTextSize() * scaleRate);

		if(m_is_3D){
			m_radioButtons[0].setTextScaleX(0.5f);
			m_radioButtons[1].setTextScaleX(0.5f);
		}else{
			m_radioButtons[0].setTextScaleX(1f);
			m_radioButtons[1].setTextScaleX(1f);
		}

		if(m_cur_width == WRAP_CONTENT || m_cur_height == WRAP_CONTENT){
			m_radioButtons[0].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_lx, m_cur_ly));
			m_radioButtons[1].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_rx, m_cur_ry));
		}else{
			m_radioButtons[0].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_lx, m_cur_ly));
			m_radioButtons[1].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_rx, m_cur_ry));
		}
		
		alayout.addView(m_radioButtons[0]);
		alayout.addView(m_radioButtons[1]);
	}


	/**
	 * 设置控件的ID
	 * @param id
	 */
	public void setId(int id) {
		if(m_radioButtons[0] == null || m_radioButtons[1] == null){
			return;
		}

		m_radioButtons[0].setId(id);
	}
	/**
	 * 取得控件的id
	 * @return if values is -1, then cannot get id, otherwise success
	 */
	public int getId(){
		if(m_radioButtons[0] == null || m_radioButtons[1] == null){
			return -1;
		}

		return m_radioButtons[0].getId();
	}

	/**
	 * 设置按钮中的文字和按钮的边距
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 */
	public void setPanding(int left,int top,int right,int bottom){
		if(m_radioButtons[0] == null || m_radioButtons[1] == null){
			return;
		}
		
		m_radioButtons[0].setPadding(left, top, right, bottom);
		m_radioButtons[1].setPadding(left, top, right, bottom);
	}

	/**
	 *设置控件的点击事件 
	 * @param l
	 */
	public void setOnClickListener(OnClickListener l){
		if(m_radioButtons[0] == null || m_radioButtons[1] == null){
			return;
		}
		
		m_radioButtons[0].setOnClickListener(l);
	}

	/**
	 * 设置控件为被选中状态
	 * @param checked
	 */
	public void setChecked(boolean checked){
		if(m_radioButtons[0] == null || m_radioButtons[1] == null){
			return;
		}
		
		m_radioButtons[0].setChecked(checked);
		m_radioButtons[1].setChecked(checked);
	}

	/**
	 * 设置控件状态改变事件
	 * @param l
	 */
	public void setOnCheckedChangeListener(android.widget.CompoundButton.OnCheckedChangeListener l){
		if(m_radioButtons[0] == null || m_radioButtons[1] == null){
			return;
		}
		
		m_radioButtons[0].setOnCheckedChangeListener(l);
	}

	public void setButtonDrawable(int resid){
		if(m_radioButtons[0] == null || m_radioButtons[1] == null){
			return;
		}
		
		m_radioButtons[0].setButtonDrawable(resid);
		m_radioButtons[1].setButtonDrawable(resid);
	}

	@Override
	public void setPadding(int left, int top, int right, int bottom) {
		if(m_radioButtons[0] == null || m_radioButtons[1] == null){
			return;
		}
		
		m_radioButtons[0].setPadding(left, top, right, bottom);
		m_radioButtons[1].setPadding(left, top, right, bottom);
	}
	
	public void setGravity(int gravity){
		if(m_radioButtons[0] == null || m_radioButtons[1] == null){
			return;
		}
		
		m_radioButtons[0].setGravity(gravity);
		m_radioButtons[1].setGravity(gravity);
	}
	
	public void setLayoutParams(LayoutParams lParams, LayoutParams rParams){
		if(m_radioButtons[0] == null || m_radioButtons[1] == null){
			return;
		}
		
		m_radioButtons[0].setLayoutParams(lParams);
		m_radioButtons[1].setLayoutParams(rParams);
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

package com.wistron.StereoUI;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * Copyright(c)2011 Wistron SWPC ALL rights reserved. 
 * @created: 2011-09-02  
 * @filename:CSCheckBox.java
 * @author :Wangwei
 * @purpose:立体控件CheckBox封裝類
 * 
 */
@SuppressWarnings("deprecation")
public class CSCheckBox  extends CSView{
	protected CheckBox[] m_checkboxs = null;
	//初始化CSCheckBox
	public CSCheckBox(Context context) {
		super(context);
		m_context = context;	
		m_checkboxs = new CheckBox[2];
		for (int i = 0; i < m_checkboxs.length; i++) {
			m_checkboxs[i] = new CheckBox(context);
		}
	}

	/**
	 * 設置控件是否可見
	 * @param visibility
	 */	
	@Override
	public void setVisibility(int visibility) {
		if(m_checkboxs[0] == null || m_checkboxs[1] == null){
			return;
		}

		m_checkboxs[0].setVisibility(visibility);
		m_checkboxs[1].setVisibility(visibility);
	}

	/**
	 * 設置控件是否可用
	 * @param enabled
	 */

	@Override
	public void setEnable(boolean enabled) {
		if(m_checkboxs[0] == null || m_checkboxs[1] == null){
			return;
		}

		m_checkboxs[0].setEnabled(enabled);
		m_checkboxs[1].setEnabled(enabled);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void setDimension(boolean is3d) {
		super.setDimension(is3d);
		if(m_checkboxs[0] == null || m_checkboxs[1] == null){
			return;
		}
		
		if(m_is_3D){
			m_checkboxs[0].setTextScaleX(0.5f);
			m_checkboxs[1].setTextScaleX(0.5f);
		}else{
			m_checkboxs[0].setTextScaleX(1f);
			m_checkboxs[1].setTextScaleX(1f);
		}
		
		if(m_checkboxs[0].getLayoutParams() != null && m_checkboxs[1].getLayoutParams() != null){
			if(m_cur_width == WRAP_CONTENT || m_cur_height == WRAP_CONTENT){
				m_checkboxs[0].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_lx, m_cur_ly));
				m_checkboxs[1].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_rx, m_cur_ry));
			}else{
				m_checkboxs[0].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_lx, m_cur_ly));
				m_checkboxs[1].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_rx, m_cur_ry));
			}
			m_checkboxs[0].requestLayout();
			m_checkboxs[1].requestLayout();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		if(m_checkboxs[0] == null || m_checkboxs[1] == null){
			return;
		}
		if(m_checkboxs[0].getLayoutParams() != null && m_checkboxs[1].getLayoutParams() != null){
			if(m_cur_width == WRAP_CONTENT || m_cur_height == WRAP_CONTENT){
				m_checkboxs[0].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_lx, m_cur_ly));
				m_checkboxs[1].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_rx, m_cur_ry));
			}else{
				m_checkboxs[0].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_lx, m_cur_ly));
				m_checkboxs[1].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_rx, m_cur_ry));
			}
			m_checkboxs[0].requestLayout();
			m_checkboxs[1].requestLayout();
		}
	}
	
	/**
	 * 判斷控件是否被選中
	 */
	public boolean isChecked(){
		if(m_checkboxs[0] == null || m_checkboxs[1] == null){
			return  false;
		}

		if(m_checkboxs[0].isChecked() && m_checkboxs[1].isChecked()){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 設置控件被選中
	 * @param checked
	 */
	public void setChecked(boolean checked){
		if(m_checkboxs[0] == null || m_checkboxs[1] == null){
			return;
		}

		m_checkboxs[0].setChecked(checked);
		m_checkboxs[1].setChecked(checked);
	}

	/**
	 * 設置控件的點擊事件
	 * @param l
	 */
	public void setOnClickListener(OnClickListener l) {
		if(m_checkboxs[0] == null || m_checkboxs[1] == null){
			return;
		}

		m_checkboxs[0].setOnClickListener(l);
		m_checkboxs[1].setOnClickListener(l);
	}

	/**
	 * 設置控件的文字
	 * @param text
	 */
	public void setText(CharSequence text){
		if(m_checkboxs[0] == null || m_checkboxs[1] == null){
			return;
		}

		m_checkboxs[0].setText(text);
		m_checkboxs[1].setText(text);
	}

	/**
	 * Sets the text color for all the states (normal, selected, focused) to be this color.
	 * @param color
	 */
	public void setTextColor(int color) {
		if(m_checkboxs[0] == null || m_checkboxs[1] == null){
			return;
		}

		m_checkboxs[0].setTextColor(color);
		m_checkboxs[1].setTextColor(color);
	}

	/**
	 * 設置控件的圖像顯示
	 * @param resid
	 */
	public void setBackgroundResource(int resid) {
		if(m_checkboxs[0] == null || m_checkboxs[1] == null){
			return;
		}

		m_checkboxs[0].setBackgroundResource(resid);
		m_checkboxs[1].setBackgroundResource(resid);
		m_checkboxs[0].setButtonDrawable(new BitmapDrawable());
		m_checkboxs[1].setButtonDrawable(new BitmapDrawable());
	}

	/**
	 * 設置控件的圖像顯示
	 * @param resid
	 */
	public void setBackgroundResource(int residL, int residR) {
		if(m_checkboxs[0] == null || m_checkboxs[1] == null){
			return;
		}

		m_checkboxs[0].setBackgroundResource(residL);
		m_checkboxs[1].setBackgroundResource(residR);
		m_checkboxs[0].setButtonDrawable(new BitmapDrawable());
		m_checkboxs[1].setButtonDrawable(new BitmapDrawable());
	}

	/**
	 * 設置OnCheckChange監聽事件
	 * @param l
	 */
	public void setOnCheckedChangeListener(OnCheckedChangeListener l){
		if(m_checkboxs[0] == null || m_checkboxs[1] == null){
			return;
		}

		m_checkboxs[0].setOnCheckedChangeListener(l);
	}

	public void setLayoutParams(LayoutParams lParams, LayoutParams rParams){
		if(m_checkboxs[0] == null || m_checkboxs[1] == null){
			return;
		}
		
		m_checkboxs[0].setLayoutParams(lParams);
		m_checkboxs[1].setLayoutParams(rParams);
	}
	
	/**
	 * 获取控件的可见性
	 * @return
	 */
	public int getVisibility(){
		int visile = View.NO_ID;
		
		if(m_checkboxs[0] == null || m_checkboxs[1] == null){
			visile = View.NO_ID;
		}

		visile = m_checkboxs[0].getVisibility();

		return visile;
	}

	/**
	 * 設置控件的觸摸事件
	 * @param event
	 */
	@Override
	public boolean touchEvent(MotionEvent event) {
		boolean state = false;

		int [] location0 = new int[2];
		int [] location1 = new int[2];

		if(m_checkboxs[0] == null || m_checkboxs[1] == null){
			return state;
		}

		m_checkboxs[0].getLocationOnScreen(location0);
		m_checkboxs[1].getLocationOnScreen(location1);

		//若控件隐藏，则不响应点击事件
		if(getVisibility()==View.INVISIBLE || getVisibility()==View.GONE){
			return false;
		}

		if(m_is_3D){
			//Under 3D mode
			if(
					(location0[0] <= event.getRawX()/2 - m_offset
					&& location0[0] + m_checkboxs[0].getWidth() >= event.getRawX()/2 - m_offset
					&& location0[1] <= event.getRawY()
					&& location0[1] + m_checkboxs[0].getHeight() >= event.getRawY())
					||
					(location1[0] <= event.getRawX()/2 + m_offset
					&& location1[0] + m_checkboxs[1].getWidth() >= event.getRawX()/2 + m_offset
					&& location1[1] <= event.getRawY()
					&& location1[1] + m_checkboxs[1].getHeight() >= event.getRawY()))
			{
				if(event.getAction() != MotionEvent.ACTION_MOVE)
				{
					state |= m_checkboxs[0].onTouchEvent(event);
					state |= m_checkboxs[1].onTouchEvent(event);
				}
			}else{
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					//				m_checkboxs[0].onTouchEvent(event);
					//				m_checkboxs[1].onTouchEvent(event);	
					m_checkboxs[0].setPressed(false);
					m_checkboxs[1].setPressed(false);
					state |= true;
				}
			}
		}else{
			//Under 2D mode
			if(
					location0[0] <= event.getRawX()
					&& location0[0] + m_checkboxs[0].getWidth() >= event.getRawX()
					&& location0[1] <= event.getRawY()
					&& location0[1] + m_checkboxs[0].getHeight() >= event.getRawY()
					)
			{
				if(event.getAction() != MotionEvent.ACTION_MOVE)
				{
					state |= m_checkboxs[0].onTouchEvent(event);
					state |= m_checkboxs[1].onTouchEvent(event);
				}
			}else{
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					//				m_checkboxs[0].onTouchEvent(event);
					//				m_checkboxs[1].onTouchEvent(event);	
					m_checkboxs[0].setPressed(false);
					m_checkboxs[1].setPressed(false);
					state |= true;
				}
			}
		}

		return state;
	}

	/**
	 *設置控件的位置并將控件添加到ViewGroup中去
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void addToLayout(ViewGroup layout) {
		float scaleRate = 1f;
		
		if(m_checkboxs[0] == null || m_checkboxs[1] == null){
			return;
		}

		if(layout == null){
			return;
		}
		
		scaleRate = m_scale_rate_w < m_scale_rate_h ? m_scale_rate_w : m_scale_rate_h;
		m_checkboxs[0].setTextSize(m_checkboxs[0].getTextSize() * scaleRate);
		m_checkboxs[1].setTextSize(m_checkboxs[1].getTextSize() * scaleRate);
		
		if(m_is_3D){
			m_checkboxs[0].setTextScaleX(0.5f);
			m_checkboxs[1].setTextScaleX(0.5f);
		}else{
			m_checkboxs[0].setTextScaleX(1f);
			m_checkboxs[1].setTextScaleX(1f);
		}

		if(m_cur_width == WRAP_CONTENT || m_cur_height == WRAP_CONTENT){
			m_checkboxs[0].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_lx, m_cur_ly));
			m_checkboxs[1].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_rx, m_cur_ry));
		}else{
			m_checkboxs[0].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_lx, m_cur_ly));
			m_checkboxs[1].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_rx, m_cur_ry));
		}

		layout.addView(m_checkboxs[0]);
		layout.addView(m_checkboxs[1]);
	}

	/**
	 * 設置控件上字體的大小
	 * @param x
	 */
	public void setTextScaleX(float x) {
		if(m_checkboxs[0] == null || m_checkboxs[1] == null){
			return;
		}
		
		m_checkboxs[0].setTextScaleX(x);
		m_checkboxs[1].setTextScaleX(x);	
	}

	/**
	 * 设置控件的ID
	 * @param id
	 */
	public void setId(int id) {
		if(m_checkboxs[0] == null || m_checkboxs[1] == null){
			return;
		}

		m_checkboxs[0].setId(id);
	}

	/**
	 * 设置控件内部的内容与控件的边距
	 */
	@Override
	public void setPadding(int left, int top, int right, int bottom) {
		if(m_checkboxs[0] == null || m_checkboxs[1] == null){
			return;
		}
		
		m_checkboxs[0].setPadding(left, top, right, bottom);
		m_checkboxs[1].setPadding(left, top, right, bottom);
	}

	public void setGravity(int gravity){
		if(m_checkboxs[0] == null || m_checkboxs[1] == null){
			return;
		}
		
		m_checkboxs[0].setGravity(gravity);
		m_checkboxs[1].setGravity(gravity);
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

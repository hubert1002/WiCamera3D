package com.wistron.StereoUI;
 
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsoluteLayout;
import android.widget.TextView;

/**
 * Copyright(c)2011 Wistron SWPC ALL rights reserved. 
 * created: 2011-09-05 
 * filename:CSTextView.java
 * @author :WH1107028
 * purpose:立体控件CSTextView的封装
 * 
 */
@SuppressWarnings("deprecation")
public class CSTextView extends CSView {

	protected TextView m_textViews[];

	public CSTextView(Context context) {
		super(context);
		m_context = context;
		m_textViews = new TextView[2];
		for (int i = 0; i < m_textViews.length; i++) {
			m_textViews[i] = new TextView(context);
		}
	}

	/**
	 * 設置控件是否可見
	 * @param visibility
	 */	
	@Override
	public void setVisibility(int visibility) {
		if(m_textViews[0] == null || m_textViews[1] == null){
			return;
		}

		m_textViews[0].setVisibility(visibility);
		m_textViews[1].setVisibility(visibility);
	}

	/**
	 * 設置控件是否可用
	 * @param enabled
	 */
	@Override
	public void setEnable(boolean enabled) {
		if(m_textViews[0] == null || m_textViews[1] == null){
			return;
		}

		m_textViews[0].setEnabled(enabled);
		m_textViews[1].setEnabled(enabled);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void setDimension(boolean is3d) {
		super.setDimension(is3d);
		if(m_textViews[0] == null || m_textViews[1] == null){
			return;
		}
		
		if(m_is_3D){
			m_textViews[0].setTextScaleX(0.5f);
			m_textViews[1].setTextScaleX(0.5f);
		}else{
			m_textViews[0].setTextScaleX(1f);
			m_textViews[1].setTextScaleX(1f);
		}
		
		setPadding(m_org_pad_left, m_org_pad_top, m_org_pad_right, m_org_pad_bottom);
		
		if(m_textViews[0].getLayoutParams() != null && m_textViews[1].getLayoutParams() != null){
			if(m_cur_width == WRAP_CONTENT || m_cur_height == WRAP_CONTENT){
				m_textViews[0].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_lx, m_cur_ly));
				m_textViews[1].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_rx, m_cur_ry));
			}else{
				m_textViews[0].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_lx, m_cur_ly));
				m_textViews[1].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_rx, m_cur_ry));
			}
			m_textViews[0].requestLayout();
			m_textViews[1].requestLayout();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		if(m_textViews[0] == null || m_textViews[1] == null){
			return;
		}
		
		if(m_textViews[0].getLayoutParams() != null && m_textViews[1].getLayoutParams() != null){
			if(m_cur_width == WRAP_CONTENT || m_cur_height == WRAP_CONTENT){
				m_textViews[0].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_lx, m_cur_ly));
				m_textViews[1].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_rx, m_cur_ry));
			}else{
				m_textViews[0].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_lx, m_cur_ly));
				m_textViews[1].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_rx, m_cur_ry));
			}
			m_textViews[0].requestLayout();
			m_textViews[1].requestLayout();
		}
	}
	
	/**
	 * 设定控件背景图
	 * @param resid
	 */
	public void setBackground(int resid){
		if(m_textViews[0] == null || m_textViews[1] == null){
			return;
		}

		m_textViews[0].setBackgroundResource(resid);
		m_textViews[1].setBackgroundResource(resid);
	}

	/**
	 * 设定控件背景图
	 * @param resid
	 */
	public void setBackground(int residL, int residR){
		if(m_textViews[0] == null || m_textViews[1] == null){
			return;
		}

		m_textViews[0].setBackgroundResource(residL);
		m_textViews[1].setBackgroundResource(residR);
	}

	
	/**
	 * 设置控件背景颜色
	 * @param color
	 */
	public void setBackgroundColor(int color) {
		if(m_textViews[0] == null || m_textViews[1] == null){
			return;
		}

		m_textViews[0].setBackgroundColor(color);
		m_textViews[1].setBackgroundColor(color);
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

		if(m_textViews[0] == null || m_textViews[1] == null){
			return state;
		}

		m_textViews[0].getLocationOnScreen(location0);
		m_textViews[1].getLocationOnScreen(location1);

		//若控件隐藏，则不响应点击事件
		if(getVisibility()==View.INVISIBLE || getVisibility()==View.GONE){
			return false;
		}
		

		if(m_is_3D){
			//Under 3D mode
			if(
					(location0[0] <= event.getRawX()/2 - m_offset
					&& location0[0] + m_textViews[0].getWidth() >= event.getRawX()/2 - m_offset
					&& location0[1] <= event.getRawY()
					&& location0[1] + m_textViews[0].getHeight() >= event.getRawY())
					||
					(location1[0] <= event.getRawX()/2 + m_offset
					&& location1[0] + m_textViews[1].getWidth() >= event.getRawX()/2 + m_offset
					&& location1[1] <= event.getRawY()
					&& location1[1] + m_textViews[1].getHeight() >= event.getRawY()))
			{
				if(event.getAction() != MotionEvent.ACTION_MOVE)
				{
					state |= m_textViews[0].onTouchEvent(event);
					state |= m_textViews[1].onTouchEvent(event);
				}
			}
		}else{
			//Under 2D mode
			if(
					location0[0] <= event.getRawX()
					&& location0[0] + m_textViews[0].getWidth() >= event.getRawX()
					&& location0[1] <= event.getRawY()
					&& location0[1] + m_textViews[0].getHeight() >= event.getRawY()
					)
			{
				if(event.getAction() != MotionEvent.ACTION_MOVE)
				{
					state |= m_textViews[0].onTouchEvent(event);
					state |= m_textViews[1].onTouchEvent(event);
				}
			}
		}

		return state;
	}

	/**
	 * 把本Group添加至指定ViewGroup
	 * @param layout
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void addToLayout(ViewGroup alayout) {
		float scaleRate = 1f;
		
		if(m_textViews[0] == null || m_textViews[1] == null){
			return;
		}

		if(alayout == null){
			return;
		}

		scaleRate = m_scale_rate_w < m_scale_rate_h ? m_scale_rate_w : m_scale_rate_h;
		m_textViews[0].setTextSize(m_textViews[0].getTextSize() * scaleRate);
		m_textViews[1].setTextSize(m_textViews[1].getTextSize() * scaleRate);
		
		if(m_is_3D){
			m_textViews[0].setTextScaleX(0.5f);
			m_textViews[1].setTextScaleX(0.5f);
		}else{
			m_textViews[0].setTextScaleX(1f);
			m_textViews[1].setTextScaleX(1f);
		}

		if(m_cur_width == WRAP_CONTENT || m_cur_height == WRAP_CONTENT){
			m_textViews[0].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_lx, m_cur_ly));
			m_textViews[1].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_rx, m_cur_ry));
		}else{
			m_textViews[0].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_lx, m_cur_ly));
			m_textViews[1].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_rx, m_cur_ry));
		}

		alayout.addView(m_textViews[0]);
		alayout.addView(m_textViews[1]);
	}

	/**
	 * 设定点击事件处理
	 * @param l
	 */
	public void setOnClickListener(OnClickListener l) {
		if(m_textViews[0] == null || m_textViews[1] == null){
			return;
		}

		m_textViews[0].setOnClickListener(l);
	}

	/**
	 * 设置文本的横向缩放比
	 * @param size
	 */
	public void setTextScaleX(float size) {
		if(m_textViews[0] == null || m_textViews[1] == null){
			return;
		}

		m_textViews[0].setTextScaleX(size);
		m_textViews[1].setTextScaleX(size);
	}

	/**
	 * 设置显示文本
	 * @param text CharSequence
	 */
	public void setText(CharSequence text) {
		if(m_textViews[0] == null || m_textViews[1] == null){
			return;
		}

		m_textViews[0].setText(text);
		m_textViews[1].setText(text);
	}

	/**
	 * 设置显示文本
	 * @param text R.string.*
	 */
	public void setText(int resid) {
		if(m_textViews[0] == null || m_textViews[1] == null){
			return;
		}

		m_textViews[0].setText(resid);
		m_textViews[1].setText(resid);
	}

	/**
	 * 设置文本颜色
	 * @param colors ColorStateList
	 */
	public void setTextColor(ColorStateList colors) {
		if(m_textViews[0] == null || m_textViews[1] == null){
			return;
		}

		m_textViews[0].setTextColor(colors);
		m_textViews[1].setTextColor(colors);
	}

	/**
	 * 设置文本颜色
	 * @param colors int
	 */
	public void setTextColor(int color) {
		if(m_textViews[0] == null || m_textViews[1] == null){
			return;
		}

		m_textViews[0].setTextColor(color);
		m_textViews[1].setTextColor(color);
	}

	/**
	 * 设置文本字号
	 * @param size
	 */
	public void setTextSize(float size) {
		if(m_textViews[0] == null || m_textViews[1] == null){
			return;
		}

		m_textViews[0].setTextSize(size);
		m_textViews[1].setTextSize(size);
	}

	public void setLayoutParams(LayoutParams lParams, LayoutParams rParams){
		if(m_textViews[0] == null || m_textViews[1] == null){
			return;
		}
		
		m_textViews[0].setLayoutParams(lParams);
		m_textViews[1].setLayoutParams(rParams);
	}
	
	/**
	 * 获取控件的可见性
	 * @return
	 */
	public int getVisibility(){
		int visile = View.NO_ID;
		if(m_textViews[0] == null || m_textViews[1] == null){
			visile = View.NO_ID;
		}

		visile = m_textViews[0].getVisibility();

		return visile;
	}

	/**
	 * 设置控件的ID
	 * @param id
	 */
	public void setId(int id) {
		if(m_textViews[0] == null || m_textViews[1] == null){
			return;
		}

		m_textViews[0].setId(id);
		m_textViews[1].setId(id);
	}

	/**
	 * Return the visible drawing bounds of your left-screen view. Fills in the output rectangle with the values from getScrollX(), getScrollY(), getWidth(), and getHeight().
	 * @return The (scrolled) drawing bounds of the left-screen view.
	 */
	public Rect getDrawingRect() {
		Rect rect = new Rect();
		if(m_textViews[0] == null || m_textViews[1] == null){
			return null;
		}

		m_textViews[0].getWidth();
		m_textViews[0].getHeight();

		rect.set(m_textViews[0].getLeft(), 
				 m_textViews[0].getTop(), 
				 m_textViews[0].getRight(), 
				 m_textViews[0].getBottom());

		return rect;
	}

	/**
	 * 设置控件内部的内容与控件的边距
	 */
	@Override
	public void setPadding(int left, int top, int right, int bottom) {
		if(m_textViews[0] == null || m_textViews[1] == null){
			return;
		}
		
		super.setPadding(left, top, right, bottom);
		
		m_textViews[0].setPadding(m_pad_lleft, m_pad_ltop, m_pad_lright, m_pad_lbottom);
		m_textViews[1].setPadding(m_pad_rleft, m_pad_rtop, m_pad_rright, m_pad_rbottom);
	}

	public void setGravity(int gravity){
		if(m_textViews[0] == null || m_textViews[1] == null){
			return;
		}
		
		m_textViews[0].setGravity(gravity);
		m_textViews[1].setGravity(gravity);
	}

	public TextPaint getPaint(){
		if(m_textViews[0] == null || m_textViews[1] == null){
			return null;
		}
		
		m_textViews[0].getPaint();
		
		return m_textViews[0].getPaint();
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

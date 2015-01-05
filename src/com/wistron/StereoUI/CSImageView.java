package com.wistron.StereoUI;


import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;

/**
 * Copyright(c)2011 Wistron SWPC ALL rights reserved. 
 * created: 2011-09-25  
 * filename:CSImageView.java
 * @author :WangWei
 * purpose:立体控件CSImageView的封装
 * 
 */

@SuppressWarnings("deprecation")
public class CSImageView extends CSView{
	protected ImageView[] m_imageviews = null;

	public CSImageView(Context context) {
		super(context);
		m_context = context;
		m_imageviews = new ImageView[2];
		for (int i = 0; i < m_imageviews.length; i++) {
			m_imageviews[i] = new ImageView(context);
		}
	}

	@Override
	public void setVisibility(int visibility) {
		if(m_imageviews[0] == null || m_imageviews[1] == null){
			return;
		}

		m_imageviews[0].setVisibility(visibility);
		m_imageviews[1].setVisibility(visibility);
	}

	@Override
	public void setEnable(boolean enabled) {
		if(m_imageviews[0] == null || m_imageviews[1] == null){
			return;
		}

		m_imageviews[0].setEnabled(enabled);
		m_imageviews[1].setEnabled(enabled);
	}
	
	@Override
	public void setDimension(boolean is3d) {
		super.setDimension(is3d);
		if(m_imageviews[0] == null || m_imageviews[1] == null){
			return;
		}
		
		if(m_imageviews[0].getLayoutParams() != null && m_imageviews[1].getLayoutParams() != null){
			if(m_cur_width == WRAP_CONTENT || m_cur_height == WRAP_CONTENT){
				m_imageviews[0].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_lx, m_cur_ly));
				m_imageviews[1].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_rx, m_cur_ry));
			}else{
				m_imageviews[0].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_lx, m_cur_ly));
				m_imageviews[1].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_rx, m_cur_ry));
			}
			m_imageviews[0].requestLayout();
			m_imageviews[1].requestLayout();
		}
	}
	
	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		if(m_imageviews[0] == null || m_imageviews[1] == null){
			return;
		}
		if(m_imageviews[0].getLayoutParams() != null && m_imageviews[1].getLayoutParams() != null){
			if(m_cur_width == WRAP_CONTENT || m_cur_height == WRAP_CONTENT){
				m_imageviews[0].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_lx, m_cur_ly));
				m_imageviews[1].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_rx, m_cur_ry));
			}else{
				m_imageviews[0].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_lx, m_cur_ly));
				m_imageviews[1].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_rx, m_cur_ry));
			}
			m_imageviews[0].requestLayout();
			m_imageviews[1].requestLayout();
		}
	}

	public void setClickable(Boolean clickable){
		if(m_imageviews[0] == null || m_imageviews[1] == null){
			return;
		}
		
		m_imageviews[0].setClickable(clickable);
		m_imageviews[1].setClickable(clickable);
		
	}
	
	public void setAnimation(Animation animation){
		if(m_imageviews[0] == null || m_imageviews[1] == null){
			return;
		}
		
		m_imageviews[0].startAnimation(animation);
		m_imageviews[1].startAnimation(animation);
		
		
	}
	@Override
	public boolean touchEvent(MotionEvent event) {
		boolean state = false;

		int [] location0 = new int[2];
		int [] location1 = new int[2];

		if(m_imageviews[0] == null || m_imageviews[1] == null){
			return state;
		}

		m_imageviews[0].getLocationOnScreen(location0);
		m_imageviews[1].getLocationOnScreen(location1);

		//若控件隐藏，则不响应点击事件
		if(getVisibility()==View.INVISIBLE || getVisibility()==View.GONE){
			return false;
		}

		if(m_is_3D){
			//Under 3D mode
			if(
					(location0[0] <= event.getRawX()/2 - m_offset
					&& location0[0] + m_imageviews[0].getWidth() >= event.getRawX()/2 - m_offset
					&& location0[1] <= event.getRawY()
					&& location0[1] + m_imageviews[0].getHeight() >= event.getRawY())
					||
					(location1[0] <= event.getRawX()/2 + m_offset
					&& location1[0] + m_imageviews[1].getWidth() >= event.getRawX()/2 + m_offset
					&& location1[1] <= event.getRawY()
					&& location1[1] + m_imageviews[1].getHeight() >= event.getRawY()))
			{
				if(event.getAction() != MotionEvent.ACTION_MOVE)
				{
					state |= m_imageviews[0].onTouchEvent(event);
					state |= m_imageviews[1].onTouchEvent(event);
				}
			}
		}else{
			//Under 2D mode
//			if(
//					location0[0] <= event.getRawX()
//					&& location0[0] + m_imageviews[0].getWidth() >= event.getRawX()
//					&& location0[1] <= event.getRawY()
//					&& location0[1] + m_imageviews[0].getHeight() >= event.getRawY()
//					)
//			{
//				if(event.getAction() != MotionEvent.ACTION_MOVE)
//				{
//					state |= m_imageviews[0].onTouchEvent(event);
//					state |= m_imageviews[1].onTouchEvent(event);
//				}
//			}
		}

		return state;
	}

	
	
	
	@Override
	public void addToLayout(ViewGroup alayout) {
		if(m_imageviews[0] == null || m_imageviews[1] == null){
			return;
		}

		if(alayout == null){
			return;
		}

		if(m_cur_width == WRAP_CONTENT || m_cur_height == WRAP_CONTENT){
			m_imageviews[0].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_lx, m_cur_ly));
			m_imageviews[1].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_rx, m_cur_ry));
		}else{
			m_imageviews[0].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_lx, m_cur_ly));
			m_imageviews[1].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_rx, m_cur_ry));
		}

		alayout.addView(m_imageviews[0]);
		alayout.addView(m_imageviews[1]);
	}

	/**
	 * 获取控件的可见性
	 * @return
	 */
	public int getVisibility(){
		int visile = View.NO_ID;
		if(m_imageviews[0] == null || m_imageviews[1] == null){
			visile = View.NO_ID;
		}

		visile = m_imageviews[0].getVisibility();

		return visile;
	}

	/**
	 * Set this to true if you want the ImageView to adjust its bounds to preserve the aspect ratio of its drawable.
	 * @param isAdjust
	 */
	public void setAdjustViewBounds(boolean isAdjust){
		if(m_imageviews[0] == null || m_imageviews[1] == null){
			return;
		}

		m_imageviews[0].setAdjustViewBounds(isAdjust);
		m_imageviews[1].setAdjustViewBounds(isAdjust);
	}

	public void setOnClickListener(OnClickListener l){
		if(m_imageviews[0] == null || m_imageviews[1] == null){
			return;
		}
		m_imageviews[0].setOnClickListener(l);
		m_imageviews[1].setOnClickListener(l);
	}
	/**
	 * 设置透明度
	 * @param alpha
	 */
	public void setAlpha(int alpha) {
		if(m_imageviews[0] == null || m_imageviews[1] == null){
			return;
		}

		m_imageviews[0].setAlpha(alpha);
		m_imageviews[1].setAlpha(alpha);
	}

	/**
	 * Set the background to a given resource. The resource should refer to a Drawable object or 0 to remove the background.
	 * @param resid
	 */
	public void setBackgroundResource(int resid) {
		if(m_imageviews[0] == null || m_imageviews[1] == null){
			return;
		}

		m_imageviews[0].setBackgroundResource(resid);
		m_imageviews[1].setBackgroundResource(resid);
	}

	/**
	 * Sets a drawable as the content of this ImageView. 
	 * This does Bitmap reading and decoding on the UI thread, which can cause a latency hiccup. If that's a concern, consider using setImageDrawable(Drawable) or setImageBitmap(Bitmap) and BitmapFactory instead.
	 * @param resid the resource identifier of the the drawable
	 */
	public void setImageResource(int resid) {
		if(m_imageviews[0] == null || m_imageviews[1] == null){
			return;
		}

		m_imageviews[0].setImageResource(resid);
		m_imageviews[1].setImageResource(resid);
	}

	public void setBackgroundColor(int color){
		if(m_imageviews[0] == null || m_imageviews[1] == null){
			return;
		}

		m_imageviews[0].setBackgroundColor(color);
		m_imageviews[1].setBackgroundColor(color);
	}

	public void setLayoutParams(LayoutParams lParams, LayoutParams rParams){
		if(m_imageviews[0] == null || m_imageviews[1] == null){
			return;
		}
		
		m_imageviews[0].setLayoutParams(lParams);
		m_imageviews[1].setLayoutParams(rParams);
	}
	
	/**
	 * Call this view's OnClickListener, if it is defined.
	 * @return True there was an assigned OnClickListener that was called, false otherwise is returned. 
	 */
	public boolean performClick(){
		boolean state = false;
		if(m_imageviews[0] == null || m_imageviews[1] == null){
			return false;
		}
		
		state |= m_imageviews[0].performClick();
		
		return state;
	}

	/**
	 * 设置控件内部的内容与控件的边距
	 */
	@Override
	public void setPadding(int left, int top, int right, int bottom) {
		if(m_imageviews[0] == null || m_imageviews[1] == null){
			return;
		}

		m_imageviews[0].setPadding(left, top, right, bottom);
		m_imageviews[1].setPadding(left, top, right, bottom);
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

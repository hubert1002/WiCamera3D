package com.wistron.StereoUI;

import Utilities.CSStaticData;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;

/**
 * Copyright(c)2011 Wistron SWPC ALL rights reserved. 
 * @date: 2011-12-22 09:15:07
 * @author :WH1107028
 * @purpose:立体控件のToast
 */
@SuppressWarnings("deprecation")
public class CSGallery extends CSView{
	
	private static final String TAG          = "CSGallery";
	protected Gallery[]      m_gallerys      = null;
	private CSGalleryAdapter m_adapter       = null;
	private boolean          m_allow_dragged = false;
	private boolean          m_dragged       = false;
	private int              m_ambiguous     = 0;
	private int              m_spacing       = 0;
	
	public CSGallery(Context context) {
		super(context);
		m_context = context;
		m_gallerys = new Gallery[2];
		for (int i = 0; i < m_gallerys.length; i++) {
			m_gallerys[i] = new Gallery(context);
		}
	}

	@Override
	public void setVisibility(int visibility) {
		if(m_gallerys[0] == null || m_gallerys[1] == null){
			return;
		}

		m_gallerys[0].setVisibility(visibility);
		m_gallerys[1].setVisibility(visibility);
	}

	@Override
	public void setEnable(boolean enabled) {
		if(m_gallerys[0] == null || m_gallerys[1] == null){
			return;
		}

		m_gallerys[0].setEnabled(enabled);
		m_gallerys[1].setEnabled(enabled);
	}
	
	@Override
	public void setDimension(boolean is3d) {
		super.setDimension(is3d);
		
		if(m_gallerys[0] == null || m_gallerys[1] == null){
			return;
		}
		
		if(m_adapter != null && m_adapter instanceof CSGalleryAdapter){
			m_adapter.setDimension(is3d);
		}
		
		m_gallerys[0].setSpacing(m_spacing/2);
		m_gallerys[1].setSpacing(m_spacing/2);
		
		if(m_gallerys[0].getLayoutParams() != null && m_gallerys[1].getLayoutParams() != null){
			if(m_cur_width == WRAP_CONTENT || m_cur_height == WRAP_CONTENT){
				m_gallerys[0].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_lx, m_cur_ly));
				m_gallerys[1].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_rx, m_cur_ry));
			}else{
				m_gallerys[0].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_lx, m_cur_ly));
				m_gallerys[1].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_rx, m_cur_ry));
			}
			m_gallerys[0].requestLayout();
			m_gallerys[1].requestLayout();
		}
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		if(m_gallerys[0] == null || m_gallerys[1] == null){
			return;
		}
		
		if(m_gallerys[0].getLayoutParams() != null && m_gallerys[1].getLayoutParams() != null){
			if(m_cur_width == WRAP_CONTENT || m_cur_height == WRAP_CONTENT){
				m_gallerys[0].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_lx, m_cur_ly));
				m_gallerys[1].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_rx, m_cur_ry));
			}else{
				m_gallerys[0].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_lx, m_cur_ly));
				m_gallerys[1].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_rx, m_cur_ry));
			}
			m_gallerys[0].requestLayout();
			m_gallerys[1].requestLayout();
		}
	}

	@Deprecated
	@Override
	public void setLocation(int x, int y, int screenWidth, int screenHeight) {
		super.setLocation(x, y, screenWidth, screenHeight);
		if(m_gallerys[0] == null || m_gallerys[1] == null){
			return;
		}
		if(m_gallerys[0].getLayoutParams() != null && m_gallerys[1].getLayoutParams() != null){
			m_gallerys[0].requestLayout();
			m_gallerys[1].requestLayout();
		}
	}
	
	public void setPadding(int left,int top,int right,int bottom){
		if(m_gallerys[0] == null || m_gallerys[1] == null){
			return;
		}
		
		super.setPadding(left, top, right, bottom);
		
		m_gallerys[0].setPadding(m_pad_lleft, m_pad_ltop, m_pad_lright, m_pad_lbottom);
		m_gallerys[1].setPadding(m_pad_rleft, m_pad_rtop, m_pad_rright, m_pad_rbottom);
	}
	
	public void setGravity(int gravity){
		if(m_gallerys[0] == null || m_gallerys[1] == null){
			return;
		}
		
		m_gallerys[0].setGravity(gravity);
		m_gallerys[1].setGravity(gravity);
	}
		
	@Deprecated
	public void setId(int id) {
		if(m_gallerys[0] == null || m_gallerys[1] == null){
			return;
		}

		m_gallerys[0].setId(id);
	}
	
	public void setAnimationDuration(int animationDurationMillis){
		if(m_gallerys[0] == null || m_gallerys[1] == null){
			return;
		}
		
		m_gallerys[0].setAnimationDuration(animationDurationMillis);
		m_gallerys[1].setAnimationDuration(animationDurationMillis);
	}
	
	public void setBackgroundResource(int resid){
		setBackgroundResource(resid, resid);
	}
	
	public void setBackgroundResource(int residL, int residR){
		if(m_gallerys[0] == null || m_gallerys[1] == null){
			return;
		}
		
		m_gallerys[0].setBackgroundResource(residL);
		m_gallerys[1].setBackgroundResource(residR);
	}
	
	public void setCallbackDuringFling(boolean shouldCallback){
		if(m_gallerys[0] == null || m_gallerys[1] == null){
			return;
		}
		
		m_gallerys[0].setCallbackDuringFling(shouldCallback);
		m_gallerys[1].setCallbackDuringFling(shouldCallback);
	}
	
	public void setSpacing(int spacing){
		if(m_gallerys[0] == null || m_gallerys[1] == null){
			return;
		}
		
		m_spacing = spacing;
		
		if(m_is_3D){
			m_gallerys[0].setSpacing(spacing/2);
			m_gallerys[1].setSpacing(spacing/2);
		}else{
			m_gallerys[0].setSpacing(spacing);
			m_gallerys[1].setSpacing(spacing);
		}
	}
		
	public void setUnselectedAlpha(float unselectedAlpha){
		if(m_gallerys[0] == null || m_gallerys[1] == null){
			return;
		}
		
		m_gallerys[0].setUnselectedAlpha(unselectedAlpha);
		m_gallerys[1].setUnselectedAlpha(unselectedAlpha);
	}
	
	public void setAdapter(BaseAdapter adapter){
		if(m_gallerys[0] == null || m_gallerys[1] == null){
			return;
		}
		
		m_adapter = (CSGalleryAdapter) adapter;
		m_gallerys[0].setAdapter(m_adapter);
		m_gallerys[1].setAdapter(m_adapter);
	}
	
	public void setSelected(boolean selected){
		if(m_gallerys[0] == null || m_gallerys[1] == null){
			return;
		}
		
		m_gallerys[0].setSelected(selected);
		m_gallerys[1].setSelected(selected);
	}
	
	public void setSelection(int position, boolean animate){
		if(m_gallerys[0] == null || m_gallerys[1] == null){
			return;
		}
		
		m_gallerys[0].setSelection(position, animate);
		m_gallerys[1].setSelection(position, animate);
	}
	
	public void setSelection(int position){
		if(m_gallerys[0] == null || m_gallerys[1] == null){
			return;
		}
		
		m_gallerys[0].setSelection(position);
		m_gallerys[1].setSelection(position);
	}
	
	public void setOnClickListener(View.OnClickListener listener){
		if(m_gallerys[0] == null){
			return;
		}
		
		m_gallerys[0].setOnClickListener(listener);
	}
	
	public void setOnItemClickListener(AdapterView.OnItemClickListener listener){
		if(m_gallerys[0] == null){
			return;
		}
		
		m_gallerys[0].setOnItemClickListener(listener);
	}

	public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener listener){
		if(m_gallerys[0] == null){
			return;
		}
		
		m_gallerys[0].setOnItemSelectedListener(listener);
	}
	
	public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener listener){
		if(m_gallerys[0] == null){
			return;
		}
		
		m_gallerys[0].setOnItemLongClickListener(listener);
	}
	
	
	public int getVisibility(){
		int visible = View.NO_ID;
		
		if(m_gallerys[0] == null || m_gallerys[1] == null){
			return visible;
		}
		visible = m_gallerys[0].getVisibility();

		return View.NO_ID;
	}

	public int getId(){
		int id = View.NO_ID;
		
		if(m_gallerys[0] == null || m_gallerys[1] == null){
			return id;
		}
		id = m_gallerys[0].getId();

		return View.NO_ID;
	}
	
	public Object getSelectedItem(){
		Object seletedItem = null;
		
		if(m_gallerys[0] == null || m_gallerys[1] == null){
			return seletedItem;
		}
		seletedItem = m_gallerys[0].getSelectedItem();
		
		return null;
	}
	
	public int getSelectedItemPosition(){
		int position = -1;
		
		if(m_gallerys[0] == null || m_gallerys[1] == null){
			return position;
		}
		position = m_gallerys[0].getSelectedItemPosition();
		
		return position;
	}
	
	public View getSelectedView(){
		View selectedView = null;
		
		if(m_gallerys[0] == null || m_gallerys[1] == null){
			return selectedView;
		}
		selectedView = m_gallerys[0].getSelectedView();
		
		return selectedView;
	}
	
	public int getCount(){
		int count = 0;
		
		if(m_gallerys[0] == null || m_gallerys[1] == null){
			return count;
		}
		count = m_gallerys[0].getCount();
		
		return count;
	}
	
	public int getWidth(){
		int width = 0;
		
		if(m_gallerys[0] == null || m_gallerys[1] == null){
			return width;
		}
		width = m_gallerys[0].getWidth();
		
		return width;
	}
	
	public int getHeight(){
		int height = 0;
		
		if(m_gallerys[0] == null || m_gallerys[1] == null){
			return height;
		}
		height = m_gallerys[0].getHeight();
		
		return height;
	}
	
	/**
	 * 传入屏幕上的实际坐标，此方法会自动根据2D-3D模式做出判断
	 * @param x
	 * @param y
	 * @return
	 */
	public int pointToPosition(int x, int y){
		int position = -1;
		
		if(m_gallerys[0] == null || m_gallerys[1] == null){
			return position;
		}
		
		if(m_is_3D){
			position = m_gallerys[0].pointToPosition(x/2, y);
		}else{
			position = m_gallerys[0].pointToPosition(x, y);
		}
		
		return position;
	}

	public void startAnimation(Animation anim) {
		if(m_gallerys[0] == null || m_gallerys[1] == null){
			return;
		}
		
		m_gallerys[0].startAnimation(anim);
		m_gallerys[1].startAnimation(anim);
	}

	public void startAnimation(Animation animL, Animation animR) {
		if(m_gallerys[0] == null || m_gallerys[1] == null){
			return;
		}
		
		m_gallerys[0].startAnimation(animL);
		m_gallerys[1].startAnimation(animR);
	}
	
	public void clearAnimation(){
		if(m_gallerys[0] == null || m_gallerys[1] == null){
			return;
		}
		
		m_gallerys[0].clearAnimation();
		m_gallerys[1].clearAnimation();
	}
	
	@Override
	public boolean touchEvent(MotionEvent event) {
		boolean state = false;

		int [] location0 = new int[2];
		int [] location1 = new int[2];
		if(m_gallerys[0] == null || m_gallerys[1] == null){
			return state;
		}

		m_gallerys[0].getLocationOnScreen(location0);
		m_gallerys[1].getLocationOnScreen(location1);

		//若控件隐藏，则不响应点击事件
		if(getVisibility()==View.INVISIBLE || getVisibility()==View.GONE){
			return false;
		}

		if(m_is_3D){
			//Under 3D mode
			if(
					(location0[0] <= event.getRawX()/2 - m_offset - m_ambiguous
					&& location0[0] + m_gallerys[0].getWidth() >= event.getRawX()/2 - m_offset
					&& location0[1] <= event.getRawY()
					&& location0[1] + m_gallerys[0].getHeight() >= event.getRawY())
					||
					(location1[0] <= event.getRawX()/2 + m_offset + m_ambiguous
					&& location1[0] + m_gallerys[1].getWidth() >= event.getRawX()/2 + m_offset
					&& location1[1] <= event.getRawY()
					&& location1[1] + m_gallerys[1].getHeight() >= event.getRawY())
					)
			{
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					m_allow_dragged = true;
					event.setLocation(event.getX()/2f, event.getY());
					state |= m_gallerys[0].onTouchEvent(event);
					event.setLocation(event.getX() + m_screen_width/2f, event.getY());
					state |= m_gallerys[1].onTouchEvent(event);
					event.setLocation(event.getX()*2f - m_screen_width , event.getY());
					
					if (CSStaticData.DEBUG)
						Log.w(TAG, "[touchEvent]接受ACTION_DOWN: " + event.getRawX() + ", " + event.getRawY());
					return state;
				}
			}
			if(event.getAction() == MotionEvent.ACTION_UP && m_allow_dragged)
			{
				m_allow_dragged = false;
				event.setLocation(event.getX()/2f, event.getY());
				state |= m_gallerys[0].onTouchEvent(event);
				event.setLocation(event.getX() + m_screen_width/2f, event.getY());
				state |= m_gallerys[1].onTouchEvent(event);
				event.setLocation(event.getX()*2f - m_screen_width , event.getY());
				
				if (CSStaticData.DEBUG)
					Log.w(TAG, "[touchEvent]接受ACTION_UP: " + event.getRawX() + ", " + event.getRawY());
				return state;
			}

			if(m_allow_dragged){
				event.setLocation(event.getX()/2f, event.getY());
				state |= m_gallerys[0].onTouchEvent(event);
				event.setLocation(event.getX() + m_screen_width/2f, event.getY());
				state |= m_gallerys[1].onTouchEvent(event);
				event.setLocation(event.getX()*2f - m_screen_width , event.getY());
				
				if (CSStaticData.DEBUG)
					Log.w(TAG, "[touchEvent]接受ACTION_MOVE: " + event.getRawX() + ", " + event.getRawY());
			}
		}else{
			//Under 2D mode
			if(
					location0[0] <= event.getRawX() - m_ambiguous
					&& location0[0] + m_gallerys[0].getWidth() >= event.getRawX()
					&& location0[1] <= event.getRawY()
					&& location0[1] + m_gallerys[0].getHeight() >= event.getRawY()
					)
			{
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					m_allow_dragged = true;
					state |= m_gallerys[0].onTouchEvent(event);
//					state |= m_gallerys[1].onTouchEvent(event);
					
					if (CSStaticData.DEBUG)
						Log.w(TAG, "[touchEvent]接受ACTION_DOWN: " + event.getRawX() + ", " + event.getRawY());
					return state;
				}
			}
			if(event.getAction() == MotionEvent.ACTION_UP && m_allow_dragged)
			{
				m_allow_dragged = false;
				state |= m_gallerys[0].onTouchEvent(event);
//				state |= m_gallerys[1].onTouchEvent(event);
				
				if (CSStaticData.DEBUG)
					Log.w(TAG, "[touchEvent]接受ACTION_UP: " + event.getRawX() + ", " + event.getRawY());
				state |= true;
				return state;
			}

			if(m_allow_dragged){
				state |= m_gallerys[0].onTouchEvent(event);
//				state |= m_gallerys[1].onTouchEvent(event);
				
				if (CSStaticData.DEBUG)
					Log.w(TAG, "[touchEvent]接受ACTION_MOVE: " + event.getRawX() + ", " + event.getRawY());
			}
		}

		return state;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void addToLayout(ViewGroup layout) {
		if(m_gallerys[0] == null || m_gallerys[1] == null){
			return;
		}

		if(layout == null){
			return;
		}

		//設置控件在屏幕中顯示的位置
		if(m_cur_width == WRAP_CONTENT || m_cur_height == WRAP_CONTENT){
			m_gallerys[0].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_lx, m_cur_ly));
			m_gallerys[1].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_rx, m_cur_ry));
		}else{
			m_gallerys[0].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_lx, m_cur_ly));
			m_gallerys[1].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_rx, m_cur_ry));
		}

		if (CSStaticData.DEBUG)
		{
			Log.w("CSSeekBar","[addToLayout]w:h:x:y = " + m_cur_width + ", " + m_cur_height  + ", " + m_cur_lx + ", " + m_cur_ly);
			Log.w("CSSeekBar","[addToLayout]w:h:x:y = " + m_cur_width + ", " + m_cur_height  + ", " + m_cur_rx + ", " + m_cur_ry);
		}
		
		layout.addView(m_gallerys[0]);
		layout.addView(m_gallerys[1]);
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

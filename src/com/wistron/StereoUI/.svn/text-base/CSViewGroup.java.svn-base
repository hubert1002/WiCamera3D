package com.wistron.StereoUI;
 
import java.util.ArrayList;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;

/**
 * Copyright(c)2011 Wistron SWPC ALL rights reserved. 
 * @datetime: 2011-09-02 15:04:49
 * @author :WH1107028 
 * @purpose:统一管理立体控件的ViewGroup
 * 
 */
@SuppressWarnings({ "unused", "deprecation" })
public class CSViewGroup extends Object {
	protected ArrayList<CSView> m_ctrl_list = null;
	private ViewGroup m_layout = null;

	boolean state = false;

	public CSViewGroup() {
		m_ctrl_list = new ArrayList<CSView>();
	}

	/**
	 * 添加CSControlBase對象
	 * 
	 * @param csview
	 * @return
	 */
	public boolean add(CSView csview) {
		boolean state = false;

		if (m_ctrl_list == null) {
			return state;
		}

		state = m_ctrl_list.add(csview);
		return state;
	}

	/**
	 * 移除控件
	 * 
	 * @param index
	 */
	public void remove(int index) {

		if (m_ctrl_list == null) {
			return;
		}

		m_ctrl_list.remove(index);
	}

	/**
	 * 添加2D控件到ViewGroup
	 * 
	 * @param layout
	 */
	public boolean addToLayout(ViewGroup layout) {

		//备份layout
		m_layout = layout;
		
		//开始遍历layout
		if(m_ctrl_list != null || layout != null){
			for (CSView ctrl : m_ctrl_list) {
				ctrl.addToLayout(layout);
			}
			state = true;
		}else{
			state = false;
		}
		
		return state;
	}

	/**
	 * 分發觸摸事件
	 * 
	 * @param event
	 * @return
	 */
	public boolean dispatchTouchEvent(MotionEvent event) {
		boolean stateSum = false;
		boolean state = false;
			for (CSView ctrl : m_ctrl_list) {
				state = ctrl.touchEvent(event);
				stateSum |= state;
			}

		return stateSum;
	}

	/**
	 * 更新CSViewGroup
	 */
	public void invalidate() {
		if (m_layout != null) {
			m_layout.removeAllViews();
			for (CSView ctrl : m_ctrl_list) {
				ctrl.addToLayout(m_layout);
			}
		}
	}
	
	/**
	 * 设置包含在其内部的所有控件的2/3D模式
	 */
	public void setDimension(boolean is3D){
		if(m_ctrl_list != null){
			for (CSView ctrl : m_ctrl_list) {
				ctrl.setDimension(is3D);
			}
		}
	}
}

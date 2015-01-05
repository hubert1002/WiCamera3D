package com.wistron.StereoUI;

import android.content.Context;

/**
 * Copyright(c)2011 Wistron SWPC ALL rights reserved. 
 * @date: 2011-12-20 14:00:27
 * @author :WH1107028
 * @purpose:立体控件のToast
 */
@Deprecated
public class CSToast extends CSTextView{
	
	public static int LENGTH_LONG   = 3000;
	public static int LENGTH_SHORT  = 500;
	
	private int mDuration = LENGTH_SHORT;

	CSTextView[] mToastView = null;
	
	public CSToast(Context context) {
		super(context);
		setSize(CSView.WRAP_CONTENT, CSView.WRAP_CONTENT);
		setLocation(1024/2 - m_cur_width/2, 500);
	}

	/**
	 * Make a standard toast that just contains a text view.
	 * @param context The context to use. Usually your Application or Activity object. 
	 * @param text The text to show. Can be formatted text. 
	 * @param duration How long to display the message. Either LENGTH_SHORT or LENGTH_LONG  
	 */
	public static CSToast makeText(Context context, CharSequence text, int duration){
		
		return null;
	}
	

	/**
	 * Make a standard toast that just contains a text view.
	 * @param context The context to use. Usually your Application or Activity object. 
	 * @param resid The resource id of text to show. Can be formatted text. 
	 * @param duration How long to display the message. Either LENGTH_SHORT or LENGTH_LONG  
	 */
	public static CSToast makeText(Context context, int resid, int duration){
		
		return null;
	}
	
	/**
	 * 设置背景图片
	 */
	public void setBackground(int resid){
		
	}
	
	/**
	 * 设置背景图片
	 */
	public void setBackground(int residL, int residR){
		
	}
	
	/**
	 * 设置文字内容
	 */
	public void setText(CharSequence text){
		if(m_textViews[0] == null || m_textViews[1] == null){
			return;
		}

		m_textViews[0].setText(text);
		m_textViews[1].setText(text);
	}
	
	/**
	 * 显示CSToast
	 */
	public void show(){
		
	}

	@Override
	public void setDimension(boolean is3d) {
		// TODO Auto-generated method stub
		super.setDimension(is3d);
	}

	@Override
	public void setLocation(int x, int y) {
		// TODO Auto-generated method stub
		super.setLocation(x, y);
	}

	@Override
	public void setTextScaleX(float size) {
		// TODO Auto-generated method stub
		super.setTextScaleX(size);
	}

	@Override
	public void setPadding(int left, int top, int right, int bottom) {
		// TODO Auto-generated method stub
		super.setPadding(left, top, right, bottom);
	}

	@Override
	public void setGravity(int gravity) {
		// TODO Auto-generated method stub
		super.setGravity(gravity);
	}
	
	
}

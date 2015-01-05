//package com.wistron.stereoui;
//
// 
//import Utilities.TDStaticData;
//import android.content.Context;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.Paint.FontMetrics;
//import android.text.TextPaint;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.AbsoluteLayout;
//
///**
// * Copyright (c) 2011 Wistron SWPC
// * All rights reserved.
// * created: 2011/09/26
// * filename:CSMessageBox.java
// * @author WangWei
// * purpose:MessageBox的封装类,外部调用方式为:TDStaticData.ShowMsgBox(),隐藏MessageBox调用TDStaticData.HideMsgBox();
// * 
// */
//public class CSMessageBox extends CSViewGroup {
//
//	private AbsoluteLayout m_layout;	
//	private CSButton m_button_sure = null;
//	private CSButton m_button_cancel = null;
//	private CSButton m_button_no = null;
//	private CSTextView m_textView = null;
//	private CSTextView m_title = null;
//	private Context m_context = null;
//	private CSImageView m_image_title = null; 	
//	private CSImageView m_image_text = null;
//	private CSImageView m_image_button = null;
//	
//	private int m_button_type = 0;//Button的类型
//	private int m_messageBox_x = 0;//MessageBox的x，y坐标
//	private int m_messageBox_y = 0;
//	private int m_messageBox_width = 0;//MessageBox的宽
//	private int m_messageBox_width_button = 0;
//	private int m_messageBox_hight = 0;//MessageBox的高
//	private int m_title_height = 41;
//	private int m_location_type = 0;	//位置类型，根据不同位置确定显示的方式
//	private int m_button_space = 10;	// 按钮直接的间隔
//	private int m_space = 15;			//messagebox和里面内容的边距
//	private int m_offset = 6;
//	private int m_offset_btn = 5;
//	private int m_textSize = 22;
//	private int m_titleSize = 18;
//	private int	m_button_width = 53;// 按钮的宽
//	private int	m_button_hight = 37;// 按钮的高
//	private int	m_textView_width = 0;
//	private int m_textView_height = 0;
//	private int m_line_number = 0;
//	private String m_text = null;
//	//TYPE1，2，3表示显示1,2,3个Button（Yes，No，Cancel），剩下的参数分别表示以左上，左下，中间，右上，右下为参照点弹出MessageBox
//	public static final int TYPE1_LEFT_TOP = 11;
//	public static final int TYPE1_LEFT_BOTTOM = 12;
//	public static final int TYPE1_CENTER = 13;
//	public static final int TYPE1_RIGHT_TOP = 14;
//	public static final int TYPE1_RIGHT_BOTTOM = 15;
//	
//	public static final int TYPE1_OK= 113;
//	public static final int TYPE2_LEFT_TOP = 21;
//	public static final int TYPE2_LEFT_BOTTOM = 22;
//	public static final int TYPE2_CENTER = 23;
//	public static final int TYPE2_RIGHT_TOP = 24;
//	public static final int TYPE2_RIGHT_BOTTOM = 25;
//
//	public static final int TYPE3_LEFT_TOP = 31;
//	public static final int TYPE3_LEFT_BOTTOM = 32;
//	public static final int TYPE3_CENTER = 33;
//	public static final int TYPE3_RIGHT_TOP = 34;
//	public static final int TYPE3_RIGHT_BOTTOM = 35;
//	
//	public CSMessageBox(Context context) {
//		m_context = context;
//	}
//
//	/**
//	 * 添加MessageBox到布局
//	 * @param layout:父布局
//	 * @param title:MessageBox的标题
//	 * @param text:MessageBox的提示信息
//	 * @param type:Button种类+弹出的位置，如:TYPE2_CENTER
//	 * @param x:MessageBox的X坐标
//	 * @param y:MessageBox的Y坐标
//	 * @param screenWidth:屏幕宽
//	 * @param screenHeight:屏幕高
//	 */
//	public void addToLayout(ViewGroup layout, String title, Object text, int type, int x,
//			int y, int screenWidth, int screenHeight) {
//		
//		m_text = (text.toString());
//		m_textView = new CSTextView(m_context);
//		m_textView.setText((CharSequence) text);
//		m_textView.setTextSize(m_textSize);
//		 
//		TextPaint paint =m_textView.getPaint();
//		paint.setTextSize(m_textSize);
//		//获取每行的字符串并计算长度，以最长的那行字符串的长作为TextView的宽
//		if(m_text.indexOf('\n' ) == -1){
//			m_textView_width = (int) (paint.measureText(m_text)/2);
//		}else{
//			String[]  subStrings = null ;
//
//			int[] string_width = null;
//
//			subStrings = m_text.split("\\\n");
//			string_width = new int[subStrings.length];
//
//			for(int i = 0;i<subStrings.length;i++){
//				string_width[i] = (int) (paint.measureText(subStrings[i])/2);
//				System.out.println("CSMessageBox subString:"+subStrings[i]);
//				if(string_width[i] >m_textView_width){
//					m_textView_width = string_width[i];
//					//					 Log.e("CSMessageBox", "m_textView_width"+m_textView_width);
//				}
//			}
//			}
//
//
//		// 计算类型
//		if(type == TYPE1_OK){
//			m_button_type = type/100;
//			
//		}else{
//			m_button_type = type / 10 ;
//			
//		}
//		m_line_number = getLineCount("\n") + 1;
//		m_textView_height = getFontHeight(m_textSize)*m_line_number + getFontHeight(m_textSize);
//		m_location_type = type % 10;
//		
//		int x0 = x/2;	
//		
//		m_layout = new AbsoluteLayout(m_context);
//		m_button_sure = new CSButton(m_context);
//		m_button_no = new CSButton(m_context);
//		m_button_cancel = new CSButton(m_context);
//
//		m_title = new CSTextView(m_context);
//		m_title.setText(title);
//		m_title.setTextSize(m_titleSize);
//
//		m_button_sure.setSize(m_button_width, m_button_hight);
//		m_button_sure.setDimension(true);
//		
//		m_button_sure.setPanding(1, 1, 1, 1);
//		m_button_no.setPanding(1, 1, 1, 1);
//		m_button_cancel.setPanding(1, 1, 1, 1);
//		m_button_no.setSize(m_button_width, m_button_hight);
//		m_button_no.setDimension(true);
//		m_button_cancel.setSize(m_button_width, m_button_hight);
//		m_button_cancel.setDimension(true);
//		
//		m_image_title = new CSImageView(m_context);
//	
//		m_image_text = new CSImageView(m_context);
//	
//		m_image_button = new CSImageView(m_context);
//	
//		switch (m_button_type) {
//		case 1:
//			m_messageBox_width_button = m_button_width ;
//			break;
//		case 2:
//			m_messageBox_width_button = m_button_width * 2 +  + m_button_space;
//			break;
//		case 3:
//			m_messageBox_width_button = m_button_width * 3 +  + 2* m_button_space;
//		}
//
//		// 确定MessageBox的宽和高
//		if (m_textView_width > m_messageBox_width_button) {
//			m_messageBox_width = m_textView_width + 2 * m_space;
//		} else {
//			m_messageBox_width = m_messageBox_width_button + 2 * m_space;
//		}
//
//		m_messageBox_hight = m_title_height + m_textView_height + m_button_hight;
//
//		// 根据传入的设置
//		switch (m_location_type) {
//		case 1:
//			m_messageBox_x = x0;
//			m_messageBox_y = y;
//
//			break;
//		case 2:
//			m_messageBox_x = x0;
//			m_messageBox_y = y - m_title_height - m_textView_height - m_button_hight;
//			break;
//
//		case 3:
//			m_messageBox_x = x0 - m_messageBox_width / 2;
//			m_messageBox_y = y - m_messageBox_hight / 2;
//			break;
//		case 4:
//			m_messageBox_x = x0 - m_messageBox_width;
//			m_messageBox_y = y;
//			break;
//		case 5:
//			m_messageBox_x = x0 - m_messageBox_width;
//			m_messageBox_y = y - m_messageBox_hight;
//		}
//
//		m_button_sure.setBackground(R.drawable.yesno_selector);
//		m_button_no.setBackground(R.drawable.yesno_selector);
//		m_button_cancel.setBackground(R.drawable.yesno_selector);
//		
//		if(type == TYPE1_OK){
//			m_button_sure.setText("Ok");
//			
//		}else {
//			m_button_sure.setText("Yes");
//			m_button_sure.setTextColor(Color.WHITE);
//		}
//		
//		m_button_no.setText("No");
//		m_button_cancel.setText("Cancel");
//		
//		m_button_no.setTextColor(Color.WHITE);
//		m_button_cancel.setTextColor(Color.WHITE);
//		m_title.setTextColor(Color.GRAY);
//		m_textView.setTextColor(Color.WHITE);
//		m_button_sure.setTextSize(m_textSize);
//		m_button_no.setTextSize(m_textSize);
//		m_button_cancel.setTextSize(m_textSize);
//			
//		add(m_image_button);
//		switch (m_button_type) {
//		case 1:
//			m_button_sure.setLocation(2*(m_messageBox_width / 2 - m_button_width / 2
//					+ m_messageBox_x -m_offset_btn ), m_messageBox_y + m_title_height
//					+ m_textView_height + 2, screenWidth, screenHeight);
//
//			add(m_button_sure);
//			break;
//		case 2:
//			m_button_sure.setLocation(2*(m_messageBox_x
//					+ (m_messageBox_width / 2 - m_button_width - m_button_space / 2 -m_offset_btn)),
//					m_messageBox_y + m_title_height + m_textView_height + 2, screenWidth,
//					screenHeight);
//			
//			m_button_no.setLocation(2*(m_messageBox_x
//					+ (m_messageBox_width / 2 + m_button_space / 2 - m_offset_btn)), m_messageBox_y
//					+ m_title_height + m_textView_height + 2, screenWidth, screenHeight);
//			add(m_button_sure);
//			add(m_button_no);
//			break;
//		case 3:
//
//			m_button_sure.setLocation(2*(m_messageBox_width / 2 - 3 * m_button_width / 2
//					+ m_messageBox_x - m_button_space - m_offset_btn), m_messageBox_y + m_title_height
//					+ m_textView_height + 2, screenWidth, screenHeight);
//			m_button_no.setLocation(2*(m_messageBox_width / 2 - m_button_width / 2
//					+ m_messageBox_x -m_offset_btn),
//					m_messageBox_y + m_title_height + m_textView_height + 2, screenWidth,
//					screenHeight);
//			m_button_cancel.setLocation(2*(m_messageBox_width / 2 + m_button_width / 2
//					+ m_messageBox_x + m_button_space -m_offset_btn), m_messageBox_y + m_title_height
//					+ m_textView_height + 2, screenWidth, screenHeight);
//			add(m_button_sure);
//			add(m_button_no);
//			add(m_button_cancel);
//			break;
//		}
//
//		m_title.setLocation(2*(m_messageBox_x + m_space ), m_messageBox_y+2*m_offset, screenWidth,
//				screenHeight);
//		m_textView.setLocation(2*(m_messageBox_x + m_space ), m_messageBox_y + m_title_height +getFontHeight(m_textSize)/2-5,
//				screenWidth, screenHeight);
//
//		m_image_title.setSize(m_messageBox_width, m_title_height);
//		m_image_text.setSize(m_messageBox_width, m_textView_height);
//		m_image_button.setSize(m_messageBox_width, m_button_hight + 3*m_offset);
//
//		m_image_title.setBackgroundResource(R.drawable.popup_top);
//		m_image_text.setBackgroundResource(R.drawable.popup_confirm);
//		m_image_button.setBackgroundResource(R.drawable.popup_confirm_yesno);
//
//		m_image_title.setLocation(2*m_messageBox_x, m_messageBox_y+1, screenWidth, screenHeight);
//		m_image_text.setLocation(2*m_messageBox_x, m_messageBox_y + m_title_height, screenWidth, screenHeight);
//		m_image_button.setLocation(2*m_messageBox_x, m_messageBox_y+ m_title_height + m_textView_height, screenWidth, screenHeight);
//
//		add(m_image_title);
//		add(m_image_text);		
//		add(m_title);
//		add(m_textView);
//		addToLayout(m_layout);
//		layout.addView(m_layout);
//		
//	}
//
//	/**
//	 * YES按鈕的點擊事件
//	 * 
//	 * @param l
//	 */
//	public void setOnClickListenerSure(OnClickListener l) {
//		if (m_button_sure == null || m_button_sure == null) {
//			return;
//		}
//
//		m_button_sure.setOnClickListener(l);
//	}
//
//	/**
//	 * NO按鈕的點擊事件
//	 * 
//	 * @param l
//	 */
//	public void setOnClickListenerNo(OnClickListener l) {
//		if (m_button_no == null || m_button_no == null) {
//			return;
//		}
//
//		m_button_no.setOnClickListener(l);
//
//	}
//
//	/**
//	 * CANCEL按鈕的點擊事件
//	 * 
//	 * @param l
//	 */
//	public void setOnClickListenerCancel(OnClickListener l) {
//
//		if (m_button_cancel == null || m_button_cancel == null) {
//			return;
//		}
//		m_button_cancel.setOnClickListener(l);
//
//	}
//	
//	public  void yesPerformClick(){
//		if (m_button_sure == null || m_button_sure == null) {
//			return;
//		}
//
//		m_button_sure.performClick();
//	}
//	public  void noPerformClick(){
//		if (m_button_no == null || m_button_no == null) {
//			return;
//		}
//
//		m_button_no.performClick();
//	}
//	public  void cancelPerformClick(){
//		if (m_button_cancel == null || m_button_cancel == null) {
//			return;
//		}
//
//		m_button_cancel.performClick();
//	}
//	
//
//	/**
//	 * 把本类的布局从父布局中删除
//	 * 
//	 * @param layout父布局
//	 */
//	public void removeFromView(ViewGroup layout) {
//		if (m_layout == null) {
//			return;
//		}
//		m_layout.removeAllViews();
//		layout.removeView(m_layout);
//	}
//	
//	/**
//	 * 计算某个字符出现的次数，用此方法计算有多少次换行从而得出有文字有多少行
//	 * @param target
//	 * @return
//	 */
//	public  int getLineCount(String target) {
//		int counts = 0;
//		String temp = m_text;
//		int needle = -1;
//		while (true) {
//			needle = temp.indexOf(target, needle + 1);
//			if (needle >= 0)
//				counts++;
//			else {
//				break;
//			}
//
//			try{ 
//				Thread.sleep(2); 
//			}
//			catch(Exception ex)
//			{ 
//				ex.printStackTrace(); 
//			} 
//		}
//
//		return counts;
//	}
//	
//	/**
//	 * 通过字号得出字体的高
//	 * @param fontSize
//	 * @return
//	 */
//	  public int getFontHeight(float fontSize){   
//	    Paint paint = new Paint();   
//	    paint.setTextSize(fontSize);   
//	    FontMetrics fm = paint.getFontMetrics();      
//	    return (int) Math.ceil(fm.descent - fm.ascent);
//	    		 
//	    }  
//	  
//	  /**
//	   * 判断MessageBox 是否已显示
//	   * @return true if shown, otherwise is hide
//	   */
//	  public boolean isShown(){
//		  if(TDStaticData.g_msgbox != null){
//			  return true;
//		  }else{
//			  return false;
//		  }
//	  }
//	  
//}

package com.wistron.StereoUI;
 
import java.lang.reflect.Field;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;

/**
 * Copyright(c)2011 Wistron SWPC ALL rights reserved. 
 * @date: 2011-12-20 14:00:27
 * @author :WH1107028
 * @purpose:立体控件基类
 * @postscript:內部代碼冗餘是為了提高動態設置代碼屬性時的低耦合性
 */
public abstract class CSView {

	private static final int   COMPUTE_STD_WIDTH_HEIGHT             = 0x01;//計算標準屏幕下的控件大小
	private static final int   COMPUTE_CUR_WIDTH_HEIGHT_LOCATION_3D = 0x02;//計算當前屏幕下控件的自適應信息
	private static final int   COMPUTE_CUR_WIDTH_HEIGHT_LOCATION_2D = 0x03;//計算2D模式下的控件的自適應信息
	private static final int   COMPUTE_CUR_PADDING                  = 0x04;//計算控件PADDING

	private static final float DEFAULT_WIDTH                        = 1024f,
							   DEFAULT_HEIGHT                       = 600f;
	
	public static final  int   WRAP_CONTENT                         = LayoutParams.WRAP_CONTENT, //控件的默认值
			                   MATCH_PARENT                         = LayoutParams.MATCH_PARENT;

	protected int      m_ltop            = -1,  m_rtop            = -1, //左右屏控件的margin
					   m_lbottom         = -1,  m_rbottom         = -1,	
					   m_lleft           = -1,  m_rleft           = -1,
					   m_lright          = -1,  m_rright          = -1;
	
	protected int 	   m_pad_ltop        = -1,  m_pad_rtop        = -1, //左右屏控件的padding
			      	   m_pad_lbottom     = -1,  m_pad_rbottom     = -1,
			      	   m_pad_lleft       = -1,  m_pad_rleft       = -1,
			      	   m_pad_lright      = -1,  m_pad_rright      = -1;
	
	protected int 	   m_std_lx          = -1,  m_std_rx          = -1, 
				  	   m_std_ly          = -1,  m_std_ry          = -1, //以1024x600爲準
				  	   m_cur_lx          = -1,  m_cur_rx          = -1,
				  	   m_cur_ly          = -1,  m_cur_ry          = -1; //以當前屏幕大小爲準
				 
	
	protected int 	   m_std_width       = -2,  m_std_height      = -2, //以1024x600爲準
				  	   m_cur_width       = -2,  m_cur_height      = -2; //以當前屏幕大小爲準
	
	protected int 	   m_org_x           = -1,  m_org_y           = -1, //原始传入的坐标
			      	   m_org_width       = -2,  m_org_height      = -2, //原始传入的大小
			      	   m_org_mar_top     = -1,  m_org_mar_bottom  = -1, //原始传入的margin
			      	   m_org_mar_left    = -1,  m_org_mar_right   = -1,
			      	   m_org_pad_top     = -1,  m_org_pad_bottom  = -1, //原始传入的padding
			      	   m_org_pad_left    = -1,  m_org_pad_right   = -1;
	
	protected int 	   m_screen_width    = -1, 
				  	   m_screen_height   = -1;
	
	protected int 	   m_offset          = 0;	//偏移量
	
	protected float    m_scale_rate_w    = 1f,   //压缩比率
			           m_scale_rate_h    = 1f;
	
	protected boolean  m_is_3D           = true;	
	
	protected boolean  m_border_check    = true;
	
	protected boolean  m_width_wrap_content  = true,
				       m_height_wrap_content = true;
	
	protected Context  m_context;
	
	public CSView() {

	}
	
	public CSView(Context context) {
		m_context = context;
		
		Class<?> cint  = null; //类的实例
		Object   obj   = null; //类的对象
		Field    field = null; //状态栏高度的变量
		int      objid = 0,    //资源标识符
				 sbarh = 0;    //状态栏高度
		
		//获取状态栏高度
		try {
			if(android.os.Build.VERSION.SDK_INT >= 11){
				//仅对有状态栏的版本使用此方法
				cint  = Class.forName("com.android.internal.R$dimen");
				obj   = cint.newInstance();
				field = cint.getField("status_bar_height");
				objid = Integer.parseInt(field.get(obj).toString());
				sbarh = m_context.getResources().getDimensionPixelSize(objid);
//				sbarh = 0;
			}else{
				sbarh = 0;
			}
			Log.e("CSView","[CSView]状态栏高度 = " + sbarh);
		} catch (Exception e1) {
			Log.e("CSView","[CSView]获取状态栏高度失败");
			e1.printStackTrace();
		}  
		
		//获取屏幕宽度和高度
		WindowManager wm = (WindowManager) m_context.getSystemService(Context.WINDOW_SERVICE);
		if(wm != null){
//			if (m_context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//				m_screen_width  = wm.getDefaultDisplay().getWidth();
//				m_screen_height = wm.getDefaultDisplay().getHeight() + sbarh;
//			}else{
//				m_screen_width  = wm.getDefaultDisplay().getHeight() + sbarh;
//				m_screen_height = wm.getDefaultDisplay().getWidth();
//			}
			m_screen_width  = wm.getDefaultDisplay().getWidth();
			m_screen_height = wm.getDefaultDisplay().getHeight();
			

		}
		
		//比较获取的屏幕高度是否已经除除了状态栏高度
		if (m_context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			Log.i("CSView", "[CSView]显示模式：横屏");
			if(m_screen_height % 100 == 0){
//				m_screen_height -= sbarh;
			}
		}
		else if (m_context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			Log.i("CSView", "[CSView]显示模式：竖屏");
			if(m_screen_height % 10 == 0){
//				m_screen_height -= sbarh;
			}
		}
		
		//保存压缩比率
		m_scale_rate_w = (float)m_screen_width/DEFAULT_WIDTH;
		m_scale_rate_h = (float)m_screen_height/DEFAULT_HEIGHT;
		Log.d("CSWindow","Default:width = " + m_screen_width + "; height = " + m_screen_height);
	}
	
	/**
	 * 設置控件的邊距
	 * @param Top
	 * @param Bottom
	 * @param Left
	 * @param Right
	 * @return 
	 * @since 1.0
	 */
	public void setMargin(int top, int bottom, int left, int right){
		m_org_mar_top    = top;
		m_org_mar_bottom = bottom;
		m_org_mar_left   = left;
		m_org_mar_right  = right;
		
		//左
		m_lleft = left/2 - m_offset;
		m_rleft = left/2 + m_screen_width/2 - m_offset;
		
		//右
		m_lright = right/2 - m_offset;
		m_rright = right/2 + m_screen_width/2 + m_offset;
		
		//上
		m_ltop = m_rtop = top;
		
		//下
		m_lbottom = m_rbottom = bottom;
		
		build(COMPUTE_STD_WIDTH_HEIGHT);
		build(COMPUTE_CUR_WIDTH_HEIGHT_LOCATION_3D);
	}
	
	public void setLeft(int left){
		m_org_mar_left = left;
		if(m_is_3D){
			m_lleft = (int)((float)(left - m_offset)*((float)m_screen_width/DEFAULT_WIDTH)/2f);
			m_rleft = (int)((float)(left + m_screen_width - m_offset)*(m_screen_width/DEFAULT_WIDTH)/2f);
		}else{
			m_lleft = (int)((float)(left - m_offset)*((float)m_screen_width/DEFAULT_WIDTH));
			m_rleft = (int)((float)(left + m_screen_width - m_offset)*(m_screen_width/DEFAULT_WIDTH));
		}
	}
	
	public void setRight(int right){
		m_org_mar_right = right;
		if(m_is_3D){
			m_lright = (int)((float)(right - m_offset)*((float)m_screen_width/DEFAULT_WIDTH)/2f);
			m_rright = (int)((float)(right + m_screen_width + m_offset)*((float)m_screen_width/DEFAULT_WIDTH)/2f);
		}else{
			m_lright = (int)((float)(right - m_offset)*((float)m_screen_width/DEFAULT_WIDTH));
			m_rright = (int)((float)(right + m_screen_width + m_offset)*((float)m_screen_width/DEFAULT_WIDTH));
		}
	}

	public void setTop(int top){
		m_org_mar_top = top;
		m_ltop = m_rtop = (int)((float)top*((float)m_screen_height/DEFAULT_HEIGHT));
	}

	public void setBottom(int bottom){
		m_org_mar_bottom = bottom;
		m_lbottom = m_rbottom = (int)((float)bottom*((float)m_screen_height/DEFAULT_HEIGHT));
	}

	/**
	 * 設置控件的位置
	 * @param x 用戶視圖下的x
	 * @param y 用戶視圖下的y
	 * @since 2.0
	 */
	public void setLocation(int x, int y){

		//记录原始位置信息
		m_org_x = x;
		m_org_y = y;
//		if(x >= m_screen_width){
//			m_org_x = m_screen_width - m_std_width;
//		}
//		if(y >= m_screen_height){
//			m_org_y = m_screen_height - m_std_height;
//		}

		if(m_border_check){

			//左控件
			m_std_lx = x/2 - m_offset;
			m_std_ly = y;
			m_lleft = m_std_lx;
			m_lright = m_std_lx + m_std_width;
			m_ltop = m_std_ly;
			m_lbottom = m_std_ly + m_std_height;

			//右控件
			m_std_rx = x/2 + 2*m_offset+m_screen_width/2;
			m_std_ry = y;
			m_rleft = m_std_rx;
			m_rright = m_std_rx + m_std_width;
			m_rtop = m_std_ry;
			m_rbottom = m_std_ry + m_std_height;
		}else{
			//左控件
			if(x/2 - m_offset<0){
				m_std_lx = 0;
			}else if(x/2 + m_offset + m_screen_width/2>=m_screen_width){
				m_std_lx = m_screen_width/2 -2*m_offset-m_std_width;

			}else{
				m_std_lx = x/2 - m_offset;
			}
			
			if(y>=m_screen_height){
				m_std_ly = m_screen_height - m_std_height;
				m_std_ry = m_screen_height - m_std_height;
			}else if(y<0){
				m_std_ly = 0;
				m_std_ry = 0;

			}else{
				m_std_ly = y;
				m_std_ry = y;
			}

			m_lleft = m_std_lx;
			m_lright = m_std_lx + m_std_width;
			m_ltop = m_std_ly;
			m_lbottom = m_std_ly + m_std_height;

			//右控件		
			if(x/2 + m_screen_width/2 + m_offset>=m_screen_width){
				m_std_rx = m_screen_width-m_std_width;
			}else{
				m_std_rx = x/2 + m_screen_width/2 + m_offset;
			}	
			m_rleft = m_std_rx;
			m_rright = m_std_rx + m_std_width;
			m_rtop = m_std_ry;
			m_rbottom = m_std_ry + m_std_height;
		}

		build(COMPUTE_CUR_WIDTH_HEIGHT_LOCATION_3D);
	}

	/**
	 * 設置控件的位置
	 * @param x 用戶視圖下的x
	 * @param y 用戶視圖下的y
	 * @param screenWidth 當前屏幕的寬度
	 * @param screenHeight 當前屏幕的高度
	 * @since 1.0
	 */
	public void setLocation(int x, int y, int screenWidth, int screenHeight){
		//记录原始位置信息
		m_org_x = x;
		m_org_y = y;

		//屏幕
		m_screen_width = screenWidth;
		m_screen_height = screenHeight;

		if(m_border_check){

			//左控件
			m_std_lx = x/2 - m_offset;
			m_std_ly = y;
			m_lleft = m_std_lx;
			m_lright = m_std_lx + m_std_width;
			m_ltop = m_std_ly;
			m_lbottom = m_std_ly + m_std_height;

			//右控件
			m_std_rx = x/2 + 2*m_offset+m_screen_width/2;
			m_std_ry = y;
			m_rleft = m_std_rx;
			m_rright = m_std_rx + m_std_width;
			m_rtop = m_std_ry;
			m_rbottom = m_std_ry + m_std_height;
		}else{
			//左控件
			if(x/2 - m_offset<0){
				m_std_lx = 0;
			}else if(x/2 + m_offset + m_screen_width/2>=m_screen_width){
				m_std_lx = m_screen_width/2 -2*m_offset-m_std_width;

			}else{
				m_std_lx = x/2 - m_offset;
			}
			if(y>=m_screen_height){
				m_std_ly = m_screen_height - m_std_height;
				m_std_ry = m_screen_height - m_std_height;
			}else if(y<0){
				m_std_ly = 0;
				m_std_ry = 0;

			}else{
				m_std_ly = y;
				m_std_ry = y;
			}

			m_lleft = m_std_lx;
			m_lright = m_std_lx + m_std_width;
			m_ltop = m_std_ly;
			m_lbottom = m_std_ly + m_std_height;

			//右控件		
			if(x/2 + m_screen_width/2 + m_offset>=m_screen_width){
				m_std_rx = m_screen_width-m_std_width;
			}else{
				m_std_rx = x/2 + m_screen_width/2 + m_offset;
			}	
			m_rleft = m_std_rx;
			m_rright = m_std_rx + m_std_width;
			m_rtop = m_std_ry;
			m_rbottom = m_std_ry + m_std_height;
		}

		build(COMPUTE_CUR_WIDTH_HEIGHT_LOCATION_3D);
	}
	
	/**
	 * 設置控件的绝对位置
	 * 这会跳过1024：600的自适应标准
	 * @param x 用戶視圖下的x
	 * @param y 用戶視圖下的y
	 * @since 2.0
	 */
	public void setAbsLocation(int x, int y){
		//记录原始位置信息
		m_org_x = x;
		m_org_y = y;
				
		m_cur_lx = x;
		m_cur_ly = y;
		m_cur_rx = x + m_cur_width;
		m_cur_ry = y;
	}
	
	/**
	 * 設置控件的大小
	 * @param width
	 * @param height
	 * @since 1.0
	 */
	public void setSize(int width, int height){
		//记录原始位置信息
		m_org_width = width;
		m_org_height = height;
				
		m_std_width = width;
		m_std_height = height;
		
		m_width_wrap_content = false;
		m_height_wrap_content = false;
		build(COMPUTE_CUR_WIDTH_HEIGHT_LOCATION_3D);
		
		if(width == WRAP_CONTENT){
			m_width_wrap_content = true;
		}
		if(height == WRAP_CONTENT){
			m_height_wrap_content = true;
		}
	}
	
	/**
	 * 设置控件的绝对大小
	 * 这会跳过1024：600的自适应标准
	 * @param width
	 * @param height
	 * @since 2.0
	 */
	public void setAbsSize(int width, int height){
		//记录原始位置信息
		m_org_width = width;
		m_org_height = height;
		
		m_cur_width = width;
		m_cur_height = height;
	}

	/**
	 * 設置控件的可见性
	 * 
	 * @param visibility
	 */
	public abstract void setVisibility(int visibility);
	
	/**
	 * 設置屏幕
	 * @param screenWidth
	 * @param screenHeight
	 */
	public void setDisplayFrame(int screenWidth,int screenHeight){
		this.m_screen_width = screenWidth;
		this.m_screen_height = screenHeight;
	}
	
	/**
	 * 設置控件的可用性
	 * @param enabled
	 */
	public abstract void setEnable(boolean enabled);
	
	/**
	 * 设置控件内部的内容与控件的边距
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 */
	public void setPadding(int left,int top,int right,int bottom){
		m_org_pad_left   = left;
		m_org_pad_top    = top;
		m_org_pad_right  = right;
		m_org_pad_bottom = bottom;
		
		if(m_is_3D){
			//左
			m_pad_lleft = left/2;
			m_pad_rleft = left/2;
			
			//右
			m_pad_lright = right/2;
			m_pad_rright = right/2;
			
			//上
			m_pad_ltop = m_pad_rtop = top;
			
			//下
			m_pad_lbottom = m_pad_rbottom = bottom;

		}else{
			//左
			m_pad_lleft = left;
			m_pad_rleft = left;
			
			//右
			m_pad_lright = right;
			m_pad_rright = right;
			
			//上
			m_pad_ltop = m_pad_rtop = top;
			
			//下
			m_pad_lbottom = m_pad_rbottom = bottom;
			
		}
		
		//計算Padding自適應
		build(COMPUTE_CUR_PADDING);
	}
	
	/**
	 * 觸摸事件
	 * @param event
	 * @return
	 */
	public abstract boolean touchEvent(MotionEvent event);
	
	/**
	 * 把本Group添加至指定ViewGroup
	 * @param layout
	 * @return
	 */
	public abstract void addToLayout(ViewGroup layout);
	

	/**
	 * 設置控件的偏移
	 * @param offset
	 */
	public void setOffset(int offset){
		m_offset = offset;
		build(COMPUTE_CUR_WIDTH_HEIGHT_LOCATION_3D);
	}
	
	/**
	 * 2D/3D切换
	 * @param is3D
	 */
	@SuppressWarnings("all")
	public void setDimension(boolean is3D) {
		m_is_3D = is3D;
		if(m_is_3D){
			//計算位置和大小
			build(COMPUTE_CUR_WIDTH_HEIGHT_LOCATION_3D);
			if(m_cur_width != MATCH_PARENT && m_cur_width != WRAP_CONTENT){
				m_cur_width  = m_cur_width/2;
				m_cur_height = m_cur_height;
			}
			
			//計算Padding
			m_pad_lleft   = m_pad_rleft   = m_org_pad_left/2;
			m_pad_lright  = m_pad_rright  = m_org_pad_right/2;
			m_pad_ltop    = m_pad_rtop    = m_org_pad_top;
			m_pad_lbottom = m_pad_rbottom = m_org_pad_bottom;
			
			//計算Margin
			setLeft(m_org_mar_left);
			setRight(m_org_mar_right);
			setTop(m_org_mar_top);
			setBottom(m_org_mar_bottom);
			
			build(COMPUTE_CUR_PADDING);
		}else{
			//計算位置和大小
			build(COMPUTE_CUR_WIDTH_HEIGHT_LOCATION_2D);
			
			//計算Padding
			m_pad_lleft   = m_pad_rleft   = m_org_pad_left;
			m_pad_lright  = m_pad_rright  = m_org_pad_right;
			m_pad_ltop    = m_pad_rtop    = m_org_pad_top;
			m_pad_lbottom = m_pad_rbottom = m_org_pad_bottom;
			
			//計算Margin
			setLeft(m_org_mar_left);
			setRight(m_org_mar_right);
			setTop(m_org_mar_top);
			setBottom(m_org_mar_bottom);
			
			build(COMPUTE_CUR_PADDING);
		}
	}
	
	/*
	 * 設置是否允許控件的位置越出屏幕
	 */
	public void setOutOfScreenCheck(boolean checkScreen) {
		m_border_check = checkScreen;
	}
	
	/**
	 * 內部參數計算，根據當前屏幕重新計算所有參數
	 * @param index 計算方式
	 */
	private void build(int index) {
		switch (index) {
		case COMPUTE_STD_WIDTH_HEIGHT:
		{//step 1:compute the m_std_width and m_std_height
			m_std_width = (m_lright - m_lleft) >= (m_rright - m_rleft) ? (m_lright - m_lleft) : (m_rright - m_rleft);
			m_std_height = (m_lbottom - m_ltop) >= (m_rbottom - m_rtop) ? (m_lbottom - m_ltop) : (m_rbottom - m_rtop);
		}
		break;
		case COMPUTE_CUR_WIDTH_HEIGHT_LOCATION_3D:
		{//step 2:compute the m_cur_width and m_cur_height with m_screen_width and m_screen_height
			if(m_org_width != MATCH_PARENT && m_org_width != WRAP_CONTENT){
				m_cur_width = (int)(((float)m_screen_width/DEFAULT_WIDTH) * m_std_width);	
			}
			if(m_org_height != MATCH_PARENT && m_org_height != WRAP_CONTENT){
				m_cur_height = (int)(((float)m_screen_height/DEFAULT_HEIGHT) * m_std_height);
			}
			
			m_cur_lx = (int)(((float)m_screen_width/DEFAULT_WIDTH) * m_std_lx) - m_offset;
			m_cur_ly = (int)(((float)m_screen_height/DEFAULT_HEIGHT) * m_std_ly + 0.5f);
			m_cur_rx = m_cur_lx + m_screen_width/2 + 2*m_offset;
			m_cur_ry = (int)(((float)m_screen_height/DEFAULT_HEIGHT) * m_std_ry + 0.5f);
		}
		break;
		case COMPUTE_CUR_WIDTH_HEIGHT_LOCATION_2D:
		{//step 3:compute the m_cur_width and m_cur_height with m_screen_width and m_screen_height in 2D mode
			if(m_org_width != MATCH_PARENT && m_org_width != WRAP_CONTENT){
				m_cur_width = (int)(((float)m_screen_width/DEFAULT_WIDTH) * m_org_width);
			}
			if(m_org_height != MATCH_PARENT && m_org_height != WRAP_CONTENT){
				m_cur_height = (int)(((float)m_screen_height/DEFAULT_HEIGHT) * m_org_height);
			}
			m_cur_lx = (int)(((float)m_screen_width/DEFAULT_WIDTH) * m_org_x);
			m_cur_ly = (int)(((float)m_screen_height/DEFAULT_HEIGHT) * m_org_y + 0.5f);
			m_cur_rx = - (m_screen_width-m_cur_lx);
			m_cur_ry = - m_cur_ly;
		}
		break;
		case COMPUTE_CUR_PADDING:
		{//step 4:compute the m_cur_l/t/r/b
			m_pad_lleft   = m_pad_rleft   = (int)(((float)m_screen_width/DEFAULT_WIDTH) * m_pad_lleft);
			m_pad_lright  = m_pad_rright  = (int)(((float)m_screen_width/DEFAULT_WIDTH) * m_pad_lright);
			m_pad_ltop    = m_pad_rtop    = (int)(((float)m_screen_width/DEFAULT_WIDTH) * m_pad_ltop);
		    m_pad_lright  = m_pad_rright  = (int)(((float)m_screen_width/DEFAULT_WIDTH) * m_pad_lright);
		}
		break;
		}
		
//		if(m_width_wrap_content){
//			m_cur_width = WRAP_CONTENT;
//		}
//		if(m_height_wrap_content){
//			m_cur_height = WRAP_CONTENT;
//		}
	}
	
	/**
	 * 保存立体控件的状态
	 * @return 
	 */
	public abstract Object save();
	
		

	/**
	 * 恢复保存的状态
	 */
	public abstract void restore(Object object);
}
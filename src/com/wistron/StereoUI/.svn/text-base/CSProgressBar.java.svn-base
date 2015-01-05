package com.wistron.StereoUI;
 
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.ProgressBar;

/**
 * 2D控件 ProgressBar的封裝類
 * @author WangWei
 * 
 */
@SuppressWarnings("deprecation")
public class CSProgressBar extends CSView{

	protected ProgressBar[] m_progressBars = null;     
	private int           m_style        = android.R.attr.progressBarStyleHorizontal;

	public CSProgressBar(Context context) {
		super(context);
		m_context = context;

		m_progressBars = new ProgressBar[2];
		for (int i = 0; i < m_progressBars.length; i++) {
			m_progressBars[i] = new ProgressBar(context,null,android.R.attr.progressBarStyleHorizontal);
		}
	}
	
	/**
	 * 初始化CSProgressBar
	 * @param context
	 * @param style 進度條的樣式（如果設置為默認，請填寫0）
	 */
	public CSProgressBar(Context context,int style) {
		super(context);
		m_context = context;
		
		if(style == 0){
			style = android.R.attr.progressBarStyleHorizontal;
		}
		this.m_style = style;
			
		m_progressBars = new ProgressBar[2];
		for (int i = 0; i < m_progressBars.length; i++) {
			m_progressBars[i] = new ProgressBar(context,null,m_style);
		}
	}

	/**
	 * 設置控件的可見性
	 */
	@Override
	public void setVisibility(int visibility) {
		if(m_progressBars[0] == null || m_progressBars[1] == null){
			return;
		}

		m_progressBars[0].setVisibility(visibility);
		m_progressBars[1].setVisibility(visibility);
	}

	/**
	 * 設置進度條的最大進度值
	 * @param max
	 */
	public void setMax(int max) {
		if(m_progressBars[0] == null || m_progressBars[1] == null){
			return;
		}

		m_progressBars[0].setMax(max);
		m_progressBars[1].setMax(max);
	}
	/**
	 * 設置控件的可用性
	 */
	@Override
	public void setEnable(boolean enabled) {
		if(m_progressBars[0] == null || m_progressBars[1] == null){
			return;
		}

		m_progressBars[0].setEnabled(enabled);
		m_progressBars[1].setEnabled(enabled);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void setDimension(boolean is3d) {
		super.setDimension(is3d);
		if(m_progressBars[0] == null || m_progressBars[1] == null){
			return;
		}
		
		if(m_progressBars[0].getLayoutParams() != null && m_progressBars[1].getLayoutParams() != null){
			if(m_cur_width == WRAP_CONTENT || m_cur_height == WRAP_CONTENT){
				m_progressBars[0].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_lx, m_cur_ly));
				m_progressBars[1].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_rx, m_cur_ry));
			}else{
				m_progressBars[0].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_lx, m_cur_ly));
				m_progressBars[1].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_rx, m_cur_ry));
			}
			m_progressBars[0].requestLayout();
			m_progressBars[1].requestLayout();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		if(m_progressBars[0] == null || m_progressBars[1] == null){
			return;
		}
		if(m_progressBars[0].getLayoutParams() != null && m_progressBars[1].getLayoutParams() != null){
			if(m_cur_width == WRAP_CONTENT || m_cur_height == WRAP_CONTENT){
				m_progressBars[0].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_lx, m_cur_ly));
				m_progressBars[1].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_rx, m_cur_ry));
			}else{
				m_progressBars[0].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_lx, m_cur_ly));
				m_progressBars[1].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_rx, m_cur_ry));
			}
			m_progressBars[0].requestLayout();
			m_progressBars[1].requestLayout();
		}
	}
	
	/**
	 * 設置控件的觸摸事件
	 */
	@Override
	public boolean touchEvent(MotionEvent event) {
		boolean state = false;	
		
		return state;
	}

	/**
	 * 获取控件的可见性
	 * @return
	 */
	public int getVisibility(){
		int visile = View.NO_ID;
		if(m_progressBars[0] == null || m_progressBars[1] == null){
			visile = View.NO_ID;
		}

		visile = m_progressBars[0].getVisibility();

		return visile;
	}

	/**
	 * 將控件添加到layout中去
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void addToLayout(ViewGroup alayout) {
		if(m_progressBars[0] == null || m_progressBars[1] == null){
			return;
		}

		if(alayout == null){
			return;
		}

		//設置控件在屏幕中顯示的位置
		if(m_cur_width == WRAP_CONTENT || m_cur_height == WRAP_CONTENT){
			m_progressBars[0].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_lx, m_cur_ly));
			m_progressBars[1].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_rx, m_cur_ry));
		}else{
			m_progressBars[0].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_lx, m_cur_ly));
			m_progressBars[1].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height, m_cur_rx, m_cur_ry));
		}

		alayout.addView(m_progressBars[0]);
		alayout.addView(m_progressBars[1]);
	}


	/**
	 * 設置進度條的進度
	 * @param process
	 */
	public void setProgress(int process){
		if(m_progressBars[0] == null ||m_progressBars[1] == null){
			return;
		}
		
		m_progressBars[0].setProgress(process);
		m_progressBars[1].setProgress(process);
	}

	/**
	 * 設置進度條的背景
	 * @param resid
	 */
	public void setBackgroundResource(int resid){
		if(m_progressBars[0] == null ||m_progressBars[1] == null){
			return;
		}	
		
		m_progressBars[0].setBackgroundResource(resid);
		m_progressBars[1].setBackgroundResource(resid);
	}

	/**
	 * 設置進度條的背景
	 * @param resid
	 */
	public void setBackgroundResource(int residL, int residR){
		if(m_progressBars[0] == null ||m_progressBars[1] == null){
			return;
		}	
		
		m_progressBars[0].setBackgroundResource(residL);
		m_progressBars[1].setBackgroundResource(residR);
	}

	/**
	 * 設置進度條的最小寬度
	 * @param minWidth
	 */
	public void setMinimumWidth(int minWidth){
		if(m_progressBars[0] == null || m_progressBars[1] == null){
			return;
		}

		m_progressBars[0].setMinimumWidth(minWidth);
		m_progressBars[1].setMinimumWidth(minWidth);
	}

	/**
	 * 設置進度條的最小高度
	 * @param minHight
	 */
	public void setMinimumHeight(int minHight){
		if(m_progressBars[0] == null || m_progressBars[1] == null){
			return;
		}
		
		m_progressBars[0].setMinimumHeight(minHight);
		m_progressBars[1].setMinimumHeight(minHight);
	}

	/**
	 * 设置控件的ID
	 * @param id
	 */
	public void setId(int id) {
		if(m_progressBars[0] == null || m_progressBars[1] == null){
			return;
		}

		m_progressBars[0].setId(id);
	}

	/**
	 * 设置控件内部的内容与控件的边距
	 */
	@Override
	public void setPadding(int left, int top, int right, int bottom) {
		if(m_progressBars[0] == null || m_progressBars[1] == null){
			return;
		}

		m_progressBars[0].setPadding(left, top, right, bottom);
		m_progressBars[1].setPadding(left, top, right, bottom);
	}
	
	public void setLayoutParams(LayoutParams lParams, LayoutParams rParams){
		if(m_progressBars[0] == null || m_progressBars[1] == null){
			return;
		}
		
		m_progressBars[0].setLayoutParams(lParams);
		m_progressBars[1].setLayoutParams(rParams);
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

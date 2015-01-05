package com.wistron.WiGallery;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.wistron.WiGallery.Sphere.OnScaleListener;

import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;

import Utilities.CSStaticData;


public class ElementGroup 
{
	private boolean m_b_list_change = false;
	private List<ElementList> m_list = null;
	
	public boolean m_b_flip = false;

	protected CSStaticData.ANIMATION_TYPE m_animation_type = CSStaticData.ANIMATION_TYPE.NONE;
	public boolean m_is_drawing = true;
	public boolean m_b_restore = true;
	private CSStaticData.LIST_TYPE m_type = CSStaticData.LIST_TYPE.LIST_INVALID;
	private CSStaticData.LIST_ELEMENT_TYPE m_element_type = CSStaticData.LIST_ELEMENT_TYPE.LIST_ELEMENT_ALL;
	
	private boolean m_b_enter_group = false; //是否进入文件列表标志
	private int m_select_group_index = -1; // 保存当前选择的子链表编号
	
	public int m_index = 0;
	private int m_dategroup_animation_frame_num = 0;
	public SCurve m_scurve = new SCurve();
	private Sphere mSphere  = new Sphere();
	
	//Date_afterclick/Date_afterclick_inverse时使用
	public  float[] pos_offset = {0,0,0}; 
	public ElementGroup()
	{
		setType(CSStaticData.LIST_TYPE.LIST_NONE, false);
//		mSphere.addLandmarker(new Element(CSStaticData.MEDIA_TYPE.NORMAL_IMAGE, 
//				CSStaticData.defaultTextureID, CSStaticData.defaultTextureID, 
//				CSStaticData.choosedTextureID, CSStaticData.notChoosedTextureID, 
//				"1", "2012 12 25", -90, 0 /*116.41667, 39.91667*/));
//		mSphere.addLandmarker(new Element(CSStaticData.MEDIA_TYPE.NORMAL_IMAGE, 
//				CSStaticData.defaultTextureID, CSStaticData.defaultTextureID, 
//				CSStaticData.choosedTextureID, CSStaticData.notChoosedTextureID, 
//				"2", "2012 12 25", -60, 0));
//		mSphere.addLandmarker(new Element(CSStaticData.MEDIA_TYPE.NORMAL_IMAGE, 
//				CSStaticData.defaultTextureID, CSStaticData.defaultTextureID, 
//				CSStaticData.choosedTextureID, CSStaticData.notChoosedTextureID, 
//				"3", "2012 12 25", -30, 0));
//		mSphere.addLandmarker(new Element(CSStaticData.MEDIA_TYPE.NORMAL_IMAGE, 
//				CSStaticData.defaultTextureID, CSStaticData.defaultTextureID, 
//				CSStaticData.choosedTextureID, CSStaticData.notChoosedTextureID, 
//				"4", "2012 12 25", 0, 0));
//		mSphere.addLandmarker(new Element(CSStaticData.MEDIA_TYPE.NORMAL_IMAGE, 
//				CSStaticData.defaultTextureID, CSStaticData.defaultTextureID, 
//				CSStaticData.choosedTextureID, CSStaticData.notChoosedTextureID, 
//				"5", "2012 12 25", 30, 0));
//		mSphere.addLandmarker(new Element(CSStaticData.MEDIA_TYPE.NORMAL_IMAGE, 
//				CSStaticData.defaultTextureID, CSStaticData.defaultTextureID, 
//				CSStaticData.choosedTextureID, CSStaticData.notChoosedTextureID, 
//				"6", "2012 12 25", 60, 0));
//		mSphere.addLandmarker(new Element(CSStaticData.MEDIA_TYPE.NORMAL_IMAGE, 
//				CSStaticData.defaultTextureID, CSStaticData.defaultTextureID, 
//				CSStaticData.choosedTextureID, CSStaticData.notChoosedTextureID, 
//				"7", "2012 12 25", 90, 0));
//		mSphere.addLandmarker(new Element(CSStaticData.MEDIA_TYPE.NORMAL_IMAGE, 
//				CSStaticData.defaultTextureID, CSStaticData.defaultTextureID, 
//				CSStaticData.choosedTextureID, CSStaticData.notChoosedTextureID, 
//				"8", "2012 12 25", 0, 45));
//		mSphere.addLandmarker(new Element(CSStaticData.MEDIA_TYPE.NORMAL_IMAGE, 
//				CSStaticData.defaultTextureID, CSStaticData.defaultTextureID, 
//				CSStaticData.choosedTextureID, CSStaticData.notChoosedTextureID, 
//				"9", "2012 12 25", 0, 90));
//		mSphere.addLandmarker(new Element(CSStaticData.MEDIA_TYPE.NORMAL_IMAGE, 
//				CSStaticData.defaultTextureID, CSStaticData.defaultTextureID, 
//				CSStaticData.choosedTextureID, CSStaticData.notChoosedTextureID, 
//				"10", "2012 12 25", 0, 135));
//		mSphere.addLandmarker(new Element(CSStaticData.MEDIA_TYPE.NORMAL_IMAGE, 
//				CSStaticData.defaultTextureID, CSStaticData.defaultTextureID, 
//				CSStaticData.choosedTextureID, CSStaticData.notChoosedTextureID, 
//				"11", "2012 12 25", 0, 180));
//		mSphere.addLandmarker(new Element(CSStaticData.MEDIA_TYPE.NORMAL_IMAGE, 
//				CSStaticData.defaultTextureID, CSStaticData.defaultTextureID, 
//				CSStaticData.choosedTextureID, CSStaticData.notChoosedTextureID, 
//				"12", "2012 12 25", 0, -45));
//		mSphere.addLandmarker(new Element(CSStaticData.MEDIA_TYPE.NORMAL_IMAGE, 
//				CSStaticData.defaultTextureID, CSStaticData.defaultTextureID, 
//				CSStaticData.choosedTextureID, CSStaticData.notChoosedTextureID, 
//				"13", "2012 12 25", 0, -90));
//		mSphere.addLandmarker(new Element(CSStaticData.MEDIA_TYPE.NORMAL_IMAGE, 
//				CSStaticData.defaultTextureID, CSStaticData.defaultTextureID, 
//				CSStaticData.choosedTextureID, CSStaticData.notChoosedTextureID, 
//				"14", "2012 12 25", 0, -135));
//		mSphere.addLandmarker(new Element(CSStaticData.MEDIA_TYPE.NORMAL_IMAGE, 
//				CSStaticData.defaultTextureID, CSStaticData.defaultTextureID, 
//				CSStaticData.choosedTextureID, CSStaticData.notChoosedTextureID, 
//				"15", "2012 12 25", 0, -180));
//		mSphere.addLandmarker(new Element(CSStaticData.MEDIA_TYPE.NORMAL_IMAGE, 
//				CSStaticData.defaultTextureID, CSStaticData.defaultTextureID, 
//				CSStaticData.choosedTextureID, CSStaticData.notChoosedTextureID, 
//				"16", "2012 12 25", 34.04, -118.15));
		
		mSphere.setOnScaleListener(new OnScaleListener() {
			
			@Override
			public void onScaleChanged(float rate) {
				// Locatrion模式下，当放大或者缩小地球时，修改显示级别以及对应的视角
				if (m_type == CSStaticData.LIST_TYPE.LIST_LOCATION_1
						|| m_type == CSStaticData.LIST_TYPE.LIST_LOCATION_2
						|| m_type == CSStaticData.LIST_TYPE.LIST_LOCATION_3
						|| m_type == CSStaticData.LIST_TYPE.LIST_LOCATION_4)
				{
					// 更新视角位置
					float ratio = (rate -mSphere.getMinScale()) / ( mSphere.getMaxScale() - mSphere.getMinScale());
					//CSStaticData.eye_center_none[1] = 2.0f * ratio;
					CSStaticData.eye_position_location[2] = 3 + rate;
					
					//  更新loacation级别
					CSStaticData.LIST_TYPE type = CSStaticData.LIST_TYPE.LIST_INVALID;
					if(rate < (mSphere.getMaxScale() - mSphere.getMinScale())*0.25 + mSphere.getMinScale()){ //不要写 mRadio >= mMinScale
						type = CSStaticData.LIST_TYPE.LIST_LOCATION_1;
					}else 
					if(rate >= (mSphere.getMaxScale() - mSphere.getMinScale())*0.25 + mSphere.getMinScale() && rate < (mSphere.getMaxScale() - mSphere.getMinScale())*0.5 + mSphere.getMinScale()){
						type = CSStaticData.LIST_TYPE.LIST_LOCATION_2;
					}else
					if(rate >= (mSphere.getMaxScale() - mSphere.getMinScale())*0.5 + mSphere.getMinScale() && rate < (mSphere.getMaxScale() - mSphere.getMinScale())*0.75 + mSphere.getMinScale()){
						type = CSStaticData.LIST_TYPE.LIST_LOCATION_3;
					}else
					if(rate >= (mSphere.getMaxScale() - mSphere.getMinScale())*0.75 + mSphere.getMinScale()){ //不要写 mRadio <= mMaxScale
						type = CSStaticData.LIST_TYPE.LIST_LOCATION_4;
					}
					
					// 刷新数据
					setType(type, false);
				}
			}
		});
	}
	
	public boolean bEnterGroup()
	{
		return m_b_enter_group;
	}
	
	public int getOpenGroupIndex()
	{
		if (m_b_enter_group)
			return m_select_group_index;
		else
			return -1;
	}
	
	
	public int size()
	{
		if (m_list == null)
			return 0;
		
		return m_list.size();
	}
	
	public ElementList get(int index)
	{
		int n = size();
		if (index <0 || index > n-1)
		{
			return null;
		}
		
		return m_list.get(index);
	}

	
	/**
	 * 打开文件夹
	 */
	public void openGroup(int i)
	{
		{//屏幕旋转锁定
			Message msg = new Message();
			msg.what    = WiGalleryActivity.HANDLE_LOCK_SCREEN;
			msg.obj     = Boolean.TRUE;
			if(WiGalleryActivity.mEmergencyHandler != null)
				WiGalleryActivity.mEmergencyHandler.sendMessage(msg);
		}
		
		if (i <0 || i > m_list.size())
		{
			return;
		}
		else
		{		
			m_select_group_index = i;
			
			startAnimation(CSStaticData.ANIMATION_TYPE.DATE_AFTERCLICK);
			// 等待打开动画完成
			while(isMoving())
			{
		        try{ 
		            Thread.sleep(10); 
		           }
		        catch(Exception ex)
		           { 
		            ex.printStackTrace(); 
		           }
			}
			
			m_is_drawing = false;
			
	        try{ 
	            Thread.sleep(100); 
	           }
	        catch(Exception ex)
	           { 
	            ex.printStackTrace(); 
	           }
	        
			m_b_enter_group = true;
			
			m_list.get(i).setEntering(true);
			setChoosedMode(ELEM_STATUS.NORMAL_STATUS);
			
			m_is_drawing = true;
			
			// 等待打开动画完成
			while(m_list.get(i).isMoving())
			{
		        try{ 
		            Thread.sleep(10); 
		           }
		        catch(Exception ex)
		           { 
		            ex.printStackTrace(); 
		           }
			}
			
			m_list.get(i).refresh(0);
			
			if(WiGalleryInterface.m_onGLScenceListener != null)
			{
				WiGalleryInterface.m_onGLScenceListener.onScenceChanged();
			}
			
			if (WiGalleryInterface.m_onGLMoveListener != null)
			{
				int curIndex = getCurIndex();
				int totalIndex = getTotalIndex();
				WiGalleryInterface.m_onGLMoveListener.setScrollBar(curIndex, totalIndex);
			}
		}
		
		//~!
		{//屏幕旋转解除
			Message msg = new Message();
			msg.what    = WiGalleryActivity.HANDLE_LOCK_SCREEN;
			msg.obj     = Boolean.FALSE;
			if(WiGalleryActivity.mEmergencyHandler != null)
				WiGalleryActivity.mEmergencyHandler.sendMessage(msg);
		}
	}
	
	/**
	 * 关闭文件夹
	 */
	public void closeGroup()
	{
		{//屏幕旋转锁定
			Message msg = new Message();
			msg.what    = WiGalleryActivity.HANDLE_LOCK_SCREEN;
			msg.obj     = Boolean.TRUE;
			if(WiGalleryActivity.mEmergencyHandler != null)
				WiGalleryActivity.mEmergencyHandler.sendMessage(msg);
		}
		
		if (m_b_enter_group == true && m_select_group_index != -1)
		{ 	
			if (m_list == null || m_list.size() <= m_select_group_index){
				//还是需要把状态改变为closeGroup后的状态
				m_select_group_index = -1;
				m_b_enter_group = false;
				return;
			}
			
			m_list.get(m_select_group_index).setEntering(false);

			while(isMoving())
			{
		        try{ 
		            Thread.sleep(10); 
		           }
		        catch(Exception ex)
		           { 
		            ex.printStackTrace(); 
		           }
			}
			
			m_is_drawing = false;
			
	        try{ 
	            Thread.sleep(100); 
	           }
	        catch(Exception ex)
	           { 
	            ex.printStackTrace(); 
	           }
	        
	        setChoosedMode(ELEM_STATUS.NORMAL_STATUS);
	        
			
			m_b_enter_group = false;
			//m_element_type = CSStaticData.LIST_ELEMENT_TYPE.LIST_ELEMENT_ALL;

			if (m_select_group_index >= 0 && m_select_group_index <= m_list.size()-1)
			{
				ElementList list = m_list.get(m_select_group_index);
				if (list != null)
					list.m_b_enter = false;
			}
			
			for (int i = 0; i < m_list.size(); i++)
			{
				ElementList list = m_list.get(i);
				list.m_b_enter = false;
			}
			
	        m_is_drawing = true;
	        
			startAnimation(CSStaticData.ANIMATION_TYPE.DATE_AFTERCLICK_INVERISON);
			// 等待打开动画完成
			while(isMoving())
			{
		        try{ 
		            Thread.sleep(10); 
		           }
		        catch(Exception ex)
		           { 
		            ex.printStackTrace(); 
		           }
			}
			
			m_select_group_index = -1;
			refreshPosition(m_index);
			
			if(WiGalleryInterface.m_onGLScenceListener != null)
			{
				WiGalleryInterface.m_onGLScenceListener.onScenceChanged();
			}
			
			if (WiGalleryInterface.m_onGLMoveListener != null)
			{
				int curIndex = getCurIndex();
				int totalIndex = getTotalIndex();
				WiGalleryInterface.m_onGLMoveListener.setScrollBar(curIndex, totalIndex);
			}
		}
		
		//~!
		{//屏幕旋转解除
			Message msg = new Message();
			msg.what    = WiGalleryActivity.HANDLE_LOCK_SCREEN;
			msg.obj     = Boolean.FALSE;
			if(WiGalleryActivity.mEmergencyHandler != null)
				WiGalleryActivity.mEmergencyHandler.sendMessage(msg);
		}
	}
	
	public ElementList getCurrentDisplayedList()
	{
		if (m_b_enter_group)
		{
			if(m_list == null || m_select_group_index >= m_list.size()){
				return null;
			}
			return m_list.get(m_select_group_index);
		}
		else if(m_type == CSStaticData.LIST_TYPE.LIST_NONE)
		{
			if (m_list != null && m_list.size() > 0)
				return m_list.get(0);
			else 
				return null;
		}
		
		return  null;
	}
	
	public ELEM_STATUS getChoosedMode()
	{		
		ElementList list = WiGalleryOpenGLRenderer.m_element_group.getCurrentDisplayedList();
		if (list != null)
		{
			if (list.size() > 0)
			{
				Element element = list.get(0);
				return element.getStatus();
			}
			else
			{
				return ELEM_STATUS.NORMAL_STATUS;
			}
			
		}
		else
		{
			if(m_list == null){
				return ELEM_STATUS.NORMAL_STATUS;
			}
			if (m_list.size() > 0)
			{
				// 只需要判断第一个元素当前模式
				return  m_list.get(0).getStatus();
			}
			else
			{
				return ELEM_STATUS.NORMAL_STATUS;
			}
		}
	}
	
	
	public void setChoosedMode(ELEM_STATUS status)
	{
		//多选模式回调
		if(WiGalleryInterface.m_onGlItemListener != null){
			if(status == ELEM_STATUS.SELECTED_STATUS){
				WiGalleryInterface.m_onGlItemListener.onMultiSelectionModeChanged(true);
			}else{
				WiGalleryInterface.m_onGlItemListener.onMultiSelectionModeChanged(false);
			}
		}
		
		if (WiGalleryOpenGLRenderer.m_element_group == null)
			return;

		if (m_list != null && m_list.size() != 0)
		{
			if (m_b_enter_group)
			{
				if (m_select_group_index >= 0 && m_select_group_index <= m_list.size()-1)
				{
					ElementList eList = m_list.get(m_select_group_index);
					eList.setStatus(status);
				}
			}
			else if(m_type == CSStaticData.LIST_TYPE.LIST_NONE)
			{
				ElementList eList = m_list.get(0);
				eList.setStatus(status);
			}
			else
			{		
				for (int i = 0; i < m_list.size(); i++)
				{
					ElementList eList = m_list.get(i);
					eList.setStatus(status);
				}
			}
		}
	}
	
	public void setElementType(CSStaticData.LIST_ELEMENT_TYPE type)
	{
		if (m_element_type != type)
		{
			m_element_type = type;
			
			m_is_drawing = false;
			
	        try{ 
	            Thread.sleep(100); 
	           }
	        catch(Exception ex)
	           { 
	            ex.printStackTrace(); 
	           } 
	        
			rebuildList();
			
			m_is_drawing = true;
		}
	}
	
	public CSStaticData.LIST_ELEMENT_TYPE getElementType()
	{
		return m_element_type;
	}
	
	public CSStaticData.LIST_TYPE getType()
	{
		return m_type;
	}
	
	
	
	/**
	 *    状态恢复时使用
	 * @param type
	 * @param bEnter
	 * @param index
	 * @param status
	 * @param elemType
	 */
	public void setType(CSStaticData.LIST_TYPE type, boolean bEnter, int selIndex, ELEM_STATUS status, CSStaticData.LIST_ELEMENT_TYPE elemType)
	{
		//m_is_drawing = false;
        try{ 
            Thread.sleep(100); 
           }
        catch(Exception ex)
           { 
            ex.printStackTrace(); 
           }
                

		m_type = type;
		m_element_type = elemType;
		rebuildList();
		
		m_b_enter_group = bEnter;
		m_select_group_index = selIndex;
		
		if (m_b_enter_group && m_select_group_index >= 0)
			m_list.get(m_select_group_index).m_b_enter = true;
		
		setChoosedMode(status);
		setIndex(0);
		
		if (m_type == CSStaticData.LIST_TYPE.LIST_NONE || m_b_enter_group)
		{
			CSStaticData.g_debug_varient = 0.065f;
		}
		else
		{
			CSStaticData.g_debug_varient = 0.03f;
		}
		
		//m_is_drawing = true;
		
		if(WiGalleryInterface.m_onGLScenceListener != null)
		{
			WiGalleryInterface.m_onGLScenceListener.onScenceChanged();
		}

		if (WiGalleryInterface.m_onGLMoveListener != null)
		{
			int curIndex = getCurIndex();
			int totalIndex = getTotalIndex();
			WiGalleryInterface.m_onGLMoveListener.setScrollBar(curIndex, totalIndex);
		}
	}
	
	public void rebuildList(CSStaticData.LIST_TYPE type, boolean bEnter, int selIndex, ELEM_STATUS status, CSStaticData.LIST_ELEMENT_TYPE elemType)
	{              

		m_type = type;
		m_element_type = elemType;
		
		if (m_list != null)
			m_list.clear();
		
		// 获取链表
		m_list = WiGalleryOpenGLRenderer.m_data_manager.GetListArray(m_type, m_element_type);
		
		m_b_enter_group = bEnter;
		m_select_group_index = selIndex;
		
		if (m_b_enter_group && m_select_group_index >= 0){
			if(m_list != null){
				if(m_select_group_index < m_list.size()){
					ElementList eList = m_list.get(m_select_group_index);
					if(eList != null)
						eList.m_b_enter = true;
				}
			}
		}
		
		setChoosedMode(status);                                                                                                                                                                                                                                                           
		
		if (m_type == CSStaticData.LIST_TYPE.LIST_NONE || m_b_enter_group)
		{
			CSStaticData.g_debug_varient = 0.065f;
		}
		else
		{
			CSStaticData.g_debug_varient = 0.03f;
		}
		
		// 链表排序
		setOrder(CSStaticData.g_sort_order_mode);
				
		if(WiGalleryInterface.m_onGLScenceListener != null)
		{
			WiGalleryInterface.m_onGLScenceListener.onScenceChanged();
		}
	}
	
	/**
	 * 设置当前显示类型
	 * @param type
	 */
	public void setType(CSStaticData.LIST_TYPE type, boolean bAnimation)
	{
		if (type != m_type || CSStaticData.g_surface_invalidate)
		{
			if (bAnimation)
			{
				if (m_type == CSStaticData.LIST_TYPE.LIST_NONE)
				{
					CSStaticData.g_debug_varient = 0.065f;
					// 退出动画
					if(m_list != null && m_list.size() != 0){
						m_list.get(0).m_b_enter = true;
						m_list.get(0).setEntering(false);
					}
	
					while(isMoving())
					{
				        try{ 
				            Thread.sleep(10); 
				           }
				        catch(Exception ex)
				           { 
				            ex.printStackTrace(); 
				           }
					}
					
				}
				else if (m_type == CSStaticData.LIST_TYPE.LIST_DATE)
				{
					if (!m_b_enter_group)
					{
						CSStaticData.g_debug_varient = 0.03f;
						startAnimation(CSStaticData.ANIMATION_TYPE.DATE_CLOSING);
						if (!CSStaticData.g_surface_invalidate) {
							startAnimation(CSStaticData.ANIMATION_TYPE.DATE_CLOSING);
							}
						
					}
					else
					{
						CSStaticData.g_debug_varient = 0.065f;
						
						if(m_list != null && m_list.size() != 0){
							m_list.get(m_select_group_index).m_b_enter = true;
							m_list.get(m_select_group_index).setEntering(false);
						}
					}
					
					
					while(isMoving())
					{
				        try{ 
				            Thread.sleep(10); 
				           }
				        catch(Exception ex)
				           { 
				            ex.printStackTrace(); 
				           }
					}
				}
			}
			
			m_is_drawing = false;
	        try{ 
	            Thread.sleep(100); 
	           }
	        catch(Exception ex)
	           { 
	            ex.printStackTrace(); 
	           }
	        
	        setChoosedMode(ELEM_STATUS.NORMAL_STATUS);
	        
	        // 更新场景与数据
	        CSStaticData.LIST_TYPE old_type = m_type;

			m_type = type;
			
			m_b_enter_group = false;
			//m_element_type = CSStaticData.LIST_ELEMENT_TYPE.LIST_ELEMENT_ALL;
			rebuildList();
			
			// 进入动画
			if (bAnimation)
			{
				if (m_type == CSStaticData.LIST_TYPE.LIST_DATE)
				{
					startAnimation(CSStaticData.ANIMATION_TYPE.DATE_OPENING);
					
				}
				else if (m_type == CSStaticData.LIST_TYPE.LIST_NONE)
				{			
						// 第一次不做动画
					if(m_list != null && m_list.size() != 0){
						m_list.get(0).m_b_enter = true;
						m_list.get(0).startAnimation(CSStaticData.ANIMATION_TYPE.NONEGROUP_OPENING);		
					}
				}
			}
			
			m_is_drawing = true;
			
			if(WiGalleryInterface.m_onGLScenceListener != null)
			{
				WiGalleryInterface.m_onGLScenceListener.onScenceChanged();
			}

			if (WiGalleryInterface.m_onGLMoveListener != null)
			{
				int curIndex = getCurIndex();
				int totalIndex = getTotalIndex();
				WiGalleryInterface.m_onGLMoveListener.setScrollBar(curIndex, totalIndex);
			}
		}
	}
	
	/**
	 * 添加文件后，必须重新刷新链表
	 */
	public void addElement()
	{
		m_is_drawing = false;
		
        try{ 
            Thread.sleep(100); 
           }
        catch(Exception ex)
           { 
            ex.printStackTrace(); 
           } 
        
        
		rebuildList();
		
		m_is_drawing = true;
	}
	
	/**
	 * 删除所有链表元素
	 */
	public void clear()
	{
		if (m_list != null)
		{
			int size = m_list.size();
			for (int i = 0; i < size; i++)
			{
				ElementList list = m_list.get(i);
				
				list.clear();
			}
			m_list.clear();
		}
	}
	
	/**
	 * 删除选择的文件或者文件夹
	 */
	public void delete()
	{
		m_is_drawing = false;
        try{ 
            Thread.sleep(100); 
           }
        catch(Exception ex)
           { 
            ex.printStackTrace(); 
           } 
        
        
		if(m_b_enter_group)
		{
			// 删除子文件夹里面的文件
			ElementList list = m_list.get(m_select_group_index);
			list.deleteSeleted();
			
			if (list.size() == 0)
			{
				// 空文件夹
				m_list.remove(m_select_group_index);
				m_select_group_index = -1;
			}
		}
		else
		{
			// 删除文件夹，包括CSStaticData.LIST_TYPE.LIST_NONE模式下的文件
			if (m_type == CSStaticData.LIST_TYPE.LIST_NONE)
			{
				ElementList list = m_list.get(0);
				list.deleteSeleted();
			}
			else
			{
		        Iterator<ElementList> it = m_list.iterator();   
		        while (it.hasNext())    
		        {   
		        	ElementList list = it.next();
		        	if (list != null && list.isChoosed())
		        	{
		        		list.deleteAll();
		        		it.remove();
		        	}
		        } 
			}
		}
		
		refreshPosition(0);
		
		m_is_drawing = true;
	}
	
	public void onDraw(GL10 gl, MEDIA_VIEW view)
	{
		if (m_b_enter_group)
		{
			if (!m_is_drawing)
				return;
			
			if (m_list == null || m_list.size() <= 0)
				return;
			
			if (m_select_group_index < 0 || m_select_group_index >= m_list.size())
			{
				m_select_group_index = -1;
				m_b_enter_group = false;
				return;
			}
			
			ElementList list = m_list.get(m_select_group_index);
			list.m_b_enter = true;
			list.onDraw(gl, view);
		}
		else
		{
			if (m_type == CSStaticData.LIST_TYPE.LIST_NONE || m_type == CSStaticData.LIST_TYPE.LIST_INVALID)
			{
				if (!m_is_drawing)
					return;
				
				if (m_list == null || m_list.size() <= 0)
					return;
				
				ElementList list = m_list.get(0);
				list.onDraw(gl, view);
			}
			else if (m_type == CSStaticData.LIST_TYPE.LIST_DATE)
			{
				if (!m_is_drawing)
					return;
				//~!
				
				drawGroupData(gl, view);
			}
			else
			{
				// 位置分组
				
				//启用顶点设置功能，之后必须要关闭功能
				mSphere.onDraw(gl, m_list);
				/*
				if (m_is_drawing)
				{
					for (int i = 0; i < m_list.size(); i++)
					{
						ElementList list = m_list.get(i);
						
						if (list.getName().equalsIgnoreCase("Unknown"))
						{
							continue;
						}
						
						Element elem = list.get(i);
						if(elem != null){
							double longitude = elem.m_longitude;
							double latitude = elem.m_latitude;
							float[] pos = mSphere.GetPositionAndAngle(latitude, longitude);
							
							list.moveTo(pos[0], pos[1], pos[2]);
							list.setAngleTo(pos[3], pos[4], pos[5]);
//							list.moveTo(0, 0, -0.5f);
//							list.setAngleTo(0, 0, 0);
							
							list.onDraw(gl, view);
						}
					}	
				}
				*/
			}
		}
	}
	
	public void refreshPosition(int index)
	{
		//boolean bDraw = m_is_drawing;

		//m_is_drawing = false;
		Log.e("refreshPosition", String.format("m_index = %d", index));
		if(m_b_enter_group)
		{
			if(m_list == null){
				return;
			}
			
			if(m_select_group_index < m_list.size() && m_select_group_index >=0){
				ElementList list = m_list.get(m_select_group_index);		
				if (list.size() > 0)
				{
					// 文件夹里面还存在文件
					list.m_b_enter = true;
					list.refresh(index/CSStaticData.group_none_row_num);
				}
				else
				{
					// 空文件夹			
					// ~!
				}
			}
		}
		else
		{
			// 文件夹，包括CSStaticData.LIST_TYPE.LIST_NONE模式下的文件
			if (m_type == CSStaticData.LIST_TYPE.LIST_NONE)
			{
				if (m_list.size() > 0)
				{
					ElementList list = m_list.get(0);			
					list.refresh(index/CSStaticData.group_none_row_num);
				}
			}
			else if(m_type == CSStaticData.LIST_TYPE.LIST_DATE)
			{
				if (m_list != null)
				{
					// 刷新时间文件夹
					m_index = index;
					for  (int i = 0; i < m_list.size(); i++)
					{
						int posIndex = m_scurve.getPositionIndex(i-index);
						m_list.get(i).moveToIndex(posIndex);
					}
				}
			}
			else
			{
				// Location
				for  (int i = 0; i < m_list.size(); i++)
				{
					Element elem = m_list.get(i).get(0);
					if(elem != null){
						if(!Double.isNaN(elem.m_latitude) && !Double.isNaN(elem.m_longitude)){
							GPSPointInfo gpsPos = new GPSPointInfo(elem.m_latitude, elem.m_longitude, 1, new float[]{0, 0, 0});
							float gpsLoc[] = gpsPos.getGPSPoint();
							m_list.get(i).moveTo(gpsLoc[0], gpsLoc[1], gpsLoc[2]);
							m_list.get(i).setAngle(gpsPos.getXTheata(), gpsPos.getYTheata(), gpsPos.getZTheata());
						}
					}
					
				}
				
			}
		}
		
		if (WiGalleryInterface.m_onGLMoveListener != null)
		{
			int curIndex = getCurIndex();
			int totalIndex = getTotalIndex();
			
			WiGalleryInterface.m_onGLMoveListener.setScrollBar(curIndex, totalIndex);
		}
		
		//m_is_drawing = bDraw;
	}
	

	/**
	 * 刷新所有列表
	 */
	public void rebuildList()
	{
		m_is_drawing = false;
		// 全部刷新
		int orgListSize = 0;
		int childIndex = 0;
		String chooseFolderName = null;

		LinkedList<GroupDataInfo> grouo_data_info_list = new LinkedList<GroupDataInfo>();
		
		if (m_list != null && m_list.size() > 0)
		{
			orgListSize = m_list.size();
			if (m_type == CSStaticData.LIST_TYPE.LIST_NONE)
			{
				ElementList list = m_list.get(0);
				childIndex = list.GetCurIndex();	
			}
			else
			{
				for (int i = 0; i < orgListSize; i++)
				{
					ElementList list = m_list.get(i);
					if (i == m_select_group_index)
					{
						childIndex = list.GetCurIndex();
						chooseFolderName = list.getName();
					}
					
					GroupDataInfo info = new GroupDataInfo();
					info.pos = list.getPosition();
					info.posIndex = list.getIndex();

					grouo_data_info_list.add(info);
					
					list.clear();
				}
				
			}
			m_list.clear();
		}
		
		m_list = WiGalleryOpenGLRenderer.m_data_manager.GetListArray(m_type, m_element_type);
		
		// 链表排序
		setOrder(CSStaticData.g_sort_order_mode);
		
		if(m_list != null && m_list.size() != 0){
			if (orgListSize == m_list.size())
			{
				// 文件夹没有清空
				// 刷新文件夹内元素位置
				int index = 0;
				if(m_type == CSStaticData.LIST_TYPE.LIST_DATE)
					index = m_select_group_index;
				
				if (index >= 0 && index <= m_list.size()-1)
				{
					ElementList list = m_list.get(index);
					list.refresh(childIndex/CSStaticData.group_none_row_num);
				}
		        
				// 刷新文件夹位置
				if(m_type == CSStaticData.LIST_TYPE.LIST_DATE)
				{
					// 刷新时间文件夹
					for  (int i = 0; i < m_list.size(); i++)
					{
						GroupDataInfo info = grouo_data_info_list.get(i);
											
						m_list.get(i).moveTo(info.pos[0], info.pos[1], info.pos[2]);
						m_list.get(i).setIndex(info.posIndex);
					}
					
					grouo_data_info_list.clear();
				}
				
				

				//refreshPosition(childIndex);
			}
			else
			{
				// 刷新子文件
				if(m_type == CSStaticData.LIST_TYPE.LIST_DATE)
				{
					m_b_list_change = true;
					m_index = 0;

					// 刷新时间文件夹
					for  (int i = 0; i < m_list.size(); i++)
					{		
						int posIndex = m_scurve.getPositionIndex(i);
						m_list.get(i).moveToIndex(posIndex);
						
						if(chooseFolderName != null && chooseFolderName.compareToIgnoreCase(m_list.get(i).getName()) == 0)
						{
							m_b_enter_group = true;
							m_select_group_index = i;
							refreshPosition(childIndex);
						}						
					}
					
				}
				else
				{
					m_b_enter_group = false;
					m_select_group_index = -1;
					refreshPosition(0);
				}
			}
		}
		
		
		int curIndex = getCurIndex();
		int totalIndex = getTotalIndex();
		
		WiGalleryInterface.m_onGLMoveListener.setScrollBar(curIndex, totalIndex);
		m_is_drawing = true;
		
		//刷新未知个数和喜好个数
		if(WiGalleryActivity.mUIHandler != null){
			WiGalleryActivity.mUIHandler.sendEmptyMessage(WiGalleryActivity.HANDLE_REFLASH_FAVORITE_COUNTER);
			WiGalleryActivity.mUIHandler.sendEmptyMessage(WiGalleryActivity.HANDLE_REFLASH_UNKNOWN_COUNTER);
		}
	}
	
	
	public boolean onTouchEvent(float x, float y)
	{	
		
		if(m_list == null || m_list.size() == 0){
			return false;
		}
		
        if (m_b_enter_group)
        {
        	if (m_list == null || m_list.size() <= m_select_group_index)
        	{
        		return true;
        	}
        	else {
            	ElementList list = m_list.get(m_select_group_index);
            	return list.onTouchEvent(x, y);
			}

        }
        else if (m_type == CSStaticData.LIST_TYPE.LIST_NONE)
        {
        	ElementList list = m_list.get(0);
        	return list.onTouchEvent(x, y);
        }
        else
        {
        	if (m_animation_type != CSStaticData.ANIMATION_TYPE.NONE)
        	{
        		return true;
        	}
        	//~
        	// 文件夹模式
        	for (int i = 0; i < m_list.size(); i++)
        	{
        		ElementList list = m_list.get(i);
        		if (list.onTouchEvent(x, y))
        		{
	    			if (list.getStatus() == ELEM_STATUS.NORMAL_STATUS)
	    			{
	    				// 打开文件夹
	    				openGroup(i);
	    			}
	    			else
	    			{
	    				// 选择模式
	    				list.setChoosed(!list.isChoosed());
	    				if(WiGalleryInterface.m_onGlItemListener != null){
	    					WiGalleryInterface.m_onGlItemListener.onGroupClick(list.getName());
	    				}
	    			}

        			return true;
        		}
        	}
        }
        //else
        //{
            	//if(mSphere != null)
            	//	return mSphere.onTouchEvent(x, y);
        //}
		return false;
	}
	
	/**
	 * 传入球体的触摸事件
	 * @param event
	 * @return
	 */
	public boolean onSphereTouchEvent(MotionEvent event){
		boolean state = false;
		
		if(mSphere != null){
			state = mSphere.onTouchEvent(event);
		}
		
		return state;
	}
	
	/**
	 * 传入球体的触摸事件
	 * @param xDistance
	 * @param yDistance
	 * @return
	 */
	public boolean onSphereTouchEvent(float xDistance, float yDistance){
		boolean state = false;
		
		if(mSphere != null){
			state = mSphere.onTouchEvent(xDistance, yDistance);
		}
		
		return state;
	}
	
	public boolean onSphereFlingEvent(float velocityX, float velocityY) {
		boolean state = false;
		
		if(mSphere != null){
			state = mSphere.onFlingEvent(velocityX, velocityY);
		}
		
		return state;
	}
	
	public void isSphereTouching(boolean touching){
		if(mSphere != null){
			mSphere.isTouching(touching);
		}
	}
	
	public boolean onScale(float scale){
		boolean state = false;
		
		if(mSphere != null){
			mSphere.onScale(scale);
			state = true;
		}else{
			state = false;
		}
		
		return state;
	}
	
	public boolean isMoving()
	{
		if(m_list == null || m_list.size() == 0){
			return false;
		}
		
		if(m_type == CSStaticData.LIST_TYPE.LIST_NONE)
		{
        	ElementList list = m_list.get(0);
        	return list.isMoving();
		}
		else if ( m_b_enter_group)
		{
			if (m_select_group_index < 0 || m_select_group_index > m_list.size()-1)
				return false;
			
        	ElementList list = m_list.get(m_select_group_index);
        	return list.isMoving();
		}
		else
		{
			if (m_animation_type ==  CSStaticData.ANIMATION_TYPE.DATE_OPENING)
			{
				return true;
			}
			else if (m_animation_type == CSStaticData.ANIMATION_TYPE.DATE_CLOSING)
			{
				// 判断是否为进入动画
				return true;
			}
			else if (m_animation_type == CSStaticData.ANIMATION_TYPE.DATE_AFTERCLICK)
			{
				return true;
			}
			else if (m_animation_type == CSStaticData.ANIMATION_TYPE.DATE_AFTERCLICK_INVERISON)
			{
				return true;
			}
			
			for(int i = 0; i < size(); i++)
			{	
				ElementList list = m_list.get(i);
				if (list != null)
				{
					if (list.isMoving())
						return true;
				}
			}
		}
		return false;
	}
	
	public void startAnimation(CSStaticData.ANIMATION_TYPE type)
	{
		if (m_list == null)
			return;
		
		if (m_list.size() <= 0)
			return;
		
        if (m_b_enter_group)
        {
        	ElementList list = m_list.get(m_select_group_index);
        	list.startAnimation(type);
        }
        else if (m_type == CSStaticData.LIST_TYPE.LIST_NONE)
        {
        	ElementList list = m_list.get(0);
        	list.startAnimation(type);
        }
        else if (m_type == CSStaticData.LIST_TYPE.LIST_DATE)
        {    		
        	for (int i = 0; i < m_list.size(); i++)
        	{
        		ElementList list = m_list.get(i);
        		if (list != null)
        		{
        			if (list.isMoving())
        			{
        				return;
        			}
        		}
        	}
        	
        	
			if (type == CSStaticData.ANIMATION_TYPE.LEFT_SHIFT)
			{
				if (m_index+WiGalleryOpenGLRenderer.m_element_group.m_scurve.m_mov_num > m_list.size())
					return;
					
	        	for (int i = 0; i < m_list.size(); i++)
	        	{
	        		ElementList elist = m_list.get(i);
	        		if (elist != null)
	        		{
	        			elist.startAnimation(type);
	        		}
	        	}
	        	
				m_index += WiGalleryOpenGLRenderer.m_element_group.m_scurve.m_mov_num;
				
				if(CSStaticData.DEBUG)
					Log.e("ElementGroup", String.format("----------------- m_index = %d --------------", m_index));
			}
			else if (type == CSStaticData.ANIMATION_TYPE.RIGHT_SHIFT)
			{
				if (m_index <= 0)
					return;
				
	        	for (int i = 0; i < m_list.size(); i++)
	        	{
	        		ElementList elist = m_list.get(i);
	        		if (elist != null)
	        		{
	        			elist.startAnimation(type);
	        		}
	        	}
	        	
				m_index -= WiGalleryOpenGLRenderer.m_element_group.m_scurve.m_mov_num;
				
				if(CSStaticData.DEBUG)
					Log.e("ElementGroup", String.format("----------------- m_index = %d --------------", m_index));
			}
			else if (type == CSStaticData.ANIMATION_TYPE.DATE_OPENING)
			{
				// Date分组时的进入动画
				
				for  (int i = 0; i < m_list.size(); i++)
				{
					// 初始化位置
					int posIndex = m_scurve.getPositionIndex(11+i*2);
					m_list.get(i).moveToIndex(posIndex);
				}
				
				
				m_dategroup_animation_frame_num = 0;
				m_animation_type = CSStaticData.ANIMATION_TYPE.DATE_OPENING;
			}
			else if (type == CSStaticData.ANIMATION_TYPE.DATE_CLOSING)
			{
				// Date分组时的退出动画
				m_dategroup_animation_frame_num = 0;
				m_animation_type = CSStaticData.ANIMATION_TYPE.DATE_CLOSING;
			}
			else if (type == CSStaticData.ANIMATION_TYPE.DATE_AFTERCLICK)
			{
				m_dategroup_animation_frame_num = 0;
				m_animation_type = CSStaticData.ANIMATION_TYPE.DATE_AFTERCLICK;
			}
			else if (type == CSStaticData.ANIMATION_TYPE.DATE_AFTERCLICK_INVERISON)
			{
				m_dategroup_animation_frame_num = 0;
				m_animation_type = CSStaticData.ANIMATION_TYPE.DATE_AFTERCLICK_INVERISON;
			}
			else
			{
				
			}
			
				if(CSStaticData.DEBUG)
					Log.d("ElementGroup", String.format("m_index = %d", m_index));
        }
        else
        {
        	
        }
	}
	
	/**
	 * 保存状态时调用
	 * @return
	 */
	public int  getIndex()
	{
		if (m_type == CSStaticData.LIST_TYPE.LIST_NONE || m_b_enter_group)
		{
			ElementList list = getCurrentDisplayedList();
			if (list == null)
				return 0;
			else
				return list.GetCurIndex();
		}
		else 
		{
			// 直接返回文件夹个数
			return m_index;
		}	
	}
	
	/**
	 * 恢复状态时调用
	 * @return
	 */
	public void setIndex(int index)
	{
		if (m_type == CSStaticData.LIST_TYPE.LIST_NONE || m_b_enter_group)
		{
			ElementList list = getCurrentDisplayedList();
			if (list != null)
				list.SetCurIndex(m_index);
		}
		else 
		{
			// 直接返回文件夹个数
			m_index = index;
		}		
	}
	
	/*
	 * Quick seek bar 使用，
	 */
	public int getCurIndex()
	{
		if (m_type == CSStaticData.LIST_TYPE.LIST_NONE || m_b_enter_group)
		{
			ElementList list = getCurrentDisplayedList();
			if (list == null)
				return 0;
			else
			{
				int index = list.GetCurIndex();
				if (index < 0) index = 0;
				
				return index / CSStaticData.group_none_row_num;
			}
		}
		else 
		{
			// 直接返回文件夹个数
			int index = m_index;
			if (index < 0) index = 0;
			return index;
		}
	}
	
	/*
	 * Quick seek bar 使用，
	 */
	public int getTotalIndex()
	{

		if (m_type == CSStaticData.LIST_TYPE.LIST_NONE || m_b_enter_group)
		{
			ElementList list = getCurrentDisplayedList();
			if (list != null)
			{
				int tRow = 3; //每屏显示的列数
				if (WiGalleryOpenGLRenderer.m_b_horizontal_screen)
					tRow = 4;
				
				int inde = getCurrentDisplayedList().size() / CSStaticData.group_none_row_num - tRow;
				if (inde < 0)
					inde = 0;
				return inde;
			}
			else
				return 0;
		}
		else 
		{
			// 直接返回文件夹个数
			if (m_list == null)
				return 0;
			
			return m_list.size() - 1;
		}
	}
	
	public void shiftTo(int index)
	{
		if (isMoving())
		{
			return;
		}
		
		
		if (m_type == CSStaticData.LIST_TYPE.LIST_NONE)
		{
			index = index * CSStaticData.group_none_row_num;
		}
		else
		{
			if (m_b_enter_group)
			{
				index = index * CSStaticData.group_none_row_num;
			}
			else
			{
				// 不做处理
			}
		}
		
		refreshPosition(index);

	}
	
	/**
	 * 轻移或者拖动seek bar
	 * @param index: 显示文件编号
	 */
	public void shiftTo(int index, boolean bAlign)
	{
		if (isMoving())
		{
			return;
		}
	
		
		if (m_type == CSStaticData.LIST_TYPE.LIST_NONE)
		{
        	ElementList list = m_list.get(0);
        	list.shiftTo(index, false, bAlign);
		}
		else
		{
			if (m_b_enter_group)
			{
	        	ElementList list = m_list.get(m_select_group_index);
	        	list.shiftTo(index, false, bAlign);
			}
			else
			{
				for (int i = 0; i < m_list.size(); i++)
				{
	        		ElementList elist = m_list.get(i);
	        		if (elist != null)
	        		{
	        			elist.shiftTo(m_index - index, false, bAlign);
	        		}
				}
				
				m_index = index;
				
				if(CSStaticData.DEBUG)
					Log.e("ElementGroup", String.format("----------------- m_index = %d --------------", m_index));
			}
		}

	}
	
	public void calcPosition()
	{
		if (m_list == null || m_list.size() <= 0 || m_is_drawing == false)
			return;
		
		if (m_b_enter_group)
		{			
			if (m_b_flip)
				return;
			
			if (m_list.size() <= 0 || m_select_group_index < 0 || m_select_group_index > m_list.size()-1)
				return;
			
			ElementList list = m_list.get(m_select_group_index);
			list.m_b_enter = true;
			list.calcPosition();
		}
		else
		{
			if (m_type == CSStaticData.LIST_TYPE.LIST_NONE)
			{	
				if (m_b_flip)
					return;
				
				if (m_list.size() <= 0)
					return;
				
				ElementList list = m_list.get(0);
				list.calcPosition();
			}
			else if (m_type == CSStaticData.LIST_TYPE.LIST_DATE)
			{
				//~!
				// 动画效果时，进行动画处理的元素个数
				int draw_num = 12;
				int start1 = m_index+draw_num > size()-1 ? size()-1 : m_index+draw_num;
				int end1 = m_index < 0 ? 0 : m_index;
				
				// 函数中的11标示当前正常显示的文件夹个数
				if (m_list.size() <= 0)
					return;
				
				//~!
				
				// 绘制元素
				if(m_animation_type == CSStaticData.ANIMATION_TYPE.DATE_OPENING)
				{
					// 进入Date模式
					int nnnn = SCurve.m_step/SCurve.m_mov_num;
					int cnt = 11*nnnn;
					
					for (int i = start1; i >= end1; i--)
					{
						ElementList list = m_list.get(i);
						
						if (m_dategroup_animation_frame_num < cnt)
						{
							list.moveToIndexTo(-SCurve.m_mov_num);
						}
						else
						{
							int n = m_dategroup_animation_frame_num - cnt;
							if (n/nnnn < i)
							{
								list.moveToIndexTo(-SCurve.m_mov_num);
							}
						}

						//list.calcPosition();
					}
					
					if (m_dategroup_animation_frame_num == cnt + draw_num * nnnn)
					{
						m_dategroup_animation_frame_num = -1;
						m_animation_type = CSStaticData.ANIMATION_TYPE.NONE;
					}
					
					m_dategroup_animation_frame_num++;
				}
				else if(m_animation_type == CSStaticData.ANIMATION_TYPE.DATE_CLOSING)
				{
					//退出Date模式
					int nnnn = SCurve.m_step/SCurve.m_mov_num;
					for (int i = start1; i >= end1; i--)
					{
						ElementList list = m_list.get(i);
						
						int index = Math.abs(i-start1);
						if (m_dategroup_animation_frame_num > index * nnnn)
						{
							list.moveToIndexTo(SCurve.m_mov_num);
						}

						//list.calcPosition();
					}
					
					if (m_dategroup_animation_frame_num == (draw_num + 11) * nnnn)
					{
						//~!
						m_dategroup_animation_frame_num = -1;
						m_animation_type = CSStaticData.ANIMATION_TYPE.NONE;
					}
					
					m_dategroup_animation_frame_num++;
				}
				else if (m_animation_type == CSStaticData.ANIMATION_TYPE.DATE_AFTERCLICK)
				{	
					int nnnn = SCurve.m_step/SCurve.m_mov_num;
					for (int i = start1; i >= end1; i--)
					{
						ElementList list = m_list.get(i);
						
						if (i == m_select_group_index)
						{
							
							if (m_dategroup_animation_frame_num < SCurve.m_mov_num)
							{
								list.move(0, 0.1f, 0);
							}
							else if (m_dategroup_animation_frame_num == SCurve.m_mov_num)
							{				
								float[] pos = list.getPosition();
								/*
								float x = CSStaticData.eye_position_date[0] - pos[0];
								float y = 0;
								float z = 0 - pos[2];
								
								pos_offset[0] = x / 10;
								pos_offset[1] = y / 10;
								pos_offset[2] = 0.05f;
								*/
								
								
								float x = CSStaticData.eye_position_date[0] - pos[0];
								float y = CSStaticData.eye_position_date[1] - pos[1];
								float z = CSStaticData.eye_position_date[2] - pos[2];
								
								// 绕视点偏移
								if (x > 0)
								{
									x += 0.2f;
								}
								else
								{
									x -= 0.2f;
								}
								
								pos_offset[0] = x / 10.0f;
								pos_offset[1] = y / 10.0f;
								pos_offset[2] = z / 10.0f;
								
								list.move(pos_offset[0], pos_offset[1], pos_offset[2]);
							}
							else
							{					
								list.move(pos_offset[0], pos_offset[1], pos_offset[2]);
							}
						}
						else
						{
							if (m_dategroup_animation_frame_num >= SCurve.m_mov_num)
							{
								list.moveToIndexTo(SCurve.m_mov_num);
							}
						}
						

						//list.calcPosition();
					}

					if (m_dategroup_animation_frame_num == draw_num * nnnn+SCurve.m_mov_num)
					{
						m_dategroup_animation_frame_num = -1;
						m_animation_type = CSStaticData.ANIMATION_TYPE.NONE;
					}
					
					m_dategroup_animation_frame_num++;
				}
				else if (m_animation_type == CSStaticData.ANIMATION_TYPE.DATE_AFTERCLICK_INVERISON)
				{					
					int nnnn = SCurve.m_step/SCurve.m_mov_num;
					for (int i = start1; i >= end1; i--)
					{
						ElementList list = m_list.get(i);
						
						if (i == m_select_group_index)
						{

							if (m_dategroup_animation_frame_num > draw_num * nnnn+3 - SCurve.m_mov_num)
							{
								list.move(0, -0.1f, 0);
							}
							else if (m_dategroup_animation_frame_num <= draw_num * nnnn+3 - SCurve.m_mov_num)
							{
								//list.move(0, 0, -0.05f);
								list.move(-pos_offset[0], -pos_offset[1], -pos_offset[2]);
							}
						}
						else
						{
							if (m_dategroup_animation_frame_num <= draw_num * nnnn+3 - 3)
							{
								list.moveToIndexTo(-SCurve.m_mov_num);
							}
						}

						//list.calcPosition();
					}
					
					if (m_dategroup_animation_frame_num == draw_num * nnnn+3)
					{
						m_dategroup_animation_frame_num = -1;
						m_animation_type = CSStaticData.ANIMATION_TYPE.NONE;
					}
					
					m_dategroup_animation_frame_num++;
				}
				else
				{
					
					for (int i = m_list.size()-1; i >= 0; i--)
					{
						ElementList list = m_list.get(i);
						list.calcPosition();
					}
					
				}

			}
			else
			{
				// 位置分组
			}
		}
	
	}
	
	private void drawGroupData(GL10 gl, MEDIA_VIEW view)
	{
		// 动画效果时，进行动画处理的元素个数
		int draw_num = 12;
		int start1 = m_index+draw_num*2 > size()-1 ? size()-1 : m_index+draw_num*2;
		int end1 = m_index-draw_num < 0 ? 0 : m_index-draw_num;
		
		// 函数中的11标示当前正常显示的文件夹个数
		if (m_list == null)
			return;
		
		if (m_list.size() <= 0 || !m_is_drawing)
			return;
		
		//~!
		// 时间分组
		gl.glPushMatrix();
		
    	// 绘制曲线
		m_scurve.onDraw(gl);
		
		// 绘制元素

		{//NeoYeoh
//			int size = m_list.size();
//			for (int i = start1; i >= end1; i--)
//			{
//				if (i < 0 || i > size-1)
//					continue;
//				ElementList list = m_list.get(i);
//				list.onDraw(gl, view);
//			}
		}
		{//Cocoonshu ： 规避在循环中主链表被修改时产生的越界问题
			for (int i = start1; i >= end1; i--)
			{
				if (i < 0 || i > m_list.size() - 1) 
					continue;
				ElementList list = m_list.get(i);
				list.onDraw(gl, view);
			}
		}

		
		gl.glPopMatrix();
	}
	
	/**
	 * 当前元素重新排序
	 * @param bAsc - true： 升序排序 
	 * 				 false： 降序排序
	 */
	public void setOrder(boolean bAsc)
	{
		if (m_list == null || m_list.size()<=0) return;
		
		CSStaticData.g_sort_order_mode = bAsc;
		if (m_type == CSStaticData.LIST_TYPE.LIST_NONE)
		{
			ElementList elist = m_list.get(0);
			
			List<Element> list = elist.getList();
			
			if (bAsc)
			{
				list = WiGalleryOpenGLRenderer.m_data_manager.sortByAsc(list);
			}
			else
			{
				list = WiGalleryOpenGLRenderer.m_data_manager.sortByDesc(list);
			}
			
			;
			elist.setList(list);
			
			refreshPosition(elist.GetCurIndex());
		}
		else
		{
			if (m_b_enter_group)
			{
				if (m_list == null || m_select_group_index < 0 || m_select_group_index > m_list.size()-1)
					return;
				
				ElementList elist = m_list.get(m_select_group_index);
				List<Element> list = elist.getList();
				
				if (bAsc)
				{
					list = WiGalleryOpenGLRenderer.m_data_manager.sortByAsc(list);
				}
				else
				{
					list = WiGalleryOpenGLRenderer.m_data_manager.sortByDesc(list);
				}
				
				elist.setList(list);
				
				refreshPosition(elist.GetCurIndex());
			}
		}
	}
	
	/**
	 * 获取喜好设置文件个数
	 * @return
	 */
	public int getFavoriteNum()
	{
		int num = 0;
		if (m_list == null)
			return 0;
		for (int i = 0; i < m_list.size(); i++)
		{
    		ElementList elist = m_list.get(i);
    		if (elist != null)
    		{
    			num += elist.getFavoriteNum();
    		}
		}
		
		return num;
	}
	
	/**
	 * 获取没有地理位置信息的图片数量
	 * @return
	 */
	public int getUnknownNum()
	{
		int num = 0;
		
		for (int i = 0; i < m_list.size();i++)
		{
			ElementList list = m_list.get(i);
			if (list.getName().equalsIgnoreCase("unknown"))
			{
				num = list.getSize();
				return num;
			}
		}
		return num;
	}
	
	public int getUnknownListIndex()
	{
		if (m_list == null)
			return 0;
		
		for (int i = 0; i < m_list.size();i++)
		{
			ElementList list = m_list.get(i);
			if (list.getName().equalsIgnoreCase("unknown"))
			{
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * 轻移时使用
	 * @param xOffset
	 */
	public void moving(float xOffset)
	{
		if(m_list == null || m_list.size() == 0){
			return;
		}
		
    	if (isMoving())
    	{
    		return;
    	}
    	
    	float xoffset_3d = 0;
    	float zoffset_3d = 0;
    	
    	if (WiGalleryOpenGLRenderer.m_b_horizontal_screen)
    	{
    		xoffset_3d = xOffset * CSStaticData.screen_3d_width_h / CSStaticData.g_screen_width;
    		zoffset_3d = xoffset_3d * CSStaticData.z_list_pos_offset / CSStaticData.x_list_pos_offset;
    	}
    	else
    	{
    		xoffset_3d = xOffset * CSStaticData.screen_3d_width_v / CSStaticData.g_screen_width;
    		zoffset_3d = xoffset_3d * CSStaticData.z_list_pos_offset / CSStaticData.x_list_pos_offset;
    	}
    	
		if (m_type == CSStaticData.LIST_TYPE.LIST_NONE)
		{
			if(m_list != null){
				ElementList list = m_list.get(0);

				if(list != null){
					if (list.GetCurIndex() <= 0 && xOffset >= 0)
					{
						// 已是第一个元素，如果继续右移，则不处理
						return;
					}

					for (int i = 0; i < list.getSize(); i++)
					{
						Element elem = list.get(i);
						if (elem != null)
							elem.move(xoffset_3d, 0, zoffset_3d);
					}
				}
			}
		}
		else
		{
			if (m_b_enter_group)
			{
				ElementList list = m_list.get(m_select_group_index);
				
				if (list.GetCurIndex() <= 0 && xOffset >= 0)
				{
					// 已是第一个元素，如果继续右移，则不处理
					return;
				}
				
				for (int i = 0; i < list.getSize(); i++)
				{
					Element elem = list.get(i);
					elem.move(xoffset_3d, 0, zoffset_3d);
				}
			}
			else
			{
				if (m_type == CSStaticData.LIST_TYPE.LIST_DATE)
				{
					int xoffset_t = (int)(xOffset * 35*4 / CSStaticData.g_screen_width);
					for (int i = 0; i < size(); i++)
					{
						ElementList list = this.get(i);
						list.moveToIndexTo(xoffset_t);
					}
				}
			}
		}

	}
	
	public boolean onDown(MotionEvent event) 
	{
		return true;
	}

	/**
	 * 轻移完成后调用
	 * @param xOffset: 从down到up的位移
	 */
	public void  moveFinished(float xOffset)
	{
		if(m_list == null || m_list.size() == 0){
			return;
		}
		
		float xoffset_3d = 0;
    	if (WiGalleryOpenGLRenderer.m_b_horizontal_screen)
    	{
    		xoffset_3d = xOffset  * CSStaticData.screen_3d_width_h / CSStaticData.g_screen_width;
    	}
    	else
    	{
    		xoffset_3d = xOffset  * CSStaticData.screen_3d_width_v / CSStaticData.g_screen_width;
    	}
    	
		if (m_type == CSStaticData.LIST_TYPE.LIST_NONE)
		{
			ElementList list = m_list.get(0);
			int offset_index = (int)(xoffset_3d/CSStaticData.x_list_pos_offset);
			shiftTo(list.GetCurIndex() / CSStaticData.group_none_row_num - offset_index, true);
		}
		else
		{
			if (m_b_enter_group)
			{
				ElementList list = m_list.get(m_select_group_index);
				int offset_index = (int)(xoffset_3d/CSStaticData.x_list_pos_offset);
				shiftTo(list.GetCurIndex() / CSStaticData.group_none_row_num - offset_index, true);
			}
			else
			{
				if (m_type == CSStaticData.LIST_TYPE.LIST_DATE)
				{
					int xoffset_t = (int)(xOffset * 35*4 / CSStaticData.g_screen_width);
					m_index -= xoffset_t / 35;
					
					if (m_index < 0) m_index = 0;
					else if (m_index >= m_list.size()-1) m_index = m_list.size() - 1;
					
					for (int i = 0; i < size(); i++)
					{		
						int posIndex = m_scurve.getPositionIndex(i-m_index);
						m_list.get(i).moveToIndex(posIndex);
					}
				}
			}
		}
		
		int curIndex = getCurIndex();
		int totalIndex = getTotalIndex();
		
		WiGalleryInterface.m_onGLMoveListener.setScrollBar(curIndex, totalIndex);

	}

	
}

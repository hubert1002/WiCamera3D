// Copyright (c) 2011 Wistron SWPC
// All rights reserved.
//	created:	2011/09/28
//	filename: 	IconList.java
//	author:		Neo Yeoh
//	purpose:	
 
package com.wistron.WiGallery;


import Utilities.CSStaticData;
import Utilities.FileOperation;
import android.graphics.Bitmap;
import android.os.RemoteCallbackList;
import android.util.Log;
import android.view.MotionEvent;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import javax.microedition.khronos.opengles.GL10;

import com.wistron.WiGallery.WiGalleryOpenGLRenderer;

public class ElementList extends ElementBase
{
    private static final float[] m_image_coords =
    {       
		0,1, 1,1,
 		0,0, 1,0,
    };
    
    // 文件夹
    protected int m_pos_index = 0;
    protected int m_offset_index = 0; //文件夹位移数
    
    private float[] m_nongroup_animation_step = {0,0,0}; // 开启/关闭 None Group模式动画步长
    private int m_nongroup_animation_frame_num = 0;
    
	private static final Byte [] m_line_index ={0,1,3,2};
	
	private static final float[] m_image_vers = new float[12];
	
    private FloatBuffer m_tex_buff = ResourceManager.makeFloatBuffer(m_image_coords);	
    private FloatBuffer m_cover_buff = null;
    private ByteBuffer m_line_index_buff = ResourceManager.makeByteBuffer(m_line_index);
    
	private List<Element> m_list = null;
	
	private String m_name = null;
	private int    m_nameTexture = CSStaticData.INVALID_TEXTURE_ID;
	private int    m_elementNumTexture = CSStaticData.INVALID_TEXTURE_ID;
	private int m_elementNum = 0;
	protected int m_animation_index = 0;
	private CSStaticData.LIST_TYPE m_type = CSStaticData.LIST_TYPE.LIST_NONE;

		
	private int m_index = 0;
	private int m_sort_order = -1;
	
	private float m_angle = 30;
	// 当前最大加载纹理的文件个数
	private int m_max_texture_num = 100;
	
	private static String m_open_file_name = null;
	
	
	public boolean m_b_enter = false;
	
	public ElementList(CSStaticData.LIST_TYPE type)
	{		
		m_type = type;
		if (m_list != null)
			m_list.clear();
		else
			m_list = new LinkedList<Element>();
	}
	
	public ElementList(List<Element> list, CSStaticData.LIST_TYPE type)
	{
		m_type = type;
		
		if (m_list != null)
			m_list.clear();
		else
			m_list = new LinkedList<Element>();	
		
		if (list != null)
		{			
			int num = list.size();
			for (int i = 0; i < num; i++)
			{
				m_list.add(list.get(i));
			}
		}
	}
	
	public void setEntering(boolean val)
	{
		// 打开nongroup动画
		if (val)
		{
			if (!m_b_enter)
			{
				m_b_enter = true;
				m_index = 0;
				startAnimation(CSStaticData.ANIMATION_TYPE.NONEGROUP_OPENING);
			}
		}
		else
		{
			if(m_b_enter)
			{
				startAnimation(CSStaticData.ANIMATION_TYPE.NONEGROUP_CLOSING);
			}
		}
	}
	
	
	public boolean isEntering()
	{
		return m_b_enter;
	}
	
	public void setName(String name)
	{
		m_name = name;
	}
	
	public String getName()
	{
		return m_name;
	}
	
	public CSStaticData.LIST_TYPE getType()
	{
		return m_type;
	}
	
	public int getSize()
	{
		return m_list.size();
	}
		
	Element get(int i)
	{
		if (i < 0 || i >= m_list.size())
		{
			return null;
		}
		else
		{
			return m_list.get(i);
		}
	}
	
	/**
	 * 刷新列表,  参数为文件列数
	 */
	public void refresh(int firstElement)
	{		
		int totalCol = size() / CSStaticData.group_none_row_num;
		if (firstElement > totalCol) firstElement = totalCol;
		else if (firstElement < 0) firstElement = 0;
		
		if (m_type == CSStaticData.LIST_TYPE.LIST_NONE || m_b_enter == true)
		{		
			m_index = firstElement * CSStaticData.group_none_row_num;
			
			refreshPosition_None();
		}
		else
		{
			m_index = firstElement;
		}
	}
	
	private void refreshPosition_None()
	{
		int n = m_list.size();
		if (WiGalleryOpenGLRenderer.m_b_horizontal_screen)
		{
			// 横屏			
			m_angle = 15.0f;
		}
		else
		{
			// 竖屏			
			m_angle = 30.0f;
		}
		
		int curRow = m_index / CSStaticData.group_none_row_num;
		for (int i = 0; i < n; i++)
		{
			Element elem = m_list.get(i);
			if (elem != null)
			{
				int row = i % CSStaticData.group_none_row_num; // 行
				int col = i / CSStaticData.group_none_row_num - curRow;  // 列
				
				float x = (float) (CSStaticData.x_list_pos_start + CSStaticData.x_list_pos_offset * col);
				float y = (float) (CSStaticData.y_list_pos_start + CSStaticData.y_list_pos_offset * row);
				float z = (float) (CSStaticData.z_list_pos_start + CSStaticData.z_list_pos_offset * col);
				
				elem.moveTo(x, y, z);
				elem.setAngleTo(0, m_angle, 0);
			}
		}
	}
	
	/**
	 * 删除选中的元素以及对应的纹理
	 */
	public void deleteSeleted()
	{
        Iterator<Element> it = m_list.iterator();   
        while (it.hasNext())    
        {   
        	Element elem = it.next();
        	if (elem != null && elem.isChoosed())
        	{
        		elem.delete();;
        		it.remove();
        	}
        } 
        
        if (m_list.size() == 0)
        {
    		WiGalleryOpenGLRenderer.m_resource_manager.AddNeedDeleteTextureID(m_elementNumTexture);
    		m_elementNumTexture = CSStaticData.INVALID_TEXTURE_ID;
    		WiGalleryOpenGLRenderer.m_resource_manager.AddNeedDeleteTextureID(m_nameTexture);
    		m_nameTexture = CSStaticData.INVALID_TEXTURE_ID;
        }
        else
        {
        	refresh(0);
        }
	}
	
	/**
	 * 情况列表，不删除数据
	 */
	public void clear()
	{
		m_list.clear();
	}
	/**
	 * 删除所有元素以及对应的纹理
	 */
	public void deleteAll()
	{
        Iterator<Element> it = m_list.iterator();   
        while (it.hasNext())    
        {   
        	Element elem = it.next();
        	if (elem != null)
        	{
        		elem.delete();;
        		it.remove();
        	}
        } 
		
		// 需要删除纹理
		WiGalleryOpenGLRenderer.m_resource_manager.AddNeedDeleteTextureID(m_elementNumTexture);
		m_elementNumTexture = CSStaticData.INVALID_TEXTURE_ID;
		WiGalleryOpenGLRenderer.m_resource_manager.AddNeedDeleteTextureID(m_nameTexture);
		m_nameTexture = CSStaticData.INVALID_TEXTURE_ID;
	}
	
	public List<Element> getList()
	{
		return m_list;
	}
	
	public void setList(List<Element> list)
	{
		if (list != null)
		{
			m_list.clear();
			
			for (int i = 0; i < list.size(); i++)
			{
				m_list.add(list.get(i));
			}
		}
	}

	public void SetCurIndex(int index)
	{
		index = index / CSStaticData.group_none_row_num;
		index *= CSStaticData.group_none_row_num;
		m_index = index;
	}
	
	public int GetCurIndex()
	{
		return m_index;
	}
	
	public void add(Element icon)
	{
		synchronized(m_list)
		{
			m_list.add(icon);			
		}
	}
	
	// 
	/**
	 * 元素个数
	 * @return
	 */
	public int size()
	{
		return m_list.size();
	}
	
	// 
	/**
	 * 获取指定元素
	 */
	public Element getIndex(int i)
	{
		synchronized(m_list)
		{
			if(i < 0 || i >= size())
				return null;
			
			return m_list.get(i);
		}
		
	}
	
	public Element getElement(String name) {
		synchronized (m_list) {
			int size = size();
			for (int i = 0; i < size; i++) {
				Element elem = m_list.get(i);
				if (name.compareToIgnoreCase(elem.getName()) == 0) {
					return elem;
				}
			}
		}
		return null;
	}
	
	
	// 
	/**
	 * 设置选择状态
	 * @param status
	 */
	public void setStatus(ELEM_STATUS status)
	{		
		if (m_type == CSStaticData.LIST_TYPE.LIST_NONE || m_b_enter)
		{
		    Iterator<Element> it = m_list.iterator();   
	        while (it.hasNext())    
	        {   
	        	Element elem = it.next();
	        	
	        	CSStaticData.MEDIA_TYPE type = elem.getFileType();
	        	if (WiGalleryOpenGLRenderer.getMediaMetaType() == CSStaticData.MEDIA_META_TYPE.ALL_MEDIA_TYPE
	        			|| WiGalleryOpenGLRenderer.getMediaMetaType() == CSStaticData.MEDIA_META_TYPE.NO_FILTER)
	        	{
	        		elem.setStatus(status);
	        	}
	        	else if (WiGalleryOpenGLRenderer.getMediaMetaType() == CSStaticData.MEDIA_META_TYPE.IMAGE_MEDIA_TYPE)
	        	{
	        		// 图片
	        		if (type == CSStaticData.MEDIA_TYPE.NORMAL_IMAGE || type == CSStaticData.MEDIA_TYPE.STOERE_IMAGE)
	        		{
	        			elem.setStatus(status);
	        		}
	        		else
	        		{
	        			elem.setStatus(ELEM_STATUS.DISABLE_STATUS);
	        		}
	        	}
	        	else  if (WiGalleryOpenGLRenderer.getMediaMetaType() == CSStaticData.MEDIA_META_TYPE.VIDEO_MEDIA_TYPE)
	        	{
	        		// 视频
	        		if (type == CSStaticData.MEDIA_TYPE.NORMAL_VIDEO || type == CSStaticData.MEDIA_TYPE.STOERE_VIDEO)
	        		{
	        			elem.setStatus(status);
	        		}
	        		else
	        		{
	        			elem.setStatus(ELEM_STATUS.DISABLE_STATUS);
	        		}
	        	}
	        	else if (WiGalleryOpenGLRenderer.getMediaMetaType() == CSStaticData.MEDIA_META_TYPE.SET_FAVORITE_TYPE)
	        	{
	        		// 设置喜好
	        		if (!elem.bFavorite())
	        		{
	        			elem.setStatus(status);
	        		}
	        		else
	        		{
	        			elem.setStatus(ELEM_STATUS.DISABLE_STATUS);
	        		}
	        	}
	        	else if (WiGalleryOpenGLRenderer.getMediaMetaType() == CSStaticData.MEDIA_META_TYPE.REMOVE_FAVORITE_TYPE)
	        	{
	        		// 取消喜好
	        		if (elem.bFavorite())
	        		{
	        			elem.setStatus(status);
	        		}
	        		else
	        		{
	        			elem.setStatus(ELEM_STATUS.DISABLE_STATUS);
	        		}
	        	}
	        	else
	        	{
	        		// 不做处理
	        	}
	        } 
		}
		else
		{
			m_status = status;
			
			if (status == ELEM_STATUS.NORMAL_STATUS)
			{
				// 如果设置为正常模式,不选择
				setChoosed(false);
			}
		}
	}

	
	public boolean isMoving()
	{
		// 文件列表模式
		if (getAnimationType()== CSStaticData.ANIMATION_TYPE.NONEGROUP_OPENING)
		{
			// 判断是否为进入动画
			return true;
		}
		else if (getAnimationType()== CSStaticData.ANIMATION_TYPE.NONEGROUP_CLOSING)
		{
			// 判断是否为退出动画
			return true;
		}
		
		// 文件夹模式
		if (m_type != CSStaticData.LIST_TYPE.LIST_NONE)
		{
			if (!m_b_enter)
			{
				// 文件夹移动
				if (getAnimationType()== CSStaticData.ANIMATION_TYPE.NONE)
					return false;
				else
					return true;
			}
			else
			{

					synchronized(m_list)
					{	
						for(int i = m_index-CSStaticData.group_none_row_num; i < m_index+18; i++)
						{
							if (i < 0 || i > size()-1)
								continue;
							
							Element elem = m_list.get(i);
							if (elem != null)
							{
								if (elem.getAnimationType() != CSStaticData.ANIMATION_TYPE.NONE)
								{
									return true;
								}
							}
						}
					}

				
		    	return false;
			}
		}
		else
		{
			// 文件列表模式
			synchronized(m_list)
			{	
				for(int i = m_index-CSStaticData.group_none_row_num; i < m_index+18; i++)
				{
					if (i < 0 || i > size()-1)
						continue;
					
					Element elem = m_list.get(i);
					if (elem != null)
					{
						if (elem.getAnimationType() != CSStaticData.ANIMATION_TYPE.NONE)
						{
							return true;
						}
					}
				}
			}
			
	    	return false;
		}
	}

	public void calcPosition()
	{
		if (m_type == CSStaticData.LIST_TYPE.LIST_NONE || m_b_enter == true)
		{
			// 显示文件列表
			CSStaticData.eye_center_none[1] = 0;// 恢复为默认值
			
			if (size() <= 0)
			{
				m_animation_type = CSStaticData.ANIMATION_TYPE.NONE;
				m_animation_index = -1;		
			}
				
			
			int total_col = size() / CSStaticData.group_none_row_num;
			
			int num = 12;		
			
			int start = m_index - num;
			int end = m_index + num * 2;
			//loadFileTexture(start, end);
			
			if (m_animation_type == CSStaticData.ANIMATION_TYPE.NONEGROUP_OPENING)
			{
				// 打开NoneGroup

				int move_num = 18/CSStaticData.group_none_row_num;
				int start1 = m_index+17;
				int end1 = m_index-CSStaticData.group_none_row_num;
				for (int i = start1; i >= end1; i--)
				{
					if (i < 0 || i > size()-1)
						continue;
					
					int colIndex = i / CSStaticData.group_none_row_num;
					
					Element elem = getIndex(i);
					if (elem != null)
					{
						if (m_nongroup_animation_frame_num < move_num)
							elem.move(m_nongroup_animation_step[0], m_nongroup_animation_step[1], m_nongroup_animation_step[2]);
						else
						{
							int n = m_nongroup_animation_frame_num - move_num;
							
							if (n >= colIndex*5 && n < colIndex*5+5)
							{
								elem.setAngle(0, 15, 0);
							}
							else if (n < colIndex*5)
							{
								elem.move(m_nongroup_animation_step[0], m_nongroup_animation_step[1], m_nongroup_animation_step[2]);
							}
							else
							{
								if (n >= (total_col+1)*5)
								{
									m_nongroup_animation_frame_num = -1;
									m_animation_type = CSStaticData.ANIMATION_TYPE.NONE;
									break;
								}
							}
							
						}
						
						elem.calcPosition();
						
					}
				}
				
				int c = total_col > 12 ? 12 : total_col;
				if (m_nongroup_animation_frame_num > move_num + 12 * 5)
				{
					m_nongroup_animation_frame_num = -1;
					m_animation_type = CSStaticData.ANIMATION_TYPE.NONE;
					refresh(0);
				}
				
				m_nongroup_animation_frame_num++;
			}
			else if(m_animation_type == CSStaticData.ANIMATION_TYPE.NONEGROUP_CLOSING)
			{
				// 关闭NoneGroup
				// 18 表示最多移动18个元素
				int start1 = m_index+17;
				int end1 = m_index - CSStaticData.group_none_row_num ;
				for (int i = start1; i >= end1; i--)
				//for (int i = size()-1; i >= 0; i--)
				{
					if (i < 0 || i > size()-1)
						continue;
					
					Element elem = getIndex(i);
					if (elem != null)
					{
						int colIndex = start1/CSStaticData.group_none_row_num - i / CSStaticData.group_none_row_num;
						if (m_nongroup_animation_frame_num > colIndex*2 && m_nongroup_animation_frame_num < colIndex*2+5)
						{
							elem.setAngle(0, -15, 0);
						}
						else if (m_nongroup_animation_frame_num >= colIndex*2+2)
						{
							elem.move(m_nongroup_animation_step[0], m_nongroup_animation_step[1], m_nongroup_animation_step[2]);
						}
						
						elem.calcPosition();
					}
				}
				
				//float[] last_elem_pos = m_list.get(m_list.size()-1).getPosition();
				int last = m_index+17;
				if (last < 0) last = 0;
				else if (last > size()-1) last = size()-1;
				float[] last_elem_pos = m_list.get(last).getPosition();
				if (last_elem_pos[0] < CSStaticData.x_list_pos_start + CSStaticData.x_list_pos_offset * -2)
				{
					// 最后一个 元素移除后动画停止
					m_nongroup_animation_frame_num = -1;
					m_animation_type = CSStaticData.ANIMATION_TYPE.NONE;
					
					m_b_enter = false;
					
				}
				m_nongroup_animation_frame_num++;
			}
			else
			{
				float angle = 0;
				if (WiGalleryOpenGLRenderer.m_b_horizontal_screen)
				{
					// 横屏			
					angle = 15.0f;
				}
				else
				{
					// 竖屏			
					angle = 30.0f;
				}
				int curRow = m_index / CSStaticData.group_none_row_num;
				
				
				for (int i = 0; i <= size()-1; i++)
				{
					Element elem = getIndex(i);
					if (elem != null)
					{
						if (elem.getAnimationType() != CSStaticData.ANIMATION_TYPE.NONE)
							elem.calcPosition();
						else
						{				
							int row = i % CSStaticData.group_none_row_num; // 行
							int col = i / CSStaticData.group_none_row_num - curRow;  // 列
							
							float x = (float) (CSStaticData.x_list_pos_start + CSStaticData.x_list_pos_offset * col);
							float y = (float) (CSStaticData.y_list_pos_start + CSStaticData.y_list_pos_offset * row);
							float z = (float) (CSStaticData.z_list_pos_start + CSStaticData.z_list_pos_offset * col);
							
							elem.moveTo(x, y, z);
							elem.setAngleTo(0, angle, 0);
						}
					}
				}
			}
			
			/*
			for (int i = size()-1; i >= 0; i--)
			{
				Element elem = getIndex(i);
				if (elem != null)
				{
					if (i >= start && i < end)
					{
						// 不作处理

					}
					else
					{
						if (elem.m_is_load_texture)
						{
							WiGalleryOpenGLRenderer.m_resource_manager.DeleteTextureID(elem);
						}
						
					}
				}
			}
			*/
			
		}
		else
		{
			// 文件夹模式
			if (m_animation_type == CSStaticData.ANIMATION_TYPE.LEFT_SHIFT)
			{
				// 左移
				m_pos_index -= SCurve.m_mov_num;
				
				if (m_animation_index == SCurve.m_step-1)
				{
					m_animation_type = CSStaticData.ANIMATION_TYPE.NONE;
					m_animation_index = -1;
				}
				m_animation_index++;
				
				float[] pos = WiGalleryOpenGLRenderer.m_element_group.m_scurve.getPosition(m_pos_index);
				moveTo(pos[0], pos[1], pos[2]);
			}
			else if (m_animation_type == CSStaticData.ANIMATION_TYPE.RIGHT_SHIFT)
			{
				// 右移
				m_pos_index += SCurve.m_mov_num;
				
				if (m_animation_index == SCurve.m_step-1)
				{
					m_animation_type = CSStaticData.ANIMATION_TYPE.NONE;
					m_animation_index = -1;
				}
				
				m_animation_index++;
				
				float[] pos = WiGalleryOpenGLRenderer.m_element_group.m_scurve.getPosition(m_pos_index);
				moveTo(pos[0], pos[1], pos[2]);
			}
			else if (m_animation_type == CSStaticData.ANIMATION_TYPE.GENTLE_SHIFT)
			{
				// 右轻移
				if (m_offset_index < 0)
					m_pos_index-=SCurve.m_mov_num;
				else
					m_pos_index+=SCurve.m_mov_num;
				
				if (m_animation_index == Math.abs(m_offset_index*SCurve.m_step/5)-1)
				{
					m_animation_type = CSStaticData.ANIMATION_TYPE.NONE;
					m_animation_index = -1;
				}
				
				m_animation_index++;
				
				float[] pos = WiGalleryOpenGLRenderer.m_element_group.m_scurve.getPosition(m_pos_index);
				moveTo(pos[0], pos[1], pos[2]);
			}
			else
			{
				//~!
				//float[] pos = WiGalleryOpenGLRenderer.m_element_group.m_scurve.getPosition(m_pos_index);
				//moveTo(pos[0], pos[1], pos[2]);
			}
		}
	}
	
	
	public void onDraw(GL10 gl, MEDIA_VIEW view)
	{			
		int num = size();
		if (num <= 0)
			return;
		
		if (m_elementNumTexture == CSStaticData.INVALID_TEXTURE_ID || m_elementNum != num)
		{
			// 更新文件个数纹理
			if (m_type == CSStaticData.LIST_TYPE.LIST_DATE)
			{
				Bitmap bm = WiGalleryOpenGLRenderer.m_date_bitmap.copy(WiGalleryOpenGLRenderer.m_date_bitmap.getConfig(), true);
				m_elementNumTexture = WiGalleryOpenGLRenderer.m_resource_manager.GetTextureID(m_elementNumTexture, 
						bm,  
						String.format("%d", num), 
						bm.getHeight()*2/5,
						CSStaticData.ALIGN_TYPE.ALIGN_CENTRE,
						true);
				bm.recycle();
			}
			else if (m_type == CSStaticData.LIST_TYPE.LIST_NONE)
			{
				// 不做处理
			}
			else
			{
				// Location
				Bitmap bm = WiGalleryOpenGLRenderer.m_location_bitmap.copy(WiGalleryOpenGLRenderer.m_location_bitmap.getConfig(), true);
				m_elementNumTexture = WiGalleryOpenGLRenderer.m_resource_manager.GetTextureID(m_elementNumTexture, 
						bm,  
						String.format("%d", num), 
						bm.getHeight()/5 ,
						CSStaticData.ALIGN_TYPE.ALIGN_CENTRE,
						true);	
				
				bm.recycle();
			}
			
			m_elementNum = num;
		}
		
		if(m_nameTexture == CSStaticData.INVALID_TEXTURE_ID && m_name!= null)
		{
			WiGalleryOpenGLRenderer.m_label_bitmap.eraseColor(0);

			m_nameTexture = WiGalleryOpenGLRenderer.m_resource_manager.GetTextureID(m_nameTexture, 
					WiGalleryOpenGLRenderer.m_label_bitmap,  
					m_name, 
					WiGalleryOpenGLRenderer.m_label_bitmap.getHeight() * 4 / 5,
					CSStaticData.ALIGN_TYPE.ALIGN_LEFT,
					false);
		}
		
		if (m_type == CSStaticData.LIST_TYPE.LIST_NONE || m_b_enter == true|| m_type == CSStaticData.LIST_TYPE.LIST_INVALID)
		{
			// 显示文件列表
			CSStaticData.eye_center_none[1] = 0;// 恢复为默认值
			drawGroupNone(gl, view);
		}
		else
		{
			m_view_side = view;
			// 文件夹模式---
	
			// 显示文件夹
			// 显示前三张图片
			gl.glPushMatrix();
			
			if (m_type == CSStaticData.LIST_TYPE.LIST_LOCATION_1 
					|| m_type == CSStaticData.LIST_TYPE.LIST_LOCATION_2
					|| m_type == CSStaticData.LIST_TYPE.LIST_LOCATION_3
					|| m_type == CSStaticData.LIST_TYPE.LIST_LOCATION_4)
			{
				gl.glScalef(1.0f, 1.0f, 1.0f);
			}
			
			gl.glTranslatef(m_x_offset, m_y_offset, m_z_offset);
//			gl.glRotatef(m_zrot, 0f, 0f, 1f); //不能转Z轴，也不能把Z轴转为0
			gl.glRotatef(m_yrot, 0f, 1f, 0f);
			gl.glRotatef(m_xrot, 1f, 0f, 0f);

			int id = CSStaticData.INVALID_TEXTURE_ID;
			
			float a = 0.8f;
			float textureHeight = a * 0.4f;
			float textureWidth = a * 0.4f;
			
			if (m_type == CSStaticData.LIST_TYPE.LIST_DATE)
			{
				// 日期分组
				m_image_vers[0] = - textureWidth / 2.0f; m_image_vers[1] = - textureHeight / 2.0f;  m_image_vers[2] = 0;
				m_image_vers[3] =  textureWidth / 2.0f; m_image_vers[4] = - textureHeight / 2.0f;  m_image_vers[5] = 0;
				m_image_vers[6] = - textureWidth / 2.0f; m_image_vers[7] =  textureHeight / 2.0f;  m_image_vers[8] = 0;
				m_image_vers[9] =  textureWidth / 2.0f; m_image_vers[10] =  textureHeight / 2.0f;  m_image_vers[11] = 0;
			}
			else
			{
				// 地理位置分组
				m_image_vers[0] = - textureWidth / 3.0f; m_image_vers[1] = - textureHeight / 3.0f;  m_image_vers[2] = 0;
				m_image_vers[3] =  textureWidth / 3.0f; m_image_vers[4] = - textureHeight / 3.0f;  m_image_vers[5] = 0;
				m_image_vers[6] = - textureWidth / 3.0f; m_image_vers[7] =  textureHeight / 3.0f;  m_image_vers[8] = 0;
				m_image_vers[9] =  textureWidth / 3.0f; m_image_vers[10] =  textureHeight / 3.0f;  m_image_vers[11] = 0;
			
			}
			
			if (m_cover_buff != null)
				m_cover_buff.clear();
			
			m_cover_buff = ResourceManager.makeFloatBuffer(m_image_vers);	

			
			for (int i = 2; i >= 0; i--)
			{
				if (i >= m_list.size())
					continue;
				
				Element elem = m_list.get(i);
				if (elem != null)
				{
					if (!elem.m_is_load_texture)
					{
						WiGalleryOpenGLRenderer.m_data_manager.setRequestLoadFileList(elem);
					}
					
					id = elem.getTextureID(view);
					
					if (id == CSStaticData.INVALID_TEXTURE_ID)
					{
						if (elem.getFileType() == CSStaticData.MEDIA_TYPE.NORMAL_VIDEO || elem.getFileType() == CSStaticData.MEDIA_TYPE.STOERE_VIDEO)
							id = WiGalleryOpenGLRenderer.m_default_video_icon;
						else
							id = WiGalleryOpenGLRenderer.m_default_file_icon;;
					}
										
					gl.glPushMatrix();
					
					if (m_type == CSStaticData.LIST_TYPE.LIST_DATE)
					{
						gl.glRotatef(-20.0f, 1.0f, 0.0f, 0);
						gl.glRotatef(-15.0f, 0.0f, 1.0f, 0);
						
						if (i == 0)
							gl.glTranslatef(0, 0, 0);
						else if (i == 1)
							gl.glTranslatef(-0.02f , 0.03f, -0.01f);
						else
							gl.glTranslatef(0.03f , 0.01f, -0.02f);	
					}
					else
					{
						gl.glRotatef(90.0f, 1.0f, 0.0f, 0);
						
						if (i == 0)
							gl.glTranslatef(0, 0, 0);
						else if (i == 1)
							gl.glTranslatef(0.01f , 0.01f, -0.01f);
						else
							gl.glTranslatef(0.02f , 0.02f, -0.02f);
					}
					
	
					
					// 绘制图片
					//gl.glDisable(GL10.GL_DEPTH_TEST);
			        gl.glBindTexture(GL10.GL_TEXTURE_2D, id);
			        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_cover_buff);
			        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, m_tex_buff);
			        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			        //gl.glEnable(GL10.GL_DEPTH_TEST);
			        
			        // 绘制文件类型				
			        if (WiGalleryOpenGLRenderer.m_zmap_videoindicator_id != CSStaticData.INVALID_TEXTURE_ID 
			        		&& (elem.getFileType() == CSStaticData.MEDIA_TYPE.NORMAL_VIDEO || elem.getFileType() == CSStaticData.MEDIA_TYPE.STOERE_VIDEO))
			        {
						gl.glPushMatrix();
				        gl.glBindTexture(GL10.GL_TEXTURE_2D, WiGalleryOpenGLRenderer.m_zmap_videoindicator_id);
				        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_cover_buff);
				        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, m_tex_buff);
				        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);   
						gl.glPopMatrix();
			        }
			        
			       
			        // 绘制边框
			        gl.glDisable(GL10.GL_TEXTURE_2D);
			        gl.glColor4f(0.4f, 0.4f, 0.4f, 1.0f);
			        gl.glLineWidth(2.0f);
			        
			        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_cover_buff);
			        gl.glDrawElements(GL10.GL_LINE_LOOP, 4,	GL10.GL_UNSIGNED_BYTE, m_line_index_buff);
			        gl.glEnable(GL10.GL_TEXTURE_2D);
			        
			        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			        
			        gl.glPopMatrix();
				}
			}
					
			// 显示文件夹名称
			if (m_nameTexture != CSStaticData.INVALID_TEXTURE_ID)
			{
				int w = WiGalleryOpenGLRenderer.m_label_bitmap.getWidth();
				int h = WiGalleryOpenGLRenderer.m_label_bitmap.getHeight();
				
				textureHeight = 0.07f;
				textureWidth = textureHeight * w / h;
				m_image_vers[0] = - textureWidth / 2.0f; m_image_vers[1] = - textureHeight / 2.0f;  m_image_vers[2] = 0;
				m_image_vers[3] =  textureWidth / 2.0f; m_image_vers[4] = - textureHeight / 2.0f;  m_image_vers[5] = 0;
				m_image_vers[6] = - textureWidth / 2.0f; m_image_vers[7] =  textureHeight / 2.0f;  m_image_vers[8] = 0;
				m_image_vers[9] =  textureWidth / 2.0f; m_image_vers[10] =  textureHeight / 2.0f;  m_image_vers[11] = 0;
				
				if (m_cover_buff != null)
					m_cover_buff.clear();
				
				m_cover_buff = ResourceManager.makeFloatBuffer(m_image_vers);	

				gl.glPushMatrix();
				
				if (m_type == CSStaticData.LIST_TYPE.LIST_DATE)
				{
					gl.glRotatef(-15.0f, 1.0f, 0, 0);
					gl.glTranslatef(0.17f, -0.18f, 0.05f);
				}
				else
				{
					gl.glRotatef(90.0f, 1.0f, 0.0f, 0);
					gl.glTranslatef(0.25f, -0.15f, 0.01f);
				}
				
		        gl.glBindTexture(GL10.GL_TEXTURE_2D, m_nameTexture);
		        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_cover_buff);
		        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, m_tex_buff);
		        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		        
		        gl.glPopMatrix();
			}
			
			// 显示文件张数
			if (m_elementNumTexture != CSStaticData.INVALID_TEXTURE_ID)
			{
				int w = 0;
				int h = 0;
				
				if (m_type == CSStaticData.LIST_TYPE.LIST_DATE)
				{
					w = WiGalleryOpenGLRenderer.m_date_bitmap.getWidth();
					h = WiGalleryOpenGLRenderer.m_date_bitmap.getHeight();
				}
				else
				{
					w = WiGalleryOpenGLRenderer.m_location_bitmap.getWidth();
					h = WiGalleryOpenGLRenderer.m_location_bitmap.getHeight();
				}

				if (w !=0 && h !=0 )
				{
					textureHeight = 0.15f;
					textureWidth = textureHeight * w / h;
					m_image_vers[0] = - textureWidth / 2.0f; m_image_vers[1] = - textureHeight / 2.0f;  m_image_vers[2] = 0;
					m_image_vers[3] =  textureWidth / 2.0f; m_image_vers[4] = - textureHeight / 2.0f;  m_image_vers[5] = 0;
					m_image_vers[6] = - textureWidth / 2.0f; m_image_vers[7] =  textureHeight / 2.0f;  m_image_vers[8] = 0;
					m_image_vers[9] =  textureWidth / 2.0f; m_image_vers[10] =  textureHeight / 2.0f;  m_image_vers[11] = 0;
			
					if (m_cover_buff != null)
						m_cover_buff.clear();
					
					m_cover_buff = ResourceManager.makeFloatBuffer(m_image_vers);
					
					gl.glPushMatrix();
					
					if (m_type == CSStaticData.LIST_TYPE.LIST_DATE)
					{
						gl.glRotatef(-15.0f, 1.0f, 0, 0);
						gl.glTranslatef(-0.13f, -0.09f, 0.05f);
					}
					else
					{
						gl.glRotatef(90.0f, 1.0f, 0.0f, 0);
						gl.glTranslatef(-0.08f, -0.12f, 0.01f);
					}
					
			        gl.glBindTexture(GL10.GL_TEXTURE_2D, m_elementNumTexture);
			        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_cover_buff);
			        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, m_tex_buff);
			        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			        
			        gl.glPopMatrix();
				}
			}
			
	        // 绘制选择标志
	        if (m_status == ELEM_STATUS.SELECTED_STATUS )
	        {
	        	gl.glPushMatrix();
	        	
	        	if (m_type == CSStaticData.LIST_TYPE.LIST_DATE)
	        	{
	        		gl.glRotatef(-15.0f, 1.0f, 0, 0);
	        		gl.glTranslatef(0.095f, -0.095f, 0.05f);
	        	}
	        	else
	        	{
	        		gl.glRotatef(90.0f, 1.0f, 0.0f, 0);
	        		gl.glTranslatef(0.095f, -0.095f, 0.01f);
	        	}
				
	        	if(m_is_choosed)
	        	{
	        		if (WiGalleryOpenGLRenderer.m_zmap_select_gallery_select_id != CSStaticData.INVALID_TEXTURE_ID)
	        		{
				        gl.glBindTexture(GL10.GL_TEXTURE_2D, WiGalleryOpenGLRenderer.m_zmap_select_gallery_select_id);
				        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_cover_buff);
				        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, m_tex_buff);
				        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4); 
	        		}
	        	}
	        	else
	        	{
	        		if (WiGalleryOpenGLRenderer.m_zmap_select_gallery_none_id != CSStaticData.INVALID_TEXTURE_ID)
	        		{
				        gl.glBindTexture(GL10.GL_TEXTURE_2D, WiGalleryOpenGLRenderer.m_zmap_select_gallery_none_id);
				        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_cover_buff);
				        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, m_tex_buff);
				        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4); 
	        		}
	        	}
	        	gl.glPopMatrix();
	        }
	        
			gl.glPopMatrix();
		}
	}
	
	
	public boolean onTouchEvent(float x, float y)
	{
        if (isMoving())
        {
        	return true;
        }			
		
        if (m_type == CSStaticData.LIST_TYPE.LIST_NONE)
        {
	        for (int i = m_index-3; i < m_index+24; i++)
	        {
	        	if (i < 0 || i > m_list.size()-1)
	        	{
	        		continue;
	        	}
	        	
	    		Element elem = m_list.get(i);
	    		if(elem != null)
	    		{
	    			if (elem.onTouchEvent(x, y))
	    			{
	    				if(CSStaticData.DEBUG)
	    					Log.d("ElementList", String.format("choose [%d, %s]",i, elem.getName()));
	    				return true;
	    			}
	    		}
	        }
        }
        else
        {        	
        	if (m_b_enter)
        	{
    	        for (int i = m_index-3; i < m_index+24; i++)
    	        {
    	        	if (i < 0 || i > m_list.size()-1)
    	        	{
    	        		continue;
    	        	}
    	        	
    	    		Element elem = m_list.get(i);
    	    		if(elem != null)
    	    		{
    	    			if (elem.onTouchEvent(x, y))
    	    			{
    	    				if(CSStaticData.DEBUG)
    	    					Log.d("ElementList", String.format("choose [%d, %s]",i, elem.getName()));
    	    				return true;
    	    			}
    	    		}
    	        }
        	}
        	else
        	{
            	//~
            	// 文件夹模式
        		if(CSStaticData.isInRect(m_x_offset, m_y_offset, m_z_offset, x, y, 0.25f))
				{
	    			return true;
	    		}
	    		else
	    		{
	    			return false;
	    		}
        	}
        }
		return false;
	}
	
	/**
	 *  获取打开的文件路径与文件名
	 */
	public static String getOpenFilePathName()
	{
		return m_open_file_name;
	}
	// 

	
	/**
	 * 排列顺序显示
	 * @param order  0：升序     1：降序
	 */
	public void setOrder(int order)
	{
		if(order == 0){
			CSStaticData.g_sort_order_mode = true;
		}
		if(order == 1){
			CSStaticData.g_sort_order_mode = false;
		}
		
		if (m_sort_order == order)
			return;
		
		m_sort_order = order;
		
		if (order == 0)
			m_list = WiGalleryOpenGLRenderer.m_data_manager.sortByAsc(m_list);
		else if (order == 1)
			m_list = WiGalleryOpenGLRenderer.m_data_manager.sortByDesc(m_list);
		
		refresh(0);
	}
	
	// 
	/**
	 * 获取选中的文件个数
	 * @return
	 */
	public int getSelectedNum()
	{
		int i = 0;

		int size = size();
		for (int j = 0; j < size; j++)
		{
			Element elem = getIndex(j);
        	if (elem != null && elem.isChoosed())
        	{
        		i++;
        	}
		}
        
        return i;
	}
	
	/**
	 * Date文件夹模式下的移动
	 * @param index
	 */
	public void moveToIndex(int index)
	{
		m_pos_index = index;
		
		float[] pos = WiGalleryOpenGLRenderer.m_element_group.m_scurve.getPosition(m_pos_index);
		moveTo(pos[0], pos[1], pos[2]);
	}
	
	/////
	// 保存状态时使用
	public int getIndex()
	{
		return m_pos_index;
	}
	
	public void setIndex(int index)
	{
		m_pos_index = index;
	}
	////
	/**
	 * Date文件夹模式下的移动
	 * @param index
	 */
	public void moveToIndexTo(int val)
	{
		m_pos_index += val;
		
		float[] pos = WiGalleryOpenGLRenderer.m_element_group.m_scurve.getPosition(m_pos_index);
		moveTo(pos[0], pos[1], pos[2]);
	}
	
	/**
	 * 在none模式或者文件夹打开模式下移动
	 * @param index： 显示区域第一个显示元素的标号
	 */
	public void shiftTo(int index, boolean bAnimation, boolean bAlign)
	{	
		if (bAlign)
		{
			// 第一屏与最后一屏时进行对其处理
			int tRow = 3; //每屏显示的列数
			if (WiGalleryOpenGLRenderer.m_b_horizontal_screen)
				tRow = 4;
			int tIndex = (size() +CSStaticData.group_none_row_num-1) / CSStaticData.group_none_row_num - 1;
			if (index < 0) index = 0;
			else if (index > tIndex-tRow) index = tIndex - tRow;
		}
		
		// 12表示一屏显示的文件个数
		if (size() <= 12)
			index = 0;
		
		
		if (m_type == CSStaticData.LIST_TYPE.LIST_NONE || m_b_enter)
		{
			int cIndex = m_index / CSStaticData.group_none_row_num;
			int cd = cIndex - index;

			
			int num_frame = 2 * Math.abs(cd);
			float x = CSStaticData.x_list_pos_offset * cd;
			float y = Math.abs(0 * cd);
			float z = CSStaticData.z_list_pos_offset * cd;
			
			
			float[] steps = {x/num_frame, y/num_frame, z/num_frame};
			float[] final_Angle = {0, m_angle, 0}; 
			
			for (int i = 0; i < m_list.size(); i++)
			{
				int row = i % CSStaticData.group_none_row_num; // 行
				int col = i / CSStaticData.group_none_row_num - index;  // 列
				
				x = (float) (CSStaticData.x_list_pos_start + CSStaticData.x_list_pos_offset * col);
				y = (float) (CSStaticData.y_list_pos_start + CSStaticData.y_list_pos_offset * row);
				z = (float) (CSStaticData.z_list_pos_start + CSStaticData.z_list_pos_offset * col);
				
				float[] final_pos = {x,y,z};
				
				Element elem = m_list.get(i);
				if (elem != null)
				{
					if (bAnimation)
					{
						elem.startAnimation(CSStaticData.ANIMATION_TYPE.GENTLE_SHIFT, num_frame, steps, final_pos, final_Angle);
					}
					else
					{
						elem.moveTo(final_pos[0], final_pos[1], final_pos[2]);
						elem.setAngleTo(final_Angle[0], final_Angle[1], final_Angle[2]);
					}
				}
			}
			
			m_index = index * CSStaticData.group_none_row_num; 
		}
		else
		{
			if (m_type == CSStaticData.LIST_TYPE.LIST_DATE)
			{
				if (bAnimation)
				{
					m_offset_index = index;
					m_animation_type = CSStaticData.ANIMATION_TYPE.GENTLE_SHIFT;
				}
				else
				{
					m_pos_index += index*SCurve.m_step;
						
						
					float[] pos = WiGalleryOpenGLRenderer.m_element_group.m_scurve.getPosition(m_pos_index);
					moveTo(pos[0], pos[1], pos[2]);
				}
			}
		}
	}
	
	public void startAnimation(CSStaticData.ANIMATION_TYPE type)
	{
		int row = 0; // 行
		int col = 0;  // 列
		
		float x = 0;
		float y = 0;
		float z = 0;
		
		if (isMoving())
		{
			return;
		}
		
		if (m_b_enter || m_type == CSStaticData.LIST_TYPE.LIST_NONE)
		{
			
			if (type == CSStaticData.ANIMATION_TYPE.LEFT_SHIFT)
			{
				// 左移
				int n = 12;		
				
				if (m_index + n >= size() ||  size() <= n)
					return;
					
				int num_frame = 20;
				
				row = n % CSStaticData.group_none_row_num; // 行
				col = n / CSStaticData.group_none_row_num;  // 列
				
				x = Math.abs(CSStaticData.x_list_pos_offset * col);
				y = Math.abs(CSStaticData.y_list_pos_offset * row);
				z = Math.abs(CSStaticData.z_list_pos_offset * col);
				
				m_index += n;
				
				float[] steps = {x/num_frame, y/num_frame, z/num_frame};
				float[] final_Angle = {0, m_angle, 0}; 
				
				int col_offset = m_index / CSStaticData.group_none_row_num;
				for (int i = 0; i < m_list.size(); i++)
				{
					row = i % CSStaticData.group_none_row_num; // 行
					col = i / CSStaticData.group_none_row_num - col_offset;  // 列
					
					x = (float) (CSStaticData.x_list_pos_start + CSStaticData.x_list_pos_offset * col);
					y = (float) (CSStaticData.y_list_pos_start + CSStaticData.y_list_pos_offset * row);
					z = (float) (CSStaticData.z_list_pos_start + CSStaticData.z_list_pos_offset * col);
					
					float[] final_pos = {x,y,z};
					
					Element elem = m_list.get(i);
					if (elem != null)
					{
						elem.startAnimation(type, num_frame, steps, final_pos, final_Angle);
					}
				}
			}
			else if (type == CSStaticData.ANIMATION_TYPE.RIGHT_SHIFT)
			{
				// 右移
				int n = 12;
				
				
				if (m_index <= 0)
				{
					refresh(0);
					return;
				}
				
				int num_frame = 20;
				
				row = n % CSStaticData.group_none_row_num; // 行
				col = n / CSStaticData.group_none_row_num;  // 列
				
				x = Math.abs(CSStaticData.x_list_pos_offset * col);
				y = Math.abs(CSStaticData.y_list_pos_offset * row);
				z = Math.abs(CSStaticData.z_list_pos_offset * col);
				
				m_index -= n;
				
				if (m_index <= 0)
				{
					m_index = 0;
				}
				
				float[] steps = {x/num_frame, y/num_frame, z/num_frame};
				float[] final_Angle = {0, m_angle, 0}; 
				
				int col_offset = m_index / CSStaticData.group_none_row_num;
				for (int i = 0; i < m_list.size(); i++)
				{
					row = i % CSStaticData.group_none_row_num; // 行
					col = i / CSStaticData.group_none_row_num - col_offset;  // 列
					
					x = (float) (CSStaticData.x_list_pos_start + CSStaticData.x_list_pos_offset * col);
					y = (float) (CSStaticData.y_list_pos_start + CSStaticData.y_list_pos_offset * row);
					z = (float) (CSStaticData.z_list_pos_start + CSStaticData.z_list_pos_offset * col);
					
					float[] final_pos = {x,y,z};
					
					Element elem = m_list.get(i);
					if (elem != null)
					{
						elem.startAnimation(type, num_frame, steps, final_pos, final_Angle);
					}
				}
			}
			else if (type == CSStaticData.ANIMATION_TYPE.NONEGROUP_OPENING)
			{
				//開啟Nongroup
				// 初始化位置

				int n = 18;// m_list.size();
				int last_col = n/CSStaticData.group_none_row_num;
				if (WiGalleryOpenGLRenderer.m_b_horizontal_screen)
				{
					// 横屏			
					m_angle = 20.0f - 75;
				}
				else
				{
					// 竖屏			
					m_angle = 30.0f - 75;
				}
				
				for (int i = 0; i < n; i++)
				{
					if (i > size()-1)
						continue;
					
					Element elem = m_list.get(i);
					if (elem != null)
					{
						row = i % CSStaticData.group_none_row_num; // 行
						col = i / CSStaticData.group_none_row_num  - last_col;  // 列
						
						x = (float) (CSStaticData.x_list_pos_start + CSStaticData.x_list_pos_offset * col / 5);
						y = (float) (CSStaticData.y_list_pos_start + CSStaticData.y_list_pos_offset * row);
						z = (float) (CSStaticData.z_list_pos_start + CSStaticData.z_list_pos_offset * col / 5);
						
						elem.moveTo(x, y, z);
						elem.setAngleTo(0, m_angle, 0);
					}
				}

				// 开始动画
				
				m_nongroup_animation_step[0] = CSStaticData.x_list_pos_offset/5;
				m_nongroup_animation_step[1] = 0;
				m_nongroup_animation_step[2] = CSStaticData.z_list_pos_offset/5;
				
				m_nongroup_animation_frame_num = 0;
				m_animation_type = CSStaticData.ANIMATION_TYPE.NONEGROUP_OPENING;

				
			}
			else if(type == CSStaticData.ANIMATION_TYPE.NONEGROUP_CLOSING)
			{
				//关闭Nongroup
				m_nongroup_animation_step[0] = -CSStaticData.x_list_pos_offset * 2/5.0f;
				m_nongroup_animation_step[1] = 0;
				m_nongroup_animation_step[2] = -CSStaticData.z_list_pos_offset * 2/5.0f;	
				
				m_nongroup_animation_frame_num = 0;
				m_animation_type = CSStaticData.ANIMATION_TYPE.NONEGROUP_CLOSING;
			}
			else
			{
				//~!
			}
		}
		else
		{
			if (m_type == CSStaticData.LIST_TYPE.LIST_DATE)
			{
				m_animation_type = type;
			}
			else
			{
				//~!
			}
		}
	}
	
	private void loadFileTexture(int start, int end)
	{
		int min = start > 0 ? start : 0;
		int max = end > size() ? size() : end;
		
		for (int i = min; i < max; i++)
		{
			Element elem = getIndex(i);
			if (elem != null)
			{
				if (!elem.m_is_load_texture)
				{
					WiGalleryOpenGLRenderer.m_data_manager.setRequestLoadFileList(elem);
				}
			}
		}
	}
	

	
	private void drawGroupNone(GL10 gl, MEDIA_VIEW view)
	{
		if (size() <= 0)
			return;
		
		int num = 12;		
		
		int start = m_index - num;
		int end = m_index + num * 2;
		
		if (m_animation_type == CSStaticData.ANIMATION_TYPE.NONEGROUP_OPENING)
		{
			// 打开NoneGroup

			//int move_num = ((m_list.size()-1)/CSStaticData.group_none_row_num + 2);
			int move_num = 18/CSStaticData.group_none_row_num;
			int start1 = m_index+17;
			int end1 = m_index-CSStaticData.group_none_row_num;
			//for (int i = start1; i >= end1; i--)
			for (int i = end1; i <= start1; i++)
			{
				if (i < 0 || i > size()-1)
					continue;
				
				int colIndex = i / CSStaticData.group_none_row_num;
				
				Element elem = getIndex(i);
				if (elem != null)
				{					
					if (i >= start && i < end)
					{	
						
						if (!elem.m_is_load_texture)
						{
							WiGalleryOpenGLRenderer.m_data_manager.setRequestLoadFileList(elem);
						}
						
						elem.SetViewSide(view);
						elem.onDraw(gl, true);
					}
					else
					{
						if (elem.m_is_load_texture)
						{
							WiGalleryOpenGLRenderer.m_resource_manager.DeleteTextureID(elem);
						}
						
						elem.SetViewSide(view);
						elem.onDraw(gl, false);
					}
					
				}
			}
			
		}
		else if(m_animation_type == CSStaticData.ANIMATION_TYPE.NONEGROUP_CLOSING)
		{
			// 关闭NoneGroup
			// 18 表示最多移动18个元素
			int start1 = m_index+17;
			int end1 = m_index - CSStaticData.group_none_row_num ;
			//for (int i = start1; i >= end1; i--)
			for (int i = end1; i <= start1; i++)
			{
				if (i < 0 || i > size()-1)
					continue;
				
				Element elem = getIndex(i);
				if (elem != null)
				{
					
					if (i >= start && i < end)
					{
						if (!elem.m_is_load_texture)
						{
							WiGalleryOpenGLRenderer.m_data_manager.setRequestLoadFileList(elem);
						}
						
						elem.SetViewSide(view);
						elem.onDraw(gl, true);
					}
					else
					{
						if (elem.m_is_load_texture)
						{
							WiGalleryOpenGLRenderer.m_resource_manager.DeleteTextureID(elem);
						}
						
						elem.SetViewSide(view);
						elem.onDraw(gl, true);
					}
					
				}
			}
		}
		else
		{
			 // 无动画
			//for (int i = size()-1; i >= 0; i--)
			for (int i = 0; i <= size()-1; i++)
			{
				Element elem = getIndex(i);
				if (elem != null)
				{
					if (i >= start && i < end)
					{
						if (!elem.m_is_load_texture)
						{
							WiGalleryOpenGLRenderer.m_data_manager.setRequestLoadFileList(elem);
						}
						
						elem.SetViewSide(view);
						elem.onDraw(gl, true);
					}
					else
					{
						if (elem.m_is_load_texture)
						{
							WiGalleryOpenGLRenderer.m_resource_manager.DeleteTextureID(elem);
						}
						
						elem.SetViewSide(view);
						elem.onDraw(gl, false);
					}
				}
			}
		}
	}
	
	public int getFavoriteNum()
	{
		int num = 0;
		for (int i = 0; i < m_list.size(); i++)
		{
			Element elem = m_list.get(i);
			if (elem != null && elem.bFavorite())
			{
				num++;
			}
		}
		
		return num;
	}
}
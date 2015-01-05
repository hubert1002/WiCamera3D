// Copyright (c) 2011 Wistron SWPC
// All rights reserved.
//	created:	2011/09/28
//	filename: 	FileIcon.java
//	author:		Neo Yeoh
//	purpose:	
 
package com.wistron.WiGallery;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import Utilities.CSStaticData;
import android.graphics.Bitmap;
import android.util.Log;


enum MEDIA_VIEW
{
	LEFT_VIEW,
	RIGHT_VIEW
}

enum ELEM_STATUS
{
	NORMAL_STATUS,
	SELECTED_STATUS,
	DISABLE_STATUS
}

public class Element extends ElementBase
{
	public  long m_id = -1;
	private int m_cover_left_texture_id;
	private int m_cover_right_texture_id;
	
	private boolean m_b_request = false;
	
	private CSStaticData.MEDIA_TYPE m_type = CSStaticData.MEDIA_TYPE.NORMAL_IMAGE;
	
	public	Bitmap m_bmpL;
	public	Bitmap m_bmpR;	
	
	public String[] m_str_address = null;
	private final Byte [] m_line_index ={0,1,3,2};
	private ByteBuffer m_line_index_buff = ResourceManager.makeByteBuffer(m_line_index);
	
	// globe
	private int m_filetype_texture_id = CSStaticData.INVALID_TEXTURE_ID;
	private int m_choosed_texture_id = CSStaticData.INVALID_TEXTURE_ID;
	private int m_not_choosed_texture_id = CSStaticData.INVALID_TEXTURE_ID;
	

	
	private FloatBuffer m_cover_buff = null;
	private FloatBuffer m_indicator_buff = null;
	private FloatBuffer m_tex_buff = null;
	
	private String m_str_name = null;
	private String m_str_date = null;
	
	public double m_longitude = 0;
	public double m_latitude = 0;
	
	private boolean m_b_favorite = false;

	// 判断是否加载纹理
	public boolean m_is_load_texture = false;
	
	
	private static final float[] m_image_vers =
	{          
		-0.5f, -0.5f,  0.0f,
		 0.5f, -0.5f,  0.0f,
		-0.5f,  0.5f,  0.0f,
		 0.5f,  0.5f,  0.0f,
    };
	
	private static final float[] m_indicator_vers =
	{          
		-0.125f, -0.125f,  0.0f,
		 0.125f, -0.125f,  0.0f,
		-0.125f,  0.125f,  0.0f,
		 0.125f,  0.125f,  0.0f,
    };
	
    private static final float[] m_image_coords =
    {       
		0,1, 1,1,
 		0,0, 1,0,
    };

	public Element(CSStaticData.MEDIA_TYPE fileType, int coverLeftTextureID, int coverRightTextureID, int choosedTextureID, int notChoosedTextureID, String name, String date, double latitude, double longitude)
	{
		m_type = fileType;
		m_is_choosed = false;
		
		m_str_name = name;
		m_str_date = date;
		
		m_longitude = longitude; //经度
		m_latitude = latitude; //纬度
		
		m_cover_left_texture_id = coverLeftTextureID;
		m_cover_right_texture_id = coverRightTextureID;
		
		switch (m_type)
		{
			case NORMAL_IMAGE:
			case STOERE_IMAGE:
			{
				m_filetype_texture_id = 0;
			}
			break;
			case STOERE_VIDEO:
			case NORMAL_VIDEO:
			{
				m_filetype_texture_id = WiGalleryOpenGLRenderer.m_zmap_videoindicator_id;
			}
				break;
			default:
				break;
			
		}
		
		m_choosed_texture_id = choosedTextureID;
		m_not_choosed_texture_id = notChoosedTextureID;
		
		m_cover_buff = ResourceManager.makeFloatBuffer(m_image_vers);
		m_indicator_buff = ResourceManager.makeFloatBuffer(m_indicator_vers);
		m_tex_buff = ResourceManager.makeFloatBuffer(m_image_coords);		
	}
	
	public void setFavorite(boolean bFavorite)
	{
		m_b_favorite = bFavorite;
	}
	
	public boolean bFavorite()
	{
		return m_b_favorite;
	}
	
	public void setTextureID(int coverLeftTextureID, int coverRightTextureID)
	{
		m_cover_left_texture_id = coverLeftTextureID;
		m_cover_right_texture_id = coverRightTextureID;
	}
	
	
	public  CSStaticData.MEDIA_TYPE getFileType()
	{
		return m_type;
	}
	
	/**
	 *  设置视图
	 * @param 0: 左视图 1:右视图
	 */
	public void SetViewSide(MEDIA_VIEW view)
	{
		m_view_side = view;
	}
	
	// 获取当前File的ID
	public String getName()
	{
		return m_str_name;
	}
	
	public void setName(String strName){
		m_str_name = strName;
	}
	
	public String getDate()
	{
		return m_str_date;
	}
	
	public boolean isRequest()
	{
		return m_b_request;
	}
	
	public void setRequest(boolean request)
	{
		m_b_request = request;
	}
	
	// 获取封面纹理ID
	public int getRightTextureID()
	{
		if(m_type == CSStaticData.MEDIA_TYPE.STOERE_IMAGE)
			return m_cover_right_texture_id;
		else
			return m_cover_left_texture_id;
	}
	
	public int getTextureID(MEDIA_VIEW view)
	{
		if (view == MEDIA_VIEW.LEFT_VIEW)
			return getLeftTextureID();
		else
		{
			return getRightTextureID();
		}
	}
	
	public int getLeftTextureID()
	{
		return m_cover_left_texture_id;
	}

	public void calcPosition()
	{
		if (m_animation_type == CSStaticData.ANIMATION_TYPE.LEFT_SHIFT)
		{
			//左移动画
			if (m_animation_index == (int)(m_animation_num * 1.5f))
			{
				// 最后一帧
				m_x_offset = m_final_position[0];
				m_y_offset = m_final_position[1];
				m_z_offset = m_final_position[2];
				
				m_xrot = m_final_angle[0];
				m_yrot = m_final_angle[1];
				m_zrot = m_final_angle[2];
				
				m_animation_index = -1;
				
				m_animation_type = CSStaticData.ANIMATION_TYPE.NONE;
			}
			else if (m_animation_index < m_animation_num)
			{
				// 移动+翻转
				m_x_offset -= m_animation_step[0];
				m_y_offset += m_animation_step[1];
				m_z_offset += m_animation_step[2];
				
				m_yrot -= 90.0f/m_animation_num;
			}
			else
			{
				// 翻转
				m_yrot += 180.0f/m_animation_num;
			}
				
			m_animation_index++;
		}
		else if (m_animation_type == CSStaticData.ANIMATION_TYPE.RIGHT_SHIFT)
		{
			//右移动画
			if (m_animation_index == (int)(m_animation_num * 1.5f) )
			{
				// 最后一帧
				m_x_offset = m_final_position[0];
				m_y_offset = m_final_position[1];
				m_z_offset = m_final_position[2];
				
				m_xrot = m_final_angle[0];
				m_yrot = m_final_angle[1];
				m_zrot = m_final_angle[2];
				
				m_animation_index = -1;
				m_animation_type = CSStaticData.ANIMATION_TYPE.NONE;
			}
			else if (m_animation_index < m_animation_num)
			{
				// 移动+翻转
				m_x_offset += m_animation_step[0];
				m_y_offset -= m_animation_step[1];
				m_z_offset -= m_animation_step[2];
				
				m_yrot += 1;
			}
			else
			{
				// 翻转
				m_yrot -= 2;
			}
				
			m_animation_index++;
		}
		else if (m_animation_type == CSStaticData.ANIMATION_TYPE.GENTLE_SHIFT)
		{
			// 左右轻移动画
			if (m_animation_index >= m_animation_num )
			{
				// 最后一帧
				m_x_offset = m_final_position[0];
				m_y_offset = m_final_position[1];
				m_z_offset = m_final_position[2];
				
				m_xrot = m_final_angle[0];
				m_yrot = m_final_angle[1];
				m_zrot = m_final_angle[2];
				
				m_animation_index = -1;
				m_animation_type = CSStaticData.ANIMATION_TYPE.NONE;
			}
			else if (m_animation_index < m_animation_num)
			{
				// 移动+翻转
				m_x_offset += m_animation_step[0];
				m_y_offset += m_animation_step[1];
				m_z_offset += m_animation_step[2];		
			}
			
			m_animation_index++;
		}
		else
		{
			//~!
		}
	}
	
	
	public void onDraw(GL10 gl, boolean bDraw)  
	{	
		gl.glPushMatrix();
	

		gl.glEnable(GL10.GL_TEXTURE_2D);
		
		gl.glTranslatef(m_x_offset, m_y_offset, m_z_offset);
		gl.glRotatef(m_xrot, 1, 0, 0);
		gl.glRotatef(m_yrot, 0, 1, 0);
		gl.glRotatef(m_zrot, 0, 0, 1);

		int texID = 0;
		if (bDraw)
		{				
			// 绘制阴影
			/*
			if (!CSStaticData.g_is_3D_mode)
			{
				gl.glPushMatrix();
				gl.glDisable(GL10.GL_DEPTH_TEST);
				gl.glTranslatef(0.15f, 0.10f, 0.0f);
				gl.glBindTexture(GL10.GL_TEXTURE_2D, WiGalleryOpenGLRenderer.m_zmap_shade);
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_cover_buff);
			    gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, m_tex_buff);
				gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
				gl.glEnable(GL10.GL_DEPTH_TEST);
				gl.glPopMatrix(); 
			}
			*/
			
			// 绘制图片
			if (m_view_side == MEDIA_VIEW.RIGHT_VIEW && m_type == CSStaticData.MEDIA_TYPE.STOERE_IMAGE)
			{
				// 立体图，并且当前显示右图
				texID = m_cover_right_texture_id;
			}
			else
			{
				texID = m_cover_left_texture_id;
			}
			
			if (texID == CSStaticData.INVALID_TEXTURE_ID)
			{
				if (m_type == CSStaticData.MEDIA_TYPE.NORMAL_VIDEO || m_type == CSStaticData.MEDIA_TYPE.STOERE_VIDEO)
					texID = WiGalleryOpenGLRenderer.m_default_video_icon;
				else
					texID = WiGalleryOpenGLRenderer.m_default_file_icon;
			}
			
			gl.glBindTexture(GL10.GL_TEXTURE_2D, texID);
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_cover_buff);
		    gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, m_tex_buff);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			
	        // 绘制文件类型
			if (m_type == CSStaticData.MEDIA_TYPE.NORMAL_VIDEO || m_type == CSStaticData.MEDIA_TYPE.STOERE_VIDEO)
				m_filetype_texture_id  = WiGalleryOpenGLRenderer.m_zmap_videoindicator_id;
			
	        if (m_filetype_texture_id != CSStaticData.INVALID_TEXTURE_ID 
	        		&& (m_type == CSStaticData.MEDIA_TYPE.NORMAL_VIDEO || m_type == CSStaticData.MEDIA_TYPE.STOERE_VIDEO))
	        {
				gl.glPushMatrix();
				gl.glTranslatef(0, 0, 0.02f);
		        gl.glBindTexture(GL10.GL_TEXTURE_2D, m_filetype_texture_id);
		        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_cover_buff);
		        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, m_tex_buff);
		        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);   
				gl.glPopMatrix();
	        }
	        
	        // 3D标志
	        if (WiGalleryOpenGLRenderer.m_indicator_3d != CSStaticData.INVALID_TEXTURE_ID 
	        		&& (m_type == CSStaticData.MEDIA_TYPE.STOERE_VIDEO || m_type == CSStaticData.MEDIA_TYPE.STOERE_IMAGE))
	        {
	        	gl.glPushMatrix();
	        	gl.glDisable(GL10.GL_DEPTH_TEST);
	        	gl.glTranslatef(0.35f, 0.35f, 0);
		        gl.glBindTexture(GL10.GL_TEXTURE_2D, WiGalleryOpenGLRenderer.m_indicator_3d);
		        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_indicator_buff);
		        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, m_tex_buff);
		        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4); 
		        gl.glEnable(GL10.GL_DEPTH_TEST);
		        gl.glPopMatrix();
	        }
	        
	        if (m_b_favorite)
	        {
	        	gl.glPushMatrix();
	        	gl.glDisable(GL10.GL_DEPTH_TEST);
	        	gl.glTranslatef(0.35f, 0.1f, 0);
		        gl.glBindTexture(GL10.GL_TEXTURE_2D, WiGalleryOpenGLRenderer.m_indicator_favorite);
		        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_indicator_buff);
		        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, m_tex_buff);
		        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);  
		        gl.glEnable(GL10.GL_DEPTH_TEST);
		        gl.glPopMatrix();
	        }
	        
	        // 绘制选择标志
	        if (m_status == ELEM_STATUS.SELECTED_STATUS )
	        {
	        	gl.glPushMatrix();
	        	gl.glDisable(GL10.GL_DEPTH_TEST);
	        	gl.glTranslatef(0.35f, -0.35f, 0);
	        	
	        	if (m_choosed_texture_id == CSStaticData.INVALID_TEXTURE_ID)
	        		m_choosed_texture_id = WiGalleryOpenGLRenderer.m_zmap_select_gallery_select_id;
	        	
	        	if (m_not_choosed_texture_id == CSStaticData.INVALID_TEXTURE_ID)
	        		m_not_choosed_texture_id = WiGalleryOpenGLRenderer.m_zmap_select_gallery_none_id;
	        	
	        	if(m_is_choosed)
	        	{
	        		if (m_choosed_texture_id != CSStaticData.INVALID_TEXTURE_ID)
	        		{
				        gl.glBindTexture(GL10.GL_TEXTURE_2D, m_choosed_texture_id);
				        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_indicator_buff);
				        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, m_tex_buff);
				        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4); 
	        		}
	        	}
	        	else
	        	{
	        		if (m_not_choosed_texture_id != CSStaticData.INVALID_TEXTURE_ID)
	        		{
				        gl.glBindTexture(GL10.GL_TEXTURE_2D, m_not_choosed_texture_id);
				        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_indicator_buff);
				        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, m_tex_buff);
				        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4); 
	        		}
	        	}
	        	gl.glEnable(GL10.GL_DEPTH_TEST);
	        	gl.glPopMatrix();
	        }
	        
	        
	        // 绘制边框
//	        gl.glDisable(GL10.GL_TEXTURE_2D);
//	        gl.glColor4f(0.5f, 0.5f, 0.5f, 0.5f);
//	        gl.glLineWidth(4.0f);
//	        
//	        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_cover_buff);
//	        gl.glDrawElements(GL10.GL_LINE_LOOP, 4,	GL10.GL_UNSIGNED_BYTE, m_line_index_buff);
	        gl.glEnable(GL10.GL_TEXTURE_2D);
	        
		}
		
        gl.glPopMatrix();      
	}
	
	/**
	 * 设置创建纹理所需的位图
	 * @param bmpL
	 * @param bmpR
	 */
	public void setBitmap(Bitmap bmpL, Bitmap bmpR)
	{
		m_bmpR = bmpR;
		m_bmpL = bmpL;	
	}
	
	/**
	 * 从主链表中删除改元素及对应的纹理，如果该元素存在于其它链表中，则需手动删除
	 */
	public void delete()
	{
		WiGalleryOpenGLRenderer.m_data_manager.AddNeedDeleteElement(this);
	}
	
	public void loadTexture(GL10 gl)
	{	
		switch(m_type)
		{
		case STOERE_IMAGE:
		case STOERE_VIDEO:	
			{
				if (m_bmpL != null)
					m_cover_left_texture_id = WiGalleryOpenGLRenderer.m_resource_manager.GetTextureID(m_bmpL);
				else
					m_cover_left_texture_id = CSStaticData.INVALID_TEXTURE_ID;
				
				if (m_bmpR != null)
					m_cover_right_texture_id = WiGalleryOpenGLRenderer.m_resource_manager.GetTextureID(m_bmpR);
				else
					m_cover_right_texture_id = CSStaticData.INVALID_TEXTURE_ID;
				
				// 如果不在创建一个纹理，texR就不起作用，临时这样处理一下，在设置完后删除该纹理
				int id = WiGalleryOpenGLRenderer.m_resource_manager.GetTextureID(m_bmpR);

				
				// 纹理加载完成
					m_is_load_texture = true;
				
				WiGalleryOpenGLRenderer.m_resource_manager.DeleteTextureID(id);
				
				
				if (m_bmpL != null)
				{
					m_bmpL.recycle();
					m_bmpL = null;
				}
				if (m_bmpR != null)
				{
					m_bmpR.recycle();
					m_bmpR = null;
				}
				
			}
			break;
		case NORMAL_IMAGE:
		case NORMAL_VIDEO:
			{
				if (m_bmpL != null)
					m_cover_left_texture_id = WiGalleryOpenGLRenderer.m_resource_manager.GetTextureID(m_bmpL);
				else
					m_cover_left_texture_id = CSStaticData.INVALID_TEXTURE_ID;
				
				// 如果不在创建一个纹理，texR就不起作用，临时这样处理一下，在设置完后删除该纹理
				int id = WiGalleryOpenGLRenderer.m_resource_manager.GetTextureID(m_bmpL);

				
				// 纹理加载完成
					m_is_load_texture = true;
				
				WiGalleryOpenGLRenderer.m_resource_manager.DeleteTextureID(id);
				
				
				if (m_bmpL != null)
				{
					m_bmpL.recycle();
					m_bmpL = null;
				}
				
			}
			break;
		default:
			break;
		}
		
		m_b_request = false;
		
		if (CSStaticData.DEBUG)
			Log.d("Element", String.format("[%s][%d][%d]", m_str_name, m_cover_left_texture_id, m_cover_right_texture_id));

	}
	
	
	public boolean onTouchEvent(float x, float y)
	{
		if(CSStaticData.isInRect(m_x_offset, m_y_offset, m_z_offset, x, y, 0.5f))
		{
			if (m_status == ELEM_STATUS.NORMAL_STATUS)
			{
				// 打开文件
			
				if (WiGalleryInterface.m_onGlItemListener != null)
					WiGalleryInterface.m_onGlItemListener.onFileClick(getName());
				if (CSStaticData.DEBUG)
					Log.d("Element", String.format("[OpenFile][%s]", getName()));
			}
			else if (m_status == ELEM_STATUS.SELECTED_STATUS)
			{
				// 选择模式
				m_is_choosed = !m_is_choosed;	
				
				if(WiGalleryInterface.m_onGlItemListener != null){
					WiGalleryInterface.m_onGlItemListener.onFileClick(m_str_name);
				}
			}
			else
			{
				
			}
			return true;
		}
		else
		{
			return false;
		}
	}
}


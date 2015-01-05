// Copyright (c) 2011 Wistron SWPC
// All rights reserved.
//	created:	2011/09/28
//	filename: 	ResourceManager.java
//	author:		Neo Yeoh
//	purpose:	
 
package com.wistron.WiGallery;

import com.wistron.WiGallery.Element;

import Utilities.CSStaticData;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLUtils;
import android.util.Log;


public class ResourceManager
{
	private int m_bm_width = 256;
	private int m_bm_height = 32;
	
	static private  GL10 m_gl = null;
	private  Canvas m_canves = null;
	private  Paint m_paint = null;
	
	private  int[] m_texture_id = new int[2];
	
	private List<Integer>   m_need_delete_textureid_list = null; //请求文件列表

	
	public  ResourceManager()
	{
		m_need_delete_textureid_list =  new ArrayList<Integer>();
    	m_paint = new Paint( Paint.ANTI_ALIAS_FLAG);   
	}
	
	public  void SetGL10(GL10 gl)
	{
		m_gl = gl;
	}
	
	public void AddNeedDeleteTextureID(int id)
	{
		if (id != CSStaticData.INVALID_TEXTURE_ID)
		{
			synchronized(m_need_delete_textureid_list)
			{
				m_need_delete_textureid_list.add(id);
			}
		}
	}

	/**
	 * 删除纹理ID，该函数必须在ondraw调用
	 */
	public void DeleteNeedDeleteTextureID()
	{
		synchronized(m_need_delete_textureid_list)
		{
	        Iterator<Integer> it = m_need_delete_textureid_list.iterator();   
	        while (it.hasNext())    
	        {   
	        	int id = it.next();
	        	if (id != CSStaticData.INVALID_TEXTURE_ID)
	        	{
	        		DeleteTextureID(id);
	        		it.remove();
	        	}
	        }
		}
	}

	
	public void DeleteTextureID(Element elem)
	{
		if (elem == null)
			return;
		
		if (CSStaticData.DEBUG)
			Log.i("Resource", "Delete [" + elem.getName() + "] Texture");
		
		int[] texID = new int[2];
		
		switch(elem.getFileType())
		{
			case STOERE_IMAGE:
			case STOERE_VIDEO:
				{
					if ( elem.getLeftTextureID() !=  CSStaticData.INVALID_TEXTURE_ID)
					{
						texID[0] = elem.getLeftTextureID();
						m_gl.glDeleteTextures(1, makeIntBuffer(texID));
					}
					
					if (  elem.getRightTextureID() !=  CSStaticData.INVALID_TEXTURE_ID)
					{
						texID[0] = elem.getRightTextureID();
						m_gl.glDeleteTextures(1, makeIntBuffer(texID));
					}
					
					elem.setTextureID(CSStaticData.INVALID_TEXTURE_ID, CSStaticData.INVALID_TEXTURE_ID);
					elem.m_is_load_texture = false;
					elem.setRequest(false);
				}
				break;
			case NORMAL_IMAGE:
			case NORMAL_VIDEO:
				{
					if (elem.getLeftTextureID() != CSStaticData.INVALID_TEXTURE_ID)
					{
						texID[0] = elem.getLeftTextureID();
						m_gl.glDeleteTextures(1, makeIntBuffer(texID));
					}
					
					elem.setTextureID(CSStaticData.INVALID_TEXTURE_ID, CSStaticData.INVALID_TEXTURE_ID);
					elem.m_is_load_texture = false;
					elem.setRequest(false);
				}
				break;
			default:
				break;
		}
	}
	
	//重置为缺省贴图ID [For Black Block]
	public void ReinitTextureID(Element elem)
	{
		if (elem == null)
			return;
		
		if (CSStaticData.DEBUG)
			Log.i("Resource", "Delete [" + elem.getName() + "] Texture");
		
		int[] texID = new int[2];
		
		switch(elem.getFileType())
		{
			case STOERE_IMAGE:
			case STOERE_VIDEO:
				{
//					if ( elem.getLeftTextureID() !=  CSStaticData.INVALID_TEXTURE_ID)
//					{
//						texID[0] = elem.getLeftTextureID();
//						m_gl.glDeleteTextures(1, makeIntBuffer(texID));
//					}
//					
//					if (  elem.getRightTextureID() !=  CSStaticData.INVALID_TEXTURE_ID)
//					{
//						texID[0] = elem.getRightTextureID();
//						m_gl.glDeleteTextures(1, makeIntBuffer(texID));
//					}
					
					elem.setTextureID(CSStaticData.INVALID_TEXTURE_ID, CSStaticData.INVALID_TEXTURE_ID);
					elem.m_is_load_texture = false;
					elem.setRequest(false);
				}
				break;
			case NORMAL_IMAGE:
			case NORMAL_VIDEO:
				{
//					if (elem.getLeftTextureID() != CSStaticData.INVALID_TEXTURE_ID)
//					{
//						texID[0] = elem.getLeftTextureID();
//						m_gl.glDeleteTextures(1, makeIntBuffer(texID));
//					}
					
					elem.setTextureID(CSStaticData.INVALID_TEXTURE_ID, CSStaticData.INVALID_TEXTURE_ID);
					elem.m_is_load_texture = false;
					elem.setRequest(false);
				}
				break;
			default:
				break;
		}
	}
	
	public void DeleteTextureID(int id)
	{
		if (id <= CSStaticData.INVALID_TEXTURE_ID)
			return;
		
		int[] texID = new int[2];
		texID[0] = id;
		
		m_gl.glDeleteTextures(1, makeIntBuffer(texID));
		
		id = CSStaticData.INVALID_TEXTURE_ID;
	}
	
	public  int GetTextureID(Bitmap bitmap)
	{
		if(bitmap == null || bitmap.isRecycled())
		{
			return 0;
		}
						
        // 生成纹理
		m_gl.glGenTextures(1, m_texture_id, 0);
		m_gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		m_gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		m_gl.glBindTexture(GL10.GL_TEXTURE_2D, m_texture_id[0]);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		
		////////////////////////////////////////////////////////////////////
		// 零时处理  
		
		int[] texID = new int[1];
        m_gl.glGenTextures(1, texID, 0);
        m_gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        m_gl.glBindTexture(GL10.GL_TEXTURE_2D, texID[0]);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        DeleteTextureID(texID[0]);

        //////////////////////////////////////////////////////////////
        
		return m_texture_id[0];
	}
	
	public int GetTextureID(int textID, Bitmap bm, String string, int fontSize, CSStaticData.ALIGN_TYPE alignType, boolean bShadow)
	{
		if (textID != CSStaticData.INVALID_TEXTURE_ID)
		{
			DeleteTextureID(textID);
		}
		
		return GetTextureID(bm, string, fontSize, alignType, bShadow);
	}

	
	private  int GetTextureID(Bitmap bm, String string, int fontSize, CSStaticData.ALIGN_TYPE alignType, boolean bShadow)
	{
		if (bm == null || string == null) 
			return CSStaticData.INVALID_TEXTURE_ID;
		else
		{
			int n = string.lastIndexOf("/");
			
			if (n >= 0)
			{
				string = string.substring(n+1, string.length());
			}
			
			//////////////////////////////////
			if (m_canves == null)
				m_canves = new Canvas(bm);
			else
				m_canves.setBitmap(bm);
	    	
	        m_paint.setTextSize(fontSize); 
	        
	        // 绘制文字
	        DrawBitmap(bm, string, alignType, bShadow);
	        
	        // 生成纹理
	        m_gl.glGenTextures(1, m_texture_id, 0);
	        m_gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
	        m_gl.glBindTexture(GL10.GL_TEXTURE_2D, m_texture_id[0]);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bm, 0);
	        
			////////////////////////////////////////////////////////////////////
			// 零时处理  
			
			int[] texID = new int[1];
	        m_gl.glGenTextures(1, texID, 0);
	        m_gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
	        m_gl.glBindTexture(GL10.GL_TEXTURE_2D, texID[0]);
	        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bm, 0);
	        DeleteTextureID(texID[0]);
	        
	        //////////////////////////////////////////////////////////////
	        
	        return m_texture_id[0];
		}
	}
	
	private  void DrawBitmap(Bitmap bm, String string, CSStaticData.ALIGN_TYPE alignType, boolean bShadow)
	{	
		if (bm == null)
			return;
		
		// 设置画笔颜色
		m_paint.setColor(Color.WHITE);	
		if (bShadow)
			m_paint.setShadowLayer(1, 2, 2, Color.BLACK);
		else
			m_paint.setShadowLayer(0, 0, 0, Color.BLACK);
		//m_paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		//m_paint.setAntiAlias(true);

    	int nChar = string.getBytes().length;
    	
    	float[] widths = new float[nChar+1];  
    	m_paint.getTextWidths(string, widths);
    	Paint.FontMetrics fm = m_paint.getFontMetrics();
    	
    	// 计算显示字符宽度
    	int totalWidth = 0;
    	for (int i = 0; i < nChar+1; i++)
    	{
    		totalWidth += widths[i];
    	}
    	
    	float xPos = 0;
    	float yPos = (bm.getHeight() - fm.ascent) / 2;

    	if (alignType == CSStaticData.ALIGN_TYPE.ALIGN_LEFT)
    	{
    		xPos = 0;
    	}
    	else if (alignType == CSStaticData.ALIGN_TYPE.ALIGN_CENTRE)
    	{
    		xPos = (bm.getWidth() - totalWidth ) / 2.0f;
    	}
    	else
    	{
    		xPos = bm.getWidth() - totalWidth;
    	}
    	
    	m_canves.drawText(string, xPos, yPos, m_paint);	
  	
	}
	
	public static FloatBuffer makeFloatBuffer(float[] arr) 
	{
		ByteBuffer bb = ByteBuffer.allocateDirect(arr.length*4);
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer fb = bb.asFloatBuffer();
		fb.put(arr);
		fb.position(0);
		return fb;
	}
	
	public static ShortBuffer makeShortBuffer(short[] arr)
	{
        ByteBuffer ib = ByteBuffer.allocateDirect(arr.length * 2);  
        ib.order(ByteOrder.nativeOrder());  
        ShortBuffer sb = ib.asShortBuffer();  
        sb.put(arr);  
        sb.position(0);  
        return sb;  
	}
	
	 public static IntBuffer makeIntBuffer(int[]a)  
	 {  
	  //先初始化buffer,数组的长度*4,因为一个int占4个字节  
	        ByteBuffer mbb=ByteBuffer.allocateDirect(a.length*4);  
	        //数组排列用nativeOrder  
	        mbb.order(ByteOrder.nativeOrder());  
	        IntBuffer intBuffer = mbb.asIntBuffer();  
	        intBuffer.put(a);  
	        intBuffer.position(0);  
	        return intBuffer;  
	 }
	 
	 public static ByteBuffer makeByteBuffer(Byte[]a)  
	 {  
	  //先初始化buffer,数组的长度*4,因为一个int占4个字节  
	        ByteBuffer mbb=ByteBuffer.allocateDirect(a.length);  
	        //数组排列用nativeOrder  
	        mbb.order(ByteOrder.nativeOrder());  
	        
	        for (int i = 0; i < a.length; i++)
	        mbb.put(a[i]);
	        
	        mbb.position(0);
	        return mbb;  
	 }
	 
	public static float fabs(float val) 
	{
		if (val < 0)
			val *= -1;
		
		return val;
	}
}
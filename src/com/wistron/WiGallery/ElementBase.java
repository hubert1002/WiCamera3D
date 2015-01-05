package com.wistron.WiGallery;

import Utilities.CSStaticData;

public class ElementBase 
{
	protected float m_xrot = 0.0f;
	protected float m_yrot = 0.0f;
	protected float m_zrot = 0.0f;
	
	protected float m_x_offset = 0.0f;
	protected float m_y_offset = 0.0f;
	protected float m_z_offset = 0.0f;
	
	
	protected MEDIA_VIEW m_view_side = MEDIA_VIEW.LEFT_VIEW;
	// 动画设置
	public CSStaticData.ANIMATION_TYPE m_animation_type = CSStaticData.ANIMATION_TYPE.NONE;
	protected float[]  m_animation_step = new float[3]; // 动画移动步长
	protected int m_animation_num = 0; // 动画帧数
	protected float[] m_final_position = new float[3]; // 动画完成后的位置
	protected float[] m_final_angle = new float[3]; // 动画完成后的角度
	protected int m_animation_index = 0;
	/////////////////////////////////////////////////
	
	protected boolean m_is_choosed = false;
	protected ELEM_STATUS m_status = ELEM_STATUS.NORMAL_STATUS;
	
	// 获取Element的显示位置
	public float[] getPosition()
	{
		float[] val = new float[3];
		val[0] = m_x_offset;
		val[1] = m_y_offset;
		val[2] = m_z_offset;
		return val;
	}
	
	public float[] getAngle()
	{
		float[] val = new float[3];
		val[0] = m_xrot;
		val[1] = m_yrot;
		val[2] = m_zrot;
		return val;
	}
	
	public void setStatus(ELEM_STATUS status)
	{
		m_status = status;
		if (status == ELEM_STATUS.NORMAL_STATUS || status == ELEM_STATUS.DISABLE_STATUS)
		{
			setChoosed(false);
		}
	}
	
	public ELEM_STATUS getStatus()
	{
		return m_status;
	}
	
	public void setChoosed(boolean isChoosed)
	{	
		m_is_choosed = isChoosed;
	}
	
	
	public boolean isChoosed()
	{
		return m_is_choosed;
	}
	
	public void moveTo(float x, float y, float z)
	{
		m_x_offset = x;
		m_y_offset = y;
		m_z_offset = z;
	}
	
	public void setAngleTo(float xRot, float yRot, float zRot)
	{
		m_xrot = xRot;
		m_yrot = yRot;
		m_zrot = zRot;
	}
	
	public void setAngle(float xRot, float yRot, float zRot)
	{
		m_xrot += xRot;
		m_yrot += yRot;
		m_zrot += zRot;
	}
	
	public void move(float x, float y, float z)
	{
		m_x_offset += x;
		m_y_offset += y;
		m_z_offset += z;	
	}
	
	public void startAnimation(CSStaticData.ANIMATION_TYPE type, int num, float[] step, float[] finalPositon, float[] finalAngle)
	{
		m_animation_type = type;
		m_animation_num = num;
		m_animation_index = 0;
		for (int i = 0; i < 3; i++)
		{
			m_animation_step[i] = step[i];
			m_final_position[i] = finalPositon[i];
			m_final_angle[i] = finalAngle[i];
		}
	}
	
	public CSStaticData.ANIMATION_TYPE getAnimationType()
	{
		return m_animation_type;
	}
}

package com.wistron.WiCamera;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 
 * @Copyright (c) 2011 Wistron SWPC All rights reserved.
 * 
 * @created: 2011/09/26
 * @filename: StoredData.java
 * @author WH1107063(周海江)
 * @purpose 储存数据到xml文件的类
 * 
 */
public class StoredData {

	/**
	 * 照相录像模块的变量
	 */
	private static SharedPreferences sharedPreferences;
	public static final String M_NAME = "WiCamera3D2";
	// 拍照的风景模式
	public static final String M_SCENEMODE = "m_scenemode";
	// 照片的大小
	public static final String M_PICTURESIZE = "m_pictureSize";
	// 闪光灯模式
	public static final String M_FLASHMODE = "m_flashmode";
	// 拍照时的声音
	public static final String M_SOUNDMODE = "m_soundmode";
	// sdcard插入
	public static final String M_SDCARDMOUNTED = "m_sdcardmounted";
	// sdcard拔出
	public static final String M_SDCARDUNMOUNTED = "m_sdcardunmounted";
	// 照片的质量
	public static final String M_JPEGQUALITY = "m_jpegquality";
	// 是否是3D
	public static final String M_CAMERA3D = "m_camera3D";
	// 文件的存储方式
	public static final String M_STORAGEMODE = "m_storagemode";
	// 文件的存储方式
	public static final String M_RECFORMAT = "m_Recformat";
	// 照片的存储格式
	public static final String M_PICFORMAT = "m_Recformat";
	// 是否把gps写入图片
	// public static final String M_ISGPSON = "m_isgpson";
	// 设置曝光度
	public static final String M_EXPOSURE = "M_EXPOSURE";
	// 笑脸识别
	public static final String M_SMILESHOT = "M_Smileshot";
	// 人脸识别
	public static final String M_FACETRACKING = "M_Facetracking";
	// 自定义拍照时间
	public static final String M_SELFTIMER = "M_Selftimer";
	// 触摸对焦
	public static final String M_TOUCHFOCUS = "M_Touchfocus";
	// 显示在viewfinder中
	public static final String M_GRIDDISINFINDER = "M_Griddisinviewfinder";
	// 是否去红眼
//	public static final String M_REDEYEREMOVAL = "M_Redeyeremoval";
	// 自动旋转
	public static final String M_AUTOROTATE = "M_AutoRotate";
	// 是否跳到galler中
	public static final String M_GOTOGALLERY = "M_gotogallery";
	// 是否添加gps信息
	public static final String M_ADDTAG = "M_addtag";
	// 是否预览
	public static final String M_REVIEW = "M_review";
	// hjr
	public static final String M_HJR = "M_HJR";
	// 白平衡
	public static final String M_WHITEBALANCE = "M_WhiteBalance";
	// 照相类型
	public static final String M_CAMERA_STATE = "M_CAMERA_STATE";

	// 连拍的张数
	public static final String M_CONTINUESHOTNUM = "m_continueshot";
	// Video resolution
	public static final String M_VIDEORESOLUTION = "m_videoresolution";
	// 上次的review的图片的路径
	public static final String M_REVIEWFILEPATH = "M_REVIEWFILEPATH";
	// 是否允许控件随屏幕旋转转动
	public static final String m_GSENSOR = "GSENSOR";
	// iso
	public static final String M_ISO = "M_ISO";
	// 定时连拍的按钮背景
	public static final String M_CAMERA_CONTINUOUSBG = "M_CAMERA_CONTINUOUSBG";
	public static final String M_CAMERA_SELF_TIMERBG = "M_CAMERA_self_timerbg";
	public static final String M_VIDEO_SELF_TIMERBG = "M_VIDEO_SELF_TIMERBG";
	public static final String M_VIDEO_SELFTIMER = "M_VIDEO_selftimer";

	/**
	 * 初始化SharedPreferences
	 * 
	 * @param context
	 */
	public static void init(Context context) {
		if (sharedPreferences == null)
			sharedPreferences = context.getSharedPreferences(M_NAME,
					context.MODE_WORLD_READABLE);
	}

	/**
	 * 保存一个需要保存的String类型的值
	 * 
	 * @param key
	 * @param value
	 */
	public static void saveString(String key, String value) {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * 获取指定Key的String类型的值
	 * 
	 * @param key
	 * @return
	 */
	public static String getString(String key, String value) {
		return sharedPreferences.getString(key, value);
	}

	/**
	 * 保存一个需要保存的integer类型的值
	 * 
	 * @param key
	 * @param value
	 */
	public static void saveInt(String key, int value) // 保存
	{
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	/**
	 * 获取指定Key的int类型的值
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static int getInt(String key, int defaultValue) // 获取数据
	{
		return sharedPreferences.getInt(key, defaultValue);
	}

	/**
	 * 保存一个需要保存的long类型的值
	 * 
	 * @param key
	 * @param value
	 */
	public static void saveLong(String key, Long value) // 保存
	{
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	/**
	 * 获取指定Key的long类型的值
	 * 
	 * @param key
	 * @param value
	 */
	public static Long getLong(String key, Long defaultValue) // 保存
	{
		return sharedPreferences.getLong(key, defaultValue);
	}

	/**
	 * 保存一个需要保存的boolean类型的值
	 * 
	 * @param key
	 * @param value
	 */
	public static void saveBoolean(String key, Boolean value) // 保存
	{
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * 获取指定Key的boolean类型的值
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static boolean getBoolean(String key, Boolean defaultValue) // 获取数据
	{
		return sharedPreferences.getBoolean(key, defaultValue);
	}

	/**
	 * 保存一个需要保存的Float类型的值
	 * 
	 * @param key
	 * @param value
	 */
	public static void saveFloat(String key, float value) // 保存
	{
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putFloat(key, value);
		editor.commit();
	}

	/**
	 * 获取指定Key的Float类型的值
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static float getFloat(String key, Float defaultValue) // 获取数据
	{
		return sharedPreferences.getFloat(key, defaultValue);
	}
}

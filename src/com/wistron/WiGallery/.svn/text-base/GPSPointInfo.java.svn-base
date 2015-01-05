package com.wistron.WiGallery;

/**
 * Copyright (c) 2012 Wistron SWPC
 * All rights reserved.
 * @author 
 * @date 2012-03-08 16:08:42
 * @comment 
 * @purpose 计算经纬度映射的球面坐标以及法线向量
 * @detail 
 */
public class GPSPointInfo{
	
	private float[] mPoint  = new float[3];
	private float[] mUpDirect = new float[3];
	private float   mXTheata = 0f,
			        mYTheata = 0f,
			        mZTheata = 0f;
	
	public GPSPointInfo(double latitude, double longitude, float radio, float[] center){
		double  DTOR   = (Math.PI / 180f);
		double  lon    = 0f,
				lat    = 0f;

		lat = - latitude;
		lon = - longitude;
		lat += 90;
		lon += 360;
		
		lat = lat * DTOR;
		lon = lon * DTOR;
		
		mPoint[0] = (float) (radio * Math.cos(lon) * Math.sin(lat)) + center[0];
		mPoint[1] = (float) (radio * Math.cos(lat)) + center[1];
		mPoint[2] = (float) (radio * Math.sin(lon) * Math.sin(lat)) + center[2];
		
		mUpDirect[0] = mPoint[0] - center[0];
		mUpDirect[1] = mPoint[1] - center[1];
		mUpDirect[2] = mPoint[2] - center[2];
		
		mXTheata = -(float) (latitude + 90);
		mYTheata = (float) (longitude + 90);
		mZTheata = 0;
		
	}
	
	public float[] getGPSPoint(){
		return mPoint;
	}
	
	public float[] getGPSUpDirect(){
		return mUpDirect;
	}
	
	public float getXTheata(){
		return mXTheata;
	}
	
	public float getYTheata(){
		return mYTheata;
	}

	public float getZTheata(){
		return mZTheata;
	}
}
package com.wistron.WiGallery;

import android.R.integer;
import android.media.ExifInterface;

public class NewtonTheory {

	private static final String TAG  = "NewtonTheory";
	private static float ACC         = 1.05f;   //加速度阻尼
	private static float ACC_Limit   = 0.1f;    //加速阻尼下限
	private static float SCALE_Limit = 0.05f;   //缩放逼近下限
	private static float SCALE_Step  = 0.2f;    //缩放平滑度，越小越平滑
	private static float DECELOR     = 150.0f;  //缓冲因子

	private float   mXVelocity       = 0f;
	private float   mYVelocity       = 0f;
	private float   mZVelocity       = 0f;
	
	private float   mSmoothScaleFrom = 1f;
	private float   mSmoothScaleTo   = 1f;
	private float   mSmoothScaleCur  = 1f;
	private float   mSmoothScaleX    = 1f;
	private float   mSmoothScaleMin  = 1f;
	private float   mSmoothScaleMax  = 1f;


	public void setStartVelocity(float velocity, int axis){
		switch (axis) {
		case 0:
			mXVelocity  = velocity/DECELOR;
			break;

		case 1:
			mYVelocity  = velocity/DECELOR;
			break;

		case 2:
			mZVelocity  = velocity/DECELOR;
			break;

		default:
			break;
		}
	}
	
	
	/**
	 * 阻尼器
	 * @param velocity
	 * @param isStart  为初始速度
	 * @return
	 */
	public float getDecelerator(int axis){
		float delta = 0f;
		
		switch (axis) {
		case 0:
			delta = mXVelocity/ACC;
			if(Math.abs(delta) < ACC_Limit){
				delta = 0f;
			}
			mXVelocity = delta;
			break;

		case 1:
			delta = mYVelocity/ACC;
			if(Math.abs(delta) < ACC_Limit){
				delta = 0f;
			}
			mYVelocity = delta;
			break;

		case 2:
			delta = mZVelocity/ACC;
			if(Math.abs(delta) < ACC_Limit){
				delta = 0f;
			}
			mZVelocity = delta;
			break;

		default:
			break;
		}
		
		return delta;
	}
	
	public void setSmoothScale(float fromScale, float toScale){
		mSmoothScaleFrom = fromScale;
		mSmoothScaleTo   = toScale;
		mSmoothScaleCur  = fromScale;
	}
	
	public void setScaleLimit(float minScale, float maxScale){
		mSmoothScaleMin = minScale;
		mSmoothScaleMax = maxScale;
	}
	
	/**
	 * 获取平滑缩放的缩放比，采用对数逼近
	 * @param output 输出的缩放比
	 * @return true = 缩放比有变化（或正在缩放）， false = 缩放比无变化（静止状态）
	 */
	public boolean getSmoothScaleLog(float[] output){
		if(mSmoothScaleCur == mSmoothScaleTo || Math.abs(mSmoothScaleTo - mSmoothScaleCur) < SCALE_Limit){
			mSmoothScaleX = 1f;
			return false;
		}
		
		if(mSmoothScaleCur == mSmoothScaleFrom){
			mSmoothScaleX = 1f; //x值恢复至起点
		}
		
		mSmoothScaleX  += SCALE_Step;
		mSmoothScaleCur = (float) Math.log(mSmoothScaleX)*(mSmoothScaleTo - mSmoothScaleFrom);
		mSmoothScaleCur = mSmoothScaleFrom + mSmoothScaleCur;
		if(output == null || output.length < 1){
			output = new float[1];
		}
		
		if(mSmoothScaleCur - mSmoothScaleMin <= 0){
			mSmoothScaleCur = mSmoothScaleMin;
		}
		if(mSmoothScaleCur - mSmoothScaleMax >= 0){
			mSmoothScaleCur = mSmoothScaleMax;
		}
		output[0] = mSmoothScaleCur;
		
		return true;
	}
	
	/**
	 * 获取平滑缩放的缩放比，采用正弦逼近
	 * @param output 输出的缩放比
	 * @return true = 缩放比有变化（或正在缩放）， false = 缩放比无变化（静止状态）
	 */
	public boolean getSmoothScaleSin(float[] output){
		if(mSmoothScaleCur == mSmoothScaleTo || Math.abs(mSmoothScaleTo - mSmoothScaleCur) < SCALE_Limit){
			mSmoothScaleX = 0f;
			return false;
		}
		
		if(mSmoothScaleCur == mSmoothScaleFrom){
			mSmoothScaleX = 0f; //x值恢复至起点
		}
		
		mSmoothScaleX  += SCALE_Step;
		mSmoothScaleCur = (float) Math.sin(mSmoothScaleX)*(mSmoothScaleTo - mSmoothScaleFrom);
		mSmoothScaleCur = mSmoothScaleFrom + mSmoothScaleCur;
		if(output == null || output.length < 1){
			output = new float[1];
		}
		
		if(mSmoothScaleCur - mSmoothScaleMin <= 0){
			mSmoothScaleCur = mSmoothScaleMin;
		}
		if(mSmoothScaleCur - mSmoothScaleMax >= 0){
			mSmoothScaleCur = mSmoothScaleMax;
		}
		output[0] = mSmoothScaleCur;
		
		return true;
	}

	/**
	 * 计算两个图标是否重叠
	 * @param latA       图标A纬度
	 * @param longA      图标A经度
	 * @param latB       图标B纬度
	 * @param longB      图标B经度
	 * @param iconRadio  图标直径
	 * @param earthRadio 地球直径
	 * @return
	 */
	public static boolean isOverlaped(double latA, double longA, double latB, double longB, float iconRadio, float earthRadio){
		boolean result   = false;
		double  distance = 0;
		
		if(Double.isNaN(latA) || Double.isNaN(latB) || Double.isNaN(longA) || Double.isNaN(longB)){
			return false;
		}
		
		distance = Math.acos(Math.pow(earthRadio, 2) * Math.abs(latA - latB) * Math.abs(longA - longB));
		if(iconRadio - distance > 0){
			result = true;
		}else{
			result = false;
		}
		
		return result;
	}
}

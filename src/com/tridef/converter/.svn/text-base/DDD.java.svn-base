package com.tridef.converter;

import Utilities.CSStaticData;
import Utilities.FileOperation;
import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.util.Log;
import android.widget.Toast;

public class DDD {
	
	private int  mPicWidth    = 0;
	private int  mPicHeight   = 0;
	private int  mSceneDepth  = 255;
	private long mProcessTime = 0;
	
	public DDD(){
		super();
	}
	
	/**
	 * 设置景深，取值 0~255，若不设置，则为默认值
	 * @param depth 
	 */
	public void setSceneDepth(int depth){
		mSceneDepth = depth;
	}
	
	/**
	 * 设置图片的大小
	 * @param width
	 * @param height
	 */
	public void setBitmapSize(int width, int height){
		mPicWidth  = width;
		mPicHeight = height;
	}
	
	/**
	 * 生成3D图片
	 * @param normalBmp 普通原始图片
	 * @return
	 */
	public Bitmap genrenteTriDefBitmap(Bitmap normalBmp, boolean keepAspect){
		int[]    pixels    = null;
		int[]    triPixels = null;
		int      width     = 0,
				 height    = 0;
		Config   config    = null;
		Bitmap   result    = null;
		
		mProcessTime = 0;
		long start = System.currentTimeMillis();
		
		if(normalBmp == null){
			return null;
		}
		
		if(mSceneDepth >= 0){
			NativeTest.setSceneDepth(mSceneDepth);
		}
		config    = normalBmp.getConfig();
		int tempw =normalBmp.getWidth();
		int temph=normalBmp.getHeight();
		if(CSStaticData.DEBUG)
		Log.e("DDD", "处理的图片长宽为"+tempw+"*"+temph);
		if(normalBmp.getWidth()%2!=0)
		{
			tempw=normalBmp.getWidth()+1;
		}
		if(normalBmp.getHeight()%2!=0)
		{
			temph=normalBmp.getHeight()+1;
		}
		Bitmap temp = scaleBitmap(normalBmp, tempw, temph);
		width     = temp.getWidth();
		height    = temp.getHeight();
		pixels    = new int[width * height];
		triPixels = new int[width * height];
		temp.getPixels(pixels, 0, width, 0, 0, width, height);
		NativeTest.processRGB(pixels, width, height, triPixels);
		if(temp!=normalBmp)
		{
			//产生了新图
			temp.recycle();
		}
		if(config==null)
		{
			config=Config.RGB_565;
		}
		result   = Bitmap.createBitmap(triPixels, width, height, config); //创建出Side-by-Side的Bitmap
		if(keepAspect){
			Bitmap bmp = result;
			result = scaleBitmap(bmp, width * 2, height);
			bmp.recycle();
		}
		
		long end = System.currentTimeMillis();
		mProcessTime = end - start;
		
		return result;
	}
	
	
	/**
	 * 生成3D图片,左右图分离
	 * @param normalBmp 原始图片
	 * @param keepAspect 保持原图比例
	 * @return
	 */
	public Bitmap[] genrenteTriDefSplitBitmap(Bitmap normalBmp, boolean keepAspect){
		int[]    pixels    = null;
		int[]    triPixels = null;
		int      width     = 0,
				 height    = 0;
		Config   config    = null;
		Bitmap   process   = null;
		Bitmap[] result    = new Bitmap[2];
		
		mProcessTime = 0;
		long start = System.currentTimeMillis();
		
		if(normalBmp == null){
			return result;
		}
		
		if(mSceneDepth >= 0){
			NativeTest.setSceneDepth(mSceneDepth);
		}
		config    = normalBmp.getConfig();
		normalBmp = scaleBitmap(normalBmp, mPicWidth, mPicHeight);
		width     = normalBmp.getWidth();
		height    = normalBmp.getHeight();
		pixels    = new int[width * height];
		triPixels = new int[width * height];
		normalBmp.getPixels(pixels, 0, width, 0, 0, width, height);
		NativeTest.processRGB(pixels, width, height, triPixels);
		
		process   = Bitmap.createBitmap(triPixels, width, height, config); //创建出Side-by-Side的Bitmap
		result[0] = Bitmap.createBitmap(process, process.getWidth()/2, 0, process.getWidth()/2, process.getHeight()); //分离出左图
		result[1] = Bitmap.createBitmap(process, 0, 0, process.getWidth()/2, process.getHeight()); //分离出右图
		
		if(keepAspect){
			Bitmap bmp = null;
			bmp = result[0];
			result[0] = scaleBitmap(bmp, width, height);
			bmp.recycle();
			bmp = result[1];
			result[1] = scaleBitmap(bmp, width, height);
			bmp.recycle();
		}
		
//		FileOperation.saveBitmapOnFS(process, "/mnt/sdcard/DDD/DDDtrans/DDDImage_" + System.currentTimeMillis() + ".jpg", true);
		
		long end = System.currentTimeMillis();
		mProcessTime = end - start;
		
		return result;
	}
	
	/**
	 * 获取执行时间
	 * @return
	 */
	public long getProcessdTime(){
		return mProcessTime;
	}
	
	private Bitmap scaleBitmap(Bitmap bmp, int w, int h) {
		int    width  = 0;
		int    height = 0;
		
		if(bmp == null){
			return null;
		}
		
		width = bmp.getWidth();
		height = bmp.getHeight();
		
		if(w == width && h == height){
			return bmp;
		}
		if(w <= 0 || h <= 0){
			return bmp;
		}
		
		Bitmap res= Bitmap.createScaledBitmap(bmp, w, h, true);
//		if(res!=bmp)
//		{
//			bmp.recycle();
//		}
		return res;
	}
}

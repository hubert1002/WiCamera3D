package Utilities;

import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

public class DDD {
	
	private int  mPicWidth    = 0;
	private int  mPicHeight   = 0;
	private int  mSceneDepth  = -1;
	private long mProcessTime = 0;
	
	public DDD(){
		
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
	public Bitmap genrenteTriDefBitmap(Bitmap normalBmp){
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
		normalBmp = scaleBitmap(normalBmp, mPicWidth, mPicHeight);
		width     = normalBmp.getWidth();
		height    = normalBmp.getHeight();
		pixels    = new int[width * height];
		triPixels = new int[width * height];
		normalBmp.getPixels(pixels, 0, width, 0, 0, width, height);
		NativeTest.processRGB(pixels, width, height, triPixels);
		
		result   = Bitmap.createBitmap(triPixels, width, height, config); //创建出Side-by-Side的Bitmap
		
		long end = System.currentTimeMillis();
		mProcessTime = end - start;
		
		return result;
	}
	
	
	/**
	 * 生成3D图片,左右图分离
	 * @param normalBmp 普通原始图片
	 * @return
	 */
	public Bitmap[] genrenteTriDefSplitBitmap(Bitmap normalBmp){
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
			return null;
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
		
		process.recycle();
		
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
		
		return Bitmap.createScaledBitmap(bmp, w, h, true);
	}
}

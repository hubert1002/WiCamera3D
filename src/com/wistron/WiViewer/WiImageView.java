package com.wistron.WiViewer;




import java.util.Date;

import com.wistron.swpc.wicamera3dii.R;

import Utilities.CSStaticData;
import Utilities.FileTypeHelper;
import android.R.bool;
import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
public class WiImageView extends View
{
	public  Bitmap[] m_nextbitmap = new Bitmap[4];// 下一张图片
	public  Bitmap[] m_curbitmap = new Bitmap[4];// 当前图片
	private int m_alpha = 0;
    public  boolean is_3D=false;
	public int mode = 0;// 判断动画模式，用于幻灯片播放 为0时为正常的左右移动方式
	private Matrix m_matrix = new Matrix();      // 图片矩阵
	private Matrix m_matrix_scale = new Matrix();// 图片大小矩阵   ，只记录缩放比例，不记录中心点
	private Matrix m_matrix_rotate = new Matrix();//图片旋转矩阵
	private Matrix m_matrix_drag = new Matrix(); // 图片移动矩阵
	private Rect m_src1 = new Rect(0, 0, TDStaticData.SCREEN_WIDTH / 2,
			TDStaticData.SCREEN_HEIGHT);// 初始源图片区域选取矩形
	private Rect m_src2 = new Rect();// 下一张图片区域选取矩形
	private Rect m_dst1 = new Rect(0, 0, TDStaticData.SCREEN_WIDTH / 2,
			TDStaticData.SCREEN_HEIGHT);// 用于设置左半部分
	private Rect m_dst2 = new Rect(TDStaticData.SCREEN_WIDTH / 2, 0,
			TDStaticData.SCREEN_WIDTH, TDStaticData.SCREEN_HEIGHT);// 用于设置右半部分
	private Rect m_dst3 = new Rect();// 用于设置左半部分
	private Rect m_dst4 = new Rect();// 用于设置右半部分
	private boolean m_isloadimage = false;
	private static String m_error = null;
	private static String TAG = "WiImageView ";
	public Bitmap[] m_tempbitmap = null;// 下一张图片3D
	//public Bitmap m_temp2Dbitmap = null;// 下一张图片2D
	private VersionedGestureDetector mDetector;
	private boolean m_isOutOfBounds=false;
	public float m_Scale = 1.0f;
	public float[] m_drag = new float[2];
	public float m_rotate = 0;
	private Paint  m_paintforAntiAlias=null;
	Paint mpaint1 = null;
	Paint mpaint2 = null;
	OperateBitmaps mOperateBitmaps;
	String currentFilePath="";
	MediaFilePicker bitmapPicker;
	Context context;
	Matrix exchangematrix;
	private int mDegree=360;
    boolean isRotatable=true;
    public boolean isScalable=true;
    private boolean isConvertAuto=true;
  //  Region m_lRegion;
    Region m_rRegion;
	/*
	 * private Canvas m_CanvasforM ; private Bitmap m_bitforM ;
	 */
	public WiImageView(Context context,MediaFilePicker bitmapPicker)
	{
		super(context);
		this.context=context;
		mOperateBitmaps=new OperateBitmaps(context);
		mDetector = VersionedGestureDetector.newInstance(context,
				new GestureCallback());
		m_paintforAntiAlias=new Paint();
		m_paintforAntiAlias.setAntiAlias(true);
		m_paintforAntiAlias.setFilterBitmap(true);
		mpaint1 = new Paint();
		mpaint2 = new Paint();
		this.bitmapPicker=bitmapPicker;
		exchangematrix=new Matrix();
		int width=TDStaticData.SCREEN_WIDTH_ORG;
		int height=TDStaticData.SCREEN_HEIGHT_ORG;
		Rect tempRect=new Rect(0,0,width,height);
	//	m_lRegion =new Region();
		m_rRegion=new Region();
		for(int i=0;i<width/2;i++)
		{
//			tempRect.left=i*2+1;
//			tempRect.right=i*2+2;
//			m_lRegion.op(tempRect, Region.Op.UNION);
			tempRect.left=i*2;
			tempRect.right=i*2+1;
			m_rRegion.op(tempRect, Region.Op.UNION);
		}
//		exchangematrix.postRotate(-90, 0, 0);
//		exchangematrix.postTranslate(0, TDStaticData.SCREEN_WIDTH);
		// init();
	}
	public void setConvertAuto(boolean isconvert)
	{
		isConvertAuto=isconvert;
		mOperateBitmaps.setConvertAuto(isconvert);
	}
	public void setSceneDepth(int SceneDepth)
	{
		mOperateBitmaps.setSceneDepth(SceneDepth);
	}
	
	public void setMediaPicker(MediaFilePicker bitmapPicker)
	{
		this.bitmapPicker=bitmapPicker;
	}
	public void setOrientationType(int type)
	{
		if(type==1)
			isRotatable=false;
		this.mOperateBitmaps.setOrientationType(type);
	}
	
	
public void setOritention(int degree)
{
	if(degree==360)
	{
		exchangematrix=new Matrix();
		
	}
	else if (degree==90) {
		exchangematrix=new Matrix();
		exchangematrix.postRotate(-90, 0, 0);
		exchangematrix.postTranslate(0, TDStaticData.SCREEN_WIDTH);
	}
	else if(degree==180) {
		exchangematrix=new Matrix();
		exchangematrix.postRotate(180, TDStaticData.SCREEN_WIDTH/2, TDStaticData.SCREEN_HEIGHT/2);
	}
	else if (degree==270) {
		exchangematrix=new Matrix();
		exchangematrix.postRotate(90, 0, 0);
		exchangematrix.postTranslate(TDStaticData.SCREEN_HEIGHT, 0);
	}
	mDegree=degree;
	mOperateBitmaps.setOrientation(degree%360);
}
//public  void setOperateOrientation(int degree)
//{
//	mOperateBitmaps.setOrientation(degree%360);
//}
	/*
	 * public void init() {
	 * 
	 * try { m_bitforM=Bitmap.createBitmap(TDStaticData.SCREEN_WIDTH/2,
	 * TDStaticData.SCREEN_HEIGHT, Bitmap.Config.RGB_565); m_CanvasforM=new
	 * Canvas(m_bitforM); } catch (OutOfMemoryError e) { // TODO: handle
	 * exception m_error="fail to createBitmap"; Log.e(TAG,m_error);
	 * m_CanvasforM=null; m_bitforM=null; System.gc(); } }
	 */
	/**
	 * 回收当前图片，再设置为指定图片，并回收暂存片
	 */
	public void setCurbitmapR(Bitmap[] bitmap)
	{
		 recycleDBitmap(m_curbitmap);
		 this.m_curbitmap = bitmap;
		 recycleDBitmap(m_tempbitmap);
	     m_tempbitmap=null;
		 resetMatrix();
	}
	public void setCurbitmap(Bitmap[] bitmap)
	{
		
		this.m_curbitmap = bitmap;
		 recycleDBitmap(m_tempbitmap);
	     m_tempbitmap=null;
		 resetMatrix();
	}
	
	/**
	 * 设置下一张要显示的图片
	 */
	public void setNextbitmap(Bitmap[] bitmap)
	{
		this.m_nextbitmap = bitmap;
	}

	public void setNextbitmapR(Bitmap[] bitmap)
	{
		recycleDBitmap(m_nextbitmap);
		this.m_nextbitmap = bitmap;
	}

	public void exchange()
	{
		setCurbitmap(m_nextbitmap);
	}
	/**
	 * 交换当前图片和下一张要显示的图片
	 */
public void exchangeT()
{
	Bitmap[] tempBitmaps=this.m_curbitmap;
   this.m_curbitmap=this.m_nextbitmap;
   this.m_nextbitmap=tempBitmaps;
}
	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
	//	Log.e(TAG, "view touch");
		mDetector.onTouchEvent(ev);
		return true;
	}
public Rect scalerRect(Rect rect)
{
	float rate=1;
	Rect mRect =null;
	if(is_3D)
	{
		 mRect=new Rect((int)(rect.left*2), rect.top, (int)(rect.right*2), rect.bottom);
	}
	else
	{
		if(TDStaticData.SCREEN_HEIGHT!=TDStaticData.SCREEN_HEIGHT_ORG)
		{
			rate=((float)TDStaticData.SCREEN_HEIGHT)/((float)TDStaticData.SCREEN_HEIGHT_ORG);
		}
		else if(TDStaticData.SCREEN_WIDTH!=TDStaticData.SCREEN_WIDTH_ORG)
		{
			rate=((float)TDStaticData.SCREEN_WIDTH)/((float)TDStaticData.SCREEN_WIDTH_ORG);
		}
		 mRect=new Rect((int)(rect.left*2*rate), rect.top, (int)(rect.right*2*rate), rect.bottom);
		
	}
	
	return mRect;
	}
	/**
	 * 绘制图像
	 * 
	 * @param
	 */
	@Override
	protected void onDraw(Canvas canvas)
	{
		canvas.concat(exchangematrix);
		if(is_3D)
		{
			if(mode==0)
		   	{
		   		if (m_tempbitmap != null)
				{
		   		    if(m_tempbitmap[0]!=null&&!m_tempbitmap[0].isRecycled() )
		   		    {
		   		     canvas.drawBitmap(m_tempbitmap[0], scalerRect(m_src1), scalerRect(m_dst1), m_paintforAntiAlias);
		   		    }
				}
		   		else
		   		{
		   			//canvas.concat(m_matrix);
		   			if (this.m_curbitmap != null)
					{
						if(m_curbitmap[0]!=null&&!m_curbitmap[0].isRecycled() )
						{
						   canvas.drawBitmap(m_curbitmap[0], scalerRect(m_src1), scalerRect(m_dst1), m_paintforAntiAlias);
						}
					}
				}
		   		
			if (this.m_nextbitmap != null)
			{
				if(m_nextbitmap[0]!=null&&!m_nextbitmap[0].isRecycled())
				{
					canvas.drawBitmap(m_nextbitmap[0], scalerRect(m_src2), scalerRect(m_dst3), m_paintforAntiAlias);
				}
			
			}
			if (this.m_curbitmap == null && this.m_nextbitmap == null)
			{
				canvas.drawColor(Color.BLACK);
			}
		   	}
		   	else
		   	{
				mpaint1.setAlpha(m_alpha);
				mpaint2.setAlpha(255 - m_alpha);
				if (this.m_curbitmap != null&&m_curbitmap[0]!=null&&!m_curbitmap[0].isRecycled())
				{
					canvas.drawBitmap(m_curbitmap[0], 0, 0, mpaint2);

				}
				if (this.m_nextbitmap != null&&m_nextbitmap[0]!=null&&!m_nextbitmap[0].isRecycled())
				{
					canvas.drawBitmap(m_nextbitmap[0], 0, 0, mpaint1);
					
				}
				if (this.m_curbitmap == null && this.m_nextbitmap == null)
				{
					canvas.drawColor(Color.BLACK);
				}
			}
		}
		else
		{
		   	if(mode==0)
		   	{
		   		if (m_tempbitmap != null)
				{
		   		    if(m_tempbitmap[1]!=null&&!m_tempbitmap[1].isRecycled() )
		   		    {
		   		     canvas.drawBitmap(m_tempbitmap[1], scalerRect(m_src1), scalerRect(m_dst1), m_paintforAntiAlias);
		   		    }
				}
		   		else
		   		{
		   			//canvas.concat(m_matrix);
		   			if (this.m_curbitmap != null)
					{
						if(m_curbitmap[1]!=null&&!m_curbitmap[1].isRecycled() )
						{
						   canvas.drawBitmap(m_curbitmap[1], scalerRect(m_src1), scalerRect(m_dst1), m_paintforAntiAlias);
						}
					}
				}
		   		
			if (this.m_nextbitmap != null)
			{
				if(m_nextbitmap[1]!=null&&!m_nextbitmap[1].isRecycled())
				{
					canvas.drawBitmap(m_nextbitmap[1], scalerRect(m_src2), scalerRect(m_dst3), m_paintforAntiAlias);
				}
			
			}
			if (this.m_curbitmap == null && this.m_nextbitmap == null)
			{
				canvas.drawColor(Color.BLACK);
			}
		   	}
		   	else
		   	{
				mpaint1.setAlpha(m_alpha);
				mpaint2.setAlpha(255 - m_alpha);
				if (this.m_curbitmap != null&&m_curbitmap[1]!=null&&!m_curbitmap[1].isRecycled())
				{
					canvas.drawBitmap(m_curbitmap[1], 0, 0, mpaint2);

				}
				if (this.m_nextbitmap != null&&m_nextbitmap[1]!=null&&!m_nextbitmap[1].isRecycled())
				{
					canvas.drawBitmap(m_nextbitmap[1], 0, 0, mpaint1);
				}
				if (this.m_curbitmap == null && this.m_nextbitmap == null)
				{
					canvas.drawColor(Color.BLACK);
				}
			}
		}
	}
	/**
	 * 设置画笔的透明度
	 */
	public void setAlpha(int alpha)
	{
		this.m_alpha = alpha;
	}

	public int getPAlpha()
	{
		return this.m_alpha;
	}

	/**
	 * 设置源左图片的区域选取矩形
	 * 
	 * @param
	 */
	public void setSrc1Rect(int left, int top, int bottom, int right)
	{
		this.m_src1.left = left;
		this.m_src1.top = top;
		this.m_src1.right = right;
		this.m_src1.bottom = bottom;
	}

	/**
	 * 设置源右图片的区域选取矩形
	 * 
	 * @param
	 */
	public void setSrc2Rect(int left, int top, int bottom, int right)
	{
		this.m_src2.left = left;
		this.m_src2.top = top;
		this.m_src2.right = right;
		this.m_src2.bottom = bottom;
	}

	/**
	 * 重设区域选取矩形
	 * 
	 */
	public void resetRect()
	{
		this.m_src1.left = 0;
		this.m_src1.top = 0;
		this.m_src1.right = TDStaticData.SCREEN_WIDTH_ORG / 2;
		this.m_src1.bottom = TDStaticData.SCREEN_HEIGHT_ORG;
		this.m_dst1.left = 0;
		this.m_dst1.top = 0;
		this.m_dst1.right = TDStaticData.SCREEN_WIDTH_ORG / 2;
		this.m_dst1.bottom = TDStaticData.SCREEN_HEIGHT_ORG;
		this.m_dst2.left = TDStaticData.SCREEN_WIDTH_ORG / 2;
		this.m_dst2.top = 0;
		this.m_dst2.right = TDStaticData.SCREEN_WIDTH_ORG;
		this.m_dst2.bottom = TDStaticData.SCREEN_HEIGHT_ORG;
	}

	/**
	 * 设置源图片应放置到画布上的区域选取矩形，左部分
	 * 
	 * @param
	 */
	public void setDst1Rect(int left, int top, int bottom, int right)
	{
		this.m_dst1.left = left;
		this.m_dst1.top = top;
		this.m_dst1.right = right;
		this.m_dst1.bottom = bottom;
	}

	/**
	 * 设置源图片应放置到画布上的区域选取矩形，右部分
	 * 
	 * @param
	 */
	public void setDst2Rect(int left, int top, int bottom, int right)
	{
		this.m_dst2.left = left;
		this.m_dst2.top = top;
		this.m_dst2.right = right;
		this.m_dst2.bottom = bottom;
	}

	/**
	 * 设置下一张图片应放置到画布上的区域选取矩形，左部分
	 * 
	 * @param
	 */
	public void setDst3Rect(int left, int top, int bottom, int right)
	{
		this.m_dst3.left = left;
		this.m_dst3.top = top;
		this.m_dst3.right = right;
		this.m_dst3.bottom = bottom;
	}

	/**
	 * 设置下一张图片应放置到画布上的区域选取矩形，右部分
	 * 
	 * @param
	 */
	public void setDst4Rect(int left, int top, int bottom, int right)
	{
		this.m_dst4.left = left;
		this.m_dst4.top = top;
		this.m_dst4.right = right;
		this.m_dst4.bottom = bottom;

	}

	public boolean isLoading()
	{
		return this.m_isloadimage;
	}
	
	/**
	 * 通过各压缩一半的左右图，生成左右交错的3D显示图片，大小为屏幕大小
	 * @param bitmaps
	 * @param isinit
	 * @return
	 */
	public Bitmap get3DBitmapToShowFromLR(Bitmap[] bitmaps,boolean isinit)
	{
		long time1,time2,time3;
	   time1=new Date().getTime();
		if(bitmaps!=null)
		{
			if(bitmaps[0]!=null&&bitmaps[1]!=null)
			{
				if(isinit)
				{
					int width=bitmaps[0].getWidth();
					int height=bitmaps[0].getHeight();
					Bitmap temp_l=Bitmap.createScaledBitmap(bitmaps[0], width*2, height, false);
					Bitmap temp_r=Bitmap.createScaledBitmap(bitmaps[1], width*2, height, false);
					Canvas mCanvas=new Canvas(temp_l);
					time2=new Date().getTime();
					if(CSStaticData.DEBUG)
					Log.e("clip-----------------1>","时间为"+(time2-time1));
					mCanvas.clipRegion(m_rRegion, Region.Op.INTERSECT);
					mCanvas.drawBitmap(temp_r,0,0, null);
					temp_r.recycle();
					time3=new Date().getTime();
					if(CSStaticData.DEBUG)
					Log.e("clip-----------------2>","时间为"+(time3-time2));
					return temp_l;
				}
				else
				{
					Canvas mCanvas=new Canvas(bitmaps[0]);
					time2=new Date().getTime();
					if(CSStaticData.DEBUG)
					Log.e("clip-----------------1>","时间为"+(time2-time1));
					mCanvas.clipRegion(m_rRegion, Region.Op.INTERSECT);
					mCanvas.drawBitmap(bitmaps[1],0,0, null);
					bitmaps[1].recycle();
					time3=new Date().getTime();
					if(CSStaticData.DEBUG)
					Log.e("clip-----------------2>","时间为"+(time3-time2));
					return bitmaps[0];
				}
			}
		}
		return null;
	}
	
	
	//GenBitmapToShowInClip
	public Bitmap get3DBitmapToShowFromLR__(Bitmap[] bitmaps)
	{
		long time1,time2,time3,time4,time5,time6;
	   time1=new Date().getTime();
		Bitmap res =null;
		if(bitmaps!=null)
		{
			if(bitmaps[0]!=null&&bitmaps[1]!=null)
			{
				int width=bitmaps[0].getWidth();
				int height=bitmaps[1].getHeight();
				Rect srcRect=new Rect(0,0,width,height);
				Rect desRect=new Rect(0,0,width*2,height);
				Matrix matrix=new Matrix();
				matrix.postScale(2.0f,1);
				Bitmap temp_l=Bitmap.createScaledBitmap(bitmaps[0], width*2, height, false);
				Bitmap temp_r=Bitmap.createScaledBitmap(bitmaps[1], width*2, height, false);
				res=Bitmap.createBitmap(width*2, height, Config.RGB_565);
				Canvas mCanvas=new Canvas(res);
				time2=new Date().getTime();
				Log.e("clip-----------------1>","时间为"+(time2-time1));
				mCanvas.save();
			//	mCanvas.clipRect(0, 0, width*2, height);
		/*		mCanvas.clipRegion(m_lRegion, Region.Op.INTERSECT);*/
				time3=new Date().getTime();
				Log.e("clip-----------------2>","时间为"+(time3-time2));
//				for(int i=0;i<width;i++)
//				{
//					tempRect.set(i*2, 0, i*2+1, height);
//					mCanvas.clipRect(tempRect, Region.Op.DIFFERENCE);
//				}
				mCanvas.drawBitmap(temp_l,0,0 , null);
				temp_l.recycle();
				
				time4=new Date().getTime();
				Log.e("clip-----------------3>","时间为"+(time4-time3));
	            mCanvas.restore();
	            time5=new Date().getTime();
	    		Log.e("clip-----------------4>","时间为"+(time5-time4));
	            
	            
	          //  mCanvas.save();
			//	mCanvas.clipRect(0, 0, width*2, height);
//				for(int i=0;i<width;i++)
//				{
//					tempRect.set(i*2+1, 0, i*2+2, height);
//					mCanvas.clipRect(tempRect, Region.Op.DIFFERENCE);
//				}
				mCanvas.clipRegion(m_rRegion, Region.Op.INTERSECT);
				mCanvas.drawBitmap(temp_r,0,0, null);
				temp_r.recycle();
	          //  mCanvas.restore();
	            
//				mCanvas.save();
//				mCanvas.drawBitmap(bitmaps[1], srcRect, desRect, null);
//				for(int i=0;i<width;i++)
//				{
//					tempRect.set(i*2+1, 0, i*2+2, height);
//					mCanvas.clipRect(tempRect);
//				}
//				mCanvas.restore();
			}
		}
		 time6=new Date().getTime();
		 Log.e("clip-----------------4>","总时间为"+(time6-time1));
		return res;
		
	}
	
	public Bitmap get3DBitmapToShowFromLR_(Bitmap[] bitmaps)
	{
		long time1,time2,time3,time4,time5,time6;
		 time1=new Date().getTime();
		Bitmap res=null;
		if(bitmaps!=null)
		{
			if(bitmaps[0]!=null&&bitmaps[1]!=null)
			{
				int width=bitmaps[0].getWidth()*2;
				int width_half=width/2;
				int height=bitmaps[0].getHeight();
				Rect srcRect=new Rect(0, 0, width, height);
				Rect desRect1=new Rect(0, 0, width, height);
				Rect desRect2=new Rect(0, 0, width, height);
				res=Bitmap.createBitmap(width, height, Config.RGB_565);
				
				Canvas mCanvas=new Canvas(res);
				Paint mPaint=new Paint();
				mPaint.setAntiAlias(true);
				 time2=new Date().getTime();
					Log.e("clip-----------------1>","时间为"+(time2-time1));
				for(int i=0;i<width_half;i++)
				{
					srcRect.left=i;
					srcRect.right=i+1;
					desRect1.left=i*2;
					desRect1.right=i*2+1;
					desRect2.left=i*2+1;
					desRect2.right=(i+1)*2;
					mCanvas.drawBitmap(bitmaps[1], srcRect, desRect1, mPaint);
					mCanvas.drawBitmap(bitmaps[0], srcRect, desRect2, mPaint);
				}
				time3=new Date().getTime();
				Log.e("clip-----------------3>","时间为"+(time3-time2));
				
//				 int[] colorArrey_l=new int[width_half*height];
//				 int[] colorArrey_r=new int[width_half*height];
//				 bitmaps[0].getPixels(colorArrey_l, 0, width_half, 0,0, width_half, height);
//				 bitmaps[1].getPixels(colorArrey_r, 0, width_half, 0,0, width_half, height);
//				 for (int i = 0; i < width_half; i++)
//				{
//					 
//					 mCanvas.drawBitmap(colorArrey_r, 0, width_half, i*2, 0, 1, height, false, null);
//					 mCanvas.drawBitmap(colorArrey_l, 0, width_half, i*2+1, 0, 1, height, false, null);
//				}
				
//				int[] colorArrey=new int[height];
//				 for (int i = 0; i < width_half; i++)
//				{
//					 bitmaps[1].getPixels(colorArrey, 0, 1, i,0, 1, height);
//					 mCanvas.drawBitmap(colorArrey, 0, 1, i*2, 0, 1, height, false, null);
//					 bitmaps[0].getPixels(colorArrey, 0, 1, i,0, 1, height);
//					 mCanvas.drawBitmap(colorArrey, 0, 1, i*2+1, 0, 1, height, false, null);
//				}
			}
		}
		 time6=new Date().getTime();
		Log.e("clip-----------------4>","时间为"+(time6-time1));
		return res;
		
	}
	/**
	 * 根据图片路径生成图片数组
	 * 【0】为交错后用于3D显示的图片，尺寸为屏幕大小
	 * 【1】为正常比例图片，2D下为原图，3D下为左图，尺寸为屏幕大小
	 * 【2】【3】为各压缩一半后的左右图，尺寸为屏幕大小的一半
	 * @param filepath
	 * @return
	 */
	public Bitmap[] getNextBitmapEx(String filepath)
	{
		Bitmap[] res=new Bitmap[4];
		try
		{
			  long time1=new Date().getTime();
		      Bitmap[] mBitmaps=getNextBitmap(filepath);
			  long time2=new Date().getTime();
			  if(CSStaticData.DEBUG)
			  Log.e(TAG, "getNextBitmap-------->耗时为"+(time2-time1));
			  res[1]=mBitmaps[2];
			  Bitmap[] temp=new Bitmap[2];
			  temp[0]=mBitmaps[0];
			  temp[1]=mBitmaps[1];
			  res[0]=get3DBitmapToShowFromLR(temp,true);
			  res[2]=mBitmaps[0];
			  res[3]=mBitmaps[1];
		} catch (OutOfMemoryError e)
		{
			if(CSStaticData.DEBUG)
			Log.e(TAG, " [OutOfMemoryError]--------> getNextBitmapEx");
		}
		return res;
		
	}
	/**
	 * 根据文件路径解码生成图片数组，
	 * 【0】【1】为各压缩一半后的左右图，尺寸为屏幕大小的一半
	 * 【2】为正常比例图片，2D下为原图，3D下为左图，尺寸为屏幕大小
	 */
	public   Bitmap[] getNextBitmap(String filepath)
	{
		if(CSStaticData.DEBUG)
			Log.e("当前路径", "" + filepath);
		
		Bitmap[] srcBitmap = null;
		Canvas m_lCanvas = null;
		Canvas m_rCanvas = null;
		Canvas m_Canvas = null;
		int srcHeight = 0;
		int srcWidth = 0;
		int destWidth_2D =  TDStaticData.SCREEN_WIDTH;
		int destHeight_2D = TDStaticData.SCREEN_HEIGHT;
		int destWidth_3D = TDStaticData.SCREEN_WIDTH_ORG;
		int destHeight_3D = TDStaticData.SCREEN_HEIGHT_ORG;
		//srcBitmap为解码后的原图数组，如果为3D则为左右图原图，2D为原图和null
		srcBitmap=mOperateBitmaps.decodeBitmap(filepath);
		//01为左右图（压缩过后的），2为左图（原图）
		Bitmap[] mbitmap = new Bitmap[3];
		
			mbitmap[0] = Bitmap.createBitmap(destWidth_3D/ 2,
					destHeight_3D, Bitmap.Config.RGB_565);
			mbitmap[2]= Bitmap.createBitmap(destWidth_2D,
					destHeight_2D, Bitmap.Config.RGB_565);
			m_lCanvas = new Canvas(mbitmap[0]);
			m_Canvas  =new Canvas(mbitmap[2]);
			mbitmap[1] = Bitmap.createBitmap(destWidth_3D / 2,destHeight_3D, Bitmap.Config.RGB_565);
			m_rCanvas = new Canvas(mbitmap[1]);

		if(srcBitmap!=null&&srcBitmap[0]!=null)
		{
			
			//默认左右图大小相同
			srcWidth = srcBitmap[0].getWidth();
			srcHeight = srcBitmap[0].getHeight();
			float ratio1=((float) srcWidth / (float) srcHeight) ;
			float ratio_des_2D = ((float) destWidth_2D / (float) destHeight_2D);
			float ratio_des_3D = ((float) destWidth_3D / (float) destHeight_3D);
			Rect srcRect=new Rect(0, 0, srcWidth, srcHeight);
			Rect srcRect1=new Rect(0, 0, srcWidth/2, srcHeight);
			Rect srcRect2=new Rect(srcWidth/2, 0, srcWidth, srcHeight);
			//生成屏幕大小的图片
			
			//画2D图片，分别为左图原图
			if (ratio1 >= ratio_des_2D)
			{
				int top=(int)(destHeight_2D- destWidth_2D/ratio1)/2;
				int bot=(int)(destHeight_2D+ destWidth_2D/ratio1)/2;
				Rect desRect=new Rect(0, top, destWidth_2D, bot);
				m_Canvas.drawBitmap(srcBitmap[0], srcRect, desRect, null);
			
		    } else
			{
				int left=(int)(destWidth_2D-ratio1*destHeight_2D)/2;
				int right=(int)(destWidth_2D+ratio1*destHeight_2D)/2;
				Rect desRect=new Rect(left, 0, right, destHeight_2D);
				m_Canvas.drawBitmap(srcBitmap[0], srcRect, desRect, null);
				
		    }
	       //画3D图片，左右压缩图
			if(srcBitmap[1]!=null)
			{
				if (ratio1 >= ratio_des_3D)
				{
					int top=(int)(destHeight_3D- destWidth_3D/ratio1)/2;
					int bot=(int)(destHeight_3D+ destWidth_3D/ratio1)/2;
					Rect desRect2=new Rect(0, top, destWidth_3D/2, bot);
					m_lCanvas.drawBitmap(srcBitmap[1], srcRect1, desRect2, null);
					m_rCanvas.drawBitmap(srcBitmap[1], srcRect2, desRect2, null);
			    } else
				{
					int left=(int)(destWidth_3D-ratio1*destHeight_3D)/2;
					int right=(int)(destWidth_3D+ratio1*destHeight_3D)/2;
					Rect desRect2=new Rect(left/2, 0, right/2, destHeight_3D);
					m_lCanvas.drawBitmap(srcBitmap[1], srcRect1, desRect2, null);
					m_rCanvas.drawBitmap(srcBitmap[1], srcRect2, desRect2, null);
			    }
			}
		}
		//回收原图
        OperateBitmaps.recycleDBitmap(srcBitmap);
        srcBitmap=null;
		//添加视频标志，无论原图是否为空
//		if(is_video)
//		{
//		videotag = BitmapFactory.decodeResource(this.getResources(),
//					R.drawable.video_review_icon);
//		m_lCanvas.drawBitmap(videotag, new Rect(0, 0, videotag.getWidth(),
//				videotag.getHeight()), 	new Rect((destWidth_3D / 2 - videotag.getWidth() / 2) / 2,
//						(destHeight_3D - videotag.getHeight()) / 2,
//						(destWidth_3D / 2 + videotag.getWidth() / 2) / 2,
//						(destHeight_3D + videotag.getHeight()) / 2), null);
//			m_rCanvas.drawBitmap(videotag, new Rect(0, 0, videotag.getWidth(),
//					videotag.getHeight()), 	new Rect((destWidth_3D / 2 - videotag.getWidth() / 2) / 2,
//							(destHeight_3D - videotag.getHeight()) / 2,
//							(destWidth_3D / 2 + videotag.getWidth() / 2) / 2,
//							(destHeight_3D + videotag.getHeight()) / 2), null);
//        m_Canvas.drawBitmap(videotag, (destWidth_2D-videotag.getWidth())/2, (destHeight_2D-videotag.getHeight())/2, null);
//		videotag.recycle();
//		}
		return mbitmap;
	}
//	
//	/**
//	 * 根据文件路径获得一个存放压缩了的左右图的bitmap数组,以及左图的原图，如果为2d则为原图
//	 */
//	public   Bitmap[] getNextBitmapEx(String filepath)
//	{
//		Log.e("当前路径", "" + filepath);
//		Bitmap[] srcBitmap = null;
//		Bitmap videotag = null;
//		Canvas m_lCanvas = null;
//		Canvas m_rCanvas = null;
//		Canvas m_Canvas = null;
//		boolean is_3D=OperateBitmaps.is3DSource(filepath);//判断是否为3D，包括视频
//		boolean is_mpo =OperateBitmaps.isMpo(filepath) ;
//		boolean is_video = OperateBitmaps.isVideo(filepath);
//		int srcHeight = 0;
//		int srcWidth = 0;
//		int destWidth = TDStaticData.SCREEN_WIDTH;
//		int destHeight = TDStaticData.SCREEN_HEIGHT;
//		int destWidth_org = TDStaticData.SCREEN_WIDTH_ORG;
//		int destHeight_org = TDStaticData.SCREEN_HEIGHT_ORG;
//		//srcBitmap为解码后的原图数组，如果为3D则为左右图原图，2D为原图和null
//		srcBitmap=mOperateBitmaps.decodeBitmap(filepath);
//		//01为左右图（压缩过后的），2为左图（原图）
//		Bitmap[] mbitmap = new Bitmap[3];
//			mbitmap[0] = Bitmap.createBitmap(TDStaticData.SCREEN_WIDTH / 2,
//					TDStaticData.SCREEN_WIDTH, Bitmap.Config.RGB_565);
//			mbitmap[2]= Bitmap.createBitmap(TDStaticData.SCREEN_WIDTH ,
//					TDStaticData.SCREEN_HEIGHT, Bitmap.Config.RGB_565);
//			m_lCanvas = new Canvas(mbitmap[0]);
//			m_Canvas  =new Canvas(mbitmap[2]);
//		if(is_3D)
//		{
//			mbitmap[1] = Bitmap.createBitmap(TDStaticData.SCREEN_WIDTH / 2,
//					TDStaticData.SCREEN_WIDTH, Bitmap.Config.RGB_565);
//			m_rCanvas = new Canvas(mbitmap[1]);
//		}
//		else
//		{
//			mbitmap[1]=mbitmap[0];
//		}
//
//
//	
//		if(srcBitmap!=null&&srcBitmap[0]!=null)
//		{
//			
//			//默认左右图大小相同
//			srcWidth = srcBitmap[0].getWidth();
//			srcHeight = srcBitmap[0].getHeight();
//			float ratio1=((float) srcWidth / (float) srcHeight) ;
//			float ratio2 = ((float) destWidth / (float) destHeight);
//			float ratio2_ = ((float) destWidth_org / (float) destHeight_org);
//			Rect srcRect=new Rect(0, 0, srcWidth, srcHeight);
//			//生成屏幕大小的图片
//				if (ratio1 >= ratio2)
//				{
//					Log.e("shang hei", "ok");
//					int top=(int)(destHeight- destWidth/ratio1)/2;
//					int bot=(int)(destHeight+ destWidth/ratio1)/2;
//					Rect desRect=new Rect(0, top, destWidth, bot);
//					Rect desRect2=new Rect(0, top, destWidth/2, bot);
//					m_Canvas.drawBitmap(srcBitmap[0], srcRect, desRect, null);
//					m_lCanvas.drawBitmap(srcBitmap[0], srcRect, desRect2, null);
//					//  m_Canvas.drawBitmap(srcBitmap[0], new Rect(0, 0, srcWidth, srcHeight), new Rect(0, (int)(destHeight- destWidth/ratio1)/2, destWidth, (int)(destHeight+ destWidth/ratio1)/2), null);
//					 // m_lCanvas.drawBitmap(srcBitmap[0], new Rect(0, 0, srcWidth, srcHeight), new Rect(0, (int)(destHeight- destWidth/ratio1)/2, destWidth/2, (int)(destHeight+ destWidth/ratio1)/2), null);
//					 if(is_3D)
//					 {
//						 m_rCanvas.drawBitmap(srcBitmap[1], srcRect, desRect2, null);
//					 // m_rCanvas.drawBitmap(srcBitmap[1], new Rect(0, 0, srcWidth, srcHeight), new Rect(0, (int)(destHeight- destWidth/ratio1)/2, destWidth/2, (int)(destHeight+ destWidth/ratio1)/2), null);
//					 }
//			    } else
//				{
//					Log.e("zuo hei", "ok");
//					int left=(int)(destWidth-ratio1*destHeight)/2;
//					int right=(int)(destWidth+ratio1*destHeight)/2;
//					Rect desRect=new Rect(left, 0, right, destHeight);
//					Rect desRect2=new Rect(left/2, 0, right/2, destHeight);
//					m_Canvas.drawBitmap(srcBitmap[0], srcRect, desRect, null);
//					m_lCanvas.drawBitmap(srcBitmap[0], srcRect, desRect2, null);
//					 // m_lCanvas.drawBitmap(srcBitmap[0], new Rect(0, 0, srcWidth, srcHeight), new Rect((int)(destWidth-ratio1*destHeight)/4, 0, (int)(destWidth+ratio1*destHeight)/4, destHeight), null);
//					 // m_Canvas.drawBitmap(srcBitmap[0], new Rect(0, 0, srcWidth, srcHeight), new Rect((int)(destWidth-ratio1*destHeight)/2, 0, (int)(destWidth+ratio1*destHeight)/2, destHeight), null);
//					 if(is_3D)
//					 {
//						m_rCanvas.drawBitmap(srcBitmap[1], srcRect, desRect2, null);
//					  //m_rCanvas.drawBitmap(srcBitmap[1], new Rect(0, 0, srcWidth, srcHeight), new Rect((int)(destWidth-ratio1*destHeight)/4, 0, (int)(destWidth+ratio1*destHeight)/4, destHeight), null);
//					 }
//			    }
//		
//		}
//		//回收原图
//        OperateBitmaps.recycleDBitmap(srcBitmap);
//		//添加视频标志，无论原图是否为空
//		if(is_video)
//		{
//			videotag = BitmapFactory.decodeResource(this.getResources(),
//					R.drawable.videotag);
//		m_lCanvas.drawBitmap(videotag, new Rect(0, 0, videotag.getWidth(),
//				videotag.getHeight()), 	new Rect((destWidth / 2 - videotag.getWidth() / 2) / 2,
//						(destHeight-50 - videotag.getHeight()) / 2,
//						(destWidth / 2 + videotag.getWidth() / 2) / 2,
//						(destHeight -50+ videotag.getHeight()) / 2), null);
//		if(is_3D)
//		{
//			m_rCanvas.drawBitmap(videotag, new Rect(0, 0, videotag.getWidth(),
//					videotag.getHeight()), 	new Rect((destWidth / 2 - videotag.getWidth() / 2) / 2,
//							(destHeight-50 - videotag.getHeight()) / 2,
//							(destWidth / 2 + videotag.getWidth() / 2) / 2,
//							(destHeight -50+ videotag.getHeight()) / 2), null);
//		}
//	
//        m_Canvas.drawBitmap(videotag, (destWidth-videotag.getWidth())/2, (destHeight-50-videotag.getHeight())/2, null);
//		videotag.recycle();
//		}
//		return mbitmap;
//
//	}
	
	/**
	 * 重置矩阵
	 */
	public void resetMatrix()
	{
		this.m_matrix.setTranslate(0, 0);
		this.m_matrix_rotate.setTranslate(0, 0);
		this.m_matrix_drag.setTranslate(0, 0);
		this.m_matrix_scale.setTranslate(0, 0);
		m_Scale = 1.0f;
		m_drag=new float[2];
		m_rotate=0;
	}
	public Bitmap[] getLRFrom3DBitmap(Bitmap bitmap)
	{
		if(bitmap!=null)
		{
			int width=bitmap.getWidth();
			int height=bitmap.getHeight();
			if(width*height!=0)
			{
				Rect srcRect;
				Rect desRect;
				Bitmap[] mBitmaps =new Bitmap[2];
				mBitmaps[0]=Bitmap.createBitmap(width/2, height, Config.RGB_565);
				mBitmaps[1]=Bitmap.createBitmap(width/2, height, Config.RGB_565);
				Canvas lCanvas=new Canvas(mBitmaps[0]);
				Canvas rCanvas=new Canvas(mBitmaps[1]);
				for(int i=0;i<width;i++)
				{
					srcRect=new Rect(i, 0, i+1, height);
					
					if(i%2==0)
					{
						desRect=new Rect(i/2, 0, i/2+1, height);
						rCanvas.drawBitmap(bitmap, srcRect, desRect, null);
					}
					else
					{
						desRect=new Rect((i-1)/2, 0, (i-1)/2+1, height);
						lCanvas.drawBitmap(bitmap, srcRect, desRect, null);
					}
				}
				return mBitmaps;
			}
			
		}
		return null;
	}
	
	/**
	 * 根据矩阵，设置左右图
	 */
	public void setLRwithMatrix()
	{
      long time1,time2;
      time1=new Date().getTime();
		if (m_curbitmap != null)
		{
		try{
			if (m_tempbitmap == null)
				m_tempbitmap = new Bitmap[2];
			if(is_3D)
			{
				if(m_curbitmap[2]!=null&&m_curbitmap[3]!=null)
				{
					Bitmap[] lrBitmaps=new Bitmap[2];
					lrBitmaps[0]=m_curbitmap[2];
					lrBitmaps[1]=m_curbitmap[3];
					Matrix temp =new Matrix(m_matrix);
					temp.postScale(2, 1);
					//先放大，在左右交错
					for (int i = 0; i < 2; i++)
					{
							Bitmap m_Mbitmap = Bitmap.createBitmap(
									TDStaticData.SCREEN_WIDTH_ORG ,
									TDStaticData.SCREEN_HEIGHT_ORG, Bitmap.Config.RGB_565);
							Canvas m_Mcanvas = new Canvas(m_Mbitmap);
							m_Mcanvas.drawBitmap(lrBitmaps[i], temp, m_paintforAntiAlias);
							lrBitmaps[i] = m_Mbitmap;
					}
					Bitmap mBitmap=get3DBitmapToShowFromLR(lrBitmaps,false);
					recycleSBitmap(m_tempbitmap[0]);
					m_tempbitmap[0] = mBitmap;
				}
				
			}
			else
			{
				if(m_curbitmap[1]!=null)
				{
					Bitmap m_Mbitmap = Bitmap.createBitmap(
							TDStaticData.SCREEN_WIDTH,
							TDStaticData.SCREEN_HEIGHT, Bitmap.Config.RGB_565);
					Canvas m_Mcanvas = new Canvas(m_Mbitmap);
					m_Mcanvas.drawBitmap(m_curbitmap[1], m_matrix, m_paintforAntiAlias);
					recycleSBitmap(m_tempbitmap[1]);
					m_tempbitmap[1] = m_Mbitmap;
				}
				
			}
			}catch(OutOfMemoryError er)
			{
				Log.e(TAG, "缩放时内存溢出了！");
			}
		}
		time2=new Date().getTime();
		if(CSStaticData.DEBUG)
			Log.e(TAG, "缩放总时长="+(time2-time1));
		invalidate();
	}
	/**
	 * 设置矩阵
	 */
	public void setMatrix(Matrix matrix)
	{
		this.m_matrix = matrix;
		setLRwithMatrix();
	}
	/**
	 * 回收bitmap数组
	 */
	public static void recycleDBitmap(Bitmap[] bitmaps)
	{
		if (bitmaps != null)
		{
			for (int i = 0; i < bitmaps.length; i++)
			{
				if(bitmaps[i]!=null)
				{
					if (!bitmaps[i].isRecycled())
						bitmaps[i].recycle();
				}
			
			}
		}

	}
	public static boolean isRecycledD(Bitmap[] bitmaps)
	{
		boolean res=true;
		if(bitmaps!=null)
		{
			for (int i = 0; i < bitmaps.length; i++)
			{
				if(bitmaps[i]!=null)
				{
					if (!bitmaps[i].isRecycled())
						res=false;
				}
			}
		}
		return res;
	}
	
	
    public void ChangeMode(boolean bool)
    {
    	is_3D=bool;
    	resetMatrix();
    		recycleDBitmap(m_tempbitmap);
    		m_tempbitmap=null;
    	invalidate();
    }
	public void recycleSBitmap(Bitmap bitmap)
	{
		if (bitmap != null)
		{
			if (!bitmap.isRecycled())
				bitmap.recycle();
			bitmap=null;	
		}
	
	}

	/**
	 * 判定放大或拖动是否移动超出边界
	 */
	public boolean isOutOfBounds()
	{
		int width;
		int height;
		float[] a = new float[9];
		m_matrix.getValues(a);
		float[] b = new float[4];
		b[0] = a[2];
		b[1] = a[5];
		if (is_3D)
		{
			width = TDStaticData.SCREEN_WIDTH_ORG / 2;
			height = TDStaticData.SCREEN_HEIGHT_ORG;
		} else
		{
			width = TDStaticData.SCREEN_WIDTH;
			height = TDStaticData.SCREEN_HEIGHT;
		}
		b[2] = a[2] + width * a[0];
		b[3] = a[5] + height * a[0];
		//b[2] = a[2] + TDStaticData.SCREEN_WIDTH_ORG * a[0];
		//b[3] = a[5] + TDStaticData.SCREEN_HEIGHT_ORG * a[0];
		boolean[] m_bool = new boolean[4];
		if (b[0] >= width / 2 || b[1] >= height / 2)
			m_bool[0] = true;
		if (b[2] <= width / 2 || b[1] >= height / 2)
			m_bool[1] = true;
		if (b[0] >= width / 2 || b[3] <= height / 2)
			m_bool[2] = true;
		if (b[2] <= width / 2 || b[3] <= height / 2)
			m_bool[3] = true;
		if (m_bool[0] || m_bool[1] || m_bool[2] || m_bool[3])
			return true;
		return false;

	}
	/**
	 * 实现拖动和缩放
	 */
	private class GestureCallback implements
			VersionedGestureDetector.OnGestureListener
	{
		public void onDrag(float x, float y)
		{
			float dx = 0;
			float dy = 0;
			if (mDegree == 90)
			{
				dx = -y;
				dy = x;
			} else if (mDegree == 270)
			{
				dx = y;
				dy = -x;
			} else if (mDegree == 360)
			{
				dx = x;
				dy = y;
			} else if (mDegree == 180)
			{
				dx = -x;
				dy = -y;
			}
			float x_cent = is_3D ? dx / 2 : dx;
			if (m_Scale > 1.0)
			{
				m_matrix.postTranslate(x_cent, dy);
				m_matrix_drag.postTranslate(x_cent, dy);
				m_isOutOfBounds = isOutOfBounds();
				if (m_isOutOfBounds)
				{
					m_matrix.postTranslate(-x_cent, -dy);
					m_matrix_drag.postTranslate(-x_cent, -dy);
				} else
				{
					float[] a = new float[9];
					m_matrix_drag.getValues(a);
					m_drag[0] = a[2];
					m_drag[1] = a[5];
					setLRwithMatrix();
				}
			}
		}

		@Override
		public void onScale(float scaleFactor, float dx, float dy,
				ScaleGestureDetector detector)
		{
			//缩放比例小于零时没有以中心来缩放
//			if(isScalable)
//			{
//				 float[] ex=new float[2];
//				 ex[0]=dx;
//				 ex[1]=dy;
//				 Matrix matrix=new Matrix();
//				 exchangematrix.invert(matrix);
//				 matrix.mapPoints(ex);
//				float x=ex[0];
//				float y=ex[1];
//				float x_cent=is_3D?x/2:x;
//				m_matrix.postScale(scaleFactor, scaleFactor, x_cent, y);
//				m_matrix_scale.postScale(scaleFactor, scaleFactor, x_cent, y);
//				float[] a = new float[9];
//				m_matrix_scale.getValues(a);
//				m_Scale = a[0];
//				m_isOutOfBounds=isOutOfBounds();
//				if (m_Scale > 0.5f && m_Scale < 5.0f)
//				{
//					if (!m_isOutOfBounds)
//					{
//					    setLRwithMatrix();
//						return;
//					}
//				}
//				m_matrix.postScale(1 / scaleFactor, 1 / scaleFactor, x_cent, y);
//				m_matrix_scale.postScale(1 / scaleFactor, 1 / scaleFactor, x_cent, y);
//			}
			
			
			
			
			if(isScalable)
			{
				m_matrix_scale.postScale(scaleFactor, scaleFactor);
				float[] a = new float[9];
				m_matrix_scale.getValues(a);
				m_Scale = a[0];
				if (m_Scale > 0.5f && m_Scale < 5.0f)
				{
					 float x_cent=0;
					 float y=0;
					 float[] ex=new float[2];
					if(m_Scale<1)
					{
						if(is_3D)
						{
							
							 ex[0]=TDStaticData.SCREEN_WIDTH_ORG;
							 ex[1]=TDStaticData.SCREEN_HEIGHT_ORG;
							 Matrix matrix=new Matrix();
							 exchangematrix.invert(matrix);
							 matrix.mapPoints(ex);
							 y=ex[1]/2;
							 x_cent=ex[0]/4;
						}
						else
						{
							 ex[0]=TDStaticData.SCREEN_WIDTH;
							 ex[1]=TDStaticData.SCREEN_HEIGHT;
							 Matrix matrix=new Matrix();
							 exchangematrix.invert(matrix);
							 matrix.mapPoints(ex);
							 y=ex[1]/2;
							 x_cent=ex[0]/2;
						}
						m_matrix.postScale(scaleFactor, scaleFactor, x_cent, y);
						setLRwithMatrix();
					}
					else
					{
						 ex[0]=dx;
						 ex[1]=dy;
						 Matrix matrix=new Matrix();
						 exchangematrix.invert(matrix);
						 matrix.mapPoints(ex);
						 y=ex[1];
						 x_cent=is_3D?ex[0]/2:ex[0];
						 m_matrix.postScale(scaleFactor, scaleFactor, x_cent, y);
						 m_isOutOfBounds=isOutOfBounds();
						if (!m_isOutOfBounds)
						{
						   setLRwithMatrix();
						}
						else
						{
							m_matrix.postScale(1 / scaleFactor, 1 / scaleFactor, x_cent, y);
						}
					}
						
				}
				else
				{
					m_matrix_scale.postScale(1 / scaleFactor, 1 / scaleFactor);
				}
			}
				
				
//				 float[] ex=new float[2];
//				 ex[0]=dx;
//				 ex[1]=dy;
//				 Matrix matrix=new Matrix();
//				 exchangematrix.invert(matrix);
//				 matrix.mapPoints(ex);
//				float x=ex[0];
//				float y=ex[1];
//				float x_cent=is_3D?x/2:x;
//				m_matrix.postScale(scaleFactor, scaleFactor, x_cent, y);
//				m_matrix_scale.postScale(scaleFactor, scaleFactor, x_cent, y);
//				float[] a = new float[9];
//				m_matrix_scale.getValues(a);
//				m_Scale = a[0];
//				m_isOutOfBounds=isOutOfBounds();
//				if (m_Scale > 0.5f && m_Scale < 5.0f)
//				{
//					if (!m_isOutOfBounds)
//					{
//					    setLRwithMatrix();
//						return;
//					}
//				}
//				m_matrix.postScale(1 / scaleFactor, 1 / scaleFactor, x_cent, y);
//				m_matrix_scale.postScale(1 / scaleFactor, 1 / scaleFactor, x_cent, y);
//			}
			
			
		
		}

		@Override
		public void onRotate(float angle, float totalangle,boolean isEnd)
		{
			// TODO Auto-generated method stub
			if(!is_3D&&isRotatable&&!FileTypeHelper.isStereoImageFile(bitmapPicker.getCurrentFileName()))
			{
				m_matrix.postRotate( angle, TDStaticData.SCREEN_WIDTH/2, TDStaticData.SCREEN_HEIGHT/2);
				m_matrix_rotate.postRotate( angle, TDStaticData.SCREEN_WIDTH/2, TDStaticData.SCREEN_HEIGHT/2);
				m_rotate=totalangle;
			    setLRwithMatrix();
				if(isEnd)
				{
					//结束时判断旋转角，然后重新载入图片，最后并修改媒体库的tag
					if(((int)totalangle)%360!=0)
					{
						//修改旋转角度
						mOperateBitmaps.updateOrientation( bitmapPicker.getCurrentFileName(), (int) totalangle);
				     	LoadImageTask.Callback callback = new LoadImageTask.Callback() {
						@Override
						public void onComplete(long time,Bitmap[] mBitmaps)
						{
							if(CSStaticData.DEBUG)
								Log.e(TAG, "生成图片用时"+time);
							
							 recycleDBitmap(m_curbitmap);
							 m_curbitmap = mBitmaps;
							 recycleDBitmap(m_tempbitmap);
						     m_tempbitmap=null;
							 resetMatrix();
							 invalidate();
							 WiImageViewerActivity.m_isNextBitmapLoaded--;
						}
			        };
			        LoadImageTask task=new LoadImageTask(context, callback,WiImageView.this);
					task.execute(bitmapPicker.getCurrentFileName());
					WiImageViewerActivity.m_isNextBitmapLoaded++;
				}
			}
		}
	}
  }
}

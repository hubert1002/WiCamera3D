package com.wistron.WiViewer;

import Utilities.CSStaticData;
import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class PanoramaView extends View
{
	int ScreenWidth;
	int ScreenHeight;
	int ImageWidth;
	int ImageHeight;
	boolean isMove;
	Bitmap myBitmap;
	Bitmap mBimapToShow;
	Canvas mCanvas;
	String TAG = "PanoramaView";
    int  move_Start=0;
    int  m_Currentposition;
    int move_X;
    public float m_sight_angle=120f;
   // private float mOrientationDegree=0;
    private static final int WIDTH =8;
    float halfWIDTH;
    private static final int HEIGHT = 6;
    float halfHEIGHT;
    private static final int COUNT = (WIDTH + 1) * (HEIGHT + 1);
    private final float[] mVerts = new float[COUNT*2];
    private final float[] mOrig = new float[COUNT*2];
    float x_rate=0;
    float y_rate=0;
    float rate;
    private Matrix matrix=new Matrix();
   // rotateAround mAround;
    public boolean m_UI_mode=true;
 //   float m_current_degree;
    float m_pre_degree;
 //   float m_temp_changed_degree;
    float m_total_changed_degree;
    float m_total_changed_pre_degree;
    boolean isValid=false;
    public void setSightAngle(float angle)
    {
    	m_sight_angle=angle;
    }
    
	public PanoramaView(Context context,int resource)
	{
		super(context);
		ScreenWidth = Panorama360Activity.screenw;
		ScreenHeight = Panorama360Activity.screenh;
		 halfWIDTH=WIDTH/2;
         halfHEIGHT=HEIGHT/2;
         x_rate=-0.06f;
         y_rate=4;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig=Config.RGB_565;
		try
		{
			myBitmap=BitmapFactory.decodeResource(context.getResources(), resource, options);
			if(myBitmap!=null)
			{
				ImageWidth = myBitmap.getWidth();
				ImageHeight = myBitmap.getHeight();
				if(ImageWidth!=0&&ImageHeight!=0)
				{
					   rate=((float)ScreenHeight)/((float)ImageHeight);
					  //	mBimapToShow=Bitmap.createBitmap(90000, 90000, Config.RGB_565);
						mBimapToShow=Bitmap.createBitmap((int)(ScreenWidth/rate), ImageHeight, Config.RGB_565);
						mCanvas=new Canvas(mBimapToShow);
			            // matrix.setTranslate(0,(ScreenHeight-ImageHeight)/2);
			            matrix.setScale(rate, rate);
			            isValid=true;
				}
			
			}
		} catch (OutOfMemoryError e)
		{
			if(CSStaticData.DEBUG)
				Log.e(TAG, "OutOfMemoryError");
			Log.e(TAG, "guale +++++++++++++++++++++++++++++");
		}
		getVers();
		move();
		
		if(CSStaticData.DEBUG)
			Log.e(TAG, "图片长度为"+(int) (ScreenWidth/rate-ImageWidth));
		
	}
	public PanoramaView(Context context,String filepath)
	{
		super(context);
		ScreenWidth = Panorama360Activity.screenw;
		ScreenHeight = Panorama360Activity.screenh;
		 halfWIDTH=WIDTH/2;
         halfHEIGHT=HEIGHT/2;
         x_rate=-0.06f;
         y_rate=4;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig=Config.RGB_565;
		try
		{
			myBitmap=BitmapFactory.decodeFile(filepath, options);
			if(myBitmap!=null)
			{
				ImageWidth = myBitmap.getWidth();
				ImageHeight = myBitmap.getHeight();
				if(ImageWidth!=0&&ImageHeight!=0)
				{
					rate=((float)ScreenHeight)/((float)ImageHeight);
					//mBimapToShow=Bitmap.createBitmap(20000, 10000, Config.ARGB_8888);
					mBimapToShow=Bitmap.createBitmap((int)(ScreenWidth/rate), ImageHeight, Config.RGB_565);
					mCanvas=new Canvas(mBimapToShow);
		            // matrix.setTranslate(0,(ScreenHeight-ImageHeight)/2);
		            matrix.setScale(rate, rate);
		            isValid=true;
				}
				
			}
		} catch (OutOfMemoryError e)
		{
			if(CSStaticData.DEBUG){
				Log.e(TAG, "==================");
				Log.e(TAG, "|OutOfMemoryError|");
				Log.e(TAG, "==================");
			}
			//isValid=false;
		}
		getVers();
		move();
		if(CSStaticData.DEBUG)
			Log.e(TAG, "图片长度为"+(int) (ScreenWidth/rate-ImageWidth));
		
	}
	
	public void switchUImode()
	{
		m_UI_mode=!m_UI_mode;
		invalidate();
	}
	public float getRate_Y(float y )
	{
		if(y<=halfHEIGHT)
		{
			return (-halfHEIGHT/(y_rate*halfWIDTH*halfWIDTH))/(y*2+1);
		}
		else {
			return (-halfHEIGHT/(y_rate*halfWIDTH*halfWIDTH))/((HEIGHT-y)*2+1);
		}
		
		
		}
	public float getLocation_Y(int x, int y,float rate)
	{	
		if(y<=halfHEIGHT)
		return rate*x*x-WIDTH*rate*x+y;
		else {
			return HEIGHT-getLocation_Y(x,HEIGHT-y,rate);
		}
	}

	public float getLocation_X(int x)
	{
	if(x<=halfWIDTH)
			//return	2*x-x*x/halfWIDTH;
		return x_rate*x*x-(x_rate*halfWIDTH-1)*x;
	else {
		return WIDTH-getLocation_X(WIDTH-x);
	}
	   
	}
	public void getVers()
	{
	       int index = 0;
	       float fx = (ScreenWidth/rate) / WIDTH;
	       float fy = ImageHeight  / HEIGHT;
	       for (int y = 0; y <= HEIGHT; y++) {
	    	    float rate_x=getRate_Y(y);
	           for (int x = 0; x <= WIDTH; x++) {
	        	   setXY(mVerts, index, getLocation_X(x)*fx, getLocation_Y(x, y,rate_x)*fy);
	               index += 1;
	           }
	       }
	}
    private static void setXY(float[] array, int index, float x, float y) {
        array[index*2 + 0] = x;
        array[index*2 + 1] = y;
    }
    @Override 
    protected void onDraw(Canvas canvas) {
    	 canvas.drawColor(0xFF000000);
         canvas.concat(matrix);
         if(mBimapToShow!=null)
         {
        	 if(m_UI_mode)
         	{
               canvas.drawBitmapMesh(mBimapToShow, WIDTH, HEIGHT, mVerts, 0,
                                     null, 0, null);
         	}
         	else
         	{
         		canvas.drawBitmap(mBimapToShow, 0, 0, null);
     		}
         }
    }
	public void  move()
	{
		if(mCanvas!=null)
		{
			mCanvas.drawColor(0xFF000000);
			if(myBitmap!=null)
			mCanvas.drawBitmap(myBitmap, m_Currentposition, 0, null);
			invalidate();
		}
	}
	public void setInitOrientation(float degree)
	{
		m_pre_degree=degree;
	}
	
	public float getChangeDegree(float average_degree,float current_degree)
	{
		if(CSStaticData.DEBUG)
		{
			//Log.e(TAG, "[getChangeDegree]-------------------------------->数据分析");
			//Log.e(TAG, "当前传入参数:average_degree="+average_degree+"current_degree"+current_degree);
		}
		
		float res=0;
		float temp=average_degree-m_pre_degree;
		if(Math.abs(temp)>50)
		{
			if(temp>0)
			{
				res=current_degree-m_pre_degree-360;
			}
			else
			{
				res=current_degree-m_pre_degree+360;
			}
			m_pre_degree=current_degree;
		}
		else
		{
			res=temp;
			m_pre_degree=average_degree;
		}
//		if(CSStaticData.DEBUG)
//		Log.e(TAG, "处理完成后:m_pre_degree="+m_pre_degree+"res="+res);
		return res;
	}
	public float updateUIWhenOrientationChanged(float degree ,float current_degree)
	{
		float m_temp_changed_degree=getChangeDegree(degree,current_degree);
		m_total_changed_degree+=m_temp_changed_degree;
		if(Math.abs(m_total_changed_degree-m_total_changed_pre_degree)>0.2)
		{
			if(!isMove)
			{
				float mr=((float)m_total_changed_degree)/m_sight_angle;
				m_Currentposition=(int) ((ScreenWidth/rate-ImageWidth)*mr);
				if(m_Currentposition<ScreenWidth/rate-ImageWidth)
				{
					m_Currentposition=(int) (ScreenWidth/rate-ImageWidth);
					m_total_changed_degree=m_sight_angle;
				}
				else if(m_Currentposition>0) {
					m_total_changed_degree=0;
					m_Currentposition=0;
				}
				move();
				m_total_changed_pre_degree=m_total_changed_degree;
			}
		}
		return m_pre_degree; 
	}
	public void release()
	{
		if(mBimapToShow!=null)
		{
			if(!mBimapToShow.isRecycled())
			{
				mBimapToShow.recycle();
			}
			mBimapToShow=null;
		}
		
		if(myBitmap!=null)
		{
			if(!myBitmap.isRecycled())
			{
				myBitmap.recycle();
			}
			myBitmap=null;
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		boolean state = false;
		if(CSStaticData.DEBUG)
			Log.e(TAG, "onTouchEvent");
		switch (ev.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			isMove=true;
			move_Start=(int) ev.getX();
			state = true;
			break;
		case MotionEvent.ACTION_MOVE:
			float mCurX = ev.getX();
			move_X = (int) (mCurX - move_Start);
			m_Currentposition=m_Currentposition+move_X;
			if(m_Currentposition<ScreenWidth/rate-ImageWidth)
			{
				m_Currentposition=(int) (ScreenWidth/rate-ImageWidth);
			}
			else if(m_Currentposition>0) {
				m_Currentposition=0;
			}

			if(CSStaticData.DEBUG)
				Log.e(TAG, "m_Currentposition"+m_Currentposition);

			move();
			move_Start=(int) mCurX;
			state = true;
			break;
		case MotionEvent.ACTION_UP:
			float tempRate=((float)m_Currentposition)/(float) (ScreenWidth/rate-ImageWidth);
			m_total_changed_degree=m_sight_angle*tempRate;
			isMove=false;
			move_X=0;
			state = true;

			break;
		}

		return state;
	}
}

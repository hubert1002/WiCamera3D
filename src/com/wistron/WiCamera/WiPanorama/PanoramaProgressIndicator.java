package com.wistron.WiCamera.WiPanorama;

import com.wistron.swpc.wicamera3dii.R;

import Utilities.CSStaticData;
import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
public class PanoramaProgressIndicator extends ImageView {
	Paint myPaint ;
	String TAG="PanoramaProgressIndicator";
    public static final int DIRECTION_NONE = 0;
    public static final int DIRECTION_LEFT = 1;
    public static final int DIRECTION_RIGHT = 2;
	int  viewwidth;
	int  viewheight;
	int  srcImageWidth;
	int  srcImageHeight;
	private  int  mDirection=0;
	Bitmap  backgroundImage;
	Bitmap  progressImage;
	Bitmap  backgroundWrongImage;
	int  mType=0;
	Rect  src;
	Rect des;
	int  progress;
	float  linestart;
    private final Paint mLinePaint = new Paint();
    private final Paint mLinePaint_ = new Paint();
    private boolean isToFast=false;
    private boolean isWrongPose=false;
	public PanoramaProgressIndicator(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
	    myPaint = new Paint();
		myPaint.setColor(Color.GREEN);
		myPaint.setStyle(Paint.Style.STROKE);
		myPaint.setStrokeWidth(3);
		myPaint.setAntiAlias(true);
		myPaint.setFilterBitmap(true);
		
		   mLinePaint.setStyle(Paint.Style.STROKE);
	        mLinePaint.setColor(Color.WHITE);
	        mLinePaint.setAntiAlias(true);
	        
	        mLinePaint_.setStyle(Paint.Style.FILL);
	        mLinePaint_.setColor(Color.WHITE);
	        mLinePaint_.setAntiAlias(true);
		
		viewwidth=350;
		viewheight=50;
		linestart=viewheight/2;
		backgroundWrongImage=BitmapFactory.decodeResource(context.getResources(), R.drawable.progress_panoramatest_error);
		backgroundImage=BitmapFactory.decodeResource(context.getResources(),R.drawable.background_panoramatest );
		progressImage=BitmapFactory.decodeResource(context.getResources(),R.drawable.progress_panoramatest );
		srcImageWidth=backgroundImage.getWidth();
		srcImageHeight=backgroundImage.getHeight();
		if(CSStaticData.DEBUG)
		Log.e(TAG, "控件长宽为"+srcImageWidth+"*"+srcImageHeight);
		src=new Rect(0, 0, srcImageWidth, srcImageHeight);
		des=new Rect(0, 0, viewwidth, viewheight);
	}
	public void setIsTofast(boolean bool )
	{
		isToFast=bool;
		invalidate();
	}
	public void reset()
	{
		isToFast=false;
		isWrongPose=false;
		mType=0;
		linestart=viewheight/2;
		mDirection=0;
		progress=0;
		src=new Rect(0, 0, srcImageWidth, srcImageHeight);
		des=new Rect(0, 0, viewwidth, viewheight);
	}
  public void setType(int type)
  {
	  mType=type;
  }
  
  /**
   * 更新保存进度
   * @param progress
   */
  public void  setProgress(int progress)
  {
	  if (progress > 100)
		{
			progress = 100;
		}
		if (progress < 0)
		{
			progress = 0;
		}
		this.progress = progress;
		mDirection = DIRECTION_NONE;
		invalidate();
  }
  /**
   * 更新拍摄进度
   * @param progress
   * @param derectionprogress
   * @return
   */
	public int setProgress(int progress, int derectionprogress)
	{
		if (mDirection == DIRECTION_NONE)
		{
			if (derectionprogress > 10)
			{
					mDirection = DIRECTION_RIGHT;
					return DIRECTION_RIGHT;

			} else if (derectionprogress < -10)
			{
				mDirection = DIRECTION_LEFT;
				return DIRECTION_LEFT;
			}
		} else
		{
			if (progress > 100)
			{
				progress = 100;
			}
			if (progress < 0)
			{
				progress = 0;
			}
			this.progress = progress;
			invalidate();
		}
		return DIRECTION_NONE;
	}
	public float getRate()
	{
		return 	((float)progress)/100f;
	}
	public void  convertSensor(double d )
	{
		double res=-d/10f*90;
		setlinerate(res);
	}
	public  void  setlinerate(double d )
	{
		float tan= (float) Math.tan(d);
		linestart=viewheight/2-tan*viewwidth/2;
		if(linestart<0)
		{
			linestart=0;
			isWrongPose=true;
		}
		else if(linestart>viewheight)
		{
			linestart=viewheight;
			isWrongPose=true;
		}
		else {
			isWrongPose=false;
		}
		invalidate();
	}
	public Rect getSrcRect(Rect src,float rate )
	{
		if(mDirection==DIRECTION_LEFT)
		{
			return	new Rect((int)(src.right*(1-rate)), 0,src.right , src.bottom);
		}
		else
		{
			return	new Rect(0, 0,(int) (src.right*rate) , src.bottom);
		}
	}
	public Rect getDesRect(Rect des,float rate )
	{
		if(mDirection==DIRECTION_LEFT)
		{
			return	new Rect((int)(des.right*(1-rate)), 0,des.right , des.bottom);
		}
		else
		{
			return	new Rect(0, 0,(int) (des.right*rate) , des.bottom);
		}
	}
	@Override
	protected void onDraw(Canvas canvas) {
		//画背景图片
		if(mType==0)
		{
			if(isToFast||isWrongPose)
			{
				canvas.drawBitmap(backgroundWrongImage, src, des, null);
			}
			else
			{
				canvas.drawBitmap(backgroundImage, src, des, null);
			}
		}
		else
		{
			canvas.drawBitmap(backgroundImage, src, des, null);
		}
		
		float rate_src= getRate();
		//画进度条
		canvas.drawBitmap(progressImage, getSrcRect(src,rate_src), getDesRect(des,rate_src), null);
		if(mType==0)
		{
			 canvas.drawLine(0, 0.5f*viewheight, viewwidth, 0.5f*viewheight, mLinePaint_);
			  Path path = new Path();       
		        path.moveTo(0, linestart);  
		        path.lineTo(viewwidth,viewheight-linestart);        
		        PathEffect effects = new DashPathEffect(new float[]{6,6,6,6},1);  
		        mLinePaint.setPathEffect(effects);  
		        canvas.drawPath(path, mLinePaint);  
			 
			 
			// canvas.drawLine(0, linestart, viewwidth, viewheight-linestart, mLinePaint);
		}
	    
     }
}

package com.wistron.WiViewer;


import Utilities.CSStaticData;
import android.R.integer;
import android.content.Context;
import android.graphics.Matrix;
import android.os.Build;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
  
public abstract class VersionedGestureDetector {
    private static final String TAG = "VersionedGestureDetector";
    OnGestureListener mListener;
    boolean canRotate=false;
    boolean isRotatable=false;
    float rotateAngle=0;
    float preAngle=0;
   // Matrix matrix_Rotate=new Matrix();
    /**
	 * 实例化多点识别器
	 */
    public static VersionedGestureDetector newInstance(Context context,
            OnGestureListener listener) {
        final int sdkVersion = Integer.parseInt(Build.VERSION.SDK);
        VersionedGestureDetector detector = null;
        if (sdkVersion < Build.VERSION_CODES.ECLAIR) {
            detector = new CupcakeDetector();
        } else if (sdkVersion < Build.VERSION_CODES.FROYO) {
            detector = new EclairDetector();
        } else {
            detector = new FroyoDetector(context);
        }
        
        Log.d(TAG, "Created new " + detector.getClass());
        detector.mListener = listener;
        
        return detector;
    }
    
    public abstract boolean onTouchEvent(MotionEvent ev);
    /**
	 * 手势监听器，监听缩放和拖動
	 */
    public interface OnGestureListener {
        public void onDrag(float dx, float dy);
        public void onScale(float scaleFactor,float x,float y,ScaleGestureDetector de);
        public void onRotate(float angle,float totalangle,boolean isEnd);
    }
public float getRestAngle(float srcAngle)
{
	float angle=0;
	while(srcAngle>=360)
	{
		srcAngle-=360;
	}
	while(srcAngle<=-360)
	{
		srcAngle+=360;
	}
	
	if(srcAngle>0)
	{
		if(srcAngle<45)
		{
			angle=-srcAngle;
		}
		else if(srcAngle<90)
		{
			angle=90-srcAngle;
		}
		else if( srcAngle<135)
		{
			angle=(90-srcAngle);
		}
		else if (srcAngle<180)
		{
			angle=180-srcAngle;
		}
		else if (srcAngle <225)
		{
			angle=(180-srcAngle);
		}
		else if (srcAngle<270)
		{
			angle=270-srcAngle;
		}
		else if (srcAngle<315) 
		{
		    angle=(270-srcAngle);	
		}
		else if( srcAngle<360)
		{
			angle=360-srcAngle;
		}
	}
	else 
	{
		if(srcAngle>-45)
		{
			angle=-srcAngle;
		}
		else if(srcAngle>-90)
		{
			angle=-(90+srcAngle);
		}
		else if( srcAngle>-135)
		{
			angle=(-90-srcAngle);
		}
		else if (srcAngle>-180)
		{
			angle=-180-srcAngle;
		}
		else if (srcAngle >-225)
		{
			angle=-180-srcAngle;
		}
		else if (srcAngle>-270)
		{
			angle=-270-srcAngle;
		}
		else if (srcAngle>-315) 
		{
			angle=-270-srcAngle;
		}
		else if( srcAngle>-360)
		{
			angle=-360-srcAngle;
		}
	}
	
	return angle;
	
	}
public float getRotateAngle(float x,float y,float x_,float y_)
   {
	if(x_==x)
	{
		if(y_>y)
			return 90;
		else {
			return 270;
		}
	}
	if( y==y_)
	{
		if(x_<x)
			return 180;
		else {
			return  0;
		}
	}
	float angle=(float)Math.toDegrees(Math.atan((y_-y)/(x_-x)));
    	if( x_<x)
    	{
    		angle+=180;
    	}
    	else  {
    		if(y_<y)
    		{
    			angle+=360;
    		}
    		
		}
		//Log.e(TAG, "angle="+angle);
	return angle ;
	
	
	}
    private static class CupcakeDetector extends VersionedGestureDetector {
        float mLastTouchX;
        float mLastTouchY;
        
        float getActiveX(MotionEvent ev) {
            return ev.getX();
        }

        float getActiveY(MotionEvent ev) {
            return ev.getY();
        }
        
        boolean shouldDrag() {
            return true;
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mLastTouchX = getActiveX(ev);
                mLastTouchY = getActiveY(ev);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                final float x = getActiveX(ev);
                final float y = getActiveY(ev);
                
                if (shouldDrag()) {
                    mListener.onDrag(x - mLastTouchX, y - mLastTouchY);
                }
                
                mLastTouchX = x;
                mLastTouchY = y;
                break;
            }
            }
            return true;
        }
    }
    
    private static class EclairDetector extends CupcakeDetector {
        private static final int INVALID_POINTER_ID = -1;
        private int mActivePointerId = INVALID_POINTER_ID;
        private int mActivePointerIndex = 0;

        @Override
        float getActiveX(MotionEvent ev) {
        	float  mf=0;
        	try
			{
        		mf= ev.getX(mActivePointerIndex);
			} catch (Exception e)
			{
				// TODO: handle exception
				e.printStackTrace();
			}
           return mf;
        }

        @Override
        float getActiveY(MotionEvent ev) {
        	float  mf=0;
        	try
			{
				mf= ev.getY(mActivePointerIndex);;
			} catch (Exception e)
			{
				// TODO: handle exception
				e.printStackTrace();
			}
            return mf;
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev)
        {
        	
            final int action = ev.getAction();
            switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = ev.getPointerId(0);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mActivePointerId = INVALID_POINTER_ID;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) 
                        >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = ev.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mActivePointerId = ev.getPointerId(newPointerIndex);
                    mLastTouchX = ev.getX(newPointerIndex);
                    mLastTouchY = ev.getY(newPointerIndex);
                }
                break;
            }
            mActivePointerIndex = ev.findPointerIndex(mActivePointerId);
            return super.onTouchEvent(ev);
        }
    }

    private static class FroyoDetector extends EclairDetector {
        private ScaleGestureDetector mDetector;
        
        public FroyoDetector(Context context) {
        	if(CSStaticData.DEBUG)
        	Log.e(TAG, "FroyoDetector");
            mDetector = new ScaleGestureDetector(context,
                    new ScaleGestureDetector.SimpleOnScaleGestureListener()
            {
				@Override
                public boolean onScale(ScaleGestureDetector detector) {
                	
                   mListener.onScale(detector.getScaleFactor(),detector.getFocusX(),detector.getFocusY(),detector);
                    return true;
                }
            });
        }
        @Override
        boolean shouldDrag() {
           return !mDetector.isInProgress();
        //	return true;
        }
        
        @Override
        public boolean onTouchEvent(MotionEvent ev) 
        {
        	switch (ev.getAction())
			{
			case MotionEvent.ACTION_MOVE:
				
				if(canRotate)
	        	{
				    float curangle=getRotateAngle(ev.getX(0),ev.getY(0), ev.getX(1), ev.getY(1));
				    float temp= curangle-preAngle;
				    float tempfortotal=temp;
				    if(temp>300)
				    {
				    	tempfortotal=temp-360;
				    }
				    else if( temp<-300)
				    {
				    	tempfortotal=360+temp;
					}
				    rotateAngle+=tempfortotal;
				//  Log.e(TAG, "rotateAngle="+rotateAngle);
				    if(!isRotatable)
				    {
				    	 int angleTrigger=Math.abs((int)rotateAngle)%90;
				    	if(angleTrigger>30)
					    {
				    		//Log.e(TAG, "isRotatable");
				    		isRotatable=true;
				    		rotateAngle=tempfortotal;
					    }
				    }
				    if(isRotatable)
                    {
                    	mListener.onRotate(temp,rotateAngle,false);
                    }
	        		preAngle=curangle;
	        	}
				break;
           case MotionEvent.ACTION_CANCEL:
           case MotionEvent.ACTION_UP:
                //Log.e(TAG, "ACTION_UP");
        	   canRotate=false;
        	   if(isRotatable)
        	   {
        		   isRotatable=false;
        		   float resetAngle=getRestAngle(rotateAngle);
        		   rotateAngle=rotateAngle+resetAngle;
        		   mListener.onRotate(resetAngle,rotateAngle,true);
        		  // Log.e(TAG, "resetAngle"+resetAngle);
        		 //  Log.e(TAG, "rotateAngle"+rotateAngle);
        	   }
        	   rotateAngle=0;
				break;
           case MotionEvent.ACTION_POINTER_UP:
           case MotionEvent.ACTION_POINTER_2_UP:
        	  // Log.e(TAG, "ACTION_POINTER_UP");
        	    canRotate=false;
        	   // isRotatable=false;
        	    //rotateAngle=0;
        	    break;
           case MotionEvent.ACTION_POINTER_2_DOWN:
        	    canRotate=true;
        	    preAngle=getRotateAngle(ev.getX(0),ev.getY(0), ev.getX(1), ev.getY(1));
        	    break;
			}
        	
        	
            mDetector.onTouchEvent(ev);
            return super.onTouchEvent(ev);
        }
    }
}


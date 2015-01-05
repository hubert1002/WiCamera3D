package com.wistron.WiCamera.WiPanorama;

import android.R.integer;

public  class RotateAround 
{
	boolean[] degreeA={false,false,false,false};
	float startOrientation=0;
	public static int DIRECT_NONE=0;
	public static int DIRECT_RIGHT=1;
	public static int DIRECT_LEFT=-1;
	int mDirect=0;//向右旋转
	public RotateAround()
	{
		
	}
	public void setDirection(int direct )
	{
		mDirect=direct;
	}
	public void setStartOrientation(float degree)
	{
		startOrientation=degree;
	}
	public boolean onOrientationChanged(float degree)
	{
		if(degree>=0&&degree<90)
		{
			degreeA[0]=true;
		}
		else if(degree>=90&&degree<180) {
			degreeA[1]=true;
		}
		else if(degree>=180&&degree<270) {
			degreeA[2]=true;
		}
        else if(degree>=270&&degree<360) {
        	degreeA[3]=true;
		}
		boolean res=true;
		for (int i = 0; i < degreeA.length; i++)
		{
			res=res&&degreeA[i];
		}
		if(res)
		{
			if(mDirect==DIRECT_RIGHT)
			{
				if( startOrientation>=350)
				{
					if(degree<10)
					{
						return true;
					}
				}
				else {
					if(degree>startOrientation&&degree<startOrientation+10)
					{
						return true;
					}
				}
				
			}
			else if(mDirect==DIRECT_LEFT)
			{
				
				if(startOrientation<=10)
				{
					if(degree>350)
					{
						return true;
					}
				}
				else {
					if(degree<startOrientation&&degree>startOrientation-10)
					{
						return true;
					}
				}
				
			}
			
		}
		return false;
	}
 public void resetToInit()
 {
	 for (int i = 0; i < degreeA.length; i++)
	{
		 degreeA[i]=false;
	}
	startOrientation=0;
    mDirect=DIRECT_NONE;
 }
}
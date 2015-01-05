package com.wistron.WiViewer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class Video2DService extends VideoView implements VideoService
{
	public Video2DService(Context context)
	{
		super(context);
	}
	public Video2DService(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void force3DMode(int mode)
	{
		// TODO Auto-generated method stub
	}


}

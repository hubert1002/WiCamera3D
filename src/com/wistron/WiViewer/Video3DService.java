package com.wistron.WiViewer;

import com.tridef3d.widget.VideoView3D;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class Video3DService extends VideoView3D implements VideoService
{

	public Video3DService(Context context)
	{
		super(context);
	}
	public Video3DService(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}


}

package com.wistron.WiViewer;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;

import com.tridef3d.view.TextureView3D;


public class MyVideoView extends TextureView3D
{
	String TAG="MyVideoView";
	MediaPlayer mMediaPlayer=null;
	 MediaPlayer.OnVideoSizeChangedListener mVideoSizeChangedOut=null;
	int mVideoWidth=0;
	int mVideoHeight=0;
    int mMeasuredWidth=0;
    int mMeasuredHeight=0;
    Context mContext;
    int mVideoLayout;
    TextureView.SurfaceTextureListener outside;
	public MyVideoView(Context context)
	{
		super(context);
		 initVideoView3D(context);
		// TODO Auto-generated constructor stub
	}
	public MyVideoView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		 initVideoView3D(context);
		// TODO Auto-generated constructor stub
	}
	  public MyVideoView(Context context, AttributeSet attrs, int defStyle) {
		    super(context, attrs, defStyle);
		    initVideoView3D(context);
	 }
	public void   initVideoView3D(Context context)
	{
			    this.mContext = context;
			    this.mVideoWidth = 0;
			    this.mVideoHeight = 0;
			    this.mVideoLayout = 0;
			    this.mMeasuredWidth = 0;
			    this.mMeasuredHeight = 0;
			    setSurfaceTextureListener(this.mSTCallback);
			    setFocusable(true);
			    setFocusableInTouchMode(true);
			    requestFocus();
	}
	public void setMediaPlayer(MediaPlayer mp)
	{
		this.mMediaPlayer=mp;
	}
	public void setOnVideoSizeChangedListenerOut(MediaPlayer.OnVideoSizeChangedListener sc)
	{
		mVideoSizeChangedOut=sc;
	}
	  MediaPlayer.OnVideoSizeChangedListener mSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener()
	  {
	    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
	    	if(mVideoSizeChangedOut!=null)
	    		mVideoSizeChangedOut.onVideoSizeChanged(mp, width, height);
	      if ((mp.getVideoWidth() == 0) || (mp.getVideoHeight() == 0))
	        return;
	      requestLayout();
	      invalidate();
	    }
	  };
	public void setMySurfaceTextureListener(TextureView.SurfaceTextureListener outside)
	{
		this.outside=outside;
	}
	public void setVideoSize(int width,int height)
	{
		this.mVideoWidth=width;
		this.mVideoHeight=height;
	}
//	public void invalidateView(boolean bool)
//	{
//		 requestLayout();
//		 if(bool)
//		 {
//			 invalidate();
//		 }
//		 
//	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		//super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		Log.e(TAG, "onMeasure");
	    double aspect = (this.mImage == null) ? 1.0D : this.mImage.getPixelAspectRatio();
	    int width = getDefaultSize((int)(this.mVideoWidth * aspect), widthMeasureSpec);
	    int height = getDefaultSize(this.mVideoHeight, heightMeasureSpec);
	    if ((this.mVideoWidth > 0) && (this.mVideoHeight > 0)) {
	      if (this.mVideoWidth * aspect * height > width * this.mVideoHeight)
	      {
	        height = (int)(width * this.mVideoHeight / (this.mVideoWidth * aspect));
	      } else if (this.mVideoWidth * aspect * height < width * this.mVideoHeight)
	      {
	        width = (int)(height * this.mVideoWidth * aspect / this.mVideoHeight);
	      }

	    }
	    this.mMeasuredWidth = width;
	    this.mMeasuredHeight = height;
	    setMeasuredDimension(width, height);
	    Log.e(TAG, "setMeasuredDimension---width"+width);
	    Log.e(TAG, "setMeasuredDimension---height"+height);
	}
	 TextureView.SurfaceTextureListener mSTCallback = new TextureView.SurfaceTextureListener()
	  {

		@Override
		public void onSurfaceTextureAvailable(SurfaceTexture surface,
				int width, int height)
		{
			// TODO Auto-generated method stub
			if(outside!=null)
			{
				outside.onSurfaceTextureAvailable(surface, width, height);
			}
			if(mMediaPlayer!=null&&mVideoSizeChangedOut!=null)
			{
				mMediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
			}
		}

		@Override
		public boolean onSurfaceTextureDestroyed(SurfaceTexture surface)
		{
			// TODO Auto-generated method stub
			if(outside!=null)
			{
				return outside.onSurfaceTextureDestroyed(surface);
			}
			return false;
		}

		@Override
		public void onSurfaceTextureSizeChanged(SurfaceTexture surface,
				int width, int height)
		{
			// TODO Auto-generated method stub
			
			if(outside!=null)
			{
				outside.onSurfaceTextureSizeChanged(surface, width, height);
			}
		}

		@Override
		public void onSurfaceTextureUpdated(SurfaceTexture surface)
		{
			// TODO Auto-generated method stub
			if(outside!=null)
			{
				outside.onSurfaceTextureUpdated(surface);
			}
		}
	  };
	
	

}

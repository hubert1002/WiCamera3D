package com.wistron.WiCamera.WiPanorama;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.List;

import com.wistron.WiCamera.SoundPlayer;
import com.wistron.WiViewer.WiImageView;
import com.wistron.swpc.wicamera3dii.R;import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.wistron.swpc.wicamera3dii.R;

//1.如果为3D则需要重算最佳预览尺寸，现在还没有添加
//2.reportprogress也要修改
public class WiPanoramaActivity extends Activity implements PreviewCallback,
		SurfaceHolder.Callback,SensorEventListener
{
	/** Called when the activity is first created. */
	Camera mCameraDevice;
	public static final int DEFAULT_SWEEP_ANGLE = 160;
	public static final int DEFAULT_BLEND_MODE = Mosaic.BLENDTYPE_HORIZONTAL;
	public static final int DEFAULT_CAPTURE_PIXELS = 960 * 720;
	public AlertDialog mAlertDialog;
	private static final int MSG_LOW_RES_FINAL_MOSAIC_READY = 1;
	private static final int MSG_RESET_TO_PREVIEW_WITH_THUMBNAIL = 2;
	private static final int MSG_GENERATE_FINAL_MOSAIC_ERROR = 3;
	private static final int MSG_RESET_TO_PREVIEW = 4;
	private static final int MSG_TURNTO_REVIEW = 5;
	private boolean mThreadRunning;
	private static final String TAG = "PanoramaActivity";
	private static final int CAPTURE_STATE_VIEWFINDER = 0;
	private static final int CAPTURE_STATE_MOSAIC = 1;
	private Handler mMainHandler;
	boolean flag_reportprogress = true;
	private MBroadcastReceiver mbroadcastReceiver;
	// Speed is in unit of deg/sec
	// 記錄當前幀數，用於判斷是否新加入了幀
	private int currentFrameCount = 0;
	private int mCaptureState;
	// Ratio of nanosecond to second
	private long mTimeTaken;
	int mPreviewWidth;
	int mPreviewHeight;
	TextView mText;
	SurfaceView surfaceView;
	SurfaceHolder surfaceHolder;
	ImageView mImageView;
	private float mHorizontalViewAngle;
	private float[] mTransformMatrix;
	private MosaicFrameProcessor mMosaicFrameProcessor;
    private Button  m_button;
    private boolean m_is3D=false;
    PanoramaProgressIndicator mPanoramaProgressIndicator;
    private  RelativeLayout mPanoProgressLayout;
    private  ImageView mPanoProgressLeft;
    private  ImageView mPanoProgressRight;
    private static final String VIDEO_RECORD_SOUND = "/system/media/audio/ui/VideoRecord.ogg";
    private SoundPlayer mRecordSound;
    private  SensorManager sensorManager;
    
    @Override
    protected void onPause() {
    	super.onPause();
        releaseSoundRecorder();
    	if (sensorManager != null) {
		sensorManager.unregisterListener(this);
	}
    }
    @Override
    public void onResume()
    {
    	super.onResume();
    	
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensors = sensorManager
				.getSensorList(Sensor.TYPE_ORIENTATION);
        if (sensors.size() > 0) {
			sensorManager.registerListener(this, sensors.get(0),SensorManager.SENSOR_DELAY_UI);
		}
    	
    	initSoundRecorder();
    }
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.wipanoramaactivity_main);

		m_button=(Button) findViewById(R.id.button);
		mPanoProgressLayout=(RelativeLayout) findViewById(R.id.panoprogress_relativelayout);
		mPanoramaProgressIndicator=(PanoramaProgressIndicator) findViewById(R.id.panoramaprogressIndicator);
		mPanoProgressLeft=(ImageView) findViewById(R.id.panoprogressleft);
		mPanoProgressRight=(ImageView) findViewById(R.id.panoprogressright);
		mPanoProgressLayout.setVisibility(View.INVISIBLE);
		m_button.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				if (mThreadRunning)
				{
					mAlertDialog.show();
					return ;
				}
				switch (mCaptureState)
				{
				case CAPTURE_STATE_VIEWFINDER:
					startCapture();
					break;
				case CAPTURE_STATE_MOSAIC:
					stopCapture(false);
					break;
				}
			}
		});
		mTransformMatrix = new float[16];
		surfaceView = (SurfaceView) this.findViewById(R.id.surface);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surfaceHolder.addCallback(this);
		mImageView = (ImageView) findViewById(R.id.imageView1);
		mImageView.setVisibility(View.GONE);
		mText = (TextView) findViewById(R.id.shenma);
		mText.setVisibility(View.INVISIBLE);
		mMainHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				switch (msg.what)
				{
				case MSG_LOW_RES_FINAL_MOSAIC_READY:
					Log.e("MSG_LOW_RES_FINAL_MOSAIC_READY", "ok");
					onBackgroundThreadFinished();
					showFinalMosaic((Bitmap) msg.obj);
					saveHighResMosaic();
					reportProgress();
					break;
				case MSG_RESET_TO_PREVIEW_WITH_THUMBNAIL:
					onBackgroundThreadFinished();

					break;
				case MSG_TURNTO_REVIEW:

					break;
				case MSG_GENERATE_FINAL_MOSAIC_ERROR:
					onBackgroundThreadFinished();

					break;
				case MSG_RESET_TO_PREVIEW:
					Log.e(TAG, "MSG_RESET_TO_PREVIEW");
					onBackgroundThreadFinished();
					reset();
					mImageView.setVisibility(View.GONE);
					startCameraPreview();
					mText.setText("start another one ");
				}
				// clearMosaicFrameProcessorIfNeeded();
			}
		};

		mAlertDialog = (new AlertDialog.Builder(this))
				.setTitle("be careful")
				.setMessage(
						"something is running in background,do you want to quit?")
				.create();
		mAlertDialog.setCancelable(true);
		mAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "ok",
				new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
						try
						{
							resetToPreview();
						} catch (Exception e)
						{
							// TODO: handle exception
							Log.e("mAlertDialog click ok ",
									"failed in process resetToPreview");
						}

					}
				});
		mAlertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "cancel",
				new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				});
	    resetPanoProgressToInit();
	    mPanoProgressLayout.setVisibility(View.VISIBLE);
		Log.e("oncreate", "over");
		// registerSDMount();
	}

	private void registerSDMount()
	{
		Log.e(TAG, "registerSDMount+++++++++++++++++++++++");
		/*
		 * IntentFilter intentFilter = new IntentFilter(
		 * android.hardware.Camera.ACTION_NEW_PICTURE);
		 * intentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
		 */
		IntentFilter intentFilter = new IntentFilter(
				Intent.ACTION_MEDIA_MOUNTED);
		intentFilter.addDataScheme("file");
		mbroadcastReceiver = new MBroadcastReceiver();
		registerReceiver(mbroadcastReceiver, intentFilter);// 注册监听函数

	}
    private void initSoundRecorder() {
        // Construct sound player; use enforced sound output if necessary
        File recordSoundFile = new File(VIDEO_RECORD_SOUND);
        try {
            ParcelFileDescriptor recordSoundParcel =
                    ParcelFileDescriptor.open(recordSoundFile,
                            ParcelFileDescriptor.MODE_READ_ONLY);
            AssetFileDescriptor recordSoundAsset =
                    new AssetFileDescriptor(recordSoundParcel, 0,
                                            AssetFileDescriptor.UNKNOWN_LENGTH);
                mRecordSound = new SoundPlayer(recordSoundAsset, true,0);
        } catch (java.io.FileNotFoundException e) {
            Log.e(TAG, "System video record sound not found");
            mRecordSound = null;
        }
    }

    private void releaseSoundRecorder() {
        if (mRecordSound != null) {
            mRecordSound.release();
            mRecordSound = null;
        }
    }

	private void resetToPreview()
	{
		reset();
		startCameraPreview();
	}

	private void clearMosaicFrameProcessorIfNeeded()
	{
		if (mThreadRunning)
			return;
		mMosaicFrameProcessor.clear(m_is3D);

	}

	private void reset()
	{
		mCaptureState = CAPTURE_STATE_VIEWFINDER;
		currentFrameCount=0;
		flag_reportprogress=true;
		mMosaicFrameProcessor.reset();

	}

	public void saveHighResMosaic()
	{
		runBackgroundThread(new Thread()
		{
			@Override
			public void run()
			{
				MosaicJpeg jpeg = generateFinalMosaic(true, true);
				if (jpeg == null)
				{ 
					mMainHandler.sendEmptyMessage(MSG_RESET_TO_PREVIEW);
				} else if (!jpeg.isValid)
				{ 
					mMainHandler
							.sendEmptyMessage(MSG_GENERATE_FINAL_MOSAIC_ERROR);
				} else
				{
					if(m_is3D)
					{
						savePanorama(jpeg.data, jpeg.width, jpeg.height, 0, 0);
						MosaicJpeg jpeg2 = generateFinalMosaic(true, false);
						if (jpeg2 == null)
						{ 
							mMainHandler.sendEmptyMessage(MSG_RESET_TO_PREVIEW);
						} else if (!jpeg2.isValid)
						{ 
							mMainHandler
									.sendEmptyMessage(MSG_GENERATE_FINAL_MOSAIC_ERROR);
						} else
						{
							savePanorama(jpeg2.data, jpeg2.width, jpeg2.height, 0, 1);
							mMainHandler.sendMessage(mMainHandler
									.obtainMessage(MSG_RESET_TO_PREVIEW));
						}
					}
					else
					{
						savePanorama(jpeg.data, jpeg.width, jpeg.height, 0, 2);
						mMainHandler.sendMessage(mMainHandler
								.obtainMessage(MSG_RESET_TO_PREVIEW));
					}
					
				}
			}
		});

	}
	private String savePanorama(byte[] jpegData, int width, int height,
			int orientation, int tag)
	{
		Log.e("savePanorama", "start");
		if (jpegData != null)
		{
			String imagePath;
			if (tag == 0)
			{
				imagePath = PanoUtil.createName("dd-hh-mm-ss", mTimeTaken)
						+ "L";
			} else if(tag==1)
			{
				imagePath = PanoUtil.createName("dd-hh-mm-ss", mTimeTaken)
						+ "R";
			}
			else {
				imagePath = PanoUtil.createName("dd-hh-mm-ss", mTimeTaken);
			}
			return Storage.addImage(this, this.getContentResolver(), imagePath,
					mTimeTaken, null, orientation, jpegData, width, height);
		}
		return null;
	}

	private void onBackgroundThreadFinished()
	{
		mThreadRunning = false;

	}

	private void showFinalMosaic(Bitmap bitmap)
	{
		if (bitmap != null)
		{
			mImageView.setImageBitmap(bitmap);
		}
	}

	private boolean findBestPreviewSize(List<Size> supportedSizes,
			boolean need4To3, boolean needSmaller)
	{
		int pixelsDiff = DEFAULT_CAPTURE_PIXELS;
		boolean hasFound = false;
		for (Size size : supportedSizes)
		{
			int h = size.height;
			int w = size.width;
			// we only want 4:3 format.
			int d = DEFAULT_CAPTURE_PIXELS - h * w;
			if (needSmaller && d < 0)
			{ // no bigger preview than 960x720.
				continue;
			}
			if (need4To3 && (h * 4 != w * 3))
			{
				continue;
			}
			d = Math.abs(d);
			if (d < pixelsDiff)
			{
				mPreviewWidth = w;
				mPreviewHeight = h;
				pixelsDiff = d;
				hasFound = true;
			}
		}
		return hasFound;
	}

	private void initMosaicFrameProcessorIfNeeded()
	{
		Log.e("initMosaicFrameProcessorIfNeeded", "ok");
		if(m_is3D)
		{
			if (mMosaicFrameProcessor == null)
			{
				mMosaicFrameProcessor = new MosaicFrameProcessor(mPreviewWidth / 2,
						mPreviewHeight, getPreviewBufSize() / 2);
			}
		}
		else
		{
			if (mMosaicFrameProcessor == null)
			{
				mMosaicFrameProcessor = new MosaicFrameProcessor(mPreviewWidth,
						mPreviewHeight, getPreviewBufSize());
			}
		}
		mMosaicFrameProcessor.initialize(m_is3D);
	}

	public int getPreviewBufSize()
	{
		PixelFormat pixelInfo = new PixelFormat();
		PixelFormat.getPixelFormatInfo(mCameraDevice.getParameters()
				.getPreviewFormat(), pixelInfo);
		// TODO: remove this extra 32 byte after the driver bug is fixed.
		return (mPreviewWidth * mPreviewHeight * pixelInfo.bitsPerPixel / 8) + 32;
	}

	private void startCameraPreview()
	{
		// If we're previewing already, stop the preview first (this will blank
		// the screen).
		Log.e("startCameraPreview", "ok");
		try
		{
			mCameraDevice.startPreview();
		} catch (Throwable ex)
		{
			// mCameraDevice.release();
		}

	}
	public void runMosaicCapture(byte[][] prebyte)
	{
		mMosaicFrameProcessor.processFrame(prebyte[0], prebyte[1]);
	}

	public void runMosaicCapture2D(byte[] prebyte)
	{
		mMosaicFrameProcessor.processFrame(prebyte);
	}

	public void startCapture()
	{
		 if (mRecordSound != null) mRecordSound.play();
		Log.e("startCapture", "ok");
	    resetPanoProgressToInit();
	    mPanoProgressLayout.setVisibility(View.VISIBLE);
	    m_button.setBackgroundResource(R.drawable.panorama_stop);
		mTimeTaken = System.currentTimeMillis();
		mCaptureState = CAPTURE_STATE_MOSAIC;
		mMosaicFrameProcessor
				.setProgressListener(new MosaicFrameProcessor.ProgressListener()
				{
					@Override
					public void onProgress(boolean isFinished,
							float panningRateX, float panningRateY,
							float progressX, float progressY)
					{
						 if(isFinished)
			              {
			            	  stopCapture(false);
			              }
			              else {
			            	  updateProgress(panningRateX, progressX, progressY);
						}
						
					}
				});

	}

	private void updateProgress(float panningRate, float progressX,
			float progressY)
	{
		if (Math.abs(mHorizontalViewAngle * panningRate) > 20)
			mPanoramaProgressIndicator.setIsTofast(true);
		else
		{
			mPanoramaProgressIndicator.setIsTofast(false);
		}
		
		if (currentFrameCount < mMosaicFrameProcessor.curframe)
		{
//由于中帧数为200，而进度条最大进度为100，所以要缩小
			mText.setText("current frame count: "+ mMosaicFrameProcessor.curframe);
		    int derection=	mPanoramaProgressIndicator.setProgress(mMosaicFrameProcessor.curframe/2,(int) (progressX * mHorizontalViewAngle));
			if(derection==PanoramaProgressIndicator.DIRECTION_LEFT)
			{
			   mPanoProgressLeft.setBackgroundResource(R.drawable.l_arrow);
			}
			else if (derection==PanoramaProgressIndicator.DIRECTION_RIGHT) {
				mPanoProgressRight.setBackgroundResource(R.drawable.r_arrow);
			}
		    currentFrameCount = mMosaicFrameProcessor.curframe;
		}

	}

	private void runBackgroundThread(Thread thread)
	{
		mThreadRunning = true;
		thread.start();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		// Stop the capturing first.
		if (mCaptureState == CAPTURE_STATE_MOSAIC)
		{
			stopCapture(true);
			reset();
		}
		releaseCamera();
		clearMosaicFrameProcessorIfNeeded();
		System.gc();
		// unregisterReceiver(mbroadcastReceiver);
	}

	private void releaseCamera()
	{
		if (mCameraDevice != null)
		{
			mCameraDevice.setPreviewCallbackWithBuffer(null);
			mCameraDevice.release();
			mCameraDevice = null;
		}
	}

	private void stopCapture(boolean aborted)
	{
		 if (mRecordSound != null) mRecordSound.play();
		Log.e("stopCapture", "ok");
		//mImageView.setVisibility(View.VISIBLE);
		mCaptureState = CAPTURE_STATE_VIEWFINDER;
		 m_button.setBackgroundResource(R.drawable.panorama_stop_click);
		mMosaicFrameProcessor.setProgressListener(null);
		mCameraDevice.stopPreview();
		runBackgroundThread(new Thread()
		{
			@Override
			public void run()
			{
				MosaicJpeg jpeg = generateFinalMosaic(false, false);
				if (jpeg != null && jpeg.isValid)
				{
					Bitmap bitmap = null;
					bitmap = BitmapFactory.decodeByteArray(jpeg.data, 0,
							jpeg.data.length);
					mMainHandler.sendMessage(mMainHandler.obtainMessage(
							MSG_LOW_RES_FINAL_MOSAIC_READY, bitmap));
				} else
				{
					mMainHandler.sendMessage(mMainHandler
							.obtainMessage(MSG_RESET_TO_PREVIEW));
				}
			}
		});

	}

	public MosaicJpeg generateFinalMosaic(boolean highRes, boolean isleft)
	{
		byte[] imageData;
		if (isleft)
		{
			// 创建左边的图片
			if (mMosaicFrameProcessor.createMosaic(highRes, true) == Mosaic.MOSAIC_RET_CANCELLED)
			{
				Log.e(TAG, "failed to generateFinalMosaic(left image)");
				return null;
			}
			imageData = mMosaicFrameProcessor.getFinalMosaicNV21();
		} else
		{
			// 创建右边的图片
			if (mMosaicFrameProcessor.createMosaic(highRes, false) == Mosaic.MOSAIC_RET_CANCELLED)
			{
				Log.e(TAG, "failed to generateFinalMosaic(left image)");
				return null;
			}
			imageData = mMosaicFrameProcessor.getFinalMosaicNV21();
		}

		if (imageData == null)
		{
			Log.e(TAG, "getFinalMosaicNV21() returned null.");
			return new MosaicJpeg();
		}
		int len = imageData.length - 8;
		int width = (imageData[len + 0] << 24)
				+ ((imageData[len + 1] & 0xFF) << 16)
				+ ((imageData[len + 2] & 0xFF) << 8)
				+ (imageData[len + 3] & 0xFF);
		int height = (imageData[len + 4] << 24)
				+ ((imageData[len + 5] & 0xFF) << 16)
				+ ((imageData[len + 6] & 0xFF) << 8)
				+ (imageData[len + 7] & 0xFF);
		Log.v(TAG, "ImLength = " + (len) + ", W = " + width + ", H = " + height);

		if (width <= 0 || height <= 0)
		{
			Log.e(TAG, "width|height <= 0!!, len = " + (len) + ", W = " + width
					+ ", H = " + height);
			return new MosaicJpeg();
		}
		YuvImage yuvimage = new YuvImage(imageData, ImageFormat.NV21, width,
				height, null);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		yuvimage.compressToJpeg(new Rect(0, 0, width, height), 100, out);
		try
		{
			out.close();
		} catch (Exception e)
		{
			Log.e(TAG, "Exception in storing final mosaic", e);
			return new MosaicJpeg();
		}
		return new MosaicJpeg(out.toByteArray(), width, height);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		return super.dispatchTouchEvent(ev);
	}

	private class MosaicJpeg
	{
		public MosaicJpeg(byte[] data, int width, int height)
		{
			this.data = data;
			this.width = width;
			this.height = height;
			this.isValid = true;
		}

		public MosaicJpeg()
		{
			this.data = null;
			this.width = 0;
			this.height = 0;
			this.isValid = false;
		}

		public final byte[] data;
		public final int width;
		public final int height;
		public final boolean isValid;
	}

	public void resetPanoProgressToInit()
	{
		mPanoramaProgressIndicator.reset();
		mPanoProgressLeft.setBackgroundResource(R.drawable.l_arrow_disappear);
		mPanoProgressRight.setBackgroundResource(R.drawable.r_arrow_disappear);
		mPanoProgressLeft.setVisibility(View.VISIBLE);
		mPanoProgressRight.setVisibility(View.VISIBLE);
		mPanoProgressLayout.setVisibility(View.INVISIBLE);
	}
	public void resetPanoProgressToReport()
	{
		mPanoProgressLeft.setVisibility(View.INVISIBLE);
		mPanoProgressRight.setVisibility(View.INVISIBLE);
		mPanoProgressLayout.setVisibility(View.VISIBLE);
		mPanoramaProgressIndicator.reset();
		mPanoramaProgressIndicator.setType(1);
	}
	
	public void reportProgress()
	{
		resetPanoProgressToInit();
		resetPanoProgressToReport();
		Thread t = new Thread()
		{
			@Override
			public void run()
			{
				while (flag_reportprogress)
				{
					final int progress = mMosaicFrameProcessor.reportProgress(
							true, false);
					try
					{
						Thread.sleep(50);
					} catch (Exception e)
					{

					}
					runOnUiThread(new Runnable()
					{
						public void run()
						{

							if (progress > 99)
							{
								flag_reportprogress = false;
								resetPanoProgressToInit();
								mPanoProgressLayout.setVisibility(View.VISIBLE);
							} else
							{
								mText.setText(" save progress:" + progress);
								mPanoramaProgressIndicator.setProgress(progress,20);
							}

						}
					});
				}
			}
		};
		t.start();
	}

	public static byte[][] rawoperation(byte[] data, int w, int h)
	{
		byte[][] m = new byte[2][1];
		m[0] = new byte[(int) (w / 2 * h * 3 / 2)];
		m[1] = new byte[(int) (w / 2 * h * 3 / 2)];
		int index = 0;
		int index1 = 0;
		int index2 = 0;
		int data_length = data.length;
		while (index < data_length)
		{
			if (index % w < w / 2)
			{
				m[0][index1] = data[index];
				index1++;
			} else
			{
				m[1][index2] = data[index];
				index2++;
			}
			index++;
		}
		return m;
	}

	/*
	 * public static byte[][] rawSeperate( byte[] data,int w,int h) { byte[][]
	 * double_image=new byte[2][1]; double_image[0] = new byte[(int) (w / 2 * h
	 * * 3/2)]; double_image[1] = new byte[(int) (w / 2 * h * 3/2)]; ByteBuffer
	 * srcBuffer=ByteBuffer.wrap(data); ByteBuffer
	 * lBuffer=ByteBuffer.wrap(double_image[0]); ByteBuffer
	 * rBuffer=ByteBuffer.wrap(double_image[1]); int halfwidth=w/2; byte[]
	 * dst=new byte[halfwidth]; int index =0; int rownum=h*3/2; while
	 * (index<rownum) { srcBuffer.get(dst,0,halfwidth); lBuffer.put(dst);
	 * srcBuffer.get(dst,0,halfwidth); rBuffer.put(dst); index++; } return
	 * double_image; }
	 */

	/**
	 * 將nv21格式的源數據拆分成左右兩張圖片。
	 * 
	 * @param data
	 * @param w
	 * @param h
	 * @return 左右圖byte數組
	 */
	public static byte[][] rawSeperate(byte[] data, int w, int h)
	{
		Log.e(TAG, "rawSeperate+++++");
		byte[][] double_image = new byte[2][1];
		double_image[0] = new byte[(int) (w / 2 * h * 3 / 2)];
		double_image[1] = new byte[(int) (w / 2 * h * 3 / 2)];
		ByteBuffer srcBuffer = ByteBuffer.wrap(data);
		ByteBuffer lBuffer = ByteBuffer.wrap(double_image[0]);
		ByteBuffer rBuffer = ByteBuffer.wrap(double_image[1]);
		int halfwidth = w / 2;
		byte[] dst = new byte[w];
		int index = 0;
		int rownum = h * 3 / 2;
		while (index < rownum)
		{
			srcBuffer.get(dst, 0, w);
			lBuffer.put(dst, 0, halfwidth);
			rBuffer.put(dst, halfwidth, halfwidth);
			index++;
		}
		return double_image;
	}

	@Override
	public void onPreviewFrame(byte[] data, Camera camera)
	{

		if (mCaptureState == CAPTURE_STATE_MOSAIC)
		{
			if(m_is3D)
			{
				runMosaicCapture(rawSeperate(data, mPreviewWidth, mPreviewHeight));
			}
			else
			{
				runMosaicCapture2D(data);
			}
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height)
	{
		if (mCameraDevice != null)
		{
			mCameraDevice.setPreviewCallback(this);
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		// TODO Auto-generated method stub
		Log.e("surfaceCreated", "ok");
		try
		{

			mCameraDevice = Camera.open(0);
			mCameraDevice.setPreviewDisplay(holder);
			Parameters p = mCameraDevice.getParameters();
			List<Size> mList = p.getSupportedPreviewSizes();
			for (int i = 0; i < mList.size(); i++)
			{
				Log.e("支持的预览尺寸为" + i,
						"" + mList.get(i).width + "dfdf" + mList.get(i).height);
			}

			List<Integer> aList = p.getSupportedPictureFormats();
			for (int i = 0; i < aList.size(); i++)
			{
				Log.e("支持的格式為" + i, "" + aList.get(i));
			}

			List<Size> supportedSizes = p.getSupportedPreviewSizes();
			if (!findBestPreviewSize(supportedSizes, true, true))
			{
				Log.w(TAG, "No 4:3 ratio preview size supported.");
				if (!findBestPreviewSize(supportedSizes, false, true))
				{
					Log.w(TAG,
							"Can't find a supported preview size smaller than 960x720.");
					findBestPreviewSize(supportedSizes, false, false);
				}
			}
			Log.e("setupCamera   pre_w{{{{{{{{{{{{{{{{{{{{{{{{{{", "="
					+ mPreviewWidth);
			Log.e("setupCamera   pre_h", "=" + mPreviewHeight);
			p.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);
			mPreviewWidth = 640;
			mPreviewHeight = 480;
			p.setPreviewSize(mPreviewWidth, mPreviewHeight);
			mHorizontalViewAngle = p.getHorizontalViewAngle();
			Log.e(" 拍摄的水平视角和垂直视角为 ", "="+ mHorizontalViewAngle+"*"+p.getVerticalViewAngle());
			mCameraDevice.setParameters(p);
			Log.e("预览尺寸为", "="+ mCameraDevice.getParameters().getPreviewSize().width);
			mCameraDevice.startPreview();
			initMosaicFrameProcessorIfNeeded();
		} catch (Exception e)
		{
			Log.e("tuichu ", "crash");
			if(mCameraDevice!=null)
			mCameraDevice.release();
		}

	}


	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{

		Log.e("gua  le ", "mei de ");
	}

	public class MBroadcastReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context arg0, Intent arg1)
		{
			System.out.println("SDCardBroadcastReceiver 被注册");
			String action = arg1.getAction();
			// 当SD卡插入时
			Log.e(TAG, action + "MBroadcastReceiver___________________________");
		}

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy)
	{
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onSensorChanged(SensorEvent event)
	{
	   //	if(mCaptureState == CAPTURE_STATE_MOSAIC)
		mPanoramaProgressIndicator.setlinerate(Math.toRadians(event.values[1]));
	}

}
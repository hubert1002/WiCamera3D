package com.wistron.WiCamera;

import java.io.IOException;
import java.util.ArrayList;

import Utilities.CSStaticData;
import Utilities.SystemInfo;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.wistron.WiViewer.MediaFilePicker;
import com.wistron.WiViewer.TDStaticData;
import com.wistron.WiViewer.WiImageView;
import com.wistron.swpc.wicamera3dii.R;

/**
 * 
 * @Copyright (c) 2012 Wistron SWPC All rights reserved.
 * 
 * @created: 2012/06/21
 * @filename: CameraPreview.java
 * @author WH1107063(周海江)
 * @purpose 照相的activity,主要用于连拍两张图片合成3D图片的操作逻辑
 * 
 * 
 * 
 * 
 */
public class CameraPreview extends Activity {
	// 打开camera的surfaceview
	public CameraView mPreview;
	// 预览的view
	private OverView imageview;
	public int mAlpha = 160;
	private int stdx;
	private int mOldAlpha;
	private ImageButton saveBtn;
	// 照相按钮
	private ToggleButton takePictureBtn;
	// 保存进度
	private ProgressBar circleProgressBar;
	// 定义一个Handler
	protected static final int FINISH = 0x10000;
	// 接受消息的handle
	public static Handler mHandler;
	String Tag = "CameraPreview";
	// 播放声音
	MediaFilePicker mp = null;
	public ArrayList<String> fileArray;
	// 图片预览
	WiImageView wv = null;
	RelativeLayout mRelativeLayout;
	SystemInfo si = new SystemInfo();
	// 监听屏幕旋转的类
	MyOrientationEventListener orientationListener;
	public int mOrientation;
	public int mOrientationCompensation;
	public int mCurrentDegree = OrientationEventListener.ORIENTATION_UNKNOWN;
	private int mPreOrientation = 270;
	public static boolean isReview = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setWindows();
		// 设置Activity的根内容视图
		setContentView(R.layout.controllpad);

		// 初始化控件
		mRelativeLayout = (RelativeLayout) findViewById(R.id.camerathree);
		circleProgressBar = (ProgressBar) findViewById(R.id.circleProgressBar);
		circleProgressBar.setIndeterminate(false);
		takePictureBtn = (ToggleButton) findViewById(R.id.ImageButtonTake);
		saveBtn = (ImageButton) findViewById(R.id.ImageButtonSave);
		mPreview = (CameraView) findViewById(R.id.surfaceview);
		imageview = (OverView) findViewById(R.id.imageview);
		fileArray = new ArrayList<String>();

		// 得到屏幕的宽高
		WiCameraActivity.initializeScreenBrightness(getWindow(),
				getContentResolver());
		TDStaticData.SCREEN_WIDTH = getWindowManager().getDefaultDisplay()
				.getWidth();
		TDStaticData.SCREEN_HEIGHT = getWindowManager().getDefaultDisplay()
				.getHeight();

		TDStaticData.SCREEN_WIDTH_ORG = TDStaticData.SCREEN_WIDTH;
		TDStaticData.SCREEN_HEIGHT_ORG = TDStaticData.SCREEN_HEIGHT;

		wv = new WiImageView(CameraPreview.this, mp);

		mRelativeLayout.addView(wv);
		wv.setVisibility(View.GONE);
		orientationListener = new MyOrientationEventListener(this);
		orientationListener.enable();
		takePictureBtn.setOnClickListener(MyonClickListener);
		saveBtn.setOnClickListener(MyonClickListener);
		// handle的初始化
		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				// 保存图片后预览
				case FINISH:
					isReview = true;
					// takePictureBtn.setVisibility(View.VISIBLE);
					circleProgressBar.setVisibility(View.GONE);
					// imageview.setAlpha(160);
					// updateImageView();

					ArrayList<String> tempArraylist = new ArrayList<String>();
					for (int i = fileArray.size() - 1; i >= 0; i--) {
						tempArraylist.add(fileArray.get(i));
					}
					System.out.println(tempArraylist);
					si.setScreenDimension(true);
					mp = new MediaFilePicker(tempArraylist,
							tempArraylist.get(0));
					// wv = new WiImageView(CameraPreview.this, mp);
					wv.setMediaPicker(mp);
					wv.ChangeMode(true);
					wv.setVisibility(View.VISIBLE);
					wv.setCurbitmapR(wv.getNextBitmapEx(mp.getCurrentFileName()));
					wv.setBackgroundColor(Color.BLACK);
					wv.setOrientationType(1);
					// setScreenDimension(true);
					break;
				// 第一次拍照完后设置，设置拍照按钮可用进行第二次拍照
				case 100:
					takePictureBtn.setEnabled(true);
					break;
				// 在竖屏情况下不能拍3D照片
				case 200:
					if (mCurrentDegree == 90 || mCurrentDegree == 270) {
						takePictureBtn.setEnabled(false);
					} else {
						takePictureBtn.setEnabled(true);
					}
					break;
				}

			}
		};

	}

	@Override
	protected void onResume() {
		si.setScreenDimension(false);
		// TODO Auto-generated method stub
		if (orientationListener != null) {
			orientationListener.enable();
		}

		super.onResume();
	}

	/**
	 * 打开3D屏幕
	 * 
	 * @param is3D
	 *            如果为true 则打开3D屏，否则打开2D屏
	 */
	public void setScreenDimension(boolean is3D) {
		// m_imageview.ChangeMode(is3D);
		String[] cmdTurnOn3D = { // 开启屏幕3D命名
		"/system/bin/sh", "-c",
				"echo 1 > /sys/devices/platform/mipi_masterimage.513/enable_3d_barrier" };

		String[] cmdTurnOff3D = { // 关闭屏幕3D命令
		"/system/bin/sh", "-c",
				"echo 0 > /sys/devices/platform/mipi_masterimage.513/enable_3d_barrier" };

		CSStaticData.g_is_3D_mode = is3D;
		try {
			if (CSStaticData.g_is_3D_mode) {
				if (CSStaticData.DEBUG) {
					Log.w(Tag, "[setScreenDimension]开启屏幕3D显示模式");
				}
				Runtime.getRuntime().exec(cmdTurnOn3D);
			} else {
				if (CSStaticData.DEBUG) {
					Log.w(Tag, "[setScreenDimension]关闭屏幕3D显示模式");
				}
				Runtime.getRuntime().exec(cmdTurnOff3D);
			}
		} catch (IOException exp) {
			if (CSStaticData.DEBUG) {
				Log.w(Tag, "[setScreenDimension]屏幕3D显示模式切换：失败，命令行输入流异常");
			}
		} catch (SecurityException exp) {
			if (CSStaticData.DEBUG) {
				Log.w(Tag, "[setScreenDimension]屏幕3D显示模式切换：失败，系统安全保护阻止本操作");
			}
		} catch (Exception exp) {
			if (CSStaticData.DEBUG) {
				Log.w(Tag, "[setScreenDimension]屏幕3D显示模式切换：失败，未知错误");
			}
		}
	}

	public OnClickListener MyonClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			// 拍照
			case R.id.ImageButtonTake:
				mPreview.takePicture();
				takePictureBtn.setEnabled(false);
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 设置全屏，无标题
	 */
	public void setWindows() {
		// 窗口去掉标题
		final Window win = getWindow();
		win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 窗口设置为全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// 设置窗口为半透明
		// getWindow().setFormat(PixelFormat.TRANSLUCENT);
	}

	// 显示预览imageview
	void updateImageView() {
		imageview.SetmBitmap(mPreview.mBitmap);
		imageview.setAlpha(mAlpha);
		imageview.setVisibility(View.VISIBLE);
		imageview.alpha = 160;
		imageview.invalidate();
	}

	// 拍照结束，保存图片
	void takePictureFinish() {
		// saveBtn.setVisibility(View.VISIBLE);
		circleProgressBar.setVisibility(View.VISIBLE);
		takePictureBtn.setVisibility(View.GONE);
		imageview.setVisibility(View.GONE);
		// saveBtn.setVisibility(View.INVISIBLE);
		// 存盘时显示等待图标，使用异步模式
		new myAsy().execute();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub

		mPreview.stopPreview();
		mPreview.releaseCamera();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mPreview.stopPreview();
		mPreview.releaseCamera();
		if (orientationListener != null) {
			orientationListener.disable();
			orientationListener = null;
		}
		super.onDestroy();

	}

	/*
	 * 异步保存图片
	 */
	class myAsy extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			mPreview.savePicture();
			return null;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		// TODO Auto-generated method stub
		// 在按下back键时的操作
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			// 如果正在预览，则返回拍照界面，否则退出
			if (isReview == true) {
				takePictureBtn.setVisibility(View.VISIBLE);
				wv.setVisibility(View.GONE);
				mPreview.startPreview();
				si.setScreenDimension(false);
				isReview = false;
				return false;
			} else {
				isReview = false;

				onDestroy();
				this.finish();
				return false;
			}
		}
		return false;
	}

	/**
	 * 触摸屏幕是改变左图的透明度
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (mPreview.bFlag) {
			imageview.onTouchEvent(event);
		}
		return false;
	}

	/**
	 * 监听屏幕转动的角度的类
	 * 
	 * @author WH1107063
	 * 
	 */
	/**
	 * 监听屏幕转动的角度的类
	 * 
	 * @author WH1107063
	 * 
	 */
	public class MyOrientationEventListener extends OrientationEventListener {
		Animation m_mr_smaller;
		Animation m_mr_bigger;

		public MyOrientationEventListener(Context context) {
			super(context);

		}

		@Override
		public void onOrientationChanged(int orientation) {
			boolean isGsensor = StoredData.getBoolean(StoredData.m_GSENSOR,
					true);
			if (!isGsensor) {
				return;
			}

			if (orientation == ORIENTATION_UNKNOWN)
				return;
			mOrientation = Util.roundOrientation(orientation, mOrientation);

			int orientationCompensation = mOrientation
					+ Util.getDisplayRotation(CameraPreview.this);
			if (mOrientationCompensation != orientationCompensation) {
				mOrientationCompensation = orientationCompensation;
				mCurrentDegree = mOrientationCompensation;
				// System.out.println("mOrientationss=" + mCurrentDegree);
				// System.out.println("mOrientation=======" + mOrientation);
				// 发送手机的当前旋转角度，如果为竖屏则禁用拍照按钮
				// if (mPreOrientation != mOrientation) {
				mHandler.removeMessages(200);
				mHandler.sendEmptyMessage(200);
				// }

			}

		}
	}
}

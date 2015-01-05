package com.wistron.WiCamera;

import java.util.ArrayList;
import java.util.List;

import Utilities.CSStaticData;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
/**
 * 
 * @Copyright (c) 2011 Wistron SWPC All rights reserved.
 * 
 * @created: 2012/02/21
 * @filename: InterSurfaceView.java
 * @author WH1107063(周海江)
 * @purpose 照相或录像的接口，供ddd控件和普通的surfaceview切换使用
 * 
 * 
 * 
 * 
 */
public interface InterSurfaceView {
	public final static int CAMERA_PICLIST_UPDATE = 502;
	public final static int CAMERA_VIDEOLIST_UPDATE = 501;
	// 单拍
	public final static int CAMERA_SINGLE_SHOT = 0;
	// 连拍
	public final static int CAMERA_CONTINUOUS = 1;
	// 局部对焦
	public final static int CAMERA_FOCUAREA = 2;
	// 录像
	public final static int CAMERA_VIDEO = 3;
	// 设定时间拍照
	public final static int CAMERA_SELFTIME = 4;
	// 设定定时连拍
	public final static int CAMERA_CONTINUOUSANDSELFTIME = 5;
	// 没有对焦
	public static final int STATE_IDLE = 0;
	// 正在对焦
	public static final int STATE_FOCUSING = 1;
	// 对焦未完成
	public static final int STATE_FOCUSING_SNAP_ON_FINISH = 2;
	// 对焦成功
	public static final int STATE_SUCCESS = 3;
	// 对焦失败
	public static final int STATE_FAIL = 4;
	// 连拍的声音
	public final static String[] CAMERA_SHOT_SOUND = new String[] {
			"file:///system/media/audio/ui/camera_click.ogg",
			"file:///system/media/audio/ui/Dock.ogg" };
	// 场景模式;
	public final static String[] CAMERA_SCENEMODE_ARRAY = new String[] {
			Camera.Parameters.SCENE_MODE_AUTO,
			Camera.Parameters.SCENE_MODE_ACTION,
			Camera.Parameters.SCENE_MODE_NIGHT,
			Camera.Parameters.SCENE_MODE_THEATRE,
			Camera.Parameters.SCENE_MODE_BEACH,
			Camera.Parameters.SCENE_MODE_SNOW,
			Camera.Parameters.SCENE_MODE_SUNSET,
			Camera.Parameters.SCENE_MODE_FIREWORKS

	};
	// 白平衡模式
	public final static String[] CAMERA_WHITEBALANCE_ARRAY = new String[] {
			Camera.Parameters.WHITE_BALANCE_AUTO,
			Camera.Parameters.WHITE_BALANCE_CLOUDY_DAYLIGHT,
			Camera.Parameters.WHITE_BALANCE_INCANDESCENT,
			Camera.Parameters.WHITE_BALANCE_DAYLIGHT,
			Camera.Parameters.WHITE_BALANCE_FLUORESCENT };
	// iso
	public final static String[] CAMERA_ISO_ARRAY = new String[] { "auto",
			"ISO100", "ISO200", "ISO400", "ISO800", "ISO1600" };
	// 曝光类别
	public final static int[] CAMERA_EXPOSURE_ALL = { -4, -3, -2, -1, 0, 1, 2,
			3, 4 };
	// 闪光灯模式
	public final static String[] CAMERA_FLASHARRAY = new String[] {
			Camera.Parameters.FLASH_MODE_AUTO, Camera.Parameters.FLASH_MODE_ON,
			Camera.Parameters.FLASH_MODE_OFF,

	};

	public abstract void onPause();

	public abstract void onResume();

	// 闪光灯模式
	public final static String[] CAMERA_FOCUSMODE = new String[] {
			Camera.Parameters.FOCUS_MODE_AUTO,
			Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE,
			Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO,

	};
	public final static String CAMERA_SDCARDPATH = Environment
			.getExternalStorageDirectory().getAbsolutePath();
	public final static String[] CAMERA_PAHTARRAY = new String[] {
			CSStaticData.TMP_INT_DIR,// 内置
			CSStaticData.TMP_EXT_DIR // sdcard

	};
	// 设定照片的大小
	public final static int[][] CAMERA_PICSIZE = new int[][] { { 2592, 1944 },
			{ 2048, 1536 }, { 1600, 1200 }, { 1024, 768 }, { 640, 480 } };
	// 设定自定义时间拍照的时间
	public final static int[] CAMERA_SELFTIMERARRAY = new int[] { 0, 3, 5, 10 };
	// 设定连拍的张数
	public final static int[] CAMERA_CONTINUESHOTNUMARRAY = new int[] { 3, 5, 7 };
	// 设定录像的像素
	public final static int[][] VIDEO_VIDEORESOLUTIONARRAY = new int[][] {
			{ 1920, 1080 }, { 1280, 720 }, { 800, 480 }, { 640, 480 },
			{ 352, 288 }, { 320, 240 } };
	// 设定自定义时间录像
	public final static int[] VIDEO_SELFTIME = new int[] { 0, 3, 5, 10 };

	public abstract void camerasFocus(boolean isfocuskeyup);

	public abstract void isSingleModes(boolean isSingle);

	// 0为一般模式 1为连拍模式
	public abstract void setContinueMode(int mode);

	public abstract int getPreviewFormat();

	public abstract void continueNum(int num);

	public abstract void is3D(boolean is3D);

	public abstract void initParm();

	public abstract void setSize(int previewwidth, int previewheight);

	public abstract void continueCapture();

	public void isMute(boolean isMute);

	public abstract String getVideoPath();

	public abstract void setVideoPath(String path);
	void cancalAutoFocus();
	/**
	 * 设置照相时的不停对焦模式
	 */
	public abstract void setFocusMode(int focusmode);

	public abstract List<Size> getSupportedPreviewSizes();

	void soundPlayerRelease();

	public abstract Size getPictureSize();

	/**
	 * 切换摄像头 如果无法切换到相应的摄像头(前置，后置,并重置摄像头
	 */
	public abstract boolean switchCamera();


	public abstract boolean isNeedFocus(Rect[] oldRect, Rect[] newRect);

	public abstract void setDisplayOrientation();

	public abstract void setOrientationIndicator(int degree);

	public abstract void surfaceChanged();

	public abstract void surfaceDestroyed();

	/**
	 * 设定焦距值，进行预览的放大和缩小
	 * 
	 * @param value
	 */
	public abstract void setZoomSize(int value);

	/**
	 * 设置iso android camera如何设置iso信息
	 */
	public abstract void setISO();

	/**
	 * 设置照片的大小
	 */
	public abstract void setPictureSize();

	/**
	 * 如果已经连接到摄像头，则设定预览大小
	 */
	public abstract void setPreviewSize(int width, int height);

	public abstract float getHorizontalViewAngle();

	/**
	 * 如果已经连接到摄像头，则设定预览大小
	 */
	public abstract void setPreviewSize();

	/**
	 * 开始预览控制
	 */
	public abstract void startPreview();

	/**
	 * 停止预览
	 */
	public abstract void stopPreview();

	/**
	 * 设置场景模式
	 */
	public abstract void setSceneMode();

	public abstract void setRotationParm();

	public abstract ArrayList<String> getVideoFileList();

	public abstract void setVideoFileList(ArrayList<String> lst);

	public abstract ArrayList<String> getPicFileList();

	public abstract void setPicFileList(ArrayList<String> lst);

	/**
	 * 自定义时间拍照
	 */
	public abstract void setSelfTime();

	/**
	 * 自定义时间拍照
	 */
	public abstract void setSelfTimes();

	/**
	 * 设置闪关灯模式
	 */
	public abstract void setFlashMode(int flashmode);

	/**
	 * 设置白平衡
	 */
	public abstract void setWhiteBalanceMode();

	/**
	 * 恢复默认
	 */
	public abstract void restoreDefault();

	/**
	 * 如果已经连接到摄像头，则设定曝光度
	 */
	public abstract void setExposure();

	/**
	 * 设置拍照声音的类型
	 * 
	 */
	public abstract void PlaySounds(int a);

	/**
	 * 释放多媒体播放器资源
	 */
	public abstract void releaseMediaplayer();

	public abstract void playSound(int id);

	public abstract void setSoundMode(int id);

	/**
	 * 释放camera资源
	 */
	public abstract void releaseCamera();

	public abstract void startFaceDetection();

	public abstract void stopFaceDetection();

	/**
	 * 照相
	 */
	public abstract void myTakePic();

	public abstract void onPictureTaken(byte[] data, Camera cameras);

	public abstract void setOrientationHint();

	public abstract void startRecord();

	/**
	 * 停止录制
	 */
	public abstract void stopRecord();

	public abstract void storedImage(final byte[] data, final int photonum);

	/**
	 * 设置对焦区域
	 */
	public abstract void setArea();

	public abstract void updateCameraParametersInitialize();

	public abstract boolean onTouchEvent(MotionEvent e);

	/**
	 * 计算对焦区域
	 * 
	 * @param focusWidth
	 *            对焦图片的宽
	 * @param focusHeight
	 *            对焦图片的高
	 * @param areaMultiple
	 *            乘以的倍数
	 * @param x
	 *            触点的x坐标
	 * @param y
	 *            触点的y坐标
	 * @param previewWidth
	 *            surfaceview的宽
	 * @param previewHeight
	 *            surfaceview的高
	 * @param rect
	 *            对焦的矩阵
	 * @return 计算了的矩阵
	 */
	public abstract Rect calculateTapArea(int focusWidth, int focusHeight,
			double areaMultiple, int x, int y, int previewWidth,
			int previewHeight, Rect rect);

	/* 移动图片的方法 */
	public abstract void picMove(float x, float y, ImageView imageView,
			int intWidth, int intHeight, int intScreenX, int intScreenY);

	public abstract void setPreviewCallback(PreviewCallback my);

	public abstract int saveImage(final byte[] data, final int photonum);

	// public abstract void setOnTouchListener(OnTouchListener onTouchListener);
}
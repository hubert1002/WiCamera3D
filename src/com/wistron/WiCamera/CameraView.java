package com.wistron.WiCamera;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 
 * @Copyright (c) 2012 Wistron SWPC All rights reserved.
 * 
 * @created: 2012/06/21
 * @filename: CameraView.java
 * @author WH1107063(周海江)
 * @purpose 照相的SurfaceView,主要用于打开camera和对camera的操作 ，如打开camera，设置参数，拍照
 * 
 * 
 * 
 * 
 */
public class CameraView extends SurfaceView implements SurfaceHolder.Callback {
	// SurfaceHolder实例
	SurfaceHolder mHolder;
	// camera实例
	Camera mCamera;
	// 预览的bitmap
	public Bitmap mBitmap = null;
	// activity对象
	public CameraPreview owner;
	byte[] callbackBuffer;
	public boolean bFlag = false;
	private int mScreenWidth = 800;
	private int mScreenHeight = 480;
	// 预览图
	private Bitmap mbmp_previewL = null;
	private Bitmap mbmp_previewR = null;
	// 实际拍照图
	private Bitmap mbmp_picL = null;
	private Bitmap mbmp_picR = null;
	private int mCameraID;
	public CameraPreview context;

	public CameraView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		this.context = (CameraPreview) context;

		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	private Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
		public void onPreviewFrame(byte[] data, Camera camera) {

		}
	};

	public void takePicture() {
		// mCamera.takePicture(shutterCallback, null, pictureCallback);
		mCamera.autoFocus(autoFocusCallback);

	}

	// 准备一个保存图片的PictureCallback对象
	public Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {

		public void onPictureTaken(byte[] data, Camera camera) {
			// 用BitmapFactory.decodeByteArray()方法可以把相机传回的裸数据转换成Bitmap对象
			// 拍摄左图
			if (bFlag) {

				// convertToRBImage(mbmp_previewL, mbmp_previewR, mBitmap);\
				dataR = data;

				context.mAlpha = 250;
				bFlag = false;
				context.takePictureFinish();
				// 拍摄右图
			} else {
				mbmp_picL = BitmapFactory.decodeByteArray(data, 0, data.length);

				Canvas canvas = new Canvas(mbmp_previewL);
				Rect rect1 = new Rect(0, 0, mbmp_picL.getWidth(),
						mbmp_picL.getHeight());
				Rect rect2 = new Rect(0, 0, mbmp_previewL.getWidth(),
						mbmp_previewL.getHeight());
				canvas.drawBitmap(mbmp_picL, rect1, rect2, null);
				canvas = new Canvas(mBitmap);
				canvas.drawBitmap(mbmp_previewL, 0, 0, null);

				dataL = data;
				bFlag = true;
				context.mAlpha = 170;
				context.updateImageView();
			}

			mCamera.startPreview();
			context.mHandler.sendEmptyMessage(100);
		}
	};

	public Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
		public void onShutter() {
		}
	};

	public Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			camera.takePicture(shutterCallback, null, pictureCallback);
		}
	};

	/**
	 * 把bitmap保存成文件
	 * 
	 * @param bmp
	 * @param name
	 */
	public void saveImage(Bitmap bmp, String name) {
		File file = new File(name);
		try {
			file.createNewFile();
			BufferedOutputStream os = new BufferedOutputStream(
					new FileOutputStream(file));
			bmp.compress(Bitmap.CompressFormat.JPEG, 90, os);
			os.flush();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置图片的旋转角度
	 * 
	 * @param activity
	 * @param cameraId
	 * @param camera
	 */
	public static void setCameraDisplayOrientation(Activity activity,
			int cameraId, android.hardware.Camera camera) {
		android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
		android.hardware.Camera.getCameraInfo(cameraId, info);
		int rotation = activity.getWindowManager().getDefaultDisplay()
				.getRotation();
		int degrees = 0;
		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 0;
			break;
		case Surface.ROTATION_90:
			degrees = 90;
			break;
		case Surface.ROTATION_180:
			degrees = 180;
			break;
		case Surface.ROTATION_270:
			degrees = 270;
			break;
		}

		int result;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360; // compensate the mirror
		} else { // back-facing
			result = (info.orientation - degrees + 360) % 360;
		}
		camera.setDisplayOrientation(result);
	}

	List<Size> mCameraSizes;
	private byte[] dataL;
	private byte[] dataR;

	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, acquire the camera and tell it where
		// to draw.
		// int num = Camera.getNumberOfCameras ();
		// Log.i("info", "Camera count : " + num );
		// if(num < 2)
		// {
		// return;
		// }
		// mCamera = Camera.open(mCameraID);
		mCamera = Camera.open(0);
		if (mCamera == null) {
			context.finish();
		}

		setCameraDisplayOrientation(context, 0, mCamera);

		Camera.Parameters parameters = mCamera.getParameters();// 设置照片输出格式
		parameters.setPictureFormat(PixelFormat.JPEG);
		mCameraSizes = parameters.getSupportedPictureSizes();// 取得相机所支持多少图片大小的个数
		String string = parameters.getFocusMode();
		Log.i("info", string);

		Size optimalSize = getOptimalPreviewSize(mCameraSizes, 1600, 960);
		parameters.setPictureSize(640, 480);
		parameters.setFocusMode(Parameters.FOCUS_MODE_AUTO);
		// 给相机对象设置刚才设定的参数
		mCamera.setParameters(parameters);

		try {
			mCamera.setPreviewDisplay(holder);
		} catch (IOException exception) {
			mCamera.release();
			mCamera = null;
			// TODO: add more exception handling logic here
		}

	}

	/*
	 * 开始预览
	 */
	public void startPreview() {
		if (mCamera != null) {
			mCamera.startPreview();
		}
	}

	/*
	 * 停止预览
	 */
	public void stopPreview() {
		if (mCamera != null) {
			mCamera.stopPreview();
		}
	}

	/*
	 * 释放camera资源
	 */
	public void releaseCamera() {
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.cancelAutoFocus();
			mCamera.release();
			mCamera = null;
		}
	}

	/**
	 * surface销毁时释放camera资源
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {
		// Surface will be destroyed when we return, so stop the preview.
		// Because the CameraDevice object is not a shared resource, it's very
		// important to release it when the activity is paused.
		// context.si.setScreenDimension(false);
		stopPreview();
		releaseCamera();
	}

	/**
	 * 根据图片大小得到最适合的预览大小
	 * 
	 * @param sizes
	 * @param w
	 * @param h
	 * @return
	 */
	private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
		final double ASPECT_TOLERANCE = 0.05;
		double targetRatio = (double) w / h;
		if (sizes == null)
			return null;

		Size optimalSize = null;
		double minDiff = 200;

		int targetHeight = h;

		// Try to find an size match aspect ratio and size
		for (Size size : sizes) {
			double ratio = (double) size.width / size.height;
			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
				continue;
			if (Math.abs(size.height - targetHeight) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
			}
		}

		// Cannot find the one match the aspect ratio, ignore the requirement
		if (optimalSize == null) {
			minDiff = Double.MAX_VALUE;
			for (Size size : sizes) {
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}
		return optimalSize;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		Camera.Parameters parameters = mCamera.getParameters();

		List<Size> sizes = parameters.getSupportedPreviewSizes();
		Size optimalSize = getOptimalPreviewSize(sizes, w, h);
		parameters.setPreviewSize(640, 480);
		String s = String.format("surface w: %d  h: %d", w, h);
		mScreenWidth = w;
		mScreenHeight = h;
		Log.i("info", s);

		// 创建用于预览合成的图片
		mbmp_previewL = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);// 创建bitmap对象
		mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

		// mCamera.setParameters(parameters);
		mCamera.startPreview();

	}

	public String filePath = null;

	/*
	 * 保存图片
	 */
	public void savePicture() {

		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(date);
		String title = InterSurfaceView.CAMERA_PAHTARRAY[0] + "photo-"
				+ dateString;
		filePath = title + ".jps";
		context.fileArray.add(filePath);

		Bitmap bitl = BitmapFactory.decodeByteArray(dataL, 0, dataL.length);
		Bitmap bitr = BitmapFactory.decodeByteArray(dataR, 0, dataR.length);
		Bitmap b = Bitmap.createBitmap(bitl.getWidth() * 2, bitl.getHeight(),
				Config.ARGB_8888);
		Canvas c = new Canvas(b);
		c.drawBitmap(bitl, 0, 0, null);
		c.drawBitmap(bitr, bitl.getWidth(), 0, null);

		// TODO Auto-generated method stub
		// MPOFileStreamParser.encodeFile(filePath, dataL, dataR,
		// new OnMPOWrittenListener() {
		//
		// @Override
		// public void OnMPOWrittenCompleted() {
		// // TODO Auto-generated method stub
		// Message msg = new Message();
		// msg.what = context.FINISH;
		// context.mHandler.sendMessage(msg);
		//
		// }
		// });
		saveBitmap(b, filePath);
		OperationFile.intTodb(getContext(), "image/jpeg", title, 0, filePath,
				b.getByteCount());
		Message msg = new Message();
		stopPreview();
		msg.what = context.FINISH;
		context.mHandler.sendMessage(msg);
		System.gc();
		dataL = null;
		dataR = null;
	}

	// 把bitmap保存成文件
	public static void saveBitmap(Bitmap b, String filePath) {
		File f = new File(filePath);
		if (!f.getParentFile().exists()) {
			f.mkdirs();
		}
		FileOutputStream fOut = null;

		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		b.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}

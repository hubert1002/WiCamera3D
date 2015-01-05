package com.wistron.WiCamera;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import Utilities.CSStaticData;
import Utilities.SystemInfo;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.Area;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.FaceDetectionListener;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.location.Location;
import android.media.AudioManager;
import android.media.FaceDetector.Face;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.tridef.converter.DDD;
import com.tridef3d.graphics.Image3D;
import com.tridef3d.hardware.Display3D;
import com.tridef3d.view.TextureView3D;
import com.wistron.WiGallery.WiGalleryOpenGLRenderer;
import com.wistron.swpc.wicamera3dii.R;

/**
 * 
 * @Copyright (c) 2011 Wistron SWPC All rights reserved.
 * 
 * @created: 2012/02/21
 * @filename: VideoSurfaceView.java
 * @author WH1107063(周海江)
 * @purpose 照相的surfaceView类，包含照相，连拍等操作
 * 
 * 
 * 
 * 
 */
public class VideoSurfaceView extends TextureView3D implements
		TextureView.SurfaceTextureListener, Camera.PictureCallback,
		InterSurfaceView, FaceDetectionListener {
	public Camera cameras;
	ArrayList<byte[]> list;
	public MediaPlayer m_mediaplayer;
	public int photonum = 0;
	public static Handler handler;
	// SurfaceHolder camera_holder;
	public Context m_context;
	public SoundPlayer soundPlayer;
	// 判断对camera的操作，如连拍，单拍等
	// public static int CAMERA_STATE = 0;
	public static String[] VIDEO_ARRRAY;

	// 对焦的状态
	public static int FOCU_STATE = 0;
	// 剩余空间
	public String spareSpace;
	// 对焦回调
	AutoFocusCallback myAutoFocusCallback;
	// 连拍的最后一张图片的路径
	public String CAMERA_CONTINUOUS_LASTPICPATH;
	// 录制文件的路径
	public static String M_VIDEOPATH = "";
	// 设置焦距的当前视图区域
	public List<Area> mFocusArea;
	// 设置感光度的当前视图区域
	public List<Area> mMeteringArea;
	public ImageView camera_focusImageView;
	public Button camera_spareSpace;
	public ImageView camera_newpic_thumbnails;
	public Button camera_continuous;
	public static boolean ok = false;
	public ArrayList<String> continous_array;
	// camera参数
	public Parameters camera_parameters;
	int previewwidth;
	int previewheight;
	// m_recorder对象
	public MediaRecorder m_recorder = null;
	// 视频文件
	public File m_videofile;
	// 是否在录制状态
	public boolean m_isrecord = false;
	// 前置后置照相头
	public int m_camera_front = 0;
	public int m_camera_back = 0;
	// 使用摄像头的位置0 默认,1前置
	public int m_cameraIndex = 0;
	public Matrix mMatrix = new Matrix();;

	public boolean m_isPreview = false; // 是否在预览中
	public int mCurrentDegree;
	// public static String newpicsize = "0";
	Face[] mFaces;
	public boolean isFacedetectSupported = false;
	public boolean m_isreview = true;
	// public OnClickListener camera_onClickListener;
	public boolean m_isfacedetection = true;
	public Rect[] oldRect = new Rect[0];
	public ImageView camera_review_thumbnails;
	public ArrayList<String> VIDEO_FILE_LIST;
	public ArrayList<String> PIC_FILE_LIST;
	public Location m_location;
	public GetGPSInfo m_getGpsInfo;
	AudioManager audioManager;
	public boolean isToFocusToCapture = false;
	public boolean isfocuskeyDown = false;
	public TextureView3D mTextureView;
	public SurfaceTexture mSurfaceTexture;

	int mVideoWidth = 640;
	int mVideoHeight = 480;
	SystemInfo si = new SystemInfo();

	public ArrayList<String> getVideoFileList() {
		return VIDEO_FILE_LIST;
	}

	public void setVideoFileList(ArrayList<String> lst) {
		this.VIDEO_FILE_LIST = lst;
	}

	public ArrayList<String> getPicFileList() {
		return PIC_FILE_LIST;
	}

	public void setPicFileList(ArrayList<String> lst) {
		this.PIC_FILE_LIST = lst;
	}

	public int getPreviewFormat() {
		// TODO Auto-generated method stub
		return camera_parameters.getPreviewFormat();
	}

	public List<Size> getSupportedPreviewSizes() {
		// TODO Auto-generated method stub
		camera_parameters = cameras.getParameters();
		return camera_parameters.getSupportedPreviewSizes();
	}

	public void soundPlayerRelease() {
		// TODO Auto-generated method stub
		soundPlayer.release();
	}

	@Override
	public Size getPictureSize() {
		return camera_parameters.getPictureSize();
	}

	public String getVideoPath() {
		return M_VIDEOPATH;
	}

	public void setVideoPath(String path) {
		this.M_VIDEOPATH = path;
	}

	public VideoSurfaceView(Context context, AttributeSet paramAttributeSet) {
		super(context, paramAttributeSet);

		mTextureView = VideoSurfaceView.this;
		System.out.println("VideoSurfaceView init" + mTextureView);
		mTextureView.setSurfaceTextureListener(this);
		// mTextureView.enable3D(true);
		mTextureView.force3DMode(Display3D.MODE_2D);

		m_context = context;
		// camera_holder = getHolder();
		// camera_holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		// camera_holder.setKeepScreenOn(true);
		// camera_holder.addCallback(this);
		m_recorder = new MediaRecorder();
		StoredData.init(context);
		// VIDEO_FILE_LIST = new ArrayList<String>();
		// PIC_FILE_LIST = new ArrayList<String>();
		m_getGpsInfo = new GetGPSInfo(m_context);
		m_getGpsInfo.getGps();

		list = new ArrayList<byte[]>();
		photonum = 0;
		if (continous_array != null) {
			continous_array.clear();
			continous_array = null;
		}
		continous_array = new ArrayList<String>();
		audioManager = (AudioManager) m_context
				.getSystemService(Service.AUDIO_SERVICE);
		// 对焦的回调类
		myAutoFocusCallback = new AutoFocusCallback() {

			@Override
			public void onAutoFocus(boolean success, Camera camera) {
				// TODO Auto-generated method stub
				if (isToFocusToCapture) {
					PlaySounds(1);
					// if(success){
					cameras.takePicture(null, null, null, VideoSurfaceView.this);
					isToFocusToCapture = false;
					camera_focusImageView.setVisibility(View.GONE);
					return;
					// }
				}
				if (isfocuskeyDown) {
					PlaySounds(1);
					isfocuskeyDown = false;
					// camera_focusImageView.setVisibility(View.GONE);
					// handler.sendEmptyMessageDelayed(CAMERA_FOCUAREA, 3000);
					return;
				}
				System.out.println("对焦的回调哦、、、、、、、、、、、、、、、");
				if (m_isfacedetection) {
					if (success) {
						FOCU_STATE = STATE_SUCCESS;
					} else {
						FOCU_STATE = STATE_FAIL;
					}
					if (cameras != null) {
						cameras.cancelAutoFocus();
						// setFocusMode(1);

					}
					System.out.println(" 自动对讲回调sss");
				} else {

					if (FOCU_STATE == STATE_FOCUSING_SNAP_ON_FINISH) {
						if (success) {
							// PlaySounds();
							FOCU_STATE = STATE_SUCCESS;
						} else {
							FOCU_STATE = STATE_FAIL;

						}
						myTakePic();
					} else if (FOCU_STATE == STATE_FOCUSING) {
						if (success) {
							FOCU_STATE = STATE_SUCCESS;
							PlaySounds(1);

						} else {
							FOCU_STATE = STATE_FAIL;
						}
						System.out.println(" 自动对讲回调");
					}
					handler.removeMessages(CAMERA_FOCUAREA);
					handler.sendEmptyMessageDelayed(CAMERA_FOCUAREA, 3000);
				}
			}
		};

		// 接受对camera操作发回的消息，并做相应的处理
		handler = new Handler() {

			public void handleMessage(Message msg) {
				int what = msg.what;
				switch (what) {
				case CAMERA_SINGLE_SHOT:
					camera_newpic_thumbnails.setVisibility(View.VISIBLE);
					camera_newpic_thumbnails.setAlpha(255);
					WiCameraActivity.newpic_thumbnails_parent
							.setVisibility(View.VISIBLE);
					storedImage(singleData, -1);
					Bitmap bitmap = OperationFile.fitSizeImg(
							CAMERA_CONTINUOUS_LASTPICPATH, 90, 90, 90);
					camera_newpic_thumbnails.setImageBitmap(bitmap);
					spareSpace = OperationFile.readSDCard();
					camera_spareSpace.setText(spareSpace + "");
					if (WiCameraActivity.mCurrentDegree == 90) {
						WiCameraActivity.m_al_camera_overlayui.postInvalidate();
					}
					boolean m_isReview = StoredData.getBoolean(
							StoredData.M_REVIEW, true);
					// WiCameraActivity.m_btn_camera_captureorrecord
					// .setEnabled(true);
					if (m_isReview) {
						Bitmap bit = null;
						// Bitmap bits = BitmapFactory
						// .decodeFile(CAMERA_CONTINUOUS_LASTPICPATH);
						if (WiCameraActivity.mCurrentDegree == 90) {
							bit = OperationFile.fitSizeImg(
									CAMERA_CONTINUOUS_LASTPICPATH,
									previewheight / 2, previewheight / 2,
									previewwidth / 2);
							WiCameraActivity.M_ISLANSCAPE = false;
						} else if (WiCameraActivity.mCurrentDegree == 360) {
							bit = OperationFile.fitSizeImg(
									CAMERA_CONTINUOUS_LASTPICPATH,
									previewheight / 2, previewwidth / 2,
									previewheight / 2);
							WiCameraActivity.M_ISLANSCAPE = true;
						}
						camera_review_thumbnails.setImageBitmap(bit);
						((WiCameraActivity) (m_context)).isShowReview(true);
					}
					WiCameraActivity.m_main_handle.removeMessages(3);
					WiCameraActivity.m_main_handle.sendEmptyMessageDelayed(3,
							500);
					if (soundPlayer != null) {
						soundPlayer.release();
					}
					break;
				case CAMERA_CONTINUOUS:
					camera_newpic_thumbnails.setVisibility(View.VISIBLE);
					camera_newpic_thumbnails.setAlpha(255);
					WiCameraActivity.newpic_thumbnails_parent
							.setVisibility(View.VISIBLE);
					int numindex = StoredData.getInt(
							StoredData.M_CONTINUESHOTNUM, 0);
					int maxnum = CAMERA_CONTINUESHOTNUMARRAY[numindex];
					boolean is3D = StoredData.getBoolean(StoredData.M_CAMERA3D,
							false);
					for (int i = 0; i < list.size(); i++) {

						// if(is3D){
						saveImage(list.get(i), i);
						// }
						// storedImage(list.get(i), i);

						if ((i + 1) == maxnum) {
							Bitmap bm = OperationFile.fitSizeImg(
									continous_array.get(0), 90, 90, 90);
							camera_newpic_thumbnails.setImageBitmap(bm);
							// camera_newpic_thumbnails
							// .setVisibility(View.VISIBLE);
							// camera_newpic_thumbnails.setAlpha(255);

						}

					}

					System.gc();
					spareSpace = OperationFile.readSDCard();
					camera_spareSpace.setText(spareSpace + "");
					if (WiCameraActivity.mCurrentDegree == 90) {
						WiCameraActivity.m_al_camera_overlayui.postInvalidate();
					}
					// camera_continuous.setEnabled(true);
					// WiCameraActivity.m_btn_camera_captureorrecord
					// .setEnabled(true);
					boolean m_isReviews = StoredData.getBoolean(
							StoredData.M_REVIEW, true);
					if (m_isReviews) {
						Bitmap bit = null;
						// Bitmap bits = BitmapFactory
						// .decodeFile(CAMERA_CONTINUOUS_LASTPICPATH);
						if (WiCameraActivity.mCurrentDegree == 90) {
							bit = OperationFile.fitSizeImg(
									continous_array.get(0), previewheight / 2,
									previewheight / 2, previewwidth / 2);
							WiCameraActivity.M_ISLANSCAPE = false;
						} else if (WiCameraActivity.mCurrentDegree == 360) {
							bit = OperationFile.fitSizeImg(
									continous_array.get(0), previewheight / 2,
									previewwidth / 2, previewheight / 2);
							WiCameraActivity.M_ISLANSCAPE = true;
						}
						camera_review_thumbnails.setImageBitmap(bit);
						((WiCameraActivity) (m_context)).isShowReview(true);
					}
					WiCameraActivity.m_main_handle.removeMessages(4);
					WiCameraActivity.m_main_handle.sendEmptyMessageDelayed(4,
							500);
					if (soundPlayer != null) {
						soundPlayer.release();
					}
					list = new ArrayList<byte[]>();
					photonum = 0;
					if (continous_array != null) {
						continous_array.clear();
						continous_array = null;
					}
					continous_array = new ArrayList<String>();

					break;
				case CAMERA_FOCUAREA:
					if (cameras != null) {
						startFaceDetection();
						// cameras.cancelAutoFocus();
						m_isfacedetection = true;
						int flashMode = StoredData.getInt(
								StoredData.M_FLASHMODE, 0);
						setFlashMode(flashMode);
						// setFocusMode(1);
						camera_focusImageView.setVisibility(View.GONE);
					}

					break;
				case CAMERA_SELFTIME:
					Util.CAMERA_STATE = CAMERA_SELFTIME;
					StoredData.saveInt(StoredData.M_CAMERA_STATE,
							CAMERA_SELFTIME);
					if (cameras != null) {
						// Util.setRotationParameter(camera_parameters,
						// m_cameraIndex, WiCameraActivity.mOrientation);
						// cameras.setParameters(camera_parameters);
						setRotationParm();
					}
					if (FOCU_STATE == STATE_FOCUSING) {
						FOCU_STATE = STATE_FOCUSING_SNAP_ON_FINISH;
					} else {
						if (cameras != null) {
							// cameras.takePicture(null, null, null,
							// VideoSurfaceView.this);
							cameras.cancelAutoFocus();
							isToFocusToCapture = true;
							cameras.autoFocus(myAutoFocusCallback);
						}
					}
					break;
				case CAMERA_CONTINUOUSANDSELFTIME:
					Util.CAMERA_STATE = CAMERA_CONTINUOUSANDSELFTIME;
					StoredData.saveInt(StoredData.M_CAMERA_STATE,
							CAMERA_CONTINUOUSANDSELFTIME);
					if (cameras != null) {
						// Util.setRotationParameter(camera_parameters,
						// m_cameraIndex, WiCameraActivity.mOrientation);
						// cameras.setParameters(camera_parameters);
						setRotationParm();
					}
					if (FOCU_STATE == STATE_FOCUSING) {
						FOCU_STATE = STATE_FOCUSING_SNAP_ON_FINISH;
					} else {
						if (cameras != null) {
							cameras.takePicture(null, null, null,
									VideoSurfaceView.this);
						}
					}
					break;
				case CAMERA_VIDEO:
					startRecord();
					break;
				default:
					break;
				}
			};
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * 对焦
	 */
	@Override
	public void camerasFocus(boolean isfocuskeyup) {
		isfocuskeyDown = true;
		cameras.cancelAutoFocus();
		cameras.autoFocus(myAutoFocusCallback);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * 硬件连拍是设置是连拍模式还是单拍模式
	 */
	@Override
	public void isSingleModes(boolean isSingle) {
		if (isSingle) {
			setContinueMode(0);
			stopPreview();
			startPreview();
			continueNum(1);
		} else {
			int num = StoredData.getInt(StoredData.M_CONTINUESHOTNUM, 0);
			setContinueMode(1);
			stopPreview();
			startPreview();
			continueNum(InterSurfaceView.CAMERA_CONTINUESHOTNUMARRAY[num]);
		}

	}

	// 0为一般模式 1为连拍模式
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wistron.WiCamera.Inter#setContinueMode(int)
	 */
	@Override
	public void setContinueMode(int mode) {
		if (cameras != null) {
			Camera.Parameters p = cameras.getParameters();
			p.set("camera-mode", mode);
			cameras.setParameters(p);
		}
	}

	/**
	 * 取消对焦
	 */
	public void cancalAutoFocus() {
		if (cameras != null) {
			cameras.cancelAutoFocus();
			camera_focusImageView.setVisibility(View.GONE);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * 设置连拍的张数
	 */
	@Override
	public void continueNum(int num) {
		try {
			Runtime.getRuntime().exec(
					"adb shell setprop persist.camera.snapshot.number " + num);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * 设置3D和2D的切换
	 */
	@Override
	public void is3D(boolean is3D) {
		if (is3D) {
			si.setScreenDimension(true);
			mTextureView.force3DMode(Display3D.VERTICAL_INTERLACED);

		} else {
			si.setScreenDimension(false);
			mTextureView.force3DMode(Display3D.MODE_2D);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * 初始化camera参数
	 */
	@Override
	public void initParm() {
		cameras.stopPreview();
		// setPreviewSize();
		setPreviewSize(800, 480);
		setFlashMode(2);
		camera_parameters.set("iso", "auto");
		cameras.setParameters(camera_parameters);
		// setSceneMode();
		// setWhiteBalanceMode();
		// setExposure();
		// setFocusMode(1);
		setPictureSize();
		setWhiteBalanceMode();
		setExposure();
		cameras.startPreview();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * 初始化变量
	 */
	@Override
	public void setSize(int previewwidth, int previewheight) {
		// if (VIDEO_FILE_LIST == null) {
		// VIDEO_FILE_LIST = new ArrayList<String>();
		// }
		// if (PIC_FILE_LIST == null) {
		// PIC_FILE_LIST = new ArrayList<String>();
		// }
		this.previewwidth = previewwidth;
		this.previewheight = previewheight;
		camera_focusImageView = WiCameraActivity.m_iv_camera_focus;
		camera_spareSpace = WiCameraActivity.m_btn_camera_newpic_sizeofsum;
		camera_newpic_thumbnails = WiCameraActivity.m_iv_camera_newpic_thumbnails;
		camera_continuous = WiCameraActivity.m_btn_camera_continuous;
		camera_review_thumbnails = WiCameraActivity.m_iv_camera_review_thumbnails;
		spareSpace = OperationFile.readSDCard();
		// camera_spareSpace.setText(spareSpace + "/" + "" + newpicsize + "M");
		// System.out.println("setSize=" + camera_spareSpace.getText());
		Bitmap bit = null;
		String preReviewPath = StoredData.getString(
				StoredData.M_REVIEWFILEPATH, "a");
		System.out.println("保存路径为:" + preReviewPath);
		if (preReviewPath.equals("a")) {
			// camera_newpic_thumbnails.setImageBitmap(BitmapFactory
			// .decodeResource(getResources(), R.drawable.gray));
			camera_newpic_thumbnails.setBackgroundResource(R.drawable.gray);
			WiCameraActivity.newpic_thumbnails_parent.setVisibility(View.GONE);
			return;
		} else {
			File f = new File(preReviewPath);
			if (f.exists()) {
				if (preReviewPath.endsWith("mp4")) {
					bit = OperationFile.getVideotThumbnail(preReviewPath, 90,
							90);

				} else {
					bit = OperationFile.fitSizeImg(preReviewPath, 90, 90, 90);
				}
				camera_newpic_thumbnails.setImageBitmap(bit);
			} else {
				// camera_newpic_thumbnails.setImageBitmap(BitmapFactory
				// .decodeResource(getResources(), R.drawable.gray));
				WiCameraActivity.newpic_thumbnails_parent
						.setVisibility(View.GONE);
				camera_newpic_thumbnails.setBackgroundResource(R.drawable.gray);
			}

		}
		// if (bit != null) {
		// bit.recycle();
		// }
		list = new ArrayList<byte[]>();
		photonum = 0;
		if (continous_array != null) {
			continous_array.clear();
			continous_array = null;
		}
		continous_array = new ArrayList<String>();

		// camera_onClickListener = new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// int clickcount = 0;
		// switch (v.getId()) {
		// case R.id.btn_camera_capture:
		// if (CAMERA_STATE != CAMERA_VIDEO) {
		//
		// setSelfTime();
		// } else {
		// if (clickcount % 2 == 0) {
		//
		// } else {
		//
		// }
		// ++clickcount;
		// }
		//
		// break;
		// case R.id.btn_camera_continuous:
		// // TODO Auto-generated method stub
		// camera_continuous.setEnabled(false);
		//
		// if (cameras != null) {
		// Util.setRotationParameter(camera_parameters,
		// m_cameraIndex, WiCameraActivity.mOrientation);
		// cameras.setParameters(camera_parameters);
		// }
		// list = new ArrayList<byte[]>();
		// photonum = 0;
		// StoredData.saveInt(StoredData.M_CAMERA_STATE,
		// CAMERA_CONTINUOUS);
		// CAMERA_STATE = CAMERA_CONTINUOUS;
		//
		// if (FOCU_STATE == STATE_FOCUSING) {
		// FOCU_STATE = STATE_FOCUSING_SNAP_ON_FINISH;
		// } else {
		// myTakePic();
		// }
		// break;
		// case R.id.btn_camera_used_camera:
		// switchCamera();
		// break;
		// default:
		// break;
		// }
		// }
		// };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * 连拍
	 */
	@Override
	public void continueCapture() {
		// camera_continuous.setEnabled(false);
		int time = StoredData.getInt(StoredData.M_SELFTIMER, 0);
		if (cameras != null) {
			// Util.setRotationParameter(camera_parameters, m_cameraIndex,
			// WiCameraActivity.mOrientation);
			// cameras.setParameters(camera_parameters);
			setRotationParm();
		}

		// list = new ArrayList<byte[]>();
		// photonum = 0;
		// if (continous_array != null) {
		// continous_array.clear();
		// continous_array = null;
		// }
		// continous_array = new ArrayList<String>();

		StoredData.saveInt(StoredData.M_CAMERA_STATE,
				CAMERA_CONTINUOUSANDSELFTIME);
		// CAMERA_STATE = CAMERA_CONTINUOUS;

		if (FOCU_STATE == STATE_FOCUSING) {
			FOCU_STATE = STATE_FOCUSING_SNAP_ON_FINISH;
		} else {
			// myTakePic();
			handler.sendEmptyMessageDelayed(CAMERA_CONTINUOUSANDSELFTIME, 0);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wistron.WiCamera.Inter#isMute(boolean)
	 */
	@Override
	public void isMute(boolean isMute) {
		audioManager.setStreamMute(AudioManager.STREAM_MUSIC, isMute); // 设置是否静音
		// if(isMute){
		//
		// audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		// audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
		// AudioManager.VIBRATE_SETTING_OFF);
		// audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
		// AudioManager.VIBRATE_SETTING_OFF);
		// }else{
		// audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		// audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
		// AudioManager.VIBRATE_SETTING_OFF);
		// audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
		// AudioManager.VIBRATE_SETTING_OFF);
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * 设置对焦模式
	 */
	@Override
	public void setFocusMode(int focusmode) {
		try {
			if (cameras != null) {

				cameras.cancelAutoFocus();
				boolean videosupported = camera_parameters
						.isVideoSnapshotSupported();
				boolean isfocusmodesupported = isSupported(
						CAMERA_FOCUSMODE[focusmode],
						camera_parameters.getSupportedFocusModes());
				if (focusmode == 2 && videosupported) {
					camera_parameters.setFocusMode(CAMERA_FOCUSMODE[focusmode]);

				} else if (focusmode == 1) {
					if (isfocusmodesupported) {
						camera_parameters
								.setFocusMode(CAMERA_FOCUSMODE[focusmode]);
					} else {
						camera_parameters.setFocusMode(CAMERA_FOCUSMODE[0]);
					}
				}
				// cameras.startSmoothZoom(m_zoomValue);
				cameras.setParameters(camera_parameters);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * 转换前后照相头
	 */
	// boolean isRear = true;

	@Override
	public boolean switchCamera() {
		WiCameraActivity.m_fd_face.setVisibility(View.GONE);
		boolean isRear = true;
		m_isPreview = false;
		if (m_cameraIndex == m_camera_back) {
			m_cameraIndex = m_camera_front;
			StoredData.saveBoolean("isRear", false);
			isRear = false;
		} else if (m_cameraIndex == m_camera_front) {
			m_cameraIndex = m_camera_back;
			StoredData.saveBoolean("isRear", true);
			isRear = true;
		}
		if (!m_isPreview && cameras != null) {
			cameras.stopPreview();
			cameras.release();
			cameras = null;
			try {
				cameras = Camera.open(m_cameraIndex);
				m_isPreview = true;
			} catch (Exception e) {
				if (m_cameraIndex == m_camera_front) {
					m_cameraIndex = m_camera_back;
				} else {
					m_cameraIndex = m_camera_front;
				}
			}
		}
		if (cameras != null && m_isPreview) {
			try {
				// mTextureView.setInputFormat(800, 480, Image3D.LAYOUT_2D, 1.0,
				// false, true, Image3D.QUALITY_FASTEST);
				// 设定照相类型
				cameras.setPreviewTexture(mSurfaceTexture);
				camera_parameters = cameras.getParameters();
				// setFocusMode(1);
				// updateCameraParametersInitialize();
				// setPreviewSize();
				startPreview();
				System.out.println("切换照相头哦....");
				m_isPreview = true;
			} catch (Exception e) {
				cameras.release(); // 释放摄像头资源
				cameras = null;
			} finally {
				if (isRear) {
					initParm();
					startFaceDetection();
				}
			}
		}
		return isRear;
	}

	// ////////////////////////////////////////////////////////////////////////////////

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wistron.WiCamera.Inter#onSurfaceTextureAvailable(android.graphics
	 * .SurfaceTexture, int, int)
	 */
	@Override
	public void onSurfaceTextureAvailable(SurfaceTexture surface, int width,
			int height) {
		mSurfaceTexture = surface;
		surfaceCreated();
		surfaceChanged();

		try {
			if (cameras != null) {
				// List<Size> lst = cameras.getParameters()
				// .getSupportedPreviewSizes();
				// for (int i = 0; i < lst.size(); i++) {
				// System.out.println("w=" + lst.get(i).width + "h="
				// + lst.get(i).height);
				// }
				Camera.Size size = cameras.getParameters().getPreviewSize();

				mTextureView.setInputFormat(size.width, size.height,
						Image3D.LAYOUT_2D, 1.0, false, true,
						Image3D.QUALITY_FASTEST);
				cameras.setPreviewTexture(surface);
				cameras.startPreview();
			}
		} catch (IOException ioe) {
			// Something bad happened
		}
	}

	// @Override
	// protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	// {
	// //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	// double aspect = (this.mImage == null) ? 1.0D :
	// this.mImage.getPixelAspectRatio();
	// int width = getDefaultSize((int)(this.mVideoWidth * aspect),
	// widthMeasureSpec);
	// int height = getDefaultSize(this.mVideoHeight, heightMeasureSpec);
	// if ((this.mVideoWidth > 0) && (this.mVideoHeight > 0)) {
	// if (this.mVideoWidth * aspect * height > width * this.mVideoHeight)
	// {
	// height = (int)(width * this.mVideoHeight / (this.mVideoWidth * aspect));
	// } else if (this.mVideoWidth * aspect * height < width *
	// this.mVideoHeight)
	// {
	// width = (int)(height * this.mVideoWidth * aspect / this.mVideoHeight);
	// }
	//
	// }
	// setMeasuredDimension(width, height);
	// }
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wistron.WiCamera.Inter#onSurfaceTextureSizeChanged(android.graphics
	 * .SurfaceTexture, int, int)
	 */
	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width,
			int height) {
		Log.e("VideoSurfaceView", "onSurfaceTextureSizeChanged");
		boolean newSurface = surface != mSurfaceTexture;
		try {
			if (cameras != null) {
				if (newSurface) {

					mSurfaceTexture.release();
					mSurfaceTexture = surface;

					cameras.stopPreview();
					Camera.Size size = cameras.getParameters().getPreviewSize();
					mTextureView.setInputFormat(size.width, size.height,
							Image3D.LAYOUT_2D, 1.0, false, true,
							Image3D.QUALITY_FASTEST);
					Log.e("onSurfaceTextureSizeChanged", "size" + size.width
							+ size.height);
					cameras.setPreviewTexture(mSurfaceTexture);
					cameras.startPreview();
					// 判定为进入全景拍摄
					if (size.width == 640 && size.height == 480) {
						//
					}
					// WiCameraActivity.isClickable=true;
					// Log.e("onSurfaceTextureSizeChanged", "按钮置为可用了");
					WiCameraActivity.mMainHandler.postDelayed(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							for (int i = 0; i < WiCameraActivity.m_camera_overlayui_array.length; i++) {
								if (WiCameraActivity.m_camera_overlayui_array[i] != null
										&& i != 0) {
									WiCameraActivity.m_camera_overlayui_array[i]
											.setClickable(true);
									WiCameraActivity.m_camera_overlayui_array[i]
											.setEnabled(true);
									WiCameraActivity.m_camera_overlayui_array[i]
											.setFocusable(true);
								}
							}
							if (WiCameraActivity.m_camera_overlayui_array != null) {
								System.out.println("全景退出 ......"
										+ WiCameraActivity.isPanoramaMode);
								if (WiCameraActivity.isPanoramaMode) {
									WiCameraActivity.m_camera_overlayui_array[10]
											.setEnabled(false);
									WiCameraActivity.m_camera_overlayui_array[10]
											.setClickable(false);
									WiCameraActivity.m_camera_overlayui_array[10]
											.setFocusable(false);
								} else {
									WiCameraActivity.m_camera_overlayui_array[10]
											.setEnabled(true);
									WiCameraActivity.m_camera_overlayui_array[10]
											.setClickable(true);
									WiCameraActivity.m_camera_overlayui_array[10]
											.setFocusable(true);
								}

							}
							Log.e("onSurfaceTextureSizeChanged", "按钮置为可用了");
						}
					}, 1000);
					// WiCameraActivity.mMainHandler.post();
					// for(int
					// i=0;i<WiCameraActivity.m_camera_overlayui_array.length;i++)
					// {
					// if(WiCameraActivity.m_camera_overlayui_array[i]!=null)
					// {
					// WiCameraActivity.m_camera_overlayui_array[i].setClickable(true);
					// WiCameraActivity.m_camera_overlayui_array[i].setEnabled(true);
					// }
					// }
				} else {
					WiCameraActivity.mMainHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							WiCameraActivity.isCameraOpen = true;
							for (int i = 0; i < WiCameraActivity.m_camera_overlayui_array.length; i++) {
								if (Util.CAMERA_STATE == CAMERA_VIDEO) {
									if (i != 13 && i != 14 && i != 10) {
										WiCameraActivity.m_camera_overlayui_array[i]
												.setEnabled(true);
										WiCameraActivity.m_camera_overlayui_array[i]
												.setClickable(true);
										WiCameraActivity.m_camera_overlayui_array[i]
												.setEnabled(true);
									}
								} else {
									WiCameraActivity.m_camera_overlayui_array[i]
											.setEnabled(true);
									WiCameraActivity.m_camera_overlayui_array[i]
											.setClickable(true);
									WiCameraActivity.m_camera_overlayui_array[i]
											.setEnabled(true);
								}
							}
							// 3D模式
							if (CSStaticData.g_is_3D_mode) {
								WiCameraActivity.m_camera_overlayui_array[12]
										.setEnabled(false);
								WiCameraActivity.m_camera_overlayui_array[12]
										.setClickable(false);
								WiCameraActivity.m_camera_overlayui_array[12]
										.setFocusable(false);
								WiCameraActivity.m_camera_overlayui_array[13]
										.setEnabled(false);
								WiCameraActivity.m_camera_overlayui_array[13]
										.setClickable(false);
								WiCameraActivity.m_camera_overlayui_array[13]
										.setFocusable(false);
							}
							// 全景
							if (WiCameraActivity.m_camera_overlayui_array != null) {
								if (WiCameraActivity.isPanoramaMode) {
									WiCameraActivity.m_camera_overlayui_array[10]
											.setEnabled(false);
									WiCameraActivity.m_camera_overlayui_array[10]
											.setClickable(false);
									WiCameraActivity.m_camera_overlayui_array[10]
											.setFocusable(false);
								} else {
									WiCameraActivity.m_camera_overlayui_array[10]
											.setEnabled(true);
									WiCameraActivity.m_camera_overlayui_array[10]
											.setClickable(true);
									WiCameraActivity.m_camera_overlayui_array[10]
											.setFocusable(true);
								}

							}
							// 前置摄像头
							if (!WiCameraActivity.isRear) {
								WiCameraActivity.m_camera_overlayui_array[10]
										.setEnabled(false);
								WiCameraActivity.m_camera_overlayui_array[13]
										.setClickable(false);
								WiCameraActivity.m_camera_overlayui_array[10]
										.setFocusable(false);
								WiCameraActivity.m_camera_overlayui_array[10]
										.setClickable(false);
								WiCameraActivity.m_camera_overlayui_array[13]
										.setFocusable(false);
								WiCameraActivity.m_camera_overlayui_array[13]
										.setClickable(false);

							}
						}
					}, 1000);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();

		}
		postInvalidate();
		// Ignored, Camera does all the work for us
		System.out.println("onSurfaceTextureSizeChanged.....");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wistron.WiCamera.Inter#onSurfaceTextureDestroyed(android.graphics
	 * .SurfaceTexture)
	 */
	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
		surfaceDestroyed();
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wistron.WiCamera.Inter#onSurfaceTextureUpdated(android.graphics.
	 * SurfaceTexture)
	 */
	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture surface) {
		// Invoked every time there's a new Camera preview frame
		// System.out.println("onSurfaceTextureUpdated");
	}

	// ////////////////////////////////////////////////////////////////////////////

	/*
	 * (non-Javadoc)
	 * 
	 * 打开camera
	 */

	public void surfaceCreated() {
		System.out.println("回来了。。。。。。。glsur");
		// if (!m_isPreview) {
		CameraInfo info = new CameraInfo();
		for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
			Camera.getCameraInfo(i, info);
			if (info.facing == CameraInfo.CAMERA_FACING_FRONT) // 前置摄像头
				m_camera_front = i;
			else if (info.facing == CameraInfo.CAMERA_FACING_BACK)
				m_camera_back = i;
		}
		// }
		m_cameraIndex = m_camera_back;
		boolean isrears = WiCameraActivity.isRear;
		if (isrears) {
			// switchCamera();
			m_cameraIndex = m_camera_back;
		} else {
			m_cameraIndex = m_camera_front;
		}
		try {
			cameras = Camera.open(m_cameraIndex); // 获取摄像头对象
			// 设定照相类型
			// cameras.setPreviewDisplay(holder);
			camera_parameters = cameras.getParameters();
			int maxfacenum = camera_parameters.getMaxNumDetectedFaces();
			isFacedetectSupported = maxfacenum > 0 ? true : false;
			// setFocusMode(1);
			updateCameraParametersInitialize();
			// setPictureSize();
			// setPreviewSize();
			// cameras.setPreviewDisplay(holder);
			// startPreview();
			// initParm();
			m_isPreview = true;
			setDisplayOrientation();

		} catch (Exception e) {
			// e.printStackTrace();
			releaseCamera();
		}
	}

	// public void setOrientation() {
	// Util.setRotationParameter(camera_parameters, m_cameraIndex,
	// WiCameraActivity.mOrientation);
	// // System.out.println("拍照时的mOrientation="
	// // + WiCameraActivity.mOrientation + "m_cameraIndex="
	// // + m_cameraIndex);
	// cameras.setParameters(camera_parameters);
	// }

	/*
	 * (non-Javadoc) 人脸识别
	 * 
	 * @see
	 * com.wistron.WiCamera.Inter#onFaceDetection(android.hardware.Camera.Face
	 * [], android.hardware.Camera)
	 */
	@Override
	public void onFaceDetection(android.hardware.Camera.Face[] faces,
			Camera camera) {
		boolean isfacedet = StoredData.getBoolean(StoredData.M_FACETRACKING,
				false);
		// System.out.println("是否可以人脸识别：" + isfacedet);
		if (!isfacedet
				|| (m_cameraIndex == m_camera_front
						|| Util.CAMERA_STATE == CAMERA_VIDEO
						|| WiCameraActivity.isContinus || (!WiCameraActivity.isCameraOpen))) {
			WiCameraActivity.m_fd_face.setVisibility(View.GONE);
			return;
		}
		if (m_isfacedetection) {
			WiCameraActivity.m_fd_face.setVisibility(View.VISIBLE);
			// if (FOCU_STATE == STATE_FOCUSING) {
			// WiCameraActivity.m_fd_face.setVisibility(View.GONE);
			// return;
			// }
			// TODO Auto-generated method stub
			// TODO Auto-generated method stub
			Rect[] rectarrayRects = new Rect[faces.length];

			for (int i = 0; i < faces.length; i++) {
				rectarrayRects[i] = faces[i].rect;
			}
			// TODO Auto-generated method stub
			boolean isneedfocus = isNeedFocus(oldRect, rectarrayRects);
			WiCameraActivity.m_fd_face.setFaces(faces);
			if (isneedfocus) {

				if ((mFocusArea != null)
						&& (FOCU_STATE == STATE_SUCCESS || FOCU_STATE == STATE_FAIL)) {
					mFocusArea = null;
					mMeteringArea = null;
					cameras.cancelAutoFocus();
					FOCU_STATE = STATE_IDLE;
				}
				mFocusArea = new ArrayList<Camera.Area>();
				mMeteringArea = new ArrayList<Camera.Area>();
				if (cameras != null) {
					for (int i = 0; i < faces.length; i++) {
						mFocusArea.add(new Area(faces[i].rect, 100));
						mMeteringArea.add(new Area(faces[i].rect, 100));
					}
				}
				// FOCU_STATE = STATE_FOCUSING;
				setArea();
			}
			oldRect = rectarrayRects;
		}
	}

	/*
	 * (non-Javadoc) 计算是否需要人脸识别后对焦
	 * 
	 * @see com.wistron.WiCamera.Inter#isNeedFocus(android.graphics.Rect[],
	 * android.graphics.Rect[])
	 */
	@Override
	public boolean isNeedFocus(Rect[] oldRect, Rect[] newRect) {
		// System.out.println("newrect.length=" + newRect.length);
		if (newRect.length <= 0) {
			return false;
		}

		if (Math.abs(oldRect.length - newRect.length) > 0) {
			return true;
		}
		int oldx = 0, oldy = 0;
		int newx = 0, newy = 0;
		int diffx = 0, diffy = 0;
		for (int i = 0; i < oldRect.length; i++) {
			oldx += oldRect[i].centerX();
			oldy += oldRect[i].centerY();
		}

		for (int i = 0; i < newRect.length; i++) {
			newx += newRect[i].centerX();
			newy += newRect[i].centerY();
		}
		diffx = Math.abs(newx - oldx);
		diffy = Math.abs(newy - oldy);
		if (diffx >= 105 || diffy >= 105) {
			return true;
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * 设置camera的旋转方向
	 */
	@Override
	public void setDisplayOrientation() {
		int mDisplayRotation = Util.getDisplayRotation((Activity) m_context);
		int mDisplayOrientation = Util.getDisplayOrientation(mDisplayRotation,
				CameraInfo.CAMERA_FACING_BACK);
		cameras.setDisplayOrientation(mDisplayOrientation);
		if (WiCameraActivity.m_fd_face != null) {
			WiCameraActivity.m_fd_face
					.setDisplayOrientation(mDisplayOrientation);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wistron.WiCamera.Inter#setOrientationIndicator(int)
	 */
	@Override
	public void setOrientationIndicator(int degree) {
		if (WiCameraActivity.m_fd_face != null)
			WiCameraActivity.m_fd_face.setOrientation(degree);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * camera打开后设置camera参数
	 */
	@Override
	public void surfaceChanged() {
		try {
			if (cameras != null) {
				if (m_cameraIndex == m_camera_back) {
					initParm();
					startFaceDetection();
				} else {
					setPreviewSize(800, 480);
				}
				// String iso = camera_parameters.get("iso-mode-values");
				String iso = camera_parameters.get("iso-values");
				// System.out
				// .println("iso=================================" + iso);
				// 支持的照相预览的大小
				List<Size> listSizes = cameras.getParameters()
						.getSupportedPictureSizes();
				// for (int i = 0; i < listSizes.size(); i++) {
				// System.out.println("支持的照相照片的大小：w=" + listSizes.get(i).width
				// + "h=" + listSizes.get(i).height);
				//
				// }
				int exposure_Level = (int) (CAMERA_EXPOSURE_ALL[5] / camera_parameters
						.getExposureCompensationStep());
				// 支持的录像大小
				List<Size> videosizes = cameras.getParameters()
						.getSupportedVideoSizes();
				for (int i = 0; i < videosizes.size(); i++) {
					// System.out.println("支持的录像大小：w=" + videosizes.get(i).width
					// + "h=" + videosizes.get(i).height);

				}
				cameras.setParameters(camera_parameters);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		if (WiCameraActivity.isPanoramaMode) {
			WiCameraActivity.m_main_handle.sendEmptyMessage(500);
		}
		// isSingleMode(false);
	}

	/*
	 * (non-Javadoc) surface销毁后释放照相头资源
	 * 
	 * @see com.wistron.WiCamera.Inter#surfaceDestroyed()
	 */
	@Override
	public void surfaceDestroyed() {
		// 当Surface被销毁的时候，该方法被调用
		FOCU_STATE = STATE_IDLE;
		// isSingleMode(true);
		// 在这里需要释放Camera资源
		// if (cameras != null) {
		// if (isFacedetectSupported) {
		// // cameras.setFaceDetectionListener(null);
		// // cameras.stopFaceDetection();
		// }
		// m_isPreview = false;
		// cameras.setPreviewCallback(null);
		// cameras.stopPreview();
		// cameras.release();
		// cameras = null;
		// }
		stopPreview();
		releaseCamera();
	}

	/*
	 * (non-Javadoc) 设定焦距值，进行预览的放大和缩小
	 * 
	 * @see com.wistron.WiCamera.Inter#setZoomSize(int)
	 */
	@Override
	public void setZoomSize(int value) {
		if (cameras != null) {
			try {
				camera_parameters.setZoom(value);
				cameras.setParameters(camera_parameters);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	}

	/*
	 * (non-Javadoc) 设置iso
	 * 
	 * @see com.wistron.WiCamera.Inter#setISO()
	 */
	@Override
	public void setISO() {
		if (cameras != null) {
			try {
				int iso = StoredData.getInt(StoredData.M_ISO, 0);
				camera_parameters.set("iso", CAMERA_ISO_ARRAY[iso]);
				cameras.setParameters(camera_parameters);
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("此iso不支持");
			}
			Parameters p = cameras.getParameters();
			System.out.println("设置iso=============" + p.get("iso"));
		}
	}

	/*
	 * (non-Javadoc) 设置图片大小
	 * 
	 * @see com.wistron.WiCamera.Inter#setPictureSize()
	 */
	@Override
	public void setPictureSize() {
		try {
			int picsize = StoredData.getInt(StoredData.M_PICTURESIZE, 2);
			camera_parameters.setPictureSize(CAMERA_PICSIZE[picsize][0],
					CAMERA_PICSIZE[picsize][1]);
			cameras.setParameters(camera_parameters);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("这种照片大小不支持");
		}

	}

	/*
	 * (non-Javadoc) 设置预览大小
	 * 
	 * @see com.wistron.WiCamera.Inter#setPreviewSize(int, int)
	 */
	@Override
	public void setPreviewSize(int width, int height) {
		try {
			if (cameras != null) {
				camera_parameters.setPreviewSize(width, height);
				cameras.setParameters(camera_parameters);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wistron.WiCamera.Inter#getHorizontalViewAngle()
	 */
	@Override
	public float getHorizontalViewAngle() {
		float angle = 0;
		try {
			if (cameras != null) {
				Parameters temParameters = cameras.getParameters();
				angle = temParameters.getHorizontalViewAngle();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return angle;

	}

	/*
	 * (non-Javadoc) 根据图片大小设置预览大小
	 * 
	 * @see com.wistron.WiCamera.Inter#setPreviewSize()
	 */
	@Override
	public void setPreviewSize() {
		try {
			if (cameras != null) {

				// camera_parameters
				// .setPreviewSize(WiCameraActivity.screenWidth
				// + WiCameraActivity.sbarh,
				// WiCameraActivity.screenHeight);
				List<Size> sizes = camera_parameters.getSupportedPreviewSizes();
				int picsize = StoredData.getInt(StoredData.M_PICTURESIZE, 0);
				Size optimalSize = Util.getOptimalPreviewSize(
						(Activity) m_context, sizes,
						(double) CAMERA_PICSIZE[picsize][0]
								/ CAMERA_PICSIZE[picsize][1]);
				Size original = camera_parameters.getPreviewSize();
				if (!original.equals(optimalSize)) {
					System.out.println("预览大小:宽=" + optimalSize.width + "高="
							+ optimalSize.height);
					camera_parameters.setPreviewSize(optimalSize.width,
							optimalSize.height);
					cameras.setParameters(camera_parameters);
				}

				cameras.setParameters(camera_parameters);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc) 开始预览
	 * 
	 * @see com.wistron.WiCamera.Inter#startPreview()
	 */
	@Override
	public void startPreview() {
		// 先使camera处于停止预览状态
		if (m_isPreview) {
			stopPreview();
		}

		// 开始预览
		if (cameras != null) {
			cameras.startPreview();
		} else {
			if (CSStaticData.DEBUG) {
				Log.e("Cocoonshu", "[startPreview]cameras为空");
			}
		}
		m_isPreview = true;
	}

	/*
	 * (non-Javadoc) 停止预览
	 * 
	 * @see com.wistron.WiCamera.Inter#stopPreview()
	 */
	@Override
	public void stopPreview() {
		// 如果正在预览，就停止预览
		if (m_isPreview == true) {
			if (cameras != null) {
				cameras.stopPreview();
				m_isPreview = false;
				FOCU_STATE = STATE_IDLE;
			}

		}
	}

	/*
	 * (non-Javadoc) 设置场景模式
	 * 
	 * @see com.wistron.WiCamera.Inter#setSceneMode()
	 */
	@Override
	public void setSceneMode() {
		try {
			int sceneMode = StoredData.getInt(StoredData.M_SCENEMODE, 0);
			boolean issupportedscene = isSupported(
					CAMERA_SCENEMODE_ARRAY[sceneMode],
					camera_parameters.getSupportedSceneModes());
			if (issupportedscene) {
				camera_parameters
						.setSceneMode(CAMERA_SCENEMODE_ARRAY[sceneMode]);
				cameras.setParameters(camera_parameters);
			} else {
				System.out.println("不支持这种scenemode");
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc) 设置图片的旋转角度
	 * 
	 * @see com.wistron.WiCamera.Inter#setRotationParm()
	 */
	@Override
	public void setRotationParm() {
		try {
			Parameters p = cameras.getParameters();
			Util.setRotationParameter(p, m_cameraIndex,
					WiCameraActivity.mOrientation);
			System.out.println("拍照时的mOrientation="
					+ WiCameraActivity.mOrientation + "m_cameraIndex="
					+ m_cameraIndex);
			cameras.setParameters(p);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("设定角度失败");
		}
	}

	/*
	 * (non-Javadoc) 按下capture按钮后单拍
	 * 
	 * @see com.wistron.WiCamera.Inter#setSelfTime()
	 */
	@Override
	public void setSelfTime() {
		int time = StoredData.getInt(StoredData.M_SELFTIMER, 0);

		if (time == 0) {
			Util.CAMERA_STATE = CAMERA_SINGLE_SHOT;
			StoredData.saveInt(StoredData.M_CAMERA_STATE, CAMERA_SINGLE_SHOT);
			if (cameras != null) {
				// try {
				// Util.setRotationParameter(camera_parameters, m_cameraIndex,
				// WiCameraActivity.mOrientation);
				// // System.out.println("拍照时的mOrientation="
				// // + WiCameraActivity.mOrientation + "m_cameraIndex="
				// // + m_cameraIndex);
				// cameras.setParameters(camera_parameters);
				// } catch (Exception e) {
				// // TODO: handle exception
				// System.out.println("设定角度失败");
				// }
				setRotationParm();
			}
			isToFocusToCapture = true;
			if (FOCU_STATE == STATE_FOCUSING) {
				FOCU_STATE = STATE_FOCUSING_SNAP_ON_FINISH;
			} else {
				if (cameras != null) {
					// cameras.takePicture(null, null, null,
					// VideoSurfaceView.this);
					// isToFocusToCapture = true;
					cameras.cancelAutoFocus();
					try {
						cameras.autoFocus(myAutoFocusCallback);
					} catch (Exception e) {
						// TODO: handle exception
						WiCameraActivity.m_main_handle.removeMessages(3);
						WiCameraActivity.m_main_handle.sendEmptyMessage(3);
						cameras.cancelAutoFocus();
					}

					LayoutParams lp = (LayoutParams) camera_focusImageView
							.getLayoutParams();
					lp.addRule(RelativeLayout.CENTER_IN_PARENT);
					camera_focusImageView.setLayoutParams(lp);

					camera_focusImageView.setVisibility(View.VISIBLE);
				}
			}
		} else {

			handler.sendEmptyMessageDelayed(CAMERA_SELFTIME, 0);
			// if (cameras != null) {
			// cameras.takePicture(null, null, null, VideoSurfaceView.this);
			// }
		}

	}

	/*
	 * (non-Javadoc) 单拍
	 * 
	 * @see com.wistron.WiCamera.Inter#setSelfTimes()
	 */
	@Override
	public void setSelfTimes() {
		int time = StoredData.getInt(StoredData.M_SELFTIMER, 0);

		// if (time == 0) {
		Util.CAMERA_STATE = CAMERA_SINGLE_SHOT;
		StoredData.saveInt(StoredData.M_CAMERA_STATE, CAMERA_SINGLE_SHOT);
		if (cameras != null) {
			// try {
			// Util.setRotationParameter(camera_parameters, m_cameraIndex,
			// WiCameraActivity.mOrientation);
			// // System.out.println("拍照时的mOrientation="
			// // + WiCameraActivity.mOrientation + "m_cameraIndex="
			// // + m_cameraIndex);
			// cameras.setParameters(camera_parameters);
			// } catch (Exception e) {
			// // TODO: handle exception
			// System.out.println("设定角度失败");
			// }
			setRotationParm();
		}
		// if (FOCU_STATE == STATE_FOCUSING) {
		// FOCU_STATE = STATE_FOCUSING_SNAP_ON_FINISH;
		// } else {
		if (cameras != null) {
			cameras.takePicture(null, null, null, VideoSurfaceView.this);
		}
	}

	// } else {
	// handler.sendEmptyMessageDelayed(CAMERA_SELFTIME, 0);
	// // if (cameras != null) {
	// // cameras.takePicture(null, null, null, VideoSurfaceView.this);
	// // }
	// }

	// }

	/*
	 * (non-Javadoc) 设置闪光灯
	 * 
	 * @see com.wistron.WiCamera.Inter#setFlashMode(int)
	 */
	@Override
	public void setFlashMode(int flashmode) {
		try {
			if (cameras != null) {
				// int flashMode = StoredData.getInt(StoredData.M_FLASHMODE, 2);
				// boolean isflashmodesupported = isSupported(
				// CAMERA_FLASHARRAY[flashMode],
				// camera_parameters.getSupportedFlashModes());
				// if (isflashmodesupported) {
				camera_parameters.setFlashMode(CAMERA_FLASHARRAY[flashmode]);
				cameras.setParameters(camera_parameters);
				// } else {

				// }
				// System.out.println("flashMode=" + flashMode);
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("这种flashmode不支持");
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc) 设置白平衡
	 * 
	 * @see com.wistron.WiCamera.Inter#setWhiteBalanceMode()
	 */
	@Override
	public void setWhiteBalanceMode() {
		if (cameras != null) {
			cameras.stopPreview();
			try {
				int whiteBalanceMode = StoredData.getInt(
						StoredData.M_WHITEBALANCE, 0);
				boolean isflahsmodesupported = isSupported(
						CAMERA_WHITEBALANCE_ARRAY[whiteBalanceMode],
						camera_parameters.getSupportedWhiteBalance());
				if (isflahsmodesupported) {
					camera_parameters
							.setWhiteBalance(CAMERA_WHITEBALANCE_ARRAY[whiteBalanceMode]);
					cameras.setParameters(camera_parameters);
				} else {
					System.out.println("这种白平衡不支持");
				}

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			cameras.startPreview();
		}
	}

	/*
	 * (non-Javadoc) 恢复默认
	 * 
	 * @see com.wistron.WiCamera.Inter#restoreDefault()
	 */
	@Override
	public void restoreDefault() {
		if (cameras != null) {
			StoredData.saveInt(StoredData.M_SCENEMODE, 0);
			StoredData.saveInt(StoredData.M_FLASHMODE, 0);
			StoredData.saveInt(StoredData.M_WHITEBALANCE, 0);
			StoredData.saveInt(StoredData.M_EXPOSURE, 4);
			StoredData.saveInt(StoredData.M_PICTURESIZE, 2);
			// StoredData.saveInt(StoredData.M_SELFTIMER, 0);
			StoredData.saveBoolean(StoredData.M_SOUNDMODE, true);
			StoredData.saveBoolean(StoredData.M_SMILESHOT, false);
			StoredData.saveBoolean(StoredData.m_GSENSOR, true);
			StoredData.saveBoolean(StoredData.M_HJR, true);
			StoredData.saveBoolean(StoredData.M_TOUCHFOCUS, true);
			StoredData.saveBoolean(StoredData.M_GRIDDISINFINDER, false);
			// StoredData.saveBoolean(StoredData.M_REDEYEREMOVAL, false);
			StoredData.saveBoolean(StoredData.M_REVIEW, true);
			boolean isExt = OperationFile.isExtSdcardExists();
			boolean isInt = OperationFile.isIntSdcardExists();
			if (isExt && isInt) {
				int storageMode = StoredData
						.getInt(StoredData.M_STORAGEMODE, 0);
				StoredData.saveInt(StoredData.M_STORAGEMODE, 0);
			} else {
				if (isInt) {
					StoredData.saveInt(StoredData.M_STORAGEMODE, 0);
				}
				if (isExt) {
					StoredData.saveInt(StoredData.M_STORAGEMODE, 1);
				}
			}
			// StoredData.saveInt(StoredData.M_STORAGEMODE, 0);
			StoredData.saveInt(StoredData.M_CONTINUESHOTNUM, 0);
			StoredData.saveInt(StoredData.M_VIDEORESOLUTION, 1);

			StoredData.saveInt(StoredData.M_CONTINUESHOTNUM, 0);
			StoredData.saveBoolean(StoredData.M_FACETRACKING, false);
			StoredData.saveBoolean(StoredData.M_ADDTAG, true);
			StoredData.saveInt(StoredData.M_ISO, 0);

			try {

				camera_parameters.setSceneMode(CAMERA_SCENEMODE_ARRAY[0]);
				camera_parameters.setFlashMode(CAMERA_FLASHARRAY[2]);
				camera_parameters.setWhiteBalance(CAMERA_WHITEBALANCE_ARRAY[0]);
				camera_parameters
						.setExposureCompensation((int) (CAMERA_EXPOSURE_ALL[4] / camera_parameters
								.getExposureCompensationStep()));
				camera_parameters.setPictureSize(CAMERA_PICSIZE[2][0],
						CAMERA_PICSIZE[2][1]);
				cameras.setParameters(camera_parameters);
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("恢复默认出错");
			} finally {
				// cameras.setParameters(camera_parameters);
			}
		}
	}

	/*
	 * (non-Javadoc) 设置曝光度
	 * 
	 * @see com.wistron.WiCamera.Inter#setExposure()
	 */
	@Override
	public void setExposure() {
		try {
			if (cameras != null && camera_parameters != null) {
				int exposure_Level = StoredData
						.getInt(StoredData.M_EXPOSURE, 4);
				camera_parameters
						.setExposureCompensation((int) (CAMERA_EXPOSURE_ALL[exposure_Level] / camera_parameters
								.getExposureCompensationStep()));
				cameras.setParameters(camera_parameters);
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("不支持此曝光度");
		}

	}

	/*
	 * (non-Javadoc) 播放声音
	 * 
	 * @see com.wistron.WiCamera.Inter#PlaySounds(int)
	 */
	@Override
	public void PlaySounds(int a) {
		boolean isplaysound = StoredData.getBoolean(StoredData.M_SOUNDMODE,
				true);
		if (isplaysound) {
			// isMute(false);
			if (a == 0) {

				playSound(R.raw.camera_click);
			} else {
				playSound(R.raw.camera_focus);
			}
		} else {
			// isMute(true);
		}

	}

	/*
	 * (non-Javadoc) 释放Mediaplayer
	 * 
	 * @see com.wistron.WiCamera.Inter#releaseMediaplayer()
	 */
	@Override
	public void releaseMediaplayer() {
		// TODO Auto-generated method stub
		if (soundPlayer != null) {
			soundPlayer.release();
		}
	}

	/*
	 * (non-Javadoc) 根据声音id播放声音
	 * 
	 * @see com.wistron.WiCamera.Inter#playSound(int)
	 */
	@Override
	public void playSound(int id) {
		boolean isplaysound = StoredData.getBoolean(StoredData.M_SOUNDMODE,
				true);
		if (isplaysound) {
			// TODO Auto-generated method stub
			int num = StoredData.getInt(StoredData.M_CONTINUESHOTNUM, 0);
			soundPlayer = new SoundPlayer(getResources().openRawResourceFd(id),
					false, CAMERA_CONTINUESHOTNUMARRAY[num]);
			soundPlayer.play();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wistron.WiCamera.Inter#setSoundMode(int)
	 */
	@Override
	public void setSoundMode(int id) {
		// TODO Auto-generated method stub
		// int sound = StoredData.getInt(StoredData.M_SOUNDMODE, 0);
		AudioManager meng = (AudioManager) getContext().getSystemService(
				Context.AUDIO_SERVICE);
		int voice = meng.getStreamVolume(AudioManager.STREAM_NOTIFICATION);

		if (voice != 0) {
			if (m_mediaplayer == null) {
				m_mediaplayer = new MediaPlayer();
			}
			if (m_mediaplayer != null)
				try {
					m_mediaplayer.setDataSource(getResources()
							.openRawResourceFd(id).getFileDescriptor());
					m_mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
					m_mediaplayer.setLooping(false);
					m_mediaplayer.prepare();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			m_mediaplayer.start();
		}

	}

	/*
	 * (non-Javadoc) 释放camera资源
	 * 
	 * @see com.wistron.WiCamera.Inter#releaseCamera()
	 */
	@Override
	public void releaseCamera() {
		// TODO Auto-generated method stub
		if (cameras != null) {
			m_isPreview = false;
			cameras.stopPreview();
			cameras.cancelAutoFocus();
			cameras.release();
			cameras = null;
			releaseMediaplayer();

		}

	}

	/*
	 * (non-Javadoc) 开始人脸识别
	 * 
	 * @see com.wistron.WiCamera.Inter#startFaceDetection()
	 */
	@Override
	public void startFaceDetection() {
		try {
			if (cameras != null) {
				if (isFacedetectSupported) {
					if (m_cameraIndex == m_camera_back) {
						cameras.setFaceDetectionListener(this);
						camera_parameters.set("face-detection", "on");
						cameras.startFaceDetection();
						cameras.setParameters(camera_parameters);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc) 停止人脸识别
	 * 
	 * @see com.wistron.WiCamera.Inter#stopFaceDetection()
	 */
	@Override
	public void stopFaceDetection() {
		try {
			if (cameras != null) {
				if (isFacedetectSupported) {
					if (m_cameraIndex == m_camera_back) {
						camera_parameters.set("face-detection", "off");
						cameras.setFaceDetectionListener(null);
						cameras.stopFaceDetection();
						cameras.setParameters(camera_parameters);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wistron.WiCamera.Inter#myTakePic()
	 */
	@Override
	public void myTakePic() {
		if (cameras != null) {
			cameras.takePicture(null, null, null, VideoSurfaceView.this);
		}
	}

	public final class ShutterCallback implements
			android.hardware.Camera.ShutterCallback {
		public void onShutter() {
		}
	}

	public final class PostViewPictureCallback implements PictureCallback {
		public void onPictureTaken(byte[] data, android.hardware.Camera camera) {
		}
	}

	public final class RawPictureCallback implements PictureCallback {
		public void onPictureTaken(byte[] rawData,
				android.hardware.Camera camera) {
		}
	}

	public byte[] singleData;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wistron.WiCamera.Inter#onPictureTaken(byte[],
	 * android.hardware.Camera)
	 */
	@Override
	public void onPictureTaken(byte[] data, Camera cameras) {
		// if (WiCameraActivity.mCurrentDegree == 90) {
		// WiCameraActivity.isok = false;
		// } else if (WiCameraActivity.mCurrentDegree == 360) {
		// WiCameraActivity.isok = true;
		// }

		switch (Util.CAMERA_STATE) {
		case CAMERA_SINGLE_SHOT:
		case CAMERA_SELFTIME:
			PlaySounds(0);
			System.out.println("time3=........................."
					+ new Date().getTime());
			// 单拍
			// storedImage(data, -1);
			singleData = data;
			// cameras.startPreview();
			// PlaySounds();

			// newpicsize = (Math.round((double) data.length / (1024 * 1024)) +
			// "")
			// .substring(0, 1);

			// camera_spareSpace.setText(spareSpace + "/" + "" + newpicsize +
			// "M");
			handler.sendEmptyMessage(CAMERA_SINGLE_SHOT);
			// 重新预览，开始人脸识别
			// stopFaceDetection();
			startPreview();
			startFaceDetection();

			break;
		case CAMERA_CONTINUOUS:
		case CAMERA_CONTINUOUSANDSELFTIME:
			// if(!WiCameraActivity.isContinus){
			// if (soundPlayer != null) {
			// soundPlayer.release();
			// }
			// photonum = 0;
			// handler.sendEmptyMessage(CAMERA_CONTINUOUS);
			// // 重新预览，开始人脸识别
			// startPreview();
			// startFaceDetection();
			// }
			// if (WiCameraActivity.isToStopContinus) {
			// soundPlayer.release();
			// photonum = 0;
			// handler.sendEmptyMessage(CAMERA_CONTINUOUS);
			// // 重新预览，开始人脸识别
			// startPreview();
			// startFaceDetection();
			// return;
			// }
			PlaySounds(0);
			// newpicsize = (Math.round((double) data.length / (1024 * 1024)) +
			// "")
			// .substring(0, 1);
			spareSpace = OperationFile.readSDCard();
			camera_spareSpace.setText(spareSpace + "");
			camera_spareSpace.postInvalidate();
			// if(list.size()%5==0){
			// new MyAsyncTask(data, photonum).execute();
			// }
			list.add(data);
			// PlaySounds();

			// cameras.startPreview();
			++photonum;
			System.out.println("photonum=========" + photonum);
			// System.out.println("photonum=" + photonum);
			int numindex = StoredData.getInt(StoredData.M_CONTINUESHOTNUM, 0);
			int maxnum = CAMERA_CONTINUESHOTNUMARRAY[numindex];
			if (photonum < maxnum) {
				// System.out.println("photonum========="+photonum);
				// playSound();
				// myTakePic();
				// 设置开始计时时间
				int cameraSelfTimeId = StoredData.getInt(
						StoredData.M_SELFTIMER, 0);
				System.out.println("自定义上时间为。。。。。。。。。。。。。。。。。"
						+ cameraSelfTimeId);
				if (cameraSelfTimeId == 0) {
					// myTakePic();
					WiCameraActivity.m_main_handle.sendEmptyMessageDelayed(
							20000, 800);
					startPreview();
					startFaceDetection();
				} else {
					WiCameraActivity.m_main_handle.sendEmptyMessageDelayed(
							20000, 800);
					// WiCameraActivity.m_main_handle.sendEmptyMessage(10000);
					// myTakePic();
					// 重新预览，开始人脸识别
					// stopPreview();
					startPreview();
					startFaceDetection();
				}
			} else {
				PlaySounds(0);
				if (soundPlayer != null) {
					soundPlayer.release();
				}
				photonum = 0;
				handler.sendEmptyMessage(CAMERA_CONTINUOUS);
				// 重新预览，开始人脸识别
				startPreview();
				startFaceDetection();

				return;
			}

			break;
		case CAMERA_FOCUAREA:
			boolean isRecordTakePicSuport = cameras.getParameters()
					.isVideoSnapshotSupported();
			if (isRecordTakePicSuport) {
				// cameras.takePicture(null, null, null, this);
			}
			break;
		default:
			break;
		}
		FOCU_STATE = STATE_IDLE;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wistron.WiCamera.Inter#setOrientationHint()
	 */
	@Override
	public void setOrientationHint() {

		// Util.setRotationParameter(camera_parameters, m_cameraIndex,
		// WiCameraActivity.mOrientation);
		// // System.out.println("拍照时的mOrientation="
		// // + WiCameraActivity.mOrientation + "m_cameraIndex="
		// // + m_cameraIndex);
		// cameras.setParameters(camera_parameters);

		if (cameras != null) {
			int rotation = 0;
			if (WiCameraActivity.mOrientation != OrientationEventListener.ORIENTATION_UNKNOWN) {
				CameraInfo info = CameraHolder.instance().getCameraInfo()[m_cameraIndex];
				if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
					rotation = (info.orientation
							- WiCameraActivity.mOrientation + 360) % 360;
				} else { // back-facing camera
					rotation = (info.orientation + WiCameraActivity.mOrientation) % 360;
				}
			}
			m_recorder.setOrientationHint(rotation);
		}
	}

	/**
	 * 开始录制视频
	 */
	String videoTitle = null;

	/*
	 * (non-Javadoc) 开始录制
	 * 
	 * @see com.wistron.WiCamera.Inter#startRecord()
	 */
	@Override
	public void startRecord() {
		StoredData.saveInt(StoredData.M_CAMERA_STATE, CAMERA_VIDEO);
		try {
			Util.CAMERA_STATE = CAMERA_VIDEO;
			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			String dateparse = formatter.format(date);
			int storepath = StoredData.getInt(StoredData.M_STORAGEMODE, 0);
			// 视频文件的保存路径
			M_VIDEOPATH = CAMERA_PAHTARRAY[storepath] + "video-" + dateparse
					+ ".mp4";
			videoTitle = "video-" + dateparse;
			// 创建视频文件
			m_videofile = new File(M_VIDEOPATH);
			StoredData.saveString(StoredData.M_REVIEWFILEPATH, M_VIDEOPATH);
			// 如果父目录不存在,就创建目录
			if (!m_videofile.getParentFile().exists()) {
				m_videofile.getParentFile().mkdirs();
			}
			VIDEO_FILE_LIST.add(M_VIDEOPATH);
			setVideoPath(M_VIDEOPATH);
			// 使camera先处于停止状态
			stopRecord();
			// 以下开始进入录制状态
			m_isrecord = true;
			cameras.unlock();
			// 为了快速在预览和录制之间转换，故使用此函数
			m_recorder.setCamera(cameras);
			// 设置视频源为camera
			m_recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
			// 设置音频源为mic
			m_recorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);// mic
			// 设置输出文件的格式为mp4
			m_recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);// mp4
			// 声道
			// m_recorder.setAudioChannels(1);
			// 最长录制5000秒钟
			// m_recorder.setMaxDuration(5000000);
			// 最大录制500M
			// m_recorder.setMaxFileSize(500000000);
			// 2320k/s
			m_recorder.setVideoEncodingBitRate(2920000);// 2920000

			// m_recorder.setVideoFrameRate(25);//30
			m_recorder.setAudioSamplingRate(8000);// 8000
			m_recorder.setAudioEncodingBitRate(64000);// 12000
			m_recorder.setVideoFrameRate(30);
			// 设置音频编码
			m_recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);// aac
			// 设置视频编码
			m_recorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);// h262

			int videosizeindex = StoredData.getInt(
					StoredData.M_VIDEORESOLUTION, 3);
			int videosize[] = VIDEO_VIDEORESOLUTIONARRAY[videosizeindex];
			// 设置录制视频的分辨率
			m_recorder.setVideoSize(videosize[0], videosize[1]);// 352,288

			// 输出文件的路径和名称
			m_recorder.setOutputFile(m_videofile.getAbsolutePath());
			m_recorder.setPreviewDisplay(mTextureView.getHolder().getSurface());
			setOrientationHint();
			// 准备，开始，视频录制
			m_recorder.prepare();
			m_recorder.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			m_isrecord = false;
		}
	}

	/*
	 * (non-Javadoc) 停止录制
	 * 
	 * @see com.wistron.WiCamera.Inter#stopRecord()
	 */
	@Override
	public void stopRecord() {
		try {
			// 判断是否是录制状态
			if (m_isrecord) {
				boolean is3d = StoredData.getBoolean(StoredData.M_CAMERA3D,
						false);
				if (is3d) {
					OperationFile
							.intTodbv(m_context, "video/mp4-3d", videoTitle,
									WiCameraActivity.mCurrentDegree,
									m_videofile.getAbsolutePath(),
									m_videofile.length());
				} else {
					OperationFile
							.intTodbv(m_context, "video/mp4", videoTitle,
									WiCameraActivity.mCurrentDegree,
									m_videofile.getAbsolutePath(),
									m_videofile.length());
				}

				// 通知数据层
				if (WiGalleryOpenGLRenderer.mAsyncFileProvider != null) {
					WiGalleryOpenGLRenderer.mAsyncFileProvider
							.addNewFile(M_VIDEOPATH);
				}

				// 以下停止录制状态，设置状态为false和状态标志为绿色
				m_isrecord = false;
				// 设置m_recorder为空的状态，为下次录制做准备
				m_recorder.reset();
				// 锁定照相头
				cameras.lock();

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		// try {
		// Thread.sleep(1000);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

	/**
	 * 保存连拍的照片
	 * 
	 * @param data
	 *            连拍照片的数据
	 * @param photonum
	 *            第几张连拍的照片
	 * @return 是否保存成功
	 */
	File picture = null;
	String title = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wistron.WiCamera.Inter#storedImage(byte[], int)
	 */
	@Override
	public void storedImage(final byte[] data, final int photonum) {
		saveImage(data, photonum);
	}

	/*
	 * (non-Javadoc) 计算对焦区域
	 * 
	 * @see com.wistron.WiCamera.Inter#setArea()
	 */
	@Override
	public void setArea() {
		if (cameras != null) {
			try {
				if (FOCU_STATE == STATE_FOCUSING) {
					return;
				}
				boolean mLockAeAwbNeeded = (camera_parameters
						.isAutoExposureLockSupported() || camera_parameters
						.isAutoWhiteBalanceLockSupported());
				if (mLockAeAwbNeeded) {
					camera_parameters.setAutoExposureLock(false);
					camera_parameters.setAutoWhiteBalanceLock(false);
				}

				boolean mfocusareasupported = camera_parameters
						.getMaxNumFocusAreas() > 0
						&& isSupported(Parameters.FOCUS_MODE_AUTO,
								camera_parameters.getSupportedFocusModes());
				boolean ismeteringareassupported = camera_parameters
						.getMaxNumMeteringAreas() > 0;
				if (mfocusareasupported) {
					camera_parameters.setFocusAreas(mFocusArea);
				}
				if (ismeteringareassupported) {
					camera_parameters.setMeteringAreas(mMeteringArea);
				}

				// camera_parameters.setSceneMode(camera_parameters.SCENE_MODE_AUTO);
				// camera_parameters
				// .setWhiteBalance(camera_parameters.WHITE_BALANCE_AUTO);
				camera_parameters.setJpegQuality(100);
				camera_parameters
						.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
				cameras.setParameters(camera_parameters);
				FOCU_STATE = STATE_FOCUSING;
				cameras.autoFocus(myAutoFocusCallback);
				System.out.println("自动对焦。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。");
			} catch (Exception e) {
				// TODO: handle exception
				FOCU_STATE = STATE_FAIL;
				// cameras.autoFocus(null);
				cameras.cancelAutoFocus();
				e.printStackTrace();
			}
		}
	}

	public static boolean isSupported(String value, List<String> supported) {
		return supported == null ? false : supported.indexOf(value) >= 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wistron.WiCamera.Inter#updateCameraParametersInitialize()
	 */
	@Override
	public void updateCameraParametersInitialize() {
		try {

			if (cameras != null) {
				// Reset preview frame rate to the maximum because it may be
				// lowered
				// by
				// video camera application.
				List<Integer> frameRates = camera_parameters
						.getSupportedPreviewFrameRates();
				if (frameRates != null) {
					Integer max = Collections.max(frameRates);
					camera_parameters.setPreviewFrameRate(max);
				}

				camera_parameters.setRecordingHint(false);

				// Disable video stabilization. Convenience methods not
				// available in
				// API
				// level <= 14
				String vstabSupported = camera_parameters
						.get("video-stabilization-supported");
				if ("true".equals(vstabSupported)) {
					camera_parameters.set("video-stabilization", "false");
				}
				cameras.setParameters(camera_parameters);
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("出错了");
		}
	}

	/*
	 * (non-Javadoc) 触摸对焦的逻辑
	 * 
	 * @see com.wistron.WiCamera.Inter#onTouchEvent(android.view.MotionEvent)
	 */

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		// boolean isTouchFocus = StoredData.getBoolean(StoredData.M_TOUCHFOCUS,
		// true);
		// if (!isTouchFocus){
		// return false;
		// }

		if (WiCameraActivity.isContinus || (!WiCameraActivity.isCameraOpen)) {
			return false;
		}
		if (WiCameraActivity.isPanoramaMode) {
			return false;
		}
		if (m_cameraIndex == m_camera_front) {
			return false;
		}
		if (FOCU_STATE == STATE_FOCUSING_SNAP_ON_FINISH) {
			return false;
		}
		if (cameras != null && Util.CAMERA_STATE != CAMERA_VIDEO) {

			// TODO Auto-generated method stub
			// if ((FOCU_STATE == STATE_FOCUSING)) {
			// return false;
			// }
			if ((mFocusArea != null)
					&& (FOCU_STATE == STATE_SUCCESS || FOCU_STATE == STATE_FAIL || FOCU_STATE == STATE_FOCUSING)) {
				mFocusArea.clear();
				mMeteringArea.clear();
				mFocusArea = null;
				mMeteringArea = null;
				cameras.cancelAutoFocus();
				handler.removeMessages(CAMERA_FOCUAREA);
				camera_focusImageView.setVisibility(View.GONE);
				FOCU_STATE = STATE_IDLE;
			}
			// FOCU_STATE = STATE_FOCUSING;
			// Util.setRotationParameter(camera_parameters, 0,
			// WiCameraActivity.mOrientation);
			// cameras.setParameters(camera_parameters);
			Matrix matrix = new Matrix();
			Util.prepareMatrix(
					matrix,
					false,
					Util.getDisplayOrientation(
							Util.getDisplayRotation((Activity) m_context), 0),
					previewwidth, previewheight);
			boolean b = matrix.invert(mMatrix);

			camera_parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
			try {
				cameras.setParameters(camera_parameters);
			} catch (Exception e2) {
				// TODO: handle exception
			}

			cameras.cancelAutoFocus();

			if (mFocusArea != null) {
				mFocusArea.clear();
				mMeteringArea.clear();
				mFocusArea = null;
				mMeteringArea = null;
			}

			float x = (float) e.getX();
			float y = (float) e.getY();

			boolean istouchfocus = StoredData.getBoolean(
					StoredData.M_TOUCHFOCUS, true);
			if (istouchfocus) {
				if (e.getAction() == MotionEvent.ACTION_DOWN) {
					stopFaceDetection();
				}
				switch (e.getAction()) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_MOVE:
					System.out.println("x=" + x + "y=" + y);

					picMove(x, y, camera_focusImageView, 200, 200,
							previewwidth, previewheight);
					handler.removeMessages(CAMERA_FOCUAREA);
					handler.sendEmptyMessageDelayed(CAMERA_FOCUAREA, 3000);
					// if ((FOCU_STATE == STATE_FOCUSING)) {
					// cameras.cancelAutoFocus();
					// FOCU_STATE = STATE_IDLE;
					// }
					break;
				case MotionEvent.ACTION_UP:

					// FOCU_STATE = STATE_FOCUSING;
					if (mFocusArea == null) {
						mFocusArea = new ArrayList<Camera.Area>();
						mMeteringArea = new ArrayList<Camera.Area>();

						mFocusArea.add(new Area(new Rect(), 1));
						mMeteringArea.add(new Area(new Rect(), 1));
					}
					calculateTapArea(120, 120, 1, (int) x, (int) y,
							previewwidth, previewheight, mFocusArea.get(0).rect);
					calculateTapArea(120, 120, 1.5, (int) x, (int) y,
							previewwidth, previewheight,
							mMeteringArea.get(0).rect);
					m_isfacedetection = false;
					WiCameraActivity.m_fd_face.setVisibility(View.GONE);

					setArea();
					break;
				default:
					break;
				}
			} else {
				camera_focusImageView.setVisibility(View.GONE);
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc) 计算对焦区域
	 * 
	 * @see com.wistron.WiCamera.Inter#calculateTapArea(int, int, double, int,
	 * int, int, int, android.graphics.Rect)
	 */
	@Override
	public Rect calculateTapArea(int focusWidth, int focusHeight,
			double areaMultiple, int x, int y, int previewWidth,
			int previewHeight, Rect rect) {
		int areaWidth = (int) (focusWidth * areaMultiple);
		int areaHeight = (int) (focusHeight * areaMultiple);
		int left = Util.clamp(x - areaWidth / 2, 0, previewWidth - areaWidth);
		int top = Util.clamp(y - areaHeight / 2, 0, previewHeight - areaHeight);

		RectF rectF = new RectF(left, top, left + areaWidth, top + areaHeight);

		mMatrix.mapRect(rectF);
		Util.rectFToRect(rectF, rect);
		return rect;
	}

	/* 移动图片的方法 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wistron.WiCamera.Inter#picMove(float, float,
	 * android.widget.ImageView, int, int, int, int)
	 */
	@Override
	public void picMove(float x, float y, ImageView imageView, int intWidth,
			int intHeight, int intScreenX, int intScreenY) {
		imageView.setBackgroundResource(R.drawable.camera_focus);
		imageView.setVisibility(View.VISIBLE);
		float mX, mY;
		/* 默认微调图片与指针的相对位置 */
		mX = x - (intWidth / 2);
		mY = y - (intHeight / 2);

		/* 防图片超过屏幕的相关处理 */
		/* 防止屏幕向右超过屏幕 */
		if ((mX + intWidth) > intScreenX) {
			mX = intScreenX - intWidth;
		}
		/* 防止屏幕向左超过屏幕 */
		if (mX < 0) {
			mX = 0;
		}
		/* 防止屏幕向下超过屏幕 */
		if ((mY + intHeight) > intScreenY) {
			mY = intScreenY - intHeight;
		}
		/* 防止屏幕向上超过屏幕 */
		if (mY < 0) {
			mY = 0;
		}
		/* 通过log 来查看图片位置 */
		Log.i("jay", Float.toString(mX) + "," + Float.toString(mY));
		/* 以setLayoutParams方法，重新安排Layout上的位置 */
		// imageView.setLayoutParams(new AbsoluteLayout.LayoutParams(intWidth,
		// intHeight, (int) mX, (int) mY));

		LayoutParams p = (LayoutParams) imageView.getLayoutParams();
		p.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		p.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		p.leftMargin = (int) mX;
		p.topMargin = (int) mY;
		p.width = intWidth;
		p.height = intHeight;
		imageView.setLayoutParams(p);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wistron.WiCamera.Inter#setPreviewCallback(android.hardware.Camera
	 * .PreviewCallback)
	 */
	@Override
	public void setPreviewCallback(PreviewCallback my) {
		Log.e("test++++++++++++++", "setPreviewCallback");
		if (cameras != null) {
			cameras.setPreviewCallback(my);

		}
	}

	/*
	 * (non-Javadoc) 保存图片
	 * 
	 * @see com.wistron.WiCamera.Inter#saveImage(byte[], int)
	 */
	@Override
	public int saveImage(final byte[] data, final int photonum) {
		// data是一个原始的JPEG图像数据，
		// 在这里我们可以存储图片，很显然可以采用MediaStore
		// 注意保存图片后，再次调用startPreview()回到预览
		boolean is3D = StoredData.getBoolean(StoredData.M_CAMERA3D, false);
		if (is3D) {
			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			String dateString = formatter.format(date);
			// File picture = null;
			// String title = null;
			int storepath = StoredData.getInt(StoredData.M_STORAGEMODE, 0);
			if (photonum == -1) {
				title = "photo-" + dateString;
				picture = new File(CAMERA_PAHTARRAY[storepath] + "photo-"
						+ dateString + ".jps");
			} else {
				title = dateString + "_" + (photonum + 1);
				picture = new File(CAMERA_PAHTARRAY[storepath] + dateString
						+ "_" + (photonum + 1) + ".jps");
				continous_array.add(picture.getAbsolutePath());
				System.out.println("path=" + picture.getAbsolutePath());

			}
			if (!picture.getParentFile().exists()) {
				picture.getParentFile().mkdirs();
			}
			try {
				DDD ddd = new DDD();
				Options o = new Options();
				o.inJustDecodeBounds = false;
				o.inPreferredConfig = Config.RGB_565;
				// o.inSampleSize=2;
				Bitmap bit = BitmapFactory.decodeByteArray(data, 0,
						data.length, o);
				// Bitmap[] bits = ddd.genrenteTriDefSplitBitmap(OperateBitmaps
				// .fitScreenSize(bit, WiCameraActivity.screenWidth,
				// WiCameraActivity.screenHeight), true);
				Bitmap[] bits = ddd.genrenteTriDefSplitBitmap(bit, true);
				if (bit != null) {
					bit.recycle();
				}
				Bitmap b = Bitmap.createBitmap(bits[0].getWidth() * 2,
						bits[0].getHeight(), Config.RGB_565);
				Canvas c = new Canvas(b);
				c.drawBitmap(bits[0], 0, 0, null);
				c.drawBitmap(bits[1], bits[0].getWidth(), 0, null);
				bits[0].recycle();
				bits[1].recycle();

				CameraView.saveBitmap(b, picture.getAbsolutePath());
				b.recycle();
				OperationFile.intTodb(m_context, "image/jpeg", title,
						WiCameraActivity.mCurrentDegree,
						picture.getAbsolutePath(), data.length);

				// 添加iso信息
				// int isoId = StoredData.getInt(StoredData.M_ISO, 1);
				// String isoValue = CAMERA_ISO_ARRAY[isoId];
				// OperationFile.addImageISO(picture.getAbsolutePath(),
				// isoValue);
				// 添加gps信息
				boolean isaddgps = StoredData.getBoolean(StoredData.M_ADDTAG,
						true);
				if (isaddgps) {

					// if (GetGPSInfo.isGPSOpen()) {
					if (m_getGpsInfo == null) {
						m_getGpsInfo = new GetGPSInfo(m_context);
						m_getGpsInfo.getGps();
					}
					m_location = m_getGpsInfo.m_locations;
					System.out.println("location============================"
							+ m_location);
					OperationFile.addImageGps(m_location,
							picture.getAbsolutePath());
				}
				// }

				// 所有相片的路径
				// if (PIC_FILE_LIST != null) {
				// synchronized (PIC_FILE_LIST) {
				PIC_FILE_LIST.add(picture.getAbsolutePath());
				// }

				// 通知数据层
				if (WiGalleryOpenGLRenderer.mAsyncFileProvider != null) {
					WiGalleryOpenGLRenderer.mAsyncFileProvider
							.addNewFile(picture.getAbsolutePath());
				}

				System.out.println("pic array size=========="
						+ PIC_FILE_LIST.size());
				// }

				int numindex = StoredData.getInt(StoredData.M_CONTINUESHOTNUM,
						0);
				int maxnum = CAMERA_CONTINUESHOTNUMARRAY[numindex];
				if (((photonum + 1) == maxnum) || photonum == -1) {
					CAMERA_CONTINUOUS_LASTPICPATH = picture.getPath();
					StoredData.saveString(StoredData.M_REVIEWFILEPATH,
							CAMERA_CONTINUOUS_LASTPICPATH);
				}
				return photonum;
			} catch (Exception e) {
				// TODO: handle exception
				return -2;
			}
		} else {

			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			String dateString = formatter.format(date);
			File picture = null;
			String title = null;
			int storepath = StoredData.getInt(StoredData.M_STORAGEMODE, 0);
			if (photonum == -1) {
				title = "photo-" + dateString;
				// boolean is3D = StoredData.getBoolean(StoredData.M_CAMERA3D,
				// false);
				// String tag = ".jpg";
				// if (is3D) {
				// tag = ".jpg";
				// } else {
				// tag = ".jpg";
				// }
				picture = new File(CAMERA_PAHTARRAY[storepath] + "photo-"
						+ dateString + ".jpg");
			} else {
				title = dateString + "_" + (photonum + 1);
				picture = new File(CAMERA_PAHTARRAY[storepath] + dateString
						+ "_" + (photonum + 1) + ".jpg");
				continous_array.add(picture.getAbsolutePath());
				System.out.println("path=" + picture.getAbsolutePath());

			}
			if (!picture.getParentFile().exists()) {
				picture.getParentFile().mkdirs();
			}
			try {
				// 获得文件输出流
				FileOutputStream fos = new FileOutputStream(picture);
				fos.write(data);
				// 关闭文件流
				fos.flush();
				fos.close();
				OperationFile.intTodb(m_context, "image/jpeg", title,
						WiCameraActivity.mCurrentDegree,
						picture.getAbsolutePath(), data.length);
				// 添加iso信息
				// int isoId = StoredData.getInt(StoredData.M_ISO, 1);
				// String isoValue = CAMERA_ISO_ARRAY[isoId];
				// OperationFile.addImageISO(picture.getAbsolutePath(),
				// isoValue);
				// 添加gps信息
				boolean isaddgps = StoredData.getBoolean(StoredData.M_ADDTAG,
						true);
				if (isaddgps) {

					// if (GetGPSInfo.isGPSOpen()) {
					if (m_getGpsInfo == null) {
						m_getGpsInfo = new GetGPSInfo(m_context);
						m_getGpsInfo.getGps();
					}
					m_location = m_getGpsInfo.m_locations;
					System.out.println("location============================"
							+ m_location);
					OperationFile.addImageGps(m_location,
							picture.getAbsolutePath());
				}
				// }

				// 所有相片的路径
				// if (PIC_FILE_LIST != null) {
				// synchronized (PIC_FILE_LIST) {
				PIC_FILE_LIST.add(picture.getAbsolutePath());
				// }

				// 通知数据层
				if (WiGalleryOpenGLRenderer.mAsyncFileProvider != null) {
					WiGalleryOpenGLRenderer.mAsyncFileProvider
							.addNewFile(picture.getAbsolutePath());
				}

				System.out.println("pic array size=========="
						+ PIC_FILE_LIST.size());
				// }

				int numindex = StoredData.getInt(StoredData.M_CONTINUESHOTNUM,
						0);
				int maxnum = CAMERA_CONTINUESHOTNUMARRAY[numindex];
				if (((photonum + 1) == maxnum) || photonum == -1) {
					CAMERA_CONTINUOUS_LASTPICPATH = picture.getPath();
					StoredData.saveString(StoredData.M_REVIEWFILEPATH,
							CAMERA_CONTINUOUS_LASTPICPATH);
				}
				// Thread.sleep(300);
				return photonum;
			} catch (Exception e) {
				// e.printStackTrace();
				return -2;
			}
		}

	}

	// 异步保存图片
	class MyAsyncTask extends AsyncTask<Void, Void, Void> {
		byte[] data;
		int photonum;

		public MyAsyncTask(byte[] data, int photonum) {
			// TODO Auto-generated constructor stub
			this.data = data;
			this.photonum = photonum;
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			saveImage(data, photonum);
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			int photonum = StoredData.getInt(StoredData.M_CONTINUESHOTNUM, 3);
			if (Util.CAMERA_STATE == InterSurfaceView.CAMERA_CONTINUOUSANDSELFTIME) {

			}
			super.onCancelled();
		}
	}
}
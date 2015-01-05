package com.wistron.WiCamera;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import Utilities.CSStaticData;
import Utilities.FileTypeHelper;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.wistron.StereoUI.FileInfoMsgBox;
import com.wistron.StereoUI.MsgBox;
import com.wistron.StereoUI.ShareMenu;
import com.wistron.WiCamera.SlideButton.OnCheckedChangedListener;
import com.wistron.WiCamera.WiPanorama.Mosaic;
import com.wistron.WiCamera.WiPanorama.MosaicFrameProcessor;
import com.wistron.WiCamera.WiPanorama.OrientationSensor;
import com.wistron.WiCamera.WiPanorama.PanoUtil;
import com.wistron.WiCamera.WiPanorama.PanoramaProgressIndicator;
import com.wistron.WiCamera.WiPanorama.Storage;
import com.wistron.WiViewer.ImageInfo;
import com.wistron.WiViewer.ImageInfoBean;
import com.wistron.WiViewer.MediaFilePicker;
import com.wistron.WiViewer.TDStaticData;
import com.wistron.WiViewer.VideoInfo;
import com.wistron.WiViewer.VideoProvider;
import com.wistron.WiViewer.WiImageView;
import com.wistron.WiViewer.WiImageViewerActivity;
import com.wistron.WiViewer.WiVideoViewerActivity;
import com.wistron.swpc.wicamera3dii.R;

/**
 * 
 * @Copyright (c) 2011 Wistron SWPC All rights reserved.
 * 
 * @created: 2012/02/21
 * @filename: VideoSurfaceView.java
 * @author WH1107063(周海江)
 * @purpose 对控件的初始化和注册事件以及执行动画的类
 * 
 * 
 * 
 * 
 */
public class WiCameraActivity extends Activity implements OnGestureListener,
		SensorEventListener, PreviewCallback {

	private RelativeLayout mMenuLayout = null;
	public RelativeLayout mSubMenuLayout = null;
	private ListView mListView = null;
	public static ListView mSubListView = null;
	private ArrayList<CharSequence> mListData = null;
	private MainListViewAdapter mMainListViewAdapter;
	private TextView mSubMenuTitle = null;
	private SubListViewAdapter mSubMenuAdapter = null;
	private SeekBar mExposureSeekBar = null;
	private ImageView[] mExposureIV = null;
	public static int LISTPOSITION = -1;
	public static boolean mIsCameraList = true;
	public static boolean mIsRecoderList = false;
	private XmlPullParser mTextColorWhite = null;
	private XmlPullParser mTextColorGreen = null;
	public static ColorStateList COLORSTATELIST_WHITE = null;
	public static ColorStateList COLORSTATELIST_GREEN = null;
	private ColorStateList mCurrentColorList = null;
	OrientationSensor mOrientationSensor = null;
	public HashMap<Integer, Integer> mSubMenuStateMap;
	private static final String TAG = "Wicamer3DMenu";
	private String m_currentpano_filepath = "";
	private int mSettingMenuWidth = 400, mSettingMenuHeight = 460,
			mSubMenuWidth = 150,
			mSubMenuHeight = ViewGroup.LayoutParams.WRAP_CONTENT,
			mMsgBoxWidth = 667, mMsgBoxheight = 305;
	/**
	 * 各个子菜单的起始id
	 */
	public static final int CAMERA_SIMESHOT_START_ID = 10000;
	public static final int CAMERA_FACETRACKING_START_ID = 20000;
	public static final int CAMERA_CONTINUESHOT_START_ID = 30000;
	public static final int CAMERA_SHUTTERSOUND_START_ID = 40000;
	public static final int CAMERA_GRIDDISPLAY_START_ID = 50000;
	// public static final int CAMERA_REDEYEREMOVER_START_ID = 60000;
	public static final int CAMERA_GSENSOR_START_ID = 70000;
	public static final int CAMERA_ADDLCATION_START_ID = 80000;
	public static final int CAMERA_REVIEW_START_ID = 90000;
	public static final int CAMERA_SCENESWITCH_START_ID = 100000;
	public static final int CAMERA_SELFTIMER_START_ID = 110000;
	public static final int CAMERA_EXPOSURE_START_ID = 120000;
	public static final int CAMERA_PICTURESIZE_START_ID = 130000;
	public static final int CAMERA_WHITEBALANCE_START_ID = 140000;
	public static final int CAMERA_TOUCHFOCUS_START_ID = 150000;
	public static final int CAMERA_HANDJITTER_START_ID = 160000;
	public static final int CAMERA_SAVETO_START_ID = 170000;
	public static final int CAMERA_RESTOREDEFAULT_START_ID = 180000;
	public static final int CAMERA_VIDEORESOLUTION_START_ID = 60000;

	// 对话框
	private MsgBox mMsgBox;
	// 对话框
	private MsgBox mMsgBox_sdcard;
	// 主菜单的项
	public static int[] m_camera_mainmenu_setting_resid = new int[] {
			R.string.camera_setting_smileShot,
			R.string.camera_setting_faceTracking,
			R.string.camera_setting_continueShot,
			R.string.camera_setting_shutterSound,
			R.string.camera_setting_gridDisplayInViewfinder,
			// R.string.camera_setting_redEyeRemoval,
			R.string.camera_setting_gSensorForJPEGOrientationFlag,
			R.string.camera_setting_addLocationTag,
			R.string.camera_setting_review,
			R.string.camera_setting_sceneSwitch,
			R.string.camera_setting_selfTimer,
			R.string.camera_setting_exposure,
			R.string.camera_setting_pictureSize,
			R.string.camera_setting_whiteBalance,
			R.string.camera_setting_touchFocus,
			R.string.camera_setting_handJitterReduction,
			R.string.camera_setting_saveTo,
			R.string.camera_setting_restoreDefault,
			R.string.camera_setting_test };
	public static int[] m_video_mainmenu_setting_resid = new int[] {
			R.string.camera_setting_shutterSound,
			R.string.camera_setting_selfTimer, R.string.camera_setting_review,
			R.string.camera_setting_sceneSwitch,
			R.string.camera_setting_exposure,
			R.string.camera_setting_videoresolution,
			R.string.camera_setting_whiteBalance,
			R.string.camera_setting_handJitterReduction,
			R.string.camera_setting_saveTo,
			R.string.camera_setting_restoreDefault };
	/**
	 * 各个子菜单的项的对应的资源
	 */
	public static int[][] m_camera_submenu_smileShot_resid = new int[][] {
			{ 0, R.string.camera_setting_on },
			{ 0, R.string.camera_setting_off } };

	public static int[][] m_camera_submenu_faceTracking_resid = new int[][] {
			{ 0, R.string.camera_setting_on },
			{ 0, R.string.camera_setting_off } };

	public static int[][] m_camera_submenu_continueShot_resid = new int[][] {
			{ 0, R.string.camera_setting_3Shots },
			{ 0, R.string.camera_setting_5Shots },
			{ 0, R.string.camera_setting_7Shots } };

	public static int[][] m_camera_submenu_gridDisplay_resid = new int[][] {
			{ 0, R.string.camera_setting_on },
			{ 0, R.string.camera_setting_off } };

	// public static int[][] m_camera_submenu_redEyeRemoval_resid = new int[][]
	// {
	// { 0, R.string.camera_setting_on },
	// { 0, R.string.camera_setting_off } };

	public static int[][] m_camera_submenu_gSensor_resid = new int[][] {
			{ 0, R.string.camera_setting_on },
			{ 0, R.string.camera_setting_off } };

	public static int[][] m_camera_submenu_addLocationTag_resid = new int[][] {
			{ 0, R.string.camera_setting_on },
			{ 0, R.string.camera_setting_off } };

	public static int[][] m_camera_submenu_review_resid = new int[][] {
			{ 0, R.string.camera_setting_on },
			{ 0, R.string.camera_setting_off } };

	public static int[][] m_camera_submenu_sceneSwitch_resid = new int[][] {
			{ R.drawable.auto_icon, R.string.camera_setting_auto },
			{ R.drawable.action, R.string.camera_setting_action },
			{ R.drawable.night, R.string.camera_setting_night },
			{ R.drawable.treat_icon, R.string.camera_setting_theatre },
			{ R.drawable.beach_icon, R.string.camera_setting_beach },
			{ R.drawable.snow_icon, R.string.camera_setting_snow },
			{ R.drawable.sunset_icon, R.string.camera_setting_sunset },
			{ R.drawable.fireworks_icon, R.string.camera_setting_fireworks } };

	public static int[][] m_camera_submenu_selfTimer_resid = new int[][] {
			{ R.drawable.self_timer_off, R.string.camera_setting_off },
			{ R.drawable.self_timer_3s, R.string.camera_setting_3s },
			{ R.drawable.self_timer_5s, R.string.camera_setting_5s },
			{ R.drawable.self_timer_10s, R.string.camera_setting_10s } };

	public static int[][] m_camera_submenu_exposure_resid = new int[][] {
			{ 0, R.string.camera_setting_minus4 },
			{ 0, R.string.camera_setting_minus3 },
			{ 0, R.string.camera_setting_minus2 },
			{ 0, R.string.camera_setting_minus1 },
			{ 0, R.string.camera_setting_0 }, { 0, R.string.camera_setting_1 },
			{ 0, R.string.camera_setting_2 }, { 0, R.string.camera_setting_3 },
			{ 0, R.string.camera_setting_4 } };

	public static int[][] m_camera_submenu_pictureSize_resid = new int[][] {
			{ 0, R.string.camera_setting_5M },
			{ 0, R.string.camera_setting_3M },
			{ 0, R.string.camera_setting_2M },
			{ 0, R.string.camera_setting_1M },
			{ 0, R.string.camera_setting_VGA } };

	public static int[][] m_camera_submenu_whiteBalance_resid = new int[][] {
			{ R.drawable.white_balance_auto, R.string.camera_setting_auto },
			{ R.drawable.cloud, R.string.camera_setting_cloudy },
			{ R.drawable.incandescent, R.string.camera_setting_incandescent },
			{ R.drawable.daylight, R.string.camera_setting_daylight },
			{ R.drawable.fluorescent, R.string.camera_setting_fluorescent } };

	public static int[][] m_camera_submenu_ISO_resid = new int[][] {
			{ 0, R.string.camera_setting_iso_auto },
			{ 0, R.string.camera_setting_iso_100 },
			{ 0, R.string.camera_setting_iso_200 },
			{ 0, R.string.camera_setting_iso_400 } };

	public static int[][] m_camera_submenu_handJitterReduction_resid = new int[][] {
			{ 0, R.string.camera_setting_on },
			{ 0, R.string.camera_setting_off } };

	private int[][] m_camera_submenu_saveTo_resid = new int[][] {
			{ 0, R.string.camera_setting_internalStorage },
			{ 0, R.string.camera_setting_SDCard } };
	public static int[][] m_camera_submenu_restoreDefault_resid = new int[][] { {
			0, R.string.camera_setting_restoreDefault } };

	public static int[][] m_video_submenu_videoresolution = new int[][] {
			{ 0, R.string.camera_setting_videoresolution_1080p },
			{ 0, R.string.camera_setting_videoresolution_720p },
			{ 0, R.string.camera_setting_videoresolution_WVGA },
			{ 0, R.string.camera_setting_videoresolution_VGA },
			{ 0, R.string.camera_setting_videoresolution_CIF }

	};

	/** Called when the activity is first created. */
	public static float rate1 = ((float) 720) / ((float) 1196);
	public static float rate2 = ((float) 1196) / ((float) 720);
	/**
	 * surface ui
	 */

	RelativeLayout m_rl_camera_main_layout;
	// 含有surfaceview的绝对布局的ui
	public static RelativeLayout m_al_camera_surfaceui;
	public static RelativeLayout m_al_surfaceview;
	// 触摸对焦的imageview
	public static ImageView m_iv_camera_focus;
	// 人脸识别的view
	public static FaceIndicater m_fd_face;
	// 录像时计时的控件
	public Chronometer m_ch_camera_time_count;
	// 拍照计时控件
	public static Chronometer m_ch_camera_time_count1;
	public static boolean isok = true;
	// 屏幕宽高单位pix
	public static int screenWidth;
	public static int screenHeight;
	public static int screenWidth_Review;
	public static int screenHeight_Review;
	public static float mDensity = 0;
	// grid line
	public AuxiliaryLine m_aux_camera_auxiliaryline;
	/**
	 * 照相时的悬浮控件
	 * 
	 * 
	 */
	// 照相和录像时的悬浮绝对布局
	public static RelativeLayout m_al_camera_overlayui;
	/**
	 * 照相和录像时左边的悬浮ui
	 */
	// 设置
	public static ToggleButton m_btn_camera_setting;
	// 前后切换
	public ToggleButton m_btn_camera_used_camera;
	// 闪光灯
	public ToggleButton m_btn_camera_flashmode;
	// 录像，照相的文件的缩略图
	public static ImageView m_iv_camera_newpic_thumbnails;
	/**
	 * 照相和录像时中间的悬浮ui
	 * 
	 */

	/**
	 * 照相时中间的悬浮ui
	 */
	// 剩余电量
	public Button m_btn_camera_sparepower;
	public Button m_btn_panorama_sparepower;
	// 储存方式
	public Button m_btn_camera_storagemode;
	// 风景模式
	public Button m_btn_camera_scence;
	// 当前照片的大小和可以找到总照片的个数 为2/20样式
	public static Button m_btn_camera_newpic_sizeofsum;
	public static Button m_btn_camera_self_timer;
	/**
	 * 录像时中间的悬浮ui
	 */

	/**
	 * 照相和录像时的右边的悬浮ui
	 */
	// 调整焦距
	public ArcSeekBar m_skb_camera_zoom_size;
	// 右边的背景
	public Button m_btn_camera_uiright_bg;
	// 2D和3D间的切换
	public SlideButton m_btn_camera_dimension;
	// 照相按钮
	public ToggleButton m_btn_camera_capture;
	// 录像和照相的切换按钮
	public static SlideButton m_btn_camera_captureorrecord;
	public ImageView m_camera_slide_bg_camera_icon;
	public ImageView m_camera_slide_bg_record_icon;
	public ImageView m_camera_slide_bg_2d_icon;
	public ImageView m_camera_slide_bg_3d_icon;
	// 全景拍摄按钮
	public ToggleButton m_btn_camera_panoramic;
	// 连拍按钮
	public static ToggleButton m_btn_camera_continuous;
	/**
	 * 预览时的控件
	 */
	// review 时的绝对布局
	public static RelativeLayout m_al_camera_reviewui;
	public static RelativeLayout m_al_camera_reviewui_wiiamgeview;
	public static WiImageView m_imageview;
	// review时的缩略图
	public static ImageView m_iv_camera_review_thumbnails;
	// 删除
	public ToggleButton m_btn_camera_review_delete;
	// 分享
	public ToggleButton m_btn_camera_review_share;
	// 当前文件的信息
	public ToggleButton m_btn_camera_review_information;
	// 返回到camera
	public Button m_btn_camera_review_tocamera;
	// 播放video
	public Button m_btn_camera_review_play_video;
	RelativeLayout btn_camera_review_play_videoparent;
	// 照相，录像时ui的数组
	public static View[] m_camera_overlayui_array;
	// review时的ui的数组
	public static View[] m_camera_reviewui_array;
	// 声明监测屏幕旋转角度的对象
	public static MyOrientationEventListener mOrientationEventListener;
	public static int mOrientation;
	public static int mOrientationCompensation;
	public static int mCurrentDegree = OrientationEventListener.ORIENTATION_UNKNOWN;
	Animation rAnimation;
	private int mPreOrientation = 270;
	private int start;
	int a;

	// 声明videosurfaceview对象
	public InterSurfaceView videoSurfaceView;
	// 声明监测电量的类的对象
	private BatteryReceiver batteryReceiver;
	// 监听外置sdcard是否被拔出
	// public SDCardBroadcastReceiver msdCardBroadcastReceiver;
	// 声明计时对象
	private TimeCounter timeCounter;
	// 摄像头的状态
	int camera_state = 0;
	int camera_captue = 0;
	int camera_record = 1;
	int camera_continus = 2;
	int camera_panoramic = 3;
	// 是否正在录制视频
	boolean m_isRecord = false;
	// 是否是横屏
	public static boolean M_ISLANSCAPE = false;
	// 是否正在预览
	public boolean m_isReview = false;
	public MsgBox mMsgBox_delete;
	public MsgBox mMsgBox_Panoramacancel;
	// 分享菜单
	public static ShareMenu mShareMenu;
	// handle
	public static Handler m_main_handle;
	// 显示详细信息
	private FileInfoMsgBox camera_mMsgBox_info;

	public MediaFilePicker m_bitmappicker;
	public static boolean isCanSwitch = true;

	// 关于imageview的动画
	private Handler m_handler;
	private Runnable m_runnable;// 移入移出
	private Runnable m_runnable1;// 移動還原
	private Runnable mrunnable2;// 淡入淡出
	float m_moveX = 0;
	public int m_isNextBitmapLoaded = 0;// 标记下一张图片是够加载完成
	public Bitmap[][] m_bitmaps = new Bitmap[2][2];// 用于存放读取出来的上下一张图片
	private boolean m_stopdispatch = false;// 判断是否分发触摸事件
	float m_FirstTouchX = 0;// 标记触摸的起始位置
	private boolean m_isNext = false;// 用于标记是否为下一张
	private boolean m_isSolidPlay = false;// 进行其他操作时当前是否在幻灯片播放
	private boolean m_moveIsOver = false;// 标记当前是否还在移动
	private int m_position;// 用于左右移动的位置
	private boolean m_isMove = false;// 记录动画是否完成
	private int m_alpha;// 用于淡入淡出的透明度
	private String Tag = "WiCameraActivity";
	private boolean m_downisonthisview = false;// 判斷down事件是否在當前view上
	private GestureDetector m_gesturedetector;
	ArrayList<String> picList;
	ArrayList<String> videoList;
	public static int sbarh = 0; // 状态栏高度
	// public static boolean isToStopContinus = false;
	public static boolean isRecording = false;
	// 全景变量
	public RelativeLayout m_panorama_relativelayout;
	public static boolean isPanoramaMode = false;
	// Camera mCameraDevice;
	public static final int DEFAULT_SWEEP_ANGLE = 160;
	public static final int DEFAULT_BLEND_MODE = Mosaic.BLENDTYPE_HORIZONTAL;
	public static final int DEFAULT_CAPTURE_PIXELS = 960 * 720;
	// public AlertDialog mAlertDialog;
	private static final int MSG_LOW_RES_FINAL_MOSAIC_READY = 1;
	private static final int MSG_RESET_TO_PREVIEW_WITH_THUMBNAIL = 2;
	private static final int MSG_GENERATE_FINAL_MOSAIC_ERROR = 3;
	private static final int MSG_RESET_TO_PREVIEW = 4;
	private static final int MSG_TURNTO_REVIEW = 5;
	private static final int MSG_TURNTO_CAMERA = 6;
	private static final int MSG_UPDATE_THUMBNALL = 7;
	private boolean mThreadRunning;
	private static final int CAPTURE_STATE_VIEWFINDER = 0;
	private static final int CAPTURE_STATE_MOSAIC = 1;
	public static Handler mMainHandler;
	boolean flag_reportprogress = false;
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
	// SurfaceView surfaceView;
	// SurfaceHolder surfaceHolder;
	ImageView mImageView;
	private float mHorizontalViewAngle;
	private float[] mTransformMatrix;
	private MosaicFrameProcessor mMosaicFrameProcessor;
	private Button m_button;
	private boolean m_is3D = false;
	PanoramaProgressIndicator mPanoramaProgressIndicator;
	private RelativeLayout mPanoProgressLayout;
	private ImageView mPanoProgressLeft;
	private ImageView mPanoProgressRight;
	private static final String VIDEO_RECORD_SOUND = "mnt/sdcard/di.ogg";
	// private static final String VIDEO_RECORD_SOUND =
	// "/system/media/audio/ui/VideoRecord.ogg";
	private SoundPlayer mRecordSound;
	private SensorManager sensorManager;
	private int m_sensor_pretime = 0;
	private float[] gravity = new float[3];
	private boolean mCancelComputation;
	public static boolean isContinus = false;
	private Object mWaitObject = new Object();
	public boolean mIsBlendingThumbImage = false;
	// public static boolean isClickable=true;
	public int ldegree = 0;

	public ArrayList<String> VIDEO_FILE_LIST;
	public ArrayList<String> PIC_FILE_LIST;
	public static boolean isRear = true;
	public static boolean isCameraOpen = false;
	public static RelativeLayout newpic_thumbnails_parent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("回来了。。。。。。。。。。。。。。。" + "onCreate");
		VIDEO_FILE_LIST = new ArrayList<String>();
		PIC_FILE_LIST = new ArrayList<String>();
		setFullScreenAndNoTitle();
		// 屏幕旋转的类
		ldegree = mCurrentDegree;
		TDStaticData.SCREEN_WIDTH = getWindowManager().getDefaultDisplay()
				.getWidth();
		TDStaticData.SCREEN_HEIGHT = getWindowManager().getDefaultDisplay()
				.getHeight();

		TDStaticData.SCREEN_WIDTH_ORG = TDStaticData.SCREEN_WIDTH;
		TDStaticData.SCREEN_HEIGHT_ORG = TDStaticData.SCREEN_HEIGHT;

		// 获取屏幕密度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		mDensity = dm.density;

		setContentView(R.layout.wicameraactivity_main);
		// enterLightsOutMode(getWindow());
		// 初始化监测屏幕旋转的对象，并启用
		StoredData.init(this);
		initControl();
		newpic_thumbnails_parent = (RelativeLayout) findViewById(R.id.newpic_thumbnails_parent);
		initCameraListView();
		initCameraListViewListener();
		initMsgBox();
		// add3DView(true);
		m_gesturedetector = new GestureDetector(this);
		mOrientationEventListener = new MyOrientationEventListener(this);
		mOrientationEventListener.enable();
		// m_ch_camera_time_count1 = (Chronometer)
		// findViewById(R.id.ch_camera_time_count1);
		setMovement();
		initPanoramaUI();
		mOrientationSensor = new OrientationSensor(WiCameraActivity.this);

		boolean is3D = StoredData.getBoolean(StoredData.M_CAMERA3D, false);
		// boolean is3D = CSStaticData.g_is_3D_mode;
		// m_btn_camera_dimension.setCheckedWithCallback(!is3D);
		if (is3D) {
			// m_btn_camera_dimension.setChecked(false);
			m_btn_camera_used_camera.setAlpha(0);
			m_btn_camera_used_camera.setEnabled(false);
		} else {
			// m_btn_camera_dimension.setChecked(true);
			m_btn_camera_used_camera.setAlpha(1);
			m_btn_camera_used_camera.setEnabled(true);
		}
	}

	// 设置全屏
	public static void enterLightsOutMode(Window window) {
		WindowManager.LayoutParams params = window.getAttributes();
		params.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE;
		window.setAttributes(params);
	}

	/**
	 * 设置全屏
	 */
	public void fitScreenSize() {
		Class<?> cint = null; // 类的实例
		Object obj = null; // 类的对象
		Field field = null; // 状态栏高度的变量
		int objid = 0; // 资源标识符

		try {
			if (android.os.Build.VERSION.SDK_INT >= 11) {
				// 仅对有状态栏的版本使用此方法
				cint = Class.forName("com.android.internal.R$dimen");
				obj = cint.newInstance();
				field = cint.getField("status_bar_height");
				objid = Integer.parseInt(field.get(obj).toString());
				sbarh = this.getResources().getDimensionPixelSize(objid);
				// sbarh = 53;
			} else {
				sbarh = 0;
			}
			System.out.println("[View]状态栏高度 = " + sbarh);
		} catch (Exception e1) {
			System.out.println("[View]获取状态栏高度失败");
			e1.printStackTrace();
		}
		android.widget.FrameLayout.LayoutParams lps = (android.widget.FrameLayout.LayoutParams) m_rl_camera_main_layout
				.getLayoutParams();
		lps.width = screenWidth + sbarh;
		lps.height = screenHeight;
		m_rl_camera_main_layout.setLayoutParams(lps);
		enterLightsOutMode(getWindow());
		initializeScreenBrightness(getWindow(), getContentResolver());
	}

	public static void initializeScreenBrightness(Window win,
			ContentResolver resolver) {
		// Overright the brightness settings if it is automatic
		int mode = Settings.System.getInt(resolver,
				Settings.System.SCREEN_BRIGHTNESS_MODE,
				Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
		if (mode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
			WindowManager.LayoutParams winParams = win.getAttributes();
			winParams.screenBrightness = 0.7f;
			win.setAttributes(winParams);
		}
	}

	/*
	 * 初始化messbox
	 */
	public void initMsgBox() {

		mShareMenu = new ShareMenu(WiCameraActivity.this, screenWidth - 400,
				100);
		mShareMenu.setAnchor(ShareMenu.ANCHOR_TOP_RIGHT);
		ArrayList<String> filepath = new ArrayList<String>();
		String pathString = "file:/mnt/sdcardwinter.jpg";
		filepath.add(pathString);
		mShareMenu.setImagePath(filepath);
		mShareMenu.addToLayout(m_rl_camera_main_layout);
		mShareMenu.hide();

		m_btn_camera_flashmode
				.setBackgroundResource(R.drawable.camera_flash_btn_autotoon_selecter);
		// 弹出自己定义的mMsgBox
		mMsgBox = new MsgBox(this);
		mMsgBox.setBackgound(R.drawable.main_menu_window_portrait);
		mMsgBox.setMessage(getResources().getText(
				R.string.camera_restoreDefault));
		mMsgBox.setPositiveButton(
				getResources().getText(R.string.gallery_delete_confirm),
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (Util.CAMERA_STATE == videoSurfaceView.CAMERA_VIDEO) {
							videoSurfaceView.restoreDefault();
							videolistViewToDefault();
							System.out.println("video 恢复默认");
						} else {

							videoSurfaceView.restoreDefault();
							cameralistViewToDefault();
							System.out.println("camera 恢复默认");
						}
						mMsgBox.hide();
						mMenuLayout.setVisibility(View.GONE);
						m_btn_camera_setting.setChecked(false);
						LISTPOSITION = -1;
						mMainListViewAdapter.notifyDataSetChanged();
					}
				});
		mMsgBox.setNegativeButton(
				getResources().getText(R.string.gallery_delete_cancel),
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mMsgBox.hide();
						LISTPOSITION = -1;
						mMainListViewAdapter.notifyDataSetChanged();
					}
				});
		// mMsgBox_sdcard = new MsgBox(WiCameraActivity.this);
		mMsgBox.addToLayout(m_rl_camera_main_layout);

		// 定义全景的取消弹出框
		mMsgBox_Panoramacancel = new MsgBox(this);
		mMsgBox_Panoramacancel
				.setBackgound(R.drawable.main_menu_window_portrait);
		mMsgBox_Panoramacancel.setMessage(getResources().getText(
				R.string.camera_panorama_cancel));
		mMsgBox_Panoramacancel.setPositiveButton(
				getResources().getText(R.string.gallery_delete_confirm),
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						m_btn_camera_capture
								.setBackgroundResource(R.drawable.capture_button);
						mMsgBox_Panoramacancel.hide();
						panoramaToCamera();
						try {
							cancelHighResComputation();
						} catch (Exception e) {
							// TODO: handle exception
							Log.e("mAlertDialog click ok ",
									"failed in process resetToPreview");
						}
					}
				});
		mMsgBox_Panoramacancel.setNegativeButton(
				getResources().getText(R.string.gallery_delete_cancel),
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mMsgBox_Panoramacancel.hide();
					}
				});
		// mMsgBox_sdcard = new MsgBox(WiCameraActivity.this);
		mMsgBox_Panoramacancel.addToLayout(m_rl_camera_main_layout);

		// 弹出自己定义的mMsgBox
		mMsgBox_delete = new MsgBox(this);
		mMsgBox_delete.setBackgound(R.drawable.main_menu_window_portrait);
		mMsgBox_delete.setModelStatus(false);
		mMsgBox_delete.setMessage(getResources().getText(
				R.string.gallery_delete_text));
		mMsgBox_delete.setPositiveButton(
				getResources().getText(R.string.gallery_delete_confirm),
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						m_btn_camera_review_tocamera
								.setVisibility(View.VISIBLE);
						m_btn_camera_review_tocamera.setAlpha(1);
						m_btn_camera_review_tocamera.setClickable(true);

						// TODO Auto-generated method stub
						// if (VideoSurfaceView.CAMERA_STATE ==
						// VideoSurfaceView.CAMERA_VIDEO) {
						OperationFile.deleteFile(WiCameraActivity.this,
								m_bitmappicker.getCurrentFileName());
						if (Util.CAMERA_STATE == InterSurfaceView.CAMERA_VIDEO) {
							videoSurfaceView.getVideoFileList().remove(
									m_bitmappicker.getCurrentFileName());
						} else {
							videoSurfaceView.getPicFileList().remove(
									m_bitmappicker.getCurrentFileName());
						}

						boolean nextorpre = true;
						if (m_bitmappicker.getCurrentFileIndex() == m_bitmappicker
								.getFileCounts() - 1) {
							nextorpre = false;
						}
						// 从链表中把改图片删除,对话框隐藏
						m_bitmappicker.deleteFile();

						mMsgBox_delete.hide();
						m_btn_camera_review_delete.setChecked(false);
						// 删除完了
						if (m_bitmappicker.getCurrentFileIndex() == -1) {
							StoredData.saveString(StoredData.M_REVIEWFILEPATH,
									"a");
							// mMsgBox_delete.hide();
							// 回到照相界面

							m_isReview = false;
							isShowReview(false);
							m_btn_camera_review_delete.setChecked(false);

							Bitmap bit = null;
							// StoredData.saveString(StoredData.M_REVIEWFILEPATH,
							// "a");
							m_iv_camera_newpic_thumbnails.setImageBitmap(bit);
							WiCameraActivity.newpic_thumbnails_parent
									.setVisibility(View.GONE);

							if (camera_state == camera_panoramic) {
								for (int i = 0; i < m_camera_overlayui_array.length; i++) {
									m_camera_overlayui_array[i].setAlpha(1);
									m_camera_overlayui_array[i]
											.setVisibility(View.VISIBLE);

								}
								m_camera_slide_bg_camera_icon.setAlpha(255);
								m_camera_slide_bg_record_icon.setAlpha(255);
								m_camera_slide_bg_2d_icon.setAlpha(255);
								m_camera_slide_bg_3d_icon.setAlpha(255);
								m_camera_slide_bg_camera_icon
										.setVisibility(View.VISIBLE);
								m_camera_slide_bg_record_icon
										.setVisibility(View.VISIBLE);
								m_camera_slide_bg_2d_icon
										.setVisibility(View.VISIBLE);
								m_camera_slide_bg_3d_icon
										.setVisibility(View.VISIBLE);
								m_btn_panorama_sparepower
										.setVisibility(View.GONE);
								m_btn_camera_capture
										.setBackgroundResource(R.drawable.capture_button);
							}

						} else {
							StoredData.saveString(StoredData.M_REVIEWFILEPATH,
									m_bitmappicker.getCurrentFileName());
							m_imageview.resetRect();
							if (!nextorpre) {
								m_imageview.setCurbitmapR(m_bitmaps[0]);
								loadBitmap(false);
							} else {
								m_imageview.setCurbitmapR(m_bitmaps[1]);
								loadBitmap(true);
							}
							m_imageview.resetRect();
							m_imageview.invalidate();

							Bitmap bit;
							if (m_bitmappicker.getCurrentFileName().endsWith(
									"mp4")) {
								bit = OperationFile.getVideotThumbnail(
										m_bitmappicker.getCurrentFileName(),
										90, 90);

							} else {
								bit = OperationFile.fitSizeImg(
										m_bitmappicker.getCurrentFileName(),
										90, 90, 90);
							}
							m_iv_camera_newpic_thumbnails.setImageBitmap(bit);
							WiCameraActivity.newpic_thumbnails_parent
									.setVisibility(View.VISIBLE);

						}

						System.out.println("删除视频"
								+ VideoSurfaceView.M_VIDEOPATH);
					}
				});
		mMsgBox_delete.setNegativeButton(
				getResources().getText(R.string.gallery_delete_cancel),
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						m_btn_camera_review_tocamera
								.setVisibility(View.VISIBLE);
						m_btn_camera_review_tocamera.setAlpha(1);
						m_btn_camera_review_tocamera.setClickable(true);

						mMsgBox_delete.hide();
						System.out.println("随便吧");
						m_btn_camera_review_delete.setChecked(false);
					}
				});
		mMsgBox_delete.addToLayout(m_rl_camera_main_layout);
	}

	/*
	 * 设置全屏 ，没有标题
	 */
	private void setFullScreenAndNoTitle() {
		// 设置全屏，无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// 设置屏幕常亮
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	private void initVideoListView() {

		// 如果曝光度seekbar显示，就隐藏
		if (mExposureSeekBar != null) {
			if (mExposureSeekBar.getVisibility() == View.VISIBLE) {
				mExposureIV[0].setVisibility(View.GONE);
				mExposureIV[1].setVisibility(View.GONE);
				mExposureIV[2].setVisibility(View.GONE);
				mExposureSeekBar.setVisibility(View.GONE);
			}
		}

		mMenuLayout = (RelativeLayout) findViewById(R.id.menulayout);
		mSubMenuLayout = (RelativeLayout) findViewById(R.id.listItemlayout);
		mListView = (ListView) findViewById(R.id.mainlistview);
		mSubListView = (ListView) findViewById(R.id.sublistview);

		mSubMenuStateMap = new HashMap<Integer, Integer>();

		// 两个listview的HeadView

		mSubMenuTitle = (TextView) findViewById(R.id.sublisttitle);
		mSubMenuTitle.setVisibility(View.INVISIBLE);
		mSubMenuAdapter = new SubListViewAdapter(this);

		mSubListView.setVisibility(View.INVISIBLE);

		// 定义主菜单中的数据
		mListData = new ArrayList<CharSequence>();
		for (int i = 0; i < m_video_mainmenu_setting_resid.length; i++) {

			mListData.add(getResources().getText(
					m_video_mainmenu_setting_resid[i]));
		}
		mMainListViewAdapter = new MainListViewAdapter(this,
				m_video_mainmenu_setting_resid);
		mListView.setAdapter(mMainListViewAdapter);

		boolean isShotSound = StoredData.getBoolean(StoredData.M_SOUNDMODE,
				true);
		if (isShotSound) {
			// videoSurfaceView.isMute(false);
			mSubMenuStateMap.put(CAMERA_SHUTTERSOUND_START_ID, 0);
		} else {
			// videoSurfaceView.isMute(true);
			mSubMenuStateMap.put(CAMERA_SHUTTERSOUND_START_ID, 1);
		}
		int selfTimer = StoredData.getInt(StoredData.M_VIDEO_SELFTIMER, 0);
		mSubMenuStateMap.put(CAMERA_SELFTIMER_START_ID, selfTimer);
		int videobg = StoredData.getInt(StoredData.M_VIDEO_SELF_TIMERBG,
				R.drawable.popup_transparent);
		m_btn_camera_self_timer.setBackgroundResource(videobg);

		boolean isReview = StoredData.getBoolean(StoredData.M_REVIEW, true);
		if (isReview) {
			mSubMenuStateMap.put(CAMERA_REVIEW_START_ID, 0);
		} else {
			mSubMenuStateMap.put(CAMERA_REVIEW_START_ID, 1);
		}
		int sceneSwitch = StoredData.getInt(StoredData.M_SCENEMODE, 0);
		mSubMenuStateMap.put(CAMERA_SCENESWITCH_START_ID, sceneSwitch);

		int exposureID = StoredData.getInt(StoredData.M_EXPOSURE, 4);
		mSubMenuStateMap.put(CAMERA_EXPOSURE_START_ID, exposureID);

		int videoResolution = StoredData
				.getInt(StoredData.M_VIDEORESOLUTION, 1);
		String videosolutionStr = StoredData
				.getString("videoSize", "1280x720 ");
		mSubMenuStateMap.put(CAMERA_VIDEORESOLUTION_START_ID, videoResolution);
		m_btn_camera_scence.setText(videosolutionStr);

		int whitebalanceID = StoredData.getInt(StoredData.M_WHITEBALANCE, 0);
		mSubMenuStateMap.put(CAMERA_WHITEBALANCE_START_ID, whitebalanceID);

		boolean hjr = StoredData.getBoolean(StoredData.M_HJR, true);
		if (hjr) {
			mSubMenuStateMap.put(CAMERA_HANDJITTER_START_ID, 0);
		} else {
			mSubMenuStateMap.put(CAMERA_HANDJITTER_START_ID, 1);
		}

		boolean isExt = OperationFile.isExtSdcardExists();
		boolean isInt = OperationFile.isIntSdcardExists();
		if (isExt && isInt) {
			int storageMode = StoredData.getInt(StoredData.M_STORAGEMODE, 0);
			mSubMenuStateMap.put(CAMERA_SAVETO_START_ID, storageMode);
		} else {
			if (isInt) {
				StoredData.saveInt(StoredData.M_STORAGEMODE, 0);
				mSubMenuStateMap.put(CAMERA_SAVETO_START_ID, 0);
			}
			if (isExt) {
				StoredData.saveInt(StoredData.M_STORAGEMODE, 1);
				mSubMenuStateMap.put(CAMERA_SAVETO_START_ID, 1);
			}
		}
		mSubMenuStateMap.put(CAMERA_RESTOREDEFAULT_START_ID, 0);
		mSubMenuAdapter.setMap(mSubMenuStateMap);
		// 弹出自己定义的mMsgBox

	}

	private void initVideoListViewListener() {
		// TODO Auto-generated method stub
		// 注册子菜单的监听事件
		mSubListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				System.out.println("arg2:" + arg2 + "arg0.getcount()"
						+ arg0.getCount());

				// arg2 = arg2 - 1;
				for (int i = 0; i < arg0.getCount(); i++) {
					RadioButton tempButton = (RadioButton) WiCameraActivity.this
							.findViewById(i);

					if (tempButton != null) {
						if (i == arg2) {
							tempButton.setChecked(true);
						} else {
							tempButton.setChecked(false);
						}
					}
				}

				switch (arg1.getId()) {

				// 第4个菜单的ID
				case CAMERA_SHUTTERSOUND_START_ID:
					mSubMenuStateMap.put(CAMERA_SHUTTERSOUND_START_ID, arg2);
					StoredData.saveBoolean(StoredData.M_SOUNDMODE, true);
					// videoSurfaceView.isMute(false);
					break;
				case CAMERA_SHUTTERSOUND_START_ID + 1:
					mSubMenuStateMap.put(CAMERA_SHUTTERSOUND_START_ID, arg2);
					StoredData.saveBoolean(StoredData.M_SOUNDMODE, false);
					// videoSurfaceView.isMute(true);
					break;

				// 第11个菜单的ID
				case CAMERA_SELFTIMER_START_ID:
					mSubMenuStateMap.put(CAMERA_SELFTIMER_START_ID, arg2);
					StoredData.saveInt(StoredData.M_VIDEO_SELFTIMER, 0);
					m_btn_camera_self_timer
							.setBackgroundResource(R.drawable.popup_transparent);
					StoredData.saveInt(StoredData.M_VIDEO_SELF_TIMERBG,
							R.drawable.popup_transparent);
					break;
				case CAMERA_SELFTIMER_START_ID + 1:
					mSubMenuStateMap.put(CAMERA_SELFTIMER_START_ID, arg2);
					StoredData.saveInt(StoredData.M_VIDEO_SELFTIMER, 1);
					m_btn_camera_self_timer
							.setBackgroundResource(R.drawable.camera_selftimer3);
					StoredData.saveInt(StoredData.M_VIDEO_SELF_TIMERBG,
							R.drawable.camera_selftimer3);
					break;
				case CAMERA_SELFTIMER_START_ID + 2:
					mSubMenuStateMap.put(CAMERA_SELFTIMER_START_ID, arg2);
					StoredData.saveInt(StoredData.M_VIDEO_SELFTIMER, 2);
					m_btn_camera_self_timer
							.setBackgroundResource(R.drawable.camera_selftimer5);
					StoredData.saveInt(StoredData.M_VIDEO_SELF_TIMERBG,
							R.drawable.camera_selftimer5);
					break;
				case CAMERA_SELFTIMER_START_ID + 3:
					mSubMenuStateMap.put(CAMERA_SELFTIMER_START_ID, arg2);
					StoredData.saveInt(StoredData.M_VIDEO_SELFTIMER, 3);
					m_btn_camera_self_timer
							.setBackgroundResource(R.drawable.camera_selftimer10);
					StoredData.saveInt(StoredData.M_VIDEO_SELF_TIMERBG,
							R.drawable.camera_selftimer10);
					break;

				// Video resolution
				case CAMERA_VIDEORESOLUTION_START_ID:
					mSubMenuStateMap.put(CAMERA_VIDEORESOLUTION_START_ID, arg2);
					StoredData.saveInt(StoredData.M_VIDEORESOLUTION, 0);
					m_btn_camera_scence.setText("1920x1080");
					StoredData.saveString("videoSize", m_btn_camera_scence
							.getText().toString());
					break;

				case CAMERA_VIDEORESOLUTION_START_ID + 1:
					mSubMenuStateMap.put(CAMERA_VIDEORESOLUTION_START_ID, arg2);
					StoredData.saveInt(StoredData.M_VIDEORESOLUTION, 1);
					m_btn_camera_scence.setText("1280x720 ");
					StoredData.saveString("videoSize", m_btn_camera_scence
							.getText().toString());
					break;
				case CAMERA_VIDEORESOLUTION_START_ID + 2:
					mSubMenuStateMap.put(CAMERA_VIDEORESOLUTION_START_ID, arg2);
					StoredData.saveInt(StoredData.M_VIDEORESOLUTION, 2);
					m_btn_camera_scence.setText(" 800x480 ");
					StoredData.saveString("videoSize", m_btn_camera_scence
							.getText().toString());
					break;
				case CAMERA_VIDEORESOLUTION_START_ID + 3:
					mSubMenuStateMap.put(CAMERA_VIDEORESOLUTION_START_ID, arg2);
					StoredData.saveInt(StoredData.M_VIDEORESOLUTION, 3);
					m_btn_camera_scence.setText(" 640x480 ");
					StoredData.saveString("videoSize", m_btn_camera_scence
							.getText().toString());
					break;
				case CAMERA_VIDEORESOLUTION_START_ID + 4:
					mSubMenuStateMap.put(CAMERA_VIDEORESOLUTION_START_ID, arg2);
					StoredData.saveInt(StoredData.M_VIDEORESOLUTION, 4);
					m_btn_camera_scence.setText(" 352x288 ");
					StoredData.saveString("videoSize", m_btn_camera_scence
							.getText().toString());
					break;
				case CAMERA_VIDEORESOLUTION_START_ID + 5:
					mSubMenuStateMap.put(CAMERA_VIDEORESOLUTION_START_ID, arg2);
					StoredData.saveInt(StoredData.M_VIDEORESOLUTION, 5);
					m_btn_camera_scence.setText(" 320x240 ");
					StoredData.saveString("videoSize", m_btn_camera_scence
							.getText().toString());
					break;
				// 第9个菜单的ID
				case CAMERA_REVIEW_START_ID:
					mSubMenuStateMap.put(CAMERA_REVIEW_START_ID, arg2);
					StoredData.saveBoolean(StoredData.M_REVIEW, true);
					break;
				case CAMERA_REVIEW_START_ID + 1:
					mSubMenuStateMap.put(CAMERA_REVIEW_START_ID, arg2);
					StoredData.saveBoolean(StoredData.M_REVIEW, false);
					break;

				// 第10个菜单的ID
				case CAMERA_SCENESWITCH_START_ID:
					mSubMenuStateMap.put(CAMERA_SCENESWITCH_START_ID, arg2);
					StoredData.saveInt(StoredData.M_SCENEMODE, 0);
					videoSurfaceView.setSceneMode();

					break;
				case CAMERA_SCENESWITCH_START_ID + 1:
					mSubMenuStateMap.put(CAMERA_SCENESWITCH_START_ID, arg2);
					StoredData.saveInt(StoredData.M_SCENEMODE, 1);
					videoSurfaceView.setSceneMode();
					break;
				case CAMERA_SCENESWITCH_START_ID + 2:
					mSubMenuStateMap.put(CAMERA_SCENESWITCH_START_ID, arg2);
					StoredData.saveInt(StoredData.M_SCENEMODE, 2);
					videoSurfaceView.setSceneMode();
					break;
				case CAMERA_SCENESWITCH_START_ID + 3:
					mSubMenuStateMap.put(CAMERA_SCENESWITCH_START_ID, arg2);
					StoredData.saveInt(StoredData.M_SCENEMODE, 3);
					videoSurfaceView.setSceneMode();
					break;
				case CAMERA_SCENESWITCH_START_ID + 4:
					mSubMenuStateMap.put(CAMERA_SCENESWITCH_START_ID, arg2);
					StoredData.saveInt(StoredData.M_SCENEMODE, 4);
					videoSurfaceView.setSceneMode();
					break;
				case CAMERA_SCENESWITCH_START_ID + 5:
					mSubMenuStateMap.put(CAMERA_SCENESWITCH_START_ID, arg2);
					StoredData.saveInt(StoredData.M_SCENEMODE, 5);
					videoSurfaceView.setSceneMode();
					break;
				case CAMERA_SCENESWITCH_START_ID + 6:
					mSubMenuStateMap.put(CAMERA_SCENESWITCH_START_ID, arg2);
					StoredData.saveInt(StoredData.M_SCENEMODE, 6);
					videoSurfaceView.setSceneMode();
					break;
				case CAMERA_SCENESWITCH_START_ID + 7:
					mSubMenuStateMap.put(CAMERA_SCENESWITCH_START_ID, arg2);
					StoredData.saveInt(StoredData.M_SCENEMODE, 7);
					videoSurfaceView.setSceneMode();
					break;

				// 第12个菜单的ID
				case CAMERA_EXPOSURE_START_ID:
					mSubMenuStateMap.put(CAMERA_EXPOSURE_START_ID, arg2);
					StoredData.saveInt(StoredData.M_EXPOSURE, 0);
					videoSurfaceView.setExposure();
					break;
				case CAMERA_EXPOSURE_START_ID + 1:
					mSubMenuStateMap.put(CAMERA_EXPOSURE_START_ID, arg2);
					StoredData.saveInt(StoredData.M_EXPOSURE, 1);
					videoSurfaceView.setExposure();
					break;
				case CAMERA_EXPOSURE_START_ID + 2:
					mSubMenuStateMap.put(CAMERA_EXPOSURE_START_ID, arg2);
					StoredData.saveInt(StoredData.M_EXPOSURE, 2);
					videoSurfaceView.setExposure();
					break;
				case CAMERA_EXPOSURE_START_ID + 3:
					mSubMenuStateMap.put(CAMERA_EXPOSURE_START_ID, arg2);
					StoredData.saveInt(StoredData.M_EXPOSURE, 3);
					videoSurfaceView.setExposure();
					break;
				case CAMERA_EXPOSURE_START_ID + 4:
					mSubMenuStateMap.put(CAMERA_EXPOSURE_START_ID, arg2);
					StoredData.saveInt(StoredData.M_EXPOSURE, 4);
					videoSurfaceView.setExposure();
					break;
				case CAMERA_EXPOSURE_START_ID + 5:
					mSubMenuStateMap.put(CAMERA_EXPOSURE_START_ID, arg2);
					StoredData.saveInt(StoredData.M_EXPOSURE, 5);
					videoSurfaceView.setExposure();
					break;
				case CAMERA_EXPOSURE_START_ID + 6:
					mSubMenuStateMap.put(CAMERA_EXPOSURE_START_ID, arg2);
					StoredData.saveInt(StoredData.M_EXPOSURE, 6);
					videoSurfaceView.setExposure();
					break;
				case CAMERA_EXPOSURE_START_ID + 7:
					mSubMenuStateMap.put(CAMERA_EXPOSURE_START_ID, arg2);
					StoredData.saveInt(StoredData.M_EXPOSURE, 7);
					videoSurfaceView.setExposure();
					break;
				case CAMERA_EXPOSURE_START_ID + 8:
					mSubMenuStateMap.put(CAMERA_EXPOSURE_START_ID, arg2);
					StoredData.saveInt(StoredData.M_EXPOSURE, 8);
					videoSurfaceView.setExposure();
					break;

				// 第14个菜单的ID
				case CAMERA_WHITEBALANCE_START_ID:
					mSubMenuStateMap.put(CAMERA_WHITEBALANCE_START_ID, arg2);
					StoredData.saveInt(StoredData.M_WHITEBALANCE, 0);
					videoSurfaceView.setWhiteBalanceMode();
					break;
				case CAMERA_WHITEBALANCE_START_ID + 1:
					mSubMenuStateMap.put(CAMERA_WHITEBALANCE_START_ID, arg2);
					StoredData.saveInt(StoredData.M_WHITEBALANCE, 1);
					videoSurfaceView.setWhiteBalanceMode();
					break;
				case CAMERA_WHITEBALANCE_START_ID + 2:
					mSubMenuStateMap.put(CAMERA_WHITEBALANCE_START_ID, arg2);
					StoredData.saveInt(StoredData.M_WHITEBALANCE, 2);
					videoSurfaceView.setWhiteBalanceMode();
					break;
				case CAMERA_WHITEBALANCE_START_ID + 3:
					mSubMenuStateMap.put(CAMERA_WHITEBALANCE_START_ID, arg2);
					StoredData.saveInt(StoredData.M_WHITEBALANCE, 3);
					videoSurfaceView.setWhiteBalanceMode();
					break;
				case CAMERA_WHITEBALANCE_START_ID + 4:
					mSubMenuStateMap.put(CAMERA_WHITEBALANCE_START_ID, arg2);
					StoredData.saveInt(StoredData.M_WHITEBALANCE, 4);
					videoSurfaceView.setWhiteBalanceMode();
					break;

				// 第16个菜单的ID
				case CAMERA_HANDJITTER_START_ID:
					mSubMenuStateMap.put(CAMERA_HANDJITTER_START_ID, arg2);
					StoredData.saveBoolean(StoredData.M_HJR, true);
					break;
				case CAMERA_HANDJITTER_START_ID + 1:
					mSubMenuStateMap.put(CAMERA_HANDJITTER_START_ID, arg2);
					StoredData.saveBoolean(StoredData.M_HJR, false);
					break;

				// 第17个菜单的ID
				case CAMERA_SAVETO_START_ID:
					mSubMenuStateMap.put(CAMERA_SAVETO_START_ID, arg2);
					StoredData.saveInt(StoredData.M_STORAGEMODE, 0);
					// m_btn_camera_storagemode
					// .setBackgroundResource(R.drawable.storage_internal);
					if (!OperationFile.isIntSdcardExists()) {
						mSubMenuStateMap.put(CAMERA_SAVETO_START_ID, 1);
						StoredData.saveInt(StoredData.M_STORAGEMODE, 1);
						// m_btn_camera_storagemode
						// .setBackgroundResource(R.drawable.storage_sdcard);
						// 弹出自己定义的mMsgBox
						mMsgBox_sdcard = new MsgBox(WiCameraActivity.this);
						mMsgBox_sdcard
								.setBackgound(R.drawable.main_menu_window_portrait);
						mMsgBox_sdcard.setMessage(getResources().getText(
								R.string.camera_no_intsdcard));
						mMsgBox_sdcard.setPositiveButton(getResources()
								.getText(R.string.gallery_delete_confirm),
								new View.OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										mSubMenuStateMap.put(
												CAMERA_SAVETO_START_ID, 1);
										StoredData.saveInt(
												StoredData.M_STORAGEMODE, 1);
										mMsgBox_sdcard.hide();

									}
								});
						String spareSpace = OperationFile.readSDCard();
						m_btn_camera_newpic_sizeofsum.setText(spareSpace + "");
						mMsgBox_sdcard.addToLayout(m_rl_camera_main_layout);
						mMsgBox_sdcard.setRotation(-ldegree);
						mMsgBox_sdcard.show();
					}

					break;
				case CAMERA_SAVETO_START_ID + 1:
					mSubMenuStateMap.put(CAMERA_SAVETO_START_ID, arg2);
					StoredData.saveInt(StoredData.M_STORAGEMODE, 1);
					// m_btn_camera_storagemode
					// .setBackgroundResource(R.drawable.storage_sdcard);
					if (!OperationFile.isExtSdcardExists()) {
						mSubMenuStateMap.put(CAMERA_SAVETO_START_ID, 0);
						StoredData.saveInt(StoredData.M_STORAGEMODE, 0);
						// m_btn_camera_storagemode
						// .setBackgroundResource(R.drawable.storage_internal);
						// 弹出自己定义的mMsgBox
						mMsgBox_sdcard = new MsgBox(WiCameraActivity.this);
						mMsgBox_sdcard
								.setBackgound(R.drawable.main_menu_window_portrait);
						mMsgBox_sdcard.setMessage(getResources().getText(
								R.string.camera_no_extsdcard));
						mMsgBox_sdcard.setPositiveButton(getResources()
								.getText(R.string.gallery_delete_confirm),
								new View.OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										mSubMenuStateMap.put(
												CAMERA_SAVETO_START_ID, 0);
										StoredData.saveInt(
												StoredData.M_STORAGEMODE, 0);
										mMsgBox_sdcard.hide();
									}
								});
						String spareSpace = OperationFile.readSDCard();
						m_btn_camera_newpic_sizeofsum.setText(spareSpace + "");
						mMsgBox_sdcard.addToLayout(m_rl_camera_main_layout);
						mMsgBox_sdcard.setRotation(-ldegree);
						mMsgBox_sdcard.show();
					}
					break;
				}
				mSubMenuTitle.setVisibility(View.GONE);
				mSubListView.setVisibility(View.GONE);
				LISTPOSITION = -1;
				mMainListViewAdapter.notifyDataSetChanged();

			}

		});
		// 注册子菜单listview的事件
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				LISTPOSITION = arg2;
				int count = arg0.getChildCount();
				for (int i = 0; i < count; i++) {
					((TextView) arg0.getChildAt(i).findViewById(
							R.id.SettingContent))
							.setTextColor(COLORSTATELIST_WHITE);
				}

				((TextView) arg1.findViewById(R.id.SettingContent))
						.setTextColor(COLORSTATELIST_GREEN);
				Log.d(TAG, "arg2;" + arg2);
				switch (arg2) {
				case 0:
					if (mSubListView.getVisibility() == View.VISIBLE
							&& mSubMenuTitle.getText() == getResources()
									.getText(
											R.string.camera_setting_shutterSound)) {
						mSubListView.setVisibility(View.INVISIBLE);
						mSubMenuTitle.setVisibility(View.INVISIBLE);
						((TextView) arg1.findViewById(R.id.SettingContent))
								.setTextColor(COLORSTATELIST_WHITE);
						return;
					}
					mSubMenuTitle.setText(getResources().getText(
							R.string.camera_setting_shutterSound));
					mSubMenuAdapter
							.setMenuItemStartId(CAMERA_SHUTTERSOUND_START_ID);
					mSubMenuAdapter
							.setResIdArray(m_camera_submenu_smileShot_resid);
					break;
				case 1:
					if (mSubListView.getVisibility() == View.VISIBLE
							&& mSubMenuTitle.getText() == getResources()
									.getText(R.string.camera_setting_selfTimer)) {
						mSubListView.setVisibility(View.INVISIBLE);
						mSubMenuTitle.setVisibility(View.INVISIBLE);
						((TextView) arg1.findViewById(R.id.SettingContent))
								.setTextColor(COLORSTATELIST_WHITE);
						return;
					}
					mSubMenuTitle.setText(getResources().getText(
							R.string.camera_setting_selfTimer));
					mSubMenuAdapter
							.setMenuItemStartId(CAMERA_SELFTIMER_START_ID);
					mSubMenuAdapter
							.setResIdArray(m_camera_submenu_selfTimer_resid);
					break;
				case 2:
					if (mSubListView.getVisibility() == View.VISIBLE
							&& mSubMenuTitle.getText() == getResources()
									.getText(R.string.camera_setting_review)) {
						mSubListView.setVisibility(View.INVISIBLE);
						mSubMenuTitle.setVisibility(View.INVISIBLE);
						((TextView) arg1.findViewById(R.id.SettingContent))
								.setTextColor(COLORSTATELIST_WHITE);
						return;
					}
					mSubMenuTitle.setText(getResources().getText(
							R.string.camera_setting_review));
					mSubMenuAdapter.setMenuItemStartId(CAMERA_REVIEW_START_ID);
					mSubMenuAdapter
							.setResIdArray(m_camera_submenu_review_resid);
					break;
				case 3:
					if (mSubListView.getVisibility() == View.VISIBLE
							&& mSubMenuTitle.getText() == getResources()
									.getText(
											R.string.camera_setting_sceneSwitch)) {
						mSubListView.setVisibility(View.INVISIBLE);
						mSubMenuTitle.setVisibility(View.INVISIBLE);
						((TextView) arg1.findViewById(R.id.SettingContent))
								.setTextColor(COLORSTATELIST_WHITE);
						return;
					}
					mSubMenuTitle.setText(getResources().getText(
							R.string.camera_setting_sceneSwitch));
					mSubMenuAdapter
							.setMenuItemStartId(CAMERA_SCENESWITCH_START_ID);
					mSubMenuAdapter
							.setResIdArray(m_camera_submenu_sceneSwitch_resid);
					break;
				case 4:
					if (mSubListView.getVisibility() == View.VISIBLE
							&& mSubMenuTitle.getText() == getResources()
									.getText(R.string.camera_setting_exposure)) {
						mSubListView.setVisibility(View.INVISIBLE);
						mSubMenuTitle.setVisibility(View.INVISIBLE);
						mExposureSeekBar.setVisibility(View.INVISIBLE);
						mExposureIV[0].setVisibility(View.INVISIBLE);
						mExposureIV[1].setVisibility(View.INVISIBLE);
						mExposureIV[2].setVisibility(View.INVISIBLE);
						((TextView) arg1.findViewById(R.id.SettingContent))
								.setTextColor(COLORSTATELIST_WHITE);
						return;
					}
					mSubMenuTitle.setText(getResources().getText(
							R.string.camera_setting_exposure));
					mSubMenuAdapter
							.setMenuItemStartId(CAMERA_EXPOSURE_START_ID);
					mSubMenuAdapter
							.setResIdArray(m_camera_submenu_exposure_resid);
					mExposureSeekBar.setVisibility(View.VISIBLE);
					mExposureIV[0].setVisibility(View.VISIBLE);
					mExposureIV[1].setVisibility(View.VISIBLE);
					mExposureIV[2].setVisibility(View.VISIBLE);
					break;
				case 5:
					if (mSubListView.getVisibility() == View.VISIBLE
							&& mSubMenuTitle.getText() == getResources()
									.getText(
											R.string.camera_setting_videoresolution)) {
						mSubListView.setVisibility(View.INVISIBLE);
						mSubMenuTitle.setVisibility(View.INVISIBLE);
						((TextView) arg1.findViewById(R.id.SettingContent))
								.setTextColor(COLORSTATELIST_WHITE);
						return;
					}
					mSubMenuTitle.setText(getResources().getText(
							R.string.camera_setting_videoresolution));
					mSubMenuAdapter
							.setMenuItemStartId(CAMERA_VIDEORESOLUTION_START_ID);
					mSubMenuAdapter
							.setResIdArray(m_video_submenu_videoresolution);
					System.out.println("m_video_submenu_videoresolution"
							+ m_video_submenu_videoresolution.length);
					break;
				case 6:
					if (mSubListView.getVisibility() == View.VISIBLE
							&& mSubMenuTitle.getText() == getResources()
									.getText(
											R.string.camera_setting_whiteBalance)) {
						mSubListView.setVisibility(View.INVISIBLE);
						mSubMenuTitle.setVisibility(View.INVISIBLE);
						((TextView) arg1.findViewById(R.id.SettingContent))
								.setTextColor(COLORSTATELIST_WHITE);
						return;
					}
					mSubMenuTitle.setText(getResources().getText(
							R.string.camera_setting_whiteBalance));
					mSubMenuAdapter
							.setMenuItemStartId(CAMERA_WHITEBALANCE_START_ID);
					mSubMenuAdapter
							.setResIdArray(m_camera_submenu_whiteBalance_resid);
					break;
				case 7:
					if (mSubListView.getVisibility() == View.VISIBLE
							&& mSubMenuTitle.getText() == getResources()
									.getText(
											R.string.camera_setting_handJitterReduction)) {
						mSubListView.setVisibility(View.INVISIBLE);
						mSubMenuTitle.setVisibility(View.INVISIBLE);
						((TextView) arg1.findViewById(R.id.SettingContent))
								.setTextColor(COLORSTATELIST_WHITE);
						return;
					}
					mSubMenuTitle.setText(getResources().getText(
							R.string.camera_setting_handJitterReduction));
					mSubMenuAdapter
							.setMenuItemStartId(CAMERA_HANDJITTER_START_ID);
					mSubMenuAdapter
							.setResIdArray(m_camera_submenu_handJitterReduction_resid);
					break;
				case 8:
					if (mSubListView.getVisibility() == View.VISIBLE
							&& mSubMenuTitle.getText() == getResources()
									.getText(R.string.camera_setting_saveTo)) {
						mSubListView.setVisibility(View.INVISIBLE);
						mSubMenuTitle.setVisibility(View.INVISIBLE);
						((TextView) arg1.findViewById(R.id.SettingContent))
								.setTextColor(COLORSTATELIST_WHITE);
						return;
					}
					mSubMenuTitle.setText(getResources().getText(
							R.string.camera_setting_saveTo));
					mSubMenuAdapter.setMenuItemStartId(CAMERA_SAVETO_START_ID);
					mSubMenuAdapter
							.setResIdArray(m_camera_submenu_saveTo_resid);
					break;
				case 9:

					mSubMenuTitle.setText("");
					mSubMenuAdapter
							.setMenuItemStartId(CAMERA_RESTOREDEFAULT_START_ID);
					mSubMenuAdapter
							.setResIdArray(m_camera_submenu_restoreDefault_resid);
					mSubListView.setAdapter(mSubMenuAdapter);
					mSubListView.setVisibility(View.INVISIBLE);
					// MegBox显示
					mMsgBox.setRotation(-ldegree);
					mMsgBox.show();

					break;
				}
				System.out.println("mSubMenuAdapter.getMenuItemStartId()="
						+ mSubMenuAdapter.getMenuItemStartId());
				if (mSubMenuAdapter.getMenuItemStartId() == CAMERA_RESTOREDEFAULT_START_ID) {
					return;
				}
				if (mSubMenuAdapter.getMenuItemStartId() == CAMERA_EXPOSURE_START_ID) {
					mSubListView.setVisibility(View.VISIBLE);
					mSubMenuTitle.setVisibility(View.VISIBLE);
					mSubListView
							.setLayoutParams(new RelativeLayout.LayoutParams(
									283, 330));
					mSubListView.setAdapter(null);

					return;
				}

				mSubListView.setLayoutParams(new RelativeLayout.LayoutParams(
						283, ViewGroup.LayoutParams.WRAP_CONTENT));
				mExposureSeekBar.setVisibility(View.INVISIBLE);
				mExposureIV[0].setVisibility(View.INVISIBLE);
				mExposureIV[1].setVisibility(View.INVISIBLE);
				mExposureIV[2].setVisibility(View.INVISIBLE);
				mSubMenuTitle.setVisibility(View.VISIBLE);
				mSubListView.setVisibility(View.VISIBLE);
				mSubListView.setAdapter(mSubMenuAdapter);

			}
		});
	}

	/**
	 * 初始化listView
	 */
	public void initCameraListView() {

		// 初始化菜单颜色
		mTextColorWhite = getResources().getXml(
				R.drawable.textcolor_selector_white);
		mTextColorGreen = getResources().getXml(
				R.drawable.textcolor_selector_green);

		try {
			COLORSTATELIST_WHITE = ColorStateList.createFromXml(getResources(),
					mTextColorWhite);
			COLORSTATELIST_GREEN = ColorStateList.createFromXml(getResources(),
					mTextColorGreen);
		} catch (XmlPullParserException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// 如果曝光度seekbar显示，就隐藏
		if (mExposureSeekBar != null) {
			if (mExposureSeekBar.getVisibility() == View.VISIBLE) {
				mExposureIV[0].setVisibility(View.GONE);
				mExposureIV[1].setVisibility(View.GONE);
				mExposureIV[2].setVisibility(View.GONE);
				mExposureSeekBar.setVisibility(View.GONE);
			}
		}

		mMenuLayout = (RelativeLayout) findViewById(R.id.menulayout);
		// mLayout = (RelativeLayout) findViewById(R.id.layout);
		mListView = (ListView) findViewById(R.id.mainlistview);
		mSubListView = (ListView) findViewById(R.id.sublistview);

		mSubMenuStateMap = new HashMap<Integer, Integer>();

		// 两个listview的HeadView

		mSubMenuTitle = (TextView) findViewById(R.id.sublisttitle);
		mSubMenuAdapter = new SubListViewAdapter(this);
		mSubListView.setVisibility(View.INVISIBLE);

		// 定义主菜单中的数据
		mListData = new ArrayList<CharSequence>();
		for (int i = 0; i < m_camera_mainmenu_setting_resid.length; i++) {

			mListData.add(getResources().getText(
					m_camera_mainmenu_setting_resid[i]));
		}
		mMainListViewAdapter = new MainListViewAdapter(this,
				m_camera_mainmenu_setting_resid);
		mListView.setAdapter(mMainListViewAdapter);

		boolean isSimeshot = StoredData
				.getBoolean(StoredData.M_SMILESHOT, true);
		if (isSimeshot) {
			mSubMenuStateMap.put(CAMERA_SIMESHOT_START_ID, 0);
		} else {
			mSubMenuStateMap.put(CAMERA_SIMESHOT_START_ID, 1);
		}
		boolean isFaceTracking = StoredData.getBoolean(
				StoredData.M_FACETRACKING, false);
		if (isFaceTracking) {
			mSubMenuStateMap.put(CAMERA_FACETRACKING_START_ID, 0);
		} else {
			mSubMenuStateMap.put(CAMERA_FACETRACKING_START_ID, 1);
		}
		int continueShotNum = StoredData
				.getInt(StoredData.M_CONTINUESHOTNUM, 0);
		mSubMenuStateMap.put(CAMERA_CONTINUESHOT_START_ID, continueShotNum);

		if (m_btn_camera_continuous != null) {
			int camera_continuousbg = StoredData.getInt(
					StoredData.M_CAMERA_CONTINUOUSBG,
					R.drawable.burst_btn_3num_selecter);
			m_btn_camera_continuous.setBackgroundResource(camera_continuousbg);
		}

		boolean isShotSound = StoredData.getBoolean(StoredData.M_SOUNDMODE,
				true);
		if (isShotSound) {
			mSubMenuStateMap.put(CAMERA_SHUTTERSOUND_START_ID, 0);
		} else {
			mSubMenuStateMap.put(CAMERA_SHUTTERSOUND_START_ID, 1);
		}
		boolean isGridShow = StoredData.getBoolean(
				StoredData.M_GRIDDISINFINDER, false);
		System.out.println("是否显示网格" + isGridShow);
		if (isGridShow) {
			mSubMenuStateMap.put(CAMERA_GRIDDISPLAY_START_ID, 0);
			m_aux_camera_auxiliaryline.setVisibility(View.VISIBLE);

			m_aux_camera_auxiliaryline.setLineType(2);
		} else {
			mSubMenuStateMap.put(CAMERA_GRIDDISPLAY_START_ID, 1);
			m_aux_camera_auxiliaryline.setVisibility(View.GONE);
			// m_aux_camera_auxiliaryline.setLineType(0);
		}

		boolean isGsensor = StoredData.getBoolean(StoredData.m_GSENSOR, true);
		if (isGsensor) {
			mSubMenuStateMap.put(CAMERA_GSENSOR_START_ID, 0);
		} else {
			mSubMenuStateMap.put(CAMERA_GSENSOR_START_ID, 1);
		}
		boolean isAddLocation = StoredData
				.getBoolean(StoredData.M_ADDTAG, true);
		if (isAddLocation) {
			mSubMenuStateMap.put(CAMERA_ADDLCATION_START_ID, 0);
		} else {
			mSubMenuStateMap.put(CAMERA_ADDLCATION_START_ID, 1);
		}
		boolean isReview = StoredData.getBoolean(StoredData.M_REVIEW, true);

		if (isReview) {
			mSubMenuStateMap.put(CAMERA_REVIEW_START_ID, 0);
		} else {
			mSubMenuStateMap.put(CAMERA_REVIEW_START_ID, 1);
		}
		int sceneSwitch = StoredData.getInt(StoredData.M_SCENEMODE, 0);
		mSubMenuStateMap.put(CAMERA_SCENESWITCH_START_ID, sceneSwitch);

		int selfTimer = StoredData.getInt(StoredData.M_SELFTIMER, 0);
		mSubMenuStateMap.put(CAMERA_SELFTIMER_START_ID, selfTimer);
		if (m_btn_camera_self_timer != null) {
			int camera_selftimerbg = StoredData.getInt(
					StoredData.M_CAMERA_SELF_TIMERBG,
					R.drawable.popup_transparent);
			m_btn_camera_self_timer.setBackgroundResource(camera_selftimerbg);
		}
		int exposureID = StoredData.getInt(StoredData.M_EXPOSURE, 4);
		mSubMenuStateMap.put(CAMERA_EXPOSURE_START_ID, exposureID);

		int pictureSize = StoredData.getInt(StoredData.M_PICTURESIZE, 2);
		mSubMenuStateMap.put(CAMERA_PICTURESIZE_START_ID, pictureSize);

		int whitebalanceID = StoredData.getInt(StoredData.M_WHITEBALANCE, 0);
		mSubMenuStateMap.put(CAMERA_WHITEBALANCE_START_ID, whitebalanceID);

		int isoSwitch = StoredData.getInt(StoredData.M_ISO, 0);

		mSubMenuStateMap.put(CAMERA_TOUCHFOCUS_START_ID, isoSwitch);

		boolean hjr = StoredData.getBoolean(StoredData.M_HJR, true);
		if (hjr) {
			mSubMenuStateMap.put(CAMERA_HANDJITTER_START_ID, 0);
		} else {
			mSubMenuStateMap.put(CAMERA_HANDJITTER_START_ID, 1);
		}
		// 判断内置或外置sdcard是否存在
		boolean isExt = OperationFile.isExtSdcardExists();
		boolean isInt = OperationFile.isIntSdcardExists();
		System.out.println("isExt=" + isExt + "isInt=" + isInt);
		if (isExt && isInt) {
			int storageMode = StoredData.getInt(StoredData.M_STORAGEMODE, 0);
			mSubMenuStateMap.put(CAMERA_SAVETO_START_ID, storageMode);
		} else {
			if (isInt) {
				StoredData.saveInt(StoredData.M_STORAGEMODE, 0);
				mSubMenuStateMap.put(CAMERA_SAVETO_START_ID, 0);
			}
			if (isExt) {
				StoredData.saveInt(StoredData.M_STORAGEMODE, 1);
				mSubMenuStateMap.put(CAMERA_SAVETO_START_ID, 1);
			}
		}
		m_btn_camera_newpic_sizeofsum.setText(OperationFile.readSDCard());
		mSubMenuStateMap.put(CAMERA_RESTOREDEFAULT_START_ID, 0);
		mSubMenuAdapter.setMap(mSubMenuStateMap);

	}

	/*
	 * 设置camera的菜单为默认
	 */
	public void cameralistViewToDefault() {
		// 将菜单项的默认值设置保存到hashmap
		mSubMenuStateMap.put(CAMERA_SIMESHOT_START_ID, 1);
		mSubMenuStateMap.put(CAMERA_FACETRACKING_START_ID, 1);
		mSubMenuStateMap.put(CAMERA_CONTINUESHOT_START_ID, 0);
		mSubMenuStateMap.put(CAMERA_SHUTTERSOUND_START_ID, 0);
		mSubMenuStateMap.put(CAMERA_GRIDDISPLAY_START_ID, 1);
		// mSubMenuStateMap.put(CAMERA_REDEYEREMOVER_START_ID, 0);
		mSubMenuStateMap.put(CAMERA_GSENSOR_START_ID, 0);
		mSubMenuStateMap.put(CAMERA_ADDLCATION_START_ID, 0);
		mSubMenuStateMap.put(CAMERA_REVIEW_START_ID, 0);
		mSubMenuStateMap.put(CAMERA_SCENESWITCH_START_ID, 0);
		mSubMenuStateMap.put(CAMERA_SELFTIMER_START_ID, 0);
		mSubMenuStateMap.put(CAMERA_EXPOSURE_START_ID, 4);
		mSubMenuStateMap.put(CAMERA_PICTURESIZE_START_ID, 2);
		mSubMenuStateMap.put(CAMERA_WHITEBALANCE_START_ID, 0);
		// iso
		mSubMenuStateMap.put(CAMERA_TOUCHFOCUS_START_ID, 0);
		mSubMenuStateMap.put(CAMERA_HANDJITTER_START_ID, 0);
		mSubMenuStateMap.put(CAMERA_SAVETO_START_ID, 0);
		mSubMenuStateMap.put(CAMERA_RESTOREDEFAULT_START_ID, 0);
		m_btn_camera_flashmode
				.setBackgroundResource(R.drawable.camera_flash_btn_autotoon_selecter);
		m_aux_camera_auxiliaryline.setVisibility(View.GONE);
		m_btn_camera_storagemode
				.setBackgroundResource(R.drawable.storage_internal);
		m_btn_camera_continuous
				.setBackgroundResource(R.drawable.burst_btn_3num_selecter);
		StoredData.saveInt(StoredData.M_CAMERA_CONTINUOUSBG,
				R.drawable.burst_btn_3num_selecter);

		StoredData.saveInt(StoredData.M_SELFTIMER, 0);
		m_btn_camera_self_timer
				.setBackgroundResource(R.drawable.popup_transparent);
		StoredData.saveInt(StoredData.M_CAMERA_SELF_TIMERBG,
				R.drawable.popup_transparent);
		if (mExposureSeekBar != null) {
			mExposureSeekBar.setProgress(4);
		}
	}

	/*
	 * 设置录像的菜单为默认
	 */
	public void videolistViewToDefault() {
		// 将菜单项的默认值设置保存到hashmap
		mSubMenuStateMap.put(CAMERA_SHUTTERSOUND_START_ID, 0);
		mSubMenuStateMap.put(CAMERA_REVIEW_START_ID, 0);
		mSubMenuStateMap.put(CAMERA_SCENESWITCH_START_ID, 0);
		mSubMenuStateMap.put(CAMERA_SELFTIMER_START_ID, 0);
		mSubMenuStateMap.put(CAMERA_EXPOSURE_START_ID, 4);
		mSubMenuStateMap.put(CAMERA_VIDEORESOLUTION_START_ID, 1);
		mSubMenuStateMap.put(CAMERA_WHITEBALANCE_START_ID, 0);
		mSubMenuStateMap.put(CAMERA_HANDJITTER_START_ID, 0);
		mSubMenuStateMap.put(CAMERA_SAVETO_START_ID, 0);
		mSubMenuStateMap.put(CAMERA_RESTOREDEFAULT_START_ID, 0);
		m_btn_camera_flashmode
				.setBackgroundResource(R.drawable.camera_flash_btn_autotoon_selecter);
		m_btn_camera_continuous
				.setBackgroundResource(R.drawable.burst_btn_3num_selecter);
		StoredData.saveInt(StoredData.M_CAMERA_CONTINUOUSBG,
				R.drawable.burst_btn_3num_selecter);
		m_btn_camera_self_timer
				.setBackgroundResource(R.drawable.popup_transparent);
		StoredData.saveInt(StoredData.M_VIDEO_SELF_TIMERBG,
				R.drawable.popup_transparent);
		StoredData.saveInt(StoredData.M_VIDEO_SELFTIMER, 0);

		m_btn_camera_scence.setText("1280X720 ");
		if (mExposureSeekBar != null) {
			mExposureSeekBar.setProgress(4);
		}
	}

	/**
	 * 初始化listview的监听事件
	 */
	private void initCameraListViewListener() {
		// 注册子菜单的监听事件
		mSubListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				System.out.println("arg2:" + arg2);

				for (int i = 0; i < arg0.getCount(); i++) {
					RadioButton tempButton = (RadioButton) WiCameraActivity.this
							.findViewById(i);

					if (tempButton != null) {
						if (i == arg2) {
							tempButton.setChecked(true);
						} else {
							tempButton.setChecked(false);
						}
					}
				}

				switch (arg1.getId()) {

				// 第1个菜单的ID，菜单内部选项的ID是每个递增1
				case CAMERA_SIMESHOT_START_ID:
					mSubMenuStateMap.put(CAMERA_SIMESHOT_START_ID, arg2);
					StoredData.saveBoolean(StoredData.M_SMILESHOT, true);

					break;
				case CAMERA_SIMESHOT_START_ID + 1:
					mSubMenuStateMap.put(CAMERA_SIMESHOT_START_ID, arg2);
					StoredData.saveBoolean(StoredData.M_SMILESHOT, false);
					break;

				// 第2个菜单的ID
				case CAMERA_FACETRACKING_START_ID:
					mSubMenuStateMap.put(CAMERA_FACETRACKING_START_ID, arg2);
					StoredData.saveBoolean(StoredData.M_FACETRACKING, true);
					break;
				case CAMERA_FACETRACKING_START_ID + 1:
					mSubMenuStateMap.put(CAMERA_FACETRACKING_START_ID, arg2);
					StoredData.saveBoolean(StoredData.M_FACETRACKING, false);
					break;

				// 第3个菜单的ID
				case CAMERA_CONTINUESHOT_START_ID:
					mSubMenuStateMap.put(CAMERA_CONTINUESHOT_START_ID, arg2);
					StoredData.saveInt(StoredData.M_CONTINUESHOTNUM, 0);
					m_btn_camera_continuous
							.setBackgroundResource(R.drawable.burst_btn_3num_selecter);
					StoredData.saveInt(StoredData.M_CAMERA_CONTINUOUSBG,
							R.drawable.burst_btn_3num_selecter);
					// videoSurfaceView.isSingleMode(false);
					break;
				case CAMERA_CONTINUESHOT_START_ID + 1:
					mSubMenuStateMap.put(CAMERA_CONTINUESHOT_START_ID, arg2);
					StoredData.saveInt(StoredData.M_CONTINUESHOTNUM, 1);
					m_btn_camera_continuous
							.setBackgroundResource(R.drawable.burst_btn_5num_selecter);
					StoredData.saveInt(StoredData.M_CAMERA_CONTINUOUSBG,
							R.drawable.burst_btn_5num_selecter);
					// videoSurfaceView.isSingleMode(false);
					break;
				case CAMERA_CONTINUESHOT_START_ID + 2:
					mSubMenuStateMap.put(CAMERA_CONTINUESHOT_START_ID, arg2);
					StoredData.saveInt(StoredData.M_CONTINUESHOTNUM, 2);
					m_btn_camera_continuous
							.setBackgroundResource(R.drawable.burst_btn_7num_selecter);
					StoredData.saveInt(StoredData.M_CAMERA_CONTINUOUSBG,
							R.drawable.burst_btn_7num_selecter);
					// videoSurfaceView.isSingleMode(false);
					break;

				// 第4个菜单的ID
				case CAMERA_SHUTTERSOUND_START_ID:
					mSubMenuStateMap.put(CAMERA_SHUTTERSOUND_START_ID, arg2);
					StoredData.saveBoolean(StoredData.M_SOUNDMODE, true);
					break;
				case CAMERA_SHUTTERSOUND_START_ID + 1:
					mSubMenuStateMap.put(CAMERA_SHUTTERSOUND_START_ID, arg2);
					StoredData.saveBoolean(StoredData.M_SOUNDMODE, false);
					break;

				// 第5个菜单的ID
				case CAMERA_GRIDDISPLAY_START_ID:
					mSubMenuStateMap.put(CAMERA_GRIDDISPLAY_START_ID, arg2);
					StoredData.saveBoolean(StoredData.M_GRIDDISINFINDER, true);
					m_aux_camera_auxiliaryline.setVisibility(View.VISIBLE);
					m_aux_camera_auxiliaryline.setLineType(2);
					break;

				case CAMERA_GRIDDISPLAY_START_ID + 1:
					mSubMenuStateMap.put(CAMERA_GRIDDISPLAY_START_ID, arg2);
					StoredData.saveBoolean(StoredData.M_GRIDDISINFINDER, false);
					m_aux_camera_auxiliaryline.setVisibility(View.GONE);
					// m_aux_camera_auxiliaryline.setLineType(0);
					break;

				// 第7个菜单的ID
				case CAMERA_GSENSOR_START_ID:
					mSubMenuStateMap.put(CAMERA_GSENSOR_START_ID, arg2);
					StoredData.saveBoolean(StoredData.m_GSENSOR, true);
					mOrientationEventListener.enable();
					break;
				case CAMERA_GSENSOR_START_ID + 1:
					mSubMenuStateMap.put(CAMERA_GSENSOR_START_ID, arg2);
					StoredData.saveBoolean(StoredData.m_GSENSOR, false);
					mOrientationEventListener.disable();
					break;

				// 第8个菜单的ID
				case CAMERA_ADDLCATION_START_ID:
					mSubMenuStateMap.put(CAMERA_ADDLCATION_START_ID, arg2);
					StoredData.saveBoolean(StoredData.M_ADDTAG, true);
					break;
				case CAMERA_ADDLCATION_START_ID + 1:
					mSubMenuStateMap.put(CAMERA_ADDLCATION_START_ID, arg2);
					StoredData.saveBoolean(StoredData.M_ADDTAG, false);
					break;
				// 第9个菜单的ID
				case CAMERA_REVIEW_START_ID:
					mSubMenuStateMap.put(CAMERA_REVIEW_START_ID, arg2);
					StoredData.saveBoolean(StoredData.M_REVIEW, true);
					break;
				case CAMERA_REVIEW_START_ID + 1:
					mSubMenuStateMap.put(CAMERA_REVIEW_START_ID, arg2);
					StoredData.saveBoolean(StoredData.M_REVIEW, false);
					break;

				// 第10个菜单的ID
				case CAMERA_SCENESWITCH_START_ID:
					mSubMenuStateMap.put(CAMERA_SCENESWITCH_START_ID, arg2);
					StoredData.saveInt(StoredData.M_SCENEMODE, 0);
					videoSurfaceView.setSceneMode();

					break;
				case CAMERA_SCENESWITCH_START_ID + 1:
					mSubMenuStateMap.put(CAMERA_SCENESWITCH_START_ID, arg2);
					StoredData.saveInt(StoredData.M_SCENEMODE, 1);
					videoSurfaceView.setSceneMode();
					break;
				case CAMERA_SCENESWITCH_START_ID + 2:
					mSubMenuStateMap.put(CAMERA_SCENESWITCH_START_ID, arg2);
					StoredData.saveInt(StoredData.M_SCENEMODE, 2);
					videoSurfaceView.setSceneMode();
					break;
				case CAMERA_SCENESWITCH_START_ID + 3:
					mSubMenuStateMap.put(CAMERA_SCENESWITCH_START_ID, arg2);
					StoredData.saveInt(StoredData.M_SCENEMODE, 3);
					videoSurfaceView.setSceneMode();
					break;
				case CAMERA_SCENESWITCH_START_ID + 4:
					mSubMenuStateMap.put(CAMERA_SCENESWITCH_START_ID, arg2);
					StoredData.saveInt(StoredData.M_SCENEMODE, 4);
					videoSurfaceView.setSceneMode();
					break;
				case CAMERA_SCENESWITCH_START_ID + 5:
					mSubMenuStateMap.put(CAMERA_SCENESWITCH_START_ID, arg2);
					StoredData.saveInt(StoredData.M_SCENEMODE, 5);
					videoSurfaceView.setSceneMode();
					break;
				case CAMERA_SCENESWITCH_START_ID + 6:
					mSubMenuStateMap.put(CAMERA_SCENESWITCH_START_ID, arg2);
					StoredData.saveInt(StoredData.M_SCENEMODE, 6);
					videoSurfaceView.setSceneMode();
					break;
				case CAMERA_SCENESWITCH_START_ID + 7:
					mSubMenuStateMap.put(CAMERA_SCENESWITCH_START_ID, arg2);
					StoredData.saveInt(StoredData.M_SCENEMODE, 7);
					videoSurfaceView.setSceneMode();
					break;

				// 第11个菜单的ID
				case CAMERA_SELFTIMER_START_ID:
					mSubMenuStateMap.put(CAMERA_SELFTIMER_START_ID, arg2);
					StoredData.saveInt(StoredData.M_SELFTIMER, 0);
					m_btn_camera_self_timer
							.setBackgroundResource(R.drawable.popup_transparent);
					StoredData.saveInt(StoredData.M_CAMERA_SELF_TIMERBG,
							R.drawable.popup_transparent);
					break;
				case CAMERA_SELFTIMER_START_ID + 1:
					mSubMenuStateMap.put(CAMERA_SELFTIMER_START_ID, arg2);
					StoredData.saveInt(StoredData.M_SELFTIMER, 1);
					m_btn_camera_self_timer
							.setBackgroundResource(R.drawable.camera_selftimer3);
					StoredData.saveInt(StoredData.M_CAMERA_SELF_TIMERBG,
							R.drawable.camera_selftimer3);
					break;
				case CAMERA_SELFTIMER_START_ID + 2:
					mSubMenuStateMap.put(CAMERA_SELFTIMER_START_ID, arg2);
					StoredData.saveInt(StoredData.M_SELFTIMER, 2);
					m_btn_camera_self_timer
							.setBackgroundResource(R.drawable.camera_selftimer5);
					StoredData.saveInt(StoredData.M_CAMERA_SELF_TIMERBG,
							R.drawable.camera_selftimer5);
					break;
				case CAMERA_SELFTIMER_START_ID + 3:
					mSubMenuStateMap.put(CAMERA_SELFTIMER_START_ID, arg2);
					StoredData.saveInt(StoredData.M_SELFTIMER, 3);
					m_btn_camera_self_timer
							.setBackgroundResource(R.drawable.camera_selftimer10);
					StoredData.saveInt(StoredData.M_CAMERA_SELF_TIMERBG,
							R.drawable.camera_selftimer10);
					break;

				// 第12个菜单的ID
				case CAMERA_EXPOSURE_START_ID:
					mSubMenuStateMap.put(CAMERA_EXPOSURE_START_ID, arg2);
					StoredData.saveInt(StoredData.M_EXPOSURE, 0);
					videoSurfaceView.setExposure();
					break;
				case CAMERA_EXPOSURE_START_ID + 1:
					mSubMenuStateMap.put(CAMERA_EXPOSURE_START_ID, arg2);
					StoredData.saveInt(StoredData.M_EXPOSURE, 1);
					videoSurfaceView.setExposure();
					break;
				case CAMERA_EXPOSURE_START_ID + 2:
					mSubMenuStateMap.put(CAMERA_EXPOSURE_START_ID, arg2);
					StoredData.saveInt(StoredData.M_EXPOSURE, 2);
					videoSurfaceView.setExposure();
					break;
				case CAMERA_EXPOSURE_START_ID + 3:
					mSubMenuStateMap.put(CAMERA_EXPOSURE_START_ID, arg2);
					StoredData.saveInt(StoredData.M_EXPOSURE, 3);
					videoSurfaceView.setExposure();
					break;
				case CAMERA_EXPOSURE_START_ID + 4:
					mSubMenuStateMap.put(CAMERA_EXPOSURE_START_ID, arg2);
					StoredData.saveInt(StoredData.M_EXPOSURE, 4);
					videoSurfaceView.setExposure();
					break;
				case CAMERA_EXPOSURE_START_ID + 5:
					mSubMenuStateMap.put(CAMERA_EXPOSURE_START_ID, arg2);
					StoredData.saveInt(StoredData.M_EXPOSURE, 5);
					videoSurfaceView.setExposure();
					break;
				case CAMERA_EXPOSURE_START_ID + 6:
					mSubMenuStateMap.put(CAMERA_EXPOSURE_START_ID, arg2);
					StoredData.saveInt(StoredData.M_EXPOSURE, 6);
					videoSurfaceView.setExposure();
					break;
				case CAMERA_EXPOSURE_START_ID + 7:
					mSubMenuStateMap.put(CAMERA_EXPOSURE_START_ID, arg2);
					StoredData.saveInt(StoredData.M_EXPOSURE, 7);
					videoSurfaceView.setExposure();
					break;
				case CAMERA_EXPOSURE_START_ID + 8:
					mSubMenuStateMap.put(CAMERA_EXPOSURE_START_ID, arg2);
					StoredData.saveInt(StoredData.M_EXPOSURE, 8);
					videoSurfaceView.setExposure();
					break;

				// 第13个菜单的ID
				case CAMERA_PICTURESIZE_START_ID:
					mSubMenuStateMap.put(CAMERA_PICTURESIZE_START_ID, arg2);
					StoredData.saveInt(StoredData.M_PICTURESIZE, 0);
					videoSurfaceView.setPictureSize();
					String spareSpace = OperationFile.readSDCard();
					m_btn_camera_newpic_sizeofsum.setText(spareSpace + "");
					break;
				case CAMERA_PICTURESIZE_START_ID + 1:
					mSubMenuStateMap.put(CAMERA_PICTURESIZE_START_ID, arg2);
					StoredData.saveInt(StoredData.M_PICTURESIZE, 1);
					videoSurfaceView.setPictureSize();
					String spareSpace1 = OperationFile.readSDCard();
					m_btn_camera_newpic_sizeofsum.setText(spareSpace1 + "");
					break;
				case CAMERA_PICTURESIZE_START_ID + 2:
					mSubMenuStateMap.put(CAMERA_PICTURESIZE_START_ID, arg2);
					StoredData.saveInt(StoredData.M_PICTURESIZE, 2);
					videoSurfaceView.setPictureSize();
					String spareSpace2 = OperationFile.readSDCard();
					m_btn_camera_newpic_sizeofsum.setText(spareSpace2 + "");
					break;
				case CAMERA_PICTURESIZE_START_ID + 3:
					mSubMenuStateMap.put(CAMERA_PICTURESIZE_START_ID, arg2);
					StoredData.saveInt(StoredData.M_PICTURESIZE, 3);
					videoSurfaceView.setPictureSize();
					String spareSpace3 = OperationFile.readSDCard();
					m_btn_camera_newpic_sizeofsum.setText(spareSpace3 + "");
					break;
				case CAMERA_PICTURESIZE_START_ID + 4:
					mSubMenuStateMap.put(CAMERA_PICTURESIZE_START_ID, arg2);
					StoredData.saveInt(StoredData.M_PICTURESIZE, 4);
					videoSurfaceView.setPictureSize();
					String spareSpace4 = OperationFile.readSDCard();
					m_btn_camera_newpic_sizeofsum.setText(spareSpace4 + "");
					break;

				// 第14个菜单的ID
				case CAMERA_WHITEBALANCE_START_ID:
					mSubMenuStateMap.put(CAMERA_WHITEBALANCE_START_ID, arg2);
					StoredData.saveInt(StoredData.M_WHITEBALANCE, 0);
					videoSurfaceView.setWhiteBalanceMode();
					break;
				case CAMERA_WHITEBALANCE_START_ID + 1:
					mSubMenuStateMap.put(CAMERA_WHITEBALANCE_START_ID, arg2);
					StoredData.saveInt(StoredData.M_WHITEBALANCE, 1);
					videoSurfaceView.setWhiteBalanceMode();
					break;
				case CAMERA_WHITEBALANCE_START_ID + 2:
					mSubMenuStateMap.put(CAMERA_WHITEBALANCE_START_ID, arg2);
					StoredData.saveInt(StoredData.M_WHITEBALANCE, 2);
					videoSurfaceView.setWhiteBalanceMode();
					break;
				case CAMERA_WHITEBALANCE_START_ID + 3:
					mSubMenuStateMap.put(CAMERA_WHITEBALANCE_START_ID, arg2);
					StoredData.saveInt(StoredData.M_WHITEBALANCE, 3);
					videoSurfaceView.setWhiteBalanceMode();
					break;
				case CAMERA_WHITEBALANCE_START_ID + 4:
					mSubMenuStateMap.put(CAMERA_WHITEBALANCE_START_ID, arg2);
					StoredData.saveInt(StoredData.M_WHITEBALANCE, 4);
					videoSurfaceView.setWhiteBalanceMode();
					break;

				// 第15个菜单的ID
				case CAMERA_TOUCHFOCUS_START_ID:
					mSubMenuStateMap.put(CAMERA_TOUCHFOCUS_START_ID, arg2);
					StoredData.saveInt(StoredData.M_ISO, 0);
					videoSurfaceView.setISO();
					break;
				case CAMERA_TOUCHFOCUS_START_ID + 1:
					mSubMenuStateMap.put(CAMERA_TOUCHFOCUS_START_ID, arg2);
					StoredData.saveInt(StoredData.M_ISO, 1);
					videoSurfaceView.setISO();
					break;
				case CAMERA_TOUCHFOCUS_START_ID + 2:
					mSubMenuStateMap.put(CAMERA_TOUCHFOCUS_START_ID, arg2);
					StoredData.saveInt(StoredData.M_ISO, 2);
					videoSurfaceView.setISO();
					break;
				case CAMERA_TOUCHFOCUS_START_ID + 3:
					mSubMenuStateMap.put(CAMERA_TOUCHFOCUS_START_ID, arg2);
					StoredData.saveInt(StoredData.M_ISO, 3);
					videoSurfaceView.setISO();
					break;
				// 第16个菜单的ID
				case CAMERA_HANDJITTER_START_ID:
					mSubMenuStateMap.put(CAMERA_HANDJITTER_START_ID, arg2);
					StoredData.saveBoolean(StoredData.M_HJR, true);
					break;
				case CAMERA_HANDJITTER_START_ID + 1:
					mSubMenuStateMap.put(CAMERA_HANDJITTER_START_ID, arg2);
					StoredData.saveBoolean(StoredData.M_HJR, false);
					break;

				// 第17个菜单的ID
				case CAMERA_SAVETO_START_ID:
					mSubMenuStateMap.put(CAMERA_SAVETO_START_ID, arg2);
					StoredData.saveInt(StoredData.M_STORAGEMODE, 0);
					m_btn_camera_storagemode
							.setBackgroundResource(R.drawable.storage_internal);
					if (!OperationFile.isIntSdcardExists()) {
						mSubMenuStateMap.put(CAMERA_SAVETO_START_ID, 1);
						StoredData.saveInt(StoredData.M_STORAGEMODE, 1);
						m_btn_camera_storagemode
								.setBackgroundResource(R.drawable.storage_sdcard);
						// 弹出自己定义的mMsgBox
						mMsgBox_sdcard = new MsgBox(WiCameraActivity.this);
						mMsgBox_sdcard
								.setBackgound(R.drawable.main_menu_window_portrait);
						mMsgBox_sdcard.setMessage(getResources().getText(
								R.string.camera_no_intsdcard));
						mMsgBox_sdcard.setPositiveButton(getResources()
								.getText(R.string.gallery_delete_confirm),
								new View.OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										mSubMenuStateMap.put(
												CAMERA_SAVETO_START_ID, 1);
										StoredData.saveInt(
												StoredData.M_STORAGEMODE, 1);
										mMsgBox_sdcard.hide();

									}
								});
						String spareSpaces = OperationFile.readSDCard();
						m_btn_camera_newpic_sizeofsum.setText(spareSpaces + "");
						mMsgBox_sdcard.addToLayout(m_rl_camera_main_layout);
						mMsgBox_sdcard.setRotation(-ldegree);
						mMsgBox_sdcard.show();
					}
					break;
				case CAMERA_SAVETO_START_ID + 1:
					mSubMenuStateMap.put(CAMERA_SAVETO_START_ID, arg2);
					StoredData.saveInt(StoredData.M_STORAGEMODE, 1);
					m_btn_camera_storagemode
							.setBackgroundResource(R.drawable.storage_sdcard);
					if (!OperationFile.isExtSdcardExists()) {
						m_btn_camera_storagemode
								.setBackgroundResource(R.drawable.storage_internal);
						mSubMenuStateMap.put(CAMERA_SAVETO_START_ID, 0);
						StoredData.saveInt(StoredData.M_STORAGEMODE, 0);
						// 弹出自己定义的mMsgBox
						mMsgBox_sdcard = new MsgBox(WiCameraActivity.this);
						mMsgBox_sdcard
								.setBackgound(R.drawable.main_menu_window_portrait);
						mMsgBox_sdcard.setMessage(getResources().getText(
								R.string.camera_no_extsdcard));
						mMsgBox_sdcard.setPositiveButton(getResources()
								.getText(R.string.gallery_delete_confirm),
								new View.OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										mSubMenuStateMap.put(
												CAMERA_SAVETO_START_ID, 0);
										StoredData.saveInt(
												StoredData.M_STORAGEMODE, 0);
										mMsgBox_sdcard.hide();

									}
								});
						String spareSpacea = OperationFile.readSDCard();
						m_btn_camera_newpic_sizeofsum.setText(spareSpacea + "");
						mMsgBox_sdcard.addToLayout(m_rl_camera_main_layout);
						mMsgBox_sdcard.setRotation(-ldegree);
						mMsgBox_sdcard.show();
					}
					break;
				}
				mSubListView.setVisibility(View.GONE);
				mSubMenuTitle.setVisibility(View.GONE);
				LISTPOSITION = -1;
				mMainListViewAdapter.notifyDataSetChanged();
			}

		});

		// 注册子菜单listview的事件
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				LISTPOSITION = arg2;
				int count = arg0.getChildCount();
				for (int i = 0; i < count; i++) {
					((TextView) arg0.getChildAt(i).findViewById(
							R.id.SettingContent))
							.setTextColor(COLORSTATELIST_WHITE);
				}

				((TextView) arg1.findViewById(R.id.SettingContent))
						.setTextColor(COLORSTATELIST_GREEN);

				switch (arg2) {
				case 0:
					if (mSubListView.getVisibility() == View.VISIBLE
							&& mSubMenuTitle.getText() == getResources()
									.getText(R.string.camera_setting_smileShot)) {
						mSubListView.setVisibility(View.INVISIBLE);
						mSubMenuTitle.setVisibility(View.INVISIBLE);
						((TextView) arg1.findViewById(R.id.SettingContent))
								.setTextColor(COLORSTATELIST_WHITE);
						return;
					}
					mSubMenuTitle.setText(getResources().getText(
							R.string.camera_setting_smileShot));
					mSubMenuAdapter
							.setMenuItemStartId(CAMERA_SIMESHOT_START_ID);
					mSubMenuAdapter
							.setResIdArray(m_camera_submenu_smileShot_resid);
					break;
				case 1:
					if (mSubListView.getVisibility() == View.VISIBLE
							&& mSubMenuTitle.getText() == getResources()
									.getText(
											R.string.camera_setting_faceTracking)) {
						mSubListView.setVisibility(View.INVISIBLE);
						mSubMenuTitle.setVisibility(View.INVISIBLE);
						((TextView) arg1.findViewById(R.id.SettingContent))
								.setTextColor(COLORSTATELIST_WHITE);
						return;
					}
					mSubMenuTitle.setText(getResources().getText(
							R.string.camera_setting_faceTracking));
					mSubMenuAdapter
							.setMenuItemStartId(CAMERA_FACETRACKING_START_ID);
					mSubMenuAdapter
							.setResIdArray(m_camera_submenu_faceTracking_resid);

					break;
				case 2:
					if (mSubListView.getVisibility() == View.VISIBLE
							&& mSubMenuTitle.getText() == getResources()
									.getText(
											R.string.camera_setting_continueShot)) {
						mSubListView.setVisibility(View.INVISIBLE);
						mSubMenuTitle.setVisibility(View.INVISIBLE);
						((TextView) arg1.findViewById(R.id.SettingContent))
								.setTextColor(COLORSTATELIST_WHITE);
						return;
					}
					mSubMenuTitle.setText(getResources().getText(
							R.string.camera_setting_continueShot));
					mSubMenuAdapter
							.setMenuItemStartId(CAMERA_CONTINUESHOT_START_ID);
					mSubMenuAdapter
							.setResIdArray(m_camera_submenu_continueShot_resid);
					break;
				case 3:
					if (mSubListView.getVisibility() == View.VISIBLE
							&& mSubMenuTitle.getText() == getResources()
									.getText(
											R.string.camera_setting_shutterSound)) {
						mSubListView.setVisibility(View.INVISIBLE);
						mSubMenuTitle.setVisibility(View.INVISIBLE);
						((TextView) arg1.findViewById(R.id.SettingContent))
								.setTextColor(COLORSTATELIST_WHITE);
						return;
					}
					mSubMenuTitle.setText(getResources().getText(
							R.string.camera_setting_shutterSound));
					mSubMenuAdapter
							.setMenuItemStartId(CAMERA_SHUTTERSOUND_START_ID);
					mSubMenuAdapter
							.setResIdArray(m_camera_submenu_smileShot_resid);
					break;
				case 4:
					if (mSubListView.getVisibility() == View.VISIBLE
							&& mSubMenuTitle.getText() == getResources()
									.getText(
											R.string.camera_setting_gridDisplayInViewfinder)) {
						mSubListView.setVisibility(View.INVISIBLE);
						mSubMenuTitle.setVisibility(View.INVISIBLE);
						((TextView) arg1.findViewById(R.id.SettingContent))
								.setTextColor(COLORSTATELIST_WHITE);
						return;
					}
					mSubMenuTitle.setText(getResources().getText(
							R.string.camera_setting_gridDisplayInViewfinder));
					mSubMenuAdapter
							.setMenuItemStartId(CAMERA_GRIDDISPLAY_START_ID);
					mSubMenuAdapter
							.setResIdArray(m_camera_submenu_gridDisplay_resid);
					break;
				case 5:
					if (mSubListView.getVisibility() == View.VISIBLE
							&& mSubMenuTitle.getText() == getResources()
									.getText(
											R.string.camera_setting_gSensorForJPEGOrientationFlag)) {
						mSubListView.setVisibility(View.INVISIBLE);
						mSubMenuTitle.setVisibility(View.INVISIBLE);
						((TextView) arg1.findViewById(R.id.SettingContent))
								.setTextColor(COLORSTATELIST_WHITE);
						return;
					}
					mSubMenuTitle
							.setText(getResources()
									.getText(
											R.string.camera_setting_gSensorForJPEGOrientationFlag));
					mSubMenuAdapter.setMenuItemStartId(CAMERA_GSENSOR_START_ID);
					mSubMenuAdapter
							.setResIdArray(m_camera_submenu_gSensor_resid);
					break;
				case 6:
					if (mSubListView.getVisibility() == View.VISIBLE
							&& mSubMenuTitle.getText() == getResources()
									.getText(
											R.string.camera_setting_addLocationTag_n)) {
						mSubListView.setVisibility(View.INVISIBLE);
						mSubMenuTitle.setVisibility(View.INVISIBLE);
						((TextView) arg1.findViewById(R.id.SettingContent))
								.setTextColor(COLORSTATELIST_WHITE);
						return;
					}
					mSubMenuTitle.setText(getResources().getText(
							R.string.camera_setting_addLocationTag_n));
					mSubMenuAdapter
							.setMenuItemStartId(CAMERA_ADDLCATION_START_ID);
					mSubMenuAdapter
							.setResIdArray(m_camera_submenu_addLocationTag_resid);
					break;
				case 7:
					if (mSubListView.getVisibility() == View.VISIBLE
							&& mSubMenuTitle.getText() == getResources()
									.getText(R.string.camera_setting_review)) {
						mSubListView.setVisibility(View.INVISIBLE);
						mSubMenuTitle.setVisibility(View.INVISIBLE);
						((TextView) arg1.findViewById(R.id.SettingContent))
								.setTextColor(COLORSTATELIST_WHITE);
						return;
					}
					mSubMenuTitle.setText(getResources().getText(
							R.string.camera_setting_review));
					mSubMenuAdapter.setMenuItemStartId(CAMERA_REVIEW_START_ID);
					mSubMenuAdapter
							.setResIdArray(m_camera_submenu_review_resid);
					break;
				case 8:
					if (mSubListView.getVisibility() == View.VISIBLE
							&& mSubMenuTitle.getText() == getResources()
									.getText(
											R.string.camera_setting_sceneSwitch)) {
						mSubListView.setVisibility(View.INVISIBLE);
						mSubMenuTitle.setVisibility(View.INVISIBLE);
						((TextView) arg1.findViewById(R.id.SettingContent))
								.setTextColor(COLORSTATELIST_WHITE);
						return;
					}
					mSubMenuTitle.setText(getResources().getText(
							R.string.camera_setting_sceneSwitch));
					mSubMenuAdapter
							.setMenuItemStartId(CAMERA_SCENESWITCH_START_ID);
					mSubMenuAdapter
							.setResIdArray(m_camera_submenu_sceneSwitch_resid);
					break;
				case 9:
					if (mSubListView.getVisibility() == View.VISIBLE
							&& mSubMenuTitle.getText() == getResources()
									.getText(R.string.camera_setting_selfTimer)) {
						mSubListView.setVisibility(View.INVISIBLE);
						mSubMenuTitle.setVisibility(View.INVISIBLE);
						((TextView) arg1.findViewById(R.id.SettingContent))
								.setTextColor(COLORSTATELIST_WHITE);
						return;
					}
					mSubMenuTitle.setText(getResources().getText(
							R.string.camera_setting_selfTimer));
					mSubMenuAdapter
							.setMenuItemStartId(CAMERA_SELFTIMER_START_ID);
					mSubMenuAdapter
							.setResIdArray(m_camera_submenu_selfTimer_resid);
					break;
				case 10:
					if (mSubListView.getVisibility() == View.VISIBLE
							&& mSubMenuTitle.getText() == getResources()
									.getText(R.string.camera_setting_exposure)) {
						mSubListView.setVisibility(View.INVISIBLE);
						mSubMenuTitle.setVisibility(View.INVISIBLE);

						mExposureSeekBar.setVisibility(View.INVISIBLE);
						mExposureIV[0].setVisibility(View.INVISIBLE);
						mExposureIV[1].setVisibility(View.INVISIBLE);
						mExposureIV[2].setVisibility(View.INVISIBLE);

						((TextView) arg1.findViewById(R.id.SettingContent))
								.setTextColor(COLORSTATELIST_WHITE);
						return;
					}
					mSubMenuTitle.setText(getResources().getText(
							R.string.camera_setting_exposure));
					mSubMenuAdapter
							.setMenuItemStartId(CAMERA_EXPOSURE_START_ID);
					mSubMenuAdapter
							.setResIdArray(m_camera_submenu_exposure_resid);

					mExposureSeekBar.setVisibility(View.VISIBLE);
					mExposureIV[0].setVisibility(View.VISIBLE);
					mExposureIV[1].setVisibility(View.VISIBLE);
					mExposureIV[2].setVisibility(View.VISIBLE);

					break;
				case 11:
					if (mSubListView.getVisibility() == View.VISIBLE
							&& mSubMenuTitle.getText() == getResources()
									.getText(
											R.string.camera_setting_pictureSize)) {
						mSubListView.setVisibility(View.INVISIBLE);
						mSubMenuTitle.setVisibility(View.INVISIBLE);
						((TextView) arg1.findViewById(R.id.SettingContent))
								.setTextColor(COLORSTATELIST_WHITE);
						return;
					}
					mSubMenuTitle.setText(getResources().getText(
							R.string.camera_setting_pictureSize));
					mSubMenuAdapter
							.setMenuItemStartId(CAMERA_PICTURESIZE_START_ID);
					mSubMenuAdapter
							.setResIdArray(m_camera_submenu_pictureSize_resid);
					break;
				case 12:
					if (mSubListView.getVisibility() == View.VISIBLE
							&& mSubMenuTitle.getText() == getResources()
									.getText(
											R.string.camera_setting_whiteBalance)) {
						mSubListView.setVisibility(View.INVISIBLE);
						mSubMenuTitle.setVisibility(View.INVISIBLE);
						((TextView) arg1.findViewById(R.id.SettingContent))
								.setTextColor(COLORSTATELIST_WHITE);
						return;
					}
					mSubMenuTitle.setText(getResources().getText(
							R.string.camera_setting_whiteBalance));
					mSubMenuAdapter
							.setMenuItemStartId(CAMERA_WHITEBALANCE_START_ID);
					mSubMenuAdapter
							.setResIdArray(m_camera_submenu_whiteBalance_resid);
					break;
				case 13:
					if (mSubListView.getVisibility() == View.VISIBLE
							&& mSubMenuTitle.getText() == getResources()
									.getText(R.string.camera_setting_touchFocus)) {
						mSubListView.setVisibility(View.INVISIBLE);
						mSubMenuTitle.setVisibility(View.INVISIBLE);
						((TextView) arg1.findViewById(R.id.SettingContent))
								.setTextColor(COLORSTATELIST_WHITE);
						return;
					}
					mSubMenuTitle.setText(getResources().getText(
							R.string.camera_setting_touchFocus));
					mSubMenuAdapter
							.setMenuItemStartId(CAMERA_TOUCHFOCUS_START_ID);
					mSubMenuAdapter.setResIdArray(m_camera_submenu_ISO_resid);
					break;
				case 14:
					if (mSubListView.getVisibility() == View.VISIBLE
							&& mSubMenuTitle.getText() == getResources()
									.getText(
											R.string.camera_setting_handJitterReduction)) {
						mSubListView.setVisibility(View.INVISIBLE);
						mSubMenuTitle.setVisibility(View.INVISIBLE);
						((TextView) arg1.findViewById(R.id.SettingContent))
								.setTextColor(COLORSTATELIST_WHITE);
						return;
					}
					mSubMenuTitle.setText(getResources().getText(
							R.string.camera_setting_handJitterReduction));
					mSubMenuAdapter
							.setMenuItemStartId(CAMERA_HANDJITTER_START_ID);
					mSubMenuAdapter
							.setResIdArray(m_camera_submenu_handJitterReduction_resid);
					break;
				case 15:
					if (mSubListView.getVisibility() == View.VISIBLE
							&& mSubMenuTitle.getText() == getResources()
									.getText(R.string.camera_setting_saveTo)) {
						mSubListView.setVisibility(View.INVISIBLE);
						mSubMenuTitle.setVisibility(View.INVISIBLE);
						((TextView) arg1.findViewById(R.id.SettingContent))
								.setTextColor(COLORSTATELIST_WHITE);
						return;
					}
					mSubMenuTitle.setText(getResources().getText(
							R.string.camera_setting_saveTo));
					mSubMenuAdapter.setMenuItemStartId(CAMERA_SAVETO_START_ID);
					mSubMenuAdapter
							.setResIdArray(m_camera_submenu_saveTo_resid);
					break;
				case 16:

					mSubMenuTitle.setText("");
					mSubMenuAdapter
							.setMenuItemStartId(CAMERA_RESTOREDEFAULT_START_ID);
					mSubMenuAdapter
							.setResIdArray(m_camera_submenu_restoreDefault_resid);
					mSubListView.setAdapter(mSubMenuAdapter);
					mSubListView.setVisibility(View.INVISIBLE);
					// MegBox显示
					mMsgBox.setRotation(-ldegree);
					mMsgBox.show();

					break;
				case 17:

					if (mCurrentColorList == COLORSTATELIST_GREEN) {
						mCurrentColorList = COLORSTATELIST_WHITE;
					} else {
						mCurrentColorList = COLORSTATELIST_GREEN;
					}

					((TextView) arg1.findViewById(R.id.SettingContent))
							.setTextColor(mCurrentColorList);
					Intent intent = new Intent();
					intent.setClass(WiCameraActivity.this, CameraPreview.class);
					startActivity(intent);

					// WiCameraActivity.this.finish();
					break;
				}

				if (arg2 == 17) {
					return;
				}
				if (mSubMenuAdapter.getMenuItemStartId() == CAMERA_RESTOREDEFAULT_START_ID) {
					return;
				}

				if (mSubMenuAdapter.getMenuItemStartId() == CAMERA_EXPOSURE_START_ID) {
					mSubListView.setVisibility(View.VISIBLE);
					mSubMenuTitle.setVisibility(View.VISIBLE);
					mSubListView
							.setLayoutParams(new RelativeLayout.LayoutParams(
									283, 330));
					mSubListView.setAdapter(null);

					return;
				}

				mSubListView.setLayoutParams(new RelativeLayout.LayoutParams(
						283, ViewGroup.LayoutParams.WRAP_CONTENT));
				mExposureSeekBar.setVisibility(View.INVISIBLE);
				mExposureIV[0].setVisibility(View.INVISIBLE);
				mExposureIV[1].setVisibility(View.INVISIBLE);
				mExposureIV[2].setVisibility(View.INVISIBLE);
				mSubListView.setVisibility(View.VISIBLE);
				mSubListView.setAdapter(mSubMenuAdapter);
				mSubMenuTitle.setVisibility(View.VISIBLE);

			}
		});
	}

	private void initListTextStyle() {
		int count = mListView.getChildCount();
		for (int i = 0; i < count; i++) {
			((TextView) mListView.getChildAt(i).findViewById(
					R.id.SettingContent)).setTextColor(COLORSTATELIST_WHITE);
		}
	}

	/**
	 * 初始化控件
	 */
	private void initControl() {
		// TODO Auto-generated method stub
		// 获取是否是3D
		StoredData
				.saveBoolean(StoredData.M_CAMERA3D, CSStaticData.g_is_3D_mode);
		System.out.println("是否是3D g=" + CSStaticData.g_is_3D_mode);
		/* 取得屏幕对象 */
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
		/**
		 * 对含有surfaceview的绝对布局的初始化
		 */
		m_aux_camera_auxiliaryline = (AuxiliaryLine) findViewById(R.id.aul_camera_auxiliaryline);
		m_aux_camera_auxiliaryline.setSize(screenWidth, screenHeight);
		m_rl_camera_main_layout = (RelativeLayout) findViewById(R.id.rl_camera_main_layout);
		// 屏幕自适应
		fitScreenSize();

		m_al_camera_surfaceui = (RelativeLayout) findViewById(R.id.al_camera_surface_ui);
		m_al_surfaceview = (RelativeLayout) findViewById(R.id.surfaceView);
		// videoSurfaceView = (VideoSurfaceView)
		// findViewById(R.id.vsv_videosurfaceview);
		m_iv_camera_focus = (ImageView) findViewById(R.id.iv_camera_focus);
		m_fd_face = (FaceIndicater) findViewById(R.id.fi_face);
		m_ch_camera_time_count = (Chronometer) findViewById(R.id.ch_camera_time_count);
		m_ch_camera_time_count1 = (Chronometer) findViewById(R.id.ch_camera_time_count1);
		/**
		 * 对录像和照相悬浮的绝对布局初始化
		 */
		m_al_camera_overlayui = (RelativeLayout) findViewById(R.id.al_camera_overlay_ui);
		m_btn_camera_setting = (ToggleButton) findViewById(R.id.btn_camera_setting);
		m_btn_camera_used_camera = (ToggleButton) findViewById(R.id.btn_camera_used_camera);
		m_btn_camera_flashmode = (ToggleButton) findViewById(R.id.btn_camera_flashmode);
		m_iv_camera_newpic_thumbnails = (ImageView) findViewById(R.id.iv_camera_newpic_thumbnails);
		m_btn_camera_sparepower = (Button) findViewById(R.id.btn_camera_sparepower);
		m_btn_panorama_sparepower = (Button) findViewById(R.id.btn_panorama_sparepower);
		m_btn_camera_storagemode = (Button) findViewById(R.id.btn_camera_storagemode);
		m_btn_camera_scence = (Button) findViewById(R.id.btn_camera_scence);
		m_btn_camera_newpic_sizeofsum = (Button) findViewById(R.id.btn_camera_newpic_sizeOfsum);
		m_btn_camera_newpic_sizeofsum.setShadowLayer(3, 3, 1, Color.BLACK);
		m_btn_camera_self_timer = (Button) findViewById(R.id.btn_self_timer);

		m_btn_camera_panoramic = (ToggleButton) findViewById(R.id.btn_camera_panoramic);
		m_btn_camera_continuous = (ToggleButton) findViewById(R.id.btn_camera_continuous);
		m_btn_camera_capture = (ToggleButton) findViewById(R.id.btn_camera_capture);

		m_skb_camera_zoom_size = new ArcSeekBar(this);
		LayoutParams lpzoombtn = new LayoutParams(175, 175);
		lpzoombtn.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lpzoombtn.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		lpzoombtn.rightMargin = pxTodip(getBaseContext(), 45);
		lpzoombtn.topMargin = -10;
		m_skb_camera_zoom_size.setLayoutParams(lpzoombtn);

		m_skb_camera_zoom_size.setQuad(ArcSeekBar.POS_4ST_QUAD);
		m_skb_camera_zoom_size
				.setBackground(R.drawable.zoom_focus_curve_camera);
		m_skb_camera_zoom_size.setThumb(R.drawable.zoom_camera_focus_btn);
		m_skb_camera_zoom_size
				.setThumbPressed(R.drawable.zoom_camera_focus_btn_click);
		// m_skb_camera_zoom_size.setRotation(-13);
		m_skb_camera_zoom_size.setScaleX(1f);
		m_skb_camera_zoom_size.setProgress(0);
		m_skb_camera_zoom_size.setMax(28);
		m_al_camera_overlayui.addView(m_skb_camera_zoom_size);

		m_btn_camera_uiright_bg = (Button) findViewById(R.id.btn_camera_uiright_bg);
		m_rl_camera_main_layout.getWidth();
		int ii = sbarh;
		float scaleY = (float) (getWindowManager().getDefaultDisplay()
				.getHeight()) / 480f;

		RelativeLayout.LayoutParams layoutParams_camera_btn = new RelativeLayout.LayoutParams(
				(int) (204f * scaleY), (int) (475f * scaleY));
		layoutParams_camera_btn.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		layoutParams_camera_btn.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		m_btn_camera_uiright_bg.setLayoutParams(layoutParams_camera_btn);
		// m_btn_camera_dimension = (SlideButton)
		// findViewById(R.id.btn_camera_dimension);
		m_btn_camera_dimension = new SlideButton(WiCameraActivity.this);

		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				pxTodip(WiCameraActivity.this, 118), pxTodip(
						WiCameraActivity.this, 65));
		// layoutParams.setMargins(0, 120,
		// 40, 0);

		System.out.println("---" + sbarh);
		layoutParams.setMargins(0, 0, -3, -12);
		// layoutParams.setMargins(0, 0, -3 + 32, 2);
		layoutParams
				.addRule(RelativeLayout.ABOVE, m_btn_camera_capture.getId());
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		m_btn_camera_dimension.setRotation(-57);
		m_btn_camera_dimension.setLayoutParams(layoutParams);

		m_btn_camera_dimension.setBackgroundResid(R.drawable.slide_btn_bg,
				R.drawable.btn_3d2d_3d_normal_psd,
				R.drawable.btn_3d2d_2d_normal_psd,
				R.drawable.btn_3d2d_3d_pressed_psd,
				R.drawable.btn_3d2d_2d_pressed_psd);

		m_btn_camera_dimension.setChecked(true);

		m_al_camera_overlayui.addView(m_btn_camera_dimension);

		// 拖动按钮---------------------------------------------------------------------------
		m_btn_camera_captureorrecord = (SlideButton) findViewById(R.id.btn_camera_captureorreocord);
		m_camera_slide_bg_camera_icon = (ImageView) findViewById(R.id.slide_bg_camera_icon);
		m_camera_slide_bg_record_icon = (ImageView) findViewById(R.id.slide_bg_record_icon);
		m_camera_slide_bg_2d_icon = (ImageView) findViewById(R.id.slide_bg_2d_icon);
		m_camera_slide_bg_3d_icon = (ImageView) findViewById(R.id.slide_bg_3d_icon);

		m_btn_camera_captureorrecord.setBackgroundResid(
				R.drawable.slide_btn_bg,
				R.drawable.record_btn_select_normal_psd_rotate,
				R.drawable.switch_camera_select_normal_psd_rotate,
				R.drawable.record_btn_select_click_psd_rotate,
				R.drawable.switch_camera_select_click_psd_rotate);

		m_btn_camera_captureorrecord.setChecked(true);

		/**
		 * 对review的绝对布局的初始化
		 */
		m_al_camera_reviewui = (RelativeLayout) findViewById(R.id.al_camera_review_ui);
		m_al_camera_reviewui_wiiamgeview = (RelativeLayout) findViewById(R.id.al_camera_review_ui_wiimageview);
		m_imageview = new WiImageView(WiCameraActivity.this, m_bitmappicker);
		m_imageview.setOrientationType(1);
		m_al_camera_reviewui_wiiamgeview.addView(m_imageview);
		m_iv_camera_review_thumbnails = (ImageView) findViewById(R.id.iv_camera_review_thumbnails);
		m_btn_camera_review_delete = (ToggleButton) findViewById(R.id.btn_camera_review_delete);
		m_btn_camera_review_share = (ToggleButton) findViewById(R.id.btn_camera_review_share);
		m_btn_camera_review_information = (ToggleButton) findViewById(R.id.btn_camera_review_information);
		m_btn_camera_review_tocamera = (Button) findViewById(R.id.btn_camera_review_tocamera);
		m_btn_camera_review_play_video = (Button) findViewById(R.id.btn_camera_review_play_video);
		btn_camera_review_play_videoparent = (RelativeLayout) findViewById(R.id.btn_camera_review_play_videoparent);
		// 拍照，录像时控件的数组
		m_camera_overlayui_array = new View[] { m_btn_camera_setting,// 0
				m_btn_camera_used_camera,// 1
				m_btn_camera_flashmode,// 2
				m_iv_camera_newpic_thumbnails,// 3
				m_btn_camera_sparepower,// 4
				m_btn_camera_storagemode,// 5
				m_btn_camera_scence,// 6
				m_btn_camera_newpic_sizeofsum,// 7
				m_btn_camera_self_timer,// 8
				m_skb_camera_zoom_size,// 9
				m_btn_camera_dimension,// 10
				m_btn_camera_capture,// 11
				m_btn_camera_captureorrecord,// 12
				m_btn_camera_panoramic,// 13
				m_btn_camera_continuous,// 14
				m_btn_camera_uiright_bg // 15
		};
		// 重新预览的控件的数组
		m_camera_reviewui_array = new View[] { m_iv_camera_review_thumbnails,// 0
				m_btn_camera_review_delete,// 1
				m_btn_camera_review_share,// 2
				m_btn_camera_review_information,// 3
				m_btn_camera_review_tocamera,// 4
				m_btn_camera_review_play_video // 5
		};
		try {
			if (batteryReceiver != null) {
				unregisterReceiver(batteryReceiver);
			}
			IntentFilter batteryLevelFilter = new IntentFilter(
					Intent.ACTION_BATTERY_CHANGED);
			batteryReceiver = new BatteryReceiver(m_btn_camera_sparepower,
					m_btn_panorama_sparepower);
			registerReceiver(batteryReceiver, batteryLevelFilter);
		} catch (Exception e) {

		}

		// exposure进度条
		mExposureSeekBar = (SeekBar) findViewById(R.id.exposure);
		mExposureIV = new ImageView[3];
		mExposureIV[0] = (ImageView) findViewById(R.id.exposure_icon_left);
		mExposureIV[1] = (ImageView) findViewById(R.id.exposure_icon_right);
		mExposureIV[2] = (ImageView) findViewById(R.id.exposure_bg);
		/**
		 * 注册事件
		 */
		m_btn_camera_captureorrecord
				.setOnChangedListener(onCheckedChangedListenerOfCameraOrVideo);
		m_btn_camera_dimension
				.setOnChangedListener(onCheckedChangedListenerOf3D);

		// }
		m_btn_camera_capture.setOnClickListener(myListener);
		m_btn_camera_review_tocamera.setOnClickListener(myListener);
		m_btn_camera_flashmode.setOnClickListener(myListener);
		m_btn_camera_used_camera.setOnClickListener(myListener);
		m_btn_camera_continuous.setOnClickListener(myListener);
		m_btn_camera_review_delete.setOnClickListener(myListener);
		m_skb_camera_zoom_size.setProgress(0);
		m_skb_camera_zoom_size
				.setOnSeekBarChangeListener(mySeekBarChangeListener);
		m_btn_camera_setting.setOnClickListener(myListener);
		m_btn_camera_review_share.setOnClickListener(myListener);

		m_btn_camera_used_camera.setOnClickListener(myListener);
		// videoSurfaceView.setOnTouchListener(onTouchListener);
		m_btn_camera_sparepower.setOnClickListener(myListener);
		m_btn_camera_storagemode.setOnClickListener(myListener);
		m_btn_camera_scence.setOnClickListener(myListener);
		m_btn_camera_newpic_sizeofsum.setOnClickListener(myListener);
		m_btn_camera_uiright_bg.setOnClickListener(myListener);
		m_btn_camera_self_timer.setOnClickListener(myListener);
		m_imageview.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent ev) {
				m_gesturedetector.onTouchEvent(ev);
				if (m_imageview.mode == 1 || m_isMove
						|| (m_isNextBitmapLoaded > 0)) {
					m_stopdispatch = true;
				}
				// 当imageview触摸时，隐藏菜单
				else {
					if (!mMsgBox_delete.isShown()) {
						m_btn_camera_review_share.setChecked(false);
						mShareMenu.hide();
					}
					m_btn_camera_review_information.setChecked(false);
				}

				if (m_stopdispatch) {
					switch (ev.getAction()) {
					case MotionEvent.ACTION_DOWN:
					case MotionEvent.ACTION_MOVE:
					case MotionEvent.ACTION_POINTER_2_DOWN:
						return true;
					case MotionEvent.ACTION_UP:
						m_stopdispatch = false;
						return true;
					}
				}
				m_imageview.onTouchEvent(ev);
				if (m_imageview.m_Scale == 1.0f) {
					switch (ev.getAction()) {
					case MotionEvent.ACTION_DOWN: {
						m_downisonthisview = true;
						m_FirstTouchX = convertXY(mCurrentDegree, ev,m_imageview.is_3D);
						m_moveX = 0;
						m_moveIsOver = false;
						break;
					}
					case MotionEvent.ACTION_MOVE: {
						if (m_downisonthisview) {
							if (!m_moveIsOver) {
								float mCurX;
								mCurX = convertXY(mCurrentDegree, ev,m_imageview.is_3D);
								// Log.e(Tag, "mCurX" + mCurX);
								// Log.e(Tag, "mFirstTouchX" + m_FirstTouchX);
								m_moveX = (mCurX - m_FirstTouchX) / 2;
								if (m_moveX != 0)
									showNextOnMove(m_moveX < 0);

							}
						}
						break;
					}
					case MotionEvent.ACTION_POINTER_2_DOWN:
					case MotionEvent.ACTION_UP: {
						m_downisonthisview = false;
						m_moveIsOver = true;
						if (m_moveX < 0
								&& m_bitmappicker.getCurrentFileIndex() == (m_bitmappicker
										.getFileCounts() - 1) || m_moveX > 0
								&& m_bitmappicker.getCurrentFileIndex() == 0) {
							return true;
						}
						if (!m_isMove && m_moveX != 0) {
							if (Math.abs(m_moveX) < screenWidth_Review / 4) {
								moveBack(m_moveX, true);
							} else {
								if (m_moveX > 0) {
									m_bitmappicker.getPreBitmap();
									loadBitmap(false);
								} else {

									m_bitmappicker.getNextBitmap();
									loadBitmap(true);
								}
								moveBack(m_moveX, false);
							}
						}
						m_moveX = 0;

					}
					}

				}

				return true;
			}
		});
		m_iv_camera_review_thumbnails.setOnTouchListener(onTouchListener);
		m_iv_camera_newpic_thumbnails.setOnClickListener(myListener);
		m_btn_camera_review_information.setOnClickListener(myListener);
		m_btn_camera_panoramic.setOnClickListener(myListener);
		m_btn_camera_review_play_video.setOnClickListener(myListener);
		int expoid = StoredData.getInt(StoredData.M_EXPOSURE, 4);
		mExposureSeekBar.setProgress(expoid);
		mExposureSeekBar.setMax(8);
		mExposureSeekBar.setOnSeekBarChangeListener(mes);

		m_main_handle = new Handler() {
			@Override
			public void dispatchMessage(Message msg) {
				// TODO Auto-generated method stub
				super.dispatchMessage(msg);

				// 录像浏览时候删除录像，接收后list删除录像路径
				if (msg.what == InterSurfaceView.CAMERA_VIDEOLIST_UPDATE) {
					String m = msg.obj.toString();
					ArrayList<String> lst = videoSurfaceView.getVideoFileList();
					if (m_bitmappicker != null) {
						m_bitmappicker.deleteFile();
					}
					if (lst.contains(m)) {
						lst.remove(m);
					}
					videoSurfaceView.setVideoFileList(lst);
					// 更新缩略图标
					updateThumbnails(lst);
					updateReiewThumBnails();
				}
				// 图片浏览时候删除录像，接收后list删除图片路径
				if (msg.what == InterSurfaceView.CAMERA_PICLIST_UPDATE) {
					// System.out.println("删除图片" + msg.obj.toString());
					String m = msg.obj.toString();
					ArrayList<String> lst = videoSurfaceView.getPicFileList();
					if (m_bitmappicker != null) {
						m_bitmappicker.deleteFile();
					}

					if (lst.contains(m)) {
						lst.remove(m);
					}
					// System.out.println("删除图片 图片的链表的长度为：" + lst.size() + ""
					// + m_bitmappicker.getfilepathList().size());
					videoSurfaceView.setPicFileList(lst);
					// 更新缩略图标
					updateThumbnails(lst);
					updateReiewThumBnails();
				}

				// 用于2D和3D切换的时间延时
				if (msg.what == 8000) {
					if (camera_state != camera_record) {
						m_btn_camera_dimension.setEnabled(true);
					}
				}
				if (msg.what == 500) {
					if (isPanoramaMode) {
						preparePanoramic();
						m_btn_camera_panoramic.setChecked(true);
						videoSurfaceView.setPicFileList(getIntent()
								.getStringArrayListExtra("fileList"));
						videoSurfaceView.stopPreview();
						videoSurfaceView.startPreview();
						videoSurfaceView.startFaceDetection();
						if (videoSurfaceView.getPicFileList() == null) {
							videoSurfaceView
									.setPicFileList(new ArrayList<String>());
						}
						ArrayList<String> fileList = videoSurfaceView
								.getPicFileList();
						System.out.println("videoSurfaceView.getPicFileList()="
								+ videoSurfaceView.getPicFileList());
					}
				}
				// 重新计时拍照
				if (msg.what == 10000) {
					// 设置开始计时时间
					m_ch_camera_time_count1.setBase(SystemClock
							.elapsedRealtime());
					// 启动计时器
					m_ch_camera_time_count1.start();
				}
				// 重新计时拍照
				if (msg.what == 20000) {
					continueShot();
				}
				// 连拍完后恢复按钮状态
				if (msg.what == 3) {

					isContinus = false;
					for (int i = 0; i < m_camera_overlayui_array.length; i++) {
						m_camera_overlayui_array[i].setEnabled(true);
						m_camera_overlayui_array[i].setClickable(true);
						m_camera_overlayui_array[i].setFocusable(true);
					}
					if (!isRear) {
						m_btn_camera_dimension.setEnabled(false);
						m_btn_camera_dimension.setClickable(false);
						m_btn_camera_dimension.setFocusable(false);
						m_btn_camera_panoramic.setEnabled(false);
						m_btn_camera_panoramic.setClickable(false);
						m_btn_camera_panoramic.setFocusable(false);
					}
					if (CSStaticData.g_is_3D_mode) {
						m_btn_camera_captureorrecord.setEnabled(false);
						m_btn_camera_panoramic.setEnabled(false);
						m_btn_camera_captureorrecord.setClickable(false);
						m_btn_camera_captureorrecord.setFocusable(false);
						m_btn_camera_panoramic.setClickable(false);
						m_btn_camera_panoramic.setFocusable(false);
					}
					m_skb_camera_zoom_size.setEnabled(true);

				}
				// 单拍完后恢复按钮状态
				if (msg.what == 4) {
					if (camera_state == camera_continus) {
						m_btn_camera_capture
								.setBackgroundResource(R.drawable.capture_burst_button_selecter);
						m_btn_camera_capture.setChecked(false);
						System.out.println("连拍完毕了......................");
					}

					isContinus = false;
					for (int i = 0; i < m_camera_overlayui_array.length; i++) {
						m_camera_overlayui_array[i].setEnabled(true);
						m_camera_overlayui_array[i].setClickable(true);
						m_camera_overlayui_array[i].setFocusable(true);
					}
					if (!isRear) {
						m_btn_camera_dimension.setEnabled(false);
						m_btn_camera_panoramic.setEnabled(false);
						m_btn_camera_dimension.setClickable(false);
						m_btn_camera_dimension.setFocusable(false);
						m_btn_camera_panoramic.setClickable(false);
						m_btn_camera_panoramic.setFocusable(false);
					}
					if (CSStaticData.g_is_3D_mode) {
						m_btn_camera_captureorrecord.setEnabled(false);
						m_btn_camera_panoramic.setEnabled(false);
						m_btn_camera_captureorrecord.setClickable(false);
						m_btn_camera_captureorrecord.setFocusable(false);
						m_btn_camera_panoramic.setClickable(false);
						m_btn_camera_panoramic.setFocusable(false);
					}
					m_skb_camera_zoom_size.setEnabled(true);

				}

				if (msg.what == 200) {
					// 在全景或3D拍照时拍是按钮状态切为横屏
					if (msg.arg1 == 1) {
						ldegree = 360;
					} else {
						// 在全景或3D拍照时拍取消按钮状态恢复为屏幕当前状态
						ldegree = mCurrentDegree;
					}
					System.out
							.println("degree为"
									+ ldegree
									+ ""
									+ mCurrentDegree
									+ "..........................................................");
					System.out.println("handle接受到消息啦。。。。。");
					if (ldegree == 90 || ldegree == 270) {
						m_btn_camera_dimension.setButtonRotation(-ldegree);
						m_btn_camera_captureorrecord
								.setButtonRotation(-ldegree);
						m_camera_slide_bg_camera_icon.setRotation(-ldegree);
						m_camera_slide_bg_record_icon.setRotation(-ldegree);
						m_camera_slide_bg_2d_icon.setRotation(-ldegree);
						m_camera_slide_bg_3d_icon.setRotation(-ldegree);

						LayoutParams lp = (LayoutParams) mMenuLayout
								.getLayoutParams();
						// lp.leftMargin = 80;
						// lp.topMargin = 45;这是以前的参数
						// 6/5之后添加了一下参数：
						if (ldegree == 90) {
							lp.setMargins(150, 65, -200, -100);
							lp.height = 520;// 在竖屏时改变主菜单的高
							lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

							mMenuLayout.setRotation(-ldegree);
							mMenuLayout.setPivotX(160);
							mMenuLayout.setPivotY(220);
							mMenuLayout.setLayoutParams(lp);
							mMenuLayout.invalidate();
						} else if (ldegree == 270) {
							lp.setMargins(230, 0, 0, -100);
							lp.height = 520;// 在竖屏时改变主菜单的高
							lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

							mMenuLayout.setRotation(-ldegree);
							mMenuLayout.setPivotX(160);
							mMenuLayout.setPivotY(220);
							mMenuLayout.setLayoutParams(lp);
							mMenuLayout.invalidate();
						}

						// Toast.makeText(WiCameraActivity.this, "竖屏",
						// 500).show();

						// 竖屏

						mMsgBox.setRotation(-ldegree);
						// mMsgBox.invalidate();
						mMsgBox_delete.setRotation(-ldegree);
						mMsgBox_delete
								.setMsgBoxLayoutParams(new RelativeLayout.LayoutParams(
										400, 300));
						mMsgBox_Panoramacancel.setRotation(-ldegree);
						if (mMsgBox_sdcard != null) {
							mMsgBox_sdcard.setRotation(-ldegree);
						}
						// mMsgBox_delete.invalidate();
						if (camera_mMsgBox_info != null) {
							// camera_mMsgBox_info.setRotation(-ldegree);
							RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
									400, 650);
							// layoutParams
							// .addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

							// layoutParams.setMargins(100, 0, 0, 70);
							camera_mMsgBox_info.setLayoutParams(layoutParams);
							camera_mMsgBox_info.setRotation(-ldegree);
							camera_mMsgBox_info.setTranslationX(150);
							// camera_mMsgBox_info.invalidate();
						}

						if (mShareMenu != null) {
							LayoutParams lps = (LayoutParams) new LayoutParams(
									300,
									android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

							mShareMenu.setLayoutParams(lps);
							mShareMenu.invalidate();
							mShareMenu.setTranslationX(180);
							mShareMenu.setRotation(-ldegree);

						}

						// m_btn_camera_dimension.invalidate();
					} else if (ldegree == 360 || ldegree == 0 || ldegree == 180) {

						m_btn_camera_dimension.setButtonRotation(-ldegree);
						m_btn_camera_captureorrecord
								.setButtonRotation(-ldegree);
						m_camera_slide_bg_camera_icon.setRotation(-ldegree);
						m_camera_slide_bg_record_icon.setRotation(-ldegree);
						m_camera_slide_bg_2d_icon.setRotation(-ldegree);
						m_camera_slide_bg_3d_icon.setRotation(-ldegree);

						if (ldegree == 180) {
							LayoutParams lp = (LayoutParams) mMenuLayout
									.getLayoutParams();

							lp.setMargins(110, 33, 0, 0);
							lp.height = 540;// 在横屏时改变主菜单的高
							lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
							mMenuLayout.setLayoutParams(lp);
							mMenuLayout.setRotation(-ldegree);
							mMenuLayout.setPivotX(190);
							mMenuLayout.setPivotY(225);
							mMenuLayout.invalidate();
						} else if (ldegree == 360 || ldegree == 0) {
							LayoutParams lp = (LayoutParams) mMenuLayout
									.getLayoutParams();

							lp.setMargins(90, 33, -200, 0);
							lp.height = 540;// 在横屏时改变主菜单的高
							lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
							mMenuLayout.setLayoutParams(lp);
							mMenuLayout.setRotation(-ldegree);
							mMenuLayout.invalidate();
						}

						// 横屏
						System.out.println(mCurrentDegree + "mCurrentDegree");
						mMsgBox.setRotation(-ldegree);
						if (mMsgBox_sdcard != null) {
							mMsgBox_sdcard.setRotation(-ldegree);
						}
						// mMsgBox.invalidate();
						mMsgBox_delete
								.setMsgBoxLayoutParams(new RelativeLayout.LayoutParams(
										500, 350));
						mMsgBox_delete.setRotation(-ldegree);
						mMsgBox_Panoramacancel.setRotation(-ldegree);
						// mMsgBox_delete.invalidate();
						if (camera_mMsgBox_info != null) {
							camera_mMsgBox_info.setRotation(-ldegree);
							RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
									400, 650);
							layoutParams
									.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
							// layoutParams.setMargins(20, 80, 0, 20);
							camera_mMsgBox_info.setTranslationX(0);
							layoutParams.topMargin = 100;
							layoutParams.rightMargin = 35;
							camera_mMsgBox_info.setLayoutParams(layoutParams);
							// camera_mMsgBox_info.invalidate();
						}

						if (mShareMenu != null) {
							LayoutParams lps = (LayoutParams) new LayoutParams(
									300,
									android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
							lps.rightMargin = 100;
							lps.topMargin = 80;
							lps.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
							mShareMenu.setTranslationX(0);
							mShareMenu.setLayoutParams(lps);
							mShareMenu.setRotation(-ldegree);
							mShareMenu.invalidate();
							//
						}

					}
					System.out.println("计算的mCurrentDegree=" + mCurrentDegree);
					// 在屏幕旋转时候控件位置的改变
					if (mCurrentDegree == 90 || mCurrentDegree == 270) {
						if (msg.arg1 == 1) {
							reviewRotateDegree(360);
						} else {
							reviewRotateDegree(90);
						}

						if (camera_state != camera_record) {
							if (msg.arg1 == 1) {
								cameraRotateDegree(360);
							} else {
								cameraRotateDegree(90);
							}
						} else {
							if (m_isRecord) {
								recordRotateDegree(90, true);
							} else {
								if (msg.arg1 == 1) {
									recordRotateDegree(360, false);
								} else {
									recordRotateDegree(90, false);
								}
							}

						}
					} else if (mCurrentDegree == 360 || mCurrentDegree == 0
							|| mCurrentDegree == 180) {
						reviewRotateDegree(360);

						if (camera_state != camera_record) {

							cameraRotateDegree(360);

						} else {
							if (m_isRecord) {
								recordRotateDegree(360, true);
							} else {
								recordRotateDegree(360, false);
							}

						}
					}

					// if (mPreOrientation != mOrientation) {
					boolean is3D = StoredData.getBoolean(StoredData.M_CAMERA3D, false);
					if(!is3D)
					{
						if (mCurrentDegree == 90 || mCurrentDegree == 270) {
							TDStaticData.SCREEN_HEIGHT = getWindowManager()
									.getDefaultDisplay().getWidth();
							TDStaticData.SCREEN_WIDTH = getWindowManager()
									.getDefaultDisplay().getHeight();
							TDStaticData.SCREEN_WIDTH_ORG = TDStaticData.SCREEN_WIDTH;
							TDStaticData.SCREEN_HEIGHT_ORG = TDStaticData.SCREEN_HEIGHT;

						} else if (mCurrentDegree == 360 || mCurrentDegree == 180) {
							TDStaticData.SCREEN_HEIGHT = getWindowManager()
									.getDefaultDisplay().getHeight();
							TDStaticData.SCREEN_WIDTH = getWindowManager()
									.getDefaultDisplay().getWidth();
							TDStaticData.SCREEN_WIDTH_ORG = TDStaticData.SCREEN_WIDTH;
							TDStaticData.SCREEN_HEIGHT_ORG = TDStaticData.SCREEN_HEIGHT;
						}
						screenWidth_Review = TDStaticData.SCREEN_WIDTH;
						screenHeight_Review = TDStaticData.SCREEN_HEIGHT;
						if (m_al_camera_reviewui.getVisibility() == View.VISIBLE) {
								WiImageView.recycleDBitmap(m_imageview.m_curbitmap);
								WiImageView.recycleDBitmap(m_imageview.m_nextbitmap);
								WiImageView.recycleDBitmap(m_imageview.m_tempbitmap);
								WiImageView.recycleDBitmap(m_bitmaps[0]);
								WiImageView.recycleDBitmap(m_bitmaps[1]);
								
								m_imageview.setOritention(mCurrentDegree);
								m_imageview
										.setCurbitmapR(m_imageview
												.getNextBitmapEx(m_bitmappicker
														.getFirBitmap()));
								m_imageview.setBackgroundColor(Color.BLACK);
								m_imageview.resetRect();
								m_imageview.invalidate();
								if (m_bitmappicker.getCurrentFileIndex() < m_bitmappicker
										.getFileCounts() - 1)
									loadBitmap(true);
								if (m_bitmappicker.getCurrentFileIndex() > 0)
									loadBitmap(false);
								
							
						}
					}
			
					// boolean demon =
					// StoredData.getBoolean(StoredData.M_CAMERA3D, false);
					// if(!demon)
					// {
					if (msg.arg1 == 1) {// 2d 3d

						a = getAngle(mPreOrientation, 270);
					}
					if (msg.arg1 == 2) {// 3d 2d
						a = getAngle(270, mOrientation);
					}
					if (msg.arg1 == 0) {// auto
						a = getAngle(mPreOrientation, mOrientation);
					}
					// }

					AnimationSet as = null;

					AnimationSet ass = new AnimationSet(true);
					Animation iAnimation = new RotateAnimation(start,
							start + a, Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 0.5f);
					iAnimation.setDuration(400);
					iAnimation.setFillAfter(true);
					ass.addAnimation(iAnimation);
					ass.setFillAfter(true);
					// System.out.println("m_isReview" + m_isReview);
					// if (m_isReview) {
					if (mCurrentDegree == 0 || mCurrentDegree == 360
							|| mCurrentDegree == 90 || mCurrentDegree == 180
							|| mCurrentDegree == 270) {
						for (int i = 0; i < m_camera_reviewui_array.length; i++) {
							if (i != 0) {
								as = new AnimationSet(true);
								Animation rAnimation = new RotateAnimation(
										start, start + a,
										Animation.RELATIVE_TO_SELF, 0.5f,
										Animation.RELATIVE_TO_SELF, 0.5f);
								rAnimation.setDuration(400);
								rAnimation.setFillAfter(true);
								AlphaAnimation al = new AlphaAnimation(0, 1);
								al.setDuration(2000);
								al.setFillAfter(true);
								as.addAnimation(al);
								as.addAnimation(rAnimation);
								as.setFillAfter(true);
								m_camera_reviewui_array[i].startAnimation(as);
								m_camera_reviewui_array[i].invalidate();
								// 旋转触摸对焦的图片
								// if (m_iv_camera_focus.getVisibility() ==
								// View.VISIBLE) {
								m_iv_camera_focus.setRotation(mCurrentDegree);
								// }
							} else {
								if (!m_isReview) {
									as = new AnimationSet(true);
									Animation rAnimation = new RotateAnimation(
											start, start + a,
											Animation.RELATIVE_TO_SELF, 0.5f,
											Animation.RELATIVE_TO_SELF, 0.5f);
									rAnimation.setDuration(400);
									rAnimation.setFillAfter(true);
									as.addAnimation(rAnimation);
									as.setFillAfter(true);
									m_camera_reviewui_array[0]
											.startAnimation(as);
									m_camera_reviewui_array[0].invalidate();
								} else {
									mOrientationEventListener
											.setRate(M_ISLANSCAPE);
									mOrientationEventListener.reviewRorate(
											M_ISLANSCAPE, a, start);
								}
							}
						}
						m_al_camera_reviewui.postInvalidate();

						// //////////////////////////////////////

						// ////////////////////////////////////
						// } else {

						for (int i = 0; i < m_camera_overlayui_array.length; i++) {
							if (i == 3) {
								m_camera_overlayui_array[i].startAnimation(ass);
								m_camera_overlayui_array[i].invalidate();
							} else if (i != 10 && i != 12 && i != 9 && i != 15) {
								if (i >= 4 && i <= 8) {
									as = new AnimationSet(true);
									Animation rAnimation = new RotateAnimation(
											start, start + a,
											Animation.RELATIVE_TO_SELF, 0.5f,
											Animation.RELATIVE_TO_SELF, 0.5f);
									rAnimation.setDuration(400);
									rAnimation.setFillAfter(true);
									AlphaAnimation al = new AlphaAnimation(0, 1);
									al.setDuration(2000);
									al.setFillAfter(true);
									as.addAnimation(al);
									as.addAnimation(rAnimation);
									as.setFillAfter(true);
									m_camera_overlayui_array[i]
											.startAnimation(as);
									m_camera_overlayui_array[i].invalidate();
								} else {
									as = new AnimationSet(true);
									Animation rAnimation = new RotateAnimation(
											start, start + a,
											Animation.RELATIVE_TO_SELF, 0.5f,
											Animation.RELATIVE_TO_SELF, 0.5f);
									rAnimation.setDuration(400);
									rAnimation.setFillAfter(true);
									as.addAnimation(rAnimation);
									as.setFillAfter(true);
									m_camera_overlayui_array[i]
											.startAnimation(as);
									m_camera_overlayui_array[i].invalidate();
								}
							}
						}
						if (msg.arg1 != 1) {
							mPreOrientation = mOrientation;
						}
						start += a;
					}
					//
					videoSurfaceView.setRotationParm();
				}

				// }

			}
		};
		// 监听拍照计时的计时器
		m_ch_camera_time_count1
				.setOnChronometerTickListener(new OnChronometerTickListener() {

					@Override
					public void onChronometerTick(Chronometer chronometer) {
						// TODO Auto-generated method stub
						long time = SystemClock.elapsedRealtime()
								- m_ch_camera_time_count1.getBase();
						String str = (String) chronometer.getText();
						int cameraSelfTimeId = StoredData.getInt(
								StoredData.M_SELFTIMER, 0);
						int videoSelfTimeId = StoredData.getInt(
								StoredData.M_VIDEO_SELFTIMER, 0);
						int cameraSelfTime = videoSurfaceView.CAMERA_SELFTIMERARRAY[cameraSelfTimeId];
						int videoSelfTime = videoSurfaceView.VIDEO_SELFTIME[videoSelfTimeId];

						if (Util.CAMERA_STATE == InterSurfaceView.CAMERA_CONTINUOUSANDSELFTIME) {
							if (time >= 10 && time < cameraSelfTime * 1000) {
								videoSurfaceView.PlaySounds(0);
							} else if (time >= cameraSelfTime * 1000) {
								videoSurfaceView.PlaySounds(1);
								// }
								System.out.println("计时时间为：" + str + "自定义时间为："
										+ cameraSelfTime + "连拍");

								continueShot();
								m_ch_camera_time_count1.setBase(SystemClock
										.elapsedRealtime());
								m_ch_camera_time_count1.stop();
							}
						}
						// }
						if (Util.CAMERA_STATE == InterSurfaceView.CAMERA_SINGLE_SHOT) {
							m_btn_camera_continuous.setEnabled(false);
							m_btn_camera_captureorrecord.setEnabled(false);
							if (time > 10 && time < cameraSelfTime * 1000) {
								videoSurfaceView.PlaySounds(0);
							} else if (time >= cameraSelfTime * 1000) {
								videoSurfaceView.PlaySounds(1);
								// }
								System.out.println("计时时间为：" + str + "自定义时间为："
										+ cameraSelfTime + "单拍1");
								// if (time >= cameraSelfTime * 1000) {
								System.out.println("计时时间为：" + str + "自定义时间为："
										+ cameraSelfTime + "单拍");
								long time1 = new Date().getTime();
								cameraUp();
								m_ch_camera_time_count1.setBase(SystemClock
										.elapsedRealtime());
								m_ch_camera_time_count1.stop();
								long time2 = new Date().getTime();
								System.out.println("拍照时间为:...................."
										+ (time2) + "time1=" + time1);
								// videoSurfaceView.releaseMediaplayer();
							}
						}
						if (Util.CAMERA_STATE == InterSurfaceView.CAMERA_VIDEO) {
							if (time >= 10 && time < videoSelfTime * 1000) {
								System.out.println("计时时间为：" + time + "自定义时间为："
										+ videoSelfTime + "录像");
								videoSurfaceView.PlaySounds(0);
							} else if (time >= videoSelfTime * 1000) {
								videoSurfaceView.PlaySounds(1);
								// }
								// if (time >= videoSelfTime * 1000) {
								System.out.println("计时时间为：" + str + "自定义时间为："
										+ videoSelfTime + "录像");
								toStartRecord();
								m_ch_camera_time_count1.setBase(SystemClock
										.elapsedRealtime());
								m_ch_camera_time_count1.stop();
								// videoSurfaceView.releaseMediaplayer();
							}
						}
					}
				});

	}

	public static int pxTodip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;

		return (int) (pxValue * scale + 0.5f);
	}

	public float convertXY(int orientation, MotionEvent ev,boolean is3D) {
		float a = 0;
		if(is3D)
		{
			return ev.getX();
		}
		
		if (orientation == 90) {
			a = TDStaticData.SCREEN_WIDTH - ev.getY();
		} else if (orientation == 270) {
			a = ev.getY();
		} else if (orientation == 360) {
			a = ev.getX();
		} else if (orientation == 180) {
			a = TDStaticData.SCREEN_HEIGHT - ev.getX();
		}
		return a;
	}

	/**
	 * 是否显示预览界面
	 * 
	 * @param isShow
	 */
	public void isShowReview(boolean isShow) {

		if (isShow) {
			// if(camera_state!=camera_record){
			// videoSurfaceView.setPreviewSize(800, 480);
			videoSurfaceView.stopPreview();

			// }
			m_al_camera_surfaceui.setVisibility(View.GONE);
			m_al_camera_overlayui.setVisibility(View.GONE);
			m_al_camera_reviewui.setVisibility(View.VISIBLE);

			// Collections.reverse(videoSurfaceView.getVideoFileList());
			// Collections.reverse(videoSurfaceView.getPicFileList());
			System.out.println("picSize=+=================="
					+ videoSurfaceView.getPicFileList() + "videoSize="
					+ videoSurfaceView.getVideoFileList());
			// 得到list，并反转
			picList = new ArrayList<String>();
			videoList = new ArrayList<String>();

			if (Util.CAMERA_STATE == InterSurfaceView.CAMERA_VIDEO) {
				if (videoSurfaceView.getVideoFileList() != null) {
					for (int i = videoSurfaceView.getVideoFileList().size() - 1; i >= 0; i--) {
						videoList.add(videoSurfaceView.getVideoFileList()
								.get(i));
					}
				}
				m_bitmappicker = new MediaFilePicker(videoList,
						videoList.get(0));
				m_imageview.isScalable = false;
			} else {
				if (videoSurfaceView.getPicFileList() != null) {
					for (int i = videoSurfaceView.getPicFileList().size() - 1; i >= 0; i--) {
						picList.add(videoSurfaceView.getPicFileList().get(i));
					}
				}

				m_bitmappicker = new MediaFilePicker(picList, picList.get(0));
				m_imageview.isScalable = true;
			}
			boolean is3D = StoredData.getBoolean(StoredData.M_CAMERA3D, false);
			// if (is3D) {
			// setScreenDimension(is3D);
			// }
			m_imageview.setMediaPicker(m_bitmappicker);

			m_imageview.setConvertAuto(is3D);
			m_imageview.is_3D = is3D;
			// Log.e(Tag, "m_bitmappicker元素个数" +
			// m_bitmappicker.getFileCounts());
			if (mCurrentDegree == 90 || mCurrentDegree == 270) {
				TDStaticData.SCREEN_HEIGHT = getWindowManager()
						.getDefaultDisplay().getWidth();
				TDStaticData.SCREEN_WIDTH = getWindowManager()
						.getDefaultDisplay().getHeight();
				TDStaticData.SCREEN_WIDTH_ORG = TDStaticData.SCREEN_WIDTH;
				TDStaticData.SCREEN_HEIGHT_ORG = TDStaticData.SCREEN_HEIGHT;

			} else if (mCurrentDegree == 360 || mCurrentDegree == 180) {
				TDStaticData.SCREEN_HEIGHT = getWindowManager()
						.getDefaultDisplay().getHeight();
				TDStaticData.SCREEN_WIDTH = getWindowManager()
						.getDefaultDisplay().getWidth();
				TDStaticData.SCREEN_WIDTH_ORG = TDStaticData.SCREEN_WIDTH;
				TDStaticData.SCREEN_HEIGHT_ORG = TDStaticData.SCREEN_HEIGHT;
			}
			if(is3D)
			{
				TDStaticData.SCREEN_HEIGHT = getWindowManager()
						.getDefaultDisplay().getHeight();
				TDStaticData.SCREEN_WIDTH = getWindowManager()
						.getDefaultDisplay().getWidth();
				TDStaticData.SCREEN_WIDTH_ORG = TDStaticData.SCREEN_WIDTH;
				TDStaticData.SCREEN_HEIGHT_ORG = TDStaticData.SCREEN_HEIGHT;
			}
			screenWidth_Review = TDStaticData.SCREEN_WIDTH;
			screenHeight_Review = TDStaticData.SCREEN_HEIGHT;
			if (is3D) {
				m_imageview.setOritention(360);
				
			} else {
				m_imageview.setOritention(mCurrentDegree);
			}
			// m_imageview.setOperateOrientation(mCurrentDegree);
			m_imageview.setCurbitmap(m_imageview.getNextBitmapEx(m_bitmappicker
					.getFirBitmap()));
			m_imageview.setBackgroundColor(Color.BLACK);
			m_imageview.resetRect();
			m_imageview.invalidate();
			if (m_bitmappicker.getCurrentFileIndex() < m_bitmappicker
					.getFileCounts() - 1)
				loadBitmap(true);
			if (m_bitmappicker.getCurrentFileIndex() > 0)
				loadBitmap(false);

		} else {
			if (camera_state != camera_record) {
				videoSurfaceView.startPreview();
				videoSurfaceView.startFaceDetection();
			} else {
				// videoSurfaceView.setPictureSize();
				// videoSurfaceView.setPreviewSize();
				videoSurfaceView.startPreview();
			}
			m_al_camera_overlayui.setVisibility(View.VISIBLE);
			m_al_camera_surfaceui.setVisibility(View.VISIBLE);
			m_btn_camera_setting.setChecked(false);
			m_al_camera_reviewui.setVisibility(View.GONE);
			if (mShareMenu.isShown()) {
				mShareMenu.hide();
			}

			WiImageView.recycleDBitmap(m_imageview.m_curbitmap);
			WiImageView.recycleDBitmap(m_imageview.m_nextbitmap);
			WiImageView.recycleDBitmap(m_imageview.m_tempbitmap);
			WiImageView.recycleDBitmap(m_bitmaps[0]);
			WiImageView.recycleDBitmap(m_bitmaps[1]);
			if (camera_state == camera_panoramic) {
				onBackgroundThreadFinished();
				reset();
				mImageView.setVisibility(View.GONE);
				// startCameraPreview();
				mText.setText("start another one ");
			}
			boolean is3D = StoredData.getBoolean(StoredData.M_CAMERA3D, false);
			// if (is3D) {
			// setScreenDimension(false);
			// }
		}
	}

	/**
	 * 把屏幕切为2D或3D状态
	 * 
	 * @param is3D
	 *            如果为true 则切换为3D状态，否则切为2D状态
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

	int keydowncount = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		switch (event.getKeyCode()) {
		// 对焦
		case KeyEvent.KEYCODE_FOCUS:
			if (keydowncount == 0) {
				LayoutParams lp = new LayoutParams(200, 200);
				lp.addRule(RelativeLayout.CENTER_IN_PARENT);
				m_iv_camera_focus.setLayoutParams(lp);

				m_iv_camera_focus.setVisibility(View.VISIBLE);
				// videoSurfaceView.handler.sendEmptyMessageDelayed(videoSurfaceView.CAMERA_FOCUAREA,
				// 3000);
				System.out.println("key focus...............................");
				videoSurfaceView.camerasFocus(true);
				++keydowncount;
				return true;
			}
			break;
		// 拍照
		case KeyEvent.KEYCODE_CAMERA:
			if (keydowncount == 1) {
				m_iv_camera_focus.setVisibility(View.GONE);
				// videoSurfaceView.camerasFocus(false);
				// videoSurfaceView.isfocuskeyDown=true;
				cameraUp();
				System.out
						.println("key capture...............................");
				keydowncount = 0;
				return false;
			}
			break;

		default:
			break;
		}

		return super.onKeyDown(keyCode, event);

	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {

		// TODO Auto-generated method stub
		switch (event.getKeyCode()) {
		// 对焦按钮key up时候把 keydowncount置为0 ，防止不多次对焦
		case KeyEvent.KEYCODE_FOCUS:
			System.out
					.println("key up keydowncount...............................");
			m_iv_camera_focus.setVisibility(View.GONE);
			keydowncount = 0;
			break;
		// 按下back键是动作
		case KeyEvent.KEYCODE_BACK:
			// 在camera為打開時按下back鍵無效
			if (!m_isReview) {
				if (!isCameraOpen) {
					return true;
				}
			}
			// 在連拍時按下back鍵退出
			if (camera_state == camera_continus) {
				if (isContinus) {
					isContinus = false;
					// setScreenDimension(false);
					finish();
					// android.os.Process.killProcess(android.os.Process.myPid());
					return true;
				}
			}

			// isRear = true;
			System.out.println("m_isReview=" + m_isReview);
			// 在預覽時按下back鍵的操作，主要實現逐步退出
			if (m_isReview) {
				if (mMsgBox_delete.isShown()) {
					mMsgBox_delete.hide();
					m_btn_camera_review_delete.setChecked(false);

					m_btn_camera_review_tocamera.setVisibility(View.VISIBLE);
					m_btn_camera_review_tocamera.setAlpha(1);
					m_btn_camera_review_tocamera.setClickable(true);

					return true;
				}

				if (camera_mMsgBox_info != null) {
					if (camera_mMsgBox_info.isShown()) {
						// for (int i = 1; i < m_camera_reviewui_array.length;
						// i++) {
						// m_camera_reviewui_array[i].setEnabled(true);
						// }
						camera_mMsgBox_info.hideAndRemove();
						m_btn_camera_review_information.setChecked(false);
						camera_mMsgBox_info = null;
						return true;
					}
				}
				if (mShareMenu.isShown()) {
					m_btn_camera_review_share.setChecked(false);
					mShareMenu.hide();
					return true;
				} else {
					m_isReview = false;
					isShowReview(false);
					m_btn_camera_capture.setChecked(false);
					// 在全景預覽時退出后恢復按鈕狀態
					if (camera_state == camera_panoramic) {
						for (int i = 0; i < m_camera_overlayui_array.length; i++) {
							m_camera_overlayui_array[i].setAlpha(1);
							m_camera_overlayui_array[i]
									.setVisibility(View.VISIBLE);

						}
						m_camera_slide_bg_camera_icon.setAlpha(255);
						m_camera_slide_bg_record_icon.setAlpha(255);
						m_camera_slide_bg_2d_icon.setAlpha(255);
						m_camera_slide_bg_3d_icon.setAlpha(255);
						m_camera_slide_bg_camera_icon
								.setVisibility(View.VISIBLE);
						m_camera_slide_bg_record_icon
								.setVisibility(View.VISIBLE);
						m_camera_slide_bg_2d_icon.setVisibility(View.VISIBLE);
						m_camera_slide_bg_3d_icon.setVisibility(View.VISIBLE);
						m_btn_panorama_sparepower.setVisibility(View.GONE);
						m_btn_camera_capture
								.setBackgroundResource(R.drawable.capture_button);
						videoSurfaceView.stopPreview();
						videoSurfaceView.startPreview();
						return true;
					}
					return true;
				}

			} else {
				// 非在全景状态下退出，主要实现逐步退出
				if (!isPanoramaMode) {
					// 子菜单的消失
					boolean isMsgboxShow = mMsgBox.isShown();
					int isSubListViewShow = mSubListView.getVisibility();
					int isMainListViewShow = mMenuLayout.getVisibility();
					if (mMsgBox_sdcard != null) {
						if ((mMsgBox_sdcard.isShown())) {
							if (mMsgBox_sdcard != null) {
								mMsgBox_sdcard.hide();
							}

							return true;
						}
					}
					if (isMsgboxShow) {
						mMsgBox.hide();
						return true;
					} else {
						if (isSubListViewShow == View.VISIBLE) {
							mSubListView.setVisibility(View.GONE);
							mSubMenuTitle.setVisibility(View.GONE);
							if (mExposureSeekBar.getVisibility() == View.VISIBLE) {
								mExposureIV[0].setVisibility(View.GONE);
								mExposureIV[1].setVisibility(View.GONE);
								mExposureIV[2].setVisibility(View.GONE);
								mExposureSeekBar.setVisibility(View.GONE);
							}

							// 子菜单退出时，主菜单绿色字体复原
							LISTPOSITION = -1;
							mMainListViewAdapter.notifyDataSetChanged();
							return true;
						} else {
							if (isMainListViewShow == View.VISIBLE) {
								mMenuLayout.setVisibility(View.GONE);
								m_btn_camera_setting.setChecked(false);
								return true;
							}
						}
					}
					if (camera_state == camera_record) {
						if (!isRecording) {
							m_btn_camera_captureorrecord
									.setCheckedWithCallback(true);
						} else {
							m_btn_camera_capture.performClick();
						}
						return true;
					}
					if (camera_state == camera_captue) {
						// setScreenDimension(false);
						finish();
						// android.os.Process.killProcess(android.os.Process
						// .myPid());
						return true;
					}
					// 在全景状态下退出
				} else if (isPanoramaMode) {// camera_state=camera_panoramic
					if (mThreadRunning) {
						OnCancelClicked();
						return true;
					}
					m_al_camera_overlayui.setVisibility(View.VISIBLE);
					// boolean isGridShow = StoredData.getBoolean(
					// StoredData.M_GRIDDISINFINDER, false);
					// if (isGridShow) {
					// mSubMenuStateMap.put(CAMERA_GRIDDISPLAY_START_ID, 0);
					// m_aux_camera_auxiliaryline.setVisibility(View.VISIBLE);
					//
					// m_aux_camera_auxiliaryline.setLineType(2);
					// } else {
					// mSubMenuStateMap.put(CAMERA_GRIDDISPLAY_START_ID, 1);
					m_aux_camera_auxiliaryline.setVisibility(View.GONE);
					// // m_aux_camera_auxiliaryline.setLineType(0);
					// }

					if (mCaptureState == CAPTURE_STATE_MOSAIC) {
						m_btn_camera_capture
								.setBackgroundResource(R.drawable.capture_button);
						stopCapture(true);
						onBackgroundThreadFinished();
						reset();
						startCameraPreview();
						resetPanoUI();
						panoramaToCamera();
						return true;
					} else {
						clearMosaicFrameProcessorIfNeeded();
						isPanoramaMode = false;
					}

					// m_panorama_relativelayout.setVisibility(View.GONE);
					// clearMosaicFrameProcessorIfNeeded();
					// isPanoramaMode = false;

				}
			}
			if (camera_state == camera_panoramic) {
				finish();
				// onDestroy();
				// setScreenDimension(false);

			}
			break;

		default:
			break;
		}
		return super.onKeyUp(keyCode, event);

	}

	/**
	 * 从全景界面到普通界面的控件切换
	 */
	public void panoramaToCamera() {

		m_btn_camera_capture.setBackgroundResource(R.drawable.capture_button);

		m_btn_panorama_sparepower.setVisibility(View.GONE);
		m_btn_camera_capture.setChecked(false);
		for (int i = 0; i < m_camera_overlayui_array.length; i++) {
			m_camera_overlayui_array[i].setAlpha(1);
			m_camera_overlayui_array[i].setVisibility(View.VISIBLE);
		}
		m_camera_slide_bg_camera_icon.setAlpha(255);
		m_camera_slide_bg_record_icon.setAlpha(255);
		m_camera_slide_bg_2d_icon.setAlpha(255);
		m_camera_slide_bg_3d_icon.setAlpha(255);
		m_camera_slide_bg_camera_icon.setVisibility(View.VISIBLE);
		m_camera_slide_bg_record_icon.setVisibility(View.VISIBLE);
		m_camera_slide_bg_2d_icon.setVisibility(View.VISIBLE);
		m_camera_slide_bg_3d_icon.setVisibility(View.VISIBLE);
	}

	public void resetPanoUI() {
		resetPanoProgressToInit();
		mCaptureState = CAPTURE_STATE_VIEWFINDER;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		// boolean isdis = mMenuLayout.dispatchTouchEvent(ev);
		if (mMsgBox != null) {
			if (mMsgBox.isShown()) {
				return mMsgBox.dispatchTouchEvent(ev);
			}
		}
		if (mMsgBox_Panoramacancel != null) {
			if (mMsgBox_Panoramacancel.isShown()) {
				return mMsgBox_Panoramacancel.dispatchTouchEvent(ev);
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 单拍
	 */
	public void sinagleShot() {

		// boolean is3D = CSStaticData.g_is_3D_mode;
		// 在3D状态下，不能照相/录像
		boolean is3D = StoredData.getBoolean(StoredData.M_CAMERA3D, false);
		if (mCurrentDegree == 90 || mCurrentDegree == 270) {
			if (is3D) {
				return;
			}
		}
		isContinus = true;

		videoSurfaceView.setSelfTime();

		// m_btn_camera_continuous.setEnabled(false);
		// m_btn_camera_captureorrecord.setEnabled(false);
		// for (int i = 0; i < m_camera_overlayui_array.length; i++) {
		// m_camera_overlayui_array[i].setEnabled(false);
		// m_camera_overlayui_array[i].setClickable(false);
		// }
		// m_skb_camera_zoom_size.setEnabled(false);

		mMenuLayout.setVisibility(View.GONE);
		m_btn_camera_setting.setChecked(false);
		// videoSurfaceView.setSelfTime();

		boolean m_isReviews = StoredData.getBoolean(StoredData.M_REVIEW, true);
		System.out.println("是否预览" + m_isReviews);
		// 预览
		if (m_isReviews) {
			mMenuLayout.setVisibility(View.GONE);
			m_btn_camera_review_play_video.setVisibility(View.GONE);
			m_btn_camera_review_play_video.setAlpha(0);
			btn_camera_review_play_videoparent.setVisibility(View.GONE);
			m_isReview = true;
			if (mCurrentDegree == 90) {
				mOrientationEventListener.setRate(false);
				mOrientationEventListener.reviewRorate(false, a + 90, start);
				M_ISLANSCAPE = false;
			} else {
				// mOrientationEventListener.setRate(true);
				// mOrientationEventListener.reviewRorate(true);
				M_ISLANSCAPE = true;
			}
		}
	}

	/**
	 * 准备录像
	 */
	public void toStartRecord() {
		isRecording = true;

		// 在3D状态下，不能照相/录像
		// boolean is3D = CSStaticData.g_is_3D_mode;
		boolean is3D = StoredData.getBoolean(StoredData.M_CAMERA3D, false);
		if (mCurrentDegree == 90 || mCurrentDegree == 270) {
			if (is3D) {
				return;
			}
		}

		showRecordingUI(true);
		recordRotateDegree(mCurrentDegree, true);
		m_isRecord = true;
		// m_btn_camera_capture.setText("停止");
		m_btn_camera_capture
				.setBackgroundResource(R.drawable.record_stop_btn_selecter);
		timeCounter.start();
		// videoSurfaceView.setFocusMode(2);
		videoSurfaceView.startRecord();

	}

	/**
	 * 连拍
	 */
	public void continueShot() {
		isContinus = true;

		boolean is3Ds = StoredData.getBoolean(StoredData.M_CAMERA3D, false);
		if (mCurrentDegree == 90 || mCurrentDegree == 270) {
			if (is3Ds) {
				return;
			}
		}
		boolean m_isReviews = StoredData.getBoolean(StoredData.M_REVIEW, true);
		if (m_isReviews) {
			m_isReview = true;
		}
		mMenuLayout.setVisibility(View.GONE);
		m_btn_camera_setting.setChecked(false);
		videoSurfaceView.continueCapture();

		// 预览
		if (m_isReviews) {
			mMenuLayout.setVisibility(View.GONE);
			m_btn_camera_review_play_video.setVisibility(View.GONE);
			m_btn_camera_review_play_video.setAlpha(0);
			btn_camera_review_play_videoparent.setVisibility(View.GONE);
			m_isReview = true;
			if (mCurrentDegree == 90) {
				mOrientationEventListener.setRate(false);
				mOrientationEventListener.reviewRorate(false, a + 90, start);
				M_ISLANSCAPE = false;
			} else {
				M_ISLANSCAPE = true;
			}
		}
	}

	/**
	 * 在capture key down时准备拍照
	 */
	public void cameraUp() {
		Util.CAMERA_STATE = videoSurfaceView.CAMERA_SINGLE_SHOT;
		WiCameraActivity.isContinus = true;

		m_btn_camera_continuous.setEnabled(false);
		m_btn_camera_captureorrecord.setEnabled(false);
		for (int i = 0; i < m_camera_overlayui_array.length; i++) {
			m_camera_overlayui_array[i].setEnabled(false);
		}
		m_skb_camera_zoom_size.setEnabled(false);

		int cameraSelfTime = StoredData.getInt(StoredData.M_SELFTIMER, 0);

		boolean is3D = StoredData.getBoolean(StoredData.M_CAMERA3D, false);
		if (mCurrentDegree == 90 || mCurrentDegree == 270) {
			if (is3D) {
				return;
			}
		}
		isContinus = true;

		m_btn_camera_continuous.setEnabled(false);
		m_btn_camera_captureorrecord.setEnabled(false);
		for (int i = 0; i < m_camera_overlayui_array.length; i++) {
			m_camera_overlayui_array[i].setEnabled(false);
		}
		m_skb_camera_zoom_size.setEnabled(false);

		mMenuLayout.setVisibility(View.GONE);
		m_btn_camera_setting.setChecked(false);
		videoSurfaceView.setSelfTimes();
		boolean m_isReviews = StoredData.getBoolean(StoredData.M_REVIEW, true);
		System.out.println("是否预览" + m_isReviews);
		// 预览
		if (m_isReviews) {
			mMenuLayout.setVisibility(View.GONE);
			m_btn_camera_review_play_video.setVisibility(View.GONE);
			m_btn_camera_review_play_video.setAlpha(0);
			btn_camera_review_play_videoparent.setVisibility(View.GONE);
			m_isReview = true;
			if (mCurrentDegree == 90) {
				mOrientationEventListener.setRate(false);
				mOrientationEventListener.reviewRorate(false, a + 90, start);
				M_ISLANSCAPE = false;
			} else {
				M_ISLANSCAPE = true;
			}
		}
	}

	/**
	 * 按钮单击事件的注册
	 */
	public OnClickListener myListener = new OnClickListener() {
		int i = 0;
		int j = 0;
		int k = 0;

		public void onClick(View v) {
			if (v.getId() != R.id.btn_camera_setting) {
				// 菜单隐藏
				if (!mMsgBox.isShown()) {
					mMenuLayout.setVisibility(View.GONE);
					m_btn_camera_setting.setChecked(false);
					// mMenuLayout.setBackgroundColor(Color.RED);
				}
			}
			switch (v.getId()) {
			case R.id.btn_camera_sparepower:
			case R.id.btn_camera_storagemode:
			case R.id.btn_camera_scence:
			case R.id.btn_camera_newpic_sizeOfsum:
			case R.id.btn_self_timer:
			case R.id.btn_camera_uiright_bg:
				if (!mMsgBox.isShown()) {
					mMenuLayout.setVisibility(View.GONE);
					m_btn_camera_setting.setChecked(false);
					// mMenuLayout.setBackgroundColor(Color.RED);
				}

				break;
			case R.id.btn_camera_capture:
				// videoSurfaceView.cancalAutoFocus();
				// 防止双击
				Handler handle = null;
				// doubleClick(handle, m_btn_camera_capture, 2000);
				// boolean is3D = CSStaticData.g_is_3D_mode;
				// 在3D状态下，不能照相/录像
				boolean is3D = StoredData.getBoolean(StoredData.M_CAMERA3D,
						false);
				if (mCurrentDegree == 90 || mCurrentDegree == 270) {
					if (is3D) {
						m_btn_camera_capture.setChecked(false);
						return;
					}
				}

				mMenuLayout.setVisibility(View.GONE);
				m_btn_camera_setting.setChecked(false);

				if (camera_state == camera_record) {

					if (j % 2 == 0) {

						Util.CAMERA_STATE = InterSurfaceView.CAMERA_VIDEO;

						int videoSelfTime = StoredData.getInt(
								StoredData.M_VIDEO_SELFTIMER, 0);
						if (videoSelfTime == 0) {

							for (int i = 0; i < m_camera_overlayui_array.length; i++) {
								m_camera_overlayui_array[i].setEnabled(false);
								m_camera_overlayui_array[i].setClickable(false);
								m_camera_overlayui_array[i].setFocusable(false);
							}
							// m_btn_camera_capture.setEnabled(true);
							// m_btn_camera_capture.setClickable(true);
							Handler hans = null;
							doubleClick(hans, m_btn_camera_capture, 1500);

							toStartRecord();
						} else {
							for (int i = 0; i < m_camera_overlayui_array.length; i++) {
								m_camera_overlayui_array[i].setEnabled(false);
								m_camera_overlayui_array[i].setClickable(false);
								m_camera_overlayui_array[i].setFocusable(false);
							}
							// m_btn_camera_capture.setEnabled(true);
							// m_btn_camera_capture.setClickable(true);
							Handler hans = null;
							doubleClick(
									hans,
									m_btn_camera_capture,
									videoSurfaceView.VIDEO_SELFTIME[videoSelfTime] + 1500);

							// 设置开始计时时间
							m_ch_camera_time_count1.setBase(SystemClock
									.elapsedRealtime());
							// 启动计时器
							m_ch_camera_time_count1.start();
						}
					} else {
						showRecordingUI(false);
						m_isRecord = false;
						// m_btn_camera_capture.setText("录制");
						m_btn_camera_capture
								.setBackgroundResource(R.drawable.record_on_btn_selecter);
						// recordRotateDegree(mCurrentDegree, false);
						timeCounter.stop();
						videoSurfaceView.stopRecord();
						recordRotateDegree(mCurrentDegree, false);

						for (int i = 0; i < m_camera_overlayui_array.length; i++) {
							if (i != 10) {
								m_camera_overlayui_array[i].setEnabled(true);
								m_camera_overlayui_array[i].setClickable(true);
								m_camera_overlayui_array[i].setFocusable(true);
							}
						}

						doubleClick(handle, m_btn_camera_capture, 1000);
						// 设定缩略图
						Bitmap bitvideo1 = OperationFile.getVideotThumbnail(
								videoSurfaceView.getVideoPath(), 90, 90);
						System.out.println("录制视频路径"
								+ videoSurfaceView.getVideoPath());
						m_iv_camera_newpic_thumbnails.setImageBitmap(bitvideo1);
						m_iv_camera_newpic_thumbnails.setAlpha(255);
						m_iv_camera_newpic_thumbnails
								.setVisibility(View.VISIBLE);
						newpic_thumbnails_parent.setVisibility(View.VISIBLE);
						boolean m_isReviews = StoredData.getBoolean(
								StoredData.M_REVIEW, true);
						if (m_isReviews) {
							int videoSelfTime = StoredData.getInt(
									StoredData.M_VIDEO_SELFTIMER, 0);
							if (!isRecording) {
								if (videoSelfTime != 0) {
									m_ch_camera_time_count1.setBase(SystemClock
											.elapsedRealtime());
									m_ch_camera_time_count1.stop();
									videoSurfaceView.soundPlayerRelease();
								}
							} else {

								mMenuLayout.setVisibility(View.GONE);
								m_btn_camera_review_play_video
										.setBackgroundResource(R.drawable.video_review_icon);
								m_btn_camera_review_play_video
										.setVisibility(View.VISIBLE);
								m_btn_camera_review_play_video.setAlpha(255);
								btn_camera_review_play_videoparent
										.setVisibility(View.VISIBLE);
								Bitmap bit;
								m_isReview = true;
								if (mCurrentDegree == 90) {
									bit = OperationFile.getVideotThumbnail(
											videoSurfaceView.getVideoPath(),
											screenHeight / 2, screenWidth / 2);

									M_ISLANSCAPE = false;
									m_iv_camera_review_thumbnails
											.setImageBitmap(bit);
									// m_iv_camera_review_thumbnails.setRotation(0);
									mOrientationEventListener.setRate(false);
									mOrientationEventListener.reviewRorate(
											false, a + 90, start);
								} else {
									bit = OperationFile.getVideotThumbnail(
											VideoSurfaceView.M_VIDEOPATH,
											screenWidth / 2, screenHeight / 2);
									m_iv_camera_review_thumbnails
											.setImageBitmap(bit);
									M_ISLANSCAPE = true;
								}
								//

								isShowReview(true);
							}
						}
						isRecording = false;
						// else{
						// m_btn_camera_captureorrecord.setCheckedWithCallback(true);
						// }
					}
					AnimationSet as = null;
					for (int i = 0; i < m_camera_overlayui_array.length; i++) {
						if (i >= 4 && i <= 8) {
							as = new AnimationSet(true);
							Animation rAnimation = new RotateAnimation(start,
									start, Animation.RELATIVE_TO_SELF, 0.5f,
									Animation.RELATIVE_TO_SELF, 0.5f);
							rAnimation.setDuration(400);
							rAnimation.setFillAfter(true);
							AlphaAnimation al = new AlphaAnimation(0, 1);
							al.setDuration(2000);
							al.setFillAfter(true);
							as.addAnimation(al);
							as.addAnimation(rAnimation);
							as.setFillAfter(true);
							m_camera_overlayui_array[i].startAnimation(as);
							m_camera_overlayui_array[i].invalidate();
						}

					}
					++j;

				} else if (camera_state == camera_captue) {
					Util.CAMERA_STATE = videoSurfaceView.CAMERA_SINGLE_SHOT;
					WiCameraActivity.isContinus = true;

					m_btn_camera_continuous.setEnabled(false);
					m_btn_camera_captureorrecord.setEnabled(false);
					for (int i = 0; i < m_camera_overlayui_array.length; i++) {
						m_camera_overlayui_array[i].setEnabled(false);
						m_camera_overlayui_array[i].setClickable(false);
						m_camera_overlayui_array[i].setFocusable(false);
					}
					m_skb_camera_zoom_size.setEnabled(false);

					int cameraSelfTime = StoredData.getInt(
							StoredData.M_SELFTIMER, 0);
					if (cameraSelfTime == 0) {

						sinagleShot();
					} else {
						// 设置开始计时时间
						m_ch_camera_time_count1.setBase(SystemClock
								.elapsedRealtime());
						// 启动计时器
						m_ch_camera_time_count1.start();
					}
				} else if (camera_state == camera_continus) {
					// if (m_btn_camera_capture.isChecked()) {
					// isToStopContinus = false;
					System.out.println("开始连拍");
					isContinus = true;

					m_btn_camera_capture.setEnabled(false);
					m_btn_camera_captureorrecord.setEnabled(false);
					for (int i = 0; i < m_camera_overlayui_array.length; i++) {
						m_camera_overlayui_array[i].postInvalidate();
						m_camera_overlayui_array[i].setEnabled(false);
						m_camera_overlayui_array[i].setClickable(false);
						m_camera_overlayui_array[i].setFocusable(false);
					}
					m_skb_camera_zoom_size.setEnabled(false);

					Util.CAMERA_STATE = videoSurfaceView.CAMERA_CONTINUOUSANDSELFTIME;
					int cameraSelfTime = StoredData.getInt(
							StoredData.M_SELFTIMER, 0);
					if (cameraSelfTime == 0) {
						continueShot();
					} else {
						// 设置开始计时时间
						m_ch_camera_time_count1.setBase(SystemClock
								.elapsedRealtime());
						// 启动计时器
						m_ch_camera_time_count1.start();
					}
				} else if (camera_state == camera_panoramic) {

					if (m_btn_camera_capture.isChecked()) {

						m_btn_camera_dimension.setEnabled(false); // 防止双击
						m_btn_camera_dimension.setFocusable(false);
						m_btn_camera_dimension.setClickable(false);

						m_aux_camera_auxiliaryline.setVisibility(View.GONE);

						for (int i = 0; i < m_camera_overlayui_array.length; i++) {
							m_camera_overlayui_array[i].setAlpha(0);
							m_camera_overlayui_array[i]
									.setVisibility(View.GONE);
						}
						m_btn_camera_capture.setVisibility(View.VISIBLE);
						m_btn_camera_capture.setAlpha(1);

						m_camera_slide_bg_camera_icon.setAlpha(0);
						m_camera_slide_bg_record_icon.setAlpha(0);
						m_camera_slide_bg_2d_icon.setAlpha(0);
						m_camera_slide_bg_3d_icon.setAlpha(0);
						m_camera_slide_bg_camera_icon.setVisibility(View.GONE);
						m_camera_slide_bg_record_icon.setVisibility(View.GONE);
						m_camera_slide_bg_2d_icon.setVisibility(View.GONE);
						m_camera_slide_bg_3d_icon.setVisibility(View.GONE);
						// 开始全景拍摄
						// 1.初始化相机参数
						// 2.初始化全景参数

						m_panorama_relativelayout.setVisibility(View.VISIBLE);

						if (mThreadRunning) {
							// mAlertDialog.show();
							return;
						}
						switch (mCaptureState) {
						case CAPTURE_STATE_VIEWFINDER:
							m_btn_camera_capture
									.setBackgroundResource(R.drawable.panorama_stop);
							startCapture();
							break;
						case CAPTURE_STATE_MOSAIC:
							m_btn_camera_capture
									.setBackgroundResource(R.drawable.panorama_stop_click);
							stopCapture(false);
							break;
						}

					} else {
						if (mThreadRunning) {
							// mAlertDialog.show();
							return;
						}
						switch (mCaptureState) {
						case CAPTURE_STATE_VIEWFINDER:
							m_btn_camera_capture
									.setBackgroundResource(R.drawable.panorama_stop);
							startCapture();
							break;
						case CAPTURE_STATE_MOSAIC:
							m_btn_camera_capture
									.setBackgroundResource(R.drawable.panorama_stop_click);
							stopCapture(false);
							break;
						}
					}
				}
				break;
			case R.id.btn_camera_continuous:
				Log.e(TAG, "btn_camera_continuous连拍按钮点中了");

				if (m_btn_camera_continuous.isChecked()) {
					m_btn_camera_panoramic.setChecked(false);

					// videoSurfaceView.isSingleMode(false);
					camera_state = camera_continus;
					Util.CAMERA_STATE = videoSurfaceView.CAMERA_CONTINUOUSANDSELFTIME;
					m_btn_camera_capture
							.setBackgroundResource(R.drawable.capture_burst_button_selecter);
					m_btn_camera_capture.setChecked(false);
					if (m_panorama_relativelayout.getVisibility() == View.VISIBLE) {
						m_panorama_relativelayout.setVisibility(View.GONE);
					}

					// m_btn_camera_panoramic.setChecked(false);
					if (isPanoramaMode) {
						Message msg = new Message();
						msg.what = 200;
						msg.arg1 = 2;// 2d
						m_main_handle.sendMessage(msg);
						recylePanorama(true);
						isPanoramaMode = false;
					}

				} else {
					camera_state = camera_captue;
					m_btn_camera_capture
							.setBackgroundResource(R.drawable.capture_button_selecter);
					Util.CAMERA_STATE = videoSurfaceView.CAMERA_SINGLE_SHOT;

				}
				break;
			case R.id.btn_camera_review_tocamera:
				ThreeButtonToggle(m_btn_camera_review_tocamera);
				isShowReview(false);
				m_isReview = false;
				m_btn_camera_capture.setChecked(false);
				m_iv_camera_review_thumbnails.clearAnimation();
				if (camera_state == camera_panoramic) {
					for (int i = 0; i < m_camera_overlayui_array.length; i++) {
						m_camera_overlayui_array[i].setAlpha(1);
						m_camera_overlayui_array[i].setVisibility(View.VISIBLE);

					}
					m_camera_slide_bg_camera_icon.setAlpha(255);
					m_camera_slide_bg_record_icon.setAlpha(255);
					m_camera_slide_bg_2d_icon.setAlpha(255);
					m_camera_slide_bg_3d_icon.setAlpha(255);
					m_camera_slide_bg_camera_icon.setVisibility(View.VISIBLE);
					m_camera_slide_bg_record_icon.setVisibility(View.VISIBLE);
					m_camera_slide_bg_2d_icon.setVisibility(View.VISIBLE);
					m_camera_slide_bg_3d_icon.setVisibility(View.VISIBLE);
					m_btn_camera_capture
							.setBackgroundResource(R.drawable.capture_button);
					m_aux_camera_auxiliaryline.setVisibility(View.GONE);
					videoSurfaceView.stopPreview();
					videoSurfaceView.startPreview();
					return;
				}
				break;

			case R.id.btn_camera_review_delete:

				if (mShareMenu.isShown()) {
					mShareMenu.hide();
					m_btn_camera_review_share.setChecked(false);
				}
				// m_btn_camera_review_delete.setChecked(true);
				boolean is3Dd = StoredData.getBoolean(StoredData.M_CAMERA3D,
						false);
				if (is3Dd) {
					mMsgBox_delete.setRotation(-360);
				} else {
					mMsgBox_delete.setRotation(-ldegree);
				}
				if (m_btn_camera_review_delete.isChecked()) {
					m_btn_camera_review_tocamera.setVisibility(View.GONE);
					m_btn_camera_review_tocamera.setAlpha(0);
					m_btn_camera_review_tocamera.setClickable(false);
					mMsgBox_delete.show();
				} else {
					m_btn_camera_review_tocamera.setVisibility(View.VISIBLE);
					m_btn_camera_review_tocamera.setAlpha(1);
					m_btn_camera_review_tocamera.setClickable(true);
					mMsgBox_delete.hide();

				}

				ThreeButtonToggle(m_btn_camera_review_delete);
				break;
			case R.id.btn_camera_setting:
				if (camera_state == camera_panoramic) {
					return;
				}
				boolean isChecked = m_btn_camera_setting.isChecked();
				if (isChecked) {
					// if (mExposureSeekBar.getVisibility() == View.VISIBLE) {
					mExposureSeekBar.setVisibility(View.GONE);
					mExposureIV[0].setVisibility(View.GONE);
					mExposureIV[1].setVisibility(View.GONE);
					mExposureIV[2].setVisibility(View.GONE);
					// }
					mMenuLayout.setVisibility(View.VISIBLE);
					LISTPOSITION = -1;
					mListView.setAdapter(mMainListViewAdapter);
				} else {
					mMenuLayout.setVisibility(View.GONE);
				}
				mSubListView.setVisibility(View.GONE);
				mSubMenuTitle.setVisibility(View.GONE);
				break;
			case R.id.btn_camera_review_share:
				if (mShareMenu == null) {

				} else {
					String sharePath = "";
					ArrayList<String> filepath = new ArrayList<String>();
					sharePath = "file:" + m_bitmappicker.getCurrentFileName();
					System.out.println("视频分享");
					filepath.add(sharePath);
					mShareMenu.setImagePath(filepath);
					if (mShareMenu.isShown()) {
						mShareMenu.hide();
						m_btn_camera_review_share.setChecked(false);
					} else {
						mShareMenu.show();
						m_btn_camera_review_share.setChecked(true);
					}
					// m_btn_camera_review_information.setChecked(false);
					ThreeButtonToggle(m_btn_camera_review_share);
				}
				break;
			case R.id.btn_camera_flashmode:
				int flashId = StoredData.getInt(StoredData.M_FLASHMODE, 0);
				// off to on
				if (flashId == 2) {
					m_btn_camera_flashmode
							.setBackgroundResource(R.drawable.camera_flash_btn_autotoon_selecter);
					StoredData.saveInt(StoredData.M_FLASHMODE, 0);// auto
					videoSurfaceView.setFlashMode(0);
				}
				// auto to off
				if (flashId == 0) {
					m_btn_camera_flashmode
							.setBackgroundResource(R.drawable.camera_flash_btn_ontooff_selecter);
					StoredData.saveInt(StoredData.M_FLASHMODE, 1);// on
					videoSurfaceView.setFlashMode(1);
				}
				// on to auto
				if (flashId == 1) {

					m_btn_camera_flashmode
							.setBackgroundResource(R.drawable.camera_flash_btn_offtoauto_selecter);
					StoredData.saveInt(StoredData.M_FLASHMODE, 2);// off
					videoSurfaceView.setFlashMode(2);
				}
				break;
			case R.id.btn_camera_used_camera:
				// 防止双击
				Handler handles = null;
				doubleClick(handles, m_btn_camera_used_camera, 1500);
				int mNumberOfCameras = android.hardware.Camera
						.getNumberOfCameras();
				System.out.println("照相头的个数:" + mNumberOfCameras);
				if (mNumberOfCameras <= 1) {
					return;
				}
				// 在3D状态下，不能照相/录像
				boolean is3Ds = StoredData.getBoolean(StoredData.M_CAMERA3D,
						false);
				// boolean is3Ds = CSStaticData.g_is_3D_mode;
				// if (mCurrentDegree == 90 || mCurrentDegree == 270) {
				if (is3Ds) {
					return;
				}
				// }
				if (camera_state == camera_panoramic) {
					return;
				}
				for (int i = 0; i < m_camera_overlayui_array.length; i++) {
					m_camera_overlayui_array[i].setEnabled(false);
				}
				videoSurfaceView.cancalAutoFocus();
				
				isRear = videoSurfaceView.switchCamera();
				if(!isRear){
					add3DView(false);
				}else{
					add3DView(true);
				}
				for (int i = 0; i < m_camera_overlayui_array.length; i++) {
					m_camera_overlayui_array[i].setEnabled(true);
				}
				if (!isRear) {
					m_btn_camera_dimension.setEnabled(false);
					m_btn_camera_dimension.setFocusable(false);
					m_btn_camera_dimension.setClickable(false);
					m_btn_camera_panoramic.setEnabled(false);
					m_btn_camera_panoramic.setClickable(false);
					m_btn_camera_panoramic.setFocusable(false);
					return;
				} else {
					m_btn_camera_dimension.setEnabled(true);
					m_btn_camera_dimension.setClickable(true);
					m_btn_camera_dimension.setFocusable(true);
					m_btn_camera_panoramic.setEnabled(true);
					m_btn_camera_panoramic.setClickable(true);
					m_btn_camera_panoramic.setFocusable(true);
				}
				break;
			case R.id.btn_camera_panoramic:
				if (m_btn_camera_panoramic.isChecked()) {
					if (m_btn_camera_dimension.getChecked()) {
						Message msg = new Message();
						msg.what = 200;
						msg.arg1 = 1;// 3d

						m_main_handle.sendMessage(msg);
					}
					preparePanoramic();
				} else {
					if (m_btn_camera_dimension.getChecked()) {
						Message msg = new Message();
						msg.what = 200;
						msg.arg1 = 2;// 2d
						m_main_handle.sendMessage(msg);
						//
					}
					recylePanorama(true);

					boolean isShowGrid = StoredData.getBoolean(
							StoredData.M_GRIDDISINFINDER, false);
					if (isShowGrid) {
						m_aux_camera_auxiliaryline.setVisibility(View.VISIBLE);
					}
					// videoSurfaceView.isSingleMode(true);
				}
				break;
			case R.id.iv_camera_newpic_thumbnails:
				videoSurfaceView.cancalAutoFocus();
				// 得到list，并反转
				picList = new ArrayList<String>();
				videoList = new ArrayList<String>();

				Intent intent = null;
				String path = StoredData.getString(StoredData.M_REVIEWFILEPATH,
						"a");

				if (path.equals("a") || (!new File(path).exists())) {
					return;
				} else {

					if (FileTypeHelper.isImageFile(path)) {
						if (videoSurfaceView.getPicFileList().size() == 0) {
							picList.add(path);
						} else {
							if (videoSurfaceView.getPicFileList() != null) {
								for (int i = videoSurfaceView.getPicFileList()
										.size() - 1; i >= 0; i--) {
									picList.add(videoSurfaceView
											.getPicFileList().get(i));
								}
							}
						}
						boolean ispano = isPanoramaImage(picList.get(0));
						if (ispano) {
							callPanoramaViewer(picList.get(0), 5004);
						} else {
							intent = new Intent(WiCameraActivity.this,
									WiImageViewerActivity.class);
							intent.putExtra("fileList", picList);
							intent.putExtra("filePath", picList.get(0));
							intent.putExtra("cmd", "play");
							startActivity(intent);
						}
						// 视频
					} else {
						videoList.add(path);

						Intent intent1 = new Intent(WiCameraActivity.this,
								WiVideoViewerActivity.class);
						intent1.putExtra("filePath", videoList.get(0));
						intent1.putStringArrayListExtra("fileList", videoList);
						intent1.putExtra("cmd", "play");
						startActivity(intent1);
					}
				}

				// WiCameraActivity.this.finish();
				break;
			case R.id.btn_camera_review_information:
				SpannableString[] string = null;
				camera_mMsgBox_info = new FileInfoMsgBox(WiCameraActivity.this);
				camera_mMsgBox_info.setTitle("Details");
				camera_mMsgBox_info.setClickBlankHide(true);
				camera_mMsgBox_info.setMode(false);
				// 视频
				if (Util.CAMERA_STATE == videoSurfaceView.CAMERA_VIDEO) {
					VideoProvider m_videoProvider = new VideoProvider(
							WiCameraActivity.this);
					VideoInfo m_videoinfo = m_videoProvider
							.getVideoInfo(m_bitmappicker.getCurrentFileName());

					if (m_videoinfo == null) {
						camera_mMsgBox_info.setMessage("Fail to get info!");
					} else {
						string = m_videoinfo.showInfoList();
						camera_mMsgBox_info.setMessage(string);
					}
					// 图片
				} else {
					if (Util.CAMERA_STATE == InterSurfaceView.CAMERA_CONTINUOUSANDSELFTIME) {
						ImageInfo m_imaImageInfo = new ImageInfo(
								WiCameraActivity.this);
						ImageInfoBean m_ImageInfoBean = m_imaImageInfo
								.getImagesInfo(m_bitmappicker
										.getCurrentFileName());
						if (m_ImageInfoBean != null) {
							string = m_ImageInfoBean.showInfoList();
							camera_mMsgBox_info.setMessage(string);
						} else {
							camera_mMsgBox_info.setMessage("Fail to get info!");
						}
						string = m_ImageInfoBean.showInfoList();
					} else {
						ImageInfo m_imaImageInfo = new ImageInfo(
								WiCameraActivity.this);
						ImageInfoBean m_ImageInfoBean = m_imaImageInfo
								.getImagesInfo(m_bitmappicker
										.getCurrentFileName());
						if (m_ImageInfoBean != null) {
							string = m_ImageInfoBean.showInfoList();
							camera_mMsgBox_info.setMessage(string);
						} else {
							camera_mMsgBox_info.setMessage("Fail to get info!");
						}
						System.out.println("string=="
								+ m_bitmappicker.getCurrentFileName());
					}

				}

				if (!m_btn_camera_review_information.isChecked()) {
					camera_mMsgBox_info.hideAndRemove();
					// m_btn_camera_review_information.setChecked(true);
					m_btn_camera_review_information.setChecked(false);
					camera_mMsgBox_info = null;
				} else {
					m_btn_camera_review_information.setChecked(true);
					camera_mMsgBox_info.setClickBlankHide(true);
					camera_mMsgBox_info.addToLayout(m_rl_camera_main_layout);
					boolean is3Di = StoredData.getBoolean(
							StoredData.M_CAMERA3D, false);
					if (is3Di) {
						if (camera_mMsgBox_info != null) {
							camera_mMsgBox_info.setRotation(-360);
							System.out.println("-ldegree=====" + ldegree);
							RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
									400, 650);
							layoutParams
									.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
							// layoutParams.setMargins(20, 80, 0, 20);
							layoutParams.topMargin = 100;
							layoutParams.rightMargin = 35;
							camera_mMsgBox_info.setLayoutParams(layoutParams);
							// camera_mMsgBox_info.invalidate();
						}
					} else {
						if (mCurrentDegree == 0 || mCurrentDegree == 360
								|| mCurrentDegree == 180) {
							if (camera_mMsgBox_info != null) {
								camera_mMsgBox_info.setRotation(-ldegree);
								System.out.println("-ldegree=====" + ldegree);
								RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
										400, 650);
								layoutParams
										.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
								// layoutParams.setMargins(20, 80, 0, 20);
								layoutParams.topMargin = 100;
								layoutParams.rightMargin = 35;
								camera_mMsgBox_info
										.setLayoutParams(layoutParams);
								// camera_mMsgBox_info.invalidate();
							}
						} else {
							if (camera_mMsgBox_info != null) {
								// camera_mMsgBox_info.setRotation(-ldegree);
								// 竖屏msgbox
								RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
										400, 650);
								layoutParams
										.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
								camera_mMsgBox_info
										.setLayoutParams(layoutParams);
								camera_mMsgBox_info.setRotation(-ldegree);
								camera_mMsgBox_info.setTranslationX(180);
								// camera_mMsgBox_info.invalidate();
							}
						}

					}
				}
				ThreeButtonToggle(m_btn_camera_review_information);
				break;
			case R.id.btn_camera_review_play_video:
				System.out.println("播放视频");
				ArrayList<String> mList = m_bitmappicker.getVideoList();
				Intent intent1 = new Intent(WiCameraActivity.this,
						WiVideoViewerActivity.class);
				intent1.putExtra("filePath",
						m_bitmappicker.getCurrentFileName());
				intent1.putStringArrayListExtra("fileList", mList);
				intent1.putExtra("cmd", "play");
				startActivity(intent1);
				// WiCameraActivity.this.onDestroy();
				break;
			default:

				break;
			}
		}
	};

	public void ThreeButtonToggle(View view) {
		switch (view.getId()) {
		case R.id.btn_camera_review_delete:
			m_btn_camera_review_share.setChecked(false);
			if (mShareMenu != null) {
				mShareMenu.hide();
			}
			m_btn_camera_review_information.setChecked(false);
			if (camera_mMsgBox_info != null) {
				camera_mMsgBox_info.hideAndRemove();
				// m_btn_camera_review_information.setChecked(false);
				camera_mMsgBox_info = null;
			}
			break;
		case R.id.btn_camera_review_share:
			m_btn_camera_review_delete.setChecked(false);
			mMsgBox_delete.hide();

			m_btn_camera_review_information.setChecked(false);
			if (camera_mMsgBox_info != null) {
				camera_mMsgBox_info.hideAndRemove();
				// m_btn_camera_review_information.setChecked(false);
				camera_mMsgBox_info = null;
			}
			m_btn_camera_review_tocamera.setVisibility(View.VISIBLE);
			m_btn_camera_review_tocamera.setAlpha(1);
			m_btn_camera_review_tocamera.setClickable(true);
			break;
		case R.id.btn_camera_review_information:
			m_btn_camera_review_delete.setChecked(false);
			mMsgBox_delete.hide();

			m_btn_camera_review_share.setChecked(false);
			mShareMenu.hide();

			m_btn_camera_review_tocamera.setVisibility(View.VISIBLE);
			m_btn_camera_review_tocamera.setAlpha(1);
			m_btn_camera_review_tocamera.setClickable(true);
			break;
		case R.id.btn_camera_review_tocamera:
			m_btn_camera_review_delete.setChecked(false);
			m_btn_camera_review_share.setChecked(false);
			m_btn_camera_review_information.setChecked(false);
			break;
		default:
			break;
		}
	}

	/**
	 * 退出全景拍摄，回收资源
	 * @param isUnableButtons
	 *            是否将按钮禁用
	 */
	public void recylePanorama(boolean isUnableButtons) {

		if (isUnableButtons) {
			for (int i = 0; i < WiCameraActivity.m_camera_overlayui_array.length; i++) {
				if (WiCameraActivity.m_camera_overlayui_array[i] != null) {
					WiCameraActivity.m_camera_overlayui_array[i]
							.setClickable(false);
					WiCameraActivity.m_camera_overlayui_array[i]
							.setEnabled(false);
				}
			}
		}
		m_btn_camera_setting.setEnabled(true);
		m_btn_camera_setting.setClickable(true);
		m_btn_camera_used_camera.setEnabled(true);
		m_btn_camera_used_camera.setClickable(true);
		Log.e(TAG, "设置按钮为不可用");
		m_btn_camera_capture
				.setBackgroundResource(R.drawable.capture_button_selecter);
		camera_state = camera_captue;
		Util.CAMERA_STATE = videoSurfaceView.CAMERA_SINGLE_SHOT;
		isPanoramaMode = false;
		m_panorama_relativelayout.setVisibility(View.GONE);

		if (mCaptureState == CAPTURE_STATE_MOSAIC) {
			stopCapture(true);
			onBackgroundThreadFinished();
			reset();
		}
		// startCameraPreview();
		m_panorama_relativelayout.setVisibility(View.GONE);
		clearMosaicFrameProcessorIfNeeded();
		isPanoramaMode = false;
		resetPanoUI();
		videoSurfaceView.setPreviewSize(800, 480);
		TsetScreenSize(true);
		videoSurfaceView.stopPreview();
		videoSurfaceView.startPreview();
	}

	/**
	 * 准备全景拍摄，分配内存
	 */
	public void preparePanoramic() {
		Log.e(Tag, "[preparePanoramic]");
		// 处理耗时，需要将其他按钮禁用
		for (int i = 0; i < WiCameraActivity.m_camera_overlayui_array.length; i++) {
			if (WiCameraActivity.m_camera_overlayui_array[i] != null) {
				WiCameraActivity.m_camera_overlayui_array[i]
						.setClickable(false);
				WiCameraActivity.m_camera_overlayui_array[i].setEnabled(false);
				WiCameraActivity.m_camera_overlayui_array[i]
						.setFocusable(false);
			}
		}
		Log.e(TAG, "[preparePanoramic]--------按钮置为不可用");
		m_iv_camera_focus.setVisibility(View.GONE);
		if (CSStaticData.DEBUG)
			Log.e(TAG, "点击初始化全景参数");
		isPanoramaMode = true;
		videoSurfaceView.stopPreview();
		videoSurfaceView.startPreview();
		List<Size> supportedSizes = videoSurfaceView.getSupportedPreviewSizes();
		if (!findBestPreviewSize(supportedSizes, true, true)) {
			Log.w(TAG, "No 4:3 ratio preview size supported.");
			if (!findBestPreviewSize(supportedSizes, false, true)) {
				Log.w(TAG,
						"Can't find a supported preview size smaller than 960x720.");
				findBestPreviewSize(supportedSizes, false, false);
			}
		}
		videoSurfaceView.setFocusMode(0);
		Log.e(TAG, "[findBestPreviewSize]-----------全景拍摄最佳预览大小为"
				+ mPreviewWidth + "*" + mPreviewHeight);
		mPreviewWidth = 640;
		mPreviewHeight = 480;
		videoSurfaceView.setPreviewSize(640, 480);
		videoSurfaceView.stopPreview();
		videoSurfaceView.startPreview();
		// }
		mHorizontalViewAngle = videoSurfaceView.getHorizontalViewAngle();
		if (mHorizontalViewAngle == 0) {
			mHorizontalViewAngle = 60;
		}
		// 暂时的解决方案
		mHorizontalViewAngle = 60;
		if (CSStaticData.DEBUG)
			Log.e(TAG, "[mHorizontalViewAngle]--->" + mHorizontalViewAngle);
		initMosaicFrameProcessorIfNeeded();
		camera_state = camera_panoramic;
		m_panorama_relativelayout.setVisibility(View.VISIBLE);
		// m_btn_camera_capture
		// .setBackgroundResource(R.drawable.panorama_selecter);
		m_btn_camera_capture.setBackgroundResource(R.drawable.capture_button);
		isPanoramaMode = true;

		m_btn_camera_continuous.setChecked(false);

		// m_iv_camera_focus.setVisibility(View.GONE);
		m_aux_camera_auxiliaryline.setVisibility(View.GONE);
		TsetScreenSize(false);

	}
/**
 * 设置预览显示区域大小
 * @param isFullScreen
 */
	private void TsetScreenSize(boolean isFullScreen) {
		LayoutParams mLayoutParams = (LayoutParams) new LayoutParams(10, 10);
		if (isFullScreen) {
			mLayoutParams.width = screenWidth + sbarh;
			mLayoutParams.height = screenHeight;
		} else {
			float rate_src = 4f / 3f;
			float rate_screen = (float) screenWidth / (float) screenHeight;
			if (rate_screen > rate_src) {
				mLayoutParams.width = (int) (screenHeight * rate_src);
				mLayoutParams.height = screenHeight;
			} else {
				mLayoutParams.width = screenWidth;
				mLayoutParams.height = (int) (screenWidth / rate_src);
			}
		}
		// mLayoutParams.width = 640;
		// mLayoutParams.height = 400;
		mLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		((View) videoSurfaceView).setLayoutParams(mLayoutParams);
	}

	/**
	 * ddd和普通surfaceview的切换
	 * 
	 * @param isTrue
	 *            是否为普通的ddd
	 */
	public void add3DView(boolean isTrue) {
		System.gc();
		try {

			for (int i = 0; i < m_camera_overlayui_array.length; i++) {
				m_camera_overlayui_array[i].setEnabled(false);
				m_camera_overlayui_array[i].setClickable(false);
				m_camera_overlayui_array[i].setFocusable(false);
			}
			isCameraOpen = false;
			if (videoSurfaceView != null) {
				videoSurfaceView.stopPreview();
				videoSurfaceView.releaseCamera();
			}
			if (isTrue) {

				if (videoSurfaceView != null) {
					m_al_surfaceview.removeView((View) videoSurfaceView);
					((View) videoSurfaceView).destroyDrawingCache();
					videoSurfaceView = null;
				}
				LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.FILL_PARENT);
				videoSurfaceView = new VideoSurfaceView(this, null);
				((View) videoSurfaceView).setLayoutParams(lp);

				m_al_surfaceview.addView((View) videoSurfaceView);
			} else {
				if (videoSurfaceView != null) {
					videoSurfaceView.onPause();
					m_al_surfaceview.removeView((View) videoSurfaceView);
					((View) videoSurfaceView).destroyDrawingCache();
					videoSurfaceView = null;
				}
				LayoutParams lp2 = new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.FILL_PARENT);
				videoSurfaceView = new VideoSurfaceView2D(this, null);
				((View) videoSurfaceView).setLayoutParams(lp2);

				m_al_surfaceview.addView((View) videoSurfaceView);
			}
			videoSurfaceView.setVideoFileList(VIDEO_FILE_LIST);
			videoSurfaceView.setPicFileList(PIC_FILE_LIST);
			videoSurfaceView.setSize(WiCameraActivity.screenWidth,
					WiCameraActivity.screenHeight);
			videoSurfaceView.is3D(CSStaticData.g_is_3D_mode);
			((View) videoSurfaceView).setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					if (!mMsgBox.isShown()) {
						mMenuLayout.setVisibility(View.GONE);
						m_btn_camera_setting.setChecked(false);
					}
					return false;
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
			for (int i = 0; i < m_camera_overlayui_array.length; i++) {
				m_camera_overlayui_array[i].setEnabled(true);
				m_camera_overlayui_array[i].setClickable(true);
				m_camera_overlayui_array[i].setFocusable(false);
			}
			if (!isRear) {
				m_btn_camera_dimension.setEnabled(false);
				m_btn_camera_panoramic.setEnabled(false);
			}
		} finally {
		}
	}

	OnSeekBarChangeListener mes = new OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			// progress = progress - 4;
			System.out.println("曝光度的进度为:" + progress);
			StoredData.saveInt(StoredData.M_EXPOSURE, progress);
			videoSurfaceView.setExposure();
		}
	};

	OnCheckedChangedListener onCheckedChangedListenerOfCameraOrVideo = new OnCheckedChangedListener() {

		@Override
		public void OnChecked(boolean isChecked) {
			videoSurfaceView.cancalAutoFocus();
			// 菜单隐藏
			if (!mMsgBox.isShown()) {
				mMenuLayout.setVisibility(View.GONE);
				m_btn_camera_setting.setChecked(false);
			}

			// TODO Auto-generated method stub
			mMenuLayout.setVisibility(View.GONE);
			mSubMenuTitle.setVisibility(View.GONE);
			m_btn_camera_setting.setChecked(false);
			if (!isChecked) {
				if (isPanoramaMode) {
					Message msg = new Message();
					msg.what = 200;
					msg.arg1 = 2;// 2d
					m_main_handle.sendMessage(msg);
					recylePanorama(true);
					isPanoramaMode = false;
				}

				boolean is3D = CSStaticData.g_is_3D_mode;
				Util.CAMERA_STATE = InterSurfaceView.CAMERA_VIDEO;
				if(!isRear){
//					add3DView(false);
				}else{
					add3DView(false);
				}
				//add3DView(false);
				m_btn_camera_dimension.setEnabled(false);

				StoredData.saveInt(StoredData.M_CAMERA_STATE,
						videoSurfaceView.CAMERA_VIDEO);
				if (m_btn_camera_panoramic.isChecked()) {
					m_btn_camera_panoramic.setChecked(false);
					recylePanorama(false);// 回收资源，修改屏幕尺寸
				}
				if (m_btn_camera_continuous.isChecked()) {
					m_btn_camera_continuous.setChecked(false);
				}
				camera_state = camera_record;

				mIsRecoderList = true;
				mIsCameraList = false;
				LISTPOSITION = -1;
				initVideoListView();
				initVideoListViewListener();
				IsShowCameraMiddleUI(false);
				if (!CSStaticData.g_is_3D_mode) {
					cameraRotateDegree(mCurrentDegree);
					recordRotateDegree(mCurrentDegree, false);
				} else {
					cameraRotateDegree(360);
					recordRotateDegree(360, false);
				}
				// recordRotateDegree(mCurrentDegree, false);
				camera_state = camera_record;
				// m_btn_camera_capture.setText("录像");
				Util.CAMERA_STATE = InterSurfaceView.CAMERA_VIDEO;
				m_btn_camera_capture
						.setBackgroundResource(R.drawable.record_on_btn_selecter);

				m_aux_camera_auxiliaryline.setVisibility(View.GONE);
				m_iv_camera_focus.setVisibility(View.GONE);

			} else {
				Util.CAMERA_STATE = InterSurfaceView.CAMERA_SINGLE_SHOT;
				if(!isRear){
//					add3DView(false);
				}else{
					add3DView(true);
				}
				//add3DView(true);
				m_btn_camera_dimension.setEnabled(true);

				StoredData.saveInt(StoredData.M_CAMERA_STATE,
						videoSurfaceView.CAMERA_SINGLE_SHOT);

				mIsCameraList = true;
				mIsRecoderList = false;
				LISTPOSITION = -1;
				initCameraListView();
				initCameraListViewListener();

				IsShowCameraMiddleUI(true);
				camera_state = camera_captue;
				// m_btn_camera_capture.setText("拍照");
				if (!CSStaticData.g_is_3D_mode) {
					recordRotateDegree(mCurrentDegree, false);
					cameraRotateDegree(mCurrentDegree);
				} else {
					recordRotateDegree(360, false);
					cameraRotateDegree(360);
				}
				Util.CAMERA_STATE = InterSurfaceView.CAMERA_SINGLE_SHOT;
				// videoSurfaceView.setFocusMode(1);
				m_btn_camera_capture
						.setBackgroundResource(R.drawable.capture_button_selecter);
				boolean isShowGrid = StoredData.getBoolean(
						StoredData.M_GRIDDISINFINDER, false);
				System.out.println("isShowGrid" + isShowGrid);
				if (isShowGrid) {
					m_aux_camera_auxiliaryline.setVisibility(View.VISIBLE);
				}

			}

			AnimationSet as = null;
			for (int i = 0; i < m_camera_overlayui_array.length; i++) {
				if (i >= 4 && i <= 8) {
					as = new AnimationSet(true);
					Animation rAnimation = new RotateAnimation(start, start,
							Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 0.5f);
					rAnimation.setDuration(400);
					rAnimation.setFillAfter(true);
					AlphaAnimation al = new AlphaAnimation(0, 1);
					al.setDuration(2000);
					al.setFillAfter(true);
					as.addAnimation(al);
					as.addAnimation(rAnimation);
					as.setFillAfter(true);
					m_camera_overlayui_array[i].startAnimation(as);
					m_camera_overlayui_array[i].invalidate();
				}

			}

		}
	};
	OnCheckedChangedListener onCheckedChangedListenerOf3D = new OnCheckedChangedListener() {

		@Override
		public void OnChecked(boolean isChecked) {
			System.out.println("onCheckedChangedListenerOf3D回调执行了");
			// TODO Auto-generated method stub
			m_btn_camera_dimension.setEnabled(false);
			m_main_handle.removeMessages(8000);
			m_main_handle.sendEmptyMessageDelayed(8000, 2000);
			// 菜单隐藏
			if (mMsgBox != null) {
				if (!mMsgBox.isShown()) {
					mMenuLayout.setVisibility(View.GONE);
					m_btn_camera_setting.setChecked(false);
					// mMenuLayout.setBackgroundColor(Color.RED);
				}
			}
			if (!isChecked) {
				// if(mCurrentDegree==90||mCurrentDegree==270){
				// m_btn_camera_continuous.setEnabled(false);
				// m_btn_camera_continuous.setClickable(false);}

				m_btn_camera_panoramic.setEnabled(false);
				m_btn_camera_panoramic.setFocusable(false);
				m_btn_camera_panoramic.setClickable(false);
				m_btn_camera_captureorrecord.setEnabled(false);
				m_btn_camera_captureorrecord.setFocusable(false);
				m_btn_camera_captureorrecord.setClickable(false);

				videoSurfaceView.is3D(true);
				StoredData.saveBoolean(StoredData.M_CAMERA3D, true);
				m_btn_camera_used_camera.setAlpha(0);
				m_btn_camera_used_camera.setEnabled(false);
				CSStaticData.g_is_3D_mode = true;

				// start = 0;
				// mPreOrientation = 270;
				// if(mCurrentDegree==90||mCurrentDegree==270){
				// a = getAngle(mCurrentDegree, 360);
				// mCurrentDegree = 0;

				if (!m_btn_camera_panoramic.isChecked()) {
					Message msg = new Message();
					msg.what = 200;
					msg.arg1 = 1;// 3d

					m_main_handle.sendMessage(msg);
				}
				// }
			} else {

				m_btn_camera_panoramic.setEnabled(true);
				m_btn_camera_panoramic.setFocusable(true);
				m_btn_camera_panoramic.setClickable(true);
				m_btn_camera_captureorrecord.setEnabled(true);
				m_btn_camera_captureorrecord.setFocusable(true);
				m_btn_camera_captureorrecord.setClickable(true);
				videoSurfaceView.is3D(false);
				StoredData.saveBoolean(StoredData.M_CAMERA3D, false);
				if (camera_state != camera_record) {
					m_btn_camera_used_camera.setAlpha(1);
					m_btn_camera_used_camera.setEnabled(true);
				}
				CSStaticData.g_is_3D_mode = false;
				if (!m_btn_camera_panoramic.isChecked()) {
					Message msg = new Message();
					msg.what = 200;
					msg.arg1 = 2;// 2d
					m_main_handle.sendMessage(msg);
				}
			}
			// m_btn_camera_dimension.setEnabled(false);
			// m_main_handle.sendEmptyMessageDelayed(800, 8000);
		}
	};
	OnTouchListener onTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case 3:
				if (!mMsgBox.isShown()) {
					mMenuLayout.setVisibility(View.GONE);
					m_btn_camera_setting.setChecked(false);
				}

				break;
			default:
				break;
			}

			return false;
		}
	};

	public void demon(boolean isCheck) {
		if (!isCheck) {

			m_btn_camera_panoramic.setEnabled(false);
			m_btn_camera_panoramic.setFocusable(false);
			m_btn_camera_panoramic.setClickable(false);
			m_btn_camera_captureorrecord.setEnabled(false);
			m_btn_camera_captureorrecord.setFocusable(false);
			m_btn_camera_captureorrecord.setClickable(false);

		} else {

			m_btn_camera_panoramic.setEnabled(true);
			m_btn_camera_panoramic.setFocusable(true);
			m_btn_camera_panoramic.setClickable(true);
			m_btn_camera_captureorrecord.setEnabled(true);
			m_btn_camera_captureorrecord.setFocusable(true);
			m_btn_camera_captureorrecord.setClickable(true);
		}
	}

	public ArcSeekBar.OnArcSeekBarChangeListener mySeekBarChangeListener = new ArcSeekBar.OnArcSeekBarChangeListener() {
		int pre = 0;

		@Override
		public void onStopTrackingTouch(ArcSeekBar seekBar) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStartTrackingTouch(ArcSeekBar seekBar) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProgressChanged(ArcSeekBar seekBar, int progress,
				boolean fromUser) {

			// TODO Auto-generated method stub
			if (pre != progress) {
				if (m_skb_camera_zoom_size != null) {

					mMenuLayout.setVisibility(View.GONE);
					m_btn_camera_setting.setChecked(false);
					// mMenuLayout.setBackgroundColor(Color.RED);

					mMenuLayout.setVisibility(View.GONE);
					m_btn_camera_setting.setChecked(false);
					videoSurfaceView.setZoomSize(progress);
					System.out.println("zoom size===" + progress + "fromUser="
							+ fromUser);
					pre = progress;
				}
			}
		}
	};

	public void updateReiewThumBnails() {
		if (m_bitmappicker == null) {
			return;
		}
		boolean nextorpre = true;
		if (m_bitmappicker.getCurrentFileIndex() == m_bitmappicker
				.getFileCounts() - 1) {
			nextorpre = false;
		}
		// 从链表中把改图片删除,对话框隐藏
		// m_bitmappicker.deleteFile();

		// mMsgBox_delete.hide();
		// m_btn_camera_review_delete.setChecked(false);
		// 删除完了
		if (m_bitmappicker.getCurrentFileIndex() == -1) {
			StoredData.saveString(StoredData.M_REVIEWFILEPATH, "a");
			// mMsgBox_delete.hide();
			// 回到照相界面

			m_isReview = false;
			isShowReview(false);

			if (camera_state == camera_panoramic) {
				for (int i = 0; i < m_camera_overlayui_array.length; i++) {
					m_camera_overlayui_array[i].setAlpha(1);
					m_camera_overlayui_array[i].setVisibility(View.VISIBLE);

				}
				m_camera_slide_bg_camera_icon.setAlpha(255);
				m_camera_slide_bg_record_icon.setAlpha(255);
				m_camera_slide_bg_2d_icon.setAlpha(255);
				m_camera_slide_bg_3d_icon.setAlpha(255);
				m_camera_slide_bg_camera_icon.setVisibility(View.VISIBLE);
				m_camera_slide_bg_record_icon.setVisibility(View.VISIBLE);
				m_camera_slide_bg_2d_icon.setVisibility(View.VISIBLE);
				m_camera_slide_bg_3d_icon.setVisibility(View.VISIBLE);
				m_btn_panorama_sparepower.setVisibility(View.GONE);
				m_btn_camera_capture
						.setBackgroundResource(R.drawable.capture_button);
			}

		} else {
			StoredData.saveString(StoredData.M_REVIEWFILEPATH,
					m_bitmappicker.getCurrentFileName());
			m_imageview.resetRect();
			if (!nextorpre) {
				m_imageview.setCurbitmapR(m_bitmaps[0]);
				loadBitmap(false);
			} else {
				m_imageview.setCurbitmapR(m_bitmaps[1]);
				loadBitmap(true);
			}
			m_imageview.resetRect();
			m_imageview.invalidate();

		}
	}

	public void updateThumbnails(ArrayList<String> lst) {
		Bitmap bit = null;
		String path = "";
		if (lst != null) {
			if (lst.size() == 0) {
				WiCameraActivity.newpic_thumbnails_parent
						.setVisibility(View.GONE);
				m_iv_camera_newpic_thumbnails.setImageBitmap(bit);
				StoredData.saveString(StoredData.M_REVIEWFILEPATH, "a");
			} else {
				path = lst.get(lst.size() - 1);
				if (FileTypeHelper.isImageFile(path)) {
					bit = OperationFile.fitSizeImg(path, 90, 90, 90);
				} else {
					bit = OperationFile.getVideotThumbnail(path, 90, 90);
				}
				StoredData.saveString(StoredData.M_REVIEWFILEPATH, path);
			}
		} else {
			StoredData.saveString(StoredData.M_REVIEWFILEPATH, "a");
			m_iv_camera_newpic_thumbnails.setImageBitmap(bit);
			WiCameraActivity.newpic_thumbnails_parent.setVisibility(View.GONE);
		}

	}

	/**
	 * 编写双击事件
	 * 
	 * @param handle
	 *            发送延迟消息的handle
	 * @param button
	 *            需要双击事件的控件
	 */
	public void doubleClick(Handler handle, final View button, int misecond) {
		// 单击button后设置其为不可用
		button.setEnabled(false);
		button.setClickable(false);
		handle = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				// 得到延迟发的消息后设置按钮为可用
				if (msg.what == 0) {
					button.setEnabled(true);
					button.setClickable(true);
				}
				super.handleMessage(msg);
			}
		};
		// 创建消息并用当前handle延迟发送
		Message message = new Message();
		;
		message.what = 0;
		handle.sendMessageDelayed(message, misecond);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		System.out.println("恢复..............................................");
		super.onResume();

		if (mOrientationEventListener != null) {
			mOrientationEventListener.enable();
		}
		// 全景
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		List<Sensor> sensors = sensorManager
				.getSensorList(Sensor.TYPE_ACCELEROMETER);
		if (sensors.size() > 0) {
			sensorManager.registerListener(this, sensors.get(0),
					SensorManager.SENSOR_DELAY_UI);
		}
		initSoundRecorder();
		boolean is3D = CSStaticData.g_is_3D_mode;
		if (camera_state != camera_record) {
			if (!isRear) {
				is3D = false;
				CSStaticData.g_is_3D_mode = false;
			}
			add3DView(true);

		} else {
			if (is3D) {
				is3D = false;
				CSStaticData.g_is_3D_mode = false;
			}
			if (!isRear) {
				is3D = false;
				CSStaticData.g_is_3D_mode = false;
			}
			add3DView(false);
		}

		if (is3D) {
			if (isPanoramaMode) {
				m_btn_camera_capture
						.setBackgroundResource(R.drawable.panorama_stop_click);
				recylePanorama(true);
				m_btn_camera_panoramic.setChecked(false);
				Message msg = new Message();
				msg.what = 200;
				msg.arg1 = 2;// 2d
				m_main_handle.sendMessage(msg);
			}
		}
		// m_btn_camera_dimension.setChecked(is3D);
		if (is3D) {
			// m_btn_camera_dimension.setChecked(false);
			m_btn_camera_used_camera.setAlpha(0);
			m_btn_camera_used_camera.setEnabled(false);
		} else {
			// m_btn_camera_dimension.setChecked(true);
			m_btn_camera_used_camera.setAlpha(1);
			m_btn_camera_used_camera.setEnabled(true);
		}
		m_btn_camera_dimension.setChecked(!is3D);
		// m_btn_camera_panoramic.setEnabled(false);
		demon(is3D);

	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		System.out.println("onrestart...................");
		// }
		super.onRestart();
	}

	/*
	 * @Override protected void onStart() { // TODO Auto-generated method stub
	 * System.out.println("onstart.......................");
	 * videoSurfaceView.stopPreview(); videoSurfaceView.startPreview();
	 * super.onStart(); }
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		videoSurfaceView.onPause();
		System.out
				.println("onpause..............................................");
		super.onPause();
		if (m_btn_camera_review_share != null) {
			m_btn_camera_review_share.setChecked(false);
		}
		if (m_btn_camera_review_information != null) {
			m_btn_camera_review_information.setChecked(false);
		}
		// 全景
		releaseSoundRecorder();
		if (sensorManager != null) {
			sensorManager.unregisterListener(this);
		}
		if (camera_state == camera_panoramic) {
			if (mThreadRunning)
				cancelHighResComputation();
			if (mCaptureState == CAPTURE_STATE_MOSAIC) {
				m_btn_camera_capture
						.setBackgroundResource(R.drawable.capture_button);
				stopCapture(true);
				onBackgroundThreadFinished();
				reset();
				startCameraPreview();
				resetPanoUI();
				panoramaToCamera();
			}
		}
		if (Util.CAMERA_STATE != InterSurfaceView.CAMERA_VIDEO) {

			videoSurfaceView.stopPreview();
			videoSurfaceView.releaseCamera();
		} else {
			// 释放mediarecorder的资源
			videoSurfaceView.stopRecord();
			// 释放照相头资源
			videoSurfaceView.stopPreview();
			videoSurfaceView.releaseCamera();
		}

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		System.out.println("onDestroy 执行了。。。是否是3D" + CSStaticData.g_is_3D_mode);
		// 设为非连拍模式
		// videoSurfaceView.isSingleMode(true);
		super.onDestroy();
		boolean isreview = StoredData.getBoolean(StoredData.M_REVIEW, true);
		boolean is3d = StoredData.getBoolean(StoredData.M_CAMERA3D, false);

		Intent intent = getIntent();
		if (intent != null) {

			String extra = intent.getStringExtra("camera_cmd");
			if (extra != null) {
				if ((extra.equals("call_from_gallery"))
						|| (extra.equals("call_from_imageview"))
						|| (extra.equals("call_from_videoview"))) {

				} else {
					// 退出程序变为2D
					StoredData.saveBoolean(StoredData.M_CAMERA3D, false);
					CSStaticData.g_is_3D_mode = false;
					// WiCameraActivity.isRear = true;
					setScreenDimension(false);
				}
			} else {
				// 退出程序变为2D
				StoredData.saveBoolean(StoredData.M_CAMERA3D, false);
				CSStaticData.g_is_3D_mode = false;
				// WiCameraActivity.isRear = true;
				setScreenDimension(false);
			}
		} else {
			// 退出程序变为2D
			StoredData.saveBoolean(StoredData.M_CAMERA3D, false);
			CSStaticData.g_is_3D_mode = false;
			// WiCameraActivity.isRear = true;
			setScreenDimension(false);
		}

		// if (isreview && camera_state == camera_record && is3d) {
		// StoredData.saveBoolean(StoredData.M_CAMERA3D, true);
		// }
		try {
			// 取消注册电池电量的广播
			if (batteryReceiver != null) {
				unregisterReceiver(batteryReceiver);
				batteryReceiver = null;
			}

			if (videoSurfaceView != null) {
				videoSurfaceView.stopFaceDetection();
			}
			WiCameraActivity.isRear = true;
			if (camera_state != camera_record) {
				videoSurfaceView.stopPreview();
				videoSurfaceView.releaseCamera();
			} else {
				// 释放mediarecorder的资源
				videoSurfaceView.stopRecord();
				// 释放照相头资源
				videoSurfaceView.stopPreview();
				videoSurfaceView.releaseCamera();
			}
			if (mOrientationEventListener != null) {
				mOrientationEventListener.disable();
				mOrientationEventListener = null;
			}
			// 取消注册sdcard是否被拔出的广播
			// if (msdCardBroadcastReceiver != null) {
			// unregisterReceiver(msdCardBroadcastReceiver);
			// }
		} catch (Exception e) {
			// TODO: handle exception
			Log.v("camera", "onDestroy failed");
			e.printStackTrace();
		}

		if (isPanoramaMode) {
			// 如果正在保存
			if (mThreadRunning) {
				cancelHighResComputation();
			}
			// 正在全景拍摄
			if (mCaptureState == CAPTURE_STATE_MOSAIC) {
				stopCapture(true);
				reset();
			}
			try {
				releaseCamera();
				clearMosaicFrameProcessorIfNeeded();
				panoramaToCamera();
				m_btn_camera_panoramic.setChecked(false);
				System.gc();
			} catch (Exception e) {
				if (CSStaticData.DEBUG)
					Log.e(Tag, "回收资源失败了！");
			}
		}

	}

	/**
	 * 拍照时屏幕旋转时候控件的位置的改变
	 * 
	 * @param degree
	 *            屏幕旋转的角度
	 */
	/**
	 * 拍照时屏幕旋转时候控件的位置的改变
	 * 
	 * @param degree
	 *            屏幕旋转的角度
	 */
	public void cameraRotateDegree(int degree) {
		if (degree == 90 || degree == 270) {
			LayoutParams p = (LayoutParams) new LayoutParams(40, 30);
			p.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			p.leftMargin = 100;
			p.bottomMargin = 15;
			m_btn_camera_sparepower.setLayoutParams(p);
			// if (camera_state == camera_record) {
			LayoutParams p1 = (LayoutParams) new LayoutParams(40, 30);
			p1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			p1.leftMargin = 100;
			p1.bottomMargin = 60;
			m_btn_camera_storagemode.setLayoutParams(p1);

			LayoutParams p2 = (LayoutParams) new LayoutParams(40, 30);
			p2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			p2.leftMargin = 100;
			p2.bottomMargin = 100;
			m_btn_camera_scence.setLayoutParams(p2);
			// }
			LayoutParams p3 = (LayoutParams) new LayoutParams(80, 30);
			p3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			p3.leftMargin = 80;
			if (m_btn_camera_newpic_sizeofsum.getText().toString().length() < 5) {
				p3.leftMargin = 80;
			} else if (m_btn_camera_newpic_sizeofsum.getText().toString()
					.length() == 5) {
				p3.leftMargin = 80;
			} else if (m_btn_camera_newpic_sizeofsum.getText().toString()
					.length() == 6) {
				p3.leftMargin = 80;
			}

			p3.bottomMargin = 150;
			m_btn_camera_newpic_sizeofsum.setLayoutParams(p3);

			LayoutParams p4 = (LayoutParams) new LayoutParams(40, 30);
			p4.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			p4.leftMargin = 100;
			p4.bottomMargin = 205;
			m_btn_camera_self_timer.setLayoutParams(p4);

		} else {

			LayoutParams p = (LayoutParams) new LayoutParams(40, 30);
			p.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			p.leftMargin = 105;
			p.topMargin = 10;
			m_btn_camera_sparepower.setLayoutParams(p);
			// if (camera_state == camera_record) {
			LayoutParams p1 = (LayoutParams) new LayoutParams(40, 30);
			p1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			p1.leftMargin = 155;
			p1.topMargin = 10;
			m_btn_camera_storagemode.setLayoutParams(p1);

			LayoutParams p2 = (LayoutParams) new LayoutParams(40, 30);
			p2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			p2.leftMargin = 205;
			p2.topMargin = 10;
			m_btn_camera_scence.setLayoutParams(p2);
			// }
			LayoutParams p3 = (LayoutParams) new LayoutParams(
					LayoutParams.WRAP_CONTENT, 30);
			p3.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			p3.leftMargin = 250;
			p3.topMargin = 10;
			m_btn_camera_newpic_sizeofsum.setLayoutParams(p3);

			LayoutParams p4 = (LayoutParams) new LayoutParams(40, 30);
			p4.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			p4.leftMargin = 305;
			p4.topMargin = 10;
			m_btn_camera_self_timer.setLayoutParams(p4);

		}

	}

	/**
	 * 在切换到录像和正在录像时候屏幕旋转时控件位置的改变
	 * 
	 * @param degree
	 *            屏幕旋转的角度
	 * @param isRecord
	 *            是否正在录像
	 */
	public void recordRotateDegree(int degree, boolean isRecord) {
		if (isRecord) {
			if (degree == 90 || degree == 270) {

				LayoutParams p = (LayoutParams) new LayoutParams(40, 30);
				p.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
				p.leftMargin = 0;
				p.bottomMargin = 10;
				m_btn_camera_sparepower.setLayoutParams(p);

				LayoutParams p1 = (LayoutParams) new LayoutParams(90, 30);
				p1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
				p1.leftMargin = -26;
				p1.bottomMargin = 90;
				m_btn_camera_storagemode.setLayoutParams(p1);
			} else {

				LayoutParams p = (LayoutParams) new LayoutParams(40, 30);
				p.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				p.leftMargin = 6;
				p.topMargin = 5;
				m_btn_camera_sparepower.setLayoutParams(p);

				LayoutParams p1 = (LayoutParams) new LayoutParams(80, 30);
				p1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				p1.leftMargin = 66;
				p1.topMargin = 2;
				m_btn_camera_storagemode.setLayoutParams(p1);

			}
		} else {
			if (degree == 90 || degree == 270) {

				LayoutParams p = (LayoutParams) new LayoutParams(40, 30);
				p.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
				p.leftMargin = 100;
				p.bottomMargin = 15;
				m_btn_camera_sparepower.setLayoutParams(p);

				LayoutParams p1 = (LayoutParams) new LayoutParams(80, 30);
				p1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
				p1.leftMargin = 80;
				p1.bottomMargin = 90;
				m_btn_camera_storagemode.setLayoutParams(p1);

				LayoutParams p2 = (LayoutParams) new LayoutParams(100, 30);
				p2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
				p2.leftMargin = 70;
				p2.bottomMargin = 190;
				m_btn_camera_scence.setLayoutParams(p2);

				LayoutParams p4 = (LayoutParams) new LayoutParams(40, 30);
				p4.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
				p4.leftMargin = 100;
				p4.bottomMargin = 255;
				m_btn_camera_self_timer.setLayoutParams(p4);

			} else {

				LayoutParams p = (LayoutParams) new LayoutParams(40, 30);
				p.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				p.addRule(RelativeLayout.ALIGN_PARENT_TOP);
				p.leftMargin = 105;
				p.topMargin = 20;
				m_btn_camera_sparepower.setLayoutParams(p);

				LayoutParams p1 = (LayoutParams) new LayoutParams(90, 30);
				p1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				p1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
				p1.leftMargin = 153;
				p1.topMargin = 16;
				m_btn_camera_storagemode.setLayoutParams(p1);

				LayoutParams p2 = (LayoutParams) new LayoutParams(100, 30);
				p2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				p2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
				p2.leftMargin = 252;
				p2.topMargin = 16;
				m_btn_camera_scence.setLayoutParams(p2);

				LayoutParams p4 = (LayoutParams) new LayoutParams(40, 30);
				p4.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				p4.addRule(RelativeLayout.ALIGN_PARENT_TOP);
				p4.leftMargin = 350;
				p4.topMargin = 16;
				m_btn_camera_self_timer.setLayoutParams(p4);
			}
		}
		m_al_camera_overlayui.postInvalidate();

	}

	/**
	 * review时review控件在屏幕角度值发生改变时review控件位置的改变
	 * 
	 * @param degree
	 *            屏幕旋转的角度
	 */
	public void reviewRotateDegree(int degree) {
		if (degree == 90 || degree == 270) {

			LayoutParams p = (LayoutParams) new LayoutParams(72, 72);
			p.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			p.bottomMargin = 84;
			p.leftMargin = 5;
			m_btn_camera_review_delete.setLayoutParams(p);

			LayoutParams p1 = (LayoutParams) new LayoutParams(72, 72);
			p1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			p1.bottomMargin = 185;
			p1.leftMargin = 5;
			m_btn_camera_review_share.setLayoutParams(p1);

			LayoutParams p2 = (LayoutParams) new LayoutParams(72, 72);
			p2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			p2.bottomMargin = 286;
			p2.leftMargin = 5;
			m_btn_camera_review_information.setLayoutParams(p2);

			LayoutParams p3 = (LayoutParams) new LayoutParams(72, 72);
			p3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			p3.bottomMargin = 387;
			p3.leftMargin = 5;
			m_btn_camera_review_tocamera.setLayoutParams(p3);

		} else {

			LayoutParams p = (LayoutParams) new LayoutParams(72, 72);
			p.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

			p.rightMargin = 303 + sbarh;
			p.topMargin = 5;
			m_btn_camera_review_delete.setLayoutParams(p);

			LayoutParams p1 = (LayoutParams) new LayoutParams(72, 72);
			p1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			p1.rightMargin = 203 + sbarh;
			p1.topMargin = 5;
			m_btn_camera_review_share.setLayoutParams(p1);

			LayoutParams p2 = (LayoutParams) new LayoutParams(72, 72);
			p2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			p2.rightMargin = 101 + sbarh;
			p2.topMargin = 5;
			m_btn_camera_review_information.setLayoutParams(p2);

			LayoutParams p3 = (LayoutParams) new LayoutParams(72, 72);
			p3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			p3.rightMargin = 0 + sbarh;
			p3.topMargin = 5;
			m_btn_camera_review_tocamera.setLayoutParams(p3);

		}
	}

	/**
	 * 是否显示照相ui，还是显示录像ui
	 * 
	 * @param isShow
	 *            Boolean变量，true则显示照相ui，false则显示录像ui
	 */
	public void IsShowCameraMiddleUI(boolean isShow) {

		int isVideoUIShow = View.GONE;
		int isCameraUIShow = View.VISIBLE;
		if (isShow) {
			isVideoUIShow = 0;
			isCameraUIShow = 1;
		} else {
			isVideoUIShow = 1;
			isCameraUIShow = 0;
		}
		m_al_camera_overlayui.postInvalidate();
		if (isShow) {

			m_btn_camera_newpic_sizeofsum.setAlpha(isCameraUIShow);
			m_btn_camera_continuous.setAlpha(isCameraUIShow);
			m_btn_camera_panoramic.setAlpha(isCameraUIShow);
			m_btn_camera_self_timer.setAlpha(isCameraUIShow);
			m_btn_camera_newpic_sizeofsum.setClickable(true);
			m_btn_camera_continuous.setClickable(true);
			m_btn_camera_panoramic.setClickable(true);
			m_btn_camera_self_timer.setClickable(true);
			m_btn_camera_newpic_sizeofsum.postInvalidate();
			m_btn_camera_continuous.postInvalidate();
			m_btn_camera_panoramic.postInvalidate();

			int storageMode = StoredData.getInt(StoredData.M_STORAGEMODE, 0);
			if (storageMode == 0) {
				m_btn_camera_storagemode
						.setBackgroundResource(R.drawable.storage_internal);
			} else {
				m_btn_camera_storagemode
						.setBackgroundResource(R.drawable.storage_sdcard);
			}
			m_btn_camera_storagemode.setText("");
			m_btn_camera_scence
					.setBackgroundResource(R.drawable.camera_pictures);
			m_btn_camera_scence.setText("");
			LayoutParams lps = (LayoutParams) m_btn_camera_scence
					.getLayoutParams();
			lps.width = 40;
			lps.height = 30;
			m_btn_camera_scence.setLayoutParams(lps);

			m_btn_camera_storagemode.setLayoutParams(new LayoutParams(40, 30));

			m_btn_camera_storagemode.setShadowLayer(0, 0, 0, Color.TRANSPARENT);
			m_btn_camera_scence.setShadowLayer(0, 0, 0, Color.TRANSPARENT);
			int camerabg = StoredData.getInt(StoredData.M_CAMERA_SELF_TIMERBG,
					R.drawable.popup_transparent);
			m_btn_camera_self_timer.setBackgroundResource(camerabg);
		} else {
			m_btn_camera_newpic_sizeofsum.setAlpha(isCameraUIShow);
			m_btn_camera_continuous.setAlpha(isCameraUIShow);
			m_btn_camera_panoramic.setAlpha(isCameraUIShow);
			m_btn_camera_newpic_sizeofsum.setClickable(false);
			m_btn_camera_continuous.setClickable(false);
			m_btn_camera_panoramic.setClickable(false);
			m_btn_camera_self_timer.setClickable(false);
			m_btn_camera_newpic_sizeofsum.postInvalidate();
			m_btn_camera_continuous.postInvalidate();
			m_btn_camera_panoramic.postInvalidate();
			m_btn_camera_self_timer.postInvalidate();
			timeCounter = new TimeCounter(m_ch_camera_time_count,
					m_btn_camera_storagemode);
			m_btn_camera_storagemode.setText("00:00:00");
			m_btn_camera_storagemode
					.setBackgroundResource(R.drawable.popup_transparent);
			String videoSizeText = StoredData.getString("videoSize",
					"1280x720 ");
			m_btn_camera_scence.setText(videoSizeText);
			m_btn_camera_scence
					.setBackgroundResource(R.drawable.popup_transparent);
			m_btn_camera_storagemode.setShadowLayer(1, 2, 2, Color.BLACK);
			m_btn_camera_scence.setShadowLayer(1, 2, 2, Color.BLACK);
			int videobg = StoredData.getInt(StoredData.M_VIDEO_SELF_TIMERBG,
					R.drawable.popup_transparent);
			m_btn_camera_self_timer.setBackgroundResource(videobg);
		}

	}

	/**
	 * 是否显示正在录像的ui
	 * 
	 * @param isShow
	 *            boolean 值 是否显示 true 代表显示，false 代表不显示
	 */
	public void showRecordingUI(boolean isShow) {

		for (int i = 0; i < m_camera_overlayui_array.length; i++) {
			if (m_camera_overlayui_array[i] != null) {
				if (isShow) {

					if (i == 4 || i == 11 || i == 5) {
						m_camera_overlayui_array[i].setAlpha(1);
						m_camera_overlayui_array[i].setClickable(true);
					} else {
						m_camera_overlayui_array[i].setAlpha(0);
						m_camera_overlayui_array[i].setClickable(false);

						m_camera_slide_bg_camera_icon.setAlpha(0);
						m_camera_slide_bg_record_icon.setAlpha(0);
						m_camera_slide_bg_2d_icon.setAlpha(0);
						m_camera_slide_bg_3d_icon.setAlpha(0);
						m_camera_slide_bg_camera_icon.setVisibility(View.GONE);
						m_camera_slide_bg_record_icon.setVisibility(View.GONE);
						m_camera_slide_bg_2d_icon.setVisibility(View.GONE);
						m_camera_slide_bg_3d_icon.setVisibility(View.GONE);
					}
				} else {

					if (i != 7 && i != 13 && i != 14) {
						m_camera_overlayui_array[i].setAlpha(1);
						m_camera_overlayui_array[i].setClickable(true);

						m_camera_slide_bg_camera_icon.setAlpha(255);
						m_camera_slide_bg_record_icon.setAlpha(255);
						m_camera_slide_bg_2d_icon.setAlpha(255);
						m_camera_slide_bg_3d_icon.setAlpha(255);
						m_camera_slide_bg_camera_icon
								.setVisibility(View.VISIBLE);
						m_camera_slide_bg_record_icon
								.setVisibility(View.VISIBLE);
						m_camera_slide_bg_2d_icon.setVisibility(View.VISIBLE);
						m_camera_slide_bg_3d_icon.setVisibility(View.VISIBLE);
						m_camera_slide_bg_camera_icon.postInvalidate();
						m_camera_slide_bg_record_icon.postInvalidate();
						m_camera_slide_bg_2d_icon.postInvalidate();
						m_camera_slide_bg_3d_icon.postInvalidate();
					} else {
						m_camera_overlayui_array[i].setAlpha(0);
						m_camera_overlayui_array[i].setClickable(false);
					}

				}
			}
		}
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

		public synchronized void setRate(boolean isok) {
			if (m_mr_smaller != null) {
				m_mr_smaller = null;
			}
			if (m_mr_smaller != null) {
				m_mr_smaller = null;
			}

			if (isok) {// rate2
				m_mr_smaller = new ScaleAnimation(1f, rate1, 1f, rate1,
						Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 0.5f);
				m_mr_bigger = new ScaleAnimation(rate1, 1, rate1, 1,
						Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 0.5f);
			} else {
				m_mr_bigger = new ScaleAnimation(1f, rate2, 1f, rate2,
						Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 0.5f);
				m_mr_smaller = new ScaleAnimation(rate2, 1, rate2, 1,
						Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 0.5f);

			}
		}

		public synchronized void reviewRorate(boolean isok, int a1, int start1) {
			AnimationSet animSet = null;
			m_camera_reviewui_array[0].clearAnimation();

			if (isok) {
				if (mCurrentDegree == 360 || mCurrentDegree == 180) {
					animSet = new AnimationSet(true);
					// animSet.setDuration(400);
					animSet.addAnimation(new RotateAnimation(start1, start1
							+ a1, Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 0.5f));
					// 手机竖屏转横屏，缩小
					animSet.addAnimation(m_mr_bigger);
					animSet.setFillAfter(true);

					m_camera_reviewui_array[0].startAnimation(animSet);
				}
				if (mCurrentDegree == 90 || mCurrentDegree == 270)

				{
					animSet = new AnimationSet(true);
					// animSet.setDuration(400);
					animSet.addAnimation(new RotateAnimation(start1, start1
							+ a1, Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 0.5f));
					// 手机横屏转竖屏，放大
					animSet.addAnimation(m_mr_smaller);
					animSet.setFillAfter(true);

					m_camera_reviewui_array[0].startAnimation(animSet);
				}

				// start1 += a1;
				// mPreOrientations = mOrientation;
			} else {

				if (mCurrentDegree == 360 || mCurrentDegree == 180) {
					animSet = new AnimationSet(true);
					// animSet.setDuration(400);
					animSet.addAnimation(new RotateAnimation(start1, start1
							+ a1, Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 0.5f));
					// 手机竖屏转横屏，缩小
					animSet.addAnimation(m_mr_smaller);
					animSet.setFillAfter(true);

					m_camera_reviewui_array[0].startAnimation(animSet);
				}
				if (mCurrentDegree == 90 || mCurrentDegree == 270)

				{
					animSet = new AnimationSet(true);

					animSet.addAnimation(new RotateAnimation(start1, start1
							+ a1, Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 0.5f));
					// 手机横屏转竖屏，放大
					animSet.addAnimation(m_mr_bigger);
					animSet.setFillAfter(true);

					m_camera_reviewui_array[0].startAnimation(animSet);
				}

			}
		}

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
					+ Util.getDisplayRotation(WiCameraActivity.this);
			if (mOrientationCompensation != orientationCompensation) {
				mOrientationCompensation = orientationCompensation;
				mCurrentDegree = mOrientationCompensation;
				System.out.println("mOrientationss=" + mCurrentDegree);
				// System.out.println("mOrientation=======" + mOrientation);
				if (videoSurfaceView != null) {
					videoSurfaceView
							.setOrientationIndicator(mOrientationCompensation);
				}
				System.out.println("mCurrentDegree" + mCurrentDegree);
				// 改变listview的位置

				boolean demon = StoredData.getBoolean(StoredData.M_CAMERA3D,
						false);
				if (demon) {
					return;
				}
				if (camera_state == camera_panoramic) {
					return;
				}
				if (mPreOrientation != mOrientation) {
					// videoSurfaceView.setRotationParm();
					Message mess = new Message();
					mess.arg1 = 0;
					mess.what = 200;
					m_main_handle.removeMessages(200);
					m_main_handle.sendMessage(mess);
				}

			}

			// }

		}
	}
/**
 * 设置review界面动画效果，定义动画runnable
 */
	public void setMovement() {

		m_handler = new Handler();
		m_runnable1 = new Runnable() {

			@Override
			public void run() {
				if (m_isNext) {
					m_position -= (screenWidth_Review / 12);
					int i = m_position;
					if (i > 0) {
						m_imageview.setSrc1Rect(0, 0, screenHeight_Review,
								screenWidth_Review / 2 - i);
						m_imageview.setDst1Rect(i, 0, screenHeight_Review,
								screenWidth_Review / 2);
						m_imageview.setDst2Rect(i + screenWidth_Review / 2, 0,
								screenHeight_Review, screenWidth_Review);
						m_imageview.setSrc2Rect(screenWidth_Review / 2 - i, 0,
								screenHeight_Review, screenWidth_Review / 2);
						m_imageview.setDst3Rect(0, 0, screenHeight_Review, i);
						m_imageview
								.setDst4Rect(screenWidth_Review / 2, 0,
										screenHeight_Review, screenWidth_Review
												/ 2 + i);
						m_imageview.postInvalidate();
					}

				} else {
					m_position += screenWidth_Review / 12;
					int i = m_position;
					if (m_position < screenWidth_Review / 2) {
						m_imageview.setSrc1Rect(screenWidth_Review / 2 - i, 0,
								screenHeight_Review, screenWidth_Review / 2);
						m_imageview.setDst1Rect(0, 0, screenHeight_Review, i);
						m_imageview
								.setDst2Rect(screenWidth_Review / 2, 0,
										screenHeight_Review, screenWidth_Review
												/ 2 + i);
						m_imageview.setSrc2Rect(0, 0, screenHeight_Review,
								screenWidth_Review / 2 - i);
						m_imageview.setDst3Rect(i, 0, screenHeight_Review,
								screenWidth_Review / 2);
						m_imageview.setDst4Rect(screenWidth_Review / 2 + i, 0,
								screenHeight_Review, screenWidth_Review);
						m_imageview.postInvalidate();
					}
				}

				if (m_position > 0 && m_position < screenWidth_Review / 2) {
					m_handler.postDelayed(m_runnable1, 40);
				}
				/*
				 * if ((m_position <= 0 && m_position > -(screenWidth / 12)) ||
				 * (m_position >= screenWidth / 2 && m_position < (screenWidth /
				 * 2 + screenWidth / 12)))
				 */
				else {
					m_imageview.resetRect();
					m_imageview.setNextbitmap(null);
					m_imageview.postInvalidate();
					m_isMove = false;
					m_moveX = 0;
				}
			}
		};
		// 下一张的移动动画
		m_runnable = new Runnable() {

			@Override
			public void run() {
				if (m_isNext) {
					m_position -= (screenWidth_Review / 12);
					int i = m_position;
					if (m_position > 0) {
						m_imageview.setSrc1Rect(screenWidth_Review / 2 - i, 0,
								screenHeight_Review, screenWidth_Review / 2);
						m_imageview.setDst1Rect(0, 0, screenHeight_Review, i);
						m_imageview
								.setDst2Rect(screenWidth_Review / 2, 0,
										screenHeight_Review, screenWidth_Review
												/ 2 + i);
						m_imageview.setSrc2Rect(0, 0, screenHeight_Review,
								screenWidth_Review / 2 - i);

						m_imageview.setDst3Rect(i, 0, screenHeight_Review,
								screenWidth_Review / 2);
						m_imageview.setDst4Rect(screenWidth_Review / 2 + i, 0,
								screenHeight_Review, screenWidth_Review);
						m_imageview.postInvalidate();
					}
				} else {
					m_position += screenWidth_Review / 12;
					int i = m_position;
					if (m_position < screenWidth_Review / 2) {
						m_imageview.setSrc1Rect(0, 0, screenHeight_Review,
								screenWidth_Review / 2 - i);

						m_imageview.setDst1Rect(i, 0, screenHeight_Review,
								screenWidth_Review / 2);
						m_imageview.setDst2Rect(screenWidth_Review / 2 + i, 0,
								screenHeight_Review, screenWidth_Review);
						m_imageview.setSrc2Rect(screenWidth_Review / 2 - i, 0,
								screenHeight_Review, screenWidth_Review / 2);

						m_imageview.setDst3Rect(0, 0, screenHeight_Review, i);
						m_imageview
								.setDst4Rect(screenWidth_Review / 2, 0,
										screenHeight_Review, screenWidth_Review
												/ 2 + i);
						m_imageview.postInvalidate();
					}

				}

				if (m_position > 0 && m_position < screenWidth_Review / 2) {
					m_handler.postDelayed(m_runnable, 40);
				}

				else {
					if (m_isNext) {
						WiImageView.recycleDBitmap(m_bitmaps[0]);
						m_bitmaps[0] = m_imageview.m_curbitmap;
					} else {
						WiImageView.recycleDBitmap(m_bitmaps[1]);
						m_bitmaps[1] = m_imageview.m_curbitmap;
					}
					m_imageview.resetRect();
					m_imageview.exchange();
					m_imageview.setNextbitmap(null);
					m_imageview.postInvalidate();
					m_isMove = false;
					m_moveX = 0;
				}
			}
		};

		// 渐变动画
		mrunnable2 = new Runnable() {

			@Override
			public void run() {
				int i = m_alpha;
				m_alpha = m_alpha + 15;
				if (m_alpha < 255) {
					if (m_imageview.mode != 0) {
						m_handler.postDelayed(mrunnable2, 25);
						m_imageview.setAlpha(i);
						m_imageview.postInvalidate();
					} else {
						if (CSStaticData.DEBUG)
							Log.e(Tag, "slide show exit wrong");
					}
				} else if (m_alpha >= 255 && m_alpha < 270) {
					m_imageview.setAlpha(0);
					WiImageView.recycleDBitmap(m_bitmaps[0]);
					m_bitmaps[0] = m_imageview.m_curbitmap;
					m_imageview.exchange();
					m_imageview.setNextbitmap(null);
					m_imageview.postInvalidate();
					m_bitmappicker.getNextBitmap();
					if (m_imageview.mode == 1) {
						// 设置下一张图片
						File file = new File(
								m_bitmappicker.getCurrentFileName());
						if (file.exists()) {
							m_imageview.setNextbitmap(m_bitmaps[1]);
							m_bitmappicker.getNextBitmap();
							loadBitmap(true);
							m_bitmappicker.getPreBitmap();
							m_alpha = 0;
							m_handler.postDelayed(mrunnable2, 2000);
						} else {
							m_imageview.mode = 0;
						}
					}
				}

			}
		};
	}

	/**
	 * Review界面手指松开时，移回到原图的动画
	 */
	public void moveBack(float X, boolean bool) {
		m_isMove = true;
		if (X < 0) {
			m_isNext = false;
			m_position = screenWidth_Review / 2 + (int) X;
			if (!bool) {
				m_isNext = true;
			}
		} else {
			m_isNext = true;
			m_position = (int) X;
			if (!bool) {
				m_isNext = false;
			}
		}
		if (!bool)
			m_handler.post(m_runnable);
		else {
			m_handler.post(m_runnable1);
		}
	}

	/**
	 * 异步加载下一张要显示的图片
	 */
	public void loadBitmapInBackground(String string, int next) {
		LoadBufImageTask loadtask = new LoadBufImageTask(this);
		loadtask.execute(string, next);
	}

	public void loadBitmap(Boolean isnext) {
		if (isnext) {
			loadBitmapInBackground(m_bitmappicker.getNextBitmapforBuf(), 1);
		} else {
			loadBitmapInBackground(m_bitmappicker.getPreBitmapforBuf(), 0);
		}
	}
/**
 * 异步加载下一张图片
 * 
 *
 */
	class LoadBufImageTask extends AsyncTask<Object, Integer, Object> {
		// 可变长的输入参数，与AsyncTask.exucute()对应
		Bitmap[] bufBitmaps;
		int whichtoupdate;

		public LoadBufImageTask(Context context) {
			m_isNextBitmapLoaded++;
			bufBitmaps = new Bitmap[2];
		}

		@Override
		protected String doInBackground(Object... params) {
			bufBitmaps = m_imageview.getNextBitmapEx((String) params[0]);
			whichtoupdate = (Integer) params[1];

			return null;

		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Object result) {
			if (CSStaticData.DEBUG)
				Log.e(Tag, "異步加載完成" + "whichtoupdate" + whichtoupdate);
			if (whichtoupdate == 0)
				m_bitmaps[0] = bufBitmaps;
			else {
				m_bitmaps[1] = bufBitmaps;
			}
			m_isNextBitmapLoaded--;
		}

		@Override
		protected void onPreExecute() {
			// 任务启动，可以在这里显示一个对话框，这里简单处理
			if (CSStaticData.DEBUG)
				Log.e(Tag, "異步加載圖片開始");
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// 更新进度

		}

	}

	/**
	 * 触发显示下一张图片的动画
	 */
	public void showNext(boolean bool) {
		if (bool) {
			m_isNext = true;
			m_imageview.setNextbitmap(m_bitmaps[1]);
			m_bitmappicker.getNextBitmap();
			loadBitmap(true);

			m_position = screenWidth_Review / 2 + (int) m_moveX;

		} else {
			m_isNext = false;
			m_imageview.setNextbitmap(m_bitmaps[0]);
			m_bitmappicker.getPreBitmap();
			loadBitmap(false);

			m_position = (int) m_moveX;
		}
		m_isMove = true;
		m_handler.post(m_runnable);

	}

	/**
	 * 在没有缩放的情况下，图片随手指移动
	 */
	public void showNextOnMove(boolean bool) {

		// true 下一張，false 上一張
		if (bool) {
			if (m_bitmappicker.getCurrentFileIndex() < (m_bitmappicker
					.getFileCounts() - 1)) {

				/*
				 * if (m_imageview.m_nextbitmap == null) {
				 * m_imageview.setNextbitmap(m_bitmaps[1]); }
				 */
				m_imageview.setNextbitmap(m_bitmaps[1]);
				m_imageview.setSrc1Rect(-(int) m_moveX, 0, screenHeight_Review,
						screenWidth_Review / 2);
				m_imageview.setDst1Rect(0, 0, screenHeight_Review,
						(int) m_moveX + screenWidth_Review / 2);
				m_imageview
						.setDst2Rect(screenWidth_Review / 2, 0,
								screenHeight_Review, screenWidth_Review
										+ (int) m_moveX);
				m_imageview.setSrc2Rect(0, 0, screenHeight_Review,
						-(int) m_moveX);

				m_imageview.setDst3Rect((int) m_moveX + screenWidth_Review / 2,
						0, screenHeight_Review, screenWidth_Review / 2);
				m_imageview.setDst4Rect(screenWidth_Review + (int) m_moveX, 0,
						screenHeight_Review, screenWidth_Review);
				m_imageview.invalidate();

			}

		} else {
			if (m_bitmappicker.getCurrentFileIndex() > 0) {/*
															 * if (m_imageview.
															 * m_nextbitmap ==
															 * null) {
															 * m_imageview
															 * .setNextbitmap
															 * (m_bitmaps[0]); }
															 */
				m_imageview.setNextbitmap(m_bitmaps[0]);
				m_imageview.setSrc1Rect(0, 0, screenHeight_Review,
						screenWidth_Review / 2 - (int) m_moveX);

				m_imageview.setDst1Rect((int) m_moveX, 0, screenHeight_Review,
						screenWidth_Review / 2);
				m_imageview.setDst2Rect(screenWidth_Review / 2 + (int) m_moveX,
						0, screenHeight_Review, screenWidth_Review);
				m_imageview.setSrc2Rect(screenWidth_Review / 2 - (int) m_moveX,
						0, screenHeight_Review, screenWidth_Review / 2);

				m_imageview.setDst3Rect(0, 0, screenHeight_Review,
						(int) m_moveX);
				m_imageview.setDst4Rect(screenWidth_Review / 2, 0,
						screenHeight_Review, screenWidth_Review / 2
								+ (int) m_moveX);
				m_imageview.invalidate();

			}

		}
	}

	/**
	 * 计算动画的开始和结束的角度
	 * 
	 * @param start
	 *            开始角度
	 * @param end
	 *            结束角度
	 * @return
	 */
	public int getAngle(int start, int end) {
		if (start == end) {
			return 0;
		}
		int angle = Math.abs(start - end);
		int a = angle % 180;
		if (a == 0) {
			if (start > end) {
				return 180;
			} else {
				return -180;
			}
		} else {
			if (angle > 180) {
				if (start > end) {
					return -a;
				} else {
					return a;
				}
			} else {
				if (start > end) {
					return a;
				} else {
					return -a;
				}
			}

		}
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
/**
 * 在手势onfling时，滑动图片
 */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if ((int) m_imageview.m_rotate % 90 != 0) {
			return false;
		}

		float x1 = 0;
		float x2 = 0;
		float vx = 0;
		x1 = convertXY(mCurrentDegree, e1,m_imageview.is_3D);
		x2 = convertXY(mCurrentDegree, e2,m_imageview.is_3D);

		if (mCurrentDegree == 90) {
			vx = -velocityY;
		} else if (mCurrentDegree == 270) {
			vx = velocityY;
		} else if (mCurrentDegree == 360) {
			vx = velocityX;
		} else if (mCurrentDegree == 180) {
			vx = -velocityX;
		}
		if(m_imageview.is_3D)
			vx = velocityX;
		if (Math.abs(x1 - x2) > 100 && vx < -200) {
			if (m_isMove || (m_isNextBitmapLoaded > 0)) {

			} else {
				if (m_bitmappicker.getCurrentFileIndex() < (m_bitmappicker
						.getFileCounts() - 1)) {

					if (m_imageview.m_Scale != 1) {
						m_imageview.setNextbitmap(null);
					}
					if (m_moveX > 0) {
						moveBack(m_moveX, true);
					} else {
						showNext(true);
					}

					// setNumber();
				}
			}
		} else if (Math.abs(x2 - x1) > 100 && vx > 200) {
			if (m_isMove || (m_isNextBitmapLoaded > 0)) {

			} else {
				if (m_bitmappicker.getCurrentFileIndex() > 0) {
					if (m_imageview.m_Scale != 1) {
						m_imageview.setNextbitmap(null);
					}
					if (m_moveX < 0) {
						moveBack(m_moveX, true);
					} else {
						showNext(false);
					}
					// setNumber();
				}
			}
		}

		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	// ______________________________________________________________________
	// 全景方法
	private void releaseSoundRecorder() {
		if (mRecordSound != null) {
			mRecordSound.release();
			mRecordSound = null;
		}
	}

	private void initSoundRecorder() {

		mRecordSound = new SoundPlayer(getResources().openRawResourceFd(
				R.raw.di), false, 5);

	}

	private void resetToPreview() {
		reset();
		startCameraPreview();
	}
/**
 * 回收全景拍摄所用资源
 */
	private void clearMosaicFrameProcessorIfNeeded() {
		if (mThreadRunning)
			return;
		if (CSStaticData.DEBUG)
			Log.e(TAG,
					"----------------------[清除了mosaic内存——java]clearMosaicFrameProcessorIfNeeded------------------");
		if (mMosaicFrameProcessor != null) {
			mMosaicFrameProcessor.clear(m_is3D);
			mMosaicFrameProcessor = null;
		}

	}
/**
 * 重置全景拍摄状态量
 */
	private void reset() {
		mCaptureState = CAPTURE_STATE_VIEWFINDER;
		currentFrameCount = 0;
		flag_reportprogress = false;
		if (mMosaicFrameProcessor != null)
			mMosaicFrameProcessor.reset();

	}
/**
 * 保存处理全景图片
 */
	public void saveHighResMosaic() {
		runBackgroundThread(new Thread() {
			@Override
			public void run() {
				MosaicJpeg jpeg = generateFinalMosaic(true, true);
				if (jpeg == null) {
					// 用户取消了
					mMainHandler.sendEmptyMessage(MSG_RESET_TO_PREVIEW);
				} else if (!jpeg.isValid) {
					// 合成失败了
					mMainHandler.sendEmptyMessage(MSG_RESET_TO_PREVIEW);
				} else {
					if (m_is3D) {
						m_currentpano_filepath = savePanorama(jpeg.data,
								jpeg.width, jpeg.height, 0, 0);
						MosaicJpeg jpeg2 = generateFinalMosaic(true, false);
						if (jpeg2 == null) {
							mMainHandler.sendEmptyMessage(MSG_RESET_TO_PREVIEW);
						} else if (!jpeg2.isValid) {
							mMainHandler.sendEmptyMessage(MSG_RESET_TO_PREVIEW);
						} else {

							boolean isReviewss = StoredData.getBoolean(
									StoredData.M_REVIEW, true);
							m_currentpano_filepath = savePanorama(jpeg2.data,
									jpeg2.width, jpeg2.height, 0, 1);

							if (isReviewss) {
								mMainHandler.sendMessage(mMainHandler
										.obtainMessage(MSG_TURNTO_REVIEW));
							} else {
								mMainHandler.sendMessage(mMainHandler
										.obtainMessage(MSG_RESET_TO_PREVIEW));
							}
						}
					} else {
						m_currentpano_filepath = savePanorama(jpeg.data,
								jpeg.width, jpeg.height, 0, 2);
						boolean isReviewss = StoredData.getBoolean(
								StoredData.M_REVIEW, true);
						if (isReviewss) {
							mMainHandler.sendMessage(mMainHandler
									.obtainMessage(MSG_TURNTO_REVIEW));
						} else {
							mMainHandler.sendMessage(mMainHandler
									.obtainMessage(MSG_RESET_TO_PREVIEW));
						}
					}
					mMainHandler.sendMessage(mMainHandler
							.obtainMessage(MSG_UPDATE_THUMBNALL));
				}
			}
		});

	}
/**
 * 保存图片到sd卡
 * @param jpegData
 * @param width
 * @param height
 * @param orientation
 * @param tag
 * @return
 */
	private String savePanorama(byte[] jpegData, int width, int height,
			int orientation, int tag) {
		if (CSStaticData.DEBUG)
			Log.e("savePanorama", "start");
		if (jpegData != null) {
			String imagePath;
			if (tag == 0) {
				imagePath = "photo_"
						+ PanoUtil.createName("hhmmss", mTimeTaken) + "L";
			} else if (tag == 1) {
				imagePath = "photo_"
						+ PanoUtil.createName("hhmmss", mTimeTaken) + "R";
			} else {
				imagePath = "photo_"
						+ PanoUtil.createName("hhmmss", mTimeTaken);
			}

			String aString = Storage.addImage(this, this.getContentResolver(),
					imagePath, mTimeTaken, null, orientation, jpegData, width,
					height);
			videoSurfaceView.getPicFileList().add(aString);

			return aString;

		}
		return null;
	}
/**
 * 标记全景拍摄时后台线程是否在运行
 */
	private void onBackgroundThreadFinished() {
		mThreadRunning = false;
	}
/**
 * 显示生成的全景图片
 * @param bitmap
 */
	private void showFinalMosaic(Bitmap bitmap) {
		if (bitmap != null) {
			mImageView.setImageBitmap(bitmap);
		}
	}
/**
 * 设置全景最佳拍摄参数
 * @param supportedSizes
 * @param need4To3
 * @param needSmaller
 * @return
 */
	private boolean findBestPreviewSize(List<Size> supportedSizes,
			boolean need4To3, boolean needSmaller) {
		int pixelsDiff = DEFAULT_CAPTURE_PIXELS;
		boolean hasFound = false;
		for (Size size : supportedSizes) {
			int h = size.height;
			int w = size.width;
			// we only want 4:3 format.
			int d = DEFAULT_CAPTURE_PIXELS - h * w;
			if (needSmaller && d < 0) { // no bigger preview than 960x720.
				continue;
			}
			if (need4To3 && (h * 4 != w * 3)) {
				continue;
			}
			d = Math.abs(d);
			if (d < pixelsDiff) {
				mPreviewWidth = w;
				mPreviewHeight = h;
				pixelsDiff = d;
				hasFound = true;
			}
		}
		return hasFound;
	}
/**
 * 为全景拍摄分配内存
 */
	private void initMosaicFrameProcessorIfNeeded() {
		if (mMosaicFrameProcessor == null) {
			if (CSStaticData.DEBUG)
				Log.e(Tag, "[initMosaicFrameProcessorIfNeeded]------分配全景资源");
			if (m_is3D) {
				mMosaicFrameProcessor = new MosaicFrameProcessor(
						mPreviewWidth / 2, mPreviewHeight,
						getPreviewBufSize() / 2);
			} else {
				mMosaicFrameProcessor = new MosaicFrameProcessor(mPreviewWidth,
						mPreviewHeight, getPreviewBufSize());
			}
			mMosaicFrameProcessor.initialize(m_is3D);
		}

	}

	public int getPreviewBufSize() {

		PixelFormat pixelInfo = new PixelFormat();
		PixelFormat.getPixelFormatInfo(videoSurfaceView.getPreviewFormat(),
				pixelInfo);
		// TODO: remove this extra 32 byte after the driver bug is fixed.
		return (mPreviewWidth * mPreviewHeight * pixelInfo.bitsPerPixel / 8) + 32;
	}

	private void startCameraPreview() {
		// If we're previewing already, stop the preview first (this will blank
		// the screen).
		if (CSStaticData.DEBUG)
			Log.e("startCameraPreview", "ok");
		try {
			videoSurfaceView.stopPreview();
			videoSurfaceView.startPreview();
		} catch (Throwable ex) {
			// mCameraDevice.release();
		}

	}
/**
 * 在3D模式下传递预览帧，进行全景拍摄
 * @param prebyte
 */
	public void runMosaicCapture(byte[][] prebyte) {
		mMosaicFrameProcessor.processFrame(prebyte[0], prebyte[1]);
	}
	/**
	 * 在2D模式下传递预览帧，进行全景拍摄
	 * @param prebyte
	 */
	public void runMosaicCapture2D(byte[] prebyte) {
		mMosaicFrameProcessor.processFrame(prebyte);
	}
/**
 * 开设全景拍摄，更新UI及状态量
 */
	public void startCapture() {
		mCancelComputation = false;
		m_btn_panorama_sparepower.setVisibility(View.VISIBLE);
		int widthm = videoSurfaceView.getPictureSize().width;
		int heightm = videoSurfaceView.getPictureSize().height;
		if (CSStaticData.DEBUG)
			Log.e(Tag, "预览的尺寸为=" + widthm + "*" + heightm);
		videoSurfaceView.setPreviewCallback(WiCameraActivity.this);
		videoSurfaceView.PlaySounds(R.raw.camera_focus);
		if (mRecordSound != null)
			mRecordSound.play();
		resetPanoProgressToInit();
		mPanoProgressLayout.setVisibility(View.VISIBLE);
		m_button.setBackgroundResource(R.drawable.panorama_stop);
		mTimeTaken = System.currentTimeMillis();
		mCaptureState = CAPTURE_STATE_MOSAIC;
		mOrientationSensor.setEnable();
		mMosaicFrameProcessor
				.setProgressListener(new MosaicFrameProcessor.ProgressListener() {
					@Override
					public void onProgress(boolean isFinished,
							float panningRateX, float panningRateY,
							float progressX, float progressY) {
						if (isFinished || mOrientationSensor.isAroundComplete) {
							stopCapture(false);
						} else {
							updateProgress(panningRateX, progressX, progressY);
						}

					}
				});

	}
/**
 * 更新全景拍摄进度
 * @param panningRate
 * @param progressX
 * @param progressY
 */
	private void updateProgress(float panningRate, float progressX,
			float progressY) {
		if (Math.abs(mHorizontalViewAngle * panningRate) > 20)
			mPanoramaProgressIndicator.setIsTofast(true);
		else {
			mPanoramaProgressIndicator.setIsTofast(false);
		}

		if (currentFrameCount < mMosaicFrameProcessor.curframe) {
			// 由于中帧数为200，而进度条最大进度为100，所以要缩小
			mText.setText("current frame count: "
					+ mMosaicFrameProcessor.curframe);
			int derection = mPanoramaProgressIndicator.setProgress(
					mMosaicFrameProcessor.curframe ,
					(int) (progressX * mHorizontalViewAngle));
			// videoSurfaceView.PlaySounds(R.raw.di);
			if (mRecordSound != null)
				mRecordSound.play();
			if (derection == PanoramaProgressIndicator.DIRECTION_LEFT) {
				mPanoProgressLeft.setBackgroundResource(R.drawable.l_arrow);
				mOrientationSensor.setDirect(-1);
			} else if (derection == PanoramaProgressIndicator.DIRECTION_RIGHT) {
				mPanoProgressRight.setBackgroundResource(R.drawable.r_arrow);
				mOrientationSensor.setDirect(1);
			}
			currentFrameCount = mMosaicFrameProcessor.curframe;
		}

	}

	private void runBackgroundThread(Thread thread) {
		mThreadRunning = true;
		thread.start();
	}
/**
 * 全景拍摄使用的数据保存类
 * @author WH1107011
 *
 */
	private class MosaicJpeg {
		public MosaicJpeg(byte[] data, int width, int height) {
			this.data = data;
			this.width = width;
			this.height = height;
			this.isValid = true;
		}

		public MosaicJpeg() {
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

	private void releaseCamera() {
		try {
			videoSurfaceView.releaseCamera();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
/**
 * 停止全景拍摄
 * @param aborted  是否放弃全景合成
 */
	private void stopCapture(boolean aborted) {
		m_btn_panorama_sparepower.setVisibility(View.GONE);
		if (!aborted) {
			videoSurfaceView.PlaySounds(R.raw.camera_focus);
		}
		mOrientationSensor.resetToInit();
		// mImageView.setVisibility(View.VISIBLE);
		mCaptureState = CAPTURE_STATE_VIEWFINDER;
		m_button.setBackgroundResource(R.drawable.panorama_stop_click);
		mMosaicFrameProcessor.setProgressListener(null);
		videoSurfaceView.stopPreview();
		videoSurfaceView.setPreviewCallback(null);
		if (!aborted && !mThreadRunning) {
			runBackgroundThread(new Thread() {
				@Override
				public void run() {
					if (mIsBlendingThumbImage) {
						MosaicJpeg jpeg = generateFinalMosaic(false, false);
						if (jpeg != null && jpeg.isValid) {
							Bitmap bitmap = null;
							bitmap = BitmapFactory.decodeByteArray(jpeg.data,
									0, jpeg.data.length);
							mMainHandler.sendMessage(mMainHandler
									.obtainMessage(
											MSG_LOW_RES_FINAL_MOSAIC_READY,
											bitmap));
						} else {
							mMainHandler.sendMessage(mMainHandler
									.obtainMessage(MSG_RESET_TO_PREVIEW));
						}
					} else {
						mMainHandler.sendMessage(mMainHandler.obtainMessage(
								MSG_LOW_RES_FINAL_MOSAIC_READY, null));
					}
				}
			});
		}
	}
/**
 * 进行全景合成
 * @param highRes 
 * @param isleft
 * @return
 */
	public MosaicJpeg generateFinalMosaic(boolean highRes, boolean isleft) {
		byte[] imageData;
		if (isleft) {
			// 创建左边的图片
			if (mMosaicFrameProcessor.createMosaic(highRes, true) == Mosaic.MOSAIC_RET_CANCELLED) {
				if (CSStaticData.DEBUG)
					Log.e(TAG, "failed to generateFinalMosaic(left image)");
				return null;
			}
			imageData = mMosaicFrameProcessor.getFinalMosaicNV21();
		} else {
			// 创建右边的图片
			if (mMosaicFrameProcessor.createMosaic(highRes, false) == Mosaic.MOSAIC_RET_CANCELLED) {
				if (CSStaticData.DEBUG)
					Log.e(TAG, "failed to generateFinalMosaic(left image)");
				return null;
			}
			imageData = mMosaicFrameProcessor.getFinalMosaicNV21();
		}

		if (imageData == null) {
			if (CSStaticData.DEBUG)
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
		Log.v(TAG, "[generateFinalMosaic]-------------->ImLength = " + (len)
				+ ", W = " + width + ", H = " + height);

		if (width <= 0 || height <= 0) {
			if (CSStaticData.DEBUG)
				Log.e(TAG, "width|height <= 0!!, len = " + (len) + ", W = "
						+ width + ", H = " + height);
			return new MosaicJpeg();
		}
		YuvImage yuvimage = new YuvImage(imageData, ImageFormat.NV21, width,
				height, null);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		yuvimage.compressToJpeg(new Rect(0, 0, width, height), 100, out);
		try {
			out.close();
		} catch (Exception e) {
			Log.e(TAG, "Exception in storing final mosaic", e);
			return new MosaicJpeg();
		}
		if (out.toByteArray().length == 0)
			return new MosaicJpeg();
		return new MosaicJpeg(out.toByteArray(), width, height);
	}
   /**
    * 更新全景进度条为拍摄初始状态
    */
	public void resetPanoProgressToInit() {
		mPanoramaProgressIndicator.reset();
		mPanoProgressLeft.setBackgroundResource(R.drawable.l_arrow_disappear);
		mPanoProgressRight.setBackgroundResource(R.drawable.r_arrow_disappear);
		mPanoProgressLeft.setVisibility(View.VISIBLE);
		mPanoProgressRight.setVisibility(View.VISIBLE);
		// panoramaToCamera();
		// mPanoProgressLayout.setVisibility(View.INVISIBLE);
	}
	   /**
	    * 更新全景进度条为合成初始状态
	    */
	public void resetPanoProgressToReport() {
		mPanoramaProgressIndicator.reset();
		mPanoProgressLeft.setVisibility(View.INVISIBLE);
		mPanoProgressRight.setVisibility(View.INVISIBLE);
		mPanoProgressLayout.setVisibility(View.VISIBLE);
		mPanoramaProgressIndicator.reset();
		mPanoramaProgressIndicator.setType(1);
	}
/**
 * 取消高品质全景合成
 */
	private void cancelHighResComputation() {
		if (CSStaticData.DEBUG)
			Log.e(Tag, "[cancelHighResComputation]----->取消合成命令发出");
		mCancelComputation = true;
		synchronized (mWaitObject) {
			mWaitObject.notify();
		}
	}
/**
 * 更新合成进度
 */
	public void reportProgress() {
		flag_reportprogress = true;
		resetPanoProgressToInit();
		resetPanoProgressToReport();
		Thread t = new Thread() {
			@Override
			public void run() {
				while (flag_reportprogress) {
					final int progress = mMosaicFrameProcessor.reportProgress(
							true, mCancelComputation);

					try {
						synchronized (mWaitObject) {
							mWaitObject.wait(50);
						}
					} catch (InterruptedException e) {
						throw new RuntimeException(
								"Panorama reportProgress failed", e);
					}
					runOnUiThread(new Runnable() {
						public void run() {
							if (!mCancelComputation) {
								if (progress > 95) {
									if (mMsgBox_Panoramacancel.isShown())
										mMsgBox_Panoramacancel.hide();
								}
								if (progress > 99) {
									flag_reportprogress = false;
									resetPanoProgressToInit();
									mPanoProgressLayout
											.setVisibility(View.VISIBLE);
								} else {
									mText.setText(" save progress:" + progress);
									if (flag_reportprogress)
										mPanoramaProgressIndicator
												.setProgress(progress);

								}
							} else {
								flag_reportprogress = false;
								resetPanoProgressToInit();
								mPanoProgressLayout.setVisibility(View.VISIBLE);
							}
						}
					});
				}
			}
		};
		t.start();
	}
/**
 * 处理原始数据，将预览得到的nv21格式的数据拆分为左右图
 * @param data
 * @param w
 * @param h
 * @return
 */
	public static byte[][] rawoperation(byte[] data, int w, int h) {
		byte[][] m = new byte[2][1];
		m[0] = new byte[(int) (w / 2 * h * 3 / 2)];
		m[1] = new byte[(int) (w / 2 * h * 3 / 2)];
		int index = 0;
		int index1 = 0;
		int index2 = 0;
		int data_length = data.length;
		while (index < data_length) {
			if (index % w < w / 2) {
				m[0][index1] = data[index];
				index1++;
			} else {
				m[1][index2] = data[index];
				index2++;
			}
			index++;
		}
		return m;
	}

	/**
	 * 將nv21格式的源數據拆分成左右兩張圖片。
	 * 
	 * @param data
	 * @param w
	 * @param h
	 * @return 左右圖byte數組
	 */
	public static byte[][] rawSeperate(byte[] data, int w, int h) {
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
		while (index < rownum) {
			srcBuffer.get(dst, 0, w);
			lBuffer.put(dst, 0, halfwidth);
			rBuffer.put(dst, halfwidth, halfwidth);
			index++;
		}
		return double_image;
	}
/**
 *  预览帧回调，设置全景参数
 */
	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		if (isPanoramaMode) {
			if (mCaptureState == CAPTURE_STATE_MOSAIC) {
				if (m_is3D) {
					runMosaicCapture(rawSeperate(data, mPreviewWidth,
							mPreviewHeight));
				} else {
					runMosaicCapture2D(data);
				}
			}
		}

	}

	public class MBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			System.out.println("SDCardBroadcastReceiver 被注册");
			String action = arg1.getAction();
			// 当SD卡插入时
		}

	}
 /**
  * 初始化全景拍摄多用的UI控件
  */
	public void initPanoramaUI() {

		m_panorama_relativelayout = (RelativeLayout) findViewById(R.id.control_relativelayout);
		m_button = (Button) findViewById(R.id.button);
		mPanoProgressLayout = (RelativeLayout) findViewById(R.id.panoprogress_relativelayout);
		mPanoramaProgressIndicator = (PanoramaProgressIndicator) findViewById(R.id.panoramaprogressIndicator);
		mPanoProgressLeft = (ImageView) findViewById(R.id.panoprogressleft);
		mPanoProgressRight = (ImageView) findViewById(R.id.panoprogressright);
		mPanoProgressLayout.setVisibility(View.INVISIBLE);
		m_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mThreadRunning) {
					// mAlertDialog.show();
					return;
				}
				switch (mCaptureState) {
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
		mImageView = (ImageView) findViewById(R.id.imageView1);
		mImageView.setVisibility(View.GONE);
		mText = (TextView) findViewById(R.id.shenma);
		mText.setVisibility(View.INVISIBLE);
		mMainHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case MSG_LOW_RES_FINAL_MOSAIC_READY:
					if (CSStaticData.DEBUG)
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

					onBackgroundThreadFinished();
					m_btn_camera_review_play_video.setVisibility(View.GONE);
					m_btn_camera_review_play_video.setAlpha(0);
					btn_camera_review_play_videoparent.setVisibility(View.GONE);
					if (isPanoramaImage(m_currentpano_filepath)) {
						callPanoramaViewer(m_currentpano_filepath, 5005);
						return;
					}
					m_isReview = true;
					isShowReview(true);
					break;
				case MSG_UPDATE_THUMBNALL:
					StoredData.saveString(StoredData.M_REVIEWFILEPATH,
							m_currentpano_filepath);
					Bitmap bit = OperationFile.fitSizeImg(
							m_currentpano_filepath, screenHeight / 2,
							screenWidth / 2, screenWidth / 2);
					m_iv_camera_newpic_thumbnails.setImageBitmap(bit);
					WiCameraActivity.newpic_thumbnails_parent
							.setVisibility(View.VISIBLE);

					break;
				case MSG_TURNTO_CAMERA:
					m_al_camera_overlayui.setVisibility(View.VISIBLE);
					boolean isGridShow = StoredData.getBoolean(
							StoredData.M_GRIDDISINFINDER, false);
					if (isGridShow) {
						mSubMenuStateMap.put(CAMERA_GRIDDISPLAY_START_ID, 0);
						m_aux_camera_auxiliaryline.setVisibility(View.VISIBLE);

						m_aux_camera_auxiliaryline.setLineType(2);
					} else {
						mSubMenuStateMap.put(CAMERA_GRIDDISPLAY_START_ID, 1);
						m_aux_camera_auxiliaryline.setVisibility(View.GONE);
						// m_aux_camera_auxiliaryline.setLineType(0);
					}
					if (mCaptureState == CAPTURE_STATE_MOSAIC) {
						stopCapture(true);
					}
					onBackgroundThreadFinished();
					reset();
					startCameraPreview();
					m_panorama_relativelayout.setVisibility(View.GONE);
					isPanoramaMode = false;
					resetPanoProgressToInit();

					break;
				case MSG_GENERATE_FINAL_MOSAIC_ERROR:
					onBackgroundThreadFinished();

					break;
				case MSG_RESET_TO_PREVIEW:
					if (CSStaticData.DEBUG)
						Log.e(TAG, "MSG_RESET_TO_PREVIEW");

					panoramaToCamera();
					resetPanoProgressToInit();
					onBackgroundThreadFinished();
					reset();
					mImageView.setVisibility(View.GONE);
					startCameraPreview();
					mText.setText("start another one ");
				}
				// clearMosaicFrameProcessorIfNeeded();
			}
		};

		resetPanoProgressToInit();
		mPanoProgressLayout.setVisibility(View.VISIBLE);
	}
    /**
     * 判断一张图片是否为全景图片
     * @param path
     * @return
     */
	private boolean isPanoramaImage(String path) {
		if (path == null)
			return false;
		Options opts = new Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, opts);
		int width = opts.outWidth;
		int height = opts.outHeight;
		boolean isJps = path.toLowerCase().equals(".jps");
		if (isJps) {
			width = width / 2;
		}
		if (height != 0 && (width / height) >= 3 && width >= 1500) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 调用全景浏览器
	 * 
	 * @param path
	 */
	private void callPanoramaViewer(String path, int requestCode) {
		panoramaToCamera();
		resetPanoProgressToInit();
		onBackgroundThreadFinished();
		reset();
		Intent intent = new Intent();
		intent.setClass(WiCameraActivity.this,
				com.wistron.WiViewer.Panorama360Activity.class);
		intent.putExtra("cmd", "pano");
		intent.putExtra("filePath", path);
		intent.putExtra("fileList", videoSurfaceView.getPicFileList());
		startActivity(intent);

		// WiCameraActivity.this.onDestroy();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		super.onBackPressed();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		// Log.e(TAG, "onSensorChanged");
		float alpha = 0.5f;
		m_sensor_pretime++;
		if (m_sensor_pretime < 5) {
			gravity[0] = event.values[0];
			gravity[1] = event.values[1];
			gravity[2] = event.values[2];
			return;
		}

		gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
		gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
		gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

		mPanoramaProgressIndicator.convertSensor(Math.toRadians(gravity[1]));
	}

	public void OnCancelClicked() {
		mMsgBox_Panoramacancel.setRotation(-ldegree);
		mMsgBox_Panoramacancel.show();
	}

	// 放大缩小图片
	private Bitmap zoomBitmap(Bitmap bitmap, float w, float h) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidht = ((float) w / width);
		float scaleHeight = ((float) h / height);
		matrix.postScale(scaleWidht, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		return newbmp;
	}

	class ShotAsyncTask extends AsyncTask<Void, Void, Void> {
		int type;

		public ShotAsyncTask(int type) {
			// TODO Auto-generated constructor stub
			this.type = type;
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			if (type == camera_captue) {
				sinagleShot();
				m_ch_camera_time_count1.setBase(SystemClock.elapsedRealtime());
				m_ch_camera_time_count1.stop();
			}
			if (type == camera_continus) {

			}
			return null;
		}
	}
}
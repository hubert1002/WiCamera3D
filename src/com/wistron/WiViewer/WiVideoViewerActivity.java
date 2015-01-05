package com.wistron.WiViewer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.tridef3d.hardware.Display3D;
import com.tridef3d.widget.VideoView3D;
import com.wistron.StereoUI.CaptionsResolve;
import com.wistron.StereoUI.FileInfoMsgBox;
import com.wistron.StereoUI.MsgBox;
import com.wistron.StereoUI.ShareMenu;
import com.wistron.StereoUI.SlideButton;
import com.wistron.StereoUI.SlideButton.OnCheckedChangedListener;
import com.wistron.WiCamera.InterSurfaceView;
import com.wistron.WiCamera.WiCameraActivity;
import com.wistron.WiGallery.AsyncFileProvider;
import com.wistron.WiGallery.FavoriteDBAdapter;
import com.wistron.WiGallery.WiGalleryOpenGLRenderer;
import com.wistron.swpc.wicamera3dii.R;
import com.wistron.swpc.wicamera3dii.R.color;

import Utilities.CSStaticData;
import Utilities.FileTypeHelper;
import android.R.integer;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.VideoView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import android.widget.Toast;

@SuppressWarnings("deprecation")
public class WiVideoViewerActivity extends Activity implements
		OnGestureListener
{

	final static String TAG = "3D Media Player";

	final static int PLAYBACK_STATE_PLAY = 0x01; // 定义播放器状态
	final static int PLAYBACK_STATE_PAUSE = 0x02;
	final static int PLAYBACK_STATE_STOP = 0x03;
	final static int PLAYBACK_STATE_UNKNOWN = 0x04;

	final static int CONTROL_HANDLER_APPEAR_CTRLS = 0x10; // 通过handler控制按钮消失隐藏
	final static int CONTROL_HANDLER_HIDE_CTRLS = 0x11;
	final static int CONTROL_HANDLER_DISPLAY_VIDEO_INFO = 0x12;
	final static int CONTROL_HANDLER_HIDE_VIDEO_INFO = 0x13;
	final static int CONTROL_HANDLER_PROGRESS_CHANGED = 0x14;
	final static int CONTROL_HANDLER_PROGRESS_STOP = 0x15;
	final static int CONTROL_HANDLER_RESET_PROGRESS = 0x16;

	// final static int CONTROL_HANDLER_APPEAR_CTRLS_SYS = 0x17; // 系统状态栏隐藏

	final static int CONTROL_HANDLER_HIDEFRAMESEEKBAR_DELAY = 0x50;
	final static int CONTROL_HANDLER_SHOWFRAMESEEKBAR = 0x51;
	final static int BROADCAST_HANDLER_ADJUST_VOLUME = 0x20;
	public final static int WiVideoViewerActivityID = 0x76114;
	private static final long DELAY_HIDE_TIME = 3000; // 按钮隐藏时间
	private static final int CTRL_OFFSET = 2; //
	private static final int TEXT_SIZE = 22; //
	private ArrayList<String> mDeleteList = new ArrayList<String>();
	private boolean m_isplay_return = false;
	private int m_interrupt_process = 0;
	private int m_screen_width = 0;
	private int m_screen_height = 0;
	private int m_video_width = 0;
	private int m_video_height = 0;
	public static int mMainListDisablePosition = 3;
	private boolean mIsVideoSizeKnown = false;
	private boolean mIsVideoReadyToBePlayed = false;
	private int mVideoWidth;
	private int mVideoHeight;
	// 喜好列表
	private List<String> mFavoriteInfoList;
	private int mFavoriteListCount = 0;
	private int m_state_playback = 0x01; // 记录播放器状态
	private int m_current_process = 0; // 当前播放进度
	private int m_duration_process = 0; // 当前视频长度
	private int m_duration_frameseek = 0;
	private int m_max_volume = 0;
	private VideoInfo m_video_info = null;
	private boolean m_control_isshow = true;
	private boolean isMute = false;
	private boolean isFullScreen = true;
	private boolean isFrameSeekbarModeChanged = false;
	private VideoService m_media_player = null;
	private AudioManager m_audio_manager = null;
	// private SurfaceView m_surfaceview = null;
	// private SurfaceHolder m_surfaceholder = null;
	MediaFilePicker m_videopicker; // 视频文件列表维护器
	private FileInputStream m_fin = null; //
	private String fileName = null;
	// private String videoName=null;
	public static Handler m_handler_broadcast = null;
	private boolean m_ispause = false;
	private boolean m_allow_hide_share = true;

	private SlideButton m_bt_3dSwitch;
	private Button m_bt_cameraSwitch;
	private Button m_bt_more;
	private Button m_bt_mute;
	private Button m_bt_play;
	private Button m_bt_fullScreen;
	private SeekBar m_sbr_Normal;
	private SeekBar m_sbr_FrameSeekBar;
	private RelativeLayout m_relativelayout_frameseekbar;
	private TextView m_tv_during_normalseekbar;
	private TextView m_tv_during_frameseekbar;
	private RelativeLayout m_relativelayout_normalseekbar;
	private RelativeLayout m_relativelayout_controlLayout;
	private RelativeLayout m_relativelayout_surfaceview;
	private ShareMenu mShareMenu;
	private MsgBox mMsgBox;
	private FileInfoMsgBox mMsgBox_info;
	private RelativeLayout m_popupLayout;
	private RelativeLayout m_captionLayout;
	// 菜单中的资源id
	private int[][] mMoreMenuResId = null, mSeekBarModeResId = null,
			mLanguageEcodingResId = null, mGalleryMoveToMenuResId = null;
	private ListView mMenuListView = null, mSubMenuListView = null;
	private ViewerListAdapter mMenuListAdapter = null, mSubListAdapter = null;
	private HashMap<Integer, Integer> mSubMenuStateMap;
	public static int mListPosition = -1;
	public static int mSubListPosition = -1;
	private XmlPullParser mTextColorWhite = null;
	private XmlPullParser mTextColorGreen = null;
	public static ColorStateList COLORSTATELIST_WHITE = null;
	public static ColorStateList COLORSTATELIST_GREEN = null;
	private Context mContext = null;
	private RelativeLayout mMenuLayout = null,// 菜单所在layout
			mSubMenuLayout = null;// 子菜单所在layout

	private TextView mSubListTitle = null;
	private static final int SUBMENU_SEEKBARMODE_START_ID = 0x110;
	private static final int SUBMENU_LANGUAGEENCODING_START_ID = 0x120;
	private static final int SUBMENU_MOVETO_INTERNAL = 0x00140;
	public static boolean mIsVideoViewerList = false;
	private GridView gv_base;
	private Gallery gv_sub;
	private Handler m_VideoFrameHandler;
	private boolean isVideoFrameLoadComplete = true;
	final static int MSG_SEEK_BASE_READY = 8;
	final static int MSG_SEEK_SON_READY = 7;
	private LoadVideoFrameTask loadInnerFrametaskImageTask;
	private VideoFrameAdapter mFrameAdapter_base;
	private VideoFrameAdapter mFrameAdapter_sub;
	private boolean m_isLandScape;
	private GestureDetector m_gesturedetector;
	private RelativeLayout m_MoreLayout;
	private RelativeLayout m_relativelayout_frameseekbar_small;
	private android.widget.RelativeLayout.LayoutParams m_layoutparams_frameseekbar_small;
	int m_layoutparams_frameseekbar_small_off;
	int m_init_playmode = 0;
	// 字幕解析
	private CaptionsResolve mCaptionsResolve = null;
	private String mCaptionPath = null;
	private String mCharSet = null;
	private boolean isBaseFramebarHasShow = false;//标记帧预览底层图片是否已经加载
	private boolean m_is3DVideo = false; // 指示当前文件是否为3D视频(其实是标示视频是否会被芯片识别为3D视频)
	private int m_seekbar_mode = 0;
	private boolean isLaunchedFromInner = true;
	private int m_frameseek_tempprogress = 0;
	public static MsgBox msgBox_disappearautoBox = null;
	// public boolean is3Dmode=false;//判断当前屏幕是否开启了3D模式
	private RelativeLayout m_relativelayout_content;// 用于放置VideoView
	private VideoView m_videoview2D;
	private VideoView3D m_videoview3D;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		winParams.buttonBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_OFF;
		win.setAttributes(winParams);
		TDStaticData.SCREEN_WIDTH = getWindowManager().getDefaultDisplay()
				.getWidth();
		TDStaticData.SCREEN_HEIGHT = getWindowManager().getDefaultDisplay()
				.getHeight();
		TDStaticData.SCREEN_WIDTH_ORG = TDStaticData.SCREEN_WIDTH;
		TDStaticData.SCREEN_HEIGHT_ORG = TDStaticData.SCREEN_HEIGHT;
		m_screen_width = TDStaticData.SCREEN_WIDTH;
		m_screen_height = TDStaticData.SCREEN_HEIGHT;
		m_screen_width = 800;
		m_screen_height = 480;
		if (m_screen_width > m_screen_height)
		{
			m_isLandScape = true;
		} else
		{
			m_isLandScape = false;
		}
		setContentView(R.layout.wivideovieweractivity_main);
		// 初始化按钮控件
		initializeControls();
		initFrameSeekbar();
		// 添加监听器
		initializeListeners();
		m_gesturedetector = new GestureDetector(this);
		initData();
		initListViewListenner();
		lookupFavoriteInfo();
	}

	private void initFrameSeekbar()
	{
		// TODO Auto-generated method stub
		m_relativelayout_frameseekbar_small = (RelativeLayout) findViewById(R.id.frameseekbar_small_relativelayout);
		m_relativelayout_frameseekbar_small.setVisibility(View.INVISIBLE);
		m_layoutparams_frameseekbar_small = (android.widget.RelativeLayout.LayoutParams) m_relativelayout_frameseekbar_small
				.getLayoutParams();
		m_layoutparams_frameseekbar_small_off = m_layoutparams_frameseekbar_small.leftMargin;

		mFrameAdapter_sub = new VideoFrameAdapter(getBaseContext(), 31, false);
		gv_sub.setAdapter(mFrameAdapter_sub);
		gv_sub.setUnselectedAlpha(100);
		gv_sub.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3)
			{

				//
				// if (!isVideoFrameLoadComplete)
				// {
				// loadInnerFrametaskImageTask.cancel(true);
				// }
				// LoadVideoFrameTask.Callback callback = new
				// LoadVideoFrameTask.Callback()
				// {
				// @Override
				// public void onComplete(long time)
				// {
				// Log.e(TAG, "???曄??冽" + time);
				// isVideoFrameLoadComplete = true;
				// }
				// };
				// loadInnerFrametaskImageTask = new LoadVideoFrameTask(
				// WiVideoViewerActivity.this, callback,
				// m_videopicker.getCurrentFileName(),
				// m_VideoFrameHandler);
				// loadInnerFrametaskImageTask.execute(
				// m_duration_process, 7,
				// (int) (rate * m_duration_process), 1,10);
				// isVideoFrameLoadComplete = false;

				if (arg2 < 10)
				{
					if (arg2 - 1 >= 0)
					{
						if (mFrameAdapter_sub.mBitmaps[arg2 - 1] == null)
						{
							LoadVideoFrameTask.Callback callback = new LoadVideoFrameTask.Callback()
							{
								@Override
								public void onComplete(long time)
								{
									if (CSStaticData.DEBUG)
										Log.e(TAG, "生成图片耗时" + time);
								}
							};
							LoadVideoFrameTask tempTask = new LoadVideoFrameTask(
									WiVideoViewerActivity.this, callback,
									m_videopicker.getCurrentFileName(),
									m_VideoFrameHandler);
							tempTask.execute(m_duration_process, 7,
									m_duration_frameseek, false, arg2 - 1, 5);
						}
					}

				} else if (arg2 > 10)
				{
					if (arg2 + 5 <= 30)
					{
						if (mFrameAdapter_sub.mBitmaps[arg2 + 5] == null)
						{
							LoadVideoFrameTask.Callback callback = new LoadVideoFrameTask.Callback()
							{
								@Override
								public void onComplete(long time)
								{
									if (CSStaticData.DEBUG)
										Log.e(TAG, "生成图片耗时" + time);
								}
							};
							LoadVideoFrameTask tempTask = new LoadVideoFrameTask(
									WiVideoViewerActivity.this, callback,
									m_videopicker.getCurrentFileName(),
									m_VideoFrameHandler);
							tempTask.execute(m_duration_process, 7,
									m_duration_frameseek, false, arg2 + 5);
						}
					}
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
				// TODO Auto-generated method stub

			}
		});
		if (m_isLandScape)
		{
			mFrameAdapter_base = new VideoFrameAdapter(getBaseContext(), 8,
					true);
		} else
		{
			mFrameAdapter_base = new VideoFrameAdapter(getBaseContext(), 5,
					true);
		}
		gv_base.setAdapter(mFrameAdapter_base);
		gv_sub.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				if (CSStaticData.DEBUG)
					Log.e(TAG, "setOnItemClickListener");
				if (m_duration_process != 0)
				{
					try
					{
						m_media_player
								.seekTo((int) (m_duration_frameseek + (position - 10) * 1000));
					} catch (Exception e)
					{
						if (CSStaticData.DEBUG)
							Log.e(TAG, "fail to seekto");
					}
				}
			}
		});
		gv_base.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{

			}
		});
		m_VideoFrameHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				switch (msg.arg1)
				{
				case MSG_SEEK_BASE_READY:
					Bitmap bitmap1 = (Bitmap) msg.obj;
					if (CSStaticData.DEBUG)
						Log.e("changkuan", "" + bitmap1.getWidth() + "*"
								+ bitmap1.getHeight());
					mFrameAdapter_base.mBitmaps[msg.arg2] = (Bitmap) msg.obj;
					mFrameAdapter_base.notifyDataSetChanged();
					break;
				case MSG_SEEK_SON_READY:
					Bitmap bitmap2 = (Bitmap) msg.obj;
					if (CSStaticData.DEBUG)
						Log.e("changkuan", "" + bitmap2.getWidth() + "*"
								+ bitmap2.getHeight());
					mFrameAdapter_sub.mBitmaps[msg.arg2] = (Bitmap) msg.obj;
					mFrameAdapter_sub.notifyDataSetChanged();
					break;

				}
			}
		};
	}

	private MediaPlayer.OnPreparedListener mOnPreparedListener = new OnPreparedListener()
	{

		@Override
		public void onPrepared(MediaPlayer mp)
		{
			// TODO Auto-generated method stub
			if (m_state_playback == PLAYBACK_STATE_PLAY)
			{
				if (mp != null)
				{
					m_ispause = false;
				}
			}
			if (m_state_playback == PLAYBACK_STATE_PAUSE)
			{
				if (mp != null)
				{
				  	m_ispause = true;
				}
			}
			m_state_playback = PLAYBACK_STATE_PLAY;
			updateDuration();
			m_video_width = mp.getVideoWidth();
			m_video_height = mp.getVideoHeight();
			TsetScreenSize();
			startVideoPlayback();
			if(m_ispause||m_isplay_return)
			{
				pauseMedia(true);
			}
			
//			if (m_ispause)
//			{
//				if (!m_isplay_return)
//				{
//					pauseMedia(true);
//				} else
//				{
//					m_isplay_return = false;
//				}
//			}

		}
	};
	private MediaPlayer.OnCompletionListener mOnCompletionListener = new OnCompletionListener()
	{

		@Override
		public void onCompletion(MediaPlayer mp)
		{
			// TODO Auto-generated method stub
			m_state_playback = PLAYBACK_STATE_STOP;
			stopMedia();
			resetDelayonFling();
			if (m_init_playmode == 0)
			{
				finish();
			} else
			{
				if (m_videopicker.getCurrentFileIndex() != m_videopicker
						.getFileCounts() - 1)
				{
					m_videopicker.getNextBitmap();
					resetPlayer(true, false);
					if (CSStaticData.DEBUG)
						Toast.makeText(
								WiVideoViewerActivity.this,
								"文件列表信息"
										+ (m_videopicker.getCurrentFileIndex() + 1)
										+ "/" + m_videopicker.getFileCounts(),
								1000).show();
				} else
				{
					finish();
				}

			}
		}
	};

	private MediaPlayer.OnErrorListener mOnErrorListener = new OnErrorListener()
	{

		@Override
		public boolean onError(MediaPlayer mp, int what, int extra)
		{
			// TODO Auto-generated method stub
			Log.v(TAG, "onError called");
			mp.reset();
			resetPlayer(false, true);
			resetDelayonFling();
			mMsgBox = new MsgBox(mContext);
			mMsgBox.setMessage(R.string.video_warn_error);
			mMsgBox.setBackgound(R.drawable.main_menu_window_portrait);
			mMsgBox.setPositiveButton(R.string.video_warn_confirm,
					new OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							mMsgBox.hide();
							m_popupLayout.removeView(mMsgBox);
							// finish();
						}
					});
			mMsgBox.addToLayout(m_popupLayout);
			mMsgBox.show();
			return true;
		}
	};

	/**
	 * 重置播放器
	 * 
	 * @param refreshAll
	 *            是否保留播放进度，全屏，静音的设置
	 */
	private void initVideoView(boolean refreshAll)
	{
		// 只有在2D视频在3D模式下采用3D视频控件
		if (!m_is3DVideo)
		{
			// 使用3D控件
			if (m_videoview2D != null)
			{
				m_relativelayout_content.removeView(m_videoview2D);
				m_videoview2D = null;
			}
			if (m_videoview3D == null)
			{
				m_videoview3D = new Video3DService(WiVideoViewerActivity.this);
				m_relativelayout_content.addView(m_videoview3D);
			}
			m_media_player = (VideoService) m_videoview3D;
		} else
		{
			if (m_videoview3D != null)
			{
				m_relativelayout_content.removeView(m_videoview3D);
				m_videoview3D = null;
			}
			if (m_videoview2D == null)
			{
				m_videoview2D = new Video2DService(WiVideoViewerActivity.this);
				m_relativelayout_content.addView(m_videoview2D);
			}
			m_media_player = (VideoService) m_videoview2D;
		}
		initializeMediaPlayer(refreshAll);
	}

	@Override
	protected void onStart()
	{
		// TODO Auto-generated method stub
		Intent intent = WiVideoViewerActivity.this.getIntent();
		String intentPath = intent.getDataString();
		System.out.println("没转intentpath"+intentPath);
		if (intentPath != null)
		{
			// 从外部启动
			isLaunchedFromInner = false;
			intentPath = Uri.decode(intentPath);
			m_videopicker = new MediaFilePicker(intentPath);
			System.out.println("intentpath"+intentPath);
			//m_videopicker.getCurrentFileName();
			//m_videopicker
			m_init_playmode = 0;
		} else
		{
			// 从内部启动
			isLaunchedFromInner = true;
			String playmode = intent.getStringExtra("cmd");
			if (playmode != null && playmode.equals("sildeshow"))
			{
				m_init_playmode = 1;
			} else if (playmode != null && playmode.equals("play"))
			{
				m_init_playmode = 0;
			}
			// 获取当前文件路径
			String fileName = intent.getStringExtra("filePath");
			List<String> fileList = WiVideoViewerActivity.this.getIntent()
					.getStringArrayListExtra("fileList");
			if (fileName != null && fileList != null && fileList.size() > 0)
			{
				m_videopicker = new MediaFilePicker(fileList, fileName);
				update3DSwitchButton();
			} else
			{
				// 传入文件路径有误
				if (CSStaticData.DEBUG)
					Toast.makeText(WiVideoViewerActivity.this, "传入文件路径有误",
							1000).show();
				finish();
				// try
				// {
				// m_videopicker=new
				// MediaFilePicker(TDStaticData.VIEWMODE_VIDEO_VIEW, null);
				// } catch (IOException e)
				// {
				// e.printStackTrace();
				// }
			}
		}
		initStatusData();
		// 开始字幕解析
		startCaptionResolve();
		super.onStart();
	}

	public void update3DSwitchButton()
	{
		String mString = null;
		mString = m_videopicker.getCurrentFileName();
		if (mString != null)
		{
			m_is3DVideo = FileTypeHelper.isStereoVideoFileWithFilepath(
					WiVideoViewerActivity.this.getContentResolver(), mString);
		}
		m_bt_3dSwitch.setChecked(m_is3DVideo);
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		m_is3DVideo = FileTypeHelper.isStereoVideoFileWithFilepath(
				WiVideoViewerActivity.this.getContentResolver(),
				m_videopicker.getCurrentFileName());
		initVideoView(false);
		m_media_player.resume();
		if (isFrameSeekbarModeChanged)
		{
			m_seekbar_mode = 1;
			m_relativelayout_normalseekbar.setVisibility(View.INVISIBLE);
			m_relativelayout_frameseekbar.setVisibility(View.VISIBLE);
			if (!isBaseFramebarHasShow)
			{
				// 加载帧预览
				LoadVideoFrameTask.Callback callback = new LoadVideoFrameTask.Callback()
				{
					@Override
					public void onComplete(long time)
					{
						if (CSStaticData.DEBUG)
							Log.e(TAG, "生成图片耗时" + time);
					}
				};
				LoadVideoFrameTask task = new LoadVideoFrameTask(
						WiVideoViewerActivity.this, callback,
						m_videopicker.getCurrentFileName(), m_VideoFrameHandler);
				if (m_isLandScape)
				{
					task.execute(m_duration_process, 8, 0, true, 0);
				} else
				{
					task.execute(m_duration_process, 5, 0, true, 0);
				}
				isBaseFramebarHasShow = true;
			}
			if (isLoadSubFrames(m_duration_process))
			{
				resetSubImageToGivenTime(m_interrupt_process);
			} else
			{
				m_relativelayout_frameseekbar_small
						.setVisibility(View.INVISIBLE);
			}
			isFrameSeekbarModeChanged = false;
		}
		// 显示按钮
		m_handle_control.removeMessages(CONTROL_HANDLER_HIDE_CTRLS);
		m_handle_control.sendEmptyMessage(CONTROL_HANDLER_APPEAR_CTRLS);
		boolean bool = (mMsgBox != null && mMsgBox.isShown())
				|| (mMsgBox_info != null && mMsgBox_info.isShown());
		if (!bool)
			m_handle_control.sendEmptyMessageDelayed(
					CONTROL_HANDLER_HIDE_CTRLS, DELAY_HIDE_TIME);
		
		setControlEnable();
		boolean initstate = CSStaticData.g_is_3D_mode || m_is3DVideo;
		// CSStaticData.g_is_3D_mode=CSStaticData.g_is_3D_mode||is3DVideo;
		// if(CSStaticData.g_is_3D_mode){
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		// }else{
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
		// }
		m_bt_3dSwitch.setChecked(initstate);
		setScreenDimension(initstate);
		if (CSStaticData.DEBUG)
			Toast.makeText(
					WiVideoViewerActivity.this,
					"通过读取tag判定文件3D=" + m_is3DVideo + "g_is_3D_mode="
							+ CSStaticData.g_is_3D_mode, 3000).show();
		mListPosition = -1;
		if (mMenuListAdapter != null)
		{
			mMenuListAdapter.notifyDataSetChanged();
		}
		super.onResume();
	}

	public void setScreenDimension(boolean is3D)
	{
		String[] cmdTurnOn3D =
		{ "/system/bin/sh", "-c",
				"echo 1 > /sys/devices/platform/mipi_masterimage.513/enable_3d_barrier" };

		String[] cmdTurnOff3D =
		{ "/system/bin/sh", "-c",
				"echo 0 > /sys/devices/platform/mipi_masterimage.513/enable_3d_barrier" };

		// CSStaticData.g_is_3D_mode = is3D;
		try
		{
			if (is3D)
			{
				if (CSStaticData.DEBUG)
				{
					Log.w(TAG, "[setScreenDimension]设置屏幕为3D");
				}
				Runtime.getRuntime().exec(cmdTurnOn3D);
			} else
			{
				if (CSStaticData.DEBUG)
				{
					Log.w(TAG, "[setScreenDimension]设置屏幕为2D");
				}
				Runtime.getRuntime().exec(cmdTurnOff3D);
			}
		} catch (IOException exp)
		{
			if (CSStaticData.DEBUG)
			{
				Log.w(TAG, "[setScreenDimension]设置屏幕IOException");
			}
		} catch (SecurityException exp)
		{
			if (CSStaticData.DEBUG)
			{
				Log.w(TAG, "[setScreenDimension]设置屏幕SecurityException");
			}
		} catch (Exception exp)
		{
			if (CSStaticData.DEBUG)
			{
				Log.w(TAG, "[setScreenDimension]设置屏幕Exception");
			}
		}
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		// 暂停时保存播放进度
		if (m_state_playback == PLAYBACK_STATE_PLAY
				|| m_state_playback == PLAYBACK_STATE_PAUSE)
		{
			if (m_media_player != null)
			{
				try
				{
					m_interrupt_process = m_media_player.getCurrentPosition();
					Log.e(TAG, "[onPause]---------------->暂停时保存的进度为"
							+ m_interrupt_process);
					m_media_player.pause();
				} catch (Exception e)
				{
					if (CSStaticData.DEBUG)
						Log.e(TAG, "unnable to  getCurrentPosition  ");
				}

			}
		}
		releaseMediaPlayer();
		if(mMenuLayout.getVisibility()==View.VISIBLE)
		{
			mMenuLayout.setVisibility(View.GONE);
			if(m_isplay_return==true)
			{
				m_isplay_return=false;
				m_state_playback=PLAYBACK_STATE_PLAY;
			}
		}
		
		m_bt_more.setBackgroundResource(R.drawable.gallery_more_btn_selector);
		doCleanUp();
		if (CSStaticData.g_is_3D_mode || m_is3DVideo)
		{
			setScreenDimension(false);
		}

	}

	@Override
	protected void onStop()
	{
		// TODO Auto-generated method stub
		super.onStop();
		if (m_media_player != null)
		{
			try
			{
				m_media_player.stopPlayback();
			} catch (Exception e)
			{
				// TODO: handle exception

			}

		}
	}

	@Override
	protected void onDestroy()
	{
		releaseMediaPlayer();
		doCleanUp();
		if (m_audio_manager != null)
			m_audio_manager
					.unregisterMediaButtonEventReceiver(new ComponentName(
							"com.wistron.wigallery3d",
							"com.wistron.wigallery3d.MediaButtonBroadcastReceiver"));
		m_handle_control.removeMessages(CONTROL_HANDLER_PROGRESS_CHANGED);
		super.onDestroy();
	}

	/**
	 * 初始化播放器，
	 * 
	 * @param refreshAll
	 *            ，为true时销毁播放器播放状态
	 */
	private void initializeMediaPlayer(boolean refreshAll)
	{
		doCleanUp();
		if (refreshAll)
		{
			//取消帧预览
			isBaseFramebarHasShow=false;
			m_seekbar_mode = 0;
			mSubMenuStateMap.put(SUBMENU_SEEKBARMODE_START_ID, 0);
			m_relativelayout_normalseekbar.setVisibility(View.VISIBLE);
			m_relativelayout_frameseekbar.setVisibility(View.INVISIBLE);
			
			m_state_playback = PLAYBACK_STATE_PLAY;
			isFullScreen = true;
			m_bt_fullScreen
					.setBackgroundResource(R.drawable.gallery_screen_narrow_btn_selector);
			m_interrupt_process = 0;
			isMute = false;
		}
		try
		{
			if (m_audio_manager == null)
			{
				m_audio_manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			}
			m_media_player.setOnPreparedListener(mOnPreparedListener);
			m_media_player.setOnCompletionListener(mOnCompletionListener);
			m_media_player.setOnErrorListener(mOnErrorListener);
			if (m_videopicker.getCurrentFileName() != null)
				
				m_media_player.setVideoPath(m_videopicker.getCurrentFileName());
			if (CSStaticData.g_is_3D_mode)
			{
				m_media_player.force3DMode(Display3D.VERTICAL_INTERLACED);
			} else
			{
				m_media_player.force3DMode(Display3D.MODE_2D);
			}

			setMute(isMute);
		} catch (Exception e)
		{
			resetPlayer(false, true);
			resetDelayonFling();
			mMsgBox = new MsgBox(mContext);
			mMsgBox.setMessage(R.string.video_warn_failinit);
			mMsgBox.setBackgound(R.drawable.main_menu_window_portrait);
			mMsgBox.setPositiveButton(R.string.video_warn_confirm,
					new OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							// m_media_player.setDisplay(m_surfaceholder);
							mMsgBox.hide();
							m_popupLayout.removeView(mMsgBox);
						}
					});
			mMsgBox.addToLayout(m_popupLayout);
			mMsgBox.show();
		}
	}

	private void startVideoPlayback()
	{
		if (CSStaticData.DEBUG)
			Log.e(TAG, "当前播放进度为[m_interrupt_process]-------------->"
					+ m_interrupt_process);
		if (m_interrupt_process > 0)
		{
			m_media_player.seekTo(m_interrupt_process);
		}
		updateProcess();
		m_media_player.start();
		m_bt_play.setBackgroundResource(R.drawable.gallery_pause_btn_selector);
	}

	/**
	 * 初始化控件
	 */
	private void initializeControls()
	{
		// TODO Auto-generated method stub
		m_relativelayout_controlLayout = (RelativeLayout) findViewById(R.id.control_relativelayout);
		m_relativelayout_normalseekbar = (RelativeLayout) findViewById(R.id.normalseekbar_relativelayout);
		m_relativelayout_surfaceview = (RelativeLayout) findViewById(R.id.surfaceview_relativelayout);
		m_popupLayout = (RelativeLayout) findViewById(R.id.popup_relativelayout);
		m_captionLayout = (RelativeLayout) findViewById(R.id.captionlayout);
		m_relativelayout_content = (RelativeLayout) findViewById(R.id.surfaceview_content);
		m_MoreLayout = (RelativeLayout) findViewById(R.id.MenuLayout);//全屏布局
		m_bt_3dSwitch = (SlideButton) findViewById(R.id.toggleButtonDimension);
		m_bt_cameraSwitch = (Button) findViewById(R.id.button2);
		gv_base = (GridView) findViewById(R.id.Gridview02);
		gv_sub = (Gallery) findViewById(R.id.Gridview01);
		m_sbr_FrameSeekBar = (SeekBar) findViewById(R.id.progress_frameseekbar);
		Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.scrolling_bar_normal);
		BitmapDrawable mBitmapDrawable = new BitmapDrawable(mBitmap);
		m_sbr_FrameSeekBar.setThumb(mBitmapDrawable);
		m_sbr_FrameSeekBar.setMax(100);
		m_bt_more = (Button) findViewById(R.id.button1);
		m_bt_play = (Button) findViewById(R.id.button4);
		m_bt_mute = (Button) findViewById(R.id.button5);
		m_bt_fullScreen = (Button) findViewById(R.id.button6);
		m_sbr_Normal = (SeekBar) findViewById(R.id.progress);
		m_sbr_Normal.setMax(100);
		m_tv_during_normalseekbar = (TextView) findViewById(R.id.video_during);
		m_tv_during_frameseekbar = (TextView) findViewById(R.id.video_during_frameseekbar);
		m_relativelayout_frameseekbar = (RelativeLayout) findViewById(R.id.frameseekbar_relativelayout);
		m_relativelayout_frameseekbar.setVisibility(View.GONE);
		msgBox_disappearautoBox = new MsgBox(WiVideoViewerActivity.this);
		// msgBox_disappearautoBox.setMessage("...");
		// msgBox_disappearautoBox.hideDelay(2000);
		msgBox_disappearautoBox.setModelStatus(false);
		msgBox_disappearautoBox.addToLayout(m_popupLayout);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		if (mMsgBox != null && mMsgBox.isShown())
		{
			return mMsgBox.dispatchTouchEvent(ev);
		}
		if (mMsgBox_info != null && mMsgBox_info.isShown())
		{
			if (!mMsgBox_info.dispatchTouchEvent(ev))
			{
				if (m_isplay_return)
				{
					contiuneMedia();
					m_isplay_return = false;
				}
				hideButtonsWithDelay();
			}
			return true;
		}
		return super.dispatchTouchEvent(ev);
	}

	public void updateTimeIndicator(boolean isreset)
	{
		int myduring = m_duration_process;
		int time = m_current_process;
		if (isreset)
		{
			myduring = 0;
			time = 0;
		}
		String currentProgress = getStringFromTime(time);
		String totalDuring = getStringFromTime((int) Math.ceil(myduring));
		String myString = currentProgress + "/" + totalDuring;
		SpannableString ss = new SpannableString(myString);
		ss.setSpan(new ForegroundColorSpan(0xff88ee00), 0,
				currentProgress.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		ss.setSpan(new ForegroundColorSpan(Color.WHITE),
				currentProgress.length(), currentProgress.length()
						+ totalDuring.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		m_tv_during_normalseekbar.setText(ss);
		m_tv_during_frameseekbar.setText(ss);
	}

	public String getStringFromTime(int time)
	{
		time /= 1000;
		int minute;
		int second;
		minute = (int) (time) / 60;
		second = (int) (time) % 60;
		if (minute >= 100)
		{
			return String.format("%03d:%02d", minute, second);
		} else
		{
			return String.format("%02d:%02d", minute, second);
		}
	}

	/**
	 * 根据播放进度修改帧预览子进度条的位置
	 * 
	 * @param progress
	 * @return
	 */
	public int getOffsetMargin(int progress)
	{
		if (progress < 32)
		{
			return 0;
		} else if (progress < 68)
		{
			return 240 * (progress - 32) / 36;
		} else
		{
			return 240;
		}

	}

	/**
	 * 添加监听器
	 */
	private void initializeListeners()
	{
		if (CSStaticData.DEBUG)
			Log.e("initializeListeners", "ok");

		m_relativelayout_surfaceview
				.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
				{
					int times=0;
					public void onSystemUiVisibilityChange(int visibility)
					{
						Log.e(TAG, "[onSystemUiVisibilityChange]-------"+visibility);
						if (visibility == View.SYSTEM_UI_FLAG_VISIBLE)
						{
							if(times%3==0)
							{
								resetOnSystemUiShow();
							}
							times++;
						}
					}
				});

		m_relativelayout_surfaceview.setOnTouchListener(new OnTouchListener()
		{

			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if (m_state_playback == PLAYBACK_STATE_PLAY)
				{
					if (event.getAction() == MotionEvent.ACTION_DOWN)
					{
						Log.e(TAG, "onTouchEvent--------surfaceview");
						resetDelay();
					}
				}
				m_gesturedetector.onTouchEvent(event);
				return true;
			}
		});
		m_bt_3dSwitch.setOnChangedListener(new OnCheckedChangedListener()
		{
			@Override
			public void OnChecked(boolean isChecked)
			{
				int isLandscape = getRequestedOrientation();
				// if(isChecked){
				// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				// if(isLandscape == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
				// ||
				// isLandscape ==
				// ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
				// ||
				// isLandscape ==
				// ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT){
				// return;
				// }
				// }else{
				// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
				// }
				CSStaticData.g_is_3D_mode = isChecked;
				setScreenDimension(isChecked);
				if(m_media_player!=null)
				{
					if (CSStaticData.g_is_3D_mode)
					{
						m_media_player.force3DMode(Display3D.VERTICAL_INTERLACED);
					} else
					{
						m_media_player.force3DMode(Display3D.MODE_2D);
					}
				}
				
			}
		});
		m_bt_cameraSwitch.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent(WiVideoViewerActivity.this,
						com.wistron.WiCamera.WiCameraActivity.class);
				intent.putExtra("camera_cmd", "call_from_videoview");
				startActivity(intent);
				// finish();
			}
		});
		m_sbr_FrameSeekBar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
				{
					// boolean control=false;
					@Override
					public void onStopTrackingTouch(SeekBar seekBar)
					{
						if (m_isplay_return)
						{
							m_isplay_return = false;
							contiuneMedia();
							m_handle_control.sendEmptyMessageDelayed(
									CONTROL_HANDLER_HIDEFRAMESEEKBAR_DELAY,
									DELAY_HIDE_TIME);
						}
						if (isLoadSubFrames(m_duration_process))
						{
							resetSubImageToGivenTime(m_frameseek_tempprogress);
						} else
						{
							m_relativelayout_frameseekbar_small
									.setVisibility(View.INVISIBLE);
						}

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar)
					{
						if (m_state_playback == PLAYBACK_STATE_PLAY)
						{
							m_isplay_return = true;
							pauseMedia(false);
						}
						m_frameseek_tempprogress = 0;
					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser)
					{

						double rate = 0.0;
						if (fromUser)
						{
							if (m_media_player != null)
							{

								if (progress < 99)
								{
									rate = (float) progress / 100f;
									updateTimeIndicator(false);
								} else
								{
									rate = 0.99;
									updateTimeIndicator(false);
								}
								try
								{
									m_media_player
											.seekTo((int) (rate * m_duration_process));
									if (CSStaticData.DEBUG)
										Log.e(TAG,
												"[onProgressChanged]------->当前播放进度为"
														+ (int) (rate * m_duration_process));
								} catch (Exception e)
								{
									Log.e(TAG, "fail to seek");
								}
								m_frameseek_tempprogress = (int) (rate * m_duration_process);
							}

						}
						if (m_isLandScape)
						{
							m_layoutparams_frameseekbar_small.setMargins(
									m_layoutparams_frameseekbar_small_off
											+ getOffsetMargin(progress), 0, 0,
									0);
							m_relativelayout_frameseekbar_small
									.setLayoutParams(m_layoutparams_frameseekbar_small);
						}
					}
				});
		m_sbr_Normal.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{
			@Override
			public void onStopTrackingTouch(SeekBar seekBar)
			{
				if (m_isplay_return)
				{
					m_isplay_return = false;
					contiuneMedia();
				}

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar)
			{
				if (m_state_playback == PLAYBACK_STATE_PLAY)
				{
					m_isplay_return = true;
					pauseMedia(false);
				}
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser)
			{
				double rate = 0.0;
				if (fromUser)
				{
					if (CSStaticData.DEBUG)
						Log.e("onProgressChanged", "ok_______________________");
					if (m_media_player != null)
					{

						if (progress < 99)
						{
							rate = (float) progress / 100f;
							updateTimeIndicator(false);
						} else
						{
							rate = 0.99;
							updateTimeIndicator(false);
						}
						try
						{
							m_media_player
									.seekTo((int) (rate * m_duration_process));
						} catch (Exception e)
						{
							Log.e(TAG, "fail to seekto");
						}
					}
					// }

				}
			}
		});

		m_bt_play.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Tplay();
			}
		});

		m_MoreLayout.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				// TODO Auto-generated method stub
				if (mMenuLayout.getVisibility() == View.VISIBLE)
				{
					mMenuLayout.setVisibility(View.INVISIBLE);
					if (mShareMenu != null && mShareMenu.isShown())
					{
						mShareMenu.hideAndRemove();
					//	m_popupLayout.removeView(mShareMenu);
						if (m_isplay_return)
						{
							contiuneMedia();
							m_isplay_return = false;
						}
					}
					if(m_isplay_return)
					{
						m_isplay_return=false;
						contiuneMedia();
					}
					hideButtonsWithDelay();
					m_bt_more
							.setBackgroundResource(R.drawable.gallery_more_btn_selector);
					return true;
				}
				return false;
			}
		});
		m_bt_more.setOnTouchListener(mButtonOntouchListener);
		m_bt_3dSwitch.setOnTouchListener(mButtonOntouchListener);
		m_bt_cameraSwitch.setOnTouchListener(mButtonOntouchListener);
		m_bt_fullScreen.setOnTouchListener(mButtonOntouchListener);
		m_bt_mute.setOnTouchListener(mButtonOntouchListener);
		m_bt_play.setOnTouchListener(mButtonOntouchListener);
		m_bt_more.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				if (mMenuLayout.getVisibility() == View.VISIBLE)
				{
					mMenuLayout.setVisibility(View.INVISIBLE);
					hideButtonsWithDelay();
					m_bt_more
							.setBackgroundResource(R.drawable.gallery_more_btn_selector);
					if(m_isplay_return)
					{
						m_isplay_return=false;
						contiuneMedia();
					}
					return;
				}
                 
				if (m_state_playback == PLAYBACK_STATE_PLAY)
				{
					m_isplay_return = true;
					pauseMedia(true);
				}
				mListPosition = -1;
				updateMenuInfo(m_videopicker.getCurrentFileName());
				mMenuListView.setAdapter(mMenuListAdapter);
				mSubMenuLayout.setVisibility(View.INVISIBLE);
				if (mShareMenu != null && mShareMenu.isShown())
				{
					mShareMenu.hideAndRemove();
				//	m_popupLayout.removeView(mShareMenu);
					if (m_isplay_return)
					{
						contiuneMedia();
						m_isplay_return = false;
					}
				}
				m_bt_more
				.setBackgroundResource(R.drawable.gallery_more_btn_click);
				mMenuLayout.setVisibility(View.VISIBLE);
				showButtonsWithoutDelay();
			}
		});
		m_bt_fullScreen.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				isFullScreen = !isFullScreen;
				TsetScreenSize();
			}
		});

		m_bt_mute.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				isMute = !isMute;
				setMute(isMute);
			}
		});

	}

	public void setMute(boolean isMute)
	{
		if (m_audio_manager != null)
		{
			m_audio_manager.setStreamMute(AudioManager.STREAM_MUSIC, isMute);
			if (isMute)
			{
				m_bt_mute
						.setBackgroundResource(R.drawable.gallery_mute_btn_selector);
			} else
			{
				m_bt_mute
						.setBackgroundResource(R.drawable.gallery_mute_off_btn_selector);
			}
		}

	}

	public void setControlEnable()
	{
		// m_csbtn_play_pause.setEnable(true);
		// m_csbtn_set.setEnable(true);
		// m_csbtn_share.setEnable(true);
		// m_csbtn_delete.setEnable(true);
	}

	/**
	 * 添加广播（音量）
	 */
	private void initializeBroadcastReceiver()
	{

		m_audio_manager.registerMediaButtonEventReceiver(new ComponentName(
				"com.wistron.wigallery3d",
				"com.wistron.wigallery3d.MediaButtonBroadcastReceiver"));

		Handler m_handler_broadcast = new Handler()
		{

			@Override
			public void handleMessage(Message msg)
			{
				// TODO Auto-generated method stub

				switch (msg.what)
				{
				case BROADCAST_HANDLER_ADJUST_VOLUME:

					break;

				}
				super.handleMessage(msg);
			}

		};

	}

	private void resetOnSystemUiShow()
	{
		m_handle_control.removeMessages(CONTROL_HANDLER_HIDE_CTRLS);
		m_handle_control.sendEmptyMessage(CONTROL_HANDLER_APPEAR_CTRLS);
		m_handle_control.sendEmptyMessageDelayed(CONTROL_HANDLER_HIDE_CTRLS,
				DELAY_HIDE_TIME);
	}

	private void resetDelay()
	{
		// TODO Auto-generated method stub
      
		if (m_control_isshow)
		{
			if (CSStaticData.DEBUG)
				Log.e(TAG, "hide+++++++++++++++");
			m_handle_control.removeMessages(CONTROL_HANDLER_APPEAR_CTRLS);
			m_handle_control.removeMessages(CONTROL_HANDLER_HIDE_CTRLS);
			m_handle_control.sendEmptyMessage(CONTROL_HANDLER_HIDE_CTRLS);

		} else
		{
			if (CSStaticData.DEBUG)
				Log.e(TAG, "show++++++++++");
			m_handle_control.removeMessages(CONTROL_HANDLER_HIDE_CTRLS);
			m_handle_control.sendEmptyMessage(CONTROL_HANDLER_APPEAR_CTRLS);
			m_handle_control.sendEmptyMessageDelayed(
					CONTROL_HANDLER_HIDE_CTRLS, DELAY_HIDE_TIME);
		}
	}

	private void showButtonsWithoutDelay()
	{
		m_handle_control.removeMessages(CONTROL_HANDLER_HIDE_CTRLS);
		m_handle_control.sendEmptyMessage(CONTROL_HANDLER_APPEAR_CTRLS);
	}

	private void hideButtonsWithDelay()
	{
		if (m_state_playback == PLAYBACK_STATE_PLAY)
		{
			m_handle_control.removeMessages(CONTROL_HANDLER_HIDE_CTRLS);
			m_handle_control.sendEmptyMessage(CONTROL_HANDLER_APPEAR_CTRLS);
			m_handle_control.sendEmptyMessageDelayed(
					CONTROL_HANDLER_HIDE_CTRLS, DELAY_HIDE_TIME);
		}
	}

	public OnTouchListener mButtonOntouchListener = new OnTouchListener()
	{

		@Override
		public boolean onTouch(View v, MotionEvent event)
		{
				int action = event.getAction();
				switch (action)
				{
				case MotionEvent.ACTION_DOWN:
					Log.e(TAG, "MotionEvent.ACTION_DOWN");
					showButtonsWithoutDelay();
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
					hideButtonsWithDelay();
					Log.e(TAG, "MotionEvent.ACTION_UP");
					break;
				default:
					break;
				}
			return false;
		}
	};

	private void resetDelayonFling()
	{
		// TODO Auto-generated method stub

		m_handle_control.removeMessages(CONTROL_HANDLER_HIDE_CTRLS);
		m_handle_control.sendEmptyMessage(CONTROL_HANDLER_APPEAR_CTRLS);
		m_handle_control.sendEmptyMessageDelayed(CONTROL_HANDLER_HIDE_CTRLS,
				DELAY_HIDE_TIME);

	}

	/**
	 * 更新视频播放长度信息
	 */
	private void updateDuration()
	{
		try
		{
			double duration = m_media_player.getDuration();
			m_current_process = 0;
			m_duration_process = (int) duration;
			updateTimeIndicator(false);
		} catch (Exception e)
		{
			// TODO: handle exception
			Log.e(TAG, "updateDuration");
		}
	}

	/**
	 * 更新播放进度，控制UI
	 */
	private void updateProcess()
	{
		m_handle_control.sendEmptyMessage(CONTROL_HANDLER_PROGRESS_CHANGED);
	}

	/**
	 * ??慦?
	 */
	public void pauseMedia(boolean bool)
	{
		try
		{
			if (m_media_player != null)
			{
				if (m_media_player.isPlaying())
				{
					Log.w(TAG, "[pauseMedia]??慦?嚗挽蝵格?敹蛹PAUSE");
					m_media_player.pause();
					m_state_playback = PLAYBACK_STATE_PAUSE;
					if (bool)
					{
						m_bt_play
								.setBackgroundResource(R.drawable.gallery_play_btn_selector);
					}
					m_handle_control.removeMessages(CONTROL_HANDLER_HIDE_CTRLS);
					m_handle_control
							.sendEmptyMessage(CONTROL_HANDLER_APPEAR_CTRLS);
					m_handle_control
							.removeMessages(CONTROL_HANDLER_HIDEFRAMESEEKBAR_DELAY);
					m_handle_control
							.sendEmptyMessage(CONTROL_HANDLER_SHOWFRAMESEEKBAR);
				}
			}
		} catch (Exception e)
		{
			// TODO: handle exception
			if (CSStaticData.DEBUG)
				Log.e(TAG, "fail to pauseMedia");
		}
	}

	/**
	 * 继续播放
	 */
	public void contiuneMediaWithoutHideButtonsInDelay()
	{
		try
		{
			if (m_media_player != null)
			{
				m_media_player.start();
				m_bt_play
						.setBackgroundResource(R.drawable.gallery_pause_btn_selector);
				m_state_playback = PLAYBACK_STATE_PLAY;
			}
		} catch (Exception e)
		{
			// TODO: handle exception
		}
	}

	public void contiuneMedia()
	{
		try
		{
			if (m_media_player != null)
			{
				Log.w(TAG, "[contiuneMedia]蝏剜慦?嚗挽蝵格?敹蛹PLAY");
				m_media_player.start();
				m_bt_play
						.setBackgroundResource(R.drawable.gallery_pause_btn_selector);
				m_state_playback = PLAYBACK_STATE_PLAY;
				m_handle_control
						.sendEmptyMessageDelayed(
								CONTROL_HANDLER_HIDEFRAMESEEKBAR_DELAY,
								DELAY_HIDE_TIME);
				m_handle_control.sendEmptyMessageDelayed(
						CONTROL_HANDLER_HIDE_CTRLS, DELAY_HIDE_TIME);
			}
		} catch (Exception e)
		{
			// TODO: handle exception
		}
	}

	private void releaseMediaPlayer()
	{
		if (m_media_player != null)
		{
			m_media_player.stopPlayback();
			m_media_player = null;
		}
	}

	/**
	 * 停止播放
	 */
	public void stopMedia()
	{
		m_handle_control.removeMessages(CONTROL_HANDLER_PROGRESS_CHANGED);
		m_handle_control.sendEmptyMessage(CONTROL_HANDLER_PROGRESS_STOP);
		m_handle_control.sendEmptyMessage(CONTROL_HANDLER_RESET_PROGRESS);
		if (m_media_player != null)
		{
			if (m_state_playback != PLAYBACK_STATE_UNKNOWN)
			{
				m_media_player.pause();
			} else
			{
				// resetPlayer(false);
			}
			m_media_player.stopPlayback();
			m_state_playback = PLAYBACK_STATE_STOP;
		}

	}

	/**
	 * 重置播放器
	 * 
	 * @param refreshAll
	 *            ，是否保存播放进度等信息
	 * @param iserro
	 *            ，在发生错误时使用
	 */
	public void resetPlayer(boolean refreshAll, boolean iserro)
	{
		m_handle_control.removeMessages(CONTROL_HANDLER_PROGRESS_CHANGED);
		m_handle_control.sendEmptyMessage(CONTROL_HANDLER_PROGRESS_STOP);
		updateTimeIndicator(true);
		update3DSwitchButton();
		m_sbr_Normal.setProgress(0);
		m_sbr_FrameSeekBar.setProgress(0);
		isBaseFramebarHasShow = false;
		try
		{
			if (m_media_player.isPlaying())
			{
				m_media_player.pause();
			}
			m_media_player.stopPlayback();
		} catch (Exception e)
		{
			if (CSStaticData.DEBUG)
				Log.e(TAG, "fail to reset player");

		}
		if (iserro)
		{
			m_bt_play.setEnabled(false);
			m_bt_mute.setEnabled(false);
		} else
		{
			initVideoView(refreshAll);
			m_bt_play.setEnabled(true);
			m_bt_mute.setEnabled(true);
		}

	}

	@Override
	public void finish()
	{
		Intent intent = new Intent();
		intent.putStringArrayListExtra("callbackfilelist",
				(ArrayList<String>) m_videopicker.getfilepathList());
		if (mDeleteList.size() > 0)
		{
			intent.putStringArrayListExtra("deletelist", mDeleteList);

		}
		WiVideoViewerActivity.this.setResult(RESULT_OK, intent);
		super.finish();
	}

	@Override
	public void onBackPressed()
	{
		if (CSStaticData.DEBUG)
			Log.e(TAG, "onBackPressed");
		// TODO Auto-generated method stub
		setControlEnable();
		// 对话框是否显示
		if (mMsgBox != null && mMsgBox.isShown())
		{
			mMsgBox.hide();
			m_popupLayout.removeView(mMsgBox);
			if (m_isplay_return)
			{
				contiuneMedia();
				m_isplay_return = false;
			}
			hideButtonsWithDelay();
			if(m_videopicker.getCurrentFileIndex() == -1)
			{
				//文件为空，自动退出
				super.onBackPressed();
			}
			return;
		}

		if (mMsgBox_info != null && mMsgBox_info.isShown())
		{
			mMsgBox_info.hideAndRemove();
			if (m_isplay_return)
			{
				contiuneMedia();
				m_isplay_return = false;
			}
			hideButtonsWithDelay();
			return;
		}
		if (mShareMenu != null && mShareMenu.isShown())
		{
			mShareMenu.hideAndRemove();
			//m_popupLayout.removeView(mShareMenu);
//			if (m_isplay_return)
//			{
//				contiuneMediaWithoutHideButtonsInDelay();
//				m_isplay_return = false;
//			}
			mListPosition = -1;
			mMenuListAdapter.notifyDataSetChanged();
			return;
		}
		if (mSubMenuLayout.getVisibility() == View.VISIBLE)
		{
			mSubMenuLayout.setVisibility(View.INVISIBLE);

			mListPosition = -1;
			mMenuListAdapter.notifyDataSetChanged();
			return;
		}

		if (mMenuLayout.getVisibility() == View.VISIBLE)
		{
			mMenuLayout.setVisibility(View.INVISIBLE);
			if (m_isplay_return)
			{
				contiuneMedia();
				m_isplay_return = false;
			}
			m_bt_more
					.setBackgroundResource(R.drawable.gallery_more_btn_selector);
			return;
		}
		super.onBackPressed();
	}

	/*
	 * @Override public void onBackPressed() { // TODO Auto-generated method
	 * stub m_csbtn_play.setEnable(true); m_csbtn_share.setEnable(true);
	 * m_csbtn_delete.setEnable(true); m_csbtn_set.setEnable(true); if
	 * (TDStaticData.g_msgbox != null) { TDStaticData.HideMsgBox(m_framelayout);
	 * return; } if (m_imageview.mode == 1) { stopsolidplay(); return; }
	 * 
	 * super.onBackPressed(); }
	 */
	// handler????

	public void setCtlable(boolean mbool)
	{
		// **********************???
		if (mbool)
		{
			m_relativelayout_controlLayout.setVisibility(View.VISIBLE);
		} else
		{
			m_relativelayout_controlLayout.setVisibility(View.GONE);
		}
		enterLightsOutMode(!mbool);
		// if(mbool)
		// {
		// m_csbtn_back.setVisibility(View.VISIBLE);
		// m_csbtn_home.setVisibility(View.VISIBLE);
		// m_csbtn_set.setVisibility(View.VISIBLE);
		// m_csbtn_delete.setVisibility(View.VISIBLE);
		// m_csbtn_share.setVisibility(View.VISIBLE);
		// m_csbtn_play_pause.setVisibility(View.VISIBLE);
		// m_cstex_bottom.setVisibility(View.VISIBLE);
		//
		// m_cstxv_process.setVisibility(View.VISIBLE);
		// m_cstxv_duration.setVisibility(View.VISIBLE);
		// m_cssbr_process.setVisibility(View.VISIBLE);
		// m_cstex_seekbarbottom.setVisibility(View.VISIBLE);
		// m_cstex_videoname.setVisibility(View.VISIBLE);
		// }
		// else
		// {
		// m_csbtn_back.setVisibility(View.INVISIBLE);
		// m_csbtn_home.setVisibility(View.INVISIBLE);
		// m_csbtn_set.setVisibility(View.INVISIBLE);
		// m_csbtn_delete.setVisibility(View.INVISIBLE);
		// m_csbtn_share.setVisibility(View.INVISIBLE);
		// m_csbtn_play_pause.setVisibility(View.INVISIBLE);
		// m_cstex_bottom.setVisibility(View.INVISIBLE);
		// m_cstxv_process.setVisibility(View.INVISIBLE);
		// m_cstxv_duration.setVisibility(View.INVISIBLE);
		// m_cssbr_process.setVisibility(View.INVISIBLE);
		// m_cstex_seekbarbottom.setVisibility(View.INVISIBLE);
		// m_cstex_videoname.setVisibility(View.INVISIBLE);
		//
		// }
		// m_csviewgroup.invalidate();
	}

	public void enterLightsOutMode(boolean bool)
	{
		WindowManager.LayoutParams params = WiVideoViewerActivity.this
				.getWindow().getAttributes();
		int temp=params.systemUiVisibility;
		if (bool)
		{
			if(temp!=View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
			{
				params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
				WiVideoViewerActivity.this.getWindow().setAttributes(params);
			}
			
		} else
		{
			if(temp!=View.SYSTEM_UI_FLAG_VISIBLE)
			{
				params.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE;
				WiVideoViewerActivity.this.getWindow().setAttributes(params);
			}
		}
	}

	
//	public void enterLightsOutMode(boolean bool)
//	{
//		WindowManager.LayoutParams params = WiVideoViewerActivity.this
//				.getWindow().getAttributes();
//		int temp=params.systemUiVisibility;
//		if (bool)
//		{
//			params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//		} else
//		{
//			params.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE;
//		}
//		WiVideoViewerActivity.this.getWindow().setAttributes(params);
//	}

	
	
	public void Tsetting()
	{
		if (m_state_playback == PLAYBACK_STATE_PLAY)
		{
			m_isplay_return = true;
			pauseMedia(true);
		}
		SpannableString[] string = null;
		VideoProvider m_videoProvider = new VideoProvider(
				WiVideoViewerActivity.this);
		String filepath=null;
		if (isLaunchedFromInner) {
			//从WiGallery启动
			filepath=m_videopicker.getCurrentFileName();
		}else {
			//从sdcard启动
			String temp=m_videopicker.getCurrentFileName();
			int index = temp.indexOf("/sdcard/");
			String mCaptionPath_temp = temp.substring(index,temp.length());
			filepath = "/mnt/"+mCaptionPath_temp;
		}
		VideoInfo m_videoinfo = m_videoProvider.getVideoInfo(filepath);
		mMsgBox_info = new FileInfoMsgBox(mContext);
		if (m_videoinfo != null)
		{
			string = m_videoinfo.showInfoList();
			mMsgBox_info.setMessage(string);
		} else
		{
			mMsgBox_info.setMessage("Fail to get info!");
		}
		mMsgBox_info.setClickBlankHide(true);
		mMsgBox_info.setTitle("Details");
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				400, LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		layoutParams.setMargins(0, 80, 0, 20);
		mMsgBox_info.setLayoutParams(layoutParams);
		mMsgBox_info.addToLayout(m_popupLayout);
	}

	public void Tdeletefile()
	{
		if (m_state_playback == PLAYBACK_STATE_PLAY)
		{
			m_isplay_return = true;
			pauseMedia(true);
		}
		mMsgBox = new MsgBox(mContext);
		mMsgBox.setMessage(getResources().getText(R.string.gallery_delete_text));
		mMsgBox.setBackgound(R.drawable.main_menu_window_portrait);
		mMsgBox.setPositiveButton(
				getResources().getText(R.string.gallery_delete_confirm),
				new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						// TODO Auto-generated method stub
						if (CSStaticData.DEBUG)
							Log.e("AsyncFileHelper", "ok");
						setControlEnable();

						AsyncFileProvider asyncer = null;
						if (WiGalleryOpenGLRenderer.mAsyncFileProvider == null)
						{
							asyncer = new AsyncFileProvider(mContext);
						} else
						{
							asyncer = WiGalleryOpenGLRenderer.mAsyncFileProvider;
						}
						List<String> temp = new ArrayList<String>();
						temp.add(m_videopicker.getCurrentFileName());
						asyncer.deteleFiles(temp, false, true, WiVideoViewerActivityID);
						if (WiCameraActivity.m_main_handle != null)
						{
							Message deletemesg = new Message();
							if (FileTypeHelper.isVideoFile(m_videopicker
									.getCurrentFileName()))
							{
								deletemesg.what= InterSurfaceView.CAMERA_VIDEOLIST_UPDATE;
							} else
							{
								deletemesg.what = InterSurfaceView.CAMERA_PICLIST_UPDATE;
							}
							deletemesg.obj = m_videopicker.getCurrentFileName();
							WiCameraActivity.m_main_handle
									.sendMessage(deletemesg);
						}

						// if (WiGalleryOpenGLRenderer.mAsyncFileProvider !=
						// null
						// && WiGalleryOpenGLRenderer.mAsyncFileProvider !=
						// null)
						// {
						// List<String> temp = new ArrayList<String>();
						// temp.add(m_videopicker.getCurrentFileName());
						// WiGalleryOpenGLRenderer.mAsyncFileProvider
						// .deteleFiles(temp, false);
						// }
						mDeleteList.add(m_videopicker.getCurrentFileName());
						m_videopicker.deleteFile();
						mMsgBox.hide();
						m_popupLayout.removeView(mMsgBox);
						if (m_init_playmode == 0)
						{
							finish();
							return;
						}
						if (m_videopicker.getCurrentFileIndex() == -1)
						{
//							{
//								mMsgBox = new MsgBox(mContext);
//								mMsgBox.setMessage(getResources().getText(
//										R.string.gallery_delete_warn));
//								mMsgBox.setBackgound(R.drawable.main_menu_window_portrait);
//								mMsgBox.addToLayout(m_popupLayout);
//								mMsgBox.show();
//								m_bt_more.setClickable(false);
//							}
							finish();
						} else
						{
							resetPlayer(true, false);
							if (CSStaticData.DEBUG)
								Toast.makeText(
										WiVideoViewerActivity.this,
										"当前文件列表信息"
												+ (m_videopicker
														.getCurrentFileIndex() + 1)
												+ "/"
												+ m_videopicker.getFileCounts(),
										1000).show();
						}
						hideButtonsWithDelay();
					}
				});
		mMsgBox.setNegativeButton(
				getResources().getText(R.string.gallery_delete_cancel),
				new OnClickListener()
				{

					@Override
					public void onClick(View v)
					{
						mMsgBox.hide();
						m_popupLayout.removeView(mMsgBox);
						if (m_isplay_return)
						{
							contiuneMedia();
							m_isplay_return = false;
						}
						setControlEnable();
						hideButtonsWithDelay();
					}
				});
		mMsgBox.addToLayout(m_popupLayout);
		mMsgBox.show();
	}

	private void TsetScreenSize_()
	{
		android.view.ViewGroup.LayoutParams mLayoutParams = null;
		if (!m_is3DVideo && CSStaticData.g_is_3D_mode)
		{
			mLayoutParams = m_videoview3D.getLayoutParams();
		} else
		{
			mLayoutParams = m_videoview2D.getLayoutParams();
		}
		if (!isFullScreen)
		{
			if (m_video_width * m_video_height != 0)
			{
				mLayoutParams.width = m_video_width;
				mLayoutParams.height = m_video_height;
				m_bt_fullScreen
						.setBackgroundResource(R.drawable.gallery_screen_wide_btn_selector);
			}
		} else
		{
			if (m_video_width * m_video_height != 0)
			{
				float rate_src = (float) m_video_width / (float) m_video_height;
				float rate_screen = (float) m_screen_width
						/ (float) m_screen_height;
				if (rate_screen > rate_src)
				{
					mLayoutParams.width = (int) (m_screen_height * rate_src);
					mLayoutParams.height = m_screen_height;
				} else
				{
					mLayoutParams.width = m_screen_width;
					mLayoutParams.height = (int) (m_screen_width / rate_src);
				}
			}
			m_bt_fullScreen
					.setBackgroundResource(R.drawable.gallery_screen_narrow_btn_selector);
		}

		if (!m_is3DVideo && CSStaticData.g_is_3D_mode)
		{
			m_videoview3D.setLayoutParams(mLayoutParams);
		} else
		{
			m_videoview2D.setLayoutParams(mLayoutParams);
		}

	}

	/**
	 * 根据当前播放器模式修改尺寸
	 */
	private void TsetScreenSize()
	{
		android.view.ViewGroup.LayoutParams mLayoutParams = m_relativelayout_content
				.getLayoutParams();

		int fullscreen_width = 0;
		int fullscreen_height = 0;
		if (m_video_width * m_video_height != 0)
		{
			float rate_src = (float) m_video_width / (float) m_video_height;
			float rate_screen = (float) m_screen_width
					/ (float) m_screen_height;
			if (rate_screen > rate_src)
			{
				fullscreen_width = (int) (m_screen_height * rate_src);
				fullscreen_height = m_screen_height;
			} else
			{
				fullscreen_width = m_screen_width;
				fullscreen_height = (int) (m_screen_width / rate_src);
			}

			if (isFullScreen)
			{
				mLayoutParams.width = fullscreen_width;
				mLayoutParams.height = fullscreen_height;
				m_bt_fullScreen
						.setBackgroundResource(R.drawable.gallery_screen_narrow_btn_selector);
				m_relativelayout_content.setLayoutParams(mLayoutParams);
			} else
			{

				if (fullscreen_width >= m_video_width
						&& fullscreen_height >= m_video_height)
				{
					mLayoutParams.width = m_video_width;
					mLayoutParams.height = m_video_height;
					m_bt_fullScreen
							.setBackgroundResource(R.drawable.gallery_screen_wide_btn_selector);
					m_relativelayout_content.setLayoutParams(mLayoutParams);
				}
			}

		}
		//
		//
		// if (!isFullScreen)
		// {
		// if(m_video_width*m_video_height!=0)
		// {
		// mLayoutParams.width = m_video_width;
		// mLayoutParams.height = m_video_height;
		// m_bt_fullScreen
		// .setBackgroundResource(R.drawable.gallery_screen_wide_btn_selector);
		// }
		// } else
		// {
		// if (m_video_width * m_video_height != 0)
		// {
		// float rate_src = (float) m_video_width
		// / (float) m_video_height;
		// float rate_screen = (float) m_screen_width
		// / (float) m_screen_height;
		// if (rate_screen > rate_src)
		// {
		// mLayoutParams.width = (int) (m_screen_height * rate_src);
		// mLayoutParams.height = m_screen_height;
		// } else
		// {
		// mLayoutParams.width = m_screen_width;
		// mLayoutParams.height = (int) (m_screen_width / rate_src);
		// }
		// }
		// m_bt_fullScreen
		// .setBackgroundResource(R.drawable.gallery_screen_narrow_btn_selector);
		// }
		// m_relativelayout_content.setLayoutParams(mLayoutParams);
		// //m_relativelayout_content.requestLayout();
	}

	public void Tshare()
	{
		if (m_state_playback == PLAYBACK_STATE_PLAY)
		{
			m_isplay_return = true;
			pauseMedia(true);
		}
//		mShareMenu = new ShareMenu(WiVideoViewerActivity.this,
//				CSStaticData.g_screen_width - 300, 150);
		mShareMenu = new ShareMenu(WiVideoViewerActivity.this,
		0, 0);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(300, ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		layoutParams.setMargins(0, 60, 0, 0);
		mShareMenu.setShareLayoutParams(layoutParams);
		mShareMenu.setAnchor(ShareMenu.ANCHOR_TOP_RIGHT);
		String m_filePath_menuString;
		if(isLaunchedFromInner)
		{
			 m_filePath_menuString = "file:/"
					+ m_videopicker.getCurrentFileName();
		}
		else
		{
			 m_filePath_menuString = m_videopicker.getCurrentFileName();
		}
		
		ArrayList<String> m_ArrayList = new ArrayList<String>();
		m_ArrayList.add(m_filePath_menuString);
		mShareMenu.setVideoPath(m_ArrayList);
		mShareMenu.addToLayout(mMenuLayout);
		mShareMenu.show();
	}

	public void Tplay()
	{
		m_isplay_return = false;
		if (m_state_playback == PLAYBACK_STATE_PLAY)
		{
			pauseMedia(true);
			if (m_seekbar_mode == 1)
			{
				if (isLoadSubFrames(m_duration_process))
				{
					resetSubImageToGivenTime(m_current_process);
				} else
				{
					m_relativelayout_frameseekbar_small
							.setVisibility(View.INVISIBLE);
				}
			}
		} else if (m_state_playback == PLAYBACK_STATE_PAUSE)
		{
			contiuneMedia();
		}
	}

	private void initData()
	{

		mTextColorWhite = getResources().getXml(
				R.drawable.textcolor_selector_white);
		mTextColorGreen = getResources().getXml(
				R.drawable.textcolor_selector_green);

		try
		{
			COLORSTATELIST_WHITE = ColorStateList.createFromXml(getResources(),
					mTextColorWhite);
			COLORSTATELIST_GREEN = ColorStateList.createFromXml(getResources(),
					mTextColorGreen);
		} catch (XmlPullParserException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		mMoreMenuResId = new int[][]
		{
				{ R.drawable.gallery_more_share,
						R.string.gallery_menu_item_share },
				{ R.drawable.gallery_more_delete,
						R.string.gallery_menu_item_delete },
				// { R.drawable.gallery_more_move,
				// R.string.gallery_menu_item_move },
				{ R.drawable.gallery_more_set_as_favorite,
						R.string.gallery_menu_item_setfavorite },
				{ R.drawable.gallery_more_remove_favorite,
						R.string.gallery_menu_item_removefavorite },
				{ R.drawable.gallery_more_file_info,
						R.string.gallery_menu_item_fileinfo },
				{ R.drawable.gallery_seekbar_settings,
						R.string.videoPalyer_menu_item_seekBarMode },
				{ R.drawable.gallery_language_encording,
						R.string.videoPalyer_menu_item_languageEncoding },
				{ 0, R.string.gallery_menu_item_2Dto3D } };
		mSeekBarModeResId = new int[][]
		{
		{ 0, R.string.videoPalyer_menu_item_normalSeekBar },
		{ 0, R.string.videoPalyer_menu_item_frameSeekBar } };
		mLanguageEcodingResId = new int[][]
		{
		{ 0, R.string.videoPalyer_menu_item_english_unitedKingdom },
		{ 0, R.string.videoPalyer_menu_item_english_unitedStates },
		{ 0, R.string.videoPalyer_menu_item_french },
		{ 0, R.string.videoPalyer_menu_item_deutsch },
		{ 0, R.string.videoPalyer_menu_item_spanish },
		{ 0, R.string.videoPalyer_menu_item_italiano },
		{ 0, R.string.videoPalyer_menu_item_simpleChinese },
		{ 0, R.string.videoPalyer_menu_item_traditionalChinese }, };

		// m_charset_string1 = {"UTF-8", "GBK",
		// "GB2312","BIG5","BIG5_HKSCS","ISO-8859-1",
		// "ISO-8859-2","ISO-8859-6","ISO-8859-4","ISO-8859-5","Cyrillic(KOI8-R)","ISO-8859-7","ISO-8859-8",
		// "EUC-JP", "EUC-KR", "ISO-8859-9" };
		mGalleryMoveToMenuResId = new int[][]
		{
		{ 0, R.string.gallery_menu_item_internal },
		{ 0, R.string.gallery_menu_item_external }, };

		mSubMenuStateMap = new HashMap<Integer, Integer>();
		mSubMenuStateMap.put(SUBMENU_MOVETO_INTERNAL, -1);
		mSubMenuStateMap.put(SUBMENU_SEEKBARMODE_START_ID, 0);
		mSubMenuStateMap.put(SUBMENU_LANGUAGEENCODING_START_ID, -1);

		mContext = WiVideoViewerActivity.this;
		mMenuLayout = (RelativeLayout) findViewById(R.id.moreMenuLayout);
		mSubMenuLayout = (RelativeLayout) findViewById(R.id.subMenuLayout);

		mMenuListView = (ListView) findViewById(R.id.moreListView);
		mSubMenuListView = (ListView) findViewById(R.id.subListView);
		mSubListTitle = (TextView) findViewById(R.id.subListTitle);

		mMenuListAdapter = new ViewerListAdapter(mContext, mMoreMenuResId);
		mSubListAdapter = new ViewerListAdapter(mContext, null);

		mMenuListAdapter
				.setMenuType(ViewerListAdapter.MENU_TYPE_MAIN_MENU_VIDEOVIEWER);
		mSubListAdapter
				.setMenuType(ViewerListAdapter.MENU_TYPE_SUB_MENU_VIDEOVIEWER);
		mSubListAdapter.setMap(mSubMenuStateMap);
		mMenuListView.setAdapter(mMenuListAdapter);
		mMenuLayout.setVisibility(View.INVISIBLE);// 设置为不可见
		mSubMenuLayout.setVisibility(View.INVISIBLE);// 设置为不可见

		// 初始化字幕解析
		mCaptionsResolve = new CaptionsResolve(WiVideoViewerActivity.this);
		mCaptionsResolve.addToLayout(m_captionLayout);

	}

	public void lookupFavoriteInfo()
	{
		FavoriteDBAdapter dbAdapter = new FavoriteDBAdapter(mContext);
		mFavoriteInfoList = dbAdapter.getFavoriteFiles();
		if (mFavoriteInfoList != null && mFavoriteInfoList.size() > 0)
		{
			mFavoriteListCount = mFavoriteInfoList.size();
		} else
		{
			mFavoriteListCount = 0;
		}
		dbAdapter.dispose();
		dbAdapter = null;
	}

	public void setFavoriteInfo(String fileString, boolean bool)
	{
		// 设置喜好
		if (fileString != null)
		{
			{//LiuWei
//				FavoriteDBAdapter dbAdapter = new FavoriteDBAdapter(
//						WiVideoViewerActivity.this);
//				List<String> selectedList = new ArrayList<String>();
//				selectedList.add(fileString);
//				if (bool)
//				{
//					dbAdapter.setAsFavorite(selectedList);
//				} else
//				{
//					dbAdapter.removeFromFavorite(selectedList);
//				}
//				if (CSStaticData.DEBUG)
//					Log.e(TAG, "[setFavoriteInfo]-->" + bool);
//				dbAdapter.dispose();
//				dbAdapter = null;
			}
			{//Cocoonshu
				AsyncFileProvider asyncFileProvider = null;
				if(WiGalleryOpenGLRenderer.mAsyncFileProvider == null){
					asyncFileProvider = new AsyncFileProvider(WiVideoViewerActivity.this);
				}else{
					asyncFileProvider = WiGalleryOpenGLRenderer.mAsyncFileProvider;
				}
				List<String> selectedList = new ArrayList<String>();
				selectedList.add(fileString);
				if(bool){
					asyncFileProvider.setFavoriteToDB(selectedList, WiVideoViewerActivityID);
				}else{
					asyncFileProvider.removeFavoriteFromDB(selectedList, WiVideoViewerActivityID);	
				}
				asyncFileProvider = null;
			}
		}
	}

	public void updateMenuInfo(String filepath)
	{
		lookupFavoriteInfo();
		boolean isFavorite = false;
		boolean isInner = false;
		for (int i = 0; i < mFavoriteListCount; i++)
		{
			if (mFavoriteInfoList.get(i).equals(filepath))
			{
				isFavorite = true;
			}
		}
		if (filepath != null)
		{
			isInner = FileTypeHelper.isInternalFile(filepath);
		} else
		{
			isInner = false;
		}
		if (isInner)
		{
			mSubMenuStateMap.put(SUBMENU_MOVETO_INTERNAL, 0);
		} else
		{
			mSubMenuStateMap.put(SUBMENU_MOVETO_INTERNAL, 1);
		}
		if (isFavorite)
		{
			mMainListDisablePosition = 2;
		} else
		{
			mMainListDisablePosition = 3;
		}
	}

	private void initListViewListenner()
	{
		mMenuListView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				// TODO Auto-generated method stub
				mListPosition = arg2;
				int count = arg0.getChildCount();
				for (int i = 0; i < count; i++)
				{

					if (((TextView) arg0.getChildAt(i).findViewById(
							R.id.videoPlayerMenuText)).getCurrentTextColor() != Color.GRAY)
					{

						((TextView) arg0.getChildAt(i).findViewById(
								R.id.videoPlayerMenuText))
								.setTextColor(COLORSTATELIST_WHITE);
					}
				}

				((TextView) arg1.findViewById(R.id.videoPlayerMenuText))
						.setTextColor(COLORSTATELIST_GREEN);

				switch (arg2)
				{
				case 0:
					mSubMenuLayout.setVisibility(View.INVISIBLE);
					if (mShareMenu != null && mShareMenu.isShown())
					{
						mShareMenu.hideAndRemove();
						((TextView) arg1.findViewById(R.id.videoPlayerMenuText))
								.setTextColor(COLORSTATELIST_WHITE);
						return;
					}
					Tshare();
					break;
				case 1:
					Tdeletefile();
					mMenuLayout.setVisibility(View.INVISIBLE);
					m_bt_more
							.setBackgroundResource(R.drawable.gallery_more_btn_selector);
					break;
				// case 2:
				// if (mSubMenuLayout.getVisibility() == View.VISIBLE &&
				// mSubListTitle.getText() ==
				// getResources().getText(R.string.gallery_menu_item_move)) {
				// mSubMenuLayout.setVisibility(View.INVISIBLE);
				// ((TextView)
				// arg1.findViewById(R.id.videoPlayerMenuText)).setTextColor(COLORSTATELIST_WHITE);
				// return;
				// }
				// mSubListAdapter.setItemStartId(SUBMENU_MOVETO_INTERNAL);
				// mSubListAdapter.setResId(mGalleryMoveToMenuResId);
				// mSubListTitle.setText(R.string.gallery_menu_item_move);
				// mSubMenuLayout.setVisibility(View.VISIBLE);
				//
				//
				// break;
				case 2:
					mMenuLayout.setVisibility(View.INVISIBLE);
					if (m_isplay_return)
					{
						contiuneMedia();
						m_isplay_return = false;
					}
					m_bt_more
							.setBackgroundResource(R.drawable.gallery_more_btn_selector);
					mMainListDisablePosition = 3;
					setFavoriteInfo(m_videopicker.getCurrentFileName(), true);
					String temp = m_videopicker.getCurFileName();
					msgBox_disappearautoBox.updateMessage(temp
							+ " has been set as favorite .");
					msgBox_disappearautoBox.show();
					msgBox_disappearautoBox.hideDelay(2000);
					hideButtonsWithDelay();
					break;
				case 3:
					mMenuLayout.setVisibility(View.INVISIBLE);
					if (m_isplay_return)
					{
						contiuneMedia();
						m_isplay_return = false;
					}
					m_bt_more
							.setBackgroundResource(R.drawable.gallery_more_btn_selector);
					mMainListDisablePosition = 2;
					setFavoriteInfo(m_videopicker.getCurrentFileName(), false);
					String temp1 = m_videopicker.getCurFileName();
					msgBox_disappearautoBox
							.updateMessage("Favorite tag has been removed.");
					msgBox_disappearautoBox.show();
					msgBox_disappearautoBox.hideDelay(2000);
					hideButtonsWithDelay();
					break;
				case 4:
					mMenuLayout.setVisibility(View.INVISIBLE);
					m_bt_more
							.setBackgroundResource(R.drawable.gallery_more_btn_selector);
					Tsetting();
					break;
				case 5:
					if (mSubMenuLayout.getVisibility() == View.VISIBLE
							&& mSubListTitle.getText() == getResources()
									.getText(
											R.string.videoPalyer_menu_item_seekBarMode))
					{
						mSubMenuLayout.setVisibility(View.INVISIBLE);
						((TextView) arg1.findViewById(R.id.videoPlayerMenuText))
								.setTextColor(COLORSTATELIST_WHITE);
						return;
					}
					mSubListAdapter
							.setItemStartId(SUBMENU_SEEKBARMODE_START_ID);
					mSubListAdapter.setResId(mSeekBarModeResId);
					mSubListTitle.setText(getResources().getText(
							R.string.videoPalyer_menu_item_seekBarMode));
					mSubMenuLayout.setVisibility(View.VISIBLE);
					break;
				case 6:
					if (mSubMenuLayout.getVisibility() == View.VISIBLE
							&& mSubListTitle.getText() == getResources()
									.getText(
											R.string.videoPalyer_menu_item_languageEncoding))
					{
						mSubMenuLayout.setVisibility(View.INVISIBLE);
						((TextView) arg1.findViewById(R.id.videoPlayerMenuText))
								.setTextColor(COLORSTATELIST_WHITE);
						return;
					}
					mSubListAdapter
							.setItemStartId(SUBMENU_LANGUAGEENCODING_START_ID);
					mSubListAdapter.setResId(mLanguageEcodingResId);
					mSubListTitle.setText(getResources().getText(
							R.string.videoPalyer_menu_item_languageEncoding));
					mSubMenuLayout.setVisibility(View.VISIBLE);
					break;
				case 7:
					mMenuLayout.setVisibility(View.INVISIBLE);
					if (m_isplay_return)
					{
						contiuneMedia();
						m_isplay_return = false;
					}
					m_bt_more
							.setBackgroundResource(R.drawable.gallery_more_btn_selector);
					hideButtonsWithDelay();
				}
				if (arg2 != 0)
				{
					if (mShareMenu != null && mShareMenu.isShown())
					{
						mShareMenu.hideAndRemove();
						//m_popupLayout.removeView(mShareMenu);
					}
				}

				if (arg2 == 0 || arg2 == 1 || arg2 == 3 || arg2 == 4)
				{
					return;
				}
				mSubMenuListView.setAdapter(mSubListAdapter);

			}
		});

		mSubMenuListView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				// TODO Auto-generated method stub
				WiImageViewerActivity.mSubListPosition = arg2;
				if(CSStaticData.DEBUG)
                 Log.e(TAG, "[onItemClick] arg2"+arg2);
				switch (arg1.getId())
				{
				// 进度条样式
				case SUBMENU_SEEKBARMODE_START_ID:
					m_seekbar_mode = 0;
					mSubMenuStateMap.put(SUBMENU_SEEKBARMODE_START_ID, arg2);
					m_relativelayout_normalseekbar.setVisibility(View.VISIBLE);
					m_relativelayout_frameseekbar.setVisibility(View.INVISIBLE);
					break;
				case SUBMENU_SEEKBARMODE_START_ID + 1:
					m_seekbar_mode = 1;
					mSubMenuStateMap.put(SUBMENU_SEEKBARMODE_START_ID, arg2);
					m_relativelayout_normalseekbar
							.setVisibility(View.INVISIBLE);
					m_relativelayout_frameseekbar.setVisibility(View.VISIBLE);
					if (!isBaseFramebarHasShow)
					{
						// 加载图片
						mFrameAdapter_base.resetBitmap();
						LoadVideoFrameTask.Callback callback = new LoadVideoFrameTask.Callback()
						{
							@Override
							public void onComplete(long time)
							{
								if (CSStaticData.DEBUG)
									Log.e(TAG, "生成图片用时" + time);
							}
						};
						LoadVideoFrameTask task = new LoadVideoFrameTask(
								WiVideoViewerActivity.this, callback,
								m_videopicker.getCurrentFileName(),
								m_VideoFrameHandler);
						if (m_isLandScape)
						{
							task.execute(m_duration_process, 8, 0, true, 0);
						} else
						{
							task.execute(m_duration_process, 5, 0, true, 0);
						}
						isBaseFramebarHasShow = true;
					}
					if (isLoadSubFrames(m_duration_process))
					{
						resetSubImageToGivenTime(m_current_process);
					} else
					{
						m_relativelayout_frameseekbar_small
								.setVisibility(View.INVISIBLE);
					}
					break;
				case SUBMENU_LANGUAGEENCODING_START_ID:
					mSubMenuStateMap.put(SUBMENU_LANGUAGEENCODING_START_ID,
							arg2);
					// English (United Kingdom)
					mCharSet = "ISO-8859-1";
					break;
				case SUBMENU_LANGUAGEENCODING_START_ID + 1:
					mSubMenuStateMap.put(SUBMENU_LANGUAGEENCODING_START_ID,
							arg2);
					// /English (United States)
					mCharSet = "ISO-8859-1";
					break;
				case SUBMENU_LANGUAGEENCODING_START_ID + 2:
					// French
					mSubMenuStateMap.put(SUBMENU_LANGUAGEENCODING_START_ID,
							arg2);
					mCharSet = "ISO-8859-1";
					break;
				case SUBMENU_LANGUAGEENCODING_START_ID + 3:
					// Deutsch
					mSubMenuStateMap.put(SUBMENU_LANGUAGEENCODING_START_ID,
							arg2);
					mCharSet = "ISO-8859-2";
					break;
				case SUBMENU_LANGUAGEENCODING_START_ID + 4:
					// Spanish
					mSubMenuStateMap.put(SUBMENU_LANGUAGEENCODING_START_ID,
							arg2);
					mCharSet = "ISO-8859-1";
					break;
				case SUBMENU_LANGUAGEENCODING_START_ID + 5:
					// Italiano
					mSubMenuStateMap.put(SUBMENU_LANGUAGEENCODING_START_ID,
							arg2);
					mCharSet = "ISO-8859-1";
					break;
				case SUBMENU_LANGUAGEENCODING_START_ID + 6:
					// Simple Chinese gb18030
					mCharSet = "gbk";
					mSubMenuStateMap.put(SUBMENU_LANGUAGEENCODING_START_ID,
							arg2);
					break;

				case SUBMENU_LANGUAGEENCODING_START_ID + 7:
					// traditionalChinese BIG5
					mCharSet = "BIG5";
					mSubMenuStateMap.put(SUBMENU_LANGUAGEENCODING_START_ID,
							arg2);
					break;
				// case SUBMENU_LANGUAGEENCODING_START_ID + 10:
				// break;
				// 移动文件
				case SUBMENU_MOVETO_INTERNAL:
					mSubMenuStateMap.put(SUBMENU_MOVETO_INTERNAL, arg2);
					break;
				case SUBMENU_MOVETO_INTERNAL + 1:
					mSubMenuStateMap.put(SUBMENU_MOVETO_INTERNAL, arg2);
					break;
				}
				hideButtonsWithDelay();
				mSubMenuLayout.setVisibility(View.INVISIBLE);
				if (m_isplay_return)
				{
					contiuneMedia();
					m_isplay_return = false;
				}
				mMenuLayout.setVisibility(View.INVISIBLE);
				m_bt_more
						.setBackgroundResource(R.drawable.gallery_more_btn_selector);

				if (mCharSet == null)
				{
					return;
				}
				mCaptionsResolve.setCharset(mCharSet);
				startCaptionResolve();
				if (CSStaticData.DEBUG)
				{
					Log.v(TAG, "mCaptionPath:" + mCaptionPath + "fileName:"
							+ fileName);
				}
			}
		});

	}

	public boolean isLoadSubFrames(int during)
	{
		if (during >= 10000)
		{
			return true;
		} else
		{
			return false;
		}
	}

	public void resetSubImageToGivenTime(int startTime)
	{
		if (CSStaticData.DEBUG)
			Log.e(TAG, "[resetSubImageToGivenTime]");
		m_duration_frameseek = startTime;
		mFrameAdapter_sub.resetBitmap();
		gv_sub.setSelection(10);
		if (!isVideoFrameLoadComplete)
		{
			loadInnerFrametaskImageTask.cancel(true);
		}
		LoadVideoFrameTask.Callback callback2 = new LoadVideoFrameTask.Callback()
		{
			@Override
			public void onComplete(long time)
			{
				if (CSStaticData.DEBUG)
					Log.e(TAG, "生成图片用时" + time);
				isVideoFrameLoadComplete = true;
			}
		};
		loadInnerFrametaskImageTask = new LoadVideoFrameTask(
				WiVideoViewerActivity.this, callback2,
				m_videopicker.getCurrentFileName(), m_VideoFrameHandler);
		loadInnerFrametaskImageTask.execute(m_duration_process, 7,
				m_duration_frameseek, false, 9);
		isVideoFrameLoadComplete = false;
	}

	/**
	 * 去掉文件扩展名
	 * 
	 * @param filename
	 * @return
	 */
	public static String trimExtension(String filename)
	{
		if (filename != null && filename.length() > 0)
		{
			int i = filename.lastIndexOf(".");

			if (i > -1 && i < filename.length())
			{
				return filename.substring(0, i);
			}
		}

		return filename;
	}

	private void startCaptionResolve()
	{
		if (m_videopicker == null || mCaptionsResolve == null)
		{
			if (CSStaticData.DEBUG)
				Log.e(TAG, "停止字幕解析");
			return;
		}
//		String fileName = m_videopicker.getCurFileName();
//		String videoName = trimExtension(fileName);
//		mCaptionPath = CSStaticData.TMP_EXT_DIR + videoName;
//		System.out.println("视频名："+fileName);
//		System.out.println("字幕路径"+mCaptionPath);
//		mCaptionsResolve.readFile(mCaptionPath);
		
		if (isLaunchedFromInner) {
			//从WiGallery启动
			mCaptionPath = trimExtension(m_videopicker.getCurrentFileName());
			mCaptionsResolve.readFile(mCaptionPath);
		}else {
			//从sdcard启动
			try
			{
				mCaptionPath = trimExtension(m_videopicker.getCurrentFileName());
				int index = mCaptionPath.indexOf("/sdcard/");
				String mCaptionPath_temp = mCaptionPath.substring(index,mCaptionPath.length());
				String mCaptionPath_format = "/mnt/"+mCaptionPath_temp;
//				String mCaptionPath1 = replace(mCaptionPath, "file://", "");
				mCaptionsResolve.readFile(mCaptionPath_format);
			} catch (Exception e)
			{
				// TODO: handle exception
				mCaptionsResolve.readFile("");
			}
			
		}
		

	}

	/**
	 * Handler 控制按钮显示隐藏
	 */
	public Handler m_handle_control = new Handler()
	{
		int duration = 0;

		int minute = 0, second = 0, hour = 0;

		@Override
		public void handleMessage(Message msg)
		{
			// TODO Auto-generated method stub

			switch (msg.what)
			{
			case CONTROL_HANDLER_APPEAR_CTRLS:
				// m_ctrl_layout.setVisibility(View.VISIBLE);
				setCtlable(true);
				m_control_isshow = true;
				break;

			case CONTROL_HANDLER_HIDE_CTRLS:
				// m_ctrl_layout.setVisibility(View.INVISIBLE);
				setCtlable(false);
				m_control_isshow = false;
				break;
			case CONTROL_HANDLER_HIDEFRAMESEEKBAR_DELAY:
				if (isLoadSubFrames(m_duration_process))
				{
					m_relativelayout_frameseekbar_small
							.setVisibility(View.INVISIBLE);
					Bitmap mBitmap = BitmapFactory.decodeResource(
							getResources(), R.drawable.scrolling_bar_normal);
					BitmapDrawable mBitmapDrawable = new BitmapDrawable(mBitmap);
					m_sbr_FrameSeekBar.setThumb(mBitmapDrawable);
				}
				break;
			case CONTROL_HANDLER_SHOWFRAMESEEKBAR:
				if (isLoadSubFrames(m_duration_process))
				{
					m_relativelayout_frameseekbar_small
							.setVisibility(View.VISIBLE);
					Bitmap aBitmap = BitmapFactory.decodeResource(
							getResources(),
							R.drawable.scrolling_bar_with_thumb_base);
					BitmapDrawable aBitmapDrawable = new BitmapDrawable(aBitmap);
					m_sbr_FrameSeekBar.setThumb(aBitmapDrawable);
				}
				break;
			case CONTROL_HANDLER_PROGRESS_CHANGED:
				// 更新视频播放进度信息
				if (m_media_player != null)
				{

					if (m_duration_process >= 100)
					{
						try
						{
							m_current_process = m_media_player
									.getCurrentPosition();
						} catch (Exception e)
						{
							// TODO: handle exception
							Log.e(TAG, "fail to getCurrentPosition");
						}

						//
						if (m_state_playback == PLAYBACK_STATE_PLAY)
						{
							Message message = new Message();
							message.what = 1;
							message.obj = m_current_process;
							mCaptionsResolve.mHandler.sendMessage(message);
						}
						float rate = ((float) m_current_process)
								/ ((float) m_duration_process);
						int myprogress = (int) (rate * 100);
						m_sbr_Normal.setProgress(myprogress);
						m_sbr_FrameSeekBar.setProgress(myprogress);
						updateTimeIndicator(false);
					} else
					{
						m_sbr_Normal.setProgress(0);
						m_sbr_FrameSeekBar.setProgress(0);
						updateTimeIndicator(false);
					}
				}
				sendEmptyMessageDelayed(CONTROL_HANDLER_PROGRESS_CHANGED, 300);// 重复回调
				break;

			case CONTROL_HANDLER_PROGRESS_STOP:
				break;
			case CONTROL_HANDLER_RESET_PROGRESS:
				m_current_process = 0;
				updateTimeIndicator(false);
				m_sbr_Normal.setProgress(0);
				m_sbr_FrameSeekBar.setProgress(0);
				break;
			case CONTROL_HANDLER_DISPLAY_VIDEO_INFO:
				if (CSStaticData.DEBUG)
					Log.e("info", "true");
				break;

			case CONTROL_HANDLER_HIDE_VIDEO_INFO:
				if (CSStaticData.DEBUG)
					Log.e("info", "false");
				break;
			}

			super.handleMessage(msg);
		}
	};

	private void doCleanUp()
	{
		mVideoWidth = 0;
		mVideoHeight = 0;
		mIsVideoReadyToBePlayed = false;
		mIsVideoSizeKnown = false;
	}

	@Override
	public boolean onDown(MotionEvent arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY)
	{
		if (CSStaticData.DEBUG)
			Log.e(TAG, "onFling");
		if (m_init_playmode == 0)
		{
			// 播放模式
		} else if (m_init_playmode == 1)
		{
			float x1 = 0;
			float x2 = 0;
			float vx = 0;
			x1 = e1.getX();
			x2 = e2.getX();
			vx = velocityX;
			if (Math.abs(x1 - x2) > 100 && vx < -200)
			{
				if (m_videopicker.getCurrentFileIndex() < (m_videopicker
						.getFileCounts() - 1))
				{
					m_videopicker.getNextBitmap();
					resetPlayer(true, false);
					if (CSStaticData.DEBUG)
						Toast.makeText(
								WiVideoViewerActivity.this,
								"当前文件列表信息"
										+ (m_videopicker.getCurrentFileIndex() + 1)
										+ "/" + m_videopicker.getFileCounts(),
								1000).show();
					return true;
				}

			} else if (Math.abs(x2 - x1) > 100 && vx > 200)
			{

				if (m_videopicker.getCurrentFileIndex() > 0)
				{
					m_videopicker.getPreBitmap();
					resetPlayer(true, false);
					if (CSStaticData.DEBUG)
						Toast.makeText(
								WiVideoViewerActivity.this,
								"当前文件列表信息"
										+ (m_videopicker.getCurrentFileIndex() + 1)
										+ "/" + m_videopicker.getFileCounts(),
								1000).show();
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public void onLongPress(MotionEvent e)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e)
	{
		return false;
	}

	/**
	 * 横竖屏信息还原
	 */
	private void initStatusData()
	{
		VideoViewerStateInfo stateInfo = (VideoViewerStateInfo) getLastNonConfigurationInstance();
		if (stateInfo != null)
		{
			m_videopicker = new MediaFilePicker(stateInfo.getmFileList(),
					stateInfo.getmCurrentFileIndex());
			m_init_playmode = stateInfo.getM_init_playmode();
			m_state_playback = stateInfo.getPlayState();
			m_interrupt_process = stateInfo.getmCurrentProgress();
			isMute = stateInfo.isMute();
			isFullScreen = stateInfo.isFullScreen();
			m_duration_process = stateInfo.getmDuring();
			if (stateInfo.isDeleteBoxShow())
			{
				Tdeletefile();
			}
			if (stateInfo.isFileInfoBoxShow())
				Tsetting();
			// 进度条样式保存
			if (stateInfo.getmSeekbarMode() == 1)
			{
				isFrameSeekbarModeChanged = true;
			}

		}
	}

	/**
	 * 横竖屏状态保存
	 */
	@Override
	public Object onRetainNonConfigurationInstance()
	{
		Log.w(TAG, "[onRetainNonConfigurationInstance]");
		boolean isDelete = false;
		boolean isInfo = false;
		if (mMsgBox != null && mMsgBox.isShown())
		{
			isDelete = true;
		}
		if (mMsgBox_info != null && mMsgBox_info.isShown())
		{
			isInfo = true;
		}
		VideoViewerStateInfo tempStateInfo = new VideoViewerStateInfo(
				m_videopicker.getCurrentFileIndex(), m_interrupt_process,
				m_state_playback, false, m_videopicker.getfilepathList(),
				isMute, isFullScreen, m_init_playmode, isDelete, isInfo,
				m_seekbar_mode, m_duration_process);
		return tempStateInfo;
	}
	public static String replace(String strSource, String strFrom, String strTo) {    
	    if (strSource == null) {        
	      return null;    
	    }  
	    int i = 0;
	    if ((i = strSource.indexOf(strFrom, i)) >= 0) {
	      char[] cSrc = strSource.toCharArray();
	      char[] cTo = strTo.toCharArray();
	      int len = strFrom.length();  
	      StringBuffer buf = new StringBuffer(cSrc.length);  
	      buf.append(cSrc, 0, i).append(cTo);
	      i += len;    
	      int j = i;       
	      while ((i = strSource.indexOf(strFrom, i)) > 0) {  
	        buf.append(cSrc, j, i - j).append(cTo);   
	        i += len;  
	        j = i;        
	      }        
	      buf.append(cSrc, j, cSrc.length - j); 
	      return buf.toString(); 
	    } 
	    return strSource;
	  } 

	
	
}

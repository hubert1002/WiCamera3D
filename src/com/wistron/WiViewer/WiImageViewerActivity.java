package com.wistron.WiViewer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import Utilities.CSStaticData;
import Utilities.FileTypeHelper;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

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

public class WiImageViewerActivity extends Activity implements
		OnGestureListener
{
	/** Called when the activity is first created. */
	private String Tag = "WiImageViewerActivity";
	public final static int WiImageViewerActivityID = 0x76113;
	private boolean m_isMove = false;// 记录动画是否完成
	public static MediaFilePicker m_bitmappicker; // 读取图片
	private int screenWidth;// 当前屏幕宽
	private int screenHeight;// 当前屏幕高
	private int m_position;// 用于左右移动的位置
	private int m_alpha;// 用于淡入淡出的透明度
	public static WiImageView m_imageview;
	private Runnable m_runnable;// 移入移出
	private Runnable m_runnable1;// 移動還原
	private Runnable mrunnable2;// 淡入淡出
	private GestureDetector m_gesturedetector;
	private static boolean m_control_isshow = true;// 用于标记控件层是否显示
	private int m_fontsize = 22;
	private Matrix matrix;// 用于键盘控制的放大缩小
	float m_moveX = 0;
	public static int m_isNextBitmapLoaded = 0;// 标记下一张图片是够加载完成
	public Bitmap[][] m_bitmaps = null;// 用于存放读取出来的上下一张图片
	private boolean m_stopdispatch = false;// 判断是否分发触摸事件
	float m_FirstTouchX = 0;// 标记触摸的起始位置
	private boolean m_isNext = false;// 用于标记是否为下一张
	private boolean m_isSolidPlay = false;// 进行其他操作时当前是否在幻灯片播放
	private boolean m_moveIsOver = false;// 标记当前是否还在移动
	private boolean m_SDismounted = false;// 标记sd卡是否被拔出
	// oncreate时就查询favorite信息
	private List<String> mFavoriteInfoList;
	private int mFavoriteListCount = 0;
	private Handler m_handler;
	private SDCardBroadcastReceiver m_sdcardbroadcastReceiver;
	private RelativeLayout m_imageviewLayout;
	private static RelativeLayout m_controlLayout;
	private static RelativeLayout m_popupLayout;
	private RelativeLayout m_MoreLayout;
	private RelativeLayout m_playbuttonLayout;
	// private FrameLayout m_framelayout;
	private SlideButton m_3DSwitchButton;
	private Button m_CameraSwitchButton;
	private Button m_MoreButton;
	public Button m_btn_play_video;
	private TextView m_Indicator;
	// private FrameLayout m_framelayout = null;
	private final static int CONTROL_HANDLER_APPEAR_CTRLS = 0x10; // 控制Handler：显示控制栏
	private final static int CONTROL_HANDLER_HIDE_CTRLS = 0x11; // 控制Handler：隐藏控制栏
	private final static int CONTROL_HANDLER_SHOW_NEXT = 0x12; //
	private final static int CONTROL_HANDLER_SHOW_PRA = 0x13; //
	private final static int CONTROL_HANDLER_SHOW_SOLID = 0x14; //
	public final static int CONTROL_HANDLER_SHOW_MOVEINFO = 0x15; //
	public final static int CONTROL_HANDLER_SHOW_MOVEINFO_FUCTION = 0x16; //
	public final static int CONTROL_HANDLER_HIDEDELAY_INMOVE = 0x17; //
	private static final long DELAY_HIDE_TIME = 3000; // 自动隐藏控件时间
	public final static int REQUESTCODE_WIIMAGEEDITOR = 6000;
	private boolean m_downisonthisview = false;// 判斷down事件是否在當前view上
	private boolean m_control_isPresseddown = false;
	public boolean m_isLandscape = true;
	private ShareMenu mShareMenu;
	private static MsgBox mMsgBox;
	private static int mMsgBox_type = 0;// 1为删除 提示，2为删光提示
	private FileInfoMsgBox mMsgBox_info;
	// 定义菜单变量
	private int[][] mMoreMenuResId = null;
	private int[][] mGalleryMoveToMenuResId = null;
	private ListView mMenuListView = null, mSubMenuListView = null;
	private ViewerListAdapter mMenuListAdapter = null, mSubListAdapter = null;
	public static int mListPosition = -1;
	public static int mSubListPosition = -1;
	public static ArrayList<Integer> mDisableList = null;
	private XmlPullParser mTextColorWhite = null;
	private XmlPullParser mTextColorGreen = null;
	public static ColorStateList COLORSTATELIST_WHITE = null;
	public static ColorStateList COLORSTATELIST_GREEN = null;
	private HashMap<Integer, Integer> mSubMenuStateMap;

	private Context mContext = null;
	private RelativeLayout mMenuLayout = null,// 主菜单layout
			mSubMenuLayout = null;// 子菜单layout
	// private RelativeLayout controlplayandshow_relativelayout;
	private TextView mSubListTitle = null;
	private static final int SUBMENU_MOVETO_INTERNAL = 0x00140;

	private int m_init_playmode = 0;// 定义进入时是否为幻灯播放
	private int m_init_loadimagenum = 0;
	public static MsgBox msgBox_disappearautoBox = null;
	private boolean isLaunchedFromInner = true;
	private static boolean m_move_istoinner = true;
	private static String m_move_filename = "";
	LoadBufImageTask loadtask0;//异步加载上一张
	LoadBufImageTask loadtask1;//异步加载下一张
	LoadImageTask task;//异步加载当前图片

	@Override
	public void finish()
	{
		if (CSStaticData.DEBUG)
			Log.e(Tag, "finish");
		// **********当有对话框时应当先销毁
		super.finish();
	}

	@Override
	protected void onDestroy()
	{
		if (CSStaticData.DEBUG)
			Log.e(Tag, "onDestroy");
		unregisterReceiver(m_sdcardbroadcastReceiver);
		if(m_isNextBitmapLoaded>0)
		{
			if(task!=null)
			task.cancel(true);
			if(loadtask1!=null)
			loadtask1.cancel(true);
			if(loadtask0!=null)
			loadtask0.cancel(true);
		}
		
		super.onDestroy();

	}

	@Override
	protected void onStart()
	{
		if (CSStaticData.DEBUG)
			Log.e(Tag, "onStart");
		super.onStart();
	}

	@Override
	protected void onResume()
	{
		if (CSStaticData.DEBUG)
			Log.e(Tag, "onResume");
		if (CSStaticData.g_is_3D_mode)
		{
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else
		{
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
		}
		setScreenDimension(CSStaticData.g_is_3D_mode);
		m_3DSwitchButton.setChecked(CSStaticData.g_is_3D_mode);
		
		//加载图片
		if(WiImageView.isRecycledD(m_imageview.m_curbitmap))
		{
			LoadImageTask.Callback callback = new LoadImageTask.Callback() {
				@Override
				public void onComplete(long time,Bitmap[] mBitmaps)
				{
					if(CSStaticData.DEBUG)
						Log.e(Tag, "[onComplete]------------------->生成图片用时"+time);
					    Log.e(Tag, "[onComplete]------------------->生成图片用时"+time);
					   // WiImageView.recycleDBitmap(m_imageview.m_curbitmap);
						m_imageview.setCurbitmap(mBitmaps);
						m_imageview.setBackgroundColor(Color.BLACK);
						m_imageview.resetRect();
						m_imageview.invalidate();
					    m_isNextBitmapLoaded--;
				}
	        };
	        task =new LoadImageTask(WiImageViewerActivity.this, callback,m_imageview);
			task.execute(m_bitmappicker.getCurrentFileName());
			m_isNextBitmapLoaded++;
		}
		if(WiImageView.isRecycledD(m_bitmaps[1]))
		loadBitmap(true);
		if(WiImageView.isRecycledD(m_bitmaps[0]))
		{
			if(m_bitmappicker.getCurrentFileIndex() > 0)
				loadBitmap(false);
		}
		updateUIinfo();
		
		
		mListPosition = -1;
		if (mMenuListAdapter != null)
		{
			mMenuListAdapter.notifyDataSetChanged();
		}
		m_handle_control.removeMessages(CONTROL_HANDLER_HIDE_CTRLS);
		m_handle_control.sendEmptyMessage(CONTROL_HANDLER_APPEAR_CTRLS);
		boolean bool = (mMsgBox != null && mMsgBox.isShown())
				|| (mMsgBox_info != null && mMsgBox_info.isShown());
		if (!bool)
			m_handle_control.sendEmptyMessageDelayed(
					CONTROL_HANDLER_HIDE_CTRLS, DELAY_HIDE_TIME);
		super.onResume();
	}

	@Override
	protected void onPause()
	{
		if (CSStaticData.DEBUG)
			Log.e(Tag, "onPause");
		stopsolidplay();
		mMenuLayout.setVisibility(View.GONE);
		m_MoreButton
				.setBackgroundResource(R.drawable.gallery_more_btn_selector);
		if (CSStaticData.g_is_3D_mode)
		{
			setScreenDimension(false);
		}
		showButtonsWithoutDelay();
		m_handle_control_outside.removeMessages(CONTROL_HANDLER_HIDEDELAY_INMOVE);
		super.onPause();
	}

	@Override
	protected void onStop()
	{
		// TODO Auto-generated method stub
		if (CSStaticData.DEBUG)
			Log.e(Tag, "onStop");
		WiImageView.recycleDBitmap(m_imageview.m_curbitmap);
		WiImageView.recycleDBitmap(m_imageview.m_nextbitmap);
		WiImageView.recycleDBitmap(m_imageview.m_tempbitmap);
		WiImageView.recycleDBitmap(m_bitmaps[0]);
		WiImageView.recycleDBitmap(m_bitmaps[1]);
		super.onStop();
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (CSStaticData.DEBUG)
			Log.e(Tag, "onCreate");
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		TDStaticData.SCREEN_WIDTH = getWindowManager().getDefaultDisplay()
				.getWidth();
		TDStaticData.SCREEN_HEIGHT = getWindowManager().getDefaultDisplay()
				.getHeight();
		TDStaticData.SCREEN_WIDTH_ORG = TDStaticData.SCREEN_WIDTH;
		TDStaticData.SCREEN_HEIGHT_ORG = TDStaticData.SCREEN_HEIGHT;

		// 特性
		// TDStaticData.SCREEN_WIDTH=1184;
		// TDStaticData.SCREEN_HEIGHT=720;
		// TDStaticData.SCREEN_WIDTH_ORG=1280;
		// TDStaticData.SCREEN_HEIGHT_ORG=720;

		screenWidth = TDStaticData.SCREEN_WIDTH_ORG;
		screenHeight = TDStaticData.SCREEN_HEIGHT_ORG;
		if (screenWidth < screenHeight)
		{
			m_isLandscape = false;
		}
		if (CSStaticData.DEBUG)
			Log.e(Tag, "屏幕长宽为" + screenWidth + "*" + screenHeight);
		Intent intent = WiImageViewerActivity.this.getIntent();
		String intentPath = intent.getDataString();
		if (intentPath != null)
		{
			// 第三方调用
			isLaunchedFromInner = false;
			intentPath = Uri.decode(intentPath);
			m_bitmappicker = new MediaFilePicker(intentPath);
		} else
		{
			// 内部调用通过gallery启动图片浏览器
			isLaunchedFromInner = true;
			String playmode = intent.getStringExtra("cmd");
			if (playmode != null && playmode.equals("sildeshow"))
			{
				m_init_playmode = 1;
			} else if (playmode != null && playmode.equals("play"))
			{
				m_init_playmode = 0;
			}
			String fileName = intent.getStringExtra("filePath");
			if (fileName != null)
			{
				List<String> fileList = new ArrayList<String>();
				fileList = intent.getStringArrayListExtra("fileList");
				m_bitmappicker = new MediaFilePicker(fileList, fileName);
			} else
			{
				try
				{
					m_bitmappicker = new MediaFilePicker(
							TDStaticData.VIEWMODE_ALL_VIEW, null);
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		// 自定义启动，扫描文件夹，然后自己生成文件列表
		// try
		// {
		// m_bitmappicker=new BitmapPicker(TDStaticData.VIEWMODE_ALL_VIEW,
		// null);
		// } catch (IOException e)
		// {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		setContentView(R.layout.wiimagevieweractivity_main);
		m_imageviewLayout = (RelativeLayout) findViewById(R.id.imageview_relativelayout);
		m_controlLayout = (RelativeLayout) findViewById(R.id.imageview_relativelayout_control);
		m_popupLayout = (RelativeLayout) findViewById(R.id.popup_relativelayout);
		m_playbuttonLayout = (RelativeLayout) findViewById(R.id.playbutton_relativelayout);
		m_MoreLayout = (RelativeLayout) findViewById(R.id.MenuLayout);
		// m_framelayout=(FrameLayout)
		// findViewById(R.id.imageviewer_framelayout);
		// 定义wiimageview层，并添加到framelayout
		m_imageview = new WiImageView(WiImageViewerActivity.this,
				m_bitmappicker);
		m_imageviewLayout.addView(m_imageview);
		initializeControls();
		// 设置事件处理
		initData();
		// 设置菜单初始化
		initListViewListenner();
		initializeListeners();
		m_bitmaps = new Bitmap[2][2];
		matrix = new Matrix();
		initStatusData();
		// 添加相应识别器
		m_gesturedetector = new GestureDetector(this);
		// 设置动画runnable
		setMovement();
		// 注册sd卡插拔广播
		registerSDMount();
		//lookupFavoriteInfo();
       m_isNextBitmapLoaded=0;
		// 初始化屏幕显示
	}

	/**
	 * 设置动画方式，上下一张切换，返回和幻灯片播放
	 */
	public void setMovement()
	{

		m_handler = new Handler();
		m_runnable1 = new Runnable()
		{

			@Override
			public void run()
			{
				if (m_isNext)
				{
					m_position -= (screenWidth / 12);
					int i = m_position;
					if (i > 0)
					{
						m_imageview.setSrc1Rect(0, 0, screenHeight, screenWidth
								/ 2 - i);
						m_imageview.setDst1Rect(i, 0, screenHeight,
								screenWidth / 2);
						m_imageview.setDst2Rect(i + screenWidth / 2, 0,
								screenHeight, screenWidth);
						m_imageview.setSrc2Rect(screenWidth / 2 - i, 0,
								screenHeight, screenWidth / 2);
						m_imageview.setDst3Rect(0, 0, screenHeight, i);
						m_imageview.setDst4Rect(screenWidth / 2, 0,
								screenHeight, screenWidth / 2 + i);
						m_imageview.postInvalidate();
					}

				} else
				{
					m_position += screenWidth / 12;
					int i = m_position;
					if (m_position < screenWidth / 2)
					{
						m_imageview.setSrc1Rect(screenWidth / 2 - i, 0,
								screenHeight, screenWidth / 2);
						m_imageview.setDst1Rect(0, 0, screenHeight, i);
						m_imageview.setDst2Rect(screenWidth / 2, 0,
								screenHeight, screenWidth / 2 + i);
						m_imageview.setSrc2Rect(0, 0, screenHeight, screenWidth
								/ 2 - i);
						m_imageview.setDst3Rect(i, 0, screenHeight,
								screenWidth / 2);
						m_imageview.setDst4Rect(screenWidth / 2 + i, 0,
								screenHeight, screenWidth);
						m_imageview.postInvalidate();
					}
				}

				if (m_position > 0 && m_position < screenWidth / 2)
				{
					m_handler.postDelayed(m_runnable1, 40);
				}
				/*
				 * if ((m_position <= 0 && m_position > -(screenWidth / 12)) ||
				 * (m_position >= screenWidth / 2 && m_position < (screenWidth /
				 * 2 + screenWidth / 12)))
				 */
				else
				{
					m_imageview.resetRect();
					m_imageview.setNextbitmap(null);
					m_imageview.postInvalidate();
					updateUIinfo();
					m_isMove = false;
					m_moveX = 0;
				}
			}
		};
		// 下一张的移动动画
		m_runnable = new Runnable()
		{

			@Override
			public void run()
			{
				if (m_isNext)
				{
					m_position -= (screenWidth / 12);
					int i = m_position;
					if (m_position > 0)
					{
						m_imageview.setSrc1Rect(screenWidth / 2 - i, 0,
								screenHeight, screenWidth / 2);
						m_imageview.setDst1Rect(0, 0, screenHeight, i);
						m_imageview.setDst2Rect(screenWidth / 2, 0,
								screenHeight, screenWidth / 2 + i);
						m_imageview.setSrc2Rect(0, 0, screenHeight, screenWidth
								/ 2 - i);

						m_imageview.setDst3Rect(i, 0, screenHeight,
								screenWidth / 2);
						m_imageview.setDst4Rect(screenWidth / 2 + i, 0,
								screenHeight, screenWidth);
						m_imageview.postInvalidate();
					}
				} else
				{
					m_position += screenWidth / 12;
					int i = m_position;
					if (m_position < screenWidth / 2)
					{
						m_imageview.setSrc1Rect(0, 0, screenHeight, screenWidth
								/ 2 - i);

						m_imageview.setDst1Rect(i, 0, screenHeight,
								screenWidth / 2);
						m_imageview.setDst2Rect(screenWidth / 2 + i, 0,
								screenHeight, screenWidth);
						m_imageview.setSrc2Rect(screenWidth / 2 - i, 0,
								screenHeight, screenWidth / 2);

						m_imageview.setDst3Rect(0, 0, screenHeight, i);
						m_imageview.setDst4Rect(screenWidth / 2, 0,
								screenHeight, screenWidth / 2 + i);
						m_imageview.postInvalidate();
					}

				}

				if (m_position > 0 && m_position < screenWidth / 2)
				{
					m_handler.postDelayed(m_runnable, 40);
				}

				/*
				 * if ((m_position <= 0 && m_position > -(screenWidth / 12)) ||
				 * (m_position >= screenWidth / 2 && m_position < (screenWidth /
				 * 2 + screenWidth / 12)))
				 */
				else
				{
					if (m_isNext)
					{
						WiImageView.recycleDBitmap(m_bitmaps[0]);
						m_bitmaps[0] = m_imageview.m_curbitmap;
					} else
					{
						WiImageView.recycleDBitmap(m_bitmaps[1]);
						m_bitmaps[1] = m_imageview.m_curbitmap;
					}
					m_imageview.resetRect();
					m_imageview.exchange();
					m_imageview.setNextbitmap(null);
					m_imageview.postInvalidate();
					updateUIinfo();
					m_isMove = false;
					m_moveX = 0;
				}
			}
		};

		// 渐变动画
		mrunnable2 = new Runnable()
		{

			@Override
			public void run()
			{
				int i = m_alpha;
				m_alpha = m_alpha + 15;
				if (m_alpha < 255)
				{
					if (m_imageview.mode != 0)
					{
						m_handler.postDelayed(mrunnable2, 25);
						m_imageview.setAlpha(i);
						m_imageview.postInvalidate();
					} else
					{
						if (CSStaticData.DEBUG)
							Log.e(Tag, "slide show exit wrong");
						/*
						 * m_imageview.setAlpha(0); m_imageview.exchange();
						 * m_imageview.setNextbitmap(null);
						 * m_imageview.resetRect();
						 * m_imageview.postInvalidate();
						 */
					}
				} else if (m_alpha >= 255 && m_alpha < 270)
				{
					m_imageview.setAlpha(0);
					WiImageView.recycleDBitmap(m_bitmaps[0]);
					m_bitmaps[0] = m_imageview.m_curbitmap;
					m_imageview.exchange();
					m_imageview.setNextbitmap(null);
					m_imageview.postInvalidate();
					m_bitmappicker.getNextBitmap();
					updateUIinfo();
					if (m_imageview.mode == 1)
					{
						// 设置下一张图片
						File file = new File(
								m_bitmappicker.getCurrentFileName());
						if (file.exists())
						{
							m_imageview.setNextbitmap(m_bitmaps[1]);
							m_bitmappicker.getNextBitmap();
							loadBitmap(true);
							m_bitmappicker.getPreBitmap();
							m_alpha = 0;
							m_handler.postDelayed(mrunnable2, 2000);
						} else
						{
							m_imageview.mode = 0;
						}
					}
				}

			}
		};
	}

	/**
	 * 注册sd卡插拔
	 */
	private void registerSDMount()
	{
		IntentFilter intentFilter = new IntentFilter(
				Intent.ACTION_MEDIA_MOUNTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
		intentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
		intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
		intentFilter.addDataScheme("file");
		m_sdcardbroadcastReceiver = new SDCardBroadcastReceiver();
		registerReceiver(m_sdcardbroadcastReceiver, intentFilter);// 注册监听函数

	}

	/**
	 * 控制空间层的隐藏消失，显示时，点击消失
	 */
	private void resetDelay()
	{
		// TODO Auto-generated method stub

		if (m_control_isshow)
		{
			m_handle_control.removeMessages(CONTROL_HANDLER_APPEAR_CTRLS);
			m_handle_control.removeMessages(CONTROL_HANDLER_HIDE_CTRLS);
			m_handle_control.sendEmptyMessage(CONTROL_HANDLER_HIDE_CTRLS);
		} else
		{
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
		m_handle_control_outside.removeMessages(CONTROL_HANDLER_HIDEDELAY_INMOVE);
	}

	private void hideButtonsWithDelay()
	{
		m_handle_control.removeMessages(CONTROL_HANDLER_HIDE_CTRLS);
		m_handle_control.sendEmptyMessage(CONTROL_HANDLER_APPEAR_CTRLS);
		m_handle_control.sendEmptyMessageDelayed(CONTROL_HANDLER_HIDE_CTRLS,
				DELAY_HIDE_TIME);
	}

	/**
	 * 初始化控件
	 */
	private void initializeControls()
	{
		if (CSStaticData.DEBUG)
			Log.e(Tag, "initializeControls");
		m_3DSwitchButton = (SlideButton) findViewById(R.id.toggleButtonDimension);
		m_CameraSwitchButton = (Button) findViewById(R.id.button2);
		m_MoreButton = (Button) findViewById(R.id.button1);
		m_btn_play_video = (Button) findViewById(R.id.imageview_play_video);
		m_Indicator = (TextView) findViewById(R.id.tx_title);
		m_Indicator.setText(R.string.imageview_title);
		msgBox_disappearautoBox = new MsgBox(WiImageViewerActivity.this);
		// msgBox_disappearautoBox.setMessage("...");
		// msgBox_disappearautoBox.hideDelay(2000);
		msgBox_disappearautoBox.setModelStatus(false);
		msgBox_disappearautoBox.addToLayout(m_popupLayout);
	}

	/**
	 * 设置控件层隐藏和显示
	 */
	public static void setCtlable(boolean bool)
	{
		if (bool)
		{
			m_controlLayout.setVisibility(View.VISIBLE);
		} else
		{
			m_controlLayout.setVisibility(View.GONE);
		}
	}

	/**
	 * 初始化按钮点击事件监听器
	 */
	private void initializeListeners()
	{
		m_3DSwitchButton.setOnChangedListener(new OnCheckedChangedListener()
		{

			@Override
			public void OnChecked(boolean isChecked)
			{
				int isLandscape = getRequestedOrientation();
				if (isChecked)
				{
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
					if (isLandscape == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
							|| isLandscape == ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
							|| isLandscape == ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT)
					{
						return;
					}
				} else
				{
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
				}
				CSStaticData.g_is_3D_mode = isChecked;
				setScreenDimension(isChecked);

			}
		});
		m_CameraSwitchButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(WiImageViewerActivity.this,
						com.wistron.WiCamera.WiCameraActivity.class);
				intent.putExtra("camera_cmd", "call_from_imageview");
				startActivity(intent);
				// finish();
			}
		});
		m_btn_play_video.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				if (CSStaticData.DEBUG)
					Log.e(Tag, "[m_btn_play_video] 点击事件");
				if (FileTypeHelper.isVideoFile(m_bitmappicker
						.getCurrentFileName()) && m_imageview.mode == 0)
				{
					Tplay(true);
				} else if (FileTypeHelper.isImageFile(m_bitmappicker
						.getCurrentFileName()) && m_imageview.mode == 0)
				{
					if (isPanoramaImage(m_bitmappicker.getCurrentFileName()))
					{
						callPanoramaViewer(m_bitmappicker.getCurrentFileName());
					}

				}

			}
		});
		m_MoreButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (mMenuLayout.getVisibility() == View.VISIBLE)
				{
					mMenuLayout.setVisibility(View.INVISIBLE);
					m_MoreButton
							.setBackgroundResource(R.drawable.gallery_more_btn_selector);
					hideButtonsWithDelay();
					return;
				}
				mListPosition = -1;
				updateMenuInfo(m_bitmappicker.getCurrentFileName());
				mMenuListView.setAdapter(mMenuListAdapter);
				mMenuLayout.setVisibility(View.VISIBLE);
				mSubMenuLayout.setVisibility(View.INVISIBLE);
				showButtonsWithoutDelay();
				m_MoreButton
						.setBackgroundResource(R.drawable.gallery_more_btn_click);
			}
		});
		m_MoreLayout.setOnTouchListener(new OnTouchListener()
		{

			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				// TODO Auto-generated method stub
				if (m_imageview.mode == 1)
				{
					stopsolidplay();
					return true;
				}
				if (mMenuLayout.getVisibility() == View.VISIBLE)
				{
					mMenuLayout.setVisibility(View.INVISIBLE);
					if (mShareMenu != null && mShareMenu.isShown())
					{
						mShareMenu.hideAndRemove();
					//	m_popupLayout.removeView(mShareMenu);
					}
					hideButtonsWithDelay();
					m_MoreButton
							.setBackgroundResource(R.drawable.gallery_more_btn_selector);
					return true;
				}
				return false;
			}
		});
		m_imageview.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent ev)
			{
				m_gesturedetector.onTouchEvent(ev);
				if (m_imageview.mode == 1 || m_isMove
						|| (m_isNextBitmapLoaded > 0))
				{
					m_stopdispatch = true;
				}
				if (m_stopdispatch)
				{
					switch (ev.getAction())
					{
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
				if (m_imageview.m_Scale == 1.0f)
				{
					switch (ev.getAction())
					{
					case MotionEvent.ACTION_DOWN:
					{
						m_downisonthisview = true;
						m_FirstTouchX = ev.getX();
						m_moveX = 0;
						m_moveIsOver = false;
						break;
					}
					case MotionEvent.ACTION_MOVE:
					{
						if (m_downisonthisview)
						{
							if (!m_moveIsOver)
							{
								float mCurX = ev.getX();
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
					case MotionEvent.ACTION_UP:
					{
						m_downisonthisview = false;
						m_moveIsOver = true;
						// Log.e(Tag,
						// "MotionEvent.ACTION_UPXXXXXXXXXXXXXXXXXXXXXXXXXXX");
						if (m_moveX < 0
								&& m_bitmappicker.getCurrentFileIndex() == (m_bitmappicker
										.getFileCounts() - 1) || m_moveX > 0
								&& m_bitmappicker.getCurrentFileIndex() == 0)
						{
							return true;
						}
						// Log.e(Tag, "m_isMove" + m_isMove);
						// Log.e(Tag, "m_moveX" + m_moveX);
						if (!m_isMove && m_moveX != 0)
						{
							if (Math.abs(m_moveX) < screenWidth / 4)
							{
								moveBack(m_moveX, true);
							} else
							{
								if (m_moveX > 0)
								{

									m_bitmappicker.getPreBitmap();
									loadBitmap(false);
								} else
								{

									m_bitmappicker.getNextBitmap();
									loadBitmap(true);
								}
								moveBack(m_moveX, false);
							}
							// setNumber();
						}
						m_moveX = 0;

					}
					}

				}
				/*
				 * else { switch (ev.getAction()) { case
				 * MotionEvent.ACTION_DOWN: Log.e(Tag, "scale!=1  ACTION_DOWN");
				 * break; case MotionEvent.ACTION_MOVE: Log.e(Tag,
				 * "scale!=1  ACTION_MOVE"); break; case MotionEvent.ACTION_UP:
				 * break; case MotionEvent.ACTION_POINTER_2_DOWN: break;
				 * 
				 * } }
				 */
				// return m_gesturedetector.onTouchEvent(arg1);
				// resetDelay();
				return true;
			}
		});
		//
		// m_csbtn_back.setOnClickListener(new OnClickListener()
		// {
		//
		// @Override
		// public void onClick(View v)
		// {
		// onBackPressed();
		// }
		// });
		//
		// m_csbtn_share.setOnClickListener(new OnClickListener()
		// {
		//
		// @Override
		// public void onClick(View v)
		// {
		// Tshare();
		//
		// }
		// });
		// m_csbtn_home.setOnClickListener(new OnClickListener()
		// {
		//
		// @Override
		// public void onClick(View v)
		// {
		// // TODO Auto-generated method stub
		// stopsolidplay();
		// Intent intent = new Intent(Intent.ACTION_MAIN);
		// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// pay more //
		// // attention
		// intent.addCategory(Intent.CATEGORY_HOME);
		// startActivity(intent);
		// }
		// });
		//
		// m_csbtn_set.setOnClickListener(new OnClickListener()
		// {
		// @Override
		// public void onClick(View v)
		// {
		// // TODO Auto-generated method stub
		// Tsetting();
		//
		// }
		// });
		// m_cstex_indi3d.setOnClickListener(new OnClickListener()
		// {
		// @Override
		// public void onClick(View v)
		// {
		// // TODO Auto-generated method stub
		// m_imageview.ChangeMode();
		//
		// }
		// });
		// m_csbtn_play.setOnClickListener(new OnClickListener()
		// {
		//
		// @Override
		// public void onClick(View v)
		// {
		// // TODO Auto-generated method stub
		// Tplay();
		//
		// }
		// });
		//
		// m_csbtn_delete.setOnClickListener(new OnClickListener()
		// {
		// @Override
		// public void onClick(View v)
		// {
		// // TODO Auto-generated method stub
		// Tdeletefile();
		// }
		// });
		m_MoreButton.setOnTouchListener(mButtonOntouchListener);
		m_3DSwitchButton.setOnTouchListener(mButtonOntouchListener);
		m_CameraSwitchButton.setOnTouchListener(mButtonOntouchListener);
	}

	/**
	 * 设置播放，设置，分享，删除按钮为可用
	 */
	public void setControlEnable()
	{
		// 设置按钮的可用性
		// m_csbtn_play.setEnable(true);
		// m_csbtn_set.setEnable(true);
		// m_csbtn_share.setEnable(true);
		// m_csbtn_delete.setEnable(true);
	}

	/**
	 * Handler 控制控件消失和显示
	 */
	Handler m_handle_control = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			// TODO Auto-generated method stub
			switch (msg.what)
			{
			case CONTROL_HANDLER_APPEAR_CTRLS:
				// setCtlable();
				setCtlable(true);
				m_control_isshow = true;
				break;
			case CONTROL_HANDLER_HIDE_CTRLS:
				setCtlable(false);
				m_control_isshow = false;
				break;
			case CONTROL_HANDLER_SHOW_NEXT:
				m_handler.post(m_runnable);
				break;
			case CONTROL_HANDLER_SHOW_PRA:
				break;
			case CONTROL_HANDLER_SHOW_SOLID:
				solidShow();
			case CONTROL_HANDLER_SHOW_MOVEINFO_FUCTION:

				MsgBox msgBox = new MsgBox(WiImageViewerActivity.this);
				msgBox.setMessage("...");
				msgBox.hideDelay(2000);
				msgBox.setModelStatus(false);
				msgBox.addToLayout(m_popupLayout);
				msgBox.show();
				break;
			}
			super.handleMessage(msg);
		}
	};
	/**
	 * Handler 控制控件消失和显示
	 */
	public static Handler m_handle_control_outside = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			String temp = "";
			// TODO Auto-generated method stub
			switch (msg.what)
			{
			case CONTROL_HANDLER_SHOW_MOVEINFO:
				if (msg.obj != null)
				{
					boolean isSuccess = false;
					if (msg.arg1 <= 0)
					{
						isSuccess = false;
					} else
					{
						isSuccess = true;
					}

					if (isSuccess)
					{
						String genfilepath = "";
						if (m_move_istoinner)
						{
							temp = m_move_filename
									+ " has been moved to internal storage";
							genfilepath = CSStaticData.TMP_INT_DIR
									+ m_move_filename;
						} else
						{
							temp = m_move_filename
									+ " has been moved to external storage";
							genfilepath = CSStaticData.TMP_EXT_DIR
									+ m_move_filename;
						}
						m_bitmappicker.updateCurrentFilePath(genfilepath);
					} else
					{
						if (m_move_istoinner)
						{
							temp = "fail to move" + m_move_filename
									+ " to internal storage";
						} else
						{
							temp = "fail to move" + m_move_filename
									+ " to external storage";
						}
					}

				}
				mMsgBox.updateMessage(temp);
				mMsgBox_type=0;
				mMsgBox.hideDelay(2000);
				sendEmptyMessageDelayed(CONTROL_HANDLER_HIDEDELAY_INMOVE, DELAY_HIDE_TIME);
				
				//hideButtonsWithDelay();
				break;
			case CONTROL_HANDLER_HIDEDELAY_INMOVE:
				setCtlable(false);
				m_control_isshow = false;
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public boolean onDown(MotionEvent arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * back键方法重写
	 */
	@Override
	public void onBackPressed()
	{
		setControlEnable();
		if (mMsgBox != null && mMsgBox.isShown())
		{
			mMsgBox.hide();
			m_popupLayout.removeView(mMsgBox);
			if (m_isSolidPlay)
			{
				solidShow();
				m_isSolidPlay = false;
			}
			hideButtonsWithDelay();
			if(m_bitmappicker.getCurrentFileIndex() == -1)
			{
				//文件为空，自动退出
				super.onBackPressed();
			}
			return;
		}
		if (mMsgBox_info != null && mMsgBox_info.isShown())
		{
			mMsgBox_info.hideAndRemove();
			hideButtonsWithDelay();
			if (m_isSolidPlay)
			{
				solidShow();
				m_isSolidPlay = false;
			}
			return;
		}
		if (mShareMenu != null && mShareMenu.isShown())
		{
			mShareMenu.hideAndRemove();
			//m_popupLayout.removeView(mShareMenu);
			if (m_isSolidPlay)
			{
				solidShow();
				m_isSolidPlay = false;
			}

			mListPosition = -1;
			mMenuListAdapter.notifyDataSetChanged();
			return;
		}
		if (m_imageview.mode == 1)
		{
			stopsolidplay();
			return;
		}

		if (m_isMove)
		{
			m_handler.removeCallbacks(m_runnable);
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
			m_MoreButton
					.setBackgroundResource(R.drawable.gallery_more_btn_selector);
			return;
		}
		super.onBackPressed();
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
				Log.e(Tag, "MotionEvent.ACTION_DOWN");
				showButtonsWithoutDelay();
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				hideButtonsWithDelay();
				Log.e(Tag, "MotionEvent.ACTION_UP");
				break;
			default:
				break;
			}
			return false;
		}
	};

	//
	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent msg)
	// {
	// Log.e("onKeyDown", "ok");
	// resetDelayonFling();
	// if (m_isMove)
	// {
	//
	// } else
	// {
	//
	// if (m_imageview.mode == 1 && keyCode != KeyEvent.KEYCODE_BACK)
	// {
	// m_csbtn_set.setEnable(true);
	// m_csbtn_share.setEnable(true);
	// m_csbtn_delete.setEnable(true);
	// m_csbtn_play.setEnable(true);
	// stopsolidplay();
	// return super.onKeyDown(keyCode, msg);
	// }
	// if (TDStaticData.g_msgbox == null)
	// {
	//
	// if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
	// {
	// if (m_imageview.mode == 1)
	// {
	// stopsolidplay();
	// } else
	// {
	// if(m_isNextBitmapLoaded)
	// {
	// if (m_bitmappicker.getCurrentFileIndex() < (m_bitmappicker
	// .getFileCounts() - 1))
	// {
	// showNext(true);
	//
	// }
	// }
	// }
	// }
	// if (keyCode == KeyEvent.KEYCODE_S)
	// {
	// Tshare();
	//
	// }
	// if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
	// {
	//
	// if (m_imageview.mode == 1)
	// {
	// stopsolidplay();
	// } else
	// {
	// if(m_isNextBitmapLoaded)
	// {
	// if (m_bitmappicker.getCurrentFileIndex() > 0)
	// {
	// showNext(false);
	//
	// }
	// }
	// }
	// }
	// if (keyCode == KeyEvent.KEYCODE_A)
	// {
	// Tplay();
	//
	// }
	// // if (keyCode == KeyEvent.KEYCODE_V)
	// // {
	// //
	// // matrix.postScale(1.1f, 1.1f, TDStaticData.SCREEN_WIDTH / 4,
	// // TDStaticData.SCREEN_HEIGHT / 2);
	// // m_imageview.setMatrix(matrix);
	// //
	// // }
	// // if (keyCode == KeyEvent.KEYCODE_B)
	// // {
	// //
	// // matrix.postScale(0.9f, 0.9f, TDStaticData.SCREEN_WIDTH / 4,
	// // TDStaticData.SCREEN_HEIGHT / 2);
	// // m_imageview.setMatrix(matrix);
	// //
	// // }
	// if (keyCode == KeyEvent.KEYCODE_I)
	// {
	// Tsetting();
	//
	// }
	// if (keyCode == KeyEvent.KEYCODE_D)
	// {
	// Tdeletefile();
	//
	// }
	// }
	//
	// }
	// if (TDStaticData.g_msgbox != null)
	// {
	// switch (keyCode)
	// {
	// case KeyEvent.KEYCODE_Y:
	// TDStaticData.g_msgbox.yesPerformClick();
	// m_csbtn_play.setEnable(true);
	// m_csbtn_set.setEnable(true);
	// m_csbtn_share.setEnable(true);
	// m_csbtn_delete.setEnable(true);
	// break;
	// case KeyEvent.KEYCODE_N:
	// TDStaticData.g_msgbox.noPerformClick();
	// break;
	// case KeyEvent.KEYCODE_C:
	// TDStaticData.g_msgbox.cancelPerformClick();
	// break;
	// }
	// }
	// Log.e("onKeyDown", "ok");
	// return super.onKeyDown(keyCode, msg);
	//
	// }
	//

	/**
	 * 触发fling事件，显示上下一张切换
	 */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY)
	{
		if (CSStaticData.DEBUG)
			Log.e(Tag, "onFling");
		// TODO Auto-generated method stub
		// && Math.abs(velocityX) > 200
		// resetDelayonFling();
		if ((int) m_imageview.m_rotate % 90 != 0)
		{
			return false;
		}
		if (m_imageview.mode == 1)
			stopsolidplay();
		else
		{

			// ******如果没有对话框则
			if (true)// TDStaticData.g_msgbox == null
			{
				float x1 = 0;
				float x2 = 0;
				float vx = 0;
				try
				{
					x1 = e1.getX();
					x2 = e2.getX();
					vx = velocityX;
				} catch (Exception e)
				{
					// TODO: handle exception
					e.printStackTrace();
					return false;
				}
				if (Math.abs(x1 - x2) > 100 && vx < -200)
				{
					if (m_isMove || (m_isNextBitmapLoaded > 0))
					{

					} else
					{
						if (m_bitmappicker.getCurrentFileIndex() < (m_bitmappicker
								.getFileCounts() - 1))
						{

							if (m_imageview.m_Scale != 1)
							{
								m_imageview.setNextbitmap(null);
							}
							if (m_moveX > 0)
							{
								moveBack(m_moveX, true);
							} else
							{
								showNext(true);
							}

							// setNumber();
						}
					}
				} else if (Math.abs(x2 - x1) > 100 && vx > 200)
				{
					if (m_isMove || (m_isNextBitmapLoaded > 0))
					{

					} else
					{
						if (m_bitmappicker.getCurrentFileIndex() > 0)
						{
							if (m_imageview.m_Scale != 1)
							{
								m_imageview.setNextbitmap(null);
							}
							if (m_moveX < 0)
							{
								moveBack(m_moveX, true);
							} else
							{
								showNext(false);
							}
							// setNumber();
						}
					}
				}
			}
		}

		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0)
	{
		// TODO Auto-generated method stub
		if (CSStaticData.DEBUG)
			Log.e(Tag, "onSingleTapUp");
		resetDelay();
		stopsolidplay();
		return false;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		// TODO Auto-generated method stub
		if (mMsgBox != null && mMsgBox.isShown())
		{
			return mMsgBox.dispatchTouchEvent(ev);
		}
		if (mMsgBox_info != null && mMsgBox_info.isShown())
		{
			// Log.e(Tag, "mMsgBox_info ***********");
			// mMsgBox_info.dispatchTouchEvent(ev);
			// if (!mMsgBox_info.getMsgBoxTouched()) {
			// mMsgBox_info.hide();
			// m_popupLayout.removeView(mMsgBox_info);
			// if (m_isSolidPlay)
			// {
			// m_isSolidPlay = false;
			// solidShow();
			// }
			// }
			// mMsgBox_info.setMsgBoxTouched(false);
			mMsgBox_info.dispatchTouchEvent(ev);
			if (!mMsgBox_info.isShown())
				hideButtonsWithDelay();
			return true;
		}
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 更据当前文件序号，或者文件类型来更新UI
	 */
	public void updateUIinfo()
	{
		if (m_imageview.mode == 1)
			return;
		int visi = m_btn_play_video.getVisibility();
		boolean isshow;
		if (visi == View.VISIBLE)
		{
			isshow = true;
		} else
		{
			isshow = false;
		}
		if (CSStaticData.DEBUG)
			Log.e(Tag, "[updateUIinfo]----------------------->isshow=" + isshow);
		if (FileTypeHelper.isImageFile(m_bitmappicker.getCurrentFileName()))
		{
			// 图片文件
			if (isPanoramaImage(m_bitmappicker.getCurrentFileName()))
			{
				// 全景图标显示
				if (isshow)
				{
					m_btn_play_video
							.setBackgroundResource(R.drawable.panorameviiewer_icon);
				} else
				{
					ButtonAnim(m_btn_play_video, true,
							R.drawable.panorameviiewer_icon);
				}
			} else
			{
				// 图标隐藏
				if (isshow)
				{
					ButtonAnim(m_btn_play_video, false, 0);
				}
			}
			m_imageview.isScalable = true;
			m_imageview.isRotatable = true;
		} else if (FileTypeHelper.isVideoFile(m_bitmappicker
				.getCurrentFileName()))
		{
			// 视频图标显示
			if (isshow)
			{
				m_btn_play_video
						.setBackgroundResource(R.drawable.video_review_icon);
			} else
			{
				ButtonAnim(m_btn_play_video, true, R.drawable.video_review_icon);
			}
			m_imageview.isScalable = false;
			m_imageview.isRotatable = false;
		} else
		{
			// 未知文件则隐藏图标。
			if (isshow)
			{
				ButtonAnim(m_btn_play_video, false, 0);
			}
			m_imageview.isScalable = false;
			m_imageview.isRotatable = false;
		}

	}

	public void ButtonAnim(final Button bt, final boolean isshow, int background)
	{
		try
		{
			bt.setVisibility(View.VISIBLE);
			bt.setBackgroundResource(background);
			if (isshow)
			{
				if (bt.getParent() == null)
				{
					m_playbuttonLayout.addView(bt);
				}
			}
			AnimationSet as = new AnimationSet(true);
			AlphaAnimation al;
			if (isshow)
			{
				al = new AlphaAnimation(0, 1);
			} else
			{
				al = new AlphaAnimation(1, 0);
			}
			al.setDuration(400);
			al.setFillAfter(true);
			as.addAnimation(al);
			as.setFillAfter(true);
			bt.startAnimation(as);
			as.setAnimationListener(new AnimationListener()
			{

				@Override
				public void onAnimationStart(Animation arg0)
				{
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationRepeat(Animation arg0)
				{
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationEnd(Animation arg0)
				{
					// TODO Auto-generated method stub
					if (CSStaticData.DEBUG)
						Log.e(Tag,
								"[onAnimationEnd]--------------------------->isshow="
										+ isshow);
					if (!isshow)
					{
						bt.setVisibility(View.GONE);
						if (bt.getParent() != null)
							m_playbuttonLayout.removeView(bt);
					}
				}
			});
		} catch (Exception e)
		{
			// TODO: handle exception
			if (CSStaticData.DEBUG)
				Log.e(Tag, "an exception is detected when setting icon state");
		}

	}

	/**
	 * 幻灯播放
	 */
	public void solidShow()
	{

		if (m_bitmappicker.getFileCounts() > 1)
		{
			if (CSStaticData.DEBUG)
				Log.e(Tag, "[solidShow]---------->幻灯播放开始");
			m_imageview.mode = 1;
			m_imageview.setNextbitmap(m_bitmaps[1]);
			m_bitmappicker.getNextBitmap();
			loadBitmap(true);
			m_bitmappicker.getPreBitmap();
			m_alpha = 0;
			m_handler.postDelayed(mrunnable2, 2000);
			// m_handler.post(mrunnable2);
			if (m_btn_play_video.getParent() != null)
			{
				m_btn_play_video.setVisibility(View.GONE);
				m_playbuttonLayout.removeView(m_btn_play_video);
			}

		}

	}

	/**
	 * 设置按钮功能,用于显示文件信息等等
	 */
	public void Tsetting()
	{
		if (m_isMove || m_bitmappicker.getFileCounts() <= 0)
			return;
		if (m_imageview.mode == 1)
		{
			m_isSolidPlay = true;
			stopsolidplay();
		}
		SpannableString[] string = null;
		mMsgBox_info = new FileInfoMsgBox(mContext);
		if (FileTypeHelper.isImageFile(m_bitmappicker.getCurrentFileName()))
		{
			ImageInfo m_imaImageInfo = new ImageInfo(WiImageViewerActivity.this);
			ImageInfoBean m_ImageInfoBean = m_imaImageInfo
					.getImagesInfo(m_bitmappicker.getCurrentFileName());
			if (m_ImageInfoBean != null)
			{
				string = m_ImageInfoBean.showInfoList();
				mMsgBox_info.setMessage(string);
			} else
			{
				mMsgBox_info.setMessage("Fail to get info!");
			}

		} else if (FileTypeHelper.isVideoFile(m_bitmappicker
				.getCurrentFileName()))
		{
			VideoProvider m_videoProvider = new VideoProvider(
					WiImageViewerActivity.this);
			VideoInfo m_videoinfo = m_videoProvider.getVideoInfo(m_bitmappicker
					.getCurrentFileName());
			if (m_videoinfo != null)
			{
				string = m_videoinfo.showInfoList();
				mMsgBox_info.setMessage(string);
			} else
			{
				mMsgBox_info.setMessage("Fail to get info!");
			}
		}
		mMsgBox_info.setTitle("Details");
		mMsgBox_info.setClickBlankHide(true);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				400, LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		layoutParams.setMargins(0, 80, 0, 20);
		mMsgBox_info.setLayoutParams(layoutParams);

		mMsgBox_info.addToLayout(m_popupLayout);
	}

	/**
	 * 播放按钮功能
	 */
	public void Tplay(boolean isplayvideo)
	{
		if (!m_SDismounted)
		{
			if (!m_isMove)
			{
				if (m_bitmappicker.getFileCounts() <= 0)
					return;
				m_isSolidPlay = false;
				if (isplayvideo)
				{
					if (FileTypeHelper.isVideoFile(m_bitmappicker
							.getCurrentFileName()) && m_imageview.mode == 0)
					{
						ArrayList<String> mList = m_bitmappicker.getVideoList();
						Intent intent = new Intent(WiImageViewerActivity.this,
								WiVideoViewerActivity.class);
						intent.putExtra("filePath",
								m_bitmappicker.getCurrentFileName());
						intent.putStringArrayListExtra("fileList", mList);
						intent.putExtra("cmd", "sildeshow");
						startActivityForResult(intent,
								TDStaticData.REQUEST_CODE_LOAD_VIDEO);
					}
				} else
				{
					if (m_imageview.mode == 0)
					{
						solidShow();

					} else
					{
						stopsolidplay();
					}
				}
			}
		}
	}

	/**
	 * 分享按钮功能
	 */
	public void Tshare()
	{
		if (m_isMove)
			return;
		if (m_bitmappicker.getFileCounts() <= 0)
			return;
		if (m_imageview.mode == 1)
		{
			m_isSolidPlay = true;
			stopsolidplay();
		}
		mShareMenu = new ShareMenu(WiImageViewerActivity.this,
				0, 0);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(300, ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		layoutParams.setMargins(0,60, 0, 0);
		mShareMenu.setShareLayoutParams(layoutParams);
		mShareMenu.setAnchor(ShareMenu.ANCHOR_TOP_RIGHT);
		String m_filePath_menuString = "file:/"
				+ m_bitmappicker.getCurrentFileName();
		ArrayList<String> m_ArrayList = new ArrayList<String>();
		if (FileTypeHelper.isImageFile(m_bitmappicker.getCurrentFileName()))
		{
			m_ArrayList.add(m_filePath_menuString);
			mShareMenu.setImagePath(m_ArrayList);
		} else
		{
			m_ArrayList.add(m_filePath_menuString);
			mShareMenu.setVideoPath(m_ArrayList);
		}
		mShareMenu.addToLayout(mMenuLayout);
		mShareMenu.show();
	}

	/**
	 * 删除按钮功能
	 */
	public void Tdeletefile()
	{
		if (m_isMove)
			return;
		if (m_bitmappicker.getFileCounts() <= 0)
			return;
		if (m_imageview.mode == 1)
		{
			m_isSolidPlay = true;
			stopsolidplay();
		}
		String mString = "Do you want to delete this image?";
		if (FileTypeHelper.isVideoFile(m_bitmappicker.getCurrentFileName()))
			mString = "Do you want to delete this video?";
		stopsolidplay();

		mMsgBox = new MsgBox(mContext);
		mMsgBox_type = 1;
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
						setControlEnable();
						// 添加删除
						AsyncFileProvider asyncer = null;
						if (WiGalleryOpenGLRenderer.mAsyncFileProvider == null)
						{
							asyncer = new AsyncFileProvider(mContext);
						} else
						{
							asyncer = WiGalleryOpenGLRenderer.mAsyncFileProvider;
						}
						List<String> temp = new ArrayList<String>();
						temp.add(m_bitmappicker.getCurrentFileName());
						asyncer.deteleFiles(temp, false, true, WiImageViewerActivityID);
						// 更新cameraReview的链表信息。
						if (WiCameraActivity.m_main_handle != null)
						{
							Message deletemesg = new Message();
							if (FileTypeHelper.isVideoFile(m_bitmappicker
									.getCurrentFileName()))
							{
								deletemesg.what = InterSurfaceView.CAMERA_VIDEOLIST_UPDATE ;
							} else
							{
								deletemesg.what = InterSurfaceView.CAMERA_PICLIST_UPDATE;
							}
							deletemesg.obj = m_bitmappicker
									.getCurrentFileName();
							WiCameraActivity.m_main_handle
									.sendMessage(deletemesg);
						}
						// if(WiGalleryOpenGLRenderer.mAsyncFileProvider != null
						// && WiGalleryOpenGLRenderer.mAsyncFileProvider !=
						// null){
						// List<String> temp=new ArrayList<String>();
						// temp.add(m_bitmappicker.getCurrentFileName());
						// WiGalleryOpenGLRenderer.mAsyncFileProvider.deteleFiles(temp,
						// false);
						// }

						boolean nextorpre = true;
						if (m_bitmappicker.getCurrentFileIndex() == m_bitmappicker
								.getFileCounts() - 1)
						{
							nextorpre = false;
						}

						m_bitmappicker.deleteFile();
						updateUIinfo();
						mMsgBox.hide();
						m_popupLayout.removeView(mMsgBox);
						if (m_bitmappicker.getCurrentFileIndex() == -1)
						{
							showInfoWhenDeleteAll();
						} else
						{
							m_imageview.resetRect();
							if (!nextorpre)
							{
								m_imageview.setCurbitmapR(m_bitmaps[0]);
								loadBitmap(false);
							} else
							{
								m_imageview.setCurbitmapR(m_bitmaps[1]);
								loadBitmap(true);
							}
							m_imageview.resetRect();
							m_imageview.invalidate();
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
						// TODO Auto-generated method stub
						mMsgBox.hide();
						m_popupLayout.removeView(mMsgBox);
						setControlEnable();
						if (m_isSolidPlay)
						{
							m_isSolidPlay = false;
							solidShow();
						}
						hideButtonsWithDelay();
					}
				});
		mMsgBox.addToLayout(m_popupLayout);
		mMsgBox.show();
	}

	/**
	 * 触发显示下一张图片的动画
	 */
	public void showNext(boolean bool)
	{
		if (bool)
		{
			m_isNext = true;
			m_imageview.setNextbitmap(m_bitmaps[1]);
			m_bitmappicker.getNextBitmap();
			loadBitmap(true);

			m_position = screenWidth / 2 + (int) m_moveX;

		} else
		{
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
	public void showNextOnMove(boolean bool)
	{

		// true 下一張，false 上一張
		if (bool)
		{
			if (m_bitmappicker.getCurrentFileIndex() < (m_bitmappicker
					.getFileCounts() - 1))
			{

				/*
				 * if (m_imageview.m_nextbitmap == null) {
				 * m_imageview.setNextbitmap(m_bitmaps[1]); }
				 */
				m_imageview.setNextbitmap(m_bitmaps[1]);
				m_imageview.setSrc1Rect(-(int) m_moveX, 0, screenHeight,
						screenWidth / 2);
				m_imageview.setDst1Rect(0, 0, screenHeight, (int) m_moveX
						+ screenWidth / 2);
				m_imageview.setDst2Rect(screenWidth / 2, 0, screenHeight,
						screenWidth + (int) m_moveX);
				m_imageview.setSrc2Rect(0, 0, screenHeight, -(int) m_moveX);

				m_imageview.setDst3Rect((int) m_moveX + screenWidth / 2, 0,
						screenHeight, screenWidth / 2);
				m_imageview.setDst4Rect(screenWidth + (int) m_moveX, 0,
						screenHeight, screenWidth);
				m_imageview.invalidate();

			}

		} else
		{
			if (m_bitmappicker.getCurrentFileIndex() > 0)
			{/*
			 * if (m_imageview.m_nextbitmap == null) {
			 * m_imageview.setNextbitmap(m_bitmaps[0]); }
			 */
				m_imageview.setNextbitmap(m_bitmaps[0]);
				m_imageview.setSrc1Rect(0, 0, screenHeight, screenWidth / 2
						- (int) m_moveX);

				m_imageview.setDst1Rect((int) m_moveX, 0, screenHeight,
						screenWidth / 2);
				m_imageview.setDst2Rect(screenWidth / 2 + (int) m_moveX, 0,
						screenHeight, screenWidth);
				m_imageview.setSrc2Rect(screenWidth / 2 - (int) m_moveX, 0,
						screenHeight, screenWidth / 2);

				m_imageview.setDst3Rect(0, 0, screenHeight, (int) m_moveX);
				m_imageview.setDst4Rect(screenWidth / 2, 0, screenHeight,
						screenWidth / 2 + (int) m_moveX);
				m_imageview.invalidate();

			}

		}
		// Log.e(Tag, "show next on move over XXXXXXXXXXXXXXXXXXX");
	}

	/**
	 * 手指松开时，移回到原图的动画
	 */
	public void moveBack(float X, boolean bool)
	{
		m_isMove = true;
		if (X < 0)
		{
			m_isNext = false;
			m_position = screenWidth / 2 + (int) X;
			if (!bool)
			{
				m_isNext = true;
			}
		} else
		{
			m_isNext = true;
			m_position = (int) X;
			if (!bool)
			{
				m_isNext = false;
			}
		}
		if (!bool)
			m_handler.post(m_runnable);
		else
		{
			m_handler.post(m_runnable1);
		}
	}

	/**
	 * 停止幻灯片播放
	 */
	public void stopsolidplay()
	{

		if (m_imageview.mode == 1)
		{
			if (CSStaticData.DEBUG)
				Log.e(Tag, "[stopsolidplay]---------->幻灯播放结束 ");
			m_handler.removeCallbacks(mrunnable2);
			m_imageview.mode = 0;
			if (m_imageview.getPAlpha() < 127)
			{
				WiImageView.recycleDBitmap(m_bitmaps[1]);
				m_bitmaps[1] = m_imageview.m_nextbitmap;
				m_imageview.setNextbitmap(null);
			} else
			{
				WiImageView.recycleDBitmap(m_bitmaps[0]);
				m_bitmaps[0] = m_imageview.m_curbitmap;
				m_imageview.exchange();
				m_imageview.setNextbitmap(null);
				m_bitmappicker.getNextBitmap();
			}
			m_imageview.setAlpha(0);
			m_imageview.postInvalidate();
			updateUIinfo();
			hideButtonsWithDelay();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (CSStaticData.DEBUG)
		{

			Log.e("onActivityResult", "程序返回标示");
			Log.e(Tag, "requestCode=" + requestCode + "resultCode="
					+ resultCode + data);
		}
		switch (requestCode)
		{
		case TDStaticData.REQUEST_CODE_LOAD_VIDEO:
			if (resultCode == RESULT_OK)
			{
				List<String> deletelist = data
						.getStringArrayListExtra("deletelist");
				if (deletelist != null)
				{
					if (CSStaticData.DEBUG)
						Log.e(Tag,
								"当前的文件索引为"
										+ m_bitmappicker.getCurrentFileIndex()
										+ m_bitmappicker.getCurrentFileName());
					m_bitmappicker.deleteFileList(deletelist);
					if (CSStaticData.DEBUG)
						Log.e(Tag,
								"删除后当前的文件索引为"
										+ m_bitmappicker.getCurrentFileIndex()
										+ m_bitmappicker.getCurrentFileName());

					if (m_bitmappicker.getCurrentFileIndex() == -1)
					{
						showInfoWhenDeleteAll();
					} else
					{
						// 显示删除后的图片
						m_imageview
								.setCurbitmapR(m_imageview
										.getNextBitmapEx(m_bitmappicker
												.getFirBitmap()));
						m_imageview.resetRect();
						m_imageview.invalidate();
						// 加载上下一张图片
						OperateBitmaps.recycleDBitmap(m_bitmaps[0]);
						OperateBitmaps.recycleDBitmap(m_bitmaps[1]);
						loadBitmap(false);
						loadBitmap(true);
					}
				}
				updateUIinfo();
			}
			break;
		case REQUESTCODE_WIIMAGEEDITOR:
			if (resultCode == RESULT_OK)
			{
				String redeyefilepath = data.getStringExtra("redeyefilepath");
				if (redeyefilepath != null)
				{
					// 更新链表，然后刷新图片。
					m_bitmappicker.addFile(redeyefilepath);
					OperateBitmaps.recycleDBitmap(m_bitmaps[1]);
					loadBitmap(true);
					if (WiGalleryOpenGLRenderer.mAsyncFileProvider != null)
					{
						Log.e(Tag, "向gallery中添加数据");
						WiGalleryOpenGLRenderer.mAsyncFileProvider
								.addNewFile(redeyefilepath);
					}
				}
			}
			break;
		}
	}

	public void showInfoWhenDeleteAll()
	{
//		{
//			mMsgBox = new MsgBox(mContext);
//			mMsgBox_type = 2;
//			mMsgBox.setMessage(getResources().getText(
//					R.string.gallery_delete_warn));
//			mMsgBox.setBackgound(R.drawable.main_menu_window_portrait);
//			mMsgBox.addToLayout(m_popupLayout);
////			mMsgBox.setPositiveButton(
////					getResources()
////							.getText(
////									R.string.gallery_delete_warn_confirm),
////					new OnClickListener()
////					{
//	//
////						@Override
////						public void onClick(View v)
////						{
////							// TODO Auto-generated method stub
////							mMsgBox.hide();
////							m_popupLayout.removeView(mMsgBox);
////							// finish();
////						}
////					});
//			mMsgBox.show();
//			m_imageview.setCurbitmap(null);
//			m_imageview.invalidate();
//			m_MoreButton.setClickable(false);
//		}
		
		finish();
		
	}

	/**
	 * 异步加载下一张要显示的图片
	 */
	public void loadBitmapInBackground(String string, int next)
	{
		if(next==0)
		{
			 loadtask0 = new LoadBufImageTask(this);
			 loadtask0.execute(string, next);
		}
		else if(next==1)
		{
		   loadtask1 = new LoadBufImageTask(this);
		   loadtask1.execute(string, next);
		}
		
		
	}

	public void loadBitmap(Boolean isnext)
	{
		if(m_bitmappicker.getFileCounts()>0)
		{
			if (isnext)
			{
				loadBitmapInBackground(m_bitmappicker.getNextBitmapforBuf(), 1);
				Log.e(Tag, "[loadBitmap]------------>加载下一张图片");
			} else
			{
				// if (m_bitmappicker.getCurrentFileIndex() > 0)
				// {
				loadBitmapInBackground(m_bitmappicker.getPreBitmapforBuf(), 0);
				Log.e(Tag, "[loadBitmap]------------>加载上一张图片");
				// }
			}
		}
	}

	class LoadBufImageTask extends AsyncTask<Object, Integer, Object>
	{
		// 可变长的输入参数，与AsyncTask.exucute()对应
		Bitmap[] bufBitmaps;
		int whichtoupdate;

		public LoadBufImageTask(Context context)
		{
			m_isNextBitmapLoaded++;
			bufBitmaps = new Bitmap[2];
		}

		@Override
		protected String doInBackground(Object... params)
		{
			bufBitmaps = m_imageview.getNextBitmapEx((String) params[0]);
			whichtoupdate = (Integer) params[1];

			return null;

		}

		@Override
		protected void onCancelled()
		{
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Object result)
		{
			Log.e(Tag, "[onPostExecute]---------->異步加載完成,更新的方向为"
					+ whichtoupdate);
			if (CSStaticData.DEBUG)
				Log.e(Tag, "[onPostExecute]---------->異步加載完成,更新的方向为"
						+ whichtoupdate);
			if (whichtoupdate == 0)
				m_bitmaps[0] = bufBitmaps;
			else
			{
				m_bitmaps[1] = bufBitmaps;
			}
			m_isNextBitmapLoaded--;
			m_init_loadimagenum++;
			if (m_init_loadimagenum == 2)
			{
				if (m_init_playmode == 1)
				{
					m_init_playmode = 0;
					m_handle_control
							.sendEmptyMessage(CONTROL_HANDLER_SHOW_SOLID);
					// m_handle_control.sendEmptyMessageDelayed(CONTROL_HANDLER_SHOW_SOLID,2000);
				}
			}
		}

		@Override
		protected void onPreExecute()
		{
			// 任务启动，可以在这里显示一个对话框，这里简单处理
			if (CSStaticData.DEBUG)
				Log.e(Tag, "[onPreExecute]---------->異步加載圖片開始");
		}

		@Override
		protected void onProgressUpdate(Integer... values)
		{
			// 更新进度

		}

	}

	private void initData()
	{

		// 初始化菜单颜色
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
				{ R.drawable.gallery_more_move, R.string.gallery_menu_item_move },
				{ R.drawable.gallery_more_set_as_favorite,
						R.string.gallery_menu_item_setfavorite },
				{ R.drawable.gallery_more_remove_favorite,
						R.string.gallery_menu_item_removefavorite },
				{ R.drawable.gallery_slideshow,
						R.string.gallery_menu_item_slideshow },
				{ R.drawable.gallery_more_file_info,
						R.string.gallery_menu_item_fileinfo },
				{ R.drawable.gallery_remove_red_eye,
						R.string.gallery_remove_red_eye },
				{ 0, R.string.gallery_menu_item_2Dto3D } };
		mGalleryMoveToMenuResId = new int[][]
		{
		{ 0, R.string.gallery_menu_item_internal },
		{ 0, R.string.gallery_menu_item_external }, };

		mDisableList = new ArrayList<Integer>();
		mSubMenuStateMap = new HashMap<Integer, Integer>();
		mSubMenuStateMap.put(SUBMENU_MOVETO_INTERNAL, -1);
		mContext = WiImageViewerActivity.this;
		mMenuLayout = (RelativeLayout) findViewById(R.id.moreMenuLayout);
		mSubMenuLayout = (RelativeLayout) findViewById(R.id.subMenuLayout);

		mMenuListView = (ListView) findViewById(R.id.moreListView);
		mSubMenuListView = (ListView) findViewById(R.id.subListView);
		mSubListTitle = (TextView) findViewById(R.id.subListTitle);

		mMenuListAdapter = new ViewerListAdapter(mContext, mMoreMenuResId);
		mSubListAdapter = new ViewerListAdapter(mContext, null);
		mMenuListAdapter
				.setMenuType(ViewerListAdapter.MENU_TYPE_MAIN_MENU_IMAGEVIEWER);
		mSubListAdapter
				.setMenuType(ViewerListAdapter.MENU_TYPE_SUB_MENU_IMAGEVIEWER);
		mSubListAdapter.setMap(mSubMenuStateMap);

		mMenuListView.setAdapter(mMenuListAdapter);
		mMenuLayout.setVisibility(View.INVISIBLE);// 调用此方法让主菜单显示或隐藏
		mSubMenuLayout.setVisibility(View.INVISIBLE);// 调用此方法让子菜单显示或隐藏

	}

	public void setScreenDimension(boolean is3D)
	{
		m_imageview.ChangeMode(is3D);
		String[] cmdTurnOn3D =
		{ // 开启屏幕3D命名
		"/system/bin/sh", "-c",
				"echo 1 > /sys/devices/platform/mipi_masterimage.513/enable_3d_barrier" };

		String[] cmdTurnOff3D =
		{ // 关闭屏幕3D命令
		"/system/bin/sh", "-c",
				"echo 0 > /sys/devices/platform/mipi_masterimage.513/enable_3d_barrier" };

		// CSStaticData.g_is_3D_mode = is3D;
		try
		{
			if (is3D)
			{
				if (CSStaticData.DEBUG)
				{
					Log.w(Tag, "[setScreenDimension]开启屏幕3D显示模式");
				}
				Runtime.getRuntime().exec(cmdTurnOn3D);
			} else
			{
				if (CSStaticData.DEBUG)
				{
					Log.w(Tag, "[setScreenDimension]关闭屏幕3D显示模式");
				}
				Runtime.getRuntime().exec(cmdTurnOff3D);
			}
		} catch (IOException exp)
		{
			if (CSStaticData.DEBUG)
			{
				Log.w(Tag, "[setScreenDimension]屏幕3D显示模式切换：失败，命令行输入流异常");
			}
		} catch (SecurityException exp)
		{
			if (CSStaticData.DEBUG)
			{
				Log.w(Tag, "[setScreenDimension]屏幕3D显示模式切换：失败，系统安全保护阻止本操作");
			}
		} catch (Exception exp)
		{
			if (CSStaticData.DEBUG)
			{
				Log.w(Tag, "[setScreenDimension]屏幕3D显示模式切换：失败，未知错误");
			}
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
				// mSubMenuLayout.setVisibility(View.INVISIBLE);//调用此方法让子菜单显示或隐藏

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
					mSubMenuLayout.setVisibility(View.INVISIBLE);// 调用此方法让子菜单显示或隐藏
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
					m_MoreButton
							.setBackgroundResource(R.drawable.gallery_more_btn_selector);
					break;
				case 2:
					if (mSubMenuLayout.getVisibility() == View.VISIBLE
							&& mSubListTitle.getText() == getResources()
									.getText(R.string.gallery_menu_item_move))
					{
						mSubMenuLayout.setVisibility(View.INVISIBLE);
						((TextView) arg1.findViewById(R.id.videoPlayerMenuText))
								.setTextColor(COLORSTATELIST_WHITE);
						return;
					}

					mSubListAdapter.setItemStartId(SUBMENU_MOVETO_INTERNAL);
					mSubListAdapter.setResId(mGalleryMoveToMenuResId);
					mSubListTitle.setText(R.string.gallery_menu_item_move);
					mSubMenuLayout.setVisibility(View.VISIBLE);// 调用此方法让子菜单显示或隐藏
					break;
				case 3:
					mMenuLayout.setVisibility(View.INVISIBLE);
					m_MoreButton
							.setBackgroundResource(R.drawable.gallery_more_btn_selector);
					// mDisableList.add(new Integer(4));
					// mDisableList.remove(new Integer(3));
					setFavoriteInfo(m_bitmappicker.getCurrentFileName(), true);
					String temp = m_bitmappicker.getCurFileName();
					msgBox_disappearautoBox.updateMessage(temp
							+ " has been set as favorite .");
					msgBox_disappearautoBox.show();
					msgBox_disappearautoBox.hideDelay(2000);
					hideButtonsWithDelay();
					break;
				case 4:
					mMenuLayout.setVisibility(View.INVISIBLE);
					m_MoreButton
							.setBackgroundResource(R.drawable.gallery_more_btn_selector);
					// mDisableList.add(new Integer(3));
					// mDisableList.remove(new Integer(4));
					setFavoriteInfo(m_bitmappicker.getCurrentFileName(), false);
					msgBox_disappearautoBox
							.updateMessage("Favorite tag has been removed.");
					msgBox_disappearautoBox.show();
					msgBox_disappearautoBox.hideDelay(2000);
					hideButtonsWithDelay();
					break;
				case 5:
					Tplay(false);
					hideButtonsWithDelay();
					mMenuLayout.setVisibility(View.INVISIBLE);
					m_MoreButton
							.setBackgroundResource(R.drawable.gallery_more_btn_selector);
					break;
				case 6:
					Tsetting();
					mMenuLayout.setVisibility(View.INVISIBLE);
					m_MoreButton
							.setBackgroundResource(R.drawable.gallery_more_btn_selector);
					break;
				case 7:
					// 去红眼功能
					if (FileTypeHelper.isImageFile(m_bitmappicker
							.getCurrentFileName()))
					{
						Intent intent = new Intent(
								WiImageViewerActivity.this,
								com.wistron.WiEditor.WiImageEditorActivity.class);
						intent.putExtra("filePath",
								m_bitmappicker.getCurrentFileName());
						startActivityForResult(intent,
								REQUESTCODE_WIIMAGEEDITOR);
					}
					mMenuLayout.setVisibility(View.INVISIBLE);
					m_MoreButton
							.setBackgroundResource(R.drawable.gallery_more_btn_selector);
					hideButtonsWithDelay();
					break;
				case 8:
					mMenuLayout.setVisibility(View.INVISIBLE);
					m_MoreButton
							.setBackgroundResource(R.drawable.gallery_more_btn_selector);
					hideButtonsWithDelay();
				}
				if (arg2 != 0)
				{
					if (mShareMenu != null && mShareMenu.isShown())
					{
						mShareMenu.hideAndRemove();
					//	m_popupLayout.removeView(mShareMenu);
					}
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
				mSubListPosition = arg2;
				switch (arg1.getId())
				{
				// group菜单的id，每个菜单内部，选项的id目前设置为每项递增1
				case SUBMENU_MOVETO_INTERNAL:
					// mSubMenuStateMap.put(SUBMENU_MOVETO_INTERNAL, arg2);
					moveFuction(m_bitmappicker.getCurrentFileName(),
							m_bitmappicker.getCurFileName(), true);
					break;
				case SUBMENU_MOVETO_INTERNAL + 1:
					// mSubMenuStateMap.put(SUBMENU_MOVETO_INTERNAL, arg2);
					moveFuction(m_bitmappicker.getCurrentFileName(),
							m_bitmappicker.getCurFileName(), false);
					break;
				}
				mSubMenuLayout.setVisibility(View.INVISIBLE);
				mMenuLayout.setVisibility(View.INVISIBLE);
				m_MoreButton
						.setBackgroundResource(R.drawable.gallery_more_btn_selector);
			}
		});
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

	public void moveFuction(final String filepath, final String filename,
			final boolean isToInner)
	{
		if (filepath != null)
		{
			mMsgBox = new MsgBox(mContext);
			mMsgBox_type = 3;
			String temp = null;
			if (isToInner)
			{
				temp = "Do you want to move " + filename
						+ " to interal storage ?";
			} else
			{
				temp = "Do you want to move " + filename
						+ " to exteral storage ?";
			}
			mMsgBox.setMessage(temp);
			mMsgBox.setBackgound(R.drawable.main_menu_window_portrait);
			mMsgBox.setPositiveButton(
					getResources().getText(R.string.gallery_delete_confirm),
					new OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							// TODO Auto-generated method stub
							setControlEnable();
							mMsgBox.hide();
							m_popupLayout.removeView(mMsgBox);
							mMsgBox = new MsgBox(mContext);
							mMsgBox_type=4;
							mMsgBox.setMessage("Moving……Please wait for a while");
							mMsgBox.setBackgound(R.drawable.main_menu_window_portrait);
							mMsgBox.addToLayout(m_popupLayout);
							mMsgBox.show();
							
							List<String> tempList = new ArrayList<String>();
							tempList.add(filepath);
							AsyncFileProvider asyncer = null;
							if (WiGalleryOpenGLRenderer.mAsyncFileProvider == null)
							{
								asyncer = new AsyncFileProvider(mContext);
							} else
							{
								asyncer = WiGalleryOpenGLRenderer.mAsyncFileProvider;
							}
							if (isToInner)
							{
								asyncer.moveTo(tempList,
										CSStaticData.TMP_INT_DIR, false, WiImageViewerActivityID);
							} else
							{
								asyncer.moveTo(tempList,
										CSStaticData.TMP_EXT_DIR, false, WiImageViewerActivityID);
							}
							m_move_istoinner = isToInner;
							m_move_filename = filename;
							//hideButtonsWithDelay();
						}
					});
			mMsgBox.setNegativeButton(
					getResources().getText(R.string.gallery_delete_cancel),
					new OnClickListener()
					{

						@Override
						public void onClick(View v)
						{
							// TODO Auto-generated method stub
							mMsgBox.hide();
							m_popupLayout.removeView(mMsgBox);
							setControlEnable();
							if (m_isSolidPlay)
							{
								m_isSolidPlay = false;
								solidShow();
							}
							hideButtonsWithDelay();
						}
					});
			mMsgBox.addToLayout(m_popupLayout);
			mMsgBox.show();

		}
	}

	public void setFavoriteInfo(String fileString, boolean bool)
	{
		// 设置喜好数据库
		if (fileString != null)
		{
			{//LiuWei
//				FavoriteDBAdapter dbAdapter = new FavoriteDBAdapter(
//						WiImageViewerActivity.this);
//				List<String> selectedList = new ArrayList<String>();
//				selectedList.add(fileString);
//				if (bool)
//				{
//					dbAdapter.setAsFavorite(selectedList);
//				} else
//				{
//					dbAdapter.removeFromFavorite(selectedList);
//				}
//				dbAdapter.dispose();
//				dbAdapter = null;
			}
			{//Cocoonshu
				AsyncFileProvider asyncFileProvider = null;
				if(WiGalleryOpenGLRenderer.mAsyncFileProvider == null){
					asyncFileProvider = new AsyncFileProvider(WiImageViewerActivity.this);
				}else{
					asyncFileProvider = WiGalleryOpenGLRenderer.mAsyncFileProvider;
				}
				List<String> selectedList = new ArrayList<String>();
				selectedList.add(fileString);
				if(bool){
					asyncFileProvider.setFavoriteToDB(selectedList, WiImageViewerActivityID);
				}else{
					asyncFileProvider.removeFavoriteFromDB(selectedList, WiImageViewerActivityID);	
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
		boolean isVideo = false;
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
			isVideo = FileTypeHelper.isVideoFile(filepath);
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
		mDisableList.clear();
		if (isFavorite)
		{
			mDisableList.add(new Integer(3));
		} else
		{
			mDisableList.add(new Integer(4));
		}
		if (isVideo)
		{
			mDisableList.add(new Integer(7));
			mDisableList.add(new Integer(8));
		}
	}

	private void callPanoramaViewer(String path)
	{
		Intent intent = new Intent();

		intent.setClass(WiImageViewerActivity.this,
				com.wistron.WiViewer.Panorama360Activity.class);
		intent.putExtra("cmd", "pano");
		intent.putExtra("filePath", path);
		startActivity(intent);
	}

	private boolean isPanoramaImage(String path)
	{
		if (path == null)
			return false;
		Options opts = new Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, opts);
		int width = opts.outWidth;
		int height = opts.outHeight;
		boolean isJps = path.toLowerCase().endsWith(".jps");
		if (isJps)
		{
			width = width / 2;
		}
		if (height != 0 && (width / height) >= 3 && width >= 1500)
		{
			return true;
		} else
		{
			return false;
		}
	}

	/**
	 * 设置sd卡插拔广播
	 */
	public class SDCardBroadcastReceiver extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context arg0, Intent arg1)
		{
			System.out.println("SDCardBroadcastReceiver 被注册");
			// TODO Auto-generated method stub
			String action = arg1.getAction();
			// 当SD卡插入时
			if (Intent.ACTION_MEDIA_MOUNTED.equals(action)
					|| Intent.ACTION_MEDIA_SCANNER_STARTED.equals(action)
					|| Intent.ACTION_MEDIA_SCANNER_FINISHED.equals(action))
			{
				m_SDismounted = false;
				setControlEnable();
				// 当SD卡拔出时
			} else if (Intent.ACTION_MEDIA_REMOVED.equals(action)
					|| Intent.ACTION_MEDIA_UNMOUNTED.equals(action)
					|| Intent.ACTION_MEDIA_BAD_REMOVAL.equals(action))
			{
				m_SDismounted = true;
				stopsolidplay();
				// ******设置按钮的状态，是否可用

				// m_csbtn_play.setEnable(false);
				// m_csbtn_set.setEnable(false);
				// m_csbtn_share.setEnable(false);
				// m_csbtn_delete.setEnable(false);
			}
		}

	}

	/**
	 * 恢复保存状态
	 */
	private void initStatusData()
	{
		ImageViewerStateInfo stateInfo = (ImageViewerStateInfo) getLastNonConfigurationInstance();
		if (stateInfo != null)
		{
			m_bitmappicker = new MediaFilePicker(stateInfo.getmFileList(),
					stateInfo.getmCurrentFileIndex());
			m_imageview.setMediaPicker(m_bitmappicker);
			if (stateInfo.ismIsSlideShow())
			{
				m_init_playmode = 1;
			} else
			{
				m_init_playmode = 0;
			}
			int boxtype = stateInfo.isDeleteBoxShow();
			if (boxtype != 0)
			{
				if (boxtype == 1)
				{
					Tdeletefile();
				} else if (boxtype == 2)
				{
					// 提示删光了
					showInfoWhenDeleteAll();
				} else if (boxtype == 3)
				{
					// 移动
					boolean isinner = false;
					int a = mSubMenuStateMap.get(SUBMENU_MOVETO_INTERNAL);
					if (a == 1)
					{
						isinner = true;
					}
					moveFuction(m_bitmappicker.getCurrentFileName(),
							m_bitmappicker.getCurFileName(), isinner);
				}
				else if(boxtype==4)
				{
					mMsgBox = new MsgBox(mContext);
					mMsgBox_type=4;
					mMsgBox.setMessage("Moving……Please wait for a while");
					mMsgBox.setBackgound(R.drawable.main_menu_window_portrait);
					mMsgBox.addToLayout(m_popupLayout);
					mMsgBox.show();
				}

			}
			if (stateInfo.isFileInfoBoxShow())
				Tsetting();

		}

	}

	/**
	 * 保存状态
	 */
	@Override
	public Object onRetainNonConfigurationInstance()
	{
		Log.w(Tag, "[onRetainNonConfigurationInstance]");
		int boxtype = 0;
		boolean isInfo = false;
		if (mMsgBox != null && mMsgBox.isShown())
		{
			boxtype = mMsgBox_type;
		}
		if (mMsgBox_info != null && mMsgBox_info.isShown())
		{
			isInfo = true;
		}
		ImageViewerStateInfo stateInfo = new ImageViewerStateInfo(
				m_bitmappicker.getCurrentFileIndex(), m_isSolidPlay,
				m_imageview.is_3D, m_bitmappicker.getfilepathList(), boxtype,
				isInfo);
		return stateInfo;
	}
}

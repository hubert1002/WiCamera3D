package com.wistron.WiGallery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.wistron.StereoUI.SlideButton;
import com.wistron.StereoUI.SlideButton.OnCheckedChangedListener;
import Utilities.*;
import Utilities.CSStaticData.MEDIA_META_TYPE;
import Utilities.SystemDebug.OnSystemDebugTrigger;

import com.wistron.swpc.wicamera3dii.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * Copyright (c) 2012 Wistron SWPC
 * All rights reserved.
 * @author 
 * @date 2012-02-20 12:00:00
 * @comment 
 * @purpose Scan Multi-media Files Activity
 * @detail 
 */
public class WiGalleryActivity extends Activity implements WiGalleryInterface.onGLItemListener, 
														   WiGalleryInterface.onGLScenceListener,
														   WiGalleryInterface.onGLMoveListener{

	private  static final String     TAG                             = "WiGalleryActivity";
	public   static final int        WiGalleryActivityID             = 0x76112; //WiGalleryActivityUID
	/**
	 * false 加载普通控件; true 加载CSView
	 */
	private  static final boolean    UI_MODE                         = false;
	           
	private  static final int        MENU_SETTING_ID                 = 0x11110;
	private  static final int        MENU_ITEM_BYDATE_ID             = 0x21110;
	private  static final int        MENU_ITEM_BYLOCATION_ID         = 0x21111;
	private  static final int        MENU_ITEM_NONE_ID               = 0x21112;
	public   static final int        MULTI_NONE_ID                   = 0x00000; //没有触发多选
	public   static final int        MULTI_USER_ID                   = 0x00200; //由长按触发的多选
	public   static final int        MULTI_SHARE_ID                  = 0x00201; //由设置分享触发的多选
	public   static final int        MULTI_DELETE_ID                 = 0x00202; //由设置删除触发的多选
	public   static final int        MULTI_MOVETO_INTERNAL_ID        = 0x00203; //由设置移动触发的多选
	public   static final int        MULTI_MOVETO_EXTERNAL_ID        = 0x00204; //由设置移动触发的多选
	public   static final int        MULTI_FAVORITE_ID               = 0x00205; //由设置喜好触发的多选
	public   static final int        MULTI_REMOVE_FAVORITE_ID        = 0x00206; //由移出喜好触发的多选
	public   static final int        MULTI_FILEINFO_ID               = 0x00207; //由设置信息触发的多选
	private  static final int        SORT_ORDER_ASC                  = 0x00000; //按日期升序排序
	private  static final int        SORT_ORDER_DESC                 = 0x00001; //按日期降序排序
	private  static final int        VIEW_FILTER_ALL                 = 0x00010; //显示所有文件
	private  static final int        VIEW_FILTER_2D                  = 0x00011; //显示2D文件
	private  static final int        VIEW_FILTER_3D                  = 0x00012; //显示3D文件
	private  static final int        MOVE_TO_INTERNAL_DIR            = 0x00100; //移动至内部目录
	private  static final int        MOVE_TO_EXTERNAL_DIR            = 0x00101; //移动至外部目录
	private  static final int        SUBMENU_GROUP_NONE              = 0x00110; //分组子菜单ID，起始偏移ID
	private  static final int        SUBMENU_GROUP_LOCATION          = 0x00111; //分组子菜单ID
	private  static final int        SUBMENU_GROUP_DATE              = 0x00112; //分组子菜单ID
	private  static final int        SUBMENU_SORTORDER_DESC          = 0x00120; //排序子菜单ID，起始偏移ID
	private  static final int        SUBMENU_SORTORDER_ASC           = 0x00121; //排序子菜单ID
	private  static final int        SUBMENU_CONTENTSWITCH_ALLFILES  = 0x00130; //文件过滤子菜单ID，起始偏移ID
	private  static final int        SUBMENU_CONTENTSWITCH_2D_ONLY   = 0x00131; //文件过滤子菜单ID
	private  static final int        SUBMENU_CONTENTSWITCH_3D_ONLY   = 0x00132; //文件过滤子菜单ID
	private  static final int        SUBMENU_MOVETO_INTERNAL         = 0x00140; //移动文件子菜单ID
	private  static final int        SUBMENU_MOVETO_EXTERNAL         = 0x00141; //移动文件子菜单ID
	public   static final int        HANDLE_APPLICATION_LAUNCHED     = 0x00200; //程序已经启动
	public   static final int        HANDLE_REFLASH_FAVORITE_COUNTER = 0x00300; //刷新喜好计数器
	public   static final int        HANDLE_REFLASH_UNKNOWN_COUNTER  = 0x00301; //刷新未知计数器
	public   static final int        HANDLE_REFLASH_PAGE_SEEKBAR     = 0x00302; //刷新Seekbar样式
	public   static final int        HANDLE_REFLASH_MORE_MENU_STYLE  = 0x00303; //刷新More菜单默认样式
	public   static final int        HANDLE_REFLASH_GALLERY_TITLE    = 0x00304; //刷新Title显示
	public   static final int        HANDLE_SWITCH_MORE_MENU_ITEMS   = 0x00305; //刷新More菜单选项
	public   static final int        HANDLE_MOVE_PROGRESS            = 0x00306; //文件移动的进度通知
	public   static final int        HANDLE_MOVE_COMPLETED           = 0x00307; //文件移动的完成通知
	public   static final int        HANDLE_DELETE_PROGRESS          = 0x00308; //文件删除的进度通知
	public   static final int        HANDLE_DELETE_COMPLETED         = 0x00309; //文件删除的完成通知
	public   static final int        HANDLE_SET_FAVORITE_TIP         = 0x00310; //设置喜好的提示通知
	public   static final int        HANDLE_REMOVE_FAVORITE_TIP      = 0x00311; //移除喜好的提示通知
	public   static final int        HANDLE_WIFI_CHECKED             = 0x00312; //WIFI检查的提示通知
	public   static final int        HANDLE_LOCK_SCREEN              = 0x00313; //锁定屏幕
	private  static final int        HANDLE_NONMULTI_SELECT          = 0x00314; //跳出多选模式
	private  static final int        HANDLE_MULTI_SELECT_NOT_ON_MENU = 0x00315; //长按跳入多选模式
	private  static final int        HANDLE_MULTI_SELECT_ON_MENU     = 0x00316; //菜单跳入多选模式
	private  static final int        HANDLE_SCROLL_PAGE_INTERFACE    = 0x00317; //设置Seekbar的队列接口
	private  static final int        HANDLE_JUMP_TO_LOCATION_MODE    = 0x00318; //跳转到Location模式
	
	
	  
	private FrameLayout              mMainLayout                     = null; //框架层
	private AbsoluteLayout           mCtrlLayout                     = null; //控件层
	private LinearLayout             mGlsurfaceLayout                = null; //内容层
	private RelativeLayout           mMenuLayout                     = null; //菜单层
    private RelativeLayout		     mSubMenuLayout                  = null; //子菜单层
    private RelativeLayout           mPopupLayout                    = null;
	private WiGalleryOpenGLView      mGlsurfaceView                  = null; //OpenGL
	 
	public  static Handler           mUIHandler                      = null;
	public  static Handler           mEmergencyHandler               = null;
	private SlideButton              mBtnDimension                   = null;
	private ToggleButton             mBtnMore                        = null;
	private Button                   mBtnCamera                      = null;
	private Button                   mBtnCancel                      = null;
	private Button                   mBtnExecute                     = null;
	private Button                   mDebugBtnIncrease               = null; //Debug模式按钮
	private Button                   mDebugBtnDecrease               = null; //Debug模式按钮
	private SeekBar                  mSbrScrollPage                  = null; 
	private TextView                 mTxvDate                        = null;
	private TextView                 mTxvTitle                       = null;
	private TextView                 mSubMenuTitle                   = null;
	private TextView                 mTxvFavoriteNumber              = null;
	private TextView                 mTxvUnknownNumber               = null;
	private RelativeLayout           mFavoriteLayout                 = null;
	private RelativeLayout           mUnknownLayout                  = null;
	private GalleryListViewAdapter   mGalleryListAdapter             = null;
	private GalleryListViewAdapter   mSubListAdapter                 = null;
	private ListView                 mGalleryMoreListView            = null;
	private ListView  	             mSubListView                    = null;
	private ShareToModule            mShareMenu                      = null;  //分享子菜单模块
	private MsgBox                   mMsgBoxDeleteConfirm            = null;  //确认对话框
	private MsgBox                   mMsgBoxMoveConfirm              = null;  //确认对话框
	private MsgBox                   mMsgBoxDeleteProgress           = null;  //进度对话框
	private MsgBox                   mMsgBoxMoveProgress             = null;  //进度对话框
	private MsgBox                   mMsgBoxSetFavoriteProgress      = null;  //进度对话框
	private MsgBox                   mMsgBoxRemoveFavoriteProgress   = null;  //进度对话框
	private MsgBox                   mMsgBoxSetFavoriteTipbox        = null;  //提示对话框
	private MsgBox                   mMsgBoxRemoveFavoriteTipbox     = null;  //提示对话框
	private MsgBox                   mMsgBoxMovedTipbox              = null;  //提示对话框
	private MsgBox                   mMsgBoxWIFITipbox               = null;  //提示对话框
	private MsgBox                   mMsgBoxWIFIDiscontectConfirm    = null;  //确认对话框 
	private MsgBox                   mMsgBoxError                    = null;  //错误对话框
	private boolean                  mDimension                      = false;
	private boolean                  mIsMoreMenuShow                 = false; //More菜单是否已显示
	private boolean                  mIsSubMoreMenuShow              = false; //More子菜单是否已显示
	private boolean                  mIsMoreMenuTouched              = false;
	private boolean                  mHasOperatedMenu                = false; //是否已经操作过菜单（与下面的标识对应使用）
	private boolean                  mHasMultiSelectedFiles          = false; //是否已经多选过文件（与上面的标识对应使用）
	private boolean                  mIsMulitSelectionMode           = false; //是否处于多选模式
	private boolean                  mIsCallingSystemSetting         = false; //是否已经呼叫了系统设置
	private boolean                  mNoNeedToUnlock                 = false; //无需解锁屏幕
	private float                    mTextSize                       = 22;
	private int                      mMsgboxAutoHideTimeout          = 2000;
	private int                      mCurMultiStatus                 =  0;    //记录当前的多选是由谁激发的,按了execute按钮后使用
	public  static int               mMoreMenuCurClickPosition       = -1;    //记录当前主菜单的选中项
	public  static int               mMoreMenuDisableClickPosition   = -1;    //记录当前主菜单的选中项不可用的选项
	public  static int               mGroupMenuCurClickPosition      =  0;    //记录当前分组菜单的已选项，用于设定为不可用
	public  static int               mOrderMenuCurClickPosition      =  0;    //记录当前排序菜单的已选项，用于设定为不可用
	public  static int               mFilterMenuCurClickPosition     =  0;    //记录当前过滤菜单的已选项，用于设定为不可用
	public  static MultiTempData     mMultiTempData                  = null;  //记录多选操作的临时数据
	public  static ColorStateList    COLORSTATELIST_WHITE            = null;
	public  static ColorStateList    COLORSTATELIST_GREEN            = null;
	public  static int               mCurrentScreenState             = ActivityInfo.SCREEN_ORIENTATION_USER;  //锁屏函数：当前屏幕状态
	public  static int               mCurrentOrenState               = -1;
	public  static int               mHistoryOrenState               = -1;
	public  static boolean           mHasLockedScreen                = false;
	private OrientationEventListener mOrenListener                   = null;  
	private XmlPullParser 		     mTextColorWhite                 = null;
	private XmlPullParser            mTextColorGreen                 = null;
	private List<String>             mSelectedFileList               = null;
	private int[][]                  mGalleryMoreMenuResId           = null;
	private int[][]                  mGalleryGroupMenuResId          = null;
	private int[][]                  mGallerySortOrderMenuResId      = null;
	private int[][]                  mGalleryContentSwitchMenuResId  = null;
	private int[][]                  mGalleryMoveToMenuResId         = null;
	
	@SuppressWarnings("deprecation") //for AbsoluteLayout
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.w(TAG, "[onCreate]");
		super.onCreate(savedInstanceState);
		//设置全屏运行
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //设置屏幕常亮
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); 
        //除去程序标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		CSStaticData.LOAD_STARTED = false;
		CSStaticData.LOAD_COMPLETED = false;
		
		if(UI_MODE){
			
		}else{
			//显示布局
			setContentView(R.layout.wigalleryactivity_main_ax);

			//获取控件
			mMainLayout       = (FrameLayout) findViewById(R.id.frameLayoutBase);
			mGlsurfaceView    = new WiGalleryOpenGLView(WiGalleryActivity.this);
			mGlsurfaceLayout  = (LinearLayout) findViewById(R.id.linearLayoutGLContent);
			mPopupLayout      = (RelativeLayout) findViewById(R.id.absoluteLayoutControls);
			
			//初始化参数
			initControls();
			initListeners();
			initBoastcasts();
			initHandles();
			initStatusData();
			
			//初始化WiGallery接口
			WiGalleryInterface.m_onGlItemListener = this;
			WiGalleryInterface.m_onGLScenceListener = this;
			WiGalleryInterface.m_onGLMoveListener = this;

			//添加OpenGLES
			mGlsurfaceLayout.addView(mGlsurfaceView);
		}
		
		//拾取屏幕信息
		{
			CSStaticData.g_screen_width  = getWindowManager().getDefaultDisplay().getWidth();
			CSStaticData.g_screen_height = getWindowManager().getDefaultDisplay().getHeight();
		}
		
		//启动屏幕Debug方法
		{
			final TextView debugBox    = new TextView(WiGalleryActivity.this);
			AbsoluteLayout debugLayout = new AbsoluteLayout(WiGalleryActivity.this);
			
			debugLayout.setLayoutParams(new AbsoluteLayout.LayoutParams(
					AbsoluteLayout.LayoutParams.MATCH_PARENT, 
					AbsoluteLayout.LayoutParams.MATCH_PARENT, 
					0, 0));
			debugBox.setLayoutParams(new AbsoluteLayout.LayoutParams(
					0, 0, 
					0, 0));
			debugBox.setBackgroundResource(R.drawable.camera_focus);
			debugBox.setVisibility(View.GONE);
			
			mMainLayout.addView(debugLayout);
			debugLayout.addView(debugBox);
			
			SystemDebug.setOnSystemDebugTrigger(new OnSystemDebugTrigger() {
				
				@Override
				public void drawRect(Rect rect) {
					debugBox.layout(rect.left, rect.top, rect.right, rect.bottom);
					debugBox.setVisibility(View.VISIBLE);
				}
				
				@Override
				public void clear() {
					debugBox.setVisibility(View.GONE);
				}
			});
		}
	}

	/****************************************************************/
	/*                           生命周期                                                                         */
	/****************************************************************/

	@Override
	protected void onStart() {
		Log.w(TAG, "[onStart]");
		// TODO Auto-generated method stub
		WiGalleryOpenGLRenderer.mAsyncFileProvider.launchImageLoadService();
		WiGalleryOpenGLRenderer.mAsyncFileProvider.launchVideoLoadService();
		super.onStart();
	}
	
	@Override
	protected void onResume() {
		Log.w(TAG, "[onResume]");
		// TODO Auto-generated method stub
		callCloseMenu();
		super.onResume();
		
		//启动屏幕3D显示
		if(CSStaticData.gStatusInfo != null){
			CSStaticData.gStatusInfo.restoreActivity();
//			mCurMultiStatus = CSStaticData.gStatusInfo.restoreCurMultiStatus();
//			mHasOperatedMenu = CSStaticData.gStatusInfo.restoreHasOperatedMenu();
		}
		
		//如果配置中为3D，则开启
		mBtnDimension.setCheckedWithCallback(CSStaticData.g_is_3D_mode);
		
//		WiGalleryOpenGLRenderer.mAsyncFileProvider.scanGEOLib();
		if(CSStaticData.gStatusInfo != null){
			/* Beta版过后再改，现在存在问题，有可能转屏后，恢复的Messagebox不再消失了
			if(mMsgBoxDeleteProgress != null && CSStaticData.gStatusInfo.getMsgboxDeleteProgressStatus()){
				mMsgBoxDeleteProgress.setProgress(CSStaticData.gStatusInfo.getMsgboxDeleteProgress());
				mMsgBoxDeleteProgress.show();
			}
			if(mMsgBoxMoveProgress != null && CSStaticData.gStatusInfo.getMsgBoxMoveProgressStatus()){
				mMsgBoxMoveProgress.setProgress(CSStaticData.gStatusInfo.getMsgBoxMoveProgress());
				mMsgBoxMoveProgress.show();
			}
			if(mMsgBoxRemoveFavoriteProgress != null && CSStaticData.gStatusInfo.getMsgBoxRemoveFavoriteProgressStatus()){
				mMsgBoxRemoveFavoriteProgress.setProgress(CSStaticData.gStatusInfo.getMsgBoxRemoveFavoriteProgress());
				mMsgBoxRemoveFavoriteProgress.show();
			}
			if(mMsgBoxSetFavoriteProgress != null && CSStaticData.gStatusInfo.getMsgBoxSetFavoriteProgressStatus()){
				mMsgBoxSetFavoriteProgress.setProgress(CSStaticData.gStatusInfo.getMsgBoxSetFavoriteProgress());
				mMsgBoxSetFavoriteProgress.show();
			}
			*/
				
			if(mSbrScrollPage != null){
				mSbrScrollPage.setMax(CSStaticData.gStatusInfo.restoreActivitySeekbarMax());
				mSbrScrollPage.setProgress(CSStaticData.gStatusInfo.restoreActivitySeekbarProgress());
			}
		}
		
		
		if(CSStaticData.DEBUG){
			FavoriteDBAdapter dbAdapter = new FavoriteDBAdapter(WiGalleryActivity.this);
			List<String> favorFile = dbAdapter.getFavoriteFiles();
			if(favorFile != null){
				Log.w(TAG, "[onResume]喜好文件共 " + favorFile.size() + " 个");
				for(int i = 0; i < favorFile.size(); i++){
					Log.w(TAG, "[onResume]  --- " + i + ". " + favorFile.get(i));
				}
			}
		}
		
		if(mIsCallingSystemSetting){
			//可能有问题  <============================================================================================
			if(CSStaticData.DEBUG){
				Log.w(TAG, "[onResume]已经调用过系统设置了，直接进入Location分组模式");
			}
			callGroupingSet(CSStaticData.LIST_TYPE.LIST_LOCATION_1);
		}
		
	}

	@Override
	protected void onPause() {
		Log.w(TAG, "[onPause]");
		super.onPause();
		//关闭屏幕3D显示
		if(CSStaticData.g_is_3D_mode){ //如果配置中为3D，则关闭
			if(CSStaticData.DEBUG){
				Log.w(TAG, "[onResume]关闭屏幕3D显示模式：失败，命令行输入流异常");
			}
			setScreenDimension(false);
		}
	}
	
	@Override
	protected void onRestart() {
		Log.w(TAG, "[onRestart]");
		// TODO Auto-generated method stub
		super.onRestart();
		if(!CSStaticData.LOAD_COMPLETED && !CSStaticData.LOAD_STARTED){
			if(WiGalleryOpenGLRenderer.mAsyncFileProvider != null){
				WiGalleryOpenGLRenderer.mAsyncFileProvider.scanMediaLib();
			}
		}
	}
	
	@Override
	protected void onStop() {
		Log.w(TAG, "[onStop]");
		// TODO Auto-generated method stub
		super.onStop();
		if(CSStaticData.gStatusInfo == null){
			CSStaticData.gStatusInfo = new StatusInfo();
		}
//		CSStaticData.gStatusInfo.saveGL();
		CSStaticData.gStatusInfo.saveActivity();
		CSStaticData.gStatusInfo.saveSortOrderMode();
		CSStaticData.gStatusInfo.saveActivitySeekbar(mSbrScrollPage.getProgress(), mSbrScrollPage.getMax());
		if(WiGalleryOpenGLRenderer.mAsyncFileProvider != null){
			WiGalleryOpenGLRenderer.mAsyncFileProvider.stopImageLoadService();
			WiGalleryOpenGLRenderer.mAsyncFileProvider.stopVideoLoadService();
		}
		
//		if(WiGalleryOpenGLRenderer.m_element_group != null){
//			WiGalleryOpenGLRenderer.m_element_group.clear();
//		}
//		
//		if(WiGalleryOpenGLRenderer.m_data_manager != null){
//			WiGalleryOpenGLRenderer.m_data_manager.ReleaseAllTexture();
//			WiGalleryOpenGLRenderer.m_data_manager.DelAllBitmap();
//			WiGalleryOpenGLRenderer.m_data_manager.destoryDataManager();
//		}
		
//		disposeActivityData();
		
		CSStaticData.LOAD_STARTED = false;
		CSStaticData.LOAD_COMPLETED = false;
	}
	
	@Override
	protected void onDestroy() {
		Log.w(TAG, "[onDestroy]");
		// TODO Auto-generated method stub
		super.onDestroy();
		
		{//回收线程
			//关闭自己的线程
			if(WiGalleryOpenGLRenderer.mAsyncFileProvider != null){
				WiGalleryOpenGLRenderer.mAsyncFileProvider.stopImageLoadService();
				WiGalleryOpenGLRenderer.mAsyncFileProvider.stopVideoLoadService();
				WiGalleryOpenGLRenderer.mAsyncFileProvider.stopParserGeoService();
			}
			
			//关闭系统绑定线程
			if(Thread.activeCount() > 0){
				if(CSStaticData.DEBUG){
					Log.w(TAG, "[onDestroy]还有 " + Thread.activeCount() + " 条线程没有关闭");
				}
				//FIMME
				String threadName = null;
				Thread[] unclosedThreads = {};
				Thread.enumerate(unclosedThreads);
				if(unclosedThreads != null){
					for(int i = 0; i < unclosedThreads.length; i++){
						if(unclosedThreads[i] != null){
							threadName = unclosedThreads[i].getName();
							if(threadName != null && threadName.indexOf("Binder Thread") >= 0){
								try{
									unclosedThreads[i].stop();
								}catch (UnsupportedOperationException exp) {
									// TODO: handle exception
									// stay null
								}
							}
							if(CSStaticData.DEBUG){
								Log.w(TAG, "[onDestroy]---" + threadName);
							}
						}
					}
				}
				
				{//清空MainLooper
					/* 主线程无法自杀，无效代码
					if(getMainLooper().getThread() != null){
						try{
							getMainLooper().getThread().stop();
						}catch (UnsupportedOperationException exp) {
							// TODO: handle exception
						}
					}
					*/
				}
			}
		}
		
		{//强行终止程序
			/* 不到迫不得已的时候，不用使用这个代码块！！！它会强行终于整个线程树
			System.runFinalizersOnExit(true);
			System.exit(0);
			*/
		}
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		
//		if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE){
//			SystemDebug.drawRect(new Rect((int)event.getX(), (int)event.getY(), (int)event.getX()+50, (int)event.getY()+50));
//		}
//		if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL){
//			SystemDebug.clear();
//		}
		
		if(mIsMoreMenuShow && mMenuLayout != null){
			if(!((event.getX() >= mMenuLayout.getLeft() && event.getX() <= mMenuLayout.getRight())
					&&
			    (event.getY() >= mMenuLayout.getTop() && event.getY() <= mMenuLayout.getBottom()))
			    	&&
			    event.getAction() == MotionEvent.ACTION_DOWN
			   ){
				
				if(mShareMenu != null && mShareMenu.getVisibility() == View.VISIBLE){
					mShareMenu.setVisibility(View.INVISIBLE);
					mIsSubMoreMenuShow = false;
					mMoreMenuCurClickPosition = -1;
					mGalleryListAdapter.notifyDataSetChanged();
				}
				if(mIsSubMoreMenuShow){
//					mSubMenuLayout.setVisibility(View.INVISIBLE);
//					mIsSubMoreMenuShow = false;
					mMoreMenuCurClickPosition = -1;
					mGalleryListAdapter.notifyDataSetChanged();
				}
				
				mGalleryMoreListView.setSelection(0);
				mSubMenuLayout.setVisibility(View.INVISIBLE);
				mMenuLayout.setVisibility(View.INVISIBLE);
				recoverMoreMenuUIStyle();
				mBtnMore.setChecked(false);
				mIsSubMoreMenuShow = false;
				mIsMoreMenuShow = false;
				mIsMoreMenuTouched = false;
				
				return true;
			}
		}
			
		return super.dispatchTouchEvent(event);
	}
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		
		callLowMemoryLawman();
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		if(mMenuLayout != null && mMenuLayout.getVisibility() == View.VISIBLE && !mIsMoreMenuTouched){
			if(!mIsMoreMenuTouched){
				mGalleryMoreListView.setSelection(0);
				mSubMenuLayout.setVisibility(View.INVISIBLE);
				mMenuLayout.setVisibility(View.INVISIBLE);
				recoverMoreMenuUIStyle();
				mBtnMore.setChecked(false);
				mIsSubMoreMenuShow = false;
				mIsMoreMenuShow = false;
				mIsMoreMenuTouched = false;
			}
			return true;
		}
		
		return super.onTouchEvent(event);
	}

	@Override
	public void onBackPressed() {
		if(CSStaticData.DEBUG){
			Log.e(TAG, "[onBackPressed]");
		}
		onBack();
	}

	@Override
	public boolean isChangingConfigurations() {
		callCloseMenu();
		return super.isChangingConfigurations();
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		Log.w(TAG, "[onRetainNonConfigurationInstance]");
		boolean isMsgBoxDeleteProgressShow           = false;  //进度对话框
		boolean isMsgBoxMoveProgressShow             = false;  //进度对话框
		boolean isMsgBoxSetFavoriteProgressShow      = false;  //进度对话框
		boolean isMsgBoxRemoveFavoriteProgressShow   = false;  //进度对话框 
		int     isMsgBoxDeleteProgress               = 0;
		int     isMsgBoxMoveProgress                 = 0;
		int     isMsgBoxSetFavoriteProgress          = 0;
		int     isMsgBoxRemoveFavoriteProgress       = 0;
		
		callCloseMenu();
		
		if(CSStaticData.gStatusInfo == null){
			CSStaticData.gStatusInfo = new StatusInfo();
		}
		CSStaticData.gStatusInfo.saveActivity();
		//CSStaticData.gStatusInfo.saveGL();
		
		//保存Messageboxes的状态
		if(mMsgBoxDeleteProgress != null){
			isMsgBoxDeleteProgressShow = mMsgBoxDeleteProgress.isShown();
			isMsgBoxDeleteProgress = mMsgBoxDeleteProgress.getProgress();
		}
		if(mMsgBoxMoveProgress != null){
			isMsgBoxMoveProgressShow = mMsgBoxMoveProgress.isShown();
			isMsgBoxMoveProgress = mMsgBoxMoveProgress.getProgress();
		}
		if(mMsgBoxRemoveFavoriteProgress != null){
			isMsgBoxRemoveFavoriteProgressShow = mMsgBoxRemoveFavoriteProgress.isShown();
			isMsgBoxSetFavoriteProgress = mMsgBoxRemoveFavoriteProgress.getProgress();
		}
		if(mMsgBoxSetFavoriteProgress != null){
			isMsgBoxSetFavoriteProgressShow = mMsgBoxSetFavoriteProgress.isShown();
			isMsgBoxRemoveFavoriteProgress = mMsgBoxSetFavoriteProgress.getProgress();
		}
		
		CSStaticData.gStatusInfo.saveGalleryMessageBoxes(isMsgBoxDeleteProgressShow,
														 isMsgBoxMoveProgressShow,
														 isMsgBoxRemoveFavoriteProgressShow,
														 isMsgBoxSetFavoriteProgressShow);
		CSStaticData.gStatusInfo.saveGalleryMessageBoxesProgress(isMsgBoxDeleteProgress,
																 isMsgBoxMoveProgress,
																 isMsgBoxSetFavoriteProgress,
																 isMsgBoxRemoveFavoriteProgress);
		
		CSStaticData.gStatusInfo.saveMulitOperation(mCurMultiStatus, mHasOperatedMenu);
		
		return CSStaticData.gStatusInfo;
	}
	
	@Override
	public void onFileClick(String fileItemName) {
		int     selectedItemsNum = 0;
		Message msg              = null;
		if(CSStaticData.DEBUG){
			Log.w(TAG, "[onFileClick]File: " + fileItemName + "被点击");
		}
		if(mIsMulitSelectionMode){
			selectedItemsNum = getSelectedItemsSize();
			msg = new Message();
			msg.what = HANDLE_REFLASH_GALLERY_TITLE;
			if(selectedItemsNum == 0 || selectedItemsNum == 1){
				msg.obj = "Selected " + selectedItemsNum + " item";
			}else{
				msg.obj = "Selected " + selectedItemsNum + " items";
			}
			mUIHandler.sendMessage(msg);
		}else{
			
			if(FileTypeHelper.isImageFile(fileItemName)){
				mMsgBoxError.setMessage(R.string.gallery_msgbox_imageviewr_not_found);
				try{
					callImageViewer(fileItemName, mGlsurfaceView.getRender().getCurrentDisplayedList());
				}catch (Error exp) {
					callImageViewer(fileItemName, mGlsurfaceView.getRender().getCurrentDisplayedList());
					mMsgBoxError.show();
				}catch(Exception exp){
					callImageViewer(fileItemName, mGlsurfaceView.getRender().getCurrentDisplayedList());
					mMsgBoxError.show();
				}
//				finally{
//					mMsgBoxError.setMessage(R.string.gallery_msgbox_imageviewr_not_found);
//					mMsgBoxError.show();
//				}
				return;
			}
			if(FileTypeHelper.isVideoFile(fileItemName)){
				mMsgBoxError.setMessage(R.string.gallery_msgbox_videoviewer_not_found);
				try{
					callVideoViewer(fileItemName, mGlsurfaceView.getRender().getCurrentDisplayedList());
				}catch (Error exp) {
					callVideoViewer(fileItemName, mGlsurfaceView.getRender().getCurrentDisplayedList());
					mMsgBoxError.show();
				}catch (Exception exp) {
					callVideoViewer(fileItemName, mGlsurfaceView.getRender().getCurrentDisplayedList());
					mMsgBoxError.show();
				}
//				finally{
//					mMsgBoxError.setMessage(R.string.gallery_msgbox_videoviewer_not_found);
//					mMsgBoxError.show();
//				}
				return;
			}
			
		}
	}

	@Override
	public void onGroupClick(String groupItemName) {
		int     selectedItemsNum = 0;
		Message msg              = null;
		if(CSStaticData.DEBUG){
			Log.w(TAG, "[onGroupClick]Group: " + groupItemName + "被点击");
		}
		if(mIsMulitSelectionMode){
			selectedItemsNum = getSelectedItemsSize();
			msg = new Message();
			msg.what = HANDLE_REFLASH_GALLERY_TITLE;
			if(selectedItemsNum == 0 || selectedItemsNum == 1){
				msg.obj = "Selected " + selectedItemsNum + " item";
			}else{
				msg.obj = "Selected " + selectedItemsNum + " items";
			}
			mUIHandler.sendMessage(msg);
		}
	}
	
	@Override
	public void onSortOrderChanged() {}

	@Override
	public void onViewModeChangded() {}

	@Override
	public void onGroupModeChanged() {}

	@Override
	public void onScenceChanged() {
		mUIHandler.sendEmptyMessage(HANDLE_REFLASH_FAVORITE_COUNTER);
		mUIHandler.sendEmptyMessage(HANDLE_REFLASH_UNKNOWN_COUNTER);
		mUIHandler.sendEmptyMessage(HANDLE_REFLASH_PAGE_SEEKBAR);
//		mUIHandler.sendEmptyMessage(HANDLE_REFLASH_GALLERY_TITLE);a
	}


	@Override
	public void onScenceCreated() {}

	@Override
	public void onScenceDestoryed() {}
	
	@Override
	public void onMoveStart(int currentRow, int rows) {}

	@Override
	public void onMoveing(int currentRow, int rows) {}

	@Override
	public void onMoveEnd(int currentRow, int rows) {}
	
	@Override
	public void onDelete(List<String> deleteFileList, boolean fromuesr) {}
	
	@Override
	public void onMultiSelectionModeChanged(boolean isMultiSelection) {
		//进入多选锁屏，退出多选解屏
		if(mNoNeedToUnlock){
			
		}else{
			lockScreen(isMultiSelection);
		}
		
		{//More Menu 行为
			Message msg = new Message();
			msg.what = HANDLE_SWITCH_MORE_MENU_ITEMS;
			msg.obj  = (Boolean)isMultiSelection;
			mUIHandler.sendMessage(msg);
		}
		
		mIsMulitSelectionMode = isMultiSelection;
		Message msg           = new Message();
		if(isMultiSelection){
			//Activity UI 行为
			int selectItems = getSelectedItemsSize();
			msg.what    = HANDLE_REFLASH_GALLERY_TITLE;
			msg.obj     = "Selected 0 item";
			if(selectItems > 0){
				if(selectItems >= 2){
					msg.obj = "Selected " + selectItems + " items";
				}else{
					msg.obj = "Selected " + selectItems + " item";
				}
			}
			mUIHandler.sendMessage(msg);
			if(mHasOperatedMenu){
				mUIHandler.sendEmptyMessage(HANDLE_MULTI_SELECT_ON_MENU);
			}else{
				mUIHandler.sendEmptyMessage(HANDLE_MULTI_SELECT_NOT_ON_MENU);
			}
		}else{
			//Activity UI 行为
			mUIHandler.sendEmptyMessage(HANDLE_NONMULTI_SELECT);
			mIsSubMoreMenuShow = false;
			mIsMoreMenuShow = false;
			msg.what    = HANDLE_REFLASH_GALLERY_TITLE;
			msg.obj     = getResources().getString(R.string.gallery_title);
			mUIHandler.sendMessage(msg);
			mUIHandler.sendEmptyMessage(HANDLE_REFLASH_MORE_MENU_STYLE);
		}
	}
	
	/****************************************************************/
	/*                           实用工具                                                                         */
	/****************************************************************/
	
	/**
	 * 初始化普通控件
	 */
	private void initControls() {
		
		mGalleryMoreMenuResId = CSStaticData.gGroupNoneResid;
		
		mGalleryGroupMenuResId = new int[][]{
				{0, R.string.gallery_menu_item_none},
				{0, R.string.gallery_menu_item_location},
				{0, R.string.gallery_menu_item_date}
		};
		
		mGallerySortOrderMenuResId = new int[][]{
				{0, R.string.gallery_menu_item_newestFist},
				{0, R.string.gallery_menu_item_newestLast},
		};
		
		mGalleryContentSwitchMenuResId = new int[][]{
				{0, R.string.gallery_menu_item_2Dand3D},
				{0, R.string.gallery_menu_item_2DOnly},
				{0, R.string.gallery_menu_item_3DOnly}
		};
		
		mGalleryMoveToMenuResId = new int[][]{
				{0, R.string.gallery_menu_item_internal},
				{0, R.string.gallery_menu_item_external},
		};
		
		mBtnDimension        = (SlideButton) findViewById(R.id.toggleButtonDimension);
		mBtnMore             = (ToggleButton) findViewById(R.id.buttonMore);
		mBtnCamera           = (Button) findViewById(R.id.buttonCamera);
		mBtnCancel           = (Button) findViewById(R.id.buttonCancel);
		mBtnExecute          = (Button) findViewById(R.id.buttonExecute);
		mSbrScrollPage       = (SeekBar) findViewById(R.id.seekBarScrollPage);
		mTxvTitle            = (TextView) findViewById(R.id.textViewGalleryTitle);
		mTxvDate             = (TextView) findViewById(R.id.textViewDate);
		mTxvFavoriteNumber   = (TextView) findViewById(R.id.textViewFavoriteNumber);
		mTxvUnknownNumber    = (TextView) findViewById(R.id.textViewUnknownNumber);
		
		mTxvTitle.setLongClickable(true);
		mBtnDimension.setEnabled(false);
		mBtnMore.setChecked(false);
		mBtnMore.setEnabled(false);
		
		if(CSStaticData.DEBUG){
			mDebugBtnIncrease    = new Button(WiGalleryActivity.this);
			mDebugBtnDecrease    = new Button(WiGalleryActivity.this);
			mDebugBtnIncrease.setLayoutParams(new FrameLayout.LayoutParams(100, 100, Gravity.LEFT|Gravity.CENTER_VERTICAL));
			mDebugBtnDecrease.setLayoutParams(new FrameLayout.LayoutParams(100, 100, Gravity.RIGHT|Gravity.CENTER_VERTICAL));
			mDebugBtnIncrease.setText(" + ");
			mDebugBtnDecrease.setText(" - ");
			mDebugBtnIncrease.setTextSize(28);
			mDebugBtnDecrease.setTextSize(28);
			mDebugBtnIncrease.setTextColor(Color.RED);
			mDebugBtnDecrease.setTextColor(Color.RED);
			mDebugBtnIncrease.setAlpha(0.5f);
			mDebugBtnDecrease.setAlpha(0.5f);
			mMainLayout.addView(mDebugBtnIncrease);
			mMainLayout.addView(mDebugBtnDecrease);
		}
		
		
		//初始化菜单颜色
		 mTextColorWhite = getResources().getXml(R.drawable.textcolor_selector_white);
		 mTextColorGreen = getResources().getXml(R.drawable.textcolor_selector_green);
	
		try {
			COLORSTATELIST_WHITE = ColorStateList.createFromXml(getResources(), mTextColorWhite);
			COLORSTATELIST_GREEN = ColorStateList.createFromXml(getResources(), mTextColorGreen);
		} catch (XmlPullParserException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		mFavoriteLayout      = (RelativeLayout) findViewById(R.id.favoriteLayout);
		mUnknownLayout       = (RelativeLayout) findViewById(R.id.unknownLayout);
		mMenuLayout          = (RelativeLayout) findViewById(R.id.moreMenuLayout);
		mSubMenuLayout       = (RelativeLayout) findViewById(R.id.subMenuLayout);
		mGalleryMoreListView = (ListView) findViewById(R.id.moreListView);
		mSubListView         = (ListView) findViewById(R.id.subListView);
		mSubMenuTitle        = (TextView) findViewById(R.id.subListTitle);
		
		mMsgBoxDeleteConfirm          = new MsgBox(WiGalleryActivity.this);
		mMsgBoxMoveConfirm            = new MsgBox(WiGalleryActivity.this);
		mMsgBoxDeleteProgress         = new MsgBox(WiGalleryActivity.this);
		mMsgBoxMoveProgress           = new MsgBox(WiGalleryActivity.this);
		mMsgBoxSetFavoriteTipbox      = new MsgBox(WiGalleryActivity.this);
		mMsgBoxRemoveFavoriteTipbox   = new MsgBox(WiGalleryActivity.this);
		mMsgBoxSetFavoriteProgress    = new MsgBox(WiGalleryActivity.this);
		mMsgBoxRemoveFavoriteProgress = new MsgBox(WiGalleryActivity.this);
		mMsgBoxMovedTipbox            = new MsgBox(WiGalleryActivity.this);
		mMsgBoxWIFITipbox             = new MsgBox(WiGalleryActivity.this);
		mMsgBoxWIFIDiscontectConfirm  = new MsgBox(WiGalleryActivity.this);
		mMsgBoxError                  = new MsgBox(WiGalleryActivity.this);
		
		//删除确认对话框
		mMsgBoxDeleteConfirm.setMessage(R.string.gallery_msgbox_delete_confirm);
		mMsgBoxDeleteConfirm.addToLayout(mMainLayout);
		
		//移动确认对话框
		mMsgBoxMoveConfirm.setMessage(String.format(getResources().getString(R.string.gallery_msgbox_move_to_confirm, 0, "", "internal")));
		mMsgBoxMoveConfirm.addToLayout(mMainLayout);
		
		//删除进度对话框
		mMsgBoxDeleteProgress.setMessage(R.string.gallery_msgbox_delete_progress);
		mMsgBoxDeleteProgress.setProgressBarShow(true);
		mMsgBoxDeleteProgress.addToLayout(mMainLayout);
		
		//移动进度对话框
		mMsgBoxMoveProgress.setMessage(R.string.gallery_msgbox_move_progress);
		mMsgBoxMoveProgress.setProgressBarShow(true);
		mMsgBoxMoveProgress.addToLayout(mMainLayout);
		
		//设置喜好进度框
		mMsgBoxSetFavoriteProgress.setMessage(R.string.gallery_msgbox_set_as_favorite_progress);
		mMsgBoxSetFavoriteProgress.addToLayout(mMainLayout);
		
		//移除喜好进度框
		mMsgBoxRemoveFavoriteProgress.setMessage(R.string.gallery_msgbox_remove_from_favorite_progress);
		mMsgBoxRemoveFavoriteProgress.addToLayout(mMainLayout);
		
		//设置喜好提示框
		mMsgBoxSetFavoriteTipbox.setMessage(String.format(getResources().getString(R.string.gallery_msgbox_set_as_favorite, 0, "", "has")));
		mMsgBoxSetFavoriteTipbox.setAutoHide(true, mMsgboxAutoHideTimeout);
		mMsgBoxSetFavoriteTipbox.addToLayout(mMainLayout);
		
		//移除喜好提示框
		mMsgBoxRemoveFavoriteTipbox.setMessage(R.string.gallery_msgbox_remove_from_favorite);
		mMsgBoxRemoveFavoriteTipbox.setAutoHide(true, mMsgboxAutoHideTimeout);
		mMsgBoxRemoveFavoriteTipbox.addToLayout(mMainLayout);
		
		//移动完成提示框
		mMsgBoxMovedTipbox.setMessage(String.format(getResources().getString(R.string.gallery_msgbox_moved_to_tip), 0, "", "has", "internal"));
		mMsgBoxMovedTipbox.setAutoHide(true, mMsgboxAutoHideTimeout);
		mMsgBoxMovedTipbox.addToLayout(mMainLayout);
		
		//WIFI检查提示框
		mMsgBoxWIFITipbox.setMessage(R.string.gallery_msgbox_wifi_tip);
		mMsgBoxWIFITipbox.setAutoHide(false, 0);
		mMsgBoxWIFITipbox.setModelStatus(true);
		mMsgBoxWIFITipbox.addToLayout(mMainLayout);
		
		//WIFI不连通确认框
		mMsgBoxWIFIDiscontectConfirm.setMessage(R.string.gallery_msgbox_wifi_discontect_confirm);
		mMsgBoxWIFIDiscontectConfirm.setModelStatus(true);
		mMsgBoxWIFIDiscontectConfirm.addToLayout(mMainLayout);
		
		//错误确认框
		mMsgBoxError.setMessage(R.string.gallery_msgbox_unknown_error);
		mMsgBoxError.setModelStatus(true);
		mMsgBoxError.addToLayout(mMainLayout);
		
		mShareMenu           = new ShareToModule(WiGalleryActivity.this, 0, 0);  // Don't touch this, dangerous
		RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams(300,LayoutParams.WRAP_CONTENT);
		mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		mShareMenu.setLayoutParams(mLayoutParams);
		mGalleryListAdapter  = new GalleryListViewAdapter(this,mGalleryMoreMenuResId);
		mSubListAdapter      = new GalleryListViewAdapter(this, mGalleryGroupMenuResId);
		mGalleryListAdapter.setMenuType(GalleryListViewAdapter.MENU_TYPE_MAIN_MENU);
		mSubListAdapter.setMenuType(GalleryListViewAdapter.MENU_TYPE_GROUP_MENU);
		mShareMenu.addToLayout(mMenuLayout);
		mShareMenu.setVisibility(View.INVISIBLE);
		mGalleryMoreListView.setAdapter(mGalleryListAdapter);
		mSubListView.setVisibility(View.INVISIBLE);
		mFavoriteLayout.setVisibility(View.INVISIBLE);
		mUnknownLayout.setVisibility(View.INVISIBLE);
		mTxvFavoriteNumber.setVisibility(View.INVISIBLE);
		mTxvUnknownNumber.setVisibility(View.INVISIBLE);
		mUnknownLayout.setClickable(true);
	}
	
	/**
	 * 初始化普通控件监听事件
	 */
	private void initListeners() {
		
		if(CSStaticData.DEBUG){
			mDebugBtnIncrease.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					CSStaticData.g_debug_varient += 0.005f;
					Log.i(TAG, "[3D Depth] Adjust = " + CSStaticData.g_debug_varient);
				}
			});
			mDebugBtnDecrease.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					CSStaticData.g_debug_varient -= 0.005f;
					Log.i(TAG, "[3D Depth] Adjust = " + CSStaticData.g_debug_varient);
				}
			});
		}
		
		
		//屏幕旋转传感器
		mOrenListener = new OrientationEventListener(WiGalleryActivity.this) {
			
			@Override
			public void onOrientationChanged(int orientation) {
//				if(orientation == ORIENTATION_UNKNOWN){
//					return;
//				}
//				if(mCurrentOrenState == -1){
//					mCurrentOrenState = orientation;
//				}
//				mCurrentOrenState = Util.roundOrientation(orientation, mCurrentOrenState);
				{//Cocoonshu 20120626
					mCurrentOrenState = ((orientation + 45) / 90 * 90) % 360;
				}
			}
		};
		mOrenListener.enable();
		
		//移动确认框
		mMsgBoxMoveConfirm.setPositiveButton(getResources().getString(R.string.gallery_msgbox_confirm_btn), new OnClickListener() {
			@Override
			public void onClick(View v) {
				mMsgBoxMoveConfirm.hide();
				execMoveTo();
			}
		});
		mMsgBoxMoveConfirm.setNegativeButton(getResources().getString(R.string.gallery_msgbox_cancel_btn), new OnClickListener() {
			@Override
			public void onClick(View v) {
				mMsgBoxMoveConfirm.hide();
				mNoNeedToUnlock = false;
				lockScreen(false);
			}
		});
		
		//删除确认框
		mMsgBoxDeleteConfirm.setPositiveButton(getResources().getString(R.string.gallery_msgbox_confirm_btn), new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mMsgBoxDeleteConfirm.hide();
				execDelete();
			}
		});
		mMsgBoxDeleteConfirm.setNegativeButton(getResources().getString(R.string.gallery_msgbox_cancel_btn), new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mMsgBoxDeleteConfirm.hide();
				mNoNeedToUnlock = false;
				lockScreen(false);
			}
		});
		
		//WIFI不连通确认框
		mMsgBoxWIFIDiscontectConfirm.setPositiveButton(getResources().getString(R.string.gallery_msgbox_confirm_btn), new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mMsgBoxWIFIDiscontectConfirm.hide();
				Message msg = new Message();
				msg.what    = HANDLE_JUMP_TO_LOCATION_MODE;
				msg.arg1    = 0;
				mUIHandler.sendMessage(msg);
			}
		});
		
		//错误确认框
		mMsgBoxError.setPositiveButton(R.string.gallery_msgbox_confirm_btn, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mMsgBoxError.hide();
				mMsgBoxError.setMessage(R.string.gallery_msgbox_unknown_error);
			}
		});
		
		//标题
		mTxvTitle.setOnClickListener(new OnClickListener() {
			
			@SuppressWarnings("static-access") // for m_element_group
			@Override
			public void onClick(View v) {
				if(mGlsurfaceView.getRender().m_element_group != null && 
						(mGlsurfaceView.getRender().m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_LOCATION_1
								||
								mGlsurfaceView.getRender().m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_LOCATION_2
								||
								mGlsurfaceView.getRender().m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_LOCATION_3
								||
								mGlsurfaceView.getRender().m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_LOCATION_4
								)){
					CSStaticData.EARTH_AUTO_ROTATE = !CSStaticData.EARTH_AUTO_ROTATE;
					
					AllocGPS.mallocGPS(); // 自动分配GPS信息
					
					return;
				}
//				if(CSStaticData.DEBUG){
//					lockScreen(!mHasLockedScreen);
//				}
			}
		});
		

		//未知计数器
		mUnknownLayout.setOnClickListener(new OnClickListener() {
			
			@SuppressWarnings("static-access")
			@Override
			public void onClick(View v) {
				if(mGlsurfaceView != null && mGlsurfaceView.getRender() != null){
					mGlsurfaceView.getRender().m_element_group.openGroup(mGlsurfaceView.getRender().m_element_group.getUnknownListIndex());
				}
			}
		});
		
		//2/3D切换按钮
		mBtnDimension.setOnChangedListener(new OnCheckedChangedListener() {
			
			@Override
			public void OnChecked(boolean isChecked) {
				
				//判断是否在动画中
				if(WiGalleryOpenGLRenderer.m_element_group != null && WiGalleryOpenGLRenderer.m_element_group.isMoving()){
					mBtnDimension.setChecked(!isChecked);
					return;
				}
				
				int isLandscape = getRequestedOrientation();
				mDimension = isChecked;
				CSStaticData.g_is_3D_mode = mDimension;
				if(mDimension){
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
					if (mGlsurfaceView.getRender().m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_DATE && !mGlsurfaceView.getRender().m_element_group.bEnterGroup()) {
						CSStaticData.g_surface_invalidate = false;
						callGroupingSet(CSStaticData.LIST_TYPE.LIST_DATE);
					}
				}else{
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
				}
				if(mGlsurfaceView != null){
					mGlsurfaceView.setDimension(mDimension);
					setScreenDimension(mDimension);
				}
			}
		});
		
		//照相机按钮
		mBtnCamera.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				callCamera();
			}
		});
		
		//菜单按钮
		mBtnMore.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					mMenuLayout.setVisibility(View.VISIBLE);
					mGalleryMoreListView.setSelection(0);
					mIsMoreMenuShow = true;
					
					//控制setFavorite和removeFavorite两个选项的可用性
					int checkListSize = 0;
					boolean isSet = false, isRemove = false;
					ArrayList<Element> checkList = (ArrayList<Element>) getSelectedElements();
					if(checkList != null){
						checkListSize = checkList.size();
						for(int i = 0; i < checkListSize; i++){
							if(checkList.get(i).bFavorite()){
								isRemove = true; //包含了已设定了喜好的文件
							}else{
								isSet = true;    //包含了未设定喜好的文件
							}
						}
					}
					if(!isSet && isRemove){//设定喜好不可用
						mMoreMenuDisableClickPosition = 3; //position of set favorite
					}
					if(!isRemove && isSet){//移除喜好不可用
						mMoreMenuDisableClickPosition = 4; //position of remove favorite
					}
					mGalleryMoreListView.setAdapter(mGalleryListAdapter);
				}else{
					mMenuLayout.setVisibility(View.INVISIBLE);
					mSubMenuLayout.setVisibility(View.INVISIBLE);
					recoverMoreMenuUIStyle();
					mIsMoreMenuShow = false;
					ELEM_STATUS status = WiGalleryOpenGLRenderer.m_element_group.getChoosedMode();
					if (status == ELEM_STATUS.NORMAL_STATUS){
						mShareMenu.hide();
						mShareMenu.setMetaType(MEDIA_META_TYPE.ALL_MEDIA_TYPE);
						mSubMenuLayout.setVisibility(View.INVISIBLE);
						mIsSubMoreMenuShow = false;
					}
					
					//还原可用性设置
					mMoreMenuDisableClickPosition = -1;
					mGalleryMoreListView.setAdapter(mGalleryListAdapter);
				}
			}
		});
		
		//分享菜单
		mShareMenu.setOnClickListener(new ShareToModule.OnClickListener() {
			
			@Override
			public void OnClick() {
				
				mGalleryMoreListView.setSelection(0);
				mSubMenuLayout.setVisibility(View.INVISIBLE);
				mMenuLayout.setVisibility(View.INVISIBLE);
				recoverMoreMenuUIStyle();
				mIsSubMoreMenuShow = false;
				mIsMoreMenuShow = false;
				mBtnMore.setChecked(false);
				if(WiGalleryOpenGLRenderer.m_element_group != null){
					ELEM_STATUS status = WiGalleryOpenGLRenderer.m_element_group.getChoosedMode();
					if (status == ELEM_STATUS.NORMAL_STATUS){
						mHasOperatedMenu = true;
						mGlsurfaceView.getRender().setMediaMetaType(mShareMenu.getMetaType());
						WiGalleryOpenGLRenderer.m_element_group.setChoosedMode(ELEM_STATUS.SELECTED_STATUS);
						mCurMultiStatus = MULTI_SHARE_ID;
					}
					else {
						callShareTo();
						mGlsurfaceView.getRender().setMediaMetaType(MEDIA_META_TYPE.ALL_MEDIA_TYPE);
						WiGalleryOpenGLRenderer.m_element_group.setChoosedMode(ELEM_STATUS.NORMAL_STATUS);
						mCurMultiStatus = MULTI_NONE_ID;
						mHasOperatedMenu = false;
					}
				}
			}
		});
		
		//取消按钮
		mBtnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(WiGalleryOpenGLRenderer.m_element_group != null){
					mGlsurfaceView.getRender().setMediaMetaType(MEDIA_META_TYPE.ALL_MEDIA_TYPE);
					WiGalleryOpenGLRenderer.m_element_group.setChoosedMode(ELEM_STATUS.NORMAL_STATUS);
					callMultiCancel();
				}
				mCurMultiStatus = MULTI_NONE_ID;
				mHasOperatedMenu = false;
				
				mShareMenu.hide();
				mSubMenuLayout.setVisibility(View.INVISIBLE);
				mMenuLayout.setVisibility(View.INVISIBLE);
				recoverMoreMenuUIStyle();
				mIsSubMoreMenuShow = false;
				mIsMoreMenuShow = false;
				mBtnMore.setChecked(false);
			}
		});
		
		//执行按钮
		mBtnExecute.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(WiGalleryOpenGLRenderer.m_element_group != null){
					switch (mCurMultiStatus) {
					case MULTI_USER_ID:

						break;

					case MULTI_SHARE_ID:
						mNoNeedToUnlock = true;
						callShareTo();
						break;     

					case MULTI_DELETE_ID:
						mNoNeedToUnlock = true;
						callDelete();
						break;

					case MULTI_MOVETO_INTERNAL_ID:
						mNoNeedToUnlock = true;
						callMoveTo(MOVE_TO_INTERNAL_DIR);
						break;
						
					case MULTI_MOVETO_EXTERNAL_ID:
						mNoNeedToUnlock = true;
						callMoveTo(MOVE_TO_EXTERNAL_DIR);
						break;

					case MULTI_FILEINFO_ID:

						break;

					case MULTI_FAVORITE_ID:
						mNoNeedToUnlock = true;
						callSetFavorite(true);
						break;
						
					case MULTI_REMOVE_FAVORITE_ID:
						mNoNeedToUnlock = true;
						callSetFavorite(false);
						break;

					case MULTI_NONE_ID:
						break;
					}
					
					mGlsurfaceView.getRender().setMediaMetaType(MEDIA_META_TYPE.ALL_MEDIA_TYPE);
					WiGalleryOpenGLRenderer.m_element_group.setChoosedMode(ELEM_STATUS.NORMAL_STATUS);
				}
				
				mCurMultiStatus = MULTI_NONE_ID;
				mHasOperatedMenu = false;
			}
		});
		
		//翻页滚动条
		mSbrScrollPage.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@SuppressWarnings("static-access") //for m_element_group
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// START
				if(WiGalleryOpenGLRenderer.m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_DATE 
						&& !WiGalleryOpenGLRenderer.m_element_group.bEnterGroup()){
					mTxvDate.setVisibility(View.GONE);
				}else{
					if(mGlsurfaceView.getRender().m_element_group != null && 
					   mGlsurfaceView.getRender().m_element_group.getCurrentDisplayedList() != null &&
					   mGlsurfaceView.getRender().m_element_group.getCurrentDisplayedList().size() > 0){
						mTxvDate.setVisibility(View.VISIBLE);
					}
				}
				String dateStr = null;
				if(mGlsurfaceView.getRender().m_element_group.getCurrentDisplayedList() != null && mGlsurfaceView.getRender().m_element_group.getCurrentDisplayedList().size() > 0){
					dateStr = mGlsurfaceView.getRender().m_element_group.getCurrentDisplayedList().get(seekBar.getProgress()*CSStaticData.group_none_row_num).getDate();
				}
				if(dateStr != null && dateStr.length() > 0){
					dateStr = dateStr.substring(0, 4) + "-" + dateStr.substring(5, 7) + "-" + dateStr.substring(8, 10);
				}else{
					dateStr = "1970-01-01";
				}
				mTxvDate.setText(dateStr);
				if(mGlsurfaceView != null && mGlsurfaceView.getRender() != null && mGlsurfaceView.getRender().m_element_group != null){
					seekBar.setMax(mGlsurfaceView.getRender().m_element_group.getTotalIndex());
					if(CSStaticData.DEBUG){
						Log.w(TAG, "[onScenceChanged]设置滑动条最大值为 " + mSbrScrollPage.getMax());
					}
				}
			}
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// STOP
				mTxvDate.setVisibility(View.GONE);
				mTxvDate.setText("1970-01-01");
			}
			
			@SuppressWarnings("static-access") //for m_element_group
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// CHANGE			
				if(fromUser){
					if(mGlsurfaceView != null && mGlsurfaceView.getRender() != null && mGlsurfaceView.getRender().m_element_group != null){
						String dateStr = null;
						if(mGlsurfaceView.getRender().m_element_group.getCurrentDisplayedList() != null){
							dateStr = mGlsurfaceView.getRender().m_element_group.getCurrentDisplayedList().get(progress*CSStaticData.group_none_row_num).getDate();
						}
						if(dateStr != null && dateStr.length() > 0){
							dateStr = dateStr.substring(0, 4) + "-" + dateStr.substring(5, 7) + "-" + dateStr.substring(8, 10);
						}else{
							dateStr = "1970-01-01";
						}
						mTxvDate.setText(dateStr);
						mGlsurfaceView.getRender().m_element_group.shiftTo(progress);
					}
				}
			}
		});
		
		mMenuLayout.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mIsMoreMenuTouched = true;
				return false;
			}
		});
		
		mGalleryMoreListView.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("static-access")
			@Override
			public void onItemClick(AdapterView<?> menuItemList, View menuItem, int pos, long id) {
				if(pos == -1 || pos == 3 || pos == 4){ // 不允许禁用除setFavorite和removeFavorite以外的其他项
					mMoreMenuCurClickPosition = pos;
				}
				int count = menuItemList.getChildCount();
				for (int i = 0; i < count ; i++) {
					if(((TextView) menuItemList.getChildAt(i).findViewById(R.id.galleryMenuText)).getCurrentTextColor() != Color.GRAY){
						((TextView) menuItemList.getChildAt(i).findViewById(R.id.galleryMenuText)).setTextColor(COLORSTATELIST_WHITE);
					}
				}
				
				((TextView) menuItem.findViewById(R.id.galleryMenuText)).setTextColor(COLORSTATELIST_GREEN);

				switch (pos) {
				case 0: //Share
					if(!mIsSubMoreMenuShow || mSubListAdapter.getItemStartId() != -5){
						mShareMenu.setMetaType(FileTypeHelper.getMetaTypeInSelectedFiles(getSelectedFiles()));
						mSubListAdapter.setItemStartId(-5); //sid = -5 < -2, 用来忽悠其他正版子菜单的
						mShareMenu.show();
						mSubMenuLayout.setVisibility(View.INVISIBLE);
						mIsSubMoreMenuShow = true;
					}else if(mIsSubMoreMenuShow){
						mShareMenu.hide();
						mShareMenu.setMetaType(MEDIA_META_TYPE.ALL_MEDIA_TYPE);
						mSubMenuLayout.setVisibility(View.INVISIBLE);
						mIsSubMoreMenuShow = !mIsSubMoreMenuShow;
					}

					break;
				case 1: //Delete
					mSubMenuLayout.setVisibility(View.INVISIBLE);
					mMenuLayout.setVisibility(View.INVISIBLE);
					recoverMoreMenuUIStyle();
					mIsSubMoreMenuShow = false;
					mIsMoreMenuShow = false;
					mBtnMore.setChecked(false);
					if(WiGalleryOpenGLRenderer.m_element_group != null){
						ELEM_STATUS status = WiGalleryOpenGLRenderer.m_element_group.getChoosedMode();
						if (status == ELEM_STATUS.NORMAL_STATUS){
							mHasOperatedMenu = true;
							WiGalleryOpenGLRenderer.m_element_group.setChoosedMode(ELEM_STATUS.SELECTED_STATUS);
							mCurMultiStatus = MULTI_DELETE_ID;
						}
						else {
							callDelete();
							mGlsurfaceView.getRender().setMediaMetaType(MEDIA_META_TYPE.ALL_MEDIA_TYPE);
							WiGalleryOpenGLRenderer.m_element_group.setChoosedMode(ELEM_STATUS.NORMAL_STATUS);
							mCurMultiStatus = MULTI_NONE_ID;
							mHasOperatedMenu = false;
						}
					}
					break;
				case 2: //Move to
					if(!mIsSubMoreMenuShow || mSubListAdapter.getItemStartId() != SUBMENU_MOVETO_INTERNAL){
						mSubListAdapter.setItemStartId(SUBMENU_MOVETO_INTERNAL);
						mSubListAdapter.setResId(mGalleryMoveToMenuResId);
						mSubListAdapter.setMenuType(GalleryListViewAdapter.MENU_TYPE_MOVETO_MENU);
						mSubMenuTitle.setText(R.string.gallery_menu_item_move);
						mSubMenuLayout.setVisibility(View.VISIBLE);
						mShareMenu.hide();
						mShareMenu.setMetaType(MEDIA_META_TYPE.ALL_MEDIA_TYPE);
						mIsSubMoreMenuShow = true;
					}else if(mIsSubMoreMenuShow){
						mSubMenuLayout.setVisibility(View.INVISIBLE);
						mIsSubMoreMenuShow = !mIsSubMoreMenuShow;
					}
					break;
				case 3: //Set as favorite
					mSubMenuLayout.setVisibility(View.INVISIBLE);
					mMenuLayout.setVisibility(View.INVISIBLE);
					recoverMoreMenuUIStyle();
					mIsSubMoreMenuShow = false;
					mIsMoreMenuShow = false;
					mBtnMore.setChecked(false);
					if(WiGalleryOpenGLRenderer.m_element_group != null){
						ELEM_STATUS status = WiGalleryOpenGLRenderer.m_element_group.getChoosedMode();
						if (status == ELEM_STATUS.NORMAL_STATUS){
							mHasOperatedMenu = true;
							mGlsurfaceView.getRender().setMediaMetaType(CSStaticData.MEDIA_META_TYPE.SET_FAVORITE_TYPE);
							WiGalleryOpenGLRenderer.m_element_group.setChoosedMode(ELEM_STATUS.SELECTED_STATUS);
							mCurMultiStatus = MULTI_FAVORITE_ID;
						}
						else {
							callSetFavorite(true);
							mGlsurfaceView.getRender().setMediaMetaType(MEDIA_META_TYPE.ALL_MEDIA_TYPE);
							WiGalleryOpenGLRenderer.m_element_group.setChoosedMode(ELEM_STATUS.NORMAL_STATUS);
							mCurMultiStatus = MULTI_NONE_ID;
							mHasOperatedMenu = false;
						}
					}
					break;
					
				case 4: //Remove from favorite
					mSubMenuLayout.setVisibility(View.INVISIBLE);
					mMenuLayout.setVisibility(View.INVISIBLE);
					recoverMoreMenuUIStyle();
					mIsSubMoreMenuShow = false;
					mIsMoreMenuShow = false;
					mBtnMore.setChecked(false);
					if(WiGalleryOpenGLRenderer.m_element_group != null){
						ELEM_STATUS status = WiGalleryOpenGLRenderer.m_element_group.getChoosedMode();
						if (status == ELEM_STATUS.NORMAL_STATUS){
							mHasOperatedMenu = true;
							mGlsurfaceView.getRender().setMediaMetaType(CSStaticData.MEDIA_META_TYPE.REMOVE_FAVORITE_TYPE);
							WiGalleryOpenGLRenderer.m_element_group.setChoosedMode(ELEM_STATUS.SELECTED_STATUS);
							mCurMultiStatus = MULTI_REMOVE_FAVORITE_ID;
						}
						else {
							callSetFavorite(false);
							mGlsurfaceView.getRender().setMediaMetaType(MEDIA_META_TYPE.ALL_MEDIA_TYPE);
							WiGalleryOpenGLRenderer.m_element_group.setChoosedMode(ELEM_STATUS.NORMAL_STATUS);
							mCurMultiStatus = MULTI_NONE_ID;
							mHasOperatedMenu = false;
						}
					}
					break;
				case 5: //Group
					if(!mIsSubMoreMenuShow || mSubListAdapter.getItemStartId() != SUBMENU_GROUP_NONE){
						mSubListAdapter.setItemStartId(SUBMENU_GROUP_NONE);
						mSubListAdapter.setResId(mGalleryGroupMenuResId);
						mSubListAdapter.setMenuType(GalleryListViewAdapter.MENU_TYPE_GROUP_MENU);
						mSubMenuTitle.setText(R.string.gallery_menu_item_group);
						mSubMenuLayout.setVisibility(View.VISIBLE);
						mShareMenu.hide();
						mShareMenu.setMetaType(MEDIA_META_TYPE.ALL_MEDIA_TYPE);
						mIsSubMoreMenuShow = true;
					}else if(mIsSubMoreMenuShow){
						mSubMenuLayout.setVisibility(View.INVISIBLE);
						mIsSubMoreMenuShow = !mIsSubMoreMenuShow;
					}
					break;
				case 6: //Sort Order
					if(mGlsurfaceView.getRender().m_element_group != null
						&&
					   !((
						mGlsurfaceView.getRender().m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_DATE
							   ||
						(mGlsurfaceView.getRender().m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_LOCATION_1
							   ||
						mGlsurfaceView.getRender().m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_LOCATION_2
							   ||
						mGlsurfaceView.getRender().m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_LOCATION_3
							   ||
						mGlsurfaceView.getRender().m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_LOCATION_4
						) && !mGlsurfaceView.getRender().m_element_group.bEnterGroup())
						)){ //直接执行Case 7
						if(!mIsSubMoreMenuShow || mSubListAdapter.getItemStartId() != SUBMENU_SORTORDER_DESC){
							mSubListAdapter.setItemStartId(SUBMENU_SORTORDER_DESC);
							mSubListAdapter.setResId(mGallerySortOrderMenuResId);
							mSubListAdapter.setMenuType(GalleryListViewAdapter.MENU_TYPE_ORDER_MENU);
							mSubMenuTitle.setText(R.string.gallery_menu_item_sortOrder);
							mSubMenuLayout.setVisibility(View.VISIBLE);
							mShareMenu.hide();
							mShareMenu.setMetaType(MEDIA_META_TYPE.ALL_MEDIA_TYPE);
							mIsSubMoreMenuShow = true;
						}else if(mIsSubMoreMenuShow){
							mSubMenuLayout.setVisibility(View.INVISIBLE);
							mIsSubMoreMenuShow = !mIsSubMoreMenuShow;
						}
						break;
					}
				case 7: //Content switch
					if(!mIsSubMoreMenuShow || mSubListAdapter.getItemStartId() != SUBMENU_CONTENTSWITCH_ALLFILES){
						mSubListAdapter.setItemStartId(SUBMENU_CONTENTSWITCH_ALLFILES);
						mSubListAdapter.setResId(mGalleryContentSwitchMenuResId);
						mSubListAdapter.setMenuType(GalleryListViewAdapter.MENU_TYPE_FILTER_MENU);
						mSubMenuTitle.setText(R.string.gallery_menu_item_contentSwitch);
						mSubMenuLayout.setVisibility(View.VISIBLE);
						mShareMenu.hide();
						mShareMenu.setMetaType(MEDIA_META_TYPE.ALL_MEDIA_TYPE);
						mIsSubMoreMenuShow = true;
					}else if(mIsSubMoreMenuShow){
						mSubMenuLayout.setVisibility(View.INVISIBLE);
						mIsSubMoreMenuShow = !mIsSubMoreMenuShow;
					}
					break;
				case 8: //.....
					mSubMenuLayout.setVisibility(View.INVISIBLE);
					mMenuLayout.setVisibility(View.INVISIBLE);
					mShareMenu.hide();
					mShareMenu.setMetaType(MEDIA_META_TYPE.ALL_MEDIA_TYPE);
					recoverMoreMenuUIStyle();
					mIsSubMoreMenuShow = false;
					mIsMoreMenuShow = false;
					mBtnMore.setChecked(false);
					break;

				}

				
				//如果当前点击时，mIsSubMoreMenuShow为true，则说明要关闭子菜单，此时需要把当前Item变白
				if(!mIsSubMoreMenuShow){
					((TextView) menuItem.findViewById(R.id.galleryMenuText)).setTextColor(Color.WHITE);
				}
				mSubListView.setAdapter(mSubListAdapter);
				mSubListView.setVisibility(View.VISIBLE);
			}
		});



		mSubListView.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("static-access") //for m_element_group
			@Override
			public void onItemClick(AdapterView<?> menuItemList, View menuItem, int pos, long id) {
				switch (menuItem.getId()) {
				//MoveTo菜单
				case SUBMENU_MOVETO_INTERNAL:
					mSubMenuLayout.setVisibility(View.INVISIBLE);
					mMenuLayout.setVisibility(View.INVISIBLE);
					recoverMoreMenuUIStyle();
					mBtnMore.setChecked(false);
					mIsSubMoreMenuShow = false;
					mIsMoreMenuShow = false;
					if(WiGalleryOpenGLRenderer.m_element_group != null){
						ELEM_STATUS status = WiGalleryOpenGLRenderer.m_element_group.getChoosedMode();
						if (status == ELEM_STATUS.NORMAL_STATUS){
							mHasOperatedMenu = true;
							WiGalleryOpenGLRenderer.m_element_group.setChoosedMode(ELEM_STATUS.SELECTED_STATUS);
							mCurMultiStatus = MULTI_MOVETO_INTERNAL_ID;
						}
						else {
							mCurMultiStatus = MULTI_MOVETO_INTERNAL_ID;
							callMoveTo(MOVE_TO_INTERNAL_DIR);
							mGlsurfaceView.getRender().setMediaMetaType(MEDIA_META_TYPE.ALL_MEDIA_TYPE);
							WiGalleryOpenGLRenderer.m_element_group.setChoosedMode(ELEM_STATUS.NORMAL_STATUS);
							mCurMultiStatus = MULTI_NONE_ID;
							mHasOperatedMenu = false;
						}
					}
					break;
				case SUBMENU_MOVETO_EXTERNAL:
					mSubMenuLayout.setVisibility(View.INVISIBLE);
					mMenuLayout.setVisibility(View.INVISIBLE);
					recoverMoreMenuUIStyle();
					mBtnMore.setChecked(false);
					mIsSubMoreMenuShow = false;
					mIsMoreMenuShow = false;
					if(WiGalleryOpenGLRenderer.m_element_group != null){
						ELEM_STATUS status = WiGalleryOpenGLRenderer.m_element_group.getChoosedMode();
						if (status == ELEM_STATUS.NORMAL_STATUS){
							mHasOperatedMenu = true;
							WiGalleryOpenGLRenderer.m_element_group.setChoosedMode(ELEM_STATUS.SELECTED_STATUS);
							mCurMultiStatus = MULTI_MOVETO_EXTERNAL_ID;
						}
						else {
							mCurMultiStatus = MULTI_MOVETO_EXTERNAL_ID;
							callMoveTo(MOVE_TO_EXTERNAL_DIR);
							mGlsurfaceView.getRender().setMediaMetaType(MEDIA_META_TYPE.ALL_MEDIA_TYPE);
							WiGalleryOpenGLRenderer.m_element_group.setChoosedMode(ELEM_STATUS.NORMAL_STATUS);
							mCurMultiStatus = MULTI_NONE_ID;
							mHasOperatedMenu = false;
						}
					}
					break;
				
				//Group菜单
				case SUBMENU_GROUP_NONE:
					mGroupMenuCurClickPosition = 0; //记录点击位置
					callGroupingSet(CSStaticData.LIST_TYPE.LIST_NONE);
					mSubMenuLayout.setVisibility(View.INVISIBLE);
					mMenuLayout.setVisibility(View.INVISIBLE);
					recoverMoreMenuUIStyle();
					mBtnMore.setChecked(false);
					mIsSubMoreMenuShow = false;
					mIsMoreMenuShow = false;
					break;
				case SUBMENU_GROUP_LOCATION:
					mGroupMenuCurClickPosition = 1; //记录点击位置
//					callGroupingSet(CSStaticData.LIST_TYPE.LIST_LOCATION_1);
					callWIFIChecking();
					mSubMenuLayout.setVisibility(View.INVISIBLE);
					mMenuLayout.setVisibility(View.INVISIBLE);
					recoverMoreMenuUIStyle();
					mBtnMore.setChecked(false);
					mIsSubMoreMenuShow = false;
					mIsMoreMenuShow = false;
					break;
				case SUBMENU_GROUP_DATE:
					mGroupMenuCurClickPosition = 2; //记录点击位置
					callGroupingSet(CSStaticData.LIST_TYPE.LIST_DATE);
					mSubMenuLayout.setVisibility(View.INVISIBLE);
					mMenuLayout.setVisibility(View.INVISIBLE);
					recoverMoreMenuUIStyle();
					mBtnMore.setChecked(false);
					mIsSubMoreMenuShow = false;
					mIsMoreMenuShow = false;
					break;


				//SortOrder菜单
				case SUBMENU_SORTORDER_DESC:
					mOrderMenuCurClickPosition = 0; //记录点击位置
					callSortOrder(false);
					mSubMenuLayout.setVisibility(View.INVISIBLE);
					mMenuLayout.setVisibility(View.INVISIBLE);
					recoverMoreMenuUIStyle();
					mBtnMore.setChecked(false);
					mIsSubMoreMenuShow = false;
					mIsMoreMenuShow = false;

					break;
				case SUBMENU_SORTORDER_ASC:
					mOrderMenuCurClickPosition = 1; //记录点击位置
					callSortOrder(true);
					mSubMenuLayout.setVisibility(View.INVISIBLE);
					mMenuLayout.setVisibility(View.INVISIBLE);
					recoverMoreMenuUIStyle();
					mBtnMore.setChecked(false);
					mIsSubMoreMenuShow = false;
					mIsMoreMenuShow = false;

					break;

					
				//Content Switch菜单
				case SUBMENU_CONTENTSWITCH_ALLFILES:
					mFilterMenuCurClickPosition = 0; //记录点击位置
					callFileFilter(VIEW_FILTER_ALL);
					mSubMenuLayout.setVisibility(View.INVISIBLE);
					mMenuLayout.setVisibility(View.INVISIBLE);
					recoverMoreMenuUIStyle();
					mBtnMore.setChecked(false);
					mIsSubMoreMenuShow = false;
					mIsMoreMenuShow = false;
					break;
				case SUBMENU_CONTENTSWITCH_2D_ONLY:
					mFilterMenuCurClickPosition = 1; //记录点击位置
					callFileFilter(VIEW_FILTER_2D);
					mSubMenuLayout.setVisibility(View.INVISIBLE);
					mMenuLayout.setVisibility(View.INVISIBLE);
					recoverMoreMenuUIStyle();
					mBtnMore.setChecked(false);
					mIsSubMoreMenuShow = false;
					mIsMoreMenuShow = false;
					break;
				case SUBMENU_CONTENTSWITCH_3D_ONLY:
					mFilterMenuCurClickPosition = 2; //记录点击位置
					callFileFilter(VIEW_FILTER_3D);
					mSubMenuLayout.setVisibility(View.INVISIBLE);
					mMenuLayout.setVisibility(View.INVISIBLE);
					recoverMoreMenuUIStyle();
					mBtnMore.setChecked(false);
					mIsSubMoreMenuShow = false;
					mIsMoreMenuShow = false;
					break;
				}

			}
		});
		
		mPopupLayout.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(mShareMenu != null && mShareMenu.isShown()){
					mShareMenu.hide();
				}
				return false;
			}
		});
	}

	/**
	 * 初始化广播
	 */
	private void initBoastcasts() {}

	/**
	 * 初始化消息队列
	 */
	private void initHandles() {
		mUIHandler = new Handler(){

			@SuppressWarnings("static-access") // for m_element_group
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case HANDLE_REFLASH_FAVORITE_COUNTER: //刷新喜好计数器
					int favorNum = 0;

					if(WiGalleryOpenGLRenderer.m_element_group != null){
						if(WiGalleryOpenGLRenderer.m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_INVALID){
							mFavoriteLayout.setVisibility(View.INVISIBLE);
							mTxvFavoriteNumber.setVisibility(View.INVISIBLE);
						}else{
							if(WiGalleryOpenGLRenderer.m_element_group.getType() != CSStaticData.LIST_TYPE.LIST_NONE 
									&&
									!WiGalleryOpenGLRenderer.m_element_group.bEnterGroup()){
								favorNum = WiGalleryOpenGLRenderer.m_element_group.getFavoriteNum();
								mFavoriteLayout.setVisibility(View.VISIBLE);
								mTxvFavoriteNumber.setVisibility(View.VISIBLE);
								mTxvFavoriteNumber.setText("" + favorNum);
							}else{
								mFavoriteLayout.setVisibility(View.INVISIBLE);
								mTxvFavoriteNumber.setVisibility(View.INVISIBLE);
							}
						}
					}

					break;
					
				case HANDLE_REFLASH_UNKNOWN_COUNTER: //刷新未知计数器
					int unknownNum = 0;

					if(WiGalleryOpenGLRenderer.m_element_group != null){
						if(WiGalleryOpenGLRenderer.m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_INVALID){
							mUnknownLayout.setVisibility(View.INVISIBLE);
							mTxvUnknownNumber.setVisibility(View.INVISIBLE);
						}else{
							if(WiGalleryOpenGLRenderer.m_element_group.getType() != CSStaticData.LIST_TYPE.LIST_NONE 
									&&
									WiGalleryOpenGLRenderer.m_element_group.getType() != CSStaticData.LIST_TYPE.LIST_DATE
									&&
									!WiGalleryOpenGLRenderer.m_element_group.bEnterGroup()){
								unknownNum = WiGalleryOpenGLRenderer.m_element_group.getUnknownNum();
								mUnknownLayout.setVisibility(View.VISIBLE);
								mTxvUnknownNumber.setVisibility(View.VISIBLE);
								mTxvUnknownNumber.setText("" + unknownNum);
							}else{
								mUnknownLayout.setVisibility(View.INVISIBLE);
								mTxvUnknownNumber.setVisibility(View.INVISIBLE);
							}
						}
					}
					
					break;
					
				case HANDLE_REFLASH_PAGE_SEEKBAR: //刷新Seekbar样式
					if(WiGalleryOpenGLRenderer.m_element_group != null){
						if((WiGalleryOpenGLRenderer.m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_LOCATION_1
								||
								WiGalleryOpenGLRenderer.m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_LOCATION_2
								||
								WiGalleryOpenGLRenderer.m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_LOCATION_3
								||
								WiGalleryOpenGLRenderer.m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_LOCATION_4
								)
								&&
								!WiGalleryOpenGLRenderer.m_element_group.bEnterGroup()){
							mSbrScrollPage.setVisibility(View.INVISIBLE);
						}else{
							mSbrScrollPage.setVisibility(View.VISIBLE);
						}
					}else{
						mSbrScrollPage.setVisibility(View.VISIBLE);
					}
					
					mSbrScrollPage.setProgress(0); // 以防m_element_group为空时取的默认值
					if(mGlsurfaceView != null && mGlsurfaceView.getRender() != null && mGlsurfaceView.getRender().m_element_group != null){
						mSbrScrollPage.setMax(mGlsurfaceView.getRender().m_element_group.getTotalIndex());
						mSbrScrollPage.setProgress(mGlsurfaceView.getRender().m_element_group.getCurIndex());
						if(CSStaticData.DEBUG){
							Log.w(TAG, "[onScenceChanged]设置滑动条最大值为 " + mSbrScrollPage.getMax());
						}
					}
					break;

				case HANDLE_SWITCH_MORE_MENU_ITEMS:
					switchMoreMenuItem((Boolean)msg.obj);
					break;
					
				case HANDLE_REFLASH_MORE_MENU_STYLE:
					recoverMoreMenuUIStyle();
					break;
					
				case HANDLE_REFLASH_GALLERY_TITLE:
					String title = (String)msg.obj;
					mTxvTitle.setText(title);
					break;
					
				case HANDLE_MULTI_SELECT_ON_MENU:
					mBtnCamera.setVisibility(View.GONE);
					mBtnDimension.setVisibility(View.GONE);
					mBtnMore.setVisibility(View.GONE);
					mBtnCancel.setVisibility(View.VISIBLE);
					mBtnExecute.setVisibility(View.VISIBLE);
					break;
					
				case HANDLE_MULTI_SELECT_NOT_ON_MENU:
					mBtnCamera.setVisibility(View.GONE);
					mBtnDimension.setVisibility(View.GONE);
					mBtnMore.setVisibility(View.VISIBLE);
					mBtnCancel.setVisibility(View.VISIBLE);
					mBtnExecute.setVisibility(View.GONE);
					break;
					
				case HANDLE_NONMULTI_SELECT:
					mBtnCamera.setVisibility(View.VISIBLE);
					mBtnDimension.setVisibility(View.VISIBLE);
					mBtnMore.setVisibility(View.VISIBLE);
					mBtnCancel.setVisibility(View.GONE);
					mBtnExecute.setVisibility(View.GONE);
					mSubMenuLayout.setVisibility(View.INVISIBLE);
					mMenuLayout.setVisibility(View.INVISIBLE);
					break;

				case HANDLE_MOVE_PROGRESS:
					mMsgBoxMoveProgress.show();
					mMsgBoxMoveProgress.setProgress(msg.arg1);
					break;
					
				case HANDLE_MOVE_COMPLETED:
				{
					int    fileNum = 0;
					String strPlural  = getResources().getString(R.string.gallery_string_plural);
					String strHasOrHave = getResources().getString(R.string.gallery_string_have);
					String strDestination = getResources().getString(R.string.gallery_string_internal);
					
					if(mMultiTempData != null && mMultiTempData.isMoveTo()){
						fileNum = mMultiTempData.getSelectedFileNum();
					}else{
						fileNum = msg.arg1 + msg.arg2;
					}
					
					if(fileNum < 2){
						strPlural = "";
						strHasOrHave = getResources().getString(R.string.gallery_string_has);
					}
					if(mMultiTempData != null && mMultiTempData.isMoveTo() && mMultiTempData.isMoveToExternal()){
						strDestination = getResources().getString(R.string.gallery_string_external);
					}
					mMsgBoxMoveProgress.hide();
					if(fileNum == 1){
						mMsgBoxMovedTipbox.setMessage(
								String.format(getResources().getString(R.string.gallery_msgbox_moved_to_tip_single), 
										"\"" + FileTypeHelper.getFileNameFromPath((String)msg.obj) + "\"", strHasOrHave, strDestination));
					}else{
						mMsgBoxMovedTipbox.setMessage(
								String.format(getResources().getString(R.string.gallery_msgbox_moved_to_tip), 
										fileNum, strPlural, strHasOrHave, strDestination));
					}
					mMsgBoxMovedTipbox.show();
					
					//清空临时数据
					if(mMultiTempData != null){
						mMultiTempData.clear();
						mMultiTempData = null;
					}
					
					//执行完移动解除锁屏
					mNoNeedToUnlock = false;
					Message emergencyMsg = new Message();
					emergencyMsg.what    = HANDLE_LOCK_SCREEN;
					emergencyMsg.obj     = false;
					mEmergencyHandler.sendMessageDelayed(emergencyMsg, 2000);
					//lockScreen(false); 
				}
					break;
					
				case HANDLE_DELETE_PROGRESS:
					mMsgBoxDeleteProgress.show();
					mMsgBoxDeleteProgress.setProgress(msg.arg1);
					break;
					
				case HANDLE_DELETE_COMPLETED:{
					mMsgBoxDeleteProgress.hide();
					
					//清空临时数据
					if(mMultiTempData != null){
						mMultiTempData.clear();
						mMultiTempData = null;
					}
					
					//执行完删除解除锁屏
					mNoNeedToUnlock = false;
					Message emergencyMsg = new Message();
					emergencyMsg.what    = HANDLE_LOCK_SCREEN;
					emergencyMsg.obj     = false;
					mEmergencyHandler.sendMessageDelayed(emergencyMsg, 2000);
					//lockScreen(false); 
					
					//刷新喜好计数器
					mUIHandler.sendEmptyMessage(HANDLE_REFLASH_FAVORITE_COUNTER);
				}
					break;
					
				case HANDLE_WIFI_CHECKED:
				{
					boolean isConnected = (Boolean)msg.obj;
					if(isConnected){
						if(mMsgBoxWIFITipbox != null){
							mMsgBoxWIFITipbox.hideDelay(500);
						}
						mUIHandler.sendEmptyMessage(HANDLE_JUMP_TO_LOCATION_MODE);
					}else{
//						startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
//						mIsCallingSystemSetting = true;
						if(mMsgBoxWIFITipbox != null){
							mMsgBoxWIFITipbox.hide();
						}
						if(mMsgBoxWIFIDiscontectConfirm != null){
							mMsgBoxWIFIDiscontectConfirm.show();
						}
					}
				}
					break;
					
				case HANDLE_SET_FAVORITE_TIP:{
					mMsgBoxSetFavoriteProgress.hide();
					mMsgBoxSetFavoriteTipbox.show();
					mNoNeedToUnlock = false;
					Message emergencyMsg = new Message();
					emergencyMsg.what    = HANDLE_LOCK_SCREEN;
					emergencyMsg.obj     = false;
					mEmergencyHandler.sendMessageDelayed(emergencyMsg, 2000);
					//lockScreen(false); //执行完解除锁屏
				}
					break;
					
				case HANDLE_REMOVE_FAVORITE_TIP:{
					mMsgBoxRemoveFavoriteProgress.hide();
					mMsgBoxRemoveFavoriteTipbox.show();
					mNoNeedToUnlock = false;
					Message emergencyMsg = new Message();
					emergencyMsg.what    = HANDLE_LOCK_SCREEN;
					emergencyMsg.obj     = false;
					mEmergencyHandler.sendMessageDelayed(emergencyMsg, 2000);
					//lockScreen(false); //执行完解除锁屏
				}
					break;
					
				case HANDLE_APPLICATION_LAUNCHED:
					mBtnMore.setEnabled(true);
					mBtnMore.setChecked(false);
					mBtnDimension.setEnabled(true);
					//if(CSStaticData.gStatusInfo != null){
					//	CSStaticData.gStatusInfo.restoreSortOrderMode();
					//}
					//callSortOrder(CSStaticData.g_sort_order_mode);
					break;
					
				case HANDLE_SCROLL_PAGE_INTERFACE:
					mSbrScrollPage.setMax(msg.arg2);
					mSbrScrollPage.setProgress(msg.arg1);
					break;
					
				case HANDLE_JUMP_TO_LOCATION_MODE:
					if(msg == null){
						callGroupingSet(CSStaticData.LIST_TYPE.LIST_LOCATION_1);
					}else{
						switch (msg.arg1) {
						case 1:
							callGroupingSet(CSStaticData.LIST_TYPE.LIST_LOCATION_2);
							break;
						case 2:
							callGroupingSet(CSStaticData.LIST_TYPE.LIST_LOCATION_3);
							break;
						case 3:
							callGroupingSet(CSStaticData.LIST_TYPE.LIST_LOCATION_4);
							break;
						case 0:
						default:
							callGroupingSet(CSStaticData.LIST_TYPE.LIST_LOCATION_1);
							break;
						}
					}
					
					break;
				default:
					break;
				}
			}
			
		};
		
		mEmergencyHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case HANDLE_LOCK_SCREEN:
					lockScreen((Boolean)msg.obj);
					break;

				default:
					break;
				}
			}
			
		};
	}
	
	/**
	 * 恢复保存状态
	 */
	private void initStatusData() {
		
		CSStaticData.gStatusInfo = (StatusInfo) getLastNonConfigurationInstance();
		if(CSStaticData.gStatusInfo != null){
			CSStaticData.gStatusInfo.restoreActivity();
			if(mSbrScrollPage != null){
				mSbrScrollPage.setMax(CSStaticData.gStatusInfo.restoreActivitySeekbarMax());
				mSbrScrollPage.setProgress(CSStaticData.gStatusInfo.restoreActivitySeekbarProgress());
			}
		}
		mBtnDimension.setCheckedWithCallback(CSStaticData.g_is_3D_mode);
	}
	
	
	/**
	 * 调用相机
	 */
	private void callCamera(){
		Intent intent = new Intent();
		intent.setClass(WiGalleryActivity.this, com.wistron.WiCamera.WiCameraActivity.class);
		intent.putExtra("camera_cmd", "call_from_gallery");
		startActivity(intent);
	}
	
	/**
	 * 调用图片浏览器
	 * @param path
	 * @param fileList
	 */
	private void callImageViewer(String path, ElementList fileList){
		ArrayList<String> pathList = new ArrayList<String>();
		Intent intent = new Intent();
		int    size = 0;
		
		if(fileList == null || fileList.size() == 0){
			return;
		}
		size = fileList.size();
		for(int i = 0; i < size; i++){
			pathList.add(fileList.get(i).getName());
		}
		intent.setClass(WiGalleryActivity.this, com.wistron.WiViewer.WiImageViewerActivity.class);
		intent.putExtra("cmd", "play");
		intent.putExtra("filePath", path);
		intent.putStringArrayListExtra("fileList", pathList);
		
		startActivity(intent);
	}
	
	/**
	 * 调用视频浏览器
	 * @param path
	 */
	private void callVideoViewer(String path, ElementList fileList){
		ArrayList<String> pathList = new ArrayList<String>();
		Intent intent = new Intent();
		int    size = 0;
		
		if(fileList == null || fileList.size() == 0){
			return;
		}
		size = fileList.size();
		for(int i = 0; i < size; i++){
			if(FileTypeHelper.isVideoFile(fileList.get(i).getName())){
				pathList.add(fileList.get(i).getName());
			}
		}
		intent.setClass(WiGalleryActivity.this, com.wistron.WiViewer.WiVideoViewerActivity.class);
		intent.putExtra("cmd", "play");
		intent.putExtra("filePath", path);
		intent.putStringArrayListExtra("fileList", pathList);
		startActivity(intent);
	}
	
	/**
	 * 调用图片编辑器
	 * @param path
	 */
	private void callImageEditor(String path){
		Intent intent = new Intent();
		intent.setClass(WiGalleryActivity.this, com.wistron.WiEditor.WiImageEditorActivity.class);
		intent.putExtra("filePath", path);
		startActivity(intent);
	}

	/**
	 * 调用全景浏览器
	 * @param path
	 */
	private void callPanoramaViewer(String path){
		Intent intent = new Intent();
	
		intent.setClass(WiGalleryActivity.this, com.wistron.WiViewer.Panorama360Activity.class);
		intent.putExtra("cmd", "pano");
		intent.putExtra("filePath", path);
		startActivity(intent);
	}
	/**
	 * 文件移动操作
	 * 内部存储器/外部存储器
	 */
	private void callMoveTo(int destination){
		int          fileListSize    = 0;
		String       strPlural       = getResources().getString(R.string.gallery_string_plural);
		String       strDestination  = getResources().getString(R.string.gallery_string_internal);
		List<String> fileList        = getSelectedFiles();
		
		if(fileList == null || fileList.size() == 0){
			return;
		}
		
		//保存多选操作临时数据
		if(mMultiTempData == null){
			mMultiTempData = new MultiTempData();
		}
		mMultiTempData.clear();
		mMultiTempData.setIsMoveToInternal(true);
		
		if(fileList != null){//如果为空，文件数就直接为0
			fileListSize = fileList.size();
		}
		
		if(fileListSize < 2){//判断单数，默认为复数
			strPlural = "";
		}
		if(destination == MOVE_TO_EXTERNAL_DIR){//判断是否为外部空间，默认为内部
			strDestination = getResources().getString(R.string.gallery_string_external);
			mMultiTempData.setIsMoveToExternal(true);
		}
		
		mMultiTempData.setSelectedFileNum(fileListSize);
		
		if(fileListSize == 1){
			String fileName = FileTypeHelper.getFileNameFromPath(fileList.get(fileListSize - 1));
			mMsgBoxMoveConfirm.setMessage(String.format(getResources().getString(
					R.string.gallery_msgbox_move_to_confirm_single), "\"" + fileName + "\"", strDestination));
		}else{
			mMsgBoxMoveConfirm.setMessage(String.format(getResources().getString(
					R.string.gallery_msgbox_move_to_confirm), fileListSize, strPlural, strDestination));
		}
		mNoNeedToUnlock = true;
		mMsgBoxMoveConfirm.show();
		mMultiTempData.setSelectedFileList(fileList);//使其能全局访问，以便execMoveTo能够访问到这个数据
	}
	
	private void execMoveTo(){
		int          destination     = MOVE_TO_INTERNAL_DIR;
		List<String> fileList        = null;
		
		if(mMultiTempData != null && mMultiTempData.isMoveTo()){//得到原始的全局文件列表
			fileList = mMultiTempData.getSelectedFileList();
			if(mMultiTempData.isMoveToExternal()){
				destination = MOVE_TO_EXTERNAL_DIR;
			}
		}
		
		switch (destination) {
		case MOVE_TO_INTERNAL_DIR:
			if(WiGalleryOpenGLRenderer.mAsyncFileProvider != null && fileList.size() > 0){
				mNoNeedToUnlock = true;
				lockScreen(true); //执行移动前锁屏
				WiGalleryOpenGLRenderer.mAsyncFileProvider.moveTo(fileList, CSStaticData.TMP_INT_DIR, false, WiGalleryActivityID);
			}
			break;

		case MOVE_TO_EXTERNAL_DIR:
			if(WiGalleryOpenGLRenderer.mAsyncFileProvider != null && fileList.size() > 0){
				mNoNeedToUnlock = true;
				lockScreen(true); //执行移动前锁屏
				WiGalleryOpenGLRenderer.mAsyncFileProvider.moveTo(fileList, CSStaticData.TMP_EXT_DIR, false, WiGalleryActivityID);
			}
			break;
		}
	}

	/**
	 * 分享至操作
	 */
	private void callShareTo(){
		List<String> fileList = getSelectedFiles();//获取选中的文件

		if(mShareMenu != null){
			int size = fileList.size();
			for(int i = 0; i < size; i++){
				fileList.set(i, "file://" + fileList.get(i));
			}
			mShareMenu.sendShareList(fileList);//发送出去
		}
	}
	
	/**
	 * 排序操作
	 * 日期升序/日期降序
	 */
	@SuppressWarnings({ "static-access", "unused" }) //for m_element_group, has used
	private void callSortOrder(boolean sortOrder){
		if(mGlsurfaceView.getRender() != null && mGlsurfaceView.getRender().m_element_group != null){
			mGlsurfaceView.getRender().m_element_group.setOrder(sortOrder);
		}
	}
	
	/**
	 * 分组操作
	 * NONE/时间/地理
	 */
	@SuppressWarnings("static-access") //for m_element_group
	private void callGroupingSet(CSStaticData.LIST_TYPE type){
		if(mGlsurfaceView != null && mGlsurfaceView.getRender() != null && mGlsurfaceView.getRender().m_element_group != null && !mGlsurfaceView.getRender().m_element_group.isMoving()){
			mGlsurfaceView.getRender().m_element_group.setType(type, true);
		}
	}

	/**
	 * 喜好设定
	 * 选中的文件的喜好设定若与传入参数相同，则不会对其进行操作
	 * @param isFavorite true = 设定为喜好，false = 取消喜好
	 */
	@SuppressWarnings("static-access") //for m_element_group
	private void callSetFavorite(boolean isFavorite){		
		int    setFavoriteNum    = 0;
		int    removeFavoriteNum = 0;
		String strPlural         = getResources().getString(R.string.gallery_string_plural);
		String strHasOrHave      = getResources().getString(R.string.gallery_string_have);
		String lastFileName      = "";
		
		//刷新界面
		if(mGlsurfaceView.getRender() != null)
		{
			ElementList list = mGlsurfaceView.getRender().getCurrentDisplayedList();
			List<String> selectedList = new ArrayList<String>();
			if (list != null)
			{
				int n = list.size();
				for(int i = 0; i < n; i++)
				{
					Element elem = list.get(i);
					if(elem.isChoosed())
					{
						elem.setFavorite(isFavorite);
						selectedList.add(elem.getName());
						lastFileName = elem.getName();
					}
				}
			}
			else 
			{
				int n = mGlsurfaceView.getRender().m_element_group.size();
				for (int i = 0; i < n; i++)
				{
					ElementList elist = mGlsurfaceView.getRender().m_element_group.get(i);
					if (elist != null && elist.isChoosed())
					{
						for (int j = 0; j < elist.size(); j++)
						{
							Element elem = elist.get(j);
							elem.setFavorite(isFavorite);
							selectedList.add(elem.getName());
							lastFileName = elem.getName();
						}
					}
				}
			}
			
			//准备进度提示框的内容
			if(isFavorite){
				setFavoriteNum = selectedList.size();
				
				if(setFavoriteNum < 2){
					strPlural = "";
				}
				
				if(setFavoriteNum != 0){
					mMsgBoxSetFavoriteProgress.setMessage(
							String.format(getResources().getString(
									R.string.gallery_msgbox_set_as_favorite_progress), setFavoriteNum, strPlural));
					mMsgBoxSetFavoriteProgress.show();
				}
				if(WiGalleryOpenGLRenderer.mAsyncFileProvider == null){
					WiGalleryOpenGLRenderer.mAsyncFileProvider = new AsyncFileProvider(WiGalleryActivity.this);
				}
				
				mNoNeedToUnlock = true;
				lockScreen(true); //执行喜好前锁屏
				WiGalleryOpenGLRenderer.mAsyncFileProvider.setFavoriteToDB(selectedList, WiGalleryActivityID);
				
			}else{
				removeFavoriteNum = selectedList.size();
				
				if(removeFavoriteNum == 0){
					return;
				}
				
				if(removeFavoriteNum < 2){
					strPlural = "";
				}
				
				if(removeFavoriteNum != 0){
					mMsgBoxRemoveFavoriteProgress.setMessage(
							String.format(getResources().getString(
									R.string.gallery_msgbox_remove_from_favorite_progress)));
					mMsgBoxRemoveFavoriteProgress.show();
				}
				
				if(WiGalleryOpenGLRenderer.mAsyncFileProvider == null){
					WiGalleryOpenGLRenderer.mAsyncFileProvider = new AsyncFileProvider(WiGalleryActivity.this);
				}
				
				mNoNeedToUnlock = true;
				lockScreen(true); //执行喜好前锁屏
				WiGalleryOpenGLRenderer.mAsyncFileProvider.removeFavoriteFromDB(selectedList, WiGalleryActivityID);
				
			}
		}
		
		//刷新喜好计数器
		mUIHandler.sendEmptyMessage(HANDLE_REFLASH_FAVORITE_COUNTER);

		//准备完成提示框的内容
		if(isFavorite){
			if(setFavoriteNum < 2){
				strPlural = "";
				strHasOrHave = getResources().getString(R.string.gallery_string_has);
			}

			if(setFavoriteNum == 1){
				mMsgBoxSetFavoriteTipbox.setMessage(
						String.format(getResources().getString(
								R.string.gallery_msgbox_set_as_favorite_single), "\"" + FileTypeHelper.getFileNameFromPath(lastFileName) + "\"", strHasOrHave));
			}else{
				mMsgBoxSetFavoriteTipbox.setMessage(
						String.format(getResources().getString(
								R.string.gallery_msgbox_set_as_favorite), setFavoriteNum, strPlural, strHasOrHave));
			}
		}else{
			if(removeFavoriteNum < 2){
				strHasOrHave = getResources().getString(R.string.gallery_string_has);
			}else{
				strHasOrHave = getResources().getString(R.string.gallery_string_have);
			}
			mMsgBoxRemoveFavoriteTipbox.setMessage(
					String.format(getResources().getString(
							R.string.gallery_msgbox_remove_from_favorite), strHasOrHave));
		}

	}

	/**
	 * 幻灯片展示操作
	 */
	private void callSlideShow(ElementList fileList){
		ArrayList<String> pathList = new ArrayList<String>();
		Intent intent = new Intent();
		int    size = 0;
		
		if(fileList == null || fileList.size() == 0){
			return;
		}
		size = fileList.size();
		for(int i = 0; i < size; i++){
			pathList.add(fileList.get(i).getName());
		}
		intent.setClass(WiGalleryActivity.this, com.wistron.WiViewer.WiImageViewerActivity.class);
		intent.putExtra("cmd", "slideshow");
		intent.putStringArrayListExtra("fileList", pathList);
		startActivity(intent);
	}
	
	/**
	 * 2D/3D过滤操作
	 * 2D/3D/All files
	 */
	@SuppressWarnings("static-access") //for m_element_group
	private void callFileFilter(int fileFilter){
		if(mGlsurfaceView != null && mGlsurfaceView.getRender() != null && mGlsurfaceView.getRender().m_element_group != null && !mGlsurfaceView.getRender().m_element_group.isMoving()){
			switch (fileFilter) {
			case VIEW_FILTER_ALL:
				mGlsurfaceView.getRender().m_element_group.setElementType(CSStaticData.LIST_ELEMENT_TYPE.LIST_ELEMENT_ALL);
				break;

			case VIEW_FILTER_2D:
				mGlsurfaceView.getRender().m_element_group.setElementType(CSStaticData.LIST_ELEMENT_TYPE.LIST_ELEMENT_2D);
				break;
				
			case VIEW_FILTER_3D:
				mGlsurfaceView.getRender().m_element_group.setElementType(CSStaticData.LIST_ELEMENT_TYPE.LIST_ELEMENT_3D);
				break;
			}
			
		}
	}
	
	/**
	 * 文件详细信息
	 */
	private void callFileDetail(String filePath){
		//TODO 调用外部接口
	}
	
	private void callWIFIChecking(){
		if(mMsgBoxWIFITipbox != null){
			mMsgBoxWIFITipbox.show();
		}
		if(WiGalleryOpenGLRenderer.mAsyncFileProvider != null){
			WiGalleryOpenGLRenderer.mAsyncFileProvider.checkWifiConnection();
		}
	}
	
	private void callDelete() {
		List<String> fileList = getSelectedFiles();

		if(fileList == null || fileList.size() == 0){
			return;
		}
		
		//保存多选操作临时数据
		if(mMultiTempData == null){
			mMultiTempData = new MultiTempData();
		}
		mMultiTempData.clear();
		mMultiTempData.setIsDelete(true);
		mMultiTempData.setSelectedFileList(fileList);
		
		mNoNeedToUnlock = true;
		mMsgBoxDeleteConfirm.show();
	}
	
	@SuppressWarnings("static-access") //for mAsyncFileProvider
	private void execDelete() {
		List<String> delList = null;
		if(mGlsurfaceView.getRender() != null && mGlsurfaceView.getRender().mAsyncFileProvider != null){
			if(mMultiTempData != null && mMultiTempData.isDelete()){
				delList = mMultiTempData.getSelectedFileList();
			}else{
				return;
			}
			
			mNoNeedToUnlock = true;
			lockScreen(true); //执行删除前锁屏
			mGlsurfaceView.getRender().mAsyncFileProvider.deteleFiles(delList, false, false, WiGalleryActivityID);
		}
	}
	
	/**
	 * 关闭整个菜单
	 */
	private void callCloseMenu(){
		//关闭子菜单
		if(mShareMenu != null && mShareMenu.getVisibility() == View.VISIBLE){
			mShareMenu.setVisibility(View.INVISIBLE);
			mIsSubMoreMenuShow = false;
			mMoreMenuCurClickPosition = -1;
			mGalleryListAdapter.notifyDataSetChanged();
		}
		if(mIsSubMoreMenuShow){
			mSubMenuLayout.setVisibility(View.INVISIBLE);
			mIsSubMoreMenuShow = false;
			mMoreMenuCurClickPosition = -1;
			mGalleryListAdapter.notifyDataSetChanged();
		}


		//关闭菜单
		if(mIsMoreMenuShow){
			mGalleryMoreListView.setSelection(0);
			mMenuLayout.setVisibility(View.INVISIBLE);
			recoverMoreMenuUIStyle();
			mBtnMore.setChecked(false);
			mIsMoreMenuShow = false;
		}
	}
	
	private void callLowMemoryLawman(){
//		dalvik.system.VMRuntime.getRuntime().setMinimumHeapSize(CSStaticData.CWJ_HEAP_SIZE);
//		dalvik.system.VMRuntime.getRuntime().setTargetHeapUtilization(CSStaticData.CWJ_RAM_UTILIZATION);
		Log.e(TAG, "[callLowMemoryLawman]内存不足！");
	}
	
	private void callMultiCancel() {}

	private void callMultiExecute() {}
	
	/**
	 * Back键按下的动作
	 */
	@SuppressWarnings("static-access")
	private boolean onBack(){
		
		//关闭移动确认框
		if(mMsgBoxMoveConfirm != null && mMsgBoxMoveConfirm.isShown()){
			mMsgBoxMoveConfirm.hide();
			mNoNeedToUnlock = false;
			lockScreen(false);
			return true; //直接return，否则触发关闭程序事件
		}
		
		//关闭删除确认框
		if(mMsgBoxDeleteConfirm != null && mMsgBoxDeleteConfirm.isShown()){
			mMsgBoxDeleteConfirm.hide();
			mNoNeedToUnlock = false;
			lockScreen(false);
			return true; //直接return，否则触发关闭程序事件
		}
		
		//关闭子菜单
		if(mShareMenu != null && mShareMenu.getVisibility() == View.VISIBLE){
			mShareMenu.setVisibility(View.INVISIBLE);
			mIsSubMoreMenuShow = false;
			mMoreMenuCurClickPosition = -1;
			mGalleryListAdapter.notifyDataSetChanged();
			return true; //直接return，否则触发关闭程序事件
		}
		if(mIsSubMoreMenuShow){
			mSubMenuLayout.setVisibility(View.INVISIBLE);
			mIsSubMoreMenuShow = false;
			mMoreMenuCurClickPosition = -1;
			mGalleryListAdapter.notifyDataSetChanged();
			return true; //直接return，否则触发关闭程序事件
		}
		
		
		//关闭菜单
		if(mIsMoreMenuShow){
			mGalleryMoreListView.setSelection(0);
			mMenuLayout.setVisibility(View.INVISIBLE);
			recoverMoreMenuUIStyle();
			mBtnMore.setChecked(false);
			mIsMoreMenuShow = false;
			return true; //直接return，否则触发关闭程序事件
		}
		
		//取消多选模式
		if(mIsMulitSelectionMode){
			if(WiGalleryOpenGLRenderer.m_element_group != null){
				mGlsurfaceView.getRender().setMediaMetaType(MEDIA_META_TYPE.ALL_MEDIA_TYPE);
				WiGalleryOpenGLRenderer.m_element_group.setChoosedMode(ELEM_STATUS.NORMAL_STATUS);
				callMultiCancel();
			}
			mCurMultiStatus = MULTI_NONE_ID;
			mHasOperatedMenu = false;
			mNoNeedToUnlock = false;
			return true; //直接return，否则触发关闭程序事件
		}
		
		//返回上级目录
		if(mGlsurfaceView != null && mGlsurfaceView.getRender() != null && mGlsurfaceView.getRender().m_element_group != null && mGlsurfaceView.getRender().m_element_group.bEnterGroup()){
			mGlsurfaceView.getRender().m_element_group.closeGroup();
			return true; //直接return，否则触发关闭程序事件
		}
		
		//终止程序
		finish();
		mBtnDimension.setCheckedWithCallback(false);
		disposeActivityData();
		
		return true;
	}
	
	

	/****************************************************************/
	/*                           辅助工具                                                                         */
	/****************************************************************/
	
	/**
	 * 获取选中元素的个数
	 * @return
	 */
	private int getSelectedItemsSize(){
		int selectedItemsNum = 0;
		if(mGlsurfaceView.getRender() != null){
			if (mGlsurfaceView.getRender().getCurrentDisplayedList() != null){//文件模式
				selectedItemsNum = mGlsurfaceView.getRender().getCurrentDisplayedList().getSelectedNum();
			}
			else {//文件夹模式
				int groupSize = mGlsurfaceView.getRender().m_element_group.size();
				for (int i = 0; i < groupSize; i++){
					ElementList list = mGlsurfaceView.getRender().m_element_group.get(i);
					if (list != null && list.isChoosed()){
						selectedItemsNum ++;
					}
				}
			}
		}
		
		return selectedItemsNum;
	}
	
	/**
	 * 获取当前场景中已被选择的文件
	 * @return
	 */
	@SuppressWarnings("static-access") //for m_element_group
	private List<String> getSelectedFiles(){
		int selectedSize = 0;
		List<String> fileList = new ArrayList<String>();
		
		if(mGlsurfaceView.getRender() != null){
			if (mGlsurfaceView.getRender().getCurrentDisplayedList() != null)
			{
				selectedSize = mGlsurfaceView.getRender().getCurrentDisplayedList().size();
				for(int i = 0; i < selectedSize; i++){
					if(mGlsurfaceView.getRender().getCurrentDisplayedList().get(i).isChoosed()){
						fileList.add(mGlsurfaceView.getRender().getCurrentDisplayedList().get(i).getName());
					}
				}
			}
			else 
			{
				selectedSize = mGlsurfaceView.getRender().m_element_group.size();
				for (int i = 0; i < selectedSize; i++)
				{
					ElementList list = mGlsurfaceView.getRender().m_element_group.get(i);
					if (list != null && list.isChoosed())
					{
						for (int j = 0; j < list.size(); j++)
						{
							fileList.add(list.get(j).getName());
						}
					}
				}
			}
		}
		
		return fileList;
	}
	
	@SuppressWarnings("static-access") //for m_element_group
	private List<Element> getSelectedElements(){
		int selectedSize = 0;
		List<Element> elementsList = new ArrayList<Element>();
		
		if(mGlsurfaceView.getRender() != null){
			if (mGlsurfaceView.getRender().getCurrentDisplayedList() != null)
			{
				selectedSize = mGlsurfaceView.getRender().getCurrentDisplayedList().size();
				for(int i = 0; i < selectedSize; i++){
					if(mGlsurfaceView.getRender().getCurrentDisplayedList().get(i).isChoosed()){
						elementsList.add(mGlsurfaceView.getRender().getCurrentDisplayedList().get(i));
					}
				}
			}
			else 
			{
				if (mGlsurfaceView.getRender().m_element_group == null)
				{
					return elementsList;
				}
				
				selectedSize = mGlsurfaceView.getRender().m_element_group.size();
				for (int i = 0; i < selectedSize; i++)
				{
					ElementList list = mGlsurfaceView.getRender().m_element_group.get(i);
					if (list != null && list.isChoosed())
					{
						for (int j = 0; j < list.size(); j++)
						{
							elementsList.add(list.get(j));
						}
					}
				}
			}
		}
		
		return elementsList;
	}
	
	/**
	 * 切换More Menu的Item显示模式
	 * @param isUnderMultiMode 
	 * 			false = 正常显示
	 * 			true  = 不显示Group by, Sort Order, Content Switch
	 */
	@SuppressWarnings("static-access") //for m_element_group
	public void switchMoreMenuItem(boolean isUnderMultiMode){
		
		//设置菜单
		if(mGlsurfaceView.getRender().m_element_group != null
				&&
				mGlsurfaceView.getRender().m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_NONE){
			mGalleryMoreMenuResId = CSStaticData.gGroupNoneResid;
			if(isUnderMultiMode){
				mGalleryMoreMenuResId = CSStaticData.gGroupNoneMultiResid;
			}
		}
		if(mGlsurfaceView.getRender().m_element_group != null
				&&
				mGlsurfaceView.getRender().m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_DATE){
			mGalleryMoreMenuResId = CSStaticData.gGroupDateResid;
			if(isUnderMultiMode){
				mGalleryMoreMenuResId = CSStaticData.gGroupDateMultiResid;
			}
			if(mGlsurfaceView.getRender().m_element_group.bEnterGroup()){
				mGalleryMoreMenuResId = CSStaticData.gGroupDateSubResid;
				if(isUnderMultiMode){
					mGalleryMoreMenuResId = CSStaticData.gGroupDateSubMultiResid;
				}
			}
		}
		if(mGlsurfaceView.getRender().m_element_group != null
				&&
				(mGlsurfaceView.getRender().m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_LOCATION_1
					||
				 mGlsurfaceView.getRender().m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_LOCATION_2
				 	||
				 mGlsurfaceView.getRender().m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_LOCATION_3
				 	||
				 mGlsurfaceView.getRender().m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_LOCATION_4)
				 ){
			mGalleryMoreMenuResId = CSStaticData.gGroupLocationResid;
			if(isUnderMultiMode){
				mGalleryMoreMenuResId = CSStaticData.gGroupLocationMultiResid;
			}
			if(mGlsurfaceView.getRender().m_element_group.bEnterGroup()){
				mGalleryMoreMenuResId = CSStaticData.gGroupLocationSubResid;
				if(isUnderMultiMode){
					mGalleryMoreMenuResId = CSStaticData.gGroupLocationSubMultiResid;
				}
			}
		}
		
		//重刷菜单
		if(mGalleryListAdapter != null){
			mGalleryListAdapter  = new GalleryListViewAdapter(this,mGalleryMoreMenuResId);
			mGalleryListAdapter.setMenuType(GalleryListViewAdapter.MENU_TYPE_MAIN_MENU);
			mGalleryMoreListView.setAdapter(mGalleryListAdapter);
		}
	}
	
	/**
	 * 恢复菜单样式
	 * 把More的主菜单的Adapter恢复初始
	 */
	public void recoverMoreMenuUIStyle(){
		mMoreMenuCurClickPosition = -1;//清除菜单选中项
		mMoreMenuDisableClickPosition = -1;//<==============================??
		mGalleryMoreListView.setAdapter(mGalleryListAdapter);//重刷菜单
	}

	@Override
	public void setScrollBar(int currentRow, int rows) {
		Message msg = new Message();
		msg.what = HANDLE_SCROLL_PAGE_INTERFACE;
		msg.arg1 = currentRow;
		msg.arg2 = rows;
		mUIHandler.sendMessage(msg);
	}

	public void setScreenDimension(boolean is3D){
		mDimension = is3D;
		String[] cmdTurnOn3D = { //开启屏幕3D命名
				"/system/bin/sh", "-c", 
				"echo 1 > /sys/devices/platform/mipi_masterimage.513/enable_3d_barrier"
		};

		String[] cmdTurnOff3D = { //关闭屏幕3D命令
				"/system/bin/sh", "-c", 
				"echo 0 > /sys/devices/platform/mipi_masterimage.513/enable_3d_barrier"
		};

		try {
			if(mDimension){
				if(CSStaticData.DEBUG){
					Log.w(TAG, "[setScreenDimension]开启屏幕3D显示模式");
	}
				Runtime.getRuntime().exec(cmdTurnOn3D);
			}else{
				if(CSStaticData.DEBUG){
					Log.w(TAG, "[setScreenDimension]关闭屏幕3D显示模式");
				}
				Runtime.getRuntime().exec(cmdTurnOff3D);
			}
		} catch (IOException exp) {
			if(CSStaticData.DEBUG){
				Log.w(TAG, "[setScreenDimension]屏幕3D显示模式切换：失败，命令行输入流异常");
			}
		} catch (SecurityException  exp) {
			if(CSStaticData.DEBUG){
				Log.w(TAG, "[setScreenDimension]屏幕3D显示模式切换：失败，系统安全保护阻止本操作");
			}
		} catch (Exception exp) {
			if(CSStaticData.DEBUG){
				Log.w(TAG, "[setScreenDimension]屏幕3D显示模式切换：失败，未知错误");
			}
		}
	}
	
	/**
	 * 锁定屏幕
	 * @param isLock true =  锁定， false = 解锁
	 */
	public void lockScreen(boolean isLock){
		
		if(mHasLockedScreen == isLock){
			return;
		}
		mHasLockedScreen = isLock;
		if(isLock){
			if(CSStaticData.DEBUG){
				Log.w(TAG, "[lockScreen]屏幕锁定");
			}
//			mCurrentScreenState = getRequestedOrientation(); //记录屏幕当前状态
//			if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {//当前为横屏
//				if(mCurrentOrenState == 0 || mCurrentOrenState == 360){
//					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//				}else if(mCurrentOrenState == 180){
//					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
//				}else{
//					if(CSStaticData.DEBUG){
//						Log.w(TAG, "[lockScreen]屏幕加锁失败");
//					}
//				}
//			}
//			else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {//当前为竖屏
//				if(mCurrentOrenState == 90){
//					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//				}else if(mCurrentOrenState == 270){
//					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
//				}else{
//					if(CSStaticData.DEBUG){
//						Log.w(TAG, "[lockScreen]屏幕加锁失败");
//					}
//				}
//			}
			{//Cocoonshu 20120626
				switch (mCurrentOrenState) {
				case 0:
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
					break;

				case 270:
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
					break;
					
				case 180:
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
					break;
					
				case 90:
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
					break;
					
				default:
					break;
				}
			}
		}else{
			if(CSStaticData.DEBUG){
				Log.w(TAG, "[lockScreen]屏幕解锁");
			}
			{//Cocoonshu 20120626
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
			}
		}

	}
	
	/**
	 * 回收程序参数
	 */
	private void disposeActivityData() {
		mMoreMenuCurClickPosition       = -1;    //记录当前主菜单的选中项
		mMoreMenuDisableClickPosition   = -1;    //记录当前主菜单的选中项不可用的选项
		mGroupMenuCurClickPosition      =  0;    //记录当前分组菜单的已选项，用于设定为不可用
		mOrderMenuCurClickPosition      =  0;    //记录当前排序菜单的已选项，用于设定为不可用
		mFilterMenuCurClickPosition     =  0;    //记录当前过滤菜单的已选项，用于设定为不可用
		mMultiTempData                  = null;  //记录多选操作的临时数据
		CSStaticData.LOAD_COMPLETED     = false; //初始数据是否加装完毕
		CSStaticData.g_is_3D_mode       = false; //3D模式
		CSStaticData.g_sort_order_mode  = false; //排序模式：true = 升序  false = 降序
		CSStaticData.gStatusInfo        = null;
		mGalleryMoreMenuResId           = CSStaticData.gGroupNoneResid;
		
		//重刷菜单
		if(mGalleryListAdapter != null){
			mGalleryListAdapter  = new GalleryListViewAdapter(this,mGalleryMoreMenuResId);
			mGalleryListAdapter.setMenuType(GalleryListViewAdapter.MENU_TYPE_MAIN_MENU);
			mGalleryMoreListView.setAdapter(mGalleryListAdapter);
		}
	}

}


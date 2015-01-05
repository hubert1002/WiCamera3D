package Utilities;

import com.wistron.WiGallery.StatusInfo;
import com.wistron.WiGallery.WiGalleryOpenGLRenderer;
import com.wistron.swpc.wicamera3dii.R;

import android.R.integer;
import android.opengl.GLU;
import android.os.Environment;

/**
 * Copyright (c) 2012 Wistron SWPC
 * All rights reserved.
 * @author Cocoonshu
 * @date 2012-02-20 12:00:00
 * @comment 
 * @purpose Record basic configuration
 * @detail 
 */
public class CSStaticData {
	
	public static final String  APP_TAG              = "WiCamera3DII";  //全局调试TAG
	public static final String  TMP_SUF              = ".tmp";          //全局临时文件后缀
	public static final String  TMP_DIR              = Environment.getExternalStorageDirectory().getAbsolutePath().toString() 
                                                       + "/"; //APP_HOME目录 &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
	public static final String  TMP_EXT_DIR          = Environment.getExternalStorageDirectory().getAbsolutePath().toString() 
			                                           + "/3DPhoto/"; //APP_HOME外部目录
	public static final String  TMP_INT_DIR          = Environment.getExternalStorageDirectory().getAbsolutePath().toString() 
                                                       + "/DCIM/"; //APP_HOME内部目录
	public static final String[]MEDIA_SCAN_DIR       = new String[]{Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/DCIM/",
																	Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/3DPhotoes/"};
	public static final boolean DEBUG                = false;            //是否为DEBUG状态
	public static final boolean DEMO                 = true;            //是否为DEMO状态
	public static final boolean AUTO_FLUSH_DATABASE  = true;            //是否自动刷新数据库
	public static       boolean EARTH_AUTO_ROTATE    = false;           //地球是否自动旋转
	public static final int     THUMB_SIZE           = 256;             //缩略图的默认大小
	public static       boolean LOAD_STARTED         = false;           //初始数据是否开始加装
	public static       boolean LOAD_COMPLETED       = false;           //初始数据是否加装完毕
	public static final int     GEO_REQUEST_SLEEP    = 1180;            //GoogleAPI 请求等待，值太小时GoogleAPI可能会返回OVER_QUERY_LIMIT
	public static final String  GEO_KEY              = "0vIOHclF7BNpPYikMTpJ9lY3HhySTlnARGcS_1w";
	public static final String  RENAME_SUFFIX        = "_copy";         //重命名文件时，添加的后缀
	public static final String  DBNAME_GEOINFO       = "geoCache";      //GEO信息数据库名
	public static final String  DBNAME_FAVORITE      = "favoriteCache"; //喜好数据库名
	public static final int     CWJ_HEAP_SIZE        = 40*1024*1024;    //最小堆用量
	public static final float   CWJ_RAM_UTILIZATION  = 0.78f;           //堆执行效率
	public static       boolean THUMB_FROM_MEDIA_DB  = false;           //是否从媒体数据库获取缩图
	public static       boolean ENABLE_THUMB_ROTATE  = true;            //启动缩图旋转
	
	public static StatusInfo gStatusInfo                    = null;     //状态保存
	public static boolean g_is_3D_mode                      = false;    //3D模式
	public static int     g_screen_width                    = 800;      //屏幕宽度
	public static int     g_screen_height                   = 480;      //屏幕高度
	public static int     g_long_press_timeout              = 450;      //长按超时
	public static float   g_fling_velocity                  = 1000;     //滑动速度
	public static int     g_load_limit                      = 50;       //加载缩略图数量限制
	public static int     g_thumbnailSize                   = 128;      //贴图缩略图大小
	public static boolean g_gallery_runing                  = false;    //标识WiGallery3DActivity是否在运行
	public static int     g_label_length                    = 10;       //专辑左下角文本，请赋偶数
	public static boolean g_flag_load_completed             = false;    //文件加载已完成
	public static int     g_fling_speed_threshold           = 2700;     //触发滑动事件的速度阀值
	public static boolean g_is_surface_alive                = false;    //GLSurface是否存在
	public static float   g_debug_varient                   = 0.155f;   //调试变量
	public static boolean g_sort_order_mode                 = false;    //排序模式：true = 升序  false = 降序
	public static boolean g_surface_changed_called          = false;
	public static boolean g_surface_invalidate              = false;
	
	// 800*480分辨率对应的3D窗口宽高（手动调试）
	public static float screen_3d_width_h = 4.5f;
	public static float screen_3d_height_h = 2.6f;
	// 480*800分辨率对应的3D窗口宽高（手动调试）
	public static float screen_3d_width_v = 1.58f;
	public static float screen_3d_height_v = 2.6f;
	
	public static enum SORT_TYPE
	{
		SORT_ASCENDING,
		SORT_DESCENGING
	}
	
	public static enum SHARE_MULTI_SELECT_TYPE
	{
		NONE_LIMITE,
		SINGLE_SELECTION,
		MULTI_SELECTION
	}
	
	public static enum LIST_ELEMENT_TYPE
	{
		LIST_ELEMENT_ALL,
		LIST_ELEMENT_3D,
		LIST_ELEMENT_2D
	}
	
	public static enum ALIGN_TYPE
	{
		ALIGN_LEFT,
		ALIGN_CENTRE,
		ALIGN_RIGHT
	}
	
	
	public static enum ANIMATION_TYPE
	{
		NONE,
		RIGHT_SHIFT,
		LEFT_SHIFT,
		GENTLE_SHIFT,
		NONEGROUP_OPENING,
		NONEGROUP_CLOSING,
		DATE_OPENING,
		DATE_CLOSING,
		DATE_AFTERCLICK,
		DATE_AFTERCLICK_INVERISON,
		LOCATION_OPENING,
		LOCATION_AFERCLICK,
		LOCATION_AFERCLICK_INVERISON
	}
	
	public static enum LIST_TYPE
	{
		LIST_NONE,
		LIST_DATE,
		
		LIST_LOCATION_1, //国家
		LIST_LOCATION_2, // 省
		LIST_LOCATION_3, // 市
		LIST_LOCATION_4,  // 区，街道
		
		LIST_INVALID
	}
	
	public static enum MEDIA_TYPE
	{
		NORMAL_IMAGE,
		STOERE_IMAGE,
		NORMAL_VIDEO,
		STOERE_VIDEO
	}
	
	public static enum MEDIA_META_TYPE
	{
		ALL_MEDIA_TYPE,
		VIDEO_MEDIA_TYPE,
		IMAGE_MEDIA_TYPE,
		SET_FAVORITE_TYPE,
		REMOVE_FAVORITE_TYPE,
		NO_FILTER
	}
	
	public static enum FAVORITE_TYPE
	{
		FAVORITE_FILE,
		NONFAVORITE_FILE
	}
	
	public static enum STORAGE_TYPE
	{
		NONE,
		INTERNAL_DIR,
		EXTERNAL_DIR
	}
	
	public static int           choosedTextureID    = 0;               //选中图标贴图ID
	public static int           notChoosedTextureID = 0;               //待选图标贴图ID
	public static int           defaultTextureID    = 0;               //默认图标贴图ID
	public static int           INVALID_TEXTURE_ID  = 0;               //无效贴图ID
	
	public static final String[] SUPPORT_SUF        = {                //支持的文件后缀
		".png",  //0
		".jpg",  //1
		".jpe",  //2
		".jpeg", //3
		".jps",  //4
		".png",  //5
		".mpo",  //6
		".gif",  //7
		".bmp",  //8
		".wbmp", //9
		".3gp",  //10
		".3g2",  //11
		".mp4",  //12
		".ts",   //13
		".webm", //14
		".m4v",  //15
		".divx", //16
		".wmv",  //17
		".asf",  //18
		".xvid", //19
		".mkv",  //20
		".avi",  //21
		".srt",  //22
		".smi",  //23
	};
	
	public static float[] eye_position_none = {0,0,5};
	public static float[] eye_center_none = {0, 0, 0};
	
	public static float[] eye_position_date = {0,0,3};
	public static float[] eye_center_date = {0, -1.5f, 0};
	
	public static float[] eye_position_location = {0,0,3};
	public static float[] eye_center_location = {0, 0, 0};
	
	public static int   group_none_row_num = 2;
	
	// group_none模式元素的位置设置
	public static float x_list_pos_offset = 0.8f;
	public static float y_list_pos_offset = 1.2f;
	public static float z_list_pos_offset = 2.0f;
	
	
	public static float x_list_pos_start = -1.5f;
	public static float y_list_pos_start = -0.6f;
	public static float z_list_pos_start = 0;
	
	public static float screen_2d_width = 480;
	public static float screen_2d_height = 800;
	
	
	
	//x0, y0, z0为屏幕坐标
	//z1, y1, z1为物体中心点坐标
	public static boolean isInRect(float[] eye_position, float[] eye_center, float x0, float y0, float z0, float x1, float y1, float z1, float r)
	{
		float xn, yn, zn;
		float[] ep = {0,0,0};
		
		xn = x1;
		yn = y1;
		zn = z1;
		
		float m = (float) Math.sqrt(eye_position[2] * eye_position[2] + eye_center[1] * eye_center[1]);
		float cosa = Math.abs(eye_position[2] / m);
		float sina = Math.abs(eye_center[1] / m);
		
		float yo = eye_position[1] - eye_position[2] * sina;
		float zo = eye_position[2] * (1-cosa);
		

		float _zn = z1 - zo;
		float _yn = y1 - yo;
		
		zn = _zn*cosa + _yn*sina;
		yn = _yn*cosa - _zn*sina;
		
		
		ep[0] = eye_position[0];
		float ep1 = eye_position[1] - yo;
		float ep2 = eye_position[2] - zo;
		
		ep[2] = ep2*cosa + ep1*sina;
		ep[1] = ep1*cosa - ep2*sina;

		
		float R = r * ep[2] / (ep[2] - zn);
		
		float k = (ep[2] - 0) / (0-zn);
		
		float x = (ep[0] + k * xn) / (k+1);
		float y = (ep[1] + k * yn) / (k+1);
		
		/////////////////////////////////////////////
		/*
		float[] rect = {0,0,
				0,0,
				0,0,
				0,0};
		
		rect[0] = x - R; rect[1] = y - R;
		rect[2] = x + R; rect[3] = y - R;
		rect[4] = x + R; rect[5] = y + R;
		rect[6] = x - R; rect[7] = y + R;
		
		float xRatio = 0;
		float yRatio = 0;
		
		if (WiGalleryOpenGLRenderer.m_b_horizontal_screen)
		{
			xRatio =  CSStaticData.screen_3d_width_h /CSStaticData.screen_2d_width;
			yRatio =  CSStaticData.screen_3d_height_h /CSStaticData.screen_2d_height;
		}
		else
		{
			xRatio = CSStaticData.screen_3d_width_v /CSStaticData.screen_2d_width;
			yRatio = CSStaticData.screen_3d_height_v /CSStaticData.screen_2d_height;
		}
		
		
		rect[0] /= xRatio; rect[1] /= yRatio;
		rect[2] /= xRatio; rect[3] /= yRatio;
		rect[4] /= xRatio; rect[5] /= yRatio;
		rect[6] /= xRatio; rect[7] /= yRatio;
		
		rect[0] += CSStaticData.screen_2d_width/2; rect[1] = CSStaticData.screen_2d_height/2 - rect[1];
		rect[2] += CSStaticData.screen_2d_width/2; rect[3] = CSStaticData.screen_2d_height/2 - rect[3];
		rect[4] += CSStaticData.screen_2d_width/2; rect[5] = CSStaticData.screen_2d_height/2 - rect[5];
		rect[6] += CSStaticData.screen_2d_width/2; rect[7] = CSStaticData.screen_2d_height/2 - rect[7];
		
		Rect cRect = new Rect();
		cRect.set((int)rect[0], (int)rect[5], (int)rect[4], (int)rect[1]);
		
		SystemDebug.drawRect(cRect);
		*/
		/////////////////////////////////////////////////////////////////////
		
		if (x0 > x+R || x0 < x-R)
		{
			return false;
		}
		else if (y0 > y+R || y0 < y-R)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	
	public static boolean isInRect(float x_3d, float y_3d, float z_3d, float x_2d, float y_2d, float r)
	{		
		if (Double.isNaN(x_3d) || Double.isNaN(y_3d) || Double.isNaN(z_3d))
				return false;
		
		float[] coord2d = get2DCoord(x_3d, y_3d, z_3d);
		float[] coord2d1 = get2DCoord(x_3d+r, y_3d, z_3d);
		
		r = (coord2d1[0] - coord2d[0])/2;
		coord2d[1] = CSStaticData.screen_2d_height - coord2d[1];
		
 		if (x_2d > coord2d[0]+r || x_2d < coord2d[0]-r)
		{
			return false;
		}
		else if (y_2d > coord2d[1]+r || y_2d < coord2d[1]-r)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	private static float[] get2DCoord(float x, float y, float z)
	{       
          float[] ret = new float[4];
          //GLU.gluUnProject((float)x, (float)y, 0f, model, 0, proj, 0, view, 0, ret, 0);
          GLU.gluProject(x, y, z,  WiGalleryOpenGLRenderer.g_model, 0, WiGalleryOpenGLRenderer.g_proj, 0, WiGalleryOpenGLRenderer.g_viewArray, 0, ret, 0);

          
          return ret;
	}
	
	public static int[][] gGroupNoneResid = {
		{ R.drawable.gallery_more_share, R.string.gallery_menu_item_share},                     //share item
		{ R.drawable.gallery_more_delete, R.string.gallery_menu_item_delete},                   //delete item
		{ R.drawable.gallery_more_move, R.string.gallery_menu_item_move},                       //moveTo item
		{ R.drawable.gallery_more_set_as_favorite, R.string.gallery_menu_item_setfavorite},     //favorite item
		{ R.drawable.gallery_more_remove_favorite, R.string.gallery_menu_item_removefavorite},  //remove favorite item
		{ R.drawable.gallery_group_settings, R.string.gallery_menu_item_group},                 //group item
		{ R.drawable.gallery_sort_order, R.string.gallery_menu_item_sortOrder},                 //sortOrder item
//		{ R.drawable.gallery_viewing_with_all_files, R.string.gallery_menu_item_contentSwitch}  //contentSwitch item
	};
	
	public static int[][] gGroupNoneMultiResid = {
		{ R.drawable.gallery_more_share, R.string.gallery_menu_item_share},                     //share item
		{ R.drawable.gallery_more_delete, R.string.gallery_menu_item_delete},                   //delete item
		{ R.drawable.gallery_more_move, R.string.gallery_menu_item_move},                       //moveTo item
		{ R.drawable.gallery_more_set_as_favorite, R.string.gallery_menu_item_setfavorite},     //favorite item
		{ R.drawable.gallery_more_remove_favorite, R.string.gallery_menu_item_removefavorite},  //remove favorite item
	};
	
	public static int[][] gGroupDateResid = {
		{ R.drawable.gallery_more_share, R.string.gallery_menu_item_share},                     //share item
		{ R.drawable.gallery_more_delete, R.string.gallery_menu_item_delete},                   //delete item
		{ R.drawable.gallery_more_move, R.string.gallery_menu_item_move},                       //moveTo item
		{ R.drawable.gallery_more_set_as_favorite, R.string.gallery_menu_item_setfavorite},     //favorite item
		{ R.drawable.gallery_more_remove_favorite, R.string.gallery_menu_item_removefavorite},  //remove favorite item
		{ R.drawable.gallery_group_settings, R.string.gallery_menu_item_group},                 //group item
//		{ R.drawable.gallery_viewing_with_all_files, R.string.gallery_menu_item_contentSwitch}  //contentSwitch item
	};
	
	public static int[][] gGroupDateMultiResid = gGroupNoneMultiResid;
	
	public static int[][] gGroupDateSubResid = gGroupDateResid;
	
	public static int[][] gGroupDateSubMultiResid = gGroupNoneMultiResid;
	
	public static int[][] gGroupLocationResid = gGroupDateResid;
	
	public static int[][] gGroupLocationMultiResid = gGroupNoneMultiResid;
	
	public static int[][] gGroupLocationSubResid = gGroupNoneResid;
	
	public static int[][] gGroupLocationSubMultiResid = gGroupNoneMultiResid;
}

package com.wistron.WiViewer;


import android.content.Context;
import android.graphics.Rect;
import android.os.Environment;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

public class TDStaticData {
	
	public static final String ROOT_DIR     			= Environment.getExternalStorageDirectory()
														.getAbsolutePath().toString()+"/WiCamera3D";
	public static final String LOCAL_PATH_SEPARATOR 	= "/";
	public static final String SERVER_PATH_SEPARATOR 	= "\\";
	public static final int    FLODER_TYPE 				= 1 ;
	public static final int    FILE_TYPE    			= 2 ;
	public static final String FILTER_TEMP_NAME 		= ".tmp";
	public static final String INSTANDSEPERATER 		= ".!.";
	public static int ITEM_STATE 						= 0 ; // gridItem鐘舵?
	public static boolean ROOT_EXIT  					= true ;
	
	public static final int VIEWMODE_ALL_VIEW                 = 0x010; //瑙嗗浘妯″紡锛氭墍鏈夎鍥?
	public static final int VIEWMODE_IMAGE_VIEW               = 0x011; //瑙嗗浘妯″紡锛氬浘鐗囪鍥?
	public static final int VIEWMODE_VIDEO_VIEW               = 0x012; //瑙嗗浘妯″紡锛氳棰戣鍥?
	public static final int GROUPMODE_NONE_VIEW               = 0x00;  //褰掔被妯″紡锛氭棤               锛?000
	public static final int GROUPMODE_DIR_VIEW                = 0x04;  //褰掔被妯″紡锛氭枃浠跺す     锛?100
	public static final int GROUPMODE_DATE_VIEW               = 0x08;  //褰掔被妯″紡锛氫笓杈?         锛?000
	public static final int SORTMODE_DESC_VIEW                = 0x01;  //鎺掑簭妯″紡锛氶檷搴?         锛?001
	public static final int SORTMODE_ASCE_VIEW                = 0x02;  //鎺掑簭妯″紡锛氬崌搴?         锛?010
	public static final int REQUEST_CODE_LOAD_SCENCE          = 0x410; //RequestCode:Gallery鍚姩鏃跺姞杞藉姩鐢昏姹傜爜
	public static final int REQUEST_CODE_SWITCH_VIEW_TILD     = 0x411; //RequestCode:Gallery澶勪簬骞抽摵妯″紡鏃跺垏鎹㈣鍥?
	public static final int REQUEST_CODE_LOAD_VIDEO           = 0x510; //RequestCode:浠巔hotoview鍚姩videoplayer鐨勮姹傜爜
	public static final int REQUEST_CODE_AVBL                 = 0x1000;//RequestCode:鏃犳晥璇锋眰鐮?

	public static int SCREEN_WIDTH                            = 0;     //灞忓箷鏈夋晥瀹藉害
	public static int SCREEN_HEIGHT                           = 0;     //灞忓箷鏈夋晥楂樺害
	public static int SCREEN_WIDTH_ORG                        = 0;     //灞忓箷鏍囩О瀹藉害
	public static int SCREEN_HEIGHT_ORG                       = 0;     //灞忓箷鏍囩О楂樺害
	
	public static int     g_view_mode                       = VIEWMODE_ALL_VIEW;   //瑙嗗浘妯″紡
	public static int     g_sort_mode                       = SORTMODE_DESC_VIEW;  //鎺掑簭鏂瑰紡
	public static int     g_group_mode                      = GROUPMODE_NONE_VIEW; //褰掔被鏂瑰紡
	public static int     g_long_press_timeout              = 50;                 //闀挎寜瓒呮椂
	public static float   g_fling_velocity                  = 1000;                //婊戝姩閫熷害
	public static int     g_load_limit                      = 50;                  //鍔犺浇缂╃暐鍥炬暟閲忛檺鍒?
	public static int     g_thumbnailSize                   = 128;                  //璐村浘缂╃暐鍥惧ぇ灏?
	public static boolean g_gallery_runing                  = false;               //鏍囪瘑WiGallery3DActivity鏄惁鍦ㄨ繍琛?
	public static int     g_label_length                    = 10;                   //涓撹緫宸︿笅瑙掓枃鏈紝璇疯祴鍋舵暟
	public static boolean g_flag_load_completed             = false;               //鏂囦欢鍔犺浇宸插畬鎴?
	
	
	// 鏈?ぇ瀵煎叆绾圭悊鏂囦欢涓暟
	// 鏄剧ず浣嶇疆鍋忕Щ閲?
	public static float[] g_group_start_pos = {-5.25f, 2.0f, -7.0f};
	public static float[] g_group_interval = {3.5f, 3.2f, 0.9f};
	
	public static float[] g_list_start_pos = {-3.75f, 2.0f, -1.8f};
	public static float[] g_list_interval = {1.5f, 1.3f, 0.9f};
	
	public static float g_step_horizon = 0.2f;
	public static float g_step_vertical = 0.12f;
	// For MOTO XOOM
	/*
	public static Rect g_group_2d_pos_1 = new Rect(205, 145, 320, 265);//left, top, right, bottom);
	public static int g_group_2d_interval_1 = 280;
	public static Rect g_group_2d_pos_2 = new Rect(170, 430, 300, 550);//left, top, right, bottom);
	public static int g_group_2d_interval_2 = 300;

	public static Rect g_list_2d_pos_1 = new Rect(135, 80, 250, 195);//left, top, right, bottom);
	public static int g_list_2d_interval_1 = 175;
	public static Rect g_list_2d_pos_2 = new Rect(95, 220, 220, 350);//left, top, right, bottom);
	public static int g_list_2d_interval_2 = 190;
	public static Rect g_list_2d_pos_3 = new Rect(45, 390, 180, 525);//left, top, right, bottom);
	public static int g_list_2d_interval_3 = 210;
	*/
	// For wistron 3D
	public static Rect g_group_2d_pos_1 = new Rect(128, 122, 238, 200);//left, top, right, bottom);
	public static int g_group_2d_interval_1 = 217;
	public static Rect g_group_2d_pos_2 = new Rect(110, 325, 220, 410);//left, top, right, bottom);
	public static int g_group_2d_interval_2 = 230;

	public static Rect g_list_2d_pos_1 = new Rect(135, 60, 220, 150);//left, top, right, bottom);
	public static int g_list_2d_interval_1 = 135;
	public static Rect g_list_2d_pos_2 = new Rect(100, 165, 200, 260);//left, top, right, bottom);
	public static int g_list_2d_interval_2 = 145;
	public static Rect g_list_2d_pos_3 = new Rect(65, 295, 170, 390);//left, top, right, bottom);
	public static int g_list_2d_interval_3 = 158;
	

}

package com.wistron.WiGallery;

import java.util.LinkedList;
import java.util.List;

import android.util.Log;

import Utilities.CSStaticData;

public class StatusInfo 
{
	private static int m_restore_num = 0;
	//GL part
	private CSStaticData.LIST_TYPE         m_type                = CSStaticData.LIST_TYPE.LIST_INVALID;
	private CSStaticData.LIST_ELEMENT_TYPE m_element_type        = CSStaticData.LIST_ELEMENT_TYPE.LIST_ELEMENT_ALL;
	private boolean                        m_b_enter_group       = false;     //是否进入文件列表标志
	private int                            m_select_group_index  = -1;        //保存当前选择的子链表编号
	private int                            m_index               = 0;
	private ELEM_STATUS                    m_status              = ELEM_STATUS.NORMAL_STATUS;                         
	//System
	private int      m_screen_width                              = 800;      //屏幕宽度
	private int      m_screen_height                             = 480;      //屏幕高度
	private int      m_long_press_timeout                        = 450;      //长按超时
	private float    m_fling_velocity                            = 1000;     //滑动速度
	private int      m_load_limit                                = 50;       //加载缩略图数量限制
	private int      m_thumbnailSize                             = 256;      //贴图缩略图大小
	private int      m_label_length                              = 10;       //专辑左下角文本，请赋偶数
	private int      m_fling_speed_threshold                     = 20;       //触发滑动事件的速度阀值
	private boolean  m_sort_order_mode                           = false;    //排序模式

	//Activity
	private int      m_scroll_bar_progress                       = 0;
	private int      m_scroll_bar_max                            = 10;
	private boolean  m_dimension_switch                          = false;

	// selected status list
	private List<Element>  m_selected_list                       = null;     //保存选中文件状态的列表
	private boolean        m_is_select_mode                      = false;    //是否处于选择模式
	
	//Gallery messageboxes
	private boolean mIsMsgBoxDeleteProgressShow           = false;  //进度对话框
	private boolean mIsMsgBoxMoveProgressShow             = false;  //进度对话框
	private boolean mIsMsgBoxSetFavoriteProgressShow      = false;  //进度对话框
	private boolean mIsMsgBoxRemoveFavoriteProgressShow   = false;  //进度对话框 
	private int     mIsMsgBoxDeleteProgress               = 0;
	private int     mIsMsgBoxMoveProgress                 = 0;
	private int     mIsMsgBoxSetFavoriteProgress          = 0;
	private int     mIsMsgBoxRemoveFavoriteProgress       = 0;
	
	//Gallery Multi Operation
	private boolean mHasOperatedMenu                      = false;
	private int     mCurMultiStatus                       = 0;

	// group_data
	private List<GroupDataInfo> grouo_data_info_list = null;
	private  float[] pos_offset = {0,0,0}; 
	private int group_data_index = 0;

	// group——none模式下排序
	private int sort_status = 1;// 0:升序 1：降序

	public StatusInfo() {
		if (grouo_data_info_list != null)
			grouo_data_info_list.clear();
		else
			grouo_data_info_list = new LinkedList<GroupDataInfo>();
	}

	public void saveGalleryMessageBoxes(boolean msgboxDeleteProgress,
										boolean msgboxMoveProgress,
										boolean msgboxRemoveFavoriteProgress,
										boolean msgboxSetFavoriteProgress){
		mIsMsgBoxDeleteProgressShow         = msgboxDeleteProgress;
		mIsMsgBoxMoveProgressShow           = msgboxMoveProgress;
		mIsMsgBoxRemoveFavoriteProgressShow = msgboxRemoveFavoriteProgress;
		mIsMsgBoxSetFavoriteProgressShow    = msgboxSetFavoriteProgress;
	}
	
	public void saveGalleryMessageBoxesProgress(int msgboxDeleteProgress,
												int msgboxMoveProgress,
												int msgboxRemoveFavoriteProgress,
												int msgboxSetFavoriteProgress){
		mIsMsgBoxDeleteProgress               = msgboxDeleteProgress;
		mIsMsgBoxMoveProgress                 = msgboxMoveProgress;
		mIsMsgBoxSetFavoriteProgress          = msgboxSetFavoriteProgress;
		mIsMsgBoxRemoveFavoriteProgress       = msgboxRemoveFavoriteProgress;
	}
	
	public boolean getMsgboxDeleteProgressStatus(){
		return mIsMsgBoxDeleteProgressShow;
	}
	
	public int getMsgboxDeleteProgress(){
		return mIsMsgBoxDeleteProgress;
	}
	
	public boolean getMsgBoxMoveProgressStatus(){
		return mIsMsgBoxMoveProgressShow;
	}
	
	public int getMsgBoxMoveProgress(){
		return mIsMsgBoxMoveProgress;
	}
	
	public boolean getMsgBoxRemoveFavoriteProgressStatus(){
		return mIsMsgBoxRemoveFavoriteProgressShow;
	}
	
	public int getMsgBoxRemoveFavoriteProgress(){
		return mIsMsgBoxRemoveFavoriteProgress;
	}
	
	public boolean getMsgBoxSetFavoriteProgressStatus(){
		return mIsMsgBoxSetFavoriteProgressShow;
	}
	
	public int getMsgBoxSetFavoriteProgress(){
		return mIsMsgBoxSetFavoriteProgress;
	}
	
	public void saveGL()
	{
		//Selected List
//		m_selected_list = WiGalleryOpenGLRenderer.m_data_manager.getSelectedElementsList();


		//GL
		if (WiGalleryOpenGLRenderer.m_element_group != null)
		{
			m_type                  = WiGalleryOpenGLRenderer.m_element_group.getType();
			m_element_type          = WiGalleryOpenGLRenderer.m_element_group.getElementType();
			m_b_enter_group         = WiGalleryOpenGLRenderer.m_element_group.bEnterGroup();
			m_select_group_index    = WiGalleryOpenGLRenderer.m_element_group.getOpenGroupIndex();
			m_status                = WiGalleryOpenGLRenderer.m_element_group.getChoosedMode();
			m_index                 = WiGalleryOpenGLRenderer.m_element_group.getIndex();

			grouo_data_info_list.clear();
			if (WiGalleryOpenGLRenderer.m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_DATE && grouo_data_info_list.size() == 0 && WiGalleryOpenGLRenderer.m_element_group.bEnterGroup())
			{
					for (int i = 0; i < WiGalleryOpenGLRenderer.m_element_group.size(); i++)
					{
						ElementList list = WiGalleryOpenGLRenderer.m_element_group.get(i);
						GroupDataInfo info = new GroupDataInfo();
	
						info.pos = list.getPosition();
						info.posIndex = list.getIndex();
	
						grouo_data_info_list.add(info);
	
					}
				
					pos_offset[0] = WiGalleryOpenGLRenderer.m_element_group.pos_offset[0];
					pos_offset[1] = WiGalleryOpenGLRenderer.m_element_group.pos_offset[1];
					pos_offset[2] = WiGalleryOpenGLRenderer.m_element_group.pos_offset[2];
				
					group_data_index = WiGalleryOpenGLRenderer.m_element_group.m_index;
			}
			else if (WiGalleryOpenGLRenderer.m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_NONE)
			{
				ElementList list = WiGalleryOpenGLRenderer.m_element_group.get(0);
				for (int i = 0 ; i < list.size(); i++)
				{
					Element elem = list.get(i);
					elem.m_animation_type = CSStaticData.ANIMATION_TYPE.NONE;
				}
			}
		}

		WiGalleryOpenGLRenderer.m_data_manager.ReleaseAllTexture();

		if(CSStaticData.DEBUG)
			Log.e("SaveGL", String.format("----------------- m_index = %d --------------", m_index));

		if(WiGalleryOpenGLRenderer.m_element_group != null){
			WiGalleryOpenGLRenderer.m_element_group.m_is_drawing = false;
		}
	}

	public void saveSystem(){
		//Activity
		m_screen_width          = CSStaticData.g_screen_width;
		m_screen_height         = CSStaticData.g_screen_height;
		m_long_press_timeout    = CSStaticData.g_long_press_timeout;
		m_fling_velocity        = CSStaticData.g_fling_velocity;
		m_load_limit            = CSStaticData.g_load_limit;
		m_thumbnailSize         = CSStaticData.g_thumbnailSize;
		m_label_length          = CSStaticData.g_label_length;
		m_fling_speed_threshold = CSStaticData.g_fling_speed_threshold;
	}

	public void saveActivity(){
		m_dimension_switch          = CSStaticData.g_is_3D_mode; 
	}
	
	public void saveMulitOperation(int curMultiStatus, boolean hasOperatedMenu){
		mCurMultiStatus  = curMultiStatus;
		mHasOperatedMenu = hasOperatedMenu;
	}

	public void saveSortOrderMode(){
		m_sort_order_mode           = CSStaticData.g_sort_order_mode;
	}

	public void saveActivitySeekbar(int progress, int max){
		m_scroll_bar_progress = progress;
		m_scroll_bar_max      = max;
	}

	public void restoreGL()
	{
		//GL
		if (WiGalleryOpenGLRenderer.m_element_group != null)
		{	
			WiGalleryOpenGLRenderer.m_element_group.m_b_restore = false;
			/*
			if (m_restore_num < 1)
			{
				m_restore_num++;
				return;
			}
			else
			{
				m_restore_num = 0;
			}
			 */

			if(CSStaticData.DEBUG)
				Log.e("RESTOREGL", String.format("----------------- m_index = %d --------------", m_index));


			//WiGalleryOpenGLRenderer.m_element_group.setType(m_type, m_b_enter_group, m_select_group_index, m_status, m_element_type);

			WiGalleryOpenGLRenderer.m_element_group.rebuildList(m_type, m_b_enter_group, m_select_group_index, m_status, m_element_type);
			WiGalleryOpenGLRenderer.m_element_group.refreshPosition(m_index);

			if (WiGalleryOpenGLRenderer.m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_DATE && WiGalleryOpenGLRenderer.m_element_group.bEnterGroup())
			{
				WiGalleryOpenGLRenderer.m_element_group.pos_offset[0] = pos_offset[0];
				WiGalleryOpenGLRenderer.m_element_group.pos_offset[1] = pos_offset[1];
				WiGalleryOpenGLRenderer.m_element_group.pos_offset[2] = pos_offset[2];
	
				if (WiGalleryOpenGLRenderer.m_element_group.size() == grouo_data_info_list.size())
				{		
					for (int i = 0; i < WiGalleryOpenGLRenderer.m_element_group.size(); i++)
					{
						ElementList list = WiGalleryOpenGLRenderer.m_element_group.get(i);
						GroupDataInfo info = grouo_data_info_list.get(i);
		
		
						list.moveTo(info.pos[0], info.pos[1], info.pos[2]);
						list.setIndex(info.posIndex);
		
					}
				
					WiGalleryOpenGLRenderer.m_element_group.m_index = group_data_index;
				}
			}
			

			int curIndex = WiGalleryOpenGLRenderer.m_element_group.getCurIndex();
			int totalIndex = WiGalleryOpenGLRenderer.m_element_group.getTotalIndex();

			WiGalleryInterface.m_onGLMoveListener.setScrollBar(curIndex, totalIndex);

			
			WiGalleryOpenGLRenderer.m_element_group.m_is_drawing = true;
			
			if(CSStaticData.DEBUG)
				Log.e("RESTOREGL", String.format("-----------------restore finished --------------"));
			
	        try{ 
	            Thread.sleep(500); 
	           }
	        catch(Exception ex)
	           { 
	            ex.printStackTrace(); 
	           }
			WiGalleryOpenGLRenderer.m_element_group.m_b_restore = true;
		}


		//最后开始处理Element的选中状态
		//TODO .......................
	}

	public void restoreSystem(){
		//System
		CSStaticData.g_long_press_timeout    = m_long_press_timeout;
		CSStaticData.g_fling_velocity        = m_fling_velocity;
		CSStaticData.g_load_limit            = m_load_limit;
		CSStaticData.g_thumbnailSize         = m_thumbnailSize;
		CSStaticData.g_label_length          = m_label_length;
		CSStaticData.g_fling_speed_threshold = m_fling_speed_threshold;
	}

	public void restoreActivity(){
		//		CSStaticData.g_is_3D_mode            = m_dimension_switch; 
		CSStaticData.g_sort_order_mode       = m_sort_order_mode;
	}
	
	public int restoreCurMultiStatus(){
		return mCurMultiStatus;
	}
	
	public boolean restoreHasOperatedMenu(){
		return mHasOperatedMenu;
	}

	public int restoreActivitySeekbarProgress(){
		return m_scroll_bar_progress;
	}

	public int restoreActivitySeekbarMax(){
		return m_scroll_bar_max;
	}

	public boolean restoreSortOrderMode(){
		return m_sort_order_mode;
	}
}

package com.wistron.WiGallery;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import Utilities.CSStaticData;
import Utilities.FileOperation;
import android.util.Log;
import com.wistron.WiGallery.Element;
import com.wistron.WiGallery.GEO.GeoParser;
import com.wistron.WiGallery.WiGalleryInterface.onRequestListListener;


public class DataManager 
{
	private List<Element>   m_request_list                   = null; //请求文件列表
	private List<Element>   m_need_load_texture_request_list = null; //请求文件列表
	private List<Element>   m_list                           = null; //主文件列表
	private List<Element>   m_load_file_list                 = null; //加载了纹理的文件列表
	private List<Element>   m_need_delete_element_list       = null;
	private List<Element>   m_selected_element_list          = null; //已选中的元素列表
	
	private onRequestListListener requestListListener        = null;
	private final String TAG = "DataManager";
	public DataManager()
	{
		if (m_list != null)
			m_list.clear();
		else
			m_list = new LinkedList<Element>();
		
		if (m_request_list != null)
			m_request_list.clear();
		else 
			m_request_list = new ArrayList<Element>();
		
		if (m_load_file_list != null)
			m_load_file_list.clear();
		else 
			m_load_file_list = new ArrayList<Element>();
		
		if (m_need_load_texture_request_list != null)
			m_need_load_texture_request_list.clear();
		else 
			m_need_load_texture_request_list = new ArrayList<Element>();
		
		m_need_delete_element_list = new ArrayList<Element>();
		
		
		
	}
	
	/**
	 * DataManager析构函数
	 */
	public void destoryDataManager(){
//		if (m_list != null){
//			m_list.clear();
//			m_list = null;
//		}
		if(m_request_list != null){
			m_request_list.clear();
			m_request_list = null;
		}
		if (m_load_file_list != null){
			m_load_file_list.clear();
			m_load_file_list = null;
		}
		if(m_need_load_texture_request_list != null){
			m_need_load_texture_request_list.clear();
			m_need_load_texture_request_list = null;
		}
		if(m_need_delete_element_list != null){
			m_need_delete_element_list.clear();
			m_need_delete_element_list = null;
		}
		if(m_selected_element_list != null){
			m_selected_element_list.clear();
			m_selected_element_list = null;
		}
		
		System.gc();
	}
	
	public void AddNeedDeleteElement(Element elem)
	{
		if (elem != null)
		{
			if (m_need_delete_element_list == null)
				m_need_delete_element_list = new ArrayList<Element>();
			
			synchronized(m_need_delete_element_list)
			{
				m_need_delete_element_list.add(elem);
			}
		}
	}
	
	/**
	 * 删除Element对象兵从主链表清除该元素，该函数必须在ondraw调用
	 */
	public void DeleteNeedDeleteElement()
	{
		if (m_need_delete_element_list == null)
			return;
		
		synchronized(m_need_delete_element_list)
		{	
	        Iterator<Element> it = m_need_delete_element_list.iterator();   
	        while (it.hasNext())    
	        {   
	        	Element elem = it.next();
	        	if (elem != null)
	        	{
	        		Delete(elem.getName());
	        		it.remove();
	        	}
	        }
		}
	}
	
	
	public void AddNeedLoadTextureFile(Element file)
	{
		if (file == null)
			return ;
		
		if ( m_need_load_texture_request_list == null)
			m_need_load_texture_request_list = new ArrayList<Element>();
		
		synchronized(m_need_load_texture_request_list)
		{
			m_need_load_texture_request_list.add(file);
			
			if (CSStaticData.DEBUG)
				Log.i(TAG, String.format("[+][%d][AddNeedLoadTextureFile][%s]", m_need_load_texture_request_list.size(),file.getName()));
		}
	}
	
	public Element GetNeedLoadTextureFile(int index)
	{
		if (m_need_load_texture_request_list == null)
			return null;
		
		synchronized(m_need_load_texture_request_list)
		{
			if (index < 0 || index >= m_need_load_texture_request_list.size())
				return null;
			
			Element elem = m_need_load_texture_request_list.get(index);
			m_need_load_texture_request_list.remove(index);
			
			//Log.d(TAG, String.format("[-][%d][AddNeedLoadTextureFile][%s]", m_need_load_texture_request_list.size(), elem.getName()));
			return elem;
		}
	}
	
	/**
	 * 删除所有的纹理
	 */
	public void ReleaseAllTexture()
	{
		if(m_list == null || m_list.size() <= 0){
			return;
		}

		synchronized(m_list)
		{      
	        for (int i = 0 ; i < m_list.size(); i++)
	        {
	        	Element elem = m_list.get(i);
//	        	WiGalleryOpenGLRenderer.m_resource_manager.DeleteTextureID(elem); //[For Black Block]
	        	WiGalleryOpenGLRenderer.m_resource_manager.ReinitTextureID(elem); //[For Black Block]
	        }
		}
	}
	

	/**
	 * 向主文件列表中添加信息
	 * @param file
	 * @return
	 */
	public int Add(Element elem)
	{
		if (m_list == null)
		{
			m_list = new LinkedList<Element>();
		}
		
		synchronized(m_list)
		{
			if (m_list == null)
			{
				m_list = new LinkedList<Element>();
			}
			
			m_list.add(elem);
			
			return m_list.size() - 1;
		}
	}
	
	public Element Get(int index)
	{
		if(m_list == null){
			return null;
		}
		
		if (index < 0) 
			return null;
		else if (index >= m_list.size()) 
			return null;
		
		synchronized(m_list)
		{		
			if(m_list == null){
				return null;
			}
			return m_list.get(index);
		}
	}
	
	public Element Get(String fileName)
	{
		if (fileName == null || m_list == null) return null;
		
		synchronized(m_list)
		{		
	        Iterator<Element> it = m_list.iterator();   
	        while (it.hasNext())    
	        {   
	        	Element file = it.next();
	        	if (file != null && fileName.compareToIgnoreCase(file.getName()) == 0)
	        	{
	        		return file;
	        	}
	        }
		}
		
		return null;
	}
	
	/**
	 * 获取主链表长度
	 * @return
	 */
	public int getMainListSize(){
		int size = 0;
		if(m_list == null){
			return size;
		}
		synchronized (m_list) {
			if(m_list == null){
				return 0;
			}
			size = m_list.size();
		}
		return size;
	}
	
	/**
	 * 拷贝方式获取主数据链表
	 * @return
	 */
	public List<Element> cloneMainList(){
		List<Element> result = null;
		
		if(m_list == null){
			return null;
		}
		
		synchronized(m_list)
		{		
	       result = new ArrayList<Element>(m_list);
		}
		
		return result;
	}
	
	public ElementList GetList (CSStaticData.LIST_TYPE type, String name)
	{
		if (type == CSStaticData.LIST_TYPE.LIST_NONE)
		{
			return null;
		}
		else
		{	
			ElementList list = new ElementList(type);
			list.setName(name);
			int mainListNum = m_list.size();
			
			if (type == CSStaticData.LIST_TYPE.LIST_DATE)
			{			
				for (int j = 0; j < mainListNum; j++)
				{
					Element elem = m_list.get(j);
					if (elem != null && name.compareToIgnoreCase(elem.getDate()) == 0)
					{
						list.add(elem);
					}
				}
			}
			else
			{
				// 地理信息分组
				//~! 

			}
			return list;
		}
	}
	
	public List<ElementList> GetListArray (CSStaticData.LIST_TYPE type, CSStaticData.LIST_ELEMENT_TYPE elementType)
	{
		List<ElementList> mainList = new LinkedList<ElementList>();
		if (type == CSStaticData.LIST_TYPE.LIST_NONE)
		{
			ElementList list = new ElementList(WiGalleryOpenGLRenderer.m_data_manager.m_list, type);
			if (list != null)
			{
				if(elementType == CSStaticData.LIST_ELEMENT_TYPE.LIST_ELEMENT_ALL)
				{
					mainList.add(list);
				}
				else if (elementType == CSStaticData.LIST_ELEMENT_TYPE.LIST_ELEMENT_2D)
				{
					if (WiGalleryOpenGLRenderer.m_data_manager.m_list == null)
						return null;
					
					ElementList clist = new ElementList(type);
					
					int num = WiGalleryOpenGLRenderer.m_data_manager.m_list.size();
					for (int i = 0; i < num; i++)
					{
						Element elem = WiGalleryOpenGLRenderer.m_data_manager.m_list.get(i);
						if (elem.getFileType() == CSStaticData.MEDIA_TYPE.NORMAL_IMAGE || elem.getFileType() == CSStaticData.MEDIA_TYPE.NORMAL_VIDEO)
						{
							clist.add(elem);
						}
					}
					
					mainList.add(clist);
				}
				else if (elementType == CSStaticData.LIST_ELEMENT_TYPE.LIST_ELEMENT_3D)
				{
					if (WiGalleryOpenGLRenderer.m_data_manager.m_list == null)
						return null;
					
					ElementList clist = new ElementList(type);
					
					int num = WiGalleryOpenGLRenderer.m_data_manager.m_list.size();
					for (int i = 0; i < num; i++)
					{
						Element elem = WiGalleryOpenGLRenderer.m_data_manager.m_list.get(i);
						if (elem.getFileType() == CSStaticData.MEDIA_TYPE.STOERE_IMAGE || elem.getFileType() == CSStaticData.MEDIA_TYPE.STOERE_VIDEO)
						{
							clist.add(elem);
						}
					}
					
					mainList.add(clist);
				}
				else
				{
					// 不做处理
				}
			}
		}
		else if (type == CSStaticData.LIST_TYPE.LIST_DATE)
		{
			// 查找当前文件有多少个日期分类
			// 以下可以优化
			List<String> strList = WiGalleryOpenGLRenderer.m_data_manager.GetAlbumName();
			if (strList == null)
				return null;
			
			for (int i = 0; i < strList.size(); i++)
			{				
				String strDate = strList.get(i);
				ElementList list = new ElementList(type);
				list.setName(strDate);
				
				int mainListNum = Size();				
				for (int j = 0; j < mainListNum; j++)
				{
					Element elem = Get(j);
					if (elem != null && strDate.compareToIgnoreCase(FileOperation.getDate(elem.getDate())) == 0)
					{
						if(elementType == CSStaticData.LIST_ELEMENT_TYPE.LIST_ELEMENT_ALL)
						{
							list.add(elem);
						}
						else if (elementType == CSStaticData.LIST_ELEMENT_TYPE.LIST_ELEMENT_3D)
						{
							if (elem.getFileType() == CSStaticData.MEDIA_TYPE.STOERE_IMAGE || elem.getFileType() == CSStaticData.MEDIA_TYPE.STOERE_VIDEO)
							{
								list.add(elem);
							}
						}
						else if (elementType == CSStaticData.LIST_ELEMENT_TYPE.LIST_ELEMENT_2D)
						{
							if (elem.getFileType() == CSStaticData.MEDIA_TYPE.NORMAL_IMAGE || elem.getFileType() == CSStaticData.MEDIA_TYPE.NORMAL_VIDEO)
							{
								list.add(elem);
							}
						}
						else
						{
							// 不做处理
						}
								
					}
				}
				
				if (list.size() > 0)
					mainList.add(list);
			}
		}
		else
		{
			// 地理信息分组
			//~!
			 WiGalleryOpenGLRenderer.mAsyncFileProvider.SortListByLocation(mainList, type);
			
		}
		
		return mainList;
	}
	
	/**
	 * 在activity隐藏时调用 ，该函数删除所有的bitmap
	 */
	public void DelAllBitmap()
	{
		if(m_list == null){
			return;
		}
		
		synchronized (m_list) {
			for  (int i = 0; i < m_list.size(); i++)
			{
				Element elem = m_list.get(i);
				if (elem != null)
				{
					if (elem.m_bmpL != null)
					{
						elem.m_bmpL.recycle();
						elem.m_bmpL = null;
					}

					if (elem.m_bmpR != null)
					{
						elem.m_bmpR.recycle();
						elem.m_bmpR = null;
					}
				}
			}
		}
	}

	
	/**
	 * 根据字符串首字母排序
	 * @param elementLists
	 * @param locationList
	 * @return
	 */
	public List<ElementList> GetListArrayByAleph(List<ElementList> elementList,Locale locale){
	
		if (elementList.size() == 0) {
			return null;
		}else {
			Comparator<Object> comparator=Collator.getInstance(locale); 
			Collections.sort(elementList, comparator);
			return elementList;
		}
		
	}
	
	
	public int Size()
	{
		if (m_list != null)
			return m_list.size();
		else
			return 0;
	}
	
	public void Delete(int index)
	{
		if (index < 0) return;
		else if (index >= m_list.size()) return;
		
		synchronized(m_list)
		{		
			Element elem = m_list.get(index);
			WiGalleryOpenGLRenderer.m_resource_manager.DeleteTextureID(elem);
			m_list.remove(index);
		}
	}
	
	/**
	 * 从主链表删除元素以及对应的纹理ID
	 */
	public void Delete(String fileName)
	{
		if (fileName == null) return;
		
		synchronized(m_list)
		{	
	        Iterator<Element> it = m_list.iterator();   
	        while (it.hasNext())    
	        {   
	        	Element file = it.next();
	        	if (file != null && fileName.compareToIgnoreCase(file.getName()) == 0)
	        	{
	        		WiGalleryOpenGLRenderer.m_resource_manager.DeleteTextureID(file);
	        		it.remove();
	        		return;
	        	}
	        } 
		}
	}
	
/**
	 * 
	 * @param name
	 * @param mode  0:日期   1:位置
	 * CSStaticData.LIST_TYPE 该参数在mode = 1时有效
	 */
	public void DeleteAlbum(String name, int mode, CSStaticData.LIST_TYPE locType)
	{
		if (name == null) return;
		
		synchronized(m_list)
		{	
	        Iterator<Element> it = m_list.iterator();   
	        
	        if (mode == 0)
	        {
		        while (it.hasNext())    
		        {   
		        	Element elem = it.next();
		        	
			        if (elem != null && name.compareToIgnoreCase(elem.getDate()) == 0)
		        	{
		        		WiGalleryOpenGLRenderer.m_resource_manager.DeleteTextureID(elem);
		        		it.remove();
		        	}
		        } 
	        }
	        else if (mode == 1)
	        {
		        while (it.hasNext())    
		        {   
		        	Element elem = it.next();
		        	if (elem != null)
		        	{
			        	String loc = GeoParser.parserGeo(WiGalleryOpenGLRenderer.m_context, elem.m_longitude, elem.m_latitude, locType);
				        if (elem != null && name.compareToIgnoreCase(loc) == 0)
			        	{
			        		WiGalleryOpenGLRenderer.m_resource_manager.DeleteTextureID(elem);
			        		it.remove();
			        	}
		        	}
		        } 
	        }
	        else
	        {
	        	
	        }
		}		
	}
	
	/**
	 * 清空主文件列表
	 */
	public void DeleteAll()
	{
		synchronized(m_list)
		{	
	        Iterator<Element> it = m_list.iterator();   
	        while (it.hasNext())    
	        {   
	        	Element file = it.next();
	        	if (file != null)
	        	{
	        		WiGalleryOpenGLRenderer.m_resource_manager.DeleteTextureID(file);
	        		it.remove();
	        	}
	        } 
		}
	}
	
	public List<String> GetAlbumName()
	{
		List<String> list = new LinkedList<String>();
		if (m_list == null)
			return null;
		
		synchronized(m_list)
		{	
			if (m_list == null)
				return null;
			
			for (int i = 0; i < m_list.size(); i++)
			{
  
	        	Element elem = m_list.get(i);
	        	if (elem != null)
	        	{
	        		String str = null;

	        		str = FileOperation.getDate(elem.getDate());
	        		
	        		// 遍历查找
	        		if (list.size() == 0)
	        		{
	        			list.add(str);
	        		}
	        		else
	        		{
	        			boolean bFind = false;
	        			for (int j = 0; j < list.size(); j++)
	        			{
	        				if (list.get(j).compareTo(str) == 0)
	        				{
	        					bFind = true;
	        					break;
	        				}
	        			}
	        			
	        			if (!bFind)
	        			{
	        				list.add(str);
	        			}
	        		}
	        	}
	        } 
		}

		return list;
	}
	
	
	/**
	 * 升序排列
	 * @param list
	 * @return
	 */
	public List<Element> sortByAsc(List<Element> list)
	{
		long startTime = System.currentTimeMillis();
		if (list == null || list.size() <= 0) return null;
		List<Element> output = new ArrayList<Element>();
		for (int i = 0; i < list.size(); i++)
		{
			Element file = list.get(i);
			if (file != null)
			{
				if (output.size() == 0)
				{
					output.add(file);
				}
				
				else
				{
					boolean isInsert = false;
					String date = file.getDate();
					for (int j = 0; j < output.size(); j++)
					{
						String TmpDate = output.get(j).getDate();
						if (TmpDate.compareTo(date) > 0)
						{
							// 当前元素值大于待插入的值，则插入
							output.add(j, file);
							isInsert = true;
							break;
						}
					}
					
					if (!isInsert)
					{
						output.add(file);
					}
				}
			}
		}
		
		list.clear();
//		List<Element> result = FileOperation.quickSortByAsc(list);
		long endTime = System.currentTimeMillis();
		if(CSStaticData.DEBUG){
			Log.w(TAG, "[sortByAsc]排序时间: " + (endTime - startTime));
		}
//		return result;
		return output;
	}
	
	/**
	 * 降序排列
	 * @param list
	 * @return
	 */
	public List<Element> sortByDesc(List<Element> list)
	{
		long startTime = System.currentTimeMillis();
		if (list == null || list.size() <= 0) return null;
		
		List<Element> output = new ArrayList<Element>();
		for (int i = 0; i < list.size(); i++)
		{
			Element file = list.get(i);
			if (file != null)
			{
				if (output.size() == 0)
				{
					output.add(file);
				}
				
				else
				{
					boolean isInsert = false;
					String date = file.getDate();
					for (int j = 0; j < output.size(); j++)
					{
						String TmpDate = output.get(j).getDate();
						if (TmpDate.compareTo(date) < 0)
						{
							// 当前元素值大于待插入的值，则插入
							output.add(j, file);
							isInsert = true;
							break;
						}
					}
					
					if (!isInsert)
					{
						output.add(file);
					}
				}
			}
		}
		
		list.clear();
//		List<Element> result = FileOperation.quickSortByDesc(list);
		long endTime = System.currentTimeMillis();
		if(CSStaticData.DEBUG){
			Log.w(TAG, "[sortByDesc]排序时间: " + (endTime - startTime));
		}
//		return result;
		return output;
	}
	
	/**
	 * 为主链表排序
	 * @param isAsc
	 */
	public void sortMainList(boolean isAsc){
		if(isAsc){
			m_list = sortByAsc(m_list);
		}else{
			m_list = sortByDesc(m_list);
		}
	}
	
	/**
	 * 地理位置排序
	 * @param list
	 * @param type
	 */
	public static void SortListByLocation(List<ElementList> list, CSStaticData.LIST_TYPE type){
		int     mainListSize  = 0,
			    inputListSize = 0;
		boolean hasCanceled   = false;
		boolean hasInserted   = false; //是否以找到对应的列表，没有找到则新建一个列表
		String  tempAddr      = "";
		List<Element> mainDataList = null;
		Element tempElement   = null;
		
		if(WiGalleryOpenGLRenderer.m_data_manager == null){
			return;
		}else{
			mainDataList = WiGalleryOpenGLRenderer.m_data_manager.cloneMainList();
			mainListSize = mainDataList.size();
		}
		
		if(list != null){
			list.clear();
		}
		
		for(int i = 0; i < mainListSize; i++){//遍历主链表
			if(hasCanceled){
				return; //可以直接return
			}
//			tempAddr = GeoParser.parserGeo(mContext, mainDataList.get(i).m_latitude, mainDataList.get(i).m_latitude, type);
			tempElement = mainDataList.get(i);
			for(int m = 0; m < tempElement.m_str_address.length; m++){
				tempAddr = tempElement.m_str_address[m] + ", ";
			}
			if(tempAddr.length() >= 2){
				tempAddr = tempAddr.substring(0, tempAddr.length() - 2);
			}
			
			inputListSize = list.size();
			hasInserted = false;
			for(int j = 0; j < list.size(); j++){
				if(hasCanceled){
					return; //可以直接return
				}
				//比较list中的每条列表的头结点
				if(list.get(j).getName().equals(tempAddr)){
					list.get(j).add(mainDataList.get(i));
					hasInserted = true;
					break;
				}
			}
			//没有找到对应的链表，新建一个列表
			if(!hasInserted){ 
				if(hasCanceled){
					return; //可以直接return
				}
				ArrayList<Element> appendElementList = new ArrayList<Element>();
				ElementList tmpElementList = null; 
				appendElementList.add(mainDataList.get(i));
				tmpElementList = new ElementList(appendElementList, type);
				tmpElementList.setName(tempAddr);
				list.add(tmpElementList);
			}
		}
	}


	/**
	 * 读取请求文件列表
	 * @return
	 */
	public List<Element> getRequestLoadFileList(){
		List<Element> output = new ArrayList<Element>();
		
		if (m_request_list == null)
		{
			return null;
		}
		
		synchronized (m_request_list) {
			if (m_request_list == null)
			{
				return null;
			}
			for(int i = 0; i < m_request_list.size(); i++){
				output.add(m_request_list.get(i));
			}
			m_request_list.clear();
		}
		
		//调用读取完毕回调
		if(requestListListener != null)
			requestListListener.onRequestListReadCompleted();
		
		return output;
	}
	
	/**
	 * 写入请求文件列表
	 */
	public void setRequestLoadFileList(Element file){
		
		if (file == null)
		{
			return;
		}
		
		
		if(file.isRequest())
		{
			return;
		}
		
		
		if (m_request_list == null)
		{
			m_request_list = new ArrayList<Element>();
		}
		
		synchronized (m_request_list){
			//遍历是否有重复元素
			int cnt = m_request_list.size();
			for(int i = 0; i < cnt; i++){
				if(m_request_list.get(i).toString().equals(file.getName())){
					return;
				}
			}
			//遍历后没有重复元素，添加
			m_request_list.add(file);
			file.setRequest( true );
			
			if (CSStaticData.DEBUG)
				Log.d(TAG, String.format("[Add][%d][setRequestLoadFileList][%s]", m_request_list.size(), file.getName()));
		}
		
		//调用写入完毕回调
		if(requestListListener != null)
			requestListListener.onRequestListWriteCompleted();
	}
	
	/**
	 * 设置请求文件列表监听
	 * @param listener onRequestListListener
	 */
	public void setOnRequestListListener(onRequestListListener listener){
		requestListListener = listener;
	}

	public void clearMainList() {
		if(m_list != null){
			m_list.clear();
		}
	}
	
	/**
	 * 设订元素的选中状态
	 * @param elem
	 */
	public void setElementSelected(Element elem){
		if(m_selected_element_list == null){
			m_selected_element_list = new ArrayList<Element>();
		}
		
		synchronized (m_selected_element_list) {
			if(m_selected_element_list == null){
				m_selected_element_list = new ArrayList<Element>();
			}
			
			m_selected_element_list.add(elem);
		}
	}
	
	/**
	 * 取消元素的选中状态
	 * @param elem
	 */
	public void setElementUnselected(Element elem){
		if(m_selected_element_list == null){
			m_selected_element_list = new ArrayList<Element>();
		}
		
		synchronized (m_selected_element_list) {
			if(m_selected_element_list == null){
				m_selected_element_list = new ArrayList<Element>();
			}
			
			m_selected_element_list.remove(elem);
		}
	}
	
	public synchronized List<Element> getSelectedElementsList(){
		return m_selected_element_list;
	}
}



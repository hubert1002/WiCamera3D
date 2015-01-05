package com.wistron.WiGallery;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.wistron.StreamHelper.MPOFileStreamParser;
import com.wistron.WiGallery.WiGalleryInterface.BatchProcessCallBack;
import com.wistron.WiGallery.GEO.GEOCacheHelper;
import com.wistron.WiGallery.GEO.GeoParser;
import com.wistron.WiViewer.WiImageViewerActivity;

import Utilities.*;
import android.R.integer;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteBindOrColumnIndexOutOfRangeException;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.Thumbnails;
import android.util.Log;

/**
 * Copyright (c) 2012 Wistron SWPC
 * All rights reserved.
 * @author Cocoonshu
 * @date 2012-02-23 12:00:00
 * @comment 
 * @purpose Provide Add/Delete/Modify/Select Operation on FileSystem
 * @detail 
 */
public class AsyncFileProvider {

	protected static final String TAG                     = "AsyncFileProvider";
	
	public    static boolean      CPU_DEBUG               = false;
	public    static boolean      Thread_DEBUG            = false;
	public    static final int    DELMODE_DATE            = 0x0010; //删除模式：按日期删除
	public    static final int    DELMODE_LOCA            = 0x0011; //删除模式：按地区删除
	public    static final int    CLEAR_DATABASE_ALL      = 0x0012; //清空数据库：所有数据库
	public    static final int    CLEAR_DATABASE_GEO      = 0x0013; //清空数据库：地理信息数据库
	public    static final int    CLEAR_DATABASE_FAVORITE = 0x0014; //清空数据库：喜好数据库
	public           boolean      VIDEO_SERIVCE_CANCEL    = false;  //线程标识：视频伺服强制取消本次请求列表
	public           boolean      VIDEO_SERIVCE_STOP      = false;  //线程标识：视频伺服强制停止
	
	private Context       mContext            = null;
	private Thread        mThrScanLib         = null; //浏览媒体库线程
	private Thread        mThrScanGEO         = null; //启动时扫描地理位置
	private Thread        mThrImageService    = null; //图片加载伺服
	private Thread        mThrVideoService    = null; //视频加载伺服
	private Thread        mThrGeoService      = null; //获取地理信息服务
	private Thread        mThrFavoriteService = null; //喜好数据库扫描服务
	private List<Element> mRequestImageList   = null;
	private List<Element> mRequestVideoList   = null;
	private List<ElementList>  mSortedList    = null; //地理位置排序临时表
	
	private OnDataScanCompletedListener mOnDataScanCompletedListener = null;

	public AsyncFileProvider(Context context) {
		mContext = context;
		mRequestImageList = new ArrayList<Element>();
		mRequestVideoList = new ArrayList<Element>();
	}

	/**
	 * 扫描系统媒体库
	 */
	public void scanMediaLib(){
		mThrScanLib = new Thread(initScanLib());
		mThrScanLib.setName("mThrScanLib");
		mThrScanLib.start();
	}
	
	@Deprecated
	public void scanMediaLib(String rootDir){
		mThrScanLib = new Thread(initScanLib());
		mThrScanLib.setName("mThrScanLib(String rootDir");
		mThrScanLib.start();
	}
	
	/**
	 * 扫描全部GEO信息
	 */
	public void scanGEOLib(){
		mThrScanGEO = new Thread(initScanGEOLib(null, 0));
		mThrScanGEO.setName("mThrScanGEO");
		mThrScanGEO.start();
	}
	
	/**
	 * 扫描全部GEO信息
	 * @param processHandler 发送回调时应被调用的Handler
	 * @param msgWhat 发送回调时送出的信息标识
	 */
	public void scanGEOLib(Handler processHandler, int msgWhat){
		mThrScanGEO = new Thread(initScanGEOLib(processHandler, msgWhat));
		mThrScanGEO.setName("mThrScanGEO");
		mThrScanGEO.start();
	}

	/**
	 * 启动图片加载伺服
	 */
	public void launchImageLoadService(){
		if(mThrImageService == null){
			mThrImageService = new Thread(imageLoadService());
			mThrImageService.setName("mThrImageService");
		}
		
		if(!mThrImageService.isAlive()){
			try{
				mThrImageService.start();
			}catch (IllegalThreadStateException exp) {
				//不做其余操作
				stopImageLoadService();
				launchImageLoadService();
			}
		}
	}
	
	/**
	 * 启动视频加载伺服
	 */
	public void launchVideoLoadService(){
		if(mThrVideoService == null){
			mThrVideoService = new Thread(videoLoadService());
			mThrVideoService.setName("mThrVideoService");
		}
		
		if(!mThrVideoService.isAlive()){
			try{
				mThrVideoService.start();
			}catch (IllegalThreadStateException exp) {
				//不做其余操作
			}
		}
	}
	
	/**
	 * 启动地理位置排序服务
	 */
	public void launchParserGeoService(List<ElementList> list,CSStaticData.LIST_TYPE type){
		if (mThrGeoService == null) {
			mThrGeoService = new Thread(parserGeoService(list,type));
			mThrGeoService.setName("mThrGeoService");
		}
		
		if(!mThrGeoService.isAlive()){
			mThrGeoService.start();
		}
	}
	
	/**
	 * 启动喜好数据库扫描服务
	 */
	public void launchScanFavoriteDBSerivce(){
		if(mThrFavoriteService == null){
			mThrFavoriteService = new Thread(scanFavoriteDBRunnable());
			mThrFavoriteService.setName("mThrFavoriteService");
		}
		
		if(!mThrFavoriteService.isAlive()){
			mThrFavoriteService.start();
		}
	}
	
	/**
	 * 中断图片加载伺服
	 */
	public void interruptImageLoadService(){
		if(mThrImageService != null && mThrImageService.isAlive()){
			mThrImageService.interrupt();
			try{
//				mThrImageService.stop();
			}catch (UnsupportedOperationException exp) {
				exp.printStackTrace();
			}
		}
	}
	
	/**
	 * 中断视频加载伺服
	 */
	public void interruptVideoLoadService(){
		if(mThrVideoService != null && mThrVideoService.isAlive()){
			mThrVideoService.interrupt();
			try{
//				mThrVideoService.stop();
			}catch (UnsupportedOperationException exp) {
				exp.printStackTrace();
			}
		}
	}
	
	/**
	 * 中断地理位置排序伺服
	 */
	public void interruptParserGeoService(){
		if(mThrGeoService != null && mThrGeoService.isAlive()){
			mThrGeoService.interrupt();
			try{
//				mThrGeoServivce.stop();
			}catch (UnsupportedOperationException exp) {
				exp.printStackTrace();
			}
			mThrGeoService = null; //使下次能快速启动这个排序伺服
		}
	}
	
	/**
	 * 中断选择模式恢复服务
	 */
	public void interruptSelectionModeRecoverySerivce(){
		
	}
	
	/**
	 * 停止图片加载伺服
	 */
	public void stopImageLoadService(){
		interruptImageLoadService();
		if(mThrVideoService != null){
			try {
				mThrVideoService.stop();
				mThrVideoService = null;
				System.gc();
			} catch (UnsupportedOperationException exp) {
				mThrVideoService = null;
			}
		}
	}

	/**
	 * 停止视频加载伺服
	 */
	public void stopVideoLoadService(){
		interruptVideoLoadService();
		if(mThrImageService != null){
			try{
				mThrImageService.stop();
				mThrImageService = null;
				System.gc();
			} catch (UnsupportedOperationException exp) {
				mThrImageService = null;
			}
		}
	}

	/**
	 * 停止地理位置排序伺服
	 */
	public void stopParserGeoService(){
		interruptParserGeoService();
		if(mThrGeoService != null){
			try{
				mThrGeoService.stop();
				mThrGeoService = null;
				System.gc();
			} catch (UnsupportedOperationException exp) {
				mThrGeoService = null;
			}
		}
	}
	
	/**
	 * 停止选择模式恢复服务
	 */
	public void stopSelectionModeRecoverySerivce(){
		
	}
	
	/**
	 * 重命名文件
	 * 不支持通配符
	 * @param resFileName [drive:][path]filename 原文件
	 * @param desFileName filename 目标文件名
	 * @return 是否操作成功
	 */
	public boolean rename(String resFileName, String desFileName){
		return false;
	}

	/**
	 * 删除文件
	 * @param fileList 要删除的文件列表
	 * @param afterExit 是否在退出后才执行
	 * @return
	 */
	public boolean deteleFiles(List<String> fileList, boolean afterExit, boolean ignoreOpenGL, int owner){
		Thread mThrDelService = new Thread(deleteFilesRunnable(fileList, afterExit, ignoreOpenGL, owner));
		mThrDelService.setName("mThrDelService");
		mThrDelService.start();
		
		return false;
	}
	
	/**
	 * 编辑文件
	 * @param editFileName
	 */
	public synchronized void editFile(String editFileName){
		if(WiGalleryOpenGLRenderer.m_data_manager != null){
			Element elem = WiGalleryOpenGLRenderer.m_data_manager.Get(editFileName);
			if(elem != null){
				WiGalleryOpenGLRenderer.m_data_manager.setRequestLoadFileList(elem);
			}
		}
	}

	/**
	 * 移动文件，每次调用都会新启线程，且操作不可取消
	 * @param fileList 待移动的文件
	 * @param desDir 目标目录
	 */
	public void moveTo(List<String> fileList, String desDir, boolean ignoreOpenGL, int owner) {
		Thread thrMoveFiles = new Thread(moveFilesRunnable(fileList, desDir, ignoreOpenGL, owner));
		thrMoveFiles.setName("thrMoveFiles");
		thrMoveFiles.start();
	}
	
	/**
	 * 检查WIFI的连通性
	 */
	public void checkWifiConnection(){
		Thread thrWifiCheck = new Thread(checkInternetRunnable());
		thrWifiCheck.setName("thrWIFICheck");
		thrWifiCheck.start();
	}

	/**
	 * 把选中的文件添加到喜好数据库中
	 * @param fileName
	 */
	public void setFavoriteToDB(List<String> fileName, int owner){
		Thread thrSetFavorite = new Thread(setFavoriteToDBRunnable(fileName, owner));
		thrSetFavorite.setName("thrSetFavorite");
		thrSetFavorite.start();
	}

	/**
	 * 把指定的文件从喜好数据库中移除
	 * @param fileName
	 */
	public void removeFavoriteFromDB(List<String> fileName, int owner){
		Thread thrRemoveFavorite = new Thread(removeFavoriteFromDBRunnable(fileName, owner));
		thrRemoveFavorite.setName("thrRemoveFavorite");
		thrRemoveFavorite.start();
	}

	/**
	 * 向Gallery中添加一个文件
	 * @param file
	 */
	public void addNewFile(String file){
		File                     fin       = null;
		Element                  fileElem  = null;
		String                   date      = "";
		Calendar                 calendar  = Calendar.getInstance();
		CSStaticData.MEDIA_TYPE  mediaType = CSStaticData.MEDIA_TYPE.NORMAL_IMAGE;
		
		if(file == null || file.equals("")){
			return;
		}
		
		//解析文件
		fin = new File(file);
		if(!fin.exists() || !fin.isFile()){
			return;
		}
		if(!FileTypeHelper.isSupportedFile(file)){
			return;
		}
		calendar.setTimeInMillis(fin.lastModified());
		
		//解析为Element
		if(FileTypeHelper.isImageFile(file)){
			if(FileTypeHelper.isStereoImageFile(file)){
				mediaType = CSStaticData.MEDIA_TYPE.STOERE_IMAGE;
			}else{
				mediaType = CSStaticData.MEDIA_TYPE.NORMAL_IMAGE;
			}
		}
		if(FileTypeHelper.isVideoFile(file)){
			if(FileTypeHelper.isStereoVideoFile(file)){
				mediaType = CSStaticData.MEDIA_TYPE.STOERE_VIDEO;
			}else{
				mediaType = CSStaticData.MEDIA_TYPE.NORMAL_VIDEO;
			}
		}
		
		fileElem = new Element(mediaType,
				0, 0,
				CSStaticData.choosedTextureID,
				CSStaticData.notChoosedTextureID,
				file,
				String.format("%04d %02d %02d %02d %02d %02d", 
						calendar.get(Calendar.YEAR),
						calendar.get(Calendar.MONTH) + 1,
						calendar.get(Calendar.DAY_OF_MONTH),
						calendar.get(Calendar.HOUR),
						calendar.get(Calendar.MINUTE),
						calendar.get(Calendar.SECOND)),
				Double.NaN, Double.NaN);
		
		//添加到主文件列表
		if(WiGalleryOpenGLRenderer.m_data_manager != null){
			WiGalleryOpenGLRenderer.m_data_manager.Add(fileElem);
		}	
	}

	/**
	 * 强制停止视频伺服
	 */
	public void forceStopVideoSerivce(){
		VIDEO_SERIVCE_STOP = true;
	}
	
	/**
	 * 忽略一次视频缩图请求
	 * 这会取消掉本次视频缩图请求中的所有请求文件
	 */
	public void ignoreVideoRequestSerivceOnce(){
		VIDEO_SERIVCE_CANCEL = true;
	}
	
	/***********************************************/
	/*                    内部方法                                              */
	/***********************************************/
	/**
	 * 生成扫描系统媒体数据库的方法
	 * @return
	 */
	private Runnable initScanLib() {
		Runnable runnable = new Runnable() {

			long lStart = 0, //Load起始时间
				 lEnd   = 0; //Load结束时间
			int  lCount = 0; //记录数量
			
			@Override
			public void run() {
				lStart = System.currentTimeMillis();
				String[] imageColumnsStr = { //图片待查字段
						MediaStore.Images.Media._ID,
						MediaStore.Images.Media.DATA, 
						MediaStore.Images.Media.DISPLAY_NAME, 
						MediaStore.Images.Media.DATE_MODIFIED,
						MediaStore.Images.Media.LATITUDE,
						MediaStore.Images.Media.LONGITUDE,
						MediaStore.Images.Media.MIME_TYPE
				},

				videoColumnsStr = { //视频待查字段
						MediaStore.Video.Media._ID,
						MediaStore.Video.Media.DATA,
						MediaStore.Video.Media.DISPLAY_NAME,
						MediaStore.Video.Media.DATE_MODIFIED,
						MediaStore.Video.Media.LATITUDE,
						MediaStore.Video.Media.LONGITUDE,
						MediaStore.Video.Media.MIME_TYPE
				};

				String   imageWhereStr = MediaStore.Images.Media.DATA + " LIKE ?", //图片待查条件
						 videoWhereStr = MediaStore.Video.Media.DATA + " LIKE ?";  //视频待查条件
				
				String[] imageWhereArg = new String[]{CSStaticData.TMP_DIR + "%"},
						 videoWhereArg = new String[]{CSStaticData.TMP_DIR + "%"};
				
				if(CSStaticData.DEMO){
//					imageWhereArg = CSStaticData.MEDIA_SCAN_DIR;
//					for(int i = 0; i < CSStaticData.MEDIA_SCAN_DIR.length; i++){
//						imageWhereArg[i] = imageWhereArg[i] + "%";
//					}
//					if(imageWhereArg[CSStaticData.MEDIA_SCAN_DIR.length - 1].length() >= 4){
//						imageWhereArg[CSStaticData.MEDIA_SCAN_DIR.length - 1] 
//								= imageWhereArg[CSStaticData.MEDIA_SCAN_DIR.length - 1]
//										.substring(0, imageWhereArg[CSStaticData.MEDIA_SCAN_DIR.length - 1].length() - 4);
//					}
//					videoWhereArg = imageWhereArg;
					imageWhereStr = MediaStore.Images.Media.DATA + " LIKE ? OR " + MediaStore.Images.Media.DATA + " LIKE ?";
					videoWhereStr = MediaStore.Video.Media.DATA + " LIKE ? OR " + MediaStore.Video.Media.DATA + " LIKE ?";
					imageWhereArg = new String[]{CSStaticData.TMP_INT_DIR + "%", CSStaticData.TMP_EXT_DIR + "%"};
					videoWhereArg = new String[]{CSStaticData.TMP_INT_DIR + "%", CSStaticData.TMP_EXT_DIR + "%"};
				}
				
				CSStaticData.LOAD_COMPLETED = false;
				CSStaticData.LOAD_STARTED = true;
				
				if(CSStaticData.DEMO){
					if(WiGalleryOpenGLRenderer.m_data_manager != null && WiGalleryOpenGLRenderer.m_data_manager.Size() > 0){
						if(WiGalleryOpenGLRenderer.m_on_data_list_listener != null){
							WiGalleryOpenGLRenderer.m_on_data_list_listener.onDataFillCompleted();
						}else{
							Log.w(TAG, "[scanMediaLib]render is null");
						}
						if(mOnDataScanCompletedListener != null){
							mOnDataScanCompletedListener.onDataScanCompletedListener();
						}
						CSStaticData.LOAD_COMPLETED = true;
						CSStaticData.LOAD_STARTED = false;
						setFavoriteSettingToMainList();
						return;
					}
				}

				String   imageOrderStr = MediaStore.Images.Media.DATE_MODIFIED + " DESC", //图片排序方式
						 videoOrderStr = MediaStore.Video.Media.DATE_MODIFIED + " DESC";  //视频排序方式
				
				CSStaticData.MEDIA_TYPE  mediaType = CSStaticData.MEDIA_TYPE.NORMAL_IMAGE;
				Element                  fileElem  = null;
				Cursor                   cursor    = null;
				ContentResolver          resolver  = null;
				Calendar                 calendar  = Calendar.getInstance();


				if(mContext == null){
					return;
				}

				/* [按日期降序查询相关路径下的文件]
				 * SELECT _data, _display, date_modified, latitude, longitude 
				 * FROM MeidaStore.tab 
				 * WHERE _data = '?%' 
				 * ORDER BY date_modified DESC
				 */
				resolver = mContext.getContentResolver();
				//扫描图片数据库
				{
					//内部存储器
					try{
						//清空主链表
						WiGalleryOpenGLRenderer.m_data_manager.clearMainList();
						
						cursor   = resolver.query(MediaStore.Images.Media.INTERNAL_CONTENT_URI, imageColumnsStr, imageWhereStr, imageWhereArg, imageOrderStr);
						if(CSStaticData.DEBUG){
							int count = 0;
							String colset = "";
							if(cursor != null){
								count = cursor.getCount();
								lCount += count;
								for (int i = 0; i < cursor.getColumnNames().length; i++) {
									colset += cursor.getColumnNames()[i] + "  ";
								}
								Log.w(TAG,"[scanMediaLib][Inner Image]记录数：" + count);
								Log.w(TAG,"[scanMediaLib][Inner Image]字段集：" + colset);
							}
						}
						if(cursor != null && cursor.moveToFirst()){
							do{
								if(FileTypeHelper.isStereoImageFile(cursor.getString(1))){
									mediaType = CSStaticData.MEDIA_TYPE.STOERE_IMAGE;
								}else{
									mediaType = CSStaticData.MEDIA_TYPE.NORMAL_IMAGE;
								}
								calendar.setTimeInMillis(cursor.getInt(3) * 1000L);          //getInt(2)的结果是秒，所以乘以1000换算成毫秒
								Double[] gps = new Double[]{cursor.getDouble(4), cursor.getDouble(5)};
								if(gps[0] == 0.0f){
									gps[0] = Double.NaN;
								}
								if(gps[1] == 0.0f){
									gps[1] = Double.NaN;
								}
								fileElem = new Element(mediaType,                            //FileType
										0,                                                   //LeftImageTextureID
										0,                                                   //RightImageTextureID
										CSStaticData.choosedTextureID,                       //ChoosedTextureID
										CSStaticData.notChoosedTextureID,                    //notChoosedTextureID
										cursor.getString(1),                                 //File Path
										String.format("%04d %02d %02d %02d %02d %02d", 
												calendar.get(Calendar.YEAR),
												calendar.get(Calendar.MONTH) + 1,
												calendar.get(Calendar.DAY_OF_MONTH),
												calendar.get(Calendar.HOUR),
												calendar.get(Calendar.MINUTE),
												calendar.get(Calendar.SECOND)),              //Date Modified
										gps[0],                                              //Latitude
										gps[1]);                                             //Longitude
								fileElem.m_id = cursor.getLong(0);                           //ID
								WiGalleryOpenGLRenderer.m_data_manager.Add(fileElem);
								if(CSStaticData.DEBUG){
									Log.w(TAG,"[scanMediaLib][Inner Image]" + cursor.getString(1) + ", " 
											+ String.valueOf(cursor.getInt(3)) + ", "
											+ gps[0] + ", "
											+ gps[1]);
								}
							}while(cursor.moveToNext());

							cursor.close();
						}
					}catch (SQLiteBindOrColumnIndexOutOfRangeException e) {
						e.printStackTrace();
					}catch (SQLiteException e) {
						e.printStackTrace();
					}
					//外部存储器
					try{
						
						cursor   = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageColumnsStr, imageWhereStr, imageWhereArg, imageOrderStr);
						if(CSStaticData.DEBUG){
							int count = 0;
							String colset = "";
							if(cursor != null){
								count = cursor.getCount();
								lCount += count;
								for (int i = 0; i < cursor.getColumnNames().length; i++) {
									colset += cursor.getColumnNames()[i] + "  ";
								}
								Log.w(TAG,"[scanMediaLib][Outter Image]记录数：" + count);
								Log.w(TAG,"[scanMediaLib][Outter Image]字段集：" + colset);
							}
						}
						if(cursor != null && cursor.moveToFirst()){
							do{
								if(FileTypeHelper.isStereoImageFile(cursor.getString(1))){
									mediaType = CSStaticData.MEDIA_TYPE.STOERE_IMAGE;
								}else{
									mediaType = CSStaticData.MEDIA_TYPE.NORMAL_IMAGE;
								}
								calendar.setTimeInMillis(cursor.getInt(3) * 1000L); 
								Double[] gps = new Double[]{cursor.getDouble(4), cursor.getDouble(5)};
								if(gps[0] == 0.0f){
									gps[0] = Double.NaN;
								}
								if(gps[1] == 0.0f){
									gps[1] = Double.NaN;
								}
								fileElem = new Element(mediaType, 
										0, 
										0, 
										CSStaticData.choosedTextureID, 
										CSStaticData.notChoosedTextureID, 
										cursor.getString(1),
										String.format("%04d %02d %02d %02d %02d %02d", 
												calendar.get(Calendar.YEAR),
												calendar.get(Calendar.MONTH) + 1,
												calendar.get(Calendar.DAY_OF_MONTH),
												calendar.get(Calendar.HOUR),
												calendar.get(Calendar.MINUTE),
												calendar.get(Calendar.SECOND)), 
										gps[0], 
										gps[1]);
								fileElem.m_id = cursor.getLong(0);
								WiGalleryOpenGLRenderer.m_data_manager.Add(fileElem);
								if(CSStaticData.DEBUG){
									Log.w(TAG,"[scanMediaLib][Outter Image]" + cursor.getString(1) + ", " 
											+ String.valueOf(cursor.getInt(3)) + ", "
											+ gps[0] + ", "
											+ gps[1]);
								}
							}while(cursor.moveToNext());
							cursor.close();
						}
					}catch (SQLiteBindOrColumnIndexOutOfRangeException e) {
						e.printStackTrace();
					}catch (SQLiteException e) {
						e.printStackTrace();
					}
				}
				//扫描视频数据库
				{
					//内部存储器
					try{
						cursor   = resolver.query(MediaStore.Video.Media.INTERNAL_CONTENT_URI, videoColumnsStr, videoWhereStr, videoWhereArg, videoOrderStr);
						if(CSStaticData.DEBUG){
							int count = 0;
							if(cursor != null){
								count = cursor.getCount();
								lCount += count;
								Log.w(TAG,"[scanMediaLib][Inner Video]记录数：" + count);
								Log.w(TAG,"[scanMediaLib][Inner Video]字段集：" + cursor.getColumnNames());
							}
						}
						if(cursor != null && cursor.moveToFirst()){
							do{
								if(FileTypeHelper.isStereoVideoFile(cursor.getString(6))){
									mediaType = CSStaticData.MEDIA_TYPE.STOERE_VIDEO;
								}else{
									mediaType = CSStaticData.MEDIA_TYPE.NORMAL_VIDEO;
								}
								calendar.setTimeInMillis(cursor.getInt(3) * 1000L);
								Double[] gps = new Double[]{cursor.getDouble(4), cursor.getDouble(5)};
								if(gps[0] == 0.0f){
									gps[0] = Double.NaN;
								}
								if(gps[1] == 0.0f){
									gps[1] = Double.NaN;
								}
								fileElem = new Element(mediaType, 
										0, 
										0, 
										CSStaticData.choosedTextureID, 
										CSStaticData.notChoosedTextureID, 
										cursor.getString(1),
										String.format("%04d %02d %02d %02d %02d %02d", 
												calendar.get(Calendar.YEAR),
												calendar.get(Calendar.MONTH) + 1,
												calendar.get(Calendar.DAY_OF_MONTH),
												calendar.get(Calendar.HOUR),
												calendar.get(Calendar.MINUTE),
												calendar.get(Calendar.SECOND)),
										gps[0], 
										gps[1]);
								fileElem.m_id = cursor.getLong(0);
								WiGalleryOpenGLRenderer.m_data_manager.Add(fileElem);
								if(CSStaticData.DEBUG){
									Log.w(TAG,"[scanMediaLib][Inner Video]" + cursor.getString(1) + ", " 
											+ String.valueOf(cursor.getInt(3)) + ", "
											+ gps[0] + ", "
											+ gps[1]);
								}
							}while(cursor.moveToNext());
							cursor.close();
						}
					}catch (SQLiteBindOrColumnIndexOutOfRangeException e) {
						e.printStackTrace();
					}catch (SQLiteException e) {
						e.printStackTrace();
					}
					//外部存储器
					try{
						cursor   = resolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoColumnsStr, videoWhereStr, videoWhereArg, videoOrderStr);
						if(CSStaticData.DEBUG){
							int count = 0;
							if(cursor != null){
								count = cursor.getCount();
								lCount += count;
								Log.w(TAG,"[scanMediaLib][Outter Video]记录数：" + count);
								Log.w(TAG,"[scanMediaLib][Outter Video]字段集：" + cursor.getColumnNames());
							}
						}
						if(cursor != null && cursor.moveToFirst()){
							do{
								if(FileTypeHelper.isStereoVideoFile(cursor.getString(6))){
									mediaType = CSStaticData.MEDIA_TYPE.STOERE_VIDEO;
								}else{
									mediaType = CSStaticData.MEDIA_TYPE.NORMAL_VIDEO;
								}
								calendar.setTimeInMillis(cursor.getInt(3) * 1000L);
								Double[] gps = new Double[]{cursor.getDouble(4), cursor.getDouble(5)};
								if(gps[0] == 0.0f){
									gps[0] = Double.NaN;
								}
								if(gps[1] == 0.0f){
									gps[1] = Double.NaN;
								}
								fileElem = new Element(mediaType, 
										0, 
										0, 
										CSStaticData.choosedTextureID, 
										CSStaticData.notChoosedTextureID, 
										cursor.getString(1),
										String.format("%04d %02d %02d %02d %02d %02d", 
												calendar.get(Calendar.YEAR),
												calendar.get(Calendar.MONTH) + 1,
												calendar.get(Calendar.DAY_OF_MONTH),
												calendar.get(Calendar.HOUR),
												calendar.get(Calendar.MINUTE),
												calendar.get(Calendar.SECOND)),
										gps[0], 
										gps[1]);
								fileElem.m_id = cursor.getLong(0);
								WiGalleryOpenGLRenderer.m_data_manager.Add(fileElem);
								if(CSStaticData.DEBUG){
									Log.w(TAG,"[scanMediaLib][Outter Video]" + cursor.getString(1) + ", " 
											+ String.valueOf(cursor.getInt(3)) + ", "
											+ gps[0] + ", "
											+ gps[1]);
								}
							}while(cursor.moveToNext());
							cursor.close();
						}
					}catch (SQLiteBindOrColumnIndexOutOfRangeException e) {
						e.printStackTrace();
					}catch (SQLiteException e) {
						e.printStackTrace();
					}
				}
				
				//排序主链表
				if(CSStaticData.DEBUG){
					Log.w(TAG, "[initScanLib]文件加载完毕，开始排序");
				}
				WiGalleryOpenGLRenderer.m_data_manager.sortMainList(CSStaticData.g_sort_order_mode);
				
				lEnd = System.currentTimeMillis();
				if(CSStaticData.DEBUG){
					Log.w(TAG, "[scanMediaLib]Load Time = " + (lEnd - lStart) + " ms");
					Log.w(TAG, "[scanMediaLib]Load Records = " + lCount);
				}
				
				//发送完毕回调
				if(WiGalleryOpenGLRenderer.m_on_data_list_listener != null){
					WiGalleryOpenGLRenderer.m_on_data_list_listener.onDataFillCompleted();
				}else{
					Log.w(TAG, "[scanMediaLib]render is null");
				}
				if(mOnDataScanCompletedListener != null){
					mOnDataScanCompletedListener.onDataScanCompletedListener();
				}
				CSStaticData.LOAD_COMPLETED = true;
				CSStaticData.LOAD_STARTED = false;
				
				if(CSStaticData.DEBUG){
					Log.w(TAG, "[scanMediaLib]开始扫描GEO信息");
				}
				WiGalleryOpenGLRenderer.mAsyncFileProvider.scanGEOLib();
				
				//根据数据库设置文件喜好
				if(CSStaticData.DEBUG){
					Log.w(TAG, "[initScanLib]文件排序完毕，开始加载喜好设置");
				}
				setFavoriteSettingToMainList();
				
				if(CSStaticData.DEBUG){
					Log.w(TAG, "[initScanLib]喜好设置加载完毕");
				}
			}
		};
		
		return runnable;
	}

	/**
	 * 扫描全部文件地理信息的方法
	 * @return
	 */
	private Runnable initScanGEOLib(final Handler processHandler, final int msgWhat){
		Runnable runnable = new Runnable() {
			int           curPtr       = 0; //当前进度
			int           tolPtr       = 0; //总进度
			int           batchNum     = 50; //一批查询的数量
			boolean       mKeepGetting = true; 
			List<Element> mSysMainList = new ArrayList<Element>(); //批量处理链表
			List<Element> mResultList  = null;
			
			@Override
			public void run() {
				
				{//方法体 
					//等待数据加载完毕
					while(!CSStaticData.LOAD_COMPLETED){
						try {
							Thread.sleep(100);
						} catch (InterruptedException exp) {
							exp.printStackTrace();
						}
					}
					
					//开始查询, 50个一批的查询，以提高查询效率
					while(mKeepGetting){
						int startPtr = 0,
							endPtr   = 0;
						
						if(mSysMainList == null){
							mSysMainList = new ArrayList<Element>();
						}
						mSysMainList.clear();
						
						tolPtr = WiGalleryOpenGLRenderer.m_data_manager.getMainListSize(); //随时监控，随时改变
						startPtr = curPtr;
						if(curPtr < tolPtr){
							for(int i = 0; i < batchNum; i++){
								//tolPtr = WiGalleryOpenGLRenderer.m_data_manager.getMainListSize(); //随时监控，随时改变
								if(curPtr < tolPtr){
									mSysMainList.add(WiGalleryOpenGLRenderer.m_data_manager.Get(i));
									curPtr ++;
								}else{
									mKeepGetting = false;
									break;
								}
							}
							endPtr = curPtr;
							
							//查询并回调
							mResultList = GeoParser.parserGeoBatch(mContext, mSysMainList, CSStaticData.LIST_TYPE.LIST_LOCATION_4, new BatchProcessCallBack() {
								
								@Override
								public void startProcess(int process, int max) {
									if(processHandler != null){
										Message msg = new Message();
										msg.what = msgWhat;
										msg.arg1 = process;
										processHandler.sendMessage(msg);
									}
								}
								
								@Override
								public void inProcess(int process, int max) {
									if(processHandler != null){
										Message msg = new Message();
										msg.what = msgWhat;
										msg.arg1 = process;
										processHandler.sendMessage(msg);
									}
								}
								
								@Override
								public void endProcess(int process, int max) {
									if(processHandler != null){
										Message msg = new Message();
										msg.what = msgWhat;
										msg.arg1 = process;
										processHandler.sendMessage(msg);
									}
								}
							});
							
							//回执给主链表
							for(int i = 0; i < batchNum; i++){
								if(i + startPtr < endPtr){
									WiGalleryOpenGLRenderer.m_data_manager.Get(i+startPtr).m_str_address
										= mResultList.get(i).m_str_address;
								}else{
									break;
								}
							}
						}
					}
				}
				Log.e("GEOCacheHelper", "GPS(29.7123, 107.4090) = " + GeoParser.parserGeo(mContext, 29.7123, 107.4090, CSStaticData.LIST_TYPE.LIST_LOCATION_4));
				Log.e("GEOCacheHelper", "GPS(30.5833, 114.3000) = " + GeoParser.parserGeo(mContext, 30.5833, 114.3000, CSStaticData.LIST_TYPE.LIST_LOCATION_4));
				Log.e("GEOCacheHelper", "GPS(45.4166,  75.7166) = " + GeoParser.parserGeo(mContext, 45.4166,  75.7166, CSStaticData.LIST_TYPE.LIST_LOCATION_4));
				Log.e("GEOCacheHelper", "GPS(25.0333, 121.6333) = " + GeoParser.parserGeo(mContext, 25.0333, 121.6333, CSStaticData.LIST_TYPE.LIST_LOCATION_4));
			}
		};
		
		return runnable;
	}
	
	/**
	 * 图片缩略图加载线程的方法
	 * @return
	 */
	private Runnable imageLoadService(){
		Runnable runnable   = new Runnable() {
			long          sleepMsec   = 0;
			int           sleepNsec   = 0;
			List<Element> requestList = null;
			Options       options     = new Options();
			boolean       keepRuning  = true;
			Bitmap[]      srcBmp      = new Bitmap[2];
			
			@Override
			public void run() {
				
				if(CSStaticData.DEBUG){
					Log.w(TAG, "[ImageLoadService]图片贴图加载私服启动");
				}
				
				options.outWidth  = CSStaticData.THUMB_SIZE;
				options.outHeight = CSStaticData.THUMB_SIZE;
				mRequestImageList = new ArrayList<Element>();
				while(keepRuning){
					
					{//线程控制区间
						if(Thread.interrupted()){
							keepRuning = false;
						}
					}
					
					{//执行区间
						try {
							if(CPU_DEBUG && CSStaticData.DEBUG){
								Log.w("CPU_Trace", "Wicamera3D II CPU: " + Integer.valueOf(SystemInfo.getCpuWithApplicationName("com.wistron.swpc.wicamera3dii")) + "%");
							}
							
							// 读取请求列表
							if(WiGalleryOpenGLRenderer.m_data_manager == null){
								Thread.sleep(sleepMsec, sleepNsec);
								if(CSStaticData.DEBUG && Thread_DEBUG){
									Log.w(TAG, "[ImageLoadService]等待主数据列表创建，线程已休眠：  " + sleepMsec + "ms, " + sleepNsec + "ns" );
								}
								continue;
							}
							
							splitRequestFileList(WiGalleryOpenGLRenderer.m_data_manager.getRequestLoadFileList());
							if(requestList != null)
								requestList.clear();
							requestList = new ArrayList<Element>(mRequestImageList);
							mRequestImageList.clear();

							// 搜索文件
							int requestListSize = requestList.size();
							if(requestListSize == 0){
								sleepMsec = 50;
								sleepNsec = 0;
							}else{
								sleepMsec = 0;
								sleepNsec = 0;
							}
							for (int i = 0; i < requestListSize; i++) {
								Element requestListItem     = requestList.get(i);
								if(requestListItem == null){
									continue;
								}
								String  requestListItemName = requestListItem.getName();  
								File    file                = new File(requestListItemName.toString());

								if (FileTypeHelper.getFileType(requestListItemName.toString()) == FileTypeHelper.TYPE_IMAGE_2D) {
									if(!file.exists()){
										if(CSStaticData.DEBUG){
											Log.w(TAG, "[ImageLoadService]追加文件贴图： " + requestListItemName.toString() + "失败，文件不存在！");
										}
										continue;
									}

									if(CSStaticData.THUMB_FROM_MEDIA_DB){
										srcBmp[0] = android.provider.MediaStore.Images.Thumbnails.getThumbnail(mContext.getContentResolver(), requestListItem.m_id, android.provider.MediaStore.Images.Thumbnails.MINI_KIND, options);
									}else{
										srcBmp[0] = FileOperation.fitSizeImg(
												requestListItemName.toString(),
												CSStaticData.THUMB_SIZE);
									}
									if(srcBmp[0] != null){
										try{
											srcBmp[0] = FileOperation.cutBitmapWithProportion(srcBmp[0],
													CSStaticData.THUMB_SIZE,
													CSStaticData.THUMB_SIZE, true);
											if(CSStaticData.ENABLE_THUMB_ROTATE){
												srcBmp[0] = FileOperation.ratoteBitmapWithExifTag(srcBmp[0], requestListItemName);
											}
										}catch(Exception exp){
											exp.printStackTrace();
											srcBmp[1] = srcBmp[0] = null;
										}catch (Error exp) {
											exp.printStackTrace();
											srcBmp[1] = srcBmp[0] = null;
										}
										srcBmp[1] = null;
									}else{
										srcBmp[1] = srcBmp[0] = null;
									}

									if (requestListItem != null) {
										requestListItem.setBitmap(srcBmp[0], null);
										WiGalleryOpenGLRenderer.m_data_manager.AddNeedLoadTextureFile(requestListItem);
										if(CSStaticData.DEBUG){
											if(srcBmp[0] != null){
												Log.w(TAG, "[ImageLoadService]追加文件贴图： " + requestListItemName.toString());
											}else{
												Log.w(TAG, "[ImageLoadService]文件贴图： " + requestListItemName.toString() + "加载失败");
											}
										}
									}
								}
								if (FileTypeHelper.getFileType(requestListItemName.toString()) == FileTypeHelper.TYPE_IMAGE_3DJ) {
									if(!file.exists()){
										if(CSStaticData.DEBUG){
											Log.w(TAG, "[ImageLoadService]追加文件贴图： " + requestListItemName.toString() + "失败，文件不存在！");
										}
										continue;
									}
									try{
										
									if(CSStaticData.THUMB_FROM_MEDIA_DB){
										srcBmp[0] = android.provider.MediaStore.Images.Thumbnails.getThumbnail(mContext.getContentResolver(), requestListItem.m_id, android.provider.MediaStore.Images.Thumbnails.MINI_KIND, options);
									}else{
										srcBmp[0] = FileOperation.fitSizeImg(
												requestListItemName.toString(),
												CSStaticData.THUMB_SIZE);
									}
									
									if(CSStaticData.ENABLE_THUMB_ROTATE){
										srcBmp[0] = FileOperation.ratoteBitmapWithExifTag(srcBmp[0], requestListItemName);
									}
									srcBmp = FileTypeHelper.image3DTo2D(
											srcBmp[0],
											CSStaticData.THUMB_SIZE,
											CSStaticData.THUMB_SIZE);
									}catch (Exception exp) {
										exp.printStackTrace();
										srcBmp[1] = srcBmp[0] = null;
									}catch (Error exp) {
										exp.printStackTrace();
										srcBmp[1] = srcBmp[0] = null;
									}

									if (requestListItem != null) {
										requestListItem.setBitmap(srcBmp[0], srcBmp[1]);
										WiGalleryOpenGLRenderer.m_data_manager.AddNeedLoadTextureFile(requestListItem);
										if(CSStaticData.DEBUG){
											if(srcBmp[0] != null && srcBmp[1] != null){
												Log.w(TAG, "[ImageLoadService]追加文件贴图： " + requestListItemName.toString());
											}else if(srcBmp[0] == null && srcBmp[1] == null){
												Log.w(TAG, "[ImageLoadService]文件贴图： " + requestListItemName.toString() + "加载失败");
											}else{
												Log.w(TAG, "[ImageLoadService]文件贴图： " + requestListItemName.toString() + "部分加载成功");
											}
										}
									}

								}
								if (FileTypeHelper.getFileType(requestListItemName.toString()) == FileTypeHelper.TYPE_IMAGE_3DS) {
									if(!file.exists()){
										if(CSStaticData.DEBUG){
											Log.w(TAG, "[ImageLoadService]追加文件贴图： " + requestListItemName.toString() + "失败，文件不存在！");
										}
										continue;
									}
									try{
										srcBmp = MPOFileStreamParser.decodeFile(requestListItemName.toString(), options);
										srcBmp = FileTypeHelper.scaleMPOImage(srcBmp, CSStaticData.THUMB_SIZE, CSStaticData.THUMB_SIZE);
										if(CSStaticData.ENABLE_THUMB_ROTATE){
											srcBmp = FileOperation.ratoteBitmapWithExifTag(srcBmp, requestListItemName);
										}
									}catch (Exception exp) {
										exp.printStackTrace();
										if(srcBmp != null && srcBmp.length == 1){
											srcBmp[0] = null;
										}else if(srcBmp != null && srcBmp.length == 2){
											srcBmp[1] = srcBmp[0] = null;
										}else{
											srcBmp = new Bitmap[2];
										}
									}catch (Error exp) {
										exp.printStackTrace();
										srcBmp = new Bitmap[2];
									}

									if (requestListItem != null) {
										requestListItem.setBitmap(srcBmp[0], srcBmp[1]);
										WiGalleryOpenGLRenderer.m_data_manager.AddNeedLoadTextureFile(requestListItem);
										if(CSStaticData.DEBUG){
											if(srcBmp[0] != null && srcBmp[1] != null){
												Log.w(TAG, "[ImageLoadService]追加文件贴图： " + requestListItemName.toString());
											}else if(srcBmp[0] == null && srcBmp[1] == null){
												Log.w(TAG, "[ImageLoadService]文件贴图： " + requestListItemName.toString() + "加载失败");
											}else{
												Log.w(TAG, "[ImageLoadService]文件贴图： " + requestListItemName.toString() + "部分加载成功");
											}
										}
									}

								}
								srcBmp[0] = srcBmp[1] = null;
							}
							mRequestImageList.clear();

							Thread.sleep(sleepMsec, sleepNsec);
							if(CSStaticData.DEBUG && Thread_DEBUG){
								Log.w(TAG, "[ImageLoadService]线程已休眠：  " + sleepMsec + "ms, " + sleepNsec + "ns" );
							}
						} catch (InterruptedException exp) {
							exp.printStackTrace();
							keepRuning = false;
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
				}
				
				if(CSStaticData.DEBUG){
					Log.w(TAG, "[ImageLoadService]图片贴图加载私服停止");
				}
				
			}
		};
		
		return runnable;
	}

	/**
	 * 视频缩略图加载线程的方法
	 * @return
	 */
	private Runnable videoLoadService(){
		Runnable runnable = new Runnable() {
			long          sleepMsec   = 0;
			int           sleepNsec   = 0;
			List<Element> requestList = null;
			Options       options     = new Options();
			boolean       keepRuning  = true;
			Bitmap        srcBmp      = null;

			@Override
			public void run() {
				
				if(CSStaticData.DEBUG){
					Log.w(TAG, "[VideoLoadService]视频贴图加载私服启动");
				}
				
				mRequestVideoList = new ArrayList<Element>();
				while(keepRuning){

					{//线程控制区间
						if(Thread.interrupted()){
							keepRuning = false;
						}
					}

					{//执行区间
						try {
							if(CPU_DEBUG && CSStaticData.DEBUG){
								Log.w("CPU_Trace", "Wicamera3D II CPU: " + Integer.valueOf(SystemInfo.getCpuWithApplicationName("com.wistron.swpc.wicamera3dii")) + "%");
							}
							
							// 读取请求列表
							if(WiGalleryOpenGLRenderer.m_data_manager == null){
								Thread.sleep(sleepMsec, sleepNsec);
								if(CSStaticData.DEBUG && Thread_DEBUG){
									Log.w(TAG, "[VideoLoadService]等待主数据列表创建，线程已休眠：  " + sleepMsec + "ms, " + sleepNsec + "ns" );
								}
								continue;
							}
							
							//splitRequestFileList(WiGalleryOpenGLRenderer.m_data_manager.getRequestLoadFileList());
							if(requestList != null)
								requestList.clear();
							requestList = new ArrayList<Element>(mRequestVideoList);
							mRequestVideoList.clear();

							// 搜索文件
							int requestListSize = requestList.size();
							if(requestListSize == 0){
								sleepMsec = 50;
								sleepNsec = 0;
							}else{
								sleepMsec = 0;
								sleepNsec = 0;
							}
							for (int i = 0; i < requestListSize; i++) {
								Element requestListItem     = requestList.get(i);
								if(requestListItem == null){
									continue;
								}
								String  requestListItemName = requestListItem.getName();  
								File    file                = new File(requestListItemName.toString());
								
								{//视频私服强制停止
									if(VIDEO_SERIVCE_STOP){
										keepRuning = false; //直接停止线程
										break;
									}
									if(VIDEO_SERIVCE_CANCEL){
										VIDEO_SERIVCE_CANCEL = false; //只停止响应请求一次
										break;
									}
								}
								
								if (FileTypeHelper.getFileType(requestListItemName.toString()) == FileTypeHelper.TYPE_VIDEO_2D) {
									if(!file.exists()){
										if(CSStaticData.DEBUG){
											Log.w(TAG, "[VideoLoadService]追加文件贴图： " + requestListItemName.toString() + "失败，文件不存在！");
										}
										continue;
									}
									srcBmp = ThumbnailUtils.createVideoThumbnail(requestListItemName, Thumbnails.MINI_KIND);
									
									if(srcBmp != null){
										try{
											srcBmp = FileOperation.cutBitmapWithProportion(
													srcBmp, 
													CSStaticData.THUMB_SIZE,
													CSStaticData.THUMB_SIZE, true);
										}catch(Exception exp){
											exp.printStackTrace();
											srcBmp = null;
										}catch (Error exp) {
											exp.printStackTrace();
											srcBmp = null;
										}
									}else{
										srcBmp = null;
									}
									
									if (requestListItem != null) {
										requestListItem.setBitmap(srcBmp, null);
										WiGalleryOpenGLRenderer.m_data_manager.AddNeedLoadTextureFile(requestListItem);
										if(CSStaticData.DEBUG){
											if(srcBmp != null){
												Log.w(TAG, "[VideoLoadService]追加文件贴图： " + requestListItemName.toString());
											}else{
												Log.w(TAG, "[VideoLoadService]文件贴图： " + requestListItemName.toString() + "加载失败");
											}
										}
									}
								}
								if (FileTypeHelper.getFileType(requestListItemName.toString()) == FileTypeHelper.TYPE_VIDEO_3DJ) {
									if(CSStaticData.DEBUG){
										Log.w(TAG, "[VideoLoadService]无法处理文件贴图： " + requestListItemName.toString());
									}
								}
								if (FileTypeHelper.getFileType(requestListItemName.toString()) == FileTypeHelper.TYPE_VIDEO_3DS) {
									if(CSStaticData.DEBUG){
										Log.w(TAG, "[VideoLoadService]无法处理文件贴图： " + requestListItemName.toString());
									}
								}
							}
							mRequestVideoList.clear();

							Thread.sleep(sleepMsec, sleepNsec);
							if(CSStaticData.DEBUG && Thread_DEBUG){
								Log.w(TAG, "[VideoLoadService]线程已休眠：  " + sleepMsec + "ms, " + sleepNsec + "ns" );
							}
						} catch (InterruptedException exp) {
							exp.printStackTrace();
							keepRuning = false;
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
				}
				
				if(CSStaticData.DEBUG){
					Log.w(TAG, "[VideoLoadService]视频贴图加载私服停止");
				}
				
			}
		};

		return runnable;
	}

	/**
	 * 按地址排序的方法
	 * @param list 传出参数，排序结果链表
	 * @param type 排序类型
	 * @return
	 */
	private Runnable parserGeoService(List<ElementList> list, final CSStaticData.LIST_TYPE type){

		mSortedList = list; //传引用
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// 清理内存
				if(mSortedList != null){
					mSortedList.clear();
				}else{
					mSortedList = new ArrayList<ElementList>();
				}

				{//执行区间
					try {

						int     mainListSize  = 0,
								inputListSize = 0;
						boolean hasInserted   = false; //是否以找到对应的列表，没有找到则新建一个列表
						String  tempAddr      = "";
						List<Element> mainDataList = null;

						if(WiGalleryOpenGLRenderer.m_data_manager == null){
							return;
						}else{
							mainDataList = WiGalleryOpenGLRenderer.m_data_manager.cloneMainList();
							mainListSize = mainDataList.size();
						}

						for(int i = 0; i < mainListSize; i++){//遍历主链表
							if(Thread.interrupted()){
								return; //可以直接return
							}

							tempAddr = GeoParser.parserGeo(mContext, mainDataList.get(i).m_latitude, mainDataList.get(i).m_latitude, type);
							inputListSize = mSortedList.size();
							hasInserted = false;
							for(int j = 0; j < mSortedList.size(); j++){
								if(Thread.interrupted()){
									return; //可以直接return
								}
								//比较list中的每条列表的头结点
								if(mSortedList.get(j).get(i).getName().equals(tempAddr)){
									mSortedList.get(j).add(mainDataList.get(i));
									hasInserted = true;
									break;
								}
							}
							//没有找到对应的链表，新建一个列表
							if(!hasInserted){ 
								if(Thread.interrupted()){
									return; //可以直接return
								}
								ArrayList<Element> appendElementList = new ArrayList<Element>();
								appendElementList.add(mainDataList.get(i));
								mSortedList.add(new ElementList(appendElementList, type));
							}
						}

					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}

		};

		return runnable;
	}

	/**
	 * 文件移动的方法
	 * @param fileList 待移动的文件
	 * @param desDir 目标目录
	 * @param ignoreOpenGL 不通知OpenGL层
	 * @return
	 */
	private Runnable moveFilesRunnable(final List<String> moveFileList, final String desDir, final boolean ignoreOpenGL, final int owner) {
		
		Runnable runable = new Runnable() {
			
			@Override
			public void run() {
				int             dbResult     = 0;    
				int             listSize     = 0;
				int             sucessNum    = 0;    //操作成功数量
				int             failedNum    = 0;    //操作失败数量
				File            fout         = null;
				String          resFilePath  = null;
				String          desFilePath  = null;
				String          filepath     = null;
				ContentResolver resolver     = null;
				List<String>    fileList     = new ArrayList<String>(moveFileList);
				List<String>    destList     = new ArrayList<String>(moveFileList);
				
				if(fileList != null){
					listSize = fileList.size();
					for(int i = 0; i < listSize; i++){
						//发送操作过程消息
						if(owner == WiGalleryActivity.WiGalleryActivityID){
							if(WiGalleryActivity.mUIHandler != null && owner == WiGalleryActivity.WiGalleryActivityID){
								Message msg = new Message();
								msg.what    = WiGalleryActivity.HANDLE_MOVE_PROGRESS;
								msg.arg1    = (i * 100)/(2 * listSize);
								WiGalleryActivity.mUIHandler.sendMessage(msg);
							}
						}
						fout = new File(fileList.get(i));
						
						if(fout.exists()){
							File desFile = new File(desDir + fout.getName());
							resFilePath = fout.getAbsolutePath();
							desFilePath = desFile.getAbsolutePath();
							destList.set(i, desFilePath);
							if(CSStaticData.DEBUG){
								Log.w(TAG, "[moveFiles]文件 " + resFilePath + " 至 " + desFilePath);
							}
							if(resFilePath.equals(desFilePath)){
								continue;
							}
							if(!desFile.getParentFile().exists()){
								//如果目录不存在，则创建目录
								desFile.getParentFile().mkdirs();
							}
							if(!FileOperation.moveFileTo(resFilePath, desFilePath)){
								//如果移动不成功，就在文件名后加"_copy."，然后重试
								String retryFileName = desDir + fout.getName();
								String retryFileExten = retryFileName.substring(retryFileName.lastIndexOf("."), retryFileName.length());
								retryFileName = retryFileName.substring(0, retryFileName.lastIndexOf("."));
								retryFileName = retryFileName + CSStaticData.RENAME_SUFFIX + retryFileExten;
								
								if(CSStaticData.DEBUG){
									Log.w(TAG, "[moveFiles]文件 " + fout.getAbsolutePath() + " 移动失败，重命名为 " + retryFileName + " 后重试!");
								}
								desFile = new File(retryFileName);
								if(!desFile.getParentFile().exists()){
									//如果目录不存在，则创建目录
									desFile.getParentFile().mkdirs();
								}
								if(!FileOperation.moveFileTo(resFilePath, retryFileName)){ 
									//再不成功就不管了
									if(CSStaticData.DEBUG){
										Log.w(TAG, "[moveFiles]文件重命名为 " + retryFileName + " 后移动失败，可能没有写权限!");
									}
									
									//移动失败，不再进行后续操作
									failedNum ++;
									continue;
								}else{
									if(CSStaticData.DEBUG){
										Log.w(TAG, "[moveFiles]文件重命名为 " + retryFileName + " 后移动成功!");
									}
									sucessNum ++;
								}
							}else{
								if(CSStaticData.DEBUG){
									Log.w(TAG, "[moveFiles]文件 " + fout.getAbsolutePath() + " 移动成功!");
								}
								sucessNum ++;
							}
						}else{
							sucessNum ++;
						}
					}
					
					//通知OpenGL
					if(!ignoreOpenGL){
						if(WiGalleryOpenGLRenderer.m_data_manager != null && WiGalleryOpenGLRenderer.m_element_group != null){
							for(int i = 0; i < listSize; i++){
								resFilePath = fileList.get(i);
								desFilePath = destList.get(i);
								if(WiGalleryOpenGLRenderer.m_data_manager.Get(resFilePath) != null){
									WiGalleryOpenGLRenderer.m_data_manager.Get(resFilePath).setName(desFilePath);
								}
							}
							WiGalleryOpenGLRenderer.m_element_group.rebuildList();
						}
					}
					
					//操作数据库
					if(mContext != null){
						for(int i = 0; i < listSize; i++){
							if(owner == WiGalleryActivity.WiGalleryActivityID){
								//发送操作过程消息
								if(WiGalleryActivity.mUIHandler != null && owner == WiGalleryActivity.WiGalleryActivityID){
									Message msg = new Message();
									msg.what    = WiGalleryActivity.HANDLE_MOVE_PROGRESS;
									msg.arg1    = ((i + listSize) * 100)/(2 * listSize);
									WiGalleryActivity.mUIHandler.sendMessage(msg);
								}
							}
							
							resolver = mContext.getContentResolver();
							resFilePath = fileList.get(i);
							desFilePath = destList.get(i);
							
							if(resFilePath.equals(desFilePath)){
								continue;
							}
							//删除旧地址
							if(FileTypeHelper.isImageFile(resFilePath)){
//								if(FileTypeHelper.isInternalFile(filepath)){
									try{
										dbResult = resolver.delete(MediaStore.Images.Media.INTERNAL_CONTENT_URI, " " + MediaStore.Images.Media.DATA + " = '" + resFilePath + "' ", null);
									}catch (SQLiteException exp) {
										exp.printStackTrace();
										//继续删除
										mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + resFilePath)));
										dbResult = -1;
									}
									if(CSStaticData.DEBUG){
										if(dbResult > 0){
											Log.w(TAG, "[deleteFiles]移出数据库旧记录 " + resFilePath + " 成功");
										}else if(dbResult == -1){
											Log.w(TAG, "[deleteFiles]移出数据库旧记录 " + resFilePath + " 失败, 广播通知数据库自行删除");
										}else{
											Log.w(TAG, "[deleteFiles]移出数据库旧记录 " + resFilePath + " 失败");
										}
									}
//								}else{
									try{
										dbResult = resolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, " " + MediaStore.Images.Media.DATA + " = '" + resFilePath + "' ", null);
									}catch (SQLiteException exp) {
										exp.printStackTrace();
										//继续删除
										mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_REMOVED, Uri.parse("file://" + resFilePath)));
										dbResult = -1;
									}
									if(CSStaticData.DEBUG){
										if(dbResult > 0){
											Log.w(TAG, "[deleteFiles]移出数据库旧记录 " + resFilePath + " 成功");
										}else if(dbResult == -1){
											Log.w(TAG, "[deleteFiles]移出数据库旧记录 " + resFilePath + " 失败, 广播通知数据库自行删除");
										}else{
											Log.w(TAG, "[deleteFiles]移出数据库旧记录 " + resFilePath + " 失败");
										}
									}
//								}
							}
							if(FileTypeHelper.isVideoFile(resFilePath)){
//								if(FileTypeHelper.isInternalFile(filepath)){
									try{
										dbResult = resolver.delete(MediaStore.Video.Media.INTERNAL_CONTENT_URI, " " + MediaStore.Images.Media.DATA + " = '" + resFilePath + "' ", null);
									}catch (SQLiteException exp) {
										exp.printStackTrace();
										//继续删除
										mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + resFilePath)));
										dbResult = -1;
									}
									if(CSStaticData.DEBUG){
										if(dbResult > 0){
											Log.w(TAG, "[deleteFiles]移出数据库旧记录 " + resFilePath + " 成功");
										}else if(dbResult == -1){
											Log.w(TAG, "[deleteFiles]移出数据库旧记录 " + resFilePath + " 失败, 广播通知数据库自行删除");
										}else{
											Log.w(TAG, "[deleteFiles]移出数据库旧记录 " + resFilePath + " 失败");
										}
									}
//								}else{
									try{
										dbResult = resolver.delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, " " + MediaStore.Images.Media.DATA + " = '" + resFilePath + "' ", null);
									}catch (SQLiteException exp) {
										exp.printStackTrace();
										//发送广播，继续删除
										mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_REMOVED, Uri.parse("file://" + resFilePath)));
										dbResult = -1;
									}
									if(CSStaticData.DEBUG){
										if(dbResult > 0){
											Log.w(TAG, "[deleteFiles]移出数据库旧记录 " + resFilePath + " 成功");
										}else if(dbResult == -1){
											Log.w(TAG, "[deleteFiles]移出数据库旧记录 " + resFilePath + " 失败, 广播通知数据库自行删除");
										}else{
											Log.w(TAG, "[deleteFiles]移出数据库旧记录 " + resFilePath + " 失败");
										}
									}
//								}
							}
							
							//插入新地址
							mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + desFilePath)));
						}
					}
					//发送操作过程消息
					if(owner == WiGalleryActivity.WiGalleryActivityID){
						if(WiGalleryActivity.mUIHandler != null && owner == WiGalleryActivity.WiGalleryActivityID){
							Message msg = new Message();
							msg.what    = WiGalleryActivity.HANDLE_MOVE_PROGRESS;
							if(listSize == 0){
								msg.arg1 = 100;
							}else{
								msg.arg1 = (listSize * 100)/(listSize);
							}
							WiGalleryActivity.mUIHandler.sendMessage(msg);
						}
					}
				}
				if(owner == WiGalleryActivity.WiGalleryActivityID){
					//发送操作完成消息
					Message msg = new Message();
					msg.what    = WiGalleryActivity.HANDLE_MOVE_COMPLETED;
					msg.arg1    = sucessNum;
					msg.arg2    = failedNum;
					msg.obj     = resFilePath;
					if(WiGalleryActivity.mUIHandler != null && owner == WiGalleryActivity.WiGalleryActivityID){
						WiGalleryActivity.mUIHandler.sendMessageDelayed(msg, 500);
					}
				}
				if(owner == WiImageViewerActivity.WiImageViewerActivityID){
					Message msgViewer = new Message();
					msgViewer.what    = WiImageViewerActivity.CONTROL_HANDLER_SHOW_MOVEINFO;
					msgViewer.arg1    = sucessNum;
					msgViewer.arg2    = failedNum;
					msgViewer.obj     = resFilePath;
					try {
						if(WiImageViewerActivity.m_handle_control_outside != null && owner == WiImageViewerActivity.WiImageViewerActivityID){
							WiImageViewerActivity.m_handle_control_outside.sendMessage(msgViewer);
						}
					} catch (Error exp) {
						// TODO: handle exception
					} catch (Exception exp) {
						// TODO: handle exception
					}
				}
				
			}
		};

		return runable;
	}

	private Runnable deleteFilesRunnable(final List<String> fileList, final boolean afterExit, final boolean ignoreOpenGL, final int owner){
		Runnable runable = new Runnable() {

			@Override
			public void run() {
				int             dbResult     = 0;
				String          filepath     = null;
				int             fileListSize = 0;
				int             sucessNum    = 0;
				int             failedNum    = 0;
				ContentResolver resolver     = null;
				
				if(fileList != null && fileList.size() > 0){
					fileListSize = fileList.size();
					
					//1.删除主链表中对应条目
					if(WiGalleryOpenGLRenderer.m_data_manager != null){
						for(int i = 0; i < fileListSize; i++){
							WiGalleryOpenGLRenderer.m_data_manager.Delete(fileList.get(i));
						}
					}else{
						return;
					}
					
					//2.通知OpenGL
					if(!ignoreOpenGL){
						if(WiGalleryOpenGLRenderer.m_element_group != null){
							WiGalleryOpenGLRenderer.m_element_group.m_is_drawing = false;
							WiGalleryOpenGLRenderer.m_element_group.rebuildList();
							WiGalleryOpenGLRenderer.m_element_group.m_is_drawing = true;
						}
					}
					
					
					//3.删除文件系统
					for(int i = 0; i < fileListSize; i++){
						if(CSStaticData.DEBUG){
							Log.w(TAG, "[deleteFiles]删除 " + fileList.get(i));
						}
						//发送操作过程消息
						if(WiGalleryActivity.mUIHandler != null && owner == WiGalleryActivity.WiGalleryActivityID){
							Message msg = new Message();
							msg.what    = WiGalleryActivity.HANDLE_DELETE_PROGRESS;
							if(fileListSize == 0){
								msg.arg1 = 0;
							}else{
								msg.arg1 = (i * 100)/(2 * fileListSize);
							}
							WiGalleryActivity.mUIHandler.sendMessage(msg);
						}
						
						if(afterExit){//退出后删除
							new File(fileList.get(i)).deleteOnExit();
							sucessNum ++;
						}else{//即时删除
							if(new File(fileList.get(i)).delete()){
								sucessNum ++;
							}else{
								failedNum ++;
							}
						}
					}
					
					//4.删除数据库中对应条目
					if(mContext != null){
						for(int i = 0; i < fileListSize; i++){
							//发送操作过程消息
							if(WiGalleryActivity.mUIHandler != null && owner == WiGalleryActivity.WiGalleryActivityID){
								Message msg = new Message();
								msg.what    = WiGalleryActivity.HANDLE_DELETE_PROGRESS;
								if(fileListSize == 0){
									msg.arg1 = 0;
								}else{
									msg.arg1 = ((i + fileListSize) * 100)/(2 * fileListSize);
								}
								WiGalleryActivity.mUIHandler.sendMessage(msg);
							}
							
							resolver = mContext.getContentResolver();
							filepath = fileList.get(i);
							if(FileTypeHelper.isImageFile(filepath)){
//								if(FileTypeHelper.isInternalFile(filepath)){
									try{
										dbResult = resolver.delete(MediaStore.Images.Media.INTERNAL_CONTENT_URI, " " + MediaStore.Images.Media.DATA + " = '" + filepath + "' ", null);
									}catch (SQLiteException exp) {
										exp.printStackTrace();
										//继续删除
										mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + filepath)));
										dbResult = -1;
									}
									if(CSStaticData.DEBUG){
										if(dbResult > 0){
											Log.w(TAG, "[deleteFiles]移出数据库记录 " + filepath + " 成功");
										}else if(dbResult == -1){
											Log.w(TAG, "[deleteFiles]移出数据库记录 " + filepath + " 失败, 广播通知数据库自行删除");
										}else{
											Log.w(TAG, "[deleteFiles]移出数据库记录 " + filepath + " 失败");
										}
									}
//								}else{
									try{
										dbResult = resolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, " " + MediaStore.Images.Media.DATA + " = '" + filepath + "' ", null);
									}catch (SQLiteException exp) {
										exp.printStackTrace();
										//继续删除
										mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_REMOVED, Uri.parse("file://" + filepath)));
										dbResult = -1;
									}
									if(CSStaticData.DEBUG){
										if(dbResult > 0){
											Log.w(TAG, "[deleteFiles]移出数据库记录 " + filepath + " 成功");
										}else if(dbResult == -1){
											Log.w(TAG, "[deleteFiles]移出数据库记录 " + filepath + " 失败, 广播通知数据库自行删除");
										}else{
											Log.w(TAG, "[deleteFiles]移出数据库记录 " + filepath + " 失败");
										}
									}
//								}
							}
							if(FileTypeHelper.isVideoFile(filepath)){
//								if(FileTypeHelper.isInternalFile(filepath)){
									try{
										dbResult = resolver.delete(MediaStore.Video.Media.INTERNAL_CONTENT_URI, " " + MediaStore.Images.Media.DATA + " = '" + filepath + "' ", null);
									}catch (SQLiteException exp) {
										exp.printStackTrace();
										//继续删除
										mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + filepath)));
										dbResult = -1;
									}
									if(CSStaticData.DEBUG){
										if(dbResult > 0){
											Log.w(TAG, "[deleteFiles]移出数据库记录 " + filepath + " 成功");
										}else if(dbResult == -1){
											Log.w(TAG, "[deleteFiles]移出数据库记录 " + filepath + " 失败, 广播通知数据库自行删除");
										}else{
											Log.w(TAG, "[deleteFiles]移出数据库记录 " + filepath + " 失败");
										}
									}
//								}else{
									try{
										dbResult = resolver.delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, " " + MediaStore.Images.Media.DATA + " = '" + filepath + "' ", null);
									}catch (SQLiteException exp) {
										exp.printStackTrace();
										//发送广播，继续删除
										mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_REMOVED, Uri.parse("file://" + filepath)));
										dbResult = -1;
									}
									if(CSStaticData.DEBUG){
										if(dbResult > 0){
											Log.w(TAG, "[deleteFiles]移出数据库记录 " + filepath + " 成功");
										}else if(dbResult == -1){
											Log.w(TAG, "[deleteFiles]移出数据库记录 " + filepath + " 失败, 广播通知数据库自行删除");
										}else{
											Log.w(TAG, "[deleteFiles]移出数据库记录 " + filepath + " 失败");
										}
									}
//								}
							}
							
						}
					}
					
					//发送操作过程消息
					if(WiGalleryActivity.mUIHandler != null && owner == WiGalleryActivity.WiGalleryActivityID){
						Message msg = new Message();
						msg.what    = WiGalleryActivity.HANDLE_DELETE_PROGRESS;
						if(fileListSize == 0){
							msg.arg1 = 100;
						}else{
							msg.arg1 = (fileListSize * 100)/(fileListSize);
						}
						WiGalleryActivity.mUIHandler.sendMessage(msg);
					}
				}
				
				//发送操作完成消息
				if(WiGalleryActivity.mUIHandler != null && owner == WiGalleryActivity.WiGalleryActivityID){
					Message msg = new Message();
					msg.what    = WiGalleryActivity.HANDLE_DELETE_COMPLETED;
					msg.arg1    = sucessNum;
					msg.arg2    = failedNum;
					msg.obj     = filepath;
					WiGalleryActivity.mUIHandler.sendMessageDelayed(msg, 100);
				}
			}
		};
		
		return runable;
	}
	
	/**
	 * 网络连通性检查方法
	 * @return
	 */
	private Runnable checkInternetRunnable() {
		
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				NetworkInfo         networkInfo          = null;
				ConnectivityManager connectivityManager  = null;
				boolean             isNetWorkConnectable = true;
				
				//判断网络状态
				connectivityManager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
				networkInfo         = connectivityManager.getActiveNetworkInfo();
				if (networkInfo == null || !networkInfo.isConnected()) {
					isNetWorkConnectable = false;
				}
				
				//发送测试结果
				Message msg = new Message();
				msg.what    = WiGalleryActivity.HANDLE_WIFI_CHECKED;
				msg.obj     = isNetWorkConnectable;
				WiGalleryActivity.mUIHandler.sendMessage(msg);
			}
		};
		
		return runnable;
	}
	
	/**
	 * 恢复选择模式的方法
	 * @return
	 */
	@SuppressWarnings("unused")
	private Runnable recoverSelectionModeRunnable(final List<Element> stateList) {
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				int size = 0;
				
				if(stateList != null && stateList.size() != 0){
					
				}
			}
		};
		
		return runnable;
	}
	
	private Runnable setFavoriteToDBRunnable(final List<String> selectedList, final int owner) {
		return new Runnable() {
			@Override
			public void run() {
				FavoriteDBAdapter dbAdapter = new FavoriteDBAdapter(mContext);
				dbAdapter.setAsFavorite(selectedList);
				dbAdapter.dispose();
				dbAdapter = null;
				System.gc();
				//修改主链表
				if(WiGalleryOpenGLRenderer.m_data_manager != null){
					int favorSize = selectedList.size();
					for(int i = 0; i < favorSize; i++){
						Element elem = WiGalleryOpenGLRenderer.m_data_manager.Get(selectedList.get(i));
						if(elem != null)
							elem.setFavorite(true);
					}
				}
				//发送消息
				if(WiGalleryActivity.mUIHandler != null && owner == WiGalleryActivity.WiGalleryActivityID){
					WiGalleryActivity.mUIHandler.sendEmptyMessage(WiGalleryActivity.HANDLE_SET_FAVORITE_TIP);
				}
			}
		};
	}
	
	private Runnable removeFavoriteFromDBRunnable(final List<String> selectedList, final int owner) {
		return new Runnable() {
			@Override
			public void run() {
				FavoriteDBAdapter dbAdapter = new FavoriteDBAdapter(mContext);
				dbAdapter.removeFromFavorite(selectedList);
				dbAdapter.dispose();
				dbAdapter = null;
				System.gc();
				//修改主链表
				if(WiGalleryOpenGLRenderer.m_data_manager != null){
					int favorSize = selectedList.size();
					for(int i = 0; i < favorSize; i++){
						Element elem = WiGalleryOpenGLRenderer.m_data_manager.Get(selectedList.get(i));
						if(elem != null)
							elem.setFavorite(false);
					}
				}
				//发送消息
				if(WiGalleryActivity.mUIHandler != null && owner == WiGalleryActivity.WiGalleryActivityID){
					WiGalleryActivity.mUIHandler.sendEmptyMessage(WiGalleryActivity.HANDLE_REMOVE_FAVORITE_TIP);
				}
			}
		};
	}
	
	private Runnable scanFavoriteDBRunnable(){
		return new Runnable() {
			
			@Override
			public void run() {
				setFavoriteSettingToMainList();
			}
		};
	}
	
	@SuppressWarnings("unused") //for DEBUG block
	private void splitRequestFileList(List<Element> requestList){
		int size = 0;
		long requestOpID = System.currentTimeMillis();
		if(requestList != null){
			size = requestList.size();
			if(CSStaticData.DEBUG && false){
				Log.w(TAG, "[splitRequestFileList]本次请求文件 " + size + " 个，ID = " + requestOpID);
			}
			for(int i = 0; i < size; i++){
				//必须分别对每个类型单独判断，以免遇到不支持的文件时，误判
				if(FileTypeHelper.isVideoFile(requestList.get(i).getName())){
					synchronized(mRequestVideoList){
						mRequestVideoList.add(requestList.get(i));
					}
				}
				if(FileTypeHelper.isImageFile(requestList.get(i).getName())){
					synchronized(mRequestImageList){
						mRequestImageList.add(requestList.get(i));
					}
				}
			}
			if(CSStaticData.DEBUG && false){
				int imageSize = 0,
					videoSize = 0;
				try{
					synchronized(mRequestVideoList){
						imageSize = mRequestVideoList.size();
					}
					synchronized(mRequestImageList){
						videoSize = mRequestImageList.size();
					}
				}catch (NullPointerException e) {
					// TODO: handle exception
				}
				Log.w(TAG, "[splitRequestFileList]---图片 " + imageSize + " 个， ID = " + requestOpID);
				Log.w(TAG, "[splitRequestFileList]---视频" + videoSize + " 个， ID = " + requestOpID);
			}
			
			requestList.clear();
		}
	}

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
			if(mainDataList == null){
				return;
			}
			mainListSize = mainDataList.size();
		}
		
		if(list != null){
			list.clear();
		}
		
		for(int i = 0; i < mainListSize; i++){//遍历主链表
			if(hasCanceled){
				return; //可以直接return
			}
			tempElement = mainDataList.get(i);
			if(tempElement.m_str_address != null){
				int size = 0;
//				size = tempElement.m_str_address.length;
				if(type == CSStaticData.LIST_TYPE.LIST_LOCATION_1){
					size = 1;
				}
				if(type == CSStaticData.LIST_TYPE.LIST_LOCATION_2){
					size = 2;
				}
				if(type == CSStaticData.LIST_TYPE.LIST_LOCATION_3){
					size = 3;
				}
				if(type == CSStaticData.LIST_TYPE.LIST_LOCATION_4){
					size = 4;
				}
				if(tempElement.m_str_address.length < size){
					size = tempElement.m_str_address.length;
				}
				for(int m = 0; m < size; m++){
					tempAddr = tempElement.m_str_address[m] + ", ";
				}
				if(tempAddr.length() >= 2){
					tempAddr = tempAddr.substring(0, tempAddr.length() - 2);
				}
			}else{
				tempAddr = "Unknown";
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
	 * 在主链表中恢复喜好数据库中文件的喜好
	 */
	public void setFavoriteSettingToMainList(){
		int               favorSize = 0;
		List<String>      favorList = new ArrayList<String>();
		FavoriteDBAdapter dbAdapter = new FavoriteDBAdapter(mContext);
		
		if(WiGalleryOpenGLRenderer.m_data_manager == null || WiGalleryOpenGLRenderer.m_data_manager.getMainListSize() == 0){
			return;
		}
		
		favorList = dbAdapter.getFavoriteFiles();
		if(favorList != null && favorList.size() > 0){
			favorSize = favorList.size();
			for(int i = 0; i < favorSize; i++){
				Element elem = WiGalleryOpenGLRenderer.m_data_manager.Get(favorList.get(i));
				if(elem != null)
					elem.setFavorite(true);
			}
		}
		dbAdapter.dispose();
	}
	
	public void setOnDataScanCompletedListener(OnDataScanCompletedListener listener){
		this.mOnDataScanCompletedListener = listener;
	}
	
	/**
	 * 重建数据层
	 */
//	public void flushDataLayer(){
//		if(WiGalleryOpenGLRenderer.m_data_manager != null){
//			//清空DataManager
//			WiGalleryOpenGLRenderer.m_data_manager.clearMainList();
//			//扫描文件数据库
//			scanMediaLib();
//			//重建显示链表
//			setOnDataScanCompletedListener(new OnDataScanCompletedListener() {
//				@Override
//				public void onDataScanCompletedListener() {
//					if(CSStaticData.DEBUG){
//						Log.w(TAG, "[flushDataLayer]重建显示链表");
//					}
//					WiGalleryOpenGLRenderer.m_element_group.rebuildList();
//				}
//			});
//		}
//	}
	
	/**
	 * 清空数据库
	 * @param execArea 执行范围
	 */
	public void clearDatabase(int execArea){
		switch (execArea) {
		case CLEAR_DATABASE_ALL:
			{
				GEOCacheHelper dbAdapter = new GEOCacheHelper(mContext);
				dbAdapter.clearDataBase();
				dbAdapter.close();
			}
			{
				FavoriteDBAdapter dbAdapter = new FavoriteDBAdapter(mContext);
				dbAdapter.clearDataBase();
				dbAdapter.dispose();
			}
			break;
		case CLEAR_DATABASE_GEO:{
				GEOCacheHelper dbAdapter = new GEOCacheHelper(mContext);
				dbAdapter.clearDataBase();
				dbAdapter.close();
			}
			break;
		case CLEAR_DATABASE_FAVORITE:{
				FavoriteDBAdapter dbAdapter = new FavoriteDBAdapter(mContext);
				dbAdapter.clearDataBase();
				dbAdapter.dispose();
			}
			break;

		default:
			break;
		}
	}
	
	public interface OnDataScanCompletedListener{
		public void onDataScanCompletedListener();
	}
}

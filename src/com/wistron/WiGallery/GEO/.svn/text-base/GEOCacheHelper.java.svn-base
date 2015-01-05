package com.wistron.WiGallery.GEO;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.wistron.WiGallery.WiGalleryInterface.BatchProcessCallBack;
import com.wistron.swpc.wicamera3dii.R;

import Utilities.CSStaticData;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.location.Address;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Copyright (c) 2011 Wistron SWPC
 * All rights reserved.
 * @author Cocoonshu
 * @date 2012-02-29 11:21:49
 * @purpose 可通过这个类，在Cache中查询GEO信息，也可以管理Cache
 * @detail  流程：在Cache中查不到则联网查询
 */
public class GEOCacheHelper {
	
	private static final String TAG                     = "GEOCacheHelper"; 
	public  static final int    EARTH_RADIUS_METERS     = 6378137;          //地球半径
    public  static final int    LAT_MIN                 = -90;              //最小纬度
    public  static final int    LAT_MAX                 = 90;               //最大纬度
    public  static final int    LON_MIN                 = -180;             //最小经度
    public  static final int    LON_MAX                 = 180;              //最大经度
    private static final int    MAX_COUNTRY_NAME_LENGTH = 8;                //国家名称限制
    private static final int    MAX_LOCALITY_MILE_RANGE = 20;               //地点范围限制
	private static       String UNKNOWN_LOCATION        = "Unknown";        //默认未知地理位置字符串
    
	private Context             mContext                = null;
	
	private GEODBAdapter        mSqlReader              = null;
	private Address             mAddress                = null;
	private String              mLangeuage              = "";               //默认：不带语言标签
	
	public GEOCacheHelper(Context context) {
		mContext             = context;
		mLangeuage           = Locale.getDefault().getLanguage();
		mSqlReader           = new GEODBAdapter(mContext, mLangeuage);
		UNKNOWN_LOCATION     = context.getResources().getString(R.string.no_location);
		
		if(UNKNOWN_LOCATION == null || UNKNOWN_LOCATION.equals("")){
			UNKNOWN_LOCATION = "Unknown";
		}
	}
	
	public void clearDataBase(){
		if(mSqlReader != null){
			mSqlReader.clearAll();
		}
	}
	
	public void close(){
		if(mSqlReader != null){
			mSqlReader.close();
		}
		
		System.gc();
	}
	
	protected void closeDataBase(){
		if(mSqlReader != null){
			mSqlReader.close();
			mSqlReader = null;
		}
		
		System.gc();
	}
	
	/**
	 * 获取定位字符串
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	public String[] getLocationString(double latitude, double longitude){
		String[]      result        = null;

		if(!Double.isNaN(latitude) && !Double.isNaN(longitude)){
			try{
				//先在数据库中查找
				result = searchInLib(latitude, longitude, mLangeuage);
				
				result = checkEmpty(result);

				//如果数据库中没有找到结果，则在线查找
				if(result == null || checkEmpty(result)[0].equals(UNKNOWN_LOCATION) || result.equals(UNKNOWN_LOCATION)){
					result = searchOnInternet(latitude, longitude, mLangeuage);
				}
			}catch (SQLException e) {
				if(CSStaticData.DEBUG){
					e.printStackTrace();
				}
				result = null;
			}catch (ArrayIndexOutOfBoundsException e) {
				if(CSStaticData.DEBUG){
					e.printStackTrace();
				}
				result = null;
			}catch (Exception e) {
				if(CSStaticData.DEBUG){
					e.printStackTrace();
				}
				result = null;
			}
		}else{
			result = null;
		}
		
		//检查位置字符串是否为空串
		result = checkEmpty(result);
		
		return result;
	}
	
	public List<String[]> getLocationStringBatch(List<CoordinateMap> maps, BatchProcessCallBack callback){
		int                 mapsSize             = 0;
		boolean             isNetWorkConnectable = true;  //网络是否连通
		String[]            elemResult           = null;
		CoordinateMap       elem                 = null;
		List<String[]>      result               = new ArrayList<String[]>();
		NetworkInfo         networkInfo          = null;
		ConnectivityManager connectivityManager  = null;
		
		if(maps == null){
			return null;
		}
		if(maps.size() == 0){
			return result;
		}
		
		//判断网络状态
		connectivityManager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		networkInfo         = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null || !networkInfo.isConnected()) {
			isNetWorkConnectable = false;
		}
		
		//先在数据库中批量查询
		mapsSize = maps.size();
		for(int i = 0; i < mapsSize; i++){
			elem = maps.get(i);
			if(elem != null){
				
				if(!Double.isNaN(elem.getLatitude()) && !Double.isNaN(elem.getLongitude())){

					try{
						//先在数据库中查找
						elemResult = searchInLib(elem.getLatitude(), elem.getLongitude(), mLangeuage);

						//如果数据库中没有找到结果，则在线查找
						if(isNetWorkConnectable && (elemResult == null || checkEmpty(elemResult)[0].equals(UNKNOWN_LOCATION) || elemResult.equals(UNKNOWN_LOCATION))){
							elemResult = searchOnInternet(elem.getLatitude(), elem.getLongitude(), mLangeuage);
						}

					}catch (SQLException e) {
						if(CSStaticData.DEBUG){
							e.printStackTrace();
						}
						elemResult = null;
					}catch (ArrayIndexOutOfBoundsException e) {
						if(CSStaticData.DEBUG){
							e.printStackTrace();
						}
						elemResult = null;
					}catch (Exception e) {
						elemResult = null;
					}finally{
						//保持查询队列的连续性
						//Do Nothing
					}

				}else{
					elemResult = null;
				}
				
				//检查位置字符串是否为空串
				elemResult = checkEmpty(elemResult);
				
				//添加查询结果
				result.add(elemResult);
			}else{
				result.add(null);
			}
			
			//调用回调
			if(callback != null){
				if(i == 0){
					callback.startProcess(i, mapsSize);
					callback.inProcess(i, mapsSize);
				}else if(i == mapsSize - 1){
					callback.endProcess(i, mapsSize);
					callback.inProcess(i, mapsSize);
				}else{
					callback.inProcess(i, mapsSize);
				}
			}
		}
		
		return result;
	}

	/**
	 * 在线搜索位置字符串
	 * @return
	 */
	protected String[] searchOnInternet(double latitude, double longitude, String langeuage){
		String[]            result              = new String[4]; 
		String              defaultResult       = "";
		GEOAddress          geoAddress          = null;
		StringBuilder       address             = null;
		NetworkInfo         networkInfo         = null;
		ConnectivityManager connectivityManager = null;
		
		if(CSStaticData.DEBUG && false){
			Log.w(TAG, "[searchOnInternet]联线搜索地理信息: GPS(" + latitude + ", " + longitude + ")");
		}
		
		if(Double.isNaN(latitude) && Double.isNaN(longitude)){
			return new String[]{defaultResult};
		}
		
		//判断网络状态
		connectivityManager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		networkInfo         = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null || !networkInfo.isConnected()) {
			result = new String[]{defaultResult};
			return result;
		}
		
		//连线查询
		try {
			address = GEOCoder.getAddress(latitude, longitude, langeuage);
			result  = GEOCoder.parserJSON(address.toString(), GEOCoder.PRECISION_ORG);
			
			if(result != null){
				geoAddress = new GEOAddress();
				geoAddress.setCountryName(result[0]);
				geoAddress.setLocality(result[1]);
				geoAddress.setAdminArea(result[2]);
				geoAddress.setSubAdminArea(result[3]);
			}
			
		} catch (IllegalArgumentException e) {
			if(CSStaticData.DEBUG){
				e.printStackTrace();
			}
			geoAddress = new GEOAddress();
		}

		//插入数据库
		if(result != null){
			if(mSqlReader == null){
				mSqlReader = new GEODBAdapter(mContext, mLangeuage);
			}
			//		if(!mLangeuage.equals(address.getLocale().getLanguage())){
			//			//重开数据库
			//			closeDataBase();
			//			mLangeuage = address.getLocale().getLanguage();
			//			mSqlReader = new GEODBAdapter(mContext, mLangeuage);
			//		}

			//执行插入事务
			mSqlReader.insert(getUID(latitude, longitude), geoAddress, mLangeuage);
			mSqlReader.close();
		}
		//过滤数据，检查位置字符串中有没有"null"标签
		for(int i = 0; i < result.length; i++){
			checkNull(result[i]);
		}
		
		return result;
	}

	/**
	 * 在数据库中搜索位置字符串
	 * @param langeuage 
	 * @param uid 
	 * @return
	 */
	protected String[] searchInLib(double latitude, double longitude, String langeuage) throws SQLException{
		String[] result        = null; 
		String   defaultResult = "";
		long     uid           = 0l;
		Cursor   cursor        = null;

		if(CSStaticData.DEBUG && false){
			Log.w(TAG, "[searchInLib]本地搜索地理信息: GPS(" + latitude + ", " + longitude + ")");
		}
		
		if(Double.isNaN(latitude) && Double.isNaN(longitude)){
			return new String[]{defaultResult};
		}
		
		//计算UID
		uid = getUID(latitude, longitude);
		
		if(mSqlReader == null){
			mSqlReader = new GEODBAdapter(mContext, mLangeuage);
		}
		if(!mLangeuage.equals(langeuage)){
			//重开数据库
			closeDataBase();
			mLangeuage = langeuage;
			mSqlReader = new GEODBAdapter(mContext, mLangeuage);
		}
		
		//执行查询事务
		cursor = mSqlReader.select(uid, mLangeuage);
		
		//过滤数据
		if(cursor != null && cursor.getCount() > 0){
			//找到有效数据
			if(!cursor.moveToFirst()){
				do{
					if(cursor.moveToNext()){
						break;
					}
				}while(!cursor.isLast());
			}
			
			//读取数据
			result    = new String[4];
			result[0] = cursor.getString(1);
			result[1] = cursor.getString(2);
			result[2] = cursor.getString(3);
			result[3] = cursor.getString(4);
		}else{
			result = new String[]{defaultResult};
		}
		
		//关闭数据库
		mSqlReader.close();
		
		//检查位置字符串中有没有"null"标签
		for(int i = 0; i < result.length; i++){
			checkNull(result[i]);
		}

		return result;
	}
	
	/**
	 * 获取由经纬度换算的UID
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	protected long getUID(double latitude, double longitude) {
		return (long) (((latitude + LAT_MAX) * 2 * LAT_MAX + (longitude + LON_MAX)) * EARTH_RADIUS_METERS);
	}
	
	/**
	 * 防止从数据库中读取出null
	 * @param locality
	 * @return
	 */
	protected String checkNull(String locality) {
		if (locality == null)
			return "";
		if (locality.equals("null"))
			return "";
		return locality;
	}
	
	/**
	 * 检查位置字符串是否为空串
	 * 空串则返回"Unknown", 否则返回原字符串
	 * @param locality
	 * @return
	 */
	protected String[] checkEmpty(String[] locality) {
		boolean passable = false;
		
		if(locality != null){
			for(int i = 0; i < locality.length; i++){
				if(locality[i] != null && !locality[i].equals("")){
					passable = true;
					break;
				}
			}
		}
		
		if(!passable){
			return new String[]{UNKNOWN_LOCATION};
		}else{
			return locality;
		}
	}
}

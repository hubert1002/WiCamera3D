package com.wistron.WiGallery.GEO;

import java.util.ArrayList;
import java.util.List;

import com.wistron.WiGallery.Element;
import com.wistron.WiGallery.WiGalleryInterface.BatchProcessCallBack;
import com.wistron.swpc.wicamera3dii.R;

import android.content.Context;
import android.util.Log;

import Utilities.CSStaticData;
import Utilities.FileOperation;

/**
 * Copyright (c) 2011 Wistron SWPC
 * All rights reserved.
 * @author Cocoonshu
 * @date 2012-03-01 17:38:38
 * @purpose 位置信息解析
 * @detail  
 */
public class GeoParser 
{
	private static String TAG = "GeoParser";
	private static String UNKNOWN_LOCATION  = "Unknown"; //默认未知地理位置字符串

	public GeoParser(Context context) {
		UNKNOWN_LOCATION  = context.getResources().getString(R.string.no_location);
		if(UNKNOWN_LOCATION == null || UNKNOWN_LOCATION.equals("")){
			UNKNOWN_LOCATION = "Unknown";
		}
	}

	/**
	 * 查询位置信息
	 * @param longitude GPS 经度
	 * @param latitude  GPS 纬度
	 * @param type 位置级别
	 * @return 根据位置级别的限制来返回不同的地区名称， Unknown则没有查询到结果
	 */
	public static String parserGeo(Context context, double latitude, double longitude, CSStaticData.LIST_TYPE type)
	{
		String         result         = "";
		String[]       locality       = null;
		GEOCacheHelper geoCacheHelper = null;
		
		//查询位置
		geoCacheHelper = new GEOCacheHelper(context);
		locality = geoCacheHelper.getLocationString(latitude, longitude);
		
		//取出等级
		result = translateLocalityFormat(locality, type);
		
		//过滤数据
		if(result == null || result.equals("")){
			result = context.getResources().getString(R.string.no_location);
			if(result == null){ //只判断空指针，不判断空串，方便以后需求兼容
				result = UNKNOWN_LOCATION;
			}
		}
		
		return result;		
	}
	
	/**
	 * 批量查询位置信息
	 * @param context
	 * @param coordinateMaps
	 * @param type
	 * @param callBack 执行进度回调接口
	 * @return
	 */
	public static List<Element> parserGeoBatch(Context context, List<Element> coordinateMaps, CSStaticData.LIST_TYPE type, BatchProcessCallBack callBack){
		int                 mapsSize       = 0;
		Element             elem           = null;
		List<String[]>      mapsResult     = null;
		List<Element>       result         = new ArrayList<Element>(coordinateMaps); //异步操作时间过长，传值防止数据变化
		List<CoordinateMap> coordinateList = new ArrayList<CoordinateMap>();
		GEOCacheHelper      geoCacheHelper = null;
		
		if(result == null || result.size() == 0){
			return result;
		}
		
		//生成经纬度坐标列表
		mapsSize = result.size();
		for(int i = 0; i < mapsSize; i++){
			double[] gpsinfo = {Double.NaN, Double.NaN};
			elem = result.get(i);
			gpsinfo[0] = result.get(i).m_latitude;
			gpsinfo[1] = result.get(i).m_longitude;
			if(Double.isNaN(gpsinfo[0])||Double.isNaN(gpsinfo[1])){//若数据库里没有，就去文件系统上查
				gpsinfo = FileOperation.getGPSInfoFromFile(result.get(i).getName());
			}
			coordinateList.add(new CoordinateMap(gpsinfo[0], gpsinfo[1]));
		}
		
		//查询地理地址
		geoCacheHelper = new GEOCacheHelper(context);
		mapsResult = geoCacheHelper.getLocationStringBatch(coordinateList, callBack);
		
		//为链表的每个元素赋上地址信息
		for(int i = 0; i < mapsSize; i++){
			result.get(i).m_str_address = mapsResult.get(i);
			if(CSStaticData.DEBUG){
				Log.w(TAG, "[parserGeoBatch]查询GPS(" + result.get(i).m_latitude + ", " + result.get(i).m_longitude + ") = " + result.get(i).m_str_address);
			}
		}
		
		return result;
	}

	/**
	 * 把位置数组转换为可阅读字符串
	 * @param locality
	 * @param type
	 * @return
	 */
	private static String translateLocalityFormat(String[] locality, CSStaticData.LIST_TYPE type){
		String result = "";
		int    loop   = 0;
		
		if(type == CSStaticData.LIST_TYPE.LIST_LOCATION_1){
			loop = 1;
		}
		if(type == CSStaticData.LIST_TYPE.LIST_LOCATION_2){
			loop = 2;
		}
		if(type == CSStaticData.LIST_TYPE.LIST_LOCATION_3){
			loop = 3;
		}
		if(type == CSStaticData.LIST_TYPE.LIST_LOCATION_4){
			loop = 4;
		}

		if(locality.length < loop){
			loop = locality.length;
		}
		
		for(int i = 0; i < loop; i++){
			if(locality[i] != null && !locality[i].equals("")){
				result += locality[i] + ", ";
			}
		}

		try{
			if(result == null || result.equals("")){
				result = UNKNOWN_LOCATION;
			}else{
				result = result.substring(0, result.length() - 2);
			}
		}catch (IndexOutOfBoundsException e) {
			if(CSStaticData.DEBUG){
				e.printStackTrace();
			}
			result = UNKNOWN_LOCATION;
		}

		return result;
	}
}

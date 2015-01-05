package com.wistron.WiGallery.GEO;

import Utilities.CSStaticData;

/**
 * 
 * @author WH1107028
 * @detail 用于封装经纬度信息
 */
public class CoordinateMap {
	private static String UNKNOWN_LOCATION  = "Unknown"; //默认未知地理位置字符串
	
	private double   mLatitude  = Double.NaN;
	private double   mLongitude = Double.NaN;
	private String   mAddress   = null;
	private String[] mLocality  = null;
	
	public CoordinateMap(double latitude, double longitude){
		this.mLatitude = latitude;
		this.mLongitude = longitude;
	}
	
	public double getLatitude() {
		return mLatitude;
	}
	
	public void setLatitude(double latitude) {
		this.mLatitude = latitude;
	}
	
	public double getLongitude() {
		return mLongitude;
	}
	
	public void setLongitude(double longitude) {
		this.mLongitude = longitude;
	}
	
	public String getAddress() {
		if(mAddress == null || mAddress.equals("")){
			mAddress = translateLocalityFormat(mLocality, CSStaticData.LIST_TYPE.LIST_LOCATION_4);
		}
		return mAddress;
	}
	
	public void setAddress(String address) {
		this.mAddress = address;
	}
	
	public String[] getLocality() {
		return mLocality;
	}
	
	public void setLocality(String[] locality) {
		this.mLocality = locality;
	}
	
	private String translateLocalityFormat(String[] locality, CSStaticData.LIST_TYPE type){
		String result = UNKNOWN_LOCATION;
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
				result = locality[i] + ", ";
			}
		}

		try{
			if(result == null || result.equals("")){
				result = UNKNOWN_LOCATION;
			}else{
				result = result.substring(0, result.length() - 2);
			}
		}catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			result = UNKNOWN_LOCATION;
		}

		return result;
	}
	
	
}

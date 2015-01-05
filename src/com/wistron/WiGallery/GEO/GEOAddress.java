package com.wistron.WiGallery.GEO;

/**
 * Copyright (c) 2011 Wistron SWPC
 * All rights reserved.
 * @author Cocoonshu
 * @date 2012-03-05 15:33:26
 * @purpose 封装地址信息的结构
 * @detail  
 */
public class GEOAddress {
	String mCountryName      = null;
	String mLocalityName     = null;
	String mAdminAreaName    = null;
	String mSubAdminAreaName = null;
	
	public String getCountryName() {
		return mCountryName;
	}
	
	public void setCountryName(String countryName) {
		this.mCountryName = countryName;
	}
	
	public String getLocality() {
		return mLocalityName;
	}
	
	public void setLocality(String localityname) {
		this.mLocalityName = localityname;
	}
	
	public String getAdminArea() {
		return mAdminAreaName;
	}
	
	public void setAdminArea(String adminAreaName) {
		this.mAdminAreaName = adminAreaName;
	}
	
	public String getSubAdminArea() {
		return mSubAdminAreaName;
	}
	
	public void setSubAdminArea(String subAdminAreaName) {
		this.mSubAdminAreaName = subAdminAreaName;
	}
	
}

package com.wistron.WiCamera;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.Bundle;
import android.widget.Toast;

/**
 * 
 * @Copyright (c) 2011 Wistron SWPC All rights reserved.
 * 
 * @created: 2011/09/26
 * @filename: GetGPSInfo.java
 * @author WH1107063(周海江)
 * @purpose 用于得到用户所在gps信息的类
 * 
 */
public class GetGPSInfo {
	// 定义LocationManager对象
	LocationManager m_locManager;
	// 定义获取经度，维度的常量
	public static double m_getLatitude, m_latitude;
	public static Location m_locations;
	public static Context m_context;

	public GetGPSInfo(Context m_context) {
		this.m_context = m_context;
	}

	public Location getGps() {
		// 创建LocationManager对象
		m_locManager = (LocationManager) m_context
				.getSystemService(Context.LOCATION_SERVICE);
		// 从GPS获取最近的gps信息
		m_locations = m_locManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		// 使用location根据EditText的显示
		// 每3秒获取gps信息
		m_locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000,
				8, new LocationListener() {
					@Override
					public void onLocationChanged(Location location) {
						// 当GPS定位信息发生改变时，更新位置
						m_locations = location;
					}

					@Override
					public void onProviderDisabled(String provider) {
						m_locations = null;
					}

					@Override
					public void onProviderEnabled(String provider) {
						// 当GPS LocationProvider可用时更新位置
						m_locations = m_locManager
								.getLastKnownLocation(provider);
					}

					@Override
					public void onStatusChanged(String provider, int status,
							Bundle extras) {
					}
				});
		if (m_locations != null) {
			System.out.println("df" + m_locations.getLatitude() + "s"
					+ m_locations.getLongitude());
			Toast.makeText(m_context, m_locations.getLatitude() + "s"
					+ m_locations.getLongitude(), 1000);
		}
		return m_locations;
	}

	public static boolean isGPSOpen() {
		try {
			// GPS是否开启
			LocationManager locationManager = (LocationManager) m_context
					.getSystemService(Context.LOCATION_SERVICE);
			return locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception e) {
			// TODO: handle exception
			// System.out.println("不支持gps");
			return false;
		}

	}
}
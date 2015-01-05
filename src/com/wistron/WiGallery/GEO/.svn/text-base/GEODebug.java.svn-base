package com.wistron.WiGallery.GEO;

import com.wistron.WiGallery.WiGalleryOpenGLRenderer;

import android.content.Context;

public class GEODebug {
	
	/**
	 * 填充地理信息数据库
	 */
	static public void fillDatabase(Context context){
		GEOCacheHelper helper     = new GEOCacheHelper(context);
		GEODBAdapter   adapter    = new GEODBAdapter(context, "zh");
		GEOAddress     geoAddress = null;
		
		geoAddress = new GEOAddress();
		geoAddress.setCountryName("中国");
		geoAddress.setLocality("湖北");
		geoAddress.setAdminArea("武汉");
		geoAddress.setSubAdminArea("江夏区");
		adapter.insert(helper.getUID(29.7123, 107.4090), geoAddress, "zh");
		
		geoAddress = new GEOAddress();
		geoAddress.setCountryName("中国");
		geoAddress.setLocality("台湾");
		geoAddress.setAdminArea("台北");
		geoAddress.setSubAdminArea("汐止区");
		adapter.insert(helper.getUID(30.5833, 114.3000), geoAddress, "zh");
		
		geoAddress = new GEOAddress();
		geoAddress.setCountryName("中国");
		geoAddress.setLocality("重庆市");
		geoAddress.setAdminArea("重庆");
		geoAddress.setSubAdminArea("涪陵区");
		adapter.insert(helper.getUID(45.4166,  75.7166), geoAddress, "zh");
		
		geoAddress = new GEOAddress();
		geoAddress.setCountryName("中国");
		geoAddress.setLocality("上海市");
		geoAddress.setAdminArea("上海");
		geoAddress.setSubAdminArea("涪陵");
		adapter.insert(helper.getUID(25.0333, 121.6333), geoAddress, "zh");
		
		adapter.close();
		adapter = null;
		System.gc();

		float[][] loc  = {{29.7123f, 107.4090f},
				{30.5833f, 114.3000f},
				{45.4166f,  75.7166f},
				{25.0333f, 121.6333f}};
		String[][]  addrs = {{"中国","重庆","重庆","涪陵"},
							 {"中国","湖北","武汉","江夏"},
							 {"中国","台湾","台北","汐止"},
							 {"中国","湖北","武汉","新洲"}};

		int size = WiGalleryOpenGLRenderer.m_data_manager.getMainListSize();
		if(size < 4){
			for(int i = 0; i < size; i++){
				WiGalleryOpenGLRenderer.m_data_manager.Get(i).m_latitude  = loc[i][0];
				WiGalleryOpenGLRenderer.m_data_manager.Get(i).m_longitude = loc[i][1];
				WiGalleryOpenGLRenderer.m_data_manager.Get(i).m_str_address = addrs[i];
			}
		}else{
			for(int i = 0; i < loc.length; i++){
				WiGalleryOpenGLRenderer.m_data_manager.Get(i).m_latitude  = loc[i][0];
				WiGalleryOpenGLRenderer.m_data_manager.Get(i).m_longitude = loc[i][1];
				WiGalleryOpenGLRenderer.m_data_manager.Get(i).m_str_address = addrs[i];
			}
}
	}
	
	/**
	 * 清除地理信息数据库
	 */
	static public void clearDatabase(Context context){
		GEOCacheHelper helper = new GEOCacheHelper(context);
		helper.clearDataBase();
		helper.closeDataBase();
		helper = null;
		System.gc();
	}
}

package com.wistron.WiGallery;

public class AllocGPS {
	
	static double[][] mGPSGroove = {
		{116.28, 39.54}, //北京
		{121.29, 31.14}, //上海
		{117.11, 39.09}, //天津
		{106.32, 29.32}, //重庆
		{21.00,  52.15}, //波兰 华沙
		{13.25,  52.30}, //德国 柏林
		{-118.15, 34.04} //美国 洛杉矶
	};
		
	static String[][] mGPSAddress = {
		{"中国", "北京市",    "北京",  "北京"},
		{"中国", "上海市",    "上海",  "上海"},
		{"中国", "天津市",    "天津",  "天津"},
		{"中国", "重庆市",    "重庆",  "重庆"},
		{"波兰", "华沙",     "华沙",  "华沙"},
		{"德国", "柏林",     "柏林",  "柏林"},
		{"美国", "加利福尼亚", "洛杉矶", "洛杉矶"},
	};
	
	public static void mallocGPS(){
		if(WiGalleryOpenGLRenderer.m_data_manager != null){
			if(WiGalleryOpenGLRenderer.m_data_manager.Size() > 0){
				int size = WiGalleryOpenGLRenderer.m_data_manager.Size();
				for(int i = 0; i < size/2; i++){
					WiGalleryOpenGLRenderer.m_data_manager.Get(i).m_longitude   = mGPSGroove[0][0];
					WiGalleryOpenGLRenderer.m_data_manager.Get(i).m_latitude    = mGPSGroove[0][1];
					WiGalleryOpenGLRenderer.m_data_manager.Get(i).m_str_address = mGPSAddress[0];
				}
			}
		}
	}
}

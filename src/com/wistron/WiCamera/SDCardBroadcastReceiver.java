package com.wistron.WiCamera;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.widget.TextView;

/**
 * 
 * @Copyright (c) 2011 Wistron SWPC All rights reserved.
 * 
 * @created: 2011/09/26
 * @filename: SDCardBroadcastReceiver.java
 * @author WH1107063(周海江)
 * @purpose 判断有无sdcard的类
 * 
 */
public class SDCardBroadcastReceiver extends BroadcastReceiver {
	public Context context;
	public TextView m_sparephotos;
	// long spare = OperationUtil.readSDCard(0) / 1024 / 1024;
	public static boolean M_ISTFCARD_MOUNTED = false;
	public static int M_TFCARD_MOUNTED = 1;
	public static int M_TFCARD_UNMOUNTED = 2;

	public SDCardBroadcastReceiver(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		
	}

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		System.out.println("SDCardBroadcastReceiver 被注册");
		// TODO Auto-generated method stub
		String action = arg1.getAction();
		// 当SD卡插入时
		if (Intent.ACTION_MEDIA_MOUNTED.equals(action)
				|| Intent.ACTION_MEDIA_SCANNER_STARTED.equals(action)
				|| Intent.ACTION_MEDIA_SCANNER_FINISHED.equals(action)) {
			// spare = OperationUtil.readSDCard(0) / 1024 / 1024;
			// m_sparephotos.setText(spare + "M");
			// System.out.println("SDCARD被插入fsadfasdf");
			M_ISTFCARD_MOUNTED = false;
			Message msg = new Message();
			msg.arg1 = M_TFCARD_MOUNTED;
			WiCameraActivity.m_main_handle.sendMessage(msg);
			// 当SD卡拔出时
		} else if (Intent.ACTION_MEDIA_REMOVED.equals(action)
				|| Intent.ACTION_MEDIA_UNMOUNTED.equals(action)
				|| Intent.ACTION_MEDIA_BAD_REMOVAL.equals(action)) {
			// spare = OperationUtil.readSDCard(0) / 1024 / 1024;
			// m_sparephotos.setText(spare + "M");
			// System.out.println("SDCARD被拔出fdfsadfsadf");
			M_ISTFCARD_MOUNTED = true;
			Message msg = new Message();
			msg.arg1 = M_TFCARD_UNMOUNTED;
			WiCameraActivity.m_main_handle.sendMessage(msg);
		}
	}
}

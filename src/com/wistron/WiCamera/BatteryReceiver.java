package com.wistron.WiCamera;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.widget.Button;

import com.wistron.swpc.wicamera3dii.R;

/**
 * 
 * @Copyright (c) 2011 Wistron SWPC All rights reserved.
 * 
 * @created: 2011/09/26
 * @filename: BatteryReceiver.java
 * @author WH1107063(周海江)
 * @purpose 接收剩余电量广播的Receiver
 * 
 * 
 * 
 * 
 */
public class BatteryReceiver extends BroadcastReceiver {
	private Button m_btn_battery;
    private Button m_btn_battery_panorama;
	public BatteryReceiver(Button m_btn_battery,Button m_btn_battery_panorama) {
		// TODO Auto-generated constructor stub
		this.m_btn_battery = m_btn_battery;
		this.m_btn_battery_panorama=m_btn_battery_panorama;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
//		System.out.println("BatteryReceiver 被注册");

		int status = intent.getIntExtra("status", 0);
		if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
			m_btn_battery.setBackgroundResource(R.drawable.camera_battery_charge);
			m_btn_battery_panorama.setBackgroundResource(R.drawable.camera_battery_charge);

		} else {

			// TODO Auto-generated method stub
			int rawlevel = intent.getIntExtra("level", -1);// 获得当前电量
			int scale = intent.getIntExtra("scale", -1);
			// 获得总电量
			int level = -1;
			if (rawlevel >= 0 && scale > 0) {
				level = (rawlevel * 100) / scale;
			}
			// m_btn_battery.setText(level + "%");
			if (level == 100) {
				m_btn_battery
						.setBackgroundResource(R.drawable.camera_battery_high);
				m_btn_battery_panorama.setBackgroundResource(R.drawable.camera_battery_high);
			} else if (level > 80) {
				m_btn_battery
						.setBackgroundResource(R.drawable.camera_battery_high);
				m_btn_battery_panorama
				.setBackgroundResource(R.drawable.camera_battery_high);
			} else if (level >= 30) {
				m_btn_battery
						.setBackgroundResource(R.drawable.camera_battery_med);
				m_btn_battery_panorama
				.setBackgroundResource(R.drawable.camera_battery_med);
			} else {
				m_btn_battery
						.setBackgroundResource(R.drawable.camera_battery_low);
				m_btn_battery_panorama
				.setBackgroundResource(R.drawable.camera_battery_low);
			}

		}
	}

}

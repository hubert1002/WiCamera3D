package com.wistron.WiCamera;

import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;

/**
 * 
 * @Copyright (c) 2011 Wistron SWPC All rights reserved.
 * 
 * @created: 2011/09/26
 * @filename: TimeCounter.java
 * @author WH1107063(周海江)
 * @purpose 用于录制计时的类
 * 
 */
public class TimeCounter {
	// 定义显示时间的textview
	public Button m_csbtn_recordtime;
	// 定义用于计时的计时控件
	public Chronometer m_chronometer_recordtime;

	public TimeCounter(Chronometer m_chronometer_recordtime,
			Button m_csbtn_recordtime) {
		// 接收构造函数传过来的值，并给本类的参数赋值
		this.m_csbtn_recordtime = m_csbtn_recordtime;
		this.m_chronometer_recordtime = m_chronometer_recordtime;

		// 初始化本类的时候隐藏传过来的显示录制时间的textview，并赋初始值
		m_csbtn_recordtime.setText("00:00:00");
		// m_csbtn_recordtime.setVisibility(View.GONE);
	}

	/**
	 * 开始计时
	 */
	public void start() {
		// System.out.println("开始计时了。。。");
		// 显示录制时间的textview
		m_csbtn_recordtime.setVisibility(View.VISIBLE);
		// 计时器控件重新置数，并开始启动计时器
		m_chronometer_recordtime.setBase(SystemClock.elapsedRealtime());
		m_chronometer_recordtime.start();
		// 当背景改变是后背景改变
		m_chronometer_recordtime
				.setOnChronometerTickListener(new OnChronometerTickListener() {
					// 记录录制时间变量
					@Override
					public void onChronometerTick(Chronometer chronometer) {
						// TODO Auto-generated method stub

						// 为显示录制时间的textview赋值
						String time = chronometer.getText().toString();
						System.out.println("time为" + time);
						// 确保显示时间的格式为00:00:00
						if (time.length() == 5) {
							m_csbtn_recordtime.setText("00:"
									+ chronometer.getText());
						} else if (time.length() == 7) {
							m_csbtn_recordtime.setText("0"
									+ chronometer.getText());
						} else if (time.length() == 8) {
							m_csbtn_recordtime.setText(chronometer.getText());
						}

						if (WiCameraActivity.mCurrentDegree == 90||WiCameraActivity.mCurrentDegree == 270) {
							WiCameraActivity.m_al_camera_overlayui
									.postInvalidate();
						}
					}

				});
	}

	/**
	 * 停止计时
	 */
	public void stop() {

		// 隐藏传过来的显示录制时间的textview
		// m_cstxt_recordtime.setVisibility(View.INVISIBLE);
		m_csbtn_recordtime.setText("00:00:00");
		// 计时器控件重新置数，并开始停止计时器
		m_chronometer_recordtime.stop();
		// m_chronometer_recordtime.setBase(SystemClock.elapsedRealtime());
	}

}

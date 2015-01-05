package com.wistron.StereoUI;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import Utilities.CSStaticData;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Copyright(c)2011 Wistron SWPC ALL rights reserved. 
 * created: 2012-02-01  
 * filename:CaptionsResolve.java
 * 
 * purpose:字幕解析
 * 
 */
public class CaptionsResolve {

	private Context 										mContext 				= null;
	private StringBuilder 									mNewString 				= null;
	private CaptionsBean 									mCaptionsBean 			= null;
	private int 											mTimeError 				= 0; // 在此设定需要调整的时间秒数，正数为延迟，负数为提早
	private int 											mLine 					= 0;
	private ArrayList<Long> 								mStartTime 				= null;
	private ArrayList<Long> 								mEndTime 				= null;
	private ArrayList<StringBuilder> 						mCaptionsList 			= null;
	private ArrayList<StringBuilder> 						mStartTimeFormatList 	= null;
	private ArrayList<StringBuilder> 						mEndTimeFormatList 		= null;
	public  Handler 										mHandler 				= null;//控制字幕显示的Handler
	private TextView 										mCaptionsTV 			= null;//用于显示字幕的TextView
	private String 											mTempString 			= null,
															mStartTimeFormat 		= null,	
															mEndTimeFormat 			= null,	
															mFileType 				= null,
															mCharset 				= null,
															mSystemCharset          = null,
															mFilePath 				= null,
															mRegex 					= null;
	private Matcher 										mMatcher 				= null;
	private static final String 							FILE_TYPE_SRT 			= ".srt",//.SRT格式的字幕
															FILE_TYPE_SMI 			= ".smi",//.SMI格式的字幕
															TAG                     = "CaptionsResolve";
//	private MsgBox                                          mMsgBox                 = null;

	public CaptionsResolve(Context context) {

		mContext = context;
		// 初始化控件
		initialize();
	}

	public void initialize() {

		mCaptionsBean = new CaptionsBean();
		mCaptionsTV = new TextView(mContext);
		mCaptionsTV.setTextSize(24);
		mCaptionsTV.setTextColor(Color.WHITE);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		layoutParams.setMargins(0, 0, 0, 0);
		mCaptionsTV.setLayoutParams(layoutParams);
		mCaptionsTV.setShadowLayer(3, 0, 0, 0xFF0000FF);
		mCaptionsTV.setGravity(Gravity.CENTER_HORIZONTAL);

		mStartTime = new ArrayList<Long>();
		mEndTime = new ArrayList<Long>();
		mCaptionsList = new ArrayList<StringBuilder>();

		mStartTimeFormatList = new ArrayList<StringBuilder>();
		mEndTimeFormatList = new ArrayList<StringBuilder>();
		mHandler();

	}

	public void setCaptionPath(String path){
		mFilePath = path;
	}
	
	public void addToLayout(ViewGroup layout){
//		mParentLayout = layout;
		if(layout == null){
			return;
		}
		layout.addView(mCaptionsTV);
	}
	
	/**
	 * 开始解析字幕
	 * @param path
	 */
	public void readFile(String path){
		mFilePath = path;
		mSystemCharset = System.getProperty("file.encoding");
	
		//如果用户未选择字幕的编码方式，默认启动系统的编码方式
		if (getCharset() == null) {
			mCharset = mSystemCharset;
		}
			
		if (CSStaticData.DEBUG) {
			Log.v(TAG,"系统语言user.language:"+System.getProperty("user.language")); //zh
			Log.v(TAG,"用户注册user.region:"+System.getProperty("user.region")); //CN
			Log.v(TAG,"系统编码方式file.encoding:"+mSystemCharset);
			Log.v(TAG,"已采用的编码方式"+mCharset);	
		}
		
		try {
			mFilePath = mFilePath + ".srt";
			InputStreamReader iStream = new InputStreamReader(new FileInputStream(mFilePath), mCharset);
			BufferedReader reader = new BufferedReader(iStream);
			mNewString = new StringBuilder();

			while ((mTempString = reader.readLine()) != null) {
				mNewString.append(mTempString);
				mNewString.append("\n");
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (mNewString == null) {
			try {
				mFilePath = mFilePath + ".smi";
				InputStreamReader iStream = new InputStreamReader(new FileInputStream(mFilePath), mCharset);
				BufferedReader reader = new BufferedReader(iStream);
				mNewString = new StringBuilder();

				while ((mTempString = reader.readLine()) != null) {
					mNewString.append(mTempString);
					mNewString.append("\n");
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			
		}
		if (mNewString == null) {
			
			if (CSStaticData.DEBUG) {
				Log.v(TAG, "字幕解析失败，没有找到对应的字幕文件！");
			}
			
//			mMsgBox = new MsgBox(mContext);
//			mMsgBox.setMessage("Sorry,no suitable caption found!");
//			mMsgBox.setModelStatus(false);
//			mMsgBox.hideDelay(3000);
//			mMsgBox.setSize(500, 200);
//			mMsgBox.addToLayout(mParentLayout);
//			mMsgBox.show();
//			Toast.makeText(mContext, "Sorry,no suitable caption found!", 1000).show();
			return;
		}else {
			if (CSStaticData.DEBUG) {
				Log.v(TAG, "字幕解析成功！");
			}
			
//			mMsgBox = new MsgBox(mContext);
//			mMsgBox.setMessage("Loaded caption successfully!");
//			mMsgBox.setModelStatus(false);
//			mMsgBox.hideDelay(3000);
//			mMsgBox.setSize(500, 200);
//			mMsgBox.addToLayout(mParentLayout);
//			mMsgBox.show();
//			Toast.makeText(mContext, "Loaded caption successfully!", 1000).show();
		}
		
		mFileType = mFilePath.substring(mFilePath.length() - 4, mFilePath.length());

		if (mFileType.equals(FILE_TYPE_SRT)) {
			// 正则表达式，用于匹配类似于“01:54:16,332 --> 01:54:18,163”的时间描述字符行
			mRegex = "([0-9]*\\n)([0-9]*:[0-9]*:[0-9]*,[0-9]*[\\s]*-->[\\s]*[0-9]*:[0-9]*:[0-9]*,[0-9]*\\n)(.*[\\s\\S]\\D*(?![0-9]*:[0-9]*:[0-9]*,[0-9]*[\\s]*-->[\\s]*[0-9]*:[0-9]*:[0-9]*,[0-9]*\\n))";// 匹配时间后的两行
			if (mNewString == null) {
				return;
			} else {
				mMatcher = Pattern.compile(mRegex, Pattern.CASE_INSENSITIVE).matcher(mNewString);
			}

			while (mMatcher.find()) {

				// 以下对时间描述字符行进行格式转换和数学运算

				int second_start = Integer.parseInt(mMatcher.group(2).substring(6, 8));
				int minute_start = Integer.parseInt(mMatcher.group(2).substring(3, 5));
				int hour_start = Integer.parseInt(mMatcher.group(2).substring(0, 2));
				long startTime = (mTimeError + second_start + minute_start * 60 + hour_start * 3600) * 1000;

				mStartTimeFormat = mMatcher.group(2).substring(0, 12);
				mEndTimeFormat = mMatcher.group(2).substring(17, 29);
				int second_end = Integer.parseInt(mMatcher.group(2).substring(23, 25));
				int minute_end = Integer.parseInt(mMatcher.group(2).substring(20, 22));
				int hour_end = Integer.parseInt(mMatcher.group(2).substring(17, 19));
				long endTime = (mTimeError + second_end + minute_end * 60 + hour_end * 3600) * 1000;

				mStartTime.add(startTime);
				mEndTime.add(endTime);

				StringBuilder sb_captions = new StringBuilder(mMatcher.group(3));
				StringBuilder sb_startTime_format = new StringBuilder(mStartTimeFormat);
				StringBuilder sb_endTime_format = new StringBuilder(mEndTimeFormat);

				mCaptionsList.add(sb_captions);

				mStartTimeFormatList.add(sb_startTime_format);
				mEndTimeFormatList.add(sb_endTime_format);

				mCaptionsBean.setM_startTime(mStartTime);
				mCaptionsBean.setM_endTime(mEndTime);
				mCaptionsBean.setM_captions(mCaptionsList);

			}
		} else if (mFileType.equals(FILE_TYPE_SMI)) {//解析.SMI格式的字幕

			mRegex = "<SYNC[\\s]*Start=([0-9]*)>([^<]*(?!&nbsp;))";
			mMatcher = Pattern.compile(mRegex, Pattern.CASE_INSENSITIVE).matcher(mNewString);
			String m_text_sim = null;
			while (mMatcher.find()) {
				if (mMatcher.group(2).contains("&nbsp;")) {
					m_text_sim = mMatcher.group(2).replace("&nbsp;", "");

				} else {
					m_text_sim = mMatcher.group(2);
				}

				long startTime = Integer.parseInt(mMatcher.group(1));
				mStartTime.add(startTime);

				StringBuilder sb_captions = new StringBuilder(m_text_sim);

				mCaptionsList.add(sb_captions);
				String string_second = mMatcher.group(1).substring(0, mMatcher.group(1).length() - 3);
				String string_ms = mMatcher.group(1).substring(mMatcher.group(1).length() - 3,
						mMatcher.group(1).length());
				mStartTimeFormat = String.format("%02d:%02d:%02d:%03d", startTime / 3600000, startTime / 60000,
						Integer.parseInt(string_second) % 60, Integer.parseInt(string_ms));
				StringBuilder sb_startTime_format = new StringBuilder(mStartTimeFormat);
				mStartTimeFormatList.add(sb_startTime_format);

				mCaptionsBean.setM_startTime(mStartTime);
				mCaptionsBean.setM_endTime(mStartTime);
				mCaptionsBean.setM_captions(mCaptionsList);
			}
		} else {
			mRegex = "([0-9]*\\n)([0-9]*:[0-9]*:[0-9]*,[0-9]*[\\s]*-->[\\s]*[0-9]*:[0-9]*:[0-9]*,[0-9]*\\n)(.*[\\s\\S]\\D*(?![0-9]*:[0-9]*:[0-9]*,[0-9]*[\\s]*-->[\\s]*[0-9]*:[0-9]*:[0-9]*,[0-9]*\\n))";// 匹配时间后的两行
		}
	}


	public void mHandler() {

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:
					long msgobj = (Integer) msg.obj;
					for (int i = 0; i < mStartTime.size(); i++) {
						if (msgobj / 1000 == mStartTime.get(i) / 1000) {
							mLine = i;
							updateText();
							if (mEndTime.size()!=0&&mEndTime.get(mLine) > msgobj) {
								mHandler.sendEmptyMessageDelayed(3, mEndTime.get(mLine) - msgobj);
							}
						}
					}
					break;
				case 2:
					// updateTime();
					break;
				case 3:
					hideText();
					break;
				}
			}
		};
	}

	public void updateText() {
	
		if (mLine < mStartTime.size()) {
			mCaptionsTV.setText(mCaptionsList.get(mLine));
		}

	}

	public void hideText() {
		mCaptionsTV.setText("");
	}

	public static String getCharset(File file) {
		String charset = "GBK";
		byte[] first3Bytes = new byte[3];
		try {
			boolean checked = false;
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			bis.mark(0);
			int read = bis.read(first3Bytes, 0, 3);
			if (read == -1)
				return charset;
			if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
				charset = "UTF-16LE";
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF) {
				charset = "UTF-16BE";
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB && first3Bytes[2] == (byte) 0xBF) {
				charset = "UTF-8";
				checked = true;
			}
			bis.reset();
			if (!checked) {
				while ((read = bis.read()) != -1) {
					if (read >= 0xF0)
						break;
					if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
						break;
					if (0xC0 <= read && read <= 0xDF) {
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) // 双字节 (0xC0 - 0xDF)
							// (0x80
							// - 0xBF),也可能在GB编码内
							continue;
						else
							break;
					} else if (0xE0 <= read && read <= 0xEF) {// 也有可能出错，但是几率较小
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) {
							read = bis.read();
							if (0x80 <= read && read <= 0xBF) {
								charset = "UTF-8";
								break;
							} else
								break;
						} else
							break;
					}
				}
			}

			bis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return charset;
	}

	public void setCharset(String charset) {
		mCharset = charset;
	}

	public String getCharset(){
		return mCharset;
	}

}

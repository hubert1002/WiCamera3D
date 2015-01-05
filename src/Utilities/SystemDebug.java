package Utilities;

import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;

/**
 * Copyright (c) 2011 Wistron SWPC
 * All rights reserved.
 * @author Cocoonshu
 * @date 2012-04-09 13:56:42
 * @purpose 调试类
 * @detail  
 */
public class SystemDebug{
	
	public static OnSystemDebugTrigger mTrigger  = null;
	private static final int HANLDER_DRAW_DEBUG  = 0x01;
	private static final int HANLDER_CLEAR_DEBUG = 0x02;
	private static Handler mAgent = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			case HANLDER_DRAW_DEBUG:
				if(mTrigger != null){
					mTrigger.drawRect((Rect)msg.obj);
				}
				break;

			case HANLDER_CLEAR_DEBUG:
				if(mTrigger != null){
					mTrigger.clear();
				}
				break;
			}
			
		}
		
	};

	public static interface OnSystemDebugTrigger{
		public void drawRect(Rect rect);
		public void clear();
	}
	
	static public void setOnSystemDebugTrigger(OnSystemDebugTrigger listener){
		mTrigger = listener; 
	}

	/**
	 * 绘制矩形
	 * @param rect
	 */
	static public void drawRect(Rect rect){
		Message msg = new Message();
		msg.what = HANLDER_DRAW_DEBUG;
		msg.obj  = rect;
		mAgent.sendMessage(msg);
	}
	
	/**
	 * 清除屏幕绘制
	 */
	static public void clear(){
		Message msg = new Message();
		msg.what = HANLDER_CLEAR_DEBUG;
		mAgent.sendMessage(msg);
	}
	
}

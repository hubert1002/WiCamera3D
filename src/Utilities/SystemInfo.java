package Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.os.Environment;
import android.util.Log;

/**
 * Copyright (c) 2011 Wistron SWPC
 * All rights reserved.
 * @author Cocoonshu
 * @date 2012-02-28 16:16:55
 * @commit personal library, system information provider
 * @purpose 系统信息读取器
 * @detail
 */
public class SystemInfo {
	private static final String TAG = null;
	public static int    UserCpu   = 0;
	public static int    SystemCpu = 0;
	public static String DetailCpu = null;
	
	private boolean mHaveGot       = false; //已获取信息
	
	/**
	 * 获取系统CPU使用情况的详细信息
	 * @return
	 * @throws IOException
	 */
	public static String getCpu() throws IOException{
		String  info          = "";
		String  result        = null;
		Process systemProcess = Runtime.getRuntime().exec("top -n 1");
		BufferedReader br     = null;
		
		br = new BufferedReader(new InputStreamReader(systemProcess.getInputStream ()));
		DetailCpu = br.toString();
		while((result = br.readLine()) != null)
		{
			if(result.trim().length() < 1){
				continue;
			}else{
				//获取总的CPU使用率
				String[] CPUusr = result.split("[%]");
				if(CPUusr.length == 0)
					continue;
				String[] CPUusage = CPUusr[0].split("User");
				if(CPUusage.length == 0)
					continue;
				String[] SYSusage = CPUusr[1].split("System");
				if(SYSusage.length == 0)
					continue;
				
				if(CPUusage[1].trim().length() != 0)
					UserCpu   = Integer.valueOf(CPUusage[1].trim());
				if(SYSusage[1].trim().length() != 0)
					SystemCpu = Integer.valueOf(SYSusage[1].trim());
				
				info += "USER:" + CPUusr[0] + "\n";
				info += "CPU:"  + CPUusage[1].trim() + " length:" + CPUusage[1].trim().length() + "\n";
				info += "SYS:"  + SYSusage[1].trim() + " length:" + SYSusage[1].trim().length() + "\n";
				info += result  + "\n";
				break;
			}
		}
		
		return info;
	}

	/**
	 * 获取CPU的用户使用量
	 * @return
	 * @throws IOException
	 */
	public static int getUserCpu() throws IOException{
		getCpu();
		
		return UserCpu;
	}
	
	/**
	 * 获取CPU的系统使用量
	 * @return
	 * @throws IOException
	 */
	public static int getSystemCpu() throws IOException{
		getCpu();
		
		return SystemCpu;
	}
	
	/**
	 * 获取特定程序的CPU使用率
	 * @param pkgName
	 * @return
	 * @throws IOException
	 */
	public static int getCpuWithApplicationName(String pkgName) throws IOException{
		int result = 0;
		String line = "";
		String temp = "";
		Process systemProcess = Runtime.getRuntime().exec("top -n 1");
		BufferedReader br     = null;
		
		br = new BufferedReader(new InputStreamReader(systemProcess.getInputStream ()));
		while((line = br.readLine()) != null)
		{
			if(line.trim().length() < 1){
				continue;
			}else{
				if(line.endsWith(pkgName)){
					int    spacecount = 0;
					for(int i = 1; i < line.length() - 1; i++){
						if((line.charAt(i - 1) == ' ')&&(line.charAt(i) != ' ')){
							spacecount++;
						}
						if(spacecount == 2){
							temp += line.charAt(i - 1);
						}
						if(spacecount > 2)
							break;
					}
					break;
				}
			}
		}
		
		try {
			temp = temp.trim();
			temp = temp.substring(0, temp.length() - 1);
			if(temp == null || temp.equals("")){
				result = 0;
			}else{
				result = Integer.valueOf(temp);
			}
		} catch (NumberFormatException e) {
			result = 0;
		} catch (Exception e) {
			result = 0;
		}

		return result;
	}
	
	/**
	 * 获取Sdcard状态
	 * @return Sdcard的状态字符串
	 */
	public static String getSDCardStatus() {
		String status = Environment.getExternalStorageState();
		
		if (status.equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
			//"SD卡存在"
			// TODO
		} else if (status.equalsIgnoreCase(Environment.MEDIA_MOUNTED_READ_ONLY)) {
			//虽然SD存在，但是为只读状态"
		} else if (status.equalsIgnoreCase(Environment.MEDIA_REMOVED)) {
			//"SD不存在"
		} else if (status.equalsIgnoreCase(Environment.MEDIA_SHARED)) {
			//"虽然SD卡存在，但是正与PC等相连接"
		} else if (status.equalsIgnoreCase(Environment.MEDIA_BAD_REMOVAL)) {
			//"SD卡在挂载状态下被错误取出"
		} else if (status.equalsIgnoreCase(Environment.MEDIA_CHECKING)) {
			//"正在检查SD卡"
		} else if (status.equalsIgnoreCase(Environment.MEDIA_NOFS)) {
			//"虽然SD卡存在，但其文件系统不被支持"
		} else if (status.equalsIgnoreCase(Environment.MEDIA_UNMOUNTABLE)) {
			//"虽然SD卡存在，但是无法被挂载"
		} else if (status.equalsIgnoreCase(Environment.MEDIA_UNMOUNTED)) {
			//"虽然SD卡存在，但是未被挂载"
		} else {
			//"其他原因"
		}
		
		return status;
	}
	
	/**
	 * 设定屏幕2/3D模式
	 * @param is3D
	 */
	public void setScreenDimension(boolean is3D){
		String[] cmdTurnOn3D = { //开启屏幕3D命名
				"/system/bin/sh", "-c", 
				"echo 1 > /sys/devices/platform/mipi_masterimage.513/enable_3d_barrier"
		};

		String[] cmdTurnOff3D = { //关闭屏幕3D命令
				"/system/bin/sh", "-c", 
				"echo 0 > /sys/devices/platform/mipi_masterimage.513/enable_3d_barrier"
		};

		try {
			if(is3D){
				if(CSStaticData.DEBUG){
					Log.w(TAG, "[setScreenDimension]开启屏幕3D显示模式");
				}
				Runtime.getRuntime().exec(cmdTurnOn3D);
			}else{
				if(CSStaticData.DEBUG){
					Log.w(TAG, "[setScreenDimension]关闭屏幕3D显示模式");
				}
				Runtime.getRuntime().exec(cmdTurnOff3D);
			}
		} catch (IOException exp) {
			if(CSStaticData.DEBUG){
				Log.w(TAG, "[setScreenDimension]屏幕3D显示模式切换：失败，命令行输入流异常");
			}
		} catch (SecurityException  exp) {
			if(CSStaticData.DEBUG){
				Log.w(TAG, "[setScreenDimension]屏幕3D显示模式切换：失败，系统安全保护阻止本操作");
			}
		} catch (Exception exp) {
			if(CSStaticData.DEBUG){
				Log.w(TAG, "[setScreenDimension]屏幕3D显示模式切换：失败，未知错误");
			}
		}
	}
}

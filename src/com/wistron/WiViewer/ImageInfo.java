package com.wistron.WiViewer;


import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import com.wistron.StreamHelper.MPOHexTAG;

import Utilities.CSStaticData;
import android.R.integer;
import android.content.Context;
import android.media.ExifInterface;
import android.util.Log;
/**
 * 用于读取图片信息
 * 
 **/ 
public class ImageInfo{    
	private static final String TAG = "ImageInfo";
	private Context context; 
	private ImageInfoBean m_info=null;
	private ExifInterface m_exif=null;
	
	
	public ImageInfo(Context context) {
		this.context = context;    
	}        
	/**
	 * 主方法
	 * @author hubert
	 * @param  ImagesPath 图片路径
	 **/ 
	public ImageInfoBean  getImagesInfo(String ImagesPath) {
		if(CSStaticData.DEBUG)
			Log.w(TAG, "[getImageInfo]文件路径 "+ImagesPath);
		File mFile=new File(ImagesPath);
        if(!mFile.exists()){return m_info;}
        m_info=new ImageInfoBean();
		//TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
		 Date date=new Date(mFile.lastModified());
         SimpleDateFormat sdf=new SimpleDateFormat("yyyy.MM.dd/HH:mm:ss");
         String dd=sdf.format(date);
         String[] m_filename;
		 m_filename=ImagesPath.split("\\.");
		 m_info.setM_format(m_filename[m_filename.length-1].toUpperCase());
         
				try
				{
					m_exif=new ExifInterface(ImagesPath);
					String aString= m_exif.getAttribute(ExifInterface.TAG_ORIENTATION);
					
					if(CSStaticData.DEBUG)
						Log.e(TAG, "aString="+aString);
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				m_info.setM_path(ImagesPath);
				m_info.setM_gps_latitude(m_exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE));
				m_info.setM_gps_longitude(m_exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE));
				m_info.setM_image_length(m_exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH));
				m_info.setM_image_width(m_exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH));
				m_info.setM_make(m_exif.getAttribute(ExifInterface.TAG_MAKE));
				m_info.setM_mode(m_exif.getAttribute(ExifInterface.TAG_MODEL));
				m_info.setM_orientation(getImageOrientationForShow(ImagesPath));
				m_info.setM_name(mFile.getName());
				m_info.setM_size(mFile.length());
				m_info.setM_time(dd);
		return m_info;
	}
	/**
	 * 当时图片时查询orientation
	 * @param ImagesPath
	 * @return 0~8
	 */
	public static  int getImageOrientation(String ImagesPath)
	{
		int res=0;
		try
		{
			ExifInterface m_exif=new ExifInterface(ImagesPath);
			String aString= m_exif.getAttribute(ExifInterface.TAG_ORIENTATION);
			 res=Integer.valueOf(aString);
			return res;
		} catch (IOException e)
		{
			if(CSStaticData.DEBUG)
				Log.e(TAG, "查询文件orientation失败");
			e.printStackTrace();
		}
		return res;
	}
	/**
	 * 将orientation映射到旋转角度
	 * @param ImagesPath
	 * @return 90 180 270 0 
	 */
	public static int getImageOrientationForRotate(String ImagesPath)
	{
		int a=getImageOrientation(ImagesPath);
		int  res=0;
		switch (a)
		{
		case 0 :
			//ExifInterface.ORIENTATION_UNDEFINED
			res = 0;
			break;

		case 1:
			//ExifInterface.ORIENTATION_NORMAL
			res = 0;
			break;
			
		case 2:
			//ExifInterface.ORIENTATION_FLIP_HORIZONTAL
			res = 0;
			break;
			
		case 3:
			//ExifInterface.ORIENTATION_ROTATE_180
			res = 180;
			break;
			
		case 4:
			//ExifInterface.ORIENTATION_FLIP_VERTICAL
			res = 180;
			break;
			
		case 5:
			//ExifInterface.ORIENTATION_TRANSPOSE
			res = 90;
			break;
			
		case 6:
			//ExifInterface.ORIENTATION_ROTATE_90
			res = 90;
			break;
		case 7:
			//ExifInterface.ORIENTATION_TRANSVERSE
			res = 270;
			break;
		case 8:
			//ExifInterface.ORIENTATION_ROTATE_270
			res = 270;
			break;
		default:
			res = 0;
			break;
		
		}
		return res;
	}

	/**
	 * 将orientation映射文字描述
	 * @param ImagesPath
	 * @return top-left....
	 */
	public static String getImageOrientationForShow(String ImagesPath)
	{
		int a=getImageOrientation(ImagesPath);
		String  res="Undefind";
		switch (a)
		{
		case 1:
			//ExifInterface.ORIENTATION_NORMAL
			res = "Top-Left";
			break;
			
		case 2:
			//ExifInterface.ORIENTATION_FLIP_HORIZONTAL
			res = "Top-Right";
			break;
			
		case 3:
			//ExifInterface.ORIENTATION_ROTATE_180
			res = "Bottom-Right";
			break;
			
		case 4:
			//ExifInterface.ORIENTATION_FLIP_VERTICAL
			res = "Bottom-Left";
			break;
			
		case 5:
			//ExifInterface.ORIENTATION_TRANSPOSE
			res = "Left-Top";
			break;
			
		case 6:
			//ExifInterface.ORIENTATION_ROTATE_90
			res = "Right-Top";
			break;
		case 7:
			//ExifInterface.ORIENTATION_TRANSVERSE
			res = "Right-Bottom";
			break;
		case 8:
			//ExifInterface.ORIENTATION_ROTATE_270
			res = "Left-Bottom";
			break;
			default:
				//ExifInterface.ORIENTATION_UNDEFINED
				res = "Undefind";
				break;
		}
		return res;
	}

	public static int genNewOrientation(int degree)
	{
		int res = 1;
		switch (degree)
		{
		case 90:
           res=6;
			break;
		case 180:
            res=3;
			break;
		case 270:
            res=8;
			break;
		default:
			res=1;
			break;
		}
		return res;
	}

}
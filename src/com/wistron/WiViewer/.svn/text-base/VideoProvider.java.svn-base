package com.wistron.WiViewer;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import Utilities.CSStaticData;
import android.R.integer;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.AudioColumns;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.MediaColumns;
import android.provider.MediaStore.Video.VideoColumns;
import android.util.Log;
import android.widget.Toast;

/**
 * Copyright (c) 2011 Wistron SWPC
 * All rights reserved.
 * @date 2011-09-27 14:14:20
 */
public class VideoProvider{    
	private static final String TAG = "VideoProvider";
	private Context context;        
	public VideoProvider(Context context) {
		this.context = context;    
	}        
	public VideoInfo  getMediaInfo(String path)
	{
		VideoInfo info = null;
		
		return null;
		
	}
	public VideoInfo getVideoInfo(String videoPath)
	{
		VideoInfo info = null;
	    String mString=null;
		File mFile=new File(videoPath);
		
        if(!mFile.exists()){return info;}
       
		//TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00"));
		 Date date=new Date(mFile.lastModified());
         SimpleDateFormat sdf=new SimpleDateFormat("yyyy.MM.dd/HH:MM:SS");
         String dd=sdf.format(date);
         String[] m_filename;
		 m_filename=mFile.getName().split("\\.");
		 String  m_nameString="";
		 for(int i=0;i<m_filename.length-1;i++)
		 {
			 m_nameString+=m_filename[i];
			 if(i<m_filename.length-2)
				 m_nameString+=".";
		 }
		 
 		 MediaMetadataRetriever retriever = new MediaMetadataRetriever();
 		  try {
 		  retriever.setDataSource(videoPath);
 		  String title1=retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
 			String title6=retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
 			String width=retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
 			String height=retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
 			info=new VideoInfo();
 			info.setTitle(m_nameString);
 			info.setDuration(Integer.parseInt(title1));
 			info.setM_date(dd);
 			info.setM_format(m_filename[m_filename.length-1].toUpperCase());
 			if(width==null||height==null||width.toLowerCase().equals("null")||height.toLowerCase().equals("null"))
 			{
 				info.setM_videoquality("UNknown");
 			}
 			else
 			{
 				info.setM_videoquality(width+"x"+height);
 			}
 			info.setSize(mFile.length());
 			info.setM_framerate("Unknown");
 			info.setM_AudioChannel("Unknown");
 		  } catch (IllegalArgumentException ex) {
 		  // Assume this is a corrupt video file
 		  } catch (RuntimeException ex) {
 		  // Assume this is a corrupt video file.
 		  } finally {
 		  try {
 		  retriever.release();
 		  } catch (RuntimeException ex) {
 		  // Ignore failures while cleaning up.
 		  }
 		  }
		
		return info;
		
	}
	/**
	 * 获取单个文件的视频信息
	 * @param videoPath
	 * @return 视频信息
	 */
	public VideoInfo getVideoInfoDeprecated(String videoPath) {
		Log.w(TAG, "[getVideoInfo]传入路径： "+videoPath);	
		VideoInfo info = null;
		 final int sdkVersion = Integer.parseInt(Build.VERSION.SDK);
		  if (sdkVersion < 10){
		  String where = MediaColumns.DATA + "=?";   
          String[] selectionArgs = new String[] { videoPath };   
          try
		{
        	  if(context != null){
      				Cursor cursor = context.getContentResolver().query(                    
      						 MediaStore.Video.Media.EXTERNAL_CONTENT_URI, 
      						null, 
      					   where,                    
      						selectionArgs,
      						null); 
      			if (cursor != null) {    
      				   cursor.moveToFirst();        
      					String title = cursor                            
      							.getString(cursor                                    
      									.getColumnIndexOrThrow(MediaColumns.TITLE));                    
      					String album = cursor                            
      							.getString(cursor                                    
      									.getColumnIndexOrThrow(VideoColumns.ALBUM));                    
      					String artist = cursor                            
      							.getString(cursor                                    
      									.getColumnIndexOrThrow(VideoColumns.ARTIST));                    
      					String displayName = cursor                            
      							.getString(cursor                                    
      									.getColumnIndexOrThrow(MediaColumns.DISPLAY_NAME));                    
      					String mimeType = cursor                            
      							.getString(cursor                                    
      									.getColumnIndexOrThrow(MediaColumns.MIME_TYPE));                    
      					String path = cursor                            
      							.getString(cursor                                    
      									.getColumnIndexOrThrow(MediaColumns.DATA));                    
      					long duration = cursor                            
      							.getInt(cursor                                    
      									.getColumnIndexOrThrow(VideoColumns.DURATION));                    
      					long size = cursor                            
      							.getLong(cursor                                    
      									.getColumnIndexOrThrow(MediaColumns.SIZE));  
      				}              
      		}
		} catch (Exception e)
		{
			// TODO: handle exception
		}
		
	}
		return info;
	}
	
}
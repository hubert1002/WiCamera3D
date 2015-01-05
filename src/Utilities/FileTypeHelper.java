package Utilities;

import java.io.File;
import java.util.List;

import Utilities.CSStaticData.MEDIA_META_TYPE;
import android.R.bool;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.provider.MediaStore.Video.VideoColumns;

/**
 * Copyright (c) 2011 Wistron SWPC
 * All rights reserved.
 * @author WH1107028
 * @date 2011-09-27 14:14:20
 * @purpose 识别文件类型，对文件做毛坯处理操作
 * @detail  
 */
public class FileTypeHelper{
	public static final int TYPE_IMAGE_2D   = 0x200; //2D图片
	public static final int TYPE_IMAGE_3DJ  = 0x201; //3D图片
	public static final int TYPE_IMAGE_3DS  = 0x202; //3D图片
	public static final int TYPE_VIDEO_2D   = 0x203; //2D视频
	public static final int TYPE_VIDEO_3DJ  = 0x204; //3D视频
	public static final int TYPE_VIDEO_3DS  = 0x205; //3D视频
	public static final int TYPE_UNKNOWN    = 0x206; //未知

	private static String 
	imageSuffixs2D[] = new String[]{
		".jpg",
		".png",
		".jpeg",
		".jpe",
	},
	imageSuffixs3DJ[] = new String[]{
		".jps"
	},
	imageSuffixs3DS[] = new String[]{
		".mpo"
	},
	videoSuffixs2D[] = new String[]{
		".mp4",
		".3gp"
	},
	videoSuffixs3DJ[] = new String[]{},
	videoSuffixs3DS[] = new String[]{};

	/**
	 * 获取文件类型
	 * @param filePath
	 * @return
	 */
	public static int getFileType(String filePath){
		
		if(filePath == null){
			return TYPE_UNKNOWN;
		}
		
		File file = new File(filePath);
		int type = TYPE_UNKNOWN;

		if(file.exists()){
			for (String type_iter : imageSuffixs2D) {
				if(file.getAbsolutePath().toString().toLowerCase().endsWith(type_iter)==true){
					type = TYPE_IMAGE_2D;
					return type;
				}
			}
			for (String type_iter : imageSuffixs3DJ) {
				if(file.getAbsolutePath().toString().toLowerCase().endsWith(type_iter)==true){
					type = TYPE_IMAGE_3DJ;
					return type;
				}
			}
			for (String type_iter : imageSuffixs3DS) {
				if(file.getAbsolutePath().toString().toLowerCase().endsWith(type_iter)==true){
					type = TYPE_IMAGE_3DS;
					return type;
				}
			}
			for (String type_iter : videoSuffixs2D) {
				if(file.getAbsolutePath().toString().toLowerCase().endsWith(type_iter)==true){
					type = TYPE_VIDEO_2D;
					return type;
				}
			}
			for (String type_iter : videoSuffixs3DJ) {
				if(file.getAbsolutePath().toString().toLowerCase().endsWith(type_iter)==true){
					type = TYPE_VIDEO_3DJ;
					return type;
				}
			}
			for (String type_iter : videoSuffixs3DS) {
				if(file.getAbsolutePath().toString().toLowerCase().endsWith(type_iter)==true){
					type = TYPE_VIDEO_3DS;
					return type;
				}
			}

		}

		return type;
	}

	/**
	 * 把3D缩略图转换成左右两张分立的2D缩略图 
	 * @param m_srcbmp 要转换的3D图片
	 * @param destWidth 目标图片的宽
	 * @param destHeight 目标图片的高
	 * @return 已完成转换的2D图片
	 */
	public static Bitmap[] image3DTo2D(Bitmap srcbmp, int destWidth, int destHeight){
		Bitmap[] desbmp = new Bitmap[2];

		if(srcbmp == null){
			desbmp[0] = desbmp[1] = null;
			return desbmp;
		}
		
		//获取左图
		desbmp[0] = Bitmap.createBitmap(srcbmp, 0, 0, srcbmp.getWidth()/2, srcbmp.getHeight());
		desbmp[0] = FileOperation.cutBitmapWithProportion(desbmp[0], destWidth, destHeight, true);

		//获取右图
		desbmp[1] = Bitmap.createBitmap(srcbmp, srcbmp.getWidth()/2, 0, srcbmp.getWidth()/2, srcbmp.getHeight());
		desbmp[1] = FileOperation.cutBitmapWithProportion(desbmp[1], destWidth, destHeight, true);

		//回收原图,慎用！！！
		//m_srcbmp.recycle();

		return desbmp;
	}
	
	/**
	 * 缩放MPO的解出图片
	 * @param srcbmp
	 * @param destWidth
	 * @param destHeight
	 * @return
	 */
	public static Bitmap[] scaleMPOImage(Bitmap[] srcbmp, int destWidth, int destHeight){
		Bitmap[] result = new Bitmap[2];
		
		if(srcbmp == null || srcbmp.length == 0){
			result[0] = result[1] = null;
			return result;
		}
		
		if(srcbmp[0] != null){
			result[0] = FileOperation.cutBitmapWithProportion(srcbmp[0], destWidth, destHeight, true);
			srcbmp[0].recycle();
		}
		if(srcbmp[1] != null){
			result[1] = FileOperation.cutBitmapWithProportion(srcbmp[1], destWidth, destHeight, true);
			srcbmp[1].recycle();
		}
		
		return result;
	}
	
	/**
	 * 判断文件是否是视频
	 * @param path 文件路径
	 * @return
	 */
	public static boolean isVideoFile(String path){
		
		if(path == null){
			return false;
		}
		
		for(int i = 10; i <= 21; i++){ //SUPPORT_SUF[10...21]为视频格式
			if(path.toLowerCase().endsWith(CSStaticData.SUPPORT_SUF[i])){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断文件是否是图片
	 * @param path
	 * @return
	 */
	public static boolean isImageFile(String path){
		
		if(path == null){
			return false;
		}
		
		for(int i = 0; i <= 9; i++){ //SUPPORT_SUF[0...9]为图片格式
			if(path.toLowerCase().endsWith(CSStaticData.SUPPORT_SUF[i])){
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * 判断文件是否是立体视频
	 * @param path
	 * @return
	 */
	public static boolean isStereoVideoFile(String path){
		boolean  result      = false;
		String   strTypePart = "";
		String   strSuffPart = "";
		String[] strTemp     = null;
		
		if(path == null || path.equals("")){
			result = false;
		}else{
			try{
				//截取类型部分
				strTemp = path.split("/");
				if(strTemp != null && strTemp.length > 0){
					strTypePart = strTemp[0];
				}
				
				//截取后缀部分
				strTemp = path.split("-");
				if(strTemp != null && strTemp.length > 1){
					strSuffPart = strTemp[strTemp.length - 1];
				}
				
				//判断类型
				if(strTypePart.equalsIgnoreCase("video") && strSuffPart.equalsIgnoreCase("3d")){
					result = true;
				}
			}catch (Exception exp) {
				result = false;
			}
		}
		
		return result;
	}
	
	/**
	 * 判断文件是否是立体图片
	 * @param path
	 * @return
	 */
	public static boolean isStereoImageFile(String path){
		
		if(path == null){
			return false;
		}
		
		if(
				path.toLowerCase().endsWith(CSStaticData.SUPPORT_SUF[4])//".jps"
				||
				path.toLowerCase().endsWith(CSStaticData.SUPPORT_SUF[6])//".mpo"
				){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean isStereoVideoFileWithFilepath(ContentResolver resolver, String filepath){
		boolean  result      = false;
		String   strTypePart = "";
		String   strSuffPart = "";
		String[] strTemp     = null;
		String   path=null;
		if(resolver!=null)
		{
			
			  String where = MediaColumns.DATA + "=?";   
	          String[] selectionArgs = new String[] { filepath };   
		     try
				{
		      				Cursor cursor =resolver.query(                    
		      						 MediaStore.Video.Media.EXTERNAL_CONTENT_URI, 
		      						null, 
		      					   where,                    
		      						selectionArgs,
		      						null); 
		      			if (cursor != null) {    
		      				   cursor.moveToFirst();        
		      					String mimeType = cursor                            
		      							.getString(cursor                                    
		      									.getColumnIndexOrThrow(MediaColumns.MIME_TYPE)); 
		      					path=mimeType;
		      				}              
				} catch (Exception e)
				{
					// TODO: handle exception
				}
		}
		if(path == null || path.equals("")){
			result = false;
		}else{
			try{
				//截取类型部分
				strTemp = path.split("/");
				if(strTemp != null && strTemp.length > 0){
					strTypePart = strTemp[0];
				}
				
				//截取后缀部分
				strTemp = path.split("-");
				if(strTemp != null && strTemp.length > 1){
					strSuffPart = strTemp[strTemp.length - 1];
				}
				
				//判断类型
				if(strTypePart.equalsIgnoreCase("video") && strSuffPart.equalsIgnoreCase("3d")){
					result = true;
				}
			}catch (Exception exp) {
				result = false;
			}
		}
		
		return result;
	}
	
	/**
	 * 是否为支持的文件格式
	 * @param path
	 * @return
	 */
	public static boolean isSupportedFile(String path){
		
		if(path == null){
			return false;
		}
		
		for(int i = 0; i <= CSStaticData.SUPPORT_SUF.length; i++){ //SUPPORT_SUF为支持的格式
			if(path.toLowerCase().endsWith(CSStaticData.SUPPORT_SUF[i])){
				return true;
			}
		}
		
		return false;
	}

	/**
	 * 判断文件是否在内部存储器上
	 * @param filepath
	 * @return
	 */
	public static boolean isInternalFile(String filepath) {
	
		if(filepath != null && !filepath.equals("")){
			if( filepath.indexOf(CSStaticData.TMP_INT_DIR) != -1){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	/**
	 * 判断文件是否在外部存储器上
	 * @param filepath
	 * @return
	 */
	public static boolean isExternalFile(String filepath){
		
		if(filepath != null && !filepath.equals("")){
			if( filepath.indexOf(CSStaticData.TMP_EXT_DIR) != -1){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	/**
	 * 从文件路径中获取文件名
	 * @param filePath
	 * @return
	 */
	public static String getFileNameFromPath(String filePath){
		String result[] = null;
		
		if(filePath != null && !filePath.equals("")){
			result = filePath.split("/");
			if(result != null && result.length > 0){
				return result[result.length - 1];
			}else{
				return filePath;
			}
		}else{
			return filePath;
		}
	}
	/**
	 * 获取当前选取的文件的媒体类型
	 * @param list
	 * @return
	 */
	public static MEDIA_META_TYPE getMetaTypeInSelectedFiles(List<String> list){
		int fileSize = 0;
		int imageSize = 0;
		int videoSize = 0;
		
		if(list == null || list.size() == 0){
			return MEDIA_META_TYPE.ALL_MEDIA_TYPE;
		}
		
		fileSize = list.size();
		for(int i = 0; i < fileSize; i++){
			if(FileTypeHelper.isImageFile(list.get(i))){
				imageSize++;
				continue;
			}
			if(FileTypeHelper.isVideoFile(list.get(i))){
				videoSize++;
				continue;
			}
		}
		
		if(imageSize == fileSize){
			return MEDIA_META_TYPE.IMAGE_MEDIA_TYPE;
		}else if(videoSize == fileSize){
			return MEDIA_META_TYPE.VIDEO_MEDIA_TYPE;
		}else{
			return MEDIA_META_TYPE.ALL_MEDIA_TYPE;
		}
	}
}


package Utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.wistron.WiGallery.Element;
import com.wistron.WiViewer.ImageInfo;

import Utilities.CSStaticData.STORAGE_TYPE;
import android.database.CursorJoiner.Result;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;

public class FileOperation {
	
	private final String[][] MIME_MapTable={ 
			{".3gp",	"video/3gpp"},
			{".apk",	"application/vnd.android.package-archive"},
			{".asf",	"video/x-ms-asf"},
			{".avi",	"video/x-msvideo"},
			{".bin",	"application/octet-stream"},
			{".bmp",  	"image/bmp"},
			{".c",		"text/plain"},
			{".class",	"application/octet-stream"},
			{".conf",	"text/plain"},
			{".cpp",	"text/plain"},
			{".doc",	"application/msword"},
			{".exe",	"application/octet-stream"},
			{".gif",	"image/gif"},
			{".gtar",	"application/x-gtar"},
			{".gz",		"application/x-gzip"},
			{".h",		"text/plain"},
			{".htm",	"text/html"},
			{".html",	"text/html"},
			{".jar",	"application/java-archive"},
			{".java",	"text/plain"},
			{".jpeg",	"image/jpeg"},
			{".jpg",	"image/jpeg"},
			{".js",		"application/x-javascript"},
			{".log",	"text/plain"},
			{".m3u",	"audio/x-mpegurl"},
			{".m4a",	"audio/mp4a-latm"},
			{".m4b",	"audio/mp4a-latm"},
			{".m4p",	"audio/mp4a-latm"},
			{".m4u",	"video/vnd.mpegurl"},
			{".m4v",	"video/x-m4v"},	
			{".mov",	"video/quicktime"},
			{".mp2",	"audio/x-mpeg"},
			{".mp3",	"audio/x-mpeg"},
			{".mp4",	"video/mp4"},
			{".mpc",	"application/vnd.mpohun.certificate"},		
			{".mpe",	"video/mpeg"},	
			{".mpeg",	"video/mpeg"},	
			{".mpg",	"video/mpeg"},	
			{".mpg4",	"video/mp4"},	
			{".mpga",	"audio/mpeg"},
			{".msg",	"application/vnd.ms-outlook"},
			{".ogg",	"audio/ogg"},
			{".pdf",	"application/pdf"},
			{".png",	"image/png"},
			{".pps",	"application/vnd.ms-powerpoint"},
			{".ppt",	"application/vnd.ms-powerpoint"},
			{".prop",	"text/plain"},
			{".rar",	"application/x-rar-compressed"},
			{".rc",		"text/plain"},
			{".rmvb",	"audio/x-pn-realaudio"},
			{".rtf",	"application/rtf"},
			{".sh",		"text/plain"},
			{".tar",	"application/x-tar"},	
			{".tgz",	"application/x-compressed"}, 
			{".txt",	"text/plain"},
			{".wav",	"audio/x-wav"},
			{".wma",	"audio/x-ms-wma"},
			{".wmv",	"audio/x-ms-wmv"},
			{".wps",	"application/vnd.ms-works"},
			//{".xml",	"text/xml"},
			{".xml",	"text/plain"},
			{".z",		"application/x-compress"},
			{".zip",	"application/zip"},
			{"",		"*/*"}	
		};


	/**
	 * 把List<File>转换成ArrayList<String>，以便Intent发送
	 * @param input 
	 * @return
	 */
	public ArrayList<String> FileList2ArrayList(List<File> input){
		ArrayList<String> output = new ArrayList<String>();
		
		for(File fin:input){
			output.add(fin.getAbsolutePath());
		}
		
		return output;
	}
	
	
	public List<File> ArrayList2FileList(ArrayList<String> input){
		List<File> output = new ArrayList<File>();
		
		for(String in:input){
			output.add(new File(in));
		}
		
		return output;
	}
	
	/**
	 * 按比例剪裁缩放图片
	 * @param src
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap cutBitmapWithProportion(Bitmap src, int destWidth, int destHeight, boolean filter){
		int    srcWidth    = 0,
			   srcHeight   = 0;
		int    transWidth  = 0,
			   transHeight = 0,
			   transXpos   = 0,
			   transYpos   = 0;
		Bitmap result      = null;
		
		if(src == null){
			return null;
		}
		
		srcWidth = src.getWidth();
		srcHeight = src.getHeight();

		//计算缩放后的宽高
		if((srcWidth - destWidth) != (srcHeight - destHeight)){
			if((srcWidth - destWidth) > (srcHeight - destHeight)){
				//缩放至srcHeight == destHeight
				transHeight = destHeight;
				transWidth  = (int)((float)srcWidth * ((float)destHeight/(float)srcHeight));
			}else{
				//缩放至srcWidth == destWidth
				transWidth  = destWidth;
				transHeight = (int)((float)srcHeight * ((float)destWidth/(float)srcWidth));
			}
		}else{
			//无需缩放及剪裁
			return src;
		}
		
		//缩放
		try {
			result = Bitmap.createScaledBitmap(src, transWidth, transHeight, filter);
			src.recycle();
		} catch (OutOfMemoryError exp) {
			return null;
		} catch (Exception exp) {
			// Do nothing
		}
		
		
		//计算剪裁的坐标
		if(result != null){
			srcWidth  = result.getWidth();
			srcHeight = result.getHeight();
			if(srcWidth == destWidth && srcHeight == destHeight){
				//无需剪裁
				return result;
			}else{
				transWidth  = destWidth;
				transHeight = destHeight;
				transXpos   = (srcWidth - destWidth)/2;
				transYpos   = (srcHeight - destHeight)/2;
			}
		}
		
		//剪裁
		src    = result;
		result = Bitmap.createBitmap(src, transXpos, transYpos, transWidth, transHeight);
		try {
			src.recycle();
		} catch (Exception exp) {
			//Do nothing
		}
		
		return result;
	}

	
	/**
	 * 为防止内存溢出做的图片按比例缩小的方法-path
	 * 
	 * @param path
	 * @param maxLength
	 * @return Bitmap
	 */
	public static Bitmap fitSizeImg(String path, int maxLength) {
		if (path == null || path.length() < 1) return null;
		try {
			//File file = new File(path);
			Options opts = new Options();
			opts.inJustDecodeBounds = true; // 当为true时，不为图片分配内存，只获取图片的大小，并保存在opts的outWidth和outHeight
			//BitmapFactory.decodeFile(file.getPath(), opts);
			BitmapFactory.decodeFile(path, opts);
			int srcWidth   = opts.outWidth;
			int srcHeight  = opts.outHeight;
			int destWidth  = 0;
			int destHeight = 0;
			double ratio   = 0.0;
			// 对图片进行压缩，是在读取的过程中进行压缩，而不是把图片读进了内存再进行压缩
			Options newOpts = new Options();
			// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
			// 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，
			// 其值表明缩放的倍数，SDK中建议其值是2的指数值
			if ((srcWidth - srcHeight)>10) {
				ratio 				 = srcWidth / maxLength;
				destHeight 			 = maxLength;
				destWidth 			 = (int) (srcWidth / ratio);
				newOpts.inSampleSize = (int) ratio + 1;
			} else if (srcHeight-srcWidth >10) {
				ratio				 = srcHeight / maxLength;
				destWidth			 = maxLength;
				destHeight			 = (int) (srcHeight / ratio);
				newOpts.inSampleSize = (int) ratio + 1;
			}else {
				ratio				 = srcHeight / maxLength;
				destHeight			 = maxLength;
				destWidth			 = maxLength;
				newOpts.inSampleSize = (int) ratio;
			}
			// inJustDecodeBounds设为false表示把图片读进内存中
			newOpts.inJustDecodeBounds = false;
			// 设置大小，这个一般是不准确的，是以inSampleSize的为准
			newOpts.outHeight = destHeight;
			newOpts.outWidth  = destWidth;
			Bitmap destBm = BitmapFactory.decodeFile(path, newOpts);
			//Bitmap thumbBmp = Bitmap.createScaledBitmap(destBm, MAX_IMAGE_WIDTH, MAX_IMAGE_HEIGHT, true);
			return destBm;
		} catch (Exception exp) {
			exp.printStackTrace();
			return null;
		} catch (OutOfMemoryError exp) {
			exp.printStackTrace();
			return null;
		}
	}
	
	
	
	/**
	 * 按字节长度截取字符串
	 * @param src 原字符串
	 * @param desLength 要截取的长度
	 * @param suffix 要在字符串后添加的后缀
	 * @return 
	 */
	public static String cutString(String src, int desLength, String suffix){
		int strlen = 0;
		int orglen = 0; //原长
		String  result   = "";
		char [] temp     = null;
		byte[]  signByte = null;
		
		if(src == null){
			return "";
		}
		
		src = src.trim();
		src = new String(src.getBytes(),Charset.defaultCharset());

		orglen = src.getBytes().length;
		temp = src.toCharArray();
		
//		if(temp.length <= desLength){
//			return src;
//		}

		//字长处理
		
		for(int i = 0; (i < temp.length && strlen < desLength); i++){
			signByte = src.valueOf(temp[i]).getBytes();
			strlen  += signByte.length;
			result  += temp[i];
			signByte = null;
		}
		
//		if(desLength < result.getBytes().length ){
//			byte [] temp = new byte[desLength];
//			for(int i = 0 ; i < desLength; i++){
//				try {
//					temp[i] = result.getBytes("ISO8859-1")[i];
//				} catch (UnsupportedEncodingException e) {
//					e.printStackTrace();
//				}
//			}
//			result = new String(temp, Charset.defaultCharset());
//		}
    	
		
		if(orglen > strlen){
			result += suffix;
		}
		
		return result;
	}
	
	public static String substringByByte(String src, int length, String suffix)  
	{   

		byte[] bytes;
		int orglen = 0;
		String result = "";

		try {
			bytes  = src.getBytes("Unicode");
			orglen = bytes.length;
			int n  = 0; // 表示当前的字节数   
			int i  = 2; // 要截取的字节数，从第3个字节开始   
			for (; i < bytes.length && n < length; i++)   
			{   
				// 奇数位置，如3、5、7等，为UCS2编码中两个字节的第二个字节   
				if (i % 2 == 1)   
				{   
					n++; // 在UCS2第二个字节时n加1   
				}   
				else  
				{   
					// 当UCS2编码的第一个字节不等于0时，该UCS2字符为汉字，一个汉字算两个字节   
					if (bytes[i] != 0)   
					{   
						n++;   
					}   
				}   
			}   
			// 如果i为奇数时，处理成偶数   
			if (i % 2 == 1)  
			{   
				// 该UCS2字符是汉字时，去掉这个截一半的汉字   
				if (bytes[i - 1] != 0)   
					i = i - 1;   
				// 该UCS2字符是字母或数字，则保留该字符   
				else  
					i = i + 1;   
			}   

			result = new String(bytes, 0, i, "Unicode");
			
			if(orglen > length){
				result += suffix;
			}
			
			return result;   
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return cutString(src,length,suffix);
		} 
	}
	
	/**
	 * Double转String
	 * @param src
	 * @param prec 截取的小数位数
	 * @return
	 */
	public static String double2String(double src, int prec){
		String   result    = "" + src;
		String[] strParts  = null;
		
		if(Double.isNaN(src)){
			return result;
		}
		
		//截断小数点
		strParts = result.split("[.]");
		if(strParts != null && strParts.length >= 2){
			if(strParts[1].length() > prec){ //截断小数位数
				strParts[1] = strParts[1].substring(0, prec);
			}else { //补充小数位数
				//先不做操作
			}
			
			//拼接结果
			result = strParts[0] + "." + strParts[1];
		}

		return result;
	}
	
	/**
	 * 截取Datetime中的Date信息，仅Gallery使用
	 * @param datetime
	 * @return
	 */
	public static String getDate(String datetime){
		String   result      = "";
		String[] dateTimeRes = null;
		
		if(datetime == null || datetime.length() < 9){
			return datetime;
		}
		
		dateTimeRes = datetime.split(" ");
		if(dateTimeRes != null && dateTimeRes.length > 3){
				result = dateTimeRes[0] + " " + dateTimeRes[1] + " " + dateTimeRes[2];
		}
		
		return result;
	}
	
	/**
	 * 截取Datetime中的Time信息，仅Gallery使用
	 * @param datetime
	 * @return
	 */
	public static String getTime(String datetime){
		String   result      = "";
		String[] dateTimeRes = null;
		
		if(datetime == null || datetime.length() < 9){
			return datetime;
		}
		
		dateTimeRes = datetime.split(" ");
		if(dateTimeRes != null && dateTimeRes.length >= 6){
			result = dateTimeRes[3] + " " + dateTimeRes[4] + " " + dateTimeRes[5];
		}
		
		return result;
	}
	
	/**
	 * 是否存在Pullox内部目录
	 * @return
	 */
	public static boolean hasInternalStorage(){
		boolean result = false;
		File    fin    = null;
		
		fin = new File(CSStaticData.TMP_INT_DIR);
		result = fin.exists();
		fin = null;
		
		return result;
	}
	
	/**
	 * 是否存在Pullox外部目录
	 * @return
	 */
	public static boolean hasExternalStorage(){
		boolean result = false;
		File    fin    = null;
		
		fin = new File(CSStaticData.TMP_EXT_DIR);
		result = fin.exists();
		fin = null;
		
		return result;
	}
	
	/**
	 * 判断文件处于内部还是外部存储器
	 * @param path
	 * @return
	 */
	public static STORAGE_TYPE whereIsThisFile(String path){
		STORAGE_TYPE result = STORAGE_TYPE.NONE;
		
		if(path != null && !path.equals("")){
			if(path.startsWith(CSStaticData.TMP_EXT_DIR)){
				result = STORAGE_TYPE.EXTERNAL_DIR;
			}
			if(path.startsWith(CSStaticData.TMP_INT_DIR)){
				result = STORAGE_TYPE.INTERNAL_DIR;
			}
		}
		
		return result;
	}
	
	/**
	 * 保存图片到文件系统
	 * @param bmp
	 * @param filePath
	 * @param underAsync 是否异步处理
	 */
	static public void saveBitmapOnFS(final Bitmap bmp, final String filePath, boolean underAsync){
		if(underAsync){
			Thread thrAsyncSave = new Thread(new Runnable() {
				
				@Override
				public void run() {
					writeBitmapToFS(bmp, filePath);
				}
			});
			thrAsyncSave.start();
		}else{
			writeBitmapToFS(bmp, filePath);
		}
	}
	
	static private void writeBitmapToFS(Bitmap bmp, String filePath){
		//创建文件
		File desFile = new File(filePath);
		if(!desFile.getParentFile().exists()){
			desFile.getParentFile().mkdirs();
		}
		if(!desFile.exists()){
			try {
				if(!desFile.createNewFile()){
					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//创建流
		FileOutputStream fout;
		try {
			fout = new FileOutputStream(desFile);
			bmp.compress(CompressFormat.JPEG, 100, fout);
			fout.close();
		} catch (FileNotFoundException exp) {
			exp.printStackTrace();
		} catch (IOException exp) {
			// TODO Auto-generated catch block
			exp.printStackTrace();
		}
	}
	
	/**
	 * 快速排序法,升序
	 * @param srcList
	 * @return
	 */
	public static List<Element> quickSortByAsc(List<Element> srcList){
		Element       temp   = null;
		String        key    = null;
		int           start  = 0,
				      end    = 0;
		
		if(srcList == null || srcList.size() == 0){
			return srcList;
		}
		
		start = 0;
		end   = srcList.size() - 1;
		key   = srcList.get(end/2).getDate();
		
		while(start < end){
			while(start < end){
				if(srcList.get(end).getDate().compareTo(key) < 0){
					temp = srcList.get(end);
					srcList.set(end, srcList.get(start));
					srcList.set(start, temp);
					temp = null;
					break;
				}else{
					end --;
				}
			}
			while(start < end){
				if(srcList.get(start).getDate().compareTo(key) > 0){
					temp = srcList.get(end);
					srcList.set(end, srcList.get(start));
					srcList.set(start, temp);
					temp = null;
					break;
				}else{
					start ++;
				}
			}
		}
		
		System.gc();
		
		return srcList;
	}
	
	/**
	 * 快速排序法,降序
	 * @param srcList
	 * @return
	 */
	public static List<Element> quickSortByDesc(List<Element> srcList){
		Element       temp   = null;
		String        key    = null;
		int           start  = 0,
				      end    = 0;
		
		if(srcList == null || srcList.size() == 0){
			return srcList;
		}
		
		start = 0;
		end   = srcList.size() - 1;
		key   = srcList.get(end/2).getDate();
		
		while(start < end){
			while(start < end){
				if(srcList.get(end).getDate().compareTo(key) > 0){
					temp = srcList.get(end);
					srcList.set(end, srcList.get(start));
					srcList.set(start, temp);
					temp = null;
					break;
				}else{
					end --;
				}
			}
			while(start < end){
				if(srcList.get(start).getDate().compareTo(key) < 0){
					temp = srcList.get(end);
					srcList.set(end, srcList.get(start));
					srcList.set(start, temp);
					temp = null;
					break;
				}else{
					start ++;
				}
			}
		}
		
		System.gc();
		
		return srcList;
	}

	/**
	 * 用Exif Tag旋转图片
	 * @param bitmap
	 * @param requestListItemName
	 * @return
	 */
	public static Bitmap ratoteBitmapWithExifTag(Bitmap bitmap, String requestListItemName) {
		// 1. 通过ExifInterface(requestListItemName)来查询旋转角度
		// 2. 旋转bitmap
		// TODO
		Bitmap        result      = null;
		ExifInterface exif        = null;
		int           rotateValue = -1;
		int           rotateRadio = -1;
		Matrix        matrix      = new Matrix();
		
		
		if(requestListItemName == null || requestListItemName.equals("")){
			return bitmap;
		}
		
		rotateValue = ImageInfo.getImageOrientation(requestListItemName);
		
		switch (rotateValue)
		{
		case 0 :
			//ExifInterface.ORIENTATION_UNDEFINED
		case 1:
			//ExifInterface.ORIENTATION_NORMAL		
		case 2:
			//ExifInterface.ORIENTATION_FLIP_HORIZONTAL
			rotateRadio = 0;
			return bitmap;
			
		case 3:
			//ExifInterface.ORIENTATION_ROTATE_180	
		case 4:
			//ExifInterface.ORIENTATION_FLIP_VERTICAL
			rotateRadio = 180;
			matrix.postRotate(180);
			result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			break;
			
		case 5:
			//ExifInterface.ORIENTATION_TRANSPOSE
		case 6:
			//ExifInterface.ORIENTATION_ROTATE_90
			rotateRadio = 90;
			matrix.postRotate(90);
			result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getHeight(), bitmap.getWidth(), matrix, true);
			break;
			
		case 7:
			//ExifInterface.ORIENTATION_TRANSVERSE
		case 8:
			//ExifInterface.ORIENTATION_ROTATE_270
			rotateRadio = 270;
			matrix.postRotate(270);
			result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getHeight(), bitmap.getWidth(), matrix, true);
			break;
		default:
			rotateRadio = 0;
			result = bitmap;
			return bitmap;
		}
		
		if(result == null){
			result = bitmap;
		}else{
			if(bitmap != null && !bitmap.isRecycled()){
				bitmap.recycle();
				bitmap = null;
			}
		}
		
		System.gc();

		return result;
	}

	/**
	 * 通过文件获取GPS信息
	 * @param file
	 * @return
	 */
	public static double[] getGPSInfoFromFile(String file){
		ExifInterface exif = null;
		float[]      output = new float[2];
		double[]     result = new double[2];
		
		if(file == null || file.equals("")){
			return result;
		}
		
		try {
			exif = new ExifInterface(file);
			if(exif.getLatLong(output)){
				if(output != null && output.length == 2){
					result[0] = output[0];
					result[1] = output[1];
				}
			}else{
				result = new double[2];
			}
		} catch (IOException exp) {
			exp.printStackTrace();
			result = new double[2];
		}
		
		return result;
	}
	
	/**
	 * 用Exif Tag旋转图片
	 * @param bitmap
	 * @param requestListItemName
	 * @return
	 */
	public static Bitmap[] ratoteBitmapWithExifTag(Bitmap[] srcBmp, String requestListItemName) {
		// 1. 通过ExifInterface(requestListItemName)来查询旋转角度
		// 2. 旋转bitmap
		// TODO
		Bitmap[]      result      = new Bitmap[2];
		ExifInterface exif        = null;
		int           rotateValue = -1;
		int           rotateRadio = -1;
		Matrix        matrix      = new Matrix();
		
		
		if(requestListItemName == null || requestListItemName.equals("")){
			return srcBmp;
		}
		
		try {
			exif = new ExifInterface(requestListItemName);
			rotateValue = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
		} catch (IOException exp) {
			// TODO Auto-generated catch block
			exp.printStackTrace();
		}
		
		if(rotateRadio == -1){
			return srcBmp;
		}
		
		switch (rotateValue)
		{
		case 0 :
			//ExifInterface.ORIENTATION_UNDEFINED
		case 1:
			//ExifInterface.ORIENTATION_NORMAL		
		case 2:
			//ExifInterface.ORIENTATION_FLIP_HORIZONTAL
			rotateRadio = 0;
			result = srcBmp;
			break;
			
		case 3:
			//ExifInterface.ORIENTATION_ROTATE_180	
		case 4:
			//ExifInterface.ORIENTATION_FLIP_VERTICAL
			rotateRadio = 180;
			matrix.postRotate(180);
			result[0] = Bitmap.createBitmap(srcBmp[0], 0, 0, srcBmp[0].getWidth(), srcBmp[0].getHeight(), matrix, true);
			result[1] = Bitmap.createBitmap(srcBmp[1], 0, 0, srcBmp[1].getWidth(), srcBmp[1].getHeight(), matrix, true);
			break;
			
		case 5:
			//ExifInterface.ORIENTATION_TRANSPOSE
		case 6:
			//ExifInterface.ORIENTATION_ROTATE_90
			rotateRadio = 90;
			matrix.postRotate(90);
			result[0] = Bitmap.createBitmap(srcBmp[0], 0, 0, srcBmp[0].getHeight(), srcBmp[0].getWidth(), matrix, true);
			result[1] = Bitmap.createBitmap(srcBmp[1], 0, 0, srcBmp[1].getHeight(), srcBmp[1].getWidth(), matrix, true);
			break;
			
		case 7:
			//ExifInterface.ORIENTATION_TRANSVERSE
		case 8:
			//ExifInterface.ORIENTATION_ROTATE_270
			rotateRadio = 270;
			matrix.postRotate(270);
			result[0] = Bitmap.createBitmap(srcBmp[0], 0, 0, srcBmp[0].getHeight(), srcBmp[0].getWidth(), matrix, true);
			result[1] = Bitmap.createBitmap(srcBmp[1], 0, 0, srcBmp[1].getHeight(), srcBmp[1].getWidth(), matrix, true);
			break;
		default:
			rotateRadio = 0;
			break;
		}
		
		if(result == null){
			result = srcBmp;
		}else{
			if(srcBmp != null && srcBmp[0] != null && !srcBmp[0].isRecycled()){
				srcBmp[0].recycle();
			}
			if(srcBmp != null && srcBmp[1] != null && !srcBmp[1].isRecycled()){
				srcBmp[1].recycle();
			}
		}
		
		System.gc();

		return result;
	}
	
	/** 
     * 拷贝一个文件,srcFile源文件，destFile目标文件 
     *  
     * @param path 
     * @throws IOException 
     */  
    public static boolean copyFileTo(String srcFile, String destFile){  
    	File fin = new File(srcFile);
    	File fout = new File(destFile);
        if (fin.isDirectory() || fout.isDirectory())  
            return false;// 判断是否是文件  
        try{
        	FileInputStream fis = new FileInputStream(fin);  
        	FileOutputStream fos = new FileOutputStream(fout);  
        	int readLen = 0;  
        	byte[] buf = new byte[1024];  
        	while ((readLen = fis.read(buf)) != -1) {  
        		fos.write(buf, 0, readLen);  
        	}  
        	fos.flush();  
        	fos.close();  
        	fis.close();  
        }catch (Exception exp) {
			return false;
		}
        return true;  
    }  
    
    /** 
     * 移动一个文件 
     *  
     * @param srcFile 
     * @param destFile 
     * @return 
     * @throws IOException 
     */  
    public static boolean moveFileTo(String srcFile, String destFile){  
        boolean iscopy = false;
        try {
        	iscopy = copyFileTo(srcFile, destFile);  
		} catch (Exception exp) {
			// TODO: handle exception
		}
        
        if (!iscopy)  
            return false; 
        
        new File(srcFile).delete(); 
        return true;  
    }  
}



package com.wistron.WiViewer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.R.layout;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

public class FileOperation {
	
	public List<File> subAllFileList = new ArrayList<File>();
	public List<File> tmpAllFileList = new ArrayList<File>();
	public static String TEMPFILESAPERATER =TDStaticData.FILTER_TEMP_NAME;
	public static String FILESEPERATER = ".";
	public static String FOLDERSEPERATER =TDStaticData.LOCAL_PATH_SEPARATOR;
	public static String FOLDERSEPERATEROTHER =TDStaticData.SERVER_PATH_SEPARATOR;
	public static String INSTANDSEPERATER = ".!.";
	public static String ROOT_DIR ="";
	public static int TYPE_FOLDER = TDStaticData.FLODER_TYPE ;
	public static int TYPE_FILE = TDStaticData.FILE_TYPE ;
	public List<String> para  ;
	
	public static List<File> chooseFileBySuffix(List<File> oldFileList,String suffix)
	{
		List<File> tempFileList  = new ArrayList<File>();
		for(int i=0;i<oldFileList.size();i++)
		{
			if(new FileOperation().getFileTypeStrInSuffix(oldFileList.get(i)).equals(suffix))tempFileList.add(oldFileList.get(i));	
		}
		return tempFileList;
	}
	
	public List<File> getFileListBySuffix(List<String> suffixList,String filePath)
	{
		File nowFile ;
		if(filePath==null)nowFile = this.file;
		else nowFile = new File(filePath);
		this.subAllFileList = this.getAllSubFileByType(0);
		for(int i=0;i<this.subAllFileList.size();i++)
		{
			boolean isInSelect =  false ;
			int suffixListSize = suffixList.size();
			for(int j=0;j<suffixListSize;j++)
			{
				String s = this.getFileTypeStrInSuffix(subAllFileList.get(i));
				if(this.getFileTypeStrInSuffix(subAllFileList.get(i)).equals(suffixList.get(j)))
				{
					isInSelect = true ;
					break ;
				}
				
			}
			if(!isInSelect)
			{
				this.subAllFileList.remove(i);
				i--;
			}
		}
		this.subAllFileList = sortFileByStatus(this.subAllFileList,0 );
		return this.subAllFileList;
	}
	
	public List<File> sortFileByStatus(List<File> file,int status)
	{
		List<File> tempFileList = file;
		for(int i = tempFileList.size()-1;i>1 ;i--)
		{
			for(int j=0;j<i;j++)
			{
				if(tempFileList.get(j).lastModified()<tempFileList.get(j+1).lastModified())
				{
					File tempFile = tempFileList.get(j);
					tempFileList.set(j, tempFileList.get(j+1));
					tempFileList.set(j+1, tempFile);
				}
			}
		}
		return tempFileList ;
	}

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
	
	private String filePath ;
	private File   file ;

	/**
	 * 获得当前文件的修改时�?
	 * @param timeStr
	 * @return
	 */
	public String getStandTimeString(String timeStr)
	{
   		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
   		String modifyTime   ="0000-00-00:00 00 00" ;
   		
   		
		if(timeStr!=null)modifyTime=timeStr;
		else if(this.file!=null&&this.file.exists())modifyTime = String.valueOf(this.file.lastModified());
		return modifyTime;
	}
	public FileOperation()
	{
		
	}
	public FileOperation(String filePath)
	{
		this.filePath = filePath;
		this.file = new File(filePath);
	}
	
	/**
	 * 给当前文件改�?
	 * @param name
	 */
	public void reName(String name)
	{
		this.file.renameTo(new File(this.file.getPath().replace(this.file.getName(), "")+name));
	}
	
	
	/**
	 * 复制当前文件到新目录
	 * @param path
	 * @param name
	 */
	public void copyTo(String path,String name)
	{
		this.file.renameTo(new File(path,name));
	}
	
	/**
	 * 给临时文件（指定的文件后�?��，去掉临时文件的后缀
	 */
	public void changeTmpNameToRealName()
	{
		if(this.file==null)return ;
		if(this.file.exists())this.file.delete();
		String tmpNamePath = this.file.getPath().replace(this.file.getName(),"")+this.getTmpFileName(null);
		File tmpFile = new File(tmpNamePath);
		if(tmpFile.exists())
		tmpFile.renameTo(this.file);
	}
	
	/**
	 * 删除文件（�?归删除，假如是文件夹会删除所有的文件�?
	 */
	public void delete()
	{
		if(this.file==null)return ;
		else if(this.file.exists())
		{
			this.deleteAllFile(this.file.getPath());
		}
			
		this.deleteTmpFile();
	}
	
	/**
	 * 删除给定的文件的临时文件（临时文件的后缀是被指定的）
	 */
	public void deleteTmpFile()
	{
		if(this.file==null)return ;
		String tmpNamePath = this.file.getPath().replace(this.file.getName(),"")+this.getTmpFileName(null);
		File tmpFile = new File(tmpNamePath);
		if(tmpFile.exists())tmpFile.delete();
	}
	
	/**
	 * 获得文件的临时文件名字（�?��指定后缀名）
	 * @return
	 */
	public String getTmpFileName(String fileName)
	{
		if(fileName!=null)
		{
			int count = fileName.length();
			int index = fileName.lastIndexOf(FILESEPERATER);
			if(index<0)index = count-1;
			return fileName.substring(0, index)+TEMPFILESAPERATER;
		}
		else
		{
			int count = this.file.getName().length();
			int index = this.file.getName().lastIndexOf(FILESEPERATER);
			if(index<0)index = count-1;
			return this.file.getName().substring(0, index)+TEMPFILESAPERATER;
		}
		
		
	}
	/**
	 * 通过文件路径名获得文件名
	 * @param fileFullPath
	 * @return
	 */
	public String getFileName(String fileFullPath)
	{
		int index = fileFullPath.lastIndexOf(FOLDERSEPERATER);
		if(index<0)return fileFullPath;
		else return fileFullPath.substring(index+1);
	}
	public String getFilePath(String fileFullPath)
	{
		int index = fileFullPath.lastIndexOf(FOLDERSEPERATER);
		if(index<0)return "";
		else return fileFullPath.substring(0,index+1);
	}
	
	/**
	 * 读取指定文件，指定偏移文件的，指定大小的文件�?
	 * @param byteSize
	 * @param byteOffset
	 * @return
	 */
	public byte[] readFileStream(long byteSize,long byteOffset) 
	{
		byte[] stream_tmp = new byte[(int) byteSize];
		InputStream fileStream = null;
		try {
			fileStream = new FileInputStream(file);
			fileStream.skip(byteOffset);
			int length = fileStream.read(stream_tmp);
			fileStream.close();
		} 
		catch (FileNotFoundException e) {
			System.out.println("readFileStream:error");
			e.printStackTrace();
		}
		catch (IOException e) {
			try {
				fileStream.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return stream_tmp ;
	}
	
	/**
	 * 写文件流
	 * @param outputStream
	 */
	public void writeFileStream(byte[] outputStream)
	{
		OutputStream  fileStream = null ;
		try {
			fileStream = new FileOutputStream(file,true);
			fileStream.write(outputStream);
			fileStream.close();
			
		} 
		catch (FileNotFoundException e) {
			System.out.println("writeFileStream:error");
			e.printStackTrace();
		}
		catch (IOException e) {
			try {
				fileStream.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * 给临时文件写文件�?
	 * @param outputStream
	 */
	public void writeFileStreamToTmpFile(byte[] outputStream)
	{
		OutputStream  fileStream = null ;
		String tempFilePah = this.file.getPath().replace(this.file.getName(),"")+this.getTmpFileName(null);
		File tempFile = new File(tempFilePah);
		try {
			fileStream = new FileOutputStream(tempFile,true);
			fileStream.write(outputStream);
			fileStream.close();
			
		} 
		catch (FileNotFoundException e) {
			System.out.println("writeFileStream:error");
			e.printStackTrace();
		}
		catch (IOException e) {
			try {
				fileStream.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * 获得文件的修改时�?	 * @param fileT
	 * @return
	 */
	public String getFileLastModify(File fileT)
	{
		long modefyTime ;
		if(fileT==null)modefyTime=this.file.lastModified();
		else modefyTime = fileT.lastModified();
		SimpleDateFormat   formatter   =   new   SimpleDateFormat( "yyyy-MM-dd HH:mm:ss"); 
        String   tsForm   =   formatter.format(new   Date(modefyTime)); 
        return tsForm ;
	}
		
	/**
	 * 获得父文件夹
	 * @param filePath
	 * @return
	 */
	public String getParent(String filePath)
	{
		if(filePath==null)return this.file.getParent();
		else return new File(filePath).getParent();
	}
	/**
	 * 获得文件类型(指定类型列表中的�?
	 * @return
	 */
	public String getFileTypeStrInTypeList(File file)
	{
		
		String type="*/*";;
		String fName= null;
		if(file==null) fName = this.file.getName();
		else fName = file.getName();
		int dotIndex = fName.lastIndexOf(".");
		if(dotIndex < 0){
			return type;
		}
		String end=fName.substring(dotIndex,fName.length()).toLowerCase();
		if(end=="")return type;
		for(int i=0;i<MIME_MapTable.length;i++){
			if(end.equals(MIME_MapTable[i][0]))
				type = MIME_MapTable[i][1];
		}
		return type;
	}
	
	/**
	 * 获得后缀名类�?
	 * @return 
	 */
	public String getFileTypeStrInSuffix(File file)
	{
		
		String type="*/*";;
		String fName= null;
		if(file==null) fName = this.file.getName();
		else fName = file.getName();
		int dotIndex = fName.lastIndexOf(".");
		if(dotIndex < 0){
			return type;
		}
		String end=fName.substring(dotIndex,fName.length()).toLowerCase();
		if(end=="")return type;
		return end;
	}
	
	/**
	 * 获得文件的层�?	 * @param fileDir
	 * @return
	 */
	public int getFileLayer(String fileDir)
	{
		int layer = 0 ;
		
		if(fileDir==null)fileDir = this.filePath;
		fileDir = fileDir.replace(FOLDERSEPERATER, INSTANDSEPERATER);
		fileDir = fileDir.replace(FOLDERSEPERATEROTHER, INSTANDSEPERATER);
		String[] layerSplit = fileDir.split(INSTANDSEPERATER);
		layer = layerSplit.length;
		return layer;
	}
	
	/**
	 * 获得文件列表
	 * @return
	 */
	public List<File> getSubFileList(File file)
	{
		File[] tempFileList =null;
		List<File> subFileList = new ArrayList<File>();
		if(file==null)tempFileList = this.file.listFiles();
		else tempFileList = file.listFiles();
		for(int i =0;i<tempFileList.length;i++)
		{
			if(this.getFileTypeStrInSuffix(tempFileList[i]).equals(TEMPFILESAPERATER))continue;
			subFileList.add(tempFileList[i]);
		}
		return subFileList;
	}
	
	/**
	 * 获得相对路径
	 * @param relativePath 比对路径
	 * @return
	 */
	public String getRelativePath(String relativePath,File fileT)
	{
		String filePath = "";
		if(fileT==null)filePath=this.file.getParent();
		else filePath = fileT.getParent();
		String fileDir = filePath.replace(relativePath, "");
		fileDir = fileDir.replace(FOLDERSEPERATEROTHER, FOLDERSEPERATER);
		fileDir = fileDir+FOLDERSEPERATER;
		return fileDir;
	}
	
	/**
	 * 获得文件�?
	 * @param fileT
	 * @return
	 */
	public String getFilename(File fileT)
	{
		if(fileT==null)return this.file.getName();
		else return fileT.getName();
	}
	
	/**
	 * 获得文件大小
	 * @param fileT
	 * @return
	 */
	public long getFileSize(File fileT)
	{
		if(fileT==null)return this.file.length();
		else return fileT.length();
	}
	

	/**
	 * 获取文件夹下�?��文件
	 * @param fileType
	 * @return
	 */
	public List<File> getAllSubFileByType(int fileType)
	{
		this.clearSubFileList();
		this.getSubFile(this.file);
		if(fileType==TYPE_FILE)
		{
			for(int i=0;i<this.tmpAllFileList.size();i++)
			{
				if(this.tmpAllFileList.get(i).isFile())this.subAllFileList.add(this.tmpAllFileList.get(i));
			}
		}
		else if(fileType==FileOperation.TYPE_FOLDER)
		{
			for(int i=0;i<this.tmpAllFileList.size();i++)
			{
				if(this.tmpAllFileList.get(i).isDirectory())this.subAllFileList.add(this.tmpAllFileList.get(i));
			}
		}
		else this.subAllFileList= this.tmpAllFileList;
		return this.subAllFileList;
	}
	

	
	/**
	 * 获得当前文件的所有下属文�?
	 * @param file
	 */
	public void getSubFile(File file)
	{
		List<File> subFileList = this.getSubFileList(file);
		for(int i=0;i<subFileList.size();i++)
		{
			this.tmpAllFileList.add(subFileList.get(i));
			if(subFileList.get(i).isDirectory())getSubFile(subFileList.get(i));
		}
		
	}
	
	/**
	 * 清除缓存
	 */
	public void clearSubFileList()
	{
		this.subAllFileList.clear();
		this.tmpAllFileList.clear();
	}
	

	
	/**
	 * 创建文件
	 * @param fileType
	 */
	public boolean createFile(int fileType)
	{
		if(fileType==TYPE_FILE)
			try {
				String parentFolder = file.getParent();
				File parentFolderFile = new File(parentFolder);
				if(!parentFolderFile.exists())parentFolderFile.mkdirs();
				this.file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false ;
			}
		else this.file.mkdirs();
		return true ;
		
	}
	/**
	 * 删除�?��文件
	 * @param oldPath
	 */
	public void deleteAllFile(String filePath) {
		File oldPath ;
		if(filePath==null)oldPath= this.file;
		else oldPath = new File(filePath);
		  if (oldPath.isDirectory()) {
		   File[] files = oldPath.listFiles();
		   for (File file : files) {
			   deleteAllFile(file.getPath());
		   }
		   if(!oldPath.getPath().equals(ROOT_DIR))oldPath.delete();
		  }else{
			  if(!oldPath.getPath().equals(ROOT_DIR))oldPath.delete();
		  }
		}
	

	/**
	 * 把List<File>转换成ArrayList<String>，以便Intent发�?
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
	 * 为防止内存溢出做的图片按比例缩小的方�?path
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
			// 对图片进行压缩，是在读取的过程中进行压缩，�?不是把图片读进了内存再进行压�?
			Options newOpts = new Options();
			// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长�?
			// 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能�?过inSampleSize来进行缩放，
			// 其�?表明缩放的�?数，SDK中建议其值是2的指数�?
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
			// 设置大小，这个一般是不准确的，是以inSampleSize的为�?
			newOpts.outHeight = destHeight;
			newOpts.outWidth  = destWidth;
			Bitmap destBm = BitmapFactory.decodeFile(path, newOpts);
			//Bitmap thumbBmp = Bitmap.createScaledBitmap(destBm, MAX_IMAGE_WIDTH, MAX_IMAGE_HEIGHT, true);
			return destBm;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 按字节长度截取字符串
	 * @param src 原字符串
	 * @param desLength 要截取的长度
	 * @param suffix 要在字符串后添加的后�?
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
			int i  = 2; // 要截取的字节数，从第3个字节开�?  
			for (; i < bytes.length && n < length; i++)   
			{   
				// 奇数位置，如3�?�?等，为UCS2编码中两个字节的第二个字�?  
				if (i % 2 == 1)   
				{   
					n++; // 在UCS2第二个字节时n�?   
				}   
				else  
				{   
					// 当UCS2编码的第�?��字节不等�?时，该UCS2字符为汉字，�?��汉字算两个字�?  
					if (bytes[i] != 0)   
					{   
						n++;   
					}   
				}   
			}   
			// 如果i为奇数时，处理成偶数   
			if (i % 2 == 1)  
			{   
				// 该UCS2字符是汉字时，去掉这个截�?��的汉�?  
				if (bytes[i - 1] != 0)   
					i = i - 1;   
				// 该UCS2字符是字母或数字，则保留该字�?  
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
}


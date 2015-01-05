package com.wistron.StreamHelper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.StreamCorruptedException;
import java.io.StringWriter;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.util.ByteArrayBuffer;

import android.R.bool;
import android.R.integer;
import android.media.ExifInterface;
import android.os.BadParcelableException;
import android.os.Debug;
import android.util.Log;

/**
 * Exif信息解析类
 * @author Cocoonshu
 * @date 2012-02-02 13:53:23
 */
public class MPOExifInterface extends MPOFileStreamParser{
	
	public  static boolean DEBUG    =  true;
	
	private static final String TAG = "MPOExifInterface";
	private List<Integer> mPtrs     = null; //Buffer中的定位点列表
	private List<IFDInfo> mTagList  = null; //Exif Tag List
	private JPEGExif      mIFDExif  = null; //图片的Exif信息
	private HashMap<Integer, Object> mINFOHashMap = new HashMap<Integer, Object>();
	
	public MPOExifInterface(String filePath) throws FileNotFoundException, StreamCorruptedException {
		// TODO 获取图片的所有Exif info，以便调用getAttribute时，不需要再次读取
		super(filePath);
		
		byte[] buffer  = null;
		mPtrs = new ArrayList<Integer>();
		mTagList = new ArrayList<IFDInfo>();
		mIFDExif = new JPEGExif();
		
		if(mFin == null){
			Log.e(TAG, "[MPOExifInterface]文件无法打开");
			throw new FileNotFoundException("File cannot be open");
		}
		
		//读取文件大小
		mINFOHashMap.clear();
		try {
			mINFOHashMap.put(MPOHexTAG.TID_IMAGESIZE, mFin.length());
		} catch (IOException exp) {
			exp.printStackTrace();
			Log.e(TAG, "[MPOExifInterface]文件读取错误");
		}
		
		//开始读取APP1块
		buffer = getAPP1Block(mFin);
		if(mPtrs.size() < 1){
			Log.e(TAG, "[MPOExifInterface]找不到APP1标签");
			throw new StreamCorruptedException("Cannot find APP1 DATA TAG");
		}
		
		queryExif(buffer); //开始执行Exif查询过程
		
		if(DEBUG){//////////////////////////////////////////////////////
			if(mINFOHashMap != null){
				Log.w(TAG, "[HASHMAP][" + mINFOHashMap.toString() + "]");
				String GPSLatitude = "",
					   GPSLongitude = "";
				if((String[])mINFOHashMap.get(2) != null)
					for(int a = 0; a < ((String[])mINFOHashMap.get(2)).length; a++){
						GPSLatitude += ((String[])mINFOHashMap.get(2))[a] + " ";
					}
				if((String[])mINFOHashMap.get(4) != null)
					for(int a = 0; a < ((String[])mINFOHashMap.get(4)).length; a++){
						GPSLongitude += ((String[])mINFOHashMap.get(4))[a] + " ";
					}
				Log.w(TAG, "[HASHMAP]GPSLatitude = " + GPSLatitude + " ,GPSLongitude = " + GPSLongitude);
			}
		}//////////////////////////////////////////////////////
		
		//关闭文件流
		try {
			mFin.close();
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(TAG, "[MPOExifInterface]文件流已关闭");
		}
		System.gc();
	}
	
	/**
	 * 在静态表中查询TAG
	 * @return
	 */
	protected int queryTag(int tag) {
		return BaselineUtls.BinarySearch(tag, MPOHexTAG.TID_LINK);
	}
	
	public String getAttributeString(int tag){
		String res = "";
		Object tmp = null;
		
		try{
		if(queryTag(tag) != -1){
			if(mINFOHashMap != null){
				tmp = mINFOHashMap.get(tag);
				if(tmp != null){
					switch (tag) {
					case MPOHexTAG.TID_GPSLAT:
					case MPOHexTAG.TID_GPSLON:{
						//数组方式处理
						String[] str = null;
						str = (String[])tmp;
						for(int i = 0; i < str.length; i++){
							res += str[i] + ",";
						}
						res = res.substring(0, res.length() - 2);
					}
						break;
						
					case MPOHexTAG.TID_FLASH:{
						int key = (Integer) tmp;
						switch (key) {
						case MPOHexTAG.FLASH_AUTO_FIRED:
						case MPOHexTAG.FLASH_AUTO_FIRED_DETECTED:
						case MPOHexTAG.FLASH_AUTO_FIRED_UNDETECTED:
						case MPOHexTAG.FLASH_AUTO_UNFIRED:
							res = "Auto";
							break;
						case MPOHexTAG.FLASH_COMPULSORY_FIRED:
						case MPOHexTAG.FLASH_COMPULSORY_FIRED_DETECTED:
						case MPOHexTAG.FLASH_COMPULSORY_FIRED_UNDETECTED:
						case MPOHexTAG.FLASH_COMPULSORY_UNFIRED:
							res = "Compulsory";
						case MPOHexTAG.FLASH_DETECTED:
						case MPOHexTAG.FLASH_FIRED:
							res = "Fired";
							break;
							
						case MPOHexTAG.FLASH_NO_FUNCTION:
							res = "No flash";
							break;
							
						case MPOHexTAG.FLASH_REDEYE_AUTO:
						case MPOHexTAG.FLASH_REDEYE_AUTO_DETECTED:
						case MPOHexTAG.FLASH_REDEYE_AUTO_UNDETECTED:
						case MPOHexTAG.FLASH_REDEYE_COMPULSORY_FIRED:
						case MPOHexTAG.FLASH_REDEYE_COMPULSORY_FIRED_DETECTED:
						case MPOHexTAG.FLASH_REDEYE_COMPULSORY_FIRED_UNDETECTED:
						case MPOHexTAG.FLASH_REDEYE_FIRED:
						case MPOHexTAG.FLASH_REDEYE_FIRED_DETECTED:
						case MPOHexTAG.FLASH_REDEYE_FIRED_UNDETECTED:
							res = "Red eye";
							break;
							
						case MPOHexTAG.FLASH_UNDETECTED:
						case MPOHexTAG.FLASH_UNFIRED:
							res = "Unfired";
							break;
						}
					}
						break;
						
					case MPOHexTAG.TID_ORIENTATION:{
						int key = (Integer) tmp;
						switch (key) {
						case MPOHexTAG.ORIENTATION_BOTLEFT:
							//res = "180-roll";
							res = "bottom-left";
							break;

						case MPOHexTAG.ORIENTATION_BOTRIGHT:
							//res = "180";
							res = "bottom-right";
							break;
							
						case MPOHexTAG.ORIENTATION_LEFTTOP:
							//res = "270-roll";
							res = "left-top";
							break;
							
						case MPOHexTAG.ORIENTATION_RIGHTBOT:
							//res = "90-roll";
							res = "right-bot";
							break;
							
						case MPOHexTAG.ORIENTATION_RIGHTTOP:
							//res = "270";
							res = "right-top";
							break;
							
						case MPOHexTAG.ORIENTATION_TOPLEFT:
							//res = "0";
							res = "top-left";
							break;
							
						case MPOHexTAG.ORIENTATION_TOPRIGHT:
							//res = "0-roll";
							res = "top-right";
							break;
						}
					}
						break;
						
					case MPOHexTAG.TID_WHITEBALANCE:{
						int key = (Integer) tmp;
						switch (key) {
						case MPOHexTAG.WHITE_BALANCE_AUTO:
							res = "Auto";
							break;

						case MPOHexTAG.WHITE_BALANCE_MANUAL:
							res = "Manual";
							break;
						}
					}
						break;
						
					case MPOHexTAG.TID_GPSALTREF:{
						int key = (Byte) tmp;
						switch (key) {
						case MPOHexTAG.GPS_ABOVE_SEA_LEVEL:
							res = "+";
							break;

						case MPOHexTAG.GPS_BELOW_SEA_LEVEL:
							res = "-";
							break;
						}
					}
						break;
						
					default:
						res = "" + tmp;
						break;
					}
					
				}
			}
		}
		}catch (Exception exp) {
			exp.printStackTrace();
			res = "";
		}
		
		return res;
	}
	
	public int getAttributeInt(int tag, int defaultValue){
		int res = 0;
		Object tmp = null;
		
		try{
			if(queryTag(tag) != -1){
				if(mINFOHashMap != null){
					tmp = mINFOHashMap.get(tag);
					if(tmp != null){
						switch (tag) {
						case MPOHexTAG.TID_GPSLAT:
						case MPOHexTAG.TID_GPSLON:
						case MPOHexTAG.TID_GPSALT:{
							res = defaultValue;
						}
						break;
						
						case MPOHexTAG.TID_ORIENTATION:{
							int key = (Integer) tmp;
							switch (key) {
							case MPOHexTAG.ORIENTATION_BOTLEFT:
								//res = "180-roll";
								res = 180;
								break;

							case MPOHexTAG.ORIENTATION_BOTRIGHT:
								//res = "180";
								res = 180;
								break;
								
							case MPOHexTAG.ORIENTATION_LEFTTOP:
								//res = "270-roll";
								res = 270;
								break;
								
							case MPOHexTAG.ORIENTATION_RIGHTBOT:
								//res = "90-roll";
								res = 90;
								break;
								
							case MPOHexTAG.ORIENTATION_RIGHTTOP:
								//res = "270";
								res = 270;
								break;
								
							case MPOHexTAG.ORIENTATION_TOPLEFT:
								//res = "0";
								res = 0;
								break;
								
							case MPOHexTAG.ORIENTATION_TOPRIGHT:
								//res = "0-roll";
								res = 0;
								break;
							}
						}
							break;

						default:
							try{
								res = (Integer) tmp;
							}catch(ClassCastException exp){
								exp.printStackTrace();
								long castData = (Long)tmp;
								res = (int)castData;
							}
							break;
						}

					}
				}
			}
		}catch (Exception exp) {
			exp.printStackTrace();
			res = defaultValue;
		}
		
		return res;	
	}
	
	public double getAttributeDouble(int tag, double defaultValue){
		double res = 0;
		Object tmp = null;
		
		try{
		if(queryTag(tag) != -1){
			if(mINFOHashMap != null){
				tmp = mINFOHashMap.get(tag);
				if(tmp != null){
					switch (tag) {
					case MPOHexTAG.TID_GPSLAT:
					case MPOHexTAG.TID_GPSLON:{
						String[] gpsStr = null;
						double deg = 0, min = 0, sec = 0;
						
						gpsStr = (String[])tmp;
						if(gpsStr != null && gpsStr.length == 3){
							deg = BaselineUtls.Rational2UDouble(gpsStr[0]);
							min = BaselineUtls.Rational2UDouble(gpsStr[1])/60f;
							sec = BaselineUtls.Rational2UDouble(gpsStr[2])/3600f;
							res = deg + min + sec;
						}
						
						if(deg < 0 || min < 0 || sec < 0){//解析错误时，BaselineUtls.Rational2UDouble返回-1
							res = defaultValue;
						}
					}
						break;
						
					case MPOHexTAG.TID_GPSALT:
						//实数型处理
						res = BaselineUtls.Rational2UDouble((String) tmp);
						break;
						
					default:
						if(tmp instanceof String){
							res = BaselineUtls.Rational2UDouble((String) tmp);
						}else{
							try{
								res = (Long) tmp;
							}catch(ClassCastException exp){
								exp.printStackTrace();
								double castData = (Integer)tmp;
								res = (double)castData;
							}
						}
						break;
					}
					
				}
			}
		}
		}catch (Exception exp) {
			exp.printStackTrace();
			res = defaultValue;
		}
		
		return res;
	}
	
	@Deprecated
	public void setAttribute(int tag, String value){
		
	}
	
	@Deprecated
	public void saveAttributesIntoImage(){
		
	}
	
	/**
	 * 查询Exif信息的过程
	 * @param buffer APP1数据段
	 * @notice 若运算的结果mIFDExif为空，则说明buffer已损坏
	 */
	protected void queryExif(byte[] buffer) {
		int StartPTR             = -1,                   //函数中使用的Buffer位置锚
		    ExifPTR              = -1,                   //Exif信息块索引
		    GPSPTR               = -1;                   //GPS信息块索引
		int BaselineInfoTagCount = 0,                    //基本信息的数量
		    ExifInfoTagCount     = 0,                    //Exif信息的数量
		    GPSInfoTagCount      = 0;                    //GPS信息的数量
		int CodeOrder            = MPOHexTAG.BIG_ENDIAN; //字节序
		
		if(mTagList == null){
			mTagList = new ArrayList<IFDInfo>();
		}
		
		if(mIFDExif == null){
			mIFDExif = new JPEGExif();
		}
		
		//清空数据
		mTagList.clear();
		
		//读取基本信息块
		try{
			//1.判断数据头部是否为FF E1
			if(buffer[0] == MPOHexTAG.TAG_APP1[0] && buffer[1] == MPOHexTAG.TAG_APP1[1])
			{
				//2.判断数据是否为Exif信息
				if(BaselineUtls.Byte2String(new byte[]{buffer[4], buffer[5], buffer[6], buffer[7]}).equals("Exif")){
					//3.读取字节序
					switch (BaselineUtls.Byte2Int(new byte[]{buffer[10], buffer[11], buffer[12], buffer[13]}, MPOHexTAG.BIG_ENDIAN)) {
					case MPOHexTAG.BIG_ENDIAN:
						CodeOrder = MPOHexTAG.BIG_ENDIAN;
						break;

					case MPOHexTAG.LITTLE_ENDIAN:
						CodeOrder = MPOHexTAG.LITTLE_ENDIAN;
						break;

					default:
						mIFDExif = null;
						return;
					}
					
					//读取偏移量，寻找基本信息定义表
					StartPTR = 10 //基址
							+ 2   //BaselineInfoTagCount的空间
							+ BaselineUtls.Byte2Int(new byte[]{buffer[14], buffer[15], buffer[16], buffer[17]}, CodeOrder); //数据偏移量
					BaselineInfoTagCount = BaselineUtls.Byte2Int(new byte[]{buffer[18], buffer[19]}, CodeOrder);
					
					//开始读取基本信息
					for(int i = 0; i < BaselineInfoTagCount; i++){
						byte[] inbuffer = new byte[12];
						for(int j = 0; j < 12; j++){
							inbuffer[j] = buffer[StartPTR + i*12 + j];
						}
						mTagList.add(getIFDInfo(inbuffer, CodeOrder));
					}
					
				}else{
					mIFDExif = null;
					return;
				}
				
			}else{
				mIFDExif = null;
				return;
			}
		}catch (ArrayIndexOutOfBoundsException exp) {
			exp.printStackTrace();
			Log.e(TAG, "[queryExif]读取基本信息时失败，APP1 Buffer DATA BLOCK已损坏");
			//说明buffer不完整，就已读取到的信息来返回
			//继续下面的操作
		}
		
		//解析基本Exif信息（包括Exif信息块和GPS信息块的索引）
		solveDataDetail(buffer, CodeOrder);
		
		//查找Exif信息块和GPS信息块的索引
		if(mINFOHashMap.get(MPOHexTAG.TID_EXIF_IFD_PTR) != null){
			long tmp = (Long) mINFOHashMap.get(MPOHexTAG.TID_EXIF_IFD_PTR); 
			ExifPTR = (int)tmp;
		}
		if(mINFOHashMap.get(MPOHexTAG.TID_GPS_IFD_PTR) != null){
			long tmp = (Long) mINFOHashMap.get(MPOHexTAG.TID_GPS_IFD_PTR);
			GPSPTR = (int)tmp;
		}
		
		//读取Exif信息块
		try{
			if(ExifPTR >= 0){
				ExifPTR += 10; //转换成buffer中的地址
				ExifInfoTagCount = BaselineUtls.Byte2UInt(new byte[]{buffer[ExifPTR], buffer[ExifPTR + 1]}, CodeOrder);
				
				//开始读取Exif信息
				ExifPTR += 2; //重定位到Entry节点头
				for(int i = 0; i < ExifInfoTagCount; i++){
					byte[] inbuffer = new byte[12];
					for(int j = 0; j < 12; j++){
						inbuffer[j] = buffer[ExifPTR + i*12 + j];
					}
					mTagList.add(getIFDInfo(inbuffer, CodeOrder));
				}
			}
		}catch (ArrayIndexOutOfBoundsException exp) {
			exp.printStackTrace();
			Log.e(TAG, "[queryExif]读取Exif信息时失败，APP1 Buffer DATA BLOCK已损坏");
			//说明buffer不完整，就已读取到的信息来返回
			//继续下面的操作
		}
		
		//读取GPS信息块
		try{
			if(GPSPTR >= 0){
				GPSPTR += 10; //转换成buffer中的地址
				GPSInfoTagCount = BaselineUtls.Byte2UInt(new byte[]{buffer[GPSPTR], buffer[GPSPTR + 1]}, CodeOrder);
				
				//开始读取GPS信息
				GPSPTR += 2; //重定位到Entry节点头
				for(int i = 0; i < GPSInfoTagCount; i++){
					byte[] inbuffer = new byte[12];
					for(int j = 0; j < 12; j++){
						inbuffer[j] = buffer[GPSPTR + i*12 + j];
					}
					mTagList.add(getIFDInfo(inbuffer, CodeOrder));
				}
			}
		}catch (ArrayIndexOutOfBoundsException exp) {
			exp.printStackTrace();
			Log.e(TAG, "[queryExif]读取GPS信息时失败，APP1 Buffer DATA BLOCK已损坏");
			//说明buffer不完整，就已读取到的信息来返回
			//继续下面的操作
		}
		
		//解析Exif信息
		solveDataDetail(buffer, CodeOrder);
	}

	/**
	 * 解析数据为可阅读信息
	 * @param buffer
	 * @param codeOrder
	 */
	protected void solveDataDetail(byte[] buffer, int codeOrder) {
		int size = mTagList.size();
		IFDInfo temp = null;
		for(int i = 0; i < size; i++){
			if(mTagList.get(i).mValue == null){
				//检索Value
				temp = mTagList.get(i);
				Object value = null;
				byte[] data  = new byte[temp.mCount * BaselineUtls.sizeof(temp.mType)];
				if(temp.mOffset + data.length + 10 < buffer.length){
					//在buffer中获取
					for(int j = 0; j < data.length; j++){
						data[j] = buffer[temp.mOffset + 10 + j];
					}
					{//解析Value Data
						if(temp.mType == MPOHexTAG.TAG_ASCII){
							temp.mValue = BaselineUtls.getValue(data, temp.mType, codeOrder);
						}else{
							if(temp.mTagName == MPOHexTAG.TID_USERCOMMENTS){//COMMENT字段要除外，这个字段的数据为字符串，类型为UNDEFINED
								temp.mValue = BaselineUtls.getValue(data, MPOHexTAG.TAG_ASCII, codeOrder);
							}
							if(temp.mCount < 2){
								temp.mValue = BaselineUtls.getValue(data, temp.mType, codeOrder);
							}else{
								switch (temp.mType) {
								case MPOHexTAG.TAG_RATIONAL:
									String[] str = new String[temp.mCount];
									for(int m = 0; m < temp.mCount; m++){
										byte[] unitData = new byte[BaselineUtls.sizeof(temp.mType)];
										for(int n = 0; n < unitData.length; n++){
											unitData[n] = data[n + unitData.length * m];
										}
										str[m] = (String) BaselineUtls.getValue(unitData, temp.mType, codeOrder);
										temp.mValue = str;
									}
									break;
								default:
									Object[] dbl = new Object[temp.mCount];
									for(int m = 0; m < temp.mCount; m++){
										byte[] unitData = new byte[BaselineUtls.sizeof(temp.mType)];
										for(int n = 0; n < unitData.length; n++){
											unitData[n] = data[n + unitData.length * m];
										}
										dbl[m] = (Object) BaselineUtls.getValue(unitData, temp.mType, codeOrder);
										temp.mValue = dbl;
									}
									break;
								}
							}
						}
					}
					mTagList.get(i).mValue = temp.mValue;
				}else{
					//在fileStream中获取，直接读取文件流
					try {
						if(mFin == null){
							mFin = new RandomAccessFile(mFileName, "r");
						}
						mFin.seek(temp.mOffset + 12);
						mFin.read(data, 0, data.length);
						{//解析Value Data
							if(temp.mType == MPOHexTAG.TAG_ASCII){
								temp.mValue = BaselineUtls.getValue(data, temp.mType, codeOrder);
							}else{
								if(temp.mCount < 2){
									temp.mValue = BaselineUtls.getValue(data, temp.mType, codeOrder);
								}else{
									if(temp.mCount >= mFin.length()){
										//文件偏移错误，停止解析本TAG
										temp.mValue = null;
										continue;
									}
									switch (temp.mType) {
									case MPOHexTAG.TAG_RATIONAL:
										String[] str = new String[temp.mCount];
										for(int m = 0; m < temp.mCount; m++){
											byte[] unitData = new byte[BaselineUtls.sizeof(temp.mType)];
											for(int n = 0; n < unitData.length; n++){
												unitData[n] = data[n + unitData.length * m];
											}
											str[m] = (String) BaselineUtls.getValue(unitData, temp.mType, codeOrder);
											temp.mValue = str;
										}
										break;
									default:
										Object[] dbl = new Object[temp.mCount];
										for(int m = 0; m < temp.mCount; m++){
											byte[] unitData = new byte[BaselineUtls.sizeof(temp.mType)];
											for(int n = 0; n < unitData.length; n++){
												unitData[n] = data[n + unitData.length * m];
											}
											dbl[m] = (Object) BaselineUtls.getValue(unitData, temp.mType, codeOrder);
											temp.mValue = dbl;
										}
										break;
									}
								}
							}
						}
						mTagList.get(i).mValue = temp.mValue;
					} catch (IOException exp) {
						exp.printStackTrace();
						Log.e(TAG, "[queryExif]解析Exif信息时 " + temp.mTagName + " 失败");
						//继续读取下一条
						break;
					}
				}
			}
				
			mINFOHashMap.put(mTagList.get(i).mTagName, mTagList.get(i).mValue);
		}
	}
	
	protected IFDInfo getIFDInfo(byte[] buffer, int codeOrder) {
		IFDInfo res = new IFDInfo();
		
		if(DEBUG){////////////////////////////////////////////////////////////////////////////////////
		String str = "";
		for(int a = 0; a < buffer.length; a++)
			str += Integer.toHexString(BaselineUtls.Byte2UInt(new byte[]{buffer[a]}, MPOHexTAG.BIG_ENDIAN)) + " ";
		Log.w(TAG, "[getIFDInfo]buffer = " + str );
		}////////////////////////////////////////////////////////////////////////////////////
		
		if(buffer.length < 12){
			return null;
		}
		
		switch (codeOrder) {
		case MPOHexTAG.BIG_ENDIAN:
			res.mTagName = BaselineUtls.getTag(buffer[0], buffer[1]);
			res.mType    = BaselineUtls.getType(buffer[2], buffer[3]);
			res.mCount   = BaselineUtls.getCount(buffer[4], buffer[5], buffer[6], buffer[7]);
			if(BaselineUtls.sizeof(res.mType)*res.mCount > 4){//最后一位存的是偏移量
				res.mOffset  = BaselineUtls.getOffset(buffer[8], buffer[9], buffer[10], buffer[11]);
				res.mValue   = null;
			}else{//最后一位存的是值
				res.mOffset  = -1;
				res.mValue   = BaselineUtls.getValue(new byte[]{buffer[8], buffer[9], buffer[10], buffer[11]}, res.mType);
			}
			
			break;

		case MPOHexTAG.LITTLE_ENDIAN:
			res.mTagName = BaselineUtls.getTag(buffer[1], buffer[0]);
			res.mType    = BaselineUtls.getType(buffer[3], buffer[2]);
			res.mCount   = BaselineUtls.getCount(buffer[7], buffer[6], buffer[5], buffer[4]);
			if(BaselineUtls.sizeof(res.mType)*res.mCount > 4){//最后一位存的是偏移量
				res.mOffset  = BaselineUtls.getOffset(buffer[11], buffer[10], buffer[9], buffer[8]);
				res.mValue   = null;
			}else{//最后一位存的是值
				res.mOffset  = -1;
				res.mValue   = BaselineUtls.getValue(new byte[]{buffer[11], buffer[10], buffer[9], buffer[8]}, res.mType); /*mType为ASCII的时候要小心这里的字节序问题*/
			}
			break;
		}
		
		return res;
	}
		
	protected byte[] getAPP1Block(RandomAccessFile fin) {
		byte[] res = new byte[0];
		int    pos = 0,
			   len = 0;
		
		try{
			if(fin == null){
				return new byte[0];
			}
			
			if(mPtrs == null){
				mPtrs = new ArrayList<Integer>();
			}
			
			boolean E1_OK = false;
			byte[] buffer = new byte[1024];
			int loopCount = (int) (fin.length()/1024f);
			int loopMod   = (int) (fin.length()%1024f);
			for(int i = 0; i < loopCount; i++){
				if(E1_OK){
					break;
				}
				//1024倍数文件块
				fin.read(buffer, 0, 1024);
				for(int j = 0; j < buffer.length - 1; j++){
					//FF E1
					if(buffer[j] == MPOHexTAG.TAG_APP1[0] && buffer[j+1] == MPOHexTAG.TAG_APP1[1]){
						mPtrs.add(i*buffer.length + j);
						pos = i*buffer.length + j;
						E1_OK = true;
						break;
					}
				}
			}
			if((loopMod > 0) && (!E1_OK)){
				//1024余数文件块
				fin.read(buffer, 0, loopMod);
				for(int j = 0; j < 1024 - 1; j++){
					//FF E1
					if(buffer[j] == MPOHexTAG.TAG_APP1[0] && buffer[j+1] == MPOHexTAG.TAG_APP1[1]){
						mPtrs.add(loopCount*buffer.length + j);
						pos = loopCount*buffer.length + j;
						E1_OK = true;
						break;
					}
				}
			}
			
			fin.seek(pos + 2);
			byte[] tmp = new byte[2];
			fin.read(tmp, 0, 2);
			len = BaselineUtls.Byte2UInt(tmp, MPOHexTAG.BIG_ENDIAN);
			res = new byte[len];
			fin.seek(pos);
			fin.read(res, 0, len);
			fin.seek(0);
			
			if(DEBUG){//打印LOG
				String str = "";
				for(int i = 0; i < mPtrs.size(); i++){
					str += " " + mPtrs.get(i).toString();
				}
				Log.w(TAG, "[getMarkerList]PTRs : " + str);
			}
		}catch(IOException exp){
			exp.printStackTrace();
			return new byte[0];
		}
		
		System.gc();
		
		return res;
	}
}

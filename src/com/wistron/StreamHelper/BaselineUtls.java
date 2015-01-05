package com.wistron.StreamHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;

/**
 * 数据类型处理帮助类
 * @author WH1107028 Cocoonshu
 * @date   2012-02-01 11:42:22
 * @commit 使用RandomAccessFile作为文件流读取类
 */
public class BaselineUtls {
	
	/**
	 * 无序Byte数组转Long型
	 * @param src 无序Byte数组，长度0~4
	 * @param endian 字节序
	 * @return
	 */
	static public long Byte2Long(byte[] src, int endian){
		long res  = 0;
		long ones = 0xff;

		switch (endian) {
		case MPOHexTAG.LITTLE_ENDIAN:
			for(int i = 0; i < src.length; i++){
				res = res | (((long)src[i]) << 8*i);
			}
			break;

		case MPOHexTAG.BIG_ENDIAN:
			for(int i = 0; i < src.length; i++){
				res = res | (((long)src[src.length - i - 1]) << (8*i));
			}
			break;
			
		default:
			res = res & ones;
				break;
		}
		
		return res;
	}
	
	/**
	 * 无序Byte数组转Int型
	 * @param src 无序Byte数组，长度0~2
	 * @param endian 字节序
	 * @return
	 */
	static public int Byte2Int(byte[] src, int endian){
		int res  = 0x00;
		int ones = 0xff;
		
		switch (endian) {
		case MPOHexTAG.LITTLE_ENDIAN:
			for(int i = 0; i < src.length; i++){
				res = res | (((int)src[i]) << 8*i);
			}
			break;

		case MPOHexTAG.BIG_ENDIAN:
			for(int i = 0; i < src.length; i++){
				res = res | (((int)src[src.length - i - 1]) << (8*i));
			}
			break;

		default:
			res = res & ones;
			break;
		}

		
		return res;
	}
	
	/**
	 * 无序Byte数组转UInt型
	 * @param src 无序Byte数组，长度0~2
	 * @param endian 字节序
	 * @return
	 */
	static public int Byte2UInt(byte[] src, int endian){
		int tmp  = 0x00;
		int res  = 0x00;
		int ones = 0xff;
		
		switch (endian) {
		case MPOHexTAG.LITTLE_ENDIAN:
			for(int i = 0; i < src.length; i++){
				tmp = (int)src[i];
				if(tmp < 0){
					tmp &= ones;
				}
				res = res | (tmp << 8*i);
			}

			break;

		case MPOHexTAG.BIG_ENDIAN:
			for(int i = 0; i < src.length; i++){
				tmp = (int)src[src.length - i - 1];
				if(tmp < 0){
					tmp &= ones;
				}
				res = res | (tmp << (8*i));
			}
			
			break;

		default:
			res = res & ones;
			break;
		}

		if(res < 0){
			res &= ones;
		}
		
		return res;
	}
	
	/**
	 * 无序Byte数组转Long型
	 * @param src 无序Byte数组，长度0~4
	 * @param endian 字节序
	 * @return
	 */
	static public long Byte2ULong(byte[] src, int endian){
		long tmp  = 0;
		long res  = 0;
		long ones = 0xff;

		switch (endian) {
		case MPOHexTAG.LITTLE_ENDIAN:
			for(int i = 0; i < src.length; i++){
				tmp = src[i];
				if(tmp < 0){
					tmp &= ones;
				}
				res = res | (tmp << 8*i);
			}
			break;

		case MPOHexTAG.BIG_ENDIAN:
			for(int i = 0; i < src.length; i++){
				tmp = src[src.length - i - 1];
				if(tmp < 0){
					tmp &= ones;
				}
				res = res | (tmp << (8*i));
			}
			break;
			
		default:
			res = res & ones;
				break;
		}
		
		if(res < 0){
			res &= ones;
		}
		
		return res;
	}
	
	/**
	 * Byte数组转换为String型
	 * @param buffer
	 * @return
	 */
	static public String Byte2String(byte[] buffer){
		String res = null;
		
		res = new String(buffer);
		
		return res;
	}
	
	/**
	 * 整型转Big Endian Byte数组
	 * @param value 要转换的值
	 * @param length 整型数组长度
	 * @return
	 */
	static public byte[] Int2Byte(int value, int length){
		if(length < 0 || length > Integer.MAX_VALUE){
			length = 0;
		}
		byte[]  res = new byte[length];
		
		for(int i = 0; i < length; i++){
			res[length - i - 1] = (byte)((value >> (8 * i)) & 0xFF);
		}

		return res;
	}
	
	/**
	 * 实数字符串转double型，因为这里的double是无符号的，故返回值小于0表示出错
	 * @param rational
	 * @return
	 */
	static public double Rational2UDouble(String rational){
		String[] tmp = null;
		double   res = -1;
		
		if(rational == null){
			return -1;
		}
		
		tmp = rational.split("/");
		if(tmp.length == 2){
			res = Double.parseDouble(tmp[0])/Double.parseDouble(tmp[1]);
		}else if(tmp.length == 1){
			res = Double.parseDouble(tmp[0]);
		}else{
			res = -1;
		}
		
		return res;
	}
	
	/**
	 * 在Byte数组中查找arg0 arg1组合值，如0xFF 0xD8
	 * @param src
	 * @param arg0
	 * @param arg1
	 * @return 返回第index个组合值在Byte数组中的位置,若返回-1,则没有找到
	 */
	static public List<Integer> getMarkerOffset(byte[] src, int arg0, int arg1){
		List<Integer> pos = new ArrayList<Integer>();

		for(int j = 0; j < src.length - 1; j++){
			if(src[j] == arg0 && src[j+1] == arg1){
				pos.add(j);
			}
		}

		return pos;
	}
	
	/**
	 * 在文件流中查找标志位
	 * @param fin
	 * @param arg0
	 * @param arg1
	 * @return
	 */
	static public List<Integer> getMarkerOffset(RandomAccessFile fin, byte arg0, byte arg1){
		List<Integer> pos = new ArrayList<Integer>();
		
		try{
			if(fin == null){
				return null;
			}
			
			byte[] buffer = new byte[1024];
			int loopCount = (int) (fin.length()/1024f);
			int loopMod   = (int) (fin.length()%1024f);
			for(int i = 0; i < loopCount; i++){
				//1024倍数文件块
				fin.read(buffer, 0, 1024);
				for(int j = 0; j < buffer.length - 1; j++){
					if(buffer[j] == arg0 && buffer[j+1] == arg1){
						pos.add(i*buffer.length + j);
					}
				}
			}
			if(loopMod > 0){
				//1024余数文件块
				fin.read(buffer, 0, loopMod);
				for(int j = 0; j < 1024 - 1; j++){
					if(buffer[j] == arg0 && buffer[j+1] == arg1){
						pos.add(loopCount*buffer.length + j);
					}
				}
			}
			fin.seek(0);
		}catch(IOException exp){
			exp.printStackTrace();
		}
		
		return pos;
	}
	
	/**
	 * 在文件流中查找标志位
	 * @param fin
	 * @param arg0
	 * @param arg1
	 * @param start
	 * @param end
	 * @return
	 */
	static public List<Integer> getMarkerOffset(RandomAccessFile fin, byte arg0, byte arg1, int start, int end){
		List<Integer> pos  = new ArrayList<Integer>(),
				      ans  = new ArrayList<Integer>();
		int           size = 0,
				      curr = 0;
		
		pos  = getMarkerOffset(fin, arg0, arg1);
		size = pos.size();
		for(int i = 0; i < size; i++){
			curr = pos.get(i);
			if(curr >= start && curr <= end){
				ans.add(curr);
			}
		}
		
		return ans;
	}
	

	/**
	 * 在线性表中查询
	 * @param KeyValue
	 * @param list
	 * @return -1为未找到
	 */
	public static int BinarySearch(int KeyValue, int[] list)
	{
		int counter = 1;    // 计数器
		int Max     = list.length;
		int left    = 0,    // 左边界变量
			right   = 0,    // 右边界变量
			middle  = 0;    // 中位数变量

		left = 0;
		right = Max - 1;

		while (left <= right)
		{
			middle = (left + right) / 2;
			if (KeyValue < list[middle]) // 欲查找值较小
				right = middle - 1; // 查找前半段
			// 欲查找值较大
			else if (KeyValue > list[middle])
				left = middle + 1; // 查找后半段
			// 查找到数据
			else if (KeyValue == list[middle])
			{
				return list[middle];
			}
			counter++;
		}
		
		return -1;
	}

	
	/**
	 * 把原始比特流写入文件
	 * @param filePath
	 * @param data
	 */
	public static void writeByteToFile(String filePath, byte[] data){
		File picture = new File(filePath);

		// 如果父目录不存在,就创建目录
		if (!picture.getParentFile().exists()) {
			picture.getParentFile().mkdirs();
			picture = new File(filePath);
		}

		// 获得文件输出流
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(picture);
			if(data != null){
				fos.write(data);
			}
			// 关闭文件流
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 把Bitmap写入图片文件
	 * @param filePath
	 * @param bmp
	 * @param compressFormat
	 * @param quality
	 */
	public static void writeBitemapToFile(String filePath, Bitmap bmp, CompressFormat compressFormat, int quality){
		File picture = new File(filePath);

		// 如果父目录不存在,就创建目录
		if (!picture.getParentFile().exists()) {
			picture.getParentFile().mkdirs();
			picture = new File(filePath);
		}

		// 获得文件输出流
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(picture);
			if(bmp != null){
				bmp.compress(compressFormat, quality, fos);
			}
			// 关闭文件流
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取标签名称，byte数组以BIG_ENDIAN传入
	 * @param b
	 * @param c
	 * @return
	 */
	public static int getTag(byte b, byte c) {
		return Byte2UInt(new byte[]{b, c}, MPOHexTAG.BIG_ENDIAN);
	}

	/**
	 * 获取标签的数据类型，byte数组以BIG_ENDIAN传入
	 * @param b
	 * @param c
	 * @return
	 */
	public static int getType(byte b, byte c) {
		return Byte2Int(new byte[]{b, c}, MPOHexTAG.BIG_ENDIAN);
	}

	/**
	 * 获取标签的数据个数，byte数组以BIG_ENDIAN传入
	 * @param b
	 * @param c
	 * @param d
	 * @param e
	 * @return
	 */
	public static int getCount(byte b, byte c, byte d, byte e) {
		return Byte2Int(new byte[]{b, c, d, e}, MPOHexTAG.BIG_ENDIAN);
	}

	/**
	 * 获取标签的数据偏移量，byte数组以BIG_ENDIAN传入
	 * @param b
	 * @param c
	 * @param d
	 * @param e
	 * @return
	 */
	public static int getOffset(byte b, byte c, byte d, byte e) {
		return Byte2UInt(new byte[]{b, c, d, e}, MPOHexTAG.BIG_ENDIAN);
	}

	/**
	 * 获取标签的值，byte数组以BIG_ENDIAN传入
	 * @param bs
	 * @param mType
	 * @return
	 */
	public static Object getValue(byte[] bs, int type) {
		Object res = null;
		
		switch (type) {
		case MPOHexTAG.TAG_BYTE:
			res = bs[bs.length - 1];
			if(bs[bs.length - 1] < 0){
				res = bs[bs.length - 1] & 0xff;
			}
			break;

		case MPOHexTAG.TAG_ASCII:
			res = new String(bs).trim();
			break;
			
		case MPOHexTAG.TAG_SHORT:
			res = Byte2UInt(new byte[]{bs[2], bs[3]}, MPOHexTAG.BIG_ENDIAN);
			break;
			
		case MPOHexTAG.TAG_LONG:
			res = Byte2ULong(new byte[]{bs[0], bs[1], bs[2], bs[3]}, MPOHexTAG.BIG_ENDIAN);
			break;
			
		case MPOHexTAG.TAG_RATIONAL://会以"12/13"的String返回
			res = "" + Byte2ULong(new byte[]{bs[0], bs[1], bs[2], bs[3]}, MPOHexTAG.BIG_ENDIAN);
			res = (String)res + "/";
			res = (String)res + Byte2ULong(new byte[]{bs[4], bs[5], bs[6], bs[7]}, MPOHexTAG.BIG_ENDIAN);
			break;
			
		case MPOHexTAG.TAG_SBYTE:
			res = bs[bs.length - 1];
			break;
			
		case MPOHexTAG.TAG_UNDEFINED:
			res = bs;
			break;
			
		case MPOHexTAG.TAG_SSHORT:
			res = Byte2Int(new byte[]{bs[2], bs[3]}, MPOHexTAG.BIG_ENDIAN);
			break;
			
		case MPOHexTAG.TAG_SLONG:
			res = Byte2Long(new byte[]{bs[0], bs[1], bs[2], bs[3]}, MPOHexTAG.BIG_ENDIAN);
			break;
			
		case MPOHexTAG.TAG_SRATIONAL:
			res = "" + Byte2Long(new byte[]{bs[0], bs[1], bs[2], bs[3]}, MPOHexTAG.BIG_ENDIAN);
			res = (String)res + "/";
			res = (String)res + Byte2Long(new byte[]{bs[4], bs[5], bs[6], bs[7]}, MPOHexTAG.BIG_ENDIAN);
			break;
			
		case MPOHexTAG.TAG_FLOAT:
		case MPOHexTAG.TAG_DOUBLE:
			res = Byte2Long(bs, MPOHexTAG.BIG_ENDIAN);
			break;
		}
		
		return res;
	}
	
	/**
	 * 获取标签的值，byte数组以BIG_ENDIAN传入
	 * @param bs
	 * @param mType
	 * @return
	 */
	public static Object getValue(byte[] bs, int type, int codeOrder) {
		Object res = null;
		
		switch (type) {
		case MPOHexTAG.TAG_BYTE:
			res = bs[bs.length - 1];
			if(bs[bs.length - 1] < 0){
				res = bs[bs.length - 1] & 0xff;
			}
			break;

		case MPOHexTAG.TAG_ASCII:
			res = new String(bs).trim();
			break;
			
		case MPOHexTAG.TAG_SHORT:
			res = Byte2UInt(new byte[]{bs[0], bs[1]}, codeOrder);
			break;
			
		case MPOHexTAG.TAG_LONG:
			res = Byte2ULong(new byte[]{bs[0], bs[1], bs[2], bs[3]}, codeOrder);
			break;
			
		case MPOHexTAG.TAG_RATIONAL://会以"12/13"的String返回
			res = "" + Byte2ULong(new byte[]{bs[0], bs[1], bs[2], bs[3]}, codeOrder);
			res = (String)res + "/";
			res = (String)res + Byte2ULong(new byte[]{bs[4], bs[5], bs[6], bs[7]}, codeOrder);
			break;
			
		case MPOHexTAG.TAG_SBYTE:
			res = bs[bs.length - 1];
			break;
			
		case MPOHexTAG.TAG_UNDEFINED:
			res = bs;
			break;
			
		case MPOHexTAG.TAG_SSHORT:
			res = Byte2Int(new byte[]{bs[0], bs[1]}, codeOrder);
			break;
			
		case MPOHexTAG.TAG_SLONG:
			res = Byte2Long(new byte[]{bs[0], bs[1], bs[2], bs[3]}, codeOrder);
			break;
			
		case MPOHexTAG.TAG_SRATIONAL:
			res = "" + Byte2Long(new byte[]{bs[0], bs[1], bs[2], bs[3]}, codeOrder);
			res = (String)res + "/";
			res = (String)res + Byte2Long(new byte[]{bs[4], bs[5], bs[6], bs[7]}, codeOrder);
			break;
			
		case MPOHexTAG.TAG_FLOAT:
		case MPOHexTAG.TAG_DOUBLE:
			res = Byte2Long(bs, MPOHexTAG.BIG_ENDIAN);
			break;
		}
		
		return res;
	}
	
	/**
	 * 获取数据类型的大小,單位Byte
	 * @param mType
	 * @return
	 */
	public static int sizeof(int mType) {
		switch (mType) {
		case MPOHexTAG.TAG_BYTE:
			return 1;

		case MPOHexTAG.TAG_ASCII:
			return 1;
			
		case MPOHexTAG.TAG_SHORT:
			return 2;
			
		case MPOHexTAG.TAG_LONG:
			return 4;
			
		case MPOHexTAG.TAG_RATIONAL:
			return 8;
			
		case MPOHexTAG.TAG_SBYTE:
			return 2;
			
		case MPOHexTAG.TAG_UNDEFINED:
			return 1;
			
		case MPOHexTAG.TAG_SSHORT:
			return 4;
			
		case MPOHexTAG.TAG_SLONG:
			return 4;
			
		case MPOHexTAG.TAG_SRATIONAL:
			return 8;
			
		case MPOHexTAG.TAG_FLOAT:
			return 4;
			
		case MPOHexTAG.TAG_DOUBLE:
			return 8;
		}
		
		return 0;
	}
	
	/**
	 * 把字节大小转换成方便阅读的UI大小字符串
	 * 如10256348 B将被转换成9.781215667724609375 MB
	 * @param byteLength 字节大小
	 * @param decimal 保留小数位数
	 * @return
	 */
	public static String getUISizeString(double byteLength, int decimal){
		String res = "";
		String unit = "";
		String[] decPart = null;
		int pointPos = -1;
		
		//换算
		if(byteLength/1024f >= 1f){
			if(byteLength/1024f/1024f >= 1f){
				if(byteLength/1024f/1024f/1024f >= 1f){
					//超过1G
					res = "" + byteLength/1024f/1024f/1024f;
					unit = " GB";
				}else{
					//不足1G
					res = "" + byteLength/1024f/1024f;
					unit = " MB";
				}
			}else{
				//不足1M
				res = "" + byteLength/1024f;
				unit = " KB";
			}
		}else{
			//不足1K
			res = "" + byteLength;
			unit = " B";
		}
		
		//保留小数点
		pointPos = res.indexOf(".");
		if(pointPos >= 0){
			res = res.substring(0, pointPos + decimal + 1);
			//小数为全0，则去掉小数部分
			decPart = res.split(".");
			if(decPart.length > 1 && Double.valueOf(decPart[decPart.length - 1]) == 0 ){
				res = decPart[decPart.length - 2];
			}
		}
		
		
		return res + unit;
	}
}

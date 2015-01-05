package com.wistron.StreamHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;

/**
 * MPO文件解析类
 * @author WH1107028 Cocoonshu
 * @commit 使用RandomAccessFile作为文件流读取类
 * @Notice 为了减少内存的使用，buffer尽量使用全局的
 */
public class MPOFileStreamParser {
	
	private static final boolean DEBUG = false;
	protected Options mOpts             = new Options();
	protected long[]  mAnchorInAPP2     = null; //偏移点列表
	protected int     mImageCount       = 0;    //MPO中包含的图片数量
	protected long[]  mImageSizes       = null;
	protected byte[]  mBuffer           = null;
	protected String  mFileName         = null;
	protected int     mPointer          = 0;    //文件Buffer指针
	protected RandomAccessFile mFin     = null;
	protected RandomAccessFile mFout    = null;
	protected int     m0thImageLen      = 0,    //APP2段中记录0th图片的长度
			          m1thImageLen      = 0,    //APP2段中记录1th图片的长度
			          m0thImageOffset   = -1,   //APP2段中记录0th图片的偏移
			          m1thImageOffset   = -1,   //APP2段中记录1th图片的偏移
	                  m0thAPP2IISOffset = -1,   //APP2段中记录0th Individual Image Size 的位置
	                  m1thAPP2IISOffset = -1,   //APP2段中记录1th Individual Image Size 的位置
	                  mAPP0LengthL      = 0,    //APP0左图段长
	                  mAPP0LengthR      = 0,    //APP0右图段长
	                  mAPP1LengthL      = 0,    //APP1左图段长
	                  mAPP1LengthR      = 0,    //APP1右图段长
	                  mAPP2LengthL      = 0,    //APP2左图段长
	                  mAPP2LengthR      = 0,    //APP2右图段长
	                  mLeftImageLength  = 0,    //左图段长
	                  mRightImageLength = 0;    //右图段长
	
	protected List<Integer> mFFE1_list = null; //APP1
	protected List<Integer> mFFE2_list = null; //APP2
	protected List<Integer> mFFDB_list = null; //DQT
	protected List<Integer> mFFD9_list = null; //EOI
	
	/**
	 * 解码MPO文件
	 * @param filePath
	 * @return
	 */
	static public Bitmap[] decodeFile(String filePath){
		MPOFileStreamParser parser     = null;
		Bitmap[]            bmp        = new Bitmap[2];
		int                 imageCount = 0;
		
		try {
			parser = new MPOFileStreamParser(filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
		imageCount = parser.getImageCount();
		bmp = new Bitmap[imageCount];
		for(int i = 0; i < imageCount; i++){
			bmp[i] = parser.seekIndividualImage(i);
		}
		parser.close();
		
		return bmp;
	}
	
	/**
	 * 解码MPO文件
	 * @param filePath
	 * @return
	 */
	static public Bitmap[] decodeFile(String filePath, Options opt){
		MPOFileStreamParser parser     = null;
		Bitmap[]            bmp        = new Bitmap[2];
		int                 imageCount = 0;
		
		try {
			parser = new MPOFileStreamParser(filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
		try{
			imageCount = parser.getImageCount();
			bmp = new Bitmap[imageCount];
			parser.setOption(opt);
			for(int i = 0; i < imageCount; i++){
				bmp[i] = parser.seekIndividualImage(i);
			}
		}catch (Exception exp) {
			exp.printStackTrace();
			bmp = null;
		}
		parser.close();
		
		System.gc();
		return bmp;
	}
	
	/**
	 * 编码MPO文件
	 * @param filePath
	 * @return
	 */
	static public boolean encodeFile(String filePath, byte[] JPEGImage){
		MPOFileStreamParser parser     = null;
		boolean             isDone     = true;
		byte[]              APP0       = null,
				            APP1       = null,
				            IMGL       = null,
				            IMGR       = null,
				            JPEGImageL = null,
				            JPEGImageR = null;
		byte[][]            srcImage   = new byte[2][];
		
		
		if(JPEGImage != null && JPEGImage.length > 4){
			//1.record APP0 APP1 APP2 block, we call it [imageTIFF]
			//2.split Image
			//3.merge APP0+APP1+APP2+LImage APP0+APP1+APP2+RImage
			//4.call encodeFile(String, byte[], byte[])
			parser = new MPOFileStreamParser();
			parser.mFileName = filePath; // DEBUG!!
			parser.setBuffer(JPEGImage);
			APP0 = parser.pickAPP0(0);
			APP1 = parser.pickAPP1(0);
			srcImage = splitImage(JPEGImage);
			parser.setBuffer(srcImage[0]);
			IMGL = parser.pickCData(0);
			parser.setBuffer(srcImage[1]);
			IMGR = parser.pickCData(1);
			JPEGImageL = new byte[APP0.length + APP1.length + IMGL.length];
			JPEGImageR = new byte[APP0.length + APP1.length + IMGR.length];
			System.arraycopy(MPOHexTAG.TAG_SOI, 0, JPEGImageL, 0, 2);
			System.arraycopy(APP0, 0, JPEGImageL, 2, APP0.length);
			System.arraycopy(APP1, 0, JPEGImageL, APP0.length, APP1.length);
			System.arraycopy(IMGL, 0, JPEGImageL, APP0.length + APP1.length, IMGL.length);
			IMGL = null;
			System.gc();
			System.arraycopy(MPOHexTAG.TAG_SOI, 0, JPEGImageR, 0, 2);
			System.arraycopy(APP0, 0, JPEGImageR, 0, APP0.length);
			System.arraycopy(APP1, 0, JPEGImageR, APP0.length, APP1.length);
			System.arraycopy(IMGR, 0, JPEGImageR, APP0.length + APP1.length, IMGR.length);
			IMGR = null;
			System.gc();
			isDone = encodeFile(filePath, JPEGImageL, JPEGImageR);
			APP0 = null;
			APP1 = null;
			System.gc();
		}else{
			isDone = false;
		}
		
		return isDone;
	}
	
	

	/**
	 * 编码MPO文件
	 * @param filePath
	 * @return
	 */
	static public boolean encodeFile(String filePath, byte[] JPEGImageL, byte[] JPEGImageR){

		MPOFileStreamParser parser     = null;
		boolean             isDone     = true;
		int                 imageCount = 0;
		String              path       = null;
		File                desFile    = null,
				            desDir     = null;

		try{
			if(JPEGImageL != null && JPEGImageR != null && JPEGImageL.length > 4 && JPEGImageR.length > 4){
				//创建MPO文件
				desFile = new File(filePath);
				if(!desFile.getParentFile().exists()){
					desFile.getParentFile().mkdirs();
				}
				if(!desFile.exists()){
					try {
						if(!desFile.createNewFile()){
							return false;
						}
					} catch (IOException e) {
						e.printStackTrace();
						return false;
					}
				}

				//创建流
				parser = new MPOFileStreamParser();
				try {
					parser.create(desFile);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					return false;
				}

				//处理左图并写入
				parser.writeLeftImage(JPEGImageL); 
				JPEGImageL = null;
				System.gc();
				//处理右图并写入
				parser.writeRightImage(JPEGImageR);
				JPEGImageR = null;
				System.gc();
				
				parser.close();
			}else{
				return false;
			}
		}catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			new File(filePath).delete();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			new File(filePath).delete();
			return false;
		}
		
		System.gc();
		
		return isDone;
	}

	/**
	 * 编码MPO文件
	 * @param filePath
	 * @return
	 */
	static public boolean encodeFile(String filePath, byte[] JPEGImage, OnMPOWrittenListener listener){
		boolean result = false;
		result = encodeFile(filePath, JPEGImage);
		if(listener != null){
			listener.OnMPOWrittenCompleted();
		}
			
		return result;
	}

	/**
	 * 编码MPO文件
	 * @param filePath
	 * @return
	 */
	static public boolean encodeFile(String filePath, byte[] JPEGImageL, byte[] JPEGImageR, OnMPOWrittenListener listener){
		boolean result = false;
		result = encodeFile(filePath, JPEGImageL, JPEGImageR);
		if(listener != null){
			listener.OnMPOWrittenCompleted();
		}
			
		return result;
	}






	

	

	/**
	 * 获取MPO文件的指定大小的缩略图
	 * @param filePath
	 * @param width
	 * @param height
	 * @return
	 */
	static public Bitmap[] getThumbnails(String filePath, int width, int height){//×
		
		return null;
	}
	
	/**
	 * 获取MPO文件的预设打下的缩略图
	 * @param filePath
	 * @param thumbSize
	 * @return
	 */
	static public Bitmap[] getThumbnails(String filePath, int thumbSize){//×
		
		switch (thumbSize) {
		case Thumbnails.FULL_SCREEN_KIND:
			
			break;
			
		case Thumbnails.MINI_KIND:
			
			break;
			
		case Thumbnails.MICRO_KIND:
	
			break;

		default:
			break;
		}
		
		return null;
	}
	
	/**
	 * 将图片分解为左右两张
	 * @param src
	 * @return
	 */
	static protected byte[][] splitImage(byte[] src){
		byte[][]         res     = new byte[2][];
		File             tmpMPOL = null,
				         tmpMPOR = null;
		FileOutputStream foutL   = null,
				         foutR   = null;
		RandomAccessFile finL    = null,
						 finR    = null;
		String           str     = null;
		Bitmap           bmp     = null;
		Bitmap[]         desbmp  = new Bitmap[2];
		
		if(src == null || src.length < 4){
			return null;
		}

		try {
			bmp       = BitmapFactory.decodeByteArray(src, 0, src.length);
		
			str       = System.currentTimeMillis() + "";
			tmpMPOL   = File.createTempFile("MPOencoder_" + str + "L", "tmp");
			tmpMPOR   = File.createTempFile("MPOencoder_" + str + "R", "tmp");
			
			foutL     = new FileOutputStream(tmpMPOL);
			foutR     = new FileOutputStream(tmpMPOR);
			
			//获取左图
			desbmp[0] = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth()/2, bmp.getHeight());
			desbmp[0].compress(CompressFormat.JPEG, 80, foutL);

			//获取右图
			desbmp[1] = Bitmap.createBitmap(bmp, bmp.getWidth()/2, 0, bmp.getWidth()/2, bmp.getHeight());
			desbmp[1].compress(CompressFormat.JPEG, 80, foutR);
			
			bmp.recycle();
			desbmp[0].recycle();
			desbmp[1].recycle();
			
			finL      = new RandomAccessFile(tmpMPOL, "r");
			finR      = new RandomAccessFile(tmpMPOR, "r");
			
			res[0]    = new byte[(int) finL.length()];
			res[1]    = new byte[(int) finR.length()];
			
			finL.read(res[0]);
			finR.read(res[1]);
			
			finL.close();
			finR.close();
			
			tmpMPOL.delete();
			tmpMPOR.delete();
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			Log.w("MPOencoder","[splitImage]OutOfMemoryError");
			return null;
		}
		
		return res;
	}
		
	///////////////////////////////////
	//  @Don't call by outer
	//  Private functions
	///////////////////////////////////
	
	protected MPOFileStreamParser() {
		
	}
	
	protected MPOFileStreamParser(String filePath) throws FileNotFoundException{
		mFileName = filePath;
		if(filePath != null){
			if(!open()){
				throw new FileNotFoundException("Cannot open \"" + filePath + "\"");
			}
		}
		
		mFFE1_list = new ArrayList<Integer>();
		mFFE2_list = new ArrayList<Integer>();
		mFFDB_list = new ArrayList<Integer>();
		mFFD9_list = new ArrayList<Integer>();
		
		if(mFin == null){
			open();
		}
		
		getMarkerList(mFin);
	}
	
	private void create(File desFile) throws FileNotFoundException {
		if(desFile == null){
			throw new FileNotFoundException("File is null");
		}
		if(mFout != null){
			mFout = null;
		}
		
		mFileName = desFile.getAbsolutePath();
		mFout = new RandomAccessFile(desFile, "rw"); //要求同步写入时，可设定mFout的mode为"rws"
	}
	
	private void writeLeftImage(byte[] buffer) throws NumberFormatException, IOException {
		if(mFout == null){
			mFout = new RandomAccessFile(mFileName, "rw"); //要求同步写入时，可设定mFout的mode为"rws"
		}
		
		mPointer = 0;
		mBuffer  = buffer;
		
		//写入文件SOI
		mFout.write(new byte[]{(byte)0xFF, (byte)0xD8});
		//写入APP0
		mFout.write(pickAPP0(0));
		//写入APP1
		mFout.write(pickAPP1(0));
		//写入APP2
		mFout.write(pickAPP2(0));
		//写入左图数据
		mFout.write(pickCompData(0));
		//修正偏移量
		fixOffsetInAPP2();
		/*******************************/
		/*******************************/
		/*******************************/
	}
	
	private void writeRightImage(byte[] buffer) throws NumberFormatException, IOException {
		if(mFout == null){
			mFout = new RandomAccessFile(mFileName, "rw"); //要求同步写入时，可设定mFout的mode为"rws"
		}
		
		mPointer = 0;
		mBuffer  = buffer;
		
		//写入文件SOI
		mFout.write(new byte[]{(byte)0xFF, (byte)0xD8});
		//写入APP0
		mFout.write(pickAPP0(1));
		//写入APP1
//		mFout.write(pickAPP1(1)); //可以不写入
		pickAPP1(1); //运行一次，跳过APP1区
		//写入APP2
		mFout.write(pickAPP2(1));
		//写入右图原始数据
		mFout.write(pickCompData(1));
		//修正偏移量
		fixOffsetInAPP2();
		/*******************************/
		/*******************************/
		/*******************************/
	}
	
	private void fixOffsetInAPP2() throws IOException {
		long FDpointer = mFout.getFilePointer();
		//修正APP2数据
		if(m0thAPP2IISOffset >= 0){
			mFout.seek(m0thAPP2IISOffset);
			mFout.write(BaselineUtls.Int2Byte(m0thImageLen, 4));
			mFout.write(BaselineUtls.Int2Byte(0, 4));
		}
		if(m1thAPP2IISOffset >= 0){
			mFout.seek(m1thAPP2IISOffset);
			mFout.write(BaselineUtls.Int2Byte(m1thImageLen, 4));
			mFout.write(BaselineUtls.Int2Byte(2 + mAPP0LengthL + mAPP1LengthL + mAPP2LengthL + m0thImageLen, 4));
		}
		mFout.seek(FDpointer);
	}
	
	private byte[] pickAPP0(int index) throws ArrayIndexOutOfBoundsException{//为了减少内存的使用，buffer不传入，而使用全局的
		byte[] resAPP0 = new byte[0];
		int    pos     = 0,
			   len     = 0;
		
		if(mBuffer == null){
			return resAPP0;
		}
		
		//查找APP0标签
		for(int i = 0; i < mBuffer.length - 1; i++){
			if(mBuffer[i] == MPOHexTAG.TAG_APP0[0] && mBuffer[i+1] == MPOHexTAG.TAG_APP0[1]){
				pos = i;
				len = BaselineUtls.Byte2UInt(new byte[]{mBuffer[i+2], mBuffer[i+3]}, MPOHexTAG.BIG_ENDIAN) + 2; //要加上FF E0 2个字节
				break;
			}
		}
		
		//开始拷贝
		resAPP0 = new byte[len];
		for(int i = 0; i < len; i++){ /**<=================>**/
			resAPP0[i] = mBuffer[i + pos];
		}
		
		switch (index) {
		case 0:
			mAPP0LengthL = len;
			break;

		case 1:
			mAPP0LengthR = len;
			break;
			
		default:
			break;
		}
		
		if(pos == 0 && len == 0){
//			mPointer = pos + len;
		}else{
			mPointer = pos + len - 2;
		}
		System.gc();
		
		if(DEBUG){
			Log.w("MPOEncoder", "[pickAPP0]pos = " + pos + ", len = " + len + ", mPointer = " + mPointer);
			DEBUG_saveData(mFileName + ".APP0", resAPP0);
		}
		return resAPP0;
	}
	
	private byte[] pickAPP1(int index) {//为了减少内存的使用，buffer不传入，而使用全局的
		byte[] resAPP1 = new byte[0];
		int    pos     = 0,
			   len     = 0;
		
		if(mBuffer == null){
			return resAPP1;
		}

		//查找APP1标签
		//for(int i = mPointer; i < mBuffer.length - 1; i++){
		for(int i = 0; i < mBuffer.length - 1; i++){
			if(mBuffer[i] == MPOHexTAG.TAG_APP1[0] && mBuffer[i+1] == MPOHexTAG.TAG_APP1[1]){
				pos = i;
				len = BaselineUtls.Byte2UInt(new byte[]{mBuffer[i+2], mBuffer[i+3]}, MPOHexTAG.BIG_ENDIAN) + 2; //要加上FF E1 2个字节
				break;
			}
		}

		//开始拷贝
		resAPP1 = new byte[len];
		for(int i = 0; i < len; i++){
			resAPP1[i] = mBuffer[i + pos];
		}

		switch (index) {
		case 0:
			mAPP1LengthL = len;
			break;

		case 1:
			mAPP1LengthR = len;
			
		default:
			break;
		}
		
		if(pos == 0 && len == 0){
//			mPointer = pos + len;
		}else{
			mPointer = pos + len - 2; 
		}
		System.gc();
		
		if(DEBUG){
			Log.w("MPOEncoder", "[pickAPP1]pos = " + pos + ", len = " + len);
			DEBUG_saveData(mFileName + ".APP1", resAPP1);
		}
		return resAPP1;
	}
	
	/**
	 * 
	 * @param index 0 = 左图  1 = 右图
	 * @return
	 */
	private byte[] pickAPP2(int index) {
		ByteBuffer res = ByteBuffer.allocate(160); //预定义APP2 Block Length = 0x5A
		
		mAPP2LengthL = res.capacity();
		
		//记录要修改的关键点
		switch (index) {
		case 0:
			//生成APP2头部
			res.put(MPOHexTAG.TAG_APP2);                                                       //APP2标签
			res.put(BaselineUtls.Int2Byte(158, 2));                                            //APP2段长
			res.put(new byte[]{(byte)0x4D, (byte)0x50, (byte)0x46, (byte)0x00});               //MP 标识
			res.put(new byte[]{(byte)0x4D, (byte)0x4D, (byte)0x00, (byte)0x2A});               //字节序：默认为Big Endian
			res.putInt(8);                                                                     //偏移量
			//生成MP Index IFD
			res.put(BaselineUtls.Int2Byte(3, 2));                                              //节点数量
			res.put(MPOHexTAG.TAG_MPFVER);                                                     //MP 格式版本
			res.put(BaselineUtls.Int2Byte(MPOHexTAG.TAG_UNDEFINED, 2));                        //节点的数据类型：未定义
			res.put(BaselineUtls.Int2Byte(4, 4));                                              //节点大小：4
			res.put(new byte[]{(byte)0x30, (byte)0x31, (byte)0x30, (byte)0x30});               //值：默认为“0100”，即v1.0.0
			res.put(MPOHexTAG.TAG_MPIMGCOUNT);                                                 //图片数量
			res.put(BaselineUtls.Int2Byte(MPOHexTAG.TAG_LONG, 2));                             //节点的数据类型：Long
			res.put(BaselineUtls.Int2Byte(1, 4));                                              //节点大小：1
			res.put(BaselineUtls.Int2Byte(2, 4));                                              //值：2
			res.put(MPOHexTAG.TAG_MPENTRY);                                                    //MP 节点偏移
			res.put(BaselineUtls.Int2Byte(MPOHexTAG.TAG_UNDEFINED, 2));                        //节点的数据类型：未定义
			res.put(BaselineUtls.Int2Byte(2, 4));                                              //节点大小：2
			res.put(BaselineUtls.Int2Byte(32, 4));                                             //值：32
			res.putInt(82);                                                                    //下个节点的偏移：0x52 = 82
			res.put(new byte[]{(byte)0x20, (byte)0x02, (byte)0x00, (byte)0x02});               //图片属性:即100000000000100000000000000010  <=========  貌似Big Endian不是单纯的把字序换一下，而是重新按照bit的顺序计算
			res.put(BaselineUtls.Int2Byte(0, 4));                                              //图片大小
			res.put(BaselineUtls.Int2Byte(0, 4));                                              //图片偏移：0，第一张为0
			res.put(BaselineUtls.Int2Byte(0, 2));                                              //附属图片0的编号：没有时为0
			res.put(BaselineUtls.Int2Byte(0, 2));                                              //附属图片1的编号：没有时为0
			res.put(new byte[]{(byte)0x00, (byte)0x02, (byte)0x00, (byte)0x02});               //图片属性:即000000000000100000000000000010  <=========  貌似Big Endian不是单纯的把字序换一下，而是重新按照bit的顺序计算
			res.put(BaselineUtls.Int2Byte(0, 4));                                              //图片大小
			res.put(BaselineUtls.Int2Byte(0, 4));                                              //图片偏移：JPEG Header length + APP0 Length + APP1 Length + APP2 Length
			res.put(BaselineUtls.Int2Byte(0, 2));                                              //附属图片0的编号：没有时为0
			res.put(BaselineUtls.Int2Byte(0, 2));                                              //附属图片1的编号：没有时为0
			//生成MP Attribute IFD
			res.put(BaselineUtls.Int2Byte(4, 2));                                              //节点数量
			res.put(MPOHexTAG.TAG_MPIMGNUM);                                                   //MPIndividualNum
			res.put(BaselineUtls.Int2Byte(MPOHexTAG.TAG_LONG, 2));                             //节点的数据类型：Long
			res.put(BaselineUtls.Int2Byte(1, 4));                                              //节点大小：1
			res.put(BaselineUtls.Int2Byte(1, 4));                                              //值：2
			res.put(MPOHexTAG.TAG_BASEVPNUM);                                                  //BaseViewPointNum
			res.put(BaselineUtls.Int2Byte(MPOHexTAG.TAG_LONG, 2));                             //节点的数据类型：Long
			res.put(BaselineUtls.Int2Byte(1, 4));                                              //节点大小：1
			res.put(BaselineUtls.Int2Byte(1, 4));                                              //值：2
			res.put(MPOHexTAG.TAG_CONANGLE);                                                   //ConvergenceAngle
			res.put(BaselineUtls.Int2Byte(MPOHexTAG.TAG_SRATIONAL, 2));                        //节点的数据类型：SRational
			res.put(BaselineUtls.Int2Byte(1, 4));                                              //节点大小：1
			res.put(BaselineUtls.Int2Byte(138, 4));                                            //偏移：XXXXXXXXXXXXXXXXXXXXXXXXX
			res.put(MPOHexTAG.TAG_BASELINELEN);                                                //BaselineLength
			res.put(BaselineUtls.Int2Byte(MPOHexTAG.TAG_RATIONAL, 2));                         //节点的数据类型：Rational
			res.put(BaselineUtls.Int2Byte(1, 4));                                              //节点大小：1
			res.put(BaselineUtls.Int2Byte(146, 4));                                            //偏移：XXXXXXXXXXXXXXXXXXXXXXXXX
			//生成MP Attribute Value
			res.put(new byte[]{(byte)0x00, (byte)0x00});                                        //pad
			res.put(new byte[]{(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,                  //ConvergenceAngle 值：unknown
					           (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF});
			res.put(new byte[]{(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF});                //BaselineLength 值：unknown
			res.putInt(0);                                                                      //下个节点的偏移：0
			
			
			m0thAPP2IISOffset = 2 + mAPP0LengthL + mAPP1LengthL + 62;
			m1thAPP2IISOffset = 2 + mAPP0LengthL + mAPP1LengthL + 78;
			break;
			
		case 1:
			//生成APP2头部
			res.put(MPOHexTAG.TAG_APP2);                                                       //APP2标签
			res.put(BaselineUtls.Int2Byte(88, 2));                                             //APP2段长
			res.put(new byte[]{(byte)0x4D, (byte)0x50, (byte)0x46, (byte)0x00});               //MP 标识
			res.put(new byte[]{(byte)0x4D, (byte)0x4D, (byte)0x00, (byte)0x2A});               //字节序：默认为Big Endian
			res.putInt(8);                                                                     //偏移量
			//生成MP Index IFD
//			res.put(BaselineUtls.Int2Byte(3, 2));                                              //节点数量
//			res.put(MPOHexTAG.TAG_MPFVER);                                                     //MP 格式版本
//			res.put(BaselineUtls.Int2Byte(MPOHexTAG.TAG_UNDEFINED, 2));                        //节点的数据类型：未定义
//			res.put(BaselineUtls.Int2Byte(4, 2));                                              //节点大小：4
//			res.put(new byte[]{(byte)0x30, (byte)0x31, (byte)0x30, (byte)0x30});               //值：默认为“0100”，即v1.0.0
//			res.put(MPOHexTAG.TAG_MPIMGCOUNT);                                                 //图片数量
//			res.put(BaselineUtls.Int2Byte(MPOHexTAG.TAG_LONG, 2));                             //节点的数据类型：Long
//			res.put(BaselineUtls.Int2Byte(1, 4));                                              //节点大小：1
//			res.put(BaselineUtls.Int2Byte(2, 4));                                              //值：2
//			res.put(MPOHexTAG.TAG_MPENTRY);                                                    //MP 节点偏移
//			res.put(BaselineUtls.Int2Byte(MPOHexTAG.TAG_UNDEFINED, 2));                        //节点的数据类型：未定义
//			res.put(BaselineUtls.Int2Byte(2, 4));                                              //节点大小：2
//			res.put(BaselineUtls.Int2Byte(32, 4));                                             //值：32
//			res.put(BaselineUtls.Int2Byte(0, 4));                                              //下个节点的偏移
//			res.put(new byte[]{(byte)0x20, (byte)0x02, (byte)0x00, (byte)0x02});               //图片属性:即100000000000100000000000000010
//			res.put(BaselineUtls.Int2Byte(0, 4));                                              //图片大小
//			res.put(BaselineUtls.Int2Byte(0, 4));                                              //图片偏移：0，第一张为0
//			res.put(BaselineUtls.Int2Byte(0, 2));                                              //附属图片0的编号：没有时为0
//			res.put(BaselineUtls.Int2Byte(0, 2));                                              //附属图片1的编号：没有时为0
//			res.put(new byte[]{(byte)0x00, (byte)0x02, (byte)0x00, (byte)0x02});               //图片属性:即000000000000100000000000000010
//			res.put(BaselineUtls.Int2Byte(0, 4));                                              //图片大小
//			res.put(BaselineUtls.Int2Byte(0, 4));                                              //图片偏移：JPEG Header length + APP0 Length + APP1 Length + APP2 Length
//			res.put(BaselineUtls.Int2Byte(0, 2));                                              //附属图片0的编号：没有时为0
//			res.put(BaselineUtls.Int2Byte(0, 2));                                              //附属图片1的编号：没有时为0
			//生成MP Attribute IFD
			res.put(BaselineUtls.Int2Byte(4, 2));                                              //节点数量
			res.put(MPOHexTAG.TAG_MPIMGNUM);                                                   //MPIndividualNum
			res.put(BaselineUtls.Int2Byte(MPOHexTAG.TAG_LONG, 2));                             //节点的数据类型：Long
			res.put(BaselineUtls.Int2Byte(1, 4));                                              //节点大小：1
			res.put(BaselineUtls.Int2Byte(2, 4));                                              //值：2
			res.put(MPOHexTAG.TAG_BASEVPNUM);                                                  //BaseViewPointNum
			res.put(BaselineUtls.Int2Byte(MPOHexTAG.TAG_LONG, 2));                             //节点的数据类型：Long
			res.put(BaselineUtls.Int2Byte(1, 4));                                              //节点大小：1
			res.put(BaselineUtls.Int2Byte(1, 4));                                              //值：2
			res.put(MPOHexTAG.TAG_CONANGLE);                                                   //ConvergenceAngle
			res.put(BaselineUtls.Int2Byte(MPOHexTAG.TAG_SRATIONAL, 2));                        //节点的数据类型：SRational
			res.put(BaselineUtls.Int2Byte(1, 4));                                              //节点大小：1
			res.put(BaselineUtls.Int2Byte(68, 4));                                             //偏移：XXXXXXXXXXXXXXXXXXXXXXXXX
			res.put(MPOHexTAG.TAG_BASELINELEN);                                                //BaselineLength
			res.put(BaselineUtls.Int2Byte(MPOHexTAG.TAG_RATIONAL, 2));                         //节点的数据类型：Rational
			res.put(BaselineUtls.Int2Byte(1, 4));                                              //节点大小：1
			res.put(BaselineUtls.Int2Byte(76, 4));                                             //偏移：XXXXXXXXXXXXXXXXXXXXXXXXX
			//生成MP Attribute Value
			res.put(new byte[]{(byte)0x00, (byte)0x00});                                        //pad
			res.put(new byte[]{(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,                  //ConvergenceAngle 值：unknown
					           (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF});
			res.put(new byte[]{(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF});                //BaselineLength 值：unknown
			res.putInt(0);                                                                      //下个节点的偏移：0
			
//			m0thAPP2IISOffset = 2 + mAPP0LengthL + mAPP1LengthL + mAPP2LengthL + m0thImageLen + 2 + mAPP0LengthR + mAPP1LengthR + 62;
//			m1thAPP2IISOffset = 2 + mAPP0LengthL + mAPP1LengthL + mAPP2LengthL + m0thImageLen + 2 + mAPP0LengthR + mAPP1LengthR + 78;
			m0thAPP2IISOffset = -1;
			m1thAPP2IISOffset = -1;
			break;

		default:
			break;
		}
		
		return res.array();
	}
	
	/**
	 * 获取jpeg中的图像数据，encodeFile(byte[] arg0)用
	 * @param index
	 * @return
	 */
	private byte[] pickCData(int index) {
		byte[]  resData  = new byte[0];
		int     pos      = 0,
				len      = 0;
		boolean APP0_OK  = false,
				APP1_OK  = false;
		
		if(mBuffer == null){
			return resData;
		}
		
		//跳过APPn
		for(int i = 0; i < mBuffer.length - 2; i++){
			if((!APP0_OK) && (mBuffer[i] == MPOHexTAG.TAG_APP0[0] && mBuffer[i+1] == MPOHexTAG.TAG_APP0[1])){//APP0
				if(i + 3 <= mBuffer.length){//防越界
					int jump = BaselineUtls.Byte2UInt(new byte[]{mBuffer[i+2], mBuffer[i+3]}, MPOHexTAG.BIG_ENDIAN);
					if(jump >= mBuffer.length ){
						return new byte[0];
					}else{
						i = jump;
						pos = i;
					}
				}
				APP0_OK = true;
			}
			if((!APP1_OK) && (mBuffer[i] == MPOHexTAG.TAG_APP1[0] && mBuffer[i+1] == MPOHexTAG.TAG_APP1[1])){//APP1
				if(i + 3 <= mBuffer.length){//防越界
					int jump = BaselineUtls.Byte2UInt(new byte[]{mBuffer[i+2], mBuffer[i+3]}, MPOHexTAG.BIG_ENDIAN);
					if(jump >= mBuffer.length ){
						return new byte[0];
					}else{
						i = jump;
						pos = i;
					}
				}
				APP1_OK = true;
			}
		}
		
		//查找FF DB标签
		for(int i = pos ; i < mBuffer.length - 1; i++){
			if(mBuffer[i] == MPOHexTAG.TAG_DQT[0] && mBuffer[i+1] == MPOHexTAG.TAG_DQT[1]){
				pos = i;
				len = mBuffer.length - pos;
				break;
			}
		}

		resData = new byte[len];             //
		for(int i = 0; i < len; i++){        //建议在这部分直接操作mFout来写入文件
			resData[i] = mBuffer[i + pos];   //节约内存，提高速度
		}                                    //

		if(len == 0){
			resData = new byte[0];
			System.gc();
		}

		if(DEBUG){
			Log.w("MPOEncoder", "[pickCDATA" + index + "]pos = " + pos + ", len = " + len);
			DEBUG_saveData(mFileName + ".CDATA" + index, resData);
		}
		return resData;
	}
	
	/**
	 * 获取jpeg中的图像数据，encodeFile(byte[] arg0, byte[] arg1)用
	 * @param index
	 * @return
	 * @throws ArrayIndexOutOfBoundsException
	 */
	private byte[] pickCompData(int index) throws ArrayIndexOutOfBoundsException{
		byte[] resData  = new byte[0];
		int    pos      = 0,
			   len      = 0;
		
		if(mBuffer == null){
			return resData;
		}
		
		if(DEBUG){
			for(int i = 0; i < mBuffer.length - 1; i++){
				if(mBuffer[i] == MPOHexTAG.TAG_DQT[0] && mBuffer[i+1] == MPOHexTAG.TAG_DQT[1]){
					Log.w("MPOEncoder", "FF DB = " + i);
					break;
				}
			}
			DEBUG_saveData(mFileName + ".SCR" + index + ".jpg", mBuffer);
		}
		
		
		//查找FF DB标签
		for(int i = (mPointer - 2 < 0 ? 0 : mPointer - 2); i < mBuffer.length - 1; i++){
			if(mBuffer[i] == MPOHexTAG.TAG_DQT[0] && mBuffer[i+1] == MPOHexTAG.TAG_DQT[1]){
				pos = i;
				len = mBuffer.length - pos;
				break;
			}
		}
		
		resData = new byte[len];             //
		for(int i = 0; i < len; i++){        //建议在这部分直接操作mFout来写入文件
			resData[i] = mBuffer[i + pos];   //节约内存，提高速度
		}                                    //
		
		if(len == 0){
			resData = new byte[0];
			System.gc();
		}
		
		switch (index) {
		case 0:
			m0thImageLen = len;
			break;

		case 1:
			m1thImageLen = len;
			break;
			
		default:
			break;
		}
		
		if(DEBUG){
			Log.w("MPOEncoder", "[pickCOMDATA" + index + "]pos = " + pos + ", len = " + len);
			DEBUG_saveData(mFileName + ".COMDATA" + index, resData);
		}
		return resData;
	}
	
	private void appendBytes(byte[] content) throws IOException {
		if(mFout == null){
			mFout = new RandomAccessFile(mFileName, "rw"); //要求同步写入时，可设定mFout的mode为"rws"
		}
		
		mFout.seek(mFout.length());
		mFout.write(content);
		mFout.seek(mFout.length());
	}
	
	private void setBuffer(byte[] buffer) {
		mBuffer = null;
		System.gc();
		mBuffer = buffer;
		mPointer = 0;
		System.gc();
	}
	
	private byte[] getBuffer() {
		return mBuffer;
	}
	/**
	 * 搜索图片
	 * @param imageIndex 第几幅图
	 * @return 
	 */
	private Bitmap seekIndividualImage(int imageIndex){
		Bitmap ans = null;
		int    start = 0,
			   end   = 0;
		
		if(mFin == null){
			return null;
		}
		
		if(imageIndex >= mImageCount){
			return null;
		}
		
		//开始搜索
		for(int i = imageIndex; i < mFFDB_list.size(); i++){
			if(mFFDB_list.get(i) >= mFFE2_list.get(imageIndex)){
				start = mFFDB_list.get(i);
				break;
			}
		}
		for(int i = imageIndex; i < mFFD9_list.size(); i++){
			if(mFFD9_list.get(i) >= mFFE2_list.get(imageIndex) && mFFD9_list.get(i) >= start){
				end = mFFD9_list.get(i);
				break;
			}
		}
		
		//开始读取
		if(mFFDB_list.size() >= imageIndex && mFFD9_list.size() >= imageIndex){
			byte[] buffer = new byte[end - start + 4];
			try {
				mFin.seek(start);
				mFin.read(buffer, 2, end - start);
				mFin.seek(0);
				buffer[0] = MPOHexTAG.TAG_SOI[0];
				buffer[1] = MPOHexTAG.TAG_SOI[1];
				buffer[buffer.length - 2] = MPOHexTAG.TAG_EOI[0];
				buffer[buffer.length - 1] = MPOHexTAG.TAG_EOI[1];
				ans = BitmapFactory.decodeByteArray(buffer, 0, buffer.length, mOpts);
				buffer = null;
				System.gc();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NullPointerException e){
				e.printStackTrace();
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			}
		}
		
		return ans;
	}
	
	protected void setOption(Options opt) {
		mOpts = opt;
	}
	
	protected int getImageCount(){
		return mImageCount;
	}
	
	protected void getMarkerList(RandomAccessFile fin){
			
		try{
			if(fin == null){
				return;
			}
			
			byte[] buffer = new byte[1024];
			int loopCount = (int) (fin.length()/1024f);
			int loopMod   = (int) (fin.length()%1024f);
			for(int i = 0; i < loopCount; i++){
				//1024倍数文件块
				fin.read(buffer, 0, 1024);
				for(int j = 0; j < buffer.length - 1; j++){
					//APP1
					if(buffer[j] == MPOHexTAG.TAG_APP1[0] && buffer[j+1] == MPOHexTAG.TAG_APP1[1]){
						mFFE1_list.add(i*buffer.length + j);
					}
					//APP2
					if(buffer[j] == MPOHexTAG.TAG_APP2[0] && buffer[j+1] == MPOHexTAG.TAG_APP2[1]){
						mFFE2_list.add(i*buffer.length + j);
					}
					//DQT
					if(buffer[j] == MPOHexTAG.TAG_DQT[0] && buffer[j+1] == MPOHexTAG.TAG_DQT[1]){
						mFFDB_list.add(i*buffer.length + j);
					}
					//EOI
					if(buffer[j] == MPOHexTAG.TAG_EOI[0] && buffer[j+1] == MPOHexTAG.TAG_EOI[1]){
						mFFD9_list.add(i*buffer.length + j);
					}
				}
			}
			if(loopMod > 0){
				//1024余数文件块
				fin.read(buffer, 0, loopMod);
				for(int j = 0; j < 1024 - 1; j++){
					//APP1
					if(buffer[j] == MPOHexTAG.TAG_APP1[0] && buffer[j+1] == MPOHexTAG.TAG_APP1[1]){
						mFFE1_list.add(loopCount*buffer.length + j);
					}
					//APP2
					if(buffer[j] == MPOHexTAG.TAG_APP2[0] && buffer[j+1] == MPOHexTAG.TAG_APP2[1]){
						mFFE2_list.add(loopCount*buffer.length + j);
					}
					//DQT
					if(buffer[j] == MPOHexTAG.TAG_DQT[0] && buffer[j+1] == MPOHexTAG.TAG_DQT[1]){
						mFFDB_list.add(loopCount*buffer.length + j);
					}
					//EOI
					if(buffer[j] == MPOHexTAG.TAG_EOI[0] && buffer[j+1] == MPOHexTAG.TAG_EOI[1]){
						mFFD9_list.add(loopCount*buffer.length + j);
					}
				}
			}
			fin.seek(0);
		}catch(IOException exp){
			exp.printStackTrace();
		}
	}
	
	private boolean open(){
		boolean         result  = true;
		ArrayList<Long> anchors = new ArrayList<Long>();
		byte[]          buffer  = new byte[1024];
		
		if(mFin != null){
			try {
				mFin.close();
			} catch (IOException e) {
				e.printStackTrace();
				mFin = null;
			}
		}
		
		try {
			/**
			 * 读取0xFFE2的个数，就能获取图片数量
			 */
			mFin = new RandomAccessFile(mFileName, "r");
			//获取基本信息
			mImageCount = 0;
			int loopCount = (int) (mFin.length()/1024f);
			int loopMod   = (int) (mFin.length()%1024f);
			for(int i = 0; i < loopCount; i++){
				//1024倍数文件块
				mFin.read(buffer, 0, 1024);
				for(int j = 0; j < 1024 - 1; j++){
					if(buffer[j] == MPOHexTAG.TAG_APP2[0] && buffer[j+1] == MPOHexTAG.TAG_APP2[1]){
						mImageCount++;
						anchors.add((long)(i*1024+j));
					}
				}
			}
			if(loopMod > 0){
				//1024余数文件块
				mFin.read(buffer, 0, loopMod);
				for(int j = 0; j < 1024 - 1; j++){
					if(buffer[j] == MPOHexTAG.TAG_APP2[0] && buffer[j+1] == MPOHexTAG.TAG_APP2[1]){
						mImageCount++;
						anchors.add((long)(loopCount*1024+j));
					}
				}
			}
			mFin.seek(0);
			
			//记录偏移点
			mAnchorInAPP2 = new long[mImageCount];
			mImageSizes   = new long[mImageCount];
			for(int i = 0; i < mImageCount; i++){
				mAnchorInAPP2[i] = anchors.get(i);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return result;
	}
	
	private boolean close(){
		if(mFin == null){

		}else{
			try {
				mFin.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(mFout == null){

		}else{
			try {
				mFout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		System.gc();
		
		return true;
	}
	
	public interface OnMPOWrittenListener{
		public void OnMPOWrittenCompleted();
	}
	
	/////////////////////////////////////////////
	private void DEBUG_saveData(String filePath, byte [] sdata) {
		//创建MPO文件
		File File = new File(filePath);
		if(File.getParentFile() == null || !File.getParentFile().exists()){
			File.getParentFile().mkdirs();
		}
		if(!File.exists()){
			try {
				if(!File.createNewFile()){
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			FileOutputStream fout = new FileOutputStream(File);
			fout.write(sdata);
			fout.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

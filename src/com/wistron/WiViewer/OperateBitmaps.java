package com.wistron.WiViewer;






import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StreamCorruptedException;

import com.tridef.converter.DDD;
import com.wistron.StreamHelper.MPOExifInterface;
import com.wistron.StreamHelper.MPOFileStreamParser;
import com.wistron.StreamHelper.MPOHexTAG;

import Utilities.CSStaticData;
import Utilities.FileTypeHelper;
import android.R.integer;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.MediaColumns;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;

public class OperateBitmaps
{
	static int screenwidth=TDStaticData.SCREEN_WIDTH;
	static  int scrennheight=TDStaticData.SCREEN_HEIGHT;
	static int screenwidth_org=TDStaticData.SCREEN_WIDTH_ORG;
	static  int scrennheight_org=TDStaticData.SCREEN_HEIGHT_ORG;
static String TAG="OperateBitmaps";
String m_error="";	
Context context;
static int mOritationType=0;//0代表正常根据图片tag显示，1为不读图片tag
static int mOrientation;
private int mSceneDepth=255;
private boolean isConvert2DTo3D=true;
DDD m3DConverter=null;
public void setConvertAuto(boolean isconvert)
{
	isConvert2DTo3D=isconvert;
}
public void setSceneDepth(int SceneDepth)
{
	mSceneDepth=SceneDepth;
	m3DConverter.setSceneDepth(mSceneDepth);
}
public void setOrientationType(int type )
{
	mOritationType=type;
}
//private int mOrientation=0;
public OperateBitmaps( Context context)
{
	this.context= context;
	m3DConverter=new DDD();
	m3DConverter.setSceneDepth(mSceneDepth);
}
public void setOrientation(int degree)
{
	mOrientation=degree;
}
/**
 * 解码图片，得到适当大小的图片数组。
 *【0】为原图（2D为原图，3D为左图），【1】为左右图压缩一半后的合成图
 * @param filepath
 * @return
 */
	public  Bitmap[] decodeBitmap(String filepath)
	{
		try
		{
			if(CSStaticData.DEBUG)
			Log.e(TAG, "decodeBitmap---------------1");
			boolean isVideo = isVideo(filepath);
			boolean is3DSource = is3DSource(filepath);
			if (isVideo)
			{
				// 解码视频得到缩略图,因为视频缩图一般比较小，所以不用调整大小（调整大小主要是为了防止内存溢出）
				Bitmap srcBitmap = ThumbnailUtils.createVideoThumbnail(filepath,
						Thumbnails.FULL_SCREEN_KIND);
				return  genFinalBitmaps(seperateBitmap(srcBitmap,is3DSource,0), is3DSource,false);
			} else
			{
				//先解码（用到option可以压缩图片），根据是否为3D及图片尺寸来决定压缩比率。
					return decodeImage(filepath,is3DSource);
			}
		} catch (OutOfMemoryError e)
		{
			// TODO: handle exception
			if(CSStaticData.DEBUG)
			Log.e(TAG, "decodeBitmap------[OutOfMemoryError]");
			return null;
		}
		
	}
	public void updateOrientation(String filepath,int changeangle)
	{
		int rotate= ImageInfo.getImageOrientationForRotate(filepath);
		int orientation=(rotate+changeangle+720)%360;
		 if(orientation==0)
		  orientation=360;
		 try
			{
			  ExifInterface	m_exif=new ExifInterface(filepath);
			   int aString=  ImageInfo.genNewOrientation(orientation);
			   m_exif.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(aString));
			   m_exif.saveAttributes();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
	}
	public  int lookUpOrientation(String filepath )
	{
		int a=ImageInfo.getImageOrientationForRotate(filepath);
		if(CSStaticData.DEBUG)
		{
			Log.e(TAG, "[lookUpOrientation]="+a);
		}
		return a;
	}
	public Bitmap  Rotate2DBitmap(Bitmap bitmap, int orientation )
	{
		
		if(bitmap!=null)
		{
			int width=bitmap.getWidth();
			int height=bitmap.getHeight();
			if(width*height==0)
				return null;
			if(!(orientation==90||orientation==180||orientation==270))
			{
				 return bitmap;
			}
			else
			{
				Matrix matrix=new Matrix();
				matrix.postRotate(orientation,width/2, height/2);
				Bitmap  mBitmap=Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
				 if(mBitmap!=bitmap)
				  recycleSBitmap(bitmap);
				 return mBitmap;
			}
		}
		return null;
		
		
	}
	
/**
 * 将输入图片居中分割为左右图和旋转，并将输入图片回收。如果图片为2D，则直接返回，数组第二元素为第一元素
 * @param bitmap
 * @return
 */
public static Bitmap[] seperateBitmap(Bitmap bitmap,boolean is3D,int orientation)
{
	if(CSStaticData.DEBUG)
      Log.e(TAG, "seperateBitmap---------------3");
	if(bitmap==null||bitmap.getWidth()==0||bitmap.getHeight()==0)
	return null;
	   int width=bitmap.getWidth();
		int height=bitmap.getHeight();
   Bitmap[] mBitmaps=new Bitmap[2];
   if(mOritationType==0)
   {
	   //根据图片tag信息修改旋转角
	   if(is3D)
	   {
				Matrix matrix=new Matrix();
				if(orientation==90||orientation==180||orientation==270)
				matrix.postRotate(orientation,width/4, height/2);
				mBitmaps[0]=Bitmap.createBitmap(bitmap, 0, 0, width/2, height, matrix, false);
				mBitmaps[1]=Bitmap.createBitmap(bitmap, width/2, 0, width/2, height, matrix, false);
				recycleSBitmap(bitmap);
	   }
	   else
	   {
			if(!(orientation==90||orientation==180||orientation==270))
			{
				 mBitmaps[0]=bitmap;
			}
			else
			{
				Matrix matrix=new Matrix();
				matrix.postRotate(orientation,width/2, height/2);
				mBitmaps[0]=Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
				 if(mBitmaps[0]!=bitmap)
				  recycleSBitmap(bitmap);
			}
			 mBitmaps[1]=null;
	   }
	   
   }
   else if(mOritationType==1)
   {
	   int tempangle=0;
	   if(orientation!=0&&orientation!=-1)
	   {
		   //查询不出错
		   tempangle=  getDegreeToShow(orientation);
	   }
	   tempangle=0;
	    if(is3D)
	   {
				Matrix matrix=new Matrix();
				if(tempangle!=0&&tempangle!=360)
				matrix.postRotate(tempangle,width/4, height/2);
				mBitmaps[0]=Bitmap.createBitmap(bitmap, 0, 0, width/2, height, matrix, false);
				mBitmaps[1]=Bitmap.createBitmap(bitmap, width/2, 0, width/2, height, matrix, false);
				recycleSBitmap(bitmap);
	   }
	   else
	   {
		   if(CSStaticData.DEBUG)
		   Log.e(TAG, " 进入非tag模式，获取的temangle为："+tempangle);
			if(tempangle==0||tempangle==360)
			{
				 mBitmaps[0]=bitmap;
			}
			else
			{
				Matrix matrix=new Matrix();
				matrix.postRotate(tempangle,width/2, height/2);
				mBitmaps[0]=Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
				 if(mBitmaps[0]!=bitmap)
				  recycleSBitmap(bitmap);
			}
			 mBitmaps[1]=null;
	   }
    }
   return mBitmaps;
	}
public static int getDegreeToShow(int orientation)
{
 return	orientation-mOrientation;
//	if(mOrientation==360)
//	{
//		return mOrientation;
//	}
//	else if(mOrientation==180)
//	{
//		return mOrientation-180;
//		
//	}
//	else if (mOrientation==90) {
//		return mOrientation-90;
//		
//	}
//	else  {
//		return mOrientation-270;
//	}
	
	}
	/**
	 * 需要将图片调整为适应屏幕大小,计算option的压缩比
	 * 
	 * @param width
	 *            图片的长( 真实长度，3D模式下左图长度)
	 * @param height
	 *            图片宽
	 * @param is3D
	 *            是否为3D
	 * @return 压缩的比例
	 */
	public static int getDecodeRate(float width, float height, boolean is3D,int orientation)
	{
		float rateScreen =0;
		float rateImage =0;
		int rate=0;
		if(orientation==90||orientation==270)
		{
			rateImage = height / width;
		}
		else
		{
			rateImage = width / height;
		}
			
		boolean flag;
		if (is3D)
		{
			rateScreen= ((float) screenwidth_org) / ((float) scrennheight_org);
			if((rateImage / 2f) - rateScreen > 0)
			{
				flag=true;
			}
			else
			{
			   flag=false;	
			}
			if (width > 2 * screenwidth || height > scrennheight)
			{
				if (flag)
				{
					rate= (int) (width / 2) / screenwidth;
				} else
				{
					rate= (int) height / scrennheight;
				}
			} else
			{
				rate= 1;
			}
		} else
		{
			rateScreen= ((float) screenwidth) / ((float) scrennheight);
			if(rateImage - rateScreen > 0)
			{
				flag=true;
			}
			else
			{
				flag=false;
			}
			if (width > screenwidth || height > scrennheight)
			{
				if (flag)
				{
					rate= (int) (width) / screenwidth;
				} else
				{
					rate= (int) height / scrennheight;
				}
			} else
			{
				rate= 1;
			}
		}
		if(CSStaticData.DEBUG)
		Log.e(TAG, "压缩比例为"+rate);
        return rate;

	}

/**
 * 判断是否为3D资源，无论是3D影片还是图片
 * @param filepath
 * @return
 */
	public static boolean is3DSource(String filepath)
	{
		return FileTypeHelper.isStereoImageFile(filepath)||FileTypeHelper.isStereoImageFile(filepath);
	}
	/**
	 * 判断是否为影片
	 * @param filepath
	 * @return
	 */
	public static boolean isVideo(String filepath)
	{
         return  FileTypeHelper.isVideoFile(filepath);
	}
	/**
	 * 判断是否为mpo文件
	 * @param filepath
	 * @return
	 */
	public static boolean isMpo(String filepath)
	{
		String mString=filepath.toLowerCase();
		return mString.endsWith("mpo");
	}
//	public void setOrientation(int orientation)
//	{
//		mOrientation=orientation;
//		Log.e(TAG, "当前operateBitmap中的mOrientation="+mOrientation);
//	}
/**
 * 解码图片，定义解压比例，返回图片数组，生成图片数组 【0】为原图（2D为原图，3D为左图），【1】为左右图压缩一半后的合成图
 * @param filepath
 * @return
 */
	public  Bitmap[] decodeImage(String filepath,boolean is3DSource)
	{
		if(CSStaticData.DEBUG)
		Log.e(TAG, "decodeImage---------------2");
		boolean ismpo=isMpo(filepath);
		if(ismpo)
		{
			Bitmap[] res =null;
			try
			{
				Bitmap[] mpoBitmaps =decodeMpoBitmap(filepath, screenwidth, scrennheight);
				res= genFinalBitmaps(mpoBitmaps,true,false);
			} catch (OutOfMemoryError e)
			{
				if(CSStaticData.DEBUG)
				Log.e(TAG, "解码mpo文件失败，内存不足");
			}
			return res;
		}
		else
		{
			//为jps和jpg等
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			options.inPurgeable = true;
			options.inInputShareable = true;
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(filepath, options);
			int srcWidth = options.outWidth;
			int srcHeight = options.outHeight;
			if (srcWidth * srcHeight != 0)
			{
				int orientation=lookUpOrientation(filepath);
		        int rate=getDecodeRate(srcWidth, srcHeight, is3DSource,orientation);
		        options.inJustDecodeBounds=false;
		        options.inSampleSize=rate;
		        Bitmap mBitmap=BitmapFactory.decodeFile(filepath, options);
		        if(isConvert2DTo3D&&!filepath.toLowerCase().endsWith("jps"))
				{
		        	Bitmap rotatebitmap=Rotate2DBitmap(mBitmap,orientation);
		        	if(rotatebitmap!=null)
		        	{
		        		int width=rotatebitmap.getWidth();
		        		int height=rotatebitmap.getHeight();
		        		if(width!=0&&height!=0)
		        		{
		        			float r=((float)height)/((float)width);
		        			
		        			//如果比例超过2.4则不适用2D转3D
		        			if(r>2.4)
		        			{
		        				return genFinalBitmaps(seperateBitmap(rotatebitmap, is3DSource,0), is3DSource,false);
		        			}
		        			else
		        			{
		        				Bitmap temp=fitScreenSize(rotatebitmap, TDStaticData.SCREEN_WIDTH_ORG, TDStaticData.SCREEN_HEIGHT_ORG);
		        				Bitmap tempBitmaps= m3DConverter.genrenteTriDefBitmap(temp, false);
		     				    Bitmap[] resBitmaps=new Bitmap[2];
		     				    resBitmaps[0]=temp;
		     				    resBitmaps[1]=tempBitmaps;
		     				    return resBitmaps;
		        			}
		        		}
		        	}
				   
				}
		        else
		        {
		        	 return genFinalBitmaps(seperateBitmap(mBitmap, is3DSource,orientation), is3DSource,false);
				}
		    }
		}
		return null;
	}
	
	/**
	 * 用于解码图片，并旋转（orientation）。
	 * @return
	 */


	/**
	 * 回收bitmap数组
	 */
	public static void recycleDBitmap(Bitmap[] bitmaps)
	{
		if (bitmaps != null)
		{
			for (int i = 0; i < bitmaps.length; i++)
			{
				if(bitmaps[i]!=null)
				{
					if (!bitmaps[i].isRecycled())
						bitmaps[i].recycle();
				}
			
			}
			bitmaps=null;
		}

	}

	public static void recycleSBitmap(Bitmap bitmap)
	{
		if (bitmap != null)
		{
			if (!bitmap.isRecycled())
				bitmap.recycle();
			bitmap=null;	
		}
	
	}
	
	
	
	
	
	
	//通过给定的屏幕大小生成一张完整的图片，包括mpo（拼成一张），并且读取了旋转信息
	
	
	public Bitmap decodeMpotoSingle(String filepath,int Swidth,int Sheight)
	{
		Bitmap[] mBitmaps =decodeMpoBitmap(filepath, Swidth/2, Sheight);
		Bitmap res=null;
		if(mBitmaps!=null&&mBitmaps[0]!=null)
		{
			int singlewidth=mBitmaps[0].getWidth();
			int singleheight=mBitmaps[0].getHeight();
			Bitmap mbitmap = Bitmap.createBitmap(singlewidth * 2,
					singleheight, Config.ARGB_8888);
			Canvas mCanvas = new Canvas(mbitmap);
			mCanvas.drawBitmap(mBitmaps[0], 0, 0, null);
			if(mBitmaps[1]!=null)
			mCanvas.drawBitmap(mBitmaps[1], singlewidth, 0, null);
			res = mbitmap;
		}
		return fitScreenSize(res,Swidth,Sheight);
	}
	
	/**
	 * 生成jpg和jps适应屏幕的图片
	 */
public Bitmap decodeNomalBitmaptoSingle(String filepath,int Swidth,int Sheight)
{
	Bitmap mRetrunBitmap=null;
	BitmapFactory.Options options = new BitmapFactory.Options();
	options.inPreferredConfig = Bitmap.Config.ARGB_8888;
	options.inPurgeable = true;
	options.inInputShareable = true;
	options.inJustDecodeBounds = true;
	BitmapFactory.decodeFile(filepath, options);
	int srcWidth = options.outWidth;
	int srcHeight = options.outHeight;
	if (srcWidth * srcHeight != 0)
	{
		int orientation=lookUpOrientation(filepath);
		
        int sampleSize = Math.max(srcWidth / Swidth, srcHeight / Sheight);
        sampleSize = Math.min(sampleSize,
                Math.max(srcWidth / Sheight,srcHeight / Swidth));
        options.inSampleSize = Math.max(sampleSize, 2);
        options.inJustDecodeBounds=false;
        Bitmap mBitmap=BitmapFactory.decodeFile(filepath, options);
        
       
        int width=mBitmap.getWidth();
        int height=mBitmap.getHeight();
        //特殊处理,处理编码方式
        if(mBitmap.getConfig()!=Config.ARGB_8888)
        {
        	  Bitmap temp= Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
              Canvas tempcanvas=new Canvas(temp);
              tempcanvas.drawBitmap(mBitmap, 0, 0, null);
             if(mBitmap!=temp)
             {
            	 mBitmap.recycle();
            	 mBitmap=temp;
             }
             else
             {
            	 temp.recycle();
             }
        }
      
        
        
    	if(!(orientation==90||orientation==180||orientation==270))
		{
    		mRetrunBitmap=mBitmap;
		}
		else
		{
			Matrix matrix=new Matrix();
			matrix.postRotate(orientation,width/2, height/2);
			mRetrunBitmap=Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false);
			 if(mRetrunBitmap!=mBitmap)
			  recycleSBitmap(mBitmap);
		}
    }
	return fitScreenSize(mRetrunBitmap,Swidth,Sheight);
}
public static Bitmap fitScreenSize(Bitmap bitmap,int swidth,int sheight)
{
	Bitmap res=null;
	if(bitmap!=null)
	{
		int width=bitmap.getWidth();
		int height=bitmap.getHeight();
		if(width*height!=0)
		{
            if(width<=swidth&&height<=sheight)
            {
            	return bitmap;
            }
            else
            {
            	//按比例压缩大小
            	float rate_src=((float)width)/((float)height);
            	float rate_screen=((float)swidth)/((float)sheight);
            	Bitmap temp=null;
            	if(rate_src<=rate_screen)
            	{
            	  temp=Bitmap.createScaledBitmap(bitmap, (int) (sheight*rate_src), sheight, false);
            	}
            	else
            	{
            	   temp=Bitmap.createScaledBitmap(bitmap,swidth, (int) (swidth/rate_src), false);
            	}
            	if(temp!=bitmap)
            	{
            		bitmap.recycle();
            		res=temp;
            	}
            	
            }
		}
	}
	
	return res;
}

/**
 * 压缩mpo到屏幕大小,并调整大小，然后旋转,生成正常比例的左右图的图片数组
 * @param filepath
 * @param Swidth
 * @param Sheight
 * @return
 */
	public Bitmap[] decodeMpoBitmap(String filepath,int Swidth,int Sheight)
	{
		if(CSStaticData.DEBUG)
		Log.e(TAG, "decodeMpoBitmap");
		Bitmap [] mReturnBitmaps=new Bitmap[2];
		int orientation=lookUpOrientation( filepath);
		int decodeRate=1;
		Options mOptions=new Options();
		mOptions.inPreferredConfig=Config.RGB_565;
		
		try
		{
			ExifInterface exf=new ExifInterface(filepath);
			int width=Integer.valueOf(exf.getAttribute(ExifInterface.TAG_IMAGE_WIDTH));
			int height=Integer.valueOf(exf.getAttribute(ExifInterface.TAG_IMAGE_LENGTH));
			if(width>0&&height>0)
			{
				decodeRate= getDecodeRate(width*2, height,true , orientation);			
			}
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mOptions.inSampleSize=decodeRate;
		if(CSStaticData.DEBUG)
		   Log.e(TAG, "[decodeMpoBitmap]-------------------------------------------->inSampleSize="+decodeRate);
		Bitmap[] mBitmaps=MPOFileStreamParser.decodeFile(filepath,mOptions);
		if(mBitmaps==null)
			return null;
		//调整大小
		for (int i = 0; i < mBitmaps.length; i++)
		{
			if(i<2)
			{
				//mReturnBitmaps[i]=mBitmaps[i];
				mReturnBitmaps[i]=reSizeBitmap(mBitmaps[i], Swidth, Sheight);
			}
			else
			{
				if(mBitmaps[i]!=null)
				mBitmaps[i].recycle();
			}
		}
		
		//调整左右图大小一致,这里偷工减料了，不保证为原比例
		if(mReturnBitmaps[0]!=null&&mReturnBitmaps[1]!=null)
		{
			int width=mReturnBitmaps[0].getWidth();
		    int height=mReturnBitmaps[0].getHeight();
			if(width!=mReturnBitmaps[1].getWidth()||height!=mReturnBitmaps[1].getHeight())
			{
				//ruguo
				if(width*height!=0)
				{
					Bitmap tmpBitmap=Bitmap.createScaledBitmap(mReturnBitmaps[1], width, height, false);
				    if(tmpBitmap!=mReturnBitmaps[1])
				    	mReturnBitmaps[1].recycle();
				    mReturnBitmaps[1]=tmpBitmap;
				}
				
			}
		}
		//orientation旋转
		//同普通图片方式一致，测试
	
		if(orientation==90||orientation==180||orientation==270)
		{
			Bitmap[] temp=new Bitmap[2];
			Matrix matrix=new Matrix();
			if(mReturnBitmaps[0]!=null)
			{
				if(mReturnBitmaps[0].getWidth()*mReturnBitmaps[0].getHeight()!=0)
				{
					matrix.setRotate(orientation,mReturnBitmaps[0].getWidth()/2, mReturnBitmaps[0].getHeight()/2);
					temp[0]=Bitmap.createBitmap(mReturnBitmaps[0], 0, 0, mReturnBitmaps[0].getWidth(), mReturnBitmaps[0].getHeight(), matrix, false);
				}
			
			}
             if(mReturnBitmaps[1]!=null)
             {
            	 
            	 if(mReturnBitmaps[1].getWidth()*mReturnBitmaps[1].getHeight()!=0)
 				{
            		matrix.setRotate(orientation,mReturnBitmaps[1].getWidth()/2, mReturnBitmaps[1].getHeight()/2);
 					temp[1]=Bitmap.createBitmap(mReturnBitmaps[1], 0, 0, mReturnBitmaps[1].getWidth(), mReturnBitmaps[1].getHeight(), matrix, false);
 				}
             }
 			 recycleDBitmap(mReturnBitmaps);
             mReturnBitmaps=temp;
		}
		//返回图片
		return mReturnBitmaps;
		
	}
	/**
	 * 将两张图片合成一张图片，如果为3D，则左右压缩后合成一张，如果为2D，则将图片压缩一半后画两遍
	 * @param bitmaps
	 * @param is3d
	 * @param isrecycleleft
	 * @return
	 */
	public Bitmap conbineImages2OneInhalf(Bitmap[] bitmaps,boolean is3d,boolean isrecycleleft)
	{
		if(CSStaticData.DEBUG)
		Log.e(TAG, "conbineImages2OneInhalf---------------5");
		if(bitmaps!=null&&bitmaps[0]!=null)
		{
			int width=bitmaps[0].getWidth();
			int height=bitmaps[0].getHeight();
			if(width*height!=0)
			{
				Bitmap res=Bitmap.createBitmap(width, height, Config.RGB_565);
				Canvas mCanvas=new Canvas(res);
				Rect src=new Rect(0, 0, width, height);
				Rect dst1=new Rect(0, 0, width/2, height);
				Rect dst2=new Rect(width/2, 0, width, height);
				if(is3d)
				{
					mCanvas.drawBitmap(bitmaps[0], src, dst1, null);
				   if(bitmaps[1]!=null)
				   {
				      mCanvas.drawBitmap(bitmaps[1], src, dst2, null);
				      bitmaps[1].recycle();
				    }
				}
				else
				{
					mCanvas.drawBitmap(bitmaps[0], src, dst1, null);
					mCanvas.drawBitmap(bitmaps[0], src, dst2, null);
				}
				if(isrecycleleft)
				{
					bitmaps[0].recycle();
				}
				return res;
			}
			else
			{
			  return null;	
			}
			
		}
		else {
			return null;
		}
		
		
	}
	/**
	 * 生成图片数组 【0】为原图（2D为原图，3D为左图），【1】为左右图压缩一半后的合成图
	 * @param bitmaps
	 * @param is3D
	 * @param isrecycleleft
	 * @return
	 */
	public Bitmap[] genFinalBitmaps(Bitmap[] bitmaps,boolean is3D,boolean isrecycleleft)
	{
		if(CSStaticData.DEBUG)
		Log.e(TAG, "genFinalBitmaps---------------4");
		if(bitmaps==null||bitmaps[0]==null)
		{
			return null;
		}
        Bitmap[] res=new Bitmap[2]; 
        res[0]=bitmaps[0];
        res[1]=conbineImages2OneInhalf(bitmaps, is3D,isrecycleleft);
		return res;
	}
	
	
	public Bitmap reSizeBitmap(Bitmap bitmap ,int width,int height )
	{
		Bitmap mBitmap=null;
		if(bitmap!=null)
		{
			int srcwidth=bitmap.getWidth();
			int srcheight=bitmap.getHeight();
			if(srcwidth*srcheight!=0)
			{
				if(srcwidth<=width&&srcheight<=height)
				{
					mBitmap=bitmap;
				}
				else
				{
					float rate_src=((float)srcwidth)/((float)srcheight);
					float rate_screen=((float)width)/((float)height);
					if(rate_src>rate_screen)
					{
						mBitmap=Bitmap.createScaledBitmap(bitmap, width, (int) (width/rate_src), false);
					}
					else
					{
						mBitmap=Bitmap.createScaledBitmap(bitmap, (int) (height*rate_src), height, false);
					}
					if(bitmap!=mBitmap)
						bitmap.recycle();
				}
			}
		}
		
		return mBitmap;
		
	}
	
}

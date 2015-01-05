package com.wistron.WiCamera;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import Utilities.CSStaticData;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.location.Location;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.Images.Thumbnails;
import android.provider.MediaStore.MediaColumns;
import android.provider.MediaStore.Video;
import android.provider.MediaStore.Video.VideoColumns;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * @Copyright (c) 2011 Wistron SWPC All rights reserved.
 * 
 * @created: 2012/02/21
 * @filename: OperationFile.java
 * @author WH1107063(周海江)
 * @purpose 对文件处理的类
 * 
 * 
 * 
 * 
 */
public class OperationFile {

	private int BroadcastRetryCounter = 0;

	/**
	 * 得到图片的缩略图
	 * 
	 * @param path
	 *            图片的路径
	 * @param maxLength
	 *            图片的最大的长或宽
	 * @param max_image_width
	 *            图片的最大宽度
	 * @param max_iamge_height
	 *            图片的最大高度
	 * @return 返回图片的bitmap
	 */
	public static Bitmap fitSizeImg(String path, int maxLength,
			int max_image_width, int max_iamge_height) {
		Bitmap thumbBmp = null;
		if (path == null || path.length() < 1)
			return null;
		try {
			// File file = new File(path);
			Options opts = new Options();
			opts.inJustDecodeBounds = true; // 当为true时，不为图片分配内存，只获取图片的大小，并保存在opts的outWidth和outHeight
			// BitmapFactory.decodeFile(file.getPath(), opts);
			BitmapFactory.decodeFile(path, opts);
			int srcWidth = opts.outWidth;
			int srcHeight = opts.outHeight;
			int destWidth = 0;
			int destHeight = 0;
			double ratio = 0.0;
			// 对图片进行压缩，是在读取的过程中进行压缩，而不是把图片读进了内存再进行压缩
			Options newOpts = new Options();
			// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
			// 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，
			// 其值表明缩放的倍数，SDK中建议其值是2的指数值
			if ((srcWidth - srcHeight) > 10) {
				ratio = srcWidth / maxLength;
				destHeight = maxLength;
				destWidth = (int) (srcWidth / ratio);
				newOpts.inSampleSize = (int) ratio + 1;
			} else if (srcHeight - srcWidth > 10) {
				ratio = srcHeight / maxLength;
				destWidth = maxLength;
				destHeight = (int) (srcHeight / ratio);
				newOpts.inSampleSize = (int) ratio + 1;
			} else {
				ratio = srcHeight / maxLength;
				destHeight = maxLength;
				destWidth = maxLength;
				newOpts.inSampleSize = (int) ratio + 1;
			}
			// inJustDecodeBounds设为false表示把图片读进内存中
			Log.v("ratio", " " + newOpts.inSampleSize);
			newOpts.inJustDecodeBounds = false;
			// 设置大小，这个一般是不准确的，是以inSampleSize的为准
			newOpts.outHeight = destHeight;
			newOpts.outWidth = destWidth;
			Bitmap destBm = BitmapFactory.decodeFile(path, newOpts);
			thumbBmp = Bitmap.createScaledBitmap(destBm, max_image_width,
					max_iamge_height, true);
			return thumbBmp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
		}
	}

	public static Bitmap fitSizeImga(String path, int maxLength,
			int max_image_width, int max_iamge_height) {
		Bitmap b = BitmapFactory.decodeFile(path);
		Bitmap bit = Bitmap.createScaledBitmap(b, max_image_width,
				max_iamge_height, false);
		return b;

	}

	public static String readSDCard() {
		double picSize = 2;
		int storageModeId = StoredData.getInt(StoredData.M_STORAGEMODE, 0);
		int picId = StoredData.getInt(StoredData.M_PICTURESIZE, 2);
		switch (picId) {
		case 0:
			picSize = 5;
			break;
		case 1:
			picSize = 3;
			break;
		case 2:
			picSize = 2;
			break;
		case 3:
			picSize = 1;
			break;
		case 4:
			picSize = 0.5;
			break;

		default:
			break;
		}
		if (storageModeId == 0) {
			return ((readSystemSpace() / picSize) + "").split("\\.")[0];
		} else if (storageModeId == 1) {
			return ((readSDCardSpace() / picSize) + "").split("\\.")[0];
		}
		WiCameraActivity.m_btn_camera_newpic_sizeofsum.postInvalidate();
		return "";
	}

	/**
	 * 读取sdcard剩余空间大小
	 * 
	 * @return
	 */
	public static long readSDCardSpace() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File sdcardDir = Environment.getExternalStorageDirectory();
			StatFs sf = new StatFs(sdcardDir.getPath());
			long blockSize = sf.getBlockSize();
			long blockCount = sf.getBlockCount();
			long availCount = sf.getAvailableBlocks();
			Log.d("", "block大小:" + blockSize + ",block数目:" + blockCount
					+ ",总大小:" + blockSize * blockCount / 1024 + "KB");
			Log.d("", "可用的block数目：:" + availCount + ",剩余空间:" + availCount
					* blockSize / 1024 + "KB");
			System.out.println(sdcardDir.getAbsolutePath());
			// readSystem();
			return ((availCount * blockSize) / (1024 * 1024));
		}
		return 0;
	}

	// 读取手机内存剩余空间大小
	public static long readSystemSpace() {
		File root = Environment.getRootDirectory();
		StatFs sf = new StatFs(root.getPath());
		long blockSize = sf.getBlockSize();
		long blockCount = sf.getBlockCount();
		long availCount = sf.getAvailableBlocks();
		System.out.println(root.getAbsolutePath());
		Log.d("", "block大小:" + blockSize + ",block数目:" + blockCount + ",总大小:"
				+ blockSize * blockCount / 1024 + "KB");
		Log.d("", "可用的block数目：:" + availCount + ",剩余空间:" + availCount
				* blockSize / 1024 + "KB");
		return ((availCount * blockSize) / (1024 * 1024));
	}

	// 删除文件
	public synchronized static boolean deleteFile(Context context, String path) {
		File file = new File(path);
		boolean issucc = false;
		if (file.exists()) {
			issucc = file.delete();
			if (issucc) {
				String where = MediaColumns.DATA + "=?";
				String[] selectionArgs = new String[] { path };
				int columnsNum = context.getContentResolver()
						.delete(Images.Media.EXTERNAL_CONTENT_URI, where,
								selectionArgs);
				if (columnsNum > 0) {
					return issucc;
				} else {
					return false;
				}
			} else {
				return false;
			}

		} else {
			return false;
		}

	}

	/**
	 * 得到gps信息并写入指定照片
	 * 
	 * @param location
	 *            gps信息
	 * @param filePath
	 *            照片路径
	 * @throws IOException
	 */
	public synchronized static void addImageGps(Location location,
			String filePath) {
		// System.out.println("m_isornotaddimagegps=" + m_isornotaddimagegps
		// + "location=" + location);
		if (location != null) {
			ExifInterface exif = null;
			try {
				exif = new ExifInterface(filePath);
				exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE,
						GetDegree(location.getLatitude()));
				exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE,
						GetDegree(location.getLongitude()));
				if (exif != null) {
					exif.saveAttributes();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} else {
			// Toast.makeText(m_context, "GPS is not open", 1000);
		}
	}

	/**
	 * 把iso信息写入图片
	 * 
	 * @param filePath
	 *            图片路径
	 * @param isoValue
	 *            iso的值
	 */
	public static void addImageISO(String filePath, String isoValue) {
		// System.out.println("m_isornotaddimagegps=" + m_isornotaddimagegps
		// + "location=" + location);
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(filePath);
			exif.setAttribute(ExifInterface.TAG_ISO, isoValue);
			if (exif != null) {
				exif.saveAttributes();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		ExifInterface exifs = null;
		try {
			exifs = new ExifInterface(
					"/mnt/sdcard/DCIM/Camera/IMG_20120502_172649.jpg");
			String iso = exifs.getAttribute("ISOSpeedRatings");
			System.out.println("保存的iso为：" + iso);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * 把图片文件保存到数据库
	 */
	public static Uri intTodb(Context context, String type, String title,
			int orientation, String path, int length) {
		// Insert into MediaStore.
		ContentValues values = new ContentValues(7);
		values.put(ImageColumns.TITLE, title);
		values.put(ImageColumns.DISPLAY_NAME, title + ".jpg");
		values.put(ImageColumns.DATE_TAKEN, System.currentTimeMillis());
		if (orientation == 360) {
			orientation = 0;
		}
		values.put(ImageColumns.MIME_TYPE, type);
		values.put(ImageColumns.ORIENTATION, orientation);
		values.put(ImageColumns.DATA, path);
		values.put(ImageColumns.SIZE, length);
		// values.put(ImageColumns.WIDTH, width);
		// values.put(ImageColumns.HEIGHT, height);

		// if (location != null) {
		// values.put(ImageColumns.LATITUDE, location.getLatitude());
		// values.put(ImageColumns.LONGITUDE, location.getLongitude());
		// }

		Uri uri = null;
		int stromode = StoredData.getInt(StoredData.M_STORAGEMODE, 0);
		// if (stromode == 0) {
		// uri = context.getContentResolver().insert(
		// Images.Media.INTERNAL_CONTENT_URI, values);
		// } else {
		uri = context.getContentResolver().insert(
				Images.Media.EXTERNAL_CONTENT_URI, values);
		// }

		context.sendBroadcast(new Intent("android.hardware.action.NEW_PICTURE",
				uri));
		if (uri == null) {

			return null;
		}
		System.out.println("是否执行加入到db了。。。。。。。。。。。。。。。。。。。。。。。。。。。。");
		return uri;
	}

	// 把视频文件保存到数据库
	public static Uri intTodbv(Context context, String type, String title,
			int orientation, String path, Long length) {
		// Insert into MediaStore.
		ContentValues values = new ContentValues(9);
		values.put(VideoColumns.TITLE, title);
		values.put(VideoColumns.DISPLAY_NAME, title + ".mp4");
		values.put(VideoColumns.DATE_TAKEN, new Date().toLocaleString());
		values.put(VideoColumns.MIME_TYPE, type);
		// values.put(VideoColumns., orientation);
		values.put(VideoColumns.DATA, path);
		values.put(VideoColumns.SIZE, length);
		// values.put(ImageColumns.WIDTH, width);
		// values.put(ImageColumns.HEIGHT, height);

		// if (location != null) {
		// values.put(ImageColumns.LATITUDE, location.getLatitude());
		// values.put(ImageColumns.LONGITUDE, location.getLongitude());
		// }

		Uri uri = null;
		int stromode = StoredData.getInt(StoredData.M_STORAGEMODE, 0);
		// if (stromode == 0) {
		// uri = context.getContentResolver().insert(
		// Video.Media.INTERNAL_CONTENT_URI, values);
		// } else {
		uri = context.getContentResolver().insert(
				Video.Media.EXTERNAL_CONTENT_URI, values);
		// }
		context.sendBroadcast(new Intent("android.hardware.action.NEW_VIDEO",
				uri));
		if (uri == null) {

			return null;
		}
		return uri;
	}

	/**
	 * 根据经纬度得到度分秒
	 * 
	 * @param latitudeOrlongitude
	 *            要传进去得经纬度数
	 * @return 返回度分秒数
	 */
	public static String GetDegree(double latitudeOrlongitude) {
		String[] duarr = Double.toString(latitudeOrlongitude).split("\\.");
		String degrees = duarr[0];
		String[] fenarr = Double.toString(
				(Double.parseDouble("0." + duarr[1]) * 60)).split("\\.");
		String minutes = fenarr[0];
		String seconds = Double.toString(
				((Double.parseDouble("0." + fenarr[1]) * 60) * 10000)).split(
				"\\.")[0];
		String dustr = degrees + "/1," + minutes + "/1," + seconds + "/10000";
		// System.out.println(dustr);
		return dustr;
	}

	/**
	 * 判断有无sdcard
	 * 
	 * @return 如果有则返回true，否则返回false
	 */
	public static boolean isExtSdcardExists() {
		if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment
				.getExternalStorageState())) {
			Log.e("有无sdcard", "有sdcard");
			return true;
		} else {
			Log.e("有无sdcard", "无sdcard");
			return false;
		}
	}

	/**
	 * 内置sdcard 是否存在
	 * 
	 * @return
	 */
	public static boolean isIntSdcardExists() {
		File f = new File(CSStaticData.TMP_INT_DIR);
		if (f.exists()) {
			return true;
		} else {
			boolean isSucc = f.mkdirs();
			if (isSucc) {
				return true;
			} else {
				return false;
			}
		}
	}

	/*
	 * 显示toast
	 */
	public static void showToast(Context context, String text, int duration) {
		Toast.makeText(context, text, duration).show();
	}

	/*
	 * 得到视频文件的缩略图
	 */
	public static Bitmap getVideotThumbnail(String path, int width, int height) {
		Bitmap srcBitmap = ThumbnailUtils.createVideoThumbnail(path,
				Thumbnails.FULL_SCREEN_KIND);
		Bitmap targetedbitmap = ThumbnailUtils.extractThumbnail(srcBitmap,
				width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return targetedbitmap;

	}
}

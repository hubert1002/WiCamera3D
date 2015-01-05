/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wistron.WiCamera.WiPanorama;

import Utilities.CSStaticData;
import android.R.integer;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Camera;
import android.location.Location;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.MediaColumns;
import android.provider.MediaStore.Video;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.Video.VideoColumns;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import com.wistron.WiCamera.StoredData;
import com.wistron.WiCamera.VideoSurfaceView;


public class Storage {
    private static final String TAG = "CameraStorage";

    public static final String DCIM =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();
    
    public static final String DIRECTORY = DCIM + "/Camera";

    // Match the code in MediaProvider.computeBucketValues().
    public static final String BUCKET_ID =
            String.valueOf(DIRECTORY.toLowerCase().hashCode());

    public static final long UNAVAILABLE = -1L;
    public static final long PREPARING = -2L;
    public static final long UNKNOWN_SIZE = -3L;
    public static final long LOW_STORAGE_THRESHOLD= 50000000;
    public static final long PICTURE_SIZE = 1500000;

    private static final int BUFSIZE = 4096;
  
 /*   private void addVideoToMediaStore() {
        if (mVideoFileDescriptor == null) {
            Uri videoTable = Uri.parse("content://media/external/video/media");
            mCurrentVideoValues.put(Video.Media.SIZE,
                    new File(mCurrentVideoFilename).length());
            long duration = SystemClock.uptimeMillis() - mRecordingStartTime;
            if (duration > 0) {
                if (mCaptureTimeLapse) {
                    duration = getTimeLapseVideoLength(duration);
                }
                mCurrentVideoValues.put(Video.Media.DURATION, duration);
            } else {
                Log.w(TAG, "Video duration <= 0 : " + duration);
            }
            try {
                mCurrentVideoUri = mContentResolver.insert(videoTable,
                        mCurrentVideoValues);
                sendBroadcast(new Intent(android.hardware.Camera.ACTION_NEW_VIDEO,
                        mCurrentVideoUri));
            } catch (Exception e) {
                // We failed to insert into the database. This can happen if
                // the SD card is unmounted.
                mCurrentVideoUri = null;
                mCurrentVideoFilename = null;
            } finally {
                Log.v(TAG, "Current video URI: " + mCurrentVideoUri);
            }
        }
        mCurrentVideoValues = null;
    }
*/
    public static String addImage(Context context, ContentResolver resolver, String title, long date,
                Location location, int orientation, byte[] jpeg, int width, int height) {
        // Save the image.
        String path = DIRECTORY + '/' + title + ".jpg";
    	int storepath = StoredData.getInt(StoredData.M_STORAGEMODE, 0);
    	if(storepath==1)
    	{
    		path=CSStaticData.TMP_EXT_DIR+title + ".jpg";
    	}
    	else
    	{
    		path=CSStaticData.TMP_INT_DIR+title + ".jpg";	
		}
        
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            out.write(jpeg);
            
            
            // Insert into MediaStore.
            ContentValues values = new ContentValues(7);
            values.put(ImageColumns.TITLE, title);
            values.put(ImageColumns.DISPLAY_NAME, title + ".jpg");
            values.put(ImageColumns.DATE_TAKEN, date);
            values.put(ImageColumns.MIME_TYPE, "image/jpeg");
            values.put(ImageColumns.ORIENTATION, orientation);
            values.put(ImageColumns.DATA, path);
            values.put(ImageColumns.SIZE, jpeg.length);
            if (location != null) {
                values.put(ImageColumns.LATITUDE, location.getLatitude());
                values.put(ImageColumns.LONGITUDE, location.getLongitude());
            }
            Uri uri = resolver.insert(Images.Media.EXTERNAL_CONTENT_URI, values);
           // Uri uri=Uri.fromFile(new File(path));
            if (uri == null) {
            	if(CSStaticData.DEBUG)
                Log.e(TAG, "Failed to write MediaStore");
                return null;
            }
           // context.sendBroadcast(new Intent(android.hardware.Camera.ACTION_NEW_PICTURE,
           //   		uri));
            
        } catch (Exception e) {
        	if(CSStaticData.DEBUG)
            Log.e(TAG, "Failed to write image", e);
            return null;
        } finally {
            try {
                out.close();
            } catch (Exception e) {
            }
        }
        return path;
    }

	public static void getVideoInfo( ContentResolver resolver,String videoPath) {
		Log.w(TAG, "[getVideoInfo]传入路径： "+videoPath);	
		

		  String where = MediaColumns.DATA + "=?";   
          String[] selectionArgs = new String[] { videoPath };   
          
          try
		{
        	
      		
      				Cursor cursor =resolver.query(                    
      						 MediaStore.Images.Media.EXTERNAL_CONTENT_URI, 
      						null, 
      					   where,                    
      						selectionArgs,
      						null); 
      			if (cursor != null) {    
      				if(CSStaticData.DEBUG)
      				Log.e("cursor", "not  null");
      				   cursor.moveToFirst();                    
      					                 
      					String mimeType = cursor                            
      							.getString(cursor                                    
      									.getColumnIndexOrThrow(MediaColumns.MIME_TYPE));                    
      					String path = cursor                            
      							.getString(cursor                                    
      									.getColumnIndexOrThrow(MediaColumns.DATA));   
      				} 
      			else {
      				if(CSStaticData.DEBUG)
					Log.e(TAG, "cursor==null");
				}
      		
		} catch (Exception e)
		{
			// TODO: handle exception
			if(CSStaticData.DEBUG)
			Log.e(TAG, "query exception ,failed");
		}
	}
    public static long getAvailableSpace() {
        String state = Environment.getExternalStorageState();
        Log.d(TAG, "External storage state=" + state);
        if (Environment.MEDIA_CHECKING.equals(state)) {
            return PREPARING;
        }
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return UNAVAILABLE;
        }

        File dir = new File(DIRECTORY);
        dir.mkdirs();
        if (!dir.isDirectory() || !dir.canWrite()) {
            return UNAVAILABLE;
        }

        try {
            StatFs stat = new StatFs(DIRECTORY);
            return stat.getAvailableBlocks() * (long) stat.getBlockSize();
        } catch (Exception e) {
            Log.i(TAG, "Fail to access external storage", e);
        }
        return UNKNOWN_SIZE;
    }

    /**
     * OSX requires plugged-in USB storage to have path /DCIM/NNNAAAAA to be
     * imported. This is a temporary fix for bug#1655552.
     */
    public static void ensureOSXCompatible() {
        File nnnAAAAA = new File(DCIM, "100ANDRO");
        if (!(nnnAAAAA.exists() || nnnAAAAA.mkdirs())) {
        	if(CSStaticData.DEBUG)
            Log.e(TAG, "Failed to create " + nnnAAAAA.getPath());
        }
    }
}

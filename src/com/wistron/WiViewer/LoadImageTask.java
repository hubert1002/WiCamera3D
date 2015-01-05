package com.wistron.WiViewer;


import java.util.Date;



import Utilities.CSStaticData;
import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.animation.AnimationUtils;
import android.widget.SeekBar;
import android.widget.Toast;


/**
 * Asynchronous task for loading source  screennail.
 */
public class LoadImageTask extends AsyncTask<Object, Integer, Bitmap[]> {
String TAG="LoadImageTask";
    /**
     * Callback for the completed asynchronous task.
     */
    public interface Callback {

        void onComplete(long time,Bitmap[] mBitmaps);
    }
    private final Context context;
    private final Callback callback;
    WiImageView mImageView;
    long  time;
    private boolean isCancled=false;
    public LoadImageTask(Context context, Callback callback,WiImageView mImageView) {
        this.context = context;
        this.callback = callback;
        this.mImageView=mImageView;
        isCancled=false;
    }

/**
 * 
 */
    @Override
    protected Bitmap[] doInBackground(Object... params) {
        if (params[0] == null) {
            return null;
        }
        String  filepath= (String) params[0];
        
        if(CSStaticData.DEBUG)
        	Log.e(TAG, "filepath"+filepath);
        
        long time1=new Date().getTime();
        Bitmap[] mBitmaps=  mImageView.getNextBitmapEx(filepath);
        long time2=new Date().getTime();
        time=time2-time1;
        return mBitmaps;
    }

    @Override
	protected void onProgressUpdate(Integer... values)
	{
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

	@Override
	protected void onCancelled()
	{
		
		// TODO Auto-generated method stub
		if(CSStaticData.DEBUG)
			Log.e(TAG, "onCancelled called");
		
		isCancled=true;
		super.onCancelled();
	}

	@Override
    protected void onPostExecute(Bitmap[] result) {
    	String mString="load  complete";
        if (result == null) {
        	  mString="loading failure";
        }
        if(CSStaticData.DEBUG)
        {
        	Toast toast = Toast.makeText(context, mString, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        if(!isCancled)
        callback.onComplete(time,result); 
    }
}

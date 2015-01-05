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
 * Asynchronous task for loading source video screennail.
 */
public class LoadVideoFrameTask extends AsyncTask<Object, Integer, Integer> {
String TAG="LoadImageTask";
    /**
     * Callback for the completed asynchronous task.
     */
    public interface Callback {

        void onComplete(long time);
    }
    private final Context context;
    private final Callback callback;
    MediaMetadataRetriever retriever;
    Handler handler;
    long  time;
    public LoadVideoFrameTask(Context context, Callback callback, String filepath,Handler handler) {
        this.context = context;
        this.callback = callback;
        this.handler=handler;
        MediaMetadataRetriever mretriever = new MediaMetadataRetriever();
        mretriever.setDataSource(filepath);
        retriever=mretriever;
    }

/**
 * 第一个参数为影片长度，第二个为取几个缩图为5,7或8，第三个为起始时间,第四个为图片大小。第五个为图片偏移
 */
    @Override
    protected Integer doInBackground(Object... params) {
        if (params[0] == null||params[1]==null||params[2]==null||params[3]==null||params[4]==null) {
            return -1;
        }
      
        int  duration=(Integer) params[0];
        duration=duration*1000;
        int  ratio= (Integer) params[1];
        int  start=(Integer) params[2];
        Boolean  isbase= (Boolean) params[3];
        int  paraindex= (Integer) params[4];

        if(!isbase)
        {
        	start=(start+(paraindex-10)*1000)*1000;
        }
        
        if(CSStaticData.DEBUG)
        	Log.e(TAG, "[ 初始取帧当前时间点为 ]----->"+start);
         
        boolean isright=true;
        if(params.length==6)
        {
        	if(params[5]!=null)
            {
            	isright=false;
            }
        }
        
        long seperatetime;
        if(isbase)
        {
        	 seperatetime=duration/ratio;
        }
        else
        {
        	if(start>duration-7000000)
        	{
        		start=duration-7000000;
        	}
        	else if(start<0){
        		start=0;         		
        	}
        	seperatetime=1000000;
        }
        long time1=new Date().getTime();
        	 for (int i = 0; i < ratio; i++)
     		{
             	 if(!isCancelled())
             	 {
             		 long a=0;
             		 int index=0;
             		 if(isright)
             		 {
             			a=(start+i*seperatetime);	
             			index=paraindex+i;
             		 }
             		 else 
             		 {
             			a=(start-i*seperatetime);	
             			index=paraindex-i;
					  }
             		 
             		if(CSStaticData.DEBUG)
             			Log.e(TAG, "时间为"+a);
             		
             		 Bitmap temp=null;
             		 if(index>=0&&index<=30)
             		 {
             			 temp= retriever.getFrameAtTime(a,MediaMetadataRetriever.OPTION_NEXT_SYNC);
             		 }
                	  if(temp!=null)
                	  {
                		  Message msg=new Message();
                    	  msg.arg1=ratio;
                    	  msg.arg2=index;
                    	  if(isbase)
                    	  {
                    		  msg.obj=  ThumbnailUtils.extractThumbnail(temp, 83, 53);
                    	  }
                    	  else {
                    		  msg.obj=  ThumbnailUtils.extractThumbnail(temp, 72, 48);
						}
                    	  handler.sendMessage(msg);
                    	  temp.recycle();
                	  }
                	  else {
                		  
                		  if(CSStaticData.DEBUG)
                			  Log.e(TAG,"图片取出为空" );
					}
             	 }
     		}
        long time2=new Date().getTime();
        time=time2-time1;
        return 1;
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
		super.onCancelled();
		retriever.release();
	}

	@Override
    protected void onPostExecute(Integer result) {
    	String mString="load  complite";
        if (result == -1) {
        	  mString="loading failure";
        	  Toast toast = Toast.makeText(context, mString, Toast.LENGTH_SHORT);
              toast.setGravity(Gravity.CENTER, 0, 0);
              toast.show();
        }
        retriever.release();
        callback.onComplete(time); 
    }
}

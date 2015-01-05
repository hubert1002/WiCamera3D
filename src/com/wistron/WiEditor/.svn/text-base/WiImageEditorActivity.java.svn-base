package com.wistron.WiEditor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Stack;

import com.wistron.StreamHelper.MPOFileStreamParser;
import com.wistron.WiCamera.WiPanorama.PanoUtil;
import com.wistron.WiViewer.ImageViewerStateInfo;
import com.wistron.WiViewer.MediaFilePicker;
import com.wistron.WiViewer.OperateBitmaps;
import com.wistron.WiViewer.WiVideoViewerActivity;
import com.wistron.swpc.wicamera3dii.R;

import Utilities.CSStaticData;
import Utilities.FileTypeHelper;
import android.R.bool;
import android.R.integer;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.ImageColumns;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class WiImageEditorActivity extends Activity
{
	String TAG = "WiImageEditorActivity";
	private FilterStack filterStack;
	Bitmap mBitmap;
	Button button_cancel;
	Button button_save;
	private int screenWidth;
	private int screenHeight;
	RelativeLayout mRelativeLayout;
	private final Matrix photoMatrix = new Matrix();
	private Stack<PointF> undoStack1 = new Stack<PointF>();
	String filepath;
	String outputpath;
	boolean is3D = false;
	boolean isMpo = false;
    String Tag="WiImageEditorActivity";
    String m_title="redeye";
    String m_redeyefilepath_back;
	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		screenHeight = getWindowManager().getDefaultDisplay().getHeight();
		setContentView(R.layout.wiimageeditoractivity_main);
		mRelativeLayout = (RelativeLayout) findViewById(R.id.relativelayout_background);
		button_cancel = (Button) findViewById(R.id.button_cancel);
		button_save = (Button) findViewById(R.id.button_execute);
		initViewEvent();

		// if (Intent.ACTION_EDIT.equalsIgnoreCase(intent.getAction())) {
		// sourceUri = intent.getData();
		// }

		Intent intent = this.getIntent();
		String intentPath = intent.getDataString();
		if (intentPath != null)
		{
			// 第三方调用
			filepath = Uri.decode(intentPath);
		} else
		{
			// 内部调用通过gallery启动图片浏览器
			String fileName = intent.getStringExtra("filePath");
			if (fileName != null)
			{
				filepath = fileName;
			}
		}

	//	filepath = "mnt/sdcard/RG.mpo";
		outputpath = genOutputPath(filepath);
		mBitmap = openBitmap(filepath);
		filterStack = new FilterStack(
				(PhotoView) findViewById(R.id.photo_view),
				new FilterStack.StackListener()
				{

					@Override
					public void onStackChanged(boolean canUndo, boolean canRedo)
					{

					}
				});
		filterStack.setPhotoSource(mBitmap, new OnDoneCallback()
		{
			@Override
			public void onDone()
			{
				if(CSStaticData.DEBUG)
				Log.e(TAG, "加载图片完成");
			}
		});
		initStatusData();
	}
/**
 * 得到保存路径
 * @param path
 * @return
 */
	public String genOutputPath(String path)
	{
		String mString = "mnt/sdcard/redeye.jpg";
		if (path != null)
		{
			String[] maStrings = path.split("\\.");
			if (maStrings.length > 1)
			{
				mString = "";
				long time= System.currentTimeMillis();
				   String shottime=	PanoUtil.createName("hhmmss", time);
				maStrings[maStrings.length - 1] = "_redeye"+shottime+"."
						+ maStrings[maStrings.length - 1];
				for (int i = 0; i < maStrings.length; i++)
				{
					mString += maStrings[i];
					if(CSStaticData.DEBUG)
					Log.e(TAG, mString);
				}
			}
		}
		if(CSStaticData.DEBUG)
		Log.e(TAG, "输出路径为" + mString);
		return mString;

	}

	/**
	 * 解码图片，当时jpg jps时直接打开，如果为mpo则打开后，将两张图片先合成一张
	 * 
	 * @param filepath
	 * @return
	 */

	public Bitmap openBitmap(String filepath)
	{
		Bitmap bitmap = null;
		is3D = FileTypeHelper.isStereoImageFile(filepath);
		OperateBitmaps mOperateBitmaps=new OperateBitmaps(getApplicationContext());
		if (filepath.toLowerCase().endsWith(".mpo"))
		{
			isMpo = true;
		}
		try
		{
			if (isMpo)
			{
				bitmap = mOperateBitmaps.decodeMpotoSingle(filepath, screenWidth, screenHeight);
			} else
			{
				bitmap = mOperateBitmaps.decodeNomalBitmaptoSingle(filepath, screenWidth, screenHeight);
			}
		} catch (OutOfMemoryError e)
		{
			if(CSStaticData.DEBUG)
			Log.e(TAG, "加载图片失败,内存问题");
		} catch (Exception e)
		{
			if(CSStaticData.DEBUG)
			Log.e(TAG, "加载图片失败");
		}
		return bitmap;
	}
	public void saveBitmap(Bitmap bitmap)
	{
		if (bitmap != null)
		{
			if (isMpo)
			{
				// 先才成两半，然后保存
				Bitmap[] mBitmaps = new Bitmap[2];
				mBitmaps[0] = Bitmap.createBitmap(bitmap, 0, 0,
						bitmap.getWidth() / 2, bitmap.getHeight());
				mBitmaps[1] = Bitmap.createBitmap(bitmap,
						bitmap.getWidth() / 2, 0, bitmap.getWidth() / 2,
						bitmap.getHeight());

				try
				{

					ByteArrayOutputStream out0 = new ByteArrayOutputStream();
					mBitmaps[0].compress(Bitmap.CompressFormat.JPEG, 100, out0);
					byte[] byte0 = out0.toByteArray();
					out0.flush();
					out0.close();
					if(CSStaticData.DEBUG)
					Log.e(TAG, "byte长度为" + byte0.length);

					ByteArrayOutputStream out1 = new ByteArrayOutputStream();
					mBitmaps[1].compress(Bitmap.CompressFormat.JPEG, 100, out1);
					byte[] byte1 = out1.toByteArray();
					out1.flush();
					out1.close();
					boolean bool = MPOFileStreamParser.encodeFile(outputpath,
							byte0, byte1);
					if(bool)
					{
						//添加到媒体数据库
						if(CSStaticData.DEBUG)
						Log.e(Tag, "保存图片成功，文件路径为"+outputpath);
						File temp=new File(outputpath);
						  ContentValues values = new ContentValues(4);
					        values.put(ImageColumns.DATE_TAKEN, temp.lastModified());
					        values.put(ImageColumns.MIME_TYPE, "image/jpeg");
					        values.put(ImageColumns.ORIENTATION, 0);
					        values.put(ImageColumns.DATA, outputpath);
					        Uri uri = WiImageEditorActivity.this.getContentResolver().insert(Images.Media.EXTERNAL_CONTENT_URI, values);
					        if (uri != null) {
					        	if(CSStaticData.DEBUG)
					            Log.e(TAG, "Insert new image to MediaStore");
					        	m_redeyefilepath_back=outputpath;
					        }
					}
					if(CSStaticData.DEBUG)
					Log.e(TAG, "是否保存成功" + bool);
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else
			{
				try
				{
					FileOutputStream out = new FileOutputStream(outputpath);
					bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
					out.flush();
					out.close();
					if(CSStaticData.DEBUG)
						Log.e(Tag, "保存图片成功，文件路径为"+outputpath);
						File temp=new File(outputpath);
						  ContentValues values = new ContentValues(4);
					        values.put(ImageColumns.DATE_TAKEN, temp.lastModified());
					        values.put(ImageColumns.MIME_TYPE, "image/jpeg");
					        values.put(ImageColumns.ORIENTATION, 0);
					        values.put(ImageColumns.DATA, outputpath);
					        Uri uri = WiImageEditorActivity.this.getContentResolver().insert(Images.Media.EXTERNAL_CONTENT_URI, values);
					        if (uri != null) {
					        	if(CSStaticData.DEBUG)
					            Log.e(TAG, "Insert new image to MediaStore");
					        	m_redeyefilepath_back=outputpath;
					        }
					
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		if(CSStaticData.DEBUG)
		Log.e("saveBitmap", "ok");
	}
@Override
public void finish()
{
	// TODO Auto-generated method stub
	Intent intent=new Intent();
	intent.putExtra("redeyefilepath", m_redeyefilepath_back);
	WiImageEditorActivity.this.setResult(RESULT_OK, intent);
	super.finish();
}
	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		// 释放资源
		Filter.releaseContext();
		super.onDestroy();
	}

	public void initViewEvent()
	{
		button_cancel.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				if(CSStaticData.DEBUG)
				Log.e(TAG, "取消按钮点中");
//				if (undoStack1.size() > 0)
//				{
//					if(CSStaticData.DEBUG)
//					Log.e(TAG, "undoStack1大小" + undoStack1.size());
//					undoStack1.pop();
//					RedEyeFilter mEyeFilter = new RedEyeFilter();
//					mEyeFilter.setEyes(undoStack1);
//					filterStack.pushFilterRedeye(mEyeFilter);
//					filterStack.updateUI();
//				}
				finish();
			}
		});
		button_save.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				filterStack.saveRedEyeBitmap(new OnDoneBitmapCallback()
				{
					@Override
					public void onDone(Bitmap bitmap)
					{
						// TODO Auto-generated method stub
                       if(bitmap!=null)
                       {
                    	   saveBitmap(bitmap);
                    	   Toast.makeText(WiImageEditorActivity.this, "Remove red eye complete!", 1000).show();
                       }
                       else
                       {
                    	   
                       }
						finish();
					}
				});
			}
		});
		mRelativeLayout.setOnTouchListener(new OnTouchListener()
		{

			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				// TODO Auto-generated method stub

				PointF point = mapPhotoPoint(event.getX(), event.getY());
				if(CSStaticData.DEBUG)
				Log.e(TAG, "onTouch 比例为" + point.x + "*" + point.y);
				undoStack1.push(point);

				RedEyeFilter mEyeFilter = new RedEyeFilter();
				mEyeFilter.setEyes(undoStack1);
				filterStack.pushFilterRedeye(mEyeFilter);
				filterStack.updateUI();
				return false;
			}
		});
	}

	/**
	 * 将屏幕的点击位置映射到图片上，采用相对值
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	protected PointF mapPhotoPoint(float x, float y)
	{
		float[] point = new float[]
{ x, y };
		Matrix matrix = new Matrix();
		matrix.setRectToRect(
				new RectF(0, 0, mBitmap.getWidth(), mBitmap.getHeight()),
				new RectF(0, 0, screenWidth, screenHeight),
				Matrix.ScaleToFit.CENTER);
		matrix.invert(photoMatrix);
		photoMatrix.mapPoints(point);
		PointF pointF = new PointF();
		pointF.x = point[0] / mBitmap.getWidth();
		pointF.y = point[1] / mBitmap.getHeight();
		return pointF;
	}
	
	/**
	 * 恢复保存状态
	 */
	private void initStatusData() {
		Stack<PointF> restore = (Stack<PointF>) getLastNonConfigurationInstance();
		if(restore != null){
			if(restore.size()>0)
			{
				undoStack1=restore;
				    if(CSStaticData.DEBUG)
					Log.e(TAG, "undoStack1大小" + undoStack1.size());
					RedEyeFilter mEyeFilter = new RedEyeFilter();
					mEyeFilter.setEyes(undoStack1);
					filterStack.pushFilterRedeye(mEyeFilter);
					filterStack.updateUI();
			}
		}
	}
    
    /**
     * 保存状态
     */
	@Override
	public Object onRetainNonConfigurationInstance() {
		Log.w(Tag, "[onRetainNonConfigurationInstance]");
		Stack<PointF> restore=undoStack1;
		return restore;
	}
	
	
}
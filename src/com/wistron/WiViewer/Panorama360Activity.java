package com.wistron.WiViewer;

import java.util.List;

import com.wistron.swpc.wicamera3dii.R;

import Utilities.CSStaticData;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ZoomControls;

public class Panorama360Activity extends Activity implements SensorEventListener {
    /** Called when the activity is first created. */
static	int screenw;
static	int screenh;
private SensorManager sensorManager;
float   m_orientation=0;
float[]  m_bufferOritation ={0,0,0,0,0};
int m_times=0;
float m_alpha=0.5f;
float m_alpha2=0.2f;
PanoramaView mPanoramaView;
TextView mTextView;
Button m_btn_normal;
Button m_btn_filtrate;
Button m_btn_switchUI;
Button  m_btn_filter2;
int filtermode=0;
String TAG="Panorama360Activity";
//ZoomControls mzoom;
RelativeLayout m_relative_pano;
String m_filepath="";
String m_cmd="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		screenw=getWindowManager().getDefaultDisplay().getWidth();
		screenh=getWindowManager().getDefaultDisplay().getHeight();
        setContentView(R.layout.panorama360);
        m_relative_pano=(RelativeLayout) findViewById(R.id.relativelayout_pano);
        
        
        
        Intent intent=Panorama360Activity.this.getIntent();
        String intentPath = intent.getDataString();
		 if(intentPath != null){
		 //第三方调用
			 m_filepath = Uri.decode(intentPath);
		 }
		 else 
		 {
		        //内部调用通过gallery启动图片浏览器
			    m_cmd=  intent.getStringExtra("cmd");
				String fileName = intent.getStringExtra(
						"filePath");
				if(fileName!=null)
				{
					m_filepath=fileName;
				}
				else {
					finish();
				}
		 }
        //m_filepath="mnt/sdcard/big.PNG";
		// m_filepath="mnt/sdcard/mypano4.jpg";
		 mPanoramaView=new PanoramaView(Panorama360Activity.this, m_filepath);
		 if(!mPanoramaView.isValid)
		 {
			 //异常退出
			 finish();
		 }
		 
       // mPanoramaView=new PanoramaView(Panorama360Activity.this, m_filepath);
        m_relative_pano.addView(mPanoramaView);
        mTextView=(TextView) findViewById(R.id.editText1);
        m_btn_normal=(Button) findViewById(R.id.button3);
        m_btn_filtrate=(Button) findViewById(R.id.button2);
        m_btn_switchUI=(Button) findViewById(R.id.button1);
        m_btn_filter2=(Button) findViewById(R.id.button4);
		initButtonClick();
    }
    
    @Override
    protected void onResume()
    {
    	// TODO Auto-generated method stub
    	sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		List<Sensor> sensors = sensorManager
				.getSensorList(Sensor.TYPE_ORIENTATION);
		if (sensors.size() > 0) {
			sensorManager.registerListener(this, sensors.get(0),
					SensorManager.SENSOR_DELAY_FASTEST);
		}
    	super.onResume();
    }
    @Override
    public void finish()
    {
    	Panorama360Activity.this.setResult(RESULT_OK);
    	super.finish();
    }
    public void initButtonClick()
    {
    	m_btn_filter2.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				filtermode=2;
			}
		});
    	m_btn_normal.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				filtermode=0;
			}
		});
    	m_btn_filtrate.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stubgg
				if(CSStaticData.DEBUG)
				Log.e(TAG, "m_btn_filtrate clicked");
				filtermode=1;
			}
		});
    	m_btn_switchUI.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				mPanoramaView.switchUImode();
			}
		});
    }
    @Override
    protected void onPause()
    {
    	// TODO Auto-generated method stub
    	if (sensorManager != null) {
			sensorManager.unregisterListener(this);
		}
    	super.onPause();
    }
    @Override
    protected void onDestroy()
    {
    	// TODO Auto-generated method stub

    	mPanoramaView.release();
    	super.onDestroy();
    }
    @Override
	public boolean dispatchTouchEvent(MotionEvent event)
	{   
    	{
    		boolean state = false;
    		state |= super.dispatchTouchEvent(event);
    		state |= mPanoramaView.onTouchEvent(event);
    		return state;
    	}
//    	{
//    		super.dispatchTouchEvent(event);
//    		mPanoramaView.onTouchEvent(event);
//    		return false;
//    	}
	}
    public void setOrientationToBuffer(float degree)
    {
    	for (int i = 0; i < m_bufferOritation.length-1; i++)
		{
    		m_bufferOritation[i]=m_bufferOritation[i+1];
		}
    	m_bufferOritation[4]=degree;
    }
	@Override
	public void onSensorChanged(SensorEvent event)
	{
		// TODO Auto-generated method stub
		float tempDegree=event.values[0];
		setOrientationToBuffer(Math.round(event.values[0]));
		if(m_times<5)
		{
			m_orientation=event.values[0];
			mPanoramaView.setInitOrientation(m_orientation);
			m_times++;
		}
		else
		{
			if(filtermode==1)
			{
				if(Math.abs(event.values[0]-m_orientation)>1)
				{
					mPanoramaView.updateUIWhenOrientationChanged(tempDegree,tempDegree);
				}
				m_orientation=tempDegree;
			}
			else if(filtermode==0)
			{
				float average=m_alpha*event.values[0]+(1-m_alpha)*m_orientation;
				m_orientation= mPanoramaView.updateUIWhenOrientationChanged(average,tempDegree);
			}
			else if (filtermode==2) {
				float total=0;
				for (int i = 0; i < m_bufferOritation.length; i++)
				{
					total+=m_bufferOritation[i];
				}
				total=total/5f;
				m_orientation= mPanoramaView.updateUIWhenOrientationChanged(total,tempDegree);
			}
			
			
			
		}
		mTextView.setText("当前方位值为"+event.values[0]+"当前视角为"+mPanoramaView.m_sight_angle);
	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy)
	{
		// TODO Auto-generated method stub
		
	}
	
}
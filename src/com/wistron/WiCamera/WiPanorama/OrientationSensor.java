package com.wistron.WiCamera.WiPanorama;



import java.util.List;

import Utilities.CSStaticData;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class OrientationSensor   implements SensorEventListener {
	private SensorManager sensorManager;
	float[] a={0,0,0};
	RotateAround mAround=null;
    float[]  	gravity=new float[3];
	  final float alpha = 0.2f;
	  private boolean isEnable=false;
	  private int mStartTimes=0;
	  public boolean isAroundComplete=false;
    public  OrientationSensor(Context context) {
        sensorManager = (SensorManager)context.getSystemService(context.SENSOR_SERVICE);
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
        if(CSStaticData.DEBUG)
        Log.e("geshu ", ""+sensors.size());
		if (sensors.size() > 0) {
			sensorManager.registerListener(this, sensors.get(0), SensorManager.SENSOR_DELAY_FASTEST);
		}
		mAround=new RotateAround();
    }
    public void setEnable()
    {
    	mStartTimes=0;
    	isEnable=true;
    }
    public void setDirect(int direct )
    {
    	mAround.setDirection(direct);
    }
    public void resetToInit()
    {
    	mAround.resetToInit();
    	isEnable=false;
    	mStartTimes=0;
    	isAroundComplete=false;
    }
	public void unregisterSensorListener() {
		if(sensorManager!=null)
		sensorManager.unregisterListener(this);
	}
	
	@Override
	public void onSensorChanged(SensorEvent event)
	{
		// TODO Auto-generated method stub
		if(isEnable)
		{
			float temp=event.values[0];
			mStartTimes++;
			if(mStartTimes<3)
			{
				mAround.setStartOrientation(temp);
				return;
			}
			isAroundComplete= mAround.onOrientationChanged(temp);
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy)
	{
		// TODO Auto-generated method stub
	}
}
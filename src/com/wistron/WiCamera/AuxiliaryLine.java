package com.wistron.WiCamera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * http://www.cnblogs.com/feisky/archive/2010/09/12/1824320.html
 * 
 * @author WH1107063
 * 
 */
public class AuxiliaryLine extends View {
	private static float rate = 0.618f;
	private int previewwidth;
	private int previewheight;
	private int type;
	private boolean is3D;
	private Paint myPaint;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.e("touch event  in overlayview", "ok");
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				type++;
				type = type % 3;
				type=2;
				invalidate();
			}
		return false;
	}

	public AuxiliaryLine(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		previewwidth = getWidth();
		previewheight = getHeight();
		Log.e("AuxiliaryLine", " 长宽为" + previewwidth + "*" + previewheight);
		type = 0;
		myPaint = new Paint();
		myPaint.setColor(Color.rgb(230, 230, 230));
		myPaint.setStyle(Paint.Style.STROKE);
		myPaint.setStrokeWidth(1);
		myPaint.setAntiAlias(true);
		PathEffect effects = new DashPathEffect(new float[]{8,8,8,8},0.5f);  
	    myPaint.setPathEffect(effects);  
	}

	public void setSize(int previewwidth, int previewheight) {
		this.previewwidth = previewwidth;
		this.previewheight = previewheight;
		invalidate();
	}

	/**
	 * 设置辅助线种类，0为隐藏，1为平均分，2为黄金比例分割
	 * 
	 * @param type
	 */
	public void setLineType(int type) {
		this.type = type;
		type=2;
		invalidate();
	}

	public void setDimension(boolean is3D) {
		this.is3D = is3D;
		invalidate();
	}

	protected void onDraw(Canvas canvas) {
		int width=previewwidth/3;
		int height=previewheight/3+4;
		if (is3D) {
			switch (type) {
			case 0:

				break;
			case 1:
				canvas.drawLine(0, previewheight / 4, previewwidth,
						previewheight / 4, myPaint);
				canvas.drawLine(0, previewheight * 3 / 4, previewwidth,
						previewheight * 3 / 4, myPaint);

				canvas.drawLine(previewwidth / 8, 0, previewwidth / 8,
						previewheight, myPaint);
				canvas.drawLine(previewwidth * 3 / 8, 0, previewwidth * 3 / 8,
						previewheight, myPaint);
				canvas.drawLine(previewwidth * 5 / 8, 0, previewwidth * 5 / 8,
						previewheight, myPaint);
				canvas.drawLine(previewwidth * 7 / 8, 0, previewwidth * 7 / 8,
						previewheight, myPaint);
				break;
			case 2:
				canvas.drawLine(0, previewheight * (1 - rate), previewwidth,
						previewheight * (1 - rate), myPaint);
				canvas.drawLine(0, previewheight * rate, previewwidth,
						previewheight * rate, myPaint);
				canvas.drawLine(previewwidth * (1 - rate) / 2, 0, previewwidth
						* (1 - rate) / 2, previewheight, myPaint);
				canvas.drawLine(previewwidth * rate / 2, 0, previewwidth * rate
						/ 2, previewheight, myPaint);

				canvas.drawLine(previewwidth * (2 - rate) / 2, 0, previewwidth
						* (2 - rate) / 2, previewheight, myPaint);
				canvas.drawLine(previewwidth * (1 + rate) / 2, 0, previewwidth
						* (1 + rate) / 2, previewheight, myPaint);
				break;

			}

		} else {

			switch (type) {
			case 0:

				break;
			case 1:
				canvas.drawLine(0, previewheight / 4, previewwidth,
						previewheight / 4, myPaint);
				canvas.drawLine(0, previewheight * 3 / 4, previewwidth,
						previewheight * 3 / 4, myPaint);
				canvas.drawLine(previewwidth / 4, 0, previewwidth / 4,
						previewheight, myPaint);
				canvas.drawLine(previewwidth * 3 / 4, 0, previewwidth * 3 / 4,
						previewheight, myPaint);

				break;
			case 2:
				Path path1 = new Path();       
				
		        path1.moveTo(previewwidth/2-width/2, previewheight/2-height*2/5);  
		        path1.lineTo(previewwidth/2+width/2,previewheight/2-height*2/5);        
		        canvas.drawPath(path1, myPaint); 
		        Path path2 = new Path();  
		        path2.moveTo(previewwidth/2-width*2/5, previewheight/2-height/2);  
		        path2.lineTo(previewwidth/2-width*2/5,previewheight/2+height/2);        
		        canvas.drawPath(path2, myPaint);
		        Path path3 = new Path(); 
		        path3.moveTo(previewwidth/2+width*2/5, previewheight/2-height/2);  
		        path3.lineTo(previewwidth/2+width*2/5,previewheight/2+height/2);        
		        canvas.drawPath(path3, myPaint);
		        Path path4 = new Path(); 
		        path4.moveTo(previewwidth/2-width/2, previewheight/2+height*2/5);  
		        path4.lineTo(previewwidth/2+width/2,previewheight/2+height*2/5);        
		        canvas.drawPath(path4, myPaint);
//				canvas.drawLine(0, previewheight * (1 - rate), previewwidth,
//						previewheight * (1 - rate), myPaint);
//				canvas.drawLine(0, previewheight * rate, previewwidth,
//						previewheight * rate, myPaint);
//				canvas.drawLine(previewwidth * (1 - rate), 0, previewwidth
//						* (1 - rate), previewheight, myPaint);
//				canvas.drawLine(previewwidth * rate, 0, previewwidth * rate,
//						previewheight, myPaint);

				break;

			}
		}

	}

}

package com.wistron.WiCamera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 
 * @Copyright (c) 2011 Wistron SWPC All rights reserved.
 * 
 * @created: 2012/06/21
 * @filename: OperationFile.java
 * @author WH1107063(周海江)
 * @purpose 连拍两张合成3D图片预览时候显示的view
 * 
 * 
 * 
 * 
 */
public class OverView extends View {
	public int alpha = 160;
	public Bitmap mbitmap;

	public OverView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		// TODO Auto-generated constructor stub
	}

	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}

	public void SetmBitmap(Bitmap mbitmap) {
		this.mbitmap = mbitmap;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(0x00000000);

		if (mbitmap != null) {
			Paint p = new Paint();
			p.setAlpha(alpha);
			canvas.drawBitmap(mbitmap, 0, 0, p);
		}

	}

	float prex;
	float prey;
	float curx;
	float cury;

	/**
	 * 在预览时，向左滑动则该view的透明度递减，否则，递增
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		System.out.println("overlayview touch");
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			prex = event.getX();
			prey = event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			curx = event.getX();
			cury = event.getY();
			if ((curx - prex > 0) || (cury - prey > 0)) {
				alpha += 10;
				if (alpha >= 255) {
					alpha = 255;
				}
			}
			if ((curx - prex < 0) || (cury - prey < 0)) {
				alpha -= 10;
				if (alpha <= 0) {
					alpha = 0;
				}
			}
			invalidate();
			break;

		default:
			break;
		}
		return super.onTouchEvent(event);
	}
}
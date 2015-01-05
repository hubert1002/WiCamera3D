package com.wistron.WiCamera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.hardware.Camera.Face;
import android.util.AttributeSet;
import android.view.View;

import com.wistron.swpc.wicamera3dii.R;

/**
 * 
 * @Copyright (c) 2011 Wistron SWPC All rights reserved.
 * 
 * @created: 2012/02/21
 * @filename: OperationFile.java
 * @author WH1107063(周海江)
 * @purpose 把人脸识别的区域对应到屏幕的具体位置并画出矩形来标注的类
 * 
 * 
 * 
 * 
 */
public class FaceIndicater extends View {
	private Face[] mFaces;
	private Matrix mMatrix = new Matrix();
	Paint myPaint;
	private Drawable mFaceIndicator;
	// The value for android.hardware.Camera.setDisplayOrientation.
	private int mDisplayOrientation;
	// The orientation compensation for the face indicator to make it look
	// correctly in all device orientations. Ex: if the value is 90, the
	// indicator should be rotated 90 degrees counter-clockwise.
	private int mOrientation;
	String TAG = "FaceIndicater";
	private RectF mRect = new RectF();
	int state = 0;

	// Drawable mydraDrawable;
	public FaceIndicater(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		myPaint = new Paint();
		// mFaceIndicator = getResources().getDrawable(
		// R.drawable.ic_focus_face_focused);
		mFaceIndicator = getResources().getDrawable(R.drawable.camera_focus);
		myPaint.setColor(Color.GREEN);
		myPaint.setStyle(Paint.Style.STROKE);
		myPaint.setStrokeWidth(3);
		myPaint.setAntiAlias(true);
		myPaint.setFilterBitmap(true);
		// mydraDrawable=getResources().getDrawable(R.drawable.ic_focus_focusing);
	}

	public void setDisplayOrientation(int orientation) {
		mDisplayOrientation = orientation;
	}

	public void setOrientation(int orientation) {
		mOrientation = orientation;
		invalidate();
	}

	public void setFaces(Face[] faces) {
		// Log.v(TAG, "Num of faces=" + faces.length);
		if (faces != null && faces.length >= 1)
			System.out.println("人脸位置参数" + faces[0].rect.toString());
		mFaces = faces;
		invalidate();

	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (mFaces != null && mFaces.length > 0) {
			mMatrix.setScale(1, 1);
			// Focus indicator is directional. Rotate the matrix and the canvas
			// so it looks correctly in all orientations.
			mMatrix.postRotate(mDisplayOrientation);
			// Camera driver coordinates range from (-1000, -1000) to (1000,
			// 1000).
			// UI coordinates range from (0, 0) to (width, height).
			mMatrix.postScale(getWidth() / 2000f, getHeight() / 2000f);
			mMatrix.postTranslate(getWidth() / 2f, getHeight() / 2f);

			canvas.save();

			mMatrix.postRotate(mOrientation); // postRotate is clockwise
			canvas.rotate(-mOrientation);
			for (int i = 0; i < mFaces.length; i++) {
				mRect.set(mFaces[i].rect);
				mMatrix.mapRect(mRect);
				mFaceIndicator.setBounds(Math.round(mRect.left),
						Math.round(mRect.top), Math.round(mRect.right),
						Math.round(mRect.bottom));
				mFaceIndicator.draw(canvas);
				System.out.println("人脸的个数:" + mFaces.length);
				// canvas.drawRect(mRect, myPaint);
			}
			canvas.restore();
			mFaces = null;
		}
		super.onDraw(canvas);
	}
}

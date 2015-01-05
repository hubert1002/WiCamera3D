package com.wistron.WiCamera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class ArcSeekBar extends View {

	private static final String TAG = "ArcSeekBar";

	public static final int POS_1ST_QUAD = 0; // 位于第一象限
	public static final int POS_2ND_QUAD = 1; // 位于第二象限
	public static final int POS_3RT_QUAD = 2; // 位于第三象限
	public static final int POS_4ST_QUAD = 3; // 位于第四象限

	private boolean mIsTouched = false, mIsFromUser = false,
			mIsTouchable = false, mIsEnabled = true;
	public static boolean mIsInvalidate = false;
	private float mXpos = 0, mYpos = 0, mXDraw = 0, mYDraw = 0;
	private double mProgress = 0, mMax = 28, mNewProgress = 0;
	private int mWidth = 100, mHeight = 100, mThumbWidth = 77,
			mThumbHeight = 77;
	private int mQuad = 0; // View所处圆象限
	private double mDegree = 0f; // 圆上点与圆点连线的角度：0...pi ~ 0...90
	private float mScaleX = 1f; // X轴向压缩比
	private float mROffset = 0f; // 圆半径偏移（缩短圆轨迹的半径）
	private float mSemiA = 0f, // 长半轴长度: a = width = height
			mSemiB = 0f, // 短半轴长度: b = width/2 = height/2
			mSemiC = 0f; // 焦点长度 : c = sqrt(a^2 - b^2)
	private int mBackgroundResid = 0, mThumbResid = 0, mThumbResidHint = 0,
			mSeekBarOffSet = 40, mThumbOffSet = 16;
	private Bitmap mBackgroundBmp = null, mThumbBmp = null,
			mThumbBmpHint = null;

	private Matrix mDrawMatrix = null; // onDraw中使用
	private Paint mDrawPaint = null; // onDraw中使用

	private int mRadiu = 0;
	public OnArcSeekBarChangeListener marcSeekBarChangeListener;

	public ArcSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public ArcSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ArcSeekBar(Context context) {
		super(context);
		init();
	}

	public void setBackground(int resid) {
		Log.w(TAG, "[setBackground]设定背景");
		mBackgroundResid = resid;
		init();
		resize();
	}

	public void setThumb(int resid) {
		mThumbResid = resid;
	}

	public void setThumbPressed(int resid) {
		mThumbResidHint = resid;
	}

	public void setQuad(int quad) {
		Log.w(TAG, "[setQuad]设定象限");
		mQuad = quad;
		init();
		resize();
	}

	public void setRadiusOffset(float offset) {
		mROffset = offset;
		init();
		resize();
	}

	@Override
	public void setEnabled(boolean enabled) {
		// TODO Auto-generated method stub

		mIsEnabled = enabled;
		super.setEnabled(enabled);
	}

	public void setMax(int max) {
		mMax = max;
	}

	public void setScaleX(float scaleX) {
		Log.w(TAG, "[setScaleX]设定X轴向压缩比");
		/*
		 * steps: 1.压缩Thumb 2.压缩Background 3.压缩轨迹
		 */

		mScaleX = scaleX;
		resize();
	}

	public void setRadiu(int radiu) {
		mRadiu = radiu;
	}

	@Override
	public void setLayoutParams(LayoutParams params) {
		Log.w(TAG, "[setLayoutParams]设定LayoutParams");
		super.setLayoutParams(params);
		if (params != null) {
			mWidth = params.width;
			mHeight = params.height;
		}

		if (mWidth < 0) {
			mWidth = 200;
		}

		if (mHeight < 0) {
			mHeight = 200;
		}

		init();
		resize();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!mIsEnabled) {
			return false;
		}
		boolean state = false;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			int[] location = new int[2];

			this.getLocationOnScreen(location);
			int x = location[0];
			int y = location[1];

			Log.i(TAG, "x" + x + "y" + y);
			// 圆心点的坐标
			// getX()+getWidth()
			float centerX = getX() + getWidth();
			float centerY = getY() + getHeight();

			// 触摸的点和圆心的距离
			double length1 = (centerX - event.getRawX())
					* (centerX - event.getRawX()) + (centerY - event.getRawY())
					* (centerY - event.getRawY());
			double length = Math.sqrt(length1);
			if (length <= getWidth() - 40 || length > getWidth() + 20) {
				mIsTouchable = false;
				mIsTouched = false;
				state = false;
			} else {
				mIsTouchable = true;
				mIsTouched = true;
				state = true;
			}

		
			break;

		case MotionEvent.ACTION_MOVE:
			state = true;
			break;

		case MotionEvent.ACTION_UP:
			mIsTouched = false;
			state = true;
			break;

		case MotionEvent.ACTION_CANCEL:
			mIsTouched = false;
			state = true;
			break;

		default:
			mIsTouched = false;
			break;
		}

		mXpos = event.getX();
		mYpos = event.getY();
		if (event.getX() > mWidth) {
			mXpos = mWidth;
		}
		if (event.getY() > mHeight) {
			mYpos = mHeight;
		}

		if (mIsTouchable) {
			// Log.e(TAG, "ontouch");
			invalidate();
			return true;

		}

		return state;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		/*
		 * 要考虑mQuad 要考虑mScaleX 要考虑mRadiusOffset
		 */

		// 绘制Background
		canvas.drawBitmap(mBackgroundBmp, mDrawMatrix, mDrawPaint);

		// 绘制Thumb
		switch (mQuad) {
		case POS_1ST_QUAD:
			mDegree = Math.atan((mHeight - mYpos) / mXpos); // 算出角度
			if (Double.isNaN(mDegree)) {
				mDegree = 0.01;
			}
			if (mDegree <= 0) {
				mDegree = 0.01;
			}
			if (mDegree > 0.5 * Math.PI) {
				mDegree = 0.5 * Math.PI - 0.01;
			}
			mSemiA = mHeight - mROffset - mThumbHeight / 2; // 计算长轴
			mSemiB = (mWidth - mROffset - mThumbWidth / 2) * mScaleX; // 计算短轴
			mXDraw = (float) (mSemiB * Math.cos(mDegree)); // 计算X坐标
			mYDraw = -(float) (mSemiA * Math.sin(mDegree)) + mSemiA; // 计算Y坐标
			if (mIsTouched) {
				canvas.drawBitmap(mThumbBmpHint, mXDraw, mYDraw, mDrawPaint);

				Log.w(TAG, "Thumb Pos = " + mXpos + " : " + mYpos);
				Log.w(TAG,
						"Thumb Pos = "
								+ ((float) (mSemiB * Math.cos(mDegree)))
								+ " : "
								+ (-(float) (mSemiA * Math.sin(mDegree)) + mSemiA));
			} else {
				canvas.drawBitmap(mThumbBmp, mXDraw, mYDraw, mDrawPaint);

				Log.w(TAG, "Thumb Pos = " + mXpos + " : " + mYpos);
				Log.w(TAG,
						"Thumb Pos = "
								+ ((float) (mSemiB * Math.cos(mDegree)))
								+ " : "
								+ (-(float) (mSemiA * Math.sin(mDegree)) + mSemiA));
			}

			break;

		case POS_2ND_QUAD:
			mDegree = Math.atan(mYpos / mXpos); // 算出角度
			if (Double.isNaN(mDegree)) {
				mDegree = 0.01;
			}
			if (mDegree <= 0) {
				mDegree = 0.01;
			}
			if (mDegree > 0.5 * Math.PI) {
				mDegree = 0.5 * Math.PI - 0.01;
			}
			mSemiA = mHeight - mROffset - mThumbHeight / 2; // 计算长轴
			mSemiB = (mWidth - mROffset - mThumbWidth / 2) * mScaleX; // 计算短轴
			mXDraw = (float) (mSemiB * Math.cos(mDegree)); // 计算X坐标
			mYDraw = (float) (mSemiA * Math.sin(mDegree)); // 计算Y坐标
			if (mIsTouched) {
				canvas.drawBitmap(mThumbBmpHint, mXDraw, mYDraw, mDrawPaint);

				Log.w(TAG, "Thumb Pos = " + mXpos + " : " + mYpos);
				Log.w(TAG,
						"Thumb Pos = " + ((float) (mSemiB * Math.cos(mDegree)))
								+ " : "
								+ ((float) (mSemiA * Math.sin(mDegree))));
			} else {
				canvas.drawBitmap(mThumbBmp, mXDraw, mYDraw, mDrawPaint);

				Log.w(TAG, "Thumb Pos = " + mXpos + " : " + mYpos);
				Log.w(TAG,
						"Thumb Pos = " + ((float) (mSemiB * Math.cos(mDegree)))
								+ " : "
								+ ((float) (mSemiA * Math.sin(mDegree))));
			}
			break;

		case POS_3RT_QUAD:
			mDegree = Math.atan(mYpos / (mWidth - mXpos)); // 算出角度
			if (Double.isNaN(mDegree)) {
				mDegree = 0.01;
			}
			if (mDegree <= 0) {
				mDegree = 0.01;
			}
			if (mDegree > 0.5 * Math.PI) {
				mDegree = 0.5 * Math.PI - 0.01;
			}
			mSemiA = mHeight - mROffset - mThumbHeight / 2; // 计算长轴
			mSemiB = (mWidth - mROffset - mThumbWidth / 2) * mScaleX; // 计算短轴
			mXDraw = -(float) (mSemiB * Math.cos(mDegree)) + mSemiB; // 计算X坐标
			mYDraw = (float) (mSemiA * Math.sin(mDegree)); // 计算Y坐标
			if (mIsTouched) {
				canvas.drawBitmap(mThumbBmpHint, mXDraw, mYDraw, mDrawPaint);

				Log.w(TAG, "Thumb Pos = " + mXpos + " : " + mYpos);
				Log.w(TAG,
						"Thumb Pos = "
								+ (-(float) (mSemiB * Math.cos(mDegree)) + mSemiB)
								+ " : "
								+ ((float) (mSemiA * Math.sin(mDegree))));
			} else {
				canvas.drawBitmap(mThumbBmp, mXDraw, mYDraw, mDrawPaint);

				Log.w(TAG, "Thumb Pos = " + mXpos + " : " + mYpos);
				Log.w(TAG,
						"Thumb Pos = "
								+ (-(float) (mSemiB * Math.cos(mDegree)) + mSemiB)
								+ " : "
								+ ((float) (mSemiA * Math.sin(mDegree))));
			}
			break;

		case POS_4ST_QUAD:

			if (mIsTouchable) {

				if (!mIsFromUser) {

					mDegree = Math.atan((mHeight - mYpos) / (mWidth - mXpos)); // 算出角度
					if (Double.isNaN(mDegree)) {
						mDegree = 0.01;
					}
					if (mDegree <= 0) {
						mDegree = 0.01;
					}
					if (mDegree > 0.5 * Math.PI) {
						mDegree = 0.5 * Math.PI - 0.01;
					}

				} else {
					mDegree = getDegree();
					mIsFromUser = false;
				}
				mSemiA = mHeight - mROffset - mThumbHeight / 2; // 计算长轴
				mSemiB = (mWidth - mROffset - mThumbWidth / 2) * mScaleX; // 计算短轴
				mXDraw = -(float) (mSemiB * Math.cos(mDegree)) + mSemiB - 20; // 计算X坐标
				mYDraw = -(float) (mSemiA * Math.sin(mDegree)) + mSemiA - 15; // 计算Y坐标

				double offsetDegree = 0;
				mProgress = (mDegree * mMax / (Math.PI / 2 - offsetDegree));

				mProgress = mProgress - 5;
				if (mProgress <= 0) {
					mProgress = 0;
				}
				if (mProgress >= mMax / 2) {
					mProgress = mMax / 2;
				}

			}
			mNewProgress = 2 * mProgress;
			if (marcSeekBarChangeListener != null) {
				marcSeekBarChangeListener.onProgressChanged(this,
						(int) mNewProgress, mIsFromUser);
				marcSeekBarChangeListener.onStartTrackingTouch(this);
				marcSeekBarChangeListener.onStopTrackingTouch(this);
			}
			// System.out.println("mprogress------------------" +
			// mProgress+"mnewprogress:"+mNewProgress);
			if (mYDraw >= 93 || mXDraw < -15) {
				mXDraw = -15;
				mYDraw = 93;
			} else if (mXDraw >= 40 || mYDraw <= 12) {
				mXDraw = 40;
				mYDraw = 12;
			}

			if (mIsTouched) {
				if (Double.isNaN(mProgress)) {
					canvas.drawBitmap(mThumbBmp, -14, mHeight - mThumbHeight
							- 5, mDrawPaint);
				} else {
					canvas.drawBitmap(mThumbBmpHint, mXDraw, mYDraw, mDrawPaint);
				}

			} else {
				if (!mIsTouchable || mProgress == 0 || Double.isNaN(mProgress)) {
					canvas.drawBitmap(mThumbBmp, -14, mHeight - mThumbHeight
							- 5, mDrawPaint);

				} else {
					canvas.drawBitmap(mThumbBmp, mXDraw, mYDraw, mDrawPaint);
				}
			}
			break;
		}

		// 检查Thumb的位置
		Log.w("ArcSeekBar", "Draw");
	}

	protected void init() {
		Log.w(TAG, "[init]初始化控件");
		Bitmap defBackgroud = null, // 默认背景
		defThumb = null, // 默认Thumb
		defThumbHint = null; // 默认ThumbHint
		Canvas canvas = null;
		Paint paint = null;

		// 取Background
		if (mBackgroundResid == 0) {
			defBackgroud = Bitmap.createBitmap(mWidth, mHeight,
					Config.ARGB_8888);
			canvas = new Canvas(defBackgroud);
			paint = new Paint(Paint.ANTI_ALIAS_FLAG);

			paint.setARGB(255, 225, 225, 225);
			paint.setStyle(Style.STROKE);
			paint.setStrokeWidth(5);
			canvas.drawARGB(100, 100, 100, 100);
			switch (mQuad) {
			case POS_1ST_QUAD:
				canvas.drawArc(new RectF(mROffset - mWidth + 10, mROffset + 10,
						mWidth - mROffset - 10, mHeight - mROffset + mHeight
								- 10), ((mQuad + 4 - 1) % 4) * 90, 90, true,
						paint);
				break;

			case POS_2ND_QUAD:
				canvas.drawArc(new RectF(mROffset - mWidth + 10, mROffset
						- mHeight + 10, mWidth - mROffset - 10, mHeight
						- mROffset - 10), ((mQuad + 4 - 1) % 4) * 90, 90, true,
						paint);
				break;

			case POS_3RT_QUAD:
				canvas.drawArc(new RectF(mROffset + 10,
						mROffset - mHeight + 10, mWidth - mROffset + mWidth
								- 10, mHeight - mROffset - 10),
						((mQuad + 4 - 1) % 4) * 90, 90, true, paint);
				break;

			case POS_4ST_QUAD:
				canvas.drawArc(new RectF(mROffset + 10, mROffset + 10, mWidth
						- mROffset + mWidth - 10, mHeight - mROffset + mHeight
						- 10), ((mQuad + 4 - 1) % 4) * 90, 90, true, paint);
				break;
			}
			mBackgroundBmp = defBackgroud;
			mROffset = -5;
		} else {
			mBackgroundBmp = Bitmap.createScaledBitmap(BitmapFactory
					.decodeResource(getResources(), mBackgroundResid), mWidth,
					mHeight, true);
		}

		// 取Thumb
		if (mThumbResid == 0) {
			defThumb = Bitmap.createBitmap(20, 20, Config.ARGB_8888);
			canvas = new Canvas(defThumb);
			paint = new Paint(Paint.ANTI_ALIAS_FLAG);

			paint.setARGB(255, 255, 205, 205);
			paint.setStyle(Style.FILL);
			canvas.drawArc(new RectF(0, 0, 20, 20), 0, 360, true, paint);
			mThumbBmp = defThumb;
		} else {
			mThumbBmp = BitmapFactory.decodeResource(getResources(),
					mThumbResid);
		}

		// 取Thumb Hint
		if (mThumbResidHint == 0) {
			defThumbHint = Bitmap.createBitmap(20, 20, Config.ARGB_8888);
			canvas = new Canvas(defThumbHint);
			paint = new Paint(Paint.ANTI_ALIAS_FLAG);

			paint.setARGB(255, 205, 255, 205);
			paint.setStyle(Style.FILL);
			canvas.drawArc(new RectF(0, 0, 20, 20), 0, 360, true, paint);
			mThumbBmpHint = defThumbHint;
		} else {
			mThumbBmpHint = BitmapFactory.decodeResource(getResources(),
					mThumbResidHint);
		}

		// 初始化状态
		mWidth = mBackgroundBmp.getWidth();
		mHeight = mBackgroundBmp.getHeight();
		mProgress = 0;
		// if (mMax == 0) {
		// mMax = 100;
		// }

		switch (mQuad) {
		case POS_1ST_QUAD:
			mXpos = 0.001f;
			mYpos = mHeight;
			break;

		case POS_2ND_QUAD:
			mXpos = mWidth;
			mYpos = 0.001f;
			break;

		case POS_3RT_QUAD:
			mXpos = 0.001f;
			mYpos = 0.001f;
			break;

		case POS_4ST_QUAD:
			mXpos = mWidth;
			mYpos = 0.001f;
			break;
		}

		// 初始化onDraw使用的工具
		mDrawMatrix = new Matrix();
		mDrawPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	}

	protected void resize() {
		Log.w(TAG, "[resize]重测控件");
		mBackgroundBmp = Bitmap.createScaledBitmap(mBackgroundBmp,
				(int) (mWidth * mScaleX), (int) mHeight, false);
		mThumbBmp = Bitmap.createScaledBitmap(mThumbBmp,
				(int) (mThumbWidth * mScaleX), (int) mThumbHeight, false);
		mThumbBmpHint = Bitmap.createScaledBitmap(mThumbBmpHint,
				(int) (mThumbWidth * mScaleX), (int) mThumbHeight, false);
		switch (mQuad) {
		case POS_1ST_QUAD:
			mXpos = 0.001f;
			mYpos = mHeight;
			break;

		case POS_2ND_QUAD:
			mXpos = mWidth * mScaleX;
			mYpos = 0.001f;
			break;

		case POS_3RT_QUAD:
			mXpos = 0.001f;
			mYpos = 0.001f;
			break;

		case POS_4ST_QUAD:
			mXpos = mWidth * mScaleX;
			mYpos = 0.001f;
			break;
		}
	}

	/**
	 * ArcSeekBar事件监听接口
	 * 
	 * @author WH1107028
	 * 
	 */
	public interface OnArcSeekBarChangeListener {
		public void onStopTrackingTouch(ArcSeekBar seekBar);

		public void onStartTrackingTouch(ArcSeekBar seekBar);

		public void onProgressChanged(ArcSeekBar seekBar, int progress,
				boolean fromUser);
	}

	public void setOnSeekBarChangeListener(OnArcSeekBarChangeListener listener) {
		this.marcSeekBarChangeListener = listener;
	}

	public OnArcSeekBarChangeListener getOnSeekBarChangeListener() {
		return marcSeekBarChangeListener;
	}

	public void setProgress(int progress) {
		mIsFromUser = true;
		mProgress = progress;
		mDegree = (((double) progress / (double) mMax) * Math.PI / 2);
		invalidate();

	}

	private double getDegree() {
		return mDegree;
	}
}

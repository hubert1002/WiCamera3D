package com.wistron.StereoUI;

import android.R.integer;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.BitmapFactory.Options;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.wistron.StereoUI.CSToggleButton.OnChangedListener;
import com.wistron.swpc.wicamera3dii.R;

/**
 * 拨动按钮的普通版
 * @author WH1107028
 *
 */
public class SlideButton extends View  {

	private static final String NAMESPACE = "http://www.pro.dev.com/common";
	private boolean mNowChoose  = false;//记录当前按钮是否打开,true为打开,flase为关闭
	private boolean mOnSlip     = false;//记录用户是否在滑动的变量
	private float   mDownX      = 0,
			        mNowX       = 0;    //按下时的x,当前的x,
	private Rect    mBtnOn      = null,
			        mBtnOff     = null; //打开和关闭状态下,游标的Rect
	
	private float   mSlipBtnX   = 0;

	private boolean mIsChangeListenerOn = false;
	private OnCheckedChangedListener mOnChangeListener;

	private boolean mIsPressed = false;
	private boolean mIsEnable  = true;
	
	private Bitmap mBgOn                = null,
			       mBgOff               = null,
			       mSlipOn              = null,
			       mSlipOff             = null,
			       mSlipOnHint          = null,
			       mSlipOffHint         = null,
			       mSlipButton          = null,
			
			       mBgOnHalf            = null,
			       mBgOffHalf           = null,
			       mSlipButtonHalf      = null,
			
			       mBgOnOriginal        = null,
			       mBgOffOriginal       = null,
			       mSlipOnOriginal      = null,
			       mSlipOffOriginal     = null,
			       mSlipButtonOriginal  = null,
			       mSlipOnOriginalHint  = null,
			       mSlipOffOriginalHint = null;

	private int    mIdBgOn              = R.drawable.btn_3d2d_background,
				   mIdBgOff             = R.drawable.btn_3d2d_background,
				   mIdBtnOn             = R.drawable.btn_3d2d_2d_normal,
				   mIdBtnOff            = R.drawable.btn_3d2d_3d_normal,
				   mIdBtnOnHint         = R.drawable.btn_3d2d_2d_pressed,
				   mIdBtnOffHint        = R.drawable.btn_3d2d_3d_pressed;
	
	private int    mBgOnWidth           = 0,
			       mBgOnHeight          = 0,
			       mSlipOnWidth         = 0,
			       mSlipOnHeight        = 0;
			    
	
	private int    mLayout_width        = 0,
			       mLayout_height       = 0;
	private AttributeSet   mAttrs       = null;
	private boolean        mHasMeasured = false;

	public SlideButton(Context context) {
		super(context);
		init(null);
	}

	public SlideButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlideButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs);
		mAttrs = attrs;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if(!mHasMeasured){
			init(mAttrs);
			mHasMeasured = true;
		}
	}

	private void init(AttributeSet attrs){//初始化
		//载入图片资源
		Options opt = new Options();
		opt.inJustDecodeBounds = false;
		
		mBgOnOriginal        = BitmapFactory.decodeResource(getResources(), mIdBgOn, opt);
		mBgOffOriginal       = BitmapFactory.decodeResource(getResources(), mIdBgOff, opt);
		mSlipOnOriginal      = BitmapFactory.decodeResource(getResources(), mIdBtnOn, opt);
		mSlipOffOriginal     = BitmapFactory.decodeResource(getResources(), mIdBtnOff, opt);
		mSlipOnOriginalHint  = BitmapFactory.decodeResource(getResources(), mIdBtnOnHint, opt);
		mSlipOffOriginalHint = BitmapFactory.decodeResource(getResources(), mIdBtnOffHint, opt);

		if(attrs != null){
//			mLayout_width  = attrs.getAttributeIntValue(NAMESPACE, "layout_width", LayoutParams.WRAP_CONTENT);
//			mLayout_height = attrs.getAttributeIntValue(NAMESPACE, "layout_height", LayoutParams.WRAP_CONTENT);
			mLayout_width  = getWidth();
			mLayout_height = getHeight();
			
			if(mLayout_width == 0 && mLayout_height == 0){ //设定最佳默认值
				mLayout_width  = 133;
				mLayout_height = 78;
			}
			
			if(mLayout_width > 0 && mLayout_height > 0){
				mBgOn = Bitmap.createScaledBitmap(mBgOnOriginal, mLayout_width, mLayout_height, true);
				mBgOff = Bitmap.createScaledBitmap(mBgOffOriginal, mLayout_width, mLayout_height, true);
				mSlipOn = mSlipOnOriginal;
				mSlipOff = mSlipOffOriginal;
				mSlipOnHint = mSlipOnOriginalHint;
				mSlipOffHint = mSlipOffOriginalHint;
				mSlipOn = Bitmap.createScaledBitmap(mSlipOnOriginal, mLayout_height - 2, mLayout_height - 2, true);
				mSlipOff = Bitmap.createScaledBitmap(mSlipOffOriginal, mLayout_height - 2, mLayout_height - 2, true);
				mSlipOnHint = Bitmap.createScaledBitmap(mSlipOnOriginalHint, mLayout_height - 2, mLayout_height - 2, true);
				mSlipOffHint = Bitmap.createScaledBitmap(mSlipOffOriginalHint, mLayout_height - 2, mLayout_height - 2, true);
				mSlipButton = mSlipOn;
			}else{
				mBgOn = mBgOnOriginal;
				mBgOff = mBgOffOriginal;
				mSlipOn = mSlipOnOriginal;
				mSlipOff = mSlipOffOriginal;
				mSlipOnHint = mSlipOnOriginalHint;
				mSlipOffHint = mSlipOffOriginalHint;
				mSlipButton = mSlipOn;
			}
		}else{
			mBgOn = mBgOnOriginal;
			mBgOff = mBgOffOriginal;
			mSlipOn = mSlipOnOriginal;
			mSlipOff = mSlipOffOriginal;
			mSlipOnHint = mSlipOnOriginalHint;
			mSlipOffHint = mSlipOffOriginalHint;
			mSlipButton = mSlipOn;
		}

		//获得需要的Rect数据
		mBtnOn  = new Rect(0,0,mSlipButton.getWidth(),mSlipButton.getHeight());
		mBtnOff = new Rect(
				mBgOff.getWidth() - mSlipButton.getWidth(),
				0,
				mBgOff.getWidth(),
				mSlipButton.getHeight());

		this.setPivotX(mBtnOn.width()/2);
		this.setPivotY(mBtnOn.height()/2);
		
		mSlipOnWidth = mSlipOnOriginal.getWidth();
		mSlipOnHeight = mSlipOnOriginal.getHeight();
	}

	public void setScaleThumbX(float scale, float degree){
		
	}
	
	public void setBackground(int resid){
		mIdBgOff  = mIdBgOn = resid;
//		mBgOff    = mBgOn   = BitmapFactory.decodeResource(getResources(), resid);
		mBgOffOriginal    = mBgOnOriginal   = BitmapFactory.decodeResource(getResources(), resid);
	}

	public void setOnImage(int resid){
		mIdBtnOn  = resid;
		mSlipOnOriginal   = BitmapFactory.decodeResource(getResources(), resid);
		init(mAttrs);
	}

	public void setOffImage(int resid){
		mIdBtnOff = resid;
		mSlipOffOriginal  = BitmapFactory.decodeResource(getResources(), resid);
		init(mAttrs);
	}

	public void setPivot(float pivotX, float pivotY){
		this.setPivotX(pivotX);
		this.setPivotY(pivotY);
	}

	/* ToggleButton.onDraw()
    @Override 
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final Drawable buttonDrawable = mButtonDrawable;
        if (buttonDrawable != null) {
            final int verticalGravity = getGravity() & Gravity.VERTICAL_GRAVITY_MASK;
            final int height = buttonDrawable.getIntrinsicHeight();

            int y = 0;

            switch (verticalGravity) {
                case Gravity.BOTTOM:
                    y = getHeight() - height;
                    break;
                case Gravity.CENTER_VERTICAL:
                    y = (getHeight() - height) / 2;
                    break;
            }

            buttonDrawable.setBounds(0, y, buttonDrawable.getIntrinsicWidth(), y + height);
            buttonDrawable.draw(canvas);
        }
    }
    */
	
	@Override
	protected void onDraw(Canvas canvas) {//绘图函数
		
		Log.e("TEST", "onDraw----------------------------------------");
		mBtnOn  = new Rect(0,0,mSlipButton.getWidth(),mSlipButton.getHeight());
		mBtnOff = new Rect(
				mBgOff.getWidth() - mSlipButton.getWidth(),
				0,
				mBgOff.getWidth(),
				mSlipButton.getHeight());

		super.onDraw(canvas);

		Matrix matrix = new Matrix();
		Paint  paint  = new Paint();
		float  x      = 0f;

		paint.setAntiAlias(true);

		{
			//滑动到前半段与后半段的背景不同
			if(mNowX < (mBgOn.getWidth()/2)){
				canvas.drawBitmap(mBgOff, matrix, paint); //画出关闭时的背景
			}
			else{
				canvas.drawBitmap(mBgOn, matrix, paint);  //画出打开时的背景
			}

		
			
			//定位游标位置
			if(mOnSlip){//正在滑动
				if(mNowX >= mBgOn.getWidth() - mSlipButton.getWidth()/2){//是否划出指定范围
					x = mBgOn.getWidth() - mSlipButton.getWidth(); 
					Log.w("SlipButton_-2", "x = bg.w - sb.w = " + mBgOn.getWidth() + " - " + mSlipButton.getWidth() + " = " + x);
				}
				else{
					x = mNowX - mSlipButton.getWidth()/2;
					Log.w("SlipButton_-1", "x = now.x - sb.w/2 = " + mNowX + " - " + mSlipButton.getWidth()/2 + " = " + x);
				}
			}else{//未滑动
				if(mNowChoose)//根据现在的开关状态设置画游标的位置
					x = mBtnOff.left;
				else
					x = mBtnOn.left;
			}

			//对游标位置进行异常判断...
			if(x < 0){
				x = 0;
			}
			else if(x > mBgOn.getWidth() - mSlipButton.getWidth()){
				x = mBgOn.getWidth() - mSlipButton.getWidth();
				Log.w("SlipButton_1", "x = bg.w - sb.w = " + mBgOn.getWidth() + " - " + mSlipButton.getWidth() + " = " + x);
			}


			//更换游标图片
			if(x < mBgOn.getWidth()/2 - mSlipButton.getWidth()/2){
				if(mIsPressed){
					mSlipButton = mSlipOnHint;
				}else{
					mSlipButton = mSlipOn;
				}
			}else{
				if(mIsPressed){
					mSlipButton = mSlipOffHint;
				}else{
					mSlipButton = mSlipOff;
				}
			}

			canvas.drawBitmap(mSlipButton, x, 0, paint);//画出游标.
		}
	}


	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		mIsEnable = enabled;
	}

	@Override
	public boolean onTouchEvent( MotionEvent event) {
		boolean state = false;

		int [] location = new int[2];
		
		if(!mIsEnable){
			return true;
		}

		getLocationOnScreen(location);

		Log.e("event.getx()="+event.getX(), "mbnon.getwidth()/2="+mBgOn.getWidth()/2+"event.gety()="+event.getY()+"location[0]="+location[0]+"location[1]="+location[1]);
		switch(event.getAction()){
		case MotionEvent.ACTION_MOVE:
			mNowX = event.getX();
			state = true;
			mIsPressed = true;
			break;
		case MotionEvent.ACTION_DOWN:
			if(event.getX() > mBgOn.getWidth() || event.getY() >mBgOn.getHeight()){
				return false;
			}else {
				mOnSlip = true;
				mDownX  = event.getX();
				mNowX   = mDownX;
				state = true;
			}
			
			break;
		case MotionEvent.ACTION_UP:
			mOnSlip = false;
			mIsPressed = false;
			boolean LastChoose = mNowChoose;
			if(event.getX() >= (mBgOn.getWidth() / 2)){
				mNowChoose = true;
			}
			else{
				mNowChoose = false;
			}
			Log.e("up", "mIsChangeListenerOn:"+mIsChangeListenerOn+"LastChoose:"+LastChoose+"mNowChoose"+mNowChoose);
			
			if(mIsChangeListenerOn && (LastChoose != mNowChoose)){
				mOnChangeListener.OnChecked(mNowChoose);
			}
			state = true;
			break;
		default:
			break;
		}

		invalidate();//重画控件

		return state;
	}

	public void setOnChangedListener(OnCheckedChangedListener listener){//设置监听器,当状态修改的时候
		mIsChangeListenerOn = true;
		mOnChangeListener   = listener;
	}
	
	public void setBackgroundResid(int resid){
		mIdBgOn  = resid;
		mIdBgOff = resid;
		init(mAttrs);
	}
	
	public void setButtonImageResid(int residOn, int residOnHit, int residOff, int residOffHit){
		mIdBtnOn      = residOn;
		mIdBgOff      = residOff;
		mIdBtnOnHint  = residOnHit;
		mIdBtnOffHint = residOffHit;
		init(mAttrs);
	}
	
	public void setButtonSize(int width,int height){
		mBgOnWidth = width;
		mBgOnHeight = height;

		mBgOn = zoomBitmap(mBgOnOriginal, width, height);
		mBgOff = zoomBitmap(mBgOffOriginal, width, height);
		invalidate();
	}
	
	public void setSlipSize(int width,int height){
		
		mSlipOnWidth = width;
		mSlipOnHeight = height;

		mSlipOn = zoomBitmap(mSlipOnOriginal, width, height);
		mSlipOff = zoomBitmap(mSlipOffOriginal, width, height);
		invalidate();
	}
	
	
	public void setHalfScale(){
		//保存压缩后的图片
		Matrix matrix = new Matrix();
		matrix.setScale(0.5f, 1f);
		mSlipButtonHalf = Bitmap.createBitmap(mSlipButton, 0, 0, mSlipButton.getWidth(), mSlipButton.getHeight(), matrix, true);
	}
	
	public void setSlipScaleAndRotation(float sx,float sy,float rotation){
		
		invalidate();
	}
	
	public void setButtonScale(float sx,float sy){
		Matrix matrix = new Matrix();
		matrix.setScale(sx, sy);
		mBgOn = Bitmap.createBitmap(mBgOn, 0, 0, mBgOn.getWidth(), mBgOn.getHeight(), matrix, true);
		mBgOff = Bitmap.createBitmap(mBgOff, 0, 0, mBgOff.getWidth(), mBgOff.getHeight(), matrix, true);
		invalidate();
		
	}
	
	public void setChecked(boolean isChecked){
		if(isChecked != mNowChoose){
			mNowChoose = isChecked;
			invalidate();
		}
	}
	
	public void setCheckedWithCallback(boolean isChecked){
		mNowChoose = isChecked;
		invalidate();
		mOnChangeListener.OnChecked(mNowChoose);
	}
	
	public boolean getChecked(){
		return mNowChoose;
	}
	
	public int getSlipWidth(){
		if(mSlipOn != null && mSlipOff != null){
			return mSlipOn.getWidth();
		}else {
			return -1;
		}

	}
	
	public int getSlipHeight(){
		if(mSlipOn != null && mSlipOff != null){
			return mSlipOn.getHeight();
		}else {
			return -1;
		}
	}
	
	//放大缩小图片
	public  Bitmap zoomBitmap(Bitmap bitmap,int w,int h){
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidht = ((float)w / width);
		float scaleHeight = ((float)h / height);
		matrix.postScale(scaleWidht, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		return newbmp;
	}
	
	/**
	 * 事件回调接口
	 * @author WH1107028
	 *
	 */
	public interface OnCheckedChangedListener {
		abstract void OnChecked(boolean isChecked);
	}
}

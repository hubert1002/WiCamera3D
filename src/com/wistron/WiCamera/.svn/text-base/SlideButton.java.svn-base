package com.wistron.WiCamera;

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
import android.widget.ImageView;
import com.wistron.WiViewer.TDStaticData;
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
			       mBgOnOriginal        = null,
			       mBgOffOriginal       = null,
			       mSlipOnOriginal      = null,
			       mSlipOffOriginal     = null,
			       mSlipOnOriginalHint  = null,
			       mSlipOffOriginalHint = null;


	private int    mIdBgOn              = 	R.drawable.slide_btn_bg,
			   mIdBgOff             = R.drawable.slide_btn_bg,
			   mIdBtnOn             = R.drawable.record_btn_select_normal_psd_rotate,
			   mIdBtnOff            = R.drawable.switch_camera_select_normal_psd_rotate,
			   mIdBtnOnHint         = R.drawable.record_btn_select_click_psd_rotate,
			   mIdBtnOffHint        = R.drawable.switch_camera_select_click_psd_rotate;
	
	
	private int    mBgOnWidth           = 0,
			       mBgOnHeight          = 0,
			       mSlipOnWidth         = 0,
			       mSlipOnHeight        = 0;
			    
	
	private int    mOffset              = 0;
	private float  mDegree              = 0;
			  
	private AttributeSet   mAttrs       = null;
	private ImageView mImageView[]      = null;
	private Context mContext            = null;
	private int mScreenWidth = 0,mScreenHeight = 0;
	private float mDensity = 0;//屏幕密度

	public SlideButton(Context context) {
		super(context);
		mContext = context;
	
		init(null);
	}

	public SlideButton(Context context, AttributeSet attrs) {
		
		this(context, attrs, 0);
		mContext = context;
	}

	public SlideButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init(attrs);
		mAttrs = attrs;

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		init(mAttrs);
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

//		if(attrs != null){
////			mLayout_width  = attrs.getAttributeIntValue(NAMESPACE, "layout_width", LayoutParams.WRAP_CONTENT);
////			mLayout_height = attrs.getAttributeIntValue(NAMESPACE, "layout_height", LayoutParams.WRAP_CONTENT);
//			mLayout_width  = getWidth();
//			mLayout_height = getHeight();
//			
//			if(mLayout_width > 0 && mLayout_height > 0){
//				mBgOn = Bitmap.createScaledBitmap(mBgOnOriginal, mLayout_width, mLayout_height, true);
//				mBgOff = Bitmap.createScaledBitmap(mBgOffOriginal, mLayout_width, mLayout_height, true);
//				mSlipOn = mSlipOnOriginal;
//				mSlipOff = mSlipOffOriginal;
//				mSlipOnHint = mSlipOnOriginalHint;
//				mSlipOffHint = mSlipOffOriginalHint;
////				mSlipOn = Bitmap.createScaledBitmap(mSlipOnOriginal, mLayout_height - 2, mLayout_height - 2, true);
////				mSlipOff = Bitmap.createScaledBitmap(mSlipOffOriginal, mLayout_height - 2, mLayout_height - 2, true);
////				mSlipOnHint = Bitmap.createScaledBitmap(mSlipOnOriginalHint, mLayout_height - 2, mLayout_height - 2, true);
////				mSlipOffHint = Bitmap.createScaledBitmap(mSlipOffOriginalHint, mLayout_height - 2, mLayout_height - 2, true);
//				mSlipButton = mSlipOn;
//			}else{
//				mBgOn = mBgOnOriginal;
//				mBgOff = mBgOffOriginal;
//				mSlipOn = mSlipOnOriginal;
//				mSlipOff = mSlipOffOriginal;
//				mSlipOnHint = mSlipOnOriginalHint;
//				mSlipOffHint = mSlipOffOriginalHint;
//				mSlipButton = mSlipOn;
//			}
//		}else{
		
		mScreenWidth = TDStaticData.SCREEN_WIDTH;
		mScreenHeight = TDStaticData.SCREEN_HEIGHT;
//		Log.v("SlideButton", "TDStaticData.SCREEN_WIDTH"+TDStaticData.SCREEN_WIDTH+"TDStaticData.SCREEN_HEIGHT"+TDStaticData.SCREEN_HEIGHT);
		if (WiCameraActivity.mCurrentDegree == 90  || WiCameraActivity.mCurrentDegree == 270) {
			mScreenHeight = TDStaticData.SCREEN_WIDTH;
		}else {
			mScreenHeight = TDStaticData.SCREEN_HEIGHT;
		}
		
//		Log.e("SlideButton", ""+mDensity);
	
		mDensity = WiCameraActivity.mDensity;
		
		
//		float scaleY = (float)mScreenHeight/480f * mDensity;
		float scaleY = mDensity;
		mBgOn = mBgOff = 	zoomBitmap(mBgOnOriginal,(float)(mBgOnOriginal.getWidth())/scaleY,(float)(mBgOnOriginal.getHeight())/scaleY);
		mSlipOn = zoomBitmap(mSlipOnOriginal,(float)(mSlipOnOriginal.getWidth())/scaleY,(float)(mSlipOnOriginal.getHeight())/scaleY);
		mSlipOff = zoomBitmap(mSlipOffOriginal,(float)(mSlipOffOriginal.getWidth())/scaleY,(float)(mSlipOffOriginal.getHeight())/scaleY);
		
		mSlipOnHint = zoomBitmap(mSlipOnOriginalHint,(float)(mSlipOnOriginalHint.getWidth())/scaleY,(float)(mSlipOnOriginalHint.getHeight())/scaleY);
		mSlipOffHint = zoomBitmap(mSlipOffOriginalHint,(float)(mSlipOffOriginalHint.getWidth())/scaleY,(float)(mSlipOffOriginalHint.getHeight())/scaleY);
			
//			mBgOn = mBgOnOriginal;
//			mBgOff = mBgOffOriginal;
//			mSlipOn = mSlipOnOriginal;
//			mSlipOff = mSlipOffOriginal;
//			mSlipOnHint = mSlipOnOriginalHint;
//			mSlipOffHint = mSlipOffOriginalHint;
			mSlipButton = mSlipOn;
//		}

			
			mSlipOnWidth = mSlipOnOriginal.getWidth();
			mSlipOnHeight = mSlipOnOriginal.getHeight();
			
			
		//获得需要的Rect数据
		mBtnOn  = new Rect(0,0,mSlipButton.getWidth(),mSlipButton.getHeight());
		mBtnOff = new Rect(
				mBgOff.getWidth() - mSlipButton.getWidth(),
				0,
				mBgOff.getWidth(),
				mSlipButton.getHeight());

		this.setPivotX(mBgOn.getWidth()/2);
		this.setPivotY(mBgOn.getHeight()/2);
		
	
		
		mImageView = new ImageView[2];
		mImageView[0] = new ImageView(mContext);
		mImageView[1] = new ImageView(mContext);
		
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

	
	@Override
	protected void onDraw(Canvas canvas) {//绘图函数
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
					x = mBgOn.getWidth() - mSlipButton.getWidth() + mOffset ; 
					Log.w("SlipButton_-2", "x = bg.w - sb.w = " + mBgOn.getWidth() + " - " + mSlipButton.getWidth() + " = " + x);
				}
				else{
					x = mNowX - mSlipButton.getWidth()/2 ;
					Log.w("SlipButton_-1", "x = now.x - sb.w/2 = " + mNowX + " - " + mSlipButton.getWidth()/2 + " = " + x);
				}
			}else{//未滑动
				if(mNowChoose)//根据现在的开关状态设置画游标的位置
					x = mBtnOff.left + mOffset ;
				else
					x = mBtnOn.left ;
			}

			//对游标位置进行异常判断...
			if(x < 0){
				x = 0;
			}
			else if(x > mBgOn.getWidth() - mSlipButton.getWidth()/2 ){
				x = mBgOn.getWidth() - mSlipButton.getWidth() + mOffset ;
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

			Matrix matrix2 = new Matrix();
			matrix2.setRotate(mDegree);
			mSlipButton = Bitmap.createBitmap(mSlipButton, 0, 0, mSlipButton.getWidth(), mSlipButton.getHeight(), matrix2, true);
			canvas.drawBitmap(mSlipButton, x, 4, paint);//画出游标.
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

//		Log.e("event.getx()="+event.getX(), "mbnon.getwidth()/2="+mBgOn.getWidth()/2+"event.gety()="+event.getY()+"location[0]="+location[0]+"location[1]="+location[1]);
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
//			Log.e("up", "mIsChangeListenerOn:"+mIsChangeListenerOn+"LastChoose:"+LastChoose+"mNowChoose"+mNowChoose);
			
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
	
	public void setBackgroundResid(int resid,int residOn,int residOff, int residOnHit,  int residOffHit){
		mIdBgOn  = resid;
		mIdBgOff = resid;
		mIdBtnOn      = residOn;
		mIdBtnOff      = residOff;
		mIdBtnOnHint  = residOnHit;
		mIdBtnOffHint = residOffHit;
		init(mAttrs);
	}
	
	public void setButtonImageResid(int residOff,int residOn,int residoff, int residOnHit,  int residOffHit){
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
	
	
	public void setChecked(boolean isChecked){
		if(isChecked != mNowChoose){
			mNowChoose = isChecked;
			invalidate();
		}
	}
	
	public void setCheckedWithCallback(boolean isChecked){
		if(isChecked != mNowChoose){
			mNowChoose = isChecked;
			invalidate();
			mOnChangeListener.OnChecked(mNowChoose);  //小心！！！！！！！！！！！！！！！！！！！
		}
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
	public  Bitmap zoomBitmap(Bitmap bitmap,float w,float h){
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidht = ((float)w / width);
		float scaleHeight = ((float)h / height);
		matrix.postScale(scaleWidht, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,
				true);
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
	
	@Override
	public void setRotation(float rotation) {
		// TODO Auto-generated method stub
		super.setRotation(rotation);
		
		
	}
	
	public void setButtonRotation(float rotation){
		  mDegree = rotation;
		  invalidate();
			
	}
	public static int pxTodip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;

		return (int) (pxValue * scale + 0.5f);
	}
}

package com.wistron.StereoUI;

import android.R;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * @author WH1107028
 * @design 总宽度不能超过屏幕的3/5，总高度不能超过屏幕的3/5，越界的部分，使用TextView的Scroll解决
 */
@SuppressWarnings("deprecation")
public class CSMsgBox extends CSView{
	private static final String TAG               = "CSMsgBox";
	public final static int ANCHOR_CENTER         =  0x00; //0000
	public final static int ANCHOR_TOP_LEFT       =  0x01; //0001
	public final static int ANCHOR_TOP_RIGHT      =  0x02; //0010
	public final static int ANCHOR_BUTTOM_LEFT    =  0x04; //0100
	public final static int ANCHOR_BUTTOM_RIGHT   =  0x08; //1000
	
	private LinearLayout     mLayoutL             = null; //承载整个组合控件的结构，在这个layout内部自适应，即可解决误差问题
	private LinearLayout     mLayoutR             = null; //承载整个组合控件的结构，在这个layout内部自适应，即可解决误差问题
	private CSButton         mCsbtnPositive       = null; //btnOK/YES/Confirm
	private CSButton         mCsbtnNegative       = null; //btnNo/Close/Cancel
	private CSButton         mCsbtnNeutral        = null; //btnAbort/Ignore
	
	private int              mAnchor              = ANCHOR_CENTER;
	private boolean          mHasPositive         = false; //存在确认键？
	private boolean          mHasNegative         = true;  //存在忽略键？
	private boolean          mHasNeutral          = false; //存在取消键？
	private boolean          mHasHeader           = false; //存在标题栏？
	private boolean          mHasBody             = false; //存在内容栏？
	private boolean          mHasFooter           = true;  //存在按钮栏？
	private boolean          mHasIcon             = false; //存在图标？
	private boolean          mIsModal             = true;  //是否为模式对话框
	
	private int              mActWidth            = 0,     //当前使用的宽度
	                         mActHeight           = 0,     //当前使用的高度
							 mBoxWidth            = LayoutParams.WRAP_CONTENT,
			                 mBoxHeight           = LayoutParams.WRAP_CONTENT,
			                 mHeaderWidth         = LayoutParams.MATCH_PARENT,
			                 mHeaderHeight        = LayoutParams.MATCH_PARENT,
			                 mBodyWidth           = LayoutParams.MATCH_PARENT,
			                 mBodyHeight          = LayoutParams.MATCH_PARENT,
			                 mFooterWidth         = LayoutParams.MATCH_PARENT,
			                 mFooterHeight        = LayoutParams.MATCH_PARENT;
	
	private int              mIconLresid          = 0;
	private int              mIconRresid          = 0;
	private int              mHeaderBgL           = 0;
	private int              mHeaderBgR           = 0;
	private int              mBodyBgL             = 0;
	private int              mBodyBgR             = 0;
	private int              mFooterBgL           = 0;
	private int              mFooterBgR           = 0;
	private String           mTitle               = "";
	private String           mMessage             = "";
	
	private LinearLayout     mHeaderL             = null,    mHeaderR  = null,
			 				 mBodyL               = null,    mBodyR    = null,
			 				 mFooterL             = null,    mFooterR  = null;
	private int              mConWidth            = LayoutParams.WRAP_CONTENT,
		     				 mConHeight           = LayoutParams.WRAP_CONTENT;
	private TextView         mTitleL              = null,    mTitleR   = null,
		     				 mContentL            = null,    mContentR = null;
	private ImageView        mIconL               = null,    mIconR    = null;
	private CSTextView       mCsTitle             = null,    mCsContent = null;
	private CSImageView      mCsIcon              = null;
	
	public CSMsgBox(Context context) {
		super(context);
		m_context = context;
		initLayout();
	}

	/**
	 * 设置定位点
	 * @param anchor TOP_LEFT | TOP_RIGHT | BUTTOM_LEFT | BUTTOM_RIGHT | CENTER
	 */
	public void setAnchor(int anchor){
		mAnchor = anchor;
	}

	@Override
	public void setVisibility(int visibility) {
		if(mLayoutL == null || mLayoutR == null){
			return;
		}
		
		mLayoutL.setVisibility(visibility);
		mLayoutR.setVisibility(visibility);
	}

	@Override
	public void setEnable(boolean enabled) {
		if(mLayoutL == null || mLayoutR == null){
			return;
		}
		
		mLayoutL.setEnabled(enabled);
		mLayoutR.setEnabled(enabled);
	}
	
	public void setModality(boolean modal){
		mIsModal = modal;
	}

	@Override
	public void setDimension(boolean is3d) {
		super.setDimension(is3d);
		int curWidth  = m_org_width,
		curHeight = mLayoutL.getHeight();
		
		
		if(m_is_3D){
//			if(mCsTitle != null){
//				mCsTitle.setTextScaleX(0.5f);
//			}
//			if(mCsContent != null ){
//				mCsContent.setTextScaleX(0.5f);
//			}
//			if(mCsIcon != null ){
//				Matrix max = new Matrix();
//				max.setScale(0.5f, 0.5f);
//				mIconL.setImageMatrix(max);
//				mIconR.setImageMatrix(max);
//				mIconL.setMaxWidth(15);
//				mIconR.setMaxWidth(15);
//			}
			
		
			
			if(mTitleL != null && mTitleR != null){
				mTitleL.setTextScaleX(0.5f);
				mTitleR.setTextScaleX(0.5f);
			}
			if(mContentL != null && mContentR != null){
				mContentL.setTextScaleX(0.5f);
				mContentR.setTextScaleX(0.5f);
			}
			if(mIconL != null && mIconR != null){
//				Matrix max = new Matrix();
//				max.setScale(0.5f, 1.0f);
//				mIconL.setImageMatrix(max);
//				mIconR.setImageMatrix(max);
				mIconL.setLayoutParams(new LinearLayout.LayoutParams(20, 40));
				mIconR.setLayoutParams(new LinearLayout.LayoutParams(20, 40));
			}
			if(mBoxWidth != WRAP_CONTENT && mBoxWidth != MATCH_PARENT){
					curWidth = m_org_width/2;
					curWidth = (int)(curWidth*((float)m_screen_width/1024f));
			}
			
			mCsbtnPositive.setTextScaleX(0.5f);
			mCsbtnNeutral.setTextScaleX(0.5f);
			mCsbtnNegative.setTextScaleX(0.5f);
		}else{
//			if(mCsTitle != null ){
//				mCsTitle.setTextScaleX(1f);
////				mTitleR.setTextScaleX(1f);
//			}
//			if(mCsContent != null ){
//				mCsContent.setTextScaleX(1f);
//			}
			
			if(mTitleL != null && mTitleR != null){
				mTitleL.setTextScaleX(1f);
				mTitleR.setTextScaleX(1f);
			}
			if(mContentL != null && mContentR != null){
				mContentL.setTextScaleX(1f);
				mContentR.setTextScaleX(1f);
			}
			if(mIconL != null && mIconR != null){
//				Matrix max = new Matrix();
//				max.setScale(1f, 1f);
//				mIconL.setImageMatrix(max);
//				mIconR.setImageMatrix(max);
				mIconL.setLayoutParams(new LinearLayout.LayoutParams(40, 40));
				mIconR.setLayoutParams(new LinearLayout.LayoutParams(40, 40));
			}

			if(mBoxWidth != WRAP_CONTENT && mBoxWidth != MATCH_PARENT){
					curWidth = m_org_width;
					curWidth = (int)(curWidth*((float)m_screen_width/1024f));
			}
			
			mCsbtnPositive.setTextScaleX(1f);
			mCsbtnNeutral.setTextScaleX(1f);
			mCsbtnNegative.setTextScaleX(1f);
		}
		mCsbtnPositive.computeDimension(is3d);
		mCsbtnNeutral.computeDimension(is3d);
		mCsbtnNegative.computeDimension(is3d);
		mCsbtnPositive.setLayoutParams(new LinearLayout.LayoutParams(mCsbtnPositive.m_cur_width, mCsbtnPositive.m_cur_height), new LinearLayout.LayoutParams(mCsbtnPositive.m_cur_width, mCsbtnPositive.m_cur_height));
		mCsbtnNeutral.setLayoutParams(new LinearLayout.LayoutParams(mCsbtnPositive.m_cur_width, mCsbtnPositive.m_cur_height), new LinearLayout.LayoutParams(mCsbtnPositive.m_cur_width, mCsbtnPositive.m_cur_height));
		mCsbtnNegative.setLayoutParams(new LinearLayout.LayoutParams(mCsbtnPositive.m_cur_width, mCsbtnPositive.m_cur_height), new LinearLayout.LayoutParams(mCsbtnPositive.m_cur_width, mCsbtnPositive.m_cur_height));
		
		Log.w(TAG, "[pre-pre-reloc]L w:h:x:y = " + curWidth + ", " + curHeight + ", " + m_cur_lx + ", " + m_cur_ly+"m_org_width"+m_org_width);
		Log.w(TAG, "[pre-pre-reloc]R w:h:x:y = " + curWidth + ", " + curHeight + ", " + m_cur_rx + ", " + m_cur_ry);
		Log.w(TAG, "[pre-reloc]L w:h:x:y = " + mLayoutL.getWidth() + ", " + mLayoutL.getHeight() + ", " + mLayoutL.getX() + ", " + mLayoutL.getY());
		Log.w(TAG, "[pre-reloc]R w:h:x:y = " + mLayoutR.getWidth() + ", " + mLayoutR.getHeight() + ", " + mLayoutR.getX() + ", " + mLayoutR.getY());		
		relocate(curWidth, curHeight);
	}
	
	@Override
	public void setSize(int width, int height) {
		m_org_width  = width;
		m_org_height = height;
		super.setSize(width, height);
		
		//屏幕自适应换算,只需要计算宽度
		if(width != LayoutParams.MATCH_PARENT || width != LayoutParams.WRAP_CONTENT)
			mBoxWidth  = (int)(width*((float)m_screen_width/1024f));

		//2D-3D换算
		if(m_is_3D){
			if(width != LayoutParams.MATCH_PARENT || width != LayoutParams.WRAP_CONTENT){
				mBoxWidth  = (int)(width*((float)m_screen_width/1024f)/2f);
			}else{
				
			}
		}
	}
	
	public void setButtonBackground(int resid){
		setButtonBackground(resid, resid);
	}
	
	public void setButtonBackground(int residL, int residR){
		if(mCsbtnNegative != null){
			mCsbtnNegative.setBackground(residL, residR);
		}
		if(mCsbtnNeutral != null){
			mCsbtnNeutral.setBackground(residL, residR);
		}
		if(mCsbtnPositive != null){
			mCsbtnPositive.setBackground(residL, residR);
		}
		
		mLayoutL.requestLayout();
		mLayoutR.requestLayout();
	}
	
	public void setButtonSize(int width, int height){
		if(mCsbtnNegative != null){
			mCsbtnNegative.setSize(width, height);
		}
		if(mCsbtnNeutral != null){
			mCsbtnNeutral.setSize(width, height);
		}
		if(mCsbtnPositive != null){
			mCsbtnPositive.setSize(width, height);
		}
		
		mLayoutL.requestLayout();
		mLayoutR.requestLayout();
	}

	public void setHeaderSize(int width, int height){
		mHasHeader    = true;
		mHeaderWidth  = width;
		mHeaderHeight = height;
		//重置MsgBox的宽度
		if(mBoxWidth >= 0 && mBoxWidth < mHeaderWidth){
			mBoxWidth = width;
		}
		//高度不必匹配，设定为WRAP_CONTENT就OK
		//最终的宽度已Header、Body、Footer、Box中的最大值为准
	}
	
	public void setHeaderBackground(int resid){
		mHasHeader = true;
		setHeaderBackground(resid, resid);
	}
	
	public void setHeaderBackground(int residL, int residR){
		mHasHeader = true;
		mHeaderBgL = residL;
		mHeaderBgR = residR;
	}
	
	public void setBodySize(int width, int height){
		mHasBody    = true;
		mBodyWidth  = width;
		mBodyHeight = height;
		//重置MsgBox的宽度
		if(mBoxWidth >= 0 && mBoxWidth < mBodyWidth){
			mBoxWidth = width;
		}
		//高度不必匹配，设定为WRAP_CONTENT就OK
		//最终的宽度已Header、Body、Footer、Box中的最大值为准
	}
		
	public void setBodyBackground(int resid){
		mHasBody   = true;
		setBodyBackground(resid, resid);
	}
	
	public void setBodyBackground(int residL, int residR){
		mHasBody   = true;
		mBodyBgL   = residL;
		mBodyBgR   = residR;
	}
	
	public void setFooterSize(int width, int height){
		mHasFooter    = true;
		mFooterWidth  = width;
		mFooterHeight = height;
		//重置MsgBox的宽度
		if(mBoxWidth >= 0 && mBoxWidth < mFooterWidth){
			mBoxWidth = width;
		}
		//高度不必匹配，设定为WRAP_CONTENT就OK
		//最终的宽度已Header、Body、Footer、Box中的最大值为准
	}
	
	public void setFooterBackground(int resid){
		mHasFooter = true;
		setFooterBackground(resid, resid);
	}
	
	public void setFooterBackground(int residL, int residR){
		mHasFooter = true;
		mFooterBgL = residL;
		mFooterBgR = residR;
	}
	
	public void setBackground(int residHeader, int residBody, int residFooter){
		mHasHeader = true;
		mHasBody   = true;
		mHasFooter = true;
		setBackground(residHeader, residHeader, residBody, residBody, residFooter, residFooter);
	}
	
	public void setBackground(int residmHeaderL, int residmHeaderR,int residBodyL, int residBodyR, int residmFooterL, int residmFooterR){
		mHasHeader = true;
		mHasBody   = true;
		mHasFooter = true;
		mHeaderBgL = residmHeaderL;
		mHeaderBgR = residmHeaderR;
		mBodyBgL   = residBodyL;
		mBodyBgR   = residBodyR;
		mFooterBgL = residmFooterL;
		mFooterBgR = residmFooterR;
	}
	
	public void setIcon(int resid){
		mHasIcon   = true;
		mHasHeader = true;
		setIcon(resid, resid);
	}
	
	public void setIcon(int residL, int residR){
		if(mLayoutL == null || mLayoutR == null){
			return;
		}
		
		mHasIcon   = true;
		mHasHeader = true;
		
		mIconLresid = residL;
		mIconRresid = residR;
	}

	public void setTitle(CharSequence title){
		mHasHeader = true;
		mTitle     = (String) title;
	}
	
	public void setMessage(CharSequence content){
		mHasBody = true;
		mMessage = (String) content;
	}
	
	public void setPositiveButton(CharSequence text, OnClickListener listener){
		if(mCsbtnPositive != null){
			mHasPositive = true;
			mCsbtnPositive.setText(text);
			mCsbtnPositive.setVisibility(View.VISIBLE);
			mCsbtnPositive.setOnClickListener(listener);
		}else{
			mHasPositive = false;
			new Exception("Positive Button created failed!").printStackTrace();
		}
	}
	
	public void setPositiveBackground(int resid){
		mHasPositive = true;
		setPositiveBackground(resid, resid);
	}
	
	public void setPositiveBackground(int residL, int residR){
		mHasPositive = true;
		mCsbtnPositive.setBackground(residL, residR);
	}
	
	public void setNeutralButton(CharSequence text, OnClickListener listener){
		if(mCsbtnNeutral != null){
			mHasNeutral = true;
			mCsbtnNeutral.setText(text);
			mCsbtnNeutral.setVisibility(View.VISIBLE);
			mCsbtnNeutral.setOnClickListener(listener);
		}
	}
	
	public void setNeutralBackground(int resid){
		mHasNeutral = true;
		setNeutralBackground(resid, resid);
	}
	
	public void setNeutralBackground(int residL, int residR){
		mHasNeutral = true;
		mCsbtnNeutral.setBackground(residL, residR);
	}
	
	public void setNegativeButton(CharSequence text, OnClickListener listener){
		if(mCsbtnNegative != null){
			mHasNegative = true;
			mCsbtnNegative.setText(text);
			mCsbtnNegative.setVisibility(View.VISIBLE);
			mCsbtnNegative.setOnClickListener(listener);
		}else{
			mHasNegative = false;
			new Exception("Negative Button created failed!").printStackTrace();
		}
	}
	
	public void setNegativeBackground(int resid){
		mHasNegative = true;
		setNegativeBackground(resid, resid);
	}
	
	public void setNegativeBackground(int residL, int residR){
		mHasNegative = true;
		mCsbtnNegative.setBackground(residL, residR);
	}
	
	public void show(){

		//重刷布局&&显示
		if(mLayoutL != null && mLayoutR != null){	
			mLayoutL.setVisibility(View.VISIBLE);
			mLayoutR.setVisibility(View.VISIBLE);
		}
		
		relocate(null,null);

		
		Log.w(TAG, "[show]L w:h:x:y = " + mLayoutL.getWidth() + ", " + mLayoutL.getHeight() + ", " + mLayoutL.getX() + ", " + mLayoutL.getY());
		Log.w(TAG, "[show]R w:h:x:y = " + mLayoutR.getWidth() + ", " + mLayoutR.getHeight() + ", " + mLayoutR.getX() + ", " + mLayoutR.getY());
	}
	
	public void hide(){
		if(mLayoutL != null && mLayoutR != null){
			mLayoutL.setVisibility(View.INVISIBLE);
			mLayoutR.setVisibility(View.INVISIBLE);
		}
	}
	
	public boolean isShown(){
		
		if(mLayoutL == null || mLayoutR == null){
			return false;
		}
		
		return mLayoutL.isShown() && mLayoutR.isShown();
	}
	
	private void relocate(Integer specialW, Integer specialH){
			int  axisXL = 0, axisXR = 0,
				 axisYL = 0, axisYR = 0;
			int  boxW   = 0,
				 boxH   = 0;
			Rect rectL  = new Rect(),
				 rectR  = new Rect();
			
			if(mLayoutL == null || mLayoutR == null){
				return;
			}

			mLayoutL.getGlobalVisibleRect(rectL);  
			mLayoutR.getGlobalVisibleRect(rectR);
			boxW = mLayoutL.getWidth()  >= rectL.width()  ? mLayoutL.getWidth()  : rectL.width();
			boxH = mLayoutL.getHeight() >= rectL.height() ? mLayoutL.getHeight() : rectL.height();

			if(specialW != null && specialH != null){
				boxW = specialW;
				boxH = specialH;
			}
			//定位
			switch (mAnchor) {
			case ANCHOR_CENTER:
				axisXL = m_cur_lx - boxW/2;
				axisYL = m_cur_ly - boxH/2;
				axisXR = m_cur_rx - boxW/2;
				axisYR = m_cur_ry - boxH/2;
				break;

			case ANCHOR_TOP_LEFT:
				axisXL = m_cur_lx;
				axisYL = m_cur_ly;
				axisXR = m_cur_rx;
				axisYR = m_cur_ry;
				break;

			case ANCHOR_TOP_RIGHT:
				axisXL = m_cur_lx - boxW;
				axisYL = m_cur_ly;
				axisXR = m_cur_rx - boxW;
				axisYR = m_cur_ry;
				break;

			case ANCHOR_BUTTOM_LEFT:
				axisXL = m_cur_lx;
				axisYL = m_cur_ly - boxH;
				axisXR = m_cur_rx;
				axisYR = m_cur_ry - boxH;
				break;

			case ANCHOR_BUTTOM_RIGHT:
				axisXL = m_cur_lx - boxW;
				axisYL = m_cur_ly - boxH;
				axisXR = m_cur_rx - boxW;
				axisYR = m_cur_ry - boxH;
				break;
			}

			if(boxH == 0)
				boxH = mBoxHeight;
			if(boxW == 0)
				boxW = mBoxWidth;
			mLayoutL.setLayoutParams(new AbsoluteLayout.LayoutParams(boxW, boxH, axisXL, axisYL));
			mLayoutR.setLayoutParams(new AbsoluteLayout.LayoutParams(boxW, boxH, axisXR, axisYR));
	}

	private void generateLayout(){
		if(mLayoutL == null || mLayoutR == null){
			return;
		}

		mCsbtnNegative.setDimension(m_is_3D);
		mCsbtnNeutral .setDimension(m_is_3D);
		mCsbtnPositive.setDimension(m_is_3D);

		//标记生成Footer，装载按钮
		mFooterL = new LinearLayout(m_context);
		mFooterR = new LinearLayout(m_context);
		mFooterL.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		mFooterR.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		mFooterL.setOrientation(LinearLayout.HORIZONTAL);
		mFooterR.setOrientation(LinearLayout.HORIZONTAL);
		mFooterL.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
		mFooterR.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
		mFooterL.setPadding(5, 5, 5, 5);
		mFooterR.setPadding(5, 5, 5, 5);
		mFooterL.addView(mCsbtnPositive.getChild(0), mCsbtnPositive.m_cur_width, mCsbtnPositive.m_cur_height);
		mFooterR.addView(mCsbtnPositive.getChild(1), mCsbtnPositive.m_cur_width, mCsbtnPositive.m_cur_height);
		mFooterL.addView(mCsbtnNeutral.getChild(0), mCsbtnNeutral.m_cur_width, mCsbtnNeutral.m_cur_height);
		mFooterR.addView(mCsbtnNeutral.getChild(1), mCsbtnNeutral.m_cur_width, mCsbtnNeutral.m_cur_height);
		mFooterL.addView(mCsbtnNegative.getChild(0), mCsbtnNegative.m_cur_width, mCsbtnNegative.m_cur_height);
		mFooterR.addView(mCsbtnNegative.getChild(1), mCsbtnNegative.m_cur_width, mCsbtnNegative.m_cur_height);

		
		if(mHasHeader){
			mHeaderL = new LinearLayout(m_context);
			mHeaderR = new LinearLayout(m_context);
			mHeaderL.setPadding(5, 5, 5, 5);
			mHeaderR.setPadding(5, 5, 5, 5);
			mHeaderL.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			mHeaderR.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			mHeaderL.setOrientation(LinearLayout.HORIZONTAL);
			mHeaderR.setOrientation(LinearLayout.HORIZONTAL);
			mHeaderL.setGravity(Gravity.TOP);
			mHeaderR.setGravity(Gravity.TOP);
			
			if(mHasIcon){
				mIconL = new ImageView(m_context);
				mIconR = new ImageView(m_context);
				mIconL.setImageResource(mIconLresid);
				mIconR.setImageResource(mIconRresid);
				mIconL.setScaleType(ScaleType.FIT_XY);
				mIconR.setScaleType(ScaleType.FIT_XY);
				mIconL.setLeft(0);
				mIconR.setLeft(0);
				mIconL.setLayoutParams(new LayoutParams(20, 40));
				mIconR.setLayoutParams(new LayoutParams(20, 40));
				mHeaderL.addView(mIconL);
				mHeaderR.addView(mIconR);
				
				
//				mCsIcon = new CSImageView(m_context);
//				mCsIcon.setImageResource(mIconLresid);
//				mCsIcon.setScaleType(ScaleType.FIT_XY);
//				mCsIcon.setLeft(0);
//				mCsIcon.setLayoutParams(new LayoutParams(20, 40));
//				mHeaderL.addView(mCsIcon.getChild(0));
//				mHeaderR.addView(mCsIcon.getChild(1));
			}
			
			mTitleL = new TextView(m_context);
			mTitleR = new TextView(m_context);
			mTitleL.setText(mTitle);
			mTitleR.setText(mTitle);
			mTitleL.setTextSize(20);
			mTitleR.setTextSize(20);
			mTitleL.getPaint().setFakeBoldText(true);
			mTitleR.getPaint().setFakeBoldText(true);
			mTitleL.setGravity(Gravity.CENTER);
			mTitleR.setGravity(Gravity.CENTER);
			
			mTitleL.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			mTitleR.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			

			mHeaderL.addView(mTitleL);
			mHeaderR.addView(mTitleR);
			
//			mCsTitle = new CSTextView(m_context);
//			mCsTitle.setText(mTitle);
//			mCsTitle.setTextSize(20);
//			mCsTitle.getPaint().setFakeBoldText(true);
//			mCsTitle.setGravity(Gravity.CENTER);
//			mCsTitle.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//			mHeaderL.addView(mCsTitle.getChild(0));
//			mHeaderR.addView(mCsTitle.getChild(1));
			
		}else{
			mHeaderL = mHeaderR = null;
		}
		
		if(mHasBody){
			mBodyL = new LinearLayout(m_context);
			mBodyR = new LinearLayout(m_context);
			mBodyL.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			mBodyR.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			mBodyL.setOrientation(LinearLayout.VERTICAL);
			mBodyR.setOrientation(LinearLayout.VERTICAL);
			mBodyL.setGravity(Gravity.CENTER);
			mBodyR.setGravity(Gravity.CENTER);
			mBodyL.setPadding(1, 1, 1, 1);
			mBodyR.setPadding(1, 1, 1, 1);
			

			
//			mCsContent = new CSTextView(m_context);
//			mCsContent.setText(mMessage);
//			mCsContent.setSingleLine(false);
//			mCsContent.setMovementMethod(ScrollingMovementMethod.getInstance());
//			mBodyL.addView(mCsContent.getChild(0));
//			mBodyR.addView(mCsContent.getChild(1));
			mContentL = new TextView(m_context);
			mContentR = new TextView(m_context);
			mContentL.setText(mMessage);
			mContentR.setText(mMessage);
			mContentL.setSingleLine(false);
			mContentR.setSingleLine(false);
			mContentL.setMovementMethod(ScrollingMovementMethod.getInstance());
			mContentR.setMovementMethod(ScrollingMovementMethod.getInstance());
			mContentL.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			mContentR.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			mBodyL.addView(mContentL);
			mBodyR.addView(mContentR);
		}else{
			mBodyL = mBodyR = null;
		}
		
		
		if(mHasPositive){
			mCsbtnPositive.setVisibility(View.VISIBLE);
		}else{
			mCsbtnPositive.setVisibility(View.INVISIBLE);
		}
		
		if(mHasNeutral){
			mCsbtnNeutral.setVisibility(View.VISIBLE);
		}else{
			mCsbtnNeutral.setVisibility(View.INVISIBLE);
		}
		
		if(mHasNegative){
			mCsbtnNegative.setVisibility(View.VISIBLE);
		}else{
			mCsbtnNegative.setVisibility(View.INVISIBLE);
		}
		
		//2D-3D换算
		if(m_is_3D){
			if(mTitleL != null && mTitleR != null){
				mTitleL.setTextScaleX(0.5f);
				mTitleR.setTextScaleX(0.5f);
			}
			if(mContentL != null && mContentR != null){
				mContentL.setTextScaleX(0.5f);
				mContentR.setTextScaleX(0.5f);
			}
			if(mIconL != null && mIconR != null){
				Matrix max = new Matrix();
				max.setScale(0.5f, 1f);
				mIconL.setImageMatrix(max);
				mIconR.setImageMatrix(max);
			}
			mCsbtnPositive.setTextScaleX(0.5f);
			mCsbtnNeutral.setTextScaleX(0.5f);
			mCsbtnNegative.setTextScaleX(0.5f);
		}else{
			if(mTitleL != null && mTitleR != null){
				mTitleL.setTextScaleX(1f);
				mTitleR.setTextScaleX(1f);
			}
			if(mContentL != null && mContentR != null){
				mContentL.setTextScaleX(1f);
				mContentR.setTextScaleX(1f);
			}
			if(mIconL != null && mIconR != null){
				Matrix max = new Matrix();
				max.setScale(1f, 1f);
				mIconL.setImageMatrix(max);
				mIconR.setImageMatrix(max);
			}
			mCsbtnPositive.setTextScaleX(1f);
			mCsbtnNeutral.setTextScaleX(1f);
			mCsbtnNegative.setTextScaleX(1f);
		}
		
		//生成主框架,装载Footer
		if(mHeaderL != null && mHeaderR != null){
			mLayoutL.addView(mHeaderL);
			mLayoutR.addView(mHeaderR);
		}
		if(mBodyL != null && mBodyR != null){
			mLayoutL.addView(mBodyL);
			mLayoutR.addView(mBodyR);
		}
		mLayoutL.addView(mFooterL);
		mLayoutR.addView(mFooterR);
	}
	
	private void initLayout() {		
		if(mLayoutL == null){
			mLayoutL = new LinearLayout(m_context);
		}
		if(mLayoutR == null){
			mLayoutR = new LinearLayout(m_context);
		}
		
		//准备默认值
		mCsbtnNegative = new CSButton(m_context);
		mCsbtnNeutral  = new CSButton(m_context);
		mCsbtnPositive = new CSButton(m_context);
		mCsbtnNegative.setText("Cancel");
		mCsbtnNeutral .setText("No");
		mCsbtnPositive.setText("Yes");
		mCsbtnNegative.setGravity(Gravity.CENTER);
		mCsbtnNeutral .setGravity(Gravity.CENTER);
		mCsbtnPositive.setGravity(Gravity.CENTER);
		mCsbtnNegative.setVisibility(View.VISIBLE);
		mCsbtnNeutral .setVisibility(View.VISIBLE);
		mCsbtnPositive.setGravity(Gravity.CENTER);
		mCsbtnNegative.setDimension(m_is_3D);
		mCsbtnNeutral .setDimension(m_is_3D);
		mCsbtnPositive.setDimension(m_is_3D);
		mCsbtnNegative.setLeft(5);
		mCsbtnNegative.setRight(5);
		mCsbtnNeutral.setLeft(5);
		mCsbtnNeutral.setRight(5);
		mCsbtnPositive.setLeft(5);
		mCsbtnPositive.setRight(5);
		
		mCsbtnNegative.setPadding(1, 1,1, 1);
		mCsbtnNeutral.setPadding(1, 1,1, 1);
		mCsbtnPositive.setPadding(1, 1,1, 1);
		
		mCsbtnNegative.setMargin(1, 1, 2, 2);
		mCsbtnNeutral.setMargin(1, 1, 2, 2);
		mCsbtnPositive.setMargin(1, 1, 2, 2);
		
		
		//生成基本框架
		mLayoutL.setLayoutParams(new LayoutParams((int)(m_screen_width*(3f/10f)), LayoutParams.WRAP_CONTENT));
		mLayoutR.setLayoutParams(new LayoutParams((int)(m_screen_width*(3f/10f)), LayoutParams.WRAP_CONTENT));
		mLayoutL.setOrientation(LinearLayout.VERTICAL);
		mLayoutR.setOrientation(LinearLayout.VERTICAL);
		mLayoutL.setBackgroundResource(R.drawable.dialog_frame);
		mLayoutR.setBackgroundResource(R.drawable.dialog_frame);
			
	}
	
	@Override
	public boolean touchEvent(MotionEvent event) {
		boolean state     = false;
		boolean intercept = false; 
		
		if(mIsModal){
			int [] location0 = new int[2];
			int [] location1 = new int[2];

			if(mLayoutL == null || mLayoutR == null){
				return state;
			}

			mLayoutL.getLocationOnScreen(location0);
			mLayoutR.getLocationOnScreen(location1);

			//若控件隐藏，则不响应点击事件
			if(mLayoutL.getVisibility()==View.INVISIBLE || mLayoutL.getVisibility()==View.GONE){
				return false;
			}

			if(intercept && (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP)){
				intercept = false;
			}
			
			if(m_is_3D){
				//Under 3D mode
				if(
						(location0[0] <= event.getRawX()/2 - m_offset
						&& location0[0] + mLayoutL.getWidth() >= event.getRawX()/2 - m_offset
						&& location0[1] <= event.getRawY()
						&& location0[1] + mLayoutL.getHeight() >= event.getRawY())
						||
						(location1[0] <= event.getRawX()/2 + m_offset
						&& location1[0] + mLayoutL.getWidth() >= event.getRawX()/2 + m_offset
						&& location1[1] <= event.getRawY()
						&& location1[1] + mLayoutL.getHeight() >= event.getRawY()))
				{
					if(event.getAction() == MotionEvent.ACTION_DOWN)
					{
						intercept = true;
					}
				}
			}else{
				//Under 2D mode
				if(
						location0[0] <= event.getRawX()
						&& location0[0] + mLayoutL.getWidth() >= event.getRawX()
						&& location0[1] <= event.getRawY()
						&& location0[1] + mLayoutL.getHeight() >= event.getRawY()
						)
				{
					if(event.getAction() == MotionEvent.ACTION_DOWN)
					{
						intercept = true;
					}
				}
			}
		}else{
			intercept = false;
		}
		
		if(mCsbtnPositive != null && mCsbtnPositive.getVisibility() == View.VISIBLE){
			state |= mCsbtnPositive.touchEvent(event);
		}
		if(mCsbtnNeutral != null && mCsbtnNeutral.getVisibility() == View.VISIBLE){
			state |= mCsbtnNeutral.touchEvent(event);
		}
		if(mCsbtnNegative != null && mCsbtnNegative.getVisibility() == View.VISIBLE){
			state |= mCsbtnNegative.touchEvent(event);
		}

		if(intercept){
			state = true;
		}
		
		return state;
	}

	@Override
	public void addToLayout(ViewGroup alayout) {
		
		if(mLayoutL == null || mLayoutR == null){
			return;
		}

		generateLayout();
		
		alayout.addView(mLayoutL);
		alayout.addView(mLayoutR);
		
		mLayoutL.setVisibility(View.INVISIBLE);
		mLayoutR.setVisibility(View.INVISIBLE);
	}

	@Override
	public Object save() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void restore(Object object) {
		// TODO Auto-generated method stub
		
	}
}

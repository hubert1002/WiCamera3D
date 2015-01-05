package com.wistron.StereoUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wistron.swpc.wicamera3dii.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


/**
 * 
 * @author WH1107028
 * @description 
 * 		1.直接使用SimpleAdapter即可，这样可以更方便的定义菜单内的布局
 * 		2.在控件初始化时，应该用INVISIBLE来设定，在hide()中就可以使用GONE了
 * 		3.建议菜单内部不使用3D效果图片，否则要使用两个Adapter，降低效率
 * @behaviour
 * 		1.x轴上，子菜单会优先向右弹出，右边的位置不够了，会改变方向向左弹出，直到左边的位置也不够后，再次改变方向
 * 		2.y轴上，子菜单会优先向下弹出，下边位置不够了则向上弹出
 * 		3.z轴上，子菜单的深度越高，z轴的值越大
 */
@SuppressWarnings("deprecation")
public class CSMenu extends CSView{
	private   final static String TAG                   = "CSMenu";
	protected final static String sKeyOfIcon            = "imageViewIcon";
	protected final static String sKeyOfText            = "textViewContent";
	protected final static String sKeyOfExtendIcon      = "imageViewExtendIcon";
	protected final static int    sIdOfIcon             = R.id.imageViewIcon; //<<==============================================Item中的图标
	protected final static int    sIdOfText             = R.id.textViewContent; //<<==============================================Item中的文字
	protected final static int    sIdOfExtendIcon       = R.id.imageViewExtendIcon; //<<==============================================Item中拓展图标
	protected final static int    sIdOfItemLayout       = R.layout.csmenu_item; //<<==============================================Item的布局
	public    final static int    ANCHOR_CENTER         = 0x00; //0000
	public    final static int    ANCHOR_TOP_LEFT       = 0x01; //0001
	public    final static int    ANCHOR_TOP_RIGHT      = 0x02; //0010
	public    final static int    ANCHOR_BUTTOM_LEFT    = 0x04; //0100
	public    final static int    ANCHOR_BUTTOM_RIGHT   = 0x08; //1000
	
	private   OnCSMenuClickListener         mOnCSMenuClickListener         = null;
	private   OnCSMenuItemClickListener     mOnCSMenuItemClickListener     = null;
	private   OnCSMenuItemLongClickListener mOnCSMenuItemLongClickListener = null;
	private   OnCSMenuItemSelectedListener  mOnCSMenuItemSelectedListener  = null;
	private   OnCSMenuScrollListener        mOnCSMenuScrollListener        = null;
	
	private   ListView[]                    mListViews                     = null;
	private   ArrayList<HashMap<String, Object>>
											mArrayListL                    = null,
											mArrayListR                    = null;
	private   CSMenuAdapter                 mAdapterL                      = null,
											mAdapterR                      = null;
	
	
	private   long                          mID                            = View.NO_ID;
	protected CSMenuItem                    mParentItem                    = null;
	protected CSMenuItem                    mCsMenuItem                    = null;
	protected CSMenu                        mParentMenu                    = null;
	private   int                           mAnchor                        = ANCHOR_CENTER;
	private   boolean                       mIsSubMenu                     = false;              //这个是必要的，CSMenu将根据这个标识来判断使用用户坐标还是自动根据父项定位
	private   int                           mScreenWidth                   = 0,
			                                mScreenHeight                  = 0;
	private TextView                        mListTitle[]                   = null,
			                                mListBottom[]                  = null;
	private int                             mTextSize                      = 18;
	private int                             mCsMenuWidth                   = 0,
			                                mCsMenuHeight                  = 0,
			                                mListBottomHeight              = 0,
			                                mListTitleHeight               = 40,

	                                        mSize                          = 0,
	                                        mParentPostion                 = 0;
	private long                            mItemId                        = 0;
	private int                             mResId                         = 0;
	public boolean                          mIsShow                         = false;
	private int                             mTextColor                     = Color.WHITE;
	
	
	   
	public CSMenu() {
		// TODO Auto-generated constructor stub
	}
	
	public CSMenu(Context context, long id) {
		super(context);
		mID = id;
		m_context = context;
		
		mListViews = new ListView[2];
		for (int i = 0; i < mListViews.length; i++) {
			mListViews[i] = new ListView(context);
		}
		
		//初始化数据源
		initDataSources();
//		setTitle(title);
//		setAdapter();
		
		//注册监听事件
//		mListViews[0].setOnClickListener(new OnClickListener(){
//
//			public void onClick(View v) {
//				if(mOnCSMenuClickListener != null){
//					if(mIsSubMenu){
//						mOnCSMenuClickListener.OnCSMenuClick(menuId);
//					}else{
//						mOnCSMenuClickListener.OnCSMenuClick(mID);
//					}
//				}
//			}
//
//		});
		
		mListViews[0].setOnItemClickListener(new OnItemClickListener() {

			//菜单项被点击后是把整个菜单关掉呢，还是只关被点的那个菜单呢？
			//菜单项被点后，有子菜单 ？ 显示子菜单 ：递归关闭全部菜单
			@SuppressWarnings("unchecked")
			public void onItemClick(AdapterView<?> arg0, View view, int pos, long id) {
				
				if(mOnCSMenuItemClickListener != null){
					//1.判断ClickedItem携带有子菜单？弹出子菜单：发出回调，关闭整个菜单
					if(mListViews[0] != null && mListViews[1] != null){
						if(((HashMap<String, Object>)mAdapterL.getItem(pos)).get("subMenu") == null){
							//找到root，回传点击事件，关闭整个菜单
							CSMenu curNode = CSMenu.this;
							while(curNode.mParentMenu != null){
								curNode = curNode.mParentMenu;
							}
							curNode.mOnCSMenuItemClickListener.OnCSMenuItemClick(arg0, view, pos, ((Long)((HashMap<String, Object>)mAdapterL.getItem(pos)).get("id")));
//							curNode.hideAll();
						}else{
							//打开子菜单
							((CSMenu)((HashMap<String, Object>)mAdapterL.getItem(pos)).get("subMenu")).show();
						}
					}
				}
			}
		});
		
		mListViews[0].setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View view, int pos, long id) {
				if(mOnCSMenuItemSelectedListener != null){
					if(mListViews[0] != null && mListViews[1] != null){
						if(((HashMap<String, Object>)mAdapterL.getItem(pos)).get("subMenu") == null){
							//找到root，回传点击事件
							CSMenu curNode = CSMenu.this;
							while(curNode.mParentMenu != null){
								curNode = curNode.mParentMenu;
							}
							curNode.mOnCSMenuItemSelectedListener.OnCSMenuItemSelected(arg0, view, pos, ((Long)((HashMap<String, Object>)mAdapterL.getItem(pos)).get("id")));
						}
					}
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		
		mListViews[0].setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View view, int pos, long id) {
				boolean state = false;
				if(mOnCSMenuItemLongClickListener != null){
					if(mListViews[0] != null && mListViews[1] != null){
						if(((HashMap<String, Object>)mAdapterL.getItem(pos)).get("subMenu") == null){
							//找到root，回传点击事件，关闭整个菜单
							CSMenu curNode = CSMenu.this;
							while(curNode.mParentMenu != null){
								curNode = curNode.mParentMenu;
							}
							state = curNode.mOnCSMenuItemLongClickListener.OnCSMenuItemLongClick(arg0, view, pos, ((Long)((HashMap<String, Object>)mAdapterL.getItem(pos)).get("id")));
							curNode.hideAll();
						}
					}
				}
				return state;
			}
		});
	}
	

	/**
	 * 设置定位点
	 * @param anchor TOP_LEFT | TOP_RIGHT | BUTTOM_LEFT | BUTTOM_RIGHT | CENTER
	 */
	public void setAnchor(int anchor) {//WeiWang
		switch (anchor) {


case CSMenu.ANCHOR_TOP_LEFT:
			
			break;
		case CSMenu.ANCHOR_BUTTOM_LEFT:
			m_cur_ly = m_cur_ly - mListViews[0].getHeight();
			break;
		case CSMenu.ANCHOR_CENTER:
			m_cur_lx = m_cur_lx - mListViews[0].getWidth()/2;
			m_cur_ly = m_cur_ly - mListViews[0].getHeight()/2;
			break;
		case CSMenu.ANCHOR_TOP_RIGHT:
			m_cur_lx = m_cur_lx - mListViews[0].getWidth();

			break;
		case CSMenu.ANCHOR_BUTTOM_RIGHT:
			m_cur_lx = m_cur_lx - mListViews[0].getWidth();
			m_cur_ly = m_cur_ly - mListViews[0].getHeight();
			break;
		}
	}
	
	/**
	 * 設置菜單的可見性
	 */
	@Override
	public void setVisibility(int visibility) {//WeiWang
		// TODO Auto-generated method stub
		if(mListViews[0] == null ||mListViews[1] == null){
			return;
		}
		
		mListViews[0].setVisibility(visibility);
		mListViews[1].setVisibility(visibility);
		
	}

	/**
	 * 設置菜單是否可用
	 */
	@Override
	public void setEnable(boolean enabled) {//WeiWang
		// TODO Auto-generated method stub
		if(mListViews[0] == null ||mListViews[1] == null){
			return;
		}
		mListViews[0].setEnabled(enabled);
		mListViews[1].setEnabled(enabled);
	}

	public void setTextColor(int color){
		mTextColor = color;
	}
	/**
	 * 設置菜單為3D模式
	 */
	@Override
	public void setDimension(boolean is3d) {//WeiWang
		// TODO Auto-generated method stub
		super.setDimension(is3d);
		
		
			if(mListTitle[0] != null && mListTitle[1] != null ){
				if(m_is_3D){
					mListTitle[0].setTextScaleX(0.5f);
					mListTitle[1].setTextScaleX(0.5f);
				}else{
					mListTitle[0].setTextScaleX(1f);
					mListTitle[1].setTextScaleX(1f);
				}
					mListTitle[0].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, mListTitleHeight, m_cur_lx, m_cur_ly - mListTitleHeight));
					mListTitle[1].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, mListTitleHeight, m_cur_rx, m_cur_ry - mListTitleHeight));
					mListTitle[0].requestLayout();
					mListTitle[1].requestLayout();
			}
		
			if(mListBottom[0] != null && mListBottom[1] != null ){
			
				
				mListBottom[0].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, mListTitleHeight, m_cur_lx, m_cur_ly + m_cur_height));
				mListBottom[1].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, mListTitleHeight, m_cur_rx, m_cur_ry + m_cur_height));
				mListBottom[0].requestLayout();
				mListBottom[1].requestLayout();
		}
		
		if(mListViews[0] != null &&mListViews[1]!= null){
			if(m_cur_width == WRAP_CONTENT || m_cur_height == WRAP_CONTENT){
				mListViews[0].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_lx, m_cur_ly));
				mListViews[1].setLayoutParams(new AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, m_cur_rx, m_cur_ry));
			}else{
			mListViews[0].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height,m_cur_lx,m_cur_ly));
			mListViews[1].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, m_cur_height,m_cur_rx,m_cur_ry));
			}
			int count  = mListViews[0].getCount();
			for(int i=0;i<count;i++){
				if( ((HashMap<String, Object>)mAdapterL.getItem(i)).get("subMenu") != null){
					((CSMenu)((HashMap<String, Object>)mAdapterL.getItem(i)).get("subMenu")).setDimension(is3d); 

					}
				}
			}
		
			mListViews[0].requestLayout();
			mListViews[1].requestLayout();
		
	}
	
	/**
	 * 設置菜單的大小
	 */
	@Override
	public void setSize(int width, int height) {
		// TODO Auto-generated method stub
		super.setSize(width, height);
		mCsMenuWidth  = width;
		mCsMenuHeight = height;
	}
	
	/**
	 * 設置菜單的橫縱座標
	 */
	@Override
	public void setLocation(int x, int y) {
		// TODO Auto-generated method stub
		m_cur_lx = x;
		m_cur_ly = y;
		m_cur_rx= m_cur_lx+m_screen_width/2;
		m_cur_ry = y;
		super.setLocation(x, y);
		if(mListViews[0] ==null){
			return;
		}
		mListViews[0].setLayoutParams(new AbsoluteLayout.LayoutParams(mCsMenuWidth, mCsMenuHeight, m_cur_lx, m_cur_ly));
		mListViews[1].setLayoutParams(new AbsoluteLayout.LayoutParams(mCsMenuWidth, mCsMenuHeight, m_cur_lx+m_screen_width/2, m_cur_ly));
	}
	
	/**
	 * 設置菜單中ListView的Adapter
	 * @param adapter
	 */
	public void setAdapter(ListAdapter adapter){//WeiWang
		if(mListViews[0] == null ||mListViews[1] == null){
			return;
		}
		mListViews[0].setAdapter(adapter);
		mListViews[1].setAdapter(adapter);
	}
	
	/**
	 * 設置菜單的透明度
	 * @param alpha
	 */
	public void setAlpha(float alpha){//WeiWang
		if(mListViews[0] == null ||mListViews[1] == null){
			return;
		}
		mListViews[0].setAlpha(alpha);
		mListViews[1].setAlpha(alpha);
	}
	
	/**
	 * 設置菜單分割線的顏色
	 * @param color
	 */
	public void setCacheColorHint(int color){
		if(mListViews[0] == null ||mListViews[1] == null){
			return;
		}
		mListViews[0].setCacheColorHint(color);
		mListViews[1].setCacheColorHint(color);
		
	}
	
	/**
	 * 設置菜單的背景圖
	 * @param resid
	 */
	public void setBackground(int resid){//WeiWang
		if(mListViews[0] == null ||mListViews[1] == null){
			return;
		}
		setBackground(resid, resid);
	}
	
	/**
	 * 設置左屏幕菜單的背景圖和右屏幕菜單的背景圖
	 * @param residL
	 * @param residR
	 */
	public void setBackground(int residL, int residR){//WeiWang
		if(mListViews[0] == null ||mListViews[1] == null){
			return;
		}
		mListViews[0].setBackgroundResource(residL);
		mListViews[1].setBackgroundResource(residR);
	}
	
	/**
	 * 設置菜單的背景顏色
	 * @param color
	 */
	public void setBackColor(int color){//WeiWang
		if(mListViews[0] == null ||mListViews[1] == null){
			return;
		}
		mListViews[0].setBackgroundColor(color);
		mListViews[1].setBackgroundColor(color);
	}
	
	/**
	 * 設置菜單選項之間的分割線
	 * @param drawable
	 */
	public void setDivider(Drawable drawable){//WeiWang
		if(mListViews[0] == null ||mListViews[1] == null){
			return;
		}
		setDivider(drawable, drawable);
	}
	
	/**
	 * 設置左右菜單選項之間的分割線
	 * Sets the drawable that will be drawn between each item in the list. If the drawable does not have an intrinsic height, you should also call setDividerHeight(int)
	 * @param drawableL
	 * @param drawableR
	 */
	public void setDivider(Drawable drawableL, Drawable drawableR){//WeiWang
		if(mListViews[0] == null ||mListViews[1] == null){
			return;
		}
		mListViews[0].setDivider(drawableL);
		mListViews[1].setDivider(drawableR);
	}
	
	/**
	 * Sets the height of the divider that will be drawn between each item in the list. Calling this will override the intrinsic height as set by setDivider(Drawable)
	 * @param height
	 */
	public void setDividerHeight(int height){//WeiWang
		if(mListViews[0] == null ||mListViews[1] == null){
			return;
		}
		mListViews[0].setDividerHeight(height);
		mListViews[1].setDividerHeight(height);
	}
	
	/**
	 * The amount of friction applied to flings. The default value is getScrollFriction().
	 * @param friction
	 */
	public void setFriction(float friction){//WeiWang
		if(mListViews[0] == null ||mListViews[1] == null){
			return;
		}
		mListViews[0].setFriction(friction);
		mListViews[1].setFriction(friction);
	}
	
	/**
	 * Enables or disables the drawing of the divider for header views.
	 * @param enable: True to draw the headers, false otherwise.
	 */
	public void setHeaderDividersEnabled(boolean enable){//WeiWang
		if(mListViews[0] == null ||mListViews[1] == null){
			return;
		}
		mListViews[0].setHeaderDividersEnabled(enable);
		mListViews[1].setHeaderDividersEnabled(enable);
	}
	
	/**
	 * Enables or disables the drawing of the divider for footer views.
	 * @param enable:True to draw the footers, false otherwise. 
	 */
	public void setFooterDividersEnabled(boolean enable){//WeiWang
		if(mListViews[0] == null ||mListViews[1] == null){
			return;
		}
		mListViews[0].setFooterDividersEnabled(enable);
		mListViews[1].setFooterDividersEnabled(enable);
	}
	
	/**
	 * Sets the checked state of the specified position. The is only valid if the choice mode has been set to CHOICE_MODE_SINGLE or CHOICE_MODE_MULTIPLE.
	 * @param position:The item whose checked state is to be checked 
	 * @param checked:The new checked state for the item
	 */
	public void setItemChecked(int position, boolean checked){//WeiWang
		if(mListViews[0] == null ||mListViews[1] == null){
			return;
		}
		mListViews[0].setItemChecked(position, checked);
		mListViews[1].setItemChecked(position, checked);
	}
	
	/**
	 * Enables or disables long click events for this view. When a view is long
	 * clickable it reacts to the user holding down the button for a longer
	 * duration than a tap. This event can either launch the listener or a
	 * context menu.
	 * 
	 * @param longClickable
	 *            true to make the view long clickable, false otherwise
	 */
	public void setLongClickable(boolean longClickable){//WeiWang
		if(mListViews[0] == null ||mListViews[1] == null){
			return;
		}
		mListViews[0].setLongClickable(longClickable);
		mListViews[1].setLongClickable(longClickable);
	}
	
	/**
	 * Sets the pressed state for this view.
	 * 
	 * @param isPressed
	 *            Pass true to set the View's internal state to "pressed", or
	 *            false to reverts the View's internal state from a previously
	 *            set "pressed" state.
	 */
	public void setPressed(boolean isPressed){//WeiWang
		if(mListViews[0] == null ||mListViews[1] == null){
			return;
		}
		mListViews[0].setPressed(isPressed);
		mListViews[1].setPressed(isPressed);
	}
	
	/**
	 * Specify the style of the scrollbars. The scrollbars can be overlaid or
	 * inset. When inset, they add to the padding of the view. And the
	 * scrollbars can be drawn inside the padding area or on the edge of the
	 * view. For example, if a view has a background drawable and you want to
	 * draw the scrollbars inside the padding specified by the drawable, you can
	 * use SCROLLBARS_INSIDE_OVERLAY or SCROLLBARS_INSIDE_INSET. If you want
	 * them to appear at the edge of the view, ignoring the padding, then you
	 * can use SCROLLBARS_OUTSIDE_OVERLAY or SCROLLBARS_OUTSIDE_INSET.
	 * 
	 * @param style
	 */
	public void setScrollBarStyle(int style){//WeiWang
		if(mListViews[0] == null ||mListViews[1] == null){
			return;
		}
		mListViews[0].setScrollBarStyle(style);
		mListViews[1].setScrollBarStyle(style);
	}
	
	/**
	 * Sets the currently selected item. If in touch mode, the item will not be
	 * selected but it will still be positioned appropriately. If the specified
	 * selection position is less than 0, then the item at position 0 will be
	 * selected.
	 * 
	 * @param position
	 *            Index (starting at 0) of the data item to be selected.
	 */
	public void setSelection(int position){//WeiWang
		if(mListViews[0] == null ||mListViews[1] == null){
			return;
		}
		mListViews[0].setSelection(position);
		mListViews[1].setSelection(position);
	}
	
	/**
	 * Changes the selection state of this view. A view can be selected or not.
	 * Note that selection is not the same as focus. Views are typically
	 * selected in the context of an AdapterView like ListView or GridView; the
	 * selected view is the view that is highlighted.
	 * 
	 * @param selected
	 *            true if the view must be selected, false otherwise
	 */
	public void setSelected(boolean selected){//WeiWang
		if(mListViews[0] == null ||mListViews[1] == null){
			return;
		}
		mListViews[0].setSelected(selected);
		mListViews[1].setSelected(selected);
	}
	
	/**
	 * Sets the selected item and positions the selection y pixels from the top
	 * edge of the ListView. (If in touch mode, the item will not be selected
	 * but it will still be positioned appropriately.)
	 * 
	 * @param position
	 *            Index (starting at 0) of the data item to be selected.
	 * @param y
	 *            The distance from the top edge of the ListView (plus padding)
	 *            that the item will be positioned.
	 */
	public void setSelectionFromTop(int position, int y){//WeiWang
		if(mListViews[0] == null ||mListViews[1] == null){
			return;
		}
		mListViews[0].setSelectionFromTop(position,  y);
		mListViews[1].setSelectionFromTop(position,  y);
	}
	
	/**
	 * Set a Drawable that should be used to highlight the currently selected item.
	 * @param resid
	 */
	public void setSelector(int resid){//WeiWang
		if(mListViews[0] == null ||mListViews[1] == null){
			return;
		}
		setSelector(resid, resid);
	}
	
	
	public void setSelector(int residL, int residR){//WeiWang
		if(mListViews[0] == null ||mListViews[1] == null){
			return;
		}
		mListViews[0].setSelector(residL);
		mListViews[1].setSelector(residR);
	}

	/**
	 * 設置菜單選項的點擊事件
	 * @param listener
	 */
	public void setOnItemClickListener(OnCSMenuItemClickListener listener){
		mOnCSMenuItemClickListener = listener;
		int count = mListViews[0].getCount();
		for(int i=0;i<count;i++){
			if( ((HashMap<String, Object>)mAdapterL.getItem(i)).get("subMenu") != null){
				((CSMenu)((HashMap<String, Object>)mAdapterL.getItem(i)).get("subMenu")).setOnItemClickListener(listener); 

			}
		}
	}
	
	/**
	 * 設置菜單選項被選中時的監聽事件
	 * @param listener
	 */
	public void setOnItemSelectedListener(OnCSMenuItemSelectedListener listener){
		mOnCSMenuItemSelectedListener = listener;
		int count = mListViews[0].getCount();
		for(int i=0;i<count;i++){
			if( ((HashMap<String, Object>)mAdapterL.getItem(i)).get("subMenu") != null){
				((CSMenu)((HashMap<String, Object>)mAdapterL.getItem(i)).get("subMenu")).setOnItemSelectedListener(listener); 

			}
		}
	}
	
	/**
	 * 設置菜單的長按事件
	 * @param listener
	 */
	public void setOnItemLongClickListener(OnCSMenuItemLongClickListener listener){
		mOnCSMenuItemLongClickListener = listener;
		int count = mListViews[0].getCount();
		for(int i=0;i<count;i++){
			if( ((HashMap<String, Object>)mAdapterL.getItem(i)).get("subMenu") != null){
				((CSMenu)((HashMap<String, Object>)mAdapterL.getItem(i)).get("subMenu")).setOnItemLongClickListener(listener); 

			}
		}
	}
	
	/**
	 * 設置菜單的點擊事件
	 * @param listener
	 */
	public void setOnClickListener(OnCSMenuClickListener listener){
		mOnCSMenuClickListener = listener;
		int count = mListViews[0].getCount();
		for(int i=0;i<count;i++){
			if( ((HashMap<String, Object>)mAdapterL.getItem(i)).get("subMenu") != null){
				((CSMenu)((HashMap<String, Object>)mAdapterL.getItem(i)).get("subMenu")).setOnClickListener(listener); 

			}
		}
	}
	
	/**
	 * 設置菜單的滑動時的監聽事件
	 * @param listener
	 */
	public void setOnScrollListener(OnCSMenuScrollListener listener){
		mOnCSMenuScrollListener = listener;
		int count = mListViews[0].getCount();
		for(int i=0;i<count;i++){
			if( ((HashMap<String, Object>)mAdapterL.getItem(i)).get("subMenu") != null){
				((CSMenu)((HashMap<String, Object>)mAdapterL.getItem(i)).get("subMenu")).setOnScrollListener(listener); 

			}
		}
	}
	
	/**
	 * 獲取菜單當前顯示的模式（2D/3D）
	 * @return
	 */
	public boolean getDimension(){//WeiWang~~
		return m_is_3D;
	}
	
	/**
	 * 獲取左屏幕上或者右屏幕上的菜單
	 * @param index
	 * @return
	 */
	@Deprecated
	public View getChildAt(int index){//WeiWang
		if(mListViews[0] == null || mListViews[1] == null){
			return null;
		}
		return mListViews[0].getChildAt(index);
	}
	
	/**
	 * 獲取菜單的選項個數
	 * @return
	 */
	public int getCount(){//WeiWang~~
		if(mListViews[0] == null || mListViews[1] == null){
			return -1;
		}
		return mListViews[0].getCount();
	}
	
	/**
	 * Returns the position within the adapter's data set for the first item
	 * displayed on screen.
	 * 
	 * @return The position within the adapter's data set
	 */
	public int getFisrtVisiblePosition(){//WeiWang~~
		if(mListViews[0] == null || mListViews[1] == null){
			return -1;
		}
		return mListViews[0].getFirstVisiblePosition();
	}
	
	/**
	 * Return the width of the your view.
	 * @return
	 * Returns The width of your view, in pixels.
	 */
	public int getWidth(){//WeiWang~~
		if(mListViews[0] == null || mListViews[1] == null){
			return -1;
		}
		return mListViews[0].getWidth();
	}
	
	/**
	 * Return the height of your view.
	 * 
	 * @return
	 * Returns The height of your view, in pixels.
	 */
	public int getHeight(){//WeiWang~~
		if(mListViews[0] == null || mListViews[1] == null){
			return -1;
		}
		return mListViews[0].getHeight();
	}
	
	/**
	 * Gets the data associated with the specified position in the list.
	 * 
	 * @param position
	 *            Which data to get
	 * @return Returns The data associated with the specified position in the
	 *         list
	 */
	public Object getItemAtPosition(int position){//WeiWang~~
		if(mListViews[0] == null || mListViews[1] == null){
			return null;
		}
		return mListViews[0].getItemAtPosition(position);
	}
	
	/**
	 * Get the item id at a position
	 * @param position
	 * @return
	 */
	public long getItemIdAtPosition(int position){//WeiWang~~
		if(mListViews[0] == null || mListViews[1] == null){
			return -1;
		}
		return mListViews[0].getItemIdAtPosition(position);
	}
	
	/**
	 * Returns the position within the adapter's data set for the last item
	 * displayed on screen.
	 * 
	 * @return The position within the adapter's data set
	 */
	public int getLastVisiblePosition(){//WeiWang~~
		if(mListViews[0] == null || mListViews[1] == null){
			return -1;
		}
		return mListViews[0].getLastVisiblePosition();
	}
	
	/**
	 * Get the LayoutParams associated with this view.
	 * 
	 * @return The LayoutParams associated with this view, or null if no
	 *         parameters have been set yet
	 */
	public LayoutParams getLayoutParams(){//WeiWang~~
		if(mListViews[0] == null || mListViews[1] == null){
			return null;
		}
		return mListViews[0].getLayoutParams();
	}
	
	/**
	 * Get the position within the adapter's data set for the view, where view
	 * is a an adapter item or a descendant of an adapter item.
	 * 
	 * @param view
	 *            an adapter item, or a descendant of an adapter item. This must
	 *            be visible in this AdapterView at the time of the call.
	 * @return the position within the adapter's data set of the view, or
	 *         INVALID_POSITION if the view does not correspond to a list item
	 *         (or it is not currently visible).
	 */
	public int getPositionForView(View view){//WeiWang~~
		if(mListViews[0] == null || mListViews[1] == null){
			return -1;
		}
		return mListViews[0].getPositionForView(view);
	}
	
	/**
	 * 
	 * @return The data corresponding to the currently selected item, or null if
	 *         there is nothing selected.
	 */
	public Object getSelectedItem(){//WeiWang~~
		if(mListViews[0] == null || mListViews[1] == null){
			return null;
		}
		return mListViews[0].getSelectedItem();
	
	}
	
	/**
	 * @return The id corresponding to the currently selected item, or INVALID_ROW_ID if nothing is selected.
	 */
	public long getSelectedItemId(){//WeiWang~~
		if(mListViews[0] == null || mListViews[1] == null){
			return -1;
		}
		
		return mListViews[0].getSelectedItemId();
	}
	
	/**
	 * @return Return the position of the currently selected item within the adapter's data set
	 */
	public int getSelectedItemPosition(){//WeiWang~~
		if(mListViews[0] == null || mListViews[1] == null){
			return -1;
		}
		
		return mListViews[0].getSelectedItemPosition();
	}
	
	/**
	 * @return The view corresponding to the currently selected item, or null if nothing is selected
	 */
	public View getSelectedView(){//WeiWang~~
		if(mListViews[0] == null || mListViews[1] == null){
			return null;
		}
		
		return mListViews[0].getSelectedView();
	}
	
	/**
	 * 
	 * @return Returns the visibility status for this view.
	 */
	public int getVisibility(){//WeiWang~~
		if(mListViews[0] == null || mListViews[1] == null){
			return -1;
		}
		return mListViews[0].getVisibility();
	}

	/**
	 * Set textsize of the menu
	 * @param size
	 */
	public void setTextSize(int size){
		mTextSize =  size;
	}
	
	/**
	 * 
	 * @return If menu is show return true,else return false;
	 */
	public boolean isSubMenu(){//Cocoonshu √۞
		if(mParentItem != null && mParentMenu != null){
			return true;
		}
		return false;
	}
	
	/**
	 * 添加Menu.Item
	 * @param menuItem
	 */
	public boolean addItem(CSMenuItem menuItem){//Cocoonshu √۞
		if(mAdapterL == null || mAdapterR == null || mArrayListL == null || mArrayListR == null){
			return false;
		}
		
		if(menuItem == null){
			return false;
		}
		
		mArrayListL.add(menuItem.mItemL);
		mArrayListR.add(menuItem.mItemR);
		mAdapterL.notifyDataSetChanged();
		mAdapterR.notifyDataSetChanged();
		
		return true;
	}
	
	
	/**
	 * 添加Menu.Item
	 * @param id item的id
	 * @param text item的文字
	 * @return true is successful
	 */
	public CSMenuItem addItem(int id, CharSequence text){//Cocoonshu √۞
		
		CSMenuItem item = null;
		if(mAdapterL == null || mAdapterR == null || mArrayListL == null || mArrayListR == null){
			return null;
		}
		
		item = new CSMenuItem(0, 0, text, 0, 0, id);
		mArrayListL.add(item.mItemL);
		mArrayListR.add(item.mItemR);
		item.setPosition(mArrayListL.size() - 1);
		item.setParent(this);
		mAdapterL.notifyDataSetChanged();
		mAdapterR.notifyDataSetChanged();
		
		return item;
	}
	
	/**
	 * 添加Menu.Item
	 * @param id item的id
	 * @param text item的文字
	 * @return true is successful
	 */
	public CSMenuItem addItem(int id, int textResid){//Cocoonshu √۞
		
		CSMenuItem item = null;
		String     text = "";
		if(mAdapterL == null || mAdapterR == null || mArrayListL == null || mArrayListR == null){
			return null;
		}
		
		
		text = m_context.getResources().getString(textResid);
		item = new CSMenuItem(0, 0, text, 0, 0, id);
		mArrayListL.add(item.mItemL);
		mArrayListR.add(item.mItemR);
		item.setPosition(mArrayListL.size() - 1);
		item.setParent(this);
		mAdapterL.notifyDataSetChanged();
		mAdapterR.notifyDataSetChanged();
		
		return item;
	}
	
	/**
	 * Add a fixed view to appear at the top of the list. If addHeaderView is
	 * called more than once, the views will appear in the order they were
	 * added. Views added using this call can take focus if they want.
	 * 
	 * @param view
	 *            The view to add.
	 */
	public void addHeaderView(View view){//WeiWang~~
		if(mListViews[0] == null || mListViews[1] == null){
			return ;
		}
		
		mListViews[0].addHeaderView(view);
		mListViews[1].addHeaderView(view);
	}
	
	/**
	 * Add a fixed view to appear at the bottom of the list. If addFooterView is
	 * called more than once, the views will appear in the order they were
	 * added. Views added using this call can take focus if they want.
	 * 
	 * @param view
	 *            The view to add.
	 */
	public void addFooterView(View view){//WeiWang~~
		if(mListViews[0] == null || mListViews[1] == null){
			return ;
		}
		mListViews[0].addFooterView(view);
		mListViews[1].addFooterView(view);
	}
	
	/**
	 * 給菜單添加標題
	 * @param title
	 */
	public void setTitle(CharSequence title) {
		if (mListTitle[0] == null || mListTitle[1] == null) {
			return;
		}

		mListTitle[0].setText(title);
		mListTitle[0].setTextScaleX(0.5f);
		mListTitle[0].setTextSize(mTextSize);
		mListTitle[0].setTextColor(mTextColor);
//		mListTitle[0].setBackgroundResource(R.drawable.popup_list);
		mListTitle[0].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, mListTitleHeight, m_cur_lx, m_cur_ly - mListTitleHeight));

		mListTitle[1].setText(title);
		mListTitle[1].setTextScaleX(0.5f);
		mListTitle[1].setTextSize(mTextSize);
		mListTitle[1].setTextColor(mTextColor);
//		mListTitle[1].setBackgroundResource(R.drawable.popup_list);
		mListTitle[1].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, mListTitleHeight, m_cur_rx, m_cur_ry - mListTitleHeight));

	}
	
	/**
	 * 給菜單添加標題
	 * @param title
	 */
	public void setTitle(int resid) {
		if (mListTitle[0] == null || mListTitle[1] == null) {
			return;
		}

		mListTitle[0].setText(resid);
		mListTitle[0].setTextScaleX(0.5f);
		mListTitle[0].setTextSize(mTextSize);
		mListTitle[0].setTextColor(mTextColor);
		mListTitle[0].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, mListTitleHeight, m_cur_lx, m_cur_ly - mListTitleHeight));

		mListTitle[1].setText(resid);
		mListTitle[1].setTextScaleX(0.5f);
		mListTitle[1].setTextSize(mTextSize);
		mListTitle[1].setTextColor(mTextColor);
		mListTitle[1].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, mListTitleHeight, m_cur_rx, m_cur_ry - mListTitleHeight));

	}
	
	/**
	 * 
	 * @param resId
	 */
	public void setTitleBackground(int resId){
		if(mListTitle[0] == null || mListTitle[1] == null){
			return ;
		}
	
		mListTitle[0].setBackgroundResource(resId);
		mListTitle[1].setBackgroundResource(resId);
		mListTitle[0].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, mListTitleHeight, m_cur_lx, m_cur_ly - mListTitleHeight));
		mListTitle[1].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, mListTitleHeight, m_cur_rx, m_cur_ry - mListTitleHeight));
	}
	
	public void setTitleTextSize(int size){
		if(mListTitle[0] == null || mListTitle[1] == null){
			return ;
		}
		mListTitle[0].setTextSize(size);
		mListTitle[1].setTextSize(size);
	}
	public void setTitleHeight(int height){
		mListTitleHeight = height;
	}
	
	public void setBottomBackground(int resId){
		if(mListBottom[0] == null || mListBottom[1] == null){
			return ;
		}
		mListBottom[0].setBackgroundResource(resId);
		mListBottom[1].setBackgroundResource(resId);
		mListBottom[0].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, mListTitleHeight, m_cur_lx , m_cur_ly + m_cur_height));
		mListBottom[1].setLayoutParams(new AbsoluteLayout.LayoutParams(m_cur_width, mListTitleHeight, m_cur_rx, m_cur_ry + m_cur_height));
	}
	public void show(){//Cocoonshu:子菜单定位还没做
		if(mListViews[0] == null || mListViews[1] == null){
			return;
		}
		
		if(mParentItem != null && mParentMenu != null){
			float subMenuX = 0,
				  subMenuY = 0;
			float subMenuW = 0,
				  subMenuH = 0;
			
			//计算子菜单的X
			subMenuX = mParentMenu.mListViews[0].getX();
			subMenuW = mParentMenu.mListViews[0].getWidth();
//			if((int)(subMenuX + subMenuW) >= mListViews[0].getWidth()){//<===================================菜单全屏怎么办？？？
//				//子菜单出现在父菜单右边
//				subMenuX = subMenuX + subMenuW;
//			}else{
//				//子菜单出现在父菜单左边
//				subMenuX = subMenuX - mListViews[0].getWidth();
//			}
//			
//			
//			//计算子菜单的Y
//			int dfafsaf = mParentItem.getPosition();
//			View sdsfView = mParentMenu.mListViews[0].getChildAt(mParentItem.getPosition());
//			subMenuY = mParentMenu.mListViews[0].getY() + mParentMenu.mListViews[0].getChildAt(mParentItem.getPosition()).getY();
//			if(subMenuY + mListViews[0].getHeight() > m_screen_height){
//				//子菜单出现在基线父Item上方
//				subMenuY = subMenuY - mListViews[0].getHeight();
//			}else{//<===================================万一上下都不满足就惨了，就必须降低子菜单高度？？？
//				//子菜单出现在基线父Item下方
//				//不用重算subMenuY了
//			}
			
			//计算子菜单的Height
			//<==========?????==========>//
			
			//设定子菜单
//			setLocation((int)subMenuX, (int)subMenuY);
//			setSize((int)subMenuW, (int)subMenuH);
		}   
		
		if (mParentMenu != null) {

			for (int i = 0; i < this.mParentMenu.mListViews[0].getCount(); i++) {

				if(((CSMenu)((HashMap<String, Object>)mParentMenu.mAdapterL.getItem(i)).get("subMenu"))!= null ){
					((CSMenu)((HashMap<String, Object>)mParentMenu.mAdapterL.getItem(i)).get("subMenu")).hide(); 
				}
			}
		}
	
//			for(int i = 0;i<mListViews[0].getCount();i++){		
//				if(((CSMenu)((HashMap<String, Object>)mAdapterL.getItem(i)).get("subMenu"))!= null ){
//					((CSMenu)((HashMap<String, Object>)mAdapterL.getItem(i)).get("subMenu")).hide(); 
//				}
//				}
		
		//子级菜单定位
		mListViews[0].setVisibility(View.VISIBLE); //子级菜单显示
		mListViews[1].setVisibility(View.VISIBLE);
		
		if(mListTitle[0] != null && mListTitle[1] != null ){
				mListTitle[0].setVisibility(View.VISIBLE);
				mListTitle[1].setVisibility(View.VISIBLE);
			
		}
		
		if(mListBottom[0] != null && mListBottom[1] != null ){
			mListBottom[0].setVisibility(View.VISIBLE);
			mListBottom[1].setVisibility(View.VISIBLE);
		
	}
		
		
		mIsShow = true;
		
	}
	
	public void showAll(){//Cocoonshu:子菜单定位还没做
		
		
		//显示root menu
		if(mListViews[0] == null || mListViews[1] == null){
			mListViews[0].setVisibility(View.VISIBLE);
			mListViews[1].setVisibility(View.VISIBLE);
		}
		
		//显示子级菜单
		if(mAdapterL != null && mAdapterR != null){
			mSize = mAdapterL.getCount() < mAdapterR.getCount() ? mAdapterL.getCount() : mAdapterR.getCount();
			for (int i = 0; i < mSize; i++) {
				CSMenu subMenuL = ((CSMenu)((HashMap<String, Object>)mAdapterL.getItem(i)).get("subMenu"));
				CSMenu subMenuR = ((CSMenu)((HashMap<String, Object>)mAdapterR.getItem(i)).get("subMenu"));
				if(subMenuL != null){
					//<==========?????==========>//子级菜单定位
					subMenuL.setVisibility(View.VISIBLE); //设定子级菜单
					subMenuL.showAll(); //递归设定
				}
				if(subMenuR != null){
					//<==========?????==========>//子级菜单定位
					subMenuR.setVisibility(View.VISIBLE); //设定子级菜单
					subMenuR.showAll(); //递归设定
				}
			}
		}
	}
	
	public void hide(){//Cocoonshu √۞
		if(mListViews[0] == null || mListViews[1] == null){
			return;
		}

		
		
//		if(mListViews[0].getWidth() == 0){
//			mListViews[0].setVisibility(View.GONE);
//			mListViews[1].setVisibility(View.GONE);
//		}else{
			mListViews[0].setVisibility(View.INVISIBLE);
			mListViews[1].setVisibility(View.INVISIBLE);

			for(int i = 0;i<mListViews[0].getCount();i++){		
				if( (CSMenu)((HashMap<String, Object>)mAdapterL.getItem(i)).get("subMenu")!=null){
					//						((CSMenu)((HashMap<String, Object>)mAdapterL.getItem(4)).get("subMenu")).show();
					((CSMenu)((HashMap<String, Object>)mAdapterL.getItem(i)).get("subMenu")).hide(); 
				}}

//		}
		
		if(mListTitle[0] != null && mListTitle[1] != null ){
				mListTitle[0].setVisibility(View.INVISIBLE);
				mListTitle[1].setVisibility(View.INVISIBLE);
		}
		
		if(mListBottom[0] != null && mListBottom[1] != null ){
			mListBottom[0].setVisibility(View.INVISIBLE);
			mListBottom[1].setVisibility(View.INVISIBLE);
		}
		mIsShow = false;
	}
	
	public void hideAll(){//Cocoonshu √۞
		int size = 0;
				
		//隐藏子级菜单
		if(mAdapterL != null && mAdapterR != null){
			size = mAdapterL.getCount() < mAdapterR.getCount() ? mAdapterL.getCount() : mAdapterR.getCount();
			for (int i = 0; i < size; i++) {
				CSMenu subMenuL = ((CSMenu)((HashMap<String, Object>)mAdapterL.getItem(i)).get("subMenu"));
				CSMenu subMenuR = ((CSMenu)((HashMap<String, Object>)mAdapterR.getItem(i)).get("subMenu"));
				if(subMenuL != null){
					subMenuL.setVisibility(View.INVISIBLE); //设定子级菜单
					subMenuL.showAll(); //递归设定
				}
				if(subMenuR != null){
					subMenuR.setVisibility(View.INVISIBLE); //设定子级菜单
					subMenuR.showAll(); //递归设定
				}
			}
		}
		
		//隐藏root menu
		if(mListViews[0] == null || mListViews[1] == null){
			mListViews[0].setVisibility(View.GONE); //or GONE，INVISIBLE会不会拦截事件？？
			mListViews[1].setVisibility(View.GONE); //or GONE，INVISIBLE会不会拦截事件？？
		}
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public boolean touchEvent(MotionEvent event) {//WeiWang
		boolean state = false;
		boolean isTouchable = false;
		boolean isMenuTouch = true;
		CSMenu subMenu = null;

		int [] location0 = new int[2];
		int [] location1 = new int[2];
		mListViews[0].getLocationOnScreen(location0);
		mListViews[1].getLocationOnScreen(location1);


		if(mListViews[0] == null || mListViews[1] == null){
			return state;
		}else {
			//若控件隐藏，则不响应点击事件
			if(mListViews[0].getVisibility()==View.INVISIBLE || mListViews[1].getVisibility()==View.GONE){
				return false;
			}

			int count = mListViews[0].getCount();
			for(int i = 0;i<1;i++){	{
				if( (CSMenu)((HashMap<String, Object>)mAdapterL.getItem(i)).get("subMenu")!=null){
					subMenu = (CSMenu)((HashMap<String, Object>)mAdapterL.getItem(i)).get("subMenu");
					CSMenu pareantMenu  = subMenu.mParentMenu;
					try {
						state |= ((CSMenu)((HashMap<String, Object>)mAdapterL.getItem(i)).get("subMenu")).touchEvent(event); 
					} catch (Exception e) {
					}


					int subMenuX = subMenu.m_cur_lx;
					int subMenuY = subMenu.m_cur_ly;
					int subMenuWidth = subMenu.m_cur_width;
					int subMenuheight = subMenu.m_cur_height;

					int pareantMenuX = m_cur_lx;
					int pareantMenuY = m_cur_ly;
					int pareantWidth = m_cur_width;
					int pareantHeight = m_cur_height;
					if(subMenu.mParentMenu != null){
						pareantMenuX = subMenu.mParentMenu.m_cur_lx;
						pareantMenuY = subMenu.mParentMenu.m_cur_ly;
						pareantWidth = subMenu.mParentMenu.m_cur_width;
						pareantHeight = subMenu.mParentMenu.m_cur_height;
					}

					int x = (int) event.getX();
					int y = (int) event.getY();

					//菜单重叠时的处理--------------------------------------------------------------------------------------------------------------------------------
					if(m_is_3D){
						if(m_cur_width - (subMenuX - m_cur_lx)  > 0 && m_cur_height - (subMenuY - m_cur_ly) > 0 && event.getX() > 2 * subMenuX && event.getX() < 2 * subMenuX + 2 * (m_cur_width - (subMenuX - m_cur_lx)) && event.getY() > subMenuY && event.getY() < subMenuY + m_cur_height - (subMenuY - m_cur_ly) && subMenu.getVisibility() == View.VISIBLE){
							isTouchable = true;
						}
					}else{
						if(m_cur_width - (subMenuX - m_cur_lx)  > 0 && m_cur_height - (subMenuY - m_cur_ly) > 0 && event.getX() > subMenuX && event.getX() < subMenuX + m_cur_width - (subMenuX - m_cur_lx) && event.getY() > subMenuY && event.getY() < subMenuY + m_cur_height - (subMenuY - m_cur_ly) && subMenu.getVisibility() == View.VISIBLE){
							isTouchable = true;
						}
					}

					//					if (event.getAction() == MotionEvent.ACTION_UP && subMenu.getVisibility() == View.VISIBLE) {
					//						isMenuTouch = true;
					//					}
					//					
					//					if(subMenu.getVisibility() == View.INVISIBLE){
					//						isMenuTouch = true;
					//					}else if(!isTouchable && event.getAction() == MotionEvent.ACTION_UP) {
					//						isMenuTouch = true;
					//					}else {
					//						isMenuTouch = false;
					//					}

					//如果主菜单和子菜单重叠，点击重叠区域优先分发事件给子菜单
					if(isTouchable && isMenuTouch){

						if(m_is_3D){
							if(event.getX() > 2 * subMenuX && event.getX() < 2 * subMenuX + 2 * (m_cur_width - (subMenuX - m_cur_lx)) && event.getY() > subMenuY && event.getY() < subMenuY + m_cur_height - (subMenuY - m_cur_ly)){

								if (	   event.getX() >  subMenuX * 2
										&& event.getX() < 2 * ( subMenuX +  subMenuWidth)
										&& event.getY() > subMenuY 
										&& event.getY() < subMenuY +  subMenuheight +mListBottomHeight
										) {
									try {
										event.setLocation((event.getX() - 2* subMenuX)/2, event.getY()-subMenuY);
										state |= ((CSMenu)((HashMap<String, Object>)mAdapterL.getItem(i)).get("subMenu")).mListViews[0].dispatchTouchEvent(event);
										state |= ((CSMenu)((HashMap<String, Object>)mAdapterL.getItem(i)).get("subMenu")).mListViews[1].dispatchTouchEvent(event);
										event.setLocation((event.getX()*2 + 2* subMenuX), event.getY()+subMenuY);
									} catch (Exception e) {
										// TODO: handle exception
									}
								}else {
									if (event.getAction() == MotionEvent.ACTION_UP) {
										((CSMenu)((HashMap<String, Object>)mAdapterL.getItem(i)).get("subMenu")).mListViews[0].setPressed(false);
										((CSMenu)((HashMap<String, Object>)mAdapterL.getItem(i)).get("subMenu")).mListViews[1].setPressed(false);
									}
								}
							}
						}else{
							if(event.getX() > subMenuX && event.getX() < subMenuX + m_cur_width - (subMenuX - m_cur_lx) && event.getY() > subMenuY && event.getY() < subMenuY + m_cur_height - (subMenuY - m_cur_ly)){
								if (	   event.getX() >  subMenuX
										&& event.getX() < ( subMenuX +  subMenuWidth)
										&& event.getY() > subMenuY 
										&& event.getY() < subMenuY +  subMenuheight +mListBottomHeight
										) {
									try {
										event.getOrientation();
										event.setLocation(event.getX() - subMenuX, event.getY() - subMenuY);
										state |= ((CSMenu)((HashMap<String, Object>)mAdapterL.getItem(i)).get("subMenu")).mListViews[0].dispatchTouchEvent(event);
										state |= ((CSMenu)((HashMap<String, Object>)mAdapterL.getItem(i)).get("subMenu")).mListViews[1].dispatchTouchEvent(event);
										event.setLocation(event.getX() + subMenuX, event.getY() + subMenuY);
									} catch (Exception e) {
										// TODO: handle exception
									}
								}
								else {

									if (event.getAction() == MotionEvent.ACTION_UP) {
										((CSMenu)((HashMap<String, Object>)mAdapterL.getItem(i)).get("subMenu")).mListViews[0].setPressed(false);
										((CSMenu)((HashMap<String, Object>)mAdapterL.getItem(i)).get("subMenu")).mListViews[1].setPressed(false);
									}

								}
							}
						}

						//						return true;
					}
				}
			}
				}
					
				
			
			

			if(!isTouchable && isMenuTouch){
				if(m_is_3D){

					if (	   event.getX() >  m_cur_lx * 2
							&& event.getX() < 2 * ( m_cur_lx +  m_cur_width)
							&& event.getY() > m_cur_ly 
							&& event.getY() < m_cur_ly +  m_cur_height +mListBottomHeight 
							) {
						try {
							event.setLocation((event.getX() - 2* m_cur_lx)/2, event.getY()-m_cur_ly);
							state |= mListViews[0].dispatchTouchEvent(event);
							state |= mListViews[1].dispatchTouchEvent(event);
							
//							state |= subMenu.mParentMenu.mListViews[0].dispatchTouchEvent(event);
//							state |= subMenu.mParentMenu.mListViews[1].dispatchTouchEvent(event);
							event.setLocation((event.getX()*2 + 2* m_cur_lx), event.getY()+m_cur_ly);
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
					else {

						if (event.getAction() == MotionEvent.ACTION_UP) {
							 mListViews[0].setPressed(false);
							 mListViews[1].setPressed(false);
						}
					
					}
				}else{
					if (	   event.getX() >  m_cur_lx
							&& event.getX() < ( m_cur_lx +  m_cur_width)
							&& event.getY() > m_cur_ly 
							&& event.getY() < m_cur_ly +  m_cur_height +mListBottomHeight 
							) {
						try {
							event.getOrientation();
							event.setLocation(event.getX() - m_cur_lx, event.getY() - m_cur_ly);
							state |= mListViews[0].dispatchTouchEvent(event);
							state |= mListViews[1].dispatchTouchEvent(event);
							
//							state |= subMenu.mParentMenu.mListViews[0].dispatchTouchEvent(event);
//							state |= subMenu.mParentMenu.mListViews[1].dispatchTouchEvent(event);
							event.setLocation(event.getX() + m_cur_lx, event.getY() + m_cur_ly);
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
					else {
						mListViews[0].setPressed(false);
						 mListViews[1].setPressed(false);
					}
				}
				
//				return true;
			}
		}
		return state;
	}

	private void overlapTouchEvent(){
		
	}

	@Override
	public void addToLayout(ViewGroup layout) {//WeiWang~~
		
		if(mListViews[0] == null ||mListViews[1] == null){
			return;
		}
		if(mListTitle[0] != null && mListTitle[1] != null ){
				layout.addView(mListTitle[0]);
				layout.addView(mListTitle[1]);
		}
		
		if(mListBottom[0] != null && mListBottom[1] != null ){
			layout.addView(mListBottom[0]);
			layout.addView(mListBottom[1]);
		}
		
		layout.addView(mListViews[0]);
		layout.addView(mListViews[1]);
		
		
		int count = mListViews[0].getCount();
		for(int i = 0;i<count;i++){	

			if( ((HashMap<String, Object>)mAdapterL.getItem(i)).get("subMenu") != null){
				((CSMenu)((HashMap<String, Object>)mAdapterL.getItem(i)).get("subMenu")).addToLayout(layout); 

			}
		}


		if(m_is_3D){
			//under 3D mode


		}else{
			//under 2D mode

		}
	}
	
	////////////////////////////
	//private functions
	////////////////////////////
	private void initDataSources() {//Cocoonshu √۞
		if(mListViews[0] == null || mListViews[1] == null){
			return;
		}
		if(mArrayListL != null || mArrayListR != null){
			mArrayListL.clear();
			mArrayListR.clear();
		}
		mListTitle  = new TextView[2];
		mListTitle[0] = new TextView(m_context);
		mListTitle[1] = new TextView(m_context);
		
		mListBottom = new TextView[2];
		mListBottom[0] = new TextView(m_context);
		mListBottom[1] = new TextView(m_context);
		
//		mListViews[0].setPadding(1, 1, 1, 1);
//		mListViews[1].setPadding(1, 1, 1, 1);

		mArrayListL = new ArrayList<HashMap<String,Object>>();
		mArrayListR = new ArrayList<HashMap<String,Object>>();
		
		mCsMenuItem = new CSMenuItem();
		addItem(mCsMenuItem);
		
		mAdapterL =mAdapterR  = new CSMenuAdapter(m_context, mArrayListL, sIdOfItemLayout, new String[]{sKeyOfIcon, sKeyOfText, sKeyOfExtendIcon}, new int[]{sIdOfIcon, sIdOfText, sIdOfExtendIcon});
//		mAdapterR   = new CSMenuAdapter(m_context, mArrayListR, sIdOfItemLayout, new String[]{sKeyOfIcon, sKeyOfText, sKeyOfExtendIcon}, new int[]{sIdOfIcon, sIdOfText, sIdOfExtendIcon});
		
//		TextView[] mTextViews =new TextView[2];
//		mTextViews[0] = new TextView(m_context);
//		mTextViews[1] = new TextView(m_context);
//		mTextViews[0].setText(mTitle);
//		mTextViews[0].setTextScaleX(0.5f);
//		
//		mTextViews[1].setText(mTitle);
//		mTextViews[1].setTextScaleX(0.5f);
//		
//		try {
//			mListViews[0].addHeaderView(mTextViews[0]);
//			mListViews[1].addHeaderView(mTextViews[1]);
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
	
		mListViews[0].setAdapter(mAdapterL);
		mListViews[1].setAdapter(mAdapterR);
	}
	
	private void setAdapter(){
		mListViews[0].setAdapter(mAdapterL);
		mListViews[1].setAdapter(mAdapterR);
	}
	
	public void setExtentIcon(int id,int resId){
		mItemId = id;
		mResId = id;
		
	}
	public void setItemIcon(int id,int headIcon,int extendIcon){
		if(mListViews[0] == null || mListViews[1] == null){
			return ;
		}
		int count = mListViews[0].getCount();
		for(int i = 0;i<count;i++){	
			if(id == mListViews[0].getItemIdAtPosition(i+1)){
			mArrayListL.get(i).put(sKeyOfIcon, headIcon);
			mArrayListL.get(i).put(sKeyOfExtendIcon, extendIcon);	
			}
		}
	}
	/**
	 * 
	 * @author WH1107028
	 * @description 数据装载器
	 */
	class CSMenuAdapter extends SimpleAdapter{
		private boolean mDimension = true;

		public CSMenuAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
			super(context, data, resource, from, to);
			
			
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView listHeadIcon = new ImageView(m_context);
			ImageView listExtendIcon = new ImageView(m_context);
			TextView listContent = new TextView(m_context);
			
			if(convertView == null){
				convertView = LayoutInflater.from(m_context).inflate(R.layout.csmenu_item, null);
			}
			
			listHeadIcon = 	((ImageView)convertView.findViewById(R.id.imageViewIcon));
			listContent=	((TextView)convertView.findViewById(R.id.textViewContent));
			listExtendIcon =((ImageView)convertView.findViewById(R.id.imageViewExtendIcon));
			listContent.setTextSize(mTextSize);
			listContent.setTextColor(mTextColor);
			listContent.setText((CharSequence)mArrayListL.get(position).get(sKeyOfText));
			
			if(mArrayListL.get(position).get(sKeyOfIcon) != null){
				listHeadIcon.setBackgroundResource((Integer)mArrayListL.get(position).get(sKeyOfIcon));
			}
			if(mArrayListL.get(position).get(sKeyOfExtendIcon) != null){
			listExtendIcon.setBackgroundResource((Integer)mArrayListL.get(position).get(sKeyOfExtendIcon));
			}
			if(m_is_3D){
				//缩放Icon和text
			
				listHeadIcon.setScaleX(0.5f);
				listContent.setScaleX(0.5f);
				listExtendIcon.setScaleX(0.5f);

			}else{
				//缩放Icon
				listHeadIcon.setScaleX(1f);
				listContent.setScaleX(1f);
				listExtendIcon.setScaleX(1f);
			}
			
			return convertView;
		}
		@Deprecated
		public void setDimension(boolean is3D){
			int size = getCount();
			if(is3D == mDimension){
				return;
			}
		}
	}
	
	/**
	 * 菜单项
	 * @author WH1107028
	 * @description CSMenu的元素
	 */
	public class CSMenuItem{
		private long                    mID      = View.NO_ID;
		private int                     mPos     = -1;
		private HashMap<String, Object> mItemL   = null,
				                        mItemR   = null;
		private CSMenu                  mSubMenu = null;
		private CSMenu                  mParent  = null;
		
		public CSMenuItem() {
			mItemL = new HashMap<String, Object>();
			mItemR = new HashMap<String, Object>();
		}
		
		public CSMenuItem(int iconResidL, int iconResidR, CharSequence text, int extendIconResidL, int extendIconResidR, long id){
			mID   = id;
			
			mItemL = new HashMap<String, Object>();
			mItemL.put("id", mID);
			mItemL.put("position", mPos);
			mItemL.put("subMenu", mSubMenu);
			mItemL.put("parent", mParent);
			mItemL.put(CSMenu.sKeyOfIcon, iconResidL);
			mItemL.put(CSMenu.sKeyOfText, text);
			mItemL.put(CSMenu.sKeyOfExtendIcon, extendIconResidL);
			
			mItemR = new HashMap<String, Object>();
			mItemR.put("id", mID);
			mItemR.put("position", mPos);
			mItemR.put("subMenu", mSubMenu);
			mItemR.put("parent", mParent);
			mItemR.put(CSMenu.sKeyOfIcon, iconResidR);
			mItemR.put(CSMenu.sKeyOfText, text);
			mItemR.put(CSMenu.sKeyOfExtendIcon, extendIconResidR);
		}
		
		public long getID(){
			return mID;
		}
		
		public int getPosition(){
			return mPos;
		}
		
		public CSMenu getParent(){
			return mParent;
		}
		
		
		public void setPosition(int position){
			mPos = position;
			if(mItemL != null && mItemR != null){
				//因为mPos是int，传值类型，所以这里要重设定
				mItemL.put("position", mPos);
				mItemR.put("position", mPos);
			}
		}

		public void setParent(CSMenu parentView){
			mParent = parentView;
			mItemL.put("parent", mParent);
			mItemR.put("parent", mParent);
		}
		
		public void setSubMenu(CSMenu subMenu){
			mSubMenu = subMenu;
			mItemL.put("subMenu", mSubMenu);
			mItemR.put("subMenu", mSubMenu);
		}
		/**
		 * 添加子菜单
		 * @param subMenu
		 */
		public CSMenuItem addSubMenu(CSMenu subMenu){
			mSubMenu = subMenu;
			if(mSubMenu != null){
//				mSubMenu.setVisibility(View.INVISIBLE);//or View.GONE
				mSubMenu.mIsSubMenu = true;    //这个是必要的，CSMenu将根据这个标识来自动定位
				subMenu.mParentItem = this;    //为子菜单关联父级item
				subMenu.mParentMenu = mParent; //为子菜单关联父级menu
				mItemL.put("subMenu", mSubMenu);
				mItemL.put("parent", mParent);
			
				mItemR.put("subMenu", mSubMenu);
				mItemR.put("parent", mParent);
			}else {
				subMenu = mParentMenu;
			}
			return subMenu.mParentItem;
		}
	}
	
	public interface OnCSMenuItemClickListener{
		public void OnCSMenuItemClick(AdapterView<?> arg0, View view, int pos, long id);
	}
	
	public interface OnCSMenuItemSelectedListener{
		public void OnCSMenuItemSelected(AdapterView<?> arg0, View view, int pos, long id);
		public void OnCSMenuNothingSelected(AdapterView<?> arg0);
	}
	
	public interface OnCSMenuItemLongClickListener{
		public boolean OnCSMenuItemLongClick(AdapterView<?> arg0, View view, int pos, long id);
	}
	
	public interface OnCSMenuClickListener{
		public void OnCSMenuClick(long menuId);
	}
	
	public interface OnCSMenuScrollListener{
		public void OnCSMenuScroll(long menuId);
	}
	
	public boolean getMenuShow(){
		if(mListViews[0] != null && mListViews[1] != null){
			return false;
		}
		return mIsShow;
	}

	@Override
	public Object save() {
		// TODO Auto-generated method stub
		
		return mIsShow;
	}
	@Override
	public void restore(Object object) {
		// TODO Auto-generated method stub
		
	}
}

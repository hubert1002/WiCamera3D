package com.wistron.WiGallery;
 
import java.util.Timer;
import Utilities.CSStaticData;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.SurfaceHolder;
import android.view.ViewConfiguration;

public class WiGalleryOpenGLView extends GLSurfaceView implements OnGestureListener, OnScaleGestureListener
{
	enum moveStatus
	{
		eGently,
		eShift,
		eNone
	};
	
	public  Timer timer = null;
	private static final String TAG = "WiGalleryOpenGLView";
	private WiGalleryOpenGLRenderer render = null;
	private GestureDetector m_gesture = null;
	private ScaleGestureDetector m_scale_gesture = null;
	private float xDistanceOnScroll = 0; //整个滑动过程中，x轴向滑过的距离
	private float yDistanceOnScroll = 0; //整个滑动过程中，y轴向滑过的距离
	
	private float touchTurn = 0;
	private float touchTurnUp = 0;

	private float xDownPos = -1;
	private float yDownPos = -1;
	
	private float xpos = -1;
	private float ypos = -1;
	
	private static float xLongPressStart = -1;
	private static float yLongPressStart = -1;
	private static float xLongPressEnd = -1;
	private static float yLongPressEnd = -1;
	
	private moveStatus m_is_gently_move = moveStatus.eNone;
	private boolean m_allow_scroll = false;
	private boolean m_have_catcher = false; //开始处理事件
	
	private float ratio = 95;
	private boolean underMoving = false; //用于判断当前的各个子事件是否属于一个滑动事件
	
	public WiGalleryOpenGLView(Context context) 
	{
		super(context);
		render = new WiGalleryOpenGLRenderer(context);
		m_gesture = new GestureDetector(this);
		m_scale_gesture = new ScaleGestureDetector(context, this);
		m_gesture.setIsLongpressEnabled(true);
		setEGLConfigChooser(8, 8, 8, 8, 16, 4); // stencil
		//setEGLConfigChooser(true);
		setRenderer(render);
		
//		if(ViewConfiguration.getLongPressTimeout() > CSStaticData.g_long_press_timeout)
//		{
//			timer = new Timer();
//		}
	}
	
	



	/**
	 *  瑩攫岈璃揭燴
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{

		return true;
	}
	
	/**
	 * 获取页面数量
	 * @return
	 */
	public int getPageCount(){
		return 0;

	}
	
	/**
	 * 获取当前页码
	 * @return
	 */
	public int getCurrentPage(){
		return 0;

	}
	
	/**
	 * 获取文件数量
	 * @return
	 */
	public int getFileCount() {
		return 0;

	}
	/**
	 * 上翻一页
	 * @return
	 */
	public int previousPage(){
		WiGalleryOpenGLRenderer.m_element_group.startAnimation(CSStaticData.ANIMATION_TYPE.RIGHT_SHIFT);
		
		if (WiGalleryInterface.m_onGLMoveListener != null)
		{
			int curIndex = WiGalleryOpenGLRenderer.m_element_group.getCurIndex();
			int totalIndex = WiGalleryOpenGLRenderer.m_element_group.getTotalIndex();
			
			WiGalleryInterface.m_onGLMoveListener.setScrollBar(curIndex, totalIndex);
		}
		
		return 0;

	}
	
	/**
	 * 下翻一页
	 * @return
	 */
	public int nextPage(){
		WiGalleryOpenGLRenderer.m_element_group.startAnimation(CSStaticData.ANIMATION_TYPE.LEFT_SHIFT);
		if (WiGalleryInterface.m_onGLMoveListener != null)
		{
			int curIndex = WiGalleryOpenGLRenderer.m_element_group.getCurIndex();
			int totalIndex = WiGalleryOpenGLRenderer.m_element_group.getTotalIndex();
			
			WiGalleryInterface.m_onGLMoveListener.setScrollBar(curIndex, totalIndex);
		}
		
		return 0;
	}
	
	public int getSelectCount(){

		return -1;
	}
	
	/**
	 * 删除已选中的文件
	 */
	public void deleteItem(){

	}

	
	/**
	 * 设置景深偏移，在现有景深的基础上加减
	 * @param offset
	 */
	public void setDepthOffset(float offset){
		
	}
	
	/**
	 * GLSurfaceView是否已准备完毕
	 * @return
	 */
	public boolean isSurfaceReady(){
		return render.m_is_surface_ready;
	}
	
	/**
	 * 返回画布是否正在卷动
	 * @return
	 */
	public boolean isScrolling(){

		return false;
	}
	
	/**
	 * 终止GLSurfaceView，退出程序
	 */
	public void terminateSurface(){
		System.exit(0);
		getRender().m_data_manager.DeleteAll();
	}
	
	/**
	 * 选中单元
	 * @param event 单元位置
	 */
	public void selectedItem(int x, int y){

	}
	
	/**
	 * 设置选择模式
	 * @param isSelectable
	 */
	public void setState(boolean isSelectable){

	}
	
	/**
	 * 获取是否处于选择模式
	 */
	public boolean getState(){
		return false;

	}
	
	/**
	 * 获取2D、3D模式
	 */
	public boolean getDimension(){
		return render.getDimension();
	}
	
	/**
	 * 设置2D、3D模式
	 */
	public void setDimension(boolean is3D) {
		render.setDimension(is3D);
	}
	
	/**
	 * 获取render
	 */
	public WiGalleryOpenGLRenderer getRender(){
		return render;
	}

	
	
	//手势处理
	
	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		if (getRender().m_element_group == null 
				|| !getRender().m_element_group.m_is_drawing  )
			return true;
		
		if (!getRender().m_element_group.m_b_restore)
			return true;
		
		getRender().m_element_group.m_b_flip = true;
		
		m_scale_gesture.onTouchEvent(event);	
		
		if(m_have_catcher == false)
		{
			switch (event.getAction()) 
			{
			case MotionEvent.ACTION_DOWN:
				m_have_catcher = true;
				break;
			case MotionEvent.ACTION_MOVE:
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				return false;
			}
		}
		else
		{
			switch (event.getAction()) 
			{
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				m_have_catcher = false;
				break;
			}
		}
		
		if(event.getAction() != MotionEvent.ACTION_CANCEL)
		{
			if(ViewConfiguration.getLongPressTimeout() > CSStaticData.g_long_press_timeout)
			{
				//调用自定义onLongPress
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					
					if(xLongPressStart == -1 && yLongPressStart == -1)
					{
						m_allow_scroll = false;
						xLongPressStart = event.getX();
						yLongPressStart = event.getY();
//						timer.schedule(new TimerTask() 
//						{
//							@Override
//							public void run() 
//							{
//								//对于触发轻易的LongPress来说
//								//这个自定义的onLongPress和@Override onLongPress没有区别
//								//可以不纠结
//								if((xLongPressEnd <= xLongPressStart + 10 && xLongPressEnd >= xLongPressStart - 10)
//										&&
//										(yLongPressEnd <= yLongPressStart + 10 && yLongPressEnd >= yLongPressStart - 10)
//										){
//									onLongPress();
//								}
//								xLongPressStart = -1;
//								yLongPressStart = -1;
//							}
//						}, CSStaticData.g_long_press_timeout);
					}
					
				}

				xLongPressEnd = event.getX();
				yLongPressEnd = event.getY();
			}

			if(event.getAction() == MotionEvent.ACTION_UP)
			{
				onUp(event);
			}
			
			if(m_allow_scroll)
			{
				m_is_gently_move = moveStatus.eGently;

				float offset = (event.getX() - xpos) / ratio;
				xpos = event.getX();

				//~!
			}
		}
		
		
		return m_gesture.onTouchEvent(event);
	}
	
	public void onLongPress() 
	{
//		ELEM_STATUS status = WiGalleryOpenGLRenderer.m_element_group.getChoosedMode();
//		if (status == ELEM_STATUS.NORMAL_STATUS)
//			WiGalleryOpenGLRenderer.m_element_group.setChoosedMode(ELEM_STATUS.SELECTED_STATUS);
//		else 
//			WiGalleryOpenGLRenderer.m_element_group.setChoosedMode(ELEM_STATUS.NORMAL_STATUS);
	}
	
	public void onLongPress(MotionEvent e) {
		
		if(WiGalleryOpenGLRenderer.m_element_group != null && WiGalleryOpenGLRenderer.m_element_group.getTotalIndex() >= 0){
			ELEM_STATUS status = WiGalleryOpenGLRenderer.m_element_group.getChoosedMode();
			if (status == ELEM_STATUS.NORMAL_STATUS){
				WiGalleryOpenGLRenderer.m_element_group.setChoosedMode(ELEM_STATUS.SELECTED_STATUS);
			}
			else {
				WiGalleryOpenGLRenderer.m_element_group.setChoosedMode(ELEM_STATUS.NORMAL_STATUS);
			}
		}
	}
	
	/**
	 * 点到屏幕时
	 */
	@Override
	public boolean onDown(MotionEvent event) {	
		if(CSStaticData.DEBUG){
			Log.w(TAG, "[onDown]");
		}
		
		xDownPos = event.getX();
		yDownPos = event.getY();
        
		xpos = xDownPos;
		ypos = yDownPos;
		
		xDistanceOnScroll = xpos;
		yDistanceOnScroll = ypos;
		
		return true;
	}
	
	/**
	 * 离开
	 * @param event
	 * @return
	 */
	public boolean onUp(MotionEvent event){
		if(CSStaticData.DEBUG){
			Log.w(TAG, "[onUp]");
		}
		
		xDistanceOnScroll = event.getX() - xDistanceOnScroll;
		yDistanceOnScroll = event.getY() - yDistanceOnScroll;
		if(getRender() != null && getRender().m_element_group != null && underMoving ){
			if(CSStaticData.DEBUG){
				Log.w(TAG, "[onUp]此次滑动事件滑过的距离向量: " + xDistanceOnScroll + ", " + yDistanceOnScroll);
			}
			getRender().m_element_group.moveFinished(xDistanceOnScroll);
		}
		underMoving = false;
		
		getRender().m_element_group.m_b_flip = false;
		return false;
	}

	/**
	 * 滑动
	 * @param eventStart 滑动起点时的MotionEvent
	 * @param eventEnd   滑动终点时的MotionEvent
	 * @param distanceX  滑动的X轴速度
	 * @param distanceY  滑动的Y轴速度
	 * @detail 
	 *    |<------|------|------|------|------|------>|
	 *  -inf   -2000    -50     0     50    2000     inf
	 *      (高速)  (低速)   （点          击）       (低速)  (高速)
	 */
	@Override
	public boolean onFling(MotionEvent eventStart, MotionEvent eventEnd, float velocityX, float velocityY) {
		boolean state = false;
		
		if(getRender() != null && getRender().m_element_group != null){
			if (getRender().m_element_group.isMoving())
				return true;
		}

		
		if(CSStaticData.DEBUG){
			Log.w(TAG, "[onFling]滑动速度: " + velocityX);
		}
		underMoving = false;
		if (getRender().getCurrentDisplayedList() != null){//文件模式
			if(velocityX > CSStaticData.g_fling_speed_threshold){
				previousPage();
				state = true;
			}
			if(velocityX < -CSStaticData.g_fling_speed_threshold){
				nextPage();
				state = true;
			}
		}else{//文件夹模式
			if(velocityX > CSStaticData.g_fling_speed_threshold - 500){
				previousPage();
				state = true;
			}
			if(velocityX < -CSStaticData.g_fling_speed_threshold + 500){
				nextPage();
				state = true;
			}
		}
		
		if(getRender().m_element_group != null && (
				getRender().m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_LOCATION_1
				||
				getRender().m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_LOCATION_2
				||
				getRender().m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_LOCATION_3
				||
				getRender().m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_LOCATION_4
				)
				){
			if(Math.abs(velocityX) > 700 || Math.abs(velocityY) > 700){
				getRender().m_element_group.isSphereTouching(false);
				state |= getRender().m_element_group.onSphereFlingEvent(velocityX, velocityY);
			}
		}
		
		return state;
	}

	/**
	 * 滑动
	 * @param eventStart 滑动起点时的MotionEvent
	 * @param eventEnd   滑动终点时的MotionEvent
	 * @param distanceX  滑动的X轴距离
	 * @param distanceY  滑动的Y轴距离
	 */
	@Override
	public boolean onScroll(MotionEvent eventStart, MotionEvent eventEnd, float distanceX, float distanceY) {
		if(CSStaticData.DEBUG){
			Log.w(TAG, "[onScroll]");
		}

		if(getRender() != null && WiGalleryOpenGLRenderer.m_element_group != null){
			if (getRender().m_element_group.isMoving())
				return false;
		}
		
		underMoving = true;
		if(getRender() != null && getRender().m_element_group != null){
			if(eventEnd.getAction() == MotionEvent.ACTION_MOVE){
				getRender().m_element_group.isSphereTouching(true);
			}
			getRender().m_element_group.moving(-distanceX);
			getRender().m_element_group.onSphereTouchEvent(distanceX, distanceY);
		}
		
		return false;
	}

	//点击屏幕
	@Override
	public void onShowPress(MotionEvent event) {
		if(CSStaticData.DEBUG){
			Log.w(TAG, "[onShowPress]");
		}
	}

	//触击（单击）屏幕
	@Override
	public boolean onSingleTapUp(MotionEvent event) {
		if(CSStaticData.DEBUG){
			Log.w(TAG, "[onSingleTapUp]");
		}
		if(getRender() != null && getRender().m_element_group != null){
			return WiGalleryOpenGLRenderer.m_element_group.onTouchEvent(event.getX(), event.getY());
		}
		
		return false;
	}
	

	@Override
	public boolean onScale(ScaleGestureDetector detector) {
		if(CSStaticData.DEBUG){
			Log.w(TAG, "[onScale]");
		}
		if(getRender() != null && getRender().m_element_group != null
				&& 
				(getRender().m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_LOCATION_1
				||
				 getRender().m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_LOCATION_2
				||
				 getRender().m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_LOCATION_3
				||
				 getRender().m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_LOCATION_4
				)){
			getRender().m_element_group.onScale(detector.getScaleFactor());
			return true;
		}

		return false;
	}



	@Override
	public boolean onScaleBegin(ScaleGestureDetector detector) {
		// TODO Auto-generated method stub
		if(CSStaticData.DEBUG){
			Log.w(TAG, "[onScaleBegin]");
		}
		return true;
	}



	@Override
	public void onScaleEnd(ScaleGestureDetector detector) {
		// TODO Auto-generated method stub
		if(CSStaticData.DEBUG){
			Log.w(TAG, "[onScaleEnd]");
		}
	}





	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// TODO Auto-generated method stub
		
		super.surfaceChanged(holder, format, w, h);
		CSStaticData.g_is_surface_alive = true;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		super.surfaceCreated(holder);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if(CSStaticData.DEBUG){
			Log.w(TAG, "[surfaceDestroyed]GLThread.surfaceDestroyed");
		}
		
		CSStaticData.g_is_surface_alive = false;
		CSStaticData.g_surface_changed_called = false;
		if(CSStaticData.gStatusInfo == null){
			CSStaticData.gStatusInfo = new StatusInfo();
		}
		CSStaticData.gStatusInfo.saveGL();
		
		super.surfaceDestroyed(holder);
		
		if(WiGalleryOpenGLRenderer.mAsyncFileProvider != null){
			WiGalleryOpenGLRenderer.mAsyncFileProvider.stopImageLoadService();
			WiGalleryOpenGLRenderer.mAsyncFileProvider.stopVideoLoadService();
		}
		
		if(WiGalleryOpenGLRenderer.m_element_group != null){
			WiGalleryOpenGLRenderer.m_element_group.clear();
		}
		
		if(WiGalleryOpenGLRenderer.m_data_manager != null){
			WiGalleryOpenGLRenderer.m_data_manager.ReleaseAllTexture();
			WiGalleryOpenGLRenderer.m_data_manager.DelAllBitmap();
			WiGalleryOpenGLRenderer.m_data_manager.destoryDataManager();
		}
	}





	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		CSStaticData.g_is_surface_alive = false;
		super.onDetachedFromWindow();
	}
	

	
}
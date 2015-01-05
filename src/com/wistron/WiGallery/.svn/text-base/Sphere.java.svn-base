package com.wistron.WiGallery;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import Utilities.CSStaticData;
import android.util.Log;
import android.view.MotionEvent;

public class Sphere{
	private static final String TAG  = "Sphere";
	private static float PI          = 3.14159265f;
	private static float DTOR        = PI / 90;
	private static float SCALE_Limit = 0.05f;

	private static ByteBuffer  m_index_buff              = null;
	private static FloatBuffer m_vertex_buff             = null;
	private static ShortBuffer mVertexIndexBuffer        = null; //地球：
	private static FloatBuffer mVertexNormalBuffer       = null; //地球：
	private static FloatBuffer mVertexPositionBuffer     = null; //地球：计算出来的原始数据
	private static FloatBuffer mVertexCurPositionBuffer  = null; //地球：用于缩放显示的
	private static FloatBuffer mVertexTextureCoordBuffer = null; //地球：
	private static FloatBuffer mEmissionBuffer           = null; //地球：自发光颜色
	private static FloatBuffer mHaloVertexBuffer         = null; //光晕：顶点的原始数据
	private static FloatBuffer mHaloCurVertexBuffer      = null; //光晕：顶点，用于缩放显示的
	private static FloatBuffer mHaloCoordBuffer          = null; //光晕：贴图坐标
	private static ShortBuffer mHaloIndexBuffer          = null; //光晕：索引
	
	private List<Element>   mLandmarkerList  = new ArrayList<Element>();
	private OnScaleListener mOnScaleListener = null;
	private NewtonTheory    mDecelor         = new NewtonTheory();
	private boolean         mIsTouching      = false;
	
	private static float rot           =  0;      //自转角度
	private static float mRadio        =  0.8f;   //球半径
	private static float mTmpRadio     =  0.8f;
	private static float mRadioOffset  =  0.3f;   //贴图半径偏移
	private static float mScaleRate    =  0.8f;   //缩放比率
	private static float mMinScale     =  0.8f,   //缩放比率下限
			             mMaxScale     = 14.0f;   //缩放比率上限
	private static float mIconMinScale =  0.1f,   //图标缩放比率下限
			             mIconMaxScale =  1.0f;   //图标缩放比率上限
	private static float mHaloScale    =  1.0f;   //光晕比地球的放大倍数
 	
	
	private int     mSphereBG    = 0;  //背景贴图
	private float   mOldX        = 0, 
			        mOldY        = 0;
	private boolean mAllowScroll = false;
	private boolean mLaunchAnim  = true;
	private int     mDtheta      = 10, // 纬度
			        mDphi        = 10; // 经度
	private float   mXRot        = 0, 
			        mYRot        = 0, 
			        mZRot        = 0;
	

	public Sphere() {
		initVertex(mRadio);
		mDecelor.setScaleLimit(mMinScale, mMaxScale);
	}

	private void initVertex(float sphereRadio){
		int     latitudeBands    = 60;          //纬线
		int     longitudeBands   = 60;          //经线
		float   radio            = sphereRadio; //球体半径
		
		int     vpdItr           = 0;                                             //VertexPosData迭代器
		int     nmdItr           = 0;                                             //NormalData迭代器
		int     tcdItr           = 0;                                             //TextureCoordData迭代器
		int     idxItr           = 0;                                             //IndexData迭代器
		int     unitDataSize     = (latitudeBands + 1) * (longitudeBands + 1);
	 	float[] vertexPosData    = new float[3 * unitDataSize];                   //顶点坐标
		float[] normalData       = new float[3 * unitDataSize];                   //单位向量
		float[] textureCoordData = new float[2 * unitDataSize];                   //贴图坐标
		short[] indexData        = new short[6 * latitudeBands * longitudeBands]; //索引向量
		float[] haloVert         = null;                                          //光晕顶点
		float[] haloCoord        = null;                                          //光晕贴图
		short[] haloIndex        = null;                                          //光晕索引
		float[] emission         = new float[]{0.3f, 0.3f, 1.0f, 0.8f};           //自发光颜色
		
		//生成光晕坐标
		haloVert = new float[]{
				-(mRadio + 0.25f)*mHaloScale, -(mRadio + 0.25f)*mHaloScale, -0.15f,
				 (mRadio + 0.25f)*mHaloScale, -(mRadio + 0.25f)*mHaloScale, -0.15f,
				-(mRadio + 0.25f)*mHaloScale,  (mRadio + 0.25f)*mHaloScale, -0.15f,
				 (mRadio + 0.25f)*mHaloScale,  (mRadio + 0.25f)*mHaloScale, -0.15f
		};
		
		//生成光晕贴图坐标
		haloCoord = new float[]{
				0.0f,  0.0f,
				1.0f,  0.0f,
				0.0f,  1.0f,
				1.0f,  1.0f
		};
		
		//生成光晕索引
		haloIndex = new short[]{
				0, 1, 2, 3
		};
		
		//生成地球坐标
		for(int latNum = 0; latNum <= latitudeBands; latNum ++){ //纬线圈
			float theta    = (float) (latNum * Math.PI / latitudeBands);
			float sinTheta = (float) Math.sin(theta);
			float cosTheta = (float) Math.cos(theta);
			
			for(int longNum = 0; longNum <= longitudeBands; longNum ++){ //经线圈
				float phi    = (float) (longNum * 2 *Math.PI / longitudeBands);
				float sinPhi = (float) Math.sin(phi);
				float cosPhi = (float) Math.cos(phi);
				
				float x = cosPhi * sinTheta;
				float y = cosTheta;
				float z = sinPhi * sinTheta;
				float u = 1f - ((float)longNum / (float)longitudeBands);
				float v = 1f - ((float)latNum / (float)latitudeBands);
				
				normalData[nmdItr]       = x;         nmdItr ++;
				normalData[nmdItr]       = y;         nmdItr ++;
				normalData[nmdItr]       = z;         nmdItr ++;
				textureCoordData[tcdItr] = u;         tcdItr ++;
				textureCoordData[tcdItr] = v;         tcdItr ++;
				vertexPosData[vpdItr]    = radio * x; vpdItr ++;
				vertexPosData[vpdItr]    = radio * y; vpdItr ++;
				vertexPosData[vpdItr]    = radio * z; vpdItr ++;
			}
		}
		
		//生成地球索引
		for(int latNum = 0; latNum < latitudeBands; latNum ++){
			for(int longNum = 0; longNum < longitudeBands; longNum ++){
				int first  = (latNum * (longitudeBands + 1)) + longNum;
				int second = first + longitudeBands + 1;
				
				indexData[idxItr] = (short) first;        idxItr ++;
				indexData[idxItr] = (short) second;       idxItr ++;
				indexData[idxItr] = (short) (first + 1);  idxItr ++;
				
				indexData[idxItr] = (short) second;       idxItr ++;
				indexData[idxItr] = (short) (second + 1); idxItr ++;
				indexData[idxItr] = (short) (first + 1);  idxItr ++;
			}
		}
		
		//生成缓存
		if(mVertexIndexBuffer != null){
			mVertexIndexBuffer.clear();
		}
		if(mVertexNormalBuffer != null){
			mVertexNormalBuffer.clear();
		}
		if(mVertexPositionBuffer != null){
			mVertexPositionBuffer.clear();
		}
		if(mVertexTextureCoordBuffer != null){
			mVertexTextureCoordBuffer.clear();
		}
		if(mHaloIndexBuffer != null){
			mHaloIndexBuffer.clear();
		}
		if(mHaloVertexBuffer != null){
			mHaloVertexBuffer.clear();
		}
		if(mEmissionBuffer != null){
			mEmissionBuffer.clear();
		}
		mEmissionBuffer           = ResourceManager.makeFloatBuffer(emission);
		mHaloIndexBuffer          = ResourceManager.makeShortBuffer(haloIndex);
		mHaloCoordBuffer          = ResourceManager.makeFloatBuffer(haloCoord);
		mHaloVertexBuffer         = ResourceManager.makeFloatBuffer(haloVert);
		mVertexIndexBuffer        = ResourceManager.makeShortBuffer(indexData);
		mVertexNormalBuffer       = ResourceManager.makeFloatBuffer(normalData);
		mVertexPositionBuffer     = ResourceManager.makeFloatBuffer(vertexPosData);
		mVertexTextureCoordBuffer = ResourceManager.makeFloatBuffer(textureCoordData);
		
		//给出显示顶点
		if(mVertexCurPositionBuffer != null){
			mVertexCurPositionBuffer.clear();
			mVertexCurPositionBuffer = null;
		}
		if(mHaloCurVertexBuffer != null){
			mHaloCurVertexBuffer.clear();
			mHaloCurVertexBuffer = null;
		}
		System.gc();
		mVertexCurPositionBuffer = ResourceManager.makeFloatBuffer(vertexPosData);
		mHaloCurVertexBuffer = ResourceManager.makeFloatBuffer(haloVert);
		
		//缩放到最小
		onScale(1f);
	}
	
	

	/**
	 * 对球面进行缩放
	 * 
	 * @param scale 1 == 标准; < 1 缩小; > 1 放大
	 */
	public void onScale(float scale) {
		mScaleRate = scale;
		
		//限制缩放比
		mTmpRadio *= mScaleRate;
		if (mTmpRadio - mMinScale < 0) {
			mTmpRadio = mMinScale;
		}
		
		if(mTmpRadio - mMaxScale > 0){
			mTmpRadio = mMaxScale;
		}
		
		if(Math.abs(mTmpRadio - mRadio) < SCALE_Limit){
			return;
		}
		
		//设定缩放级别:一共4个级别
		if(mTmpRadio < (mMaxScale - mMinScale)*0.25 + mMinScale){ //不要写 tmpRadio >= mMinScale
			mDtheta = 10;
			mDphi   = 10;
		}else 
		if(mTmpRadio >= (mMaxScale - mMinScale)*0.25 + mMinScale && mRadio < (mMaxScale - mMinScale)*0.5 + mMinScale){
			mDtheta = 8;
			mDphi   = 8;
		}else
		if(mTmpRadio >= (mMaxScale - mMinScale)*0.5 + mMinScale && mRadio < (mMaxScale - mMinScale)*0.75 + mMinScale){
			mDtheta = 6;
			mDphi   = 6;
		}else
		if(mTmpRadio >= (mMaxScale - mMinScale)*0.75 + mMinScale){ //不要写 tmpRadio <= mMaxScale
			mDtheta = 4;
			mDphi   = 4;
		}
		
		//指定平滑缩放
		mDecelor.setSmoothScale(mRadio, mTmpRadio);
	
		
	}

	public boolean onFlingEvent(float velocityX, float velocityY) {
		
		//加速度变换器
		mDecelor.setStartVelocity(velocityY/(6*mRadio), 0);
		mDecelor.setStartVelocity(velocityX/(6*mRadio), 1);
		
		return true;
	}
	
	public boolean onTouchEvent(float xDistance, float yDistance) {

		mXRot -= (yDistance/6f)/mRadio; //使地球在任何倍数下都尽量紧跟手指的位置来滑动
		mYRot -= (xDistance/6f)/mRadio; //地球半径越大，滑动越慢，反之则滑动越快
		
		mXRot = mXRot % 360f;
		mYRot = mYRot % 360f;
		mZRot = mZRot % 360f;
		
		return true;
	}

	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mAllowScroll = true;
			mOldX = event.getX();
			mOldY = event.getY();
			break;

		case MotionEvent.ACTION_MOVE:
			mXRot += event.getX() - mOldX;
			mYRot += event.getY() - mOldY;
			mOldX = event.getX();
			mOldY = event.getY();
			break;

		case MotionEvent.ACTION_UP:
		default:
			mAllowScroll = false;
			break;
		}

		return true;
	}
	
	public void onDraw(GL10 gl, List<ElementList> list) {
		if(mLaunchAnim){ //触发开场动画
			onFlingEvent(6000, 0);
			mLaunchAnim = false;
		}
		
		if(CSStaticData.EARTH_AUTO_ROTATE){ //地球自转
			mYRot = (float) ((0.5f + mYRot) % 360f);
		}
		
		//加速度变换器
		mXRot = mXRot + mDecelor.getDecelerator(0);
		mYRot = mYRot + mDecelor.getDecelerator(1);
		mZRot = mZRot + mDecelor.getDecelerator(2);
		
		//缩放阻尼变换器
		float[] temp = new float[1];
		if(mDecelor.getSmoothScaleSin(temp)){
			if(temp != null || temp.length == 1){
				mRadio = temp[0];
				if(mOnScaleListener != null){
					mOnScaleListener.onScaleChanged(mRadio);
				}
			}
			sphereScale();
		}
		
		//限制mXRot旋转
		float includeAngle = mXRot % 360;
		if(includeAngle > 60){
			mXRot = 60;
		}
		if(includeAngle < -60){
			mXRot = -60;
		}
		
		//绘制
		CreateUnitSphere(gl, mDtheta, mDphi, mXRot, mYRot, mZRot);
		drawIcon(gl, list); // 测试
	}
	
	public GPSPointInfo getGPSPoint(double latitude, double longitude){
		return new GPSPointInfo(latitude, longitude, (mRadio + mRadioOffset)*(1f - 0.175f), new float[]{0, 0, 0});
	}

	private void drawIcon(GL10 gl, List<ElementList> list) {
		float scale = mScaleRate;
		
		if(scale - mIconMinScale < 0){
			scale = mIconMinScale;
		}
		if(scale - mIconMaxScale > 0){
			scale = mIconMaxScale;
		}
		
		gl.glRotatef(mXRot, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(mYRot, 0.0f, 1.0f, 0.0f);
		gl.glRotatef(mZRot, 0.0f, 0.0f, 1.0f); //修正Icon方向
		
		for(Element itr : mLandmarkerList){
			GPSPointInfo gps = getGPSPoint(itr.m_latitude, itr.m_longitude);
			
			gl.glPushMatrix();
			gl.glTranslatef(gps.getGPSPoint()[0], gps.getGPSPoint()[1], gps.getGPSPoint()[2]);
//			gl.glRotatef(gps.getZTheata(), 0f, 0f, 1f); //不能转Z轴，也不能把Z轴转为0
			gl.glRotatef(gps.getYTheata(), 0f, 1f, 0f);
			gl.glRotatef(gps.getXTheata(), 1f, 0f, 0f);
			gl.glScalef(scale * 0.175f, scale * 0.175f, scale * 0.175f);
			
			gl.glPushMatrix();
			gl.glRotatef(90f, 1.0f, 0.0f, 0.0f);
			itr.onDraw(gl, true);
			gl.glPopMatrix();
			gl.glPopMatrix();
		}
		
		///////GroupList.draw
		for (int i = 0; i < list.size(); i++)
		{
			ElementList elist = list.get(i);
			
			if (elist.getName().equalsIgnoreCase("Unknown"))
			{
				continue;
			}
			
			Element elem = elist.get(i);
			if(elem != null){
				double longitude = elem.m_longitude;
				double latitude = elem.m_latitude;
				float[] pos = GetPositionAndAngle(latitude, longitude);
				
				elist.moveTo(pos[0], pos[1], pos[2]);
				elist.setAngleTo(pos[3], pos[4], pos[5]);
				
				elist.onDraw(gl, MEDIA_VIEW.LEFT_VIEW);
			}
		}
		///////
	}
	
	public float[] GetPositionAndAngle(double latitude, double longitude)
	{
		float scale = mScaleRate;
		
		if(scale - mIconMinScale < 0){
			scale = mIconMinScale;
		}
		if(scale - mIconMaxScale > 0){
			scale = mIconMaxScale;
		}
		
		GPSPointInfo gps = getGPSPoint(latitude, longitude);
		
		float[] pos = {0,0,0, 0, 0, 0};
		
		pos[0] = gps.getGPSPoint()[0];
		pos[1] = gps.getGPSPoint()[1];
		pos[2] = gps.getGPSPoint()[2];
		
		pos[3] = gps.getXTheata();
		pos[4] = gps.getYTheata();
		pos[5] = gps.getZTheata();
		
		return pos;
	}
	
	static public void CreateUnitSphere(GL10 gl, int dtheta, int dphi, float xRot, float yRot, float zRot) {

		
		gl.glPushMatrix();
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, WiGalleryOpenGLRenderer.m_earth_overlay_id);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mHaloCurVertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mHaloCoordBuffer);
		gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, mHaloIndexBuffer.capacity(), GL10.GL_UNSIGNED_SHORT, mHaloIndexBuffer);
		
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

		// 旋转效果
		gl.glRotatef(xRot, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(yRot, 0.0f, 1.0f, 0.0f);
		gl.glRotatef(zRot, 0.0f, 0.0f, 1.0f);
		
		gl.glEnable(GL10.GL_DEPTH_TEST);
		//gl.glEnableClientState(GL_NORMAL_ARRAY); //允许激活法线数组
		//gl.glEnableClientState(GL_TEXTURE_COORD_ARRAY); //允许激活贴图数组
		gl.glBindTexture(GL10.GL_TEXTURE_2D, WiGalleryOpenGLRenderer.m_earth_background_id);
		//gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		//gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR); 
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexCurPositionBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mVertexTextureCoordBuffer);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, mVertexNormalBuffer); //设置法线数组，用于光照处理
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_EMISSION, mEmissionBuffer); //自发光
		gl.glDrawElements(GL10.GL_TRIANGLES, mVertexIndexBuffer.capacity(), GL10.GL_UNSIGNED_SHORT, mVertexIndexBuffer);

		gl.glPopMatrix();
		
	}
	

	public void sphereScale(){
		// 重刷半径
		if(mVertexCurPositionBuffer == null){
			mVertexCurPositionBuffer = FloatBuffer.allocate(mVertexPositionBuffer.capacity());
		}
		if(mHaloCurVertexBuffer == null){
			mHaloCurVertexBuffer = FloatBuffer.allocate(mHaloVertexBuffer.capacity());
		}
		for (int i = 0; i < mVertexPositionBuffer.capacity(); i++){
			mVertexCurPositionBuffer.put(i, mVertexPositionBuffer.get(i) * mRadio);
		}
		for (int i = 0; i < mHaloVertexBuffer.capacity(); i++){
			mHaloCurVertexBuffer.put(i, mHaloVertexBuffer.get(i) * mRadio);
		}
	}
	
	public void addLandmarker(Element elem){
		mLandmarkerList.add(elem);
	}
	
	public static float getMinScale() {
		return mMinScale;
	}

	public static float getMaxScale() {
		return mMaxScale;
	}

	public void clearLandmarker(){
		mLandmarkerList.clear();
	}
	
	public void setOnScaleListener(OnScaleListener listener){
		mOnScaleListener = listener;
	}
	
	public interface OnScaleListener{
		public void onScaleChanged(float rate);
	}

	public void isTouching(boolean touching) {
		mIsTouching = touching;
	}

	
}

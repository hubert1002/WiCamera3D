package com.wistron.WiGallery;

import java.nio.FloatBuffer;

import static android.opengl.GLES10.GL_MODELVIEW;
import static android.opengl.GLES10.glClearColor;
import static android.opengl.GLES11.*;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import com.wistron.WiGallery.WiGalleryInterface.onDataListListener;
import com.wistron.swpc.wicamera3dii.R;
import Utilities.CSStaticData;
import Utilities.CSStaticData.LIST_TYPE;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;

public class WiGalleryOpenGLRenderer implements GLSurfaceView.Renderer {
	private PhotoCube mCube;
    
	// group_none 模式下，图片的起始位置及偏移量
	private final float x_pos_offset_h = 0.9f;
	private final float y_pos_offset_h = -1.2f;
	private final float z_pos_offset_h = -0.7f;

	private final float x_pos_start_h = -1.2f;
	private final float y_pos_start_h = 0.5f;
	private final float z_pos_start_h = 0.5f;

	private final float x_pos_offset_v = 0.8f;
	private final float y_pos_offset_v = -1.1f;
	private final float z_pos_offset_v = -1.5f;

	private final float x_pos_start_v = -0.8f;
	private final float y_pos_start_v = 1.1f;
	private final float z_pos_start_v = -2.5f;

	//
	enum CrossRenderType {
		RL_RENDER, CROSS_RENDER, STENCIL_RENDER
	}

	public static Context m_context;

	private static float[] m_image_vers = { -11.0f, -7.0f, -20.0f, 11.0f,
			-7.0f, -20.0f, -11.0f, 7.0f, -20.0f, 11.0f, 7.0f, -20.0f, };

	private static float[] m_bg_nongroup_landscape_vers = { -11.6f, -7.0f,
			-20.0f, 11.6f, -7.0f, -20.0f, -11.6f, 7.0f, -20.0f, 11.6f, 7.0f,
			-20.0f, };

	private static float[] m_bg_nongroup_portrait_vers = { -2.7f, -4.5f,
			-10.0f, 2.7f, -4.5f, -10.0f, -2.7f, 4.5f, -10.0f, 2.7f, 4.5f,
			-10.0f, };

	private static final float[] m_image_coords = { 0, 1, 1, 1, 0, 0, 1, 0, };

	private FloatBuffer m_cover_buff = ResourceManager.makeFloatBuffer(m_image_vers);
	private FloatBuffer m_cover_nongroup_landscape_vers = ResourceManager.makeFloatBuffer(m_bg_nongroup_landscape_vers);
	private FloatBuffer m_cover_nongroup_portrait_vers = ResourceManager.makeFloatBuffer(m_bg_nongroup_portrait_vers);
	private FloatBuffer m_tex_buff = ResourceManager.makeFloatBuffer(m_image_coords);

	private float[] light_white = { 10.0f, 10.0f, 10.0f, 1.0f };
	private FloatBuffer m_light_white = ResourceManager.makeFloatBuffer(light_white);

	private float[] light_ambient = { 0.05f, 0.05f, 0.05f, 1.0f };
	private FloatBuffer m_light_ambient = ResourceManager.makeFloatBuffer(light_ambient);

	private float[] light_diffuse = { 0.5f, 0.5f, 0.5f, 1.0f };
	private FloatBuffer m_light_diffuse = ResourceManager.makeFloatBuffer(light_diffuse);

	private float[] light_pos = { 0.0f, 2.0f, 1.5f, 0.0f };
	private FloatBuffer m_light_pos = ResourceManager.makeFloatBuffer(light_pos);

	private int m_scr_width = 0;
	private int m_scr_height = 0;

	// 左右图交叉
	private FloatBuffer m_cross_render_vers = null;
	private int m_cross_render_texture_id = CSStaticData.INVALID_TEXTURE_ID;

	private CrossRenderType m_cross_render_type = CrossRenderType.STENCIL_RENDER;

	// Stencil交叉
	private FloatBuffer m_stencil_vers = null;

	//

	static public onDataListListener m_on_data_list_listener = null;
	static public ResourceManager m_resource_manager = new ResourceManager();
	static public DataManager m_data_manager = new DataManager();
	static public AsyncFileProvider mAsyncFileProvider = null;

	// 选中状态
	static public int m_zmap_select_gallery_none_id = 0;
	static public int m_zmap_select_gallery_select_id = 0;
	static public int m_zmap_select_grouping_none_id = 0;
	static public int m_zmap_select_grouping_on_id = 0;
	static public int m_zmap_videoindicator_id = 0;
	static public int m_default_icon = 0;
	static public int m_default_file_icon = 0;
	static public int m_default_video_icon = 0;
	static public int m_zmap_shade = 0;
	static public int m_indicator_3d = 0;
	static public int m_indicator_favorite = 0;
	static public int m_earth_background_id = 0;
	static public int m_earth_overlay_id = 0;
	
	//
	private int m_background_id = 0;
	private int m_bg_nongroup_landscape_id = 0;
	private int m_bg_nongroup_portrait_id = 0;

	public static boolean m_is_3D_device = false;

	public boolean m_is_surface_ready = false;

	public static boolean m_b_del_all_texture = false;

	//
	static public ElementGroup m_element_group = null;

	public static boolean m_b_horizontal_screen = false;
	// ///
	public static Bitmap m_label_bitmap = null;
	public static Bitmap m_location_bitmap = null;
	public static Bitmap m_date_bitmap = null;
	// ///

	// 多选时确定当前选择的媒体类型
	private static CSStaticData.MEDIA_META_TYPE m_media_meta_type = CSStaticData.MEDIA_META_TYPE.ALL_MEDIA_TYPE;

	// 3D 物体拾取使用
	public static float[] g_model = new float[16];
	public static float[] g_proj = new float[16];
	public static int[] g_viewArray = new int[4];

	//

	public WiGalleryOpenGLRenderer(Context context) {
		m_context = context;
		
		mCube = new PhotoCube(context);
		
		mAsyncFileProvider = new AsyncFileProvider(context);
		mAsyncFileProvider.scanMediaLib();

		m_location_bitmap = BitmapFactory.decodeResource( m_context.getResources(), R.drawable.group_location_icon);
		m_date_bitmap = BitmapFactory.decodeResource(m_context.getResources(), R.drawable.group_date_icon);
		m_label_bitmap = Bitmap.createBitmap(256, 32, Bitmap.Config.ALPHA_8);
	}

	public void setMediaMetaType(CSStaticData.MEDIA_META_TYPE type) {
		m_media_meta_type = type;
	}

	static public CSStaticData.MEDIA_META_TYPE getMediaMetaType() {
		return m_media_meta_type;
	}

	private void delCommonTexture(){
		if (glIsTexture(m_zmap_shade))	m_resource_manager.DeleteTextureID(m_zmap_shade);
		if (glIsTexture(m_zmap_select_gallery_none_id))	m_resource_manager.DeleteTextureID(m_zmap_select_gallery_none_id);
		if (glIsTexture(m_zmap_videoindicator_id))	m_resource_manager.DeleteTextureID(m_zmap_videoindicator_id);
	
		if (glIsTexture(m_zmap_select_gallery_select_id))	m_resource_manager.DeleteTextureID(m_zmap_select_gallery_select_id);
		if (glIsTexture(m_background_id))	m_resource_manager.DeleteTextureID(m_background_id);
		if (glIsTexture(m_default_video_icon))	m_resource_manager.DeleteTextureID(m_default_video_icon);
		if (glIsTexture(m_default_icon))	m_resource_manager.DeleteTextureID(m_default_icon);
		if (glIsTexture(m_default_file_icon))	m_resource_manager.DeleteTextureID(m_default_file_icon);
		if (glIsTexture(m_indicator_3d))	m_resource_manager.DeleteTextureID(m_indicator_3d);
		if (glIsTexture(m_indicator_favorite))	m_resource_manager.DeleteTextureID(m_indicator_favorite);
		if (glIsTexture(m_bg_nongroup_portrait_id))	m_resource_manager.DeleteTextureID(m_bg_nongroup_portrait_id);
		if (glIsTexture(m_bg_nongroup_landscape_id))	m_resource_manager.DeleteTextureID(m_bg_nongroup_landscape_id);
		if (glIsTexture(m_earth_background_id))	m_resource_manager.DeleteTextureID(m_earth_background_id);
		
	}
	
	private void loadCommonTexture() {
		if (m_zmap_shade == 0)
		{
			Bitmap  bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
							m_context.getResources(), R.drawable.zmap_shade),
							256, 256, true);
			m_zmap_shade = m_resource_manager.GetTextureID(bm);
			bm.recycle();
		}
		else {
			boolean bValid = glIsTexture(m_zmap_shade);

			if (!bValid) {
				m_resource_manager.DeleteTextureID(m_zmap_shade);
				
				Bitmap bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(m_context.getResources(),
								R.drawable.zmap_shade), 256, 256, true);
				m_zmap_shade = m_resource_manager
						.GetTextureID(bm);
				bm.recycle();
			}
		}

		if (m_zmap_select_gallery_none_id == 0)
		{
			Bitmap bm = Bitmap.createScaledBitmap(
					BitmapFactory.decodeResource(m_context.getResources(),
							R.drawable.gallery_multi_select_deselect_icon),256, 256, true);
			m_zmap_select_gallery_none_id = m_resource_manager.GetTextureID(bm);
			bm.recycle();
		}
		else {
			boolean bValid = glIsTexture(m_zmap_select_gallery_none_id);

			if (!bValid) {
				m_resource_manager.DeleteTextureID(m_zmap_select_gallery_none_id);
				
				Bitmap bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(m_context.getResources(),R.drawable.gallery_multi_select_deselect_icon),
						256, 256, true);
				m_zmap_select_gallery_none_id = m_resource_manager.GetTextureID(bm);
				bm.recycle();
			}
		}

		if (m_zmap_videoindicator_id == 0)
		{
			Bitmap bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
					m_context.getResources(),R.drawable.group_video_icon), 256, 256, true);
			m_zmap_videoindicator_id = m_resource_manager.GetTextureID(bm);
			bm.recycle();
		}
		else {
			boolean bValid = glIsTexture(m_zmap_videoindicator_id);

			if (!bValid) {
				m_resource_manager.DeleteTextureID(m_zmap_videoindicator_id);
				
				Bitmap bm =Bitmap.createScaledBitmap(BitmapFactory.decodeResource(m_context.getResources(),
								R.drawable.group_video_icon), 256, 256,true);
				
				m_zmap_videoindicator_id = m_resource_manager.GetTextureID(bm);
				bm.recycle();
			}
		}

		if (m_zmap_select_gallery_select_id == 0)
		{
			Bitmap bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
							m_context.getResources(),R.drawable.gallery_multi_select_select_icon),256, 256, true);
			
			m_zmap_select_gallery_select_id = m_resource_manager
					.GetTextureID(bm);
			bm.recycle();
		}
		else {
			boolean bValid = glIsTexture(m_zmap_select_gallery_select_id);

			if (!bValid) {
				m_resource_manager.DeleteTextureID(m_zmap_select_gallery_select_id);
				Bitmap bm = Bitmap.createScaledBitmap(
						BitmapFactory.decodeResource(m_context.getResources(),R.drawable.gallery_multi_select_select_icon),
						256, 256, true);
				
				m_zmap_select_gallery_select_id = m_resource_manager.GetTextureID(bm);
				bm.recycle();
			}
		}

		if (m_background_id == CSStaticData.INVALID_TEXTURE_ID)
		{
			Bitmap bm  = null;
			try{
				bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
						m_context.getResources(), R.drawable.background),512, 512, true);
			}catch(OutOfMemoryError exp){
				exp.printStackTrace();
				bm = null;
			}
			m_background_id = m_resource_manager.GetTextureID(bm);
			
			if(bm != null){
				bm.recycle();
			}
		}
		else {
			boolean bValid = glIsTexture(m_background_id);

			if (!bValid) {
				m_resource_manager.DeleteTextureID(m_background_id);
				
				Bitmap bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
						m_context.getResources(), R.drawable.background),512, 512, true);
				
				m_background_id = m_resource_manager.GetTextureID(bm);
				bm.recycle();
			}
		}

		if (m_default_video_icon == 0)
		{
			Bitmap bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
							m_context.getResources(),
							R.drawable.default_video_icon), 256, 256, true);
			
			m_default_video_icon = m_resource_manager.GetTextureID(bm);
			bm.recycle();
		}
		else {
			boolean bValid = glIsTexture(m_default_video_icon);

			if (!bValid) {
				m_resource_manager.DeleteTextureID(m_default_video_icon);
				
				Bitmap bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
						m_context.getResources(),R.drawable.default_video_icon), 256, 256, true);
				
				m_default_video_icon = m_resource_manager
						.GetTextureID(bm);
				
				bm.recycle();
			}
		}

		if (m_default_icon == 0)
		{
			Bitmap bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
							m_context.getResources(), R.drawable.default_icon),256, 256, true);
			m_default_icon = m_resource_manager.GetTextureID(bm);
			bm.recycle();
		}
		else {
			boolean bValid = glIsTexture(m_default_icon);

			if (!bValid) {
				m_resource_manager.DeleteTextureID(m_default_icon);
				
				Bitmap bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
								m_context.getResources(),R.drawable.default_icon), 256, 256, true);
				
				m_default_icon = m_resource_manager.GetTextureID(bm);
				bm.recycle();
			}
		}

		if (m_default_file_icon == 0)
		{
			Bitmap bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
							m_context.getResources(),R.drawable.default_file_icon), 256, 256, true);
			m_default_file_icon = m_resource_manager.GetTextureID(bm);
			bm.recycle();
		}
		else {
			boolean bValid = glIsTexture(m_default_file_icon);

			if (!bValid) {
				m_resource_manager.DeleteTextureID(m_default_file_icon);
				
				Bitmap bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
						m_context.getResources(),R.drawable.default_file_icon), 256, 256, true);
				
				m_default_file_icon = m_resource_manager.GetTextureID(bm);
				bm.recycle();
			}
		}

		if (m_indicator_3d == 0)
		{
			Bitmap bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
							m_context.getResources(),R.drawable.group_3d_image_icon), 64, 64, true);
			
			m_indicator_3d = m_resource_manager.GetTextureID(bm);
			
			bm.recycle();
		}
		else {
			boolean bValid = glIsTexture(m_indicator_3d);

			if (!bValid) {
				m_resource_manager.DeleteTextureID(m_indicator_3d);
				Bitmap bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
						m_context.getResources(),R.drawable.group_3d_image_icon), 64, 64, true);
		
				m_indicator_3d = m_resource_manager.GetTextureID(bm);
				
				bm.recycle();
			}
		}

		if (m_indicator_favorite == 0)
		{
			Bitmap bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
							m_context.getResources(),R.drawable.group_my_favorite_icon), 64, 64, true);
			
			m_indicator_favorite = m_resource_manager.GetTextureID(bm);
			
			bm.recycle();
		}
		else {
			boolean bValid = glIsTexture(m_indicator_favorite);

			if (!bValid) {
				m_resource_manager.DeleteTextureID(m_indicator_favorite);
				Bitmap bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
						m_context.getResources(),R.drawable.group_my_favorite_icon), 64, 64, true);
		
				m_indicator_favorite = m_resource_manager.GetTextureID(bm);
				
				bm.recycle();
			}
		}

		if (m_bg_nongroup_portrait_id == 0)
		{
			Bitmap bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
							m_context.getResources(),R.drawable.bg_nongroup_portrait), 512, 1024, true);
			
			m_bg_nongroup_portrait_id = m_resource_manager.GetTextureID(bm);
			
			bm.recycle();
		}
		else {
			boolean bValid = glIsTexture(m_bg_nongroup_portrait_id);

			if (!bValid) {
				m_resource_manager.DeleteTextureID(m_bg_nongroup_portrait_id);
				Bitmap bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
						m_context.getResources(),R.drawable.bg_nongroup_portrait), 512, 1024, true);
		
				m_bg_nongroup_portrait_id = m_resource_manager.GetTextureID(bm);
				
				bm.recycle();
			}
		}

		if (m_bg_nongroup_landscape_id == 0)
		{
			Bitmap  bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(m_context.getResources(),
							R.drawable.bg_nongroup_landscape), 1024,512, true);
			
			m_bg_nongroup_landscape_id = m_resource_manager.GetTextureID(bm);
			
			bm.recycle();
		}
		else {
			boolean bValid = glIsTexture(m_bg_nongroup_landscape_id);

			if (!bValid) {
				m_resource_manager.DeleteTextureID(m_bg_nongroup_landscape_id);
				Bitmap  bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(m_context.getResources(),
						R.drawable.bg_nongroup_landscape), 1024,512, true);
		
				m_bg_nongroup_landscape_id = m_resource_manager.GetTextureID(bm);
				
				bm.recycle();
			}
		}
		
		if(m_earth_background_id == 0)
		{
			Bitmap bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(m_context.getResources(),
							R.drawable.earth_512x512),512, 512, true);
			
			m_earth_background_id = m_resource_manager.GetTextureID(bm);
			
			bm.recycle();
		}
		else {
			boolean bValid = glIsTexture(m_earth_background_id);
			
			if(!bValid) {
				m_resource_manager.DeleteTextureID(m_earth_background_id);
				Bitmap bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(m_context.getResources(),
						R.drawable.earth_512x512),512, 512, true);
		
				m_earth_background_id = m_resource_manager.GetTextureID(bm);
				
				bm.recycle();
			}
		}
		//m_earth_overlay_id
		if(m_earth_overlay_id == 0)
		{
			Bitmap bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(m_context.getResources(),
							R.drawable.earth_overlay),256, 256, true);
			
			m_earth_overlay_id = m_resource_manager.GetTextureID(bm);
			
			bm.recycle();
		}
		else {
			boolean bValid = glIsTexture(m_earth_overlay_id);
			
			if(!bValid) {
				m_resource_manager.DeleteTextureID(m_earth_overlay_id);
				Bitmap bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(m_context.getResources(),
						R.drawable.earth_overlay),256, 256, true);
		
				m_earth_overlay_id = m_resource_manager.GetTextureID(bm);
				
				bm.recycle();
			}
		}
	}

	private void initOpenGL(GL10 gl) {
		gl.glDisable(GL_SCISSOR_TEST);
//		gl.glDisable(GL_SCISSOR_BOX);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glEnable(GL_SCISSOR_TEST);
//		gl.glEnable(GL_SCISSOR_BOX);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glEnable(GL10.GL_TEXTURE_2D);

		gl.glDepthFunc(GL10.GL_LEQUAL);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

		gl.glEnable(GL10.GL_DEPTH_TEST);


		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		Log.w("WiGalleryRender", "[onSurfaceCreated]GL 创建");
		m_resource_manager.SetGL10(gl);
		m_is_surface_ready = false;
		
		initOpenGL(gl);

		loadCommonTexture();

		m_on_data_list_listener = new onDataListListener() {

			@Override
			public void onDataFillCompleted() {
				// TODO Auto-generated method stub
				if(CSStaticData.DEBUG){
					Log.w("WiGalleryRender", "[onDataFillCompleted]初始数据加载完毕，回调");
				}

				if (m_element_group == null)
				{
					if(CSStaticData.DEBUG){
						Log.e("Render", "m_element_group == null");
					}
					m_element_group = new ElementGroup();						
				}
				
				if (CSStaticData.gStatusInfo != null)
				{
					if(CSStaticData.DEBUG){
						Log.e("Render", "RestoreGL");
					}
					CSStaticData.gStatusInfo.restoreGL();
				}
				else
				{
					if(CSStaticData.DEBUG){
						Log.e("Render", "setType to LIST_NONE");
					}
					m_element_group.setType(CSStaticData.LIST_TYPE.LIST_NONE,false);
				}
				
				if(CSStaticData.DEBUG){
					Log.w("WiGalleryRender", "[onDataFillCompleted][初始数据加载完毕，回调完成]");
				}
			}
		};
		
		if (!CSStaticData.LOAD_COMPLETED) {
			//FIXME !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		} else {
			Log.w("Render", "[onDataFillCompleted]初始数据加载完毕，非回调");
			if (m_element_group == null)
			{
				Log.e("Render", "m_element_group == null");
				m_element_group = new ElementGroup();
			}
			
			if (CSStaticData.gStatusInfo != null)
			{
				CSStaticData.gStatusInfo.restoreGL();
			}
			else
			{
				m_element_group.setType(CSStaticData.LIST_TYPE.LIST_NONE,false);
			}
		}

		mAsyncFileProvider.launchImageLoadService();
		mAsyncFileProvider.launchVideoLoadService();
		
		if (CSStaticData.DEBUG) {
			Log.w("WiGalleryRender", "[onSurfaceCreated] completed-------------");
		}
	}

	public void onDrawFrame(GL10 gl) {
		
		if (m_element_group == null) {
			return;
		}

		if (m_cover_buff != null)
			m_cover_buff.clear();

		m_resource_manager.SetGL10(gl);

		// 删除无效的纹理
		m_resource_manager.DeleteNeedDeleteTextureID();

		// 删除无效的Element
		m_data_manager.DeleteNeedDeleteElement();

		// 加载纹理
		Element elem = m_data_manager.GetNeedLoadTextureFile(0);
		if (elem != null) {
			elem.loadTexture(gl);
		}

        gl.glDisable(GL10.GL_STENCIL_TEST);
        gl.glClearStencil(0);
        gl.glDisable(GL_SCISSOR_TEST);
//		gl.glDisable(GL_SCISSOR_BOX);
        gl.glClear(GL10.GL_STENCIL_BUFFER_BIT);
        gl.glEnable(GL_SCISSOR_TEST);
//		gl.glEnable(GL_SCISSOR_BOX);
        
		if (m_is_3D_device) {
			if (m_cross_render_type == CrossRenderType.CROSS_RENDER)
				D3CrossRender(gl);
			else if (m_cross_render_type == CrossRenderType.RL_RENDER)
				D3RLRender(gl);
			else
				D3StencilRender(gl);
		} else {
			D2Render(gl);
		}

		// 视角恢复为单屏模式， 获取矩阵数据
		resetView(gl);
		int[] bits = new int[16];

		gl.glGetIntegerv(GL11.GL_MODELVIEW_MATRIX_FLOAT_AS_INT_BITS_OES, bits, 0);
		for (int i = 0; i < bits.length; i++) {
			g_model[i] = Float.intBitsToFloat(bits[i]);
		}

		gl.glGetIntegerv(GL11.GL_PROJECTION_MATRIX_FLOAT_AS_INT_BITS_OES, bits, 0);
		for (int i = 0; i < bits.length; i++) {
			g_proj[i] = Float.intBitsToFloat(bits[i]);
		}

		gl.glGetIntegerv(GL11.GL_VIEWPORT, g_viewArray, 0);
		// ///////////////////////////////////////////////////////////////////////////////////

		try {
			Thread.sleep(5);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		
		if(CSStaticData.g_surface_changed_called){
			return;
		}
		
		if (CSStaticData.DEBUG) {
			Log.w("WiGalleryRender", "[onSurfaceChanged]");
			Log.w("WiGalleryRender", "width: " + width + "   height: " + height);
		}

		WiGalleryActivity.mUIHandler.sendEmptyMessage(WiGalleryActivity.HANDLE_APPLICATION_LAUNCHED);
		m_is_surface_ready = false;
		CSStaticData.g_surface_changed_called = true;
		
		m_scr_width = width;
		m_scr_height = height;

		CSStaticData.screen_2d_width = m_scr_width;
		CSStaticData.screen_2d_height = m_scr_height;

		//m_data_manager.ReleaseAllTexture();
		m_is_surface_ready = true;

		if (m_scr_width > m_scr_height) {
			// 横屏
			m_b_horizontal_screen = true;
			CSStaticData.group_none_row_num = 2;

			CSStaticData.x_list_pos_offset = x_pos_offset_h;
			CSStaticData.y_list_pos_offset = y_pos_offset_h;
			CSStaticData.z_list_pos_offset = z_pos_offset_h;

			CSStaticData.x_list_pos_start = x_pos_start_h;
			CSStaticData.y_list_pos_start = y_pos_start_h;
			CSStaticData.z_list_pos_start = z_pos_start_h;
		} else {
			// 竖屏
			m_b_horizontal_screen = false;
			CSStaticData.group_none_row_num = 3;

			CSStaticData.x_list_pos_offset = x_pos_offset_v;
			CSStaticData.y_list_pos_offset = y_pos_offset_v;
			CSStaticData.z_list_pos_offset = z_pos_offset_v;

			CSStaticData.x_list_pos_start = x_pos_start_v;
			CSStaticData.y_list_pos_start = y_pos_start_v;
			CSStaticData.z_list_pos_start = z_pos_start_v;
		}

		if (m_cross_render_type == CrossRenderType.CROSS_RENDER) {
			float[] vers = { 0, m_scr_height, 0, m_scr_width, m_scr_height, 0,
					0, 0, 0, m_scr_width, 0, 0, };

			if (m_cross_render_vers != null)
				m_cross_render_vers.clear();

			m_cross_render_vers = ResourceManager.makeFloatBuffer(vers);

			if (m_b_horizontal_screen) {

				if (m_cross_render_texture_id == CSStaticData.INVALID_TEXTURE_ID) {
					Bitmap bitmap = Bitmap.createBitmap(m_scr_width, m_scr_height, Config.RGB_565);

					// 交叉显示时，使用的纹理
					// 左右屏数据交叉处理
					bitmap.eraseColor(0);
					m_cross_render_texture_id = m_resource_manager.GetTextureID(bitmap);

					bitmap.recycle();
				}
			}

		} else if (m_cross_render_type == CrossRenderType.STENCIL_RENDER) {

			int newW = width;
			int newH = height;

			float[] stencil_vertexArray = new float[(int) newW * 3];

			float inc = 0;
			for (int i = 0; i < newW; i++) {
				if (i % 2 == 0) {
					stencil_vertexArray[3 * i] = inc;
					stencil_vertexArray[3 * i + 1] = 0;
					stencil_vertexArray[3 * i + 2] = 0f;
				} else {
					stencil_vertexArray[3 * i] = inc;
					stencil_vertexArray[3 * i + 1] = newH - 1;
					stencil_vertexArray[3 * i + 2] = 0f;

					inc = inc + 2f;
				}
			}

			if (m_stencil_vers != null)
				m_stencil_vers.clear();

			m_stencil_vers = ResourceManager.makeFloatBuffer(stencil_vertexArray);
			stencil_vertexArray = null;
			
			float[] vers = { 0, newH, 0, newW, newH, 0, 0, 0, 0, newW, 0, 0, };

			if (m_cross_render_vers != null)
				m_cross_render_vers.clear();

			m_cross_render_vers = ResourceManager.makeFloatBuffer(vers);
		}



		mAsyncFileProvider.launchImageLoadService();
		mAsyncFileProvider.launchVideoLoadService();
		
		if (CSStaticData.gStatusInfo != null)
		{
			CSStaticData.gStatusInfo.restoreGL();
		}
		
		if (m_element_group != null)
		{
			// 临时处理， 主要解决各个设备之间线程生命周期不一致的问题，
			if (m_element_group.getType() == LIST_TYPE.LIST_NONE)
			{
				m_element_group.refreshPosition(m_element_group.getIndex());
			}
		}
        
		if (CSStaticData.DEBUG) {
			Log.w("WiGalleryRender", "[onSurfaceChanged] completed-------------");
		}
	}

	public void onSurfaceDestroyed(GL10 gl) {
		Log.w("WiGalleryRender", "[onSurfaceViewDestroyed]GL 销毁");
		mAsyncFileProvider.stopImageLoadService();
		mAsyncFileProvider.stopVideoLoadService();
		m_is_surface_ready = false;
		m_scr_width = 0;
		m_scr_height = 0;
		WiGalleryOpenGLRenderer.m_data_manager.ReleaseAllTexture();
	}
	
	

	public void setDimension(boolean is3D) {
		m_is_3D_device = is3D;
	}

	private void D3RLRender(GL10 gl) {
		m_element_group.calcPosition();
		// ////////////////////////////////////////////////////////////////
		// 左视图
		gl.glDisable(GL_SCISSOR_TEST);
//		gl.glDisable(GL_SCISSOR_BOX);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glEnable(GL_SCISSOR_TEST);
//		gl.glEnable(GL_SCISSOR_BOX);

		gl.glViewport(0, 0, m_scr_width / 2, m_scr_height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();

		GLU.gluPerspective(gl, 30, (float) m_scr_width / m_scr_height, 0.01f,
				100);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glEnable(GL10.GL_BLEND);
		gl.glLoadIdentity();

		// /
		if (m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_NONE
				|| m_element_group.bEnterGroup() == true) {
			GLU.gluLookAt(gl, CSStaticData.eye_position_none[0] - CSStaticData.g_debug_varient,
					CSStaticData.eye_position_none[1],
					CSStaticData.eye_position_none[2],
					CSStaticData.eye_center_none[0] - CSStaticData.g_debug_varient,
					CSStaticData.eye_center_none[1],
					CSStaticData.eye_center_none[2], 0, 1, 0);

			gl.glPushMatrix();

			if (m_b_horizontal_screen) {
				gl.glBindTexture(GL10.GL_TEXTURE_2D, m_bg_nongroup_landscape_id);
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_cover_nongroup_landscape_vers);
			} else {
				gl.glBindTexture(GL10.GL_TEXTURE_2D, m_bg_nongroup_portrait_id);
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_cover_nongroup_portrait_vers);
			}

			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, m_tex_buff);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			gl.glPopMatrix();
		} else if (m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_DATE) {
			GLU.gluLookAt(gl, CSStaticData.eye_position_date[0] - CSStaticData.g_debug_varient,
					CSStaticData.eye_position_date[1],
					CSStaticData.eye_position_date[2],
					CSStaticData.eye_center_date[0] - CSStaticData.g_debug_varient,
					CSStaticData.eye_center_date[1],
					CSStaticData.eye_center_date[2], 0, 1, 0);

			gl.glPushMatrix();

			if (m_b_horizontal_screen)
				gl.glTranslatef(0, 1.5f, -1.0f);
			else
				gl.glTranslatef(0, 2.5f, -1.0f);

			gl.glRotatef(-40.0f, 1.0f, 0, 0);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, m_background_id);
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_cover_buff);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, m_tex_buff);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			gl.glPopMatrix();

		} else {
			GLU.gluLookAt(gl, CSStaticData.eye_position_location[0] - CSStaticData.g_debug_varient,
					CSStaticData.eye_position_location[1],
					CSStaticData.eye_position_location[2],
					CSStaticData.eye_center_location[0] - CSStaticData.g_debug_varient,
					CSStaticData.eye_center_location[1],
					CSStaticData.eye_center_location[2], 0, 1, 0);

			/*
			gl.glPushMatrix();

			if (m_b_horizontal_screen) {
				gl.glBindTexture(GL10.GL_TEXTURE_2D, m_bg_nongroup_landscape_id);
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_cover_nongroup_landscape_vers);
			} else {
				gl.glBindTexture(GL10.GL_TEXTURE_2D, m_bg_nongroup_portrait_id);
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_cover_nongroup_portrait_vers);
			}

			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, m_tex_buff);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			gl.glPopMatrix();
			*/
		}
		// /

		m_element_group.onDraw(gl, MEDIA_VIEW.LEFT_VIEW);

		// ///////////////////////////////////////////////////////////////////

		// /////////////////////////////////////////////////////////////////////////
		// 右视图
		// gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		gl.glViewport(m_scr_width / 2, 0, m_scr_width / 2, m_scr_height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();

		GLU.gluPerspective(gl, 30, (float) m_scr_width / m_scr_height, 0.01f,
				100);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

		// /
		if (m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_NONE
				|| m_element_group.bEnterGroup() == true) {
			GLU.gluLookAt(gl, CSStaticData.eye_position_none[0] + CSStaticData.g_debug_varient,
					CSStaticData.eye_position_none[1],
					CSStaticData.eye_position_none[2],
					CSStaticData.eye_center_none[0] + CSStaticData.g_debug_varient,
					CSStaticData.eye_center_none[1],
					CSStaticData.eye_center_none[2], 0, 1, 0);

			gl.glPushMatrix();

			if (m_b_horizontal_screen) {
				gl.glBindTexture(GL10.GL_TEXTURE_2D, m_bg_nongroup_landscape_id);
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_cover_nongroup_landscape_vers);
			} else {
				gl.glBindTexture(GL10.GL_TEXTURE_2D, m_bg_nongroup_portrait_id);
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_cover_nongroup_portrait_vers);
			}

			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, m_tex_buff);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			gl.glPopMatrix();
		} else if (m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_DATE) {
			GLU.gluLookAt(gl, CSStaticData.eye_position_date[0] + CSStaticData.g_debug_varient,
					CSStaticData.eye_position_date[1],
					CSStaticData.eye_position_date[2],
					CSStaticData.eye_center_date[0] + CSStaticData.g_debug_varient,
					CSStaticData.eye_center_date[1],
					CSStaticData.eye_center_date[2], 0, 1, 0);

			gl.glPushMatrix();

			if (m_b_horizontal_screen)
				gl.glTranslatef(0, 1.5f, -1.0f);
			else
				gl.glTranslatef(0, 2.5f, -1.0f);

			gl.glRotatef(-40.0f, 1.0f, 0, 0);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, m_background_id);
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_cover_buff);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, m_tex_buff);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			gl.glPopMatrix();

		} else {
			GLU.gluLookAt(gl, CSStaticData.eye_position_location[0] + CSStaticData.g_debug_varient,
					CSStaticData.eye_position_location[1],
					CSStaticData.eye_position_location[2],
					CSStaticData.eye_center_location[0] + CSStaticData.g_debug_varient,
					CSStaticData.eye_center_location[1],
					CSStaticData.eye_center_location[2], 0, 1, 0);

			/*
			gl.glPushMatrix();

			if (m_b_horizontal_screen) {
				gl.glBindTexture(GL10.GL_TEXTURE_2D, m_bg_nongroup_landscape_id);
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_cover_nongroup_landscape_vers);
			} else {
				gl.glBindTexture(GL10.GL_TEXTURE_2D, m_bg_nongroup_portrait_id);
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_cover_nongroup_portrait_vers);
			}

			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, m_tex_buff);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			gl.glPopMatrix();
			*/
		}

		m_element_group.onDraw(gl, MEDIA_VIEW.RIGHT_VIEW);

	}

	private void D3CrossRender(GL10 gl) {
		m_element_group.calcPosition();
		// ////////////////////////////////////////////////////////////////
		// 左视图
		gl.glDisable(GL_SCISSOR_TEST);
//		gl.glDisable(GL_SCISSOR_BOX);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glEnable(GL_SCISSOR_TEST);
//		gl.glEnable(GL_SCISSOR_BOX);

		gl.glViewport(0, 0, m_scr_width / 2, m_scr_height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();

		GLU.gluPerspective(gl, 30, (float) m_scr_width / m_scr_height, 0.01f,100);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glEnable(GL10.GL_BLEND);
		gl.glLoadIdentity();
		
		// /
		if (m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_NONE
				|| m_element_group.bEnterGroup() == true) {
			GLU.gluLookAt(gl, CSStaticData.eye_position_none[0] - CSStaticData.g_debug_varient,
					CSStaticData.eye_position_none[1],
					CSStaticData.eye_position_none[2],
					CSStaticData.eye_center_none[0] - CSStaticData.g_debug_varient,
					CSStaticData.eye_center_none[1],
					CSStaticData.eye_center_none[2], 0, 1, 0);

			gl.glPushMatrix();

			if (m_b_horizontal_screen) {
				gl.glBindTexture(GL10.GL_TEXTURE_2D, m_bg_nongroup_landscape_id);
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_cover_nongroup_landscape_vers);
			} else {
				gl.glBindTexture(GL10.GL_TEXTURE_2D, m_bg_nongroup_portrait_id);
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_cover_nongroup_portrait_vers);
			}

			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, m_tex_buff);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			gl.glPopMatrix();
		} else if (m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_DATE) {
			GLU.gluLookAt(gl, CSStaticData.eye_position_date[0] - CSStaticData.g_debug_varient,
					CSStaticData.eye_position_date[1],
					CSStaticData.eye_position_date[2],
					CSStaticData.eye_center_date[0] - CSStaticData.g_debug_varient,
					CSStaticData.eye_center_date[1],
					CSStaticData.eye_center_date[2], 0, 1, 0);

			gl.glPushMatrix();

			if (m_b_horizontal_screen)
				gl.glTranslatef(0, 1.5f, -1.0f);
			else
				gl.glTranslatef(0, 2.5f, -1.0f);

			gl.glRotatef(-40.0f, 1.0f, 0, 0);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, m_background_id);
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_cover_buff);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, m_tex_buff);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			gl.glPopMatrix();

		} else {
			GLU.gluLookAt(gl, CSStaticData.eye_position_location[0] - CSStaticData.g_debug_varient,
					CSStaticData.eye_position_location[1],
					CSStaticData.eye_position_location[2],
					CSStaticData.eye_center_location[0] - CSStaticData.g_debug_varient,
					CSStaticData.eye_center_location[1],
					CSStaticData.eye_center_location[2], 0, 1, 0);

		}
		// /

		m_element_group.onDraw(gl, MEDIA_VIEW.LEFT_VIEW);

		//
		glBindTexture(GL10.GL_TEXTURE_2D, m_cross_render_texture_id);

		for (int x = 0; x < m_scr_width / 2; x++) {
			gl.glCopyTexSubImage2D(GL10.GL_TEXTURE_2D, 0, x * 2, 0, x, 0, 1, m_scr_height);
		}

		// ///////////////////////////////////////////////////////////////////

		// /////////////////////////////////////////////////////////////////////////
		// 右视图
		gl.glViewport(m_scr_width / 2, 0, m_scr_width / 2, m_scr_height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();

		GLU.gluPerspective(gl, 30, (float) m_scr_width / m_scr_height, 0.01f,100);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

		// /
		if (m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_NONE
				|| m_element_group.bEnterGroup() == true) {
			GLU.gluLookAt(gl, CSStaticData.eye_position_none[0] + CSStaticData.g_debug_varient,
					CSStaticData.eye_position_none[1],
					CSStaticData.eye_position_none[2],
					CSStaticData.eye_center_none[0] + CSStaticData.g_debug_varient,
					CSStaticData.eye_center_none[1],
					CSStaticData.eye_center_none[2], 0, 1, 0);

			gl.glPushMatrix();

			if (m_b_horizontal_screen) {
				gl.glBindTexture(GL10.GL_TEXTURE_2D, m_bg_nongroup_landscape_id);
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_cover_nongroup_landscape_vers);
			} else {
				gl.glBindTexture(GL10.GL_TEXTURE_2D, m_bg_nongroup_portrait_id);
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_cover_nongroup_portrait_vers);
			}

			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, m_tex_buff);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			gl.glPopMatrix();
		} else if (m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_DATE) {
			GLU.gluLookAt(gl, CSStaticData.eye_position_date[0] + CSStaticData.g_debug_varient,
					CSStaticData.eye_position_date[1],
					CSStaticData.eye_position_date[2],
					CSStaticData.eye_center_date[0] + CSStaticData.g_debug_varient,
					CSStaticData.eye_center_date[1],
					CSStaticData.eye_center_date[2], 0, 1, 0);

			gl.glPushMatrix();

			if (m_b_horizontal_screen)
				gl.glTranslatef(0, 1.5f, -1.0f);
			else
				gl.glTranslatef(0, 2.5f, -1.0f);

			gl.glRotatef(-40.0f, 1.0f, 0, 0);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, m_background_id);
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_cover_buff);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, m_tex_buff);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			gl.glPopMatrix();

		} else {
			GLU.gluLookAt(gl, CSStaticData.eye_position_location[0] + CSStaticData.g_debug_varient,
					CSStaticData.eye_position_location[1],
					CSStaticData.eye_position_location[2],
					CSStaticData.eye_center_location[0] + CSStaticData.g_debug_varient,
					CSStaticData.eye_center_location[1],
					CSStaticData.eye_center_location[2], 0, 1, 0);

		}

		m_element_group.onDraw(gl, MEDIA_VIEW.RIGHT_VIEW);

		//
		glBindTexture(GL10.GL_TEXTURE_2D, m_cross_render_texture_id);

		for (int x = 0; x < m_scr_width / 2; x++) {
			gl.glCopyTexSubImage2D(GL10.GL_TEXTURE_2D, 0, x*2+1, 0, m_scr_width/ 2 + x, 0, 1, m_scr_height);
		}

		// ////////////////
		gl.glDisable(GL_SCISSOR_TEST);
//		gl.glDisable(GL_SCISSOR_BOX);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glEnable(GL_SCISSOR_TEST);
//		gl.glEnable(GL_SCISSOR_BOX);

		gl.glViewport(0, 0, m_scr_width, m_scr_height);
		gl.glDisable(GL10.GL_DEPTH_TEST);

		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();

		gl.glOrthof(0, m_scr_width, 0, m_scr_height, -1, 1);

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

		gl.glPushMatrix();

		gl.glBindTexture(GL10.GL_TEXTURE_2D, m_cross_render_texture_id);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_cross_render_vers);

		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, m_tex_buff);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

		gl.glPopMatrix();
	}

	private void D3StencilRender(GL10 gl) {

		m_element_group.calcPosition();
		
		gl.glFrontFace(GL10.GL_CCW);

		float ratio = (float) m_scr_width / m_scr_height;

		// create stencil buffer
		stencil(gl, m_scr_width, m_scr_height);
		gl.glDisable(GL_SCISSOR_TEST);
//		gl.glDisable(GL_SCISSOR_BOX);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(0, 0, 0, 1);
		gl.glEnable(GL_SCISSOR_TEST);
//		gl.glEnable(GL_SCISSOR_BOX);

		gl.glEnable(GL10.GL_BLEND); // Turn blending off (NEW)
		gl.glEnable(GL10.GL_DEPTH_TEST); // Turn depth testing on (NEW)

		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluPerspective(gl, 30, ratio, 0.01f, 100f);
		GLU.gluLookAt(gl, 0.0f, 0.0f, 7f, 0, 0, 0, 0, 1, 0);

		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity();

		// right cube
		gl.glStencilFunc(GL10.GL_NOTEQUAL, 1, 1);

		stencilRightView(gl);
		// mCube.draw(gl, WiGalleryOpenGLRenderer.m_earth_background_id,
		// m_cross_render_vers);
		gl.glPopMatrix();

		// left cube
		gl.glStencilFunc(GL10.GL_EQUAL, 1, 1);
		gl.glPushMatrix();

		stencilLeftView(gl);
		// mCube.draw(gl, WiGalleryOpenGLRenderer.m_earth_background_id,
		// m_cross_render_vers);
		gl.glPopMatrix();

		gl.glDisable(GL10.GL_DEPTH_TEST);
	}

	private void resetView(GL10 gl) {
		gl.glViewport(0, 0, m_scr_width, m_scr_height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();

		GLU.gluPerspective(gl, 30, (float) m_scr_width / m_scr_height, 0.01f,
				100);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glEnable(GL10.GL_BLEND);
		gl.glLoadIdentity();

		if (m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_NONE
				|| m_element_group.bEnterGroup() == true) {
			GLU.gluLookAt(gl, CSStaticData.eye_position_none[0],
					CSStaticData.eye_position_none[1],
					CSStaticData.eye_position_none[2],
					CSStaticData.eye_center_none[0],
					CSStaticData.eye_center_none[1],
					CSStaticData.eye_center_none[2], 0, 1, 0);
		} else if (m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_DATE) {
			GLU.gluLookAt(gl, CSStaticData.eye_position_date[0],
					CSStaticData.eye_position_date[1],
					CSStaticData.eye_position_date[2],
					CSStaticData.eye_center_date[0],
					CSStaticData.eye_center_date[1],
					CSStaticData.eye_center_date[2], 0, 1, 0);

		} else {
			GLU.gluLookAt(gl, CSStaticData.eye_position_location[0],
					CSStaticData.eye_position_location[1],
					CSStaticData.eye_position_location[2],
					CSStaticData.eye_center_location[0],
					CSStaticData.eye_center_location[1],
					CSStaticData.eye_center_location[2], 0, 1, 0);
		}

	}

	private void D2Render(GL10 gl) {
		m_element_group.calcPosition();
		
		gl.glDisable(GL_SCISSOR_TEST);
//		gl.glDisable(GL_SCISSOR_BOX);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glEnable(GL_SCISSOR_TEST);
//		gl.glEnable(GL_SCISSOR_BOX);

		gl.glViewport(0, 0, m_scr_width, m_scr_height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();

		GLU.gluPerspective(gl, 30, (float) m_scr_width / m_scr_height, 0.01f, 100);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glEnable(GL10.GL_BLEND);
		gl.glLoadIdentity();

		if (m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_NONE || m_element_group.bEnterGroup() == true) {
			GLU.gluLookAt(gl, CSStaticData.eye_position_none[0],
					CSStaticData.eye_position_none[1],
					CSStaticData.eye_position_none[2],
					CSStaticData.eye_center_none[0],
					CSStaticData.eye_center_none[1],
					CSStaticData.eye_center_none[2], 0, 1, 0);

			gl.glPushMatrix();

			if (m_b_horizontal_screen) {
				gl.glBindTexture(GL10.GL_TEXTURE_2D, m_bg_nongroup_landscape_id);
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_cover_nongroup_landscape_vers);
			} else {
				gl.glBindTexture(GL10.GL_TEXTURE_2D, m_bg_nongroup_portrait_id);
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_cover_nongroup_portrait_vers);
			}

			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, m_tex_buff);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			gl.glPopMatrix();
		} else if (m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_DATE) {
			GLU.gluLookAt(gl, CSStaticData.eye_position_date[0],
					CSStaticData.eye_position_date[1],
					CSStaticData.eye_position_date[2],
					CSStaticData.eye_center_date[0],
					CSStaticData.eye_center_date[1],
					CSStaticData.eye_center_date[2], 0, 1, 0);

			gl.glPushMatrix();

			if (m_b_horizontal_screen)
				gl.glTranslatef(0, 1.5f, -1.0f);
			else
				gl.glTranslatef(0, 2.5f, -1.0f);

			gl.glRotatef(-40.0f, 1.0f, 0, 0);

			gl.glBindTexture(GL10.GL_TEXTURE_2D, m_background_id);
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_cover_buff);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, m_tex_buff);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			gl.glPopMatrix();

		} else {
			GLU.gluLookAt(gl, CSStaticData.eye_position_location[0],
					CSStaticData.eye_position_location[1],
					CSStaticData.eye_position_location[2],
					CSStaticData.eye_center_location[0],
					CSStaticData.eye_center_location[1],
					CSStaticData.eye_center_location[2], 0, 1, 0);
		}

		//

		
		m_element_group.onDraw(gl, MEDIA_VIEW.LEFT_VIEW);
	}

	static public boolean getDimension() {
		return m_is_3D_device;
	}

	public void AddNeedLoadTextureFile(Element file) {
		m_data_manager.AddNeedLoadTextureFile(file);
	}

	public ElementList getCurrentDisplayedList() {
		// TODO 获取当前正在显示的文件列表
		if (WiGalleryOpenGLRenderer.m_element_group != null)
			return WiGalleryOpenGLRenderer.m_element_group
				.getCurrentDisplayedList();
		else
			return null;
	}

	public static void onGLScenceChanged(boolean bHorizontal) {
		m_b_horizontal_screen = bHorizontal;
	}

	public static void onFileItemClick(String fileItemName) {
		WiGalleryInterface.m_onGlItemListener.onFileClick(fileItemName);
	}

	public static void onFileDelete(String fileName) {

	}

	public static void onPageMoveStart(int currentRow, int rows) {
		WiGalleryInterface.m_onGLMoveListener.onMoveStart(currentRow, rows);
	}

	public static void onPageMoving(int currentRow, int rows) {
		WiGalleryInterface.m_onGLMoveListener.onMoveing(currentRow, rows);
	}

	public static void onPageMoveEnd(int currentRow, int rows) {
		WiGalleryInterface.m_onGLMoveListener.onMoveEnd(currentRow, rows);
	}

	public static void onAsyncComplete(int totalFileNumber) {

	}

	private void stencilRightView(GL10 gl)
	{
		// ////////////////////////////////////////////////////////////////
		// 右视图		
	//	gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glViewport(0, 0, m_scr_width, m_scr_height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();

		GLU.gluPerspective(gl, 30, (float) m_scr_width / m_scr_height, 0.01f,100);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        
		// /
		if (m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_NONE || m_element_group.bEnterGroup() == true) 
		{
			GLU.gluLookAt(gl, CSStaticData.eye_position_none[0] + CSStaticData.g_debug_varient,
					CSStaticData.eye_position_none[1],
					CSStaticData.eye_position_none[2],
					CSStaticData.eye_center_none[0] + CSStaticData.g_debug_varient,
					CSStaticData.eye_center_none[1],
					CSStaticData.eye_center_none[2], 0, 1, 0);

			gl.glPushMatrix();

			if (m_b_horizontal_screen) 
			{
				gl.glBindTexture(GL10.GL_TEXTURE_2D, m_bg_nongroup_landscape_id);
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_cover_nongroup_landscape_vers);
			} else {
				gl.glBindTexture(GL10.GL_TEXTURE_2D, m_bg_nongroup_portrait_id);
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_cover_nongroup_portrait_vers);
			}

			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, m_tex_buff);
			
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			gl.glPopMatrix();
		}
		else if (m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_DATE) 
		{
			GLU.gluLookAt(gl, CSStaticData.eye_position_date[0] + CSStaticData.g_debug_varient,
					CSStaticData.eye_position_date[1],
					CSStaticData.eye_position_date[2],
					CSStaticData.eye_center_date[0] + CSStaticData.g_debug_varient,
					CSStaticData.eye_center_date[1],
					CSStaticData.eye_center_date[2], 0, 1, 0);

			gl.glPushMatrix();

			if (m_b_horizontal_screen)
				gl.glTranslatef(0, 1.5f, -1.0f);
			else
				gl.glTranslatef(0, 2.5f, -1.0f);

			gl.glRotatef(-40.0f, 1.0f, 0, 0);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, m_background_id);
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_cover_buff);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, m_tex_buff);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			gl.glPopMatrix();

		} 
		else 
		{
			GLU.gluLookAt(gl, CSStaticData.eye_position_location[0] + CSStaticData.g_debug_varient,
					CSStaticData.eye_position_location[1],
					CSStaticData.eye_position_location[2],
					CSStaticData.eye_center_location[0] + CSStaticData.g_debug_varient,
					CSStaticData.eye_center_location[1],
					CSStaticData.eye_center_location[2], 0, 1, 0);

		}

		m_element_group.onDraw(gl, MEDIA_VIEW.RIGHT_VIEW);
	}
	
	private void stencilLeftView(GL10 gl)
	{
		// ////////////////////////////////////////////////////////////////
		// 左视图
	//	gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

	//	gl.glViewport(0, 0, m_scr_width, m_scr_height);
	//	gl.glMatrixMode(GL10.GL_PROJECTION);
	//	gl.glLoadIdentity();

	//	GLU.gluPerspective(gl, 30, (float) m_scr_width / m_scr_height, 0.01f,100);
	//	gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glEnable(GL10.GL_BLEND);
		gl.glLoadIdentity();

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        
		// /
		if (m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_NONE || m_element_group.bEnterGroup() == true) 
		{
			GLU.gluLookAt(gl, CSStaticData.eye_position_none[0] - CSStaticData.g_debug_varient,
					CSStaticData.eye_position_none[1],
					CSStaticData.eye_position_none[2],
					CSStaticData.eye_center_none[0] - CSStaticData.g_debug_varient,
					CSStaticData.eye_center_none[1],
					CSStaticData.eye_center_none[2], 0, 1, 0);

			gl.glPushMatrix();

			if (m_b_horizontal_screen) 
			{
				gl.glBindTexture(GL10.GL_TEXTURE_2D, m_bg_nongroup_landscape_id);
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_cover_nongroup_landscape_vers);
			} else {
				gl.glBindTexture(GL10.GL_TEXTURE_2D, m_bg_nongroup_portrait_id);
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_cover_nongroup_portrait_vers);
			}

			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, m_tex_buff);				
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			gl.glPopMatrix();
		} else if (m_element_group.getType() == CSStaticData.LIST_TYPE.LIST_DATE) 
		{
			GLU.gluLookAt(gl, CSStaticData.eye_position_date[0] - CSStaticData.g_debug_varient,
					CSStaticData.eye_position_date[1],
					CSStaticData.eye_position_date[2],
					CSStaticData.eye_center_date[0] - CSStaticData.g_debug_varient,
					CSStaticData.eye_center_date[1],
					CSStaticData.eye_center_date[2], 0, 1, 0);

			gl.glPushMatrix();

			if (m_b_horizontal_screen)
				gl.glTranslatef(0, 1.5f, -1.0f);
			else
				gl.glTranslatef(0, 2.5f, -1.0f);

			gl.glRotatef(-40.0f, 1.0f, 0, 0);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, m_background_id);
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_cover_buff);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, m_tex_buff);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			gl.glPopMatrix();

		} 
		else 
		{
			GLU.gluLookAt(gl, CSStaticData.eye_position_location[0] - CSStaticData.g_debug_varient,
					CSStaticData.eye_position_location[1],
					CSStaticData.eye_position_location[2],
					CSStaticData.eye_center_location[0] - CSStaticData.g_debug_varient,
					CSStaticData.eye_center_location[1],
					CSStaticData.eye_center_location[2], 0, 1, 0);
		}
		// /

		m_element_group.onDraw(gl, MEDIA_VIEW.LEFT_VIEW);
	}
	
    private void stencil(GL10 gl, float width, float height){
        
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU.gluOrtho2D(gl, 0, width, 0, height);
        
        gl.glMatrixMode(GL10.GL_MODELVIEW);  
        gl.glLoadIdentity(); 

         gl.glEnable(GL10.GL_STENCIL_TEST);
         gl.glClearStencil(0);
         gl.glDisable(GL_SCISSOR_TEST);
// 		 gl.glDisable(GL_SCISSOR_BOX);
         gl.glClear(GL10.GL_STENCIL_BUFFER_BIT);
         gl.glEnable(GL_SCISSOR_TEST);
// 		 gl.glEnable(GL_SCISSOR_BOX);
         gl.glStencilOp (GL10.GL_REPLACE, GL10.GL_REPLACE, GL10.GL_REPLACE); // colorbuffer is copied to stencil
         gl.glDisable(GL10.GL_DEPTH_TEST);
         gl.glStencilFunc(GL10.GL_ALWAYS,1,1); // to avoid interaction with stencil content
        
         
        gl.glLoadIdentity();

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, m_stencil_vers);
        gl.glLineWidth(1);
        //gl.glColor4f(1,1,1,0);     // alfa is 0 not to interfere with alpha tests
        gl.glDrawArrays(GL10.GL_LINES, 0, (int)width);
        
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        
        gl.glStencilOp (GL10.GL_KEEP, GL10.GL_KEEP, GL10.GL_KEEP); // disabling changes in stencil buffer
        gl.glFlush();
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnable(GL10.GL_DEPTH_TEST);
    }
    
}

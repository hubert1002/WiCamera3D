package com.wistron.WiGallery;

import static android.opengl.GLES10.GL_CCW;
import static android.opengl.GLES10.GL_FLOAT;
import static android.opengl.GLES10.GL_TEXTURE_2D;
import static android.opengl.GLES10.glEnable;
import static android.opengl.GLES10.glFrontFace;
import static android.opengl.GLES10.glTexCoordPointer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import com.wistron.swpc.wicamera3dii.R;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class PhotoCube{
    
  //  int resId = R.raw.robot;
    private Context mContext;
	private int[] buffers= new int[9];
 //   private final static int VERTS = 6;
    private final static int numFaces = 1;
    public FloatBuffer mFVertexBuffer;
    public FloatBuffer mTexBuffer;
    private float[][] colors = {  // Colors of the 6 faces
    	      {1.0f, 0.5f, 0.0f, 1.0f},  // 0. orange
    	      {1.0f, 0.0f, 1.0f, 1.0f},  // 1. violet
    	      {0.0f, 1.0f, 0.0f, 1.0f},  // 2. green
    	      {0.0f, 0.0f, 1.0f, 1.0f},  // 3. blue
    	      {1.0f, 0.0f, 0.0f, 1.0f},  // 4. red
    	      {1.0f, 1.0f, 0.0f, 1.0f}   // 5. yellow
    	   };
    
    private float cubeHalfSize = 1.0f;
    
    
    private int[] textureIDs = new int[numFaces];
    private Bitmap[] bitmap = new Bitmap[numFaces];

    
    float faceLeft = 0;
    float faceRight = 0;
    float faceTop = 0;
    float faceBottom = 0;
    
    public PhotoCube(Context context) {
    	mContext = context;
        
        // Allocate vertex buffer. An float has 4 bytes
        ByteBuffer vbb = ByteBuffer.allocateDirect(12 * 4 * numFaces);
        vbb.order(ByteOrder.nativeOrder());
        mFVertexBuffer = vbb.asFloatBuffer();
        
        // Read images. Find the aspect ratio and adjust the vertices accordingly.

    	  
          float faceWidth = 4.0f;
          float faceHeight = 4.0f;

          faceLeft = -faceWidth / 2;
          faceRight = -faceLeft;
          faceTop = faceHeight / 2;
          faceBottom = -faceTop;
          
           // Define the vertices for this face
           float[] vertices = {
              faceLeft,  faceBottom, 0.0f,  // 0. left-bottom-front
              faceRight, faceBottom, 0.0f,  // 1. right-bottom-front
              faceLeft,  faceTop,    0.0f,  // 2. left-top-front
              faceRight, faceTop,    0.0f,  // 3. right-top-front
           };
           
           mFVertexBuffer.put(vertices);  // Populate


        ByteBuffer tbb = ByteBuffer.allocateDirect(numFaces * 4 * 8);
        tbb.order(ByteOrder.nativeOrder());
        mTexBuffer = tbb.asFloatBuffer();


        // Allocate texture buffer. An float has 4 bytes. Repeat for 6 faces.
        float[] texCoords = {
           0.0f, 1.0f,  // A. left-bottom
           1.0f, 1.0f,  // B. right-bottom
           0.0f, 0.0f,  // C. left-top
           1.0f, 0.0f   // D. right-top
        };
        

        for (int face = 0; face < numFaces; face++) {
        	
        	mTexBuffer.put(texCoords);
         }

        mFVertexBuffer.position(0);    // Rewind
        mTexBuffer.position(0);
    }
    
    public void draw(GL10 gl, int textureID, FloatBuffer vertex) {
        glFrontFace(GL_CCW);
        
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mFVertexBuffer);

        glEnable(GL_TEXTURE_2D);
        glTexCoordPointer(2, GL_FLOAT, 0, mTexBuffer);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        
  //      gl.glPushMatrix();
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureID);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
  //      gl.glPopMatrix();

                
 //       gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
 //       gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        
        
    }

}
package com.iori.custom.testmodel.opengl;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.iori.custom.testmodel.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenglImageActivity extends BaseOpenGlActivity{
    private int programId;
//    private String vertexShaderCode =
//            "precision mediump float;\n" +
//                    "attribute vec4 a_position;\n" +
//                    "attribute vec2 a_textureCoordinate;\n" +
//                    "varying vec2 v_textureCoordinate;\n" +
//                    "void main() {\n" +
//                    "    v_textureCoordinate = a_textureCoordinate;\n" +
//                    "    gl_Position = a_position;\n" +
//                    "}";

    private String vertexShaderCode =
            "precision mediump float;\n" +
                    "attribute vec4 a_position;\n" +
                    "attribute vec2 a_textureCoordinate;\n" +
                    "varying vec2 v_textureCoordinate;\n" +
                    "void main() {\n" +
                    "vec2 p=a_textureCoordinate;\n" +
                    "float temp=p.x;\n"+
                    "p.x=temp;\n"+
                    "    v_textureCoordinate=p;\n"+
                    "    gl_Position = a_position;\n" +
                    "}";

//    vec2 color;
//    if (v_textureCoordinate.x == 0) {
//        color=vec2(1,v_textureCoordinate.y);
//    } else {
//        color=vec2(0,v_textureCoordinate.y);
//    }


    private String fragmentShaderCode =
            "precision mediump float;\n" +
                    "varying vec2 v_textureCoordinate;\n" +
                    "uniform sampler2D u_texture;\n" +
                    "void main() {\n" +
                    "    gl_FragColor = texture2D(u_texture, v_textureCoordinate);\n" +
                    "}";

    private float[] vertexData = new float[]{-1f, -1f, -1f, 1f, 1f, 1f, -1f, -1f, 1f, 1f, 1f, -1f};

    private float[] textureCoordinateData = new float[]{0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 1f, 0f, 1f, 1f};

//    invert
//    private float[] textureCoordinateData = new float[]{
//            1f, 1f
//            ,1f, 0f
//            ,0f, 0f
//            ,1f, 1f
//            ,0f, 0f
//            ,0f, 1f
//    };

    private int VERTEX_COMPONENT_COUNT = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initGl();
    }

    private void initGl(){
        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 0, 0);
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(new ImageRender());
    }

    private class ImageRender implements GLSurfaceView.Renderer{

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            programId = GLES20.glCreateProgram();

            int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
            int fragmentShader= GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

            vertexShaderCode=OpenglUtil.getStringFromRaw(OpenglImageActivity.this,R.raw.opengl_image_vertex);

            GLES20.glShaderSource(vertexShader, vertexShaderCode);

            fragmentShaderCode=OpenglUtil.getStringFromRaw(OpenglImageActivity.this, R.raw.opengl_image_fragment);

            GLES20.glShaderSource(fragmentShader, fragmentShaderCode);
            GLES20.glCompileShader(vertexShader);
            GLES20.glCompileShader(fragmentShader);

            GLES20.glAttachShader(programId, vertexShader);
            GLES20.glAttachShader(programId, fragmentShader);

            GLES20.glLinkProgram(programId);

            GLES20.glUseProgram(programId);


            FloatBuffer buffer = ByteBuffer.allocateDirect(vertexData.length * java.lang.Float.SIZE)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer();
            buffer.put(vertexData);
            buffer.position(0);

            int location = GLES20.glGetAttribLocation(programId, "a_position");
            GLES20.glEnableVertexAttribArray(location);
            GLES20.glVertexAttribPointer(location, 2, GLES20.GL_FLOAT, false,0, buffer);

            createTextures();

        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            glSurfaceViewWidth = width;
            glSurfaceViewHeight = height;
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            GLES20.glClearColor(0.9f, 0.9f, 0.9f, 1f);
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
            GLES20.glViewport(0, 0, glSurfaceViewWidth, glSurfaceViewHeight);





            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexData.length/VERTEX_COMPONENT_COUNT);
            OpenglUtil.checkGLError();
        }

        private void loadBitmap(){
            Bitmap bitmap = OpenglUtil.decodeBitmapFromAssets(OpenglImageActivity.this,"piece.jpg");
            ByteBuffer b = ByteBuffer.allocateDirect(bitmap.getWidth() * bitmap.getHeight() * 4);
            bitmap.copyPixelsToBuffer(b);
            b.position(0);

//            load bitmap to texture
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D,
                    0,
                    GLES20.GL_RGBA,
                    bitmap.getWidth(),
                    bitmap.getHeight(),
                    0,
                    GLES20.GL_RGBA,
                    GLES20.GL_UNSIGNED_BYTE,
                    b);

//            use texture
            int uTextureLocation = GLES20.glGetAttribLocation(programId, "u_texture");

            useTexture(uTextureLocation,1);

            Log.d("iori", "loadBitmap: 9");
        }

        private void useTexture(int uTextureLocation,int usedTextureUnit){
            Log.d("iori", "useTexture1: ");
            switch (usedTextureUnit){
                case 0:
                    GLES20.glUniform1i(uTextureLocation, usedTextureUnit);
                    break;
                case 1:
                    GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
                    GLES20.glUniform1i(uTextureLocation, usedTextureUnit);
                    break;
            }
        }

        private void createTextures(){
            int a_textureCoordinate=GLES20.glGetAttribLocation(programId,"a_textureCoordinate");

            GLES20.glEnableVertexAttribArray(a_textureCoordinate);
            FloatBuffer textureBuffer=ByteBuffer.allocateDirect(textureCoordinateData.length*Float.SIZE/8)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer();
            textureBuffer.put(textureCoordinateData);
            textureBuffer.position(0);

            GLES20.glVertexAttribPointer(a_textureCoordinate,2,GLES20.GL_FLOAT,false,0,textureBuffer);

            OpenglUtil.checkGLError("init a_textureCoordinate");

//            create textures
            int[] textures = new int[]{0};
            GLES20.glGenTextures(textures.length, textures, 0);
            int imageTexture = textures[0];

//            bind texture
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, imageTexture);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
            loadBitmap();
        }
    }
}

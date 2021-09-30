package com.iori.custom.testmodel.opengl;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.iori.custom.testmodel.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenglLongImageActivity extends BaseOpenGlActivity implements View.OnClickListener {
    private int programId;
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

    private String fragmentShaderCode =
            "precision mediump float;\n" +
                    "varying vec2 v_textureCoordinate;\n" +
                    "uniform sampler2D u_texture;\n" +
                    "void main() {\n" +
                    "    gl_FragColor = texture2D(u_texture, v_textureCoordinate);\n" +
                    "}";

    private float[] vertexData = new float[]{
            -1f, 1f,  //left top
            1f, 1f,   //right top

            -1f,0f,   //area left top
            1f,0f,    //area right top

            -1f,-0.5f,  //area left bottom
            1f,-0.5f,   //area right bottom

            -1f, -1f,   //left bottom
            1f, -1f     //right bottom
    };

    private float[] textureCoordinateData = new float[]{
            0f, 0f,   //left top
            1f, 0f,   //right top

            0f,0.5f,  //area left top
            1f,0.5f,  //area right top

            0f,0.75f,  //area left bottom
            1f,0.75f,  //area right bottom

            0f, 1f,    //left bottom
            1f, 1f     //right bottom
    };

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

    private Button incRatio;
    private Button decRatio;

    private float extension =0.05f;

    @Override
    protected int getLayoutID() {
        return R.layout.opengl_long_image_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        incRatio=findViewById(R.id.incRatio);
        decRatio=findViewById(R.id.decRatio);
        incRatio.setOnClickListener(this);
        decRatio.setOnClickListener(this);
        initGl();
    }

    private void initGl(){
        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 0, 0);
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(new ImageRender());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.incRatio:
                resizeImageDraw(extension);
                break;
            case R.id.decRatio:
                resizeImageDraw(-extension);
                break;
        }
    }

    private void resizeImageDraw(float delta){
        calcNewVertex(delta);
    }

    private void calcNewVertex(float delta) {
//        left top
        float[] leftTop=new float[]{vertexData[0],vertexData[1]};
        leftTop[1]+=delta;
//        right top
        float[] rightTop=new float[]{vertexData[2],vertexData[3]};
        rightTop[1]+=delta;
//        left bottom
        float[] leftBottom=new float[]{vertexData[12],vertexData[13]};
        leftBottom[1]-=delta;
//        right bottom
        float[] rightBottom=new float[]{vertexData[14],vertexData[15]};
        rightBottom[1]-=delta;

        vertexData[1]=leftTop[1];
        vertexData[3]=rightTop[1];
        vertexData[13]=leftBottom[1];
        vertexData[15]=rightBottom[1];

        calcNewArea(delta);

        Log.d("iori", ": new left top "+leftTop[1]
            +"right top "+rightTop[1]
            +"left bottom "+leftBottom[1]
            +"right bottom "+rightBottom[1]);
    }

    private void calcNewArea(float delta) {
        //area left top
        float[] areaLeftTop=new float[]{vertexData[4],vertexData[5]};
        //area right top
        float[] areaRightTop=new float[]{vertexData[6],vertexData[7]};
        //area left bottom
        float[] areaLeftBottom=new float[]{vertexData[8],vertexData[9]};
        //area right bottom
        float[] areaRightBottom=new float[]{vertexData[10],vertexData[11]};

        areaLeftTop[1]+=delta;
        vertexData[5]=areaLeftTop[1];

        areaRightTop[1]+=delta;
        vertexData[7]=areaRightTop[1];

        areaLeftBottom[1]-=delta;
        vertexData[9]=areaLeftBottom[1];

        areaRightBottom[1]-=delta;
        vertexData[11]=areaRightBottom[1];
    }

    private class ImageRender implements GLSurfaceView.Renderer{

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            programId = GLES20.glCreateProgram();

            int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
            int fragmentShader= GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

            vertexShaderCode=OpenglUtil.getStringFromRaw(OpenglLongImageActivity.this,R.raw.opengl_image_vertex);

            GLES20.glShaderSource(vertexShader, vertexShaderCode);

            fragmentShaderCode=OpenglUtil.getStringFromRaw(OpenglLongImageActivity.this, R.raw.opengl_image_fragment);

            GLES20.glShaderSource(fragmentShader, fragmentShaderCode);
            GLES20.glCompileShader(vertexShader);
            GLES20.glCompileShader(fragmentShader);

            GLES20.glAttachShader(programId, vertexShader);
            GLES20.glAttachShader(programId, fragmentShader);

            GLES20.glLinkProgram(programId);

            GLES20.glUseProgram(programId);


            loadVertex();

            createTextures();

        }

        private void loadVertex() {
            FloatBuffer buffer = ByteBuffer.allocateDirect(vertexData.length * Float.SIZE)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer();
            buffer.put(vertexData);
            buffer.position(0);

            int location = GLES20.glGetAttribLocation(programId, "a_position");
            GLES20.glEnableVertexAttribArray(location);
            GLES20.glVertexAttribPointer(location, 2, GLES20.GL_FLOAT, false,0, buffer);
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
            loadVertex();
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vertexData.length/VERTEX_COMPONENT_COUNT);
            OpenglUtil.checkGLError();
        }

        private void loadBitmap(){
            Bitmap bitmap = OpenglUtil.decodeBitmapFromAssets(OpenglLongImageActivity.this,"piece.jpg");
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

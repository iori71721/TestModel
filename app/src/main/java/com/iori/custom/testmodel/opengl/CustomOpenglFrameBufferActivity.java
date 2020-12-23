package com.iori.custom.testmodel.opengl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CustomOpenglFrameBufferActivity extends BaseOpenGlActivity{
    private int loadBitmapProgramID;

    private String loadBitmapProgramVertexShaderCode =
            "precision mediump float;\n" +
                    "attribute vec4 a_position;\n" +
                    "attribute vec2 a_textureCoordinate;\n" +
                    "varying vec2 v_textureCoordinate;\n" +
                    "void main() {\n" +
                    "    v_textureCoordinate = a_textureCoordinate;\n" +
                    "    gl_Position = a_position;\n" +
                    "}";

    private String loadBitmapProgramFragmentShaderCode =
            "precision mediump float;\n" +
                    "varying vec2 v_textureCoordinate;\n" +
                    "uniform sampler2D u_texture;\n" +
                    "void main() {\n" +
                    "    gl_FragColor = texture2D(u_texture, v_textureCoordinate);\n" +
                    "}";

    private float[] loadBitmapProgramProgramVertexData = new float[]{-1f, -1f, -1f, 1f, 1f, 1f, -1f, -1f, 1f, 1f, 1f, -1f};

    private FloatBuffer loadBitmapProgramProgramVertexBuffer;

    private float[] loadBitmapProgramTextureCoordinateData = new float[]{
            1f, 1f
            ,1f, 0f
            ,0f, 0f
            ,1f, 1f
            ,0f, 0f
            ,0f, 1f
    };

    private FloatBuffer loadBitmapProgramTextureBuffer;

    private int loadBitmapTextureID;


    private int colorTriangleProgramID;

    private String colorTriangleProgramVertexShaderCode =
            "precision mediump float;\n" +
            "attribute vec4 a_position;\n" +
            "attribute vec4 a_Color;\n" +
            "varying vec4 v_Color;\n" +
            "void main() {\n" +
            "    v_Color = a_Color;\n" +
            "    gl_Position = a_position;\n" +
            "}";

    private String colorTriangleProgramFragmentShaderCode =
            "precision mediump float;\n" +
            "varying vec4 v_Color;\n" +
            "void main() {\n" +
            "    gl_FragColor = v_Color;\n" +
            "}";

    private float[] colorTriangleProgramVertexData=new float[]{0f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f};
    private FloatBuffer colorTriangleProgramVertexBuffer;

    private float[] colorTriangleProgramColorData = new float[]{
            1.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f};

    private FloatBuffer colorTriangleProgramColorBuffer;

    private int COLOR_COMPONENT_COUNT = 4;

    private int frameTextureID;
    private int frameBufferID;

    private int VERTEX_COMPONENT_COUNT = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initGl();
    }

    private void initGl(){
        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 0, 0);
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(new CustomOpenglFrameBufferRender());
    }

    private class CustomOpenglFrameBufferRender implements GLSurfaceView.Renderer{

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            loadBitmapProgramID=OpenglUtil.createGLProgram(loadBitmapProgramVertexShaderCode, loadBitmapProgramFragmentShaderCode);
            colorTriangleProgramID=OpenglUtil.createGLProgram(colorTriangleProgramVertexShaderCode, colorTriangleProgramFragmentShaderCode);
            initData();
            Log.d("iori", "onSurfaceCreated: 5");
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            glSurfaceViewWidth = width;
            glSurfaceViewHeight = height;
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            loadBitmapProgram();
            colorTriangleProgram();
        }

        private void drawToScreen(){
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        }

        private void colorTriangleProgram(){
            int a_position=OpenglUtil.loadDataToAttribute(colorTriangleProgramID,VariableName.a_position,VERTEX_COMPONENT_COUNT, GLES20.GL_FLOAT,colorTriangleProgramVertexBuffer);
            int a_Color=OpenglUtil.loadDataToAttribute(colorTriangleProgramID,VariableName.a_Color,COLOR_COMPONENT_COUNT,GLES20.GL_FLOAT,colorTriangleProgramColorBuffer);

            drawToScreen();
            render(false);
        }

        private void loadBitmapProgram(){
            int a_position=OpenglUtil.loadDataToAttribute(loadBitmapProgramID,VariableName.a_position,VERTEX_COMPONENT_COUNT, GLES20.GL_FLOAT,loadBitmapProgramProgramVertexBuffer);
            int a_textureCoordinate=OpenglUtil.loadDataToAttribute(loadBitmapProgramID,VariableName.a_textureCoordinate,VERTEX_COMPONENT_COUNT, GLES20.GL_FLOAT,loadBitmapProgramTextureBuffer);

            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, loadBitmapTextureID);
            int u_texture=GLES20.glGetAttribLocation(loadBitmapProgramID, VariableName.u_texture);
            GLES20.glUniform1i(u_texture, 0);

//            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBufferID);

            render(true);
        }

        private void initData(){
            loadBitmapProgramProgramVertexBuffer=OpenglUtil.createFloatBuffer(loadBitmapProgramProgramVertexData);
            loadBitmapProgramTextureBuffer=OpenglUtil.createFloatBuffer(loadBitmapProgramTextureCoordinateData);

            colorTriangleProgramVertexBuffer=OpenglUtil.createFloatBuffer(colorTriangleProgramVertexData);
            colorTriangleProgramColorBuffer=OpenglUtil.createFloatBuffer(colorTriangleProgramColorData);

            loadBitmapTextureID=OpenglUtil.createImageTexture();
            loadBitmapTextureID=OpenglUtil.loadBitmapFromAssets(CustomOpenglFrameBufferActivity.this,"piece.jpg",loadBitmapTextureID);

            frameTextureID= OpenglUtil.createImageTexture();
            frameBufferID=OpenglUtil.createFrameBuffer();
            frameBufferID=OpenglUtil.bindImageTextureToFrameBuffer(glSurfaceViewWidth,glSurfaceViewHeight,frameBufferID,frameTextureID);
        }

        private class VariableName{
            public static final String a_position="a_position";
            public static final String a_textureCoordinate="a_textureCoordinate";
            public static final String u_texture="u_texture";
            public static final String a_Color="a_Color";
        }

        private void render(boolean clearScreen) {
            if(clearScreen) {
                GLES20.glClearColor(0.9f, 0.9f, 0.9f, 1f);
                GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
            }
            GLES20.glViewport(0, 0, glSurfaceViewWidth, glSurfaceViewHeight);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, loadBitmapProgramProgramVertexData.length / VERTEX_COMPONENT_COUNT);
        }
    }
}

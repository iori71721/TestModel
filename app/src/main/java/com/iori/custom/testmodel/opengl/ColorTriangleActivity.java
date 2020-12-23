package com.iori.custom.testmodel.opengl;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import androidx.annotation.Nullable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class ColorTriangleActivity extends BaseOpenGlActivity{
    private int programId;
    private String vertexShaderCode =
            "precision mediump float;\n" +
                    "attribute vec4 a_Position;\n" +
                    "attribute vec4 a_Color;\n" +
                    "varying vec4 v_Color;\n" +
                    "void main() {\n" +
                    "    v_Color = a_Color;\n" +
                    "    gl_Position = a_Position;\n" +
                    "}";

    private String fragmentShaderCode =
            "precision mediump float;\n" +
                    "varying vec4 v_Color;\n" +
                    "void main() {\n" +
                    "    gl_FragColor = v_Color;\n" +
                    "}";

    private float[] vertexData = new float[]{0f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f};


    private float[] colorData = new float[]{
            1.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f};

    private int VERTEX_COMPONENT_COUNT = 2;

    private int COLOR_COMPONENT_COUNT = 4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initGl();
    }

    private void initGl(){
        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 0, 0);
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(new ColorTriangleRender());
    }

    private class ColorTriangleRender implements GLSurfaceView.Renderer{

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            programId = GLES20.glCreateProgram();

            int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
            int fragmentShader= GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
            GLES20.glShaderSource(vertexShader, vertexShaderCode);
            GLES20.glShaderSource(fragmentShader, fragmentShaderCode);
            GLES20.glCompileShader(vertexShader);
            GLES20.glCompileShader(fragmentShader);

            GLES20.glAttachShader(programId, vertexShader);
            GLES20.glAttachShader(programId, fragmentShader);

            GLES20.glLinkProgram(programId);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            glSurfaceViewWidth = width;
            glSurfaceViewHeight = height;
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            GLES20.glUseProgram(programId);

            FloatBuffer buffer = ByteBuffer.allocateDirect(vertexData.length * java.lang.Float.SIZE)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer();
            buffer.put(vertexData);
            buffer.position(0);

            int location = GLES20.glGetAttribLocation(programId, "a_Position");
            GLES20.glEnableVertexAttribArray(location);
            GLES20.glVertexAttribPointer(location, 2, GLES20.GL_FLOAT, false,0, buffer);


            FloatBuffer colorBuffer=ByteBuffer.allocateDirect(colorData.length*Float.SIZE)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer();
            colorBuffer.put(colorData);
            colorBuffer.position(0);

            int aColorLocation = GLES20.glGetAttribLocation(programId, "a_Color");
            GLES20.glEnableVertexAttribArray(aColorLocation);
            GLES20.glVertexAttribPointer(aColorLocation,COLOR_COMPONENT_COUNT,GLES20.GL_FLOAT,false,0,colorBuffer);

            GLES20.glClearColor(0.9f, 0.9f, 0.9f, 1f);
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

            GLES20.glViewport(0, 0, glSurfaceViewWidth, glSurfaceViewHeight);

//            兩個三角形
//            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);

            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexData.length/VERTEX_COMPONENT_COUNT);
        }
    }
}

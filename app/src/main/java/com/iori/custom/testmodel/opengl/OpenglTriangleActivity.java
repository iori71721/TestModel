package com.iori.custom.testmodel.opengl;

import android.app.Activity;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.iori.custom.testmodel.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenglTriangleActivity extends Activity {
    private GLSurfaceView glSurfaceView;
    private int glSurfaceViewWidth;
    private int glSurfaceViewHeight;
    private int programId;
    private String vertexShaderCode =
            "precision mediump float;\n" +
                    "attribute vec4 a_Position;\n" +
                    "void main() {\n" +
                    "    gl_Position = a_Position;\n" +
                    "}";

    private String fragmentShaderCode =
            "precision mediump float;\n" +
                    "void main() {\n" +
                    "    gl_FragColor = vec4(0.0, 0.0, 1.0, 1.0);\n" +
                    "}";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opengl_triangle_activity);
        glSurfaceView=findViewById(R.id.glSurfaceView);
        initGl();
        drawGl();
    }

    private void initGl(){
        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 0, 0);
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(new SampleHelloWorld());
    }

    private void drawGl(){

    }

    private class SampleHelloWorld implements GLSurfaceView.Renderer{

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
//            float[] vertexData = new float[]{0f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f};

            float[] vertexData = new float[]{0f, 1f, -1f, -0.5f, 0.5f, -0.5f};

            FloatBuffer buffer = ByteBuffer.allocateDirect(vertexData.length * java.lang.Float.SIZE)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer();
            buffer.put(vertexData);
            buffer.position(0);

            int location = GLES20.glGetAttribLocation(programId, "a_Position");

            GLES20.glEnableVertexAttribArray(location);

            GLES20.glVertexAttribPointer(location, 2, GLES20.GL_FLOAT, false,0, buffer);

            GLES20.glClearColor(0.9f, 0.9f, 0.9f, 1f);

            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

            GLES20.glViewport(0, 0, glSurfaceViewWidth, glSurfaceViewHeight);

            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
        }
    }
}

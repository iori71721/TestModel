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

public class TranslateActivity extends BaseOpenGlActivity{
    private int programId;
    private String vertexShaderCode =
            "precision mediump float;\n" +
                    "attribute vec4 a_Position;\n" +
                    "\n" +
                    "uniform vec2 u_Translate;\n" +
                    "uniform float u_Scale;\n" +
                    "uniform float u_Rotate;\n" +
                    "uniform float u_Ratio;\n" +
                    "\n" +
                    "void main() {\n" +
                    "    vec4 p = a_Position;\n" +
                    "    p.y = p.y / u_Ratio;\n" +
                    "    mat4 translateMatrix = mat4(1.0, 0.0, 0.0, 0.0,\n" +
                    "                              0.0, 1.0, 0.0, 0.0,\n" +
                    "                              0.0, 0.0, 1.0, 0.0,\n" +
                    "                              u_Translate.x, u_Translate.y, 0.0, 1.0);\n" +
                    "    mat4 scaleMatrix = mat4(u_Scale, 0.0, 0.0, 0.0,\n" +
                    "                        0.0, u_Scale, 0.0, 0.0,\n" +
                    "                        0.0, 0.0, 1.0, 0.0,\n" +
                    "                        0.0, 0.0, 0.0, 1.0);\n" +
                    "    mat4 rotateMatrix = mat4(cos(u_Rotate), sin(u_Rotate), 0.0, 0.0,\n" +
                    "                         -sin(u_Rotate), cos(u_Rotate), 0.0, 0.0,\n" +
                    "                         0.0, 0.0, 1.0, 0.0,\n" +
                    "                         0.0, 0.0, 0.0, 1.0);\n" +
                    "    p = translateMatrix * rotateMatrix * scaleMatrix * p;\n" +
                    "    p.y = p.y * u_Ratio;\n" +
                    "    gl_Position = p;\n" +
                    "}";

    private String fragmentShaderCode =
            "precision mediump float;\n" +
                    "void main() {\n" +
                    "    gl_FragColor = vec4(0.0, 0.0, 1.0, 1.0);\n" +
                    "}";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initGl();
    }

    private void initGl(){
        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 0, 0);
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(new TranslateRender());
    }

    private class TranslateRender implements GLSurfaceView.Renderer{

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
            float[] vertexData = new float[]{0f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f};

//            兩個三角形
//            vertexData=new float[]{-0.5f, 1f, -1f, 0f, 0f, 0f, 0.5f, 0f, 0f, -1f, 1f, -1f};

            FloatBuffer buffer = ByteBuffer.allocateDirect(vertexData.length * java.lang.Float.SIZE)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer();
            buffer.put(vertexData);
            buffer.position(0);

            int location = GLES20.glGetAttribLocation(programId, "a_Position");
            GLES20.glEnableVertexAttribArray(location);
            GLES20.glVertexAttribPointer(location, 2, GLES20.GL_FLOAT, false,0, buffer);

            int uTranslateLocation = GLES20.glGetUniformLocation(programId, "u_Translate");
            GLES20.glEnableVertexAttribArray(uTranslateLocation);
            GLES20.glUniform2f(uTranslateLocation, 0.3f, 0.3f);

            int uScaleLocation = GLES20.glGetUniformLocation(programId, "u_Scale");
            GLES20.glEnableVertexAttribArray(uScaleLocation);
            GLES20.glUniform1f(uScaleLocation, 0.75f);

            int uRotateLocation = GLES20.glGetUniformLocation(programId, "u_Rotate");
            GLES20.glEnableVertexAttribArray(uRotateLocation);
            GLES20.glUniform1f(uRotateLocation, new Float(Math.toRadians(180.0)));

//            處理寬高比不一致的問題
            int uRatioLocation = GLES20.glGetUniformLocation(programId, "u_Ratio");
            GLES20.glEnableVertexAttribArray(uRatioLocation);
            GLES20.glUniform1f(uRatioLocation, glSurfaceViewWidth * 1.0f / glSurfaceViewHeight);


            GLES20.glClearColor(0.9f, 0.9f, 0.9f, 1f);
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

            GLES20.glViewport(0, 0, glSurfaceViewWidth, glSurfaceViewHeight);

//            兩個三角形
//            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);

            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
        }
    }
}

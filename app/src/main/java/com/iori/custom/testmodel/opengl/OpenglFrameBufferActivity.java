package com.iori.custom.testmodel.opengl;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import androidx.annotation.Nullable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenglFrameBufferActivity extends BaseOpenGlActivity{
    private String vertexShaderCode =
            "precision mediump float;\n" +
                    "attribute vec4 a_position;\n" +
                    "attribute vec2 a_textureCoordinate;\n" +
                    "varying vec2 v_textureCoordinate;\n" +
                    "void main() {\n" +
                    "    v_textureCoordinate = a_textureCoordinate;\n" +
                    "    gl_Position = a_position;\n" +
                    "}";

    private String fragmentShaderCode0 =
            "precision mediump float;\n" +
                    "varying vec2 v_textureCoordinate;\n" +
                    "uniform sampler2D u_texture;\n" +
                    "void main() {\n" +
                    "    vec4 color = texture2D(u_texture, v_textureCoordinate);\n" +
                    "    color.b = 0.5;\n" +
                    "    gl_FragColor = color;\n" +
                    "}";

    private String fragmentShaderCode1 =
            "precision mediump float;\n" +
                    "varying vec2 v_textureCoordinate;\n" +
                    "uniform sampler2D u_texture;\n" +
                    "void main() {\n" +
                    "    float offset = 0.005;\n" +
                    "    vec4 color = texture2D(u_texture, v_textureCoordinate) * 0.11111;\n" +
                    "    color += texture2D(u_texture, vec2(v_textureCoordinate.x - offset, v_textureCoordinate.y)) * 0.11111;\n" +
                    "    color += texture2D(u_texture, vec2(v_textureCoordinate.x + offset, v_textureCoordinate.y)) * 0.11111;\n" +
                    "    color += texture2D(u_texture, vec2(v_textureCoordinate.x - offset * 2.0, v_textureCoordinate.y)) * 0.11111;\n" +
                    "    color += texture2D(u_texture, vec2(v_textureCoordinate.x + offset * 2.0, v_textureCoordinate.y)) * 0.11111;\n" +
                    "    color += texture2D(u_texture, vec2(v_textureCoordinate.x - offset * 3.0, v_textureCoordinate.y)) * 0.11111;\n" +
                    "    color += texture2D(u_texture, vec2(v_textureCoordinate.x + offset * 3.0, v_textureCoordinate.y)) * 0.11111;\n" +
                    "    color += texture2D(u_texture, vec2(v_textureCoordinate.x - offset * 4.0, v_textureCoordinate.y)) * 0.11111;\n" +
                    "    color += texture2D(u_texture, vec2(v_textureCoordinate.x + offset * 4.0, v_textureCoordinate.y)) * 0.11111;\n" +
                    "    gl_FragColor = color;\n" +
                    "}";

    private float[] vertexData = new float[]{-1f, -1f, -1f, 1f, 1f, 1f, -1f, -1f, 1f, 1f, 1f, -1f};
    private int VERTEX_COMPONENT_COUNT = 2;
    private FloatBuffer vertexDataBuffer;

    private float[] textureCoordinateData0 = new float[]{0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 1f, 0f, 1f, 1f};
//    source is texture,so used texture coordinate
    private float[] textureCoordinateData1 = new float[]{0f, 0f, 0f, 1f, 1f, 1f, 0f, 0f, 1f, 1f, 1f, 0f};
    private FloatBuffer textureCoordinateDataBuffer0;
    private FloatBuffer textureCoordinateDataBuffer1;
    private int TEXTURE_COORDINATE_COMPONENT_COUNT = 2;

    private int programId0 = 0;
    private int programId1 = 0;

    private int frameBuffer = 0;

    private int frameBufferTexture = 0;

    private int imageTexture = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initGl();
    }

    private void initGl(){
        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 0, 0);
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(new OpenglFrameBufferRender());
    }

    private class OpenglFrameBufferRender implements GLSurfaceView.Renderer{

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            initData();

            // 创建2个GL Program，第一个将图片的蓝色通道全部设为0.5，第二做水平方向模糊
            // Create two GL programs, and one is used for set the blue channel to 0.5, while the other is used for horizontal blur
            programId0 = createGLProgram(vertexShaderCode, fragmentShaderCode0);
            programId1 = createGLProgram(vertexShaderCode, fragmentShaderCode1);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            glSurfaceViewWidth = width;
            glSurfaceViewHeight = height;
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            //必需在texture操作之前建立
            // 初始化frame buffer
            // Init the frame buffer
            initFrameBuffer(glSurfaceViewWidth, glSurfaceViewHeight);

            // 绑定第0个GL Program
            // Bind GL program 0
            bindGLProgram(programId0, imageTexture, textureCoordinateDataBuffer0);

            // 绑定frame buffer
            // Bind the frame buffer
            bindFrameBuffer(frameBuffer);

            // 执行渲染，渲染效果为将图片的蓝色通道全部设为0.5
            // Perform rendering, and we can get the result of blue channel set to 0.5
            render();

            // 绑定第1个GL Program
            // Bind GL program 1
            bindGLProgram(programId1, frameBufferTexture, textureCoordinateDataBuffer1);

            // 绑定0号frame buffer
            // Bind the 0# frame buffer
            bindFrameBuffer(0);

            // 执行渲染，渲染效果水平方向的模糊
            // Perform rendering, and we can get the result of horizontal blur base on the previous result
            render();
        }

        private int createGLProgram(String vertexShaderCode,String fragmentShaderCode){

            // 创建GL程序
            // Create the GL program
            int programId = GLES20.glCreateProgram();

            // 加载、编译vertex shader和fragment shader
            // Load and compile vertex shader and fragment shader
            int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
            int fragmentShader= GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
            GLES20.glShaderSource(vertexShader, vertexShaderCode);
            GLES20.glShaderSource(fragmentShader, fragmentShaderCode);
            GLES20.glCompileShader(vertexShader);
            GLES20.glCompileShader(fragmentShader);

            // 将shader程序附着到GL程序上
            // Attach the compiled shaders to the GL program
            GLES20.glAttachShader(programId, vertexShader);
            GLES20.glAttachShader(programId, fragmentShader);

            // 链接GL程序
            // Link the GL program
            GLES20.glLinkProgram(programId);

            OpenglUtil.checkGLError();

            return programId;

        }

        private void initData() {

            // 将三角形顶点数据放入buffer中
            // Put the triangle vertex data into the vertexDataBuffer
            vertexDataBuffer = ByteBuffer.allocateDirect(vertexData.length * java.lang.Float.SIZE / 8)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer();
            vertexDataBuffer.put(vertexData);
            vertexDataBuffer.position(0);

            // 将纹理坐标数据放入buffer中
            // Put the texture coordinates into the textureCoordinateDataBuffer
            textureCoordinateDataBuffer0 = ByteBuffer.allocateDirect(textureCoordinateData0.length * java.lang.Float.SIZE / 8)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer();
            textureCoordinateDataBuffer0.put(textureCoordinateData0);
            textureCoordinateDataBuffer0.position(0);

            textureCoordinateDataBuffer1 = ByteBuffer.allocateDirect(textureCoordinateData1.length * java.lang.Float.SIZE / 8)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer();
            textureCoordinateDataBuffer1.put(textureCoordinateData1);
            textureCoordinateDataBuffer1.position(0);

            // 创建图片纹理
            // Create texture
            int[] textures = new int[]{0};
            GLES20.glGenTextures(textures.length, textures, 0);
            imageTexture = textures[0];

            // 设置纹理参数
            // Set texture parameters
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, imageTexture);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

            // 解码图片并加载到纹理中
            // Decode the image and load it into texture
            Bitmap bitmap = OpenglUtil.decodeBitmapFromAssets(OpenglFrameBufferActivity.this,"image_0.jpg");
            ByteBuffer b = ByteBuffer.allocate(bitmap.getWidth() * bitmap.getHeight() * 4);
            bitmap.copyPixelsToBuffer(b);
            b.position(0);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, bitmap.getWidth(), bitmap.getHeight(), 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, b);
        }

        private void initFrameBuffer(int width,int height) {

            // 创建frame buffer绑定的纹理
            // Create texture which binds to frame buffer
            int[] textures = new int[]{0};
            GLES20.glGenTextures(textures.length, textures, 0);
            frameBufferTexture = textures[0];

            // 创建frame buffer
            // Create frame buffer
            int[] frameBuffers = new int[]{0};
            GLES20.glGenFramebuffers(frameBuffers.length, frameBuffers, 0);
            frameBuffer = frameBuffers[0];

            //綁定texture
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, frameBufferTexture);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);

            // 将frame buffer与texture绑定
            // Bind the texture to frame buffer
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffer);
            GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, frameBufferTexture, 0);
        }

        private void bindGLProgram(int programId,int texture,FloatBuffer textureCoordinateDataBuffer) {

            // 应用GL程序
            // Use the GL program
            GLES20.glUseProgram(programId);

            // 获取字段a_position在shader中的位置
            // Get the location of a_position in the shader
            int aPositionLocation = GLES20.glGetAttribLocation(programId, "a_position");

            // 启动对应位置的参数
            // Enable the parameter of the location
            GLES20.glEnableVertexAttribArray(aPositionLocation);

            // 指定a_position所使用的顶点数据
            // Specify the data of a_position
            GLES20.glVertexAttribPointer(aPositionLocation, VERTEX_COMPONENT_COUNT, GLES20.GL_FLOAT, false,0, vertexDataBuffer);

            // 获取字段a_textureCoordinate在shader中的位置
            // Get the location of a_textureCoordinate in the shader
            int aTextureCoordinateLocation = GLES20.glGetAttribLocation(programId, "a_textureCoordinate");

            // 启动对应位置的参数
            // Enable the parameter of the location
            GLES20.glEnableVertexAttribArray(aTextureCoordinateLocation);

            // 指定a_textureCoordinate所使用的顶点数据
            // Specify the data of a_textureCoordinate
            GLES20.glVertexAttribPointer(aTextureCoordinateLocation, TEXTURE_COORDINATE_COMPONENT_COUNT, GLES20.GL_FLOAT, false,0, textureCoordinateDataBuffer);

            // 绑定纹理并设置u_texture参数
            // Bind the texture and set the u_texture parameter
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
            int uTextureLocation = GLES20.glGetAttribLocation(programId, "u_texture");
            GLES20.glUniform1i(uTextureLocation, 0);

        }

        private void bindFrameBuffer(int frameBuffer) {

            // 绑定frame buffer
            // Bind the frame buffer
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffer);

        }

        private void render() {

            // 设置清屏颜色
            // Set the color which the screen will be cleared to
            GLES20.glClearColor(0.9f, 0.9f, 0.9f, 1f);

            // 清屏
            // Clear the screen
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

            // 设置视口，这里设置为整个GLSurfaceView区域
            // Set the viewport to the full GLSurfaceView
            GLES20.glViewport(0, 0, glSurfaceViewWidth, glSurfaceViewHeight);

            // 调用draw方法用TRIANGLES的方式执行渲染，顶点数量为3个
            // Call the draw method with GL_TRIANGLES to render 3 vertices
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexData.length / VERTEX_COMPONENT_COUNT);

        }
    }
}

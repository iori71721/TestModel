package com.iori.custom.testmodel.rtmp.custom;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.view.Surface;

import com.iori.custom.common.reflect.ReflectHelper;
import com.iori.custom.testmodel.R;
import com.pedro.encoder.input.gl.render.filters.object.SurfaceFilterRender;
import com.pedro.encoder.utils.gl.GlUtil;

public class CustomSurfaceFilterRender extends SurfaceFilterRender {

    public CustomSurfaceFilterRender(SurfaceReadyCallback surfaceReadyCallback) {
        super(surfaceReadyCallback);
    }

    @Override
    protected void initGlFilter(Context context) {
        super.initGlFilter(context);
//        testInit(context);

    }

    private void testInit(Context context){
//        this.width = width;
//        this.height = height;
        GlUtil.checkGlError("initGl start");
        String vertexShader = GlUtil.getStringFromRaw(context, R.raw.simple_vertex);
        String fragmentShader = GlUtil.getStringFromRaw(context, R.raw.camera_fragment);

        int program = GlUtil.createProgram(vertexShader, fragmentShader);
        ReflectHelper.setValue(this, SurfaceFilterRender.class, "program", program, new ReflectHelper.ReflectParser() {
            @Override
            public Object parse(Object o) {
                return Integer.parseInt(o.toString());
            }
        });


        int aPositionHandle = GLES20.glGetAttribLocation(program, "aPosition");
        ReflectHelper.setValue(this, SurfaceFilterRender.class, "aPositionHandle", aPositionHandle, new ReflectHelper.ReflectParser() {
            @Override
            public Object parse(Object o) {
                return Integer.parseInt(o.toString());
            }
        });

//        aTextureCameraHandle = GLES20.glGetAttribLocation(program, "aTextureCoord");



        int uMVPMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");
        ReflectHelper.setValue(this, SurfaceFilterRender.class, "uMVPMatrixHandle", uMVPMatrixHandle, new ReflectHelper.ReflectParser() {
            @Override
            public Object parse(Object o) {
                return Integer.parseInt(o.toString());
            }
        });

        int uSTMatrixHandle = GLES20.glGetUniformLocation(program, "uSTMatrix");
        ReflectHelper.setValue(this, SurfaceFilterRender.class, "uSTMatrixHandle", uSTMatrixHandle, new ReflectHelper.ReflectParser() {
            @Override
            public Object parse(Object o) {
                return Integer.parseInt(o.toString());
            }
        });

        uSTMatrixHandle = GLES20.glGetUniformLocation(program, "uSTMatrix");

        ReflectHelper.setValue(this, SurfaceFilterRender.class, "uSTMatrixHandle", uSTMatrixHandle, new ReflectHelper.ReflectParser() {
            @Override
            public Object parse(Object o) {
                return Integer.parseInt(o.toString());
            }
        });

        //camera texture
        int[] textureID=new int[] { -1 };
        GlUtil.createExternalTextures(1, textureID, 0);
        SurfaceTexture surfaceTexture = new SurfaceTexture(textureID[0]);
        ReflectHelper.setValue(this, SurfaceFilterRender.class, "surfaceTexture", surfaceTexture, new ReflectHelper.ReflectParser() {
            @Override
            public Object parse(Object o) {
                return (SurfaceTexture)o;
            }
        });
        surfaceTexture.setDefaultBufferSize(getWidth(), getHeight());
        Surface surface = new Surface(surfaceTexture);

        ReflectHelper.setValue(this, SurfaceFilterRender.class, "surface", surface, new ReflectHelper.ReflectParser() {
            @Override
            public Object parse(Object o) {
                return (Surface)o;
            }
        });

        SurfaceReadyCallback surfaceReadyCallback=ReflectHelper.getValue(this,SurfaceFilterRender.class,"surfaceReadyCallback",SurfaceReadyCallback.class
        );

        if (surfaceReadyCallback != null) surfaceReadyCallback.surfaceReady();
    }
}

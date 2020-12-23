package com.iori.custom.testmodel.opengl;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.iori.custom.testmodel.R;

public class BaseOpenGlActivity extends Activity {
    protected GLSurfaceView glSurfaceView;
    protected int glSurfaceViewWidth;
    protected int glSurfaceViewHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opengl_triangle_activity);
        glSurfaceView=findViewById(R.id.glSurfaceView);
    }
}

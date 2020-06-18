package com.iori.custom.testmodel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iori.custom.testmodel.ratioCamera.Camera2SurfaceView;

import java.util.ArrayList;
import java.util.List;

public class RatioCameraPreviewActivity extends AppCompatActivity {
    private SurfaceView camera_preview;
    private Camera2SurfaceView camera_preview_custom;
    private CameraManager cameraManager;
    private static final String CAMERA_ID = "1";
    private CameraDevice cameraDevice;
    private List<Surface> drawSurfaces = new ArrayList<>(10);
    private CameraCaptureSession cameraCaptureSession;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ratio_camera_preview_activity);
        camera_preview=findViewById(R.id.camera_preview);
        camera_preview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                initCamera();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

        camera_preview_custom=findViewById(R.id.camera_preview_custom);
    }

    @SuppressLint("MissingPermission")
    private void initCamera(){
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        drawSurfaces.add(camera_preview.getHolder().getSurface());

        try {
            cameraManager.openCamera(CAMERA_ID, new CameraDevice.StateCallback() {
                @Override
                public void onOpened(@NonNull final CameraDevice camera) {
                    try {
                        cameraDevice = camera;
                        camera.createCaptureSession(drawSurfaces, new CameraCaptureSession.StateCallback() {
                            @Override
                            public void onConfigured(@NonNull CameraCaptureSession session) {
                                try {
                                    cameraCaptureSession = session;
                                    CaptureRequest.Builder builder = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                                    builder.addTarget(drawSurfaces.get(0));
                                    CaptureRequest captureRequest = builder.build();
                                    session.setRepeatingRequest(captureRequest, null, null);
                                } catch (CameraAccessException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                            }
                        }, null);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onDisconnected(@NonNull CameraDevice camera) {

                }

                @Override
                public void onError(@NonNull CameraDevice camera, int error) {

                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}

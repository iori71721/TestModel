package com.iori.custom.testmodel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.pedro.encoder.input.gl.render.filters.object.SurfaceFilterRender;
import com.pedro.encoder.input.video.CameraHelper;
import com.pedro.rtplibrary.rtmp.RtmpCamera2;
import com.pedro.rtplibrary.view.OpenGlView;

import net.ossrs.rtmp.ConnectCheckerRtmp;

import java.util.ArrayList;
import java.util.List;

/**
 * keep to ask lib owner.
 */
public class MoreCameraPreview_back extends AppCompatActivity {
    private static final int PERMISSION_REQUEST = 1;
    private SurfaceView first;
    private SurfaceView second;
    private CameraManager cameraManager;
    public static final String[] PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private List<Surface> drawSurfaces = new ArrayList<>(10);
    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSession;
    private Handler handler;
    private OpenGlView surfaceView;
    private RtmpCamera2 streamCamera;
    private SurfaceFilterRender surfaceFilterRender;
    private static final String CAMERA_ID = "1";

    private GLSurfaceView glSurfaceView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_camera_preview_layout);
        surfaceView = findViewById(R.id.camera_surface);
        first = findViewById(R.id.first);
        second = findViewById(R.id.second);
        handler = new Handler();
        if (allPermissionsGranted(PERMISSIONS)) {
            initCamera();
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST) {
            if (permissions.length == grantResults.length) {
                initCamera();
            }
        }
    }

    /**
     * Process result from permission request dialog box, has the request
     * been granted? If yes, start Camera. Otherwise display a toast
     */
    protected boolean allPermissionsGranted(String[] requestPermissions) {
        boolean allPermissionsGranted = true;
        for (String checkPermission : requestPermissions) {
            if (ContextCompat.checkSelfPermission(this, checkPermission) != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
                break;
            }
        }
        return allPermissionsGranted;
    }

    private void addSurfaces(CaptureRequest.Builder builder) {
        for (Surface addSurface : drawSurfaces) {
            builder.addTarget(addSurface);
        }
    }

    private void initRtmp() {
        streamCamera = new RtmpCamera2(surfaceView, new ConnectCheckerRtmp() {
            @Override
            public void onConnectionSuccessRtmp() {

            }

            @Override
            public void onConnectionFailedRtmp(@NonNull String reason) {

            }

            @Override
            public void onNewBitrateRtmp(long bitrate) {

            }

            @Override
            public void onDisconnectRtmp() {

            }

            @Override
            public void onAuthErrorRtmp() {

            }

            @Override
            public void onAuthSuccessRtmp() {

            }
        });
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.d("iori_surface", "surfaceChanged: start preview");
                streamCamera.startPreview(CameraHelper.Facing.FRONT);

                drawSurfaces.add(surfaceView.getSurface());
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }

    /**
     * to ask git
     */
    private void drawCameraPreviewToSurfaceFilterRender() {
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        initRtmp();
        initSurfaceRender();


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (ActivityCompat.checkSelfPermission(MoreCameraPreview_back.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    cameraManager.openCamera(CAMERA_ID, new CameraDevice.StateCallback() {
                                @Override
                                public void onOpened(@NonNull final CameraDevice camera) {
                                    cameraDevice = camera;
                                    try {
                                        camera.createCaptureSession(drawSurfaces, new CameraCaptureSession.StateCallback() {
                                            @Override
                                            public void onConfigured(@NonNull CameraCaptureSession session) {
                                                try {
                                                    CaptureRequest.Builder builder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                                                    addSurfaces(builder);
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
                            }
                            , null);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
        },6000);

    }

    private void initSurfaceRender(){
        surfaceFilterRender=new SurfaceFilterRender(new SurfaceFilterRender.SurfaceReadyCallback() {
            @Override
            public void surfaceReady() {
                Log.d("iori_surface", "surfaceReady: ");
                drawSurfaces.add(surfaceFilterRender.getSurface());

                surfaceFilterRender.getSurfaceTexture().setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
                    @Override
                    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                        Log.d("iori_surface", "onFrameAvailable: ");
//                        surfaceTexture.updateTexImage();

                    }
                });

            }
        });

        surfaceFilterRender.setScale(50f, 50f);
        streamCamera.getGlInterface().setFilter(surfaceFilterRender);
    }

    private void initCamera(){
        drawCameraPreviewToSurfaceFilterRender();
    }
}

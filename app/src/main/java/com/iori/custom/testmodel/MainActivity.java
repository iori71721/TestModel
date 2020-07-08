package com.iori.custom.testmodel;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST=99;
    public static final String[] PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private Button more_camera_preview;
    private Button ratio_camera_preview;
    private Button longTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        Log.d("iori_long", "onCreate: 5");

        if(!allPermissionsGranted(PERMISSIONS)) {
            ActivityCompat.requestPermissions(this,PERMISSIONS,PERMISSION_REQUEST);
        }


        more_camera_preview=findViewById(R.id.more_camera_preview);

        more_camera_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MoreCameraPreview.class));
            }
        });

        ratio_camera_preview=findViewById(R.id.ratio_camera_preview);
        ratio_camera_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RatioCameraPreviewActivity.class));
            }
        });

        longTextView=findViewById(R.id.longTextView);
        longTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LongTextViewActivity.class));
            }
        });
    }

    /**
     * Process result from permission request dialog box, has the request
     * been granted? If yes, start Camera. Otherwise display a toast
     */
    protected boolean allPermissionsGranted(String[] requestPermissions){
        boolean allPermissionsGranted=true;
        for(String checkPermission:requestPermissions){
            if(ContextCompat.checkSelfPermission(this,checkPermission) != PackageManager.PERMISSION_GRANTED){
                allPermissionsGranted=false;
                break;
            }
        }
        return allPermissionsGranted;
    }
}

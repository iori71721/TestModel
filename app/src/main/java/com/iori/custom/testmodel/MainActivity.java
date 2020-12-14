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

import com.iori.custom.testmodel.lottery.LotteryActivity;
import com.iori.custom.testmodel.opengl.OpenglTriangleActivity;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST=99;
    public static final String[] PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private Button more_camera_preview;
    private Button ratio_camera_preview;
    private Button longTextView;
    private Button lottery;
    private Button winUserList;
    private Button fixedHeaderTable;
    private Button opengl_TriangleFilter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
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

        lottery=findViewById(R.id.lottery);
        lottery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LotteryActivity.class));
            }
        });

        winUserList=findViewById(R.id.winUserList);
        winUserList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,WinUserListActivity.class));
            }
        });

        fixedHeaderTable=findViewById(R.id.fixedHeaderTable);
        fixedHeaderTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,FixedHeaderTableActivity.class));
            }
        });

        opengl_TriangleFilter=findViewById(R.id.opengl_TriangleFilter);
        opengl_TriangleFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, OpenglTriangleActivity.class));
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

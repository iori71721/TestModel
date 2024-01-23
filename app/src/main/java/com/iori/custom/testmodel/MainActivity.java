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
import com.iori.custom.testmodel.opengl.ColorTriangleActivity;
import com.iori.custom.testmodel.opengl.CustomOpenglFrameBufferActivity;
import com.iori.custom.testmodel.opengl.OpenglFrameBufferActivity;
import com.iori.custom.testmodel.opengl.OpenglImageActivity;
import com.iori.custom.testmodel.opengl.OpenglLongImageActivity;
import com.iori.custom.testmodel.opengl.OpenglTriangleActivity;
import com.iori.custom.testmodel.opengl.TranslateActivity;

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
    private Button opengl_translate;
    private Button opengl_colorTriangle;
    private Button opengl_image;
    private Button opengl_frame_buffer;
    private Button opengl_frame_buffer_custom;
    private Button animation;
    private Button googleMap;
    private Button currentLocation;
    private Button streetMap;
    private Button mapMarker;
    private Button opengl_long_image;
    private Button scrollDel;
    private Button scrollDelSwipeRecyclerView;

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

        opengl_translate=findViewById(R.id.opengl_translate);
        opengl_translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TranslateActivity.class));
            }
        });

        opengl_colorTriangle=findViewById(R.id.opengl_colorTriangle);
        opengl_colorTriangle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ColorTriangleActivity.class));
            }
        });

        opengl_image=findViewById(R.id.opengl_image);
        opengl_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, OpenglImageActivity.class));
            }
        });

        opengl_frame_buffer=findViewById(R.id.opengl_frame_buffer);
        opengl_frame_buffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, OpenglFrameBufferActivity.class));
            }
        });

        opengl_frame_buffer_custom=findViewById(R.id.opengl_frame_buffer_custom);
        opengl_frame_buffer_custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CustomOpenglFrameBufferActivity.class));
            }
        });

        animation=findViewById(R.id.animation);
        animation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AnimationActivity.class));
            }
        });

        googleMap=findViewById(R.id.googleMap);
        googleMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,GoogleMapActivity.class));
            }
        });

        currentLocation=findViewById(R.id.currentLocation);
        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CurrentLocationActivity.class));
            }
        });

        streetMap=findViewById(R.id.streetMap);
        streetMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,StreetMapActivity.class));
            }
        });

        mapMarker=findViewById(R.id.mapMarker);
        mapMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MapMarkerActivity.class));
            }
        });

        opengl_long_image=findViewById(R.id.opengl_long_image);
        opengl_long_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, OpenglLongImageActivity.class));
            }
        });

        scrollDel=findViewById(R.id.scrollDel);
        scrollDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ScrollDelActivity.class));
            }
        });

        scrollDelSwipeRecyclerView=findViewById(R.id.scrollDelSwipeRecyclerView);
        scrollDelSwipeRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ScrollDelSwipeRecyclerViewActivity.class));
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

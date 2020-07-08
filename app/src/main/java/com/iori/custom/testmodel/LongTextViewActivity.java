package com.iori.custom.testmodel;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LongTextViewActivity extends AppCompatActivity {
    private Handler handler;
    private TextView longTextView;
    private LinearLayout longTextViewLayout;
    private String longText="123456789123456789下一位123456789123456789123456789123456789123456789123456789123456789下兩位123456789xyzend";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.long_text_view_activity);
        handler=new Handler();
        longTextView=findViewById(R.id.longTextView);
        longTextViewLayout=findViewById(R.id.longTextViewLayout);
        
        longTextView.post(new Runnable() {
            @Override
            public void run() {
                Log.d("iori_long", "run: init textView width "+longTextView.getWidth());
                longTextView.setWidth(4000);
                Log.d("iori_long", "run: force setup textView width ");
            }
        });


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                forceSetupWidth();
            }
        },3000);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                cycleMoveText();

            }
        },5000);


    }
    
    private void forceSetupWidth(){
        ViewGroup.LayoutParams params=longTextViewLayout.getLayoutParams();
        params.width=4000;
        longTextViewLayout.setLayoutParams(params);
        longTextViewLayout.post(new Runnable() {
            @Override
            public void run() {
                Log.d("iori_long", "run: forcesetup width longTextViewLayout.getWidth() "+longTextViewLayout.getWidth());
                longTextView.setWidth(4000);
                longTextView.setText(longText);
            }
        });

//        直接擴寬無效，還是需要先從父層layout擴寬
//        longTextView.setWidth(4000);
//        longTextView.setText(longText);

    }

    private void cycleMoveText(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                longTextView.setX(longTextView.getX()-1);
                cycleMoveText();
            }
        },16);
    }
}

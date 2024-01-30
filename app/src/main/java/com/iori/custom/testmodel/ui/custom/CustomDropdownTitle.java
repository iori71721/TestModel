package com.iori.custom.testmodel.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.iori.custom.testmodel.R;

public class CustomDropdownTitle extends LinearLayout {
    private LinearLayout triggerLayout;
    private TextView inputHint;
    private Spinner chooseSpinner;
    private ImageView rightImageView;

    public CustomDropdownTitle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        View root= LayoutInflater.from(context).inflate(R.layout.model_custom_dropdown_title,this);

        triggerLayout=root.findViewById(R.id.triggerLayout);
        inputHint=root.findViewById(R.id.inputHint);
        chooseSpinner=root.findViewById(R.id.chooseSpinner);
        rightImageView=root.findViewById(R.id.rightImageView);

        initLayout(root);
    }

    private void initLayout(View root) {
        triggerLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("iori_s", "onClick: CustomDropdownTitle triggerLayout triggerLayout.hasFocus() "+triggerLayout.hasFocus());
                triggerLayout.setActivated(true);
                chooseSpinner.performClick();
            }
        });

        triggerLayout.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Log.d("iori_s", "CustomDropdownTitle triggerLayout: onFocusChange "+b);
                triggerLayout.setActivated(b);
                if(b){
                    chooseSpinner.performClick();
                }
            }
        });

        chooseSpinner.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d("iori_s", "onTouch: chooseSpinner ");
//                點擊時轉移焦點
                triggerLayout.requestFocus();
                triggerLayout.setActivated(true);
                return false;
            }
        });
    }

    public LinearLayout getTriggerLayout() {
        return triggerLayout;
    }

    public TextView getInputHint() {
        return inputHint;
    }

    public Spinner getChooseSpinner() {
        return chooseSpinner;
    }

    public ImageView getRightImageView() {
        return rightImageView;
    }
}

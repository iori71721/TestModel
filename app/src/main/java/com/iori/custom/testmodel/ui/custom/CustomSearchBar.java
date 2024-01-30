package com.iori.custom.testmodel.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.iori.custom.testmodel.R;

public class CustomSearchBar extends LinearLayout {
    private LinearLayout triggerLayout;
    private LinearLayout leftImageLayout;
    private ImageView leftImageView;
    private EditText inputEditText;


    public CustomSearchBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        View root= LayoutInflater.from(context).inflate(R.layout.model_custom_search_bar,this);
        triggerLayout=root.findViewById(R.id.triggerLayout);
        leftImageLayout=root.findViewById(R.id.leftImageLayout);
        leftImageView=root.findViewById(R.id.leftImageView);
        inputEditText=root.findViewById(R.id.inputEditText);

        initLayout(root);
    }

    private void initLayout(View root) {
        triggerLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                triggerLayout.setActivated(true);

                inputEditText.requestFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(inputEditText,InputMethodManager.SHOW_FORCED);
            }
        });

//        可獲得focus的操作元件一併變更狀態
        inputEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                triggerLayout.setActivated(b);
            }
        });
    }

    public LinearLayout getTriggerLayout() {
        return triggerLayout;
    }

    public LinearLayout getLeftImageLayout() {
        return leftImageLayout;
    }

    public ImageView getLeftImageView() {
        return leftImageView;
    }

    public EditText getInputEditText() {
        return inputEditText;
    }
}

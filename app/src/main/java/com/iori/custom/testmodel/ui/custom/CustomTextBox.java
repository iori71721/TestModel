package com.iori.custom.testmodel.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.iori.custom.testmodel.R;

public class CustomTextBox extends LinearLayout {
    private LinearLayout triggerLayout;
    private TextView hintLabel;
    private EditText inputEditText;

    public CustomTextBox(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        View root= LayoutInflater.from(context).inflate(R.layout.model_custom_textbox,this);
        triggerLayout=root.findViewById(R.id.triggerLayout);
        hintLabel=root.findViewById(R.id.hintLabel);
        inputEditText=root.findViewById(R.id.inputEditText);

        initLayout(root);
    }

    private void initLayout(View root) {

        triggerLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("iori_s", "onClick: CustomTextBox triggerLayout ");
                triggerLayout.setActivated(true);
                inputEditText.requestFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(inputEditText,InputMethodManager.SHOW_FORCED);
            }
        });

        inputEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Log.d("iori_s", "onClick: CustomTextBox onFocusChange "+b);
                triggerLayout.setActivated(b);
            }
        });
    }

    public LinearLayout getTriggerLayout() {
        return triggerLayout;
    }

    public TextView getHintLabel() {
        return hintLabel;
    }

    public EditText getInputEditText() {
        return inputEditText;
    }
}

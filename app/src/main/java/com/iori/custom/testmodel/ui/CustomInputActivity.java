package com.iori.custom.testmodel.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iori.custom.common.string.StringTool;
import com.iori.custom.testmodel.R;
import com.iori.custom.testmodel.ui.custom.CustomDropdownTitle;
import com.iori.custom.testmodel.ui.custom.CustomInputEditText;
import com.iori.custom.testmodel.ui.custom.CustomSearchBar;
import com.iori.custom.testmodel.ui.custom.CustomTextBox;

import java.util.Locale;

public class CustomInputActivity extends AppCompatActivity {
    private CustomInputEditText customInputEditText;
    private CustomInputEditText customInput2;
    private CustomTextBox customTextBox;
    private CustomDropdownTitle customDropdownTitle;
    private CustomSearchBar customSearchBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("iori_s", "onCreate: 1");

        setContentView(R.layout.custom_input_activity);

        customInputEditText=findViewById(R.id.customInput);
        customInput2=findViewById(R.id.customInput2);
        customTextBox=findViewById(R.id.customTextBox);
        customDropdownTitle=findViewById(R.id.customDropdownTitle);
        customSearchBar=findViewById(R.id.customSearchBar);

        initCustomInputEditText(customInputEditText);
        customInputEditText.getRightImageLayout().setVisibility(View.GONE);

        initCustomInputEditText(customInput2);
        customInput2.getRightImageLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("iori_s", "onClick: right image ");
            }
        });

        initCustomTextBox(customTextBox);
        initCustomDrowdownTitle(customDropdownTitle);
        initCustomSearchBar(customSearchBar);
    }

    private void initCustomSearchBar(CustomSearchBar customSearchBar) {
        customSearchBar.getTriggerLayout().setBackgroundResource(R.drawable.model_stream_enable_full_green_background);
        customSearchBar.getInputEditText().setHint("t_搜尋商品");
        customSearchBar.getLeftImageView().setImageResource(R.drawable.search_bg);

        ViewGroup.LayoutParams params=customSearchBar.getTriggerLayout().getLayoutParams();
        params.height=120;

        customSearchBar.getTriggerLayout().setLayoutParams(params);
    }

    private void initCustomDrowdownTitle(CustomDropdownTitle customDropdownTitle) {
        ArrayAdapter adapter = new ArrayAdapter(this,R.layout.align_left_spinner_text,new String[]{"購物車","常溫","冷藏","冷凍"});

        //設定下拉選單的樣式
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        customDropdownTitle.getChooseSpinner().setAdapter(adapter);

        customDropdownTitle.getTriggerLayout().setBackgroundResource(R.drawable.model_stream_enable_full_green_background);
        customDropdownTitle.getRightImageView().setImageResource(R.drawable.dropdown);
        customDropdownTitle.getInputHint().setText("t_購物車");
    }

    private void initCustomTextBox(CustomTextBox customTextBox) {
        customTextBox.getTriggerLayout().setBackgroundResource(R.drawable.model_stream_enable_full_green_background);
    }

    private void initCustomInputEditText(final CustomInputEditText customInputEditText) {
        //        ok測試
        customInputEditText.setText("hello baby");
        customInputEditText.setHintText("t_直播名稱");
//        customInputEditText.setTextSize(32);
//        customInputEditText.setHintSize(24);
        customInputEditText.setTextColor(getColor(R.color.black_353535));
        customInputEditText.setHintColor(getColor(R.color.gray_8D8E90));
        customInputEditText.setBackgroundResource(R.drawable.model_stream_enable_full_green_background);

        customInputEditText.getInputEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input=editable.toString();
//                暫時取消
//                displayError(input);
            }
        });

        customInputEditText.setCallback(new CustomInputEditText.Callback() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    displayError(customInputEditText.getText(),customInputEditText);
                }
            }
        });
    }

    private void displayError(String input,CustomInputEditText customInputEditText){
        input=input.toLowerCase(Locale.ROOT);
        if(StringTool.isEmpty(input)){
//            暫時取消
            customInputEditText.displayErrorLayout("t_不可輸入空值");
        }else if(input.contains("error")){
            customInputEditText.displayErrorLayout("t_不可輸入error");
        }else if(input.contains("empty")){
//            暫時取消，會顯示錯誤
//            customInputEditText.setText("");
//            customInputEditText.displayErrorLayout("t_觀看空輸入的錯誤狀況");
        }
    }
}

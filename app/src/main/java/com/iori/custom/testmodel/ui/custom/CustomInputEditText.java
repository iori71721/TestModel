package com.iori.custom.testmodel.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.SuperscriptSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.Nullable;

import com.iori.custom.common.string.StringTool;
import com.iori.custom.testmodel.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class CustomInputEditText extends LinearLayout {
    private LinearLayout singleLineInputLayout;
    private TextView inputHint;
    private EditText inputEditText;
    private @CustomInputEditText.Status.Data int status= CustomInputEditText.Status.PREVIEW;
    private float scaleInit=1;
    private float scale=0.25f;
    private long animationDuration=100;


    private String hintText="";
    private int hintColor= Color.BLACK;
    /**
     * px
     */
    private int hintSize;

    private String inputText="";
    private int textColor= Color.BLACK;
    /**
     * px
     */
    private int textSize;

    private LinearLayout errorLayout;
    private TextView errorTextView;
    private Drawable defaultDrawable;
    private Callback callback;

    private LinearLayout rightImageLayout;
    private ImageView rightImageView;

    public CustomInputEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
//        setOrientation(VERTICAL);
        View root= LayoutInflater.from(context).inflate(R.layout.model_custom_input,this);

        singleLineInputLayout=root.findViewById(R.id.model_singleLineInputLayout);
        inputHint=root.findViewById(R.id.model_inputHint);
        inputEditText=root.findViewById(R.id.model_inputEditText);
        errorLayout=root.findViewById(R.id.model_errorLayout);
        errorTextView=root.findViewById(R.id.model_errorText);
        rightImageLayout=root.findViewById(R.id.rightImageLayout);
        rightImageView=root.findViewById(R.id.rightImageView);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomInputEditText);

        hintText = a.getString(R.styleable.CustomInputEditText_hint);
        inputText= a.getString(R.styleable.CustomInputEditText_text);
        hintColor= a.getColor(R.styleable.CustomInputEditText_textColorHint,Color.BLACK);
        textColor= a.getColor(R.styleable.CustomInputEditText_textColor,Color.BLACK);
        textSize=a.getDimensionPixelOffset(R.styleable.CustomInputEditText_textSize,50);
        hintSize=a.getDimensionPixelOffset(R.styleable.CustomInputEditText_hintSize,50);

        Log.d("iori_s", "CustomInputEditText: textSize "+textSize
        +" hintSize "+hintSize);

        a.recycle();

        initLayout(root);
    }

    private void initLayout(View root) {
        setText(inputText);
        defaultDrawable=singleLineInputLayout.getBackground();
        singleLineInputLayout.setActivated(false);

        disPlayPreViewLayout();

        singleLineInputLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("iori_s", "onClick: singleLineInputLayout ");
//                此處設定無效，因為已經產生點擊了，移至輸入移開時變更
//                singleLineInputLayout.setBackground(defaultDrawable);

                boolean statusChange=status != CustomInputEditText.Status.INPUT;
                if(statusChange) {
                    status = CustomInputEditText.Status.INPUT;
                }
                displayInputLayout(statusChange);

            }
        });

        inputEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                singleLineInputLayout.setBackground(defaultDrawable);

                if(!b) {
                    status = CustomInputEditText.Status.PREVIEW;
                    restorePreViewLayout();
                }else{
                    displayInputLayout(status == Status.INPUT);
                }

                if(callback != null){
                    callback.onFocusChange(view,b);
                }
            }
        });
    }

    private void restorePreViewLayout() {
        Animation scaleAnimation=new ScaleAnimation(scaleInit,scaleInit+scale,scaleInit,scaleInit+scale);

        AnimationSet animationSet=new AnimationSet(false);
        animationSet.setFillAfter(true);
        animationSet.addAnimation(scaleAnimation);
        animationSet.setDuration(animationDuration);

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        if(StringTool.isEmpty(getText())) {
            inputHint.startAnimation(animationSet);
            inputEditText.startAnimation(animationSet);
        }

        disPlayPreViewLayout();
    }

    public void setText(String inputText) {
        inputEditText.setText(inputText);
        disPlayPreViewLayout();
    }

    public String getText() {
        return inputEditText.getText().toString();
    }

    private void disPlayPreViewLayout() {
        displayErrorLayout(false,"");
        inputHint.setTextSize(TypedValue.COMPLEX_UNIT_PX,hintSize);
        inputEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
        inputEditText.setTextColor(textColor);

        singleLineInputLayout.setActivated(false);

        if(StringTool.isEmpty(getText())){
            disPlayEmptyInputPreViewLayout();
        }else{
            displayNonEmptyInputPreviewLayout();
        }
    }

    private void disPlayEmptyInputPreViewLayout() {
        status= CustomInputEditText.Status.PREVIEW;
        String inputText=inputEditText.getText().toString();
        String setupText=StringTool.isEmpty(inputText)?hintText:inputText;

        inputEditText.clearAnimation();
        inputEditText.setVisibility(GONE);

        setHintTextToPreviewMode(setupText);
    }

    private void displayNonEmptyInputPreviewLayout(){
        inputEditText.clearAnimation();
        inputEditText.setVisibility(VISIBLE);
        status= CustomInputEditText.Status.PREVIEW;
        setHintTextToInputMode(hintText);
    }

    private void setHintTextToPreviewMode(String text){
        inputHint.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
        inputHint.setTextColor(textColor);
        inputHint.setText(text);
    }

    private void setHintTextToInputMode(String text){
        inputHint.setTextSize(TypedValue.COMPLEX_UNIT_PX,hintSize);
        inputHint.setTextColor(hintColor);
        inputHint.setText(text);
    }

    private void displayInputLayout(boolean runAnimation) {
        displayErrorLayout(false,"");
        inputEditText.setVisibility(VISIBLE);

        singleLineInputLayout.setActivated(true);
        Animation scaleAnimation=new ScaleAnimation(scaleInit,scaleInit-scale,scaleInit,scaleInit-scale);

        AnimationSet animationSet=new AnimationSet(false);
        animationSet.setFillAfter(true);
        animationSet.addAnimation(scaleAnimation);
        animationSet.setDuration(animationDuration);

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        if(runAnimation && StringTool.isEmpty(getText())) {
            inputHint.startAnimation(animationSet);
            inputEditText.startAnimation(animationSet);
        }

        setHintTextToInputMode(hintText);
        if(!inputEditText.hasFocus()) {
            inputEditText.requestFocus();

            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(inputEditText,InputMethodManager.SHOW_FORCED);
            singleLineInputLayout.setActivated(true);
        }
    }

    private void displayErrorLayout(boolean visible,String errorText){
        if(visible) {
            disPlayPreViewLayout();
            errorLayout.setVisibility(VISIBLE);
            singleLineInputLayout.setBackgroundResource(R.drawable.model_stream_all_radius_red);

            Animation animation=new AlphaAnimation(0,100);
            animation.setDuration(animationDuration);
            errorLayout.startAnimation(animation);
        }else{
            errorLayout.setVisibility(GONE);
        }

        String setupText="*"+errorText;
        SpannableString setupSpannableString=new SpannableString(setupText);
        setupSpannableString.setSpan(new SuperscriptSpan(),0,1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        errorTextView.setText(setupSpannableString);
    }

    public void displayErrorLayout(String errorText){
        displayErrorLayout(true,errorText);
    }

    public LinearLayout getSingleLineInputLayout() {
        return singleLineInputLayout;
    }

    public TextView getInputHint() {
        return inputHint;
    }

    public EditText getInputEditText() {
        return inputEditText;
    }

    public String getHintText() {
        return hintText;
    }

    public void setHintText(String hintText) {
        this.hintText = hintText;
        disPlayPreViewLayout();
    }

    public int getHintColor() {
        return hintColor;
    }

    public void setHintColor(int hintColor) {
        this.hintColor = hintColor;
        disPlayPreViewLayout();
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        disPlayPreViewLayout();
    }

    public int getHintSize() {
        return hintSize;
    }

    public void setHintSize(int hintSize) {
        this.hintSize = hintSize;
        disPlayPreViewLayout();
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        disPlayPreViewLayout();
    }

    public LinearLayout getRightImageLayout() {
        return rightImageLayout;
    }

    public ImageView getRightImageView() {
        return rightImageView;
    }

    public void setBackgroundResource(@DrawableRes int resid) {
        singleLineInputLayout.setBackgroundResource(resid);
        defaultDrawable=singleLineInputLayout.getBackground();
        disPlayPreViewLayout();
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public static class Status{
        public static final int PREVIEW=0;
        public static final int INPUT=1;

        @IntDef({PREVIEW,INPUT})
        @Retention(RetentionPolicy.SOURCE)
        public static @interface Data{

        }
    }

    public static interface Callback{
        void onFocusChange(View view, boolean b);
    }
}

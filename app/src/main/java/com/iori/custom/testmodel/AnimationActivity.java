package com.iori.custom.testmodel;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AnimationActivity extends AppCompatActivity implements View.OnClickListener, ViewSwitcher.ViewFactory {
    private ImageView animationView;
    private Button frameAnimation;
    private Button alphaAnimation;
    private Button scaleAnimation;
    private Button roateAnimation;
    private Button translateAnimation;
    private Button combinationAnimation;
    private ImageSwitcher animationSwitcher;
    private Button inAndOutAnimation;
    private Button leftToRightAnimation;
    private Button propertyAnimation;
    private Button valueAnimator;
    private Handler handler;
    private int imageIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation_activity);
        handler=new Handler();

        animationView=findViewById(R.id.animationView);
        frameAnimation=findViewById(R.id.frameAnimation);
        alphaAnimation=findViewById(R.id.alphaAnimation);
        scaleAnimation=findViewById(R.id.scaleAnimation);
        roateAnimation=findViewById(R.id.roateAnimation);
        translateAnimation=findViewById(R.id.translateAnimation);
        combinationAnimation=findViewById(R.id.combinationAnimation);
        animationSwitcher=findViewById(R.id.animationSwitcher);
        inAndOutAnimation=findViewById(R.id.inAndOutAnimation);
        leftToRightAnimation=findViewById(R.id.leftToRightAnimation);
        propertyAnimation=findViewById(R.id.propertyAnimation);
        valueAnimator=findViewById(R.id.valueAnimator);

        frameAnimation.setOnClickListener(this);
        alphaAnimation.setOnClickListener(this);
        scaleAnimation.setOnClickListener(this);
        roateAnimation.setOnClickListener(this);
        translateAnimation.setOnClickListener(this);
        combinationAnimation.setOnClickListener(this);
        inAndOutAnimation.setOnClickListener(this);
        leftToRightAnimation.setOnClickListener(this);
        propertyAnimation.setOnClickListener(this);
        valueAnimator.setOnClickListener(this);

        initImageSwitcher();
    }

    private void initImageSwitcher() {
        animationSwitcher.setFactory(this);
    }

    private void startFrameAnimation(){
//        startFrameAnimation_xml();
        startFrameAnimation_code();
    }

    private void startFrameAnimation_xml(){
        AnimationDrawable animationDrawable=(AnimationDrawable) getDrawable(R.drawable.frame_animation);
        animationView.setImageDrawable(animationDrawable);
        animationDrawable.start();
    }

    private void startFrameAnimation_code(){
        final AnimationDrawable animationDrawable=(AnimationDrawable) getDrawable(R.drawable.frame_animation);
        animationView.setImageDrawable(animationDrawable);

        animationDrawable.addFrame(getDrawable(R.drawable.stop_stream),5000);

        animationDrawable.setOneShot(true);

//        animationDrawable.setAlpha(50);

        animationDrawable.start();


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(AnimationActivity.this,"stop startFrameAnimation_code() ",Toast.LENGTH_SHORT).show();
                animationDrawable.stop();
            }
        },5000);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.frameAnimation:
                startFrameAnimation();
                break;
            case R.id.alphaAnimation:
                startAlphaAnimation();
                break;
            case R.id.scaleAnimation:
                startScaleAnimation();
                break;
            case R.id.roateAnimation:
                startRoateAnimation();
                break;
            case R.id.translateAnimation:
                tartTranslateAnimation();
                break;
            case R.id.combinationAnimation:
                combinationAnimation();
                break;
            case R.id.inAndOutAnimation:
                inAndOutAnimation();
                break;
            case R.id.leftToRightAnimation:
                leftToRightAnimation();
                break;
            case R.id.propertyAnimation:
                propertyAnimation();
                break;
            case R.id.valueAnimator:
                valueAnimator();
                break;
        }
    }

    private void valueAnimator() {
        ValueAnimator valueAnimator=ValueAnimator.ofInt(0,255);
        valueAnimator.setDuration(10000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int val = (Integer)animation.getAnimatedValue();
                animationView.setBackgroundColor(0xFF000000 | val << 8);
                Log.d("iori", "onAnimationUpdate: value "+val);
            }
        });
        valueAnimator.start();
    }

    private void propertyAnimation() {
//        startPropertyAnimation_xml();

        startPropertyAnimation_code();
    }

    private void startPropertyAnimation_code() {
        ObjectAnimator scaleX=ObjectAnimator.ofFloat(animationView, "scaleX",0.2f,0.5f);
        scaleX.setDuration(2000);

        ObjectAnimator scaleY = ObjectAnimator.ofFloat(animationView, "scaleY", 0.2f, 0.5f);
        scaleY.setDuration(2000);

        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(scaleX,scaleY);

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d("iori_animation", "onAnimationEnd: AnimatorSet");
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        
        animatorSet.start();
    }

    private void startPropertyAnimation_xml() {
        AnimatorSet scaleAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(this,  R.animator.scale_property);
        scaleAnimator.setTarget(animationView);
        scaleAnimator.start();
    }

    private void leftToRightAnimation() {
//        setup animation time
        animationSwitcher.getInAnimation().setDuration(2000);
        animationSwitcher.getOutAnimation().setDuration(2000);

        changeImageSwitcherImage();
    }

    private void inAndOutAnimation() {
        animationSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.roate_animation));

        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0F, 0.0F);
        alphaAnimation.setDuration(2000);

        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0F, 0.0F, 1.0F, 0.0F,
                Animation.RELATIVE_TO_SELF,
                0.5F,
                Animation.RELATIVE_TO_SELF,
                0.5F);
        scaleAnimation.setDuration(2000);

        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF,
                0.5F,
                Animation.RELATIVE_TO_SELF,
                0.5F);
        rotateAnimation.setDuration(2000);

        TranslateAnimation translateAnimation = new TranslateAnimation(0, 500, 0, 0);
        translateAnimation.setDuration(2000);

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(translateAnimation);

        animationSwitcher.setOutAnimation(animationSet);
        changeImageSwitcherImage();

    }

    private void changeImageSwitcherImage() {
        //        一定要切換圖片才會觸發效果
        switch(imageIndex)
        {
            case 0:
                animationSwitcher.setImageResource(R.drawable.user);
                imageIndex = 1;
                break;
            case 1:
                animationSwitcher.setImageResource(R.drawable.password);
                imageIndex = 2;
                break;
            case 2:
                animationSwitcher.setImageResource(R.drawable.server);
                imageIndex = 0;
                break;
        }
    }

    private void combinationAnimation() {
//        startCombinationAnimation_xml();

        startCombinationAnimation_code();
    }

    private void startCombinationAnimation_code() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0F, 0.5F);
        alphaAnimation.setDuration(2000);

        ScaleAnimation scaleAnimation = new ScaleAnimation(0.0F, 1.5F, 0.0F, 1.5F, Animation.RELATIVE_TO_SELF, 0.5F,
                Animation.RELATIVE_TO_SELF, 0.5F);
        scaleAnimation.setDuration(2000);

        RotateAnimation rotateAnimation = new RotateAnimation(360, 0, Animation.RELATIVE_TO_SELF, 0.5F,
                Animation.RELATIVE_TO_SELF, 0.5F);
        rotateAnimation.setDuration(2000);

        TranslateAnimation translateAnimation = new TranslateAnimation(-300, 300, 0, 0);
        translateAnimation.setDuration(2000);

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(translateAnimation);

        animationView.startAnimation(animationSet);
    }

    private void startCombinationAnimation_xml() {
        animationView.startAnimation(AnimationUtils.loadAnimation(this,R.anim.combination_animation));
    }

    private void tartTranslateAnimation() {
//        startTranslateAnimation_xml();

        startTranslateAnimation_code();
    }

    private void startTranslateAnimation_code() {
        TranslateAnimation translateAnimation =
                new TranslateAnimation(-200,
                        200,
                        -200,
                        200);
        translateAnimation.setDuration(3*1000);

        translateAnimation.setRepeatCount(3);
        translateAnimation.setRepeatMode(Animation.REVERSE);
        translateAnimation.setFillAfter(true);

        animationView.post(new Runnable() {
            @Override
            public void run() {
                Log.d("iori_animation", "run: setFillAfter before animation "+animationView.getX());
            }
        });
        
        animationView.startAnimation(translateAnimation);

        animationView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("iori_animation", "run: setFillAfter after animation "+animationView.getX());
            }
        },20*1000);

    }

    private void startTranslateAnimation_xml() {
        animationView.startAnimation(AnimationUtils.loadAnimation(this,R.anim.translate_animation));
    }

    private void startRoateAnimation() {
//        startRoateAnimation_xml();

        startRoateAnimation_code();
    }

    private void startRoateAnimation_code() {
        RotateAnimation rotateAnimation =new RotateAnimation(360,
                        0,
                        Animation.RELATIVE_TO_SELF,
                        0.5F,
                        Animation.RELATIVE_TO_SELF,
                        0f);
        rotateAnimation.setDuration(5000);
        animationView.startAnimation(rotateAnimation);
    }

    private void startRoateAnimation_xml() {
        animationView.startAnimation(AnimationUtils.loadAnimation(this,R.anim.roate_animation));
    }

    private void startScaleAnimation() {
//        startScaleAnimation_xml();

        startScaleAnimation_code();
    }

    private void startScaleAnimation_code() {
        ScaleAnimation scaleAnimation=new ScaleAnimation(0.5f,2.0f,0.5f,2.0f, Animation.RELATIVE_TO_SELF
                ,0F,Animation.RELATIVE_TO_SELF,0.5f);

        scaleAnimation.setDuration(5000);
        scaleAnimation.setRepeatCount(3);

        animationView.startAnimation(scaleAnimation);
    }

    private void startScaleAnimation_xml() {
        animationView.startAnimation(AnimationUtils.loadAnimation(this,R.anim.scale_animation));
    }

    private void startAlphaAnimation() {
//        startAlphaAnimation_xml();

        startAlphaAnimation_code();
    }

    private void startAlphaAnimation_xml() {
        animationView.startAnimation(AnimationUtils.loadAnimation(this,R.anim.alpha_animation));
    }

    private void startAlphaAnimation_code() {
        AlphaAnimation alphaAnimation=new AlphaAnimation(1.0f,0.2f);
        alphaAnimation.setDuration(5000);
        animationView.startAnimation(alphaAnimation);
    }

    @Override
    public View makeView() {
//        must implement and generate by code
        ImageView v = new ImageView(this);
        v.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return v;
    }
}

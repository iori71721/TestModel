package com.iori.custom.testmodel.lottery;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iori.custom.testmodel.R;

import java.util.ArrayList;
import java.util.List;

public class LotteryActivity extends AppCompatActivity {
    private LotteryListRecyclerView lotteryList;
    private LotteryRecyclerAdapter lotteryRecyclerAdapter;
    private View lottery_user_rectangle;
    private Handler handler;
    private Button lottery;
    private Button prepare_lottery;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lottery_activity);
        lotteryList=findViewById(R.id.lottery_list);
        lottery_user_rectangle=findViewById(R.id.lottery_user_rectangle);
        lottery=findViewById(R.id.lottery);
        prepare_lottery=findViewById(R.id.prepare_lottery);
        lottery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lotteryList.lotteryUser();
            }
        });
        prepare_lottery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lotteryList.startLottery();
            }
        });
        handler=new Handler();
        initLotteryList();
        startScroll();
    }

    private void startScroll(){
        lotteryList.startPreview();
    }

    private void initLotteryList(){
        lotteryRecyclerAdapter=new LotteryRecyclerAdapter(this);
        LotteryRecyclerAdapter.ItemData itemData;
        List<LotteryRecyclerAdapter.ItemData> addUsers=new ArrayList<>(100);
        for(int i=0;i<50;i++){
            itemData=new LotteryRecyclerAdapter.ItemData();
            itemData.userName="iori"+i;
            addUsers.add(itemData);
        }
        lotteryRecyclerAdapter.addlotteryUsers(addUsers);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);

        lotteryRecyclerAdapter.resizeUserRectangle=new LotteryRecyclerAdapter.ResizeUserRectangle() {
            @Override
            public void resize(int width, int height) {
                Log.d("iori_lottery", "listener resize: width "+width+" height "+height);
                ViewGroup.LayoutParams layoutParams=lottery_user_rectangle.getLayoutParams();
                layoutParams.width=width;
                layoutParams.height=height;
                lottery_user_rectangle.setLayoutParams(layoutParams);
                lottery_user_rectangle.post(new Runnable() {
                    @Override
                    public void run() {
                        lotteryList.lotteryUserRectangleLeft =lottery_user_rectangle.getLeft();
                    }
                });
            }
        };

        lotteryList.setLayoutManager(linearLayoutManager);
        lotteryList.setAdapter(lotteryRecyclerAdapter);
        lotteryList.handler=handler;
    }
}

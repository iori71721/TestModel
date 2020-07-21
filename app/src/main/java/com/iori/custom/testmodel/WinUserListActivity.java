package com.iori.custom.testmodel;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iori.custom.testmodel.ui.layout.WinUserTableLayout;

import java.util.ArrayList;
import java.util.List;

public class WinUserListActivity extends AppCompatActivity {
    private WinUserTableLayout winUserLayout;
    private Handler handler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.winuer_list_activity);

        handler=new Handler();
        winUserLayout=findViewById(R.id.winUserLayout);

        winUserLayout.reloadItems(handler,createData(),3);
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                winUserLayout.clear(handler);
//            }
//        },10000);
    }

    private List<WinUserTableLayout.ItemData> createData(){
        int createCount=47;
        List<WinUserTableLayout.ItemData> itemDatas=new ArrayList<>(createCount);
        WinUserTableLayout.ItemData itemData;
        for(int i=0;i<createCount;i++){
            itemData=new WinUserTableLayout.ItemData();
            itemData.userName="iori測試直播團"+i;
            if(i%3==0){
                itemData.userImageResource=R.drawable.user;
            }else if(i%3==1){
                itemData.userImageResource=R.drawable.server;
            }else{
                itemData.userImageResource=R.drawable.password;
            }
            itemDatas.add(itemData);
        }
        return itemDatas;
    }
}

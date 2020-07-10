package com.iori.custom.testmodel.lottery;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iori.custom.testmodel.R;

import java.util.ArrayList;
import java.util.List;

public class LotteryRecyclerAdapter extends RecyclerView.Adapter<LotteryRecyclerAdapter.ViewHolder> {
    public int inValidItemNumber=10;
    public int minLotteryToShow=30;
    private List<ItemData> lotteryUsers=new ArrayList(100);
    private Context context;
    public ResizeUserRectangle resizeUserRectangle;

    public LotteryRecyclerAdapter(Context context) {
        this.context = context;
    }

    private List<ItemData> generateLotteryRealData(List<ItemData> addUsers) {
        List<ItemData> inValidDatas;
        if (addUsers.size() <= inValidItemNumber) {
            inValidDatas = new ArrayList<>(addUsers);
        } else {
            inValidDatas = new ArrayList<>(inValidItemNumber);
            for (int i = 0; i < inValidItemNumber; i++) {
                inValidDatas.add(addUsers.get(i));
            }
        }
        int realDataSize = addUsers.size() + inValidDatas.size() * 2;
        List<ItemData> realData = new ArrayList<>(realDataSize);
        ItemData addItem;
        for (int i = 0; i < inValidDatas.size(); i++) {
            addItem=new ItemData();
            addItem.userName="ng_"+i;
            realData.add(addItem);
        }
        if (addUsers.size() >= minLotteryToShow) {
            realData.addAll(addUsers);
        } else {
            int addValidUserCount = 1;
            while (addValidUserCount <= minLotteryToShow) {
                for (int i = 0; i < addUsers.size(); i++) {
                    if (addValidUserCount <= minLotteryToShow) {
                        realData.add(addUsers.get(i));
                        addValidUserCount++;
                    } else {
                        break;
                    }
                }
            }
        }
        realData.addAll(inValidDatas);
        Log.d("iori_lottery", "generateLotteryRealData: real size " + realData.size());
        return realData;
    }

    public void addlotteryUsers(List<ItemData> addUsers){
        synchronized (lotteryUsers){
            lotteryUsers.clear();
            lotteryUsers.addAll(generateLotteryRealData(addUsers));
            notifyDataSetChanged();
        }
    }

    public ItemData getItem(int position){
        synchronized (lotteryUsers){
            return lotteryUsers.get(position);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        final View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.lottery_adapter_item,null);
        view.post(new Runnable() {
            @Override
            public void run() {
                Log.d("iori_lottery", "run: content view width "+view.getWidth()+" height "+view.getHeight());
                if(resizeUserRectangle != null){
                    resizeUserRectangle.resize(view.getWidth(),view.getHeight());
                }
            }
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.userName.setText(getItem(position).userName);
    }



    @Override
    public int getItemCount() {
        synchronized (lotteryUsers) {
            return lotteryUsers.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView userImage;
        private TextView userName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage=itemView.findViewById(R.id.userImage);
            userName=itemView.findViewById(R.id.userName);
        }
    }

    public static class ItemData{
        public String userName="";
    }

    public static interface ResizeUserRectangle{
        void resize(int width,int height);
    }
}

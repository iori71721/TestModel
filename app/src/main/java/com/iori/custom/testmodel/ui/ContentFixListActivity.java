package com.iori.custom.testmodel.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.iori.custom.testmodel.R;

import java.util.ArrayList;
import java.util.List;

public class ContentFixListActivity extends AppCompatActivity {
    private RecyclerView list;
    private Button addItem;
    private MyAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_fix_list_activity);

        Log.d("iori_s", "onCreate: 1");

        list=findViewById(R.id.listData);
        addItem=findViewById(R.id.addItem);
        initList();

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addOne();
            }
        });
    }

    private void initList() {
        FlexboxLayoutManager manager = new FlexboxLayoutManager(this);
        //设置主轴排列方式
        manager.setFlexDirection(FlexDirection.ROW);
        //设置是否换行
        manager.setFlexWrap(FlexWrap.WRAP);
        list.setLayoutManager(manager);
        list.setItemAnimator(new DefaultItemAnimator());
        adapter=new MyAdapter(this);
        list.setAdapter(adapter);
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
        private List<ItemData> datas=new ArrayList<>(100);
        private Context context;

        public MyAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.content_fix_list_adapter_item,null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

            ItemData itemData=datas.get(position);

//            填充每一行的剩余空间
            ViewGroup.LayoutParams params = holder.input.getLayoutParams();
            if (params instanceof FlexboxLayoutManager.LayoutParams) {
//                該段程式根本不會進來
                FlexboxLayoutManager.LayoutParams flexboxLp = (FlexboxLayoutManager.LayoutParams) holder.input.getLayoutParams();

                Log.d("iori_s", "onBindViewHolder: FlexboxLayoutManager.LayoutParams flexboxLp.getFlexGrow() "+flexboxLp.getFlexGrow()
                    +" position "+position);

//                flexboxLp.setFlexGrow(1.0f);

//                強制換行失敗
//                flexboxLp.setWrapBefore(true);

                holder.input.setLayoutParams(flexboxLp);

            }

//            此段可強制換行
            ViewGroup.LayoutParams emptyViewParams=holder.emptyView.getLayoutParams();

            if(position %3 ==0){
                emptyViewParams.width= ViewGroup.LayoutParams.WRAP_CONTENT;
            }else{
                emptyViewParams.width= 0;
            }

            holder.emptyView.setLayoutParams(emptyViewParams);


            Log.d("iori_s", "onBindViewHolder: itemData.requestFocus "+itemData.requestFocus+" position "+position);

            holder.input.setText(itemData.content);

            holder.input.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(position == holder.getAdapterPosition()) {
                        datas.get(holder.getAdapterPosition()).content = s.toString();
                    }
                }
            });

            if(itemData.requestFocus){
                if(!holder.input.hasFocus()) {
                    Log.d("iori_s", "onBindViewHolder: position " + position + " requestFocus true ");

                    holder.input.requestFocus();
//                    holder.input.performClick();

//                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

//                    無法強制開啟鍵盤
//                    InputMethodManager imm = (InputMethodManager) holder.input.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);


//                    imm.showSoftInput(holder.input,InputMethodManager.SHOW_FORCED);

//                    imm.showSoftInput(holder.input,InputMethodManager.SHOW_IMPLICIT);

//                    imm.showSoftInput(holder.input,0);




//                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
//
//                    Log.d("iori_s", "onBindViewHolder: imm.isActive() "+imm.isActive()+" position "+position);
//
////                    此寫法可以強制開啟，但是會出現，一開一關的問題
//                    if (imm.isActive()) {
//                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); // 隐藏
//                    } else {
//                        imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY); // 显示
//                    }




                    itemData.requestFocus = false;
                }
            }

        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        public void addOne(){
            synchronized (datas){
                datas.add(new ItemData());
                if(datas.size() %3 == 0){
                    datas.get(datas.size()-1).forceLine=true;
                }
            }
            notifyDataSetChanged();
            moveToLastItem();
        }

        private void moveToLastItem() {

        }

        public class ItemData{
            private String content;
            private boolean requestFocus=true;
            private boolean forceLine;
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            private EditText input;
            private View emptyView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                input=itemView.findViewById(R.id.input);
                emptyView=itemView.findViewById(R.id.emptyView);
            }
        }
    }
}

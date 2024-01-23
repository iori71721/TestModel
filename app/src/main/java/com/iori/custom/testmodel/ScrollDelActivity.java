package com.iori.custom.testmodel;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iori.custom.testmodel.ui.custom.LeftOnSwipedCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 遺留bug 持續左滑，滑動條會一直往左移
 */
public class ScrollDelActivity extends AppCompatActivity {
    private RecyclerView contentList;
    private ContentListAdapter contentListAdapter;
    private List<ItemData> contentDatas;

    private RecyclerView contentList2;
    private ContentListAdapter contentListAdapter2;
    private List<ItemData> contentDatas2;

    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("iori_s", "onCreate:  1");
        setContentView(R.layout.scroll_del_activity);

        Handler handler=new Handler();
        contentList=findViewById(R.id.contentList);
        contentList2=findViewById(R.id.contentList2);

        initContentList();
    }

    private void moveView(View view,int dX){
        view.scrollTo(dX,0);
    }

    private void initContentList() {
        contentDatas=new ArrayList<>(50);
        ItemData itemData=null;
        for(int i=0;i<20;i++){
            itemData=new ItemData();
            itemData.content="item"+i;
            contentDatas.add(itemData);
        }

        contentListAdapter=new ContentListAdapter();

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        contentList.setLayoutManager(linearLayoutManager);

        contentListAdapter.setDatas(contentDatas);

        //  實現拖移、左右滑動刪除的效果，第一個參數設定支援拖拉的方向
        //  第二參數設定滑動的方向，若假設滑動只想要左滑有效果設定單邊就好了，這樣就不會產生右滑的動畫效果


        LeftOnSwipedCallback leftOnSwipedCallback=new LeftOnSwipedCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT) {
            @Override
            protected void moveItem(int fromPos, int toPos) {
                contentListAdapter.moveItem(fromPos,toPos);
            }

            @Override
            protected void enableRightLayoutByItemData(int position) {
                contentDatas.get(position).showRightLayout=true;
            }
        };
        leftOnSwipedCallback.setDefaultScrollX(120);

        new ItemTouchHelper(leftOnSwipedCallback).attachToRecyclerView(contentList);

        contentList.setAdapter(contentListAdapter);

        initContentList2();
    }

    private void initContentList2() {
        contentDatas2=new ArrayList<>(50);
        ItemData itemData=null;
        for(int i=0;i<20;i++){
            itemData=new ItemData();
            itemData.content="item"+i;
            contentDatas2.add(itemData);
        }

        contentListAdapter2=new ContentListAdapter();

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        contentList2.setLayoutManager(linearLayoutManager);

        contentListAdapter2.setDatas(contentDatas2);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position=viewHolder.getAdapterPosition();
                if(direction == ItemTouchHelper.LEFT) {
                    Dialog dialog=new AlertDialog.Builder(ScrollDelActivity.this)
                            .setTitle("是否刪除?")
                            .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    contentListAdapter2.removeItem(position);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    contentListAdapter2.notifyDataSetChanged();
                                }
                            }).show();
                }
            }
        }).attachToRecyclerView(contentList2);

        contentList2.setAdapter(contentListAdapter2);
    }

    class ContentListAdapter extends RecyclerView.Adapter<ContentListAdapter.ContentListViewHolder>{
        private List<ItemData> datas=new ArrayList<>(50);

        @NonNull
        @Override
        public ContentListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.common_adapter_item,parent,false);
            return new ContentListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ContentListViewHolder holder, final int position) {
            ItemData itemData=datas.get(position);
            String data=itemData.content;
            holder.itemContent.setText(data);

            holder.displayDelLayout(itemData.showRightLayout);
            holder.delLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(position == holder.getAdapterPosition()){
                        removeItem(position);
                    }
                }
            });

            holder.contentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    contentDatas.get(holder.getAdapterPosition()).showRightLayout =false;
                    holder.displayDelLayout(false);
                }
            });
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        public void setDatas(List<ItemData> datas) {
            this.datas = datas;
        }

        public void moveItem(int fromPos, int toPos) {
            Collections.swap(datas, fromPos, toPos);
            notifyItemMoved(fromPos, toPos);
        }

        public void removeItem(int position){
            datas.remove(position);
            notifyItemRemoved(position);
            notifyDataSetChanged();
        }

        class ContentListViewHolder extends RecyclerView.ViewHolder{
//            固定寬度，避免getWidth 取得數值飄忽不定，但都設定定值了，應該不用如此處理才對，後續發現為必要
            public static final int DEL_LAYOUT_WIDTH =120;
            private TextView itemContent;
            private LinearLayout delLayout;
            private LinearLayout contentLayout;

            public ContentListViewHolder(@NonNull View itemView) {
                super(itemView);
                itemContent=itemView.findViewById(R.id.itemContent);
                delLayout=itemView.findViewById(R.id.delLayout);
                contentLayout=itemView.findViewById(R.id.contentLayout);
            }

            public void displayDelLayout(boolean enable){
                    if(enable){
                        moveView(itemView,DEL_LAYOUT_WIDTH);
                    }else{
                        moveView(itemView,0);
                    }
            }
        }
    }

    static class ItemData{
        private String content="";
        private boolean showRightLayout;

        public ItemData() {
        }
    }
}

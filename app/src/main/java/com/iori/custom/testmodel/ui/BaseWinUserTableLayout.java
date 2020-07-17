package com.iori.custom.testmodel.ui;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.UiThread;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @param <I> itemdata
 */
public abstract class BaseWinUserTableLayout<I> extends TableLayout {
    private int maxColumnCount=3;
    private List<I> items=new ArrayList<>(100);

    public BaseWinUserTableLayout(Context context) {
        super(context);
    }

    public BaseWinUserTableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void reloadItems(Handler handler, final List<I> loadItems, final int maxColumnCount){
        this.maxColumnCount=maxColumnCount;
        handler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (items){
                    items.clear();
                    items.addAll(loadItems);
                }
                reOrderItems(items,maxColumnCount);
            }
        });
    }

    private boolean isFirstColumn(int index,int maxColumnCount){
        return index == 0 || index % maxColumnCount ==0;
    }

    @UiThread
    protected TableRow createRow(){
        TableRow tableRow=new TableRow(getContext());
        MarginLayoutParams layoutParams=new MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        final int marginPX=(int)(10*getContext().getResources().getDisplayMetrics().density);
        layoutParams.setMargins(0,0,0,marginPX);

        tableRow.setLayoutParams(layoutParams);
        tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
        return tableRow;
    }

    @UiThread
    protected abstract View createItemView(int index,I itemData);

    @UiThread
    private void reOrderItems(final List<I> loadItems, int maxColumnCount){
        removeAllViews();
        TableRow tableRow=null;
        for(int i=0;i<loadItems.size();i++){
            if(isFirstColumn(i,maxColumnCount)){
                tableRow=createRow();
                addView(tableRow);
            }
            tableRow.addView(createItemView(i,loadItems.get(i)));
        }
    }

    public void clear(Handler handler){
        synchronized (items){
            items.clear();
            removeAllViews();
        }
    }

    public int getMaxColumnCount() {
        return maxColumnCount;
    }
}

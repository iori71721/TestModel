package com.iori.custom.testmodel.ui.custom;

import android.graphics.Canvas;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public abstract class LeftOnSwipedCallback extends ItemTouchHelper.SimpleCallback{
    private int currentScrollX;
    private boolean firstInactive;
    private int currentScrollXWhenInactive;
    private float initXWhenInactive;
    private int defaultScrollX;

    public LeftOnSwipedCallback(int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);
    }


    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
//                一定要設定，此處這樣設定便取消了onSwiped判定，onSwiped 便不會再次觸發
        return Integer.MAX_VALUE;
    }

    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        return Integer.MAX_VALUE;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        // Step 2-1
        final int fromPos = viewHolder.getAdapterPosition();
        final int toPos = target.getAdapterPosition();
        // move item in `fromPos` to `toPos` in adapter.
//                contentListAdapter.notifyItemMoved(fromPos, toPos);
        moveItem(fromPos,toPos);
        return true;// true if moved, false otherwise
    }

    protected abstract void moveItem(int fromPos, int toPos);

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        int position=viewHolder.getAdapterPosition();
        if (dX == 0) {
            currentScrollX = viewHolder.itemView.getScrollX();
            firstInactive = true;
        }
        if (isCurrentlyActive) { // 手指滑动
            // 基于当前的距离滑动，影響後續getScrollX() 取得滑動的距離的值，左滑為負，右滑為正
            viewHolder.itemView.scrollTo(currentScrollX + (int) -dX, 0);
        } else { // 动画滑动
//            代表已判定滑動事件，後續的動畫動作
            if (firstInactive) {
                firstInactive = false;
//                紀錄多少滑動距離為觸發滑動刪除
                currentScrollXWhenInactive = viewHolder.itemView.getScrollX();
//                觸發滑動時當前view的x位置，後續要計算往回拉的動畫使用
                initXWhenInactive = dX;
            }
//            因每次滑動觸發的距離不一定，所以要自行實作補間動畫
            int scrollDx=0;
            if (viewHolder.itemView.getScrollX() >= defaultScrollX) {
                // 滑動距離大於刪除layout
                // 当手指松开时，ItemView的滑动距离大于给定阈值，那么最终就停留在阈值，显示删除按钮。
                scrollDx=Math.max(currentScrollX + (int) -dX, defaultScrollX);
                viewHolder.itemView.scrollTo(scrollDx, 0);
//                因沒辦法觸發onSwiped，所以在此處改變item的狀態
                enableRightLayoutByItemData(position);
            } else {
                // 滑動距離小於刪除layout
                // 这里只能做距离的比例缩放，因为回到最初位置必须得从当前位置开始，dx不一定与ItemView的滑动距离相等
                scrollDx=(int) (currentScrollXWhenInactive * dX / initXWhenInactive);
                viewHolder.itemView.scrollTo(scrollDx, 0);
            }
        }
    }

    protected abstract void enableRightLayoutByItemData(int position);

    @Override
    public long getAnimationDuration(@NonNull RecyclerView recyclerView, int animationType, float animateDx, float animateDy) {
//                預設的一半，這樣刪除會比較順，預設是120
        return 60;
    }

    @Override
    public boolean isLongPressDragEnabled() {
//                暫時取消拖動功能
        return false;
    }

    public int getDefaultScrollX() {
        return defaultScrollX;
    }

    public void setDefaultScrollX(int defaultScrollX) {
        this.defaultScrollX = defaultScrollX;
    }
}

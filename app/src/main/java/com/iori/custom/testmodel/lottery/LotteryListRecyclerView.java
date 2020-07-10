package com.iori.custom.testmodel.lottery;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LotteryListRecyclerView extends RecyclerView {
    public int inValidItemNumber=10;
    public int minLotteryToShow=100;
    private static final int cycleMoveStepMs=32;
    public Handler handler;
    private int currentLotteryIndex;
    private boolean cycleMove;
    private int scrollDxPx=10;
    private LinearLayoutManager linearLayoutManager;
    public int lotteryUserRectangleLeft;
    private OnScrollListener scrolltoTopListener;
    private boolean lotteryUser;


    public LotteryListRecyclerView(@NonNull Context context) {
        super(context);
    }

    public LotteryListRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LotteryListRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void cycleMove(){
        if(!cycleMove){
            executeStopCycle();
            return;
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                smoothScrollBy(scrollDxPx,0);
                currentLotteryIndex=detectLotteryIndex();
                Log.d("iori_lottery_currentLotteryIndex", "run: currentLotteryIndex "+currentLotteryIndex);
                if(isBottom()){
                    scrollToStartIndex();
                }
                cycleMove();
            }
        },cycleMoveStepMs);
    }

    private void executeStopCycle(){
        if(lotteryUser){
            selectItemInLotteryUserRectangle(currentLotteryIndex);
        }
    }

    public void lotteryUser(){
        lotteryUser=true;
        stopCycle();
    }

    private int detectLotteryIndex(){
        int lotteryIndex=getLinearLayoutManager().findLastVisibleItemPosition();
        View lastView=getLinearLayoutManager().findViewByPosition(lotteryIndex);
        Log.d("iori_lottery", "detectLotteryIndex: lastview left "+lastView.getLeft()+" right "+lastView.getRight()+" width "+lastView.getWidth());
        return lotteryIndex>=inValidItemNumber?lotteryIndex:inValidItemNumber;
    }

    private void scrollToStartIndex(){
        scrollToPosition(0);
        Toast.makeText(getContext(),"scrollToValidItemStartIndex",Toast.LENGTH_SHORT).show();
    }

    /**
     * only dx
     * @param visibleItem
     * @param lotterPosition
     */
    private void moveItemInToInLotteryUserRectangle(final View visibleItem, final int lotterPosition,int moveDx){
//        int outScreenDx=0;
//        if(visibleItem.getLeft()<getLeft()){
//            outScreenDx=visibleItem.getLeft()-getLeft();
//        }else if(visibleItem.getRight()>getRight()){
//            outScreenDx=getRight()-visibleItem.getRight();
//        }
//        Log.d("iori_lottery_outScreenDx", "selectItemInLotteryUserRectangle: visibleItem.getLeft() "+visibleItem.getLeft()+" visibleItem.getRight() "+visibleItem.getRight()
//                +" getLeft() "+getLeft()+" getRight() "+getRight()+" outScreenDx "+outScreenDx);
////        moveDx+=outScreenDx;
//        int visibleWidth=visibleItem.getRight()-visibleItem.getLeft();
//        Log.d("iori_lottery", "selectItemInLotteryUserRectangle: before scroll left "+visibleItem.getLeft());


//        if(scrolltoLotteryUserRectangleListener == null){
//            scrolltoLotteryUserRectangleListener=new OnScrollListener() {
//                @Override
//                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                    super.onScrollStateChanged(recyclerView, newState);
//                }
//
//                @Override
//                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                    super.onScrolled(recyclerView, dx, dy);
//                    if(visibleItem.getLeft()>=lotteryUserRectangleLeft){
//                        removeOnScrollListener(scrolltoLotteryUserRectangleListener);
//                    }else{
//                        moveItemInToInLotteryUserRectangle(visibleItem,lotterPosition);
//                    }
//                }
//            };
//        }
//        addOnScrollListener(scrolltoLotteryUserRectangleListener);

//        scrollBy(moveDx,0);

        smoothScrollBy(moveDx,0);


//        visibleItem = getLinearLayoutManager().findViewByPosition(position);
//        Log.d("iori_lottery", "selectItemInLotteryUserRectangle:  visibleItem.getLeft() "+visibleItem.getLeft()
//                +" lotteryUserRectangleLeft "+lotteryUserRectangleLeft+" moveDx "+moveDx+" visibleItem.getWidth() "+visibleItem.getWidth()
//                +" visibleWidth "+visibleWidth);
    }

    private int calcMoveDxToTarget(View moveView,int targetPosition){
        return moveView.getLeft()-targetPosition;
    }

    private void forceMoveViewIntoLotteryUserRectangle(View moveView,int moveViewIndex){
        int moveDx=calcMoveDxToTarget(moveView,lotteryUserRectangleLeft);
        currentLotteryIndex=moveViewIndex;
        moveItemInToInLotteryUserRectangle(moveView,currentLotteryIndex,moveDx);
    }

    /**
     * temp public to test
     * @param position
     */
    public void selectItemInLotteryUserRectangle(final int position){
        Log.d("iori_lottery_idle", "selectItemInLotteryUserRectangle: position "+position+" currentLotteryIndex "+currentLotteryIndex);
        if(position < inValidItemNumber){
            return;
        }
        if(scrolltoTopListener == null){
            scrolltoTopListener =new OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    Log.d("iori_lottery_idle", "onScrollStateChanged: "+newState+" lotteryUser "+lotteryUser);
                    if(lotteryUser){
                        switch (newState){
                            case SCROLL_STATE_IDLE:
                                View lotteryView=getLinearLayoutManager().findViewByPosition(currentLotteryIndex);
                                if(lotteryView != null) {
                                    int moveDx=calcMoveDxToTarget(lotteryView,lotteryUserRectangleLeft);
                                    if(moveDx==0){
                                        lotteryUser=false;
                                        Log.d("iori_lottery_idle", "onScrollStateChanged: current position moveDx "+moveDx+" end move "+" position "+position+" currentLotteryIndex "+currentLotteryIndex);
                                    }else{
                                        Log.d("iori_lottery_idle", "onScrollStateChanged: lotteryView.getLeft() "+lotteryView.getLeft()+" getLeft() "+getLeft()+" moveDx "+moveDx);
                                        moveItemInToInLotteryUserRectangle(lotteryView,currentLotteryIndex,moveDx);
                                    }
                                }else{
                                    Log.d("iori_lottery_idle", "onScrollStateChanged: lotteryView "+lotteryView+" newState "+newState+" repeat scroll position "+position+" currentLotteryIndex "+currentLotteryIndex+" lotteryUser "+lotteryUser);

//                                    selectItemInLotteryUserRectangle(currentLotteryIndex);
                                    int firstViewIndex=getLinearLayoutManager().findFirstVisibleItemPosition();

//                                    firstViewIndex=getLinearLayoutManager().findFirstCompletelyVisibleItemPosition();
                                    View firstView=getLinearLayoutManager().findViewByPosition(firstViewIndex);

                                    
                                    forceMoveViewIntoLotteryUserRectangle(firstView,firstViewIndex);
                                    Log.d("iori_lottery_idle", "onScrollStateChanged: forceMoveViewIntoLotteryUserRectangle  currentLotteryIndex "+currentLotteryIndex);



//
//                                    if(firstViewIndex != currentLotteryIndex) {
//                                        Log.d("iori_lottery_idle", "onScrollStateChanged: index not equal repeat selectItemInLotteryUserRectangle firstViewIndex "+firstViewIndex+" currentLotteryIndex "+currentLotteryIndex);
//                                        currentLotteryIndex=firstViewIndex;
//
//
//
//                                        selectItemInLotteryUserRectangle(currentLotteryIndex);
//                                    }else{
//                                        View firstView=getLinearLayoutManager().findViewByPosition(firstViewIndex);
//                                        if(firstView != null){
//                                            Log.d("iori_lottery_idle","index equal move to user rectangle");
//                                            int moveDx=calcMoveDxToTarget(firstView,lotteryUserRectangleLeft);
//                                            moveItemInToInLotteryUserRectangle(firstView,currentLotteryIndex,moveDx);
//                                        }else{
//                                            Log.d("iori_lottery_idle","index equal but firstView is null. "+firstView);
//                                        }
//                                    }





                                }
                                break;
                        }
                    }
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }
            };


            addOnScrollListener(scrolltoTopListener);

        }
        getLinearLayoutManager().scrollToPositionWithOffset(position,0);
        Log.d("iori_lottery_idle", "selectItemInLotteryUserRectangle: execute scrollToPositionWithOffset position "+position+" currentLotteryIndex "+currentLotteryIndex);
    }

    private boolean isBottom(){
        int firstVisibleItemPosition=getLinearLayoutManager().findFirstVisibleItemPosition();
        View firstVisibleItem = getLinearLayoutManager().findViewByPosition(firstVisibleItemPosition);
        int itemCount = getLinearLayoutManager().getItemCount();
        int recyclerviewHeight = getHeight();
        int firstItemBottom=getLinearLayoutManager().getDecoratedBottom(firstVisibleItem);
        int itemHeight = firstVisibleItem.getHeight();
        int scrollDownDistance=(itemCount - firstVisibleItemPosition - 1) * itemHeight- recyclerviewHeight + firstItemBottom;

        Log.d("iori_lottery", "isBottom: firstVisibleItemPosition "+firstVisibleItemPosition
            +" itemCount "+itemCount+" recyclerviewHeight "+recyclerviewHeight+" firstItemBottom "+firstItemBottom
            +" itemHeight "+itemHeight+" scrollDownDistance "+scrollDownDistance+" firstVisibleItem.getLeft() "+firstVisibleItem.getLeft()
            +" firstVisibleItem.getRight() "+firstVisibleItem.getRight());

        View lastItemView=getLinearLayoutManager().findViewByPosition(getAdapter().getItemCount()-1);
        if(lastItemView != null){
            Log.d("iori_lottery", "isBottom: lastItemView.getWidth() "+lastItemView.getWidth()+" left "+lastItemView.getLeft()+" right "+lastItemView.getRight()+" list right "+getRight());
            int lastItemViewWidth=getRight()-lastItemView.getLeft();
            Log.d("iori_lottery_bottom", "isBottom: lastItemViewWidth "+lastItemViewWidth+" lastItemRealWidth "+lastItemView.getWidth());
            if(lastItemViewWidth >= lastItemView.getWidth()){
                Log.d("iori_lottery_bottom", "isBottom: true ");
                return true;
            }
        }

        
        return false;
    }

    public void startPreview(){
        scrollToStartIndex();
        scrollDxPx=10;
        cycleMove=true;
        cycleMove();
    }

    public void startLottery(){
        scrollDxPx=200;
        cycleMove=true;
        cycleMove();
    }

    private void stopCycle(){
        cycleMove=false;
    }

    /**
     * test to public
     * @return
     */
    public LinearLayoutManager getLinearLayoutManager() {
        if(linearLayoutManager == null){
            linearLayoutManager=(LinearLayoutManager)getLayoutManager();
        }
        return linearLayoutManager;
    }

}

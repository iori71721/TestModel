package com.iori.custom.testmodel.ui.layout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;

import com.iori.custom.testmodel.R;
import com.iori.custom.testmodel.ui.BaseWinUserTableLayout;

public class WinUserTableLayout extends BaseWinUserTableLayout<WinUserTableLayout.ItemData> {


    public WinUserTableLayout(Context context) {
        super(context);
    }

    public WinUserTableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View createItemView(int index, ItemData itemData) {
        View itemView= LayoutInflater.from(getContext()).inflate(R.layout.winuser_list_item,null);
        ViewHolder viewHolder=new ViewHolder(itemView);
        viewHolder.userName.setText(itemData.userName);
        Drawable userImage=getContext().getResources().getDrawable(itemData.userImageResource);
        viewHolder.userImage.setImageDrawable(userImage);
        return itemView;
    }

    public static class ItemData{
        public String userName="";
        public @DrawableRes int userImageResource=R.drawable.user;
    }

    public static class ViewHolder{
        public TextView userName;
        public ImageView userImage;

        public ViewHolder(View itemView) {
            userName=itemView.findViewById(R.id.winUserName);
            userImage=itemView.findViewById(R.id.winUserImage);
        }
    }
}

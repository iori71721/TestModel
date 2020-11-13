package com.iori.custom.testmodel.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.iori.custom.common.adapter.CommonBaseAdapter;
import com.iori.custom.testmodel.R;

public class FixedHeaderTableAdapter extends CommonBaseAdapter<FixedHeaderTableAdapter.ViewHolder, FixedHeaderTableAdapter.ItemData> {

    public FixedHeaderTableAdapter(Context context) {
        super(context);
    }

    @Override
    protected void initLayout(ViewHolder viewHolder, ItemData itemData, int i) {

    }

    @Override
    protected int getConvertViewID() {
        return R.layout.fixed_header_table_item;
    }

    @Override
    protected ViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    public static class ViewHolder{
        private EditText spec;
        private EditText count;
        private Button edit;
        private Button del;

        public ViewHolder(View root) {
            spec=root.findViewById(R.id.spec);
            count=root.findViewById(R.id.count);
            edit=root.findViewById(R.id.edit);
            del=root.findViewById(R.id.del);
        }
    }

    public static class ItemData{
        public String spec;
        public int count;

        public ItemData(String spec, int count) {
            this.spec = spec;
            this.count = count;
        }
    }
}

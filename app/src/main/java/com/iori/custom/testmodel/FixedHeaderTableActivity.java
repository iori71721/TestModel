package com.iori.custom.testmodel;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iori.custom.testmodel.adapter.FixedHeaderTableAdapter;

import java.util.ArrayList;
import java.util.List;

public class FixedHeaderTableActivity extends AppCompatActivity {
    private ListView fixedHeaderTable;
    private FixedHeaderTableAdapter fixedHeaderTableAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fixed_header_table_activity);
        fixedHeaderTable=findViewById(R.id.fixedHeaderTable);
        fixedHeaderTableAdapter=new FixedHeaderTableAdapter(this);

        List<FixedHeaderTableAdapter.ItemData> datas=new ArrayList<>(100);
        FixedHeaderTableAdapter.ItemData addData;
        for(int i=0;i<100;i++){
            addData=new FixedHeaderTableAdapter.ItemData("S_"+i,i);
            datas.add(addData);
        }

        fixedHeaderTableAdapter.addItemDatas(datas);
        fixedHeaderTable.setAdapter(fixedHeaderTableAdapter);
    }
}

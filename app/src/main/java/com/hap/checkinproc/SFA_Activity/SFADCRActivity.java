package com.hap.checkinproc.SFA_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.hap.checkinproc.Interface.onPayslipItemClick;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.HAPListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SFADCRActivity extends AppCompatActivity {
    JSONArray MnuList;
    RecyclerView recyclerView;
    private HAPListItem mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sfa_dcr);
        MnuList= new JSONArray();
        addMnuitem("1","SALES CALLS");
        addMnuitem("2","VAN ROUTE SUPPLY");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new HAPListItem(MnuList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        HAPListItem.SetPayOnClickListener(new onPayslipItemClick() {
            @Override
            public void onClick(JSONObject item) {

            }
        });
    }
    public void addMnuitem(String id,String name){
        try {
            JSONObject item=new JSONObject();
            item.put("id",id);
            item.put("name",name);
            MnuList.put(item);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
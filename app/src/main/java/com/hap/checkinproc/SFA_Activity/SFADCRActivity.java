package com.hap.checkinproc.SFA_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
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
    Shared_Common_Pref CommonPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sfa_dcr);
        CommonPref = new Shared_Common_Pref(this);
        Log.d("isDebuggable",String.valueOf(isDebuggable(this)));

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
                try {
                    if (item.getString("id") == "1") {
                        CommonPref.save(CommonPref.DCRMode, "SC");
                    }
                    if (item.getString("id") == "2") {
                        CommonPref.save(CommonPref.DCRMode, "VC");
                    }
                    Intent intent=new Intent(SFADCRActivity.this,Dashboard_Route.class);
                    startActivity(intent);
                }catch (JSONException e){

                }
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
    private boolean isDebuggable(Context ctx)
    {
        boolean debuggable = false;

        PackageManager pm = ctx.getPackageManager();
        try
        {
            ApplicationInfo appinfo = pm.getApplicationInfo(ctx.getPackageName(), 0);
            debuggable = (0 != (appinfo.flags & ApplicationInfo.FLAG_DEBUGGABLE));
        }
        catch(PackageManager.NameNotFoundException e)
        {
            /*debuggable variable will remain false*/
        }

        return debuggable;
    }
}
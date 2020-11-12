package com.hap.checkinproc.Activity_Hap;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import com.hap.checkinproc.Activity.Util.UpdateUi;
import com.hap.checkinproc.Model_Class.Distributor_Master;
import com.hap.checkinproc.Model_Class.Route_Master;
import com.hap.checkinproc.Model_Class.Work_Type_Model;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.DataAdapter;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CustomListViewDialog extends Dialog implements View.OnClickListener, UpdateUi {
    public CustomListViewDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public CustomListViewDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    public Activity activity;
    public Dialog dialog;
    public Button yes, no;
    EditText searchView;
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    //RecyclerView.Adapter adapter;
    DataAdapter da;
    int type;
    List<Work_Type_Model> mDataset;
    List<Distributor_Master> Distributor_Master;
    List<Route_Master> route_Master;

    public CustomListViewDialog(Activity a, List<Work_Type_Model> wk, int type, List<Distributor_Master> distributor, List<Route_Master> routelist) {
        super(a);
        this.activity = a;
        this.type = type;
        //this.adapter = adapter;
        this.mDataset = wk;
        this.Distributor_Master = distributor;
        this.route_Master = routelist;
        this.da = new DataAdapter(mDataset, activity, type, Distributor_Master,route_Master);
        setupLayout();


    }

    private void setupLayout() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_layout);
        no = (Button) findViewById(R.id.no);
        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setAdapter(da);

        no.setOnClickListener(this);
        da.notifyDataSetChanged();
        searchView.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                da.getFilter().filter(s);

            }
        });
       /* searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.getFilter().filter(query);
                return false;
            }
        });*/

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.no:
                dismiss();
                break;
            default:
                break;
        }

    }


    @Override
    public void update(int value, int pos) {
        Log.e("CUSTOm_DIalog_Calling", "");
        dismiss();
    }

    @Override
    public void Getjsoninterface(String Success) {

    }
}
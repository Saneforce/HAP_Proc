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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Activity.Util.UpdateUi;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.DataAdapter;

import java.util.List;

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
    List<Common_Model> mDataset;


    public CustomListViewDialog(Activity a, List<Common_Model> wk, int type) {
        super(a);
        this.activity = a;
        this.type = type;
        //this.adapter = adapter;
        this.mDataset = wk;

        this.da = new DataAdapter(mDataset, activity, type);
        setupLayout();
    }

    private void setupLayout() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*requestWindowFeature(Window.FEATURE_NO_TITLE);*/
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

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                da.getFilter().filter(s);
            }
        });

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
        Log.e("Custom_Dialog_Calling", "");
        dismiss();
    }


}
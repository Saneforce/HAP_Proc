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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.hap.checkinproc.Activity.Util.UpdateUi;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Activity.Nearby_Outlets;
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
    public Button mBtnSave, no;
    EditText searchView;
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    //RecyclerView.Adapter adapter;
    DataAdapter da;
    int type;
    List<Common_Model> mDataset;
    ImageView mIvAddMapKey;
    EditText etAddKey;
    TextView tvtitle;

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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_layout);
        no = (Button) findViewById(R.id.no);
        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recycler_view);
        mIvAddMapKey = (findViewById(R.id.ivAddKey));
        mBtnSave = findViewById(R.id.btn_save);
        etAddKey = findViewById(R.id.et_addMapKey);
        tvtitle = findViewById(R.id.txt);

        if (type == 1000) {
            searchView.setVisibility(View.GONE);
            mIvAddMapKey.setVisibility(View.VISIBLE);
            tvtitle.setText("Map KeyList");

        }


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(da);

        no.setOnClickListener(this);
        //da.notifyDataSetChanged();
        searchView.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                da.getFilter().filter(s.toString());
            }
        });

        mIvAddMapKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etAddKey.setVisibility(View.VISIBLE);
                mBtnSave.setVisibility(View.VISIBLE);
            }
        });

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etAddKey.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Empty key is not allowed", Toast.LENGTH_SHORT).show();
                } else {
                    mDataset.add(new Common_Model(etAddKey.getText().toString()));
                    etAddKey.setText("");
                    Gson gson = new Gson();
                    Nearby_Outlets.shared_common_pref.save(Constants.MAP_KEYLIST, gson.toJson(mDataset));
                    da.notifyDataSetChanged();

                }
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
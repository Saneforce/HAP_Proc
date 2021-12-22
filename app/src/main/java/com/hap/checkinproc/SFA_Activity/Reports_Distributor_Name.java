package com.hap.checkinproc.SFA_Activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.DistributerListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

public class Reports_Distributor_Name extends AppCompatActivity {
    Gson gson;
    private RecyclerView recyclerView;
    Common_Class common_class;
    Shared_Common_Pref shared_common_pref;
    double ACBalance = 0.0;

    EditText etSearch;
    public static Reports_Distributor_Name reports_distributor_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_dist_name);
            shared_common_pref = new Shared_Common_Pref(this);
            recyclerView = findViewById(R.id.rvDistList);
            etSearch = findViewById(R.id.etSearchDist);
            reports_distributor_name=this;

            common_class = new Common_Class(this);
            gson = new Gson();
            ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
            common_class.gotoHomeScreen(this, ivToolbarHome);
            JSONArray arr = new JSONArray(shared_common_pref.getvalue(Constants.Distributor_List));
            recyclerView.setAdapter(new DistributerListAdapter(arr, R.layout.dist_info_recyclerview, this));


            etSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {

                        JSONArray filterArr = new JSONArray();
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject itm = arr.getJSONObject(i);
                            if (Common_Class.isNullOrEmpty(s.toString()) || itm.getString("name").toUpperCase().contains(s.toString().toUpperCase())) {
                                filterArr.put(arr.getJSONObject(i));
                            }

                        }
                        recyclerView.setAdapter(new DistributerListAdapter(filterArr, R.layout.dist_info_recyclerview, Reports_Distributor_Name.this));


                    } catch (Exception e) {

                    }
                }
            });

        } catch (Exception e) {

        }

    }

}
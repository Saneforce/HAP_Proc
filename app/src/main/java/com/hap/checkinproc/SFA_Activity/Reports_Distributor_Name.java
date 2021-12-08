package com.hap.checkinproc.SFA_Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.DistributerListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Reports_Distributor_Name extends AppCompatActivity {
    Gson gson;
    private RecyclerView recyclerView;
    Common_Class common_class;
    Shared_Common_Pref shared_common_pref;
    double ACBalance = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_dist_name);
            shared_common_pref = new Shared_Common_Pref(this);
            recyclerView = findViewById(R.id.rvDistList);
            common_class = new Common_Class(this);
            gson = new Gson();
            ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
            common_class.gotoHomeScreen(this, ivToolbarHome);
            JSONArray arr = new JSONArray(shared_common_pref.getvalue(Constants.Distributor_List));
            recyclerView.setAdapter(new DistributerListAdapter(arr, R.layout.dist_info_recyclerview, this));

        } catch (Exception e) {

        }

    }

}
package com.hap.checkinproc.SFA_Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hap.checkinproc.Activity_Hap.Tp_Mydayplan;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Model_Class.Tp_View_Master;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Model_Class.Category_Universe_Modal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Route_Product_Info extends AppCompatActivity implements View.OnClickListener {
    GridView Categorygrid, availablitygrid;
    Switch availswitch, cateswitch;
    EditText reason_availablity;
    LinearLayout more_info;
    Shared_Common_Pref shared_common_pref;
    Common_Class common_class;
    List<Category_Universe_Modal> Category_univ_Modal = new ArrayList<>();
    TextView takeorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route__product__info);
        shared_common_pref = new Shared_Common_Pref(this);
        common_class = new Common_Class(this);
        Categorygrid = findViewById(R.id.category);
        takeorder = findViewById(R.id.takeorder);
        more_info = findViewById(R.id.more_info);
        availablitygrid = findViewById(R.id.availablitygrid);
        availswitch = findViewById(R.id.availswitch);
        cateswitch = findViewById(R.id.cateswitch);
        reason_availablity = findViewById(R.id.reason_availablity);
        cateswitch.setTextColor(getResources().getColor(R.color.green));
        availswitch.setTextColor(getResources().getColor(R.color.green));
        availswitch.setChecked(true);
        cateswitch.setChecked(true);
        takeorder.setOnClickListener(this);
        cateswitch.setOnClickListener(this);
        availswitch.setOnClickListener(this);
        more_info.setOnClickListener(this);
        category_universe();
       /* grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // set an Intent to Another Activity
                *//*list.add("Siva");
                CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), list);
                grid.setAdapter(customAdapter);*//*
            }
        });*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cateswitch:
                if (cateswitch.isChecked()) {
                    cateswitch.setChecked(true);
                    cateswitch.setText("YES");
                    cateswitch.setTextColor(getResources().getColor(R.color.green));
                } else {
                    cateswitch.setChecked(false);
                    cateswitch.setText("NO");
                    cateswitch.setTextColor(getResources().getColor(R.color.color_red));
                }
                break;
            case R.id.availswitch:
                if (availswitch.isChecked()) {
                    availswitch.setChecked(true);
                    availswitch.setText("YES");
                    availswitch.setTextColor(getResources().getColor(R.color.green));
                    availablitygrid.setVisibility(View.VISIBLE);
                    //reason_availablity.setVisibility(View.INVISIBLE);
                } else {
                    availswitch.setChecked(false);
                    availswitch.setText("NO");
                    availswitch.setTextColor(getResources().getColor(R.color.color_red));
                    availablitygrid.setVisibility(View.GONE);
                    // reason_availablity.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.more_info:
                common_class.CommonIntentwithFinish(More_Info_Activity.class);
                break;
            case R.id.takeorder:
                common_class.CommonIntentwithFinish(Invoice_History.class);
                break;


        }
    }


    public class CustomAdapteravailablity extends BaseAdapter {
        Context context;
        List<Category_Universe_Modal> listt;
        LayoutInflater inflter;

        public CustomAdapteravailablity(Context applicationContext, List<Category_Universe_Modal> listt) {
            this.context = applicationContext;
            this.listt = listt;
            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return listt.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflter.inflate(R.layout.category_universe_gridview, null); // inflate the layout
            TextView icon = (TextView) view.findViewById(R.id.textView);
            LinearLayout gridcolor = view.findViewById(R.id.gridcolor);
            // get the reference of ImageView
            icon.setText(listt.get(i).getName()); // set logo images
            // icon.setBackgroundColor(getResources().getColor(R.color.color_light_red));
            if (i > 2) {
                // gridcolor.setBackgroundColor(getResources().getColor(R.color.color_light_red));
                gridcolor.setBackground(getResources().getDrawable(R.drawable.grid_backround));
            } else {
                gridcolor.setBackground(getResources().getDrawable(R.drawable.grid_backround_red));
            }
            return view;
        }
    }

    public class CustomCategoryAdapter extends BaseAdapter {
        Context context;
        List<Category_Universe_Modal> listt;
        LayoutInflater inflter;

        public CustomCategoryAdapter(Context applicationContext, List<Category_Universe_Modal> listt) {
            this.context = applicationContext;
            this.listt = listt;
            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return listt.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflter.inflate(R.layout.category_universe_gridview, null); // inflate the layout
            TextView name = (TextView) view.findViewById(R.id.textView);
            LinearLayout gridcolor = view.findViewById(R.id.gridcolor);
            name.setText(listt.get(i).getName()); // set logo images
            if (i > 2) {
                // gridcolor.setBackgroundColor(getResources().getColor(R.color.color_light_red));
                gridcolor.setBackground(getResources().getDrawable(R.drawable.grid_backround));
            } else {
                gridcolor.setBackground(getResources().getDrawable(R.drawable.grid_backround_red));
            }
            return view;
        }
    }


    public void category_universe() {
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "table/list");
        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
        QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
        QueryString.put("rSF", Shared_Common_Pref.Sf_Code);
        QueryString.put("State_Code", Shared_Common_Pref.StateCode);
        Call<Object> call = service.GetRouteObject(QueryString, "{\"tableName\":\"category_universe\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}");
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e("MAsterSyncView_Result", response.body() + "");
                System.out.println("Route_Matser" + response.body().toString());
                GetJsonData(new Gson().toJson(response.body()), "1");
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    private void GetJsonData(String jsonResponse, String type) {
        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String id = String.valueOf(jsonObject1.optInt("id"));
                String name = jsonObject1.optString("name");
                String Division_Code = jsonObject1.optString("Division_Code");
                String Cat_Image = jsonObject1.optString("Cat_Image");
                String sampleQty = jsonObject1.optString("sampleQty");
                String colorflag = jsonObject1.optString("colorflag");
                Category_univ_Modal.add(new Category_Universe_Modal(id, name, Division_Code, Cat_Image, sampleQty,colorflag));
            }
            CustomCategoryAdapter customAdapter = new CustomCategoryAdapter(getApplicationContext(), Category_univ_Modal);
            Categorygrid.setAdapter(customAdapter);
            CustomAdapteravailablity customAdapteravail = new CustomAdapteravailablity(getApplicationContext(), Category_univ_Modal);
            availablitygrid.setAdapter(customAdapteravail);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
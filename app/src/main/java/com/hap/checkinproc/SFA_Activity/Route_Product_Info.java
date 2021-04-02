package com.hap.checkinproc.SFA_Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Activity_Hap.AddNewRetailer;
import com.hap.checkinproc.Activity_Hap.CustomListViewDialog;
import com.hap.checkinproc.Activity_Hap.Tp_Mydayplan;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.Model_Class.Tp_View_Master;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Model_Class.Category_Universe_Modal;
import com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
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

public class Route_Product_Info extends AppCompatActivity implements View.OnClickListener, Master_Interface {
    GridView Categorygrid, availablitygrid;
    Switch availswitch, cateswitch;
    EditText reason_availablity, reason_category;
    LinearLayout more_info;
    Shared_Common_Pref sharedCommonPref;
    Common_Class common_class;
    List<Category_Universe_Modal> Category_univ_Modal = new ArrayList<>();
    TextView takeorder, Nextadd, Compititorname;
    List<Category_Universe_Modal> Availlistt;
    List<Category_Universe_Modal> Universelistt;
    LinearLayout reason_categoryLin;
    List<Common_Model> Compititor_List = new ArrayList<>();
    CustomListViewDialog customDialog;
    String CompIDServer = "";
    Gson gson;
    Type userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route__product__info);
        sharedCommonPref = new Shared_Common_Pref(this);
        gson = new Gson();
        common_class = new Common_Class(this);
        Categorygrid = findViewById(R.id.category);
        takeorder = findViewById(R.id.takeorder);
        reason_category = findViewById(R.id.reason_category);
        reason_categoryLin = findViewById(R.id.reason_categoryLin);
        more_info = findViewById(R.id.more_info);
        Nextadd = findViewById(R.id.Nextadd);
        Compititorname = findViewById(R.id.Compititorname);
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
        Nextadd.setOnClickListener(this);
        availswitch.setOnClickListener(this);
        more_info.setOnClickListener(this);
        Compititorname.setOnClickListener(this);
        //category_universe("1");
        // category_universe("2");

        String Category_List = sharedCommonPref.getvalue(Shared_Common_Pref.Category_List);
        String Compititor_List = sharedCommonPref.getvalue(Shared_Common_Pref.Compititor_List);

        GetJsonData(Category_List, "1");
        GetJsonData(Compititor_List, "2");
        if (Shared_Common_Pref.Outler_AddFlag != null && Shared_Common_Pref.Outler_AddFlag.equals("1")) {
            Nextadd.setVisibility(View.VISIBLE);
            takeorder.setVisibility(View.GONE);
        } else {
            Nextadd.setVisibility(View.GONE);
            takeorder.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cateswitch:
                if (cateswitch.isChecked()) {
                    cateswitch.setChecked(true);
                    cateswitch.setText("YES");
                    cateswitch.setTextColor(getResources().getColor(R.color.green));
                    if (Shared_Common_Pref.Outler_AddFlag != null && Shared_Common_Pref.Outler_AddFlag.equals("1")) {
                        Categorygrid.setVisibility(View.VISIBLE);
                        reason_categoryLin.setVisibility(View.GONE);
                    }

                } else {
                    cateswitch.setChecked(false);
                    cateswitch.setText("NO");
                    cateswitch.setTextColor(getResources().getColor(R.color.color_red));
                    if (Shared_Common_Pref.Outler_AddFlag != null && Shared_Common_Pref.Outler_AddFlag.equals("1")) {
                        Categorygrid.setVisibility(View.GONE);
                        reason_categoryLin.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case R.id.availswitch:
                if (availswitch.isChecked()) {
                    availswitch.setChecked(true);
                    availswitch.setText("YES");
                    availswitch.setTextColor(getResources().getColor(R.color.green));
                    availablitygrid.setVisibility(View.VISIBLE);
                    reason_availablity.setVisibility(View.GONE);
                } else {
                    availswitch.setChecked(false);
                    availswitch.setText("NO");
                    availswitch.setTextColor(getResources().getColor(R.color.color_red));
                    availablitygrid.setVisibility(View.GONE);
                    reason_availablity.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.more_info:
                common_class.CommonIntentwithFinish(More_Info_Activity.class);
                break;
            case R.id.takeorder:
                common_class.CommonIntentwithoutFinish(Invoice_History.class);
                break;
            case R.id.Nextadd:
                boolean checkavail = false;
                for (Common_Model fil : Compititor_List) {
                    if (fil.isSelected() == true) {
                        checkavail = true;
                    }
                }
                if (checkavail == false) {
                    Toast.makeText(this, "Select The Other Brand", Toast.LENGTH_SHORT).show();
                } else {
                    StringBuilder CatUniverId = new StringBuilder();
                    for (Category_Universe_Modal ListUniv : Universelistt) {
                        if (ListUniv.getColorFlag().equals("1")) {
                            CatUniverId.append("," + ListUniv.getId());
                        }
                    }
                    StringBuilder AvailCat = new StringBuilder();
                    for (Category_Universe_Modal ListUniv : Availlistt) {
                        if (ListUniv.getColorFlag().equals("1")) {
                            AvailCat.append("," + ListUniv.getId());
                        }
                    }

                    Intent intent = new Intent(getBaseContext(), AddNewRetailer.class);
                    intent.putExtra("Compititor_Id", CompIDServer);
                    intent.putExtra("Compititor_Name", Compititorname.getText().toString());
                    intent.putExtra("CatUniverSelectId", CatUniverId.toString());
                    intent.putExtra("AvailUniverSelectId", AvailCat.toString());

                    startActivity(intent);

                    // common_class.CommonIntentwithFinish(AddNewRetailer.class);
                }
                break;

            case R.id.Compititorname:
                customDialog = new CustomListViewDialog(Route_Product_Info.this, Compititor_List, -1);
                Window chillwindow = customDialog.getWindow();
                chillwindow.setGravity(Gravity.CENTER);
                chillwindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
                break;

        }
    }

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        if (type != -1) {
            if (type == 1) {
                Compititor_List.get(Compititor_List.indexOf(myDataset.get(position))).setSelected(true);
            } else {
                Compititor_List.get(Compititor_List.indexOf(myDataset.get(position))).setSelected(false);
            }
            StringBuilder Compname = new StringBuilder();
            StringBuilder CompId = new StringBuilder();
            for (Common_Model fil : Compititor_List) {
                if (fil.isSelected() == true) {
                    Compname.append("," + fil.getName());
                    CompId.append("," + fil.getName());
                }
            }
            Log.e("IndexofPOSITION", String.valueOf(Compititor_List.indexOf(myDataset.get(position))));
            Compititorname.setText("" + Compname);
            CompIDServer = CompId.toString();
            Log.e("GOOGLEPOSITION", String.valueOf(type));
        }
    }


    public class CustomAdapteravailablity extends BaseAdapter {
        Context context;
        LayoutInflater inflter;

        public CustomAdapteravailablity(Context applicationContext, List<Category_Universe_Modal> listt) {
            this.context = applicationContext;
            Availlistt = listt;
            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return Availlistt.size();
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
            name.setText(Availlistt.get(i).getName());
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("BACKROUND_COLOR", Availlistt.get(i).getColorFlag());
                    if (Availlistt.get(i).getColorFlag().equals("0")) {
                        Availlistt.get(i).setColorFlag("1");
                        ShowCatUniverse(i, "1");
                        gridcolor.setBackground(getResources().getDrawable(R.drawable.grid_backround));
                    } else {
                        Availlistt.get(i).setColorFlag("0");
                        gridcolor.setBackground(getResources().getDrawable(R.drawable.grid_backround_red));
                        ShowCatUniverse(i, "0");
                    }
                }
            });
            if (Availlistt.get(i).getColorFlag().equals("0")) {
                gridcolor.setBackground(getResources().getDrawable(R.drawable.grid_backround_red));
            } else {
                gridcolor.setBackground(getResources().getDrawable(R.drawable.grid_backround));
            }
            return view;
        }
    }

    public class CustomCategoryAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflter;

        public CustomCategoryAdapter(Context applicationContext, List<Category_Universe_Modal> listt) {
            this.context = applicationContext;
            Universelistt = listt;
            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return Universelistt.size();
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
            name.setText(Universelistt.get(i).getName()); // set logo images
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Universelistt.get(i).getColorFlag().equals("0")) {
                        Universelistt.get(i).setColorFlag("1");
                        gridcolor.setBackground(getResources().getDrawable(R.drawable.grid_backround));
                    } else {
                        Universelistt.get(i).setColorFlag("0");
                        gridcolor.setBackground(getResources().getDrawable(R.drawable.grid_backround_red));
                    }
                }
            });
            if (Universelistt.get(i).getColorFlag().equals("0")) {
                gridcolor.setBackground(getResources().getDrawable(R.drawable.grid_backround_red));
            } else {
                gridcolor.setBackground(getResources().getDrawable(R.drawable.grid_backround));
            }
            return view;
        }
    }



  /*  public void category_universe(String flag) {
        String commonworktype;
        if (flag.equals("1")) {
            commonworktype = "{\"tableName\":\"category_universe\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        } else {
            commonworktype = "{\"tableName\":\"get_compititordetails\"}";
        }
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "table/list");
        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
        QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
        QueryString.put("rSF", Shared_Common_Pref.Sf_Code);
        QueryString.put("State_Code", Shared_Common_Pref.StateCode);
        Log.e("EOOR_EXAMPLE", QueryString.toString());
        Call<Object> call = service.GetRouteObject(QueryString, commonworktype);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e("MAsterSyncView_Result", response.body() + "");
                System.out.println("Route_Matser" + response.body().toString());
                if (flag.equals("1")) {
                    GetJsonData(new Gson().toJson(response.body()), "1");
                } else {
                    GetJsonData(new Gson().toJson(response.body()), "2");
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }*/

    private void GetJsonData(String jsonResponse, String type) {
        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String id = String.valueOf(jsonObject1.optInt("id"));
                String name = jsonObject1.optString("name");
                if (type.equals("1")) {
                    String Division_Code = jsonObject1.optString("Division_Code");
                    String Cat_Image = jsonObject1.optString("Cat_Image");
                    String sampleQty = jsonObject1.optString("sampleQty");
                    String colorflag = jsonObject1.optString("colorflag");
                    Category_univ_Modal.add(new Category_Universe_Modal(id, name, Division_Code, Cat_Image, sampleQty, colorflag));
                } else {
                    Compititor_List.add(new Common_Model(name, id, false));

                }
            }
            if (type.equals("1")) {
                int index = 0;
                for (Category_Universe_Modal cuv : Category_univ_Modal) {
                    System.out.println("Outlet_Avail" + Shared_Common_Pref.OutletAvail);
                    System.out.println("Outlet_getId" + cuv.getId());
                    if (String.valueOf(Shared_Common_Pref.OutletAvail).indexOf(cuv.getId()) > -1) {
                        Category_univ_Modal.get(index).setColorFlag("1");
                        index++;

                    }
                }
                CustomCategoryAdapter customAdapter = new CustomCategoryAdapter(getApplicationContext(), Category_univ_Modal);
                Categorygrid.setAdapter(customAdapter);
                int indexx = 0;
                for (Category_Universe_Modal cuv : Category_univ_Modal) {
                    System.out.println("Outlet_Avail" + Shared_Common_Pref.OutletUniv);
                    System.out.println("Outlet_getId" + cuv.getId());
                    if (String.valueOf(Shared_Common_Pref.OutletUniv).indexOf(cuv.getId()) > -1) {
                        Category_univ_Modal.get(indexx).setColorFlag("1");
                        index++;

                    }
                }

                CustomAdapteravailablity customAdapteravail = new CustomAdapteravailablity(getApplicationContext(), Category_univ_Modal);
                availablitygrid.setAdapter(customAdapteravail);
            }
            System.out.println("ThiruLIST" + String.valueOf(Compititor_List.size()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void ShowCatUniverse(int position, String colorflag) {
        Log.e("AVAIL_CLOICK", String.valueOf(position));
        Universelistt.get(position).setColorFlag(colorflag);
        CustomCategoryAdapter customAdapter = new CustomCategoryAdapter(getApplicationContext(), Universelistt);
        Categorygrid.setAdapter(customAdapter);
    }
}
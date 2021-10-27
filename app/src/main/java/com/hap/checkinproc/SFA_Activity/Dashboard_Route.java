package com.hap.checkinproc.SFA_Activity;

import static android.Manifest.permission.CALL_PHONE;
import static com.hap.checkinproc.Common_Class.Constants.Retailer_OutletList;
import static com.hap.checkinproc.Common_Class.Constants.Rout_List;
import static com.hap.checkinproc.Common_Class.Constants.Route_Id;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Activity_Hap.SFA_Activity;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.Interface.OnLiveUpdateListener;
import com.hap.checkinproc.Interface.UpdateResponseUI;
import com.hap.checkinproc.MVP.Main_Model;
import com.hap.checkinproc.Model_Class.Route_Master;
import com.hap.checkinproc.PushNotification.MyFirebaseMessagingService;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.Route_View_Adapter;
import com.hap.checkinproc.SFA_Model_Class.OutletReport_View_Modal;
import com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List;
import com.hap.checkinproc.common.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard_Route extends AppCompatActivity implements Main_Model.MasterSyncView, View.OnClickListener, Master_Interface, UpdateResponseUI {
    public static final String CheckInDetail = "CheckInDetail";
    public static final String UserDetail = "MyPrefs";
    public static Dashboard_Route dashboard_route;
    public static Common_Class common_class;
    public static TextView distributor_text;
    public static Shared_Common_Pref shared_common_pref;
    List<Retailer_Modal_List> Retailer_Modal_List = new ArrayList<>();
    List<Retailer_Modal_List> Retailer_Modal_ListFilter = new ArrayList<>();
    List<OutletReport_View_Modal> Retailer_Order_List;
    Gson gson;
    Type userTypeRetailor, userTypeReport;
    TextView headtext, textViewname, ReachedOutlet, route_text,
            txSrvOtlt, txUniOtlt, txSrvOtltCnt, txUniOtltCnt, smryOrd, smryNOrd, smryNOOrd, smryInv, smryInvVal;
    EditText txSearchRet;
    LinearLayout btnCmbRoute, btSrvOtlt, btUniOtlt, undrUni, undrServ;
    Common_Model Model_Pojo;
    List<Common_Model> distributor_master = new ArrayList<>();
    List<Common_Model> FRoute_Master = new ArrayList<>();
    String Route_id;
    String Distributor_Id;
    String DCRMode;
    String sDeptType, RetType = "1";
    SharedPreferences CheckInDetails;
    SharedPreferences UserDetails;
    DatabaseHandler db;

    ImageView ivToolbarHome, ivBtnRpt;
    LinearLayout llDistributor, llOrder, llNewOrder, llInvoice, llNoOrder;
    TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1001;
    int CountUR = 0, CountSR = 0;
    Boolean StopedUpdate;
    ApiInterface apiInterface;
    boolean updSale = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard__route);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        dashboard_route = this;
        StopedUpdate = false;

        db = new DatabaseHandler(this);
        getDbstoreData();

        common_class = new Common_Class(this);
        shared_common_pref = new Shared_Common_Pref(this);
        CheckInDetails = getSharedPreferences(CheckInDetail, Context.MODE_PRIVATE);
        UserDetails = getSharedPreferences(UserDetail, Context.MODE_PRIVATE);
        getSalesCounts();
        JSONObject jParam = new JSONObject();
        try {
            jParam.put("SF", UserDetails.getString("Sfcode", ""));
            jParam.put("div", UserDetails.getString("Divcode", ""));
            apiInterface.getDataArrayList("get/prodgroup", jParam.toString()).enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    db.deleteMasterData("PGroup");
                    db.addMasterData("PGroup", response.body());
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new MyFirebaseMessagingService().setOnLiveUpdateListener(new OnLiveUpdateListener() {
            @Override
            public void onUpdate(String mode) {
                Log.d("LiveEvent", "reloadList");
                if (mode.equalsIgnoreCase("reloadSale") && updSale == false) {
                    getSalesCounts();
                    getLastInvoiceData();
                }
            }
        });

        common_class.getDataFromApi(Constants.Outlet_Total_Orders, this, false);
        try {
            headtext = findViewById(R.id.headtext);
            route_text = findViewById(R.id.route_text);
            distributor_text = findViewById(R.id.distributor_text);
            textViewname = findViewById(R.id.textViewname);
            ReachedOutlet = findViewById(R.id.ReachedOutlet);
            btnCmbRoute = findViewById(R.id.btnCmbRoute);
            ivToolbarHome = findViewById(R.id.toolbar_home);
            llDistributor = findViewById(R.id.llDistributor);
            llOrder = findViewById(R.id.llOrder);
            llNewOrder = findViewById(R.id.llNewOrder);
            llNoOrder = findViewById(R.id.llNoOrder);
            llInvoice = findViewById(R.id.llInv);
            txSearchRet = findViewById(R.id.txSearchRet);
            txSrvOtlt = findViewById(R.id.txSrvOtlt);
            txSrvOtltCnt = findViewById(R.id.txSrvOtltCnt);
            txUniOtltCnt = findViewById(R.id.txUniOtltCnt);
            txUniOtlt = findViewById(R.id.txUniOtlt);
            btSrvOtlt = findViewById(R.id.btSrvOtlt);
            btUniOtlt = findViewById(R.id.btUniOtlt);
            ivBtnRpt = findViewById(R.id.ivBtnRpt);

            smryOrd = findViewById(R.id.smryOrd);
            smryNOrd = findViewById(R.id.smryNOrd);
            smryNOOrd = findViewById(R.id.smryNOOrd);
            smryInv = findViewById(R.id.smryInv);
            smryInvVal = findViewById(R.id.smryInvVal);

            undrServ = findViewById(R.id.undrServ);
            undrUni = findViewById(R.id.undrUni);
            viewPager = findViewById(R.id.viewpager);
            viewPager.setOffscreenPageLimit(4);
            tabLayout = findViewById(R.id.tabs);

            ReachedOutlet.setOnClickListener(this);
            distributor_text.setOnClickListener(this);
            route_text.setOnClickListener(this);
            ivToolbarHome.setOnClickListener(this);
            btnCmbRoute.setOnClickListener(this);
            llDistributor.setOnClickListener(this);
            llOrder.setOnClickListener(this);
            llNewOrder.setOnClickListener(this);
            llNoOrder.setOnClickListener(this);
            llInvoice.setOnClickListener(this);

            ivBtnRpt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    common_class.CommonIntentwithoutFinish(HistoryInfoActivity.class);
                }
            });
            btSrvOtlt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RetType = "1";
                    txSrvOtlt.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txSrvOtlt.setTypeface(null, Typeface.BOLD);
                    undrServ.setVisibility(View.VISIBLE);
                    undrUni.setVisibility(View.INVISIBLE);
                    txUniOtlt.setTypeface(null, Typeface.NORMAL);
                    txUniOtlt.setTextColor(getResources().getColor(R.color.grey_900));
                    setPagerAdapter(false);
                    // SearchRetailers();
                }
            });
            btUniOtlt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RetType = "0";
                    txUniOtlt.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    txUniOtlt.setTypeface(null, Typeface.BOLD);
                    undrUni.setVisibility(View.VISIBLE);
                    undrServ.setVisibility(View.INVISIBLE);
                    txSrvOtlt.setTypeface(null, Typeface.NORMAL);
                    txSrvOtlt.setTextColor(getResources().getColor(R.color.grey_900));

                    setPagerAdapter(false);
                }
            });

            txSearchRet.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    setPagerAdapter(true);


                }
            });
            gson = new Gson();


            userTypeRetailor = new TypeToken<ArrayList<Retailer_Modal_List>>() {
            }.getType();
            // GetJsonData(shared_common_pref.getvalue(Shared_Common_Pref.Todaydayplanresult), "6");
            DCRMode = shared_common_pref.getvalue(Shared_Common_Pref.DCRMode);
            if (DCRMode.equalsIgnoreCase("SC")) {
                headtext.setText("SALES CALLS");
            }
            DCRMode = shared_common_pref.getvalue(Shared_Common_Pref.DCRMode);
            if (DCRMode.equalsIgnoreCase("VC")) {
                headtext.setText("VAN ROUTE SUPPLY");
            }

            Retailer_Modal_ListFilter = new ArrayList<>();
            Retailer_Modal_List = new ArrayList<>();
            if (!shared_common_pref.getvalue(Constants.Distributor_Id).equals("")) {
                common_class.getDb_310Data(Rout_List, this);
                getLastInvoiceData();
                String outletserializableob = shared_common_pref.getvalue(Constants.Retailer_OutletList);
                Retailer_Modal_List = gson.fromJson(outletserializableob, userTypeRetailor);
                distributor_text.setText(shared_common_pref.getvalue(Constants.Distributor_name));
                Distributor_Id = shared_common_pref.getvalue(Constants.Distributor_Id);
                loadroute();


                if (!shared_common_pref.getvalue(Route_Id).equals("")) {
                    route_text.setText(shared_common_pref.getvalue(Constants.Route_name));
                }
                if (Retailer_Modal_List != null) {

                    String todayorderliost = String.valueOf(db.getMasterData(Constants.Outlet_Total_Orders));

                    userTypeReport = new TypeToken<ArrayList<OutletReport_View_Modal>>() {
                    }.getType();
                    Retailer_Order_List = gson.fromJson(todayorderliost, userTypeReport);
                    if (Retailer_Modal_List != null && Retailer_Modal_List.size() > 0) {
                        for (int i = 0; Retailer_Modal_List.size() > i; i++) {
                            for (int j = 0; Retailer_Order_List.size() > j; j++) {
                                if (Retailer_Modal_List.get(i).getId().equals(Retailer_Order_List.get(j).getOutletCode())) {
                                    Log.e("Invoice_Flag", Retailer_Order_List.get(j).getInvoice_Flag());
                                    if (Retailer_Order_List.get(j).getInvoice_Flag().equals("2")) {
                                        Retailer_Modal_List.get(i).setInvoiceDate(Retailer_Order_List.get(j).getOrderDate());
                                        Retailer_Modal_List.get(i).setInvoiceValues(String.valueOf(Retailer_Order_List.get(j).getInvoicevalues()));
                                        Retailer_Modal_List.get(i).setStatusname(String.valueOf(Retailer_Order_List.get(j).getStatus()));
                                        Retailer_Modal_List.get(i).setInvoice_Flag(Retailer_Order_List.get(j).getInvoice_Flag());
                                        Retailer_Modal_List.get(i).setValuesinv("" + Retailer_Order_List.get(j).getOrderValue());
                                    } else {
                                        Retailer_Modal_List.get(i).setInvoice_Flag(Retailer_Order_List.get(j).getInvoice_Flag());
                                    }
                                }
                            }
                        }
                    }
                    Retailer_Modal_ListFilter.clear();
//                    if (shared_common_pref.getvalue(Constants.Route_Id).equals(""))
//                        OutletFilter(shared_common_pref.getvalue(Constants.Distributor_Id), "1", true);
//                    else
//                        OutletFilter(shared_common_pref.getvalue(Route_Id), "0", true);

                    sDeptType = UserDetails.getString("DeptType", "");
                    sDeptType = "1";
                    btnCmbRoute.setVisibility(View.VISIBLE);

                }


            } else {
                btnCmbRoute.setVisibility(View.GONE);
            }

            setPagerAdapter(false);
            createTabFragment();

            if (shared_common_pref.getvalue(Constants.LOGIN_TYPE).equals(Constants.DISTRIBUTER_TYPE)) {
                distributor_text.setEnabled(false);
                findViewById(R.id.ivDistSpinner).setVisibility(View.GONE);
            }


        } catch (Exception e) {
            Log.e("Retailor List:ex ", e.getMessage());
            e.printStackTrace();
        }


    }


    public void getSalesCounts() {
        updSale = true;
        JSONObject jParam = new JSONObject();
        try {
            jParam.put("SF", UserDetails.getString("Sfcode", ""));
            jParam.put("div", UserDetails.getString("Divcode", ""));
            apiInterface.getDataArrayList("get/salessumry", jParam.toString()).enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    try {
                        JsonArray jRes = response.body();
                        Log.v("Salessumry:", response.body().toString());
                        if (jRes.size() > 0) {
                            JsonObject jItm = jRes.get(0).getAsJsonObject();
                            double invVal = jItm.get("InvVal").getAsDouble();
                            smryOrd.setText(jItm.get("Orders").getAsString());
                            smryNOrd.setText(jItm.get("NOrders").getAsString());
                            smryNOOrd.setText(jItm.get("NoOrder").getAsString());
                            smryInv.setText(jItm.get("InvCnt").getAsString());
                            smryInvVal.setText("₹" + new DecimalFormat("##0.00").format(invVal));
                        } else {

                            smryOrd.setText("0");
                            smryNOrd.setText("0");
                            smryNOOrd.setText("0");
                            smryInv.setText("0");
                            smryInvVal.setText("₹0.00");
                        }

                        updSale = false;
                        //if(StopedUpdate==false) updateSales();
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    updSale = false;
                    //if(StopedUpdate==false) updateSales();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StopedUpdate = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Distributor_Id != null) {
            common_class.getDb_310Data(Constants.RETAILER_STATUS, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0) {

                }
        }
    }

    private void getLastInvoiceData() {
        try {

            if (common_class.isNetworkAvailable(this)) {
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                JSONObject HeadItem = new JSONObject();
                HeadItem.put("distributorCode", shared_common_pref.getvalue(Constants.Distributor_Id));

                String div_code = Shared_Common_Pref.Div_Code.replaceAll(",", "");
                HeadItem.put("divisionCode", div_code);


                Call<ResponseBody> call = service.getLastThreeMnthsData(HeadItem.toString());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        InputStreamReader ip = null;
                        StringBuilder is = new StringBuilder();
                        String line = null;
                        try {


                            if (response.isSuccessful()) {
                                ip = new InputStreamReader(response.body().byteStream());
                                BufferedReader bf = new BufferedReader(ip);
                                while ((line = bf.readLine()) != null) {
                                    is.append(line);
                                    Log.v("Res>>", is.toString());
                                }

                                shared_common_pref.save(Constants.RetailorTodayData, is.toString());

                            }

                        } catch (Exception e) {

                            Log.v("fail>>1", e.getMessage());

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.v("fail>>2", t.toString());


                    }
                });
            } else {
                common_class.showMsg(Dashboard_Route.dashboard_route, "Please check your internet connection");
            }
        } catch (Exception e) {
            Log.v("fail>>", e.getMessage());


        }
    }

    private void createTabFragment() {
        adapter = new TabAdapter(getSupportFragmentManager(), tabLayout.getSelectedTabPosition(), Retailer_Modal_ListFilter, RetType);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llOrder:
                Intent intent = new Intent(getApplicationContext(), DashboardInfoActivity.class);
                intent.putExtra("type", "Orders");
                startActivity(intent);
                break;
            case R.id.llNewOrder:
                Intent intentNew = new Intent(getApplicationContext(), DashboardInfoActivity.class);
                intentNew.putExtra("type", "New Order");
                startActivity(intentNew);
                break;
            case R.id.llNoOrder:
                Intent intentNO = new Intent(getApplicationContext(), DashboardInfoActivity.class);
                intentNO.putExtra("type", "Orders");
                intentNO.putExtra("status", "No Order");
                startActivity(intentNO);
                break;
            case R.id.llInv:
                Intent intentInv = new Intent(getApplicationContext(), DashboardInfoActivity.class);
                intentInv.putExtra("type", "Invoice");
                startActivity(intentInv);
                break;

            case R.id.ReachedOutlet:
                //if (Distributor_Id == null || Distributor_Id.equals("")) {
                /*if (distributor_text.getText().toString().equals("")) {
                    Toast.makeText(this, "Select The Distributor", Toast.LENGTH_SHORT).show();
                } else if (route_text.getText().toString().equals("")) {
                    Toast.makeText(this, "Select The Route", Toast.LENGTH_SHORT).show();
                } else {*/
                // shared_common_pref.save("RouteSelect", Route_id);
                // shared_common_pref.save("RouteName", route_text.getText().toString());
                // shared_common_pref.save("Distributor_ID", Distributor_Id);
                // Shared_Common_Pref.Outler_AddFlag = "1";
                common_class.CommonIntentwithoutFinish(Nearby_Outlets.class);

                overridePendingTransition(R.anim.in, R.anim.out);
                //common_class.CommonIntentwithoutFinish(New_Outlet_Map_creations.class);
                //}
                break;
            case R.id.distributor_text:
                common_class.showCommonDialog(distributor_master, 2, this);
                break;
            case R.id.route_text:
                if (FRoute_Master != null && FRoute_Master.size() > 1) {
                    common_class.showCommonDialog(FRoute_Master, 3, this);
                }
                break;
            case R.id.toolbar_home:
                common_class.CommonIntentwithoutFinish(SFA_Activity.class);
                break;
        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {
    }

    @Override
    public void setDataToRoute(ArrayList<Route_Master> noticeArrayList) {
        Log.e("ROUTE_MASTER", String.valueOf(noticeArrayList.size()));
    }

    @Override
    public void OnclickMasterType(java.util.List<Common_Model> myDataset, int position, int type) {

        common_class.dismissCommonDialog();
        if (type == 2) {
            route_text.setText("");
            shared_common_pref.save(Constants.Route_name, "");
            shared_common_pref.save(Constants.Route_Id, "");
            Distributor_Id = myDataset.get(position).getId();
            btnCmbRoute.setVisibility(View.VISIBLE);
            distributor_text.setText(myDataset.get(position).getName());
            shared_common_pref.save(Constants.Distributor_name, myDataset.get(position).getName());
            shared_common_pref.save(Constants.Distributor_Id, myDataset.get(position).getId());
            shared_common_pref.save(Constants.TEMP_DISTRIBUTOR_ID, myDataset.get(position).getId());
            shared_common_pref.save(Constants.Distributor_phone, myDataset.get(position).getPhone());
            common_class.getDb_310Data(Constants.RETAILER_STATUS, this);
            getLastInvoiceData();
            common_class.getDataFromApi(Retailer_OutletList, this, false);
            common_class.getDb_310Data(Rout_List, this);
        } else if (type == 3) {
            route_text.setText(myDataset.get(position).getName());
            shared_common_pref.save(Constants.Route_name, myDataset.get(position).getName());
            shared_common_pref.save(Constants.Route_Id, myDataset.get(position).getId());
            setPagerAdapter(false);
        }
    }

    @Override
    public void setDataToRouteObject(Object noticeArrayList, int position) {
    }

    @Override
    public void onResponseFailure(Throwable throwable) {


    }

    public void loadroute() {

        if (FRoute_Master.size() == 1) {
            findViewById(R.id.ivRouteSpinner).setVisibility(View.INVISIBLE);
            route_text.setText(FRoute_Master.get(0).getName());
            shared_common_pref.save(Constants.Route_name, FRoute_Master.get(0).getName());
            shared_common_pref.save(Constants.Route_Id, FRoute_Master.get(0).getId());

        } else {
            findViewById(R.id.ivRouteSpinner).setVisibility(View.VISIBLE);
        }
    }

    void getDbstoreData() {
        try {
            // JSONArray jsonArray = db.getMasterData(listType);
            JSONArray jsonArray = new JSONArray(shared_common_pref.getvalue(Constants.Distributor_List));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String id = String.valueOf(jsonObject1.optInt("id"));
                String name = jsonObject1.optString("name");
                String flag = jsonObject1.optString("FWFlg");
                String ETabs = jsonObject1.optString("ETabs");
                Model_Pojo = new Common_Model(id, name, flag);
                String Add2 = jsonObject1.optString("Addr2");
                String Mob = jsonObject1.optString("Mobile");
                Model_Pojo = new Common_Model(name, id, flag, Add2, Mob);
                distributor_master.add(Model_Pojo);

            }


        } catch (Exception e) {

        }


    }


    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {
            if (apiDataResponse != null) {
                switch (key) {
                    case Rout_List:
                        JSONArray routeArr = new JSONArray(apiDataResponse);
                        FRoute_Master.clear();
                        for (int i = 0; i < routeArr.length(); i++) {
                            JSONObject jsonObject1 = routeArr.getJSONObject(i);
                            String id = String.valueOf(jsonObject1.optInt("id"));
                            String name = jsonObject1.optString("name");
                            String flag = jsonObject1.optString("FWFlg");
                            Model_Pojo = new Common_Model(id, name, flag);
                            Model_Pojo = new Common_Model(id, name, jsonObject1.optString("stockist_code"));
                            FRoute_Master.add(Model_Pojo);

                        }
                        loadroute();
                        break;
                    case Retailer_OutletList:
                        setPagerAdapter(false);
                        break;
                    case Constants.RETAILER_STATUS:
                        JSONObject jsonObject = new JSONObject(apiDataResponse);
                        if (jsonObject.getBoolean("success")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("Data");
                            String outletCode = "";


                            for (int arr = 0; arr < jsonArray.length(); arr++) {
                                JSONObject arrObj = jsonArray.getJSONObject(arr);

                                int flag = arrObj.getInt("OrderFlg");
                                //  To be=0,invoice-3,order-2,no order-1;
                                String sMode = flag == 0 ? "To be" : flag == 3 ? "invoice" : flag == 2 ? "order" : "no order";

                                outletCode = outletCode + arrObj.getString("ListedDrCode") + sMode + ",";


                            }

                            shared_common_pref.save(Constants.RETAILER_STATUS, outletCode);

                            Log.v("statusList:", outletCode);

                            setPagerAdapter(false);
                        }
                        break;
                }
            }
        } catch (Exception e) {

        }

    }

    void setPagerAdapter(boolean isFilter) {

        if (!shared_common_pref.getvalue(Constants.Distributor_Id).equals("")) {
            String outletserializableob = shared_common_pref.getvalue(Constants.Retailer_OutletList);
            Retailer_Modal_List = gson.fromJson(outletserializableob, userTypeRetailor);
        }


        Retailer_Modal_ListFilter.clear();
        CountUR = 0;
        CountSR = 0;

        for (int i = 0; i < Retailer_Modal_List.size(); i++) {
            if (!Retailer_Modal_List.get(i).getType().equalsIgnoreCase("1")) CountUR++;
            if (Retailer_Modal_List.get(i).getType().equalsIgnoreCase("1")) CountSR++;


        }
        txUniOtltCnt.setText(String.valueOf(CountUR));
        txSrvOtltCnt.setText(String.valueOf(CountSR));

        Retailer_Modal_ListFilter = Retailer_Modal_List;


        if (isFilter) {
            adapter.notifyData(Retailer_Modal_ListFilter, tabLayout.getSelectedTabPosition(), txSearchRet.getText().toString(), RetType);

        } else {
            adapter = new TabAdapter(getSupportFragmentManager(), tabLayout.getSelectedTabPosition(), Retailer_Modal_ListFilter, RetType);
            viewPager.setCurrentItem(tabLayout.getSelectedTabPosition());
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    public static class AllDataFragment extends Fragment {
        View mView;
        String tabPosition = "";
        List<Retailer_Modal_List> mRetailer_Modal_ListFilter;
        private Context context;
        private RecyclerView recyclerView;

        public AllDataFragment(List<Retailer_Modal_List> retailer_Modal_ListFilter, int position) {
            this.mRetailer_Modal_ListFilter = retailer_Modal_ListFilter;
            this.tabPosition = String.valueOf(position);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            return inflater.inflate(R.layout.fragment_tab_outlet, container, false);
        }


        public void updateData() {
            recyclerView.setAdapter(new Route_View_Adapter(mRetailer_Modal_ListFilter, R.layout.route_dashboard_recyclerview, getActivity(), new AdapterOnClick() {
                @Override
                public void onIntentClick(int position) {
                    try {

                        if (dashboard_route.Distributor_Id == null || dashboard_route.Distributor_Id.equalsIgnoreCase("")) {
                            Toast.makeText(getActivity(), "Select The Distributor", Toast.LENGTH_SHORT).show();
                        } else if (shared_common_pref.getvalue(Route_Id).equals("") && !dashboard_route.sDeptType.equalsIgnoreCase("2")) {
                            Toast.makeText(getActivity(), "Select The Route", Toast.LENGTH_SHORT).show();
                        } else {
                            Shared_Common_Pref.Outler_AddFlag = "0";
                            Shared_Common_Pref.OutletName = mRetailer_Modal_ListFilter.get(position).getName().toUpperCase();
                            Shared_Common_Pref.OutletCode = mRetailer_Modal_ListFilter.get(position).getId();
                            Shared_Common_Pref.DistributorCode = dashboard_route.Distributor_Id;
                            Shared_Common_Pref.DistributorName = distributor_text.getText().toString();
                            // Shared_Common_Pref.Route_Code = dashboard_route.Route_id;
                            // common_class.CommonIntentwithoutFinish(Route_Product_Info.class);
                            shared_common_pref.save(Constants.Retailor_Address, mRetailer_Modal_ListFilter.get(position).getListedDrAddress1());
                            shared_common_pref.save(Constants.Retailor_ERP_Code, mRetailer_Modal_ListFilter.get(position).getERP_Code());
                            shared_common_pref.save(Constants.Retailor_Name_ERP_Code, mRetailer_Modal_ListFilter.get(position).getName().toUpperCase() /*+ "~"
                                    + mRetailer_Modal_ListFilter.get(position).getERP_Code()*/);
//                            if (mRetailer_Modal_ListFilter.get(position).getMobileNumber().equalsIgnoreCase("")
//                                    || mRetailer_Modal_ListFilter.get(position).getOwner_Name().equalsIgnoreCase("")) {
//
//                                Intent intent = new Intent(context, AddNewRetailer.class);
//                                Shared_Common_Pref.Outlet_Info_Flag = "0";
//                                Shared_Common_Pref.Editoutletflag = "1";
//                                Shared_Common_Pref.Outler_AddFlag = "0";
//                                Shared_Common_Pref.OutletCode = String.valueOf(mRetailer_Modal_ListFilter.get(position).getId());
//                                intent.putExtra("OutletCode", String.valueOf(mRetailer_Modal_ListFilter.get(position).getId()));
//                                intent.putExtra("OutletName", mRetailer_Modal_ListFilter.get(position).getName());
//                                intent.putExtra("OutletAddress", mRetailer_Modal_ListFilter.get(position).getListedDrAddress1());
//                                intent.putExtra("OutletMobile", mRetailer_Modal_ListFilter.get(position).getMobileNumber());
//                                intent.putExtra("OutletRoute", mRetailer_Modal_ListFilter.get(position).getTownName());
//                                startActivity(intent);
//                                getActivity().finish();
//
//                            } else {
                            //common_class.CommonIntentwithoutFinish(Route_Product_Info.class);

                            shared_common_pref.save(Constants.Retailor_PHNo, mRetailer_Modal_ListFilter.get(position).getPrimary_No());
                            common_class.CommonIntentwithFinish(Invoice_History.class);
                            getActivity().overridePendingTransition(R.anim.in, R.anim.out);
                            //}

                        }
                    } catch (Exception e) {

                    }
                }

                @Override
                public void CallMobile(String MobileNo) {
                    Log.d("Event", "CAll Mobile");
                    int readReq = ContextCompat.checkSelfPermission(context, CALL_PHONE);
                    if (readReq != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(HAPApp.activeActivity, new String[]{CALL_PHONE}, REQUEST_PERMISSIONS_REQUEST_CODE);
                    } else {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + MobileNo));//change the number
                        startActivity(callIntent);
                    }
                }
            }));
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            this.context = getContext();
            mView = view;
            recyclerView = view.findViewById(R.id.recyclerView);
            updateData();
        }
    }
}

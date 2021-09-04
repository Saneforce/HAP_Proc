package com.hap.checkinproc.SFA_Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Activity_Hap.CustomListViewDialog;
import com.hap.checkinproc.Activity_Hap.SFA_Activity;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.Interface.LocationEvents;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.Interface.UpdateResponseUI;
import com.hap.checkinproc.MVP.Main_Model;
import com.hap.checkinproc.MVP.MasterSync_Implementations;
import com.hap.checkinproc.MVP.Master_Sync_View;
import com.hap.checkinproc.Model_Class.Route_Master;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.Route_View_Adapter;
import com.hap.checkinproc.SFA_Model_Class.OutletReport_View_Modal;
import com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List;
import com.hap.checkinproc.common.DatabaseHandler;
import com.hap.checkinproc.common.LocationFinder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.hap.checkinproc.Common_Class.Constants.Retailer_OutletList;

public class Dashboard_Route extends AppCompatActivity implements Main_Model.MasterSyncView, View.OnClickListener, Master_Interface, UpdateResponseUI {
    public static final String CheckInDetail = "CheckInDetail";
    public static final String UserDetail = "MyPrefs";
    public static Dashboard_Route dashboard_route;
    static Common_Class common_class;
    static TextView distributor_text;
    static Shared_Common_Pref shared_common_pref;
    public int scrollPosition = 0;
    List<Retailer_Modal_List> Retailer_Modal_List = new ArrayList<>();
    List<Retailer_Modal_List> Retailer_Modal_ListFilter = new ArrayList<>();
    List<OutletReport_View_Modal> Retailer_Order_List;
    Gson gson;
    Type userTypeRetailor, userTypeReport;
    TextView headtext;
    TextView textViewname;
    TextView Alltextclick;
    TextView Completeclick;
    TextView Pendingclick;
    TextView ReachedOutlet;
    TextView route_text;
    View Alltextview, completeview, pendingview;
    LinearLayout btnCmbRoute;
    Common_Model Model_Pojo;
    List<Common_Model> distributor_master = new ArrayList<>();
    List<Common_Model> Route_Masterlist = new ArrayList<>();
    CustomListViewDialog customDialog;
    List<Common_Model> FRoute_Master = new ArrayList<>();
    String Route_id;
    String Distributor_Id;
    String DCRMode;
    String sDeptType;
    SharedPreferences CheckInDetails;
    SharedPreferences UserDetails;
    DatabaseHandler db;

    ImageView ivToolbarHome;
    LinearLayout llDistributor;
    TabAdapter adapter;
    private RecyclerView recyclerView;
    private Main_Model.presenter presenter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard__route);
        dashboard_route = this;
        db = new DatabaseHandler(this);
        getDbstoreData(Constants.Distributor_List);
        getDbstoreData(Constants.Rout_List);

        common_class = new Common_Class(this);
        common_class.getDataFromApi(Constants.Outlet_Total_Orders, this, false);


    }

    @Override
    public void onResume() {
        try {
            super.onResume();  // Always call the superclass method first
            recyclerView = findViewById(R.id.leaverecyclerview);
            presenter = new MasterSync_Implementations(this, new Master_Sync_View());
            presenter.requestDataFromServer();
            shared_common_pref = new Shared_Common_Pref(this);
            headtext = findViewById(R.id.headtext);
            route_text = findViewById(R.id.route_text);
            distributor_text = findViewById(R.id.distributor_text);
            textViewname = findViewById(R.id.textViewname);
            Alltextclick = findViewById(R.id.Alltextclick);
            Completeclick = findViewById(R.id.Completeclick);
            Pendingclick = findViewById(R.id.Pendingclick);
            Alltextview = findViewById(R.id.Alltextview);
            completeview = findViewById(R.id.completeview);
            ReachedOutlet = findViewById(R.id.ReachedOutlet);
            pendingview = findViewById(R.id.pendingview);
            btnCmbRoute = findViewById(R.id.btnCmbRoute);
            ivToolbarHome = findViewById(R.id.toolbar_home);
            llDistributor = findViewById(R.id.llDistributor);

            viewPager = findViewById(R.id.viewpager);
            viewPager.setOffscreenPageLimit(3);
            tabLayout = findViewById(R.id.tabs);

            Alltextview.setVisibility(View.VISIBLE);
            completeview.setVisibility(View.INVISIBLE);
            pendingview.setVisibility(View.INVISIBLE);
            Alltextclick.setOnClickListener(this);
            Completeclick.setOnClickListener(this);
            Pendingclick.setOnClickListener(this);
            ReachedOutlet.setOnClickListener(this);
            distributor_text.setOnClickListener(this);
            route_text.setOnClickListener(this);
            ivToolbarHome.setOnClickListener(this);
            btnCmbRoute.setOnClickListener(this);
            llDistributor.setOnClickListener(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            gson = new Gson();

            CheckInDetails = getSharedPreferences(CheckInDetail, Context.MODE_PRIVATE);
            UserDetails = getSharedPreferences(UserDetail, Context.MODE_PRIVATE);


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

                String outletserializableob = shared_common_pref.getvalue(Constants.Retailer_OutletList);
                //  String outletserializableob = null;
//
                //      outletserializableob = String.valueOf(db.getMasterData(Constants.Retailer_OutletList));
//

                Log.e("Retailor List: ", outletserializableob);
                Retailer_Modal_List = gson.fromJson(outletserializableob, userTypeRetailor);


                distributor_text.setText(shared_common_pref.getvalue(Constants.Distributor_name));


                Distributor_Id = shared_common_pref.getvalue(Constants.Distributor_Id);
                loadroute(shared_common_pref.getvalue(Constants.Distributor_Id));


                if (!shared_common_pref.getvalue(Constants.Route_name).equals("")) {
                    route_text.setText(shared_common_pref.getvalue(Constants.Route_name));
                    Route_id = shared_common_pref.getvalue(Constants.Route_Id);
                    //OutletFilter(shared_common_pref.getvalue(Constants.Route_Id), "0");
                }

                if (Retailer_Modal_List != null) {


                    // String todayorderliost = shared_common_pref.getvalue(Shared_Common_Pref.Outlet_Total_Orders);
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
                                        //Log.e("INVOICE_Refrence", Retailer_Modal_ListFilter.get(j).getInvoice_Flag()+"Outlet_Code"+Retailer_Order_List.get(j).getOutletCode());
                                        Retailer_Modal_List.get(i).setValuesinv("" + Retailer_Order_List.get(j).getOrderValue());
                                    } else {
                                        Log.e("Invoice_Flag", Retailer_Order_List.get(j).getInvoice_Flag());
                                        Retailer_Modal_List.get(i).setInvoice_Flag(Retailer_Order_List.get(j).getInvoice_Flag());
                                    }
                                }
                            }
                        }
                    }
                    Retailer_Modal_ListFilter.clear();
//                    if (Distributor_Id == null) {
//                        Retailer_Modal_ListFilter.addAll(Retailer_Modal_List);
//                    } else {
                    if (shared_common_pref.getvalue(Constants.Route_Id).equals(""))
                        OutletFilter(Distributor_Id, "1", true);
                    else
                        OutletFilter(Distributor_Id, "0", true);

                    //}
                    sDeptType = UserDetails.getString("DeptType", "");
                    sDeptType = "1";
                    btnCmbRoute.setVisibility(View.VISIBLE);
//                    if (sDeptType.equalsIgnoreCase("2")) {
//                        btnCmbRoute.setVisibility(View.GONE);
//                    }
                    recyclerView.setAdapter(new Route_View_Adapter(Retailer_Modal_ListFilter, R.layout.route_dashboard_recyclerview, getApplicationContext(), new AdapterOnClick() {
                        @Override
                        public void onIntentClick(int position) {
                            Shared_Common_Pref.Outler_AddFlag = "0";
                            Log.e("Route_Outlet_Info", Retailer_Modal_List.get(position).getId());
                            Shared_Common_Pref.OutletName = Retailer_Modal_List.get(position).getName().toUpperCase();
                            Shared_Common_Pref.OutletCode = Retailer_Modal_List.get(position).getId();
                            Shared_Common_Pref.OutletAvail = Retailer_Modal_List.get(position).getHatsun_AvailablityId();
                            Shared_Common_Pref.OutletUniv = Retailer_Modal_List.get(position).getCategory_Universe_Id();
                            shared_common_pref.save("CurrLoc", "");
                            new LocationFinder(getApplication(), new LocationEvents() {
                                @Override
                                public void OnLocationRecived(Location location) {
                                    shared_common_pref.save("CurrLoc", location.getLatitude() + ":" + location.getLongitude());
                                }
                            });

                            shared_common_pref.save(Constants.Retailor_Address, Retailer_Modal_ListFilter.get(position).getListedDrAddress1());
                            shared_common_pref.save(Constants.Retailor_ERP_Code, Retailer_Modal_ListFilter.get(position).getERP_Code());
                            shared_common_pref.save(Constants.Retailor_Name_ERP_Code, Retailer_Modal_List.get(position).getName().toUpperCase() + "~" + Retailer_Modal_List.get(position).getERP_Code());

                            if (!DCRMode.equalsIgnoreCase("")) {
                                common_class.CommonIntentwithoutFinish(Invoice_History.class);
                            } else {
                                //common_class.CommonIntentwithoutFinish(Route_Product_Info.class);
                                common_class.CommonIntentwithoutFinish(Invoice_History.class);
                            }


                        }
                    }));


                }


            } else {
                btnCmbRoute.setVisibility(View.GONE);
            }

            createTabFragment();

            viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    Log.e("viewPager:", "onPageScrolled:" + position);
                }

                @Override
                public void onPageSelected(int position) {
                    scrollPosition = position;
                    Log.e("viewPager:", "onPageSelected:" + position);

                    // OutletFilter("t", String.valueOf(position + 1), false);


//                    adapter = new TabAdapter(getSupportFragmentManager(), tabLayout, Retailer_Modal_ListFilter);
//                    viewPager.setAdapter(adapter);
//                    tabLayout.setupWithViewPager(viewPager);


                }

                @Override
                public void onPageScrollStateChanged(int state) {

                    if (state == ViewPager.SCROLL_STATE_IDLE) {
                        // OutletFilter("t", String.valueOf(scrollPosition));
                        //adapter.notifyDataSetChanged();


                        // Toast.makeText(getApplicationContext(), "" + scrollPosition, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            Log.e("Retailor List:ex ", e.getMessage());

            e.printStackTrace();
        }
    }


    private void createTabFragment() {

        adapter = new TabAdapter(getSupportFragmentManager(), tabLayout, Retailer_Modal_ListFilter);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Alltextclick:
                // OutletFilter("t", "1");
                Alltextview.setVisibility(View.VISIBLE);
                completeview.setVisibility(View.INVISIBLE);
                pendingview.setVisibility(View.INVISIBLE);
                break;
            case R.id.Completeclick:
                //  OutletFilter("t", "2");
                Alltextview.setVisibility(View.INVISIBLE);
                completeview.setVisibility(View.VISIBLE);
                pendingview.setVisibility(View.INVISIBLE);
                break;
            case R.id.Pendingclick:
                //OutletFilter("t", "3");
                Alltextview.setVisibility(View.INVISIBLE);
                completeview.setVisibility(View.INVISIBLE);
                pendingview.setVisibility(View.VISIBLE);
                break;
            case R.id.ReachedOutlet:
                //if (Distributor_Id == null || Distributor_Id.equals("")) {
                if (distributor_text.getText().toString().equals("")) {
                    Toast.makeText(this, "Select The Distributor", Toast.LENGTH_SHORT).show();
                } else if (route_text.getText().toString().equals("")) {
                    Toast.makeText(this, "Select The Route", Toast.LENGTH_SHORT).show();
                } else {
                    shared_common_pref.save("RouteSelect", Route_id);
                    shared_common_pref.save("RouteName", route_text.getText().toString());
                    shared_common_pref.save("Distributor_ID", Distributor_Id);
                    common_class.CommonIntentwithoutFinish(New_Outlet_Map_creations.class);
                }
                break;
            case R.id.distributor_text:
                customDialog = new CustomListViewDialog(Dashboard_Route.this, distributor_master, 2);
                Window windoww = customDialog.getWindow();
                windoww.setGravity(Gravity.CENTER);
                windoww.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
                break;
            case R.id.route_text:
                if (FRoute_Master != null && FRoute_Master.size() > 1) {
                    customDialog = new CustomListViewDialog(Dashboard_Route.this, FRoute_Master, 3);
                    Window windowww = customDialog.getWindow();
                    windowww.setGravity(Gravity.CENTER);
                    windowww.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    customDialog.show();
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
        customDialog.dismiss();
        if (type == 2) {
            route_text.setText("");
            shared_common_pref.save(Constants.Route_Id, "");
            Distributor_Id = myDataset.get(position).getId();
            btnCmbRoute.setVisibility(View.VISIBLE);
            distributor_text.setText(myDataset.get(position).getName());
            shared_common_pref.save(Constants.Distributor_name, myDataset.get(position).getName());
            shared_common_pref.save(Constants.Distributor_Id, myDataset.get(position).getId());
            loadroute(myDataset.get(position).getId());
            OutletFilter(myDataset.get(position).getId(), "1", true);


        } else if (type == 3) {
            Route_id = myDataset.get(position).getId();
            route_text.setText(myDataset.get(position).getName());
            shared_common_pref.save(Constants.Route_name, myDataset.get(position).getName());
            shared_common_pref.save(Constants.Route_Id, myDataset.get(position).getId());
            Route_id = myDataset.get(position).getId();
            OutletFilter(myDataset.get(position).getId(), "0", true);


        }
    }


    public void OutletFilter(String id, String flag, Boolean pagerUpdate) {
        try {


            if (flag.equals("0")) {

                Retailer_Modal_ListFilter.clear();

                for (int i = 0; i < Retailer_Modal_List.size(); i++) {
                    if (id.equals(Retailer_Modal_List.get(i).getTownCode()))
                        Retailer_Modal_ListFilter.add(Retailer_Modal_List.get(i));
                }


                // shared_common_pref.save(Retailer_OutletList, gson.toJson(Retailer_Modal_ListFilter));
                TabAdapter adapter = new TabAdapter(getSupportFragmentManager(), tabLayout, Retailer_Modal_ListFilter);
                viewPager.setAdapter(adapter);
                tabLayout.setupWithViewPager(viewPager);

                adapter.notifyDataSetChanged();

                recyclerView.setAdapter(new Route_View_Adapter(Retailer_Modal_ListFilter, R.layout.route_dashboard_recyclerview, getApplicationContext(), new AdapterOnClick() {
                    @Override
                    public void onIntentClick(int position) {
                        try {
                            if (Distributor_Id == null || Distributor_Id.equalsIgnoreCase("")) {
                                Toast.makeText(Dashboard_Route.this, "Select The Distributor", Toast.LENGTH_SHORT).show();
                            } else if ((Route_id == null || Route_id.equalsIgnoreCase("")) && !sDeptType.equalsIgnoreCase("2")) {
                                Toast.makeText(Dashboard_Route.this, "Select The Route", Toast.LENGTH_SHORT).show();
                            } else {
                                Shared_Common_Pref.Outler_AddFlag = "0";
                                Shared_Common_Pref.OutletName = Retailer_Modal_ListFilter.get(position).getName().toUpperCase()

                                ;
                                Shared_Common_Pref.OutletCode = Retailer_Modal_ListFilter.get(position).getId();
                                Shared_Common_Pref.DistributorCode = Distributor_Id;
                                Shared_Common_Pref.DistributorName = distributor_text.getText().toString();
                                Shared_Common_Pref.Route_Code = shared_common_pref.getvalue(Constants.Route_Id);
                                //common_class.CommonIntentwithFinish(Route_Product_Info.class);
                                shared_common_pref.save(Constants.Retailor_Address, Retailer_Modal_ListFilter.get(position).getListedDrAddress1());
                                shared_common_pref.save(Constants.Retailor_ERP_Code, Retailer_Modal_ListFilter.get(position).getERP_Code());
                                shared_common_pref.save(Constants.Retailor_Name_ERP_Code, Retailer_Modal_List.get(position).getName().toUpperCase() + "~" + Retailer_Modal_List.get(position).getERP_Code());
                                common_class.CommonIntentwithoutFinish(Invoice_History.class);
                            }
                        } catch (Exception e) {
                            Log.e("DR:RetailorClick: ", e.getMessage());
                        }
                    }
                }));

            } else {

                common_class.getDataFromApi(Retailer_OutletList, this, false);
                //  getDataFromApi(Retailer_OutletList, this, false);
//                Retailer_Modal_ListFilter.clear();
//                Log.e("Retailer_Modal_ListSIZE", "" + Retailer_Modal_List.size());
//
//                common_class.getDataFromApi(Constants.Retailer_OutletList, this, false);
//
//
//                String outletserializableob = shared_common_pref.getvalue(Constants.Retailer_OutletList);
//
//                Log.e("Retailor List: ", outletserializableob);
//
//                Retailer_Modal_List.clear();
//                Retailer_Modal_ListFilter.clear();
//
//                Retailer_Modal_List = gson.fromJson(outletserializableob, userTypeRetailor);
//
//
//                for (int i = 0; i < Retailer_Modal_List.size(); i++) {
//
//                    Retailer_Modal_ListFilter.add(Retailer_Modal_List.get(i));
////                        if (flag.equals("0")) {
////                            if (Retailer_Modal_List.get(i).getTownCode().toLowerCase().trim().replaceAll("\\s", "").contains(id.toLowerCase().trim().replaceAll("\\s", ""))) {
////                                Retailer_Modal_ListFilter.add(Retailer_Modal_List.get(i));
////                            }
////                        }
////                        if (flag.equals("1")) {
////                            if (Retailer_Modal_List.get(i).getDistCode().toLowerCase().trim().replaceAll("\\s", "").contains(id.toLowerCase().trim().replaceAll("\\s", ""))) {
////                                Retailer_Modal_ListFilter.add(Retailer_Modal_List.get(i));
////                            }
////                        }
////                        if (flag.equals("2")) {
////                            if (Retailer_Modal_List.get(i).getInvoice_Flag().equals("2")) {
////                                Retailer_Modal_ListFilter.add(Retailer_Modal_List.get(i));
////                            }
////                        }
////                        if (flag.equals("3")) {
////                            if (!Retailer_Modal_List.get(i).getInvoice_Flag().equals("2")) {
////                                Retailer_Modal_ListFilter.add(Retailer_Modal_List.get(i));
////                            }
////                        }
//
//                }


            }


        } catch (Exception e) {
            Log.e("DR:RetailorFilter: ", e.getMessage());
        }

    }


    @Override
    public void setDataToRouteObject(Object noticeArrayList, int position) {
//        Log.e("Calling Position", String.valueOf(position));
//        Log.e("ROUTE_MASTER_Object", String.valueOf(noticeArrayList));
//        if (position == 0) {
//            Log.e("SharedprefrenceVALUES", new Gson().toJson(noticeArrayList));
//            GetJsonData(new Gson().toJson(noticeArrayList), "0");
//        }
//        //move to DB
//        //pos 1=distributor,pos 2=Route list
//
////        else if (position == 1) {
////            GetJsonData(new Gson().toJson(noticeArrayList), "1");
////        } else if (position == 2) {
////            GetJsonData(new Gson().toJson(noticeArrayList), "2");
////        }
//
//        else if (position == 3) {
//            GetJsonData(new Gson().toJson(noticeArrayList), "3");
//        } else if (position == 4) {
//            GetJsonData(new Gson().toJson(noticeArrayList), "4");
//        } else if (position == 5) {
//            GetJsonData(new Gson().toJson(noticeArrayList), "5");
//        } else {
//
//        }

    }

    @Override
    public void onResponseFailure(Throwable throwable) {


    }

    public void loadroute(String id) {
        if (Common_Class.isNullOrEmpty(String.valueOf(id))) {
            Toast.makeText(this, "Select the Distributor", Toast.LENGTH_SHORT).show();
        }
        FRoute_Master.clear();
        for (int i = 0; i < Route_Masterlist.size(); i++) {
            if (Route_Masterlist.get(i).getFlag().toLowerCase().trim().replaceAll("\\s", "").contains(id.toLowerCase().trim().replaceAll("\\s", ""))) {
                Log.e("Route_Masterlist", id + "STOCKIST" + Route_Masterlist.get(i).getFlag());
                FRoute_Master.add(new Common_Model(Route_Masterlist.get(i).getId(), Route_Masterlist.get(i).getName(), Route_Masterlist.get(i).getFlag()));
            }
        }

        if (FRoute_Master.size() == 1) {
            findViewById(R.id.ivRouteSpinner).setVisibility(View.INVISIBLE);
            route_text.setText(FRoute_Master.get(0).getName());
            shared_common_pref.save(Constants.Route_name, FRoute_Master.get(0).getName());
            shared_common_pref.save(Constants.Route_Id, FRoute_Master.get(0).getId());
            Route_id = FRoute_Master.get(0).getId();

        } else {
            findViewById(R.id.ivRouteSpinner).setVisibility(View.VISIBLE);

        }
    }

//    private void GetJsonData(String jsonResponse, String type) {
//        try {
//            JSONArray jsonArray = new JSONArray(jsonResponse);
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                String id = String.valueOf(jsonObject1.optInt("id"));
//                String name = jsonObject1.optString("name");
//                String flag = jsonObject1.optString("FWFlg");
//                String ETabs = jsonObject1.optString("ETabs");
//                Model_Pojo = new Common_Model(id, name, flag);
//                // if (type.equals("1")) {
//                // distributor_master.add(Model_Pojo);
//                // } else if (type.equals("2")) {
////                    Log.e("STOCKIST_CODE", jsonObject1.optString("stockist_code"));
////                    Model_Pojo = new Common_Model(id, name, jsonObject1.optString("stockist_code"));
////                    FRoute_Master.add(Model_Pojo);
////                    Route_Masterlist.add(Model_Pojo);
//                //} else
//                    if (type.equals("6")) {
//
//                    route_text.setText(jsonObject1.optString("ClstrName"));
//                    Distributor_Id = jsonObject1.optString("stockist");
//                    Route_id = jsonObject1.optString("cluster");
//                    distributor_text.setText(jsonObject1.optString("StkName"));
//                    loadroute(jsonObject1.optString("stockist"));
//
//
//                }
//
//            }
//
//
//            //spinner.setSelection(adapter.getPosition("select worktype"));
//            //            parseJsonData_cluster(clustspin_list);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }


    void getDbstoreData(String listType) {
        try {
            JSONArray jsonArray = db.getMasterData(listType);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String id = String.valueOf(jsonObject1.optInt("id"));
                String name = jsonObject1.optString("name");
                String flag = jsonObject1.optString("FWFlg");
                String ETabs = jsonObject1.optString("ETabs");
                Model_Pojo = new Common_Model(id, name, flag);
                if (listType.equals(Constants.Distributor_List)) {
                    distributor_master.add(Model_Pojo);
                } else if (listType.equals(Constants.Rout_List)) {
                    Log.e("STOCKIST_CODE", jsonObject1.optString("stockist_code"));
                    Model_Pojo = new Common_Model(id, name, jsonObject1.optString("stockist_code"));
                    FRoute_Master.add(Model_Pojo);
                    Route_Masterlist.add(Model_Pojo);
                }
//                else if (type.equals("6")) {
//
//                    route_text.setText(jsonObject1.optString("ClstrName"));
//                    Distributor_Id = jsonObject1.optString("stockist");
//                    Route_id = jsonObject1.optString("cluster");
//                    distributor_text.setText(jsonObject1.optString("StkName"));
//                    loadroute(jsonObject1.optString("stockist"));
//
//
//                }

            }

        } catch (Exception e) {

        }


    }


    @Override
    public void onLoadFilterData(List<com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List> retailer_modal_list) {

        if (retailer_modal_list != null) {

            Retailer_Modal_List = new ArrayList<>();
            Retailer_Modal_ListFilter = new ArrayList<>();


            Retailer_Modal_List.clear();

            Retailer_Modal_ListFilter.clear();

            Retailer_Modal_List = retailer_modal_list;


            Retailer_Modal_ListFilter = Retailer_Modal_List;


            TabAdapter adapter = new TabAdapter(getSupportFragmentManager(), tabLayout, Retailer_Modal_ListFilter);
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);

            adapter.notifyDataSetChanged();

            recyclerView.setAdapter(new Route_View_Adapter(Retailer_Modal_ListFilter, R.layout.route_dashboard_recyclerview, getApplicationContext(), new AdapterOnClick() {
                @Override
                public void onIntentClick(int position) {
                    if (Distributor_Id == null || Distributor_Id.equalsIgnoreCase("")) {
                        Toast.makeText(Dashboard_Route.this, "Select The Distributor", Toast.LENGTH_SHORT).show();
                    } else if ((Route_id == null || Route_id.equalsIgnoreCase("")) && !sDeptType.equalsIgnoreCase("2")) {
                        Toast.makeText(Dashboard_Route.this, "Select The Route", Toast.LENGTH_SHORT).show();
                    } else {
                        Shared_Common_Pref.Outler_AddFlag = "0";
                        Shared_Common_Pref.OutletName = Retailer_Modal_ListFilter.get(position).getName().toUpperCase()
                        ;
                        Shared_Common_Pref.OutletCode = Retailer_Modal_ListFilter.get(position).getId();
                        Shared_Common_Pref.DistributorCode = Distributor_Id;
                        Shared_Common_Pref.DistributorName = distributor_text.getText().toString();
                        Shared_Common_Pref.Route_Code = Route_id;

                        shared_common_pref.save(Constants.Retailor_Address, Retailer_Modal_ListFilter.get(position).getListedDrAddress1());
                        shared_common_pref.save(Constants.Retailor_ERP_Code, Retailer_Modal_ListFilter.get(position).getERP_Code());
                        shared_common_pref.save(Constants.Retailor_Name_ERP_Code, Retailer_Modal_List.get(position).getName().toUpperCase() + "~" + Retailer_Modal_List.get(position).getERP_Code());
                        //common_class.CommonIntentwithFinish(Route_Product_Info.class);
                        common_class.CommonIntentwithoutFinish(Invoice_History.class);
                    }
                }
            }));
        }

    }

    @Override
    public void onLoadTodayOrderList(List<OutletReport_View_Modal> outletReportViewModals) {

    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse) {

    }


    public static class AllDataFragment extends Fragment {
        View mView;
        String tabPosition = "";
        List<Retailer_Modal_List> mRetailer_Modal_ListFilter;
        private Context context;
        private RecyclerView recyclerView;

        public AllDataFragment(List<Retailer_Modal_List> retailer_Modal_ListFilter, int position) {


            this.mRetailer_Modal_ListFilter = retailer_Modal_ListFilter;

            // this.mRetailer_Modal_ListFilter = dashboard_route.Retailer_Modal_ListFilter;
            this.tabPosition = String.valueOf(position);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            return inflater.inflate(R.layout.fragment_tab_outlet, container, false);
        }

        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser) {
                // Refresh your fragment here
                //   getFragmentManager().beginTransaction().detach(this).attach(this).commit();

                // updateData();


            }
        }


        public void updateData() {


            recyclerView.setAdapter(new Route_View_Adapter(mRetailer_Modal_ListFilter, R.layout.route_dashboard_recyclerview, getActivity(), new AdapterOnClick() {
                @Override
                public void onIntentClick(int position) {
                    try {

                        if (dashboard_route.Distributor_Id == null || dashboard_route.Distributor_Id.equalsIgnoreCase("")) {
                            Toast.makeText(getActivity(), "Select The Distributor", Toast.LENGTH_SHORT).show();
                        } else if ((dashboard_route.Route_id == null || dashboard_route.Route_id.equalsIgnoreCase("")) && !dashboard_route.sDeptType.equalsIgnoreCase("2")) {
                            Toast.makeText(getActivity(), "Select The Route", Toast.LENGTH_SHORT).show();
                        } else {
                            Shared_Common_Pref.Outler_AddFlag = "0";
                            Shared_Common_Pref.OutletName = mRetailer_Modal_ListFilter.get(position).getName().toUpperCase();
                            Shared_Common_Pref.OutletCode = mRetailer_Modal_ListFilter.get(position).getId();
                            Shared_Common_Pref.DistributorCode = dashboard_route.Distributor_Id;
                            Shared_Common_Pref.DistributorName = distributor_text.getText().toString();
                            Shared_Common_Pref.Route_Code = dashboard_route.Route_id;
                            // common_class.CommonIntentwithoutFinish(Route_Product_Info.class);
                            shared_common_pref.save(Constants.Retailor_Address, mRetailer_Modal_ListFilter.get(position).getListedDrAddress1());
                            shared_common_pref.save(Constants.Retailor_ERP_Code, mRetailer_Modal_ListFilter.get(position).getERP_Code());
                            shared_common_pref.save(Constants.Retailor_Name_ERP_Code, mRetailer_Modal_ListFilter.get(position).getName().toUpperCase() + "~"
                                    + mRetailer_Modal_ListFilter.get(position).getERP_Code());
                            common_class.CommonIntentwithoutFinish(Invoice_History.class);

                        }
                    } catch (Exception e) {

                    }
                }
            }));


//            notifyAll();

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

    public static class PendingFragment extends Fragment {
        String tabPosition = "";
        List<Retailer_Modal_List> mRetailer_Modal_ListFilter;
        private Context context;
        private RecyclerView recyclerView;

        public PendingFragment(List<Retailer_Modal_List> retailer_Modal_ListFilter, int s) {
            tabPosition = String.valueOf(s);
            this.mRetailer_Modal_ListFilter = retailer_Modal_ListFilter;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_tab_outlet, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            this.context = getContext();
            recyclerView = view.findViewById(R.id.recyclerView);


            //  dashboard_route.OutletFilter("t", "3", false);
            recyclerView.setAdapter(new Route_View_Adapter(mRetailer_Modal_ListFilter, R.layout.route_dashboard_recyclerview, getActivity(), new AdapterOnClick() {
                @Override
                public void onIntentClick(int position) {
                    if (dashboard_route.Distributor_Id == null || dashboard_route.Distributor_Id.equalsIgnoreCase("")) {
                        Toast.makeText(getActivity(), "Select The Distributor", Toast.LENGTH_SHORT).show();
                    } else if ((dashboard_route.Route_id == null || dashboard_route.Route_id.equalsIgnoreCase("")) && !dashboard_route.sDeptType.equalsIgnoreCase("2")) {
                        Toast.makeText(getActivity(), "Select The Route", Toast.LENGTH_SHORT).show();
                    } else {
                        Shared_Common_Pref.Outler_AddFlag = "0";
                        Shared_Common_Pref.OutletName = mRetailer_Modal_ListFilter.get(position).getName().toUpperCase()
                        ;
                        Shared_Common_Pref.OutletCode = mRetailer_Modal_ListFilter.get(position).getId();
                        Shared_Common_Pref.DistributorCode = dashboard_route.Distributor_Id;
                        Shared_Common_Pref.DistributorName = distributor_text.getText().toString();
                        Shared_Common_Pref.Route_Code = dashboard_route.Route_id;
                        //common_class.CommonIntentwithoutFinish(Route_Product_Info.class);
                        shared_common_pref.save(Constants.Retailor_Address, mRetailer_Modal_ListFilter.get(position).getListedDrAddress1());
                        shared_common_pref.save(Constants.Retailor_ERP_Code, mRetailer_Modal_ListFilter.get(position).getERP_Code());
                        shared_common_pref.save(Constants.Retailor_Name_ERP_Code, mRetailer_Modal_ListFilter.get(position).getName().toUpperCase() + "~" +
                                mRetailer_Modal_ListFilter.get(position).getERP_Code());
                        common_class.CommonIntentwithoutFinish(Invoice_History.class);
                    }
                }
            }));

        }
    }

    public static class CompleteFragment extends Fragment {
        String tabPosition = "";
        List<Retailer_Modal_List> mRetailer_Modal_ListFilter;
        private Context context;
        private RecyclerView recyclerView;

        public CompleteFragment(List<Retailer_Modal_List> retailer_Modal_ListFilter, int s) {
            tabPosition = String.valueOf(s);
            this.mRetailer_Modal_ListFilter = retailer_Modal_ListFilter;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_tab_outlet, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            this.context = getContext();
            recyclerView = view.findViewById(R.id.recyclerView);


            recyclerView.setAdapter(new Route_View_Adapter(mRetailer_Modal_ListFilter, R.layout.route_dashboard_recyclerview, getActivity(), new AdapterOnClick() {
                @Override
                public void onIntentClick(int position) {
                    if (dashboard_route.Distributor_Id == null || dashboard_route.Distributor_Id.equalsIgnoreCase("")) {
                        Toast.makeText(getActivity(), "Select The Distributor", Toast.LENGTH_SHORT).show();
                    } else if ((dashboard_route.Route_id == null || dashboard_route.Route_id.equalsIgnoreCase("")) && !dashboard_route.sDeptType.equalsIgnoreCase("2")) {
                        Toast.makeText(getActivity(), "Select The Route", Toast.LENGTH_SHORT).show();
                    } else {
                        Shared_Common_Pref.Outler_AddFlag = "0";
                        Shared_Common_Pref.OutletName = mRetailer_Modal_ListFilter.get(position).getName().toUpperCase();
                        Shared_Common_Pref.OutletCode = mRetailer_Modal_ListFilter.get(position).getId();
                        Shared_Common_Pref.DistributorCode = dashboard_route.Distributor_Id;
                        Shared_Common_Pref.DistributorName = distributor_text.getText().toString();
                        Shared_Common_Pref.Route_Code = dashboard_route.Route_id;
                        //common_class.CommonIntentwithoutFinish(Route_Product_Info.class);
                        shared_common_pref.save(Constants.Retailor_Address, mRetailer_Modal_ListFilter.get(position).getListedDrAddress1());
                        shared_common_pref.save(Constants.Retailor_ERP_Code, mRetailer_Modal_ListFilter.get(position).getERP_Code());
                        shared_common_pref.save(Constants.Retailor_Name_ERP_Code, mRetailer_Modal_ListFilter.get(position).getName().toUpperCase() + "~"
                                + mRetailer_Modal_ListFilter.get(position).getERP_Code());
                        common_class.CommonIntentwithoutFinish(Invoice_History.class);
                    }
                }
            }));

        }
    }

}


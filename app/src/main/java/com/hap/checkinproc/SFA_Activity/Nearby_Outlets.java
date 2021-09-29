package com.hap.checkinproc.SFA_Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Activity_Hap.AddNewRetailer;
import com.hap.checkinproc.Activity_Hap.CustomListViewDialog;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.LocationEvents;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.Model_Class.ModeOfTravel;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.Outlet_Info_Adapter;
import com.hap.checkinproc.SFA_Adapter.RetailerNearByADP;
import com.hap.checkinproc.SFA_Model_Class.Dashboard_View_Model;
import com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List;
import com.hap.checkinproc.adapters.ExploreMapAdapter;
import com.hap.checkinproc.common.DatabaseHandler;
import com.hap.checkinproc.common.LocationFinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Nearby_Outlets extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, OnMapReadyCallback, Master_Interface {
    List<Dashboard_View_Model> approvalList;
    Gson gson;
    private RecyclerView recyclerView, rclRetail;
    Type userType;
    Common_Class common_class;
    TextView Createoutlet, latitude, longitude, availableoutlets, btnNearme, btnExplore;
    List<com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List> Retailer_Modal_List;
    List<com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List> ShowRetailer_Modal_List;
    public static Shared_Common_Pref shared_common_pref;
    SharedPreferences UserDetails, CheckInDetails;
    MapView mapView;
    GoogleMap map;
    Boolean rev = false;
    ArrayList<Marker> mark = new ArrayList<>();
    private HashMap<Marker, Integer> mHashMap = new HashMap<Marker, Integer>();

    StringBuilder sb, place;
    static String googlePlacesData, placeDetail;
    double laty = 0.0, lngy = 0.0;
    JSONArray resData;
    RelativeLayout vwRetails, tabExplore;
    private int _xDelta;
    private int _yDelta, ht;

    String TAG = "Nearby_Outlets",CheckInfo = "CheckInDetail", UserInfo = "MyPrefs";

    ImageView ivFilterKeysMenu, ivNearMe, ivExplore;
    CustomListViewDialog customDialog;

    List<Common_Model> mapKeyList = new ArrayList<>();

    String nextPageToken = "";

    public static Nearby_Outlets nearby_outlets;

    ExploreMapAdapter explore;

    String mapKey;
    DatabaseHandler db;
    LinearLayout llNearMe, llExplore;
    private Polyline mPolyline;
    private String dest_lat;
    private String dest_lng;
    private String dest_name;
    private JSONArray oldData;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_nearby__outlets);
            db = new DatabaseHandler(this);
            nearby_outlets = this;
            shared_common_pref = new Shared_Common_Pref(this);

            CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
            UserDetails = getSharedPreferences(UserInfo, Context.MODE_PRIVATE);

            init();
            setClickListener();


            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(this);
            vwRetails.setOnTouchListener(this);
            RelativeLayout.LayoutParams rel_btn = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, 635);
            rel_btn.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            vwRetails.setLayoutParams(rel_btn);
            Log.d("Height:", String.valueOf(vwRetails.getHeight()));

            common_class = new Common_Class(this);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

            recyclerView.setLayoutManager(linearLayoutManager);
            rclRetail.setLayoutManager(new LinearLayoutManager(this));
            gson = new Gson();
            userType = new TypeToken<ArrayList<Retailer_Modal_List>>() {
            }.getType();
            String OrdersTable = shared_common_pref.getvalue(Constants.Retailer_OutletList);
            //  String OrdersTable = String.valueOf(db.getMasterData(Constants.Retailer_OutletList));
            latitude.setText("Locating Please Wait...");
            new LocationFinder(getApplication(), new LocationEvents() {
                @Override
                public void OnLocationRecived(Location location) {;
                    if(location==null){
                        availableoutlets.setText("Location Not Detacted.");
                        Toast.makeText(Nearby_Outlets.this,"Location Not Detacted. Please Try Again.",Toast.LENGTH_LONG).show();
                        return;
                    }

                    Shared_Common_Pref.Outletlat=location.getLatitude();
                    Shared_Common_Pref.Outletlong=location.getLongitude();
                    latitude.setText("Lat : " + location.getLatitude());
                    longitude.setText("Lng : " + location.getLongitude());
                    JSONObject jsonObject=new JSONObject();
                    try {
                        jsonObject.put("SF",UserDetails.getString("Sfcode",""));
                        jsonObject.put("Div",UserDetails.getString("Divcode",""));
                        jsonObject.put("Lat",location.getLatitude());
                        jsonObject.put("Lng",location.getLongitude());
                        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
                        service.getDataArrayList("get/fencedOutlet",jsonObject.toString()).enqueue(new Callback<JsonArray>() {
                            @Override
                            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                                JsonArray jOutlets=response.body();
                                availableoutlets.setText("Available Outlets :" + "\t" + jOutlets.size());
                                recyclerView.setAdapter(new RetailerNearByADP(jOutlets, R.layout.route_dashboard_recyclerview,
                                         getApplicationContext(), new AdapterOnClick() {
                                    @Override
                                    public void onIntentClick(int position) {
                                     JsonObject jItm=jOutlets.get(position).getAsJsonObject();

                                        Shared_Common_Pref.Outler_AddFlag = "0";
                                        Shared_Common_Pref.OutletName = jItm.get("Name").getAsString().toUpperCase();
                                        Shared_Common_Pref.OutletCode = jItm.get("Code").getAsString();
                                        Shared_Common_Pref.DistributorCode = jItm.get("DistCode").getAsString();
                                        Shared_Common_Pref.DistributorName = jItm.get("Distributor").getAsString();
                                        Shared_Common_Pref.Route_Code = shared_common_pref.getvalue(Constants.Route_Id);
                                        //common_class.CommonIntentwithFinish(Route_Product_Info.class);
                                        shared_common_pref.save(Constants.Retailor_Address, jItm.get("Add2").getAsString());
                                        shared_common_pref.save(Constants.Retailor_ERP_Code, jItm.get("ERP").getAsString());
                                        shared_common_pref.save(Constants.Retailor_Name_ERP_Code, jItm.get("Name").getAsString().toUpperCase() + "~" + jItm.get("ERP").getAsString());
                                        if (jItm.get("Mobile").getAsString().equalsIgnoreCase("") || jItm.get("Owner_Name").getAsString().equalsIgnoreCase(""))
                                            common_class.CommonIntentwithoutFinish(AddNewRetailer.class);
                                        else
                                            common_class.CommonIntentwithoutFinish(Invoice_History.class);
                                    }
                                }));
                            }

                            @Override
                            public void onFailure(Call<JsonArray> call, Throwable t) {
                                Log.d("Fence",t.getMessage());
                            }
                        });





                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    map.getUiSettings().setMyLocationButtonEnabled(false);
                    if (ActivityCompat.checkSelfPermission(Nearby_Outlets.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Nearby_Outlets.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Log.d("LocationError", "Need Location Permission");
                        return;
                    }
                    laty = location.getLatitude();
                    lngy = location.getLongitude();
                    map.setMyLocationEnabled(true);
                    map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(laty, lngy)));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(laty, lngy), 15));
                    getExploreDr(true);
                }
            });

            Createoutlet.setOnClickListener(this);
            ImageView backView = findViewById(R.id.imag_back);

            backView.setOnClickListener(this);


            if (getArrayList(Constants.MAP_KEYLIST) == null || getArrayList(Constants.MAP_KEYLIST).size() == 0) {
                mapKeyList.add(new Common_Model("Shop"));
                shared_common_pref.save(Constants.MAP_KEYLIST, gson.toJson(mapKeyList));
                shared_common_pref.save(Constants.MAP_KEY, "Shop");
            } else {
                mapKeyList = getArrayList(Constants.MAP_KEYLIST);
                mapKey = shared_common_pref.getvalue(Constants.MAP_KEY);
            }


            rclRetail.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);


                    if (!recyclerView.canScrollVertically(1)) {
                        if (common_class.isNetworkAvailable(Nearby_Outlets.this)) {
                            common_class.ProgressdialogShow(1, "");
                            getExploreDr(true);
                        } else {
                            Toast.makeText(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

            ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
            common_class.gotoHomeScreen(this, ivToolbarHome);


        } catch (Exception e) {
            Log.e(TAG, " onCreate: " + e.getMessage());

        }
    }


    private void setClickListener() {
        ivFilterKeysMenu.setOnClickListener(this);
        btnNearme.setOnClickListener(this);

        btnExplore.setOnClickListener(this);

        llNearMe.setOnClickListener(this);
        llExplore.setOnClickListener(this);
    }

    void init() {
        recyclerView = findViewById(R.id.outletrecyclerview);
        rclRetail = findViewById(R.id.rclRetail);

        Createoutlet = findViewById(R.id.Createoutlet);
        availableoutlets = findViewById(R.id.availableoutlets);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);

        vwRetails = findViewById(R.id.vwRetails);
        tabExplore = findViewById(R.id.tabExplore);
        btnNearme = findViewById(R.id.btnNearme);
        btnExplore = findViewById(R.id.btnExplore);
        mapView = (MapView) findViewById(R.id.mapview);
        ivFilterKeysMenu = (findViewById(R.id.ivFilterKeysMenu));

        llNearMe = (LinearLayout) findViewById(R.id.llNearMe);
        llExplore = (LinearLayout) findViewById(R.id.llExplore);
        ivExplore = (ImageView) findViewById(R.id.ivExplore);
        ivNearMe = (ImageView) findViewById(R.id.ivNearMe);

    }

    public ArrayList<Common_Model> getArrayList(String key) {
        Gson gson = new Gson();
        String json = shared_common_pref.getvalue(key);
        Type type = new TypeToken<ArrayList<Common_Model>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Createoutlet:
                Shared_Common_Pref.OutletAvail = "";
                Shared_Common_Pref.OutletUniv = "";
                Shared_Common_Pref.Outler_AddFlag = "1";
                Shared_Common_Pref.Editoutletflag = "0";
                Shared_Common_Pref.OutletAvail = "";
                Shared_Common_Pref.OutletUniv = "";
                Shared_Common_Pref.Outlet_Info_Flag = "0";
                common_class.CommonIntentwithFinish(AddNewRetailer.class);
                //startActivity (Nearby_Outlets.this, AddNewRetailer.class);
                break;
            case R.id.llNearMe:
                llExplore.setBackgroundColor(Color.TRANSPARENT);
                btnExplore.setTextColor(Color.BLACK);
                ivExplore.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);

                llNearMe.setBackgroundResource(R.drawable.blue_bg);
                btnNearme.setTextColor(Color.WHITE);
                ivNearMe.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
                tabExplore.setVisibility(View.GONE);
                ivFilterKeysMenu.setVisibility(View.GONE);
                break;
            case R.id.llExplore:
                llNearMe.setBackgroundColor(Color.TRANSPARENT);
                btnNearme.setTextColor(Color.BLACK);
                ivNearMe.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);

                llExplore.setBackgroundResource(R.drawable.blue_bg);
                btnExplore.setTextColor(Color.WHITE);
                ivExplore.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);

                tabExplore.setVisibility(View.VISIBLE);
                ivFilterKeysMenu.setVisibility(View.VISIBLE);


                break;


            case R.id.btnNearme:
                llExplore.setBackgroundColor(Color.TRANSPARENT);
                btnExplore.setTextColor(Color.BLACK);
                ivExplore.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);

                llNearMe.setBackgroundResource(R.drawable.blue_bg);
                btnNearme.setTextColor(Color.WHITE);
                ivNearMe.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
                tabExplore.setVisibility(View.GONE);
                ivFilterKeysMenu.setVisibility(View.GONE);
                break;
            case R.id.btnExplore:
                llNearMe.setBackgroundColor(Color.TRANSPARENT);
                btnNearme.setTextColor(Color.BLACK);
                ivNearMe.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);

                llExplore.setBackgroundResource(R.drawable.blue_bg);
                btnExplore.setTextColor(Color.WHITE);
                ivExplore.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);

                tabExplore.setVisibility(View.VISIBLE);
                ivFilterKeysMenu.setVisibility(View.VISIBLE);


                break;

            case R.id.ivFilterKeysMenu:
                customDialog = new CustomListViewDialog(Nearby_Outlets.this, mapKeyList, 1000);
                Window windoww = customDialog.getWindow();
                windoww.setGravity(Gravity.CENTER);
                windoww.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
                break;


            case R.id.imag_back:
                finish();
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            map = googleMap;
            map.getUiSettings().setMyLocationButtonEnabled(false);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d("LocationError", "Need Location Permission");
                return;
            }
            laty = Shared_Common_Pref.Outletlat;
            lngy = Shared_Common_Pref.Outletlong;
            map.setMyLocationEnabled(true);
            map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(laty, lngy)));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(laty, lngy), 15));
            getExploreDr(true);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void getExploreDr(boolean boolNextPage) {
        sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        sb.append("location=" + laty + "," + lngy);
        sb.append("&radius=500");
        //  sb.append("&types=cafe|store|shop");
        //sb.append("&keyword=tea shop|juice");

        mapKeyList = getArrayList(Constants.MAP_KEYLIST);

//        String keyVal = "";
//        for (int i = 0; i < mapKeyList.size(); i++) {
//            keyVal = keyVal + mapKeyList.get(i).getName() + "|";
//        }

        // keyVal = "&keyword=" + keyVal;

        sb.append("&keyword=" + shared_common_pref.getvalue(Constants.MAP_KEY));

        sb.append("&key=" + "AIzaSyAER5hPywUW-5DRlyKJZEfsqgZlaqytxoU");
        //https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&
        // keyword=milk|juice&key=AIzaSyAER5hPywUW-5DRlyKJZEfsqgZlaqytxoU


        // https://maps.googleapis.com/maps/api/place/nearbysearch/xml?location=42.9825,-81.254&radius=50000&name=Medical%22Clinic&sensor=false&
        // key=[KEY GOES HERE]&pagetoken=[NEXT PAGE TOKEN GOES HERE]

        //  https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&keyword=milk|juice&key=AIzaSyAER5hPywUW-5DRlyKJZEfsqgZlaqytxoU


        //next page token:Aap_uECXTWAZEAaw417hZSGDM6er_hQeOHMqMn6M3tW_yOBnr1PprfHabmN7Bq-E_z7oyt4B3-vEn9wIvxVV19WJuDMHyyTNhU8SvRGcENx2czJ_GOdWhbrTgJvqDPwZ7RyPxXI8XXc0Kcas6M8C5CjVkYbMYEP7t3uO7AFNBC1nxLIKirdy1OCZe5LNX7IZca-6zG7xcJ1mCXaXKM6rorkao0BWjs9Foibzs38M6fEwPxsEBk0n8747BwVKzzsOq1QnmH3iMncK0uPcT-PbOvNt_KL9sxLTwu6-MXVi9A96HlnwnztvoEZd-DUMien7nxtlp7eveYmNfHXI7nffHFtKDb4QyLOfrsX-WxP3DekLszOEC0S37gMBI317dyTVxLmaEsW9hU-jI7uOxi1M2z7SA9LMTYenI3otwebmJzT27RIYMfgIY1RbssqOcl7RJKrmcrRia9vyucN6b-mFAlvx9dxbSYXyFuHZsouyVujWZyARnYFpKS9dLFxxNr1tPTJV6RvHO_0GAr-WdGDLo1gqGxiCb8gELZ947iik6sbu_Jb80US2Z0DdeCnmDL7NqaX4g0xwB2g7yV4tLVFmgCR9ORQUQpeCZC8r18RepZIJ3ZYqqAq6OtQkJvRERZrOP0TkW2X3nQl9ubc5squAlhCEv1Ou6B16hc85Op7KBcW4QUmHqrCl1xWSz1UJPw-ASim8VYBx09QSBPi30wSCdeAOJzXsN5MKQO1Wg4G4OqgB95Y2gOIy8mNiMsZeDxie3VZ34kiIsDMI0cpauTLJWLzky14FBVKvjq9aM7pB-TcihT1VBgda34RECiDj9p98mDUZ-7_bBfZBc-OEqIg01Vd0OWZZN71WuizykO3u08upH_TI4nHjt6SC6yJK7ZgH6PW33Nd-04YcumLebzyc

        if (nextPageToken.length() > 0 && boolNextPage)
            sb.append("&pagetoken=" + nextPageToken);

        Log.v(TAG + " Doctor_detail_print", sb.toString());

        if (!boolNextPage) {
            oldData = null;
            resData = null;
            explore = null;

            if (marker != null && map != null) {
                for (int i = 0; i < mark.size(); i++) {
                    mark.get(i).remove();
                    //  marker.remove();
                }
            }
        }


        if (common_class.isNetworkAvailable(this))
            new findDrDetail().execute();

    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                _yDelta = Y;
                if (Y < 500) {
                    ht = vwRetails.getHeight();
                    rev = true;
                } else {
                    rev = false;
                }
                Log.d("Y:", String.valueOf(Y) + " _yDelta:" + String.valueOf(_yDelta));
                break;
            case MotionEvent.ACTION_UP:
                Log.d("Height:", String.valueOf(vwRetails.getHeight()) + "=" + String.valueOf(mapView.getHeight()));
                int Hight = 635;
                if (rev == false) {
                    Hight = 635;
                    if (vwRetails.getHeight() > 700) {
                        Hight = mapView.getHeight();
                    }
                }
                if (rev == true) {
                    Hight = mapView.getHeight();
                    if (vwRetails.getHeight() < (mapView.getHeight() - 100)) {
                        Hight = 635;
                    }
                }
                RelativeLayout.LayoutParams vwlist = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, Hight);
                vwlist.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                vwRetails.setLayoutParams(vwlist);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                int incHight = 0;
                if (rev == false) {
                    incHight = (_yDelta - Y) + 635;
                } else {
                    incHight = ht - (Y - _yDelta);
                }
                if (incHight < 0) incHight = 0;
                Log.d("Y:", String.valueOf(Y) + " _yDelta:" + String.valueOf(_yDelta) + "=" + (Y - _yDelta) + " inc:" + String.valueOf(incHight));
                if (incHight > 634) {
                    RelativeLayout.LayoutParams rel_btn = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT, incHight);
                    rel_btn.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    vwRetails.setLayoutParams(rel_btn);
                    Log.d("=>Height:", String.valueOf(vwRetails.getHeight()) + "=" + String.valueOf(mapView.getHeight()));

                }
               /* RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) vwRetails.getLayoutParams();
                layoutParams.topMargin = Y - _yDelta;*/
                // vwRetails.setLayoutParams(layoutParams);
                break;
        }
        //_root.invalidate();
        return true;
    }

    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public void getPlaceIdValues(int position) {
        try {

            JSONObject jsonObject = resData.getJSONObject(position).getJSONObject("geometry");

            JSONObject jsonLatLongObj = jsonObject.getJSONObject("location");

            dest_lat = jsonLatLongObj.getString("lat");
            dest_lng = jsonLatLongObj.getString("lng");
            dest_name = resData.getJSONObject(position).getString("name");


            String dest = jsonLatLongObj.getString("lat") + "," + jsonLatLongObj.getString("lng");

            drawRoute(dest);
            sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");

            try {
                sb.append("place_id=" + resData.getJSONObject(position).getString("place_id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //   sb.append("&fields=name,rating,formatted_phone_number,vicinity,formatted_address");
            // sb.append("&sensor=false&mode=walking&alternatives=true");

            sb.append("&key=AIzaSyAER5hPywUW-5DRlyKJZEfsqgZlaqytxoU");

            shared_common_pref.save(Constants.PLACE_ID_URL, sb.toString());
//
//
//            JSONArray jsonA = resData.getJSONObject(position).getJSONArray("photos");
//
//
//            if (jsonA != null & jsonA.length() > 0) {
//                JSONObject jo = jsonA.getJSONObject(0);
//
//                StringBuilder bu = new StringBuilder("https://maps.googleapis.com/maps/api/place/photo?photoreference=");
//                bu.append(jo.getString("photo_reference"));
//                bu.append("&sensor=false");
//                bu.append("&maxheight=" + jo.getString("height"));
//                bu.append("&maxwidth=" + jo.getString("width"));
//                bu.append("&key=AIzaSyAER5hPywUW-5DRlyKJZEfsqgZlaqytxoU");
//
//                shared_common_pref.save(Constants.SHOP_PHOTO, bu.toString());
//            } else {
//                shared_common_pref.save(Constants.SHOP_PHOTO, "");
//
//            }
        } catch (Exception e) {
            shared_common_pref.save(Constants.PLACE_ID_URL, "");
           // shared_common_pref.save(Constants.SHOP_PHOTO, "");
        }
    }

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {

    }

    class findDrDetail extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            DownloadUrl downloadUrl = new DownloadUrl();
            try {
                googlePlacesData = downloadUrl.readUrl(sb.toString());
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG + " doInBackground: ", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.v("get_dr_detttt", googlePlacesData);
            getDrDetail(googlePlacesData);
        }
    }


    public void getDrDetail(String placesdata) {
        try {


            if (resData != null)
                oldData = resData;


            resData = new JSONArray();


            JSONObject jsonObject = new JSONObject(placesdata);
            resData = jsonObject.getJSONArray("results");
            nextPageToken = jsonObject.optString("next_page_token");
            Log.e(TAG, "nextPageToken:" + nextPageToken);


            for (int i = 0; i < resData.length(); i++) {
                JSONObject json = resData.getJSONObject(i);
                String lat = json.getJSONObject("geometry").getJSONObject("location").getString("lat");
                String lng = json.getJSONObject("geometry").getJSONObject("location").getString("lng");
                String name = json.getString("name");
                String place_id = json.getString("place_id");
                String vicinity = json.getString("vicinity");
                String photoo = "", reference = "", height = "", width = "";

                try {
                    JSONArray jsonA = json.getJSONArray("photos");
                    JSONObject jo = jsonA.getJSONObject(0);
                    JSONArray ja = jo.getJSONArray("html_attributions");
                    //JSONObject jo1=ja.getJSONObject(0);
                    photoo = ja.getString(0);
                    reference = jo.getString("photo_reference");
                    height = jo.getString("height");
                    width = jo.getString("width");
                    Log.v("direction_latt", name + "phototss" + photoo);

                } catch (JSONException e) {
                }


                Log.v("direction_latt", name + "phototss");
                LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
//                marker = map.addMarker(new MarkerOptions().position(latLng)
//                        .title(name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

                marker = map.addMarker(new MarkerOptions().position(latLng)
                        .title((name)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                mark.add(marker);


            }

            drDetail();


        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG + ":getDrDetail: ", e.getMessage());
        }
    }


    JSONArray removeDuplicateItem(JSONArray yourJSONArray) {
        try {
            Set<String> stationCodes = new HashSet<String>();
            JSONArray tempArray = new JSONArray();


            for (int i = 0; i < yourJSONArray.length(); i++) {
                String stationCode = yourJSONArray.getJSONObject(i).getString("name");
                if (stationCodes.contains(stationCode)) {
                    continue;
                } else {
                    stationCodes.add(stationCode);
                    tempArray.put(yourJSONArray.getJSONObject(i));
                }

            }

            Log.e(TAG, " total size:" + yourJSONArray.length() + " FilterSize: " + tempArray.length());

            yourJSONArray = tempArray; //assign temp to original
            return yourJSONArray;

        } catch (Exception e) {

        }
        return null;
    }

    public class DownloadUrl {

        public String readUrl(String strUrl) throws IOException {
            String data = "";
            InputStream iStream = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(strUrl);

                // Creating an http connection to communicate with url
                urlConnection = (HttpURLConnection) url.openConnection();

                // Connecting to url
                urlConnection.connect();

                // Reading data from url
                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb = new StringBuffer("");

                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                data = sb.toString();
                Log.d("downloadUrl", data.toString());
                br.close();

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            } finally {
                iStream.close();
                urlConnection.disconnect();
            }
            return data;
        }

    }


    public void drDetail() {
        try {
            if (explore == null) {
                explore = new ExploreMapAdapter(Nearby_Outlets.this, resData, String.valueOf(Shared_Common_Pref.Outletlat), String.valueOf(Shared_Common_Pref.Outletlong));
                rclRetail.setAdapter(explore);
                explore.notifyDataSetChanged();

            } else {

                if (oldData != null) {
                    for (int i = 0; i < resData.length(); i++) {
                        oldData.put(resData.get(i));

                    }
                }


                resData = oldData;

                resData = removeDuplicateItem(resData);

                explore.notifyData(resData);

            }

            common_class.ProgressdialogShow(0, "");
        } catch (Exception e) {

        }

    }


    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();

    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    //draw route
    private void drawRoute(String mDestination) {


        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(mDestination);

        Intent intent = new Intent(getApplicationContext(), MapDirectionActivity.class);
        intent.putExtra(Constants.MAP_ROUTE, url);
        intent.putExtra(Constants.DEST_LAT, dest_lat);
        intent.putExtra(Constants.DEST_LNG, dest_lng);
        intent.putExtra(Constants.DEST_NAME, dest_name);
        startActivity(intent);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // drDetail();

    }

    private String getDirectionsUrl(String dest) {
        // Origin of route
        String str_origin = "origin=" + Shared_Common_Pref.Outletlat + "," + Shared_Common_Pref.Outletlong;
        // Destination of route
        String str_dest = "destination=" + dest;
        // Key
        String key = "key=" + getString(R.string.map_api_key);
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + key;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/json?" + parameters;
        return url;
    }


    //draw route


}
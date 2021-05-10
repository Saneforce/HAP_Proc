package com.hap.checkinproc.SFA_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Activity_Hap.AddNewRetailer;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.Outlet_Info_Adapter;
import com.hap.checkinproc.SFA_Model_Class.Dashboard_View_Model;
import com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List;
import com.hap.checkinproc.adapters.ExploreMapAdapter;

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
import java.util.List;

public class Nearby_Outlets extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, OnMapReadyCallback {
    List<Dashboard_View_Model> approvalList;
    Gson gson;
    private RecyclerView recyclerView,rclRetail;
    Type userType;
    Common_Class common_class;
    TextView Createoutlet, latitude, longitude, availableoutlets;
    List<com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List> Retailer_Modal_List;
    List<com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List> ShowRetailer_Modal_List;
    Shared_Common_Pref shared_common_pref;
    MapView mapView;
    GoogleMap map;
    Boolean rev=false;
    ArrayList<Marker> mark = new ArrayList<>();
    private HashMap<Marker, Integer> mHashMap = new HashMap<Marker, Integer>();

    StringBuilder sb, place;
    static String googlePlacesData, placeDetail;
    double laty = 0.0, lngy = 0.0;
    JSONArray resData;
    RelativeLayout vwRetails;
    private int _xDelta;
    private int _yDelta,ht;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby__outlets);
        shared_common_pref = new Shared_Common_Pref(this);
        recyclerView = findViewById(R.id.outletrecyclerview);
        rclRetail = findViewById(R.id.rclRetail);

        Createoutlet = findViewById(R.id.Createoutlet);
        availableoutlets = findViewById(R.id.availableoutlets);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);

        vwRetails=findViewById(R.id.vwRetails);
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(this);
        vwRetails.setOnTouchListener(this);
        RelativeLayout.LayoutParams rel_btn = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, 635);
        rel_btn.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        vwRetails.setLayoutParams(rel_btn);
        Log.d("Height:",String.valueOf(vwRetails.getHeight()));

        latitude.setText("Latitude : " + Shared_Common_Pref.Outletlat);
        longitude.setText("Latitude : " + Shared_Common_Pref.Outletlong);
        common_class = new Common_Class(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rclRetail.setLayoutManager(new LinearLayoutManager(this));
        gson = new Gson();
        userType = new TypeToken<ArrayList<Retailer_Modal_List>>() {
        }.getType();
        String OrdersTable = shared_common_pref.getvalue(Shared_Common_Pref.Outlet_List);
        Retailer_Modal_List = gson.fromJson(OrdersTable, userType);
        System.out.println("DISTANCE_CHECKING_Lat" + "---" + Shared_Common_Pref.Outletlat + "----->");
        System.out.println("DISTANCE_CHECKING_Long" + "---" + Shared_Common_Pref.Outletlong + "----->");
        ShowRetailer_Modal_List = new ArrayList<>();
        ShowRetailer_Modal_List.clear();
        for (Retailer_Modal_List rml : Retailer_Modal_List) {
            if (rml.getLat() != null && rml.getLat() != "") {
                System.out.println("DISTANCE_CHECKING" + "---" + rml.getId() + "----->" + String.valueOf(Common_Class.Check_Distance(Common_Class.ParseDouble(rml.getLat()), Common_Class.ParseDouble(rml.getLong()), Shared_Common_Pref.Outletlat, Shared_Common_Pref.Outletlong)));
                if (Common_Class.Check_Distance(Common_Class.ParseDouble(rml.getLat()), Common_Class.ParseDouble(rml.getLong()), Shared_Common_Pref.Outletlat, Shared_Common_Pref.Outletlong) < 0.5) {
                    ShowRetailer_Modal_List.add(rml);
                }
            }
        }
        availableoutlets.setText("Available Outlets:" + "\t" + ShowRetailer_Modal_List.size());
        if (ShowRetailer_Modal_List != null && ShowRetailer_Modal_List.size() > 0) {
            recyclerView.setAdapter(new Outlet_Info_Adapter(ShowRetailer_Modal_List, R.layout.outlet_info_recyclerview, getApplicationContext(), new AdapterOnClick() {
                @Override
                public void onIntentClick(int position) {
                    Shared_Common_Pref.Outler_AddFlag = "0";
                    Shared_Common_Pref.OutletName = ShowRetailer_Modal_List.get(position).getName().toUpperCase();
                    Shared_Common_Pref.OutletCode = ShowRetailer_Modal_List.get(position).getId();
                    common_class.CommonIntentwithFinish(Route_Product_Info.class);
                    common_class.CommonIntentwithoutFinish(Route_Product_Info.class);
                }
            }));
        }
        Createoutlet.setOnClickListener(this);
        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
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
                common_class.CommonIntentwithoutFinish(Route_Product_Info.class);
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("LocationError","Need Location Permission");
            return;
        }
        laty=Shared_Common_Pref.Outletlat;
        lngy=Shared_Common_Pref.Outletlong;
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(laty, lngy)));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(laty, lngy), 15));
        getExploreDr();
    }

    public void getExploreDr(){
        sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        sb.append("location="+laty+","+lngy);
        sb.append("&radius=500");
        sb.append("&keyword=milk");
        sb.append("&key="+"AIzaSyAER5hPywUW-5DRlyKJZEfsqgZlaqytxoU");
        Log.v("Doctor_detail_print",sb.toString());
        //https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&keyword=milk|juice&key=AIzaSyAER5hPywUW-5DRlyKJZEfsqgZlaqytxoU
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
                if(Y<500) {ht=vwRetails.getHeight();rev=true;} else {rev=false;}
                Log.d("Y:",String.valueOf(Y)+" _yDelta:"+String.valueOf(_yDelta));
                break;
            case MotionEvent.ACTION_UP:
                Log.d("Height:",String.valueOf(vwRetails.getHeight())+"="+String.valueOf(mapView.getHeight()));
                int Hight=635;
                if( rev==false){
                    Hight=635;
                    if(vwRetails.getHeight()>700){
                        Hight=mapView.getHeight();
                    }
                }
                if( rev==true){
                    Hight=mapView.getHeight();
                    if(vwRetails.getHeight()<(mapView.getHeight()-100)){
                        Hight=635;
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
                int incHight=0;
                if(rev==false){incHight=(_yDelta-Y)+635;}else{
                    incHight=ht-(Y-_yDelta);
                }
                if(incHight<0) incHight=0;
                Log.d("Y:",String.valueOf(Y)+" _yDelta:"+String.valueOf(_yDelta)+"="+(Y-_yDelta)+" inc:"+String.valueOf(incHight));
                if(incHight>634){
                    RelativeLayout.LayoutParams rel_btn = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, incHight);
                    rel_btn.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    vwRetails.setLayoutParams(rel_btn);
                    Log.d("=>Height:",String.valueOf(vwRetails.getHeight())+"="+String.valueOf(mapView.getHeight()));

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
    class findDrDetail extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            DownloadUrl downloadUrl = new DownloadUrl();
            try {
                googlePlacesData = downloadUrl.readUrl(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.v("get_dr_detttt",googlePlacesData);
            getDrDetail(googlePlacesData);
        }
    }

    public void getDrDetail(String placesdata){
        try {
            JSONObject jsonObject=new JSONObject(placesdata);
            resData=jsonObject.getJSONArray("results");

            for(int i=0;i<resData.length();i++){
                JSONObject json=resData.getJSONObject(i);
                String lat=json.getJSONObject("geometry").getJSONObject("location").getString("lat");
                String lng=json.getJSONObject("geometry").getJSONObject("location").getString("lng");
                String name=json.getString("name");
                String place_id=json.getString("place_id");
                String vicinity=json.getString("vicinity");
                String photoo="",reference="",height="",width="";
                try{
                    JSONArray jsonA=json.getJSONArray("photos");
                    JSONObject jo=jsonA.getJSONObject(0);
                    JSONArray ja=jo.getJSONArray("html_attributions");
                    //JSONObject jo1=ja.getJSONObject(0);
                    photoo=ja.getString(0);
                    reference=jo.getString("photo_reference");
                    height=jo.getString("height");
                    width=jo.getString("width");
                    Log.v("direction_latt",name+"phototss"+photoo);

                }catch (JSONException e){}


                Log.v("direction_latt",name+"phototss");
                LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                Marker marker = map.addMarker(new MarkerOptions().position(latLng)
                        .title(name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                mark.add(marker);
                mHashMap.put(marker, i);

            }

            drDetail();


        } catch (JSONException e) {
            e.printStackTrace();
        }
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


    public void drDetail(){
        ExploreMapAdapter explore = new ExploreMapAdapter(Nearby_Outlets.this, resData, String.valueOf(Shared_Common_Pref.Outletlat), String.valueOf(Shared_Common_Pref.Outletlong));
        rclRetail.setAdapter(explore);
        explore.notifyDataSetChanged();

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


}
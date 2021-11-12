package com.hap.checkinproc.SFA_Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.LocationEvents;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.Interface.UpdateResponseUI;
import com.hap.checkinproc.R;
import com.hap.checkinproc.common.LocationFinder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyTeamActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, OnMapReadyCallback, UpdateResponseUI, Master_Interface {


    Common_Class common_class;
    TextView Createoutlet, latitude, longitude, availableoutlets, cAddress;

    public static Shared_Common_Pref shared_common_pref;
    SharedPreferences UserDetails, CheckInDetails;
    MapView mapView;
    GoogleMap map;
    Boolean rev = false;
    ArrayList<Marker> mark = new ArrayList<>();


    double laty = 0.0, lngy = 0.0;

    RelativeLayout vwRetails;

    private int _yDelta, ht;

    String TAG = "MyTeamActivity", CheckInfo = "CheckInDetail", UserInfo = "MyPrefs";
    public static MyTeamActivity nearby_outlets;

    private Marker marker;
    LinearLayout llZSM, llRSM, llSDM, llSDE, llALL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_myteam_layout);

            nearby_outlets = this;
            shared_common_pref = new Shared_Common_Pref(this);

            CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
            UserDetails = getSharedPreferences(UserInfo, Context.MODE_PRIVATE);

            init();

            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(this);
            vwRetails.setOnTouchListener(this);
            RelativeLayout.LayoutParams rel_btn = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, 635);
            rel_btn.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            vwRetails.setLayoutParams(rel_btn);
            Log.d("Height:", String.valueOf(vwRetails.getHeight()));

            common_class = new Common_Class(this);

            latitude.setText("Locating Please Wait...");
            new LocationFinder(getApplication(), new LocationEvents() {
                @Override
                public void OnLocationRecived(Location location) {

                    if (location == null) {
                        availableoutlets.setText("Location Not Detacted.");
                        Toast.makeText(MyTeamActivity.this, "Location Not Detacted. Please Try Again.", Toast.LENGTH_LONG).show();
                        return;
                    } else {

                        showNearbyData(location);
                    }
                }
            });

            Createoutlet.setOnClickListener(this);
            ImageView backView = findViewById(R.id.imag_back);

            backView.setOnClickListener(this);


            ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
            common_class.gotoHomeScreen(this, ivToolbarHome);


        } catch (Exception e) {
            Log.e(TAG, " onCreate: " + e.getMessage());

        }
    }

    void showNearbyData(Location location) {
        Shared_Common_Pref.Outletlat = location.getLatitude();
        Shared_Common_Pref.Outletlong = location.getLongitude();
        latitude.setText("Lat : " + location.getLatitude());
        longitude.setText("Lng : " + location.getLongitude());
        getCompleteAddressString(location.getLatitude(), location.getLongitude());
        map.getUiSettings().setMyLocationButtonEnabled(false);
        if (ActivityCompat.checkSelfPermission(MyTeamActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MyTeamActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("LocationError", "Need Location Permission");
            return;
        }
        laty = location.getLatitude();
        lngy = location.getLongitude();
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(laty, lngy)));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(laty, lngy), 15));

        getTeamLoc("ALL");
    }

    void getTeamLoc(String type) {
        JsonObject data = new JsonObject();
        data.addProperty("sfcode", Shared_Common_Pref.Sf_Code);
        data.addProperty("date", "2021-11-10");
        data.addProperty("type", type);
        common_class.getDb_310Data(Constants.MYTEAM_LOCATION, this, data);

    }


    void init() {
        Createoutlet = findViewById(R.id.Createoutlet);
        availableoutlets = findViewById(R.id.availableoutlets);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        cAddress = findViewById(R.id.cAddress);

        vwRetails = findViewById(R.id.vwRetails);

        mapView = (MapView) findViewById(R.id.mapview);

        llZSM = findViewById(R.id.llZSM);
        llRSM = findViewById(R.id.llRSM);
        llSDM = findViewById(R.id.llSDM);
        llSDE = findViewById(R.id.llSDE);
        llALL = findViewById(R.id.llAll);

        llZSM.setOnClickListener(this);
        llRSM.setOnClickListener(this);
        llSDM.setOnClickListener(this);
        llSDE.setOnClickListener(this);
        llALL.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imag_back:
                finish();
                break;
            case R.id.llZSM:
                getTeamLoc("ZSM");
                break;
            case R.id.llRSM:
                getTeamLoc("RSM");
                break;
            case R.id.llSDM:
                getTeamLoc("SDM");
                break;
            case R.id.llSDE:
                getTeamLoc("SDE");
                break;
            case R.id.llAll:
                getTeamLoc("ALL");
                break;
        }
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();
                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                cAddress.setText(strReturnedAddress.toString());
                strAdd = strReturnedAddress.toString();
                //Log.w("My Current loction address", strReturnedAddress.toString());
            } else {
                // Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //  Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
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

            JsonObject data = new JsonObject();
            data.addProperty("sfcode", Shared_Common_Pref.Sf_Code);
            data.addProperty("date", "2021-11-10");
            data.addProperty("type", "ALL");
            common_class.getDb_310Data(Constants.MYTEAM_LOCATION, this, data);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
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


    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {

    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {
            if (apiDataResponse != null) {
                switch (key) {
                    case Constants.MYTEAM_LOCATION:
                        JSONObject jsonObject = new JSONObject(apiDataResponse);
                        if (jsonObject.getBoolean("success")) {

                            JSONArray arr = jsonObject.getJSONArray("Data");

                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject arrObj = arr.getJSONObject(i);
                                LatLng latLng = new LatLng(Double.parseDouble(arrObj.getString("Lat")),
                                        Double.parseDouble(arrObj.getString("Lon")));

                                marker = map.addMarker(new MarkerOptions().position(latLng)
                                        .title((arrObj.getString("Sf_Name"))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                                mark.add(marker);
                            }
                        }


                        break;
                }
            }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            new LocationFinder(this, new LocationEvents() {
                @Override
                public void OnLocationRecived(Location location) {
                    try {
                        showNearbyData(location);
                    } catch (Exception e) {
                    }
                }
            });
        } catch (Exception e) {

        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

}
package com.hap.checkinproc.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.hap.checkinproc.Activity.Util.SelectionModel;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewTASummary extends AppCompatActivity implements
        OnMapReadyCallback {
    ListView list_exp;
    ApiInterface apiInterface;
    ArrayList<SelectionModel> array = new ArrayList<>();
    TextView txt_start, txt_end, txt_start_km, txt_end_km, txt_tot;
    SharedPreferences UserDetails;
    public static final String MyPREFERENCES = "MyPrefs";
    String SF_code = "", div = "", State_Code = "";
    private static final int COLOR_ORANGE_ARGB = 0xffF57F17;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_t_a_summary);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.route_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        list_exp = findViewById(R.id.list_exp);
        txt_start = findViewById(R.id.txt_start);
        txt_end = findViewById(R.id.txt_end);
        txt_start_km = findViewById(R.id.txt_start_km);
        txt_end_km = findViewById(R.id.txt_end_km);
        txt_tot = findViewById(R.id.txt_tot);
        UserDetails = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SF_code = UserDetails.getString("Sfcode", "");
        div = UserDetails.getString("div", "");
        State_Code = UserDetails.getString("State_Code", "");
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        callApi("");
    }

    public void callApi(String date) {
        try {
            JSONObject jj = new JSONObject();
            jj.put("Ta_Date", "2020-11-13");
            jj.put("div", div);
            jj.put("sf", SF_code);
            jj.put("rSF", SF_code);
            jj.put("State_Code", State_Code);
            Log.v("json_obj_ta", jj.toString());
            Call<ResponseBody> Callto = apiInterface.getAllowance(jj.toString());
            Callto.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.isSuccessful()) {


                            Log.v("print_upload_file_true", "ggg" + response);
                            JSONObject jb = null;
                            String jsonData = null;
                            jsonData = response.body().string();
                            Log.v("response_data", jsonData);
                            JSONObject json = new JSONObject(jsonData);
                            JSONArray ja = json.getJSONArray("User_Expense");
                            int x = 0;
                            for (int i = 0; i < ja.length(); i++) {
                                JSONObject json1 = ja.getJSONObject(i);
                                array.add(new SelectionModel(json1.getString("expName"), json1.getString("Amt")));
                                x = x + Integer.parseInt(json1.getString("Amt"));

                            }
                            AdapterForViewTA adpt = new AdapterForViewTA(ViewTASummary.this, array);
                            list_exp.setAdapter(adpt);
                            JSONArray ja1 = json.getJSONArray("TAStartandEnd");
                            JSONObject jjj = ja1.getJSONObject(0);
                            txt_start.setText("Start Mode : " + jjj.getString("Start_MOT"));
                            txt_end.setText("End Mode : " + jjj.getString("End_MOT"));
                            txt_end_km.setText("End KM : " + jjj.getString("End_Km"));
                            txt_start_km.setText("Start KM : " + jjj.getString("Start_Km"));
                            txt_tot.setText(String.valueOf(x));
                        }
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        } catch (Exception e) {
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng sydney = new LatLng(13.1148, 80.2872);
        LatLng sydney2 = new LatLng(13.0300, 80.2421);

  /*      BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.marker_icon);
        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Old washermenpet"));
        googleMap.addMarker(new MarkerOptions().position(sydney2).title("Marker in Nandanam"));
       *//* googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*//*
         */
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney2, 15));
        // Zoom in, animating the camera.
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(9), 2000, null);


        Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(13.1148, 80.2872),
                        new LatLng(13.0300, 80.2421)));

        polyline1.setTag("A");
        polyline1.setColor(COLOR_ORANGE_ARGB);
    }

    public class AdapterForViewTA extends BaseAdapter {
        Context context;
        ArrayList<SelectionModel> arr = new ArrayList<>();

        public AdapterForViewTA(Context context, ArrayList<SelectionModel> arr) {
            this.context = context;
            this.arr = arr;
        }

        @Override
        public int getCount() {
            return arr.size();
        }

        @Override
        public Object getItem(int i) {
            return arr.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(context).inflate(R.layout.row_item_ta_view, viewGroup, false);
            TextView txt_start = view.findViewById(R.id.txt_start);
            TextView txt_amt = view.findViewById(R.id.txt_amt);
            txt_start.setText(arr.get(i).getTxt());
            txt_amt.setText(arr.get(i).getCode());
            return view;
        }
    }

}
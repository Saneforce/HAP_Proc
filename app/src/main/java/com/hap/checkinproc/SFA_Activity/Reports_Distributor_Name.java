package com.hap.checkinproc.SFA_Activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.LocationEvents;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.DistributerListAdapter;
import com.hap.checkinproc.common.LocationFinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Reports_Distributor_Name extends AppCompatActivity {
    Gson gson;
    private RecyclerView recyclerView;
    Common_Class common_class;
    Shared_Common_Pref shared_common_pref;
    double ACBalance = 0.0;

    EditText etSearch;
    public static Reports_Distributor_Name reports_distributor_name;
    ProgressBar pb;
    private int dist_id, dist_pos, refreshFlag;
    private JSONArray loc_Arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_dist_name);
            shared_common_pref = new Shared_Common_Pref(this);
            recyclerView = findViewById(R.id.rvDistList);
            etSearch = findViewById(R.id.etSearchDist);
            pb = findViewById(R.id.progressbar);
            reports_distributor_name = this;

            common_class = new Common_Class(this);
            gson = new Gson();
            ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
            common_class.gotoHomeScreen(this, ivToolbarHome);
            JSONArray arr = new JSONArray(shared_common_pref.getvalue(Constants.Distributor_List));
            setAdapter(arr);

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
                        setAdapter(filterArr);

                    } catch (Exception e) {

                    }
                }
            });

        } catch (Exception e) {

        }

    }

    void setAdapter(JSONArray arr) {
        recyclerView.setAdapter(new DistributerListAdapter(arr, R.layout.dist_info_recyclerview, this));

    }

    public void updateDistlatLng(int id, int pos, JSONArray arr, int flag) {
        try {
            dist_id = id;
            loc_Arr = arr;
            dist_pos = pos;
            refreshFlag = flag;

            new LocationFinder(this, new LocationEvents() {
                @Override
                public void OnLocationRecived(Location location) {
                    try {
                        if (location != null) {
                            locUpdate(id, location, loc_Arr, refreshFlag);
                        }
                    } catch (Exception e) {

                    }
                }
            });
        } catch (Exception e) {

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            new LocationFinder(this, new LocationEvents() {
                @Override
                public void OnLocationRecived(Location location) {
                    try {
                        locUpdate(dist_id, location, loc_Arr, refreshFlag);
                    } catch (Exception e) {
                    }
                }
            });
        } catch (Exception e) {

        }
    }

    void locUpdate(int id, Location location, JSONArray distArr, int flag) {
        JSONObject jsonobj = new JSONObject();

        try {
            jsonobj.put("sfCode", shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code));
            jsonobj.put("distributor_Id", id);
            jsonobj.put("lat", (String.valueOf(location.getLatitude())));
            jsonobj.put("lng", (String.valueOf(location.getLongitude())));
            jsonobj.put("current_date", (Common_Class.GetDate()));
            jsonobj.put("flag", flag);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.v("TA_REQ", jsonobj.toString());
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> mCall = apiInterface.distLatLngUpdate(jsonobj.toString());

        mCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // locationList=response.body();
                Log.e("TAG_TP_RESPONSE", "response Tp_View: " + new Gson().toJson(response.body()));
                try {

                    JSONObject obj = new JSONObject(response.body().toString());

                    if (obj.getBoolean("success")) {
                        //  JSONArray arr = new JSONArray(shared_common_pref.getvalue(Constants.Distributor_List));


                        for (int i = 0; i < loc_Arr.length(); i++) {
                            JSONObject loc_obj = loc_Arr.getJSONObject(i);
                            if (dist_id == loc_obj.getInt("id")) {

                                loc_obj.put("Latlong", refreshFlag == 1 ? location.getLatitude() + ":" + location.getLongitude() : "");
                                loc_obj.put("locUpdatedTime", Common_Class.GetDatemonthyearTimeformat());

                                break;
                            }
                        }

                        JSONArray distArr = new JSONArray(shared_common_pref.getvalue(Constants.Distributor_List));

                        for (int d = 0; d < distArr.length(); d++) {

                            if (dist_id ==
                                    distArr.getJSONObject(d).getInt("id")) {
                                distArr.put(d, loc_Arr.getJSONObject(dist_pos));
                                shared_common_pref.save(Constants.Distributor_List, distArr.toString());
                                break;
                            }
                        }

                        setAdapter(loc_Arr);


                    }

                    common_class.showMsg(Reports_Distributor_Name.this, obj.getString("Msg"));


                } catch (Exception e) {
                    Log.v("locUpdate:", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

//    private void distLocUpdate(int id, Location location) {
//        pb.setVisibility(View.VISIBLE);
//        JSONArray jsonarr = new JSONArray();
//        JSONObject jsonarrplan = new JSONObject();
//        try {
//            JSONObject jsonobj = new JSONObject();
//            jsonobj.put("Distributor_Id", id);
//            jsonobj.put("Latitude", addquote(String.valueOf(location.getLatitude())));
//            jsonobj.put("Longitude", addquote(String.valueOf(location.getLongitude())));
//            jsonobj.put("Created_Date", addquote(Common_Class.GetDate()));
//            jsonarrplan.put("saveDistiLatLong", jsonobj);
//            jsonarr.put(jsonarrplan);
//            Log.d("Distributor_QS", jsonarr.toString());
//            Map<String, String> QueryString = new HashMap<>();
//            QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
//            QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
//            QueryString.put("State_Code", Shared_Common_Pref.StateCode);
//            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
//            Call<Object> Callto = apiInterface.updateDistLatLng(QueryString, jsonarr.toString());
//            Callto.enqueue(new Callback<Object>() {
//                @Override
//                public void onResponse(Call<Object> call, Response<Object> response) {
//                    try {
//                        JSONObject obj = new JSONObject(response.body().toString());
//                        pb.setVisibility(View.GONE);
//                        if (obj.getBoolean("success")) {
//                            common_class.showMsg(Reports_Distributor_Name.this, "Latitude and Longitude Updated Successfully");
//                        }
//                    } catch (Exception e) {
//
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Object> call, Throwable t) {
//                    pb.setVisibility(View.GONE);
//                    Log.e("Reponse TAG", "onFailure : " + t.toString());
//                }
//            });
//
//        } catch (Exception e) {
//            pb.setVisibility(View.GONE);
//            e.printStackTrace();
//        }
//    }


}
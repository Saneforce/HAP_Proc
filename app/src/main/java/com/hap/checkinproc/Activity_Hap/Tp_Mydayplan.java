package com.hap.checkinproc.Activity_Hap;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.MVP.Main_Model;
import com.hap.checkinproc.MVP.MasterSync_Implementations;
import com.hap.checkinproc.MVP.Master_Sync_View;
import com.hap.checkinproc.Model_Class.Distributor_Master;
import com.hap.checkinproc.Model_Class.Route_Master;
import com.hap.checkinproc.Model_Class.Work_Type_Model;
import com.hap.checkinproc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.hap.checkinproc.Common_Class.Common_Class.addquote;

public class Tp_Mydayplan extends AppCompatActivity implements Main_Model.MasterSyncView, View.OnClickListener {
    Spinner worktypespinner, worktypedistributor, worktyperoute;
    List<Work_Type_Model> worktypelist;
    List<Route_Master> Route_Master;
    List<Distributor_Master> distributor_master;
    private Main_Model.presenter presenter;
    Gson gson;
    Type userType;
    EditText edt_remarks;
    Shared_Common_Pref shared_common_pref;
    Common_Class common_class;
    String worktype_id, worktypeflag, distributorname, distributorid, routename, routeid, worktype_name;
    private TextClock tClock;
    Button submitbutton;
    ProgressBar progressbar;
    TextView tourdate;
    ImageView backarow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tp__mydayplan);
        progressbar = findViewById(R.id.progressbar);
        shared_common_pref = new Shared_Common_Pref(this);
        common_class = new Common_Class(this);
        edt_remarks = findViewById(R.id.edt_remarks);
        backarow = findViewById(R.id.backarow);
        backarow.setOnClickListener(this);
        tourdate = findViewById(R.id.tourdate);
        gson = new Gson();
        Log.e("TOuR_PLAN_DATE", common_class.getintentValues("TourDate"));
        tourdate.setText(common_class.getintentValues("TourDate"));
        worktypespinner = findViewById(R.id.worktypespinner);
        worktypedistributor = findViewById(R.id.worktypedistributor);
        worktyperoute = findViewById(R.id.worktyperoute);
        submitbutton = findViewById(R.id.submitbutton);
        presenter = new MasterSync_Implementations(this, new Master_Sync_View());
        presenter.requestDataFromServer();
        submitbutton.setOnClickListener(this);

    }

    public void loaddistriSpinner() {
        ArrayList<String> distributor = new ArrayList<>();
        for (int i = 0; i < distributor_master.size(); i++) {

            distributor.add(distributor_master.get(i).getName());

        }

        worktypedistributor.setAdapter(new ArrayAdapter<>(Tp_Mydayplan.this, android.R.layout.simple_spinner_dropdown_item, distributor));
        worktypedistributor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                distributorname = distributor_master.get(position).getName();
                distributorid = String.valueOf(distributor_master.get(position).getDisCatCode());
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void loadroute() {
        ArrayList<String> route = new ArrayList<>();
        for (int i = 0; i < Route_Master.size(); i++) {

            route.add(Route_Master.get(i).getName());

        }

        worktyperoute.setAdapter(new ArrayAdapter<>(Tp_Mydayplan.this, android.R.layout.simple_spinner_dropdown_item, route));
        worktyperoute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                routename = Route_Master.get(position).getName();
                routeid = Route_Master.get(position).getId();
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void loadWorktypeSpinner() {

        Log.e("WOrktype_Length", String.valueOf(worktypelist.size()));
        ArrayList<String> worktype = new ArrayList<>();


        for (int i = 0; i < worktypelist.size(); i++) {
            worktype.add(worktypelist.get(i).getName());
        }
        worktypespinner.setAdapter(new ArrayAdapter<>(Tp_Mydayplan.this, android.R.layout.simple_spinner_dropdown_item, worktype));
        worktypespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                worktype_id = String.valueOf(worktypelist.get(position).getId());
                worktypeflag = worktypelist.get(position).getFWFlg();
                worktype_name = worktypelist.get(position).getName();
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
    public void setDataToRouteObject(Object noticeArrayList, int position) {
        Log.e("Calling Position", String.valueOf(position));

        //Toast.makeText(this, "Position" + position, Toast.LENGTH_SHORT).show();
        Log.e("ROUTE_MASTER_Object", String.valueOf(noticeArrayList));
        Log.e("TAG", "response Tbmydayplan: " + new Gson().toJson(noticeArrayList));
       /* Type userType = new TypeToken<ArrayList<Route_Master>>(){}.getType();
        List<Route_Master> userList = gson.fromJson(noticeArrayList.toString(), userType);
        userList.size();

        Log.e("ROUTE_MASTER_Size", String.valueOf(userList.size()));*/

        if (position == 0) {

            Log.e("SharedprefrenceVALUES", new Gson().toJson(noticeArrayList));
            userType = new TypeToken<ArrayList<Work_Type_Model>>() {
            }.getType();
            worktypelist = gson.fromJson(new Gson().toJson(noticeArrayList), userType);

            shared_common_pref.save("masterWorktype", new Gson().toJson(noticeArrayList));
            loadWorktypeSpinner();

        } else if (position == 1) {
            userType = new TypeToken<ArrayList<Distributor_Master>>() {
            }.getType();
            distributor_master = gson.fromJson(new Gson().toJson(noticeArrayList), userType);
            shared_common_pref.save("masterdistributor", new Gson().toJson(noticeArrayList));
            loaddistriSpinner();
        } else {
            userType = new TypeToken<ArrayList<Route_Master>>() {
            }.getType();
            Route_Master = gson.fromJson(new Gson().toJson(noticeArrayList), userType);

            shared_common_pref.save("masterroute", new Gson().toJson(noticeArrayList));

            loadroute();
        }

    }


    @Override
    public void onResponseFailure(Throwable throwable) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitbutton:
                if (vali()) {
                    progressbar.setVisibility(View.VISIBLE);
                    Calendar c = Calendar.getInstance();
                    String Dcr_Dste = new SimpleDateFormat("HH:mm a", Locale.ENGLISH).format(new Date());
                    JSONArray jsonarr = new JSONArray();
                    JSONObject jsonarrplan = new JSONObject();

                    String remarks = edt_remarks.getText().toString();
                    try {
                        JSONObject jsonobj = new JSONObject();
                        jsonobj.put("worktype_code", addquote(worktype_id));
                        jsonobj.put("worktype_name", addquote(worktype_name));
                        jsonobj.put("sfName", "'testonw'");
                        jsonobj.put("RouteCode", addquote(routeid));
                        jsonobj.put("objective", addquote(remarks));
                        jsonobj.put("RouteName", addquote(routename));
                        jsonobj.put("Tour_Date", addquote(tourdate.getText().toString()));
                        jsonobj.put("Worked_with_Code", addquote(distributorid));
                        jsonobj.put("Worked_with_Name", addquote(distributorname));
                        jsonobj.put("Multiretailername", "''");
                        jsonobj.put("MultiretailerCode", "''");
                        jsonobj.put("worked_with", "''");
                        jsonobj.put("jointWorkCode", "''");
                        jsonobj.put("HQ_Code", "''");
                        jsonobj.put("HQ_Name", "''");
                        jsonarrplan.put("Tour_Plan", jsonobj);
                        jsonarr.put(jsonarrplan);
                        Log.d("Mydayplan_Object", jsonarr.toString());

                        Map<String, String> QueryString = new HashMap<>();

                        QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
                        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
                        QueryString.put("State_Code", Shared_Common_Pref.StateCode);
                        QueryString.put("desig", "MGR");
                        Log.d("QueryString", String.valueOf(QueryString));
                        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                        Call<Object> Callto = apiInterface.Tb_Mydayplan(QueryString, jsonarr.toString());
                        Callto.enqueue(new Callback<Object>() {
                            @Override
                            public void onResponse(Call<Object> call, Response<Object> response) {
                                Log.e("RESPONSE_FROM_SERVER", response.body().toString());
                                progressbar.setVisibility(View.GONE);
                                if (response.code() == 200 || response.code() == 201) {
                                    common_class.CommonIntentwithFinish(Dashboard.class);
                                    Toast.makeText(Tp_Mydayplan.this, "Tour Plan Submitted Successfully", Toast.LENGTH_SHORT).show();
                                }


                            }

                            @Override
                            public void onFailure(Call<Object> call, Throwable t) {
                                progressbar.setVisibility(View.GONE);
                                Log.e("Reponse TAG", "onFailure : " + t.toString());
                            }
                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            case R.id.backarow:
                common_class.CommonIntentwithFinish(Tp_Month_Select.class);
                break;
        }
    }

    public boolean vali() {
        if (Common_Class.isNullOrEmpty(worktype_id)) {
            Toast.makeText(this, "Select The Wrktype", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (Common_Class.isNullOrEmpty(distributorid)) {
            Toast.makeText(this, "Select The Distributor", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Common_Class.isNullOrEmpty(routeid)) {
            Toast.makeText(this, "Select The Route", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}

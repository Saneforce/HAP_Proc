package com.hap.checkinproc.Activity_Hap;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Activity.Util.UpdateUi;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.MVP.Main_Model;
import com.hap.checkinproc.MVP.MasterSync_Implementations;
import com.hap.checkinproc.MVP.Master_Sync_View;
import com.hap.checkinproc.Model_Class.Distributor_Master;
import com.hap.checkinproc.Model_Class.Route_Master;
import com.hap.checkinproc.Model_Class.Work_Type_Model;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.DataAdapter;

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

public class Mydayplan_Activity extends AppCompatActivity implements Main_Model.MasterSyncView, View.OnClickListener, Master_Interface {
    Spinner worktypespinner, worktypedistributor, worktyperoute;
    List<Work_Type_Model> worktypelist;
    List<Route_Master> Route_Master;

    LinearLayout worktypelayout;
    List<Distributor_Master> distributor_master;
    private Main_Model.presenter presenter;
    Gson gson;
    Type userType;
    EditText edt_remarks;
    Shared_Common_Pref shared_common_pref;
    Common_Class common_class;
    String worktype_id, worktypeflag, distributorname, distributorid, routename, routeid;
    private TextClock tClock;
    Button submitbutton;
    CustomListViewDialog customDialog;
    ImageView backarow;
    ProgressBar progressbar;
    TextView worktype_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mydayplan_);
        progressbar = findViewById(R.id.progressbar);
        shared_common_pref = new Shared_Common_Pref(this);
        common_class = new Common_Class(this);
        edt_remarks = findViewById(R.id.edt_remarks);
        backarow = findViewById(R.id.backarow);
        gson = new Gson();
        //worktypespinner = findViewById(R.id.worktypespinner);
        worktypelayout = findViewById(R.id.worktypelayout);
        worktypedistributor = findViewById(R.id.worktypedistributor);
        worktyperoute = findViewById(R.id.worktyperoute);
        submitbutton = findViewById(R.id.submitbutton);
        worktype_text = findViewById(R.id.worktype_text);
        presenter = new MasterSync_Implementations(this, new Master_Sync_View());
        presenter.requestDataFromServer();
        backarow.setOnClickListener(this);
        submitbutton.setOnClickListener(this);
        worktypelayout.setOnClickListener(this);
        common_class.ProgressdialogShow(1, "Day plan");

        /*if(shared_common_pref.getvalue("masterWorktype")!=null){
            Log.e("SHAREDPREF_Length", String.valueOf(shared_common_pref.getvalue("masterdistributor")));

            userType = new TypeToken<ArrayList<Work_Type_Model>>() {
            }.getType();
            worktypelist = gson.fromJson(shared_common_pref.getvalue("masterWorktype"), userType);
            userType = new TypeToken<ArrayList<Distributor_Master>>() {
            }.getType();
            distributor_master = gson.fromJson(new Gson().toJson(shared_common_pref.getvalue("masterdistributor")), userType);
            userType = new TypeToken<ArrayList<Route_Master>>() {
            }.getType();
            Route_Master = gson.fromJson(new Gson().toJson(shared_common_pref.getvalue("masterroute")), userType);
            loadWorktypeSpinner();
            loaddistriSpinner();
            loadroute();
            Log.e("SHAREDPREF_Length", String.valueOf(worktypelist.size()));
        }else {
            Log.e("LOADING", "CHECKING");
            presenter = new MasterSync_Implementations(this, new Master_Sync_View());
            presenter.requestDataFromServer();
        }
*/


        /*String worktype = "{\"tableName\":\"mas_worktype\",\"coloumns\":\"[\\\"type_code as id\\\", \\\"Wtype as name\\\"]\",\"where\":\"[\\\"isnull(Active_flag,0)=0\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Work_Type_Model>> mCall = apiInterface.Getworktype("3", "MGR4762", "MGR4762", "24", worktype);

        mCall.enqueue(new Callback<List<Work_Type_Model>>() {
            @Override
            public void onResponse(Call<List<Work_Type_Model>> call, Response<List<Work_Type_Model>> response) {
               // locationList=response.body();
                Log.e("GetWorktype", String.valueOf(response.body().toString()));
                Gson gson = new Gson();
                Type userType = new TypeToken<ArrayList<Work_Type_Model>>(){}.getType();
                worktypelist = response.body();
                //spinnerLoad();
                loadSpinner();
                Log.e("Work_Type_Model", String.valueOf(response.body().toString()));

                System.out.println("WORKTYPE_LENGTH"+worktypelist.size());
            }

            @Override
            public void onFailure(Call<List<Work_Type_Model>> call, Throwable t) {

            }
        });*/
    }

    public void loaddistriSpinner() {
        ArrayList<String> distributor = new ArrayList<>();
        for (int i = 0; i < distributor_master.size(); i++) {

            distributor.add(distributor_master.get(i).getName());

        }

        worktypedistributor.setAdapter(new ArrayAdapter<>(Mydayplan_Activity.this, android.R.layout.simple_spinner_dropdown_item, distributor));
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

        worktyperoute.setAdapter(new ArrayAdapter<>(Mydayplan_Activity.this, android.R.layout.simple_spinner_dropdown_item, route));
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

    /*public void loadWorktypeSpinner() {

        Log.e("WOrktype_Length", String.valueOf(worktypelist.size()));
        ArrayList<String> worktype = new ArrayList<>();


        for (int i = 0; i < worktypelist.size(); i++) {

            worktype.add(worktypelist.get(i).getName());

        }
        worktypespinner.setAdapter(new ArrayAdapter<>(Mydayplan_Activity.this, android.R.layout.simple_spinner_dropdown_item, worktype));
        worktypespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                worktype_id = String.valueOf(worktypelist.get(position).getId());
                worktypeflag = worktypelist.get(position).getFWFlg();
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }*/

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

        Toast.makeText(this, "Position" + position, Toast.LENGTH_SHORT).show();
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
            // loadWorktypeSpinner();

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
            common_class.ProgressdialogShow(2, "Day plan");
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
                        jsonobj.put("wtype", addquote(worktype_id));
                        jsonobj.put("sf_member_code", addquote(Shared_Common_Pref.Sf_Code));
                        jsonobj.put("stockist", addquote(distributorid));
                        jsonobj.put("stkName", distributorname);
                        jsonobj.put("dcrtype", "APP");
                        jsonobj.put("cluster", addquote(routeid));
                        jsonobj.put("custid", "");
                        jsonobj.put("address", "");
                        jsonobj.put("remarks", addquote(remarks));
                        jsonobj.put("OtherWors", "");
                        jsonobj.put("FWFlg", addquote(worktypeflag));
                        jsonobj.put("ClstrName", addquote(routename));
                        jsonobj.put("AppVersion", "V_1.0.5");
                        jsonobj.put("dcr_activity_date", Common_Class.GetDate());
                        jsonobj.put("worked_with", "''");
                        jsonarrplan.put("tbMyDayPlan", jsonobj);
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
                                    common_class.CommonIntentwithFinish(Tp_Calander.class);
                                    Toast.makeText(Mydayplan_Activity.this, "Day Plan Submitted Successfully", Toast.LENGTH_SHORT).show();
                                }


                            }

                            @Override
                            public void onFailure(Call<Object> call, Throwable t) {
                                progressbar.setVisibility(View.GONE);

                                Log.e("Reponse TAG", "onFailure : " + t.toString());
//                    Toast.makeText(getActivity(), "Myday Plan Not Submitted ", Toast.LENGTH_SHORT).show();
//                    Intent i = new Intent(Leave_Activity.this, MainActivity.class);
//                    startActivity(i);
//                    getFragmentManager().beginTransaction().replace(R.id.DCRMain_Frame, new Home_Fragment_Activity()).commit();
                            }
                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            case R.id.backarow:
                common_class.CommonIntentwithFinish(Dashboard.class);
                break;
            case R.id.worktypelayout:
                customDialog = new CustomListViewDialog(Mydayplan_Activity.this, worktypelist, "worktype");
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);

                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);                customDialog.show();

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

    @Override
    public void OnclickMasterType(int Id, String Worktypepos) {
        customDialog.dismiss();
        if (Worktypepos.equals("worktype")) {
            worktype_text.setText(worktypelist.get(Id).getName());
            Toast.makeText(this, "Selected Type" + Worktypepos, Toast.LENGTH_SHORT).show();

        }
    }
}



package com.hap.checkinproc.Activity_Hap;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.Joint_Work_Listner;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.MVP.Main_Model;
import com.hap.checkinproc.MVP.MasterSync_Implementations;
import com.hap.checkinproc.MVP.Master_Sync_View;
import com.hap.checkinproc.Model_Class.ModeOfTravel;
import com.hap.checkinproc.Model_Class.Route_Master;
import com.hap.checkinproc.R;
import com.hap.checkinproc.Status_Adapter.Joint_Work_Adapter;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hap.checkinproc.Common_Class.Common_Class.addquote;

public class Tp_Mydayplan extends AppCompatActivity implements Main_Model.MasterSyncView, View.OnClickListener, Master_Interface {
    Spinner worktypespinner, worktypedistributor, worktyperoute;
    List<Common_Model> worktypelist = new ArrayList<>();
    List<Common_Model> Route_Masterlist = new ArrayList<>();
    List<Common_Model> FRoute_Master = new ArrayList<>();
    LinearLayout worktypelayout, distributors_layout, route_layout, joint_work_Caption;
    List<Common_Model> distributor_master = new ArrayList<>();
    List<Common_Model> getfieldforcehqlist = new ArrayList<>();
    List<Common_Model> ChillingCenter_List = new ArrayList<>();
    List<Common_Model> Shift_Typelist = new ArrayList<>();
    List<Common_Model> Jointworklistview = new ArrayList<>();
    List<Common_Model> Savejointwork = new ArrayList<>();
    private Main_Model.presenter presenter;
    Gson gson;
    Type userType;
    EditText edt_remarks, eText, etext2;
    Shared_Common_Pref shared_common_pref;
    Common_Class common_class;
    String worktype_id, worktypename, distributorid, routename, routeid, Fieldworkflag = "", hqid, shifttypeid, Chilling_Id;
    private TextClock tClock;
    Button submitbutton;
    CustomListViewDialog customDialog;
    ImageView backarow;
    ProgressBar progressbar;
    TextView worktype_text, distributor_text, route_text, text_tour_plancount, hq_text, shift_type, chilling_text, Remarkscaption;

    TextView tourdate;
    Common_Model Model_Pojo;
    LinearLayout BusTo, jointwork_layout, joint_work_Recyclerview, hqlayout, shiftypelayout, Procrumentlayout, chillinglayout;

    RecyclerView jointwork_recycler;
    ImageView image;
    int joint_flag = 0;
    DatePickerDialog picker;
    CardView ModeTravel, card_Toplace, CardDailyAllowance, card_from;
    LinearLayout BikeMode, BusMode, ReasonPhoto, ProofImage;
    EditText BusFrom, EditRemarks;
    public static final String Name = "Allowance";
    public static final String MOT = "ModeOfTravel";
    String DM = "", DriverNeed = "false", DriverMode = "", modeTypeVale = "", mode = "", imageURI = "", modeVal = "", StartedKM = "", FromKm = "", ToKm = "", Fare = "", strDailyAllowance = "", strDriverAllowance = "", StToEnd = "", StrID = "";
    private ArrayList<String> travelTypeList;
    CheckBox driverAllowance;
    String driverAllowanceBoolean = "";
    List<ModeOfTravel> modelOfTravel;
    List<Common_Model> modelTravelType = new ArrayList<>();
    TextView TextMode, TextToAddress, dailyAllowance;
    LinearLayout linCheckdriver;
    List<Common_Model> listOrderType = new ArrayList<>();
    Common_Model mCommon_model_spinner;
    String modeId = "", toId = "", startEnd = "";
    ScrollView tpScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tp__mydayplan);
        progressbar = findViewById(R.id.progressbar);
        shared_common_pref = new Shared_Common_Pref(this);
        common_class = new Common_Class(this);
        edt_remarks = findViewById(R.id.edt_remarks);
        gson = new Gson();
        tourdate = findViewById(R.id.tourdate);
        Log.e("TOuR_PLAN_DATE", common_class.getintentValues("TourDate"));
        Log.e("TOuR_PLAN_DATE", common_class.getintentValues("TourDate"));
        tourdate.setText(common_class.getintentValues("TourDate"));
        route_text = findViewById(R.id.route_text);
        worktypelayout = findViewById(R.id.worktypelayout);
        distributors_layout = findViewById(R.id.distributors_layout);
        ModeTravel = findViewById(R.id.card_travel_mode);
        // card_from= findViewById(R.id.card_from);
        card_Toplace = findViewById(R.id.card_Toplace);
        eText = findViewById(R.id.from_date);
        eText.setInputType(InputType.TYPE_NULL);
        etext2 = findViewById(R.id.to_date);
        etext2.setInputType(InputType.TYPE_NULL);
        Remarkscaption = findViewById(R.id.remarkscaption);
        chillinglayout = findViewById(R.id.chillinglayout);
        chilling_text = findViewById(R.id.chilling_text);
        Procrumentlayout = findViewById(R.id.Procrumentlayout);
        hqlayout = findViewById(R.id.hqlayout);
        shiftypelayout = findViewById(R.id.shiftypelayout);
        hq_text = findViewById(R.id.hq_text);
        shift_type = findViewById(R.id.shift_type);
        route_layout = findViewById(R.id.route_layout);
        submitbutton = findViewById(R.id.submitbutton);
        worktype_text = findViewById(R.id.worktype_text);
        distributor_text = findViewById(R.id.distributor_text);
        text_tour_plancount = findViewById(R.id.text_tour_plancount);
        text_tour_plancount.setText("0");
        presenter = new MasterSync_Implementations(this, new Master_Sync_View());
        presenter.requestDataFromServer();
        jointwork_layout = findViewById(R.id.jointwork_layout);
        jointwork_layout.setOnClickListener(this);
        jointwork_recycler = findViewById(R.id.jointwork_recycler);
        jointwork_recycler.setLayoutManager(new LinearLayoutManager(this));
        image = findViewById(R.id.arowimg);
        joint_work_Caption = findViewById(R.id.joint_work_lt);
        joint_work_Recyclerview = findViewById(R.id.joint_work_listlt);

        BusTo = findViewById(R.id.lin_to_place);

        submitbutton.setOnClickListener(this);
        worktypelayout.setOnClickListener(this);
        distributors_layout.setOnClickListener(this);
        route_layout.setOnClickListener(this);
        shiftypelayout.setOnClickListener(this);
        hqlayout.setOnClickListener(this);
        eText.setOnClickListener(this);
        etext2.setOnClickListener(this);

        card_Toplace.setOnClickListener(this);
        chillinglayout.setOnClickListener(this);
        joint_work_Recyclerview.setVisibility(View.GONE);
        BusFrom = findViewById(R.id.edt_frm);
        TextMode = findViewById(R.id.txt_mode);
        TextToAddress = findViewById(R.id.edt_to);
        CardDailyAllowance = findViewById(R.id.card_daily_allowance);
        dailyAllowance = findViewById(R.id.text_daily_allowance);
        driverAllowance = findViewById(R.id.da_driver_allowance);
        linCheckdriver = findViewById(R.id.lin_check_driver);
        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });
        ModeTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modelTravelType.clear();
                dynamicMode();
            }
        });
        if (Shared_Common_Pref.Dept_Type.equals("1")) {
            jointwork_layout.setVisibility(View.GONE);
            distributors_layout.setVisibility(View.GONE);
            route_layout.setVisibility(View.GONE);
            Procrumentlayout.setVisibility(View.VISIBLE);
            joint_work_Recyclerview.setVisibility(View.GONE);
            joint_work_Caption.setVisibility(View.GONE);
            edt_remarks.setHint("Enter the Purpose of Visit");
            Remarkscaption.setText("Visiting Purpose");
        } else {
            jointwork_layout.setVisibility(View.VISIBLE);
            distributors_layout.setVisibility(View.VISIBLE);
            route_layout.setVisibility(View.VISIBLE);
            Procrumentlayout.setVisibility(View.GONE);
            joint_work_Recyclerview.setVisibility(View.GONE);
            Remarkscaption.setText("Remarks");
            edt_remarks.setHint("Enter The Remarks");
        }
        CardDailyAllowance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listOrderType.clear();
                OrderType();
            }
        });

        tpScrollView = findViewById(R.id.tp_scroll);


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

        Log.e("ROUTE_MASTER_Object", String.valueOf(noticeArrayList));
        Log.e("TAG", "response Tbmydayplan: " + new Gson().toJson(noticeArrayList));
        if (position == 0) {
            Log.e("SharedprefrenceVALUES", new Gson().toJson(noticeArrayList));
            GetJsonData(new Gson().toJson(noticeArrayList), "0");
        } else if (position == 1) {
            GetJsonData(new Gson().toJson(noticeArrayList), "1");
        } else if (position == 2) {
            GetJsonData(new Gson().toJson(noticeArrayList), "2");
        } else if (position == 3) {
            GetJsonData(new Gson().toJson(noticeArrayList), "3");
        } else if (position == 4) {
            GetJsonData(new Gson().toJson(noticeArrayList), "4");
        } else if (position == 5) {
            GetJsonData(new Gson().toJson(noticeArrayList), "5");
            //Get_MydayPlan(common_class.getintentValues("TourDate"));
        } else {
            GetJsonData(new Gson().toJson(noticeArrayList), "6");
            common_class.ProgressdialogShow(2, "Day plan");
            Get_MydayPlan(common_class.getintentValues("TourDate"));
        }

    }

    public void loadroute(String id) {
        Log.e("Select the Distributor", String.valueOf(id));
        if (common_class.isNullOrEmpty(String.valueOf(id))) {
            Toast.makeText(this, "Select the Distributor", Toast.LENGTH_SHORT).show();
        }

        FRoute_Master.clear();
        for (int i = 0; i < Route_Masterlist.size(); i++) {
            if (Route_Masterlist.get(i).getFlag().toLowerCase().trim().replaceAll("\\s", "").contains(id.toLowerCase().trim().replaceAll("\\s", ""))) {
                Log.e("Route_Masterlist", String.valueOf(id) + "STOCKIST" + Route_Masterlist.get(i).getFlag());
                FRoute_Master.add(new Common_Model(Route_Masterlist.get(i).getId(), Route_Masterlist.get(i).getName(), Route_Masterlist.get(i).getFlag()));
            }

        }


    }


    @Override
    public void onResponseFailure(Throwable throwable) {

    }

    @Override
    public void OnclickMasterType(java.util.List<Common_Model> myDataset, int position, int type) {
        customDialog.dismiss();
        if (type == 1) {
            route_text.setText("");
            distributor_text.setText("");
            worktype_text.setText(myDataset.get(position).getName());
            worktype_id = String.valueOf(myDataset.get(position).getId());
            Log.e("FIELD_WORK", myDataset.get(position).getFlag());
            Fieldworkflag = myDataset.get(position).getFlag();
            if (myDataset.get(position).getFlag().equals("F") && Shared_Common_Pref.Dept_Type.equals("0")) {
                distributors_layout.setVisibility(View.VISIBLE);
                route_layout.setVisibility(View.VISIBLE);
                //joint_work_Recyclerview.setVisibility(View.VISIBLE);
                joint_work_Caption.setVisibility(View.VISIBLE);
                jointwork_layout.setVisibility(View.VISIBLE);
            } else if (myDataset.get(position).getFlag().equals("H") || myDataset.get(position).getFlag().equals("W")) {
                joint_work_Recyclerview.setVisibility(View.GONE);
                joint_work_Caption.setVisibility(View.GONE);
                jointwork_layout.setVisibility(View.GONE);
                distributors_layout.setVisibility(View.GONE);
                route_layout.setVisibility(View.GONE);
            } else if (myDataset.get(position).getFlag().equals("N") && Shared_Common_Pref.Dept_Type.equals("0")) {
                //joint_work_Recyclerview.setVisibility(View.VISIBLE);
                joint_work_Caption.setVisibility(View.VISIBLE);
                jointwork_layout.setVisibility(View.VISIBLE);
                distributors_layout.setVisibility(View.GONE);
                route_layout.setVisibility(View.GONE);
            } else {
                distributors_layout.setVisibility(View.GONE);
                route_layout.setVisibility(View.GONE);
                jointwork_layout.setVisibility(View.VISIBLE);
            }

        } else if (type == 2) {
            routeid = null;
            routename = null;
            route_text.setText("");
            distributor_text.setText(myDataset.get(position).getName());
            distributorid = String.valueOf(myDataset.get(position).getId());
            Log.e("StockistID", myDataset.get(position).getId());
            loadroute(myDataset.get(position).getId());
        } else if (type == 3) {
            route_text.setText(myDataset.get(position).getName());
            routename = myDataset.get(position).getName();
            routeid = myDataset.get(position).getId();
        } else if (type == 4) {
            hq_text.setText(myDataset.get(position).getName());
            hqid = myDataset.get(position).getId();
        } else if (type == 5) {
            shift_type.setText(myDataset.get(position).getName());
            shifttypeid = myDataset.get(position).getId();
        } else if (type == 7) {
            BusFrom.setText(myDataset.get(position).getName());
            shifttypeid = myDataset.get(position).getId();
        } else if (type == 9) {
            TextToAddress.setText(myDataset.get(position).getName());
            toId = myDataset.get(position).getId();
        } else if (type == 8) {
            TextMode.setText(myDataset.get(position).getName());
            DriverMode = myDataset.get(position).getCheckouttime();
            modeId = myDataset.get(position).getFlag();


            Log.e("Dash_Mode_Count", DriverMode);
            startEnd = myDataset.get(position).getId();
            if (startEnd.equals("0")) {
                mode = "11";
                FromKm = "";
                ToKm = "";
                StartedKM = "";
                BusFrom.setText("");
                TextToAddress.setText("");
            } else {
                mode = "12";
                FromKm = "";
                ToKm = "";
                StartedKM = "";
                BusFrom.setText("");
                TextToAddress.setText("");
            }

            if (DriverMode.equals("1")) {
                linCheckdriver.setVisibility(View.VISIBLE);
            } else {
                linCheckdriver.setVisibility(View.GONE);
            }
            DriverNeed = "";
            driverAllowance.setChecked(false);
        } else if (type == 10) {
            TextToAddress.setText(myDataset.get(position).getName());
        } else if (type == 100) {
            String TrTyp = myDataset.get(position).getName();
            dailyAllowance.setText(TrTyp);
            if (TrTyp.equals("HQ")) {
                BusTo.setVisibility(View.GONE);
            } else {
                BusTo.setVisibility(View.VISIBLE);
            }
            TextToAddress.setText("");
        } else {
            chilling_text.setText(myDataset.get(position).getName());
            Chilling_Id = myDataset.get(position).getId();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.submitbutton:
                if (vali()) {
                    Savejointwork.addAll(common_class.getfilterList(Jointworklistview));
                    Log.e("Savejointwork_SIZE", String.valueOf(Savejointwork.size()));
                    String jointwork = "";
                    String jointworkname = "";
                    for (int ii = 0; ii < Savejointwork.size(); ii++) {
                        if (ii != 0) {
                            jointwork = jointwork.concat(",");
                            jointworkname = jointworkname.concat(",");
                        }
                        Log.e("JOINT_WORK_SELECT_NAME", Savejointwork.get(ii).getName());
                        jointwork = jointwork.concat(Savejointwork.get(ii).getId());
                        jointworkname = jointworkname.concat(Savejointwork.get(ii).getName());
                    }
                    Log.e("JOINT_WORK", jointwork);
                    common_class.ProgressdialogShow(1, "Tour  plan");
                    Calendar c = Calendar.getInstance();
                    String Dcr_Dste = new SimpleDateFormat("HH:mm a", Locale.ENGLISH).format(new Date());
                    JSONArray jsonarr = new JSONArray();
                    JSONObject jsonarrplan = new JSONObject();

                    String remarks = edt_remarks.getText().toString();
                    try {

                        JSONObject jsonobj = new JSONObject();
                        jsonobj.put("worktype_code", addquote(worktype_id));
                        jsonobj.put("worktype_name", addquote(worktype_text.getText().toString()));
                        jsonobj.put("sfName", addquote(Shared_Common_Pref.Sf_Name));
                        jsonobj.put("RouteCode", addquote(routeid));
                        jsonobj.put("objective", addquote(remarks));
                        jsonobj.put("RouteName", addquote(routename));
                        jsonobj.put("Tour_Date", addquote(tourdate.getText().toString()));
                        //PROCUREMENT
                        jsonobj.put("HQid", addquote(hqid));
                        jsonobj.put("Chilling_Id", addquote(Chilling_Id));
                        jsonobj.put("Shift_Type_Id", addquote(shifttypeid));
                        jsonobj.put("Fromdate", addquote(eText.getText().toString()));
                        jsonobj.put("Todate", addquote(etext2.getText().toString()));
                        //END PROCUREMENT
                        jsonobj.put("Worked_with_Code", addquote(distributorid));
                        jsonobj.put("Worked_with_Name", addquote(distributor_text.getText().toString()));
                        jsonobj.put("Multiretailername", "''");
                        jsonobj.put("MultiretailerCode", "''");
                        jsonobj.put("worked_with", addquote(jointworkname));
                        jsonobj.put("jointWorkCode", addquote(jointwork));
                        jsonobj.put("HQ_Code", "''");
                        jsonobj.put("HQ_Name", "''");
                        jsonobj.put("Flag", addquote(Fieldworkflag));
                        jsonobj.put("MOT", addquote(TextMode.getText().toString()));
                        jsonobj.put("DA_Type", addquote(dailyAllowance.getText().toString()));
                        jsonobj.put("Driver_Allow", addquote((driverAllowance.isChecked()) ? "1" : "0"));
                        jsonobj.put("From_Place", addquote(BusFrom.getText().toString()));
                        jsonobj.put("To_Place", addquote(TextToAddress.getText().toString()));
                        jsonobj.put("MOT_ID", addquote(modeId));
                        jsonobj.put("To_Place_ID", addquote(toId));
                        jsonobj.put("Mode_Travel_ID", addquote(startEnd));
                        jsonarrplan.put("Tour_Plan", jsonobj);
                        jsonarr.put(jsonarrplan);
                        Log.e("Mydayplan_Object", jsonarr.toString());
                        Map<String, String> QueryString = new HashMap<>();
                        QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
                        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
                        QueryString.put("State_Code", Shared_Common_Pref.StateCode);
                        QueryString.put("desig", "MGR");

                        Log.e("QueryString", String.valueOf(QueryString));
                        Log.e("QueryString_SF", Shared_Common_Pref.Sf_Code);
                        Log.e("QueryString_DV", Shared_Common_Pref.Div_Code);
                        Log.e("QueryString_Sc", Shared_Common_Pref.StateCode);


                        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                        Call<Object> Callto = apiInterface.Tb_Mydayplan(QueryString, jsonarr.toString());
                        Callto.enqueue(new Callback<Object>() {
                            @Override
                            public void onResponse(Call<Object> call, Response<Object> response) {
                                Log.e("RESPONSE_FROM_SERVER", response.body().toString());
                                common_class.ProgressdialogShow(2, "Tour  plan");
                                if (response.code() == 200 || response.code() == 201) {
                                    //common_class.CommonIntentwithFinish(Dashboard.class);
                                    common_class.CommonIntentwithoutFinishputextra(Tp_Calander.class, "Monthselection", String.valueOf(common_class.getintentValues("TourMonth")));
                                    Toast.makeText(Tp_Mydayplan.this, "Tour Plan Submitted Successfully", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<Object> call, Throwable t) {
                                common_class.ProgressdialogShow(2, "Tour  plan");
                                Log.e("Reponse TAG", "onFailure : " + t.toString());
                            }
                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.jointwork_layout:

                if (joint_flag == 0) {
                    joint_flag = 1;
                    image.setImageResource(R.drawable.arrow_up);
                    joint_work_Recyclerview.setVisibility(View.VISIBLE);
                    Log.e("JOINTWORK_UP", "1");
                } else {
                    Log.e("JOINTWORK_DOWN", "2");
                    image.setImageResource(R.drawable.arrow_down);
                    joint_flag = 0;
                    joint_work_Recyclerview.setVisibility(View.GONE);
                }
                break;
            case R.id.worktypelayout:
                customDialog = new CustomListViewDialog(Tp_Mydayplan.this, worktypelist, 1);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();

                Log.e("Work_Type_List", String.valueOf(worktypelist));

                break;
            case R.id.distributors_layout:
                customDialog = new CustomListViewDialog(Tp_Mydayplan.this, distributor_master, 2);
                Window windoww = customDialog.getWindow();
                windoww.setGravity(Gravity.CENTER);
                windoww.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();

                break;
            case R.id.route_layout:
                customDialog = new CustomListViewDialog(Tp_Mydayplan.this, FRoute_Master, 3);
                Window windowww = customDialog.getWindow();
                windowww.setGravity(Gravity.CENTER);
                windowww.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();

                break;

            case R.id.hqlayout:
                customDialog = new CustomListViewDialog(Tp_Mydayplan.this, getfieldforcehqlist, 4);
                Window windowhq = customDialog.getWindow();
                windowhq.setGravity(Gravity.CENTER);
                windowhq.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();

                break;

            case R.id.shiftypelayout:
                customDialog = new CustomListViewDialog(Tp_Mydayplan.this, Shift_Typelist, 5);
                Window windowstype = customDialog.getWindow();
                windowstype.setGravity(Gravity.CENTER);
                windowstype.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
                break;
            case R.id.from_date:
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(Tp_Mydayplan.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                eText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                //difference();
                            }
                        }, year, month, day);
                Calendar calendarmin = Calendar.getInstance();
                // calendarmin.set(Integer.parseInt(minYear), Integer.parseInt(minMonth) - 1, Integer.parseInt(minDay));
                //picker.getDatePicker().setMinDate(calendarmin.getTimeInMillis());
                picker.show();
                break;

            case R.id.to_date:

                final Calendar cldrr = Calendar.getInstance();
                int dayy = cldrr.get(Calendar.DAY_OF_MONTH);
                int monthh = cldrr.get(Calendar.MONTH);
                int yearr = cldrr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(Tp_Mydayplan.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                etext2.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, yearr, monthh, dayy);
                picker.show();
                break;


            case R.id.chillinglayout:
                customDialog = new CustomListViewDialog(Tp_Mydayplan.this, ChillingCenter_List, 6);
                Window chillwindow = customDialog.getWindow();
                chillwindow.setGravity(Gravity.CENTER);
                chillwindow.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
                break;

         /*   case R.id.card_from:
                customDialog = new CustomListViewDialog(Tp_Mydayplan.this, getfieldforcehqlist, 7);
                Window chillwindoww = customDialog.getWindow();
                chillwindoww.setGravity(Gravity.CENTER);
                chillwindoww.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
                break;*/

            case R.id.card_Toplace:
                customDialog = new CustomListViewDialog(Tp_Mydayplan.this, getfieldforcehqlist, 9);
                Window chillwindowww = customDialog.getWindow();
                chillwindowww.setGravity(Gravity.CENTER);
                chillwindowww.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
                break;

        }
    }

    public void OrderType() {
        travelTypeList = new ArrayList<>();
        travelTypeList.add("HQ");
        travelTypeList.add("EXQ");
        travelTypeList.add("Out Station");

        for (int i = 0; i < travelTypeList.size(); i++) {
            String id = String.valueOf(travelTypeList.get(i));
            String name = travelTypeList.get(i);
            mCommon_model_spinner = new Common_Model(id, name, "flag");
            listOrderType.add(mCommon_model_spinner);
        }
        customDialog = new CustomListViewDialog(Tp_Mydayplan.this, listOrderType, 100);
        Window window = customDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        customDialog.show();

    }

    public boolean vali() {
        if (worktype_text.getText().toString() == null || worktype_text.getText().toString().isEmpty() || worktype_text.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Select The Worktype", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Shared_Common_Pref.Dept_Type.equals("1") && (hq_text.getText().toString() == null || hq_text.getText().toString().isEmpty() || hq_text.getText().toString().equalsIgnoreCase(""))) {
            Toast.makeText(this, "Select The Head Quarters", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Shared_Common_Pref.Dept_Type.equals("1") && (chilling_text.getText().toString() == null || chilling_text.getText().toString().isEmpty() || chilling_text.getText().toString().equalsIgnoreCase(""))) {
            Toast.makeText(this, "Select The Chilling Center", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (Shared_Common_Pref.Dept_Type.equals("1") && (shift_type.getText().toString() == null || shift_type.getText().toString().isEmpty() || shift_type.getText().toString().equalsIgnoreCase(""))) {
            Toast.makeText(this, "Select The Shift M/E", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Shared_Common_Pref.Dept_Type.equals("1") && (eText.getText().toString() == null || eText.getText().toString().isEmpty() || eText.getText().toString().equalsIgnoreCase(""))) {
            Toast.makeText(this, "Select The From Date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Shared_Common_Pref.Dept_Type.equals("1") && (etext2.getText().toString() == null || etext2.getText().toString().isEmpty() || etext2.getText().toString().equalsIgnoreCase(""))) {
            Toast.makeText(this, "Select The To Date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Shared_Common_Pref.Dept_Type.equals("0") && Fieldworkflag.equals("F") && (distributor_text.getText().toString() == null || distributor_text.getText().toString().isEmpty() || distributor_text.getText().toString().equalsIgnoreCase(""))) {
            Toast.makeText(this, "Select The Distributor", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Shared_Common_Pref.Dept_Type.equals("0") && Fieldworkflag.equals("F") && (route_text.getText().toString() == null || route_text.getText().toString().isEmpty() || route_text.getText().toString().equalsIgnoreCase(""))) {
            Toast.makeText(this, "Select The Route", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextMode.getText().toString() == null || TextMode.getText().toString().isEmpty() || TextMode.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Select The Mode of Travel", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    common_class.CommonIntentwithoutFinishputextra(Tp_Calander.class, "Monthselection", String.valueOf(common_class.getintentValues("TourMonth")));
                }
            });

    @Override
    public void onBackPressed() {

    }

    private void GetJsonData(String jsonResponse, String type) {

        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String id = String.valueOf(jsonObject1.optInt("id"));
                String name = jsonObject1.optString("name");
                String flag = jsonObject1.optString("FWFlg");
                Model_Pojo = new Common_Model(id, name, flag);
                if (type.equals("0")) {
                    worktypelist.add(Model_Pojo);
                    Log.e("WORK_TYPE", String.valueOf(worktypelist));
                } else if (type.equals("1")) {
                    distributor_master.add(Model_Pojo);
                } else if (type.equals("2")) {
                    Log.e("STOCKIST_CODE", jsonObject1.optString("stockist_code"));
                    Model_Pojo = new Common_Model(id, name, jsonObject1.optString("stockist_code"));
                    FRoute_Master.add(Model_Pojo);
                    Route_Masterlist.add(Model_Pojo);
                } else if (type.equals("3")) {
                    Model_Pojo = new Common_Model(name + "-" + jsonObject1.optString("desig"), id, false);
                    Jointworklistview.add(Model_Pojo);
                } else if (type.equals("4")) {
                    String sid = jsonObject1.optString(("id"));
                    String Odflag = jsonObject1.optString("ODFlag");
                    Model_Pojo = new Common_Model(sid, name, Odflag);
                    getfieldforcehqlist.add(Model_Pojo);
                } else if (type.equals("5")) {
                    Model_Pojo = new Common_Model(id, name, flag);
                    Shift_Typelist.add(Model_Pojo);
                } else {
                    Model_Pojo = new Common_Model(id, name, flag);
                    ChillingCenter_List.add(Model_Pojo);
                }

            }

            if (type.equals("3")) {
                jointwork_recycler.setAdapter(new Joint_Work_Adapter(Jointworklistview, R.layout.jointwork_listitem, getApplicationContext(), "10", new Joint_Work_Listner() {
                    @Override
                    public void onIntentClick(int po, boolean flag) {
                        Jointworklistview.get(po).setSelected(flag);
                        int jcount = 0;
                        for (int i = 0; Jointworklistview.size() > i; i++) {
                            if (Jointworklistview.get(i).isSelected()) {
                                jcount = jcount + 1;
                            }

                        }
                        text_tour_plancount.setText(String.valueOf(jcount));
                    }
                }));

            }
            //spinner.setSelection(adapter.getPosition("select worktype"));
//            parseJsonData_cluster(clustspin_list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void Get_MydayPlan(String tourDate) {

        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "Get/Tp_dayplan");
        QueryString.put("Sf_code", Shared_Common_Pref.Sf_Code);
        QueryString.put("Date", tourDate);
        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
        QueryString.put("desig", "MGR");
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        JSONObject sp = new JSONObject();
        jsonArray.put(jsonObject);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> mCall = apiInterface.DCRSave(QueryString, jsonArray.toString());
        Log.e("Log_TpQuerySTring", QueryString.toString());
        Log.e("Log_Tp_SELECT", jsonArray.toString());

        mCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // locationList=response.body();

                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    Log.e("GettodayResult", "response Tp_View: " + jsonObject.getJSONArray("GettodayResult"));
                    JSONArray jsoncc = jsonObject.getJSONArray("GettodayResult");
                    Log.e("LENGTH", String.valueOf(jsoncc.length()));
                    if (jsoncc.length() > 0) {
                        joint_work_Recyclerview.setVisibility(View.GONE);
                        worktype_id = String.valueOf(jsoncc.getJSONObject(0).get("worktype_code"));
                        edt_remarks.setText(String.valueOf(jsoncc.getJSONObject(0).get("remarks")));
                        Fieldworkflag = String.valueOf(jsoncc.getJSONObject(0).get("Worktype_Flag"));
                        worktype_text.setText(String.valueOf(jsoncc.getJSONObject(0).get("worktype_name")));
                        TextMode.setText(String.valueOf(jsoncc.getJSONObject(0).get("MOT")));
                        BusFrom.setText(String.valueOf(jsoncc.getJSONObject(0).get("From_Place")));

                        if(jsoncc.getJSONObject(0).get("DA_Type").equals("HQ")){
                            BusTo.setVisibility(View.GONE);
                        }else {
                            BusTo.setVisibility(View.VISIBLE);
                        }
                        TextToAddress.setText(String.valueOf(jsoncc.getJSONObject(0).get("To_Place")));
                        dailyAllowance.setText(String.valueOf(jsoncc.getJSONObject(0).get("DA_Type")));
                        if (String.valueOf(jsoncc.getJSONObject(0).get("Driver_Allow")).equals("1")) {
                            linCheckdriver.setVisibility(View.VISIBLE);
                            driverAllowance.setChecked(true);
                        } else {
                            linCheckdriver.setVisibility(View.GONE);
                            driverAllowance.setChecked(false);
                        }
                        if (Shared_Common_Pref.Dept_Type.equals("1")) {
                            hq_text.setText(String.valueOf(jsoncc.getJSONObject(0).get("TourHQ_Name")));
                            hqid = String.valueOf(jsoncc.getJSONObject(0).get("Hq_Id"));
                            shift_type.setText(String.valueOf(jsoncc.getJSONObject(0).get("Typename")));
                            shifttypeid = String.valueOf(jsoncc.getJSONObject(0).get("SHift_Type_Id"));
                            chilling_text.setText(String.valueOf(jsoncc.getJSONObject(0).get("CCentre_Name")));
                            Chilling_Id = String.valueOf(jsoncc.getJSONObject(0).get("Chilling_Id"));
                            eText.setText(String.valueOf(jsoncc.getJSONObject(0).get("Fromdate")));
                            etext2.setText(String.valueOf(jsoncc.getJSONObject(0).get("Todate")));
                        } else {

                        }

                        if (String.valueOf(jsoncc.getJSONObject(0).get("submit_status")).equals("3")) {
                            submitbutton.setVisibility(View.GONE);
                        }
                        if (String.valueOf(jsoncc.getJSONObject(0).get("Worktype_Flag")).equals("F")) {

                            routename = String.valueOf(jsoncc.getJSONObject(0).get("RouteName"));
                            route_text.setText(String.valueOf(jsoncc.getJSONObject(0).get("RouteName")));
                            routeid = String.valueOf(jsoncc.getJSONObject(0).get("RouteCode"));
                            distributorid = String.valueOf(jsoncc.getJSONObject(0).get("Worked_with_Code"));
                            loadroute(String.valueOf(jsoncc.getJSONObject(0).get("Worked_with_Code")));
                            distributor_text.setText(String.valueOf(jsoncc.getJSONObject(0).get("Worked_with_Name")));
                            Fieldworkflag = String.valueOf(jsoncc.getJSONObject(0).get("Worktype_Flag"));
                            Log.e("WORKTYPE_FLAG", Fieldworkflag);
                            String myStr = String.valueOf(jsoncc.getJSONObject(0).get("JointworkCode"));
                            Log.e("WORKTYPE_JointworkCode", myStr);
                            int jcount = 0;
                            String[] arrOfStr = myStr.split(",");
                            for (int i = 0; Jointworklistview.size() > i; i++) {
                                Log.e("Route_ID", String.valueOf(Jointworklistview.get(i).getId()));

                                for (String a : arrOfStr) {
                                    System.out.println("ROUTE_IDDD" + a);
                                    if (Jointworklistview.get(i).getId().equals(a)) {
                                        Jointworklistview.get(i).setSelected(true);
                                        jcount = jcount + 1;
                                        Log.e("TESTING", String.valueOf(Jointworklistview.get(i).isSelected()));
                                    }
                                }


                            }
                            text_tour_plancount.setText(String.valueOf(jcount));
                            jointwork_recycler.setAdapter(new Joint_Work_Adapter(Jointworklistview, R.layout.jointwork_listitem, getApplicationContext(), "10", new Joint_Work_Listner() {
                                @Override
                                public void onIntentClick(int po, boolean flag) {
                                    Jointworklistview.get(po).setSelected(flag);
                                    int jcount = 0;
                                    for (int i = 0; Jointworklistview.size() > i; i++) {
                                        if (Jointworklistview.get(i).isSelected()) {
                                            jcount = jcount + 1;
                                        }

                                    }
                                    text_tour_plancount.setText(String.valueOf(jcount));
                                }
                            }));


                        } else if (String.valueOf(jsoncc.getJSONObject(0).get("Worktype_Flag")).equals("N")) {

                            distributors_layout.setVisibility(View.GONE);
                            route_layout.setVisibility(View.GONE);
                            String myStr = String.valueOf(jsoncc.getJSONObject(0).get("JointworkCode"));
                            Log.e("WORKTYPE_JointworkCode", myStr);

                            int jcount = 0;
                            String[] arrOfStr = myStr.split(",");

                            for (int i = 0; Jointworklistview.size() > i; i++) {
                                Log.e("Route_ID", String.valueOf(Jointworklistview.get(i).getId()));

                                for (String a : arrOfStr) {
                                    System.out.println("ROUTE_IDDD" + a);
                                    if (Jointworklistview.get(i).getId().equals(a)) {
                                        Jointworklistview.get(i).setSelected(true);
                                        jcount = jcount + 1;
                                        Log.e("TESTING", String.valueOf(Jointworklistview.get(i).isSelected()));
                                    }
                                }


                            }
                            text_tour_plancount.setText(String.valueOf(jcount));
                            jointwork_recycler.setAdapter(new Joint_Work_Adapter(Jointworklistview, R.layout.jointwork_listitem, getApplicationContext(), "10", new Joint_Work_Listner() {
                                @Override
                                public void onIntentClick(int po, boolean flag) {
                                    Jointworklistview.get(po).setSelected(flag);
                                    int jcount = 0;
                                    for (int i = 0; Jointworklistview.size() > i; i++) {
                                        if (Jointworklistview.get(i).isSelected()) {
                                            jcount = jcount + 1;
                                        }

                                    }
                                    text_tour_plancount.setText(String.valueOf(jcount));
                                }
                            }));


                        } else if (String.valueOf(jsoncc.getJSONObject(0).get("Worktype_Flag")).equals("H") || String.valueOf(jsoncc.getJSONObject(0).get("Worktype_Flag")).equals("W")) {
                            joint_work_Recyclerview.setVisibility(View.GONE);
                            joint_work_Caption.setVisibility(View.GONE);
                            jointwork_layout.setVisibility(View.GONE);
                            distributors_layout.setVisibility(View.GONE);
                            route_layout.setVisibility(View.GONE);

                        } else {
                            joint_work_Caption.setVisibility(View.GONE);
                            joint_work_Recyclerview.setVisibility(View.GONE);
                            distributors_layout.setVisibility(View.GONE);
                            route_layout.setVisibility(View.GONE);

                        }
                    } else {


                    }
                    common_class.ProgressdialogShow(2, "Tour plan");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                common_class.ProgressdialogShow(2, "Tour plan");
            }
        });
    }

    public void dynamicMode() {

        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "table/list");
        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
        QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
        QueryString.put("rSF", Shared_Common_Pref.Sf_Code);
        QueryString.put("State_Code", Shared_Common_Pref.StateCode);
        String commonLeaveType = "{\"tableName\":\"getmodeoftravel\",\"coloumns\":\"[\\\"id\\\",\\\"name\\\",\\\"Leave_Name\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<Object> call = service.GetRouteObjects(QueryString, commonLeaveType);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                userType = new TypeToken<ArrayList<ModeOfTravel>>() {
                }.getType();
                modelOfTravel = gson.fromJson(new Gson().toJson(response.body()), userType);
                for (int i = 0; i < modelOfTravel.size(); i++) {
                    String id = String.valueOf(modelOfTravel.get(i).getStEndNeed());
                    String name = modelOfTravel.get(i).getName();
                    String modeId = String.valueOf(modelOfTravel.get(i).getId());
                    String driverMode = String.valueOf(modelOfTravel.get(i).getDriverNeed());
                    Model_Pojo = new Common_Model(id, name, modeId, driverMode);
                    modelTravelType.add(Model_Pojo);
                }
                customDialog = new CustomListViewDialog(Tp_Mydayplan.this, modelTravelType, 8);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("LeaveTypeList", "Error");
            }
        });
    }
}

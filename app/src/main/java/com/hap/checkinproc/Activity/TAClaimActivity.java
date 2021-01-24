package com.hap.checkinproc.Activity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hap.checkinproc.Activity.Util.ImageFilePath;
import com.hap.checkinproc.Activity.Util.SelectionModel;
import com.hap.checkinproc.Activity.Util.UpdateUi;
import com.hap.checkinproc.Activity_Hap.CustomListViewDialog;
import com.hap.checkinproc.Activity_Hap.ERT;
import com.hap.checkinproc.Activity_Hap.Help_Activity;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.DailyExpenseAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import id.zelory.compressor.Compressor;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TAClaimActivity extends AppCompatActivity implements View.OnClickListener, Master_Interface,
        OnMapReadyCallback {

    CardView card_date, card_type_travel;
    TextView txt_date;
    ApiInterface apiInterface;
    DatePickerDialog fromDatePickerDialog;
    ImageView img_attach;
    ArrayList<SelectionModel> array = new ArrayList<>();
    ArrayList<SelectionModel> array1 = new ArrayList<>();
    ArrayList<SelectionModel> array2 = new ArrayList<>();
    ListView list;
    ArrayList<String> picPath = new ArrayList<>();
    ArrayList<String> finalpicPath = new ArrayList<>();
    Button btn_sub;
    String todayExp = "";
    Uri outputFileUri;
    int pos = -1;
    SharedPreferences UserDetails;
    public static final String MyPREFERENCES = "MyPrefs";
    String SF_code = "", div = "", State_Code = "";
    LinearLayout lay_row;
    int cardViewCount = 70, rlayCount = 700;
    int ccount = 1;
    Shared_Common_Pref mShared_common_pref;
    ArrayList<Integer> checking = new ArrayList<Integer>();
    private ArrayList<String> travelTypeList;
    private ArrayList<String> ModeList;
    String StartedKm = "", ClosingKm = "",
            ModeOfTravel = "", PersonalKm = "",
            FromPlace = "", ToPlace = "", Bus = "", StratedKmImage = "",
            EndedKmImage = "", BusFareImage = "";

    /*12/1/13*/
    Common_Model mCommon_model_spinner;
    List<Common_Model> listOrderType = new ArrayList<>();
    List<Common_Model> modelRetailDetails = new ArrayList<>();
    List<Common_Model> modelTypeList = new ArrayList<>();
    CustomListViewDialog customDialog;
    LinearLayout linAddAllowance;
    LinearLayout TravelBike;
    TextView TxtStartedKm, TxtClosingKm;
    LinearLayout LinearTravelBus;
    ListView ListAllowanceMode;
    ArrayList<SelectionModel> arrayAllowance = new ArrayList<>();
    ArrayList<SelectionModel> arrayListAllowance = new ArrayList<>();
    ArrayList<Integer> cardPosition;
    TextView modeTextView;
    ArrayList<Integer> cardCountt = new ArrayList<>();
    TextView travelTypeMode;
    CheckBox chkDriverAllow;
    CardView cardvi;
    LinearLayout diverAllowanceLinear;
    private static final int COLOR_ORANGE_ARGB = 0xffF57F17;
    TextView TotalTravelledKm;

    Integer totalkm = 0, totalPersonalKm = 0;
    String exp_for = "";
    JSONObject json_o;
    String DateForAPi = "";
    GoogleMap mGoogleMap;
    EditText enterMode, enterFrom, enterTo, enterFare;
    TextView PersonalTextKM;
    Integer Pva, C = 0;
    TextView PersonalKiloMeter;
    ArrayList<SelectionModel> travelAllowanceList = new ArrayList<>();
    EditText editTextRemarks;
    String driverAllowance = "0";
    String DateTime = "";
    SharedPreferences TaSharedPrefernce;
    public static final String mypreference = "mypref";
    public static final String Name = "Allowance";
    public static final String MOT = "ModeOfTravel";
    public static final String MOC = "ModeOfCount";
    public static final String SKM = "Started_km";
    Integer S = 0;
    String shortName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_a_claim);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.route_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        getToolbar();
        txt_date = findViewById(R.id.txt_date);
        card_date = findViewById(R.id.card_date);
        card_type_travel = findViewById(R.id.card_type_travel);
        img_attach = findViewById(R.id.img_attach);
        btn_sub = findViewById(R.id.btn_sub);
        list = findViewById(R.id.list);
        lay_row = findViewById(R.id.lay_row);
        linAddAllowance = findViewById(R.id.lin_add_allowance);
        TravelBike = findViewById(R.id.linear_bike);
        TxtStartedKm = findViewById(R.id.txt_started_km);
        TxtClosingKm = findViewById(R.id.txt_ended_km);
        travelTypeMode = findViewById(R.id.txt_type_travel);
        chkDriverAllow = findViewById(R.id.diver_allowance);
        PersonalTextKM = findViewById(R.id.personal_km_text);

        LinearTravelBus = findViewById(R.id.lin_travel_bus);
        ListAllowanceMode = findViewById(R.id.list_allowance_type);
        diverAllowanceLinear = findViewById(R.id.linear_da_allowance);
        TotalTravelledKm = findViewById(R.id.total_km);
        PersonalKiloMeter = findViewById(R.id.pers_kilo_meter);
        editTextRemarks = findViewById(R.id.edt_rmk);


        UserDetails = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SF_code = UserDetails.getString("Sfcode", "");
        div = UserDetails.getString("Divcode", "");
        State_Code = UserDetails.getString("State_Code", "");
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        String date = Common_Class.GetDate();

        DateForAPi = date.substring(0, date.indexOf(" "));
        callApi(DateForAPi, "");

        mShared_common_pref = new Shared_Common_Pref(this);


        TaSharedPrefernce = getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        if (TaSharedPrefernce.contains(SKM)) {

            StartedKm = TaSharedPrefernce.getString(SKM, "");
        }

        if (TaSharedPrefernce.contains(MOT)) {
            ModeOfTravel = TaSharedPrefernce.getString(MOT, "");

        }


        dynamicDate();
        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < picPath.size(); i++) {
                    getMulipart(picPath.get(i), -1);
                }
                for (int i = 0; i < array.size(); i++) {
                    String[] imp = array.get(i).getImg_url().split(",");
                    for (int j = 0; j < imp.length - 1; j++) {
                        getMulipart(imp[j], i);
                    }
                }
                submitData();
            }
        });
        DailyExpenseAdapter.bindUpdateListener(new UpdateUi() {
            @Override
            public void update(int value, int pos1) {
                pos = pos1;
                popupCapture("attachName");
            }
        });
        img_attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos = -1;
                popupCapture("attachName");
            }
        });

        card_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog = new CustomListViewDialog(TAClaimActivity.this, modelRetailDetails, 10);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
            }
        });

        card_type_travel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listOrderType.clear();
                OrderType();
            }
        });


        linAddAllowance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.travel_allowance_dynamic, null);
                // Add the new row before the add field button.

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


                layoutParams.setMargins(0, 10, 0, 0);
                LinearTravelBus.addView(rowView, layoutParams);
                ImageView imageAttach = findViewById(R.id.image_attach);
                imageAttach.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("ImageCapture", "ImageCapture");

                    }
                });

                int size = LinearTravelBus.getChildCount();
                Log.d("PARENT_COUNT", String.valueOf(size));


            }
        });

        chkDriverAllow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.e("LOGGGGGG", "LOGGGGGGGGGGGGGGGG");
                    callApi(DateForAPi, "DIVER");
                    driverAllowance = "1";
                } else {
                    callApi(DateForAPi, "asd");
                    Log.e("LOGGGGGG", "L");
                }
            }
        });
    }


    public void onDelete(View v) {
        LinearTravelBus.removeView((View) v.getParent());
    }



    /*Toolbar*/

    public void getToolbar() {

        TextView txtHelp = findViewById(R.id.toolbar_help);
        ImageView imgHome = findViewById(R.id.toolbar_home);
        txtHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Help_Activity.class));
            }
        });

        TextView txtErt = findViewById(R.id.toolbar_ert);
        TextView txtPlaySlip = findViewById(R.id.toolbar_play_slip);

        txtErt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ERT.class));
            }
        });
        txtPlaySlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        ObjectAnimator textColorAnim;
        textColorAnim = ObjectAnimator.ofInt(txtErt, "textColor", Color.WHITE, Color.TRANSPARENT);
        textColorAnim.setDuration(500);
        textColorAnim.setEvaluator(new ArgbEvaluator());
        textColorAnim.setRepeatCount(ValueAnimator.INFINITE);
        textColorAnim.setRepeatMode(ValueAnimator.REVERSE);
        textColorAnim.start();
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TAClaimActivity.class));
            }
        });
    }


    /*Choosing Dynamic date*/
    public void dynamicDate() {

        JSONObject jj = new JSONObject();
        try {
            jj.put("sfCode", mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code));
            jj.put("divisionCode", mShared_common_pref.getvalue(Shared_Common_Pref.Div_Code));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> call = apiInterface.getTADate(jj.toString());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray jsonArray = response.body();
                for (int a = 0; a < jsonArray.size(); a++) {
                    JsonObject jsonObject = (JsonObject) jsonArray.get(a);
                    String id = String.valueOf(jsonObject.get("id"));
                    String name = String.valueOf(jsonObject.get("Datewithname"));
                    Log.e("getTADate", name);
                    name = name.replaceAll("^[\"']+|[\"']+$", "");
                    mCommon_model_spinner = new Common_Model(id, name, "");
                    modelRetailDetails.add(mCommon_model_spinner);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("LeaveTypeList", "Error");
            }
        });
    }


    /*Travel Type*/

    /* Order Types*/
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
        customDialog = new CustomListViewDialog(TAClaimActivity.this, listOrderType, 100);
        Window window = customDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        customDialog.show();

    }


    /*Mode Type*/
    public void ModeType() {
        ModeList = new ArrayList<>();
        ModeList.add("Bus");
        ModeList.add("Car");
        ModeList.add("Taxi");

        for (int i = 0; i < ModeList.size(); i++) {
            String id = String.valueOf(ModeList.get(i));
            String name = ModeList.get(i);
            mCommon_model_spinner = new Common_Model(id, name, "flag");
            modelTypeList.add(mCommon_model_spinner);
        }
        customDialog = new CustomListViewDialog(TAClaimActivity.this, modelTypeList, 1);
        Window window = customDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        customDialog.show();

    }

    /*Display Mode of travel View based on the choosed Date*/
    public void displayTravelMode(String ChoosedDate) {
        Log.d("JSON_VALUE_O", ChoosedDate);

        ChoosedDate = ChoosedDate.replaceAll("^[\"']+|[\"']+$", "");

        Log.d("JSON_VALUE_N", ChoosedDate);
        JSONObject jj = new JSONObject();
        try {
            jj.put("sfCode", mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code));
            jj.put("divisionCode", mShared_common_pref.getvalue(Shared_Common_Pref.Div_Code));
            jj.put("Selectdate", ChoosedDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> call = apiInterface.getTAdateDetails(jj.toString());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray jsonArray = response.body();

                Log.d("JSON_VALUE", jsonArray.toString());
                Log.d("JSON_VALUE", "CHECKING");
                /* TravelBike.setVisibility(View.VISIBLE);*/
                for (int a = 0; a < jsonArray.size(); a++) {
                    JsonObject jsonObject = (JsonObject) jsonArray.get(a);
                    StartedKm = String.valueOf(jsonObject.get("Start_Km"));
                    if (StartedKm != null && !StartedKm.isEmpty() && !StartedKm.equals("null") && !StartedKm.equals("")) {

                    } else {
                        TxtStartedKm.setText(StartedKm);


                        S = Integer.valueOf(String.valueOf(TxtStartedKm.getText()));

                    }

                    ClosingKm = String.valueOf(jsonObject.get("End_Km"));
                    PersonalKm = String.valueOf(jsonObject.get("Personal_Km"));
                    StartedKm = StartedKm.replaceAll("^[\"']+|[\"']+$", "");
                    PersonalKm = PersonalKm.replaceAll("^[\"']+|[\"']+$", "");
                    if (PersonalKm.equals("null")) {
                        PersonalKiloMeter.setText(PersonalKm);
                    }
                    PersonalKiloMeter.setText(PersonalKm);


                    if (ClosingKm != null && !ClosingKm.isEmpty() && !ClosingKm.equals("null") && !ClosingKm.equals("")) {
                        ClosingKm = ClosingKm.replaceAll("^[\"']+|[\"']+$", "");
                        TxtClosingKm.setText(ClosingKm);
                        C = Integer.valueOf(ClosingKm);
                        Log.e("TOTAL_KM", String.valueOf(C));
                        totalkm = C - S;
                        Log.e("TOTAL_Claim_KM", String.valueOf(totalPersonalKm));
                        TotalTravelledKm.setText(String.valueOf(totalkm));
                    }

                    if (PersonalKm != null && !PersonalKm.isEmpty() && !PersonalKm.equals("null") && !PersonalKm.equals("")) {
                        PersonalKm = PersonalKm.replaceAll("^[\"']+|[\"']+$", "");
                        Pva = Integer.valueOf(PersonalKm);
                        totalPersonalKm = totalkm - Pva;
                        PersonalTextKM.setText(String.valueOf(totalPersonalKm));
                        Log.e("TOTAL_Claim_KM", (PersonalTextKM.getText().toString()));
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("LeaveTypeList", "Error");
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            //TODO: action
            ClipData mClipData = data.getClipData();
            int pickedImageCount;
            String filepathing = "";
            if (data.getData() == null) {
                Log.v("onactivity_result", data.getData() + " ");

                for (pickedImageCount = 0; pickedImageCount < mClipData.getItemCount();
                     pickedImageCount++) {
                    Log.v("picked_image_value", mClipData.getItemAt(pickedImageCount).getUri() + "");
                    ImageFilePath filepath = new ImageFilePath();
                    String fullPath = filepath.getPath(TAClaimActivity.this, mClipData.getItemAt(pickedImageCount).getUri());
                    Log.v("picked_fullPath", fullPath + "");
                    if (pos == -1)
                        picPath.add(fullPath);
                    else {
                        SelectionModel m = array.get(pos);
                        filepathing = m.getImg_url();
                        filepathing = filepathing + fullPath + ",";
                        m.setImg_url(filepathing);

                    }
                }
            } else {
                Log.v("data_pic_multiple", "is in empty");
                ImageFilePath filepath = new ImageFilePath();
                String fullPath = filepath.getPath(TAClaimActivity.this, data.getData());
                Log.v("data_pic_multiple11", fullPath);
                if (pos == -1)
                    picPath.add(fullPath);
                else {
                    SelectionModel m = array.get(pos);
                    filepathing = m.getImg_url();
                    filepathing = filepathing + fullPath + ",";
                    m.setImg_url(filepathing);

                }
            }

        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            String finalPath = "/storage/emulated/0";
            String filePath = outputFileUri.getPath();
            filePath = filePath.substring(1);
            filePath = finalPath + filePath.substring(filePath.indexOf("/"));
            Log.v("printing__file_path", filePath);
            if (pos == -1)
                picPath.add(filePath);
            else {
                SelectionModel m = array.get(pos);
                String filepathing = "";
                filepathing = m.getImg_url();
                filepathing = filepathing + filePath + ",";
                m.setImg_url(filepathing);
            }
            //filePathing = filePathing + filePath + ",";
        }
    }

    public void submitData() {


        String uniqueKey = SF_code + System.currentTimeMillis();
        Log.e("UNIQUE_KEY", uniqueKey);


        DateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar calobj = Calendar.getInstance();
        String dateTime = df.format(calobj.getTime());
        Log.e("DATe_TIME", dateTime);


        try {

            JSONArray ja = new JSONArray();

            /*Addtion Allowance Object*/
            JSONObject additionalAllowanceObject = new JSONObject();
            JSONArray AditionalAlowanceArray = new JSONArray();

            Log.e("travelAllowancesize()", String.valueOf(travelAllowanceList.size()));

            int size = LinearTravelBus.getChildCount();
            Log.d("PARENT_COUNT", String.valueOf(size));
            for (int i = 0; i < size; i++) {
                JSONObject AditionalTravelallowance = new JSONObject();
                View view = LinearTravelBus.getChildAt(i);
                enterMode = view.findViewById(R.id.enter_mode);
                enterFrom = view.findViewById(R.id.enter_from);
                enterTo = view.findViewById(R.id.enter_to);
                enterFare = view.findViewById(R.id.enter_fare);
                AditionalTravelallowance.put("MODE", enterMode.getText().toString());
                AditionalTravelallowance.put("FROM", enterFrom.getText().toString());
                AditionalTravelallowance.put("TO", enterTo.getText().toString());
                AditionalTravelallowance.put("FARE", enterFare.getText().toString());
                Log.e("Inside_mode", String.valueOf(enterFare.getText().toString()));
                AditionalAlowanceArray.put(AditionalTravelallowance);
                // additionalAllowanceObject.put("Aditional_Allowance", AditionalAlowanceArray);
            }

            Log.e("Karthick_ARRAY", AditionalAlowanceArray.toString());


            JSONObject jjMain = new JSONObject();
            JSONObject thi = null;
            for (int i = 0; i < array.size(); i++) {
                SelectionModel m = array.get(i);
                thi = new JSONObject();
                thi.put("ID", m.getCode());
                thi.put("Name", m.getTxt());
                thi.put("amt", m.getValue());
                String[] imgUrl = m.getImg_url().split(",");
                String url_img = "";
                for (int j = 0; j < imgUrl.length; j++) {
                    url_img = url_img + imgUrl[j].substring(imgUrl[j].lastIndexOf("/") + 1) + ",";
                }
                thi.put("imgData", url_img);

                Log.v("total_size__log", m.getArray().size() + "");
                JSONArray jjarray = new JSONArray();
                for (int k = 0; k < m.getArray().size(); k++) {
                    for (int h = 0; h < m.getArray().get(k).getArray().size(); h++) {
                        JSONObject jjjson = new JSONObject();
                        jjjson.put("id", m.getArray().get(k).getArray().get(h).getCode());
                        jjjson.put("val", m.getArray().get(k).getArray().get(h).getValue());
                        jjjson.put("mainid", m.getCode());
                        jjarray.put(jjjson);
                    }


                }
                Log.v("printing_final_totl", jjarray.toString());
                thi.put("value", jjarray);
                ja.put(thi);
            }

            /* thi.put("Aditional_Allowance", AditionalAlowanceArray);*/

            //    ja.put(additionalAllowanceObject);

            // ja.put(addtionDataObject);
            // ja.put(addtionDataObject);
            Log.v("printing_final", ja.toString());
            jjMain.put("sf", SF_code);
            jjMain.put("div", div);
            jjMain.put("date", dateTime);
            jjMain.put("dailyExpense", ja);
            jjMain.put("AditionalAlowanceArray", AditionalAlowanceArray);

            ja = new JSONArray();
            JSONObject jj = new JSONObject();
            JSONObject jjj = new JSONObject(todayExp);
            jj.put("LEOS", "");
            jj.put("MOT", jjj.getString("MOT"));
            jj.put("BusFare", "");
            jj.put("DateOfExp", DateTime);
            jj.put("travel_type", travelTypeMode.getText().toString());
            jj.put("driver_allowance", driverAllowance);
            jj.put("remarks", editTextRemarks.getText().toString());
            jj.put("Start_image", jjj.getString("Image_Url"));
            jj.put("Start_Km", TxtStartedKm.getText().toString());
            jj.put("TodayworkRoute", "");
            jj.put("TodayworkName", "");
            jj.put("Workplace", "");
            jj.put("Workplace_Name", "");
            jj.put("Stop_image", jjj.getString("End_Image_Url"));
            jj.put("Stop_km", TxtClosingKm.getText().toString());
            jj.put("Ukey", uniqueKey);
            jj.put("travelled_km", TotalTravelledKm.getText().toString());
            jj.put("personal_km", PersonalKiloMeter.getText().toString());
            jj.put("total_claiming_km", PersonalTextKM.getText().toString());
            jj.put("EventcaptureUrl", "");
            ja.put(jj);
            jjMain.put("EA", ja);

            Log.e("JSONOBJECT", jjMain.toString());
            Call<ResponseBody> submit = apiInterface.saveDailyAllowance(jjMain.toString());
            submit.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String jsonData = null;
                        jsonData = response.body().string();
                        Log.v("printing_json", jsonData);
                        JSONObject json = new JSONObject(jsonData);
                        if (json.getString("success").equalsIgnoreCase("true")) {
                            Toast.makeText(TAClaimActivity.this, "Submitted Successfully ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(TAClaimActivity.this, ViewTASummary.class);
                            intent.putExtra("DateofExpense", DateTime);
                            intent.putExtra("travelMode", travelTypeMode.getText().toString());
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        Log.v("printing_excep_va", e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });


        } catch (Exception e) {
            Log.v("printing_exception_are", e.getMessage());
        }

    }

    public void getMulipart(String path, int x) {
        MultipartBody.Part imgg = convertimg("file", path);
        HashMap<String, RequestBody> values = field("MR0417");
        CallApiImage(values, imgg, x);
    }

    public MultipartBody.Part convertimg(String tag, String path) {
        MultipartBody.Part yy = null;
        Log.v("full_profile", path);
        try {
            if (!TextUtils.isEmpty(path)) {

                File file = new File(path);
                if (path.contains(".png") || path.contains(".jpg") || path.contains(".jpeg"))
                    file = new Compressor(getApplicationContext()).compressToFile(new File(path));
                else
                    file = new File(path);
                RequestBody requestBody = RequestBody.create(MultipartBody.FORM, file);
                yy = MultipartBody.Part.createFormData(tag, file.getName(), requestBody);
            }
        } catch (Exception e) {
        }
        Log.v("full_profile", yy + "");
        return yy;
    }

    public HashMap<String, RequestBody> field(String val) {
        HashMap<String, RequestBody> xx = new HashMap<String, RequestBody>();
        xx.put("data", createFromString(val));

        return xx;

    }

    private RequestBody createFromString(String txt) {
        return RequestBody.create(MultipartBody.FORM, txt);
    }

    public void CallApiImage(HashMap<String, RequestBody> values, MultipartBody.Part imgg, final int x) {
        Call<ResponseBody> Callto;

        Callto = apiInterface.uploadimg(values, imgg);

        Callto.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.v("print_upload_file", "ggg" + response.isSuccessful() + response.body());
                //uploading.setText("Uploading "+String.valueOf(count)+"/"+String.valueOf(count_check));

                try {
                    if (response.isSuccessful()) {


                        Log.v("print_upload_file_true", "ggg" + response);
                        JSONObject jb = null;
                        String jsonData = null;
                        jsonData = response.body().string();
                        Log.v("request_data_upload", String.valueOf(jsonData));
                        JSONObject js = new JSONObject(jsonData);
                        if (js.getString("success").equalsIgnoreCase("true")) {
                            Log.v("printing_dynamic_cou", js.getString("url"));
                            if (x == -1) {
                                finalpicPath.add(js.getString("url"));
                            } else {
                                String filepathing = array.get(x).getImg_url();
                                filepathing = filepathing + js.getString("url") + ",";
                                array.get(x).setImg_url(filepathing);
                            }

                            //submitData();

                        }

                    }

                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("print_failure", "ggg" + t.getMessage());
            }
        });
    }


    public void popupCapture(String attachName) {
        Log.d("AttachName", attachName);
        final Dialog dialog = new Dialog(TAClaimActivity.this, R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_capture);
        dialog.show();
        TextView upload = dialog.findViewById(R.id.upload);
        TextView camera = dialog.findViewById(R.id.camera);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectMultiImage();
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureFile();
            }
        });
    }

    public void selectMultiImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
    }

    public void captureFile() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Uri outputFileUri = Uri.fromFile(new File(getExternalCacheDir().getPath(), "pickImageResult.jpeg"));
        outputFileUri = FileProvider.getUriForFile(TAClaimActivity.this, getApplicationContext().getPackageName() + ".provider", new File(getExternalCacheDir().getPath(), "pickImageResult" + System.currentTimeMillis() + ".jpeg"));
        Log.v("priniting_uri", outputFileUri.toString() + " output " + outputFileUri.getPath() + " raw_msg " + getExternalCacheDir().getPath());
        //content://com.saneforce.sbiapplication.fileprovider/shared_video/Android/data/com.saneforce.sbiapplication/cache/pickImageResult.jpeg
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, 2);
    }

    public void callApi(String date, String OS) {
        ArrayList<String> arryShort = new ArrayList<>();
        Log.e("OS_VALLUE", " " + OS);
        try {
            JSONObject jj = new JSONObject();
            jj.put("Ta_Date", date);
            jj.put("div", div);
            jj.put("sf", SF_code);
            jj.put("rSF", SF_code);
            jj.put("State_Code", State_Code);
            Log.v("json_obj_ta", jj.toString());
            Call<ResponseBody> Callto = apiInterface.getDailyAllowance(jj.toString());
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
                            array = new ArrayList<>();
                            lay_row.removeAllViews();
                            JSONObject js = new JSONObject(jsonData);
                            JSONArray ja = js.getJSONArray("ExpenseWeb");
                            for (int i = 0; i < ja.length(); i++) {
                                JSONObject json_oo = ja.getJSONObject(i);
                                JSONObject json_o = json_oo.getJSONObject("value1");
                                exp_for = json_o.getString("Exp_For");
                                shortName = json_o.getString("Short_Name");
                                Log.d("SHORT_NAME", shortName);
                                List<String> elements = new ArrayList<>(Arrays.asList(shortName));
                                Log.d("SHORT_NAME_AR", elements.toString());
                                Log.d("SHORT_NAME_AR", String.valueOf(elements.size()));

                                Log.e("EXP", exp_for);
                                ArrayList<SelectionModel> arr = new ArrayList<>();
                                ArrayList<SelectionModel> arr1 = new ArrayList<>();
                                JSONArray jjja = json_oo.getJSONArray("value");


                                for (int j = 0; j < jjja.length(); j++) {
                                    JSONObject json_in = jjja.getJSONObject(j);
                                    arr.add(new SelectionModel(json_in.getString("Ad_Fld_Name"), "", json_in.getString("Ad_Fld_ID"), "", ""));
                                }

                                if (OS.equals("")) {
                                    if (exp_for.equals("0")) {
                                        array.add(new SelectionModel(json_o.getString("Short_Name"), json_o.getString("Name"), "", json_o.getString("ID"), "", arr1, json_o.getString("user_enter"), json_o.getString("Attachemnt"), json_o.getString("Max_Allowance")));
                                    }
                                } else if (OS.equals("DIVER")) {

                                    if (exp_for.equals("0") || exp_for.equals("1") || exp_for.equals("2")) {

                                        array.add(new SelectionModel(json_o.getString("Short_Name"), json_o.getString("Name"), "", json_o.getString("ID"), "", arr1, json_o.getString("user_enter"), json_o.getString("Attachemnt"), json_o.getString("Max_Allowance")));
                                    }
                                } else {
                                    if (exp_for.equals("0") || exp_for.equals("1")) {
                                        array.add(new SelectionModel(json_o.getString("Short_Name"), json_o.getString("Name"), "", json_o.getString("ID"), "", arr1, json_o.getString("user_enter"), json_o.getString("Attachemnt"), json_o.getString("Max_Allowance")));
                                    }
                                }

                                arr1.add(new SelectionModel("", arr));

                            }

                            JSONArray ja1 = js.getJSONArray("TodayExpense");
                            if (ja1.length() != 0)
                                todayExp = ja1.getJSONObject(0).toString();
                            Log.v("todayExp_val", todayExp);
                            DailyExpenseAdapter adpt = new DailyExpenseAdapter(TAClaimActivity.this, array);
                            list.setAdapter(adpt);
                            for (int i = 0; i < array.size(); i++) {
                                createDynamicViewForSingleRow(array.get(i).getHeader(), array.get(i).getTxt(), array.get(i).getArray(), i, array.get(i).getUser_enter(), array.get(i).getAttachment(), array.get(i).getMax());
                                Log.e("String_Name", array.get(i).getTxt());
                                Log.e("String_Name", String.valueOf(array.get(i).getArray()));
                            }






                            for (int i = 0; i < arryShort.size(); i++) {
                                Log.d("SHORT_NAME_ARRAY", arryShort.toString());
                            }
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

    @SuppressLint("ResourceType")
    public void createDynamicViewForSingleRow(String headerValue, String name, ArrayList<SelectionModel> array, int position, String userenter, String attachment, String max) {
/*
        RelativeLayout ChildRelative = new RelativeLayout(this);

        RelativeLayout.LayoutParams childRelParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        ChildRelative.setLayoutParams(childRelParams);


        *//*Short Name*//*
        RelativeLayout.LayoutParams short_name_realtive = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        short_name_realtive.setMargins(5, 0, 0, 0);
        TextView ShortName = new TextView(this);
        ShortName.setText(headerValue);
        ShortName.setLayoutParams(short_name_realtive);
        ShortName.setTextSize(16f);
        ShortName.setId(1);
        ShortName.setTextColor(Color.BLACK);
        ChildRelative.addView(ShortName);



        *//*Add Allowance on Short Value*//*
        RelativeLayout.LayoutParams typeAddAllowance = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        typeAddAllowance.addRule(RelativeLayout.ALIGN_PARENT_END);
        typeAddAllowance.setMargins(0, 10, 0, 0);

        TextView addAllowance = new TextView(this);
        addAllowance.setText("Add Allowance");
        addAllowance.setLayoutParams(typeAddAllowance);
        addAllowance.setId(2);
        addAllowance.setTextColor(Color.BLUE);
        ChildRelative.addView(addAllowance);
        addAllowance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        lay_row.addView(ChildRelative);*/

    }

    @SuppressLint("ResourceType")
    public CardView generateView(int x, ArrayList<SelectionModel> arr, ArrayList<SelectionModel> arrayList, int pos, ArrayList<Integer> cardPos) {
        CardView cardview = new CardView(this);
        cardview.setId(cardViewCount);
        cardPos.add(cardViewCount);
        cardViewCount = cardViewCount + 1;
        LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        if (x != 0) {
            // CardView cc=lay_row.findViewById(x);
            //layoutparams.addRule(RelativeLayout.BELOW,cc.getId());
        }
        cardview.setLayoutParams(layoutparams);

        cardview.setRadius(5);

        cardview.setPadding(18, 18, 18, 18);

        cardview.setCardBackgroundColor(Color.GRAY);

        cardview.setUseCompatPadding(true);
        //cardview.setMaxCardElevation(2);
        cardview.setCardElevation(5);

        /* cardview.setMaxCardElevation(6);*/
        cardview.setRadius(8);
        LinearLayout lay = new LinearLayout(this);
        LinearLayout.LayoutParams params_3 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lay.setBackgroundColor(Color.parseColor("#ffffff"));
        lay.setPadding(5, 5, 5, 5);
        params_3.setMargins(5, 6, 5, 3);
        lay.setOrientation(LinearLayout.VERTICAL);

        try {
            for (int i = 0; i < arr.size(); i++) {

                SelectionModel mm = arr.get(i);
                EditText edt1 = new EditText(this);
                edt1.setLayoutParams(params_3);
                edt1.setHint(arr.get(i).getTxt());
                edt1.setHintTextColor(Color.parseColor("#C0C0C0"));
                edt1.setBackgroundResource(R.drawable.hash_border);
                edt1.setText("");
                edt1.setPadding(9, 9, 9, 9);
                lay.addView(edt1);
                edt1.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        mm.setValue(editable.toString());
                    }
                });
            }
        } catch (Exception e) {

        }

        LinearLayout.LayoutParams params_4 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params_4.setMargins(0, 5, 0, 0);
        RelativeLayout rlay_icon = new RelativeLayout(this);
        rlay_icon.setId(657);
        RelativeLayout.LayoutParams params_5 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //rlay_icon.setLayoutParams(params_5);
        params_5.addRule(RelativeLayout.ALIGN_PARENT_END);
        params_5.setMargins(0, 6, 0, 0);
        ImageView img1 = new ImageView(this);
        img1.setImageResource(R.drawable.circle_plus_icon);
        img1.setId(899);

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int countt = Integer.parseInt(arr.get(0).getTmp_url());
                ArrayList<SelectionModel> arr_new = new ArrayList<>();
                for (int l = 0; l < arr.size(); l++) {
                    SelectionModel mm = arr.get(l);
                    arr_new.add(new SelectionModel(mm.getTxt(), "", mm.getCode(), "", mm.getTmp_url()));
                }
                arrayList.add(new SelectionModel(String.valueOf(cardViewCount), arr_new));
                Log.v("arraylist_selection", arrayList.size() + "");
                LinearLayout rlays = lay_row.findViewById(countt);
                img1.setVisibility(View.INVISIBLE);
                rlays.addView(generateView(cardview.getId(), arr_new, arrayList, pos + 1, cardPos));
                // lay_row.addView(rlays);
            }
        });

        img1.setLayoutParams(params_5);
        rlay_icon.addView(img1);
        RelativeLayout.LayoutParams params_6 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //rlay_icon.setLayoutParams(params_5);

        params_6.addRule(RelativeLayout.LEFT_OF, img1.getId());
        params_6.setMargins(0, 6, 6, 0);
        ImageView img2 = new ImageView(this);
        img2.setImageResource(R.drawable.circle_minus_icon);
        img2.setId(500);

        //layoutparams_3.addRule(RelativeLayout.CENTER_VERTICAL);
        // layoutparams_3.setMargins(0,8,0,0);
        img2.setLayoutParams(params_6);
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int countt = Integer.parseInt(arr.get(0).getTmp_url());
                LinearLayout rlays = lay_row.findViewById(countt);
                Log.v("printing_pos_are ", pos + " end ");
                int pos = cardPos.indexOf(cardview.getId());
                Log.v("positon_vard", pos + " mock ");
                cardPos.remove(pos);
                rlays.removeView(cardview);
                arrayList.remove(pos);

                int cardCount = Integer.parseInt(arrayList.get(arrayList.size() - 1).getTxt());
                CardView card = rlays.findViewById(cardCount);
                RelativeLayout rlayay = card.findViewById(657);
                ImageView img = rlayay.findViewById(899);
                img.setVisibility(View.VISIBLE);


                // lay_row.addView(rlays);
            }
        });
        if (pos == 0) {
            img2.setVisibility(View.INVISIBLE);
        }
        rlay_icon.addView(img2);
        //lay.addView(edt1);
        lay.addView(rlay_icon);

        cardview.addView(lay);
        return cardview;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        customDialog.dismiss();
        if (type == 10) {
            txt_date.setText(myDataset.get(position).getName());
            Log.d("JSON_VALUE", myDataset.get(position).getId());
            DateTime = myDataset.get(position).getId();
            onMapReady(mGoogleMap);
            displayTravelMode(myDataset.get(position).getId());
        } else if (type == 11) {
            modeTextView.setText(myDataset.get(position).getName());
            Log.d("JSON_VALUE", myDataset.get(position).getName());
        } else if (type == 100) {
            String TrTyp = myDataset.get(position).getName();
            travelTypeMode.setText(TrTyp);

            if (TrTyp.equalsIgnoreCase("OS") == true) {
                callApi(DateForAPi, "OS");
                diverAllowanceLinear.setVisibility(View.VISIBLE);
            } else {
                callApi(DateForAPi, "");
                diverAllowanceLinear.setVisibility(View.GONE);
            }

            Log.d("JSON_VALUE", myDataset.get(position).getName());


        } else if (type == 1) {
            enterMode.setText(myDataset.get(position).getName());
            Log.d("JSON_VALUE", myDataset.get(position).getName());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (!txt_date.getText().toString().equals("")) {
            LatLng locationOne = new LatLng(13.1148, 80.2872);
            LatLng locationTwo = new LatLng(13.0300, 80.2421);

 /* BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.marker_icon);
 googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Old washermenpet"));
 googleMap.addMarker(new MarkerOptions().position(sydney2).title("Marker in Nandanam"));
 *//* googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*//*
             */
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationTwo, 15));
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
    }

    public class InputFilterMinMax implements InputFilter {

        private int min, max;

        public InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public InputFilterMinMax(String min, String max) {
            this.min = Integer.parseInt(min);
            this.max = Integer.parseInt(max);
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(dest.toString() + source.toString());
                if (isInRange(min, max, input))
                    return null;
            } catch (NumberFormatException nfe) {
            }
            return "";
        }

        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }
}
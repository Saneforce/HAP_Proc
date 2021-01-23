package com.hap.checkinproc.Activity_Hap;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Activity.Util.SelectionModel;
import com.hap.checkinproc.BuildConfig;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.Model_Class.ModeOfTravel;
import com.hap.checkinproc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.zelory.compressor.Compressor;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class On_Duty_Activity extends AppCompatActivity implements View.OnClickListener, Master_Interface {
    LinearLayout haplocationtext, purposeofvisittext, haplocationbutton, otherlocationbutton, submitbutton, closebutton, exitclose, ondutylocations;
    EditText purposeofvisitedittext, ondutyedittext;
    int flag;
    Common_Model Model_Pojo;
    List<Common_Model> getfieldforcehqlist = new ArrayList<>();
    TextView selecthaplocationss;
    CustomListViewDialog customDialog;
    String hapLocid;
    Gson gson;
    LinearLayout ModeOfTravel;

    /*AllowanceActivity*/
    RelativeLayout pic, rlay_pic, lay_km, lay_to, lay_From, lay_det, lay_fare;
    Uri outputFileUri;
    ImageView capture_img;
    CardView card_travel, card_to, card_typ;
    ApiInterface apiInterface;
    Button btn_submit, btn_ta;
    String filepath_final = "";
    String mode, url, hq_code, typ_code;
    EditText edt_km, edt_rmk, edt_frm, edt_to, edt_fare;
    TextView txt_mode, txt_hq, txt_typ;
    Common_Class common_class;
    boolean updateMode = false;
    SharedPreferences UserDetails;
    public static final String MyPREFERENCES = "MyPrefs";
    String SF_code = "", div = "";
    ArrayList<SelectionModel> array_hq = new ArrayList<>();
    RelativeLayout lay_hq, lay_typ;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1001;
    Shared_Common_Pref mShared_common_pref;





    /*OnDUTY ALlowance*/

    CardView ModeTravel, BusCardTo, cardHapLoaction;
    LinearLayout BikeMode, BusMode, ReasonPhoto, ProofImage;
    EditText StartKm, BusFrom, EditFare, EditRemarks;
    ImageView attachedImage;
    Button SubmitValue;
    TextView TextMode, TextToAddress;
    private ArrayList<String> temaplateList;
    Common_Model mCommon_model_spinner;
    List<Common_Model> listOrderType = new ArrayList<>();
    List<Common_Model> modelRetailDetails = new ArrayList<>();
    Shared_Common_Pref shared_common_pref;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String Name = "Allowance";
    public static final String MOT = "ModeOfTravel";
    public static final String MOC = "ModeOfCount";
    String imageURI = "", modeVal = "", StartedKM = "", FromKm = "", ToKm = "", Fare = "";
    public static final String CheckInfo = "CheckInDetail";
    public static final String UserInfo = "MyPrefs";
    List<com.hap.checkinproc.Model_Class.ModeOfTravel> modelOfTravel;
    Type userType;
    List<Common_Model> modelTravelType = new ArrayList<>();
    String startEnd = "";
    SharedPreferences CheckInDetails;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on__duty_);


        if (!checkPermission()) {
            requestPermissions();

        } else {

        }

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
                startActivity(new Intent(getApplicationContext(), Dashboard.class));

            }
        });


        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });
        gson = new Gson();
        haplocationtext = findViewById(R.id.haplocationtext);
        purposeofvisittext = findViewById(R.id.purposeofvisittext);
        otherlocationbutton = findViewById(R.id.otherlocationbutton);
        haplocationbutton = findViewById(R.id.haplocationbutton);
        ondutylocations = findViewById(R.id.ondutylocations);
        submitbutton = findViewById(R.id.submitbutton);
        selecthaplocationss = findViewById(R.id.selecthaplocationss);
        purposeofvisitedittext = findViewById(R.id.purposeofvisitedittext);
        ondutyedittext = findViewById(R.id.ondutyedittext);
        closebutton = findViewById(R.id.closebutton);
        exitclose = findViewById(R.id.exitclose);
        ModeOfTravel = findViewById(R.id.mode_of_travel);
        cardHapLoaction = findViewById(R.id.card_hap_loaction);
        cardHapLoaction.setOnClickListener(this);
        otherlocationbutton.setOnClickListener(this);
        haplocationbutton.setOnClickListener(this);
        submitbutton.setOnClickListener(this);
        closebutton.setOnClickListener(this);
        exitclose.setOnClickListener(this);
        GetfieldforceHq();
        onDutycreate();

        /*Allowance Activity*/

        common_class = new Common_Class(this);
        capture_img = findViewById(R.id.capture_img);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        btn_submit = findViewById(R.id.btn_submit);
        edt_km = findViewById(R.id.edt_km);
        edt_rmk = findViewById(R.id.edt_rmk);
        edt_frm = findViewById(R.id.edt_frm);

        edt_fare = findViewById(R.id.edt_fare);
        txt_mode = findViewById(R.id.txt_mode);
        txt_hq = findViewById(R.id.txt_hq);
        UserDetails = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SF_code = UserDetails.getString("Sfcode", "");
        div = UserDetails.getString("Divcode", "");


        mShared_common_pref = new Shared_Common_Pref(this);


    }


    public void onDutycreate() {
        shared_common_pref = new Shared_Common_Pref(this);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        gson = new Gson();
        CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
        UserDetails = getSharedPreferences(UserInfo, Context.MODE_PRIVATE);

        common_class = new Common_Class(this);
        ModeTravel = findViewById(R.id.card_travel_mode);
        BikeMode = findViewById(R.id.bike_mode);
        BusMode = findViewById(R.id.bus_mode);
        ReasonPhoto = findViewById(R.id.reason_photo);
        StartKm = findViewById(R.id.edt_km);
        BusFrom = findViewById(R.id.edt_frm);
        BusCardTo = findViewById(R.id.card_bus_mode);
        EditFare = findViewById(R.id.edt_fare);
        ProofImage = findViewById(R.id.proof_pic);
        attachedImage = findViewById(R.id.capture_img);
        EditRemarks = findViewById(R.id.edt_rmk);
        SubmitValue = findViewById(R.id.btn_submit);
        TextMode = findViewById(R.id.txt_mode);
        TextToAddress = findViewById(R.id.edt_to);


        BusToValue();

        ModeTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("MODE_Travel", "MODEL_Travel");
                modelTravelType.clear();
                dynamicMode();
            }
        });
        BusCardTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog = new CustomListViewDialog(On_Duty_Activity.this, modelRetailDetails, 10);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
            }
        });

        ProofImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(On_Duty_Activity.this, AllowancCapture.class);
             /*   intent.putExtra("allowance", "One");
                intent.putExtra("Mode", TextMode.getText().toString());
                intent.putExtra("Started", StartKm.getText().toString());
                intent.putExtra("FromKm", BusFrom.getText().toString());
                intent.putExtra("ToKm", TextToAddress.getText().toString());
                intent.putExtra("Fare", EditFare.getText().toString());*/
                startActivity(intent);
                finish();

            }
        });



/*
        if (sharedpreferences.contains("SharedImage")) {
            imageURI = sharedpreferences.getString("SharedImage", "");
            Log.e("Privacypolicy", "Checking" + imageURI);
        }
        if (sharedpreferences.contains("SharedFromKm")) {
            FromKm = sharedpreferences.getString("SharedFromKm", "");
            Log.e("Privacypolicy", "Checking" + FromKm);
        }
        if (sharedpreferences.contains("SharedToKm")) {
            ToKm = sharedpreferences.getString("SharedToKm", "");
            Log.e("Privacypolicy", "Checking" + ToKm);
        }
        if (sharedpreferences.contains("SharedFare")) {
            Fare = sharedpreferences.getString("SharedFare", "");
            Log.e("Privacypolicy", "Checking" + Fare);
        }
        if (sharedpreferences.contains("StartedKM")) {
            StartedKM = sharedpreferences.getString("StartedKM", "");
            Log.e("Privacypolicy", "Checking" + StartedKM);
        }

        if (sharedpreferences.contains("SharedMode")) {
            modeVal = sharedpreferences.getString("SharedMode", "");
            Log.e("Privacypolicy", "Checking" + modeVal);

            if (modeVal.equals("Bus")) {
                TextMode.setText(modeVal);
                BusMode.setVisibility(View.VISIBLE);
                BikeMode.setVisibility(View.GONE);
                ReasonPhoto.setVisibility(View.VISIBLE);
                BusFrom.setText(FromKm);
                TextToAddress.setText(ToKm);
                EditFare.setText(Fare);
                attachedImage.setImageURI(Uri.parse(imageURI));

            } else {
                TextMode.setText(modeVal);
                BusMode.setVisibility(View.GONE);
                BikeMode.setVisibility(View.VISIBLE);
                ReasonPhoto.setVisibility(View.VISIBLE);
                StartKm.setText(StartedKM);
                attachedImage.setImageURI(Uri.parse(imageURI));

            }
        }*/
    }


    public void BusToValue() {

        JSONObject jj = new JSONObject();
        try {
            jj.put("sfCode", shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code));
            jj.put("divisionCode", shared_common_pref.getvalue(Shared_Common_Pref.Div_Code));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> call = apiInterface.getBusTo(jj.toString());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray jsonArray = response.body();
                for (int a = 0; a < jsonArray.size(); a++) {
                    JsonObject jsonObject = (JsonObject) jsonArray.get(a);
                    updateMode = true;
                    String id = String.valueOf(jsonObject.get("id"));
                    String name = String.valueOf(jsonObject.get("name"));
                    String townName = String.valueOf(jsonObject.get("ODFlag"));
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
                    Model_Pojo = new Common_Model(id, name, "flag");
                    Log.e("LeaveType_Request", id);
                    Log.e("LeaveType_Request", name);
                    modelTravelType.add(Model_Pojo);
                }

                customDialog = new CustomListViewDialog(On_Duty_Activity.this, modelTravelType, 8);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.otherlocationbutton:
                flag = 1;
                ondutylocations.setVisibility(View.VISIBLE);
                purposeofvisittext.setVisibility(View.VISIBLE);
                closebutton.setVisibility(View.VISIBLE);
                exitclose.setVisibility(View.GONE);
                submitbutton.setVisibility(View.VISIBLE);
                otherlocationbutton.setVisibility(View.GONE);
                haplocationbutton.setVisibility(View.GONE);
                ModeOfTravel.setVisibility(View.VISIBLE);
                purposeofvisitedittext.setText("");
                selecthaplocationss.setText("");
                ondutyedittext.setText("");
                break;

            case R.id.haplocationbutton:
                flag = 0;
                ondutyedittext.setText("");
                selecthaplocationss.setText("");
                purposeofvisitedittext.setText("");
                haplocationtext.setVisibility(View.VISIBLE);
                purposeofvisittext.setVisibility(View.VISIBLE);
                submitbutton.setVisibility(View.VISIBLE);
                closebutton.setVisibility(View.VISIBLE);
                exitclose.setVisibility(View.GONE);
                ModeOfTravel.setVisibility(View.VISIBLE);
                ondutylocations.setVisibility(View.GONE);
                haplocationbutton.setVisibility(View.GONE);
                otherlocationbutton.setVisibility(View.GONE);
                break;

            case R.id.submitbutton:
                if (txt_mode.getText().toString().equals("Bike")) {
                    if (!filepath_final.equals("") && !edt_km.getText().toString().equals("")) {
                        if (vali()) {
                            submitValue();
                        }
                    } else {
                        Toast.makeText(On_Duty_Activity.this, "Please enter all the details", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (!filepath_final.equals("") && !edt_frm.getText().toString().equals("") && !txt_hq.getText().toString().equals("") && !edt_fare.getText().toString().equals("")) {
                        if (vali()) {
                            submitValue();
                        }
                    } else {
                        Toast.makeText(On_Duty_Activity.this, "Please enter all the details", Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case R.id.closebutton:
                haplocationtext.setVisibility(View.GONE);
                purposeofvisittext.setVisibility(View.GONE);
                submitbutton.setVisibility(View.GONE);
                closebutton.setVisibility(View.GONE);
                haplocationbutton.setVisibility(View.VISIBLE);
                exitclose.setVisibility(View.VISIBLE);
                otherlocationbutton.setVisibility(View.VISIBLE);
                ondutylocations.setVisibility(View.GONE);
                ModeOfTravel.setVisibility(View.GONE);
                break;
            case R.id.exitclose:
                startActivity(new Intent(this, Dashboard.class));
                break;
            case R.id.card_hap_loaction:
                customDialog = new CustomListViewDialog(On_Duty_Activity.this, getfieldforcehqlist, 1);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
                Log.e("On_Duty_Mode", "On_Duty_Mode");
                break;
        }
    }


    public void submitValue() {
        Intent intent = new Intent(this, Checkin.class);
        Bundle extras = new Bundle();
        extras.putString("ODFlag", String.valueOf(flag));
        extras.putString("Mode", "onduty");
        if (flag == 1) {
            extras.putString("onDutyPlcNm", ondutyedittext.getText().toString());
            extras.putString("onDutyPlcID", "0");
        } else {
            extras.putString("onDutyPlcNm", selecthaplocationss.getText().toString());
            extras.putString("onDutyPlcID", hapLocid);
        }
        extras.putString("vstPurpose", purposeofvisitedittext.getText().toString());
        intent.putExtras(extras);
        startActivity(intent);
    }

    @Override
    public void OnclickMasterType(java.util.List<Common_Model> myDataset, int position, int type) {
        customDialog.dismiss();
        selecthaplocationss.setText(myDataset.get(position).getName());
        hapLocid = String.valueOf(myDataset.get(position).getId());

        if (type == 8) {
            TextMode.setText(myDataset.get(position).getName());
            startEnd = myDataset.get(position).getId();
            Log.e("Mode_Count", startEnd);
            shared_common_pref.save("MC", startEnd);

            if (startEnd.equals("0")) {
                mode = "11";
                BikeMode.setVisibility(View.GONE);
                BusMode.setVisibility(View.VISIBLE);
                ReasonPhoto.setVisibility(View.VISIBLE);
                BusFrom.setText("");
                TextToAddress.setText("");
                EditFare.setText("");
                attachedImage.setImageURI(null);


            } else {
                mode = "12";
                BusMode.setVisibility(View.GONE);
                BikeMode.setVisibility(View.VISIBLE);
                ReasonPhoto.setVisibility(View.VISIBLE);
                StartKm.setText("");
                attachedImage.setImageURI(null);

            }

            sharedpreferences.edit().remove("SharedImage");
        } else if (type == 10) {
            TextToAddress.setText(myDataset.get(position).getName());
        }
    }

    public void GetfieldforceHq() {
        String commonLeaveType = "{\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<Object> call = service.getFieldForce_HQ(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, commonLeaveType);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                Log.e("ROUTE_MASTER_Object", String.valueOf(response.body().toString()));
                GetJsonData(new Gson().toJson(response.body()), "0");
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
            }
        });
    }

    private void GetJsonData(String jsonResponse, String type) {

        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String id = String.valueOf(jsonObject1.optInt("id"));
                String name = jsonObject1.optString("name");
                String flag = jsonObject1.optString("ODFlag");

                if (flag.equals("1")) {
                    Model_Pojo = new Common_Model(id, name, flag);
                    getfieldforcehqlist.add(Model_Pojo);
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean vali() {

        if (flag == 1 && (ondutyedittext.getText().toString() == null || ondutyedittext.getText().toString().isEmpty() || ondutyedittext.getText().toString().equalsIgnoreCase(""))) {
            Toast.makeText(this, "Enter the ON-Duty Location", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (flag == 0 && (selecthaplocationss.getText().toString() == null || selecthaplocationss.getText().toString().isEmpty() || selecthaplocationss.getText().toString().equalsIgnoreCase(""))) {
            Toast.makeText(this, "Select The HAP Location", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (purposeofvisitedittext.getText().toString() == null || purposeofvisitedittext.getText().toString().isEmpty() || purposeofvisitedittext.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Enter the Purpose of visit", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }


    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    /*common_class.CommonIntentwithFinish(Dashboard.class);*/
                    On_Duty_Activity.super.onBackPressed();
                }
            });


    @Override
    public void onBackPressed() {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12 && resultCode == Activity.RESULT_OK) {

            String finalPath = "/storage/emulated/0";
            String filePath = outputFileUri.getPath();
            filePath = filePath.substring(1);
            filePath = finalPath + filePath.substring(filePath.indexOf("/"));
            Log.v("printing__file_path", filePath);


            if (filePath == null)
                Toast.makeText(On_Duty_Activity.this, "This file format not supported", Toast.LENGTH_LONG).show();
            else {
                capture_img.setVisibility(View.VISIBLE);
                capture_img.setImageURI(outputFileUri);
                capture_img.setRotation((float) 90);
                filepath_final = filePath;
                mShared_common_pref.save("Started_Image", String.valueOf(outputFileUri));
            }

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
                            url = js.getString("url");
                            submitData();
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

    public void submitData() {

        mShared_common_pref.save("Started_km", edt_km.getText().toString());
        mShared_common_pref.save("mode_of_travel", txt_mode.getText().toString());
        mShared_common_pref.save("mode_of_travel", txt_mode.getText().toString());

        Log.e("MODE_OF_Travel", txt_mode.getText().toString());
        try {
            JSONObject jj = new JSONObject();
            jj.put("km", edt_km.getText().toString());
            jj.put("rmk", edt_rmk.getText().toString());
            jj.put("mod", mode);
            jj.put("sf", SF_code);
            jj.put("div", div);
            jj.put("url", url);
            jj.put("from", edt_frm.getText().toString());
            jj.put("to", edt_to.getText().toString());
            jj.put("fare", edt_fare.getText().toString());
            //saveAllowance
            Log.v("printing_allow", jj.toString());
            Call<ResponseBody> Callto;
            if (updateMode)
                Callto = apiInterface.updateAllowance(jj.toString());
            else
                Callto = apiInterface.saveAllowance(jj.toString());

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
                            JSONObject js = new JSONObject(jsonData);
                            if (js.getString("success").equalsIgnoreCase("true")) {
                                Toast.makeText(On_Duty_Activity.this, " Submitted successfully ", Toast.LENGTH_SHORT).show();
                                common_class.CommonIntentwithFinish(Dashboard.class);
                                //common_class.CommonIntentwithFinish(AllowanceActivityTwo.class);
                            } else
                                Toast.makeText(On_Duty_Activity.this, " Cannot submitted the data ", Toast.LENGTH_SHORT).show();
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


    private boolean checkPermission() {
        return (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    //Location service part
    private void requestPermissions() {
        boolean shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA);
        if (shouldProvideRationale) {
            Snackbar.make(
                    findViewById(R.id.activity_main),
                    R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(On_Duty_Activity.this,
                                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    })
                    .show();
        } else
            ActivityCompat.requestPermissions(On_Duty_Activity.this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length <= 0) {
                    // Permission was not granted.
                } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted.
                    //mLUService.requestLocationUpdates();
                } else {
                    // Permission denied.
                    Snackbar.make(
                            findViewById(R.id.activity_main),
                            R.string.permission_denied_explanation,
                            Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.settings, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Build intent that displays the App settings screen.
                                    Intent intent = new Intent();
                                    intent.setAction(
                                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package",
                                            BuildConfig.APPLICATION_ID, null);
                                    intent.setData(uri);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            })
                            .show();
                }
        }
    }

}
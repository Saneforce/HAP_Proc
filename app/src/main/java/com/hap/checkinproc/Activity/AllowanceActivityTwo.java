package com.hap.checkinproc.Activity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hap.checkinproc.Activity_Hap.AllowancCapture;
import com.hap.checkinproc.Activity_Hap.CustomListViewDialog;
import com.hap.checkinproc.Activity_Hap.Dashboard_Two;
import com.hap.checkinproc.Activity_Hap.ERT;
import com.hap.checkinproc.Activity_Hap.Help_Activity;
import com.hap.checkinproc.Activity_Hap.ImageCapture;
import com.hap.checkinproc.Activity_Hap.PayslipFtp;
import com.hap.checkinproc.Activity_Hap.ProductImageView;
import com.hap.checkinproc.Common_Class.CameraPermission;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.LocationEvents;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.R;
import com.hap.checkinproc.common.DatabaseHandler;
import com.hap.checkinproc.common.LocationFinder;
import com.hap.checkinproc.common.TimerService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import id.zelory.compressor.Compressor;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllowanceActivityTwo extends AppCompatActivity implements Master_Interface {

    TextView TextModeTravel, TextStartedKm, TextMaxKm, TextToPlace;
    ImageView StartedKmImage, EndedKmImage;
    Button takeEndedPhoto, submitAllowance;
    EditText EndedEditText, PersonalKmEdit, ReasonMode;
    Integer stKM = 0, endKm = 0, personalKM = 0, StratKm = 0, maxKM = 0, TotalKm = 0, totalPM = 0;
    SharedPreferences CheckInDetails, sharedpreferences, UserDetails;
    Shared_Common_Pref shared_common_pref;
    ApiInterface apiInterface;
    String Photo_Name = "", imageConvert = "", StartedKm = "", StartedImage = "", CLOSINGKM = "", EndedImage = "",
            CheckInfo = "CheckInDetail", UserInfo = "MyPrefs", MOT = "ModeOfTravel", Name = "Allowance",
            mypreference = "mypref", StrToCode = "", toPlace = "", TOKM = " ", cOUT = "", ImageStart = "",
            strImg = "", strMod = "", strKm = "", Hq = "", EdtReasn = "";
    LinearLayout linToPlace;
    CustomListViewDialog customDialog;
    Common_Model mCommon_model_spinner;
    List<Common_Model> modelRetailDetails = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allowance_two);
        startService(new Intent(this, TimerService.class));
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
        UserDetails = getSharedPreferences(UserInfo, Context.MODE_PRIVATE);
        TextModeTravel = findViewById(R.id.txt_mode_travel);
        TextStartedKm = findViewById(R.id.txt_started_km);
        TextMaxKm = findViewById(R.id.txt_max);
        StartedKmImage = findViewById(R.id.img_started_km);
        EndedEditText = findViewById(R.id.ended_km);
        EndedKmImage = findViewById(R.id.img_ended_km);
        takeEndedPhoto = findViewById(R.id.btn_take_photo);
        submitAllowance = findViewById(R.id.submit_allowance);
        PersonalKmEdit = findViewById(R.id.personal_ended_km);
        linToPlace = findViewById(R.id.lin_to);
        TextToPlace = findViewById(R.id.txt_to);
        ReasonMode = findViewById(R.id.reason_mode);
        shared_common_pref = new Shared_Common_Pref(this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        getToolbar();

        BusToValue();


        cOUT = String.valueOf(getIntent().getSerializableExtra("Mode"));

        if (!cOUT.equals("null") && !cOUT.equals("")) {
            callApi();
        }
        Log.v("Text_cOUT", cOUT);

        if (sharedpreferences.contains("Share_to_id")) {
            StrToCode = sharedpreferences.getString("Share_to_id", "");

            Log.v("Text_To_ID", StrToCode);
        }


        if (sharedpreferences.contains("Share_to")) {
            TOKM = sharedpreferences.getString("Share_to", "");
            TextToPlace.setText(TOKM);
            Log.v("Text_To_Place", TextToPlace.getText().toString());
        }

        if (sharedpreferences.contains("Share_Mot")) {
            strMod = sharedpreferences.getString("Share_Mot", "");
            TextModeTravel.setText(strMod);
            Log.e("COnvert", "imageConvert");

        }
        if (sharedpreferences.contains("Share_km")) {
            strKm = sharedpreferences.getString("Share_km", "");
            TextStartedKm.setText(strKm);
            Log.e("COnvert", "imageConvert");
        }

        if (sharedpreferences.contains("Share_Img")) {
            ImageStart = sharedpreferences.getString("Share_Img", "");
            Glide.with(getApplicationContext())
                    .load(ImageStart)
                    .into(StartedKmImage);
            Log.v("ImageStart", ImageStart);
        }


        if (sharedpreferences.contains("SharedImage")) {
            StartedImage = sharedpreferences.getString("SharedImage", "");
            Log.e("Privacypolicy", "Checking" + StartedImage);
            if (StartedImage != null && !StartedImage.isEmpty() && !StartedImage.equals("null")) {
                //StartedKmImage.setImageURI(Uri.parse(StartedImage));
            }
        }
        if (sharedpreferences.contains("SharedImages")) {
            EndedImage = sharedpreferences.getString("SharedImages", "");
            Log.e("Privacypolicy", "Checking" + EndedImage);
            EndedKmImage.setImageURI(Uri.parse(EndedImage));
            imageConvert = EndedImage.substring(7);
            Log.e("COnvert", EndedImage.substring(7));
            Log.e("COnvert", imageConvert);
            getMulipart(imageConvert, 0);

        }
        if (sharedpreferences.contains("StartedKM")) {
            StartedKm = sharedpreferences.getString("StartedKM", "");
            Log.e("Privacypolicy", "STARTRD      " + StartedKm);
        }


        if (sharedpreferences.contains("Closing")) {
            CLOSINGKM = sharedpreferences.getString("Closing", "");
            Log.e("Privacypolicy", "Checking" + CLOSINGKM);
            if (!CLOSINGKM.equals("")) {
                EndedEditText.setText(CLOSINGKM);
            }
        }


        if (sharedpreferences.contains("Share_reason")) {
            EdtReasn = sharedpreferences.getString("Share_reason", "");
            ReasonMode.setText(EdtReasn);
        }

        EndedEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (EndedEditText.getText().toString() != null && !EndedEditText.getText().toString().isEmpty() && !EndedEditText.getText().toString().equals("null")) {

                    try {
                        stKM = Integer.valueOf(StartedKm);
                    } catch (NumberFormatException ex) { // handle your exception

                    }
                    if (!EndedEditText.getText().toString().equals("")) {

                        try {
                            endKm = Integer.parseInt(EndedEditText.getText().toString());
                        } catch (NumberFormatException ex) { // handle your exception

                        }
                    }
                    Log.e("STARTED_KM", String.valueOf(endKm));
                    if (stKM < endKm) {

                        Log.e("STARTED_KM", "GREATER");
                    } else {
                        Log.e("STARTED_KM", "Not GREATER");
                    }
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        PersonalKmEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!EndedEditText.getText().toString().equals("")) {
                    totalPM = Integer.valueOf((EndedEditText.getText().toString())) - Integer.valueOf((TextStartedKm.getText().toString()));
                    Log.v("TOTAL_KM_INSIDe", String.valueOf(totalPM));

                    if (totalPM > 0)
                        PersonalKmEdit.setFilters(new InputFilter[]{new Common_Class.InputFilterMinMax(0, totalPM)});
                }
            }
        });


        if (!EndedImage.matches("")) {
            EndedKmImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), ProductImageView.class);
                    intent.putExtra("ImageUrl", EndedImage);
                    startActivity(intent);
                }
            });

            if (!ImageStart.matches("")) {
                StartedKmImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), ProductImageView.class);
                        intent.putExtra("ImageUrl", ImageStart);
                        startActivity(intent);

                    }
                });
            }
        }


        takeEndedPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CameraPermission cameraPermission = new CameraPermission(AllowanceActivityTwo.this, getApplicationContext());

                if (!cameraPermission.checkPermission()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        cameraPermission.requestPermission();
                    }
                    Log.v("PERMISSION_NOT", "PERMISSION_NOT");
                } else {
                    Log.v("PERMISSION", "PERMISSION");
                    Log.v("ImageStart_ONCLICK", ImageStart);

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("Closing", EndedEditText.getText().toString());
                    editor.putString("Share_to", TextToPlace.getText().toString());
                    editor.putString("Share_Mot", TextModeTravel.getText().toString());
                    editor.putString("Share_km", TextStartedKm.getText().toString());
                    editor.putString("Share_Img", ImageStart);
                    editor.putString("Share_to_id", StrToCode);
                    editor.putString("Share_reason", ReasonMode.getText().toString());
                    editor.commit();

                    Intent intent = new Intent(AllowanceActivityTwo.this, AllowancCapture.class);
                    intent.putExtra("allowance", "Two");
                    startActivity(intent);

                }

            }
        });


        submitAllowance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (EndedEditText.getText().toString().matches("")) {
                    Toast.makeText(AllowanceActivityTwo.this, "Choose End Km", Toast.LENGTH_SHORT).show();
                    return;
                } else if (EndedImage.matches("")) {
                    Toast.makeText(AllowanceActivityTwo.this, "Choose End photo", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    try {
                        stKM = Integer.valueOf(TextStartedKm.getText().toString());
                    } catch (NumberFormatException ex) { // handle your exception

                    }
                    endKm = Integer.valueOf(String.valueOf(EndedEditText.getText().toString()));

                    Log.e("START_KM", String.valueOf(stKM));
                    Log.e("End_KM", String.valueOf(endKm));


                    if (stKM < endKm) {

                        new LocationFinder(getApplication(), new LocationEvents() {
                            @Override
                            public void OnLocationRecived(Location location) {
                                submitData();
                            }
                        });

                    } else {
                        Toast.makeText(AllowanceActivityTwo.this, "Should be greater then Started Km", Toast.LENGTH_SHORT).show();

                    }
                }

            }
        });

        linToPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog = new CustomListViewDialog(AllowanceActivityTwo.this, modelRetailDetails, 10);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
            }
        });

    }

    /*Submit*/
    public void submitData() {

        Log.e("PERSONAL_KM", PersonalKmEdit.getText().toString());

        if (PersonalKmEdit.getText().toString().equals("")) {

        } else {
            personalKM = Integer.valueOf(PersonalKmEdit.getText().toString());
        }

        try {
            JSONObject jj = new JSONObject();
            jj.put("km", EndedEditText.getText().toString());
            jj.put("rmk", ReasonMode.getText().toString());
            jj.put("pkm", personalKM);
            jj.put("mod", "11");
            jj.put("sf", shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code));
            jj.put("div", shared_common_pref.getvalue(Shared_Common_Pref.Div_Code));
            jj.put("url", Photo_Name);
            jj.put("from", "");
            jj.put("to", TextToPlace.getText().toString());
            jj.put("to_code", StrToCode);
            jj.put("fare", "");
            jj.put("Activity_Date", Common_Class.GetDate());
            //saveAllowance
            Log.v("printing_allow", jj.toString());
            Call<ResponseBody> Callto;

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            DatabaseHandler db = new DatabaseHandler(this);
            JSONArray locations=db.getAllPendingTrackDetails();
            if(locations.length()>0){
                try {
                    SharedPreferences UserDetails = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    if (UserDetails.getString("Sfcode", "") != "") {
                        Call<JsonObject> call = apiInterface.JsonSave("save/trackall", "3", UserDetails.getString("Sfcode", ""), "", "", locations.toString());
                        call.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                // Get result Repo from response.body()
                                db.deleteAllTrackDetails();
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                Log.d("LocationUpdate", "onFailure Local Location");
                            }
                        });
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Callto = apiInterface.updateAllowance(jj.toString());

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
                                Toast.makeText(AllowanceActivityTwo.this, " Submitted successfully ", Toast.LENGTH_SHORT).show();

                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.remove(Name);
                                editor.remove(MOT);
                                editor.remove("SharedImage");
                                editor.remove("Sharedallowance");
                                editor.remove("SharedMode");
                                editor.remove("StartedKM");
                                editor.remove("SharedFromKm");
                                editor.remove("SharedToKm");
                                editor.remove("SharedFare");
                                editor.remove("SharedImages");
                                editor.remove("Closing");
                                shared_common_pref.clear_pref(Shared_Common_Pref.DAMode);

                                Intent takePhoto = new Intent(AllowanceActivityTwo.this, ImageCapture.class);
                                takePhoto.putExtra("Mode", "COUT");
                                startActivity(takePhoto);
                            } else
                                Toast.makeText(AllowanceActivityTwo.this, " Cannot submitted the data ", Toast.LENGTH_SHORT).show();
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

                    String id = String.valueOf(jsonObject.get("id"));
                    String name = String.valueOf(jsonObject.get("name"));
                    String townName = String.valueOf(jsonObject.get("ODFlag"));
                    name = name.replaceAll("^[\"']+|[\"']+$", "");
                    id = id.replaceAll("^[\"']+|[\"']+$", "");
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
                startActivity(new Intent(getApplicationContext(), PayslipFtp.class));
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
                openHome();
            }
        });
    }

    public void openHome() {
        Intent Dashboard = new Intent(AllowanceActivityTwo.this, Dashboard_Two.class);
        Dashboard.putExtra("Mode", "CIN");
        startActivity(Dashboard);
    }


    public void callApi() {

        try {
            JSONObject jj = new JSONObject();
            jj.put("div", Shared_Common_Pref.Div_Code);
            jj.put("sf", Shared_Common_Pref.Sf_Code);
            jj.put("rSF", Shared_Common_Pref.Sf_Code);
            jj.put("State_Code", Shared_Common_Pref.StateCode);
            jj.put("Activity_Date", Common_Class.GetDate());
            Log.v("json_obj_ta", jj.toString());
            Call<ResponseBody> Callto = apiInterface.getStartKmDetails(jj.toString());
            Callto.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.isSuccessful()) {
                            String jsonData = null;
                            jsonData = response.body().string();
                            Log.v("response_data", jsonData);
                            JSONObject js = new JSONObject(jsonData);
                            JSONArray jsnArValue = js.getJSONArray("StartDetails");
                            for (int i = 0; i < jsnArValue.length(); i++) {
                                JSONObject json_oo = jsnArValue.getJSONObject(i);
                                TextModeTravel.setText(json_oo.getString("MOT_Name"));
                                TextStartedKm.setText(json_oo.getString("Start_Km"));
                                Glide.with(getApplicationContext())
                                        .load(json_oo.getString("start_Photo"))
                                        .into(StartedKmImage);
                                maxKM = json_oo.getInt("Maxkm");
                                Hq = json_oo.getString("dailyAllowance");

                                //   TextMaxKm.setText("Maximum km : " + maxKM);
                                StratKm = Integer.valueOf(json_oo.getString("Start_Km"));
                                ImageStart = json_oo.getString("start_Photo");
                                StrToCode = json_oo.getString("To_Place_Id");
                                TextToPlace.setText(json_oo.getString("To_Place"));
                                TotalKm = StratKm + maxKM;

                                Log.v("START_KM", String.valueOf(StratKm));
                                Log.v("ToTAL_KM", String.valueOf(TotalKm));

                                /* EndedEditText.setFilters(new InputFilter[]{new Common_Class.InputFilterMinMax(1, TotalKm)});*/

                                if (!json_oo.getString("start_Photo").matches("")) {
                                    StartedKmImage.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getApplicationContext(), ProductImageView.class);
                                            try {
                                                intent.putExtra("ImageUrl", json_oo.getString("start_Photo"));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            startActivity(intent);
                                        }
                                    });
                                }
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

    public void getMulipart(String path, int x) {
        MultipartBody.Part imgg = convertimg("file", path);
        HashMap<String, RequestBody> values = field(UserDetails.getString("Sfcode", ""));
        CallApiImage(values, imgg, x);
    }

    public HashMap<String, RequestBody> field(String val) {
        HashMap<String, RequestBody> xx = new HashMap<String, RequestBody>();
        xx.put("data", createFromString(val));
        return xx;
    }

    private RequestBody createFromString(String txt) {
        return RequestBody.create(MultipartBody.FORM, txt);
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

    public void CallApiImage(HashMap<String, RequestBody> values, MultipartBody.Part imgg, final int x) {
        Call<ResponseBody> Callto;

        Callto = apiInterface.uploadkmimg(values, imgg);

        Callto.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.v("print_upload_file", "ggg" + response.isSuccessful() + response.body());

                try {
                    if (response.isSuccessful()) {

                        Log.v("print_upload_file_true", "ggg" + response);
                        JSONObject jb = null;
                        String jsonData = null;
                        jsonData = response.body().string();
                        Log.v("request_data_upload", String.valueOf(jsonData));
                        JSONObject js = new JSONObject(jsonData);
                        if (js.getString("success").equalsIgnoreCase("true")) {
                            Photo_Name = js.getString("url");
                            Log.v("printing_dynamic_cou", js.getString("url"));
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

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        customDialog.dismiss();
        if (type == 10) {
            TextToPlace.setText(myDataset.get(position).getName());
            toPlace = myDataset.get(position).getName();
            StrToCode = myDataset.get(position).getId();
            Log.e("STRTOCOD", StrToCode);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(new Intent(this, TimerService.class));
        Log.v("LOG_IN_LOCATION", "ONRESTART");
    }

    @Override
    protected void onPause() {
        super.onPause();
        startService(new Intent(this, TimerService.class));
        Log.v("LOG_IN_LOCATION", "ONRESTART");
    }

    @Override
    protected void onStop() {
        super.onStop();
        startService(new Intent(this, TimerService.class));
        Log.v("LOG_IN_LOCATION", "ONRESTART");
    }

    @Override
    protected void onStart() {
        super.onStart();
        startService(new Intent(this, TimerService.class));
        Log.v("LOG_IN_LOCATION", "ONRESTART");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startService(new Intent(this, TimerService.class));
    }
}
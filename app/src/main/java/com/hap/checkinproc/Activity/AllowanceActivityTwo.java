package com.hap.checkinproc.Activity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.hap.checkinproc.Activity_Hap.AllowancCapture;
import com.hap.checkinproc.Activity_Hap.Dashboard_Two;
import com.hap.checkinproc.Activity_Hap.ERT;
import com.hap.checkinproc.Activity_Hap.Help_Activity;
import com.hap.checkinproc.Activity_Hap.ImageCapture;
import com.hap.checkinproc.Activity_Hap.ProductImageView;
import com.hap.checkinproc.Common_Class.CameraPermission;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import id.zelory.compressor.Compressor;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllowanceActivityTwo extends AppCompatActivity {

    TextView TextModeTravel, TextStartedKm, TextMaxKm;
    ImageView StartedKmImage, EndedKmImage;
    Button takeEndedPhoto, submitAllowance;
    EditText EndedEditText, PersonalKmEdit;
    Integer stKM = 0, endKm = 0, personalKM = 0, StratKm = 0, maxKM = 0, TotalKm = 0;
    SharedPreferences CheckInDetails, sharedpreferences, UserDetails;
    Shared_Common_Pref shared_common_pref;
    ApiInterface apiInterface;
    String Photo_Name = "", imageConvert = "", StartedKm = "", StartedImage = "", CLOSINGKM = "", EndedImage = "",
            CheckInfo = "CheckInDetail", UserInfo = "MyPrefs", MOT = "ModeOfTravel", Name = "Allowance", mypreference = "mypref";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allowance_two);
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
        shared_common_pref = new Shared_Common_Pref(this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        getToolbar();
        callApi();

        if (sharedpreferences.contains("SharedImage")) {
            StartedImage = sharedpreferences.getString("SharedImage", "");
            Log.e("Privacypolicy", "Checking" + StartedImage);
            if (StartedImage != null && !StartedImage.isEmpty() && !StartedImage.equals("null")) {
                //   StartedKmImage.setImageURI(Uri.parse(StartedImage));
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


        if (!EndedImage.matches("")) {
            EndedKmImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), ProductImageView.class);
                    intent.putExtra("ImageUrl", EndedImage);
                    startActivity(intent);
                }
            });
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
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("Closing", EndedEditText.getText().toString());
                    editor.commit();
                    Intent intent = new Intent(AllowanceActivityTwo.this, AllowancCapture.class);
                    intent.putExtra("allowance", "Two");
                    startActivity(intent);
                    finish();
                }

            }
        });


        submitAllowance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (EndedEditText.getText().toString().matches("")) {
                    Toast.makeText(AllowanceActivityTwo.this, "Enter End KM", Toast.LENGTH_SHORT).show();
                    return;
                } else if (EndedImage.matches("")) {
                    Toast.makeText(AllowanceActivityTwo.this, "Enter End KM", Toast.LENGTH_SHORT).show();
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
                        submitData();
                    } else {
                        Toast.makeText(AllowanceActivityTwo.this, "Should be greater then Started Km", Toast.LENGTH_SHORT).show();

                    }
                }

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
            jj.put("rmk", EndedEditText.getText().toString());
            jj.put("pkm", personalKM);
            jj.put("mod", "11");
            jj.put("sf", shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code));
            jj.put("div", shared_common_pref.getvalue(Shared_Common_Pref.Div_Code));
            jj.put("url", Photo_Name);
            jj.put("from", "");
            jj.put("to", "");
            jj.put("fare", "");
            //saveAllowance
            Log.v("printing_allow", jj.toString());
            Call<ResponseBody> Callto;
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

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

                            Log.v("print_upload_file_true", "ggg" + response);
                            String jsonData = null;
                            jsonData = response.body().string();
                            Log.v("response_data", jsonData);
                            JSONObject js = new JSONObject(jsonData);
                            JSONArray jsnArValue = js.getJSONArray("StartDetails");
                            for (int i = 0; i < jsnArValue.length(); i++) {
                                JSONObject json_oo = jsnArValue.getJSONObject(i);
                                TextModeTravel.setText(json_oo.getString("MOT_Name"));
                                TextStartedKm.setText(json_oo.getString("Start_Km"));
                                maxKM = json_oo.getInt("Maxkm");
                                //
                                //   TextMaxKm.setText("Maximum km : " + maxKM);
                                Glide.with(getApplicationContext())
                                        .load(json_oo.getString("start_Photo"))
                                        .into(StartedKmImage);
                                StratKm = Integer.valueOf(json_oo.getString("Start_Km"));


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

}
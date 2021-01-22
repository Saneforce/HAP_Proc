package com.hap.checkinproc.Activity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hap.checkinproc.Activity_Hap.AllowancCapture;
import com.hap.checkinproc.Activity_Hap.Dashboard;
import com.hap.checkinproc.Activity_Hap.Dashboard_Two;
import com.hap.checkinproc.Activity_Hap.ERT;
import com.hap.checkinproc.Activity_Hap.Help_Activity;
import com.hap.checkinproc.Activity_Hap.ImageCapture;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllowanceActivityTwo extends AppCompatActivity {
    String ModeOfTravel = "", StartedKm = "", StartedImage = "", CLOSINGKM = "", EndedImage = "";
    Shared_Common_Pref mShared_common_pref;
    TextView TextModeTravel, TextStartedKm;
    ImageView StartedKmImage, EndedKmImage;
    Button takeEndedPhoto, submitAllowance;
    EditText EndedEditText, PersonalKmEdit;
    Integer stKM = 0, endKm = 0;
    String EndImageURi = " ";
    public static final String mypreference = "mypref";
    public static final String Name = "Allowance";
    public static final String MOT = "ModeOfTravel";
    SharedPreferences sharedpreferences;
    SharedPreferences CheckInDetails;
    SharedPreferences UserDetails;
    public static final String CheckInfo = "CheckInDetail";
    public static final String UserInfo = "MyPrefs";
    Shared_Common_Pref shared_common_pref;
    Integer personalKM = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allowance_two);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
        UserDetails = getSharedPreferences(UserInfo, Context.MODE_PRIVATE);
        TextModeTravel = findViewById(R.id.txt_mode_travel);
        TextStartedKm = findViewById(R.id.txt_started_km);
        StartedKmImage = findViewById(R.id.img_started_km);
        EndedEditText = findViewById(R.id.ended_km);
        EndedKmImage = findViewById(R.id.img_ended_km);
        takeEndedPhoto = findViewById(R.id.btn_take_photo);
        submitAllowance = findViewById(R.id.submit_allowance);
        PersonalKmEdit = findViewById(R.id.personal_ended_km);
        shared_common_pref = new Shared_Common_Pref(this);
        getToolbar();

        if (sharedpreferences.contains("SharedMode")) {
            ModeOfTravel = sharedpreferences.getString("SharedMode", "");
            Log.e("Privacypolicy", "Checking" + ModeOfTravel);
            TextModeTravel.setText(ModeOfTravel);
        }

        if (sharedpreferences.contains("SharedImage")) {
            StartedImage = sharedpreferences.getString("SharedImage", "");
            Log.e("Privacypolicy", "Checking" + StartedImage);
            if (StartedImage != null && !StartedImage.isEmpty() && !StartedImage.equals("null")) {
                StartedKmImage.setImageURI(Uri.parse(StartedImage));

            }

        }
        if (sharedpreferences.contains("SharedImages")) {
            EndedImage = sharedpreferences.getString("SharedImages", "");
            Log.e("Privacypolicy", "Checking" + EndedImage);
            EndedKmImage.setImageURI(Uri.parse(EndedImage));
        }
        if (sharedpreferences.contains("StartedKM")) {
            StartedKm = sharedpreferences.getString("StartedKM", "");
            Log.e("Privacypolicy", "STARTRD      " + StartedKm);
            TextStartedKm.setText(StartedKm);
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
                    stKM = Integer.valueOf(StartedKm);
                    if (!EndedEditText.getText().toString().equals("")) {
                        endKm = Integer.valueOf(String.valueOf(EndedEditText.getText()));
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


        takeEndedPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("Closing", EndedEditText.getText().toString());
                editor.commit();
                Intent intent = new Intent(AllowanceActivityTwo.this, AllowancCapture.class);
                intent.putExtra("allowance", "Two");
                startActivity(intent);
                finish();

            }
        });


        submitAllowance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* if (!EndImageURi.equals("") || EndImageURi != null || !EndedEditText.getText().toString().equals("")) {
                 *//*  SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.remove(Name);
                    editor.remove(MOT);
                    editor.commit();*//*
                    stKM = Integer.valueOf(StartedKm);
                    endKm = Integer.valueOf(String.valueOf(EndedEditText.getText().toString()));
                    Log.e("STARTED_KM", String.valueOf(endKm));
                    if (stKM < endKm) {
                       *//* if (EndedEditText.getText().toString().matches("") || EndedImage.matches("")) {
                            Toast.makeText(AllowanceActivityTwo.this, "Enter details", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            submitData();
                        }*//*

                    } else {
                        Toast.makeText(AllowanceActivityTwo.this, "Should be greater then Started Km", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(AllowanceActivityTwo.this, "Please enter all the details", Toast.LENGTH_SHORT).show();
                }
*/


                if (EndedEditText.getText().toString().matches("") || EndedImage.matches("")) {
                    Toast.makeText(AllowanceActivityTwo.this, "Enter details", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    stKM = Integer.valueOf(StartedKm);
                    endKm = Integer.valueOf(String.valueOf(EndedEditText.getText().toString()));
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
            jj.put("url", "url");
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
                                editor.commit();
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
        Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
        Shared_Common_Pref.Sf_Code = UserDetails.getString("Sfcode", "");
        Shared_Common_Pref.Sf_Name = UserDetails.getString("SfName", "");
        Shared_Common_Pref.Div_Code = UserDetails.getString("Divcode", "");
        Shared_Common_Pref.StateCode = UserDetails.getString("State_Code", "");
        if (CheckIn == true) {
            Intent Dashboard = new Intent(AllowanceActivityTwo.this, Dashboard_Two.class);
            Dashboard.putExtra("Mode", "CIN");
            startActivity(Dashboard);
        } else
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
    }

}
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
import androidx.cardview.widget.CardView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hap.checkinproc.Activity_Hap.AllowancCapture;
import com.hap.checkinproc.Activity_Hap.CustomListViewDialog;
import com.hap.checkinproc.Activity_Hap.Dashboard;
import com.hap.checkinproc.Activity_Hap.Dashboard_Two;
import com.hap.checkinproc.Activity_Hap.ERT;
import com.hap.checkinproc.Activity_Hap.Help_Activity;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AllowanceActivity extends AppCompatActivity implements View.OnClickListener, Master_Interface {
    CardView ModeTravel, BusCardTo;
    LinearLayout BikeMode, BusMode, ReasonPhoto, ProofImage;
    EditText StartKm, BusFrom, EditFare, EditRemarks;
    ImageView attachedImage;
    Button SubmitValue;
    TextView TextMode, TextToAddress;
    private ArrayList<String> temaplateList;
    Common_Model mCommon_model_spinner;
    List<Common_Model> listOrderType = new ArrayList<>();
    List<Common_Model> modelRetailDetails = new ArrayList<>();
    CustomListViewDialog customDialog;
    Shared_Common_Pref shared_common_pref;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String Name = "Allowance";
    public static final String MOT = "ModeOfTravel";
    String mode = "", imageURI = "", modeVal = "", StartedKM = "", FromKm = "", ToKm = "", Fare = "";
    Boolean updateMode = false;
    Common_Class common_class;
    SharedPreferences CheckInDetails;
    SharedPreferences UserDetails;
    public static final String CheckInfo = "CheckInDetail";
    public static final String UserInfo = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allowance);
        shared_common_pref = new Shared_Common_Pref(this);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);


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
        getToolbar();
        BusToValue();

        ModeTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listOrderType.clear();
                ModeOfTravel();
            }
        });
        BusCardTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog = new CustomListViewDialog(AllowanceActivity.this, modelRetailDetails, 10);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();
            }
        });

        ProofImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AllowanceActivity.this, AllowancCapture.class);
                intent.putExtra("allowance", "One");
                intent.putExtra("Mode", TextMode.getText().toString());
                intent.putExtra("Started", StartKm.getText().toString());
                intent.putExtra("FromKm", BusFrom.getText().toString());
                intent.putExtra("ToKm", TextToAddress.getText().toString());
                intent.putExtra("Fare", EditFare.getText().toString());
                startActivity(intent);
                finish();

            }
        });

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
        }

        SubmitValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mode.equals("11")) {
                    /*BUS*/
                    if (BusFrom.getText().toString().matches("") || TextMode.getText().toString().matches("") || EditFare.getText().toString().matches("") || imageURI.matches("")) {
                        Toast.makeText(AllowanceActivity.this, "Enter details", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        submitData();
                    }

                } else {
                    /*BIKE*/

                    if (StartKm.getText().toString().matches("") || imageURI.matches("")) {
                        Toast.makeText(AllowanceActivity.this, "Enter details", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        submitData();
                    }
                }

            }
        });

       // dynamicMode();
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
            Intent Dashboard = new Intent(AllowanceActivity.this, Dashboard_Two.class);
            Dashboard.putExtra("Mode", "CIN");
            startActivity(Dashboard);
        } else
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
    }

    public void ModeOfTravel() {
        temaplateList = new ArrayList<>();
        temaplateList.add("Bike");
        temaplateList.add("Bus");

        for (int i = 0; i < temaplateList.size(); i++) {
            String id = String.valueOf(temaplateList.get(i));
            String name = temaplateList.get(i);
            mCommon_model_spinner = new Common_Model(id, name, "flag");
            listOrderType.add(mCommon_model_spinner);
        }
        customDialog = new CustomListViewDialog(AllowanceActivity.this, listOrderType, 9);
        Window window = customDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        customDialog.show();
    }


    /*Choosing Dynamic date*/
    public void dynamicMode() {

        JSONObject jj = new JSONObject();
        try {
            jj.put("sfCode", shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code));
            jj.put("divisionCode", shared_common_pref.getvalue(Shared_Common_Pref.Div_Code));
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


    /*Submit*/
    public void submitData() {

        shared_common_pref.save("Started_km", StartKm.getText().toString());
        shared_common_pref.save("mode_of_travel", TextMode.getText().toString());
        String n = "True";
        String Mode = TextMode.getText().toString();
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Name, n);
        editor.putString(MOT, Mode);
        editor.commit();

        Log.e("MODE_OF_Travel", TextMode.getText().toString());
        try {
            JSONObject jj = new JSONObject();
            jj.put("km", StartKm.getText().toString());
            jj.put("rmk", StartKm.getText().toString());
            jj.put("mod", mode);
            jj.put("sf", shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code));
            jj.put("div", shared_common_pref.getvalue(Shared_Common_Pref.Div_Code));
            jj.put("url", "url");
            jj.put("from", BusFrom.getText().toString());
            jj.put("to", TextToAddress.getText().toString());
            jj.put("fare", EditFare.getText().toString());
            //saveAllowance
            Log.v("printing_allow", jj.toString());
            Call<ResponseBody> Callto;
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
      /*      if (updateMode)
                Callto = apiInterface.updateAllowance(jj.toString());
            else*/
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
                            Log.v("response_data", String.valueOf(updateMode));
                            JSONObject js = new JSONObject(jsonData);
                            if (js.getString("success").equalsIgnoreCase("true")) {
                                Toast.makeText(AllowanceActivity.this, " Submitted successfully ", Toast.LENGTH_SHORT).show();

                                common_class.CommonIntentwithFinish(Dashboard.class);
                                // common_class.CommonIntentwithFinish(AllowanceActivityTwo.class);
                            } else
                                Toast.makeText(AllowanceActivity.this, " Cannot submitted the data ", Toast.LENGTH_SHORT).show();
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
    public void onClick(View v) {

    }

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        customDialog.dismiss();
        if (type == 9) {
            TextMode.setText(myDataset.get(position).getName());
            if (TextMode.getText().toString().equals("Bus")) {
                mode = "11";
                BikeMode.setVisibility(View.GONE);
                BusMode.setVisibility(View.VISIBLE);
                ReasonPhoto.setVisibility(View.VISIBLE);
                BusFrom.setText("");
                TextToAddress.setText("");
                EditFare.setText("");
                attachedImage.setImageURI(null);
                sharedpreferences.edit().remove("SharedImage");

            } else {
                mode = "12";
                BusMode.setVisibility(View.GONE);
                BikeMode.setVisibility(View.VISIBLE);
                ReasonPhoto.setVisibility(View.VISIBLE);
                StartKm.setText("");
                attachedImage.setImageURI(null);

            }

        } else if (type == 10) {
            TextToAddress.setText(myDataset.get(position).getName());
        }
    }

}
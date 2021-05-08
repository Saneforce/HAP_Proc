package com.hap.checkinproc.Activity_Hap;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Common_Class.CameraPermission;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.Model_Class.ModeOfTravel;
import com.hap.checkinproc.R;
import com.hap.checkinproc.common.LocationReceiver;
import com.hap.checkinproc.common.SANGPSTracker;
import com.hap.checkinproc.common.TimerService;

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
    Button haplocationbutton, otherlocationbutton, submitbutton, closebutton, exitclose;
    EditText purposeofvisitedittext, ondutyedittext;
    int flag;
    Common_Model Model_Pojo;
    List<Common_Model> getfieldforcehqlist = new ArrayList<>();
    TextView selecthaplocationss;
    CheckBox chkHlyDyFlg;
    CustomListViewDialog customDialog;
    String hapLocid;
    Gson gson;
    LinearLayout ModeOfTravel, haplocationtext, purposeofvisittext, ondutylocations, linearBus, lincheck;

    /*AllowanceActivity*/
    Uri outputFileUri;
    ImageView capture_img;
    ApiInterface apiInterface;
    Button btn_submit;
    String filepath_final = "";
    String mode;
    Common_Class common_class;
    boolean updateMode = false;
    SharedPreferences UserDetails;
    String SF_code = "", div = "";
    Shared_Common_Pref mShared_common_pref;

    private SANGPSTracker mLUService;
    private boolean mBound = false;
    private LocationReceiver myReceiver;
    String checking = "";
    String count = "";

    /*OnDUTY ALlowance*/

    CardView ModeTravel, BusCardTo, cardHapLoaction;
    LinearLayout BikeMode, BusMode, ReasonPhoto, ProofImage;
    EditText StartKm, onDutyFrom, EditRemarks;
    ImageView attachedImage;
    Button SubmitValue;
    TextView TextMode, TextToAddress, dailyAllowance;
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
    String startEnd = "", ModeTravelType = "";
    SharedPreferences CheckInDetails;
    String modeId = "";
    /*Shared Prefernce*/

    public static final String hapLocation = "hpLoc";
    public static final String otherLocation = "othLoc";
    public static final String visitPurpose = "vstPur";
    public static final String modeTravelId = "ShareModesss";
    public static final String modeTypeVale = "SharedModeTypeValesss";
    public static final String modeFromKm = "SharedFromKmsss";
    public static final String modeToKm = "SharedToKmsss";
    public static final String StartedKm = "StartedKMsss";

    CheckBox driverAllowance;
    LinearLayout linCheckdriver;
    String strHapLocation = "", strVisitPurpose = "";
    String imageConvert = "", imageServer = "";
    String DriverNeed = "false", DriverMode = "", strDailyAllowance = "", StrID = "";

    CardView CardDailyAllowance;
    private ArrayList<String> travelTypeList;
    String driverAllowanceBoolean = "", StrToCode = "", STRCode = "";
    String OnDutyCount = "";


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on__duty_);
        startService(new Intent(this, TimerService.class));
        shared_common_pref = new Shared_Common_Pref(this);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        dynamicMode(1);
        CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
        UserDetails = getSharedPreferences(UserInfo, Context.MODE_PRIVATE);
        common_class = new Common_Class(this);
        GetfieldforceHq();
        lincheck = findViewById(R.id.lin_mode);
        driverAllowance = findViewById(R.id.da_driver_allowance);
        linCheckdriver = findViewById(R.id.lin_check_driver);
        chkHlyDyFlg = findViewById(R.id.chkHlyDyFlg);
        SF_code = UserDetails.getString("Sfcode", "");
        div = UserDetails.getString("Divcode", "");
        dailyAllowance = findViewById(R.id.text_daily_allowance);
        linearBus = findViewById(R.id.lin_bus);

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
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
            }
        });

        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                common_class.CommonIntentwithFinish(Dashboard.class);
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


        Log.v("ON_Duty_COUNT", String.valueOf(OnDutyCount));
        TextMode = findViewById(R.id.txt_mode);
        TextToAddress = findViewById(R.id.on_duty_to);
        onDutyFrom = findViewById(R.id.on_duty_from);
        StartKm = findViewById(R.id.on_duty_start);
        CardDailyAllowance = findViewById(R.id.card_daily_allowance);

        ModeTravel = findViewById(R.id.card_travel_modes);
        BikeMode = findViewById(R.id.bike_modes);
        BusMode = findViewById(R.id.bus_modes);
        ReasonPhoto = findViewById(R.id.reason_photos);
        BusToValue();

        BusCardTo = findViewById(R.id.card_bus_modes);

        ProofImage = findViewById(R.id.proof_pics);
        attachedImage = findViewById(R.id.capture_imgs);
        EditRemarks = findViewById(R.id.edt_rmk);
        SubmitValue = findViewById(R.id.btn_submit);

        cardHapLoaction.setOnClickListener(this);
        otherlocationbutton.setOnClickListener(this);
        haplocationbutton.setOnClickListener(this);

        if (sharedpreferences.contains("Onduty")) {
            OnDutyCount = sharedpreferences.getString("Onduty", "");
            Log.v("ON_DUTY_COUNT", OnDutyCount);
        }

        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             /*   if (!OnDutyCount.equals("0")) {
                    Log.v("sdsadsadsa", "dsadsadsa");
                    if (!selecthaplocationss.getText().toString().equals("")) {
                        //haplocation
                        Log.v("HAP_LOACTION", "hggh" + selecthaplocationss.getText().toString());
                        if (selecthaplocationss.getText().toString().matches("")) {
                            Log.v("HAP_LOACTION", "Enter Hap Location");
                            Toast.makeText(On_Duty_Activity.this, "Enter  Location", Toast.LENGTH_SHORT).show();
                        } else if (purposeofvisitedittext.getText().toString().matches("")) {
                            Log.v("HAP_LOACTION", "Enter Visit");
                            Toast.makeText(On_Duty_Activity.this, "Enter Visit purpose", Toast.LENGTH_SHORT).show();
                        } else {
                            submitData();
                        }
                    } else {
                        if (ondutyedittext.getText().toString().matches("")) {
                            Toast.makeText(On_Duty_Activity.this, "Enter  Location", Toast.LENGTH_SHORT).show();
                        } else if (purposeofvisitedittext.getText().toString().matches("")) {
                            Toast.makeText(On_Duty_Activity.this, "Enter Visit purpose", Toast.LENGTH_SHORT).show();
                        } else {
                            submitData();
                        }

                    }
                } else {

*/
                if (!selecthaplocationss.getText().toString().equals("")) {
                    //haplocation
                    Log.v("HAP_LOACTION", "hggh" + selecthaplocationss.getText().toString());
                    if (selecthaplocationss.getText().toString().matches("")) {
                        Log.v("HAP_LOACTION", "Enter Hap Location");
                        Toast.makeText(On_Duty_Activity.this, "Enter  Location", Toast.LENGTH_SHORT).show();
                    } else if (purposeofvisitedittext.getText().toString().matches("")) {
                        Log.v("HAP_LOACTION", "Enter Visit");
                        Toast.makeText(On_Duty_Activity.this, "Enter Visit purpose", Toast.LENGTH_SHORT).show();
                    } else if (TextMode.getText().toString().matches("")) {
                        Log.v("HAP_LOACTION", startEnd + "Enter Mode");
                        Toast.makeText(On_Duty_Activity.this, "Enter Mode", Toast.LENGTH_SHORT).show();
                    } else {
                        if (startEnd.equals("0") || count.equals("0")) {

                            if (dailyAllowance.getText().toString().matches("")) {
                                Log.v("HAP_LOACTION", startEnd + "Enter Daily");
                                Log.v("HAP_LOACTION", count + "ssssEnter Daily");
                                Toast.makeText(On_Duty_Activity.this, "Enter Daily Allowance", Toast.LENGTH_SHORT).show();
                            } else if (onDutyFrom.getText().toString().matches("")) {
                                Log.v("HAP_LOACTION", startEnd + "Enter From");
                                Log.v("HAP_LOACTION", count + "Enter From");
                                Toast.makeText(On_Duty_Activity.this, "Enter From", Toast.LENGTH_SHORT).show();
                            } else {
                                submitData();
                            }
                        } else if (startEnd.equals("1") || count.equals("1")) {
                            if (dailyAllowance.getText().toString().matches("")) {
                                Log.v("HAP_LOACTION", startEnd + "Enter Daily");
                                Log.v("HAP_LOACTION", count + "sssEnter Daily");
                                Toast.makeText(On_Duty_Activity.this, "Enter Daily Allowance", Toast.LENGTH_SHORT).show();
                            } else if (onDutyFrom.getText().toString().matches("")) {
                                Log.v("HAP_LOACTION", startEnd + "Enter From");
                                Log.v("HAP_LOACTION", count + "Enter From");
                                Toast.makeText(On_Duty_Activity.this, "Enter From", Toast.LENGTH_SHORT).show();
                            } else if (StartKm.getText().toString().matches("")) {
                                Log.v("HAP_LOACTION", startEnd + "Enter Start km");
                                Log.v("HAP_LOACTION", count + "Enter Start km");
                                Toast.makeText(On_Duty_Activity.this, "Enter Start Km", Toast.LENGTH_SHORT).show();
                            } else if (imageURI.matches("")) {
                                Log.v("HAP_LOACTION", startEnd + "Enter Start");
                                Toast.makeText(On_Duty_Activity.this, "Choose Start Photo", Toast.LENGTH_SHORT).show();
                            } else {
                                submitData();
                            }

                        } else {
                            submitData();
                        }

                    }


                } else {
                    //OtherLocation
                    if (ondutyedittext.getText().toString().matches("")) {
                        Toast.makeText(On_Duty_Activity.this, "Enter  Location", Toast.LENGTH_SHORT).show();
                    } else if (purposeofvisitedittext.getText().toString().matches("")) {
                        Toast.makeText(On_Duty_Activity.this, "Enter Visit purpose", Toast.LENGTH_SHORT).show();
                    } else if (TextMode.getText().toString().matches("")) {
                        Toast.makeText(On_Duty_Activity.this, "Enter Mode", Toast.LENGTH_SHORT).show();
                    } else {
                        if (startEnd.equals("0") || count.equals("0")) {

                            if (dailyAllowance.getText().toString().matches("")) {
                                Log.v("HAP_LOACTION", startEnd + "Enter Daily");
                                Log.v("HAP_LOACTION", count + "ccEnter Daily");
                                Toast.makeText(On_Duty_Activity.this, "Enter Daily Allowance", Toast.LENGTH_SHORT).show();
                            } else if (onDutyFrom.getText().toString().matches("")) {
                                Log.v("HAP_LOACTION", startEnd + "Enter From");
                                Log.v("HAP_LOACTION", count + "Enter From");
                                Toast.makeText(On_Duty_Activity.this, "Enter From", Toast.LENGTH_SHORT).show();
                            } else {
                                submitData();
                            }
                        } else if (startEnd.equals("1") || count.equals("1")) {
                            if (dailyAllowance.getText().toString().matches("")) {
                                Log.v("HAP_LOACTION", startEnd + "Enter Daily");
                                Log.v("HAP_LOACTION", count + "cEnter Daily");
                                Toast.makeText(On_Duty_Activity.this, "Enter Daily Allowance", Toast.LENGTH_SHORT).show();
                            } else if (onDutyFrom.getText().toString().matches("")) {
                                Log.v("HAP_LOACTION", startEnd + "Enter From");
                                Log.v("HAP_LOACTION", count + "Enter From");
                                Toast.makeText(On_Duty_Activity.this, "Enter From", Toast.LENGTH_SHORT).show();
                            } else if (StartKm.getText().toString().matches("")) {
                                Log.v("HAP_LOACTION", startEnd + "Enter Start km");
                                Log.v("HAP_LOACTION", count + "Enter Start km");
                                Toast.makeText(On_Duty_Activity.this, "Enter Start Km", Toast.LENGTH_SHORT).show();
                            } else if (imageURI.matches("")) {
                                Log.v("HAP_LOACTION", startEnd + "Enter Start");
                                Toast.makeText(On_Duty_Activity.this, "Choose Start Photo", Toast.LENGTH_SHORT).show();
                            } else {
                                submitData();
                            }
                        } else {
                            submitData();
                        }
                    }
                }
                /*   }*/
            }
        });
        closebutton.setOnClickListener(this);
        exitclose.setOnClickListener(this);

        CardDailyAllowance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listOrderType.clear();
                OrderType();
            }
        });

        driverAllowance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.e("LOGGGGGG", "LOGGGGGGGGGGGGGGGG");
                    driverAllowanceBoolean = "true";
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("SharedDriverAllowancess", driverAllowanceBoolean);
                    editor.commit();


                } else {
                    driverAllowanceBoolean = "false";
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("SharedDriverAllowancess", driverAllowanceBoolean);
                    editor.commit();
                    DriverNeed = "";
                }
            }
        });
        /*Allowance Activity*/


        capture_img = findViewById(R.id.capture_img);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        btn_submit = findViewById(R.id.btn_submit);
        mShared_common_pref = new Shared_Common_Pref(this);
        ModeTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("MODE_Travel", "MODEL_Travel");
                modelTravelType.clear();
                dynamicMode(0);
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


                CameraPermission cameraPermission = new CameraPermission(On_Duty_Activity.this, getApplicationContext());


                if (!cameraPermission.checkPermission()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        cameraPermission.requestPermission();
                        Log.v("PERMISSION_NOT", "PERMISSION_NOT");
                    }
                    Log.v("PERMISSION_NOT", "PERMISSION_NOT");
                } else {

                    Log.v("PERMISSION_CHECK", startEnd);
                    SharedPreferences.Editor ed = sharedpreferences.edit();
                    ed.putString(visitPurpose, purposeofvisitedittext.getText().toString());
                    ed.putString(otherLocation, ondutyedittext.getText().toString());
                    ed.putString(modeTravelId, startEnd);
                    ed.putString(modeTypeVale, TextMode.getText().toString());
                    ed.putString(modeFromKm, onDutyFrom.getText().toString());
                    ed.putString(modeToKm, TextToAddress.getText().toString());
                    ed.putString(StartedKm, StartKm.getText().toString());
                    ed.putString("SharedDailyAllowancess", dailyAllowance.getText().toString());
                    ed.putString("SharedDriverss", DriverMode);
                    ed.putString("ShareModeIDs", modeId);
                    ed.putString("StoreId", StrToCode);
                    ed.putString(hapLocation, selecthaplocationss.getText().toString());
                    ed.commit();

                    Intent intent = new Intent(On_Duty_Activity.this, AllowancCapture.class);
                    intent.putExtra("allowance", "three");
                    startActivity(intent);
                }

            }
        });

        strHapLocation = selecthaplocationss.getText().toString();

        attachedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ProductImageView.class);
                intent.putExtra("ImageUrl", imageURI);
                startActivity(intent);

            }
        });

/*        Log.v("HAP_ON_DUTY1", String.valueOf(OnDutyCount));
        if (!OnDutyCount.equals("0")) {
            Log.v("HAP_ON_DUTY2", String.valueOf(OnDutyCount));
            lincheck.setVisibility(View.GONE);
        } else {*/
        lincheck.setVisibility(View.VISIBLE);
        Log.v("HAP_ON_DUTY3", "CHECJ" + OnDutyCount);
        if (sharedpreferences.contains(hapLocation)) {
            strHapLocation = sharedpreferences.getString(hapLocation, "");
            //
            Log.e("FlagCountFlagCount", strHapLocation);
            if (!strHapLocation.equals("")) {
                flag = 0;
                selecthaplocationss.setText(strHapLocation);
                ondutyedittext.setText("");
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
            }
            Log.e("strHapLocation", strHapLocation);
        }
        if (sharedpreferences.contains(otherLocation)) {
            strVisitPurpose = sharedpreferences.getString(otherLocation, "");
            Log.e("FlagCountFlagCount", strVisitPurpose);
            if (!strVisitPurpose.matches("")) {
                flag = 0;
                ondutyedittext.setText(strVisitPurpose);
                ondutylocations.setVisibility(View.VISIBLE);
                purposeofvisittext.setVisibility(View.VISIBLE);
                closebutton.setVisibility(View.VISIBLE);
                exitclose.setVisibility(View.GONE);
                haplocationtext.setVisibility(View.GONE);
                submitbutton.setVisibility(View.VISIBLE);
                otherlocationbutton.setVisibility(View.GONE);
                haplocationbutton.setVisibility(View.GONE);
                ModeOfTravel.setVisibility(View.VISIBLE);
            }
            Log.e("strHapLocation", strHapLocation);
        }
        if (sharedpreferences.contains("SharedImage")) {
            imageURI = sharedpreferences.getString("SharedImage", "");
            Log.e("Privacypolicy", "Checking" + imageURI);
            imageConvert = imageURI.substring(7);
            Log.e("COnvert", imageURI.substring(7));
            Log.e("COnvert", imageConvert);
            getMulipart(imageConvert, 0);
            Log.e("IMAGE_URI", imageURI);
        }

        if (sharedpreferences.contains("SharedDailyAllowancess")) {
            strDailyAllowance = sharedpreferences.getString("SharedDailyAllowancess", "");
            Log.e("strDailyAllowance", "Checking" + strDailyAllowance);
            if (strDailyAllowance.equals("HQ")) {
                linearBus.setVisibility(View.GONE);
            } else {
                linearBus.setVisibility(View.VISIBLE);
            }
        }
        if (sharedpreferences.contains("SharedDriverAllowancess")) {
            DriverNeed = sharedpreferences.getString("SharedDriverAllowancess", "");
            Log.e("DriverNeed", DriverNeed);
        }
        if (sharedpreferences.contains(visitPurpose)) {
            purposeofvisitedittext.setText(sharedpreferences.getString(visitPurpose, ""));
            Log.e("DriverNeed", DriverNeed);
        }
        if (sharedpreferences.contains(modeFromKm)) {
            FromKm = sharedpreferences.getString(modeFromKm, "");
            Log.e("Mode_FROm", sharedpreferences.getString(modeFromKm, ""));
        }

        if (sharedpreferences.contains(modeToKm)) {
            ToKm = sharedpreferences.getString(modeToKm, "");
            Log.e("Mode_To", sharedpreferences.getString(modeToKm, ""));
        }
        if (sharedpreferences.contains("StoreId")) {
            StrToCode = sharedpreferences.getString("StoreId", "");
            Log.e("Mode_To", sharedpreferences.getString("StoreId", ""));

        }

        if (sharedpreferences.contains(StartedKm)) {
            StartedKM = sharedpreferences.getString(StartedKm, "");
            Log.e("Mode_END", sharedpreferences.getString(StartedKm, ""));
        }

        if (sharedpreferences.contains(modeTravelId)) {
            startEnd = sharedpreferences.getString(modeTravelId, "");
            Log.e("Mode_STARTEND", sharedpreferences.getString(startEnd, ""));
        }

        if (sharedpreferences.contains("ShareModeIDs")) {
            modeId = sharedpreferences.getString("ShareModeIDs", "");
            Log.e("Privacypolicy", "StrID" + modeId);
        }

        if (sharedpreferences.contains(modeTypeVale)) {
            modeVal = sharedpreferences.getString(modeTypeVale, "");
            Log.e("Mode_TYPE", sharedpreferences.getString(modeTypeVale, ""));

            if (ModeTravelType.equals("0")) {
                BusMode.setVisibility(View.VISIBLE);
                BikeMode.setVisibility(View.GONE);
                ReasonPhoto.setVisibility(View.VISIBLE);
                StartKm.setText(StartedKM);
                onDutyFrom.setText(FromKm);
                TextToAddress.setText(ToKm);
                TextMode.setText(modeVal);
                dailyAllowance.setText(strDailyAllowance);
                attachedImage.setImageURI(Uri.parse(imageURI));
                attachedImage.setRotation(90);

            } else {
                BusMode.setVisibility(View.VISIBLE);
                BikeMode.setVisibility(View.VISIBLE);
                ReasonPhoto.setVisibility(View.VISIBLE);
                StartKm.setText(StartedKM);
                onDutyFrom.setText(FromKm);
                TextToAddress.setText(ToKm);
                TextMode.setText(modeVal);
                dailyAllowance.setText(strDailyAllowance);
                attachedImage.setImageURI(Uri.parse(imageURI));
                attachedImage.setRotation(90);
                if (TextMode.getText().equals("Four Wheeler")) {
                    linCheckdriver.setVisibility(View.VISIBLE);
                    if (DriverNeed.equals("true")) {
                        driverAllowance.setChecked(true);
                    } else {
                        driverAllowance.setChecked(false);
                    }
                }
            }
            if (attachedImage.getDrawable() == null) {
                Log.e("Image_Draw_able", "Null_Image_View");
            } else {
                Log.e("Image_Draw_able", "Not_Null_Image_View");
            }
        }

    }

    /*    }*/


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
        customDialog = new CustomListViewDialog(On_Duty_Activity.this, listOrderType, 100);
        Window window = customDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        customDialog.show();

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
                if (jsonArray.size() != 0) {
                    for (int a = 0; a < jsonArray.size(); a++) {
                        JsonObject jsonObject = (JsonObject) jsonArray.get(a);
                        updateMode = true;
                        String id = String.valueOf(jsonObject.get("id"));
                        String name = String.valueOf(jsonObject.get("name"));
                        String townName = String.valueOf(jsonObject.get("ODFlag"));
                        name = name.replaceAll("^[\"']+|[\"']+$", "");
                        id = id.replaceAll("^[\"']+|[\"']+$", "");
                        mCommon_model_spinner = new Common_Model(id, name, "");
                        Log.v("get/fieldforce_hq", id);
                        modelRetailDetails.add(mCommon_model_spinner);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("LeaveTypeList", "Error");
            }
        });
    }


    public void dynamicMode(Integer note) {

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
                modelTravelType.clear();
                userType = new TypeToken<ArrayList<ModeOfTravel>>() {
                }.getType();
                modelOfTravel = gson.fromJson(new Gson().toJson(response.body()), userType);
                for (int i = 0; i < modelOfTravel.size(); i++) {
                    String id = String.valueOf(modelOfTravel.get(i).getStEndNeed());
                    String name = modelOfTravel.get(i).getName();
                    String modeId = String.valueOf(modelOfTravel.get(i).getId());
                    String driverMode = String.valueOf(modelOfTravel.get(i).getDriverNeed());
                    Model_Pojo = new Common_Model(id, name, modeId, driverMode);
                    Log.e("LeaveType_Request", id);
                    Log.e("LeaveType_Request", name);
                    modelTravelType.add(Model_Pojo);

                }
                if (note == 0) {
                    customDialog = new CustomListViewDialog(On_Duty_Activity.this, modelTravelType, 8);
                    customDialog.setCanceledOnTouchOutside(false);
                    Window window = customDialog.getWindow();
                    window.setGravity(Gravity.CENTER);
                    window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    customDialog.show();
                }
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
                Log.v("ON_DUTY_LOCATION", String.valueOf(flag));
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
                strHapLocation = "";


                break;

            case R.id.haplocationbutton:
                flag = 0;
                Log.v("ON_DUTY_LOCATION", String.valueOf(flag));
                if (sharedpreferences.contains(hapLocation)) {
                    strHapLocation = sharedpreferences.getString(hapLocation, "");
                    selecthaplocationss.setText("");
                    ondutyedittext.setText("");
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
                } else {
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


    @Override
    public void OnclickMasterType(java.util.List<Common_Model> myDataset, int position, int type) {
        customDialog.dismiss();
        if (type == 8) {
            TextMode.setText(myDataset.get(position).getName());
            startEnd = myDataset.get(position).getId();

            count = myDataset.get(position).getId();
            Log.e("Dash_Mode_Count", startEnd);
            shared_common_pref.save("MC", startEnd);
            modeId = myDataset.get(position).getFlag();
            DriverMode = myDataset.get(position).getCheckouttime();
            Log.e("modeId", modeId);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("ModeCount", startEnd);
            editor.commit();

            if (startEnd.equals("0")) {
                mode = "11";
                BikeMode.setVisibility(View.GONE);
                BusMode.setVisibility(View.VISIBLE);
                ReasonPhoto.setVisibility(View.GONE);
                StartKm.setText("");
                onDutyFrom.setText("");
                TextToAddress.setText("");


            } else {
                mode = "12";
                BikeMode.setVisibility(View.VISIBLE);
                BusMode.setVisibility(View.VISIBLE);
                ReasonPhoto.setVisibility(View.VISIBLE);

                StartKm.setText("");
                onDutyFrom.setText("");
                TextToAddress.setText("");

            }
            attachedImage.setImageResource(0);
            StartKm.setText("");
            if (attachedImage.getDrawable() == null) {
                Log.e("Image_Draw_able", "Null_Image_View");
            } else {
                Log.e("Image_Draw_able", "Not_Null_Image_View");
            }
            Log.e("IMAGE_URI", imageURI);


            if (DriverMode.equals("1")) {
                linCheckdriver.setVisibility(View.VISIBLE);
            } else {
                linCheckdriver.setVisibility(View.GONE);
            }

            DriverNeed = "";
            driverAllowance.setChecked(false);

        } else if (type == 10) {
            TextToAddress.setText(myDataset.get(position).getName());
            StrToCode = myDataset.get(position).getId();
            Log.e("StrToCode", StrToCode);
            SharedPreferences.Editor ed = sharedpreferences.edit();
            ed.putString("StoreId", StrToCode);
            ed.commit();

        } else if (type == 1) {

            selecthaplocationss.setText(myDataset.get(position).getName());
            hapLocid = String.valueOf(myDataset.get(position).getId());

            SharedPreferences.Editor editors;
            editors = sharedpreferences.edit();
            editors.putString("placeName", myDataset.get(position).getName());
            editors.putString("placeId", myDataset.get(position).getId());
            editors.commit();


        } else if (type == 100) {
            String TrTyp = myDataset.get(position).getName();
            dailyAllowance.setText(TrTyp);
            if (TrTyp.equals("HQ")) {
                linearBus.setVisibility(View.GONE);
            } else {
                linearBus.setVisibility(View.VISIBLE);
            }
            TextToAddress.setText("");
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
        HashMap<String, RequestBody> values = field(UserDetails.getString("Sfcode", ""));
        Log.e("IMAGE_URI_1", path);
        Log.e("IMAGE_URI_1", String.valueOf(imgg));
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
                File file;
                file = new File(path);
                if (path.contains(".png") || path.contains(".jpg") || path.contains(".jpeg"))
                    file = new Compressor(getApplicationContext()).compressToFile(file);
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
                            imageServer = js.getString("url");
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

    public void submitData() {
        String n = "True";
        String Mode = TextMode.getText().toString();
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Name, n);
        editor.putString(MOT, Mode);
        editor.putString("MdeTpID", startEnd);
        editor.commit();
        Log.e("STRCodeSTRCode", startEnd);
        // STRCode = STRCode.replace("^[\"']+|[\"']+$", "");
        try {
            JSONObject jj = new JSONObject();
            jj.put("hap_location", selecthaplocationss.getText().toString());
            jj.put("visit_purpose", purposeofvisitedittext.getText().toString());
            jj.put("other_loaction", ondutyedittext.getText().toString());
            jj.put("mode_allowance", "OnDuty");
            jj.put("km", StartKm.getText().toString());
            jj.put("rmk", StartKm.getText().toString());
            jj.put("mode_name", TextMode.getText().toString());
            jj.put("mod", modeId);
            jj.put("sf", SF_code);
            jj.put("div", div);
            jj.put("StEndNeed", startEnd);
            jj.put("url", imageServer);
            jj.put("from", onDutyFrom.getText().toString());
            jj.put("to", TextToAddress.getText().toString());
            jj.put("to_code", StrToCode);
            jj.put("dailyAllowance", dailyAllowance.getText().toString());
            jj.put("driverAllowance", DriverNeed);

            //saveAllowance
            Log.v("printing_allow", jj.toString());
            Call<ResponseBody> Callto;
            Callto = apiInterface.saveAllowance(jj.toString());

            Log.v("ONDUTY_TEXXT_REQ", Callto.request().toString());

            Callto.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.isSuccessful()) {
                            Log.v("ONDUTY_TEXXT_RES", Callto.request().toString());
                            Log.v("print_upload_file_true", "ggg" + response);
                            JSONObject jb = null;
                            String jsonData = null;
                            jsonData = response.body().string();
                            Log.v("response_data", jsonData);
                            JSONObject js = new JSONObject(jsonData);
                            if (js.getString("success").equalsIgnoreCase("true")) {
                                Toast.makeText(On_Duty_Activity.this, " Submitted successfully ", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Checkin.class);
                                Bundle extras = new Bundle();
                                extras.putString("ODFlag", String.valueOf(flag));
                                extras.putString("Mode", "onduty");
                                if (flag == 1) {
                                    extras.putString("onDutyPlcNm", ondutyedittext.getText().toString());
                                    extras.putString("onDutyPlcID", "0");
                                    extras.putString("onDuty", "abc");
                                } else {
                                    extras.putString("onDutyPlcNm", selecthaplocationss.getText().toString());
                                    extras.putString("onDutyPlcID", hapLocid);
                                    extras.putString("onDuty", "cba");
                                }
                                extras.putString("HolidayFlag", (chkHlyDyFlg.isChecked()) ? "1" : "0");
                                extras.putString("vstPurpose", purposeofvisitedittext.getText().toString());
                                intent.putExtras(extras);
                                shared_common_pref.save(Shared_Common_Pref.DAMode, true);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString("VSTP",purposeofvisitedittext.getText().toString());
                                editor.commit();
                                mLUService = new SANGPSTracker(On_Duty_Activity.this);
                                myReceiver = new LocationReceiver();
                                bindService(new Intent(On_Duty_Activity.this, SANGPSTracker.class), mServiceConection,
                                        Context.BIND_AUTO_CREATE);
                                LocalBroadcastManager.getInstance(On_Duty_Activity.this).registerReceiver(myReceiver,
                                        new IntentFilter(SANGPSTracker.ACTION_BROADCAST));
                                mLUService.requestLocationUpdates();
                                startActivity(intent);
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

    @Override
    protected void onResume() {
        super.onResume();
        startService(new Intent(this, TimerService.class));
        Log.v("LOG_IN_LOCATION", "ONRESTART");
        checking = String.valueOf(getIntent().getSerializableExtra("CHECKING"));
        Log.v("CHECKING_DATA", checking);

        if (sharedpreferences.contains("SharedImage")) {
            imageURI = sharedpreferences.getString("SharedImage", "");
            Log.e("Privacypolicy", "Checking" + imageURI);

            imageConvert = imageURI.substring(7);
            Log.e("COnvert", imageURI.substring(7));
            Log.e("COnvert", imageConvert);
            getMulipart(imageConvert, 0);
            attachedImage.setImageURI(Uri.parse(imageURI));
            Log.e("IMAGE_URI", imageURI);
        }


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

    private final ServiceConnection mServiceConection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mLUService = ((SANGPSTracker.LocationBinder) service).getLocationUpdateService(getApplicationContext());
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mLUService = null;
            mBound = false;
        }
    };
}
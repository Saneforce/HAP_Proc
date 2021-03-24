package com.hap.checkinproc.Activity;

import android.animation.ArgbEvaluator;
import android.animation.LayoutTransition;
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
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Activity.Util.ImageFilePath;
import com.hap.checkinproc.Activity.Util.SelectionModel;
import com.hap.checkinproc.Activity_Hap.AttachementActivity;
import com.hap.checkinproc.Activity_Hap.CustomListViewDialog;
import com.hap.checkinproc.Activity_Hap.Dashboard;
import com.hap.checkinproc.Activity_Hap.Dashboard_Two;
import com.hap.checkinproc.Activity_Hap.ERT;
import com.hap.checkinproc.Activity_Hap.Help_Activity;
import com.hap.checkinproc.Activity_Hap.MapZoomIn;
import com.hap.checkinproc.Activity_Hap.ProductImageView;
import com.hap.checkinproc.Common_Class.CameraPermission;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.Model_Class.ModeOfTravel;
import com.hap.checkinproc.R;
import com.hap.checkinproc.common.TimerService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import id.zelory.compressor.Compressor;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TAClaimActivity extends AppCompatActivity implements Master_Interface,
        OnMapReadyCallback {

    SharedPreferences CheckInDetails;
    public static final String mypreference = "mypref";
    public static final String Name = "Allowance";
    public static final String MOT = "ModeOfTravel";
    public static final String SKM = "Started_km";
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences UserDetails;
    Shared_Common_Pref mShared_common_pref;
    SharedPreferences TaSharedPrefernce;
    Common_Model mCommon_model_spinner;
    Common_Model Model_Pojo;
    Common_Class mCommon_class;
    GoogleMap mGoogleMap;
    ApiInterface apiInterface;
    Uri outputFileUri;

    LinearLayout Dynamicallowance, OtherExpense, localTotal, otherExpenseLayout, linAll, linRemarks,
            linFareAmount, ldg_typ_sp, linLocalSpinner, linOtherSpinner, lodgCont, lodgContvw, ldg_stayloc, ldg_stayDt,
            lodgJoin, ldgEAra, ldgMyEAra, JNLdgEAra, drvldgEAra, jointLodging, vwBoarding, vwDrvBoarding,
            linAddAllowance, diverAllowanceLinear, LDailyAllowance, LOtherExpense, LLocalConve, LinearOtherAllowance,
            linlocalCon, linBusMode, linBikeMode, linMode, travelDynamicLoaction, linDailyAllowance, linback, lin,
            linImgPrv, TotalDays, stayDays;

    CardView card_date, TravelBike, crdDynamicLocation, ldg_ara;

    TextView txt_date, txt_ldg_type, TxtStartedKm, TxtClosingKm, modeTextView, travelTypeMode,
            TotalTravelledKm, txtBusFrom, txtBusTo, txtTaClaim, PersonalTextKM, PersonalKiloMeter,
            txtDailyAllowance, editText, ldg_cin, ldg_cout, txtJNName, txtJNDesig, txtJNDept, txtJNHQ, txtJNMob,
            lblHdBill, lblHdBln, ldgWOBBal, ldgAdd, txtJNMyEli, txtMyEligi, txtDrivEligi, lbl_ldg_eligi, txt_totDA,
            fuelAmount, TextTotalAmount, editTexts, oeEditext, localText, OeText, grandTotal, txtallamt, txt_BrdAmt,
            txt_DrvBrdAmt, txtJointAdd, txtJNEligi, txtTAamt, txtDesig, txtDept, txtEmpId, txtName, oeTxtUKey, oeTxtUKeys,
            lcTxtUKey, lcTxtUKeys, tvTxtUKey, tvTxtUKeys, txtMaxKm, txtDrvrBrod, txtStyDays, txtLodgUKey;

    EditText enterMode, enterFrom, enterTo, enterFare, etrTaFr, etrTaTo, editTextRemarks, editLaFare, edtOE, edt, edt1, edt_ldg_JnEmp,
            edt_ldg_bill, edtLcFare, lodgStyLocation;

    ImageView deleteButton, previewss, taAttach, lcAttach, oeAttach, lcPreview, oePreview, endkmimage, startkmimage,
            img_lodg_prvw, img_lodg_atta, mapZoomIn, imgBck;

    String SF_code = "", div = "", State_Code = "", StartedKm = "", ClosingKm = "", ModeOfTravel = "", PersonalKm = "",
            DriverNeed = "", DateForAPi = "", DateTime = "", shortName = "", Exp_Name = "", Id = "", userEnter = "",
            attachment = "", maxAllowonce = "", strRetriveType = "", StrToEnd = "", StrBus = "", StrTo = "", StrDaName = "",
            OEdynamicLabel = "", strFuelAmount = "", StrModeValue = "", dynamicLabel = "", StrDailyAllowance = "", ldgEmpName = "",
            witOutBill = "", ValCd = "", fullPath = "", filePath = "", editMode = "", allowanceAmt = "", myldgEliAmt = "", myBrdEliAmt = "",
            drvldgEliAmt = "", drvBrdEliAmt = "", strGT = "", totLodgAmt = "", start_Image = "", End_Imge = "", finalPath = "",
            attach_Count = "", ImageURl = "", keyEk = "EK", oeEditCnt = "", lcEditcnt = "", tvEditcnt = "", OeUKey = "",
            LcUKey = "", TlUKey = "", lcUKey = "", oeUKey = "", ImageUKey = "", taAmt = "", stayTotal = "", lodUKey = "",
            tominYear = "", tominMonth = "", tominDay = "";

    Integer totalkm = 0, totalPersonalKm = 0, Pva, C = 0, S = 0, editTextPositionss,
            oePosCnt = 0, lcPosCnt = 0, tvSize = 0, ttLod = 0;

    int size = 0, lcSize = 0, OeSize = 0, daysBetween = 0;
    long styDate = 0;

    Double tofuel = 0.0, ldgEliAmt = 0.0, ldgDrvEligi = 0.0, gTotal = 0.0, TotLdging = 0.0,
            GrandTotalAllowance = 0.0, fAmount = 0.0, doubleAmount = 0.0, myBrdAmt = 0.0, drvBrdAmt = 0.0,
            otherExp = 0.0, localCov = 0.0, sum = 0.0, sumsTotss = 0.0, sumsTot = 0.0;

    double TotDA = 0.0, sTotal = 0.0, sums = 0.0, sumsTa = 0.0, tTotAmt = 0.0, stayEgTotal = 0.0;
    float tJointAmt = 0;

    Button btn_sub, buttonSave;

    ArrayList<SelectionModel> array = new ArrayList<>();
    ArrayList<String> DA = new ArrayList<>();
    ArrayList<String> OE = new ArrayList<>();
    ArrayList<String> LC = new ArrayList<>();
    ArrayList<String> temaplateList;
    ArrayList<String> OEdynamicList, dynamicLabelList, attachCountList;
    ArrayList<String> lodgArrLst = new ArrayList<>();

    List<Common_Model> listOrderType = new ArrayList<>();
    List<Common_Model> modelRetailDetails = new ArrayList<>();
    List<Common_Model> OtherExpenseList = new ArrayList<>();
    List<Common_Model> ldgModes = new ArrayList<>();
    List<Common_Model> modelTravelType = new ArrayList<>();
    List<ModeOfTravel> modelOfTravel;
    List<EditText> newEdt = new ArrayList<>();
    List<String> listWithoutDuplicates;

    Map<String, List<EditText>> usersByCountry = new HashMap<String, List<EditText>>();
    Map<String, List<EditText>> userOtherExpense = new HashMap<String, List<EditText>>();
    Map<String, String> AttachmentImg = new HashMap<String, String>();
    public static final String CheckInfo = "CheckInDetail";
    CustomListViewDialog customDialog;
    JSONObject jsonDailyAllowance = new JSONObject();
    Type userType;
    Gson gson;
    Dialog dialog;
    DatePickerDialog picker;
    Switch ldgNeeded;
    View viw, viewBilling;
    ScrollView mLc;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_a_claim);
        startService(new Intent(this, TimerService.class));
        mCommon_class = new Common_Class(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.route_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        mShared_common_pref = new Shared_Common_Pref(this);
        getToolbar();
        gson = new Gson();
        temaplateList = new ArrayList<>();
        dynamicLabelList = new ArrayList<>();
        OEdynamicList = new ArrayList<>();
        attachCountList = new ArrayList<>();

        txt_date = findViewById(R.id.txt_date);
        card_date = findViewById(R.id.card_date);
        btn_sub = findViewById(R.id.btn_sub);
        linAddAllowance = findViewById(R.id.lin_travel_loaction);
        vwBoarding = findViewById(R.id.vwBoarding);
        vwDrvBoarding = findViewById(R.id.vwDrvBoarding);
        TravelBike = findViewById(R.id.linear_bike);
        TxtStartedKm = findViewById(R.id.txt_started_km);
        TxtClosingKm = findViewById(R.id.txt_ended_km);
        travelTypeMode = findViewById(R.id.txt_type_travel);
        PersonalTextKM = findViewById(R.id.personal_km_text);
        endkmimage = findViewById(R.id.endkmimage);
        startkmimage = findViewById(R.id.startkmimage);
        jointLodging = findViewById(R.id.lin_join_person);
        txtJointAdd = findViewById(R.id.txt_joint_add);
        TotalTravelledKm = findViewById(R.id.total_km);
        PersonalKiloMeter = findViewById(R.id.pers_kilo_meter);
        editTextRemarks = findViewById(R.id.edt_rmk);
        LDailyAllowance = findViewById(R.id.lin_daily_allowance);
        LOtherExpense = findViewById(R.id.lin_other_allowance);
        LLocalConve = findViewById(R.id.lin_local_con);
        linBusMode = findViewById(R.id.linear_bus_mode);
        linBikeMode = findViewById(R.id.linear_bike_mode);
        linMode = findViewById(R.id.linear_mode);
        localTotal = findViewById(R.id.lin_total_loca);
        otherExpenseLayout = findViewById(R.id.lin_total_other);
        lodgStyLocation = findViewById(R.id.edt_stay_loc);
        txtallamt = findViewById(R.id.txt_mode_amount);
        txt_BrdAmt = findViewById(R.id.txt_BrdAmt);
        txt_DrvBrdAmt = findViewById(R.id.txt_DrvBrdAmt);
        txtBusFrom = findViewById(R.id.txt_bus_from);
        txtBusTo = findViewById(R.id.txt_bus_to);
        txtTaClaim = findViewById(R.id.mode_name);
        txtDailyAllowance = findViewById(R.id.txt_daily_allowance_mode);
        travelDynamicLoaction = findViewById(R.id.lin_travel_dynamic_location);
        crdDynamicLocation = findViewById(R.id.card_travel_loaction);
        linDailyAllowance = findViewById(R.id.lin_da_type);
        linlocalCon = findViewById(R.id.lin_dyn_local_con);
        fuelAmount = findViewById(R.id.fuel_amount);
        TextTotalAmount = findViewById(R.id.txt_total_amt);
        LinearOtherAllowance = findViewById(R.id.lin_dyn_other_Expense);
        grandTotal = findViewById(R.id.grand_total);
        localText = findViewById(R.id.txt_local);
        OeText = findViewById(R.id.txt_oe);
        linAll = findViewById(R.id.lin_alo_view);
        linRemarks = findViewById(R.id.lin_remarks);
        linFareAmount = findViewById(R.id.lin_fare_amount);
        ldgNeeded = findViewById(R.id.sw_ldgNeed);
        ldg_ara = findViewById(R.id.linear_loadge);
        ldg_typ_sp = findViewById(R.id.ldg_typ_spiner);
        lodgCont = findViewById(R.id.lodgCont);
        lodgContvw = findViewById(R.id.lodgContvw);
        txt_ldg_type = findViewById(R.id.ldg_typ);
        ldg_stayloc = findViewById(R.id.ldg_stayloc);
        ldg_stayDt = findViewById(R.id.ldg_stayDt);
        lodgJoin = findViewById(R.id.lodgJoin);
        ldgEAra = findViewById(R.id.ldgEAra);
        ldgMyEAra = findViewById(R.id.ldgMyEAra);
        JNLdgEAra = findViewById(R.id.JNLdgEAra);
        drvldgEAra = findViewById(R.id.drvldgEAra);
        ldg_cin = findViewById(R.id.from_picker);
        ldg_cout = findViewById(R.id.to_picker);
        txt_totDA = findViewById(R.id.txt_totDA);
        txtMyEligi = findViewById(R.id.txtMyEligi);
        txtDrivEligi = findViewById(R.id.txtDrvLgd);
        lbl_ldg_eligi = findViewById(R.id.lbl_ldg_eligi);
        lblHdBill = findViewById(R.id.lblHdBill);
        lblHdBln = findViewById(R.id.lblHdBln);
        ldgWOBBal = findViewById(R.id.ldgWOBBal);
        edt_ldg_bill = findViewById(R.id.edt_ldg_bill);
        img_lodg_prvw = findViewById(R.id.lodg_preview);
        img_lodg_atta = findViewById(R.id.lodg_attach);
        txtJNEligi = findViewById(R.id.txtJNEligi);
        ldgAdd = findViewById(R.id.ldg_Add);
        mapZoomIn = findViewById(R.id.map_zoom);
        buttonSave = findViewById(R.id.save_button);
        txtTAamt = findViewById(R.id.txt_trav_loca);
        linback = findViewById(R.id.lin_back);
        imgBck = findViewById(R.id.imag_backs);
        txtName = findViewById(R.id.txt_Name);
        txtDesig = findViewById(R.id.txt_desg);
        txtDept = findViewById(R.id.txt_dep);
        txtEmpId = findViewById(R.id.txt_emp_id);
        txtName.setText(Shared_Common_Pref.Sf_Name);
        txtDesig.setText(mShared_common_pref.getvalue(Shared_Common_Pref.SF_DESIG));
        txtDept.setText(mShared_common_pref.getvalue(Shared_Common_Pref.SF_DEPT));
        txtEmpId.setText("(" + mShared_common_pref.getvalue(Shared_Common_Pref.SF_EMP_ID) + ")");
        viw = findViewById(R.id.vw_tl);
        lin = findViewById(R.id.lin_tl);
        txtMaxKm = findViewById(R.id.max_km);
        txtDrvrBrod = findViewById(R.id.txt_driver_boarding);
        txtStyDays = findViewById(R.id.txt_stay_total);
        TotalDays = findViewById(R.id.total_days);
        stayDays = findViewById(R.id.lin_stay_view);
        txtLodgUKey = findViewById(R.id.log_ukey);
        CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
        mLc = findViewById(R.id.lc_srcoll);
        linImgPrv = findViewById(R.id.lin_img_prv);
        viewBilling = findViewById(R.id.bill_view);

        img_lodg_atta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraPermission cameraPermission = new CameraPermission(TAClaimActivity.this, getApplicationContext());
                if (!cameraPermission.checkPermission()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        cameraPermission.requestPermission();
                    }
                    Log.v("PERMISSION_NOT", "PERMISSION_NOT");
                } else {
                    Log.v("PERMISSION", "PERMISSION");
                    popupCapture(143);

                    if (txtLodgUKey.getText().toString().equals("")) {
                        DateFormat dfw = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        Calendar calobjw = Calendar.getInstance();
                        lodUKey = keyEk + mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code) + dfw.format(calobjw.getTime()).hashCode();
                        txtLodgUKey.setText(lodUKey);
                    }
                }

            }
        });

        img_lodg_prvw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateTime = DateTime.replaceAll("^[\"']+|[\"']+$", "");

                Intent stat = new Intent(getApplicationContext(), AttachementActivity.class);
                stat.putExtra("position", txtLodgUKey.getText().toString());
                stat.putExtra("headTravel", "LOD");
                stat.putExtra("mode", "Room");
                stat.putExtra("date", DateTime);
                startActivity(stat);


            }
        });

        drvldgEAra.setVisibility(View.VISIBLE);
        ldgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ldgEliAmt = 0.0;

                drvldgEAra.setVisibility(View.GONE);
                if (ldgAdd.getText().equals("+ Add")) {
                    ldgAdd.setText("- Remove");
                    lodgContvw.setVisibility(View.VISIBLE);
                } else {
                    ldgAdd.setText("+ Add");
                    lodgContvw.setVisibility(View.GONE);
                    txtMyEligi.setText("Rs." + new DecimalFormat("##0.00").format(Double.valueOf("0.00")));
                }
            }
        });
        ldg_typ_sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ldgModes.clear();
                LDGType();
            }
        });
        ldg_stayDt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCldr();
            }
        });

        ldg_cin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCldr();
            }
        });

        ldg_cout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(TAClaimActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                                String currentDateandTime = sdf.format(new Date());


                                ldg_cout.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                difference();

                            }
                        }, year, month, day);
                Calendar calendarmin = Calendar.getInstance();
                calendarmin.set(Integer.parseInt(tominYear), Integer.parseInt(tominMonth) - 1, Integer.parseInt(tominDay));
                picker.getDatePicker().setMinDate(calendarmin.getTimeInMillis());


                picker.show();

            }
        });

        edt_ldg_bill.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                SumWOBLodging();
            }
        });


        strRetriveType = String.valueOf(getIntent().getSerializableExtra("Retrive_Type"));
        if (strRetriveType.equals("Daily Allowance")) {
            jsonDailyAllowance = (JSONObject) getIntent().getSerializableExtra("Retrive_Ta_List");
        }

        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });

        imgBck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });


        strGT = grandTotal.getText().toString();

        LOtherExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);


                otherExpenseLayout.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                layoutParams.setMargins(15, 15, 15, 15);

                final View rowView = inflater.inflate(R.layout.activity_other_expense, null);
                LinearOtherAllowance.addView(rowView, layoutParams);


                oePosCnt = LinearOtherAllowance.indexOfChild(rowView);

                View views = LinearOtherAllowance.getChildAt(oePosCnt);
                otherExpenseLayout.setVisibility(View.VISIBLE);
                oeEditext = (TextView) (views.findViewById(R.id.other_enter_mode));
                edtOE = (EditText) (views.findViewById(R.id.oe_fre_amt));
                oeAttach = (ImageView) (views.findViewById(R.id.oe_attach_img));
                oePreview = (ImageView) (views.findViewById(R.id.img_prvw_oe));
                oeTxtUKey = (TextView) (views.findViewById(R.id.txt_oe_ukey));
                OtherExpense = (LinearLayout) views.findViewById(R.id.lin_other_expense_dynamic);

                linOtherSpinner = (LinearLayout) (views.findViewById(R.id.lin_othr_spiner));

                oeAttach.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        CameraPermission cameraPermission = new CameraPermission(TAClaimActivity.this, getApplicationContext());

                        if (!cameraPermission.checkPermission()) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                                cameraPermission.requestPermission();
                            }
                            Log.v("PERMISSION_NOT", "PERMISSION_NOT");
                        } else {
                            Log.v("PERMISSION", "PERMISSION");
                            popupCapture(99);
                            Integer valuedfd = LinearOtherAllowance.indexOfChild(rowView);

                            View view = LinearOtherAllowance.getChildAt(valuedfd);
                            oeEditext = (TextView) (view.findViewById(R.id.other_enter_mode));
                            oeTxtUKeys = (TextView) (view.findViewById(R.id.txt_oe_ukey));
                            editMode = oeEditext.getText().toString();

                            if (oeTxtUKeys.getText().toString().equals("")) {
                                DateFormat dfw = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                Calendar calobjw = Calendar.getInstance();
                                oeEditCnt = keyEk + mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code) + dfw.format(calobjw.getTime()).hashCode();
                                oeTxtUKeys.setText(oeEditCnt);
                            }

                            OeUKey = oeTxtUKeys.getText().toString();
                        }

                    }
                });

                oePreview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Integer valuedfd = LinearOtherAllowance.indexOfChild(rowView);
                        View view = LinearOtherAllowance.getChildAt(valuedfd);
                        oeEditext = (TextView) (view.findViewById(R.id.other_enter_mode));
                        oeTxtUKeys = (TextView) (view.findViewById(R.id.txt_oe_ukey));
                        editMode = oeEditext.getText().toString();

                        OeUKey = oeTxtUKeys.getText().toString();

                        DateTime = DateTime.replaceAll("^[\"']+|[\"']+$", "");

                        Intent stat = new Intent(getApplicationContext(), AttachementActivity.class);
                        stat.putExtra("position", OeUKey);
                        stat.putExtra("headTravel", "OE");
                        stat.putExtra("mode", editMode);
                        stat.putExtra("date", DateTime);
                        startActivity(stat);

                    }
                });


                edtOE.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        SumOFOTAmount();
                    }
                });

                linOtherSpinner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OtherExpenseList.clear();
                        Integer valuedf = LinearOtherAllowance.indexOfChild(rowView);
                        OtherExpenseMode(valuedf);

                    }
                });

            }
        });
        LLocalConve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                layoutParams.setMargins(15, 15, 15, 15);

                final View rowView = inflater.inflate(R.layout.activity_local_convenyance, null);

                linlocalCon.addView(rowView, layoutParams);
                localText.setVisibility(View.VISIBLE);

                Integer localCount = linlocalCon.getChildCount();
                Log.v("Local_Count1", String.valueOf(localCount));
                Log.v("Local_Count2", String.valueOf(linlocalCon.getChildCount() - 1));

                lcPosCnt = linlocalCon.indexOfChild(rowView);
                Log.v("Local_Count3", String.valueOf(lcPosCnt));
                Log.v("Local_Count4", String.valueOf(lcPosCnt - 1));

                LayoutTransition transition = new LayoutTransition();
                linlocalCon.setLayoutTransition(transition);


                View LcchildView = linlocalCon.getChildAt(lcPosCnt);
                scroll();

                editTexts = (TextView) (LcchildView.findViewById(R.id.local_enter_mode));
                linLocalSpinner = (LinearLayout) LcchildView.findViewById(R.id.lin_loc_spiner);
                lcAttach = (ImageView) (LcchildView.findViewById(R.id.la_attach_iamg));
                lcPreview = (ImageView) (LcchildView.findViewById(R.id.img_prvw_lc));
                lcTxtUKey = (TextView) (LcchildView.findViewById(R.id.txt_lc_ukey));
                editLaFare = (EditText) (LcchildView.findViewById(R.id.edt_la_fare));
                editLaFare.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        SumOFLCAmount();
                    }
                });


                Dynamicallowance = (LinearLayout) LcchildView.findViewById(R.id.lin_allowance_dynamic);

                linLocalSpinner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listOrderType.clear();
                        Integer lcPosCntS = linlocalCon.indexOfChild(rowView);
                        dynamicModeType(lcPosCntS);
                    }
                });

                lcAttach.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        CameraPermission cameraPermission = new CameraPermission(TAClaimActivity.this, getApplicationContext());

                        if (!cameraPermission.checkPermission()) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                                cameraPermission.requestPermission();
                            }
                            Log.v("PERMISSION_NOT", "PERMISSION_NOT");
                        } else {
                            Log.v("PERMISSION", "PERMISSION");

                            popupCapture(786);

                            Integer lcPosCntS = linlocalCon.indexOfChild(rowView);
                            View view = linlocalCon.getChildAt(lcPosCntS);
                            editTexts = (TextView) (view.findViewById(R.id.local_enter_mode));
                            lcTxtUKeys = (TextView) (view.findViewById(R.id.txt_lc_ukey));
                            editMode = editTexts.getText().toString();

                            if (lcTxtUKeys.getText().toString().equals("")) {
                                DateFormat dfw = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                Calendar calobjw = Calendar.getInstance();
                                lcEditcnt = keyEk + mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code) + dfw.format(calobjw.getTime()).hashCode();
                                lcTxtUKeys.setText(lcEditcnt);
                            }
                            LcUKey = lcTxtUKeys.getText().toString();
                        }

                    }
                });

                lcPreview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Integer valuelc = linlocalCon.indexOfChild(rowView);
                        View view = linlocalCon.getChildAt(valuelc);
                        editTexts = (TextView) (view.findViewById(R.id.local_enter_mode));
                        lcTxtUKeys = (TextView) (view.findViewById(R.id.txt_lc_ukey));
                        editMode = editTexts.getText().toString();
                        LcUKey = lcTxtUKeys.getText().toString();

                        DateTime = DateTime.replaceAll("^[\"']+|[\"']+$", "");

                        Intent stat = new Intent(getApplicationContext(), AttachementActivity.class);
                        stat.putExtra("position", LcUKey);
                        stat.putExtra("headTravel", "LC");
                        stat.putExtra("mode", editMode);
                        stat.putExtra("date", DateTime);
                        startActivity(stat);
                    }
                });
            }
        });
        linAddAllowance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(15, 15, 15, 15);

                if (StrToEnd.equals("0")) {
                    crdDynamicLocation.setVisibility(View.VISIBLE);
                    final View rowView = inflater.inflate(R.layout.travel_allowance_dynamic, null);
                    travelDynamicLoaction.addView(rowView, layoutParams);
                    deleteButton = findViewById(R.id.delete_button);

                    tvSize = travelDynamicLoaction.indexOfChild(rowView);
                    View tvchildView = travelDynamicLoaction.getChildAt(tvSize);
                    viw.setVisibility(View.VISIBLE);
                    lin.setVisibility(View.VISIBLE);

                    editText = (TextView) (tvchildView.findViewById(R.id.enter_mode));
                    enterFare = (EditText) tvchildView.findViewById(R.id.enter_fare);
                    taAttach = (ImageView) (tvchildView.findViewById(R.id.image_attach));
                    previewss = (ImageView) (tvchildView.findViewById(R.id.image_preview));
                    tvTxtUKey = (TextView) (tvchildView.findViewById(R.id.txt_tv_ukey));

                    editText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            modelTravelType.clear();
                            Integer tvSizes = travelDynamicLoaction.indexOfChild(rowView);
                            localCon(tvSizes);
                        }
                    });
                    enterFare.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            SumOFTAAmount();
                        }
                    });


                    previewss.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Integer tvSizes = travelDynamicLoaction.indexOfChild(rowView);
                            View view = travelDynamicLoaction.getChildAt(tvSizes);
                            editText = (TextView) (view.findViewById(R.id.enter_mode));
                            enterFare = (EditText) view.findViewById(R.id.enter_fare);
                            tvTxtUKeys = (TextView) (view.findViewById(R.id.txt_tv_ukey));
                            editMode = editText.getText().toString();
                            TlUKey = tvTxtUKeys.getText().toString();

                            DateTime = DateTime.replaceAll("^[\"']+|[\"']+$", "");

                            Intent stat = new Intent(getApplicationContext(), AttachementActivity.class);
                            stat.putExtra("position", TlUKey);
                            stat.putExtra("headTravel", "TL");
                            stat.putExtra("mode", editMode);
                            stat.putExtra("date", DateTime);
                            startActivity(stat);


                        }
                    });


                    taAttach.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            CameraPermission cameraPermission = new CameraPermission(TAClaimActivity.this, getApplicationContext());

                            if (!cameraPermission.checkPermission()) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    cameraPermission.requestPermission();
                                }
                                Log.v("PERMISSION_NOT", "PERMISSION_NOT");
                            } else {
                                Log.v("PERMISSION", "PERMISSION");
                                popupCapture(123);

                                Integer tvSizes = travelDynamicLoaction.indexOfChild(rowView);
                                View view = travelDynamicLoaction.getChildAt(tvSizes);
                                editText = (TextView) (view.findViewById(R.id.enter_mode));
                                enterFare = (EditText) view.findViewById(R.id.enter_fare);
                                tvTxtUKeys = (TextView) (view.findViewById(R.id.txt_tv_ukey));
                                editMode = editText.getText().toString();

                                if (tvTxtUKeys.getText().toString().equals("")) {
                                    DateFormat dfw = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                    Calendar calobjw = Calendar.getInstance();
                                    tvEditcnt = keyEk + mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code) + dfw.format(calobjw.getTime()).hashCode();
                                    tvTxtUKeys.setText(tvEditcnt);
                                }
                                TlUKey = tvTxtUKeys.getText().toString();

                            }

                        }
                    });


                } else {
                    final View rowView = inflater.inflate(R.layout.travel_allowance_dynamic_one, null);
                    travelDynamicLoaction.addView(rowView, layoutParams);

                    viw.setVisibility(View.GONE);
                    lin.setVisibility(View.GONE);
                    crdDynamicLocation.setVisibility(View.VISIBLE);
                    size = travelDynamicLoaction.getChildCount();
                    for (int c = 0; c < size; c++) {
                        View views = travelDynamicLoaction.getChildAt(c);
                        deleteButton = views.findViewById(R.id.delete_button);
                        etrTaFr = (EditText) views.findViewById(R.id.ta_edt_from);
                        etrTaTo = (EditText) views.findViewById(R.id.ta_edt_to);
                    }
                }

            }
        });


        txtJointAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lodingView();
            }
        });


        UserDetails = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SF_code = UserDetails.getString("Sfcode", "");
        div = UserDetails.getString("Divcode", "");
        State_Code = UserDetails.getString("State_Code", "");
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        String date = Common_Class.GetDate();

        DateForAPi = date.substring(0, date.indexOf(" "));
        callApi(DateForAPi, "");

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

                if (txt_date.getText().toString().matches("")) {
                    Toast.makeText(TAClaimActivity.this, "Please choose Date", Toast.LENGTH_SHORT).show();
                } else {
                    submitData("SubmitForApp");
                }
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


        mapZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateTime = DateTime.replaceAll("^[\"']+|[\"']+$", "");
                Intent mapZoom = new Intent(getApplicationContext(), MapZoomIn.class);
                mapZoom.putExtra("date", DateTime);

                startActivity(mapZoom);

                overridePendingTransition(R.anim.fade,
                        R.anim.fade);

            }
        });


        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData("Save");
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            shouldShowRequestPermissionRationale("true");
        }
    }

    private void calOverAllTotal(Double localCov, Double otherExp, double tTotAmt) {
        Log.v("tTotAmt", String.valueOf(tTotAmt));


        String strldgTotal = lbl_ldg_eligi.getText().toString().substring(lbl_ldg_eligi.getText().toString().indexOf(".") + 1).trim();
        String separators = ".";
        int intldgTotal = strldgTotal.lastIndexOf(separators);


        gTotal = localCov + myBrdAmt + drvBrdAmt + otherExp + GrandTotalAllowance + Double.valueOf(strldgTotal.substring(0, intldgTotal));
        grandTotal.setText("Rs." + new DecimalFormat("##0.00").format(gTotal));

    }

    public void showCldr() {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        picker = new DatePickerDialog(TAClaimActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                        String currentDateandTime = sdf.format(new Date());

                        Log.v("CURRENT_TIME", String.valueOf(currentDateandTime));


                        ldg_cin.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        MaxMinDateTo(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        difference();

                    }
                }, year, month, day);

        picker.show();
    }

    public void onTADelete(View v) {
        int size = travelDynamicLoaction.getChildCount();
        travelDynamicLoaction.removeView((View) v.getParent());

        LayoutTransition transition = new LayoutTransition();
        travelDynamicLoaction.setLayoutTransition(transition);

        if (size == 0) {
            crdDynamicLocation.setVisibility(View.GONE);
        }

        if (StrToEnd.equals("0")) {
            SumOFTAAmount();
        } else {

        }
    }

    public void onLCDelete(View v) {
        linlocalCon.removeView((View) v.getParent());

        LayoutTransition transition = new LayoutTransition();
        linlocalCon.setLayoutTransition(transition);

        if (linlocalCon.getChildCount() == 0) {
            localTotal.setVisibility(View.GONE);
        }
        SumOFLCAmount();

    }

    public void ImagePdf(View v) {
        pdfViewList();
    }

    public void onOEDelete(View v) {

        LinearOtherAllowance.removeView((View) v.getParent());

        LayoutTransition transition = new LayoutTransition();
        LinearOtherAllowance.setLayoutTransition(transition);

        if (LinearOtherAllowance.getChildCount() == 0) {
            otherExpenseLayout.setVisibility(View.GONE);
        }

        SumOFOTAmount();
    }

    public void lodingView() {

        jointLodging.setVisibility(View.VISIBLE);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = null;

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(15, 15, 15, 15);
        rowView = inflater.inflate(R.layout.activity_loding_layout, null);
        jointLodging.addView(rowView, layoutParams);
        for (int i = 0; i < jointLodging.getChildCount(); i++) {
            View childView = jointLodging.getChildAt(i);
            ImageView deleteLod = (ImageView) childView.findViewById(R.id.ldg_delete);
            if (i == 0) {
                deleteLod.setVisibility(View.GONE);
            } else {
                deleteLod.setVisibility(View.VISIBLE);
            }
        }
    }

    public void onLodingDelete(View v) {
        jointLodging.removeView((View) v.getParent());
        SumOFJointLodging();
    }


    public void onGetEmpDetails(View v) {
        View pv = (View) v.getParent().getParent();
        edt_ldg_JnEmp = pv.findViewById(R.id.edt_ldg_JnEmp);
        String sEmpID = String.valueOf(edt_ldg_JnEmp.getText());
        DateTime = DateTime.replaceAll("^[\"']+|[\"']+$", "");

        if (edt_ldg_JnEmp.getText().toString().equals("")) {

        } else {

            Call<JsonArray> Callto = apiInterface.getDataArrayList("get/EmpByID",
                    UserDetails.getString("Divcode", ""),
                    UserDetails.getString("Sfcode", ""), sEmpID, "", DateTime, null);
            Callto.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    JsonArray res = response.body();
                    if (res.size() < 1) {
                        Toast.makeText(getApplicationContext(), "Emp Code Not Found !", Toast.LENGTH_LONG).show();
                        return;
                    }
                    JsonObject EmpDet = res.get(0).getAsJsonObject();

                    txtJNName = pv.findViewById(R.id.txtJNName);
                    txtJNDesig = pv.findViewById(R.id.txtJNDesig);
                    txtJNDept = pv.findViewById(R.id.txtJNDept);
                    txtJNHQ = pv.findViewById(R.id.txtJNHQ);
                    txtJNMob = pv.findViewById(R.id.txtJNMob);
                    txtJNMyEli = pv.findViewById(R.id.txtJNMyEli);

                    txtJNName.setText(EmpDet.get("Name").getAsString());
                    txtJNDesig.setText(EmpDet.get("Desig").getAsString());
                    txtJNDept.setText(EmpDet.get("DeptName").getAsString());
                    txtJNHQ.setText(EmpDet.get("HQ").getAsString());
                    txtJNMob.setText(EmpDet.get("Mob").getAsString());
                    float sum = EmpDet.get("ldgAllow").getAsFloat();
                    ldgEmpName = String.valueOf(sum);

                    txtJNMyEli.setText("Rs." + new DecimalFormat("##0.00").format(sum));
                    SumOFJointLodging();
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    Log.d("Error:", "Some Error" + t.getMessage());
                }
            });
        }
    }

    public void SumOFTAAmount() {
        sumsTotss = 0.0;
        int lcSize = travelDynamicLoaction.getChildCount();
        for (int k = 0; k < lcSize; k++) {
            View cv = travelDynamicLoaction.getChildAt(k);
            enterFare = cv.findViewById(R.id.enter_fare);
            String str = enterFare.getText().toString();
            if (str.matches("")) str = "0";
            sumsTotss = sumsTotss + Double.parseDouble(str);
            sumsTa = GrandTotalAllowance + sumsTotss;
        }
        txtTAamt.setText("Rs." + new DecimalFormat("##0.00").format(sumsTotss));
        localCov = sumsTotss;

        calOverAllTotal(localCov, otherExp, tTotAmt);
    }

    public void SumOFLCAmount() {
        sum = 0.0;
        int lcSize = linlocalCon.getChildCount();
        for (int k = 0; k < lcSize; k++) {
            View cv = linlocalCon.getChildAt(k);
            editLaFare = (EditText) (cv.findViewById(R.id.edt_la_fare));
            String str = editLaFare.getText().toString();
            if (str.matches("")) str = "0";
            sum = sum + Double.parseDouble(str);
            sums = GrandTotalAllowance + sum;
        }
        localText.setText("Rs." + new DecimalFormat("##0.00").format(sum));
        localCov = sum;

        calOverAllTotal(localCov, otherExp, tTotAmt);
    }

    public void SumOFOTAmount() {
        sumsTot = 0.0;
        int OeSize = LinearOtherAllowance.getChildCount();
        for (int k = 0; k < OeSize; k++) {
            View cv = LinearOtherAllowance.getChildAt(k);
            edtOE = (EditText) (cv.findViewById(R.id.oe_fre_amt));
            String strs = edtOE.getText().toString();
            if (strs.matches("")) strs = "0";
            sumsTot = sumsTot + Double.parseDouble(strs);
            sTotal = GrandTotalAllowance + sumsTot;

        }
        OeText.setText("Rs." + new DecimalFormat("##0.00").format(sumsTot));

        otherExp = sumsTot;
        calOverAllTotal(localCov, otherExp, tTotAmt);
    }

    public void SumOFDAAmount() {

        String sAmt = txtallamt.getText().toString().replaceAll("Rs.", "");
        String sBrdAmt = txt_BrdAmt.getText().toString().replaceAll("Rs.", "");
        String sDrvBrdAmt = txt_DrvBrdAmt.getText().toString().replaceAll("Rs.", "");
        Log.e("STRDAILY", StrDailyAllowance);
        if (sAmt.equalsIgnoreCase("")) sAmt = "0.00";
        if (sBrdAmt.equalsIgnoreCase("")) sBrdAmt = "0.00";
        if (sDrvBrdAmt.equalsIgnoreCase("")) sDrvBrdAmt = "0.00";
        TotDA = Double.parseDouble(sAmt) + Double.parseDouble(sBrdAmt) + Double.parseDouble(sDrvBrdAmt);
        txt_totDA.setText("Rs." + new DecimalFormat("##0.00").format(TotDA));
        calOverAllTotal(localCov, otherExp, tTotAmt);
    }

    public void SumOFJointLodging() {

        for (int i = 0; i < jointLodging.getChildCount(); i++) {
            View childView = jointLodging.getChildAt(i);
            TextView jLdgEli = (TextView) childView.findViewById(R.id.txtJNMyEli);
            String sAmt = jLdgEli.getText().toString().replaceAll("Rs.", "");
            tJointAmt = tJointAmt + Float.parseFloat(sAmt);
        }
        txtJNEligi.setText("Rs." + new DecimalFormat("##0.00").format(tJointAmt));
        SumOFLodging();
    }

    public void SumOFLodging() {
        String sMyAmt = txtMyEligi.getText().toString().replaceAll("Rs.", "");
        String sJnAmt = txtJNEligi.getText().toString().replaceAll("Rs.", "");
        String sDrivAmt = txtDrivEligi.getText().toString().replaceAll("Rs.", "");
        //  double tTotAmt = ldgEliAmt + ldgDrvEligi + Float.parseFloat(sJnAmt);
        tTotAmt = Double.parseDouble(sMyAmt) + ldgDrvEligi + Float.parseFloat(sJnAmt);

        Log.v("TOTAL_STAY", String.valueOf(stayEgTotal));
        Log.v("TOTAL_DRV", String.valueOf(ldgDrvEligi));
        Log.v("TOTAL_JNAMT", String.valueOf(sJnAmt));
        Log.v("TOTAL_TOTMAT", String.valueOf(tTotAmt));

        totLodgAmt = String.valueOf(tTotAmt);
        lbl_ldg_eligi.setText("Rs." + new DecimalFormat("##0.00").format(tTotAmt));
        SumWOBLodging();
        int IntValue = (int) tTotAmt;
        Log.v("TOTAL_AMOUNT", String.valueOf(IntValue));
        edt_ldg_bill.setFilters(new InputFilter[]{new Common_Class.InputFilterMinMax(0, IntValue)});
        calOverAllTotal(localCov, otherExp, tTotAmt);
    }

    public void SumWOBLodging() {

        String sldgAmt = lbl_ldg_eligi.getText().toString().replaceAll("Rs.", "");
        String sBillAmt = edt_ldg_bill.getText().toString().replaceAll("Rs.", "");
        if (sBillAmt.isEmpty()) sBillAmt = "0";
        float tBalAmt = Float.parseFloat(sldgAmt) - Float.parseFloat(sBillAmt);
        witOutBill = String.valueOf(tBalAmt);

        Log.v("TOTAL_LODG_AMT", sldgAmt);
        Log.v("TOTAL_LODG_BILL", sBillAmt);
        Log.v("TOTAL_LODG_TOTAL", String.valueOf(tBalAmt));

        if (tBalAmt > 0) {
            ldgWOBBal.setText("Rs." + new DecimalFormat("##0.00").format(tBalAmt));
        }

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
                /* startActivity(new Intent(getApplicationContext(), Dashboard.class));*/
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
            Intent Dashboard = new Intent(TAClaimActivity.this, Dashboard_Two.class);
            Dashboard.putExtra("Mode", "CIN");
            startActivity(Dashboard);
        } else
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
    }


    /*Choosing Dynamic date*/
    public void dynamicDate() {

        JSONObject jj = new JSONObject();
        try {
            jj.put("sfCode", SF_code);
            jj.put("divisionCode", div);
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
            }
        });
    }

    /*Display Mode of travel View based on the choosed Date*/
    public void displayTravelMode(String ChoosedDate) {

        try {

            ChoosedDate = ChoosedDate.replaceAll("^[\"']+|[\"']+$", "");

            JSONObject jj = new JSONObject();
            try {
                jj.put("sfCode", mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code));
                jj.put("divisionCode", mShared_common_pref.getvalue(Shared_Common_Pref.Div_Code));
                jj.put("Selectdate", ChoosedDate);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonObject> call = apiInterface.getTAdateDetails(jj.toString());
            String finalChoosedDate = ChoosedDate;
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    JsonArray jsonArray = null;
                    JsonArray lcDraftArray = null;
                    JsonArray oeDraftArray = null;
                    JsonArray trvldArray = null;
                    JsonArray ldArray = null;
                    JsonArray travelDetails = null;
                    JsonObject jsonObjects = response.body();
                    Log.v("JSON_OBJECT", jsonObjects.toString());
                    jsonArray = jsonObjects.getAsJsonArray("TodayStart_Details");
                    lcDraftArray = jsonObjects.getAsJsonArray("Additional_ExpenseLC");
                    oeDraftArray = jsonObjects.getAsJsonArray("Additional_ExpenseOE");
                    trvldArray = jsonObjects.getAsJsonArray("Travelled_Loc");
                    ldArray = jsonObjects.getAsJsonArray("Lodging_Head");
                    travelDetails = jsonObjects.getAsJsonArray("Travelled_Details");

                    Log.v("JSON_LODGING", ldArray.toString());

                    JsonObject jsonObject = null;
                    for (int i = 0; i < jsonArray.size(); i++) {
                        int finalC = i;

                        Log.e("JsonArray", String.valueOf(jsonArray.size()));
                        jsonObject = (JsonObject) jsonArray.get(i);

                        linDailyAllowance.setVisibility(View.VISIBLE);

                        StartedKm = jsonObject.get("Start_Km").getAsString();
                        StrToEnd = (jsonObject.get("StEndNeed").getAsString());
                        StrBus = jsonObject.get("From_Place").getAsString();
                        StrTo = jsonObject.get("To_Place").getAsString();
                        StrDaName = jsonObject.get("MOT_Name").getAsString();
                        StrDailyAllowance = jsonObject.get("dailyAllowance").getAsString();
                        strFuelAmount = jsonObject.get("FuelAmt").getAsString();
                        allowanceAmt = jsonObject.get("Allowance_Value").getAsString();
                        myldgEliAmt = jsonObject.get("myLdgAmt").getAsString();
                        myBrdEliAmt = jsonObject.get("myBrdAmt").getAsString();
                        drvldgEliAmt = jsonObject.get("drvLdgAmt").getAsString();
                        drvBrdEliAmt = jsonObject.get("drvBrdAmt").getAsString();
                        start_Image = jsonObject.get("start_Photo").getAsString();
                        End_Imge = jsonObject.get("End_photo").getAsString();
                        ClosingKm = jsonObject.get("End_Km").getAsString();
                        PersonalKm = jsonObject.get("Personal_Km").getAsString();
                        DriverNeed = jsonObject.get("driverAllowance").getAsString();


                        Log.v("ELIGIBLE_AMOUNT", myldgEliAmt);

                        JsonObject js = null;
                        for (int l = 0; l < travelDetails.size(); l++) {
                            js = (JsonObject) travelDetails.get(l);
                            taAmt = js.get("ta_total_amount").getAsString();
                            txtTAamt.setText("Rs." + taAmt + ".00");
                        }


                        Log.v("DRIVER", ClosingKm);

                        Glide.with(getApplicationContext())
                                .load(start_Image.replaceAll("^[\"']+|[\"']+$", ""))
                                .into(startkmimage);
                        Glide.with(getApplicationContext())
                                .load(End_Imge.replaceAll("^[\"']+|[\"']+$", ""))
                                .into(endkmimage);

                        if (!start_Image.matches("")) {
                            startkmimage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getApplicationContext(), ProductImageView.class);
                                    intent.putExtra("ImageUrl", start_Image);
                                    startActivity(intent);
                                }
                            });
                        }
                        if (!End_Imge.matches("")) {
                            endkmimage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getApplicationContext(), ProductImageView.class);
                                    intent.putExtra("ImageUrl", End_Imge);
                                    startActivity(intent);
                                }
                            });
                        }

                        if (StartedKm != null && !StartedKm.isEmpty() && !StartedKm.equals("null") && !StartedKm.equals("")) {
                            S = Integer.valueOf(StartedKm);
                            TxtStartedKm.setText(StartedKm);
                            fAmount = Double.valueOf(strFuelAmount);
                            fuelAmount.setText(" Rs." + new DecimalFormat("##0.00").format(fAmount) + " / KM ");
                        }

                        txtTaClaim.setText(StrDaName);
                        if (!ClosingKm.equals("0")) {

                            StartedKm = StartedKm.replaceAll("^[\"']+|[\"']+$", "");

                            PersonalKm = PersonalKm.replaceAll("^[\"']+|[\"']+$", "");

                            if (PersonalKm.equals("null")) {
                                PersonalKiloMeter.setText("0");
                            } else {
                                PersonalKiloMeter.setText(PersonalKm);
                            }

                            if (ClosingKm != null && !ClosingKm.isEmpty() && !ClosingKm.equals("null") && !ClosingKm.equals("")) {
                                ClosingKm = ClosingKm.replaceAll("^[\"']+|[\"']+$", "");
                                TxtClosingKm.setText(ClosingKm);
                                C = Integer.valueOf(ClosingKm);
                                totalkm = C - S;
                                TotalTravelledKm.setText(String.valueOf(totalkm));
                            }

                            if (PersonalKm != null && !PersonalKm.isEmpty() && !PersonalKm.equals("null") && !PersonalKm.equals("")) {
                                PersonalKm = PersonalKm.replaceAll("^[\"']+|[\"']+$", "");
                                Pva = Integer.valueOf(PersonalKm);
                                totalPersonalKm = totalkm - Pva;

                            }


                            if (totalPersonalKm > 200 && StrDaName.equals("Two Wheeler")) {
                                PersonalTextKM.setText("200");
                                txtMaxKm.setText("Actual Allowance is " + totalkm + " but the max allowance is 200 ");
                                txtMaxKm.setVisibility(View.VISIBLE);

                                Double totalAmount = Double.valueOf(strFuelAmount);
                                tofuel = 200 * totalAmount;
                                TextTotalAmount.setText("Rs." + new DecimalFormat("##0.00").format(tofuel));


                            } else if (totalkm > 500 && StrDaName.equals("Four Wheeler")) {
                                PersonalTextKM.setText("500");
                                txtMaxKm.setText("Actual Allowance is " + totalkm + " but the max allowance is 500 ");

                                Double totalAmount = Double.valueOf(strFuelAmount);
                                tofuel = 500 * totalAmount;
                                TextTotalAmount.setText("Rs." + new DecimalFormat("##0.00").format(tofuel));

                                txtMaxKm.setVisibility(View.VISIBLE);
                            } else {
                                PersonalTextKM.setText(String.valueOf(totalPersonalKm));

                                Double totalAmount = Double.valueOf(strFuelAmount);
                                tofuel = totalPersonalKm * totalAmount;
                                TextTotalAmount.setText("Rs." + new DecimalFormat("##0.00").format(tofuel));


                                txtMaxKm.setVisibility(View.GONE);
                            }

                            btn_sub.setVisibility(View.VISIBLE);
                            buttonSave.setVisibility(View.VISIBLE);

                        } else {

                            btn_sub.setVisibility(View.GONE);
                            buttonSave.setVisibility(View.GONE);
                        }

                        txtDailyAllowance.setText(StrDailyAllowance + " - " + StrTo);

                        myBrdAmt = 0.0;
                        drvBrdAmt = 0.0;
                        vwDrvBoarding.setVisibility(View.GONE);
                        if (StrDailyAllowance.equals("Out Station")) {
                            doubleAmount = 0.0;
                            txtallamt.setText("");
                            myBrdEliAmt = myBrdEliAmt.replaceAll("^[\"']+|[\"']+$", "");
                            myBrdAmt = Double.valueOf(myBrdEliAmt);
                            txt_BrdAmt.setText(" Rs." + new DecimalFormat("##0.00").format(myBrdAmt));
                            if (DriverNeed.equalsIgnoreCase("true")) {
                                drvBrdEliAmt = drvBrdEliAmt.replaceAll("^[\"']+|[\"']+$", "");
                                drvBrdAmt = Double.valueOf(drvBrdEliAmt);
                                txt_DrvBrdAmt.setText(" Rs." + new DecimalFormat("##0.00").format(drvBrdAmt));

                                vwDrvBoarding.setVisibility(View.VISIBLE);
                                txtDrvrBrod.setText("Chauffer driven Boarding");
                            } else {
                                txt_DrvBrdAmt.setText("");
                            }

                            vwBoarding.setVisibility(View.VISIBLE);
                            SumOFDAAmount();
                        } else {
                            allowanceAmt = allowanceAmt.replaceAll("^[\"']+|[\"']+$", "");
                            doubleAmount = Double.valueOf(allowanceAmt);
                            myBrdAmt = 0.0;
                            txtallamt.setText(" Rs." + new DecimalFormat("##0.00").format(doubleAmount));
                            txt_BrdAmt.setText(" Rs.0.00");

                            if (DriverNeed.equalsIgnoreCase("true")) {
                                drvBrdEliAmt = drvBrdEliAmt.replaceAll("^[\"']+|[\"']+$", "");
                                drvBrdAmt = Double.valueOf(drvBrdEliAmt);
                                txt_DrvBrdAmt.setText(" Rs." + new DecimalFormat("##0.00").format(drvBrdAmt));

                                vwDrvBoarding.setVisibility(View.VISIBLE);
                                txtDrvrBrod.setText("Chauffer driven Boarding");
                            } else {
                                txt_DrvBrdAmt.setText("");
                            }
                            vwBoarding.setVisibility(View.GONE);
                            SumOFDAAmount();
                        }

                        if (StrToEnd.equals("0")) {
                            btn_sub.setVisibility(View.VISIBLE);
                            buttonSave.setVisibility(View.VISIBLE);
                            StrBus = StrBus.replaceAll("^[\"']+|[\"']+$", "");
                            StrTo = StrTo.replaceAll("^[\"']+|[\"']+$", "");
                            for (int j = 0; j < trvldArray.size(); j++) {

                                Integer finc = j;

                                JsonObject tldraftJson = (JsonObject) trvldArray.get(j);

                                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                layoutParams.setMargins(15, 15, 15, 15);

                                final View rowView = inflater.inflate(R.layout.travel_allowance_dynamic, null);

                                travelDynamicLoaction.addView(rowView, layoutParams);
                                tvSize = travelDynamicLoaction.indexOfChild(rowView);

                                View tvchildView = travelDynamicLoaction.getChildAt(tvSize);
                                viw.setVisibility(View.VISIBLE);
                                lin.setVisibility(View.VISIBLE);

                                LinearLayout lad = (LinearLayout) tvchildView.findViewById(R.id.linear_row_ad);
                                editText = (TextView) tvchildView.findViewById(R.id.enter_mode);
                                enterFrom = tvchildView.findViewById(R.id.enter_from);
                                enterTo = tvchildView.findViewById(R.id.enter_to);
                                enterFare = tvchildView.findViewById(R.id.enter_fare);
                                tvTxtUKey = (TextView) (tvchildView.findViewById(R.id.txt_tv_ukey));


                                editText.setText("" + tldraftJson.get("Mode").getAsString());
                                enterFrom.setText(tldraftJson.get("From_P").getAsString());
                                enterTo.setText(tldraftJson.get("To_P").getAsString());
                                enterFare.setText(tldraftJson.get("Fare").getAsString());


                                if (!tldraftJson.get("Ukey").getAsString().equals("") &&
                                        !tldraftJson.get("Ukey").getAsString().isEmpty() &&
                                        tldraftJson.get("Ukey").getAsString() != null) {
                                    tvTxtUKey.setText(tldraftJson.get("Ukey").getAsString());
                                }


                                editMode = editText.getText().toString();

                                Log.v("Travel_Location", editMode);

                                deleteButton = tvchildView.findViewById(R.id.delete_button);
                                taAttach = (ImageView) tvchildView.findViewById(R.id.image_attach);
                                previewss = (ImageView) tvchildView.findViewById(R.id.image_preview);
                                enterFare.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        SumOFTAAmount();
                                    }
                                });

                                taAttach.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        CameraPermission cameraPermission = new CameraPermission(TAClaimActivity.this, getApplicationContext());

                                        if (!cameraPermission.checkPermission()) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                                cameraPermission.requestPermission();
                                            }
                                            Log.v("PERMISSION_NOT", "PERMISSION_NOT");
                                        } else {
                                            Log.v("PERMISSION", "PERMISSION");
                                            popupCapture(123);

                                            Integer tvSizes = travelDynamicLoaction.indexOfChild(rowView);
                                            View view = travelDynamicLoaction.getChildAt(tvSizes);
                                            editText = (TextView) (view.findViewById(R.id.enter_mode));
                                            enterFare = (EditText) view.findViewById(R.id.enter_fare);
                                            tvTxtUKeys = (TextView) (view.findViewById(R.id.txt_tv_ukey));
                                            editMode = editText.getText().toString();

                                            Log.v("Travel_Location_imagw", editMode);

                                            if (tvTxtUKeys.getText().toString().equals("")) {
                                                DateFormat dfw = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                                Calendar calobjw = Calendar.getInstance();
                                                tvEditcnt = keyEk + mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code) + dfw.format(calobjw.getTime()).hashCode();
                                                tvTxtUKeys.setText(tvEditcnt);
                                            }
                                            TlUKey = tvTxtUKeys.getText().toString();


                                        }
                                    }
                                });

                                previewss.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Integer tvSizes = travelDynamicLoaction.indexOfChild(rowView);
                                        View view = travelDynamicLoaction.getChildAt(tvSizes);
                                        editText = (TextView) (view.findViewById(R.id.enter_mode));
                                        enterFare = (EditText) view.findViewById(R.id.enter_fare);
                                        tvTxtUKeys = (TextView) (view.findViewById(R.id.txt_tv_ukey));
                                        editMode = editText.getText().toString();
                                        TlUKey = tvTxtUKeys.getText().toString();
                                        Log.v("Travel_Location_preview", editMode);

                                        DateTime = DateTime.replaceAll("^[\"']+|[\"']+$", "");
                                        Intent stat = new Intent(getApplicationContext(), AttachementActivity.class);
                                        stat.putExtra("position", TlUKey);
                                        stat.putExtra("headTravel", "TL");
                                        stat.putExtra("mode", editMode);
                                        stat.putExtra("date", DateTime);
                                        startActivity(stat);

                                    }
                                });

                                if (j == 0) {
                                    deleteButton.setVisibility(View.GONE);
                                    lad.setOnClickListener(null);
                                    editText.setOnClickListener(null);
                                    editText.setClickable(false);
                                    enterFrom.setEnabled(false);
                                    if (tldraftJson.get("To_P").getAsString().equals("")) {

                                    } else {
                                        enterTo.setEnabled(false);
                                    }
                                }
                            }

                            TravelBike.setVisibility(View.GONE);
                            linBusMode.setVisibility(View.VISIBLE);
                            linBikeMode.setVisibility(View.GONE);
                            linMode.setVisibility(View.VISIBLE);

                            if (StrBus != null && !StrBus.isEmpty() && !StrBus.equals("null") && !StrBus.equals("")) {
                                StrBus = StrBus.replaceAll("^[\"']+|[\"']+$", "");
                                txtBusFrom.setText(StrBus);
                            }

                            if (StrTo != null && !StrTo.isEmpty() && !StrTo.equals("null") && !StrTo.equals("")) {
                                StrTo = StrTo.replaceAll("^[\"']+|[\"']+$", "");
                                txtBusTo.setText(StrTo);
                            }
                        } else {

                            TravelBike.setVisibility(View.VISIBLE);
                            linMode.setVisibility(View.VISIBLE);
                            linBusMode.setVisibility(View.GONE);
                            linBikeMode.setVisibility(View.VISIBLE);
                        }

                    }
                    GrandTotalAllowance = doubleAmount + tofuel;
                    /*Local Convenyance*/
                    if (lcDraftArray != null || lcDraftArray.size() != 0) {
                        localConDraft(lcDraftArray);
                    }
                    if (oeDraftArray != null || oeDraftArray.size() != 0) OeDraft(oeDraftArray);
                    if (trvldArray != null || trvldArray.size() != 0) trvldLocation(trvldArray);
                    if (ldArray != null || ldArray.size() != 0) {
                      /*  if (ldgAdd.getText().equals("+ Add")) {
                            ldgAdd.setText("- Remove");
                            lodgContvw.setVisibility(View.VISIBLE);
                            lodingDraft(ldArray);

                        } else {
                            ldgAdd.setText("+ Add");
                            lodgContvw.setVisibility(View.GONE);
                        }*/

                        lodingDraft(ldArray);


                    } else {

                        Log.v("LODGING_ARRAY_ELSE", String.valueOf(ldArray.size()));
                        jointLodging.setVisibility(View.GONE);
                        ldg_cin.setText("");
                        ldg_cout.setText("");
                        lodgStyLocation.setText("");
                        txtMyEligi.setText("Rs." + 0.00);
                        ldgWOBBal.setText("Rs." + 0.00);
                        lbl_ldg_eligi.setText("Rs." + 0.00);
                        edt_ldg_bill.setText("");
                        txt_ldg_type.setText("");
                        TotalDays.setVisibility(View.GONE);
                        stayDays.setVisibility(View.GONE);


                    }

                    calOverAllTotal(localCov, otherExp, tTotAmt);
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                }
            });

        } catch (Exception exception) {
        }
    }

    public void lodingDraft(JsonArray lodingDraft) {

        JsonArray jsonAddition = null;
        JsonObject ldraft = null;
        for (int i = 0; i < lodingDraft.size(); i++) {
            ldraft = (JsonObject) lodingDraft.get(i);

            jsonAddition = ldraft.getAsJsonArray("Additional");
            ldgAdd.setText("- Remove");
            lodgContvw.setVisibility(View.VISIBLE);
            lodgCont.setVisibility(View.VISIBLE);
            ldg_stayloc.setVisibility(View.VISIBLE);
            ldg_stayDt.setVisibility(View.VISIBLE);
            lodgJoin.setVisibility(View.VISIBLE);

            ldg_cin.setText(ldraft.get("Stay_Date").getAsString());
            ldg_cout.setText(ldraft.get("To_Date").getAsString());
            lodgStyLocation.setText(ldraft.get("Ldg_Stay_Loc").getAsString());

            Log.v("lodgStyLocation", lodgStyLocation.getText().toString());

            stayDays.setVisibility(View.VISIBLE);
            Double totlLdgAmt = Double.valueOf(ldraft.get("Total_Ldg_Amount").getAsString());
            Integer noday = Integer.valueOf(ldraft.get("NO_Of_Days").getAsString());

            txtLodgUKey.setText(ldraft.get("Ukey").getAsString());
            double elibs = Integer.valueOf(ldraft.get("Eligible").getAsString());
            /*            double elibs = elib * noday;*/

            txtMyEligi.setText("Rs." + new DecimalFormat("##0.00").format(elibs));

            double srtjdgAmt = Integer.valueOf(ldraft.get("Joining_Ldg_Amount").getAsString());
            txtJNEligi.setText("Rs." + new DecimalFormat("##0.00").format(srtjdgAmt));
            Double wobal = Double.valueOf(ldraft.get("WOB_Amt").getAsString());

            Log.v("ldgWOBBal", String.valueOf(wobal));
            ldgWOBBal.setText("Rs." + new DecimalFormat("##0.00").format(wobal));

            Log.v("ldgWOBBal_______", ldgWOBBal.getText().toString());

            edt_ldg_bill.setText(ldraft.get("Bill_Amt").getAsString());
            lbl_ldg_eligi.setText("Rs." + new DecimalFormat("##0.00").format(totlLdgAmt));

            txtStyDays.setText(ldraft.get("NO_Of_Days").getAsString());

            if (ldraft.get("Lodging_Type").getAsString().equals("Joined Stay")) {
                txt_ldg_type.setText("Joined Stay");
                TotalDays.setVisibility(View.VISIBLE);
            } else if (ldraft.get("Lodging_Type").getAsString().equals("Independent Stay")) {
                txt_ldg_type.setText("Independent Stay");
                lodgJoin.setVisibility(View.GONE);
                TotalDays.setVisibility(View.VISIBLE);

            } else {
                txt_ldg_type.setText("Stay At Relative's House");
            }

            JsonObject jsonObjectAdd = null;
            for (int l = 0; l < jsonAddition.size(); l++) {


                Log.e("LOCTAION_LRD", String.valueOf(jsonAddition.size()));

                jsonObjectAdd = (JsonObject) jsonAddition.get(l);

                jointLodging.setVisibility(View.VISIBLE);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);


                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                layoutParams.setMargins(15, 15, 15, 15);
                View rowView = inflater.inflate(R.layout.activity_loding_layout, null);
                jointLodging.addView(rowView, layoutParams);

                Integer jfd = jointLodging.indexOfChild(rowView);

                View jdV = jointLodging.getChildAt(jfd);
                edt_ldg_JnEmp = (EditText) jdV.findViewById(R.id.edt_ldg_JnEmp);
                txtJNName = (TextView) jdV.findViewById(R.id.txtJNName);
                txtJNDesig = (TextView) jdV.findViewById(R.id.txtJNDesig);
                txtJNDept = (TextView) jdV.findViewById(R.id.txtJNDept);
                txtJNHQ = (TextView) jdV.findViewById(R.id.txtJNHQ);
                txtJNMob = (TextView) jdV.findViewById(R.id.txtJNMob);
                txtJNMyEli = (TextView) jdV.findViewById(R.id.txtJNMyEli);

                edt_ldg_JnEmp.setText(jsonObjectAdd.get("Emp_Code").getAsString());
                txtJNName.setText(jsonObjectAdd.get("Sf_Name").getAsString());
                txtJNDesig.setText(jsonObjectAdd.get("Desig").getAsString());
                txtJNDept.setText(jsonObjectAdd.get("Dept").getAsString());
                txtJNHQ.setText(jsonObjectAdd.get("Sf_Hq").getAsString());
                txtJNMob.setText(jsonObjectAdd.get("Sf_Mobile").getAsString());
                /*                double jntEli = Integer.valueOf(ldraft.get("Ldg_Amount").getAsString());*//*
                txtJNMyEli.setText("Rs." + "0.00");*/


                float sum = jsonObjectAdd.get("Ldg_Amount").getAsFloat();
                txtJNMyEli.setText("Rs." + new DecimalFormat("##0.00").format(sum));

            }
        }

    }

    public void localConDraft(JsonArray lcDraft) {

        JsonArray jsonAddition = null;
        JsonObject lcdraftJson = null;
        for (int i = 0; i < lcDraft.size(); i++) {
            lcdraftJson = (JsonObject) lcDraft.get(i);

            jsonAddition = lcdraftJson.getAsJsonArray("Additional");
            String expCode = String.valueOf(lcdraftJson.get("Exp_Code"));
            String expFare = lcdraftJson.get("Exp_Amt").getAsString();
            lcUKey = lcdraftJson.get("Ukey").getAsString();

            Log.v("EXP_AMOUNT", expFare);

            expCode = expCode.replaceAll("^[\"']+|[\"']+$", "");
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            layoutParams.setMargins(15, 15, 15, 15);

            final View rowView = inflater.inflate(R.layout.activity_local_convenyance, null);
            linlocalCon.addView(rowView, layoutParams);
            localText.setVisibility(View.VISIBLE);
            localTotal.setVisibility(View.VISIBLE);

            lcSize = linlocalCon.indexOfChild(rowView);

            View LcchildView = linlocalCon.getChildAt(lcSize);
            localTotal.setVisibility(View.VISIBLE);
            editTexts = (TextView) (LcchildView.findViewById(R.id.local_enter_mode));
            editLaFare = (EditText) (LcchildView.findViewById(R.id.edt_la_fare));
            linLocalSpinner = (LinearLayout) LcchildView.findViewById(R.id.lin_loc_spiner);
            lcAttach = (ImageView) (LcchildView.findViewById(R.id.la_attach_iamg));
            lcPreview = (ImageView) (LcchildView.findViewById(R.id.img_prvw_lc));

            editTexts.setText(expCode);
            editLaFare.setText(expFare);
            if (!lcUKey.equals("")) {
                lcTxtUKey.setText(lcUKey);
            }


            if (lcdraftJson.get("Attachments").getAsString().equals("1")) {
                lcAttach.setVisibility(View.VISIBLE);
                lcPreview.setVisibility(View.VISIBLE);
            } else {
                lcAttach.setVisibility(View.GONE);
                lcPreview.setVisibility(View.GONE);
            }

            Dynamicallowance = (LinearLayout) LcchildView.findViewById(R.id.lin_allowance_dynamic);
            lcTxtUKey = (TextView) (LcchildView.findViewById(R.id.txt_lc_ukey));

            editLaFare.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    SumOFLCAmount();
                }
            });


            linLocalSpinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listOrderType.clear();
                    Integer lcPosCntS = linlocalCon.indexOfChild(rowView);
                    dynamicModeType(lcPosCntS);
                }
            });

            lcAttach.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CameraPermission cameraPermission = new CameraPermission(TAClaimActivity.this, getApplicationContext());
                    if (!cameraPermission.checkPermission()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                            cameraPermission.requestPermission();
                        }
                        Log.v("PERMISSION_NOT", "PERMISSION_NOT");
                    } else {
                        Log.v("PERMISSION", "PERMISSION");

                        popupCapture(786);

                        Integer lcPosCntS = linlocalCon.indexOfChild(rowView);
                        View view = linlocalCon.getChildAt(lcPosCntS);
                        editTexts = (TextView) (view.findViewById(R.id.local_enter_mode));
                        lcTxtUKey = (TextView) (view.findViewById(R.id.txt_lc_ukey));
                        editMode = editTexts.getText().toString();

                        if (lcTxtUKey.getText().toString().equals("")) {
                            DateFormat dfw = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            Calendar calobjw = Calendar.getInstance();
                            lcEditcnt = keyEk + mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code) + dfw.format(calobjw.getTime()).hashCode();
                            lcTxtUKey.setText(lcEditcnt);

                        } else {
                        }
                        LcUKey = lcTxtUKey.getText().toString();
                    }

                }
            });

            lcPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Integer valuelc = linlocalCon.indexOfChild(rowView);
                    View view = linlocalCon.getChildAt(valuelc);
                    editTexts = (TextView) (view.findViewById(R.id.local_enter_mode));
                    lcTxtUKeys = (TextView) (view.findViewById(R.id.txt_lc_ukey));
                    editMode = editTexts.getText().toString();
                    LcUKey = lcTxtUKeys.getText().toString();

                    DateTime = DateTime.replaceAll("^[\"']+|[\"']+$", "");

                    Intent stat = new Intent(getApplicationContext(), AttachementActivity.class);
                    stat.putExtra("position", LcUKey);
                    stat.putExtra("headTravel", "LC");
                    stat.putExtra("mode", editMode);
                    stat.putExtra("date", DateTime);
                    startActivity(stat);
                }
            });

            localConDisplay(expCode, jsonAddition, lcSize);


        }

        SumOFLCAmount();

    }

    public void OeDraft(JsonArray oEDraft) {
        //  JsonArray jsonAddition = null;
        JsonObject lcdraftJson = null;
        for (int i = 0; i < oEDraft.size(); i++) {
            lcdraftJson = (JsonObject) oEDraft.get(i);
            // jsonAddition = lcdraftJson.getAsJsonArray("Additional");
            String expCode = lcdraftJson.get("Exp_Code").getAsString();
            String expFare = lcdraftJson.get("Exp_Amt").getAsString();
            oeUKey = lcdraftJson.get("Ukey").getAsString();


            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            otherExpenseLayout.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            layoutParams.setMargins(15, 15, 15, 15);

            final View rowView = inflater.inflate(R.layout.activity_other_expense, null);
            LinearOtherAllowance.addView(rowView, layoutParams);
            OeSize = LinearOtherAllowance.getChildCount();

            oePosCnt = LinearOtherAllowance.indexOfChild(rowView);

            View childView = LinearOtherAllowance.getChildAt(oePosCnt);
            otherExpenseLayout.setVisibility(View.VISIBLE);
            oeEditext = (TextView) (childView.findViewById(R.id.other_enter_mode));
            edtOE = (EditText) (childView.findViewById(R.id.oe_fre_amt));
            oeAttach = (ImageView) (childView.findViewById(R.id.oe_attach_img));
            oePreview = (ImageView) (childView.findViewById(R.id.img_prvw_oe));
            linOtherSpinner = (LinearLayout) (childView.findViewById(R.id.lin_othr_spiner));
            oeTxtUKey = (TextView) (childView.findViewById(R.id.txt_oe_ukey));

            if (lcdraftJson.get("Attachments").getAsString().equals("1")) {
                oeAttach.setVisibility(View.VISIBLE);
                oePreview.setVisibility(View.VISIBLE);
            } else {
                oeAttach.setVisibility(View.GONE);
                oePreview.setVisibility(View.GONE);
            }


            oeEditext.setText(expCode);
            edtOE.setText(expFare);
            if (!oeUKey.equals("") && oeUKey != "" && !oeUKey.isEmpty() && oeUKey != null) {
                oeTxtUKey.setText(oeUKey);
            }

            oeAttach.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    CameraPermission cameraPermission = new CameraPermission(TAClaimActivity.this, getApplicationContext());
                    if (!cameraPermission.checkPermission()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            cameraPermission.requestPermission();
                        }
                        Log.v("PERMISSION_NOT", "PERMISSION_NOT");
                    } else {
                        Log.v("PERMISSION", "PERMISSION");
                        popupCapture(99);

                        Integer valuedfd = LinearOtherAllowance.indexOfChild(rowView);
                        View view = LinearOtherAllowance.getChildAt(valuedfd);
                        oeEditext = (TextView) (view.findViewById(R.id.other_enter_mode));
                        oeTxtUKeys = (TextView) (view.findViewById(R.id.txt_oe_ukey));
                        editMode = oeEditext.getText().toString();

                        if (oeTxtUKeys.getText().toString().equals("")) {
                            DateFormat dfw = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            Calendar calobjw = Calendar.getInstance();
                            oeEditCnt = keyEk + mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code) + dfw.format(calobjw.getTime()).hashCode();
                            oeTxtUKeys.setText(oeEditCnt);
                        }
                        OeUKey = oeTxtUKeys.getText().toString();
                    }
                }
            });

            oePreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Integer valuedfd = LinearOtherAllowance.indexOfChild(rowView);
                    View view = LinearOtherAllowance.getChildAt(valuedfd);
                    oeEditext = (TextView) (view.findViewById(R.id.other_enter_mode));
                    oeTxtUKeys = (TextView) (view.findViewById(R.id.txt_oe_ukey));
                    editMode = oeEditext.getText().toString();

                    OeUKey = oeTxtUKeys.getText().toString();
                    DateTime = DateTime.replaceAll("^[\"']+|[\"']+$", "");

                    Log.v("OE_UKEY", OeUKey);
                    Log.v("OE_mode", editMode);
                    Log.v("OE_date", DateTime);

                    Intent stat = new Intent(getApplicationContext(), AttachementActivity.class);
                    stat.putExtra("position", OeUKey);
                    stat.putExtra("headTravel", "OE");
                    stat.putExtra("mode", editMode);
                    stat.putExtra("date", DateTime);
                    startActivity(stat);

                }
            });


            edtOE.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {

                    SumOFOTAmount();

                }
            });


            OtherExpense = (LinearLayout) childView.findViewById(R.id.lin_other_expense_dynamic);
            linOtherSpinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    OtherExpenseList.clear();
                    Integer valuedf = LinearOtherAllowance.indexOfChild(rowView);
                    OtherExpenseMode(valuedf);

                }
            });


        }
        SumOFOTAmount();
    }

    public void trvldLocation(JsonArray traveldLoc) {

        try {
            JsonObject tldraftJson = null;
            for (int i = 0; i < traveldLoc.size(); i++) {

                if (StrToEnd.equals("0")) {


                } else {

                    tldraftJson = (JsonObject) traveldLoc.get(i);

                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View rowView = null;

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    layoutParams.setMargins(15, 15, 15, 15);

                    rowView = inflater.inflate(R.layout.travel_allowance_dynamic_one, null);

                    viw.setVisibility(View.GONE);
                    lin.setVisibility(View.GONE);

                    travelDynamicLoaction.addView(rowView, layoutParams);
                    crdDynamicLocation.setVisibility(View.VISIBLE);

                    View views = travelDynamicLoaction.getChildAt(i);
                    deleteButton = views.findViewById(R.id.delete_button);
                    etrTaFr = (EditText) views.findViewById(R.id.ta_edt_from);
                    etrTaTo = (EditText) views.findViewById(R.id.ta_edt_to);

                    if (!tldraftJson.get("From_P").getAsString().isEmpty() && !tldraftJson.get("From_P").getAsString().equals("")) {
                        etrTaFr.setText(tldraftJson.get("From_P").getAsString());
                    }

                    if (!tldraftJson.get("To_P").getAsString().isEmpty() && !tldraftJson.get("To_P").getAsString().equals("")) {
                        etrTaTo.setText(tldraftJson.get("To_P").getAsString());
                    }

                }
            }

        } catch (Exception e) {

        }

    }

    public void localConDisplay(String modeName, JsonArray jsonAddition, int position) {

        JsonObject jsonObjectAdd = null;
        List<EditText> users = new ArrayList<>();
        for (int l = 0; l < jsonAddition.size(); l++) {
            jsonObjectAdd = (JsonObject) jsonAddition.get(l);
            String edtValueb = String.valueOf(jsonObjectAdd.get("Ref_Value"));
            RelativeLayout childRel = new RelativeLayout(getApplicationContext());
            RelativeLayout.LayoutParams layoutparams_3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutparams_3.addRule(RelativeLayout.ALIGN_PARENT_START);
            layoutparams_3.setMargins(20, -10, 0, 0);
            edt1 = new EditText(getApplicationContext());
            edt1.setLayoutParams(layoutparams_3);
            edt1.setText(edtValueb.replaceAll("^[\"']+|[\"']+$", ""));
            edt1.setTextSize(13);
            childRel.addView(edt1);
            users.add(edt1);
            dynamicLabelList.add(jsonObjectAdd.get("Ref_Code").getAsString());
            if (l == jsonAddition.size() - 1) {
                usersByCountry.put(modeName, users);
            }
            View view = linlocalCon.getChildAt(position);
            Dynamicallowance = (LinearLayout) view.findViewById(R.id.lin_allowance_dynamic);
            Dynamicallowance.addView(childRel);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 144) {

            if (resultCode == RESULT_OK) {
                if (requestCode == 144) {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {
                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            // display your images
                            ImageFilePath filepath = new ImageFilePath();
                            fullPath = filepath.getPath(TAClaimActivity.this, mClipData.getItemAt(i).getUri());
                            lodgArrLst.add(fullPath);


                            getMulipart(lodUKey, fullPath, "LOD", "", "Room", "", "");

                        }
                    } else if (data.getData() != null) {
                        Uri item = data.getData();
                        ImageFilePath filepath = new ImageFilePath();
                        fullPath = filepath.getPath(TAClaimActivity.this, item);
                        lodgArrLst.add(fullPath);


                        getMulipart(lodUKey, fullPath, "LOD", "", "Room", "", "");

                    }
                }
            }
        } else if (requestCode == 124) {
            if (resultCode == RESULT_OK) {
                if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {
                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();
                        // display your images
                        ImageFilePath filepath = new ImageFilePath();
                        fullPath = filepath.getPath(TAClaimActivity.this, mClipData.getItemAt(i).getUri());

                        getMulipart(TlUKey, fullPath, "TL", "", editMode, "", "");

                    }
                } else if (data.getData() != null) {
                    Uri item = data.getData();
                    ImageFilePath filepath = new ImageFilePath();
                    fullPath = filepath.getPath(TAClaimActivity.this, item);


                    getMulipart(TlUKey, fullPath, "TL", "", editMode, "", "");

                }
            }

        } else if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                if (requestCode == 100) {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {
                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            ImageFilePath filepath = new ImageFilePath();
                            fullPath = filepath.getPath(TAClaimActivity.this, mClipData.getItemAt(i).getUri());

                            getMulipart(OeUKey, fullPath, "OE", "", editMode, "", "");


                        }
                    } else if (data.getData() != null) {

                        Uri item = data.getData();
                        ImageFilePath filepath = new ImageFilePath();
                        fullPath = filepath.getPath(TAClaimActivity.this, item);
                        getMulipart(OeUKey, fullPath, "OE", "", editMode, "", "");

                    }
                }
            }
        } else if (requestCode == 787) {
            if (resultCode == RESULT_OK) {
                if (requestCode == 787) {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {
                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            // display your images
                            ImageFilePath filepath = new ImageFilePath();
                            fullPath = filepath.getPath(TAClaimActivity.this, mClipData.getItemAt(i).getUri());

                            getMulipart(LcUKey, fullPath, "LC", "", editMode, "", "");

                        }
                    } else if (data.getData() != null) {

                        Uri item = data.getData();
                        ImageFilePath filepath = new ImageFilePath();
                        fullPath = filepath.getPath(TAClaimActivity.this, item);

                        getMulipart(LcUKey, fullPath, "LC", "", editMode, "", "");

                    }
                }
            }
        } else if (requestCode == 143 && resultCode == Activity.RESULT_OK) {

            finalPath = "/storage/emulated/0";
            filePath = outputFileUri.getPath();
            filePath = filePath.substring(1);
            filePath = finalPath + filePath.substring(filePath.indexOf("/"));


            getMulipart(lodUKey, filePath, "LOD", "", "Room", "", "");

        } else if (requestCode == 123 && resultCode == Activity.RESULT_OK) {
            finalPath = "/storage/emulated/0";
            filePath = outputFileUri.getPath();
            filePath = filePath.substring(1);
            filePath = finalPath + filePath.substring(filePath.indexOf("/"));

            getMulipart(TlUKey, filePath, "TL", "", editMode, "", "");

        } else if (requestCode == 99 && resultCode == Activity.RESULT_OK) {

            finalPath = "/storage/emulated/0";
            filePath = outputFileUri.getPath();
            filePath = filePath.substring(1);
            filePath = finalPath + filePath.substring(filePath.indexOf("/"));

            getMulipart(OeUKey, filePath, "OE", "", editMode, "", "");

        } else if (requestCode == 786 && resultCode == Activity.RESULT_OK) {

            finalPath = "/storage/emulated/0";
            filePath = outputFileUri.getPath();
            filePath = filePath.substring(1);
            filePath = finalPath + filePath.substring(filePath.indexOf("/"));

            getMulipart(LcUKey, filePath, "LC", "", editMode, "", "");
        }
    }


    public void pdfViewList() {
        dialog = new Dialog(TAClaimActivity.this, R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.row_pdf_viewer_list);
        dialog.show();
        LinearLayout pdf1 = dialog.findViewById(R.id.lin_pdf);
        LinearLayout pdf2 = dialog.findViewById(R.id.lin_pdf2);
        pdf1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent stat = new Intent(getApplicationContext(), PdfViewerActivity.class);
                stat.putExtra("PDF_ONE", "https://hap.sanfmcg.com/Travel%20and%20Daily%20Allowance%20Policy%20-%20Domestic%20Travel%20Annexure%20C-1_1%20Feb-21.pdf");
                startActivity(stat);

                dialog.dismiss();

            }
        });
        pdf2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent stat = new Intent(getApplicationContext(), PdfViewerActivity.class);
                stat.putExtra("PDF_ONE", "https://hap.sanfmcg.com/HAP_ANNEXURE_C.pdf");
                startActivity(stat);

                dialog.dismiss();
            }
        });
    }


    public void popupCapture(Integer attachName) {
        dialog = new Dialog(TAClaimActivity.this, R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_capture);
        dialog.show();
        TextView upload = dialog.findViewById(R.id.upload);
        TextView camera = dialog.findViewById(R.id.camera);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectMultiImage(attachName);
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureFile(attachName);
            }
        });
    }

    public void selectMultiImage(Integer attachName) {
        dialog.dismiss();
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), attachName + 1);

    }

    public void captureFile(Integer positionC) {
        dialog.dismiss();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        outputFileUri = FileProvider.getUriForFile(TAClaimActivity.this, getApplicationContext().getPackageName() + ".provider", new File(getExternalCacheDir().getPath(), Shared_Common_Pref.Sf_Code + "_" + System.currentTimeMillis() + ".jpeg"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, positionC);

    }

    public void submitData(String responseVal) {

        Log.e("COUNTATTACH", attachCountList.toString());
        JSONArray transHead = new JSONArray();
        JSONObject transJson = new JSONObject();
        JSONObject jsonData = new JSONObject();

        DateTime = DateTime.replaceAll("^[\"']+|[\"']+$", "");
        StrBus = StrBus.replaceAll("^[\"']+|[\"']+$", "");
        StrTo = StrTo.replaceAll("^[\"']+|[\"']+$", "");

        try {

            /*Head Json*/
            jsonData.put("SF_Code", SF_code);
            jsonData.put("exp_date", DateTime);
            jsonData.put("da_mode", StrDailyAllowance);
            jsonData.put("al_type", "");
            jsonData.put("from_place", StrBus);
            jsonData.put("to_place", StrTo);
            jsonData.put("al_amount", "0");
            jsonData.put("da_amount", TotDA);
            jsonData.put("lc_amount", sum);
            jsonData.put("oe_amount", sumsTot);
            jsonData.put("total_ldg_amt", totLodgAmt);
            jsonData.put("ta_total_amount", tofuel);
            jsonData.put("drvBrdAmt", drvBrdAmt);
            jsonData.put("trv_lc_amt", sumsTa);
            jsonData.put("gr_total", gTotal);
            jsonData.put("edt_remark", editTextRemarks.getText().toString());

            /*Daily Allowance*/
            JSONObject daAll = new JSONObject();
            daAll.put("all_name", txtDailyAllowance.getText());
            daAll.put("brd_amt", myBrdAmt);
            daAll.put("drvBrdAmt", drvBrdAmt);

            /*Lodging Save*/

            Log.e("txtStyDaystoString()", txtStyDays.getText().toString());
            Log.e("txtStyDaystoString()", txt_ldg_type.getText().toString());
            Log.e("txtStyDaystoString()", lodgStyLocation.getText().toString());
            Log.e("txtStyDaystoString()", ldg_cin.getText().toString());
            Log.e("txtStyDaystoString()", ldg_cout.getText().toString());
            Log.e("txtStyDaystoString()", txtMyEligi.getText().toString());
            Log.e("txtStyDaystoString()", edt_ldg_bill.getText().toString());
            Log.e("txtStyDaystoString()", ldgWOBBal.getText().toString());
            Log.e("txtStyDaystoString()", txtDrivEligi.getText().toString());
            Log.e("txtStyDaystoString()", txtJNEligi.getText().toString());
            Log.e("txtStyDaystoString()", lbl_ldg_eligi.getText().toString());

            String strMyEli = txtMyEligi.getText().toString().substring(txtMyEligi.getText().toString().indexOf(".") + 1).trim();
            String separator = ".";
            int intMyEli = strMyEli.lastIndexOf(separator);

            String strldgWobBal = ldgWOBBal.getText().toString().substring(ldgWOBBal.getText().toString().indexOf(".") + 1).trim();
            String separator1 = ".";
            int intMyldg = strldgWobBal.lastIndexOf(separator1);

            String strdrvElig = txtDrivEligi.getText().toString().substring(txtDrivEligi.getText().toString().indexOf(".") + 1).trim();
            String separator2 = ".";
            int intMyDrvElg = strdrvElig.lastIndexOf(separator2);

            String strJNEligi = txtJNEligi.getText().toString().substring(txtJNEligi.getText().toString().indexOf(".") + 1).trim();
            String separator3 = ".";
            int intJNEligi = strJNEligi.lastIndexOf(separator3);

            String strLdgEli = lbl_ldg_eligi.getText().toString().substring(lbl_ldg_eligi.getText().toString().indexOf(".") + 1).trim();
            String separator4 = ".";
            int intLdgEli = strLdgEli.lastIndexOf(separator4);


            JSONObject ldgSave = new JSONObject();
            ldgSave.put("ldg_type", txt_ldg_type.getText().toString());
            ldgSave.put("ldg_type_sty", lodgStyLocation.getText().toString());
            ldgSave.put("sty_dte", ldg_cin.getText().toString());
            ldgSave.put("to_dte", ldg_cout.getText().toString());
            ldgSave.put("elgble", strMyEli.substring(0, intMyEli));
            ldgSave.put("noOfDays", txtStyDays.getText().toString());
            ldgSave.put("bil_amt", edt_ldg_bill.getText().toString());
            ldgSave.put("wob_amt", strldgWobBal.substring(0, intMyldg));
            ldgSave.put("drv_ldg_amt", strdrvElig.substring(0, intMyDrvElg));
            ldgSave.put("jnt_ldg_amt", strJNEligi.substring(0, intJNEligi));
            ldgSave.put("total_ldg_amt", strLdgEli.substring(0, intLdgEli));
            ldgSave.put("attch_bill", "");
            ldgSave.put("u_key", txtLodgUKey.getText().toString());

            JSONArray ldgArySve = new JSONArray();
            for (int jd = 0; jd < jointLodging.getChildCount(); jd++) {
                View jdV = jointLodging.getChildAt(jd);
                edt_ldg_JnEmp = (EditText) jdV.findViewById(R.id.edt_ldg_JnEmp);
                txtJNName = (TextView) jdV.findViewById(R.id.txtJNName);
                txtJNDesig = (TextView) jdV.findViewById(R.id.txtJNDesig);
                txtJNDept = (TextView) jdV.findViewById(R.id.txtJNDept);
                txtJNHQ = (TextView) jdV.findViewById(R.id.txtJNHQ);
                txtJNMob = (TextView) jdV.findViewById(R.id.txtJNMob);
                txtJNMyEli = (TextView) jdV.findViewById(R.id.txtJNMyEli);
                String strJNMyEli = txtJNMyEli.getText().toString().substring(txtJNMyEli.getText().toString().indexOf(".") + 1).trim();
                String separator5 = ".";
                int intJNMyEli = strJNMyEli.lastIndexOf(separator5);
                JSONObject jsnLdgSve = new JSONObject();
                jsnLdgSve.put("emp_cde", edt_ldg_JnEmp.getText().toString());
                jsnLdgSve.put("emp_Name", txtJNName.getText().toString());
                jsnLdgSve.put("emp_Desig", txtJNDesig.getText().toString());
                jsnLdgSve.put("emp_Dept", txtJNDept.getText().toString());
                jsnLdgSve.put("emp_HQ", txtJNHQ.getText().toString());
                jsnLdgSve.put("emp_Mob", txtJNMob.getText().toString());
                jsnLdgSve.put("emp_ldg_amt", strJNMyEli.substring(0, intJNMyEli));
                ldgArySve.put(jsnLdgSve);

                Log.e("txtStyDaystoString()", edt_ldg_JnEmp.getText().toString());
                Log.e("txtStyDaystoString()", txtJNName.getText().toString());
                Log.e("txtStyDaystoString()", txtJNDesig.getText().toString());
                Log.e("txtStyDaystoString()", txtJNDept.getText().toString());
                Log.e("txtStyDaystoString()", txtJNHQ.getText().toString());
                Log.e("txtStyDaystoString()", txtJNMob.getText().toString());
            }

            ldgSave.put("Loding_Emp", ldgArySve);

            Log.v("LODGING_SAVE", ldgSave.toString());

            /*Travel Mode Json*/
            JSONObject trDet = new JSONObject();

            trDet.put("MOT", StrDaName);
            trDet.put("Start_Km", StartedKm);
            trDet.put("End_Km", ClosingKm);
            trDet.put("Tr_km", totalkm);
            trDet.put("Pr_km", PersonalKm);
            trDet.put("total_claim", totalPersonalKm);
            trDet.put("fuel_cha", strFuelAmount);
            trDet.put("fuel_amt", tofuel);
            trDet.put("st_km_img", "");
            trDet.put("ed_km_img", "");
            trDet.put("fuel_amount", strFuelAmount);
            trDet.put("ta_total_amount", sumsTotss);

            JSONArray trvLoc = new JSONArray();
            int travelBike = travelDynamicLoaction.getChildCount();

            Log.v("STRING_TO_END_SUB", StrToEnd);
            if (StrToEnd.equals("0")) {

                Log.v("TRAVEl_LOCATION", "1");

                for (int i = 0; i < travelBike; i++) {
                    JSONObject jsonTrLoc = new JSONObject();

                    View views = travelDynamicLoaction.getChildAt(i);
                    editText = views.findViewById(R.id.enter_mode);
                    enterFrom = views.findViewById(R.id.enter_from);
                    enterTo = views.findViewById(R.id.enter_to);
                    enterFare = views.findViewById(R.id.enter_fare);
                    deleteButton = views.findViewById(R.id.delete_button);
                    tvTxtUKeys = views.findViewById(R.id.txt_tv_ukey);

                    jsonTrLoc.put("mode", editText.getText().toString());
                    jsonTrLoc.put("from", enterFrom.getText().toString());
                    jsonTrLoc.put("to", enterTo.getText().toString());
                    jsonTrLoc.put("fare", enterFare.getText().toString());
                    jsonTrLoc.put("u_key", tvTxtUKeys.getText().toString());
                    jsonTrLoc.put("attach_count", AttachmentImg.get(editMode));
                    trvLoc.put(jsonTrLoc);
                }

            } else {

                Log.v("TRAVEl_LOCATION", "0");
                for (int i = 0; i < travelBike; i++) {
                    JSONObject jsonTrLoc = new JSONObject();
                    View views = travelDynamicLoaction.getChildAt(i);
                    etrTaFr = (EditText) views.findViewById(R.id.ta_edt_from);
                    etrTaTo = (EditText) views.findViewById(R.id.ta_edt_to);
                    jsonTrLoc.put("from", etrTaFr.getText().toString());
                    jsonTrLoc.put("to", etrTaTo.getText().toString());
                    trvLoc.put(jsonTrLoc);
                }
            }

            trDet.put("trv_loca", trvLoc);





            /*Local Convenyance*/
            JSONArray addExp = new JSONArray();
            int addExpSize = linlocalCon.getChildCount();

            for (int lc = 0; lc < addExpSize; lc++) {
                View view = linlocalCon.getChildAt(lc);
                editTexts = (TextView) (view.findViewById(R.id.local_enter_mode));
                editLaFare = (EditText) (view.findViewById(R.id.edt_la_fare));
                Dynamicallowance = (LinearLayout) view.findViewById(R.id.lin_allowance_dynamic);
                lcTxtUKeys = (TextView) (view.findViewById(R.id.txt_lc_ukey));
                editMode = editTexts.getText().toString();
                newEdt = usersByCountry.get(editMode);

                JSONObject lcMode = new JSONObject();
                lcMode.put("type", editMode);
                lcMode.put("attach_count", AttachmentImg.get(editMode));
                lcMode.put("total_amount", editLaFare.getText().toString());
                lcMode.put("u_key", lcTxtUKeys.getText().toString());
                lcMode.put("exp_type", "LC");

                JSONArray lcModeRef = new JSONArray();
                Log.e("ADD_SIZE", String.valueOf(newEdt.size()));

                for (int da = 0; da < newEdt.size(); da++) {

                    JSONObject AditionallLocalConvenyance = new JSONObject();
                    AditionallLocalConvenyance.put("KEY", dynamicLabelList.get(da));
                    AditionallLocalConvenyance.put("VALUE", newEdt.get(da).getText().toString());
                    lcModeRef.put(AditionallLocalConvenyance);
                }

                lcMode.put("ad_exp", lcModeRef);
                addExp.put(lcMode);

            }

            /*Other Expensive*/
            JSONArray othrExp = new JSONArray();
            int addOtherExp = LinearOtherAllowance.getChildCount();

            for (int OC = 0; OC < addOtherExp; OC++) {
                View view = LinearOtherAllowance.getChildAt(OC);
                oeEditext = (TextView) (view.findViewById(R.id.other_enter_mode));
                edtOE = (EditText) (view.findViewById(R.id.oe_fre_amt));
                OtherExpense = (LinearLayout) view.findViewById(R.id.lin_other_expense_dynamic);
                editMode = oeEditext.getText().toString();
                oeTxtUKey = (TextView) (view.findViewById(R.id.txt_oe_ukey));

                newEdt = userOtherExpense.get(editMode);
                JSONObject lcModes2 = new JSONObject();
                lcModes2.put("type", editMode);
                lcModes2.put("attach_count", AttachmentImg.get(editMode));
                lcModes2.put("total_amount", edtOE.getText().toString());
                lcModes2.put("u_key", oeTxtUKey.getText().toString());
                lcModes2.put("exp_type", "OE");

                JSONArray lcModeRef1 = new JSONArray();
                for (int da = 0; da < newEdt.size(); da++) {
                    JSONObject AditionallLocalConvenyance = new JSONObject();
                    AditionallLocalConvenyance.put("KEY", OEdynamicList.get(da));
                    AditionallLocalConvenyance.put("VALUE", newEdt.get(da).getText().toString());

                    lcModeRef1.put(AditionallLocalConvenyance);
                }
                lcModes2.put("ad_exp", lcModeRef1);
                othrExp.put(lcModes2);
                Log.v("OE_EXPENSE", lcModes2.toString());

            }

            jsonData.put("Add_Exp", addExp);
            jsonData.put("Other_Exp", othrExp);
            jsonData.put("Trv_details", trDet);
            jsonData.put("Lodg_details", ldgSave);
            jsonData.put("Da_Claim", daAll);

            transHead.put(jsonData);

            Log.e("TOTAL_JSON", transHead.toString());
            Log.e("TOTAL_JSON_HHHH", jsonData.toString());

        } catch (Exception e) {
            Log.e("TOTAL_JSON_OUT", e.toString());
        }

        /*ImageStore();*/

        Call<ResponseBody> submit;
        if (responseVal.equals("Save")) {
            submit = apiInterface.saveDailyAllowance(jsonData.toString());
        } else {
            submit = apiInterface.submitOfApp(jsonData.toString());
        }
        submit.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
                if (responseVal.equals("Save")) {
                    Toast.makeText(TAClaimActivity.this, "Saved Successfully ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TAClaimActivity.this, "Submitted Successfully ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    public void getMulipart(String count, String path, String x, String imageKEY, String mode, String from, String to) {
        Log.v("PATH_IMAGE", path);
        MultipartBody.Part imgg = convertimg("file", path);
        Log.v("PATH_IMAGE_imgg", String.valueOf(imgg));
        sendImageToServer(count, x, mode, from, to, imgg);
    }

    public MultipartBody.Part convertimg(String tag, String path) {
        MultipartBody.Part yy = null;
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
        return yy;
    }

    public void callApi(String date, String OS) {

        ArrayList listValue = new ArrayList();
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
                            String jsonData = null;
                            jsonData = response.body().string();
                            Log.v("response_data", jsonData);
                            array = new ArrayList<>();

                            JSONObject js = new JSONObject(jsonData);
                            JSONArray jsnArValue = js.getJSONArray("ExpenseWeb");
                            for (int i = 0; i < jsnArValue.length(); i++) {
                                JSONObject json_oo = jsnArValue.getJSONObject(i);
                                Exp_Name = json_oo.getString("Name");
                                shortName = json_oo.getString("Short_Name");
                                Id = String.valueOf(json_oo.get("ID"));
                                userEnter = json_oo.getString("user_enter");
                                attachment = json_oo.getString("Attachemnt");
                                maxAllowonce = json_oo.getString("Max_Allowance");

                                Log.v("Response_Count", shortName + " : " + attachment + " : " + Exp_Name);

                                listValue.add(shortName);
                                AttachmentImg.put(Exp_Name, attachment);

                                HashSet<String> listToSet = new HashSet<String>(listValue);
                                listWithoutDuplicates = new ArrayList<String>(listToSet);

                                if (shortName.equals("Daily Allowance")) {
                                    DA.add(Exp_Name);
                                }

                                if (shortName.equals("Other Expense")) {
                                    OE.add(Exp_Name);
                                }
                                if (shortName.equals("Local Conveyance")) {
                                    LC.add(Exp_Name);
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

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        customDialog.dismiss();
        if (type == 9) {
            stayDays.setVisibility(View.VISIBLE);
            ValCd = myDataset.get(position).getId();
            String Valname = myDataset.get(position).getName();

            if (DriverNeed.equalsIgnoreCase("true")) {
                drvldgEAra.setVisibility(View.VISIBLE);
                if (drvldgEliAmt == null) drvldgEliAmt = "0.0";
                drvldgEliAmt = drvldgEliAmt.replaceAll("^[\"']+|[\"']+$", "");
                ldgDrvEligi = Double.valueOf(drvldgEliAmt);
                txtDrivEligi.setText("Rs." + new DecimalFormat("##0.00").format(ldgDrvEligi));
            } else {
                drvldgEAra.setVisibility(View.GONE);
            }

            txt_ldg_type.setText(Valname);
            lodgCont.setVisibility(View.VISIBLE);
            ldg_stayloc.setVisibility(View.GONE);
            ldg_stayDt.setVisibility(View.GONE);
            lodgJoin.setVisibility(View.GONE);
            JNLdgEAra.setVisibility(View.GONE);
            edt_ldg_bill.setVisibility(View.GONE);
            lblHdBill.setVisibility(View.GONE);
            linImgPrv.setVisibility(View.GONE);
            viewBilling.setVisibility(View.GONE);
            lblHdBln.setVisibility(View.GONE);
            ldgWOBBal.setVisibility(View.GONE);
            if (myldgEliAmt == "") myldgEliAmt = "0.0";

            myldgEliAmt = myldgEliAmt.replaceAll("^[\"']+|[\"']+$", "");
            jointLodging.removeAllViews();
            ldgEliAmt = Double.valueOf(myldgEliAmt);
            txtMyEligi.setText("Rs." + new DecimalFormat("##0.00").format(ldgEliAmt));

            Log.e("TXT_MY_ELIGIBLE", txtMyEligi.getText().toString().substring(3, 7));
            SumOFJointLodging();
            SumOFLodging();
            if (ValCd == "JS") {
                lodgJoin.setVisibility(View.VISIBLE);
                JNLdgEAra.setVisibility(View.VISIBLE);
                img_lodg_prvw.setVisibility(View.VISIBLE);
                linImgPrv.setVisibility(View.VISIBLE);
                viewBilling.setVisibility(View.VISIBLE);
                stayDays.setVisibility(View.GONE);
                /*tTotAmt = 0;*/
                ttLod = 1;
                txtMyEligi.setText("Rs." + new DecimalFormat("##0.00").format(ldgEliAmt));
                lodingView();
            }
            if (ValCd != "RS") {
                ldg_stayloc.setVisibility(View.VISIBLE);
                ldg_stayDt.setVisibility(View.VISIBLE);
                lblHdBill.setVisibility(View.VISIBLE);
                edt_ldg_bill.setVisibility(View.VISIBLE);
                lblHdBln.setVisibility(View.VISIBLE);
                ldgWOBBal.setVisibility(View.VISIBLE);
                linImgPrv.setVisibility(View.VISIBLE);
                viewBilling.setVisibility(View.VISIBLE);
                stayDays.setVisibility(View.GONE);
                ttLod = 1;
                /*tTotAmt = 0;*/
                txtMyEligi.setText("Rs." + new DecimalFormat("##0.00").format(ldgEliAmt));
                ldgWOBBal.setText("Rs." + new DecimalFormat("##0.00").format(ldgEliAmt));
                lbl_ldg_eligi.setText("Rs." + new DecimalFormat("##0.00").format(ldgEliAmt));
                img_lodg_prvw.setVisibility(View.VISIBLE);
            }

            ldg_cin.setText("");
            ldg_cout.setText("");
            lodgStyLocation.setText("");
            if (ldg_cin.getText().toString().equals("") || ldg_cout.getText().toString().equals("")) {
                TotalDays.setVisibility(View.GONE);
            } else {
                TotalDays.setVisibility(View.VISIBLE);
            }
            edt_ldg_bill.setText("");

        }
        if (type == 10) {
            txt_date.setText(myDataset.get(position).getName());
            Log.d("JSON_VALUE", myDataset.get(position).getId());
            DateTime = myDataset.get(position).getId();
            DateTime = DateTime.replaceAll("^[\"']+|[\"']+$", "");
            displayTravelMode(myDataset.get(position).getId());
            travelDynamicLoaction.removeAllViews();
            linAddAllowance.setVisibility(View.VISIBLE);
            linlocalCon.removeAllViews();
            LinearOtherAllowance.removeAllViews();
            localText.setText("");
            OeText.setText("");
            localTotal.setVisibility(View.GONE);
            otherExpenseLayout.setVisibility(View.GONE);
            linAll.setVisibility(View.VISIBLE);
            linRemarks.setVisibility(View.VISIBLE);
            linFareAmount.setVisibility(View.VISIBLE);
            linback.setVisibility(View.GONE);

        } else if (type == 11) {
            modeTextView.setText(myDataset.get(position).getName());
        } else if (type == 8) {

            Integer editTextPosition = myDataset.get(position).getPho();
            View view = travelDynamicLoaction.getChildAt(editTextPosition);
            editText = (TextView) (view.findViewById(R.id.enter_mode));
            editText.setText(myDataset.get(position).getName());

        } else if (type == 80) {
            editTextPositionss = myDataset.get(position).getPho();
            View view = linlocalCon.getChildAt(editTextPositionss);
            editTexts = (TextView) (view.findViewById(R.id.local_enter_mode));
            edtLcFare = (EditText) (view.findViewById(R.id.edt_la_fare));
            lcAttach = (ImageView) (view.findViewById(R.id.la_attach_iamg));
            lcPreview = (ImageView) (view.findViewById(R.id.img_prvw_lc));
            edtLcFare.setText("");
            editTexts.setText(myDataset.get(position).getName());
            StrModeValue = myDataset.get(position).getName();
            Dynamicallowance = (LinearLayout) view.findViewById(R.id.lin_allowance_dynamic);
            Dynamicallowance.removeAllViews();
            LocalConvenyanceApi(StrModeValue);

            if (AttachmentImg.get(StrModeValue).equals("1")) {
                lcAttach.setVisibility(View.VISIBLE);
                lcPreview.setVisibility(View.VISIBLE);
            } else {
                lcAttach.setVisibility(View.GONE);
                lcPreview.setVisibility(View.GONE);
            }

        } else if (type == 90) {

            editTextPositionss = myDataset.get(position).getPho();
            View view = LinearOtherAllowance.getChildAt(editTextPositionss);
            oeEditext = (TextView) (view.findViewById(R.id.other_enter_mode));
            oeAttach = (ImageView) (view.findViewById(R.id.oe_attach_img));
            oePreview = (ImageView) (view.findViewById(R.id.img_prvw_oe));
            OtherExpense = (LinearLayout) view.findViewById(R.id.lin_other_expense_dynamic);
            oeEditext.setText(myDataset.get(position).getName());
            StrModeValue = myDataset.get(position).getName();
            Log.e("StrMode", StrModeValue);
            OtherExpense.removeAllViews();
            OtherExpenseApi(StrModeValue);

            attachCountList.add(AttachmentImg.get(StrModeValue));
            Log.e("COUNTATTACH", attachCountList.toString());
            Log.e("COUNTATTACH", AttachmentImg.get(StrModeValue));

            if (AttachmentImg.get(StrModeValue).equals("1")) {
                oeAttach.setVisibility(View.VISIBLE);
                oePreview.setVisibility(View.VISIBLE);
            } else {
                oeAttach.setVisibility(View.GONE);
                oePreview.setVisibility(View.GONE);
            }


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

        } else if (type == 1) {
            enterMode.setText(myDataset.get(position).getName());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
    }

    public void localCon(Integer countPosition) {

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

                userType = new TypeToken<ArrayList<com.hap.checkinproc.Model_Class.ModeOfTravel>>() {
                }.getType();
                modelOfTravel = gson.fromJson(new Gson().toJson(response.body()), userType);
                for (int i = 0; i < modelOfTravel.size(); i++) {
                    String id = String.valueOf(modelOfTravel.get(i).getStEndNeed());
                    String name = modelOfTravel.get(i).getName();
                    String modeId = String.valueOf(modelOfTravel.get(i).getId());
                    String driverMode = String.valueOf(modelOfTravel.get(i).getDriverNeed());

                    Model_Pojo = new Common_Model(name, id, modeId, "", countPosition);
                    if (id.equals("0")) {
                        modelTravelType.add(Model_Pojo);
                    }
                }

                customDialog = new CustomListViewDialog(TAClaimActivity.this, modelTravelType, 8);
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

    public void LDGType() {

        mCommon_model_spinner = new Common_Model("Independent Stay", "IS");
        ldgModes.add(mCommon_model_spinner);
        mCommon_model_spinner = new Common_Model("Joined Stay", "JS");
        ldgModes.add(mCommon_model_spinner);
        mCommon_model_spinner = new Common_Model("Stay At Relaytive's House", "RS");
        ldgModes.add(mCommon_model_spinner);

        customDialog = new CustomListViewDialog(TAClaimActivity.this, ldgModes, 9);
        Window window = customDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        customDialog.show();
    }

    public void dynamicModeType(Integer poisition) {

        for (int i = 0; i < LC.size(); i++) {
            String name = LC.get(i);
            mCommon_model_spinner = new Common_Model(name, name, "", "", poisition);
            listOrderType.add(mCommon_model_spinner);
        }
        customDialog = new CustomListViewDialog(TAClaimActivity.this, listOrderType, 80);
        Window window = customDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        customDialog.show();
    }

    public void OtherExpenseMode(Integer poisition) {
        for (int i = 0; i < OE.size(); i++) {
            String name = OE.get(i);
            mCommon_model_spinner = new Common_Model(name, name, "", "", poisition);
            OtherExpenseList.add(mCommon_model_spinner);
        }
        customDialog = new CustomListViewDialog(TAClaimActivity.this, OtherExpenseList, 90);
        Window window = customDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        customDialog.show();
    }

    public void LocalConvenyanceApi(String sss) {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        try {

            JSONObject jj = new JSONObject();
            jj.put("Ta_Date", "");
            jj.put("div", div);
            jj.put("sf", SF_code);
            jj.put("rSF", SF_code);
            jj.put("State_Code", State_Code);
            Log.v("json_obj_ta", jj.toString());
            Call<ResponseBody> Callto = apiInterface.getDailyAllowance(jj.toString());
            Callto.enqueue(new Callback<ResponseBody>() {
                @SuppressLint({"ResourceType", "NewApi"})
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.isSuccessful()) {

                            String jsonData = null;
                            jsonData = response.body().string();
                            Log.v("response_data", jsonData);
                            JSONObject js = new JSONObject(jsonData);
                            JSONArray jsnArValue = js.getJSONArray("ExpenseWeb");

                            for (int i = 0; i < jsnArValue.length(); i++) {
                                JSONObject jsonHeaderObject = jsnArValue.getJSONObject(i);
                                String Exp_Name = jsonHeaderObject.getString("Name");
                                if (Exp_Name.equals(sss)) {
                                    JSONArray additionArray = null;
                                    additionArray = jsonHeaderObject.getJSONArray("value");

                                    List<EditText> users = new ArrayList<>();
                                    for (int l = 0; l <= additionArray.length(); l++) {
                                        JSONObject json_in = additionArray.getJSONObject(l);
                                        dynamicLabel = json_in.getString("Ad_Fld_Name");
                                        dynamicLabelList.add(dynamicLabel);
                                        RelativeLayout childRel = new RelativeLayout(getApplicationContext());
                                        RelativeLayout.LayoutParams layoutparams_3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                        layoutparams_3.addRule(RelativeLayout.ALIGN_PARENT_START);
                                        layoutparams_3.setMargins(20, -10, 0, 0);
                                        edt = new EditText(getApplicationContext());
                                        edt.setLayoutParams(layoutparams_3);
                                        edt.setHint(dynamicLabel);
                                        edt.setId(12345);
                                        edt.setTextSize(13);
                                        edt.setTextColor(R.color.grey_500);
                                        childRel.addView(edt);
                                        users.add(edt);

                                        if (l == additionArray.length() - 1) {
                                            usersByCountry.put(sss, users);
                                        }
                                        View view = linlocalCon.getChildAt(editTextPositionss);
                                        Dynamicallowance = (LinearLayout) view.findViewById(R.id.lin_allowance_dynamic);
                                        Dynamicallowance.addView(childRel);
                                    }
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

    public void OtherExpenseApi(String sss) {

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        try {
            JSONObject jj = new JSONObject();
            jj.put("Ta_Date", "");
            jj.put("div", div);
            jj.put("sf", SF_code);
            jj.put("rSF", SF_code);
            jj.put("State_Code", State_Code);
            Log.v("json_obj_ta", jj.toString());
            Call<ResponseBody> Callto = apiInterface.getDailyAllowance(jj.toString());
            Callto.enqueue(new Callback<ResponseBody>() {
                @SuppressLint({"ResourceType", "NewApi"})
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.isSuccessful()) {
                            Log.v("print_upload_file_true", "ggg" + response);
                            String jsonData = null;
                            jsonData = response.body().string();
                            Log.v("response_data", jsonData);
                            JSONObject js = new JSONObject(jsonData);
                            JSONArray jsnArValue = js.getJSONArray("ExpenseWeb");

                            for (int i = 0; i < jsnArValue.length(); i++) {
                                JSONObject jsonHeaderObject = jsnArValue.getJSONObject(i);
                                String Exp_Name = jsonHeaderObject.getString("Name");

                                if (Exp_Name.equals(sss)) {
                                    JSONArray additionArray = null;
                                    additionArray = jsonHeaderObject.getJSONArray("value");
                                    List<EditText> otherExpenseEdit = new ArrayList<>();
                                    for (int l = 0; l <= additionArray.length(); l++) {
                                        JSONObject json_in = additionArray.getJSONObject(l);
                                        OEdynamicLabel = json_in.getString("Ad_Fld_Name");
                                        OEdynamicList.add(OEdynamicLabel);

                                        RelativeLayout childRel = new RelativeLayout(getApplicationContext());
                                        RelativeLayout.LayoutParams layoutparams_3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                        layoutparams_3.addRule(RelativeLayout.ALIGN_PARENT_END);
                                        layoutparams_3.setMargins(20, 10, 0, 0);
                                        edt = new EditText(getApplicationContext());
                                        edt.setLayoutParams(layoutparams_3);
                                        for (int da = 0; da < OEdynamicList.size(); da++) {
                                            edt.setHint(OEdynamicList.get(da));
                                        }
                                        edt.setId(12345);
                                        edt.setTextSize(13);
                                        edt.setTextColor(R.color.grey_500);
                                        edt.setBackgroundResource(R.drawable.textbox_bg);
                                        childRel.addView(edt);
                                        otherExpenseEdit.add(edt);
                                        if (l == additionArray.length() - 1) {
                                            userOtherExpense.put(sss, otherExpenseEdit);
                                        }
                                        View view = LinearOtherAllowance.getChildAt(editTextPositionss);
                                        OtherExpense = (LinearLayout) view.findViewById(R.id.lin_other_expense_dynamic);
                                        OtherExpense.addView(childRel);
                                    }
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

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    TAClaimActivity.super.onBackPressed();
                }
            });

    @Override
    public void onBackPressed() {
    }

    /*Imageview */
    private void sendImageToServer(String count, String HeadTravel, String Mode, String from, String To, MultipartBody.Part imgg) {

        long nano_startTime = System.nanoTime();
        Log.e("nano_startTime", String.valueOf(nano_startTime));
        ImageUKey = keyEk + mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code) + nano_startTime;

        DateTime = DateTime.replaceAll("^[\"']+|[\"']+$", "");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> mCall = apiInterface.taImage(ImageUKey, count, HeadTravel, Mode, DateTime, mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code), from, To, imgg);
        Log.e("SEND_IMAGE_SERVER", mCall.request().toString());

        mCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    if (response.isSuccessful()) {
                        String jsonData = null;
                        jsonData = response.body().string();
                        JSONObject js = new JSONObject(jsonData);
                    }

                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("SEND_IMAGE_Response", "ERROR");
            }
        });
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


    public void MaxMinDateTo(String strMinDate) {
        Log.e("MAX_DATE_TWO", " " + strMinDate);

        String[] separated1 = strMinDate.split("-");
        separated1[0] = separated1[0].trim();
        separated1[1] = separated1[1].trim();
        separated1[2] = separated1[2].trim();

        tominYear = separated1[0];
        tominMonth = separated1[1];
        tominDay = separated1[2];

    }


    public void difference() {

        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {

            Date dateBefore = myFormat.parse(ldg_cin.getText().toString());
            Date dateAfter = myFormat.parse(ldg_cout.getText().toString());
            long difference = dateAfter.getTime() - dateBefore.getTime();

            long diffSecondss = difference / 1000 % 60;
            long diffMinutess = difference / (60 * 1000) % 60;
            long diffHourss = difference / (60 * 60 * 1000) % 24;
            long diffDayss = difference / (24 * 60 * 60 * 1000);
            System.out.println("Number of Days between dates: " + "Total hr: " + diffHourss + " Total mins:" + diffMinutess);
            System.out.println("Number of Days between dates: " + diffDayss + "" + diffHourss + "" + diffMinutess + "" + diffSecondss);

            if (diffDayss == 0) {
                diffDayss = diffDayss + 1;
            }
            stayTotal = String.valueOf(diffDayss);
            if (!stayTotal.equals("")) {
                TotalDays.setVisibility(View.VISIBLE);
                txtStyDays.setText(stayTotal);
                stayDays.setVisibility(View.VISIBLE);
                diffDayss = diffDayss + 1;
                stayEgTotal = diffDayss * ldgEliAmt;

                txtMyEligi.setText("Rs." + new DecimalFormat("##0.00").format(stayEgTotal));
                ldgWOBBal.setText("Rs." + new DecimalFormat("##0.00").format(stayEgTotal));

                SumOFLodging();

            } else {
                stayDays.setVisibility(View.GONE);
                TotalDays.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void scroll() {

        mLc.post(new Runnable() {
            @Override
            public void run() {

                mLc.fullScroll(View.FOCUS_DOWN);

            }
        });
    }
}
package com.hap.checkinproc.Activity;

import android.animation.ArgbEvaluator;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
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
import com.hap.checkinproc.Activity_Hap.PayslipFtp;
import com.hap.checkinproc.Activity_Hap.ProductImageView;
import com.hap.checkinproc.Activity_Hap.ViewTAStatus;
import com.hap.checkinproc.Common_Class.CameraPermission;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.LocationEvents;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.FuelListAdapter;
import com.hap.checkinproc.common.LocationFinder;
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

public class TAViewStatus extends AppCompatActivity {
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
    FuelListAdapter fuelListAdapter;
    Uri outputFileUri;
    RecyclerView mFuelRecycler;

    Double fuelAmt = 0.0;

    LinearLayout Dynamicallowance, OtherExpense, localTotal, otherExpenseLayout, linAll, linRemarks,
            linFareAmount, ldg_typ_sp, linLocalSpinner, linOtherSpinner, lodgCont, lodgContvw, ldg_stayloc, ldg_stayDt,
            lodgJoin, ldgEAra, ldgMyEAra, JNLdgEAra, drvldgEAra, jointLodging, vwBoarding, vwDrvBoarding,
            linAddAllowance, diverAllowanceLinear, LDailyAllowance, LOtherExpense, LLocalConve, LinearOtherAllowance,
            linlocalCon, linBusMode, linBikeMode, linMode, travelDynamicLoaction, linDailyAllowance, linback, lin,
            linImgPrv, TotalDays, stayDays, linEarly, linLate, linContinueStay, linCheckOut, vwldgBillAmt, linearConView,
            linearApprovals, lineAppRej;
    LinearLayout viewContinue;
    CardView card_date, TravelBike, crdDynamicLocation, ldg_ara;

    TextView txt_date, txt_ldg_type, TxtStartedKm, TxtClosingKm, modeTextView, travelTypeMode,
            TotalTravelledKm, txtBusFrom, txtBusTo, txtTaClaim, PersonalTextKM, PersonalKiloMeter,
            txtDailyAllowance, editText, ldg_cin, ldg_cout, txtJNName, txtJNDesig, txtJNDept, txtJNHQ, txtJNMob,
            lblHdBill, lblHdBln, ldgWOBBal, ldgAdd, txtJNMyEli, txtMyEligi, txtDrivEligi, lbl_ldg_eligi, txt_totDA,
            fuelAmount, TextTotalAmount, editTexts, oeEditext, localText, OeText, grandTotal, txtallamt, txt_BrdAmt,
            txt_DrvBrdAmt, txtJointAdd, txtJNEligi, txtTAamt, txtDesig, txtDept, txtEmpId, txtName, oeTxtUKey, oeTxtUKeys,
            lcTxtUKey, lcTxtUKeys, tvTxtUKey, tvTxtUKeys, txtMaxKm, txtDrvrBrod, txtStyDays, txtLodgUKey, txtTaAmt, txtDate, txtTotalAmt, txtHQ, txtTrvlMode, txtDA,
            txtTL, txtLA, txtLC, txtOE, txtReject, txtMobile;

    EditText enterMode, enterFrom, enterTo, enterFare, etrTaFr, etrTaTo, editTextRemarks, editLaFare, edtOE, edt, edt1, edt_ldg_JnEmp,
            edt_ldg_bill, edtLcFare, lodgStyLocation, earCheckIn, earCheckOut, latCheckIn, latCheckOut, edtEarBill, edtLateBill;

    ImageView deleteButton, previewss, taAttach, lcAttach, lcDelet, oeAttach, oeDelete, lcPreview, oePreview, endkmimage, startkmimage,
            img_lodg_prvw, mapZoomIn, imgBck;

    String SF_code = "", div = "", State_Code = "", StartedKm = "", ClosingKm = "", ModeOfTravel = "", PersonalKm = "",
            DriverNeed = "", DateForAPi = "", DateTime = "", shortName = "", Exp_Name = "", Id = "", userEnter = "",
            attachment = "", maxAllowonce = "", strRetriveType = "", StrToEnd = "", StrBus = "", StrTo = "", StrDaName = "",
            OEdynamicLabel = "", strFuelAmount = "", StrModeValue = "", dynamicLabel = "", StrDailyAllowance = "", ldgEmpName = "",
            witOutBill = "", ValCd = "", fullPath = "", filePath = "", editMode = "", allowanceAmt = "", myldgEliAmt = "", myBrdEliAmt = "",
            drvldgEliAmt = "", drvBrdEliAmt = "", strGT = "", totLodgAmt = "", start_Image = "", End_Imge = "", finalPath = "",
            attach_Count = "", ImageURl = "", keyEk = "EK", oeEditCnt = "", lcEditcnt = "", tvEditcnt = "", OeUKey = "",
            LcUKey = "", TlUKey = "", lcUKey = "", oeUKey = "", ImageUKey = "", taAmt = "", stayTotal = "", lodUKey = "",
            lodgEarly = "", lodgLate = "", Ta_DATE = "", SlStart = "", sfCode = "",
            tominYear = "", tominMonth = "", sty_date = "", tominDay = "", ConStay = "", ErlyStay = "", LteStay = "",
            ErlyChecIn = "", ErlyChecOut = "", ErlyAmt = "", LteAmt = "", LteChecIn = "", LteChecOut = "", GrandTotalAMT = "";

    Integer totalkm = 0, totalPersonalKm = 0, Pva, C = 0, S = 0, editTextPositionss,
            oePosCnt = 0, lcPosCnt = 0, tvSize = 0, ttLod = 0, cnSty = 0, erlSty = 0, lteSty = 0;

    int size = 0, lcSize = 0, OeSize = 0, daysBetween = 0;
    long styDate = 0;

    Double tofuel = 0.0, ldgEliAmt = 0.0, ldgDrvEligi = 0.0, gTotal = 0.0, TotLdging = 0.0,
            GrandTotalAllowance = 0.0, fAmount = 0.0, doubleAmount = 0.0, myBrdAmt = 0.0, drvBrdAmt = 0.0,
            otherExp = 0.0, localCov = 0.0, sum = 0.0, sumsTotss = 0.0, sumsTot = 0.0;

    double TotDA = 0.0, sTotal = 0.0, sums = 0.0, sumsTa = 0.0, tTotAmt = 0.0, stayEgTotal = 0.0;
    float tJointAmt = 0;


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
    List<com.hap.checkinproc.Model_Class.ModeOfTravel> modelOfTravel;
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
    CheckBox mChckCont, mChckEarly, mChckLate;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editors;
    JsonArray jsonArray = null;
    JsonArray jsonExpHead = null;
    JsonArray lcDraftArray = null;
    JsonArray oeDraftArray = null;
    JsonArray trvldArray = null;
    JsonArray ldArray = null;
    JsonArray travelDetails = null;
    JsonArray LodingCon = null;
    JsonArray StayDate = null;
    JsonArray jsonFuelAllowance = null;
    AppCompatEditText appCompatEditText;


    LinearLayout linAccept, linReject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_a_view_status);
        startService(new Intent(this, TimerService.class));
        mCommon_class = new Common_Class(this);
        appCompatEditText = findViewById(R.id.reason);

        Ta_DATE = String.valueOf(getIntent().getSerializableExtra("TA_Date"));

        SlStart = String.valueOf(getIntent().getSerializableExtra("Sl_No"));
        sfCode = String.valueOf(getIntent().getSerializableExtra("sfCode"));

        Log.v("TA_DATE", Ta_DATE);
        displayTravelMode();
        mShared_common_pref = new Shared_Common_Pref(this);
        getToolbar();
        gson = new Gson();
        temaplateList = new ArrayList<>();
        dynamicLabelList = new ArrayList<>();
        OEdynamicList = new ArrayList<>();
        attachCountList = new ArrayList<>();

        txt_date = findViewById(R.id.txt_date);
        card_date = findViewById(R.id.card_date);
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
        txtJNEligi = findViewById(R.id.txtJNEligi);
        ldgAdd = findViewById(R.id.ldg_Add);
        mapZoomIn = findViewById(R.id.map_zoom);
        txtTAamt = findViewById(R.id.txt_trav_loca);

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
        earCheckIn = findViewById(R.id.early_check_in);
        earCheckOut = findViewById(R.id.early_check_out);
        latCheckIn = findViewById(R.id.late_check_in);
        latCheckOut = findViewById(R.id.late_check_out);
        CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
        mLc = findViewById(R.id.lc_srcoll);
        linImgPrv = findViewById(R.id.lin_img_prv);
        viewBilling = findViewById(R.id.bill_view);
        linEarly = findViewById(R.id.lin_early);
        linLate = findViewById(R.id.lin_late);
        linContinueStay = findViewById(R.id.lin_indep_sty);
        linCheckOut = findViewById(R.id.lin_chck_out);
        mChckCont = findViewById(R.id.chk_cnt_sty);
        mChckEarly = findViewById(R.id.chk_early);
        mChckLate = findViewById(R.id.chk_late);
        edtEarBill = findViewById(R.id.edt_earl_bil);
        edtLateBill = findViewById(R.id.lat_lod_bil);
        linearConView = findViewById(R.id.linear_con);
        viewContinue = (LinearLayout) findViewById(R.id.lin_con_sty);

        mFuelRecycler = findViewById(R.id.recycler_fuel);
        mFuelRecycler.setHasFixedSize(true);
        mFuelRecycler.setLayoutManager(new LinearLayoutManager(this));
        mFuelRecycler.setNestedScrollingEnabled(false);

        lineAppRej = findViewById(R.id.lin_app_rejec);
        linearApprovals = findViewById(R.id.linea_details);


        if (getIntent().getSerializableExtra("TA_APPROVAL").equals("1")) {
            linearApprovals.setVisibility(View.VISIBLE);
            lineAppRej.setVisibility(View.VISIBLE);
        } else {
            linearApprovals.setVisibility(View.GONE);
            lineAppRej.setVisibility(View.GONE);
        }

        GrandTotalAMT = String.valueOf(getIntent().getSerializableExtra("total_amount"));

        txtDate = findViewById(R.id.txt_date);
        txtTotalAmt = findViewById(R.id.txt_amt);
        txtName = findViewById(R.id.txt_Name);
        txtHQ = findViewById(R.id.txt_hq);
        txtDesig = findViewById(R.id.txt_desg);
        txtDept = findViewById(R.id.txt_dep);
        txtTrvlMode = findViewById(R.id.txt_mde);
        linAccept = findViewById(R.id.lin_accp);
        linReject = findViewById(R.id.rejectonly);
        txtReject = findViewById(R.id.L_rejectsave);
        txtTaAmt = findViewById(R.id.txt_tvrl_amt);
        txtEmpId = findViewById(R.id.txt_emp_id);
        txtMobile = findViewById(R.id.txt_moble);

        txtReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendtpApproval(2);
            }
        });
        txtDate.setText(String.valueOf(getIntent().getSerializableExtra("date")));

        txtName.setText(String.valueOf(getIntent().getSerializableExtra("name")));
        txtHQ.setText(String.valueOf(getIntent().getSerializableExtra("head_quaters")));
        txtDesig.setText(String.valueOf(getIntent().getSerializableExtra("desig")));
        txtDept.setText(String.valueOf(getIntent().getSerializableExtra("dept")));
        txtTrvlMode.setText(String.valueOf(getIntent().getSerializableExtra("travel_mode")));

        txtEmpId.setText(String.valueOf(getIntent().getSerializableExtra("sf_emp_id")));
        txtMobile.setText(String.valueOf(getIntent().getSerializableExtra("SF_Mobile")));


        vwldgBillAmt = findViewById(R.id.vwldgBillAmt);

        mChckCont.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    linCheckOut.setVisibility(View.INVISIBLE);
                    vwldgBillAmt.setVisibility(View.GONE);
                    cnSty = 1;
                    ldg_cout.setText("");
                    SumOFLodging(1);
                } else {
                    SumOFLodging(0);
                    cnSty = 0;
                    vwldgBillAmt.setVisibility(View.VISIBLE);
                    linCheckOut.setVisibility(View.VISIBLE);

                }
            }
        });

        mChckEarly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    linEarly.setVisibility(View.VISIBLE);
                    erlSty = 1;
                } else {
                    erlSty = 0;
                    earCheckIn.setText("");
                    earCheckOut.setText("");
                    linEarly.setVisibility(View.GONE);
                }
            }
        });

        mChckLate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lteSty = 1;
                    linLate.setVisibility(View.VISIBLE);
                } else {
                    lteSty = 0;
                    latCheckIn.setText("");
                    latCheckOut.setText("");
                    linLate.setVisibility(View.GONE);
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
                    /* ldgAdd.setText("- Remove");*/
                    lodgContvw.setVisibility(View.VISIBLE);
                } else {
                    /*              ldgAdd.setText("+ Add");*/
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
                ShowTimePicker("check");
            }
        });

        ldg_cin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowTimePicker("Lod_Check_In");
            }
        });

        ldg_cout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowTimePicker("Lod_Check_Out");
            }
        });


        earCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowTimePicker("Ear_Check_In");
            }
        });
        earCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowTimePicker("Ear_Check_Out");
            }
        });
        latCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowTimePicker("Lat_Check_In");
            }
        });
        latCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowTimePicker("Lat_Check_Out");
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
        edtEarBill.addTextChangedListener(new TextWatcher() {
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

        edtEarBill.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                SumOFLodging(0);
            }
        });

        edtLateBill.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                SumOFLodging(0);
            }
        });

        strRetriveType = String.valueOf(getIntent().getSerializableExtra("Retrive_Type"));
        if (strRetriveType.equals("Daily Allowance")) {
            jsonDailyAllowance = (JSONObject) getIntent().getSerializableExtra("Retrive_Ta_List");
        }


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
                edtOE.setEnabled(false);
                oeDelete = (ImageView) (views.findViewById(R.id.oe_delete));
                oeDelete.setVisibility(View.GONE);
                oeAttach = (ImageView) (views.findViewById(R.id.oe_attach_img));
                oePreview = (ImageView) (views.findViewById(R.id.img_prvw_oe));
                oeTxtUKey = (TextView) (views.findViewById(R.id.txt_oe_ukey));
                OtherExpense = (LinearLayout) views.findViewById(R.id.lin_other_expense_dynamic);
                linOtherSpinner = (LinearLayout) (views.findViewById(R.id.lin_othr_spiner));
                linOtherSpinner.setEnabled(false);
                oeAttach.setEnabled(false);
                oeAttach.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        CameraPermission cameraPermission = new CameraPermission(TAViewStatus.this, getApplicationContext());

                        if (!cameraPermission.checkPermission()) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                                cameraPermission.requestPermission();
                            }
                            Log.v("PERMISSION_NOT", "PERMISSION_NOT");
                        } else {
                            Log.v("PERMISSION", "PERMISSION");
                            //popupCapture(99);
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
                        //  OtherExpenseMode(valuedf);

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
                linOtherSpinner.setEnabled(false);
                lcAttach = (ImageView) (LcchildView.findViewById(R.id.la_attach_iamg));
                lcDelet = (ImageView) (LcchildView.findViewById(R.id.delete_lc));
                lcDelet.setVisibility(View.GONE);
                lcPreview = (ImageView) (LcchildView.findViewById(R.id.img_prvw_lc));
                lcTxtUKey = (TextView) (LcchildView.findViewById(R.id.txt_lc_ukey));
                editLaFare = (EditText) (LcchildView.findViewById(R.id.edt_la_fare));
                editLaFare.setEnabled(false);
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
                        //dynamicModeType(lcPosCntS);
                    }
                });

                lcAttach.setEnabled(false);
                lcAttach.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        CameraPermission cameraPermission = new CameraPermission(TAViewStatus.this, getApplicationContext());

                        if (!cameraPermission.checkPermission()) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                                cameraPermission.requestPermission();
                            }
                            Log.v("PERMISSION_NOT", "PERMISSION_NOT");
                        } else {
                            Log.v("PERMISSION", "PERMISSION");

                            //popupCapture(786);

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
                    deleteButton.setVisibility(View.GONE);
                    tvSize = travelDynamicLoaction.indexOfChild(rowView);
                    View tvchildView = travelDynamicLoaction.getChildAt(tvSize);
                    viw.setVisibility(View.VISIBLE);
                    lin.setVisibility(View.VISIBLE);

                    editText = (TextView) (tvchildView.findViewById(R.id.enter_mode));
                    enterFare = (EditText) tvchildView.findViewById(R.id.enter_fare);
                    taAttach = (ImageView) (tvchildView.findViewById(R.id.image_attach));
                    previewss = (ImageView) (tvchildView.findViewById(R.id.image_preview));
                    tvTxtUKey = (TextView) (tvchildView.findViewById(R.id.txt_tv_ukey));
                    enterFare.setEnabled(false);
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
                            enterFare.setEnabled(false);
                            DateTime = DateTime.replaceAll("^[\"']+|[\"']+$", "");

                            Intent stat = new Intent(getApplicationContext(), AttachementActivity.class);
                            stat.putExtra("position", TlUKey);
                            stat.putExtra("headTravel", "TL");
                            stat.putExtra("mode", editMode);
                            stat.putExtra("date", DateTime);
                            startActivity(stat);


                        }
                    });

                    taAttach.setEnabled(false);
                    taAttach.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            CameraPermission cameraPermission = new CameraPermission(TAViewStatus.this, getApplicationContext());

                            if (!cameraPermission.checkPermission()) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    cameraPermission.requestPermission();
                                }
                                Log.v("PERMISSION_NOT", "PERMISSION_NOT");
                            } else {
                                Log.v("PERMISSION", "PERMISSION");
                                //popupCapture(123);

                                Integer tvSizes = travelDynamicLoaction.indexOfChild(rowView);
                                View view = travelDynamicLoaction.getChildAt(tvSizes);
                                editText = (TextView) (view.findViewById(R.id.enter_mode));
                                enterFare = (EditText) view.findViewById(R.id.enter_fare);
                                tvTxtUKeys = (TextView) (view.findViewById(R.id.txt_tv_ukey));
                                editMode = editText.getText().toString();
                                enterFare.setEnabled(false);
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
                        deleteButton.setVisibility(View.GONE);
                        etrTaFr = (EditText) views.findViewById(R.id.ta_edt_from);
                        etrTaTo = (EditText) views.findViewById(R.id.ta_edt_to);
                        etrTaFr.setEnabled(false);
                        etrTaTo.setEnabled(false);

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


        card_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog = new CustomListViewDialog(TAViewStatus.this, modelRetailDetails, 10);
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


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            shouldShowRequestPermissionRationale("true");
        }
    }

    public void LateImage(View v) {
        CameraPermission cameraPermission = new CameraPermission(TAViewStatus.this, getApplicationContext());

        if (!cameraPermission.checkPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                cameraPermission.requestPermission();
            }
            Log.v("PERMISSION_NOT", "PERMISSION_NOT");
        } else {
            Log.v("PERMISSION", "PERMISSION");
            //popupCapture(343);
            DateFormat dfw = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Calendar calobjw = Calendar.getInstance();
            lodgLate = keyEk + mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code) + dfw.format(calobjw.getTime()).hashCode();

        }
    }

    public void LateAttach(View v) {
        DateTime = DateTime.replaceAll("^[\"']+|[\"']+$", "");
        Intent stat = new Intent(getApplicationContext(), AttachementActivity.class);
        stat.putExtra("position", lodgLate);
        stat.putExtra("headTravel", "LOD");
        stat.putExtra("mode", "LateMode");
        stat.putExtra("date", DateTime);
        startActivity(stat);
    }


    public void EarImage(View v) {
        CameraPermission cameraPermission = new CameraPermission(TAViewStatus.this, getApplicationContext());

        if (!cameraPermission.checkPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                cameraPermission.requestPermission();
            }
            Log.v("PERMISSION_NOT", "PERMISSION_NOT");
        } else {
            Log.v("PERMISSION", "PERMISSION");
            //popupCapture(405);
            DateFormat dfw = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Calendar calobjw = Calendar.getInstance();
            lodgEarly = keyEk + mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code) + dfw.format(calobjw.getTime()).hashCode();

        }
    }

    public void EarAttach(View v) {
        DateTime = DateTime.replaceAll("^[\"']+|[\"']+$", "");
        Intent stat = new Intent(getApplicationContext(), AttachementActivity.class);
        stat.putExtra("position", lodgEarly);
        stat.putExtra("headTravel", "LOD");
        stat.putExtra("mode", "EarlyMode");
        stat.putExtra("date", DateTime);
        startActivity(stat);
    }

    private void calOverAllTotal(Double localCov, Double otherExp, double tTotAmt) {
        Log.v("tTotAmt", String.valueOf(tTotAmt));


        String strldgTotal = lbl_ldg_eligi.getText().toString().substring(lbl_ldg_eligi.getText().toString().indexOf(".") + 1).trim();
        String separators = ".";
        int intldgTotal = strldgTotal.lastIndexOf(separators);
        Log.v("tTotAmt_intldg", String.valueOf(intldgTotal));
        Log.v("tTotAmt_double", String.valueOf(Double.valueOf(strldgTotal.substring(0, intldgTotal))));

        if (tTotAmt == 0) {
            gTotal = localCov + myBrdAmt + drvBrdAmt + otherExp + GrandTotalAllowance + 0.0;

        } else {

            if (LodingCon.size() != 0) {
                mChckEarly.setVisibility(View.GONE);
                Integer ValueTotal = LodingCon.size() + 1;
                Double TotalVal = ValueTotal * Double.valueOf(strldgTotal.substring(0, intldgTotal));
                gTotal = localCov + myBrdAmt + drvBrdAmt + otherExp + GrandTotalAllowance + TotalVal;
                Log.v("LOADIGN_Value" +
                        "", String.valueOf(ValueTotal));
                Log.v("LOADIGN_Value", String.valueOf(TotalVal));
                Log.v("LOADIGN_Total", String.valueOf(gTotal));

            } else {
                gTotal = localCov + myBrdAmt + drvBrdAmt + otherExp + GrandTotalAllowance + Double.valueOf(strldgTotal.substring(0, intldgTotal));
                Log.v("LOADIGN_DAYS", String.valueOf(gTotal));
            }

            Log.v("LOADIGN_DAYS", String.valueOf(LodingCon.size()));


        }
        double gTT = Double.parseDouble(GrandTotalAMT);
        grandTotal.setText("Rs." + new DecimalFormat("##0.00").format(gTT));

    }

    public void ShowTimePicker(String str) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(TAViewStatus.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {


                String hour = String.format("%02d", (selectedHour));
                String min = String.format("%02d", (selectedMinute));

                Log.v("TIME_SELECTED", String.valueOf(selectedHour));
                Log.v("TIME_SELECTED", hour);

                if (str.equalsIgnoreCase("Lod_Check_In")) {
                    ldg_cin.setText(hour + ":" + min);
                } else if (str.equalsIgnoreCase("Lod_Check_Out")) {
                    ldg_cout.setText(hour + ":" + min);
                } else if (str.equalsIgnoreCase("Ear_Check_In")) {
                    earCheckIn.setText(hour + ":" + min);
                } else if (str.equalsIgnoreCase("Ear_Check_Out")) {
                    earCheckOut.setText(hour + ":" + min);
                } else if (str.equalsIgnoreCase("Lat_Check_In")) {
                    latCheckIn.setText(hour + ":" + min);
                } else if (str.equalsIgnoreCase("Lat_Check_Out")) {
                    latCheckOut.setText(hour + ":" + min);
                }

                if (!ldg_cin.getText().toString().equalsIgnoreCase("") && !ldg_cout.getText().toString().equalsIgnoreCase("")) {
                    difference("3");
                }

            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }


    public void viewStaus(View v) {
        startActivity(new Intent(getApplicationContext(), ViewTAStatus.class));
    }

    public void ImagePdf(View v) {
        pdfViewList();
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
            deleteLod.setVisibility(View.GONE);
            if (i == 0) {
                deleteLod.setVisibility(View.GONE);
            } else {
                deleteLod.setVisibility(View.VISIBLE);
            }
        }
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
                    Log.v("Allowance_Amount", String.valueOf(sum));
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
            editLaFare.setEnabled(false);
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
            edtOE.setEnabled(false);
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
        SumOFLodging(0);
    }

    public void SumOFLodging(Integer count) {

        String sMyAmt = txtMyEligi.getText().toString().replaceAll("Rs.", "");
        String sJnAmt = txtJNEligi.getText().toString().replaceAll("Rs.", "");
        String sDrivAmt = txtDrivEligi.getText().toString().replaceAll("Rs.", "");
        String sErlyAmt = edtEarBill.getText().toString().replaceAll("Rs.", "");
        String sLateAmt = edtLateBill.getText().toString().replaceAll("Rs.", "");

        if (sErlyAmt.equalsIgnoreCase("")) sErlyAmt = "0";
        if (sLateAmt.equalsIgnoreCase("")) sLateAmt = "0";
        //  double tTotAmt = ldgEliAmt + ldgDrvEligi + Float.parseFloat(sJnAmt);
        tTotAmt = Double.parseDouble(sMyAmt) + ldgDrvEligi + Float.parseFloat(sJnAmt) + Double.parseDouble(sErlyAmt) + Double.parseDouble(sLateAmt);
        totLodgAmt = String.valueOf(tTotAmt);
        lbl_ldg_eligi.setText("Rs." + new DecimalFormat("##0.00").format(tTotAmt));
        SumWOBLodging();

        int IntValue = (int) tTotAmt;
        //tTotAmt = Double.parseDouble(sMyAmt) + ldgDrvEligi + Float.parseFloat(sJnAmt)+Double.parseDouble(sErlyAmt) +Double.parseDouble(sLateAmt) ;
        Log.v("TOTAL_AMOUNT", String.valueOf(IntValue));
        edt_ldg_bill.setFilters(new InputFilter[]{new Common_Class.InputFilterMinMax(0, IntValue - 1)});
        edtEarBill.setFilters(new InputFilter[]{new Common_Class.InputFilterMinMax(0, IntValue - 1)});

        Log.v("COunt_stay", String.valueOf(count));
        if (count == 1) {
            tTotAmt = 0;
            calOverAllTotal(localCov, otherExp, tTotAmt);
        } else {
            calOverAllTotal(localCov, otherExp, tTotAmt);
        }


    }

    public void SumWOBLodging() {
        String sMyAmt = txtMyEligi.getText().toString().replaceAll("Rs.", "");
        String sJnAmt = txtJNEligi.getText().toString().replaceAll("Rs.", "");
        String sldgAmt = txtDrivEligi.getText().toString().replaceAll("Rs.", "");

        tTotAmt = Double.parseDouble(sMyAmt) + ldgDrvEligi + Float.parseFloat(sJnAmt);

        String sBillAmt = edt_ldg_bill.getText().toString().replaceAll("Rs.", "");
        String sEarBillAmt = edtEarBill.getText().toString().replaceAll("Rs.", "");

        if (sEarBillAmt.isEmpty()) sEarBillAmt = "0";

        double tBalAmts = tTotAmt - Float.parseFloat(sEarBillAmt);
        witOutBill = String.valueOf(tBalAmts);

        Log.v("TOTAL_LODG_AMT", sldgAmt);
        Log.v("TOTAL_LODG_BILL", sBillAmt);
        Log.v("TOTAL_LODG_TOTAL", String.valueOf(tBalAmts));

        if (tBalAmts > 0) {
            ldgWOBBal.setText("Rs." + new DecimalFormat("##0.00").format(tBalAmts));
        }

        if (sBillAmt.isEmpty()) sBillAmt = "0";
        double tBalAmt = tTotAmt - Float.parseFloat(sBillAmt);
        witOutBill = String.valueOf(tBalAmt);
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

        ObjectAnimator textColorAnim;
        textColorAnim = ObjectAnimator.ofInt(txtErt, "textColor", Color.WHITE, Color.TRANSPARENT);
        textColorAnim.setDuration(500);
        textColorAnim.setEvaluator(new ArgbEvaluator());
        textColorAnim.setRepeatCount(ValueAnimator.INFINITE);
        textColorAnim.setRepeatMode(ValueAnimator.REVERSE);
        textColorAnim.start();

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


        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHome();

            }
        });

        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
            Intent Dashboard = new Intent(TAViewStatus.this, Dashboard_Two.class);
            Dashboard.putExtra("Mode", "CIN");
            startActivity(Dashboard);
        } else
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
    }


    /*Display Mode of travel View based on the choosed Date*/
    public void displayTravelMode() {


        try {
            JSONObject jj = new JSONObject();
            try {
                jj.put("sfCode", Shared_Common_Pref.Sf_Code);
                jj.put("divisionCode", Shared_Common_Pref.Div_Code);
                jj.put("Selectdate", Ta_DATE);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.v("Json_date_fomrat", jj.toString());
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonObject> call = apiInterface.getTAdateDetails(jj.toString());

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                    JsonObject jsonObjects = response.body();

                    Log.v("JSON_TRAVEL_DETAILS", jsonObjects.toString());


                    jsonArray = jsonObjects.getAsJsonArray("TodayStart_Details");
                    lcDraftArray = jsonObjects.getAsJsonArray("Additional_ExpenseLC");
                    oeDraftArray = jsonObjects.getAsJsonArray("Additional_ExpenseOE");
                    trvldArray = jsonObjects.getAsJsonArray("Travelled_Loc");
                    ldArray = jsonObjects.getAsJsonArray("Lodging_Head");
                    travelDetails = jsonObjects.getAsJsonArray("Travelled_Details");
                    LodingCon = jsonObjects.getAsJsonArray("LodDtlist");
                    StayDate = jsonObjects.getAsJsonArray("Stay_Date_time");
                    jsonExpHead = jsonObjects.getAsJsonArray("Expense_Head");
                    jsonFuelAllowance = jsonObjects.getAsJsonArray("FuelAllowance");

                    if (jsonFuelAllowance != null || jsonFuelAllowance.size() != 0) {
                        Log.v("jsonFuelAllowance_IN", jsonFuelAllowance.toString());


                        fuelListAdapter = new FuelListAdapter(getApplicationContext(), jsonFuelAllowance);
                        mFuelRecycler.setAdapter(fuelListAdapter);
                        JsonObject jsFuel;
                        for (int jf = 0; jf < jsonFuelAllowance.size(); jf++) {
                            jsFuel = jsonFuelAllowance.get(jf).getAsJsonObject();

                            if (!jsFuel.get("End_Km").getAsString().equalsIgnoreCase("")) {
                                Integer start = Integer.valueOf(jsFuel.get("Start_Km").getAsString());
                                Integer end = Integer.valueOf(jsFuel.get("End_Km").getAsString());
                                String total = String.valueOf(end - start);
                                Integer Total = Integer.valueOf(total);
                                Integer Personal = Integer.valueOf(jsFuel.get("Personal_Km").getAsString());
                                String TotalPersonal = String.valueOf(Total - Personal);
                                Double q = Double.valueOf(TotalPersonal);
                                Double z = Double.valueOf(jsFuel.get("FuelAmt").getAsString());
                                String qz = String.valueOf(q * z);
                                Log.v("TA_FUEL_TOTAL", String.valueOf(qz));


                                fuelAmt = fuelAmt + (q * z);
                                fuelAmount.setText("Rs ." + fuelAmt);

                                TextTotalAmount.setText("Rs." + new DecimalFormat("##0.00").format(fuelAmt));
                            }
                        }
                    }
               /*     if (LodingCon.size() != 0) {
                        mChckEarly.setVisibility(View.GONE);
                    }*/

                    Log.v("JSON_LODGING", jsonExpHead.toString());

                    JsonObject jRremarks = null;

                    for (int i = 0; i < jsonExpHead.size(); i++) {
                        jRremarks = jsonExpHead.get(i).getAsJsonObject();
                        String strRemarks = jRremarks.get("Reason").getAsString();
                        editTextRemarks.setText(strRemarks);

                    }

                    JsonObject jsonObject = null;
                    for (int i = 0; i < jsonArray.size(); i++) {
                        int finalC = i;

                        Log.e("JsonArray", String.valueOf(jsonArray.size()));
                        //jsonObject = (JSONObject) jsonArray.get(i);

                        jsonObject = jsonArray.get(i).getAsJsonObject();
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
                            if (js != null) {
                                js = (JsonObject) travelDetails.get(l);
                                taAmt = js.get("ta_total_amount").getAsString();
                                txtTAamt.setText("Rs." + taAmt + ".00");
                            }
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


                        } else {


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
                                editText.setEnabled(false);
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
                                deleteButton.setVisibility(View.GONE);
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

                                        CameraPermission cameraPermission = new CameraPermission(TAViewStatus.this, getApplicationContext());

                                        if (!cameraPermission.checkPermission()) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                                cameraPermission.requestPermission();
                                            }
                                            Log.v("PERMISSION_NOT", "PERMISSION_NOT");
                                        } else {
                                            Log.v("PERMISSION", "PERMISSION");
                                            //popupCapture(123);

                                            Integer tvSizes = travelDynamicLoaction.indexOfChild(rowView);
                                            View view = travelDynamicLoaction.getChildAt(tvSizes);
                                            editText = (TextView) (view.findViewById(R.id.enter_mode));
                                            enterFare = (EditText) view.findViewById(R.id.enter_fare);
                                            tvTxtUKeys = (TextView) (view.findViewById(R.id.txt_tv_ukey));
                                            editMode = editText.getText().toString();
                                            enterFare.setEnabled(false);
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
                                        enterFare.setEnabled(false);
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
                                    editText.setEnabled(false);
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

                    Log.v("LODGING_ARRAY", String.valueOf(ldArray.size()));
                    if (ldArray != null || ldArray.size() != 0) {
                        Log.v("LODGING_ARRAY_IF", String.valueOf(ldArray.size()));
                        if (ldgAdd.getText().equals("+ Add")) {
                            /*ldgAdd.setText("- Remove");*/
                            lodgContvw.setVisibility(View.GONE);
                            lodingDraft(ldArray, LodingCon);
                            Log.v("TO_DA_GA_PA", txt_ldg_type.getText().toString());
                        } else {
/*
                            ldgAdd.setText("+ Add");
*/
                            lodgContvw.setVisibility(View.VISIBLE);
                            ldg_cin.setText("");
                            ldg_cout.setText("");
                            lodgStyLocation.setText("");
                            txtMyEligi.setText("Rs." + 0.00);
                            ldgWOBBal.setText("Rs." + 0.00);
                            lbl_ldg_eligi.setText("Rs." + 0.00);
                            edt_ldg_bill.setText("");
                            txt_ldg_type.setText("");
                            TotalDays.setVisibility(View.GONE);
                            earCheckIn.setText("");
                            earCheckOut.setText("");
                            latCheckIn.setText("");
                            latCheckOut.setText("");
                            edtEarBill.setText("");
                            edtLateBill.setText("");
                            mChckCont.setChecked(false);
                            mChckEarly.setChecked(false);
                            mChckLate.setChecked(false);
                        }

                    } else {
                        //lodgContvw.setVisibility(View.GONE);
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
                        lodgContvw.setVisibility(View.GONE);
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

    @SuppressLint("SetTextI18n")
    public void lodingDraft(JsonArray lodingDraft, JsonArray ContSty) {

        JsonObject eachData;

        if (ContSty.size() != 0) {
            for (int i = 0; i < ContSty.size(); i++) {
                eachData = (JsonObject) ContSty.get(i);

                TextView customOptionsName = new TextView(TAViewStatus.this);
                customOptionsName.setPadding(0, 15, 0, 15);
                customOptionsName.setText("");
                customOptionsName.setText(eachData.get("fdt").getAsString());
                viewContinue.addView(customOptionsName);
                mChckEarly.setVisibility(View.VISIBLE);
                linearConView.setVisibility(View.VISIBLE);
            }
        } else {
            mChckEarly.setVisibility(View.GONE);
            linearConView.setVisibility(View.GONE);
        }

        Log.v("Lodging_Details", ContSty.toString());
        JsonArray jsonAddition = null;

        JsonObject ldraft = null;




            for (int i = 0; i < lodingDraft.size(); i++) {
                ldraft = (JsonObject) lodingDraft.get(i);

                jsonAddition = ldraft.getAsJsonArray("Additional");
/*
            ldgAdd.setText("- Remove");
*/
                lodgContvw.setVisibility(View.VISIBLE);
                lodgCont.setVisibility(View.VISIBLE);
                ldg_stayloc.setVisibility(View.VISIBLE);
                ldg_stayDt.setVisibility(View.VISIBLE);
                lodgJoin.setVisibility(View.VISIBLE);
                linContinueStay.setVisibility(View.VISIBLE);

                if (ldraft != null || ldraft.size() != 0) {
                ldg_cin.setText(""+ldraft.get("Stay_Date").getAsString());
                ldg_cout.setText(""+ldraft.get("To_Date").getAsString());
                lodgStyLocation.setText(""+ldraft.get("Ldg_Stay_Loc").getAsString());




                txtDrivEligi.setText(""+ldraft.get("Driver_Ldg_Amount").getAsString());

                ConStay =""+ ldraft.get("Continuous_Stay").toString();
                ErlyStay =""+ ldraft.get("Early_Checkin").toString();
                LteStay =""+ ldraft.get("Late_Checkout").toString();

                ErlyChecIn = ""+ ldraft.get("Erly_Check_in").toString();
                ErlyChecOut =""+ ldraft.get("Erly_Check_out").toString();
                ErlyAmt = ""+ldraft.get("Ear_bill_amt").toString();

                LteChecIn =""+ ldraft.get("lat_chec_in").toString();
                LteChecOut =""+ ldraft.get("lat_check_out").toString();
                LteAmt =""+ ldraft.get("lat_bill_amt").toString();


                earCheckIn.setText(ErlyChecIn);
                earCheckOut.setText(ErlyChecOut);
                latCheckIn.setText(LteChecIn);
                latCheckOut.setText(LteChecOut);
                edtEarBill.setText(ErlyAmt);
                edtLateBill.setText(LteAmt);

                if (ConStay.equalsIgnoreCase("1")) mChckCont.setChecked(true);
                if (ErlyStay.equalsIgnoreCase("1")) mChckEarly.setChecked(true);
                if (LteStay.equalsIgnoreCase("1")) mChckLate.setChecked(true);


                if (mChckCont.isChecked()) {
                    linCheckOut.setVisibility(View.INVISIBLE);
                    vwldgBillAmt.setVisibility(View.GONE);
                    cnSty = 1;
                    ldg_cout.setText("");
                    SumOFLodging(1);
                } else {
                    SumOFLodging(0);
                    cnSty = 0;
                    vwldgBillAmt.setVisibility(View.VISIBLE);
                    linCheckOut.setVisibility(View.VISIBLE);

                }


                stayDays.setVisibility(View.VISIBLE);
                Double totlLdgAmt = Double.valueOf(""+ldraft.get("Total_Ldg_Amount").getAsString());
                Integer noday = Integer.valueOf(""+ldraft.get("NO_Of_Days").getAsString());

                txtLodgUKey.setText(""+ldraft.get("Ukey").getAsString());
                double elibs = Integer.valueOf(ldraft.get("Eligible").getAsString());
                txtMyEligi.setText("Rs." + new DecimalFormat("##0.00").format(elibs));

                double srtjdgAmt = Integer.valueOf(""+ldraft.get("Joining_Ldg_Amount").getAsString());
                txtJNEligi.setText("Rs." + new DecimalFormat("##0.00").format(srtjdgAmt));
                Double wobal = Double.valueOf(""+ldraft.get("WOB_Amt").getAsString());

                Log.v("ldgWOBBal", String.valueOf(wobal));
                ldgWOBBal.setText("Rs." + new DecimalFormat("##0.00").format(wobal));
                Log.v("ldgWOBBal_______", ldgWOBBal.getText().toString());

                edt_ldg_bill.setText(""+ldraft.get("Bill_Amt").getAsString());
                lbl_ldg_eligi.setText("Rs." + new DecimalFormat("##0.00").format(totlLdgAmt));
                txtStyDays.setText(""+ldraft.get("NO_Of_Days").getAsString());
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

                if (jsonAddition != null || jsonAddition.size() != 0) {
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
                        edt_ldg_JnEmp.setEnabled(false);
                        txtJNName = (TextView) jdV.findViewById(R.id.txtJNName);
                        txtJNDesig = (TextView) jdV.findViewById(R.id.txtJNDesig);
                        txtJNDept = (TextView) jdV.findViewById(R.id.txtJNDept);
                        txtJNHQ = (TextView) jdV.findViewById(R.id.txtJNHQ);
                        txtJNMob = (TextView) jdV.findViewById(R.id.txtJNMob);
                        txtJNMyEli = (TextView) jdV.findViewById(R.id.txtJNMyEli);
                        ImageView delte = jdV.findViewById(R.id.ldg_delete);
                        delte.setVisibility(View.GONE);

                        edt_ldg_JnEmp.setText(""+jsonObjectAdd.get("Emp_Code").getAsString());
                        txtJNName.setText(""+jsonObjectAdd.get("Sf_Name").getAsString());
                        txtJNDesig.setText(""+jsonObjectAdd.get("Desig").getAsString());
                        txtJNDept.setText(""+jsonObjectAdd.get("Dept").getAsString());
                        txtJNHQ.setText(""+jsonObjectAdd.get("Sf_Hq").getAsString());
                        txtJNMob.setText(""+jsonObjectAdd.get("Sf_Mobile").getAsString());
                        /* double jntEli = Integer.valueOf(ldraft.get("Ldg_Amount").getAsString());*//*
                       txtJNMyEli.setText("Rs." + "0.00");*/

                        float sum = jsonObjectAdd.get("Ldg_Amount").getAsFloat();
                        txtJNMyEli.setText("Rs." + new DecimalFormat("##0.00").format(sum));

                    }
                }
            }
        }
    }

    public void localConDraft(JsonArray lcDraft) {

        JsonArray jsonAddition = null;
        JsonObject lcdraftJson;


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
                editLaFare.setEnabled(false);
                linLocalSpinner = (LinearLayout) LcchildView.findViewById(R.id.lin_loc_spiner);
                lcAttach = (ImageView) (LcchildView.findViewById(R.id.la_attach_iamg));
                lcDelet = (ImageView) (LcchildView.findViewById(R.id.delete_lc));
                lcDelet.setVisibility(View.GONE);
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
                        // dynamicModeType(lcPosCntS);
                    }
                });

                lcAttach.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CameraPermission cameraPermission = new CameraPermission(TAViewStatus.this, getApplicationContext());
                        if (!cameraPermission.checkPermission()) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                                cameraPermission.requestPermission();
                            }
                            Log.v("PERMISSION_NOT", "PERMISSION_NOT");
                        } else {
                            Log.v("PERMISSION", "PERMISSION");

                            //popupCapture(786);

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
            edtOE.setEnabled(false);
            oeAttach = (ImageView) (childView.findViewById(R.id.oe_attach_img));
            oeDelete = (ImageView) (childView.findViewById(R.id.oe_delete));
            oeDelete.setVisibility(View.GONE);
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


                    CameraPermission cameraPermission = new CameraPermission(TAViewStatus.this, getApplicationContext());
                    if (!cameraPermission.checkPermission()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            cameraPermission.requestPermission();
                        }
                        Log.v("PERMISSION_NOT", "PERMISSION_NOT");
                    } else {
                        Log.v("PERMISSION", "PERMISSION");
                        //popupCapture(99);

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
                    //   OtherExpenseMode(valuedf);

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
                    deleteButton.setVisibility(View.GONE);
                    etrTaFr = (EditText) views.findViewById(R.id.ta_edt_from);
                    etrTaTo = (EditText) views.findViewById(R.id.ta_edt_to);
                    etrTaFr.setEnabled(false);
                    etrTaTo.setEnabled(false);
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
            edt1.setEnabled(false);
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
                            fullPath = filepath.getPath(TAViewStatus.this, mClipData.getItemAt(i).getUri());
                            lodgArrLst.add(fullPath);


                            getMulipart(lodUKey, fullPath, "LOD", "", "Room", "", "");

                        }
                    } else if (data.getData() != null) {
                        Uri item = data.getData();
                        ImageFilePath filepath = new ImageFilePath();
                        fullPath = filepath.getPath(TAViewStatus.this, item);
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
                        fullPath = filepath.getPath(TAViewStatus.this, mClipData.getItemAt(i).getUri());

                        getMulipart(TlUKey, fullPath, "TL", "", editMode, "", "");

                    }
                } else if (data.getData() != null) {
                    Uri item = data.getData();
                    ImageFilePath filepath = new ImageFilePath();
                    fullPath = filepath.getPath(TAViewStatus.this, item);


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
                            fullPath = filepath.getPath(TAViewStatus.this, mClipData.getItemAt(i).getUri());

                            getMulipart(OeUKey, fullPath, "OE", "", editMode, "", "");


                        }
                    } else if (data.getData() != null) {

                        Uri item = data.getData();
                        ImageFilePath filepath = new ImageFilePath();
                        fullPath = filepath.getPath(TAViewStatus.this, item);
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
                            fullPath = filepath.getPath(TAViewStatus.this, mClipData.getItemAt(i).getUri());

                            getMulipart(LcUKey, fullPath, "LC", "", editMode, "", "");

                        }
                    } else if (data.getData() != null) {

                        Uri item = data.getData();
                        ImageFilePath filepath = new ImageFilePath();
                        fullPath = filepath.getPath(TAViewStatus.this, item);

                        getMulipart(LcUKey, fullPath, "LC", "", editMode, "", "");

                    }
                }
            }
        } else if (requestCode == 344) {
            if (resultCode == RESULT_OK) {
                if (requestCode == 344) {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {
                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            // display your images
                            ImageFilePath filepath = new ImageFilePath();
                            fullPath = filepath.getPath(TAViewStatus.this, mClipData.getItemAt(i).getUri());

                            getMulipart(lodgLate, fullPath, "LOD", "", "LateMode", "", "");

                        }
                    } else if (data.getData() != null) {

                        Uri item = data.getData();
                        ImageFilePath filepath = new ImageFilePath();
                        fullPath = filepath.getPath(TAViewStatus.this, item);

                        getMulipart(lodgLate, fullPath, "LOD", "", "LateMode", "", "");

                    }
                }
            }
        } else if (requestCode == 406) {
            if (resultCode == RESULT_OK) {
                if (requestCode == 406) {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {
                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            // display your images
                            ImageFilePath filepath = new ImageFilePath();
                            fullPath = filepath.getPath(TAViewStatus.this, mClipData.getItemAt(i).getUri());

                            getMulipart(lodgEarly, fullPath, "LOD", "", "EarlyMode", "", "");

                        }
                    } else if (data.getData() != null) {

                        Uri item = data.getData();
                        ImageFilePath filepath = new ImageFilePath();
                        fullPath = filepath.getPath(TAViewStatus.this, item);

                        getMulipart(lodgEarly, fullPath, "LOD", "", "EarlyMode", "", "");

                    }
                }
            }
        } else if (requestCode == 143 && resultCode == Activity.RESULT_OK) {

            finalPath = "/storage/emulated/0";
            filePath = outputFileUri.getPath();
            filePath = filePath.substring(1);
            filePath = finalPath + filePath.substring(filePath.indexOf("/"));

            getMulipart(lodUKey, filePath, "LOD", "", "Room", "", "");

        } else if (requestCode == 343 && resultCode == Activity.RESULT_OK) {

            finalPath = "/storage/emulated/0";
            filePath = outputFileUri.getPath();
            filePath = filePath.substring(1);
            filePath = finalPath + filePath.substring(filePath.indexOf("/"));

            getMulipart(lodgLate, filePath, "LOD", "", "LateMode", "", "");

        } else if (requestCode == 405 && resultCode == Activity.RESULT_OK) {

            finalPath = "/storage/emulated/0";
            filePath = outputFileUri.getPath();
            filePath = filePath.substring(1);
            filePath = finalPath + filePath.substring(filePath.indexOf("/"));

            getMulipart(lodgEarly, filePath, "LOD", "", "EarlyMode", "", "");

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
        dialog = new Dialog(TAViewStatus.this, R.style.AlertDialogCustom);
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
                stat.putExtra("PDF_FILE", "Web");
                startActivity(stat);

                dialog.dismiss();

            }
        });
        pdf2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent stat = new Intent(getApplicationContext(), PdfViewerActivity.class);
                stat.putExtra("PDF_ONE", "https://hap.sanfmcg.com/HAP_ANNEXURE_C.pdf");
                stat.putExtra("PDF_FILE", "Web");
                startActivity(stat);

                dialog.dismiss();
            }
        });
    }

    public void popupCapture(Integer attachName) {
        dialog = new Dialog(TAViewStatus.this, R.style.AlertDialogCustom);
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
        outputFileUri = FileProvider.getUriForFile(TAViewStatus.this, getApplicationContext().getPackageName() + ".provider", new File(getExternalCacheDir().getPath(), Shared_Common_Pref.Sf_Code + "_" + System.currentTimeMillis() + ".jpeg"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, positionC);

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

                customDialog = new CustomListViewDialog(TAViewStatus.this, modelTravelType, 8);
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
        mCommon_model_spinner = new Common_Model("Stay At Relative's House", "RS");
        ldgModes.add(mCommon_model_spinner);

        customDialog = new CustomListViewDialog(TAViewStatus.this, ldgModes, 9);
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
        customDialog = new CustomListViewDialog(TAViewStatus.this, listOrderType, 80);
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
        customDialog = new CustomListViewDialog(TAViewStatus.this, OtherExpenseList, 90);
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
                                        edt.setEnabled(false);
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
                                        edt.setEnabled(false);
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
                    finish();
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

    public void difference(String str) {

        stayEgTotal = 1 * ldgEliAmt;

        Log.v("LODING_STY", String.valueOf(stayEgTotal));
        Log.v("LODING_ELIAMt", String.valueOf(ldgEliAmt));

        txtMyEligi.setText("Rs." + new DecimalFormat("##0.00").format(stayEgTotal));
        ldgWOBBal.setText("Rs." + new DecimalFormat("##0.00").format(stayEgTotal));

        SumOFLodging(0);

    }

    public void scroll() {

        mLc.post(new Runnable() {
            @Override
            public void run() {

                mLc.fullScroll(View.FOCUS_DOWN);

            }
        });
    }


    public void onApproval(View v) {
        new LocationFinder(getApplication(), new LocationEvents() {
            @Override
            public void OnLocationRecived(Location location) {
                SendtpApproval(1);
            }
        });
    }

    public void onReject(View v) {
        linAccept.setVisibility(View.GONE);
        linReject.setVisibility(View.VISIBLE);
    }


    private void SendtpApproval(int flag) {
        JSONObject taReq = new JSONObject();

        try {
            taReq.put("sfCode", sfCode);
            taReq.put("Flag", flag);
            taReq.put("Sl_No", SlStart);
            taReq.put("AAmount", GrandTotalAMT);
            taReq.put("Reason", appCompatEditText.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.v("TA_REQ", taReq.toString());
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> mCall = apiInterface.taApprove(taReq.toString());

        mCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // locationList=response.body();
                Log.e("TAG_TP_RESPONSE", "response Tp_View: " + new Gson().toJson(response.body()));
                try {
                    finish();
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (flag == 1) {
                        Toast.makeText(getApplicationContext(), "TA  Approved Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "TA Rejected  Successfully", Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
}
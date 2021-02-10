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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Activity.Util.ImageFilePath;
import com.hap.checkinproc.Activity.Util.SelectionModel;
import com.hap.checkinproc.Activity.Util.UpdateUi;
import com.hap.checkinproc.Activity_Hap.AttachementActivity;
import com.hap.checkinproc.Activity_Hap.CustomListViewDialog;
import com.hap.checkinproc.Activity_Hap.Dashboard;
import com.hap.checkinproc.Activity_Hap.Dashboard_Two;
import com.hap.checkinproc.Activity_Hap.ERT;
import com.hap.checkinproc.Activity_Hap.Help_Activity;
import com.hap.checkinproc.Activity_Hap.MapZoomIn;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.Model_Class.ModeOfTravel;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.DailyExpenseAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
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

public class TAClaimActivity extends AppCompatActivity implements View.OnClickListener, Master_Interface,
        OnMapReadyCallback {

    CardView card_date, card_type_travel;
    TextView txt_date, txt_ldg_type;
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
    List<Common_Model> OtherExpenseList = new ArrayList<>();
    CustomListViewDialog customDialog;
    LinearLayout linAddAllowance;
    CardView TravelBike;
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

    ArrayList<LatLng> latLonArray = new ArrayList<>();
    Integer totalkm = 0, totalPersonalKm = 0;
    String exp_for = "";
    JSONObject json_o;
    String DateForAPi = "";
    GoogleMap mGoogleMap;
    EditText enterMode, enterFrom, enterTo, enterFare;
    TextView PersonalTextKM;
    Integer Pva, C = 0;
    TextView PersonalKiloMeter, txtDailyAllowance;
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
    String shortName = "", Exp_Name = "";

    ArrayList<String> DA = new ArrayList<>();
    ArrayList<String> OE = new ArrayList<>();
    ArrayList<String> LC = new ArrayList<>();
    List<String> listWithoutDuplicates;
    LinearLayout LDailyAllowance, LOtherExpense, LLocalConve;
    String Id = "", userEnter = "", attachment = "", maxAllowonce = "";

    String strRetriveType = "";
    ArrayList<String> strRetriveTaList = new ArrayList<>();
    LinearLayout dymicDailyAllowance;
    JSONObject jsonDailyAllowance = new JSONObject();
    String StrToEnd = "", StrBus = "", StrTo = "", StrDaName = "", StrDailyAllowance = "";
    TextView txtBusFrom, txtBusTo, txtTaClaim;
    LinearLayout linBusMode, linBikeMode, linMode;
    ArrayList<String> LatArrayList = new ArrayList<>();
    ArrayList<String> LonArrayList = new ArrayList<>();
    ArrayList<String> temaplateList;
    LinearLayout travelDynamicLoaction;
    CardView crdDynamicLocation;
    String STRCHECKING;
    TextView editText;
    int size = 0, lcSize = 0, OeSize = 0;
    LinearLayout linDailyAllowance;
    View childView;
    List<Common_Model> myDataset = new ArrayList<>();
    int position;
    int type;
    LinearLayout linlocalCon;
    SharedPreferences mSharedPreferences;
    String jsnLocalCon;
    Type userType;
    Common_Model Model_Pojo;
    List<Common_Model> modelTravelType = new ArrayList<>();
    List<ModeOfTravel> modelOfTravel;
    Gson gson;
    TextView fuelAmount, TextTotalAmount, editTexts, oeEditext;
    String strFuelAmount = "";
    View rowView;
    ImageView deleteButton;
    String attCon = "";
    LinearLayout LinearOtherAllowance;

    String ModeType = "", StrModeValue = "", dynamicLabel = "";
    String OEdynamicLabel = "";
    ArrayList<String> OEdynamicList, dynamicLabelList;

    ArrayList<String> cccccccc = new ArrayList<String>();
    CardView cardModeList;
    TextView txtmodeType, txtModeValue;

    LinearLayout Dynamicallowance, OtherExpense;
    EditText enterFareAmount, edt;
    Button DynamicSave;
    List<EditText> allEds = new ArrayList<EditText>();
    List<EditText> allEds1 = new ArrayList<EditText>();
    List<EditText> newEdt = new ArrayList<>();
    Integer editTextPositionss;
    EditText editLaFare, edtOE;
    Map<String, List<EditText>> usersByCountry = new HashMap<String, List<EditText>>();
    Map<String, List<EditText>> userOtherExpense = new HashMap<String, List<EditText>>();
    ImageView previewss;

    String editMode = "";
    Double tofuel = 0.0;
    LinearLayout localTotal, otherTotal;

    ImageView taAttach, lcAttach, oeAttach, lcPreview, oePreview;
    String allowanceAmt = "";
    TextView txtallamt;

    Double daTotal = 0.0, taTotal = 0.0, lcTotal = 0.0, oeTotal = 0.0;
    Double fAmount = 0.0;
    TextView grandTotal;
    Double doubleAmount = 0.0;
    TextView localText, OeText;
    Double GrandTotalAllowance = 0.0;
    Double TotLdging = 0.0;
    double sTotal = 0.0;
    double sums = 0.0;
    CardView ldg_ara;
    LinearLayout otherExpenseLayout, linAll, linRemarks, linFareAmount, ldg_typ_sp;
    //ArrayList<String> AttachmentImg = new ArrayList<>();
    String strGT = "";
    Double dcGT = 0.0;
    Map<String, String> AttachmentImg = new HashMap<String, String>();
    Double otherExp = 0.0, localCov = 0.0;
    LinearLayout linLocalSpinner, linOtherSpinner;
    Dialog dialog;
    SharedPreferences CheckInDetails;
    String eventListStr;
    LinearLayout LinearMap;
    ImageView endkmimage, startkmimage;
    Button buttonSave;
    ArrayList<String> lcString = new ArrayList<>();


    //Loadging Expense

    DatePickerDialog picker;
    Switch ldgNeeded;
    List<Common_Model> ldgModes = new ArrayList<>();
    LinearLayout lodgCont, lodgContvw, ldg_stayloc, ldg_stayDt, lodgJoin;
    LinearLayout ldgEAra, ldgMyEAra, JNLdgEAra, drvldgEAra;
    Button btn_empget;
    EditText edt_ldg_JnEmp, edt_ldg_bill;
    TextView ldg_cin, txtJNName, txtJNDesig, txtJNDept, txtJNHQ, txtJNMob, lblHdBill, lblHdBln, ldgWOBBal,
            ldgAdd, txtJNMyEli, txtMyEligi, txtDrivEligi, lbl_ldg_eligi;

    ImageView mapZoomIn;
    LinearLayout jointLodging;
    TextView txtJointAdd, txtJNEligi;
    String start_Image, End_Imge;
    EditText edtLcFare;
    String filePath = "";
    ImageView imgPrev;
    LinearLayout linTaImgPrv;

    String fullPath = "";
    ArrayList<ArrayList<String>> tlString = new ArrayList<ArrayList<String>>();
    Integer taCount = 0;
    ArrayList<String> zero = new ArrayList<>();
    ArrayList<String> nonZero = new ArrayList<>();
    Polyline polyline1;

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
        temaplateList = new ArrayList<>();
        gson = new Gson();
        txt_date = findViewById(R.id.txt_date);
        card_date = findViewById(R.id.card_date);
        btn_sub = findViewById(R.id.btn_sub);

        linAddAllowance = findViewById(R.id.lin_travel_loaction);
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
        otherTotal = findViewById(R.id.lin_total_other);

        txtallamt = findViewById(R.id.txt_mode_amount);
        txtBusFrom = findViewById(R.id.txt_bus_from);
        txtBusTo = findViewById(R.id.txt_bus_to);
        txtTaClaim = findViewById(R.id.mode_name);
        txtDailyAllowance = findViewById(R.id.txt_daily_allowance_mode);
        travelDynamicLoaction = findViewById(R.id.lin_travel_dynamic_location);
        crdDynamicLocation = findViewById(R.id.card_travel_loaction);
        linDailyAllowance = findViewById(R.id.lin_da_type);
        linlocalCon = findViewById(R.id.lin_dyn_local_con);
        fuelAmount = findViewById(R.id.fuel_amount);
        mSharedPreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        TextTotalAmount = findViewById(R.id.txt_total_amt);
        LinearOtherAllowance = findViewById(R.id.lin_dyn_other_Expense);
        dynamicLabelList = new ArrayList<>();
        OEdynamicList = new ArrayList<>();
        grandTotal = findViewById(R.id.grand_total);
        localText = findViewById(R.id.txt_local);
        OeText = findViewById(R.id.txt_oe);
        otherExpenseLayout = findViewById(R.id.lin_total_other);
        linAll = findViewById(R.id.lin_alo_view);
        linRemarks = findViewById(R.id.lin_remarks);
        linFareAmount = findViewById(R.id.lin_fare_amount);
        linTaImgPrv = findViewById(R.id.lin_ta_img_prv);

        //Loadging Expense
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
        /*btn_empget = findViewById(R.id.btn_empget);
        edt_ldg_JnEmp = findViewById(R.id.edt_ldg_JnEmp);*/
        ldg_cin = findViewById(R.id.ldg_cin);
       /* txtJNName = findViewById(R.id.txtJNName);
        txtJNDesig = findViewById(R.id.txtJNDesig);
        txtJNDept = findViewById(R.id.txtJNDept);
        txtJNHQ = findViewById(R.id.txtJNHQ);
        txtJNMob = findViewById(R.id.txtJNMob);*/
        txtMyEligi = findViewById(R.id.txtMyEligi);
        txtDrivEligi = findViewById(R.id.txtDrvLgd);
        lbl_ldg_eligi = findViewById(R.id.lbl_ldg_eligi);
        lblHdBill = findViewById(R.id.lblHdBill);
        lblHdBln = findViewById(R.id.lblHdBln);
        ldgWOBBal = findViewById(R.id.ldgWOBBal);
        edt_ldg_bill = findViewById(R.id.edt_ldg_bill);
        txtJNEligi = findViewById(R.id.txtJNEligi);
        ldgAdd = findViewById(R.id.ldg_Add);
        mapZoomIn = findViewById(R.id.map_zoom);
        buttonSave = findViewById(R.id.save_button);
        ldgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ldgAdd.getText().equals("+ Add")) {
                    ldgAdd.setText("- Remove");
                    lodgContvw.setVisibility(View.VISIBLE);
                } else {
                    ldgAdd.setText("+ Add");
                    lodgContvw.setVisibility(View.GONE);
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
            Log.e("AllowanceType", strRetriveType);
            Log.e("AllowanceType", jsonDailyAllowance.toString());

        } else {
            Log.e("AllowanceType", strRetriveType);

        }

        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });


        strGT = grandTotal.getText().toString();
        Log.e("strGT", strGT);
 /* if (!strGT.matches("")) {
 dcGT = Double.parseDouble(strGT);
 Log.e("strGT_log", String.valueOf(dcGT));
 }*/
        LDailyAllowance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        LOtherExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = null;


                otherTotal.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                layoutParams.setMargins(15, 15, 15, 15);

                rowView = inflater.inflate(R.layout.activity_other_expense, null);
                LinearOtherAllowance.addView(rowView, layoutParams);
                OeSize = LinearOtherAllowance.getChildCount();

                for (int c = 0; c < OeSize; c++) {
                    childView = LinearOtherAllowance.getChildAt(c);
                    otherExpenseLayout.setVisibility(View.VISIBLE);
                    oeEditext = (TextView) (childView.findViewById(R.id.other_enter_mode));
                    edtOE = (EditText) (childView.findViewById(R.id.oe_fre_amt));
                    oeAttach = (ImageView) (childView.findViewById(R.id.oe_attach_img));
                    oePreview = (ImageView) (childView.findViewById(R.id.img_prvw_oe));
                    linOtherSpinner = (LinearLayout) (childView.findViewById(R.id.lin_othr_spiner));

                    edtOE.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                            Double sumsTot = 0.0;

                            for (int k = 0; k < OeSize; k++) {
                                View cv = LinearOtherAllowance.getChildAt(k);
                                edtOE = (EditText) (cv.findViewById(R.id.oe_fre_amt));
                                String strs = edtOE.getText().toString();
                                if (strs.matches("")) strs = "0";
                                sumsTot = sumsTot + Double.parseDouble(strs);

                                sTotal = GrandTotalAllowance + sumsTot;
                            }
                            Log.d("Local ", String.valueOf(sumsTot));
                            // " Rs. " + new DecimalFormat("##0.00").format(fAmount) + " / KM "
                            OeText.setText("Rs. " + new DecimalFormat("##0.00").format(sumsTot));

                            otherExp = sumsTot;

                            calOverAllTotal(localCov, otherExp);
                        }
                    });


                    OtherExpense = (LinearLayout) childView.findViewById(R.id.lin_other_expense_dynamic);
                    Integer finalC = c;

                    linOtherSpinner.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            OtherExpenseList.clear();
                            OtherExpenseMode(finalC);
                            Log.e("CLICK_POSITION", String.valueOf(finalC));
                        }
                    });

                    oeAttach.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //popupCapture("OE");
                        }
                    });

                }


            }
        });
        LLocalConve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = null;


                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                layoutParams.setMargins(15, 15, 15, 15);

                rowView = inflater.inflate(R.layout.activity_local_convenyance, null);


                linlocalCon.addView(rowView, layoutParams);
                localText.setVisibility(View.VISIBLE);

                lcSize = linlocalCon.getChildCount();
                Log.d("PARENT_COUNT", String.valueOf(lcSize));


                for (int c = 0; c < lcSize; c++) {
                    childView = linlocalCon.getChildAt(c);
                    localTotal.setVisibility(View.VISIBLE);
                    editTexts = (TextView) (childView.findViewById(R.id.local_enter_mode));
                    linLocalSpinner = (LinearLayout) childView.findViewById(R.id.lin_loc_spiner);
                    lcAttach = (ImageView) (childView.findViewById(R.id.la_attach_iamg));
                    lcPreview = (ImageView) (childView.findViewById(R.id.img_prvw_lc));


                    editLaFare = (EditText) (childView.findViewById(R.id.edt_la_fare));
                    editLaFare.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                            Double sum = 0.0;

                            for (int k = 0; k < lcSize; k++) {
                                View cv = linlocalCon.getChildAt(k);
                                editLaFare = (EditText) (cv.findViewById(R.id.edt_la_fare));
                                String str = editLaFare.getText().toString();
                                if (str.matches("")) str = "0";
                                sum = sum + Double.parseDouble(str);
                                sums = GrandTotalAllowance + sum;
                            }
                            Log.d("Local ", String.valueOf(sum));
                            // " Rs. " + new DecimalFormat("##0.00").format(fAmount) + " / KM "
                            localText.setText("Rs. " + new DecimalFormat("##0.00").format(sum));
                            localCov = sum;


                            calOverAllTotal(localCov, otherExp);
                        }
                    });


                    Dynamicallowance = (LinearLayout) childView.findViewById(R.id.lin_allowance_dynamic);
                    Integer finalC = c;

                    linLocalSpinner.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listOrderType.clear();
                            dynamicModeType(finalC);
                            Log.e("CLICK_POSITION", String.valueOf(finalC));
                        }
                    });

                    lcAttach.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //    popupCapture("LC");
                        }
                    });

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
   /*             for (int i = 0; i < picPath.size(); i++) {
                    Log.e("UPloadimageThiru", String.valueOf(picPath.size()));
                    getMulipart(picPath.get(i), -1);
                }
                for (int i = 0; i < array.size(); i++) {
                    String[] imp = array.get(i).getImg_url().split(",");
                    for (int j = 0; j < imp.length - 1; j++) {
                        getMulipart(imp[j], i);
                    }
                }*/

                if (txt_date.getText().toString().matches("")) {
                    Toast.makeText(TAClaimActivity.this, "Please choose Date", Toast.LENGTH_SHORT).show();
                } else {
                    submitData();
                }

            }
        });
        DailyExpenseAdapter.bindUpdateListener(new UpdateUi() {
            @Override
            public void update(int value, int pos1) {
                pos = pos1;
                //  popupCapture("attachName");
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
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = null;


        linAddAllowance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = null;


                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                layoutParams.setMargins(15, 15, 15, 15);

                tlString.add(new ArrayList<>());

                if (StrToEnd.equals("0")) {
                    crdDynamicLocation.setVisibility(View.VISIBLE);
                    rowView = inflater.inflate(R.layout.travel_allowance_dynamic, null);
                    travelDynamicLoaction.addView(rowView, layoutParams);
                    deleteButton = findViewById(R.id.delete_button);
                    size = travelDynamicLoaction.getChildCount();
                    // Log.d("PARENT_COUNT", String.valueOf(size));


                    for (int c = 0; c < size; c++) {
                        Integer finalC = c;

                        Log.d("Travel_location", String.valueOf(c));
                        childView = travelDynamicLoaction.getChildAt(c);


                        Log.e("ARRAY_LIST", String.valueOf(tlString.size()));

                        editText = (TextView) (childView.findViewById(R.id.enter_mode));

                        taAttach = (ImageView) (childView.findViewById(R.id.image_attach));
                        previewss = (ImageView) (childView.findViewById(R.id.image_preview));
                        previewss.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                taCount = finalC;
                                Log.e("ImageArray_1", String.valueOf(taCount));
                                Log.e("ImageArray_final", String.valueOf(finalC));
                                Log.e("ImageArray_2", taCount + "   " + (tlString.get(taCount).toString()));

                                nonZero = tlString.get(taCount);
                                Log.v("NON_ZERO_ARRAY", nonZero.toString());
                                Log.v("NON_ZERO_ARRAY", String.valueOf(nonZero.size()));

                                if (nonZero.size() != 0) {
                                    Intent stat = new Intent(getApplicationContext(), AttachementActivity.class);
                                    stat.putExtra("Data", nonZero);
                                    startActivity(stat);
                                } else {
                                    Toast.makeText(TAClaimActivity.this, "Nothing to preview", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                        editText.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                modelTravelType.clear();
                                localCon(finalC);
                                Log.e("CLICK_POSITION", String.valueOf(finalC));
                            }
                        });


                        taAttach.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                taCount = finalC;
                                popupCapture(finalC);
                                Log.e("ATTACH_POSITION", String.valueOf(finalC));

                            }
                        });


                    }
                } else {
                    rowView = inflater.inflate(R.layout.travel_allowance_dynamic_one, null);
                    travelDynamicLoaction.addView(rowView, layoutParams);
                    crdDynamicLocation.setVisibility(View.VISIBLE);
                    deleteButton = findViewById(R.id.delete_button);
                    size = travelDynamicLoaction.getChildCount();
                    Log.d("PARENT_COUNT", String.valueOf(size));
                }

            }
        });


        mapZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateTime = DateTime.replaceAll("^[\"']+|[\"']+$", "");
                Intent mapZoom = new Intent(getApplicationContext(), MapZoomIn.class);
                mapZoom.putExtra("date", DateTime);

                startActivity(mapZoom);

                overridePendingTransition(R.anim.fade, R.anim.fade);

            }
        });


        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("SUBMIT", "SUBMIT");
                submitData();
            }
        });
    }

    private void calOverAllTotal(Double localCov, Double otherExp) {
        Double s = localCov + otherExp + GrandTotalAllowance + TotLdging;
        grandTotal.setText("Rs. " + new DecimalFormat("##0.00").format(s));
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
                        ldg_cin.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
        //Calendar calendarmin = Calendar.getInstance();
        //calendarmin.set(Integer.parseInt(minYear), Integer.parseInt(minMonth) - 1, Integer.parseInt(minDay));
        //picker.getDatePicker().setMinDate(calendarmin.getTimeInMillis());

        picker.show();
    }

    public void onTADelete(View v) {
        int size = travelDynamicLoaction.getChildCount();
        Log.d("PARENT_COUNT", String.valueOf(size));
        travelDynamicLoaction.removeView((View) v.getParent());
        if (size == 0) {
            crdDynamicLocation.setVisibility(View.GONE);
        }

        tlString.remove(v);

    }


    public void onLCDelete(View v) {
        // Log.d("PARENT_COUNT", String.valueOf(lcSize));
        Log.d("LC_PARENT_COUNT", String.valueOf(linlocalCon.getChildCount()));
        linlocalCon.removeView((View) v.getParent());
        if (linlocalCon.getChildCount() == 0) {
            localTotal.setVisibility(View.GONE);
        }

        Double sum = 0.0;

        int lcSize = linlocalCon.getChildCount();

        for (int k = 0; k < lcSize; k++) {
            View view = linlocalCon.getChildAt(k);
            editLaFare = (EditText) (view.findViewById(R.id.edt_la_fare));
            String str = editLaFare.getText().toString();
            if (str.matches("")) str = "0";
            sum = sum + Double.parseDouble(str);
            sums = GrandTotalAllowance + sum;
        }
        Log.d("Local ", String.valueOf(sum));
        // " Rs. " + new DecimalFormat("##0.00").format(fAmount) + " / KM "
        localText.setText("Rs. " + new DecimalFormat("##0.00").format(sum));
        localCov = sum;


        calOverAllTotal(localCov, otherExp);

    }

    public void onOEDelete(View v) {

        int lcSizes = LinearOtherAllowance.getChildCount() - 1;
        Log.d("OE_PARENT_COUNT", String.valueOf(lcSizes));
        LinearOtherAllowance.removeView((View) v.getParent());

        if (LinearOtherAllowance.getChildCount() == 0) {
            otherExpenseLayout.setVisibility(View.GONE);
        }


        Double sumsTot = 0.0;
        int OeSize = LinearOtherAllowance.getChildCount();
        for (int k = 0; k < OeSize; k++) {
            String strs = edtOE.getText().toString();
            if (strs.matches("")) strs = "0";
            sumsTot = sumsTot + Double.parseDouble(strs);

            sTotal = GrandTotalAllowance + sumsTot;
        }
        Log.d("Local ", String.valueOf(sumsTot));
        // " Rs. " + new DecimalFormat("##0.00").format(fAmount) + " / KM "
        OeText.setText("Rs. " + new DecimalFormat("##0.00").format(sumsTot));

        otherExp = sumsTot;

        calOverAllTotal(localCov, otherExp);
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
    }


    public void onGetEmpDetails(View v) {
        View pv = (View) v.getParent().getParent();
        edt_ldg_JnEmp = pv.findViewById(R.id.edt_ldg_JnEmp);
        String sEmpID = String.valueOf(edt_ldg_JnEmp.getText());
        DateTime = DateTime.replaceAll("^[\"']+|[\"']+$", "");
        Log.d("EmpNo", sEmpID);
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
                txtJNMyEli.setText("Rs. " + new DecimalFormat("##0.00").format(sum));
                SumOFJointLodging();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("Error:", "Some Error" + t.getMessage());
            }
        });
    }


    public void SumOFJointLodging() {
        float tTotAmt = 0;
        for (int i = 0; i < jointLodging.getChildCount(); i++) {
            View childView = jointLodging.getChildAt(i);
            TextView jLdgEli = (TextView) childView.findViewById(R.id.txtJNMyEli);
            String sAmt = jLdgEli.getText().toString().replaceAll("Rs. ", "");
            tTotAmt = tTotAmt + Float.parseFloat(sAmt);
        }
        txtJNEligi.setText("Rs. " + new DecimalFormat("##0.00").format(tTotAmt));
        SumOFLodging();
    }

    public void SumOFLodging() {
        String sMyAmt = txtMyEligi.getText().toString().replaceAll("Rs. ", "");
        String sJnAmt = txtJNEligi.getText().toString().replaceAll("Rs. ", "");
        String sDrivAmt = txtDrivEligi.getText().toString().replaceAll("Rs. ", "");

        double tTotAmt = Float.parseFloat(sMyAmt) + Float.parseFloat(sJnAmt) + Float.parseFloat(sDrivAmt);
        lbl_ldg_eligi.setText("Rs. " + new DecimalFormat("##0.00").format(tTotAmt));
        TotLdging = tTotAmt;
        SumWOBLodging();

        calOverAllTotal(localCov, otherExp);
    }

    public void SumWOBLodging() {
        String sldgAmt = lbl_ldg_eligi.getText().toString().replaceAll("Rs. ", "");
        String sBillAmt = edt_ldg_bill.getText().toString().replaceAll("Rs. ", "");
        if (sBillAmt.isEmpty()) sBillAmt = "0";
        float tBalAmt = Float.parseFloat(sldgAmt) - Float.parseFloat(sBillAmt);
        ldgWOBBal.setText("Rs. " + new DecimalFormat("##0.00").format(tBalAmt));
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
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
            }
        });
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
                Log.d("LeaveTypeList", "Error");
            }
        });
    }

    /*Display Mode of travel View based on the choosed Date*/
    public void displayTravelMode(String ChoosedDate) {
        Log.d("JSON_VALUE_O", ChoosedDate);
        /*      if(polyline1!=null) polyline1.remove();*/


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
        Call<JsonObject> call = apiInterface.getTAdateDetails(jj.toString());
        String finalChoosedDate = ChoosedDate;
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonArray jsonArray = null;
                JsonArray lcDraftArray = null;
                JsonArray oeDraftArray = null;
                JsonObject jsonObjects = response.body();
                Log.v("JSON_OBJECT", jsonObjects.toString());
                jsonArray = jsonObjects.getAsJsonArray("TodayStart_Details");
                lcDraftArray = jsonObjects.getAsJsonArray("Additional_ExpenseLC");
                oeDraftArray = jsonObjects.getAsJsonArray("Additional_ExpenseOE");

                callMap(finalChoosedDate);
                JsonObject jsonObject = null;
                for (int i = 0; i < jsonArray.size(); i++) {
                    int finalC = i;

                    Log.e("JsonArray", String.valueOf(jsonArray.size()));
                    jsonObject = (JsonObject) jsonArray.get(i);

                    linDailyAllowance.setVisibility(View.VISIBLE);

                    StartedKm = String.valueOf(jsonObject.get("Start_Km"));
                    StrToEnd = String.valueOf(jsonObject.get("StEndNeed"));
                    StrBus = String.valueOf(jsonObject.get("From_Place"));
                    StrTo = String.valueOf(jsonObject.get("To_Place"));
                    StrDaName = String.valueOf(jsonObject.get("MOT_Name"));
                    StrDailyAllowance = String.valueOf(jsonObject.get("dailyAllowance"));
                    StrDailyAllowance = StrDailyAllowance.replaceAll("^[\"']+|[\"']+$", "");
                    StrDaName = StrDaName.replaceAll("^[\"']+|[\"']+$", "");
                    StrToEnd = StrToEnd.replaceAll("^[\"']+|[\"']+$", "");
                    strFuelAmount = String.valueOf(jsonObject.get("FuelAmt"));
                    allowanceAmt = String.valueOf(jsonObject.get("Allowance_Value"));
                    start_Image = String.valueOf(jsonObject.get("start_Photo"));
                    End_Imge = String.valueOf(jsonObject.get("End_photo"));
                    ClosingKm = String.valueOf(jsonObject.get("End_Km"));
                    PersonalKm = String.valueOf(jsonObject.get("Personal_Km"));

                    Glide.with(getApplicationContext())
                            .load(start_Image.replaceAll("^[\"']+|[\"']+$", ""))
                            .into(startkmimage);
                    Glide.with(getApplicationContext())
                            .load(End_Imge.replaceAll("^[\"']+|[\"']+$", ""))
                            .into(endkmimage);

                    fAmount = Double.valueOf(strFuelAmount);
                    fuelAmount.setText(" Rs. " + new DecimalFormat("##0.00").format(fAmount) + " / KM ");
                    txtTaClaim.setText(StrDaName);


                    Log.e("dailyAllowance", StrDailyAllowance);
                    txtDailyAllowance.setText(StrDailyAllowance);


                    if (StrDailyAllowance.equals("Out Station")) {
                        doubleAmount = 0.0;
                        txtallamt.setText("");
                    } else {
                        allowanceAmt = allowanceAmt.replaceAll("^[\"']+|[\"']+$", "");
                        doubleAmount = Double.valueOf(allowanceAmt);
                        txtallamt.setText(" Rs. " + new DecimalFormat("##0.00").format(doubleAmount));
                    }

                    Log.e("STRTOEND", StartedKm);
                    Log.e("STR", StrBus);
                    Log.e("STREND", StrTo);
                    Log.e("STRDAILY", StrDailyAllowance);

                    if (StrToEnd.equals("0")) {
                        StrBus = StrBus.replaceAll("^[\"']+|[\"']+$", "");
                        StrTo = StrTo.replaceAll("^[\"']+|[\"']+$", "");

                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                        layoutParams.setMargins(15, 15, 15, 15);

                        rowView = inflater.inflate(R.layout.travel_allowance_dynamic, null);

                        travelDynamicLoaction.addView(rowView, layoutParams);

                        tlString.add(new ArrayList<>());


                        View views = travelDynamicLoaction.getChildAt(0);
                        LinearLayout lad = (LinearLayout) views.findViewById(R.id.linear_row_ad);
                        editText = (TextView) views.findViewById(R.id.enter_mode);

                        enterFrom = views.findViewById(R.id.enter_from);
                        enterTo = views.findViewById(R.id.enter_to);
                        enterFare = views.findViewById(R.id.enter_fare);
                        deleteButton = views.findViewById(R.id.delete_button);
                        taAttach = (ImageView) views.findViewById(R.id.image_attach);
                        taAttach.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popupCapture(finalC);

                            }
                        });


                        previewss = (ImageView) (views.findViewById(R.id.image_preview));
                        previewss.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                           /*     Log.e("ImageArray_1", String.valueOf(tlString.size()));
                                Log.e("ImageArray_2", (tlString.get(taCount-1).toString()));*/

                                Log.e("STRING_COOUNT", String.valueOf((0)));


                                zero = tlString.get(0);

                                Log.e("ZERO_ARRAY", String.valueOf(zero.size()));
                                Log.e("ZERO_ARRAY", zero.toString());

                                if (zero.size() != 0) {
                                    Intent stat = new Intent(getApplicationContext(), AttachementActivity.class);
                                    stat.putExtra("Data", zero);
                                    startActivity(stat);
                                } else {
                                    Toast.makeText(TAClaimActivity.this, "Nothing to preview", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        editText.setText(StrDaName);
                        enterFrom.setText(StrBus);
                        enterTo.setText(StrTo);
                        deleteButton.setVisibility(View.GONE);


                        lad.setClickable(false);
                        lad.setOnClickListener(null);
                        editText.setOnClickListener(null);
                        editText.setClickable(false);
                        enterFrom.setEnabled(false);
                        enterTo.setEnabled(false);


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

                    StartedKm = StartedKm.replaceAll("^[\"']+|[\"']+$", "");
                    if (StartedKm != null && !StartedKm.isEmpty() && !StartedKm.equals("null") && !StartedKm.equals("")) {
                        Log.e("TxtStartedKm2", StartedKm);
                        S = Integer.valueOf(StartedKm);
                        Log.e("TxtStartedKm3", String.valueOf(S));
                        TxtStartedKm.setText(StartedKm);
                    } else {


                    }


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
                        Log.e("TOTAL_KM", String.valueOf(C));
                        totalkm = C - S;
                        Double totalAmount = Double.valueOf(strFuelAmount);


                        tofuel = totalkm * totalAmount;
                        Log.e("TOTAL_Claim_KM", String.valueOf(totalPersonalKm));
                        Log.e("TOTAL_tofuel", String.valueOf(tofuel));

                        TextTotalAmount.setText("Rs. " + new DecimalFormat("##0.00").format(tofuel));
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

                GrandTotalAllowance = doubleAmount + tofuel;
                grandTotal.setText("Rs. " + GrandTotalAllowance.toString());



                /*Local Convenyance*/
                if (lcDraftArray != null || lcDraftArray.size() != 0) {
                    localConDraft(lcDraftArray);
                }

                OeDraft(oeDraftArray);
            }


            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("LeaveTypeList", "Error");
            }
        });
    }


    public void localConDraft(JsonArray lcDraft) {
        Log.v("lcDraftArray_inner", lcDraft.toString());
        Log.v("lcDraftArray_inner", String.valueOf(lcDraft.size()));

        JsonArray jsonAddition = null;
        for (int i = 0; i < lcDraft.size(); i++) {
            JsonObject lcdraftJson = (JsonObject) lcDraft.get(i);

            jsonAddition = lcdraftJson.getAsJsonArray("Additional");

            Log.v("JSON_ADDITION", jsonAddition.toString());
            String expCode = String.valueOf(lcdraftJson.get("Exp_Code"));

            expCode = expCode.replaceAll("^[\"']+|[\"']+$", "");


            Log.v("expCode", expCode);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = null;

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            layoutParams.setMargins(15, 15, 15, 15);

            rowView = inflater.inflate(R.layout.activity_local_convenyance, null);


            linlocalCon.addView(rowView, layoutParams);
            localText.setVisibility(View.VISIBLE);
            int lcS = linlocalCon.getChildCount() - 1;
            Log.v("LinCon", String.valueOf(lcS));


            View view = linlocalCon.getChildAt(i);
            editTexts = (TextView) (view.findViewById(R.id.local_enter_mode));
            edtLcFare = (EditText) (view.findViewById(R.id.edt_la_fare));
            linLocalSpinner = (LinearLayout) view.findViewById(R.id.lin_loc_spiner);
            Dynamicallowance = (LinearLayout) view.findViewById(R.id.lin_allowance_dynamic);

            linLocalSpinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listOrderType.clear();
                    dynamicModeType(lcS);
                    Log.e("CLICK_POSITION", String.valueOf(lcS));
                }
            });

            localConDisplay(expCode, jsonAddition, lcS);


            editTexts.setText(expCode);
            edtLcFare.setText(lcdraftJson.get("Exp_Amt").toString());


        }


    }


    public void OeDraft(JsonArray oEDraft) {
        Log.v("oEDraftArray_inner", oEDraft.toString());
        Log.v("oEDraftArray_inner", String.valueOf(oEDraft.size()));

        //  JsonArray jsonAddition = null;
        for (int i = 0; i < oEDraft.size(); i++) {
            JsonObject lcdraftJson = (JsonObject) oEDraft.get(i);

            // jsonAddition = lcdraftJson.getAsJsonArray("Additional");


            String expCode = String.valueOf(lcdraftJson.get("Exp_Code"));

            expCode = expCode.replaceAll("^[\"']+|[\"']+$", "");


            Log.v("expCode", expCode);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = null;

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            layoutParams.setMargins(15, 15, 15, 15);

            rowView = inflater.inflate(R.layout.activity_other_expense, null);


            LinearOtherAllowance.addView(rowView, layoutParams);
            otherTotal.setVisibility(View.VISIBLE);
            int lcS = LinearOtherAllowance.getChildCount() - 1;
            Log.v("LinCon", String.valueOf(lcS));


            childView = LinearOtherAllowance.getChildAt(i);
            otherExpenseLayout.setVisibility(View.VISIBLE);
            oeEditext = (TextView) (childView.findViewById(R.id.other_enter_mode));
            edtOE = (EditText) (childView.findViewById(R.id.oe_fre_amt));
            oeAttach = (ImageView) (childView.findViewById(R.id.oe_attach_img));
            oePreview = (ImageView) (childView.findViewById(R.id.img_prvw_oe));
            linOtherSpinner = (LinearLayout) (childView.findViewById(R.id.lin_othr_spiner));

            linOtherSpinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OtherExpenseList.clear();
                    OtherExpenseMode(lcS);
                    Log.e("CLICK_POSITION", String.valueOf(lcS));
                }
            });

            //     localConDisplay(expCode, jsonAddition, lcS);


            oeEditext.setText(expCode);
            edtOE.setText(lcdraftJson.get("Exp_Amt").toString());


        }


    }


    @SuppressLint("ResourceType")
    public void localConDisplay(String modeName, JsonArray jsonAddition, int position) {

        Log.v("String_Mode_Name", modeName);
        Log.v("String_Mode_Name", jsonAddition.toString());
        Log.v("KARTHICKUMAR_SIZE", String.valueOf(jsonAddition.size()));
        Log.v("KARTHICKUMAR_SIZE", String.valueOf(position));

        JsonObject jsonObjectAdd = null;
        for (int l = 0; l < jsonAddition.size(); l++) {

            jsonObjectAdd = (JsonObject) jsonAddition.get(l);
            String edtValueb = String.valueOf(jsonObjectAdd.get("Ref_Value"));
            Log.v("KARTHICKUMAR", edtValueb);
            RelativeLayout childRel = new RelativeLayout(getApplicationContext());
            RelativeLayout.LayoutParams layoutparams_3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutparams_3.addRule(RelativeLayout.ALIGN_PARENT_START);
            layoutparams_3.setMargins(20, -10, 0, 0);
            edt = new EditText(getApplicationContext());
            edt.setLayoutParams(layoutparams_3);
            edt.setText(edtValueb.replaceAll("^[\"']+|[\"']+$", ""));
            edt.setId(12345);
            edt.setTextSize(13);
            childRel.addView(edt);
            View view = linlocalCon.getChildAt(position);
            Dynamicallowance = (LinearLayout) view.findViewById(R.id.lin_allowance_dynamic);
            Dynamicallowance.addView(childRel);

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10) {
            //TODO: action
            ClipData mClipData = data.getClipData();


            Log.v("onactivity_result", mClipData + " ");
            int pickedImageCount;
            String filepathing = "";
            if (data.getData() == null) {
                Log.v("onactivity_result", data.getData() + " ");

                for (pickedImageCount = 0; pickedImageCount < mClipData.getItemCount();
                     pickedImageCount++) {
                    Log.v("picked_image_value", mClipData.getItemAt(pickedImageCount).getUri() + "");
                    ImageFilePath filepath = new ImageFilePath();
                    Log.v("Upload_one", String.valueOf(filepath));
                    fullPath = filepath.getPath(TAClaimActivity.this, mClipData.getItemAt(pickedImageCount).getUri());
/*if(arrImage.size()!=0){
    a
}*/
                    tlString.get(taCount).add(fullPath);

                    Log.e("STRING_COOUNT", String.valueOf((taCount)));
                    getMulipart(fullPath, 0);
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
                fullPath = filepath.getPath(TAClaimActivity.this, data.getData());
                Log.v("Upload_Image_two", fullPath);
                /////////  arrImage.add(fullPath);
                if (pos == -1)
                    picPath.add(fullPath);

                else {
                    SelectionModel m = array.get(pos);
                    filepathing = m.getImg_url();
                    filepathing = filepathing + fullPath + ",";
                    m.setImg_url(filepathing);

                }
            }

        } else if (requestCode == 1 && resultCode == Activity.RESULT_OK) {


            String finalPath = "/storage/emulated/0";
            filePath = outputFileUri.getPath();
            filePath = filePath.substring(1);
            filePath = finalPath + filePath.substring(filePath.indexOf("/"));
            Log.v("Upload_Image_three", filePath);
            ////////////     arrImage.add(filePath);
            getMulipart(filePath, 0);
            Log.v("printing__Position", String.valueOf(pos));
            if (pos == -1) {
                Log.v("printing__eventListStr", filePath);
            } else {
                SelectionModel m = array.get(pos);
                String filepathing = "";
                filepathing = m.getImg_url();
                filepathing = filepathing + filePath + ",";
                m.setImg_url(filepathing);
            }

        }


    }

    public void submitData() {


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

            JSONArray trvLoc = new JSONArray();
            int travelBike = travelDynamicLoaction.getChildCount();
            if (StrToEnd.equals("1")) {
                for (int i = 0; i < travelBike; i++) {
                    JSONObject jsonTrLoc = new JSONObject();

                    View views = travelDynamicLoaction.getChildAt(i);
                    enterFrom = (EditText) views.findViewById(R.id.enter_from);
                    enterTo = (EditText) views.findViewById(R.id.enter_to);
                    jsonTrLoc.put("from", enterFrom.getText().toString());
                    jsonTrLoc.put("to", enterTo.getText().toString());
                    jsonTrLoc.put("trv_loc_img", "");
                    trvLoc.put(jsonTrLoc);
                }
            } else {
                for (int i = 0; i < travelBike; i++) {
                    JSONObject jsonTrLoc = new JSONObject();

                    View views = travelDynamicLoaction.getChildAt(i);
                    editText = views.findViewById(R.id.enter_mode);
                    enterFrom = views.findViewById(R.id.enter_from);
                    enterTo = views.findViewById(R.id.enter_to);
                    enterFare = views.findViewById(R.id.enter_fare);
                    deleteButton = findViewById(R.id.delete_button);
                    jsonTrLoc.put("MODE", editText.getText().toString());
                    jsonTrLoc.put("FROM", enterFrom.getText().toString());
                    jsonTrLoc.put("TO", enterTo.getText().toString());
                    jsonTrLoc.put("FARE", enterFare.getText().toString());
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
                Dynamicallowance = (LinearLayout) childView.findViewById(R.id.lin_allowance_dynamic);
                editMode = editTexts.getText().toString();
                newEdt = usersByCountry.get(editMode);

                JSONObject lcMode = new JSONObject();
                lcMode.put("type", editTexts.getText().toString());
                lcMode.put("total_amount", editLaFare.getText().toString());
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


            for (int lc = 0; lc < addOtherExp; lc++) {
                View view = LinearOtherAllowance.getChildAt(lc);
                oeEditext = (TextView) (view.findViewById(R.id.other_enter_mode));
                edtOE = (EditText) (view.findViewById(R.id.oe_fre_amt));
                OtherExpense = (LinearLayout) view.findViewById(R.id.lin_other_expense_dynamic);
                editMode = oeEditext.getText().toString();
                newEdt = usersByCountry.get(editMode);
                JSONObject lcModes2 = new JSONObject();
                lcModes2.put("type", editMode);
                lcModes2.put("total_amount", edtOE.getText().toString());
                lcModes2.put("exp_type", "OE");

 /*            JSONArray lcModeRef1 = new JSONArray();

                for (int da = 0; da < newEdt.size(); da++) {
                    JSONObject AditionallLocalConvenyance = new JSONObject();

                    AditionallLocalConvenyance.put("KEY", dynamicLabelList.get(da));
                    AditionallLocalConvenyance.put("VALUE", newEdt.get(da).getText().toString());

                    lcModeRef1.put(AditionallLocalConvenyance);
                }
                lcModes2.put("ad_exp", lcModeRef1);*/
                othrExp.put(lcModes2);

            }

            Log.v("OTHER_EXPENSE", othrExp.toString());

            jsonData.put("Add_Exp", addExp);
            jsonData.put("Other_Exp", othrExp);
            jsonData.put("Trv_details", trDet);


            transHead.put(jsonData);


            Log.e("TOTAL_JSON", transHead.toString());
            Log.e("TOTAL_JSON_HHHH", jsonData.toString());

        } catch (Exception e) {
            Log.e("TOTAL_JSON_OUT", e.toString());
        }


        Call<ResponseBody> submit = apiInterface.saveDailyAllowance(jsonData.toString());
        submit.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(TAClaimActivity.this, "Submitted Successfully ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }

    public void getMulipart(String path, int x) {
        MultipartBody.Part imgg = convertimg("file", path);
        HashMap<String, RequestBody> values = field(mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code));
        CallApiImage(values, imgg, x);
    }

    public MultipartBody.Part convertimg(String tag, String path) {
        MultipartBody.Part yy = null;
        Log.v("Upload_Image_three", path);
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
        Log.v("Upload_Image_three", yy + "");
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


    public void popupCapture(Integer attachName) {
        Log.d("AttachName", String.valueOf(attachName));
        dialog = new Dialog(TAClaimActivity.this, R.style.AlertDialogCustom);
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
                captureFile(attachName);
            }
        });
    }

    public void selectMultiImage() {
        dialog.dismiss();
        Intent intent = new Intent();
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
    }

    public void captureFile(Integer positionC) {
        dialog.dismiss();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Uri outputFileUri = Uri.fromFile(new File(getExternalCacheDir().getPath(), "pickImageResult.jpeg"));
        outputFileUri = FileProvider.getUriForFile(TAClaimActivity.this, getApplicationContext().getPackageName() + ".provider", new File(getExternalCacheDir().getPath(), Shared_Common_Pref.Sf_Code + "_" + System.currentTimeMillis() + ".jpeg"));
        Log.v("priniting_uri", outputFileUri.toString() + " output " + outputFileUri.getPath() + " raw_msg " + getExternalCacheDir().getPath());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        intent.putExtra("OUTPUT", "outputFileUri");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        setResult(Activity.RESULT_OK, intent);
        startActivityForResult(intent, positionC);
        eventListStr = String.valueOf(outputFileUri);
    }

    public void callApi(String date, String OS) {

        ArrayList listValue = new ArrayList();
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


                                Log.d("SHORT_NAME", Exp_Name + "Attachment" + attachment);
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
    public void onClick(View v) {

    }

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        customDialog.dismiss();
        if (type == 9) {
            //"IS. Independent Stay","JS. Jointly Stay","RS. Stay At Relaytive House"

            String ValCd = myDataset.get(position).getId();
            String Valname = myDataset.get(position).getName();
            txt_ldg_type.setText(Valname);
            lodgCont.setVisibility(View.VISIBLE);
            ldg_stayloc.setVisibility(View.GONE);
            ldg_stayDt.setVisibility(View.GONE);
            lodgJoin.setVisibility(View.GONE);
            JNLdgEAra.setVisibility(View.GONE);
            edt_ldg_bill.setVisibility(View.GONE);
            lblHdBill.setVisibility(View.GONE);
            lblHdBln.setVisibility(View.GONE);
            ldgWOBBal.setVisibility(View.GONE);
            if (ValCd == "JS") {
                lodgJoin.setVisibility(View.VISIBLE);
                JNLdgEAra.setVisibility(View.VISIBLE);
                //ldgMyEAra,JNLdgEAra,drvldgEAra

                lodingView();
            }
            if (ValCd != "RS") {
                ldg_stayloc.setVisibility(View.VISIBLE);
                ldg_stayDt.setVisibility(View.VISIBLE);
                lblHdBill.setVisibility(View.VISIBLE);
                edt_ldg_bill.setVisibility(View.VISIBLE);
                lblHdBln.setVisibility(View.VISIBLE);
                ldgWOBBal.setVisibility(View.VISIBLE);
            }
        }
        if (type == 10) {
            txt_date.setText(myDataset.get(position).getName());
            Log.d("JSON_VALUE", myDataset.get(position).getId());
            DateTime = myDataset.get(position).getId();
            displayTravelMode(myDataset.get(position).getId());
            travelDynamicLoaction.removeAllViews();
            linAddAllowance.setVisibility(View.VISIBLE);
            linlocalCon.removeAllViews();
            LinearOtherAllowance.removeAllViews();
            localText.setText("");
            OeText.setText("");
            localTotal.setVisibility(View.GONE);
            otherTotal.setVisibility(View.GONE);
            linAll.setVisibility(View.VISIBLE);
            linRemarks.setVisibility(View.VISIBLE);
            linFareAmount.setVisibility(View.VISIBLE);
            lcString.clear();


        } else if (type == 11) {
            modeTextView.setText(myDataset.get(position).getName());
            Log.d("JSON_VALUE", myDataset.get(position).getName());
        } else if (type == 8) {

            Log.d("PARENT_COUNT", String.valueOf(size));

            Log.e("IMAGE_ATTACH_next", String.valueOf(myDataset.get(position).getPho()));
            Integer editTextPosition = myDataset.get(position).getPho();

            View view = travelDynamicLoaction.getChildAt(editTextPosition);
            editText = (TextView) (view.findViewById(R.id.enter_mode));
            // enterMode = view.findViewById(R.id.enter_mode);
            editText.setText(myDataset.get(position).getName());


        } else if (type == 80) {

            Log.d("PARENT_COUNT", String.valueOf(size));

            Log.e("IMAGE_ATTACH_next", String.valueOf(myDataset.get(position).getPho()));
            editTextPositionss = myDataset.get(position).getPho();

            View view = linlocalCon.getChildAt(editTextPositionss);
            editTexts = (TextView) (view.findViewById(R.id.local_enter_mode));

            edtLcFare = (EditText) (view.findViewById(R.id.edt_la_fare));
            // enterMode = view.findViewById(R.id.enter_mode);
            lcAttach = (ImageView) (view.findViewById(R.id.la_attach_iamg));
            lcPreview = (ImageView) (view.findViewById(R.id.img_prvw_lc));

            edtLcFare.setText("");

            editTexts.setText(myDataset.get(position).getName());
            StrModeValue = myDataset.get(position).getName();
            Log.e("StrMode", StrModeValue);
            Dynamicallowance = (LinearLayout) view.findViewById(R.id.lin_allowance_dynamic);
            Dynamicallowance.removeAllViews();
            LocalConvenyanceApi(StrModeValue);

            Log.e("ASD_Key", String.valueOf(AttachmentImg.keySet()));
            Log.e("ASD_Value", String.valueOf(AttachmentImg.values()));
            Log.e("ASD_Value", String.valueOf(AttachmentImg.get(StrModeValue)));
            if (AttachmentImg.get(StrModeValue).equals("1")) {
                lcAttach.setVisibility(View.VISIBLE);
                lcPreview.setVisibility(View.VISIBLE);
            } else {
                lcAttach.setVisibility(View.GONE);
                lcPreview.setVisibility(View.GONE);
            }


        } else if (type == 90) {

            Log.d("PARENT_COUNT", String.valueOf(size));
            Log.e("IMAGE_ATTACH_next", String.valueOf(myDataset.get(position).getPho()));
            editTextPositionss = myDataset.get(position).getPho();
            View view = LinearOtherAllowance.getChildAt(editTextPositionss);
            oeEditext = (TextView) (view.findViewById(R.id.other_enter_mode));
            oeAttach = (ImageView) (view.findViewById(R.id.oe_attach_img));
            oePreview = (ImageView) (view.findViewById(R.id.img_prvw_oe));

            Log.e("Attachment", myDataset.get(position).getAddress());

            OtherExpense = (LinearLayout) view.findViewById(R.id.lin_other_expense_dynamic);
            oeEditext.setText(myDataset.get(position).getName());
            StrModeValue = myDataset.get(position).getName();
            Log.e("StrMode", StrModeValue);
            OtherExpense.removeAllViews();
            OtherExpenseApi(StrModeValue);

            Log.e("ASD_Key", String.valueOf(AttachmentImg.keySet()));
            Log.e("ASD_Value", String.valueOf(AttachmentImg.values()));
            Log.e("ASD_Value", String.valueOf(AttachmentImg.get(StrModeValue)));


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

            Log.d("JSON_VALUE", myDataset.get(position).getName());


        } else if (type == 1) {
            enterMode.setText(myDataset.get(position).getName());
            Log.d("JSON_VALUE", myDataset.get(position).getName());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
    }




    /*Showing Map based on Map*/

    public void callMap(String date) {

        Log.v("Map_Date", date + " 00:00:00");
        date = date.replaceAll("^[\"']+|[\"']+$", "");

        Log.v("Map_Date", date);
 /* DateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
 Calendar calobj = Calendar.getInstance();
 String dateTime = df.format(calobj.getTime());
 Log.e("DATe_TIME", dateTime);*/

        Call<ResponseBody> Callto = apiInterface.getMap(SF_code, date + " 00:00:00");
        Callto.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                String jsonData = null;
                try {
                    jsonData = response.body().string();
                    try {
                        JSONArray jsonArray = new JSONArray(jsonData);
                        Double lat = 0.0, lon = 0.0, lat1 = 0.0, lon1 = 0.0;
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        for (int i = 0; i < jsonArray.length() - 1; i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String strLat = jsonObject.getString("lat");
                            String strLon = jsonObject.getString("lng");
                            if (!strLat.matches("")) {
                                lat = Double.parseDouble(strLat);
                                lon = Double.parseDouble(strLon);
                                LatLng loc = new LatLng(lat, lon);
                                builder.include(loc);
                                latLonArray.add(loc);
                            }
                        }


                        if (latLonArray.size() != 0) {

                            polyline1 = mGoogleMap.addPolyline(new PolylineOptions()
                                    .clickable(true)
                                    .addAll(latLonArray));

                            polyline1.setTag("A");
                            polyline1.setColor(COLOR_ORANGE_ARGB);

                            LatLngBounds bounds = builder.build();


                            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.v("response_data", jsonData);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


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
//String attachMode = String.valueOf(modelOfTravel.get(i).get)

                    Model_Pojo = new Common_Model(name, id, modeId, "", countPosition);
                    Log.e("LeaveType_Request", id);
                    Log.e("LeaveType_Request", name);
                    Log.e("DriverMode", driverMode);
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
        /* Dynamicallowance.removeAllViews();*/
        mCommon_model_spinner = new Common_Model("Independent Stay", "IS");
        ldgModes.add(mCommon_model_spinner);
        mCommon_model_spinner = new Common_Model("Jointly Stay", "JS");
        ldgModes.add(mCommon_model_spinner);
        mCommon_model_spinner = new Common_Model("Stay At Relaytive House", "RS");
        ldgModes.add(mCommon_model_spinner);

        customDialog = new CustomListViewDialog(TAClaimActivity.this, ldgModes, 9);
        Window window = customDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        customDialog.show();
    }

    public void dynamicModeType(Integer poisition) {
        /* Dynamicallowance.removeAllViews();*/
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
        /* Dynamicallowance.removeAllViews();*/
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

        Log.e("LC_VALUE", sss);
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


                                Log.v("LC_MODE", lcString.toString());
                                if (Exp_Name.equals(sss)) {
                                    Log.d("TAF", Exp_Name);
                                    Log.d("TAF", sss);
                                    Log.d("TAF", jsonHeaderObject.getString("user_enter"));
                                    Log.d("TAF", jsonHeaderObject.getString("Max_Allowance"));
                                    JSONArray additionArray = null;

                                    additionArray = jsonHeaderObject.getJSONArray("value");
                                    List<EditText> users = new ArrayList<>();
                                    for (int l = 0; l <= additionArray.length(); l++) {
                                        JSONObject json_in = additionArray.getJSONObject(l);
                                        dynamicLabel = json_in.getString("Ad_Fld_Name");
                                        dynamicLabelList.add(dynamicLabel);
                                        Log.d("TAF_Exp_Nameffdd", additionArray.toString());
                                        Log.d("TAF_Ex", dynamicLabel);
                                        Log.d("LC_MODE", dynamicLabelList.toString());
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
                                        //   allEds.add(edt);

                                        users.add(edt);


                                        Log.e("EDITTEX_SISE", "bb " + String.valueOf(users.size()));
                                        Log.e("EDITTEX_SISE", "aa " + String.valueOf(allEds.size()));
                                        if (l == additionArray.length() - 1) {
                                            usersByCountry.put(sss, users);
                                            Log.e("EDITTEX_SISE", "dd " + String.valueOf(usersByCountry.keySet()));
                                            Log.e("EDITTEX_SISE", "dd " + String.valueOf(usersByCountry.values()));
                                        }


                                        //Create new list


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

        Log.e("StrModeValue", sss);
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
                                    Log.d("TAF", Exp_Name);
                                    Log.d("TAF", sss);
                                    Log.d("TAF", jsonHeaderObject.getString("user_enter"));
                                    Log.d("TAF", jsonHeaderObject.getString("Max_Allowance"));
                                    JSONArray additionArray = null;

                                    additionArray = jsonHeaderObject.getJSONArray("value");
                                    List<EditText> otherExpenseEdit = new ArrayList<>();
                                    for (int l = 0; l <= additionArray.length(); l++) {
                                        JSONObject json_in = additionArray.getJSONObject(l);
                                        OEdynamicLabel = json_in.getString("Ad_Fld_Name");
                                        OEdynamicList.add(OEdynamicLabel);
                                        Log.d("TAF_Exp_Nameffdd", additionArray.toString());
                                        Log.d("TAF_Ex", OEdynamicLabel);
                                        Log.d("TAF_Ex", OEdynamicList.toString());
                                        RelativeLayout childRel = new RelativeLayout(getApplicationContext());
                                        RelativeLayout.LayoutParams layoutparams_3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                        layoutparams_3.addRule(RelativeLayout.ALIGN_PARENT_END);
                                        layoutparams_3.setMargins(20, 10, 0, 0);
                                        edt = new EditText(getApplicationContext());
                                        edt.setLayoutParams(layoutparams_3);
                                        for (int da = 0; da < OEdynamicList.size(); da++) {
                                            edt.setHint(OEdynamicList.get(da));
                                            Log.e("DYNAMICE_LABEL_LIST", OEdynamicList.get(da).toString());
                                        }
                                        edt.setId(12345);
                                        edt.setTextSize(13);
                                        edt.setTextColor(R.color.grey_500);
                                        edt.setBackgroundResource(R.drawable.textbox_bg);


                                        childRel.addView(edt);


                                        otherExpenseEdit.add(edt);

                                        if (l == additionArray.length() - 1) {
                                            userOtherExpense.put(sss, otherExpenseEdit);
                                            Log.e("EDITTEX_SISE", "dd " + String.valueOf(userOtherExpense.keySet()));
                                            Log.e("EDITTEX_SISE", "dd " + String.valueOf(userOtherExpense.values()));
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
}
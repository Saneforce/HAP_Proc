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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

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
    LinearLayout lin_daily_allowance;
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
    List<EditText> newEdt = new ArrayList<EditText>();
    Integer editTextPositionss;
    EditText editLaFare, edtOE;
    Map<String, List<EditText>> usersByCountry = new HashMap<String, List<EditText>>();
    Map<String, List<EditText>> userOtherExpense = new HashMap<String, List<EditText>>();
    List<EditText> users = new ArrayList<EditText>();
    List<EditText> otherExpenseEdit = new ArrayList<EditText>();
    String editMode = "";
    Double tofuel = 0.0;
    LinearLayout localTotal, otherTotal;

    ImageView taAttach, lcAttach, oeAttach;
    String allowanceAmt = "";
    TextView txtallamt;

    Double daTotal = 0.0, taTotal = 0.0, lcTotal = 0.0, oeTotal = 0.0;
    Double fAmount = 0.0;
    TextView grandTotal;
    Double doubleAmount = 0.0;
    TextView localText, OeText;
    Double GrandTotalAllowance = 0.0;
    double sTotal = 0.0;
    double sums = 0.0;
    LinearLayout otherExpenseLayout, linAll, linRemarks, linFareAmount;
    //ArrayList<String> AttachmentImg = new ArrayList<>();
    String strGT = "";
    Double dcGT = 0.0;
    Map<String, String> AttachmentImg = new HashMap<String, String>();
    Double otherExp = 0.0, localCov = 0.0;
    LinearLayout linLocalSpinner, linOtherSpinner;
    Dialog dialog;
    SharedPreferences CheckInDetails;

    LinearLayout LinearMap;

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
        lin_daily_allowance = findViewById(R.id.lin_da_type);
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
      /*  if (!strGT.matches("")) {
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
                            //      " Rs. " + new DecimalFormat("##0.00").format(fAmount) + " / KM "
                            OeText.setText("Total : Rs. " + new DecimalFormat("##0.00").format(sumsTot));

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
                            popupCapture("OE");
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
                            //      " Rs. " + new DecimalFormat("##0.00").format(fAmount) + " / KM "
                            localText.setText("Total : Rs. " + new DecimalFormat("##0.00").format(sum));
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
                            popupCapture("LC");
                        }
                    });

                }


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
                for (int i = 0; i < picPath.size(); i++) {
                    getMulipart(picPath.get(i), -1);
                }
                for (int i = 0; i < array.size(); i++) {
                    String[] imp = array.get(i).getImg_url().split(",");
                    for (int j = 0; j < imp.length - 1; j++) {
                        getMulipart(imp[j], i);
                    }
                }

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

                if (StrToEnd.equals("0")) {
                    crdDynamicLocation.setVisibility(View.VISIBLE);
                    rowView = inflater.inflate(R.layout.travel_allowance_dynamic, null);
                    travelDynamicLoaction.addView(rowView, layoutParams);
                    deleteButton = findViewById(R.id.delete_button);
                    size = travelDynamicLoaction.getChildCount();
                    Log.d("PARENT_COUNT", String.valueOf(size));


                    for (int c = 0; c < size; c++) {
                        childView = travelDynamicLoaction.getChildAt(c);

                        editText = (TextView) (childView.findViewById(R.id.enter_mode));
                        taAttach = (ImageView) (childView.findViewById(R.id.image_attach));
                        Integer finalC = c;

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
                                popupCapture("attachName");
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


    }

    private void calOverAllTotal(Double localCov, Double otherExp) {
        Double s = localCov + otherExp + GrandTotalAllowance;
        grandTotal.setText("Rs. " + new DecimalFormat("##0.00").format(s));
    }


    public void onTADelete(View v) {
        int size = travelDynamicLoaction.getChildCount();
        Log.d("PARENT_COUNT", String.valueOf(size));
        travelDynamicLoaction.removeView((View) v.getParent());
        if (size == 0) {
            crdDynamicLocation.setVisibility(View.GONE);
        }
    }


    public void onLCDelete(View v) {
        // Log.d("PARENT_COUNT", String.valueOf(lcSize));
        Log.d("PARENT_COUNT", String.valueOf(linlocalCon.getChildCount()));
        linlocalCon.removeView((View) v.getParent());
        if (linlocalCon.getChildCount() == 0) {
            localTotal.setVisibility(View.GONE);
        }

        Double sum = 0.0;

        int lcSize = linlocalCon.getChildCount();

        for (int k = 0; k < lcSize; k++) {
            String str = editLaFare.getText().toString();
            if (str.matches("")) str = "0";
            sum = sum + Double.parseDouble(str);
            sums = GrandTotalAllowance + sum;
        }
        Log.d("Local ", String.valueOf(sum));
        //      " Rs. " + new DecimalFormat("##0.00").format(fAmount) + " / KM "
        localText.setText("Total : Rs. " + new DecimalFormat("##0.00").format(sum));
        localCov = sum;


        calOverAllTotal(localCov, otherExp);

    }

    public void onOEDelete(View v) {
        Log.d("PARENT_COUNT", String.valueOf(lcSize));
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
        //      " Rs. " + new DecimalFormat("##0.00").format(fAmount) + " / KM "
        OeText.setText("Total : Rs. " + new DecimalFormat("##0.00").format(sumsTot));

        otherExp = sumsTot;

        calOverAllTotal(localCov, otherExp);
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
                callMap(DateTime);
                Log.d("JSON_VALUE", jsonArray.toString());
                Log.d("JSON_VALUE", "CHECKING");
                //  TravelBike.setVisibility(View.VISIBLE);
                lin_daily_allowance.setVisibility(View.VISIBLE);
                for (int a = 0; a < jsonArray.size(); a++) {
                    JsonObject jsonObject = (JsonObject) jsonArray.get(a);
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

                    fAmount = Double.valueOf(strFuelAmount);
                    fuelAmount.setText(" Rs. " + new DecimalFormat("##0.00").format(fAmount) + " / KM ");
                    txtTaClaim.setText(StrDaName);
                    txtDailyAllowance.setText(StrDailyAllowance);

                    if (StrDailyAllowance.equals("Out Station")) {
                        doubleAmount = 0.0;
                        txtallamt.setText("");
                    } else {
                        allowanceAmt = allowanceAmt.replaceAll("^[\"']+|[\"']+$", "");
                        doubleAmount = Double.valueOf(allowanceAmt);
                        txtallamt.setText(" Rs. " + new DecimalFormat("##0.00").format(doubleAmount));
                    }

                    Log.e("STRTOEND", StrToEnd);
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

                        /*  for (int i = 0; i <=0; i++) {*/
                        rowView = inflater.inflate(R.layout.travel_allowance_dynamic, null);
                        travelDynamicLoaction.addView(rowView, layoutParams);

                        View views = travelDynamicLoaction.getChildAt(0);
                        editText = views.findViewById(R.id.enter_mode);
                        enterFrom = views.findViewById(R.id.enter_from);
                        enterTo = views.findViewById(R.id.enter_to);
                        enterFare = views.findViewById(R.id.enter_fare);
                        deleteButton = findViewById(R.id.delete_button);

                        editText.setText(StrDaName);
                        enterFrom.setText(StrBus);
                        enterTo.setText(StrTo);
                        deleteButton.setVisibility(View.GONE);


                        editText.setOnClickListener(null);
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

                    ClosingKm = String.valueOf(jsonObject.get("End_Km"));
                    PersonalKm = String.valueOf(jsonObject.get("Personal_Km"));

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
                oeEditext = (TextView) (childView.findViewById(R.id.other_enter_mode));
                edtOE = (EditText) (view.findViewById(R.id.oe_fre_amt));
                OtherExpense = (LinearLayout) childView.findViewById(R.id.lin_other_expense_dynamic);
                editMode = editTexts.getText().toString();
                newEdt = usersByCountry.get(editMode);
                JSONObject lcModes = new JSONObject();
                lcModes.put("type", editTexts.getText().toString());
                lcModes.put("total_amount", edtOE.getText().toString());
                lcModes.put("exp_type", "OE");

                JSONArray lcModeRef = new JSONArray();

                for (int da = 0; da < newEdt.size(); da++) {
                    JSONObject AditionallLocalConvenyance = new JSONObject();

                    AditionallLocalConvenyance.put("KEY", dynamicLabelList.get(da));
                    AditionallLocalConvenyance.put("VALUE", newEdt.get(da).getText().toString());

                    lcModeRef.put(AditionallLocalConvenyance);
                }
                lcModes.put("ad_exp", lcModeRef);
                othrExp.put(lcModes);

            }

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
            /*    try {
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
                }*/


                Toast.makeText(TAClaimActivity.this, "Submitted Successfully ", Toast.LENGTH_SHORT).show();

         /*       txt_date.setText("");
                travelDynamicLoaction.removeAllViews();
                linlocalCon.removeAllViews();
                LinearOtherAllowance.removeAllViews();
                lin_daily_allowance.setVisibility(View.GONE);
                linlocalCon.setVisibility();
               */

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });




/*

        int localConven = linlocalCon.getChildCount();
        int travelColl = travelDynamicLoaction.getChildCount();
        Log.d("Local_COnvenyance", String.valueOf(localConven));

        String uniqueKey = SF_code + System.currentTimeMillis();
        Log.e("UNIQUE_KEY", uniqueKey);

        Log.e("EDIT_TEXT", String.valueOf(usersByCountry.keySet()));
        Log.e("EDIT_TEXT", String.valueOf(usersByCountry.values()));
        Log.e("EDIT_", String.valueOf(users.size()));
        Log.e("EDIT_", String.valueOf(users.toString()));

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar calobj = Calendar.getInstance();
        String dateTime = df.format(calobj.getTime());
        Log.e("DATe_TIME", dateTime);


        */
        /*        try {*//*


        JSONArray ja = new JSONArray();

        */
        /*Addtion Allowance Object*//*


        if (travelColl != 0) {
            if (StrToEnd.equals("0")) {

                JSONArray TravelAllowance = new JSONArray();
                JSONObject TravelModeAllowance = new JSONObject();

                Log.d("PARENT_COUNT_0", String.valueOf(size));
                for (int i = 0; i <= size; i++) {

                    Log.d("PARENT_COUNT_0_0", String.valueOf(size));
                    JSONObject AditionalTravelallowance = new JSONObject();
                    View views = travelDynamicLoaction.getChildAt(i);
                    editText = views.findViewById(R.id.enter_mode);
                    enterFrom = views.findViewById(R.id.enter_from);
                    enterTo = views.findViewById(R.id.enter_to);
                    enterFare = views.findViewById(R.id.enter_fare);
                    deleteButton = findViewById(R.id.delete_button);
                    try {
                        AditionalTravelallowance.put("MODE", editText.getText().toString());
                        AditionalTravelallowance.put("FROM", enterFrom.getText().toString());
                        AditionalTravelallowance.put("TO", enterTo.getText().toString());
                        AditionalTravelallowance.put("FARE", enterFare.getText().toString());
                        TravelModeAllowance.put("TravelAllowance", AditionalTravelallowance);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.e("Inside_mode", String.valueOf(editText.getText().toString()));
                    TravelAllowance.put(TravelModeAllowance);
                }

                Log.d("Karthick_ARRAY", TravelAllowance.toString());
            } else {
                JSONArray TravelAllowance = new JSONArray();

                Log.d("PARENT_COUNT_1", String.valueOf(size));
                for (int i = 0; i <= size; i++) {
                    Log.d("PARENT_COUNT_1_1", String.valueOf(size));
                    JSONObject AditionalTravelallowance = new JSONObject();
                    View views = travelDynamicLoaction.getChildAt(i);
                    enterFrom = (EditText) views.findViewById(R.id.enter_from);
                    enterTo = (EditText) views.findViewById(R.id.enter_to);
                    try {
                        AditionalTravelallowance.put("FROM", enterFrom.getText().toString());
                        AditionalTravelallowance.put("TO", enterTo.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.e("Inside_mode", String.valueOf(editText.getText().toString()));
                    TravelAllowance.put(AditionalTravelallowance);
                }

                Log.d("Karthick_ARRAY", TravelAllowance.toString());
            }
        }

        JSONArray AditionalLAarray = new JSONArray();
        JSONArray jsonObject = new JSONArray();


        Log.e("LENGHT_Size", String.valueOf(localConven));

        for (int la = 0; la < localConven; la++) {

            Log.e("LENGHT_Size", String.valueOf(localConven));
            Log.e("LENGHT_EDIT", editTexts.getText().toString());

            View view = linlocalCon.getChildAt(la);
            editTexts = (TextView) (view.findViewById(R.id.local_enter_mode));
            Log.e("STRING_DATE", editTexts.getText().toString());
            editLaFare = (EditText) (view.findViewById(R.id.edt_la_fare));
            Dynamicallowance = (LinearLayout) childView.findViewById(R.id.lin_allowance_dynamic);
            JSONObject AditionallLocalConvenyance = null;


            Log.e("allEDt_Size", String.valueOf(allEds.size()));

            editMode = editTexts.getText().toString();
            newEdt = usersByCountry.get(editMode);
            JSONObject asd = new JSONObject();
            Log.e("STRING_DATE", String.valueOf(editMode));
            Log.e("STRING_DATE", String.valueOf(usersByCountry.get(editMode)));
            Log.e("STRING_DATE", String.valueOf(newEdt.size()));

            for (int da = 0; da < newEdt.size(); da++) {
                AditionallLocalConvenyance = new JSONObject();
                Log.e("EditeXt_LEngth", String.valueOf(newEdt.size()));
                Log.e("EditeXt_LEngth", newEdt.get(da).getText().toString());
                Log.e("DYNAMIC_LIST", dynamicLabelList.get(da));
                try {
                    AditionallLocalConvenyance.put(dynamicLabelList.get(da), newEdt.get(da).getText().toString());
                    AditionallLocalConvenyance.put("total_amount", editLaFare.getText().toString());
                    AditionallLocalConvenyance.put("type", editTexts.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            AditionalLAarray.put(AditionallLocalConvenyance);

            Log.e("INSIDE_LOOP_int", AditionalLAarray.toString());
            Log.e("INSIDE_LOOP", AditionallLocalConvenyance.toString());

        }


    */
/*        JSONObject jjMain = new JSONObject();
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

            Log.v("printing_final", ja.toString());
            jjMain.put("sf", SF_code);
            jjMain.put("div", div);
            jjMain.put("date", dateTime);
            jjMain.put("dailyExpense", ja);
            ///////////////////  jjMain.put("AditionalAlowanceArray", AditionalAlowanceArray);

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

            Log.e("JSONOBJECT", jjMain.toString());*//*

        Call<ResponseBody> submit = apiInterface.saveDailyAllowance("jjMain.toString()");
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
*/


       /* } catch (Exception e) {
            Log.v("printing_exception_are", e.getMessage());
        }
*/
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
                captureFile();
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

    public void captureFile() {
        dialog.dismiss();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Uri outputFileUri = Uri.fromFile(new File(getExternalCacheDir().getPath(), "pickImageResult.jpeg"));
        outputFileUri = FileProvider.getUriForFile(TAClaimActivity.this, getApplicationContext().getPackageName() + ".provider", new File(getExternalCacheDir().getPath(), "pickImageResult" + System.currentTimeMillis() + ".jpeg"));
        Log.v("priniting_uri", outputFileUri.toString() + " output " + outputFileUri.getPath() + " raw_msg " + getExternalCacheDir().getPath());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, 2);
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

        } else if (type == 11) {
            modeTextView.setText(myDataset.get(position).getName());
            Log.d("JSON_VALUE", myDataset.get(position).getName());
        } else if (type == 8) {

            Log.d("PARENT_COUNT", String.valueOf(size));

            Log.e("IMAGE_ATTACH_next", String.valueOf(myDataset.get(position).getPho()));
            Integer editTextPosition = myDataset.get(position).getPho();

            View view = travelDynamicLoaction.getChildAt(editTextPosition);
            editText = (TextView) (view.findViewById(R.id.enter_mode));
            //  enterMode = view.findViewById(R.id.enter_mode);
            editText.setText(myDataset.get(position).getName());


        } else if (type == 80) {

            Log.d("PARENT_COUNT", String.valueOf(size));

            Log.e("IMAGE_ATTACH_next", String.valueOf(myDataset.get(position).getPho()));
            editTextPositionss = myDataset.get(position).getPho();

            View view = linlocalCon.getChildAt(editTextPositionss);
            editTexts = (TextView) (view.findViewById(R.id.local_enter_mode));
            //  enterMode = view.findViewById(R.id.enter_mode);
            lcAttach = (ImageView) (view.findViewById(R.id.la_attach_iamg));


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
            } else {
                lcAttach.setVisibility(View.GONE);
            }


        } else if (type == 90) {

            Log.d("PARENT_COUNT", String.valueOf(size));
            Log.e("IMAGE_ATTACH_next", String.valueOf(myDataset.get(position).getPho()));
            editTextPositionss = myDataset.get(position).getPho();
            View view = LinearOtherAllowance.getChildAt(editTextPositionss);
            oeEditext = (TextView) (view.findViewById(R.id.other_enter_mode));
            oeAttach = (ImageView) (childView.findViewById(R.id.oe_attach_img));

            Log.e("Attachment", myDataset.get(position).getAddress());
        /*    if (myDataset.get(position).getAddress().equals("1")) {
                oeAttach.setVisibility(View.VISIBLE);
            } else {
                oeAttach.setVisibility(View.GONE);
            }
*/


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
            } else {
                oeAttach.setVisibility(View.GONE);
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
/*

        LatLng sydney2 = new LatLng(13.0300, 80.2421);

  */
/*      BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.marker_icon);
        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Old washermenpet"));
        googleMap.addMarker(new MarkerOptions().position(sydney2).title("Marker in Nandanam"));
       *//*
         */
        /* googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*//*
         */
        /*
         *//*

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney2,15));
        // Zoom in, animating the camera.
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(9), 2000, null);


*/
      /*  LatLng loc = new LatLng(13.1148, 80.2872);

        Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(13.1148, 80.2872),
                        new LatLng(13.0300, 80.2421)));

        polyline1.setTag("A");
        polyline1.setColor(COLOR_ORANGE_ARGB);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,15));*/

    }




    /*Showing Map based on Map*/

    public void callMap(String date) {

        Log.v("Map_Date", date + " 00:00:00");
        date = date.replaceAll("^[\"']+|[\"']+$", "");

        Log.v("Map_Date", date);
  /*      DateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
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
                            lat = Double.parseDouble(strLat);
                            lon = Double.parseDouble(strLon);
                            LatLng loc = new LatLng(lat, lon);
                            builder.include(loc);
                            latLonArray.add(loc);
                        }

                        Polyline polyline1 = mGoogleMap.addPolyline(new PolylineOptions()
                                .clickable(true)
                                .addAll(latLonArray));

                        polyline1.setTag("A");
                        polyline1.setColor(COLOR_ORANGE_ARGB);

                        LatLngBounds bounds = builder.build();
                        LinearMap = findViewById(R.id.map);
                        int width = LinearMap.getWidth();
                        int height = LinearMap.getHeight();
                        int padding = 0; // offset from edges of the map 10% of screen
                        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,0));


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


    public void dynamicModeType(Integer poisition) {
        /*  Dynamicallowance.removeAllViews();*/
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
        /*  Dynamicallowance.removeAllViews();*/
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

                                    for (int l = 0; l <= additionArray.length(); l++) {
                                        JSONObject json_in = additionArray.getJSONObject(l);
                                        dynamicLabel = json_in.getString("Ad_Fld_Name");
                                        dynamicLabelList.add(dynamicLabel);
                                        Log.d("TAF_Exp_Nameffdd", additionArray.toString());
                                        Log.d("TAF_Ex", dynamicLabel);
                                        Log.d("TAF_Ex", dynamicLabelList.toString());
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
                                        allEds.add(edt);

                                        users.add(edt);


                                        Log.e("EDITTEX_SISE", "bb  " + String.valueOf(users.size()));
                                        Log.e("EDITTEX_SISE", "aa  " + String.valueOf(allEds.size()));
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
                                        allEds1.add(edt);

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
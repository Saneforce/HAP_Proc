package com.hap.checkinproc.Activity_Hap;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.maps.GoogleMap;
import com.hap.checkinproc.Activity.Util.SelectionModel;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TravelAllowanceForm extends AppCompatActivity implements View.OnClickListener, Master_Interface {
    String ModeType = "", StrModeValue = "";
    ArrayList<String> modeList;
    CardView cardModeList;
    TextView txtmodeType, txtModeValue;
    List<Common_Model> listOrderType = new ArrayList<>();
    Common_Model mCommon_model_spinner;
    CustomListViewDialog customDialog;
    LinearLayout Dynamicallowance;


    /**/


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

    List<Common_Model> modelRetailDetails = new ArrayList<>();
    List<Common_Model> modelTypeList = new ArrayList<>();

    LinearLayout linAddAllowance;
    LinearLayout TravelBike;
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

    Integer totalkm = 0, totalPersonalKm = 0;
    String exp_for = "";
    JSONObject json_o;
    String DateForAPi = "";
    GoogleMap mGoogleMap;
    EditText enterMode, enterFrom, enterTo, enterFare;
    TextView PersonalTextKM;
    Integer Pva, C = 0;
    TextView PersonalKiloMeter;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_travel_allowance_form);
        ModeType = String.valueOf(getIntent().getSerializableExtra("Type"));
        modeList = (ArrayList<String>) getIntent().getSerializableExtra("DaList");
        lay_row = findViewById(R.id.lay_row);
        Dynamicallowance = findViewById(R.id.lin_allowance_dynamic);
        String[] stockArr = new String[modeList.size()];
        stockArr = modeList.toArray(stockArr);

        for (String s : stockArr)
            System.out.println("modeList" + s);

        txtmodeType = findViewById(R.id.travel_mode_type);
        txtmodeType.setText(ModeType);

        /*Mode of card Allowance*/
        cardModeList = findViewById(R.id.card_allowance);
        cardModeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listOrderType.clear();
                OrderType();
            }
        });

        txtModeValue = findViewById(R.id.txt_allowance);



        /*CHECKING*/
        UserDetails = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SF_code = UserDetails.getString("Sfcode", "");
        div = UserDetails.getString("Divcode", "");
        State_Code = UserDetails.getString("State_Code", "");
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Log.e("TRAVEL_ALLOWANCE_FORM", "OnCreate");


    }


    public void OrderType() {

        for (int i = 0; i < modeList.size(); i++) {
            String name = modeList.get(i);
            mCommon_model_spinner = new Common_Model("id", name, "flag");
            listOrderType.add(mCommon_model_spinner);
        }
        customDialog = new CustomListViewDialog(TravelAllowanceForm.this, listOrderType, 1);
        Window window = customDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        customDialog.show();

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        customDialog.dismiss();
        if (type == 1) {
            StrModeValue = myDataset.get(position).getName();
            callApi(StrModeValue);
            txtModeValue.setText(StrModeValue);
        }
    }


    public void callApi(String sss) {

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
                @SuppressLint("ResourceType")
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

                            ArrayList<SelectionModel> arr = new ArrayList<>();
                            ArrayList<SelectionModel> arr1 = new ArrayList<>();

                            for (int i = 0; i < jsnArValue.length(); i++) {
                                JSONObject jsonHeaderObject = jsnArValue.getJSONObject(i);
                                String Exp_Name = jsonHeaderObject.getString("Name");


                                if (Exp_Name.equals(sss)) {
                                    Log.d("TAF", Exp_Name);
                                    Log.d("TAF", sss);
                                    Log.d("TAF", jsonHeaderObject.getString("user_enter"));
                                    Log.d("TAF", jsonHeaderObject.getString("Max_Allowance"));

                                    JSONArray additionArray = jsonHeaderObject.getJSONArray("value");

                                    Log.d("TAF_Exp_Name", additionArray.toString());
                                    Log.d("TAF_Exp_Name", Exp_Name);

                                    for (int l = 0; l <= additionArray.length(); l++) {
                                        JSONObject json_in = additionArray.getJSONObject(l);
                                        Log.d("TAF_Exp_Nameffdd", additionArray.toString());
                                        RelativeLayout childRel = new RelativeLayout(getApplicationContext());
                                        RelativeLayout.LayoutParams layoutparams_3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                        layoutparams_3.addRule(RelativeLayout.ALIGN_PARENT_END);
                                        layoutparams_3.setMargins(0, 10, 0, 0);
                                        EditText edt = new EditText(getApplicationContext());
                                        edt.setLayoutParams(layoutparams_3);
                                        edt.setEms(5);
                                        edt.setHint(json_in.getString("Ad_Fld_Name"));
                                        edt.setHintTextColor(Color.BLACK);
                                        edt.setId(12345);
                                        edt.setInputType(InputType.TYPE_CLASS_NUMBER);

                                        edt.setPadding(9, 9, 9, 9);
                                        childRel.addView(edt);
                                        // Add the new row before the add field button.
                                        Dynamicallowance.addView(childRel, Dynamicallowance.getChildCount() - 1);
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

    @SuppressLint("ResourceType")
    public void createDynamicViewForSingleRow(String headerValue, String name, ArrayList<SelectionModel> array, int position, String userenter, String attachment, String max) {

        Log.e("IN_IN_IN", "IN_IN_IN");
        RelativeLayout rl = new RelativeLayout(this);
        RelativeLayout.LayoutParams layoutparams_1 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        rl.setLayoutParams(layoutparams_1);



 /* RelativeLayout.LayoutParams header = new RelativeLayout.LayoutParams(
 RelativeLayout.LayoutParams.WRAP_CONTENT,
 RelativeLayout.LayoutParams.WRAP_CONTENT);
 TextView header_Txt = new TextView(this);
 header_Txt.setText(headerValue);
 Log.e("CREATE_DYNAMIC_VIEW", name);
 header_Txt.setLayoutParams(header);
 header_Txt.setTextSize(16f);
 header_Txt.setId(54321);
 header_Txt.setTextColor(Color.BLACK);
 rl.addView(header_Txt);
 */


        RelativeLayout.LayoutParams layoutparams_2 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        layoutparams_2.setMargins(5, 0, 0, 0);
        TextView txt = new TextView(this);
        txt.setText(name);
        Log.e("CREATE_DYNAMIC_VIEW", name);
        txt.setLayoutParams(layoutparams_2);
        txt.setTextSize(16f);
        txt.setId(54321);

        txt.setTextColor(Color.BLACK);
        rl.addView(txt);


        RelativeLayout.LayoutParams layoutparams_3 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutparams_3.addRule(RelativeLayout.ALIGN_PARENT_END);
        layoutparams_3.setMargins(0, 10, 0, 0);
        EditText edt = new EditText(this);
        edt.setLayoutParams(layoutparams_3);
        edt.setBackgroundResource(R.drawable.hash_border);
        edt.setEms(5);
        edt.setId(12345);
        edt.setInputType(InputType.TYPE_CLASS_NUMBER);
        /* if (userenter.equalsIgnoreCase("0")) {
         *//* edt.setEnabled(false);
 edt.setText(max);*//*
 } else if (userenter.equalsIgnoreCase("1")) {
 edt.setFilters(new InputFilter[]{new InputFilterMinMax("0", max)});
 }*/

       /* if (userenter.equalsIgnoreCase("1")) {
            edt.setFilters(new InputFilter[]{new InputFilterMinMax("0", max)});
        }*/
        edt.setPadding(9, 9, 9, 9);
        rl.addView(edt);
        RelativeLayout.LayoutParams layoutparams_4 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutparams_4.addRule(RelativeLayout.ALIGN_PARENT_END);
        layoutparams_4.addRule(RelativeLayout.CENTER_VERTICAL);
        layoutparams_4.setMargins(0, 0, 3, 0);
        ImageView img = new ImageView(this);
        img.setImageResource(R.drawable.attach_icon);
        img.setId(899);
        if (attachment.equalsIgnoreCase("1")) {
            img.setVisibility(View.VISIBLE);
        } else
            img.setVisibility(View.INVISIBLE);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos = position;
                //popupCapture(name);
            }
        });
        //layoutparams_3.addRule(RelativeLayout.CENTER_VERTICAL);
        // layoutparams_3.setMargins(0,8,0,0);
        img.setLayoutParams(layoutparams_4);
        rl.addView(img);

        RelativeLayout.LayoutParams ParamMaxLimit = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        ParamMaxLimit.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 12345);
        ParamMaxLimit.addRule(RelativeLayout.ALIGN_LEFT, 12345);
        ParamMaxLimit.setMarginStart(-60);
        TextView mtxtTextView = new TextView(this);
        mtxtTextView.setText((max == "0") ? "" : max);
        mtxtTextView.setLayoutParams(ParamMaxLimit);
        Log.e("Create_View_Dynamic", max);
        rl.addView(mtxtTextView);

        lay_row.addView(rl);

        if (array.get(0).getArray().size() != 0) {
            LinearLayout r2 = new LinearLayout(this);
            LinearLayout.LayoutParams params_2 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            r2.setOrientation(LinearLayout.VERTICAL);
            r2.setLayoutParams(params_2);
            r2.setId(rlayCount);
            array.get(0).getArray().get(0).setTmp_url(String.valueOf(rlayCount));
            array.get(0).setTxt(String.valueOf(cardViewCount));
            rlayCount = rlayCount + 1;
            ArrayList<Integer> cardCountt = new ArrayList<>();
            // r2.addView(generateView(0, array.get(0).getArray(), array, 0, cardCountt));
            lay_row.addView(r2);
        } else {
            View vv = new View(this);
            vv.setBackgroundColor(Color.BLACK);
            vv.setLayoutParams(layoutparams_1);
            lay_row.addView(vv);
        }
    }
}
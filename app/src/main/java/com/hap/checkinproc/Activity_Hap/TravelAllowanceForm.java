package com.hap.checkinproc.Activity_Hap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.hap.checkinproc.Activity.TAClaimActivity;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TravelAllowanceForm extends AppCompatActivity implements View.OnClickListener, Master_Interface {
    String ModeType = "", StrModeValue = "", SF_code = "", div = "", State_Code = "", dynamicLabel = "";
    ArrayList<String> modeList, dynamicLabelList;

    ArrayList<String> cccccccc = new ArrayList<String>();
    CardView cardModeList;
    TextView txtmodeType, txtModeValue;
    List<Common_Model> listOrderType = new ArrayList<>();
    Common_Model mCommon_model_spinner;
    CustomListViewDialog customDialog;
    LinearLayout Dynamicallowance;
    EditText enterFareAmount, edt;
    Button DynamicSave;
    List<EditText> allEds = new ArrayList<EditText>();
    ApiInterface apiInterface;
    SharedPreferences UserDetails;
    public static final String MyPREFERENCES = "MyPrefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_travel_allowance_form);

        ModeType = String.valueOf(getIntent().getSerializableExtra("Type"));
        modeList = (ArrayList<String>) getIntent().getSerializableExtra("DaList");
        Log.e("AllowanceType", ModeType);

        Dynamicallowance = findViewById(R.id.lin_allowance_dynamic);
        DynamicSave = findViewById(R.id.btn_dynmc_save);
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
                dynamicModeType();
            }
        });

        txtModeValue = findViewById(R.id.txt_allowance);
        enterFareAmount = findViewById(R.id.enter_fare_amount);
        dynamicLabelList = new ArrayList<>();


        /*CHECKING*/
        UserDetails = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SF_code = UserDetails.getString("Sfcode", "");
        div = UserDetails.getString("Divcode", "");
        State_Code = UserDetails.getString("State_Code", "");
        apiInterface = ApiClient.getClient().create(ApiInterface.class);


        DynamicSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject AditionalTravelallowance = new JSONObject();
                for (int da = 0; da < allEds.size(); da++) {
                    if (ModeType.equals("Daily Allowance")) {

                        try {
                            AditionalTravelallowance.put(dynamicLabelList.get(da), allEds.get(da).getText().toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e("CHECKING_SIZE", ModeType + "   " + allEds.get(da).getText().toString());
                    } else if (ModeType.equals("Other Expense")) {
                        try {
                            AditionalTravelallowance.put(dynamicLabelList.get(da), allEds.get(da).getText().toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e("CHECKING_SIZE", ModeType + "   " + allEds.get(da).getText().toString());

                    } else {
                        try {
                            AditionalTravelallowance.put(dynamicLabelList.get(da), allEds.get(da).getText().toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e("CHECKING_SIZE", ModeType + "   " + allEds.get(da).getText().toString());

                    }
                }


                try {
                    AditionalTravelallowance.put("total_amount", enterFareAmount.getText().toString());
                    AditionalTravelallowance.put("type", ModeType);
                    AditionalTravelallowance.put("allowance", StrModeValue);

                    cccccccc.add(AditionalTravelallowance.toString());
                    Log.d("KARTHIC_ARRAY_List", cccccccc.toString());
                    Log.d("KARTHIC_ARRAY_OBJECT", String.valueOf(AditionalTravelallowance.length()));


                    Intent totalAllowance = new Intent(TravelAllowanceForm.this, TAClaimActivity.class);
                    totalAllowance.putExtra("Retrive_Type", ModeType);
                    totalAllowance.putExtra("Retrive_Ta_List", AditionalTravelallowance.toString());
                    startActivity(totalAllowance);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }


    public void dynamicModeType() {
        Dynamicallowance.removeAllViews();
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
        StrModeValue = myDataset.get(position).getName();
        callApi(StrModeValue);
        txtModeValue.setText(StrModeValue);
        String fareAmount = "Enter " + StrModeValue + " Amount";
        Log.e("FARE_AMOUNT", fareAmount);
        enterFareAmount.setHint(fareAmount);
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
                                        layoutparams_3.addRule(RelativeLayout.ALIGN_PARENT_END);
                                        layoutparams_3.setMargins(0, 10, 0, 0);
                                        edt = new EditText(getApplicationContext());
                                        edt.setLayoutParams(layoutparams_3);
                                        for (int da = 0; da < dynamicLabelList.size(); da++) {
                                            edt.setHint(dynamicLabelList.get(da));
                                            Log.e("DYNAMICE_LABEL_LIST", dynamicLabelList.get(da).toString());
                                        }
                                        edt.setId(12345);
                                        edt.setTextSize(13);
                                        edt.setTextColor(R.color.grey_500);
                                        edt.setBackgroundResource(R.drawable.textbox_bg);
                                        edt.setPadding(9, 9, 9, 9);

                                        childRel.addView(edt);
                                        allEds.add(edt);
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

}
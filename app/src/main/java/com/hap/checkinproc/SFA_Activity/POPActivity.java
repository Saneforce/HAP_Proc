package com.hap.checkinproc.SFA_Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.hap.checkinproc.Activity_Hap.CustomListViewDialog;
import com.hap.checkinproc.Common_Class.AlertDialogBox;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AlertBox;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.PopAddAdapter;
import com.hap.checkinproc.SFA_Adapter.QPSAdapter;
import com.hap.checkinproc.SFA_Adapter.QPS_Modal;
import com.hap.checkinproc.SFA_Model_Class.Product_Details_Modal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class POPActivity extends AppCompatActivity implements View.OnClickListener, Master_Interface {

    TextView tvViewStatus;
    Button btnSubmit;
    TextView tvOrder, tvOtherBrand, tvQPS, tvCoolerInfo;

    RecyclerView rvQps;

    QPSAdapter qpsAdapter;
    ArrayList<QPS_Modal> qpsModals = new ArrayList<>();
    Common_Class common_class;

    RecyclerView rvPopAdd;

    PopAddAdapter popAddAdapter;

    TextView tvAdd;

    List<Product_Details_Modal> popAddList = new ArrayList<>();
    private ArrayList<Common_Model> popMaterialList = new ArrayList<>();
    private CustomListViewDialog customDialog;

    public static POPActivity popActivity;
    private int selectedPos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_layout);
        common_class = new Common_Class(this);
        popActivity = this;

        btnSubmit = findViewById(R.id.btnSubmit);
        tvViewStatus = findViewById(R.id.tvPOPViewStatus);

        tvOrder = (TextView) findViewById(R.id.tvOrder);
        tvQPS = (TextView) findViewById(R.id.tvQPS);
        tvOtherBrand = (TextView) findViewById(R.id.tvOtherBrand);
        tvCoolerInfo = (TextView) findViewById(R.id.tvCoolerInfo);
        rvQps = (RecyclerView) findViewById(R.id.rvPOP);
        rvPopAdd = (RecyclerView) findViewById(R.id.rvPOPAdd);
        tvAdd = (TextView) findViewById(R.id.tvAddPOP);

        TextView tvRetailorName = findViewById(R.id.Category_Nametext);
        TextView tvRetailorCode = findViewById(R.id.retailorCode);
        Shared_Common_Pref shared_common_pref = new Shared_Common_Pref(this);

        tvRetailorName.setText(Shared_Common_Pref.OutletName);
        tvRetailorCode.setText(shared_common_pref.getvalue(Constants.Retailor_ERP_Code));


        tvOrder.setOnClickListener(this);
        tvOtherBrand.setOnClickListener(this);
        tvQPS.setOnClickListener(this);
        tvCoolerInfo.setOnClickListener(this);
        tvAdd.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        tvViewStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvViewStatus.setVisibility(View.GONE);
                findViewById(R.id.llPOPStatus).setVisibility(View.GONE);
                findViewById(R.id.llPOPRequestStatus).setVisibility(View.VISIBLE);
                tvAdd.setVisibility(View.GONE);
                btnSubmit.setText("Completed");

            }
        });


        findViewById(R.id.tvPOP).setVisibility(View.GONE);


        qpsModals.add(new QPS_Modal("233", "236763", "Cooker", "30.8.2021", "-1 day", "10.9.2021"));
        qpsModals.add(new QPS_Modal("234", "236745", "Mobile", "25.8.2021", "-5 days", "10.9.2021"));

        qpsModals.add(new QPS_Modal("235", "236789", "Bag", "28.8.2021", "-3 days", "10.9.2021"));
        qpsAdapter = new QPSAdapter(this, qpsModals);
        rvQps.setAdapter(qpsAdapter);


        popAddList.add(new Product_Details_Modal("", "", "", 0, ""));

        popAddAdapter = new PopAddAdapter(popAddList, R.layout.popup_add_recyclerview,
                this);

        rvPopAdd.setAdapter(popAddAdapter);


        ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
        common_class.gotoHomeScreen(this, ivToolbarHome);


        if (common_class.isNetworkAvailable(this))
            getPOPMasterData();
        else
            common_class.showMsg(this, "Please check your internet connection.");

    }

    private void getPOPMasterData() {
        try {
            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

            JSONObject HeadItem = new JSONObject();
            HeadItem.put("divisionCode", Shared_Common_Pref.Div_Code);
            HeadItem.put("sfCode", Shared_Common_Pref.Sf_Code);
            HeadItem.put("retailorCode", Shared_Common_Pref.OutletCode);

            HeadItem.put("distributorcode", Shared_Common_Pref.DistributorCode);


            Call<ResponseBody> call = service.getPOPMaster(HeadItem.toString());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    InputStreamReader ip = null;
                    StringBuilder is = new StringBuilder();
                    String line = null;
                    try {
                        if (response.isSuccessful()) {
                            ip = new InputStreamReader(response.body().byteStream());
                            BufferedReader bf = new BufferedReader(ip);
                            while ((line = bf.readLine()) != null) {
                                is.append(line);
                                Log.v("Res>>", is.toString());
                            }


                            JSONObject jsonObject = new JSONObject(is.toString());

                            JSONArray jsonArray = jsonObject.getJSONArray("Data");

                            popMaterialList.clear();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                popMaterialList.add(new Common_Model(jsonObject1.getString("POP_Code"), jsonObject1.getString("POP_Name"),
                                        jsonObject1.getString("POP_UOM")));
                            }


                        }

                    } catch (Exception e) {

                        Log.v("fail>>1", e.getMessage());

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.v("fail>>2", t.toString());


                }
            });
        } catch (Exception e) {
            Log.v("fail>>", e.getMessage());


        }
    }

    @Override
    public void onClick(View v) {
        Common_Class common_class = new Common_Class(this);
        switch (v.getId()) {
            case R.id.tvOrder:
                common_class.CommonIntentwithFinish(Order_Category_Select.class);
                break;
            case R.id.tvOtherBrand:
                common_class.CommonIntentwithFinish(OtherBrandActivity.class);
                break;
            case R.id.tvQPS:
                common_class.CommonIntentwithFinish(QPSActivity.class);
                break;
            case R.id.tvCoolerInfo:
                common_class.CommonIntentwithFinish(CoolerInfoActivity.class);
                break;
            case R.id.tvAddPOP:
                popAddList.add(new Product_Details_Modal("", "", "", 0, ""));
                popAddAdapter.notifyData(popAddList);

                break;
            case R.id.btnSubmit:
                SaveOrder();
                break;
        }
    }


    private void SaveOrder() {

        List<Product_Details_Modal> submitPOPList = new ArrayList<>();

        submitPOPList.clear();

        for (int i = 0; i < popAddList.size(); i++) {
            if (!popAddList.get(i).getName().equals("") && !popAddList.get(i).getBookingDate().equals("") && popAddList.get(i).getQty() > 0 &&
                    !popAddList.get(i).getUOM().equals("")) {
                submitPOPList.add(popAddList.get(i));

            }
        }


        if (submitPOPList.size() > 0) {
            if (common_class.isNetworkAvailable(this)) {

                AlertDialogBox.showDialog(POPActivity.this, "HAP SFA", "Are You Sure Want to Submit?", "OK", "Cancel", false, new AlertBox() {
                    @Override
                    public void PositiveMethod(DialogInterface dialog, int id) {

                        JSONArray data = new JSONArray();
                        JSONObject ActivityData = new JSONObject();

                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Calendar calobj = Calendar.getInstance();
                        String dateTime = df.format(calobj.getTime());

                        try {
                            JSONObject HeadItem = new JSONObject();
                            HeadItem.put("SF", Shared_Common_Pref.Sf_Code);
                            HeadItem.put("divCode", Shared_Common_Pref.Div_Code);
                            HeadItem.put("CustCode", Shared_Common_Pref.OutletCode);
                            HeadItem.put("CustName", Shared_Common_Pref.OutletName);
                            HeadItem.put("StkCode", Shared_Common_Pref.DistributorCode);
                            HeadItem.put("Datetime", dateTime);
                            ActivityData.put("Json_Head", HeadItem);


                            JSONArray Order_Details = new JSONArray();
                            for (int z = 0; z < submitPOPList.size(); z++) {
                                JSONObject ProdItem = new JSONObject();
                                ProdItem.put("id", submitPOPList.get(z).getId());
                                ProdItem.put("material", submitPOPList.get(z).getName());
                                ProdItem.put("date", submitPOPList.get(z).getBookingDate());
                                ProdItem.put("Qty", submitPOPList.get(z).getQty());
                                ProdItem.put("uom", submitPOPList.get(z).getUOM());

                                Order_Details.put(ProdItem);
                            }
                            ActivityData.put("Entry_Details", Order_Details);
                            data.put(ActivityData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                        Call<JsonObject> responseBodyCall = apiInterface.savePOP(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, data.toString());
                        responseBodyCall.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                if (response.isSuccessful()) {
                                    try {
                                        Log.e("JSON_VALUES", response.body().toString());
                                        JSONObject jsonObjects = new JSONObject(response.body().toString());
                                        String san = jsonObjects.getString("success");
                                        Log.e("Success_Message", san);
                                        if (san.equals("true")) {
                                            Toast.makeText(POPActivity.this, "Submitted Successfully", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(), Invoice_History.class));
                                            finish();
                                        }

                                    } catch (Exception e) {

                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                Log.e("SUBMIT_VALUE", "ERROR");
                            }
                        });

                    }

                    @Override
                    public void NegativeMethod(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
            } else {
                Toast.makeText(this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
            }
        } else {
            common_class.showMsg(this, "POP is empty");
        }
    }

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        if (selectedPos >= 0) {
            customDialog.dismiss();
            popAddList.set(selectedPos, new Product_Details_Modal(myDataset.get(position).getId(), myDataset.get(position).getName(),
                    "", 0, myDataset.get(position).getFlag()));
            popAddAdapter.notifyData(popAddList);

        }
    }

    public void showBrandDialog(int position) {
        selectedPos = position;
        customDialog = new CustomListViewDialog(this, popMaterialList, 1);
        Window windoww = customDialog.getWindow();
        windoww.setGravity(Gravity.CENTER);
        windoww.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        customDialog.show();
    }
}
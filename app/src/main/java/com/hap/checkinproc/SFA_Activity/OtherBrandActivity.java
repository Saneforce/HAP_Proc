package com.hap.checkinproc.SFA_Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
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
import com.hap.checkinproc.SFA_Model_Class.Product_Details_Modal;
import com.hap.checkinproc.common.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtherBrandActivity extends AppCompatActivity implements View.OnClickListener, Master_Interface {

    List<Product_Details_Modal> Getorder_Array_List;
    TextView tvOrder, tvQPS, tvPOP, tvCoolerInfo, tvAddBrand;
    private List<Product_Details_Modal> GetPurchaseOrderList;
    public static OtherBrandActivity otherBrandActivity;
    OtherBrandAdapter otherBrandAdapter;
    private List<Common_Model> otherBrandList = new ArrayList<>();
    CustomListViewDialog customDialog;
    private int selectedPos = -1;
    private Type userTypeCompetitor;
    private TextView tvSubmit;
    Common_Class common_class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_brand_layout);
        otherBrandActivity = this;

        findViewById(R.id.tvOtherBrand).setVisibility(View.GONE);


        common_class = new Common_Class(this);
        common_class.getDataFromApi(Constants.Competitor_List, this, false);


        RecyclerView recyclerView = findViewById(R.id.orderrecyclerview);

        tvOrder = (TextView) findViewById(R.id.tvOrder);
        tvPOP = (TextView) findViewById(R.id.tvPOP);
        tvQPS = (TextView) findViewById(R.id.tvQPS);
        tvCoolerInfo = (TextView) findViewById(R.id.tvCoolerInfo);
        tvAddBrand = (TextView) findViewById(R.id.tvAddBrand);
        tvSubmit = (TextView) findViewById(R.id.btnSubmit);

        TextView tvRetailorName = findViewById(R.id.Category_Nametext);
        TextView tvRetailorCode = findViewById(R.id.retailorCode);
        Shared_Common_Pref shared_common_pref = new Shared_Common_Pref(this);

        tvRetailorName.setText(Shared_Common_Pref.OutletName);
        tvRetailorCode.setText(shared_common_pref.getvalue(Constants.Retailor_ERP_Code));


        tvOrder.setOnClickListener(this);
        tvQPS.setOnClickListener(this);
        tvPOP.setOnClickListener(this);
        tvCoolerInfo.setOnClickListener(this);
        tvAddBrand.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);


        Getorder_Array_List = new ArrayList<>();

        DatabaseHandler db = new DatabaseHandler(OtherBrandActivity.otherBrandActivity);
        String Compititor_List = String.valueOf(db.getMasterData(Constants.Competitor_List));
        Gson gson = new Gson();
        userTypeCompetitor = new TypeToken<ArrayList<Common_Model>>() {
        }.getType();
        otherBrandList = gson.fromJson(Compititor_List, userTypeCompetitor);


        Getorder_Array_List.add(new Product_Details_Modal("", "Select the Other Brand", "", 0, 0, 0, ""));

        otherBrandAdapter = new OtherBrandAdapter(Getorder_Array_List, R.layout.other_brand_order_recyclerview,
                this);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(otherBrandAdapter);
        ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
        common_class.gotoHomeScreen(this, ivToolbarHome);
    }

    @Override
    public void onClick(View v) {
        Common_Class common_class = new Common_Class(this);
        switch (v.getId()) {
            case R.id.tvOrder:
                common_class.CommonIntentwithFinish(Order_Category_Select.class);
                break;
            case R.id.tvQPS:
                common_class.CommonIntentwithFinish(QPSActivity.class);
                break;
            case R.id.tvPOP:
                common_class.CommonIntentwithFinish(POPActivity.class);
                break;
            case R.id.tvCoolerInfo:
                common_class.CommonIntentwithFinish(CoolerInfoActivity.class);
                break;

            case R.id.tvAddBrand:
                Getorder_Array_List.add(new Product_Details_Modal("", "Select the Other Brand", "", 0, 0, 0, ""));
                otherBrandAdapter.notifyData(Getorder_Array_List);
                break;
            case R.id.btnSubmit:
                SaveOrder();
                break;
        }
    }

    private void SaveOrder() {
        List<Product_Details_Modal> submitBrandList = new ArrayList<>();

        submitBrandList.clear();

        for (int i = 0; i < Getorder_Array_List.size(); i++) {
            if (!Getorder_Array_List.get(i).getName().equals("") && !Getorder_Array_List.get(i).getSku().equals("") &&
                    Getorder_Array_List.get(i).getAmount() > 0 &&
                    !Getorder_Array_List.get(i).getScheme().equals("")) {
                submitBrandList.add(Getorder_Array_List.get(i));

            }
        }


        if (submitBrandList.size() > 0) {


            if (common_class.isNetworkAvailable(this)) {

                AlertDialogBox.showDialog(OtherBrandActivity.this, "HAP SFA", "Are You Sure Want to Submit?", "OK", "Cancel", false, new AlertBox() {
                    @Override
                    public void PositiveMethod(DialogInterface dialog, int id) {

                        JSONArray data = new JSONArray();
                        JSONObject ActivityData = new JSONObject();

                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
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
                            for (int z = 0; z < Getorder_Array_List.size(); z++) {
                                JSONObject ProdItem = new JSONObject();
                                ProdItem.put("id", submitBrandList.get(z).getId());
                                ProdItem.put("BrandNm", submitBrandList.get(z).getName());
                                ProdItem.put("BrandSKU", submitBrandList.get(z).getSku());
                                ProdItem.put("Qty", submitBrandList.get(z).getQty());
                                //  ProdItem.put("Product_RegularQty", Getorder_Array_List.get(z).getRegularQty());
                                ProdItem.put("Price", submitBrandList.get(z).getPrice());
                                ProdItem.put("Amt", submitBrandList.get(z).getAmount());
                                ProdItem.put("Sch", submitBrandList.get(z).getScheme());
                                Order_Details.put(ProdItem);
                            }
                            ActivityData.put("Entry_Details", Order_Details);
                            data.put(ActivityData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                        Call<JsonObject> responseBodyCall = apiInterface.saveOtherBrand(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, data.toString());
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
                                            Toast.makeText(OtherBrandActivity.this, "Other brand Submitted Successfully", Toast.LENGTH_SHORT).show();
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
            common_class.showMsg(this, "Other Brand is empty");

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            common_class.CommonIntentwithFinish(Invoice_History.class);

            return true;
        }
        return false;
    }

    public void showBrandDialog(int position) {

        selectedPos = position;

        customDialog = new CustomListViewDialog(this, otherBrandList, 1);
        Window windoww = customDialog.getWindow();
        windoww.setGravity(Gravity.CENTER);
        windoww.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        customDialog.show();

    }

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        if (selectedPos >= 0) {
            customDialog.dismiss();
            Getorder_Array_List.set(selectedPos, new Product_Details_Modal(myDataset.get(position).getId(), myDataset.get(position).getName(), "", 0, 0, 0, ""));
            otherBrandAdapter.notifyData(Getorder_Array_List);

        }
    }


//    public void notifyData(JSONArray array) {
//        this.array = array;
//        notifyDataSetChanged();
//    }


//    public void updateToTALITEMUI() {
//        TextView tvTotalItems = findViewById(R.id.tvTotalItems);
//        TextView tvTotalAmount = findViewById(R.id.tvTotalAmount);
//
//
//
//
//        GetPurchaseOrderList = new ArrayList<>();
//        GetPurchaseOrderList.clear();
//        int totalvalues = 0, totalQty = 0;
//
//        for (int i = 0; i < Getorder_Array_List.size(); i++) {
//
//            Product_Details_Modal pm = Getorder_Array_List.get(i);
//            if (pm.getAmount() > 0) {
//                GetPurchaseOrderList.add(pm);
//                totalvalues += pm.getAmount();
//                totalQty += pm.getQty();
//
//            }
//        }
//
//        tvTotalAmount.setText("₹ " + totalvalues);
//        tvTotalItems.setText("Items : " + GetPurchaseOrderList.size());
//
//
//
//    }

}
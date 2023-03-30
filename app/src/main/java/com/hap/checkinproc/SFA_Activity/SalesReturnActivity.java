package com.hap.checkinproc.SFA_Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.SalesReturnInvoiceAdapter;
import com.hap.checkinproc.SFA_Adapter.SalesReturnProductAdapter;
import com.hap.checkinproc.SFA_Model_Class.SalesReturnInvoiceModel;
import com.hap.checkinproc.SFA_Model_Class.SalesReturnProductModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesReturnActivity extends AppCompatActivity {

    ImageView home;
    TextView todayDate, outletName, outletAddress, selectedInvoice;
    RecyclerView recyclerView;
    RelativeLayout rlInvoice;

    Context context;
    Common_Class common_class;
    ArrayList<SalesReturnInvoiceModel> invoiceList;
    ArrayList<SalesReturnProductModel> productList;

    String tag = "fkufhjhrk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_return);

        home = findViewById(R.id.toolbar_home);
        todayDate = findViewById(R.id.tv_date);
        outletName = findViewById(R.id.outletName);
        outletAddress = findViewById(R.id.outletAddress);
        selectedInvoice = findViewById(R.id.invoice_status);
        recyclerView = findViewById(R.id.rv_products_list);
        rlInvoice = findViewById(R.id.rl_reference_invoice_number);

        context = this;
        common_class = new Common_Class(context);
        invoiceList = new ArrayList<>();
        productList = new ArrayList<>();

        common_class.gotoHomeScreen(context, home);
        todayDate.setText("Date: " + new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()));
        outletName.setText(Shared_Common_Pref.OutletName.toUpperCase());
        outletAddress.setText(Shared_Common_Pref.OutletAddress);

        rlInvoice.setEnabled(false);
        rlInvoice.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.custom_layout_complementary_invoice, null, false);
            builder.setView(view);
            builder.setCancelable(false);
            RecyclerView recyclerView1 = view.findViewById(R.id.recyclerView_complementary_invoice);
            recyclerView1.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            SalesReturnInvoiceAdapter adapter = new SalesReturnInvoiceAdapter(context, invoiceList);
            recyclerView1.setAdapter(adapter);
            TextView close = view.findViewById(R.id.close);
            AlertDialog dialog = builder.create();

            adapter.setItemSelect(invoiceNumber -> {
                dialog.dismiss();
                selectedInvoice.setText(invoiceNumber);
            });

            close.setOnClickListener(v1 -> dialog.dismiss());
            if (invoiceList.size() > 0) {
                dialog.show();
            } else {
                Toast.makeText(context, "No Invoices made in last 30 days", Toast.LENGTH_SHORT).show();
            }
        });

        selectedInvoice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                LoadProductDetails(s.toString());
            }
        });

        getCustomerInvoices();
    }

    private void LoadProductDetails(String invoice) {
        productList.clear();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Map<String, String> params = new HashMap<>();
        params.put("axn", "get_product_list_for_invoice");
        params.put("invoice", invoice);
        Call<ResponseBody> call = apiInterface.getUniversalData(params);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response.body() == null) {
                            Toast.makeText(context, "Response is Null", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getBoolean("success")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            Log.e("status", "Request Result: \n" + jsonArray);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String productCode = jsonArray.getJSONObject(i).getString("Product_Code");
                                String Product_Name = jsonArray.getJSONObject(i).getString("Product_Name");
                                String materialCode = jsonArray.getJSONObject(i).getString("Sale_Erp_Code");
                                String MRP = jsonArray.getJSONObject(i).getString("MRP");
                                String Price = jsonArray.getJSONObject(i).getString("Price");
                                String qty = jsonArray.getJSONObject(i).getString("qty");
                                String Unit = jsonArray.getJSONObject(i).getString("Unit");
                                String Con_Fac = jsonArray.getJSONObject(i).getString("Con_Fac");
                                productList.add(new SalesReturnProductModel(Product_Name, materialCode, MRP, Price, Unit, qty, "UNT", "0", "Select Return Type"));
                            }
                            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                            SalesReturnProductAdapter adapter = new SalesReturnProductAdapter(context, productList);
                            recyclerView.setAdapter(adapter);

                        } else {
                            Toast.makeText(context, "Request does not reached the server", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, "Error while parsing response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Response Not Success", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(context, "Response Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCustomerInvoices() {
        invoiceList.clear();
        String outletCode = Shared_Common_Pref.OutletCode;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Map<String, String> params = new HashMap<>();
        params.put("axn", "get_latest_invoice_of_outlet");
        params.put("outletCode", outletCode);
        Call<ResponseBody> call = apiInterface.getUniversalData(params);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response.body() == null) {
                            Toast.makeText(context, "Response is Null", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getBoolean("success")) {
                            JSONArray array = jsonObject.getJSONArray("response");
                            Log.e("invoiceList", array.toString());
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                String invoiceNumber = object.getString("Trans_Inv_Slno");
                                String date = object.getJSONObject("Invoice_Date").getString("date");
                                String value = object.getString("Total");
                                invoiceList.add(new SalesReturnInvoiceModel(invoiceNumber, date, value));
                            }
                            rlInvoice.setEnabled(true);
                        } else {
                            Toast.makeText(context, "Request does not reached the server", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, "Error while parsing response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Response Not Success", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(context, "Response Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
package com.hap.checkinproc.SFA_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Activity_Hap.AddNewRetailer;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.ViewReport;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.Invoice_History_Adapter;
import com.hap.checkinproc.SFA_Adapter.Outlet_Info_Adapter;
import com.hap.checkinproc.SFA_Adapter.Outlet_Report_View_Adapter;
import com.hap.checkinproc.SFA_Model_Class.OutletReport_View_Modal;
import com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Invoice_History extends AppCompatActivity implements View.OnClickListener {
    TextView outlet_name, lastinvoice;
    LinearLayout lin_order, lin_repeat_order, lin_invoice, lin_repeat_invoice;
    Common_Class common_class;
    List<OutletReport_View_Modal> OutletReport_View_Modal;
    List<OutletReport_View_Modal> FilterOrderList = new ArrayList<>();
    Type userType;
    Gson gson;
    Invoice_History_Adapter mReportViewAdapter;
    RecyclerView invoicerecyclerview;
    Shared_Common_Pref sharedCommonPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice__history);
        gson = new Gson();
        sharedCommonPref = new Shared_Common_Pref(Invoice_History.this);
        common_class = new Common_Class(this);
        lin_order = findViewById(R.id.lin_order);
        outlet_name = findViewById(R.id.outlet_name);
        outlet_name.setText(Shared_Common_Pref.OutletName);
        lin_repeat_order = findViewById(R.id.lin_repeat_order);
        lin_invoice = findViewById(R.id.lin_invoice);
        lin_repeat_invoice = findViewById(R.id.lin_repeat_invoice);
        lin_order.setOnClickListener(this);
        invoicerecyclerview = (RecyclerView) findViewById(R.id.invoicerecyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        invoicerecyclerview.setLayoutManager(layoutManager);
       String DCRMode=sharedCommonPref.getvalue(Shared_Common_Pref.DCRMode);
        lin_invoice.setVisibility(View.VISIBLE);
        if(!DCRMode.equalsIgnoreCase("")){
            lin_invoice.setVisibility(View.GONE);
        }
        String OrdersTable = sharedCommonPref.getvalue(Shared_Common_Pref.GetTodayOrder_List);
        userType = new TypeToken<ArrayList<OutletReport_View_Modal>>() {
        }.getType();
        OutletReport_View_Modal = gson.fromJson(OrdersTable, userType);
        System.out.println("Array_List_Size" + OrdersTable.toString());
        System.out.println("Array_List_Sizee" + OutletReport_View_Modal.size());
        System.out.println("Array_List_Outlet_Code" + Shared_Common_Pref.OutletCode);
        if (OutletReport_View_Modal != null && OutletReport_View_Modal.size() > 0) {
            for (OutletReport_View_Modal filterlist : OutletReport_View_Modal) {
                if (filterlist.getOutletCode().equals(Shared_Common_Pref.OutletCode)) {
                    FilterOrderList.add(filterlist);
                }
            }
        }
        System.out.println("LocalOrderValues" + OutletReport_View_Modal.toString());
        mReportViewAdapter = new Invoice_History_Adapter(Invoice_History.this, FilterOrderList, new AdapterOnClick() {
            @Override
            public void onIntentClick(int position) {
                Log.e("TRANS_SLNO", FilterOrderList.get(position).getTransSlNo());
                Shared_Common_Pref.TransSlNo = FilterOrderList.get(position).getTransSlNo();
                Shared_Common_Pref.Invoicetoorder = "1";
                if (FilterOrderList.get(position).getStatus().equals("ORDER")) {
                    Intent intent = new Intent(getBaseContext(), Order_Category_Select.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getBaseContext(), Print_Invoice_Activity.class);
                    Log.e("Sub_Total", String.valueOf(FilterOrderList.get(position).getOrderValue() + ""));
                    intent.putExtra("Order_Values", FilterOrderList.get(position).getOrderValue() + "");
                    intent.putExtra("Invoice_Values", FilterOrderList.get(position).getInvoicevalues());
                    intent.putExtra("No_Of_Items", FilterOrderList.get(position).getNo_Of_items());
                    intent.putExtra("Invoice_Date", FilterOrderList.get(position).getOrderDate());
                    intent.putExtra("NetAmount", FilterOrderList.get(position).getNetAmount());
                    intent.putExtra("Discount_Amount", FilterOrderList.get(position).getDiscount_Amount());
                    startActivity(intent);
                }

            }
        });
        invoicerecyclerview.setAdapter(mReportViewAdapter);
        lin_invoice.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_order:
                Shared_Common_Pref.Invoicetoorder = "0";
                //Shared_Common_Pref.TransSlNo = "0";
                common_class.CommonIntentwithFinish(Order_Category_Select.class);
                break;
            case R.id.lin_repeat_order:
                break;
            case R.id.lin_invoice:
                Shared_Common_Pref.Invoicetoorder = "2";
                common_class.CommonIntentwithFinish(Order_Category_Select.class);
                break;
            case R.id.lin_repeat_invoice:
                break;
        }
    }




 /*   public void ViewDateReport() {
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "table/list");
        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code.replace(",", ""));
        QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
        QueryString.put("fromdate", Common_Class.GetDateOnly());
        QueryString.put("todate", Common_Class.GetDateOnly());
        QueryString.put("Outlet_Code", Shared_Common_Pref.OutletCode);
        Log.e("Report_ValuesMap", QueryString.toString());
        Call<Object> call = service.GetRouteObject(QueryString, "{\"tableName\":\"GetOutletViewReport\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}");
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e("MAster_Product_Details", response.body() + "");
                System.out.println("GetOutletView" + new Gson().toJson(response.body()));
                userType = new TypeToken<ArrayList<OutletReport_View_Modal>>() {
                }.getType();
                OutletReport_View_Modal = gson.fromJson(new Gson().toJson(response.body()), userType);
                if (OutletReport_View_Modal.size() == 0) {
                    Toast.makeText(Invoice_History.this, "Order Not Available!", Toast.LENGTH_SHORT).show();
                }

                System.out.println("Product_Details_Size" + OutletReport_View_Modal.size());
                mReportViewAdapter = new Outlet_Report_View_Adapter(Invoice_History.this, OutletReport_View_Modal, new ViewReport() {
                    @Override
                    public void reportCliick(String productId, String orderDate) {
                        Intent intnet = new Intent(Invoice_History.this, Outet_Report_Details.class);
                        intnet.putExtra("Order_ID", productId);
                        intnet.putExtra("OrderDate", orderDate);
                        startActivity(intnet);
                    }
                });

                invoicerecyclerview.setAdapter(mReportViewAdapter);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

    }*/
}
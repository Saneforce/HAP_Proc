package com.hap.checkinproc.SFA_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.Invoice_History_Adapter;
import com.hap.checkinproc.SFA_Adapter.Print_Invoice_Adapter;
import com.hap.checkinproc.SFA_Model_Class.Product_Details_Modal;
import com.hap.checkinproc.SFA_Model_Class.Trans_Order_Details_Offline;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Print_Invoice_Activity extends AppCompatActivity implements View.OnClickListener {
    Print_Invoice_Adapter mReportViewAdapter;
    RecyclerView printrecyclerview;
    Shared_Common_Pref sharedCommonPref;
    Type userType;
    Gson gson;
    Common_Class common_class;
    List<Trans_Order_Details_Offline> InvoiceorderDetails_List;
    List<Product_Details_Modal> Order_Outlet_Filter;
    TextView netamount, cashdiscount, gstrate, totalfreeqty, totalqty, totalitem, subtotal, invoicedate, retaileAddress, billnumber, retailername, retailerroute, back, ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print__invoice_);
        printrecyclerview = findViewById(R.id.printrecyclerview);
        gson = new Gson();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        printrecyclerview.setLayoutManager(layoutManager);
        sharedCommonPref = new Shared_Common_Pref(Print_Invoice_Activity.this);
        common_class = new Common_Class(this);
        netamount = findViewById(R.id.netamount);
        back = findViewById(R.id.back);
        cashdiscount = findViewById(R.id.cashdiscount);
        gstrate = findViewById(R.id.gstrate);
        billnumber = findViewById(R.id.billnumber);
        totalfreeqty = findViewById(R.id.totalfreeqty);
        totalqty = findViewById(R.id.totalqty);
        totalitem = findViewById(R.id.totalitem);
        subtotal = findViewById(R.id.subtotal);
        invoicedate = findViewById(R.id.invoicedate);
        retaileAddress = findViewById(R.id.retaileAddress);
        retailername = findViewById(R.id.retailername);
        retailerroute = findViewById(R.id.retailerroute);
        ok = findViewById(R.id.ok);
        retailername.setText(Shared_Common_Pref.OutletName);
        netamount.setText(getIntent().getStringExtra("NetAmount"));
        cashdiscount.setText(getIntent().getStringExtra("Discount_Amount"));
        subtotal.setText(""+String.valueOf(getIntent().getStringExtra("Order_Values")));
        totalitem.setText(getIntent().getStringExtra("No_Of_Items"));
        invoicedate.setText("Date : " + "\t\t" + getIntent().getStringExtra("Invoice_Date"));
        billnumber.setText("Bill no :" + "\t\t" + Shared_Common_Pref.TransSlNo);
        back.setOnClickListener(this);
        ok.setOnClickListener(this);
        if (Shared_Common_Pref.Invoicetoorder != null) {
            if (Shared_Common_Pref.Invoicetoorder.equals("1")) {
                String orderlist = sharedCommonPref.getvalue(Shared_Common_Pref.TodayOrderDetails_List);
                userType = new TypeToken<ArrayList<Trans_Order_Details_Offline>>() {
                }.getType();
                InvoiceorderDetails_List = gson.fromJson(orderlist, userType);
                Order_Outlet_Filter = new ArrayList<>();
                Order_Outlet_Filter.clear();
                int total_qtytext = 0;
                for (Trans_Order_Details_Offline ivl : InvoiceorderDetails_List) {
                    if (ivl.getTransSlNo().equals(Shared_Common_Pref.TransSlNo)) {
                        Log.e("Product_Name", ivl.getProductName());
                        Log.e("Product_getQty", String.valueOf(ivl.getQuantity()));
                       // total_qtytext += ivl.getQty() + ivl.getQuantity();
                        total_qtytext +=ivl.getQuantity();
                        Order_Outlet_Filter.add(new Product_Details_Modal(ivl.getProductCode(), ivl.getProductName(), 1, "1",
                                "1", "5", "i", 7.99, 1.8, ivl.getRate(), ivl.getQuantity(), ivl.getQty(), ivl.getValue()));
                    }
                }
                totalqty.setText("" + String.valueOf(total_qtytext));
                mReportViewAdapter = new Print_Invoice_Adapter(Print_Invoice_Activity.this, Order_Outlet_Filter, new AdapterOnClick() {
                    @Override
                    public void onIntentClick(int position) {
                    }
                });
                printrecyclerview.setAdapter(mReportViewAdapter);

            } else {

            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ok:

                break;
        }
    }
}
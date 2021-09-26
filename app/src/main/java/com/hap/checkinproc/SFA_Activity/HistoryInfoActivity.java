package com.hap.checkinproc.SFA_Activity;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.Interface.UpdateResponseUI;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.Invoice_History_Adapter;
import com.hap.checkinproc.SFA_Model_Class.OutletReport_View_Modal;
import com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class HistoryInfoActivity extends AppCompatActivity implements View.OnClickListener, UpdateResponseUI {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    TextView tvOutletName, tvStartDate, tvEndDate;

    Common_Class common_class;
    ImageView ivToolbarHome;
    Shared_Common_Pref shared_common_pref;
    DatePickerDialog fromDatePickerDialog;
    String date = "";
    static SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");

    String response = "";

    String TAG="HistoryInfoActivity";
    List<OutletReport_View_Modal> OutletReport_View_Modal;
    List<OutletReport_View_Modal> FilterOrderList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_info);
        init();

        shared_common_pref = new Shared_Common_Pref(this);
        tvOutletName.setText(shared_common_pref.getvalue(Constants.Retailor_Name_ERP_Code));
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        common_class.gotoHomeScreen(this, ivToolbarHome);

        common_class.getDataFromApi(Constants.GetTodayOrder_List, this, false);
        common_class.getDataFromApi(Constants.Outlet_Total_AlldaysOrders, this, false);


    }

    void init() {
        tvOutletName = findViewById(R.id.retailername);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        common_class = new Common_Class(this);
        ivToolbarHome = (ImageView) findViewById(R.id.toolbar_home);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);


        tvStartDate.setOnClickListener(this);
        tvEndDate.setOnClickListener(this);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OrderInfoFragment("Order"), "Order");
        adapter.addFragment(new InvoiceInfoFragment("Invoice"), "Invoice");
        adapter.addFragment(new OrderInvoiceInfoFragment("Order vs Invoice"), "Order vs Invoice");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvStartDate:
                selectDate(1);

                break;
            case R.id.tvEndDate:
                selectDate(2);
                break;

        }
    }


    public static boolean checkDates(String stDate, String endDate) {
        boolean b = false;
        try {
            if (dfDate.parse(stDate).before(dfDate.parse(endDate))) {
                b = true;//If start date is before end date
            } else if (dfDate.parse(stDate).equals(dfDate.parse(endDate))) {
                b = true;//If two dates are equal
            } else {
                b = false; //If start date is after the end date
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return b;
    }

    @Override
    public void onLoadFilterData(List<Retailer_Modal_List> retailer_modal_list) {

    }

    @Override
    public void onLoadTodayOrderList(List<OutletReport_View_Modal> outletReportViewModals) {
        if (outletReportViewModals != null) {

            OutletReport_View_Modal.clear();

            OutletReport_View_Modal = outletReportViewModals;

            FilterOrderList.clear();

            if (OutletReport_View_Modal != null && OutletReport_View_Modal.size() > 0) {
                for (OutletReport_View_Modal filterlist : OutletReport_View_Modal) {
                    if (filterlist.getOutletCode().equals(Shared_Common_Pref.OutletCode)) {
                        FilterOrderList.add(filterlist);
                    }
                }
            }


//            mReportViewAdapter = new Invoice_History_Adapter(Invoice_History.this, FilterOrderList, new AdapterOnClick() {
//                @Override
//                public void onIntentClick(int position) {
//                    Log.e("TRANS_SLNO", FilterOrderList.get(position).getTransSlNo());
//                    Shared_Common_Pref.TransSlNo = FilterOrderList.get(position).getTransSlNo();
//                    Shared_Common_Pref.Invoicetoorder = "1";
////                    if (FilterOrderList.get(position).getStatus().equals("ORDER")) {
////                        getPreOrderQty();
////
////                    } else {
//                    Intent intent = new Intent(getBaseContext(), Print_Invoice_Activity.class);
//                    sharedCommonPref.save(Constants.FLAG, FilterOrderList.get(position).getStatus());
//
//                    Log.e("Sub_Total", String.valueOf(FilterOrderList.get(position).getOrderValue() + ""));
//                    intent.putExtra("Order_Values", FilterOrderList.get(position).getOrderValue() + "");
//                    intent.putExtra("Invoice_Values", FilterOrderList.get(position).getInvoicevalues());
//                    intent.putExtra("No_Of_Items", FilterOrderList.get(position).getNo_Of_items());
//                    intent.putExtra("Invoice_Date", FilterOrderList.get(position).getOrderDate());
//                    intent.putExtra("NetAmount", FilterOrderList.get(position).getNetAmount());
//                    intent.putExtra("Discount_Amount", FilterOrderList.get(position).getDiscount_Amount());
//                    startActivity(intent);
//                    //  }
//
//                }
//            });
//            invoicerecyclerview.setAdapter(mReportViewAdapter);
        }

    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse) {


    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    void selectDate(int val) {

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(HistoryInfoActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int month = monthOfYear + 1;

                date = ("" + year + "-" + month + "-" + dayOfMonth);
                if (val == 1) {
                    if (checkDates(date, tvEndDate.getText().toString()) ||
                            tvEndDate.getText().toString().equals(""))
                        tvStartDate.setText(date);
                    else
                        common_class.showMsg(HistoryInfoActivity.this, "Please select valid date");
                } else {
                    if (checkDates(tvStartDate.getText().toString(), date) ||
                            tvStartDate.getText().toString().equals(""))
                        tvEndDate.setText(date);
                    else
                        common_class.showMsg(HistoryInfoActivity.this, "Please select valid date");

                }
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();


    }
}
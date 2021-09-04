package com.hap.checkinproc.Activity_Hap;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.hap.checkinproc.Common_Class.AlertDialogBox;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AlertBox;
import com.hap.checkinproc.MVP.Main_Model;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Activity.Dashboard_Order_Reports;
import com.hap.checkinproc.SFA_Activity.Dashboard_Route;
import com.hap.checkinproc.SFA_Activity.Dist_Locations;
import com.hap.checkinproc.SFA_Activity.Lead_Activity;
import com.hap.checkinproc.SFA_Activity.Offline_Sync_Activity;
import com.hap.checkinproc.SFA_Activity.Outlet_Info_Activity;
import com.hap.checkinproc.SFA_Activity.Reports_Outler_Name;
import com.hap.checkinproc.SFA_Activity.SFADCRActivity;
import com.hap.checkinproc.SFA_Activity.SFA_Dashboard;
import com.hap.checkinproc.common.DatabaseHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SFA_Activity extends AppCompatActivity implements View.OnClickListener /*,Main_Model.MasterSyncView*/ {
    LinearLayout Lin_Route, Lin_DCR, Lin_Lead, Lin_Dashboard, Lin_Outlet, DistLocation, Logout, lin_Reports, SyncButon, linorders;
    Gson gson;
    Type userType;
    Common_Class common_class;
    private Main_Model.presenter presenter;
    Shared_Common_Pref sharedCommonPref;
    DatabaseHandler db;

    ImageView ivLogout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private RelativeLayout dotsLayout;
    private TextView[] dots;

    Switch switchGraphMode;
    private ViewPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sfactivity);
        db = new DatabaseHandler(this);
        sharedCommonPref = new Shared_Common_Pref(SFA_Activity.this);
        ivLogout = findViewById(R.id.toolbar_home);
        Lin_Route = findViewById(R.id.Lin_Route);
        SyncButon = findViewById(R.id.SyncButon);
        DistLocation = findViewById(R.id.DistLocation);
        Lin_Lead = findViewById(R.id.Lin_Lead);
        Lin_DCR = findViewById(R.id.Lin_DCR);
        Lin_Dashboard = findViewById(R.id.Lin_Dashboard);
        Lin_Outlet = findViewById(R.id.Lin_Outlet);
        linorders = findViewById(R.id.linorders);
        lin_Reports = findViewById(R.id.lin_Reports);
        Logout = findViewById(R.id.Logout);
        switchGraphMode = (Switch) findViewById(R.id.switchCumulativeMode);

        common_class = new Common_Class(this);
        SyncButon.setOnClickListener(this);
        Lin_Route.setOnClickListener(this);
        Lin_DCR.setOnClickListener(this);
        Lin_Lead.setOnClickListener(this);
        lin_Reports.setOnClickListener(this);
        Lin_Dashboard.setOnClickListener(this);
        Lin_Outlet.setOnClickListener(this);
        DistLocation.setOnClickListener(this);
        linorders.setOnClickListener(this);
        Logout.setOnClickListener(this);
        ivLogout.setOnClickListener(this);
        //presenter = new MasterSync_Implementations(this, new Offline_SyncView());
        gson = new Gson();
        // presenter.requestDataFromServer();

/*
        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });*/

        ivLogout.setImageResource(R.drawable.ic_baseline_logout_24);
//        if (sharedCommonPref.getvalue(Constants.HAVE_VALUE, "").equals(""))
//            common_class.getDataFromApi(Constants.GetTodayOrder_List, this, false);


        init();


        addBottomDots(0);


        ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);

//            // changing the next button text 'NEXT' / 'GOT IT'
//            if (position == layouts.length - 1) {
//                // last page. make button text to GOT IT
//                btnNext.setText(getString(R.string.start));
//                btnSkip.setVisibility(View.GONE);
//            } else {
//                // still pages are left
//                btnNext.setText(getString(R.string.next));
//                btnSkip.setVisibility(View.VISIBLE);
//            }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        };

        switchGraphMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    setupViewPager(viewPager, true);
                } else {

                    setupViewPager(viewPager, false);
                }
            }
        });

        setupViewPager(viewPager, false);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);


        tabLayout.setupWithViewPager(viewPager);


    }

    private void addBottomDots(int currentPage) {
        try {

            TextView tvDot1 = findViewById(R.id.layoutDots1);
            TextView tvDot2 = findViewById(R.id.layoutDots2);
            TextView tvDot3 = findViewById(R.id.layoutDots3);

            switch (currentPage) {
                case 0:
                    tvDot1.setTextColor(Color.BLUE);
                    tvDot2.setTextColor(Color.LTGRAY);
                    tvDot3.setTextColor(Color.LTGRAY);
                    break;
                case 1:
                    tvDot1.setTextColor(Color.LTGRAY);
                    tvDot2.setTextColor(Color.BLUE);
                    tvDot3.setTextColor(Color.LTGRAY);

                    break;
                case 2:
                    tvDot1.setTextColor(Color.LTGRAY);
                    tvDot2.setTextColor(Color.LTGRAY);
                    tvDot3.setTextColor(Color.BLUE);

                    break;
            }


        } catch (Exception e) {
            Log.e("SliderConcept: ", e.getMessage());
        }
    }

    private void setupViewPager(ViewPager viewPager, boolean isGraphMode) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());


        adapter.addFragment(new DashboardOutletDataFrag(this, "Outlet"), "Outlet");
        adapter.addFragment(new DashboardSalesDataFrag(this, "Sales"), "Sales");
        adapter.addFragment(new DashboardTableDataFrag(this, "Table3"), "Volume");
        adapter.addFragment(new DashboardVisitDataFrag(this, "Table4"), "Visit");

        findViewById(R.id.llPagerDots).setVisibility(View.VISIBLE);
        addBottomDots(0);


        viewPager.setAdapter(adapter);


//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//
//        if (isGraphMode) {
//            adapter.resetFragment();
//            adapter.addFragment(new CombinedChartFragment(this, "Table4"), "T4");
//
//            findViewById(R.id.llPagerDots).setVisibility(View.GONE);
//
//        } else {
//            adapter.resetFragment();
//            adapter.addFragment(new DashboardTableDataFrag(this, "Table1"), "T1");
//            adapter.addFragment(new DashboardBarDataFrag("Table2"), "T2");
//            adapter.addFragment(new DashboardBarDataFrag("Table3"), "T3");
//            adapter.addFragment(new DashboardBarDataFrag("Table3"), "T3");
//
//            findViewById(R.id.llPagerDots).setVisibility(View.VISIBLE);
//            addBottomDots(0);
//
//        }
//        viewPager.setAdapter(adapter);
//        adapter.notifyDataSetChanged();


    }


    public void init() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);


        tabLayout = (TabLayout) findViewById(R.id.tabs);


//        TableLayout stk = (TableLayout) findViewById(R.id.table_main);
//        TableRow tbrow0 = new TableRow(this);
//        TextView tv0 = new TextView(this);
//        tv0.setText(" Description ");
//        tv0.setTextColor(Color.WHITE);
//        tbrow0.addView(tv0);
//        TextView tv1 = new TextView(this);
//        tv1.setText(" Existing ");
//        tv1.setTextColor(Color.WHITE);
//        tbrow0.addView(tv1);
//        TextView tv2 = new TextView(this);
//        tv2.setText(" New ");
//        tv2.setTextColor(Color.WHITE);
//        tbrow0.addView(tv2);
//        TextView tv3 = new TextView(this);
//        tv3.setText(" Total Milk \n Litres ");
//        tv3.setTextColor(Color.WHITE);
//        tbrow0.addView(tv3);
//
//        TextView tv4 = new TextView(this);
//        tv4.setText(" New Milk Orders \n Litres ");
//        tv4.setTextColor(Color.WHITE);
//        tbrow0.addView(tv4);
//
//
//        stk.addView(tbrow0);
//        for (int i = 0; i < 25; i++) {
//            TableRow tbrow = new TableRow(this);
//            TextView t1v = new TextView(this);
//            t1v.setText("" + i);
//            t1v.setTextColor(Color.WHITE);
//            t1v.setGravity(Gravity.CENTER);
//            tbrow.addView(t1v);
//            TextView t2v = new TextView(this);
//            t2v.setText("Product " + i);
//            t2v.setTextColor(Color.WHITE);
//            t2v.setGravity(Gravity.CENTER);
//            tbrow.addView(t2v);
//            TextView t3v = new TextView(this);
//            t3v.setText("Rs." + i);
//            t3v.setTextColor(Color.WHITE);
//            t3v.setGravity(Gravity.CENTER);
//            tbrow.addView(t3v);
//            TextView t4v = new TextView(this);
//            t4v.setText("" + i * 15 / 32 * 10);
//            t4v.setTextColor(Color.WHITE);
//            t4v.setGravity(Gravity.CENTER);
//            tbrow.addView(t4v);
//            stk.addView(tbrow);
//        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Lin_Dashboard:
                common_class.CommonIntentwithNEwTask(SFA_Dashboard.class);
                break;
            case R.id.Lin_DCR:
              //  common_class.CommonIntentwithNEwTask(SFADCRActivity.class);
                Intent intent = new Intent(SFA_Activity.this, Dashboard_Route.class);
                startActivity(intent);
                break;
            case R.id.Lin_Route:
                sharedCommonPref.save(sharedCommonPref.DCRMode, "");
                common_class.CommonIntentwithNEwTask(Dashboard_Route.class);
                break;
            case R.id.Lin_Outlet:
                common_class.CommonIntentwithNEwTask(Outlet_Info_Activity.class);
                break;
            case R.id.DistLocation:
                common_class.CommonIntentwithNEwTask(Dist_Locations.class);
                break;
            case R.id.lin_Reports:
                common_class.CommonIntentwithNEwTask(Reports_Outler_Name.class);
                break;
            case R.id.SyncButon:
                Shared_Common_Pref.Sync_Flag = "10";
                common_class.CommonIntentwithNEwTask(Offline_Sync_Activity.class);
                break;
            case R.id.linorders:
                common_class.CommonIntentwithNEwTask(Dashboard_Order_Reports.class);
                break;
            case R.id.Lin_Lead:
                common_class.CommonIntentwithNEwTask(Lead_Activity.class);
                break;
            case R.id.toolbar_home:
                AlertDialogBox.showDialog(SFA_Activity.this, "HAP SFA", "Are You Sure Want to Logout?", "OK", "Cancel", false, new AlertBox() {
                    @Override
                    public void PositiveMethod(DialogInterface dialog, int id) {
                        sharedCommonPref.save("ActivityStart", "false");
                        Intent intent = new Intent(SFA_Activity.this, Dashboard_Two.class);
                        intent.putExtra("Mode", "CIN");
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void NegativeMethod(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                break;
        }
    }



   /* @Override
    public void showProgress() {
    }

    @Override
    public void hideProgress() {
    }

    @Override
    public void setDataToRoute(ArrayList<Route_Master> noticeArrayList) {

    }

    @Override
    public void setDataToRouteObject(Object responsebody, int position) {
        Log.e("Calling Position", String.valueOf(position));
        // Toast.makeText(this, "Position" + position, Toast.LENGTH_SHORT).show();
        Log.e("ResponseFromServer", String.valueOf(responsebody));
        String serializedData = gson.toJson(responsebody);
        switch (position) {
            case (0):
                //Outlet_List
                System.out.println("GetTodayOrder_All" + serializedData);
                sharedCommonPref.save(Shared_Common_Pref.Outlet_List, serializedData);
                break;
            case (1):
                //Distributor_List
                //  System.out.println("Distributor_List" + serializedData);
                //  sharedCommonPref.save(Shared_Common_Pref.Distributor_List, serializedData);
                break;
            case (2):
                //Category_List
                System.out.println("Category_List" + serializedData);
                sharedCommonPref.save(Shared_Common_Pref.Category_List, serializedData);
                break;
            case (3):
                //Product_List
                System.out.println("Product_List" + serializedData);
                sharedCommonPref.save(Shared_Common_Pref.Product_List, serializedData);
                break;
            case (4):
                //GetTodayOrder_List
                System.out.println("GetTodayOrder_List" + serializedData);
                sharedCommonPref.save(Shared_Common_Pref.GetTodayOrder_List, serializedData);
                break;
            default:
                System.out.println("Compititor_List" + serializedData);
                sharedCommonPref.save(Shared_Common_Pref.Compititor_List, serializedData);
        }
    }

    @Override
    public void onResponseFailure(Throwable throwable) {

    }*/

/*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            common_class.CommonIntentwithFinish(Dashboard.class);
            return true;
        }
        return false;
    }
*/

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            });


    @Override
    public void onBackPressed() {
        Log.v("CHECKING", "CHECKING");
    }


    class ViewPagerAdapter extends FragmentStatePagerAdapter {
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
            notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }


        public void resetFragment() {
            mFragmentList.clear();
            mFragmentTitleList.clear();

        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
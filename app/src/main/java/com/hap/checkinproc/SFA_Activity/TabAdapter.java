package com.hap.checkinproc.SFA_Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.google.android.material.tabs.TabLayout;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.SFA_Activity.Dashboard_Route.AllDataFragment;
import com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List;

import java.util.ArrayList;
import java.util.List;

public class TabAdapter extends FragmentStatePagerAdapter {
    List<Retailer_Modal_List> Retailer_Modal_ListFilter;
    TabLayout tabLayout;
    List<Retailer_Modal_List> mRetailer_Modal_List;

    int updatePos = -1;

    public TabAdapter(FragmentManager fm, TabLayout _tabLayout, List<Retailer_Modal_List> retailer_Modal_List) {
        super(fm);
        this.tabLayout = _tabLayout;
        this.mRetailer_Modal_List = retailer_Modal_List;
    }



    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;


        if (position == 0) {
            OutletFilter("1");
            fragment = new AllDataFragment(Retailer_Modal_ListFilter, position);
        } else if (position == 1) {
            OutletFilter("2");
            fragment = new AllDataFragment(Retailer_Modal_ListFilter, position);
        } else if (position == 2) {
            OutletFilter("3");
            fragment = new AllDataFragment(Retailer_Modal_ListFilter, position);
        } else if (position == 3) {
            OutletFilter("4");
            fragment = new AllDataFragment(Retailer_Modal_ListFilter, position);

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;


        switch (position) {
            case 0:
                OutletFilter("1");
                title = "To be - " + Retailer_Modal_ListFilter.size();
                break;
            case 1:
                OutletFilter("2");
                title = "Invoice - " + Retailer_Modal_ListFilter.size();
                break;
            case 2:
                OutletFilter("3");
                title = "Order - " + Retailer_Modal_ListFilter.size();
                break;
            case 3:
                OutletFilter("4");
                title = "No Order - " + Retailer_Modal_ListFilter.size();
                break;

        }


        return title;
    }


    public void OutletFilter(String flag) {
        Shared_Common_Pref shared_common_pref = new Shared_Common_Pref(Dashboard_Route.dashboard_route);
        Retailer_Modal_ListFilter = new ArrayList<>();
        String Route_id = shared_common_pref.getvalue(Constants.Route_Id);
        if (mRetailer_Modal_List != null) {
            int sMode = (flag.equals("1")) ? 0 : (flag.equals("2")) ? 3 : (flag.equals("3")) ? 2 : 1;
            for (int i = 0; i < mRetailer_Modal_List.size(); i++) {
                if (mRetailer_Modal_List.get(i).getOrderFlg() == (sMode) &&
                        (Route_id.equalsIgnoreCase("") || Route_id.equalsIgnoreCase(mRetailer_Modal_List.get(i).getTownCode()))) {
                    Retailer_Modal_ListFilter.add(mRetailer_Modal_List.get(i));
                }
            }
        }


    }


}
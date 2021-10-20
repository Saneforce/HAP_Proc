package com.hap.checkinproc.SFA_Activity;

import android.util.Log;

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
    List<Retailer_Modal_List> mSearchList;

    List<AllDataFragment> frag;
    private int mTabPos = -1;


    public TabAdapter(FragmentManager fm, int tabPos, List<Retailer_Modal_List> retailer_Modal_List) {
        super(fm);
        this.mTabPos = tabPos;
        this.mRetailer_Modal_List = retailer_Modal_List;
        Log.v("tabAdapter: ", "pos:" + tabPos);
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        OutletFilter(position);
        fragment = new AllDataFragment(Retailer_Modal_ListFilter, position);
        return fragment;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


    public void notifyData(List<Retailer_Modal_List> retailer_Modal_List, int tabPos) {
        this.mTabPos = tabPos;
        this.mSearchList = retailer_Modal_List;
        // OutletFilter(tabPos);
        notifyDataSetChanged();

    }


    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = (position == 0) ? "To be - " : position == 1 ? "Invoice - " : position == 2 ? "Order - " : "No Order - ";
        OutletFilter(position);
        title = title + Retailer_Modal_ListFilter.size();
        return title;
    }


    public void OutletFilter(int flag) {
        Shared_Common_Pref shared_common_pref = new Shared_Common_Pref(Dashboard_Route.dashboard_route);
        Retailer_Modal_ListFilter = new ArrayList<>();
        String Route_id = shared_common_pref.getvalue(Constants.Route_Id);
        if (mRetailer_Modal_List != null) {
            String val = shared_common_pref.getvalue(Constants.RETAILER_STATUS);
            String sMode = flag == 0 ? "To be" : flag == 1 ? "invoice" : flag == 2 ? "order" : "no order";
            for (int i = 0; i < mRetailer_Modal_List.size(); i++) {
                if (val.indexOf(mRetailer_Modal_List.get(i).getId() + sMode) > -1 &&
                        (Route_id.equalsIgnoreCase("") || Route_id.equalsIgnoreCase(mRetailer_Modal_List.get(i).getTownCode()))) {
                    Retailer_Modal_ListFilter.add(mRetailer_Modal_List.get(i));
                }
            }
        }
    }


}
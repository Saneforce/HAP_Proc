package com.hap.checkinproc.SFA_Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.google.android.material.tabs.TabLayout;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.SFA_Activity.Dashboard_Route.AllDataFragment;
import com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TabAdapter extends FragmentStatePagerAdapter {
    List<Retailer_Modal_List> Retailer_Modal_List;
    List<Retailer_Modal_List> Retailer_Modal_ListFilter;
    TabLayout tabLayout;
    List<Retailer_Modal_List> mRetailer_Modal_ListFilter;
    public TabAdapter(@NonNull @NotNull FragmentManager fm) {
        super(fm);
    }

    public TabAdapter(FragmentManager fm, TabLayout _tabLayout, List<Retailer_Modal_List> retailer_Modal_ListFilter) {
        super(fm);
        this.tabLayout = _tabLayout;
        this.mRetailer_Modal_ListFilter = retailer_Modal_ListFilter;
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
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

   @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0) {
            OutletFilter("1");
            title = "All - "+Retailer_Modal_ListFilter.size();
        } else if (position == 1) {
            OutletFilter("2");
            title = "Pending - "+Retailer_Modal_ListFilter.size();
        } else if (position == 2) {
            OutletFilter("3");
            title = "Complete - "+Retailer_Modal_ListFilter.size();
        }
        return title;
    }

    public void setData(List<Retailer_Modal_List> retailer_Modal_ListFilter) {
        this.mRetailer_Modal_ListFilter = retailer_Modal_ListFilter;
    }

    public void OutletFilter(String flag) {
        Shared_Common_Pref shared_common_pref = new Shared_Common_Pref(Dashboard_Route.dashboard_route);
        Retailer_Modal_ListFilter = new ArrayList<>();
        String Route_id = shared_common_pref.getvalue(Constants.Route_Id);

        if (mRetailer_Modal_ListFilter != null) {
            for (int i = 0; i < mRetailer_Modal_ListFilter.size(); i++) {
                if (flag.equals("1")) {
                    if (!Route_id.equals("")) {
                        if (Route_id.equals(mRetailer_Modal_ListFilter.get(i).getTownCode())) {
                            Retailer_Modal_ListFilter.add(mRetailer_Modal_ListFilter.get(i));
                        }
                    } else {
                        Retailer_Modal_ListFilter.add(mRetailer_Modal_ListFilter.get(i));
                    }
                } else if (flag.equals("2")) {

                    if (mRetailer_Modal_ListFilter.get(i).getStatusname().equals("PENDING")) {
                        if (!Route_id.equals("")) {
                            if (Route_id.equals(mRetailer_Modal_ListFilter.get(i).getTownCode())) {
                                Retailer_Modal_ListFilter.add(mRetailer_Modal_ListFilter.get(i));
                            }
                        } else {
                            Retailer_Modal_ListFilter.add(mRetailer_Modal_ListFilter.get(i));

                        }
                    }

                } else if (flag.equals("3")) {
                    if (mRetailer_Modal_ListFilter.get(i).getStatusname().equals("COMPLETED")) {
                        if (!Route_id.equals("")) {
                            if (Route_id.equals(mRetailer_Modal_ListFilter.get(i).getTownCode())) {
                                Retailer_Modal_ListFilter.add(mRetailer_Modal_ListFilter.get(i));
                            }
                        } else {
                            Retailer_Modal_ListFilter.add(mRetailer_Modal_ListFilter.get(i));

                        }
                    }
                }
            }
        }

    }


}
package com.hap.checkinproc.SFA_Activity;


import android.os.Bundle;
import android.view.View;
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
import com.hap.checkinproc.R;

import java.util.ArrayList;
import java.util.List;


public class MoreInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    TextView tvOutletName;

    Common_Class common_class;
    ImageView ivToolbarHome;
    Shared_Common_Pref shared_common_pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);
        init();

        shared_common_pref=new Shared_Common_Pref(this);
        tvOutletName.setText(shared_common_pref.getvalue(Constants.Retailor_Name_ERP_Code));
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        common_class.gotoHomeScreen(this, ivToolbarHome);

    }

    void init() {
        tvOutletName = findViewById(R.id.tvOutletName);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        common_class = new Common_Class(this);
        ivToolbarHome = (ImageView) findViewById(R.id.toolbar_home);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MoreInfoFragmentThree("Mar"), "MAR");
        adapter.addFragment(new MoreInfoFragmentTwo("Feb"), "FEB");
        adapter.addFragment(new MoreInfoFragment("Jan"), "JAN");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
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
}
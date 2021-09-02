package com.hap.checkinproc.Activity_Hap;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.CumulativeInfoAdapter;

import java.util.ArrayList;
import java.util.List;

public class DashboardVisitDataFrag extends Fragment {
    String mTabName = "";
    View view;
    Context mContext;

    public DashboardVisitDataFrag(Context context, String TabName) {
        // Required empty public constructor
        this.mContext = context;
        this.mTabName = TabName;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for mContext fragment
        view = inflater.inflate(R.layout.fragment_visitdata_info, container, false);



        return view;
    }


}
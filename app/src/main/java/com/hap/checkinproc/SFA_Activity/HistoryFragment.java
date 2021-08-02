package com.hap.checkinproc.SFA_Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.hap.checkinproc.R;

public class HistoryFragment extends Fragment {
    String mTabName = "";

    public HistoryFragment(String TabName) {
        // Required empty public constructor
        this.mTabName = TabName;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.order_history_fragment, container, false);

        TextView tvTitle = view.findViewById(R.id.tvTabName);
        tvTitle.setText(mTabName);

        return view;
    }

}
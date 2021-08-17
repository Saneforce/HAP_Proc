package com.hap.checkinproc.SFA_Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.MoreInfoAdapter;
import com.hap.checkinproc.SFA_Model_Class.Product_Details_Modal;

import java.util.ArrayList;
import java.util.List;

public class MoreInfoFragment extends Fragment {
    String mTabName = "";
    RecyclerView recyclerView;

    MoreInfoAdapter moreInfoAdapter;
    private List<Product_Details_Modal> mProductList = new ArrayList<>();

    public MoreInfoFragment(String TabName) {
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
        View view = inflater.inflate(R.layout.fragment_more_info, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);

        mProductList.clear();


        mProductList.add(new Product_Details_Modal("milk", "Aavin", 1, "1",
                "1", "5", "i", 7.99, 1.8,
                50.0, 10, 25, 150.0));

        mProductList.add(new Product_Details_Modal("Curd", "Arokya", 1, "1",
                "1", "5", "i", 7.99, 1.8,
                30.0, 10, 25, 270.0));
        mProductList.add(new Product_Details_Modal("Butter", "Arokya", 1, "1",
                "1", "5", "i", 7.99, 1.8,
                70.0, 10, 25, 200.0));

        moreInfoAdapter = new MoreInfoAdapter(getActivity(), mProductList, new AdapterOnClick() {
            @Override
            public void onIntentClick(int position) {
            }
        });

        recyclerView.setAdapter(moreInfoAdapter);


//        TextView tvTitle = view.findViewById(R.id.tvTabName);
//        tvTitle.setText(mTabName);

        return view;
    }

}
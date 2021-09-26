package com.hap.checkinproc.SFA_Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.HistoryInfoAdapter;
import com.hap.checkinproc.SFA_Model_Class.OutletReport_View_Modal;
import com.hap.checkinproc.SFA_Model_Class.Product_Details_Modal;
import com.hap.checkinproc.common.DatabaseHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class OrderInfoFragment extends Fragment {
    String mTabName = "";
    RecyclerView recyclerView;

    HistoryInfoAdapter historyInfoAdapter;
    private List<Product_Details_Modal> mProductList = new ArrayList<>();
    List<OutletReport_View_Modal> OutletReport_View_Modal;
    List<OutletReport_View_Modal> FilterOrderList = new ArrayList<>();

    public OrderInfoFragment(String TabName) {
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
        View view = inflater.inflate(R.layout.history_more_info_layout, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);

        DatabaseHandler db = new DatabaseHandler(getActivity());
        String OrdersTable = String.valueOf(db.getMasterData(Constants.GetTodayOrder_List));
        Type userType = new TypeToken<ArrayList<OutletReport_View_Modal>>() {
        }.getType();

        Gson gson = new Gson();
        OutletReport_View_Modal = gson.fromJson(OrdersTable, userType);
        FilterOrderList.clear();
        if (OutletReport_View_Modal != null && OutletReport_View_Modal.size() > 0) {
            for (OutletReport_View_Modal filterlist : OutletReport_View_Modal) {
                if (filterlist.getStatus().equals("ORDER")) {
                    FilterOrderList.add(filterlist);
                }
            }
        }


        historyInfoAdapter = new HistoryInfoAdapter(getActivity(), FilterOrderList, new AdapterOnClick() {
            @Override
            public void onIntentClick(int position) {
            }
        });

        recyclerView.setAdapter(historyInfoAdapter);


        return view;
    }

}
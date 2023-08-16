package com.hap.checkinproc.SFA_Activity;

import static com.hap.checkinproc.Common_Class.Constants.Route_Id;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Activity_Hap.AddNewRetailer;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.Outlet_Info_Adapter;
import com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PendingOutletsActivity extends AppCompatActivity {
    ImageView toolbarHome;
    LottieAnimationView noDataFound, progressBar;
    LinearLayout errorLayout;
    TextView errorText;
    RecyclerView recyclerView;

    List<com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List> Retailer_Modal_List = new ArrayList<>();
    List<Retailer_Modal_List> Retailer_Modal_ListFilter = new ArrayList<>();

    Shared_Common_Pref sharedCommonPref;
    Context context = this;
    Gson gson;
    Type userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_outlets);

        toolbarHome = findViewById(R.id.toolbar_home);
        noDataFound = findViewById(R.id.noDataFound);
        progressBar = findViewById(R.id.progressBar);
        errorLayout = findViewById(R.id.errorLayout);
        errorText = findViewById(R.id.errorText);
        recyclerView = findViewById(R.id.recyclerView);

        sharedCommonPref = new Shared_Common_Pref(this);
        gson = new Gson();
        userType = new TypeToken<ArrayList<Retailer_Modal_List>>() {
        }.getType();

        prepareData();
    }

    private void prepareData() {
        startLoading();

        String OrdersTable = sharedCommonPref.getvalue(Constants.Retailer_OutletList);
        Retailer_Modal_List = gson.fromJson(OrdersTable, userType);
        String routeId = sharedCommonPref.getvalue(Route_Id);

        Retailer_Modal_ListFilter.clear();
        if (Retailer_Modal_List != null) {
            for (int sr = 0; sr < Retailer_Modal_List.size(); sr++) {
                if (Retailer_Modal_List.get(sr).getDoctor_Active_flag().equals("2") && ((Retailer_Modal_List.get(sr).getTownCode().equals(routeId)) || (routeId.equals("")))) {
                    Retailer_Modal_ListFilter.add(Retailer_Modal_List.get(sr));
                }
            }
        }

        if (Retailer_Modal_ListFilter != null && !Retailer_Modal_ListFilter.isEmpty()) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(new Outlet_Info_Adapter(Retailer_Modal_ListFilter, R.layout.item_pending_outlet, this, "Outlets", new AdapterOnClick() {
                @Override
                public void onIntentClick(int position) {
                    try {
                        Intent intent = new Intent(getApplicationContext(), AddNewRetailer.class);
                        Shared_Common_Pref.Outlet_Info_Flag = "1";
                        Shared_Common_Pref.Editoutletflag = "0";
                        Shared_Common_Pref.Outler_AddFlag = "0";
                        Shared_Common_Pref.FromActivity = "Outlets";
                        Shared_Common_Pref.OutletCode = String.valueOf(Retailer_Modal_ListFilter.get(position).getId());
                        intent.putExtra("OutletCode", String.valueOf(Retailer_Modal_ListFilter.get(position).getId()));
                        intent.putExtra("OutletName", Retailer_Modal_ListFilter.get(position).getName());
                        intent.putExtra("OutletAddress", Retailer_Modal_ListFilter.get(position).getListedDrAddress1());
                        intent.putExtra("OutletMobile", Retailer_Modal_ListFilter.get(position).getPrimary_No());
                        intent.putExtra("OutletRoute", Retailer_Modal_ListFilter.get(position).getTownName());
                        startActivity(intent);
                    } catch (Exception ignored) {
                    }
                }
            }));
            finishLoading();
        } else {
            finishLoadingWithEmpty();
        }

    }

    private void finishLoadingWithEmpty() {
        noDataFound.playAnimation();
        progressBar.cancelAnimation();
        progressBar.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        noDataFound.setVisibility(View.VISIBLE);
    }

    private void finishLoading() {
        progressBar.cancelAnimation();
        noDataFound.cancelAnimation();
        progressBar.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
        noDataFound.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void startLoading() {
        progressBar.playAnimation();
        progressBar.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.GONE);
        noDataFound.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
    }
}
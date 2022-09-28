package com.hap.checkinproc.SFA_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.hap.checkinproc.R;

import java.util.ArrayList;
import java.util.List;

public class InshopOutletActivity extends AppCompatActivity {

    RecyclerView brand_recyclerView, category_recyclerView;

    ArrayList<String> brand_list = new ArrayList<>();
    ArrayList<String> category_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inshop_outlet);

        brand_recyclerView = findViewById(R.id.inshopBrandRecyclerView);
        category_recyclerView = findViewById(R.id.inshopCategoryRecyclerview);

        brand_list.add("777 Brand");
        brand_list.add("FOCUS PRODUCT");
        brand_list.add("MTR items");
        brand_list.add("Sakthi Masala");
        brand_list.add("777 Brand");


        category_list.add("Sakthi Garam Masala 50 gm");
        category_list.add("Sakthi Garam Masala 250 gm");
        category_list.add("Sakthi Garam Masala 500 gm");

        InshopBrandAdapter adapter= new InshopBrandAdapter(this, brand_list);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
//        brand_recyclerView.setLayoutManager(linearLayoutManager);
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        brand_recyclerView.setLayoutManager(layoutManager);
        brand_recyclerView.setAdapter(adapter);

        InshopCategoryAdapter adapter1= new InshopCategoryAdapter(this, category_list);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getApplicationContext());
        category_recyclerView.setLayoutManager(linearLayoutManager1);
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        category_recyclerView.setAdapter(adapter1);

    }
}
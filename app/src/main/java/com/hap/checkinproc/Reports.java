package com.hap.checkinproc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.hap.checkinproc.adapters.RecyclerViewAdapter;

import java.util.ArrayList;

public class Reports extends AppCompatActivity {

    private ArrayList<String> mNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);


        initShift_Time();

    }

    private void initShift_Time(){


        mNames.add("10.00 am - 12.00 pm");
        mNames.add("01.00 pm - 04.00 pm");
        mNames.add("05.00 pm - 06.00 pm");
        mNames.add("01.00 pm - 04.00 pm");

        initRecyclerView();
    }
    private void initRecyclerView(){

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mNames, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}
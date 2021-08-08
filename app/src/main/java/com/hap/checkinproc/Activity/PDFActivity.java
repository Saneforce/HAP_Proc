package com.hap.checkinproc.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.R;
import com.hap.checkinproc.common.TimerService;

public class PDFActivity extends AppCompatActivity {
    RecyclerView mPdfRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_d_f);
        mPdfRecycler =  findViewById(R.id.pdf_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mPdfRecycler.setLayoutManager(layoutManager);
    }
}
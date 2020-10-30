package com.hap.checkinproc.Activity_Hap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.hap.checkinproc.R;

public class Ta_approval extends AppCompatActivity {

    String name;
   TextView names;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ta_approval);

        Intent i = getIntent();
        name = i.getStringExtra("applieddate");

        names=findViewById(R.id.textView38);
        names.setText(name);



    }
}
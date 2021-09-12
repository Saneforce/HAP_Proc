package com.hap.checkinproc.SFA_Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.R;

public class OrderCategoriesTypesActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnOtherBrand, btnQPS, btnPOP, btnCoolerInfo;
    Common_Class common_class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_categories_types);

        btnCoolerInfo = findViewById(R.id.btnCoolerInfo);
        btnOtherBrand = findViewById(R.id.btnOtherBrand);
        btnPOP = findViewById(R.id.btnPOP);
        btnQPS = findViewById(R.id.btnQPS);

        btnPOP.setOnClickListener(this);
        btnOtherBrand.setOnClickListener(this);
        btnQPS.setOnClickListener(this);
        btnCoolerInfo.setOnClickListener(this);

        common_class = new Common_Class(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOtherBrand:
                break;
            case R.id.btnQPS:
                common_class.CommonIntentwithoutFinish(QPSActivity.class);

                break;
            case R.id.btnPOP:
                common_class.CommonIntentwithoutFinish(POPActivity.class);
                break;
            case R.id.btnCoolerInfo:
                common_class.CommonIntentwithoutFinish(CoolerInfoActivity.class);
                break;
        }

    }
}

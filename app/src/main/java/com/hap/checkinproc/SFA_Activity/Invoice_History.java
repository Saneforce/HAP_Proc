package com.hap.checkinproc.SFA_Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.R;

public class Invoice_History extends AppCompatActivity implements View.OnClickListener {
    TextView outlet_name, lastinvoice;
    LinearLayout lin_order, lin_repeat_order, lin_invoice, lin_repeat_invoice;
    Common_Class common_class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice__history);
        common_class = new Common_Class(this);
        lin_order = findViewById(R.id.lin_order);
        outlet_name = findViewById(R.id.outlet_name);
        outlet_name.setText(Shared_Common_Pref.OutletName);
        lin_repeat_order = findViewById(R.id.lin_repeat_order);
        lin_invoice = findViewById(R.id.lin_invoice);
        lin_repeat_invoice = findViewById(R.id.lin_repeat_invoice);
        lin_order.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_order:
                common_class.CommonIntentwithFinish(Order_Category_Select.class);
                break;
            case R.id.lin_repeat_order:
                break;
            case R.id.lin_invoice:
                break;
            case R.id.lin_repeat_invoice:
                break;
        }
    }
}
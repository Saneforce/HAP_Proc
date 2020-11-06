package com.hap.checkinproc.Activity_Hap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.R;
import java.util.Calendar;
public class Tp_Month_Select extends AppCompatActivity implements View.OnClickListener {
    TextView CurrentMoth, NextMonth;
    int CM;
    int NM;
    public ImageView backarow;
    Common_Class common_class;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tp__month__select);
        CurrentMoth = findViewById(R.id.CurrentMoth);
        NextMonth = findViewById(R.id.NextMonth);
        common_class = new Common_Class(this);
        Calendar cal = Calendar.getInstance();
        backarow = findViewById(R.id.backarow);
        CM = cal.get(Calendar.MONTH);
        NM = cal.get(Calendar.MONTH) + 1;
        String currrentmonth = common_class.GetMonthname(CM);
        String nextmonth = common_class.GetMonthname(NM);
        Log.e("CURRENT_MONTH", common_class.GetMonthname(cal.get(Calendar.MONTH)));
        Log.e("CURRENT_Example", String.valueOf(cal.get(Calendar.MONTH)));
        CurrentMoth.setText(currrentmonth);
        NextMonth.setText(nextmonth);
        CurrentMoth.setOnClickListener(this);
        NextMonth.setOnClickListener(this);
        backarow.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.CurrentMoth:
                common_class.CommonIntentwithoutFinishputextra(Tp_Calander.class, "Monthselection", String.valueOf(CM));
                break;
            case R.id.NextMonth:
                common_class.CommonIntentwithoutFinishputextra(Tp_Calander.class, "Monthselection", String.valueOf(NM));

                break;

            case R.id.backarow:
                common_class.CommonIntentwithFinish(Dashboard.class);
                break;

        }
    }
}

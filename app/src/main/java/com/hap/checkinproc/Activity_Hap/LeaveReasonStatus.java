package com.hap.checkinproc.Activity_Hap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.hap.checkinproc.R;
import com.hap.checkinproc.Status_Activity.Leave_Status_Activity;

public class LeaveReasonStatus extends AppCompatActivity {
    private EditText edtReason;
    private Button reasonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_leave_reason_status);
        edtReason = findViewById(R.id.edt_reason);
        reasonSend = findViewById(R.id.button_send);

        reasonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LeaveReasonStatus.this, Leave_Status_Activity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
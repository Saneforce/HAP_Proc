package com.hap.checkinproc.Activity_Hap;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hap.checkinproc.Common_Class.AlertDialogBox;
import com.hap.checkinproc.Interface.AlertBox;
import com.hap.checkinproc.R;

public class AddNewRetailer extends AppCompatActivity {
    TextView toolHeader;
    CustomListViewDialog customDialog;
    ImageView imgBack;
    EditText toolSearch;
    Button mSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_retailer);

        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });
    }

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    AlertDialogBox.showDialog(AddNewRetailer.this, "", "Do you want to exit?", "Yes", "NO", false, new AlertBox() {
                        @Override
                        public void PositiveMethod(DialogInterface dialog, int id) {
                            onSuperBackPressed();
                        }

                        @Override
                        public void NegativeMethod(DialogInterface dialog, int id) {
                        }
                    });
                }
            });

    public void onSuperBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void onBackPressed() {

    }
}
package com.hap.checkinproc.Activity_Hap;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;
import com.hap.checkinproc.Common_Class.AlertDialogBox;
import com.hap.checkinproc.Interface.AlertBox;
import com.hap.checkinproc.Model_Class.EventCapture;
import com.hap.checkinproc.R;
import com.hap.checkinproc.common.TimerService;

public class UpdateTaskActivity extends Activity {

    private EditText editTextDesc, editTextFinishBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_update_task);
        editTextDesc = findViewById(R.id.editTextDesc);

        editTextFinishBy = findViewById(R.id.editTextFinishBy);

        final EventCapture task = (EventCapture) getIntent().getSerializableExtra("task");


        loadTask(task);

        findViewById(R.id.button_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTask(task);
            }
        });


        findViewById(R.id.button_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialogBox.showDialog(UpdateTaskActivity.this, "", "Are you surely want to delete?", "Yes", "No", false, new AlertBox() {
                    @Override
                    public void PositiveMethod(DialogInterface dialog, int id) {
                        deleteTask(task);
                    }

                    @Override
                    public void NegativeMethod(DialogInterface dialog, int id) {
                    }
                });
            }
        });

    }

    private void loadTask(EventCapture task) {
        editTextDesc.setText(task.getDesc());
        editTextFinishBy.setText(task.getFinishBy());



    }

    private void updateTask(final EventCapture task) {
        final String sDesc = editTextDesc.getText().toString().trim();
        final String sFinishBy = editTextFinishBy.getText().toString().trim();


        class UpdateTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                task.setDesc(sDesc);
                task.setFinishBy(sFinishBy);

                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao()
                        .update(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finish();
            }
        }

        UpdateTask ut = new UpdateTask();
        ut.execute();
    }

    private void deleteTask(final EventCapture task) {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao()
                        .delete(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finish();
            }
        }

        DeleteTask dt = new DeleteTask();
        dt.execute();

    }
}

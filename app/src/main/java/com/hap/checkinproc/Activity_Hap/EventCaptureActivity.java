package com.hap.checkinproc.Activity_Hap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Model_Class.EventCapture;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.EventCaptureAdapter;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import id.zelory.compressor.Compressor;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class
EventCaptureActivity extends AppCompatActivity {


    Button TakeEventPicture;
    TextView txtRetailerName, txtRoute, txtDistributorName;
    int count = 0, count1 = 0;
    ArrayList<EventCapture> eventCapture;

    RecyclerView mEventCapture;
    EventCaptureAdapter mEventCaptureAdapter;
    ApiInterface apiInterface;

    String eventListStr;
    EventCapture taskOne = new EventCapture();
    Shared_Common_Pref mShaeShared_common_pref;

    int countInt = 0, idCount = 0;
    SharedPreferences sp;
    EditText editextTitle, editTextDescrption;
    String RetailerName, RouteName, DistributorName;
    SharedPreferences sharedPreferences;
    String intenValue;
    String RoomDataBase = "";
    private AppDatabase mDB;

    List<EventCapture> taskList;
    String locationValue, dateTime, checkInTime, keyEk = "EK", KeyDate, KeyHyp = "-", keyCodeValue, checkOutTime;
    String EventFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_capute);
        initialize();
    }

    private void initialize() {

        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });

        getTasks();
        sp = getSharedPreferences("USER_PREF", Context.MODE_PRIVATE);

        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);


        /*  shared_common_pref.save("Event_Capture","Remove");*/
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        mShaeShared_common_pref = new Shared_Common_Pref(this);


        /*        mShaeShared_common_pref.save("Event_Capture","Remove");*/




        DistributorName = mShaeShared_common_pref.getvalue("distributor_name");
        RouteName = mShaeShared_common_pref.getvalue("route_name");
        RetailerName = mShaeShared_common_pref.getvalue("Retailer_name");

        eventCapture = new ArrayList<>();
        TakeEventPicture = findViewById(R.id.btn_take_photo);
        txtRetailerName = findViewById(R.id.txt_reatiler_name);
        txtRoute = findViewById(R.id.txt_route);
        txtDistributorName = findViewById(R.id.txt_distributor_name);

        txtRetailerName.setText(RetailerName);

        txtRoute.setText(RouteName);
        txtDistributorName.setText(DistributorName);

        editextTitle = findViewById(R.id.editTextDesc);
        editTextDescrption = findViewById(R.id.editTextFinishBy);

        Log.e("NEW_COUNT_VALULE", "Heeeee");

        mEventCapture = findViewById(R.id.event_capture_list);
        mEventCapture.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mEventCapture.setLayoutManager(layoutManager);
        mEventCapture.setNestedScrollingEnabled(false);


        RoomDataBase = mShaeShared_common_pref.getvalue("Event_Capture");

        if (RoomDataBase.equalsIgnoreCase("Remove")) {
            delete();
        }


        TakeEventPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateFormat dfw = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Calendar calobjw = Calendar.getInstance();
                KeyDate = mShaeShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code);
                keyCodeValue = keyEk + KeyDate + dfw.format(calobjw.getTime()).hashCode();


                countInt = sharedPreferences.getInt("age", 0);
                if (countInt != 0) {
                    Log.e("Count_Value", String.valueOf(countInt));
                } else {
                    Log.e("Count_Value", String.valueOf(countInt));
                }

                taskOne.addToQuantity();
                count = taskOne.getmQuantity() + countInt;
                EventFileName = "EventCapture" + keyCodeValue + ".jpeg";
                Intent m_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = new File(getExternalCacheDir().getPath(), EventFileName);
                Uri uri = FileProvider.getUriForFile(EventCaptureActivity.this, getApplicationContext().getPackageName() + ".provider", file);
                m_intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(m_intent, 2);


                Log.e("EVENT_LIST_STR", String.valueOf(EventFileName));
                Log.e("EVENT_LIST_STR_SHared", String.valueOf(keyCodeValue));

            }
        });

    }


    private void getTasks() {
        class GetTasks extends AsyncTask<Void, Void, List<EventCapture>> {

            @Override
            protected List<EventCapture> doInBackground(Void... voids) {
                taskList = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().taskDao().getAll();


                return taskList;
            }

            @Override
            protected void onPostExecute(List<EventCapture> tasks) {
                super.onPostExecute(tasks);
                mEventCaptureAdapter = new EventCaptureAdapter(tasks, EventCaptureActivity.this);
                mEventCapture.setAdapter(mEventCaptureAdapter);
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 2:
                if (resultCode == RESULT_OK) {

                    EventFileName = "EventCapture" + keyCodeValue + ".jpeg";
                    File file = new File(getExternalCacheDir().getPath(), EventFileName);
                    Uri uri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", file);
                    eventListStr = String.valueOf(uri);
                    Log.e("EVENT_LIST_STR", String.valueOf(EventFileName));
                    Log.e("EVENT_LIST_STR_SHared", String.valueOf(keyCodeValue));

                    SharedPreferences.Editor myEdit
                            = sharedPreferences.edit();
                    myEdit.putInt("age", count);
                    myEdit.commit();

                    saveTask(eventListStr);
                    getMulipart(eventListStr, 0);
                }
                break;
        }
    }


    private void saveTask(String sTask) {

        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                //creating a task
                taskOne.setTask(sTask);
                taskOne.setDesc("");
                taskOne.setFinishBy("");

                //adding to database

                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().taskDao().insert(taskOne);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        }

        SaveTask st = new SaveTask();
        st.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTasks();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getTasks();
    }


    /*Call Api Image*/
    public void getMulipart(String path, int x) {
        MultipartBody.Part imgg = convertimg("file", path);
        HashMap<String, RequestBody> values = field("MR0417");
        CallApiImage(values, imgg, x);
    }

    public MultipartBody.Part convertimg(String tag, String path) {
        MultipartBody.Part yy = null;
        Log.v("full_profile", path);
        try {
            if (!TextUtils.isEmpty(path)) {

                File file = new File(path);
                if (path.contains(".png") || path.contains(".jpg") || path.contains(".jpeg"))
                    file = new Compressor(getApplicationContext()).compressToFile(new File(path));
                else
                    file = new File(path);
                RequestBody requestBody = RequestBody.create(MultipartBody.FORM, file);
                yy = MultipartBody.Part.createFormData(tag, file.getName(), requestBody);
            }
        } catch (Exception e) {
        }
        Log.v("full_profile", yy + "");
        return yy;
    }

    public HashMap<String, RequestBody> field(String val) {
        HashMap<String, RequestBody> xx = new HashMap<String, RequestBody>();
        xx.put("data", createFromString(val));

        return xx;

    }

    private RequestBody createFromString(String txt) {
        return RequestBody.create(MultipartBody.FORM, txt);
    }


    public void CallApiImage(HashMap<String, RequestBody> values, MultipartBody.Part imgg, final int x) {
        Call<ResponseBody> Callto;

        Callto = apiInterface.uploadProcPic(values, imgg);

        Callto.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.v("print_upload_file", response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (response.isSuccessful()) {
                        Log.v("print_upload_file_true", "ggg" + response);
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("print_failure", "ggg" + t.getMessage());
            }
        });
    }

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    //onSuperBackPressed();

                    Intent intent = new Intent(EventCaptureActivity.this, SecondaryOrderActivity.class);
                    intent.putExtra("Event_caputure", EventFileName);
                    startActivity(intent);

                }
            });

    public void onSuperBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void onBackPressed() {

    }


    private void delete() {

        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().clearAllTables();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
               // Toast.makeText(getApplicationContext(), "Delete", Toast.LENGTH_LONG).show();
            }
        }

        DeleteTask st = new DeleteTask();
        st.execute();
    }

}
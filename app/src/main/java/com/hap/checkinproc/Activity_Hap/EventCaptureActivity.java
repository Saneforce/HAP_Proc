package com.hap.checkinproc.Activity_Hap;

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
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import id.zelory.compressor.Compressor;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventCaptureActivity extends AppCompatActivity {


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

    int countInt = 0;
    SharedPreferences sp;
    EditText editextTitle, editTextDescrption;
    String RetailerName, RouteName, DistributorName;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_capute);


        initialize();


    }


    private void initialize() {
        sp = getSharedPreferences("USER_PREF", Context.MODE_PRIVATE);


        sharedPreferences = getSharedPreferences("MySharedPref",
                MODE_PRIVATE);


        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        mShaeShared_common_pref = new Shared_Common_Pref(this);


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

        txtRetailerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Event_capture_database", "Event_capture_database");

                count++;
                mShaeShared_common_pref.save("COUNT", String.valueOf(count));

                Log.e("NEW_COUNT_VALULE", String.valueOf(count));
            }
        });


        mEventCapture.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mEventCapture.setLayoutManager(layoutManager);
        mEventCapture.setNestedScrollingEnabled(false);


        TakeEventPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countInt = sharedPreferences.getInt("age", 0);
                if (countInt != 0) {
                    Log.e("Count_Value", String.valueOf(countInt));
                } else {
                    Log.e("Count_Value", String.valueOf(countInt));
                }

                taskOne.addToQuantity();
                count = taskOne.getmQuantity() + countInt;
                String EventFileName = "EventCapture" + count + ".jpeg";
                Intent m_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = new File(getExternalCacheDir().getPath(), EventFileName);
                Uri uri = FileProvider.getUriForFile(EventCaptureActivity.this, getApplicationContext().getPackageName() + ".provider", file);
                m_intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(m_intent, 2);

                Log.e("EVENT_LIST_STR", String.valueOf(count));
                Log.e("EVENT_LIST_STR_SHared", String.valueOf(countInt));

            }
        });

    }


    private void getTasks() {
        class GetTasks extends AsyncTask<Void, Void, List<EventCapture>> {

            @Override
            protected List<EventCapture> doInBackground(Void... voids) {
                List<EventCapture> taskList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .taskDao()
                        .getAll();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 2:
                if (resultCode == RESULT_OK) {

                    String EventFileName = "EventCapture" + count + ".jpeg";
                    File file = new File(getExternalCacheDir().getPath(), EventFileName);
                    Uri uri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", file);
                    eventListStr = String.valueOf(uri);
                    Log.e("EVENT_LIST_STR", eventListStr);
                    Log.e("EVENT_LIST_STR", String.valueOf(count));
                    /* mShaeShared_common_pref.save("COUNT", String.valueOf(task.getmQuantity()));*/

                    SharedPreferences.Editor myEdit
                            = sharedPreferences.edit();
                    myEdit.putInt("age", count);
                    myEdit.commit();

                    saveTask();
                    getMulipart(eventListStr, 0);
                }
                break;
        }
    }


    private void saveTask() {
        final String sTask = eventListStr;
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


}
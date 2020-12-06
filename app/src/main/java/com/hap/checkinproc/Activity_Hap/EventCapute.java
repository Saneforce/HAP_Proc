package com.hap.checkinproc.Activity_Hap;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Model_Class.EventCapture;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.EventCaptureAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventCapute extends AppCompatActivity {


    Button TakeEventPicture;
    TextView txtRetailerName, txtRoute, txtDistributorName;
    int count = 0, count1 = 0;
    ArrayList<Uri> eventList = new ArrayList<Uri>();
    ArrayList<EventCapture> eventCapture;
    EventCapture evC;
    RecyclerView mEventCapture;
    EventCaptureAdapter mEventCaptureAdapter;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_capute);
        initialize();
    }


    private void initialize() {
        Intent intnet = getIntent();
        String EventOne = intnet.getStringExtra("EventcapOne");
        Log.e("EventcapOne", EventOne);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        eventList.add(Uri.parse(EventOne));
        eventCapture = new ArrayList<>();
        TakeEventPicture = findViewById(R.id.btn_take_photo);
        txtRetailerName = findViewById(R.id.txt_reatiler_name);
        txtRoute = findViewById(R.id.txt_route);
        txtDistributorName = findViewById(R.id.txt_distributor_name);

        mEventCapture = findViewById(R.id.event_capture_list);


        mEventCapture.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mEventCapture.setLayoutManager(layoutManager);
        mEventCapture.setNestedScrollingEnabled(false);

        TakeEventPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count1++;
                String EventFileName = "EventCapture" + count1 + ".jpeg";
                Intent m_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = new File(getExternalCacheDir().getPath(), EventFileName);
                Uri uri = FileProvider.getUriForFile(EventCapute.this, getApplicationContext().getPackageName() + ".provider", file);
                m_intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(m_intent, 2);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 2:
                if (resultCode == RESULT_OK) {
                    count++;
                    String EventFileName = "EventCapture" + count + ".jpeg";
                    File file = new File(getExternalCacheDir().getPath(), EventFileName);
                    Uri uri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", file);
                    /*eventList.add(uri);
                    String eventListStr = eventList.toString();
                    Log.e("EVENT_LIST_STR", eventListStr);


                    for (int i = 0; i < count; i++) {
                        evC = new EventCapture(i, uri);
                    }

                    eventCapture.add(evC);
                    mEventCaptureAdapter = new EventCaptureAdapter(eventCapture, EventCapute.this);
                    mEventCapture.setAdapter(mEventCaptureAdapter);

                    Log.e("List_value", String.valueOf(eventCapture.size()));
                    Log.e("List_value", String.valueOf(eventCapture));
*/
                }
                break;
        }
    }





    /*Call Api Image*/
    public void CallApiImage(HashMap<String, RequestBody> values, MultipartBody.Part imgg, final int x) {
        Call<ResponseBody> imageCall;
        imageCall = apiInterface.uploadimg(values, imgg);
        imageCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.v("print_upload_file", "ggg" + response.isSuccessful() + response.body());

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }


}
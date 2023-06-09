package com.hap.checkinproc.SFA_Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.CancelledOrderHistoryAdapter;
import com.hap.checkinproc.SFA_Adapter.CommonAdapterForDateRange;
import com.hap.checkinproc.SFA_Model_Class.CommonModelWithOneString;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CancelledOrdersActivity extends AppCompatActivity {
    TextView fromDateTV, toDateTV;
    LottieAnimationView progressBar, animationView;
    Context context;
    Common_Class common_class;
    Shared_Common_Pref shared_common_pref;
    String date;
    DatePickerDialog fromDatePickerDialog;
    RecyclerView recyclerView;
    ImageView showMore, home;
    ArrayList<CommonModelWithOneString> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelled_orders);

        context = this;
        common_class = new Common_Class(this);
        shared_common_pref = new Shared_Common_Pref(this);

        home = findViewById(R.id.toolbar_home);
        fromDateTV = findViewById(R.id.fromDateTV);
        toDateTV = findViewById(R.id.toDateTV);
        progressBar = findViewById(R.id.progressbar);
        recyclerView = findViewById(R.id.recyclerView);
        showMore = findViewById(R.id.showMore);
        animationView = findViewById(R.id.animationView);

        common_class.gotoHomeScreen(context, home);
        String today = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        fromDateTV.setText(today);
        toDateTV.setText(today);

        fromDateTV.setOnClickListener(v -> selectDate(1));
        toDateTV.setOnClickListener(v -> selectDate(2));

        list = new ArrayList<>();
        list.add(new CommonModelWithOneString("Today"));
        list.add(new CommonModelWithOneString("Last 7 days"));
        list.add(new CommonModelWithOneString("Last 30 days"));

        showMore.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.common_dialog_with_rv, null, false);
            builder.setView(view);
            builder.setCancelable(false);
            RecyclerView recyclerView1 = view.findViewById(R.id.recyclerView);
            recyclerView1.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            CommonAdapterForDateRange adapter = new CommonAdapterForDateRange(context, list);
            recyclerView1.setAdapter(adapter);
            TextView title = view.findViewById(R.id.title);
            title.setText("Select Quick Dates");
            TextView close = view.findViewById(R.id.close);
            AlertDialog dialog = builder.create();
            adapter.setItemSelect(name -> {
                Calendar calendar = Calendar.getInstance();
                if (name.contains("Today")) {
                    calendar.add(Calendar.DATE, -0);
                } else if (name.contains("7")) {
                    calendar.add(Calendar.DATE, -7);
                } else if (name.contains("30")) {
                    calendar.add(Calendar.DATE, -30);
                }
                fromDateTV.setText(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
                toDateTV.setText(Common_Class.GetDatewothouttime());
                getDataFromAPI();
                dialog.dismiss();
            });
            close.setOnClickListener(v1 -> dialog.dismiss());
            dialog.show();
        });

        getDataFromAPI();
    }

    void selectDate(int val) {
        Calendar calendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
            date = ("" + year + "-" + new DecimalFormat("00").format(monthOfYear + 1) + "-" + new DecimalFormat("00").format(dayOfMonth));
            if (val == 1) {
                if (common_class.checkDates(date, toDateTV.getText().toString(), (Activity) context) || toDateTV.getText().toString().equals("")) {
                    fromDateTV.setText(date);
                    getDataFromAPI();
                } else {
                    common_class.showMsg((Activity) context, "Please select valid date");
                }
            } else {
                if (common_class.checkDates(fromDateTV.getText().toString(), date, (Activity) context) || fromDateTV.getText().toString().equals("")) {
                    toDateTV.setText(date);
                    getDataFromAPI();
                } else {
                    common_class.showMsg((Activity) context, "Please select valid date");
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();
        fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    private void getDataFromAPI() {
        animationView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        String FROM = fromDateTV.getText().toString().trim();
        String TO = toDateTV.getText().toString().trim();

        if (TextUtils.isEmpty(FROM)) {
            Toast.makeText(context, "Please select FROM date", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(TO)) {
            Toast.makeText(context, "Please select TO date", Toast.LENGTH_SHORT).show();
        } else {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Map<String, String> params = new HashMap<>();
            params.put("axn", "get_cancelled_orders");
            params.put("fromDate", FROM);
            params.put("toDate", TO);
            params.put("sfCode", Shared_Common_Pref.Sf_Code);
            params.put("distributorId", shared_common_pref.getvalue(Constants.Distributor_Id));
            Call<ResponseBody> call = apiInterface.getUniversalData(params);
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            if (response.body() == null) {
                                Toast.makeText(context, "Error 1: Response is Null", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String result = response.body().string();
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getBoolean("success")) {
                                JSONArray array = jsonObject.getJSONArray("response");
                                if (array.length() != 0) {
                                    recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                                    CancelledOrderHistoryAdapter adapter = new CancelledOrderHistoryAdapter(array, context);
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.setVisibility(View.VISIBLE);
                                } else {
                                    animationView.setVisibility(View.VISIBLE);
                                }
                            } else {
                                Toast.makeText(context, "Error 2: Response Not Success", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(context, "Error 3: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Error 4: Response Not Success", Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    Toast.makeText(context, "Error 5: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }
}
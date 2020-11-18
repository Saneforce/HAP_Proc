package com.hap.checkinproc.Common_Class;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class Common_Class {

    Intent intent;
    Activity activity;
    Dialog dialog_invitation = null;
    public Context context;
    Shared_Common_Pref shared_common_pref;
    ProgressDialog nDialog;
    Type userType;
    ;
    Gson gson;

    // Gson gson;
    String Result = "false";
    public static String Version_Name="Ver 2.3.2";
    public static String Work_Type="0";
    public void CommonIntentwithFinish(Class classname) {
        intent = new Intent(activity, classname);

        activity.startActivity(intent);
        activity.finish();
    }

    public Common_Class(Context context) {
        this.context = context;
        shared_common_pref = new Shared_Common_Pref(context);

    }


    public Common_Class(Activity activity) {
        this.activity = activity;
        nDialog = new ProgressDialog(activity);
        shared_common_pref = new Shared_Common_Pref(activity);

    }

    public void ProgressdialogShow(int flag, String message) {

        if (flag == 1) {
            nDialog.setMessage("Loading.......");
            nDialog.setTitle(message);
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();


        } else {
            nDialog.dismiss();
        }
    }


    public boolean isNetworkAvailable(final Context context) {
        this.context = context;

        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();

            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }


    public void makeCall(int mobilenumber) {
        final int REQUEST_PHONE_CALL = 1;
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + mobilenumber));
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
        } else {
            activity.startActivity(callIntent);
        }


    }


    public JsonArray FilterGson(final Iterable<JsonObject> SrcArray, String colName, String searchVal) {
        JsonArray ResArray = new JsonArray();
        for (JsonObject jObj : SrcArray) {
            if (jObj.get(colName).getAsString().equalsIgnoreCase(searchVal)) {
                ResArray.add(jObj);
            }
        }
        return ResArray;
    }
   /* public void Reurnypeface(class cl,){
        userType = new TypeToken<ArrayList<Work_Type_Model>>() {
        }.getType();
        worktypelist = gson.fromJson(new Gson().toJson(noticeArrayList), userType);

    }*/


//    public void CustomerMe(final Context context_) {
//        this.context = context_;
//        shared_common_pref = new Shared_Common_Pref(activity);
//        gson = new Gson();
//        apiService = ApiClient.getClient().create(ApiInterface.class);
//        Type type = new TypeToken<List<CustomerMe>>() {
//        }.getType();
//        System.out.println("TYPETOKEN_LIST" + type);
//        CustomerMeList = gson.fromJson(shared_common_pref.getvalue(Shared_Common_Pref.cards_pref), type);
//        JSONObject paramObject = new JSONObject();
//        try {
//            paramObject.put("name","dd");
//            paramObject.put("password","sddfdf");
//
//            Call<JsonObject> Callto = apiService.LoginJSON(paramObject.toString());
//            Callto.enqueue(CheckUser);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//            System.out.println("JSON Expections" + paramObject.toString());
//
//        }
//
//
//    }


    /* public void showToastMSG(Activity Ac, String MSg, int s) {
         TastyToast.makeText(Ac, MSg,
                 TastyToast.LENGTH_SHORT, s);
     }*/
    public static boolean isNullOrEmpty(String str) {
        if (str != null && !str.isEmpty())
            return false;
        return true;
    }

    public void CommonIntentwithNEwTask(Class classname) {
        intent = new Intent(activity, classname);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }


    public void hideKeybaord(View v, Context context) {
        this.context = context;
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
    }

    public static String addquote(String s) {
        return new StringBuilder()
                .append('\'')
                .append(s)
                .append('\'')
                .toString();
    }

    public String GetMonthname(int s) {
        String[] montharray = activity.getResources().getStringArray(R.array.MonthArray);
        Calendar cal = Calendar.getInstance();
        if (s == 12) {
            s = 0;

        }
        String currrentmonth = montharray[s];
        return currrentmonth;
    }

    public void CommonIntentwithoutFinishputextra(Class classname, String key, String value) {
        intent = new Intent(activity, classname);
        intent.putExtra(key, value);
        Log.e("commanclasstitle", value);
        activity.startActivity(intent);
    }

    public String getintentValues(String name) {
        Intent intent = activity.getIntent();
        return intent.getStringExtra(name);
    }

    public static String GetDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dpln = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String plantime = dpln.format(c.getTime());
        return plantime;
    }

    public void GetTP_Result(String name, String values, int Month) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        JSONObject sp = new JSONObject();
        try {
            jsonObject.put(name, sp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonArray.put(jsonObject);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> mCall = apiInterface.GetResponseBody("3", "MGR4762", "MGR4762", "24", String.valueOf(Month), "2020", jsonArray.toString());
        Log.e("Log_Tp_SELECT", jsonArray.toString());
        mCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // locationList=response.body();

                Log.e("TAG_TP_RESPONSE", "response Tp_View: " + new Gson().toJson(response.body()));

                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    Result = jsonObject.getString("success");
                    Toast.makeText(activity, "Send to Approval", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


    }


   /* public static ArrayList <T> getFilterAray(Filterlist,) {
        Collection<String> filtered = Collections2.filter(list,
                Predicates.containsPattern("How"));

        return  filtered;
    }*/
}


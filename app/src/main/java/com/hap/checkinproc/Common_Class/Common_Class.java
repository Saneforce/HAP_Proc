package com.hap.checkinproc.Common_Class;


import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.hap.checkinproc.Activity_Hap.Leave_Request.CheckInfo;
import static com.hap.checkinproc.Common_Class.Constants.Category_List;
import static com.hap.checkinproc.Common_Class.Constants.Competitor_List;
import static com.hap.checkinproc.Common_Class.Constants.Distributor_List;
import static com.hap.checkinproc.Common_Class.Constants.Outlet_Total_Orders;
import static com.hap.checkinproc.Common_Class.Constants.Product_List;
import static com.hap.checkinproc.Common_Class.Constants.Retailer_OutletList;
import static com.hap.checkinproc.Common_Class.Constants.Rout_List;
import static com.hap.checkinproc.Common_Class.Constants.TodayOrderDetails_List;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Activity_Hap.Dashboard;
import com.hap.checkinproc.Activity_Hap.SFA_Activity;
import com.hap.checkinproc.Interface.AlertBox;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.UpdateResponseUI;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Activity.HistoryInfoActivity;
import com.hap.checkinproc.SFA_Model_Class.OutletReport_View_Modal;
import com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List;
import com.hap.checkinproc.common.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Common_Class {

    Intent intent;
    Activity activity;
    Dialog dialog_invitation = null;
    public Context context;
    Shared_Common_Pref shared_common_pref;
    ProgressDialog nDialog;
    Type userType;
    Gson gson;

    // Gson gson;
    String Result = "false";
    public static String Version_Name = "ver 3.1.19";
    public static String Work_Type = "0";
    public static int count;

    private List<Retailer_Modal_List> retailer_modal_list = new ArrayList<>();
    private UpdateResponseUI updateUi;
    Type userTypeRetailor;

    List<OutletReport_View_Modal> outletReport_view_modalList = new ArrayList<>();
    private Type userTypeGetTodayOrder;
    private DatePickerDialog fromDatePickerDialog;

    String pickDate = "";

    public void CommonIntentwithFinish(Class classname) {
        intent = new Intent(activity, classname);

        activity.startActivity(intent);
        activity.finish();
    }

    public Common_Class(Context context) {
        this.context = context;
        shared_common_pref = new Shared_Common_Pref(context);

    }

    public static String GetDatemonthyearformat() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dpln = new SimpleDateFormat("dd-MM-yyyy");
        String plantime = dpln.format(c.getTime());
        return plantime;
    }

    public void CommonIntentwithoutFinish(Class classname) {
        intent = new Intent(activity, classname);
        activity.startActivity(intent);
    }

    public Common_Class(Activity activity) {
        this.activity = activity;
        nDialog = new ProgressDialog(activity);
        shared_common_pref = new Shared_Common_Pref(activity);
    }

    public void ProgressdialogShow(int flag, String message) {

        if (flag == 1) {
            nDialog.setMessage("Loading.......");
            if (message.length() > 1) {
                nDialog.setTitle(message);
                nDialog.setCancelable(true);

            }
            nDialog.setIndeterminate(false);
            nDialog.show();

            if (message.equals("")) {
                nDialog.setCancelable(false);
                nDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                nDialog.setContentView(R.layout.loading_progress_bottom);
            }


        } else {
            nDialog.dismiss();
        }
    }

    public static String GetDateOnly() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dpln = new SimpleDateFormat("yyyy-MM-dd");
        String plantime = dpln.format(c.getTime());
        return plantime;
    }

    public static String GetDay() {
        Date dd = new Date();
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE");

        return simpleDateformat.format(dd);
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

    public static String GetDatewothouttime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dpln = new SimpleDateFormat("yyyy-MM-dd");
        String plantime = dpln.format(c.getTime());
        return plantime;
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

    public static double ParseDouble(String strNumber) {
        if (strNumber != null && strNumber.length() > 0) {
            try {
                return Double.parseDouble(strNumber);
            } catch (Exception e) {
                return -1;   // or some value to mark this field is wrong. or make a function validates field first ...
            }
        } else return 0;
    }

    public static double Check_Distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public List<Common_Model> getfilterList(java.util.List<Common_Model> Jointworklistview) {
        List<Common_Model> Jointworklistviewsave = new ArrayList<>();
        for (int i = 0; i < Jointworklistview.size(); i++) {
            if (Jointworklistview.get(i).isSelected()) {
                Log.e("SELECTED", String.valueOf(Jointworklistview.get(i).isSelected()));
                Jointworklistviewsave.add(new Common_Model(Jointworklistview.get(i).getName(), Jointworklistview.get(i).getId(), true));
            }

        }

        return Jointworklistviewsave;
    }

    ;


    public JsonArray FilterGson(final Iterable<JsonObject> SrcArray, String colName, String searchVal) {
        JsonArray ResArray = new JsonArray();
        for (JsonObject jObj : SrcArray) {
            if (jObj.get(colName).getAsString().equalsIgnoreCase(searchVal)) {
                ResArray.add(jObj);
            }
        }
        return ResArray;
    }

    public void getDataFromApi(String key, Activity activity, Boolean boolRefresh) {

        if (isNetworkAvailable(activity)) {
            String QuerySTring1 = "";
            Map<String, String> QueryString = new HashMap<>();
            String axnname = "table/list";

            switch (key) {

                case (Retailer_OutletList):
                    QuerySTring1 = "{\"tableName\":\"vwDoctor_Master_APP\",\"coloumns\":\"[\\\"doctor_code as id\\\", \\\"doctor_name as name\\\",\\\"Type\\\",\\\"DelivType\\\"," +
                            " \\\"reason_category\\\", \\\"StateCode\\\",\\\"Tcs\\\",\\\"Tds\\\",\\\"Outlet_Type\\\",\\\"town_code\\\", \\\"ListedDr_Email\\\",\\\"cityname\\\",\\\"Owner_Name\\\",\\\"Category\\\",\\\"Speciality\\\",\\\"Class\\\",\\\"ERP_Code\\\",\\\"town_name\\\"," +
                            "\\\"lat\\\",\\\"long\\\", \\\"pin_code\\\", \\\"gst\\\",   \\\"Hatsanavail_Switch\\\"  , \\\"HatsanCategory_Switch\\\"," +
                            "\\\"addrs\\\",\\\"ListedDr_Address1\\\",\\\"ListedDr_Sl_No\\\",   \\\"Compititor_Id\\\", \\\"Compititor_Name\\\", " +
                            " \\\"LastUpdt_Date\\\",    \\\"Mobile_Number\\\",\\\"Statusname\\\" ,\\\"Invoice_Flag\\\" , \\\"InvoiceValues\\\" ," +
                            " \\\"Valuesinv\\\" , \\\"InvoiceDate\\\", \\\"Category_Universe_Id\\\", \\\"Hatsun_AvailablityId\\\",   " +
                            "\\\"Doc_cat_code\\\",\\\"ContactPersion\\\",\\\"Doc_Special_Code\\\",\\\"Distributor_Code\\\"]\",\"where\":\"" +
                            "[\\\"isnull(Doctor_Active_flag,0)=0\\\"]\",\"orderBy\":\"[\\\"OutletOrder asc\\\",\\\"doctor_name asc\\\"]\",\"desig\":\"stockist\"}";


                    break;
                case (Constants.Distributor_List):
                    ProgressdialogShow(1, "Data Syncing");
                    QuerySTring1 = "{\"tableName\":\"vwstockiest_Master_APP\",\"coloumns\":\"[\\\"distributor_code as id\\\",\\\"StateCode \\\", \\\"stockiest_name as name\\\",\\\"town_code\\\",\\\"town_name\\\",\\\"Addr1\\\",\\\"Addr2\\\",\\\"City\\\",\\\"Pincode\\\",\\\"GSTN\\\",\\\"lat\\\",\\\"long\\\",\\\"addrs\\\",\\\"Tcode\\\",\\\"Dis_Cat_Code\\\"]\",\"where\":\"[\\\"isnull(Stockist_Status,0)=0\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    break;
                case (Constants.Category_List):
                    QuerySTring1 = "{\"tableName\":\"category_universe\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    break;
                case (Constants.Product_List):
                    QuerySTring1 = "{\"tableName\":\"getproduct_details\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0," +
                            "\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    break;
                case (Constants.Rout_List):
                    QuerySTring1 = "{\"tableName\":\"vwTown_Master_APP\",\"coloumns\":\"[\\\"town_code as id\\\", \\\"town_name as name\\\",\\\"target\\\",\\\"min_prod\\\",\\\"field_code\\\",\\\"stockist_code\\\"]\",\"where\":\"[\\\"isnull(Town_Activation_Flag,0)=0\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    break;

                case Constants.GetTodayOrder_List:
                    QuerySTring1 = "{\"tableName\":\"gettotalorderbytoday\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    QueryString.put("fromdate", com.hap.checkinproc.Common_Class.Common_Class.GetDatewothouttime());
                    QueryString.put("todate", com.hap.checkinproc.Common_Class.Common_Class.GetDatewothouttime());
                    break;

                case Constants.Outlet_Total_Orders:
                    QuerySTring1 = "{\"tableName\":\"gettotaloutletorders\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    QueryString.put("fromdate", com.hap.checkinproc.Common_Class.Common_Class.GetDatewothouttime());
                    QueryString.put("todate", com.hap.checkinproc.Common_Class.Common_Class.GetDatewothouttime());
                    break;
                case Constants.TodayOrderDetails_List:
                    QuerySTring1 = "{\"tableName\":\"GettotalOrderDetails\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    QueryString.put("fromdate", Common_Class.GetDatewothouttime());
                    QueryString.put("todate", Common_Class.GetDatewothouttime());
                    QueryString.put("orderID", Shared_Common_Pref.TransSlNo);
                    break;

                case Constants.Competitor_List:
                    QuerySTring1 = "{\"tableName\":\"get_compititordetails\"}";

                    break;
                case Constants.Todaydayplanresult:
                    axnname = "Get/dayplanresult";
                    QueryString.put("Date", Common_Class.GetDatewothouttime());
                    break;
                case Constants.Outlet_Total_AlldaysOrders:
                    QuerySTring1 = "{\"tableName\":\"gettotalalldaysoutletorders\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
                    QueryString.put("fromdate", Common_Class.GetDatewothouttime());
                    QueryString.put("todate", Common_Class.GetDatewothouttime());


                    break;
            }

            QueryString.put("axn", axnname);
            QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
            QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
            QueryString.put("rSF", Shared_Common_Pref.Sf_Code);
            QueryString.put("State_Code", Shared_Common_Pref.StateCode);
            QueryString.put("desig", "stockist");
            QueryString.put(Constants.Distributor_Id, shared_common_pref.getvalue(Constants.Distributor_Id));

            callAPI(QuerySTring1, QueryString, key, activity, boolRefresh);
        } else {
            Toast.makeText(activity, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        }


    }

    void callAPI(String QuerySTring1, Map<String, String> QueryString, String key, Activity activity, Boolean boolRefresh) {
        try {
            DatabaseHandler db = new DatabaseHandler(activity);

            ApiInterface service = ApiClient.getClient().create(ApiInterface.class);


            Call<Object> call = service.GetRouteObject(QueryString, QuerySTring1);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    try {
                        Gson gson = new Gson();


                        if (shared_common_pref == null)
                            shared_common_pref = new Shared_Common_Pref(activity);

                        if (key.equals(Retailer_OutletList)) {
                            updateUi = ((UpdateResponseUI) activity);

                            shared_common_pref.save(key, gson.toJson(response.body()));

                            userTypeRetailor = new TypeToken<ArrayList<Retailer_Modal_List>>() {
                            }.getType();

                            retailer_modal_list = new ArrayList<>();
                            retailer_modal_list.clear();
                            retailer_modal_list = gson.fromJson(shared_common_pref.getvalue(Retailer_OutletList), userTypeRetailor);
                            if (retailer_modal_list != null)
                                updateUi.onLoadFilterData(retailer_modal_list);

                        } else {

                            db.deleteMasterData(key);
                            db.addMasterData(key, gson.toJson(response.body()));

                        }


                        if (key.equals(Constants.GetTodayOrder_List)) {

                            updateUi = ((UpdateResponseUI) activity);

                            String OrdersTable = String.valueOf(db.getMasterData(Constants.GetTodayOrder_List));
                            userTypeGetTodayOrder = new TypeToken<ArrayList<OutletReport_View_Modal>>() {
                            }.getType();
                            outletReport_view_modalList = gson.fromJson(OrdersTable, userTypeGetTodayOrder);

                            updateUi.onLoadTodayOrderList(outletReport_view_modalList);
                        }

                        if (key.equals(TodayOrderDetails_List) || key.equals(Competitor_List)) {

                            updateUi = ((UpdateResponseUI) activity);

                            updateUi.onLoadDataUpdateUI(gson.toJson(response.body()), key);
                        }


                        switch (key) {
                            //case Retailer_OutletList:

                            // getDataFromApi(Constants.Distributor_List, activity, boolRefresh);
                            // break;
                            case Distributor_List:
                                getDataFromApi(Category_List, activity, boolRefresh);
                                break;
                            case Category_List:
                                getDataFromApi(Product_List, activity, boolRefresh);
                                break;
                            case Product_List:
                                getDataFromApi(Rout_List, activity, boolRefresh);
                                break;
                            case Rout_List:
                                if (boolRefresh)
                                    getDataFromApi(Constants.GetTodayOrder_List, activity, boolRefresh);

                                else {
                                    ProgressdialogShow(0, "Data Syncing");
                                    activity.startActivity(new Intent(activity, SFA_Activity.class));
                                }
                                break;

                            case Constants.GetTodayOrder_List:
                                if (boolRefresh)
                                    getDataFromApi(Outlet_Total_Orders, activity, boolRefresh);
                                break;
                            case Outlet_Total_Orders:
                                if (boolRefresh)
                                    getDataFromApi(TodayOrderDetails_List, activity, boolRefresh);
                                break;
                            case TodayOrderDetails_List:
                                if (boolRefresh)
                                    getDataFromApi(Competitor_List, activity, boolRefresh);
                                break;
                            case Competitor_List:
                                if (boolRefresh)
                                    getDataFromApi(Constants.Outlet_Total_AlldaysOrders, activity, boolRefresh);
                                break;
                            case Constants.Outlet_Total_AlldaysOrders:
                                if (boolRefresh)
                                    getDataFromApi(Constants.Todaydayplanresult, activity, boolRefresh);
                                break;
                            case Constants.Todaydayplanresult:
                                if (boolRefresh)
                                    CommonIntentwithFinish(SFA_Activity.class);
                                break;


                        }

                    } catch (Exception e) {

                        Log.e("Common class:", key + " response: " + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    Log.e("api response ex:", t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("api response ex:", e.getMessage());
        }
    }


    public void getDb_310Data(String key, Activity activity) {
        try {
            if (isNetworkAvailable(activity)) {
                Map<String, String> QueryString = new HashMap<>();
                String axnname = "";
                JSONObject data = new JSONObject();


                switch (key) {

                    case Constants.HistoryData:
                        axnname = "get/orderandinvoice";
                        data.put("distributorid", shared_common_pref.getvalue(Constants.Distributor_Id));
                        data.put("fdt", HistoryInfoActivity.stDate);
                        data.put("tdt", HistoryInfoActivity.endDate);

                        break;
                    case Constants.PAYMODES:
                        axnname = "get/paymenttype";
                        data.put("divisionCode", Shared_Common_Pref.Div_Code);
                        break;

                    case Constants.QPS_STATUS:
                        axnname = "get/popmaster";
                        break;

                    case Constants.OUTSTANDING:
                        axnname = "get/customeroutstanding";
                        data.put("retailerCode", Shared_Common_Pref.OutletCode);
                        break;

                    case Constants.POP_ENTRY_STATUS:
                        axnname = "get/popentrystatus";
                        data.put("retailerCode", Shared_Common_Pref.OutletCode);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + key);
                }

                QueryString.put("axn", axnname);


                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);


                Call<ResponseBody> call = service.GetRouteObject310(QueryString, data.toString());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        InputStreamReader ip = null;
                        StringBuilder is = new StringBuilder();
                        String line = null;
                        try {
                            if (response.isSuccessful()) {
                                ip = new InputStreamReader(response.body().byteStream());
                                BufferedReader bf = new BufferedReader(ip);
                                while ((line = bf.readLine()) != null) {
                                    is.append(line);
                                    Log.v("Res>>", is.toString());
                                }


                                shared_common_pref.save(key, is.toString());
                                updateUi = ((UpdateResponseUI) activity);
                                updateUi.onLoadDataUpdateUI(is.toString(), key);
                            }

                        } catch (Exception e) {

                            Log.v("fail>>1", e.getMessage());

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.v("fail>>2", t.toString());


                    }
                });
            } else {
                showMsg(activity, "Please check your internet connection.");
            }
        } catch (Exception e) {

        }

    }


    public void showMsg(Activity activity, String msg) {
        Toast toast = Toast.makeText(activity, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    public String datePicker(Activity activity, TextView view) {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int month = monthOfYear + 1;

                pickDate = ("" + dayOfMonth + "/" + month + "/" + year);


            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();

        return pickDate;
    }

    public void commonDialog(Activity activity, Class moveActivity) {
        AlertDialogBox.showDialog(activity, "HAP Check-In", "Do you confirm to cancel Cart?", "Yes", "No", false, new AlertBox() {
            @Override
            public void PositiveMethod(DialogInterface dialog, int id) {
                CommonIntentwithFinish(moveActivity);
            }

            @Override
            public void NegativeMethod(DialogInterface dialog, int id) {

            }
        });
    }


//    public boolean checkValueStore(Activity activity, String key) {
//        DatabaseHandler db = new DatabaseHandler(activity);
//
//        try {
//            JSONArray storeData = db.getMasterData(key);
//            if (storeData != null && storeData.length() > 0)
//                return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return false;
//    }
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

    public static String GetEkey() {
        DateFormat dateformet = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Calendar calander = Calendar.getInstance();
        return "EK" + Shared_Common_Pref.Sf_Code + dateformet.format(calander.getTime()).hashCode();

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

    public void CommonIntentwithoutFinishputextratwo(Class classname, String key, String value, String key2, String value2) {
        intent = new Intent(activity, classname);
        intent.putExtra(key, value);
        intent.putExtra(key2, value2);
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

    public void GetTP_Result(String name, String values, int Month, int year) {
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
        Call<JsonObject> mCall = apiInterface.GetResponseBody(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.StateCode, String.valueOf(Month), String.valueOf(year), jsonArray.toString());
        Log.e("Log_Tp_SELECT", jsonArray.toString());
        mCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
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

    public void GetTP_Result(String name, String values, String Month, String year) {
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
        Call<JsonObject> mCall = apiInterface.GetResponseBody(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.Sf_Code, Shared_Common_Pref.StateCode, String.valueOf(Month), String.valueOf(year), jsonArray.toString());

        mCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("TAG_TP_RESPONSE", "response Tp_View: " + new Gson().toJson(response.body()));

                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    Result = jsonObject.getString("success");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }


    public static class InputFilterMinMax implements InputFilter {

        private int min, max;

        public InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }


        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(dest.toString() + source.toString());
                if (isInRange(min, max, input))
                    return null;
            } catch (NumberFormatException nfe) {
            }
            return "";
        }

        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }

    }


    public void gotoHomeScreen(Context context, View ivToolbarHome) {


        ivToolbarHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences CheckInDetails = context.getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
                Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
                if (CheckIn == true) {
//                        Intent Dashboard = new Intent(getApplicationContext(), Dashboard_Two.class);
//                        Dashboard.putExtra("Mode", "CIN");
//                        startActivity(Dashboard);
                    CommonIntentwithoutFinish(SFA_Activity.class);
                } else
                    context.startActivity(new Intent(context, Dashboard.class));

            }
        });


    }


}


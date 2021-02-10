
package com.hap.checkinproc.Activity_Hap;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hap.checkinproc.Activity.AllowanceActivityTwo;
import com.hap.checkinproc.Activity.TAClaimActivity;
import com.hap.checkinproc.Common_Class.AlertDialogBox;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AlertBox;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;
import com.hap.checkinproc.Status_Activity.View_All_Status_Activity;
import com.hap.checkinproc.adapters.HomeRptRecyler;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard_Two extends AppCompatActivity implements View.OnClickListener {
    private static String Tag = "HAP_Check-In";
    SharedPreferences CheckInDetails;
    SharedPreferences UserDetails;
    public static final String CheckInDetail = "CheckInDetail";
    public static final String UserDetail = "MyPrefs";
    Shared_Common_Pref mShared_common_pref;

    private RecyclerView recyclerView;
    private HomeRptRecyler mAdapter;
    String viewMode = "";
    int cModMnth = 1;
    Button viewButton;
    CardView StActivity;
    String AllowancePrefernce = "";

    public static final String mypreference = "mypref";
    public static final String Name = "Allowance";
    public static final String MOT = "ModeOfTravel";
    public static final String SKM = "Started_km";

    String PrivacyScreen = "", ModeOfTravel = "";
    SharedPreferences sharedpreferences;


    public static final String hapLocation = "hpLoc";
    public static final String otherLocation = "othLoc";
    public static final String visitPurpose = "vstPur";
    public static final String modeTravelId = "ShareModesss";
    public static final String modeTypeVale = "SharedModeTypeValesss";
    public static final String modeFromKm = "SharedFromKmsss";
    public static final String modeToKm = "SharedToKmsss";
    public static final String StartedKm = "StartedKMsss";


    /*String Mode = "Bus";*/
    CardView gateIn_gateOut;

    String dashMdeCnt = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard__two);

        mShared_common_pref = new Shared_Common_Pref(this);

        mShared_common_pref.save("Dashboard", "one");

        dashMdeCnt = mShared_common_pref.getvalue("MC");
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        TextView txtHelp = findViewById(R.id.toolbar_help);
        ImageView imgHome = findViewById(R.id.toolbar_home);
        txtHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Help_Activity.class));
            }
        });

        TextView txtErt = findViewById(R.id.toolbar_ert);
        TextView txtPlaySlip = findViewById(R.id.toolbar_play_slip);

        txtErt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ERT.class));
            }
        });
        txtPlaySlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        ObjectAnimator textColorAnim;
        textColorAnim = ObjectAnimator.ofInt(txtErt, "textColor", Color.WHITE, Color.TRANSPARENT);
        textColorAnim.setDuration(500);
        textColorAnim.setEvaluator(new ArgbEvaluator());
        textColorAnim.setRepeatCount(ValueAnimator.INFINITE);
        textColorAnim.setRepeatMode(ValueAnimator.REVERSE);
        textColorAnim.start();

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!viewMode.equalsIgnoreCase("CIN"))
                    startActivity(new Intent(getApplicationContext(), Dashboard.class));
            }
        });
        CheckInDetails = getSharedPreferences(CheckInDetail, Context.MODE_PRIVATE);
        UserDetails = getSharedPreferences(UserDetail, Context.MODE_PRIVATE);

        TextView txUserName = findViewById(R.id.txUserName);
        String sUName = UserDetails.getString("SfName", "");
        txUserName.setText("HI! " + sUName);


        CardView cardview3 = findViewById(R.id.cardview3);
        CardView cardview4 = findViewById(R.id.cardview4);
        CardView cardView5 = findViewById(R.id.cardview5);
        gateIn_gateOut = findViewById(R.id.btn_gate_in);


        StActivity = findViewById(R.id.StActivity);
        CardView btnCheckout = findViewById(R.id.btnCheckout);
        cardview3.setOnClickListener(this);
        cardview4.setOnClickListener(this);
        cardView5.setOnClickListener(this);
        StActivity.setOnClickListener(this);
        btnCheckout.setOnClickListener(this);
        gateIn_gateOut.setOnClickListener(this);
    if(getIntent().getExtras()!=null){
        Bundle params = getIntent().getExtras();
        viewMode = params.getString("Mode");
     if (viewMode.equalsIgnoreCase("CIN") || viewMode.equalsIgnoreCase("extended")) {
        cardview3.setVisibility(View.VISIBLE);
        cardview4.setVisibility(View.VISIBLE);
        //cardView5.setVisibility(View.VISIBLE);
        StActivity.setVisibility(View.VISIBLE);
        btnCheckout.setVisibility(View.VISIBLE);
    } else {
        cardview3.setVisibility(View.GONE);
        cardview4.setVisibility(View.GONE);
        cardView5.setVisibility(View.GONE);
        StActivity.setVisibility(View.GONE);
        btnCheckout.setVisibility(View.GONE);
    }
    }else {
        cardview3.setVisibility(View.GONE);
        cardview4.setVisibility(View.GONE);
        cardView5.setVisibility(View.GONE);
        StActivity.setVisibility(View.GONE);
        btnCheckout.setVisibility(View.GONE);
    }


        getNotify();
        getDyReports();
        getMnthReports(0);
        GetMissedPunch();


        viewButton = findViewById(R.id.button3);
        viewButton.setOnClickListener(this);
        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });


    }

    private void getNotify() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> rptCall = apiInterface.getDataArrayList("get/notify",
                UserDetails.getString("Divcode", ""),
                UserDetails.getString("Sfcode", ""), "", "", null);
        rptCall.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray res = response.body();
                Log.d("NotifyMsg", response.body().toString());
                TextView txt = findViewById(R.id.MRQtxt);
                txt.setText("");
                txt.setVisibility(View.GONE);
                String sMsg = "";
                txt.setSelected(true);
                for (int il = 0; il < res.size(); il++) {
                    JsonObject Itm = res.get(il).getAsJsonObject();
                    sMsg += Itm.get("NtfyMsg").getAsString();
                }
                if (!sMsg.equalsIgnoreCase("")) {
                    txt.setText(Html.fromHtml(sMsg));
                    txt.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

                Log.d(Tag, String.valueOf(t));
            }
        });
    }

    private void getMnthReports(int m) {
        if (cModMnth == m) return;
        String[] mns = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        Common_Class Dt = new Common_Class();
        String sDt = Dt.GetDateTime(getApplicationContext(), "yyyy-MM-dd HH:mm:ss");
        Date dt = Dt.getDate(sDt);
        if (m == -1) {
            sDt = Dt.AddMonths(sDt, -1, "yyyy-MM-dd HH:mm:ss");
        }
        if (Dt.getDay(sDt) < 23) {
            sDt = Dt.AddMonths(sDt, -1, "yyyy-MM-dd HH:mm:ss");
        }
        int fmn = Dt.getMonth(sDt);
        sDt = Dt.AddMonths(Dt.getYear(sDt) + "-" + Dt.getMonth(sDt) + "-22 00:00:00", 1, "yyyy-MM-dd HH:mm:ss");
        int tmn = Dt.getMonth(sDt);
        Log.d(Tag, sDt + "-" + String.valueOf(fmn) + "-" + String.valueOf(tmn));
        TextView txUserName = findViewById(R.id.txtMnth);
        txUserName.setText("23," + mns[fmn - 1] + " - 22," + mns[tmn - 1]);

        // appendDS = appendDS + "&divisionCode=" + userData.divisionCode + "&sfCode=" + sSF + "&rSF=" + userData.sfCode + "&State_Code=" + userData.State_Code;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> rptMnCall = apiInterface.getDataArrayList("get/AttndMn", m,
                UserDetails.getString("Divcode", ""),
                UserDetails.getString("Sfcode", ""), UserDetails.getString("Sfcode", ""), "", "", null);
        rptMnCall.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray res = response.body();
                Log.d("Respose_data", String.valueOf(response.body()));
                JsonArray dyRpt = new JsonArray();
                for (int il = 0; il < res.size(); il++) {
                    JsonObject Itm = res.get(il).getAsJsonObject();
                    JsonObject newItem = new JsonObject();
                    newItem.addProperty("name", Itm.get("Status").getAsString());
                    newItem.addProperty("value", Itm.get("StatusCnt").getAsString());
                    newItem.addProperty("color", Itm.get("StusClr").getAsString().replace(" !important", ""));
                    dyRpt.add(newItem);
                }

                recyclerView = (RecyclerView) findViewById(R.id.Rv_MnRpt);
                mAdapter = new HomeRptRecyler(dyRpt, Dashboard_Two.this);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
                Log.d(Tag, String.valueOf(response.body()));
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d(Tag, String.valueOf(t));
            }
        });
    }

    private void getDyReports() {
        // appendDS = appendDS + "&divisionCode=" + userData.divisionCode + "&sfCode=" + sSF + "&rSF=" + userData.sfCode + "&State_Code=" + userData.State_Code;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> rptCall = apiInterface.getDataArrayList("get/AttnDySty",
                UserDetails.getString("Divcode", ""),
                UserDetails.getString("Sfcode", ""), "", "", null);
        rptCall.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray res = response.body();
                if (res.size() < 1) {
                    Toast.makeText(getApplicationContext(), "No Records Today", Toast.LENGTH_LONG).show();
                    return;
                }
                JsonObject fItm = res.get(0).getAsJsonObject();
                TextView txDyDet = findViewById(R.id.lTDyTx);
                txDyDet.setText(Html.fromHtml(fItm.get("AttDate").getAsString() + "<br><small>" + fItm.get("AttDtNm").getAsString() + "</small>"));
                JsonArray dyRpt = new JsonArray();
                JsonObject newItem = new JsonObject();
                newItem.addProperty("name", "Shift");
                newItem.addProperty("value", fItm.get("SFT_Name").getAsString());
                newItem.addProperty("color", "#333333");
                dyRpt.add(newItem);
                newItem = new JsonObject();
                newItem.addProperty("name", "Status");
                newItem.addProperty("value", fItm.get("DayStatus").getAsString());
                newItem.addProperty("color", fItm.get("StaColor").getAsString());
                dyRpt.add(newItem);
                newItem = new JsonObject();
                newItem.addProperty("name", "Check-In");
                newItem.addProperty("value", fItm.get("AttTm").getAsString());
                newItem.addProperty("color", "#333333");
                dyRpt.add(newItem);
                if (!fItm.get("ET").isJsonNull()) {
                    newItem = new JsonObject();
                    newItem.addProperty("name", "Last Check-Out");
                    newItem.addProperty("value", fItm.get("ET").getAsString());
                    newItem.addProperty("color", "#333333");
                    dyRpt.add(newItem);
                }
                newItem = new JsonObject();
                newItem.addProperty("name", "Geo In");
                newItem.addProperty("value", fItm.get("GeoIn").getAsString());
                newItem.addProperty("color", "#333333");
                newItem.addProperty("type", "geo");
                dyRpt.add(newItem);
                newItem = new JsonObject();
                newItem.addProperty("name", "Geo Out");
                newItem.addProperty("value", fItm.get("GeoOut").getAsString());//"<a href=\"https://www.google.com/maps?q="+fItm.get("GeoOut").getAsString()+"\">"+fItm.get("GeoOut").getAsString()+"</a>");
                newItem.addProperty("color", "#333333");
                newItem.addProperty("type", "geo");

                dyRpt.add(newItem);


                recyclerView = (RecyclerView) findViewById(R.id.Rv_DyRpt);
                mAdapter = new HomeRptRecyler(dyRpt, Dashboard_Two.this);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

                Log.d(Tag, String.valueOf(t));
            }
        });
        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(Dashboard_Two.this, "There is no back action", Toast.LENGTH_LONG).show();
    }

    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);

                    Log.d(Tag, String.valueOf(CheckIn));
                    if (CheckIn != true) {
                        Dashboard_Two.super.onBackPressed();
                    }
                    //
                }
            });

    private void GetMissedPunch() {
        // appendDS = appendDS + "&divisionCode=" + userData.divisionCode + "&sfCode=" + sSF + "&rSF=" + userData.sfCode + "&State_Code=" + userData.State_Code;
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> modelCall = apiInterface.getDataList("CheckWeekofandmis",
                UserDetails.getString("Divcode", ""),
                UserDetails.getString("Sfcode", ""), "", "", null);
        modelCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d(Tag, String.valueOf(response.body()));
                JsonObject itm = response.body().getAsJsonObject();
                String mMessage = "";
                try {
                    mMessage = itm.get("Msg").getAsString();
                    JsonArray MissedItems = itm.getAsJsonArray("GetMissed");
                    if (MissedItems.size() > 0) {
                        AlertDialog alertDialog = new AlertDialog.Builder(Dashboard_Two.this)
                                .setTitle("HAP Check-In")
                                .setMessage(Html.fromHtml(mMessage))
                                .setCancelable(false)
                                .setPositiveButton("Missed Punch Request", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        JsonObject mItem = MissedItems.get(0).getAsJsonObject();
                                        Intent mIntent = new Intent(Dashboard_Two.this, Missed_Punch.class);
                                        mIntent.putExtra("EDt", mItem.get("name").getAsString());
                                        mIntent.putExtra("Shift", mItem.get("name1").getAsString());
                                        mIntent.putExtra("CInTm", mItem.get("CInTm").getAsString());
                                        mIntent.putExtra("COutTm", mItem.get("COutTm").getAsString());

                                        Dashboard_Two.this.startActivity(mIntent);

                                        //((AppCompatActivity) Dashboard_Two.this).finish();
                                    }
                                })
                                .show();

                    } else {

                        JsonArray WKItems = itm.getAsJsonArray("CheckWK");
                        if (WKItems.size() > 0) {
                            if (itm.get("WKFlg").getAsInt() == 1) {
                                Log.d("WEEKOFF", String.valueOf(itm.get("WKFlg").getAsInt()));
                                AlertDialog alertDialog = new AlertDialog.Builder(Dashboard_Two.this)
                                        .setTitle("HAP Check-In")
                                        .setMessage(Html.fromHtml(mMessage))
                                        .setCancelable(false)
                                        .setPositiveButton("Weekoff", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                JsonObject mItem = WKItems.get(0).getAsJsonObject();
                                                Intent iWeekOff = new Intent(Dashboard_Two.this, Weekly_Off.class);
                                                iWeekOff.putExtra("EDt", mItem.get("EDt").getAsString());
                                                Dashboard_Two.this.startActivity(iWeekOff);

                                                ((AppCompatActivity) Dashboard_Two.this).finish();
                                            }
                                        }).setNegativeButton("Others", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                JsonObject mItem = WKItems.get(0).getAsJsonObject();
                                                Intent iLeave = new Intent(Dashboard_Two.this, Leave_Request.class);
                                                iLeave.putExtra("EDt", mItem.get("EDt").getAsString());
                                                Dashboard_Two.this.startActivity(iLeave);

                                                ((AppCompatActivity) Dashboard_Two.this).finish();
                                            }
                                        })
                                        .show();
                            } else {
                                AlertDialog alertDialog = new AlertDialog.Builder(Dashboard_Two.this)
                                        .setTitle("HAP Check-In")
                                        .setMessage(Html.fromHtml(mMessage))
                                        .setCancelable(false)
                                        .setPositiveButton("Others", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {


                                                JsonObject mItem = WKItems.get(0).getAsJsonObject();
                                                Intent iLeave = new Intent(Dashboard_Two.this, Leave_Request.class);
                                                iLeave.putExtra("EDt", mItem.get("EDt").getAsString());
                                                Dashboard_Two.this.startActivity(iLeave);

                                                ((AppCompatActivity) Dashboard_Two.this).finish();
                                            }
                                        })
                                        .show();
                            }
                        }
                    }
                } catch (Exception e) {
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }

        });
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.cardview3:
                intent = new Intent(this, Leave_Dashboard.class);
                break;
            case R.id.cardview4:
                intent = new Intent(this, TAClaimActivity.class);
                break;
            case R.id.cardview5:
                intent = new Intent(this, Reports.class);
                break;
            case R.id.btn_gate_in:
                startActivity(new Intent(this, QRCodeScanner.class));
                break;
            case R.id.StActivity:
                new AlertDialog.Builder(Dashboard_Two.this)
                        .setTitle("HAP Check-In")
                        .setMessage(Html.fromHtml("Are you sure to start your Today Activity Now ?"))
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent aIntent;
                                String sDeptType = UserDetails.getString("DeptType", "");
                                Log.d("DeptType", sDeptType);

                                if (sDeptType.equalsIgnoreCase("1")) {
                                    aIntent = new Intent(getApplicationContext(), OrderDashBoard.class);
                                } else {
                                    aIntent = new Intent(getApplicationContext(), OrderDashBoard.class);
                                }
                                startActivity(aIntent);
                                //((AppCompatActivity) Dashboard_Two.this).finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
                break;
            case R.id.button3:
                intent = new Intent(this, View_All_Status_Activity.class);
                break;
            case R.id.btnCheckout:
                AlertDialogBox.showDialog(Dashboard_Two.this, "HAP Check-In", "Do you want to Checkout?", "Yes", "No", false, new AlertBox() {
                    @Override
                    public void PositiveMethod(DialogInterface dialog, int id) {
                        if (sharedpreferences.contains(Name) && sharedpreferences.contains(MOT)) {
                            PrivacyScreen = sharedpreferences.getString(Name, "");
                            ModeOfTravel = sharedpreferences.getString(MOT, "");
                            Log.e("Privacypolicy", "Checking" + ModeOfTravel);
                            Log.e("Privacypolicy", "Checking" + PrivacyScreen);
                            if (PrivacyScreen.equals("True") && dashMdeCnt.equals("1")) {
                                Intent takePhoto = new Intent(Dashboard_Two.this, AllowanceActivityTwo.class);
                                takePhoto.putExtra("Mode", "COUT");
                                startActivity(takePhoto);
                            } else {

                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.remove(Name);
                                editor.remove(MOT);
                                editor.remove("SharedImage");
                                editor.remove("Sharedallowance");
                                editor.remove("SharedMode");
                                editor.remove("StartedKM");
                                editor.remove("SharedFromKm");
                                editor.remove("SharedToKm");
                                editor.remove("SharedFare");
                                editor.remove("SharedImages");
                                editor.remove("Closing");


                                editor.remove(hapLocation);
                                editor.remove(otherLocation);
                                editor.remove(visitPurpose);
                                editor.remove(modeTravelId);
                                editor.remove(modeTypeVale);
                                editor.remove(modeFromKm);
                                editor.remove(modeToKm);
                                editor.remove(StartedKm);
                                editor.remove("SharedDailyAllowancess");
                                editor.remove("SharedDriverss");
                                editor.remove("ShareModeIDs");
                                editor.remove("StoreId");


                                editor.commit();

                                Intent takePhoto = new Intent(Dashboard_Two.this, ImageCapture.class);
                                takePhoto.putExtra("Mode", "COUT");
                                startActivity(takePhoto);
                            }

                        } else {
                            Intent takePhoto = new Intent(Dashboard_Two.this, ImageCapture.class);
                            takePhoto.putExtra("Mode", "COUT");
                            startActivity(takePhoto);
                        }

                    }

                    @Override
                    public void NegativeMethod(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                break;

            default:
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

}
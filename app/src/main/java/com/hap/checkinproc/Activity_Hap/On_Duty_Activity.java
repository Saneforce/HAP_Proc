package com.hap.checkinproc.Activity_Hap;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.hap.checkinproc.Activity.TAClaimActivity;
import com.hap.checkinproc.Activity.Util.SelectionModel;
import com.hap.checkinproc.BuildConfig;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.AdapterForSelectionList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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

public class On_Duty_Activity extends AppCompatActivity implements View.OnClickListener, Master_Interface {
    LinearLayout haplocationtext, purposeofvisittext, haplocationbutton, otherlocationbutton, submitbutton, closebutton, exitclose, ondutylocations;
    EditText purposeofvisitedittext, ondutyedittext;
    int flag;
    Common_Model Model_Pojo;
    List<Common_Model> getfieldforcehqlist = new ArrayList<>();
    TextView selecthaplocationss;
    CustomListViewDialog customDialog;
    String hapLocid;
    Gson gson;
    LinearLayout ModeOfTravel;

    /*AllowanceActivity*/
    RelativeLayout pic, rlay_pic, lay_km, lay_to, lay_From, lay_det, lay_fare;
    Uri outputFileUri;
    ImageView capture_img;
    CardView card_travel, card_to, card_typ;
    ApiInterface apiInterface;
    Button btn_submit, btn_ta;
    String filepath_final = "";
    String mode, url, hq_code, typ_code;
    EditText edt_km, edt_rmk, edt_frm, edt_to, edt_fare;
    TextView txt_mode, txt_hq, txt_typ;
    Common_Class common_class;
    boolean updateMode = false;
    SharedPreferences UserDetails;
    public static final String MyPREFERENCES = "MyPrefs";
    String SF_code = "", div = "";
    ArrayList<SelectionModel> array_hq = new ArrayList<>();
    RelativeLayout lay_hq, lay_typ;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1001;
    Shared_Common_Pref mShared_common_pref;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on__duty_);


        if (!checkPermission()) {
            requestPermissions();

        } else {

        }

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
                startActivity(new Intent(getApplicationContext(), Dashboard.class));

            }
        });


        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });
        gson = new Gson();
        haplocationtext = findViewById(R.id.haplocationtext);
        purposeofvisittext = findViewById(R.id.purposeofvisittext);
        otherlocationbutton = findViewById(R.id.otherlocationbutton);
        haplocationbutton = findViewById(R.id.haplocationbutton);
        ondutylocations = findViewById(R.id.ondutylocations);
        submitbutton = findViewById(R.id.submitbutton);
        selecthaplocationss = findViewById(R.id.selecthaplocationss);
        purposeofvisitedittext = findViewById(R.id.purposeofvisitedittext);
        ondutyedittext = findViewById(R.id.ondutyedittext);
        closebutton = findViewById(R.id.closebutton);
        exitclose = findViewById(R.id.exitclose);
        ModeOfTravel = findViewById(R.id.mode_of_travel);
        otherlocationbutton.setOnClickListener(this);
        haplocationbutton.setOnClickListener(this);
        submitbutton.setOnClickListener(this);
        closebutton.setOnClickListener(this);
        exitclose.setOnClickListener(this);
        selecthaplocationss.setOnClickListener(this);
        GetfieldforceHq();


        /*Allowance Activity*/
        pic = findViewById(R.id.pic);
        common_class = new Common_Class(this);
        rlay_pic = findViewById(R.id.rlay_pic);
        lay_From = findViewById(R.id.lay_From);
        lay_to = findViewById(R.id.lay_to);
        lay_km = findViewById(R.id.lay_km);
        lay_det = findViewById(R.id.lay_det);
        lay_fare = findViewById(R.id.lay_fare);
        capture_img = findViewById(R.id.capture_img);
        card_travel = findViewById(R.id.card_travel);
        card_to = findViewById(R.id.card_to);
        card_typ = findViewById(R.id.card_typ);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        btn_submit = findViewById(R.id.btn_submit);
        btn_ta = findViewById(R.id.btn_ta);
        edt_km = findViewById(R.id.edt_km);
        edt_rmk = findViewById(R.id.edt_rmk);
        edt_frm = findViewById(R.id.edt_frm);
        edt_to = findViewById(R.id.edt_to);
        edt_fare = findViewById(R.id.edt_fare);
        txt_mode = findViewById(R.id.txt_mode);
        lay_hq = findViewById(R.id.lay_hq);
        txt_hq = findViewById(R.id.txt_hq);
        lay_typ = findViewById(R.id.lay_typ);
        txt_typ = findViewById(R.id.txt_typ);
        UserDetails = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SF_code = UserDetails.getString("Sfcode", "");
        div = UserDetails.getString("Divcode", "");


        mShared_common_pref = new Shared_Common_Pref(this);
        getTravelMode();
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                imageTake();

            }
        });
        card_travel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupSpinner();
            }
        });
        btn_ta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(On_Duty_Activity.this, TAClaimActivity.class);
                startActivity(i);
            }
        });
        lay_hq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupSpinnerHq();
            }
        });
        lay_typ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupSpinnerType();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(txt_mode.getText().toString())) {
                    Toast.makeText(On_Duty_Activity.this, "Select the mode of travels ", Toast.LENGTH_SHORT).show();
                } else {
                    if (rlay_pic.getVisibility() == View.VISIBLE) {
                        getMulipart(filepath_final, 0);
                    } else {

                    }
                }
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.otherlocationbutton:
                flag = 1;
                ondutylocations.setVisibility(View.VISIBLE);
                purposeofvisittext.setVisibility(View.VISIBLE);
                closebutton.setVisibility(View.VISIBLE);
                exitclose.setVisibility(View.GONE);
                submitbutton.setVisibility(View.VISIBLE);
                otherlocationbutton.setVisibility(View.GONE);
                haplocationbutton.setVisibility(View.GONE);
                ModeOfTravel.setVisibility(View.VISIBLE);
                purposeofvisitedittext.setText("");
                selecthaplocationss.setText("");
                ondutyedittext.setText("");
                break;

            case R.id.haplocationbutton:
                flag = 0;
                ondutyedittext.setText("");
                selecthaplocationss.setText("");
                purposeofvisitedittext.setText("");
                haplocationtext.setVisibility(View.VISIBLE);
                purposeofvisittext.setVisibility(View.VISIBLE);
                submitbutton.setVisibility(View.VISIBLE);
                closebutton.setVisibility(View.VISIBLE);
                exitclose.setVisibility(View.GONE);
                ModeOfTravel.setVisibility(View.VISIBLE);
                ondutylocations.setVisibility(View.GONE);
                haplocationbutton.setVisibility(View.GONE);
                otherlocationbutton.setVisibility(View.GONE);
                break;

            case R.id.submitbutton:
                if (txt_mode.getText().toString().equals("Bike")) {
                    if (!filepath_final.equals("") && !edt_km.getText().toString().equals("")) {
                        if (vali()) {
                            submitValue();
                        }
                    } else {
                        Toast.makeText(On_Duty_Activity.this, "Please enter all the details", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (!filepath_final.equals("") && !edt_frm.getText().toString().equals("") && !txt_hq.getText().toString().equals("") && !edt_fare.getText().toString().equals("")) {
                        if (vali()) {
                            submitValue();
                        }
                    } else {
                        Toast.makeText(On_Duty_Activity.this, "Please enter all the details", Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case R.id.closebutton:
                haplocationtext.setVisibility(View.GONE);
                purposeofvisittext.setVisibility(View.GONE);
                submitbutton.setVisibility(View.GONE);
                closebutton.setVisibility(View.GONE);
                haplocationbutton.setVisibility(View.VISIBLE);
                exitclose.setVisibility(View.VISIBLE);
                otherlocationbutton.setVisibility(View.VISIBLE);
                ondutylocations.setVisibility(View.GONE);
                ModeOfTravel.setVisibility(View.GONE);
                break;
            case R.id.exitclose:
                startActivity(new Intent(this, Dashboard.class));
                break;
            case R.id.selecthaplocationss:

                customDialog = new CustomListViewDialog(On_Duty_Activity.this, getfieldforcehqlist, 1);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.show();

                break;
        }
    }


    public void submitValue() {
        Intent intent = new Intent(this, Checkin.class);
        Bundle extras = new Bundle();
        extras.putString("ODFlag", String.valueOf(flag));
        extras.putString("Mode", "onduty");
        if (flag == 1) {
            extras.putString("onDutyPlcNm", ondutyedittext.getText().toString());
            extras.putString("onDutyPlcID", "0");
        } else {
            extras.putString("onDutyPlcNm", selecthaplocationss.getText().toString());
            extras.putString("onDutyPlcID", hapLocid);
        }
        extras.putString("vstPurpose", purposeofvisitedittext.getText().toString());
        intent.putExtras(extras);
        startActivity(intent);
    }

    @Override
    public void OnclickMasterType(java.util.List<Common_Model> myDataset, int position, int type) {
        customDialog.dismiss();
        selecthaplocationss.setText(myDataset.get(position).getName());
        hapLocid = String.valueOf(myDataset.get(position).getId());
    }

    public void GetfieldforceHq() {
        String commonLeaveType = "{\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<Object> call = service.getFieldForce_HQ(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, commonLeaveType);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                Log.e("ROUTE_MASTER_Object", String.valueOf(response.body().toString()));
                GetJsonData(new Gson().toJson(response.body()), "0");
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
            }
        });
    }

    private void GetJsonData(String jsonResponse, String type) {

        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String id = String.valueOf(jsonObject1.optInt("id"));
                String name = jsonObject1.optString("name");
                String flag = jsonObject1.optString("ODFlag");

                if (flag.equals("1")) {
                    Model_Pojo = new Common_Model(id, name, flag);
                    getfieldforcehqlist.add(Model_Pojo);
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean vali() {

        if (flag == 1 && (ondutyedittext.getText().toString() == null || ondutyedittext.getText().toString().isEmpty() || ondutyedittext.getText().toString().equalsIgnoreCase(""))) {
            Toast.makeText(this, "Enter the ON-Duty Location", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (flag == 0 && (selecthaplocationss.getText().toString() == null || selecthaplocationss.getText().toString().isEmpty() || selecthaplocationss.getText().toString().equalsIgnoreCase(""))) {
            Toast.makeText(this, "Select The HAP Location", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (purposeofvisitedittext.getText().toString() == null || purposeofvisitedittext.getText().toString().isEmpty() || purposeofvisitedittext.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Enter the Purpose of visit", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }


    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    /*common_class.CommonIntentwithFinish(Dashboard.class);*/
                    On_Duty_Activity.super.onBackPressed();
                }
            });


    @Override
    public void onBackPressed() {

    }


    /*Allowance Activity Method*/
    public void popupSpinner() {


        final Dialog dialog = new Dialog(On_Duty_Activity.this, R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_dynamic_view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        ListView popup_list = (ListView) dialog.findViewById(R.id.popup_list);
        ImageView iv_close_popup = (ImageView) dialog.findViewById(R.id.iv_close_popup);
        Button ok = (Button) dialog.findViewById(R.id.ok);
        ArrayList<SelectionModel> array = new ArrayList<>();
        array.add(new SelectionModel("Bike", false, "12"));
        array.add(new SelectionModel("Bus", false, "11"));
        TextView tv_todayplan_popup_head = (TextView) dialog.findViewById(R.id.tv_todayplan_popup_head);
        tv_todayplan_popup_head.setText("Mode of Travel");
        AdapterForSelectionList adapt = new AdapterForSelectionList(On_Duty_Activity.this, array, 0);
        popup_list.setAdapter(adapt);
        final SearchView search_view = (SearchView) dialog.findViewById(R.id.search_view);
        search_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_view.setIconified(false);
                InputMethodManager im = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
                im.showSoftInput(search_view, 0);
            }
        });
        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.v("search_view_str", s);
                adapt.getFilter().filter(s);
                return false;
            }
        });
        iv_close_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
                if(isEmpty) {
                    if (array_selection.contains(new SelectionModel(true))) {
                        for (int i = 0; i < array_selection.size(); i++) {
                            SelectionModel m = array_selection.get(i);
                            m.setClick(false);
                        }
                    }
                }
*/

                dialog.dismiss();
                //commonFun();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (array.contains(new SelectionModel(true))) {
                    for (int i = 0; i < array.size(); i++) {
                        SelectionModel m = array.get(i);
                        if (m.isClick()) {
                            mode = m.getCode();

                            Log.e("Allowance_Activity", m.getTxt());
                            edt_frm.getText().clear();
                            txt_hq.setText("");
                            edt_fare.setText("");
                            edt_km.setText("");
                            edt_rmk.setText("");
                            txt_mode.setText(m.getTxt());
                            enableFields();
                        }
                    }
                }

                dialog.dismiss();
                capture_img.setImageURI(null);
            }
        });


    }


    public void imageTake() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        outputFileUri = FileProvider.getUriForFile(On_Duty_Activity.this, getApplicationContext().getPackageName() + ".provider", new File(getExternalCacheDir().getPath(), "pickImageResult" + System.currentTimeMillis() + ".jpeg"));
        Log.v("priniting_uri", outputFileUri.toString() + " output " + outputFileUri.getPath() + " raw_msg " + getExternalCacheDir().getPath());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, 12);
    }

    public void popupSpinnerType() {
        final Dialog dialog = new Dialog(On_Duty_Activity.this, R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_dynamic_view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        ListView popup_list = (ListView) dialog.findViewById(R.id.popup_list);
        ImageView iv_close_popup = (ImageView) dialog.findViewById(R.id.iv_close_popup);
        Button ok = (Button) dialog.findViewById(R.id.ok);
        ArrayList<SelectionModel> array = new ArrayList<>();
        array.add(new SelectionModel("Metro", false, "MT"));
        array.add(new SelectionModel("Major", false, "MJ"));
        array.add(new SelectionModel("Others", false, "OT"));
        TextView tv_todayplan_popup_head = (TextView) dialog.findViewById(R.id.tv_todayplan_popup_head);
        tv_todayplan_popup_head.setText("Travel Type");
        AdapterForSelectionList adapt = new AdapterForSelectionList(On_Duty_Activity.this, array, 0);
        popup_list.setAdapter(adapt);
        final SearchView search_view = (SearchView) dialog.findViewById(R.id.search_view);
        search_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_view.setIconified(false);
                InputMethodManager im = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
                im.showSoftInput(search_view, 0);
            }
        });
        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.v("search_view_str", s);
                adapt.getFilter().filter(s);
                return false;
            }
        });
        iv_close_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
                if(isEmpty) {
                    if (array_selection.contains(new SelectionModel(true))) {
                        for (int i = 0; i < array_selection.size(); i++) {
                            SelectionModel m = array_selection.get(i);
                            m.setClick(false);
                        }
                    }
                }
*/
                dialog.dismiss();
                //commonFun();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (array.contains(new SelectionModel(true))) {
                    for (int i = 0; i < array.size(); i++) {
                        SelectionModel m = array.get(i);
                        if (m.isClick()) {
                            typ_code = m.getCode();
                            txt_typ.setText(m.getTxt());

                        }
                    }
                }
                dialog.dismiss();

            }
        });


    }

    public void popupSpinnerHq() {
        final Dialog dialog = new Dialog(On_Duty_Activity.this, R.style.AlertDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_dynamic_view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        ListView popup_list = (ListView) dialog.findViewById(R.id.popup_list);
        ImageView iv_close_popup = (ImageView) dialog.findViewById(R.id.iv_close_popup);
        Button ok = (Button) dialog.findViewById(R.id.ok);

        TextView tv_todayplan_popup_head = (TextView) dialog.findViewById(R.id.tv_todayplan_popup_head);
        tv_todayplan_popup_head.setText("Select Headquater");
        AdapterForSelectionList adapt = new AdapterForSelectionList(On_Duty_Activity.this, array_hq, 0);
        popup_list.setAdapter(adapt);
        final SearchView search_view = (SearchView) dialog.findViewById(R.id.search_view);
        search_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_view.setIconified(false);
                InputMethodManager im = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
                im.showSoftInput(search_view, 0);
            }
        });
        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.v("search_view_str", s);
                adapt.getFilter().filter(s);
                return false;
            }
        });
        iv_close_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
                if(isEmpty) {
                    if (array_selection.contains(new SelectionModel(true))) {
                        for (int i = 0; i < array_selection.size(); i++) {
                            SelectionModel m = array_selection.get(i);
                            m.setClick(false);
                        }
                    }
                }
*/
                dialog.dismiss();
                //commonFun();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (array_hq.contains(new SelectionModel(true))) {
                    for (int i = 0; i < array_hq.size(); i++) {
                        SelectionModel m = array_hq.get(i);
                        if (m.isClick()) {
                            hq_code = m.getCode();
                            txt_hq.setText(m.getTxt());
                            if (hq_code.equalsIgnoreCase("-1")) {
                                card_to.setVisibility(View.VISIBLE);
                                card_typ.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
                dialog.dismiss();

            }
        });


    }

    public void enableFields() {
        if (mode.equalsIgnoreCase("11")) {
            rlay_pic.setVisibility(View.VISIBLE);
            lay_det.setVisibility(View.GONE);
            lay_From.setVisibility(View.VISIBLE);
            lay_to.setVisibility(View.VISIBLE);
            card_to.setVisibility(View.GONE);
            card_typ.setVisibility(View.GONE);
            lay_fare.setVisibility(View.VISIBLE);
            txt_mode.setText("Bus");
        } else {
            rlay_pic.setVisibility(View.VISIBLE);
            lay_km.setVisibility(View.VISIBLE);
            lay_det.setVisibility(View.VISIBLE);
            lay_From.setVisibility(View.GONE);
            lay_to.setVisibility(View.GONE);
            card_to.setVisibility(View.GONE);
            card_typ.setVisibility(View.GONE);
            lay_fare.setVisibility(View.GONE);
            txt_mode.setText("Bike");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12 && resultCode == Activity.RESULT_OK) {

            String finalPath = "/storage/emulated/0";
            String filePath = outputFileUri.getPath();
            filePath = filePath.substring(1);
            filePath = finalPath + filePath.substring(filePath.indexOf("/"));
            Log.v("printing__file_path", filePath);


            if (filePath == null)
                Toast.makeText(On_Duty_Activity.this, "This file format not supported", Toast.LENGTH_LONG).show();
            else {
                capture_img.setVisibility(View.VISIBLE);
                capture_img.setImageURI(outputFileUri);
                capture_img.setRotation((float) 90);
                filepath_final = filePath;
                mShared_common_pref.save("Started_Image", String.valueOf(outputFileUri));
            }

        }

    }

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

        Callto = apiInterface.uploadimg(values, imgg);

        Callto.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.v("print_upload_file", "ggg" + response.isSuccessful() + response.body());
                //uploading.setText("Uploading "+String.valueOf(count)+"/"+String.valueOf(count_check));

                try {
                    if (response.isSuccessful()) {


                        Log.v("print_upload_file_true", "ggg" + response);
                        JSONObject jb = null;
                        String jsonData = null;
                        jsonData = response.body().string();
                        Log.v("request_data_upload", String.valueOf(jsonData));
                        JSONObject js = new JSONObject(jsonData);
                        if (js.getString("success").equalsIgnoreCase("true")) {
                            Log.v("printing_dynamic_cou", js.getString("url"));
                            url = js.getString("url");
                            submitData();
                        }

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

    public void submitData() {

        mShared_common_pref.save("Started_km", edt_km.getText().toString());
        mShared_common_pref.save("mode_of_travel", txt_mode.getText().toString());
        mShared_common_pref.save("mode_of_travel", txt_mode.getText().toString());

        Log.e("MODE_OF_Travel", txt_mode.getText().toString());
        try {
            JSONObject jj = new JSONObject();
            jj.put("km", edt_km.getText().toString());
            jj.put("rmk", edt_rmk.getText().toString());
            jj.put("mod", mode);
            jj.put("sf", SF_code);
            jj.put("div", div);
            jj.put("url", url);
            jj.put("from", edt_frm.getText().toString());
            jj.put("to", edt_to.getText().toString());
            jj.put("fare", edt_fare.getText().toString());
            //saveAllowance
            Log.v("printing_allow", jj.toString());
            Call<ResponseBody> Callto;
            if (updateMode)
                Callto = apiInterface.updateAllowance(jj.toString());
            else
                Callto = apiInterface.saveAllowance(jj.toString());

            Callto.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.isSuccessful()) {


                            Log.v("print_upload_file_true", "ggg" + response);
                            JSONObject jb = null;
                            String jsonData = null;
                            jsonData = response.body().string();
                            Log.v("response_data", jsonData);
                            JSONObject js = new JSONObject(jsonData);
                            if (js.getString("success").equalsIgnoreCase("true")) {
                                Toast.makeText(On_Duty_Activity.this, " Submitted successfully ", Toast.LENGTH_SHORT).show();
                                common_class.CommonIntentwithFinish(Dashboard.class);
                                //common_class.CommonIntentwithFinish(AllowanceActivityTwo.class);
                            } else
                                Toast.makeText(On_Duty_Activity.this, " Cannot submitted the data ", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } catch (Exception e) {
        }
    }

    public void getTravelMode() {
        try {
            JSONObject jj = new JSONObject();

            jj.put("sf", SF_code);
            jj.put("div", div);
            //jj.put("url",url);
            //saveAllowance
            Log.v("printing_allow", jj.toString());
            Call<ResponseBody> Callto = apiInterface.getTravelMode(jj.toString());
            Callto.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.isSuccessful()) {


                            Log.v("print_upload_file_true", "ggg" + response);
                            JSONObject jb = null;
                            String jsonData = null;
                            jsonData = response.body().string();
                            Log.v("get_mode_Res", jsonData);
                            JSONArray ja = new JSONArray(jsonData);
                            if (ja.length() != 0) {
                                JSONObject js = ja.getJSONObject(0);
                                mode = js.getString("MOT");
                                enableFields();
                                updateMode = true;
                            }
                            getHeadQuaters();
                           /* JSONObject js=new JSONObject(jsonData);
                            if(js.getString("success").equalsIgnoreCase("true")){
                                Toast.makeText(AllowanceActivity.this," Submitted successfully ",Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(AllowanceActivity.this," Cannot submitted the data ",Toast.LENGTH_SHORT).show();*/
                        }
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } catch (Exception e) {
        }
    }

    public void getHeadQuaters() {
        try {
            JSONObject jj = new JSONObject();

            jj.put("sfCode", SF_code);
            jj.put("divisionCode", div);
            //jj.put("url",url);
            //saveAllowance
            Log.v("printing_allow", jj.toString());
            Call<ResponseBody> Callto = apiInterface.gethq(jj.toString());
            Callto.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.isSuccessful()) {


                            Log.v("print_upload_file_true", "ggg" + response);
                            JSONObject jb = null;
                            String jsonData = null;
                            jsonData = response.body().string();
                            Log.v("get_mode_Res", jsonData);
                            JSONArray ja = new JSONArray(jsonData);
                            for (int i = 0; i < ja.length(); i++) {
                                JSONObject jjson = ja.getJSONObject(i);
                                array_hq.add(new SelectionModel(jjson.getString("name"), false, jjson.getString("id")));
                            }
                            array_hq.add(new SelectionModel("Others", false, "-1"));
                           /* if(ja.length()!=0){
                                card_travel.setEnabled(false);
                                JSONObject js=ja.getJSONObject(0);
                                mode=js.getString("MOT");
                                enableFields();
                                updateMode=true;
                            }*/
                           /* JSONObject js=new JSONObject(jsonData);
                            if(js.getString("success").equalsIgnoreCase("true")){
                                Toast.makeText(AllowanceActivity.this," Submitted successfully ",Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(AllowanceActivity.this," Cannot submitted the data ",Toast.LENGTH_SHORT).show();*/
                        }
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } catch (Exception e) {
        }
    }


    private boolean checkPermission() {
        return (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    //Location service part
    private void requestPermissions() {
        boolean shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA);
        if (shouldProvideRationale) {
            Snackbar.make(
                    findViewById(R.id.activity_main),
                    R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(On_Duty_Activity.this,
                                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    })
                    .show();
        } else
            ActivityCompat.requestPermissions(On_Duty_Activity.this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length <= 0) {
                    // Permission was not granted.
                } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted.
                    //mLUService.requestLocationUpdates();
                } else {
                    // Permission denied.
                    Snackbar.make(
                            findViewById(R.id.activity_main),
                            R.string.permission_denied_explanation,
                            Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.settings, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Build intent that displays the App settings screen.
                                    Intent intent = new Intent();
                                    intent.setAction(
                                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package",
                                            BuildConfig.APPLICATION_ID, null);
                                    intent.setData(uri);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            })
                            .show();
                }
        }
    }

}
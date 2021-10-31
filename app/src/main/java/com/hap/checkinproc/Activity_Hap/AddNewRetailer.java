package com.hap.checkinproc.Activity_Hap;

import static com.hap.checkinproc.Activity_Hap.Leave_Request.CheckInfo;
import static com.hap.checkinproc.Common_Class.Constants.Rout_List;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Common_Model;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.LocationEvents;
import com.hap.checkinproc.Interface.Master_Interface;
import com.hap.checkinproc.Interface.OnImagePickListener;
import com.hap.checkinproc.Interface.UpdateResponseUI;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Activity.Dashboard_Route;
import com.hap.checkinproc.SFA_Activity.Outlet_Info_Activity;
import com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List;
import com.hap.checkinproc.common.DatabaseHandler;
import com.hap.checkinproc.common.FileUploadService;
import com.hap.checkinproc.common.LocationFinder;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewRetailer extends AppCompatActivity implements Master_Interface, View.OnClickListener, OnMapReadyCallback, UpdateResponseUI {
    TextView toolHeader;
    ImageView imgBack;
    EditText toolSearch, retailercode;
    GoogleMap mGoogleMap;
    Button mSubmit;
    ApiInterface service;
    RelativeLayout linReatilerRoute, rlDistributor, rlDelvryType, rlOutletType, rlState, linReatilerChannel;
    LinearLayout linReatilerClass, CurrentLocLin, retailercodevisible;
    TextView txtRetailerRoute, txtRetailerClass, txtRetailerChannel, CurrentLocationsAddress, headtext, distributor_text,
            txDelvryType, txOutletType, tvStateName;
    Type userType;
    List<Common_Model> modelRetailClass = new ArrayList<>();
    List<Common_Model> modelRetailChannel = new ArrayList<>();
    List<Common_Model> modelRetailDetails = new ArrayList<>();
    Common_Model mCommon_model_spinner;
    Gson gson;
    EditText addRetailerName, owner_name, addRetailerAddress, addRetailerCity, addRetailerPhone, addRetailerEmail,
            edt_pin_codeedit, edt_gst, etPhoneNo2, edt_outstanding;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    JSONArray mainArray;
    JSONObject docMasterObject;
    String keyEk = "N", KeyDate, KeyHyp = "-", keyCodeValue, imageConvert = "", imageServer = "";
    Integer classId, channelID, iOutletTyp, stateCode;
    String routeId, Compititor_Id, Compititor_Name, CatUniverSelectId, AvailUniverSelectId, reason_category_remarks = "", HatsunAvailswitch = "", categoryuniverseswitch = "";
    Shared_Common_Pref shared_common_pref;
    SharedPreferences UserDetails, CheckInDetails;
    Common_Class common_class;
    List<Retailer_Modal_List> Retailer_Modal_List;
    ImageView copypaste;
    String TAG = "AddNewRetailer: ", UserInfo = "MyPrefs";
    DatabaseHandler db;

    ImageView ivPhotoShop;

    String filePath;

    File file;
    private Uri outputFileUri;
    private String finalPath = "";
    private String place_id = "";
    Common_Model Model_Pojo;
    List<Common_Model> FRoute_Master = new ArrayList<>();
    List<Common_Model> distributor_master = new ArrayList<>();
    CircularProgressButton btnRefLoc;
    double RetLat = 0.0, RetLng = 0.0;
    List<Common_Model> deliveryTypeList, outletTypeList;
    final Handler handler = new Handler();
    private ArrayList<Common_Model> stateList;
    private String name = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_new_retailer);

            CheckInDetails = getSharedPreferences(CheckInfo, Context.MODE_PRIVATE);
            UserDetails = getSharedPreferences(UserInfo, Context.MODE_PRIVATE);

            db = new DatabaseHandler(this);
            linReatilerRoute = findViewById(R.id.rl_route);
            rlDistributor = findViewById(R.id.rl_Distributor);
            txtRetailerRoute = findViewById(R.id.retailer_type);
            distributor_text = findViewById(R.id.distributor_text);
            retailercode = findViewById(R.id.retailercode);
            common_class = new Common_Class(this);
            CurrentLocLin = findViewById(R.id.CurrentLocLin);
            retailercodevisible = findViewById(R.id.retailercodevisible);
            CurrentLocationsAddress = findViewById(R.id.CurrentLocationsAddress);
            copypaste = findViewById(R.id.copypaste);
            edt_gst = findViewById(R.id.edt_gst);
            headtext = findViewById(R.id.headtext);
            addRetailerName = findViewById(R.id.edt_new_name);
            owner_name = findViewById(R.id.owner_name);
            addRetailerAddress = findViewById(R.id.edt_new_address);
            addRetailerCity = findViewById(R.id.edt_new_city);
            addRetailerPhone = findViewById(R.id.edt_new_phone);
            addRetailerEmail = findViewById(R.id.edt_new_email);
            edt_pin_codeedit = findViewById(R.id.edt_pin_code);
            edt_pin_codeedit = findViewById(R.id.edt_pin_code);
            rlDelvryType = findViewById(R.id.rlDelvryType);
            txDelvryType = findViewById(R.id.txDelvryType);
            rlOutletType = findViewById(R.id.rlOutletType);
            txOutletType = findViewById(R.id.txOutletType);
            rlState = findViewById(R.id.rl_state);
            tvStateName = findViewById(R.id.tvState);

            ivPhotoShop = findViewById(R.id.ivShopPhoto);
            mSubmit = findViewById(R.id.submit_button);
            etPhoneNo2 = findViewById(R.id.edt_new_phone2);
            edt_outstanding = findViewById(R.id.edt_retailer_outstanding);
            btnRefLoc = findViewById(R.id.btnRefLoc);
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.route_map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }

            deliveryTypeList = new ArrayList<>();
            mCommon_model_spinner = new Common_Model("AC", "AC", "flag");
            deliveryTypeList.add(mCommon_model_spinner);
            mCommon_model_spinner = new Common_Model("OT", "Others", "flag");
            deliveryTypeList.add(mCommon_model_spinner);

            rlDelvryType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    common_class.showCommonDialog(deliveryTypeList, 11, AddNewRetailer.this);
                }
            });

            outletTypeList = new ArrayList<>();
            mCommon_model_spinner = new Common_Model("1", "Service", "flag");
            outletTypeList.add(mCommon_model_spinner);
            mCommon_model_spinner = new Common_Model("0", "Universal", "flag");
            outletTypeList.add(mCommon_model_spinner);

            rlOutletType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    common_class.showCommonDialog(outletTypeList, 13, AddNewRetailer.this);

                }
            });
            copypaste.setOnClickListener(this);
            ivPhotoShop.setOnClickListener(this);
            rlState.setOnClickListener(this);
            btnRefLoc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                new LocationFinder(getApplication(), new LocationEvents() {
                                    @Override
                                    public void OnLocationRecived(Location location) {
                                        try {
                                            if (location == null) {
                                                Toast.makeText(AddNewRetailer.this, "Location Can't Getting Location. Try Again.", Toast.LENGTH_LONG).show();
                                                //btnRefLoc.doneLoadingAnimation(getResources().getColor(R.color.color_red), BitmapFactory.decodeResource(getResources(), R.drawable.ic_wrong));
                                                return;
                                            } else {
                                                refreshLocation(location);
                                            }
                                        } catch (Exception e) {
                                            Log.v(TAG, "LOC1:" + e.getMessage());
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                Log.v(TAG, "LOC3:" + e.getMessage());
                            }
                        }
                    }, 100);

                }

            });

            gson = new Gson();
            shared_common_pref = new Shared_Common_Pref(this);
            service = ApiClient.getClient().create(ApiInterface.class);
            gson = new Gson();
            userType = new TypeToken<ArrayList<Retailer_Modal_List>>() {
            }.getType();
            String OrdersTable = shared_common_pref.getvalue(Constants.Retailer_OutletList);
            Retailer_Modal_List = gson.fromJson(OrdersTable, userType);
            distributor_text.setText(shared_common_pref.getvalue(Constants.Distributor_name));
            txtRetailerRoute.setText(shared_common_pref.getvalue(Constants.Route_name));
            routeId = shared_common_pref.getvalue(Constants.Route_Id);

            if (Shared_Common_Pref.Outler_AddFlag != null && Shared_Common_Pref.Outler_AddFlag.equals("1")) {
                mSubmit.setVisibility(View.VISIBLE);
                CurrentLocLin.setVisibility(View.GONE);
                retailercodevisible.setVisibility(View.GONE);
                CurrentLocationsAddress.setVisibility(View.GONE);
                //   routeId = shared_common_pref.getvalue("RouteSelect");
                CurrentLocationsAddress.setText("" + Shared_Common_Pref.OutletAddress);
                getCompleteAddressString(Shared_Common_Pref.Outletlat, Shared_Common_Pref.Outletlong);
                headtext.setText("Create Outlet");
            } else {
                retailercodevisible.setVisibility(View.VISIBLE);
                CurrentLocLin.setVisibility(View.GONE);
                CurrentLocationsAddress.setVisibility(View.GONE);
                Shared_Common_Pref.Outler_AddFlag = "0";
            }
            if (Shared_Common_Pref.Outlet_Info_Flag != null && Shared_Common_Pref.Outlet_Info_Flag.equals("1")) {
                mSubmit.setVisibility(View.GONE);
                headtext.setText("Outlet Info");
            }
            //  getRouteDetails();
            getRetailerClass();
            getRetailerChannel();

            if (Shared_Common_Pref.Editoutletflag != null && Shared_Common_Pref.Editoutletflag.equals("1") || (Shared_Common_Pref.Outlet_Info_Flag != null && Shared_Common_Pref.Outlet_Info_Flag.equals("1"))) {
                iOutletTyp = Integer.valueOf(Retailer_Modal_List.get(getOutletPosition()).getType());
                if (Retailer_Modal_List.get(getOutletPosition()).getType() == null)
                    iOutletTyp = 0;
                if (iOutletTyp == 0)
                    txOutletType.setText("Universal");
                else
                    txOutletType.setText("Service");
                txDelvryType.setText(Retailer_Modal_List.get(getOutletPosition()).getDelivType());
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
                    Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
                    if (CheckIn == true) {
                        common_class.CommonIntentwithoutFinish(SFA_Activity.class);
                    } else
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                }
            });

            onClickRetailerClass();
            onClickRetailerChannel();


            addRetailerName.clearFocus();
            Intent i = getIntent();
            Log.e("TestOutler_AddFlag", Shared_Common_Pref.Outler_AddFlag);
            if (i != null && i.getExtras() != null) {
                if (Shared_Common_Pref.Outler_AddFlag != null && Shared_Common_Pref.Outler_AddFlag.equals("1")) {
                    Compititor_Id = i.getExtras().getString("Compititor_Id");
                    Compititor_Name = i.getExtras().getString("Compititor_Name");
                    CatUniverSelectId = i.getExtras().getString("CatUniverSelectId");
                    AvailUniverSelectId = i.getExtras().getString("AvailUniverSelectId");
                    reason_category_remarks = i.getExtras().getString("reason_category");
                    HatsunAvailswitch = i.getExtras().getString("HatsunAvailswitch");
                    categoryuniverseswitch = i.getExtras().getString("categoryuniverseswitch");
                    Log.e("HatsunAvailswitch", "" + HatsunAvailswitch);
                    Log.e("categoryuniverseswitch", "" + categoryuniverseswitch);
                    Log.e("reason_category", "" + reason_category_remarks);
                    Log.e("CatUniverSelectId", "" + CatUniverSelectId);
                    Log.e("AvailUniverSelectId", "" + AvailUniverSelectId);
                    Log.e("Compititor_Name", "" + Compititor_Name);
                    //The key argument here must match that used in the other activity
                } else {
                    common_class.getDb_310Data(Constants.STATE_LIST, this);

                    name = "http://hapapps.sanfmcg.com/" + Retailer_Modal_List.get(getOutletPosition()).getImagename();
                    name = name.replace(",", "");
                    Picasso.with(AddNewRetailer.this)
                            .load(name)
                            .error(R.drawable.profile_img)
                            .into(ivPhotoShop);
                    addRetailerName.setText("" + Retailer_Modal_List.get(getOutletPosition()).getName());
                    addRetailerAddress.setText("" + Retailer_Modal_List.get(getOutletPosition()).getListedDrAddress1());
                    txtRetailerRoute.setText("" + Retailer_Modal_List.get(getOutletPosition()).getTownName());
                    addRetailerPhone.setText("" + Retailer_Modal_List.get(getOutletPosition()).getPrimary_No());
                    retailercode.setText("" + Retailer_Modal_List.get(getOutletPosition()).getId());
                    if (Retailer_Modal_List.get(getOutletPosition()).getSecondary_No() != null)
                        etPhoneNo2.setText("" + Retailer_Modal_List.get(getOutletPosition()).getSecondary_No());
                    if (Retailer_Modal_List.get(getOutletPosition()).getCityname() != null)
                        addRetailerCity.setText("" + Retailer_Modal_List.get(getOutletPosition()).getCityname());
                    if (Retailer_Modal_List.get(getOutletPosition()).getListedDr_Email() != null)
                        addRetailerEmail.setText("" + Retailer_Modal_List.get(getOutletPosition()).getListedDr_Email());
                    if (Retailer_Modal_List.get(getOutletPosition()).getOwner_Name() != null)
                        owner_name.setText("" + Retailer_Modal_List.get(getOutletPosition()).getOwner_Name());
                    edt_pin_codeedit.setText("" + (Retailer_Modal_List.get(getOutletPosition()).getPin_code()));
                    edt_gst.setText("" + (Retailer_Modal_List.get(getOutletPosition()).getGst()));
                    // txtRetailerClass.setText("" + Retailer_Modal_List.get(getOutletPosition()).getClass());


                    if (i.getExtras().getString("Compititor_Id") != null)
                        Compititor_Id = i.getExtras().getString("Compititor_Id");
                    if (i.getExtras().getString("Compititor_Name") != null)
                        Compititor_Name = i.getExtras().getString("Compititor_Name");
                    if (i.getExtras().getString("CatUniverSelectId") != null)
                        CatUniverSelectId = i.getExtras().getString("CatUniverSelectId");
                    if (i.getExtras().getString("AvailUniverSelectId") != null)
                        AvailUniverSelectId = i.getExtras().getString("AvailUniverSelectId");
                    if (i.getExtras().getString("reason_category") != null)
                        reason_category_remarks = i.getExtras().getString("reason_category");
                }

            }


            if (Shared_Common_Pref.Editoutletflag != null && Shared_Common_Pref.Editoutletflag.equals("1")) {
                mSubmit.setVisibility(View.VISIBLE);
                addRetailerName.setText("" + Retailer_Modal_List.get(getOutletPosition()).getName());
                addRetailerAddress.setText("" + Retailer_Modal_List.get(getOutletPosition()).getListedDrAddress1());
                txtRetailerRoute.setText("" + Retailer_Modal_List.get(getOutletPosition()).getTownName());
                addRetailerPhone.setText("" + Retailer_Modal_List.get(getOutletPosition()).getPrimary_No());
                retailercode.setText("" + Retailer_Modal_List.get(getOutletPosition()).getId());
                routeId = Retailer_Modal_List.get(getOutletPosition()).getTownCode();


                RetLat = Double.parseDouble(Retailer_Modal_List.get(getOutletPosition()).getLat());
                RetLng = Double.parseDouble(Retailer_Modal_List.get(getOutletPosition()).getLong());

                Shared_Common_Pref.Outletlat = RetLat;
                Shared_Common_Pref.Outletlong = RetLng;

                if (Retailer_Modal_List.get(getOutletPosition()).getCityname() != null)
                    addRetailerCity.setText("" + Retailer_Modal_List.get(getOutletPosition()).getCityname());
                if (Retailer_Modal_List.get(getOutletPosition()).getListedDr_Email() != null)
                    addRetailerEmail.setText("" + Retailer_Modal_List.get(getOutletPosition()).getListedDr_Email());
                if (Retailer_Modal_List.get(getOutletPosition()).getOwner_Name() != null)
                    owner_name.setText("" + Retailer_Modal_List.get(getOutletPosition()).getOwner_Name());
                edt_pin_codeedit.setText("" + Retailer_Modal_List.get(getOutletPosition()).getPin_code());
                edt_gst.setText("" + Retailer_Modal_List.get(getOutletPosition()).getGst());
                //  txtRetailerClass.setText("" + Retailer_Modal_List.get(getOutletPosition()).getClass());
                if (i != null && i.getExtras() != null) {
                    if (i.getExtras().getString("Compititor_Id") != null)
                        Compititor_Id = i.getExtras().getString("Compititor_Id");
                    if (i.getExtras().getString("Compititor_Name") != null)
                        Compititor_Name = i.getExtras().getString("Compititor_Name");
                    if (i.getExtras().getString("CatUniverSelectId") != null)
                        CatUniverSelectId = i.getExtras().getString("CatUniverSelectId");
                    if (i.getExtras().getString("AvailUniverSelectId") != null)
                        AvailUniverSelectId = i.getExtras().getString("AvailUniverSelectId");
                    if (i.getExtras().getString("reason_category") != null)
                        reason_category_remarks = i.getExtras().getString("reason_category");
                    if (i.getExtras().getString("HatsunAvailswitch") != null)
                        HatsunAvailswitch = i.getExtras().getString("HatsunAvailswitch");
                    if (i.getExtras().getString("categoryuniverseswitch") != null)
                        categoryuniverseswitch = i.getExtras().getString("categoryuniverseswitch");
                }
            }


            mSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (txtRetailerRoute.getText().toString().matches("")) {
                        Toast.makeText(getApplicationContext(), "Select route", Toast.LENGTH_SHORT).show();
                    } else if (addRetailerName.getText().toString().matches("")) {
                        Toast.makeText(getApplicationContext(), "Enter Outlet Name", Toast.LENGTH_SHORT).show();
                    } else if (owner_name.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Enter the owner Name", Toast.LENGTH_SHORT).show();
                    } else if (addRetailerAddress.getText().toString().matches("")) {
                        Toast.makeText(getApplicationContext(), "Enter Address", Toast.LENGTH_SHORT).show();
                    } else if (tvStateName.getText().toString().matches("")) {
                        Toast.makeText(getApplicationContext(), "Select the State", Toast.LENGTH_SHORT).show();
                    } else if (addRetailerCity.getText().toString().matches("")) {
                        Toast.makeText(getApplicationContext(), "Enter City", Toast.LENGTH_SHORT).show();
                    } else if (addRetailerPhone.getText().toString().matches("")) {
                        Toast.makeText(getApplicationContext(), "Enter Phone", Toast.LENGTH_SHORT).show();
                    }
//                    else if (txtRetailerClass.getText().toString().matches("")) {
//                        Toast.makeText(getApplicationContext(), "Select the Outlet Type", Toast.LENGTH_SHORT).show();
//                    }
                    else if (txtRetailerChannel.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(getApplicationContext(), "Select the Outlet Category", Toast.LENGTH_SHORT).show();
                    } else if (txDelvryType.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(getApplicationContext(), "Select the Delivery Type", Toast.LENGTH_SHORT).show();
                    } else if (txOutletType.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(getApplicationContext(), "Select the Outlet Type", Toast.LENGTH_SHORT).show();
                    } else if (imageConvert.equals("") && name.equals("")) {
                        Toast.makeText(getApplicationContext(), "Please take picture", Toast.LENGTH_SHORT).show();

                    } else {
                        addNewRetailers();
                    }

                }
            });

            String placeIdData = getIntent().getStringExtra(Constants.PLACE_ID);
            if (placeIdData != null) {
                //  Nearby_Outlets.

                JSONObject jsonObject = new JSONObject(placeIdData);

                JSONObject jsonResult = jsonObject.getJSONObject("result");
                addRetailerPhone.setText("" + jsonResult.optString("formatted_phone_number"));
                addRetailerAddress.setText("" + jsonResult.optString("vicinity"));
                addRetailerName.setText("" + jsonResult.getString("name"));

                place_id = jsonResult.getString("place_id");

                Log.e(TAG, "Address:" + jsonObject.optString("formatted_address"));

                JSONArray addressJsonArray = jsonResult.getJSONArray("address_components");
                for (int addressIdex = 0; addressIdex < addressJsonArray.length(); addressIdex++) {

                    JSONObject jsonAddressObj = addressJsonArray.getJSONObject(addressIdex);

                    JSONArray typesArray = addressJsonArray.getJSONObject(addressIdex).getJSONArray("types");

                    for (int typesIndex = 0; typesIndex < typesArray.length(); typesIndex++) {

                        if (typesArray.get(typesIndex).equals("postal_code")) {
                            edt_pin_codeedit.setText("" + jsonAddressObj.optString("long_name"));
                        }

                        if (typesArray.get(typesIndex).equals("locality")) {

                            addRetailerCity.setText("" + jsonAddressObj.optString("long_name"));
                        }
                    }
                }
            }

            shared_common_pref.save(Constants.Retailor_FilePath, "");


            if (Shared_Common_Pref.Outler_AddFlag.equals("1")) {
                linReatilerRoute.setOnClickListener(this);
                rlDistributor.setOnClickListener(this);
                getDbstoreData();
                common_class.getDb_310Data(Rout_List, this);

            }


            if (shared_common_pref.getvalue(Constants.LOGIN_TYPE).equals(Constants.DISTRIBUTER_TYPE)) {
                if (Shared_Common_Pref.Outler_AddFlag != null && !Shared_Common_Pref.Outler_AddFlag.equals("1"))
                    mSubmit.setVisibility(View.GONE);

                rlDistributor.setEnabled(false);
                findViewById(R.id.ivDistSpinner).setVisibility(View.GONE);
            }


        } catch (Exception e) {
            Log.e(TAG, e.getMessage());

        }


    }

    void refreshLocation(Location location) {

        btnRefLoc.startAnimation();
        RetLat = location.getLatitude();
        RetLng = location.getLongitude();
        Shared_Common_Pref.Outletlat = RetLat;
        Shared_Common_Pref.Outletlong = RetLng;
        getCompleteAddressString(RetLat, RetLng);
        centreMapOnLocation("Your Location");
        btnRefLoc.doneLoadingAnimation(getResources().getColor(R.color.green), BitmapFactory.decodeResource(getResources(), R.drawable.done));


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    btnRefLoc.stopAnimation();
                    btnRefLoc.revertAnimation();
                    btnRefLoc.setBackground(getDrawable(R.drawable.button_blueg));
                } catch (Exception e) {
                    Log.v(TAG, "LOC2:" + e.getMessage());

                }
            }
        }, 1000);
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();
                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                addRetailerAddress.setText(strReturnedAddress.toString());
                addRetailerCity.setText(returnedAddress.getLocality());
                edt_pin_codeedit.setText(returnedAddress.getPostalCode());
                strAdd = strReturnedAddress.toString();
                //Log.w("My Current loction address", strReturnedAddress.toString());
            } else {
                // Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //  Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }

    void getDbstoreData() {
        try {
            //  JSONArray jsonArray = db.getMasterData(listType);
            JSONArray jsonArray = new JSONArray(shared_common_pref.getvalue(Constants.Distributor_List));

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String id = String.valueOf(jsonObject1.optInt("id"));
                String name = jsonObject1.optString("name");
                String flag = jsonObject1.optString("FWFlg");
                Model_Pojo = new Common_Model(id, name, flag);
                distributor_master.add(Model_Pojo);


            }

        } catch (Exception e) {

        }


    }


    /*Route Details*/
//    public void getRouteDetails() {
//
//        String routeMap = "{\"tableName\":\"vwTown_Master_APP\",\"coloumns\":\"[\\\"town_code as id\\\", \\\"town_name as name\\\",\\\"target\\\",\\\"min_prod\\\",\\\"field_code\\\",\\\"stockist_code\\\"]\",\"where\":\"[\\\"isnull(Town_Activation_Flag,0)=0\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
//        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
//        Call<JsonArray> call = apiInterface.retailerClass(shared_common_pref.getvalue(Shared_Common_Pref.Div_Code), shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code), shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code), "24", routeMap);
//        call.enqueue(new Callback<JsonArray>() {
//            @Override
//            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
//
//                Log.e("Route_response", response.body().toString());
//
//                JsonArray jsonArray = response.body();
//                for (int a = 0; a < jsonArray.size(); a++) {
//                    JsonObject jsonObject = (JsonObject) jsonArray.get(a);
//                    String className = String.valueOf(jsonObject.get("name"));
//                    String retailerClass = String.valueOf(className.subSequence(1, className.length() - 1));
//                    String id = String.valueOf(jsonObject.get("id"));
//                    Log.e("RETAILER_CLASS_NAME", retailerClass);
//
//
//                    mCommon_model_spinner = new Common_Model(id, retailerClass, "flag");
//                    Log.e("LeaveType_Request", retailerClass);
//                    modelRetailDetails.add(mCommon_model_spinner);
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<JsonArray> call, Throwable t) {
//                Log.e("Route_response", "ERROR");
//
//            }
//        });
//    }

    /*Route Class*/
    public void getRetailerClass() {
        try {
            String routeMap = "{\"tableName\":\"Mas_Doc_Class\",\"coloumns\":\"[\\\"Doc_ClsCode as id\\\"," +
                    " \\\"Doc_ClsSName as name\\\"]\",\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonArray> call = apiInterface.retailerClass(shared_common_pref.getvalue(Shared_Common_Pref.Div_Code),
                    shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code), shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code), "24", routeMap);
            call.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    JsonArray jsonArray = response.body();
                    Log.e("RESPONSE_VALUE", String.valueOf(jsonArray));
                    for (int a = 0; a < jsonArray.size(); a++) {
                        JsonObject jsonObject = (JsonObject) jsonArray.get(a);
                        String className = String.valueOf(jsonObject.get("name"));
                        String id = String.valueOf(jsonObject.get("id"));
                        String retailerClass = String.valueOf(className.subSequence(1, className.length() - 1));
                        Log.e("RETAILER_CLASS_NAME", retailerClass);
                        if (Shared_Common_Pref.Editoutletflag != null && Shared_Common_Pref.Editoutletflag.equals("1") || (Shared_Common_Pref.Outlet_Info_Flag != null && Shared_Common_Pref.Outlet_Info_Flag.equals("1"))) {
                            /*if (id.equals(String.valueOf(Retailer_Modal_List.get(getOutletPosition()).getDocCatCode()))) {
                                txtRetailerClass.setText(className.replace('"', ' '));
                                classId = Integer.valueOf(String.valueOf(jsonObject.get("id")));
                            }*/
                        }
                        mCommon_model_spinner = new Common_Model(id, retailerClass, "flag");
                        Log.e("LeaveType_Request", retailerClass);
                        modelRetailClass.add(mCommon_model_spinner);
                    }


                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {

                }
            });
        } catch (Exception e) {

        }
    }

    /*Retailer Class Click*/
    public void onClickRetailerClass() {
        linReatilerClass = findViewById(R.id.linear_retailer_class);
        txtRetailerClass = findViewById(R.id.txt_retailer_class);
        linReatilerClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                common_class.showCommonDialog(modelRetailClass, 9, AddNewRetailer.this);
            }
        });
    }


    public int getOutletPosition() {
        for (int i = 0; Retailer_Modal_List.size() > i; i++) {
            if (Retailer_Modal_List.get(i).getId().equals(Shared_Common_Pref.OutletCode)) {
                return i;
            }
        }
        return -1;
    }

    /*Retailer Channel */
    public void getRetailerChannel() {
        String routeMap = "{\"tableName\":\"Doctor_Specialty\",\"coloumns\":\"[\\\"Specialty_Code as id\\\", \\\"Specialty_Name as name\\\"]\"," +
                "\"where\":\"[\\\"isnull(Deactivate_flag,0)=0\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonArray> call = apiInterface.retailerClass(shared_common_pref.getvalue(Shared_Common_Pref.Div_Code),
                shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code), shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code), "24",
                routeMap);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                JsonArray jsonArray = response.body();
                Log.e("RESPONSE_VALUE", String.valueOf(jsonArray));
                for (int a = 0; a < jsonArray.size(); a++) {
                    JsonObject jsonObject = (JsonObject) jsonArray.get(a);
                    String className = String.valueOf(jsonObject.get("name"));
                    String id = String.valueOf(jsonObject.get("id"));
                    if (Shared_Common_Pref.Editoutletflag != null && Shared_Common_Pref.Editoutletflag.equals("1") || (Shared_Common_Pref.Outlet_Info_Flag != null && Shared_Common_Pref.Outlet_Info_Flag.equals("1"))) {
                        if (id.equals(String.valueOf(Retailer_Modal_List.get(getOutletPosition()).getDocSpecialCode()))) {
                            txtRetailerChannel.setText(className.replace('"', ' '));
                            channelID = Integer.valueOf(String.valueOf(jsonObject.get("id")));
                        }
                    }
                    String retailerClass = String.valueOf(className.subSequence(1, className.length() - 1));
                    Log.e("RETAILER_Channel_NAME", retailerClass);
                    mCommon_model_spinner = new Common_Model(id, retailerClass, "flag");
                    Log.e("LeaveType_Request", retailerClass);
                    modelRetailChannel.add(mCommon_model_spinner);
                }

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }

    /*Retailer Channel Click*/
    public void onClickRetailerChannel() {
        linReatilerChannel = findViewById(R.id.linear_retailer_channel);
        txtRetailerChannel = findViewById(R.id.txt_retailer_channel);
        linReatilerChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                common_class.showCommonDialog(modelRetailChannel, 10, AddNewRetailer.this);
            }
        });
    }

    public void addNewRetailers() {
        try {
            if (!imageServer.equalsIgnoreCase("")) {
                Intent mIntent = new Intent(AddNewRetailer.this, FileUploadService.class);
                mIntent.putExtra("mFilePath", imageConvert);
                mIntent.putExtra("SF", UserDetails.getString("Sfcode", ""));
                mIntent.putExtra("FileName", imageServer);
                mIntent.putExtra("Mode", "Outlet");
                FileUploadService.enqueueWork(this, mIntent);
            }
            DateFormat dfw = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Calendar calobjw = Calendar.getInstance();
            KeyDate = shared_common_pref.getvalue(Shared_Common_Pref.Sf_Code);
            keyCodeValue = keyEk + KeyHyp + KeyDate + dfw.format(calobjw.getTime()).hashCode();
            Log.e("KEY_CODE_HASH", keyCodeValue);
            JSONObject reportObject = new JSONObject();
            docMasterObject = new JSONObject();

            reportObject.put("town_code", "'" + routeId + "'");
            reportObject.put("wlkg_sequence", "null");
            reportObject.put("unlisted_doctor_name", "'" + addRetailerName.getText().toString() + "'");
            reportObject.put("unlisted_Owner_name", "'" + owner_name.getText().toString() + "'");
            reportObject.put("unlisted_doctor_pincode", "'" + edt_pin_codeedit.getText().toString() + "'");
            reportObject.put("unlisted_doctor_gst", "'" + edt_gst.getText().toString() + "'");
            reportObject.put("unlisted_doctor_address", "'" + addRetailerAddress.getText().toString().replace("\n", "") + "'");
            reportObject.put("unlisted_doctor_phone", "'" + addRetailerPhone.getText().toString() + "'");
            reportObject.put("unlisted_doctor_secondphone", "'" + etPhoneNo2.getText().toString() + "'");
            if (edt_outstanding.getText().toString().equals(""))
                reportObject.put("outstanding_amount", 0);

            else
                reportObject.put("outstanding_amount", "'" + edt_outstanding.getText().toString());
            reportObject.put("unlisted_doctor_cityname", "'" + addRetailerCity.getText().toString() + "'");
            reportObject.put("State_Code", "'" + stateCode + "'");
            reportObject.put("unlisted_doctor_landmark", "''");
            reportObject.put("unlisted_doctor_mobiledate", common_class.addquote(Common_Class.GetDatewothouttime()));
            reportObject.put("reason_category", common_class.addquote(reason_category_remarks));
            reportObject.put("Compititor_Id", common_class.addquote(Compititor_Id));
            reportObject.put("Compititor_Name", common_class.addquote(Compititor_Name));
            reportObject.put("CatUniverSelectId", common_class.addquote(CatUniverSelectId));
            reportObject.put("AvailUniverSelectId", common_class.addquote(AvailUniverSelectId));
            reportObject.put("HatsunAvailswitch", common_class.addquote(HatsunAvailswitch));
            reportObject.put("categoryuniverseswitch", common_class.addquote(categoryuniverseswitch));
            reportObject.put("lat", common_class.addquote(String.valueOf(Shared_Common_Pref.Outletlat)));
            reportObject.put("long", common_class.addquote(String.valueOf(Shared_Common_Pref.Outletlong)));
            reportObject.put("VechType", txDelvryType.getText().toString());
            reportObject.put("OutletTypeNm", txOutletType.getText().toString());
            reportObject.put("OutletTypeCd", iOutletTyp);


            reportObject.put("unlisted_doctor_areaname", "''");
            reportObject.put("unlisted_doctor_Email", common_class.addquote(addRetailerEmail.getText().toString()));
            reportObject.put("unlisted_doctor_contactperson", "''");
            reportObject.put("unlisted_doctor_designation", "''");
            reportObject.put("unlisted_doctor_phone2", "''");
            reportObject.put("unlisted_doctor_phone3", "''");
            reportObject.put("unlisted_doctor_contactperson2", "''");
            reportObject.put("unlisted_doctor_contactperson3", "''");
            reportObject.put("unlisted_doctor_designation2", "''");
            reportObject.put("unlisted_cat_code", "null");
            reportObject.put("unlisted_specialty_name", txtRetailerChannel.getText().toString());
            reportObject.put("unlisted_specialty_code", channelID);
            reportObject.put("unlisted_qulifi", "'samp'");
            reportObject.put("unlisted_class", classId);
            reportObject.put("id", common_class.addquote(Shared_Common_Pref.OutletCode));
            reportObject.put("DrKeyId", "'" + keyCodeValue + "'");

            //for marked option in explore screen
            reportObject.put("place_id", "'" + place_id + "'");
//
//            String imgName = filePath.substring(filePath.indexOf("/"));
            reportObject.put("img_name", "'" + imageServer + "'");
            reportObject.put(Constants.LOGIN_TYPE, "'" + shared_common_pref.getvalue(Constants.LOGIN_TYPE) + "'");


            //


            docMasterObject.put("unlisted_doctor_master", reportObject);

            mainArray = new JSONArray();
            mainArray.put(docMasterObject);

            String totalValueString = "";
            Map<String, String> QueryString = new HashMap<>();
            if (Shared_Common_Pref.Outler_AddFlag != null && Shared_Common_Pref.Outler_AddFlag.equals("1")) {
                QueryString.put("axn", "dcr/save");
                totalValueString = mainArray.toString();
            } else {
                QueryString.put("axn", "upd/retailer");
                totalValueString = reportObject.toString();
            }
            QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
            QueryString.put("State_Code", Shared_Common_Pref.StateCode);
            QueryString.put("rSF", Shared_Common_Pref.Sf_Code);
            QueryString.put("divisionCode", Shared_Common_Pref.Div_Code);
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            // addNewRetailer
            Log.e("QueryString", totalValueString);


            Call<JsonObject> call = apiInterface.addNewRetailer(QueryString, totalValueString);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    JsonObject jsonObject = response.body();
                    // Log.e("Add_Retailer_details", String.valueOf(jsonObject));
                    String success = String.valueOf(jsonObject.get("success"));

                    common_class.getDataFromApi(Constants.Retailer_OutletList, AddNewRetailer.this, false);

                    if (Shared_Common_Pref.Outler_AddFlag != null && Shared_Common_Pref.Outler_AddFlag.equals("1")) {
                        Toast.makeText(AddNewRetailer.this, "Outlet Added successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddNewRetailer.this, "Outlet Updated successfully", Toast.LENGTH_SHORT).show();
                    }

                    if (Shared_Common_Pref.FromActivity == "Outlets") {
                        Shared_Common_Pref.FromActivity = "";
                        common_class.CommonIntentwithFinish(Outlet_Info_Activity.class);
                    } else if ((success.equalsIgnoreCase("true") && Shared_Common_Pref.Outler_AddFlag.equals("1")) || (success.equalsIgnoreCase("true") && Shared_Common_Pref.Editoutletflag.equals("1"))) {
                        Shared_Common_Pref.Outler_AddFlag = "0";
                        Shared_Common_Pref.Sync_Flag = "1";
                        //startActivity(new Intent(getApplicationContext(), Dashboard_Route.class));
                        common_class.CommonIntentwithFinish(Dashboard_Route.class);
                        // startActivity(new Intent(getApplicationContext(), Offline_Sync_Activity.class));
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnclickMasterType(List<Common_Model> myDataset, int position, int type) {
        common_class.dismissCommonDialog();
        switch (type) {
            case 1:
                tvStateName.setText(myDataset.get(position).getName());
                stateCode = Integer.valueOf(myDataset.get(position).getId());
                break;
            case 2:
                txtRetailerRoute.setText("");
                routeId = "";
                distributor_text.setText(myDataset.get(position).getName());
                findViewById(R.id.rl_route).setVisibility(View.VISIBLE);
                shared_common_pref.save(Constants.TEMP_DISTRIBUTOR_ID, myDataset.get(position).getId());
                common_class.getDb_310Data(Constants.Rout_List, this);
                break;
            case 3:
                routeId = myDataset.get(position).getId();
                txtRetailerRoute.setText(myDataset.get(position).getName());
                break;
            case 9:
                txtRetailerClass.setText(myDataset.get(position).getName());
                classId = Integer.valueOf(myDataset.get(position).getId());
                break;
            case 10:
                txtRetailerChannel.setText(myDataset.get(position).getName());
                channelID = Integer.valueOf(myDataset.get(position).getId());
                break;
            case 11:
                txDelvryType.setText(myDataset.get(position).getName());
                break;
            case 13:
                txOutletType.setText(myDataset.get(position).getName());
                iOutletTyp = Integer.valueOf(myDataset.get(position).getId());
                break;
        }
    }

    public void loadroute(String id) {
        if (Common_Class.isNullOrEmpty(String.valueOf(id))) {
            Toast.makeText(this, "Select the Distributor", Toast.LENGTH_SHORT).show();
        }

        if (FRoute_Master.size() == 1) {
            findViewById(R.id.ivRouteSpinner).setVisibility(View.INVISIBLE);
            txtRetailerRoute.setText(FRoute_Master.get(0).getName());
            routeId = FRoute_Master.get(0).getId();
        } else {
            findViewById(R.id.ivRouteSpinner).setVisibility(View.VISIBLE);
        }
    }

//    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
//            new OnBackPressedDispatcher(new Runnable() {
//                @Override
//                public void run() {
//
//                    finish();
//                }
//            });

    @Override
    public void onBackPressed() {
        finish();
    }

    public void onSuperBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {


            new LocationFinder(this, new LocationEvents() {
                @Override
                public void OnLocationRecived(Location location) {
                    try {
                        refreshLocation(location);
                    } catch (Exception e) {
                    }
                }
            });
            if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
                finalPath = "/storage/emulated/0";
                filePath = outputFileUri.getPath();
                filePath = filePath.substring(1);
                filePath = finalPath + filePath.substring(filePath.indexOf("/"));

                file = new File(filePath);

                ivPhotoShop.setImageURI(Uri.fromFile(file));


            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_state:
                if (stateList == null || stateList.size() == 0)
                    common_class.getDb_310Data(Constants.STATE_LIST, this);
                //else
                common_class.showCommonDialog(stateList, 1, this);
                break;

            case R.id.rl_route:
                if (FRoute_Master != null && FRoute_Master.size() > 1) {
                    common_class.showCommonDialog(FRoute_Master, 3, this);
                }
                break;
            case R.id.rl_Distributor:
                common_class.showCommonDialog(distributor_master, 2, this);
                break;
            case R.id.copypaste:
                addRetailerAddress.setText(CurrentLocationsAddress.getText().toString());
                break;

            case R.id.ivShopPhoto:
                try {
                    AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                        @Override
                        public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {
                            imageServer = FileName;
                            imageConvert = fullPath;
                            ivPhotoShop.setImageBitmap(image);
                        }
                    });
                    Intent intent = new Intent(AddNewRetailer.this, AllowancCapture.class);
                    intent.putExtra("allowance", "One");
                    startActivity(intent);
                } catch (Exception e) {
                    Log.v(TAG, ":imageClk:" + e.getMessage());
                }
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        centreMapOnLocation("Your Location");
    }

    public void centreMapOnLocation(String title) {

        LatLng userLocation = new LatLng(RetLat, RetLng);
        mGoogleMap.clear();
        mGoogleMap.addMarker(new MarkerOptions().position(userLocation).title(title));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16));

    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {
            if (apiDataResponse != null) {

                switch (key) {
                    case Rout_List:
                        JSONArray routeArr = new JSONArray(apiDataResponse);
                        FRoute_Master.clear();
                        for (int i = 0; i < routeArr.length(); i++) {
                            JSONObject jsonObject1 = routeArr.getJSONObject(i);
                            String id = String.valueOf(jsonObject1.optInt("id"));
                            String name = jsonObject1.optString("name");
                            String flag = jsonObject1.optString("FWFlg");
                            Model_Pojo = new Common_Model(id, name, flag);
                            Model_Pojo = new Common_Model(id, name, jsonObject1.optString("stockist_code"));
                            FRoute_Master.add(Model_Pojo);

                        }
                        loadroute(shared_common_pref.getvalue(Constants.TEMP_DISTRIBUTOR_ID));
                        break;
                    case Constants.STATE_LIST:
                        JSONObject stateObj = new JSONObject(apiDataResponse);
                        if (stateObj.getBoolean("success")) {
                            stateList = new ArrayList<>();
                            stateList.clear();

                            JSONArray array = stateObj.getJSONArray("Data");

                            String strState = "";
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);
                                strState = strState + obj.getString("State_Code") + obj.getString("StateName") + ",";
                                stateList.add(new Common_Model(obj.getString("StateName"), obj.getString("State_Code")));
                            }

                            if (!Shared_Common_Pref.Outler_AddFlag.equals("1")) {
                                strState = strState.substring(strState.indexOf(Retailer_Modal_List.get(getOutletPosition()).getStateCode()) + Retailer_Modal_List.get(getOutletPosition()).getStateCode().length());
                                strState = strState.substring(0, strState.indexOf(","));
                                tvStateName.setText("" + strState);
                            }


                        }
                        break;
                }

            }

        } catch (Exception e) {

        }

    }
}

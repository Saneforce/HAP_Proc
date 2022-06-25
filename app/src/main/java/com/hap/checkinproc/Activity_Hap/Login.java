package com.hap.checkinproc.Activity_Hap;

import static android.widget.Toast.LENGTH_LONG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.common.reflect.TypeToken;
import com.google.firebase.BuildConfig;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hap.checkinproc.Common_Class.CameraPermission;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Model_Class.Model;
import com.hap.checkinproc.R;
import com.hap.checkinproc.common.DatabaseHandler;
import com.hap.checkinproc.common.FileUploadService;
import com.hap.checkinproc.common.LocationReceiver;
import com.hap.checkinproc.common.SANGPSTracker;
import com.hap.checkinproc.common.TimerService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    TextInputEditText name, password;
    Button btnLogin;
    ImageView profileImage;
    String photo;
    String idToken, eMail, UserLastName, UserLastName1;
    Button signInButton, ReportsButton, ExitButton;
    Shared_Common_Pref shared_common_pref;
    private static final String TAG = "LoginActivity";
    private GoogleApiClient googleApiClient;
    private final static int RC_SIGN_IN = 1;
    private final static int RC_MYREPORTS = 2;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    SharedPreferences UserDetails;
    SharedPreferences CheckInDetails;
    public static final String CheckInDetail = "CheckInDetail";
    public static final String MyPREFERENCES = "MyPrefs";
    private ProgressDialog mProgress;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1001;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    final Handler handler = new Handler();
    private LocationReceiver rcvMReceiver;
    private SANGPSTracker mLUService;
    private LocationReceiver myReceiver;
    private TimerService mTimerService;

    String deviceToken = "";
    Uri profile;
    // Tracks the bound state of the service.
    private boolean mBound = false;
    ApiInterface apiInterface;
    CameraPermission cameraPermission;
    Common_Class DT = new Common_Class();
    DatabaseHandler db;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = new DatabaseHandler(this);
        shared_common_pref = new Shared_Common_Pref(this);

        if (com.hap.checkinproc.Common_Class.Common_Class.GetDatewothouttime().equalsIgnoreCase(shared_common_pref.getvalue(Constants.LOGIN_DATE))) {
            Shared_Common_Pref.LOGINTYPE = shared_common_pref.getvalue(Constants.LOGIN_TYPE);
        } else {
            shared_common_pref.clear_pref(Constants.LOGIN_DATA);
            shared_common_pref.clear_pref(Constants.VAN_STOCK_LOADING);
        }


        JSONArray pendingPhotos = db.getAllPendingPhotos();
        if (pendingPhotos.length() > 0) {
            try {
                JSONObject itm = pendingPhotos.getJSONObject(0);
                Intent mIntent = new Intent(Login.this, FileUploadService.class);
                mIntent.putExtra("mFilePath", itm.getString("FileURI"));
                mIntent.putExtra("SF", itm.getString("SFCode"));
                mIntent.putExtra("FileName", itm.getString("FileName"));
                mIntent.putExtra("Mode", itm.getString("Mode"));
                FileUploadService.enqueueWork(Login.this, mIntent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        deviceToken = task.getResult();
                        Log.e("LoginActivity", deviceToken);
                    }
                });



/* mRegistrationBroadcastReceiver = new BroadcastReceiver() {
 @Override
 public void onReceive(Context context, Intent intent) {

 // checking for type intent filter
 if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
 // gcm successfully registered
 // now subscribe to `global` topic to receive app wide notifications
 FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

 displayFirebaseRegId();

 } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
 // new push notification is received

 String message = intent.getStringExtra("message");

 Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

 // txtMessage.setText(message);
 }
 }
 };*/

        //displayFirebaseRegId();

        name = (TextInputEditText) findViewById(R.id.username);
        password = (TextInputEditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        profileImage = (ImageView) findViewById(R.id.profile_image);
        UserDetails = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        CheckInDetails = getSharedPreferences(CheckInDetail, Context.MODE_PRIVATE);
        mProgress = new ProgressDialog(this);
        String titleId = "Signing in...";
        mProgress.setTitle(titleId);
        mProgress.setMessage("Please Wait...");

        deviceToken = shared_common_pref.getvalue(Shared_Common_Pref.Dv_ID);

        eMail = UserDetails.getString("email", "");
        UserLastName = UserDetails.getString("DesigNm", "");
        UserLastName1 = UserDetails.getString("DepteNm", "");
        name.setText(eMail);

        cameraPermission = new CameraPermission(Login.this, getApplicationContext());

        if (!cameraPermission.checkPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraPermission.requestPermission();
                Log.v("PERMISSION_NOT", "PERMISSION_NOT");
            }
            Log.v("PERMISSION_NOT", "PERMISSION_NOT");
        } else {
            Log.v("PERMISSION", "PERMISSION");
        }


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(name.getText().toString())) {
                    Toast.makeText(Login.this, "username/password required", Toast.LENGTH_SHORT).show();
                } else {
                    //proceed to login
                    mProgress.show();
                    login(0);
                }
            }
        });
        signInButton = (Button) findViewById(R.id.sign_in_button);

        firebaseAuth = FirebaseAuth.getInstance();
        //this is where we start the Auth state Listener to listen for whether the user is signed in or not
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mLUService = new SANGPSTracker(Login.this);
                myReceiver = new LocationReceiver();
                // Bind to the service. If the service is in foreground mode, this signals to the service
                // that since this activity is in the foreground, the service can exit foreground mode.

                Boolean DAMode = shared_common_pref.getBoolValue(Shared_Common_Pref.DAMode);
                if (DAMode == true) {

                    if (isMyServiceRunning(SANGPSTracker.class) == false) {
                        try {
                            bindService(new Intent(getApplicationContext(), SANGPSTracker.class), mServiceConection,
                                    Context.BIND_AUTO_CREATE);
                            LocalBroadcastManager.getInstance(getApplication()).registerReceiver(myReceiver,
                                    new IntentFilter(SANGPSTracker.ACTION_BROADCAST));
                            mLUService.requestLocationUpdates();
                        } catch (Exception e) {
                        }
                    }
                }
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                Login.this.startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });

        ReportsButton = (Button) findViewById(R.id.btnReport);
        ReportsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(signInIntent, RC_MYREPORTS);
            }
        });
        ExitButton = (Button) findViewById(R.id.btnExit);
        ExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shared_common_pref.clear_pref(Constants.LOGIN_DATA);
                SharedPreferences.Editor editor = UserDetails.edit();
                editor.putBoolean("Login", false);
                editor.apply();
                CheckInDetails.edit().clear().commit();
                finishAffinity();
            }
        });
 /*
 if(UserDetails.getString("Sfcode", "")!="") {
 Call<JsonArray> Callto = apiInterface.getDataArrayList("get/Signout_Check",
 UserDetails.getString("Divcode", ""),
 UserDetails.getString("Sfcode", ""));
 Callto.enqueue(new Callback<JsonArray>() {

 @Override
 public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

 }

 @Override
 public void onFailure(Call<JsonArray> call, Throwable t) {

 }
 });
 }*/

        Boolean Login = UserDetails.getBoolean("Login", false);
        Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
        Intent inten = new Intent(this, TimerService.class);
        startService(inten);
        if (Login == true || CheckIn == true) {
            /*PERMISSION REQUEST*/
            if (cameraPermission.checkPermission()) {

                Boolean DAMode = shared_common_pref.getBoolValue(Shared_Common_Pref.DAMode);
                if (DAMode == true) {
                    if (isMyServiceRunning(SANGPSTracker.class) == false) {
                        try {
                            Intent playIntent = new Intent(this, SANGPSTracker.class);
                            bindService(playIntent, mServiceConection, Context.BIND_AUTO_CREATE);
                            startService(playIntent);
                        } catch (Exception e) {
                        }
                    }
                }
            }

            if (Login == true && CheckIn == false) {

                try {
                    mProgress.show();
                } catch (Exception e) {

                }
                login(RC_SIGN_IN);
            } else if (CheckIn == true) {

                Shared_Common_Pref.Sf_Code = UserDetails.getString("Sfcode", "");
                Shared_Common_Pref.Sf_Name = UserDetails.getString("SfName", "");
                Shared_Common_Pref.Div_Code = UserDetails.getString("Divcode", "");
                Shared_Common_Pref.StateCode = UserDetails.getString("State_Code", "");

                String ActStarted = shared_common_pref.getvalue("ActivityStart");

                if (shared_common_pref.getvalue(Constants.LOGIN_TYPE).equals(Constants.DISTRIBUTER_TYPE)) {
                    Shared_Common_Pref.LOGINTYPE = Constants.DISTRIBUTER_TYPE;
                    startActivity(new Intent(this, SFA_Activity.class));
                } else {
                    if (ActStarted.equalsIgnoreCase("true")) {
                        Intent aIntent;
                        String sDeptType = UserDetails.getString("DeptType", "");
                        if (sDeptType.equalsIgnoreCase("1")) {
                            //bommu
                            // aIntent = new Intent(getApplicationContext(), ProcurementDashboardActivity.class);
                            aIntent = new Intent(getApplicationContext(), SFA_Activity.class);

                        } else {
                            Shared_Common_Pref.Sync_Flag = "0";
                            if (checkValueStore())
                                aIntent = new Intent(getApplicationContext(), SFA_Activity.class);
                            else {
                                aIntent = new Intent(getApplicationContext(), Dashboard_Two.class);
                                aIntent.putExtra("Mode", "CIN");
                            }
                        }
                        startActivity(aIntent);
                    } else {
                        Intent Dashboard = new Intent(Login.this, Dashboard_Two.class);
                        Dashboard.putExtra("Mode", "CIN");
                        startActivity(Dashboard);
                    }
                }
            }
        }


    }

    boolean checkValueStore() {
        try {
            //  JSONArray storeData = db.getMasterData(Constants.Distributor_List);
            JSONArray storeData = new JSONArray(shared_common_pref.getvalue(Constants.Distributor_List));
            if (storeData != null && storeData.length() > 0)
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

 /* private void displayFirebaseRegId() {
 SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
 String regId = pref.getString("regId", null);

 Log.e(TAG, "Firebase reg id: " + regId);

*//* if (!TextUtils.isEmpty(regId))
 txtRegId.setText("Firebase Reg Id: " + regId);
 else
 txtRegId.setText("Firebase Reg Id is not received yet!");*//*
 }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN || requestCode == RC_MYREPORTS) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            // assert result != null;
            if (result.isSuccess()) {
                Log.e("Result_success", result.getStatus().toString());
            }
            Log.e("Result_success", result.getStatus().toString());

            Log.e("Result_googel", String.valueOf(result));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                handleSignInResult(result, requestCode);
            }
        }
    }

    private void handleSignInResult(GoogleSignInResult result, int requestCode) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            //assert account != null;
            Log.d("LoginDetails", String.valueOf(account.getPhotoUrl()));
            idToken = account.getIdToken();
            name.setText(account.getEmail());
            profile = (account.getPhotoUrl());
            eMail = account.getEmail();
            UserLastName = account.getFamilyName().replace("- ", "")
                    .replace("(", "")
                    .replace(")", "")
                    .replace("/", "-");
            UserLastName1 = account.getDisplayName();
            try {
                Glide.with(this).load(account.getPhotoUrl()).into(profileImage);
                photo = account.getPhotoUrl().toString();
                shared_common_pref.save(Shared_Common_Pref.Profile, account.getPhotoUrl().toString());
            } catch (NullPointerException e) {
                Toast.makeText(getApplicationContext(), "image not found", LENGTH_LONG).show();
            }
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            firebaseAuthWithGoogle(credential);
            login(requestCode);
        } else {
            mProgress.dismiss();
            Toast.makeText(getApplicationContext(), "Sign in cancel", LENGTH_LONG).show();
        }
    }

    private void firebaseAuthWithGoogle(AuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        if (task.isSuccessful()) {
                        } else {
                            Log.w(TAG, "signInWithCredential" + task.getException().getMessage());
                            task.getException().printStackTrace();
                        }
                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v("LOG_IN_LOCATION", "ONRESTART");
    }

    @Override
    protected void onResume() {
        super.onResume();
       /* Intent inten = new Intent(this, TimerService.class);
        startService(inten);*/
        Log.v("LOG_IN_LOCATION", "ONRESUME");
        /*REQUEST PERMISISON*/
        if (cameraPermission.checkPermission()) {
            if (mLUService == null)
                mLUService = new SANGPSTracker(getApplicationContext());
            myReceiver = new LocationReceiver();

            // Bind to the service. If the service is in foreground mode, this signals to the service
            // that since this activity is in the foreground, the service can exit foreground mode.

            Boolean DAMode = shared_common_pref.getBoolValue(Shared_Common_Pref.DAMode);
            if (DAMode == true) {
                if (isMyServiceRunning(SANGPSTracker.class) == false) {
                    try {
                        bindService(new Intent(this, SANGPSTracker.class), mServiceConection,
                                Context.BIND_AUTO_CREATE);
                        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
                                new IntentFilter(SANGPSTracker.ACTION_BROADCAST));
                    } catch (Exception e) {
                    }
                }
            }
            Log.e("Loaction_Check", "Loaction_Check");
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("LOG_IN_LOCATION", "ONPAUSE");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);

    }


/*// LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
 super.onPause();
 }*/


    @Override
    protected void onStart() {
        Log.v("LOG_IN_LOCATION", "ONSTART");
        super.onStart();
        if (authStateListener != null) {
            FirebaseAuth.getInstance().signOut();
        }
        firebaseAuth.addAuthStateListener(authStateListener);


//        if(getIntent().getStringExtra("loading")!=null)
//            assignLoginData();
    }

    @Override
    protected void onStop() {
 /* if (mBound) {
 // Unbind from the service. This signals to the service that this activity is no longer
 // in the foreground, and the service can respond by promoting itself to a foreground
 // service.
 unbindService(mServiceConection);
 mBound = false;

 }*/
        super.onStop();
     /*   Intent inten = new Intent(this, TimerService.class);
        startService(inten);
*/

        Log.v("LOG_IN_LOCATION", "ONSTOP");

        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }


    public void login(int requestCode) {
        try {
            Gson gson = new Gson();
            if (!Common_Class.isNullOrEmpty(shared_common_pref.getvalue(Constants.LOGIN_DATA))) {
                String loginData = shared_common_pref.getvalue(Constants.LOGIN_DATA);
                Type userType = new TypeToken<Model>() {
                }.getType();
                Model response = gson.fromJson(loginData, userType);
                assignLoginData(response, requestCode);
            } else {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    if (eMail.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Invalid Email ID", LENGTH_LONG).show();
                        mProgress.dismiss();
                        return;
                    }
                }

                //eMail = "sakthivel.sa@hap.in";
                //eMail = "srinivas.p@hap.in";
                //eMail = "sajan@hap.in";
                //   eMail = "1005985@hap.in";
                //eMail = "haptest5@hap.in";
                //eMail = "testhap3@hap.in";

                //eMail = "ciadmin@hap.in";
                // eMail = "rajkumar@hap.in";
                //  eMail = "haptest5@hap.in";
                // eMail = "senthilraja.d@hap.in";
                // eMail = "pachamuthu.m@hap.in";
                // eMail="akshay.r@hap.in";
                // eMail = "1025751@hap.in";//test

                //   eMail = "1014700@hap.in";
                // eMail = "1006208@hap.in";
                // eMail = "1009998@hap.in";
                // eMail = "testuser@gmail.com";
                //  eMail = "anbu@saneforce.com";
//                eMail = "1016431@hap.in";
//                eMail = "1022324@hap.in";
                // eMail = "1025257@hap.in";
                // eMail = "1026494@hap.in";
//                eMail = "1007660@hap.in";//pranita
                //eMail = "1026508@hap.in";//alaguselvi
                // eMail = "1012639@hap.in";//md
                //  eMail="1014022@hap.in";//rmk

                // eMail = "1006626@hap.in";
                // eMail = "1006345@hap.in";
                //eMail = "1006812@hap.in";
                // eMail = "1013362@hap.in";//(-18)
                // eMail="ssiva2519@gmail.com";
                //  eMail = "1013362@hap.in";
                // eMail="1021453@hap.in";
                //eMail = "anbu@saneforce.com";
                // eMail="1018656@hap.in";

                //eMail="sivakumar.s@hap.in";

                // eMail = "1026710@hap.in";

                // eMail="1017169@hap.in";


                //eMail = "1006208@hap.in";
                // eMail="rajasekaranm@hap.in";
                //   eMail="1018368@hap.in";
                //eMail="sajan@hap.in";
                // eMail="1022081@hap.in";
                // eMail="johnkennedy.i@hap.in";
                //eMail = "1018937@hap.in";

                // eMail="1006812@hap.in";//ramalakshmi
                // eMail="1018821@hap.in";//md food services
                // eMail="1006815@hap.in";
                //  eMail="1977ananthkumar@gmail.com";
                //eMail = "gpmuthu11@gmail.com";
//                eMail = "1015742@hap.in";
//                eMail = "1021230@hap.in";
                // eMail="shiva.saneforce@gmail.com";
                // eMail="rajasekaranm@hap.in";
                // eMail="1014700@hap.in";

                //   eMail="1026594@hap.in";
                //   eMail = "gnanaoli.j@hap.in";
                // eMail = "testhap3@hap.in";
                //eMail = "shanmugam@hap.in";
                //eMail = "anbu@saneforce.in";
                //  eMail="1010057@hap.in";
                // eMail="1008672@hap.in";
               // eMail = "1014499@hap.in";//FRom
                //eMail="1015603@hap.in";//TO


                Call<Model> modelCall = apiInterface.login("get/GoogleLogin", eMail, BuildConfig.VERSION_NAME, deviceToken);
                modelCall.enqueue(new Callback<Model>() {
                    @Override
                    public void onResponse(Call<Model> call, Response<Model> response) {
                        try {
                            if (response.isSuccessful()) {

                                if (response.body().getSuccess() == true) {

                                    try {
                                        Gson gson = new Gson();
                                        assignLoginData(response.body(), requestCode);
                                        shared_common_pref.save(Constants.LOGIN_DATA, gson.toJson(response.body()));

//                                        try {
//                                            PackageManager manager = getPackageManager();
//
//                                            if (response.body().getData().get(0).getLoginType().equalsIgnoreCase("Distributor")) {
//
//                                                // enable old icon
//                                                manager.setComponentEnabledSetting(new ComponentName(Login.this, DistributorLauncherAlias.class)
//                                                        , PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
//
//                                                // disable new icon
//                                                manager.setComponentEnabledSetting(new ComponentName(Login.this, FFALauncherAlias.class)
//                                                        , PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
//
//                                                manager.setComponentEnabledSetting(new ComponentName(Login.this, DefaultLauncherAlias.class)
//                                                        , PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
//
//                                                Toast.makeText(Login.this, "Enable " + Constants.DISTRIBUTER_TYPE + " Icon", Toast.LENGTH_LONG).show();
//
//
//                                            } else {
//
//                                                manager.setComponentEnabledSetting(new ComponentName(Login.this, FFALauncherAlias.class)
//                                                        , PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
//
//                                                // disable new icon
//                                                manager.setComponentEnabledSetting(new ComponentName(Login.this, DistributorLauncherAlias.class)
//                                                        , PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
//
//                                                manager.setComponentEnabledSetting(new ComponentName(Login.this, DefaultLauncherAlias.class)
//                                                        , PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
//
//                                                Toast.makeText(Login.this, "Enable " + Constants.CHECKIN_TYPE + " Icon", Toast.LENGTH_LONG).show();
//
//
//                                            }
//
//                                        } catch (Exception e) {
//                                            Log.v("launcherIcon:", e.getMessage());
//                                        }
//
//                                        mProgress.setTitle("Please wait while we configure...");
//                                        mProgress.show();
//
//                                        handler.postDelayed(new Runnable() {
//                                            public void run() {
//                                                assignLoginData(response.body(), requestCode);
//
//
//                                            }
//                                        }, 1000);


                                    } catch (Exception e) {
                                        Log.v("Login:assign", e.getMessage());
                                    }
                                } else {
                                    try {
                                        mProgress.dismiss();
                                    } catch (Exception e) {

                                    }
                                    Toast.makeText(getApplicationContext(), "Check username and password", LENGTH_LONG).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.v("Login:response", e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<Model> call, Throwable t) {

                        Toast.makeText(getApplicationContext(), t.getMessage(), LENGTH_LONG).show();
                        try {
                            mProgress.dismiss();
                        } catch (Exception e) {

                        }
                    }
                });
            }
        } catch (Exception e) {
            Log.v("LOGIN:", e.getMessage());
        }
    }


    @SuppressLint("NewApi")
    void assignLoginData(Model response, int requestCode) {
        try {

            shared_common_pref.save(Constants.LOGIN_DATE, com.hap.checkinproc.Common_Class.Common_Class.GetDatewothouttime());

            Gson gson = new Gson();

            SharedPreferences.Editor userEditor = UserDetails.edit();
            SharedPreferences.Editor cInEditor = CheckInDetails.edit();


            if (response.getData().get(0).getLoginType() != null &&
                    response.getData().get(0).getLoginType().equals("Distributor")) {
                shared_common_pref.save(Constants.SALES_RETURN_FILECOUNT, response.getData().get(0).getSalesReturnImg());

                shared_common_pref.save(Constants.Distributor_Id, response.getData().get(0).getDistCode());
                shared_common_pref.save(Constants.TEMP_DISTRIBUTOR_ID, response.getData().get(0).getDistCode());
                shared_common_pref.save(Constants.Distributor_name, response.getData().get(0).getStockist_Name());
                shared_common_pref.save(Constants.Distributor_phone, response.getData().get(0).getStockist_Mobile());
                shared_common_pref.save(Constants.LOGIN_TYPE, Constants.DISTRIBUTER_TYPE);
                shared_common_pref.save(Constants.CUTOFF_TIME, response.getData().get(0).getCutoffTime());
                shared_common_pref.save(Constants.SlotTime, gson.toJson(response.getData().get(0).getSlotTime()));
                shared_common_pref.save(Constants.DistributorERP, response.getData().get(0).getERP_Code());
                shared_common_pref.save(Constants.DivERP, response.getData().get(0).getDivERP());
                shared_common_pref.save(Constants.DistributorAdd, response.getData().get(0).getStockist_Address());
                shared_common_pref.save(Constants.CusSubGrpErp, response.getData().get(0).getCusSubGrpErp());

                Shared_Common_Pref.LOGINTYPE = Constants.DISTRIBUTER_TYPE;
                userEditor.putString("Sfcode", response.getData().get(0).getDistCode());
                userEditor.putString("Divcode", response.getData().get(0).getDivisionCode());
                userEditor.putString("State_Code", response.getData().get(0).getState_Code());
                userEditor.putInt("FlightAllowed", 0);

                Shared_Common_Pref.Sf_Code = response.getData().get(0).getDistCode();
                Shared_Common_Pref.Div_Code = response.getData().get(0).getDivisionCode();

                shared_common_pref.save(Shared_Common_Pref.Div_Code, response.getData().get(0).getDivisionCode());
                shared_common_pref.save(Shared_Common_Pref.Sf_Code, response.getData().get(0).getDistCode());

                userEditor.putString("email", eMail);
                if (!UserLastName.equalsIgnoreCase("")) {
                    userEditor.putString("DesigNm", UserLastName);
                    userEditor.putString("DepteNm", UserLastName1);
                }
                userEditor.putString("SfName", response.getData().get(0).getStockist_Name());

                userEditor.putString("url", String.valueOf(profile));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    userEditor.apply();
                }
                if (requestCode == RC_SIGN_IN || requestCode == 0)
                    userEditor.putBoolean("Login", true);
                else
                    userEditor.putBoolean("Login", false);
                userEditor.apply();

                cInEditor.putBoolean("CheckIn", true);
                cInEditor.apply();
                shared_common_pref.save(Constants.Freezer_Mandatory, response.getData().get(0).getFreezer_Mandatory());
                startActivity(new Intent(Login.this, SFA_Activity.class));

            } else {
                shared_common_pref.save(Constants.LOGIN_TYPE, Constants.CHECKIN_TYPE);
                Shared_Common_Pref.LOGINTYPE = Constants.CHECKIN_TYPE;
                Intent intent = null;
                Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
                JsonArray CinData = response.getCInData();
                if (CinData.size() > 0) {
                    JsonObject CinObj = CinData.get(0).getAsJsonObject();
                    cInEditor.putString("Shift_Selected_Id", CinObj.get("Sft_ID").getAsString());
                    cInEditor.putString("Shift_Name", CinObj.get("Sft_Name").getAsString());
                    ///if(CinObj.getAsJsonObject("End_Time").isJsonNull()!= true)
                    if (CinObj.get("End_Time").isJsonNull() != true)
                        cInEditor.putString("CINEnd", CinObj.getAsJsonObject("End_Time").get("date").getAsString());
                    else
                        cInEditor.putString("CINEnd", "");
                    cInEditor.putString("ShiftStart", CinObj.getAsJsonObject("Sft_STime").get("date").getAsString());
                    cInEditor.putString("ShiftEnd", CinObj.getAsJsonObject("sft_ETime").get("date").getAsString());
                    cInEditor.putString("ShiftCutOff", CinObj.getAsJsonObject("ACutOff").get("date").getAsString());
                    cInEditor.putString("On_Duty_Flag", CinObj.get("Wtp").getAsString());

                    String CTime = DT.getDateWithFormat(CinObj.getAsJsonObject("Start_Time").get("date").getAsString(), "HH:mm:ss");
                    int Type = CinObj.get("Type").getAsInt();
                    if (CheckInDetails.getString("FTime", "").equalsIgnoreCase(""))
                        cInEditor.putString("FTime", CTime);
                    cInEditor.putString("Logintime", CTime);
                    if (Type == 0) CheckIn = true;
                    cInEditor.putBoolean("CheckIn", CheckIn);
                    cInEditor.apply();
                }


                if (requestCode == RC_SIGN_IN) {
                    if (CheckIn == true) {
                        intent = new Intent(Login.this, Dashboard_Two.class);
                        intent.putExtra("Mode", "CIN");
                    } else {
                        intent = new Intent(Login.this, Dashboard.class);
                    }
                } else {
                    intent = new Intent(Login.this, Dashboard_Two.class);
                    intent.putExtra("Mode", "RPT");
                }

                String code = response.getData().get(0).getSfCode();
                String Sf_type = String.valueOf(response.getData().get(0).getSFFType());
                String empID = response.getData().get(0).getSfEmpId();
                String sName = response.getData().get(0).getSfName();
                String div = response.getData().get(0).getDivisionCode();
                Integer type = response.getData().get(0).getCheckCount();
                String DesigNm = response.getData().get(0).getSfDesignationShortName();
                String SFRptCd = response.getData().get(0).getSfRptCode();
                String SFRptNm = response.getData().get(0).getSfRptName();
                String DeptCd = response.getData().get(0).getSFDept();
                String DeptNm = response.getData().get(0).getDeptName();
                String DeptType = response.getData().get(0).getDeptType();
                String SFHQ = response.getData().get(0).getsFHQ();
                String SFHQID = response.getData().get(0).getHQID();
                String SFHQCode = response.getData().get(0).getHQCode();
                String SFHQLoc = response.getData().get(0).getHOLocation();
                int THrsPerm = response.getData().get(0).getTHrsPerm();
                String mProfile = response.getData().get(0).getProfile();
                String mProfPath = response.getData().get(0).getProfPath();
                Integer OTFlg = response.getData().get(0).getOTFlg();
                Integer Flight = response.getData().get(0).getFlightAllowed();
                if (Flight == null) Flight = 0;
                shared_common_pref.save(Constants.Freezer_Mandatory, response.getData().get(0).getFreezer_Mandatory() == null ? 0 : response.getData().get(0).getFreezer_Mandatory());


                /* Unwanted Lines */
                Shared_Common_Pref.Sf_Code = code;
                Shared_Common_Pref.Sf_Name = response.getData().get(0).getSfName();
                Shared_Common_Pref.Div_Code = div;
                Shared_Common_Pref.StateCode = Sf_type;
                shared_common_pref.save(Shared_Common_Pref.Sf_Code, code); //l
                shared_common_pref.save(Shared_Common_Pref.Div_Code, div); //l
                shared_common_pref.save(Shared_Common_Pref.StateCode, Sf_type); //l
                shared_common_pref.save(Shared_Common_Pref.SF_EMP_ID, response.getData().get(0).getSfEmpId()); //l
                shared_common_pref.save(Shared_Common_Pref.Sf_Name, response.getData().get(0).getSfName()); //l
                shared_common_pref.save(Shared_Common_Pref.SF_DEPT, response.getData().get(0).getDeptName()); //l
                shared_common_pref.save(Shared_Common_Pref.SF_DESIG, response.getData().get(0).getSfDesignationShortName()); //l
                shared_common_pref.save(Shared_Common_Pref.CHECK_COUNT, String.valueOf(type)); //l
                shared_common_pref.save(Shared_Common_Pref.Sf_Code, code); //l
                Shared_Common_Pref.Dept_Type = DeptType;
                Shared_Common_Pref.SF_Type = Sf_type;
                /*Endof Unwanted Lines*/

                userEditor.putString("Sf_Type", Sf_type);
                userEditor.putString("Sfcode", code);
                userEditor.putString("EmpId", empID);
                userEditor.putString("SfName", sName);
                userEditor.putString("SFDesig", DesigNm);
                userEditor.putString("SFRptCode", SFRptNm);
                userEditor.putString("SFRptName", SFRptNm);
                userEditor.putString("SFHQ", SFHQ);
                userEditor.putString("SFHQCode", SFHQCode);
                userEditor.putString("SFHQID", SFHQID);
                userEditor.putInt("THrsPerm", THrsPerm);
                userEditor.putString("Divcode", div);
                userEditor.putInt("CheckCount", type);
                userEditor.putString("DeptCd", DeptCd);
                userEditor.putString("DeptNm", DeptNm);
                userEditor.putString("DeptType", DeptType);
                userEditor.putInt("FlightAllowed", Flight);
                userEditor.putInt("OTFlg", OTFlg);
                Log.d("DeptType", String.valueOf(DeptType));
                userEditor.putString("State_Code", Sf_type);
                userEditor.putString("email", eMail);
                userEditor.putString("HOLocation", SFHQLoc);
                try {
                    userEditor.putString("radius", String.valueOf(response.getData().get(0).getRadius()) == null ? "0.0" : String.valueOf(response.getData().get(0).getRadius()));
                } catch (Exception e) {

                }
                if (!UserLastName.equalsIgnoreCase("")) {
                    userEditor.putString("DesigNm", UserLastName);
                    userEditor.putString("DepteNm", UserLastName1);
                }

                userEditor.putString("url", String.valueOf(profile));
                userEditor.putString("Profile", String.valueOf(mProfile));
                userEditor.putString("ProfPath", String.valueOf(mProfPath));

                userEditor.apply();
                if (requestCode == RC_SIGN_IN || requestCode == 0)
                    userEditor.putBoolean("Login", true);
                else
                    userEditor.putBoolean("Login", false);
                userEditor.apply();
                startActivity(intent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                    overridePendingTransition(R.anim.in, R.anim.out);
                }

                try {
                    mProgress.dismiss();
                } catch (Exception e) {

                }
            }
        } catch (Exception e) {
            Log.v("Login:assign", e.getMessage());
        }
    }

    private boolean checkPermission() {
        return (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
        //PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    //Location service part
    private void requestPermissions() {
        boolean shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (shouldProvideRationale) {
            Snackbar.make(
                            findViewById(R.id.activity_main),
                            R.string.permission_rationale,
                            Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(Login.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION, Manifest.permission.FOREGROUND_SERVICE},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    })
                    .show();
        } else
            ActivityCompat.requestPermissions(Login.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION, Manifest.permission.FOREGROUND_SERVICE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length <= 0) {
                    // Permission was not granted.
                } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted.
                    if (mLUService == null)
                        mLUService = new SANGPSTracker(getApplicationContext());
                    //mLUService.requestLocationUpdates();
                } else {
                    // Permission denied.
                    //Snackbar snackbar =
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
                            .setTextColor(Color.WHITE)
                            .show();
                }
        }
    }

    private final ServiceConnection mServiceConection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mLUService = ((SANGPSTracker.LocationBinder) service).getLocationUpdateService(getApplicationContext());
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mLUService = null;
            mBound = false;
        }
    };

}
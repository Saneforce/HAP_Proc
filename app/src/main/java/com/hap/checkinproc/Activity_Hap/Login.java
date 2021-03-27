package com.hap.checkinproc.Activity_Hap;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import androidx.multidex.BuildConfig;

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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hap.checkinproc.Common_Class.CameraPermission;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Model_Class.Model;
import com.hap.checkinproc.R;
import com.hap.checkinproc.common.LocationReceiver;
import com.hap.checkinproc.common.SANGPSTracker;
import com.hap.checkinproc.common.TimerService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.LENGTH_LONG;

public class Login extends AppCompatActivity {
    TextInputEditText name, password;
    Button btnLogin;
    ImageView profileImage;
    String photo;
    String idToken, eMail;
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


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


                        // Log and toast

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
        shared_common_pref = new Shared_Common_Pref(this);
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
                bindService(new Intent(getApplicationContext(), SANGPSTracker.class), mServiceConection,
                        Context.BIND_AUTO_CREATE);
                LocalBroadcastManager.getInstance(getApplication()).registerReceiver(myReceiver,
                        new IntentFilter(SANGPSTracker.ACTION_BROADCAST));
                mLUService.requestLocationUpdates();
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });

        ReportsButton = (Button) findViewById(R.id.btnReport);
        ReportsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLUService == null)
                    mLUService = new SANGPSTracker(getApplicationContext());
 /* if(mTimerService == null)
 mTimerService = new TimerService();
 mTimerService.startTimerService();*/

                /*startService(new Intent(Login.this, TimerService.class));*/

                mLUService.requestLocationUpdates();
                mProgress.show();
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(signInIntent, RC_MYREPORTS);
            }
        });
        ExitButton = (Button) findViewById(R.id.btnExit);
        ExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = UserDetails.edit();
                editor.putBoolean("Login", false);
                editor.apply();
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

        if (Login == true || CheckIn == true) {

            /*PERMISSION REQUEST*/
            if (cameraPermission.checkPermission()) {
                Intent playIntent = new Intent(this, SANGPSTracker.class);
                bindService(playIntent, mServiceConection, Context.BIND_AUTO_CREATE);
                startService(playIntent);

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
                Intent Dashboard = new Intent(Login.this, Dashboard_Two.class);
                Dashboard.putExtra("Mode", "CIN");
                startActivity(Dashboard);
            }
        }


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
            handleSignInResult(result, requestCode);
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
            try {
                Glide.with(this).load(account.getPhotoUrl()).into(profileImage);
                Log.e("aara", account.getPhotoUrl().toString());
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

     /*   Intent inten = new Intent(this, TimerService.class);
        startService(inten);*/
    }

    @Override
    protected void onRestart() {

        super.onRestart();
      /*  Intent inten = new Intent(this, TimerService.class);
        startService(inten);*/
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
             /*if (Utils.requestingLocationUpdates(this)) {
             if (!checkPermission()) {
             requestPermissions();
             } else {
             // mLUService.requestLocationUpdates();
             }
             }*/


            // Bind to the service. If the service is in foreground mode, this signals to the service
            // that since this activity is in the foreground, the service can exit foreground mode.
            bindService(new Intent(this, SANGPSTracker.class), mServiceConection,
                    Context.BIND_AUTO_CREATE);
            LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
                    new IntentFilter(SANGPSTracker.ACTION_BROADCAST));


 /* LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
 new IntentFilter(Config.REGISTRATION_COMPLETE));

 // register new push message receiver
 // by doing this, the activity will be notified each time a new message arrives
 LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
 new IntentFilter(Config.PUSH_NOTIFICATION));

 // clear the notification area when the app is opened
 MyNotificationManager.clearNotifications(getApplicationContext());*/

            Log.e("Loaction_Check", "Loaction_Check");

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
      /*  Intent inten = new Intent(this, TimerService.class);
        startService(inten);*/
        Log.v("LOG_IN_LOCATION", "ONPAUSE");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);

    }


/*// LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
 super.onPause();
 }*/


    @Override
    protected void onStart() {

      /*  Intent inten = new Intent(this, TimerService.class);
        startService(inten);*/
        Log.v("LOG_IN_LOCATION", "ONSTART");
        super.onStart();
        if (authStateListener != null) {
            FirebaseAuth.getInstance().signOut();
        }
        firebaseAuth.addAuthStateListener(authStateListener);
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

        if (eMail.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Invalid Email ID", Toast.LENGTH_LONG).show();
            mProgress.dismiss();
            return;
        }
        Log.d(TAG, "TWO " + deviceToken);
        //eMail="haptest4@hap.in";
        Call<Model> modelCall = apiInterface.login("get/GoogleLogin", eMail, deviceToken);
      //  Call<Model> modelCall = apiInterface.login("get/GoogleLogin", eMail, deviceToken);
        //  Call<Model> modelCall = apiInterface.login("get/GoogleLogin", "haptest4@hap.in", deviceToken);
        modelCall.enqueue(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {

                if (response.isSuccessful()) {
                    Log.d(TAG, "Three " + deviceToken);

                    Log.d("LoginData", String.valueOf(response.body()));

                    // Log.e("sfName",response.body().getData().get(0).getSfCode());

                    if (response.body().getSuccess() == true) {
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                        Intent intent;

                        Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
                        JsonArray CinData = response.body().getCInData();
                        if (CinData.size() > 0) {
                            JsonObject CinObj = CinData.get(0).getAsJsonObject();
                            Log.d("CinData", String.valueOf(CinObj));

                            SharedPreferences.Editor editor = CheckInDetails.edit();
                            editor.putString("Shift_Selected_Id", CinObj.get("Sft_ID").getAsString());
                            editor.putString("Shift_Name", CinObj.get("Sft_Name").getAsString());
                            ///if(CinObj.getAsJsonObject("End_Time").isJsonNull()!= true)
                            if (CinObj.get("End_Time").isJsonNull() != true)
                                editor.putString("CINEnd", CinObj.getAsJsonObject("End_Time").get("date").getAsString());
                            else
                                editor.putString("CINEnd", "");
                            editor.putString("ShiftStart", CinObj.getAsJsonObject("Sft_STime").get("date").getAsString());
                            editor.putString("ShiftEnd", CinObj.getAsJsonObject("sft_ETime").get("date").getAsString());
                            editor.putString("ShiftCutOff", CinObj.getAsJsonObject("ACutOff").get("date").getAsString());
                            editor.putString("On_Duty_Flag", CinObj.get("Wtp").getAsString());

                            String CTime = DT.getDateWithFormat(CinObj.getAsJsonObject("Start_Time").get("date").getAsString(), "HH:mm:ss");
                            int Type = CinObj.get("Type").getAsInt();
                            if (CheckInDetails.getString("FTime", "").equalsIgnoreCase(""))
                                editor.putString("FTime", CTime);
                            editor.putString("Logintime", CTime);
                            if (Type == 0) CheckIn = true;
                            editor.putBoolean("CheckIn", CheckIn);
                            editor.apply();
                        }

                        if (requestCode == RC_SIGN_IN) {
                            if (CheckIn == true) {
                                intent = new Intent(Login.this, Dashboard_Two.class);
                                // intent = new Intent(Login.this, TAClaimActivity.class);
                                intent.putExtra("Mode", "CIN");
                            } else {
                                intent = new Intent(Login.this, Dashboard.class);
                                // intent = new Intent(Login.this, TAClaimActivity.class);
                            }
                        } else {
                            // intent = new Intent(Login.this, AllowanceActivity.class);
                            intent = new Intent(Login.this, Dashboard_Two.class);
                            intent.putExtra("Mode", "RPT");
                        }
                        String code = response.body().getData().get(0).getSfCode();
                        String Sf_type = String.valueOf(response.body().getData().get(0).getSFFType());
                        String sName = response.body().getData().get(0).getSfName();
                        String div = response.body().getData().get(0).getDivisionCode();
                        Integer type = response.body().getData().get(0).getCheckCount();
                        String DeptCd = response.body().getData().get(0).getSFDept();
                        String DeptType = response.body().getData().get(0).getDeptType();
                        Integer OTFlg = response.body().getData().get(0).getOTFlg();
                        SharedPreferences.Editor editor = UserDetails.edit();
                        Shared_Common_Pref.Sf_Code = code;
                        Shared_Common_Pref.Sf_Name = response.body().getData().get(0).getSfName();
                        Shared_Common_Pref.Div_Code = div;
                        Shared_Common_Pref.StateCode = Sf_type;
                        shared_common_pref.save(Shared_Common_Pref.Sf_Code, code);
                        shared_common_pref.save(Shared_Common_Pref.Div_Code, div);
                        shared_common_pref.save(Shared_Common_Pref.StateCode, Sf_type);
                        shared_common_pref.save(Shared_Common_Pref.SF_EMP_ID, response.body().getData().get(0).getSfEmpId());
                        shared_common_pref.save(Shared_Common_Pref.Sf_Name, response.body().getData().get(0).getSfName());

                        shared_common_pref.save(Shared_Common_Pref.SF_DEPT, response.body().getData().get(0).getDeptName());
                        shared_common_pref.save(Shared_Common_Pref.SF_DESIG, response.body().getData().get(0).getSfDesignationShortName());

                        shared_common_pref.save(Shared_Common_Pref.CHECK_COUNT, String.valueOf(type));

                        Shared_Common_Pref.Dept_Type = DeptType;
                        Shared_Common_Pref.SF_Type = Sf_type;


                        Log.e("SF_TYPEVALUE", Sf_type);
                        Log.e("STATECODE", code);
                        Log.e("STATECODE", div);

                        Log.e("STATECODE", Shared_Common_Pref.StateCode);
                        Log.e("LOGIN_RESPONSE", String.valueOf(response.body().getData().get(0).getSfCode()));
                        editor.putString("Sf_Type", Sf_type);
                        editor.putString("Sfcode", code);
                        editor.putString("SfName", sName);
                        editor.putString("Divcode", div);
                        editor.putInt("CheckCount", type);
                        editor.putString("DeptCd", DeptCd);
                        editor.putString("DeptType", DeptType);
                        editor.putInt("OTFlg", OTFlg);
                        Log.d("DeptType", String.valueOf(DeptType));
                        editor.putString("State_Code", Sf_type);
                        editor.putString("email", eMail);
                        editor.putString("url", String.valueOf(profile));
                        editor.apply();
                        if (requestCode == RC_SIGN_IN || requestCode == 0)
                            editor.putBoolean("Login", true);
                        else
                            editor.putBoolean("Login", false);

                        editor.apply();
                        startActivity(intent);
                        try {
                            mProgress.dismiss();
                        } catch (Exception e) {

                        }

                    } else {
                        try {
                            mProgress.dismiss();
                        } catch (Exception e) {

                        }
                        Toast.makeText(getApplicationContext(), "Check username and password", Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {

                Toast.makeText(getApplicationContext(), "Not Working", Toast.LENGTH_LONG).show();
                try {
                    mProgress.dismiss();
                } catch (Exception e) {

                }
            }
        });
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
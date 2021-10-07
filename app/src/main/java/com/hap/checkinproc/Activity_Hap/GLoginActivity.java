package com.hap.checkinproc.Activity_Hap;

import static android.widget.Toast.LENGTH_LONG;

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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.multidex.BuildConfig;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hap.checkinproc.Activity.ProcurementDashboardActivity;
import com.hap.checkinproc.Common_Class.CameraPermission;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Model_Class.Model;
import com.hap.checkinproc.R;
import com.hap.checkinproc.common.DatabaseHandler;
import com.hap.checkinproc.common.LocationReceiver;
import com.hap.checkinproc.common.SANGPSTracker;
import com.hap.checkinproc.common.TimerService;

import org.json.JSONArray;

import javax.microedition.khronos.opengles.GL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GLoginActivity extends AppCompatActivity implements View.OnClickListener {
    TextInputEditText name, password;
    Button btnLogin;
    ImageView profileImage;
    String photo;
    String idToken, eMail="", UserLastName, UserLastName1;
    Button signInButton, ReportsButton, ExitButton;

    Shared_Common_Pref shared_common_pref;
    private static final String TAG = "LoginActivity";
    private GoogleSignInClient googleApiClient;
    private final static int RC_SIGN_IN = 1;
    private final static int RC_MYREPORTS = 9001;
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
    DatabaseHandler db;

    EditText etUserName, etPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glogin);
        shared_common_pref=new Shared_Common_Pref(this);
        db=new DatabaseHandler(this);
        name = (TextInputEditText) findViewById(R.id.username);

        profileImage = (ImageView) findViewById(R.id.profile_image);
        signInButton = (Button) findViewById(R.id.sign_in_button);

       // findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.disconnect_button).setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        //this is where we start the Auth state Listener to listen for whether the user is signed in or not
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("TAG", "onAuthStateChanged:signed_out");
                }
            }
        };

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(this.getResources().getString(R.string.web_client_id))
                .requestEmail()
                .build();

        googleApiClient = GoogleSignIn.getClient(this, gso);


        // [START customize_button]
        // Set the dimensions of the sign-in button.
    //    SignInButton signInButton = findViewById(R.id.sign_in_button);
//        signInButton.setSize(SignInButton.SIZE_STANDARD);
//        signInButton.setColorScheme(SignInButton.COLOR_LIGHT);


        mProgress = new ProgressDialog(this);
        String titleId = "Signing in...";
        mProgress.setTitle(titleId);
        mProgress.setMessage("Please Wait...");
        deviceToken = shared_common_pref.getvalue(Shared_Common_Pref.Dv_ID);
        UserDetails = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        CheckInDetails = getSharedPreferences(CheckInDetail, Context.MODE_PRIVATE);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);


        ExitButton = (Button) findViewById(R.id.btnExit);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
//                Intent signInIntent = googleApiClient.getSignInIntent();
//                startActivityForResult(signInIntent, RC_SIGN_IN);


//                try {
//
//                    if (etUserName.getText().toString().equals("") || etPw.getText().toString().equals("")) {
//                        Toast.makeText(getApplicationContext(), "User name is empty", Toast.LENGTH_SHORT).show();
//                    } else if (etPw.getText().toString().equals("")) {
//                        Toast.makeText(getApplicationContext(), "Password is empty", Toast.LENGTH_SHORT).show();
//
//                    } else {
//
//                        eMail = etUserName.getText().toString();
//
//                        mLUService = new SANGPSTracker(Login.this);
//                        myReceiver = new LocationReceiver();
//                        // Bind to the service. If the service is in foreground mode, this signals to the service
//                        // that since this activity is in the foreground, the service can exit foreground mode.
//
//                        Boolean DAMode = shared_common_pref.getBoolValue(Shared_Common_Pref.DAMode);
//                        if (DAMode == true) {
//                            bindService(new Intent(getApplicationContext(), SANGPSTracker.class), mServiceConection,
//                                    Context.BIND_AUTO_CREATE);
//                            LocalBroadcastManager.getInstance(getApplication()).registerReceiver(myReceiver,
//                                    new IntentFilter(SANGPSTracker.ACTION_BROADCAST));
//                            mLUService.requestLocationUpdates();
//                        }
//
//                        //   login(RC_SIGN_IN);
//
//                        signInAPI(etUserName.getText().toString(), etPw.getText().toString());
//
////command the line for android 6 device crossing pblm
////                    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
////                   signInIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
////                    startActivityForResult(signInIntent, RC_SIGN_IN);
//
//                    }
//                } catch (Exception e) {
//                    Log.v("LOGINNN:", e.getMessage());
//                }

            }
        });


        ExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = UserDetails.edit();
                editor.putBoolean("Login", false);
                editor.apply();
                CheckInDetails.edit().clear().commit();
                finishAffinity();
            }
        });

        cameraPermission = new CameraPermission(GLoginActivity.this, getApplicationContext());

        if (!cameraPermission.checkPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraPermission.requestPermission();
                Log.v("PERMISSION_NOT", "PERMISSION_NOT");
            }
            Log.v("PERMISSION_NOT", "PERMISSION_NOT");
        } else {
            Log.v("PERMISSION", "PERMISSION");
        }

        Boolean Login = UserDetails.getBoolean("Login", false);
        Boolean CheckIn = CheckInDetails.getBoolean("CheckIn", false);
        Intent inten = new Intent(this, TimerService.class);
        startService(inten);
        if (Login == true || CheckIn == true) {
            /*PERMISSION REQUEST*/
            if (cameraPermission.checkPermission()) {

                Boolean DAMode = shared_common_pref.getBoolValue(Shared_Common_Pref.DAMode);
                if (DAMode == true) {
                    Intent playIntent = new Intent(this, SANGPSTracker.class);
                    bindService(playIntent, mServiceConection, Context.BIND_AUTO_CREATE);
                    startService(playIntent);
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
                if (ActStarted.equalsIgnoreCase("true")) {
                    Intent aIntent;
                    String sDeptType = UserDetails.getString("DeptType", "");
                    if (sDeptType.equalsIgnoreCase("1")) {
                        aIntent = new Intent(getApplicationContext(), ProcurementDashboardActivity.class);
                    } else {
                        Shared_Common_Pref.Sync_Flag = "0";
                        if (checkValueStore())
                            aIntent = new Intent(getApplicationContext(), SFA_Activity.class);
                        else
                            aIntent = new Intent(getApplicationContext(), Dashboard_Two.class);
                    }
                    startActivity(aIntent);
                } else {
                    Intent Dashboard = new Intent(GLoginActivity.this, Dashboard_Two.class);
                    Dashboard.putExtra("Mode", "CIN");
                    startActivity(Dashboard);
                }
            }
        }



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


    boolean checkValueStore() {
        try {
            JSONArray storeData = db.getMasterData(Constants.Distributor_List);
            if (storeData != null && storeData.length() > 0)
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
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
                bindService(new Intent(this, SANGPSTracker.class), mServiceConection,
                        Context.BIND_AUTO_CREATE);
                LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
                        new IntentFilter(SANGPSTracker.ACTION_BROADCAST));
            }
            Log.e("Loaction_Check", "Loaction_Check");

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("LOG_IN_LOCATION", "ONPAUSE");
       // LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);

    }


    @Override
    public void onStart() {
        super.onStart();

        // [START on_start_sign_in]
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);

        Log.v("LOG_IN_LOCATION", "ONSTART");
        super.onStart();
        if (authStateListener != null) {
            FirebaseAuth.getInstance().signOut();
        }
        firebaseAuth.addAuthStateListener(authStateListener);
        // [END on_start_sign_in]
    }

    private void updateUI(@Nullable GoogleSignInAccount account) {
//        if (account != null) {
//            //mStatusTextView.setText(getString(R.string.signed_in_fmt, account.getDisplayName()));
//
//            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
//             findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
//        } else {
//            // mStatusTextView.setText(R.string.signed_out);
//
//            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
//            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
//        }
    }

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task,requestCode);
        }
    }
    // [END onActivityResult]

    // [START handleSignInResult]
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask,int requestCode) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);


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

            // Signed in successfully, show authenticated UI.
            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    public void login(int requestCode) {
        if (eMail.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Invalid Email ID", Toast.LENGTH_LONG).show();
            mProgress.dismiss();
            return;
        }

        //eMail = "purushotham.b@hap.in";
        //eMail = "haptest3@hap.in";
        //  eMail="senthilraja.d@hap.in";


        Call<Model> modelCall = apiInterface.login("get/GoogleLogin", eMail, deviceToken);
        modelCall.enqueue(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSuccess() == true) {
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
                                intent = new Intent(GLoginActivity.this, Dashboard_Two.class);
                                intent.putExtra("Mode", "CIN");
                            } else {
                                intent = new Intent(GLoginActivity.this, Dashboard.class);
                            }
                        } else {
                            intent = new Intent(GLoginActivity.this, Dashboard_Two.class);
                            intent.putExtra("Mode", "RPT");
                        }
                        String code = response.body().getData().get(0).getSfCode();
                        String Sf_type = String.valueOf(response.body().getData().get(0).getSFFType());
                        String empID = response.body().getData().get(0).getSfEmpId();
                        String sName = response.body().getData().get(0).getSfName();
                        String div = response.body().getData().get(0).getDivisionCode();
                        Integer type = response.body().getData().get(0).getCheckCount();
                        String DesigNm = response.body().getData().get(0).getSfDesignationShortName();
                        String SFRptCd = response.body().getData().get(0).getSfRptCode();
                        ;
                        String SFRptNm = response.body().getData().get(0).getSfRptName();
                        ;
                        String DeptCd = response.body().getData().get(0).getSFDept();
                        String DeptNm = response.body().getData().get(0).getDeptName();
                        String DeptType = response.body().getData().get(0).getDeptType();
                        String SFHQ = response.body().getData().get(0).getsFHQ();
                        String SFHQID = response.body().getData().get(0).getHQID();
                        String SFHQCode = response.body().getData().get(0).getHQCode();
                        String SFHQLoc = response.body().getData().get(0).getHOLocation();
                        int THrsPerm = response.body().getData().get(0).getTHrsPerm();
                        String mProfile = response.body().getData().get(0).getProfile();
                        String mProfPath = response.body().getData().get(0).getProfPath();
                        Integer OTFlg = response.body().getData().get(0).getOTFlg();

                        /* Unwanted Lines */
                        Shared_Common_Pref.Sf_Code = code;
                        Shared_Common_Pref.Sf_Name = response.body().getData().get(0).getSfName();
                        Shared_Common_Pref.Div_Code = div;
                        Shared_Common_Pref.StateCode = Sf_type;
                        shared_common_pref.save(Shared_Common_Pref.Sf_Code, code); //l
                        shared_common_pref.save(Shared_Common_Pref.Div_Code, div); //l
                        shared_common_pref.save(Shared_Common_Pref.StateCode, Sf_type); //l
                        shared_common_pref.save(Shared_Common_Pref.SF_EMP_ID, response.body().getData().get(0).getSfEmpId()); //l
                        shared_common_pref.save(Shared_Common_Pref.Sf_Name, response.body().getData().get(0).getSfName()); //l
                        shared_common_pref.save(Shared_Common_Pref.SF_DEPT, response.body().getData().get(0).getDeptName()); //l
                        shared_common_pref.save(Shared_Common_Pref.SF_DESIG, response.body().getData().get(0).getSfDesignationShortName()); //l
                        shared_common_pref.save(Shared_Common_Pref.CHECK_COUNT, String.valueOf(type)); //l
                        shared_common_pref.save(Shared_Common_Pref.Sf_Code, code); //l
                        Shared_Common_Pref.Dept_Type = DeptType;
                        Shared_Common_Pref.SF_Type = Sf_type;
                        /*Endof Unwanted Lines*/

                        SharedPreferences.Editor editor = UserDetails.edit();
                        editor.putString("Sf_Type", Sf_type);
                        editor.putString("Sfcode", code);
                        editor.putString("EmpId", empID);
                        editor.putString("SfName", sName);
                        editor.putString("SFDesig", DesigNm);
                        editor.putString("SFRptCode", SFRptNm);
                        editor.putString("SFRptName", SFRptNm);
                        editor.putString("SFHQ", SFHQ);
                        editor.putString("SFHQCode", SFHQCode);
                        editor.putString("SFHQID", SFHQID);
                        editor.putInt("THrsPerm", THrsPerm);
                        editor.putString("Divcode", div);
                        editor.putInt("CheckCount", type);
                        editor.putString("DeptCd", DeptCd);
                        editor.putString("DeptNm", DeptNm);
                        editor.putString("DeptType", DeptType);
                        editor.putInt("OTFlg", OTFlg);
                        Log.d("DeptType", String.valueOf(DeptType));
                        editor.putString("State_Code", Sf_type);
                        editor.putString("email", eMail);
                        if (!UserLastName.equalsIgnoreCase("")) {
                            editor.putString("DesigNm", UserLastName);
                            editor.putString("DepteNm", UserLastName1);
                        }

                        editor.putString("url", String.valueOf(profile));
                        editor.putString("Profile", String.valueOf(mProfile));
                        editor.putString("ProfPath", String.valueOf(mProfPath));

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

    private void firebaseAuthWithGoogle(AuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("TAG", "signInWithCredential:onComplete:" + task.isSuccessful());
                        if (task.isSuccessful()) {
                        } else {
                            Log.w("TAG", "signInWithCredential" + task.getException().getMessage());
                            task.getException().printStackTrace();
                        }
                    }
                });
    }
    // [END handleSignInResult]

    // [START signIn]
    private void signIn() {
        Intent signInIntent = googleApiClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]

    // [START signOut]
    private void signOut() {
        googleApiClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        updateUI(null);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]

    // [START revokeAccess]
    private void revokeAccess() {
        googleApiClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        updateUI(null);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
            case R.id.disconnect_button:
                revokeAccess();
                break;
        }
    }
}

package com.hap.checkinproc.Activity_Hap;

import android.Manifest;
import android.app.ProgressDialog;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
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
import com.hap.checkinproc.BuildConfig;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Model_Class.Model;
import com.hap.checkinproc.R;
import com.hap.checkinproc.common.LocationReceiver;
import com.hap.checkinproc.common.SANGPSTracker;
import com.hap.checkinproc.common.TimerService;
import com.hap.checkinproc.common.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import static android.widget.Toast.LENGTH_LONG;

public class Login extends AppCompatActivity {
    TextInputEditText name,password;
    Button btnLogin;
    ImageView profileImage;
    String photo;
    String idToken,eMail;
    Button signInButton, ReportsButton, ExitButton;
    Shared_Common_Pref shared_common_pref;
    private static final String TAG = "LoginActivity";
    private GoogleApiClient googleApiClient;
    private  final static int RC_SIGN_IN = 1;
    private  final static int RC_MYREPORTS = 2;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    SharedPreferences sharedPreferences;
    SharedPreferences CheckInDetails;
    public static final String CheckInDetail = "CheckInDetail" ;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private ProgressDialog mProgress;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1001;


    private LocationReceiver rcvMReceiver;
    private SANGPSTracker mLUService;
    private LocationReceiver myReceiver;
    private TimerService mTimerService;

    // Tracks the bound state of the service.
    private boolean mBound = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        name=(TextInputEditText)findViewById(R.id.username);
        password=(TextInputEditText)findViewById(R.id.password);
        shared_common_pref = new Shared_Common_Pref(this);
        btnLogin=(Button)findViewById(R.id.btnLogin);
        profileImage=(ImageView)findViewById(R.id.profile_image);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        CheckInDetails = getSharedPreferences(CheckInDetail, Context.MODE_PRIVATE);
           mProgress =new ProgressDialog(this);
           String titleId="Signing in...";
           mProgress.setTitle(titleId);
           mProgress.setMessage("Please Wait...");

            eMail=sharedPreferences.getString("email", "");
            name.setText(eMail);
            if(!checkPermission())
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    requestPermissions();
                }
            }
        else{
            }
           btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(name.getText().toString()) ){
                    Toast.makeText(Login.this, "username/password required", Toast.LENGTH_SHORT).show();
                }else{
                    //proceed to login
                    mProgress.show();
                    login(0);
                }
            }
        });
           signInButton=(Button)findViewById(R.id.sign_in_button);

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


        signInButton.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /* if(mLUService == null)
                   mLUService = new SANGPSTracker(getApplicationContext());
              *//* if(mTimerService == null)
                   mTimerService = new TimerService();
               mTimerService.startTimerService();*//*

               startService(new Intent(Login.this, TimerService.class));
               mLUService.requestLocationUpdates();
               mProgress.show();*/
               Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
               startActivityForResult(signInIntent,RC_SIGN_IN);
            }
        });

        ReportsButton=(Button)findViewById(R.id.btnReport);
        ReportsButton.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mLUService == null)
                    mLUService = new SANGPSTracker(getApplicationContext());
              /* if(mTimerService == null)
                   mTimerService = new TimerService();
               mTimerService.startTimerService();*/

                startService(new Intent(Login.this, TimerService.class));
                mLUService.requestLocationUpdates();
                mProgress.show();
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(signInIntent,RC_MYREPORTS);
            }
        });
        ExitButton=(Button)findViewById(R.id.btnExit);
        ExitButton.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("Login", false);
                editor.apply();
                finishAffinity();
            }
        });
        Boolean Login= sharedPreferences.getBoolean("Login", false);
        Boolean CheckIn= CheckInDetails.getBoolean("CheckIn", false);

        if(Login==true||CheckIn==true){
            if(checkPermission())
            {
                Intent playIntent = new Intent(this, SANGPSTracker.class);
                bindService(playIntent, mServiceConection, Context.BIND_AUTO_CREATE);
                startService(playIntent);
            }
            if(Login==true && CheckIn==false) {
                mProgress.show();
                login(RC_SIGN_IN);
            }
            else if(CheckIn==true) {
                Intent Dashboard=new Intent(Login.this, Dashboard_Two.class);
                Dashboard.putExtra("Mode","CIN");
                startActivity(Dashboard);
            }
        }

    }

       @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN || requestCode==RC_MYREPORTS){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
           // assert result != null;
            if(result.isSuccess()){
                Log.e("Result_success",result.getStatus().toString());
            }                Log.e("Result_success",result.getStatus().toString());

            Log.e("Result_googel", String.valueOf(result));
            handleSignInResult(result,requestCode);
        }
        }
        private void handleSignInResult(GoogleSignInResult result,int requestCode){
            if(result.isSuccess()){


                GoogleSignInAccount account=result.getSignInAccount();
                //assert account != null;
                Log.d("LoginDetails", String.valueOf(account.getDisplayName()));
                idToken = account.getIdToken();
                name.setText(account.getEmail());
                eMail=account.getEmail();
                try{
                    Glide.with(this).load(account.getPhotoUrl()).into(profileImage);
                    Log.e("aara",account.getPhotoUrl().toString() );
                    photo=account.getPhotoUrl().toString();

                } catch (NullPointerException e){
                    Toast.makeText(getApplicationContext(),"image not found", LENGTH_LONG).show();
                }
                AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
                firebaseAuthWithGoogle(credential);
                login(requestCode);
            }
            else{
                mProgress.dismiss();
            Toast.makeText(getApplicationContext(),"Sign in cancel", LENGTH_LONG).show();
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
    protected void onResume() {
        super.onResume();
        if(checkPermission())
        {
            if(mLUService == null)
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

        }
    }

    @Override
    protected void onPause() {
       // LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
        super.onPause();
    }

    @Override
    protected void onStart() {
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
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
    public void login(int requestCode) {
            if(eMail.isEmpty()){
                Toast.makeText(getApplicationContext(),"Invalid Email ID", Toast.LENGTH_LONG).show();
                return;
            }
          ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
          Call<Model> modelCall = apiInterface.login("get/GoogleLogin",eMail);
          modelCall.enqueue(new Callback<Model>() {
              @Override
              public void onResponse(Call<Model> call, Response<Model> response) {

                  if(response.isSuccessful()) {

                //      Log.e("sfName",response.body().getData().get(0).getSfCode());

                      if (response.body().getSuccess() == true) {
                          Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_LONG).show();
                          Intent intent ;
                          if(requestCode==RC_SIGN_IN)
                                intent = new Intent(Login.this, Dashboard.class);
                          else
                                intent = new Intent(Login.this, Dashboard_Two.class);
                          intent.putExtra("photo",photo);
                          String code = response.body().getData().get(0).getSfCode();
                          String Sf_type = String.valueOf(response.body().getData().get(0).getSFFType());
                          String sName = response.body().getData().get(0).getSfName();
                          String div = response.body().getData().get(0).getDivisionCode();
                          Integer type = response.body().getData().get(0).getCheckCount();
                          SharedPreferences.Editor editor = sharedPreferences.edit();
                          Shared_Common_Pref.Sf_Code = code;
                          Shared_Common_Pref.Sf_Name = response.body().getData().get(0).getSfName();
                          Shared_Common_Pref.Div_Code = div;
                          Shared_Common_Pref.StateCode = Sf_type;
                          shared_common_pref.save(Shared_Common_Pref.Sf_Code, code);
                          shared_common_pref.save(Shared_Common_Pref.Div_Code, div);
                          shared_common_pref.save(Shared_Common_Pref.StateCode, Sf_type);
                          Log.e("LOGIN_RESPONSE", String.valueOf(response.body().getData().get(0).getSfCode()));
                          editor.putString("Sf_Type", Sf_type);
                          editor.putString("Sfcode", code);
                          editor.putString("SfName", sName);
                          editor.putString("Divcode",div);
                          editor.putInt("CheckCount",type);
                          editor.putString("State_Code", Sf_type);
                          editor.putString("email", eMail);
                          editor.apply();
                          if(requestCode==RC_SIGN_IN || requestCode==0)
                              editor.putBoolean("Login",true);
                          else
                              editor.putBoolean("Login",false);

                          editor.apply();
                          startActivity(intent);
                          mProgress.dismiss();

                      }else{
                          mProgress.dismiss();
                          Toast.makeText(getApplicationContext(), "Check username and password", Toast.LENGTH_LONG).show();
                      }
                  }

              }

              @Override
              public void onFailure(Call<Model> call, Throwable t) {

                  Toast.makeText(getApplicationContext(),"Not Working",Toast.LENGTH_LONG).show();

              }
          });
      }
      private boolean checkPermission()
      {
            return (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
                    //PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
      }

      //Location service part
      @RequiresApi(api = Build.VERSION_CODES.Q)
      private void requestPermissions()
      {
          boolean shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,
                  Manifest.permission.ACCESS_FINE_LOCATION);
          if(shouldProvideRationale)
          {
              Snackbar.make(
                      findViewById(R.id.activity_main),
                      R.string.permission_rationale,
                      Snackbar.LENGTH_INDEFINITE)
                      .setAction(R.string.ok, new View.OnClickListener() {
                          @Override
                          public void onClick(View view) {
                              // Request permission
                              ActivityCompat.requestPermissions(Login.this,
                                      new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION,Manifest.permission.FOREGROUND_SERVICE},
                                      REQUEST_PERMISSIONS_REQUEST_CODE);
                          }
                      })
                      .show();
          }
          else
              ActivityCompat.requestPermissions(Login.this,
                      new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION,Manifest.permission.FOREGROUND_SERVICE},
                      REQUEST_PERMISSIONS_REQUEST_CODE);
      }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case REQUEST_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length <= 0) {
                    // Permission was not granted.
                } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted.
                    if(mLUService == null)
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
            mLUService = ((SANGPSTracker.LocationBinder)service).getLocationUpdateService(getApplicationContext());
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mLUService = null;
            mBound = false;
        }
    };
}
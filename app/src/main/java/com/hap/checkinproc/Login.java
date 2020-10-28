package com.hap.checkinproc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Model_Class.Model;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.LENGTH_LONG;

public class Login extends AppCompatActivity {
    TextInputEditText name,password;
    Button btnLogin;
    ImageView profileImage;
    String photo;
    String idToken;
    SignInButton signInButton;
    private static final String TAG = "LoginActivity";
    private GoogleApiClient googleApiClient;
    private  final static int RC_SIGN_IN = 1;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private ProgressDialog mProgress;


       @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        name=(TextInputEditText)findViewById(R.id.username);
        password=(TextInputEditText)findViewById(R.id.password);
        btnLogin=(Button)findViewById(R.id.btnLogin);
        profileImage=(ImageView)findViewById(R.id.profile_image);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
           mProgress =new ProgressDialog(this);
           String titleId="Signing in...";
           mProgress.setTitle(titleId);
           mProgress.setMessage("Please Wait...");


           btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(name.getText().toString()) ){
                    Toast.makeText(Login.this, "username/password required", Toast.LENGTH_SHORT).show();
                }else{
                    //proceed to login
                    mProgress.show();
                    login();
                }
            }
        });

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
            .requestIdToken("57770165619-0j7hhlcbg6q3p6jo8g0v1itrea9uumlf.apps.googleusercontent.com")//you can also use R.string.default_web_client_id
            .requestEmail()
            .build();
            googleApiClient = new GoogleApiClient.Builder(this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build();
        signInButton=(SignInButton)findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new  View.OnClickListener() {

       @Override
        public void onClick(View view) {
           Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
           startActivityForResult(signInIntent,RC_SIGN_IN);
        }
        });
        }

       public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        }

       @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            assert result != null;
            handleSignInResult(result);
        }
        }
        private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account=result.getSignInAccount();
            assert account != null;
            idToken = account.getIdToken();
            name.setText(account.getEmail());
            try{
                Glide.with(this).load(account.getPhotoUrl()).into(profileImage);
                Log.e("aara",account.getPhotoUrl().toString() );
                photo=account.getPhotoUrl().toString();

            } catch (NullPointerException e){
                Toast.makeText(getApplicationContext(),"image not found", LENGTH_LONG).show();
            }
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            firebaseAuthWithGoogle(credential);
        }
        else{
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
    protected void onStart() {
        super.onStart();
        if (authStateListener != null) {
            FirebaseAuth.getInstance().signOut();
        }
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }


    public void login() {

          ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
          Call<Model> modelCall = apiInterface.login("get/GoogleLogin",name.getText().toString());
          modelCall.enqueue(new Callback<Model>() {
              @Override
              public void onResponse(Call<Model> call, Response<Model> response) {

                  if(response.isSuccessful()) {

                //      Log.e("sfName",response.body().getData().get(0).getSfCode());

                      if (response.body().getSuccess() == true) {
                          Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_LONG).show();
                          Intent intent = new Intent(Login.this,Dashboard.class);
                          intent.putExtra("photo",photo);
                          String code = response.body().getData().get(0).getSfCode();
                          String div = response.body().getData().get(0).getDivisionCode();
                          Integer type = response.body().getData().get(0).getSFFType();
                          SharedPreferences.Editor editor = sharedPreferences.edit();
                          editor.putString("Sfcode", code);
                          editor.putString("Divcode",div);
                          editor.putInt("Sfftype",type);
                          editor.apply();
                          startActivity(intent);

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
}
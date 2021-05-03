package com.hap.checkinproc.Activity_Hap;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaFuelEdit extends AppCompatActivity {
    EditText edtFrom, edtTo, edtPersonal;
    String SLNO = "", MOT = "", starEd = "", endEd = "";
    Shared_Common_Pref mShared_common_pref;
    Integer inEdtFrom, inEdtTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ta_fuel_edit);
        SLNO = String.valueOf(getIntent().getSerializableExtra("SL_NO"));
        MOT = String.valueOf(getIntent().getSerializableExtra("MOT"));
        mShared_common_pref = new Shared_Common_Pref(this);
        edtFrom = findViewById(R.id.edt_from);
        edtTo = findViewById(R.id.edt_to);
        edtPersonal = findViewById(R.id.edt_pers);

        edtFrom.setText("" + getIntent().getSerializableExtra("Start"));
        edtTo.setText("" + getIntent().getSerializableExtra("End"));
        edtPersonal.setText("0");
        edtTo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inEdtFrom = Integer.valueOf(edtFrom.getText().toString());
                if (!edtTo.getText().toString().equals("")) {

                    try {
                        inEdtTo = Integer.parseInt(edtTo.getText().toString());
                    } catch (NumberFormatException ex) { // handle your exception

                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void UpdteAllowance(View v) throws JSONException {

        if (edtFrom.getText().toString().equalsIgnoreCase("")) edtFrom.setText("0");
        if (edtTo.getText().toString().equalsIgnoreCase("")) edtTo.setText("0");
        if (edtPersonal.getText().toString().equalsIgnoreCase("")) edtPersonal.setText("0");

        if (edtFrom.getText().toString() != null && !edtFrom.getText().toString().isEmpty() && !edtFrom.getText().toString().equals("null")) {
            if (edtTo.getText().toString() != null && !edtTo.getText().toString().isEmpty() && !edtTo.getText().toString().equals("null")) {

                inEdtFrom = Integer.valueOf(edtFrom.getText().toString());
                inEdtTo = Integer.parseInt(edtTo.getText().toString());

                if (inEdtFrom < inEdtTo) {
                    JSONObject jj = new JSONObject();
                    jj.put("sl_no", SLNO);
                    jj.put("mot", MOT);
                    jj.put("sfCode", mShared_common_pref.getvalue(Shared_Common_Pref.Sf_Code));
                    jj.put("startKm", edtFrom.getText().toString());
                    jj.put("endKm", edtTo.getText().toString());
                    jj.put("personalKm", edtPersonal.getText().toString());
                    Log.v("printing_allow", jj.toString());

                    Call<JsonObject> Callto;
                    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                    Callto = apiInterface.upteAllowance(jj.toString());
                    Callto.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                            JsonObject json = response.body();

                            Log.v("CHECKING", json.get("success").getAsString());


                            if (json.get("success").getAsString().equalsIgnoreCase("true")) {
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {

                        }
                    });
                } else {
                    Toast.makeText(this, "Should be greater than Start Km", Toast.LENGTH_SHORT).show();
                }

            }


        }
    }
}
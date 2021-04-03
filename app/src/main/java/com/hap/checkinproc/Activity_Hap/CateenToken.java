package com.hap.checkinproc.Activity_Hap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.hap.checkinproc.Interface.OnImagePickListener;
import com.hap.checkinproc.R;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class CateenToken extends AppCompatActivity {
    public static final String UserDetail = "MyPrefs";
    private static final String TAG = "MYQR";
    SharedPreferences UserDetails;
    ImageView qrImage,btnProfile,imgProfile;
    TextView txtHQName,txtSFName,txtEmpID,btnCanteen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cateen_token);
        qrImage=findViewById(R.id.imgQR);
        txtHQName=findViewById(R.id.HQName);
        txtSFName=findViewById(R.id.empName);
        txtEmpID=findViewById(R.id.empcode);


        btnCanteen=findViewById(R.id.btnCanteen);
        btnProfile=findViewById(R.id.btnProfile);
        imgProfile=findViewById(R.id.imgProf);
        btnCanteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent frmCanteen=new Intent(CateenToken.this,foodExp.class);
                startActivity(frmCanteen);
                finish();
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent frmCanteen=new Intent(CateenToken.this,ImageCapture.class);
                frmCanteen.putExtra("Mode","PF");
                startActivity(frmCanteen);
                //finish();
            }
        });
        ImageCapture.setOnImagePickListener(new OnImagePickListener() {
            @Override
            public void OnImagePick(Bitmap image) {
                imgProfile.setImageBitmap(image);
            }
        });
        UserDetails = getSharedPreferences(UserDetail, Context.MODE_PRIVATE);
        String HQCode=UserDetails.getString("SFHQCode","1000");
        String EmpID=UserDetails.getString("EmpId","");

        String sHQName=UserDetails.getString("SFHQ","");
        String sSFNm=UserDetails.getString("SfName","");
        txtHQName.setText(sHQName);
        txtSFName.setText(sSFNm);
        txtEmpID.setText(EmpID);
        String inputValue=HQCode+EmpID;
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;
        QRGEncoder qrgEncoder = new QRGEncoder(inputValue, null, QRGContents.Type.TEXT, smallerDimension);

        try {
            Bitmap bitmap = qrgEncoder.encodeAsBitmap();
            qrImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Log.v(TAG, e.toString());
        }
    }
}
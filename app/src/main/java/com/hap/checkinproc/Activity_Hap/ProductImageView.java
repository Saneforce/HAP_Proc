package com.hap.checkinproc.Activity_Hap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.cuneytayyildiz.gestureimageview.GestureImageView;
import com.hap.checkinproc.R;
import com.hap.checkinproc.common.TimerService;

import java.io.InputStream;
import java.net.URL;

public class ProductImageView extends Activity {

    GestureImageView ProductZoomImage;
    String ImageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_image_view);
        startService(new Intent(this, TimerService.class));
        ImageUrl = getIntent().getStringExtra("ImageUrl");
        ProductZoomImage = findViewById(R.id.product_image);

        new DownLoadImageTask(ProductZoomImage).execute(ImageUrl);

    }

    public void CloseActivity(View v) {
        finish();
    }


    private class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        ProgressDialog dialog;

        public DownLoadImageTask(ImageView imageView) {
            this.imageView = imageView;
            dialog = new ProgressDialog(ProductImageView.this);
        }

        protected Bitmap doInBackground(String... urls) {
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try {
                InputStream is = new URL(urlOfImage).openStream();
                logo = BitmapFactory.decodeStream(is);
            } catch (Exception e) { // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }


        protected void onPostExecute(Bitmap result) {


            super.onPostExecute(result);
            if (dialog.isShowing()) {
                imageView.setImageBitmap(result);
                dialog.dismiss();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("please wait.");
            dialog.show();

        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        startService(new Intent(this, TimerService.class));
        Log.v("LOG_IN_LOCATION", "ONRESTART");
    }

    @Override
    protected void onPause() {
        super.onPause();

        startService(new Intent(this, TimerService.class));
        Log.v("LOG_IN_LOCATION", "ONRESTART");
    }

    @Override
    protected void onStop() {
        super.onStop();
        startService(new Intent(this, TimerService.class));
        Log.v("LOG_IN_LOCATION", "ONRESTART");
    }

    @Override
    protected void onStart() {
        super.onStart();
        startService(new Intent(this, TimerService.class));
        Log.v("LOG_IN_LOCATION", "ONRESTART");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startService(new Intent(this, TimerService.class));
    }


}


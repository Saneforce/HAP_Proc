package com.hap.checkinproc.Activity_Hap;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.cuneytayyildiz.gestureimageview.GestureImageView;
import com.hap.checkinproc.R;
import com.squareup.picasso.Picasso;

public class ProductImageView extends Activity {

    GestureImageView ProductZoomImage;
    private float mScaleFactor = 1.0f;
    private ScaleGestureDetector mScaleGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_product_image_view);
        ProductZoomImage = findViewById(R.id.product_image);
        Intent intent = getIntent();
        String ImageUrl = intent.getStringExtra("ImageUrl");
        Log.e("ImageView",ImageUrl);
        Log.e("ImageView", String.valueOf(Uri.parse(ImageUrl)));
        Picasso.with(this)
                .load(ImageUrl)
                .into(ProductZoomImage);
        /*ProductZoomImage.setRotation(90);*/
    }

    public void CloseActivity(View v){

        finish();
    }


}


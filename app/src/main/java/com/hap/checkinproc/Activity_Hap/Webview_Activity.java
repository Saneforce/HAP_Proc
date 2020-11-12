package com.hap.checkinproc.Activity_Hap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hap.checkinproc.R;

public class Webview_Activity extends AppCompatActivity {
    private WebView wv1;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_);
        wv1 = findViewById(R.id.webView);
        i = getIntent();
        wv1.setWebViewClient(new Webview_Activity.MyBrowser());
        wv1.getSettings().setLoadsImagesAutomatically(true);
        wv1.getSettings().setJavaScriptEnabled(true);
        wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        Log.e("LOCATIONS", i.getExtras().getString("Locations"));
        wv1.loadUrl("https://www.google.com/maps?q=" + i.getExtras().getString("Locations"));

    }
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}

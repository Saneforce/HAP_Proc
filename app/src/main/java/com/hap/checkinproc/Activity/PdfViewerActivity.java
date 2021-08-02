package com.hap.checkinproc.Activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.hap.checkinproc.R;
import com.hap.checkinproc.common.TimerService;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class PdfViewerActivity extends AppCompatActivity {

    String pdfurl = "",pdfFile="";
    PDFView pdfView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);
        pdfView = (PDFView) findViewById(R.id.pdfView);
        pdfurl = String.valueOf(getIntent().getSerializableExtra("PDF_ONE"));
        pdfFile = String.valueOf(getIntent().getSerializableExtra("PDF_FILE"));
        Log.v("KARTHIC_URl", pdfurl);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sUrl=pdfurl.replace("file://","");
                File outputPath= new File(sUrl);
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("application/pdf");
                shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+outputPath));
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(shareIntent, "Share it"));
            }
        });

        if (pdfFile.equalsIgnoreCase("local")) {
            pdfView.fromFile(new File(pdfurl)).load();
        } else {
            new RetrivePDFfromUrl().execute(pdfurl);
        }
    }

  /*  private Boolean downloadAndSaveFile(String server, int portNumber,
                                        String user, String password, String filename, File localFile)
            throws IOException {
        FTPClient ftp = null;

        try {
            ftp = new FTPClient();
            ftp.connect(server, portNumber);
            //Log.d(LOG_TAG, "Connected. Reply: " + ftp.getReplyString());

            ftp.login(user, password);
            //Log.d(LOG_TAG, "Logged in");
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            //Log.d(LOG_TAG, "Downloading");
            ftp.enterLocalPassiveMode();

            OutputStream outputStream = null;
            boolean success = false;
            try {
                outputStream = new BufferedOutputStream(new FileOutputStream(
                        localFile));
                success = ftp.retrieveFile(filename, outputStream);
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
            }

            return success;
        } finally {
            if (ftp != null) {
                ftp.logout();
                ftp.disconnect();
            }
        }
    }
*/
    class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        ProgressDialog dialog;

        public RetrivePDFfromUrl() {
            dialog = new ProgressDialog(PdfViewerActivity.this);
        }

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {

            super.onPostExecute(inputStream);
            if (dialog.isShowing()){
                pdfView.fromStream(inputStream).load();

                dialog.dismiss();
                Toast.makeText(PdfViewerActivity.this,"Downloaded Successfully...",Toast.LENGTH_LONG).show();
                /*View view = findViewById(R.id.fab);
                Snackbar.make(pdfView, "Downloaded Successfully...", Snackbar.LENGTH_LONG)
                        .setBackgroundTint(Color.BLACK)
                        .setTextColor(Color.WHITE)
                        .setAction("Action", null).show();*/
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("please wait.");
            dialog.show();
        }
    }
}
package com.hap.checkinproc.Activity_Hap;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Interface.onPayslipItemClick;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.HAPListItem;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;


public class PayslipFtp extends AppCompatActivity {

    private static final String TAG = "FTPActivity";
    public static final String UserDetail = "MyPrefs";
    public FTPClient mFTPClient = null;

    RecyclerView mRecyclerView;

    JSONArray lsFiles;
    HAPListItem listItems;

    SharedPreferences UserDetails;

    String HomePath = "/home/hapftp/Payroll", CurrPath = "",
            EmpId = "";
    String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payslip_ftp);
        CurrPath = HomePath;
        getPaySlipFolder("");

        UserDetails = getSharedPreferences(UserDetail, Context.MODE_PRIVATE);
        EmpId = UserDetails.getString("EmpId", "");
        HAPListItem.SetPayOnClickListener(new onPayslipItemClick() {
            @Override
            public void onClick(JSONObject item) {
                try {
                    if (item.getString("type").equalsIgnoreCase("dir")) {
                        getPaySlipFolder(item.getString("name"));
                    } else {
                        ftpDownload(CurrPath + "/" + item.getString("name"), pdfPath + item.getString("name"));


                    }
                    Log.v("PAY_SLIP_KARTHIC_o",CurrPath);
                    Log.v("PAY_SLIP_KARTHIC_o",pdfPath);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getPaySlipFolder(String mCurrPath) {
        if (ftpConnect("sf.hap.in", "hapftp", "VFAY$du3@=9^", 21)) {
            CurrPath = CurrPath + ((mCurrPath != "") ? "/" : "") + mCurrPath;
            lsFiles = ftpGetFilesList(CurrPath);

            Log.v("Karthic_Response_JSON", lsFiles.toString());
            listItems = new HAPListItem(lsFiles, this);
            mRecyclerView = findViewById(R.id.fileList);
            mRecyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(layoutManager);

            mRecyclerView.setAdapter(listItems);
            ftpDisconnect();
        }
    }

    public boolean ftpConnect(String host, String username, String password,
                              int port) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll()
                    .build();
            StrictMode.setThreadPolicy(policy);

            mFTPClient = new FTPClient();
            mFTPClient.setConnectTimeout(5000);
            mFTPClient.connect(host, port);
            if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {
                boolean status = mFTPClient.login(username, password);
                mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
                mFTPClient.enterLocalPassiveMode();

                return status;
            }
        } catch (Exception e) {
            Log.d(TAG, "Error: could not connect to host " + host + "\n" + e.getMessage());
        }

        return false;
    }

    public boolean ftpDisconnect() {
        try {
            mFTPClient.logout();
            mFTPClient.disconnect();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error occurred while disconnecting from ftp server.");
        }

        return false;
    }

    // Method to get current working directory:

    public String ftpGetCurrentWorkingDirectory() {
        try {
            String workingDir = mFTPClient.printWorkingDirectory();
            return workingDir;
        } catch (Exception e) {
            Log.d(TAG, "Error: could not get current working directory.");
        }

        return null;
    }

    // Method to change working directory:

    public boolean ftpChangeDirectory(String directory_path) {
        try {
            mFTPClient.changeWorkingDirectory(directory_path);
        } catch (Exception e) {
            Log.d(TAG, "Error: could not change directory to " + directory_path);
        }

        return false;
    }

    // Method to list all files in a directory:

    public JSONArray ftpGetFilesList(String dir_path) {
        JSONArray fileList = new JSONArray();
        try {
            FTPFile[] ftpFiles = mFTPClient.listFiles(dir_path);
            int length = ftpFiles.length;
            for (int i = 0; i < length; i++) {
                String name = ftpFiles[i].getName();
                boolean isFile = ftpFiles[i].isFile();
                if (isFile) {
                    if (name.indexOf(EmpId + ".pdf") > -1) { //"-"+
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("name", name);
                        jsonObject.put("type", "FILE");
                        fileList.put(jsonObject);
                    }
                    Log.v("KARTHIC_FILE_NAME", "File : " + name + "= -" + EmpId + ".pdf");
                } else {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name", name);
                    jsonObject.put("type", "DIR");
                    fileList.put(jsonObject);

                    Log.v("KARTHIC_FILE_NAME", "Directory : " + name);
                }
            }
            return fileList;
        } catch (Exception e) {
            e.printStackTrace();
            return fileList;
        }
    }

    // Method to create new directory:

    public boolean ftpMakeDirectory(String new_dir_path) {
        try {
            boolean status = mFTPClient.makeDirectory(new_dir_path);
            return status;
        } catch (Exception e) {
            Log.d(TAG, "Error: could not create new directory named "
                    + new_dir_path);
        }

        return false;
    }

    // Method to delete/remove a directory:

    public boolean ftpRemoveDirectory(String dir_path) {
        try {
            boolean status = mFTPClient.removeDirectory(dir_path);
            return status;
        } catch (Exception e) {
            Log.d(TAG, "Error: could not remove directory named " + dir_path);
        }

        return false;
    }

    // Method to delete a file:

    public boolean ftpRemoveFile(String filePath) {
        try {
            boolean status = mFTPClient.deleteFile(filePath);
            return status;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Method to rename a file:

    public boolean ftpRenameFile(String from, String to) {
        try {
            boolean status = mFTPClient.rename(from, to);
            return status;
        } catch (Exception e) {
            Log.d(TAG, "Could not rename file: " + from + " to: " + to);
        }

        return false;
    }

    // Method to download a file from FTP server:

    /**
     * mFTPClient: FTP client connection object (see FTP connection example)
     * srcFilePath: path to the source file in FTP server desFilePath: path to
     * the destination file to be saved in sdcard
     */
    public boolean ftpDownload(String srcFilePath, String desFilePath) {

        Log.v("PAY_SLIP_KARTHIC_in",CurrPath);
        Log.v("PAY_SLIP_KARTHIC_in",pdfPath);
        Log.v("PAY_SLIP_KARTHIC_in",srcFilePath);
        Log.v("PAY_SLIP_KARTHIC_in",desFilePath);


        boolean status = false;
        try {
            if (ftpConnect("sf.hap.in", "hapftp", "VFAY$du3@=9^", 21)) {
                FileOutputStream desFileStream = new FileOutputStream(desFilePath);
                status = mFTPClient.retrieveFile(srcFilePath, desFileStream);
                Log.d("Karthic_Response", pdfPath);
                Log.d("Karthic_Response", desFilePath);
                desFileStream.close();
                ftpDisconnect();
                return status;
            }
        } catch (Exception e) {
            Log.d(TAG, "download failed" + e.getMessage());
        }

        return status;
    }

    // Method to upload a file to FTP server:

    /**
     * mFTPClient: FTP client connection object (see FTP connection example)
     * srcFilePath: source file path in sdcard desFileName: file name to be
     * stored in FTP server desDirectory: directory path where the file should
     * be upload to
     */
    public boolean ftpUpload(String srcFilePath, String desFileName,
                             String desDirectory, Context context) {
        boolean status = false;
        try {
            FileInputStream srcFileStream = new FileInputStream(srcFilePath);

            // change working directory to the destination directory
            // if (ftpChangeDirectory(desDirectory)) {
            status = mFTPClient.storeFile(desFileName, srcFileStream);
            // }

            srcFileStream.close();

            return status;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "upload failed: " + e);
        }

        return status;
    }
}
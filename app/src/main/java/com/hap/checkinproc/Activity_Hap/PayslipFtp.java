package com.hap.checkinproc.Activity_Hap;

import static android.os.Build.VERSION.SDK_INT;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.hap.checkinproc.Activity.PdfViewerActivity;
import com.hap.checkinproc.BuildConfig;
import com.hap.checkinproc.Interface.onListItemClick;
import com.hap.checkinproc.Interface.onPayslipItemClick;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.HAPListItem;
import com.hap.checkinproc.adapters.adBackFolders;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;


public class PayslipFtp extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "FTPActivity";
    public static final String UserDetail = "MyPrefs";
    public FTPClient mFTPClient = null;

    RecyclerView mRecyclerView, mRecylPrvFldr;
    TextView btnCanteen;
    JSONArray lsFiles;
    HAPListItem listItems;
    adBackFolders bkFldrItems;

    SharedPreferences UserDetails;

    String HomePath = "/home/hapftp/Payroll", CurrPath = "",
            EmpId = "";
    String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
    Common_Class common_class;
    ImageView ivHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payslip_ftp);
        common_class = new Common_Class();
        ivHome = findViewById(R.id.toolbar_home);
        ivHome.setOnClickListener(this);


        CurrPath = HomePath;
        //refreshCurrFolder();
        getPaySlipFolder("");
        btnCanteen = findViewById(R.id.btnCanteen);
        btnCanteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent frmCanteen = new Intent(PayslipFtp.this, foodExp.class);
                startActivity(frmCanteen);
                finish();
            }
        });
        UserDetails = getSharedPreferences(UserDetail, Context.MODE_PRIVATE);
        EmpId = UserDetails.getString("EmpId", "");
        HAPListItem.SetPayOnClickListener(new onPayslipItemClick() {
            @Override
            public void onClick(JSONObject item) {
                try {
                    if (item.getString("type").equalsIgnoreCase("dir")) {
                        getPaySlipFolder(item.getString("name"));
                    } else {
                        if (ftpDownload(CurrPath + "/" + item.getString("name"), pdfPath + item.getString("name"))) {
                            Intent stat = new Intent(getApplicationContext(), PdfViewerActivity.class);
                            stat.putExtra("PDF_FILE", "local");
                            stat.putExtra("PDF_ONE", pdfPath + item.getString("name"));
                            startActivity(stat);
                        }
                    }
                } catch (JSONException e) {
                   // e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Error1:"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        adBackFolders.SetOnClickListener(new onListItemClick() {
            @Override
            public void onItemClick(JSONObject item) {
                try {
                    CurrPath = item.getString("path");

                    getPaySlipFolder("");
                    //refreshCurrFolder();
                } catch (JSONException e) {
                   // e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Error2:"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void refreshCurrFolder() {
        String[] folders = CurrPath.replace(HomePath, "").split("/");
        JSONArray FldrItems = new JSONArray();
        String path = HomePath;
        for (int il = 0; il < folders.length; il++) {
            JSONObject jsonObject = new JSONObject();
            try {
                if (!folders[il].equalsIgnoreCase("")) {
                    jsonObject.put("name", folders[il]);
                    path = path + "/" + folders[il];
                    jsonObject.put("path", path);
                    FldrItems.put(jsonObject);
                } else {
                    jsonObject.put("name", "Pay Slip");
                    jsonObject.put("path", path);
                    FldrItems.put(jsonObject);
                }
            } catch (JSONException e) {
               // e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Error3:"+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
        bkFldrItems = new adBackFolders(FldrItems, this);
        mRecylPrvFldr = findViewById(R.id.prvFolder);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mRecylPrvFldr.setLayoutManager(layoutManager);

        mRecylPrvFldr.setAdapter(bkFldrItems);
    }

    public void getPaySlipFolder(String mCurrPath) {
        if (ftpConnect("sf.hap.in", "hapftp", "VFAY$du3@=9^", 21)) {
            CurrPath = CurrPath + ((mCurrPath != "") ? "/" : "") + mCurrPath;
            lsFiles = ftpGetFilesList(CurrPath);

            listItems = new HAPListItem(lsFiles, this);
            mRecyclerView = findViewById(R.id.fileList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(layoutManager);

            mRecyclerView.setAdapter(listItems);
            ftpDisconnect();
            refreshCurrFolder();
        }
    }

    public boolean ftpConnect(String host, String username, String password, int port) {
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
            Toast.makeText(getApplicationContext(),"Failed to connect your FTP",Toast.LENGTH_SHORT).show();

           // Toast.makeText(getApplicationContext(), "Error: could not connect to host " + host + "\n" + e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    public boolean ftpDisconnect() {
        try {
            mFTPClient.logout();
            mFTPClient.disconnect();
            return true;
        } catch (Exception e) {
           // Log.d(TAG, "Error occurred while disconnecting from ftp server.");
            Toast.makeText(getApplicationContext(),"Error4:Error occurred while disconnecting from ftp server.",Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    // Method to get current working directory:

    public String ftpGetCurrentWorkingDirectory() {
        try {
            String workingDir = mFTPClient.printWorkingDirectory();
            return workingDir;
        } catch (Exception e) {
            //Log.d(TAG, "Error: could not get current working directory.");
            Toast.makeText(getApplicationContext(),"Error5: could not get current working directory",Toast.LENGTH_SHORT).show();
        }

        return null;
    }

    // Method to change working directory:

    public boolean ftpChangeDirectory(String directory_path) {
        try {
            mFTPClient.changeWorkingDirectory(directory_path);
        } catch (Exception e) {
          //  Log.d(TAG, "Error: could not change directory to " + directory_path);
            Toast.makeText(getApplicationContext(),"Error6: could not change directory to " + directory_path,Toast.LENGTH_SHORT).show();
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
                    if (name.indexOf("-" + EmpId + ".pdf") > -1) { //"-"+
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("name", name);
                        jsonObject.put("type", "FILE");
                        fileList.put(jsonObject);
                    }
                    Log.i(TAG, "File : " + name + "= -" + EmpId + ".pdf");
                } else {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name", name);
                    jsonObject.put("type", "DIR");
                    fileList.put(jsonObject);

                    Log.i(TAG, "Directory : " + name);
                }
            }
            return fileList;
        } catch (Exception e) {
           // e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Error7:"+e.getMessage(), Toast.LENGTH_SHORT).show();
            return fileList;
        }
    }

    // Method to create new directory:

    public boolean ftpMakeDirectory(String new_dir_path) {
        try {
            boolean status = mFTPClient.makeDirectory(new_dir_path);
            return status;
        } catch (Exception e) {
           // Log.d(TAG, "Error: could not create new directory named " + new_dir_path);
            Toast.makeText(getApplicationContext(),"Error8: could not create new directory named " + new_dir_path, Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    // Method to delete/remove a directory:

    public boolean ftpRemoveDirectory(String dir_path) {
        try {
            boolean status = mFTPClient.removeDirectory(dir_path);
            return status;
        } catch (Exception e) {
            //Log.d(TAG, "Error: could not remove directory named " + dir_path);
            Toast.makeText(getApplicationContext(),"Error9: could not remove directory named " + dir_path, Toast.LENGTH_SHORT).show();

        }

        return false;
    }

    // Method to delete a file:

    public boolean ftpRemoveFile(String filePath) {
        try {
            boolean status = mFTPClient.deleteFile(filePath);
            return status;
        } catch (Exception e) {
           // e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Error10:" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getApplicationContext(),"Error11: Could not rename file: " + from + " to: " + to, Toast.LENGTH_SHORT).show();
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
        boolean status = false;
        try {
            if (ftpConnect("sf.hap.in", "hapftp", "VFAY$du3@=9^", 21)) {
                if (SDK_INT >= 30) {
                    if (!Environment.isExternalStorageManager()) {
                        Snackbar.make(findViewById(android.R.id.content), "Permission needed!", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Settings", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        try {
                                            Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
                                            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri);
                                            startActivity(intent);
                                        } catch (Exception ex) {
                                            Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                            startActivity(intent);
                                        }
                                    }
                                })
                                .show();
                    } else {
                        FileOutputStream desFileStream = new FileOutputStream(desFilePath);
                        status = mFTPClient.retrieveFile(srcFilePath, desFileStream);

                        //pdfView.fromStream(desFileStream).load();
                        desFileStream.close();
                        ftpDisconnect();
                    }
                }else {
                    FileOutputStream desFileStream = new FileOutputStream(desFilePath);
                    status = mFTPClient.retrieveFile(srcFilePath, desFileStream);

                    //pdfView.fromStream(desFileStream).load();
                    desFileStream.close();
                    ftpDisconnect();
                }
                    return status;

            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Download Failed: " + e.getMessage(),Toast.LENGTH_SHORT).show();
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
           // Log.d(TAG, "upload failed: " + e);
            Toast.makeText(getApplicationContext(),"Error12: upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();

        }

        return status;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_home:
                common_class.openHome(PayslipFtp.this);
                break;
        }
    }
}
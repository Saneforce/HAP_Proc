package com.hap.checkinproc.Common_Class;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.Manifest.permission.ACCESS_BACKGROUND_LOCATION;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class LocationServices extends Activity {

    Activity activity;
    Context _context;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1001;

    public LocationServices(Activity activity, Context _context) {
        this.activity = activity;
        this._context = _context;
    }

    public boolean checkPermission() {
        int locationReq = ContextCompat.checkSelfPermission(_context, ACCESS_FINE_LOCATION);
        int coarseReq = ContextCompat.checkSelfPermission(_context, ACCESS_COARSE_LOCATION);
        int backReq = ContextCompat.checkSelfPermission(_context, ACCESS_BACKGROUND_LOCATION);

        return locationReq == PackageManager.PERMISSION_GRANTED && coarseReq == PackageManager.PERMISSION_GRANTED &&
                backReq == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission() {

        ActivityCompat.requestPermissions(activity, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, ACCESS_BACKGROUND_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0) {

                boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                boolean cameraAccepteds = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                if (locationAccepted && cameraAccepted && cameraAccepteds) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                          /*  if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {

                            }*/
                    }

                }
            }

        }

    }
}

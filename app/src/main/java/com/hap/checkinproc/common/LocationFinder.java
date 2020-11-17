package com.hap.checkinproc.common;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class LocationFinder {
    /*The desired interval for location updates. Inexact. Updates may be more or less frequent.*/
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /* The fastest rate for active location updates. Updates will never be more frequent than this value. */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    /* The identifier for the notification displayed for the foreground service. */
    private static final int NOTIFICATION_ID = 12345678;
    /*
     * Used to check whether the bound activity has really gone away and not unbound as part of an
     * orientation change. We create a foreground service notification only if the former takes
     * place.
     */
    private boolean mChangingConfiguration = false;

    /* Contains parameters used by {@link com.google.android.gms.location.FusedLocationProviderApi}. */
    private LocationRequest mLocationRequest;
    /* Provides access to the Fused Location Provider API. */
    private FusedLocationProviderClient mFusedLocationClient;
    private static final String TAG ="Location Finder";

    Context mContext;
    /* The current location. */
    private Location mLocation;

    public LocationFinder(Context context) {
        mContext=context;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        createLocationRequest();
        getLocation();
    }

    /* Sets the location request parameters. */
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    Location location=null;
    public Location getLocation()
    {
        location=null;
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                location = task.getResult();
                            } else {
                                Log.w(TAG, "Failed to get location.");
                            }
                        }
                    });
        } catch (SecurityException unlikely) {
            Log.e(TAG, "Lost location permission." + unlikely);
        }
        return location;
    }
}

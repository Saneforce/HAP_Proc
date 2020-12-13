package com.hap.checkinproc;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMservices  extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.

        String tpookem = com.google.firebase.iid.FirebaseInstanceId.getInstance().getToken();
        Log.d("DEVICE_TOKEN",tpookem);
    }
}

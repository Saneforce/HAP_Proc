package com.hap.checkinproc;

import android.content.Context;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;

public class MyFirebaseInstanceIdService extends FirebaseMessagingService {
    Shared_Common_Pref shared_common_pref;
    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.e("newToken", token);

        shared_common_pref = new Shared_Common_Pref(this);
        shared_common_pref.save(Shared_Common_Pref.Dv_ID, token);
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fcm_token", token).apply();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }

    //Whenewer you need FCM token, just call this static method to get it.
    public static String getToken(Context context) {
        return context.getSharedPreferences("_", MODE_PRIVATE).getString("fcm_token", "empty");
    }
}
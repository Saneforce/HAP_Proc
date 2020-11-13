package com.hap.checkinproc;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

public class HAPApp extends Application {

    public static Activity activeActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        setupActivityListener();
    }
   /* public Activity getActiveActivity(){
        return activeActivity;
    }*/
    private void setupActivityListener() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                activeActivity = activity;
            }
            @Override
            public void onActivityStarted(Activity activity) {
            }
            @Override
            public void onActivityResumed(Activity activity) {
                activeActivity = activity;
            }
            @Override
            public void onActivityPaused(Activity activity) {
              //  activeActivity = null;
            }
            @Override
            public void onActivityStopped(Activity activity) {
            }
            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }
            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
    }

    public static Activity getActiveActivity(){
        return activeActivity;
    }

}
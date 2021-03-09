package com.hap.checkinproc.Common_Class;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.hap.checkinproc.Model_Class.Location;
import com.hap.checkinproc.common.TimerService;

public class LocationBlocker  extends Activity {

    public LocationBlocker() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, TimerService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(new Intent(this, TimerService.class));

    }

    @Override
    protected void onPause() {
        super.onPause();
        startService(new Intent(this, TimerService.class));

    }

    @Override
    protected void onStop() {
        super.onStop();
        startService(new Intent(this, TimerService.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        startService(new Intent(this, TimerService.class));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startService(new Intent(this, TimerService.class));
    }

}

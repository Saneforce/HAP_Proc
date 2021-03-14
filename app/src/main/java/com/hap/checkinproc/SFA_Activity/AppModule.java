package com.hap.checkinproc.SFA_Activity;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 *Created by thiru on 03/03/2021.
 */



@Module
public class AppModule {
    private Application mApplication;
    public AppModule(Application mApplication) {
        this.mApplication = mApplication;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return mApplication;
    }
}
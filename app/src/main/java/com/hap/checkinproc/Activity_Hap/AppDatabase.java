package com.hap.checkinproc.Activity_Hap;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.hap.checkinproc.Model_Class.EventCapture;
import com.hap.checkinproc.Model_Class.EventCaptureDao;

@Database(entities = {EventCapture.class}, version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract EventCaptureDao taskDao();
}

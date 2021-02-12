package com.hap.checkinproc.common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DBHAPCheckin";
    private static final String TABLE_Track = "Tracking_Location";
    private static final String Loc_Date = "Loc_Date";
    private static final String Loc_Lat = "Loc_Lat";
    private static final String Loc_Lng = "Loc_Lng";
    private static final String SF_Code = "Loc_Lng";
    private static final String Speed = "Speed";
    private static final String Bearing = "Bearing";
    private static final String Accuracy = "Accuracy";
    private static final String Flag = "Flag";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_Track + "("
                + Loc_Date + " TEXT PRIMARY KEY,"
                + Loc_Lat + " TEXT," + Loc_Lng + " TEXT," + Speed+ " TEXT," + Bearing+ " TEXT," + Accuracy+ " TEXT," + Flag + " INT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Track);

        // Create tables again
        onCreate(db);
    }

    void addTrackDetails(JSONObject Location) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(Loc_Date, Location.getString("Time"));
            values.put(Loc_Lat, Location.getString("Latitude"));
            values.put(Loc_Lng, Location.getString("Longitude"));
            values.put(Speed, Location.getString("Speed"));
            values.put(Bearing, Location.getString("Bearing"));
            values.put(Accuracy, Location.getString("Accuracy"));
            values.put(Flag, 0);


            // Inserting Row
            db.insert(TABLE_Track, null, values);
            //2nd argument is String containing nullColumnHack
            db.close(); // Closing database connection
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void updateTrackDetails(JSONObject Location,int flag) {
        try{
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(Flag, flag);

            // updating row
            db.update(TABLE_Track, values, Loc_Date + " = ?",
                    new String[] { Location.getString("Time") });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    // Deleting single TrackDetails
    public void deleteTrackDetails(JSONObject Location) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_Track, Loc_Date + " = ?",
                    new String[]{Location.getString("Time")});
            db.close();
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public JSONArray getAllPendingTrackDetails() {
         // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_Track+" WHERE "+Flag+"=0";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        JSONArray jsonArray=new JSONArray();
        if (cursor.moveToFirst()) {
            do {
                JSONObject item = new JSONObject();
                try {
                    item.put("Time", cursor.getString(0));
                    item.put("Latitude", cursor.getString(1));
                    item.put("Longitude", cursor.getString(2));
                    item.put("Speed", cursor.getString(3));
                    item.put("Bearing", cursor.getString(4));
                    item.put("Accuracy", cursor.getString(5));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(item);

            } while (cursor.moveToNext());
        }

        // return contact list
        return jsonArray;
    }
}

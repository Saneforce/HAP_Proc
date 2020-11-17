package com.hap.checkinproc.Activity_Hap;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.provider.Settings;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Common_Class {
    public static String Version_Name="Ver 2.3.2";
    public static String Work_Type="0";
    public static Location location=null;


    public void openDateTimeSetting() {
        Intent intent = new Intent(Settings.ACTION_DATE_SETTINGS);
        //this.webView.getContext().startActivity(intent);
    }

    public static boolean isTimeAutomatic(Context c) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
        } else {
            return android.provider.Settings.System.getInt(c.getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0) == 1;
        }
    }
    public  String GetDateTime(Context context,String pattern) {
        if(isTimeAutomatic(context)==false){

            //this.webView.sendJavascript("blockApp('date')");
        }
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(c.getTime());

    }
    public String AddDays(String dateInString,int NoofDays, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        try {

            c.setTime(sdf.parse(dateInString));
            c.add(Calendar.DATE, NoofDays);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        sdf = new SimpleDateFormat(pattern);
        Date resultdate = new Date(c.getTimeInMillis());
        dateInString = sdf.format(resultdate);
        return dateInString;

    }
    public String AddMonths(String dateInString,int NoofDays, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        try {

            c.setTime(sdf.parse(dateInString));
            c.add(Calendar.MONTH, NoofDays);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        sdf = new SimpleDateFormat(pattern);
        Date resultdate = new Date(c.getTimeInMillis());
        dateInString = sdf.format(resultdate);
        return dateInString;

    }
    public int getDay(String dateInString){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dateInString));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return c.get(Calendar.DATE);
    }

    public int getMonth(String dateInString){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dateInString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return c.get(Calendar.MONTH);
    }

    public int getYear(String dateInString){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dateInString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return c.get(Calendar.YEAR);
    }
    public Date getDate(String dateInString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dateInString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date resultdate = new Date(c.getTimeInMillis());
        return resultdate;
    }
}

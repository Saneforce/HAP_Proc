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
    public static String Version_Name="Ver 3.3.1";
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
    public static boolean isNullOrEmpty(String str) {
        if (str != null && !str.isEmpty())
            return false;
        return true;
    }
    public Date GetCurrDateTime(Context context) {
        if (isTimeAutomatic(context) == false) {

            //this.webView.sendJavascript("blockApp('date')");
        }
        Calendar c = Calendar.getInstance();

        Date resultdate = new Date(c.getTimeInMillis());
        return resultdate;

    }

    public String GetDateTime(Context context, String pattern) {
        if (isTimeAutomatic(context) == false) {

            //this.webView.sendJavascript("blockApp('date')");
        }
        Calendar c = Calendar.getInstance();

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
    public Date AddDays(String dateInString,int NoofDays) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        try {

            c.setTime(sdf.parse(dateInString));
            c.add(Calendar.DATE, NoofDays);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date resultdate = new Date(c.getTimeInMillis());
        return resultdate;
    }
    public Date AddMonths(String dateInString,int NoofMonths) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        try {

            c.setTime(sdf.parse(dateInString));
            c.add(Calendar.MONTH, NoofMonths);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date resultdate = new Date(c.getTimeInMillis());
        return resultdate;
    }
    public String AddMonths(String dateInString,int NoofDays, String pattern) {
        Log.d("DateString",dateInString);
        Log.d("DateString No.Dys",String.valueOf(NoofDays));
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
        Log.d("DateString GMonth",dateInString);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dateInString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("DateString GMonth",dateInString+"-"+String.valueOf(c.get(Calendar.MONTH)+1));
        return c.get(Calendar.MONTH)+1;
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
    public int getHour(String dateInString){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dateInString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return c.get(Calendar.HOUR_OF_DAY);

    }
    public int getMinute(String dateInString){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dateInString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return c.get(Calendar.MINUTE);
    }
    public int getSeconds(String dateInString){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dateInString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return c.get(Calendar.SECOND);
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
    public String getDateWithFormat(String dateInString, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dateInString));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        sdf = new SimpleDateFormat(pattern);
        Date resultdate = new Date(c.getTimeInMillis());
        dateInString = sdf.format(resultdate);
        return dateInString;
    }

    public String getDateWithFormat(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String dateInString = sdf.format(date);
        return dateInString;
    }

    public Date AddMinute(Date date,int NoofDays) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        try {

            c.setTime(date);
            c.add(Calendar.MINUTE, NoofDays);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Date resultdate = new Date(c.getTimeInMillis());
        return resultdate;

    }
    public long Daybetween(String date1,String date2)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date Date1 = null,Date2 = null;
        try{
            Date1 = sdf.parse(date1);
            Date2 = sdf.parse(date2);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return (Date2.getTime() - Date1.getTime())/(24*60*60*1000);
    }
    public long minutesBetween(String date1,String date2)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date Date1 = null,Date2 = null;
        try{
            Date1 = sdf.parse(date1);
            Date2 = sdf.parse(date2);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return (Date2.getTime() - Date1.getTime())/(60*1000);
    }

}

package com.hap.checkinproc.Common_Class;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Shared_Common_Pref {
    SharedPreferences Common_pref;
    SharedPreferences.Editor editor;
    Activity activity;
    Context _context;
    public static final String spName = "SP_LOGIN_DETAILS";
    public static String Sf_Code = "Sf_Code";
    public static String Profile = "Profile";
    public static String Div_Code = "Div_Code";
    public static String StateCode = "StateCode";
    public static String Sf_Name = "Sf_Name";
    public static String Dv_ID = "DvID";
    public static String SF_Type = "SF_Type";
    public static String CHECK_COUNT = "0";
    public static String SF_EMP_ID = "sf_emp_id";
    public static String SF_DESIG = "sf_Designation_Short_Name";
    public static String SF_DEPT = "DeptName";

    public static  Double Outletlat ;
    public static  Double Outletlong;
    public static  String OutletAddress;
    public static  String Outler_AddFlag;
    public static String OutletName="OutletName";
    public static String OutletCode="OutletCode";

    public static String Dept_Type = "Dept_Type";

    public static final String spNamemas = "SP_MAS_DETAILS";
    public static final String loggedIn = "loggedIn";
    public static final String Password = "Password";
    public static final String name = "name";
    public static final String nameuser = "nameuser";
    public static final String cards_pref = "cards_pref";
    public static final String login_user = "login_user";
    public static final String Remember_me = "rememberMe";
    public static final String mastersynclog = "masterlog";//,boolean mastersynclog
    //My day plan
    public static final String spMydayplan = "SP_MY_DAY_PLAN";
    public static final String Worktype = "worktype";
    public static final String Cluster = "cluster";
    public static final String Work_date = "workdate";
    public static final String Status = "status";
    public static int TotalCountApproval = 0;


    public Shared_Common_Pref(Activity Ac) {
        activity = Ac;
        if (activity != null) {
            Common_pref = activity.getSharedPreferences("Preference_values", Context.MODE_PRIVATE);
            editor = Common_pref.edit();
        }
    }

    public Shared_Common_Pref(Context cc) {
        this._context = cc;
        Common_pref = cc.getSharedPreferences("Preference_values", Context.MODE_PRIVATE);
        editor = Common_pref.edit();
    }

    public void save(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getvalue(String key) {
        String text = null;
        text = Common_pref.getString(key, null);
        return text;
    }

    public void clear_pref(String key) {
        Common_pref.edit().remove(key).apply();

        //the good quality product by the end of the day worth od manual  developement in this quality regaurds minimum qu.
    }


}

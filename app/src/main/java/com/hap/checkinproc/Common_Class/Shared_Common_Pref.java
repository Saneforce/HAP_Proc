package com.hap.checkinproc.Common_Class;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Shared_Common_Pref {
    public static  String VAN_SALES_MODE ="Van Sales Order" ;
    public static String SFA_MENU = "";
    public static String CUSTOMER_CODE = "";
    public static String SALES_MODE = "";
    public static String LOGINTYPE = "";
    public static int Projection_Approval=0;
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
    public static String DAMode = "DAMode";
    public static Double Outletlat;
    public static Double Outletlong;
    public static String OutletAddress;
    public static String Outler_AddFlag = "";
    public static String FromActivity = "";
    public static String OutletName = "OutletName";
    public static String OutletCode = "OutletCode";
    public static String SecOrdOutletType = "";


    public static String Dept_Type = "Dept_Type";
    public static String Sync_Flag;
    public static String spNamemas = "SP_MAS_DETAILS";
    public static String loggedIn = "loggedIn";
    public static String Password = "Password";
    public static String name = "name";
    public static String nameuser = "nameuser";
    public static String cards_pref = "cards_pref";
    public static String login_user = "login_user";
    public static String Remember_me = "rememberMe";
    public static String mastersynclog = "masterlog";//,boolean mastersynclog
    //My day plan
    public static String spMydayplan = "SP_MY_DAY_PLAN";
    public static String Worktype = "worktype";
    public static String Cluster = "cluster";
    public static String Work_date = "workdate";
    public static String Status = "status";
    public static int TotalCountApproval = 0;
    public static String TransSlNo;
    public static String Outlet_Info_Flag;
    public static String Invoicetoorder;
    public static String OutletAvail = "OutletAvail";
    public static String OutletUniv = "OutletUniv";

    public static String Rout_List = "Rout_List";
    public static String Outlet_List = "Outlet_List";
    public static String Distributor_List = "Distributor_List";
    public static String Category_List = "Category_List";
    public static String Product_List = "Product_List";
    public static String GetTodayOrder_List = "GetTodayOrder_List";
    public static String TodayOrderDetails_List = "TodayOrderDetails_List";
    public static String Compititor_List = "Compititor_List";
    public static String Todaydayplanresult = "Todaydayplanresult";
    public static String Outlet_Total_Orders = "Outlet_Total_Orders";
    public static String Outlet_Total_AlldaysOrders = "Outlet_Total_AlldaysOrders";

    public static String TodaySfOrdervalues = "TodaySfOrdervalues";
    public static String DistributorCode = "DistributorCode";
    public static String DistributorName = "DistributorName";
    public static String Route_Code = "Route_Code";
    public static String Route_name = "Route_name";

    public static String Editoutletflag = "Editoutletflag";
    public static String Tp_Approvalflag = "Tp_Approvalflag";
    public static String Tp_Sf_name = "Tp_Sf_name";
    public static String Tp_Monthname = "Tp_Monthname";
    public static String Tp_SFCode = "Tp_SFCode";
    public static String DCRMode = "DCRMode";


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

    public void save(String key, Boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void save(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }


    public String getvalue(String key, String defValue) {
        if (defValue == null) defValue = "";
        String text = Common_pref.getString(key, defValue);
        return text;
    }

    public String getvalue(String key) {
        String text = Common_pref.getString(key, "");
        return text;
    }

    public Boolean getBoolValue(String Key) {
        Boolean val = false;
        val = Common_pref.getBoolean(Key, false);
        return val;
    }

    public int getIntValue(String Key) {
        int val = Common_pref.getInt(Key, -1);
        return val;
    }

    public void clear_pref(String key) {
        Common_pref.edit().remove(key).apply();

        //the good quality product by the end of the day worth od manual  developement in this quality regaurds minimum qu.
    }


}

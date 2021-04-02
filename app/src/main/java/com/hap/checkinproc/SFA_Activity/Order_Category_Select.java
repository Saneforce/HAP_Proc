package com.hap.checkinproc.SFA_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.service.Common;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Activity_Hap.Dashboard;
import com.hap.checkinproc.Activity_Hap.SFA_Activity;
import com.hap.checkinproc.Common_Class.AlertDialogBox;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.Interface.AlertBox;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.Route_View_Adapter;
import com.hap.checkinproc.SFA_Model_Class.Category_Universe_Modal;
import com.hap.checkinproc.SFA_Model_Class.OutletReport_View_Modal;
import com.hap.checkinproc.SFA_Model_Class.Product_Details_Modal;
import com.hap.checkinproc.SFA_Model_Class.Product_Details_Modal;
import com.hap.checkinproc.SFA_Model_Class.RegularQty_Modal;
import com.hap.checkinproc.SFA_Model_Class.Trans_Order_Details_Offline;
import com.hap.checkinproc.Status_Adapter.ExtendedShift_Status_Adapter;
import com.hap.checkinproc.Status_Model_Class.MissedPunch_Status_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.hap.checkinproc.Common_Class.Shared_Common_Pref.OutletName;

public class Order_Category_Select extends AppCompatActivity implements View.OnClickListener {
    GridView categorygrid;
    List<Category_Universe_Modal> Category_Modal = new ArrayList<>();
    List<Product_Details_Modal> Product_Modal;
    List<Product_Details_Modal> Order_Outlet_Filter;
    List<com.hap.checkinproc.SFA_Model_Class.RegularQty_Modal> RegularQty_Modal;
    List<Product_Details_Modal> Product_ModalSetAdapter;
    List<Product_Details_Modal> Getorder_Array_List;
    List<Trans_Order_Details_Offline> InvoiceorderDetails_List;
    List<Category_Universe_Modal> listt;
    Type userType;
    Gson gson;
    TextView takeorder, ok, back, Out_Let_Name, Category_Nametext, totalqty, totalvalue, orderbutton, netamount;
    @Inject
    Retrofit retrofit;
    private RecyclerView recyclerView;
    LinearLayout lin_orderrecyclerview, lin_gridcategory, totalorderbottom, linnetamount, linnercashdiscount;
    public boolean gobackflag = false;
    Common_Class common_class;
    JSONObject ProductJson_Object;
    JSONObject Activity_Report_APP_Object, Activity_Outlet_Report_object, Product_Details_Object,
            eventCapturesObjectArray, pendingBillObjectArray, ComProductObjectArray, Input_Report, Trans_Order_Details_Object;
    JSONArray sendtoserverArray;
    String Convert_Json_toString;
    Shared_Common_Pref sharedCommonPref;
    EditText cashdiscount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__category__select);
        //((MyApplication) getApplication()).getNetComponent().inject(this);
        sharedCommonPref = new Shared_Common_Pref(Order_Category_Select.this);
        common_class = new Common_Class(this);
        categorygrid = findViewById(R.id.category);
        takeorder = findViewById(R.id.takeorder);
        orderbutton = findViewById(R.id.orderbutton);
        netamount = findViewById(R.id.netamount);
        ok = findViewById(R.id.ok);
        back = findViewById(R.id.back);
        lin_orderrecyclerview = findViewById(R.id.lin_orderrecyclerview);
        totalorderbottom = findViewById(R.id.totalorderbottom);
        cashdiscount = findViewById(R.id.cashdiscount);
        linnetamount = findViewById(R.id.linnetamount);
        linnercashdiscount = findViewById(R.id.linnercashdiscount);
        lin_gridcategory = findViewById(R.id.lin_gridcategory);
        totalqty = findViewById(R.id.totalqty);
        totalvalue = findViewById(R.id.totalvalue);
        Out_Let_Name = findViewById(R.id.outlet_name);
        Category_Nametext = findViewById(R.id.Category_Nametext);
        Out_Let_Name.setText(Shared_Common_Pref.OutletName);
        Product_ModalSetAdapter = new ArrayList<>();
        gson = new Gson();
        ok.setOnClickListener(this);
        takeorder.setOnClickListener(this);
        back.setOnClickListener(this);
        orderbutton.setOnClickListener(this);
        Out_Let_Name.setText(Shared_Common_Pref.OutletName);
        recyclerView = findViewById(R.id.orderrecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        GetJsonData(sharedCommonPref.getvalue(Shared_Common_Pref.Category_List), "1");
        String OrdersTable = sharedCommonPref.getvalue(Shared_Common_Pref.Product_List);
        userType = new TypeToken<ArrayList<Product_Details_Modal>>() {
        }.getType();
        Product_Modal = gson.fromJson(OrdersTable, userType);
        if (Shared_Common_Pref.Invoicetoorder == null || Shared_Common_Pref.Invoicetoorder.equals("0")) {
            Get_regularqty();
        }

        if (Shared_Common_Pref.Invoicetoorder != null) {
            if (Shared_Common_Pref.Invoicetoorder.equals("1")) {
                String orderlist = sharedCommonPref.getvalue(Shared_Common_Pref.TodayOrderDetails_List);
                userType = new TypeToken<ArrayList<Trans_Order_Details_Offline>>() {
                }.getType();
                InvoiceorderDetails_List = gson.fromJson(orderlist, userType);
                Order_Outlet_Filter = new ArrayList<>();
                for (Trans_Order_Details_Offline ivl : InvoiceorderDetails_List) {
                    if (ivl.getTransSlNo().equals(Shared_Common_Pref.TransSlNo)) {
                        Order_Outlet_Filter.add(new Product_Details_Modal(ivl.getProductCode(), ivl.getProductName(), 1, "1",
                                "1", "5", "i", 7.99, 1.8, ivl.getRate(), ivl.getQuantity(), ivl.getQty(), ivl.getValue()));
                    }

                }
                //Log.e("Product_Modal.size()", String.valueOf(Product_Modal.size()));
                for (int j = 0; Product_Modal.size() > j; j++) {
                    for (int i = 0; Order_Outlet_Filter.size() > i; i++) {
                        if (Product_Modal.get(j).getId().equals(Order_Outlet_Filter.get(i).getId())) {
                            Product_Modal.get(j).setQty(Order_Outlet_Filter.get(i).getQty());
                            Log.e("SETQTY", String.valueOf(Order_Outlet_Filter.get(i).getQty()));
                            Product_Modal.get(j).setAmount(Order_Outlet_Filter.get(i).getAmount());
                            Product_Modal.get(j).setRegularQty(Order_Outlet_Filter.get(i).getRegularQty());
                        }
                    }
                }
                GetJsonData(sharedCommonPref.getvalue(Shared_Common_Pref.Category_List), "2");
                int jki = 0;
                Log.e("Category_Before", String.valueOf(Category_Modal.size()));
                for (Category_Universe_Modal CM : Category_Modal) {
                    for (Product_Details_Modal PM : Product_Modal) {
                        Log.e("GETQTY", String.valueOf(PM.getQty()));
                        Log.e("GETREgular", String.valueOf(PM.getRegularQty()));
                        if (CM.getId().equals(PM.getProductCatCode()) && (PM.getQty() > 0 || PM.getRegularQty() > 0)) {
                            Category_Modal.get(jki).setColorFlag("1");
                            Log.e("Category_Modal_CAT", Category_Modal.get(jki).getColorFlag());
                        }
                    }
                    jki++;

                }
                FilterProduct("invoice", true);
            } else {

            }
        }

        cashdiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")) {
                    if (Double.valueOf(s.toString()) > 0) {
                        Double totalamount = Double.valueOf(totalvalue.getText().toString());
                        if (Double.valueOf(s.toString()) > Double.valueOf(totalvalue.getText().toString())) {
                            Toast.makeText(Order_Category_Select.this, "Discount Exceeded", Toast.LENGTH_SHORT).show();
                            cashdiscount.setText("");
                            netamount.setText("" + totalvalue.getText().toString());
                        } else {
                            Double discountvalues = totalamount - Double.valueOf(s.toString());
                            netamount.setText("" + discountvalues);
                        }
                    }
                } else {
                    netamount.setText("" + totalvalue.getText().toString());
                }
            }
        });
    }

    private void GetJsonData(String jsonResponse, String type) {
        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);
            Category_Modal.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String id = String.valueOf(jsonObject1.optInt("id"));
                String name = jsonObject1.optString("name");
                String Division_Code = jsonObject1.optString("Division_Code");
                String Cat_Image = jsonObject1.optString("Cat_Image");
                String sampleQty = jsonObject1.optString("sampleQty");
                String colorflag = jsonObject1.optString("colorflag");
                if (type.equals("1")) {
                    Category_Modal.add(new Category_Universe_Modal(id, name, Division_Code, Cat_Image, sampleQty, colorflag));
                } else {
                    Category_Modal.add(new Category_Universe_Modal(id, name, Division_Code, Cat_Image, sampleQty, colorflag));


                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.takeorder:
                FilterProduct("order", false);
                break;
            case R.id.ok:
                lin_gridcategory.setVisibility(View.VISIBLE);
                lin_orderrecyclerview.setVisibility(View.GONE);
                totalorderbottom.setVisibility(View.GONE);
                orderbutton.setVisibility(View.GONE);
                gobackflag = false;
                takeorder.setVisibility(View.VISIBLE);
                Order_Category_Select.CategoryAdapter customAdapteravail = new Order_Category_Select.CategoryAdapter(getApplicationContext(), Category_Modal);
                categorygrid.setAdapter(customAdapteravail);
                break;
            case R.id.back:
                if (gobackflag == false) {
                    common_class.CommonIntentwithFinish(Invoice_History.class);
                } else {
                    gobackflag = false;
                    lin_gridcategory.setVisibility(View.VISIBLE);
                    lin_orderrecyclerview.setVisibility(View.GONE);
                    totalorderbottom.setVisibility(View.GONE);
                    orderbutton.setVisibility(View.GONE);
                    takeorder.setVisibility(View.VISIBLE);
                    Order_Category_Select.CategoryAdapter customAdapteravaill = new Order_Category_Select.CategoryAdapter(getApplicationContext(), Category_Modal);
                    categorygrid.setAdapter(customAdapteravaill);
                }
                break;
            case R.id.orderbutton:
                SaveOrder();
                break;
        }
    }

    private void SaveOrder() {
        AlertDialogBox.showDialog(Order_Category_Select.this, "HAP SFA", "Are You Sure Want to Submit?", "OK", "Cancel", false, new AlertBox() {
            @Override
            public void PositiveMethod(DialogInterface dialog, int id) {
                JSONObject dayplan_json_Object = new JSONObject();
                JSONObject Outtlet_Json_Object = new JSONObject();
                Activity_Report_APP_Object = new JSONObject();
                Activity_Outlet_Report_object = new JSONObject();
                Activity_Outlet_Report_object = new JSONObject();
                eventCapturesObjectArray = new JSONObject();
                Input_Report = new JSONObject();
                pendingBillObjectArray = new JSONObject();
                ComProductObjectArray = new JSONObject();
                Trans_Order_Details_Object = new JSONObject();
                Product_Details_Object = new JSONObject();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                Calendar calobj = Calendar.getInstance();
                String dateTime = df.format(calobj.getTime());
                try {
                    dayplan_json_Object.put("Worktype_code", "'41'");
                    dayplan_json_Object.put("Town_code", "'10'");
                    dayplan_json_Object.put("totalproductitem", common_class.addquote(String.valueOf(Getorder_Array_List.size())));
                    dayplan_json_Object.put("dcr_activity_date", common_class.addquote(dateTime));
                    dayplan_json_Object.put("Daywise_Remarks", "''");
                    dayplan_json_Object.put("eKey", Common_Class.GetEkey());
                    dayplan_json_Object.put("rx", "'1'");
                    dayplan_json_Object.put("rx_t", "''");
                    dayplan_json_Object.put("orderValue", totalvalue.getText().toString());
                    dayplan_json_Object.put("DataSF", common_class.addquote(Shared_Common_Pref.Sf_Code));
                    Activity_Report_APP_Object.put("Activity_Report_APP", dayplan_json_Object);
                    Outtlet_Json_Object.put("Doctor_POB", 6);
                    Outtlet_Json_Object.put("Worked_With", "");
                    Outtlet_Json_Object.put("Doc_Meet_Time", Common_Class.addquote(Common_Class.GetDate()));
                    Outtlet_Json_Object.put("modified_time", Common_Class.addquote(Common_Class.GetDate()));
                    Outtlet_Json_Object.put("net_weight_value", 1.50);
                    Outtlet_Json_Object.put("stockist_code", "'2625'");
                    Outtlet_Json_Object.put("stockist_name", "'SRI SARASWATHI AGENCIES'");
                    Outtlet_Json_Object.put("superstockistid", "''");
                    Outtlet_Json_Object.put("Discountpercent", "''");
                    Outtlet_Json_Object.put("CheckinTime", "''");
                    Outtlet_Json_Object.put("CheckoutTime", "''");
                    Outtlet_Json_Object.put("location", "''");
                    Outtlet_Json_Object.put("geoaddress", "''");
                    Outtlet_Json_Object.put("PhoneOrderTypes", 1);
                    Outtlet_Json_Object.put("Order_Stk", "''");
                    Outtlet_Json_Object.put("Order_No", "''");
                    Outtlet_Json_Object.put("rootTarget", "''");
                    Outtlet_Json_Object.put("orderValue", totalvalue.getText().toString());
                    Outtlet_Json_Object.put("rateMode", "Free");
                    Outtlet_Json_Object.put("discount_price", 0);
                    Outtlet_Json_Object.put("doctor_code", common_class.addquote(Shared_Common_Pref.OutletCode));
                    Outtlet_Json_Object.put("doctor_name", common_class.addquote(Shared_Common_Pref.OutletName));
                    Activity_Outlet_Report_object.put("Activity_Doctor_Report", Outtlet_Json_Object);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray personarray = new JSONArray();
                for (int z = 0; z < Getorder_Array_List.size(); z++) {
                    ProductJson_Object = new JSONObject();
                    try {
                        ProductJson_Object.put("product_Name", Getorder_Array_List.get(z).getName());
                        ProductJson_Object.put("product_code", Getorder_Array_List.get(z).getId());
                        ProductJson_Object.put("Product_Rx_Qty", Getorder_Array_List.get(z).getQty());
                        ProductJson_Object.put("Product_RegularQty", Getorder_Array_List.get(z).getRegularQty());
                        ProductJson_Object.put("Product_Total_Qty", Getorder_Array_List.get(z).getQty() + Getorder_Array_List.get(z).getRegularQty());
                        ProductJson_Object.put("Product_Sample_Qty", Getorder_Array_List.get(z).getAmount());
                        ProductJson_Object.put("cb_qty", 0);
                        ProductJson_Object.put("free", 0);
                        ProductJson_Object.put("net_weight", 0);
                        ProductJson_Object.put("discount", 0);
                        ProductJson_Object.put("Rate", Getorder_Array_List.get(z).getRate());
                        personarray.put(ProductJson_Object);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                JSONObject eventCapturesObject = new JSONObject();
                JSONArray eventCapturesArray = new JSONArray();
                eventCapturesArray.put(eventCapturesObject);
                try {
                    Product_Details_Object.put("Activity_Sample_Report", personarray);
                    eventCapturesObjectArray.put("Activity_Event_Captures", eventCapturesArray);
                    Input_Report.put("Activity_Input_Report", eventCapturesArray);
                    pendingBillObjectArray.put("PENDING_Bills", eventCapturesArray);
                    ComProductObjectArray.put("Compititor_Product", eventCapturesArray);
                    Trans_Order_Details_Object.put("Compititor_Product", eventCapturesArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sendtoserverArray = new JSONArray();
                sendtoserverArray.put(Activity_Report_APP_Object);//0-Activity_Report_APP
                sendtoserverArray.put(Activity_Outlet_Report_object);//1-Activity_Doctor_Report
                sendtoserverArray.put(Product_Details_Object);//2-Activity_Sample_Report
                sendtoserverArray.put(Trans_Order_Details_Object);//3-Trans_Order_Details
                sendtoserverArray.put(Input_Report);//4-Activity_Input_Report
                sendtoserverArray.put(eventCapturesObjectArray);//5-Activity_Event_Captures
                sendtoserverArray.put(pendingBillObjectArray);//6-PENDING_Bills
                sendtoserverArray.put(ComProductObjectArray);//8-Compititor_Product
                Convert_Json_toString = sendtoserverArray.toString();//
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

                Call<JsonObject> responseBodyCall = apiInterface.submitValue(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, Convert_Json_toString);
                Log.e("Convert_Json_toString", Convert_Json_toString);
                responseBodyCall.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            try {
                                Log.e("JSON_VALUES", response.body().toString());
                                JSONObject jsonObjects = new JSONObject(response.body().toString());
                                String san = jsonObjects.getString("success");
                                Log.e("Success_Message", san);
                                if (san.equals("true")) {
                                    Toast.makeText(Order_Category_Select.this, "Order Submitted Successfully", Toast.LENGTH_SHORT).show();
                                    Shared_Common_Pref.Sync_Flag = "2";
                                    startActivity(new Intent(getApplicationContext(), Offline_Sync_Activity.class));
                                }

                            } catch (Exception e) {

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.e("SUBMIT_VALUE", "ERROR");
                    }
                });

            }

            @Override
            public void NegativeMethod(DialogInterface dialog, int id) {

                dialog.dismiss();
            }
        });

    }

    private void FilterProduct(String StringFlag, boolean flag) {
        boolean checkavail = flag;
        if (StringFlag.equals("order")) {
            linnercashdiscount.setVisibility(View.GONE);
            linnetamount.setVisibility(View.GONE);
            for (Category_Universe_Modal cl : listt) {
                if (cl.getColorFlag().equals("1")) {
                    checkavail = true;
                }
            }
            orderbutton.setText("ORDER");
        } else {
            Order_Category_Select.CategoryAdapter customAdapteravail = new Order_Category_Select.CategoryAdapter(getApplicationContext(), Category_Modal);
            categorygrid.setAdapter(customAdapteravail);
            linnercashdiscount.setVisibility(View.VISIBLE);
            linnetamount.setVisibility(View.VISIBLE);
            orderbutton.setText("INVOICE");
        }
        if (checkavail == false) {
            lin_gridcategory.setVisibility(View.VISIBLE);
            lin_orderrecyclerview.setVisibility(View.GONE);
            totalorderbottom.setVisibility(View.GONE);
            orderbutton.setVisibility(View.GONE);
            gobackflag = false;
            takeorder.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Enter The Qty", Toast.LENGTH_SHORT).show();
        } else {
            lin_gridcategory.setVisibility(View.GONE);
            lin_orderrecyclerview.setVisibility(View.VISIBLE);
            totalorderbottom.setVisibility(View.VISIBLE);
            orderbutton.setVisibility(View.VISIBLE);
            gobackflag = true;
            takeorder.setVisibility(View.GONE);
            Category_Nametext.setText("");
            Category_Nametext.setVisibility(View.GONE);
            int talqty = 0, totalvalues = 0;
            Getorder_Array_List = new ArrayList<>();
            Getorder_Array_List.clear();
            for (Product_Details_Modal pm : Product_Modal) {
                System.out.println("Product_getQty" + pm.getQty());
                System.out.println("Product_getQty" + pm.getRegularQty());
                if (pm.getRegularQty() != null) {
                    if (pm.getQty() > 0 || pm.getRegularQty() > 0) {
                        Getorder_Array_List.add(pm);
                        talqty += pm.getQty() + pm.getRegularQty();
                        totalvalues += pm.getAmount();
                    }
                }
            }
            totalvalue.setText("" + totalvalues);
            if (StringFlag.equals("order")) {
                netamount.setText("");
            } else {
                netamount.setText("" + totalvalues);

            }
            totalqty.setText("" + talqty);
            recyclerView.setAdapter(new Prodct_Adapter(Getorder_Array_List, R.layout.product_order_recyclerview, getApplicationContext(), -1));
            new Prodct_Adapter(Getorder_Array_List, R.layout.product_order_recyclerview, getApplicationContext(), 0).notifyDataSetChanged();
            recyclerView.setItemViewCacheSize(Product_Modal.size());

        }
    }

    public class CategoryAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflter;

        public CategoryAdapter(Context applicationContext, List<Category_Universe_Modal> list) {
            this.context = applicationContext;
            listt = list;
            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return listt.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflter.inflate(R.layout.category_universe_gridview, null); // inflate the layout
            TextView icon = view.findViewById(R.id.textView);
            LinearLayout gridcolor = view.findViewById(R.id.gridcolor);
            icon.setText(listt.get(i).getName());
            if (listt.get(i).getColorFlag().equals("0")) {
                gridcolor.setBackground(getResources().getDrawable(R.drawable.grid_backround_red));
            } else {
                gridcolor.setBackground(getResources().getDrawable(R.drawable.grid_backround));
            }
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Product_ModalSetAdapter.clear();
                    for (Product_Details_Modal personNpi : Product_Modal) {
                        if (personNpi.getProductCatCode().toString().equals(listt.get(i).getId())) {
                            Product_ModalSetAdapter.add(personNpi);
                        }
                    }
                    lin_gridcategory.setVisibility(View.GONE);
                    lin_orderrecyclerview.setVisibility(View.VISIBLE);
                    totalorderbottom.setVisibility(View.GONE);
                    orderbutton.setVisibility(View.GONE);
                    gobackflag = true;
                    takeorder.setVisibility(View.GONE);
                    Category_Nametext.setVisibility(View.VISIBLE);
                    Category_Nametext.setText(listt.get(i).getName());
                    recyclerView.setAdapter(new Prodct_Adapter(Product_ModalSetAdapter, R.layout.product_order_recyclerview, getApplicationContext(), i));
                    new Prodct_Adapter(Product_ModalSetAdapter, R.layout.product_order_recyclerview, getApplicationContext(), i).notifyDataSetChanged();
                    recyclerView.setItemViewCacheSize(Product_ModalSetAdapter.size());
                    gridcolor.setBackground(getResources().getDrawable(R.drawable.grid_backround_red));
                    if (listt.get(i).getColorFlag().equals("0")) {
                    } else {
                        gridcolor.setBackground(getResources().getDrawable(R.drawable.grid_backround));
                    }
                }
            });
            return view;
        }
    }

    public class Prodct_Adapter extends RecyclerView.Adapter<Prodct_Adapter.MyViewHolder> {
        private List<Product_Details_Modal> Product_Details_Modalitem;
        private int rowLayout;
        private Context context;
        AdapterOnClick mAdapterOnClick;
        private int Categorycolor;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView productname, Rate, Amount, Disc, Free, RegularQty;
            EditText Qty;

            public MyViewHolder(View view) {
                super(view);
                productname = view.findViewById(R.id.productname);
                Rate = view.findViewById(R.id.Rate);
                Qty = view.findViewById(R.id.Qty);
                RegularQty = view.findViewById(R.id.RegularQty);
                Amount = view.findViewById(R.id.Amount);
                Disc = view.findViewById(R.id.Disc);
                Free = view.findViewById(R.id.Free);
            }
        }

        public Prodct_Adapter(List<Product_Details_Modal> Product_Details_Modalitem, int rowLayout, Context context, int Categorycolor) {
            this.Product_Details_Modalitem = Product_Details_Modalitem;
            this.rowLayout = rowLayout;
            this.context = context;
            this.Categorycolor = Categorycolor;
        }

        @Override
        public Prodct_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(Prodct_Adapter.MyViewHolder holder, int position) {
            Product_Details_Modal Product_Details_Modal = Product_Details_Modalitem.get(position);
            holder.productname.setText("" + Product_Details_Modal.getName().toUpperCase());
            holder.Rate.setText("Rate:   " + Product_Details_Modal.getRate());
            holder.Amount.setText("" + Product_Details_Modal.getAmount());
            if (common_class.isNullOrEmpty(String.valueOf(Product_Details_Modal.getRegularQty()))) {
                holder.RegularQty.setText("" + 0);
            } else {
                holder.RegularQty.setText("" + Product_Details_Modal.getRegularQty());
            }

            holder.Disc.setText("Disc :" + 0);
            if (Categorycolor == -1) {
                holder.Qty.setEnabled(false);
            } else {
                holder.Qty.setEnabled(true);
            }
            if (Product_Details_Modal.getQty() > 0)
                holder.Qty.setText("" + Product_Details_Modal.getQty());
            holder.Qty.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence charSequence, int start,
                                          int before, int count) {
                    if (!charSequence.toString().equals("")) {
                        if (Double.valueOf(charSequence.toString()) > 0)
                            listt.get(Categorycolor).setColorFlag("1");
                        Product_Details_Modalitem.get(position).setQty(Integer.valueOf(charSequence.toString()));
                        holder.Amount.setText("" + String.valueOf((Double.valueOf(charSequence.toString()) + Product_Details_Modalitem.get(position).getRegularQty()) * Product_Details_Modalitem.get(position).getRate()));
                        Product_Details_Modalitem.get(position).setAmount(((Double.valueOf(charSequence.toString()) + Product_Details_Modalitem.get(position).getRegularQty()) * Product_Details_Modalitem.get(position).getRate()));
                    } else {
                        holder.Amount.setText("" + String.valueOf(Product_Details_Modalitem.get(position).getRegularQty() * Product_Details_Modalitem.get(position).getRate()));
                        Product_Details_Modalitem.get(position).setQty((Integer) 0);
                        Product_Details_Modalitem.get(position).setAmount(Product_Details_Modalitem.get(position).getRegularQty() * Product_Details_Modalitem.get(position).getRate());
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    int showcolor = 0;
                    for (Product_Details_Modal personNpi : Product_Details_Modalitem) {
                        showcolor += personNpi.getQty();
                    }
                    if (showcolor < 1) {
                        listt.get(Categorycolor).setColorFlag("0");
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return Product_Details_Modalitem.size();
        }

    }

    public void Get_regularqty() {
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Map<String, String> QueryString = new HashMap<>();
        QueryString.put("axn", "table/list");
        QueryString.put("divisionCode", Shared_Common_Pref.Div_Code.replace(",", ""));
        QueryString.put("sfCode", Shared_Common_Pref.Sf_Code);
        QueryString.put("OutletCode", Shared_Common_Pref.OutletCode);
        QueryString.put("OrderDate", Common_Class.GetDate());
        QueryString.put("rSF", Shared_Common_Pref.Sf_Code);
        QueryString.put("State_Code", Shared_Common_Pref.StateCode);
        Log.e("GET_REGULAr_MAp", QueryString.toString());
        Call<Object> call = service.GetRouteObject(QueryString, "{\"tableName\":\"getproductregularqty\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}");
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.e("Product_Before_ToString", response.body() + "");
                Log.e("Product_Regular_Qty", response.body().toString() + "");
                System.out.println("Product_Details" + new Gson().toJson(response.body()));
                System.out.println("Product_Details" + new Gson().toJson(response.body()));
                userType = new TypeToken<ArrayList<RegularQty_Modal>>() {
                }.getType();
                RegularQty_Modal = gson.fromJson(new Gson().toJson(response.body()), userType);
                int currentPosition = 0;
                for (Product_Details_Modal PM : Product_Modal) {
                    Product_Modal.get(currentPosition).setRegularQty(0);
                    for (com.hap.checkinproc.SFA_Model_Class.RegularQty_Modal Rm : RegularQty_Modal) {
                        if (PM.getId().equals(Rm.getProductCode())) {
                            Product_Modal.get(currentPosition).setRegularQty(Rm.getQty());
                            Product_Modal.get(currentPosition).setAmount(Double.valueOf(Rm.getQty()) * Product_Modal.get(currentPosition).getRate());
                            System.out.println("Product_Regular_Qty" + Product_Modal.get(currentPosition).getRegularQty());
                        }
                    }
                    currentPosition++;
                }
                Order_Category_Select.CategoryAdapter customAdapteravail = new Order_Category_Select.CategoryAdapter(getApplicationContext(), Category_Modal);
                categorygrid.setAdapter(customAdapteravail);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (gobackflag == false) {
                common_class.CommonIntentwithFinish(Invoice_History.class);
            } else {
                gobackflag = false;
                lin_gridcategory.setVisibility(View.VISIBLE);
                lin_orderrecyclerview.setVisibility(View.GONE);
                totalorderbottom.setVisibility(View.GONE);
                orderbutton.setVisibility(View.GONE);
                takeorder.setVisibility(View.VISIBLE);
                Order_Category_Select.CategoryAdapter customAdapteravaill = new Order_Category_Select.CategoryAdapter(getApplicationContext(), Category_Modal);
                categorygrid.setAdapter(customAdapteravaill);
            }
            return true;
        }
        return false;
    }
}
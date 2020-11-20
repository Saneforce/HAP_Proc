package com.hap.checkinproc.Activity_Hap;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.hap.checkinproc.Common_Class.AlertDialogBox;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AlertBox;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.ParentListInterface;
import com.hap.checkinproc.Model_Class.HeaderCat;
import com.hap.checkinproc.Model_Class.HeaderName;
import com.hap.checkinproc.Model_Class.Product;
import com.hap.checkinproc.Model_Class.Product_Array;
import com.hap.checkinproc.R;
import com.hap.checkinproc.adapters.ParentListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderCategoryActivity extends AppCompatActivity {
    TextView toolHeader, toolTime, toolSlash, toolCutOFF, grandTotal, item_count, txtClosing;
    ImageView imgBack;

    Product_Array product_array;
    ArrayList<String> list;
    RecyclerView mRecyclerView;
    List<String> productHeaderList, productChildList;
    String productID;
    EditText toolSearch;
    Integer productCode;
    ParentListAdapter event_list_parent_adapter;
    ArrayList<Product_Array> Product_Array_List;

    /* Submit button */
    LinearLayout proceedCart;
    int j;
    int sum = 0;

    String time;
    SimpleDateFormat simpleDateFormat;
    Calendar calander;

    ArrayList<String> mResponseProductID;
    JSONObject person1;
    JSONObject PersonObjectArray;
    ArrayList<String> listV = new ArrayList<>();
    String JsonDatas;
    String SF_CODE, DIVISION_CODE, CUTT_OFF_CODE;

    String seachName;

    List<HeaderName> headerNameArrayList;
    List<Product> eventsArrayList;
    Timestamp timestamp, ts2;
    int b3;
    /*CHECKING*/
    ArrayList<String> mHeaderNameValue;
    LinearLayout bottomLinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_category);
        getToolbar();
        @SuppressLint("WrongConstant")
        SharedPreferences sh
                = getSharedPreferences("MyPrefs",
                MODE_APPEND);
        SF_CODE = Shared_Common_Pref.Sf_Code;
        DIVISION_CODE = Shared_Common_Pref.Div_Code;
        CUTT_OFF_CODE = Shared_Common_Pref.StateCode;

        Log.e("CAT_Details", SF_CODE);
        Log.e("CAT_Details", DIVISION_CODE);
        Log.e("CAT_Details", CUTT_OFF_CODE);

        SubCategoryHeader();
        currentTime();
        checking();
        /* 2020-4-9 00:00:00*/
        Product_Array_List = new ArrayList<Product_Array>();
        Product_Array_List.clear();

        list = new ArrayList<>();
        productHeaderList = new ArrayList<>();
        productChildList = new ArrayList<>();
        mResponseProductID = new ArrayList<>();

        mHeaderNameValue = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        grandTotal = (TextView) findViewById(R.id.total_amount);
        item_count = (TextView) findViewById(R.id.item_count);
        mRecyclerView.setNestedScrollingEnabled(false);
        proceedCart = (LinearLayout) findViewById(R.id.Linear_proceed_cart);



        ImageView backView = findViewById(R.id.imag_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBackPressedDispatcher.onBackPressed();
            }
        });
        bottomLinear = (LinearLayout) findViewById(R.id.bottom_linear);
        proceedCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String STR = grandTotal.getText().toString();
                Log.e("STRVALUE", STR);
                if (STR.equals("0")) {
                    Toast.makeText(OrderCategoryActivity.this, "Please choose to cart", Toast.LENGTH_SHORT).show();
                } else {
                    SaveDataValue();
                }

            }
        });


    }

    /*Toolbar*/
    public void getToolbar() {

        toolHeader = (TextView) findViewById(R.id.toolbar_title);
        toolHeader.setText(R.string.pri_orders);
        toolTime = (TextView) findViewById(R.id.current_time);
        toolSlash = (TextView) findViewById(R.id.slash);
        toolSlash.setText("/");
        toolCutOFF = (TextView) findViewById(R.id.cut_off_time);
        txtClosing = (TextView) findViewById(R.id.text_closing);
        txtClosing.setText("Order Closing Time");
        txtClosing.setVisibility(View.VISIBLE);
        toolSearch = (EditText) findViewById(R.id.toolbar_search);
        toolSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                event_list_parent_adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    public void checking() {
        timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println("format" + timestamp);

        ts2 = Timestamp.valueOf("2020-11-03 15:56:00");
        //compares ts1 with ts2
        b3 = timestamp.compareTo(ts2);
        if (b3 == 0) {
            // bottomLinear.setVisibility(View.GONE);
            //  startActivity(new Intent(CategoryActivity.this, TimeOut.class));
        } else if (b3 > 0) {
            //   bottomLinear.setVisibility(View.GONE);
            //  startActivity(new Intent(CategoryActivity.this, TimeOut.class));
            System.out.println("TimeSpan1 value is greater");
        } else {
            //  bottomLinear.setVisibility(View.VISIBLE);
            System.out.println("TimeSpan2 value is greater");
        }

    }


    /*Current Time for Cuttoff Process*/
    public void currentTime() {

        String setTIME = "15:56:00";
        toolCutOFF.setText(setTIME);
        calander = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
        time = simpleDateFormat.format(calander.getTime());
        final Handler someHandler = new Handler(getMainLooper());
        someHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toolTime.setText(new SimpleDateFormat("HH:mm:ss", Locale.US).format(new Date()));
                if (toolTime.getText().equals(setTIME)) {
                    // Toast.makeText(getApplicationContext(), "Your data has been submitted successfully", Toast.LENGTH_LONG).show();
                    //    bottomLinear.setVisibility(View.GONE);
                    //   startActivity(new Intent(CategoryActivity.this, TimeOut.class));
                } else {

                }
                someHandler.postDelayed(this, 1000);
            }
        }, 10);
    }

    /*subCategory header Listview*/
    public void SubCategoryHeader() {
        Log.e("CAT_Details", SF_CODE);
        Log.e("CAT_Details", DIVISION_CODE);
        Log.e("CAT_Details", Shared_Common_Pref.StateCode);

        String tempalteValue = "{\"tableName\":\"category_master\",\"coloumns\":\"[\\\"Category_Code as id\\\", \\\"Category_Name as name\\\"]\",\"sfCode\":0,\"orderBy\":\"[\\\"name asc\\\"]\",\"desig\":\"mgr\"}";
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<HeaderCat> ca = apiInterface.SubCategory(DIVISION_CODE, SF_CODE, SF_CODE, Shared_Common_Pref.StateCode, tempalteValue);
        ca.enqueue(new Callback<HeaderCat>() {
            @Override
            public void onResponse(Call<HeaderCat> call, Response<HeaderCat> response) {
                if (response.isSuccessful()) {

                    HeaderCat headerCat = response.body();
                    Log.e("RESPOSNE", headerCat.toString());
                    headerNameArrayList = headerCat.getData();
                    HeaderName mHeaderName = new HeaderName();
                    for (int i = 0; i < headerNameArrayList.size(); i++) {
                        String str = headerNameArrayList.get(i).getName();
                        mHeaderName.setName(str);
                        mHeaderNameValue.add(str);
                        Log.e("HEADER_NAME", String.valueOf(mHeaderNameValue));
                        eventsArrayList = headerNameArrayList.get(i).getProduct();
                        childListData(eventsArrayList, headerCat, headerNameArrayList);
                    }
                }
            }

            @Override
            public void onFailure(Call<HeaderCat> call, Throwable t) {
                Log.e("RESPOSNE", "response.body().toString()");
            }
        });
    }


    // ChildListAdapter
    private void childListData(List<Product> eventsArrayLists, HeaderCat headerCat, List<HeaderName> headerNameArrayLists) {


        for (j = 0; j < eventsArrayLists.size(); j++) {
            productID = eventsArrayLists.get(j).getId();
            productCode = eventsArrayLists.get(j).getProductCatCode();
            mResponseProductID.add(String.valueOf(productID));
            seachName = eventsArrayLists.get(j).getName();
            mHeaderNameValue.add(seachName);
            event_list_parent_adapter = new ParentListAdapter(headerCat, headerNameArrayLists, eventsArrayLists, OrderCategoryActivity.this, mHeaderNameValue, new ParentListInterface() {
                @Override
                public void onClickParentInter(String value, int totalValue, String itemID, Integer positionValue, String productName, String productCode, Integer productQuantiy, String catImage, String catName) {

                    Log.e("Product_code", productCode);

                    if (Product_Array_List.size() == 0) {
                        sum = sum + productQuantiy * Integer.parseInt(productCode);
                        grandTotal.setText("" + sum);
                        item_count.setText("Items:" + "1");
                        Product_Array_List.add(new Product_Array(itemID, productName, productQuantiy, productQuantiy * Integer.parseInt(productCode), Integer.parseInt(productCode), catImage, catName));
                        System.out.println("First_Product_Added" + Product_Array_List.size());


                    } else {
                        System.out.println("PRODUCT_Array_SIzeElse" + Product_Array_List.size());
                        int Total_Size = Product_Array_List.size();
                        for (int i = 0; i < Total_Size; i++) {
                            product_array = Product_Array_List.get(i);
                            if (itemID == product_array.getProductcode()) {
                                System.out.println("Product_Code" + itemID);
                                System.out.println("Existing_Code" + product_array.getProductcode());
                                System.out.println("Position_Count" + i);
                                Product_Array_List.remove(i);
                                System.out.println("PRODUCT_Array_SIZE" + Product_Array_List.size());
                                Total_Size = Total_Size - 1;
                                System.out.println("AlreadyExist" + product_array.getProductcode());
                            }

                        }

                        Product_Array_List.add(new Product_Array(itemID, productName, productQuantiy, productQuantiy * Integer.parseInt(productCode), Integer.parseInt(productCode), catImage, catName));
                        int sum = 0;

                        Log.e("PRODUCT_ARRAY_SIZE", String.valueOf(Product_Array_List));
                        for (int i = 0; i < Product_Array_List.size(); i++) {
                            sum = sum + Product_Array_List.get(i).getSampleqty();
                            System.out.println("Final_Name" + "  " + Product_Array_List.get(i).getProductname() + "Qty" + "  " + Product_Array_List.get(i).getProductqty() + "SampleQty" + "  " + Product_Array_List.get(i).getSampleqty());
                            if (Product_Array_List.get(i).getProductqty() == 0) {

                                Product_Array_List.remove(i);
                            }

                        }
                        grandTotal.setText("" + sum);

                        item_count.setText("Items:" + Product_Array_List.size());
                        Log.e("DATA_CHECKING", String.valueOf(Product_Array_List.size()));
                    }



                    /*Product_code*/
                    List<JSONObject> myJSONObjects = new ArrayList<JSONObject>(Product_Array_List.size());


                    JSONArray personarray = new JSONArray();
                    PersonObjectArray = new JSONObject();
                    JSONObject fkeyprodcut = new JSONObject();

                    for (int z = 0; z < Product_Array_List.size(); z++) {
                        person1 = new JSONObject();


                        try {

                            //adding items to first json object
                            person1.put("product_Name", Product_Array_List.get(z).getProductname());
                            person1.put("product_code", Product_Array_List.get(z).getProductcode());
                            person1.put("Qty", Product_Array_List.get(z).getProductqty());
                            person1.put("PQty", Product_Array_List.get(z).getProductRate());
                            person1.put("cb_qty", 0);
                            person1.put("free", 0);
                            person1.put("f_key", fkeyprodcut);
                            fkeyprodcut.put("activity_stockist_code", "Activity_Stockist_Report");

                            myJSONObjects.add(person1);
                            listV.add(String.valueOf((person1)));
                            personarray.put(person1);
                            PersonObjectArray.put("Activity_Stk_POB_Report", personarray);
                            String JsonData = PersonObjectArray.toString();

                            System.out.println("Activity_Stk_POB_Report: " + JsonData);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    /*Sending to view cart*/
                    ArrayList<JSONObject> reportJSON = new ArrayList<>();
                    JSONObject ReportObjectArray = new JSONObject();
                    JSONArray jsonElements = new JSONArray();
                    JSONObject js;

                    for (int z = 0; z < Product_Array_List.size(); z++) {

                        if (Product_Array_List.get(z).getProductqty() != 0) {
                            js = new JSONObject();

                            try {
                                js.put("product_Name", Product_Array_List.get(z).getProductname());
                                js.put("product_code", Product_Array_List.get(z).getProductcode());
                                js.put("Qty", Product_Array_List.get(z).getProductqty());
                                js.put("Samp", Product_Array_List.get(z).getProductRate());
                                js.put("Cat_Image", Product_Array_List.get(z).getCatImage());
                                js.put("Cat_Name", Product_Array_List.get(z).getCatName());
                                reportJSON.add(js);
                                jsonElements.put(js);
                                ReportObjectArray.put("Activity_Stk_POB_Report", jsonElements);
                                JsonDatas = ReportObjectArray.toString();
                                System.out.println("Activity_Stk_POB_Report:sssssssssss " + JsonDatas);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                }

            });

            mRecyclerView.setAdapter(event_list_parent_adapter);

        }


    }


    /*SAVE DATA FROM */

    public void SaveDataValue() {

        Gson gson = new Gson();

        String jsonCars = gson.toJson(Product_Array_List);

        Intent mIntent = new Intent(OrderCategoryActivity.this, ViewCartActivity.class);
        mIntent.putExtra("list_as_string", jsonCars);
        startActivity(mIntent);

    }


    private final OnBackPressedDispatcher mOnBackPressedDispatcher =
            new OnBackPressedDispatcher(new Runnable() {
                @Override
                public void run() {
                    AlertDialogBox.showDialog(OrderCategoryActivity.this, "", "Do you want to exit?", "Yes", "NO", false, new AlertBox() {
                        @Override
                        public void PositiveMethod(DialogInterface dialog, int id) {
                            onSuperBackPressed();
                        }

                        @Override
                        public void NegativeMethod(DialogInterface dialog, int id) {
                        }
                    });
                }
            });

    public void onSuperBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void onBackPressed() {

    }

}
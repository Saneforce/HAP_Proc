package com.hap.checkinproc.SFA_Activity;

import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.hap.checkinproc.Activity_Hap.AllowancCapture;
import com.hap.checkinproc.BuildConfig;
import com.hap.checkinproc.Common_Class.AlertDialogBox;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AlertBox;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.LocationEvents;
import com.hap.checkinproc.Interface.OnImagePickListener;
import com.hap.checkinproc.Interface.UpdateResponseUI;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.FilesAdapter;
import com.hap.checkinproc.SFA_Adapter.Print_Invoice_Adapter;
import com.hap.checkinproc.SFA_Adapter.QPS_Modal;
import com.hap.checkinproc.SFA_Model_Class.Product_Details_Modal;
import com.hap.checkinproc.common.FileUploadService;
import com.hap.checkinproc.common.LocationFinder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Print_Invoice_Activity extends AppCompatActivity implements View.OnClickListener, UpdateResponseUI {
    Print_Invoice_Adapter mReportViewAdapter;
    RecyclerView printrecyclerview, rvReturnInv;
    Shared_Common_Pref sharedCommonPref;
    Common_Class common_class;
    List<Product_Details_Modal> Order_Outlet_Filter;
    TextView netamount, returnNetAmt, cashdiscount, gstLabel, gstrate, totalfreeqty, totalqty, totalitem, subtotal, invoicedate, retaileAddress, billnumber, retailername,
            retailerroute, back, tvOrderType, tvRetailorPhone, tvDistributorPh, tvDistributorName, tvOutstanding, tvPaidAmt, tvHeader, tvDistId,
            tvDistAdd, returngstLabel, returngstrate, returntotalqty, returntotalitem, returnsubtotal;
    ImageView ok, ivPrint;
    public static Print_Invoice_Activity mPrint_invoice_activity;
    CircularProgressButton btnInvoice;
    NumberFormat formatter = new DecimalFormat("##0.00");
    private String label, amt, storeName = "", address = "", phone = "";
    private ArrayList<Product_Details_Modal> taxList;
    private ArrayList<Product_Details_Modal> uomList;

    double cashDisc, subTotalVal, outstandAmt;
    LinearLayout llDistCal, llRetailCal;
    String[] strLoc;
    final Handler handler = new Handler();
    public String TAG = "Print_Invoice_Activity";
    ImageView ivStockCapture;
    RecyclerView rvStockCapture;
    List<QPS_Modal> stockFileList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            mPrint_invoice_activity = this;
            setContentView(R.layout.activity_print__invoice_);
            ButterKnife.inject(this);
            printrecyclerview = findViewById(R.id.printrecyclerview);
            rvReturnInv = findViewById(R.id.rvReturnInv);

            sharedCommonPref = new Shared_Common_Pref(Print_Invoice_Activity.this);
            common_class = new Common_Class(this);
            netamount = findViewById(R.id.netamount);
            back = findViewById(R.id.back);
            cashdiscount = findViewById(R.id.cashdiscount);
            billnumber = findViewById(R.id.billnumber);
            totalfreeqty = findViewById(R.id.totalfreeqty);
            totalqty = findViewById(R.id.totalqty);
            totalitem = findViewById(R.id.totalitem);
            subtotal = findViewById(R.id.subtotal);
            invoicedate = findViewById(R.id.invoicedate);
            retaileAddress = findViewById(R.id.retaileAddress);
            retailername = findViewById(R.id.retailername);
            retailerroute = findViewById(R.id.retailerroute);
            ok = findViewById(R.id.ok);
            btnInvoice = findViewById(R.id.btnInvoice);
            tvDistributorName = findViewById(R.id.distributor_Name);
            tvDistributorPh = findViewById(R.id.distPhoneNum);
            tvOrderType = findViewById(R.id.tvTypeLabel);
            tvRetailorPhone = findViewById(R.id.retailePhoneNum);
            tvOutstanding = findViewById(R.id.tvOutstanding);
            tvPaidAmt = findViewById(R.id.tvPaidAmt);
            tvHeader = findViewById(R.id.tvHeader);
            gstLabel = findViewById(R.id.gstLabel);
            gstrate = findViewById(R.id.gstrate);
            tvDistAdd = findViewById(R.id.tvAdd);
            tvDistId = findViewById(R.id.tvDistId);
            llDistCal = findViewById(R.id.llDistCall);
            llRetailCal = findViewById(R.id.llRetailCal);
            returntotalqty = findViewById(R.id.returnTotalqty);
            returntotalitem = findViewById(R.id.returnTotalitem);
            returnsubtotal = findViewById(R.id.returnSubtotal);
            returngstLabel = findViewById(R.id.returnInvTax);
            returngstrate = findViewById(R.id.returnGstrate);
            returnNetAmt = findViewById(R.id.tvReturnAmt);
            ivStockCapture = findViewById(R.id.ivStockCapture);
            rvStockCapture = findViewById(R.id.rvStockFiles);


            retailername.setText(sharedCommonPref.getvalue(Constants.Retailor_Name_ERP_Code));
            tvDistributorName.setText(sharedCommonPref.getvalue(Constants.Distributor_name));
            ivPrint = findViewById(R.id.ivPrint);

            back.setOnClickListener(this);
            ok.setOnClickListener(this);
            ivPrint.setOnClickListener(this);
            btnInvoice.setOnClickListener(this);
            llDistCal.setOnClickListener(this);
            llRetailCal.setOnClickListener(this);
            ivStockCapture.setOnClickListener(this);


            ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
            common_class.gotoHomeScreen(this, ivToolbarHome);

            if (sharedCommonPref.getvalue(Constants.FLAG).equalsIgnoreCase("Sales return"))
                tvHeader.setText(sharedCommonPref.getvalue(Constants.FLAG));
            else
                tvHeader.setText("Sales " + sharedCommonPref.getvalue(Constants.FLAG));

            tvDistributorPh.setText(sharedCommonPref.getvalue(Constants.Distributor_phone));
            tvRetailorPhone.setText(sharedCommonPref.getvalue(Constants.Retailor_PHNo));

            if (Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.Distributor_phone)))
                llDistCal.setVisibility(View.GONE);
            if (Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.Retailor_PHNo)))
                llRetailCal.setVisibility(View.GONE);
            retailerroute.setText(sharedCommonPref.getvalue(Constants.Route_name));
            retaileAddress.setText(sharedCommonPref.getvalue(Constants.Retailor_Address));
            invoicedate.setText(getIntent().getStringExtra("Invoice_Date"));
            tvDistId.setText(sharedCommonPref.getvalue(Constants.DistributorERP));
            tvDistAdd.setText(sharedCommonPref.getvalue(Constants.DistributorAdd));

            if (sharedCommonPref.getvalue(Constants.FLAG).equals("ORDER")) {
                findViewById(R.id.llCreateInvoice).setVisibility(View.VISIBLE);
                common_class.getDataFromApi(Constants.TodayOrderDetails_List, this, false);
                common_class.getDb_310Data(Constants.OUTSTANDING, this);
                storeName = retailername.getText().toString();
                address = retaileAddress.getText().toString();
                phone = "Mobile:" + tvRetailorPhone.getText().toString();

            } else if (sharedCommonPref.getvalue(Constants.FLAG).equals("Primary Order") ||
                    sharedCommonPref.getvalue(Constants.FLAG).equals("POS INVOICE") || (sharedCommonPref.getvalue(Constants.FLAG).equals("PROJECTION"))) {
                findViewById(R.id.llCreateInvoice).setVisibility(View.GONE);
                findViewById(R.id.llOutletParent).setVisibility(View.GONE);
                tvDistAdd.setVisibility(View.VISIBLE);
                //  tvDistId.setVisibility(View.VISIBLE);
                if (sharedCommonPref.getvalue(Constants.FLAG).equals("Primary Order"))
                    common_class.getDataFromApi(Constants.TodayPrimaryOrderDetails_List, this, false);
                else if (sharedCommonPref.getvalue(Constants.FLAG).equals("PROJECTION")) {
                    common_class.getDataFromApi(Constants.ProjectionOrderDetails_List, this, false);
                    findViewById(R.id.llUOM).setVisibility(View.GONE);
                    findViewById(R.id.llPrice).setVisibility(View.GONE);
                    findViewById(R.id.llTot).setVisibility(View.GONE);
                    findViewById(R.id.rlSubTot).setVisibility(View.GONE);
                    findViewById(R.id.rlNetAmt).setVisibility(View.GONE);
                    totalitem.setTypeface(Typeface.DEFAULT_BOLD);
                    totalqty.setTypeface(Typeface.DEFAULT_BOLD);

                    tvDistAdd.setVisibility(View.GONE);
                    tvDistributorPh.setVisibility(View.GONE);
                    findViewById(R.id.llDistCall).setVisibility(View.GONE);

                } else {
                    findViewById(R.id.tvWelcomeLabel).setVisibility(View.VISIBLE);
                    common_class.getDataFromApi(Constants.PosOrderDetails_List, this, false);
                }

                findViewById(R.id.llDelivery).setVisibility(View.GONE);
                storeName = tvDistributorName.getText().toString();
                address = tvDistAdd.getText().toString();
                phone = "Mobile:" + tvDistributorPh.getText().toString();

            } else {
                findViewById(R.id.llCreateInvoice).setVisibility(View.GONE);
                storeName = retailername.getText().toString();
                address = retaileAddress.getText().toString();
                phone = "Mobile:" + tvRetailorPhone.getText().toString();

                if (sharedCommonPref.getvalue(Constants.FLAG).equalsIgnoreCase("Return Invoice")) {
                    findViewById(R.id.cvReturnInvParent).setVisibility(View.VISIBLE);
                    findViewById(R.id.cvSalesParent).setVisibility(View.GONE);
                    findViewById(R.id.llCreateInvoice).setVisibility(View.VISIBLE);
                    btnInvoice.setText("CONFIRM");
                    orderInvoiceDetailData(sharedCommonPref.getvalue(Constants.SALES_RETURN));

                } else {
                    common_class.getDataFromApi(Constants.TodayOrderDetails_List, this, false);
                    if (sharedCommonPref.getvalue(Constants.FLAG).equalsIgnoreCase("INVOICE")) {
                        findViewById(R.id.cvPayDetails).setVisibility(View.VISIBLE);

                        common_class.getDb_310Data(Constants.OUTSTANDING, this);
                    }
                }

            }

            tvOrderType.setText(sharedCommonPref.getvalue(Constants.FLAG));
            cashDisc = Double.parseDouble(getIntent().getStringExtra("Discount_Amount"));
            stockFileList.add(new QPS_Modal("", "", ""));//purity


        } catch (Exception e) {
            Log.v(TAG, e.getMessage());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (sharedCommonPref.getvalue(Constants.FLAG).equals("POS INVOICE"))
                common_class.CommonIntentwithFinish(PosHistoryActivity.class);
            else
                finish();
        }
        return false;
    }


    public void ResetSubmitBtn(int resetMode) {
        common_class.ProgressdialogShow(0, "");
        long dely = 10;
        if (resetMode != 0) dely = 1000;
        if (resetMode == 1) {
            btnInvoice.doneLoadingAnimation(getResources().getColor(R.color.green), BitmapFactory.decodeResource(getResources(), R.drawable.done));
        } else {
            btnInvoice.doneLoadingAnimation(getResources().getColor(R.color.color_red), BitmapFactory.decodeResource(getResources(), R.drawable.ic_wrong));
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                btnInvoice.stopAnimation();
                btnInvoice.revertAnimation();
            }
        }, dely);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ivStockCapture:
                if (stockFileList.get(0).getFileUrls() == null || stockFileList.get(0).getFileUrls().size() < sharedCommonPref.getIntValue(Constants.SALES_RETURN_FILECOUNT)) {
                    AllowancCapture.setOnImagePickListener(new OnImagePickListener() {
                        @Override
                        public void OnImageURIPick(Bitmap image, String FileName, String fullPath) {

                            List<String> list = new ArrayList<>();
                            File file = new File(fullPath);
                            Uri contentUri = Uri.fromFile(file);

                            if (stockFileList.get(0).getFileUrls() != null && stockFileList.get(0).getFileUrls().size() > 0)
                                list = (stockFileList.get(0).getFileUrls());
                            list.add(contentUri.toString());
                            stockFileList.get(0).setFileUrls(list);

                            rvStockCapture.setAdapter(new FilesAdapter(stockFileList.get(0).getFileUrls(), R.layout.adapter_local_files_layout, Print_Invoice_Activity.this));
                        }
                    });
                    Intent intent = new Intent(Print_Invoice_Activity.this, AllowancCapture.class);
                    intent.putExtra("allowance", "TAClaim");
                    startActivity(intent);
                } else {
                    common_class.showMsg(Print_Invoice_Activity.this, "Limit Exceed...");
                }

                break;
            case R.id.back:
                if (sharedCommonPref.getvalue(Constants.FLAG).equals("POS INVOICE"))
                    common_class.CommonIntentwithFinish(PosHistoryActivity.class);
                else
                    finish();
                break;
            case R.id.ok:
                createPdf();
                break;
            case R.id.ivPrint:
                showPrinterList();
                break;

            case R.id.btnInvoice:
                if (sharedCommonPref.getvalue(Constants.FLAG).equalsIgnoreCase("Return Invoice")) {
                    if (stockFileList.get(0).getFileUrls() == null || stockFileList.get(0).getFileUrls().size() == 0) {
                        common_class.showMsg(this, "Please take picture");
                        return;

                    }
                    if (btnInvoice.isAnimating()) return;
                    btnInvoice.startAnimation();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            String sLoc = sharedCommonPref.getvalue("CurrLoc");
                            if (sLoc.equalsIgnoreCase("")) {
                                new LocationFinder(getApplication(), new LocationEvents() {
                                    @Override
                                    public void OnLocationRecived(Location location) {
                                        strLoc = (location.getLatitude() + ":" + location.getLongitude()).split(":");
                                        confirmReturnInv();
                                    }
                                });
                            } else {
                                strLoc = sLoc.split(":");
                                confirmReturnInv();
                            }
                        }
                    }, 500);
                } else {
                    Shared_Common_Pref.Invoicetoorder = "4";
                    common_class.CommonIntentwithFinish(Invoice_Category_Select.class);
                }
                break;

            case R.id.llDistCall:
                common_class.showCalDialog(this, "Do you want to Call this Distributor?", sharedCommonPref.getvalue(Constants.Distributor_phone));
                break;
            case R.id.llRetailCal:
                common_class.showCalDialog(this, "Do you want to Call this Outlet?", sharedCommonPref.getvalue(Constants.Retailor_PHNo));
                break;
        }
    }

    private void confirmReturnInv() {
        if (common_class.isNetworkAvailable(this)) {
            AlertDialogBox.showDialog(Print_Invoice_Activity.this, "HAP SFA", "Are You Sure Want to Submit?", "OK", "Cancel", false, new AlertBox() {
                @Override
                public void PositiveMethod(DialogInterface dialog, int id) {
                    try {


                        common_class.ProgressdialogShow(1, "");
                        JSONArray data = new JSONArray();
                        JSONObject ActivityData = new JSONObject();

                        JSONObject HeadItem = new JSONObject();
                        HeadItem.put("SF", Shared_Common_Pref.Sf_Code);
                        HeadItem.put("Worktype_code", "");
                        HeadItem.put("Town_code", sharedCommonPref.getvalue(Constants.Route_Id));
                        HeadItem.put("dcr_activity_date", Common_Class.GetDate());
                        HeadItem.put("Daywise_Remarks", "");
                        HeadItem.put("UKey", Common_Class.GetEkey());
                        HeadItem.put("orderValue", formatter.format(subTotalVal));
                        HeadItem.put("DataSF", Shared_Common_Pref.Sf_Code);
                        HeadItem.put("AppVer", BuildConfig.VERSION_NAME);
                        ActivityData.put("Activity_Report_Head", HeadItem);

                        JSONObject OutletItem = new JSONObject();
                        OutletItem.put("Doc_Meet_Time", Common_Class.GetDate());
                        OutletItem.put("modified_time", Common_Class.GetDate());
                        OutletItem.put("stockist_code", sharedCommonPref.getvalue(Constants.Distributor_Id));
                        OutletItem.put("stockist_name", sharedCommonPref.getvalue(Constants.Distributor_name));
                        OutletItem.put("orderValue", formatter.format(subTotalVal));
                        OutletItem.put("CashDiscount", cashDisc);
                        OutletItem.put("NetAmount", formatter.format(subTotalVal));
                        OutletItem.put("No_Of_items", returntotalitem.getText().toString());
                        OutletItem.put("Invoice_Flag", Shared_Common_Pref.Invoicetoorder);
                        OutletItem.put("TransSlNo", "");
                        OutletItem.put("doctor_code", Shared_Common_Pref.OutletCode);
                        OutletItem.put("doctor_name", Shared_Common_Pref.OutletName);
                        OutletItem.put("ordertype", "Return Invoice");
                        OutletItem.put("from", sharedCommonPref.getvalue(Constants.Distributor_Id));
                        OutletItem.put("to", Shared_Common_Pref.CUSTOMER_CODE);
                        OutletItem.put("distCode", Shared_Common_Pref.CUSTOMER_CODE);
                        OutletItem.put("customerCode", sharedCommonPref.getvalue(Constants.Distributor_Id));

                        if (strLoc.length > 0) {
                            OutletItem.put("Lat", strLoc[0]);
                            OutletItem.put("Long", strLoc[1]);
                        } else {
                            OutletItem.put("Lat", "");
                            OutletItem.put("Long", "");
                        }
                        JSONArray Order_Details = new JSONArray();
                        JSONArray totTaxArr = new JSONArray();

                        for (int z = 0; z < Order_Outlet_Filter.size(); z++) {
                            JSONObject ProdItem = new JSONObject();
                            ProdItem.put("product_Name", Order_Outlet_Filter.get(z).getName());
                            ProdItem.put("product_code", Order_Outlet_Filter.get(z).getId());
                            ProdItem.put("Product_Qty", Order_Outlet_Filter.get(z).getQty());
                            ProdItem.put("Product_RegularQty", Order_Outlet_Filter.get(z).getRegularQty());
                            ProdItem.put("Product_Total_Qty", Order_Outlet_Filter.get(z).getQty());
                            ProdItem.put("Product_Amount", Order_Outlet_Filter.get(z).getAmount());
                            ProdItem.put("Rate", String.format("%.2f", Order_Outlet_Filter.get(z).getRate()));

                            ProdItem.put("free", Order_Outlet_Filter.get(z).getFree());
                            ProdItem.put("dis", Order_Outlet_Filter.get(z).getDiscount());
                            ProdItem.put("dis_value", Order_Outlet_Filter.get(z).getDiscount_value());
                            ProdItem.put("Off_Pro_code", Order_Outlet_Filter.get(z).getOff_Pro_code());
                            ProdItem.put("Off_Pro_name", Order_Outlet_Filter.get(z).getOff_Pro_name());
                            ProdItem.put("Off_Pro_Unit", Order_Outlet_Filter.get(z).getOff_Pro_Unit());
                            ProdItem.put("Off_Scheme_Unit", Order_Outlet_Filter.get(z).getScheme());
                            ProdItem.put("discount_type", Order_Outlet_Filter.get(z).getDiscount_type());

                            JSONArray tax_Details = new JSONArray();


                            if (Order_Outlet_Filter.get(z).getProductDetailsModal() != null &&
                                    Order_Outlet_Filter.get(z).getProductDetailsModal().size() > 0) {

                                for (int i = 0; i < Order_Outlet_Filter.get(z).getProductDetailsModal().size(); i++) {
                                    JSONObject taxData = new JSONObject();

                                    String label = Order_Outlet_Filter.get(z).getProductDetailsModal().get(i).getTax_Type();
                                    Double amt = Order_Outlet_Filter.get(z).getProductDetailsModal().get(i).getTax_Amt();
                                    taxData.put("Tax_Id", Order_Outlet_Filter.get(z).getProductDetailsModal().get(i).getTax_Id());
                                    taxData.put("Tax_Val", Order_Outlet_Filter.get(z).getProductDetailsModal().get(i).getTax_Val());
                                    taxData.put("Tax_Type", label);
                                    taxData.put("Tax_Amt", formatter.format(amt));
                                    tax_Details.put(taxData);


                                }


                            }

                            ProdItem.put("TAX_details", tax_Details);

                            Order_Details.put(ProdItem);

                        }

                        for (int i = 0; i < taxList.size(); i++) {
                            JSONObject totTaxObj = new JSONObject();

                            totTaxObj.put("Tax_Type", taxList.get(i).getTax_Type());
                            totTaxObj.put("Tax_Amt", formatter.format(taxList.get(i).getTax_Amt()));
                            totTaxArr.put(totTaxObj);

                        }

                        OutletItem.put("TOT_TAX_details", totTaxArr);
                        ActivityData.put("Activity_Doctor_Report", OutletItem);
                        ActivityData.put("Order_Details", Order_Details);

                        JSONArray file_Details = new JSONArray();


                        for (int f = 0; f < stockFileList.get(0).getFileUrls().size(); f++) {
                            JSONObject ProdItem = new JSONObject();
                            File file = new File(stockFileList.get(0).getFileUrls().get(f));
                            ProdItem.put("SalesReturnImg", file.getName());
                            file_Details.put(ProdItem);
                        }


                        ActivityData.put("file_Details", file_Details);

                        data.put(ActivityData);

                        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                        Call<JsonObject> responseBodyCall = apiInterface.saveSalesReturn(Shared_Common_Pref.Div_Code, Shared_Common_Pref.Sf_Code, data.toString());
                        responseBodyCall.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                if (response.isSuccessful()) {
                                    try {
                                        common_class.ProgressdialogShow(0, "");
                                        Log.e("JSON_VALUES", response.body().toString());
                                        JSONObject jsonObjects = new JSONObject(response.body().toString());
                                        String san = jsonObjects.getString("success");
                                        Log.e("Success_Message", san);
                                        ResetSubmitBtn(1);
                                        if (san.equals("true")) {
                                            sharedCommonPref.clear_pref(Constants.LOC_INDENT_DATA);
                                            common_class.CommonIntentwithFinish(Invoice_History.class);
                                        }
                                        common_class.showMsg(Print_Invoice_Activity.this, jsonObjects.getString("Msg"));

                                    } catch (Exception e) {
                                        common_class.ProgressdialogShow(0, "");
                                        ResetSubmitBtn(2);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                common_class.ProgressdialogShow(0, "");
                                Log.e("SUBMIT_VALUE", "ERROR");
                                ResetSubmitBtn(2);
                            }
                        });


                        uploadStockFile();

                    } catch (Exception e) {
                        e.printStackTrace();
                        ResetSubmitBtn(2);
                    }
                }

                @Override
                public void NegativeMethod(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    ResetSubmitBtn(0);
                }
            });

        } else {
            Toast.makeText(this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
            ResetSubmitBtn(0);
        }
    }

    private void uploadStockFile() {
        for (int f = 0; f < stockFileList.get(0).getFileUrls().size(); f++) {
            String filePath = stockFileList.get(0).getFileUrls().get(f).replaceAll("file:/", "");
            File file = new File(filePath);
            Intent mIntent = new Intent(this, FileUploadService.class);
            mIntent.putExtra("mFilePath", filePath);
            mIntent.putExtra("SF", Shared_Common_Pref.Sf_Code);
            mIntent.putExtra("FileName", file.getName());
            mIntent.putExtra("Mode", "SalesReturnImg");
            FileUploadService.enqueueWork(this, mIntent);
        }


    }

    public void printBill() {
        try {
            Bitmap logo = Printama.getBitmapFromVector(this, R.drawable.hap_logo);
            Printama.with(this).connect(printama -> {

                printama.printImage(Printama.RIGHT, logo, 170);
                printama.addNewLine();
                printama.addNewLine();


                printama.setWideTallBold();
                printama.setTallBold();
                printama.printTextln(Printama.CENTER, storeName);
                printama.addNewLine();
                printama.setBold();
                if (tvDistAdd.getVisibility() == View.VISIBLE) {
                    printama.printTextln(Printama.LEFT, "Address :");
                    printama.setNormalText();
                    printama.addNewLine();

                    printama.printTextln(Printama.LEFT, address);
                }
                if (tvDistributorPh.getVisibility() == View.VISIBLE)
                    printama.printTextln(Printama.LEFT, phone);
                printama.addNewLine(2);
                printama.setBold();
                printama.printText(billnumber.getText().toString() + "          " + invoicedate.getText().toString());
                printama.addNewLine();
                printama.printLine();
                printama.addNewLine(2);
                printama.setBold();
                printama.printTextln("Item       " + "     Qty" + "       Price" + "      Total");
                printama.printLine();
                printama.addNewLine();
                printama.addNewLine();


                for (int i = 0; i < Order_Outlet_Filter.size(); i++) {
                    printama.setBold();
                    printama.printTextln(Order_Outlet_Filter.get(i).getName().toString().trim());
                    printama.addNewLine();

                    printama.setNormalText();

                    String name = Order_Outlet_Filter.get(i).getId() + "           ";
                    name = name.substring(0, name.length() - String.valueOf(Order_Outlet_Filter.get(i).getId()).length());


                    String qtyValue = String.valueOf(Order_Outlet_Filter.get(i).getQty());
                    String qty = "        " + qtyValue;
                    qty = qty.substring(qtyValue.length(), qty.length());

                    String rateValue = String.valueOf(formatter.format(Order_Outlet_Filter.get(i).getRate()));
                    String rate = "           " + rateValue;
                    rate = (rate.substring(rateValue.length(), rate.length()));

                    String amtValue = String.valueOf(formatter.format(Order_Outlet_Filter.get(i).getAmount()));
                    String amt = "           " + amtValue;
                    amt = (amt.substring(amtValue.length(), amt.length()));

                    printama.printTextln(name + qty +
                            rate + amt);

                    printama.addNewLine();

                }

                printama.printLine();
                printama.addNewLine(2);

                String subTotal = "           " + formatter.format(subTotalVal);
                String totItem = "           " + totalitem.getText().toString();
                String totqty = "           " + totalqty.getText().toString();
                String discount = "           " + cashDisc;
                String outstand = "           " + outstandAmt;

                printama.printText("SubTotal" + "                       " + subTotal.substring(String.valueOf(formatter.format(subTotalVal)).length(), subTotal.length()));
                printama.addNewLine();
                printama.printText("Total Item" + "                     " + totItem.substring(totalitem.getText().toString().length(), totItem.length()));
                printama.addNewLine();
                printama.printText("Total Qty" + "                      " + totqty.substring(totalqty.getText().toString().length(), totqty.length()));
                printama.addNewLine();

                if (uomList != null) {
                    for (int i = 0; i < uomList.size(); i++) {
                        String uomQty = "           " + (int) uomList.get(i).getCnvQty();
                        printama.printText(uomList.get(i).getUOM_Nm() + "                      " + uomQty.substring(String.valueOf((int) uomList.get(i).getCnvQty()).toString().length(), uomQty.length()));
                        printama.addNewLine();

                    }

                }


                //  printama.printText("Gst Rate" + "                       " + gst.substring(gstrate.getText().toString().length(), gst.length()));
                for (int i = 0; i < taxList.size(); i++) {
                    String val = String.valueOf(formatter.format(taxList.get(i).getTax_Amt()));
                    String amt = "           " + val;
                    String type = taxList.get(i).getTax_Type() + "                               ";
                    printama.printText(type.substring(0, type.length() - taxList.get(i).getTax_Type().length()) + amt.substring(val.length(), amt.length()));
                    printama.addNewLine();

                }
//                if (tvOutstanding.getVisibility() == View.VISIBLE) {
//                    printama.printText("Outstanding" + "                    " + outstand.substring(String.valueOf(outstandAmt).length(),
//                            outstand.length()));
//                    printama.addNewLine();
//                }

                if (cashDisc > 0) {
                    printama.printText("Cash Discount" + "                  " + discount.substring(String.valueOf(cashDisc).length(), discount.length()));
                    printama.addNewLine();
                    printama.printLine();
                }

                printama.addNewLine(2);
                printama.setTallBold();
                String strAmount = "           " + formatter.format(subTotalVal);

                printama.printText("Net amount" + "                     " + strAmount.substring(String.valueOf(subTotalVal).length(), strAmount.length()));
                printama.addNewLine(2);
                printama.printLine();

                printama.setLineSpacing(5);
                printama.feedPaper();
                printama.close();
            });
        } catch (Exception e) {
            Log.e("PRINT: ", e.getMessage());
        }
    }

    private void showPrinterList() {
        Printama.showPrinterList(this, R.color.dark_blue, printerName -> {
            Toast.makeText(this, printerName, Toast.LENGTH_SHORT).show();
            // TextView connectedTo = findViewById(R.id.tv_printer_info);
            String text = "Connected to : " + printerName;
            //connectedTo.setText(text);
            if (!printerName.contains("failed")) {
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
//                findViewById(R.id.btn_printer_test).setVisibility(View.VISIBLE);
                //findViewById(R.id.btn_printer_test).setOnClickListener(v -> testPrinter());
            }
        });
    }

    private void getSavedPrinter() {
        BluetoothDevice connectedPrinter = Printama.with(this).getConnectedPrinter();
        if (connectedPrinter != null) {
            //  TextView connectedTo = findViewById(R.id.tv_printer_info);
            //   String text = "Connected to : " + connectedPrinter.getName();
            // connectedTo.setText(text);
        }
    }

    private void createPdf() {
        try {
            int hgt = 500 + (Order_Outlet_Filter.size() * 40);

            // create a new document
            PdfDocument document = new PdfDocument();
            int widthSize = 600;
            // crate a page description
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(widthSize, hgt, 1).create();
            // start a page
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();
//        paint.setColor(Color.RED);
//
//
//        canvas.drawCircle(50, 50, 30, paint);

            paint.setColor(Color.BLACK);
            paint.setTextSize(17);

            int x = 10;
            int y = 50;

            canvas.drawText(storeName, x, y, paint);

//        y = y + 25;
//
//        Drawable d = getResources().getDrawable(R.drawable.rect_dash_background, null);
//        d.setBounds(5, y, 250, 170);
//        d.draw(canvas);

            paint.setColor(Color.BLACK);
            paint.setTextSize(14);
            paint.setTextAlign(Paint.Align.LEFT);

            y = y + 30;

            if (tvDistAdd.getVisibility() == View.VISIBLE) {
                canvas.drawText("Address :", x, y, paint);
                paint.setColor(Color.DKGRAY);
                paint.setTextSize(14);
                y = y + 20;

                String[] lines = Split(address, 60, address.length());
                for (int i = 0; i < lines.length; i++) {
                    System.out.println("lines[" + i + "]: (len: " + lines[i].length() + ") : " + lines[i]);
                    canvas.drawText(lines[i], x, y, paint);
                    y = y + 20;

                }
            }
            if (tvDistributorPh.getVisibility() == View.VISIBLE) {
                canvas.drawText(phone, x, y, paint);
                y = y + 30;
            }

            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5);
            canvas.drawLine(0, y, widthSize, y, paint);


            paint.setColor(Color.LTGRAY);
            paint.setStrokeWidth(40);
            canvas.drawLine(0, y + 30, widthSize, y + 30, paint);

            y = y + 35;
            paint.setColor(Color.BLACK);
            paint.setTextSize(15);
            canvas.drawText("" + billnumber.getText().toString(), x, y, paint);
            canvas.drawText("" + invoicedate.getText().toString(), (widthSize / 2) + 100, y, paint);

            y = y + 25;
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(1);
            canvas.drawLine(0, y, widthSize, y, paint);


            String space = "     ";
            y = y + 20;
            paint.setColor(Color.BLACK);
            paint.setTextSize(15);

            canvas.drawText("Item", x, y, paint);
            canvas.drawText("Qty", (widthSize / 2) + 20, y, paint);
            canvas.drawText("Price", (widthSize / 2) + 70, y, paint);
            canvas.drawText("GST", (widthSize / 2) + 150, y, paint);
            canvas.drawText("Total", (widthSize / 2) + 200, y, paint);


            y = y + 10;
            paint.setColor(Color.BLACK);
            canvas.drawLine(0, y, widthSize, y, paint);

            for (int i = 0; i < Order_Outlet_Filter.size(); i++) {

                y = y + 20;
                paint.setColor(Color.DKGRAY);
                paint.setTextSize(14);


                String name = Order_Outlet_Filter.get(i).getName() + "                               ";
                name = name.substring(0, name.length() - String.valueOf(Order_Outlet_Filter.get(i).getName()).length());


                String qtyValue = String.valueOf(Order_Outlet_Filter.get(i).getQty());
                String qty = "     " + qtyValue;
                qty = qty.substring(qtyValue.length(), qty.length());

                String rateValue = String.valueOf(formatter.format(Order_Outlet_Filter.get(i).getRate()));
                String rate = "           " + rateValue;
                rate = (rate.substring(rateValue.length(), rate.length()));

                String amtValue = String.valueOf(formatter.format(Order_Outlet_Filter.get(i).getAmount()));
                String amt = "           " + amtValue;
                amt = (amt.substring(amtValue.length(), amt.length()));

                Log.e("Values length: ", "item: " + name.length() + " qty: " + qty.length() + " rate: " + rate.length() + " amt : " + amt.length());

                // canvas.drawText(name + qty + rate + amt, x, y, paint);


                canvas.drawText("" + Order_Outlet_Filter.get(i).getName(), x, y, paint);
                canvas.drawText("" + Order_Outlet_Filter.get(i).getQty(), (widthSize / 2) + 20, y, paint);
                canvas.drawText("" + formatter.format(Order_Outlet_Filter.get(i).getRate()), (widthSize / 2) + 70, y, paint);
                canvas.drawText("" + formatter.format(Order_Outlet_Filter.get(i).getTax()), (widthSize / 2) + 150, y, paint);
                canvas.drawText("" + formatter.format(Order_Outlet_Filter.get(i).getAmount()), (widthSize / 2) + 200, y, paint);


            }


            y = y + 20;
            paint.setColor(Color.DKGRAY);
            canvas.drawLine(0, y, widthSize, y, paint);

            paint.setColor(Color.DKGRAY);

            y = y + 30;
            canvas.drawText("SubTotal", x, y, paint);
            canvas.drawText(subtotal.getText().toString(), (widthSize / 2) + 150, y, paint);

            y = y + 30;
            canvas.drawText("Total Item", x, y, paint);
            canvas.drawText(totalitem.getText().toString(), (widthSize / 2) + 150, y, paint);
            y = y + 30;
            canvas.drawText("Total Qty", x, y, paint);
            canvas.drawText(totalqty.getText().toString(), (widthSize / 2) + 150, y, paint);

            if (uomList != null) {
                for (int i = 0; i < uomList.size(); i++) {
                    y = y + 30;
                    canvas.drawText(uomList.get(i).getUOM_Nm(), x, y, paint);
                    canvas.drawText("" + (int) uomList.get(i).getCnvQty(), (widthSize / 2) + 150, y, paint);
                }

            }

//            y = y + 30;
//            canvas.drawText("Gst Rate", x, y, paint);
//            canvas.drawText(gstrate.getText().toString(), (widthSize / 2) + 150, y, paint);
//
            for (int i = 0; i < taxList.size(); i++) {
                y = y + 30;
                canvas.drawText(taxList.get(i).getTax_Type(), x, y, paint);
                canvas.drawText("₹" + formatter.format(taxList.get(i).getTax_Amt()), (widthSize / 2) + 150, y, paint);

            }

            y = y + 30;

//            if (tvOutstanding.getVisibility() == View.VISIBLE) {
//                canvas.drawText("Outstanding", x, y, paint);
//                canvas.drawText(tvOutstanding.getText().toString(), (widthSize / 2) + 150, y, paint);
//                y = y + 30;
//            }

            if (cashDisc > 0) {
                canvas.drawText("Cash Discount", x, y, paint);
                canvas.drawText(cashdiscount.getText().toString(), (widthSize / 2) + 150, y, paint);
                y = y + 20;
            }
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5);
            canvas.drawLine(0, y, widthSize, y, paint);

            paint.setTextSize(16);
            y = y + 30;
            canvas.drawText("Net Amount", x, y, paint);
            canvas.drawText(netamount.getText().toString(), (widthSize / 2) + 150, y, paint);

            y = y + 20;
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5);
            canvas.drawLine(0, y, widthSize, y, paint);

            //canvas.drawt
            // finish the page
            document.finishPage(page);
// draw text on the graphics object of the page
            // Create Page 2
//        pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 2).create();
//        page = document.startPage(pageInfo);
//        canvas = page.getCanvas();
//        paint = new Paint();
//        paint.setColor(Color.BLUE);
//        canvas.drawCircle(100, 100, 100, paint);
//        document.finishPage(page);


            // write the document content
            String directory_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/hap/";
            File file = new File(directory_path);

            deleteRecursive(file);

            if (!file.exists()) {
                file.mkdirs();
            }
            String targetPdf = directory_path + System.currentTimeMillis() + "bill.pdf";
            File filePath = new File(targetPdf);


            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();

            // close the document
            document.close();


            Uri fileUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", filePath);


            Intent intent = ShareCompat.IntentBuilder.from(this)
                    .setType("*/*")
                    .setStream(fileUri)
                    .setChooserTitle("Choose bar")
                    .createChooserIntent()
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(intent);
        } catch (Exception e) {
            Log.e("main", "error " + e.toString());
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    public static String[] Split(String text, int chunkSize, int maxLength) {
        char[] data = text.toCharArray();
        int len = Math.min(data.length, maxLength);
        String[] result = new String[(len + chunkSize - 1) / chunkSize];
        int linha = 0;
        for (int i = 0; i < len; i += chunkSize) {
            result[linha] = new String(data, i, Math.min(chunkSize, len - i));
            linha++;
        }
        return result;
    }

    void deleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();

    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {
            if (apiDataResponse != null && !apiDataResponse.equals("")) {
                switch (key) {
                    case Constants.TodayOrderDetails_List:
                        orderInvoiceDetailData(apiDataResponse);
                        break;
                    case Constants.ProjectionOrderDetails_List:
                        orderInvoiceDetailData(apiDataResponse);
                        break;
                    case Constants.TodayPrimaryOrderDetails_List:
                        Log.v("pri_res:", apiDataResponse);
                        orderInvoiceDetailData(apiDataResponse);
                        break;
                    case Constants.PosOrderDetails_List:
                        orderInvoiceDetailData(apiDataResponse);
                        break;
                    case Constants.OUTSTANDING:
                        JSONObject jsonObject = new JSONObject(apiDataResponse);
                        if (jsonObject.getBoolean("success")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("Data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                outstandAmt = Double.parseDouble(new DecimalFormat("##0.00").format(
                                        jsonArray.getJSONObject(i).getDouble("Outstanding")));

                            }

                        }

                        tvOutstanding.setText("₹" + formatter.format(outstandAmt));
                        break;
                }
            }
        } catch (Exception e) {

        }

    }

    void orderInvoiceDetailData(String response) {
        try {
            if (sharedCommonPref.getvalue(Constants.FLAG).equalsIgnoreCase("POS INVOICE"))
                Shared_Common_Pref.TransSlNo = Shared_Common_Pref.TransSlNo.replace("HAPH", "");
            billnumber.setText("Order " + Shared_Common_Pref.TransSlNo);
            Order_Outlet_Filter = new ArrayList<>();
            Order_Outlet_Filter.clear();

            int total_qtytext = 0;
            double tcsVal = 0;
            subTotalVal = 0.00;

            taxList = new ArrayList<>();
            taxList.clear();

            if (sharedCommonPref.getvalue(Constants.FLAG).equalsIgnoreCase("Return Invoice")) {
                JSONArray arr = new JSONArray(response);
                for (int a = 0; a < arr.length(); a++) {
                    List<Product_Details_Modal> pmTax = new ArrayList<>();
                    JSONObject obj = arr.getJSONObject(a);
                    total_qtytext += obj.getInt("Qty");
                    double amt = 0;

                    double taxAmt = 0.00;

                    String taxRes = sharedCommonPref.getvalue(Constants.TAXList);
                    if (!Common_Class.isNullOrEmpty(taxRes)) {
                        JSONObject jsonObject = new JSONObject(taxRes);
                        JSONArray jsonArray = jsonObject.getJSONArray("Data");


                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            if (jsonObject1.getString("Product_Detail_Code").equals(obj.getString("PCode"))) {
                                if (jsonObject1.getDouble("Tax_Val") > 0) {
                                    double taxCal = (obj.getInt("Qty") * obj.getDouble("Price")) *
                                            ((jsonObject1.getDouble("Tax_Val") / 100));

                                    pmTax.add(new Product_Details_Modal(jsonObject1.getString("Tax_Id"),
                                            jsonObject1.getString("Tax_Type"), jsonObject1.getDouble("Tax_Val"), taxCal));


                                    String label = jsonObject1.getString("Tax_Type");

                                    taxAmt += taxCal;
                                    if (taxList.size() == 0) {
                                        taxList.add(new Product_Details_Modal(label, taxCal));
                                    } else {

                                        boolean isDuplicate = false;
                                        for (int totTax = 0; totTax < taxList.size(); totTax++) {
                                            if (taxList.get(totTax).getTax_Type().equals(label)) {
                                                double oldAmt = taxList.get(totTax).getTax_Amt();
                                                isDuplicate = true;
                                                taxList.set(totTax, new Product_Details_Modal(label, oldAmt + taxCal));

                                            }
                                        }

                                        if (!isDuplicate) {
                                            taxList.add(new Product_Details_Modal(label, taxCal));
                                        }
                                    }

                                }
                            }
                        }

                        amt = ((obj.getInt("Qty") * obj.getDouble("Price"))) + taxAmt;
                        subTotalVal += amt;

                    }
                    Order_Outlet_Filter.add(new Product_Details_Modal(obj.getString("PCode"), obj.getString("PDetails"), 1, "1",
                            "1", "5", "", 0, "0", obj.getDouble("Price"),
                            obj.getInt("Qty"), obj.getInt("Qty"), amt, pmTax, "0", (taxAmt)));


                }
                mReportViewAdapter = new Print_Invoice_Adapter(Print_Invoice_Activity.this, Order_Outlet_Filter, sharedCommonPref.getvalue(Constants.FLAG));
                rvReturnInv.setAdapter(mReportViewAdapter);

            } else {

                if (sharedCommonPref.getvalue(Constants.FLAG).equals("Primary Order")) {

                    JSONObject primObj = new JSONObject(response);

                    JSONArray orderArr = primObj.getJSONArray("OrderData");
                    JSONArray uomArr = primObj.getJSONArray("uom_details");

                    for (int i = 0; i < orderArr.length(); i++) {
                        JSONObject obj = orderArr.getJSONObject(i);
                        total_qtytext += obj.getInt("Quantity");
                        subTotalVal += (obj.getDouble("value"));

                        tcsVal = obj.getDouble("TCS");

                        String paidAmt = "0";
                        try {
                            paidAmt = obj.getString("PaidAmount");
                        } catch (Exception e) {
                        }

                        double taxAmt = 0.00;
                        try {
                            JSONArray taxArr = obj.getJSONArray("TAX_details");
                            for (int tax = 0; tax < taxArr.length(); tax++) {
                                JSONObject taxObj = taxArr.getJSONObject(tax);
                                String label = taxObj.getString("Tax_Name");
                                Double amt = taxObj.getDouble("Tax_Amt");

                                taxAmt += taxObj.getDouble("Tax_Amt");
                                if (taxList.size() == 0) {
                                    if (tcsVal > 0)
                                        taxList.add(new Product_Details_Modal("TCS", tcsVal));
                                    taxList.add(new Product_Details_Modal(label, amt));
                                } else {

                                    boolean isDuplicate = false;
                                    for (int totTax = 0; totTax < taxList.size(); totTax++) {
                                        if (taxList.get(totTax).getTax_Type().equals(label)) {
                                            double oldAmt = taxList.get(totTax).getTax_Amt();
                                            isDuplicate = true;
                                            taxList.set(totTax, new Product_Details_Modal(label, oldAmt + amt));

                                        }
                                    }

                                    if (!isDuplicate) {
                                        taxList.add(new Product_Details_Modal(label, amt));
                                    }
                                }

                            }
                        } catch (Exception e) {

                        }
                        Order_Outlet_Filter.add(new Product_Details_Modal(obj.getString("Product_Code"), obj.getString("Product_Name"), 1, "1",
                                "1", "5", obj.getString("UOM"), 0, "0", obj.getDouble("Rate"),
                                obj.getInt("Quantity"), obj.getInt("qty"), obj.getDouble("value"), taxList, paidAmt, (taxAmt)));


                    }

                    if (taxList.size() == 0 && tcsVal > 0)
                        taxList.add(new Product_Details_Modal("TCS", tcsVal));

                    uomList = new ArrayList<>();

                    for (int i = 0; i < uomArr.length(); i++) {
                        JSONObject obj = uomArr.getJSONObject(i);
                        uomList.add(new Product_Details_Modal(obj.getInt("uom_qty"), obj.getString("uom_name")));
                    }

                    if (uomList != null && uomList.size() > 0) {
                        findViewById(R.id.rlUomParent).setVisibility(View.VISIBLE);
                        TextView tvUOMName = findViewById(R.id.tvUomLabel);
                        TextView tvUomQty = findViewById(R.id.tvUomQty);
                        String uomName = "";
                        String uomQty = "";
                        for (int i = 0; i < uomList.size(); i++) {
                            uomName = uomName + uomList.get(i).getUOM_Nm() + "\n";
                            uomQty = uomQty + (int) uomList.get(i).getCnvQty() + "\n";
                        }


                        tvUOMName.setText("" + uomName);
                        tvUomQty.setText("" + uomQty);
                    }
                } else {
                    JSONArray arr = new JSONArray(response);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        total_qtytext += obj.getInt("Quantity");
                        subTotalVal += (obj.getDouble("value"));

                        double taxAmt = 0.00;
                        try {
                            JSONArray taxArr = obj.getJSONArray("TAX_details");
                            for (int tax = 0; tax < taxArr.length(); tax++) {
                                JSONObject taxObj = taxArr.getJSONObject(tax);
                                String label = taxObj.getString("Tax_Name");
                                Double amt = taxObj.getDouble("Tax_Amt");

                                taxAmt += taxObj.getDouble("Tax_Amt");
                                if (taxList.size() == 0) {
                                    if (tcsVal > 0)
                                        taxList.add(new Product_Details_Modal("TCS", tcsVal));
                                    taxList.add(new Product_Details_Modal(label, amt));
                                } else {

                                    boolean isDuplicate = false;
                                    for (int totTax = 0; totTax < taxList.size(); totTax++) {
                                        if (taxList.get(totTax).getTax_Type().equals(label)) {
                                            double oldAmt = taxList.get(totTax).getTax_Amt();
                                            isDuplicate = true;
                                            taxList.set(totTax, new Product_Details_Modal(label, oldAmt + amt));

                                        }
                                    }

                                    if (!isDuplicate) {
                                        taxList.add(new Product_Details_Modal(label, amt));
                                    }
                                }

                            }
                        } catch (Exception e) {

                        }


                        Order_Outlet_Filter.add(new Product_Details_Modal(obj.getString("Product_Code"), obj.getString("Product_Name"), 1, "1",
                                "1", "5", obj.getString("UOM"), 0, "0", obj.getDouble("BillRate"),
                                obj.getInt("Quantity"), obj.getInt("qty"), obj.getDouble("value"), taxList, "0", (taxAmt)));


                    }

                    if (sharedCommonPref.getvalue(Constants.FLAG).equalsIgnoreCase("Projection")) {
                        try {
                            storeName = arr.getJSONObject(0).getString("plantName") + "(" + arr.getJSONObject(0).getInt("plantID") + ")";
                            tvDistributorName.setText("" + storeName);

                        } catch (Exception e) {

                        }

                    }
                }
                mReportViewAdapter = new Print_Invoice_Adapter(Print_Invoice_Activity.this, Order_Outlet_Filter, sharedCommonPref.getvalue(Constants.FLAG));
                printrecyclerview.setAdapter(mReportViewAdapter);


            }


            subTotalVal = Double.parseDouble(formatter.format(subTotalVal + tcsVal));

            totalqty.setText("" + String.valueOf(total_qtytext));
            totalitem.setText("" + Order_Outlet_Filter.size());
            subtotal.setText("₹" + formatter.format(subTotalVal));
            netamount.setText("₹ " + formatter.format(subTotalVal));


            returntotalqty.setText("" + String.valueOf(total_qtytext));
            returntotalitem.setText("" + Order_Outlet_Filter.size());
            returnsubtotal.setText("₹" + formatter.format(subTotalVal));
            returnNetAmt.setText("₹ " + formatter.format(subTotalVal));

            tvPaidAmt.setText("₹ " + formatter.format(Double.parseDouble(Order_Outlet_Filter.get(0).getPaidAmount())));

            sharedCommonPref.save(Constants.INVOICE_ORDERLIST, response);


            cashdiscount.setText("₹" + formatter.format(cashDisc));
            gstrate.setText("₹" + formatter.format(Double.parseDouble(getIntent().getStringExtra("NetAmount"))));

            label = "";
            amt = "";
            for (int i = 0; i < taxList.size(); i++) {
                label = label + taxList.get(i).getTax_Type() + "\n";
                amt = amt + "₹" + String.valueOf(formatter.format(taxList.get(i).getTax_Amt())) + "\n";
            }

            gstLabel.setText(label);
            gstrate.setText(amt);
            returngstLabel.setText(label);
            returngstrate.setText(amt);


        } catch (Exception e) {
            Log.e("PRINT:getData ", e.getMessage());
        }
    }
}
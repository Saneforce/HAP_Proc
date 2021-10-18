package com.hap.checkinproc.SFA_Activity;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.AdapterOnClick;
import com.hap.checkinproc.Interface.ApiClient;
import com.hap.checkinproc.Interface.ApiInterface;
import com.hap.checkinproc.Interface.UpdateResponseUI;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.Print_Invoice_Adapter;
import com.hap.checkinproc.SFA_Model_Class.OutletReport_View_Modal;
import com.hap.checkinproc.SFA_Model_Class.Product_Details_Modal;
import com.hap.checkinproc.SFA_Model_Class.Retailer_Modal_List;
import com.hap.checkinproc.SFA_Model_Class.Trans_Order_Details_Offline;
import com.hap.checkinproc.common.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Print_Invoice_Activity extends AppCompatActivity implements View.OnClickListener, UpdateResponseUI {
    Print_Invoice_Adapter mReportViewAdapter;
    RecyclerView printrecyclerview;
    Shared_Common_Pref sharedCommonPref;
    Type userType;
    Gson gson;
    Common_Class common_class;
    List<Trans_Order_Details_Offline> InvoiceorderDetails_List;
    List<Product_Details_Modal> Order_Outlet_Filter;
    TextView netamount, cashdiscount, gstrate, totalfreeqty, totalqty, totalitem, subtotal, invoicedate, retaileAddress, billnumber,
            retailername, retailerroute, back, tvOrderType, tvRetailorPhone, tvDistributorPh, tvDistributorName, tvOutstanding;
    DatabaseHandler db;

    ImageView ok, ivPrint;
    private FileOutputStream writer;
    public static Print_Invoice_Activity mPrint_invoice_activity;

    Button btnInvoice;
    NumberFormat formatter = new DecimalFormat("##0.00");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            mPrint_invoice_activity = this;
            setContentView(R.layout.activity_print__invoice_);
            db = new DatabaseHandler(this);
            printrecyclerview = findViewById(R.id.printrecyclerview);
            gson = new Gson();
            sharedCommonPref = new Shared_Common_Pref(Print_Invoice_Activity.this);
            common_class = new Common_Class(this);

            common_class.getDataFromApi(Constants.TodayOrderDetails_List, this, false);

            netamount = findViewById(R.id.netamount);
            back = findViewById(R.id.back);
            cashdiscount = findViewById(R.id.cashdiscount);
            gstrate = findViewById(R.id.gstrate);
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

            retailername.setText(sharedCommonPref.getvalue(Constants.Retailor_Name_ERP_Code));
            tvDistributorName.setText(sharedCommonPref.getvalue(Constants.Distributor_name));
            ivPrint = findViewById(R.id.ivPrint);
            back.setOnClickListener(this);
            ok.setOnClickListener(this);
            ivPrint.setOnClickListener(this);
            btnInvoice.setOnClickListener(this);


            ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
            common_class.gotoHomeScreen(this, ivToolbarHome);

            if (sharedCommonPref.getvalue(Constants.FLAG).equals("ORDER")) {
                findViewById(R.id.llCreateInvoice).setVisibility(View.VISIBLE);
                tvOrderType.setText("ORDER");
            } else {
                findViewById(R.id.llCreateInvoice).setVisibility(View.GONE);
                tvOrderType.setText("TAX INVOICE");
            }

            tvDistributorPh.setText(sharedCommonPref.getvalue(Constants.Distributor_phone));
            tvRetailorPhone.setText(sharedCommonPref.getvalue(Constants.Retailor_PHNo));


            common_class.getDb_310Data(Constants.OUTSTANDING, this);

        } catch (Exception e) {

        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ok:
                createPdf();
                break;
            case R.id.ivPrint:
                showPrinterList();
                break;

            case R.id.btnInvoice:
                Shared_Common_Pref.Invoicetoorder = "4";
                getInvoiceOrderDetails();
                break;
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
                printama.printTextln(Printama.CENTER, retailername.getText().toString());
                printama.addNewLine();


                printama.setBold();
                printama.printTextln(Printama.LEFT, "Address :");


                printama.setNormalText();
                printama.addNewLine();
                printama.printTextln(Printama.LEFT, retailerroute.getText().toString());
                printama.printTextln(Printama.LEFT, retaileAddress.getText().toString());
                printama.addNewLine(2);


                printama.setBold();
                printama.printText(billnumber.getText().toString() + "          " + invoicedate.getText().toString());

                printama.addNewLine();

                printama.printLine();
                printama.addNewLine(2);

                printama.setBold();
                printama.printTextln("Item       " + "     Qty" + "       Rate" + "      Total");
                printama.printLine();

                printama.addNewLine();
                printama.addNewLine();


                for (int i = 0; i < Order_Outlet_Filter.size(); i++) {
                    printama.setBold();
                    printama.printTextln(Order_Outlet_Filter.get(i).getName());
                    printama.addNewLine();

                    printama.setNormalText();

                    String name = Order_Outlet_Filter.get(i).getId() + "           ";
                    name = name.substring(0, name.length() - String.valueOf(Order_Outlet_Filter.get(i).getId()).length());


                    String qtyValue = String.valueOf(Order_Outlet_Filter.get(i).getQty());
                    String qty = "        " + qtyValue;
                    qty = qty.substring(qtyValue.length(), qty.length());

                    String rateValue = String.valueOf(Order_Outlet_Filter.get(i).getRate());
                    String rate = "           " + rateValue;
                    rate = (rate.substring(rateValue.length(), rate.length()));

                    String amtValue = String.valueOf(Order_Outlet_Filter.get(i).getAmount());
                    String amt = "           " + amtValue;
                    amt = (amt.substring(amtValue.length(), amt.length()));

                    printama.printTextln(name + qty +
                            rate + amt);

                    printama.addNewLine();

                }

                printama.printLine();
                printama.addNewLine(2);


                String subTotal = "           " + subtotal.getText().toString();
                String totItem = "           " + totalitem.getText().toString();
                String totqty = "           " + totalqty.getText().toString();
                String gst = "           " + gstrate.getText().toString();
                String discount = "           " + cashdiscount.getText().toString();

                String outstand = "           " + tvOutstanding.getText().toString();

                printama.printText("SubTotal" + "                       " + subTotal.substring(subtotal.getText().toString().length(), subTotal.length()));
                printama.addNewLine();
                printama.printText("Total Item" + "                     " + totItem.substring(totalitem.getText().toString().length(), totItem.length()));
                printama.addNewLine();
                printama.printText("Total Qty" + "                      " + totqty.substring(totalqty.getText().toString().length(), totqty.length()));
                printama.addNewLine();
                printama.printText("Gst Rate" + "                       " + gst.substring(gstrate.getText().toString().length(), gst.length()));
                printama.addNewLine();
                printama.printText("Outstanding" + "                    " + gst.substring(tvOutstanding.getText().toString().length(),
                        outstand.length()));
                printama.addNewLine();
                printama.printText("Cash Discount" + "                  " + discount.substring(cashdiscount.getText().toString().length(), discount.length()));
                printama.addNewLine();

                printama.printLine();

                printama.addNewLine(2);
                printama.setTallBold();
                String strAmount = "           " + netamount.getText().toString();

                printama.printText("Net amount" + "                     " + strAmount.substring(netamount.getText().toString().length(), strAmount.length()));
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


            int widthSize = 500;
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

            canvas.drawText(retailername.getText().toString(), x, y, paint);


//        y = y + 25;
//
//        Drawable d = getResources().getDrawable(R.drawable.rect_dash_background, null);
//        d.setBounds(5, y, 250, 170);
//        d.draw(canvas);


            paint.setColor(Color.BLACK);
            paint.setTextSize(14);
            paint.setTextAlign(Paint.Align.LEFT);

            y = y + 30;
            canvas.drawText("Address :", x, y, paint);
            paint.setColor(Color.DKGRAY);
            paint.setTextSize(14);
            y = y + 20;
            canvas.drawText("Nandanam", x, y, paint);
            y = y + 20;
            canvas.drawText("No 4,Lotus colony,Nandanam", x, y, paint);
            y = y + 20;
            canvas.drawText("Chennai 600028", x, y, paint);

            y = y + 20;
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

//            String item = "Item                           ";
//            String qty1 = "     Qty";
//            String rate1 = "       Rate";
//            String amt1 = "      Total";


            // canvas.drawText(item + qty1 + rate1 + amt1, x, y, paint);
            canvas.drawText("Item", x, y, paint);
            canvas.drawText("Qty", (widthSize / 2) + 20, y, paint);
            canvas.drawText("Rate", (widthSize / 2) + 70, y, paint);
            canvas.drawText("Total", (widthSize / 2) + 150, y, paint);


            //  Log.e("Header length: ", "item: " + item.length() + " qty: " + qty1.length() + " rate: " + rate1.length() + " amt : " + amt1.length());


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

                String rateValue = String.valueOf(Order_Outlet_Filter.get(i).getRate());
                String rate = "           " + rateValue;
                rate = (rate.substring(rateValue.length(), rate.length()));

                String amtValue = String.valueOf(Order_Outlet_Filter.get(i).getAmount());
                String amt = "           " + amtValue;
                amt = (amt.substring(amtValue.length(), amt.length()));

                Log.e("Values length: ", "item: " + name.length() + " qty: " + qty.length() + " rate: " + rate.length() + " amt : " + amt.length());

                // canvas.drawText(name + qty + rate + amt, x, y, paint);


                canvas.drawText("" + Order_Outlet_Filter.get(i).getName(), x, y, paint);
                canvas.drawText("" + Order_Outlet_Filter.get(i).getQty(), (widthSize / 2) + 20, y, paint);
                canvas.drawText("" + Order_Outlet_Filter.get(i).getRate(), (widthSize / 2) + 70, y, paint);
                canvas.drawText("" + Order_Outlet_Filter.get(i).getAmount(), (widthSize / 2) + 150, y, paint);


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
            y = y + 30;
            canvas.drawText("Gst Rate", x, y, paint);
            canvas.drawText(gstrate.getText().toString(), (widthSize / 2) + 150, y, paint);

            y = y + 30;
            canvas.drawText("Outstanding", x, y, paint);
            canvas.drawText(tvOutstanding.getText().toString(), (widthSize / 2) + 150, y, paint);
            y = y + 30;
            canvas.drawText("Cash Discount", x, y, paint);
            canvas.drawText(cashdiscount.getText().toString(), (widthSize / 2) + 150, y, paint);


            y = y + 20;
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

    void deleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();

    }


    @Override
    public void onLoadFilterData(List<Retailer_Modal_List> retailer_modal_list) {

    }

    @Override
    public void onLoadTodayOrderList(List<OutletReport_View_Modal> outletReportViewModals) {

    }

    @Override
    public void onLoadDataUpdateUI(String apiDataResponse, String key) {
        try {
            if (apiDataResponse != null && !apiDataResponse.equals("")) {
                switch (key) {
                    case Constants.TodayOrderDetails_List:
                        orderInvoiceDetailData(apiDataResponse);
                        break;
                    case Constants.OUTSTANDING:
                        JSONObject jsonObject = new JSONObject(apiDataResponse);
                        if (jsonObject.getBoolean("success")) {

                            JSONArray jsonArray = jsonObject.getJSONArray("Data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                tvOutstanding.setText("₹" + new DecimalFormat("##0.00").format(
                                        jsonArray.getJSONObject(i).getDouble("Outstanding")));

                            }

                        } else {

                            tvOutstanding.setText("₹" + 0.00);
                        }
                        break;
                }
            }
        } catch (Exception e) {

        }

    }

    private void getInvoiceOrderDetails() {
        try {
            if (common_class.isNetworkAvailable(this)) {
                common_class.ProgressdialogShow(1, "");
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

                JSONObject HeadItem = new JSONObject();

                HeadItem.put("OrderID", Shared_Common_Pref.TransSlNo);


                Call<ResponseBody> call = service.getInvoiceOrderDetails(HeadItem.toString());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        InputStreamReader ip = null;
                        StringBuilder is = new StringBuilder();
                        String line = null;
                        try {
                            if (response.isSuccessful()) {
                                ip = new InputStreamReader(response.body().byteStream());
                                BufferedReader bf = new BufferedReader(ip);
                                while ((line = bf.readLine()) != null) {
                                    is.append(line);
                                    Log.v("Res>>", is.toString());
                                }

                                JSONObject jsonObject = new JSONObject(is.toString());


                                if (jsonObject.getBoolean("success")) {


                                    sharedCommonPref.save(Constants.InvoiceQtyList, is.toString());


                                    common_class.CommonIntentwithFinish(Invoice_Category_Select.class);


                                } else {
                                    sharedCommonPref.clear_pref(Constants.InvoiceQtyList);
                                    Log.v("PreOrderList: ", "" + "not success");

                                    common_class.CommonIntentwithFinish(Invoice_Category_Select.class);


                                }


                            } else {

                            }

                        } catch (Exception e) {
                            common_class.ProgressdialogShow(0, "");


                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.v("fail>>", t.toString());
                        common_class.ProgressdialogShow(0, "");


                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.v("fail>>", e.getMessage());


        }
    }


    void orderInvoiceDetailData(String response) {
        try {

            billnumber.setText("Order " + Shared_Common_Pref.TransSlNo);
            //   String orderlist = String.valueOf(db.getMasterData(Constants.TodayOrderDetails_List));
            userType = new TypeToken<ArrayList<Trans_Order_Details_Offline>>() {
            }.getType();
            InvoiceorderDetails_List = gson.fromJson(response, userType);
            Order_Outlet_Filter = new ArrayList<>();
            Order_Outlet_Filter.clear();
            int total_qtytext = 0;
            double subTotalVal = 0.00;
            for (Trans_Order_Details_Offline ivl : InvoiceorderDetails_List) {

                total_qtytext += ivl.getQuantity();
                subTotalVal += ivl.getValue();


                Order_Outlet_Filter.add(new Product_Details_Modal(ivl.getProductCode(), ivl.getProductName(), 1, "1",
                        "1", "5", "i", 7.99, 1.8, ivl.getRate(), ivl.getQuantity(), ivl.getQty(), ivl.getValue()));

            }


            retailerroute.setText(sharedCommonPref.getvalue(Constants.Route_name));
            retaileAddress.setText(sharedCommonPref.getvalue(Constants.Retailor_Address));

            totalqty.setText("" + String.valueOf(total_qtytext));
            totalitem.setText("" + Order_Outlet_Filter.size());
            subtotal.setText("₹" + formatter.format(subTotalVal));
            netamount.setText("₹ " + formatter.format(subTotalVal));


            invoicedate.setText(/*"Date : " +*/ Common_Class.GetDatewothouttime());

            sharedCommonPref.save(Constants.INVOICE_ORDERLIST, gson.toJson(Order_Outlet_Filter));

            mReportViewAdapter = new Print_Invoice_Adapter(Print_Invoice_Activity.this, Order_Outlet_Filter, new AdapterOnClick() {
                @Override
                public void onIntentClick(int position) {
                    //  sharedCommonPref.save(Constants.ORDER_ID, Order_Outlet_Filter.get(position).getId());
                }
            });
            printrecyclerview.setAdapter(mReportViewAdapter);

            cashdiscount.setText("₹" + formatter.format(Double.parseDouble(getIntent().getStringExtra("Discount_Amount"))));
            gstrate.setText("₹" + formatter.format(Double.parseDouble(getIntent().getStringExtra("NetAmount"))));


        } catch (Exception e) {
            Log.e("PRINT:getData ", e.getMessage());

        }
    }
}
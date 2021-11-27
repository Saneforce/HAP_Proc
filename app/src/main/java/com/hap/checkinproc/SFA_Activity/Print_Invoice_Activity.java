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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.hap.checkinproc.Common_Class.Common_Class;
import com.hap.checkinproc.Common_Class.Constants;
import com.hap.checkinproc.Common_Class.Shared_Common_Pref;
import com.hap.checkinproc.Interface.UpdateResponseUI;
import com.hap.checkinproc.R;
import com.hap.checkinproc.SFA_Adapter.Print_Invoice_Adapter;
import com.hap.checkinproc.SFA_Model_Class.Product_Details_Modal;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class Print_Invoice_Activity extends AppCompatActivity implements View.OnClickListener, UpdateResponseUI {
    Print_Invoice_Adapter mReportViewAdapter;
    RecyclerView printrecyclerview;
    Shared_Common_Pref sharedCommonPref;
    Common_Class common_class;
    List<Product_Details_Modal> Order_Outlet_Filter;
    TextView netamount;
    TextView cashdiscount;
    TextView gstLabel;
    TextView gstrate;
    TextView totalfreeqty;
    TextView totalqty;
    TextView totalitem;
    TextView subtotal;
    TextView invoicedate;
    TextView retaileAddress;
    TextView billnumber;
    TextView retailername;
    TextView retailerroute;
    TextView back;
    TextView tvOrderType;
    TextView tvRetailorPhone;
    TextView tvDistributorPh;
    TextView tvDistributorName;
    TextView tvOutstanding;
    TextView tvPaidAmt;
    TextView tvHeader;
    TextView tvDistId;
    TextView tvDistAdd;

    ImageView ok, ivPrint;

    public static Print_Invoice_Activity mPrint_invoice_activity;

    Button btnInvoice;
    NumberFormat formatter = new DecimalFormat("##0.00");
    private String label, amt, storeName = "", address = "", phone = "";
    private ArrayList<Product_Details_Modal> taxList;
    double cashDisc, subTotalVal, outstandAmt;
    LinearLayout llDistCal, llRetailCal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            mPrint_invoice_activity = this;
            setContentView(R.layout.activity_print__invoice_);
            printrecyclerview = findViewById(R.id.printrecyclerview);

            sharedCommonPref = new Shared_Common_Pref(Print_Invoice_Activity.this);
            common_class = new Common_Class(this);
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
            tvPaidAmt = findViewById(R.id.tvPaidAmt);
            tvHeader = findViewById(R.id.tvHeader);
            gstLabel = findViewById(R.id.gstLabel);
            tvDistAdd = findViewById(R.id.tvAdd);
            tvDistId = findViewById(R.id.tvDistId);
            llDistCal = findViewById(R.id.llDistCall);
            llRetailCal = findViewById(R.id.llRetailCal);
            retailername.setText(sharedCommonPref.getvalue(Constants.Retailor_Name_ERP_Code));
            tvDistributorName.setText(sharedCommonPref.getvalue(Constants.Distributor_name));
            ivPrint = findViewById(R.id.ivPrint);
            back.setOnClickListener(this);
            ok.setOnClickListener(this);
            ivPrint.setOnClickListener(this);
            btnInvoice.setOnClickListener(this);
            llDistCal.setOnClickListener(this);
            llRetailCal.setOnClickListener(this);


            ImageView ivToolbarHome = findViewById(R.id.toolbar_home);
            common_class.gotoHomeScreen(this, ivToolbarHome);


            tvHeader.setText("Sales " + sharedCommonPref.getvalue(Constants.FLAG));

            tvDistributorPh.setText(sharedCommonPref.getvalue(Constants.Distributor_phone));
            tvRetailorPhone.setText(sharedCommonPref.getvalue(Constants.Retailor_PHNo));

            if (Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.Distributor_phone)))
                llDistCal.setVisibility(View.GONE);
            if (Common_Class.isNullOrEmpty(sharedCommonPref.getvalue(Constants.Retailor_PHNo)))
                llRetailCal.setVisibility(View.GONE);
            retailerroute.setText(sharedCommonPref.getvalue(Constants.Route_name));
            retaileAddress.setText(sharedCommonPref.getvalue(Constants.Retailor_Address));
            invoicedate.setText(Common_Class.GetDatewothouttime());
            tvDistId.setText(sharedCommonPref.getvalue(Constants.DistributorERP));
            tvDistAdd.setText(sharedCommonPref.getvalue(Constants.DistributorAdd));

            if (sharedCommonPref.getvalue(Constants.FLAG).equals("ORDER")) {
                findViewById(R.id.llCreateInvoice).setVisibility(View.VISIBLE);
                findViewById(R.id.cvPayDetails).setVisibility(View.GONE);
                tvOrderType.setText("ORDER");
                common_class.getDataFromApi(Constants.TodayOrderDetails_List, this, false);
                common_class.getDb_310Data(Constants.OUTSTANDING, this);
                storeName = retailername.getText().toString();
                address = retaileAddress.getText().toString();
                phone = "Mobile:" + tvRetailorPhone.getText().toString();

            } else if (sharedCommonPref.getvalue(Constants.FLAG).equals("Primary Order")) {
                findViewById(R.id.llCreateInvoice).setVisibility(View.GONE);
                findViewById(R.id.llOutletParent).setVisibility(View.GONE);
                findViewById(R.id.cvPayDetails).setVisibility(View.GONE);
                tvDistAdd.setVisibility(View.VISIBLE);
                tvDistId.setVisibility(View.VISIBLE);
                tvOrderType.setText("PRIMARY ORDER");
                common_class.getDataFromApi(Constants.TodayPrimaryOrderDetails_List, this, false);
                findViewById(R.id.llDelivery).setVisibility(View.GONE);
                storeName = tvDistributorName.getText().toString();
                address = tvDistAdd.getText().toString();
                phone = "Mobile:" + tvDistributorPh.getText().toString();

            } else {
                findViewById(R.id.llCreateInvoice).setVisibility(View.GONE);
                tvOrderType.setText("INVOICE");
                common_class.getDataFromApi(Constants.TodayOrderDetails_List, this, false);
                common_class.getDb_310Data(Constants.OUTSTANDING, this);
                storeName = retailername.getText().toString();
                address = retaileAddress.getText().toString();
                phone = "Mobile:" + tvRetailorPhone.getText().toString();

            }

            cashDisc = Double.parseDouble(getIntent().getStringExtra("Discount_Amount"));
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
                common_class.CommonIntentwithFinish(Invoice_Category_Select.class);
                break;

            case R.id.llDistCall:
                common_class.showCalDialog(this, "Do you want to Call this Distributor?", sharedCommonPref.getvalue(Constants.Distributor_phone));
                break;
            case R.id.llRetailCal:
                common_class.showCalDialog(this, "Do you want to Call this Outlet?", sharedCommonPref.getvalue(Constants.Retailor_PHNo));
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
                printama.printTextln(Printama.CENTER, storeName);
                printama.addNewLine();


                printama.setBold();
                printama.printTextln(Printama.LEFT, "Address :");


                printama.setNormalText();
                printama.addNewLine();
                printama.printTextln(Printama.LEFT, address);
                printama.printTextln(Printama.LEFT, phone);
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


                String subTotal = "           " + subTotalVal;
                String totItem = "           " + totalitem.getText().toString();
                String totqty = "           " + totalqty.getText().toString();
                String discount = "           " + cashDisc;
                String outstand = "           " + outstandAmt;

                printama.printText("SubTotal" + "                       " + subTotal.substring(String.valueOf(subTotalVal).length(), subTotal.length()));
                printama.addNewLine();
                printama.printText("Total Item" + "                     " + totItem.substring(totalitem.getText().toString().length(), totItem.length()));
                printama.addNewLine();
                printama.printText("Total Qty" + "                      " + totqty.substring(totalqty.getText().toString().length(), totqty.length()));
                printama.addNewLine();
                //  printama.printText("Gst Rate" + "                       " + gst.substring(gstrate.getText().toString().length(), gst.length()));
                for (int i = 0; i < taxList.size(); i++) {
                    String val = String.valueOf(formatter.format(taxList.get(i).getTax_Amt()));
                    String amt = "           " + val;
                    String type = taxList.get(i).getTax_Type() + "                               ";
                    printama.printText(type.substring(0, type.length() - taxList.get(i).getTax_Type().length()) + amt.substring(val.length(), amt.length()));
                    printama.addNewLine();

                }
                if (tvOutstanding.getVisibility() == View.VISIBLE) {
                    printama.printText("Outstanding" + "                    " + outstand.substring(String.valueOf(outstandAmt).length(),
                            outstand.length()));
                    printama.addNewLine();
                }

                if (cashDisc > 0) {
                    printama.printText("Cash Discount" + "                  " + discount.substring(String.valueOf(cashDisc).length(), discount.length()));
                    printama.addNewLine();
                    printama.printLine();
                }

                printama.addNewLine(2);
                printama.setTallBold();
                String strAmount = "           " + subTotalVal;

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
            canvas.drawText(phone, x, y, paint);
            y = y + 30;

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
            canvas.drawText("Rate", (widthSize / 2) + 70, y, paint);
            canvas.drawText("Total", (widthSize / 2) + 150, y, paint);


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
                canvas.drawText("" + formatter.format(Order_Outlet_Filter.get(i).getAmount()), (widthSize / 2) + 150, y, paint);


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

            if (tvOutstanding.getVisibility() == View.VISIBLE) {
                canvas.drawText("Outstanding", x, y, paint);
                canvas.drawText(tvOutstanding.getText().toString(), (widthSize / 2) + 150, y, paint);
                y = y + 30;
            }

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
                    case Constants.TodayPrimaryOrderDetails_List:
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
            billnumber.setText("Order " + Shared_Common_Pref.TransSlNo);
            Order_Outlet_Filter = new ArrayList<>();
            Order_Outlet_Filter.clear();

            int total_qtytext = 0;
            subTotalVal = 0.00;
            JSONArray arr = new JSONArray(response);
            taxList = new ArrayList<>();
            taxList.clear();

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                total_qtytext += obj.getInt("Quantity");
                subTotalVal += obj.getDouble("value");
                String paidAmt = "0";
                try {
                    paidAmt = sharedCommonPref.getvalue(Constants.FLAG).equals("Primary Order") ? "0" : obj.getString("PaidAmount");
                } catch (Exception e) {
                }

                JSONArray taxArr = obj.getJSONArray("TAX_details");
                for (int tax = 0; tax < taxArr.length(); tax++) {
                    JSONObject taxObj = taxArr.getJSONObject(tax);
                    String label = taxObj.getString("Tax_Name");
                    Double amt = taxObj.getDouble("Tax_Amt");
                    if (taxList.size() == 0) {
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
                Order_Outlet_Filter.add(new Product_Details_Modal(obj.getString("Product_Code"), obj.getString("Product_Name"), 1, "1",
                        "1", "5", "i", 7.99, 1.8, obj.getDouble("Rate"),
                        obj.getInt("Quantity"), obj.getInt("qty"), obj.getDouble("value"), taxList, paidAmt));


            }
            totalqty.setText("" + String.valueOf(total_qtytext));
            totalitem.setText("" + Order_Outlet_Filter.size());
            subtotal.setText("₹" + formatter.format(subTotalVal));
            netamount.setText("₹ " + formatter.format(subTotalVal));

            tvPaidAmt.setText("₹ " + formatter.format(Double.parseDouble(Order_Outlet_Filter.get(0).getPaidAmount())));

            sharedCommonPref.save(Constants.INVOICE_ORDERLIST, response);
            mReportViewAdapter = new Print_Invoice_Adapter(Print_Invoice_Activity.this, arr);
            mReportViewAdapter = new Print_Invoice_Adapter(Print_Invoice_Activity.this, arr);
            printrecyclerview.setAdapter(mReportViewAdapter);

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

        } catch (Exception e) {
            Log.e("PRINT:getData ", e.getMessage());
        }
    }
}
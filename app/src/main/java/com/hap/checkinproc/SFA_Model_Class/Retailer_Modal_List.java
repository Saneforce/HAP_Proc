package com.hap.checkinproc.SFA_Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Retailer_Modal_List {
    @SerializedName("id")
    @Expose
    private String id;

    public String getStatusname() {
        return Statusname;
    }

    public void setStatusname(String statusname) {
        Statusname = statusname;
    }

    public String getValuesinv() {
        return Valuesinv;
    }

    public void setValuesinv(String values) {
        Valuesinv = values;
    }

    public String getInvoiceValues() {
        return InvoiceValues;
    }

    public void setInvoiceValues(String invoiceValues) {
        InvoiceValues = invoiceValues;
    }

    public String getInvoiceDate() {
        return InvoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        InvoiceDate = invoiceDate;
    }

    public String get_long() {
        return _long;
    }

    public void set_long(String _long) {
        this._long = _long;
    }


    public Retailer_Modal_List(String id, String name, String statusname, String Valuesinv, String invoiceValues, String invoiceDate, String townCode, String townName, String lat, String _long, String addrs, String listedDrAddress1, Object listedDrSlNo, String mobileNumber, Integer docCatCode, String contactPersion, Integer docSpecialCode) {
        this.id = id;
        this.name = name;
        Statusname = statusname;
        Valuesinv = Valuesinv;
        InvoiceValues = invoiceValues;
        InvoiceDate = invoiceDate;
        this.townCode = townCode;
        this.townName = townName;
        this.lat = lat;
        this._long = _long;
        this.addrs = addrs;
        this.listedDrAddress1 = listedDrAddress1;
        this.listedDrSlNo = listedDrSlNo;
        this.mobileNumber = mobileNumber;
        this.docCatCode = docCatCode;
        this.contactPersion = contactPersion;
        this.docSpecialCode = docSpecialCode;
    }

    @SerializedName("name")
    @Expose
    private String name;


    @SerializedName("Distributor_Code")
    @Expose
    private String DistCode;

    public String getOwner_Name() {
        return Owner_Name;
    }

    public void setOwner_Name(String owner_Name) {
        Owner_Name = owner_Name;
    }

    public Retailer_Modal_List(String id, String name, String owner_Name, String statusname, String valuesinv, String invoiceValues, String invoiceDate, String townCode, String townName, String lat, String _long, String addrs, String listedDrAddress1, Object listedDrSlNo, String mobileNumber, Integer docCatCode, String contactPersion, Integer docSpecialCode, String invoice_Flag, String hatsun_AvailablityId, String listedDr_Email, String cityname, String compititor_Name, String compititor_Id, String lastUpdt_Date, String hatsanavail_Switch, String hatsanCategory_Switch, String reason_category, String category_Universe_Id) {
        this.id = id;
        this.name = name;
        Owner_Name = owner_Name;
        Statusname = statusname;
        Valuesinv = valuesinv;
        InvoiceValues = invoiceValues;
        InvoiceDate = invoiceDate;
        this.townCode = townCode;
        this.townName = townName;
        this.lat = lat;
        this._long = _long;
        this.addrs = addrs;
        this.listedDrAddress1 = listedDrAddress1;
        this.listedDrSlNo = listedDrSlNo;
        this.mobileNumber = mobileNumber;
        this.docCatCode = docCatCode;
        this.contactPersion = contactPersion;
        this.docSpecialCode = docSpecialCode;
        Invoice_Flag = invoice_Flag;
        Hatsun_AvailablityId = hatsun_AvailablityId;
        ListedDr_Email = listedDr_Email;
        this.cityname = cityname;
        Compititor_Name = compititor_Name;
        Compititor_Id = compititor_Id;
        LastUpdt_Date = lastUpdt_Date;
        Hatsanavail_Switch = hatsanavail_Switch;
        HatsanCategory_Switch = hatsanCategory_Switch;
        this.reason_category = reason_category;
        Category_Universe_Id = category_Universe_Id;
    }

    @SerializedName("Owner_Name")
    @Expose
    private String Owner_Name;
    @SerializedName("Statusname")
    @Expose
    private String Statusname;
    @SerializedName("Valuesinv")
    @Expose
    private String Valuesinv;
    @SerializedName("InvoiceValues")
    @Expose
    private String InvoiceValues;
    @SerializedName("InvoiceDate")
    @Expose
    private String InvoiceDate;
    @SerializedName("town_code")
    @Expose
    private String townCode;
    @SerializedName("town_name")
    @Expose
    private String townName;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("long")
    @Expose
    private String _long;
    @SerializedName("addrs")
    @Expose
    private String addrs;
    @SerializedName("ListedDr_Address1")
    @Expose
    private String listedDrAddress1;
    @SerializedName("ListedDr_Sl_No")
    @Expose
    private Object listedDrSlNo;
    @SerializedName("Mobile_Number")
    @Expose
    private String mobileNumber;
    @SerializedName("Doc_cat_code")
    @Expose
    private Integer docCatCode;
    @SerializedName("ContactPersion")
    @Expose
    private String contactPersion;
    @SerializedName("Doc_Special_Code")
    @Expose
    private Integer docSpecialCode;
    @SerializedName("Invoice_Flag")
    @Expose
    private String Invoice_Flag;
    @SerializedName("Hatsun_AvailablityId")
    @Expose
    private String Hatsun_AvailablityId;
    @SerializedName("ListedDr_Email")
    @Expose
    private String ListedDr_Email;
    @SerializedName("cityname")
    @Expose
    private String cityname;
    @SerializedName("Compititor_Name")
    @Expose
    private String Compititor_Name;

    @SerializedName("Compititor_Id")
    @Expose
    private String Compititor_Id;
    @SerializedName("LastUpdt_Date")
    @Expose
    private String LastUpdt_Date;
    @SerializedName("Hatsanavail_Switch")
    @Expose
    private String Hatsanavail_Switch;
    @SerializedName("HatsanCategory_Switch")
    @Expose
    private String HatsanCategory_Switch;


    @SerializedName("place_id")
    @Expose
    private String place_id = "ChIJ6fBt_tVnUjoRVxxz1mgBipI";


    public Retailer_Modal_List(String id, String name, String owner_Name, String statusname, String valuesinv, String invoiceValues, String invoiceDate, String townCode, String townName, String lat, String _long, String addrs, String listedDrAddress1, Object listedDrSlNo, String mobileNumber, Integer docCatCode, String contactPersion, Integer docSpecialCode, String invoice_Flag, String hatsun_AvailablityId, String listedDr_Email, String cityname, String compititor_Name, String compititor_Id, String lastUpdt_Date, String hatsanavail_Switch, String hatsanCategory_Switch, String pin_code, String gst, String reason_category, String category_Universe_Id) {
        this.id = id;
        this.name = name;
        Owner_Name = owner_Name;
        Statusname = statusname;
        Valuesinv = valuesinv;
        InvoiceValues = invoiceValues;
        InvoiceDate = invoiceDate;
        this.townCode = townCode;
        this.townName = townName;
        this.lat = lat;
        this._long = _long;
        this.addrs = addrs;
        this.listedDrAddress1 = listedDrAddress1;
        this.listedDrSlNo = listedDrSlNo;
        this.mobileNumber = mobileNumber;
        this.docCatCode = docCatCode;
        this.contactPersion = contactPersion;
        this.docSpecialCode = docSpecialCode;
        Invoice_Flag = invoice_Flag;
        Hatsun_AvailablityId = hatsun_AvailablityId;
        ListedDr_Email = listedDr_Email;
        this.cityname = cityname;
        Compititor_Name = compititor_Name;
        Compititor_Id = compititor_Id;
        LastUpdt_Date = lastUpdt_Date;
        Hatsanavail_Switch = hatsanavail_Switch;
        HatsanCategory_Switch = hatsanCategory_Switch;
        this.pin_code = pin_code;
        this.gst = gst;
        this.reason_category = reason_category;
        Category_Universe_Id = category_Universe_Id;
    }

    @SerializedName("pin_code")
    @Expose
    private String pin_code;

    public String getPin_code() {
        return pin_code;
    }

    public void setPin_code(String pin_code) {
        this.pin_code = pin_code;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    @SerializedName("gst")
    @Expose
    private String gst;

    public String getHatsanavail_Switch() {
        return Hatsanavail_Switch;
    }

    public void setHatsanavail_Switch(String hatsanavail_Switch) {
        Hatsanavail_Switch = hatsanavail_Switch;
    }

    public String getHatsanCategory_Switch() {
        return HatsanCategory_Switch;
    }

    public void setHatsanCategory_Switch(String hatsanCategory_Switch) {
        HatsanCategory_Switch = hatsanCategory_Switch;
    }

    public String getReason_category() {
        return reason_category;
    }

    public void setReason_category(String reason_category) {
        this.reason_category = reason_category;
    }

    public String getDistCode() {
        return DistCode;
    }

    public void setDistCode(String distCode) {
        DistCode = distCode;
    }

    @SerializedName("reason_category")
    @Expose
    private String reason_category;

    public String getLastUpdt_Date() {
        return LastUpdt_Date;
    }

    public void setLastUpdt_Date(String lastUpdt_Date) {
        LastUpdt_Date = lastUpdt_Date;
    }

    public Retailer_Modal_List(String id, String name, String statusname, String valuesinv, String invoiceValues, String invoiceDate, String townCode, String townName, String lat, String _long, String addrs, String listedDrAddress1, Object listedDrSlNo, String mobileNumber, Integer docCatCode, String contactPersion, Integer docSpecialCode, String invoice_Flag, String hatsun_AvailablityId, String listedDr_Email, String cityname, String compititor_Name, String compititor_Id, String category_Universe_Id) {
        this.id = id;
        this.name = name;
        Statusname = statusname;
        Valuesinv = valuesinv;
        InvoiceValues = invoiceValues;
        InvoiceDate = invoiceDate;
        this.townCode = townCode;
        this.townName = townName;
        this.lat = lat;
        this._long = _long;
        this.addrs = addrs;
        this.listedDrAddress1 = listedDrAddress1;
        this.listedDrSlNo = listedDrSlNo;
        this.mobileNumber = mobileNumber;
        this.docCatCode = docCatCode;
        this.contactPersion = contactPersion;
        this.docSpecialCode = docSpecialCode;
        Invoice_Flag = invoice_Flag;
        Hatsun_AvailablityId = hatsun_AvailablityId;
        ListedDr_Email = listedDr_Email;
        this.cityname = cityname;
        Compititor_Name = compititor_Name;
        Compititor_Id = compititor_Id;
        Category_Universe_Id = category_Universe_Id;
    }

    public String getCompititor_Name() {
        return Compititor_Name;
    }

    public void setCompititor_Name(String compititor_Name) {
        Compititor_Name = compititor_Name;
    }

    public String getCompititor_Id() {
        return Compititor_Id;
    }

    public void setCompititor_Id(String compititor_Id) {
        Compititor_Id = compititor_Id;
    }

    public String getListedDr_Email() {
        return ListedDr_Email;
    }

    public void setListedDr_Email(String listedDr_Email) {
        ListedDr_Email = listedDr_Email;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getHatsun_AvailablityId() {
        return Hatsun_AvailablityId;
    }

    public void setHatsun_AvailablityId(String hatsun_AvailablityId) {
        Hatsun_AvailablityId = hatsun_AvailablityId;
    }

    public String getCategory_Universe_Id() {
        return Category_Universe_Id;
    }

    public void setCategory_Universe_Id(String category_Universe_Id) {
        Category_Universe_Id = category_Universe_Id;
    }

    public Retailer_Modal_List(String id, String name, String statusname, String valuesinv, String invoiceValues, String invoiceDate, String townCode, String townName, String lat, String _long, String addrs, String listedDrAddress1, Object listedDrSlNo, String mobileNumber, Integer docCatCode, String contactPersion, Integer docSpecialCode, String invoice_Flag, String hatsun_AvailablityId, String category_Universe_Id) {
        this.id = id;
        this.name = name;
        this.Statusname = statusname;
        this.Valuesinv = valuesinv;
        this.InvoiceValues = invoiceValues;
        this.InvoiceDate = invoiceDate;
        this.Invoice_Flag = invoice_Flag;
        this.Hatsun_AvailablityId = hatsun_AvailablityId;
        this.Category_Universe_Id = category_Universe_Id;
        this.townCode = townCode;
        this.townName = townName;
        this.lat = lat;
        this._long = _long;
        this.addrs = addrs;
        this.listedDrAddress1 = listedDrAddress1;
        this.listedDrSlNo = listedDrSlNo;
        this.mobileNumber = mobileNumber;
        this.docCatCode = docCatCode;
        this.contactPersion = contactPersion;
        this.docSpecialCode = docSpecialCode;

    }

    @SerializedName("Category_Universe_Id")
    @Expose
    private String Category_Universe_Id;

    public String getInvoice_Flag() {
        return Invoice_Flag;
    }

    public void setInvoice_Flag(String invoice_Flag) {
        Invoice_Flag = invoice_Flag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTownCode() {
        return townCode;
    }

    public void setTownCode(String townCode) {
        this.townCode = townCode;
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLong() {
        return _long;
    }

    public void setLong(String _long) {
        this._long = _long;
    }

    public String getAddrs() {
        return addrs;
    }

    public void setAddrs(String addrs) {
        this.addrs = addrs;
    }

    public String getListedDrAddress1() {
        return listedDrAddress1;
    }

    public void setListedDrAddress1(String listedDrAddress1) {
        this.listedDrAddress1 = listedDrAddress1;
    }

    public Object getListedDrSlNo() {
        return listedDrSlNo;
    }

    public void setListedDrSlNo(Object listedDrSlNo) {
        this.listedDrSlNo = listedDrSlNo;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Integer getDocCatCode() {
        return docCatCode;
    }

    public void setDocCatCode(Integer docCatCode) {
        this.docCatCode = docCatCode;
    }

    public String getContactPersion() {
        return contactPersion;
    }

    public void setContactPersion(String contactPersion) {
        this.contactPersion = contactPersion;
    }

    public Integer getDocSpecialCode() {
        return docSpecialCode;
    }

    public void setDocSpecialCode(Integer docSpecialCode) {
        this.docSpecialCode = docSpecialCode;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }


}

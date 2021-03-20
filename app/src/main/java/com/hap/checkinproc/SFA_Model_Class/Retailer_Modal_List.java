package com.hap.checkinproc.SFA_Model_Class;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Retailer_Modal_List {
    @SerializedName("id")
    @Expose
    private String id;

    public Retailer_Modal_List(String id, String name, String townCode, String townName, String lat, String _long, String addrs, String listedDrAddress1, Object listedDrSlNo, String mobileNumber, Integer docCatCode, String contactPersion, Integer docSpecialCode) {
        this.id = id;
        this.name = name;
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

}
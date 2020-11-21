package com.hap.checkinproc.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RetailerViewDetails {

    @SerializedName("SVL")
    @Expose
    private String sVL;
    @SerializedName("DrCat")
    @Expose
    private String drCat;
    @SerializedName("DrSpl")
    @Expose
    private String drSpl;
    @SerializedName("DrCamp")
    @Expose
    private String drCamp;
    @SerializedName("DrProd")
    @Expose
    private String drProd;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("PreviousOrder")
    @Expose
    private List<Object> previousOrder = null;
    @SerializedName("StockistDetails")
    @Expose
    private Object stockistDetails;
    @SerializedName("OpeningStock")
    @Expose
    private List<Object> openingStock = null;
    @SerializedName("MOQ")
    @Expose
    private List<Object> mOQ = null;
    @SerializedName("POTENTIAL")
    @Expose
    private List<POTENTIAL> pOTENTIAL = null;
    @SerializedName("MOV")
    @Expose
    private List<MOV> mOV = null;
    @SerializedName("LVDt")
    @Expose
    private String lVDt;
    @SerializedName("CallFd")
    @Expose
    private String callFd;
    @SerializedName("Rmks")
    @Expose
    private String rmks;
    @SerializedName("ProdSmp")
    @Expose
    private String prodSmp;
    @SerializedName("Prodgvn")
    @Expose
    private String prodgvn;
    @SerializedName("DrGft")
    @Expose
    private String drGft;

    public String getSVL() {
        return sVL;
    }

    public void setSVL(String sVL) {
        this.sVL = sVL;
    }

    public String getDrCat() {
        return drCat;
    }

    public void setDrCat(String drCat) {
        this.drCat = drCat;
    }

    public String getDrSpl() {
        return drSpl;
    }

    public void setDrSpl(String drSpl) {
        this.drSpl = drSpl;
    }

    public String getDrCamp() {
        return drCamp;
    }

    public void setDrCamp(String drCamp) {
        this.drCamp = drCamp;
    }

    public String getDrProd() {
        return drProd;
    }

    public void setDrProd(String drProd) {
        this.drProd = drProd;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Object> getPreviousOrder() {
        return previousOrder;
    }

    public void setPreviousOrder(List<Object> previousOrder) {
        this.previousOrder = previousOrder;
    }

    public Object getStockistDetails() {
        return stockistDetails;
    }

    public void setStockistDetails(Object stockistDetails) {
        this.stockistDetails = stockistDetails;
    }

    public List<Object> getOpeningStock() {
        return openingStock;
    }

    public void setOpeningStock(List<Object> openingStock) {
        this.openingStock = openingStock;
    }

    public List<Object> getMOQ() {
        return mOQ;
    }

    public void setMOQ(List<Object> mOQ) {
        this.mOQ = mOQ;
    }

    public List<POTENTIAL> getPOTENTIAL() {
        return pOTENTIAL;
    }

    public void setPOTENTIAL(List<POTENTIAL> pOTENTIAL) {
        this.pOTENTIAL = pOTENTIAL;
    }

    public List<MOV> getMOV() {
        return mOV;
    }

    public void setMOV(List<MOV> mOV) {
        this.mOV = mOV;
    }

    public String getLVDt() {
        return lVDt;
    }

    public void setLVDt(String lVDt) {
        this.lVDt = lVDt;
    }

    public String getCallFd() {
        return callFd;
    }

    public void setCallFd(String callFd) {
        this.callFd = callFd;
    }

    public String getRmks() {
        return rmks;
    }

    public void setRmks(String rmks) {
        this.rmks = rmks;
    }

    public String getProdSmp() {
        return prodSmp;
    }

    public void setProdSmp(String prodSmp) {
        this.prodSmp = prodSmp;
    }

    public String getProdgvn() {
        return prodgvn;
    }

    public void setProdgvn(String prodgvn) {
        this.prodgvn = prodgvn;
    }

    public String getDrGft() {
        return drGft;
    }

    public void setDrGft(String drGft) {
        this.drGft = drGft;
    }

}

package com.hap.checkinproc.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("Sf_UserName")
    @Expose
    private String sfUserName;
    @SerializedName("SF_Status")
    @Expose
    private Integer sFStatus;
    @SerializedName("Sf_Name")
    @Expose
    private String sfName;
    @SerializedName("Sf_Password")
    @Expose
    private String sfPassword;
    @SerializedName("Division_Code")
    @Expose
    private String divisionCode;
    @SerializedName("DisRad")
    @Expose
    private Double disRad;
    @SerializedName("Sf_code")
    @Expose
    private String sfCode;
    @SerializedName("CheckCount")
    @Expose
    private Integer checkCount;
    @SerializedName("Geo_Fencing")
    @Expose
    private Integer geoFencing;
    @SerializedName("SFFType")
    @Expose
    private Integer sFFType;
    @SerializedName("OTFlg")
    @Expose
    private Integer oTFlg;
    @SerializedName("HOLocation")
    @Expose
    private String hOLocation;

    public String getSfUserName() {
        return sfUserName;
    }

    public void setSfUserName(String sfUserName) {
        this.sfUserName = sfUserName;
    }

    public Integer getSFStatus() {
        return sFStatus;
    }

    public void setSFStatus(Integer sFStatus) {
        this.sFStatus = sFStatus;
    }

    public String getSfName() {
        return sfName;
    }

    public void setSfName(String sfName) {
        this.sfName = sfName;
    }

    public String getSfPassword() {
        return sfPassword;
    }

    public void setSfPassword(String sfPassword) {
        this.sfPassword = sfPassword;
    }

    public String getDivisionCode() {
        return divisionCode;
    }

    public void setDivisionCode(String divisionCode) {
        this.   divisionCode = divisionCode;
    }

    public Double getDisRad() {
        return disRad;
    }

    public void setDisRad(Double disRad) {
        this.disRad = disRad;
    }

    public String getSfCode() {
        return sfCode;
    }

    public void setSfCode(String sfCode) {
        this.sfCode = sfCode;
    }

    public Integer getCheckCount() {
        return checkCount;
    }

    public void setCheckCount(Integer checkCount) {
        this.checkCount = checkCount;
    }

    public Integer getGeoFencing() {
        return geoFencing;
    }

    public void setGeoFencing(Integer geoFencing) {
        this.geoFencing = geoFencing;
    }

    public Integer getSFFType() {
        return sFFType;
    }

    public void setSFFType(Integer sFFType) {
        this.sFFType = sFFType;
    }

    public Integer getOTFlg() {
        return oTFlg;
    }

    public void setOTFlg(Integer oTFlg) {
        this.oTFlg = oTFlg;
    }

    public String getHOLocation() {
        return hOLocation;
    }

    public void setHOLocation(String hOLocation) {
        this.hOLocation = hOLocation;
    }

}
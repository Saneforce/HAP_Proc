package com.hap.checkinproc.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tp_Approval_FF_Modal {
    @SerializedName("Monthnameexample")
    @Expose
    private String monthnameexample;
    @SerializedName("Tmonth")
    @Expose
    private String tmonth;
    @SerializedName("Sf_Code")
    @Expose
    private String sfCode;
    @SerializedName("Tyear")
    @Expose
    private String tyear;
    @SerializedName("FieldForceName")
    @Expose
    private String fieldForceName;

    public String getMonthnameexample() {
        return monthnameexample;
    }

    public void setMonthnameexample(String monthnameexample) {
        this.monthnameexample = monthnameexample;
    }

    public String getTmonth() {
        return tmonth;
    }

    public void setTmonth(String tmonth) {
        this.tmonth = tmonth;
    }

    public String getSfCode() {
        return sfCode;
    }

    public void setSfCode(String sfCode) {
        this.sfCode = sfCode;
    }

    public String getTyear() {
        return tyear;
    }

    public void setTyear(String tyear) {
        this.tyear = tyear;
    }

    public String getFieldForceName() {
        return fieldForceName;
    }

    public void setFieldForceName(String fieldForceName) {
        this.fieldForceName = fieldForceName;
    }
}

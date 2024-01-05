package com.hap.checkinproc.SFA_Model_Class;

public class ModelPendingOutletsCategory {

    String sfCode;

    String sfName;

    String stockistCode;

    String stockistName;

    String listedDrCount;

    String designation;


    String erpCode;

    public ModelPendingOutletsCategory(String sfCode, String sfName, String stockistCode, String stockistName, String listedDrCount, String designation,String erpCode) {
        this.sfCode = sfCode;
        this.sfName = sfName;
        this.stockistCode = stockistCode;
        this.stockistName = stockistName;
        this.listedDrCount = listedDrCount;
        this.designation = designation;
        this.erpCode=erpCode;
    }

    public String getSfCode() {
        return sfCode;
    }

    public void setSfCode(String sfCode) {
        this.sfCode = sfCode;
    }

    public String getSfName() {
        return sfName;
    }

    public void setSfName(String sfName) {
        this.sfName = sfName;
    }

    public String getStockistCode() {
        return stockistCode;
    }

    public void setStockistCode(String stockistCode) {
        this.stockistCode = stockistCode;
    }

    public String getStockistName() {
        return stockistName;
    }

    public void setStockistName(String stockistName) {
        this.stockistName = stockistName;
    }

    public String getListedDrCount() {
        return listedDrCount;
    }

    public void setListedDrCount(String listedDrCount) {
        this.listedDrCount = listedDrCount;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
    public String getErpCode() {return erpCode;}

    public void setErpCode(String ERP_Code) {this.erpCode = ERP_Code;}

}

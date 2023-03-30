package com.hap.checkinproc.SFA_Model_Class;

public class SalesReturnProductModel {
    String productName, materialCode, MRP, rate, invUOM, invQty, retUOM, retQty, retType;

    public SalesReturnProductModel(String productName, String materialCode, String MRP, String rate, String invUOM, String invQty, String retUOM, String retQty, String retType) {
        this.productName = productName;
        this.materialCode = materialCode;
        this.MRP = MRP;
        this.rate = rate;
        this.invUOM = invUOM;
        this.invQty = invQty;
        this.retUOM = retUOM;
        this.retQty = retQty;
        this.retType = retType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMRP() {
        return MRP;
    }

    public void setMRP(String MRP) {
        this.MRP = MRP;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getInvUOM() {
        return invUOM;
    }

    public void setInvUOM(String invUOM) {
        this.invUOM = invUOM;
    }

    public String getInvQty() {
        return invQty;
    }

    public void setInvQty(String invQty) {
        this.invQty = invQty;
    }

    public String getRetUOM() {
        return retUOM;
    }

    public void setRetUOM(String retUOM) {
        this.retUOM = retUOM;
    }

    public String getRetQty() {
        return retQty;
    }

    public void setRetQty(String retQty) {
        this.retQty = retQty;
    }

    public String getRetType() {
        return retType;
    }

    public void setRetType(String retType) {
        this.retType = retType;
    }
}

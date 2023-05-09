package com.hap.checkinproc.SFA_Model_Class;

import java.util.ArrayList;

public class SalesReturnProductModel {
    String Product_Code, productName, materialCode, invUOM, retUOM, retType;
    double MRP, rate, invConvFac, retConvFac, retTotal, retTax;
    int invQty, retQty;
    ArrayList<UOMModel> uomList;
    ArrayList<TaxModel> taxList;

    public SalesReturnProductModel(String product_Code, String productName, String materialCode, String invUOM, String retUOM, String retType, double MRP, double rate, double invConvFac, double retConvFac, double retTotal, double retTax, int invQty, int retQty, ArrayList<UOMModel> uomList, ArrayList<TaxModel> taxList) {
        this.Product_Code = product_Code;
        this.productName = productName;
        this.materialCode = materialCode;
        this.invUOM = invUOM;
        this.retUOM = retUOM;
        this.retType = retType;
        this.MRP = MRP;
        this.rate = rate;
        this.invConvFac = invConvFac;
        this.retConvFac = retConvFac;
        this.retTotal = retTotal;
        this.retTax = retTax;
        this.invQty = invQty;
        this.retQty = retQty;
        this.uomList = uomList;
        this.taxList = taxList;
    }

    public String getProduct_Code() {
        return Product_Code;
    }

    public void setProduct_Code(String product_Code) {
        this.Product_Code = product_Code;
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

    public String getInvUOM() {
        return invUOM;
    }

    public void setInvUOM(String invUOM) {
        this.invUOM = invUOM;
    }

    public String getRetUOM() {
        return retUOM;
    }

    public void setRetUOM(String retUOM) {
        this.retUOM = retUOM;
    }

    public String getRetType() {
        return retType;
    }

    public void setRetType(String retType) {
        this.retType = retType;
    }

    public double getMRP() {
        return MRP;
    }

    public void setMRP(double MRP) {
        this.MRP = MRP;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getInvConvFac() {
        return invConvFac;
    }

    public void setInvConvFac(double invConvFac) {
        this.invConvFac = invConvFac;
    }

    public double getRetConvFac() {
        return retConvFac;
    }

    public void setRetConvFac(double retConvFac) {
        this.retConvFac = retConvFac;
    }

    public double getRetTotal() {
        return retTotal;
    }

    public void setRetTotal(double retTotal) {
        this.retTotal = retTotal;
    }

    public double getRetTax() {
        return retTax;
    }

    public void setRetTax(double retTax) {
        this.retTax = retTax;
    }

    public int getInvQty() {
        return invQty;
    }

    public void setInvQty(int invQty) {
        this.invQty = invQty;
    }

    public int getRetQty() {
        return retQty;
    }

    public void setRetQty(int retQty) {
        this.retQty = retQty;
    }

    public ArrayList<UOMModel> getUomList() {
        return uomList;
    }

    public void setUomList(ArrayList<UOMModel> uomList) {
        this.uomList = uomList;
    }

    public ArrayList<TaxModel> getTaxList() {
        return taxList;
    }

    public void setTaxList(ArrayList<TaxModel> taxList) {
        this.taxList = taxList;
    }
}
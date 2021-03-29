package com.hap.checkinproc.SFA_Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product_Details_Modal {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("Product_Cat_Code")
    @Expose
    private Integer productCatCode;
    @SerializedName("row_num")
    @Expose
    private String rowNum;
    @SerializedName("Product_Sale_Unit")
    @Expose
    private String productSaleUnit;
    @SerializedName("product_unit")
    @Expose
    private String productUnit;
    @SerializedName("Unit_code")
    @Expose
    private String unitCode;
    @SerializedName("Default_UOMQty")
    @Expose
    private Double defaultUOMQty;
    @SerializedName("Default_UOM")
    @Expose
    private Double defaultUOM;
    @SerializedName("Rate")
    @Expose
    private Double Rate;
    @SerializedName("Amount")
    @Expose
    private Double Amount;
    @SerializedName("Qty")
    @Expose
    private Integer Qty;
    @SerializedName("RegularQty")
    @Expose
    private Integer RegularQty;

    public Product_Details_Modal(String id, String name, Integer productCatCode, String rowNum, String productSaleUnit, String productUnit, String unitCode, Double defaultUOMQty, Double defaultUOM, Double Rate, Integer Qty, Integer RegularQty, Double Amount) {
        this.id = id;
        this.name = name;
        this.productCatCode = productCatCode;
        this.rowNum = rowNum;
        this.productSaleUnit = productSaleUnit;
        this.productUnit = productUnit;
        this.unitCode = unitCode;
        this.defaultUOMQty = defaultUOMQty;
        this.defaultUOM = defaultUOM;
        this.Rate = Rate;
        this.Qty = Qty;
        this.RegularQty = RegularQty;
        this.Amount = Amount;
    }

    public Integer getRegularQty() {
        return RegularQty;
    }

    public void setRegularQty(Integer regularQty) {
        RegularQty = regularQty;
    }

    public void setAmount(Double amount) {
        Amount = amount;
    }

    public Double getAmount() {
        return Amount;
    }

    public void setQty(Integer qty) {
        Qty = qty;
    }

    public Integer getQty() {
        return Qty;
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

    public Double getRate() {
        return Rate;
    }

    public void setRate(Double rate) {
        Rate = rate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getProductCatCode() {
        return productCatCode;
    }

    public void setProductCatCode(Integer productCatCode) {
        this.productCatCode = productCatCode;
    }

    public String getRowNum() {
        return rowNum;
    }

    public void setRowNum(String rowNum) {
        this.rowNum = rowNum;
    }

    public String getProductSaleUnit() {
        return productSaleUnit;
    }

    public void setProductSaleUnit(String productSaleUnit) {
        this.productSaleUnit = productSaleUnit;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public Double getDefaultUOMQty() {
        return defaultUOMQty;
    }

    public void setDefaultUOMQty(Double defaultUOMQty) {
        this.defaultUOMQty = defaultUOMQty;
    }

    public Double getDefaultUOM() {
        return defaultUOM;
    }

    public void setDefaultUOM(Double defaultUOM) {
        this.defaultUOM = defaultUOM;
    }
}

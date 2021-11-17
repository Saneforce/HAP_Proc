package com.hap.checkinproc.SFA_Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Product_Details_Modal {
    @SerializedName("Package")
    @Expose
    private String Package;
    @SerializedName("MRP")
    @Expose
    private String MRP;
    @SerializedName("POP_UOM")
    @Expose
    private String UOM;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;

    private String sku;
    private double price;
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

    public String getERP_Code() {
        return ERP_Code;
    }

    public void setERP_Code(String ERP_Code) {
        this.ERP_Code = ERP_Code;
    }

    @SerializedName("ERP_Code")
    @Expose
    private String ERP_Code;

    @SerializedName("Default_UOMQty")
    @Expose
    private Double defaultUOMQty;
    @SerializedName("Default_UOM")
    @Expose
    private Double defaultUOM;
    @SerializedName("Rate")
    @Expose
    private Double Rate;

    @SerializedName("SBRate")
    @Expose
    private Double SBRate;

    @SerializedName("Amount")
    @Expose
    private Double Amount;
    @SerializedName("Qty")
    @Expose
    private Integer Qty;

    @SerializedName("PImage")
    @Expose
    private String PImage;

    @SerializedName("ConversionFactor")
    @Expose
    private String ConversionFactor;

    @SerializedName("CGST")
    @Expose
    private Double CGST;
    @SerializedName("SGST")
    @Expose
    private Double SGST;
    private List<Product_Details_Modal> productDetailsModal;


    public String getMRP() {
        return MRP;
    }

    public void setMRP(String MRP) {
        this.MRP = MRP;
    }


    public Double getCGST() {
        return CGST;
    }

    public void setCGST(Double CGST) {
        this.CGST = CGST;
    }


    public Double getSGST() {
        return SGST;
    }

    public void setSGST(Double SGST) {
        this.SGST = SGST;
    }

    public Double getIGST() {
        return IGST;
    }

    public void setIGST(Double IGST) {
        this.IGST = IGST;
    }

    @SerializedName("IGST")
    @Expose
    private Double IGST;


    @SerializedName("free")
    @Expose
    private String free;

    public String getFree_val() {
        return free_val;
    }

    public void setFree_val(String free_val) {
        this.free_val = free_val;
    }

    @SerializedName("free_val")
    @Expose
    private String free_val;


    @SerializedName("discount")
    @Expose
    private double discount;


    @SerializedName("discount_value")
    @Expose
    private String discount_value;


    public String getDiscount_type() {
        return discount_type;
    }

    public void setDiscount_type(String discount_type) {
        this.discount_type = discount_type;
    }

    @SerializedName("discount_type")
    @Expose
    private String discount_type;


    @SerializedName("Scheme")
    @Expose
    private String scheme;

    @SerializedName("RegularQty")
    @Expose
    private Integer RegularQty;


    @SerializedName("bookingDate")
    @Expose
    private String bookingDate;

    @SerializedName("popMaterial")
    @Expose
    private String popMaterial;

    @SerializedName("tax")
    @Expose
    private String tax;
    @SerializedName("tax_value")
    @Expose
    private String tax_value;
    @SerializedName("Off_Pro_code")
    @Expose
    private String Off_Pro_code;
    @SerializedName("Off_Pro_name")
    @Expose
    private String Off_Pro_name;

    @SerializedName("Tax_Type")
    @Expose
    private String Tax_Type;

    @SerializedName("Tax_Id")
    @Expose
    private String Tax_Id;

    @SerializedName("Tax_Val")
    @Expose
    private double Tax_Val;


    public String getTax_Id() {
        return Tax_Id;
    }

    public void setTax_Id(String tax_Id) {
        Tax_Id = tax_Id;
    }

    public double getTax_Amt() {
        return Tax_Amt;
    }

    public void setTax_Amt(double tax_Amt) {
        Tax_Amt = tax_Amt;
    }

    private double Tax_Amt;

    public String getDiscount_value() {
        return discount_value;
    }

    public void setDiscount_value(String discount_value) {
        this.discount_value = discount_value;
    }

    public String getPImage() {
        return PImage;
    }

    public void setPImage(String PImage) {
        this.PImage = PImage;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getTax_value() {
        return tax_value;
    }

    public void setTax_value(String tax_value) {
        this.tax_value = tax_value;
    }

    public String getOff_Pro_code() {
        return Off_Pro_code;
    }

    public void setOff_Pro_code(String off_Pro_code) {
        Off_Pro_code = off_Pro_code;
    }

    public String getOff_Pro_name() {
        return Off_Pro_name;
    }

    public void setOff_Pro_name(String off_Pro_name) {
        Off_Pro_name = off_Pro_name;
    }

    public String getOff_Pro_Unit() {
        return Off_Pro_Unit;
    }

    public void setOff_Pro_Unit(String off_Pro_Unit) {
        Off_Pro_Unit = off_Pro_Unit;
    }

    @SerializedName("Off_Pro_Unit")
    @Expose
    private String Off_Pro_Unit;
    @SerializedName("PaidAmount")
    @Expose
    private String PaidAmount;


    public Product_Details_Modal(String id, String name, Integer productCatCode, String rowNum, String productSaleUnit, String productUnit,
                                 String unitCode, Double defaultUOMQty, Double defaultUOM, Double Rate, Integer Qty,
                                 Integer RegularQty, Double Amount,List<Product_Details_Modal> productDetailsModal,String PaidAmount) {
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
        this.productDetailsModal=productDetailsModal;
        this.PaidAmount=PaidAmount;
    }

    public Product_Details_Modal(String id, String name, String sku, double price, int Qty, double amount, String scheme) {
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.price = price;
        this.Qty = Qty;
        this.scheme = scheme;
        this.Amount = amount;
    }


    public List<Product_Details_Modal> getProductDetailsModal() {
        return productDetailsModal;
    }

    public void setProductDetailsModal(List<Product_Details_Modal> productDetailsModal) {
        this.productDetailsModal = productDetailsModal;
    }

    public Product_Details_Modal(String Tax_Id, String Tax_Type, double Tax_Val, double Tax_Amt) {
        this.Tax_Val = Tax_Val;
        this.Tax_Type = Tax_Type;
        this.Tax_Id = Tax_Id;
        this.Tax_Amt = Tax_Amt;

    }

    public Product_Details_Modal(String Tax_Type, double Tax_Amt) {

        this.Tax_Type = Tax_Type;

        this.Tax_Amt = Tax_Amt;

    }

    public Product_Details_Modal(String id, String name, String bookingDate, int Qty, String UOM) {
        this.id = id;
        this.name = name;
        this.bookingDate = bookingDate;
        this.Qty = Qty;
        this.UOM = UOM;

    }

    public Product_Details_Modal(String id, String scheme, String free, double discount, String discount_type, String Package
            , String tax, String off_Pro_code, String off_Pro_name, String off_Pro_Unit) {
        this.id = id;
        this.scheme = scheme;
        this.free = free;
        this.discount = discount;
        this.discount_type = discount_type;
        this.Package = Package;
        this.tax = tax;
        this.Off_Pro_code = off_Pro_code;
        this.Off_Pro_name = off_Pro_name;
        this.Off_Pro_Unit = off_Pro_Unit;

    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }


    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getPopMaterial() {
        return popMaterial;
    }

    public void setPopMaterial(String popMaterial) {
        this.popMaterial = popMaterial;
    }


    public String getFree() {
        return free;
    }

    public void setFree(String free) {
        this.free = free;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getUOM() {
        return UOM;
    }

    public void setUOM(String UOM) {
        this.UOM = UOM;
    }

    public String getPackage() {
        return Package;
    }

    public void setPackage(String mPackage) {
        Package = mPackage;
    }

    public double getTax_Val() {
        return Tax_Val;
    }

    public void setTax_Val(double tax_Val) {
        Tax_Val = tax_Val;
    }

    public String getTax_Type() {
        return Tax_Type;
    }

    public void setTax_Type(String tax_Type) {
        Tax_Type = tax_Type;
    }

    public Double getSBRate() {
        return SBRate;
    }

    public void setSBRate(Double SBRate) {
        this.SBRate = SBRate;
    }

    public String getConversionFactor() {
        return ConversionFactor;
    }

    public void setConversionFactor(String conversionFactor) {
        ConversionFactor = conversionFactor;
    }

    public String getPaidAmount() {
        return PaidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        PaidAmount = paidAmount;
    }
}

package com.hap.checkinproc.SFA_Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Product_Details_Modal {

    @SerializedName("Package")
    @Expose
    private String Package;

    @SerializedName("Bar_Code")
    @Expose
    private String Bar_Code;
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

    @SerializedName("Product_Grp_Code")
    @Expose
    private Integer Product_Grp_Code;

    @SerializedName("RateEdit")
    @Expose
    private Integer RateEdit;
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

    private String plant = "";
    private String plantId = "";

    private String saveMode = "";
    private int cr;
    private int Dr;

    public int getCr() {
        return cr;
    }

    public void setCr(int cr) {
        this.cr = cr;
    }

    public int getDr() {
        return Dr;
    }

    public void setDr(int dr) {
        Dr = dr;
    }

    public int getBal() {
        return Bal;
    }

    public void setBal(int bal) {
        Bal = bal;
    }

    public double getCrAmt() {
        return crAmt;
    }

    public void setCrAmt(double crAmt) {
        this.crAmt = crAmt;
    }

    public double getDrAmt() {
        return DrAmt;
    }

    public void setDrAmt(double drAmt) {
        DrAmt = drAmt;
    }

    private int Bal;
    private double crAmt,DrAmt,BalAmt;

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
    private double defaultUOMQty;
    @SerializedName("Default_UOM")
    @Expose
    private String defaultUOM;

    @SerializedName("Default_UOM_Name")
    @Expose
    private String Default_UOM_Name;

    @SerializedName("Rate")
    @Expose
    private Double Rate;

    public Double getBillRate() {
        return BillRate;
    }

    @SerializedName("BillRate")
    @Expose
    private Double BillRate;

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


    public int getOnHand() {
        return onHand;
    }

    public void setOnHand(int onHold) {
        this.onHand = onHold;
    }

    public String getMATNR() {
        return MATNR;
    }

    public void setMATNR(String MATNR) {
        this.MATNR = MATNR;
    }

    //    @SerializedName("ConversionFactor")
//    @Expose
    private int onHand;
    //    @SerializedName("ConversionFactor")
//    @Expose
    private String MATNR;

    public String getSA_UOM() {
        return SA_UOM;
    }

    public void setSA_UOM(String SA_UOM) {
        this.SA_UOM = SA_UOM;
    }

    private String SA_UOM;


    @SerializedName("CGST")
    @Expose
    private Double CGST;
    @SerializedName("SGST")
    @Expose
    private Double SGST;

    @SerializedName("Balance")
    @Expose
    private Integer Balance;

    @SerializedName("CheckStock")
    @Expose
    private Integer CheckStock;


    @SerializedName("Multiple_UOM")
    @Expose
    private String Multiple_UOM;

    @SerializedName("Multiple_Qty")
    @Expose
    private Integer Multiple_Qty;

    @SerializedName("UOMList")
    @Expose
    private List<UOM> UOMList = new ArrayList<>();
    private List<Product_Details_Modal> productDetailsModal = new ArrayList<>();

    private String UOM_Id;
    private String UOM_Nm;

    public Product_Details_Modal(double cnvQty, String UOM_Nm) {
        this.CnvQty = cnvQty;
        this.UOM_Nm = UOM_Nm;
    }

    // "Cr": 23,
//         "Dr": 0,
//         "Bal": 23
    public Product_Details_Modal(String id, int cr, int Dr, int Balance) {
        this.id = id;
        this.cr = cr;
        this.Dr = Dr;
        this.Balance = Balance;
    }

    public Product_Details_Modal(String UOM_Nm, int Qty, int Multiple_Qty) {
        this.UOM_Nm = UOM_Nm;
        this.Qty = Qty;
        this.Multiple_Qty = Multiple_Qty;
    }

    private String mfg = "";
    private String exp = "";
    private String remarks = "";
    private String batchNo = "";

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

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
    @SerializedName("orderQty")
    @Expose
    private Integer orderQty;


    @SerializedName("bookingDate")
    @Expose
    private String bookingDate;

    @SerializedName("popMaterial")
    @Expose
    private String popMaterial;

    @SerializedName("tax")
    @Expose
    private double tax;

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

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
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
    private double CnvQty;


    public Product_Details_Modal(String id, String name, Integer productCatCode, String rowNum, String productSaleUnit, String productUnit,
                                 String unitCode, double defaultUOMQty, String defaultUOM, Double Rate, Integer Qty,
                                 Integer RegularQty, Double Amount, List<Product_Details_Modal> productDetailsModal, String PaidAmount, double tax) {
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
        this.productDetailsModal = productDetailsModal;
        this.PaidAmount = PaidAmount;
        this.tax = tax;
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
            , double tax, String off_Pro_code, String off_Pro_name, String off_Pro_Unit) {
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

    public double getDefaultUOMQty() {
        return defaultUOMQty;
    }

    public void setDefaultUOMQty(double defaultUOMQty) {
        this.defaultUOMQty = defaultUOMQty;
    }

    public String getDefaultUOM() {
        return defaultUOM;
    }

    public void setDefaultUOM(String defaultUOM) {
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

    public String getBar_Code() {
        return Bar_Code;
    }

    public void setBar_Code(String bar_Code) {
        Bar_Code = bar_Code;
    }

    public List<UOM> getUOMList() {
        return UOMList;
    }

    public void setUOMList(List<UOM> UOMList) {
        this.UOMList = UOMList;
    }

    public String getUOM_Id() {
        return UOM_Id;
    }

    public void setUOM_Id(String UOM_Id) {
        this.UOM_Id = UOM_Id;
    }

    public String getUOM_Nm() {
        return UOM_Nm;
    }

    public void setUOM_Nm(String UOM_Nm) {
        this.UOM_Nm = UOM_Nm;
    }

    public String getDefault_UOM_Name() {
        return Default_UOM_Name;
    }

    public void setDefault_UOM_Name(String default_UOM_Name) {
        Default_UOM_Name = default_UOM_Name;
    }

    public double getCnvQty() {
        return CnvQty;
    }

    public void setCnvQty(double cnvQty) {
        CnvQty = cnvQty;
    }

    public Integer getBalance() {
        return Balance;
    }

    public void setBalance(Integer balance) {
        Balance = balance;
    }

    public String getMfg() {
        return mfg;
    }

    public void setMfg(String mfg) {
        this.mfg = mfg;
    }

    public Integer getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(Integer orderQty) {
        this.orderQty = orderQty;
    }

    public Integer getCheckStock() {
        return CheckStock;
    }

    public void setCheckStock(Integer checkStock) {
        CheckStock = checkStock;
    }

    public String getMultiple_UOM() {
        return Multiple_UOM;
    }

    public void setMultiple_UOM(String multiple_UOM) {
        Multiple_UOM = multiple_UOM;
    }

    public Integer getMultiple_Qty() {
        return Multiple_Qty;
    }

    public void setMultiple_Qty(Integer multiple_Qty) {
        Multiple_Qty = multiple_Qty;
    }

    public Integer getProduct_Grp_Code() {
        return Product_Grp_Code;
    }

    public void setProduct_Grp_Code(Integer product_Grp_Code) {
        Product_Grp_Code = product_Grp_Code;
    }

    public Integer getRateEdit() {
        return RateEdit;
    }

    public void setRateEdit(Integer rateEdit) {
        RateEdit = rateEdit;
    }

    public String getPlant() {
        return plant;
    }

    public void setPlant(String plant) {
        this.plant = plant;
    }

    public String getPlantId() {
        return plantId;
    }

    public void setPlantId(String plantId) {
        this.plantId = plantId;
    }

    public String getSaveMode() {
        return saveMode;
    }

    public void setSaveMode(String saveMode) {
        this.saveMode = saveMode;
    }

    public double getBalAmt() {
        return BalAmt;
    }

    public void setBalAmt(double balAmt) {
        BalAmt = balAmt;
    }


    public class UOM {
        @SerializedName("CnvQty")
        @Expose
        private double CnvQty;

        @SerializedName("UOM_Id")
        @Expose
        private String UOM_Id;

        @SerializedName("UOM_Nm")
        @Expose
        private String UOM_Nm;

        private int enterQty;

        private float value;


        public String getUOM_Id() {
            return UOM_Id;
        }

        public void setUOM_Id(String UOM_Id) {
            this.UOM_Id = UOM_Id;
        }

        public String getUOM_Nm() {
            return UOM_Nm;
        }

        public void setUOM_Nm(String UOM_Nm) {
            this.UOM_Nm = UOM_Nm;
        }

        public double getCnvQty() {
            return CnvQty;
        }

        public void setCnvQty(double cnvQty) {
            CnvQty = cnvQty;
        }

        public int getEnterQty() {
            return enterQty;
        }

        public void setEnterQty(int enterQty) {
            this.enterQty = enterQty;
        }

        public float getValue() {
            return value;
        }

        public void setValue(float value) {
            this.value = value;
        }
    }
}

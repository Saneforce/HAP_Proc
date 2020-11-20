package com.hap.checkinproc.Model_Class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("Product_Cat_Code")
    @Expose
    private Integer productCatCode;

    private int mQuantity;


    public int getmQuantity() {
        return mQuantity;
    }

    public void setmQuantity(int mQuantity) {
        this.mQuantity = mQuantity;
    }


    public void addToQuantity() {
        this.mQuantity += 1;
    }

    public void removeFromQuantity() {
        if (this.mQuantity > 0) {
            this.mQuantity -= 1;
        }
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

    public void setName(String name) {
        this.name = name;
    }

    public Integer getProductCatCode() {
        return productCatCode;
    }

    public void setProductCatCode(Integer productCatCode) {
        this.productCatCode = productCatCode;
    }
}

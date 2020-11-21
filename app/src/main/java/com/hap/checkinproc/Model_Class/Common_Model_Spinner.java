package com.hap.checkinproc.Model_Class;

public class Common_Model_Spinner {

    public String name;
    public String address;

    public Common_Model_Spinner(String name, String address) {
        this.name = name;
        this.address = address;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

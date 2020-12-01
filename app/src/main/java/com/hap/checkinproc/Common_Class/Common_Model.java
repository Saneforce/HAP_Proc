package com.hap.checkinproc.Common_Class;

public class Common_Model {
    private String name;
    private String id;
    private String flag;
    private String address;
private String phone;

    public Common_Model(String name, String id, String flag, String address,String phone) {
        this.name = name;
        this.id = id;
        this.flag = flag;
        this.address = address;
        this.phone = phone;
    }

    public Common_Model(String id, String name, String flag) {
        this.id = id;
        this.name = name;
        this.flag = flag;
    }

    public Common_Model(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getFlag() {
        return flag;
    }
    public void setFlag(String flag) {
        this.flag = flag;
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
}

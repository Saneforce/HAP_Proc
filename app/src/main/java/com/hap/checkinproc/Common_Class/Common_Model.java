package com.hap.checkinproc.Common_Class;

public class Common_Model {
    private String name;
    private String id;
    private String flag;

    public Common_Model(String id,String name,  String flag) {
        this.id = id;
        this.name = name;
        this.flag = flag;
    }

    public Common_Model(String name, String id) {
        this.name = name;
        this.id = id;
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

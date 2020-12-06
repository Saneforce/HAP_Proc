package com.hap.checkinproc.Model_Class;

import android.net.Uri;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class EventCapture extends RealmObject {

    @PrimaryKey
    @Required
    public Integer id;
    public String imageUri;
    public String Title;
    public String remarks;
/*
    public EventCapture(int id, Uri imageUri, String title, String remarks) {
        this.id = id;
        this.imageUri = imageUri;
        Title = title;
        this.remarks = remarks;
    }*/

  /*  public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }*/
}

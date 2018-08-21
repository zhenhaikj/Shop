package com.emjiayuan.app.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Label implements Serializable{

    /**
     * id : 6
     * type : 晒图
     * status : 1
     * public : 1
     */

    private String id;
    private String type;
    private String status;
    @SerializedName("public")
    private String publicX;

    public Label(String id, String type, String status, String publicX) {
        this.id = id;
        this.type = type;
        this.status = status;
        this.publicX = publicX;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPublicX() {
        return publicX;
    }

    public void setPublicX(String publicX) {
        this.publicX = publicX;
    }
}

package com.emjiayuan.app.entity;

import java.io.Serializable;

public class Type implements Serializable{
    private String typename;
    private int i;
    private String type;

    public Type(int i, String s, String s1) {
        this.i=i;
        this.typename=s;
        this.type=s1;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }
}

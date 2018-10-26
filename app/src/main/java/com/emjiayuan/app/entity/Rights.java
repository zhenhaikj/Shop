package com.emjiayuan.app.entity;


public class Rights {


    private int drawable;
    private String name;
    private String detail;

    public Rights(int drawable, String name, String detail) {
        this.drawable = drawable;
        this.name = name;
        this.detail = detail;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}

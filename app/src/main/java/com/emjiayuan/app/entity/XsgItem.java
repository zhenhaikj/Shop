package com.emjiayuan.app.entity;

/**
 * 菜单单项
 * Created by cyl on 2018年5月10日 09:46:26.
 */
public class XsgItem {
    private String name;
    private String yh;
    private String old;
    private int imgRes;

    public XsgItem(String name, String yh, String old, int imgRes) {
        this.name = name;
        this.yh = yh;
        this.old = old;
        this.imgRes = imgRes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYh() {
        return yh;
    }

    public void setYh(String yh) {
        this.yh = yh;
    }

    public String getOld() {
        return old;
    }

    public void setOld(String old) {
        this.old = old;
    }

    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }
}

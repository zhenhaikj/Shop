package com.emjiayuan.app.entity;

/**
 * 菜单单项
 * Created by cyl on 2018年5月10日 09:46:26.
 */
public class MenuItem {

    /**
     * id : 1
     * name : 所有商品
     * icon : http://qiniu.emjiayuan.com/icon1516957708048
     * index : 1
     * status : 1
     * delflag : 0
     */

    private String id;
    private String name;
    private String icon;
    private String index;
    private String status;
    private String delflag;

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelflag() {
        return delflag;
    }

    public void setDelflag(String delflag) {
        this.delflag = delflag;
    }
}

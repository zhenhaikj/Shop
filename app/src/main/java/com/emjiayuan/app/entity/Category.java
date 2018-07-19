package com.emjiayuan.app.entity;

/**
 * 类别
 * Created by cyl on 2018年5月10日 09:46:26.
 */
public class Category {
    private String name;
    private Boolean click;
    /**
     * id : 30
     * parentid : 0
     * image :
     * delflag : 0
     * title_image :
     * topimage :
     * banner : xx
     * linkid : 253
     * top : 11
     * type : 0
     * indexstatus : 1
     */

    private String id;
    private String parentid;
    private String image;
    private String delflag;
    private String title_image;
    private String topimage;
    private String banner;
    private String linkid;
    private String top;
    private String type;
    private String indexstatus;

    public Category(String name, Boolean click) {
        this.name = name;
        this.click = click;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getClick() {
        return click;
    }

    public void setClick(Boolean click) {
        this.click = click;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDelflag() {
        return delflag;
    }

    public void setDelflag(String delflag) {
        this.delflag = delflag;
    }

    public String getTitle_image() {
        return title_image;
    }

    public void setTitle_image(String title_image) {
        this.title_image = title_image;
    }

    public String getTopimage() {
        return topimage;
    }

    public void setTopimage(String topimage) {
        this.topimage = topimage;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getLinkid() {
        return linkid;
    }

    public void setLinkid(String linkid) {
        this.linkid = linkid;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIndexstatus() {
        return indexstatus;
    }

    public void setIndexstatus(String indexstatus) {
        this.indexstatus = indexstatus;
    }
}

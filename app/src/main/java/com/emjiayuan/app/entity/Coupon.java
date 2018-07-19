package com.emjiayuan.app.entity;

public class Coupon {

    /**
     * id : 36
     * title : 1月10
     * subtitle : 促销优惠卷
     * maxmoney : 1000
     * savemoney : 1
     * createtime : 1515570584
     * starttime2 : 1514739660
     * finishtime2 : 1546275660
     * starttime : 2018-01-01 01:01:00
     * finishtime : 2019-01-01 01:01:00
     * type : 2
     * status : 1
     * daterange : 2018-01-01 01:01:00,2019-01-01 01:01:00
     * isreceive : 0
     */

    private String id;
    private String title;
    private String subtitle;
    private String maxmoney;
    private String savemoney;
    private String createtime;
    private String starttime2;
    private String finishtime2;
    private String starttime;
    private String finishtime;
    private String type;
    private String status;
    private String daterange;
    private int isreceive;
    /**
     * userid : 1
     * couponid : 37
     * usestatus : 0
     * usetime : 1
     */

    private String userid;
    private String couponid;
    private String usestatus;
    private String usetime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getMaxmoney() {
        return maxmoney;
    }

    public void setMaxmoney(String maxmoney) {
        this.maxmoney = maxmoney;
    }

    public String getSavemoney() {
        return savemoney;
    }

    public void setSavemoney(String savemoney) {
        this.savemoney = savemoney;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getStarttime2() {
        return starttime2;
    }

    public void setStarttime2(String starttime2) {
        this.starttime2 = starttime2;
    }

    public String getFinishtime2() {
        return finishtime2;
    }

    public void setFinishtime2(String finishtime2) {
        this.finishtime2 = finishtime2;
    }

    public String getStarttime() {
        return starttime.substring(0,starttime.indexOf(" "));
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getFinishtime() {
        return finishtime.substring(0,finishtime.indexOf(" "));
    }

    public void setFinishtime(String finishtime) {
        this.finishtime = finishtime;
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

    public String getDaterange() {
        return daterange;
    }

    public void setDaterange(String daterange) {
        this.daterange = daterange;
    }

    public int getIsreceive() {
        return isreceive;
    }

    public void setIsreceive(int isreceive) {
        this.isreceive = isreceive;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCouponid() {
        return couponid;
    }

    public void setCouponid(String couponid) {
        this.couponid = couponid;
    }

    public String getUsestatus() {
        return usestatus;
    }

    public void setUsestatus(String usestatus) {
        this.usestatus = usestatus;
    }

    public String getUsetime() {
        return usetime;
    }

    public void setUsetime(String usetime) {
        this.usetime = usetime;
    }
}

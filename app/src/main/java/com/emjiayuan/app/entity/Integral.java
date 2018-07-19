package com.emjiayuan.app.entity;

import com.emjiayuan.app.Utils.MyUtils;

public class Integral {

    /**
     * id : 5856
     * userid : xxx
     * points : 1
     * relativeid : 0
     * reason : xx
     * createtime : 1524030212
     */

    private String id;
    private String userid;
    private String points;
    private String relativeid;
    private String reason;
    private String createtime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getRelativeid() {
        return relativeid;
    }

    public void setRelativeid(String relativeid) {
        this.relativeid = relativeid;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCreatetime() {
        return MyUtils.stampToDate(createtime);
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
}

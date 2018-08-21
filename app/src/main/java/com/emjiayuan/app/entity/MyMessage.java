package com.emjiayuan.app.entity;

import com.emjiayuan.app.Utils.MyUtils;

import java.io.Serializable;

public class MyMessage implements Serializable{

    /**
     * id : 366
     * userid : 7615
     * wid : 2569
     * replyrid : 0
     * replyname : 
     * replytype : null
     * content : xx
     * createtime : 1521079883
     * status : 1
     * isread : 0
     * username : 13512225244
     * nickname : null
     * headimg : null
     * replyusername : null
     * replynickname : null
     * replyheadimg : null
     */

    private String id;
    private String userid;
    private String wid;
    private String replyrid;
    private String replyname;
    private String replytype;
    private String content;
    private String createtime;
    private String status;
    private String isread;
    private String username;
    private String nickname;
    private String headimg;
    private String replyusername;
    private String replynickname;
    private String replyheadimg;
    private String weibocontent;
    private String pasttime;

    public String getWeibocontent() {
        return weibocontent;
    }

    public void setWeibocontent(String weibocontent) {
        this.weibocontent = weibocontent;
    }

    public String getPasttime() {
        return pasttime;
    }

    public void setPasttime(String pasttime) {
        this.pasttime = pasttime;
    }

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

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public String getReplyrid() {
        return replyrid;
    }

    public void setReplyrid(String replyrid) {
        this.replyrid = replyrid;
    }

    public String getReplyname() {
        return replyname;
    }

    public void setReplyname(String replyname) {
        this.replyname = replyname;
    }

    public String getReplytype() {
        return replytype;
    }

    public void setReplytype(String replytype) {
        this.replytype = replytype;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatetime() {
        return MyUtils.stampToDate(createtime);
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsread() {
        return isread;
    }

    public void setIsread(String isread) {
        this.isread = isread;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public String getReplyusername() {
        return replyusername;
    }

    public void setReplyusername(String replyusername) {
        this.replyusername = replyusername;
    }

    public String getReplynickname() {
        return replynickname;
    }

    public void setReplynickname(String replynickname) {
        this.replynickname = replynickname;
    }

    public String getReplyheadimg() {
        return replyheadimg;
    }

    public void setReplyheadimg(String replyheadimg) {
        this.replyheadimg = replyheadimg;
    }
}

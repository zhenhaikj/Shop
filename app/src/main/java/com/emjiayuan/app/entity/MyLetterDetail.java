package com.emjiayuan.app.entity;

import java.io.Serializable;

public class MyLetterDetail implements Serializable{
    /**
     * id : 15
     * from_userid : 1
     * to_userid : 15
     * content : xxx
     * createtime : 1534749654
     * isread : 1
     * delflag : 0
     * from_username : xx
     * from_nickname : from
     * from_headimg : xx
     * to_username : xx
     * to_nickname : to
     * to_headimg : xx
     * pasttime : 刚刚
     */

    private String id;
    private String from_userid;
    private String to_userid;
    private String content;
    private String createtime;
    private String isread;
    private String delflag;
    private String from_username;
    private String from_nickname;
    private String from_headimg;
    private String to_username;
    private String to_nickname;
    private String to_headimg;
    private String pasttime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom_userid() {
        return from_userid;
    }

    public void setFrom_userid(String from_userid) {
        this.from_userid = from_userid;
    }

    public String getTo_userid() {
        return to_userid;
    }

    public void setTo_userid(String to_userid) {
        this.to_userid = to_userid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getIsread() {
        return isread;
    }

    public void setIsread(String isread) {
        this.isread = isread;
    }

    public String getDelflag() {
        return delflag;
    }

    public void setDelflag(String delflag) {
        this.delflag = delflag;
    }

    public String getFrom_username() {
        return from_username;
    }

    public void setFrom_username(String from_username) {
        this.from_username = from_username;
    }

    public String getFrom_nickname() {
        return from_nickname;
    }

    public void setFrom_nickname(String from_nickname) {
        this.from_nickname = from_nickname;
    }

    public String getFrom_headimg() {
        return from_headimg;
    }

    public void setFrom_headimg(String from_headimg) {
        this.from_headimg = from_headimg;
    }

    public String getTo_username() {
        return to_username;
    }

    public void setTo_username(String to_username) {
        this.to_username = to_username;
    }

    public String getTo_nickname() {
        return to_nickname;
    }

    public void setTo_nickname(String to_nickname) {
        this.to_nickname = to_nickname;
    }

    public String getTo_headimg() {
        return to_headimg;
    }

    public void setTo_headimg(String to_headimg) {
        this.to_headimg = to_headimg;
    }

    public String getPasttime() {
        return pasttime;
    }

    public void setPasttime(String pasttime) {
        this.pasttime = pasttime;
    }
}

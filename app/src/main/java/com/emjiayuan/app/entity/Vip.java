package com.emjiayuan.app.entity;

import com.emjiayuan.app.Utils.MyUtils;

public class Vip {

    /**
     * id : 5
     * classname : 钻石
     * level : Lv4
     * content : null
     * background : xxx
     * cost_num : 0
     * cost_money : 100000
     * cost_jifen : 100000
     * discount : 92
     * cost : 2000
     * status : 1
     * dayline : 一年
     * days : 365
     * delflag : 0
     */

    private String id;
    private String classname;
    private String level;
    private String content;
    private String background;
    private String cost_num;
    private String cost_money;
    private String cost_jifen;
    private String discount;
    private String cost;
    private String status;
    private String dayline;
    private String days;
    private String delflag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getCost_num() {
        return cost_num;
    }

    public void setCost_num(String cost_num) {
        this.cost_num = cost_num;
    }

    public String getCost_money() {
        return cost_money;
    }

    public void setCost_money(String cost_money) {
        this.cost_money = cost_money;
    }

    public String getCost_jifen() {
        return cost_jifen;
    }

    public void setCost_jifen(String cost_jifen) {
        this.cost_jifen = cost_jifen;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getCost() {
        return MyUtils.subZeroAndDot(cost);
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDayline() {
        return dayline;
    }

    public void setDayline(String dayline) {
        this.dayline = dayline;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getDelflag() {
        return delflag;
    }

    public void setDelflag(String delflag) {
        this.delflag = delflag;
    }
}

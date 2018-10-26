package com.emjiayuan.app.entity;

import java.io.Serializable;
import java.util.List;

public class VipCenter implements Serializable{

    /**
     * vipdate : 2019-09-27
     * levelname : 钻石 VIP
     * discount : 90
     * discount_again : 9
     * savemoney : 343.90
     * levelist : [{"id":"7","classname":"钻石 VIP","level":"Lv 3","content":"","background":"http://qiniu.emjiayuan.com/upload_file/ems/2018072013472808470","cost_num":null,"cost_money":"100000","cost_jifen":null,"discount":"90","cost":"1","status":"1","dayline":"一年","days":"365","delflag":"0","discount_again":9},{"id":"3","classname":"黄金VIP","level":"Lv2","content":"","background":"http://qiniu.emjiayuan.com/upload_file/ems/2018071718729515301","cost_num":"0","cost_money":"50000","cost_jifen":"100000","discount":"95","cost":"500","status":"1","dayline":"一年","days":"365","delflag":"0","discount_again":9.5},{"id":"2","classname":"白银VIP","level":"Lv1","content":"","background":"http://qiniu.emjiayuan.com/upload_file/ems/2018071718857304437","cost_num":"0","cost_money":"10000","cost_jifen":"100000","discount":"98","cost":"200","status":"1","dayline":"一年","days":"365","delflag":"0","discount_again":9.8}]
     */

    private String vipdate;
    private String levelname;
    private String discount;
    private String discount_again;
    private String savemoney;
    private List<LevelistBean> levelist;

    public String getVipdate() {
        return vipdate;
    }

    public void setVipdate(String vipdate) {
        this.vipdate = vipdate;
    }

    public String getLevelname() {
        return levelname;
    }

    public void setLevelname(String levelname) {
        this.levelname = levelname;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscount_again() {
        return discount_again;
    }

    public void setDiscount_again(String discount_again) {
        this.discount_again = discount_again;
    }

    public String getSavemoney() {
        return savemoney;
    }

    public void setSavemoney(String savemoney) {
        this.savemoney = savemoney;
    }

    public List<LevelistBean> getLevelist() {
        return levelist;
    }

    public void setLevelist(List<LevelistBean> levelist) {
        this.levelist = levelist;
    }

    public static class LevelistBean {
        /**
         * id : 7
         * classname : 钻石 VIP
         * level : Lv 3
         * content :
         * background : http://qiniu.emjiayuan.com/upload_file/ems/2018072013472808470
         * cost_num : null
         * cost_money : 100000
         * cost_jifen : null
         * discount : 90
         * cost : 1
         * status : 1
         * dayline : 一年
         * days : 365
         * delflag : 0
         * discount_again : 9
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
        private String discount_again;

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
            return cost;
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

        public String getDiscount_again() {
            return discount_again;
        }

        public void setDiscount_again(String discount_again) {
            this.discount_again = discount_again;
        }
    }
}

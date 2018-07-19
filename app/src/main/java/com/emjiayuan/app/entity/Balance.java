package com.emjiayuan.app.entity;

import com.emjiayuan.app.Utils.MyUtils;

public class Balance {
       /* "id": "14113",
        "userid": "xx",
        "money": "1",
        "createtime": "1524030630",
        "platform": "",
        "status": "2",
        "confirmtime": null,
        "pay_status": null,
        "payment": "xx",
        "paytime": null,
        "pay_message": null,
        "recharge_no": "",
        "delflag": "0",
        "status_txt": "充值成功"*/

        public String id;
        public String userid;
        public String money;
        public String createtime;
        public String platform;
        public String status;
        public String confirmtime;
        public String pay_status;
        public String payment;
        public String paytime;
        public String pay_message;
        public String recharge_no;
        public String delflag;
        public String status_txt;

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

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getCreatetime() {
        return MyUtils.stampToDate(createtime);
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getConfirmtime() {
        return confirmtime;
    }

    public void setConfirmtime(String confirmtime) {
        this.confirmtime = confirmtime;
    }

    public String getPay_status() {
        return pay_status;
    }

    public void setPay_status(String pay_status) {
        this.pay_status = pay_status;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getPaytime() {
        return paytime;
    }

    public void setPaytime(String paytime) {
        this.paytime = paytime;
    }

    public String getPay_message() {
        return pay_message;
    }

    public void setPay_message(String pay_message) {
        this.pay_message = pay_message;
    }

    public String getRecharge_no() {
        return recharge_no;
    }

    public void setRecharge_no(String recharge_no) {
        this.recharge_no = recharge_no;
    }

    public String getDelflag() {
        return delflag;
    }

    public void setDelflag(String delflag) {
        this.delflag = delflag;
    }

    public String getStatus_txt() {
        return status_txt;
    }

    public void setStatus_txt(String status_txt) {
        this.status_txt = status_txt;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

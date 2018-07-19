package com.emjiayuan.app.entity;

import java.io.Serializable;
import java.util.List;

public class SeckillBean implements Serializable{

    /**
     * id : 23
     * ms_name : 今日特价
     * url : http://qiniu.emjiayuan.com/ms1521091187739
     * starttime : 1521095400
     * endtime : 1533484740
     * ms_status : 1
     * top : 1
     * delflag : 0
     * start_date : 2018-03-15 14:30:00
     * end_date : 2018-08-05 23:59:00
     * daterange : 2018-03-15 14:30:00,2018-08-05 23:59:00
     * product_list : [{"id":"29","ms_price":"115","limit_num":"5","kucun":"221","name":"【百味鲜鸡精】清真牛大碗同等品质鸡精 整箱25袋装 400克一袋","images":"http://qiniu.emjiayuan.com/products_headimg1524909583444","price":"123.00"}]
     */

    private String id;
    private String ms_name;
    private String url;
    private String starttime;
    private String endtime;
    private String ms_status;
    private String top;
    private String delflag;
    private String start_date;
    private String end_date;
    private String daterange;
    private String status;
    private String residuetime;
    private List<ProductListBean> product_list;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResiduetime() {
        return residuetime;
    }

    public void setResiduetime(String residuetime) {
        this.residuetime = residuetime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMs_name() {
        return ms_name;
    }

    public void setMs_name(String ms_name) {
        this.ms_name = ms_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getMs_status() {
        return ms_status;
    }

    public void setMs_status(String ms_status) {
        this.ms_status = ms_status;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getDelflag() {
        return delflag;
    }

    public void setDelflag(String delflag) {
        this.delflag = delflag;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getDaterange() {
        return daterange;
    }

    public void setDaterange(String daterange) {
        this.daterange = daterange;
    }

    public List<ProductListBean> getProduct_list() {
        return product_list;
    }

    public void setProduct_list(List<ProductListBean> product_list) {
        this.product_list = product_list;
    }

    public static class ProductListBean implements Serializable{
        /**
         * id : 29
         * ms_price : 115
         * limit_num : 5
         * kucun : 221
         * name : 【百味鲜鸡精】清真牛大碗同等品质鸡精 整箱25袋装 400克一袋
         * images : http://qiniu.emjiayuan.com/products_headimg1524909583444
         * price : 123.00
         */

        private String id;
        private String ms_price;
        private String limit_num;
        private String kucun;
        private String name;
        private String images;
        private String price;
        private String preprice;
        private String productid;

        public String getPreprice() {
            return preprice;
        }

        public void setPreprice(String preprice) {
            this.preprice = preprice;
        }

        public String getProductid() {
            return productid;
        }

        public void setProductid(String productid) {
            this.productid = productid;
        }



        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMs_price() {
            return ms_price;
        }

        public void setMs_price(String ms_price) {
            this.ms_price = ms_price;
        }

        public String getLimit_num() {
            return limit_num;
        }

        public void setLimit_num(String limit_num) {
            this.limit_num = limit_num;
        }

        public String getKucun() {
            return kucun;
        }

        public void setKucun(String kucun) {
            this.kucun = kucun;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }
}

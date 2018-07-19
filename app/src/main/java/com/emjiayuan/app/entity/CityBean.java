package com.emjiayuan.app.entity;

import java.util.List;

public class CityBean {

    /**
     * code : A
     * list : [{"id":"35","code":"152900","name":"阿拉善盟","provincecode":"150000","full_py":"a la shan meng","first_zm":"A"},{"id":"38","code":"210300","name":"鞍山市","provincecode":"210000","full_py":"an shan shi","first_zm":"A"},{"id":"105","code":"340800","name":"安庆市","provincecode":"340000","full_py":"an qing shi","first_zm":"A"},{"id":"156","code":"410500","name":"安阳市","provincecode":"410000","full_py":"an yang shi","first_zm":"A"},{"id":"256","code":"513200","name":"阿坝藏族羌族自治州","provincecode":"510000","full_py":"a ba zang zu qiang zu zi zhi zhou","first_zm":"A"},{"id":"262","code":"520400","name":"安顺市","provincecode":"520000","full_py":"an shun shi","first_zm":"A"},{"id":"289","code":"542500","name":"阿里地区","provincecode":"540000","full_py":"a li di qu","first_zm":"A"},{"id":"299","code":"610900","name":"安康市","provincecode":"610000","full_py":"an kang shi","first_zm":"A"},{"id":"335","code":"652900","name":"阿克苏地区","provincecode":"650000","full_py":"a ke su di qu","first_zm":"A"},{"id":"341","code":"654300","name":"阿勒泰地区","provincecode":"650000","full_py":"a le tai di qu","first_zm":"A"}]
     */

    private String code;
    private List<ListBean> list;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id : 35
         * code : 152900
         * name : 阿拉善盟
         * provincecode : 150000
         * full_py : a la shan meng
         * first_zm : A
         */

        private String id;
        private String code;
        private String name;
        private String provincecode;
        private String full_py;
        private String first_zm;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProvincecode() {
            return provincecode;
        }

        public void setProvincecode(String provincecode) {
            this.provincecode = provincecode;
        }

        public String getFull_py() {
            return full_py;
        }

        public void setFull_py(String full_py) {
            this.full_py = full_py;
        }

        public String getFirst_zm() {
            return first_zm;
        }

        public void setFirst_zm(String first_zm) {
            this.first_zm = first_zm;
        }
    }
}

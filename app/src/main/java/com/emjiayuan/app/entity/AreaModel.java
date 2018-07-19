package com.emjiayuan.app.entity;

import com.emjiayuan.app.Utils.DigitUtil;

public class AreaModel implements Comparable {
    private String areaCode;
    private String cityName;

    public AreaModel(String areaCode, String cityName) {
        this.areaCode = areaCode;
        this.cityName = cityName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public int compareTo(Object o) {
        AreaModel areaModel = (AreaModel) o;
        return DigitUtil.getPinYinFirst(this.getCityName()).compareTo(DigitUtil.getPinYinFirst(areaModel.getCityName()));
    }
}

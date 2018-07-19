package com.emjiayuan.app.event;

import com.emjiayuan.app.entity.CarBean;

import java.util.List;

public class CarUpdateEvent {
    private List<CarBean> carBeanList;

    public CarUpdateEvent(List<CarBean> carBeanList) {
        this.carBeanList = carBeanList;
    }

    public List<CarBean> getCarBeanList() {
        return carBeanList;
    }

    public void setCarBeanList(List<CarBean> carBeanList) {
        this.carBeanList = carBeanList;
    }
}

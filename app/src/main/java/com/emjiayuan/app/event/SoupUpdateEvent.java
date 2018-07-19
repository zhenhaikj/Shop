package com.emjiayuan.app.event;

import com.emjiayuan.app.entity.Soup;

import java.util.List;

public class SoupUpdateEvent {
    private List<Soup> list;

    public SoupUpdateEvent(List<Soup> list) {
        this.list = list;
    }

    public List<Soup> getSoupList() {
        return list;
    }

    public void setSoupList(List<Soup> list) {
        this.list = list;
    }
}

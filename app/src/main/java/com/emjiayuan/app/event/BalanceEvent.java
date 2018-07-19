package com.emjiayuan.app.event;

import com.emjiayuan.app.entity.Balance;

import java.util.ArrayList;

public class BalanceEvent {
    private ArrayList<Balance> list;

    public ArrayList<Balance> getList() {
        return list;
    }

    public void setList(ArrayList<Balance> list) {
        this.list = list;
    }
}

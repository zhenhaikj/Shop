package com.emjiayuan.app.adapter;

import android.content.Context;

import com.emjiayuan.app.entity.CarBean;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

public class CarAdapter extends CommonAdapter<CarBean> {
    public CarAdapter(Context context, int layoutId, List<CarBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, CarBean item, int position) {

    }
}

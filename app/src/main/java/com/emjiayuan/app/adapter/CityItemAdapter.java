package com.emjiayuan.app.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.emjiayuan.app.R;
import com.emjiayuan.app.entity.CityBean;
import com.emjiayuan.app.entity.CityBean.ListBean;
import com.emjiayuan.app.widget.MyListView;

import java.util.List;

/**
 * 城市配器
 * Created by zst on 2017/8/2.
 */

public class CityItemAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<CityBean.ListBean> list;
    private Context context;

    public CityItemAdapter(Context context, List<CityBean.ListBean> list) {

        //赋值初始化
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.city_item3, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView
                    .findViewById(R.id.item_city_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(list.get(position).getName());
        return convertView;
    }

    private class ViewHolder {
        TextView name;
    }
}
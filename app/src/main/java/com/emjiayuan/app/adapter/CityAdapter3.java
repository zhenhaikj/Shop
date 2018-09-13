package com.emjiayuan.app.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.DigitUtil;
import com.emjiayuan.app.activity.CityActivity;
import com.emjiayuan.app.entity.CityBean;
import com.emjiayuan.app.entity.CityBean;
import com.emjiayuan.app.widget.MyListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 城市配器
 * Created by zst on 2017/8/2.
 */

public class CityAdapter3 extends BaseAdapter {
    private LayoutInflater inflater;
    private List<CityBean> list;
    private Context context;

    public void setList(List<CityBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public CityAdapter3(Context context, List<CityBean> list) {

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
            convertView = inflater.inflate(R.layout.city_item2, null);
            holder = new ViewHolder();
            holder.alpha = (TextView) convertView
                    .findViewById(R.id.item_city_alpha);
            holder.lv_city_name = (MyListView) convertView
                    .findViewById(R.id.lv_city_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.alpha.setText(list.get(position).getCode());
        CityItemAdapter adapter=new CityItemAdapter(context,list.get(position).getList());
        holder.lv_city_name.setAdapter(adapter);
        holder.lv_city_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra("name", list.get(position).getList().get(i).getName());
                intent.putExtra("provincecode", list.get(position).getList().get(i).getProvinceid());
                ((CityActivity)context).setResult(1, intent);
                ((CityActivity)context).finish();
            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView alpha;
        MyListView lv_city_name;
    }
}
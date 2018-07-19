package com.emjiayuan.app.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.emjiayuan.app.R;
import com.emjiayuan.app.entity.Type;

import java.util.ArrayList;


public class Pro_type_adapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<Type> list;
    private Context context;
    private Type type;

    public Pro_type_adapter(Context context, ArrayList<Type> list) {
        mInflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (list != null && list.size() > 0)
            return list.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final MyView view;
        if (convertView == null) {
            view = new MyView();
            convertView = mInflater.inflate(R.layout.goods_item, null);
            view.icon = (ImageView) convertView.findViewById(R.id.icon);
            view.name = (TextView) convertView.findViewById(R.id.name);
            view.yh = (TextView) convertView.findViewById(R.id.yh);
            convertView.setTag(view);
        } else {
            view = (MyView) convertView.getTag();
        }
        if (list != null && list.size() > 0) {
            type = list.get(position);
            view.name.setText(type.getTypename());
            if (type.getTypename().contains("电脑办公")) {
                view.icon.setBackgroundResource(R.drawable.img1);
            } else if (type.getTypename().contains("个护化妆")) {
                view.icon.setBackgroundResource(R.drawable.img2);
            } else if (type.getTypename().contains("鞋靴箱包")) {
                view.icon.setBackgroundResource(R.drawable.img3);
            } else if (type.getTypename().contains("潮流女装")) {
                view.icon.setBackgroundResource(R.drawable.img4);
            } else if (type.getTypename().contains("图书")) {
                view.icon.setBackgroundResource(R.drawable.img1);
            } else if (type.getTypename().contains("玩具乐器")) {
                view.icon.setBackgroundResource(R.drawable.img1);
            } else if (type.getTypename().contains("音像制品")) {
                view.icon.setBackgroundResource(R.drawable.img1);
            } else if (type.getTypename().contains("常用分类")) {
                view.icon.setBackgroundResource(R.drawable.img1);
            } else if (type.getTypename().contains("品牌男装")) {
                view.icon.setBackgroundResource(R.drawable.img1);
            } else if (type.getTypename().contains("内衣配饰")) {
                view.icon.setBackgroundResource(R.drawable.img1);
            } else if (type.getTypename().contains("家用电器")) {
                view.icon.setBackgroundResource(R.drawable.img1);
            } else if (type.getTypename().contains("手机数码")) {
                view.icon.setBackgroundResource(R.drawable.img1);
            } else {
                view.icon.setBackgroundResource(R.drawable.img1);
            }
        }

        return convertView;
    }


    private class MyView {
        private ImageView icon;
        private TextView name;
        private TextView yh;
    }


}
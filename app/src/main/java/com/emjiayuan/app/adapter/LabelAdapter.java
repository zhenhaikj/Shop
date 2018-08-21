package com.emjiayuan.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.emjiayuan.app.R;
import com.emjiayuan.app.entity.Label;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cyl on 2018年5月10日 09:52:49.
 */

public class LabelAdapter extends BaseAdapter {
    private Context mContext;

    private List<Label> grouplists = new ArrayList<>();
    private LayoutInflater mInflater;

    public LabelAdapter(Context mContext, List<Label> grouplists) {
        super();
        this.mContext = mContext;
        this.grouplists = grouplists;
        this.mInflater = LayoutInflater.from(mContext);
    }

    private int selected=-1;

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return grouplists.size();
    }

    @Override
    public Label getItem(int position) {
        return grouplists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {// 如果是第一次显示该页面(要记得保存到viewholder中供下次直接从缓存中调用)
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.label_item, null);
            holder.label = (TextView) convertView.findViewById(R.id.label);
            convertView.setTag(holder);
        } else {// 如果之前已经显示过该页面，则用viewholder中的缓存直接刷屏
            holder = (ViewHolder) convertView.getTag();
        }

        Label item = grouplists.get(position);
        holder.label.setText(item.getType());
        if (selected==position){
            holder.label.setBackgroundResource(R.drawable.label_shape_selected);
            holder.label.setTextColor(Color.parseColor("#ffffff"));
        }else{
            holder.label.setBackgroundResource(R.drawable.label_shape);
            holder.label.setTextColor(Color.parseColor("#9C9C9C"));
        }

        return convertView;
    }

    public class ViewHolder {
        public TextView label;
    }
}

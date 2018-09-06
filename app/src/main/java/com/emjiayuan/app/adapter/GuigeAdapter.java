package com.emjiayuan.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.emjiayuan.app.R;
import com.emjiayuan.app.entity.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cyl on 2018年5月10日 09:52:49.
 */

public class GuigeAdapter extends BaseAdapter {
    private Context mContext;

    private List<Product> grouplists = new ArrayList<>();
    private LayoutInflater mInflater;

    public GuigeAdapter(Context mContext, List<Product> grouplists) {
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
    public Product getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.guige_item, null);
            holder.guige = (TextView) convertView.findViewById(R.id.guige);
            convertView.setTag(holder);
        } else {// 如果之前已经显示过该页面，则用viewholder中的缓存直接刷屏
            holder = (ViewHolder) convertView.getTag();
        }

        Product item = grouplists.get(position);
        holder.guige.setText(item.getGuige());
        if (selected==position){
            holder.guige.setBackgroundResource(R.drawable.guige_shape_selected);
            holder.guige.setTextColor(Color.parseColor("#ffffff"));
        }else{
            holder.guige.setBackgroundResource(R.drawable.guige_shape);
            holder.guige.setTextColor(Color.parseColor("#747474"));
        }

        return convertView;
    }

    public class ViewHolder {
        public TextView guige;
    }
}

package com.emjiayuan.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.emjiayuan.app.R;
import com.emjiayuan.app.entity.Order.ProductListBean;
import com.emjiayuan.app.entity.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cyl on 2018年5月10日 09:52:49.
 */

public class OrderInAdapter2 extends BaseAdapter {
    private Context mContext;
    private List<Order.ProductListBean> grouplists = new ArrayList<>();
    private LayoutInflater mInflater;
    private int type;
    public OrderInAdapter2(Context mContext, List<Order.ProductListBean> grouplists,int type) {
        super();
        this.mContext = mContext;
        this.grouplists = grouplists;
        this.type=type;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return grouplists.size();
//        return 1;
    }

    @Override
    public Order.ProductListBean getItem(int position) {
        return grouplists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        final  ViewHolder holder= new ViewHolder();
        ViewHolder holder= null;
        if (convertView == null) {// 如果是第一次显示该页面(要记得保存到holder中供下次直接从缓存中调用)
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.order_item_in, null);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.num = (TextView) convertView.findViewById(R.id.num);
            convertView.setTag(holder);
        } else {// 如果之前已经显示过该页面，则用holder中的缓存直接刷屏
            holder = (ViewHolder) convertView.getTag();
        }

        Order.ProductListBean item = grouplists.get(position);
        Glide.with(mContext).load(item.getImages()).into(holder.icon);
        holder.name.setText(item.getName());
        switch (type){
            case 0:
                holder.price.setText("¥"+item.getBuyprice());
                break;
            case 1:
                holder.price.setText(item.getJifen()+"积分");
                break;
            case 2:
                holder.price.setText(item.getGiftjifen()+"元充值赠送");
                break;
        }

        holder.num.setText("X"+item.getBuycount());

        return convertView;
    }
    public class ViewHolder {
        public ImageView icon;
        public TextView name;
        public TextView price;
        public TextView num;
    }
}

package com.emjiayuan.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.emjiayuan.app.R;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.entity.Product;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cyl on 2018年5月10日 09:52:49.
 */

public class VipPriceAdapter extends BaseAdapter {
    private Context mContext;

    private List<Product.LevelListBean> grouplists = new ArrayList<>();

    public List<Product.LevelListBean> getGrouplists() {
        return grouplists;
    }

    public void setGrouplists(List<Product.LevelListBean> grouplists) {
        this.grouplists = grouplists;
        notifyDataSetChanged();
    }

    private LayoutInflater mInflater;

    public VipPriceAdapter(Context mContext, List<Product.LevelListBean> grouplists) {
        super();
        this.mContext = mContext;
        this.grouplists = grouplists;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
//        return 4;
        return grouplists.size();
    }

    @Override
    public Product.LevelListBean getItem(int position) {
        return grouplists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {// 如果是第一次显示该页面(要记得保存到viewholder中供下次直接从缓存中调用)
            convertView = mInflater.inflate(R.layout.vip_price_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {// 如果之前已经显示过该页面，则用viewholder中的缓存直接刷屏
            holder = (ViewHolder) convertView.getTag();
        }

        final Product.LevelListBean item = grouplists.get(position);
        holder.vipLabel.setText(item.getLevel_name());
        holder.vipPrice.setText("立减"+item.getLevel_price()+"元");
        holder.vipLabel.setSelected("1".equals(item.getLevel_use()));
        holder.vipPrice.setSelected("1".equals(item.getLevel_use()));
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.vip_label)
        TextView vipLabel;
        @BindView(R.id.vip_price)
        TextView vipPrice;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

package com.emjiayuan.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.activity.LogisticsDetailActivity;
import com.emjiayuan.app.activity.OrderDetailActivity;
import com.emjiayuan.app.entity.Order;

import java.util.ArrayList;

/**
 * Created by cyl on 2018年5月10日 09:52:49.
 */

public class WuliuAdapter extends BaseAdapter {
    private Context mContext;

    private ArrayList<Order> grouplists = new ArrayList<>();

    public ArrayList<Order> getGrouplists() {
        return grouplists;
    }

    public void setGrouplists(ArrayList<Order> grouplists) {
        this.grouplists = grouplists;
    }

    private LayoutInflater mInflater;

    public WuliuAdapter(Context mContext, ArrayList<Order> grouplists) {
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
    public Order getItem(int position) {
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
            holder = new ViewHolder();
//        final ViewHolder holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.wuliu_item, null);
            holder.order_num = (TextView) convertView.findViewById(R.id.order_num);
            holder.company = (TextView) convertView.findViewById(R.id.company);
            holder.wuliu_num = (TextView) convertView.findViewById(R.id.wuliu_num);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.see = (TextView) convertView.findViewById(R.id.see);

            convertView.setTag(holder);
        } else {// 如果之前已经显示过该页面，则用viewholder中的缓存直接刷屏
            holder = (ViewHolder) convertView.getTag();
        }

        final Order item = grouplists.get(position);
        holder.order_num.setText("订单号："+item.getOrder_no());
        holder.company.setText("物流公司："+item.getExpressname());
        holder.wuliu_num.setText("物流编号："+item.getExpressno());
        holder.time.setText("下单时间："+item.getCreatedate());
//        holder.phone.setText(item.getTelphone());
//        holder.address.setText(item.getOrder());
        holder.see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyUtils.isFastClick()){
                    return;
                }
                Intent intent=new Intent(mContext, LogisticsDetailActivity.class);
                intent.putExtra("postid",item.getExpressno());
                intent.putExtra("postcom",item.getExpresscom());
                intent.putExtra("order_no",item.getOrder_no());
                intent.putExtra("date",item.getCreatedate());
                intent.putExtra("address",item.getAddress());
                mContext.startActivity(intent);
            }
        });



        return convertView;
    }


    public class ViewHolder {
        public TextView order_num;
        public TextView company;
        public TextView wuliu_num;
        public TextView time;
        public TextView see;
    }
}

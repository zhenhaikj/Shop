package com.emjiayuan.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.activity.CouponUseActivity;
import com.emjiayuan.app.entity.Coupon;
import com.emjiayuan.app.entity.OrderComfirm;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cyl on 2018年5月10日 09:52:49.
 */

public class CouponAdapter2 extends BaseAdapter {
    private Context mContext;
    private Integer[] bgs=new Integer[]{R.drawable.coupon1,R.drawable.coupon3,R.drawable.coupon4};


    public ArrayList<OrderComfirm.UsercouponsBean> getGrouplists() {
        return grouplists;
    }

    public void setGrouplists(ArrayList<OrderComfirm.UsercouponsBean> grouplists) {
        this.grouplists = grouplists;
        notifyDataSetChanged();
    }

    private ArrayList<OrderComfirm.UsercouponsBean> grouplists = new ArrayList<>();
    private LayoutInflater mInflater;

    public CouponAdapter2(Context mContext, ArrayList<OrderComfirm.UsercouponsBean> grouplists) {
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
    public OrderComfirm.UsercouponsBean getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.coupon_item2, null);
            holder = new ViewHolder(convertView);
            /*holder.coupon_ll = (LinearLayout) convertView.findViewById(R.id.coupon_ll);
            holder.value = (TextView) convertView.findViewById(R.id.value);
            holder.situation = (TextView) convertView.findViewById(R.id.situation);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.use = (TextView) convertView.findViewById(R.id.use);
            holder.label = (TextView) convertView.findViewById(R.id.label);*/
            convertView.setTag(holder);
        } else {// 如果之前已经显示过该页面，则用viewholder中的缓存直接刷屏
            holder = (ViewHolder) convertView.getTag();
        }

        final OrderComfirm.UsercouponsBean item = grouplists.get(position);
//        if ("1".equals())
        if (!"1".equals(item.getIsuse())){
            holder.rl.setBackgroundResource(R.drawable.coupon2);
            holder.get.setText("立即使用");
            holder.get.setTextColor(Color.parseColor("#CFCFCF"));
            holder.get.setBackgroundResource(R.drawable.get_shape1);
            holder.get.setEnabled(false);
        }else{
            holder.get.setText("立即使用");
            holder.get.setTextColor(Color.parseColor("#FEA080"));
            holder.get.setBackgroundResource(R.drawable.get_shape);
            holder.get.setEnabled(true);
            holder.rl.setBackgroundResource(bgs[(int)(Math.random()*3)]);
        }
        holder.value.setText(item.getSavemoney());
        holder.maxmoney.setText("满"+item.getMaxmoney()+"元可用");
        holder.time.setText(item.getStarttime()+"—"+item.getFinishtime());
        holder.get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyUtils.isFastClick()){
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("coupon", item);
                ((CouponUseActivity)mContext).setResult(200,intent);
                ((CouponUseActivity)mContext).finish();
            }
        });
        holder.content.setText(item.getSubtitle());


        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.dollar)
        TextView dollar;
        @BindView(R.id.value)
        TextView value;
        @BindView(R.id.rl)
        RelativeLayout rl;
        @BindView(R.id.content)
        TextView content;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.get)
        TextView get;
        @BindView(R.id.coupon_ll)
        LinearLayout couponLl;
        @BindView(R.id.maxmoney)
        TextView maxmoney;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

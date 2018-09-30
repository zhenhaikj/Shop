package com.emjiayuan.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.GlideUtil;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.activity.GoodsDetailActivity;
import com.emjiayuan.app.entity.SeckillBean;
import com.emjiayuan.app.entity.SeckillBean.ProductListBean;
import com.emjiayuan.app.widget.MyImageView;
import com.emjiayuan.app.widget.RatioImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cyl on 2018年5月10日 09:52:49.
 */

public class SecondsKillAdapter2 extends BaseAdapter {
    private Context mContext;
    private SeckillBean seckillBean;

    private List<ProductListBean> grouplists = new ArrayList<>();
    private LayoutInflater mInflater;
    ViewHolder holder = null;

    public SecondsKillAdapter2(Context mContext, SeckillBean seckillBean) {
        super();
        this.mContext = mContext;
        this.seckillBean = seckillBean;
//        for (int i = 0; i < 5; i++) {
//            this.grouplists.addAll(seckillBean.getProduct_list());
//        }
        this.grouplists = seckillBean.getProduct_list();
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
//        return 6;
        return grouplists.size();
    }

    @Override
    public ProductListBean getItem(int position) {
        return grouplists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {// 如果是第一次显示该页面(要记得保存到viewholder中供下次直接从缓存中调用)

            convertView = mInflater.inflate(R.layout.seconds_kill_item2, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {// 如果之前已经显示过该页面，则用viewholder中的缓存直接刷屏
            holder = (ViewHolder) convertView.getTag();
        }

        final ProductListBean item = grouplists.get(position);
        GlideUtil.loadImageViewLoding(mContext, item.getImages(), holder.icon, R.drawable.empty_img, R.drawable.empty_img);
        holder.name.setText(item.getName());
        holder.price.setText("¥" + item.getMs_price());
        holder.old_price.setText("¥" + item.getPreprice());

        double yh = Double.parseDouble(item.getPreprice()) - Double.parseDouble(item.getMs_price());
        holder.old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.old_price.getPaint().setAntiAlias(true);

        if ("0".equals(seckillBean.getStatus())) {
            holder.second_kill_btn.setText("即将开秒");
            holder.xl.setText("限量" + item.getLimit_num() + "件");
            holder.second_kill_btn.setBackgroundResource(R.drawable.kill_shape2);
            holder.second_kill_btn.setEnabled(false);
        } else if ("1".equals(seckillBean.getStatus())) {
            holder.second_kill_btn.setText("立即抢购");
            holder.xl.setText("仅剩" + item.getKucun() + "件");
            holder.second_kill_btn.setBackgroundResource(R.drawable.kill_shape1);
            holder.second_kill_btn.setEnabled(true);
        } else if ("2".equals(seckillBean.getStatus())) {
            holder.second_kill_btn.setText("已结束");
            holder.xl.setText("仅剩" + item.getKucun() + "件");
            holder.second_kill_btn.setBackgroundResource(R.drawable.kill_shape2);
            holder.second_kill_btn.setEnabled(false);
        }

        holder.second_kill_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("0".equals(seckillBean.getStatus())) {
                    MyUtils.showToast(mContext, "别急，活动还没开始呢！");
                } else if ("1".equals(seckillBean.getStatus())) {
                    Intent intent = new Intent(mContext, GoodsDetailActivity.class);
                    intent.putExtra("productid", item.getProductid());
                    intent.putExtra("kill", true);
                    intent.putExtra("promotionvalue", seckillBean.getId());
                    mContext.startActivity(intent);
                } else if ("2".equals(seckillBean.getStatus())) {
                    MyUtils.showToast(mContext, "活动已结束！");
                }
            }
        });

        holder.zs.setText("优惠" + yh + "元");
        if ("0".equals(item.getKucun())) {
            holder.bg_ll.setVisibility(View.VISIBLE);
            holder.bg_ll.getBackground().setAlpha(100);
            holder.second_kill_btn.setBackgroundResource(R.drawable.kill_shape2);
            holder.second_kill_btn.setEnabled(false);
        } else {
            holder.bg_ll.setVisibility(View.GONE);
//            holder.second_kill_btn.setBackgroundResource(R.drawable.kill_shape1);
//            holder.second_kill_btn.setEnabled(true);
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.image)
        MyImageView icon;
        @BindView(R.id.sold_out)
        RatioImageView soldOut;
        @BindView(R.id.bg_ll)
        LinearLayout bg_ll;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.old_price)
        TextView old_price;
        @BindView(R.id.zs)
        TextView zs;
        @BindView(R.id.xl)
        TextView xl;
        @BindView(R.id.kill_btn)
        Button second_kill_btn;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

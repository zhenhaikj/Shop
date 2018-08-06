package com.emjiayuan.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.GlideUtil;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.activity.GoodsDetailActivity;
import com.emjiayuan.app.activity.SecondsKillActivity;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.entity.SeckillBean.ProductListBean;
import com.emjiayuan.app.entity.SeckillBean;

import java.util.ArrayList;
import java.util.List;

import cn.iwgang.countdownview.CountdownView;

/**
 * Created by cyl on 2018年5月10日 09:52:49.
 */

public class SecondsKillAdapter extends BaseAdapter {
    private Context mContext;
    private SeckillBean seckillBean;
    /*private long mHour = 10;
    private long mMin = 30;
    private long mSecond = 00;// 天 ,小时,分钟,秒*/

    private List<SeckillBean.ProductListBean> grouplists = new ArrayList<>();
    private LayoutInflater mInflater;
    ViewHolder holder = null;
    private long sjc;

    /*private Handler timeHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1) {
                computeTime();
                holder.hour.setText(mHour+"");
                holder.minute.setText(mMin+"");
                holder.second.setText(mSecond+"");
                notifyDataSetChanged();
            }
        }
    };*/
    public SecondsKillAdapter(Context mContext, SeckillBean seckillBean) {
        super();
        this.mContext = mContext;
        this.seckillBean=seckillBean;
        this.grouplists = seckillBean.getProduct_list();
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
//        return 6;
        return grouplists.size();
    }

    @Override
    public SeckillBean.ProductListBean getItem(int position) {
        return grouplists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {// 如果是第一次显示该页面(要记得保存到viewholder中供下次直接从缓存中调用)
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.seconds_kill_item, null);
            holder.icon = (ImageView) convertView.findViewById(R.id.image);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.message = (TextView) convertView.findViewById(R.id.message);
            holder.old_price = (TextView) convertView.findViewById(R.id.old_price);
            holder.zs = (TextView) convertView.findViewById(R.id.zs);
            holder.xl = (TextView) convertView.findViewById(R.id.xl);
            /*holder.hour = (TextView) convertView.findViewById(R.id.hours_tv);
            holder.minute = (TextView) convertView.findViewById(R.id.minutes_tv);
            holder.second = (TextView) convertView.findViewById(R.id.seconds_tv);*/
            holder.second_kill_btn = (Button) convertView.findViewById(R.id.kill_btn);
            holder.countdownView = (CountdownView) convertView.findViewById(R.id.countdownview);
            holder.bg_ll = (LinearLayout) convertView.findViewById(R.id.bg_ll);
            convertView.setTag(holder);
        } else {// 如果之前已经显示过该页面，则用viewholder中的缓存直接刷屏
            holder = (ViewHolder) convertView.getTag();
        }

        final SeckillBean.ProductListBean item = grouplists.get(position);
        GlideUtil.loadImageViewLoding(mContext,item.getImages(),holder.icon,R.drawable.empty_img,R.drawable.empty_img);
//        holder.icon.setImageResource(R.drawable.img1);
        holder.name.setText(item.getName());
        holder.price.setText("¥"+item.getMs_price());
//        holder.price.setText(Html.fromHtml("<small>¥ </small><big><big>"+item.getMs_price().substring(0,item.getMs_price().indexOf("."))+"</big></big><small>"+item.getMs_price().substring(item.getMs_price().indexOf("."),item.getMs_price().length())+"</small>"));
        holder.old_price.setText("¥"+item.getPreprice());

        double yh=Double.parseDouble(item.getPreprice())-Double.parseDouble(item.getMs_price());
        holder.old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.old_price.getPaint().setAntiAlias(true);

        if ("0".equals(seckillBean.getStatus())){
            holder.message.setText("距离开秒还有");
            holder.second_kill_btn.setText("即将开秒");
            holder.xl.setText("限量"+item.getLimit_num()+"件");
//            holder.countdownView.setVisibility(View.VISIBLE);
        }else if ("1".equals(seckillBean.getStatus())){
            holder.message.setText("距离结束还有");
            holder.second_kill_btn.setText("立即抢购");
            holder.xl.setText("仅剩"+item.getKucun()+"件");
//            holder.countdownView.setVisibility(View.VISIBLE);
        }else if ("2".equals(seckillBean.getStatus())){
            holder.message.setText("已结束");
            holder.second_kill_btn.setText("已结束");
            holder.xl.setText("仅剩"+item.getKucun()+"件");
//            holder.countdownView.setVisibility(View.GONE);
        }
        holder.countdownView.start(Long.parseLong(seckillBean.getResiduetime())*1000);
        holder.countdownView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
            @Override
            public void onEnd(CountdownView cv) {
                if ("0".equals(seckillBean.getStatus())){
                    holder.message.setText("距离结束还有");
                    holder.second_kill_btn.setText("立即抢购");
//                    holder.countdownView.start((Long.parseLong(seckillBean.getEndtime())-Long.parseLong(seckillBean.getStarttime()))*1000);
                    seckillBean.setStatus("1");
                    seckillBean.setResiduetime(Long.toString(Long.parseLong(seckillBean.getEndtime())-Long.parseLong(seckillBean.getStarttime())));
                    MyUtils.e("---seckillBean.getResiduetime()---",seckillBean.getResiduetime());
                    MyUtils.e("---seckillBean.getStatus()---",seckillBean.getStatus());
//                    holder.countdownView.setVisibility(View.VISIBLE);
                    notifyDataSetChanged();
                    return;
                }else if ("1".equals(seckillBean.getStatus())){
                    holder.message.setText("已结束");
                    holder.second_kill_btn.setText("已结束");
//                    holder.countdownView.setVisibility(View.GONE);
                    seckillBean.setStatus("2");
                    seckillBean.setResiduetime("0");
                    notifyDataSetChanged();
                }

            }
        });
        holder.second_kill_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("0".equals(seckillBean.getStatus())){
                    MyUtils.showToast(mContext,"别急，活动还没开始呢！");
                }else if ("1".equals(seckillBean.getStatus())){
                    Intent intent = new Intent(mContext, GoodsDetailActivity.class);
                    intent.putExtra("productid", item.getProductid());
                    intent.putExtra("kill", true);
                    intent.putExtra("promotionvalue", seckillBean.getId());
                    mContext.startActivity(intent);
                }else if ("2".equals(seckillBean.getStatus())){
                    MyUtils.showToast(mContext,"活动已结束！");
                }
            }
        });

        holder.zs.setText("优惠"+yh+"元");
        if ("0".equals(item.getKucun())){
            holder.bg_ll.setVisibility(View.VISIBLE);
            holder.bg_ll.getBackground().setAlpha(100);
            holder.second_kill_btn.setBackgroundResource(R.drawable.kill_shape2);
            holder.second_kill_btn.setEnabled(false);
        }else{
            holder.bg_ll.setVisibility(View.GONE);
            holder.second_kill_btn.setBackgroundResource(R.drawable.kill_shape1);
            holder.second_kill_btn.setEnabled(true);
        }
//        startRun();
        return convertView;
    }

    public class ViewHolder {
        public ImageView icon;
        public TextView name;
        public TextView price;
        public TextView old_price;
        public TextView zs;
        public TextView xl;
        public TextView hour;
        public TextView minute;
        public TextView second;
        public TextView message;
        public Button second_kill_btn;
        public CountdownView countdownView;
        public LinearLayout bg_ll;

    }
    /**
     * 开启倒计时
     */
    /*private void startRun() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                    try {
                        Thread.sleep(1000); // sleep 1000ms
                        Message message = Message.obtain();
                        message.what = 1;
                        timeHandler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        }).start();
    }*/

    /**
     * 倒计时计算
     */
   /* private void computeTime() {
        mSecond--;
        if (mSecond < 0) {
            mMin--;
            mSecond = 59;
            if (mMin < 0) {
                mMin = 59;
                mHour--;
*//*                if (mHour < 0) {
                    // 倒计时结束
                    mHour = 23;
                    mDay--;
                }*//*
            }
        }
    }*/
}

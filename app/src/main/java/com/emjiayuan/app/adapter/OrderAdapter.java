package com.emjiayuan.app.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.emjiayuan.app.Constants;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.activity.GoodsDetailActivity;
import com.emjiayuan.app.activity.IntegralYlDetailActivity;
import com.emjiayuan.app.activity.JudgeActivity;
import com.emjiayuan.app.activity.LogisticsDetailActivity;
import com.emjiayuan.app.activity.OrderConfirmActivity3;
import com.emjiayuan.app.activity.OrderDetailActivity;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.entity.Order;
import com.emjiayuan.app.entity.PayResult;
import com.emjiayuan.app.entity.WXpayInfo;
import com.emjiayuan.app.event.UpdateEvent;
import com.emjiayuan.app.widget.MyListView;
import com.google.gson.Gson;
import com.qiyukf.unicorn.api.ConsultSource;
import com.qiyukf.unicorn.api.Unicorn;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

/**
 * Created by cyl on 2018年5月10日 09:52:49.
 */

public class OrderAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Order> grouplists = new ArrayList<>();
    private LayoutInflater mInflater;
    private OrderInAdapter2 adapter;
    private Map<Integer,Boolean> map=new HashMap();
    private int order_type=0;
    private int order_status=0;
    private final static int PT=0;//普通
    private final static int JF=1;//积分
    private final static int CZ=2;//充值
    private final static int TL=3;//汤料
    private final static int ALL=0;//全部
    private final static int DFK=1;//待付款
    private final static int DFH=2;//待发货
    private final static int DSH=3;//待收货
    private final static int DPJ=4;//待评价
    ViewHolder holder;
    private PopupWindow mPopupWindow;
    private String orderInfo;

    public ArrayList<Order> getGrouplists() {
        return grouplists;
    }

    public void setGrouplists(ArrayList<Order> grouplists,int order_status) {
        this.grouplists = grouplists;
        this.order_status=order_status;
        notifyDataSetChanged();
    }

    public OrderAdapter(Context mContext, ArrayList<Order> grouplists, int order_type) {
        super();
        this.mContext = mContext;
        this.grouplists = grouplists;
        this.mInflater = LayoutInflater.from(mContext);
        this.order_type=order_type;
    }

    @Override
    public int getCount() {
        return grouplists.size();
//        return 4;
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
//        final  ViewHolder holder= new ViewHolder();
//        ViewHolder holder= null;
        if (convertView == null) {// 如果是第一次显示该页面(要记得保存到holder中供下次直接从缓存中调用)
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.order_item, null);
            holder.lv_goods = (MyListView) convertView.findViewById(R.id.lv_goods);
            holder.total_tv = (TextView) convertView.findViewById(R.id.total_tv);
            holder.order_time = (TextView) convertView.findViewById(R.id.order_time);
            holder.status_tv = (TextView) convertView.findViewById(R.id.status_tv);
            holder.order_num = (TextView) convertView.findViewById(R.id.order_num);
            holder.btn1 = (TextView) convertView.findViewById(R.id.btn1);
            holder.btn2 = (TextView) convertView.findViewById(R.id.btn2);
            holder.lv_goods_in = (MyListView) convertView.findViewById(R.id.lv_goods_in);
            holder.up_down = (CheckBox) convertView.findViewById(R.id.up_down);
            holder.line = (View) convertView.findViewById(R.id.line);
            convertView.setTag(holder);
        } else {// 如果之前已经显示过该页面，则用holder中的缓存直接刷屏
            holder = (ViewHolder) convertView.getTag();
        }
        final Order item = grouplists.get(position);
//        Glide.with(mContext).load(item.getImages()).into(holder.icon);
//        holder.name.setText(item.getName());
//        holder.price.setText("¥"+item.getPrice());
//        holder.num.setText("X"+item.getPrice());

        adapter=new OrderInAdapter2(mContext,item.getProduct_list(),order_type);
        holder.lv_goods.setAdapter(adapter);
        holder.lv_goods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!MyUtils.isFastClick()){
                    return;
                }
                if (order_type!=CZ){
                    Intent intent=null;
                    switch (order_type){
                        case PT:
                            intent=new Intent(mContext, GoodsDetailActivity.class);
                            break;
                        case JF:
                            intent=new Intent(mContext, IntegralYlDetailActivity.class);
                            break;
                    }
                    if ("MS".equals(item.getOrder_cate())){
                        intent.putExtra("kill", true);
                        intent.putExtra("promotionvalue", item.getPromotion_value());
                    }
                    intent.putExtra("productid", item.getProduct_list().get(i).getProductid());
                    mContext.startActivity(intent);
                }

            }
        });
        holder.order_time.setText(item.getCreatetime());
        holder.order_num.setText(item.getOrder_no());
        int count=0;
        for (int i = 0; i < item.getProduct_list().size(); i++) {
            count+=Integer.parseInt(item.getProduct_list().get(i).getBuycount());
        }
        switch (order_type){
            case PT:
                holder.total_tv.setText("共"+count+"件商品 合计: ¥"+item.getTotalmoney()+" (含运费及优惠券优惠)");
                break;
            case JF:
                holder.total_tv.setText("共"+count+"件商品 兑换积分: "+item.getPromotion_value());
                break;
            case CZ:
                holder.total_tv.setText("共"+count+"件赠送商品");
                break;
        }
        holder.status_tv.setText(item.getOrder_status());
        //0普通订单1积分礼品2充值礼品3汤料订单
        switch (order_type){
            case PT:
                holder.up_down.setVisibility(View.GONE);
                holder.lv_goods_in.setVisibility(View.GONE);
                holder.line.setVisibility(View.GONE);
                switch (order_status) {
                    case 0:
                        switch (Integer.parseInt(item.getOrdertype())){
                            case 0:

                                holder.btn1.setVisibility(View.GONE);
                                holder.btn2.setVisibility(View.GONE);
                                holder.btn2.setText("再次购买");
                                holder.btn2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
//                                        mContext.startActivity(new Intent(mContext, OrderConfirmContext.class));
                                    }
                                });
                                break;
                            case 1:

                                holder.btn1.setVisibility(View.VISIBLE);
                                holder.btn2.setVisibility(View.VISIBLE);
                                holder.btn1.setText("取消订单");
                                holder.btn2.setText("去付款");
                                holder.btn1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (!MyUtils.isFastClick()){
                                            return;
                                        }
                                        updateOrder("0",item.getId(),position);
                                    }
                                });
                                holder.btn2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (!MyUtils.isFastClick()){
                                            return;
                                        }
//                                        Intent intent=new Intent(mContext, OrderDetailActivity.class);
//                                        intent.putExtra("orderid",item.getId());
//                                        mContext.startActivity(intent);
                                        showPopupWindow(item);
                                    }
                                });
                                break;
                            case 2:

                                holder.btn1.setVisibility(View.GONE);
                                holder.btn2.setText("提醒发货");
                                holder.btn2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (!MyUtils.isFastClick()){
                                            return;
                                        }
                                        String title = "伊穆家园客服";
                                        /**
                                         * 设置访客来源，标识访客是从哪个页面发起咨询的，用于客服了解用户是从什么页面进入。
                                         * 三个参数分别为：来源页面的url，来源页面标题，来源页面额外信息（保留字段，暂时无用）。
                                         * 设置来源后，在客服会话界面的"用户资料"栏的页面项，可以看到这里设置的值。
                                         */
                                        ConsultSource source = new ConsultSource("app", "app", "custom information string");
                                        /**
                                         * 请注意： 调用该接口前，应先检查Unicorn.isServiceAvailable()，
                                         * 如果返回为false，该接口不会有任何动作
                                         *
                                         * @param context 上下文
                                         * @param title   聊天窗口的标题
                                         * @param source  咨询的发起来源，包括发起咨询的url，title，描述信息等
                                         */
                                        Unicorn.openServiceActivity(mContext, title, source);
                                    }
                                });
                                break;
                            case 3:

                                holder.btn1.setVisibility(View.VISIBLE);
                                holder.btn2.setVisibility(View.VISIBLE);
                                holder.btn1.setText("查看物流");
                                holder.btn2.setText("确认收货");
                                holder.btn2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (!MyUtils.isFastClick()){
                                            return;
                                        }
                                        updateOrder("1",item.getId(),position);
//                                MyUtils.showToast(mContext,"确认收货！");
                                    }
                                });
                                holder.btn1.setOnClickListener(new View.OnClickListener() {
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
                                break;
                            case 4:

                                holder.btn1.setVisibility(View.GONE);
                                holder.btn2.setVisibility(View.VISIBLE);
                                if("已评价".equals(item.getComment())){
                                    holder.btn2.setText("已评价");
                                    holder.btn2.setEnabled(false);
                                }else{
                                    holder.btn2.setText("去评价");
                                    holder.btn2.setEnabled(true);
                                }

                                holder.btn2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (!MyUtils.isFastClick()){
                                            return;
                                        }
                                        Intent intent=new Intent(mContext, JudgeActivity.class);
                                        intent.putExtra("order",item);
                                        mContext.startActivity(intent);
                                    }
                                });
                                break;
                        }
                        break;
                    case 1:

                        holder.btn1.setVisibility(View.VISIBLE);
                        holder.btn1.setText("取消订单");
                        holder.btn2.setText("去付款");
                        holder.btn1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!MyUtils.isFastClick()){
                                    return;
                                }
                                updateOrder("0",item.getId(),position);
                            }
                        });
                        holder.btn2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!MyUtils.isFastClick()){
                                    return;
                                }
//                                Intent intent=new Intent(mContext, OrderDetailActivity.class);
//                                intent.putExtra("orderid",item.getId());
//                                intent.putExtra("type", 0);
//                                mContext.startActivity(intent);
                                showPopupWindow(item);
                            }
                        });
                        break;
                    case 2:

                        holder.btn1.setVisibility(View.GONE);
                        holder.btn2.setVisibility(View.VISIBLE);
                        holder.btn2.setText("提醒发货");
                        holder.btn2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!MyUtils.isFastClick()){
                                    return;
                                }
                                String title = "伊穆家园客服";
                                /**
                                 * 设置访客来源，标识访客是从哪个页面发起咨询的，用于客服了解用户是从什么页面进入。
                                 * 三个参数分别为：来源页面的url，来源页面标题，来源页面额外信息（保留字段，暂时无用）。
                                 * 设置来源后，在客服会话界面的"用户资料"栏的页面项，可以看到这里设置的值。
                                 */
                                ConsultSource source = new ConsultSource("app", "app", "custom information string");
                                /**
                                 * 请注意： 调用该接口前，应先检查Unicorn.isServiceAvailable()，
                                 * 如果返回为false，该接口不会有任何动作
                                 *
                                 * @param context 上下文
                                 * @param title   聊天窗口的标题
                                 * @param source  咨询的发起来源，包括发起咨询的url，title，描述信息等
                                 */
                                Unicorn.openServiceActivity(mContext, title, source);
                            }
                        });
                        break;
                    case 3:

                        holder.btn1.setVisibility(View.VISIBLE);
                        holder.btn1.setText("查看物流");
                        holder.btn2.setText("确认收货");
                        holder.btn2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!MyUtils.isFastClick()){
                                    return;
                                }
                                updateOrder("1",item.getId(),position);
//                                MyUtils.showToast(mContext,"确认收货！");
                            }
                        });
                        holder.btn1.setOnClickListener(new View.OnClickListener() {
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
                        break;
                    case 4:

                        holder.btn1.setVisibility(View.GONE);
                        holder.btn2.setVisibility(View.VISIBLE);
                        if("已评价".equals(item.getComment())){
                            holder.btn2.setText("已评价");
                            holder.btn2.setEnabled(false);
                        }else{
                            holder.btn2.setText("去评价");
                            holder.btn2.setEnabled(true);
                        }
                        holder.btn2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!MyUtils.isFastClick()){
                                    return;
                                }
                                Intent intent=new Intent(mContext, JudgeActivity.class);
                                intent.putExtra("order",item);
                                mContext.startActivity(intent);
                            }
                        });
                        break;
                }
                break;
            case JF:
                holder.up_down.setVisibility(View.GONE);
                holder.lv_goods_in.setVisibility(View.GONE);
                holder.line.setVisibility(View.GONE);
                switch (order_status) {
                    case 0:
                        switch (Integer.parseInt(item.getOrdertype())){
                            case 0:
                                holder.status_tv.setText(item.getOrder_status());
                                holder.btn1.setVisibility(View.GONE);
                                holder.btn2.setVisibility(View.GONE);
                                /*holder.btn1.setText("取消订单");
                                holder.btn2.setText("去付款");
                                holder.btn1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        updateOrder("0",item.getId(),position);
//                                MyUtils.showToast(mContext,"取消订单！");
                                    }
                                });
                                holder.btn2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        mContext.startActivity(new Intent(mContext, OrderConfirmContext.class));
                                    }
                                });*/
                                break;
                            case 1:

                                holder.btn1.setVisibility(View.VISIBLE);
                                holder.btn2.setVisibility(View.VISIBLE);
                                holder.btn1.setText("取消订单");
                                holder.btn2.setText("去兑换");
                                holder.btn1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (!MyUtils.isFastClick()){
                                            return;
                                        }
                                        updateOrder("0",item.getId(),position);
                                    }
                                });
                                holder.btn2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (!MyUtils.isFastClick()){
                                            return;
                                        }
//                                        Intent intent=new Intent(mContext, OrderDetailActivity.class);
//                                        intent.putExtra("orderid",item.getId());
//                                        mContext.startActivity(intent);
                                        showPopupWindow(item);
                                    }
                                });
                                break;
                            case 2:

                                holder.btn1.setVisibility(View.GONE);
                                holder.btn2.setText("提醒发货");
                                holder.btn2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (!MyUtils.isFastClick()){
                                            return;
                                        }
                                        String title = "伊穆家园客服";
                                        /**
                                         * 设置访客来源，标识访客是从哪个页面发起咨询的，用于客服了解用户是从什么页面进入。
                                         * 三个参数分别为：来源页面的url，来源页面标题，来源页面额外信息（保留字段，暂时无用）。
                                         * 设置来源后，在客服会话界面的"用户资料"栏的页面项，可以看到这里设置的值。
                                         */
                                        ConsultSource source = new ConsultSource("app", "app", "custom information string");
                                        /**
                                         * 请注意： 调用该接口前，应先检查Unicorn.isServiceAvailable()，
                                         * 如果返回为false，该接口不会有任何动作
                                         *
                                         * @param context 上下文
                                         * @param title   聊天窗口的标题
                                         * @param source  咨询的发起来源，包括发起咨询的url，title，描述信息等
                                         */
                                        Unicorn.openServiceActivity(mContext, title, source);
                                    }
                                });
                                break;
                            case 3:

                                holder.btn1.setVisibility(View.VISIBLE);
                                holder.btn2.setVisibility(View.VISIBLE);
                                holder.btn1.setText("查看物流");
                                holder.btn2.setText("确认收货");
                                holder.btn2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (!MyUtils.isFastClick()){
                                            return;
                                        }
                                        updateOrder("1",item.getId(),position);
//                                MyUtils.showToast(mContext,"确认收货！");
                                    }
                                });
                                holder.btn1.setOnClickListener(new View.OnClickListener() {
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
                                break;
                            case 4:

                                holder.btn1.setVisibility(View.GONE);
                                holder.btn2.setVisibility(View.VISIBLE);
                                if("已评价".equals(item.getComment())){
                                    holder.btn2.setText("已评价");
                                    holder.btn2.setEnabled(false);
                                }else{
                                    holder.btn2.setText("去评价");
                                    holder.btn2.setEnabled(true);
                                }
                                holder.btn2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (!MyUtils.isFastClick()){
                                            return;
                                        }
                                        Intent intent=new Intent(mContext, JudgeActivity.class);
                                        intent.putExtra("order",item);
                                        mContext.startActivity(intent);
                                    }
                                });
                                break;
                        }
                        break;
                    case 1:

                        holder.btn1.setVisibility(View.VISIBLE);
                        holder.btn1.setText("取消订单");
                        holder.btn2.setText("去兑换");
                        holder.btn1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!MyUtils.isFastClick()){
                                    return;
                                }
                                updateOrder("0",item.getId(),position);
                            }
                        });
                        holder.btn2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!MyUtils.isFastClick()){
                                    return;
                                }
                                MyUtils.showToast(mContext,"去兑换");
                            }
                        });
                        break;
                    case 2:

                        holder.btn1.setVisibility(View.GONE);
                        holder.btn2.setText("提醒发货");
                        holder.btn2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!MyUtils.isFastClick()){
                                    return;
                                }
                                String title = "伊穆家园客服";
                                /**
                                 * 设置访客来源，标识访客是从哪个页面发起咨询的，用于客服了解用户是从什么页面进入。
                                 * 三个参数分别为：来源页面的url，来源页面标题，来源页面额外信息（保留字段，暂时无用）。
                                 * 设置来源后，在客服会话界面的"用户资料"栏的页面项，可以看到这里设置的值。
                                 */
                                ConsultSource source = new ConsultSource("app", "app", "custom information string");
                                /**
                                 * 请注意： 调用该接口前，应先检查Unicorn.isServiceAvailable()，
                                 * 如果返回为false，该接口不会有任何动作
                                 *
                                 * @param context 上下文
                                 * @param title   聊天窗口的标题
                                 * @param source  咨询的发起来源，包括发起咨询的url，title，描述信息等
                                 */
                                Unicorn.openServiceActivity(mContext, title, source);
                            }
                        });
                        break;
                    case 3:

                        holder.btn1.setVisibility(View.VISIBLE);
                        holder.btn1.setText("查看物流");
                        holder.btn2.setText("确认收货");
                        holder.btn2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!MyUtils.isFastClick()){
                                    return;
                                }
                                MyUtils.showToast(mContext,"确认收货！");
                            }
                        });
                        holder.btn1.setOnClickListener(new View.OnClickListener() {
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
                        break;
                    case 4:

                        holder.btn1.setVisibility(View.GONE);
                        holder.btn2.setVisibility(View.VISIBLE);
                        if("已评价".equals(item.getComment())){
                            holder.btn2.setText("已评价");
                            holder.btn2.setEnabled(false);
                        }else{
                            holder.btn2.setText("去评价");
                            holder.btn2.setEnabled(true);
                        }
                        holder.btn2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!MyUtils.isFastClick()){
                                    return;
                                }
                                Intent intent=new Intent(mContext, JudgeActivity.class);
                                intent.putExtra("order",item);
                                mContext.startActivity(intent);
                            }
                        });
                        break;
                }
                break;
            case CZ:
                holder.up_down.setVisibility(View.GONE);
                holder.lv_goods_in.setVisibility(View.GONE);
                holder.line.setVisibility(View.GONE);
                switch (order_status) {
                    case 0:
                        switch (Integer.parseInt(item.getOrdertype())){
                            case 2:
                                holder.status_tv.setText(item.getOrder_status());
                                holder.btn1.setVisibility(View.GONE);
                                holder.btn2.setVisibility(View.VISIBLE);
                                if (item.getAddress()==null){
                                    holder.btn2.setText("去填写地址");
                                    holder.status_tv.setText("待领取");
                                }else{
                                    holder.status_tv.setText(item.getOrder_status());
                                    holder.btn2.setText("提醒发货");
                                }

                                holder.btn2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (!MyUtils.isFastClick()){
                                            return;
                                        }
                                        if (item.getAddress()==null){
                                            Intent intent=new Intent(mContext, OrderConfirmActivity3.class);
                                            intent.putExtra("orderid",item.getId());
                                            mContext.startActivity(intent);
                                        }else{
                                            String title = "伊穆家园客服";
                                            /**
                                             * 设置访客来源，标识访客是从哪个页面发起咨询的，用于客服了解用户是从什么页面进入。
                                             * 三个参数分别为：来源页面的url，来源页面标题，来源页面额外信息（保留字段，暂时无用）。
                                             * 设置来源后，在客服会话界面的"用户资料"栏的页面项，可以看到这里设置的值。
                                             */
                                            ConsultSource source = new ConsultSource("app", "app", "custom information string");
                                            /**
                                             * 请注意： 调用该接口前，应先检查Unicorn.isServiceAvailable()，
                                             * 如果返回为false，该接口不会有任何动作
                                             *
                                             * @param context 上下文
                                             * @param title   聊天窗口的标题
                                             * @param source  咨询的发起来源，包括发起咨询的url，title，描述信息等
                                             */
                                            Unicorn.openServiceActivity(mContext, title, source);
                                        }

                                    }
                                });
                                break;
                            case 3:
                                holder.status_tv.setText(item.getOrder_status());
                                holder.btn1.setVisibility(View.VISIBLE);
                                holder.btn2.setVisibility(View.VISIBLE);
                                holder.btn1.setText("查看物流");
                                holder.btn2.setText("确认收货");
                                holder.btn2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (!MyUtils.isFastClick()){
                                            return;
                                        }
                                        MyUtils.showToast(mContext,"确认收货！");
                                    }
                                });
                                holder.btn1.setOnClickListener(new View.OnClickListener() {
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
                                break;
                            case 4:
                                holder.status_tv.setText(item.getOrder_status());
                                holder.btn1.setVisibility(View.GONE);
                                holder.btn2.setVisibility(View.VISIBLE);
                                if("已评价".equals(item.getComment())){
                                    holder.btn2.setText("已评价");
                                    holder.btn2.setEnabled(false);
                                }else{
                                    holder.btn2.setText("去评价");
                                    holder.btn2.setEnabled(true);
                                }
                                holder.btn2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (!MyUtils.isFastClick()){
                                            return;
                                        }
                                        Intent intent=new Intent(mContext, JudgeActivity.class);
                                        intent.putExtra("order",item);
                                        mContext.startActivity(intent);
                                    }
                                });
                                break;
                        }
                        break;
                    case 2:

                        holder.btn1.setVisibility(View.GONE);
                        holder.btn2.setVisibility(View.VISIBLE);
                        if (item.getAddress()==null){
                            holder.btn2.setText("去填写地址");
                            holder.status_tv.setText("待领取");
                        }else{
                            holder.status_tv.setText(item.getOrder_status());
                            holder.btn2.setText("提醒发货");
                        }

                        holder.btn2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!MyUtils.isFastClick()){
                                    return;
                                }
                                if (item.getAddress()==null){
                                    Intent intent=new Intent(mContext, OrderConfirmActivity3.class);
                                    intent.putExtra("orderid",item.getId());
                                    mContext.startActivity(intent);
                                }else{
                                    String title = "伊穆家园客服";
                                    /**
                                     * 设置访客来源，标识访客是从哪个页面发起咨询的，用于客服了解用户是从什么页面进入。
                                     * 三个参数分别为：来源页面的url，来源页面标题，来源页面额外信息（保留字段，暂时无用）。
                                     * 设置来源后，在客服会话界面的"用户资料"栏的页面项，可以看到这里设置的值。
                                     */
                                    ConsultSource source = new ConsultSource("app", "app", "custom information string");
                                    /**
                                     * 请注意： 调用该接口前，应先检查Unicorn.isServiceAvailable()，
                                     * 如果返回为false，该接口不会有任何动作
                                     *
                                     * @param context 上下文
                                     * @param title   聊天窗口的标题
                                     * @param source  咨询的发起来源，包括发起咨询的url，title，描述信息等
                                     */
                                    Unicorn.openServiceActivity(mContext, title, source);
                                }

                            }
                        });
                        break;
                    case 3:
                        holder.status_tv.setText(item.getOrder_status());
                        holder.btn1.setVisibility(View.VISIBLE);
                        holder.btn2.setVisibility(View.VISIBLE);
                        holder.btn1.setText("查看物流");
                        holder.btn2.setText("确认收货");
                        holder.btn2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!MyUtils.isFastClick()){
                                    return;
                                }
                                MyUtils.showToast(mContext,"确认收货！");
                            }
                        });
                        holder.btn1.setOnClickListener(new View.OnClickListener() {
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
                        break;
                    case 4:
                        holder.status_tv.setText(item.getOrder_status());
                        holder.btn1.setVisibility(View.GONE);
                        holder.btn2.setVisibility(View.VISIBLE);
                        if("已评价".equals(item.getComment())){
                            holder.btn2.setText("已评价");
                            holder.btn2.setEnabled(false);
                        }else{
                            holder.btn2.setText("去评价");
                            holder.btn2.setEnabled(true);
                        }
                        holder.btn2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!MyUtils.isFastClick()){
                                    return;
                                }
                                Intent intent=new Intent(mContext, JudgeActivity.class);
                                intent.putExtra("order",item);
                                mContext.startActivity(intent);
                            }
                        });
                        break;
                }
                break;
            case TL:
                holder.up_down.setVisibility(View.VISIBLE);
//                holder.lv_goods_in.setAdapter(new GoodsInAdapter(mContext,new ArrayList<News>()));
                holder.up_down.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b==true){
                            map.put(position,true);
                        }else{
                            map.remove(position);
                        }
                        notifyDataSetChanged();
                    }
                });
                if (map!=null&&map.containsKey(position)){
                    holder.up_down.setChecked(true);
                    holder.lv_goods_in.setVisibility(View.VISIBLE);
                    holder.line.setVisibility(View.VISIBLE);
                }else{
                    holder.up_down.setChecked(false);
                    holder.lv_goods_in.setVisibility(View.GONE);
                    holder.line.setVisibility(View.GONE);
                }
                switch (order_status) {
                    case 0:

                        holder.btn1.setVisibility(View.VISIBLE);
                        holder.btn2.setVisibility(View.VISIBLE);
                        holder.btn1.setText("取消订单");
                        holder.btn2.setText("去付款");
                        holder.btn1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!MyUtils.isFastClick()){
                                    return;
                                }
                                MyUtils.showToast(mContext,"取消订单！");
                            }
                        });
                        holder.btn2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!MyUtils.isFastClick()){
                                    return;
                                }
//                                Intent intent=new Intent(mContext, OrderDetailActivity.class);
//                                intent.putExtra("orderid",item.getId());
//                                mContext.startActivity(intent);
                                showPopupWindow(item);
                            }
                        });
                        break;
                    case 1:

                        holder.btn1.setVisibility(View.VISIBLE);
                        holder.btn2.setVisibility(View.VISIBLE);
                        holder.btn1.setText("取消订单");
                        holder.btn2.setText("去付款");
                        holder.btn1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!MyUtils.isFastClick()){
                                    return;
                                }
                                MyUtils.showToast(mContext,"取消订单！");
                            }
                        });
                        holder.btn2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!MyUtils.isFastClick()){
                                    return;
                                }
//                                Intent intent=new Intent(mContext, OrderDetailActivity.class);
//                                intent.putExtra("orderid",item.getId());
//                                mContext.startActivity(intent);
                                showPopupWindow(item);
                            }
                        });
                        break;
                    case 2:

                        holder.btn1.setVisibility(View.GONE);
                        holder.btn2.setVisibility(View.VISIBLE);
                        holder.btn2.setText("提醒发货");
                        holder.btn2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!MyUtils.isFastClick()){
                                    return;
                                }
                                String title = "伊穆家园客服";
                                /**
                                 * 设置访客来源，标识访客是从哪个页面发起咨询的，用于客服了解用户是从什么页面进入。
                                 * 三个参数分别为：来源页面的url，来源页面标题，来源页面额外信息（保留字段，暂时无用）。
                                 * 设置来源后，在客服会话界面的"用户资料"栏的页面项，可以看到这里设置的值。
                                 */
                                ConsultSource source = new ConsultSource("app", "app", "custom information string");
                                /**
                                 * 请注意： 调用该接口前，应先检查Unicorn.isServiceAvailable()，
                                 * 如果返回为false，该接口不会有任何动作
                                 *
                                 * @param context 上下文
                                 * @param title   聊天窗口的标题
                                 * @param source  咨询的发起来源，包括发起咨询的url，title，描述信息等
                                 */
                                Unicorn.openServiceActivity(mContext, title, source);
                            }
                        });
                        break;
                    case 3:

                        holder.btn1.setVisibility(View.VISIBLE);
                        holder.btn2.setVisibility(View.VISIBLE);
                        holder.btn1.setText("查看物流");
                        holder.btn2.setText("确认收货");
                        holder.btn2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!MyUtils.isFastClick()){
                                    return;
                                }
                                MyUtils.showToast(mContext,"确认收货！");
                            }
                        });
                        holder.btn1.setOnClickListener(new View.OnClickListener() {
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
                        break;
                    case 4:

                        holder.btn1.setVisibility(View.GONE);
                        holder.btn2.setVisibility(View.VISIBLE);
                        if("已评价".equals(item.getComment())){
                            holder.btn2.setText("已评价");
                            holder.btn2.setEnabled(false);
                        }else{
                            holder.btn2.setText("去评价");
                            holder.btn2.setEnabled(true);
                        }
                        holder.btn2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!MyUtils.isFastClick()){
                                    return;
                                }
                                Intent intent=new Intent(mContext, JudgeActivity.class);
                                intent.putExtra("order",item);
                                mContext.startActivity(intent);
                            }
                        });
                        break;
                }
                break;
        }

        return convertView;
    }
    public void updateOrder(String option,String orderid, final int position){
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("orderid", orderid);//传递键值对参数
        formBody.add("option", option);//传递键值对参数
        formBody.add("userid", Global.loginResult.getId());//传递键值对参数
//new call
        Call call = MyOkHttp.GetCall("order.updateOrderStatus",formBody);
//请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("------------",e.toString());
//                        myHandler.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result=response.body().string();
                Log.d("------取消订单或确认收货结果------",result);
                Message message=new Message();
                message.what=0;
                Bundle bundle=new Bundle();
                bundle.putString("result",result);
                bundle.putInt("position",position);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    Handler myHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle=msg.getData();
            String result=bundle.getString("result");
            int position=bundle.getInt("position");
            switch (msg.what){
                case 0:
                    try {
                        JSONObject jsonObject=new JSONObject(result);
                        String code=jsonObject.getString("code");
                        String message=jsonObject.getString("message");
                        String data=jsonObject.getString("data");
                        if ("200".equals(code)){
//                            grouplists.remove(position);
                            EventBus.getDefault().post(new UpdateEvent(""));
                            MyUtils.showToast(mContext,message);
                        }else {
                            MyUtils.showToast(mContext,message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2://支付宝支付
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            orderInfo = data;
                            alipay();
                        } else {
                            MyUtils.showToast(mContext,message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3://支付宝结果
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        MyUtils.showToast(mContext, "支付成功");
                        mPopupWindow.dismiss();
                        EventBus.getDefault().post(new UpdateEvent(""));
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        MyUtils.showToast(mContext, "支付失败");
                    }
                    break;
                case 5://余额支付
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            if (mPopupWindow!=null&&mPopupWindow.isShowing()){
                                mPopupWindow.dismiss();
                            }
                            MyUtils.showToast(mContext,message);
                            EventBus.getDefault().post(new UpdateEvent(""));
                        } else{
                            MyUtils.showToast(mContext,message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 6://微信支付
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            WXpayInfo payInfo = gson.fromJson(data,WXpayInfo.class);
                            WXPay(payInfo);
                        } else{
                            MyUtils.showToast(mContext,message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    public void case0(){

    }
    public class ViewHolder {
        public TextView order_time;
        public TextView order_num;
        public TextView status_tv;
        public TextView total_tv;
        public TextView btn1;
        public TextView btn2;
        public MyListView lv_goods;
        public MyListView lv_goods_in;
        public CheckBox up_down;
        public View line;
    }

    /**
     * 弹出Popupwindow
     */
    public void showPopupWindow(final Order order) {
        View popupWindow_view = LayoutInflater.from(mContext).inflate(R.layout.payment_layout, null);
        LinearLayout yepay_ll = popupWindow_view.findViewById(R.id.yepay_ll);
        LinearLayout weixin_ll = popupWindow_view.findViewById(R.id.weixin_ll);
        final LinearLayout alipay_ll = popupWindow_view.findViewById(R.id.alipay_ll);
        Button cz_btn = popupWindow_view.findViewById(R.id.cz_btn);
        cz_btn.setText("立即支付");
        Button cancel_btn = popupWindow_view.findViewById(R.id.cancel_btn);
        final CheckBox weixin_check = popupWindow_view.findViewById(R.id.weixin_check);
        final CheckBox alipay_check = popupWindow_view.findViewById(R.id.alipay_check);
        final CheckBox yepay_check = popupWindow_view.findViewById(R.id.yepay_check);
//        yepay_ll.setVisibility(View.VISIBLE);
        weixin_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (weixin_check.isChecked()) {
                    weixin_check.setChecked(false);
                } else {
                    weixin_check.setChecked(true);
                    alipay_check.setChecked(false);
                    yepay_check.setChecked(false);
                }
            }
        });
        alipay_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alipay_check.isChecked()) {
                    alipay_check.setChecked(false);
                } else {
                    alipay_check.setChecked(true);
                    weixin_check.setChecked(false);
                    yepay_check.setChecked(false);
                }
            }
        });
        yepay_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (yepay_check.isChecked()) {
                    yepay_check.setChecked(false);
                } else {
                    alipay_check.setChecked(false);
                    weixin_check.setChecked(false);
                    yepay_check.setChecked(true);
                }
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
//                Intent intent=new Intent(mContext,OrderNormalActivity2.class);
//                intent.putExtra("type",0);
//                startActivity(intent);
//                finish();
            }
        });
        cz_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyUtils.isFastClick()){
                    return;
                }
                if (alipay_check.isChecked() == true) {
                    alipayrequest(order.getId());
                } else if (weixin_check.isChecked() == true) {
                    WXpayrequest(order.getId());
                } else if (yepay_check.isChecked() == true) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setIcon(android.R.drawable.ic_dialog_info);
                    builder.setTitle("温馨提示");
                    builder.setMessage("余额支付"+order.getTotalmoney()+"元，确定要继续吗");
                    builder.setCancelable(true);

                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Walletpayrequest(order.getId());
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            /*
                             *  在这里实现你自己的逻辑
                             */
                        }
                    });
                    builder.create().show();
                } else {
                    Toast.makeText(mContext, "请选择支付方式",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        mPopupWindow = new PopupWindow(popupWindow_view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setAnimationStyle(R.style.popwindow_anim_style);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                MyUtils.setWindowAlpa(mContext, false);
            }
        });
        if (mPopupWindow != null && !mPopupWindow.isShowing()) {
            mPopupWindow.showAtLocation(popupWindow_view, Gravity.BOTTOM, 0, 0);
        }
        MyUtils.setWindowAlpa(mContext, true);
    }

    //    支付宝支付
    private void alipay() {
/**
 * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
 * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
 * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
 *
 * orderInfo的获取必须来自服务端；
 */
        /*boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;*/
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask((Activity) mContext);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Message msg = new Message();
                msg.what = 3;
                msg.obj = result;
                myHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    //微信支付

    private void WXPay(WXpayInfo payInfo) {
        IWXAPI api = WXAPIFactory.createWXAPI(mContext, Constants.APP_ID);
        PayReq request = new PayReq();
        request.appId = payInfo.getAppid();
        request.partnerId = payInfo.getPartnerid();
        request.prepayId = payInfo.getPrepayid();
        request.packageValue = payInfo.getPackageX();
        request.nonceStr = payInfo.getNoncestr();
        request.timeStamp = payInfo.getTimestamp();
        request.sign = payInfo.getSign();
        api.sendReq(request);
    }
    public void alipayrequest(String orderid) {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("paytype", "COMMON");//传递键值对参数
        formBody.add("orderid", orderid);//传递键值对参数
//new call
        Call call = MyOkHttp.GetCall("pay.alipay", formBody);
//请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("------------", e.toString());
//                myHandler.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                MyUtils.e("------支付宝支付------", result);
                Message message = new Message();
                message.what = 2;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }
    public void Walletpayrequest(String orderid) {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("paytype", "COMMON");//传递键值对参数
        formBody.add("orderid", orderid);//传递键值对参数
        formBody.add("userid", Global.loginResult.getId());
//new call
        Call call = MyOkHttp.GetCall("pay.wallet", formBody);
//请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("------------", e.toString());
//                myHandler.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                MyUtils.e("------余额支付------", result);
                Message message = new Message();
                message.what = 5;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }
    public void WXpayrequest(String orderid) {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("paytype", "COMMON");//传递键值对参数
        formBody.add("orderid", orderid);//传递键值对参数
//new call
        Call call = MyOkHttp.GetCall("pay.wxpay", formBody);
//请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("------------", e.toString());
//                myHandler.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                MyUtils.e("------微信支付------", result);
                Message message = new Message();
                message.what = 6;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }
}

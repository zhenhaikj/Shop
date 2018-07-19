package com.emjiayuan.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.activity.LoginActivity;
import com.emjiayuan.app.entity.Coupon;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.event.UpdateEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

/**
 * Created by cyl on 2018年5月10日 09:52:49.
 */

public class CouponGetAdapter extends BaseAdapter {
    private Context mContext;
    private Integer[] bgs=new Integer[]{R.drawable.coupon1,R.drawable.coupon3,R.drawable.coupon4};


    public ArrayList<Coupon> getGrouplists() {
        return grouplists;
    }

    public void setGrouplists(ArrayList<Coupon> grouplists) {
        this.grouplists = grouplists;
        notifyDataSetChanged();
    }

    private ArrayList<Coupon> grouplists = new ArrayList<>();
    private LayoutInflater mInflater;

    public CouponGetAdapter(Context mContext, ArrayList<Coupon> grouplists) {
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
    public Coupon getItem(int position) {
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

        final Coupon item = grouplists.get(position);
//        if ("1".equals())
        if (item.getIsreceive()==1){
            holder.rl.setBackgroundResource(R.drawable.coupon2);
            holder.get.setText("已领取");
            holder.get.setEnabled(false);
            holder.get.setTextColor(Color.parseColor("#CFCFCF"));
            holder.get.setBackgroundResource(R.drawable.get_shape1);
        }else{
            holder.get.setText("立即领取");
            holder.get.setEnabled(true);
            holder.rl.setBackgroundResource(bgs[(int)(Math.random()*3)]);
            holder.get.setTextColor(Color.parseColor("#FEA080"));
            holder.get.setBackgroundResource(R.drawable.get_shape);
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
                getCoupon(item.getId(),position);
            }
        });
        holder.content.setText(item.getSubtitle());


        return convertView;
    }
    public void getCoupon(String couponid, final int position){
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("couponid", couponid);//传递键值对参数
        if (Global.loginResult==null){
            mContext.startActivity(new Intent(mContext, LoginActivity.class));
            return;
        }
        formBody.add("userid", Global.loginResult.getId());//传递键值对参数
//new call
        Call call = MyOkHttp.GetCall("userCoupon.receiveCoupon",formBody);
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
                Log.d("------领取优惠券结果------",result);
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
//                            notifyDataSetChanged();
                            EventBus.getDefault().post(new UpdateEvent(""));
                            MyUtils.showToast(mContext,message);
                        }else {
                            MyUtils.showToast(mContext,message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

            }
        }
    };
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

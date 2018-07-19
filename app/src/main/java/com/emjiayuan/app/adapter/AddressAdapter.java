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
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.activity.address.AddressActivity;
import com.emjiayuan.app.activity.address.ModifyAddressActivity;
import com.emjiayuan.app.entity.Address;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.event.AddressEvent;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

/**
 * Created by cyl on 2018年5月10日 09:52:49.
 */

public class AddressAdapter extends BaseAdapter {
    private Context mContext;

    private ArrayList<Address> grouplists = new ArrayList<>();

    public ArrayList<Address> getGrouplists() {
        return grouplists;
    }

    public void setGrouplists(ArrayList<Address> grouplists) {
        this.grouplists = grouplists;
        notifyDataSetChanged();
    }

    private LayoutInflater mInflater;

    public AddressAdapter(Context mContext, ArrayList<Address> grouplists) {
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
    public Address getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.address_item, null);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.phone = (TextView) convertView.findViewById(R.id.phone);
            holder.address = (TextView) convertView.findViewById(R.id.address);
            holder.tv_default = (TextView) convertView.findViewById(R.id.tv_default);
            holder.check = (CheckBox) convertView.findViewById(R.id.check);
            holder.default_ll = (LinearLayout) convertView.findViewById(R.id.default_ll);
            holder.edit_ll = (LinearLayout) convertView.findViewById(R.id.edit_ll);
            holder.delete_ll = (LinearLayout) convertView.findViewById(R.id.delete_ll);
            convertView.setTag(holder);
        } else {// 如果之前已经显示过该页面，则用viewholder中的缓存直接刷屏
            holder = (ViewHolder) convertView.getTag();
        }

        final Address item = grouplists.get(position);
        holder.name.setText(item.getUsername());
        holder.phone.setText(item.getTelphone());
        holder.address.setText(item.getAddress());
        if ("1".equals(item.getIs_default())){
            holder.tv_default.setText("默认地址");
            holder.check.setChecked(true);
        }else{
            holder.tv_default.setText("设为默认");
            holder.check.setChecked(false);
        }
        holder.default_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyUtils.isFastClick()){
                    return;
                }
                setDefault(item.getId(),position);
            }
        });
        holder.edit_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyUtils.isFastClick()){
                    return;
                }
                Intent intent=new Intent(mContext, ModifyAddressActivity.class);
                intent.putExtra("address",item);
                mContext.startActivity(intent);
            }
        });
        holder.delete_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyUtils.isFastClick()){
                    return;
                }
                delete(item.getId(),position);
            }
        });

        return convertView;
    }
    public void delete(String addresssid, final int position){
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("addressid", addresssid);//传递键值对参数
        formBody.add("userid", Global.loginResult.getId());//传递键值对参数
//new call
        Call call = MyOkHttp.GetCall("userAddress.removeAddress",formBody);
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
                Log.d("------删除地址结果------",result);
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

    public void setDefault(String addresssid, final int position){
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("addressid", addresssid);//传递键值对参数
        formBody.add("userid", Global.loginResult.getId());//传递键值对参数
//new call
        Call call = MyOkHttp.GetCall("userAddress.setDefaultAddress",formBody);
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
                Log.d("------设置默认地址结果------",result);
                Message message=new Message();
                message.what=1;
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
                            grouplists.remove(position);
                            notifyDataSetChanged();
                            MyUtils.showToast(mContext,message);
                        }else {
                            MyUtils.showToast(mContext,message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {
                        JSONObject jsonObject=new JSONObject(result);
                        String code=jsonObject.getString("code");
                        String message=jsonObject.getString("message");
                        String data=jsonObject.getString("data");
                        if ("200".equals(code)){
                            for (int i=0;i<grouplists.size();i++){
                                if (i==position){
                                    grouplists.get(i).setIs_default("1");
                                }else{
                                    grouplists.get(i).setIs_default("0");
                                }
                            }
                            notifyDataSetChanged();
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
    public class ViewHolder {
        public CheckBox check;
        public TextView name;
        public TextView phone;
        public TextView address;
        public TextView tv_default;
        public LinearLayout default_ll;
        public LinearLayout edit_ll;
        public LinearLayout delete_ll;
    }
}

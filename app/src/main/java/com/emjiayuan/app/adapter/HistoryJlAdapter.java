package com.emjiayuan.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.GlideUtil;
import com.emjiayuan.app.entity.Product;
import com.emjiayuan.app.event.ColUpdateEvent;
import com.emjiayuan.app.fragment.ShoppingCar.ShoppingCarFragment2;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cyl on 2018年5月10日 09:52:49.
 */

public class HistoryJlAdapter extends BaseAdapter {
    private Context mContext;
    private Map<Integer,Boolean> map=new HashMap();
    private ShoppingCarFragment2.AllCheckListener allCheckListener;
    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean isFlag() {
        return flag;
    }

    private boolean flag=false;


    public Map<Integer, Boolean> getMap() {
        return map;
    }

    public void setMap(Map<Integer, Boolean> map) {
        this.map = map;
        notifyDataSetChanged();
    }


    private List<Product> grouplists = new ArrayList<>();
    private LayoutInflater mInflater;

    public HistoryJlAdapter(Context mContext, List<Product> grouplists, ShoppingCarFragment2.AllCheckListener listener) {
        super();
        this.mContext = mContext;
        this.grouplists = grouplists;
        this.allCheckListener=listener;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
//        return 20;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {// 如果是第一次显示该页面(要记得保存到viewholder中供下次直接从缓存中调用)
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.collection_item, null);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.check = (CheckBox) convertView.findViewById(R.id.check);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.fl_item = (FrameLayout) convertView.findViewById(R.id.fl_item);
            convertView.setTag(holder);
        } else {// 如果之前已经显示过该页面，则用viewholder中的缓存直接刷屏
            holder = (ViewHolder) convertView.getTag();
        }

        final Product item = grouplists.get(position);
        holder.icon.setBackgroundColor(Color.parseColor("#F4F4F4"));
        GlideUtil.loadImageViewLoding(mContext,item.getImages(),holder.icon,R.drawable.empty_img,R.drawable.empty_img);
//        holder.icon.setImageResource(item.getImages4());
        holder.name.setText(item.getName());
//        holder.check.setText(item.getYh());
        holder.price.setText("¥"+item.getPrice());
//        holder.price.setText(Html.fromHtml("<small>¥ </small><big><big>"+item.getPrice().substring(0,item.getPrice().indexOf("."))+"</big></big><small>"+item.getPrice().substring(item.getPrice().indexOf("."),item.getPrice().length())+"</small>"));
        if (flag){
            holder.check.setVisibility(View.VISIBLE);
        }else{
            holder.check.setVisibility(View.GONE);
        }

        holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    map.put(position,true);
                }else {
                    map.remove(position);
                }
                item.setChecked(b);
                //监听每个item，若所有checkbox都为选中状态则更改main的全选checkbox状态
                EventBus.getDefault().post(new ColUpdateEvent(grouplists));
                for (Product carBean : grouplists) {
                    if (!carBean.isChecked()) {
                        allCheckListener.onCheckedChanged(false);
                        return;
                    }
                }
                allCheckListener.onCheckedChanged(true);

            }
        });
        if(map!=null&&map.containsKey(position)){
            holder.check.setChecked(true);
        }else {
            holder.check.setChecked(false);
        }

        return convertView;
    }

    public class ViewHolder {
        public ImageView icon;
        public CheckBox check;
        public FrameLayout fl_item;
        public TextView name;
        public TextView price;
    }
}

package com.emjiayuan.app.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.GlideUtil;
import com.emjiayuan.app.entity.Product;
import com.emjiayuan.app.entity.SeckillBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cyl on 2018年5月10日 09:52:49.
 */

public class XsgAdapter extends BaseAdapter {
    private Context mContext;
    private static String TAG = "MenuAdapter";

    private List<SeckillBean.ProductListBean> grouplists = new ArrayList<>();
    private LayoutInflater mInflater;

    public XsgAdapter(Context mContext, List<SeckillBean.ProductListBean> grouplists) {
        super();
        this.mContext = mContext;
        this.grouplists = grouplists;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return grouplists.size()>3?3:grouplists.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {// 如果是第一次显示该页面(要记得保存到viewholder中供下次直接从缓存中调用)
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.xsg_item, null);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.yh = (TextView) convertView.findViewById(R.id.yh);
            holder.old = (TextView) convertView.findViewById(R.id.old);
            convertView.setTag(holder);
        } else {// 如果之前已经显示过该页面，则用viewholder中的缓存直接刷屏
            holder = (ViewHolder) convertView.getTag();
        }

        SeckillBean.ProductListBean item = grouplists.get(position);
        GlideUtil.loadImageViewLoding(mContext,item.getImages(),holder.icon,R.drawable.empty_img,R.drawable.empty_img);
//        Glide.with(mContext).load(item.getImages()).placeholder(R.drawable.empty_img).error(R.drawable.empty_img).into(holder.icon);
        holder.icon.setImageResource(R.drawable.img1);
        holder.name.setText(item.getName());
        holder.yh.setText("¥ "+item.getMs_price());
        holder.old.setText("¥ "+item.getPreprice());
        holder.old.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//设置中划线
        holder.old.getPaint().setAntiAlias(true);

        return convertView;
    }

    public class ViewHolder {
        public ImageView icon;
        public TextView name;
        public TextView yh;
        public TextView old;
    }
}

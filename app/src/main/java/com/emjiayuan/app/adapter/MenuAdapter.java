package com.emjiayuan.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.GlideUtil;
import com.emjiayuan.app.entity.MenuItem;

import java.util.ArrayList;

/**
 * Created by cyl on 2018年5月10日 09:52:49.
 */

public class MenuAdapter extends BaseAdapter {
    private Context mContext;
    private static String TAG = "MenuAdapter";

    private ArrayList<MenuItem> grouplists = new ArrayList<>();
    private LayoutInflater mInflater;

    public MenuAdapter(Context mContext, ArrayList<MenuItem> grouplists) {
        super();
        this.mContext = mContext;
        this.grouplists = grouplists;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return grouplists.size();
    }

    @Override
    public MenuItem getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.menu_item, null);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            holder.content = (TextView) convertView.findViewById(R.id.txt_content);
            convertView.setTag(holder);
        } else {// 如果之前已经显示过该页面，则用viewholder中的缓存直接刷屏
            holder = (ViewHolder) convertView.getTag();
        }

        MenuItem item = grouplists.get(position);
        GlideUtil.loadImageViewLoding(mContext,item.getIcon(),holder.icon,R.drawable.empty_img,R.drawable.empty_img);
        holder.content.setText(item.getName());

        return convertView;
    }

    public class ViewHolder {
        public ImageView icon;
        public TextView content;
    }
}

package com.emjiayuan.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.GlideUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cyl on 2018年5月10日 09:52:49.
 */

public class IconsAdapter extends BaseAdapter {
    private Context mContext;

    private List<String> grouplists = new ArrayList<>();
    private LayoutInflater mInflater;
    private int noMovePosition=-1;

    public IconsAdapter(Context mContext, List<String> grouplists) {
        super();
        this.mContext = mContext;
        this.grouplists = grouplists;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public List<String> getGrouplists() {
        return grouplists;
    }

    public void setGrouplists(List<String> grouplists) {
        this.grouplists = grouplists;
        notifyDataSetChanged();
    }

    public int getNoMovePosition() {
        return noMovePosition;
    }

    public void setNoMovePosition(int noMovePosition) {
        this.noMovePosition = noMovePosition;
    }

    @Override
    public int getCount() {
        return grouplists.size();
    }

    @Override
    public String getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.icons_item, null);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            holder.delete = (ImageView) convertView.findViewById(R.id.delete);
            convertView.setTag(holder);
        } else {// 如果之前已经显示过该页面，则用viewholder中的缓存直接刷屏
            holder = (ViewHolder) convertView.getTag();
        }

        String item = grouplists.get(position);
        if (item.equals("add")) {
            setNoMovePosition(position);
            holder.icon.setImageResource(R.drawable.add_img2);
            holder.delete.setVisibility(View.GONE);
        } else {
            holder.delete.setVisibility(View.VISIBLE);
            GlideUtil.loadImageViewLoding(mContext,item,holder.icon,R.drawable.empty_img,R.drawable.empty_img);
        }
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grouplists.remove(position);
                if (!grouplists.contains("add")){
                    grouplists.add("add");
                }
                notifyDataSetChanged();
            }
        });


        return convertView;
    }

    public class ViewHolder {
        public ImageView icon;
        public ImageView delete;
    }
}

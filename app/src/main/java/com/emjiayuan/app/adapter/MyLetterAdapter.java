package com.emjiayuan.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.GlideUtil;
import com.emjiayuan.app.entity.MyLetter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cyl on 2018年5月10日 09:52:49.
 */

public class MyLetterAdapter extends BaseAdapter {
    private Context mContext;

    private ArrayList<MyLetter> grouplists = new ArrayList<>();
    private LayoutInflater mInflater;

    public MyLetterAdapter(Context mContext, ArrayList<MyLetter> grouplists) {
        super();
        this.mContext = mContext;
        this.grouplists = grouplists;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
//        return 10;
        return grouplists.size();
    }

    @Override
    public MyLetter getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.my_letter_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {// 如果之前已经显示过该页面，则用viewholder中的缓存直接刷屏
            holder = (ViewHolder) convertView.getTag();
        }

        MyLetter item = grouplists.get(position);
        GlideUtil.loadImageViewLoding(mContext, item.getFrom_headimg(), holder.icon, R.drawable.default_tx, R.drawable.default_tx);
        holder.name.setText(item.getFrom_nickname());
        holder.time.setText(item.getPasttime());
        holder.content.setText(item.getContent());
        holder.count.setText(item.getNotrednum());
        holder.count.setVisibility("0".equals(item.getNotrednum())?View.GONE:View.VISIBLE);

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.icon)
        ImageView icon;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.count)
        TextView count;
        @BindView(R.id.content)
        TextView content;
        @BindView(R.id.time)
        TextView time;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

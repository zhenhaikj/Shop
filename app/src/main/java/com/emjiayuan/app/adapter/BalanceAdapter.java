package com.emjiayuan.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emjiayuan.app.R;
import com.emjiayuan.app.entity.Balance;

import java.util.ArrayList;

/**
 * Created by cyl on 2018年5月10日 09:52:49.
 */

public class BalanceAdapter extends BaseAdapter {
    private Context mContext;

    private ArrayList<Balance> grouplists = new ArrayList<>();
    private LayoutInflater mInflater;

    public ArrayList<Balance> getGrouplists() {
        return grouplists;
    }

    public void setGrouplists(ArrayList<Balance> grouplists) {
        this.grouplists = grouplists;
        notifyDataSetChanged();
    }

    public BalanceAdapter(Context mContext, ArrayList<Balance> grouplists) {
        super();
        this.mContext = mContext;
        this.grouplists = grouplists;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
//        return 20;
        return grouplists.size();
    }

    @Override
    public Balance getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.history_item, null);
            holder.tab_je = (TextView) convertView.findViewById(R.id.tab_je);
            holder.tab_sj = (TextView) convertView.findViewById(R.id.tab_sj);
            holder.tab_ll = (LinearLayout) convertView.findViewById(R.id.tab_ll);
            convertView.setTag(holder);
        } else {// 如果之前已经显示过该页面，则用viewholder中的缓存直接刷屏
            holder = (ViewHolder) convertView.getTag();
        }

        Balance item = grouplists.get(position);
        holder.tab_je.setText("¥"+item.getMoney());
        holder.tab_sj.setText(item.getCreatetime());
        if (position%2==0){
            holder.tab_ll.setBackgroundColor(Color.parseColor("#ffffff"));
        }else{
            holder.tab_ll.setBackgroundColor(Color.parseColor("#F0F0F0"));
        }

        return convertView;
    }

    public class ViewHolder {
        public TextView tab_je;
        public TextView tab_sj;
        public LinearLayout tab_ll;
    }
}

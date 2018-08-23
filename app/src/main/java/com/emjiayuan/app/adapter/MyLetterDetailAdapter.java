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
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.entity.MyLetterDetail;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cyl on 2018年5月10日 09:52:49.
 */

public class MyLetterDetailAdapter extends BaseAdapter {
    private Context mContext;

    private ArrayList<MyLetterDetail> grouplists = new ArrayList<>();
    private LayoutInflater mInflater;

    public MyLetterDetailAdapter(Context mContext, ArrayList<MyLetterDetail> grouplists) {
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
    public MyLetterDetail getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.my_letter_detail_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {// 如果之前已经显示过该页面，则用viewholder中的缓存直接刷屏
            holder = (ViewHolder) convertView.getTag();
        }

        MyLetterDetail item = grouplists.get(position);
        if (item.getFrom_userid().equals(Global.loginResult.getId())){
            holder.llFrom.setVisibility(View.GONE);
            holder.llTo.setVisibility(View.VISIBLE);
            GlideUtil.loadImageViewLoding(mContext, item.getFrom_headimg(), holder.myicon, R.drawable.default_tx, R.drawable.default_tx);
            holder.myname.setText(item.getFrom_nickname());
            holder.mytime.setText(item.getPasttime());
            holder.mycontent.setText(item.getContent());
        }else{
            holder.llFrom.setVisibility(View.VISIBLE);
            holder.llTo.setVisibility(View.GONE);
            GlideUtil.loadImageViewLoding(mContext, item.getFrom_headimg(), holder.icon, R.drawable.default_tx, R.drawable.default_tx);
            holder.name.setText(item.getFrom_nickname());
            holder.time.setText(item.getPasttime());
            holder.content.setText(item.getContent());
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.icon)
        ImageView icon;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.content)
        TextView content;
        @BindView(R.id.ll_from)
        LinearLayout llFrom;
        @BindView(R.id.mytime)
        TextView mytime;
        @BindView(R.id.myname)
        TextView myname;
        @BindView(R.id.mycontent)
        TextView mycontent;
        @BindView(R.id.myicon)
        ImageView myicon;
        @BindView(R.id.ll_to)
        LinearLayout llTo;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

package com.emjiayuan.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.emjiayuan.app.R;
import com.emjiayuan.app.entity.Vip;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cyl on 2018年5月10日 09:52:49.
 */

public class VipAdapter extends BaseAdapter {


    private Context mContext;
    private int now_id;


    private ArrayList<Vip> grouplists = new ArrayList<>();

    public ArrayList<Vip> getGrouplists() {
        return grouplists;
    }

    public void setGrouplists(ArrayList<Vip> grouplists) {
        this.grouplists = grouplists;
    }

    private LayoutInflater mInflater;

    public VipAdapter(Context mContext, ArrayList<Vip> grouplists,int now_id) {
        super();
        this.mContext = mContext;
        this.now_id = now_id;
        this.grouplists = grouplists;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
//        return 4;
        return grouplists.size();
    }

    @Override
    public Vip getItem(int position) {
        return grouplists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = mInflater.inflate(R.layout.vip_item2, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        Vip vip = grouplists.get(position);
        Glide.with(mContext).load(vip.getBackground()).into(holder.bg);
        if (now_id == Integer.parseInt(vip.getId())) {
            holder.join.setText("当前等级");
        } else {
            holder.join.setText("开通会员");
        }
        holder.join.setVisibility(Integer.parseInt(vip.getId()) <= now_id ? View.GONE : View.VISIBLE);
        holder.join.getBackground().setAlpha(160);
        holder.classname.setText(vip.getClassname());
        holder.cost.setText(vip.getCost() + "/年");
        holder.content.setText("购物享受" + Double.parseDouble(vip.getDiscount()) / 10 + "折");

        return view;
    }

    static class ViewHolder {
        @BindView(R.id.bg)
        ImageView bg;
        @BindView(R.id.classname)
        TextView classname;
        @BindView(R.id.join)
        TextView join;
        @BindView(R.id.content)
        TextView content;
        @BindView(R.id.cost)
        TextView cost;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

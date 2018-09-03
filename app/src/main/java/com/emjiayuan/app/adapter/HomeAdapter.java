package com.emjiayuan.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.activity.GoodsDetailActivity;
import com.emjiayuan.app.activity.TypeActivity;
import com.emjiayuan.app.entity.Products;
import com.emjiayuan.app.widget.MyGridView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cyl on 2018年5月10日 09:52:49.
 */

public class HomeAdapter extends BaseAdapter {


    private Context mContext;


    private ArrayList<Products> grouplists = new ArrayList<>();

    public ArrayList<Products> getGrouplists() {
        return grouplists;
    }

    public void setGrouplists(ArrayList<Products> grouplists) {
        this.grouplists = grouplists;
    }

    private LayoutInflater mInflater;

    public HomeAdapter(Context mContext, ArrayList<Products> grouplists) {
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
    public Products getItem(int position) {
        return grouplists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = mInflater.inflate(R.layout.home_item, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        final Products products = grouplists.get(position);
        holder.header.setText(products.getName());
        holder.headerLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyUtils.isFastClick()) {
                    return;
                }
                Intent intent = new Intent(mContext, TypeActivity.class);
                intent.putExtra("Products", products);
                mContext.startActivity(intent);
            }
        });
//        if (products.getProduct_list().size()<2){
        if ("0".equals(products.getType())){
            holder.gvGoods.setNumColumns(1);
        }else if ("1".equals(products.getType())){
            holder.gvGoods.setNumColumns(2);
        }else if ("2".equals(products.getType())){
            holder.gvGoods.setNumColumns(3);
        }
        holder.gvGoods.setVisibility(View.GONE);
        holder.gvGoods.setVisibility(View.VISIBLE);
        holder.gvGoods.setFocusable(false);
        holder.gvGoods.setAdapter(new TwzqAdapter(mContext, products.getProduct_list()));
        holder.gvGoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!MyUtils.isFastClick()) {
                    return;
                }
                Intent intent = new Intent(mContext, GoodsDetailActivity.class);
                intent.putExtra("productid", products.getProduct_list().get(i).getId());
                mContext.startActivity(intent);
            }
        });

        return view;
    }

    static class ViewHolder {
        @BindView(R.id.header)
        TextView header;
        @BindView(R.id.gv_goods)
        MyGridView gvGoods;
        @BindView(R.id.header_ll)
        LinearLayout headerLl;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

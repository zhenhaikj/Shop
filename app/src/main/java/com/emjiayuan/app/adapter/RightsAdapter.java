package com.emjiayuan.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.emjiayuan.app.R;
import com.emjiayuan.app.entity.Rights;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cyl on 2018年5月10日 09:52:49.
 */

public class RightsAdapter extends BaseAdapter {


    private Context mContext;


    private ArrayList<Rights> grouplists = new ArrayList<>();

    public ArrayList<Rights> getGrouplists() {
        return grouplists;
    }

    public void setGrouplists(ArrayList<Rights> grouplists) {
        this.grouplists = grouplists;
    }

    private LayoutInflater mInflater;

    public RightsAdapter(Context mContext, ArrayList<Rights> grouplists) {
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
    public Rights getItem(int position) {
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
            view = mInflater.inflate(R.layout.rights_item, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        Rights rights = grouplists.get(position);
        holder.icon.setImageResource(rights.getDrawable());
        holder.name.setText(rights.getName());
        holder.detail.setText(rights.getDetail());
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.icon)
        ImageView icon;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.detail)
        TextView detail;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

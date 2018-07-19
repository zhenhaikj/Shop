package com.emjiayuan.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.emjiayuan.app.R;
import com.emjiayuan.app.entity.News;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cyl on 2018年5月10日 09:52:49.
 */

public class MessageAdapter extends BaseAdapter {
    private Context mContext;
    private int checked;

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
        notifyDataSetChanged();
    }

    private ArrayList<News> grouplists = new ArrayList<>();

    public ArrayList<News> getGrouplists() {
        return grouplists;
    }

    public void setGrouplists(ArrayList<News> grouplists) {
        this.grouplists = grouplists;
    }

    private LayoutInflater mInflater;

    public MessageAdapter(Context mContext, ArrayList<News> grouplists) {
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
    public News getItem(int position) {
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
            view = mInflater.inflate(R.layout.message_item, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        News item=grouplists.get(position);
        holder.title.setText(item.getTitle());
        holder.time.setText(item.getCreatetime());
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.time)
        TextView time;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

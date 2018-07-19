package com.emjiayuan.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.emjiayuan.app.R;
import com.emjiayuan.app.entity.HelpBean;

import java.util.ArrayList;

/**
 * Created by cyl on 2018年5月10日 09:52:49.
 */

public class HelpAdapter extends BaseAdapter {
    private Context mContext;

    private ArrayList<HelpBean> grouplists = new ArrayList<>();

    public ArrayList<HelpBean> getGrouplists() {
        return grouplists;
    }

    public void setGrouplists(ArrayList<HelpBean> grouplists) {
        this.grouplists = grouplists;
        notifyDataSetChanged();
    }

    private LayoutInflater mInflater;

    public HelpAdapter(Context mContext, ArrayList<HelpBean> grouplists) {
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
    public HelpBean getItem(int position) {
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
//        final ViewHolder holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.help_item, null);
            holder.question = (TextView) convertView.findViewById(R.id.question);
            holder.answer = (TextView) convertView.findViewById(R.id.answer);

            convertView.setTag(holder);
        } else {// 如果之前已经显示过该页面，则用viewholder中的缓存直接刷屏
            holder = (ViewHolder) convertView.getTag();
        }

        final HelpBean item = grouplists.get(position);
        holder.question.setText("问："+item.getQuestion());
        holder.answer.setText("答："+item.getContent());


        return convertView;
    }

    public class ViewHolder {
        public TextView question;
        public TextView answer;

    }
}

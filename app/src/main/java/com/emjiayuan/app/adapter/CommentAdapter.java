package com.emjiayuan.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.GlideUtil;
import com.emjiayuan.app.entity.Comment;
import com.emjiayuan.app.widget.MyGridView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by cyl on 2018年5月10日 09:52:49.
 */

public class CommentAdapter extends BaseAdapter {
    private Context mContext;
    private IconsAdapter adapter;

    private ArrayList<Comment> grouplists = new ArrayList<>();
    private LayoutInflater mInflater;

    public CommentAdapter(Context mContext, ArrayList<Comment> grouplists) {
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
    public Comment getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.pl_item, null);
            holder.icon = (CircleImageView) convertView.findViewById(R.id.icon);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(holder);
        } else {// 如果之前已经显示过该页面，则用viewholder中的缓存直接刷屏
            holder = (ViewHolder) convertView.getTag();
        }

        Comment item = grouplists.get(position);
        GlideUtil.loadImageViewLoding(mContext,item.getHeadimg(),holder.icon,R.drawable.default_tx,R.drawable.default_tx);
        holder.name.setText(item.getNickname());
        holder.time.setText(item.getCreatetime());
        holder.content.setText(item.getComment());

        return convertView;
    }

    public class ViewHolder {
        public CircleImageView icon;
        public TextView name;
        public TextView content;
        public TextView time;
    }
}

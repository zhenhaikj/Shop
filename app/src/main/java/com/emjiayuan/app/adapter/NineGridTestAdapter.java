package com.emjiayuan.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emjiayuan.app.R;
import com.emjiayuan.app.entity.NineGridTestModel;
import com.emjiayuan.app.widget.MyListView;
import com.emjiayuan.app.widget.NineGridTestLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * 描述：
 * 作者：HMY
 * 时间：2016/5/13
 */
public class NineGridTestAdapter extends BaseAdapter {

    private Context mContext;
    private List<NineGridTestModel> mList;
    private MyListView myListView;
    protected LayoutInflater inflater;

    public NineGridTestAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(context);
    }

    public MyListView getMyListView() {
        return myListView;
    }

    public void setMyListView(MyListView myListView) {
        this.myListView = myListView;
    }

    public void setList(List<NineGridTestModel> list) {
        mList = list;
    }

    @Override
    public int getCount() {
        return getListSize(mList);
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = inflater.inflate(R.layout.post_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//        holder.layoutNineGrid.setIsShowAll(mList.get(position).isShowAll);
//        holder.layoutNineGrid.setUrlList(mList.get(position).urlList);

        return convertView;
    }


    private int getListSize(List<NineGridTestModel> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        return list.size();
    }

    static class ViewHolder {
        @BindView(R.id.icon)
        CircleImageView icon;
        @BindView(R.id.username)
        TextView username;
        @BindView(R.id.content)
        TextView content;
//        @BindView(R.id.layout_nine_grid)
//        NineGridTestLayout layoutNineGrid;
        @BindView(R.id.label)
        TextView label;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.zan_icon)
        ImageView zanIcon;
        @BindView(R.id.zan_count)
        TextView zanCount;
        @BindView(R.id.pl_count)
        TextView plCount;
        @BindView(R.id.ll_content)
        LinearLayout llContent;
        @BindView(R.id.ll_post)
        LinearLayout llPost;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

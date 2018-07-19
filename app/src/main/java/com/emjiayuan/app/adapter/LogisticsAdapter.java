package com.emjiayuan.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.DensityUtil;
import com.emjiayuan.app.entity.LogisticsBean;
import com.emjiayuan.app.entity.LogisticsJson;

import java.util.ArrayList;

public class LogisticsAdapter extends RecyclerView.Adapter<LogisticsAdapter.LogisticsAdapterHolder> {

    private Context context;
    private LayoutInflater mLayoutInflater;
    private ArrayList<LogisticsBean> list;

    public LogisticsAdapter(Context context, ArrayList<LogisticsBean> list) {
        this.context = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getItemCount() {
//        return 10;
        return list.size();
    }

    @Override
    public LogisticsAdapter.LogisticsAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LogisticsAdapter.LogisticsAdapterHolder(mLayoutInflater.inflate(R.layout.logistics_recycle_item, parent, false));
    }

    @Override
    public void onBindViewHolder(LogisticsAdapter.LogisticsAdapterHolder holder, int position) {

        try {
//            holder.tv_status.setText(bean.getData().getList().get(position).getContext());//订单状态
//            holder.tv_time.setText(bean.getData().getList().get(position).getTime());//时间
            LogisticsBean item=list.get(position);
            holder.tv_status.setText(item.getContext());//订单状态
            holder.tv_date.setText(item.getTime());//时间
            holder.tv_time.setVisibility(View.GONE);
//            holder.tv_time.setText("14:46");//时间

            if (position == 0) {
                //绿色的圆点
                holder.iv_status.setImageResource(R.drawable.green_dot);
                RelativeLayout.LayoutParams pointParams = new RelativeLayout.LayoutParams(DensityUtil.dp2px(context, 15), DensityUtil.dp2px(context, 15));
                pointParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                holder.iv_status.setLayoutParams(pointParams);

//                holder.tv_time.setTextColor(context.getResources().getColor(R.color.newPrimary));
//                holder.tv_status.setTextColor(context.getResources().getColor(R.color.newPrimary));

                //灰色的竖线
                RelativeLayout.LayoutParams lineParams = new RelativeLayout.LayoutParams(DensityUtil.dp2px(context, 1), ViewGroup.LayoutParams.MATCH_PARENT);
                lineParams.addRule(RelativeLayout.BELOW, R.id.iv_status);//让直线置于圆点下面
                lineParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                holder.iv_line.setLayoutParams(lineParams);

            } else if(position==getItemCount()-1){
                holder.iv_status.setImageResource(R.drawable.blue_dot);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(DensityUtil.dp2px(context, 15), DensityUtil.dp2px(context, 15));
                lp.addRule(RelativeLayout.CENTER_IN_PARENT);

                holder.iv_status.setLayoutParams(lp);

//                holder.tv_time.setTextColor(context.getResources().getColor(R.color.textColor_9b));
//                holder.tv_status.setTextColor(context.getResources().getColor(R.color.textColor_9b));

                //灰色的竖线
                RelativeLayout.LayoutParams lineParams = new RelativeLayout.LayoutParams(DensityUtil.dp2px(context, 1), ViewGroup.LayoutParams.MATCH_PARENT);
                lineParams.addRule(RelativeLayout.ABOVE, R.id.iv_status);//让直线置于圆点下面
                lineParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                holder.iv_line.setLayoutParams(lineParams);

            }else{
                holder.iv_status.setImageResource(R.drawable.blue_dot);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(DensityUtil.dp2px(context, 15), DensityUtil.dp2px(context, 15));
                lp.addRule(RelativeLayout.CENTER_IN_PARENT);

                holder.iv_status.setLayoutParams(lp);

//                holder.tv_time.setTextColor(context.getResources().getColor(R.color.textColor_9b));
//                holder.tv_status.setTextColor(context.getResources().getColor(R.color.textColor_9b));

                //灰色的竖线
                RelativeLayout.LayoutParams lineParams = new RelativeLayout.LayoutParams(DensityUtil.dp2px(context, 1), ViewGroup.LayoutParams.MATCH_PARENT);
//                lineParams.addRule(RelativeLayout.BELOW, R.id.iv_status);//让直线置于圆点下面
                lineParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                holder.iv_line.setLayoutParams(lineParams);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class LogisticsAdapterHolder extends RecyclerView.ViewHolder {

        ImageView iv_status;
        TextView tv_status;
        TextView tv_time;
        TextView tv_date;
        ImageView iv_line;

        LogisticsAdapterHolder(View view) {
            super(view);
            iv_line = (ImageView) view.findViewById(R.id.iv_line);
            iv_status = (ImageView) view.findViewById(R.id.iv_status);
            tv_status = (TextView) view.findViewById(R.id.tv_status);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_date = (TextView) view.findViewById(R.id.tv_date);

        }
    }
}
package com.emjiayuan.app.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.DigitUtil;
import com.emjiayuan.app.Utils.PinyinUtil;
import com.emjiayuan.app.entity.AreaModel;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 城市配器
 * Created by zst on 2017/8/2.
 */

public class CityAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<AreaModel> list;
    private Map<String, Integer> alphaIndexer;
    private String[] sections;
    private Context context;

    public void setList(List<AreaModel> list) {
        this.list = list;
        alphaIndexer = new HashMap<String, Integer>();
        sections = new String[list.size()];

        //把相邻的相同的首字母放到一起,同时首个字母显示
//        try {
            for (int i = 0; i < list.size(); i++) {
                String currentStr = DigitUtil.getPinYinFirst(list.get(i).getCityName());
                String previewStr = (i - 1) >= 0 ? DigitUtil.getPinYinFirst(list.get(i - 1).getCityName())
                        : " ";
                if (!previewStr.equals(currentStr)) {//前一个首字母与当前首字母不同时加入HashMap中同时显示该字母
                    String name = DigitUtil.getPinYinFirst(list.get(i).getCityName());
                    alphaIndexer.put(name, i);
                    sections[i] = name;
                }
            }
//        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
//            badHanyuPinyinOutputFormatCombination.printStackTrace();
//        }

        notifyDataSetChanged();
    }

    public CityAdapter(Context context, List<AreaModel> list) {

        //赋值初始化
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    public Map<String, Integer> getCityMap() {
        return alphaIndexer;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.city_item, null);
            holder = new ViewHolder();
            holder.alpha = (TextView) convertView
                    .findViewById(R.id.item_city_alpha);
            holder.name = (TextView) convertView
                    .findViewById(R.id.item_city_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(list.get(position).getCityName());
        String currentStr = null;
        String previewStr = null;
//        try {
            currentStr = DigitUtil.getPinYinFirst(list.get(position).getCityName());
            previewStr = (position - 1) >= 0 ? DigitUtil.getPinYinFirst(list.get(position - 1)
                    .getCityName()) : " ";
//        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
//            badHanyuPinyinOutputFormatCombination.printStackTrace();
//        }

        if (!previewStr.equals(currentStr)) {
            holder.alpha.setVisibility(View.VISIBLE);
            holder.alpha.setText(currentStr);
        } else {
            holder.alpha.setVisibility(View.GONE);
        }
        return convertView;
    }

    private class ViewHolder {
        TextView alpha;
        TextView name;
    }
}
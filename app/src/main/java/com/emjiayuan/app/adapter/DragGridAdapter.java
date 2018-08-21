package com.emjiayuan.app.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.emjiayuan.app.R;

import java.lang.reflect.Field;
import java.util.List;

public class DragGridAdapter extends ArrayAdapter<String> {
    List<String> list;
    public DragGridAdapter(Context context, List<String> objects) {
        super(context, 0, objects);
        this.list=objects;
    }
    public List<String> getList(){
        return list;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view==null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.icons_item, null);
        }
        try {
            Log.v("item", "------"+getItem(position));
//根据文件名获取资源文件夹中的图片资源
            Field f= (Field)R.drawable.class.getDeclaredField(getItem(position));
            int i=f.getInt(R.drawable.class);
            ImageView imageview= (ImageView)view.findViewById(R.id.icon);
            imageview.setImageResource(i);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return view;
    }
}

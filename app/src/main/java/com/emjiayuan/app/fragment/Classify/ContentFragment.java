package com.emjiayuan.app.fragment.Classify;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.adapter.CategoryContentAdapter;
import com.emjiayuan.app.adapter.GoodsAdapter;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.entity.XsgItem;
import com.emjiayuan.app.widget.MyGridView;
import com.emjiayuan.app.widget.MyListView;

import java.util.ArrayList;


@SuppressLint("ValidFragment")
public class ContentFragment extends Fragment {
    private String context;
    private CategoryContentAdapter adapter;
    private MyListView gv_content;
    private TextView mTextView;
    private ArrayList<XsgItem> list=new ArrayList<>();

    public ContentFragment(String context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content,container,false);
        init(view);

//        adapter=new CategoryContentAdapter(getActivity(), ClassifyFragment.list);
//        gv_content.setAdapter(adapter);
        return view;
    }
    public void init(View view){
        gv_content=view.findViewById(R.id.gv_content);
    }
}
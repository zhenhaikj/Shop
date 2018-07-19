/*
package com.xbm.myapplication.fragment.Classify;

import android.annotation.SuppressLint;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xbm.myapplication.R;
import com.xbm.myapplication.adapter.CategoryAdapter;
import com.xbm.myapplication.entity.Category;

import java.util.ArrayList;
import java.util.List;


public class ClassifyFragment extends Fragment {
    private boolean flag;
    private ListView lv;
    private LinearLayout ll_search;
    private CategoryAdapter adapter;
    public static ArrayList<Category> list=new ArrayList();

    public ClassifyFragment(Boolean flag) {
        this.flag = flag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classify,container,false);
        init(view);
        Category c1=new Category("正宗汤料",true);
        Category c2=new Category("辣椒粉",false);
        Category c3=new Category("番茄酱",false);
        Category c4=new Category("味精鸡精",false);
        Category c5=new Category("精品粮油",false);
        Category c6=new Category("炒料煮料",false);
        Category c7=new Category("火锅底料",false);
        Category c8=new Category("精品餐具",false);
        list.add(c1);
        list.add(c2);
        list.add(c3);
        list.add(c4);
        list.add(c5);
        list.add(c6);
        list.add(c7);
        list.add(c8);
        adapter=new CategoryAdapter(getActivity(),list);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Category info = list.get(position);
                for (int i = 0; i < list.size(); i++) {
                    if(list.get(i).getName().equals(info.getName())){
                        list.get(i).setClick(true);
                    }else {
                        list.get(i).setClick(false);
                    }
                }

                adapter.notifyDataSetChanged();


                //
                //创建MyFragment对象
                ContentFragment myFragment = new ContentFragment(info.getName());
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container2, myFragment);
                fragmentTransaction.commit();
            }
        });
        //创建MyFragment对象
        ContentFragment myFragment = new ContentFragment("正宗汤料");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container2, myFragment);
        fragmentTransaction.commit();
        return view;
    }
    public void init(View view){
        lv=view.findViewById(R.id.listview);
        ll_search=view.findViewById(R.id.ll_search);
        if (flag){
            ll_search.setVisibility(View.GONE);
        }else{
            ll_search.setVisibility(View.VISIBLE);
        }
    }
}*/

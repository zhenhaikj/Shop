package com.emjiayuan.app.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.adapter.GoodsAdapter;
import com.emjiayuan.app.banner.GlideImageLoader;
import com.emjiayuan.app.entity.Global;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class TwzqActivity extends BaseActivity {
    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.gv)
    GridView gv;

    private GoodsAdapter adapter;


    @Override
    protected int setLayoutId() {
        return R.layout.activity_new_arrivals;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        title.setText("调味专区");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());

        banner.setImages(Global.images);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
        /*XsgItem item1 = new XsgItem("清真伊穆家园牛肉面标准化园牛肉面标准化汤料2.5斤试用装【中端型】", "¥60.00", "$70.00", R.drawable.all);
        for (int i=0;i<20;i++){
            listX.add(item1);
        }*/
        adapter = new GoodsAdapter(this, Global.list);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(TwzqActivity.this, GoodsDetailActivity.class);
                intent.putExtra("productid", Global.list.get(i).getId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void setListener() {

    }
}

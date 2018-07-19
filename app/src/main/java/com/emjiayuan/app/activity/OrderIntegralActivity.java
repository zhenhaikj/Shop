package com.emjiayuan.app.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.adapter.OrderAdapter;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.entity.LoginResult;
import com.emjiayuan.app.entity.Order;
import com.emjiayuan.app.entity.Product;
import com.emjiayuan.app.event.UpdateEvent;
import com.emjiayuan.app.fragment.ViewPagerFragment;
import com.emjiayuan.app.fragment.ViewPagerFragment2;
import com.emjiayuan.app.widget.MyListView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class OrderIntegralActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.save)
    TextView save;

    @BindView(R.id.line_top)
    View lineTop;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.vp_tab_pager)
    ViewPager vpTabPager;
    private OrderAdapter adapter;
    private ArrayList<Order> orderList;
    private String order_type = "";
    List<ViewPagerFragment2> list=new ArrayList<>();
    private int curTab=0;
    private int type=0;//0普通1积分2充值


    @Override
    protected int setLayoutId() {
        return R.layout.activity_order_nomal2;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        type=getIntent().getIntExtra("type",0);
        switch (type){
            case 0:
                title.setText("普通订单");
                break;
            case 1:
                title.setText("积分礼品");
                break;
            case 2:
                title.setText("充值礼品");
                break;
        }
        Bundle args = new Bundle();
        for (int i = 0; i < 4; i++) {
            ViewPagerFragment2 fragment = new ViewPagerFragment2(curTab);
            fragment.setTabPos(i,type);
            list.add(fragment);
        }

        final MyPagerAdapter adapter=new MyPagerAdapter(getSupportFragmentManager());
        vpTabPager.setAdapter(adapter);
        vpTabPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position){
                    case 0:
                        order_type="";
                        break;
//                    case 1:
//                        order_type="1";
//                        break;
                    case 1:
                        order_type="2";
                        break;
                    case 2:
                        order_type="3";
                        break;
                    case 3:
                        order_type="4";
                        break;
                }
//                list.get(position).getCoupon(order_type,1);
                list.get(position).sendMessage();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // 把TabLayout和ViewPager关联起来
        tabLayout.setupWithViewPager(vpTabPager);
        vpTabPager.setCurrentItem(curTab);
    }

    @Override
    protected void setListener() {
        back.setOnClickListener(this);

    }





    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    /**
     * ViewPager适配器
     */
    public class MyPagerAdapter extends FragmentPagerAdapter {

        // 定义Tab标题
        private String[] tabTitles = new String[]{
                "全部","待发货","待收货","待评价"
        };
        private String order_type="";

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // 设置Page对应的Tab标题
            return tabTitles[position];
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
        }
    }
}

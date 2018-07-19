package com.emjiayuan.app.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CouponActivity2 extends BaseActivity implements View.OnClickListener {

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

    List<CouponFragment> list=new ArrayList<>();
    private int curTab=0;


    @Override
    protected int setLayoutId() {
        return R.layout.activity_order_nomal2;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        title.setText("优惠券");
        Bundle args = new Bundle();
        for (int i = 0; i < 5; i++) {
            CouponFragment fragment = new CouponFragment(curTab);
                fragment.setTabPos(i);
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
        if (!MyUtils.isFastClick()){
            return;
        }
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
                "未使用","使用记录","已过期"
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

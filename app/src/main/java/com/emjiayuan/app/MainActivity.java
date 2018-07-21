package com.emjiayuan.app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;


import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.activity.LoginActivity;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.fragment.Classify.ClassifyFragment2;
import com.emjiayuan.app.fragment.Community.CommunityFragment;
import com.emjiayuan.app.fragment.Community.CommunityFragment2;
import com.emjiayuan.app.fragment.HomeFragment;
import com.emjiayuan.app.fragment.Personal.PersonalFragment;
import com.emjiayuan.app.fragment.ShoppingCar.ShoppingCarFragment;
import com.emjiayuan.app.widget.CustomViewPager;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by geyifeng on 2017/5/8.
 */

public class MainActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.viewPager)
    CustomViewPager viewPager;
    @BindView(R.id.ll_home)
    LinearLayout ll_home;
    @BindView(R.id.ll_category)
    LinearLayout ll_category;
    @BindView(R.id.ll_community)
    LinearLayout ll_community;
    @BindView(R.id.ll_car)
    LinearLayout ll_car;
    @BindView(R.id.ll_mine)
    LinearLayout ll_mine;
    private ArrayList<Fragment> mFragments;
    private HomeFragment homeFragment;
    private ClassifyFragment2 classifyFragment2;
    private CommunityFragment2 communityFragment;
    public static ShoppingCarFragment shoppingCarFragment;
    private PersonalFragment personalFragment;
//    private PersonalFragment personalFragment;
//    private PersonalFragment personalFragment;



    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        mFragments = new ArrayList<>();
        homeFragment = new HomeFragment();
        classifyFragment2 = new ClassifyFragment2(true);
        communityFragment = new CommunityFragment2();
        shoppingCarFragment = new ShoppingCarFragment();
        personalFragment = new PersonalFragment();
        mFragments.add(homeFragment);
        mFragments.add(classifyFragment2);
        mFragments.add(communityFragment);
        mFragments.add(shoppingCarFragment);
        mFragments.add(personalFragment);
    }

    @Override
    protected void initView() {
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(5);
        viewPager.setScroll(false);
        ll_home.setSelected(true);
    }

    @Override
    protected void setListener() {
        ll_home.setOnClickListener(this);
        ll_category.setOnClickListener(this);
        ll_community.setOnClickListener(this);
        ll_car.setOnClickListener(this);
        ll_mine.setOnClickListener(this);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        if (!MyUtils.isFastClick()){
            return;
        }
        Intent intent=null;
        switch (v.getId()) {
            case R.id.ll_home:
                viewPager.setCurrentItem(0);
                tabSelected(ll_home);
//                homeFragment.request();
                break;
            case R.id.ll_category:
                viewPager.setCurrentItem(1);
                tabSelected(ll_category);
//                classifyFragment2.reqCategory();
                break;
            case R.id.ll_community:
                if (Global.loginResult == null) {
                    intent=new Intent(mActivity, LoginActivity.class);
//                    intent.putExtra("type",0);
                    startActivityForResult(intent,0);
                    return;
                }else{
                    viewPager.setCurrentItem(2);
                    tabSelected(ll_community);
                }

                break;
            case R.id.ll_car:
                if (Global.loginResult == null) {
                    intent=new Intent(mActivity, LoginActivity.class);
//                    intent.putExtra("type",1);
                    startActivityForResult(intent,1);
                    return;
                }else{
//                    shoppingCarFragment.reqCarList();
                    viewPager.setCurrentItem(3);
                    tabSelected(ll_car);
                }
                break;
            case R.id.ll_mine:
                if (Global.loginResult == null) {
                    intent=new Intent(mActivity, LoginActivity.class);
//                    intent.putExtra("type",2);
                    startActivityForResult(intent,2);
                    return;
                }else{
                    viewPager.setCurrentItem(4);
                    tabSelected(ll_mine);
//                    personalFragment.user();
                }

                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                tabSelected(ll_home);
                break;
            case 1:
                tabSelected(ll_category);
                break;
            case 2:
                tabSelected(ll_community);
                break;
            case 3:
                tabSelected(ll_car);
                break;
            case 4:
                tabSelected(ll_mine);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void tabSelected(LinearLayout linearLayout) {
        ll_home.setSelected(false);
        ll_category.setSelected(false);
        ll_community.setSelected(false);
        ll_car.setSelected(false);
        ll_mine.setSelected(false);
        linearLayout.setSelected(true);
    }

    private class MyAdapter extends FragmentPagerAdapter {
        MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case 0:
                    viewPager.setCurrentItem(2);
                    tabSelected(ll_community);
                    break;
                case 1:
                    shoppingCarFragment.reqCarList();
                    viewPager.setCurrentItem(3);
                    tabSelected(ll_car);
                    break;
                case 2:
                    viewPager.setCurrentItem(4);
                    tabSelected(ll_mine);
                    personalFragment.user();
                    break;
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (Global.loginResult == null) {
            if (viewPager.getCurrentItem()==4){
                viewPager.setCurrentItem(0);
                tabSelected(ll_home);
                homeFragment.request();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

}

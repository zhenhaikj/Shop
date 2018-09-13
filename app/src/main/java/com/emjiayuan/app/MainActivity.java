package com.emjiayuan.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.Utils.SpUtils;
import com.emjiayuan.app.activity.LoginActivity;
import com.emjiayuan.app.entity.APPTheme;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.fragment.Classify.ClassifyFragment2;
import com.emjiayuan.app.fragment.Community.CommunityFragment;
import com.emjiayuan.app.fragment.HomeFragment;
import com.emjiayuan.app.fragment.Personal.PersonalFragment;
import com.emjiayuan.app.fragment.ShoppingCar.ShoppingCarFragment;
import com.emjiayuan.app.widget.CustomViewPager;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import cn.jzvd.JZVideoPlayer;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

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
    @BindView(R.id.img_home)
    ImageView imgHome;
    @BindView(R.id.tv_home)
    TextView tvHome;
    @BindView(R.id.img_cate)
    ImageView imgCate;
    @BindView(R.id.tv_cate)
    TextView tvCate;
    @BindView(R.id.img_sq)
    ImageView imgSq;
    @BindView(R.id.tv_sq)
    TextView tvSq;
    @BindView(R.id.img_cart)
    ImageView imgCart;
    @BindView(R.id.tv_cart)
    TextView tvCart;
    @BindView(R.id.img_my)
    ImageView imgMy;
    @BindView(R.id.tv_my)
    TextView tvMy;
    @BindView(R.id.tab_menu)
    LinearLayout tabMenu;
    private ArrayList<Fragment> mFragments;
    private HomeFragment homeFragment;
    private ClassifyFragment2 classifyFragment2;
    private CommunityFragment communityFragment;
    public static ShoppingCarFragment shoppingCarFragment;
    private PersonalFragment personalFragment;


    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        setSwipeBackEnable(false);
        mFragments = new ArrayList<>();
        homeFragment = new HomeFragment();
        classifyFragment2 = new ClassifyFragment2();
        communityFragment = new CommunityFragment();
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
        getAppTheme();
    }


    public void getAppTheme() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体

        Log.d("------参数------", formBody.build().toString());
//new call
        Call call = MyOkHttp.GetCall("system.getAppTheme", formBody);
//请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("------------", e.toString());
//                        myHandler.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                MyUtils.e("------获取主题结果------", result);
                Message message = new Message();
                message.what = 0;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String result = bundle.getString("result");
            switch (msg.what) {
                case 0:
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            Global.appTheme = gson.fromJson(data, APPTheme.class);
                            SpUtils.putObject(mActivity, "appTheme", Global.appTheme);
                            new Thread(new Runnable(){
                                @Override
                                public void run() {
                                    Drawable dw_home = MyUtils.loadImageFromNetwork(Global.appTheme.getHome_bottom_menu_home().getImg());
                                    Drawable dw_home_active = MyUtils.loadImageFromNetwork(Global.appTheme.getHome_bottom_menu_home().getImg_active());
                                    Drawable dw_cate = MyUtils.loadImageFromNetwork(Global.appTheme.getHome_bottom_menu_cate().getImg());
                                    Drawable dw_cate_active = MyUtils.loadImageFromNetwork(Global.appTheme.getHome_bottom_menu_cate().getImg_active());
                                    Drawable dw_sq = MyUtils.loadImageFromNetwork(Global.appTheme.getHome_bottom_menu_sq().getImg());
                                    Drawable dw_sq_active = MyUtils.loadImageFromNetwork(Global.appTheme.getHome_bottom_menu_sq().getImg_active());
                                    Drawable dw_cart = MyUtils.loadImageFromNetwork(Global.appTheme.getHome_bottom_menu_cart().getImg());
                                    Drawable dw_cart_active = MyUtils.loadImageFromNetwork(Global.appTheme.getHome_bottom_menu_cart().getImg_active());
                                    Drawable dw_my = MyUtils.loadImageFromNetwork(Global.appTheme.getHome_bottom_menu_my().getImg());
                                    Drawable dw_my_active = MyUtils.loadImageFromNetwork(Global.appTheme.getHome_bottom_menu_my().getImg_active());
                                    setSelector(imgHome,MyUtils.newSelector(mActivity,dw_home,dw_home_active));
                                    setSelector(imgCate,MyUtils.newSelector(mActivity,dw_cate,dw_cate_active));
                                    setSelector(imgSq,MyUtils.newSelector(mActivity,dw_sq,dw_sq_active));
                                    setSelector(imgCart,MyUtils.newSelector(mActivity,dw_cart,dw_cart_active));
                                    setSelector(imgMy,MyUtils.newSelector(mActivity,dw_my,dw_my_active));
                                }

                            }).start();
                            ColorStateList csl=new ColorStateList(new int[][]{new int[] { android.R.attr.state_selected },new int[]{}},new int[]{Color.parseColor(Global.appTheme.getHome_bottom_menu_sq().getColor_active()),Color.parseColor(Global.appTheme.getHome_bottom_menu_sq().getColor())});
                            tvHome.setTextColor(csl);
                            tvCate.setTextColor(csl);
                            tvSq.setTextColor(csl);
                            tvCart.setTextColor(csl);
                            tvMy.setTextColor(csl);
//                            imgHome.setBackground(MyUtils.newSelector(mActivity,R.drawable.home,R.drawable.homex));
                        } else {
//                            MyUtils.showToast(mActivity, message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    public void setSelector(final ImageView iv, final Drawable dw){
        // post() 特别关键，就是到UI主线程去更新图片
        iv.post(new Runnable(){
            @SuppressLint("NewApi")
            @Override
            public void run() {
                // TODO Auto-generated method stub
                iv.setBackground(dw);
            }}) ;
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
//        if (!MyUtils.isFastClick()) {
//            return;
//        }
        Intent intent = null;
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
                    intent = new Intent(mActivity, LoginActivity.class);
//                    intent.putExtra("type",0);
                    startActivityForResult(intent, 0);
                    return;
                } else {
                    viewPager.setCurrentItem(2);
                    tabSelected(ll_community);
                }

                break;
            case R.id.ll_car:
                if (Global.loginResult == null) {
                    intent = new Intent(mActivity, LoginActivity.class);
//                    intent.putExtra("type",1);
                    startActivityForResult(intent, 1);
                    return;
                } else {
//                    shoppingCarFragment.reqCarList();
                    viewPager.setCurrentItem(3);
                    tabSelected(ll_car);
                }
                break;
            case R.id.ll_mine:
                if (Global.loginResult == null) {
                    intent = new Intent(mActivity, LoginActivity.class);
//                    intent.putExtra("type",2);
                    startActivityForResult(intent, 2);
                    return;
                } else {
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
        JZVideoPlayer.releaseAllVideos();
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
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
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
            if (viewPager.getCurrentItem() == 4) {
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

//    @Override
//    public void onBackPressed() {
//        //按返回键返回桌面
//        moveTaskToBack(true);
//    }
    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }
}

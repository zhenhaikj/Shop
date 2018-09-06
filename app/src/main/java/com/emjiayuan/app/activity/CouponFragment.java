package com.emjiayuan.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.lwkandroid.stateframelayout.StateFrameLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.adapter.CouponAdapter;
import com.emjiayuan.app.entity.Coupon;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.event.UpdateEvent;
import com.emjiayuan.app.fragment.BaseLazyFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

/**
 * ViewPager页面
 */
public  class CouponFragment extends BaseLazyFragment {
    @BindView(R.id.lv_goods)
    ListView lvGoods;
    @BindView(R.id.stateLayout)
    StateFrameLayout stateLayout;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private String order_type;
    private int pageindex=1;
    private boolean flag=true;
    private boolean IS_LOADED = false;
    private int mSerial=0;
    private int mTabPos=0;
    private int type=0;
    private boolean isFirst = true;
    private CouponAdapter adapter;
    private ArrayList<Coupon> couponList=new ArrayList<>();//all
    private ArrayList<Coupon> couponList1=new ArrayList<>();//未使用
    private ArrayList<Coupon> couponList2=new ArrayList<>();//已使用
    private ArrayList<Coupon> couponList3=new ArrayList<>();//已过期

    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            Log.e("tag", "IS_LOADED="+IS_LOADED);
            switch (mTabPos){
                case 0:
                    type=0;
                    if (adapter!=null){
                        adapter.setGrouplists(couponList1);
                    }

                    break;
                case 1:
                    type=1;
                    if (adapter!=null){
                        adapter.setGrouplists(couponList2);
                    }
                    break;
                case 2:
                    type=2;
                    if (adapter!=null){
                        adapter.setGrouplists(couponList3);
                    }
                    break;
            }
            if(!IS_LOADED){
                IS_LOADED = true;
                //这里执行加载数据的操作
                getCoupon();

//                Log.e("tag", "我是page"+(mTabPos+1));
            }
            return;
        };
    };


    public CouponFragment(int serial){
        mSerial = serial;
    }

    public void sendMessage(){
        Message message = handler.obtainMessage();
        message.sendToTarget();
    }

    public void setTabPos(int mTabPos) {
        this.mTabPos = mTabPos;
    }

    /**
     * 初始化沉浸式
     */
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarDarkFont(true, 0.2f); //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
        mImmersionBar.statusBarColor(R.color.white);
        mImmersionBar.fitsSystemWindows(true);
        mImmersionBar.keyboardEnable(true).navigationBarWithKitkatEnable(false).init();
    }

    @Override
    protected void initData() {
        super.initData();
        //设置页和当前页一致时加载，防止预加载
        if (isFirst && mTabPos==mSerial) {
            isFirst = false;
            sendMessage();
        }
        stateLayout.setOnNetErrorRetryListener(new StateFrameLayout.OnNetErrorRetryListener() {
            @Override
            public void onNetErrorRetry() {
                getCoupon();
            }
        });
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setEnableRefresh(false);
//        refreshLayout.setEnableHeaderTranslationContent(false);
//        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(RefreshLayout refreshlayout) {
//                flag=false;
//                couponList.clear();
//                pageindex=1;
//                refreshLayout.finishLoadMore(false);
//                getCoupon();
//            }
//        });
//        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(RefreshLayout refreshlayout) {
//                flag=false;
//                pageindex++;
//                getCoupon();
//            }
//        });

    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_tab_coupon;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(UpdateEvent event) {

    }
    public void getCoupon() {
        if (!MyUtils.checkNetwork(getActivity())) {
            stateLayout.changeState(StateFrameLayout.NET_ERROR);
            return;
        }
        if (flag){
            stateLayout.changeState(StateFrameLayout.LOADING);
        }
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        if (Global.loginResult == null) {
            startActivityForResult(new Intent(getActivity(), LoginActivity.class), 100);
            return;
        }
        formBody.add("userid", Global.loginResult.getId());

        Log.d("------参数------", formBody.build().toString());
//new call
        Call call = MyOkHttp.GetCall("userCoupon.getCouponList", formBody);
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
                MyUtils.e("----",order_type+"|"+pageindex);
                MyUtils.e("------获取优惠券列表结果------", result);
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
//                        couponList = new ArrayList<>();
                        if ("200".equals(code)) {
                            JSONArray jsonArray = new JSONArray(data);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                couponList.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), Coupon.class));
                            }
                            if (couponList.size() > 0) {
                                for (int i = 0; i < couponList.size(); i++) {
                                    if ("1".equals(couponList.get(i).getUsestatus())){
                                        couponList2.add(couponList.get(i));
                                    }else if("0".equals(couponList.get(i).getUsetime())){
                                        couponList3.add(couponList.get(i));
                                    }else{
                                        couponList1.add(couponList.get(i));
                                    }
                                }

                                if (adapter==null){
                                    switch (type){
                                        case 0:
                                            if (couponList1.size()>0){
                                                stateLayout.changeState(StateFrameLayout.SUCCESS);
                                            }else{
                                                stateLayout.changeState(StateFrameLayout.EMPTY);
                                            }
                                            adapter = new CouponAdapter(getActivity(), couponList1);
                                            break;
                                        case 1:
                                            if (couponList2.size()>0){
                                                stateLayout.changeState(StateFrameLayout.SUCCESS);
                                            }else{
                                                stateLayout.changeState(StateFrameLayout.EMPTY);
                                            }
                                            adapter = new CouponAdapter(getActivity(), couponList2);
                                            break;
                                        case 2:
                                            if (couponList3.size()>0){
                                                stateLayout.changeState(StateFrameLayout.SUCCESS);
                                            }else{
                                                stateLayout.changeState(StateFrameLayout.EMPTY);
                                            }
                                            adapter = new CouponAdapter(getActivity(), couponList3);
                                            break;
                                    }
                                    lvGoods.setAdapter(adapter);
                                }else{
                                    adapter.notifyDataSetChanged();
                                }

                            } else {
                                stateLayout.changeState(StateFrameLayout.EMPTY);
                            }
                        } else {
                            if (couponList.size() > 0) {
                                for (int i = 0; i < couponList.size(); i++) {
                                    if ("1".equals(couponList.get(i).getUsestatus())){
                                        couponList2.add(couponList.get(i));
                                    }else if("0".equals(couponList.get(i).getUsetime())){
                                        couponList3.add(couponList.get(i));
                                    }else{
                                        couponList1.add(couponList.get(i));
                                    }
                                }
                                stateLayout.changeState(StateFrameLayout.SUCCESS);
                                if (adapter==null){
                                    switch (type){
                                        case 0:
                                            if (couponList1.size()>0){
                                                stateLayout.changeState(StateFrameLayout.SUCCESS);
                                            }else{
                                                stateLayout.changeState(StateFrameLayout.EMPTY);
                                            }
                                            adapter = new CouponAdapter(getActivity(), couponList1);
                                            break;
                                        case 1:
                                            if (couponList2.size()>0){
                                                stateLayout.changeState(StateFrameLayout.SUCCESS);
                                            }else{
                                                stateLayout.changeState(StateFrameLayout.EMPTY);
                                            }
                                            adapter = new CouponAdapter(getActivity(), couponList2);
                                            break;
                                        case 2:
                                            if (couponList3.size()>0){
                                                stateLayout.changeState(StateFrameLayout.SUCCESS);
                                            }else{
                                                stateLayout.changeState(StateFrameLayout.EMPTY);
                                            }
                                            adapter = new CouponAdapter(getActivity(), couponList3);
                                            break;
                                    }
                                    lvGoods.setAdapter(adapter);
                                }else{
                                    adapter.notifyDataSetChanged();
                                }

                            } else {
                                stateLayout.changeState(StateFrameLayout.EMPTY);
                            }
                            stateLayout.changeState(StateFrameLayout.EMPTY);
                            refreshLayout.finishLoadMore(true);
                            MyUtils.showToast(getActivity(), message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
//                    MyUtils.showToast(LoginActivity.this,result);
                    break;
            }
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadMore();
        }
    };


}
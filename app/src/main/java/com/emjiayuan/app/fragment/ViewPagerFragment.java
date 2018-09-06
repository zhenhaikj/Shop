package com.emjiayuan.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.emjiayuan.app.activity.SoupOrderDetailActivity;
import com.emjiayuan.app.adapter.SoupOrderAdapter;
import com.emjiayuan.app.entity.SoupOrder;
import com.emjiayuan.app.event.WXpaySuccessEvent;
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
import com.emjiayuan.app.activity.LoginActivity;
import com.emjiayuan.app.activity.OrderDetailActivity;
import com.emjiayuan.app.adapter.OrderAdapter;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.entity.Order;
import com.emjiayuan.app.event.UpdateEvent;
import com.emjiayuan.app.widget.MyListView;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseResp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

/**
 * ViewPager页面
 */
public class ViewPagerFragment extends BaseLazyFragment {
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
    private static int mSerial=0;
    private int mTabPos=0;
    private int type=0;
    private boolean isFirst = true;

    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            Log.e("tag", "IS_LOADED="+IS_LOADED);
            if(!IS_LOADED){
                IS_LOADED = true;
                //这里执行加载数据的操作
                switch (mTabPos){
                    case 0:
                        order_type="";
                        break;
                    case 1:
                        order_type="1";
                        break;
                    case 2:
                        order_type="2";
                        break;
                    case 3:
                        order_type="3";
                        break;
                    case 4:
                        order_type="4";
                        break;

                }
                if (type==3){
                    getSoupOrder();
                }else{
                    getOrder();
                }

//                Log.e("tag", "我是page"+(mTabPos+1));
            }
            return;
        };
    };


    public ViewPagerFragment(int serial){
        mSerial = serial;
    }

    public void sendMessage(){
        Message message = handler.obtainMessage();
        message.sendToTarget();
    }

    public void setTabPos(int mTabPos,int type) {
        this.mTabPos = mTabPos;
        this.type=type;
    }

    /**
     * 取得Fragment实例
     */
//    public static ViewPagerFragment newInstance(Bundle args) {
//        ViewPagerFragment fragment = new ViewPagerFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }

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
                if (type==3){
                    getSoupOrder();
                }else{
                    getOrder();
                }
            }
        });
//        refreshLayout.setEnableAutoLoadMore(true);
        refreshLayout.setEnableHeaderTranslationContent(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                flag=false;
                orderList.clear();
                soupOrderList.clear();
                pageindex=1;
                refreshLayout.finishLoadMore(false);
                if (type==3){
                    getSoupOrder();
                }else{
                    getOrder();
                }
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                flag=false;
                pageindex++;
                if (type==3){
                    getSoupOrder();
                }else{
                    getOrder();
                }
            }
        });
        lvGoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!MyUtils.isFastClick()){
                    return;
                }
                if (type==3){
                    Intent intent=new Intent(mActivity, SoupOrderDetailActivity.class);
                    intent.putExtra("orderid",soupOrderList.get(i).getId());
                    mActivity.startActivity(intent);
                }else{
//                    if (orderList.get(i).getAddress()!=null){
                        Intent intent=new Intent(mActivity, OrderDetailActivity.class);
                        intent.putExtra("orderid",orderList.get(i).getId());
                        mActivity.startActivity(intent);
//                    }
                }
            }
        });
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_tab;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(UpdateEvent event) {
        orderList.clear();
        soupOrderList.clear();
        IS_LOADED=false;
        sendMessage();
//        if (type==3){
//            getSoupOrder();
//        }else{
//            getOrder();
//        }
    }
    public void getOrder() {
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
        formBody.add("status", order_type);
        formBody.add("compute", "");
        formBody.add("isProduct", "1");
        formBody.add("type", Integer.toString(type));
//        if ("4".equals(order_type)){
//            formBody.add("comment", "1");
//        }
        formBody.add("pageindex", Integer.toString(pageindex));
        formBody.add("pagesize", "40");
        Log.d("------参数------", formBody.build().toString());
//new call
        Call call = MyOkHttp.GetCall("order.getOrderList", formBody);
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
                MyUtils.e("----",order_type+"|"+pageindex+"|"+type);
                MyUtils.e("------获取订单列表结果------", result);
                Message message = new Message();
                message.what = 0;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    public void getSoupOrder() {
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
//        formBody.add("status", order_type);
        formBody.add("type", order_type);
        formBody.add("compute", "");
        formBody.add("isProduct", "1");
//        formBody.add("type", Integer.toString(type));
        if ("4".equals(order_type)){
            formBody.add("comment", "1");
        }
        formBody.add("pageindex", Integer.toString(pageindex));
        formBody.add("pagesize", "40");
        Log.d("------参数------", formBody.build().toString());
//new call
        Call call = MyOkHttp.GetCall("soupOrder.getSoupOrderList", formBody);
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
                MyUtils.e("----",order_type+"|"+pageindex+"|"+type);
                MyUtils.e("------获取汤料订单列表结果------", result);
                Message message = new Message();
                message.what = 1;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    private OrderAdapter adapter;
    private SoupOrderAdapter soupOrderAdapter;
    private ArrayList<Order> orderList=new ArrayList<>();
    private ArrayList<SoupOrder> soupOrderList=new ArrayList<>();
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
                            JSONArray jsonArray = new JSONArray(data);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                orderList.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), Order.class));
                            }
                            if (orderList.size() > 0) {
                                stateLayout.changeState(StateFrameLayout.SUCCESS);
                                if (adapter==null){
                                    adapter = new OrderAdapter(getActivity(), orderList, type);
                                    lvGoods.setAdapter(adapter);
                                }else{
                                    adapter.notifyDataSetChanged();
                                }

                            } else {
                                stateLayout.changeState(StateFrameLayout.EMPTY);
                            }
                        } else  {
                            if (orderList.size() > 0) {
                                stateLayout.changeState(StateFrameLayout.SUCCESS);
                                if (adapter==null){
                                adapter = new OrderAdapter(getActivity(), orderList, type);
                                lvGoods.setAdapter(adapter);
                                }else{
                                    adapter.notifyDataSetChanged();
                                }

                            } else {
                                stateLayout.changeState(StateFrameLayout.EMPTY);
                            }
                            refreshLayout.finishLoadMore(true);
                            if (pageindex!=1){
                                MyUtils.showToast(getActivity(), "已全部加载");
                            }else{
//                                MyUtils.showToast(getActivity(), message);
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            JSONArray jsonArray = new JSONArray(data);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                soupOrderList.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), SoupOrder.class));
                            }
                            if (soupOrderList.size() > 0) {
                                stateLayout.changeState(StateFrameLayout.SUCCESS);
                                if (soupOrderAdapter==null){
                                    soupOrderAdapter = new SoupOrderAdapter(getActivity(), soupOrderList);
                                    lvGoods.setAdapter(soupOrderAdapter);
                                }else{
                                    soupOrderAdapter.notifyDataSetChanged();
                                }

                            } else {
                                stateLayout.changeState(StateFrameLayout.EMPTY);
                            }
                        } else  {
                            if (soupOrderList.size() > 0) {
                                stateLayout.changeState(StateFrameLayout.SUCCESS);
                                if (soupOrderAdapter==null){
                                    soupOrderAdapter = new SoupOrderAdapter(getActivity(), soupOrderList);
                                    lvGoods.setAdapter(soupOrderAdapter);
                                }else{
                                    soupOrderAdapter.notifyDataSetChanged();
                                }

                            } else {
                                stateLayout.changeState(StateFrameLayout.EMPTY);
                            }
                            refreshLayout.finishLoadMore(true);
                            if (pageindex!=1){
                                MyUtils.showToast(getActivity(), "已全部加载");
                            }else{
//                                MyUtils.showToast(getActivity(), message);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadMore();
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(WXpaySuccessEvent event) {
        BaseResp resp=event.getResp();
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {   // 0 成功  展示成功页面
                EventBus.getDefault().post(new UpdateEvent(""));
            } else if (resp.errCode == -1) {  // -1 错误  可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
                Toast.makeText(mActivity, "支付出错", Toast.LENGTH_SHORT)
                        .show();
//                mPopupWindow.dismiss();
            } else if (resp.errCode == -2) {  // -2 用户取消    无需处理。发生场景：用户不支付了，点击取消，返回APP。
                Toast.makeText(mActivity, "取消支付", Toast.LENGTH_SHORT)
                        .show();
//                mPopupWindow.dismiss();
            }
        }
    }
}

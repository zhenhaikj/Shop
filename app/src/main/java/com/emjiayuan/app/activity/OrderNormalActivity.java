package com.emjiayuan.app.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lwkandroid.stateframelayout.StateFrameLayout;
import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.adapter.OrderAdapter;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.entity.Order;
import com.emjiayuan.app.widget.MyListView;

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

public class OrderNormalActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.all_tv)
    TextView all_tv;
    @BindView(R.id.all_ll)
    LinearLayout all_ll;
    @BindView(R.id.dfk_tv)
    TextView dfk_tv;
    @BindView(R.id.dfk_ll)
    LinearLayout dfk_ll;
    @BindView(R.id.dfh_tv)
    TextView dfh_tv;
    @BindView(R.id.dfh_ll)
    LinearLayout dfh_ll;
    @BindView(R.id.dsh_tv)
    TextView dsh_tv;
    @BindView(R.id.dsh_ll)
    LinearLayout dsh_ll;
    @BindView(R.id.dpj_tv)
    TextView dpj_tv;
    @BindView(R.id.dpj_ll)
    LinearLayout dpj_ll;
    @BindView(R.id.tab_rl)
    LinearLayout tab_rl;
    @BindView(R.id.lv_goods)
    MyListView lv_goods;
    @BindView(R.id.line_top)
    View lineTop;
    @BindView(R.id.stateLayout)
    StateFrameLayout stateLayout;

    private OrderAdapter adapter;
    private ArrayList<Order> orderList;
    private int order_type = 0;
    private String type = "";


    @Override
    protected int setLayoutId() {
        return R.layout.activity_order_nomal;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        title.setText("普通订单");
        getOrder(type);
        lv_goods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!MyUtils.isFastClick()){
                    return;
                }
                Intent intent=new Intent(mActivity,OrderDetailActivity.class);
                intent.putExtra("orderid",orderList.get(i).getId());
                startActivity(intent);
            }
        });
        all_tv.setBackgroundResource(R.drawable.coupon_line);
        dfk_tv.setBackgroundColor(Color.parseColor("#ffffff"));
        dfh_tv.setBackgroundColor(Color.parseColor("#ffffff"));
        dsh_tv.setBackgroundColor(Color.parseColor("#ffffff"));
        dpj_tv.setBackgroundColor(Color.parseColor("#ffffff"));

        all_tv.setTextColor(Color.parseColor("#53D75D"));
        dfk_tv.setTextColor(Color.parseColor("#373737"));
        dfh_tv.setTextColor(Color.parseColor("#373737"));
        dsh_tv.setTextColor(Color.parseColor("#373737"));
        dpj_tv.setTextColor(Color.parseColor("#373737"));
    }

    @Override
    protected void setListener() {
        back.setOnClickListener(this);
        all_ll.setOnClickListener(this);
        dfk_ll.setOnClickListener(this);
        dfh_ll.setOnClickListener(this);
        dsh_ll.setOnClickListener(this);
        dpj_ll.setOnClickListener(this);
    }

    public void getOrder(String type) {
        if (!checkNetwork()){
            stateLayout.changeState(StateFrameLayout.NET_ERROR);
            return;
        }
        stateLayout.changeState(StateFrameLayout.LOADING);
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        if (Global.loginResult == null) {
            startActivityForResult(new Intent(mActivity, LoginActivity.class),100);
            return;
        }
        formBody.add("userid", Global.loginResult.getId());
        formBody.add("type", type);
        formBody.add("compute", "");
        formBody.add("isProduct", "1");
        formBody.add("pageindex", "1");
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
                        orderList = new ArrayList<>();
                        if ("200".equals(code)) {
                            JSONArray jsonArray = new JSONArray(data);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                orderList.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), Order.class));
                            }
                            if (orderList.size()>0){
                                stateLayout.changeState(StateFrameLayout.SUCCESS);
                                adapter = new OrderAdapter(mActivity, orderList, order_type);
                                lv_goods.setAdapter(adapter);
                            }else{
                                stateLayout.changeState(StateFrameLayout.EMPTY);
                            }
                        } else  {
                            stateLayout.changeState(StateFrameLayout.EMPTY);
                            MyUtils.showToast(OrderNormalActivity.this, message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
//                    MyUtils.showToast(LoginActivity.this,result);
                    break;
            }
        }
    };

    public void setSelect(TextView select, TextView unselect1, TextView unselect2, TextView unselect3, TextView unselect4) {
        select.setBackgroundResource(R.drawable.coupon_line);
        unselect1.setBackgroundColor(Color.parseColor("#ffffff"));
        unselect2.setBackgroundColor(Color.parseColor("#ffffff"));
        unselect3.setBackgroundColor(Color.parseColor("#ffffff"));
        unselect4.setBackgroundColor(Color.parseColor("#ffffff"));

        select.setTextColor(Color.parseColor("#53D75D"));
        unselect1.setTextColor(Color.parseColor("#373737"));
        unselect2.setTextColor(Color.parseColor("#373737"));
        unselect3.setTextColor(Color.parseColor("#373737"));
        unselect4.setTextColor(Color.parseColor("#373737"));
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
            case R.id.all_ll:
                order_type = 0;
                type = "";
                setSelect(all_tv, dfk_tv, dfh_tv, dsh_tv, dpj_tv);
                getOrder(type);
                break;
            case R.id.dfk_ll:
                order_type = 1;
                type = "1";
                setSelect(dfk_tv, all_tv, dfh_tv, dsh_tv, dpj_tv);
                getOrder(type);
                break;
            case R.id.dfh_ll:
                order_type = 2;
                type = "2";
                setSelect(dfh_tv, dfk_tv, all_tv, dsh_tv, dpj_tv);
                getOrder(type);
                break;
            case R.id.dsh_ll:
                order_type = 3;
                type = "3";
                setSelect(dsh_tv, dfk_tv, dfh_tv, all_tv, dpj_tv);
                getOrder(type);
                break;
            case R.id.dpj_ll:
                order_type = 4;
                type = "4";
                setSelect(dpj_tv, dfk_tv, dfh_tv, dsh_tv, all_tv);
                getOrder(type);
                break;
        }
    }
}

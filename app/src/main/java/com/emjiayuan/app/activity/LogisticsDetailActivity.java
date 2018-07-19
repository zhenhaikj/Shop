package com.emjiayuan.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.adapter.LogisticsAdapter;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.entity.LoginResult;
import com.emjiayuan.app.entity.Logistics;
import com.emjiayuan.app.entity.LogisticsBean;
import com.emjiayuan.app.entity.LogisticsJson;
import com.emjiayuan.app.entity.Order;
import com.emjiayuan.app.event.UpdateEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class LogisticsDetailActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.order_num)
    TextView order_num;
    @BindView(R.id.wuliu_num)
    TextView wuliu_num;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.rv_list)
    RecyclerView rv_list;

    private LogisticsAdapter adapter;
    private ArrayList<LogisticsBean> list=new ArrayList<>();
    private String postid="";
    private String postcom="";
    private String orderno="";
    private String date="";
    private String Address="";
    private Order order;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_wuliu_detail;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        title.setText("物流详细");
        Intent intent=getIntent();
        if (intent!=null){
            postid=intent.getStringExtra("postid");
            postcom=intent.getStringExtra("postcom");
            orderno=intent.getStringExtra("order_no");
            date=intent.getStringExtra("date");
            Address=intent.getStringExtra("address");
        }
//        order= (Order) intent.getSerializableExtra("logistics");
//        postid=order.getExpressno();
//        postcom=order.getExpresscom();
//        order_num.setText("订单号："+order.getOrder_no());
//        wuliu_num.setText("物流编号："+order.getExpressno());
//        address.setText("收货地址："+order.getAddress());
//        time.setText("下单时间："+order.getCreatedate());
        order_num.setText("订单号："+orderno);
        wuliu_num.setText("物流编号："+postid);
        address.setText("收货地址："+Address);
        time.setText("下单时间："+date);
        getlist();
        //构造与装配布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv_list.setLayoutManager(linearLayoutManager);

    }

    @Override
    protected void setListener() {
        back.setOnClickListener(this);
    }

    public void getlist() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体

        formBody.add("postid", postid);
        formBody.add("postcom", postcom);

        Log.d("------参数------", formBody.build().toString());
//new call
        Call call = MyOkHttp.GetCall("public.getExpress", formBody);
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
                MyUtils.e("------获取物流详情------", result);
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
                        Gson gson=new Gson();
                        if ("200".equals(code)) {
                            JSONArray jsonArray=new JSONArray(data);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                list.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), LogisticsBean.class));
                            }
                            adapter = new LogisticsAdapter(mActivity, list);
                            rv_list.setAdapter(adapter);
//                            MyUtils.showToast(LogisticsDetailActivity.this, message);
                        } else {
                            MyUtils.showToast(LogisticsDetailActivity.this, message);
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

}

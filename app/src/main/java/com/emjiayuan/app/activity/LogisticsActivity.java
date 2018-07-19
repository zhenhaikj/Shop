package com.emjiayuan.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.adapter.WuliuAdapter;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.entity.Order;
import com.google.gson.Gson;
import com.lwkandroid.stateframelayout.StateFrameLayout;

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

public class LogisticsActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.lv_list)
    ListView lv_list;
    @BindView(R.id.service_ll)
    LinearLayout serviceLl;
    @BindView(R.id.line_top)
    View lineTop;
    @BindView(R.id.stateLayout)
    StateFrameLayout stateLayout;

    private WuliuAdapter adapter;
    private ArrayList<Order> LogisticsList = new ArrayList<>();

    @Override
    protected int setLayoutId() {
        return R.layout.activity_wuliu;
    }

    @Override
    protected void initData() {
        title.setText("物流收货");
        getlist();

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setListener() {
        back.setOnClickListener(this);
    }

    public void getlist() {
        stateLayout.changeState(StateFrameLayout.LOADING);
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        if (Global.loginResult == null) {
            startActivity(new Intent(mActivity, LoginActivity.class));
            finish();
            return;
        }
        formBody.add("userid", Global.loginResult.getId());
        formBody.add("status", "3");
        formBody.add("pageindex", "1");
        formBody.add("pagesize", "100");

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
                MyUtils.e("------获取物流列表------", result);
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
                            JSONArray jsonArray = new JSONArray(data);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                LogisticsList.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), Order.class));
                            }
                            if (LogisticsList.size()>0){
                                stateLayout.changeState(StateFrameLayout.SUCCESS);
                            }else{
                                stateLayout.changeState(StateFrameLayout.EMPTY);
                            }
                            adapter = new WuliuAdapter(mActivity, LogisticsList);
                            lv_list.setAdapter(adapter);
//                            MyUtils.showToast(LogisticsActivity.this, result);
                        } else {
                            stateLayout.changeState(StateFrameLayout.EMPTY);
                            MyUtils.showToast(LogisticsActivity.this, message);
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
        if (!MyUtils.isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

}

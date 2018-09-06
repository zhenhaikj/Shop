package com.emjiayuan.app.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.adapter.MyMessageAdapter;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.entity.MyMessage;
import com.emjiayuan.app.event.IsReadEvent;
import com.emjiayuan.app.widget.MyListView;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;


public class MyMessageActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.service_ll)
    LinearLayout serviceLl;
    @BindView(R.id.line_top)
    View lineTop;
    @BindView(R.id.lv_post)
    MyListView lvMyMessage;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private int pageindex = 1;
    private ArrayList<MyMessage> list=new ArrayList<>();
    private MyMessageAdapter adapter;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_my_message;
    }

    @Override
    protected void initData() {
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadMore(false);
    }

    @Override
    protected void initView() {
        title.setText("我的消息");
        getWeiboMessageList();
    }

    @Override
    protected void setListener() {
        back.setOnClickListener(this);
    }

    /**
     * 我的消息
     */
    public void getWeiboMessageList() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("isread", "1");//传递键值对参数
        formBody.add("userid", Global.loginResult.getId());//传递键值对参数
//new call
        Call call = MyOkHttp.GetCall("weibo.getWeiboMessageList", formBody);
//请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("------------", e.toString());
//                myHandler.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                MyUtils.e("------我的消息------", result);
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

                case 0://我的消息
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            JSONArray jsonArray = new JSONArray(data);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                list.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), MyMessage.class));
                            }
                            adapter = new MyMessageAdapter(mActivity, list);
                            lvMyMessage.setAdapter(adapter);
                            lvMyMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent intent = new Intent(mActivity,PostDetailActivity.class);
                                    intent.putExtra("weiboid", list.get(i).getWid());
                                    startActivity(intent);
                                }
                            });
                        } else {
                            MyUtils.showToast(mActivity, message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

            }
        }
    };


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();

                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(new IsReadEvent(0));
    }
}

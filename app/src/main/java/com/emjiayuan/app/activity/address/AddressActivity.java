package com.emjiayuan.app.activity.address;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lwkandroid.stateframelayout.StateFrameLayout;
import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.activity.LoginActivity;
import com.emjiayuan.app.adapter.AddressAdapter;
import com.emjiayuan.app.entity.Address;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.event.AddressEvent;

import org.greenrobot.eventbus.EventBus;
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

public class AddressActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.lv_address)
    ListView lv_address;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.save)
    TextView add;
    @BindView(R.id.line_top)
    View lineTop;
    @BindView(R.id.stateLayout)
    StateFrameLayout stateLayout;
    private AddressAdapter adapter;
    private ArrayList<Address> list = new ArrayList<>();
    private boolean flag;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_address;
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        title.setText("地址管理");
        add.setVisibility(View.VISIBLE);
        add.setText("添加地址");
        Intent intent = getIntent();
        if (intent != null) {
            flag = intent.getBooleanExtra("flag", false);
        }
        adapter = new AddressAdapter(this, list);
        lv_address.setAdapter(adapter);
        stateLayout.setOnNetErrorRetryListener(new StateFrameLayout.OnNetErrorRetryListener() {
            @Override
            public void onNetErrorRetry() {
                request();
            }
        });
        request();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setListener() {
        back.setOnClickListener(this);
        add.setOnClickListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(AddressEvent event) {
        request();
    }

    public void request() {
        if (!checkNetwork()) {
            stateLayout.changeState(StateFrameLayout.NET_ERROR);
            return;
        }
        stateLayout.changeState(StateFrameLayout.LOADING);
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        if (Global.loginResult == null) {
            startActivity(new Intent(mActivity, LoginActivity.class));
            return;
        }
        formBody.add("userid", Global.loginResult.getId());//传递键值对参数
//new call
        Call call = MyOkHttp.GetCall("userAddress.getAddressList", formBody);
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
                Log.d("------地址列表结果------", result);
                Message message = new Message();
                message.what = 0;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (!MyUtils.isFastClick()){
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                setResult(100, new Intent());
                finish();
                break;
            case R.id.save:
                startActivity(new Intent(this, AddAddressActivity.class));
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
                        if ("200".equals(code)) {
                            list.clear();
//                            ArrayList<Address> list=new ArrayList<>();
                            JSONArray jsonArray = new JSONArray(data);
                            Gson gson = new Gson();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                list.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), Address.class));
                            }
                            if (list.size()>0){
                                stateLayout.changeState(StateFrameLayout.SUCCESS);
                                adapter.setGrouplists(list);
                                adapter.notifyDataSetChanged();
                                lv_address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        if (!MyUtils.isFastClick()){
                                            return;
                                        }
                                        if (flag) {
                                            Intent intent = new Intent();
                                            intent.putExtra("address", list.get(i));
                                            setResult(100, intent);
                                            finish();
                                        }
                                    }
                                });
                            }else{
                                stateLayout.changeState(StateFrameLayout.EMPTY);
                            }

                        } else {
                            stateLayout.changeState(StateFrameLayout.EMPTY);
                            MyUtils.showToast(AddressActivity.this, message);
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
}

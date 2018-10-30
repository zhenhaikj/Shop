package com.emjiayuan.app.activity;

import android.content.Intent;
import android.graphics.Color;
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
import com.emjiayuan.app.adapter.IntegralAdapter;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.entity.Integral;
import com.google.gson.Gson;
import com.lwkandroid.stateframelayout.StateFrameLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

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

public class IntegralCenterActivity extends BaseActivity implements View.OnClickListener {


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
    @BindView(R.id.balance)
    TextView balance;
    @BindView(R.id.tab_ll)
    LinearLayout tabLl;
    @BindView(R.id.lv_history)
    ListView lvHistory;
    @BindView(R.id.stateLayout)
    StateFrameLayout stateLayout;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private IntegralAdapter adapter;
    private ArrayList<Integral> list = new ArrayList<>();
    private int pageindex = 1;
    private String status = "1";
    private boolean flag = true;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_integral_center;
    }

    @Override
    protected void initData() {
        title.setText("积分中心");
        save.setText("礼品中心");
        save.setVisibility(View.VISIBLE);
        adapter = new IntegralAdapter(this, list);
        lvHistory.setAdapter(adapter);
        request(pageindex, status);
        refreshLayout.setEnableHeaderTranslationContent(false);
//        refreshLayout.setEnableAutoLoadMore(true);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                flag = false;
                list.clear();
                pageindex = 1;
                refreshLayout.finishLoadMore(false);
                refreshLayout.setEnableLoadMore(true);
                request(pageindex, status);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                flag = false;
                pageindex++;
                request(pageindex, status);
            }
        });
    }

    @Override
    protected void initView() {
        lineTop.setVisibility(View.GONE);

    }

    @Override
    protected void setListener() {
        back.setOnClickListener(this);
        save.setOnClickListener(this);
    }

    public void request(int pageindex, String status) {
        if (flag) {
            stateLayout.changeState(StateFrameLayout.LOADING);
        }
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("userid", Global.loginResult.getId());//传递键值对参数
        formBody.add("pageindex", Integer.toString(pageindex));//传递键值对参数
        formBody.add("pagesize", "100");//传递键值对参数
//        formBody.add("status", status);//传递键值对参数
//new call
        Call call = MyOkHttp.GetCall("User.getPointsRecord", formBody);
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
                MyUtils.e("积分明细", result);
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
                            JSONObject dataObj = new JSONObject(data);
                            balance.setText(dataObj.getString("totalpoints"));
                            JSONArray jsonArray = new JSONArray(dataObj.getString("pointslist"));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                list.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), Integral.class));
                            }
                            stateLayout.changeState(StateFrameLayout.SUCCESS);
                            adapter.notifyDataSetChanged();
                        } else {
                            refreshLayout.finishLoadMore(true);
                            if (pageindex == 1) {
                                stateLayout.changeState(StateFrameLayout.EMPTY);
                            } else {
                                stateLayout.changeState(StateFrameLayout.SUCCESS);
                            }
                            if (pageindex != 1) {
                                MyUtils.showToast(mActivity, "已全部加载");
                            } else {
                                MyUtils.showToast(mActivity, message);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    refreshLayout.finishRefresh();
                    refreshLayout.finishLoadMore();
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
            case R.id.save:
                startActivity(new Intent(mActivity,IntegralYlActivity.class));
                break;
        }
    }
}

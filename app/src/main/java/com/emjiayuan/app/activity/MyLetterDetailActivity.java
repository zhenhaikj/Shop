package com.emjiayuan.app.activity;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.adapter.MyLetterDetailAdapter;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.entity.MyLetter;
import com.emjiayuan.app.entity.MyLetterDetail;
import com.emjiayuan.app.widget.MyListView;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;


public class MyLetterDetailActivity extends BaseActivity implements View.OnClickListener {

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
    MyListView lvMyLetterDetail;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.et_pl)
    EditText etPl;
    @BindView(R.id.send)
    TextView send;
    @BindView(R.id.ll_pl)
    LinearLayout llPl;
    private int pageindex = 1;
    private ArrayList<MyLetterDetail> list = new ArrayList<>();
    private MyLetterDetailAdapter adapter;
    private MyLetter letter;
    private String content="";

    @Override
    protected int setLayoutId() {
        return R.layout.activity_my_letter;
    }

    @Override
    protected void initData() {
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadmore(false);
    }

    @Override
    protected void initView() {
        letter = (MyLetter) getIntent().getSerializableExtra("letter");
        title.setText(letter.getFrom_nickname());
        getWeiboLetterList();
    }

    @Override
    protected void setListener() {
        back.setOnClickListener(this);
        send.setOnClickListener(this);
    }

    /**
     * 私信详情列表
     */
    public void getWeiboLetterList() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("touserid", Global.loginResult.getId());//传递键值对参数
        formBody.add("fromuserid", letter.getFrom_userid());//传递键值对参数
        formBody.add("pageindex", Integer.toString(pageindex));//传递键值对参数
        formBody.add("pagesize", "20");//传递键值对参数
//new call
        Call call = MyOkHttp.GetCall("weibo.getWeiboLetterList", formBody);
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
                MyUtils.e("------私信详情列表------", result);
                Message message = new Message();
                message.what = 0;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }
    /**
     * 回复私信
     */
    public void addWeiboLetter() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("fromuserid", Global.loginResult.getId());//传递键值对参数
        formBody.add("touserid", letter.getFrom_userid());//传递键值对参数
        formBody.add("content", content);//传递键值对参数
//new call
        Call call = MyOkHttp.GetCall("weibo.addWeiboLetter", formBody);
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
                MyUtils.e("------回复私信------", result);
                Message message = new Message();
                message.what = 1;
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

                case 0://私信详情列表
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            JSONArray jsonArray = new JSONArray(data);
                            for (int i = jsonArray.length()-1; i >= 0; i--) {
                                list.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), MyLetterDetail.class));
                            }
                            adapter = new MyLetterDetailAdapter(mActivity, list);
                            lvMyLetterDetail.setAdapter(adapter);
                        } else {
                            MyUtils.showToast(mActivity, message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 1://回复私信
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            MyUtils.showToast(mActivity, message);
                            etPl.setText("");
                            list.clear();
                            getWeiboLetterList();
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
            case R.id.send:
                content=etPl.getText().toString();
                if ("".equals(content)){
                    MyUtils.showToast(mActivity,"请输入内容！");
                    return;
                }
                addWeiboLetter();
                break;
        }
    }

}

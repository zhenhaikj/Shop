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

import com.google.gson.Gson;
import com.lwkandroid.stateframelayout.StateFrameLayout;
import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.adapter.JudgeAdapter;
import com.emjiayuan.app.adapter.OrderAdapter;
import com.emjiayuan.app.entity.Address;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.entity.Order;
import com.emjiayuan.app.event.UpdateEvent;

import org.greenrobot.eventbus.EventBus;
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

public class JudgeActivity extends BaseActivity {

    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.lv_goods)
    ListView lvGoods;
    private Order order;
    private ArrayList<Order.ProductListBean> list=new ArrayList();
    private JudgeAdapter adapter;
    private String commentdata;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_judge;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        Intent intent=getIntent();
        order= (Order) intent.getSerializableExtra("order");
        title.setText("发表评价");
        save.setVisibility(View.VISIBLE);
        save.setText("发布");
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyUtils.isFastClick()){
                    return;
                }
                if (adapter!=null){
                    commentdata=adapter.getCommentdata();
                    if (commentdata!=null){
                        addProductComment();
                    }
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyUtils.isFastClick()){
                    return;
                }
                finish();
            }
        });
        adapter=new JudgeAdapter(mActivity,order.getProduct_list());
        lvGoods.setAdapter(adapter);
    }

    public void saveEditData(int position, String str) {
        Gson gson=new Gson();
        str=gson.toJson(order.getProduct_list());
        MyUtils.e("json",str);
        MyUtils.showToast(this,str+"----"+position);
    }

    public void addProductComment() {

        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体

        formBody.add("userid", Global.loginResult.getId());
        formBody.add("orderid", order.getId());
        formBody.add("commentdata", commentdata);

        Log.d("------参数------", formBody.build().toString());
//new call
        Call call = MyOkHttp.GetCall("product.addProductComment", formBody);
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
                MyUtils.e("------评价------", result);
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
//                        orderList = new ArrayList<>();
                        if ("200".equals(code)) {
                            JSONArray jsonArray = new JSONArray(data);
                            MyUtils.showToast(mActivity, message);
                            EventBus.getDefault().post(new UpdateEvent(""));
                            finish();
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
    protected void setListener() {

    }
}

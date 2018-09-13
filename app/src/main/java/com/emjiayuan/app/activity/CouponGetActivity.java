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
import com.emjiayuan.app.adapter.CouponGetAdapter;
import com.emjiayuan.app.entity.Coupon;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.event.UpdateEvent;
import com.emjiayuan.app.imageloader.GlideImageLoader;
import com.google.gson.Gson;
import com.lwkandroid.stateframelayout.StateFrameLayout;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class CouponGetActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.lv_coupon)
    ListView lv_coupon;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.service_ll)
    LinearLayout serviceLl;
    @BindView(R.id.line_top)
    View lineTop;
    @BindView(R.id.stateLayout)
    StateFrameLayout stateLayout;
    @BindView(R.id.banner)
    Banner banner;
    private CouponGetAdapter adapter;
    private ArrayList<Coupon> list = new ArrayList<>();

    @Override
    protected int setLayoutId() {
        return R.layout.activity_coupon_get;
    }

    @Override
    protected void initData() {
        request();
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        title.setText("优惠券领取");
        adapter = new CouponGetAdapter(this, list);
        lv_coupon.setAdapter(adapter);
    }

    public void request() {
        if (Global.loginResult == null) {
            startActivity(new Intent(mActivity, LoginActivity.class));
            finish();
            return;
        }
        stateLayout.changeState(StateFrameLayout.LOADING);
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        if (Global.loginResult != null) {
            formBody.add("userid", Global.loginResult.getId());//传递键值对参数
        }

//new call
        Call call = MyOkHttp.GetCall("promotion.getCouponList", formBody);
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
                MyUtils.e("==获取优惠券列表==", result);
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
                            String bannerimage = jsonObject.getString("bannerimage");
                            List<String> images = new ArrayList<>();
                            images.add(bannerimage);
                            banner.setImageLoader(new GlideImageLoader());
                            banner.setImages(images);
                            //banner设置方法全部调用完毕时最后调用
                            banner.start();
                            JSONArray jsonArray = new JSONArray(data);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                list.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), Coupon.class));
                            }
                            if (list.size() > 0) {
                                stateLayout.changeState(StateFrameLayout.SUCCESS);
                            } else {
                                stateLayout.changeState(StateFrameLayout.EMPTY);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            stateLayout.changeState(StateFrameLayout.EMPTY);
                            MyUtils.showToast(mActivity, message);
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
        back.setOnClickListener(this);
    }

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(UpdateEvent event) {
        list.clear();
        request();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}

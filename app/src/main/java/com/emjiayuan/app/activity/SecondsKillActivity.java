package com.emjiayuan.app.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.adapter.SecondsKillAdapter;
import com.emjiayuan.app.imageloader.GlideImageLoader;
import com.emjiayuan.app.entity.SeckillBean;
import com.emjiayuan.app.widget.MyListView;
import com.youth.banner.Banner;

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

public class SecondsKillActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.lv_pl)
    MyListView lv_pl;

    private SeckillBean seckillBean;
    private List<String> images=new ArrayList<>();


    @Override
    protected int setLayoutId() {
        return R.layout.activity_seconds_kill;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        title.setText("秒杀专区");
        request();
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());



    }

    @Override
    protected void setListener() {
        back.setOnClickListener(this);
        /*lv_pl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SecondsKillActivity.this, GoodsDetailActivity.class);
                intent.putExtra("product", Global.list.get(i));
                intent.putExtra("kill", true);
                startActivity(intent);
            }
        });*/
    }
    public void request() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
//new call
        Call call = MyOkHttp.GetCall("promotion.getSeckillList", formBody);
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
                Log.d("-----秒杀列表-------", result);
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
                        images=new ArrayList<>();
                        if ("200".equals(code)) {
                            JSONArray jsonArray=new JSONArray(data);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                seckillBean=gson.fromJson(jsonArray.getJSONObject(i).toString(),SeckillBean.class);
                            }
                            SecondsKillAdapter adapter = new SecondsKillAdapter(mActivity, seckillBean);
                            lv_pl.setAdapter(adapter);
                            images.add(seckillBean.getUrl());
                            banner.setImages(images);
                            //banner设置方法全部调用完毕时最后调用
                            banner.start();
                        } else {
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

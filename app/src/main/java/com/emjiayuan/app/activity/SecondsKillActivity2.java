package com.emjiayuan.app.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.adapter.SecondsKillAdapter2;
import com.emjiayuan.app.entity.SeckillBean;
import com.emjiayuan.app.imageloader.GlideImageLoader;
import com.emjiayuan.app.widget.MyListView;
import com.emjiayuan.app.widget.MyScrollView;
import com.google.gson.Gson;
import com.youth.banner.Banner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.iwgang.countdownview.CountdownView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class SecondsKillActivity2 extends BaseActivity implements View.OnClickListener, MyScrollView.OnScrollListener {
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
    @BindView(R.id.service_ll)
    LinearLayout serviceLl;
    @BindView(R.id.line_top)
    View lineTop;
    @BindView(R.id.message)
    TextView tv_message;
    @BindView(R.id.countdownview)
    CountdownView countdownview;
    @BindView(R.id.scrollView)
    MyScrollView scrollView;
    @BindView(R.id.parent_layout)
    FrameLayout parentLayout;
    @BindView(R.id.seckill_ll)
    LinearLayout seckillLl;
    @BindView(R.id.top_seckill_ll)
    LinearLayout topSeckillLl;

    private SeckillBean seckillBean;
    private List<String> images = new ArrayList<>();
    private SecondsKillAdapter2 adapter;
    private String seckillid;


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
        seckillid=getIntent().getStringExtra("seckillid");
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
                Intent intent = new Intent(SecondsKillActivity2.this, GoodsDetailActivity.class);
                intent.putExtra("product", Global.list.get(i));
                intent.putExtra("kill", true);
                startActivity(intent);
            }
        });*/
        scrollView.setOnScrollListener(this);
        //当布局的状态或者控件的可见性发生改变回调的接口
        parentLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                //这一步很重要，使得上面的购买布局和下面的购买布局重合
                onScroll(scrollView.getScrollY());

            }
        });
    }

    public void request() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("seckillid",seckillid);
//new call
        Call call = MyOkHttp.GetCall("promotion.getSeckillDetail", formBody);
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
                Log.d("-----秒杀详情-------", result);
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
                        images = new ArrayList<>();
                        if ("200".equals(code)) {
//                            JSONArray jsonArray = new JSONArray(data);
//                            for (int i = 0; i < jsonArray.length(); i++) {
                                seckillBean = gson.fromJson(data, SeckillBean.class);
//                            }
                            adapter = new SecondsKillAdapter2(mActivity, seckillBean);
                            lv_pl.setAdapter(adapter);
                            images.add(seckillBean.getUrl());
                            banner.setImages(images);
                            //banner设置方法全部调用完毕时最后调用
                            banner.start();
                            if ("0".equals(seckillBean.getStatus())) {
                                tv_message.setText("距离开秒还有");
                            } else if ("1".equals(seckillBean.getStatus())) {
                                tv_message.setText("距离结束还有");
                            } else if ("2".equals(seckillBean.getStatus())) {
                                tv_message.setText("已结束");
                            }
                            countdownview.start(Long.parseLong(seckillBean.getResiduetime()) * 1000);
                            countdownview.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                                @Override
                                public void onEnd(CountdownView cv) {
                                    if ("0".equals(seckillBean.getStatus())) {
                                        tv_message.setText("距离结束还有");
//                                        holder.second_kill_btn.setText("立即抢购");
                                        seckillBean.setStatus("1");
                                        seckillBean.setResiduetime(Long.toString(Long.parseLong(seckillBean.getEndtime()) - Long.parseLong(seckillBean.getStarttime())));
                                        MyUtils.e("---seckillBean.getResiduetime()---", seckillBean.getResiduetime());
                                        MyUtils.e("---seckillBean.getStatus()---", seckillBean.getStatus());
//                    holder.countdownView.setVisibility(View.VISIBLE);
                                        adapter.notifyDataSetChanged();
                                        return;
                                    } else if ("1".equals(seckillBean.getStatus())) {
                                        tv_message.setText("已结束");
//                                        holder.second_kill_btn.setText("已结束");
//                    holder.countdownView.setVisibility(View.GONE);
                                        seckillBean.setStatus("2");
                                        seckillBean.setResiduetime("0");
                                        adapter.notifyDataSetChanged();
                                    }

                                }
                            });
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
        if (!MyUtils.isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    public void onScroll(int scrollY) {
        int mBuyLayout2ParentTop = Math.max(scrollY, seckillLl.getTop());
        topSeckillLl.layout(0, mBuyLayout2ParentTop, topSeckillLl.getWidth(), mBuyLayout2ParentTop + topSeckillLl.getHeight());
    }
}

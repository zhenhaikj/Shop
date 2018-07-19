package com.emjiayuan.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.adapter.IntegralYlAdapter;
import com.emjiayuan.app.banner.GlideImageLoader;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.entity.Integral;
import com.emjiayuan.app.entity.Product;
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

public class IntegralYlActivity extends BaseActivity {
    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.gv)
    GridView gv;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.line_top)
    View lineTop;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private IntegralYlAdapter adapter;
    private int pageindex = 1;


    @Override
    protected int setLayoutId() {
        return R.layout.activity_new_arrivals;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        title.setText("积分有礼");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyUtils.isFastClick()){
                    return;
                }
                finish();
            }
        });
        gv = findViewById(R.id.gv);
        banner = findViewById(R.id.banner);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());

//        banner.setImages(Global.images);
        //banner设置方法全部调用完毕时最后调用
//        banner.start();
        /*XsgItem item1 = new XsgItem("清真伊穆家园牛肉面标准化园牛肉面标准化汤料2.5斤试用装【中端型】", "¥60.00", "$70.00", R.drawable.all);
        for (int i=0;i<20;i++){
            listX.add(item1);
        }*/
        request(pageindex);
        refreshLayout.setEnableAutoLoadmore(true);
        refreshLayout.setEnableHeaderTranslationContent(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                list.clear();
                pageindex = 1;
                refreshLayout.setLoadmoreFinished(false);
                request(pageindex);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageindex++;
                request(pageindex);
            }
        });
    }

    public void request(int pageindex) {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("producttype", "2");//传递键值对参数
        formBody.add("pageindex", Integer.toString(pageindex));//传递键值对参数
        formBody.add("pagesize", "40");//传递键值对参数
        formBody.add("provinceid", Global.provinceid);

//new call
        Call call = MyOkHttp.GetCall("product.getProductList", formBody);
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
                MyUtils.e("-----积分-------", result);
                Message message = new Message();
                message.what = 0;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    private ArrayList<Product> list = new ArrayList<>();
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
                            List<String> images=new ArrayList<>();
                            images.add(bannerimage);
                            banner.setImages(images);
                            //banner设置方法全部调用完毕时最后调用
                            banner.start();
                            JSONArray jsonArray = new JSONArray(data);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                list.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), Product.class));
                            }
                            adapter = new IntegralYlAdapter(mActivity, list);
                            gv.setAdapter(adapter);
                            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    if (!MyUtils.isFastClick()){
                                        return;
                                    }
                                    Intent intent = new Intent(mActivity, IntegralYlDetailActivity.class);
                                    intent.putExtra("productid", list.get(i).getId());
                                    startActivity(intent);
                                }
                            });
                        } else {
                            if (pageindex!=1){
                                MyUtils.showToast(mActivity,"已全部加载");
                            }else{
                                MyUtils.showToast(mActivity,message);
                            }
                            refreshLayout.setLoadmoreFinished(true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
//                    MyUtils.showToast(LoginActivity.this,result);
                    break;
            }
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadmore();
        }
    };

    @Override
    protected void setListener() {

    }
}

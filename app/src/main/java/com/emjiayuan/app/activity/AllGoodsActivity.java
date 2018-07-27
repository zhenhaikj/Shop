package com.emjiayuan.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.adapter.CategoryAdapter;
import com.emjiayuan.app.adapter.CategoryContentAdapter;
import com.emjiayuan.app.entity.Category;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.entity.Product;
import com.emjiayuan.app.entity.Products;
import com.emjiayuan.app.event.UpdateEvent;
import com.emjiayuan.app.fragment.Classify.ClassifyFragment2;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.lwkandroid.stateframelayout.StateFrameLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class AllGoodsActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.lv_content)
    ListView lvContent;
    @BindView(R.id.stateLayout)
    StateFrameLayout stateLayout;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
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

    private CategoryAdapter adapter;
    private CategoryContentAdapter categoryContentAdapter;
    private List<Category> categoryList;
    private List<Product> productList;
    private List<Products> productsList;
    //记录滑动的ListView 滑动的位置   
    private int scrollPosition = -1;
    private boolean flags = true;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_classify2;
    }

    /**
     * 初始化沉浸式
     */
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarDarkFont(true, 0.2f); //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
        mImmersionBar.statusBarColor(R.color.white);
        mImmersionBar.fitsSystemWindows(true);
        mImmersionBar.keyboardEnable(true).navigationBarWithKitkatEnable(false).init();
    }


    @Override
    protected void initData() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
            back.setVisibility(View.VISIBLE);
            title.setText("所有商品");
        stateLayout.setOnNetErrorRetryListener(new StateFrameLayout.OnNetErrorRetryListener() {
            @Override
            public void onNetErrorRetry() {
                reqCategory();
            }
        });
        reqCategory();
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setEnableHeaderTranslationContent(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                flags = false;
                reqCategory();
            }
        });
//        adapter = new CategoryAdapter(getActivity(), Global.Productslist);
//        categoryContentAdapter = new CategoryContentAdapter(getActivity(), Global.Productslist);
//        listview.setAdapter(adapter);
//        lvContent.setAdapter(categoryContentAdapter);

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setListener() {
        llSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyUtils.isFastClick()) {
                    return;
                }
                startActivity(new Intent(mActivity, SearchActivity.class));
            }
        });
        etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyUtils.isFastClick()) {
                    return;
                }
                startActivity(new Intent(mActivity, SearchActivity.class));
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {


                lvContent.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setSelectItem(position);
                        lvContent.setSelection(position);
//                        lvContent.setSelectionFromTop(position,0);
//                        lv_content.smoothScrollToPositionFromTop(position,0);

                    }
                });
            }
        });
        lvContent.setOnScrollListener(listener);
    }

    AbsListView.OnScrollListener listener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView absListView, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (scrollPosition != firstVisibleItem) {
                if (adapter != null) {
                    adapter.setSelectItem(firstVisibleItem);
                    listview.setSelectionFromTop(firstVisibleItem, 40);
                    scrollPosition = firstVisibleItem;
                }
            }
        }
    };

    public void reqCategory() {
        if (!checkNetwork()) {
            stateLayout.changeState(StateFrameLayout.NET_ERROR);
            return;
        }
        if (flags) {
            stateLayout.changeState(StateFrameLayout.LOADING);
        }

        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("provinceid", Global.provinceid);
        Log.d("------参数------", formBody.build().toString());
//new call
        Call call = MyOkHttp.GetCall("product.getCategoryList", formBody);
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
                MyUtils.e("------获取分类结果------", result);
                Message message = new Message();
                message.what = 0;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    public void reqProduct(String categoryid) {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("keyword", "");
        formBody.add("categoryid", categoryid);
        formBody.add("producttype", "1");
        formBody.add("isnew", "1");
        formBody.add("ishot", "1");
        formBody.add("pageindex", "1");
        formBody.add("pagesize", "40");
        formBody.add("provinceid", Global.provinceid);
        Log.d("------参数------", formBody.build().toString());
//new call
        Call call = MyOkHttp.GetCall("product.getProductList", formBody);
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
                MyUtils.e("------获取产品结果------", result);
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
                case 0:
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            stateLayout.changeState(StateFrameLayout.SUCCESS);
                            JSONArray dataArray = new JSONArray(data);
                            productsList = new ArrayList<>();
                            for (int i = 0; i < dataArray.length(); i++) {
                                productsList.add(gson.fromJson(dataArray.getJSONObject(i).toString(), Products.class));
                            }
                            adapter = new CategoryAdapter(mActivity, productsList);
                            listview.setAdapter(adapter);
                            categoryContentAdapter = new CategoryContentAdapter(mActivity, productsList);
                            lvContent.setAdapter(categoryContentAdapter);
                        } else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    refreshLayout.finishRefresh();
                    break;
                case 1:
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            JSONArray dataArray = new JSONArray(data);
                            for (int i = 0; i < dataArray.length(); i++) {
                                productList.add(gson.fromJson(dataArray.getJSONObject(i).toString(), Product.class));
                            }
                        } else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(UpdateEvent event) {

    }

    @Override
    public void onClick(View view) {

    }
}

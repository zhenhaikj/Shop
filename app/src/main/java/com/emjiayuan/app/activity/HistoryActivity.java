package com.emjiayuan.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.Utils.SpUtils;
import com.emjiayuan.app.adapter.HistoryJlAdapter;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.entity.Product;
import com.emjiayuan.app.event.ColUpdateEvent;
import com.emjiayuan.app.fragment.ShoppingCar.ShoppingCarFragment2;
import com.google.gson.Gson;
import com.lwkandroid.stateframelayout.StateFrameLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class HistoryActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.save)
    TextView manage;
    @BindView(R.id.gv_cellection)
    GridView gv_collection;
    @BindView(R.id.checkAll)
    CheckBox checkBox;
    @BindView(R.id.check)
    TextView check;
    @BindView(R.id.delete)
    TextView delete;
    @BindView(R.id.manage_ll)
    LinearLayout manage_ll;
    @BindView(R.id.service_ll)
    LinearLayout serviceLl;
    @BindView(R.id.line_top)
    View lineTop;
    @BindView(R.id.stateLayout)
    StateFrameLayout stateLayout;
    private HistoryJlAdapter adapter;
    private ArrayList<Product> list = new ArrayList<>();
    private boolean mIsFromItem = false;
    private ArrayList<Product> selectList = new ArrayList<>();
    private String productid = "";

    @Override
    protected int setLayoutId() {
        return R.layout.activity_history;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        manage.setVisibility(View.GONE);
        manage.setText("管理");
        manage.setOnClickListener(this);
        title.setText("浏览历史");
        list= (ArrayList<Product>) SpUtils.getDataList("history");
        if (list.size()>0){
            stateLayout.changeState(StateFrameLayout.SUCCESS);
        }else{
            stateLayout.changeState(StateFrameLayout.EMPTY);
        }
        adapter = new HistoryJlAdapter(mActivity, list, new ShoppingCarFragment2.AllCheckListener() {
            @Override
            public void onCheckedChanged(boolean b) {
                //根据不同的情况对maincheckbox做处理
                if (!b && !checkBox.isChecked()) {
                    return;
                } else if (!b && checkBox.isChecked()) {
                    mIsFromItem = true;
                    checkBox.setChecked(false);
                } else if (b) {
                    mIsFromItem = false;
                    checkBox.setChecked(true);
                }
            }
        });
        gv_collection.setAdapter(adapter);
        gv_collection.setNumColumns(3);
//        collection();
    }

    public void collection() {
        stateLayout.changeState(StateFrameLayout.LOADING);
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        if (Global.loginResult == null) {
            startActivity(new Intent(mActivity, LoginActivity.class));
            return;
        }
        formBody.add("userid", Global.loginResult.getId());

        Log.d("------参数------", formBody.build().toString());
//new call
        Call call = MyOkHttp.GetCall("user.getCollectionList", formBody);
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
                MyUtils.e("------收藏列表------", result);
                Message message = new Message();
                message.what = 0;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    public void delete() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        if (Global.loginResult == null) {
            startActivity(new Intent(mActivity, LoginActivity.class));
            return;
        }
        formBody.add("userid", Global.loginResult.getId());
        formBody.add("productid", productid.substring(0, productid.lastIndexOf(",")));

        Log.d("------参数------", formBody.build().toString());
//new call
        Call call = MyOkHttp.GetCall("user.removeCollection", formBody);
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
                MyUtils.e("------删除收藏列表------", result);
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
                        list.clear();
                        if ("200".equals(code)) {
                            JSONArray jsonArray = new JSONArray(data);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                list.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), Product.class));
                            }
                            if (list.size()>0){
                                stateLayout.changeState(StateFrameLayout.SUCCESS);
                            }else{
                                stateLayout.changeState(StateFrameLayout.EMPTY);
                            }
                            adapter = new HistoryJlAdapter(mActivity, list, new ShoppingCarFragment2.AllCheckListener() {
                                @Override
                                public void onCheckedChanged(boolean b) {
                                    //根据不同的情况对maincheckbox做处理
                                    if (!b && !checkBox.isChecked()) {
                                        return;
                                    } else if (!b && checkBox.isChecked()) {
                                        mIsFromItem = true;
                                        checkBox.setChecked(false);
                                    } else if (b) {
                                        mIsFromItem = false;
                                        checkBox.setChecked(true);
                                    }
                                }
                            });
                            gv_collection.setAdapter(adapter);
//                            MyUtils.showToast(mActivity, result);
                        } else {
                            stateLayout.changeState(StateFrameLayout.EMPTY);
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            }
                            MyUtils.showToast(mActivity, message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            collection();
                            manage_ll.setVisibility(View.GONE);
                            manage.setText("管理");
                            if (adapter != null) {
                                adapter.setFlag(false);
                                adapter.notifyDataSetChanged();
                            }
                            selectList.clear();
                            checkBox.setChecked(false);
                            MyUtils.showToast(mActivity, message);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(ColUpdateEvent event) {
        setSelectList(event.getProductList());
    }

    public void setSelectList(List<Product> productList) {
        selectList.clear();
        for (Product product : productList) {
            if (product.isChecked()) {
                selectList.add(product);
            }
        }
    }

    @Override
    protected void setListener() {
        back.setOnClickListener(this);
        gv_collection.setOnItemClickListener(this);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //当监听来源为点击item改变maincbk状态时不在监听改变，防止死循环
                if (mIsFromItem) {
                    mIsFromItem = false;
                    Log.e("mainCheckBox", "此时我不可以触发");
                    return;
                }

                //改变数据
                for (Product carBean : list) {
                    carBean.setChecked(b);
                }
                Map<Integer, Boolean> map = new HashMap<>();
                for (int i = 0; i < list.size(); i++) {
                    if (b) {
                        map.put(i, true);
                    } else {
                        map.remove(i);
                    }
                }
                if (adapter != null) {
                    adapter.setMap(map);
                    //刷新listview
                    adapter.notifyDataSetChanged();
                }

            }
        });
        delete.setOnClickListener(this);
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
            case R.id.delete:
                if (selectList.size() == 0) {
                    MyUtils.showToast(mActivity, "请选择要删除的收藏！");
                    return;
                }
                for (int i = 0; i < selectList.size(); i++) {
                    productid += selectList.get(i).getId() + ",";
                }
                delete();
                break;

            case R.id.save:
                if ("管理".equals(manage.getText().toString())) {
                    manage_ll.setVisibility(View.VISIBLE);
                    manage.setText("完成");
                    if (adapter != null) {
                        adapter.setFlag(true);
                        adapter.notifyDataSetChanged();
                    }

                } else {
                    manage_ll.setVisibility(View.GONE);
                    manage.setText("管理");
                    if (adapter != null) {
                        adapter.setFlag(false);
                        adapter.notifyDataSetChanged();
                    }
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (!MyUtils.isFastClick()) {
            return;
        }
        Intent intent = new Intent(HistoryActivity.this, GoodsDetailActivity.class);
        intent.putExtra("productid", list.get(i).getId());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

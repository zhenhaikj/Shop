package com.emjiayuan.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.emjiayuan.app.entity.Global;
import com.google.gson.Gson;
import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.adapter.SearchAdapter;
import com.emjiayuan.app.adapter.TopUpAdapter;
import com.emjiayuan.app.entity.PayResult;
import com.emjiayuan.app.entity.Product;
import com.emjiayuan.app.entity.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class SearchActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.lv)
    ListView lv;
    private SearchAdapter adapter;
    private ArrayList<Product> list = new ArrayList<>();
    private String keyword="";

    @Override
    protected int setLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
//        Product item1 = new Product("清真伊穆家园牛肉面标准化园牛肉面标准化汤料2.5斤试用装【中端型】", "¥60.00", "$70.00", R.drawable.img1);
//        for (int i = 0; i < 20; i++) {
//            list.add(item1);
//        }

        lv.setOnItemClickListener(this);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                keyword=charSequence.toString();
                request();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void request() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("keyword", keyword);//传递键值对参数
        formBody.add("pageindex", "1");//传递键值对参数
        formBody.add("pagesize", "100");//传递键值对参数
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
                MyUtils.e("------搜索------", result);
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
                        list.clear();
                        if ("200".equals(code)) {
                            JSONArray jsonArray = new JSONArray(data);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Product product=gson.fromJson(jsonArray.getJSONObject(i).toString(), Product.class);
                                list.add(product);
                            }
                            adapter = new SearchAdapter(mActivity, list);
                            lv.setAdapter(adapter);
                        } else {
                            MyUtils.showToast(mActivity,message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;


            }
        }
    };


    @Override
    protected void setListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyUtils.isFastClick()){
                    return;
                }
                finish();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (!MyUtils.isFastClick()){
            return;
        }
        Intent intent=new Intent(mActivity,GoodsDetailActivity.class);
        intent.putExtra("productid",list.get(i).getId());
        startActivity(intent);
    }
}

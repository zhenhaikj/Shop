package com.emjiayuan.app.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.adapter.OrderAdapter;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.entity.LoginResult;
import com.emjiayuan.app.entity.Order;
import com.emjiayuan.app.entity.Product;
import com.emjiayuan.app.event.UpdateEvent;
import com.emjiayuan.app.widget.MyListView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;

public class OrderTopUPActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.all_tv)
    TextView all_tv;
    @BindView(R.id.all_ll)
    LinearLayout all_ll;
    @BindView(R.id.dfk_tv)
    TextView dfk_tv;
    @BindView(R.id.dfk_ll)
    LinearLayout dfk_ll;
    @BindView(R.id.dfh_tv)
    TextView dfh_tv;
    @BindView(R.id.dfh_ll)
    LinearLayout dfh_ll;
    @BindView(R.id.dsh_tv)
    TextView dsh_tv;
    @BindView(R.id.dsh_ll)
    LinearLayout dsh_ll;
    @BindView(R.id.dpj_tv)
    TextView dpj_tv;
    @BindView(R.id.dpj_ll)
    LinearLayout dpj_ll;
    @BindView(R.id.tab_rl)
    LinearLayout tab_rl;
    @BindView(R.id.lv_goods)
    MyListView lv_goods;

    private OrderAdapter adapter;


    @Override
    protected int setLayoutId() {
        return R.layout.activity_order_nomal;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        title.setText("充值礼品");
        dfk_ll.setVisibility(View.GONE);
        adapter=new OrderAdapter(this,new ArrayList<Order>(),2);
        lv_goods.setAdapter(adapter);
        lv_goods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!MyUtils.isFastClick()){
                    return;
                }
                startActivity(new Intent(OrderTopUPActivity.this,OrderDetailActivity.class));
            }
        });
        all_tv.setBackgroundResource(R.drawable.coupon_line);
        dfk_tv.setBackgroundColor(Color.parseColor("#ffffff"));
        dfh_tv.setBackgroundColor(Color.parseColor("#ffffff"));
        dsh_tv.setBackgroundColor(Color.parseColor("#ffffff"));
        dpj_tv.setBackgroundColor(Color.parseColor("#ffffff"));

        all_tv.setTextColor(Color.parseColor("#53D75D"));
        dfk_tv.setTextColor(Color.parseColor("#373737"));
        dfh_tv.setTextColor(Color.parseColor("#373737"));
        dsh_tv.setTextColor(Color.parseColor("#373737"));
        dpj_tv.setTextColor(Color.parseColor("#373737"));
    }

    @Override
    protected void setListener() {
        back.setOnClickListener(this);
        all_ll.setOnClickListener(this);
        dfk_ll.setOnClickListener(this);
        dfh_ll.setOnClickListener(this);
        dsh_ll.setOnClickListener(this);
        dpj_ll.setOnClickListener(this);
    }

    Handler myHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle=msg.getData();
            String result=bundle.getString("result");
            switch (msg.what){
                case 0:
                    try {
                        JSONObject jsonObject=new JSONObject(result);
                        String code=jsonObject.getString("code");
                        String message=jsonObject.getString("message");
                        String data=jsonObject.getString("data");
                        if ("200".equals(code)){
                            Global.loginResult=new Gson().fromJson(data,LoginResult.class);
                            MyUtils.showToast(OrderTopUPActivity.this,message);
                            EventBus.getDefault().post(new UpdateEvent("update"));
                            finish();
                        }else {
                            MyUtils.showToast(OrderTopUPActivity.this,message);
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
    public void setSelect(TextView select,TextView unselect1,TextView unselect2,TextView unselect3,TextView unselect4){
        select.setBackgroundResource(R.drawable.coupon_line);
        unselect1.setBackgroundColor(Color.parseColor("#ffffff"));
        unselect2.setBackgroundColor(Color.parseColor("#ffffff"));
        unselect3.setBackgroundColor(Color.parseColor("#ffffff"));
        unselect4.setBackgroundColor(Color.parseColor("#ffffff"));

        select.setTextColor(Color.parseColor("#53D75D"));
        unselect1.setTextColor(Color.parseColor("#373737"));
        unselect2.setTextColor(Color.parseColor("#373737"));
        unselect3.setTextColor(Color.parseColor("#373737"));
        unselect4.setTextColor(Color.parseColor("#373737"));
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.all_ll:
                setSelect(all_tv,dfk_tv,dfh_tv,dsh_tv,dpj_tv);
                adapter.setGrouplists(new ArrayList<Order>(),0);
                break;
            case R.id.dfk_ll:
                setSelect(dfk_tv,all_tv,dfh_tv,dsh_tv,dpj_tv);
                adapter.setGrouplists(new ArrayList<Order>(),1);
                break;
            case R.id.dfh_ll:
                setSelect(dfh_tv,dfk_tv,all_tv,dsh_tv,dpj_tv);
                adapter.setGrouplists(new ArrayList<Order>(),2);
                break;
            case R.id.dsh_ll:
                setSelect(dsh_tv,dfk_tv,dfh_tv,all_tv,dpj_tv);
                adapter.setGrouplists(new ArrayList<Order>(),3);
                break;
            case R.id.dpj_ll:
                setSelect(dpj_tv,dfk_tv,dfh_tv,dsh_tv,all_tv);
                adapter.setGrouplists(new ArrayList<Order>(),4);
                break;
        }
    }
}

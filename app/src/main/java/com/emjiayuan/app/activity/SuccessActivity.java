package com.emjiayuan.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.MainActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.GlideUtil;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.entity.User;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class SuccessActivity extends BaseActivity {

    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.save)
    TextView save;

    @BindView(R.id.line_top)
    View lineTop;
    @BindView(R.id.back_home)
    Button backHome;
    @BindView(R.id.see_order)
    Button seeOrder;

    private String orderid="";


    @Override
    protected int setLayoutId() {
        return R.layout.activity_success;
    }

    @Override
    protected void initData() {
        Intent intent=getIntent();
        if (intent != null) {
            orderid=intent.getStringExtra("orderid");
        }
    }

    @Override
    protected void initView() {
        title.setText("支付成功");
        back.setVisibility(View.GONE);
        save.setVisibility(View.VISIBLE);
        save.setText("完成");
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyUtils.isFastClick()) {
                    return;
                }
                finish();
            }
        });
    }


    @Override
    protected void setListener() {
        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyUtils.isFastClick()) {
                    return;
                }
                startActivity(new Intent(mActivity, MainActivity.class));
                finish();
            }
        });
        seeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyUtils.isFastClick()) {
                    return;
                }
                Intent intent = new Intent(mActivity, OrderDetailActivity.class);
                intent.putExtra("orderid", orderid);
                intent.putExtra("type", 0);
                startActivity(intent);
                finish();
            }
        });
    }
}

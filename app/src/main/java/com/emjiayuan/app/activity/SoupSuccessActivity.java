package com.emjiayuan.app.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.MainActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.entity.SoupOrder;

import butterknife.BindView;

public class SoupSuccessActivity extends BaseActivity {

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
            orderid= intent.getStringExtra("orderid");
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
                Intent intent=new Intent(mActivity, SoupOrderDetailActivity.class);
                intent.putExtra("orderid",orderid);
                mActivity.startActivity(intent);
                finish();
            }
        });
    }
}

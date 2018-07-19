package com.emjiayuan.app.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.adapter.CouponAdapter2;
import com.emjiayuan.app.entity.Coupon;
import com.emjiayuan.app.entity.OrderComfirm;
import com.lwkandroid.stateframelayout.StateFrameLayout;

import java.util.ArrayList;

import butterknife.BindView;

public class CouponUseActivity extends BaseActivity implements View.OnClickListener {
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
    private CouponAdapter2 adapter;
    private ArrayList<OrderComfirm.UsercouponsBean> couponList;
    private ArrayList<Coupon> list = new ArrayList<>();

    @Override
    protected int setLayoutId() {
        return R.layout.activity_coupon_get;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        title.setText("优惠券使用");
        Intent intent = getIntent();
        couponList = (ArrayList<OrderComfirm.UsercouponsBean>) intent.getSerializableExtra("coupon");
        if (couponList.size()>0){
            stateLayout.changeState(StateFrameLayout.SUCCESS);
        }else{
            stateLayout.changeState(StateFrameLayout.EMPTY);
        }
        adapter = new CouponAdapter2(this, couponList);
        lv_coupon.setAdapter(adapter);
    }

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
}

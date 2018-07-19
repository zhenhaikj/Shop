package com.emjiayuan.app.activity;

import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.adapter.CouponAdapter;
import com.emjiayuan.app.entity.Coupon;

import java.util.ArrayList;

import butterknife.BindView;

public class CouponActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.tab_wsy)
    TextView tab1;
    @BindView(R.id.wsy_ll)
    LinearLayout wsyLl;
    @BindView(R.id.tab_syjl)
    TextView tab2;
    @BindView(R.id.syjl_ll)
    LinearLayout syjlLl;
    @BindView(R.id.tab_ygq)
    TextView tab3;
    @BindView(R.id.ygq_ll)
    LinearLayout ygqLl;
    @BindView(R.id.tab_rl)
    LinearLayout tabRl;
    @BindView(R.id.lv_coupon)
    ListView lv_coupon;
    private CouponAdapter adapter;
    private ArrayList<Coupon> list = new ArrayList<>();

    @Override
    protected int setLayoutId() {
        return R.layout.activity_coupon;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        title.setText("优惠券");
        adapter = new CouponAdapter(this, list);
        lv_coupon.setAdapter(adapter);

        tab1.setBackgroundResource(R.drawable.coupon_line);
        tab2.setBackgroundColor(Color.parseColor("#ffffff"));
        tab3.setBackgroundColor(Color.parseColor("#ffffff"));
        tab1.setTextColor(Color.parseColor("#53D75D"));
        tab2.setTextColor(Color.parseColor("#373737"));
        tab3.setTextColor(Color.parseColor("#373737"));
    }

    @Override
    protected void setListener() {
        back.setOnClickListener(this);
        tab1.setOnClickListener(this);
        tab2.setOnClickListener(this);
        tab3.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (!MyUtils.isFastClick()){
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tab_wsy:
//                search_ll.setVisibility(View.VISIBLE);
                tab1.setBackgroundResource(R.drawable.coupon_line);
                tab2.setBackgroundColor(Color.parseColor("#ffffff"));
                tab3.setBackgroundColor(Color.parseColor("#ffffff"));
                tab1.setTextColor(Color.parseColor("#53D75D"));
                tab2.setTextColor(Color.parseColor("#373737"));
                tab3.setTextColor(Color.parseColor("#373737"));
                break;
            case R.id.tab_syjl:
//                search_ll.setVisibility(View.GONE);
                tab2.setBackgroundResource(R.drawable.coupon_line);
                tab1.setBackgroundColor(Color.parseColor("#ffffff"));
                tab3.setBackgroundColor(Color.parseColor("#ffffff"));
                tab2.setTextColor(Color.parseColor("#53D75D"));
                tab1.setTextColor(Color.parseColor("#373737"));
                tab3.setTextColor(Color.parseColor("#373737"));
                break;
            case R.id.tab_ygq:
//                search_ll.setVisibility(View.GONE);
                tab3.setBackgroundResource(R.drawable.coupon_line);
                tab2.setBackgroundColor(Color.parseColor("#ffffff"));
                tab1.setBackgroundColor(Color.parseColor("#ffffff"));
                tab3.setTextColor(Color.parseColor("#53D75D"));
                tab2.setTextColor(Color.parseColor("#373737"));
                tab1.setTextColor(Color.parseColor("#373737"));
                break;
        }
    }
}

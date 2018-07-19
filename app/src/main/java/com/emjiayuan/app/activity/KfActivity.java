package com.emjiayuan.app.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyUtils;

import butterknife.BindView;

public class KfActivity extends BaseActivity {
    @BindView(R.id.back)
     LinearLayout back;
    @BindView(R.id.title)
     TextView title;


    @Override
    protected int setLayoutId() {
        return R.layout.activity_new_arrivals;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        title.setText("客服");
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
    protected void setListener() {

    }
}

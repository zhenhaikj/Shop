package com.emjiayuan.app.activity;

import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.fragment.Classify.ClassifyFragment2;

import butterknife.BindView;

public class AllGoodsActivity extends BaseActivity implements View.OnClickListener {
    /*@BindView(R.id.back)
     LinearLayout back;
    @BindView(R.id.title)
     TextView title;*/
    @Override
    protected int setLayoutId() {
        return R.layout.activity_all_goods;
    }
    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
//        title.setText("所有商品");
        ClassifyFragment2 myFragment = new ClassifyFragment2(false);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_all, myFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void setListener() {
//        back.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        if (!MyUtils.isFastClick()){
            return;
        }
        switch (view.getId()){
            case R.id.back:
//                finish();
                break;
        }
    }
}

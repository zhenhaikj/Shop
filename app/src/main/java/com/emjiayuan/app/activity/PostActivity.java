package com.emjiayuan.app.activity;


import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.widget.MyGridView;

import butterknife.BindView;


public class PostActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.icon_back)
    ImageView iconBack;
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.add)
    TextView add;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.gv_icons)
    MyGridView gvIcons;
    @BindView(R.id.gv_label)
    MyGridView gvLabel;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_post;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (!MyUtils.isFastClick()){
            return;
        }

    }
}

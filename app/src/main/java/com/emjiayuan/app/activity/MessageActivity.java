package com.emjiayuan.app.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.adapter.MessageAdapter;
import com.emjiayuan.app.entity.News;
import com.youth.banner.Banner;

import java.util.ArrayList;

import butterknife.BindView;

public class MessageActivity extends BaseActivity {

    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.lv_message)
    ListView lvMessage;
    private ArrayList<News> news;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        title.setText("消息中心");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyUtils.isFastClick()){
                    return;
                }
                finish();
            }
        });
        news= (ArrayList<News>) getIntent().getSerializableExtra("news");
        lvMessage.setAdapter(new MessageAdapter(mActivity,news));
        lvMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!MyUtils.isFastClick()){
                    return;
                }
                Intent intent=new Intent(mActivity,MessageDetailActivity.class);
                intent.putExtra("news",news);
                intent.putExtra("position",i);
                intent.putExtra("flag",true);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void setListener() {

    }
}

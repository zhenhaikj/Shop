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
import com.emjiayuan.app.widget.MyListView;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class PostDetailActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

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
    @BindView(R.id.icon)
    CircleImageView icon;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.gv_icons)
    MyGridView gvIcons;
    @BindView(R.id.label)
    TextView label;
    @BindView(R.id.time_icon)
    ImageView timeIcon;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.zan_icon)
    ImageView zanIcon;
    @BindView(R.id.zan_count)
    TextView zanCount;
    @BindView(R.id.pl_icon)
    ImageView plIcon;
    @BindView(R.id.pl_count)
    TextView plCount;
    @BindView(R.id.gv_zanicons)
    MyGridView gvZanicons;
    @BindView(R.id.lv_pl)
    MyListView lvPl;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.ll_post)
    LinearLayout llPost;
    @BindView(R.id.et_pl)
    EditText etPl;
    @BindView(R.id.send)
    TextView send;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_post_detail;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setListener() {
        back.setOnClickListener(this);
        add.setOnClickListener(this);
        send.setOnClickListener(this);
        zanIcon.setOnClickListener(this);
        plIcon.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (!MyUtils.isFastClick()){
            return;
        }

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
            case R.id.all:
//                finish();
                break;
            case R.id.zan_icon:
//                finish();
                break;
            case R.id.pl_icon:
//                finish();
                break;
            case R.id.send:
//                finish();
                break;
        }
    }
}

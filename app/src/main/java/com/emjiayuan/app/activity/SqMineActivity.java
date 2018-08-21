package com.emjiayuan.app.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.GlideUtil;
import com.emjiayuan.app.Utils.ImageCompress;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.adapter.IconsAdapter;
import com.emjiayuan.app.adapter.LabelAdapter;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.entity.Label;
import com.emjiayuan.app.widget.DragGridView;
import com.google.gson.Gson;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;


public class SqMineActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.service_ll)
    LinearLayout serviceLl;
    @BindView(R.id.line_top)
    View lineTop;
    @BindView(R.id.img_tx)
    CircleImageView imgTx;
    @BindView(R.id.ll_mypost)
    LinearLayout llMypost;
    @BindView(R.id.ll_mymessage)
    LinearLayout llMymessage;
    @BindView(R.id.ll_myletter)
    LinearLayout llMyletter;


    @Override
    protected int setLayoutId() {
        return R.layout.activity_sq_mine;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        title.setText("圈子");
        GlideUtil.loadImageViewLoding(mActivity,Global.loginResult.getHeadimg(),imgTx,R.drawable.default_tx,R.drawable.default_tx);
    }

    @Override
    protected void setListener() {
        back.setOnClickListener(this);
        llMypost.setOnClickListener(this);
        llMymessage.setOnClickListener(this);
        llMyletter.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ll_mypost:
                startActivity(new Intent(SqMineActivity.this,MyPostActivity.class));
                break;
            case R.id.ll_mymessage:
                startActivity(new Intent(SqMineActivity.this,MyMessageActivity.class));
                break;
            case R.id.ll_myletter:
                startActivity(new Intent(SqMineActivity.this,MyLetterActivity.class));
                break;
        }
    }

}

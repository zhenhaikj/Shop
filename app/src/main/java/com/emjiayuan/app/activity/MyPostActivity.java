package com.emjiayuan.app.activity;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.adapter.PostsAdapter;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.entity.Post;
import com.emjiayuan.app.event.CommentEvent;
import com.emjiayuan.app.event.UpdateEvent;
import com.emjiayuan.app.widget.MyListView;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;


public class MyPostActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

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
    @BindView(R.id.lv_post)
    MyListView lvPost;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.et_pl)
    EditText etPl;
    @BindView(R.id.send)
    TextView send;
    @BindView(R.id.ll_pl)
    LinearLayout llPl;
    @BindView(R.id.sv)
    ScrollView sv;
    private int pageindex = 1;
    private ArrayList<Post> list = new ArrayList<>();
    private PostsAdapter adapter;
    private Post.ReplylistBean bean;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_my_post;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initData() {
        refreshLayout.setEnableHeaderTranslationContent(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                list.clear();
                pageindex = 1;
                refreshLayout.setLoadmoreFinished(false);
                getWeiboList();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageindex++;
                getWeiboList();
            }
        });
        sv.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                llPl.setVisibility(View.GONE);
                MyUtils.hideSoftInput(mActivity, etPl);
            }
        });
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        title.setText("我的帖子");
        getWeiboList();
    }

    @Override
    protected void setListener() {
        back.setOnClickListener(this);
        save.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (!MyUtils.isFastClick()) {
            return;
        }

    }

    /**
     * 帖子列表
     */
    public void getWeiboList() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("type", "");//传递键值对参数
        formBody.add("userid", Global.loginResult.getId());//传递键值对参数
        if (Global.loginResult != null) {
            formBody.add("loginuserid", Global.loginResult.getId());//传递键值对参数
        }

        formBody.add("pageindex", Integer.toString(pageindex));//传递键值对参数
        formBody.add("pagesize", "20");//传递键值对参数
//new call
        Call call = MyOkHttp.GetCall("weibo.getWeiboList", formBody);
//请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("------------", e.toString());
//                myHandler.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                MyUtils.e("------帖子列表------", result);
                Message message = new Message();
                message.what = 0;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    /**
     * 评论
     */
    public void addWeiboReply(String weiboid, String content, String rid) {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("weiboid", weiboid);//传递键值对参数
        formBody.add("userid", Global.loginResult.getId());//传递键值对参数
        formBody.add("content", content);//传递键值对参数
        formBody.add("rid", rid);//传递键值对参数
//new call
        Call call = MyOkHttp.GetCall("weibo.addWeiboReply", formBody);
//请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("------------", e.toString());
//                myHandler.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                MyUtils.e("------评论------", result);
                Message message = new Message();
                message.what = 3;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }


    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String result = bundle.getString("result");
            switch (msg.what) {

                case 0://类型
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            JSONArray jsonArray = new JSONArray(data);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                list.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), Post.class));
                            }
                            if (adapter == null) {
                                adapter = new PostsAdapter(mActivity, list, true);
                                lvPost.setAdapter(adapter);
                            } else {
                                adapter.notifyDataSetChanged();
                            }
                            lvPost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent intent = new Intent(mActivity, PostDetailActivity.class);
                                    intent.putExtra("weiboid", list.get(i).getId());
                                    startActivity(intent);
                                }
                            });
                        } else {
                            MyUtils.showToast(mActivity, message);
                        }
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadmore();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 3://评论
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            etPl.setText("");
                            llPl.setVisibility(View.GONE);
                            MyUtils.hideSoftInput(mActivity, etPl);
                            list.clear();
                            getWeiboList();
                        } else {
                            MyUtils.showToast(mActivity, message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

            }
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(UpdateEvent event) {
        list.clear();
        pageindex = 1;
        getWeiboList();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(final CommentEvent event) {
        final Post post = event.getPost();
        final int position = event.getPosition();
        if (position == -1) {
            etPl.setHint("可以留言哦~");
        } else if (position == -2) {
            etPl.setHint("私信" + post.getUsername());
        } else {
            bean = post.getReplylist().get(position);
            etPl.setHint("回复" + bean.getUsername() + ":");
        }
        llPl.setVisibility(View.VISIBLE);
        etPl.requestFocus();
        MyUtils.showSoftInput(mActivity, etPl);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = etPl.getText().toString();
                if ("".equals(content)) {
                    MyUtils.showToast(mActivity, "请输入内容！");
                    return;
                }
                if (position == -2) {
//                    addWeiboLetter(post.getUserid(), content);
                } else if (position == -1) {
                    addWeiboReply(post.getId(), content, "");
                } else {
                    addWeiboReply(post.getId(), content, bean.getUserid());
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;

        }
    }

}

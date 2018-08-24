package com.emjiayuan.app.fragment.Community;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.GlideUtil;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.activity.MyMessageActivity;
import com.emjiayuan.app.activity.PostActivity;
import com.emjiayuan.app.activity.PostDetailActivity;
import com.emjiayuan.app.activity.SqMineActivity;
import com.emjiayuan.app.adapter.LabelAdapter;
import com.emjiayuan.app.adapter.PostsAdapter;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.entity.Label;
import com.emjiayuan.app.entity.NineGridTestModel;
import com.emjiayuan.app.entity.Post;
import com.emjiayuan.app.event.CommentEvent;
import com.emjiayuan.app.event.DeletePostEvent;
import com.emjiayuan.app.event.UpdateEvent;
import com.emjiayuan.app.event.ZanEvent;
import com.emjiayuan.app.fragment.BaseLazyFragment;
import com.emjiayuan.app.widget.MyGridView;
import com.emjiayuan.app.widget.MyListView;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;


public class CommunityFragment extends BaseLazyFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    @BindView(R.id.txt_content)
    RelativeLayout txtContent;
    @BindView(R.id.bg)
    ImageView bg;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.profile_image)
    CircleImageView profileImage;
    @BindView(R.id.gv_label)
    MyGridView gvLabel;
    @BindView(R.id.lv_post)
    MyListView lvPost;
    @BindView(R.id.fresh)
    ImageView fresh;
    @BindView(R.id.write)
    ImageView write;
    @BindView(R.id.back)
    LinearLayout back;
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
    @BindView(R.id.ll_mine)
    LinearLayout llMine;
    @BindView(R.id.message_icon)
    ImageView messageIcon;
    @BindView(R.id.message_count)
    TextView messageCount;
    @BindView(R.id.ll_message)
    LinearLayout llMessage;

    private LabelAdapter labelAdapter;
    private PostsAdapter mAdapter;
    private Post post;
    private ArrayList<Post> list = new ArrayList<>();
    private ArrayList<Label> labels = new ArrayList<>();
    private List<NineGridTestModel> mList = new ArrayList<>();
    private String[] mUrls = new String[]{"http://d.hiphotos.baidu.com/image/h%3D200/sign=201258cbcd80653864eaa313a7dca115/ca1349540923dd54e54f7aedd609b3de9c824873.jpg",
            "http://d.hiphotos.baidu.com/image/h%3D200/sign=ea218b2c5566d01661199928a729d498/a08b87d6277f9e2fd4f215e91830e924b999f308.jpg",
            "http://img4.imgtn.bdimg.com/it/u=3445377427,2645691367&fm=21&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=2644422079,4250545639&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=1444023808,3753293381&fm=21&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=882039601,2636712663&fm=21&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=4119861953,350096499&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=2437456944,1135705439&fm=21&gp=0.jpg",
            "http://img4.duitang.com/uploads/item/201506/11/20150611000809_yFe5Z.jpeg",
            "http://img5.imgtn.bdimg.com/it/u=1717647885,4193212272&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=2024625579,507531332&fm=21&gp=0.jpg"};
    private int pageindex = 1;
    private String type = "";//帖子类型
    Post.ReplylistBean bean;
    private int position;


    @Override
    protected int setLayoutId() {
        return R.layout.fragment_community;
    }

    /**
     * 初始化沉浸式
     */
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarDarkFont(true, 0.2f); //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
        mImmersionBar.statusBarColor(R.color.white);
        mImmersionBar.fitsSystemWindows(true);
        mImmersionBar.keyboardEnable(true).navigationBarWithKitkatEnable(false).init();
    }

    @SuppressLint("NewApi")
    @Override
    protected void initData() {
        GlideUtil.loadImageViewLoding(mActivity, Global.loginResult.getHeadimg(), profileImage, R.drawable.default_tx, R.drawable.default_tx);
        username.setText(Global.loginResult.getNickname());
        getWeiboTypeList();
        getWeiboList();
        getWeiboMessage();
        refreshLayout.setEnableHeaderTranslationContent(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mAdapter.notifyDataSetChanged();
                list.clear();
                pageindex = 1;
                refreshLayout.setLoadmoreFinished(false);
                getWeiboList();
                getWeiboMessage();
                GlideUtil.loadImageViewLoding(mActivity, Global.loginResult.getHeadimg(), profileImage, R.drawable.default_tx, R.drawable.default_tx);
                username.setText(Global.loginResult.getNickname());
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageindex++;
                getWeiboList();
            }
        });
        gvLabel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                labelAdapter.setSelected(i);
                type = labels.get(i).getId();
                pageindex = 1;
                list.clear();
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

    /**
     * 帖子列表
     */
    public void getWeiboList() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("type", type);//传递键值对参数
//        formBody.add("userid", "1");//传递键值对参数
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
     * 详情
     */
    public void getWeiboDetail(String weiboid) {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("weiboid", weiboid);//传递键值对参数
        formBody.add("loginuserid", Global.loginResult.getId());//传递键值对参数
//new call
        Call call = MyOkHttp.GetCall("weibo.getWeiboDetail", formBody);
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
                MyUtils.e("------帖子详情------", result);
                Message message = new Message();
                message.what = 5;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }
    /**
     * 帖子类型
     */
    public void getWeiboTypeList() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体

//new call
        Call call = MyOkHttp.GetCall("weibo.getWeiboTypeList", formBody);
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
                MyUtils.e("------帖子类型------", result);
                Message message = new Message();
                message.what = 1;
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
    /**
     * 私信
     */
    public void addWeiboLetter(String touserid, String content) {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("touserid", touserid);//传递键值对参数
        formBody.add("content", content);//传递键值对参数
        formBody.add("fromuserid", Global.loginResult.getId());//传递键值对参数
//new call
        Call call = MyOkHttp.GetCall("weibo.addWeiboLetter", formBody);
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
                MyUtils.e("------私信------", result);
                Message message = new Message();
                message.what = 4;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }


    /**
     * 获取最新回复消息
     */
    public void getWeiboMessage() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("userid", Global.loginResult.getId());//传递键值对参数
//new call
        Call call = MyOkHttp.GetCall("weibo.getWeiboMessage", formBody);
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
                MyUtils.e("------最新回复消息------", result);
                Message message = new Message();
                message.what = 2;
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
                case 0://帖子列表
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            JSONArray dataArray = new JSONArray(data);
                            for (int i = 0; i < dataArray.length(); i++) {
                                list.add(gson.fromJson(dataArray.getJSONObject(i).toString(), Post.class));
                            }
                            if (mAdapter == null) {
                                mAdapter = new PostsAdapter(mActivity, list,false);
                                lvPost.setAdapter(mAdapter);
                            } else {
                                mAdapter.notifyDataSetChanged();
                            }
                        } else {
                            MyUtils.showToast(mActivity, message);
                        }
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadmore();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case 1://帖子类型
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            JSONArray jsonArray = new JSONArray(data);
                            labels.add(new Label("", "全部消息", "", ""));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                labels.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), Label.class));
                            }
                            labelAdapter = new LabelAdapter(mActivity, labels);
                            gvLabel.setAdapter(labelAdapter);
                            labelAdapter.setSelected(0);
                        } else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2://最新回复消息
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            JSONObject jsonObject1 = new JSONObject(data);
                            llMessage.setVisibility("0".equals(jsonObject1.getString("num"))?View.GONE:View.VISIBLE);
                            messageCount.setText(jsonObject1.getString("num")+"条新消息");
                            String path=jsonObject1.getJSONObject("user").getString("headimg");
                            GlideUtil.loadImageViewLoding(mActivity,path,messageIcon,R.drawable.default_tx,R.drawable.default_tx);
                        } else {
                            MyUtils.showToast(mActivity, result);
                        }
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
                            MyUtils.showToast(mActivity, message);
                            etPl.setText("");
                            llPl.setVisibility(View.GONE);
                            MyUtils.hideSoftInput(mActivity, etPl);
                            getWeiboDetail(list.get(position).getId());
                        } else {
                            MyUtils.showToast(mActivity, message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 4://私信
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            MyUtils.showToast(mActivity, message);
                            etPl.setText("");
                            llPl.setVisibility(View.GONE);
                            MyUtils.hideSoftInput(mActivity, etPl);
                        } else {
                            MyUtils.showToast(mActivity, message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 5://详情
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            post = gson.fromJson(data, Post.class);
                            list.set(position,post);
                            mAdapter.notifyDataSetChanged();
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

    @Override
    protected void setListener() {
        fresh.setOnClickListener(this);
        write.setOnClickListener(this);
        lvPost.setOnItemClickListener(this);
        gvLabel.setOnItemClickListener(this);
        llMine.setOnClickListener(this);
        llMessage.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (!MyUtils.isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.fresh:
                refreshLayout.autoRefresh();
                sv.smoothScrollTo(0, 0);
                break;
            case R.id.write:
                startActivity(new Intent(getActivity(), PostActivity.class));
                break;
            case R.id.ll_mine:
                startActivity(new Intent(getActivity(), SqMineActivity.class));
                break;
            case R.id.ll_message:
                startActivity(new Intent(getActivity(), MyMessageActivity.class));
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (!MyUtils.isFastClick()) {
            return;
        }
        switch (adapterView.getId()) {
            case R.id.gv_label:

                break;
            case R.id.lv_post:
                Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                intent.putExtra("weiboid", list.get(i).getId());
                startActivity(intent);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(UpdateEvent event) {
        list.clear();
        pageindex=1;
        getWeiboList();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(ZanEvent event) {
        position=event.getPosition();
        getWeiboDetail(list.get(position).getId());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(DeletePostEvent event) {
        position=event.getPosition();
        list.remove(position);
        mAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(final CommentEvent event) {
        final Post post = event.getPost();
        final int commentposition = event.getCommentposition();
        position = event.getPosition();
        if (commentposition == -1){
            etPl.setHint("可以留言哦~");
        }else if (commentposition==-2){
            etPl.setHint("私信"+post.getNickname());
        }else{
            bean = post.getReplylist().get(commentposition);
            etPl.setHint("回复" + bean.getNickname() + ":");
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
                if (commentposition == -2) {
                    addWeiboLetter(post.getUserid(),content);
                } else if (commentposition == -1){
                    addWeiboReply(post.getId(), content, "");
                }else{
                    addWeiboReply(post.getId(), content, bean.getUserid());
                }

            }
        });
    }

//    @Override
//    protected void onVisible() {
//        super.onVisible();
//        list.clear();
//        pageindex = 1;
//        getWeiboList();
//        getWeiboMessage();
//        GlideUtil.loadImageViewLoding(mActivity, Global.loginResult.getHeadimg(), profileImage, R.drawable.default_tx, R.drawable.default_tx);
//        username.setText(Global.loginResult.getNickname());
//    }
}
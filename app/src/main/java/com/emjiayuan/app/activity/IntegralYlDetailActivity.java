package com.emjiayuan.app.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.adapter.CommentAdapter;
import com.emjiayuan.app.imageloader.GlideImageLoader;
import com.emjiayuan.app.entity.Comment;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.entity.OrderComfirm;
import com.emjiayuan.app.entity.Product;
import com.emjiayuan.app.entity.SeckillBean;
import com.emjiayuan.app.entity.User;
import com.emjiayuan.app.widget.MyListView;
import com.google.gson.Gson;
import com.lwkandroid.stateframelayout.StateFrameLayout;
import com.qiyukf.unicorn.api.ConsultSource;
import com.qiyukf.unicorn.api.Unicorn;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.iwgang.countdownview.CountdownView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class IntegralYlDetailActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.line_top)
    View lineTop;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.countdownview)
    CountdownView countdownview;
    @BindView(R.id.kill_bar_ll)
    LinearLayout killBarLl;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.old_price)
    TextView oldPrice;
    @BindView(R.id.zs)
    TextView zs;
    @BindView(R.id.youhui)
    TextView youhui;
    @BindView(R.id.xl)
    TextView xl;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.share)
    ImageView share;
    @BindView(R.id.kc)
    TextView kc;
    @BindView(R.id.ys)
    TextView ys;
    @BindView(R.id.cd)
    TextView cd;
    @BindView(R.id.tv_detail)
    TextView tvDetail;
    @BindView(R.id.detail_ll)
    LinearLayout detailLl;
    @BindView(R.id.tv_pl)
    TextView tvPl;
    @BindView(R.id.pl_ll)
    LinearLayout plLl;
    @BindView(R.id.detail_icon)
    ImageView detailIcon;
    @BindView(R.id.lv_pl)
    MyListView lvPl;
    @BindView(R.id.add)
    TextView add;
    @BindView(R.id.buy)
    TextView buy;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.service_ll)
    LinearLayout serviceLl;
    @BindView(R.id.stateLayout_pl)
    StateFrameLayout stateLayoutPl;
    @BindView(R.id.stateLayout)
    StateFrameLayout stateLayout;
    @BindView(R.id.webview)
    WebView webview;
    private Product product;
    private PopupWindow popupWindow;
    private ArrayList<Comment> commentArrayList = new ArrayList<>();
    private int num = 1;
    private int pageindex = 1;
    private String productids = "";
    private String productid = "";
    private String promotiontype = "";
    private String promotionvalue = "";
    private boolean flag;
    private SeckillBean seckillBean;
    private User user;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_integralyl_detail;
    }

    @Override
    protected void initData() {
        refreshLayout.setEnableHeaderTranslationContent(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getProduct(productid);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                pageindex++;
                getComment();
            }
        });
    }

    public void getComment() {
        stateLayoutPl.changeState(StateFrameLayout.LOADING);
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("productid", productid);
        formBody.add("pageindex", Integer.toString(pageindex));
        formBody.add("pagesize", "100");
        Log.d("------参数------", formBody.build().toString());
//new call
        Call call = MyOkHttp.GetCall("product.getProductCommentList", formBody);
//请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("------------", e.toString());
//                        myHandler.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                MyUtils.e("------获取评论结果------", result);
                Message message = new Message();
                message.what = 4;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    @Override
    protected void initView() {
        oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        oldPrice.getPaint().setAntiAlias(true);
        serviceLl.setVisibility(View.VISIBLE);
        title.setText("商品详情");
        Intent intent = getIntent();
        flag = intent.getBooleanExtra("kill", false);
        if (flag) {
            killBarLl.setVisibility(View.VISIBLE);
            youhui.setVisibility(View.VISIBLE);
            xl.setVisibility(View.VISIBLE);
            seckillBean = (SeckillBean) intent.getSerializableExtra("seckill");
            promotiontype = "ms";
            promotionvalue = seckillBean.getId();
        } else {
            killBarLl.setVisibility(View.GONE);
            youhui.setVisibility(View.GONE);
            xl.setVisibility(View.GONE);
        }
        productid = intent.getStringExtra("productid");
        getProduct(productid);
        user();
    }

    public void setdata() {
        price.setText("兑换积分：" + product.getJifen());
        oldPrice.setText("市场价：¥" + product.getPrice());
        name.setText(product.getName());
        kc.setText("库存：" + product.getTotalnum());
        ys.setText("已售：" + product.getSellnum());
        cd.setText("产地：" + product.getSource());
//        RequestOptions options = new RequestOptions()
//                .fitCenter();
//        Glide.with(this).load(product.getContent()).apply(options).into(detailIcon);
        String html = "<html>"
                + "<body>"
                + product.getContent() + "</body>" + "</html>";

        webview.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebChromeClient(new WebChromeClient());

        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        List images = new ArrayList();
        if (product.getImages() != null && !"".equals(product.getImages())) {
            images.add(product.getImages());
        }
        if (product.getImages2() != null && !"".equals(product.getImages2())) {
            images.add(product.getImages2());
        }
        if (product.getImages3() != null && !"".equals(product.getImages3())) {
            images.add(product.getImages3());
        }
        if (product.getImages4() != null && !"".equals(product.getImages4())) {
            images.add(product.getImages4());
        }

        banner.setImages(images);
        //banner设置方法全部调用完毕时最后调用
        banner.start();

    }

    @Override
    protected void setListener() {
        back.setOnClickListener(this);
        share.setOnClickListener(this);
        add.setOnClickListener(this);
        buy.setOnClickListener(this);
        detailLl.setOnClickListener(this);
        plLl.setOnClickListener(this);
        serviceLl.setOnClickListener(this);
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
            case R.id.service_ll:
                String title = "伊穆家园客服";
                /**
                 * 设置访客来源，标识访客是从哪个页面发起咨询的，用于客服了解用户是从什么页面进入。
                 * 三个参数分别为：来源页面的url，来源页面标题，来源页面额外信息（保留字段，暂时无用）。
                 * 设置来源后，在客服会话界面的"用户资料"栏的页面项，可以看到这里设置的值。
                 */
                ConsultSource source = new ConsultSource("app", "app", "custom information string");
                /**
                 * 请注意： 调用该接口前，应先检查Unicorn.isServiceAvailable()，
                 * 如果返回为false，该接口不会有任何动作
                 *
                 * @param context 上下文
                 * @param title   聊天窗口的标题
                 * @param source  咨询的发起来源，包括发起咨询的url，title，描述信息等
                 */
                Unicorn.openServiceActivity(mActivity, title, source);
                break;
            case R.id.buy:
                if (Integer.parseInt(user.getJifen()) - Integer.parseInt(product.getJifen()) < 0) {
                    MyUtils.showToast(mActivity, "积分不足！");
                    return;
                }
                confirmOrder();
                break;
            case R.id.detail_ll:
                tvDetail.setTextColor(getResources().getColor(R.color.tv_detail_color));
                tvPl.setTextColor(getResources().getColor(R.color.tv_detail_uncheck_color));
                tvDetail.setBackgroundResource(R.drawable.detail_line);
                tvPl.setBackgroundResource(R.drawable.detail_line_uncheck);
                webview.setVisibility(View.VISIBLE);
                stateLayoutPl.setVisibility(View.GONE);
                refreshLayout.setEnableLoadMore(false);
                break;
            case R.id.pl_ll:
                tvDetail.setTextColor(getResources().getColor(R.color.tv_detail_uncheck_color));
                tvPl.setTextColor(getResources().getColor(R.color.tv_detail_color));
                tvDetail.setBackgroundResource(R.drawable.detail_line_uncheck);
                tvPl.setBackgroundResource(R.drawable.detail_line);
                webview.setVisibility(View.GONE);
                stateLayoutPl.setVisibility(View.VISIBLE);
                refreshLayout.setEnableLoadMore(true);
                pageindex = 1;
                commentArrayList.clear();
                getComment();
                break;
            case R.id.share:
                break;
        }
    }

    public void getProduct(String productid) {
        stateLayout.changeState(StateFrameLayout.LOADING);
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("productid", productid);
        formBody.add("provinceid", Global.provinceid);
        if (Global.loginResult != null) {
            formBody.add("userid", Global.loginResult.getId());
        }
        if (flag) {
            formBody.add("promotiontype", promotiontype);
            formBody.add("promotionvalue", product.getJifen());
        }
        Log.d("------参数------", formBody.build().toString());
//new call
        Call call = MyOkHttp.GetCall("product.getProductDetail", formBody);
//请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("------------", e.toString());
//                        myHandler.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                MyUtils.e("------获取产品详情结果------", result);
                Message message = new Message();
                message.what = 1;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    public void user() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        if (Global.loginResult == null) {
            startActivity(new Intent(mActivity, LoginActivity.class));
            finish();
            return;
        }
        formBody.add("userid", Global.loginResult.getId());

//new call
        Call call = MyOkHttp.GetCall("user.userHome", formBody);
//请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("------------", e.toString());
//                        myHandler.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                MyUtils.e("------获取用户信息------", result);
                Message message = new Message();
                message.what = 3;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }


    public void confirmOrder() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        if (Global.loginResult == null) {
            startActivity(new Intent(mActivity, LoginActivity.class));
            return;
        }
        formBody.add("userid", Global.loginResult.getId());
//        formBody.add("addressprovince", "浙江省");
        formBody.add("couponid", "");
//        formBody.add("cartids",cartids.substring(0,cartids.lastIndexOf("，")));
        formBody.add("productids", product.getId() + "|" + num);
        formBody.add("promotiontype", "JF");
        formBody.add("promotionvalue", product.getJifen());
        formBody.add("provinceid",Global.provinceid);
        Log.d("------参数------", formBody.build().toString());
//new call
        Call call = MyOkHttp.GetCall("order.confirmOrder", formBody);
//请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("------------", e.toString());
//                        myHandler.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                MyUtils.e("------提交订单结果------", result);
                Message message = new Message();
                message.what = 2;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    private OrderComfirm orderComfirm;
    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String result = bundle.getString("result");
            switch (msg.what) {

                case 1:
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            JSONObject jsonObject1 = new JSONObject(data);
                            product = gson.fromJson(jsonObject1.toString(), Product.class);
                            stateLayout.changeState(StateFrameLayout.SUCCESS);
                            setdata();
                        } else {
                            MyUtils.showToast(mActivity, message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    refreshLayout.finishRefresh();
                    break;
                case 2:
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            JSONObject jsonObject1 = new JSONObject(data);
                            orderComfirm = gson.fromJson(jsonObject1.toString(), OrderComfirm.class);
                            Intent intent = new Intent(mActivity, OrderConfirmActivity2.class);
                            intent.putExtra("orderComfirm", orderComfirm);
                            startActivity(intent);
                        } else {
                            MyUtils.showToast(mActivity, message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            JSONObject jsonObject1 = new JSONObject(data);
                            user = gson.fromJson(jsonObject1.toString(), User.class);
                            add.setText("我的积分：" + user.getJifen());
                        } else {
                            MyUtils.showToast(mActivity, message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 4://评论
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            JSONArray jsonArray = new JSONArray(data);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                commentArrayList.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), Comment.class));
                            }
                            if (commentArrayList.size() > 0) {
                                stateLayoutPl.changeState(StateFrameLayout.SUCCESS);
                            } else {
                                stateLayoutPl.changeState(StateFrameLayout.EMPTY);
                            }
                            CommentAdapter adapter = new CommentAdapter(mActivity, commentArrayList);
                            lvPl.setAdapter(adapter);
//                            MyUtils.showToast(mActivity, result);
                        } else {
                            stateLayoutPl.changeState(StateFrameLayout.EMPTY);
                            refreshLayout.finishLoadMore(true);
                            MyUtils.showToast(mActivity, message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    refreshLayout.finishLoadMore();
                    refreshLayout.finishRefresh();
                    break;

            }
        }
    };

}

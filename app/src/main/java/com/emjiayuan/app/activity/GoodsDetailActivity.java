package com.emjiayuan.app.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.GlideUtil;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.adapter.CommentAdapter;
import com.emjiayuan.app.banner.GlideImageLoader;
import com.emjiayuan.app.entity.Comment;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.entity.OrderComfirm;
import com.emjiayuan.app.entity.Product;
import com.emjiayuan.app.entity.SeckillBean;
import com.emjiayuan.app.widget.MyListView;
import com.google.gson.Gson;
import com.lwkandroid.stateframelayout.StateFrameLayout;
import com.qiyukf.unicorn.api.ConsultSource;
import com.qiyukf.unicorn.api.Unicorn;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
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

public class GoodsDetailActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.detail_ll)
    LinearLayout detail_ll;
    @BindView(R.id.pl_ll)
    LinearLayout pl_ll;
    @BindView(R.id.share)
    ImageView share;
    @BindView(R.id.detail_icon)
    ImageView detail_icon;
    @BindView(R.id.lv_pl)
    MyListView lv_pl;
    @BindView(R.id.tv_detail)
    TextView tv_detail;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_pl)
    TextView tv_pl;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.old_price)
    TextView old_price;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.kc)
    TextView kc;
    @BindView(R.id.ys)
    TextView ys;
    @BindView(R.id.cd)
    TextView cd;
    @BindView(R.id.zs)
    TextView zs;
    @BindView(R.id.add)
    TextView add;
    @BindView(R.id.buy)
    TextView buy;
    @BindView(R.id.kf_ll)
    LinearLayout kf_ll;
    @BindView(R.id.shoucang_ll)
    LinearLayout shoucang_ll;
    @BindView(R.id.shopping_car_ll)
    LinearLayout shopping_car_ll;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.line_top)
    View lineTop;
    @BindView(R.id.countdownview)
    CountdownView countdownview;
    @BindView(R.id.kill_bar_ll)
    LinearLayout killBarLl;
    @BindView(R.id.youhui)
    TextView youhui;
    @BindView(R.id.xl)
    TextView xl;
    @BindView(R.id.col_img)
    ImageView colImg;
    @BindView(R.id.bottom_ll)
    LinearLayout bottomLl;
    @BindView(R.id.remain)
    TextView remain;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.stateLayout)
    StateFrameLayout stateLayout;
    @BindView(R.id.kill_message)
    TextView killMessage;
    @BindView(R.id.service_ll)
    LinearLayout serviceLl;
    @BindView(R.id.stateLayout_pl)
    StateFrameLayout stateLayoutPl;
    @BindView(R.id.webview)
    WebView webview;
    private Product product;
    private PopupWindow popupWindow;
    private int num = 1;
    private int pageindex = 1;
    private ArrayList<Comment> commentArrayList = new ArrayList<>();
    private String productids = "";
    private String productid = "";
    private String promotiontype = "";
    private String promotionvalue = "";
    private boolean flag;
    private SeckillBean seckillBean;
    private Product.PromotioninfoBean bean;
    private long sjc;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_googs_detail;
    }

    @Override
    protected void initData() {
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setEnableHeaderTranslationContent(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getProduct(productid);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageindex++;
                getComment();
            }
        });
    }

    @Override
    protected void initView() {
        old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        old_price.getPaint().setAntiAlias(true);
        title.setText("商品详情");
        Intent intent = getIntent();
        flag = intent.getBooleanExtra("kill", false);
        if (flag) {
            killBarLl.setVisibility(View.VISIBLE);
            youhui.setVisibility(View.VISIBLE);
            xl.setVisibility(View.VISIBLE);
            zs.setVisibility(View.GONE);
            bottomLl.setVisibility(View.GONE);
//            seckillBean = (SeckillBean) intent.getSerializableExtra("seckill");
//            int position = intent.getIntExtra("position", -1);
            promotiontype = "MS";
            promotionvalue = intent.getStringExtra("promotionvalue");
            buy.setText("立即抢购");
            shopping_car_ll.setVisibility(View.GONE);
            add.setVisibility(View.GONE);
        } else {
            killBarLl.setVisibility(View.GONE);
            youhui.setVisibility(View.GONE);
            xl.setVisibility(View.GONE);
            zs.setVisibility(View.VISIBLE);
            bottomLl.setVisibility(View.VISIBLE);
            buy.setText("立即购买");
            shopping_car_ll.setVisibility(View.VISIBLE);
            add.setVisibility(View.VISIBLE);
        }
        productid = intent.getStringExtra("productid");
        getProduct(productid);
    }

    public void setdata() {
        if (flag) {
            bean = product.getPromotioninfo();
            price.setText("¥" + bean.getPriceX());
//            price.setText(Html.fromHtml("<small>¥ </small><big><big>"+bean.getPriceX().substring(0,bean.getPriceX().indexOf("."))+"</big></big><small>"+bean.getPriceX().substring(bean.getPriceX().indexOf("."),bean.getPriceX().length())+"</small>"));
            old_price.setText("¥" + product.getPreprice());
            double yh = Double.parseDouble(product.getPreprice()) - Double.parseDouble(bean.getPriceX());
            youhui.setText("优惠" + yh + "元");
            xl.setText("限量" + bean.getLimitnumX() + "件");
            remain.setText("剩" + bean.getStock() + "件");
            if ("0".equals(bean.getStock())){
                buy.setBackgroundColor(Color.parseColor("#9FA0A0"));
                buy.setEnabled(false);
            }else{
                buy.setBackgroundColor(Color.parseColor("#B02520"));
                buy.setEnabled(true);
            }
            if ("0".equals(product.getPromotioninfo().getStatus())) {
                killMessage.setText("距离开秒还有");
                buy.setText("即将开秒");
//                countdownview.setVisibility(View.VISIBLE);
            } else if ("1".equals(product.getPromotioninfo().getStatus())) {
                killMessage.setText("距离结束还有");
                buy.setText("立即抢购");
//                countdownview.setVisibility(View.VISIBLE);
            } else if ("2".equals(product.getPromotioninfo().getStatus())) {
                killMessage.setText("已结束");
                buy.setText("已结束");
//                countdownview.setVisibility(View.GONE);
            }
            countdownview.start(Long.parseLong(product.getPromotioninfo().getResiduetime()) * 1000);
            countdownview.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                @Override
                public void onEnd(CountdownView cv) {
                    if ("0".equals(product.getPromotioninfo().getStatus())) {
                        killMessage.setText("距离结束还有");
                        buy.setText("立即抢购");
                        countdownview.start((Long.parseLong(product.getPromotioninfo().getEndtime()) - Long.parseLong(product.getPromotioninfo().getStarttime())) * 1000);
                        product.getPromotioninfo().setStatus("1");
//                        countdownview.setVisibility(View.VISIBLE);
                    } else if ("1".equals(product.getPromotioninfo().getStatus())) {
                        killMessage.setText("已结束");
                        buy.setText("已结束");
//                        countdownview.setVisibility(View.GONE);
                        product.getPromotioninfo().setStatus("2");
                    }
                }
            });
        } else {
            price.setText("¥" + product.getPrice());
//            price.setText(Html.fromHtml("<small>¥ </small><big><big>"+product.getPrice().substring(0,product.getPrice().indexOf("."))+"</big></big><small>"+product.getPrice().substring(product.getPrice().indexOf("."),product.getPrice().length())+"</small>"));
            old_price.setText("¥" + product.getPreprice());
        }
        name.setText(product.getName());
        kc.setText("库存：" + product.getTotalnum());
        ys.setText("已售：" + product.getSellnum());
        cd.setText("产地：" + product.getSource());
        zs.setText("赠" + product.getJifen() + "积分");
        if ("1".equals(product.getIscollection())) {
            colImg.setSelected(true);
        } else {
            colImg.setSelected(false);
        }


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
//        detail_html.setText(Html.fromHtml(product.getContent(),new MImageGetter(detail_html,mActivity),null));
//        RequestOptions options = new RequestOptions()
//                .fitCenter();
//        Glide.with(this).load(product.getContent()).apply(options).into(detail_icon);
        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "\t<meta charset=\"utf-8\">\n" +
                "\t<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "\t\n" +
                "\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                "\n" +
                "\t<title>detail</title>\n" +
                "\n" +
                "\t<style>body{border:0;padding:0;margin:0;}img{border:0;display:block;vertical-align: middle;padding:0;margin:0;}p{border:0;padding:0;margin:0;}div{border:0;padding:0;margin:0;}</style>\n" +
                "</head>"
                + "<body>"
                + product.getContent() + "</body>" + "</html>";

        webview.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebChromeClient(new WebChromeClient());


    }

    @Override
    protected void setListener() {
        back.setOnClickListener(this);
        share.setOnClickListener(this);
        kf_ll.setOnClickListener(this);
        shoucang_ll.setOnClickListener(this);
        shopping_car_ll.setOnClickListener(this);
        add.setOnClickListener(this);
        buy.setOnClickListener(this);
        detail_ll.setOnClickListener(this);
        pl_ll.setOnClickListener(this);
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
            case R.id.kf_ll:
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
//                startActivity(new Intent(mActivity, KfActivity.class));
                break;
            case R.id.shoucang_ll:
                if (Global.loginResult == null) {
                    startActivity(new Intent(mActivity, LoginActivity.class));
                    return;
                }
                if (colImg.isSelected()) {
                    colImg.setSelected(false);
                    collection("user.removeCollection");
                } else {
                    colImg.setSelected(true);
                    collection("user.addCollection");
                }
                break;
            case R.id.shopping_car_ll:
                startActivity(new Intent(mActivity, ShoppingCarActivity.class));
                break;
            case R.id.add:
                showPopWindow(0);
                break;
            case R.id.buy:
                if (flag) {
                    if ("0".equals(product.getPromotioninfo().getStatus())) {
                        MyUtils.showToast(mActivity, "即将开秒");
                    } else if ("1".equals(product.getPromotioninfo().getStatus())) {
                        showPopWindow(1);
//                countdownview.setVisibility(View.VISIBLE);
                    } else if ("2".equals(product.getPromotioninfo().getStatus())) {
                        MyUtils.showToast(mActivity, "已结束");
                    }
                } else {
                    showPopWindow(1);
                }
                break;
            case R.id.detail_ll:
                tv_detail.setTextColor(getResources().getColor(R.color.tv_detail_color));
                tv_pl.setTextColor(getResources().getColor(R.color.tv_detail_uncheck_color));
                tv_detail.setBackgroundResource(R.drawable.detail_line);
                tv_pl.setBackgroundResource(R.drawable.detail_line_uncheck);
                stateLayoutPl.setVisibility(View.GONE);
                webview.setVisibility(View.VISIBLE);
                refreshLayout.setEnableLoadmore(false);
                break;
            case R.id.pl_ll:
                tv_detail.setTextColor(getResources().getColor(R.color.tv_detail_uncheck_color));
                tv_pl.setTextColor(getResources().getColor(R.color.tv_detail_color));
                tv_detail.setBackgroundResource(R.drawable.detail_line_uncheck);
                tv_pl.setBackgroundResource(R.drawable.detail_line);
                webview.setVisibility(View.GONE);
                stateLayoutPl.setVisibility(View.VISIBLE);
                pageindex = 1;
                commentArrayList.clear();
                getComment();
                break;
            case R.id.share:
                break;
        }
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

    public void addCar(String num) {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        if (Global.loginResult == null) {
            startActivity(new Intent(mActivity, LoginActivity.class));
            return;
        }
        formBody.add("userid", Global.loginResult.getId());
        formBody.add("productid", product.getId());
        formBody.add("option", "0");
        formBody.add("num", num);
        formBody.add("provinceid",Global.provinceid);
        Log.d("------参数------", formBody.build().toString());
//new call
        Call call = MyOkHttp.GetCall("cart.addOrUpdateCart", formBody);
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
                MyUtils.e("------加入购物车结果------", result);
                Message message = new Message();
                message.what = 0;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    public void collection(String method) {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        if (Global.loginResult == null) {
            startActivity(new Intent(mActivity, LoginActivity.class));
            return;
        }
        formBody.add("userid", Global.loginResult.getId());
        formBody.add("productid", product.getId());

        Log.d("------参数------", formBody.build().toString());
//new call
        Call call = MyOkHttp.GetCall(method, formBody);
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
                MyUtils.e("------收藏------", result);
                Message message = new Message();
                message.what = 3;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    public void getProduct(String productid) {
        stateLayout.changeState(StateFrameLayout.LOADING);
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("productid", productid);
        if (Global.loginResult != null) {
            formBody.add("userid", Global.loginResult.getId());
        }
        if (flag) {
            formBody.add("promotiontype", promotiontype);
            formBody.add("promotionvalue", promotionvalue);
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

    public void confirmOrder() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        if (Global.loginResult == null) {
            startActivity(new Intent(mActivity, LoginActivity.class));
            return;
        }
        formBody.add("userid", Global.loginResult.getId());
//        formBody.add("addressprovince", "浙江省");
        formBody.add("couponid", "");
        formBody.add("provinceid",Global.provinceid);
        if (flag) {
            formBody.add("promotiontype", promotiontype);
            formBody.add("promotionvalue", promotionvalue);
        }
//        formBody.add("cartids",cartids.substring(0,cartids.lastIndexOf("，")));
        formBody.add("productids", product.getId() + "|" + num);
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
                case 0:
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            MyUtils.showToast(mActivity, "已加入购物车！");
                            popupWindow.dismiss();
                        } else {
                            MyUtils.showToast(mActivity, message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            stateLayout.changeState(StateFrameLayout.SUCCESS);
                            JSONObject jsonObject1 = new JSONObject(data);
                            product = gson.fromJson(jsonObject1.toString(), Product.class);
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
                            Intent intent = new Intent(mActivity, OrderConfirmActivity.class);
                            intent.putExtra("orderComfirm", orderComfirm);
                            startActivity(intent);
                            popupWindow.dismiss();
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
                            MyUtils.showToast(mActivity, message);
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
                            lv_pl.setAdapter(adapter);
//                            MyUtils.showToast(mActivity, result);
                        } else {
                            stateLayoutPl.changeState(StateFrameLayout.EMPTY);
                            refreshLayout.setLoadmoreFinished(true);
                            MyUtils.showToast(mActivity, message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    refreshLayout.finishLoadmore();
                    refreshLayout.finishRefresh();
                    break;
            }
        }
    };

    public void showPopWindow(int type) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.choose_num, null);
        ImageView imageView = view.findViewById(R.id.icon);
        TextView name = view.findViewById(R.id.name);
        TextView price = view.findViewById(R.id.price);
        TextView preprice = view.findViewById(R.id.preprice);
//        ImageView imageView=view.findViewById(R.id.icon);
        TextView up = view.findViewById(R.id.up);
        TextView down = view.findViewById(R.id.down);
        TextView kc = view.findViewById(R.id.kc);
        TextView xg = view.findViewById(R.id.xg);
        xg.setVisibility(View.GONE);
        TextView next = view.findViewById(R.id.next);
        final EditText et_count = view.findViewById(R.id.et_count);
        GlideUtil.loadImageViewLoding(mActivity, product.getImages(), imageView, R.drawable.empty_img, R.drawable.empty_img);
        name.setText(product.getName());
        if (flag) {
            price.setText("¥" + bean.getPriceX());
            preprice.setText("原价¥" + product.getPreprice());
            kc.setText("剩余" + bean.getStock() + "件");
        } else {
            price.setText("¥" + product.getPrice());
            preprice.setText("原价¥" + product.getPreprice());
            kc.setText("剩余" + product.getTotalnum() + "件");
        }

        preprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        preprice.getPaint().setAntiAlias(true);

        num = 1;
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag) {
                    if (num < Integer.parseInt(bean.getLimitnumX())) {
                        et_count.setText("" + (++num));
                    } else {
                        et_count.setText("" + num);
                        MyUtils.showToast(mActivity, "限购" + bean.getLimitnumX() + "件");
                    }
                } else {
                    if (num < Integer.parseInt(product.getTotalnum())) {
                        et_count.setText("" + (++num));
                    } else {
                        et_count.setText("" + num);
                        MyUtils.showToast(mActivity, "不能大于库存！");
                    }
                }

            }
        });
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (num > 1) {
                    et_count.setText("" + (--num));
                } else {
                    MyUtils.showToast(mActivity, "不能小于1！");
                }
            }
        });
        switch (type) {
            case 0:
                next.setText("加入购物车");
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!MyUtils.isFastClick()) {
                            return;
                        }
                        addCar(Integer.toString(num));
                    }
                });
                break;
            case 1:
                if (flag) {
                    next.setText("立即抢购");
                } else {
                    next.setText("立即购买");
                }

                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!MyUtils.isFastClick()) {
                            return;
                        }
                        confirmOrder();
                    }
                });
                break;
        }

        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(R.style.popwindow_anim_style);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                MyUtils.setWindowAlpa(mActivity, false);
            }
        });
        if (popupWindow != null && !popupWindow.isShowing()) {
            popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        }
        MyUtils.setWindowAlpa(mActivity, true);
    }
}

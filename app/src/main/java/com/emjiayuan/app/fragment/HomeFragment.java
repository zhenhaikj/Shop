package com.emjiayuan.app.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.DownLoadManager;
import com.emjiayuan.app.Utils.GlideUtil;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.Utils.SpUtils;
import com.emjiayuan.app.activity.AllGoodsActivity;
import com.emjiayuan.app.activity.CityActivity;
import com.emjiayuan.app.activity.CouponGetActivity;
import com.emjiayuan.app.activity.GoodsDetailActivity;
import com.emjiayuan.app.activity.IntegralYlActivity;
import com.emjiayuan.app.activity.LoginActivity;
import com.emjiayuan.app.activity.LogisticsActivity;
import com.emjiayuan.app.activity.MessageActivity;
import com.emjiayuan.app.activity.MessageDetailActivity;
import com.emjiayuan.app.activity.NewArrivalsActivity;
import com.emjiayuan.app.activity.PopularActivity;
import com.emjiayuan.app.activity.SearchActivity;
import com.emjiayuan.app.activity.SecondsKillActivity2;
import com.emjiayuan.app.activity.SpecialActivity;
import com.emjiayuan.app.activity.TldzActivity;
import com.emjiayuan.app.activity.TlzqActivity;
import com.emjiayuan.app.activity.TopUpActivity;
import com.emjiayuan.app.adapter.HomeAdapter;
import com.emjiayuan.app.adapter.MenuAdapter;
import com.emjiayuan.app.adapter.TlzqAdapter;
import com.emjiayuan.app.adapter.XsgAdapter;
import com.emjiayuan.app.entity.BannerItem;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.entity.MenuItem;
import com.emjiayuan.app.entity.News;
import com.emjiayuan.app.entity.Product;
import com.emjiayuan.app.entity.Products;
import com.emjiayuan.app.entity.SeckillBean;
import com.emjiayuan.app.event.UpdateEvent;
import com.emjiayuan.app.imageloader.GlideImageLoader;
import com.emjiayuan.app.widget.HorizontalListView;
import com.emjiayuan.app.widget.MyGridView;
import com.emjiayuan.app.widget.MyListView;
import com.emjiayuan.app.widget.ObservableScrollView;
import com.google.gson.Gson;
import com.lwkandroid.stateframelayout.StateFrameLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.iwgang.countdownview.CountdownView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;


public class HomeFragment extends BaseLazyFragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    @BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.ll_search)
    LinearLayout ll_search;
    @BindView(R.id.city_name)
    TextView city_name;
    @BindView(R.id.city_ll)
    LinearLayout city_ll;
    @BindView(R.id.gv_menu)
    MyGridView gv_menu;
    @BindView(R.id.tv_message)
    TextSwitcher tv_message;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.countdownview)
    CountdownView countdownview;
    @BindView(R.id.gv_xsg)
    MyGridView gv_xsg;
    @BindView(R.id.txt_more)
    TextView txt_more;
    @BindView(R.id.xsh_more_ll)
    LinearLayout xsh_more_ll;
    @BindView(R.id.banner2)
    Banner banner2;
    @BindView(R.id.txt_tlzq)
    TextView txt_tlzq;
    @BindView(R.id.gv_tlzq)
    HorizontalListView gv_tlzq;
    @BindView(R.id.txt_specials)
    TextView txt_specials;
    @BindView(R.id.ymtt)
    ImageView ymtt;
    @BindView(R.id.type1)
    TextView type1;
    @BindView(R.id.name1)
    TextView name1;
    @BindView(R.id.price1)
    TextView price1;
    @BindView(R.id.img1)
    ImageView img1;
    @BindView(R.id.goods1_ll)
    LinearLayout goods1Ll;
    @BindView(R.id.type2)
    TextView type2;
    @BindView(R.id.name2)
    TextView name2;
    @BindView(R.id.price2)
    TextView price2;
    @BindView(R.id.img2)
    ImageView img2;
    @BindView(R.id.good2_ll)
    LinearLayout good2Ll;
    @BindView(R.id.type3)
    TextView type3;
    @BindView(R.id.name3)
    TextView name3;
    @BindView(R.id.price3)
    TextView price3;
    @BindView(R.id.img3)
    ImageView img3;
    @BindView(R.id.goods3_ll)
    LinearLayout goods3Ll;
    @BindView(R.id.lv_home)
    MyListView lv_home;
    @BindView(R.id.stateLayout)
    StateFrameLayout stateLayout;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.kill_message)
    TextView kill_message;
    @BindView(R.id.banner_top)
    Banner bannerTop;

    @BindView(R.id.no_kill_img)
    ImageView noKillImg;
    @BindView(R.id.tl_more_ll)
    LinearLayout tlMoreLl;
    @BindView(R.id.tejia_more_ll)
    LinearLayout tejiaMoreLl;
    @BindView(R.id.sv)
    ObservableScrollView sv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.hyj1)
    TextView hyj1;
    @BindView(R.id.hyj2)
    TextView hyj2;
    @BindView(R.id.hyj3)
    TextView hyj3;
    private MenuAdapter adapter;
    private Product product1;
    private Product product2;
    private Product product3;

    private ArrayList<MenuItem> list;
    private ArrayList<String> images_top = new ArrayList<>();
    private ArrayList<String> images_mid = new ArrayList<>();
    private ArrayList<String> images_bot = new ArrayList<>();
    private ArrayList<BannerItem> banner_top = new ArrayList<>();
    private ArrayList<BannerItem> banner_mid = new ArrayList<>();
    private ArrayList<BannerItem> banner_bot = new ArrayList<>();
    private ArrayList<Product> souplist;
    private SeckillBean seckillBean;
    private Long sjc;

    private ArrayList<News> news;
    Thread thread;
    private int index = 0;//textview上下滚动下标
    public static final int NEWS_MESSAGE_TEXTVIEW = 100;//通知公告信息
    private static final int BAIDU_ACCESS_FINE_LOCATION = 100;//定位申请跳转
    private String provincecode = "";
    private String latitude = "";
    private String longtitude = "";
    private MyLocationListenner myListener;
    private LocationClient mLocClient;
    private String soupname = "";//汤料专区名称
    private boolean flag = true;
    private AlertDialog dialog;
    private Drawable drawable; // 顶部渐变布局需设置的Drawable
    private int fadingHeight = 600; // 当ScrollView滑动到什么位置时渐变消失（根据需要进行调整）
    private static final int START_ALPHA = 0;//scrollview滑动开始位置
    private static final int END_ALPHA = 255;//scrollview滑动结束位置


    @Override
    protected int setLayoutId() {
        return R.layout.fragment_home;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initView() {
        super.initView();
        //设置图片加载器
        bannerTop.setImageLoader(new GlideImageLoader());
        banner.setImageLoader(new GlideImageLoader());
        banner2.setImageLoader(new GlideImageLoader());
        bannerTop.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                BannerItem item = banner_top.get(position);
                if ("1".equals(item.getType())) {
                    Intent intent = new Intent(mActivity, GoodsDetailActivity.class);
                    intent.putExtra("productid", item.getLinkid());
                    startActivity(intent);
                } else if ("2".equals(item.getType())) {
                    Intent intent = new Intent(mActivity, MessageDetailActivity.class);
                    intent.putExtra("newsid", item.getLinkid());
                    startActivity(intent);
                } else if ("3".equals(item.getType())) {
                    Intent intent = new Intent(mActivity, SecondsKillActivity2.class);
                    intent.putExtra("seckillid", item.getLinkid());
                    startActivity(intent);
                }
            }
        });
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                BannerItem item = banner_mid.get(position);
                if ("1".equals(item.getType())) {
                    Intent intent = new Intent(mActivity, GoodsDetailActivity.class);
                    intent.putExtra("productid", item.getLinkid());
                    startActivity(intent);
                } else if ("2".equals(item.getType())) {
                    Intent intent = new Intent(mActivity, MessageDetailActivity.class);
                    intent.putExtra("newsid", item.getLinkid());
                    startActivity(intent);
                } else if ("3".equals(item.getType())) {
                    Intent intent = new Intent(mActivity, SecondsKillActivity2.class);
                    intent.putExtra("seckillid", item.getLinkid());
                    startActivity(intent);
                }
            }
        });
        banner2.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                BannerItem item = banner_bot.get(position);
                if ("1".equals(item.getType())) {
                    Intent intent = new Intent(mActivity, GoodsDetailActivity.class);
                    intent.putExtra("productid", item.getLinkid());
                    startActivity(intent);
                } else if ("2".equals(item.getType())) {
                    Intent intent = new Intent(mActivity, MessageDetailActivity.class);
                    intent.putExtra("newsid", item.getLinkid());
                    startActivity(intent);
                } else if ("3".equals(item.getType())) {
                    Intent intent = new Intent(mActivity, SecondsKillActivity2.class);
                    intent.putExtra("seckillid", item.getLinkid());
                    startActivity(intent);
                }
            }
        });
        requestPermission();
        initMapLocation();
        sv.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int x, int y, int i2, int i3) {
                if (y > fadingHeight) {
                    y = fadingHeight; // 当滑动到指定位置之后设置颜色为纯色，之前的话要渐变---实现下面的公式即可

//                relativela_id.setBackgroundColor(Color.WHITE);
                } else if (y < 0) {
                    y = 0;
                } else {
//                relativela_id.setBackgroundColor(0x99FFFFFF);
                }
                toolbar.getBackground().setAlpha(y * (END_ALPHA - START_ALPHA) / fadingHeight
                        + START_ALPHA);
//                drawable.setAlpha(y * (END_ALPHA - START_ALPHA) / fadingHeight
//                        + START_ALPHA);
            }
        });
//        drawable = getResources().get;
//        drawable.setAlpha(START_ALPHA);
        if (Global.appTheme != null) {
            toolbar.setBackgroundColor(Color.parseColor(Global.appTheme.getHome_top_color() != null ? Global.appTheme.getHome_top_color() : "#1f7f4b"));
        } else {
            toolbar.setBackgroundColor(Color.parseColor("#1f7f4b"));
        }
        toolbar.getBackground().setAlpha(START_ALPHA);
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(R.id.toolbar)
                .fitsSystemWindows(false)
                .init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initData() {


//        setUnderLine();
//        request();
        //设置网络错误重试监听【不传netRetryId的话需要在对应布局中设置触发控件的id为android:id="@id/id_sfl_net_error_retry"】
        stateLayout.setOnNetErrorRetryListener(new StateFrameLayout.OnNetErrorRetryListener() {
            @Override
            public void onNetErrorRetry() {
                request();
            }
        });
        getAppVersion();
        getFloatBanner();
    }

    @Override
    protected void setListener() {
        tejiaMoreLl.setOnClickListener(this);
        tlMoreLl.setOnClickListener(this);
        xsh_more_ll.setOnClickListener(this);
        et_search.setOnClickListener(this);
//        txt_more.setOnClickListener(this);
//        txt_specials.setOnClickListener(this);
//        txt_tlzq.setOnClickListener(this);
        city_ll.setOnClickListener(this);
        tv_message.setOnClickListener(this);
        gv_xsg.setOnItemClickListener(this);
        gv_tlzq.setOnItemClickListener(this);
        gv_menu.setOnItemClickListener(this);
        ymtt.setOnClickListener(this);
        goods1Ll.setOnClickListener(this);
        good2Ll.setOnClickListener(this);
        goods3Ll.setOnClickListener(this);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                flag = false;
                request();

            }
        });
        refreshLayout.setOnMultiPurposeListener(new OnMultiPurposeListener() {
            @Override
            public void onHeaderPulling(RefreshHeader header, float percent, int offset, int headerHeight, int extendHeight) {
//                toolbar.setVisibility(View.GONE);
            }

            @Override
            public void onHeaderReleased(RefreshHeader header, int headerHeight, int extendHeight) {

            }

            @Override
            public void onHeaderReleasing(RefreshHeader header, float percent, int offset, int headerHeight, int extendHeight) {
//                toolbar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onHeaderStartAnimator(RefreshHeader header, int headerHeight, int extendHeight) {
//                toolbar.setVisibility(View.GONE);
            }

            @Override
            public void onHeaderFinish(RefreshHeader header, boolean success) {

            }


            @Override
            public void onFooterPulling(RefreshFooter footer, float percent, int offset, int footerHeight, int extendHeight) {

            }

            @Override
            public void onFooterReleased(RefreshFooter footer, int footerHeight, int extendHeight) {

            }

            @Override
            public void onFooterReleasing(RefreshFooter footer, float percent, int offset, int footerHeight, int extendHeight) {

            }

            @Override
            public void onFooterStartAnimator(RefreshFooter footer, int footerHeight, int extendHeight) {

            }

            @Override
            public void onFooterFinish(RefreshFooter footer, boolean success) {

            }


            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {

            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

            }

            @Override
            public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
                Log.d("RefreshState", newState.name());
                if (refreshLayout.getState() == RefreshState.PullDownToRefresh) {
                    toolbar.setVisibility(View.GONE);
                }
                if (refreshLayout.getState() == RefreshState.PullDownCanceled) {
                    toolbar.setVisibility(View.VISIBLE);
                }
                if (refreshLayout.getState() == RefreshState.Refreshing) {
                    toolbar.setVisibility(View.GONE);
                }
                if (refreshLayout.getState() == RefreshState.None) {
                    toolbar.setVisibility(View.VISIBLE);
                }
                if (refreshLayout.getState() == RefreshState.ReleaseToRefresh) {
                    toolbar.setVisibility(View.GONE);
                }
                if (refreshLayout.getState() == RefreshState.RefreshFinish) {
                    toolbar.setVisibility(View.VISIBLE);
                }
            }
        });
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setEnableHeaderTranslationContent(false);
//        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(RefreshLayout refreshlayout) {
//                refreshlayout.finishLoadMore(2000);
//            }
//        });
    }

    public void request() {
        if (!checkNetwork()) {
            stateLayout.changeState(StateFrameLayout.NET_ERROR);
            return;
        }
        if (flag) {
            stateLayout.changeState(StateFrameLayout.LOADING);
            flag = false;
        }
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        Log.d("------参数------", formBody.build().toString());
        if (!"".equals(provincecode) && provincecode != null) {
            formBody.add("provinceid", Global.provinceid);
        }
//new call
        Call call = MyOkHttp.GetCall("public.appHome", formBody);
//        Call call = MyOkHttp.GetCall("public.getAddress", formBody);
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
                MyUtils.e("------获取首页结果------", provincecode + "|" + result);
                Message message = new Message();
                message.what = 0;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    public void getAppVersion() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("versionno", Integer.toString(MyUtils.getAppVersionCode(mActivity)));
//new call
        Call call = MyOkHttp.GetCall("system.getAppVersion", formBody);
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
                MyUtils.e("------检查更新------", result);
                Message message = new Message();
                message.what = 2;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    public void getFloatBanner() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
//new call
        Call call = MyOkHttp.GetCall("public.getFloatBanner", formBody);
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
                MyUtils.e("------浮窗广告------", result);
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
     * 设置下划线
     */
    public void setUnderLine() {
        txt_more.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        txt_specials.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        txt_tlzq.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        txt_more.getPaint().setAntiAlias(true);
        txt_specials.getPaint().setAntiAlias(true);
        txt_tlzq.getPaint().setAntiAlias(true);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (MyUtils.isFastClick()) {
            Intent intent = null;
            switch (adapterView.getId()) {
                case R.id.gv_menu:
                    switch (i) {
                        case 0:
                            startActivity(new Intent(getActivity(), AllGoodsActivity.class));
                            break;
                        case 1:
                            startActivity(new Intent(getActivity(), NewArrivalsActivity.class));
                            break;
                        case 2:
                            startActivity(new Intent(getActivity(), PopularActivity.class));
                            break;
                        case 3:
                            startActivity(new Intent(getActivity(), TldzActivity.class));
                            break;
                        case 4:
                            startActivity(new Intent(getActivity(), CouponGetActivity.class));
                            break;
                        case 5:
                            if (Global.loginResult==null){
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                return;
                            }
                            startActivity(new Intent(getActivity(), TopUpActivity.class));
                            break;
                        case 6:
                            startActivity(new Intent(getActivity(), IntegralYlActivity.class));
                            break;
                        case 7:
                            startActivity(new Intent(getActivity(), LogisticsActivity.class));
                            break;
                    }
                    break;
                case R.id.gv_xsg:
                    intent = new Intent(getActivity(), GoodsDetailActivity.class);
                    intent.putExtra("productid", seckillBean.getProduct_list().get(i).getProductid());
                    intent.putExtra("kill", true);
                    intent.putExtra("promotionvalue", seckillBean.getId());
//                    intent.putExtra("position",i);
                    startActivity(intent);
                    break;
                case R.id.gv_tlzq:
                    intent = new Intent(getActivity(), GoodsDetailActivity.class);
                    intent.putExtra("productid", souplist.get(i).getId());
                    startActivity(intent);
                    break;

            }
        }

    }

    @Override
    public void onClick(View view) {
        if (MyUtils.isFastClick()) {
            Intent intent;
            switch (view.getId()) {
                case R.id.et_search:
                    startActivity(new Intent(getActivity(), SearchActivity.class));
                    break;
                case R.id.goods1_ll:
                    if (product1 == null) {
                        return;
                    }
                    intent = new Intent(getActivity(), GoodsDetailActivity.class);
                    intent.putExtra("productid", product1.getId());
                    startActivity(intent);
                    break;
                case R.id.good2_ll:
                    if (product2 == null) {
                        return;
                    }
                    intent = new Intent(getActivity(), GoodsDetailActivity.class);
                    intent.putExtra("productid", product2.getId());
                    startActivity(intent);
                    break;
                case R.id.goods3_ll:
                    if (product3 == null) {
                        return;
                    }
                    intent = new Intent(getActivity(), GoodsDetailActivity.class);
                    intent.putExtra("productid", product3.getId());
                    startActivity(intent);
                    break;
                case R.id.ymtt:
                    intent = new Intent(getActivity(), MessageActivity.class);
//                    intent.putExtra("news", news);
                    startActivity(intent);
                    break;
                case R.id.tv_message:
                    intent = new Intent(mActivity, MessageDetailActivity.class);
                    intent.putExtra("newsid", news.get(index % news.size()).getId());
//                    intent.putExtra("news", news);
                    startActivity(intent);
                    break;
                case R.id.xsh_more_ll://秒杀专区
//                    if ("null".equals(seckilldata)) {
//                        MyUtils.showToast(mActivity, "活动已结束！");
//                        return;
//                    }
                    intent = new Intent(getActivity(), SecondsKillActivity2.class);
                    intent.putExtra("seckillid",seckillBean.getId());
                    startActivity(intent);
                    break;
                case R.id.tejia_more_ll://今日特价
                    intent = new Intent(getActivity(), SpecialActivity.class);
//                intent.putExtra("product",Global.list.get(i));
                    startActivity(intent);
                    break;
                case R.id.tl_more_ll://汤料专区
                    intent = new Intent(getActivity(), TlzqActivity.class);
                    intent.putExtra("soupid", soupid);
                    intent.putExtra("soupname", soupname);
                    startActivity(intent);
                    break;
                case R.id.city_ll://定位
                    intent = new Intent(getActivity(), CityActivity.class);
//                    intent = new Intent(getActivity(), FundArealistActivity.class);
                    startActivityForResult(intent, 1);
                    break;
            }
        }

    }


    private String soupid = "";
    private String seckilldata;
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
                            stateLayout.changeState(StateFrameLayout.SUCCESS);
                            JSONObject dataJson = new JSONObject(data);
                            JSONArray iconArray = dataJson.getJSONArray("icon");
                            JSONArray bannerArray = dataJson.getJSONArray("banner");
                            JSONArray newsArray = dataJson.getJSONArray("news");
                            JSONArray recommend = dataJson.getJSONArray("recommend");
                            JSONArray product = dataJson.getJSONArray("product");
                            JSONObject soup = dataJson.getJSONObject("soup");
                            if (dataJson.has("floatbanner")) {
                                /*final BannerItem bannerItem=gson.fromJson(dataJson.getString("floatbanner"),BannerItem.class);
                                View view= LayoutInflater.from(mActivity).inflate(R.layout.layout_gg_dialog,null);
                                LinearLayout gg_ll=view.findViewById(R.id.gg_ll);
                                ImageView close=view.findViewById(R.id.close);
                                ImageView icon=view.findViewById(R.id.icon);
                                GlideUtil.loadImageView(mActivity,bannerItem.getImage(),icon);
                                DisplayMetrics dm = getResources().getDisplayMetrics();
//        heigth = dm.heightPixels;
//        width = dm.widthPixels;
                                icon.setLayoutParams(new LinearLayout.LayoutParams(dm.widthPixels/3*2,dm.widthPixels));
                                dialog=new AlertDialog.Builder(mActivity).setView(view).create();
                                dialog.setCancelable(false);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                gg_ll.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if ("1".equals(bannerItem.getType())){
                                            Intent intent=new Intent(mActivity,GoodsDetailActivity.class);
                                            intent.putExtra("productid",bannerItem.getLinkid());
                                            startActivity(intent);
                                        }else if ("2".equals(bannerItem.getType())){
                                            Intent intent=new Intent(mActivity,MessageDetailActivity.class);
                                            intent.putExtra("newsid",bannerItem.getLinkid());
                                            startActivity(intent);
                                        }else if ("3".equals(bannerItem.getType())){
                                            Intent intent=new Intent(mActivity,SecondsKillActivity2.class);
                                            startActivity(intent);
                                        }
                                        dialog.dismiss();
                                    }
                                });
                                close.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();*/
                            }
                            seckilldata = dataJson.getString("seckill");
                            if (!"null".equals(seckilldata)) {
                                gv_xsg.setVisibility(View.VISIBLE);
                                noKillImg.setVisibility(View.GONE);
                                JSONObject seckill = dataJson.getJSONObject("seckill");
                                //秒杀
                                seckillBean = gson.fromJson(seckill.toString(), SeckillBean.class);
                                if ("0".equals(seckillBean.getStatus())) {
                                    kill_message.setText("距开始");
//                                countdownview.setVisibility(View.VISIBLE);
                                } else if ("1".equals(seckillBean.getStatus())) {
                                    kill_message.setText("距结束");
//                                countdownview.setVisibility(View.VISIBLE);
                                } else if ("2".equals(seckillBean.getStatus())) {
                                    kill_message.setText("已结束");
                                    gv_xsg.setVisibility(View.GONE);
                                    noKillImg.setVisibility(View.VISIBLE);
                                    seckilldata = "null";
//                                countdownview.setVisibility(View.GONE);
                                }
                                MyUtils.e("时间差", MyUtils.getTimeDifference(System.currentTimeMillis() / 1000, Long.parseLong(seckillBean.getEndtime())) + "ms");
//                            countdownview.start(86400000);
//                            countdownview.start(MyUtils.getTimeDifference(System.currentTimeMillis()/1000,Long.parseLong(seckillBean.getEndtime())));
//                            countdownview.start(sjc*1000);
                                countdownview.start(Long.parseLong(seckillBean.getResiduetime()) * 1000);
                                countdownview.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                                    @Override
                                    public void onEnd(CountdownView cv) {
                                        if ("0".equals(seckillBean.getStatus())) {
                                            kill_message.setText("距结束");
                                            countdownview.start((Long.parseLong(seckillBean.getEndtime()) - Long.parseLong(seckillBean.getStarttime())) * 1000);
                                            seckillBean.setStatus("1");
//                                        countdownview.setVisibility(View.VISIBLE);
                                        } else if ("1".equals(seckillBean.getStatus())) {
                                            kill_message.setText("已结束");
//                                        countdownview.setVisibility(View.GONE);
                                            seckillBean.setStatus("2");
                                            gv_xsg.setVisibility(View.GONE);
                                            noKillImg.setVisibility(View.VISIBLE);
                                            seckilldata = "null";
                                        }
                                        request();
                                    }
                                });
                                XsgAdapter xsgAdapter = new XsgAdapter(mActivity, seckillBean);
                                gv_xsg.setAdapter(xsgAdapter);
                            } else {
                                gv_xsg.setVisibility(View.GONE);
                                noKillImg.setVisibility(View.VISIBLE);
                            }

                            //菜单
                            list = new ArrayList<>();
                            images_top.clear();
                            images_mid.clear();
                            images_bot.clear();
                            banner_top.clear();
                            banner_mid.clear();
                            banner_bot.clear();
                            for (int i = 0; i < iconArray.length(); i++) {
                                MenuItem menuItem = gson.fromJson(iconArray.getJSONObject(i).toString(), MenuItem.class);
                                list.add(menuItem);
                            }
                            adapter = new MenuAdapter(mActivity, list);
                            gv_menu.setAdapter(adapter);

                            //设置图片集合
                            List images = new ArrayList();
                            for (int i = 0; i < bannerArray.length(); i++) {
                                BannerItem bannerItem = gson.fromJson(bannerArray.getJSONObject(i).toString(), BannerItem.class);
                                images.add(bannerItem.getImage());
                                if ("1".equals(bannerItem.getLocaltion())) {
                                    images_top.add(bannerItem.getImage());
                                    banner_top.add(bannerItem);
                                } else if ("2".equals(bannerItem.getLocaltion())) {
                                    images_mid.add(bannerItem.getImage());
                                    banner_mid.add(bannerItem);
                                } else if ("3".equals(bannerItem.getLocaltion())) {
                                    images_bot.add(bannerItem.getImage());
                                    banner_bot.add(bannerItem);
                                }
                            }
                            Global.images = images;
                            bannerTop.setImages(images_top);
                            //banner设置方法全部调用完毕时最后调用
                            bannerTop.start();
                            banner.setImages(images_mid);
                            //banner设置方法全部调用完毕时最后调用
                            banner.start();
                            banner2.setImages(images_bot);
                            //banner设置方法全部调用完毕时最后调用
                            banner2.start();
                            ArrayList<Product> list = new ArrayList<>();
                            news = new ArrayList<News>();
                            ArrayList<Products> Productslist = new ArrayList<>();
                            for (int i = 0; i < product.length(); i++) {
                                JSONObject jsonObject1 = product.getJSONObject(i);
                                Productslist.add(gson.fromJson(jsonObject1.toString(), Products.class));
                                JSONArray product_list = jsonObject1.getJSONArray("product_list");
                                for (int j = 0; j < product_list.length(); j++) {
                                    list.add(gson.fromJson(product_list.getJSONObject(j).toString(), Product.class));
                                }
                            }

                            for (int i = 0; i < newsArray.length(); i++) {
                                news.add(gson.fromJson(newsArray.getJSONObject(i).toString(), News.class));
                            }
                            newsMessage();
                            Global.list = list;
                            Global.news = news;
                            Global.Productslist = Productslist;
                            ArrayList<Product> recommendlist = getProductList(recommend);
                            if (recommendlist.size() == 0) {
                                goods1Ll.setVisibility(View.INVISIBLE);
                                good2Ll.setVisibility(View.INVISIBLE);
                                goods3Ll.setVisibility(View.INVISIBLE);
                            }
                            if (recommendlist.size() == 1) {
                                goods1Ll.setVisibility(View.VISIBLE);
                                good2Ll.setVisibility(View.INVISIBLE);
                                goods3Ll.setVisibility(View.INVISIBLE);
                                product1 = recommendlist.get(0);
                                hyj1.setText("会员价¥"+product1.getMinprice()+"起");
                                price1.setText(Html.fromHtml("<small>¥ </small><big><b>" + product1.getPrice().substring(0, product1.getPrice().indexOf(".")) + "</b></big><small><b>" + product1.getPrice().substring(product1.getPrice().indexOf("."), product1.getPrice().length()) + "</b></small>"));
                                GlideUtil.loadImageViewLoding(mActivity, product1.getImages(), img1, R.drawable.empty_img, R.drawable.empty_img);
                            }
                            if (recommendlist.size() == 2) {
                                goods1Ll.setVisibility(View.VISIBLE);
                                good2Ll.setVisibility(View.VISIBLE);
                                goods3Ll.setVisibility(View.INVISIBLE);
                                product1 = recommendlist.get(0);
                                name1.setText(product1.getName());
                                hyj1.setText("会员价¥"+product1.getMinprice()+"起");
                                price1.setText(Html.fromHtml("<small>¥ </small><big><b>" + product1.getPrice().substring(0, product1.getPrice().indexOf(".")) + "</b></big><small><b>" + product1.getPrice().substring(product1.getPrice().indexOf("."), product1.getPrice().length()) + "</b></small>"));
                                GlideUtil.loadImageViewLoding(mActivity, product1.getImages(), img1, R.drawable.empty_img, R.drawable.empty_img);
                                product2 = recommendlist.get(1);
                                name2.setText(product2.getName());
                                hyj2.setText("会员价¥"+product2.getMinprice()+"起");
                                price2.setText(Html.fromHtml("<small>¥ </small><big><b>" + product2.getPrice().substring(0, product2.getPrice().indexOf(".")) + "</b></big><small><b>" + product2.getPrice().substring(product2.getPrice().indexOf("."), product2.getPrice().length()) + "</b></small>"));
                                GlideUtil.loadImageViewLoding(mActivity, product2.getImages(), img2, R.drawable.empty_img, R.drawable.empty_img);
                            }
                            if (recommendlist.size() == 3) {
                                goods1Ll.setVisibility(View.VISIBLE);
                                good2Ll.setVisibility(View.VISIBLE);
                                goods3Ll.setVisibility(View.VISIBLE);
                                product1 = recommendlist.get(0);
                                name1.setText(product1.getName());
                                hyj1.setText("会员价¥"+product1.getMinprice()+"起");
                                price1.setText(Html.fromHtml("<small>¥ </small><big><b>" + product1.getPrice().substring(0, product1.getPrice().indexOf(".")) + "</b></big><small><b>" + product1.getPrice().substring(product1.getPrice().indexOf("."), product1.getPrice().length()) + "</b></small>"));
                                GlideUtil.loadImageViewLoding(mActivity, product1.getImages(), img1, R.drawable.empty_img, R.drawable.empty_img);
                                product2 = recommendlist.get(1);
                                name2.setText(product2.getName());
                                hyj2.setText("会员价¥"+product2.getMinprice()+"起");
                                price2.setText(Html.fromHtml("<small>¥ </small><big><b>" + product2.getPrice().substring(0, product2.getPrice().indexOf(".")) + "</b></big><small><b>" + product2.getPrice().substring(product2.getPrice().indexOf("."), product2.getPrice().length()) + "</b></small>"));
                                GlideUtil.loadImageViewLoding(mActivity, product2.getImages(), img2, R.drawable.empty_img, R.drawable.empty_img);
                                product3 = recommendlist.get(2);
                                name3.setText(product3.getName());
                                hyj3.setText("会员价¥"+product3.getMinprice()+"起");
                                price3.setText(Html.fromHtml("<small>¥ </small><big><b>" + product3.getPrice().substring(0, product3.getPrice().indexOf(".")) + "</b></big><small><b>" + product3.getPrice().substring(product3.getPrice().indexOf("."), product3.getPrice().length()) + "</b></small>"));
                                GlideUtil.loadImageViewLoding(mActivity, product3.getImages(), img3, R.drawable.empty_img, R.drawable.empty_img);
                            }


                            //汤料
                            soupname = soup.getString("name");
                            txt_tlzq.setText(soupname);
                            soupid = soup.getString("id");
                            souplist = getProductList(soup.getJSONArray("list"));
//                            souplist.addAll(souplist);
//                            souplist.addAll(souplist);
//                            souplist.addAll(souplist);
//                            souplist.addAll(souplist);
//                            souplist.addAll(souplist);
                            TlzqAdapter tlzqAdapter = new TlzqAdapter(getActivity(), souplist);
                            gv_tlzq.setAdapter(tlzqAdapter);
                            HomeAdapter homeAdapter = new HomeAdapter(getActivity(), Productslist);
                            lv_home.setAdapter(homeAdapter);
//                            MyUtils.showToast(getActivity(), message);
                        } else {
                            MyUtils.showToast(getActivity(), message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    refreshLayout.finishRefresh();
                    break;
                case 1:
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        if ("200".equals(code)) {
                            JSONObject object = new JSONObject(data);
                            provincecode = object.getString("id");
                            Global.provinceid = provincecode;
                            request();
                        } else {
//                            MyUtils.showToast(mActivity, message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case NEWS_MESSAGE_TEXTVIEW:
                    index++;
                    if (tv_message != null) {
                        tv_message.setText(news.get(index % news.size()).getTitle());
                        if (index == news.size()) {
                            index = 0;
                        }
                    }

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

                            String is_update = jsonObject1.getString("is_update");
                            final String app_download_url = jsonObject1.getString("download_url");
//                            String appcode = "234";
//                            final String app_download_url = "http://pz0.3dn.mse.sogou.com/semob5.13.5_154189_R14189_121106002_build47127_2.1.0.2133.apk";
                            if ("1".equals(is_update)) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                                builder.setTitle("更新");
                                builder.setMessage("检测到有更新,是否立刻更新？");
                                builder.setNegativeButton("稍后更新", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                builder.setPositiveButton("立刻更新", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (MyUtils.isWifi(mActivity)) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                                            builder.setTitle("提示");
                                            builder.setMessage("您当前正在使用移动网络，继续下载将消耗流量");
                                            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            });
                                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    downLoadApk(app_download_url);
                                                }
                                            });
                                            builder.create().show();
                                        } else {
                                            downLoadApk(app_download_url);
                                        }
                                    }
                                });
                                builder.create().show();
                            } else {
//                                MyUtils.showToast(mActivity, "当前已是最新版本");
                            }


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
                            final BannerItem bannerItem = gson.fromJson(data, BannerItem.class);
                            View view = LayoutInflater.from(mActivity).inflate(R.layout.layout_gg_dialog, null);
                            LinearLayout gg_ll = view.findViewById(R.id.gg_ll);
                            ImageView close = view.findViewById(R.id.close);
                            ImageView icon = view.findViewById(R.id.icon);
                            GlideUtil.loadImageView(mActivity, bannerItem.getImage(), icon);
                            DisplayMetrics dm = getResources().getDisplayMetrics();
//        heigth = dm.heightPixels;
//        width = dm.widthPixels;
                            icon.setLayoutParams(new LinearLayout.LayoutParams(dm.widthPixels / 3 * 2, dm.widthPixels));
                            dialog = new AlertDialog.Builder(mActivity).setView(view).create();
                            dialog.setCancelable(false);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            gg_ll.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    SpUtils.putString(mActivity,"bannerid",bannerItem.getId());
                                    if ("1".equals(bannerItem.getType())) {
                                        Intent intent = new Intent(mActivity, GoodsDetailActivity.class);
                                        intent.putExtra("productid", bannerItem.getLinkid());
                                        startActivity(intent);
                                    } else if ("2".equals(bannerItem.getType())) {
                                        Intent intent = new Intent(mActivity, MessageDetailActivity.class);
                                        intent.putExtra("newsid", bannerItem.getLinkid());
                                        startActivity(intent);
                                    } else if ("3".equals(bannerItem.getType())) {
                                        Intent intent = new Intent(mActivity, SecondsKillActivity2.class);
                                        intent.putExtra("seckillid", bannerItem.getLinkid());
                                        startActivity(intent);
                                    }
                                    dialog.dismiss();
                                }
                            });
                            close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            if(!SpUtils.getString(mActivity,"bannerid").equals(bannerItem.getId())){
                                dialog.show();
                            }
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

    public void getProviceid() {

        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体

        formBody.add("latitude", latitude);//传递键值对参数
        formBody.add("longitude", longtitude);//传递键值对参数
//new call
        Call call = MyOkHttp.GetCall("public.getLocation", formBody);
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
                Log.d("------获取省份结果------", result);
                Message message = new Message();
                message.what = 1;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    public ArrayList<Product> getProductList(JSONArray jsonArray) {
        ArrayList<Product> list = new ArrayList<>();
        Gson gson = new Gson();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                Product product = gson.fromJson(jsonArray.getJSONObject(i).toString(), Product.class);
                list.add(product);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void newsMessage() {
        tv_message.removeAllViews();
        tv_message.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(getActivity());
                textView.setSingleLine();
                textView.setTextSize(12);//字号
                textView.setTextColor(Color.parseColor("#000000"));
                textView.setEllipsize(TextUtils.TruncateAt.END);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.CENTER_VERTICAL;
                textView.setLayoutParams(params);
                return textView;
            }
        });
        if (thread == null) {
            thread = new Thread() {
                @Override
                public void run() {
                    while (index < news.size()) {
                        synchronized (this) {
                            SystemClock.sleep(4000);//每隔4秒滚动一次
                            myHandler.sendEmptyMessage(NEWS_MESSAGE_TEXTVIEW);
                        }
                    }
                }
            };
            thread.start();
        } else {
//            thread.start();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            city_name.setText(data.getStringExtra("name"));
            provincecode = data.getStringExtra("provincecode");
            Global.provinceid = provincecode;
            request();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(UpdateEvent event) {

    }

    //定位相关:初始化定位
    private void initMapLocation() {

        // 定位初始化
        myListener = new MyLocationListenner();
        mLocClient = new LocationClient(mActivity);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);//自动定位间隔
        mLocClient.setLocOption(option);
        mLocClient.start();

    }

//    @Override
//    public void onScroll(int l, int t) {
//        int newAlpha = t/500;
//        drawable.setAlpha(newAlpha);
//    }

    /**
     * 定位相关:定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {//得到未知信息
            //得到城市
            String curCity = location.getCity();
            latitude = Double.toString(location.getLatitude());
            longtitude = Double.toString(location.getLongitude());
            getProviceid();
//            String curCity = location.getProvince();
            if (TextUtils.isEmpty(curCity)) {
                city_name.setText("定位中...");
            } else {
                city_name.setText(curCity);
                mLocClient.stop();
            }

        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    //申请地图相关权限
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            ArrayList<String> permissions = new ArrayList<>();
            if (mActivity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }

            if (permissions.size() == 0) {
                return;
            } else {
                requestPermissions(permissions.toArray(new String[permissions.size()]), BAIDU_ACCESS_FINE_LOCATION);
            }
        }
    }

    //申请地图相关权限:返回监听
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case BAIDU_ACCESS_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {//允许
                    initMapLocation();
                } else {//拒绝
                    MyUtils.showToast(mActivity, "定位权限未开启");
                }
                break;
            default:
                break;

        }
    }

    /*
     * 从服务器中下载APK
     */
    protected void downLoadApk(final String url) {
        final ProgressDialog pd;    //进度条对话框
        pd = new ProgressDialog(mActivity);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    File file = DownLoadManager.getFileFromServer(url, pd);
                    sleep(3000);
                    installApk(file);
                    pd.dismiss(); //结束掉进度条对话框
                } catch (Exception e) {
//                    Toast.makeText(mActivity, "下载失败!",
//                            Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //安装apk
    protected void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        //判断是否是AndroidN以及更高的版本

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            Uri contentUri = FileProvider.getUriForFile(mActivity, "com.emjiayuan.app.fileProvider", file);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");

        } else {

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");

        }
        startActivity(intent);
    }
}
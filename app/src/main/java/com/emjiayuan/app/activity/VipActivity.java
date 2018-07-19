package com.emjiayuan.app.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.Constants;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.GlideUtil;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.entity.PayResult;
import com.emjiayuan.app.entity.User;
import com.emjiayuan.app.entity.Vip;
import com.emjiayuan.app.entity.WXpayInfo;
import com.emjiayuan.app.event.WXpaySuccessEvent;
import com.emjiayuan.app.widget.BottomPopupOption;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class VipActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.tx)
    ImageView tx;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.now)
    TextView now;
;
    @BindView(R.id.line_top)
    View lineTop;
    @BindView(R.id.rights)
    TextView rights;
    @BindView(R.id.period)
    TextView period;

    private BottomPopupOption popupOption;
    private List<View> viewList;
    private List<Vip> vipList;
    private Integer[] images = new Integer[]{R.drawable.vip_bg4, R.drawable.vip_bg3, R.drawable.vip_bg2, R.drawable.vip_bg1};
    private User user;
    private PopupWindow mPopupWindow;
    private String levelid;
    private String money;
    private String orderInfo;
    private int mTouchSlop;
    private int now_id;


    @Override
    protected int setLayoutId() {
        return R.layout.activity_vip;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        popupOption = new BottomPopupOption(this, true);
        title.setText("会员中心");

        user();
//        initViewPager();
    }

    @Override
    protected void setListener() {
        ViewConfiguration configuration = ViewConfiguration.get(mActivity);
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
        back.setOnClickListener(this);
        //由于ViewPager 没有点击事件，可以通过对VIewPager的setOnTouchListener进行监听已达到实现点击事件的效果
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            int touchFlag = 0;
            float x = 0, y = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touchFlag = 0;
                        x = event.getX();
                        y = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float xDiff = Math.abs(event.getX() - x);
                        float yDiff = Math.abs(event.getY() - y);
                        if (xDiff < mTouchSlop && xDiff >= yDiff)
                            touchFlag = 0;
                        else
                            touchFlag = -1;
                        break;
                    case MotionEvent.ACTION_UP:
                        if (touchFlag == 0) {
                            int currentItem = viewPager.getCurrentItem();
                            levelid=vipList.get(currentItem).getId();
                            if (Integer.parseInt(levelid)<=now_id){
                                MyUtils.showToast(mActivity,"请选择更高的等级来升级！");
                                break;
                            }
                            showPopupWindow();
                        }
                        break;
                }
                return false;
            }
        });
//        viewPager.setOnTouchListener(new View.OnTouchListener() {
//            int flage = 0;
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        flage = 0;
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        flage = 1;
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        if (flage == 1) {
//                            int item = viewPager.getCurrentItem();
//                            levelid=vipList.get(item).getCost();
////                            popupOption.showPopupWindow();
//                            showPopupWindow();
//                        }
//                        break;
//
//
//                }
//                return false;
//            }
//        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    public void user() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        if (Global.loginResult == null) {
            startActivity(new Intent(mActivity, LoginActivity.class));
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
                message.what = 1;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    public void setData() {
        RequestOptions options=new RequestOptions();
        options.placeholder(R.drawable.default_tx).error(R.drawable.default_tx);
        options.circleCrop();
        Glide.with(mActivity).load(user.getHeadimg()).apply(options).into(tx);
//        Glide.with(mActivity).load(user.getHeadimg()).apply(RequestOptions.circleCropTransform()).into(tx);
        username.setText(user.getShowname());
        now.setText("当前等级："+user.getClassname());
        if ("Vip普通会员".equals(user.getClassname())){
            period.setText("会员截止日期：永久");
        }else{
            period.setText("会员截止日期："+user.getViptime());
        }

        rights.setText("享受权益：购物享受" + Double.parseDouble(user.getDiscount())/10 + "折");
        int class_id=Integer.parseInt(user.getClass_id());
        int buy_class_id=Integer.parseInt(user.getBuy_class_id());
        now_id=class_id>buy_class_id?class_id:buy_class_id;
        getLevelList();

    }




    public void getLevelList() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
//new call
        Call call = MyOkHttp.GetCall("user.getLevelList", formBody);
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
                MyUtils.e("------获取vip------", result);
                Message message = new Message();
                message.what = 0;
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
                case 0:
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        vipList=new ArrayList<>();
                        viewList = new ArrayList<>();
                        if ("200".equals(code)) {
                            JSONArray jsonArray = new JSONArray(data);

//                            for (int i = 0; i < jsonArray.length(); i++) {
                            for (int i = jsonArray.length()-1; i >= 0; i--) {
                                Vip vip = gson.fromJson(jsonArray.getJSONObject(i).toString(), Vip.class);
                                vipList.add(vip);
//                                for (int j = 0; j < 4; j++) {
//                                    ImageView imageView = new ImageView(VipActivity.this);
//                                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//                                    imageView.setImageResource(images[j]);
//                                    viewList.add(imageView);
//                                }
//                                initViewPager();
                                View view = LayoutInflater.from(mActivity).inflate(R.layout.vip_item, null);
                                ImageView bg = view.findViewById(R.id.bg);
                                TextView classname = view.findViewById(R.id.classname);
                                TextView join = view.findViewById(R.id.join);
                                TextView cost = view.findViewById(R.id.cost);
                                TextView content = view.findViewById(R.id.content);
//                                Glide.with(mActivity).load(images[i]).into(bg);
                                Glide.with(mActivity).load(vip.getBackground()).into(bg);
                                if (now_id==Integer.parseInt(vip.getId())){
                                    join.setText("当前等级");
                                }
                                join.getBackground().setAlpha(60);
//                                if (user.getClass_id())
                                classname.setText(vip.getClassname()+"会员");
                                cost.setText(vip.getCost()+"/年");
                                content.setText("购物享受"+Double.parseDouble(vip.getDiscount())/10+"折");
                                viewList.add(view);
                                initViewPager();

                            }

                        } else {
                            MyUtils.showToast(mActivity,message);
                        }
                    } catch (JSONException e) {
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
                            user = gson.fromJson(data, User.class);
                            setData();
                        } else {
                            MyUtils.showToast(mActivity, message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 3://支付宝支付结果
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        MyUtils.showToast(mActivity, "支付成功");
                        mPopupWindow.dismiss();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        MyUtils.showToast(mActivity, "支付失败");
                        mPopupWindow.dismiss();
                    }
                    break;
                case Constants.WALLETPAY:
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            MyUtils.showToast(mActivity,message);
                        } else{
                            MyUtils.showToast(mActivity,message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case Constants.ALIPAY:
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            orderInfo = data;
                            alipay();
                        } else {
                            MyUtils.showToast(mActivity,message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case Constants.WXPAY:
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            WXpayInfo payInfo = gson.fromJson(data,WXpayInfo.class);
                            WXPay(payInfo);
                        } else{
                            MyUtils.showToast(mActivity,message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    private void initViewPager() {


        /**** 重要部分  ******/
        //clipChild用来定义他的子控件是否要在他应有的边界内进行绘制。 默认情况下，clipChild被设置为true。 也就是不允许进行扩展绘制。
//        viewPager.setClipChildren(false);
        //父容器一定要设置这个，否则看不出效果
//        viewPagerContainer.setClipChildren(false);


//        viewPager.setLayoutParams(params);
        //为ViewPager设置PagerAdapter


        viewPager.setAdapter(new MyPagerAdapter(mActivity, viewList));
        //设置ViewPager切换效果，即实现画廊效果
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        //设置预加载数量
        viewPager.setOffscreenPageLimit(2);
        //设置每页之间的左右间隔
        viewPager.setPageMargin(100);
        switch (now_id){
            case 1:
                viewPager.setCurrentItem(0);
                break;
            case 2:
                viewPager.setCurrentItem(0);
                break;
            case 3:
                viewPager.setCurrentItem(1);
                break;
            case 4:
                viewPager.setCurrentItem(2);
                break;
            case 5:
                viewPager.setCurrentItem(3);
                break;
        }
        //将容器的触摸事件反馈给ViewPager
        /*viewPagerContainer.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // dispatch the events to the ViewPager, to solve the problem that we can swipe only the middle view.
                return viewPager.dispatchTouchEvent(event);
            }

        });*/
    }


    class MyPagerAdapter extends PagerAdapter {

        private List<View> viewList;
        private Context context;

        public MyPagerAdapter(Context context, List<View> viewList) {
            this.viewList = viewList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return viewList == null ? 0 : viewList.size();
//            return 10;//ViewPager里的个数
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

//            ImageView imageView = new ImageView(VipActivity.this);
//            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            imageView.setImageResource(R.drawable.vip1);
//            ((ViewPager) container).addView(imageView);
            ((ViewPager) container).addView(viewList.get(position), 0);
            return viewList.get(position);

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView(viewList.get(position));
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
            //这里做切换ViewPager时，底部RadioButton的操作
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            /*if (viewPagerContainer != null) {
                viewPagerContainer.invalidate();
            }*/
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    /**
     * 实现的原理是，在当前显示页面放大至原来的MAX_SCALE
     * 其他页面才是正常的的大小MIN_SCALE
     */
    class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MAX_SCALE = 1.2f;
        private static final float MIN_SCALE = 1.0f;//0.85f

        @Override
        public void transformPage(View view, float position) {
            //setScaleY只支持api11以上
            if (position < -1) {
                view.setScaleX(MIN_SCALE);
                view.setScaleY(MIN_SCALE);
            } else if (position <= 1) //a页滑动至b页 ； a页从 0.0 -1 ；b页从1 ~ 0.0
            { // [-1,1]
//              Log.e("TAG", view + " , " + position + "");
                float scaleFactor = MIN_SCALE + (1 - Math.abs(position)) * (MAX_SCALE - MIN_SCALE);
                view.setScaleX(scaleFactor);
                //每次滑动后进行微小的移动目的是为了防止在三星的某些手机上出现两边的页面为显示的情况
                if (position > 0) {
                    view.setTranslationX(-scaleFactor * 2);
                } else if (position < 0) {
                    view.setTranslationX(scaleFactor * 2);
                }
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]

                view.setScaleX(MIN_SCALE);
                view.setScaleY(MIN_SCALE);

            }
        }

    }
    /**
     * 弹出Popupwindow
     */
    public void showPopupWindow() {
        View popupWindow_view = LayoutInflater.from(mActivity).inflate(R.layout.payment_layout, null);
        LinearLayout yepay_ll = popupWindow_view.findViewById(R.id.yepay_ll);
        LinearLayout weixin_ll = popupWindow_view.findViewById(R.id.weixin_ll);
        final LinearLayout alipay_ll = popupWindow_view.findViewById(R.id.alipay_ll);
        Button cz_btn = popupWindow_view.findViewById(R.id.cz_btn);
        cz_btn.setText("立即支付");
        Button cancel_btn = popupWindow_view.findViewById(R.id.cancel_btn);
        final CheckBox weixin_check = popupWindow_view.findViewById(R.id.weixin_check);
        final CheckBox alipay_check = popupWindow_view.findViewById(R.id.alipay_check);
        final CheckBox yepay_check = popupWindow_view.findViewById(R.id.yepay_check);
        yepay_ll.setVisibility(View.GONE);
        weixin_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (weixin_check.isChecked()) {
                    weixin_check.setChecked(false);
                } else {
                    weixin_check.setChecked(true);
                    alipay_check.setChecked(false);
                    yepay_check.setChecked(false);
                }
            }
        });
        alipay_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alipay_check.isChecked()) {
                    alipay_check.setChecked(false);
                } else {
                    alipay_check.setChecked(true);
                    weixin_check.setChecked(false);
                    yepay_check.setChecked(false);
                }
            }
        });
        yepay_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (yepay_check.isChecked()) {
                    yepay_check.setChecked(false);
                } else {
                    alipay_check.setChecked(false);
                    weixin_check.setChecked(false);
                    yepay_check.setChecked(true);
                }
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });
        cz_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyUtils.isFastClick()){
                    return;
                }
                if (alipay_check.isChecked() == true) {
                    alipayrequest();
                } else if (weixin_check.isChecked() == true) {
                    WXpayrequest();
                } else if (yepay_check.isChecked() == true) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                    builder.setIcon(android.R.drawable.ic_dialog_info);
                    builder.setTitle("温馨提示");
                    builder.setMessage("余额支付"+levelid+"元，确定要继续吗");
                    builder.setCancelable(true);

                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Walletpayrequest();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            /*
                             *  在这里实现你自己的逻辑
                             */
                        }
                    });
                    builder.create().show();

                } else {
                    Toast.makeText(mActivity, "请选择支付方式",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        mPopupWindow = new PopupWindow(popupWindow_view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setAnimationStyle(R.style.popwindow_anim_style);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                MyUtils.setWindowAlpa(mActivity, false);
            }
        });
        if (mPopupWindow != null && !mPopupWindow.isShowing()) {
            mPopupWindow.showAtLocation(popupWindow_view, Gravity.BOTTOM, 0, 0);
        }
        MyUtils.setWindowAlpa(mActivity, true);
    }

    //    支付宝支付
    private void alipay() {
/**
 * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
 * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
 * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
 *
 * orderInfo的获取必须来自服务端；
 */
        /*boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;*/
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(VipActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Message msg = new Message();
                msg.what = 3;
                msg.obj = result;
                myHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    //微信支付

    private void WXPay(WXpayInfo payInfo) {
        IWXAPI api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        PayReq request = new PayReq();
        request.appId = payInfo.getAppid();
        request.partnerId = payInfo.getPartnerid();
        request.prepayId = payInfo.getPrepayid();
        request.packageValue = payInfo.getPackageX();
        request.nonceStr = payInfo.getNoncestr();
        request.timeStamp = payInfo.getTimestamp();
        request.sign = payInfo.getSign();
        api.sendReq(request);
    }

    public void Walletpayrequest() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("paytype", "BUY_LEVEL");//传递键值对参数
        formBody.add("levelid", levelid);//传递键值对参数
        formBody.add("userid", Global.loginResult.getId());
//new call
        Call call = MyOkHttp.GetCall("pay.wallet", formBody);
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
                MyUtils.e("------余额支付------", result);
                Message message = new Message();
                message.what = Constants.WALLETPAY;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    public void alipayrequest() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("paytype", "BUY_LEVEL");//传递键值对参数
        formBody.add("levelid", levelid);//传递键值对参数
        formBody.add("userid", Global.loginResult.getId());
//new call
        Call call = MyOkHttp.GetCall("pay.alipay", formBody);
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
                MyUtils.e("------支付宝支付------", result);
                Message message = new Message();
                message.what = Constants.ALIPAY;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    public void WXpayrequest() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("paytype", "BUY_LEVEL");//传递键值对参数
        formBody.add("levelid", levelid);//传递键值对参数
        formBody.add("userid", Global.loginResult.getId());
//new call
        Call call = MyOkHttp.GetCall("pay.wxpay", formBody);
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
                MyUtils.e("------微信支付------", result);
                Message message = new Message();
                message.what = Constants.WXPAY;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPopupWindow!=null&&mPopupWindow.isShowing()){
            mPopupWindow.dismiss();
        }
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(WXpaySuccessEvent event) {
        BaseResp resp=event.getResp();
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {   // 0 成功  展示成功页面
				/*AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("微信支付结果");
				builder.setMessage("支付订单成功！");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						finish();
					}
				});
				builder.show();*/
                Toast.makeText(mActivity, "支付成功", Toast.LENGTH_SHORT)
                        .show();
            } else if (resp.errCode == -1) {  // -1 错误  可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
                Toast.makeText(mActivity, "支付出错", Toast.LENGTH_SHORT)
                        .show();
                mPopupWindow.dismiss();

            } else if (resp.errCode == -2) {  // -2 用户取消    无需处理。发生场景：用户不支付了，点击取消，返回APP。
                Toast.makeText(mActivity, "取消支付", Toast.LENGTH_SHORT)
                        .show();
                mPopupWindow.dismiss();
            }
        }
    }
}

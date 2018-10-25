package com.emjiayuan.app.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.Constants;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.adapter.TopUpAdapter;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.entity.LoginResult;
import com.emjiayuan.app.entity.PayResult;
import com.emjiayuan.app.entity.Product;
import com.emjiayuan.app.entity.User;
import com.emjiayuan.app.entity.WXpayInfo;
import com.emjiayuan.app.event.WXpaySuccessEvent;
import com.emjiayuan.app.widget.BottomPopupOption;
import com.emjiayuan.app.widget.MyGridView;
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
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class TopUpActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.gv_sye)
    MyGridView gv_sye;
    @BindView(R.id.gv_ssp)
    MyGridView gv_ssp;
    @BindView(R.id.service_ll)
    LinearLayout serviceLl;
    @BindView(R.id.line_top)
    View lineTop;
    @BindView(R.id.cz_btn)
    Button czBtn;
    @BindView(R.id.balance)
    TextView balance;
    @BindView(R.id.checkAll)
    CheckBox checkAll;
    @BindView(R.id.check)
    TextView check;
    @BindView(R.id.check_ll)
    LinearLayout checkLl;
    @BindView(R.id.xieyi)
    TextView xieyi;

    private TopUpAdapter adapter1;
    private TopUpAdapter adapter2;
    BottomPopupOption popupOption;
    private PopupWindow mPopupWindow;
    private Product product;
    private String orderInfo;
    private String giftorderid = "";

    @Override
    protected int setLayoutId() {
        return R.layout.activity_top_up;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        popupOption = new BottomPopupOption(this, false);
        product = (Product) getIntent().getSerializableExtra("product");
        title.setText("充值中心");
        save.setText("消费记录");
        save.setVisibility(View.VISIBLE);
        request();
        user();
    }

    public void user() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
//        if (Global.loginResult == null) {
//            startActivity(new Intent(mActivity, LoginActivity.class));
//            return;
//        }
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
                message.what = 4;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    public void request() {
        if (Global.loginResult == null) {
            startActivity(new Intent(mActivity, LoginActivity.class));
            finish();
            return;
        }
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("producttype", "3");//传递键值对参数
        formBody.add("pageindex", "1");//传递键值对参数
        formBody.add("pagesize", "100");//传递键值对参数
        formBody.add("provinceid", Global.provinceid);
//new call
        Call call = MyOkHttp.GetCall("product.getProductList", formBody);
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
                MyUtils.e("------赠品------", result);
                Message message = new Message();
                message.what = 0;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    public void alipayrequest() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("paytype", "RECHARGE_GIVE");//传递键值对参数
        if (Global.loginResult == null) {
            MyUtils.showToast(mActivity, "请先登录！");
            return;
//            startActivity(new Intent(mActivity,LoginActivity.class));
        }
        formBody.add("userid", Global.loginResult.getId());//传递键值对参数
        formBody.add("productid", product.getId());//传递键值对参数
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
                message.what = 1;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    public void WXpayrequest() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("paytype", "RECHARGE_GIVE");//传递键值对参数
        if (Global.loginResult == null) {
            MyUtils.showToast(mActivity, "请先登录！");
            return;
//            startActivity(new Intent(mActivity,LoginActivity.class));
        }
        formBody.add("userid", Global.loginResult.getId());//传递键值对参数
        formBody.add("productid", product.getId());//传递键值对参数
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
                message.what = 3;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    private ArrayList<Product> list1 = new ArrayList<>();//送余额
    private ArrayList<Product> list2 = new ArrayList<>();//送商品
    private User user;
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
                            JSONArray jsonArray = new JSONArray(data);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Product product = gson.fromJson(jsonArray.getJSONObject(i).toString(), Product.class);
                                if ("1".equals(product.getGifttype())) {
                                    list1.add(product);
                                } else if ("0".equals(product.getGifttype())) {
                                    list2.add(product);
                                }
                            }
                            adapter1 = new TopUpAdapter(mActivity, list1);
                            adapter2 = new TopUpAdapter(mActivity, list2);
                            gv_sye.setAdapter(adapter1);
                            gv_ssp.setAdapter(adapter2);
                            for (int i = 0; i < list1.size(); i++) {
                                if (product != null && product.getId().equals(list1.get(i).getId())) {
                                    adapter1.setSelected(i);
                                }
                            }
                            for (int i = 0; i < list2.size(); i++) {
                                if (product != null && product.getId().equals(list2.get(i).getId())) {
                                    adapter2.setSelected(i);
                                }
                            }
                        } else {
                            MyUtils.showToast(mActivity, message);
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
                        if ("0".equals(product.getGifttype())) {
                            giftorderid = jsonObject.getString("giftorderid");
                        }
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            orderInfo = data;
//                            Intent intent=new Intent(mActivity,OrderConfirmActivity3.class);
//                            intent.putExtra("orderid",giftorderid);
//                            startActivity(intent);
                            alipay();
                        } else {
                            MyUtils.showToast(mActivity, message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
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
                        Intent intent = new Intent(mActivity, OrderConfirmActivity3.class);
                        intent.putExtra("orderid", giftorderid);
                        startActivity(intent);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        MyUtils.showToast(mActivity, "支付失败");
                        mPopupWindow.dismiss();
                    }
                    break;
                case 3:
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        if ("0".equals(product.getGifttype())) {
                            giftorderid = jsonObject.getString("giftorderid");
                        }
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            WXpayInfo payInfo = gson.fromJson(data, WXpayInfo.class);
                            WXPay(payInfo);
                        } else {
                            MyUtils.showToast(mActivity, message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            user = gson.fromJson(data, User.class);
                            Global.loginResult = gson.fromJson(data, LoginResult.class);
                            balance.setText(user.getYue());
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
        back.setOnClickListener(this);
        save.setOnClickListener(this);
        czBtn.setOnClickListener(this);
        gv_sye.setOnItemClickListener(this);
        gv_ssp.setOnItemClickListener(this);
        xieyi.setOnClickListener(this);
//        checkAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//            }
//        });
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
            case R.id.xieyi:
                startActivity(new Intent(mActivity,AgreementActivity.class));
                break;
            case R.id.save:
                startActivity(new Intent(mActivity,BalanceActivity.class));
                break;
            case R.id.cz_btn:
                if (!checkAll.isChecked()) {
                    MyUtils.showToast(mActivity, "请阅读并同意充值协议！");
                    return;
                }
                if (product == null) {
                    MyUtils.showToast(mActivity, "请选择充值金额！");
                    return;
                }
                showPopupWindow();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (MyUtils.isFastClick()) {
            switch (adapterView.getId()) {
                case R.id.gv_sye:
                    product = list1.get(i);
                    adapter1.setSelected(i);
                    adapter2.setSelected(-1);
//                    showPopupWindow();
                    break;
                case R.id.gv_ssp:
                    product = list2.get(i);
                    adapter2.setSelected(i);
                    adapter1.setSelected(-1);
//                    showPopupWindow();
                    break;

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
                if (!MyUtils.isFastClick()) {
                    return;
                }
                if (alipay_check.isChecked() == true) {
                    //支付宝支付
                    alipayrequest();
                } else if (weixin_check.isChecked() == true) {
                    //微信支付
                    WXpayrequest();
                } else if (yepay_check.isChecked() == true) {
//                    MyUtils.showToast(mActivity, "余额支付");
                } else {
                    MyUtils.showToast(mActivity, "请选择支付方式");
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
                PayTask alipay = new PayTask(TopUpActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Message msg = new Message();
                msg.what = 2;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(WXpaySuccessEvent event) {
        BaseResp resp = event.getResp();
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
                if ("0".equals(product.getGifttype())) {
                    Intent intent = new Intent(mActivity, OrderConfirmActivity3.class);
                    intent.putExtra("orderid", giftorderid);
                    startActivity(intent);
                    mPopupWindow.dismiss();
//                    finish();
                }

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

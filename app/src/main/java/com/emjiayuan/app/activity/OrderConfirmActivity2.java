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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.Constants;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.activity.address.AddressActivity;
import com.emjiayuan.app.adapter.OrderInAdapter;
import com.emjiayuan.app.entity.Address;
import com.emjiayuan.app.entity.CarBean;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.entity.OrderComfirm;
import com.emjiayuan.app.entity.PayResult;
import com.emjiayuan.app.widget.BottomPopupOption;
import com.emjiayuan.app.widget.MyListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class OrderConfirmActivity2 extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.shr_tv)
    TextView shr_tv;
    @BindView(R.id.tele_tv)
    TextView tele_tv;
    @BindView(R.id.address_tv)
    TextView address_tv;
    @BindView(R.id.all_ll)
    RelativeLayout all_ll;
    @BindView(R.id.address_ll)
    LinearLayout address_ll;
    @BindView(R.id.lv_goods)
    MyListView lv_goods;
    @BindView(R.id.coupon_ll)
    LinearLayout coupon_ll;
    @BindView(R.id.bz_et)
    EditText bz_et;
    @BindView(R.id.freight_tv)
    TextView freight_tv;
    @BindView(R.id.coupon_tv)
    TextView coupon_tv;
    @BindView(R.id.total_tv)
    TextView total_tv;
    @BindView(R.id.limit_tv)
    TextView limit_tv;
    @BindView(R.id.total_pay)
    TextView total_pay;
    @BindView(R.id.pay_btn)
    TextView pay_btn;
    @BindView(R.id.line_top)
    View lineTop;
    @BindView(R.id.address_ll_no)
    LinearLayout addressLlNo;

    private BottomPopupOption popupOption;
    private OrderInAdapter adapter;
    private List<CarBean> carBeanList = new ArrayList<>();
    private String remark = "";
    private String cartids = "";
    private String productids = "";
    private String orderid;
    private OrderComfirm orderComfirm;
    private PopupWindow mPopupWindow;
    private String orderInfo;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_order_confirm2;
    }

    @Override
    protected void initData() {
        popupOption = new BottomPopupOption(this, true);
    }

    @Override
    protected void initView() {
        title.setText("确认订单");
        reqDefaultAddress();
        Intent intent = getIntent();
        if (intent != null) {
            orderComfirm = (OrderComfirm) intent.getSerializableExtra("orderComfirm");
            for (int i = 0; i < orderComfirm.getProducts().size(); i++) {
                productids += orderComfirm.getProducts().get(i).getId() + "|" + orderComfirm.getProducts().get(i).getBuynum() + ",";
            }
        }
        freight_tv.setText("运费：¥" + orderComfirm.getExpressprice());
        coupon_tv.setText("优惠券：-¥" + orderComfirm.getCouponprice());
        total_tv.setText("合计金额：¥" + orderComfirm.getPayprice());
        total_pay.setText(orderComfirm.getPromotionvalue()+"");
        adapter = new OrderInAdapter(this, orderComfirm);
        lv_goods.setAdapter(adapter);
        lv_goods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!MyUtils.isFastClick()){
                    return;
                }
                Intent intent = new Intent(OrderConfirmActivity2.this, IntegralYlDetailActivity.class);
                intent.putExtra("productid", orderComfirm.getProducts().get(i).getId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void setListener() {
        back.setOnClickListener(this);
        address_ll.setOnClickListener(this);
        addressLlNo.setOnClickListener(this);
        coupon_ll.setOnClickListener(this);
        pay_btn.setOnClickListener(this);
    }

    public void reqDefaultAddress() {

        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("userid", Global.loginResult.getId());
        Log.d("------参数------", formBody.build().toString());
//new call
        Call call = MyOkHttp.GetCall("userAddress.getDefaultAddress", formBody);
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
                MyUtils.e("------获取默认地址结果------", result);
                Message message = new Message();
                message.what = 0;
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
        formBody.add("addressprovince", address.getShengfen());
        formBody.add("productids", productids);
        formBody.add("promotiontype", orderComfirm.getPromotiontype());
        formBody.add("promotionvalue", orderComfirm.getPromotionvalue());
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
                message.what = 6;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }
    public void addOrder() {

        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("userid", Global.loginResult.getId());
        formBody.add("addressname", address.getUsername());
        formBody.add("addressphone", address.getTelphone());
        formBody.add("address", address.getAddress());
        formBody.add("addressprovince", address.getShengfen());
        formBody.add("couponid", "");
        formBody.add("remark", remark);
        formBody.add("provinceid",Global.provinceid);
//        formBody.add("cartids", cartids.substring(0,cartids.lastIndexOf(",")));
        formBody.add("productids", productids.substring(0, productids.lastIndexOf(",")));
        formBody.add("promotiontype", orderComfirm.getPromotiontype());
        formBody.add("promotionvalue", orderComfirm.getPromotionvalue()+"");
        Log.d("------参数------", formBody.build().toString());
//new call
        Call call = MyOkHttp.GetCall("order.addOrder", formBody);
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
                MyUtils.e("------添加订单结果------", result);
                Message message = new Message();
                message.what = 1;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    private Address address;

    public void setAddress() {
        if (address==null){
            addressLlNo.setVisibility(View.VISIBLE);
            address_ll.setVisibility(View.GONE);
        }else{
            addressLlNo.setVisibility(View.GONE);
            address_ll.setVisibility(View.VISIBLE);
            shr_tv.setText("收货人：" + address.getUsername());
            tele_tv.setText(address.getTelphone());
            address_tv.setText("地址：" + address.getAddress());
        }

    }
    public void alipayrequest() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("paytype", "COMMON");//传递键值对参数
        formBody.add("orderid", orderid);//传递键值对参数
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
                message.what = 2;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    public void WXpayrequest() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("paytype", "COMMON");//传递键值对参数
        formBody.add("orderid", orderid);//传递键值对参数
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
                message.what = 4;
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
                        if ("200".equals(code)) {
                            JSONObject jsonObject1 = new JSONObject(data);
                            address = gson.fromJson(jsonObject1.toString(), Address.class);
                        } else {
//                            MyUtils.showToast(mActivity, message);
                        }
                        setAddress();
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
                            JSONObject jsonObject1 = new JSONObject(data);
                            orderid = jsonObject1.getString("orderid");
                            MyUtils.showToast(mActivity,"兑换成功！");
                            finish();
//                            showPopupWindow();
//                            Intent intent = new Intent(mActivity, OrderDetailActivity.class);
//                            intent.putExtra("orderid", orderid);
//                            startActivity(intent);
                        } else {
                            MyUtils.showToast(mActivity, message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
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
                            orderInfo=data;
                            alipay();
                        } else {
                            MyUtils.showToast(mActivity,message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
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
                        MyUtils.showToast(mActivity,"支付成功");
                        mPopupWindow.dismiss();
                        Intent intent = new Intent(mActivity, OrderDetailActivity.class);
                            intent.putExtra("orderid", orderid);
                            intent.putExtra("type", 0);
                            startActivity(intent);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        MyUtils.showToast(mActivity,"支付失败");
                        mPopupWindow.dismiss();
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
                            orderInfo=data;
                            alipay();
                        } else {
                            MyUtils.showToast(mActivity,message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            JSONObject jsonObject1 = new JSONObject(data);
                            orderComfirm = gson.fromJson(jsonObject1.toString(), OrderComfirm.class);
//                            setdata();
                        } else {
                            MyUtils.showToast(mActivity, message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {
        if (!MyUtils.isFastClick()){
            return;
        }
        Intent intent;
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.address_ll:
                intent = new Intent(mActivity, AddressActivity.class);
                intent.putExtra("flag", true);
                startActivityForResult(intent, 100);
                break;
            case R.id.address_ll_no:
                intent = new Intent(mActivity, AddressActivity.class);
                intent.putExtra("flag", true);
                startActivityForResult(intent, 100);
                break;
            case R.id.coupon_ll:
                startActivity(new Intent(this, CouponGetActivity.class));
                break;
            case R.id.pay_btn:
                remark = bz_et.getText().toString();
                if (address==null){
                    MyUtils.showToast(mActivity,"请添加收货地址！");
                    return;
                }
//                showPopupWindow();
                if (orderid==null){
                    addOrder();
                }else{
                    showPopupWindow();
                }
//                popupOption.showPopupWindow();
                break;
        }
    }
    /**
     * 弹出Popupwindow
     */
    public void showPopupWindow() {
        View popupWindow_view = LayoutInflater.from(mActivity).inflate(R.layout.payment_layout, null);
        LinearLayout yepay_ll=popupWindow_view.findViewById(R.id.yepay_ll);
        LinearLayout weixin_ll=popupWindow_view.findViewById(R.id.weixin_ll);
        final LinearLayout alipay_ll=popupWindow_view.findViewById(R.id.alipay_ll);
        Button cz_btn=popupWindow_view.findViewById(R.id.cz_btn);
        cz_btn.setText("立即支付");
        Button cancel_btn=popupWindow_view.findViewById(R.id.cancel_btn);
        final CheckBox weixin_check=popupWindow_view.findViewById(R.id.weixin_check);
        final CheckBox alipay_check=popupWindow_view.findViewById(R.id.alipay_check);
        final CheckBox yepay_check=popupWindow_view.findViewById(R.id.yepay_check);
//        yepay_ll.setVisibility(View.VISIBLE);
        weixin_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (weixin_check.isChecked()){
                    weixin_check.setChecked(false);
                }else{
                    weixin_check.setChecked(true);
                    alipay_check.setChecked(false);
                    yepay_check.setChecked(false);
                }
            }
        });
        alipay_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alipay_check.isChecked()){
                    alipay_check.setChecked(false);
                }else{
                    alipay_check.setChecked(true);
                    weixin_check.setChecked(false);
                    yepay_check.setChecked(false);
                }
            }
        });
        yepay_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (yepay_check.isChecked()){
                    yepay_check.setChecked(false);
                }else{
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
                if (alipay_check.isChecked()==true){
                    alipayrequest();
                }else if(weixin_check.isChecked()==true){
//                    Toast.makeText(mActivity, "微信支付",
//                            Toast.LENGTH_LONG).show();
                    WXpayrequest();
                }else if(yepay_check.isChecked()==true){
                    Toast.makeText(mActivity, "余额支付",
                            Toast.LENGTH_LONG).show();
                }else{
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
                MyUtils.setWindowAlpa(mActivity,false);
            }
        });
        if (mPopupWindow != null && !mPopupWindow.isShowing()) {
            mPopupWindow.showAtLocation(popupWindow_view, Gravity.BOTTOM, 0, 0);
        }
        MyUtils.setWindowAlpa(mActivity,true);
    }
    //    支付宝支付
    private void alipay(){
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
                PayTask alipay = new PayTask(OrderConfirmActivity2.this);
                Map<String, String> result = alipay.payV2(orderInfo,true);
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

    private void WXPay(){
        IWXAPI api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        PayReq request = new PayReq();

        request.appId = Constants.APP_ID;
        request.partnerId = "1900000109";
        request.prepayId= "1101000000140415649af9fc314aa427";
        request.packageValue = "Sign=WXPay";
        request.nonceStr= "1101000000140429eb40476f8896f4c9";
        request.timeStamp= "1398746574";
        request.sign= "7FFECB600D7157C5AA49810D2D8F28BC2811827B";
        api.sendReq(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            address = (Address) data.getSerializableExtra("address");
            setAddress();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPopupWindow!=null&&mPopupWindow.isShowing()){
            mPopupWindow.dismiss();
        }
    }
}

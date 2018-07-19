package com.emjiayuan.app.widget;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.OrderInfoUtil2_0;

import java.util.Map;

import javax.xml.transform.Result;

public class BottomPopupOption {

    /** 支付宝支付业务：入参app_id */
    public static final String APPID ="2016080200151196";//沙箱
//    public static final String APPID ="2016091400510178";
//    public static final String APPID ="2018052960284252";

    /** 支付宝账户登录授权业务：入参pid值 */
    public static final String PID = "2088022739005990";
    /** 支付宝账户登录授权业务：入参target_id值 */
    public static final String TARGET_ID = "";

    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /** 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1 */
    public static final String RSA2_PRIVATE ="MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC+98B8enujdR1j6YeNsmPFRI+D/GtTKc2/H9vMtiAP8cUJJoFQvo/QlqMxiGmDL/7Wvm021kRpWNIAYREXBe/yipXd+QUSIppKZthEBT5z3XavWSA0k2zYa+0ysXS0Q5HBIvBk0atgxqDHfZQ1FxhlqBZsc9P6HLYUAwKxretGOPktW1unFm9HU63zwwBSQEZghTl8Sl8j3C4J9KwNCpPzg3pAIaRD5pmkm8ge2Q6RIWAm4zKBuNaJyZirxPLuSuyxTYOlx1tbSlbCx7As2J/MzBOEEaf/LDp7EmBehIF0XJtylYfkiECpgMHsRBNVKgoCqAI4MKM6Pr9Rl842KKkbAgMBAAECggEAX5Wch/G6nudIzbCfA8KNg7i8aODYS2j6LbbhioaCZ9ioudrHEUZAr7QmfO19eod0c8Ie4jq/RUeK3Vu4dSCoqGrLdq5k5l3hgmoVx34kc1QfSsc+gnJkIbNrp1sBQhUsCFLXpm7369mfshpeK3Ci6ocGqkQTjvX+AOg82fhmcd/HKWW4ntSKPef2cl0H7L6r7WLJPodWyUeeVrfgD+VA2tWQeJvqb8oCJE1WIgz3ihg8tIp7wIEaf5zzlNMf87bDdE7RpEwun4P1xdNGG5dSD5u3XsfoAmAXBCmv7VrhOOPRbe1Qn/IYnvvbR48qQYD2C48bFEY7rjNgiuNRkqpniQKBgQDdXn78dmJ44RVP79nr4yysur3QWLGCGpKa9FWuclwh4bJcfiBG1RnuaApzXsTw9cQtbsBAkX+wbm2vD5nzQsp3YChfFpth6bVAOhm+l8a43obu9b2ykG7QuXdnEawD3fjQwULzU9ZDsTDeZ+arRugh/IHKmkx4S4laz7no2um6NQKBgQDc17ougOm1jxTKrYu9YVQqpgPrVV4WelfZ8ZJrexhzlTpYX6F9jpsU7wuSdG6Zr1AwrSOTLiiApyrIp6B0AAPrCNBf1rrCjzHTGYw87ryG+Ka/6NqnBZ31kt5Ysq0IUOq1KbgrrtpM0FHecutJoT1IMNA/2OFpxqg6nspDzXPADwKBgE/XJiu+MvPwEnS7SDjYvaMq0w0TACqxlNDIcSKy4mHh+id74f5KG8ktwx8rPhK+QVw1hqeDEq+6lFrAYMEpBMcbalQpXi9pUFLy4ZgpO6YvtDD5dsqxZxZm3hbRe94FMBlyqsgCI9RUUxDUHpEFyPGBFfrRveThMZAMPbwrQINFAoGBANocHX9MRoXQGELL/9U8SPoOGJTWLSSaFCXl1ixc2Jte6M7bNA1jSHr2VEGCyLD0WoiYlygHUG0vrKucEnY+haAdhl8BHlaFQjoxYzXSu1O6REYJXdnTTFx7fymu3gH2mLfvyjsqZ+OxYwoGCacgnfusaiCEvKvRTdkHdtBcG1O1AoGBAMxhjfwQqCnZrQXTMUD33uDmnDPCJIXGkByCorF3kAEwFY7LjJL5eP+J+/lNoznM5z8vozqVNpnC2eiYKGuRMgcXqWjPe3zWteUPvqDzV6roZF3dyH6Ffl/uTe4gKQEOgSPcdmnRn3mNcVsyMr7YuVyvTuRB51vCTDMD0cmetyBE";
    public static final String RSA_PRIVATE = "";


    //上下文对象
    private Context mContext;
    //Title文字
    private String mTitle;
    //PopupWindow对象
    private PopupWindow mPopupWindow;
    //选项的文字
    private String[] options;
    //选项的颜色
    private int[] Colors;
    //点击事件
    private onPopupWindowItemClickListener itemClickListener;
    //是否用余额支付
    private boolean flag=false;

    /**
     * 一个参数的构造方法，用于弹出无标题的PopupWindow
     *
     * @param context
     */
    public BottomPopupOption(Context context) {
        this.mContext = context;
    }

    /**
     * 2个参数的构造方法，用于弹出有标题的PopupWindow
     *
     * @param context
     * @param title   标题
     */
    public BottomPopupOption(Context context, String title) {
        this.mContext = context;
        this.mTitle = title;
    }
    public BottomPopupOption(Context context, Boolean flag) {
        this.mContext = context;
        this.flag = flag;
    }

    /**
     * 设置选项的点击事件
     *
     * @param itemClickListener
     */
    public void setItemClickListener(onPopupWindowItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    /**
     * 设置选项文字
     */
    public void setItemText(String... items) {
        options = items;
    }

    /**
     * 设置选项文字颜色，必须要和选项的文字对应
     */
    public void setColors(int... color) {
        Colors = color;
    }


    /**
     * 添加子View
     */
//    private void addView(View v) {
//        LinearLayout lin_layout = (LinearLayout) v.findViewById(R.id.layout_popup_add);
//        //Title
//        TextView tv_pop_title = (TextView) v.findViewById(R.id.tv_popup_title);
//        //取消按钮
//        Button btn_cancel = (Button) v.findViewById(R.id.btn_cancel);
//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
//        if (mTitle != null) {
//            tv_pop_title.setText(mTitle);
//        } else {
//            tv_pop_title.setVisibility(View.GONE);
//        }
//        if (options != null && options.length > 0) {
//            for (int i = 0; i < options.length; i++) {
//                View item = LayoutInflater.from(mContext).inflate(R.layout.basetools_popup_item, null);
//                Button btn_txt = (Button) item.findViewById(R.id.btn_popup_option);
//                btn_txt.setText(options[i]);
//                if (Colors != null && Colors.length == options.length) {
//                    btn_txt.setTextColor(Colors[i]);
//                }
//                final int finalI = i;
//                btn_txt.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (itemClickListener != null) {
//                            itemClickListener.onItemClick(finalI);
//                        }
//                    }
//                });
//                lin_layout.addView(item);
//            }
//        }
//    }

    /**
     * 弹出Popupwindow
     */
    public void showPopupWindow() {
        View popupWindow_view = LayoutInflater.from(mContext).inflate(R.layout.payment_layout, null);
        LinearLayout yepay_ll=popupWindow_view.findViewById(R.id.yepay_ll);
        LinearLayout weixin_ll=popupWindow_view.findViewById(R.id.weixin_ll);
        final LinearLayout alipay_ll=popupWindow_view.findViewById(R.id.alipay_ll);
        Button cz_btn=popupWindow_view.findViewById(R.id.cz_btn);
        Button cancel_btn=popupWindow_view.findViewById(R.id.cancel_btn);
        final CheckBox weixin_check=popupWindow_view.findViewById(R.id.weixin_check);
        final CheckBox alipay_check=popupWindow_view.findViewById(R.id.alipay_check);
        final CheckBox yepay_check=popupWindow_view.findViewById(R.id.yepay_check);
        if (flag){
            yepay_ll.setVisibility(View.VISIBLE);
        }else{
            yepay_ll.setVisibility(View.GONE);
        }
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
                dismiss();
            }
        });
        cz_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alipay_check.isChecked()==true){
                    alipay();
                }else if(weixin_check.isChecked()==true){
                    Toast.makeText(mContext, "微信支付",
                            Toast.LENGTH_LONG).show();
                }else if(yepay_check.isChecked()==true){
                    Toast.makeText(mContext, "余额支付",
                            Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(mContext, "请选择支付方式",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        //添加子View
//        addView(popupWindow_view);
        mPopupWindow = new PopupWindow(popupWindow_view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setAnimationStyle(R.style.popwindow_anim_style);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setWindowAlpa(false);
            }
        });
        show(popupWindow_view);
    }
    //支付宝支付
    private void alipay(){
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask((Activity) mContext);
                Map<String, String> result = alipay.payV2(orderInfo,true);
                Message msg = new Message();
                msg.what = 1;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            Toast.makeText(mContext, msg.obj.toString(),
                    Toast.LENGTH_LONG).show();
        }
    };

    /**
     * 显示PopupWindow
     */
    private void show(View v) {
        if (mPopupWindow != null && !mPopupWindow.isShowing()) {
            mPopupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
        }
        setWindowAlpa(true);
    }


    /**
     * 消失PopupWindow
     */
    public void dismiss() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    /**
     * 动态设置Activity背景透明度
     *
     * @param isopen
     */
    public void setWindowAlpa(boolean isopen) {
        if (android.os.Build.VERSION.SDK_INT < 11) {
            return;
        }
        final Window window = ((Activity) mContext).getWindow();
        final WindowManager.LayoutParams lp = window.getAttributes();
        window.setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        ValueAnimator animator;
        if (isopen) {
            animator = ValueAnimator.ofFloat(1.0f, 0.5f);
        } else {
            animator = ValueAnimator.ofFloat(0.5f, 1.0f);
        }
        animator.setDuration(400);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                lp.alpha = alpha;
                window.setAttributes(lp);
            }
        });
        animator.start();
    }


    /**
     * 点击事件选择回调
     */
    public interface onPopupWindowItemClickListener {
        void onItemClick(int position);
    }
}
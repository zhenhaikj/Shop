package com.emjiayuan.app.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.et_username)
    EditText et_username;
    @BindView(R.id.et_yzm)
    EditText et_yzm;
    @BindView(R.id.get_yzm)
    TextView get_yzm;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.et_repassword)
    EditText et_repassword;
    @BindView(R.id.register)
    Button register;
    @BindView(R.id.no_yzm)
    TextView no_yzm;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        title.setText("注册");
    }

    @Override
    protected void setListener() {
        back.setOnClickListener(this);
        get_yzm.setOnClickListener(this);
        register.setOnClickListener(this);
        no_yzm.setOnClickListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(Message message) {
        Bundle bundle = message.getData();
        String mes = bundle.getString("message");
        switch (message.what) {
            case 0:
                MyUtils.showToast(this, mes);
                finish();
                break;
            case 1:
                MyUtils.showToast(this, mes);
                break;
        }

    }

    @Override
    public void onClick(View view) {
        if (!MyUtils.isFastClick()){
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.get_yzm:
                String username = et_username.getText().toString();
                if ("".equals(username)) {
                    MyUtils.showToast(RegisterActivity.this, "用户名不能为空！");
                    return;
                }
                TimeCount time = new TimeCount(60000, 1000);
                time.start();
                FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
                formBody.add("telphone", username);//传递键值对参数
                formBody.add("sendtype", "1");//传递键值对参数
//new call
                Call call = MyOkHttp.GetCall("system.sendUserSms", formBody);
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
                        Log.d("------获取验证码结果------", result);
                    }
                });
                break;
            case R.id.register:
                String name = et_username.getText().toString();
                String yzm = et_yzm.getText().toString();
                String password = et_password.getText().toString();
                String repassword = et_repassword.getText().toString();
                if ("".equals(name)) {
                    MyUtils.showToast(RegisterActivity.this, "用户名不能为空！");
                    return;
                }
                if ("".equals(yzm)) {
                    MyUtils.showToast(RegisterActivity.this, "请输入验证码！");
                    return;
                }
                if ("".equals(password)) {
                    MyUtils.showToast(RegisterActivity.this, "请输入密码！");
                    return;
                }
                if ("".equals(repassword)) {
                    MyUtils.showToast(RegisterActivity.this, "请确认密码！");
                    return;
                }
                if (!password.equals(repassword)) {
                    MyUtils.showToast(RegisterActivity.this, "两次输入密码不一致！");
                    return;
                }
                FormBody.Builder formBody2 = new FormBody.Builder();//创建表单请求体
                formBody2.add("username", name);//传递键值对参数
                formBody2.add("password", password);//传递键值对参数
                formBody2.add("confirmpassword", repassword);//传递键值对参数
                formBody2.add("smscode", yzm);//传递键值对参数
//new call
                Call call2 = MyOkHttp.GetCall("User.register", formBody2);
//请求加入调度
                call2.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("------------", e.toString());
//                        myHandler.sendEmptyMessage(1);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        Log.d("------注册结果------", result);
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");
                            String data = jsonObject.getString("data");
                            Bundle bundle = new Bundle();
                            bundle.putString("message", message);
                            Message mes = new Message();
                            mes.setData(bundle);
                            if ("200".equals(code)) {
                                mes.what = 0;
                                EventBus.getDefault().post(mes);
                            } else {
                                mes.what = 1;
                                EventBus.getDefault().post(mes);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case R.id.no_yzm:
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setIcon(android.R.drawable.ic_dialog_info);
                builder.setTitle("温馨提示");
                builder.setMessage("是否拨打客服电话4008123337？");
                builder.setCancelable(true);

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        Uri data = Uri.parse("tel:" + "4008123337");
                        intent.setData(data);
                        startActivity(intent);
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
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
//            get_yzm.setBackgroundColor(Color.parseColor("#B6B6D8"));
            if (get_yzm==null){
                return;
            }
            get_yzm.setClickable(false);
            get_yzm.setText(millisUntilFinished / 1000 + "s");
        }

        @Override
        public void onFinish() {
            if (get_yzm==null){
                return;
            }
            get_yzm.setText("重新获取验证码");
            get_yzm.setClickable(true);
//            get_yzm.setBackgroundColor(Color.parseColor("#4EB84A"));

        }
    }
}

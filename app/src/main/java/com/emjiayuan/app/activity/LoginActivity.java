package com.emjiayuan.app.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.MainActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.GlideUtil;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.Utils.SpUtils;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.entity.LoginResult;
import com.emjiayuan.app.event.LoginSuccessEvent;
import com.emjiayuan.app.widget.MyImageView;
import com.emjiayuan.app.widget.RatioImageView;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.et_username)
    EditText et_username;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.login)
    Button btn_login;
    @BindView(R.id.register)
    TextView register;
    @BindView(R.id.password_forget)
    TextView pwdforget;
    @BindView(R.id.service_ll)
    LinearLayout serviceLl;
    @BindView(R.id.line_top)
    View lineTop;
    @BindView(R.id.im_login)
    RatioImageView imLogin;
    private int type = 0;
    private SharedPreferences sp;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        title.setText("账号登录");
        Glide.with(mActivity).load(R.drawable.login_gif).into(imLogin);
//        et_username.setText("110");
//        et_password.setText("110");
//        et_username.setText("18767773654");
//        et_password.setText("123");
//        Intent intent=getIntent();
//        if (intent!=null){
//            type=intent.getIntExtra("type",0);
//        }

    }

    @Override
    protected void setListener() {
        back.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        register.setOnClickListener(this);
        pwdforget.setOnClickListener(this);
    }

    public void getDevice() {

        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("userid", Global.loginResult.getId());
        Log.d("------参数------", formBody.build().toString());
//new call
//        Call call = MyOkHttp.GetCall("public.appHome", formBody);
        Call call = MyOkHttp.GetCall("system.getDevice", formBody);
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
                MyUtils.e("------获取设备号结果------", result);
                Message message = new Message();
                message.what = 1;
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
                        if ("200".equals(code)) {
                            Global.loginResult = new Gson().fromJson(data, LoginResult.class);
                            //当用户使用自有账号登录时，可以这样统计：
                            MobclickAgent.onProfileSignIn(Global.loginResult.getId());
                            SpUtils.putObject(mActivity, "loginResult", Global.loginResult);
                            MyUtils.showToast(LoginActivity.this, message);

                            getDevice();
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            MyUtils.showToast(LoginActivity.this, message);
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
                        if ("200".equals(code)) {
                            Global.device_no = data;
                            SpUtils.putString(mActivity, "device_no", data);
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
    public void onClick(View view) {
        if (!MyUtils.isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.login:

                String username = et_username.getText().toString();
                String password = et_password.getText().toString();
                if ("".equals(username)) {
                    MyUtils.showToast(LoginActivity.this, "用户名不能为空！");
                    return;
                } else if ("".equals(password)) {
                    MyUtils.showToast(LoginActivity.this, "密码不能为空！");
                    return;
                }
                FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
                formBody.add("username", username);//传递键值对参数
                formBody.add("password", password);//传递键值对参数
//new call
                Call call = MyOkHttp.GetCall("User.login", formBody);
//请求加入调度
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("------------", e.toString());
                        myHandler.sendEmptyMessage(1);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        Message message = new Message();
                        message.what = 0;
                        Bundle bundle = new Bundle();
                        bundle.putString("result", result);
                        message.setData(bundle);
                        myHandler.sendMessage(message);
                        Log.d("------------", result);
//                        Log.d("------------",MyUtils.decodeUnicode(response.body().string()));
                        Log.d("------------", result);
//                        MyUtils.showToast(LoginActivity.this, response.body().string());
                    }
                });
                break;
            case R.id.register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.password_forget:
                startActivity(new Intent(this, PwdForgetActivity.class));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Global.loginResult == null) {
            startActivity(new Intent(mActivity, MainActivity.class));
        } else {
            EventBus.getDefault().post(new LoginSuccessEvent("update"));
        }
    }
}

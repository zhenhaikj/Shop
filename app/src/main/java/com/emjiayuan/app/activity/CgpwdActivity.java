package com.emjiayuan.app.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.entity.Global;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class CgpwdActivity extends BaseActivity {


    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.service_ll)
    LinearLayout serviceLl;
    @BindView(R.id.line_top)
    View lineTop;
    @BindView(R.id.old_pwd)
    EditText oldPwd;
    @BindView(R.id.new_pwd)
    EditText newPwd;
    @BindView(R.id.re_pwd)
    EditText rePwd;
    private String oldpassword;
    private String newpassword;
    private String confirmpassword;
    @Override
    protected int setLayoutId() {
        return R.layout.activity_cgpwd;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        title.setText("修改密码");
        save.setText("确认修改");
        save.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyUtils.isFastClick()) {
                    return;
                }
                finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyUtils.isFastClick()) {
                    return;
                }
                oldpassword=oldPwd.getText().toString();
                newpassword=newPwd.getText().toString();
                confirmpassword=rePwd.getText().toString();
                if ("".equals(oldpassword)){
                    MyUtils.showToast(mActivity,"请输入当前密码！");
                    return;
                }
                if ("".equals(newpassword)){
                    MyUtils.showToast(mActivity,"请输入新密码！");
                    return;
                }
                if ("".equals(confirmpassword)){
                    MyUtils.showToast(mActivity,"请确认密码！");
                    return;
                }
                if (!newpassword.equals(confirmpassword)){
                    MyUtils.showToast(mActivity,"两次输入密码不一致！");
                    return;
                }
                editUserPassword();
            }
        });
    }

    @Override
    protected void setListener() {

    }


    public void editUserPassword() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("userid", Global.loginResult.getId());
        formBody.add("oldpassword", oldpassword);
        formBody.add("newpassword", newpassword);
        formBody.add("confirmpassword", confirmpassword);

//new call
        Call call = MyOkHttp.GetCall("user.editUserPassword", formBody);
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
                MyUtils.e("------修改用户密码------", result);
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
                        if ("200".equals(code)) {
                            MyUtils.showToast(mActivity, message);
                            finish();
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

}

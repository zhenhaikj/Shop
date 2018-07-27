package com.emjiayuan.app.activity.address;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.AddressPickTask;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.event.AddressEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import cn.addapp.pickers.entity.City;
import cn.addapp.pickers.entity.County;
import cn.addapp.pickers.entity.Province;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class AddAddressActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.default_ll)
    LinearLayout default_ll;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_ssq)
    EditText et_ssq;
    @BindView(R.id.et_detailed)
    EditText et_detailed;
    @BindView(R.id.line_top)
    View lineTop;
    @BindView(R.id.check)
    CheckBox check;
    @BindView(R.id.tv_default)
    TextView tvDefault;
    private String is_default="0";
    private String provinceName;
    private String cityName;
    private String districtName;
    private String ssq;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_add_address;
    }

    @Override
    protected void initData() {
        title.setText("添加地址");
        save.setVisibility(View.VISIBLE);
    }
    public void onAddressPicker() {
        AddressPickTask task = new AddressPickTask(this);
        task.setHideProvince(false);
        task.setHideCounty(false);
        task.setCallback(new AddressPickTask.Callback() {
            @Override
            public void onAddressInitFailed() {
                MyUtils.showToast(mActivity,"数据初始化失败");
            }

            @Override
            public void onAddressPicked(Province province, City city, County county) {
                if (county == null) {
                   ssq=province.getAreaName() +" "+ city.getAreaName();
                } else {
                    ssq=province.getAreaName() +" "+ city.getAreaName() +" "+ county.getAreaName();
                }
                et_ssq.setText(ssq);
                provinceName=province.getAreaName();
                cityName=city.getAreaName();
                districtName=county.getAreaName();
            }
        });
        if (provinceName!=null){
            task.execute(provinceName, cityName, districtName);
        }else{
            task.execute("浙江", "宁波市", "镇海区");
        }

    }
    @Override
    protected void initView() {

    }

    @Override
    protected void setListener() {
        back.setOnClickListener(this);
        save.setOnClickListener(this);
        default_ll.setOnClickListener(this);
        et_ssq.setOnClickListener(this);
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    is_default="1";
                }else{
                    is_default="0";
                }
            }
        });
    }
    public void getDatas(){
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
//new call
        Call call = MyOkHttp.GetCall("public.getAddressAndroid", formBody);
//请求加入调度
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("------------", e.toString());
//                        myHandler.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d("------获取城市结果------", result);
                Message message = new Message();
                message.what = 1;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
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
            case R.id.et_ssq:
                getDatas();

                break;
            case R.id.save:
                String name = et_name.getText().toString();
                String phone = et_phone.getText().toString();
                String ssq = et_ssq.getText().toString();
                String detailed = et_detailed.getText().toString().replaceAll(" ","");
                if ("".equals(name)) {
                    MyUtils.showToast(this, "请输入收货人！");
                    return;
                }
                if ("".equals(phone)) {
                    MyUtils.showToast(this, "请输入联系电话！");
                    return;
                }
                if (phone.length()<11){
                    MyUtils.showToast(this, "请输入11位手机号码！");
                    return;
                }
                if ("".equals(ssq)) {
                    MyUtils.showToast(this, "请输入省市县！");
                    return;
                }
                if ("".equals(detailed)) {
                    MyUtils.showToast(this, "请输入详细地址！");
                    return;
                }
                FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
                formBody.add("userid", Global.loginResult.getId());//传递键值对参数
                formBody.add("username", name);//传递键值对参数
                formBody.add("telphone", phone);//传递键值对参数
                formBody.add("address", ssq+" "+detailed);//传递键值对参数
                formBody.add("shengfen", provinceName);//传递键值对参数
                formBody.add("default", is_default);//传递键值对参数
//new call
                Call call = MyOkHttp.GetCall("userAddress.addAddress", formBody);
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
                        Log.d("------添加地址结果------", result);
                        Message message = new Message();
                        message.what = 0;
                        Bundle bundle = new Bundle();
                        bundle.putString("result", result);
                        message.setData(bundle);
                        myHandler.sendMessage(message);
                    }
                });
                break;
            case R.id.default_ll:
//                MyUtils.showToast(this,"已设为默认");
                break;

        }
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
                            MyUtils.showToast(AddAddressActivity.this, message);
                            EventBus.getDefault().post(new AddressEvent());
                            finish();
                        } else {
                            MyUtils.showToast(AddAddressActivity.this, message);
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
                        Global.datas = new ArrayList<>();
                        if ("200".equals(code)) {
//                        String json = ConvertUtils.toString(activity.getAssets().open("city.json"));
                            Global.datas.addAll(JSON.parseArray(data, Province.class));
                            onAddressPicker();
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
}

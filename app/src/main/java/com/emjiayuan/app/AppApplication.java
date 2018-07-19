package com.emjiayuan.app;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.Utils.SpUtils;
import com.emjiayuan.app.activity.CityActivity;
import com.emjiayuan.app.banner.GlideImageLoader2;
import com.emjiayuan.app.entity.CityBean;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.lwkandroid.stateframelayout.StateFrameLayout;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.qiyukf.unicorn.api.StatusBarNotificationConfig;
import com.qiyukf.unicorn.api.Unicorn;
import com.qiyukf.unicorn.api.UnicornImageLoader;
import com.qiyukf.unicorn.api.YSFOptions;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.emjiayuan.app.entity.AreaModel;
import com.emjiayuan.app.entity.Global;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

/**
 * 描述：
 * 作者：HMY
 * 时间：2016/5/13
 */
public class AppApplication extends Application {

    private String resultString;
    private boolean first=true;



    static {//static 代码段可以防止内存泄露
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                MaterialHeader header=new MaterialHeader(context);
               header.setPrimaryColors(Color.parseColor("#00000000"));
               header.setShowBezierWave(true);
                return header;//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
    }


    @Override
    public void onCreate() {
        super.onCreate();
        //腾讯信鸽开启debug日志数据
        XGPushConfig.enableDebug(this,true);
        //token注册
        XGPushManager.registerPush(this, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
//token在设备卸载重装的时候有可能会变
                Log.d("TPush", "注册成功，设备token为：" + data);
                System.out.print("注册成功，设备token为：" + data);
                Global.token=data.toString();
            }
            @Override
            public void onFail(Object data, int errCode, String msg) {
                Log.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
            }
        });
        //注意在3.2.2 版本信鸽对账号绑定和解绑接口进行了升级具体详情请参考API文档。
        XGPushManager.bindAccount(getApplicationContext(), "XINGE");
        XGPushManager.setTag(this,"XINGE");

        // appKey 可以在七鱼管理系统->设置->APP接入 页面找到
        Unicorn.init(this, "1b5d0202117baa53c3c796075d043590", options(), new GlideImageLoader2(getApplicationContext()));
        initImageLoader();
//        getCityList();
        request();
        Global.loginResult= SpUtils.getObject(getApplicationContext(),"loginResult");
        //读取已保存的device_no
        Global.device_no = SpUtils.getString(getApplicationContext(),"device_no");

    }
    // 如果返回值为null，则全部使用默认参数。
    private YSFOptions options() {
        YSFOptions options = new YSFOptions();
        options.statusBarNotificationConfig = new StatusBarNotificationConfig();
        return options;
    }

    private void initImageLoader() {
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);

    }
    private void getCityList() {
        Runnable run = new Runnable() {

            @Override
            public void run() {
                readTextFromSDcard();
                try {
                    Global.city_list=new ArrayList<AreaModel>();
                    JSONArray jsonArray=new JSONArray(resultString);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String name=jsonObject.getString("name");
                        String code=jsonObject.getString("CityID");
                        AreaModel model=new AreaModel(code,name);
                        Global.city_list.add(model);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Collections.sort(Global.city_list);
            }
        };
        new Thread(run).start();
    }

    //将传入的is一行一行解析读取出来出来
    private void readTextFromSDcard() {
        InputStreamReader inputStreamReader;
        try {
            inputStreamReader = new InputStreamReader(getAssets().open("city_json.txt"), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(
                    inputStreamReader);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            inputStreamReader.close();
            bufferedReader.close();
            resultString = stringBuilder.toString();
            Log.i("TAG", stringBuilder.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void request() {

        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        Log.d("------参数------", formBody.build().toString());
        formBody.add("addresscode","SORT");
//new call
//        Call call = MyOkHttp.GetCall("public.appHome", formBody);
        Call call = MyOkHttp.GetCall("public.getAddress", formBody);
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
                MyUtils.e("------获取地址结果------", result);
                Message message = new Message();
                message.what = 0;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }



    Handler myHandler=new Handler(){
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
                        Gson gson=new Gson();
                        Global.city_list2=new ArrayList<>();
                        if ("200".equals(code)) {
                            JSONArray jsonArray=new JSONArray(data);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Global.city_list2.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), CityBean.class));
                            }
                        } else {
                            MyUtils.showToast(getApplicationContext(),message);
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
                        if ("200".equals(code)) {
                            Global.device_no=data;
                            SpUtils.putString(getApplicationContext(),"device_no",data);
                        } else {
                            MyUtils.showToast(getApplicationContext(),message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };


}

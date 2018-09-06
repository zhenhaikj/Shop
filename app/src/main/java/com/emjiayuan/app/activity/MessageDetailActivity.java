package com.emjiayuan.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.adapter.LogisticsAdapter;
import com.emjiayuan.app.entity.LogisticsBean;
import com.emjiayuan.app.entity.News;
import com.google.gson.Gson;
import com.youth.banner.Banner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class MessageDetailActivity extends BaseActivity {

    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.webview)
    WebView webview;
    private String newsid;
    private News news;

    @Override
    protected int setLayoutId() {
        return R.layout.message_detail;
    }

    @Override
    protected void initData() {

    }

    public void getDetail() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体

        formBody.add("newsid", newsid);

        Log.d("------参数------", formBody.build().toString());
//new call
        Call call = MyOkHttp.GetCall("news.getNewsDetail", formBody);
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
                MyUtils.e("------获取新闻详情------", result);
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
                        Gson gson=new Gson();
                        if ("200".equals(code)) {
                            news=gson.fromJson(data, News.class);
                            titleTv.setText(news.getTitle());
                            time.setText(news.getCreatetime());
                            String html = "<!DOCTYPE html>\n" +
                                    "<html lang=\"en\">\n" +
                                    "<head>\n" +
                                    "\t<meta charset=\"utf-8\">\n" +
                                    "\t<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                                    "\t\n" +
                                    "\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                                    "\n" +
                                    "\t<title>detail</title>\n" +
                                    "\n" +
                                    "\t<style>body{border:0;padding:0;margin:0;}img{border:0;display:block;vertical-align: middle;padding:0;margin:0;}p{border:0;padding:0;margin:0;}div{border:0;padding:0;margin:0;}</style>\n" +
                                    "</head>"
                                    + "<body>"
                                    + news.getContent() + "</body>" + "</html>";

                            webview.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
                            webview.getSettings().setJavaScriptEnabled(true);
                            webview.setWebChromeClient(new WebChromeClient());
//        content.setText(Html.fromHtml(news.getContent()));
                        } else {
                            MyUtils.showToast(mActivity, message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
//                    MyUtils.showToast(LoginActivity.this,result);
                    break;
            }
        }
    };
    @Override
    protected void initView() {
        final boolean flag= getIntent().getBooleanExtra("flag",false);
        newsid=getIntent().getStringExtra("newsid");
        getDetail();
        title.setText("伊穆头条");
        save.setText("查看更多");
        save.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyUtils.isFastClick()){
                    return;
                }
                finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyUtils.isFastClick()){
                    return;
                }
                if (flag){
                    finish();
                }else{
                    Intent intent=new Intent(mActivity,MessageActivity.class);
//                    intent.putExtra("news",newslist);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void setListener() {

    }
}

package com.emjiayuan.app.activity;

import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.entity.News;
import com.youth.banner.Banner;

import java.util.ArrayList;

import butterknife.BindView;

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
    private ArrayList<News> newslist;

    @Override
    protected int setLayoutId() {
        return R.layout.message_detail;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        newslist= (ArrayList<News>) getIntent().getSerializableExtra("news");
        final boolean flag= getIntent().getBooleanExtra("flag",false);
        News news=newslist.get(getIntent().getIntExtra("position",-1));
        title.setText("伊穆头条");
        save.setText("查看更多");
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
                    intent.putExtra("news",newslist);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void setListener() {

    }
}

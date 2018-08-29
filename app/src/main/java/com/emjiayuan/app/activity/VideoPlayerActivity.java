package com.emjiayuan.app.activity;

import android.content.Context;
import android.content.Intent;

import com.bumptech.glide.Glide;
import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.R;

import butterknife.BindView;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

public class VideoPlayerActivity extends BaseActivity {


//    @BindView(R.id.videoplayer)
//    JZVideoPlayerStandard videoplayer;

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JZVideoPlayer.releaseAllVideos();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_videoplayer;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        JZVideoPlayerStandard videoplayer= (JZVideoPlayerStandard) findViewById(R.id.videoplayer);
        videoplayer.setUp(url, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "sssss");
        Glide.with(mActivity).load(url).into(videoplayer.thumbImageView);
    }

    @Override
    protected void setListener() {

    }

    /***
     * 启动播放视频
     * @param   context context
     * @param  url url
     * ***/
    public static void startActivity(Context context, String url) {
        Intent intent = new Intent(context, VideoPlayerActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }
}

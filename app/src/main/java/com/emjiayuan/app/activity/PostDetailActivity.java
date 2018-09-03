package com.emjiayuan.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.GlideUtil;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.entity.PhotoInfo;
import com.emjiayuan.app.entity.Post;
import com.emjiayuan.app.widget.CommentListView;
import com.emjiayuan.app.widget.FlowLayout;
import com.emjiayuan.app.widget.MultiImageView;
import com.google.gson.Gson;
import com.previewlibrary.GPreviewBuilder;
import com.previewlibrary.loader.OnLongClickListener;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class PostDetailActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {


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
    @BindView(R.id.icon)
    CircleImageView icon;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.multiimageview)
    MultiImageView multiimageview;
    @BindView(R.id.label)
    TextView label;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.zan_icon)
    ImageView zanIcon;
    @BindView(R.id.zan_count)
    TextView zanCount;
    @BindView(R.id.pl_count)
    TextView plCount;

    @BindView(R.id.commentList)
    CommentListView commentList;
    @BindView(R.id.digCommentBody)
    LinearLayout digCommentBody;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.ll_post)
    LinearLayout llPost;
    @BindView(R.id.et_pl)
    EditText etPl;
    @BindView(R.id.send)
    TextView send;
    @BindView(R.id.flow)
    FlowLayout flow;
    @BindView(R.id.prase_ll)
    LinearLayout praseLl;
    @BindView(R.id.views)
    TextView views;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.ll_time)
    LinearLayout llTime;
    @BindView(R.id.ll_share)
    LinearLayout llShare;
    @BindView(R.id.ll_letter)
    LinearLayout llLetter;
    @BindView(R.id.ll_pl)
    LinearLayout llPl;
    @BindView(R.id.ll_zan)
    LinearLayout llZan;
    @BindView(R.id.videoplayer)
    JZVideoPlayerStandard videoplayer;
    @BindView(R.id.soud_img)
    ImageView soudImg;
    @BindView(R.id.second)
    TextView second;
    @BindView(R.id.audio_ll)
    LinearLayout audioLl;

    private String weiboid;
    private Post post;
    private int reply = 0;//0评论1回复某人评论
    Post.ReplylistBean bean;
    List<String> PraiseList = new ArrayList<>();
    private CustomShareListener mShareListener;
    private ShareAction mShareAction;
    private PopupWindow mPopupWindow;
    private MediaPlayer mPlayer;
    public AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {//是否新建个class，代码更规矩，并且变量的位置也很尴尬
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    if (mPlayer!=null){
                        mPlayer.stop();
                        mPlayer.release();
                        mPlayer=null;
                        Glide.with(mActivity).asBitmap().load(R.drawable.soud).into(soudImg);
                    }
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    if(mPlayer!=null){
                        mPlayer.stop();
                        mPlayer.release();
                        mPlayer=null;
                        Glide.with(mActivity).asBitmap().load(R.drawable.soud).into(soudImg);
                    }
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    break;
            }
        }
    };
    private AudioManager mAudioManager;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_post_detail;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        title.setText("帖子详情");
        Intent intent = getIntent();
        if (intent != null) {
            weiboid = intent.getStringExtra("weiboid");
            getWeiboDetail();
        }

    }

    public void setdata() {
        GlideUtil.loadImageViewLoding(mActivity, post.getHeadimg(), icon, R.drawable.default_tx, R.drawable.default_tx);
        username.setText(post.getNickname());
        content.setText(post.getContent());
        plCount.setText(post.getPinglun());
        zanCount.setText(post.getZan());
        time.setText(post.getPasttime());
        views.setText(post.getVisit_pv());
        address.setText(post.getAddress());
        address.setVisibility((post.getAddress() == null ? View.INVISIBLE : View.VISIBLE));
        llLetter.setVisibility((post.getUserid().equals(Global.loginResult.getId()) ? View.GONE : View.VISIBLE));
        label.setText(post.getWeibotype());
        PraiseList = new ArrayList<>();
        for (int i = 0; i < post.getZanlist().size(); i++) {
            PraiseList.add(post.getZanlist().get(i).getHeadimg());
        }
        for (int i = 0; i < 50; i++) {
//            PraiseList.add(post.getZanlist().get(0).getHeadimg());
        }
        flow.setUrls(PraiseList);
//        praiseListView.setDatas(post.getZanlist());
        commentList.setDatas(post.getReplylist());
        if (post.getShowtype()==3){
            videoplayer.setVisibility(View.VISIBLE);
            content.setVisibility(View.VISIBLE);
            multiimageview.setVisibility(View.GONE);
            audioLl.setVisibility(View.GONE);
            videoplayer.setUp(post.getVideo(), JZVideoPlayer.SCREEN_WINDOW_NORMAL,"");
            Glide.with(mActivity).load(post.getVideo()).into(videoplayer.thumbImageView);
        }else if (post.getShowtype()==2){
            videoplayer.setVisibility(View.GONE);
            content.setVisibility(View.GONE);
            multiimageview.setVisibility(View.GONE);
            audioLl.setVisibility(View.VISIBLE);
            second.setText(post.getAudio().substring(post.getAudio().lastIndexOf("_")+1,post.getAudio().lastIndexOf("."))+"''");
            audioLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mPlayer==null){
                        mPlayer=new MediaPlayer();
                    }
                    if (mPlayer.isPlaying()){
                        mAudioManager.abandonAudioFocus(onAudioFocusChangeListener);
                        mPlayer.stop();
                        Glide.with(mActivity).asBitmap().load(R.drawable.soud).into(soudImg);
                    }else{
                        try {
                            mPlayer.reset();
                            mPlayer.setDataSource(post.getAudio());
                            mPlayer.prepare();
                            mPlayer.start();
                            mAudioManager = (AudioManager) mActivity.getSystemService(Context.AUDIO_SERVICE);
                            mAudioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                            Glide.with(mActivity).asGif().load(R.drawable.soud).into(soudImg);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            mAudioManager.abandonAudioFocus(onAudioFocusChangeListener);
                            mPlayer.stop();
                            mPlayer.release();
                            mPlayer=null;
                            Glide.with(mActivity).asBitmap().load(R.drawable.soud).into(soudImg);
                        }
                    });
                }
            });

        }else{
            videoplayer.setVisibility(View.GONE);
            content.setVisibility(View.VISIBLE);
            multiimageview.setVisibility(View.VISIBLE);
            audioLl.setVisibility(View.GONE);
            final List<PhotoInfo> photos = new ArrayList<PhotoInfo>();
            if (post.getImages().size() == 1) {
                Glide.with(mActivity).load(post.getImages().get(0)).into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        photos.add(new PhotoInfo(post.getImages().get(0), "", resource.getIntrinsicWidth(), resource.getIntrinsicHeight()));
                        multiimageview.setList(photos);
                    }
                });
            } else {
                for (int i = 0; i < post.getImages().size(); i++) {
                    photos.add(new PhotoInfo(post.getImages().get(i), "", 0, 0));
                }
                multiimageview.setList(photos);
            }
            multiimageview.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (!MyUtils.isFastClick()) {
                        return;
                    }
                    //imagesize是作为loading时的图片size
//                ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());
//                ImagePagerActivity.startImagePagerActivity(mActivity, post.getImages(), position, imageSize);
                    List<View> viewList = new ArrayList<>();
                    for (int i = 0; i < multiimageview.getChildCount(); i++) {
                        View itemView = multiimageview.getChildAt(i);
                        if (itemView != null) {
                            if (itemView instanceof LinearLayout) {
                                LinearLayout thumbView = (LinearLayout) itemView;
                                for (int j = 0; j < thumbView.getChildCount(); j++) {
                                    View iconView = thumbView.getChildAt(j);
                                    viewList.add(iconView);
                                }

                            } else {
                                viewList.add(itemView);
                            }
                        }
                    }
                    for (int j = 0; j < viewList.size(); j++) {
                        Rect bounds = new Rect();
                        viewList.get(j).getGlobalVisibleRect(bounds);
                        photos.get(j).setStatusBarH(MyUtils.getStatusHeight(mActivity));
                        photos.get(j).setmBounds(bounds);
                        photos.get(j).setUrl(photos.get(j).getUrl());
                    }
                    GPreviewBuilder.from((Activity) mActivity)
                            .to(CustomImagePreviewActivity.class)
                            .setData(photos)
                            .setCurrentIndex(position)
                            .setType(GPreviewBuilder.IndicatorType.Number)
                            .start();//启动
                }
            });
        }
//        multiimageview.setList(createPhotos(post.getImages()));

        if (post.getIszan() == 1) {
            zanIcon.setSelected(true);
        } else {
            zanIcon.setSelected(false);
        }
        if (post.hasFavort()) {
            praseLl.setVisibility(View.VISIBLE);
        } else {
            praseLl.setVisibility(View.GONE);
        }
        if (post.hasComment()) {
            digCommentBody.setVisibility(View.VISIBLE);
        } else {
            digCommentBody.setVisibility(View.GONE);
        }
    }

    public List<PhotoInfo> createPhotos(final List<String> list) {
        /*PhotoInfo p1 = new PhotoInfo();
        p1.url = "http://f.hiphotos.baidu.com/image/pic/item/faf2b2119313b07e97f760d908d7912396dd8c9c.jpg";
        p1.w = 640;
        p1.h = 792;

        PhotoInfo p2 = new PhotoInfo();
        p2.url = "http://g.hiphotos.baidu.com/image/pic/item/4b90f603738da977c76ab6fab451f8198718e39e.jpg";
        p2.w = 640;
        p2.h = 792;

        PhotoInfo p3 = new PhotoInfo();
        p3.url = "http://e.hiphotos.baidu.com/image/pic/item/902397dda144ad343de8b756d4a20cf430ad858f.jpg";
        p3.w = 950;
        p3.h = 597;

        PhotoInfo p4 = new PhotoInfo();
        p4.url = "http://a.hiphotos.baidu.com/image/pic/item/a6efce1b9d16fdfa0fbc1ebfb68f8c5495ee7b8b.jpg";
        p4.w = 533;
        p4.h = 800;

        PhotoInfo p5 = new PhotoInfo();
        p5.url = "http://b.hiphotos.baidu.com/image/pic/item/a71ea8d3fd1f4134e61e0f90211f95cad1c85e36.jpg";
        p5.w = 700;
        p5.h = 467;

        PhotoInfo p6 = new PhotoInfo();
        p6.url = "http://c.hiphotos.baidu.com/image/pic/item/7dd98d1001e939011b9c86d07fec54e737d19645.jpg";
        p6.w = 700;
        p6.h = 467;

        PhotoInfo p7 = new PhotoInfo();
        p7.url = "http://pica.nipic.com/2007-10-17/20071017111345564_2.jpg";
        p7.w = 1024;
        p7.h = 640;

        PhotoInfo p8 = new PhotoInfo();
        p8.url = "http://pic4.nipic.com/20091101/3672704_160309066949_2.jpg";
        p8.w = 1024;
        p8.h = 768;

        PhotoInfo p9 = new PhotoInfo();
        p9.url = "http://pic4.nipic.com/20091203/1295091_123813163959_2.jpg";
        p9.w = 1024;
        p9.h = 640;

        PhotoInfo p10 = new PhotoInfo();
        p10.url = "http://pic31.nipic.com/20130624/8821914_104949466000_2.jpg";
        p10.w = 1024;
        p10.h = 768;
        List<PhotoInfo> PHOTOS = new ArrayList<PhotoInfo>();
        PHOTOS.add(p1);
        PHOTOS.add(p2);
        PHOTOS.add(p3);
        PHOTOS.add(p4);
        PHOTOS.add(p5);
        PHOTOS.add(p6);
        PHOTOS.add(p7);
        PHOTOS.add(p8);
        PHOTOS.add(p9);
        PHOTOS.add(p10);
        List<PhotoInfo> photos = new ArrayList<PhotoInfo>();
        int size = getRandomNum(PHOTOS.size());
        if (size > 0) {
            if (size > 9) {
                size = 9;
            }
            for (int i = 0; i < size; i++) {
                PhotoInfo photo = PHOTOS.get(getRandomNum(PHOTOS.size()));
                if (!photos.contains(photo)) {
                    photos.add(photo);
                } else {
                    i--;
                }
            }
        }*/
        final List<PhotoInfo> photos = new ArrayList<PhotoInfo>();
        for (int i = 0; i < list.size(); i++) {
            final int finalI = i;
            Glide.with(mActivity).load(list.get(i)).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                    photos.add(new PhotoInfo(list.get(finalI), "", resource.getIntrinsicWidth(), resource.getIntrinsicHeight()));
                }
            });
        }
        return photos;
    }

    @Override
    protected void setListener() {
        back.setOnClickListener(this);
        save.setOnClickListener(this);
        send.setOnClickListener(this);
        llZan.setOnClickListener(this);
        llPl.setOnClickListener(this);
        llShare.setOnClickListener(this);
        llLetter.setOnClickListener(this);
        commentList.setOnItemClickListener(new CommentListView.OnItemClickListener() {
            @Override
            public void onItemClick(int commentPosition) {
                bean = post.getReplylist().get(commentPosition);
                if (bean.getUserid().equals(Global.loginResult.getId())) {
                    showPopupWindow(bean.getContent(), bean.getId());
                } else {
                    etPl.setHint("回复" + bean.getNickname());
                    reply = 1;
                }
            }
        });
        commentList.setOnItemLongClickListener(new CommentListView.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                bean = post.getReplylist().get(position);
                showPopupWindow(bean.getContent(), bean.getId());
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (!MyUtils.isFastClick()) {
            return;
        }

    }

    @Override
    public void onClick(View view) {
        if (!MyUtils.isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ll_zan:
                zanWeibo();
                break;
            case R.id.ll_share:
                mShareListener = new CustomShareListener((PostDetailActivity) mActivity);
                /*增加自定义按钮的分享面板*/
                mShareAction = new ShareAction((Activity) mActivity).setDisplayList(
                        SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE,
                        SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.MORE)
                        .addButton("复制文本", "复制文本", "umeng_socialize_copy", "umeng_socialize_copy")
                        .addButton("复制链接", "复制链接", "umeng_socialize_copyurl", "umeng_socialize_copyurl")
                        .setShareboardclickCallback(new ShareBoardlistener() {
                            @Override
                            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                                if (snsPlatform.mShowWord.equals("复制文本")) {
                                    Toast.makeText(mActivity, "已复制", Toast.LENGTH_LONG).show();
                                } else if (snsPlatform.mShowWord.equals("复制链接")) {
                                    Toast.makeText(mActivity, "已复制", Toast.LENGTH_LONG).show();

                                } else {
                                    UMWeb web = new UMWeb("https://h5.youzan.com/wscshop/feature/ihODHu0rwF?redirect_count=1");
                                    web.setTitle("伊穆家园");
                                    web.setDescription(post.getContent());
                                    web.setThumb(new UMImage(mActivity, R.drawable.start_icon));
                                    new ShareAction((Activity) mActivity).withMedia(web)
                                            .setPlatform(share_media)
                                            .setCallback(mShareListener)
                                            .share();
                                }
                            }
                        });
                mShareAction.open();
                break;
            case R.id.ll_letter:
                reply = 2;
                etPl.setHint("私信" + post.getNickname());
                break;
            case R.id.ll_pl:
                reply = 0;
                etPl.setHint("可以留言哦~");
                break;
            case R.id.send:
                String content = etPl.getText().toString();
                if ("".equals(content)) {
                    MyUtils.showToast(mActivity, "请输入内容！");
                    return;
                }
                if (reply == 0) {
                    addWeiboReply(content, "");
                } else if (reply == 2) {
                    addWeiboLetter(post.getUserid(), content);
                } else {
                    addWeiboReply(content, bean.getUserid());
                }
                break;
        }
    }

    /**
     * 帖子点赞
     */
    public void zanWeibo() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("weiboid", weiboid);//传递键值对参数
        formBody.add("userid", Global.loginResult.getId());//传递键值对参数
//new call
        Call call = MyOkHttp.GetCall("weibo.zanWeibo", formBody);
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
                MyUtils.e("------点赞------", result);
                Message message = new Message();
                message.what = 0;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    /**
     * 评论
     */
    public void addWeiboReply(String content, String rid) {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("weiboid", weiboid);//传递键值对参数
        formBody.add("userid", Global.loginResult.getId());//传递键值对参数
        formBody.add("content", content);//传递键值对参数
        formBody.add("rid", rid);//传递键值对参数
//new call
        Call call = MyOkHttp.GetCall("weibo.addWeiboReply", formBody);
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
                MyUtils.e("------评论------", result);
                Message message = new Message();
                message.what = 1;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    /**
     * 帖子删评论
     */
    public void removeWeiboReply(String replyid) {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("replyid", replyid);//传递键值对参数
//new call
        Call call = MyOkHttp.GetCall("weibo.removeWeiboReply", formBody);
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
                MyUtils.e("------删评论------", result);
                Message message = new Message();
                message.what = 0;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    /**
     * 私信
     */
    public void addWeiboLetter(String touserid, String content) {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("touserid", touserid);//传递键值对参数
        formBody.add("content", content);//传递键值对参数
        formBody.add("fromuserid", Global.loginResult.getId());//传递键值对参数
//new call
        Call call = MyOkHttp.GetCall("weibo.addWeiboLetter", formBody);
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
                MyUtils.e("------私信------", result);
                Message message = new Message();
                message.what = 1;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    /**
     * 详情
     */
    public void getWeiboDetail() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("weiboid", weiboid);//传递键值对参数
        formBody.add("loginuserid", Global.loginResult.getId());//传递键值对参数
//new call
        Call call = MyOkHttp.GetCall("weibo.getWeiboDetail", formBody);
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
                MyUtils.e("------帖子详情------", result);
                Message message = new Message();
                message.what = 2;
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

                case 0://点赞
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            MyUtils.showToast(mActivity, message);
                            getWeiboDetail();
                        } else {
                            MyUtils.showToast(mActivity, message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 1://评论
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            MyUtils.showToast(mActivity, message);
                            etPl.setText("");
                            MyUtils.hideSoftInput(mActivity, etPl);
//                            if (reply!=1){
                            getWeiboDetail();
//                            }
                        } else {
                            MyUtils.showToast(mActivity, message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2://详情
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            post = gson.fromJson(data, Post.class);
                            setdata();
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

    private static class CustomShareListener implements UMShareListener {

        private WeakReference<PostDetailActivity> mActivity;

        private CustomShareListener(PostDetailActivity activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {

            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(mActivity.get(), platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                        && platform != SHARE_MEDIA.EMAIL
                        && platform != SHARE_MEDIA.FLICKR
                        && platform != SHARE_MEDIA.FOURSQUARE
                        && platform != SHARE_MEDIA.TUMBLR
                        && platform != SHARE_MEDIA.POCKET
                        && platform != SHARE_MEDIA.PINTEREST

                        && platform != SHARE_MEDIA.INSTAGRAM
                        && platform != SHARE_MEDIA.GOOGLEPLUS
                        && platform != SHARE_MEDIA.YNOTE
                        && platform != SHARE_MEDIA.EVERNOTE) {
                    Toast.makeText(mActivity.get(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                }

            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                    && platform != SHARE_MEDIA.EMAIL
                    && platform != SHARE_MEDIA.FLICKR
                    && platform != SHARE_MEDIA.FOURSQUARE
                    && platform != SHARE_MEDIA.TUMBLR
                    && platform != SHARE_MEDIA.POCKET
                    && platform != SHARE_MEDIA.PINTEREST

                    && platform != SHARE_MEDIA.INSTAGRAM
                    && platform != SHARE_MEDIA.GOOGLEPLUS
                    && platform != SHARE_MEDIA.YNOTE
                    && platform != SHARE_MEDIA.EVERNOTE) {
                Toast.makeText(mActivity.get(), platform + " 分享失败啦", Toast.LENGTH_SHORT).show();

            }

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {

            Toast.makeText(mActivity.get(), platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 屏幕横竖屏切换时避免出现window leak的问题
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mShareAction.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPlayer!=null){
            mPlayer.release();
            mPlayer=null;
            return;
        }
        JZVideoPlayer.releaseAllVideos();
        if (mAudioManager!=null){
            mAudioManager.abandonAudioFocus(onAudioFocusChangeListener);
        }
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    /**
     * 弹出Popupwindow
     */
    public void showPopupWindow(final String content, final String replyid) {
        View popupWindow_view = LayoutInflater.from(mActivity).inflate(R.layout.pop_layout, null);
        Button save_btn = popupWindow_view.findViewById(R.id.save_btn);
        Button zxing_btn = popupWindow_view.findViewById(R.id.zxing_btn);
        save_btn.setVisibility(post.getUserid().equals(Global.loginResult.getId()) ? View.VISIBLE : View.GONE);
        save_btn.setText("删除");
        zxing_btn.setText("复制");
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeWeiboReply(replyid);
                mPopupWindow.dismiss();
            }
        });
        zxing_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyUtils.copy(content, mActivity);
                mPopupWindow.dismiss();
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
                MyUtils.setWindowAlpa(mActivity, false);
            }
        });
        if (mPopupWindow != null && !mPopupWindow.isShowing()) {
            mPopupWindow.showAtLocation(popupWindow_view, Gravity.BOTTOM, 0, 0);
        }
        MyUtils.setWindowAlpa(mActivity, true);
    }
}

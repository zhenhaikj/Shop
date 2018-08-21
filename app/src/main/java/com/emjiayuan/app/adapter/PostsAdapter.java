package com.emjiayuan.app.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.emjiayuan.app.MainActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.GlideUtil;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.activity.CustomImagePreviewActivity;
import com.emjiayuan.app.activity.GoodsDetailActivity;
import com.emjiayuan.app.activity.PostActivity;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.entity.PhotoInfo;
import com.emjiayuan.app.entity.Post;
import com.emjiayuan.app.event.CommentEvent;
import com.emjiayuan.app.event.UpdateEvent;
import com.emjiayuan.app.widget.CommentListView;
import com.emjiayuan.app.widget.MultiImageView;
import com.emjiayuan.app.widget.PraiseListView;
import com.google.gson.Gson;
import com.previewlibrary.GPreviewBuilder;
import com.previewlibrary.loader.OnLongClickListener;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

//import com.emujiayuan.app.activity.ImagePagerActivity;

/**
 * Created by cyl on 2018年5月10日 09:52:49.
 */

public class PostsAdapter extends BaseAdapter {

    private Context mContext;
    private Context context;
    private IconsAdapter adapter;

    private ArrayList<Post> grouplists = new ArrayList<>();
    private LayoutInflater mInflater;
    private Map<Integer, PhotoInfo> imageslist = new HashMap<>();
    private PopupWindow mPopupWindow;
    private CustomShareListener mShareListener;
    private ShareAction mShareAction;
    private boolean isMine=false;

    public PostsAdapter(Context mContext, ArrayList<Post> grouplists,boolean isMine) {
        super();
        this.mContext = mContext;
        this.grouplists = grouplists;
        this.mInflater = LayoutInflater.from(mContext);
        this.isMine = isMine;
    }

    @Override
    public int getCount() {
        return grouplists.size();
//        return 16;
    }

    @Override
    public Post getItem(int position) {
        return grouplists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = mInflater.inflate(R.layout.post_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Post post = grouplists.get(position);
        GlideUtil.loadImageViewLoding(mContext, post.getHeadimg(), holder.icon, R.drawable.default_tx, R.drawable.default_tx);
        holder.username.setText(post.getNickname());
        holder.content.setText(post.getContent());
        holder.plCount.setText(post.getPinglun());
        holder.zanCount.setText(post.getZan());
        holder.views.setText(post.getVisit_pv());
        holder.address.setText(post.getAddress());
        holder.time.setText(post.getPasttime());
        holder.label.setText(post.getWeibotype());
        holder.more.setVisibility((post.getReplylist().size() > 3 ? View.VISIBLE : View.GONE));
        holder.address.setVisibility((post.getAddress() ==null ? View.INVISIBLE : View.VISIBLE));
        holder.llEdit.setVisibility((isMine ? View.VISIBLE : View.GONE));
        holder.llDelete.setVisibility((isMine ? View.VISIBLE : View.GONE));
        final ViewHolder finalHolder2 = holder;
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("查看更多...".equals(finalHolder2.more.getText().toString())){
                    finalHolder2.more.setText("收起");
                    post.setExpand(true);
                }else{
                    finalHolder2.more.setText("查看更多...");
                    post.setExpand(false);
                }
                notifyDataSetChanged();
            }
        });
//        holder.praiseListView.setDatas(post.getZanlist());
        if (!post.isExpand()){
            List<Post.ReplylistBean> list = new ArrayList<>();
            for (int i = 0; i < (post.getReplylist().size() > 3 ? 3 : post.getReplylist().size()); i++) {
                list.add(post.getReplylist().get(i));
            }
            holder.commentList.setDatas(list);
        }else{
            holder.commentList.setDatas(post.getReplylist());
        }
        final List<PhotoInfo> photos = new ArrayList<PhotoInfo>();
        if (post.getImages().size() == 1) {
            final ViewHolder finalHolder = holder;
            Glide.with(mContext).load(post.getImages().get(0)).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                    photos.add(new PhotoInfo(post.getImages().get(0), resource.getIntrinsicWidth(), resource.getIntrinsicHeight()));
                    finalHolder.multiimageview.setList(photos);
                }
            });
        } else {
            for (int i = 0; i < post.getImages().size(); i++) {
                photos.add(new PhotoInfo(post.getImages().get(i), 0, 0));
            }
            holder.multiimageview.setList(photos);
        }

        final ViewHolder finalHolder1 = holder;
        holder.multiimageview.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (!MyUtils.isFastClick()) {
                    return;
                }
                //imagesize是作为loading时的图片size
//                ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());
//                ImagePagerActivity.startImagePagerActivity(mContext, post.getImages(), position, imageSize);
                List<View> viewList = new ArrayList<>();
                for (int i = 0; i < finalHolder1.multiimageview.getChildCount(); i++) {
                    View itemView = finalHolder1.multiimageview.getChildAt(i);
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
                    photos.get(j).setStatusBarH(MyUtils.getStatusHeight(mContext));
                    photos.get(j).setmBounds(bounds);
                    photos.get(j).setUrl(photos.get(j).getUrl());
                }
                GPreviewBuilder.from((Activity) mContext)
                        .to(CustomImagePreviewActivity.class)
                        .setOnLongClickListener(new OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                showPopupWindow(context);
                                return false;
                            }

                            @Override
                            public void getContext(Context context) {
                                PostsAdapter.this.context = context;
                            }
                        })
                        .setData(photos)
                        .setCurrentIndex(position)
                        .setType(GPreviewBuilder.IndicatorType.Number)
                        .start();//启动
            }
        });
        holder.llZan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zanWeibo(post.getId());
            }
        });
        holder.llPl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new CommentEvent(post, -1));
            }
        });
        holder.llEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, PostActivity.class);
                intent.putExtra("post",post);
                mContext.startActivity(intent);
            }
        });
        holder.llDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("提醒");
                builder.setMessage("确定删除吗？");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeWeibo(post.getId());
                    }
                });
                builder.create().show();
            }
        });
        holder.llShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShareListener = new CustomShareListener((Activity)mContext);
                /*增加自定义按钮的分享面板*/
                mShareAction = new ShareAction((Activity)mContext).setDisplayList(
                        SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE,
                        SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,SHARE_MEDIA.MORE)
                        .addButton("复制文本", "复制文本", "umeng_socialize_copy", "umeng_socialize_copy")
                        .addButton("复制链接", "复制链接", "umeng_socialize_copyurl", "umeng_socialize_copyurl")
                        .setShareboardclickCallback(new ShareBoardlistener() {
                            @Override
                            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                                if (snsPlatform.mShowWord.equals("复制文本")) {
                                    Toast.makeText(mContext, "已复制", Toast.LENGTH_LONG).show();
                                } else if (snsPlatform.mShowWord.equals("复制链接")) {
                                    Toast.makeText(mContext, "已复制", Toast.LENGTH_LONG).show();

                                } else {
                                    UMWeb web = new UMWeb("https://h5.youzan.com/wscshop/feature/ihODHu0rwF?redirect_count=1");
                                    web.setTitle("伊穆家园");
                                    web.setDescription(post.getContent());
                                    web.setThumb(new UMImage(mContext, R.drawable.start_icon));
                                    new ShareAction((Activity) mContext).withMedia(web)
                                            .setPlatform(share_media)
                                            .setCallback(mShareListener)
                                            .share();
                                }
                            }
                        });
                mShareAction.open();
            }
        });
        holder.commentList.setOnItemClickListener(new CommentListView.OnItemClickListener() {
            @Override
            public void onItemClick(int commentPosition) {
                EventBus.getDefault().post(new CommentEvent(post, commentPosition));
            }
        });
        if (post.getIszan() == 1) {
            holder.zanIcon.setSelected(true);
        } else {
            holder.zanIcon.setSelected(false);
        }
//        holder.linDig.setVisibility(post.hasFavort() && post.hasComment() ? View.VISIBLE : View.GONE);
        holder.linDig.setVisibility(View.GONE);
        holder.praiseListView.setVisibility(View.GONE);
        if (post.hasComment()) {
            holder.digCommentBody.setVisibility(View.VISIBLE);
        } else {
            holder.digCommentBody.setVisibility(View.GONE);
        }
        return convertView;
    }

    public static int getRandomNum(int max) {
        Random random = new Random();
        int result = random.nextInt(max);
        return result;
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
            Glide.with(mContext).load(list.get(i)).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                    photos.add(new PhotoInfo(list.get(finalI), resource.getIntrinsicWidth(), resource.getIntrinsicHeight()));
                }
            });
        }
        return photos;
    }


    /**
     * 帖子点赞
     */
    public void zanWeibo(String weiboid) {
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
     * 帖子点赞
     */
    public void removeWeibo(String weiboid) {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("weiboid", weiboid);//传递键值对参数
        formBody.add("userid", Global.loginResult.getId());//传递键值对参数
//new call
        Call call = MyOkHttp.GetCall("weibo.removeWeibo", formBody);
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
                MyUtils.e("------删帖------", result);
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

                case 0://点赞
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            MyUtils.showToast(mContext, message);
                            EventBus.getDefault().post(new UpdateEvent(""));
                        } else {
                            MyUtils.showToast(mContext, message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 1://删帖
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            MyUtils.showToast(mContext, message);
                            EventBus.getDefault().post(new UpdateEvent(""));
                        } else {
                            MyUtils.showToast(mContext, message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    /**
     * 弹出Popupwindow
     */
    public void showPopupWindow(final Context context) {
        View popupWindow_view = LayoutInflater.from(context).inflate(R.layout.pop_layout, null);
        Button save_btn = popupWindow_view.findViewById(R.id.save_btn);
        Button zxing_btn = popupWindow_view.findViewById(R.id.zxing_btn);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyUtils.showToast(context, "保存图片");
            }
        });
        zxing_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyUtils.showToast(context, "识别二维码");
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
                MyUtils.setWindowAlpa(context, false);
            }
        });
        if (mPopupWindow != null && !mPopupWindow.isShowing()) {
            mPopupWindow.showAtLocation(popupWindow_view, Gravity.BOTTOM, 0, 0);
        }
        MyUtils.setWindowAlpa(context, true);
    }
    private static class CustomShareListener implements UMShareListener {

        private WeakReference<Activity> mContext;

        private CustomShareListener(Activity activity) {
            mContext = new WeakReference(activity);
        }

        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {

            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(mContext.get(), platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(mContext.get(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(mContext.get(), platform + " 分享失败啦", Toast.LENGTH_SHORT).show();

            }

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {

            Toast.makeText(mContext.get(), platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    }

    class ViewHolder {
        @BindView(R.id.icon)
        CircleImageView icon;
        @BindView(R.id.zan_icon)
        ImageView zanIcon;
        @BindView(R.id.username)
        TextView username;
        @BindView(R.id.views)
        TextView views;
        @BindView(R.id.content)
        TextView content;
        @BindView(R.id.multiimageview)
        MultiImageView multiimageview;
        @BindView(R.id.label)
        TextView label;
        @BindView(R.id.address)
        TextView address;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.ll_time)
        LinearLayout llTime;
        @BindView(R.id.ll_share)
        LinearLayout llShare;
        @BindView(R.id.ll_letter)
        LinearLayout llLetter;
        @BindView(R.id.ll_edit)
        LinearLayout llEdit;
        @BindView(R.id.ll_delete)
        LinearLayout llDelete;
        @BindView(R.id.pl_count)
        TextView plCount;
        @BindView(R.id.ll_pl)
        LinearLayout llPl;
        @BindView(R.id.zan_count)
        TextView zanCount;
        @BindView(R.id.ll_zan)
        LinearLayout llZan;
        @BindView(R.id.praiseListView)
        PraiseListView praiseListView;
        @BindView(R.id.lin_dig)
        View linDig;
        @BindView(R.id.commentList)
        CommentListView commentList;
        @BindView(R.id.digCommentBody)
        LinearLayout digCommentBody;
        @BindView(R.id.ll_content)
        LinearLayout llContent;
        @BindView(R.id.ll_post)
        LinearLayout llPost;
        @BindView(R.id.more)
        TextView more;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

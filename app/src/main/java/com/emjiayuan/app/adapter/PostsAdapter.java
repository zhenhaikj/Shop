package com.emjiayuan.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emjiayuan.app.MainActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyUtils;
//import com.emujiayuan.app.activity.ImagePagerActivity;
import com.emjiayuan.app.entity.PhotoInfo;
import com.emjiayuan.app.entity.Post;
import com.emjiayuan.app.widget.MultiImageView;
import com.emjiayuan.app.widget.MyGridView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by cyl on 2018年5月10日 09:52:49.
 */

public class PostsAdapter extends BaseAdapter {
    private Context mContext;
    private IconsAdapter adapter;

    private ArrayList<Post> grouplists = new ArrayList<>();
    private LayoutInflater mInflater;

    public PostsAdapter(Context mContext, ArrayList<Post> grouplists) {
        super();
        this.mContext = mContext;
        this.grouplists = grouplists;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
//        return grouplists.size();
        return 16;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = mInflater.inflate(R.layout.post_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final List<PhotoInfo> photos = createPhotos();
        holder.multiimageview.setList(photos);
        holder.multiimageview.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (!MyUtils.isFastClick()){
                    return;
                }
                //imagesize是作为loading时的图片size
//                ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());

                List<String> photoUrls = new ArrayList<String>();
                for(PhotoInfo photoInfo : photos){
                    photoUrls.add(photoInfo.url);
                }
//                ImagePagerActivity.startImagePagerActivity(mContext, photoUrls, position, imageSize);
            }
        });


        return convertView;
    }
    public static int getRandomNum(int max) {
        Random random = new Random();
        int result = random.nextInt(max);
        return result;
    }
    public static List<PhotoInfo> createPhotos() {
        PhotoInfo p1 = new PhotoInfo();
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
        }
        return photos;
    }
    static class ViewHolder {
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
        @BindView(R.id.time_icon)
        ImageView timeIcon;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.zan_icon)
        ImageView zanIcon;
        @BindView(R.id.zan_count)
        TextView zanCount;
        @BindView(R.id.pl_icon)
        ImageView plIcon;
        @BindView(R.id.pl_count)
        TextView plCount;
        @BindView(R.id.ll_content)
        LinearLayout llContent;
        @BindView(R.id.ll_post)
        LinearLayout llPost;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

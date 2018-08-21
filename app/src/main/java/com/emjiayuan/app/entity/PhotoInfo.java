package com.emjiayuan.app.entity;

import android.graphics.Rect;
import android.os.Parcel;
import android.support.annotation.Nullable;

import com.emjiayuan.app.Utils.MyUtils;
import com.previewlibrary.enitity.IThumbViewInfo;

/**
 * Created by suneee on 2016/11/17.
 */
public class PhotoInfo implements IThumbViewInfo {

    public String url;
    public int w;
    public int h;
    private Rect mBounds; // 记录坐标
    private String videoUrl;//视频链接 //不为空是视频
    private int statusBarH;

    public int getStatusBarH() {
        return statusBarH;
    }

    public void setStatusBarH(int statusBarH) {
        this.statusBarH = statusBarH;
    }

    public PhotoInfo(String url, int w, int h) {
        this.url = url;
        this.w = w;
        this.h = h;
    }

    public void setmBounds(Rect mBounds) {
        mBounds.bottom+= getStatusBarH();
        mBounds.top+= getStatusBarH();
        this.mBounds = mBounds;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public Rect getBounds() {
        return mBounds;
    }

    @Nullable
    @Override
    public String getVideoUrl() {
        return videoUrl;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.url);
        parcel.writeParcelable(this.mBounds, i);
        parcel.writeInt(this.w);
        parcel.writeInt(this.h);
        parcel.writeString(this.videoUrl);
    }
    protected PhotoInfo(Parcel in) {
        this.url = in.readString();
        this.mBounds = in.readParcelable(Rect.class.getClassLoader());
        this.w = in.readInt();
        this.h = in.readInt();
        this.videoUrl = in.readString();
    }

    public static final Creator<PhotoInfo> CREATOR=new Creator<PhotoInfo>() {
        @Override
        public PhotoInfo createFromParcel(Parcel parcel) {
            return new PhotoInfo(parcel);
        }

        @Override
        public PhotoInfo[] newArray(int i) {
            return new PhotoInfo[i];
        }
    };
}

package com.previewlibrary.view;

import android.graphics.Bitmap;

import java.io.File;

public interface ImageDownLoadCallBack {

//    void onDownLoadSuccess(Bitmap bitmap);
    void onDownLoadSuccess(File file);

    void onDownLoadFailed();
}


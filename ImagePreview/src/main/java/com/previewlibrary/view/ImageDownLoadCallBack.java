package com.previewlibrary.view;

import android.graphics.Bitmap;

public interface ImageDownLoadCallBack {

    void onDownLoadSuccess(Bitmap bitmap);

    void onDownLoadFailed();
}


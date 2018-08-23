package com.emjiayuan.app.Utils;

import android.graphics.Bitmap;

public interface ImageDownLoadCallBack {

    void onDownLoadSuccess(Bitmap bitmap);

    void onDownLoadFailed();
}


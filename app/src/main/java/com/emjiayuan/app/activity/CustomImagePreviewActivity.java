package com.emjiayuan.app.activity;

import android.os.Bundle;

import com.emjiayuan.app.R;
import com.gyf.barlibrary.ImmersionBar;
import com.previewlibrary.GPreviewActivity;


/**
 * Created by yangc on 2017/9/19.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated:
 */

public class CustomImagePreviewActivity extends GPreviewActivity {
    /***
     * 重复该方法 *使用你的自定义布局
     ***/
    @Override
    public int setContentLayout() {
        return R.layout.activity_custom_preview;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在BaseActivity里初始化
        ImmersionBar mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarDarkFont(true, 0.2f); //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
        mImmersionBar.statusBarColor(R.color.transparent);
        mImmersionBar.fitsSystemWindows(false);
        mImmersionBar.init();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        transformOut();
    }
}

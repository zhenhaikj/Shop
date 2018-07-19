package com.emjiayuan.app.fragment.Community;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.event.UpdateEvent;
import com.emjiayuan.app.fragment.BaseLazyFragment;
import com.emjiayuan.app.widget.MyChromeClient;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.iwf.photopicker.PhotoPicker;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.emjiayuan.app.widget.MyChromeClient.FILECHOOSER_RESULTCODE;


public class CommunityFragment2 extends BaseLazyFragment implements View.OnClickListener, AdapterView.OnItemClickListener {


    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.txt_content)
    RelativeLayout txtContent;
    //    private ValueCallback mUploadMessage;
    private ValueCallback<Uri> uploadFile;
    private ValueCallback<Uri[]> uploadFiles;
    private String url="";


    @Override
    protected int setLayoutId() {
        return R.layout.fragment_community2;
    }

    @Override
    protected void initData() {
//        http://sq.emjiayuan.com/#/Home?userid=1&token=abc&app=1
        url="http://sq.emjiayuan.com/#/Home?userid="+ Global.loginResult.getId()+"&token="+ MyUtils.md5(Global.loginResult.getId()+"webkey")+"&app=1";
        MyUtils.e("===url===",url);
        webview.loadUrl(url);
//        webview.loadUrl("https://www.baidu.com");
        WebSettings webSettings = webview.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        webview.setWebChromeClient(new MyWebChromeClient());
//        webview.setWebChromeClient(new MyChromeClient(this));
        webview.setWebChromeClient(new WebChromeClient() {
            // For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
//                Log.i("test", "openFileChooser 1");
                CommunityFragment2.this.uploadFile = uploadFile;
                openFileChooseProcess();
            }

            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsgs) {
//                Log.i("test", "openFileChooser 2");
                CommunityFragment2.this.uploadFile = uploadFile;
                openFileChooseProcess();
            }

            // For Android  > 4.1.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
//                Log.i("test", "openFileChooser 3");
                CommunityFragment2.this.uploadFile = uploadFile;
                openFileChooseProcess();
            }

            // For Android  >= 5.0
            public boolean onShowFileChooser(com.tencent.smtt.sdk.WebView webView,
                                             ValueCallback<Uri[]> filePathCallback,
                                             WebChromeClient.FileChooserParams fileChooserParams) {
//                Log.i("test", "openFileChooser 4:" + filePathCallback.toString());
                CommunityFragment2.this.uploadFiles = filePathCallback;
                openFileChooseProcess();
                return true;
            }

        });
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url == null) return false;

                try {
                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        return true;
                    }
                } catch (Exception e) {//防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    return true;//没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
                }

                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (webview.canGoBack()){
                    back.setVisibility(View.VISIBLE);
                }else{
                    back.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }
        });


    }

    private void openFileChooseProcess() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "test"), PhotoPicker.REQUEST_CODE);
//        PhotoPicker.builder()
//                .setPhotoCount(9)
//                .setShowCamera(true)
//                .setShowGif(true)
//                .setPreviewEnabled(true)
//                .start(mActivity,this, PhotoPicker.REQUEST_CODE);
    }

    //WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等
    private WebChromeClient webChromeClient = new WebChromeClient() {
        //不支持js的alert弹窗，需要自己监听然后通过dialog弹窗
        @Override
        public boolean onJsAlert(WebView webView, String url, String message, JsResult result) {
            AlertDialog.Builder localBuilder = new AlertDialog.Builder(webView.getContext());
            localBuilder.setMessage(message).setPositiveButton("确定", null);
            localBuilder.setCancelable(false);
            localBuilder.create().show();

            //注意:
            //必须要这一句代码:result.confirm()表示:
            //处理结果为确定状态同时唤醒WebCore线程
            //否则不能继续点击按钮
            result.confirm();
            return true;
        }

        //获取网页标题
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
//            Log.i("ansen","网页标题:"+title);
        }

        //加载进度回调
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
//            progressBar.setProgress(newProgress);
        }
    };


    @Override
    protected void setListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webview.goBack();
            }
        });
    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(UpdateEvent event) {

    }

    @Override
    public boolean onBackPressed() {
        if (webview.canGoBack()) {//点击返回按钮的时候判断有没有上一页
            webview.goBack(); // goBack()表示返回webview的上一页面
            return true;
        }
        return super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PhotoPicker.REQUEST_CODE:
                    if (null != uploadFile) {
                        Uri result = data == null || resultCode != RESULT_OK ? null
                                : data.getData();
                        uploadFile.onReceiveValue(result);
                        uploadFile = null;
                    }
                    if (null != uploadFiles) {
                        Uri result = data == null || resultCode != RESULT_OK ? null
                                : data.getData();
                        uploadFiles.onReceiveValue(new Uri[]{result});
                        uploadFiles = null;
                    }
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (null != uploadFile) {
                uploadFile.onReceiveValue(null);
                uploadFile = null;
            }
            if (null != uploadFiles) {
                uploadFiles.onReceiveValue(null);
                uploadFiles = null;
            }
        }
    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == FILECHOOSER_RESULTCODE) {
//
//            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
//            if (result == null) {
//                mUploadMessage.onReceiveValue(null);
//                mUploadMessage = null;
//                return;
//            }
//
//            String path =  FileUtils.getPath(mActivity, result);
//            if (TextUtils.isEmpty(path)) {
//                mUploadMessage.onReceiveValue(null);
//                mUploadMessage = null;
//                return;
//            }
//            Uri uri = Uri.fromFile(new File(path));
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                mUploadMessage.onReceiveValue(new Uri[]{uri});
//            } else {
//                mUploadMessage.onReceiveValue(uri);
//            }
//            mUploadMessage = null;
//        }
//    }
//    public class MyWebChromeClient extends WebChromeClient {
//        // For Android 3.0+
//        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
////            CLog.i("UPFILE", "in openFile Uri Callback");
//            if (mUploadMessage != null) {
//                mUploadMessage.onReceiveValue(null);
//            }
//            mUploadMessage = uploadMsg;
//            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//            i.addCategory(Intent.CATEGORY_OPENABLE);
//            i.setType("*/*");
//            startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
//        }
//        // For Android 3.0+
//        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
////            CLog.i("UPFILE", "in openFile Uri Callback has accept Type" + acceptType);
//            if (mUploadMessage != null) {
//                mUploadMessage.onReceiveValue(null);
//            }
//            mUploadMessage = uploadMsg;
//            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//            i.addCategory(Intent.CATEGORY_OPENABLE);
//            String type = TextUtils.isEmpty(acceptType) ? "*/*" : acceptType;
//            i.setType(type);
//            startActivityForResult(Intent.createChooser(i, "File Chooser"),
//                    FILECHOOSER_RESULTCODE);
//        }
//        // For Android 4.1
//        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
////            CLog.i("UPFILE", "in openFile Uri Callback has accept Type" + acceptType + "has capture" + capture);
//            if (mUploadMessage != null) {
//                mUploadMessage.onReceiveValue(null);
//            }
//            mUploadMessage = uploadMsg;
//            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//            i.addCategory(Intent.CATEGORY_OPENABLE);
//            String type = TextUtils.isEmpty(acceptType) ? "*/*" : acceptType;
//            i.setType(type);
//            startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
//        }
//        //Android 5.0+
//        @Override
//        @SuppressLint("NewApi")
//        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
//            if (mUploadMessage != null) {
//                mUploadMessage.onReceiveValue(null);
//            }
////            CLog.i("UPFILE", "file chooser params：" + fileChooserParams.toString());
//            mUploadMessage = filePathCallback;
//            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//            i.addCategory(Intent.CATEGORY_OPENABLE);
//            if (fileChooserParams != null && fileChooserParams.getAcceptTypes() != null
//                    && fileChooserParams.getAcceptTypes().length > 0) {
//                i.setType(fileChooserParams.getAcceptTypes()[0]);
//            } else {
//                i.setType("*/*");
//            }
//            startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
//            return true;
//        }
//    }
}
package com.emjiayuan.app.activity;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.dmcbig.mediapicker.PickerActivity;
import com.dmcbig.mediapicker.PickerConfig;
import com.dmcbig.mediapicker.PreviewActivity;
import com.dmcbig.mediapicker.entity.Media;
import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.MainActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.ImageCompress;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.adapter.IconsAdapter;
import com.emjiayuan.app.adapter.LabelAdapter;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.entity.Integral;
import com.emjiayuan.app.entity.Label;
import com.emjiayuan.app.entity.Post;
import com.emjiayuan.app.event.UpdateEvent;
import com.emjiayuan.app.fragment.HomeFragment;
import com.emjiayuan.app.widget.DragGridView;
import com.emjiayuan.app.widget.MyGridView;
import com.google.gson.Gson;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.common.Zone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.PhotoPreview;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;


public class PostActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {


    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.check_address)
    CheckBox checkAddress;
    @BindView(R.id.service_ll)
    LinearLayout serviceLl;
    @BindView(R.id.line_top)
    View lineTop;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.gv_icons)
    DragGridView gvIcons;
    @BindView(R.id.gv_label)
    MyGridView gvLabel;
    private ArrayList<String> list = new ArrayList<>();
    private ArrayList<String> camera = new ArrayList<>();
    private Map<Integer,String> imageslist = new HashMap<>();
    private Map<Integer,String> httpimageslist = new HashMap<>();
    private List<Label> labelList = new ArrayList<>();
    private List<Boolean> isOkList = new ArrayList<>();
    ArrayList<Media> select=new ArrayList<>();
    private IconsAdapter adapter;
    private LabelAdapter labelAdapter;
    private String token;
    private String type="";
    private String content="";
    private String images="";
    private String audio="";
    private String video="";
    private String addressStr="";
    private boolean addressIsChecked=false;
    private MyLocationListenner myListener;
    private LocationClient mLocClient;
    private Post post;
    private Intent intent;
    private int addType;
    private static final int BAIDU_ACCESS_FINE_LOCATION = 100;//定位申请跳转
    private PopupWindow mPopupWindow;
    private View popupWindow_view;
    private String FilePath;
    private ProgressDialog pd;
    private ArrayList<String> permissions;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_post;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        title.setText("发布圈子");
        save.setVisibility(View.VISIBLE);
        save.setText("发布");
        intent=getIntent();
        post= (Post) intent.getSerializableExtra("post");
        addType=intent.getIntExtra("addType",0);
        getQnToken();
        getWeiboTypeList();
        requestPermission();
        initMapLocation();
        showPopupWindow();
        gvIcons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if ("add".equals(adapter.getItem(i))){
                    if (mPopupWindow != null && !mPopupWindow.isShowing()) {
                        mPopupWindow.showAtLocation(popupWindow_view, Gravity.BOTTOM, 0, 0);
                    }
                    MyUtils.setWindowAlpa(mActivity,true);
//                    switch (addType){
//                        case 0:
//                            goImage();
//                            break;
//                        case 1:
//                            goVideo();
//                            break;
//                    }
//                    PhotoPicker.builder()
//                            .setPhotoCount(9-(list.size()-1))
//                            .setShowGif(true)
//                            .setPreviewEnabled(true)
//                            .start(PostActivity.this);
                }else{
//                    if (list.contains("add")){
//                        list.remove("add");
//                    }
//                    if (addType==1){
                        goPreviewActivity();
//                    }else{
//                        if (list.contains("add")){
//                            list.remove("add");
//                        }
//                        PhotoPreview.builder()
//                                .setPhotos(list)
//                                .setCurrentItem(i)
//                                .setShowDeleteButton(true)
//                                .start(PostActivity.this);
//                    }
                }
            }
        });
        gvIcons.setDragItemBackground(R.color.white);
//        gvIcons.setIgnoreDragPosition(new int[]{adapter.getNoMovePosition()});
        gvIcons.setOnChangeListener(new DragGridView.OnChanageListener() {

            @Override
            public void onChange(int from, int to) {
                String temp = list.get(from);
                //直接交互item
//				dataSourceList.set(from, dataSourceList.get(to));
//				dataSourceList.set(to, temp);
//				dataSourceList.set(to, temp);
                Media tempMedia=select.get(from);

                //这里的处理需要注意下
                if(from < to){
                    for(int i=from; i<to; i++){
                        Collections.swap(list, i, i+1);
                        Collections.swap(select, i, i+1);
                    }
                }else if(from > to){
                    for(int i=from; i>to; i--){
                        Collections.swap(list, i, i-1);
                        Collections.swap(select, i, i-1);
                    }
                }

                list.set(to, temp);
                select.set(to, tempMedia);

                adapter.notifyDataSetChanged();
//                gvIcons.setIgnoreDragPosition(new int[]{adapter.getNoMovePosition()});
            }
        });

    }

    void goImage(){
        Intent intent =new Intent(PostActivity.this, PickerActivity.class);
        intent.putExtra(PickerConfig.SELECT_MODE,PickerConfig.PICKER_IMAGE);//default image and video (Optional)
        long maxSize=188743680L;//long long long
        intent.putExtra(PickerConfig.MAX_SELECT_SIZE,maxSize); //default 180MB (Optional)
        intent.putExtra(PickerConfig.MAX_SELECT_COUNT,9-select.size());  //default 40 (Optional)
//        intent.putExtra(PickerConfig.DEFAULT_SELECTED_LIST,select); // (Optional)
        PostActivity.this.startActivityForResult(intent,200);
    }
    void goVideo(){
        Intent intent =new Intent(PostActivity.this, PickerActivity.class);
        intent.putExtra(PickerConfig.SELECT_MODE,PickerConfig.PICKER_VIDEO);//default image and video (Optional)
        long maxSize=20971520L;//long long long20Mb
        intent.putExtra(PickerConfig.MAX_SELECT_SIZE,maxSize); //default 180MB (Optional)
        intent.putExtra(PickerConfig.MAX_SELECT_COUNT,1);  //default 40 (Optional)
//        intent.putExtra(PickerConfig.DEFAULT_SELECTED_LIST,select); // (Optional)
        PostActivity.this.startActivityForResult(intent,200);
    }
    void goPreviewActivity(){
        Intent intent =new Intent(PostActivity.this, PreviewActivity.class);
        intent.putExtra(PickerConfig.PRE_RAW_LIST,select);//default image and video (Optional)
        intent.putExtra(PickerConfig.MAX_SELECT_COUNT,select.size());//default image and video (Optional)
        PostActivity.this.startActivityForResult(intent,300);
    }

    @Override
    protected void setListener() {
        back.setOnClickListener(this);
        save.setOnClickListener(this);
        checkAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                addressIsChecked=b;
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (!MyUtils.isFastClick()) {
            return;
        }

    }

    /**
     * 类型
     */
    public void getWeiboTypeList() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
//new call
        Call call = MyOkHttp.GetCall("weibo.getWeiboTypeList", formBody);
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
                MyUtils.e("------类型------", result);
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
     * 七牛token
     */
    public void getQnToken() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
//new call
        Call call = MyOkHttp.GetCall("system.getQnToken", formBody);
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
                MyUtils.e("------七牛token------", result);
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
     * 发布帖子
     */
    public void addWeibo() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("type",type);
        formBody.add("userid", Global.loginResult.getId());
        formBody.add("content",content);
        if (imageslist.size()>0){
            for (int i = 0; i < imageslist.size(); i++) {
                images+=imageslist.get(i)+",";
            }
            if (addType==1){
                formBody.add("video",images.substring(0,images.lastIndexOf(",")));
            }else{
                formBody.add("images",images.substring(0,images.lastIndexOf(",")));
            }
        }else{
            formBody.add("images","");
        }
        if (addressIsChecked){
            formBody.add("address",addressStr);
        }
//new call
        Call call = MyOkHttp.GetCall("weibo.addWeibo", formBody);
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
                MyUtils.e("------发布帖子------", result);
                Message message = new Message();
                message.what = 2;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }
    /**
     * 编辑帖子
     */
    public void editWeibo() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("weiboid",post.getId());
        formBody.add("type",type);
        formBody.add("userid", Global.loginResult.getId());
        formBody.add("content",content);
        if (imageslist.size()>0){
            for (int i = 0; i < imageslist.size(); i++) {
                images+=imageslist.get(i)+",";
            }
            formBody.add("images",images.substring(0,images.lastIndexOf(",")));
        }else{
            formBody.add("images","");
        }
        if (addressIsChecked){
            formBody.add("address",addressStr);
        }
//new call
        Call call = MyOkHttp.GetCall("weibo.editWeibo", formBody);
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
                MyUtils.e("------编辑帖子------", result);
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

                case 0://类型
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            JSONArray jsonArray = new JSONArray(data);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                labelList.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), Label.class));
                            }
                            labelAdapter = new LabelAdapter(mActivity, labelList);
                            gvLabel.setAdapter(labelAdapter);
                            if (post != null) {
                                etContent.setText(post.getContent());
                                if (post.getImages()!=null){
                                    loadAdpater(post.getImages());
                                }
                                for (int i = 0; i < labelList.size(); i++) {
                                    if (post.getType().equals(labelList.get(i).getId())){
                                        type=post.getType();
                                        labelAdapter.setSelected(i);
                                    }
                                }
                                checkAddress.setChecked(post.getAddress() != null);
                            }else{
                                list.add("add");
                                adapter = new IconsAdapter(mActivity, list);
                                gvIcons.setAdapter(adapter);
                            }
                            gvLabel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    type=labelList.get(i).getId();
                                    labelAdapter.setSelected(i);
                                }
                            });
                        } else {
                            MyUtils.showToast(mActivity, message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 1://七牛token
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            JSONObject jsonObject1=new JSONObject(data);
                            token=jsonObject1.getString("token");
                        } else {
                            MyUtils.showToast(mActivity, message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2://发布帖子
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            EventBus.getDefault().post(new UpdateEvent(""));
                            MyUtils.showToast(mActivity,message);
                            finish();
                        } else {
                            imageslist.clear();
                            httpimageslist.clear();
                            isOkList.clear();
                            images="";
                            MyUtils.showToast(mActivity, message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    private void loadAdpater(List<String> paths) {
        list.clear();
        if (list.contains("add")){
            list.remove("add");
        }
        for (int i = 0; i < paths.size(); i++) {
            if (paths.get(i).contains("mp4")){
                list.add(paths.get(i));
            }else{
                list.add(ImageCompress.compressImage(paths.get(i), Environment.getExternalStorageDirectory().getPath()+"/ymjytemp/"+getRandomFileName()+".jpg",30));
            }
        }
        if (list.size() == 9&&addType==0) {
            if (list.contains("add")) {
                list.remove("add");
            }
        }else if (list.size()==1&&addType==1){
            if (list.contains("add")) {
                list.remove("add");
            }
        }else{
            list.add("add");
        }
        adapter = new IconsAdapter(mActivity,list);
        gvIcons.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void loadAdpater2(ArrayList<String> paths) {
        list.clear();
        list.addAll(paths);
        if ((list.size() != 9&&addType==0)||list.size()==0) {
            list.add("add");
        }
        adapter = new IconsAdapter(mActivity,list);
        gvIcons.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    public void remove(int position){
        select.remove(position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 选择照片
                case PhotoPicker.REQUEST_CODE:
                    ArrayList<String> list = data.getStringArrayListExtra("SELECTED_PHOTOS");
                    loadAdpater(list);
                    break;
                // 预览
                case PhotoPreview.REQUEST_CODE:
                    ArrayList<String> ListExtra = data.getStringArrayListExtra("SELECTED_PHOTOS");
                    loadAdpater2(ListExtra);
                    break;
                // 预览
            }
        }
        if(requestCode==200&&resultCode==PickerConfig.RESULT_CODE){
            select.addAll((ArrayList)data.getParcelableArrayListExtra(PickerConfig.EXTRA_RESULT));
            ArrayList<String> list=new ArrayList<>();
            Log.i("select","select.size"+select.size());
            for(Media media:select){
                list.add(media.path);
                Log.i("media",media.path);
                Log.e("media","s:"+media.size);
//                    imageView.setImageURI(Uri.parse(media.path));
            }
            loadAdpater(list);
        }
        if(requestCode==300){
            select=data.getParcelableArrayListExtra(PickerConfig.EXTRA_RESULT);
            ArrayList<String> list=new ArrayList<>();
            Log.i("select","select.size"+select.size());
            for(Media media:select){
                list.add(media.path);
                Log.i("media",media.path);
                Log.e("media","s:"+media.size);
//                    imageView.setImageURI(Uri.parse(media.path));
            }
            loadAdpater2(list);
        }
        if (requestCode==0){
            if (resultCode==RESULT_OK){
                select.add(new Media(FilePath,"",0,0,0,0,""));
                ArrayList<String> list=new ArrayList<>();
                Log.i("select","select.size"+select.size());
                for(Media media:select){
                    list.add(media.path);
                    Log.i("media",media.path);
                    Log.e("media","s:"+media.size);
//                    imageView.setImageURI(Uri.parse(media.path));
                }
                loadAdpater(list);
            }else{
                File file=new File(FilePath);
                if (file.exists()){
                    file.delete();
                }
            }
        }
        if(requestCode==222){
            if (resultCode==102){
                FilePath=data.getStringExtra("videopath");
                select.add(new Media(FilePath,"",0,0,0,0,""));
                ArrayList<String> list=new ArrayList<>();
                Log.i("select","select.size"+select.size());
                for(Media media:select){
                    list.add(media.path);
                    Log.i("media",media.path);
                    Log.e("media","s:"+media.size);
//                    imageView.setImageURI(Uri.parse(media.path));
                }
                loadAdpater(list);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.save:
                content=etContent.getText().toString();
                if ("".equals(content)){
                    MyUtils.showToast(mActivity,"请输入内容!");
                    return;
                }
                if ("".equals(type)){
                    MyUtils.showToast(mActivity,"请选择类型!");
                    return;
                }
                if (list.size()==1&&list.contains("add")){
                    if (post == null) {
                        addWeibo();
                    }else{
                        editWeibo();
                    }
                }else{
                    pd = new ProgressDialog(mActivity);
                    pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    pd.setTitle("正在上传");
                    pd.setMessage("请稍后。。。");
                    pd.show();
                    upload();
                }
                break;
        }
    }
    public void upload(){
        Configuration config = new Configuration.Builder()
                .chunkSize(512 * 1024)        // 分片上传时，每片的大小。 默认256K
                .putThreshhold(1024 * 1024)   // 启用分片上传阀值。默认512K
                .connectTimeout(10)           // 链接超时。默认10秒
                .useHttps(true)               // 是否使用https上传域名
                .responseTimeout(60)          // 服务器响应超时。默认60秒
//                .recorder(recorder)           // recorder分片上传时，已上传片记录器。默认null
//                .recorder(recorder, keyGen)   // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
                .zone(FixedZone.zone0)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                .build();

// 重用uploadManager。一般地，只需要创建一个uploadManager对象
//        data = <File对象、或 文件路径、或 字节数组>
//        String key = <指定七牛服务上的文件名，或 null>;
//        String token = <从服务端SDK获取>;
        UploadManager uploadManager = new UploadManager(config);
        for (int i = 0; i < (list.contains("add")?list.size()-1:list.size()); i++) {
            if (list.get(i).contains("http")){
                imageslist.put(i,list.get(i));
                httpimageslist.put(i,list.get(i));
            }
        }
        for (int i = 0; i < (list.contains("add")?list.size()-1:list.size()); i++) {
            String RandomFileName=getRandomFileName();
            if (list.get(i).contains("mp4")){
                RandomFileName+=".mp4";
            }else{
                if (list.get(i).contains("gif")){
                    RandomFileName+=".gif";
                }else{
                    RandomFileName+=".jpg";
                }
            }
            final int finalI = i;
            if (!list.get(i).contains("http")){
                uploadManager.put(list.get(i), "upload_file/app/"+RandomFileName, token,new UpCompletionHandler() {
                            @Override
                            public void complete(String key, ResponseInfo info, JSONObject res) {
                                //res包含hash、key等信息，具体字段取决于上传策略的设置
                                if(info.isOK()) {
                                    pd.dismiss();
                                    isOkList.add(info.isOK());
                                    imageslist.put(finalI,"http://qiniu.emjiayuan.com/"+key);
                                    Log.i("qiniu", "Upload Success");
                                    if (isOkList.size()==(list.contains("add")?list.size()-1-httpimageslist.size():list.size()-httpimageslist.size())){
                                        if (post == null) {
                                            addWeibo();
                                        }else{
                                            editWeibo();
                                        }
                                    }
                                } else {
                                    imageslist.clear();
                                    httpimageslist.clear();
                                    isOkList.clear();
                                    images="";
                                    MyUtils.showToast(mActivity,"发布失败！");
                                    Log.i("qiniu", "Upload Fail");
                                    //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                                }
                                Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + res);
                            }
                        },
                        new UploadOptions(null, null, false,
                                new UpProgressHandler(){
                                    public void progress(String key, double percent){
                                        MyUtils.e("上传进度啊", key + ": " + percent);
//                                        pd.setProgress((int) (percent*100));
                                    }
                                }, null));
            }else{
                if (imageslist.size()==(list.contains("add")?list.size()-1:list.size())){
                    if (post == null) {
                        addWeibo();
                    }else{
                        editWeibo();
                    }
                    break;//避免重复提交
                }
            }

        }

    }
    public static String getRandomFileName() {


        SimpleDateFormat simpleDateFormat;


        simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");


        Date date = new Date();


        String str = simpleDateFormat.format(date);


        Random random = new Random();


        int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;// 获取5位随机数


        return str + rannum;// 当前时间
    }

    //定位相关:初始化定位
    private void initMapLocation() {

        // 定位初始化
        myListener = new MyLocationListenner();
        mLocClient = new LocationClient(mActivity);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);//自动定位间隔
        mLocClient.setLocOption(option);
        mLocClient.start();

    }

    /**
     * 定位相关:定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {//得到未知信息
            //得到城市
            addressStr = location.getProvince()+"·"+location.getCity();
            if (TextUtils.isEmpty(addressStr)) {
                address.setText("定位中...");
            } else {
                address.setText(addressStr);
                mLocClient.stop();
            }

        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    //申请地图相关权限
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            ArrayList<String> permissions = new ArrayList<>();
            if (mActivity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }

            if (permissions.size() == 0) {
                return;
            } else {
                requestPermissions(permissions.toArray(new String[permissions.size()]), BAIDU_ACCESS_FINE_LOCATION);
            }
        }
    }
    //请求权限
    private boolean requestPermissions(){
        if (Build.VERSION.SDK_INT >= 23) {
            permissions = new ArrayList<>();
//            if (mActivity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
//            }
//            if (mActivity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//            }
            if (mActivity.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (mActivity.checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }

            if (permissions.size() == 0) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }


    //申请地图相关权限:返回监听
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case BAIDU_ACCESS_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {//允许
                    initMapLocation();
                } else {//拒绝
                    MyUtils.showToast(mActivity, "定位权限未开启");
                }
                break;
            case 10001:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED&&grantResults[1]==PackageManager.PERMISSION_GRANTED) {//允许
                    Intent intent = new Intent();
                    switch (addType){
                        case 0:
                            // 指定开启系统相机的Action
                            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            String f = System.currentTimeMillis()+".jpg";
                            FilePath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/ymjy/"+f;
                            File file=new File(FilePath);
//                        File dir=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/ymjy");
//                        if (!dir.exists()){
//                            dir.mkdir();
//                        }
                            if (!file.exists()){
                                try {
                                    file.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            Uri fileUri = Uri.fromFile(file);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                            startActivityForResult(intent, 0);
                            break;
                        case 1:
                            intent=new Intent(mActivity,CameraActivity.class);
                            startActivityForResult(intent, 222);
                        /*intent.setAction("android.media.action.VIDEO_CAPTURE");
                        intent.addCategory("android.intent.category.DEFAULT");
                        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 20000);
                        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 10485760);
                        String str = System.currentTimeMillis()+".mp4";
                        FilePath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/Ymjy/"+str;
                        File mfile=new File(FilePath);
                        if (!mfile.exists()){
                            try {
                                mfile.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Uri mUri = Uri.fromFile(mfile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
                        startActivityForResult(intent, 0);*/
                            break;
                    }
                } else {//拒绝
                    MyUtils.showToast(mActivity, "相机或录音权限未开启");
                }
                break;
            default:
                break;

        }
    }
    /**
     * 弹出Popupwindow
     */
    public void showPopupWindow() {
        popupWindow_view = LayoutInflater.from(mActivity).inflate(R.layout.camera_layout, null);
        Button camera_btn=popupWindow_view.findViewById(R.id.camera_btn);
        Button photo_btn=popupWindow_view.findViewById(R.id.photo_btn);
        Button cancel_btn=popupWindow_view.findViewById(R.id.cancel_btn);
        camera_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (requestPermissions()){
                    Intent intent = new Intent();
                    switch (addType){
                        case 0:
                            // 指定开启系统相机的Action
                            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            String f = System.currentTimeMillis()+".jpg";
                            FilePath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/ymjy/"+f;
                            File file=new File(FilePath);
//                        File dir=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/ymjy");
//                        if (!dir.exists()){
//                            dir.mkdir();
//                        }
                            if (!file.exists()){
                                try {
                                    file.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            Uri fileUri = Uri.fromFile(file);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                            startActivityForResult(intent, 0);
                            break;
                        case 1:
                            intent=new Intent(mActivity,CameraActivity.class);
                            startActivityForResult(intent, 222);
                        /*intent.setAction("android.media.action.VIDEO_CAPTURE");
                        intent.addCategory("android.intent.category.DEFAULT");
                        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 20000);
                        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 10485760);
                        String str = System.currentTimeMillis()+".mp4";
                        FilePath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/Ymjy/"+str;
                        File mfile=new File(FilePath);
                        if (!mfile.exists()){
                            try {
                                mfile.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Uri mUri = Uri.fromFile(mfile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
                        startActivityForResult(intent, 0);*/
                            break;
                    }
                }else{
                    requestPermissions(permissions.toArray(new String[permissions.size()]), 10001);
                }
                mPopupWindow.dismiss();
            }
        });
        photo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (addType){
                    case 0:
                        goImage();
                        break;
                    case 1:
                        goVideo();
                        break;
                }
                mPopupWindow.dismiss();
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                MyUtils.setWindowAlpa(mActivity,false);
            }
        });

    }
}

package com.emjiayuan.app.activity;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
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
    private Map<Integer,String> imageslist = new HashMap<>();
    private List<Label> labelList = new ArrayList<>();
    private List<Boolean> isOkList = new ArrayList<>();
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
    private static final int BAIDU_ACCESS_FINE_LOCATION = 100;//定位申请跳转

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
        getQnToken();
        getWeiboTypeList();
        requestPermission();
        initMapLocation();

        gvIcons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if ("add".equals(adapter.getItem(i))){
                    PhotoPicker.builder()
                            .setPhotoCount(9-(list.size()-1))
                            .setShowGif(true)
                            .setPreviewEnabled(true)
                            .start(PostActivity.this);
                }else{
                    if (list.contains("add")){
                        list.remove("add");
                    }
                    PhotoPreview.builder()
                            .setPhotos(list)
                            .setCurrentItem(i)
                            .setShowDeleteButton(true)
                            .start(PostActivity.this);
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


                //这里的处理需要注意下
                if(from < to){
                    for(int i=from; i<to; i++){
                        Collections.swap(list, i, i+1);
                    }
                }else if(from > to){
                    for(int i=from; i>to; i--){
                        Collections.swap(list, i, i-1);
                    }
                }

                list.set(to, temp);

                adapter.notifyDataSetChanged();
//                gvIcons.setIgnoreDragPosition(new int[]{adapter.getNoMovePosition()});
            }
        });

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
            formBody.add("images",images.substring(0,images.lastIndexOf(",")));
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
        if (list.contains("add")){
            list.remove("add");
        }
        for (int i = 0; i < paths.size(); i++) {
            String RandomFileName=getRandomFileName();
            list.add(ImageCompress.compressImage(paths.get(i), Environment.getExternalStorageDirectory().getPath()+"/ymjy/"+RandomFileName+".jpg",30));
        }
        if (list.size() == 9) {
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
        if (list.size() != 9) {
            list.add("add");
        }
        adapter = new IconsAdapter(mActivity,list);
        gvIcons.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
                if (list.size()==1){
                    if (post == null) {
                        addWeibo();
                    }else{
                        editWeibo();
                    }
                }else{
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
            String RandomFileName=getRandomFileName();
            final int finalI = i;
            if (list.get(i).contains("http")){
                imageslist.put(finalI,list.get(i));
            }else{
                uploadManager.put(list.get(i), "upload_file/app/"+RandomFileName, token,new UpCompletionHandler() {
                            @Override
                            public void complete(String key, ResponseInfo info, JSONObject res) {
                                //res包含hash、key等信息，具体字段取决于上传策略的设置
                                if(info.isOK()) {
                                    isOkList.add(info.isOK());
                                    imageslist.put(finalI,"http://qiniu.emjiayuan.com/"+key);
                                    Log.i("qiniu", "Upload Success");
                                    if (isOkList.size()==(list.contains("add")?list.size()-1:list.size())){
                                        if (post == null) {
                                            addWeibo();
                                        }else{
                                            editWeibo();
                                        }
                                    }
                                } else {
                                    imageslist.clear();
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
                                        Log.i("qiniu", key + ": " + percent);
                                    }
                                }, null));
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
            default:
                break;

        }
    }
}

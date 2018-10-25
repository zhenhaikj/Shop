package com.emjiayuan.app.fragment.Personal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.activity.CollectionActivity;
import com.emjiayuan.app.activity.CouponActivity2;
import com.emjiayuan.app.activity.EnterpriseActivity;
import com.emjiayuan.app.activity.HelpActivity;
import com.emjiayuan.app.activity.HezuoActivity;
import com.emjiayuan.app.activity.HistoryActivity;
import com.emjiayuan.app.activity.IntegralActivity;
import com.emjiayuan.app.activity.LoginActivity;
import com.emjiayuan.app.activity.LogisticsActivity;
import com.emjiayuan.app.activity.OrderIntegralActivity;
import com.emjiayuan.app.activity.OrderNormalActivity2;
import com.emjiayuan.app.activity.SettingActivity;
import com.emjiayuan.app.activity.SpitActivity;
import com.emjiayuan.app.activity.TopUpActivity;
import com.emjiayuan.app.activity.VipActivity;
import com.emjiayuan.app.activity.address.AddressActivity;
import com.emjiayuan.app.adapter.PersonalTopUpAdapter;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.entity.LoginResult;
import com.emjiayuan.app.entity.Product;
import com.emjiayuan.app.entity.User;
import com.emjiayuan.app.event.LoginSuccessEvent;
import com.emjiayuan.app.fragment.BaseLazyFragment;
import com.emjiayuan.app.imageloader.GlideImageLoader;
import com.emjiayuan.app.widget.HorizontalListView;
import com.google.gson.Gson;
import com.qiyukf.unicorn.api.ConsultSource;
import com.qiyukf.unicorn.api.Unicorn;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yalantis.ucrop.UCrop;
import com.youth.banner.Banner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import me.iwf.photopicker.PhotoPicker;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;


public class PersonalFragment extends BaseLazyFragment implements View.OnClickListener {

    @BindView(R.id.profile_image)
    ImageView profileImage;
    @BindView(R.id.nickname)
    TextView nickname;
    @BindView(R.id.vip)
    TextView vip;
    @BindView(R.id.vip_ll)
    LinearLayout vip_ll;
    @BindView(R.id.balance_ll)
    LinearLayout balance_ll;
    @BindView(R.id.integral_ll)
    LinearLayout integral_ll;
    @BindView(R.id.coupon_ll)
    LinearLayout coupon_ll;
    @BindView(R.id.collection_ll)
    LinearLayout collection_ll;
    @BindView(R.id.spit_ll)
    LinearLayout spit_ll;
    @BindView(R.id.qyjs_ll)
    LinearLayout qyjs_ll;
    @BindView(R.id.wlcx_ll)
    LinearLayout wlcx_ll;
    @BindView(R.id.address_ll)
    LinearLayout address_ll;
    @BindView(R.id.service_ll)
    LinearLayout service_ll;
    @BindView(R.id.help_ll)
    LinearLayout help_ll;
    @BindView(R.id.setting_ll)
    LinearLayout setting_ll;
    @BindView(R.id.normal_ll)
    LinearLayout normalLl;
    @BindView(R.id.integrallp_ll)
    LinearLayout integrallpLl;
    @BindView(R.id.topup_ll)
    LinearLayout topupLl;
    @BindView(R.id.soup_ll)
    LinearLayout soupLl;
    @BindView(R.id.balance)
    TextView balance;
    @BindView(R.id.integral)
    TextView integral;
    @BindView(R.id.count)
    TextView count;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.my_top)
    LinearLayout myTop;
    @BindView(R.id.hezuo_ll)
    LinearLayout hezuoLl;
    @BindView(R.id.record_ll)
    LinearLayout recordLl;
    @BindView(R.id.top_up_center_ll)
    LinearLayout topUpCenterLl;
    @BindView(R.id.hlv)
    HorizontalListView hlv;
    @BindView(R.id.go_center)
    TextView goCenter;
    @BindView(R.id.banner)
    Banner banner;
    private ArrayList<Product> list = new ArrayList<>();//充值
    private PersonalTopUpAdapter adapter;
    private Product product;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_personal2;
    }

    @Override
    protected boolean isLazyLoad() {
        return true;
    }

    /**
     * 初始化沉浸式
     */
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.fitsSystemWindows(false);
        mImmersionBar.keyboardEnable(true).navigationBarWithKitkatEnable(false).init();
    }

    @Override
    protected void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        banner.setImageLoader(new GlideImageLoader());
        banner.setImages(Global.images);
        banner.start();
        vip_ll.getBackground().setAlpha(100);
        vip.getBackground().setAlpha(100);
//        if (Global.loginResult == null) {
//            startActivity(new Intent(getActivity(), LoginActivity.class));
//        }
        user();
        requestTopUp();
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setEnableHeaderTranslationContent(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                user();
            }
        });
        /*new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                final Drawable dw = Global.appTheme.getUser_top_img() != null ? MyUtils.loadImageFromNetwork(Global.appTheme.getUser_top_img()) : mActivity.getDrawable(R.drawable.color_shape);
                // post() 特别关键，就是到UI主线程去更新图片
                myTop.post(new Runnable() {
                    @SuppressLint("NewApi")
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        myTop.setBackground(dw);
                    }
                });
            }
        }).start();*/
    }

    @Override
    protected void setListener() {
        vip_ll.setOnClickListener(this);
        profileImage.setOnClickListener(this);

        balance_ll.setOnClickListener(this);
        integral_ll.setOnClickListener(this);
        coupon_ll.setOnClickListener(this);
        goCenter.setOnClickListener(this);
        topUpCenterLl.setOnClickListener(this);

//        dfk_ll.setOnClickListener(this);
//        dfh_ll.setOnClickListener(this);
//        dsh_ll.setOnClickListener(this);
//        dpj_ll.setOnClickListener(this);
//        thh_ll.setOnClickListener(this);

        normalLl.setOnClickListener(this);
        integrallpLl.setOnClickListener(this);
        topupLl.setOnClickListener(this);
        soupLl.setOnClickListener(this);

        collection_ll.setOnClickListener(this);
        spit_ll.setOnClickListener(this);
        qyjs_ll.setOnClickListener(this);
        wlcx_ll.setOnClickListener(this);
        address_ll.setOnClickListener(this);
        service_ll.setOnClickListener(this);
        help_ll.setOnClickListener(this);
        setting_ll.setOnClickListener(this);
        recordLl.setOnClickListener(this);
        hezuoLl.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (!MyUtils.isFastClick()) {
            return;
        }
        Intent intent = null;
        switch (view.getId()) {
            case R.id.top_up_center_ll:
                startActivity(new Intent(getActivity(), TopUpActivity.class));
                break;
            case R.id.go_center:
                if (product == null) {
                    MyUtils.showToast(mActivity, "请选择充值金额！");
                    return;
                }
                intent = new Intent(getActivity(), TopUpActivity.class);
                intent.putExtra("product", product);
                startActivity(intent);
                break;
            case R.id.vip_ll:
                startActivity(new Intent(getActivity(), VipActivity.class));
                break;
            case R.id.balance_ll:
                startActivity(new Intent(getActivity(), TopUpActivity.class));
                break;
            case R.id.integral_ll:
                startActivity(new Intent(getActivity(), IntegralActivity.class));
                break;
            case R.id.coupon_ll:
                startActivity(new Intent(getActivity(), CouponActivity2.class));
                break;
//------------------------------------------------------
            case R.id.normal_ll:
//                startActivity(new Intent(getActivity(), OrderNormalActivity.class));
                intent = new Intent(getActivity(), OrderNormalActivity2.class);
                intent.putExtra("type", 0);
                startActivity(intent);
                break;
            case R.id.topup_ll:
                intent = new Intent(getActivity(), OrderIntegralActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);
                break;
            case R.id.integrallp_ll:
                intent = new Intent(getActivity(), OrderIntegralActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
            case R.id.soup_ll:
                intent = new Intent(getActivity(), OrderNormalActivity2.class);
                intent.putExtra("type", 3);
                startActivity(intent);
                break;
//            case R.id.thh_ll:
//                startActivity(new Intent(getActivity(), BalanceActivity.class));
            //-----------------------------------------------------------------------------
//                break;
            case R.id.collection_ll:
                startActivity(new Intent(getActivity(), CollectionActivity.class));
                break;
            case R.id.spit_ll:
                startActivity(new Intent(getActivity(), SpitActivity.class));
                break;
            case R.id.qyjs_ll:
                startActivity(new Intent(getActivity(), EnterpriseActivity.class));
                break;
            case R.id.wlcx_ll:
//                startActivity(new Intent(getActivity(), BalanceActivity.class));
                startActivity(new Intent(getActivity(), LogisticsActivity.class));
                break;
            case R.id.address_ll:
                intent = new Intent(getActivity(), AddressActivity.class);
                intent.putExtra("flag", false);
                startActivity(intent);
                break;
            case R.id.service_ll:
                String title = "伊穆家园客服";
                /**
                 * 设置访客来源，标识访客是从哪个页面发起咨询的，用于客服了解用户是从什么页面进入。
                 * 三个参数分别为：来源页面的url，来源页面标题，来源页面额外信息（保留字段，暂时无用）。
                 * 设置来源后，在客服会话界面的"用户资料"栏的页面项，可以看到这里设置的值。
                 */
                ConsultSource source = new ConsultSource("app", "app", "custom information string");
                /**
                 * 请注意： 调用该接口前，应先检查Unicorn.isServiceAvailable()，
                 * 如果返回为false，该接口不会有任何动作
                 *
                 * @param context 上下文
                 * @param title   聊天窗口的标题
                 * @param source  咨询的发起来源，包括发起咨询的url，title，描述信息等
                 */
                Unicorn.openServiceActivity(mActivity, title, source);
//                startActivity(new Intent(getActivity(), KfActivity.class));
                break;
            case R.id.help_ll:
                startActivity(new Intent(getActivity(), HelpActivity.class));
                break;
            case R.id.setting_ll:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.hezuo_ll:
                startActivity(new Intent(getActivity(), HezuoActivity.class));
                break;
            case R.id.record_ll:
                startActivity(new Intent(getActivity(), HistoryActivity.class));
                break;
            case R.id.profile_image:
//                startActivity(new Intent(getActivity(), SettingActivity.class));
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "test"), PhotoPicker.REQUEST_CODE);
                break;
        }
    }

    public void editUserInfo() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        if (Global.loginResult == null) {
            startActivity(new Intent(mActivity, LoginActivity.class));
            return;
        }
        formBody.add("userid", Global.loginResult.getId());
        if (imagepath != null) {
            formBody.add("headimg", imagepath);
        }
//        if (nicknames != null) {
//            formBody.add("nickname", nicknames);
//        }

//new call
        Call call = MyOkHttp.GetCall("user.editUserInfo", formBody);
//请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("------------", e.toString());
//                        myHandler.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                MyUtils.e("------修改用户信息------", result);
                Message message = new Message();
                message.what = 2;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    public void user() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
//        if (Global.loginResult == null) {
//            startActivity(new Intent(mActivity, LoginActivity.class));
//            return;
//        }
        formBody.add("userid", Global.loginResult.getId());

//new call
        Call call = MyOkHttp.GetCall("user.userHome", formBody);
//请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("------------", e.toString());
//                        myHandler.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                MyUtils.e("------获取用户信息------", result);
                Message message = new Message();
                message.what = 0;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    public void requestTopUp() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("producttype", "3");//传递键值对参数
        formBody.add("pageindex", "1");//传递键值对参数
        formBody.add("pagesize", "100");//传递键值对参数
        formBody.add("provinceid", Global.provinceid);
//new call
        Call call = MyOkHttp.GetCall("product.getProductList", formBody);
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
                MyUtils.e("------赠品------", result);
                Message message = new Message();
                message.what = 3;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    private User user;
    private String imagepath;
    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String result = bundle.getString("result");
            switch (msg.what) {

                case 0:
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            user = gson.fromJson(data, User.class);
                            Global.loginResult = gson.fromJson(data, LoginResult.class);
                            setData();
                        } else {
                            MyUtils.showToast(mActivity, message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    refreshLayout.finishRefresh();
                    break;
                case 1:
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            JSONObject jsonObject1 = new JSONObject(data);
                            imagepath = jsonObject1.getString("imgurl");
                            editUserInfo();
//                            MyUtils.showToast(mActivity, message);
                        } else {
                            MyUtils.showToast(mActivity, message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            MyUtils.showToast(mActivity, message);
                            user();
                        } else {
                            MyUtils.showToast(mActivity, message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            JSONArray jsonArray = new JSONArray(data);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Product product = gson.fromJson(jsonArray.getJSONObject(i).toString(), Product.class);
                                list.add(product);
                            }
                            adapter = new PersonalTopUpAdapter(mActivity, list);
                            hlv.setAdapter(adapter);
                            hlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    product = list.get(position);
                                    adapter.setSelected(position);
                                }
                            });
                        } else {
                            MyUtils.showToast(mActivity, message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

            }
        }
    };


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(LoginSuccessEvent event) {
        user();
    }

    private void setData() {
        nickname.setText(user.getShowname());
        vip.setText(user.getClassname() + "(购物享受" + Double.parseDouble(user.getDiscount()) / 10 + "折)");
        balance.setText(user.getYue());
        integral.setText(user.getJifen());
        count.setText(user.getCouponcount());
        Glide.with(mActivity).load(user.getHeadimg()).apply(RequestOptions.circleCropTransform().placeholder(R.drawable.default_tx).error(R.drawable.default_tx)).into(profileImage);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    /*
     * 剪切图片
     */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);

        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, 3);
    }

    private void startCrop(Uri sourceUri) {
//        Uri sourceUri = Uri.parse("http://star.xiziwang.net/uploads/allimg/140512/19_140512150412_1.jpg");
        //裁剪后保存到文件中
        Uri destinationUri = Uri.fromFile(new File(mActivity.getCacheDir(), "SampleCropImage.png"));
        UCrop.of(sourceUri, destinationUri).withAspectRatio(1, 1).withMaxResultSize(300, 300).start(mActivity, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PhotoPicker.REQUEST_CODE) {
//                List<String> photos = null;
                if (data != null) {
//                    photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                    Uri uri = data.getData();
//                    crop(uri);
                    startCrop(uri);
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                // 从剪切图片返回的数据
                if (data != null) {
                    /*Bitmap bitmap = data.getParcelableExtra("data");
                    //防止低版本没有办法获取数据，导致奔溃
                    if(bitmap ==null){
                        return;
                    }
                    //第一步:将Bitmap压缩至字节数组输出流ByteArrayOutputStream
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
                    //第二步:利用Base64将字节数组输出流中的数据转换成字符串String
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    String imageString = new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
                    uploadImgFor64(imageString);*/
                    Uri croppedFileUri = UCrop.getOutput(data);
                    //获取默认的下载目录
                    String downloadsDirectoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                    String filename = String.format("%d_%s", Calendar.getInstance().getTimeInMillis(), croppedFileUri.getLastPathSegment());
                    Bitmap bitmap = BitmapFactory.decodeFile(croppedFileUri.getPath());
                    if (bitmap == null) {
                        return;
                    }
                    //第一步:将Bitmap压缩至字节数组输出流ByteArrayOutputStream
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
                    //第二步:利用Base64将字节数组输出流中的数据转换成字符串String
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    String imageString = new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
                    uploadImgFor64("data:image/png;base64," + imageString);
                    File saveFile = new File(downloadsDirectoryPath, filename);
                    //保存下载的图片
                    FileInputStream inStream = null;
                    FileOutputStream outStream = null;
                    FileChannel inChannel = null;
                    FileChannel outChannel = null;
                    try {
                        inStream = new FileInputStream(new File(croppedFileUri.getPath()));
                        outStream = new FileOutputStream(saveFile);
                        inChannel = inStream.getChannel();
                        outChannel = outStream.getChannel();
                        inChannel.transferTo(0, inChannel.size(), outChannel);
//                        Toast.makeText(this, "裁切后的图片保存在：" + saveFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            outChannel.close();
                            outStream.close();
                            inChannel.close();
                            inStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        //裁切失败
        if (resultCode == UCrop.RESULT_ERROR) {
            Toast.makeText(mActivity, "裁切图片失败", Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadImgFor64(String imageString) {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("imagedata", imageString);

//new call
        Call call = MyOkHttp.GetCall("system.uploadImgFor64", formBody);
//请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("------------", e.toString());
//                        myHandler.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                MyUtils.e("------上传图片------", result);
                Message message = new Message();
                message.what = 1;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

}
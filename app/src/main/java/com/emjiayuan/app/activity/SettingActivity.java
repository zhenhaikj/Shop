package com.emjiayuan.app.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.GlideUtil;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.Utils.SpUtils;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.entity.User;
import com.emjiayuan.app.widget.MyListView;
import com.google.gson.Gson;
import com.yalantis.ucrop.UCrop;
import com.youth.banner.Banner;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.save)
    TextView save;

    @BindView(R.id.line_top)
    View lineTop;
    @BindView(R.id.tx)
    ImageView tx;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.nickname)
    TextView nickname;
    @BindView(R.id.version)
    TextView version;
    @BindView(R.id.login_out)
    Button loginOut;
    private String imagepath;
    private String nicknames;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        title.setText("设置");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyUtils.isFastClick()) {
                    return;
                }
                finish();
            }
        });
        user();
    }
    public void user() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        if (Global.loginResult == null) {
            startActivity(new Intent(mActivity, LoginActivity.class));
            return;
        }
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

    public void editUserInfo() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        if (Global.loginResult == null) {
            startActivity(new Intent(mActivity, LoginActivity.class));
            return;
        }
        formBody.add("userid", Global.loginResult.getId());
        if (imagepath!=null){
            formBody.add("headimg", imagepath);
        }
        if (nicknames != null) {
            formBody.add("nickname", nicknames);
        }

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

    public void setData(){
        Glide.with(mActivity).load(user.getHeadimg()).apply(RequestOptions.circleCropTransform().placeholder(R.drawable.default_tx).error(R.drawable.default_tx)).into(tx);
        username.setText(user.getUsername());
        nickname.setText(user.getNickname());
        version.setText(MyUtils.getAppVersionName(mActivity));
    }
    private User user;
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
                            user=gson.fromJson(data, User.class);
                            setData();
                        } else {
                            MyUtils.showToast(mActivity, message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            JSONObject jsonObject1=new JSONObject(data);
                            imagepath=jsonObject1.getString("imgurl");
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


            }
        }
    };

    @Override
    protected void setListener() {
        loginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyUtils.isFastClick()){
                    return;
                }
                Global.loginResult=null;
                SpUtils.putObject(mActivity,"loginResult",Global.loginResult);
                startActivity(new Intent(mActivity,LoginActivity.class));
                finish();
            }
        });
        tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyUtils.isFastClick()){
                    return;
                }
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "test"), PhotoPicker.REQUEST_CODE);
//                PhotoPicker.builder()
//                        .setPhotoCount(1)
//                        .start(SettingActivity.this);
            }
        });
        nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyUtils.isFastClick()){
                    return;
                }
                final EditText et = new EditText(mActivity);
                et.setText(user.getNickname());
                new AlertDialog.Builder(mActivity).setTitle("修改昵称")
                        .setIcon(android.R.drawable.sym_def_app_icon)
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //按下确定键后的事件
                                nicknames=et.getText().toString();
                                editUserInfo();
//                                Toast.makeText(getApplicationContext(), et.getText().toString(),Toast.LENGTH_LONG).show();
                            }
                        }).setNegativeButton("取消",null).show();
            }
        });
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
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "SampleCropImage.png"));
        UCrop.of(sourceUri, destinationUri).withAspectRatio(1, 1).withMaxResultSize(300, 300).start(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK ){
            if(requestCode == PhotoPicker.REQUEST_CODE){
//                List<String> photos = null;
                if (data != null) {
//                    photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                    Uri uri=data.getData();
//                    crop(uri);
                    startCrop(uri);
                }
            }else if (requestCode==UCrop.REQUEST_CROP){
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
                    if(bitmap ==null){
                        return;
                    }
                    //第一步:将Bitmap压缩至字节数组输出流ByteArrayOutputStream
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
                    //第二步:利用Base64将字节数组输出流中的数据转换成字符串String
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    String imageString = new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
                    uploadImgFor64("data:image/png;base64,"+imageString);
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
            Toast.makeText(this, "裁切图片失败", Toast.LENGTH_SHORT).show();
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

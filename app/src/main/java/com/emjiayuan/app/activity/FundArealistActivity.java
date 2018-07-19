package com.emjiayuan.app.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.adapter.CityAdapter;
import com.emjiayuan.app.entity.AreaModel;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.widget.QuickLocationBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 公积金城市列表页
 * Created by zst on 2017/8/23.
 */
public class FundArealistActivity extends BaseActivity implements View.OnClickListener {
    public static final int INTENT_REQUEST_CODE = 100;//请求码
    public static final int INTENT_RESULT_SUCC_CODE = 101;//请求码正确
    public static final int INTENT_RESULT_ERR_CODE = 102;//请求码错误
    public static final String INTENT_RESULT_CODE_VAULE = "intent_result_code_value";//返回值城市代码
    public static final String INTENT_RESULT_NAME_VAULE = "intent_result_name_value";//返回值城市名称
    private static final int BAIDU_ACCESS_FINE_LOCATION = 100;//定位申请跳转

    @BindView(R.id.city_list)
     ListView mCityLit;
    @BindView(R.id.city_list_search)
     ListView city_list_search;
    @BindView(R.id.et_search)
     EditText et_search;
    @BindView(R.id.ll_city_search_list)
     LinearLayout ll_city_search_list;
    @BindView(R.id.ll_location)
     LinearLayout ll_location;
    @BindView(R.id.tv_location_name)
     TextView tv_location_name;

    private CityAdapter adapter;
    private CityAdapter adapterSearch;
    @BindView(R.id.back)
     TextView back;
    @BindView(R.id.city_dialog)
     TextView overlay;
    @BindView(R.id.city_loactionbar)
     QuickLocationBar mQuicLocationBar;
    private List<AreaModel> mCityNames;
    private List<AreaModel> mCityNamesSearch;
    private String  resultString;
    @Override
    protected int setLayoutId() {
        return R.layout.activity_fund_area_list;
    }
    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mQuicLocationBar.setTextDialog(overlay);

        mCityNames= new ArrayList<>();
        adapter = new CityAdapter(FundArealistActivity.this, mCityNames);
        mCityLit.setAdapter(adapter);

        if (Global.city_list!=null){
            mCityNames=Global.city_list;
            adapter.setList(mCityNames);
        }else{
            getCityList();
        }
//        initList();
        requestPermission();//请求权限
        listenerSearchEdit();
        initMapLocation();//定位相关
    }

    @Override
    protected void setListener() {
        back.setOnClickListener(this);
        back.setOnClickListener(this);
        tv_location_name.setOnClickListener(this);
        mQuicLocationBar.setOnTouchLitterChangedListener(new LetterListViewListener());
        mCityLit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!MyUtils.isFastClick()){
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("name", mCityNames.get(i).getCityName());
                setResult(1, intent);
                finish();
            }
        });
        city_list_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!MyUtils.isFastClick()){
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("name", mCityNamesSearch.get(i).getCityName());
                setResult(1, intent);
                finish();
            }
        });
    }

    //搜索输入监听
    private void listenerSearchEdit() {
        mCityNamesSearch = new ArrayList<>();
        adapterSearch = new CityAdapter(FundArealistActivity.this, mCityNamesSearch);
        city_list_search.setAdapter(adapterSearch);

        et_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入的内容变化的监听
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // 输入前的监听
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 输入后的监听
                if (TextUtils.isEmpty(et_search.getText().toString())) {
                    ll_city_search_list.setVisibility(View.GONE);
                    ll_location.setVisibility(View.VISIBLE);
                    mCityLit.setVisibility(View.VISIBLE);
                    mQuicLocationBar.setVisibility(View.VISIBLE);
                } else {
                    ll_city_search_list.setVisibility(View.VISIBLE);
                    ll_location.setVisibility(View.GONE);
                    mCityLit.setVisibility(View.GONE);
                    mQuicLocationBar.setVisibility(View.GONE);

                    mCityNamesSearch.clear();

                    for (int i = 0; i < mCityNames.size(); i++) {
                        if (mCityNames.get(i).getCityName().contains(et_search.getText().toString())) {
                            mCityNamesSearch.add(mCityNames.get(i));
                        }
                    }

                    adapterSearch.notifyDataSetChanged();
                    //UiUtil.showToast(FundArealistActivity.this, et_search.getText().toString());
                }
            }
        });
    }


    public void setItemClick(AreaModel cityModel) {
            if (cityModel!=null){
                String cityName = cityModel.getCityName();
                Intent intent = new Intent();
                intent.putExtra("name", cityName);
                setResult(1, intent);
            }
            finish();
    }

    private class LetterListViewListener implements
            QuickLocationBar.OnTouchLetterChangedListener {

        @Override
        public void touchLetterChanged(String s) {
            // TODO Auto-generated method stub
            if (adapter!=null){
                Map<String, Integer> alphaIndexer = adapter.getCityMap();
                //Toast.makeText(FundArealistActivity.this, s + "。。" + alphaIndexer.get(s) + "..." + alphaIndexer.size(), Toast.LENGTH_SHORT).show();
                if (alphaIndexer!=null){
                    if (alphaIndexer.get(s) != null) {
                        int position = alphaIndexer.get(s);
                        mCityLit.setSelection(position);
                        //Toast.makeText(FundArealistActivity.this, position + "", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

    }
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Global.city_list=mCityNames;
            adapter.setList(mCityNames);
        }
    };
    private void getCityList() {
        Runnable run = new Runnable() {

            @Override
            public void run() {
                readTextFromSDcard();
                try {
                    mCityNames=new ArrayList<AreaModel>();
                    JSONArray jsonArray=new JSONArray(resultString);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String name=jsonObject.getString("name");
                        String code=jsonObject.getString("CityID");
                        AreaModel model=new AreaModel(code,name);
                        mCityNames.add(model);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Collections.sort(mCityNames);

                handler.sendEmptyMessage(0);
            }
        };
        new Thread(run).start();
    }

    @Override
    public void onClick(View v) {
        if (!MyUtils.isFastClick()){
            return;
        }
        int id = v.getId();
        switch (id) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_location_name:
                AreaModel cityModel = null;
                String locationName = tv_location_name.getText().toString();
                for (int i = 0; i < mCityNames.size(); i++) {
                    if (mCityNames.get(i).getCityName().contains(locationName)) {
                        cityModel = mCityNames.get(i);
                        break;
                    }else if(locationName.contains(mCityNames.get(i).getCityName())) {
                        cityModel = mCityNames.get(i);
                        break;
                    }
                }

                //UiUtil.showToast(FundArealistActivity.this, cityModel.getCityName());
                setItemClick(cityModel);
                break;
        }

    }

    //定位相关:初始化定位
    private void initMapLocation() {

        // 定位初始化
        MyLocationListenner myListener = new MyLocationListenner();
        LocationClient mLocClient = new LocationClient(FundArealistActivity.this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(10000000);//自动定位间隔
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
            String curCity = location.getCity();
//            String curCity = location.getProvince();
            if (TextUtils.isEmpty(curCity)) {
                tv_location_name.setText("定位中...");
            } else {
                tv_location_name.setText(curCity);
            }

        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    //申请地图相关权限
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            ArrayList<String> permissions = new ArrayList<>();
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                    tv_location_name.setText("定位权限未开启");
                }
                break;
            default:
                break;

        }
    }

    //将传入的is一行一行解析读取出来出来
    private void readTextFromSDcard() {
        InputStreamReader inputStreamReader;
        try {
            inputStreamReader = new InputStreamReader(getAssets().open("city_json.txt"), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(
                    inputStreamReader);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            inputStreamReader.close();
            bufferedReader.close();
            resultString = stringBuilder.toString();
            Log.i("TAG", stringBuilder.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

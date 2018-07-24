package com.emjiayuan.app.fragment.ShoppingCar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.lwkandroid.stateframelayout.StateFrameLayout;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.activity.GoodsDetailActivity;
import com.emjiayuan.app.activity.LoginActivity;
import com.emjiayuan.app.activity.OrderConfirmActivity;
import com.emjiayuan.app.adapter.ShoppingCarAdapter;
import com.emjiayuan.app.adapter.ShoppingCarAdapter2;
import com.emjiayuan.app.entity.CarBean;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.entity.OrderComfirm;
import com.emjiayuan.app.event.CarUpdateEvent;
import com.emjiayuan.app.fragment.BaseLazyFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static com.emjiayuan.app.Utils.DensityUtil.dp2px;


public class ShoppingCarFragment2 extends BaseLazyFragment implements View.OnClickListener {

    @BindView(R.id.txt_content)
    TextView txtContent;
    @BindView(R.id.manage)
    TextView manage;
    @BindView(R.id.lv_goods)
    SwipeMenuListView lvGoods;
    @BindView(R.id.checkAll)
    CheckBox checkAll;
    @BindView(R.id.check)
    TextView check;
    @BindView(R.id.pay)
    TextView pay;
    @BindView(R.id.order)
    TextView order;
    @BindView(R.id.stateLayout)
    StateFrameLayout stateLayout;
    private ShoppingCarAdapter2 adapter;
    private ArrayList<CarBean> carBeanList;
    public List<CarBean> selectList=new ArrayList<>();
    //监听来源
    public boolean mIsFromItem = false;
    public Double total = 0.0;
    private String cartids;
    private String productids;


    @Override
    protected int setLayoutId() {
        return R.layout.fragment_shopping_car;
    }

    /**
     * 初始化沉浸式
     */
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarDarkFont(true, 0.2f); //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
        mImmersionBar.statusBarColor(R.color.white);
        mImmersionBar.fitsSystemWindows(true);
        mImmersionBar.keyboardEnable(true).navigationBarWithKitkatEnable(false).init();
    }

    @Override
    protected void initView() {
        super.initView();
//        if (Global.loginResult==null){
//            startActivityForResult(new Intent(mActivity,LoginActivity.class),100);
//            return;
//        }
//        reqCarList();
    }

    @Override
    protected void initData() {
        if (Global.loginResult==null){
            startActivityForResult(new Intent(mActivity,LoginActivity.class),100);
            return;
        }
        reqCarList();
        //        adapter=new ShoppingCarAdapter(getActivity(), new ArrayList<Product>());
//        adapter = new ShoppingCarAdapter(getActivity(), Global.list);
//        lvGoods.setAdapter(adapter);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getActivity());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
//                openItem.setBackground(Color.RED);
                // set item width
                openItem.setWidth(dp2px(getActivity(), 90));
                // set item title
                openItem.setTitle("删除");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(getActivity(), 90));
                // set a icon
                deleteItem.setIcon(R.drawable.delete);
                // add to menu
//                menu.addMenuItem(deleteItem);
            }
        };

// set creator
        lvGoods.setMenuCreator(creator);
        lvGoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!MyUtils.isFastClick()){
                    return;
                }
                Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
                intent.putExtra("productid", carBeanList.get(i).getProductid());
                startActivity(intent);
            }
        });
        lvGoods.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
//                        Global.list.remove(position);
//                        adapter.notifyDataSetChanged();
                        deleteCar(carBeanList.get(position).getCartid(), position);
                        break;
                    case 1:
                        // delete
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        lvGoods.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
    }
    //对item导致maincheckbox改变做监听
    public interface AllCheckListener {
        void onCheckedChanged(boolean b);
    }


    @Override
    protected void setListener() {

        checkAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //当监听来源为点击item改变maincbk状态时不在监听改变，防止死循环
                if (mIsFromItem) {
                    mIsFromItem = false;
                    Log.e("mainCheckBox", "此时我不可以触发");
                    return;
                }

                //改变数据
                for (CarBean carBean : carBeanList) {
                    carBean.setChecked(b);
                }
                Map<Integer,Boolean> map=new HashMap<>();
                for (int i=0;i<carBeanList.size();i++){
                    if (b){
                        map.put(i,true);
                    }else{
                        map.remove(i);
                    }
                }
                adapter.setMap(map);
                total=0.00;
                DecimalFormat df = new DecimalFormat("#0.00");
                if (b){
                    for (CarBean carBean : carBeanList) {
                        total=total+Integer.parseInt(carBean.getCartnum())*Double.parseDouble(carBean.getPrice());
                    }
                    pay.setText("¥"+df.format(total));
                }else{
                    pay.setText("¥"+total);
                }
                //刷新listview
                adapter.notifyDataSetChanged();
            }
        });
        order.setOnClickListener(this);
        manage.setOnClickListener(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(CarUpdateEvent event) {
        setPay(event.getCarBeanList());
    }
    public void setPay(List<CarBean> carBeanList){
        total=0.00;
        selectList.clear();
        cartids="";
        productids="";
        DecimalFormat df = new DecimalFormat("#0.00");
        for (CarBean carBean : carBeanList) {
            if (carBean.isChecked()){
                cartids+=carBean.getCartid()+",";
                productids+=carBean.getProductid()+"|"+carBean.getCartnum()+",";
                selectList.add(carBean);
                total=total+Integer.parseInt(carBean.getCartnum())*Double.parseDouble(carBean.getPrice());
            }
        }
        pay.setText("¥"+df.format(total));
    }
    @Override
    public void onClick(View view) {
        if (!MyUtils.isFastClick()){
            return;
        }
        switch (view.getId()) {
            case R.id.order:
                if (selectList.size()==0){
                    MyUtils.showToast(mActivity,"请选择商品！");
                    return;
                }
                confirmOrder();

                break;
            case R.id.manage:
                reqCarList();
                break;
        }
    }

    public void reqCarList() {
        if (!checkNetwork()){
            stateLayout.changeState(StateFrameLayout.NET_ERROR);
            return;
        }
        stateLayout.changeState(StateFrameLayout.LOADING);
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体

        formBody.add("userid", Global.loginResult.getId());
        formBody.add("provinceid",Global.provinceid);
        Log.d("------参数------", formBody.build().toString());
//new call
        Call call = MyOkHttp.GetCall("cart.getCartList", formBody);
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
                MyUtils.e("------获取产品结果------", result);
                Message message = new Message();
                message.what = 0;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    public void confirmOrder() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("userid", Global.loginResult.getId());
//        formBody.add("addressprovince", "浙江省");
        formBody.add("couponid", "");
        formBody.add("cartids",cartids.substring(0,cartids.lastIndexOf(",")));
        formBody.add("provinceid",Global.provinceid);
//        formBody.add("productids", productids.substring(0,productids.lastIndexOf(",")));
        Log.d("------参数------", formBody.build().toString());
//new call
        Call call = MyOkHttp.GetCall("order.confirmOrder", formBody);
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
                MyUtils.e("------提交订单结果------", result);
                Message message = new Message();
                message.what = 2;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    public void deleteCar(String cartid, final int position) {
        if (!checkNetwork()){
            stateLayout.changeState(StateFrameLayout.NET_ERROR);
            return;
        }
//        stateLayout.changeState(StateFrameLayout.LOADING);
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        if (Global.loginResult == null) {
            startActivity(new Intent(mActivity, LoginActivity.class));
            return;
        }
        formBody.add("userid", Global.loginResult.getId());
        formBody.add("cartid", cartid);
        Log.d("------参数------", formBody.build().toString());
//new call
        Call call = MyOkHttp.GetCall("cart.removeCart", formBody);
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
                MyUtils.e("------删除购物车结果------", result);
                Message message = new Message();
                message.what = 1;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                bundle.putInt("position", position);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    private OrderComfirm orderComfirm;
    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String result = bundle.getString("result");
            int position = bundle.getInt("position");
            switch (msg.what) {
                case 0:
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if ("200".equals(code)) {
                            JSONArray dataArray = new JSONArray(data);
                            carBeanList = new ArrayList<>();
                            for (int i = 0; i < dataArray.length(); i++) {
                                carBeanList.add(gson.fromJson(dataArray.getJSONObject(i).toString(), CarBean.class));
                            }
                            if (carBeanList.size()==0){
                                stateLayout.changeState(StateFrameLayout.EMPTY);
                                return;
                            }else{
                                stateLayout.changeState(StateFrameLayout.SUCCESS);
                            }
                            adapter = new ShoppingCarAdapter2(mActivity, carBeanList, lvGoods, new AllCheckListener() {
                                @Override
                                public void onCheckedChanged(boolean b) {
                                    //根据不同的情况对maincheckbox做处理
                                    if (!b && !checkAll.isChecked()) {
                                        return;
                                    } else if (!b && checkAll.isChecked()) {
                                        mIsFromItem = true;
                                        checkAll.setChecked(false);
                                    } else if (b) {
                                        mIsFromItem = false;
                                        checkAll.setChecked(true);
                                    }
                                 }
                            });
                            lvGoods.setAdapter(adapter);
                        } else {
                            MyUtils.showToast(mActivity,message);
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
                            stateLayout.changeState(StateFrameLayout.SUCCESS);
                            MyUtils.showToast(mActivity, message);
                            reqCarList();
                        } else {
                            MyUtils.showToast(mActivity,message);
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
//                            MyUtils.showToast(mActivity, result);
                            JSONObject jsonObject1 = new JSONObject(data);
                            orderComfirm=gson.fromJson(jsonObject1.toString(),OrderComfirm.class);
                            Intent intent=new Intent(mActivity,OrderConfirmActivity.class);
                            intent.putExtra("orderComfirm",orderComfirm);
                            startActivity(intent);
                        } else {
                            MyUtils.showToast(mActivity, result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RESULT_OK){
            reqCarList();
        }
    }
}
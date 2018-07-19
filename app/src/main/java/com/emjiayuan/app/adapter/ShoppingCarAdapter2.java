package com.emjiayuan.app.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.GlideUtil;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.entity.CarBean;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.event.CarUpdateEvent;
import com.emjiayuan.app.fragment.ShoppingCar.ShoppingCarFragment2.AllCheckListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

/**
 * Created by cyl on 2018年5月10日 09:52:49.
 */

public class ShoppingCarAdapter2 extends BaseAdapter {
    private Context mContext;
    private static String TAG = "MenuAdapter";

    private ArrayList<CarBean> grouplists = new ArrayList<>();
    private LayoutInflater mInflater;
    private ListView lv;
    private CheckBox checkBox;
    private int index=-1;
    private String[] text;
    private myWatcher mWatcher;
    private ViewHolder holder;
    private AllCheckListener allCheckListener;
    private Map<Integer,Boolean> map=new HashMap<>();// 存放已被选中的CheckBox

    public Map<Integer, Boolean> getMap() {
        return map;
    }

    public void setMap(Map<Integer, Boolean> map) {
        this.map = map;
        notifyDataSetChanged();
    }

    public void setGrouplists(ArrayList<CarBean> grouplists) {
        this.grouplists = grouplists;
        notifyDataSetChanged();
    }
    public void delete(int position){
        grouplists.remove(position);
        notifyDataSetChanged();
    }
    public ShoppingCarAdapter2(Context mContext, ArrayList<CarBean> grouplists, ListView lv, AllCheckListener listener) {
        super();
        this.mContext = mContext;
        this.grouplists = grouplists;
        this.mInflater = LayoutInflater.from(mContext);
        this.lv=lv;
        this.allCheckListener=listener;
        text=new String[4];
//        text=new String[grouplists.size()];
    }

    @Override
    public int getCount() {
        return grouplists.size();
//        return 4;
    }

    @Override
    public CarBean getItem(int position) {
        return grouplists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.d("getView()",position+"");
//        holder= new ViewHolder();
//        holder holder= null;
        if (convertView == null) {// 如果是第一次显示该页面(要记得保存到holder中供下次直接从缓存中调用)
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.car_item, null);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.et_count = (EditText) convertView.findViewById(R.id.et_count);
            holder.up = (TextView) convertView.findViewById(R.id.up);
            holder.down = (TextView) convertView.findViewById(R.id.down);
            holder.check = (CheckBox) convertView.findViewById(R.id.check);
            holder.check_ll =  convertView.findViewById(R.id.check_ll);
            convertView.setTag(holder);
        } else {// 如果之前已经显示过该页面，则用holder中的缓存直接刷屏
            holder = (ViewHolder) convertView.getTag();
        }

        final CarBean item = grouplists.get(position);
        int count=Integer.parseInt(item.getCartnum());
        GlideUtil.loadImageViewLoding(mContext,item.getImages(),holder.icon,R.drawable.empty_img,R.drawable.empty_img);
        holder.name.setText(item.getName());
        holder.price.setText("¥"+item.getPrice());
        holder.et_count.setText(""+count);
        holder.up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCar(item.getProductid(),"1","1",position);
            }
        });
        holder.down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCar(item.getProductid(),"2","1",position);
            }
        });
//        holder.check.setChecked(item.isChecked());
        holder.check_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (holder.check.isChecked()){
//                    holder.check.setChecked(false);
//                }else{
//                    holder.check.setChecked(true);
//                }
            }
        });
        holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    map.put(position,true);
                }else {
                    map.remove(position);
                }
                item.setChecked(b);
                //监听每个item，若所有checkbox都为选中状态则更改main的全选checkbox状态
                EventBus.getDefault().post(new CarUpdateEvent(grouplists));
                for (CarBean carBean : grouplists) {
                    if (!carBean.isChecked()) {
                        allCheckListener.onCheckedChanged(false);
                        return;
                    }
                }
                allCheckListener.onCheckedChanged(true);

            }
        });
        if(map!=null&&map.containsKey(position)){
            holder.check.setChecked(true);
        }else {
            holder.check.setChecked(false);
        }
        return convertView;
    }
    public class ViewHolder {
        public ImageView icon;
        public TextView name;
        public TextView price;
        public EditText et_count;
        public TextView down;
        public TextView up;
        public CheckBox check;
        public LinearLayout check_ll;
    }
    class myWatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // TODO Auto-generated method stub


        }

        @Override
        public void afterTextChanged(Editable s) {
            text[index]=s.toString();//为输入的位置内容设置数组管理器，防止item重用机制导致的上下内容一样的问题
        }

    }
    public void updateCar(String productid, String option, String num, final int position) {

        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体

        formBody.add("userid", Global.loginResult.getId());
        formBody.add("productid", productid);
        formBody.add("option", option);
        formBody.add("num", num);
        formBody.add("provinceid",Global.provinceid);
        Log.d("------参数------", formBody.build().toString());
//new call
        Call call = MyOkHttp.GetCall("cart.addOrUpdateCart", formBody);
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
                MyUtils.e("------操作购物车结果------", result);
                Message message = new Message();
                message.what = 0;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                bundle.putInt("position", position);
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
            int position=bundle.getInt("position");
            switch (msg.what) {
                case 0:
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        if ("200".equals(code)) {
                            JSONObject object=new JSONObject(data);
                            String cartnum=object.getString("cartnum");
                            updateItem(position,cartnum);
                            EventBus.getDefault().post(new CarUpdateEvent(grouplists));
                        } else {
                            MyUtils.showToast(mContext,message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    /**
     * 刷新指定item
     */
    private void updateItem(int index,String cartnum) {
        if (lv == null) {
            return;
        }
        // 获取当前可以看到的item位置
        int visiblePosition = lv.getFirstVisiblePosition();
        // 如添加headerview后 firstview就是hearderview
        // 所有索引+1 取第一个view
        // View view = listview.getChildAt(index - visiblePosition + 1);
        // 获取点击的view
        View view = lv.getChildAt(index-visiblePosition);
        if (view != null) {
            CarBean carBean = grouplists.get(index);
            carBean.setCartnum(cartnum);
            EditText etcount=(EditText)view.findViewById(R.id.et_count);
            etcount.setText(cartnum);
        }

    }
}

package com.emjiayuan.app.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.emjiayuan.app.BaseActivity;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyOkHttp;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.adapter.CityItemAdapter;
import com.emjiayuan.app.adapter.KeywordsAdapter;
import com.emjiayuan.app.adapter.RecordsAdapter;
import com.emjiayuan.app.adapter.SearchAdapter;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.entity.Product;
import com.emjiayuan.app.record.RecordsDao;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

import static com.emjiayuan.app.Utils.DensityUtil.dp2px;

public class SearchActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.gv_pop)
    GridView gvPop;
    @BindView(R.id.lv_history)
    SwipeMenuListView lvHistory;
    @BindView(R.id.history_ll)
    LinearLayout historyLl;
    @BindView(R.id.delete_btn)
    TextView deleteBtn;
    @BindView(R.id.pre_ll)
    LinearLayout preLl;
    private SearchAdapter adapter;
    private KeywordsAdapter keywordAdapter;
    private ArrayList<Product> list = new ArrayList<>();
    private ArrayList<String> listKeyword = new ArrayList<>();
    private String keyword = "";
    private List<String> searchRecordsList;
    private List<String> tempList;
    private RecordsDao recordsDao;
    private RecordsAdapter recordsAdapter;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initData() {
        getKeywordTop();
        recordsDao = new RecordsDao(this);
        searchRecordsList = new ArrayList<>();
        tempList = new ArrayList<>();
        tempList.addAll(recordsDao.getRecordsList());

        reversedList();
        //第一次进入判断数据库中是否有历史记录，没有则不显示
        checkRecordsSize();
        recordsAdapter = new RecordsAdapter(mActivity, searchRecordsList);
        lvHistory.setAdapter(recordsAdapter);
        lvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                keyword=searchRecordsList.get(position);
                etSearch.setText(keyword);
                request();
                recordsDao.addRecords(etSearch.getText().toString());
                preLl.setVisibility(View.GONE);
            }
        });
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        mActivity);
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
//                openItem.setBackground(Color.RED);
                // set item width
                openItem.setWidth(dp2px(mActivity, 90));
                // set item title
                openItem.setTitle("删除");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
//                SwipeMenuItem deleteItem = new SwipeMenuItem(
//                        getActivity());
                // set item background
//                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
//                        0x3F, 0x25)));
                // set item width
//                deleteItem.setWidth(dp2px(getActivity(), 90));
                // set a icon
//                deleteItem.setIcon(R.drawable.delete);
                // add to menu
//                menu.addMenuItem(deleteItem);
            }
        };

// set creator
        lvHistory.setMenuCreator(creator);
        lvHistory.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        recordsDao.delete(searchRecordsList.get(position));
                        searchRecordsList.remove(position);
                        recordsAdapter.notifyDataSetChanged();
                        historyLl.setVisibility(searchRecordsList.size()>0?View.VISIBLE:View.GONE);
                        break;

                }
                return false;
            }
        });
        lvHistory.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
    }

    //当没有匹配的搜索数据的时候不显示历史记录栏
    private void checkRecordsSize() {
        if (searchRecordsList.size() == 0) {
            historyLl.setVisibility(View.GONE);
        } else {
            historyLl.setVisibility(View.VISIBLE);
        }
    }


    //颠倒list顺序，用户输入的信息会从上依次往下显示
    private void reversedList() {
        searchRecordsList.clear();
        for (int i = tempList.size() - 1; i >= 0; i--) {
            searchRecordsList.add(tempList.get(i));
        }
    }

    @Override
    protected void initView() {
//        Product item1 = new Product("清真伊穆家园牛肉面标准化园牛肉面标准化汤料2.5斤试用装【中端型】", "¥60.00", "$70.00", R.drawable.img1);
//        for (int i = 0; i < 20; i++) {
//            list.add(item1);
//        }

        lv.setOnItemClickListener(this);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                keyword = charSequence.toString();
                if ("".equals(keyword)){
                    preLl.setVisibility(View.VISIBLE);
                    tempList.clear();
                    tempList.addAll(recordsDao.getRecordsList());
                    reversedList();
                    checkRecordsSize();
                    recordsAdapter.notifyDataSetChanged();
                }
//                request();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // do something
                    preLl.setVisibility(View.GONE);
                    keyword = etSearch.getText().toString();
                    preLl.setVisibility("".equals(keyword)?View.VISIBLE:View.GONE);
                    if ("".equals(keyword)){
                        tempList.clear();
                        tempList.addAll(recordsDao.getRecordsList());
                        reversedList();
                        checkRecordsSize();
                        recordsAdapter.notifyDataSetChanged();
                    }
                    request();
                    recordsDao.addRecords(etSearch.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    public void request() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add("keyword", keyword);//传递键值对参数
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
                MyUtils.e("------搜索------", result);
                Message message = new Message();
                message.what = 0;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            }
        });
    }

    public void getKeywordTop() {
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体

//new call
        Call call = MyOkHttp.GetCall("public.getKeywordTop", formBody);
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
                MyUtils.e("------热搜------", result);
                Message message = new Message();
                message.what = 1;
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
                case 0:
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.getString("code");
                        String message = jsonObject.getString("message");
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        list.clear();
                        if ("200".equals(code)) {
                            JSONArray jsonArray = new JSONArray(data);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Product product = gson.fromJson(jsonArray.getJSONObject(i).toString(), Product.class);
                                list.add(product);
                            }
                            adapter = new SearchAdapter(mActivity, list);
                            lv.setAdapter(adapter);
                        } else {
                            if (adapter!=null){
                                adapter.notifyDataSetChanged();
                            }

                            MyUtils.showToast(mActivity, message);
                        }
                    } catch (JSONException e) {
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
                        listKeyword.clear();
                        if ("200".equals(code)) {
                            JSONArray jsonArray = new JSONArray(data);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object=jsonArray.getJSONObject(i);
                                listKeyword.add(object.getString("keyword"));
                            }
                            keywordAdapter = new KeywordsAdapter(mActivity, listKeyword);
                            gvPop.setAdapter(keywordAdapter);
                            gvPop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    preLl.setVisibility(View.GONE);
                                    keyword=listKeyword.get(position);
                                    etSearch.setText(keyword);
                                    recordsDao.addRecords(etSearch.getText().toString());
                                    request();
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


    @Override
    protected void setListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyUtils.isFastClick()) {
                    return;
                }
                finish();
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempList.clear();
                reversedList();
                recordsDao.deleteAllRecords();
                recordsAdapter.notifyDataSetChanged();
                historyLl.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (!MyUtils.isFastClick()) {
            return;
        }

        Intent intent = new Intent(mActivity, GoodsDetailActivity.class);
        intent.putExtra("productid", list.get(i).getId());
        startActivity(intent);
    }
}

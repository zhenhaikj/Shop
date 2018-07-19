package com.emjiayuan.app.fragment.Community;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.MyUtils;
import com.emjiayuan.app.activity.PostActivity;
import com.emjiayuan.app.activity.PostDetailActivity;
import com.emjiayuan.app.adapter.LabelAdapter;
import com.emjiayuan.app.adapter.NineGridTestAdapter;
import com.emjiayuan.app.adapter.PostsAdapter;
import com.emjiayuan.app.entity.Global;
import com.emjiayuan.app.entity.NineGridTestModel;
import com.emjiayuan.app.entity.Post;
import com.emjiayuan.app.event.UpdateEvent;
import com.emjiayuan.app.fragment.BaseLazyFragment;
import com.emjiayuan.app.widget.MyGridView;
import com.emjiayuan.app.widget.MyListView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;


public class CommunityFragment extends BaseLazyFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    @BindView(R.id.txt_content)
    RelativeLayout txtContent;
    @BindView(R.id.bg)
    ImageView bg;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.profile_image)
    CircleImageView profileImage;
    @BindView(R.id.gv_label)
    MyGridView gvLabel;
    @BindView(R.id.lv_post)
    MyListView lvPost;
    @BindView(R.id.fresh)
    ImageView fresh;
    @BindView(R.id.write)
    ImageView write;
    private LabelAdapter labelAdapter;
    private PostsAdapter mAdapter;
    private ArrayList<Post> list = new ArrayList<>();
    private List<NineGridTestModel> mList = new ArrayList<>();
    private String[] mUrls = new String[]{"http://d.hiphotos.baidu.com/image/h%3D200/sign=201258cbcd80653864eaa313a7dca115/ca1349540923dd54e54f7aedd609b3de9c824873.jpg",
            "http://d.hiphotos.baidu.com/image/h%3D200/sign=ea218b2c5566d01661199928a729d498/a08b87d6277f9e2fd4f215e91830e924b999f308.jpg",
            "http://img4.imgtn.bdimg.com/it/u=3445377427,2645691367&fm=21&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=2644422079,4250545639&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=1444023808,3753293381&fm=21&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=882039601,2636712663&fm=21&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=4119861953,350096499&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=2437456944,1135705439&fm=21&gp=0.jpg",
            "http://img4.duitang.com/uploads/item/201506/11/20150611000809_yFe5Z.jpeg",
            "http://img5.imgtn.bdimg.com/it/u=1717647885,4193212272&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=2024625579,507531332&fm=21&gp=0.jpg"};


    @Override
    protected int setLayoutId() {
        return R.layout.fragment_community;
    }

    @Override
    protected void initData() {
        NineGridTestModel model1 = new NineGridTestModel();
        model1.urlList.add(mUrls[0]);
        mList.add(model1);

        NineGridTestModel model2 = new NineGridTestModel();
        model2.urlList.add(mUrls[4]);
        mList.add(model2);
//
//        NineGridTestModel model3 = new NineGridTestModel();
//        model3.urlList.add(mUrls[2]);
//        mList.add(model3);

        NineGridTestModel model4 = new NineGridTestModel();
        for (int i = 0; i < mUrls.length; i++) {
            model4.urlList.add(mUrls[i]);
        }
        model4.isShowAll = false;
        mList.add(model4);

        NineGridTestModel model5 = new NineGridTestModel();
        for (int i = 0; i < mUrls.length; i++) {
            model5.urlList.add(mUrls[i]);
        }
        model5.isShowAll = true;//显示全部图片
        mList.add(model5);

        NineGridTestModel model6 = new NineGridTestModel();
        for (int i = 0; i < 9; i++) {
            model6.urlList.add(mUrls[i]);
        }
        mList.add(model6);

        NineGridTestModel model7 = new NineGridTestModel();
        for (int i = 3; i < 7; i++) {
            model7.urlList.add(mUrls[i]);
        }
        mList.add(model7);

        NineGridTestModel model8 = new NineGridTestModel();
        for (int i = 3; i < 6; i++) {
            model8.urlList.add(mUrls[i]);
        }
        mList.add(model8);
        String[] arrays = new String[]{"热门发布", "行业新闻", "公益", "求职招聘", "转让信息", "晒图", "热门发布", "热门发布"};
        labelAdapter = new LabelAdapter(getActivity(), Arrays.asList(arrays));
        gvLabel.setAdapter(labelAdapter);
        mAdapter = new PostsAdapter(mActivity,new ArrayList<Post>());
        lvPost.setAdapter(mAdapter);
    }

    @Override
    protected void setListener() {
        fresh.setOnClickListener(this);
        write.setOnClickListener(this);
        lvPost.setOnItemClickListener(this);
        gvLabel.setOnItemClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (!MyUtils.isFastClick()){
            return;
        }
        switch (view.getId()) {
            case R.id.fresh:
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.write:
                startActivity(new Intent(getActivity(), PostActivity.class));
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (!MyUtils.isFastClick()){
            return;
        }
        switch (adapterView.getId()) {
            case R.id.gv_label:

                break;
            case R.id.lv_post:
                startActivity(new Intent(getActivity(), PostDetailActivity.class));
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(UpdateEvent event) {

    }
}
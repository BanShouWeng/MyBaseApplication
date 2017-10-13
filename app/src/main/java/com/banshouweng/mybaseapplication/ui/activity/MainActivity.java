package com.banshouweng.mybaseapplication.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.banshouweng.mybaseapplication.R;
import com.banshouweng.mybaseapplication.base.BaseBean;
import com.banshouweng.mybaseapplication.base.activity.BaseFragmentActivity;
import com.banshouweng.mybaseapplication.bean.DouBanBean;
import com.banshouweng.mybaseapplication.ui.adapter.MyAdapter;

/**
 * 《一个Android工程的从零开始》
 *
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
 */
public class MainActivity extends BaseFragmentActivity {

    private ListView addressList;
    private TextView addressName;
    private RecyclerView recyclerView;
    private ImageView icon;
    private String[] a = {"张三", "李四", "王二", "麻子"};
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        List<BaseBean> beanList = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            BaseBean baseBean = new BaseBean();
//            baseBean.setName("name" + i);
//            baseBean.setMyClass("class" + i);
//            baseBean.setGrade("grade" + i);
//            beanList.add(baseBean);
//        }
//        Log.i("adada", new Gson().toJson(beanList));
//
//        //设置title文本
//        setTitle("MainActivity");
//        //设置返回拦截
//        setBaseBack(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                jumpTo(CropActivity.class);
//            }
//        });
//        //设置功能键，以及点击方法回调监听
//        setBaseRightIcon1(R.mipmap.more, "更多", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getImage("mobile/public/getValidateCode", false);
//            }
//        });
        setBaseRightIcon1(R.mipmap.add, "更多", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post("top250", DouBanBean.class, false);
                addressName.setText("加载了");
            }
        });


//        MineFragment mineFragment = MineFragment.getInstance();
//        replaceFragment(R.id.FrameLayout, mineFragment);

//        post("top250", BaseBean.class, false);
    }

    @Override
    public void success(String action, BaseBean baseBean) {
//        icon.setImageBitmap(((BitmapBean) baseBean).getBitmap());
        addressName.setText("获取到数据了");
        adapter.setData(((DouBanBean) baseBean).getSubjects());
    }

    @Override
    public void error(String action, Throwable e) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void findViews() {
//        addressList = getView(R.id.address_list);
        addressName = getView(R.id.address_name);
        recyclerView = getView(R.id.recyclerView);
//        icon = getView(R.id.icon);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void formatViews() {
        addressName.setText("才开始");
//        addressList.setAdapter(new ArrayAdapter(activity, android.R.layout.simple_list_item_1, a));
        adapter = new MyAdapter(activity, R.layout.douban_layout);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void formatData() {

    }

    @Override
    protected void getBundle() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == MotionEvent.BUTTON_BACK) {
            moveTaskToBack(false);
        }
        return super.onKeyDown(keyCode, event);
    }
}

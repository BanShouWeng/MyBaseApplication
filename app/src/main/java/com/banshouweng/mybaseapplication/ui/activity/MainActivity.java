package com.banshouweng.mybaseapplication.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.banshouweng.mybaseapplication.R;
import com.banshouweng.mybaseapplication.base.BaseBean;
import com.banshouweng.mybaseapplication.base.BaseFragmentActivity;
import com.banshouweng.mybaseapplication.ui.fragment.MineFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

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
    private String[] a = {"张三", "李四", "王二", "麻子"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<BaseBean> beanList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            BaseBean baseBean = new BaseBean();
            baseBean.setName("name" + i);
            baseBean.setMyClass("class" + i);
            baseBean.setGrade("grade" + i);
            beanList.add(baseBean);
        }
        Log.i("adada", new Gson().toJson(beanList));

        //设置title文本
        setTitle("MainActivity");
        //设置返回拦截
        setBaseBack(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpTo(CropActivity.class);
            }
        });
        //设置功能键，以及点击方法回调监听
        setBaseRightIcon1(R.mipmap.more, "更多", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get("top250", BaseBean.class, false);
            }
        });
        setBaseRightIcon2(R.mipmap.add, "更多", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post("top250", BaseBean.class, false);
            }
        });


        MineFragment mineFragment = MineFragment.getInstance();
        replaceFragment(R.id.FrameLayout, mineFragment);
//        post("top250", BaseBean.class, false);
    }

    @Override
    public void success(String action, BaseBean baseBean) {
        String b = action;
        String a = baseBean.getGrade();
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
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void formatViews() {
        addressName.setText("dawdadw");
//        addressList.setAdapter(new ArrayAdapter(activity, android.R.layout.simple_list_item_1, a));
    }

    @Override
    protected void formatData() {

    }

    @Override
    protected void getBundle() {

    }
}

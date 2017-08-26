package com.banshouweng.mybaseapplication.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.banshouweng.mybaseapplication.R;
import com.banshouweng.mybaseapplication.base.BaseBean;
import com.banshouweng.mybaseapplication.base.BaseNetActivity;
import com.banshouweng.mybaseapplication.bean.PostStudentBean;
import com.banshouweng.mybaseapplication.reciever.ResultCallBack;
import com.banshouweng.mybaseapplication.ui.fragment.MineFragment;
import com.banshouweng.mybaseapplication.utils.LogUtil;
import com.banshouweng.mybaseapplication.zxing.activity.CaptureActivity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 《一个Android工程的从零开始》
 *
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
 */
public class MainActivity extends BaseNetActivity {

    @BindView(R.id.address_list)
    RecyclerView addressList;
    private MineFragment mineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentView(R.layout.activity_main);

        ButterKnife.bind(this);

//        PostStudentBean studentBean = new PostStudentBean();
        List<BaseBean> beanList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            BaseBean baseBean = new BaseBean();
            baseBean.setName("name" + i);
            baseBean.setMyClass("class" + i);
            baseBean.setGrade("grade" + i);
            beanList.add(baseBean);
        }
        Log.i("adada", new Gson().toJson(beanList).toString());
//        studentBean.setBaseBeen(beanList);

        //设置title文本
        setTitle("MainActivity");
        //设置返回拦截
        setBaseBack(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(CaptureActivity.class);
            }
        });
        mineFragment = new MineFragment();
        //设置功能键，以及点击方法回调监听
        setBaseRightIcon1(R.mipmap.more, "更多", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get(null, BaseBean.class, false, new ResultCallBack() {
                    @Override
                    public void success(String action, BaseBean baseBean) {

                    }

                    @Override
                    public void error(String action, Throwable e) {

                    }
                });
            }
        });
        setBaseRightIcon2(R.mipmap.add, "更多", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post(null, BaseBean.class, false, new ResultCallBack() {
                    @Override
                    public void success(String action, BaseBean baseBean) {

                    }

                    @Override
                    public void error(String action, Throwable e) {

                    }
                });
            }
        });

        post("top250", BaseBean.class, false, new ResultCallBack() {
            @Override
            public void success(String action, BaseBean baseBean) {

            }

            @Override
            public void error(String action, Throwable e) {

            }
        });
    }

    @Override
    public void success(String action, BaseBean baseBean) {
        super.success(action, baseBean);

    }
}

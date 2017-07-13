package com.banshouweng.mybaseapplication.ui.activity;

import android.os.Bundle;

import com.banshouweng.mybaseapplication.R;
import com.banshouweng.mybaseapplication.base.BaseActivity;
import com.banshouweng.mybaseapplication.base.NetWorkActivity;

/**
 * 《一个Android工程的从零开始》
 *
 * @author 半寿翁
 * 博客：
 * CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * 简书  http://www.jianshu.com/p/1410051701fe
 */
public class TestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentView(R.layout.activity_test);
        setBaseRightIcon1(R.mipmap.more, "more", new OnClickRightIcon1CallBack() {
            @Override
            public void clickRightIcon1() {
                goTo(NetWorkActivity.class);
            }
        });
    }
}

package com.banshouweng.mybaseapplication.ui.activity;

import android.os.Bundle;

import com.banshouweng.mybaseapplication.R;
import com.banshouweng.mybaseapplication.base.BaseActivity;

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
        setTitle("TestActivity");
        setBaseRightIcon1(R.mipmap.more, "more", null);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void formatViews() {

    }

    @Override
    protected void formatData() {

    }

    @Override
    protected void getBundle() {

    }
}

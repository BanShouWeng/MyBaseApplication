package com.mybaseapplication.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mybaseapplication.R;
import com.mybaseapplication.base.BaseActivity;
import com.mybaseapplication.base.NetWorkActivity;

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

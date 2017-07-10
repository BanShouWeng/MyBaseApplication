package com.mybaseapplication.base;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mybaseapplication.R;
import com.mybaseapplication.ui.activity.TestActivity;

public class NetWorkActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentView(R.layout.activity_net_work);

        setBaseRightIcon1(R.mipmap.add, "add", new OnClickRightIcon1CallBack() {
            @Override
            public void clickRightIcon1() {
                startActivity(TestActivity.class);
            }
        });
    }
}

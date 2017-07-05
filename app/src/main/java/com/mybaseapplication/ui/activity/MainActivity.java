package com.mybaseapplication.ui.activity;

import android.os.Bundle;

import com.mybaseapplication.R;
import com.mybaseapplication.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentView(R.layout.activity_main);
    }
}

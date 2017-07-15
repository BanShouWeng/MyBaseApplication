package com.banshouweng.mybaseapplication.base;

import android.os.Bundle;

import com.banshouweng.mybaseapplication.R;
import com.banshouweng.mybaseapplication.ui.activity.TestActivity;

/**
 * 《一个Android工程的从零开始》
 *
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
 */
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

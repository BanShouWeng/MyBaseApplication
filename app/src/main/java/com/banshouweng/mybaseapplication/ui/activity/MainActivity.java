package com.banshouweng.mybaseapplication.ui.activity;

import android.net.Uri;
import android.os.Bundle;

import com.banshouweng.mybaseapplication.R;
import com.banshouweng.mybaseapplication.base.BaseActivity;
import com.banshouweng.mybaseapplication.base.BaseFragment;
import com.banshouweng.mybaseapplication.base.NetWorkActivity;
import com.banshouweng.mybaseapplication.ui.fragment.MineFragment;

/**
 * 《一个Android工程的从零开始》
 *
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
 */
public class MainActivity extends BaseActivity {

    private MineFragment mineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentView(R.layout.activity_main);

        //设置title文本
        setTitle("MainActivity");
        //设置返回拦截
        setBaseBack(new OnClickBackCallBack() {
            @Override
            public void clickBack() {
                startActivity(NetWorkActivity.class);
            }
        });
        mineFragment = new MineFragment();
        //设置功能键，以及点击方法回调监听
        setBaseRightIcon1(R.mipmap.more, "更多", new OnClickRightIcon1CallBack() {
            @Override
            public void clickRightIcon1() {
                showFragment(mineFragment);
            }
        });
        setBaseRightIcon2(R.mipmap.add, "更多", new OnClickRightIcon2CallBack() {
            @Override
            public void clickRightIcon2() {
                hideFragment(mineFragment);
            }
        });

        addFragment(R.id.fragment_layout, mineFragment);
//        hideTitle();
    }
}

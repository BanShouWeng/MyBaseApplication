package com.banshouweng.mybaseapplication.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.banshouweng.mybaseapplication.R;
import com.banshouweng.mybaseapplication.base.BaseFragment;
import com.banshouweng.mybaseapplication.ui.activity.TestActivity;

/**
 * 《一个Android工程的从零开始》
 *
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
 */
public class MineFragment extends BaseFragment {
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setBaseContentView(R.layout.fragment_mine);
        //设置title文本
        setTitle("MineFragment");
        //设置返回拦截
        setBaseBack(new OnClickBackCallBack() {
            @Override
            public void clickBack() {
                startActivity(TestActivity.class);
            }
        });
        //设置功能键，以及点击方法回调监听
        setBaseRightIcon1(R.mipmap.add, "更多", new OnClickRightIcon1CallBack() {
            @Override
            public void clickRightIcon1() {
                showLoadDialog();
            }
        });
        setBaseRightIcon2(R.mipmap.more, "更多", new OnClickRightIcon2CallBack() {
            @Override
            public void clickRightIcon2() {
                hideLoadDialog();
            }
        });
//        hideTitle();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            showToast("隐藏了");
        } else {
            showToast("显示了");
        }
    }
}

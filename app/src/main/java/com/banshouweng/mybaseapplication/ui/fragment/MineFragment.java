package com.banshouweng.mybaseapplication.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.banshouweng.mybaseapplication.R;
import com.banshouweng.mybaseapplication.base.BaseBean;
import com.banshouweng.mybaseapplication.base.fragment.BaseNetFragment;
import com.banshouweng.mybaseapplication.ui.activity.TestActivity;
import com.banshouweng.mybaseapplication.utils.LogUtil;

/**
 * 《一个Android工程的从零开始》
 *
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
 */
public class MineFragment extends BaseNetFragment {

    private TextView textFragment;

    public static MineFragment getInstance() {
        return new MineFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //设置title文本
        setTitle("MineFragment");
        //设置返回拦截
        setBaseBack(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpTo(TestActivity.class);
            }
        });
        //设置功能键，以及点击方法回调监听
        setBaseRightIcon1(R.mipmap.add, "更多", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get("top250", BaseBean.class, false);
            }
        });
        setBaseRightIcon2(R.mipmap.more, "更多", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post("top250", BaseBean.class, false);
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

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void findViews() {
        textFragment = getView(R.id.text_fragment);
    }

    @Override
    protected void formatViews() {
        textFragment.setText("找到了，而且赋值成功");
    }

    @Override
    protected void formatData() {

    }

    @Override
    protected void getBundle() {

    }

    @Override
    public void success(String action, BaseBean baseBean) {
        LogUtil.e("dadadadad", action);
    }

    @Override
    public void error(String action, Throwable e) {

    }
}

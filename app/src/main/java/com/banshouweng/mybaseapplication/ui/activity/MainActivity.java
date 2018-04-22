package com.banshouweng.mybaseapplication.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.banshouweng.mybaseapplication.R;
import com.banshouweng.mybaseapplication.base.BaseBean;
import com.banshouweng.mybaseapplication.base.activity.BaseActivity;
import com.banshouweng.mybaseapplication.base.activity.BaseLayoutActivity;
import com.banshouweng.mybaseapplication.base.activity.BaseNetActivity;
import com.banshouweng.mybaseapplication.utils.Logger;
import com.banshouweng.mybaseapplication.utils.TxtUtils;

import java.util.Locale;

/**
 * 《一个Android工程的从零开始》
 *
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
 */
public class MainActivity extends BaseNetActivity {

    private Button view, mergeBtn2;
    private int count = 0;

    private EditText clickEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title);
    }

    @Override
    protected void success(String action, BaseBean baseBean) {

    }

    @Override
    protected void error(String action, Throwable e) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @SuppressLint("InflateParams")
    @Override
    protected void findViews() {
        view = getView(R.id.merge_btn);
        mergeBtn2 = getView(R.id.merge_btn2);
        clickEt = getView(R.id.click_et);
    }

    @Override
    protected void formatViews() {
        setOnClickListener(R.id.merge_btn, R.id.merge_btn2, R.id.click_et);
    }

    @Override
    protected void formatData() {
        mergeBtn2.setText(String.format(Locale.CHINA, TxtUtils.getText(context, R.string.all), "55555"));
        view.setText(String.format(Locale.CHINA, TxtUtils.getText(context, R.string.all), "12345"));
    }

    @Override
    protected void getBundle(Bundle bundle) {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.merge_btn:
                view.setText(getName(BaseBean.class));
                Logger.i("111", count + "");
                break;

            case R.id.merge_btn2:
//                backTo(TestActivity.class);
//                jumpTo(TestActivity.class);
                finishActivity();
                break;
        }
    }
}

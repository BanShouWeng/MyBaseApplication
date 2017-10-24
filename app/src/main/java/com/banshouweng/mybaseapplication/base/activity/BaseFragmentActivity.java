package com.banshouweng.mybaseapplication.base.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

/**
 * 《一个Android工程的从零开始》
 * 配置Fragment的Activity
 *
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
 */
public abstract class BaseFragmentActivity extends BaseNetActivity {

    @SuppressLint("CommitTransaction")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 添加Fragment
     *
     * @param containerViewId 对应布局的id
     * @param fragments       所要添加的Fragment，可以添加多个
     */
    public void addFragment(int containerViewId, Fragment... fragments) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragments != null) {
            for (int i = 0; i < fragments.length; i++) {
                transaction.add(containerViewId, fragments[i]);
                if (i != fragments.length - 1) {
                    transaction.hide(fragments[i]);
                }
            }
            transaction.commitAllowingStateLoss();
        }
    }

    /**
     * 显示Fragment
     *
     * @param fragment 所要显示的Fragment
     */
    public void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragment != null) {
            transaction.show(fragment).commitAllowingStateLoss();
        }
    }

    /**
     * 隐藏Fragment
     *
     * @param fragments 所要隐藏的Fragment，可以添加多个
     */
    public void hideFragment(Fragment... fragments) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                transaction.hide(fragment);
            }
            transaction.commitAllowingStateLoss();
        }
    }

    /**
     * 替换Fragment
     *
     * @param containerViewId 对应布局的id
     * @param fragment        用于替代原有Fragment的心Fragment
     */
    public void replaceFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragment != null) {
            transaction.replace(containerViewId, fragment).commitAllowingStateLoss();
        }
    }

    /**
     * 移除Fragment
     * @param fragments 所要移除的Fragment，可以添加多个
     */
    public void removeFragment(Fragment... fragments){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                transaction.hide(fragment);
            }
            transaction.commitAllowingStateLoss();
        }
    }
}

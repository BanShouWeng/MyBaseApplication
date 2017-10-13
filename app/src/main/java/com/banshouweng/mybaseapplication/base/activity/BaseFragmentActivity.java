package com.banshouweng.mybaseapplication.base.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

public abstract class BaseFragmentActivity extends BaseNetActivity {

    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transaction = getSupportFragmentManager().beginTransaction();
    }

    /**
     * 添加Fragment
     *
     * @param containerViewId 对应布局的id
     * @param fragments       所要添加的Fragment，可以添加多个
     */
    public void addFragment(int containerViewId, Fragment... fragments) {
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
        if (fragment != null) {
            transaction.show(fragment).commitAllowingStateLoss();
        }
    }

    /**
     * 隐藏Fragment
     *
     * @param fragment 所要隐藏的Fragment，可以添加多个
     */
    public void hideFragment(Fragment fragment) {
        if (fragment != null) {
            transaction.hide(fragment).commitAllowingStateLoss();
        }
    }

    /**
     * 替换Fragment
     *
     * @param containerViewId 对应布局的id
     * @param fragment        用于替代原有Fragment的心Fragment
     */
    public void replaceFragment(int containerViewId, Fragment fragment) {
        if (fragment != null) {
            transaction.replace(containerViewId, fragment).commitAllowingStateLoss();
        }
    }

}

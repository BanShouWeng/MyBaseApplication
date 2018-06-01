package com.banshouweng.mybaseapplication.base.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.banshouweng.mybaseapplication.utils.Logger;

/**
 * 《一个Android工程的从零开始》
 * 配置Fragment的Activity
 * Activity 可 能 因 为 各 种 原 因 被 销 毁 ， Android 支 持 页 面 被 销 毁 前 通 过
 * Activity#onSaveInstanceState() 保 存 自 己 的 状 态 。 但 如 果
 * FragmentTransaction.commit()发生在 Activity 状态保存之后，就会导致 Activity 重
 * 建、恢复状态时无法还原页面状态，从而可能出错。为了避免给用户造成不好的体
 * 验，系统会抛出 IllegalStateExceptionStateLoss 异常。推荐的做法是在 Activity 的
 * onPostResume() 或 onResumeFragments() （ 对 FragmentActivity ） 里 执 行
 * FragmentTransaction.commit()，如有必要也可在 onCreate()里执行。不要随意改用
 * FragmentTransaction.commitAllowingStateLoss()或者直接使用 try-catch 避免
 * crash，这不是问题的根本解决之道，当且仅当你确认 Activity 重建、恢复状态时，
 * 本次 commit 丢失不会造成影响时才可这么做。——《阿里巴巴Android开发手册》
 *
 *
 * FragmentTransaction需要使用局部变量，不然会报commit already called错误
 *
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
            transaction.commit();
        } else {
            Logger.e(getName(), "没有Fragment");
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
            transaction.show(fragment).commit();
        } else {
            Logger.e(getName(), "ragment不存在");
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
            transaction.commit();
        } else {
            Logger.e(getName(), "没有Fragment");
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
            transaction.replace(containerViewId, fragment).commit();
        } else {
            Logger.e(getName(), "ragment不存在");
        }
    }

    /**
     * 移除Fragment
     *
     * @param fragments 所要移除的Fragment，可以添加多个
     */
    public void removeFragment(Fragment... fragments) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                transaction.hide(fragment);
            }
            transaction.commit();
        } else {
            Logger.e(getName(), "没有Fragment");
        }
    }
}

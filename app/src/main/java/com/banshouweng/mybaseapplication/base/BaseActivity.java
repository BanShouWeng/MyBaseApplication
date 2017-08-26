package com.banshouweng.mybaseapplication.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.banshouweng.mybaseapplication.R;
import com.banshouweng.mybaseapplication.event.NetBroadcastReceiver;
import com.banshouweng.mybaseapplication.ui.activity.MainActivity;
import com.banshouweng.mybaseapplication.widget.CustomProgressDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;


/**
 * 《一个Android工程的从零开始》
 *
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
 */
public class BaseActivity extends AppCompatActivity implements NetBroadcastReceiver.NetEvevt {

    /**
     * 网络状态监听接受者
     */
    public static NetBroadcastReceiver.NetEvevt evevt;
    /**
     * 用于传递的上下文信息
     */
    public Context context;
    public Activity activity;

    private ImageView baseBack;
    private TextView baseTitle;
    private RelativeLayout baseTitleLayout;

    /**
     * 当前打开Activity存储List
     */
    private static List<Activity> activities = new ArrayList<>();

    /**
     * 加载提示框
     */
    private CustomProgressDialog customProgressDialog;

    public Map<String, String> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        if (!(this instanceof MainActivity)) {
            activities.add(this);
        }
        context = getApplicationContext();
        activity = this;
        evevt = this;
        customProgressDialog = new CustomProgressDialog(activity, R.style.progress_dialog_loading, "玩命加载中。。。");
        initBaseView();
    }

    /**
     * 控件初始化
     */
    public void initBaseView() {
        baseBack = (ImageView) findViewById(R.id.base_back);

        baseTitleLayout = (RelativeLayout) findViewById(R.id.base_title_layout);

        setBaseBack(null);
    }

    /**
     * 隐藏头布局
     */
    public void hideTitle() {
        baseTitleLayout.setVisibility(View.GONE);
    }

    /**
     * 隐藏返回键
     */
    private void hideBack() {
        baseBack.setVisibility(View.GONE);
    }

    /**
     * 设置标题
     *
     * @param title 标题的文本
     */
    public void setTitle(String title) {
        if (baseTitle == null) {
            baseTitle = (TextView) findViewById(R.id.base_title);
        }
        baseTitle.setText(title);
    }


    public void setBaseBack(View.OnClickListener clickListener) {
        if (clickListener == null) {
            baseBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {
            baseBack.setOnClickListener(clickListener);
        }
    }

    /**
     * 最右侧图片功能键设置方法
     *
     * @param resId         图片id
     * @param alertText     语音辅助提示读取信息
     * @param clickListener 点击事件
     * @return 将当前ImageView返回方便进一步处理
     */
    public ImageView setBaseRightIcon1(int resId, String alertText, View.OnClickListener clickListener) {
        ImageView baseRightIcon1 = (ImageView) findViewById(R.id.base_right_icon1);
        baseRightIcon1.setImageResource(resId);
        baseRightIcon1.setVisibility(View.VISIBLE);
        //语音辅助提示的时候读取的信息
        baseRightIcon1.setContentDescription(alertText);
        baseRightIcon1.setOnClickListener(clickListener);
        return baseRightIcon1;
    }

    /**
     * 右数第二个图片功能键设置方法
     *
     * @param resId         图片id
     * @param alertText     语音辅助提示读取信息
     * @param clickListener 点击事件
     * @return 将当前ImageView返回方便进一步处理
     */
    public ImageView setBaseRightIcon2(int resId, String alertText, View.OnClickListener clickListener) {
        ImageView baseRightIcon2 = (ImageView) findViewById(R.id.base_right_icon2);
        baseRightIcon2.setImageResource(resId);
        baseRightIcon2.setVisibility(View.VISIBLE);
        //语音辅助提示的时候读取的信息
        baseRightIcon2.setContentDescription(alertText);
        baseRightIcon2.setOnClickListener(clickListener);
        return baseRightIcon2;
    }

    /**
     * 最右侧文本功能键设置方法
     *
     * @param text          文本信息
     * @param clickListener 点击事件
     * @return 将当前TextView返回方便进一步处理
     */
    public TextView setBaseRightText(String text, View.OnClickListener clickListener) {
        TextView baseRightText = (TextView) findViewById(R.id.base_right_text);
        baseRightText.setText(text);
        baseRightText.setVisibility(View.VISIBLE);
        baseRightText.setOnClickListener(clickListener);
        return baseRightText;
    }

    /**
     * 最右侧文本功能键设置方法
     *
     * @param textId        文本信息id
     * @param clickListener 点击事件
     * @return 将当前TextView返回方便进一步处理
     */
    public TextView setBaseRightText(int textId, View.OnClickListener clickListener) {
        TextView baseRightText = (TextView) findViewById(R.id.base_right_text);
        baseRightText.setText(textId);
        baseRightText.setVisibility(View.VISIBLE);
        baseRightText.setOnClickListener(clickListener);
        return baseRightText;
    }

    /**
     * 引用头部布局
     *
     * @param layoutId 布局id
     */
    public void setBaseContentView(int layoutId) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.base_main_layout);

        //获取布局，并在BaseActivity基础上显示
        final View view = getLayoutInflater().inflate(layoutId, null);
        //关闭键盘
        hideKeyBoard();
        //给EditText的父控件设置焦点，防止键盘自动弹出
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layout.addView(view, params);
        layout.setVisibility(View.VISIBLE);
    }

    /**
     * 引用头部布局且当前页面基于ScrollView
     *
     * @param layoutId 布局id
     */
    public void setBaseScrollContentView(int layoutId) {
        ScrollView layout = (ScrollView) findViewById(R.id.base_scroll_view);

        //当子布局高度值不足ScrollView时，用这个方法可以充满ScrollView，防止布局无法显示
        layout.setFillViewport(true);

        //获取布局，并在BaseActivity基础上显示
        final View view = getLayoutInflater().inflate(layoutId, null);
        //关闭键盘
        hideKeyBoard();
        //给EditText的父控件设置焦点，防止键盘自动弹出
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layout.addView(view, params);
        layout.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏键盘
     */
    public void hideKeyBoard() {
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 跳转页面
     *
     * @param clz 所跳转的目的Activity类
     */
    public void startActivity(Class<?> clz) {
        startActivity(new Intent(this, clz));
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(this, clz);
        if (bundle != null) {
            intent.putExtra("bundle", bundle);
        }
        startActivity(intent);
    }

    /**
     * 跳转页面
     *
     * @param clz         所跳转的Activity类
     * @param requestCode 请求码
     */
    public void startActivityForResult(Class<?> clz, int requestCode) {
        startActivityForResult(new Intent(this, clz), requestCode);
    }

    /**
     * 跳转页面
     *
     * @param clz         所跳转的Activity类
     * @param bundle      跳转所携带的信息
     * @param requestCode 请求码
     */
    public void startActivityForResult(Class<?> clz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, clz);
        if (bundle != null) {
            intent.putExtra("bundle", bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 消息提示框
     *
     * @param message 提示消息文本
     */
    public void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 消息提示框
     *
     * @param messageId 提示消息文本ID
     */
    public void showToast(int messageId) {
        Toast.makeText(context, messageId, Toast.LENGTH_SHORT).show();
    }

    /**
     * 关闭所有Activity（除MainActivity以外）
     */
    public void finishActivity() {
        for (Activity activity : activities) {
            activity.finish();
        }
    }

    /**
     * 跳转到指定的Activity
     *
     * @param clz 指定的Activity对应的class
     */
    public void goTo(Class<?> clz) {
        if (clz.equals(MainActivity.class)) {
            finishActivity();
        } else {
            for (int i = activities.size() - 1; i >= 0; i--) {
                if (clz.equals(activities.get(i).getClass())) {
                    break;
                } else {
                    activities.get(i).finish();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activities.remove(this);
        evevt = null;
    }

    /**
     * 网络变化回调方法
     *
     * @param mobileNetState 当前的网络状态
     */
    @Override
    public void onNetChanged(int mobileNetState) {

    }

    /**
     * 显示加载提示框
     */
    public void showLoadDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                customProgressDialog.show();
            }
        });
    }

    /**
     * 隐藏加载提示框
     */
    public void hideLoadDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (customProgressDialog != null && customProgressDialog.isShowing()) {
                    customProgressDialog.dismiss();
                }
            }
        });
    }

    /**
     * 添加Fragment
     *
     * @param containerViewId 对应布局的id
     * @param fragments       所要添加的Fragment，可以添加多个
     */
    public void addFragment(int containerViewId, Fragment... fragments) {
        if (fragments != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
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
            getSupportFragmentManager().beginTransaction().show(fragment).commitAllowingStateLoss();
        }
    }

    /**
     * 隐藏Fragment
     *
     * @param fragment 所要隐藏的Fragment，可以添加多个
     */
    public void hideFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().hide(fragment).commitAllowingStateLoss();
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
            getSupportFragmentManager().beginTransaction().replace(containerViewId, fragment).commitAllowingStateLoss();
        }
    }

//    private <T extends View> T findViewById(int viewId) {
//        View view = mViews.get(viewId);
//        if (view == null) {
//            view = itemView.findViewById(viewId);
//            mViews.put(viewId, view);
//        }
//        return (T) view;
//    }
}

package com.banshouweng.mybaseapplication.base.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.banshouweng.mybaseapplication.R;
import com.banshouweng.mybaseapplication.receiver.NetBroadcastReceiver;
import com.banshouweng.mybaseapplication.utils.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 《一个Android工程的从零开始》
 *
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
 */
public abstract class BaseActivity extends AppCompatActivity implements NetBroadcastReceiver.NetEvent, View.OnClickListener {
    /**
     * BACK_ID          : title返回按钮
     * CLOSE_ID         : title关闭按钮
     * RIGHT_TEXT_ID    : title右侧文本功能键按钮
     * RIGHT_ICON_ID1   : title右侧图片功能键（右）
     * RIGHT_ICON_ID2   : title右侧图片功能键（左）
     */
    public final int BACK_ID = R.id.base_back;
    public final int CLOSE_ID = R.id.base_close;
    public final int RIGHT_TEXT_ID = R.id.base_right_text;
    public final int RIGHT_ICON_ID1 = R.id.base_right_icon1;
    public final int RIGHT_ICON_ID2 = R.id.base_right_icon2;

    /**
     * 网络状态监听接受者
     */
    public static NetBroadcastReceiver.NetEvent event;
    /**
     * 用于传递的上下文信息
     */
    public Context context;
    public Activity activity;

    /**
     * 返回按钮
     */
    private ImageView baseBack;
    /**
     * 关闭按钮
     */
    private ImageView baseClose;
    /**
     * Title ViewStub
     */
    private ViewStub titleStub;

    /**
     * 当前打开Activity存储List
     */
    private static List<Activity> activities = new ArrayList<>();
    /**
     * 调用backTo方法时，验证该Activity是否已经存在
     */
    private static Map<Class<?>, Activity> activitiesMap = new HashMap<>();
    private Toast toast;
    private ImageView baseRightIcon1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activities.add(this);
        activitiesMap.put(getClass(), this);
        context = getApplicationContext();
        activity = this;
        event = this;

        setContentView(R.layout.activity_base);
        initSDK();
        setBaseContentView(getLayoutId());
        findViews();
        getBundle(getIntent().getBundleExtra("bundle"));
        formatViews();
        formatData();
    }

    /**
     * 隐藏头布局
     */
    public void hideTitle() {
        if (titleStub != null) {
            titleStub.setVisibility(View.GONE);
        }
    }

    /**
     * 隐藏返回键
     */
    public void hideBack() {
        if (null != baseBack) {
            baseBack.setVisibility(View.GONE);
        } else {
            Logger.e(getName(), "baseBack is not exist!");
        }
    }

    /**
     * 显示返回键
     */
    public void showBack() {
        if (null != baseBack) {
            baseBack.setVisibility(View.VISIBLE);
        } else {
            Logger.e(getName(), "baseBack is not exist!");
        }
    }

    /**
     * 设置标题
     *
     * @param title 标题的文本
     */
    public void setTitle(String title) {
        setTitle(title, true);
    }

    /**
     * 设置标题
     *
     * @param titleId 标题的文本
     */
    public void setTitle(int titleId) {
        setTitle(titleId, true);
    }

    /**
     * 控件初始化
     */
    protected void initBaseView() {
        if (titleStub == null) {
            titleStub = getView(R.id.base_title_layout);
            titleStub.inflate();
        }
    }

    /**
     * 设置标题
     *
     * @param title    标题的文本
     * @param showBack 是否显示返回键
     */
    public void setTitle(String title, boolean showBack) {
        initBaseView();
        ((TextView) getView(R.id.base_title)).setText(title);
        if (showBack) {
            baseBack = getView(R.id.base_back);
            baseBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    /**
     * 设置标题
     *
     * @param titleId  标题的文本
     * @param showBack 是否显示返回键（一般只用于首页）
     */
    public void setTitle(int titleId, boolean showBack) {
        initBaseView();
        ((TextView) getView(R.id.base_title)).setText(titleId);
        if (showBack) {
            baseBack = getView(R.id.base_back);
            baseBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    /**
     * 重置返回点击事件
     */
    public void resetBaseBack() {
        if (null != baseBack) {
            baseBack.setOnClickListener(this);
        } else {
            Logger.e(getName(), "baseBack is not exist!");
        }
    }

    /**
     * 重置返回点击事件
     *
     * @param resId 重置返回按钮图标
     */
    public void setBaseBack(int resId) {
        initBaseView();
        baseBack = getView(R.id.base_back);
        baseBack.setImageResource(resId);
        baseBack.setOnClickListener(this);
    }

    /**
     * 设置关闭点击事件
     */
    public void setBaseClose() {
        setBaseClose(false);
    }

    /**
     * 设置关闭点击事件
     *
     * @param isResetClose 是否重置关闭按钮点击事件
     */
    public void setBaseClose(boolean isResetClose) {
        initBaseView();
        baseClose = getView(R.id.base_close);
        if (isResetClose) {
            baseClose.setOnClickListener(this);
        } else {
            baseClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }

    /**
     * 设置关闭点击事件
     *
     * @param resId 重置关闭按钮图标
     */
    public void setBaseClose(int resId) {
        initBaseView();
        baseClose = getView(R.id.base_close);
        baseClose.setImageResource(resId);
        baseClose.setOnClickListener(this);
    }

    /**
     * 设置关闭部分页面
     *
     * @param targetActivity 关闭后所要返回的Activity
     */
    public void setBaseClose(final Class<?> targetActivity) {
        initBaseView();
        baseClose = getView(R.id.base_close);
        baseClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backTo(targetActivity);
            }
        });
    }

    /**
     * 隐藏关闭按钮
     */
    public void hideBaseClose() {
        if (null != baseClose) {
            baseClose.setVisibility(View.GONE);
        } else {
            Logger.e(getName(), "baseClose is not exist");
        }
    }

    /**
     * 显示关闭按钮
     */
    public void showBaseClose() {
        if (null != baseClose) {
            baseClose.setVisibility(View.VISIBLE);
        } else {
            Logger.e(getName(), "baseClose is not exist");
        }
    }

    /**
     * 最右侧图片功能键设置方法
     *
     * @param resId     图片id
     * @param alertText 语音辅助提示读取信息
     * @return 将当前ImageView返回方便进一步处理
     */
    public ImageView setBaseRightIcon1(int resId, String alertText) {
        initBaseView();

        baseRightIcon1 = getView(R.id.base_right_icon1);
        baseRightIcon1.setImageResource(resId);
        baseRightIcon1.setVisibility(View.VISIBLE);
        //语音辅助提示的时候读取的信息
        baseRightIcon1.setContentDescription(alertText);
        baseRightIcon1.setOnClickListener(this);
        return baseRightIcon1;
    }

    /**
     * 右数第二个图片功能键设置方法
     *
     * @param resId     图片id
     * @param alertText 语音辅助提示读取信息
     * @return 将当前ImageView返回方便进一步处理
     */
    public ImageView setBaseRightIcon2(int resId, String alertText) {
        ImageView baseRightIcon2 = getView(R.id.base_right_icon2);
        if (null != baseRightIcon1) {
            baseRightIcon2.setImageResource(resId);
            baseRightIcon2.setVisibility(View.VISIBLE);
            //语音辅助提示的时候读取的信息
            baseRightIcon2.setContentDescription(alertText);
            baseRightIcon2.setOnClickListener(this);
        } else {
            Logger.e(getName(),"You must inflate the baseRightIcon1 before use baseRightIcon2!");
        }
        return baseRightIcon2;
    }

    /**
     * 最右侧文本功能键设置方法
     *
     * @param text 文本信息
     * @return 将当前TextView返回方便进一步处理
     */
    public TextView setBaseRightText(String text) {
        initBaseView();

        TextView baseRightText = getView(R.id.base_right_text);
        baseRightText.setText(text);
        baseRightText.setVisibility(View.VISIBLE);
        baseRightText.setOnClickListener(this);
        return baseRightText;
    }

    /**
     * 最右侧文本功能键设置方法
     *
     * @param textId 文本信息id
     * @return 将当前TextView返回方便进一步处理
     */
    public TextView setBaseRightText(int textId) {
        initBaseView();

        TextView baseRightText = getView(R.id.base_right_text);
        baseRightText.setText(textId);
        baseRightText.setVisibility(View.VISIBLE);
        baseRightText.setOnClickListener(this);
        return baseRightText;
    }

    /**
     * 引用头部布局
     *
     * @param layoutId 布局id
     */
    private void setBaseContentView(int layoutId) {
        FrameLayout layout = getView(R.id.base_main_layout);

        //获取布局，并在BaseActivity基础上显示
        final View view = getLayoutInflater().inflate(layoutId, null);
        //关闭键盘
        hideKeyBoard();
        //给EditText的父控件设置焦点，防止键盘自动弹出
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layout.addView(view, params);
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
     * @param targetActivity 所跳转的目的Activity类
     */
    public void jumpTo(Class<?> targetActivity) {
        Intent intent = new Intent(this, targetActivity);
        if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Logger.e(getName(), "activity not found for " + targetActivity.getSimpleName());
            }
        }
    }

    /**
     * 跳转页面
     *
     * @param targetActivity 所跳转的目的Activity类
     * @param bundle         跳转所携带的信息
     */
    public void jumpTo(Class<?> targetActivity, Bundle bundle) {
        Intent intent = new Intent(this, targetActivity);
        if (bundle != null) {
            intent.putExtra("bundle", bundle);
        }
        if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Logger.e(getName(), "activity not found for " + targetActivity.getSimpleName());
            }
        }
    }

    /**
     * 跳转页面
     *
     * @param targetActivity 所跳转的Activity类
     * @param requestCode    请求码
     */
    public void jumpTo(Class<?> targetActivity, int requestCode) {
        Intent intent = new Intent(this, targetActivity);
        if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
            try {
                startActivityForResult(intent, requestCode);
            } catch (ActivityNotFoundException e) {
                Logger.e(getName(), "activity not found for " + targetActivity.getSimpleName());
            }
        }
    }

    /**
     * 跳转页面
     *
     * @param targetActivity 所跳转的Activity类
     * @param bundle         跳转所携带的信息
     * @param requestCode    请求码
     */
    public void jumpTo(Class<?> targetActivity, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, targetActivity);
        if (bundle != null) {
            intent.putExtra("bundle", bundle);
        }
        if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
            try {
                startActivityForResult(intent, requestCode);
            } catch (ActivityNotFoundException e) {
                Logger.e(getName(), "activity not found for " + targetActivity.getSimpleName());
            }
        }
    }

    /**
     * 获取当前Activity类名
     *
     * @return 类名字符串
     */
    protected String getName() {
        return getClass().getSimpleName();
    }

    /**
     * 获取类名
     *
     * @param clz 需要获取名称的类
     * @return 类名字符串
     */
    protected String getName(Class<?> clz) {
        return clz.getClass().getSimpleName();
    }

    /**
     * 消息提示框
     * https://www.jianshu.com/p/4551734b3c21
     *
     * @param message 提示消息文本
     */
    public void toast(String message) {
        try {
            if (toast != null) {
                toast.setText(message);
            } else {
                toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            }
            toast.show();
        } catch (Exception e) {
            //解决在子线程中调用Toast的异常情况处理
            Looper.prepare();
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
    }

    /**
     * 消息提示框
     * https://www.jianshu.com/p/4551734b3c21
     *
     * @param messageId 提示消息文本ID
     */
    protected void toast(int messageId) {
        try {
            if (toast != null) {
                toast.setText(messageId);
            } else {
                toast = Toast.makeText(context, messageId, Toast.LENGTH_SHORT);
            }
            toast.show();
        } catch (Exception e) {
            //解决在子线程中调用Toast的异常情况处理
            Looper.prepare();
            Toast.makeText(context, messageId, Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
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
     * 返回历史界面
     *
     * @param targetActivity 指定的Activity对应的class
     */
    public static void backTo(Class<?> targetActivity) {
        int size = activities.size();
        if (activitiesMap.get(targetActivity) != null)
            for (int i = size - 1; i >= 0; i--) {
                if (targetActivity.equals(activities.get(i).getClass())) {
                    break;
                } else {
                    activities.get(i).finish();
                }
            }
        else
            Logger.e(activities.get(size - 1).getClass().getSimpleName(), "activity not open for " + targetActivity.getSimpleName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 移除当前的Activity
        activities.remove(this);
        activitiesMap.remove(getClass());
        // 清除网络状态接受者
        event = null;
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
     * 检测当的网络（Wlan、3G/2G）状态
     *
     * @return true 表示网络可用
     */
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 简化获取View
     *
     * @param viewId View的ID
     * @param <T>    将View转化为对应泛型，简化强转的步骤
     * @return ID对应的View
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        return (T) findViewById(viewId);
    }

    /**
     * 简化获取View
     *
     * @param view   父view
     * @param viewId View的ID
     * @param <T>    将View转化为对应泛型，简化强转的步骤
     * @return ID对应的View
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(View view, int viewId) {
        return (T) view.findViewById(viewId);
    }

    /**
     * 简化获取View
     *
     * @param layoutId 父布局Id
     * @param viewId   View的ID
     * @param <T>      将View转化为对应泛型，简化强转的步骤
     * @return ID对应的View
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int layoutId, int viewId) {
        return (T) LayoutInflater.from(context).inflate(layoutId, null).findViewById(viewId);
    }

    /**
     * 设置点击事件
     *
     * @param layouts 点击控件Id
     */
    protected void setOnClickListener(int... layouts) {
        for (int layout : layouts) {
            getView(layout).setOnClickListener(this);
        }
    }

    /**
     * SDK初始化
     */
    protected void initSDK() {
    }

    /**
     * 获取布局ID
     *
     * @return 获取的布局ID
     */
    protected abstract int getLayoutId();

    /**
     * 获取所有View信息
     */
    protected abstract void findViews();

    /**
     * 初始化布局信息
     */
    protected abstract void formatViews();

    /**
     * 初始化数据信息
     */
    protected abstract void formatData();

    /**
     * 初始化Bundle
     */
    protected abstract void getBundle(Bundle bundle);
}

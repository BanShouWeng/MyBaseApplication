package com.banshouweng.mybaseapplication.base.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.banshouweng.mybaseapplication.ui.activity.MainActivity;
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
     * 网络状态监听接受者
     */
    public static NetBroadcastReceiver.NetEvent event;
    /**
     * 用于传递的上下文信息
     */
    public Context context;
    public Activity activity;

    /**
     * 当前打开Activity存储List
     */
    private static List<Activity> activities = new ArrayList<>();
    /**
     * 调用backTo方法时，验证该Activity是否已经存在
     */
    private static Map<Class<?>, Activity> activitiesMap = new HashMap<>();
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activities.add(this);
        activitiesMap.put(getClass(), this);
        context = getApplicationContext();
        activity = this;
        event = this;

        initSDK();
        getBundle(getIntent().getBundleExtra("bundle"));
    }

    /**
     * 跳转页面
     *
     * @param targetActivity 所跳转的目的Activity类
     */
    public void jumpTo(Class<?> targetActivity) {
        Intent intent = new Intent(this, targetActivity);
        if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
            startActivity(intent);
        } else {
            Logger.e(getName(), "activity not found for " + targetActivity.getSimpleName());
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
            startActivity(intent);
        } else {
            Logger.e(getName(), "activity not found for " + targetActivity.getSimpleName());
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
            startActivityForResult(intent, requestCode);
        } else {
            Logger.e(getName(), "activity not found for " + targetActivity.getSimpleName());
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
            startActivityForResult(intent, requestCode);
        } else {
            Logger.e(getName(), "activity not found for " + targetActivity.getSimpleName());
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
     * @param targetClass 需要获取名称的类
     * @return 类名字符串
     */
    protected String getName(Class<?> targetClass) {
        return targetClass.getSimpleName();
    }

    /**
     * 消息提示框
     * https://www.jianshu.com/p/4551734b3c21
     *
     * @param message 提示消息文本
     */
    @SuppressLint("ShowToast")
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
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.show();
            Looper.loop();
        }
    }

    /**
     * 消息提示框
     * https://www.jianshu.com/p/4551734b3c21
     *
     * @param messageId 提示消息文本ID
     */
    @SuppressLint("ShowToast")
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
            toast = Toast.makeText(context, messageId, Toast.LENGTH_SHORT);
            toast.show();
            Looper.loop();
        }
    }

    /**
     * 关闭所有Activity（除MainActivity以外）
     */
    public void finishActivity() {
        for (Activity activity : activities) {
            if (activity.getClass().equals(MainActivity.class))
                break;
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
     * 初始化Bundle
     */
    protected abstract void getBundle(Bundle bundle);
}

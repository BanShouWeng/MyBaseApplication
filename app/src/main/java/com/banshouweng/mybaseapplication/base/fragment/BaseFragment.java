package com.banshouweng.mybaseapplication.base.fragment;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.banshouweng.mybaseapplication.R;
import com.banshouweng.mybaseapplication.base.activity.BaseActivity;
import com.banshouweng.mybaseapplication.utils.Logger;

/**
 * 《一个Android工程的从零开始》
 *
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {
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
     * 用于传递的上下文信息
     */
    protected Context context;
    protected Activity activity;

    /**
     * 返回按钮
     */
    private ImageView baseBack;
    /**
     * 关闭按钮
     */
    private ImageView baseClose;

    private View currentLayout;
    private ViewStub viewStub;
    private boolean isUseBase = false;
    private Toast toast;

    /**
     * 隐藏头布局
     */
    protected void hideTitle() {
        viewStub.setVisibility(View.GONE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the title_layout for this fragment
        currentLayout = inflater.inflate(R.layout.fragment_base, container, false);
        isUseBase = true;
        return currentLayout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isUseBase) {
            setBaseContentView(getLayoutId());
        }
        getBundle();
        findViews();
        formatData();
        formatViews();
    }

    /**
     * 控件初始化
     */
    protected void initBaseView() {
        if (viewStub == null) {
            viewStub = getView(R.id.base_title_layout);
            viewStub.inflate();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context.getApplicationContext();
        activity = (Activity) context;
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
                    activity.finish();
                }
            });
        }
    }

    /**
     * 设置标题
     *
     * @param titleId  标题的文本
     * @param showBack 是否显示返回键
     */
    public void setTitle(int titleId, boolean showBack) {
        initBaseView();
        ((TextView) getView(R.id.base_title)).setText(titleId);
        if (showBack) {
            baseBack = getView(R.id.base_back);
            baseBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.finish();
                }
            });
        }
    }

    /**
     * 设置返回点击事件
     */
    protected void setBaseBack() {
        initBaseView();
        baseBack.setOnClickListener(this);
    }

    /**
     * 设置关闭点击事件
     */
    public void setBaseClose() {
        initBaseView();
        setBaseClose(false);
    }

    /**
     * 设置关闭点击事件
     *
     * @param isResetClose 是否重置关闭按钮点击事件
     */
    public void setBaseClose(boolean isResetClose) {
        baseClose = getView(R.id.base_close);
        if (isResetClose) {
            baseClose.setOnClickListener(this);
        } else {
            baseClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.finish();
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
     * @param clz 关闭后所要返回的Activity
     */
    public void setBaseClose(final Class<?> clz) {
        initBaseView();
        baseClose = getView(R.id.base_close);
        baseClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseActivity.backTo(clz);
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
    protected ImageView setBaseRightIcon1(int resId, String alertText) {
        initBaseView();
        ImageView baseRightIcon1 = getView(R.id.base_right_icon1);
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
    protected ImageView setBaseRightIcon2(int resId, String alertText) {
        initBaseView();
        ImageView baseRightIcon2 = getView(R.id.base_right_icon2);
        baseRightIcon2.setImageResource(resId);
        baseRightIcon2.setVisibility(View.VISIBLE);
        //语音辅助提示的时候读取的信息
        baseRightIcon2.setContentDescription(alertText);
        baseRightIcon2.setOnClickListener(this);
        return baseRightIcon2;
    }

    /**
     * 最右侧文本功能键设置方法
     *
     * @param text 文本信息
     * @return 将当前TextView返回方便进一步处理
     */
    protected TextView setBaseRightText(String text) {
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
    protected TextView setBaseRightText(int textId) {
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
        LinearLayout layout = getView(R.id.base_main_layout);

        //获取布局，并在BaseActivity基础上显示
        final View view = activity.getLayoutInflater().inflate(layoutId, null);
        //关闭键盘
        hideKeyBoard();
        //给EditText的父控件设置焦点，防止键盘自动弹出
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layout.addView(view, params);
    }

    /**
     * 隐藏键盘
     */
    protected void hideKeyBoard() {
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 跳转页面
     *
     * @param targetActivity 所跳转的目标Activity类
     */
    protected void jumpTo(Class<?> targetActivity) {
        Intent intent =  new Intent(context, targetActivity);
        if (activity.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
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
     * @param targetActivity    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    protected void jumpTo(Class<?> targetActivity, Bundle bundle) {
        Intent intent = new Intent(context, targetActivity);
        if (bundle != null) {
            intent.putExtra("bundle", bundle);
        }
        if (activity.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
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
     * @param targetActivity         所跳转的Activity类
     * @param requestCode 请求码
     */
    protected void jumpTo(Class<?> targetActivity, int requestCode) {
        Intent intent =  new Intent(context, targetActivity);
        if (activity.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
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
     * @param targetActivity         所跳转的Activity类
     * @param bundle      跳转所携带的信息
     * @param requestCode 请求码
     */
    protected void jumpTo(Class<?> targetActivity, int requestCode, Bundle bundle) {
        Intent intent = new Intent(context, targetActivity);
        if (bundle != null) {
            intent.putExtra("bundle", bundle);
        }
        if (activity.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
            try {
                startActivityForResult(intent, requestCode);
            } catch (ActivityNotFoundException e) {
                Logger.e(getName(), "activity not found for " + targetActivity.getSimpleName());
            }
        }
    }

    /**
     * 获取当前Fragment类名
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
     * 简化获取View
     *
     * @param viewId View的ID
     * @param <T>    将View转化为对应泛型，简化强转的步骤
     * @return ID对应的View
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T getView(int viewId) {
        return (T) currentLayout.findViewById(viewId);
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
    protected abstract void getBundle();
}

package com.banshouweng.bswBase.base.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.banshouweng.bswBase.R;
import com.banshouweng.bswBase.receiver.NetBroadcastReceiver;
import com.banshouweng.bswBase.utils.CommonUtils;
import com.banshouweng.bswBase.utils.Const;


/**
 * 《一个Android工程的从零开始》
 *
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
 */
public abstract class BaseLayoutActivity extends BaseActivity implements NetBroadcastReceiver.NetEvent, View.OnClickListener {
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

    private ImageView baseRightIcon1;
    /**
     * 是否重置Back的点击事件
     */
    private boolean isBaseBackReset = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base_layout);
        setBaseContentView(getLayoutId());
        findViews();
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
            CommonUtils.log().e(getName(), "baseBack is not exist!");
        }
    }

    /**
     * 显示返回键
     */
    public void showBack() {
        if (null != baseBack) {
            baseBack.setVisibility(View.VISIBLE);
        } else {
            CommonUtils.log().e(getName(), "baseBack is not exist!");
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
        if (Const.isEmpty(baseBack)) {
            baseBack = getView(R.id.base_back);
        }
        baseBack.setVisibility(showBack ? View.VISIBLE : View.GONE);
        if (showBack && ! isBaseBackReset) {
            baseBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.finish();
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
        if (Const.isEmpty(baseBack)) {
            baseBack = getView(R.id.base_back);
        }
        baseBack.setVisibility(showBack ? View.VISIBLE : View.GONE);
        if (showBack) {
            baseBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.finish();
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
            CommonUtils.log().e(getName(), "baseBack is not exist!");
        }
    }

    /**
     * 重置返回点击事件
     *
     * @param resId 重置返回按钮图标
     */
    public void setBaseBack(int resId) {
        initBaseView();
        isBaseBackReset = true;
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
            CommonUtils.log().e(getName(), "baseClose is not exist");
        }
    }

    /**
     * 显示关闭按钮
     */
    public void showBaseClose() {
        if (null != baseClose) {
            baseClose.setVisibility(View.VISIBLE);
        } else {
            CommonUtils.log().e(getName(), "baseClose is not exist");
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
            CommonUtils.log().e(getName(), "You must inflate the baseRightIcon1 before use baseRightIcon2!");
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
        View view = mActivity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
}

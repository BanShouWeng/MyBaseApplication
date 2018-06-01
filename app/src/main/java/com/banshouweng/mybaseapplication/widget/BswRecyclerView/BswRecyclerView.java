package com.banshouweng.mybaseapplication.widget.BswRecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.Toast;

import com.banshouweng.mybaseapplication.R;

import java.util.List;

/**
 * 自定义RecyclerView布局
 *
 * @author leiming
 * @date 2018/4/22 11:26
 */
public class BswRecyclerView<T extends Object> extends RecyclerView {
    /**
     * 纵向布局
     */
    public static int VERTICAL = 0;
    /**
     * 横向布局
     */
    public static int HORIZONTAL = 1;

    /**
     * 上下文
     */
    private Context context;

    /**
     * 自定义适配器
     */
    private BswRecyclerAdapter<T> adapter;
    private Toast toast;

    /**
     * @param context 上下文
     */
    public BswRecyclerView(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * @param context 上下文
     * @param attrs   属性设置
     */
    public BswRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    /**
     * @param context  上下文
     * @param attrs    属性设置
     * @param defStyle http://blog.csdn.net/mybeta/article/details/39993449大神的博客
     */
    public BswRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    /**
     * 初始化适配器
     *
     * @param layoutId 适配器每个item的布局
     * @param callBack 布局配置回调接口
     * @return 当前RecyclerView
     */
    public BswRecyclerView initAdapter(int layoutId, ConvertViewCallBack<T> callBack) {
        adapter = new BswRecyclerAdapter<>(context, layoutId, callBack);
        setAdapter(adapter);
        return this;
    }

    /**
     * 初始化适配器
     *
     * @param multiplexAdapterCallBack 复杂布局回调
     * @param layouts                  布局列表
     * @return 当前RecyclerView
     */
    public BswRecyclerView initAdapter(MultiplexAdapterCallBack<T> multiplexAdapterCallBack, int... layouts) {
        adapter = new BswRecyclerAdapter<>(context, multiplexAdapterCallBack, layouts);
        setAdapter(adapter);
        return this;
    }

    /**
     * 设置devider
     *
     * @param type devider类型
     * @return 当前RecyclerView
     */
    public BswRecyclerView setDecoration(int type) {
        addItemDecoration(new BswDecoration(context, type));
        return this;
    }

    /**
     * 设置默认devider
     *
     * @return 当前RecyclerView
     */
    public BswRecyclerView setDecoration() {
        setDecoration(BswDecoration.BOTTOM_DECORATION);
        return this;
    }

    /**
     * 设置数据
     *
     * @param mData 所要展示的数据
     */
    public void setData(List<T> mData) {
        adapter.setData(mData);
    }

    /**
     * 设置数据
     *
     * @param mData 所要展示的数据
     */
    public void setData(List<T> mData, int pageNumber) {
        adapter.setData(mData, pageNumber);
    }

    /**
     * 添加数据
     *
     * @param mData 所要添加的数据
     */
    public void addData(List<T> mData) {
        adapter.addData(mData);
    }

    /**
     * 清除数据
     *
     * @param isNotify 是否刷新布局
     */
    public void clearData(boolean isNotify) {
        adapter.clearData(isNotify);
    }

    /**
     * 数据替换
     *
     * @param mData 替换的数据
     */
    public void replaceData(List<T> mData) {
        adapter.replaceData(mData);
    }

    /**
     * 移除数据
     *
     * @param pos 被移除数据的位置
     */
    public void removeItem(int pos) {
        adapter.removeItem(pos);
    }

    public void scrollViewNesting() {
        setHasFixedSize(true);
        setNestedScrollingEnabled(false);
    }

    /**
     * 设置布局样式
     *
     * @return 当前RecyclerView
     */
    public BswRecyclerView setLayoutManager() {
        setLayoutManager(VERTICAL);
        return this;
    }

    /**
     * 设置布局样式
     *
     * @param layoutrType 布局样式
     * @return 当前RecyclerView
     */
    public BswRecyclerView setLayoutManager(int layoutrType) {
        if (layoutrType == HORIZONTAL) // 横向列表
        {
            setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        } else //纵向列表
        {
            setLayoutManager(new LinearLayoutManager(context));
        }
        return this;
    }

    /**
     * 设置布局样式
     *
     * @param layoutrType   布局样式
     * @param reverseLayout 横向布局是否可以循环滑动标志位： true，可以；false，不可以
     * @return 当前RecyclerView
     */
    public BswRecyclerView setLayoutManager(int layoutrType, boolean reverseLayout) {
        if (layoutrType == HORIZONTAL) // 横向列表
        {
            setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, reverseLayout));
        } else //纵向列表
        {
            setLayoutManager(new LinearLayoutManager(context));
        }
        return this;
    }

    /**
     * 设置布局样式
     *
     * @param layoutrType 布局样式
     * @param spanCount   拓展到多少行/列
     * @return 当前RecyclerView
     */
    public BswRecyclerView setLayoutManager(int layoutrType, int spanCount) {
        if (spanCount == 1) // 当spanCount为的时候，为线性列表
        {
            return setLayoutManager(layoutrType);
        }
        if (layoutrType == HORIZONTAL) // 横向列表
        {
            setLayoutManager(new GridLayoutManager(context, spanCount, GridLayoutManager.HORIZONTAL, false));
        } else //纵向列表
        {
            setLayoutManager(new GridLayoutManager(context, spanCount));
        }
        return this;
    }

    /**
     * 设置布局样式
     *
     * @param layoutrType   布局样式
     * @param spanCount     拓展到多少行/列
     * @param reverseLayout 横向布局是否可以循环滑动标志位： true，可以；false，不可以
     * @return 当前RecyclerView
     */
    public BswRecyclerView setLayoutManager(int layoutrType, int spanCount, boolean reverseLayout) {
        if (spanCount == 1) {
            return setLayoutManager(layoutrType, reverseLayout);
        }

        if (layoutrType == HORIZONTAL) {
            setLayoutManager(new GridLayoutManager(context, spanCount, GridLayoutManager.HORIZONTAL, reverseLayout));
        } else {
            setLayoutManager(new GridLayoutManager(context, spanCount));
        }
        return this;
    }

    /**
     * 设置下拉刷新监听接口
     *
     * @param loadListener 下拉刷新监听接口
     */
    public void setLoadListener(final OnLoadListener loadListener) {
        // 滑动事件监听
        setOnScrollListener(new OnScrollListener() {
            /**
             * 最后一个显示的位置
             */
            int lastPosition;
            /**
             * 最后一项
             */
            int lastVisibleItem;

            /**
             * 是否是为了显示Footer而滑动导致的到最后一项
             */
            boolean isSmoothToEnd = false;

            /**
             * 滑动结束
             * @param recyclerView 当前recyclerView
             * @param newState 滑动后的状态newState
             */
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                /*
                 * scrollState有三种状态，分别是SCROLL_STATE_IDLE、SCROLL_STATE_TOUCH_SCROLL、SCROLL_STATE_FLING
                 * SCROLL_STATE_IDLE是当屏幕停止滚动时
                 * SCROLL_STATE_TOUCH_SCROLL是当用户在以触屏方式滚动屏幕并且手指仍然还在屏幕上时
                 * SCROLL_STATE_FLING是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时
                 */
                if (newState == SCROLL_STATE_IDLE) {
//                    boolean toBottom = recyclerView.getBottom() == recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount() - 1).getBottom();
                    if (lastPosition > 0 && lastVisibleItem == adapter.getItemCount() - 1) {
                        if (loadListener.canLoadMore()) {
                            if (isSmoothToEnd) {
                                isSmoothToEnd = false;
                            } else {
                                isSmoothToEnd = true;
                                adapter.setShowFooter();
                                smoothScrollToPosition(adapter.getItemCount());
                                loadListener.loadData();
                            }
                        } else {
                            toast(R.string.all_loaded);
                        }
                    }
                }
            }

            /**
             * 滑动过程中
             * @param recyclerView 当前recyclerView
             * @param dx 当前的x坐标
             * @param dy 当前的y坐标
             */
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                lastPosition = dy;
            }
        });
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

    public int getItemCount() {
        return adapter.getItemCount();
    }
}

package com.banshouweng.mybaseapplication.widget.MyRecyclerView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.banshouweng.mybaseapplication.base.BaseBean;

import java.util.List;

/**
 * 《一个Android工程的从零开始》
 * 自定义RecyclerView布局
 *
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
 */
public class CustomRecyclerView<T extends BaseBean> extends RecyclerView {
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
    private BaseRecyclerAdapter<T> adapter;

    /**
     * @param context 上下文
     */
    public CustomRecyclerView(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * @param context 上下文
     * @param attrs   属性设置
     */
    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    /**
     * @param context  上下文
     * @param attrs    属性设置
     * @param defStyle http://blog.csdn.net/mybeta/article/details/39993449大神的博客
     */
    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
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
    public CustomRecyclerView initAdapter(int layoutId, ConvertViewCallBack<T> callBack) {
        adapter = new BaseRecyclerAdapter<>(context, layoutId, callBack);
        setAdapter(adapter);
        return this;
    }

    public CustomRecyclerView initAdapter(MultiplexAdapterCallBack<T> multiplexAdapterCallBack, int... layouts) {
        adapter = new BaseRecyclerAdapter<>(context, multiplexAdapterCallBack, layouts);
        setAdapter(adapter);
        return this;
    }

    /**
     * 设置数据
     *
     * @param myData 所要展示的数据
     */
    public void setData(List<T> myData) {
        adapter.setData(myData);
    }

    /**
     * 添加数据
     *
     * @param myData 所要添加的数据
     */
    public void addData(List<T> myData) {
        adapter.addData(myData);
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
     * 移除数据
     *
     * @param pos 被移除数据的位置
     */
    public void removeItem(int pos) {
        adapter.removeItem(pos);
    }

    /**
     * 设置布局样式
     *
     * @param layoutrType 布局样式
     * @return 当前RecyclerView
     */
    public CustomRecyclerView setLayoutManager(int layoutrType) {
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
    public CustomRecyclerView setLayoutManager(int layoutrType, boolean reverseLayout) {
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
    public CustomRecyclerView setLayoutManager(int layoutrType, int spanCount) {
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
    public CustomRecyclerView setLayoutManager(int layoutrType, int spanCount, boolean reverseLayout) {
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
                    if (lastPosition > 0 && lastVisibleItem == adapter.getItemCount() - 1 && loadListener.canLoadMore()) {
                        loadListener.loadData();
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
}

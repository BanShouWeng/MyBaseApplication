package com.banshouweng.bswBase.widget.BswRecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Looper;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.banshouweng.bswBase.R;
import com.banshouweng.bswBase.utils.rxbus2.Logger;

import java.util.List;

/**
 * 自定义RecyclerView布局
 *
 * @author 半寿翁
 * @date 2018/4/22 11:26
 */
public class BswRecyclerView<T> extends RecyclerView {


    /**
     * 上下文
     */
    private Context context;

    /**
     * 自定义适配器
     */
    private BswRecyclerAdapter<T> adapter;
    private Toast toast;
    private Logger logger;

    /**
     * @param context 上下文
     */
    public BswRecyclerView(Context context) {
        this(context, null);
    }

    /**
     * @param context 上下文
     * @param attrs   属性设置
     */
    public BswRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * @param context  上下文
     * @param attrs    属性设置
     * @param defStyle http://blog.csdn.net/mybeta/article/details/39993449大神的博客
     */
    public BswRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        logger = new Logger();
    }

    /**
     * 初始化适配器
     *
     * @param layoutId 适配器每个item的布局
     * @param callBack 布局配置回调接口
     * @return 当前RecyclerView
     */
    @SuppressWarnings("UnusedReturnValue")
    public BswRecyclerView initAdapter(@LayoutRes int layoutId, ConvertViewCallBack<T> callBack) {
        adapter = new BswRecyclerAdapter<>(context, layoutId, callBack);
        setAdapter(adapter);
        return this;
    }

    /**
     * 设置divider
     *
     * @param type divider类型
     * @return 当前RecyclerView
     */
    @SuppressWarnings("UnusedReturnValue")
    private BswRecyclerView setDecoration(@LimitAnnotation.DecorationType int type) {
        addItemDecoration(new BswDecoration(context, type));
        return this;
    }

    /**
     * 设置默认divider
     *
     * @return 当前RecyclerView
     */
    @SuppressWarnings("UnusedReturnValue")
    public BswRecyclerView setDecoration() {
        setDecoration(LimitAnnotation.BOTTOM_DECORATION);
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
    public void setData(List<T> mData, @IntRange(from = 1) int pageNumber, @IntRange(from = 1) int pageSize) {
        adapter.setData(mData, pageNumber, pageSize);
    }

    /**
     * 清除数据
     *
     * @param isNotify 是否刷新布局
     */
    @SuppressWarnings("unused")
    public void clearData(boolean isNotify) {
        adapter.clearData(isNotify);
    }

    /**
     * 替换对应位置的数据
     *
     * @param position 被替换位置
     * @param t        替换数据
     */
    void replaceItem(int position, T t) {
        adapter.replaceItem(position, t);
    }

    /**
     * 获取某一项
     *
     * @param position 位置
     */
    public T getItem(int position) {
        return adapter.getItem(position);
    }

    /**
     * 刷新数据
     */
    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

    /**
     * 移除数据
     *
     * @param pos 被移除数据的位置
     */
    @SuppressWarnings("unused")
    public void removeItem(int pos) {
        adapter.removeItem(pos);
    }

    /**
     * 设置布局样式
     *
     * @return 当前RecyclerView
     */
    public BswRecyclerView setLayoutManager() {
        setLayoutManager(LimitAnnotation.VERTICAL);
        return this;
    }

    /**
     * 设置布局样式
     *
     * @param layoutType 布局样式
     * @return 当前RecyclerView
     */
    public BswRecyclerView setLayoutManager(@LimitAnnotation.LayoutManagerType int layoutType) {
        if (layoutType == HORIZONTAL) // 横向列表
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
     * @param layoutType    布局样式
     * @param reverseLayout 横向布局是否可以循环滑动标志位： true，可以；false，不可以
     * @return 当前RecyclerView
     */
    private BswRecyclerView setLayoutManager(@LimitAnnotation.LayoutManagerType int layoutType, boolean reverseLayout) {
        if (layoutType == HORIZONTAL) // 横向列表
        {
            setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, reverseLayout));
        } else //纵向列表
        {
            setLayoutManager(new LinearLayoutManager(context));
        }
        return this;
    }

    /**
     * 根据输入框输入文本，动态筛选条目
     *
     * @param filterEt              用于筛选的文本输入框
     * @param bswFilterDataCallBack 筛选条件判断回调
     */
    public BswRecyclerView setFilterEt(EditText filterEt, BswFilterDataCallBack<T> bswFilterDataCallBack) {
        adapter.setFilterEt(filterEt, bswFilterDataCallBack);
        return this;
    }

    /**
     * 设置布局过滤回调
     *
     * @param bswFilterLayoutFilter 回调接口
     */
    public BswRecyclerView setLayoutFilterCallBack(BswFilterLayoutFilter<T> bswFilterLayoutFilter) {
        adapter.setLayoutFilterCallBack(bswFilterLayoutFilter);
        return this;
    }

    /**
     * 设置布局样式
     *
     * @param layoutType 布局样式
     * @param spanCount  拓展到多少行/列
     * @return 当前RecyclerView
     */
    public BswRecyclerView setLayoutManager(@LimitAnnotation.LayoutManagerType int layoutType, int spanCount) {
        if (spanCount == 1) // 当spanCount为的时候，为线性列表
        {
            return setLayoutManager(layoutType);
        }
        if (layoutType == HORIZONTAL) // 横向列表
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
     * @param layoutType    布局样式
     * @param spanCount     拓展到多少行/列
     * @param reverseLayout 横向布局是否可以循环滑动标志位： true，可以；false，不可以
     * @return 当前RecyclerView
     */
    public BswRecyclerView setLayoutManager(@LimitAnnotation.LayoutManagerType int layoutType, int spanCount, boolean reverseLayout) {
        if (spanCount == 1) {
            return setLayoutManager(layoutType, reverseLayout);
        }

        if (layoutType == HORIZONTAL) {
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
    @SuppressWarnings("unused")
    public void setLoadListener(final OnLoadListener loadListener) {
        // 滑动事件监听
        //noinspection deprecation
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
                    boolean toBottom = recyclerView.getBottom() == recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount() - 1).getBottom();
                    if (lastPosition > 0 && lastVisibleItem == adapter.getItemCount() - 1 && toBottom) {
                        if (loadListener.allLoaded()) {
                            toast(R.string.all_loaded);
                        } else {
                            if (loadListener.canLoadMore()) {
                                if (isSmoothToEnd) {
                                    isSmoothToEnd = false;
                                } else {
                                    isSmoothToEnd = true;
                                    adapter.setShowFooter();
                                    smoothScrollToPosition(adapter.getItemCount());
                                    loadListener.loadData();
                                }
                            }
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
    private void toast(@StringRes int messageId) {
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        // 手指按下的时候，如果有开启的菜单，只要手指不是落在该Item上，则关闭菜单, 并且不分发事件。
        if (action == MotionEvent.ACTION_DOWN) {
            int x = (int) ev.getX();
            int y = (int) ev.getY();
            View openItem = findOpenItem();
            if (openItem != null && openItem != getTouchItem(x, y)) {
                SwipeItemLayout swipeItemLayout = findSwipeItemLayout(openItem);
                if (swipeItemLayout != null) {
                    swipeItemLayout.close();
                    logger.i("BswRecyclerView", "swipeItemLayout.close");
                    return false;
                }
            }
            logger.i("BswRecyclerView", "ACTION_DOWN");
        } else if (action == MotionEvent.ACTION_POINTER_DOWN) {
            // FIXME: 2017/3/22 不知道怎么解决多点触控导致可以同时打开多个菜单的bug，先暂时禁止多点触控
            logger.i("BswRecyclerView", "多点触控");
            return false;
        } else {
            logger.i("BswRecyclerView", "其他");
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 获取按下位置的Item
     */
    @Nullable
    private View getTouchItem(int x, int y) {
        Rect frame = new Rect();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == VISIBLE) {
                child.getHitRect(frame);
                if (frame.contains(x, y)) {
                    return child;
                }
            }
        }
        return null;
    }

    /**
     * 找到当前屏幕中开启的的Item
     */
    @Nullable
    private View findOpenItem() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            SwipeItemLayout swipeItemLayout = findSwipeItemLayout(getChildAt(i));
            if (swipeItemLayout != null && swipeItemLayout.isOpen()) {
                return getChildAt(i);
            }
        }
        return null;
    }

    /**
     * 获取该View
     */
    @Nullable
    private SwipeItemLayout findSwipeItemLayout(View view) {
        if (view instanceof SwipeItemLayout) {
            return (SwipeItemLayout) view;
        } else if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                SwipeItemLayout swipeLayout = findSwipeItemLayout(group.getChildAt(i));
                if (swipeLayout != null) {
                    return swipeLayout;
                }
            }
        }
        return null;
    }
}

package com.banshouweng.bswBase.widget.BswRecyclerView;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;


import com.banshouweng.bswBase.R;

import java.util.List;

/**
 * RecyclerView适配器
 *
 * @author 半寿翁
 * @date 2018/4/22 11:25
 */
class BswRecyclerAdapter<T> extends RecyclerView.Adapter {

    private final int FOOTER_TYPE = 0x9999;

    /**
     * 上下文
     */
    private Context context;
    /**
     * 布局解析
     */
    private LayoutInflater layoutInflater;
    private BswRecyclerData<T> bswRecyclerData;
    /**
     * 布局ID
     */
    @LayoutRes
    private int layoutId;
    private boolean showFooter = false;

    private ConvertViewCallBack<T> convertViewCallBack;

    public void setShowFooter() {
        showFooter = true;
        notifyDataSetChanged();
    }


    /**
     * 显示单一布局的Adapter
     *
     * @param mData               所要展示的数据列表
     * @param context             上下文
     * @param layoutId            布局Id
     * @param convertViewCallBack 布局设置回调接口
     */
    public BswRecyclerAdapter(List<T> mData, Context context, @LayoutRes int layoutId, ConvertViewCallBack<T> convertViewCallBack) {
        this.context = context;
        this.layoutId = layoutId;
        this.convertViewCallBack = convertViewCallBack;
        layoutInflater = LayoutInflater.from(this.context);
        bswRecyclerData = new BswRecyclerData<>(this);
    }

    /**
     * 显示单一布局的Adapter
     *
     * @param context             上下文
     * @param layoutId            布局Id
     * @param convertViewCallBack 布局设置回调接口
     */
    public BswRecyclerAdapter(Context context, @LayoutRes int layoutId, ConvertViewCallBack<T> convertViewCallBack) {
        this(null, context, layoutId, convertViewCallBack);
    }

    @Override
    public int getItemViewType(int position) {
        if (showFooter && position == bswRecyclerData.getDataSize()) {
            return FOOTER_TYPE;
        } else {
            BswLayoutItem layoutItem = bswRecyclerData.getLayoutItemByPosition(position);
            if (null == layoutItem)
                return - 1;
            else
                return layoutItem.getTag();
        }
    }

    /**
     * 获取对应位置的数据
     *
     * @param position 位置
     * @return 数据
     */
    public T getItem(int position) {
        //noinspection unchecked
        return (T) bswRecyclerData.getDataItem(position).getT();
    }

    /**
     * 替换对应位置的数据
     *
     * @param position 被替换位置
     * @param t        替换数据
     */
    void replaceItem(int position, T t) {
        bswRecyclerData.replaceItem(position, t);
    }

    /**
     * 设置数据
     *
     * @param mData 所要展示的数据列表
     */
    public void setData(List<T> mData) {
        bswRecyclerData.setData(mData);
    }

    /**
     * 设置数据
     *
     * @param mData      所要展示的数据列表
     * @param pageNumber 页码
     */
    public void setData(List<T> mData, @IntRange(from = 1) int pageNumber, @IntRange(from = 1) int pageSize) {
        bswRecyclerData.setData(mData, pageNumber, pageSize);
    }

    /**
     * 清除数据
     *
     * @param isNotify 是否刷新布局
     */
    public void clearData(boolean isNotify) {
        bswRecyclerData.clearData(isNotify);
    }

    /**
     * 移除数据
     *
     * @param pos 被移除数据的位置
     */
    public void removeItem(int pos) {
        this.notifyItemRemoved(pos);
    }

    /**
     * 根据输入框输入文本，动态筛选条目
     *
     * @param filterEt              用于筛选的文本输入框
     * @param bswFilterDataCallBack 筛选条件判断回调
     */
    public void setFilterEt(EditText filterEt, BswFilterDataCallBack<T> bswFilterDataCallBack) {
        bswRecyclerData.setFilterEt(filterEt, bswFilterDataCallBack);
    }

    /**
     * 设置布局过滤回调
     *
     * @param bswFilterLayoutFilter 回调接口
     */
    public void setLayoutFilterCallBack(BswFilterLayoutFilter<T> bswFilterLayoutFilter) {
        bswRecyclerData.setLayoutFilterCallBack(bswFilterLayoutFilter);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (FOOTER_TYPE == viewType) {
            // inflate形式是避免内部布局宽高失效
            return new RecyclerViewHolder(context, layoutInflater.inflate(R.layout.footer_laoding_layout, parent, false));
        } else {
            if (viewType == - 1) {
                // inflate形式是避免内部布局宽高失效
                return new RecyclerViewHolder(context, layoutInflater.inflate(layoutId, parent, false));
            } else {
                BswLayoutItem bswLayoutItem = bswRecyclerData.getLayoutItemByTag(viewType);
                if (null != bswLayoutItem && bswLayoutItem.isEdited()) {
                    SwipeItemLayout swipeItemLayout = new SwipeItemLayout(context);
                    FrameLayout.LayoutParams baseLp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    swipeItemLayout.setLayoutParams(baseLp);
                    int mainLayoutId = bswLayoutItem.get(LimitAnnotation.LAYOUT_MAIN);
                    int rightLayoutId = bswLayoutItem.get(LimitAnnotation.LAYOUT_RIGHT);
                    int leftLayoutId = bswLayoutItem.get(LimitAnnotation.LAYOUT_LEFT);
                    // 设置右滑显示布局
                    if (rightLayoutId != 0) {
                        View rightView = layoutInflater.inflate(rightLayoutId, swipeItemLayout, false);
                        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(rightView.getLayoutParams());
                        lp.gravity = Gravity.RIGHT;
                        rightView.setLayoutParams(lp);
                        swipeItemLayout.addView(rightView);
                    }
                    // 设置左滑显示布局
                    if (leftLayoutId != 0) {
                        View leftView = layoutInflater.inflate(leftLayoutId, swipeItemLayout, false);
                        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(leftView.getLayoutParams());
                        lp.gravity = Gravity.LEFT;
                        leftView.setLayoutParams(lp);
                        swipeItemLayout.addView(leftView);
                    }
                    // 如设置主布局，则替换
                    if (mainLayoutId != 0) {
                        View mainView = layoutInflater.inflate(mainLayoutId, swipeItemLayout, false);
                        swipeItemLayout.addView(mainView);
                    } else {
                        View mainView = layoutInflater.inflate(layoutId, swipeItemLayout, false);
                        swipeItemLayout.addView(mainView);
                    }
                    return new RecyclerViewHolder(context, swipeItemLayout);
                } else {
                    // inflate形式是避免内部布局宽高失效
                    return new RecyclerViewHolder(context, layoutInflater.inflate(layoutId, parent, false));
                }
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position >= bswRecyclerData.getDataSize()) {
            return;
        }
        BswLayoutItem layoutItem = bswRecyclerData.getLayoutItemByPosition(position);
        //noinspection unchecked
        convertViewCallBack.convert((RecyclerViewHolder) holder
                , bswRecyclerData.getDataItem(position).getT()
                , position
                , null == layoutItem ? - 1 : layoutItem.getLayoutTag());
    }

    @Override
    public int getItemCount() {
        int mDataSize = bswRecyclerData.getDataSize();
        if (showFooter) {
            mDataSize++;
        }
        return mDataSize;
    }
}

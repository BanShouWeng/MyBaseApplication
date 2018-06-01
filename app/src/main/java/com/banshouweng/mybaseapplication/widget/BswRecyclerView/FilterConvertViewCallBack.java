package com.banshouweng.mybaseapplication.widget.BswRecyclerView;

import java.util.List;

/**
 * RecyclerView布局回调接口
 *
 * @author leiming
 * @date 2018/4/22 11:26
 */
public interface FilterConvertViewCallBack<T> {
    /**
     * 布局设置方法
     *
     * @param holder   当前布局的ViewHolder
     * @param t        当前位置对应的数据bean对象
     * @param showPosition 当前布局Item的位置
     * @param allPosition 当前布局Item的位置
     */
    void convert(RecyclerViewHolder holder, T t, int showPosition, int allPosition);

    List<T> filter(List<T> data, CharSequence constraint);
}

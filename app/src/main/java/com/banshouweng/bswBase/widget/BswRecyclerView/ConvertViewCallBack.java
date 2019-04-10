package com.banshouweng.bswBase.widget.BswRecyclerView;

/**
 * RecyclerView布局回调接口
 *
 * @author 半寿翁
 * @date 2018/4/22 11:26
 */
public interface ConvertViewCallBack<T> {
    /**
     * 布局设置方法
     *
     * @param holder   当前布局的ViewHolder
     * @param t        当前位置对应的数据bean对象
     * @param position 当前布局Item的位置
     */
    T convert(RecyclerViewHolder holder, T t, int position, int layoutTag);
}

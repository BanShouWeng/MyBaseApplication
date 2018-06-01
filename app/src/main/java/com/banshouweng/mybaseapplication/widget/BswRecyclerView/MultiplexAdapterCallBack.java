package com.banshouweng.mybaseapplication.widget.BswRecyclerView;

/**
 * RecyclerView复用界面回调接口
 *
 * @author leiming
 * @date 2018/4/22 11:26
 */
public interface MultiplexAdapterCallBack<T> {
    /**
     * 布局设置方法
     *
     * @param holder   当前布局的ViewHolder
     * @param t        当前位置对应的数据bean对象
     * @param position 当前布局Item的位置
     */
    void convert(RecyclerViewHolder holder, T t, int position);

    /**
     * 复用布局设置的布局类型
     *
     * @param position 位置
     * @return ItemViewType
     */
    int getItemViewType(int position);

    /**
     * 复用布局设置Holder
     *
     * @param viewType 布局类型
     * @return 布局id
     */
    int onCreateHolder(int viewType, int[] layouts);
}

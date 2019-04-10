package com.banshouweng.bswBase.widget.BswRecyclerView;

/**
 * RecyclerView过滤筛选回调
 *
 * @author 半寿翁
 * @date 2018/4/22 11:26
 */
public interface BswFilterDataCallBack<T> {
    /**
     * 过滤筛选回调实现方法
     *
     * @param data       被筛选数据
     * @param constraint 筛选用的字段
     * @return 是否符合筛选条件
     */
    boolean filter(T data, CharSequence constraint);
}

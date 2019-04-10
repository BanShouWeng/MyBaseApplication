package com.banshouweng.bswBase.widget.BswRecyclerView;

/**
 * 缓存条目与标识的数据项
 *
 * @author leiming
 * @date 2019/3/12.
 */
public class BswDataItem<T> {
    /**
     * 区分项的标签
     */
    private int tag;
    /**
     * 条目的泛型
     */
    private T t;

    /**
     * 存储标签与条目数据
     *
     * @param tag 标签
     * @param t   条目数据
     */
    public BswDataItem(int tag, T t) {
        this.tag = tag;
        this.t = t;
    }

    public void setT(T t) {
        this.t = t;
    }

    /**
     * 获取标签
     *
     * @return 标签
     */
    public int getTag() {
        return tag;
    }

    /**
     * 获取条目数据
     *
     * @return 条目数据
     */
    public T getT() {
        return t;
    }
}

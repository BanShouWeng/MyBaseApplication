package com.banshouweng.bswBase.widget.BswRecyclerView;

import android.support.annotation.LayoutRes;
import android.util.SparseIntArray;

/**
 * 每个Item布局设置类
 *
 * @author leiming
 * @date 2019/3/12.
 */
public class BswLayoutItem {
    /**
     * 区分侧滑布局的标识
     */
    private int tag;
    private int layoutTag;

    /**
     * 各类布局存储列表：主布局、侧滑右侧布局、侧滑左侧布局
     */
    private SparseIntArray itemLayouts;

    /**
     * 构造方法
     */
    public BswLayoutItem() {
        itemLayouts = new SparseIntArray();
    }

    /**
     * 设置布局标识
     *
     * @param layoutTag 布局标识
     */
    public BswLayoutItem setLayoutTag(int layoutTag) {
        this.layoutTag = layoutTag;
        return this;
    }

    /**
     * 获取内部识别标识
     *
     * @param tag 内部识别标签
     */
    void setTag(int tag) {
        this.tag = tag;
    }

    /**
     * 获取用户使用的标签
     *
     * @return 用户判断布局的标签
     */
    public int getLayoutTag() {
        return layoutTag;
    }

    /**
     * 添加布局
     *
     * @param layoutType 布局类型
     * @param layoutId   布局Id
     * @return 当前页面，便于重复布局
     */
    public BswLayoutItem put(@LimitAnnotation.LayoutType int layoutType, @LayoutRes int layoutId) {
        itemLayouts.put(layoutType, layoutId);
        return this;
    }

    /**
     * 获取对应类型的布局Id
     *
     * @param layoutType 布局类型
     * @return 布局Id
     */
    public int get(@LimitAnnotation.LayoutType int layoutType) {
        return itemLayouts.get(layoutType);
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
     * 是否添加对应布局
     *
     * @return true添加了；false没有添加
     */
    boolean isEdited() {
        return itemLayouts.size() > 0;
    }
}

package com.banshouweng.bswBase.widget.BswRecyclerView;

/**
 * 上拉加载监听接口
 *
 * @author 半寿翁
 * @date 2018/4/22 11:26
 */
public interface OnLoadListener {
    /**
     * 上拉加载数据
     */
    void loadData();

    /**
     * 上一次未加载完成不加载新数据
     *
     * @return 是否可以加载数据：true，可以；false，不可以
     */
    boolean canLoadMore();

    /**
     * 当全部加载后不再加载
     *
     * @return 是否全部加载 true是；false 否
     */
    boolean allLoaded();
}

package com.banshouweng.mybaseapplication.widget.BswRecyclerView;

/**
 * 上拉加载监听接口
 *
 * @author leiming
 * @date 2018/4/22 11:26
 */
public interface OnLoadListener {
    /**
     * 上拉加载数据
     */
    void loadData();

    /**
     * 是否可以加载数据，当已经获取全部数据的时候，防止多余网络请求，或者开发中遇到的其他情况暂时不需要加载数据
     *
     * @return 是否可以加载数据：true，可以；false，不可以
     */
    boolean canLoadMore();
}

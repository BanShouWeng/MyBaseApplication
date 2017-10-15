package com.banshouweng.mybaseapplication.widget.MyRecyclerView;

/**
 * 《一个Android工程的从零开始》
 * 上拉加载监听接口
 *
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
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

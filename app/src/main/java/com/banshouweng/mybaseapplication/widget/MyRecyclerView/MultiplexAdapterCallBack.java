package com.banshouweng.mybaseapplication.widget.MyRecyclerView;

import com.banshouweng.mybaseapplication.base.RecyclerViewHolder;

/**
 * 《一个Android工程的从零开始》
 * RecyclerView复用界面回调接口
 *
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
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

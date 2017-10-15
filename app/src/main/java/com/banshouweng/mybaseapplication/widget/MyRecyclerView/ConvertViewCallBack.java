package com.banshouweng.mybaseapplication.widget.MyRecyclerView;

import com.banshouweng.mybaseapplication.base.RecyclerViewHolder;

/**
 * 《一个Android工程的从零开始》
 * RecyclerView布局回调接口
 *
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
 */
public interface ConvertViewCallBack<T> {
    void convert(RecyclerViewHolder holder, T t, int position);
}

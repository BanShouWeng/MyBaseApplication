package com.banshouweng.mybaseapplication.widget.MyRecyclerView;

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
    /**
     * 布局设置方法
     *
     * @param holder   当前布局的ViewHolder
     * @param t        当前位置对应的数据bean对象
     * @param position 当前布局Item的位置
     */
    void convert(RecyclerViewHolder holder, T t, int position);
}

package com.banshouweng.bswBase.widget.BswRecyclerView;

import android.annotation.SuppressLint;
import android.support.annotation.IntRange;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;

import com.banshouweng.bswBase.utils.Const;
import com.banshouweng.bswBase.utils.TxtUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 列表数据处理类
 *
 * @author leiming
 * @date 2019/3/12.
 */
public class BswRecyclerData<T> {
    /**
     * 过滤类型
     * FILTER_LAYOUT 根据字段不同过滤布局
     * FILTER_DATA   根据输入文本过滤条目
     */
    private final int FILTER_LAYOUT = 1;
    private final int FILTER_DATA = 2;

    private final int TAG_COEFFICIENT = 123456;
    /**
     * 列表加载适配器
     */
    private BswRecyclerAdapter<T> adapter;

    /**
     * 过滤状态，是根据数据过滤显示条目，还是根据布局标识动态显示布局
     */
    private int filterUseState = 0;

    /**
     * 页号缓存，当列表刷新时，用于判断数据添加形式
     */
    private int pageNumber;

    /**
     * 数据存储列表
     */
    private List<T> mData = new ArrayList<>();
//    /**
//     * 关键字动态过滤条目时，用于显示的数据列表
//     */
//    private List<T> mShowData = new ArrayList<>();
    /**
     * 带有tag的数据列表
     */
    private List<BswDataItem<T>> mLayoutData;
    /*-----------------------------------------动态条目过滤-------------------------------------------/
    /**
     * 关键字动态过滤条目时，用于显示的布局列表
     */
    private List<BswDataItem<T>> mShowLayoutData;
    /**
     * 数据存储列表过滤器
     */
    private Filter filter = null;
    /**
     * 条目过滤回调接口
     */
    private BswFilterDataCallBack<T> bswFilterDataCallBack;
    /**
     * 条目过滤的文本框
     */
    private EditText filterEt;

    /*-----------------------------------------动态布局过滤-------------------------------------------/

    /**
     * 动态设置布局或添加左滑右滑功能键时的过滤回调接口
     */
    private BswFilterLayoutFilter<T> bswFilterLayoutFilter;
    /**
     * 存储动态设置布局或添加左滑右滑功能键的map
     */
//    private Map<Long, BswLayoutItem> filterData;
    private SparseArray<BswLayoutItem> filterData;
    /**
     * 自定义文本过滤封装
     */
    private final MyFilterable filterable;

    public BswRecyclerData(BswRecyclerAdapter<T> adapter) {
        this.adapter = adapter;
        filterable = new MyFilterable();
    }

    /**
     * 设置数据
     *
     * @param mData      所要展示的数据列表
     * @param pageNumber 页码
     */
    public void setData(List<T> mData, @IntRange(from = 1) int pageNumber, @IntRange(from = 1) int pageSize) {
        this.mData = mData;
        if (pageNumber == 1) {
            setData(mData);
        } else if (pageNumber == this.pageNumber) {
            replaceData(mData, pageNumber, pageSize);
        } else {
            addData(mData);
        }
        this.pageNumber = pageNumber;
    }

    private void formatData(boolean isNotify) {
        startFilter(isNotify);
    }

    private void formatData() {
        formatData(true);
    }

    /**
     * 设置数据
     *
     * @param mData 需要显示的数据
     */
    public void setData(List<T> mData) {
        this.mData = mData;
        formatData();
    }

    /**
     * 设置数据
     *
     * @param mData 需要显示的数据
     */
    public void addData(List<T> mData) {
        this.mData.addAll(mData);
        formatData();
    }

    public void clearData(boolean isNotify) {
        this.mData.clear();
        formatData(isNotify);
    }

//    /**
//     * 数据替换（替换对应页码的数据）
//     * 原始数据：{1,2,3,4,5,6,7,8,9,0}
//     * pageNumber = 2，pageSize = 3，数据：{1,2,3}
//     * 结果：{1,2,3,1,2,3,7,8,9,0}
//     *
//     * @param mData 替换的数据
//     */
//    private void replaceData(List<T> mData, @IntRange(from = 1) int pageNumber, @IntRange(from = 1) int pageSize) {
//        int dataSize = Const.judgeListNull(mData);
//        int startPosition = pageSize * (pageNumber - 1);
//        for (int i = startPosition; i < startPosition + dataSize; i++) {
//            this.mData.remove(i);
//            this.mData.add(i, mData.get(i - startPosition));
//        }
//        formatData();
//    }

    /**
     * 数据替换（替换当前页码第一条以及之后的所有数据：当前缓存50条数据，pageNumber = 2；pageSize ）
     * 原始数据：{1,2,3,4,5,6,7,8,9,0}
     * pageNumber = 2，pageSize = 3，数据：{1,2,3}
     * 结果：{1,2,3,1,2,3}
     *
     * @param mData 替换的数据
     */
    private void replaceData(List<T> mData, @IntRange(from = 1) int pageNumber, @IntRange(from = 1) int pageSize) {
        int dataSize = Const.judgeListNull(this.mData);
        int startPosition = pageSize * (pageNumber - 1);
        for (int i = dataSize - 1; i >= startPosition; i--) {
            this.mData.remove(i);
        }
        this.mData.addAll(mData);
        formatData();
    }

    /**
     * 获取数据
     *
     * @return 全部数据
     */
    public List<T> getData() {
        return mData;
    }

    /**
     * 获取数据
     *
     * @return 获取数据数量
     */
    int getDataSize() {
        /*if (filterUseState == 0) {                          // 如果没有筛选调价，则直接获取基本数据数量
            return Const.judgeListNull(mData);
        } else if (filterUseState == FILTER_LAYOUT) {       // 如果只需要筛选条目布局，则获取格式化布局数据数量
            return Const.judgeListNull(mLayoutData);
        } else if (filterUseState == FILTER_DATA) {         // 如果只需要筛选数据，则获取筛选后的基本数据数量
            return Const.judgeListNull(mShowData);
        } else {                                            // 如果同时筛选数据和条目布局，则获取筛选后的格式化布局数据数量
            return Const.judgeListNull(mShowLayoutData);
        }*/
        if ((filterUseState & FILTER_LAYOUT) == 0) {
            return Const.judgeListNull(mLayoutData);
        } else {
            return Const.judgeListNull(mShowLayoutData);
        }
    }

    /**
     * 根据标识获取布局条目
     *
     * @param viewType 标识
     * @return 布局条目
     */
    BswLayoutItem getLayoutItemByTag(int viewType) {
        return filterData.get(viewType);
    }

    /**
     * 根据位置获取布局条目
     *
     * @param position 位置
     * @return 布局条目
     */
    BswLayoutItem getLayoutItemByPosition(int position) {
        BswDataItem<T> dataItem;
        if ((filterUseState & FILTER_DATA) == 0) {
            dataItem = mLayoutData.get(position);
        } else {
            dataItem = mShowLayoutData.get(position);
        }
        return filterData.get(dataItem.getTag());
    }

    BswDataItem<T> getDataItem(int position) {
        if ((filterUseState & FILTER_DATA) == 0) {
            return mLayoutData.get(position);
        } else {
            return mShowLayoutData.get(position);
        }
    }

    /**
     * 根据位置获取数据条目
     *
     * @param position 位置
     * @return 数据条目
     */
    void replaceItem(int position, T t) {
        if ((filterUseState & FILTER_DATA) == 0) {     // 如果只需要筛选数据，则获取筛选后的基本数据的对应位置项
            BswDataItem<T> dataItem = mLayoutData.get(position);
            dataItem.setT(t);
            mLayoutData.remove(position);
            mLayoutData.add(position, dataItem);
        } else {                                        // 如果同时筛选数据和条目布局，则获取筛选后的格式化布局数据的对应位置项
            BswDataItem<T> dataItem = mShowLayoutData.get(position);
            dataItem.setT(t);
            int tag = dataItem.getTag() / TAG_COEFFICIENT;
            mLayoutData.remove(tag);
            mLayoutData.add(tag, dataItem);
        }
        formatData();
    }


    /**
     * 根据输入框输入文本，动态筛选条目
     *
     * @param filterEt              用于筛选的文本输入框
     * @param bswFilterDataCallBack 筛选条件判断回调
     */
    public void setFilterEt(EditText filterEt, BswFilterDataCallBack<T> bswFilterDataCallBack) {
        this.bswFilterDataCallBack = bswFilterDataCallBack;
        this.filterEt = filterEt;
        filterUseState = filterUseState | FILTER_DATA;
        filterEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterable.setNotify(true);
                filterable.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 开始布局筛选
     *
     * @param isNotify
     */
    @SuppressLint("UseSparseArrays")
    private void startFilter(boolean isNotify) {
        // 重置数据
        filterData = new SparseArray<>();
        mLayoutData = new ArrayList<>();

        for (int i = 0; i < mData.size(); i++) {
            T t = mData.get(i);
            int tag = i * TAG_COEFFICIENT;
            if (null != bswFilterLayoutFilter) {
                BswLayoutItem layoutItem = new BswLayoutItem();
                bswFilterLayoutFilter.performFilter(t, layoutItem);
                if (layoutItem.isEdited()) {
                    layoutItem.setTag(tag);
                    filterData.put(tag, layoutItem);
                }
            }
            mLayoutData.add(new BswDataItem<>(tag, t));
        }
        if (filterUseState >= FILTER_DATA) {     // 需要根据输入框文本过滤之后刷新
            mShowLayoutData = mLayoutData;
            filterable.setNotify(isNotify);
            filterable.getFilter().filter(new TxtUtils().getString(filterEt));
        } else {                                // 不需要根据输入框文本过滤，直接刷新
            if (isNotify)
                adapter.notifyDataSetChanged();
        }
    }

    /**
     * 设置布局过滤回调
     *
     * @param bswFilterLayoutFilter 回调接口
     */
    public void setLayoutFilterCallBack(BswFilterLayoutFilter<T> bswFilterLayoutFilter) {
        filterUseState = filterUseState | FILTER_LAYOUT;
        this.bswFilterLayoutFilter = bswFilterLayoutFilter;
    }

    class MyFilterable implements Filterable {
        private boolean isNotify;

        public void setNotify(boolean notify) {
            isNotify = notify;
        }

        @Override
        public Filter getFilter() {
            if (filter == null) {
                filter = new Filter() {
                    @SuppressWarnings("unchecked")
                    @Override
                    protected void publishResults(CharSequence constraint, FilterResults results) {
//                    if (filterUseState == 0 || filterUseState == FILTER_DATA) {
//                        if (TextUtils.isEmpty(constraint)) {
//                            mShowData = mData;
//                        } else {
//                            mShowData = (List<T>) results.values;
//                        }
//                    } else {
                        if (TextUtils.isEmpty(constraint)) {
                            mShowLayoutData = mLayoutData;
                        } else {
                            mShowLayoutData = (List<BswDataItem<T>>) results.values;
                        }
//                    }
                        if (isNotify) {
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    protected FilterResults performFiltering(CharSequence constraint) {
                        FilterResults results = new FilterResults();
                        boolean isSuitable;
//                    if (filterUseState == 0 || filterUseState == FILTER_DATA) {
//                        List<T> filteredDataArray = new ArrayList<>();
//                        for (T t : mData) {
//                            isSuitable = bswFilterDataCallBack.filter(t, constraint);
//                            if (isSuitable) {
//                                filteredDataArray.add(t);
//                            }
//                        }
//
//                        if (Const.judgeListNull(filteredDataArray) == 0) {
//                            filteredDataArray = mData;
//                        }
//                        results.count = filteredDataArray.size();
//                        results.values = filteredDataArray;
//                    } else {
                        List<BswDataItem<T>> filteredLayoutArray = new ArrayList<>();
                        for (BswDataItem<T> item : mLayoutData) {
                            isSuitable = bswFilterDataCallBack.filter(item.getT(), constraint);
                            if (isSuitable) {
                                filteredLayoutArray.add(item);
                            }
                        }

                        if (Const.judgeListNull(filteredLayoutArray) == 0) {
                            filteredLayoutArray = mLayoutData;
                        }
                        results.count = filteredLayoutArray.size();
                        results.values = filteredLayoutArray;
//                    }
//                    Logger.e("VALUES", results.values.toString());
                        return results;
                    }
                };
            }
            return filter;
        }
    }
}

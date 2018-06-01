package com.banshouweng.mybaseapplication.widget.BswRecyclerView;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;

import com.banshouweng.mybaseapplication.R;
import com.banshouweng.mybaseapplication.utils.Const;
import com.banshouweng.mybaseapplication.utils.Logger;
import com.banshouweng.mybaseapplication.utils.TxtUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView适配器
 *
 * @author leiming
 * @date 2018/4/22 11:25
 */
public class BswFilterRecyclerAdapter<T extends Object> extends RecyclerView.Adapter implements Filterable {

    private final int FOOTER_TYPE = 0x9999;
    /**
     * 过滤使用的EditText
     */
    private final EditText mFilterEt;

    private Filter filter = null;

    /**
     * 请求数据列表
     */
    private List<T> mData = new ArrayList<>();
    private List<T> showData = new ArrayList<>();
    /**
     * 上下文
     */
    private Context context;
    /**
     * 布局解析
     */
    private LayoutInflater layoutInflater;
    /**
     * 布局ID
     */
    private int layoutId;
    private int[] layoutIds;
    private boolean showFooter = false;
    private int pageNumber = 1;

    public void setShowFooter() {
        showFooter = true;
        notifyDataSetChanged();
    }

    /**
     * 布局设置回调接口
     */
    private FilterConvertViewCallBack<T> filterConvertViewCallBack;

    /**
     * 显示单一布局的Adapter
     *
     * @param mData    所要展示的数据列表
     * @param context  上下文
     * @param layoutId 布局Id
     * @param callBack 布局设置回调接口
     */
    public BswFilterRecyclerAdapter(List<T> mData, Context context, int layoutId, EditText filterEt, FilterConvertViewCallBack<T> callBack) {
        this.mData = mData;
        showData = mData;
        mFilterEt = filterEt;
        filterEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        this.context = context;
        this.layoutId = layoutId;
        filterConvertViewCallBack = callBack;
        layoutInflater = LayoutInflater.from(this.context);
    }

    /**
     * 显示单一布局的Adapter
     *
     * @param context  上下文
     * @param layoutId 布局Id
     * @param callBack 布局设置回调接口
     */
    public BswFilterRecyclerAdapter(Context context, int layoutId, EditText filterEt, FilterConvertViewCallBack<T> callBack) {
        this(null, context, layoutId, filterEt, callBack);
    }

    @Override
    public int getItemViewType(int position) {
        if (showFooter && position == Const.judgeListNull(mData)) {
            return FOOTER_TYPE;
        } else {
            return super.getItemViewType(position);
        }
    }

    public T getItem(int position) {
        return showData.get(position);
    }

    /**
     * 设置数据
     *
     * @param mData 所要展示的数据列表
     */
    public void setData(List<T> mData) {
        this.mData = mData;
        showData = mData;
        getFilter().filter(TxtUtils.getText(mFilterEt));
    }

    /**
     * 设置数据
     *
     * @param mData      所要展示的数据列表
     * @param pageNumber 页码
     */
    public void setData(List<T> mData, int pageNumber) {
        this.mData = mData;
        if (pageNumber == 1) {
            setData(mData);
        } else if (pageNumber == this.pageNumber) {
            replaceData(mData);
        } else {
            addData(mData);
        }
        this.pageNumber = pageNumber;
    }

    /**
     * 设置数据
     *
     * @param mData 所要展示的数据列表
     */
    public void addData(List<T> mData) {
        this.mData.addAll(mData);
        showFooter = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 防止加载Footer无法显示
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getFilter().filter(TxtUtils.getText(mFilterEt));
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 数据替换
     *
     * @param mData 替换的数据
     */
    public void replaceData(List<T> mData) {
        int stringListSize = Const.judgeListNull(this.mData);
        for (int i = 1; i <= Const.judgeListNull(mData); i++) {
            this.mData.remove(stringListSize - i);
        }
        this.mData.addAll(mData);
        getFilter().filter(TxtUtils.getText(mFilterEt));
    }

    /**
     * 清除数据
     *
     * @param isNotify 是否刷新布局
     */
    public void clearData(boolean isNotify) {
        this.mData.clear();
        showData = mData;
        if (isNotify) {
            getFilter().filter(TxtUtils.getText(mFilterEt));
        }
    }

    /**
     * 移除数据
     *
     * @param pos 被移除数据的位置
     */
    public void removeItem(int pos) {
        this.notifyItemRemoved(pos);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (FOOTER_TYPE == viewType) {
            // inflate形式是避免内部布局宽高失效
            return new RecyclerViewHolder(context, layoutInflater.inflate(R.layout.footer_laoding_layout, parent, false));
        } else {
            // inflate形式是避免内部布局宽高失效
            return new RecyclerViewHolder(context, layoutInflater.inflate(layoutId, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position >= Const.judgeListNull(showData)) {
            return;
        }
        T t = showData.get(position);
        filterConvertViewCallBack.convert((RecyclerViewHolder) holder, t, position, showData.indexOf(t));
    }

    @Override
    public int getItemCount() {
        int mDataSize = Const.judgeListNull(showData);
        if (showFooter) {
            mDataSize++;
        }
        return mDataSize;
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new Filter() {
                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    if (TextUtils.isEmpty(constraint)) {
                        showData = mData;
                    } else {
                        showData = (List<T>) results.values;
                    }
                    notifyDataSetChanged();
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {

                    FilterResults results = new FilterResults();
                    List<T> filteredArray = filterConvertViewCallBack.filter(mData, constraint);

                    if (Const.isEmpty(filteredArray)) {
                        filteredArray = mData;
                    }
                    results.count = filteredArray.size();
                    results.values = filteredArray;
                    Logger.e("VALUES", results.values.toString());
                    return results;
                }
            };
        }
        return filter;
    }
}

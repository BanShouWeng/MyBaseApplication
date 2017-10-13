package com.banshouweng.mybaseapplication.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.banshouweng.mybaseapplication.utils.Const;

import java.util.ArrayList;
import java.util.List;

/**
 * ================================================
 * 作    者：顾修忠-guxiuzhong@youku.com/gfj19900401@163.com
 * 版    本：
 * 创建日期：16/7/8-下午6:11
 * 描    述：
 * 修订历史：
 * ================================================
 */
public abstract class BaseRecyclerAdapter<T extends BaseBean> extends RecyclerView.Adapter {
    protected final int VIEWTYPE_HEAD = 0;
    protected final int VIEWTYPE_NORMAL = 1;

    protected List<T> mData = new ArrayList<>();
    protected Context context;
    protected String status;
    protected LayoutInflater layoutInflater;
    protected boolean hasHead = false;
    protected OnItemClickListener<T> mOnItemClickListener;

    private int layoutId;

    public BaseRecyclerAdapter(List<T> mData, Context context, int layoutId) {
        this.mData = mData;
        this.context = context;
        this.layoutId = layoutId;
        layoutInflater = LayoutInflater.from(this.context);
    }

    public BaseRecyclerAdapter(Context context, int layoutId) {
        this(null, context, layoutId);
    }

    public void setData(List<T> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    public void addData(List<T> mData) {
        this.mData.addAll(mData);
        notifyDataSetChanged();
    }

    public void clearData(boolean isNotyfy) {
        this.mData.clear();
        if (isNotyfy) {
            notifyDataSetChanged();
        }
    }

    public void remove(int pos) {
        this.notifyItemRemoved(pos);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //根据数据创建右边的操作view
        return new RecyclerViewHolder(context, layoutInflater.inflate(layoutId, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        convert((RecyclerViewHolder) holder, mData.get(position));
    }

    protected abstract void convert(RecyclerViewHolder holder, T t);

    @Override
    public int getItemCount() {
        return Const.judgeListNull(mData);
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}

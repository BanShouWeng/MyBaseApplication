package com.banshouweng.mybaseapplication.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
public class BaseRecyclerAdapter<T> extends RecyclerView.Adapter {
    protected List<T> mData = new ArrayList<>();
    protected Context mContext;
    protected int layoutId;
    protected String status;
    protected LayoutInflater layoutInflater;
    protected boolean hasHead = false;
    protected static final int VIEWTYPE_HEAD = 0;
    protected static final int VIEWTYPE_NORMAL = 1;
    public boolean isCanLoadMore = true;

    public BaseRecyclerAdapter(List<T> mData, Context context) {
        this.mData = mData;
        this.mContext = context;
        layoutInflater = LayoutInflater.from(mContext);
    }

    public BaseRecyclerAdapter(List<T> mData, Context context, int layoutId) {
        this.mData = mData;
        this.mContext = context;
        this.layoutId = layoutId;
        layoutInflater = LayoutInflater.from(mContext);
    }

    public void setData(List<T> mData) {
        this.mData = mData;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //根据数据创建右边的操作view
        RecyclerViewHolder holder;
        holder = new RecyclerViewHolder(mContext, layoutInflater.inflate(layoutId, null));
        setListener(parent, holder, viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

    }

    @Override
    public int getItemCount() {
        // 因为底部布局要占一个位置，所以总数目要+1
        return mData == null ? 0 : mData.size();
    }

    public void remove(int pos) {
        this.notifyItemRemoved(pos);
    }

    protected void setListener(final ViewGroup parent, final RecyclerView.ViewHolder viewHolder, final int viewType) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    if (viewType == VIEWTYPE_HEAD && hasHead) {
                        mOnItemClickListener.onItemClick(v, viewHolder, null, position);
                    } else {
                        mOnItemClickListener.onItemClick(v, viewHolder, mData.get(hasHead ? position - 1 : position), position);
                    }

                }
            }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    return mOnItemClickListener.onItemLongClick(v, viewHolder, mData.get(hasHead ? position - 1 : position), position);
                }
                return false;
            }
        });
    }

    public OnItemClickListener<T> mOnItemClickListener;

    public interface OnItemClickListener<T> {
        void onItemClick(View view, RecyclerView.ViewHolder holder, T o, int position);

        boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, T o, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}

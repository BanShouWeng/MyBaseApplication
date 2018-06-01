package com.banshouweng.mybaseapplication.widget.BswRecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ViewHolder
 *
 * @author leiming
 * @date 2018/4/22 11:26
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;//集合类，layout里包含的View,以view的id作为key，value是view对象
    private Context mContext;//上下文对象

    public RecyclerViewHolder(Context ctx, View itemView) {
        super(itemView);
        mContext = ctx;
        mViews = new SparseArray<View>();
    }

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<View>();
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public ImageView getImageView(int viewId) {
        return getView(viewId);
    }

    public RecyclerViewHolder setText(int viewId, String value) {
        TextView view = getView(viewId);
        view.setText(value);
        return this;
    }

    public RecyclerViewHolder setVisibility(int viewId, int visibility) {
        View view = getView(viewId);
        view.setVisibility(visibility);
        return this;
    }

    public int getVisibility(int viewId, int visibility) {
        return getView(viewId).getVisibility();
    }

    /**
     * 设置文本以及色值
     *
     * @param viewId 设置文本的View的Id
     * @param color  设置的颜色 由于在Const的getStateColor方法封装了根据状态获取色值，所以这里直接使用，而不是传入色值Id了
     * @param value  设置的文字
     * @return holder
     */
    public RecyclerViewHolder setText(int viewId, int color, String value) {
        TextView view = getView(viewId);
        view.setText(value);
        view.setTextColor(color);
        return this;
    }

    public RecyclerViewHolder setImageRes(int viewId, int resId) {
        ((ImageView) getView(viewId)).setImageResource(resId);
        return this;
    }

    public RecyclerViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ((ImageView) getView(viewId)).setImageBitmap(bitmap);
        return this;
    }

    public RecyclerViewHolder setBackground(int viewId, int resId) {
        getView(viewId).setBackgroundResource(resId);
        return this;
    }

    public RecyclerViewHolder setClickListener(int viewId, View.OnClickListener listener) {
        getView(viewId).setOnClickListener(listener);
        return this;
    }
}

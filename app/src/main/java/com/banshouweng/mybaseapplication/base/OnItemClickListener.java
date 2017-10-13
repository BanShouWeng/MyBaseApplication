package com.banshouweng.mybaseapplication.base;

import android.view.View;

/**
 * Created by leiming on 2017/10/13.
 */

public interface OnItemClickListener<T> {
    void onItemClick(View view, RecyclerViewHolder holder, T o, int position);

    boolean onItemLongClick(View view, RecyclerViewHolder holder, T o, int position);
}

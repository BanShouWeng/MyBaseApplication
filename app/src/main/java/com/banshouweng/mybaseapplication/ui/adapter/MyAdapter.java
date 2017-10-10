package com.banshouweng.mybaseapplication.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by dell on 2017/8/5.
 */

public class MyAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter {

    private Context context;
    private int layoutId;

    public MyAdapter(int layoutId, Context context) {
        this.context = context;
        this.layoutId = layoutId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(LayoutInflater.from(context
        ).inflate(layoutId, parent,
                false)) {
            @Override
            public String toString() {
                return super.toString();
            }
        };
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}

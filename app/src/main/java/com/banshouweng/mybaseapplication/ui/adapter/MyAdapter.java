package com.banshouweng.mybaseapplication.ui.adapter;

import android.content.Context;

import com.banshouweng.mybaseapplication.R;
import com.banshouweng.mybaseapplication.base.BaseRecyclerAdapter;
import com.banshouweng.mybaseapplication.base.RecyclerViewHolder;
import com.banshouweng.mybaseapplication.bean.SubjectsBean;
import com.banshouweng.mybaseapplication.utils.GlideUtils;

import java.util.List;

/**
 * Created by dell on 2017/8/5.
 */

public class MyAdapter extends BaseRecyclerAdapter<SubjectsBean> {

    public MyAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void setData(List<SubjectsBean> mData) {
        super.setData(mData);
        notifyDataSetChanged();
    }

    @Override
    protected void convert(RecyclerViewHolder holder, SubjectsBean subjectsBean) {
        holder.setText(R.id.doubantext, subjectsBean.getTitle());
        GlideUtils.loadImageView(context, subjectsBean.getImages().getMedium(), holder.getImageView(R.id.doubanImg));
    }
}

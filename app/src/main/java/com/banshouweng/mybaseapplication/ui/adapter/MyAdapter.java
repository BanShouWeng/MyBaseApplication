//package com.banshouweng.mybaseapplication.ui.adapter;
//
//import android.content.Context;
//
//import com.banshouweng.mybaseapplication.R;
//import com.banshouweng.mybaseapplication.base.BaseBean;
//import com.banshouweng.mybaseapplication.base.BaseRecyclerAdapter;
//import com.banshouweng.mybaseapplication.base.RecyclerViewHolder;
//import com.banshouweng.mybaseapplication.bean.SubjectsBean;
//import com.banshouweng.mybaseapplication.utils.GlideUtils;
//import com.banshouweng.mybaseapplication.widget.MyRecyclerView.ConvertViewCallBack;
//
//import java.util.List;
//
///**
// * Created by dell on 2017/8/5.
// */
//
//public class MyAdapter extends BaseRecyclerAdapter<SubjectsBean> {
//
//    public MyAdapter(Context context, int layoutId) {
//        super(context, layoutId);
//    }
//
//    public MyAdapter(List<SubjectsBean> mData, Context context, int layoutId) {
//        super(mData, context, layoutId);
//    }
//
//    public <T extends BaseBean> MyAdapter(Context context, int layoutId, ConvertViewCallBack<T> callBack) {
//        super(context, layoutId, callBack);
//    }
//
//    @Override
//    public void setData(List<SubjectsBean> mData) {
//        super.setData(mData);
//        notifyDataSetChanged();
//    }
//}

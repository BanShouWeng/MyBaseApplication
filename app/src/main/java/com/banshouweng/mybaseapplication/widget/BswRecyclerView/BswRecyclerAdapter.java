package com.banshouweng.mybaseapplication.widget.BswRecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.banshouweng.mybaseapplication.R;
import com.banshouweng.mybaseapplication.utils.Const;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView适配器
 *
 * @author leiming
 * @date 2018/4/22 11:25
 */
public class BswRecyclerAdapter<T extends Object> extends RecyclerView.Adapter {

    private final int FOOTER_TYPE = 0x9999;

    /**
     * 请求数据列表
     */
    private List<T> mData = new ArrayList<>();
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
    /**
     * 分页页码，用于判断是加载数据，还是替换数据
     */
    private int pageNumber = 1;

    public void setShowFooter() {
        showFooter = true;
        notifyDataSetChanged();
    }

    /**
     * 布局设置回调接口
     */
    private ConvertViewCallBack<T> convertViewCallBack;

    /**
     * 布局复用回调接口
     */
    private MultiplexAdapterCallBack<T> multiplexAdapterCallBack;

    /**
     * 显示单一布局的Adapter
     *
     * @param mData    所要展示的数据列表
     * @param context  上下文
     * @param layoutId 布局Id
     * @param callBack 布局设置回调接口
     */
    public BswRecyclerAdapter(List<T> mData, Context context, int layoutId, ConvertViewCallBack<T> callBack) {
        this.mData = mData;
        this.context = context;
        this.layoutId = layoutId;
        convertViewCallBack = callBack;
        layoutInflater = LayoutInflater.from(this.context);
    }

    /**
     * 显示单一布局的Adapter
     *
     * @param context  上下文
     * @param layoutId 布局Id
     * @param callBack 布局设置回调接口
     */
    public BswRecyclerAdapter(Context context, int layoutId, ConvertViewCallBack<T> callBack) {
        this(null, context, layoutId, callBack);
    }

    /**
     * 显示复用布局的Adapter
     *
     * @param context                  上下文
     * @param multiplexAdapterCallBack 复用布局回调接口
     */
    public BswRecyclerAdapter(Context context, MultiplexAdapterCallBack<T> multiplexAdapterCallBack, int... layoutIds) {
        this(null, context, multiplexAdapterCallBack, layoutIds);
    }

    /**
     * 显示复用布局的Adapter
     *
     * @param mData                    所要展示的数据列表
     * @param context                  上下文
     * @param multiplexAdapterCallBack 复用布局设置回调接口
     */
    public BswRecyclerAdapter(List<T> mData, Context context, MultiplexAdapterCallBack<T> multiplexAdapterCallBack, int... layoutIds) {
        this.mData = mData;
        this.context = context;
        this.layoutIds = layoutIds;
        this.multiplexAdapterCallBack = multiplexAdapterCallBack;
        layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getItemViewType(int position) {
        if (showFooter && position == Const.judgeListNull(mData)) {
            return FOOTER_TYPE;
        }
        if (multiplexAdapterCallBack == null) {
            return super.getItemViewType(position);
        } else {
            return multiplexAdapterCallBack.getItemViewType(position);
        }
    }

    /**
     * 设置数据
     *
     * @param mData 所要展示的数据列表
     */
    public void setData(List<T> mData) {
        this.mData = mData;
        notifyDataSetChanged();
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
     * 添加数据
     *
     * @param mData 所添加的数据列表,刷新放在Footer进行
     */
    public void addData(List<T> mData) {
        this.mData.addAll(mData);
        showFooter = false;
        // 网络请求快时，footer无法显示
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            notifyDataSetChanged();
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
        int stringListSize = getItemCount();
        for (int i = 1; i <= Const.judgeListNull(mData); i++) {
            this.mData.remove(stringListSize - i);
        }
        this.mData.addAll(mData);
        notifyDataSetChanged();
    }

    /**
     * 清除数据
     *
     * @param isNotify 是否刷新布局
     */
    public void clearData(boolean isNotify) {
        this.mData.clear();
        if (isNotify) {
            notifyDataSetChanged();
        }
    }

    public T getItem(int position) {
        return mData.get(position);
    }

    public List<T> getData() {
        return mData;
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
    @SuppressLint("InflateParams")
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (FOOTER_TYPE == viewType) {
            // inflate形式是避免内部布局宽高失效
            return new RecyclerViewHolder(context, layoutInflater.inflate(R.layout.footer_laoding_layout, parent, false));
        }
        if (multiplexAdapterCallBack == null) {
            // inflate形式是避免内部布局宽高失效
            return new RecyclerViewHolder(context, layoutInflater.inflate(layoutId, parent, false));
        } else {
            // inflate形式是避免内部布局宽高失效
            return new RecyclerViewHolder(context, layoutInflater.inflate(multiplexAdapterCallBack.onCreateHolder(viewType, layoutIds), parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position >= Const.judgeListNull(mData)) {
            return;
        }
        if (multiplexAdapterCallBack == null) {
            convertViewCallBack.convert((RecyclerViewHolder) holder, mData.get(position), position);
        } else {
            multiplexAdapterCallBack.convert((RecyclerViewHolder) holder, mData.get(position), position);
        }
    }


    @Override
    public int getItemCount() {
        int mDataSize = Const.judgeListNull(mData);
        if (showFooter) {
            mDataSize++;
        }
        return mDataSize;
    }
}

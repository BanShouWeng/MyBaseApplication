package com.banshouweng.mybaseapplication.widget.BswRecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
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
public class BswRecyclerAdapter<T> extends RecyclerView.Adapter {

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
    @LayoutRes
    private int layoutId;

    @LayoutRes
    private int[] layoutIds;

    private boolean showFooter = false;
    /**
     * 分页页码，用于判断是加载数据，还是替换数据
     */
    private int pageNumber = 1;

    /**
     * 最大显示数量，默认-1，表示不限制
     */
    private int maxCount = - 1;

    void setShowFooter() {
        showFooter = true;
        notifyDataSetChanged();
    }

    void removeFooter() {
        showFooter = false;
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
    private BswRecyclerAdapter(List<T> mData, Context context, @LayoutRes int layoutId, ConvertViewCallBack<T> callBack) {
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
    BswRecyclerAdapter(Context context, MultiplexAdapterCallBack<T> multiplexAdapterCallBack, int... layoutIds) {
        this(null, context, multiplexAdapterCallBack, layoutIds);
    }

    /**
     * 显示复用布局的Adapter
     *
     * @param mData                    所要展示的数据列表
     * @param context                  上下文
     * @param multiplexAdapterCallBack 复用布局设置回调接口
     */
    private BswRecyclerAdapter(List<T> mData, Context context, MultiplexAdapterCallBack<T> multiplexAdapterCallBack, @LayoutRes int... layoutIds) {
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

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
        notifyDataSetChanged();
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
    public void setData(List<T> mData, int pageNumber, int pageSize) {
        if (pageNumber == 1) {
            setData(mData);
        } else if (pageNumber == this.pageNumber) {
            replaceData(mData, pageNumber, pageSize);
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
    void addData(List<T> mData) {
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
    private void replaceData(List<T> mData, int pageNumber, int pageSize) {
        int dataSize = Const.judgeListNull(mData);
        if (dataSize < pageNumber) {
            int allDataSize = Const.judgeListNull(this.mData);
            for (int i = mData.size(); i < allDataSize - dataSize; i--) {
                this.mData.remove(i);
            }
            this.mData.addAll(mData);
        } else {
            int startPosition = pageSize * (pageNumber - 1);
            for (int i = startPosition; i < startPosition + dataSize; i++) {
                this.mData.remove(i);
                this.mData.add(i, mData.get(i - startPosition));
            }
        }
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
     * 清除数据
     *
     * @param isNotify 是否刷新布局
     */
    void clearData(boolean isNotify) {
        if (Const.notEmpty(mData)) {
            mData.clear();
            if (isNotify) {
                notifyDataSetChanged();
            }
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
    void removeItem(int pos) {
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
            mData.set(position, convertViewCallBack.convert((RecyclerViewHolder) holder, mData.get(position), position));
        } else {
            mData.set(position, multiplexAdapterCallBack.convert((RecyclerViewHolder) holder, mData.get(position), position));
        }
    }


    @Override
    public int getItemCount() {
        int mDataSize = Const.judgeListNull(mData);
        if (showFooter) {
            mDataSize++;
        }
        int s = maxCount < 0 ? mDataSize : maxCount < mDataSize ? maxCount : mDataSize;
        return s;
    }
}

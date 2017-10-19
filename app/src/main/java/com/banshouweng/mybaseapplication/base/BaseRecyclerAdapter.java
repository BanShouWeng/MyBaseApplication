package com.banshouweng.mybaseapplication.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.banshouweng.mybaseapplication.utils.Const;
import com.banshouweng.mybaseapplication.widget.MyRecyclerView.ConvertViewCallBack;
import com.banshouweng.mybaseapplication.widget.MyRecyclerView.MultiplexAdapterCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
 */
public class BaseRecyclerAdapter<T extends BaseBean> extends RecyclerView.Adapter {

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
    private boolean hasHead = false;

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
    public BaseRecyclerAdapter(List<T> mData, Context context, int layoutId, ConvertViewCallBack<T> callBack) {
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
    public BaseRecyclerAdapter(Context context, int layoutId, ConvertViewCallBack<T> callBack) {
        this(null, context, layoutId, callBack);
    }

    /**
     * 显示复用布局的Adapter
     *
     * @param context                  上下文
     * @param multiplexAdapterCallBack 复用布局回调接口
     */
    public BaseRecyclerAdapter(Context context, MultiplexAdapterCallBack<T> multiplexAdapterCallBack, int... layoutIds) {
        this(null, context, multiplexAdapterCallBack, layoutIds);
    }

    /**
     * 显示复用布局的Adapter
     *
     * @param mData                    所要展示的数据列表
     * @param context                  上下文
     * @param multiplexAdapterCallBack 复用布局设置回调接口
     */
    public BaseRecyclerAdapter(List<T> mData, Context context, MultiplexAdapterCallBack<T> multiplexAdapterCallBack, int... layoutIds) {
        this.mData = mData;
        this.context = context;
        this.layoutIds = layoutIds;
        this.multiplexAdapterCallBack = multiplexAdapterCallBack;
        layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getItemViewType(int position) {
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
     * 添加数据
     *
     * @param mData 所添加的数据列表
     */
    public void addData(List<T> mData) {
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

    /**
     * 移除数据
     *
     * @param pos 被移除数据的位置
     */
    public void removeItem(int pos) {
        this.notifyItemRemoved(pos);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //根据数据创建右边的操作view
        if (multiplexAdapterCallBack == null) {
            return new RecyclerViewHolder(context, layoutInflater.inflate(layoutId, null));
        } else {
            return new RecyclerViewHolder(context, layoutInflater.inflate(multiplexAdapterCallBack.onCreateHolder(viewType, layoutIds), null));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (multiplexAdapterCallBack == null) {
            convertViewCallBack.convert((RecyclerViewHolder) holder, mData.get(position), position);
        } else {
            multiplexAdapterCallBack.convert((RecyclerViewHolder) holder, mData.get(position), position);
        }
    }


    @Override
    public int getItemCount() {
        return Const.judgeListNull(mData);
    }
}

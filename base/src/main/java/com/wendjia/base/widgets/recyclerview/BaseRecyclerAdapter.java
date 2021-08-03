package com.wendjia.base.widgets.recyclerview;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author lxm
 * @date 2018/5/24/024
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseViewHolder<T>> {

    /**
     * data为空时显示的视图
     */
    private View mEmptyView;

    private List<T> mData = new ArrayList<>();

    protected BaseRecyclerItemViewClickListener<T> mRecyclerItemViewClickListener;

    public BaseRecyclerAdapter() {
        registerAdapterDataObserver(emptyObserver);
    }

    public BaseRecyclerAdapter(T[] data) {
        this(Arrays.asList(data));
    }

    public BaseRecyclerAdapter(List<T> data) {
        this.mData = data;
        registerAdapterDataObserver(emptyObserver);
    }


    @Override
    @NonNull
    public BaseViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseViewHolder<T> baseViewHolder = onCreateBaseViewHolder(parent, viewType);
        if (mRecyclerItemViewClickListener != null) {
            baseViewHolder.setOnItemViewClickListener(mRecyclerItemViewClickListener);
        }
        return baseViewHolder;
    }

    public abstract BaseViewHolder<T> onCreateBaseViewHolder(@NonNull ViewGroup parent,
            int viewType);

    /* ViewHolder 绑定数据，这里的 position 和 getItemViewType() 方法的 position 不一样
        这里的 position 指当前可见的 item 的 position 的位置。
        注意 ：每个 ViewHolder 绑定数据时值调用此方法一次
     */
    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<T> holder, int position) {
        holder.setData(mData.get(position), position,position == mData.size() - 1);
    }

    /**
     * 设置数据
     *
     * @param data 数据
     * @param needNotify 是否需要更新视图
     */
    public void setData(List<T> data, boolean needNotify) {
        this.mData = data;
        if (needNotify) {
            notifyDataSetChanged();
        }
    }

    /**
     * 设置数据
     *
     * @param data 数据
     */
    public void setData(List<T> data) {
        setData(data, false);
    }

    public void setData(T[] data) {
        setData(Arrays.asList(data));
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void add(T object) {
        if (mData != null) {
            mData.add(object);
            notifyItemInserted(mData.size() - 1);
        }
    }

    public void insert(T object, int itemPosition) {
        if (mData != null && itemPosition <= mData.size()) {
            if (itemPosition == mData.size()) {
                mData.add(object);
            } else {
                mData.add(itemPosition, object);
            }
            notifyItemInserted(itemPosition);
        }
    }

    public void addAll(List<? extends T> data) {
        addAll(data, true);
    }

    public void addAll(List<? extends T> data, Boolean notify) {
        int size = data.size();
        if (size > 0) {
            int positionStart = mData.size();
            mData.addAll(data);
            if (notify){
                notifyItemRangeInserted(positionStart, size);
            }
        }
    }

    public void addAll(T[] objects) {
        addAll(Arrays.asList(objects));
    }

    public void replace(T object, int itemPosition) {
        if (mData != null) {
            int dataPosition;
            dataPosition = itemPosition;
            if (dataPosition > -1 && dataPosition < mData.size()) {
                mData.set(dataPosition, object);
                notifyItemChanged(itemPosition);
            }
        }
    }

    //position start with 0
    public void remove(T object) {
        if (!mData.contains(object)) {
            log("remove()  without the object : " + object.getClass().getName());
            return;
        }
        int dataPosition = mData.indexOf(object);
        remove(dataPosition);
    }

    //positionItem start with 0
    public void remove(int itemPosition) {
        int dataSize = mData.size();
        if (itemPosition >= 0 && itemPosition < dataSize) {
            mData.remove(itemPosition);
            notifyItemRemoved(itemPosition);
        }
    }

    /**
     * 移除一系列数据
     *
     * @param data 数据序列
     * @return 移除操作是否成功
     */
    public boolean removeAll(List<T> data) {
        try {
            int positionStart = mData.indexOf(data.get(0));
            mData.removeAll(data);
            notifyItemRangeRemoved(positionStart, data.size());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void clear() {
        if (mData == null || mData.isEmpty()) {
            return;
        }
        mData.clear();
        notifyDataSetChanged();
    }

    public List<T> getData() {
        return mData;
    }


    public void setOnItemViewClickListener(BaseRecyclerItemViewClickListener<T>
            itemViewClickListener) {
        this.mRecyclerItemViewClickListener = itemViewClickListener;
    }

    /**
     * data是否是空的
     */
    public boolean isEmpty() {
        return mData == null || mData.isEmpty();
    }

    /**
     * 监听列表数据的变化判断是否显示 emptyView
     */
    private final RecyclerView.AdapterDataObserver emptyObserver = new RecyclerView
            .AdapterDataObserver() {
        @Override
        public void onChanged() {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkIfEmpty();
        }

    };

    /**
     * 设置适配器为空时显示的视图
     *
     * @param emptyView 适配器为空时显示的视图
     */
    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
        if (emptyView != null
                && emptyView.getImportantForAccessibility() ==
                View.IMPORTANT_FOR_ACCESSIBILITY_AUTO) {
            emptyView.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_YES);
        }
        checkIfEmpty();
    }

    private void checkIfEmpty() {
        if (mEmptyView != null) {
            mEmptyView.setVisibility(isEmpty() ? View.VISIBLE : View.GONE);
        }
    }


    public void log(String content) {
        LogUtils.iTag("BaseRecyclerAdapter" + content);
    }
}

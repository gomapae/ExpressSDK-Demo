package com.wendjia.base.widgets.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

/**
 * Create by lxm
 * 2020/8/25
 */
open class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView), LayoutContainer {

    /**
     * 点击监听
     */
    protected var mRecyclerItemViewClickListener: BaseRecyclerItemViewClickListener<T>? = null

    private var mData: T? = null
    private var isLast = false

    /**
     * 当删除item 后，itemPosition 不会更新 ，请使用 layoutPosition
     */
    private var itemPosition:Int ?= 0

    constructor(parent: ViewGroup, layoutId: Int) : this(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))

    init {

        itemView.setOnClickListener { v ->
            if (mRecyclerItemViewClickListener != null) {
                mRecyclerItemViewClickListener?.onItemViewClick(mData, v, layoutPosition)
            }
            onItemViewClick(mData)
        }
    }

    open fun setOnItemViewClickListener(mRecyclerItemViewClickListener: BaseRecyclerItemViewClickListener<T>?) {
        this.mRecyclerItemViewClickListener = mRecyclerItemViewClickListener
    }

    /**
     * 绑定数据
     *
     * @param data 当前需要绑定的数据
     */
    open fun bindData(@NonNull data: T) {}

    /**
     * 绑定数据
     *
     * @param data 当前需要绑定的数据
     */
    open fun bindData(@NonNull data: T, position: Int) {
        bindData(data)
    }

    open fun bindData(@NonNull data: T, position: Int,isLast:Boolean) {
        bindData(data,position)
    }

    open fun setData(data: T?, position: Int) {
        if (data != null) {
            mData = data
            itemPosition = position
            bindData(data, position)
        }
    }
    open fun setData(data: T?, position: Int,isLast:Boolean) {
        if (data != null) {
            mData = data
            this.isLast = isLast
            itemPosition = position
            bindData(data, position,isLast)
        }
    }

    open fun getData(): T? {
        return mData
    }
    fun isLast():Boolean{
        return isLast
    }
    fun getViewPosition():Int{
        return itemPosition?:0
    }


    open fun onItemViewClick(data: T?) {}
    override val containerView: View?
        get() = itemView

}
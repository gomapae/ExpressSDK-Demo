package com.wendjia.base.widgets.recyclerview

import android.view.ViewGroup

/**
 * Create by lxm
 * 2020/8/25
 */
interface ViewHolderCreator<T> {
    /**
     * 位置[position]上的元素[item]是否匹配本代理
     *
     * @param item    元素
     * @param position 元素位置
     * @return true:匹配，false:不匹配
     */
    fun isForViewType(item: Any, position: Int): Boolean

    /**
     * 生成对应的ViewHolder
     */
    fun onCreateBaseViewHolder(parent: ViewGroup): BaseViewHolder<T>

}
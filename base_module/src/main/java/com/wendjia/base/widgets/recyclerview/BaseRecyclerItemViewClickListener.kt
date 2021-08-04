package com.wendjia.base.widgets.recyclerview

import android.view.View
/**
 * Create by lxm
 * 2020/8/25
 */
interface BaseRecyclerItemViewClickListener<T> {

    /**
     * item被点击的回调
     *
     * @param data        被点击的视图对应的数据
     * @param clickedView 被点击的视图
     * @param position    被点击的位置
     */
    fun onItemViewClick(data: T?, clickedView: View?, position: Int)

}
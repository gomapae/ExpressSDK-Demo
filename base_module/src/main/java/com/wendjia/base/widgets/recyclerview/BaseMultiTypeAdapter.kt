package com.wendjia.base.widgets.recyclerview

import android.view.ViewGroup

/**
 * Create by lxm
 * 2020/8/25
 */
abstract class BaseMultiTypeAdapter : BaseRecyclerAdapter<Any> {

    val manager = ViewHolderCreatorManager()

    constructor() : super()
    constructor(data: List<Any>) : super(data)

    override fun getItemViewType(position: Int): Int {
        return manager.getItemViewType(data, position)
    }


    override fun onCreateBaseViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Any> {
        return manager.onCreateViewHolder(parent, viewType) as BaseViewHolder<Any>
    }


}
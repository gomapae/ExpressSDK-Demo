package com.gomap.maps.demo.routing

import android.view.ViewGroup
import com.wendjia.base.bean.SearchBean
import com.wendjia.base.widgets.recyclerview.BaseRecyclerAdapter
import com.wendjia.base.widgets.recyclerview.BaseViewHolder

/**
 * @author lxm
 * @createtime 2021/8/2
 */
class StopPointListAdapter:BaseRecyclerAdapter<SearchBean>() {

    override fun onCreateBaseViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<SearchBean> {
        return StopPointListViewHolder(parent)
    }
}
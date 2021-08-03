package com.gomap.maps.demo.routing

import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.gomap.maps.demo.R
import com.gomap.maps.demo.databinding.StopPointItemViewBinding
import com.wendjia.base.bean.SearchBean
import com.wendjia.base.widgets.recyclerview.BaseViewHolder

/**
 * @author lxm
 * @createtime 2021/8/2
 */
class StopPointListViewHolder(parent:ViewGroup):BaseViewHolder<SearchBean>(parent, R.layout.stop_point_item_view) {

    override fun bindData(data: SearchBean) {
        super.bindData(data)
        val bind = DataBindingUtil.bind<StopPointItemViewBinding>(itemView)
        bind?.stopPointItemViewTxtName?.text = data.name +"(" +data.lat +"," + data.lng+")"

    }
}
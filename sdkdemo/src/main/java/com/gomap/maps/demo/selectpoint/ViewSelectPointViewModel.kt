package com.gomap.maps.demo.selectpoint

import androidx.databinding.ObservableField
import com.wendjia.base.bean.SearchBean
import com.wendjia.base.viewbinding.BaseViewModel

class ViewSelectPointViewModel : BaseViewModel() {

    val onclickable = ObservableField(false)

    val data: ObservableField<SearchBean> = ObservableField()


}

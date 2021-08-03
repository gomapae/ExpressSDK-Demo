package com.wendjia.base.extention

import android.content.Context
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wendjia.base.R

/**
 * 垂直带横线
 */
fun RecyclerView.setRvLinearV(mContext: Context) {
    setHasFixedSize(true)
    layoutManager = LinearLayoutManager(mContext)
    isMotionEventSplittingEnabled = false
//    addItemDecoration(DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL).apply {
//        setDrawable(mContext.getDrawable(R.drawable.rv_line)!!)
//    })
}
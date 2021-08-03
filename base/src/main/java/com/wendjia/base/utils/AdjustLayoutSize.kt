package com.wendjia.base.utils

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver

/**
 * Create by lxm
 * 2020/9/1
 */
class AdjustLayoutSize{

    private var mChildOfContent: View? = null
    private var usableHeightPrevious = 0
    private var frameLayoutParams: ViewGroup.LayoutParams? = null

    companion object {
        fun assistActivity(content: View):AdjustLayoutSize {
          return AdjustLayoutSize(content)
        }
    }

    constructor(content: View?){
        if (content != null){
            mChildOfContent?.viewTreeObserver?.addOnGlobalLayoutListener {
                possiblyResizeChildOfContent()
            }
            frameLayoutParams = mChildOfContent?.layoutParams
        }
    }

    fun onDestroy() {
        mChildOfContent = null
    }

    private fun possiblyResizeChildOfContent() {
        val usableHeightNow = computeUsableHeight()
        if (usableHeightNow != usableHeightPrevious) {
            //如果两次高度不一致
            //将计算的可视高度设置成视图的高度
            frameLayoutParams?.height = usableHeightNow
            mChildOfContent?.requestLayout() //请求重新布局
            usableHeightPrevious = usableHeightNow
        }
    }

    private fun computeUsableHeight(): Int {
        //计算视图可视高度
        val r = Rect()
        mChildOfContent?.getWindowVisibleDisplayFrame(r)
        return r.bottom
    }

}
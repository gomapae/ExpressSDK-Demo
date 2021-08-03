package com.wendjia.base.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

/**
 * 基础swipeRefreshLayout
 *
 */
class BaseSwipeRefreshLayout : SwipeRefreshLayout {
    private val SCROLL_MODE_IDLE = 0
    private val SCROLL_MODE_VERTICAL = 1
    private val SCROLL_MODE_HORIZONTAL = 2

    private var scrollMode = SCROLL_MODE_IDLE

    private var mInitialDownX: Float = 0.toFloat()
    private var mInitialDownY: Float = 0.toFloat()
    private val mTouchSlop: Int = 10

    private var saveEnableState = isEnabled
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    init {
        setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {

        val action = ev?.action
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                mInitialDownX = ev.x
                mInitialDownY = ev.y

                saveEnableState = isEnabled
                scrollMode = SCROLL_MODE_IDLE
            }
            MotionEvent.ACTION_UP->{
                this.isEnabled = saveEnableState
            }
            MotionEvent.ACTION_MOVE -> {

                if (scrollMode == SCROLL_MODE_IDLE) {
                    val horizontalSpace = Math.abs(ev.x - mInitialDownX)
                    val verticalSpace = Math.abs(ev.y - mInitialDownY)


                    if (horizontalSpace > verticalSpace && horizontalSpace > mTouchSlop) {
                        scrollMode = SCROLL_MODE_HORIZONTAL
                        this.isEnabled = false
                    } else if (verticalSpace > horizontalSpace && verticalSpace > mTouchSlop) {
                        scrollMode = SCROLL_MODE_VERTICAL
                    }
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }
}

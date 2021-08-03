package com.gomap.base.viewtask

import android.content.Intent
import android.os.Bundle

/**
 * @description
 * @author shenfei.wang@g42.ai
 * @createtime 2021/1/20 15:36
 */
interface IView {

    /**
     * @Author:        tiancheng.zhang@g42.ai
     * @params:        isAnimaton 是否需要动画
     * @retrun:
     * @Description:
     */
    fun show(isAnimation: Boolean)

    fun hide(isAnimation: Boolean,hide:(isFinish:Boolean)->Unit)

    /**
     * main activity onActivityResult 回调
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

    /**
     * 打开view的回调
     */
    fun onViewResult(data: Intent?)

    fun onCreate()
    fun onReStart()
    fun onResume()
    fun onPause()
    fun onStop()
    fun onDestroy()

    fun onBackPressed()

    /**
     * view 显示数量 对比activity 管理栈
     */
    fun viewShowNum():Int

    fun setBundleData(bundle: Bundle)

}
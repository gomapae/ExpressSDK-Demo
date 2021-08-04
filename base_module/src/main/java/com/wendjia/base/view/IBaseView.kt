package com.wendjia.base.view

import android.content.Context
import androidx.annotation.NonNull
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner

/**
 * Create by lxm
 * 2020/8/24
 */
interface IBaseView {

    /**
     * 初始化数据
     */
    fun initData()

    /**
     * 初始化view
     */
    fun initUIView()

    /**
     * 请求数据
     */
    fun fetchData()

    /**
     * 显示错误信息
     *
     * @param message 信息
     */
    fun showErrorMessage(message: String?)

    /**
     * 显示加载提示视图
     */
    fun showLoadingView()

    /**
     * 隐藏加载视图
     */
    fun hideLoadingView()

    /**
     * 视图是否还活跃
     */
    fun isAlive(): Boolean

    /**
     * 获取context
     *
     * @return 获取当前的context
     */
    @NonNull
    fun getContext(): Context?

    /**
     * 获取IBaseView
     *
     * @return 获取当前的IBaseView
     */
    fun getIBaseView(): IBaseView

    fun getViewModelStoreOwner(): ViewModelStoreOwner
    fun getLifecycleOwner(): LifecycleOwner

}
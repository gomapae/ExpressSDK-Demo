package com.wendjia.base.utils

import android.os.Looper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Create by lxm
 * 2020/8/24
 */
object ThreadUtils {
    /**
     * 主线程执行
     *
     * @param runnable 任务
     */
    fun runOnUi(runnable: Runnable) {
        runOnUi(runnable, 0L, TimeUnit.MILLISECONDS)
    }

    /**
     * 主线程执行
     *
     * @param runnable 任务
     * @param delay    延迟时间
     * @param timeUnit 时间类型
     */
    fun runOnUi(runnable: Runnable, delay: Long, timeUnit: TimeUnit?) {
        if (delay <= 0 && Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run()
        } else {
            AndroidSchedulers.mainThread().scheduleDirect(runnable, delay, timeUnit!!)
        }
    }

    /**
     * 在子线程执行
     *
     * @param runnable 任务
     */
    fun runOnIO(runnable: Runnable?) {
        runOnIO(runnable, 0L, TimeUnit.MILLISECONDS)
    }

    /**
     * 在子线程执行
     *
     * @param runnable 任务
     * @param delay    延迟时间
     * @param timeUnit 时间类型
     */
    fun runOnIO(runnable: Runnable?, delay: Long?, timeUnit: TimeUnit?) {
        runnable?.let {
            Schedulers.io().scheduleDirect(it, delay!!, timeUnit!!)
        }

    }

    /**
     * 工作线程中执行
     * @param runnable 任务
     * @param delay 延时执行事件
     */
    fun runOnNewThread(runnable: Runnable, delay: Long) {
        Schedulers.newThread().scheduleDirect(runnable, delay, TimeUnit.MILLISECONDS)
    }
}
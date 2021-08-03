package com.gomap.maps.demo

import android.os.Process
import android.text.TextUtils
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.Utils
import com.mapzen.android.GoMapSDK
import com.viewrouter.api.ViewRouter
import com.wendjia.base.BaseApplication
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

/**
 * @author lxm
 * @createtime 2021/8/2
 */
class DemoMainApplication:BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        val processName = getProcessName(Process.myPid())
        if (processName != this.packageName) { //防止多进程导致初始化多次
            return
        }
        ViewRouter.init(this)
        GoMapSDK.getInstance().init()
        ARouter.init(this)

        Utils.init(this)
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
    }

    /**
     * 获取当前进程名
     *
     * @param pid
     * @return
     */
    private fun getProcessName(pid: Int): String? {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
            var processName = reader.readLine()
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim { it <= ' ' }
            }
            return processName
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        } finally {
            try {
                reader?.close()
            } catch (exception: IOException) {
                exception.printStackTrace()
            }
        }
        return null
    }

}
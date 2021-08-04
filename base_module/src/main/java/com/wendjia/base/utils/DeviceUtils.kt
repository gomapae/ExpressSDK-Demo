package com.wendjia.base.utils

import android.os.Build
import android.os.Environment
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.view.WindowManager
import java.io.File
import java.io.FileInputStream
import java.lang.reflect.Method
import java.util.*

/**
 * Create by lxm
 * 2020/9/1
 */
object DeviceUtils {
    private const val KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name"
    private const val KEY_FLYME_VERSION_NAME = "ro.build.display.id"
    private const val FLYME = "flyme"
    private var sMiuiVersionName: String? = null
    private var sFlymeVersionName: String? = null
    init {
        var fileInputStream: FileInputStream? = null
        try {
            fileInputStream = FileInputStream(File(Environment.getRootDirectory(), "build.prop"))
            val properties = Properties()
            properties.load(fileInputStream)
            val clzSystemProperties = Class.forName("android.os.SystemProperties")
            val getMethod = clzSystemProperties.getDeclaredMethod("get", String::class.java)
            // miui
            sMiuiVersionName = getLowerCaseName(properties, getMethod, KEY_MIUI_VERSION_NAME)
            //flyme
            sFlymeVersionName = getLowerCaseName(properties, getMethod, KEY_FLYME_VERSION_NAME)
        } catch (e: java.lang.Exception) {
        } finally {
            try {
                fileInputStream?.close()
            } catch (e: java.lang.Exception) {
            }
        }
    }
    /**
     * 判断是否是flyme系统
     */
    fun isFlyme(): Boolean {
        return !TextUtils.isEmpty(sFlymeVersionName) && sFlymeVersionName?.contains(FLYME) == true
    }

    /**
     * 判断是否是MIUI系统
     */
    fun isMIUI(): Boolean {
        return !TextUtils.isEmpty(sMiuiVersionName)
    }
    private fun getLowerCaseName(p: Properties, get: Method, key: String): String? {
        var name = p.getProperty(key)
        if (name == null) {
            try {
                name = get.invoke(null, key) as String
            } catch (ignored: java.lang.Exception) {
            }
        }
        if (name != null) {
            name = name.toLowerCase()
        }
        return name
    }


    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为 Flyme 用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     *
     * @return boolean 成功执行返回true
     */
    fun FlymeSetStatusBarLightMode(window: Window?, dark: Boolean): Boolean {
        var result = false
        if (isFlyme() && Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT <= 23) {
            if (window != null) {
                try {
                    val lp = window.attributes
                    val darkFlag = WindowManager.LayoutParams::class.java
                            .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                    val meizuFlags = WindowManager.LayoutParams::class.java
                            .getDeclaredField("meizuFlags")
                    darkFlag.isAccessible = true
                    meizuFlags.isAccessible = true
                    val bit = darkFlag.getInt(null)
                    var value = meizuFlags.getInt(lp)
                    value = if (dark) {
                        value or bit
                    } else {
                        value and bit.inv()
                    }
                    meizuFlags.setInt(lp, value)
                    window.attributes = lp
                    result = true
                } catch (e: Exception) {
                }
            }
        }
        return result
    }

    /**
     * 设置状态栏字体图标为深色，需要 MIUIV6 以上
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     *
     * @return boolean 成功执行返回 true
     */
    fun MIUISetStatusBarLightMode(window: Window?, dark: Boolean): Boolean {
        var result = false
        if (isMIUI() && Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT <= 23) {
            if (window != null) {
                val clazz: Class<*> = window.javaClass
                try {
                    val darkModeFlag: Int
                    val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                    val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                    darkModeFlag = field.getInt(layoutParams)
                    val extraFlagField = clazz.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
                    if (dark) {
                        extraFlagField.invoke(window, darkModeFlag, darkModeFlag) //状态栏透明且黑色字体
                    } else {
                        extraFlagField.invoke(window, 0, darkModeFlag) //清除黑色字体
                    }
                    result = true
                } catch (e: Exception) {
                }
            }
        }
        return result
    }

    /**
     * 设置状态栏字体图标为颜色
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     *
     * @return boolean 成功执行返回 true
     */
    fun setStatusBarLightMode(window: Window, dark: Boolean) {
        if (isMIUI()) {
            MIUISetStatusBarLightMode(window, dark)
        } else if (isFlyme()) {
            FlymeSetStatusBarLightMode(window, dark)
        }
        var uiVisibility = window.decorView.systemUiVisibility
        uiVisibility = if (dark) {
            uiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            uiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
        window.decorView.systemUiVisibility = uiVisibility
    }
}
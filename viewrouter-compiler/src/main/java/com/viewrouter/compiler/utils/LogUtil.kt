package com.viewrouter.compiler.utils

import javax.annotation.processing.Messager
import javax.tools.Diagnostic

/**
 * @author lxm
 * @createtime 2021/6/8
 * log
 */
class LogUtil private constructor(){
    private lateinit var messager: Messager

    companion object {
        @JvmStatic
        val instance: LogUtil by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            LogUtil() }
    }
    fun init(messager: Messager){
        this.messager = messager
    }

    /**
     * 打印日志
     *
     * @param message 日志
     */
    fun log(message: String) {
        messager.printMessage(Diagnostic.Kind.NOTE, message)
    }

    /**
     * 打印错误日志（中断编译）
     *
     * @param message 日志
     */
    fun error(message: String) {
        messager.printMessage(Diagnostic.Kind.ERROR, message)
    }

    /**
     * 打印警告日志
     *
     * @param message 日志
     */
    fun warning(message: String) {
        messager.printMessage(Diagnostic.Kind.WARNING, message)
    }

}
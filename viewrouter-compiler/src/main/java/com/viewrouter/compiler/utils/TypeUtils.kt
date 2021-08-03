package com.viewrouter.compiler.utils

/**
 * @author lxm
 * @createtime 2021/6/8
 */
object TypeUtils {

    fun getValidClassName(type: String): Class<*> {
        return when (type) {
            "int" -> Int::class.java
            "java.lang.Integer" -> Int::class.java
            "long" -> Long::class.java
            "java.lang.Long" -> Long::class.java
            "float" -> Float::class.java
            "java.lang.Float" -> Float::class.java
            "boolean" -> Boolean::class.java
            "java.lang.Boolean" -> Boolean::class.java
            "short" -> Short::class.java
            "java.lang.Short" -> Short::class.java
            "double" -> Double::class.java
            "java.lang.Double" -> Double::class.java
            "byte" -> Byte::class.java
            "java.lang.Byte" -> Byte::class.java
            "char" -> Char::class.java
            "java.lang.Character" -> Char::class.java
            "java.lang.String" -> String::class.java
            else -> Any::class.java
        }
    }

}
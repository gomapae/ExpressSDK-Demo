package com.wendjia.base.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

val gson = Gson()
object JsonUtils {

    /**
     * 反序列化json字符串
     *
     * @param json json字符串
     * @return 对应的对象
     */
    @JvmStatic
    inline fun <reified T> parseJson(json: String): T? {
        return try {
            gson.fromJson(json, object : TypeToken<T>() {}.type)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 将对象序列化为json字符串
     *
     * @param o 对象
     * @return 对应的字符串
     */
    @JvmStatic
    fun toJson(o: Any): String {
        return gson.toJson(o)
    }

    /**
     * 根据指定的类型将对象序列化为json字符串
     *
     * @param o 对象
     * @return 对应的字符串
     */
    inline fun <reified T> toJsonByType(o: Any): String {
        return gson.toJson(o, object : TypeToken<T>() {}.type)
    }
}
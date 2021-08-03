package com.wendjia.base.utils;

import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Json工具
 *
 * @author lxm
 */
public class JsonUtil {

    private Gson gson = JsonUtilsKt.getGson();

    private JsonUtil() {
    }

    private static class InstanceHolder {

        private InstanceHolder() {
        }

        private static final JsonUtil INSTANCE = new JsonUtil();
    }

    public static JsonUtil getInstance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * 反序列化json字符串,用于解析含有泛型的类
     *
     * @param jsonString json字符串
     * @param type 类型信息
     * @return 对应的对象
     */
    @Nullable
    public <T> T parseJson(String jsonString, Type type) {
        try {
            return gson.fromJson(jsonString, type);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 反序列化json字符串
     *
     * @param json json字符串
     * @param clz 类型信息
     * @return 对应的对象
     */
    @Nullable
    public <T> T parseJson(String json, Class<T> clz) {
        try {
            return gson.fromJson(json, clz);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将对象序列化为json字符串
     *
     * @param o 对象
     * @return 对应的字符串
     */
    public String toJson(Object o) {
        return gson.toJson(o);
    }
}

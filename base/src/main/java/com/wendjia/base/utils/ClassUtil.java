package com.wendjia.base.utils;

import java.lang.reflect.ParameterizedType;

public class ClassUtil {

    // 使用反射技术得到T的真实类型
    public static Class getRealType(Class clazz){
        // 获取当前new的对象的泛型的父类类型
        ParameterizedType pt = (ParameterizedType) clazz.getGenericSuperclass();
        // 获取第一个类型参数的真实类型
        Class<?> clazzr = (Class<?>) pt.getActualTypeArguments()[0];
        return clazzr;
    }
}

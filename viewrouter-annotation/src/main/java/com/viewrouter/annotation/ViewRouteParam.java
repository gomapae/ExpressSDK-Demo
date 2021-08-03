package com.viewrouter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lxm
 * @createtime 2021/6/8
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.CLASS)
public @interface ViewRouteParam {
    String[] value();
}

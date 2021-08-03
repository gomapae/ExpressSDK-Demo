package com.viewrouter.compiler.utils;

import com.squareup.javapoet.ClassName;

/**
 * @author lxm
 * @createtime 2021/6/13
 */
public class Constants {

    public static final String TAG = "viewrouter-compiler";

    public static final String SEPARATOR = "$$";

    // System interface
    public static final String VIEW = "android.view.View";

    /**
     * router map
     */
    public static final String ROUTER_MAP = "RouterMap";

    /**
     * router suffix
     */
    public static final String ROUTER_SUFFIX = "$$Router";

    /**
     * router base package
     */
    public static final String ROUTER_BASE_PACKAGE = "gomap.android.viewrouter";

    /**
     * router base library package
     */
    public static final String ROUTER_BASE_LIBRARY_PACKAGE = "com.viewrouter.api";


    /**
     * class name list
     */
    public static class ClassNameList {
        public static final ClassName IVIEWROUTER_INTERFACE = ClassName.get(ROUTER_BASE_LIBRARY_PACKAGE, "IViewRouter");
        public static final ClassName IROUTER_MAP_INTERFACE = ClassName.get(ROUTER_BASE_LIBRARY_PACKAGE, "IRouterMap");
        public static final ClassName PAGE_INFO_CLASS = ClassName.get(ROUTER_BASE_LIBRARY_PACKAGE, "PageInfo");
        public static final ClassName VIEWROUTER_CLASS = ClassName.get(ROUTER_BASE_LIBRARY_PACKAGE, "ViewRouter");
        public static final ClassName HASHMAP_CLASS = ClassName.get("java.util", "HashMap");
        public static final ClassName ACTIVITY_CLASS = ClassName.get("android.app", "Activity");
        public static final ClassName BUNDLE_CLASS = ClassName.get("android.os", "Bundle");
    }
}

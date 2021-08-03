package com.viewrouter.model;


import com.viewrouter.annotation.ViewRoute;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Element;

/**
 * @author lxm
 * @createtime 2021/6/8
 */
public class ViewInfo {

    /**
     * View名称
     */
    private Class<?> viewClass;
    /**
     * Path of route
     */
    private String path;
    /**
     * group
     */
    private String group;
    private Element element;
    private ViewRoute route;

    /**
     */
    private final Map<String, Class> params = new HashMap<>();

    public void addParam(String key, Class className) {
        params.put(key, className);
    }

    public Map<String, Class> getParams() {
        return params;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Class<?> getViewClass() {
        return viewClass;
    }

    public void setViewClass(Class<?> viewClass) {
        this.viewClass = viewClass;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public ViewRoute getRoute() {
        return route;
    }

    public void setRoute(ViewRoute route) {
        this.route = route;
    }
}

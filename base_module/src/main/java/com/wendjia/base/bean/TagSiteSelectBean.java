package com.wendjia.base.bean;

import java.io.Serializable;

/**
 * @author coofee
 * @description 添加tag 选择地点回传数据
 * @createtime 2020/7/31 11:11
 */
public class TagSiteSelectBean implements Serializable {
    String name;
    String poiCode;
    double lat;
    double lng;
    boolean isLongSelect = false;

    public String getName() {
        return name;
    }

    public String getPoiCode() {
        return poiCode;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPoiCode(String poiCode) {
        this.poiCode = poiCode;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
    public TagSiteSelectBean(){}
    public TagSiteSelectBean(String name, String poiCode, double lat, double lng) {
        this.name = name;
        this.poiCode = poiCode;
        this.lat = lat;
        this.lng = lng;
    }

    public boolean isLongSelect() {
        return isLongSelect;
    }

    public void setLongSelect(boolean longSelect) {
        isLongSelect = longSelect;
    }
}

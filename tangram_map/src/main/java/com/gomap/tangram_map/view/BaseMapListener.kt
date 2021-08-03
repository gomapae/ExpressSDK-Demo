package com.gomap.tangram_map.view

import android.content.Context
import android.location.Location
import com.gomap.tangram_map.model.NaviRouteInfo
import com.mapzen.android.rge.TravelData
import com.wendjia.base.bean.LocationBean

/**
 * Create by lxm
 * 2020/11/16
 */
interface BaseMapListener {

    /**
     *  路径规划成功 回调
     */
    fun onBuiltRoute()

    /**
     * 规划失败
     */
    fun onCommonBuildError()

    /**
     *路线规划成功后，路线规划信息
     */
    fun onReceiveRoutingInfo(naviRouteInfoList: ArrayList<NaviRouteInfo>)

    /**
     * 操作标记点//或者点击地图点
     *
     */
    fun onFeaturePicker(searchBean: LocationBean)

    /**
     * 点击空白处
     */
    fun onFeaturePickerOut()

    /**
     * 位置更新
     */
    fun onLocationUpdated(location: Location)

    /**
     * 导航结束
     */
    fun onNaviFinish(data: TravelData)

    /**
     * 地图渲染完成
     */
    fun onMapRenderFinish()

    fun longPressResponder(searchBean: LocationBean)

    fun getMapContext():Context?

    /**
     * 导航设置按钮点击时间
     */
    fun onSettingClick()

    /**
     * 导航取消
     */
    fun onNaviCancel()

    /**
     * 地图图层类型 切换回调
     */
    fun onMapTypeChange(int: Int)

}
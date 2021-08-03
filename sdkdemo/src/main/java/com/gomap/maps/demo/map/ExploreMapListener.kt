package com.gomap.android.view.entrance.map

import android.content.Context
import com.gomap.tangram_map.model.NaviRouteInfo
import com.mapzen.android.rge.TravelData
import com.mapzen.tangram.FeaturePickResult
import com.wendjia.base.bean.SearchBean

/**
 * Create by lxm
 * 2020/11/6
 */

interface ExploreMapListener {
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
    fun onFeaturePicker(searchBean: SearchBean)
    fun longPressResponder(searchBean: SearchBean)

    /**
     * 点击空白处
     */
    fun onFeaturePickerOut()


    /**
     * 导航结束
     */
    fun onNaviFinish(data: TravelData)

    /**
     * 地图渲染完成
     */
    fun onMapRenderFinish()

    fun getMapContext(): Context?

    fun onSettingClick()

    fun onNaviCancel()

    /**
     * 地图图层类型 切换回调
     */
    fun onMapTypeChange(int: Int)

}

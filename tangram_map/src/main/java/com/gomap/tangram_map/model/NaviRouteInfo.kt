package com.gomap.tangram_map.model

import java.util.*

/**
 * @author lxm
 * @createtime 2021/5/17
 */
class NaviRouteInfo {

    var distanceShow:String ?= null
    var timeShow:String ?= null
    var trafficLightCount:Int ?= 0
    var routeDetailInfos: ArrayList<RoutingDetailData>? = null

    var isFavorite:Boolean?=false
}
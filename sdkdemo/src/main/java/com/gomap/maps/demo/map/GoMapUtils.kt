package com.gomap.maps.demo.map

import android.content.Context
import android.location.Location
import com.blankj.utilcode.util.LogUtils
import com.gomap.android.view.entrance.map.ExploreMapListener
import com.wendjia.base.bean.SearchBean
import com.gomap.tangram_map.model.NaviRouteInfo
import com.gomap.tangram_map.utils.BaseGoMapUtils
import com.gomap.tangram_map.view.BaseMapListener
import com.mapzen.android.data.request.MapType
import com.mapzen.android.graphics.MapFragment2
import com.mapzen.android.rge.TravelData
import com.mapzen.tangram.*
import com.wendjia.base.bean.LocationBean
import kotlin.collections.ArrayList

/**
 * Create by lxm
 * 2020/11/5
 */
class GoMapUtils private constructor() : BaseGoMapUtils(),FeaturePickListener {

    private var exploreMapListener: ExploreMapListener? = null

    companion object {
        val instance: GoMapUtils by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            GoMapUtils()
        }
    }

    /**
     * 注册地图监听
     */
    fun addListener(exploreMapListener: ExploreMapListener?) {
        this.exploreMapListener = exploreMapListener
        mapProxy?.addListener(baseMapListener)
    }

    override fun onFeaturePickComplete(it: FeaturePickResult?) {
        LogUtils.iTag(TAG, "onFeaturePickComplete", it)
    }

    fun initGoMap(mapFragment2: MapFragment2?){
        initGoMap(mapFragment2){
            mapProxy?.addListener(baseMapListener)
            routerProxy?.addListener(baseMapListener)
            mapFragment2?.setFeaturePickListener(this)
            mapFragment2?.setLocationListener {
                baseMapListener.onLocationUpdated(it)
            }
            gomap?.setGomapLayersTypeListener {
                var type = 0
                when (it.id) {
                    MapType.BASE_DEFAULT -> {/* 默认图层 */
                        type = 1
                    }
                    MapType.BASE_SATELLITE -> { /* 卫星图 */
                        type = 2
                    }
                    MapType.STREETVIEW -> { /* 街景 */
                        type = 3
                    }
                    MapType.HD -> { /* 高精度 */
                        type = 4
                    }
                    MapType.REALTIME -> { /* 实时交通图*/
                        type = 5
                    }
                }
                baseMapListener.onMapTypeChange(type)
            }
            /*地图渲染完成*/
            baseMapListener.onMapRenderFinish()
        }
    }

    /**
     * 退出app
     */
    override fun onDestroy() {
        super.onDestroy()
        exploreMapListener = null
    }

    private var baseMapListener = object : BaseMapListener {
        override fun onBuiltRoute() {
            exploreMapListener?.onBuiltRoute()
        }

        override fun onCommonBuildError() {
            exploreMapListener?.onCommonBuildError()
        }

        override fun onReceiveRoutingInfo(naviRouteInfoList: ArrayList<NaviRouteInfo>) {
            exploreMapListener?.onReceiveRoutingInfo(naviRouteInfoList)
        }

        override fun onFeaturePicker(locationBean: LocationBean) {
            val searchBean = SearchBean()
            searchBean.poi_code = locationBean.poi_code
            searchBean.lat = locationBean.lat.toString()
            searchBean.lng = locationBean.lng.toString()
            searchBean.subclass = locationBean.subclass
            searchBean.poiclass = locationBean.poiclass
            searchBean.name = locationBean.name
            exploreMapListener?.onFeaturePicker(searchBean)
        }

        override fun onFeaturePickerOut() {
            exploreMapListener?.onFeaturePickerOut()
        }

        override fun onLocationUpdated(location: Location) {
            LogUtils.d("GoMapUtil location = $location")

        }

        override fun onNaviFinish(data: TravelData) {
            isRouting = false
            exploreMapListener?.onNaviFinish(data)
        }

        override fun onMapRenderFinish() {
            exploreMapListener?.onMapRenderFinish()
        }

        /*长按事件也走 点击label事件*/
        override fun longPressResponder(locationBean: LocationBean) {
            val searchBean = SearchBean()
            searchBean.poi_code = locationBean.poi_code
            searchBean.lat = locationBean.lat.toString()
            searchBean.lng = locationBean.lng.toString()
            searchBean.subclass = locationBean.subclass
            searchBean.poiclass = locationBean.poiclass
            searchBean.name = locationBean.name

            exploreMapListener?.longPressResponder(searchBean)
        }

        override fun getMapContext(): Context? {
            return exploreMapListener?.getMapContext()
        }

        override fun onSettingClick() {
            exploreMapListener?.onSettingClick()
        }

        override fun onNaviCancel() {
            isRouting = false
            exploreMapListener?.onNaviCancel()
        }

        override fun onMapTypeChange(int: Int) {
            exploreMapListener?.onMapTypeChange(int)
        }

    }

    /**
     * is simulation state
     */
    fun isSimulation(context: Context): Boolean {
        return gomap?.isSimulation(context) ?: false
    }

    /**
     * save simulation state
     */
    fun saveSimulationState(context: Context, simulation: Boolean) {
        gomap?.saveSimulationState(context, simulation)
    }
}


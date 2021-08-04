package com.gomap.tangram_map.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.location.Location
import com.blankj.utilcode.util.LogUtils
import com.mapzen.android.graphics.GoMap
import com.mapzen.android.graphics.MapFragment2
import com.mapzen.android.graphics.model.BitmapMarker
import com.mapzen.android.rge.TravelData
import com.mapzen.tangram.Marker
import com.mapzen.tangram.SceneUpdate
import com.wendjia.base.bean.LocationBean
import com.wendjia.base.bean.SearchBean

/**
 * @author lxm
 * @createtime 2021/5/18
 */
open class BaseGoMapUtils {

    /**
     * 默认情况下是没有在导航中
     */
    var isRouting = false

    private val sceneUpdates = ArrayList<SceneUpdate>()

    /**
     * current drive type
     * drive bike walk
     */
    var currentDriveType = 1

    companion object {
        const val TAG = "----GoMapUtils----"

        /*默认*/
        const val MAPTYPE_DEF = 1

        /*卫星图*/
        const val MAPTYPE_SATELLITE = 2

        /*街景*/
        const val MAPTYPE_STREETVIEW = 3

        /* 高精度 */
        const val MAPTYPE_HD = 4

        /* 实时交通图*/
        const val MAPTYPE_REALTIME = 5


    }

    private var mapFragment2: MapFragment2? = null
    protected var gomap: GoMap? = null

    protected var mapProxy: MapProxy? = null
    protected var routerProxy: RouterProxy? = null

    fun initGoMap(mapFragment2: MapFragment2?,success:()->Unit) {
        this.mapFragment2 = mapFragment2
        // Create a scene update to apply our API key in the scene.
        sceneUpdates.add(SceneUpdate("global.sdk_api_key", ""))
        val millis = System.currentTimeMillis()
        mapFragment2?.getMapAsync { map ->
            val now = System.currentTimeMillis()
            val elapsedTime: Long = now - millis
            LogUtils.iTag(TAG, "onMapReady [$elapsedTime] millis")
            this.gomap = map
            mapProxy = MapProxy(map)
            routerProxy = RouterProxy(map)
           success.invoke()
        }
    }

    fun getGomapView(): GoMap? {
        return gomap
    }

    fun onResume() {
    }

    fun onPause() {
    }

    /**
     * 退出app
     */
    open fun onDestroy() {
        mapProxy?.onDestroy()
    }

    fun showFindMe() {
        getGomapView()?.showFindMe()
    }

    fun showNaviDialog(data: TravelData?) {
        getGomapView()?.showNaviDialog(data)
    }


    /**
     * 重置指南针
     */
    fun resetCompass() {
        mapProxy?.resetCompass()
    }

    fun setMyLocationIcon(bottom: Int) {
        mapProxy?.setMyLocationIcon(bottom)
    }

    fun setMyLocationEnable(enable: Boolean) {
        mapProxy?.setMyLocationEnable(enable)
    }

    /**
     * 获取地图版本code
     */
    fun getMapDataVersion(): Long {
        return 0L
    }

    fun showCurrentLocationIcon(isShow: Boolean) {
        mapProxy?.showCurrentLocationIcon(isShow)
    }

    fun getCurrentLocation(): Location? {
        return mapProxy?.getCurrentLocation()
    }

    /**
     * 获取屏幕中心点数据
     */
    fun getScreenRectCenter(): LocationBean? {
        return mapProxy?.getScreenRectCenterLocation()
    }

    /**
     * 获取屏幕xy 坐标 对应的点
     */
    fun getScreenPointLngLat(x: Float, y: Float): LocationBean? {
        return mapProxy?.getScreenPointLngLat(x, y)
    }
    fun addBitmapMarker(searchBean: SearchBean?,imageDrable: Drawable): BitmapMarker? {
        val locationBean = LocationBean()
        locationBean.lat = searchBean?.getDLat()
        locationBean.lng = searchBean?.getDLng()
        locationBean.name = searchBean?.name
        locationBean.poi_code = searchBean?.poi_code
        locationBean.poiclass = searchBean?.poiclass
        locationBean.subclass = searchBean?.subclass
         return mapProxy?.addBitmapMarker(locationBean,imageDrable)
    }
    /**
     * 添加标记点
     */
    fun addMarker(searchBean: SearchBean?): Marker? {
        val locationBean = LocationBean()
        locationBean.lat = searchBean?.getDLat()
        locationBean.lng = searchBean?.getDLng()
        locationBean.name = searchBean?.name
        locationBean.poi_code = searchBean?.poi_code
        locationBean.poiclass = searchBean?.poiclass
        locationBean.subclass = searchBean?.subclass
        return mapProxy?.addMarker(locationBean)
    }

    /**
     * 移除标记点
     * 移除所有的选择标记点
     */
    fun removeAllMarkers() {
        mapProxy?.removeMarker()
    }
    fun removeBitmapMaker(bitmapMarker:BitmapMarker){
        mapProxy?.removeBitmapMarker(bitmapMarker)
    }

    /**
     * 添加单个标记点并移动到中心
     */
    fun addMarkersAndMoveToCenter(site: SearchBean?) {
        addMarkerAndMoveToCenter(site, false)
    }

    private fun addMarkerAndMoveToCenter(site: SearchBean?, animal: Boolean) {
        site?.let {
            val addMarker = addMarker(site)
            site.markerId = addMarker?.markerId ?: -1
            if (animal) {
                moveToCenter(site, 300)
            } else {
                moveToCenter(site, 0)
            }
        }
    }

    /**
     * 移动到某一个点
     */
    private fun moveToCenter(searchBean: SearchBean?, duration: Int) {
        val locationBean = LocationBean()
        locationBean.lat = searchBean?.getDLat()
        locationBean.lng = searchBean?.getDLng()
        locationBean.name = searchBean?.name
        locationBean.poi_code = searchBean?.poi_code
        locationBean.poiclass = searchBean?.poiclass
        locationBean.subclass = searchBean?.subclass

        mapProxy?.moveToCenter(locationBean, duration)
    }

    fun moveToCenter(dest: SearchBean?) {
        moveToCenter(dest, 0)
    }

    /**
     * 路线规划
     */
    fun startRouter(siteList: List<SearchBean>, typeIndex : Int) {
        currentDriveType = typeIndex
        val list = ArrayList<LocationBean>()
        siteList.forEach { searchBean->
            val locationBean = LocationBean()
            locationBean.lat = searchBean.getDLat()
            locationBean.lng = searchBean.getDLng()
            locationBean.name = searchBean.name
            locationBean.poi_code = searchBean.poi_code
            locationBean.poiclass = searchBean.poiclass
            locationBean.subclass = searchBean.subclass

            list.add(locationBean)
        }
        //路线规划前，先移除所有的点
        removeAllMarkers()
        routerProxy?.startRouter(list, typeIndex)

    }

    /**
     * 点击开始导航
     */
    fun startNavi() {
        isRouting = true
        routerProxy?.startNavi()
    }

    /**
     *  取消路线规划规划
     */
    fun cancelRouting(){
        getGomapView()?.cancelPlanRoute()
        isRouting = false
    } /**
     * 取消导航
     */

    fun cancelNavi(){
        getGomapView()?.endNavi()
        isRouting = false
    }

    fun clearDroppedPins() {
        gomap?.clearDroppedPins()
    }

    /**
     * hide maptype popupwindow
     */
    fun hideMapTypePop(): Boolean {
        return gomap?.hideMapTypePop() ?: false
    }

    /**
     * show/hide mapTypeLayerButton
     */
    fun showMapTypeButton(show: Boolean) {
        gomap?.setLayersIconEnable(show)
    }

    /**
     * 设置当前位置的状态
     */
    fun setCurrentPositionState(state: Int) {
        gomap?.setCurrentPositionState(state)
    }

    /**
     * map type dark style code
     * 0 default; 1 day; 2 night
     * @return return dark style code
     */
    fun getMaptypeDarkStyleCode(context: Context): Int {
        return gomap?.getMaptypeDarkStyleCode(context)?:0
    }

    /**
     * save maptype dark style code
     * 0 default; 1 day; 2 night
     *
     * @param context  the context
     * @param darkCode dark code
     */
    fun saveMaptyleDarkStyleCode(context: Context, darkCode: Int) {
        gomap?.saveMaptyleDarkStyleCode(context, darkCode)
    }

    /**
     * reload yaml style
     * add by coofee on 20210413
     */
    fun reloadYamlStyle() {
        gomap?.reloadYamlStyle()
    }

}
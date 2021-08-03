package com.gomap.tangram_map.utils

import android.graphics.Bitmap
import android.graphics.PointF
import android.graphics.drawable.Drawable
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ScreenUtils
import com.gomap.tangram_map.R
import com.gomap.tangram_map.model.LngLatBounds
import com.gomap.tangram_map.view.BaseMapListener
import com.mapzen.android.graphics.GoMap
import com.mapzen.android.graphics.model.BitmapMarker
import com.mapzen.android.graphics.model.BitmapMarkerOptions
import com.mapzen.android.listener.TouchPanListener
import com.mapzen.tangram.ILnglat
import com.mapzen.tangram.LngLat
import com.mapzen.tangram.LngLat4Category
import com.mapzen.tangram.Marker
import com.mapzen.tangram.TouchInput.*
import com.wendjia.base.BaseApplication
import com.wendjia.base.bean.LocationBean
import kotlin.math.abs


/**
 * Create by lxm
 * 2020/11/10
 */
class MapProxy(private var map: GoMap?) : BaseProxy(map) {

    private var baseMapListener: BaseMapListener? = null

    companion object {
        const val ZOOM_INIT_START = 16F
        const val ZOOM_MARKER_SELECT = 18F
        const val ZOOM_RECT_CENTER = 12F

        const val MY_LOCATION_ICON_DEFAULT_BOTTOM = 50
        const val MY_LOCATION_ICON_BOTTOM_NAVI = 150
        const val MY_LOCATION_ICON_BOTTOM_BRIEF = 150

        private val DEFAULT_DISTANCE = 500 //单位 m
        private var isCountMapMoveDistance = true

    }

    init {
        initMap()
    }

    /**
     * 注册地图监听
     */
    fun addListener(exploreMapListener: BaseMapListener?) {
        this.baseMapListener = exploreMapListener
    }

    private fun initMap() {

        configureMap()

        setListener()

    }

    /**
     * 初始化配置
     */
    private fun configureMap() {
        map?.isMyLocationEnabled = true
        map?.setCompassButtonEnabled(true)
//        map?.setZoomButtonsEnabled(true)
        map?.setPersistMapState(true)
        map?.setLayersIconEnable(true)

        setMyLocationIcon(50)
        //bottom 是离屏幕中间的距离 单位dp
//        map?.setZoomOutPosition(40)
        //设置点击地图区域大小 dp
        map?.mapController?.setPickRadius(10f)
        map?.zoom = ZOOM_INIT_START
    }

    /**
     * 设置mylocation 图标的位置
     * dp
     */
    fun setMyLocationIcon(bottom: Int) {
        //bottom是离底部的值 单位dp
        map?.setFindMePosition(bottom)
    }

    fun setMyLocationEnable(enable: Boolean) {
        map?.isMyLocationEnabled = enable
    }

    private fun setListener() {
        map?.setLabelPickListener {
            LogUtils.iTag("lxm MapProxy setLabelPickListener", it)
            /*
            * notes: 1,如果点击的label没有poi_code则认为点击了空白区域 added by coofee on 2020年11月24日
            * 2,点击的label如若name为空则用class代替，eg:停车场
            * */
            if (it != null && !it.properties["poi_code"].isNullOrEmpty()) {
                val coordinates = it.coordinates
                val properties = it.properties
                val nameDe = properties["name_de"]
                val subclass = properties["subclass"]
                var className = properties["class"]
                var rank = properties["rank"]
                val poiCode = properties["poi_code"]
                val searchBean = LocationBean()
                searchBean.poi_code = poiCode
                searchBean.lat = coordinates.latitude
                searchBean.lng = coordinates.longitude
                searchBean.name = if (!nameDe.isNullOrEmpty()) nameDe else className
                searchBean.subclass = subclass
                searchBean.poiclass = className
                searchBean.isMarker = false

                baseMapListener?.onFeaturePicker(searchBean)
            } else {
                baseMapListener?.onFeaturePickerOut()
            }
        }

        map?.longPressResponder = LongPressResponder { x, y ->
            val extra = "x:$x y:$y"
            LogUtils.iTag("lxm MapProxy longPressResponder", extra)
            val searchBean = LocationBean()
            val lnglat = screenPositionToLngLat(x, y)
            searchBean.lat = lnglat?.latitude
            searchBean.lng = lnglat?.longitude
            searchBean.name = BaseApplication.getInstance()?.getString(R.string.unknonwn_place)
            baseMapListener?.longPressResponder(searchBean)
        }

    }

    /**
     * 坐标转化成经纬度
     */
    private fun screenPositionToLngLat(positionX: Float, positionY: Float): LngLat? {
        return map?.screenPositionToLngLat(PointF(positionX, positionY))
    }

    /**
     * 获取屏幕中心点坐标
     * getScreenRectCenterLocation 替代
     */
    private fun getScreenCenterLngLat(): LocationBean? {
        val appScreenWidth = ScreenUtils.getAppScreenWidth()
        val appScreenHeight = ScreenUtils.getAppScreenHeight()
        return getScreenPointLngLat((appScreenWidth / 2).toFloat(),(appScreenHeight / 2).toFloat())
    }

    /**
     * 获取屏幕xy 坐标 对应的点坐标
     */
    fun getScreenPointLngLat(x:Float,y:Float): LocationBean? {
        var locationBean =LocationBean()
        val screenPositionToLngLat = map?.screenPositionToLngLat(PointF(x, y))
        locationBean.lat = screenPositionToLngLat?.latitude?:0.0
        locationBean.lng = screenPositionToLngLat?.longitude?:0.0
        return locationBean
    }

    fun showCurrentLocationIcon(isShow: Boolean) {
        if (isShow) {
            map?.showLocationIcon()
        } else {
            map?.hideLocationIcon()
        }
    }

    /**
     *
     */
    fun getScreenRectCenterLocation(): LocationBean {

        val position = map?.position
        val locationBean = LocationBean()
        locationBean.lng = position?.longitude
        locationBean.lat = position?.latitude
        return locationBean
    }

    /**
     * 指南针复位
     */
    fun resetCompass() {
        map?.resetRotation()
    }

    /**
     * 移除Marker
     */
    fun removeMarker() {
        map?.removeMarker()
    }
    fun removeMaker(markerId:Long){
        map?.removeMarker(markerId)
    }

    fun removeBitmapMarker(bitmapMarker:BitmapMarker){
        map?.removeBitmapMarker(bitmapMarker)
    }

    /**
     * 添加单个marker
     */
    fun addMarker(searchBean: LocationBean): Marker? {
        return map?.addMarker(LngLat4Category(searchBean.lng?:0.0, searchBean.lat?:0.0))
    }

    fun addBitmapMarker(searchBean: LocationBean,drawable: Drawable):BitmapMarker?{
        val bitmapMarkerOptions =
            BitmapMarkerOptions().position(LngLat(searchBean.lng ?: 0.0, searchBean.lat ?: 0.0))
                .icon(drawable)
        return map?.addBitmapMarker(bitmapMarkerOptions)
    }

    /**
     * 移动到地图中心
     */
    fun moveToCenter(dest: LocationBean, duration: Int) {
        map?.zoom = ZOOM_MARKER_SELECT
        map?.setPosition(LngLat(dest.lng?:0.0, dest.lat?:0.0), duration)
    }

    /**
     * 移动到地图中心
     */
    fun moveToCenter(dest: LocationBean?, zoom: Float?, duration: Int) {
        dest?.let {
            zoom?.let {
                map?.zoom = it
            }
            map?.setPosition(LngLat(it.lng?:0.0, it.lat?:0.0), duration)
        }
    }

    fun onDestroy() {
        try {
            map?.removeMarker()
        } catch (e: Exception) {
        }
    }
}
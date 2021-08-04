package com.gomap.tangram_map.utils

import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import androidx.annotation.DimenRes
import com.blankj.utilcode.util.LogUtils
import com.gomap.tangram_map.R
import com.gomap.tangram_map.model.NaviRouteInfo
import com.gomap.tangram_map.model.RoutingDetailData
import com.gomap.tangram_map.view.BaseMapListener
import com.mapzen.android.data.NavigationResult
import com.mapzen.android.graphics.GoMap
import com.mapzen.android.rge.OnNavListener
import com.mapzen.android.rge.TravelData
import com.mapzen.android.util.DistanceUtils
import com.mapzen.tangram.LngLat
import com.wendjia.base.BaseApplication
import com.wendjia.base.bean.LocationBean
import com.wendjia.base.utils.ThreadUtils
import jni.bean.*
import jni.bean.RouteDetailInfo
import kotlin.math.ceil

/**
 * Create by lxm
 * 2020/11/12
 */
class RouterProxy(private var map: GoMap?) : BaseProxy(map) {
    private var baseMapListener: BaseMapListener? = null

    init {
        init()
    }

    private fun init() {

        map?.setOnNavListener(object : OnNavListener {
            override fun onNaviCancel() {
                LogUtils.iTag("GomapMap setOnNavListener onCancel")
                baseMapListener?.onNaviCancel()
                map?.showFindMe()
            }

            override fun onSelectPlanRoute(p0: Route?) {
                //地图点击切换路线功能 废弃
            }

            override fun onNaviUpdate(p0: NavigationResult?) {
            }

            override fun onPlanRouteSuccess(p0: RoutesInfo?) {
                LogUtils.iTag("GomapMap setOnNavListener onPlanRouteSuccess")
                ThreadUtils.runOnUi(Runnable {
                    baseMapListener?.onBuiltRoute()

                    val naviRouteInfoList = ArrayList<NaviRouteInfo>()
                    p0?.routesInfo?.forEach {
                        val naviRouteInfo = NaviRouteInfo()
                        naviRouteInfo.distanceShow = DistanceUtils.reformatTotalSurplusDistance(it.distance)
                        naviRouteInfo.timeShow = formatRoutingTime(it.time)
                        naviRouteInfo.trafficLightCount = it.trafficLightCount

                        val routingDetailDataList = ArrayList<RoutingDetailData>()
                        it.routeDetailInfos?.forEach { routeDetailInfo ->
                            val routingDetailData = RoutingDetailData()
                            routingDetailData.name = routeDetailInfo.name

                            val detailType = routeDetailInfo.detailType
                            val arrowType = routeDetailInfo.arrowType

                            routingDetailData.distanceShow = getDistanceShow(routeDetailInfo.distance,detailType, arrowType)
                            routingDetailData.imgRes = getRoutingImage(detailType, arrowType)
                            routingDetailDataList.add(routingDetailData)
                        }
                        naviRouteInfo.routeDetailInfos = routingDetailDataList
                        naviRouteInfoList.add(naviRouteInfo)
                    }
                    baseMapListener?.onReceiveRoutingInfo(naviRouteInfoList)
                })

            }

            override fun onPlanRouteFailure() {
                LogUtils.iTag("GomapMap setOnNavListener onPlanRouteFailure")
                ThreadUtils.runOnUi(Runnable {
                    baseMapListener?.onCommonBuildError()
                })
            }

            override fun onSettingClick() {
                baseMapListener?.onSettingClick()
            }

            override fun onNavFinish(data: TravelData) {
                LogUtils.iTag("GomapMap setOnNavListener onNavFinish")
                baseMapListener?.onNaviFinish(data)
            }
        })

    }

    private fun formatRoutingTime(seconds: Double): String? {

        val totalMin = ceil(seconds / 60.0)
        var minutes = totalMin % 60
        val hours = totalMin / 60
        val min = BaseApplication.getInstance().getString(R.string.minute)
        val hour = BaseApplication.getInstance().getString(R.string.hour)
        if (seconds <= 60) {
            minutes = 1.0
        }
        val displayedH = formatUnitsText(R.dimen.dp_16, R.dimen.dp_16, hours.toInt().toString(), hour)
        val displayedM = formatUnitsText(R.dimen.dp_16, R.dimen.dp_16, minutes.toInt().toString(), min)
        return (if (hours.toInt() == 0) displayedM?.toString() else TextUtils.concat(*arrayOf("$displayedH ", displayedM))).toString()
    }

    private fun formatUnitsText(@DimenRes size: Int, @DimenRes units: Int, dimension: String, unitText: String?): SpannableStringBuilder? {
        val res = SpannableStringBuilder(dimension).append(" ").append(unitText)
        res.setSpan(AbsoluteSizeSpan(BaseApplication.getInstance()?.resources?.getDimensionPixelSize(size)
                ?: 0, false), 0, dimension.length, 33)
        res.setSpan(AbsoluteSizeSpan(BaseApplication.getInstance()?.resources?.getDimensionPixelSize(units)
                ?: 0, false), dimension.length, res.length, 33)
        return res
    }

    /**
     * 注册地图监听
     */
    fun addListener(exploreMapListener: BaseMapListener?) {
        this.baseMapListener = exploreMapListener
    }

    fun startRouter(searchBeanList: List<LocationBean>, typeIndex : Int) {
        val arrayList = arrayOfNulls<Poi>(searchBeanList.size)
        for (i in searchBeanList.indices) {
            val searchBean = searchBeanList[i]
            val poi = Poi().apply {
                setPt(LngLat(searchBean.lng ?: 0.0, searchBean.lat ?: 0.0))
                name = searchBean.name
            }
            arrayList[i] = poi
        }
//        map?.cancelPlanRoute()
//        map?.planRoute(DrivingType.getDrivingType(typeIndex), *arrayList)
        planRoute(DrivingType.getDrivingType(typeIndex), arrayList)
    }

    private fun planRoute(drivingType:DrivingType,arrayList:Array<Poi?>){

        var start = arrayList.get(0)!!
        var list  = ArrayList<Poi>()

        for (i in 1 until arrayList.size){
            list.add(arrayList[i]!!)
        }
        map?.planRoute(start, list)
    }

    fun cancelRouting(){
        map?.cancelPlanRoute()
    }
    fun cancelNavi(){
        map?.endNavi()
    }

    fun startNavi() {
        LogUtils.iTag("GomapMap startNavi")
        map?.startNavi()
        map?.hideFindMe()
    }

    /**
     * 导航中
     */
    fun isNavigating(): Boolean {
        return map?.onNavigation() ?: false
    }

    /**
     * 路线规划中
     */
    fun isPlanning(): Boolean {
        return map?.onPlanRoute() ?: false
    }

    /**
     * deprecated by coofee on 20210502
     * 导航中不进行路线规划动作（路线规划，构建路线中）
     */
    @Deprecated("same with isPlanning")
    fun isBuilding(): Boolean {
        return map?.onGetRouteData() ?: false
    }

    /**
     * 判断地图底层是否活跃（持续导航判断）
     * 暂时不动 先返回true
     */
    fun isRoutingActive(): Boolean {
        return true
    }

    private fun getRoutingImage(detailType: RouteDetailInfo.RouteDetailType?, arrowType: RouteDetailInfo.RouteArrowType?): Int {

        return when (detailType) {
            RouteDetailInfo.RouteDetailType.START -> {
                R.drawable.explore_navi_start_image
            }
            RouteDetailInfo.RouteDetailType.ENTER_ROUND_ABOUT -> {
                R.drawable.explore_icon_left_uturn
            }
            RouteDetailInfo.RouteDetailType.EXIT_ROUND_ABOUT -> {
                R.drawable.explore_icon_left_uturn
            }
            RouteDetailInfo.RouteDetailType.NORMAL -> {
                when (arrowType) {
                    RouteDetailInfo.RouteArrowType.LEFT -> {
                        R.drawable.explore_icon_left
                    }
                    RouteDetailInfo.RouteArrowType.SLIGHT_LEFT -> {
                        R.drawable.explore_icon_left_slight
                    }
                    RouteDetailInfo.RouteArrowType.SHARP_LEFT -> {
                        R.drawable.explore_icon_left_sharp
                    }
                    RouteDetailInfo.RouteArrowType.RIGHT -> {
                        R.drawable.explore_icon_right
                    }
                    RouteDetailInfo.RouteArrowType.SLIGHT_RIGHT -> {
                        R.drawable.explore_icon_right_slight
                    }
                    RouteDetailInfo.RouteArrowType.SHARP_RIGHT -> {
                        R.drawable.explore_icon_right_sharp
                    }
                    RouteDetailInfo.RouteArrowType.STRAIGHT -> {
                        R.drawable.explore_icon_straight
                    }
                    RouteDetailInfo.RouteArrowType.UTURN -> {
                        R.drawable.explore_icon_left_uturn
                    }
                    else -> {
                        R.drawable.explore_navi_start_image
                    }
                }
            }
            RouteDetailInfo.RouteDetailType.WAY_POINT -> {
                R.drawable.explore_navi_start_image
            }
            RouteDetailInfo.RouteDetailType.ARRIVE -> {
                R.drawable.explore_navi_end_image
            }
            else -> {
                R.drawable.explore_navi_start_image
            }
        }
    }

    fun getDistanceShow(distance:Double,detailType: RouteDetailInfo.RouteDetailType?, arrowType: RouteDetailInfo.RouteArrowType?):String{

       return when (detailType) {
            RouteDetailInfo.RouteDetailType.START -> BaseApplication.getInstance().resources.getString(R.string.start_from_my_location)
            RouteDetailInfo.RouteDetailType.WAY_POINT -> {
                when (arrowType) {
                    RouteDetailInfo.RouteArrowType.LEFT -> {
                        val str = BaseApplication.getInstance().getString(R.string.left)
                        return BaseApplication.getInstance().getString(R.string.destination_propmt,str)
                    }
                    RouteDetailInfo.RouteArrowType.RIGHT -> {
                        val str = BaseApplication.getInstance().getString(R.string.right)
                        return BaseApplication.getInstance().getString(R.string.destination_propmt,str)
                    }
                    RouteDetailInfo.RouteArrowType.NONE -> {
                        val str = BaseApplication.getInstance().getString(R.string.front)
                        return BaseApplication.getInstance().getString(R.string.destination_propmt,str)
                    }
                    else -> ""
                }
            }
            RouteDetailInfo.RouteDetailType.ARRIVE -> {
                when (arrowType) {
                    RouteDetailInfo.RouteArrowType.LEFT -> {
                        val str = BaseApplication.getInstance().getString(R.string.left)
                        return BaseApplication.getInstance().getString(R.string.destination_propmt,str)
                    }
                    RouteDetailInfo.RouteArrowType.RIGHT -> {
                        val str = BaseApplication.getInstance().getString(R.string.right)
                        return BaseApplication.getInstance().getString(R.string.destination_propmt,str)
                    }
                    RouteDetailInfo.RouteArrowType.NONE -> {
                        val str = BaseApplication.getInstance().getString(R.string.front)
                        return BaseApplication.getInstance().getString(R.string.destination_propmt,str)
                    }
                    else -> ""
                }
            }
            RouteDetailInfo.RouteDetailType.ENTER_ROUND_ABOUT,
            RouteDetailInfo.RouteDetailType.EXIT_ROUND_ABOUT,
            RouteDetailInfo.RouteDetailType.NORMAL -> DistanceUtils.reformatTotalSurplusDistance(distance)
            else -> ""

        }
    }

}
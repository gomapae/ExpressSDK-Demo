package com.gomap.maps.demo.map

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ToastUtils
import com.gomap.android.view.entrance.map.ExploreMapListener
import com.gomap.maps.demo.R
import com.gomap.maps.demo.routing.RoutingView
import com.gomap.maps.demo.databinding.AppLayoutMapViewBinding
import com.gomap.maps.demo.selectpoint.SelectPointView
import com.gomap.tangram_map.model.NaviRouteInfo
import com.mapzen.android.graphics.MapFragment2
import com.mapzen.android.rge.TravelData
import com.wendjia.base.bean.SearchBean
import com.wendjia.base.bean.TagSiteSelectBean
import com.wendjia.base.utils.JsonUtils
import com.wendjia.base.view.BaseAppCompatActivity
import com.wendjia.base.view.IBaseView
import com.wendjia.base.viewtask.ViewTaskManager

/**
 * @author lxm
 * @createtime 2021/7/2
 */
class MapView: FrameLayout, ExploreMapListener {

    private lateinit var binding : AppLayoutMapViewBinding

    private var mMapCallBack: MapCallBack?=null

    constructor(context:Context):this(context,null)
    constructor(context:Context, attrs: AttributeSet?):this(context,attrs,-1)
    constructor(context:Context, attrs: AttributeSet?, defStyleAttr:Int?=-1):super(context,attrs,defStyleAttr?:-1){
        initView()
    }

    private fun initView(){
        binding = AppLayoutMapViewBinding.inflate(LayoutInflater.from(context), this, true)
        binding.lifecycleOwner = (context as IBaseView).getLifecycleOwner()
    }

    fun initMap(mMapCallBack: MapCallBack){
        this.mMapCallBack = mMapCallBack
        //初始化地图
        val mapFragment = (context as BaseAppCompatActivity).supportFragmentManager.findFragmentById(R.id.fragment) as MapFragment2?
        GoMapUtils.instance.initGoMap(mapFragment)
        GoMapUtils.instance.addListener(this)

    }

    //路径规划成功 回调
    override fun onBuiltRoute() {
        val currentIView = ViewTaskManager.getInstance().getCurrentIView()
        if (currentIView is RoutingView){
            currentIView.setRoutingResult(true)
        }
    }

    override fun onCommonBuildError() {
        val currentIView = ViewTaskManager.getInstance().getCurrentIView()
        if (currentIView is RoutingView){
            currentIView.setRoutingResult(false)
        }
    }

    override fun onReceiveRoutingInfo(naviRouteInfoList: ArrayList<NaviRouteInfo>) {
        val currentIView = ViewTaskManager.getInstance().getCurrentIView()
        if (currentIView is RoutingView){
            currentIView.setRoutingResultData(naviRouteInfoList)
        }
    }

    override fun longPressResponder(site: SearchBean) {

        GoMapUtils.instance.addMarker(site)

        val data = TagSiteSelectBean(site.name, site.getPoiCode(), site.getDLat(), site.getDLng())
        data.isLongSelect = true

        val currentIView = ViewTaskManager.getInstance().getCurrentIView()
        if (currentIView is SelectPointView){
            currentIView.markersInfo(
                JsonUtils.toJson(data))
        }
    }
    //需要注意
    override fun onFeaturePicker(site: SearchBean) {
        val currentIView = ViewTaskManager.getInstance().getCurrentIView()
        if (currentIView is SelectPointView){
            //暂时先处理一种非 选点情况；只在选点页面店家maker
            val data = TagSiteSelectBean(site.name, site.getPoiCode(), site.getDLat(), site.getDLng())

            currentIView.markersInfo(JsonUtils.toJson(data))
        }
    }
    /**
     * 暂时 空白处，首页（非导航中）， 路线规划页面 ，搜索结果页
     */
    override fun onFeaturePickerOut() {
        GoMapUtils.instance.removeAllMarkers()
        val currentIView = ViewTaskManager.getInstance().getCurrentIView()
        if (currentIView is SelectPointView){
            currentIView.markersInfo(null)
        }
    }

    /**
     * 导航结束回调
     */
    override fun onNaviFinish(data: TravelData) {
        GoMapUtils.instance.showFindMe()
        //显示导航结果 弹窗，可自定义
        GoMapUtils.instance.showNaviDialog(data)
        val currentIView = ViewTaskManager.getInstance().getCurrentIView()
        if (currentIView is RoutingView){
            currentIView.onNaviFinish()
        }
    }

    override fun onMapRenderFinish() {
        ToastUtils.showShort("map Render Finish")
    }

    override fun getMapContext(): Context? {
        return context
    }

    override fun onSettingClick() {
        ToastUtils.showShort("navi setting")
    }

    /**
     * 点击了 地图本身的结束导航
     */
    override fun onNaviCancel() {
        ToastUtils.showShort("navi cancel")
    }

    override fun onMapTypeChange(int: Int) {
    }

    private fun <T:Any> getServiceClass(service: Class<out T>): T {
        return ARouter.getInstance().navigation(service)
    }

    fun onResume() {
        GoMapUtils.instance.onResume()
    }
    fun onStop() {

    }

    fun onPause() {
        GoMapUtils.instance.onPause()
    }
    fun onDestroy() {
        GoMapUtils.instance.onDestroy()
    }

}

interface MapCallBack{

   fun  onNaviCancel()
   fun  onNaviFinish(data: TravelData)
   fun  onMapRenderFinish()

}
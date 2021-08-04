package com.gomap.maps.demo.routing

import android.content.Intent
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ToastUtils
import com.gomap.maps.demo.databinding.RoutingViewBinding
import com.gomap.maps.demo.map.GoMapUtils
import com.gomap.maps.demo.selectpoint.SelectPointView
import com.gomap.tangram_map.model.NaviRouteInfo
import com.wendjia.base.bean.SearchBean
import com.wendjia.base.utils.JsonUtils
import com.wendjia.base.view.IBaseView
import com.wendjia.base.viewbinding.BaseMVVMView
import com.wendjia.base.viewbinding.BaseViewModel
import com.wendjia.base.viewtask.ViewTaskManager

/**
 * @author lxm
 * @createtime 2021/8/2
 */
class RoutingView(var iBaseViewOwner: IBaseView): BaseMVVMView<RoutingViewBinding, BaseViewModel>(iBaseViewOwner.getContext()),
    RoutingViewListener {

    private var mStopPointListAdapter : StopPointListAdapter?= null

    private var selectStartPoint  = true

    private var start:SearchBean?=null

    override fun initData() {
        super.initData()
        binding.listener = this

    }

    override fun initUIView() {
        super.initUIView()
        mStopPointListAdapter = StopPointListAdapter()
        binding.routingViewRecyclerview.adapter = mStopPointListAdapter
    }

    override fun getBindingT(): RoutingViewBinding {
        return RoutingViewBinding.inflate(LayoutInflater.from(iBaseViewOwner.getContext()),this,true)
    }

    override fun getModelM(): BaseViewModel {
        return ViewModelProvider(iBaseViewOwner.getViewModelStoreOwner()).get(BaseViewModel::class.java)
    }

    override fun onBack() {
        listener?.onBack()
    }

    override fun selectPoint() {
        selectStartPoint = true

        ViewTaskManager.getInstance().startView(SelectPointView(iBaseViewOwner),true)

    }

    override fun onViewResult(data: Intent?) {
        super.onViewResult(data)

        if (data != null){
            val stringExtra = data.getStringExtra("data")
            val parseJson = JsonUtils.parseJson<SearchBean>(stringExtra)
            if (selectStartPoint){
                start = parseJson
                binding.routingViewTxtStart.text = parseJson?.name + "(" +parseJson?.lat +"," +parseJson?.lng+")"
            }else {
                mStopPointListAdapter?.add(parseJson!!)
            }
        }

    }

    fun  setRoutingResult(isSuccess:Boolean){
        if (isSuccess){
            ToastUtils.showShort("routing success")
        }else {
            ToastUtils.showShort("routing failed")
        }
    }
    fun setRoutingResultData(naviRouteInfoList: ArrayList<NaviRouteInfo>){

    }
    fun onNaviFinish(){
        ToastUtils.showShort("navi finish")
    }

    override fun addStopPoint() {
        selectStartPoint = false
        ViewTaskManager.getInstance().startView(SelectPointView(iBaseViewOwner),true)
    }

    override fun startRouting() {

        var list = ArrayList<SearchBean>()
        list.add(start!!)
        list.addAll(mStopPointListAdapter?.data!!)

        GoMapUtils.instance.startRouter(list,1)

    }

    override fun cancelRouting() {
        GoMapUtils.instance.cancelRouting()
    }

    override fun startSimulationNavi() {
        GoMapUtils.instance.saveSimulationState(context!!,true)
    }

    override fun startNavi() {
        GoMapUtils.instance.startNavi()
    }

    override fun cancelNavi() {
        GoMapUtils.instance.cancelNavi()
    }

}

interface RoutingViewListener{

    fun onBack()
    fun selectPoint()
    fun addStopPoint()
    fun startRouting()
    fun cancelRouting()
    fun startSimulationNavi()
    fun startNavi()
    fun cancelNavi()

}
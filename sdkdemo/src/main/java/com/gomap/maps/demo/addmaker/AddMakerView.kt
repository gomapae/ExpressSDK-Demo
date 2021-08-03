package com.gomap.maps.demo.addmaker

import android.view.LayoutInflater
import com.gomap.maps.demo.R
import com.gomap.maps.demo.databinding.AddMarkerViewBinding
import com.gomap.maps.demo.map.GoMapUtils
import com.mapzen.android.graphics.model.BitmapMarker
import com.viewrouter.annotation.ViewRoute
import com.wendjia.base.bean.SearchBean
import com.wendjia.base.utils.JsonUtils
import com.wendjia.base.view.IBaseView
import com.wendjia.base.viewbinding.BaseMVVMView
import com.wendjia.base.viewbinding.BaseViewModel

/**
 * @author lxm
 * @createtime 2021/8/3
 */
@ViewRoute(path = "AddMaker",group = "sdkdemo")
class AddMakerView(var iBaseViewOwner: IBaseView): BaseMVVMView<AddMarkerViewBinding, BaseViewModel>(iBaseViewOwner.getContext()),AddMakerViewListener{

    override fun initData() {
        super.initData()
        binding.listener = this

    }

    override fun initUIView() {
        super.initUIView()

    }

    override fun getBindingT(): AddMarkerViewBinding {
        return AddMarkerViewBinding.inflate(LayoutInflater.from(iBaseViewOwner.getContext()),this,true)
    }

    override fun getModelM(): BaseViewModel {
        return BaseViewModel()
    }

    override fun onBack() {
        listener?.onBack()
    }

    override fun addMarker() {
        var lng = 54.36432342947051
        var lat = 24.482680006952112

        var searchBean = SearchBean()
        searchBean.lng = lng.toString()
        searchBean.lat = lat.toString()
        GoMapUtils.instance.addMarker(searchBean)

    }

    override fun movetoCenter() {
        var lng = 54.36432342947051
        var lat = 24.482680006952112

        var searchBean = SearchBean()
        searchBean.lng = lng.toString()
        searchBean.lat = lat.toString()

        //move point  to screen center
        GoMapUtils.instance.moveToCenter(searchBean)
    }

    private var btmapMarker : BitmapMarker?=null

    override fun addMarkerDefineImage() {
        var lng = 54.36432342947051
        var lat = 24.482680006952112

        var searchBean = SearchBean()
        searchBean.lng = lng.toString()
        searchBean.lat = lat.toString()
        val drawable = context?.resources?.getDrawable(R.mipmap.icon_test_drive,null)
        btmapMarker = GoMapUtils.instance.addBitmapMarker(searchBean, drawable!!)
    }

    override fun removeMarker() {
        if (btmapMarker != null){
            GoMapUtils.instance.removeBitmapMaker(btmapMarker!!)
        }
        GoMapUtils.instance.removeAllMarkers()
    }

}

interface AddMakerViewListener {

    fun onBack()
    fun addMarker()
    fun addMarkerDefineImage()
    fun movetoCenter()
    fun removeMarker()

}
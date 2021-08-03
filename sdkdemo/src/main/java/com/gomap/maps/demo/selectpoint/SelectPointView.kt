package com.gomap.maps.demo.selectpoint

import android.content.Intent
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import com.gomap.maps.demo.databinding.ViewSelectPointBinding
import com.gomap.maps.demo.map.GoMapUtils
import com.viewrouter.annotation.ViewRoute
import com.viewrouter.annotation.ViewRouteParam
import com.wendjia.base.bean.SearchBean
import com.wendjia.base.bean.TagSiteSelectBean
import com.wendjia.base.utils.JsonUtils
import com.wendjia.base.view.IBaseView
import com.wendjia.base.viewbinding.BaseMVVMView

@ViewRoute(path = "SelectPointView",group = "app")
class SelectPointView(var iBaseViewOwner: IBaseView) : BaseMVVMView<ViewSelectPointBinding, ViewSelectPointViewModel>(iBaseViewOwner.getContext()) {

    @JvmField
    @ViewRouteParam("support_long_select")
    var supportLongSelect:Boolean?=true

    override fun getBindingT(): ViewSelectPointBinding {
        return ViewSelectPointBinding.inflate(LayoutInflater.from(iBaseViewOwner.getContext()), this, true)
    }

    override fun getModelM(): ViewSelectPointViewModel {
        return ViewModelProvider(iBaseViewOwner.getViewModelStoreOwner()).get(ViewSelectPointViewModel::class.java)
    }

    var tagSiteSelectBean: TagSiteSelectBean? = null
    override fun initUIView() {
        super.initUIView()

        binding.btnOk.setOnClickListener{
            val data = model.data.get()
            if (data != null){
                val intent = Intent()
                intent.putExtra("data",JsonUtils.toJson(data))
                listener?.setViewResult(intent)
            }else {
                listener?.setViewResult(null)
            }
            back()
        }
        binding.ivBack.setOnClickListener{
            back()
        }

        binding.btnOk.isClickable = false
        binding.btnOk.isEnabled = false

    }

    override fun onBackPressed() {
        listener?.onBack()
    }

    fun markersInfo(data: String?) {
        if (data != null){
            tagSiteSelectBean = JsonUtils.parseJson<TagSiteSelectBean>(data)

            val searchBean = SearchBean()
            searchBean.lat = tagSiteSelectBean?.lat.toString()
            searchBean.lng = tagSiteSelectBean?.lng.toString()
            searchBean.poi_code = tagSiteSelectBean?.poiCode
            searchBean.name = tagSiteSelectBean?.name
            GoMapUtils.instance.addMarker(searchBean)

            //默认支持长按选点
            if (tagSiteSelectBean?.isLongSelect == false || supportLongSelect == true){
                model.onclickable.set(true)
                model.data.set(searchBean)
                binding.btnOk.isClickable = true
                binding.btnOk.isEnabled = true
                binding.btnOk.isSelected = true
            }else if (tagSiteSelectBean?.isLongSelect == true && supportLongSelect == false){
                listener?.removeMakers()
            }
        }else {
            model.onclickable.set(false)
            model.data.set(null)

            binding.btnOk.isClickable = false
            binding.btnOk.isEnabled = false
            binding.btnOk.isSelected = false
            tagSiteSelectBean = null
        }
    }

    private fun back(){

        GoMapUtils.instance.removeAllMarkers()
        binding.btnOk.isClickable = false
        binding.btnOk.isEnabled = false

        listener?.onBack()
    }

}
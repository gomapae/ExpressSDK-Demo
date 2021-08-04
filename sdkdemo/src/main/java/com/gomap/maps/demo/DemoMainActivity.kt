package com.gomap.maps.demo

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ToastUtils
import com.gomap.maps.demo.addmaker.AddMakerView
import com.gomap.maps.demo.databinding.ActivityMainBinding
import com.gomap.maps.demo.map.MapCallBack
import com.gomap.maps.demo.routing.RoutingView
import com.gomap.maps.demo.selectpoint.SelectPointView
import com.mapzen.android.rge.TravelData
import com.wendjia.base.bean.SearchBean
import com.wendjia.base.utils.JsonUtils
import com.wendjia.base.viewbinding.BaseActivity
import com.wendjia.base.viewtask.ViewTaskManager
import com.wendjia.base.viewtask.ViewTaskManagerCallBack

class DemoMainActivity : BaseActivity<ActivityMainBinding, DemoMainViewModel>(),
    ViewTaskManagerCallBack {

    override fun initData() {
        super.initData()
    }

    override fun initUIView() {
        super.initUIView()
        ViewTaskManager.init(this, binding.viewTaskContainer, this);
        binding.layoutActivityMainMapview.initMap(object : MapCallBack {
            override fun onNaviCancel() {
            }

            override fun onNaviFinish(data: TravelData) {
            }

            override fun onMapRenderFinish() {

            }
        })
        binding.activityMainBtnRouting.setOnClickListener {
            val routingView = RoutingView(this)
            ViewTaskManager.getInstance().startView(routingView, true)
        }

        binding.activityMainBtnAddMarker.setOnClickListener {
            val addMakerView = AddMakerView(this)
            ViewTaskManager.getInstance().startView(addMakerView, true)
        }

        binding.activityMainBtnSelectPoint.setOnClickListener {
            val selectPointView = SelectPointView(this)
            ViewTaskManager.getInstance().startView(selectPointView, true)
        }
    }

    override fun getBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(LayoutInflater.from(this))
    }

    override fun getModel(): DemoMainViewModel {
        return ViewModelProvider(this).get(DemoMainViewModel::class.java)
    }

    override fun onBackPressed() {
        if (ViewTaskManager.getInstance().onBackPressed()) {
            return
        }
        super.onBackPressed()
    }

    override fun getCurrentTabPosition(): Int {
        return 0
    }

    override fun viewTaskChange(size: Int, tabPosition: Int) {
        if (size == 0) {
            binding.activityMainMain.setVisibility(View.VISIBLE)
        }
    }

    override fun backToActivity() {
    }

    override fun removeMakers() {
    }

    override fun onViewResult(intent: Intent?) {
        val data = intent?.getStringExtra("data")
        val searchBean = JsonUtils.parseJson<SearchBean>(data ?: "")
        ToastUtils.showShort("name:"+searchBean?.name +"\n" +"lat:"+searchBean?.lat +"\n"+"lng:"+searchBean?.lng)
    }

    override fun needSelectPointFromMap(isNavigationIn: Boolean) {
        binding.activityMainMain.setVisibility(View.GONE)
    }
}

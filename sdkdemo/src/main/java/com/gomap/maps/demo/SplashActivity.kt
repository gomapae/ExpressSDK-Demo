package com.gomap.maps.demo

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ToastUtils
import com.gomap.maps.demo.databinding.ActivitySplashBinding
import com.wendjia.base.utils.PermissionListener
import com.wendjia.base.utils.SystemPermissionUtils
import com.wendjia.base.viewbinding.BaseActivity
import com.wendjia.base.viewbinding.BaseViewModel

class SplashActivity : BaseActivity<ActivitySplashBinding,BaseViewModel>() {


    override fun initData() {
        super.initData()

    }

    override fun initUIView() {
        super.initUIView()

        val arrayOf = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)

        SystemPermissionUtils.checkPermission(this,
            arrayOf,object : PermissionListener() {
                override fun checkPermissionSuccess(isSuccess: Boolean) {
                    super.checkPermissionSuccess(isSuccess)

                    if (isSuccess){
                        gotoMain()
                    }else {
                        ToastUtils.showShort("please open permission")
                        finish()
                    }
                }
            })
    }

    private fun gotoMain(){
        startActivity(Intent(this,DemoMainActivity::class.java))
        finish()
    }

    override fun getBinding(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(LayoutInflater.from(this))
    }

    override fun getModel(): BaseViewModel {
        return ViewModelProvider(this).get(BaseViewModel::class.java)
    }
}
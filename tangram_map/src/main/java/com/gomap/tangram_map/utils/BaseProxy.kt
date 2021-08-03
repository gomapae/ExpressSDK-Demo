package com.gomap.tangram_map.utils

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.mapzen.android.graphics.GoMap
import com.wendjia.base.BaseApplication

/**
 * Create by lxm
 * 2020/11/20
 */
open class BaseProxy(private var map: GoMap?) {

    fun getCurrentLocation(): Location? {
        //需要初始化LostApiClient ，否则报错
        return try {
            map?.lastKnowLocation
        } catch (e: Exception) {
            //会绕过 模拟位置
            if (ActivityCompat.checkSelfPermission(BaseApplication.getInstance(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                null
            }else{
                com.mapzen.android.util.LocationUtils.getInstance().getCurrentLocation(BaseApplication.getInstance())
            }
        }
    }
}
package com.wendjia.base.utils

import android.Manifest
import android.annotation.SuppressLint
import com.tbruyelle.rxpermissions2.RxPermissions
import com.wendjia.base.view.*

/**
 * Create by lxm
 * 2020/11/4
 */
const val PERMISSION_GRANTED = 0
const val PERMISSION_ASK_AGAIN = 1
const val PERMISSION_NEED_SETTING = 2

object SystemPermissionUtils {

    private fun getPermissions(iBaseView: IBaseView):RxPermissions?{
        return when (iBaseView) {
            is RxActivity -> {
                RxPermissions(iBaseView)
            }
            is RxFragment -> {
                RxPermissions(iBaseView)
            }
            else -> {
                 null
            }
        }
    }

    @SuppressLint("CheckResult")
    fun checkCameraPermission(iBaseView: IBaseView, permissionListener: PermissionListener?) {
        val rxPermissions = getPermissions(iBaseView)
        if (rxPermissions == null){
            permissionListener?.checkPermissionSuccess(false)
            return
        }
        rxPermissions.requestEach(
                Manifest.permission.CAMERA)
                .subscribe({ permission ->
                    var isSuccess = false
                    when {
                        permission.granted -> {
                            isSuccess = true
                            // `permission.name` is granted !
                            permissionListener?.checkPermissionResult(PERMISSION_GRANTED)
                            permissionListener?.checkPermissionGrand()
                        }
                        permission.shouldShowRequestPermissionRationale -> {
                            // Denied permission without ask never again
                            permissionListener?.checkPermissionResult(PERMISSION_ASK_AGAIN)
                            permissionListener?.checkPermissionAskAgain()
                        }
                        else -> {
                            // Denied permission with ask never again
                            // Need to go to the settings
                            permissionListener?.checkPermissionResult(PERMISSION_NEED_SETTING)
                            permissionListener?.checkPermissionNeedSetting()
                        }
                    }
                    permissionListener?.checkPermissionSuccess(isSuccess)
                }, {
                    permissionListener?.checkPermissionSuccess(false)
                })
    }
    @SuppressLint("CheckResult")
    fun checkAudioPermission(iBaseView: IBaseView, permissionListener: PermissionListener?) {
        val rxPermissions = getPermissions(iBaseView)
        if (rxPermissions == null){
            permissionListener?.checkPermissionSuccess(false)
            return
        }
        rxPermissions.requestEach(Manifest.permission.RECORD_AUDIO)
                .subscribe({ permission ->
                    var isSuccess = false
                    when {
                        permission.granted -> {
                            isSuccess = true
                            // `permission.name` is granted !
                            permissionListener?.checkPermissionResult(PERMISSION_GRANTED)
                            permissionListener?.checkPermissionGrand()
                        }
                        permission.shouldShowRequestPermissionRationale -> {
                            // Denied permission without ask never again
                            permissionListener?.checkPermissionResult(PERMISSION_ASK_AGAIN)
                            permissionListener?.checkPermissionAskAgain()
                        }
                        else -> {
                            // Denied permission with ask never again
                            // Need to go to the settings
                            permissionListener?.checkPermissionResult(PERMISSION_NEED_SETTING)
                            permissionListener?.checkPermissionNeedSetting()
                        }
                    }
                    permissionListener?.checkPermissionSuccess(isSuccess)
                }, {
                    permissionListener?.checkPermissionSuccess(false)
                })
    }
    @SuppressLint("CheckResult")
    fun checkPermission(iBaseView: IBaseView,permissions: Array<String>, permissionListener: PermissionListener?) {
        val rxPermissions = getPermissions(iBaseView)
        if (rxPermissions == null){
            permissionListener?.checkPermissionSuccess(false)
            return
        }
        rxPermissions.request(*permissions)
                .subscribe({ isSuccess ->
                    permissionListener?.checkPermissionSuccess(isSuccess)
                    if (isSuccess){
                        permissionListener?.checkPermissionGrand()
                    }else{
                        permissionListener?.checkPermissionAskAgain()
                    }
                },{
                    permissionListener?.checkPermissionSuccess(false)
                })
    }

    @SuppressLint("CheckResult")
    fun checkEachPermission(iBaseView: IBaseView,permissions: Array<String>, permissionListener: PermissionListener?) {
        val rxPermissions = getPermissions(iBaseView)
        if (rxPermissions == null){
            permissionListener?.checkPermissionSuccess(false)
            return
        }
        rxPermissions.requestEach(*permissions)
                .subscribe({ permission ->
                    var isSuccess = false
                    when {
                        permission.granted -> {
                            isSuccess = true
                            // `permission.name` is granted !
                            permissionListener?.checkPermissionResult(PERMISSION_GRANTED)
                            permissionListener?.checkPermissionGrand()
                        }
                        permission.shouldShowRequestPermissionRationale -> {
                            // Denied permission without ask never again
                            permissionListener?.checkPermissionResult(PERMISSION_ASK_AGAIN)
                            permissionListener?.checkPermissionAskAgain()
                        }
                        else -> {
                            // Denied permission with ask never again
                            // Need to go to the settings
                            permissionListener?.checkPermissionResult(PERMISSION_NEED_SETTING)
                            permissionListener?.checkPermissionNeedSetting()
                        }
                    }
                    permissionListener?.checkPermissionSuccess(isSuccess)
                }, {
                    permissionListener?.checkPermissionSuccess(false)
                })
    }

    @SuppressLint("CheckResult")
    fun checkPermissionCombined(iBaseView: IBaseView,permissions: Array<String>, permissionListener: PermissionListener?) {
        val rxPermissions = getPermissions(iBaseView)
        if (rxPermissions == null){
            permissionListener?.checkPermissionSuccess(false)
            return
        }
        rxPermissions.requestEachCombined(*permissions)
                .subscribe({ permission ->
                    var isSuccess = false
                    when {
                        permission.granted -> {
                            isSuccess = true
                            // `permission.name` is granted !
                            permissionListener?.checkPermissionResult(PERMISSION_GRANTED)
                            permissionListener?.checkPermissionGrand()
                        }
                        permission.shouldShowRequestPermissionRationale -> {
                            // Denied permission without ask never again
                            permissionListener?.checkPermissionResult(PERMISSION_ASK_AGAIN)
                            permissionListener?.checkPermissionAskAgain()
                        }
                        else -> {
                            // Denied permission with ask never again
                            // Need to go to the settings
                            permissionListener?.checkPermissionResult(PERMISSION_NEED_SETTING)
                            permissionListener?.checkPermissionNeedSetting()
                        }
                    }
                    permissionListener?.checkPermissionSuccess(isSuccess)
                }, {
                    permissionListener?.checkPermissionSuccess(false)
                })
    }

}

abstract class PermissionListener {
    open fun checkPermissionResult(result: Int){}
    open fun checkPermissionGrand(){}
    open fun checkPermissionAskAgain(){}
    open fun checkPermissionNeedSetting(){}
    open fun checkPermissionSuccess(isSuccess:Boolean){}

}
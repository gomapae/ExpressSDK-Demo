package com.wendjia.base.view

import android.content.Intent
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.CheckResult
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.LogUtils
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.RxLifecycle
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.android.RxLifecycleAndroid
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

/**
 * Create by lxm
 * 2020/8/24
 */
const val TAG_LIFECYCLE = "lifecycle"
abstract class RxActivity : AppCompatActivity(), LifecycleProvider<ActivityEvent> {

    /**
     * 生命周期分发
     */
    private val lifecycleBehaviorSubject = BehaviorSubject.create<ActivityEvent>()

    @CheckResult
    override fun lifecycle(): Observable<ActivityEvent> {
        return lifecycleBehaviorSubject.hide()
    }

    @CheckResult
    override fun <T : Any?> bindToLifecycle(): LifecycleTransformer<T> {
        return RxLifecycleAndroid.bindActivity(lifecycleBehaviorSubject)
    }

    @CheckResult
    override fun <T : Any?> bindUntilEvent(event: ActivityEvent): LifecycleTransformer<T> {
        return RxLifecycle.bindUntilEvent(lifecycleBehaviorSubject, event)
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        LogUtils.iTag(TAG_LIFECYCLE, javaClass.simpleName + " onCreate")
        lifecycleBehaviorSubject.onNext(ActivityEvent.CREATE)
        super.onCreate(savedInstanceState)
    }

    override fun onRestart() {
        super.onRestart()
        LogUtils.iTag(TAG_LIFECYCLE, javaClass.simpleName + " onRestart")
    }

    @CallSuper
    override fun onStart() {
        LogUtils.iTag(TAG_LIFECYCLE, javaClass.simpleName + " onStart")
        lifecycleBehaviorSubject.onNext(ActivityEvent.START)
        super.onStart()
    }

    @CallSuper
    override fun onResume() {
        LogUtils.iTag(TAG_LIFECYCLE, javaClass.simpleName + " onResume")
        lifecycleBehaviorSubject.onNext(ActivityEvent.RESUME)
        super.onResume()
    }

    @CallSuper
    override fun onPause() {
        LogUtils.iTag(TAG_LIFECYCLE, javaClass.simpleName + " onPause")
        lifecycleBehaviorSubject.onNext(ActivityEvent.PAUSE)
        super.onPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        LogUtils.iTag(TAG_LIFECYCLE, javaClass.simpleName + " onActivityResult")
    }

    @CallSuper
    override fun onStop() {
        LogUtils.iTag(TAG_LIFECYCLE, javaClass.simpleName + " onStop")
        lifecycleBehaviorSubject.onNext(ActivityEvent.STOP)
        super.onStop()
    }

    @CallSuper
    override fun onDestroy() {
        LogUtils.iTag(TAG_LIFECYCLE, javaClass.simpleName + " onDestroy")
        lifecycleBehaviorSubject.onNext(ActivityEvent.DESTROY)
        super.onDestroy()
    }

}
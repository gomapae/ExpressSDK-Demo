package com.wendjia.base.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.CheckResult
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.LogUtils
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.RxLifecycle
import com.trello.rxlifecycle2.android.FragmentEvent
import com.trello.rxlifecycle2.android.RxLifecycleAndroid
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

/**
 * Create by lxm
 * 2020/8/24
 */
const val TAG_FRAGMENT_LIFECYCLE = "fragment_lifecycle"
abstract class RxFragment : Fragment(), LifecycleProvider<FragmentEvent> {

    /**
     * 生命周期分发
     */
    private val lifecycleBehaviorSubject = BehaviorSubject.create<FragmentEvent>()

    @CheckResult
    override fun lifecycle(): Observable<FragmentEvent> {
        return lifecycleBehaviorSubject.hide()
    }

    @CheckResult
    override fun <T : Any?> bindUntilEvent(event: FragmentEvent): LifecycleTransformer<T> {
        return RxLifecycle.bindUntilEvent(lifecycleBehaviorSubject, event)

    }

    @CheckResult
    override fun <T : Any?> bindToLifecycle(): LifecycleTransformer<T> {
        return RxLifecycleAndroid.bindFragment(lifecycleBehaviorSubject)
    }

    override fun onAttach(context: Context) {
        lifecycleBehaviorSubject.onNext(FragmentEvent.ATTACH)
        super.onAttach(context)
        LogUtils.iTag(TAG_FRAGMENT_LIFECYCLE, javaClass.simpleName + " onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        lifecycleBehaviorSubject.onNext(FragmentEvent.CREATE)
        super.onCreate(savedInstanceState)
        LogUtils.iTag(TAG_FRAGMENT_LIFECYCLE, javaClass.simpleName + " onCreate")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleBehaviorSubject.onNext(FragmentEvent.CREATE_VIEW)
        super.onViewCreated(view, savedInstanceState)
        LogUtils.iTag(TAG_FRAGMENT_LIFECYCLE, javaClass.simpleName + " onViewCreated")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        LogUtils.iTag(TAG_FRAGMENT_LIFECYCLE, javaClass.simpleName + " onActivityCreated")
    }

    override fun onStart() {
        lifecycleBehaviorSubject.onNext(FragmentEvent.START)
        super.onStart()
        LogUtils.iTag(TAG_FRAGMENT_LIFECYCLE, javaClass.simpleName + " onStart")
    }

    override fun onResume() {
        lifecycleBehaviorSubject.onNext(FragmentEvent.RESUME)
        super.onResume()
        LogUtils.iTag(TAG_FRAGMENT_LIFECYCLE, javaClass.simpleName + " onResume")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        LogUtils.iTag(TAG_FRAGMENT_LIFECYCLE, javaClass.simpleName + " onActivityResult")
    }

    override fun onPause() {
        lifecycleBehaviorSubject.onNext(FragmentEvent.PAUSE)
        super.onPause()
        LogUtils.iTag(TAG_FRAGMENT_LIFECYCLE, javaClass.simpleName + " onPause")
    }

    override fun onStop() {
        lifecycleBehaviorSubject.onNext(FragmentEvent.STOP)
        super.onStop()
        LogUtils.iTag(TAG_FRAGMENT_LIFECYCLE, javaClass.simpleName + " onStop")
    }

    override fun onDestroyView() {
        lifecycleBehaviorSubject.onNext(FragmentEvent.DESTROY_VIEW)
        super.onDestroyView()
        LogUtils.iTag(TAG_FRAGMENT_LIFECYCLE, javaClass.simpleName + " onDestroyView")
    }

    override fun onDestroy() {
        lifecycleBehaviorSubject.onNext(FragmentEvent.DESTROY)
        super.onDestroy()
        LogUtils.iTag(TAG_FRAGMENT_LIFECYCLE, javaClass.simpleName + " onDestroy")
    }

    override fun onDetach() {
        lifecycleBehaviorSubject.onNext(FragmentEvent.DETACH)
        super.onDetach()
        LogUtils.iTag(TAG_FRAGMENT_LIFECYCLE, javaClass.simpleName + " onDetach")
    }
}
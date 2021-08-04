package com.wendjia.base.viewbinding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.LogUtils
import com.wendjia.base.viewtask.IView
import com.wendjia.base.utils.AnimUtil
import com.wendjia.base.view.IBaseView
import com.wendjia.base.viewtask.ViewListener

/**
 * @description base view
 * @author shenfei.wang@g42.ai
 * @createtime 2021/1/20 18:35
 */
const val TAG_VIEW_LIFECYCLE = "view lifecycle"
abstract class BaseMVVMView<T : ViewBinding, M : BaseViewModel>(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int? = -1) :
        ConstraintLayout(context, attrs, defStyleAttr ?: -1), IView, IBaseView {


    constructor(context: Context?):this(context,null,-1)

    lateinit var binding: T
    lateinit var model: M

    open var listener: ViewListener?= null

    override fun onCreate() {
        LogUtils.iTag(TAG_VIEW_LIFECYCLE, javaClass.simpleName + " onCreate")
        binding = getBindingT()
        model = getModelM()
        initData()
        initUIView()
        fetchData()
    }

    override fun setBundleData(bundle: Bundle) {
    }

    override fun onReStart() {
        LogUtils.iTag(TAG_VIEW_LIFECYCLE, javaClass.simpleName + " onReStart")
    }

    override fun onResume() {
        LogUtils.iTag(TAG_VIEW_LIFECYCLE, javaClass.simpleName + " onResume")
    }
    override fun onPause() {
        LogUtils.iTag(TAG_VIEW_LIFECYCLE, javaClass.simpleName + " onPause")
    }
    override fun onStop() {
        LogUtils.iTag(TAG_VIEW_LIFECYCLE, javaClass.simpleName + " onStop")
    }
    override fun onDestroy() {
        LogUtils.iTag(TAG_VIEW_LIFECYCLE, javaClass.simpleName + " onDestroy")

    }

    override fun onViewResult(data: Intent?) {
        LogUtils.iTag(TAG_VIEW_LIFECYCLE, javaClass.simpleName + " onViewResult")
    }

    /**
     * 打开activity的回调
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        LogUtils.iTag(TAG_VIEW_LIFECYCLE, javaClass.simpleName + " onActivityResult")
    }

    override fun onBackPressed() {
        LogUtils.iTag(TAG_VIEW_LIFECYCLE, javaClass.simpleName + " onBackPressed")
    }

    override fun initData() {}
    override fun initUIView() {}
    override fun fetchData() {}
    protected abstract fun getBindingT(): T
    protected abstract fun getModelM(): M

    override fun viewShowNum(): Int {
        return -1
    }

    override fun showLoadingView() {}
    override fun hideLoadingView() {}

    companion object {
        val TAG = BaseMVVMView::class.java.simpleName
    }


    private fun showAnimation() {
        val animation = AnimUtil.getBottomInAnimation(200)
        animation.fillAfter = false
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                binding.root.clearAnimation()
                binding.root.invalidate()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        binding.root.startAnimation(animation)
    }

    //TODO 容器中单view show 和hide 方式是否有必要
    override fun show(isAnimotion: Boolean) {
        visibilityView()
        if (isAnimotion) {
            showAnimation()
        }
    }

    private fun visibilityView() {
        if (binding.root.visibility == View.GONE) {
            binding.root.visibility = VISIBLE
        }
    }

    override fun hide(isAnimation: Boolean, hide: (isFinish:Boolean) -> Unit) {

        if (isAnimation){
            val animation = AnimUtil.getBottomOutAnimation(200)
            animation.fillAfter = false
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    binding.root.clearAnimation()
                    binding.root.visibility = GONE
                    hide.invoke(true)
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
            binding.root.startAnimation(animation)
        }else {
            binding.root.clearAnimation()
            binding.root.visibility = GONE
            hide.invoke(true)
        }

    }

    fun setViewListener(listener: ViewListener) {
        this.listener = listener
    }

    override fun isAlive(): Boolean {
        return true
    }

    override fun getIBaseView(): IBaseView {
        return this
    }

    override fun getViewModelStoreOwner(): ViewModelStoreOwner {
        return context as ViewModelStoreOwner
    }

    override fun getLifecycleOwner(): LifecycleOwner {
        return context as LifecycleOwner
    }

    override fun showErrorMessage(message: String?) {
    }

    private var mServiceClassMap = HashMap<String,Any>()

    fun <T:Any> getServiceClass(service: Class<out T>): T {
        if (mServiceClassMap.containsKey(service.simpleName)){
            return mServiceClassMap[service.simpleName] as T
        }
        val navigation = ARouter.getInstance().navigation(service)
        mServiceClassMap[service.simpleName] = navigation
        return navigation
    }
}
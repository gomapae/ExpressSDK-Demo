package com.wendjia.base.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import com.alibaba.android.arouter.launcher.ARouter

/**
 * Create by lxm
 * 2020/8/24
 */
abstract class BaseRxFragment: Fragment(), IBaseView {

    private var context: Context ?= null

    private var hidden = false

    companion object {
        private const val STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN"

        fun <T : BaseRxFragment> newInstance(clazz: Class<*>, bundle: Bundle?): T? {
            var fragment: T? = null
            try {
                fragment = clazz.newInstance() as T
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: java.lang.InstantiationException) {
                e.printStackTrace()
            }
            fragment!!.arguments = bundle
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            val isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN)
            val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
            if (isSupportHidden) {
                ft.hide(this)
            } else {
                ft.show(this)
            }
            ft.commit()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden);
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        ARouter.getInstance().inject(this)

        if (initTheme() != 0) {
            getContext()?.theme?.applyStyle(initTheme(), true)
        }
        val view = onDefineCreateView(inflater,container,savedInstanceState)
        return view
    }

    open fun onDefineCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
        initUIView()
        fetchData()
    }

    override fun initData() {
    }

    override fun initUIView() {
    }

    override fun fetchData() {
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        this.hidden = hidden
    }


    protected open fun initTheme(): Int {
        return 0
    }

    override fun showErrorMessage(message: String?) {
    }

    override fun showLoadingView() {
    }

    override fun hideLoadingView() {
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun isAlive(): Boolean = isAdded && !isHidden;

    override fun getContext(): Context? = context

    override fun getIBaseView(): IBaseView = this
    override fun getViewModelStoreOwner(): ViewModelStoreOwner {
        return this
    }
    override fun getLifecycleOwner(): LifecycleOwner {
        return this
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